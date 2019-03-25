/*     */ package org.apache.tomcat.websocket;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.WritePendingException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.security.Principal;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import javax.websocket.CloseReason;
/*     */ import javax.websocket.CloseReason.CloseCode;
/*     */ import javax.websocket.CloseReason.CloseCodes;
/*     */ import javax.websocket.DeploymentException;
/*     */ import javax.websocket.Endpoint;
/*     */ import javax.websocket.EndpointConfig;
/*     */ import javax.websocket.Extension;
/*     */ import javax.websocket.MessageHandler;
/*     */ import javax.websocket.MessageHandler.Partial;
/*     */ import javax.websocket.MessageHandler.Whole;
/*     */ import javax.websocket.PongMessage;
/*     */ import javax.websocket.RemoteEndpoint.Async;
/*     */ import javax.websocket.RemoteEndpoint.Basic;
/*     */ import javax.websocket.SendResult;
/*     */ import javax.websocket.Session;
/*     */ import javax.websocket.WebSocketContainer;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.InstanceManager;
/*     */ import org.apache.tomcat.InstanceManagerBindings;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
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
/*     */ public class WsSession
/*     */   implements Session
/*     */ {
/*  60 */   private static final byte[] ELLIPSIS_BYTES = "…".getBytes(StandardCharsets.UTF_8);
/*     */   
/*  62 */   private static final int ELLIPSIS_BYTES_LEN = ELLIPSIS_BYTES.length;
/*     */   
/*  64 */   private static final StringManager sm = StringManager.getManager(WsSession.class);
/*  65 */   private static AtomicLong ids = new AtomicLong(0L);
/*     */   
/*  67 */   private final Log log = LogFactory.getLog(WsSession.class);
/*     */   
/*     */   private final Endpoint localEndpoint;
/*     */   
/*     */   private final WsRemoteEndpointImplBase wsRemoteEndpoint;
/*     */   
/*     */   private final RemoteEndpoint.Async remoteEndpointAsync;
/*     */   
/*     */   private final RemoteEndpoint.Basic remoteEndpointBasic;
/*     */   private final ClassLoader applicationClassLoader;
/*     */   private final WsWebSocketContainer webSocketContainer;
/*     */   private final URI requestUri;
/*     */   private final Map<String, List<String>> requestParameterMap;
/*     */   private final String queryString;
/*     */   private final Principal userPrincipal;
/*     */   private final EndpointConfig endpointConfig;
/*     */   private final List<Extension> negotiatedExtensions;
/*     */   private final String subProtocol;
/*     */   private final Map<String, String> pathParameters;
/*     */   private final boolean secure;
/*     */   private final String httpSessionId;
/*     */   private final String id;
/*  89 */   private volatile MessageHandler textMessageHandler = null;
/*     */   
/*  91 */   private volatile MessageHandler binaryMessageHandler = null;
/*  92 */   private volatile MessageHandler.Whole<PongMessage> pongMessageHandler = null;
/*  93 */   private volatile State state = State.OPEN;
/*  94 */   private final Object stateLock = new Object();
/*  95 */   private final Map<String, Object> userProperties = new ConcurrentHashMap();
/*  96 */   private volatile int maxBinaryMessageBufferSize = Constants.DEFAULT_BUFFER_SIZE;
/*  97 */   private volatile int maxTextMessageBufferSize = Constants.DEFAULT_BUFFER_SIZE;
/*  98 */   private volatile long maxIdleTimeout = 0L;
/*  99 */   private volatile long lastActive = System.currentTimeMillis();
/* 100 */   private Map<FutureToSendHandler, FutureToSendHandler> futures = new ConcurrentHashMap();
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
/*     */   private WsFrameBase wsFrame;
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
/*     */   public WsSession(Endpoint localEndpoint, WsRemoteEndpointImplBase wsRemoteEndpoint, WsWebSocketContainer wsWebSocketContainer, URI requestUri, Map<String, List<String>> requestParameterMap, String queryString, Principal userPrincipal, String httpSessionId, List<Extension> negotiatedExtensions, String subProtocol, Map<String, String> pathParameters, boolean secure, EndpointConfig endpointConfig)
/*     */     throws DeploymentException
/*     */   {
/* 144 */     this.localEndpoint = localEndpoint;
/* 145 */     this.wsRemoteEndpoint = wsRemoteEndpoint;
/* 146 */     this.wsRemoteEndpoint.setSession(this);
/* 147 */     this.remoteEndpointAsync = new WsRemoteEndpointAsync(wsRemoteEndpoint);
/* 148 */     this.remoteEndpointBasic = new WsRemoteEndpointBasic(wsRemoteEndpoint);
/* 149 */     this.webSocketContainer = wsWebSocketContainer;
/* 150 */     this.applicationClassLoader = Thread.currentThread().getContextClassLoader();
/* 151 */     wsRemoteEndpoint.setSendTimeout(wsWebSocketContainer.getDefaultAsyncSendTimeout());
/* 152 */     this.maxBinaryMessageBufferSize = this.webSocketContainer.getDefaultMaxBinaryMessageBufferSize();
/* 153 */     this.maxTextMessageBufferSize = this.webSocketContainer.getDefaultMaxTextMessageBufferSize();
/* 154 */     this.maxIdleTimeout = this.webSocketContainer.getDefaultMaxSessionIdleTimeout();
/* 155 */     this.requestUri = requestUri;
/* 156 */     if (requestParameterMap == null) {
/* 157 */       this.requestParameterMap = Collections.emptyMap();
/*     */     } else {
/* 159 */       this.requestParameterMap = requestParameterMap;
/*     */     }
/* 161 */     this.queryString = queryString;
/* 162 */     this.userPrincipal = userPrincipal;
/* 163 */     this.httpSessionId = httpSessionId;
/* 164 */     this.negotiatedExtensions = negotiatedExtensions;
/* 165 */     if (subProtocol == null) {
/* 166 */       this.subProtocol = "";
/*     */     } else {
/* 168 */       this.subProtocol = subProtocol;
/*     */     }
/* 170 */     this.pathParameters = pathParameters;
/* 171 */     this.secure = secure;
/* 172 */     this.wsRemoteEndpoint.setEncoders(endpointConfig);
/* 173 */     this.endpointConfig = endpointConfig;
/*     */     
/* 175 */     this.userProperties.putAll(endpointConfig.getUserProperties());
/* 176 */     this.id = Long.toHexString(ids.getAndIncrement());
/*     */     
/* 178 */     InstanceManager instanceManager = this.webSocketContainer.getInstanceManager();
/* 179 */     if (instanceManager == null) {
/* 180 */       instanceManager = InstanceManagerBindings.get(this.applicationClassLoader);
/*     */     }
/* 182 */     if (instanceManager != null) {
/*     */       try {
/* 184 */         instanceManager.newInstance(localEndpoint);
/*     */       } catch (Exception e) {
/* 186 */         throw new DeploymentException(sm.getString("wsSession.instanceNew"), e);
/*     */       }
/*     */     }
/*     */     
/* 190 */     if (this.log.isDebugEnabled()) {
/* 191 */       this.log.debug(sm.getString("wsSession.created", new Object[] { this.id }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public WebSocketContainer getContainer()
/*     */   {
/* 198 */     checkState();
/* 199 */     return this.webSocketContainer;
/*     */   }
/*     */   
/*     */ 
/*     */   public void addMessageHandler(MessageHandler listener)
/*     */   {
/* 205 */     Class<?> target = Util.getMessageType(listener);
/* 206 */     doAddMessageHandler(target, listener);
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> void addMessageHandler(Class<T> clazz, MessageHandler.Partial<T> handler)
/*     */     throws IllegalStateException
/*     */   {
/* 213 */     doAddMessageHandler(clazz, handler);
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> void addMessageHandler(Class<T> clazz, MessageHandler.Whole<T> handler)
/*     */     throws IllegalStateException
/*     */   {
/* 220 */     doAddMessageHandler(clazz, handler);
/*     */   }
/*     */   
/*     */ 
/*     */   private void doAddMessageHandler(Class<?> target, MessageHandler listener)
/*     */   {
/* 226 */     checkState();
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
/* 238 */     Set<MessageHandlerResult> mhResults = Util.getMessageHandlers(target, listener, this.endpointConfig, this);
/*     */     
/*     */ 
/* 241 */     for (MessageHandlerResult mhResult : mhResults) {
/* 242 */       switch (mhResult.getType()) {
/*     */       case TEXT: 
/* 244 */         if (this.textMessageHandler != null) {
/* 245 */           throw new IllegalStateException(sm.getString("wsSession.duplicateHandlerText"));
/*     */         }
/* 247 */         this.textMessageHandler = mhResult.getHandler();
/* 248 */         break;
/*     */       
/*     */       case BINARY: 
/* 251 */         if (this.binaryMessageHandler != null)
/*     */         {
/* 253 */           throw new IllegalStateException(sm.getString("wsSession.duplicateHandlerBinary"));
/*     */         }
/* 255 */         this.binaryMessageHandler = mhResult.getHandler();
/* 256 */         break;
/*     */       
/*     */       case PONG: 
/* 259 */         if (this.pongMessageHandler != null) {
/* 260 */           throw new IllegalStateException(sm.getString("wsSession.duplicateHandlerPong"));
/*     */         }
/* 262 */         MessageHandler handler = mhResult.getHandler();
/* 263 */         if ((handler instanceof MessageHandler.Whole)) {
/* 264 */           this.pongMessageHandler = ((MessageHandler.Whole)handler);
/*     */         }
/*     */         else {
/* 267 */           throw new IllegalStateException(sm.getString("wsSession.invalidHandlerTypePong"));
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */         break;
/*     */       default: 
/* 274 */         throw new IllegalArgumentException(sm.getString("wsSession.unknownHandlerType", new Object[] { listener, mhResult.getType() }));
/*     */       }
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public Set<MessageHandler> getMessageHandlers()
/*     */   {
/* 283 */     checkState();
/* 284 */     Set<MessageHandler> result = new HashSet();
/* 285 */     if (this.binaryMessageHandler != null) {
/* 286 */       result.add(this.binaryMessageHandler);
/*     */     }
/* 288 */     if (this.textMessageHandler != null) {
/* 289 */       result.add(this.textMessageHandler);
/*     */     }
/* 291 */     if (this.pongMessageHandler != null) {
/* 292 */       result.add(this.pongMessageHandler);
/*     */     }
/* 294 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public void removeMessageHandler(MessageHandler listener)
/*     */   {
/* 300 */     checkState();
/* 301 */     if (listener == null) {
/* 302 */       return;
/*     */     }
/*     */     
/* 305 */     MessageHandler wrapped = null;
/*     */     
/* 307 */     if ((listener instanceof WrappedMessageHandler)) {
/* 308 */       wrapped = ((WrappedMessageHandler)listener).getWrappedHandler();
/*     */     }
/*     */     
/* 311 */     if (wrapped == null) {
/* 312 */       wrapped = listener;
/*     */     }
/*     */     
/* 315 */     boolean removed = false;
/* 316 */     if ((wrapped.equals(this.textMessageHandler)) || (listener.equals(this.textMessageHandler))) {
/* 317 */       this.textMessageHandler = null;
/* 318 */       removed = true;
/*     */     }
/*     */     
/* 321 */     if ((wrapped.equals(this.binaryMessageHandler)) || (listener.equals(this.binaryMessageHandler))) {
/* 322 */       this.binaryMessageHandler = null;
/* 323 */       removed = true;
/*     */     }
/*     */     
/* 326 */     if ((wrapped.equals(this.pongMessageHandler)) || (listener.equals(this.pongMessageHandler))) {
/* 327 */       this.pongMessageHandler = null;
/* 328 */       removed = true;
/*     */     }
/*     */     
/* 331 */     if (!removed)
/*     */     {
/*     */ 
/*     */ 
/* 335 */       throw new IllegalStateException(sm.getString("wsSession.removeHandlerFailed", new Object[] { listener }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String getProtocolVersion()
/*     */   {
/* 342 */     checkState();
/* 343 */     return "13";
/*     */   }
/*     */   
/*     */ 
/*     */   public String getNegotiatedSubprotocol()
/*     */   {
/* 349 */     checkState();
/* 350 */     return this.subProtocol;
/*     */   }
/*     */   
/*     */ 
/*     */   public List<Extension> getNegotiatedExtensions()
/*     */   {
/* 356 */     checkState();
/* 357 */     return this.negotiatedExtensions;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isSecure()
/*     */   {
/* 363 */     checkState();
/* 364 */     return this.secure;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isOpen()
/*     */   {
/* 370 */     return this.state == State.OPEN;
/*     */   }
/*     */   
/*     */ 
/*     */   public long getMaxIdleTimeout()
/*     */   {
/* 376 */     checkState();
/* 377 */     return this.maxIdleTimeout;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setMaxIdleTimeout(long timeout)
/*     */   {
/* 383 */     checkState();
/* 384 */     this.maxIdleTimeout = timeout;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setMaxBinaryMessageBufferSize(int max)
/*     */   {
/* 390 */     checkState();
/* 391 */     this.maxBinaryMessageBufferSize = max;
/*     */   }
/*     */   
/*     */ 
/*     */   public int getMaxBinaryMessageBufferSize()
/*     */   {
/* 397 */     checkState();
/* 398 */     return this.maxBinaryMessageBufferSize;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setMaxTextMessageBufferSize(int max)
/*     */   {
/* 404 */     checkState();
/* 405 */     this.maxTextMessageBufferSize = max;
/*     */   }
/*     */   
/*     */ 
/*     */   public int getMaxTextMessageBufferSize()
/*     */   {
/* 411 */     checkState();
/* 412 */     return this.maxTextMessageBufferSize;
/*     */   }
/*     */   
/*     */ 
/*     */   public Set<Session> getOpenSessions()
/*     */   {
/* 418 */     checkState();
/* 419 */     return this.webSocketContainer.getOpenSessions(this.localEndpoint);
/*     */   }
/*     */   
/*     */ 
/*     */   public RemoteEndpoint.Async getAsyncRemote()
/*     */   {
/* 425 */     checkState();
/* 426 */     return this.remoteEndpointAsync;
/*     */   }
/*     */   
/*     */ 
/*     */   public RemoteEndpoint.Basic getBasicRemote()
/*     */   {
/* 432 */     checkState();
/* 433 */     return this.remoteEndpointBasic;
/*     */   }
/*     */   
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 439 */     close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, ""));
/*     */   }
/*     */   
/*     */   public void close(CloseReason closeReason)
/*     */     throws IOException
/*     */   {
/* 445 */     doClose(closeReason, closeReason);
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
/*     */   public void doClose(CloseReason closeReasonMessage, CloseReason closeReasonLocal)
/*     */   {
/* 459 */     if (this.state != State.OPEN) {
/* 460 */       return;
/*     */     }
/*     */     
/* 463 */     synchronized (this.stateLock) {
/* 464 */       if (this.state != State.OPEN) {
/* 465 */         return;
/*     */       }
/*     */       
/* 468 */       if (this.log.isDebugEnabled()) {
/* 469 */         this.log.debug(sm.getString("wsSession.doClose", new Object[] { this.id }));
/*     */       }
/*     */       try {
/* 472 */         this.wsRemoteEndpoint.setBatchingAllowed(false);
/*     */       } catch (IOException e) {
/* 474 */         this.log.warn(sm.getString("wsSession.flushFailOnClose"), e);
/* 475 */         fireEndpointOnError(e);
/*     */       }
/*     */       
/* 478 */       this.state = State.OUTPUT_CLOSED;
/*     */       
/* 480 */       sendCloseMessage(closeReasonMessage);
/* 481 */       fireEndpointOnClose(closeReasonLocal);
/*     */     }
/*     */     
/* 484 */     IOException ioe = new IOException(sm.getString("wsSession.messageFailed"));
/* 485 */     SendResult sr = new SendResult(ioe);
/* 486 */     for (FutureToSendHandler f2sh : this.futures.keySet()) {
/* 487 */       f2sh.onResult(sr);
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
/*     */ 
/*     */   public void onClose(CloseReason closeReason)
/*     */   {
/* 502 */     synchronized (this.stateLock) {
/* 503 */       if (this.state != State.CLOSED) {
/*     */         try {
/* 505 */           this.wsRemoteEndpoint.setBatchingAllowed(false);
/*     */         } catch (IOException e) {
/* 507 */           this.log.warn(sm.getString("wsSession.flushFailOnClose"), e);
/* 508 */           fireEndpointOnError(e);
/*     */         }
/* 510 */         if (this.state == State.OPEN) {
/* 511 */           this.state = State.OUTPUT_CLOSED;
/* 512 */           sendCloseMessage(closeReason);
/* 513 */           fireEndpointOnClose(closeReason);
/*     */         }
/* 515 */         this.state = State.CLOSED;
/*     */         
/*     */ 
/* 518 */         this.wsRemoteEndpoint.close();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void fireEndpointOnClose(CloseReason closeReason)
/*     */   {
/* 526 */     Throwable throwable = null;
/* 527 */     InstanceManager instanceManager = this.webSocketContainer.getInstanceManager();
/* 528 */     if (instanceManager == null) {
/* 529 */       instanceManager = InstanceManagerBindings.get(this.applicationClassLoader);
/*     */     }
/* 531 */     Thread t = Thread.currentThread();
/* 532 */     ClassLoader cl = t.getContextClassLoader();
/* 533 */     t.setContextClassLoader(this.applicationClassLoader);
/*     */     try {
/* 535 */       this.localEndpoint.onClose(this, closeReason);
/*     */     } catch (Throwable t1) {
/* 537 */       ExceptionUtils.handleThrowable(t1);
/* 538 */       throwable = t1;
/*     */     } finally {
/* 540 */       if (instanceManager != null) {
/*     */         try {
/* 542 */           instanceManager.destroyInstance(this.localEndpoint);
/*     */         } catch (Throwable t2) {
/* 544 */           ExceptionUtils.handleThrowable(t2);
/* 545 */           if (throwable == null) {
/* 546 */             throwable = t2;
/*     */           }
/*     */         }
/*     */       }
/* 550 */       t.setContextClassLoader(cl);
/*     */     }
/*     */     
/* 553 */     if (throwable != null) {
/* 554 */       fireEndpointOnError(throwable);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void fireEndpointOnError(Throwable throwable)
/*     */   {
/* 563 */     Thread t = Thread.currentThread();
/* 564 */     ClassLoader cl = t.getContextClassLoader();
/* 565 */     t.setContextClassLoader(this.applicationClassLoader);
/*     */     try {
/* 567 */       this.localEndpoint.onError(this, throwable);
/*     */     } finally {
/* 569 */       t.setContextClassLoader(cl);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void sendCloseMessage(CloseReason closeReason)
/*     */   {
/* 576 */     ByteBuffer msg = ByteBuffer.allocate(125);
/* 577 */     CloseReason.CloseCode closeCode = closeReason.getCloseCode();
/*     */     
/* 579 */     if (closeCode == CloseReason.CloseCodes.CLOSED_ABNORMALLY)
/*     */     {
/* 581 */       msg.putShort((short)CloseReason.CloseCodes.PROTOCOL_ERROR.getCode());
/*     */     } else {
/* 583 */       msg.putShort((short)closeCode.getCode());
/*     */     }
/*     */     
/* 586 */     String reason = closeReason.getReasonPhrase();
/* 587 */     if ((reason != null) && (reason.length() > 0)) {
/* 588 */       appendCloseReasonWithTruncation(msg, reason);
/*     */     }
/* 590 */     msg.flip();
/*     */     try {
/* 592 */       this.wsRemoteEndpoint.sendMessageBlock((byte)8, msg, true);
/*     */     }
/*     */     catch (IOException|WritePendingException e)
/*     */     {
/* 596 */       if (this.log.isDebugEnabled()) {
/* 597 */         this.log.debug(sm.getString("wsSession.sendCloseFail", new Object[] { this.id }), e);
/*     */       }
/* 599 */       this.wsRemoteEndpoint.close();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 604 */       if (closeCode != CloseReason.CloseCodes.CLOSED_ABNORMALLY) {
/* 605 */         this.localEndpoint.onError(this, e);
/*     */       }
/*     */     } finally {
/* 608 */       this.webSocketContainer.unregisterSession(this.localEndpoint, this);
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
/*     */ 
/*     */   protected static void appendCloseReasonWithTruncation(ByteBuffer msg, String reason)
/*     */   {
/* 623 */     byte[] reasonBytes = reason.getBytes(StandardCharsets.UTF_8);
/*     */     
/* 625 */     if (reasonBytes.length <= 123)
/*     */     {
/* 627 */       msg.put(reasonBytes);
/*     */     }
/*     */     else {
/* 630 */       int remaining = 123 - ELLIPSIS_BYTES_LEN;
/* 631 */       int pos = 0;
/* 632 */       byte[] bytesNext = reason.substring(pos, pos + 1).getBytes(StandardCharsets.UTF_8);
/* 633 */       while (remaining >= bytesNext.length) {
/* 634 */         msg.put(bytesNext);
/* 635 */         remaining -= bytesNext.length;
/* 636 */         pos++;
/* 637 */         bytesNext = reason.substring(pos, pos + 1).getBytes(StandardCharsets.UTF_8);
/*     */       }
/* 639 */       msg.put(ELLIPSIS_BYTES);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void registerFuture(FutureToSendHandler f2sh)
/*     */   {
/* 658 */     this.futures.put(f2sh, f2sh);
/*     */     
/* 660 */     if (this.state == State.OPEN)
/*     */     {
/*     */ 
/* 663 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 669 */     if (f2sh.isDone())
/*     */     {
/*     */ 
/*     */ 
/* 673 */       return;
/*     */     }
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
/* 690 */     IOException ioe = new IOException(sm.getString("wsSession.messageFailed"));
/* 691 */     SendResult sr = new SendResult(ioe);
/* 692 */     f2sh.onResult(sr);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void unregisterFuture(FutureToSendHandler f2sh)
/*     */   {
/* 701 */     this.futures.remove(f2sh);
/*     */   }
/*     */   
/*     */ 
/*     */   public URI getRequestURI()
/*     */   {
/* 707 */     checkState();
/* 708 */     return this.requestUri;
/*     */   }
/*     */   
/*     */ 
/*     */   public Map<String, List<String>> getRequestParameterMap()
/*     */   {
/* 714 */     checkState();
/* 715 */     return this.requestParameterMap;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getQueryString()
/*     */   {
/* 721 */     checkState();
/* 722 */     return this.queryString;
/*     */   }
/*     */   
/*     */ 
/*     */   public Principal getUserPrincipal()
/*     */   {
/* 728 */     checkState();
/* 729 */     return this.userPrincipal;
/*     */   }
/*     */   
/*     */ 
/*     */   public Map<String, String> getPathParameters()
/*     */   {
/* 735 */     checkState();
/* 736 */     return this.pathParameters;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getId()
/*     */   {
/* 742 */     return this.id;
/*     */   }
/*     */   
/*     */ 
/*     */   public Map<String, Object> getUserProperties()
/*     */   {
/* 748 */     checkState();
/* 749 */     return this.userProperties;
/*     */   }
/*     */   
/*     */   public Endpoint getLocal()
/*     */   {
/* 754 */     return this.localEndpoint;
/*     */   }
/*     */   
/*     */   public String getHttpSessionId()
/*     */   {
/* 759 */     return this.httpSessionId;
/*     */   }
/*     */   
/*     */   protected MessageHandler getTextMessageHandler()
/*     */   {
/* 764 */     return this.textMessageHandler;
/*     */   }
/*     */   
/*     */   protected MessageHandler getBinaryMessageHandler()
/*     */   {
/* 769 */     return this.binaryMessageHandler;
/*     */   }
/*     */   
/*     */   protected MessageHandler.Whole<PongMessage> getPongMessageHandler()
/*     */   {
/* 774 */     return this.pongMessageHandler;
/*     */   }
/*     */   
/*     */   protected void updateLastActive()
/*     */   {
/* 779 */     this.lastActive = System.currentTimeMillis();
/*     */   }
/*     */   
/*     */   protected void checkExpiration()
/*     */   {
/* 784 */     long timeout = this.maxIdleTimeout;
/* 785 */     if (timeout < 1L) {
/* 786 */       return;
/*     */     }
/*     */     
/* 789 */     if (System.currentTimeMillis() - this.lastActive > timeout) {
/* 790 */       String msg = sm.getString("wsSession.timeout", new Object[] { getId() });
/* 791 */       if (this.log.isDebugEnabled()) {
/* 792 */         this.log.debug(msg);
/*     */       }
/* 794 */       doClose(new CloseReason(CloseReason.CloseCodes.GOING_AWAY, msg), new CloseReason(CloseReason.CloseCodes.CLOSED_ABNORMALLY, msg));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void checkState()
/*     */   {
/* 801 */     if (this.state == State.CLOSED)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 806 */       throw new IllegalStateException(sm.getString("wsSession.closed", new Object[] { this.id }));
/*     */     }
/*     */   }
/*     */   
/*     */   private static enum State {
/* 811 */     OPEN, 
/* 812 */     OUTPUT_CLOSED, 
/* 813 */     CLOSED;
/*     */     
/*     */     private State() {}
/*     */   }
/*     */   
/*     */   void setWsFrame(WsFrameBase wsFrame) {
/* 819 */     this.wsFrame = wsFrame;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void suspend()
/*     */   {
/* 827 */     this.wsFrame.suspend();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resume()
/*     */   {
/* 835 */     this.wsFrame.resume();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\WsSession.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */