/*     */ package org.apache.coyote;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Iterator;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
/*     */ import org.apache.tomcat.util.buf.ByteChunk;
/*     */ import org.apache.tomcat.util.buf.MessageBytes;
/*     */ import org.apache.tomcat.util.net.AbstractEndpoint;
/*     */ import org.apache.tomcat.util.net.AbstractEndpoint.Handler.SocketState;
/*     */ import org.apache.tomcat.util.net.DispatchType;
/*     */ import org.apache.tomcat.util.net.SSLSupport;
/*     */ import org.apache.tomcat.util.net.SocketEvent;
/*     */ import org.apache.tomcat.util.net.SocketWrapperBase;
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
/*     */ 
/*     */ public abstract class AbstractProcessor
/*     */   extends AbstractProcessorLight
/*     */   implements ActionHook
/*     */ {
/*  44 */   private static final StringManager sm = StringManager.getManager(AbstractProcessor.class);
/*     */   
/*     */   protected Adapter adapter;
/*     */   protected final AsyncStateMachine asyncStateMachine;
/*  48 */   private volatile long asyncTimeout = -1L;
/*     */   protected final AbstractEndpoint<?> endpoint;
/*     */   protected final Request request;
/*     */   protected final Response response;
/*  52 */   protected volatile SocketWrapperBase<?> socketWrapper = null;
/*     */   
/*     */ 
/*     */ 
/*     */   protected volatile SSLSupport sslSupport;
/*     */   
/*     */ 
/*  59 */   private ErrorState errorState = ErrorState.NONE;
/*     */   
/*     */   public AbstractProcessor(AbstractEndpoint<?> endpoint)
/*     */   {
/*  63 */     this(endpoint, new Request(), new Response());
/*     */   }
/*     */   
/*     */ 
/*     */   protected AbstractProcessor(AbstractEndpoint<?> endpoint, Request coyoteRequest, Response coyoteResponse)
/*     */   {
/*  69 */     this.endpoint = endpoint;
/*  70 */     this.asyncStateMachine = new AsyncStateMachine(this);
/*  71 */     this.request = coyoteRequest;
/*  72 */     this.response = coyoteResponse;
/*  73 */     this.response.setHook(this);
/*  74 */     this.request.setResponse(this.response);
/*  75 */     this.request.setHook(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setErrorState(ErrorState errorState, Throwable t)
/*     */   {
/*  85 */     boolean blockIo = (this.errorState.isIoAllowed()) && (!errorState.isIoAllowed());
/*  86 */     this.errorState = this.errorState.getMostSevere(errorState);
/*     */     
/*     */ 
/*     */ 
/*  90 */     if ((this.response.getStatus() < 400) && (!(t instanceof IOException))) {
/*  91 */       this.response.setStatus(500);
/*     */     }
/*  93 */     if (t != null) {
/*  94 */       this.request.setAttribute("javax.servlet.error.exception", t);
/*     */     }
/*  96 */     if ((blockIo) && (!ContainerThreadMarker.isContainerThread()) && (isAsync()))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 102 */       this.asyncStateMachine.asyncMustError();
/* 103 */       if (getLog().isDebugEnabled()) {
/* 104 */         getLog().debug(sm.getString("abstractProcessor.nonContainerThreadError"), t);
/*     */       }
/* 106 */       processSocketEvent(SocketEvent.ERROR, true);
/*     */     }
/*     */   }
/*     */   
/*     */   protected ErrorState getErrorState()
/*     */   {
/* 112 */     return this.errorState;
/*     */   }
/*     */   
/*     */ 
/*     */   public Request getRequest()
/*     */   {
/* 118 */     return this.request;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAdapter(Adapter adapter)
/*     */   {
/* 128 */     this.adapter = adapter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Adapter getAdapter()
/*     */   {
/* 138 */     return this.adapter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void setSocketWrapper(SocketWrapperBase<?> socketWrapper)
/*     */   {
/* 147 */     this.socketWrapper = socketWrapper;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final SocketWrapperBase<?> getSocketWrapper()
/*     */   {
/* 155 */     return this.socketWrapper;
/*     */   }
/*     */   
/*     */ 
/*     */   public final void setSslSupport(SSLSupport sslSupport)
/*     */   {
/* 161 */     this.sslSupport = sslSupport;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Executor getExecutor()
/*     */   {
/* 169 */     return this.endpoint.getExecutor();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isAsync()
/*     */   {
/* 175 */     return this.asyncStateMachine.isAsync();
/*     */   }
/*     */   
/*     */ 
/*     */   public AbstractEndpoint.Handler.SocketState asyncPostProcess()
/*     */   {
/* 181 */     return this.asyncStateMachine.asyncPostProcess();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final AbstractEndpoint.Handler.SocketState dispatch(SocketEvent status)
/*     */   {
/* 188 */     if ((status == SocketEvent.OPEN_WRITE) && (this.response.getWriteListener() != null)) {
/* 189 */       this.asyncStateMachine.asyncOperation();
/*     */       try {
/* 191 */         if (flushBufferedWrite()) {
/* 192 */           return AbstractEndpoint.Handler.SocketState.LONG;
/*     */         }
/*     */       } catch (IOException ioe) {
/* 195 */         if (getLog().isDebugEnabled()) {
/* 196 */           getLog().debug("Unable to write async data.", ioe);
/*     */         }
/* 198 */         status = SocketEvent.ERROR;
/* 199 */         this.request.setAttribute("javax.servlet.error.exception", ioe);
/*     */       } }
/* 201 */     if ((status == SocketEvent.OPEN_READ) && (this.request.getReadListener() != null)) {
/* 202 */       dispatchNonBlockingRead();
/* 203 */     } else if (status == SocketEvent.ERROR)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 208 */       if (this.request.getAttribute("javax.servlet.error.exception") == null)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 214 */         this.request.setAttribute("javax.servlet.error.exception", this.socketWrapper.getError());
/*     */       }
/*     */       
/* 217 */       if ((this.request.getReadListener() != null) || (this.response.getWriteListener() != null))
/*     */       {
/*     */ 
/* 220 */         this.asyncStateMachine.asyncOperation();
/*     */       }
/*     */     }
/*     */     
/* 224 */     RequestInfo rp = this.request.getRequestProcessor();
/*     */     try {
/* 226 */       rp.setStage(3);
/* 227 */       if (!getAdapter().asyncDispatch(this.request, this.response, status)) {
/* 228 */         setErrorState(ErrorState.CLOSE_NOW, null);
/*     */       }
/*     */     } catch (InterruptedIOException e) {
/* 231 */       setErrorState(ErrorState.CLOSE_CONNECTION_NOW, e);
/*     */     } catch (Throwable t) {
/* 233 */       ExceptionUtils.handleThrowable(t);
/* 234 */       setErrorState(ErrorState.CLOSE_NOW, t);
/* 235 */       getLog().error(sm.getString("http11processor.request.process"), t);
/*     */     }
/*     */     
/* 238 */     rp.setStage(7);
/*     */     
/* 240 */     if (getErrorState().isError()) {
/* 241 */       this.request.updateCounters();
/* 242 */       return AbstractEndpoint.Handler.SocketState.CLOSED; }
/* 243 */     if (isAsync()) {
/* 244 */       return AbstractEndpoint.Handler.SocketState.LONG;
/*     */     }
/* 246 */     this.request.updateCounters();
/* 247 */     return dispatchEndRequest();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final void action(ActionCode actionCode, Object param)
/*     */   {
/* 254 */     switch (actionCode)
/*     */     {
/*     */     case COMMIT: 
/* 257 */       if (!this.response.isCommitted()) {
/*     */         try
/*     */         {
/* 260 */           prepareResponse();
/*     */         } catch (IOException e) {
/* 262 */           setErrorState(ErrorState.CLOSE_CONNECTION_NOW, e);
/*     */         }
/*     */       }
/*     */       
/*     */       break;
/*     */     case CLOSE: 
/* 268 */       action(ActionCode.COMMIT, null);
/*     */       try {
/* 270 */         finishResponse();
/*     */       } catch (CloseNowException cne) {
/* 272 */         setErrorState(ErrorState.CLOSE_NOW, cne);
/*     */       } catch (IOException e) {
/* 274 */         setErrorState(ErrorState.CLOSE_CONNECTION_NOW, e);
/*     */       }
/*     */     
/*     */ 
/*     */     case ACK: 
/* 279 */       ack();
/* 280 */       break;
/*     */     
/*     */     case CLIENT_FLUSH: 
/* 283 */       action(ActionCode.COMMIT, null);
/*     */       try {
/* 285 */         flush();
/*     */       } catch (IOException e) {
/* 287 */         setErrorState(ErrorState.CLOSE_CONNECTION_NOW, e);
/* 288 */         this.response.setErrorException(e);
/*     */       }
/*     */     
/*     */ 
/*     */     case AVAILABLE: 
/* 293 */       this.request.setAvailable(available(Boolean.TRUE.equals(param)));
/* 294 */       break;
/*     */     
/*     */     case REQ_SET_BODY_REPLAY: 
/* 297 */       ByteChunk body = (ByteChunk)param;
/* 298 */       setRequestBody(body);
/* 299 */       break;
/*     */     
/*     */ 
/*     */ 
/*     */     case IS_ERROR: 
/* 304 */       ((AtomicBoolean)param).set(getErrorState().isError());
/* 305 */       break;
/*     */     
/*     */     case IS_IO_ALLOWED: 
/* 308 */       ((AtomicBoolean)param).set(getErrorState().isIoAllowed());
/* 309 */       break;
/*     */     
/*     */ 
/*     */     case CLOSE_NOW: 
/* 313 */       setSwallowResponse();
/* 314 */       if ((param instanceof Throwable)) {
/* 315 */         setErrorState(ErrorState.CLOSE_NOW, (Throwable)param);
/*     */       } else {
/* 317 */         setErrorState(ErrorState.CLOSE_NOW, null);
/*     */       }
/* 319 */       break;
/*     */     
/*     */ 
/*     */ 
/*     */     case DISABLE_SWALLOW_INPUT: 
/* 324 */       disableSwallowRequest();
/*     */       
/* 326 */       setErrorState(ErrorState.CLOSE_CLEAN, null);
/* 327 */       break;
/*     */     
/*     */ 
/*     */ 
/*     */     case REQ_HOST_ADDR_ATTRIBUTE: 
/* 332 */       if ((getPopulateRequestAttributesFromSocket()) && (this.socketWrapper != null)) {
/* 333 */         this.request.remoteAddr().setString(this.socketWrapper.getRemoteAddr());
/*     */       }
/*     */       
/*     */       break;
/*     */     case REQ_HOST_ATTRIBUTE: 
/* 338 */       populateRequestAttributeRemoteHost();
/* 339 */       break;
/*     */     
/*     */     case REQ_LOCALPORT_ATTRIBUTE: 
/* 342 */       if ((getPopulateRequestAttributesFromSocket()) && (this.socketWrapper != null)) {
/* 343 */         this.request.setLocalPort(this.socketWrapper.getLocalPort());
/*     */       }
/*     */       
/*     */       break;
/*     */     case REQ_LOCAL_ADDR_ATTRIBUTE: 
/* 348 */       if ((getPopulateRequestAttributesFromSocket()) && (this.socketWrapper != null)) {
/* 349 */         this.request.localAddr().setString(this.socketWrapper.getLocalAddr());
/*     */       }
/*     */       
/*     */       break;
/*     */     case REQ_LOCAL_NAME_ATTRIBUTE: 
/* 354 */       if ((getPopulateRequestAttributesFromSocket()) && (this.socketWrapper != null)) {
/* 355 */         this.request.localName().setString(this.socketWrapper.getLocalName());
/*     */       }
/*     */       
/*     */       break;
/*     */     case REQ_REMOTEPORT_ATTRIBUTE: 
/* 360 */       if ((getPopulateRequestAttributesFromSocket()) && (this.socketWrapper != null)) {
/* 361 */         this.request.setRemotePort(this.socketWrapper.getRemotePort());
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */       break;
/*     */     case REQ_SSL_ATTRIBUTE: 
/* 368 */       populateSslRequestAttributes();
/* 369 */       break;
/*     */     case REQ_SSL_CERTIFICATE: 
/*     */       try
/*     */       {
/* 373 */         sslReHandShake();
/*     */       } catch (IOException ioe) {
/* 375 */         setErrorState(ErrorState.CLOSE_CONNECTION_NOW, ioe);
/*     */       }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     case ASYNC_START: 
/* 382 */       this.asyncStateMachine.asyncStart((AsyncContextCallback)param);
/* 383 */       break;
/*     */     
/*     */     case ASYNC_COMPLETE: 
/* 386 */       clearDispatches();
/* 387 */       if (this.asyncStateMachine.asyncComplete()) {
/* 388 */         processSocketEvent(SocketEvent.OPEN_READ, true);
/*     */       }
/*     */       
/*     */       break;
/*     */     case ASYNC_DISPATCH: 
/* 393 */       if (this.asyncStateMachine.asyncDispatch()) {
/* 394 */         processSocketEvent(SocketEvent.OPEN_READ, true);
/*     */       }
/*     */       
/*     */       break;
/*     */     case ASYNC_DISPATCHED: 
/* 399 */       this.asyncStateMachine.asyncDispatched();
/* 400 */       break;
/*     */     
/*     */     case ASYNC_ERROR: 
/* 403 */       this.asyncStateMachine.asyncError();
/* 404 */       break;
/*     */     
/*     */     case ASYNC_IS_ASYNC: 
/* 407 */       ((AtomicBoolean)param).set(this.asyncStateMachine.isAsync());
/* 408 */       break;
/*     */     
/*     */     case ASYNC_IS_COMPLETING: 
/* 411 */       ((AtomicBoolean)param).set(this.asyncStateMachine.isCompleting());
/* 412 */       break;
/*     */     
/*     */     case ASYNC_IS_DISPATCHING: 
/* 415 */       ((AtomicBoolean)param).set(this.asyncStateMachine.isAsyncDispatching());
/* 416 */       break;
/*     */     
/*     */     case ASYNC_IS_ERROR: 
/* 419 */       ((AtomicBoolean)param).set(this.asyncStateMachine.isAsyncError());
/* 420 */       break;
/*     */     
/*     */     case ASYNC_IS_STARTED: 
/* 423 */       ((AtomicBoolean)param).set(this.asyncStateMachine.isAsyncStarted());
/* 424 */       break;
/*     */     
/*     */     case ASYNC_IS_TIMINGOUT: 
/* 427 */       ((AtomicBoolean)param).set(this.asyncStateMachine.isAsyncTimingOut());
/* 428 */       break;
/*     */     
/*     */     case ASYNC_RUN: 
/* 431 */       this.asyncStateMachine.asyncRun((Runnable)param);
/* 432 */       break;
/*     */     
/*     */     case ASYNC_SETTIMEOUT: 
/* 435 */       if (param == null) {
/* 436 */         return;
/*     */       }
/* 438 */       long timeout = ((Long)param).longValue();
/* 439 */       setAsyncTimeout(timeout);
/* 440 */       break;
/*     */     
/*     */     case ASYNC_TIMEOUT: 
/* 443 */       AtomicBoolean result = (AtomicBoolean)param;
/* 444 */       result.set(this.asyncStateMachine.asyncTimeout());
/* 445 */       break;
/*     */     
/*     */     case ASYNC_POST_PROCESS: 
/* 448 */       this.asyncStateMachine.asyncPostProcess();
/* 449 */       break;
/*     */     
/*     */ 
/*     */ 
/*     */     case REQUEST_BODY_FULLY_READ: 
/* 454 */       AtomicBoolean result = (AtomicBoolean)param;
/* 455 */       result.set(isRequestBodyFullyRead());
/* 456 */       break;
/*     */     
/*     */     case NB_READ_INTEREST: 
/* 459 */       if (!isRequestBodyFullyRead()) {
/* 460 */         registerReadInterest();
/*     */       }
/*     */       
/*     */       break;
/*     */     case NB_WRITE_INTEREST: 
/* 465 */       AtomicBoolean isReady = (AtomicBoolean)param;
/* 466 */       isReady.set(isReady());
/* 467 */       break;
/*     */     
/*     */     case DISPATCH_READ: 
/* 470 */       addDispatch(DispatchType.NON_BLOCKING_READ);
/* 471 */       break;
/*     */     
/*     */     case DISPATCH_WRITE: 
/* 474 */       addDispatch(DispatchType.NON_BLOCKING_WRITE);
/* 475 */       break;
/*     */     
/*     */     case DISPATCH_EXECUTE: 
/* 478 */       executeDispatches();
/* 479 */       break;
/*     */     
/*     */ 
/*     */ 
/*     */     case UPGRADE: 
/* 484 */       doHttpUpgrade((UpgradeToken)param);
/* 485 */       break;
/*     */     
/*     */ 
/*     */ 
/*     */     case IS_PUSH_SUPPORTED: 
/* 490 */       AtomicBoolean result = (AtomicBoolean)param;
/* 491 */       result.set(isPushSupported());
/* 492 */       break;
/*     */     
/*     */     case PUSH_REQUEST: 
/* 495 */       doPush((Request)param);
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void dispatchNonBlockingRead()
/*     */   {
/* 507 */     this.asyncStateMachine.asyncOperation();
/*     */   }
/*     */   
/*     */ 
/*     */   public void timeoutAsync(long now)
/*     */   {
/* 513 */     if (now < 0L) {
/* 514 */       doTimeoutAsync();
/*     */     } else {
/* 516 */       long asyncTimeout = getAsyncTimeout();
/* 517 */       if (asyncTimeout > 0L) {
/* 518 */         long asyncStart = this.asyncStateMachine.getLastAsyncStart();
/* 519 */         if (now - asyncStart > asyncTimeout) {
/* 520 */           doTimeoutAsync();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void doTimeoutAsync()
/*     */   {
/* 529 */     setAsyncTimeout(-1L);
/* 530 */     processSocketEvent(SocketEvent.TIMEOUT, true);
/*     */   }
/*     */   
/*     */   public void setAsyncTimeout(long timeout)
/*     */   {
/* 535 */     this.asyncTimeout = timeout;
/*     */   }
/*     */   
/*     */   public long getAsyncTimeout()
/*     */   {
/* 540 */     return this.asyncTimeout;
/*     */   }
/*     */   
/*     */ 
/*     */   public void recycle()
/*     */   {
/* 546 */     this.errorState = ErrorState.NONE;
/* 547 */     this.asyncStateMachine.recycle();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract void prepareResponse()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract void finishResponse()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract void ack();
/*     */   
/*     */ 
/*     */   protected abstract void flush()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */   protected abstract int available(boolean paramBoolean);
/*     */   
/*     */ 
/*     */   protected abstract void setRequestBody(ByteChunk paramByteChunk);
/*     */   
/*     */ 
/*     */   protected abstract void setSwallowResponse();
/*     */   
/*     */ 
/*     */   protected abstract void disableSwallowRequest();
/*     */   
/*     */ 
/*     */   protected boolean getPopulateRequestAttributesFromSocket()
/*     */   {
/* 583 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void populateRequestAttributeRemoteHost()
/*     */   {
/* 592 */     if ((getPopulateRequestAttributesFromSocket()) && (this.socketWrapper != null)) {
/* 593 */       this.request.remoteHost().setString(this.socketWrapper.getRemoteHost());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void populateSslRequestAttributes()
/*     */   {
/*     */     try
/*     */     {
/* 606 */       if (this.sslSupport != null) {
/* 607 */         Object sslO = this.sslSupport.getCipherSuite();
/* 608 */         if (sslO != null) {
/* 609 */           this.request.setAttribute("javax.servlet.request.cipher_suite", sslO);
/*     */         }
/* 611 */         sslO = this.sslSupport.getPeerCertificateChain();
/* 612 */         if (sslO != null) {
/* 613 */           this.request.setAttribute("javax.servlet.request.X509Certificate", sslO);
/*     */         }
/* 615 */         sslO = this.sslSupport.getKeySize();
/* 616 */         if (sslO != null) {
/* 617 */           this.request.setAttribute("javax.servlet.request.key_size", sslO);
/*     */         }
/* 619 */         sslO = this.sslSupport.getSessionId();
/* 620 */         if (sslO != null) {
/* 621 */           this.request.setAttribute("javax.servlet.request.ssl_session_id", sslO);
/*     */         }
/* 623 */         sslO = this.sslSupport.getProtocol();
/* 624 */         if (sslO != null) {
/* 625 */           this.request.setAttribute("org.apache.tomcat.util.net.secure_protocol_version", sslO);
/*     */         }
/* 627 */         this.request.setAttribute("javax.servlet.request.ssl_session_mgr", this.sslSupport);
/*     */       }
/*     */     } catch (Exception e) {
/* 630 */       getLog().warn(sm.getString("abstractProcessor.socket.ssl"), e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void sslReHandShake()
/*     */     throws IOException
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void processSocketEvent(SocketEvent event, boolean dispatch)
/*     */   {
/* 649 */     SocketWrapperBase<?> socketWrapper = getSocketWrapper();
/* 650 */     if (socketWrapper != null) {
/* 651 */       socketWrapper.processSocket(event, dispatch);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected abstract boolean isRequestBodyFullyRead();
/*     */   
/*     */ 
/*     */   protected abstract void registerReadInterest();
/*     */   
/*     */ 
/*     */   protected abstract boolean isReady();
/*     */   
/*     */   protected void executeDispatches()
/*     */   {
/* 666 */     SocketWrapperBase<?> socketWrapper = getSocketWrapper();
/* 667 */     Iterator<DispatchType> dispatches = getIteratorAndClearDispatches();
/* 668 */     if (socketWrapper != null) {
/* 669 */       synchronized (socketWrapper)
/*     */       {
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
/* 688 */         while ((dispatches != null) && (dispatches.hasNext())) {
/* 689 */           DispatchType dispatchType = (DispatchType)dispatches.next();
/* 690 */           socketWrapper.processSocket(dispatchType.getSocketStatus(), false);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UpgradeToken getUpgradeToken()
/*     */   {
/* 706 */     throw new IllegalStateException(sm.getString("abstractProcessor.httpupgrade.notsupported"));
/*     */   }
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
/*     */   protected void doHttpUpgrade(UpgradeToken upgradeToken)
/*     */   {
/* 723 */     throw new UnsupportedOperationException(sm.getString("abstractProcessor.httpupgrade.notsupported"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ByteBuffer getLeftoverInput()
/*     */   {
/* 734 */     throw new IllegalStateException(sm.getString("abstractProcessor.httpupgrade.notsupported"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUpgrade()
/*     */   {
/* 744 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isPushSupported()
/*     */   {
/* 756 */     return false;
/*     */   }
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
/*     */   protected void doPush(Request pushTarget)
/*     */   {
/* 772 */     throw new UnsupportedOperationException(sm.getString("abstractProcessor.pushrequest.notsupported"));
/*     */   }
/*     */   
/*     */   protected abstract boolean flushBufferedWrite()
/*     */     throws IOException;
/*     */   
/*     */   protected abstract AbstractEndpoint.Handler.SocketState dispatchEndRequest();
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\AbstractProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */