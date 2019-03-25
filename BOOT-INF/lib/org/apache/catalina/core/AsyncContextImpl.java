/*     */ package org.apache.catalina.core;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import javax.naming.NamingException;
/*     */ import javax.servlet.AsyncContext;
/*     */ import javax.servlet.AsyncEvent;
/*     */ import javax.servlet.AsyncListener;
/*     */ import javax.servlet.RequestDispatcher;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.catalina.AsyncDispatcher;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.Globals;
/*     */ import org.apache.catalina.Host;
/*     */ import org.apache.catalina.Pipeline;
/*     */ import org.apache.catalina.Valve;
/*     */ import org.apache.coyote.ActionCode;
/*     */ import org.apache.coyote.AsyncContextCallback;
/*     */ import org.apache.coyote.RequestInfo;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.InstanceManager;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
/*     */ import org.apache.tomcat.util.buf.UDecoder;
/*     */ import org.apache.tomcat.util.res.StringManager;
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
/*     */ public class AsyncContextImpl
/*     */   implements AsyncContext, AsyncContextCallback
/*     */ {
/*  57 */   private static final Log log = LogFactory.getLog(AsyncContextImpl.class);
/*     */   
/*     */ 
/*  60 */   protected static final StringManager sm = StringManager.getManager("org.apache.catalina.core");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  69 */   private final Object asyncContextLock = new Object();
/*     */   
/*  71 */   private volatile ServletRequest servletRequest = null;
/*  72 */   private volatile ServletResponse servletResponse = null;
/*  73 */   private final List<AsyncListenerWrapper> listeners = new ArrayList();
/*  74 */   private boolean hasOriginalRequestAndResponse = true;
/*  75 */   private volatile Runnable dispatch = null;
/*  76 */   private Context context = null;
/*     */   
/*  78 */   private long timeout = -1L;
/*  79 */   private AsyncEvent event = null;
/*     */   private volatile org.apache.catalina.connector.Request request;
/*     */   private volatile InstanceManager instanceManager;
/*     */   
/*     */   public AsyncContextImpl(org.apache.catalina.connector.Request request) {
/*  84 */     if (log.isDebugEnabled()) {
/*  85 */       logDebug("Constructor");
/*     */     }
/*  87 */     this.request = request;
/*     */   }
/*     */   
/*     */   public void complete()
/*     */   {
/*  92 */     if (log.isDebugEnabled()) {
/*  93 */       logDebug("complete   ");
/*     */     }
/*  95 */     check();
/*  96 */     this.request.getCoyoteRequest().action(ActionCode.ASYNC_COMPLETE, null);
/*     */   }
/*     */   
/*     */   public void fireOnComplete()
/*     */   {
/* 101 */     List<AsyncListenerWrapper> listenersCopy = new ArrayList();
/* 102 */     listenersCopy.addAll(this.listeners);
/*     */     
/* 104 */     ClassLoader oldCL = this.context.bind(Globals.IS_SECURITY_ENABLED, null);
/*     */     try {
/* 106 */       for (AsyncListenerWrapper listener : listenersCopy) {
/*     */         try {
/* 108 */           listener.fireOnComplete(this.event);
/*     */         } catch (Throwable t) {
/* 110 */           ExceptionUtils.handleThrowable(t);
/* 111 */           log.warn("onComplete() failed for listener of type [" + listener
/* 112 */             .getClass().getName() + "]", t);
/*     */         }
/*     */       }
/*     */     } finally {
/* 116 */       this.context.fireRequestDestroyEvent(this.request.getRequest());
/* 117 */       clearServletRequestResponse();
/* 118 */       this.context.unbind(Globals.IS_SECURITY_ENABLED, oldCL);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean timeout()
/*     */   {
/* 124 */     AtomicBoolean result = new AtomicBoolean();
/* 125 */     this.request.getCoyoteRequest().action(ActionCode.ASYNC_TIMEOUT, result);
/*     */     
/* 127 */     Context context = this.context;
/*     */     
/* 129 */     if (result.get()) {
/* 130 */       ClassLoader oldCL = context.bind(false, null);
/*     */       try {
/* 132 */         List<AsyncListenerWrapper> listenersCopy = new ArrayList();
/* 133 */         listenersCopy.addAll(this.listeners);
/* 134 */         for (AsyncListenerWrapper listener : listenersCopy) {
/*     */           try {
/* 136 */             listener.fireOnTimeout(this.event);
/*     */           } catch (Throwable t) {
/* 138 */             ExceptionUtils.handleThrowable(t);
/* 139 */             log.warn("onTimeout() failed for listener of type [" + listener
/* 140 */               .getClass().getName() + "]", t);
/*     */           }
/*     */         }
/* 143 */         this.request.getCoyoteRequest().action(ActionCode.ASYNC_IS_TIMINGOUT, result);
/*     */       }
/*     */       finally {
/* 146 */         context.unbind(false, oldCL);
/*     */       }
/*     */     }
/* 149 */     return !result.get();
/*     */   }
/*     */   
/*     */   public void dispatch()
/*     */   {
/* 154 */     check();
/*     */     
/*     */ 
/* 157 */     ServletRequest servletRequest = getRequest();
/* 158 */     String cpath; String path; String cpath; if ((servletRequest instanceof HttpServletRequest)) {
/* 159 */       HttpServletRequest sr = (HttpServletRequest)servletRequest;
/* 160 */       String path = sr.getRequestURI();
/* 161 */       cpath = sr.getContextPath();
/*     */     } else {
/* 163 */       path = this.request.getRequestURI();
/* 164 */       cpath = this.request.getContextPath();
/*     */     }
/* 166 */     if (cpath.length() > 1) {
/* 167 */       path = path.substring(cpath.length());
/*     */     }
/* 169 */     if (!this.context.getDispatchersUseEncodedPaths()) {
/* 170 */       path = UDecoder.URLDecode(path, StandardCharsets.UTF_8);
/*     */     }
/* 172 */     dispatch(path);
/*     */   }
/*     */   
/*     */   public void dispatch(String path)
/*     */   {
/* 177 */     check();
/* 178 */     dispatch(getRequest().getServletContext(), path);
/*     */   }
/*     */   
/*     */   public void dispatch(ServletContext context, String path)
/*     */   {
/* 183 */     synchronized (this.asyncContextLock) {
/* 184 */       if (log.isDebugEnabled()) {
/* 185 */         logDebug("dispatch   ");
/*     */       }
/* 187 */       check();
/* 188 */       if (this.dispatch != null)
/*     */       {
/* 190 */         throw new IllegalStateException(sm.getString("asyncContextImpl.dispatchingStarted"));
/*     */       }
/* 192 */       if (this.request.getAttribute("javax.servlet.async.request_uri") == null) {
/* 193 */         this.request.setAttribute("javax.servlet.async.request_uri", this.request.getRequestURI());
/* 194 */         this.request.setAttribute("javax.servlet.async.context_path", this.request.getContextPath());
/* 195 */         this.request.setAttribute("javax.servlet.async.servlet_path", this.request.getServletPath());
/* 196 */         this.request.setAttribute("javax.servlet.async.path_info", this.request.getPathInfo());
/* 197 */         this.request.setAttribute("javax.servlet.async.query_string", this.request.getQueryString());
/*     */       }
/* 199 */       RequestDispatcher requestDispatcher = context.getRequestDispatcher(path);
/* 200 */       if (!(requestDispatcher instanceof AsyncDispatcher))
/*     */       {
/* 202 */         throw new UnsupportedOperationException(sm.getString("asyncContextImpl.noAsyncDispatcher"));
/*     */       }
/* 204 */       AsyncDispatcher applicationDispatcher = (AsyncDispatcher)requestDispatcher;
/*     */       
/* 206 */       ServletRequest servletRequest = getRequest();
/* 207 */       ServletResponse servletResponse = getResponse();
/* 208 */       this.dispatch = new AsyncRunnable(this.request, applicationDispatcher, servletRequest, servletResponse);
/*     */       
/* 210 */       this.request.getCoyoteRequest().action(ActionCode.ASYNC_DISPATCH, null);
/* 211 */       clearServletRequestResponse();
/*     */     }
/*     */   }
/*     */   
/*     */   public ServletRequest getRequest()
/*     */   {
/* 217 */     check();
/* 218 */     if (this.servletRequest == null)
/*     */     {
/* 220 */       throw new IllegalStateException(sm.getString("asyncContextImpl.request.ise"));
/*     */     }
/* 222 */     return this.servletRequest;
/*     */   }
/*     */   
/*     */   public ServletResponse getResponse()
/*     */   {
/* 227 */     check();
/* 228 */     if (this.servletResponse == null)
/*     */     {
/* 230 */       throw new IllegalStateException(sm.getString("asyncContextImpl.response.ise"));
/*     */     }
/* 232 */     return this.servletResponse;
/*     */   }
/*     */   
/*     */   public void start(Runnable run)
/*     */   {
/* 237 */     if (log.isDebugEnabled()) {
/* 238 */       logDebug("start      ");
/*     */     }
/* 240 */     check();
/* 241 */     Runnable wrapper = new RunnableWrapper(run, this.context, this.request.getCoyoteRequest());
/* 242 */     this.request.getCoyoteRequest().action(ActionCode.ASYNC_RUN, wrapper);
/*     */   }
/*     */   
/*     */   public void addListener(AsyncListener listener)
/*     */   {
/* 247 */     check();
/* 248 */     AsyncListenerWrapper wrapper = new AsyncListenerWrapper();
/* 249 */     wrapper.setListener(listener);
/* 250 */     this.listeners.add(wrapper);
/*     */   }
/*     */   
/*     */ 
/*     */   public void addListener(AsyncListener listener, ServletRequest servletRequest, ServletResponse servletResponse)
/*     */   {
/* 256 */     check();
/* 257 */     AsyncListenerWrapper wrapper = new AsyncListenerWrapper();
/* 258 */     wrapper.setListener(listener);
/* 259 */     wrapper.setServletRequest(servletRequest);
/* 260 */     wrapper.setServletResponse(servletResponse);
/* 261 */     this.listeners.add(wrapper);
/*     */   }
/*     */   
/*     */ 
/*     */   public <T extends AsyncListener> T createListener(Class<T> clazz)
/*     */     throws ServletException
/*     */   {
/* 268 */     check();
/* 269 */     T listener = null;
/*     */     try {
/* 271 */       listener = (AsyncListener)getInstanceManager().newInstance(clazz.getName(), clazz
/* 272 */         .getClassLoader());
/*     */     }
/*     */     catch (InstantiationException|IllegalAccessException|NamingException|ClassNotFoundException e) {
/* 275 */       ServletException se = new ServletException(e);
/* 276 */       throw se;
/*     */     } catch (Exception e) {
/* 278 */       ExceptionUtils.handleThrowable(e.getCause());
/* 279 */       ServletException se = new ServletException(e);
/* 280 */       throw se;
/*     */     }
/* 282 */     return listener;
/*     */   }
/*     */   
/*     */   public void recycle() {
/* 286 */     if (log.isDebugEnabled()) {
/* 287 */       logDebug("recycle    ");
/*     */     }
/* 289 */     this.context = null;
/* 290 */     this.dispatch = null;
/* 291 */     this.event = null;
/* 292 */     this.hasOriginalRequestAndResponse = true;
/* 293 */     this.instanceManager = null;
/* 294 */     this.listeners.clear();
/* 295 */     this.request = null;
/* 296 */     clearServletRequestResponse();
/* 297 */     this.timeout = -1L;
/*     */   }
/*     */   
/*     */   private void clearServletRequestResponse() {
/* 301 */     this.servletRequest = null;
/* 302 */     this.servletResponse = null;
/*     */   }
/*     */   
/*     */   public boolean isStarted() {
/* 306 */     AtomicBoolean result = new AtomicBoolean(false);
/* 307 */     this.request.getCoyoteRequest().action(ActionCode.ASYNC_IS_STARTED, result);
/*     */     
/* 309 */     return result.get();
/*     */   }
/*     */   
/*     */ 
/*     */   public void setStarted(Context context, ServletRequest request, ServletResponse response, boolean originalRequestResponse)
/*     */   {
/* 315 */     synchronized (this.asyncContextLock) {
/* 316 */       this.request.getCoyoteRequest().action(ActionCode.ASYNC_START, this);
/*     */       
/*     */ 
/* 319 */       this.context = context;
/* 320 */       this.servletRequest = request;
/* 321 */       this.servletResponse = response;
/* 322 */       this.hasOriginalRequestAndResponse = originalRequestResponse;
/* 323 */       this.event = new AsyncEvent(this, request, response);
/*     */       
/* 325 */       List<AsyncListenerWrapper> listenersCopy = new ArrayList();
/* 326 */       listenersCopy.addAll(this.listeners);
/* 327 */       this.listeners.clear();
/* 328 */       for (AsyncListenerWrapper listener : listenersCopy) {
/*     */         try {
/* 330 */           listener.fireOnStartAsync(this.event);
/*     */         } catch (Throwable t) {
/* 332 */           ExceptionUtils.handleThrowable(t);
/* 333 */           log.warn("onStartAsync() failed for listener of type [" + listener
/* 334 */             .getClass().getName() + "]", t);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean hasOriginalRequestAndResponse()
/*     */   {
/* 342 */     check();
/* 343 */     return this.hasOriginalRequestAndResponse;
/*     */   }
/*     */   
/*     */   protected void doInternalDispatch() throws ServletException, IOException {
/* 347 */     if (log.isDebugEnabled()) {
/* 348 */       logDebug("intDispatch");
/*     */     }
/*     */     try {
/* 351 */       Runnable runnable = this.dispatch;
/* 352 */       this.dispatch = null;
/* 353 */       runnable.run();
/* 354 */       if (!this.request.isAsync()) {
/* 355 */         fireOnComplete();
/*     */       }
/*     */     }
/*     */     catch (RuntimeException x) {
/* 359 */       if ((x.getCause() instanceof ServletException)) {
/* 360 */         throw ((ServletException)x.getCause());
/*     */       }
/* 362 */       if ((x.getCause() instanceof IOException)) {
/* 363 */         throw ((IOException)x.getCause());
/*     */       }
/* 365 */       throw new ServletException(x);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public long getTimeout()
/*     */   {
/* 372 */     check();
/* 373 */     return this.timeout;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setTimeout(long timeout)
/*     */   {
/* 379 */     check();
/* 380 */     this.timeout = timeout;
/* 381 */     this.request.getCoyoteRequest().action(ActionCode.ASYNC_SETTIMEOUT, 
/* 382 */       Long.valueOf(timeout));
/*     */   }
/*     */   
/*     */   public void setErrorState(Throwable t, boolean fireOnError)
/*     */   {
/* 387 */     if (t != null) this.request.setAttribute("javax.servlet.error.exception", t);
/* 388 */     this.request.getCoyoteRequest().action(ActionCode.ASYNC_ERROR, null);
/*     */     AsyncEvent errorEvent;
/* 390 */     if (fireOnError)
/*     */     {
/* 392 */       errorEvent = new AsyncEvent(this.event.getAsyncContext(), this.event.getSuppliedRequest(), this.event.getSuppliedResponse(), t);
/* 393 */       List<AsyncListenerWrapper> listenersCopy = new ArrayList();
/* 394 */       listenersCopy.addAll(this.listeners);
/* 395 */       for (AsyncListenerWrapper listener : listenersCopy) {
/*     */         try {
/* 397 */           listener.fireOnError(errorEvent);
/*     */         } catch (Throwable t2) {
/* 399 */           ExceptionUtils.handleThrowable(t2);
/* 400 */           log.warn("onError() failed for listener of type [" + listener
/* 401 */             .getClass().getName() + "]", t2);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 407 */     AtomicBoolean result = new AtomicBoolean();
/* 408 */     this.request.getCoyoteRequest().action(ActionCode.ASYNC_IS_ERROR, result);
/* 409 */     if (result.get())
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 415 */       ServletResponse servletResponse = this.servletResponse;
/* 416 */       if ((servletResponse instanceof HttpServletResponse)) {
/* 417 */         ((HttpServletResponse)servletResponse).setStatus(500);
/*     */       }
/*     */       
/*     */ 
/* 421 */       Host host = (Host)this.context.getParent();
/* 422 */       Valve stdHostValve = host.getPipeline().getBasic();
/* 423 */       if ((stdHostValve instanceof StandardHostValve)) {
/* 424 */         ((StandardHostValve)stdHostValve).throwable(this.request, this.request
/* 425 */           .getResponse(), t);
/*     */       }
/*     */       
/* 428 */       this.request.getCoyoteRequest().action(ActionCode.ASYNC_IS_ERROR, result);
/*     */       
/* 430 */       if (result.get())
/*     */       {
/*     */ 
/* 433 */         complete();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void logDebug(String method)
/*     */   {
/* 444 */     StringBuilder uri = new StringBuilder();
/* 445 */     String rHashCode; String crHashCode; String rpHashCode; String stage; if (this.request == null) {
/* 446 */       String rHashCode = "null";
/* 447 */       String crHashCode = "null";
/* 448 */       String rpHashCode = "null";
/* 449 */       String stage = "-";
/* 450 */       uri.append("N/A");
/*     */     } else {
/* 452 */       rHashCode = Integer.toHexString(this.request.hashCode());
/* 453 */       org.apache.coyote.Request coyoteRequest = this.request.getCoyoteRequest();
/* 454 */       String stage; if (coyoteRequest == null) {
/* 455 */         String crHashCode = "null";
/* 456 */         String rpHashCode = "null";
/* 457 */         stage = "-";
/*     */       } else {
/* 459 */         crHashCode = Integer.toHexString(coyoteRequest.hashCode());
/* 460 */         RequestInfo rp = coyoteRequest.getRequestProcessor();
/* 461 */         String stage; if (rp == null) {
/* 462 */           String rpHashCode = "null";
/* 463 */           stage = "-";
/*     */         } else {
/* 465 */           rpHashCode = Integer.toHexString(rp.hashCode());
/* 466 */           stage = Integer.toString(rp.getStage());
/*     */         }
/*     */       }
/* 469 */       uri.append(this.request.getRequestURI());
/* 470 */       if (this.request.getQueryString() != null) {
/* 471 */         uri.append('?');
/* 472 */         uri.append(this.request.getQueryString());
/*     */       }
/*     */     }
/* 475 */     String threadName = Thread.currentThread().getName();
/* 476 */     int len = threadName.length();
/* 477 */     if (len > 20) {
/* 478 */       threadName = threadName.substring(len - 20, len);
/*     */     }
/* 480 */     String msg = String.format("Req: %1$8s  CReq: %2$8s  RP: %3$8s  Stage: %4$s  Thread: %5$20s  State: %6$20s  Method: %7$11s  URI: %8$s", new Object[] { rHashCode, crHashCode, rpHashCode, stage, threadName, "N/A", method, uri });
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 485 */     if (log.isTraceEnabled()) {
/* 486 */       log.trace(msg, new DebugException(null));
/*     */     } else {
/* 488 */       log.debug(msg);
/*     */     }
/*     */   }
/*     */   
/*     */   private InstanceManager getInstanceManager() {
/* 493 */     if (this.instanceManager == null) {
/* 494 */       if ((this.context instanceof StandardContext)) {
/* 495 */         this.instanceManager = ((StandardContext)this.context).getInstanceManager();
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 500 */         this.instanceManager = new DefaultInstanceManager(null, new HashMap(), this.context, getClass().getClassLoader());
/*     */       }
/*     */     }
/* 503 */     return this.instanceManager;
/*     */   }
/*     */   
/*     */   private void check() {
/* 507 */     if (this.request == null)
/*     */     {
/* 509 */       throw new IllegalStateException(sm.getString("asyncContextImpl.requestEnded"));
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DebugException extends Exception
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */   }
/*     */   
/*     */   private static class RunnableWrapper implements Runnable
/*     */   {
/*     */     private final Runnable wrapped;
/*     */     private final Context context;
/*     */     private final org.apache.coyote.Request coyoteRequest;
/*     */     
/*     */     public RunnableWrapper(Runnable wrapped, Context ctxt, org.apache.coyote.Request coyoteRequest)
/*     */     {
/* 526 */       this.wrapped = wrapped;
/* 527 */       this.context = ctxt;
/* 528 */       this.coyoteRequest = coyoteRequest;
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void run()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 3	org/apache/catalina/core/AsyncContextImpl$RunnableWrapper:context	Lorg/apache/catalina/Context;
/*     */       //   4: getstatic 5	org/apache/catalina/Globals:IS_SECURITY_ENABLED	Z
/*     */       //   7: aconst_null
/*     */       //   8: invokeinterface 6 3 0
/*     */       //   13: astore_1
/*     */       //   14: aload_0
/*     */       //   15: getfield 2	org/apache/catalina/core/AsyncContextImpl$RunnableWrapper:wrapped	Ljava/lang/Runnable;
/*     */       //   18: invokeinterface 7 1 0
/*     */       //   23: aload_0
/*     */       //   24: getfield 3	org/apache/catalina/core/AsyncContextImpl$RunnableWrapper:context	Lorg/apache/catalina/Context;
/*     */       //   27: getstatic 5	org/apache/catalina/Globals:IS_SECURITY_ENABLED	Z
/*     */       //   30: aload_1
/*     */       //   31: invokeinterface 8 3 0
/*     */       //   36: goto +19 -> 55
/*     */       //   39: astore_2
/*     */       //   40: aload_0
/*     */       //   41: getfield 3	org/apache/catalina/core/AsyncContextImpl$RunnableWrapper:context	Lorg/apache/catalina/Context;
/*     */       //   44: getstatic 5	org/apache/catalina/Globals:IS_SECURITY_ENABLED	Z
/*     */       //   47: aload_1
/*     */       //   48: invokeinterface 8 3 0
/*     */       //   53: aload_2
/*     */       //   54: athrow
/*     */       //   55: aload_0
/*     */       //   56: getfield 4	org/apache/catalina/core/AsyncContextImpl$RunnableWrapper:coyoteRequest	Lorg/apache/coyote/Request;
/*     */       //   59: getstatic 9	org/apache/coyote/ActionCode:DISPATCH_EXECUTE	Lorg/apache/coyote/ActionCode;
/*     */       //   62: aconst_null
/*     */       //   63: invokevirtual 10	org/apache/coyote/Request:action	(Lorg/apache/coyote/ActionCode;Ljava/lang/Object;)V
/*     */       //   66: return
/*     */       // Line number table:
/*     */       //   Java source line #533	-> byte code offset #0
/*     */       //   Java source line #535	-> byte code offset #14
/*     */       //   Java source line #537	-> byte code offset #23
/*     */       //   Java source line #538	-> byte code offset #36
/*     */       //   Java source line #537	-> byte code offset #39
/*     */       //   Java source line #543	-> byte code offset #55
/*     */       //   Java source line #544	-> byte code offset #66
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	67	0	this	RunnableWrapper
/*     */       //   13	35	1	oldCL	ClassLoader
/*     */       //   39	15	2	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   14	23	39	finally
/*     */     }
/*     */   }
/*     */   
/*     */   private static class AsyncRunnable
/*     */     implements Runnable
/*     */   {
/*     */     private final AsyncDispatcher applicationDispatcher;
/*     */     private final org.apache.catalina.connector.Request request;
/*     */     private final ServletRequest servletRequest;
/*     */     private final ServletResponse servletResponse;
/*     */     
/*     */     public AsyncRunnable(org.apache.catalina.connector.Request request, AsyncDispatcher applicationDispatcher, ServletRequest servletRequest, ServletResponse servletResponse)
/*     */     {
/* 557 */       this.request = request;
/* 558 */       this.applicationDispatcher = applicationDispatcher;
/* 559 */       this.servletRequest = servletRequest;
/* 560 */       this.servletResponse = servletResponse;
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/* 565 */       this.request.getCoyoteRequest().action(ActionCode.ASYNC_DISPATCHED, null);
/*     */       try {
/* 567 */         this.applicationDispatcher.dispatch(this.servletRequest, this.servletResponse);
/*     */       }
/*     */       catch (Exception x) {
/* 570 */         throw new RuntimeException(x);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\AsyncContextImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */