/*     */ package org.apache.tomcat.websocket.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import javax.servlet.http.WebConnection;
/*     */ import javax.websocket.CloseReason;
/*     */ import javax.websocket.CloseReason.CloseCodes;
/*     */ import javax.websocket.DeploymentException;
/*     */ import javax.websocket.Endpoint;
/*     */ import javax.websocket.EndpointConfig;
/*     */ import javax.websocket.Extension;
/*     */ import org.apache.coyote.http11.upgrade.InternalHttpUpgradeHandler;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.net.AbstractEndpoint.Handler.SocketState;
/*     */ import org.apache.tomcat.util.net.SSLSupport;
/*     */ import org.apache.tomcat.util.net.SocketEvent;
/*     */ import org.apache.tomcat.util.net.SocketWrapperBase;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ import org.apache.tomcat.websocket.Transformation;
/*     */ import org.apache.tomcat.websocket.WsIOException;
/*     */ import org.apache.tomcat.websocket.WsSession;
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
/*     */ public class WsHttpUpgradeHandler
/*     */   implements InternalHttpUpgradeHandler
/*     */ {
/*  49 */   private static final Log log = LogFactory.getLog(WsHttpUpgradeHandler.class);
/*  50 */   private static final StringManager sm = StringManager.getManager(WsHttpUpgradeHandler.class);
/*     */   
/*     */   private final ClassLoader applicationClassLoader;
/*     */   
/*     */   private SocketWrapperBase<?> socketWrapper;
/*     */   
/*     */   private Endpoint ep;
/*     */   
/*     */   private EndpointConfig endpointConfig;
/*     */   private WsServerContainer webSocketContainer;
/*     */   private WsHandshakeRequest handshakeRequest;
/*     */   private List<Extension> negotiatedExtensions;
/*     */   private String subProtocol;
/*     */   private Transformation transformation;
/*     */   private Map<String, String> pathParameters;
/*     */   private boolean secure;
/*     */   private WebConnection connection;
/*     */   private WsRemoteEndpointImplServer wsRemoteEndpointServer;
/*     */   private WsFrameServer wsFrame;
/*     */   private WsSession wsSession;
/*     */   
/*     */   public WsHttpUpgradeHandler()
/*     */   {
/*  73 */     this.applicationClassLoader = Thread.currentThread().getContextClassLoader();
/*     */   }
/*     */   
/*     */ 
/*     */   public void setSocketWrapper(SocketWrapperBase<?> socketWrapper)
/*     */   {
/*  79 */     this.socketWrapper = socketWrapper;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void preInit(Endpoint ep, EndpointConfig endpointConfig, WsServerContainer wsc, WsHandshakeRequest handshakeRequest, List<Extension> negotiatedExtensionsPhase2, String subProtocol, Transformation transformation, Map<String, String> pathParameters, boolean secure)
/*     */   {
/*  88 */     this.ep = ep;
/*  89 */     this.endpointConfig = endpointConfig;
/*  90 */     this.webSocketContainer = wsc;
/*  91 */     this.handshakeRequest = handshakeRequest;
/*  92 */     this.negotiatedExtensions = negotiatedExtensionsPhase2;
/*  93 */     this.subProtocol = subProtocol;
/*  94 */     this.transformation = transformation;
/*  95 */     this.pathParameters = pathParameters;
/*  96 */     this.secure = secure;
/*     */   }
/*     */   
/*     */ 
/*     */   public void init(WebConnection connection)
/*     */   {
/* 102 */     if (this.ep == null)
/*     */     {
/* 104 */       throw new IllegalStateException(sm.getString("wsHttpUpgradeHandler.noPreInit"));
/*     */     }
/*     */     
/* 107 */     String httpSessionId = null;
/* 108 */     Object session = this.handshakeRequest.getHttpSession();
/* 109 */     if (session != null) {
/* 110 */       httpSessionId = ((HttpSession)session).getId();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 116 */     Thread t = Thread.currentThread();
/* 117 */     ClassLoader cl = t.getContextClassLoader();
/* 118 */     t.setContextClassLoader(this.applicationClassLoader);
/*     */     try {
/* 120 */       this.wsRemoteEndpointServer = new WsRemoteEndpointImplServer(this.socketWrapper, this.webSocketContainer);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 125 */       this.wsSession = new WsSession(this.ep, this.wsRemoteEndpointServer, this.webSocketContainer, this.handshakeRequest.getRequestURI(), this.handshakeRequest.getParameterMap(), this.handshakeRequest.getQueryString(), this.handshakeRequest.getUserPrincipal(), httpSessionId, this.negotiatedExtensions, this.subProtocol, this.pathParameters, this.secure, this.endpointConfig);
/*     */       
/*     */ 
/* 128 */       this.wsFrame = new WsFrameServer(this.socketWrapper, this.wsSession, this.transformation, this.applicationClassLoader);
/*     */       
/*     */ 
/*     */ 
/* 132 */       this.wsRemoteEndpointServer.setTransformation(this.wsFrame.getTransformation());
/* 133 */       this.ep.onOpen(this.wsSession, this.endpointConfig);
/* 134 */       this.webSocketContainer.registerSession(this.ep, this.wsSession);
/*     */     } catch (DeploymentException e) {
/* 136 */       throw new IllegalArgumentException(e);
/*     */     } finally {
/* 138 */       t.setContextClassLoader(cl);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public AbstractEndpoint.Handler.SocketState upgradeDispatch(SocketEvent status)
/*     */   {
/* 145 */     switch (status) {
/*     */     case OPEN_READ: 
/*     */       try {
/* 148 */         return this.wsFrame.notifyDataAvailable();
/*     */       } catch (WsIOException ws) {
/* 150 */         close(ws.getCloseReason());
/*     */       } catch (IOException ioe) {
/* 152 */         onError(ioe);
/*     */         
/* 154 */         CloseReason cr = new CloseReason(CloseReason.CloseCodes.CLOSED_ABNORMALLY, ioe.getMessage());
/* 155 */         close(cr);
/* 156 */         return AbstractEndpoint.Handler.SocketState.CLOSED;
/*     */       }
/*     */     
/*     */     case OPEN_WRITE: 
/* 160 */       this.wsRemoteEndpointServer.onWritePossible(false);
/* 161 */       break;
/*     */     
/*     */     case STOP: 
/* 164 */       CloseReason cr = new CloseReason(CloseReason.CloseCodes.GOING_AWAY, sm.getString("wsHttpUpgradeHandler.serverStop"));
/*     */       try {
/* 166 */         this.wsSession.close(cr);
/*     */       } catch (IOException ioe) {
/* 168 */         onError(ioe);
/*     */         
/* 170 */         cr = new CloseReason(CloseReason.CloseCodes.CLOSED_ABNORMALLY, ioe.getMessage());
/* 171 */         close(cr);
/* 172 */         return AbstractEndpoint.Handler.SocketState.CLOSED;
/*     */       }
/*     */     
/*     */     case ERROR: 
/* 176 */       String msg = sm.getString("wsHttpUpgradeHandler.closeOnError");
/* 177 */       this.wsSession.doClose(new CloseReason(CloseReason.CloseCodes.GOING_AWAY, msg), new CloseReason(CloseReason.CloseCodes.CLOSED_ABNORMALLY, msg));
/*     */     
/*     */ 
/*     */     case DISCONNECT: 
/*     */     case TIMEOUT: 
/* 182 */       return AbstractEndpoint.Handler.SocketState.CLOSED;
/*     */     }
/*     */     
/* 185 */     if (this.wsFrame.isOpen()) {
/* 186 */       return AbstractEndpoint.Handler.SocketState.UPGRADED;
/*     */     }
/* 188 */     return AbstractEndpoint.Handler.SocketState.CLOSED;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void pause() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void destroy()
/*     */   {
/* 201 */     if (this.connection != null) {
/*     */       try {
/* 203 */         this.connection.close();
/*     */       } catch (Exception e) {
/* 205 */         log.error(sm.getString("wsHttpUpgradeHandler.destroyFailed"), e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void onError(Throwable throwable)
/*     */   {
/* 213 */     Thread t = Thread.currentThread();
/* 214 */     ClassLoader cl = t.getContextClassLoader();
/* 215 */     t.setContextClassLoader(this.applicationClassLoader);
/*     */     try {
/* 217 */       this.ep.onError(this.wsSession, throwable);
/*     */     } finally {
/* 219 */       t.setContextClassLoader(cl);
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
/*     */   private void close(CloseReason cr)
/*     */   {
/* 233 */     this.wsSession.onClose(cr);
/*     */   }
/*     */   
/*     */   public void setSslSupport(SSLSupport sslSupport) {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\server\WsHttpUpgradeHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */