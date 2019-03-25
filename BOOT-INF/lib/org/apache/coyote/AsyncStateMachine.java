/*     */ package org.apache.coyote;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.concurrent.Executor;
/*     */ import org.apache.tomcat.util.net.AbstractEndpoint.Handler.SocketState;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ import org.apache.tomcat.util.security.PrivilegedGetTccl;
/*     */ import org.apache.tomcat.util.security.PrivilegedSetTccl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AsyncStateMachine
/*     */ {
/* 142 */   private static final StringManager sm = StringManager.getManager(AsyncStateMachine.class);
/*     */   
/*     */   private static enum AsyncState {
/* 145 */     DISPATCHED(false, false, false, false), 
/* 146 */     STARTING(true, true, false, false), 
/* 147 */     STARTED(true, true, false, false), 
/* 148 */     MUST_COMPLETE(true, true, true, false), 
/* 149 */     COMPLETE_PENDING(true, true, false, false), 
/* 150 */     COMPLETING(true, false, true, false), 
/* 151 */     TIMING_OUT(true, true, false, false), 
/* 152 */     MUST_DISPATCH(true, true, false, true), 
/* 153 */     DISPATCH_PENDING(true, true, false, false), 
/* 154 */     DISPATCHING(true, false, false, true), 
/* 155 */     READ_WRITE_OP(true, true, false, false), 
/* 156 */     MUST_ERROR(true, true, false, false), 
/* 157 */     ERROR(true, true, false, false);
/*     */     
/*     */     private final boolean isAsync;
/*     */     private final boolean isStarted;
/*     */     private final boolean isCompleting;
/*     */     private final boolean isDispatching;
/*     */     
/*     */     private AsyncState(boolean isAsync, boolean isStarted, boolean isCompleting, boolean isDispatching)
/*     */     {
/* 166 */       this.isAsync = isAsync;
/* 167 */       this.isStarted = isStarted;
/* 168 */       this.isCompleting = isCompleting;
/* 169 */       this.isDispatching = isDispatching;
/*     */     }
/*     */     
/*     */     public boolean isAsync() {
/* 173 */       return this.isAsync;
/*     */     }
/*     */     
/*     */     public boolean isStarted() {
/* 177 */       return this.isStarted;
/*     */     }
/*     */     
/*     */     public boolean isDispatching() {
/* 181 */       return this.isDispatching;
/*     */     }
/*     */     
/*     */     public boolean isCompleting() {
/* 185 */       return this.isCompleting;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/* 190 */   private volatile AsyncState state = AsyncState.DISPATCHED;
/* 191 */   private volatile long lastAsyncStart = 0L;
/*     */   
/* 193 */   private AsyncContextCallback asyncCtxt = null;
/*     */   private final AbstractProcessor processor;
/*     */   
/*     */   public AsyncStateMachine(AbstractProcessor processor)
/*     */   {
/* 198 */     this.processor = processor;
/*     */   }
/*     */   
/*     */   public boolean isAsync()
/*     */   {
/* 203 */     return this.state.isAsync();
/*     */   }
/*     */   
/*     */   public boolean isAsyncDispatching() {
/* 207 */     return this.state.isDispatching();
/*     */   }
/*     */   
/*     */   public boolean isAsyncStarted() {
/* 211 */     return this.state.isStarted();
/*     */   }
/*     */   
/*     */   public boolean isAsyncTimingOut() {
/* 215 */     return this.state == AsyncState.TIMING_OUT;
/*     */   }
/*     */   
/*     */   public boolean isAsyncError() {
/* 219 */     return this.state == AsyncState.ERROR;
/*     */   }
/*     */   
/*     */   public boolean isCompleting() {
/* 223 */     return this.state.isCompleting();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getLastAsyncStart()
/*     */   {
/* 234 */     return this.lastAsyncStart;
/*     */   }
/*     */   
/*     */   public synchronized void asyncStart(AsyncContextCallback asyncCtxt) {
/* 238 */     if (this.state == AsyncState.DISPATCHED) {
/* 239 */       this.state = AsyncState.STARTING;
/* 240 */       this.asyncCtxt = asyncCtxt;
/* 241 */       this.lastAsyncStart = System.currentTimeMillis();
/*     */     }
/*     */     else {
/* 244 */       throw new IllegalStateException(sm.getString("asyncStateMachine.invalidAsyncState", new Object[] { "asyncStart()", this.state }));
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized void asyncOperation()
/*     */   {
/* 250 */     if (this.state == AsyncState.STARTED) {
/* 251 */       this.state = AsyncState.READ_WRITE_OP;
/*     */     }
/*     */     else {
/* 254 */       throw new IllegalStateException(sm.getString("asyncStateMachine.invalidAsyncState", new Object[] { "asyncOperation()", this.state }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized AbstractEndpoint.Handler.SocketState asyncPostProcess()
/*     */   {
/* 265 */     if (this.state == AsyncState.COMPLETE_PENDING) {
/* 266 */       doComplete();
/* 267 */       return AbstractEndpoint.Handler.SocketState.ASYNC_END; }
/* 268 */     if (this.state == AsyncState.DISPATCH_PENDING) {
/* 269 */       doDispatch();
/* 270 */       return AbstractEndpoint.Handler.SocketState.ASYNC_END; }
/* 271 */     if ((this.state == AsyncState.STARTING) || (this.state == AsyncState.READ_WRITE_OP)) {
/* 272 */       this.state = AsyncState.STARTED;
/* 273 */       return AbstractEndpoint.Handler.SocketState.LONG; }
/* 274 */     if ((this.state == AsyncState.MUST_COMPLETE) || (this.state == AsyncState.COMPLETING)) {
/* 275 */       this.asyncCtxt.fireOnComplete();
/* 276 */       this.state = AsyncState.DISPATCHED;
/* 277 */       return AbstractEndpoint.Handler.SocketState.ASYNC_END; }
/* 278 */     if (this.state == AsyncState.MUST_DISPATCH) {
/* 279 */       this.state = AsyncState.DISPATCHING;
/* 280 */       return AbstractEndpoint.Handler.SocketState.ASYNC_END; }
/* 281 */     if (this.state == AsyncState.DISPATCHING) {
/* 282 */       this.state = AsyncState.DISPATCHED;
/* 283 */       return AbstractEndpoint.Handler.SocketState.ASYNC_END; }
/* 284 */     if (this.state == AsyncState.STARTED)
/*     */     {
/*     */ 
/* 287 */       return AbstractEndpoint.Handler.SocketState.LONG;
/*     */     }
/*     */     
/* 290 */     throw new IllegalStateException(sm.getString("asyncStateMachine.invalidAsyncState", new Object[] { "asyncPostProcess()", this.state }));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public synchronized boolean asyncComplete()
/*     */   {
/* 297 */     if ((!ContainerThreadMarker.isContainerThread()) && (this.state == AsyncState.STARTING)) {
/* 298 */       this.state = AsyncState.COMPLETE_PENDING;
/* 299 */       return false;
/*     */     }
/* 301 */     return doComplete();
/*     */   }
/*     */   
/*     */ 
/*     */   private synchronized boolean doComplete()
/*     */   {
/* 307 */     clearNonBlockingListeners();
/* 308 */     boolean doComplete = false;
/* 309 */     if ((this.state == AsyncState.STARTING) || (this.state == AsyncState.TIMING_OUT) || (this.state == AsyncState.ERROR) || (this.state == AsyncState.READ_WRITE_OP))
/*     */     {
/* 311 */       this.state = AsyncState.MUST_COMPLETE;
/* 312 */     } else if ((this.state == AsyncState.STARTED) || (this.state == AsyncState.COMPLETE_PENDING)) {
/* 313 */       this.state = AsyncState.COMPLETING;
/* 314 */       doComplete = true;
/*     */     }
/*     */     else {
/* 317 */       throw new IllegalStateException(sm.getString("asyncStateMachine.invalidAsyncState", new Object[] { "asyncComplete()", this.state }));
/*     */     }
/*     */     
/* 320 */     return doComplete;
/*     */   }
/*     */   
/*     */   public synchronized boolean asyncTimeout()
/*     */   {
/* 325 */     if (this.state == AsyncState.STARTED) {
/* 326 */       this.state = AsyncState.TIMING_OUT;
/* 327 */       return true; }
/* 328 */     if ((this.state == AsyncState.COMPLETING) || (this.state == AsyncState.DISPATCHING) || (this.state == AsyncState.DISPATCHED))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 333 */       return false;
/*     */     }
/*     */     
/* 336 */     throw new IllegalStateException(sm.getString("asyncStateMachine.invalidAsyncState", new Object[] { "asyncTimeout()", this.state }));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public synchronized boolean asyncDispatch()
/*     */   {
/* 343 */     if ((!ContainerThreadMarker.isContainerThread()) && (this.state == AsyncState.STARTING)) {
/* 344 */       this.state = AsyncState.DISPATCH_PENDING;
/* 345 */       return false;
/*     */     }
/* 347 */     return doDispatch();
/*     */   }
/*     */   
/*     */ 
/*     */   private synchronized boolean doDispatch()
/*     */   {
/* 353 */     clearNonBlockingListeners();
/* 354 */     boolean doDispatch = false;
/* 355 */     if ((this.state == AsyncState.STARTING) || (this.state == AsyncState.TIMING_OUT) || (this.state == AsyncState.ERROR))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 360 */       this.state = AsyncState.MUST_DISPATCH;
/* 361 */     } else if ((this.state == AsyncState.STARTED) || (this.state == AsyncState.DISPATCH_PENDING)) {
/* 362 */       this.state = AsyncState.DISPATCHING;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 369 */       doDispatch = true;
/* 370 */     } else if (this.state == AsyncState.READ_WRITE_OP) {
/* 371 */       this.state = AsyncState.DISPATCHING;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 376 */       if (!ContainerThreadMarker.isContainerThread()) {
/* 377 */         doDispatch = true;
/*     */       }
/*     */     }
/*     */     else {
/* 381 */       throw new IllegalStateException(sm.getString("asyncStateMachine.invalidAsyncState", new Object[] { "asyncDispatch()", this.state }));
/*     */     }
/*     */     
/* 384 */     return doDispatch;
/*     */   }
/*     */   
/*     */   public synchronized void asyncDispatched()
/*     */   {
/* 389 */     if ((this.state == AsyncState.DISPATCHING) || (this.state == AsyncState.MUST_DISPATCH))
/*     */     {
/* 391 */       this.state = AsyncState.DISPATCHED;
/*     */     }
/*     */     else {
/* 394 */       throw new IllegalStateException(sm.getString("asyncStateMachine.invalidAsyncState", new Object[] { "asyncDispatched()", this.state }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized void asyncMustError()
/*     */   {
/* 401 */     if (this.state == AsyncState.STARTED) {
/* 402 */       clearNonBlockingListeners();
/* 403 */       this.state = AsyncState.MUST_ERROR;
/*     */     }
/*     */     else {
/* 406 */       throw new IllegalStateException(sm.getString("asyncStateMachine.invalidAsyncState", new Object[] { "asyncMustError()", this.state }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized void asyncError()
/*     */   {
/* 413 */     if ((this.state == AsyncState.STARTING) || (this.state == AsyncState.STARTED) || (this.state == AsyncState.DISPATCHED) || (this.state == AsyncState.TIMING_OUT) || (this.state == AsyncState.MUST_COMPLETE) || (this.state == AsyncState.READ_WRITE_OP) || (this.state == AsyncState.COMPLETING) || (this.state == AsyncState.MUST_ERROR))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 421 */       clearNonBlockingListeners();
/* 422 */       this.state = AsyncState.ERROR;
/*     */     }
/*     */     else {
/* 425 */       throw new IllegalStateException(sm.getString("asyncStateMachine.invalidAsyncState", new Object[] { "asyncError()", this.state }));
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized void asyncRun(Runnable runnable)
/*     */   {
/* 431 */     if ((this.state == AsyncState.STARTING) || (this.state == AsyncState.STARTED) || (this.state == AsyncState.READ_WRITE_OP))
/*     */     {
/*     */       ClassLoader oldCL;
/*     */       
/*     */       ClassLoader oldCL;
/* 436 */       if (Constants.IS_SECURITY_ENABLED) {
/* 437 */         PrivilegedAction<ClassLoader> pa = new PrivilegedGetTccl();
/* 438 */         oldCL = (ClassLoader)AccessController.doPrivileged(pa);
/*     */       } else {
/* 440 */         oldCL = Thread.currentThread().getContextClassLoader();
/*     */       }
/*     */       try {
/* 443 */         if (Constants.IS_SECURITY_ENABLED)
/*     */         {
/* 445 */           PrivilegedAction<Void> pa = new PrivilegedSetTccl(getClass().getClassLoader());
/* 446 */           AccessController.doPrivileged(pa);
/*     */         } else {
/* 448 */           Thread.currentThread().setContextClassLoader(
/* 449 */             getClass().getClassLoader());
/*     */         }
/*     */         
/* 452 */         this.processor.getExecutor().execute(runnable);
/*     */       } finally { PrivilegedAction<Void> pa;
/* 454 */         if (Constants.IS_SECURITY_ENABLED) {
/* 455 */           PrivilegedAction<Void> pa = new PrivilegedSetTccl(oldCL);
/*     */           
/* 457 */           AccessController.doPrivileged(pa);
/*     */         } else {
/* 459 */           Thread.currentThread().setContextClassLoader(oldCL);
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 464 */       throw new IllegalStateException(sm.getString("asyncStateMachine.invalidAsyncState", new Object[] { "asyncRun()", this.state }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void recycle()
/*     */   {
/* 475 */     if (this.lastAsyncStart == 0L) {
/* 476 */       return;
/*     */     }
/*     */     
/*     */ 
/* 480 */     notifyAll();
/* 481 */     this.asyncCtxt = null;
/* 482 */     this.state = AsyncState.DISPATCHED;
/* 483 */     this.lastAsyncStart = 0L;
/*     */   }
/*     */   
/*     */   private void clearNonBlockingListeners()
/*     */   {
/* 488 */     this.processor.getRequest().listener = null;
/* 489 */     this.processor.getRequest().getResponse().listener = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\AsyncStateMachine.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */