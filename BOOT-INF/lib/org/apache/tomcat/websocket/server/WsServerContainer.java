/*     */ package org.apache.tomcat.websocket.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import javax.servlet.DispatcherType;
/*     */ import javax.servlet.FilterRegistration.Dynamic;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.websocket.CloseReason;
/*     */ import javax.websocket.CloseReason.CloseCodes;
/*     */ import javax.websocket.DeploymentException;
/*     */ import javax.websocket.Encoder;
/*     */ import javax.websocket.Endpoint;
/*     */ import javax.websocket.server.ServerContainer;
/*     */ import javax.websocket.server.ServerEndpoint;
/*     */ import javax.websocket.server.ServerEndpointConfig;
/*     */ import javax.websocket.server.ServerEndpointConfig.Builder;
/*     */ import javax.websocket.server.ServerEndpointConfig.Configurator;
/*     */ import org.apache.tomcat.InstanceManager;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ import org.apache.tomcat.websocket.Constants;
/*     */ import org.apache.tomcat.websocket.WsSession;
/*     */ import org.apache.tomcat.websocket.WsWebSocketContainer;
/*     */ import org.apache.tomcat.websocket.pojo.PojoMethodMapping;
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
/*     */ public class WsServerContainer
/*     */   extends WsWebSocketContainer
/*     */   implements ServerContainer
/*     */ {
/*  66 */   private static final StringManager sm = StringManager.getManager(WsServerContainer.class);
/*     */   
/*  68 */   private static final CloseReason AUTHENTICATED_HTTP_SESSION_CLOSED = new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "This connection was established under an authenticated HTTP session that has ended.");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  73 */   private final WsWriteTimeout wsWriteTimeout = new WsWriteTimeout();
/*     */   
/*     */   private final ServletContext servletContext;
/*  76 */   private final Map<String, ServerEndpointConfig> configExactMatchMap = new ConcurrentHashMap();
/*     */   
/*  78 */   private final ConcurrentMap<Integer, SortedSet<TemplatePathMatch>> configTemplateMatchMap = new ConcurrentHashMap();
/*     */   
/*  80 */   private volatile boolean enforceNoAddAfterHandshake = Constants.STRICT_SPEC_COMPLIANCE;
/*     */   
/*  82 */   private volatile boolean addAllowed = true;
/*  83 */   private final ConcurrentMap<String, Set<WsSession>> authenticatedSessions = new ConcurrentHashMap();
/*     */   
/*  85 */   private volatile boolean endpointsRegistered = false;
/*     */   
/*     */   WsServerContainer(ServletContext servletContext)
/*     */   {
/*  89 */     this.servletContext = servletContext;
/*  90 */     setInstanceManager((InstanceManager)servletContext.getAttribute(InstanceManager.class.getName()));
/*     */     
/*     */ 
/*  93 */     String value = servletContext.getInitParameter("org.apache.tomcat.websocket.binaryBufferSize");
/*     */     
/*  95 */     if (value != null) {
/*  96 */       setDefaultMaxBinaryMessageBufferSize(Integer.parseInt(value));
/*     */     }
/*     */     
/*  99 */     value = servletContext.getInitParameter("org.apache.tomcat.websocket.textBufferSize");
/*     */     
/* 101 */     if (value != null) {
/* 102 */       setDefaultMaxTextMessageBufferSize(Integer.parseInt(value));
/*     */     }
/*     */     
/* 105 */     value = servletContext.getInitParameter("org.apache.tomcat.websocket.noAddAfterHandshake");
/*     */     
/* 107 */     if (value != null) {
/* 108 */       setEnforceNoAddAfterHandshake(Boolean.parseBoolean(value));
/*     */     }
/*     */     
/* 111 */     FilterRegistration.Dynamic fr = servletContext.addFilter("Tomcat WebSocket (JSR356) Filter", new WsFilter());
/*     */     
/* 113 */     fr.setAsyncSupported(true);
/*     */     
/* 115 */     EnumSet<DispatcherType> types = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD);
/*     */     
/*     */ 
/* 118 */     fr.addMappingForUrlPatterns(types, true, new String[] { "/*" });
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
/*     */   public void addEndpoint(ServerEndpointConfig sec)
/*     */     throws DeploymentException
/*     */   {
/* 135 */     if ((this.enforceNoAddAfterHandshake) && (!this.addAllowed))
/*     */     {
/* 137 */       throw new DeploymentException(sm.getString("serverContainer.addNotAllowed"));
/*     */     }
/*     */     
/* 140 */     if (this.servletContext == null)
/*     */     {
/* 142 */       throw new DeploymentException(sm.getString("serverContainer.servletContextMissing"));
/*     */     }
/* 144 */     String path = sec.getPath();
/*     */     
/*     */ 
/*     */ 
/* 148 */     PojoMethodMapping methodMapping = new PojoMethodMapping(sec.getEndpointClass(), sec.getDecoders(), path);
/* 149 */     if ((methodMapping.getOnClose() != null) || (methodMapping.getOnOpen() != null) || 
/* 150 */       (methodMapping.getOnError() != null) || (methodMapping.hasMessageHandlers())) {
/* 151 */       sec.getUserProperties().put("org.apache.tomcat.websocket.pojo.PojoEndpoint.methodMapping", methodMapping);
/*     */     }
/*     */     
/*     */ 
/* 155 */     UriTemplate uriTemplate = new UriTemplate(path);
/* 156 */     if (uriTemplate.hasParameters()) {
/* 157 */       Integer key = Integer.valueOf(uriTemplate.getSegmentCount());
/*     */       
/* 159 */       SortedSet<TemplatePathMatch> templateMatches = (SortedSet)this.configTemplateMatchMap.get(key);
/* 160 */       if (templateMatches == null)
/*     */       {
/*     */ 
/*     */ 
/* 164 */         templateMatches = new TreeSet(TemplatePathMatchComparator.getInstance());
/* 165 */         this.configTemplateMatchMap.putIfAbsent(key, templateMatches);
/* 166 */         templateMatches = (SortedSet)this.configTemplateMatchMap.get(key);
/*     */       }
/* 168 */       if (!templateMatches.add(new TemplatePathMatch(sec, uriTemplate)))
/*     */       {
/*     */ 
/* 171 */         throw new DeploymentException(sm.getString("serverContainer.duplicatePaths", new Object[] { path, sec
/* 172 */           .getEndpointClass(), sec
/* 173 */           .getEndpointClass() }));
/*     */       }
/*     */     }
/*     */     else {
/* 177 */       ServerEndpointConfig old = (ServerEndpointConfig)this.configExactMatchMap.put(path, sec);
/* 178 */       if (old != null)
/*     */       {
/*     */ 
/* 181 */         throw new DeploymentException(sm.getString("serverContainer.duplicatePaths", new Object[] { path, old
/* 182 */           .getEndpointClass(), sec
/* 183 */           .getEndpointClass() }));
/*     */       }
/*     */     }
/*     */     
/* 187 */     this.endpointsRegistered = true;
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
/*     */   public void addEndpoint(Class<?> pojo)
/*     */     throws DeploymentException
/*     */   {
/* 201 */     ServerEndpoint annotation = (ServerEndpoint)pojo.getAnnotation(ServerEndpoint.class);
/* 202 */     if (annotation == null)
/*     */     {
/* 204 */       throw new DeploymentException(sm.getString("serverContainer.missingAnnotation", new Object[] {pojo
/* 205 */         .getName() }));
/*     */     }
/* 207 */     String path = annotation.value();
/*     */     
/*     */ 
/* 210 */     validateEncoders(annotation.encoders());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 215 */     Class<? extends ServerEndpointConfig.Configurator> configuratorClazz = annotation.configurator();
/* 216 */     ServerEndpointConfig.Configurator configurator = null;
/* 217 */     if (!configuratorClazz.equals(ServerEndpointConfig.Configurator.class)) {
/*     */       try {
/* 219 */         configurator = (ServerEndpointConfig.Configurator)annotation.configurator().getConstructor(new Class[0]).newInstance(new Object[0]);
/*     */       } catch (ReflectiveOperationException e) {
/* 221 */         throw new DeploymentException(sm.getString("serverContainer.configuratorFail", new Object[] {annotation
/*     */         
/* 223 */           .configurator().getName(), pojo
/* 224 */           .getClass().getName() }), e);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 232 */     ServerEndpointConfig sec = ServerEndpointConfig.Builder.create(pojo, path).decoders(Arrays.asList(annotation.decoders())).encoders(Arrays.asList(annotation.encoders())).subprotocols(Arrays.asList(annotation.subprotocols())).configurator(configurator).build();
/*     */     
/* 234 */     addEndpoint(sec);
/*     */   }
/*     */   
/*     */   boolean areEndpointsRegistered()
/*     */   {
/* 239 */     return this.endpointsRegistered;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doUpgrade(HttpServletRequest request, HttpServletResponse response, ServerEndpointConfig sec, Map<String, String> pathParams)
/*     */     throws ServletException, IOException
/*     */   {
/* 265 */     UpgradeUtil.doUpgrade(this, request, response, sec, pathParams);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public WsMappingResult findMapping(String path)
/*     */   {
/* 273 */     if (this.addAllowed) {
/* 274 */       this.addAllowed = false;
/*     */     }
/*     */     
/*     */ 
/* 278 */     ServerEndpointConfig sec = (ServerEndpointConfig)this.configExactMatchMap.get(path);
/* 279 */     if (sec != null) {
/* 280 */       return new WsMappingResult(sec, Collections.emptyMap());
/*     */     }
/*     */     
/*     */ 
/* 284 */     UriTemplate pathUriTemplate = null;
/*     */     try {
/* 286 */       pathUriTemplate = new UriTemplate(path);
/*     */     }
/*     */     catch (DeploymentException e) {
/* 289 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 293 */     Integer key = Integer.valueOf(pathUriTemplate.getSegmentCount());
/*     */     
/* 295 */     SortedSet<TemplatePathMatch> templateMatches = (SortedSet)this.configTemplateMatchMap.get(key);
/*     */     
/* 297 */     if (templateMatches == null)
/*     */     {
/*     */ 
/* 300 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 305 */     Map<String, String> pathParams = null;
/* 306 */     for (TemplatePathMatch templateMatch : templateMatches) {
/* 307 */       pathParams = templateMatch.getUriTemplate().match(pathUriTemplate);
/* 308 */       if (pathParams != null) {
/* 309 */         sec = templateMatch.getConfig();
/* 310 */         break;
/*     */       }
/*     */     }
/*     */     
/* 314 */     if (sec == null)
/*     */     {
/* 316 */       return null;
/*     */     }
/*     */     
/* 319 */     return new WsMappingResult(sec, pathParams);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isEnforceNoAddAfterHandshake()
/*     */   {
/* 325 */     return this.enforceNoAddAfterHandshake;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setEnforceNoAddAfterHandshake(boolean enforceNoAddAfterHandshake)
/*     */   {
/* 331 */     this.enforceNoAddAfterHandshake = enforceNoAddAfterHandshake;
/*     */   }
/*     */   
/*     */   protected WsWriteTimeout getTimeout()
/*     */   {
/* 336 */     return this.wsWriteTimeout;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void registerSession(Endpoint endpoint, WsSession wsSession)
/*     */   {
/* 347 */     super.registerSession(endpoint, wsSession);
/* 348 */     if ((wsSession.isOpen()) && 
/* 349 */       (wsSession.getUserPrincipal() != null) && 
/* 350 */       (wsSession.getHttpSessionId() != null)) {
/* 351 */       registerAuthenticatedSession(wsSession, wsSession
/* 352 */         .getHttpSessionId());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void unregisterSession(Endpoint endpoint, WsSession wsSession)
/*     */   {
/* 364 */     if ((wsSession.getUserPrincipal() != null) && 
/* 365 */       (wsSession.getHttpSessionId() != null)) {
/* 366 */       unregisterAuthenticatedSession(wsSession, wsSession
/* 367 */         .getHttpSessionId());
/*     */     }
/* 369 */     super.unregisterSession(endpoint, wsSession);
/*     */   }
/*     */   
/*     */ 
/*     */   private void registerAuthenticatedSession(WsSession wsSession, String httpSessionId)
/*     */   {
/* 375 */     Set<WsSession> wsSessions = (Set)this.authenticatedSessions.get(httpSessionId);
/* 376 */     if (wsSessions == null) {
/* 377 */       wsSessions = Collections.newSetFromMap(new ConcurrentHashMap());
/*     */       
/* 379 */       this.authenticatedSessions.putIfAbsent(httpSessionId, wsSessions);
/* 380 */       wsSessions = (Set)this.authenticatedSessions.get(httpSessionId);
/*     */     }
/* 382 */     wsSessions.add(wsSession);
/*     */   }
/*     */   
/*     */ 
/*     */   private void unregisterAuthenticatedSession(WsSession wsSession, String httpSessionId)
/*     */   {
/* 388 */     Set<WsSession> wsSessions = (Set)this.authenticatedSessions.get(httpSessionId);
/*     */     
/* 390 */     if (wsSessions != null) {
/* 391 */       wsSessions.remove(wsSession);
/*     */     }
/*     */   }
/*     */   
/*     */   public void closeAuthenticatedSession(String httpSessionId)
/*     */   {
/* 397 */     Set<WsSession> wsSessions = (Set)this.authenticatedSessions.remove(httpSessionId);
/*     */     
/* 399 */     if ((wsSessions != null) && (!wsSessions.isEmpty())) {
/* 400 */       for (WsSession wsSession : wsSessions) {
/*     */         try {
/* 402 */           wsSession.close(AUTHENTICATED_HTTP_SESSION_CLOSED);
/*     */         }
/*     */         catch (IOException localIOException) {}
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void validateEncoders(Class<? extends Encoder>[] encoders)
/*     */     throws DeploymentException
/*     */   {
/* 415 */     for (Class<? extends Encoder> encoder : encoders)
/*     */     {
/*     */ 
/*     */       try
/*     */       {
/*     */ 
/* 421 */         encoder.getConstructor(new Class[0]).newInstance(new Object[0]);
/*     */       } catch (ReflectiveOperationException e) {
/* 423 */         throw new DeploymentException(sm.getString("serverContainer.encoderFail", new Object[] {encoder
/* 424 */           .getName() }), e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TemplatePathMatch
/*     */   {
/*     */     private final ServerEndpointConfig config;
/*     */     private final UriTemplate uriTemplate;
/*     */     
/*     */     public TemplatePathMatch(ServerEndpointConfig config, UriTemplate uriTemplate)
/*     */     {
/* 436 */       this.config = config;
/* 437 */       this.uriTemplate = uriTemplate;
/*     */     }
/*     */     
/*     */     public ServerEndpointConfig getConfig()
/*     */     {
/* 442 */       return this.config;
/*     */     }
/*     */     
/*     */     public UriTemplate getUriTemplate()
/*     */     {
/* 447 */       return this.uriTemplate;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class TemplatePathMatchComparator
/*     */     implements Comparator<WsServerContainer.TemplatePathMatch>
/*     */   {
/* 459 */     private static final TemplatePathMatchComparator INSTANCE = new TemplatePathMatchComparator();
/*     */     
/*     */     public static TemplatePathMatchComparator getInstance()
/*     */     {
/* 463 */       return INSTANCE;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public int compare(WsServerContainer.TemplatePathMatch tpm1, WsServerContainer.TemplatePathMatch tpm2)
/*     */     {
/* 472 */       return tpm1.getUriTemplate().getNormalizedPath().compareTo(tpm2
/* 473 */         .getUriTemplate().getNormalizedPath());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\server\WsServerContainer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */