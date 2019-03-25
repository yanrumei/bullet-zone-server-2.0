/*      */ package org.apache.tomcat.websocket;
/*      */ 
/*      */ import java.io.EOFException;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.net.Proxy;
/*      */ import java.net.Proxy.Type;
/*      */ import java.net.ProxySelector;
/*      */ import java.net.SocketAddress;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.channels.AsynchronousChannelGroup;
/*      */ import java.nio.channels.AsynchronousSocketChannel;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.security.KeyStore;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ExecutionException;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.TimeoutException;
/*      */ import javax.net.ssl.SSLContext;
/*      */ import javax.net.ssl.SSLEngine;
/*      */ import javax.net.ssl.SSLException;
/*      */ import javax.net.ssl.TrustManagerFactory;
/*      */ import javax.websocket.ClientEndpoint;
/*      */ import javax.websocket.ClientEndpointConfig;
/*      */ import javax.websocket.ClientEndpointConfig.Builder;
/*      */ import javax.websocket.ClientEndpointConfig.Configurator;
/*      */ import javax.websocket.CloseReason;
/*      */ import javax.websocket.CloseReason.CloseCodes;
/*      */ import javax.websocket.DeploymentException;
/*      */ import javax.websocket.Endpoint;
/*      */ import javax.websocket.Extension;
/*      */ import javax.websocket.Extension.Parameter;
/*      */ import javax.websocket.HandshakeResponse;
/*      */ import javax.websocket.Session;
/*      */ import javax.websocket.WebSocketContainer;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.tomcat.InstanceManager;
/*      */ import org.apache.tomcat.util.buf.StringUtils;
/*      */ import org.apache.tomcat.util.codec.binary.Base64;
/*      */ import org.apache.tomcat.util.collections.CaseInsensitiveKeyMap;
/*      */ import org.apache.tomcat.util.res.StringManager;
/*      */ import org.apache.tomcat.websocket.pojo.PojoEndpointClient;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class WsWebSocketContainer
/*      */   implements WebSocketContainer, BackgroundProcess
/*      */ {
/*   78 */   private static final StringManager sm = StringManager.getManager(WsWebSocketContainer.class);
/*   79 */   private static final Random RANDOM = new Random();
/*   80 */   private static final byte[] CRLF = { 13, 10 };
/*      */   
/*   82 */   private static final byte[] GET_BYTES = "GET ".getBytes(StandardCharsets.ISO_8859_1);
/*   83 */   private static final byte[] ROOT_URI_BYTES = "/".getBytes(StandardCharsets.ISO_8859_1);
/*   84 */   private static final byte[] HTTP_VERSION_BYTES = " HTTP/1.1\r\n"
/*   85 */     .getBytes(StandardCharsets.ISO_8859_1);
/*      */   
/*   87 */   private volatile AsynchronousChannelGroup asynchronousChannelGroup = null;
/*   88 */   private final Object asynchronousChannelGroupLock = new Object();
/*      */   
/*   90 */   private final Log log = LogFactory.getLog(WsWebSocketContainer.class);
/*   91 */   private final Map<Endpoint, Set<WsSession>> endpointSessionMap = new HashMap();
/*      */   
/*   93 */   private final Map<WsSession, WsSession> sessions = new ConcurrentHashMap();
/*   94 */   private final Object endPointSessionMapLock = new Object();
/*      */   
/*   96 */   private long defaultAsyncTimeout = -1L;
/*   97 */   private int maxBinaryMessageBufferSize = Constants.DEFAULT_BUFFER_SIZE;
/*   98 */   private int maxTextMessageBufferSize = Constants.DEFAULT_BUFFER_SIZE;
/*   99 */   private volatile long defaultMaxSessionIdleTimeout = 0L;
/*  100 */   private int backgroundProcessCount = 0;
/*  101 */   private int processPeriod = Constants.DEFAULT_PROCESS_PERIOD;
/*      */   private InstanceManager instanceManager;
/*      */   
/*      */   InstanceManager getInstanceManager()
/*      */   {
/*  106 */     return this.instanceManager;
/*      */   }
/*      */   
/*      */   protected void setInstanceManager(InstanceManager instanceManager) {
/*  110 */     this.instanceManager = instanceManager;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Session connectToServer(Object pojo, URI path)
/*      */     throws DeploymentException
/*      */   {
/*  118 */     ClientEndpoint annotation = (ClientEndpoint)pojo.getClass().getAnnotation(ClientEndpoint.class);
/*  119 */     if (annotation == null)
/*      */     {
/*  121 */       throw new DeploymentException(sm.getString("wsWebSocketContainer.missingAnnotation", new Object[] {pojo
/*  122 */         .getClass().getName() }));
/*      */     }
/*      */     
/*  125 */     Endpoint ep = new PojoEndpointClient(pojo, Arrays.asList(annotation.decoders()));
/*      */     
/*      */ 
/*  128 */     Class<? extends ClientEndpointConfig.Configurator> configuratorClazz = annotation.configurator();
/*      */     
/*  130 */     ClientEndpointConfig.Configurator configurator = null;
/*  131 */     if (!ClientEndpointConfig.Configurator.class.equals(configuratorClazz)) {
/*      */       try
/*      */       {
/*  134 */         configurator = (ClientEndpointConfig.Configurator)configuratorClazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/*      */       } catch (ReflectiveOperationException e) {
/*  136 */         throw new DeploymentException(sm.getString("wsWebSocketContainer.defaultConfiguratorFail"), e);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  141 */     ClientEndpointConfig.Builder builder = ClientEndpointConfig.Builder.create();
/*      */     
/*  143 */     if (configurator != null) {
/*  144 */       builder.configurator(configurator);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  150 */     ClientEndpointConfig config = builder.decoders(Arrays.asList(annotation.decoders())).encoders(Arrays.asList(annotation.encoders())).preferredSubprotocols(Arrays.asList(annotation.subprotocols())).build();
/*  151 */     return connectToServer(ep, config, path);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Session connectToServer(Class<?> annotatedEndpointClass, URI path)
/*      */     throws DeploymentException
/*      */   {
/*      */     try
/*      */     {
/*  161 */       pojo = annotatedEndpointClass.getConstructor(new Class[0]).newInstance(new Object[0]);
/*      */     } catch (ReflectiveOperationException e) { Object pojo;
/*  163 */       throw new DeploymentException(sm.getString("wsWebSocketContainer.endpointCreateFail", new Object[] {annotatedEndpointClass
/*      */       
/*  165 */         .getName() }), e);
/*      */     }
/*      */     Object pojo;
/*  168 */     return connectToServer(pojo, path);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Session connectToServer(Class<? extends Endpoint> clazz, ClientEndpointConfig clientEndpointConfiguration, URI path)
/*      */     throws DeploymentException
/*      */   {
/*      */     try
/*      */     {
/*  179 */       endpoint = (Endpoint)clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/*      */     } catch (ReflectiveOperationException e) { Endpoint endpoint;
/*  181 */       throw new DeploymentException(sm.getString("wsWebSocketContainer.endpointCreateFail", new Object[] {clazz
/*  182 */         .getName() }), e);
/*      */     }
/*      */     
/*      */     Endpoint endpoint;
/*  186 */     return connectToServer(endpoint, clientEndpointConfiguration, path);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Session connectToServer(Endpoint endpoint, ClientEndpointConfig clientEndpointConfiguration, URI path)
/*      */     throws DeploymentException
/*      */   {
/*  194 */     return connectToServerRecursive(endpoint, clientEndpointConfiguration, path, new HashSet());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private Session connectToServerRecursive(Endpoint endpoint, ClientEndpointConfig clientEndpointConfiguration, URI path, Set<URI> redirectSet)
/*      */     throws DeploymentException
/*      */   {
/*  202 */     boolean secure = false;
/*  203 */     ByteBuffer proxyConnect = null;
/*      */     
/*      */ 
/*      */ 
/*  207 */     String scheme = path.getScheme();
/*  208 */     URI proxyPath; if ("ws".equalsIgnoreCase(scheme)) {
/*  209 */       proxyPath = URI.create("http" + path.toString().substring(2));
/*  210 */     } else if ("wss".equalsIgnoreCase(scheme)) {
/*  211 */       URI proxyPath = URI.create("https" + path.toString().substring(3));
/*  212 */       secure = true;
/*      */     } else {
/*  214 */       throw new DeploymentException(sm.getString("wsWebSocketContainer.pathWrongScheme", new Object[] { scheme }));
/*      */     }
/*      */     
/*      */     URI proxyPath;
/*      */     
/*  219 */     String host = path.getHost();
/*  220 */     if (host == null)
/*      */     {
/*  222 */       throw new DeploymentException(sm.getString("wsWebSocketContainer.pathNoHost"));
/*      */     }
/*  224 */     int port = path.getPort();
/*      */     
/*  226 */     SocketAddress sa = null;
/*      */     
/*      */ 
/*      */ 
/*  230 */     List<Proxy> proxies = ProxySelector.getDefault().select(proxyPath);
/*  231 */     Proxy selectedProxy = null;
/*  232 */     for (Proxy proxy : proxies) {
/*  233 */       if (proxy.type().equals(Proxy.Type.HTTP)) {
/*  234 */         sa = proxy.address();
/*  235 */         if ((sa instanceof InetSocketAddress)) {
/*  236 */           InetSocketAddress inet = (InetSocketAddress)sa;
/*  237 */           if (inet.isUnresolved()) {
/*  238 */             sa = new InetSocketAddress(inet.getHostName(), inet.getPort());
/*      */           }
/*      */         }
/*  241 */         selectedProxy = proxy;
/*  242 */         break;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  248 */     if (port == -1) {
/*  249 */       if ("ws".equalsIgnoreCase(scheme)) {
/*  250 */         port = 80;
/*      */       }
/*      */       else {
/*  253 */         port = 443;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  258 */     if (sa == null) {
/*  259 */       sa = new InetSocketAddress(host, port);
/*      */     } else {
/*  261 */       proxyConnect = createProxyRequest(host, port);
/*      */     }
/*      */     
/*      */ 
/*  265 */     Object reqHeaders = createRequestHeaders(host, port, clientEndpointConfiguration);
/*      */     
/*  267 */     clientEndpointConfiguration.getConfigurator().beforeRequest((Map)reqHeaders);
/*  268 */     if ((Constants.DEFAULT_ORIGIN_HEADER_VALUE != null) && 
/*  269 */       (!((Map)reqHeaders).containsKey("Origin"))) {
/*  270 */       List<String> originValues = new ArrayList(1);
/*  271 */       originValues.add(Constants.DEFAULT_ORIGIN_HEADER_VALUE);
/*  272 */       ((Map)reqHeaders).put("Origin", originValues);
/*      */     }
/*  274 */     ByteBuffer request = createRequest(path, (Map)reqHeaders);
/*      */     
/*      */     try
/*      */     {
/*  278 */       socketChannel = AsynchronousSocketChannel.open(getAsynchronousChannelGroup());
/*      */     } catch (IOException ioe) { AsynchronousSocketChannel socketChannel;
/*  280 */       throw new DeploymentException(sm.getString("wsWebSocketContainer.asynchronousSocketChannelFail"), ioe);
/*      */     }
/*      */     
/*      */     AsynchronousSocketChannel socketChannel;
/*  284 */     Map<String, Object> userProperties = clientEndpointConfiguration.getUserProperties();
/*      */     
/*      */ 
/*  287 */     long timeout = 5000L;
/*  288 */     String timeoutValue = (String)userProperties.get("org.apache.tomcat.websocket.IO_TIMEOUT_MS");
/*  289 */     if (timeoutValue != null) {
/*  290 */       timeout = Long.valueOf(timeoutValue).intValue();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  295 */     ByteBuffer response = ByteBuffer.allocate(getDefaultMaxBinaryMessageBufferSize());
/*      */     
/*  297 */     boolean success = false;
/*  298 */     List<Extension> extensionsAgreed = new ArrayList();
/*  299 */     Transformation transformation = null;
/*      */     
/*      */ 
/*  302 */     Future<Void> fConnect = socketChannel.connect(sa);
/*  303 */     AsyncChannelWrapper channel = null;
/*      */     
/*  305 */     if (proxyConnect != null) {
/*      */       try {
/*  307 */         fConnect.get(timeout, TimeUnit.MILLISECONDS);
/*      */         
/*  309 */         channel = new AsyncChannelWrapperNonSecure(socketChannel);
/*  310 */         writeRequest(channel, proxyConnect, timeout);
/*  311 */         HttpResponse httpResponse = processResponse(response, channel, timeout);
/*  312 */         if (httpResponse.getStatus() != 200) {
/*  313 */           throw new DeploymentException(sm.getString("wsWebSocketContainer.proxyConnectFail", new Object[] { selectedProxy, 
/*      */           
/*  315 */             Integer.toString(httpResponse.getStatus()) }));
/*      */         }
/*      */       }
/*      */       catch (TimeoutException|InterruptedException|ExecutionException|EOFException e) {
/*  319 */         if (channel != null) {
/*  320 */           channel.close();
/*      */         }
/*      */         
/*  323 */         throw new DeploymentException(sm.getString("wsWebSocketContainer.httpRequestFailed"), e);
/*      */       }
/*      */     }
/*      */     
/*  327 */     if (secure)
/*      */     {
/*      */ 
/*      */ 
/*  331 */       SSLEngine sslEngine = createSSLEngine(userProperties);
/*  332 */       channel = new AsyncChannelWrapperSecure(socketChannel, sslEngine);
/*  333 */     } else if (channel == null)
/*      */     {
/*      */ 
/*  336 */       channel = new AsyncChannelWrapperNonSecure(socketChannel);
/*      */     }
/*      */     try
/*      */     {
/*  340 */       fConnect.get(timeout, TimeUnit.MILLISECONDS);
/*      */       
/*  342 */       Future<Void> fHandshake = channel.handshake();
/*  343 */       fHandshake.get(timeout, TimeUnit.MILLISECONDS);
/*      */       
/*  345 */       writeRequest(channel, request, timeout);
/*      */       
/*  347 */       HttpResponse httpResponse = processResponse(response, channel, timeout);
/*      */       
/*      */ 
/*  350 */       int maxRedirects = 20;
/*      */       
/*  352 */       String maxRedirectsValue = (String)userProperties.get("org.apache.tomcat.websocket.MAX_REDIRECTIONS");
/*  353 */       if (maxRedirectsValue != null) {
/*  354 */         maxRedirects = Integer.parseInt(maxRedirectsValue);
/*      */       }
/*      */       Object auth;
/*  357 */       if (httpResponse.status != 101) {
/*  358 */         if (isRedirectStatus(httpResponse.status))
/*      */         {
/*  360 */           List<String> locationHeader = (List)httpResponse.getHandshakeResponse().getHeaders().get("Location");
/*      */           
/*      */ 
/*  363 */           if ((locationHeader == null) || (locationHeader.isEmpty()) || 
/*  364 */             (locationHeader.get(0) == null) || (((String)locationHeader.get(0)).isEmpty())) {
/*  365 */             throw new DeploymentException(sm.getString("wsWebSocketContainer.missingLocationHeader", new Object[] {
/*      */             
/*  367 */               Integer.toString(httpResponse.status) }));
/*      */           }
/*      */           
/*  370 */           URI redirectLocation = URI.create((String)locationHeader.get(0)).normalize();
/*      */           
/*  372 */           if (!redirectLocation.isAbsolute()) {
/*  373 */             redirectLocation = path.resolve(redirectLocation);
/*      */           }
/*      */           
/*  376 */           String redirectScheme = redirectLocation.getScheme().toLowerCase();
/*      */           
/*  378 */           if (redirectScheme.startsWith("http"))
/*      */           {
/*      */ 
/*      */ 
/*  382 */             redirectLocation = new URI(redirectScheme.replace("http", "ws"), redirectLocation.getUserInfo(), redirectLocation.getHost(), redirectLocation.getPort(), redirectLocation.getPath(), redirectLocation.getQuery(), redirectLocation.getFragment());
/*      */           }
/*      */           
/*  385 */           if ((!redirectSet.add(redirectLocation)) || (redirectSet.size() > maxRedirects)) {
/*  386 */             throw new DeploymentException(sm.getString("wsWebSocketContainer.redirectThreshold", new Object[] { redirectLocation, 
/*      */             
/*  388 */               Integer.toString(redirectSet.size()), 
/*  389 */               Integer.toString(maxRedirects) }));
/*      */           }
/*      */           
/*  392 */           return connectToServerRecursive(endpoint, clientEndpointConfiguration, redirectLocation, redirectSet);
/*      */         }
/*      */         
/*      */ 
/*  396 */         if (httpResponse.status == 401)
/*      */         {
/*  398 */           if (userProperties.get("Authorization") != null) {
/*  399 */             throw new DeploymentException(sm.getString("wsWebSocketContainer.failedAuthentication", new Object[] {
/*      */             
/*  401 */               Integer.valueOf(httpResponse.status) }));
/*      */           }
/*      */           
/*      */ 
/*  405 */           List<String> wwwAuthenticateHeaders = (List)httpResponse.getHandshakeResponse().getHeaders().get("WWW-Authenticate");
/*      */           
/*  407 */           if ((wwwAuthenticateHeaders == null) || (wwwAuthenticateHeaders.isEmpty()) || 
/*  408 */             (wwwAuthenticateHeaders.get(0) == null) || (((String)wwwAuthenticateHeaders.get(0)).isEmpty())) {
/*  409 */             throw new DeploymentException(sm.getString("wsWebSocketContainer.missingWWWAuthenticateHeader", new Object[] {
/*      */             
/*  411 */               Integer.toString(httpResponse.status) }));
/*      */           }
/*      */           
/*  414 */           String authScheme = ((String)wwwAuthenticateHeaders.get(0)).split("\\s+", 2)[0];
/*      */           
/*  416 */           String requestUri = new String(request.array(), StandardCharsets.ISO_8859_1).split("\\s", 3)[1];
/*      */           
/*  418 */           auth = AuthenticatorFactory.getAuthenticator(authScheme);
/*      */           
/*  420 */           if (auth == null)
/*      */           {
/*  422 */             throw new DeploymentException(sm.getString("wsWebSocketContainer.unsupportedAuthScheme", new Object[] {
/*  423 */               Integer.valueOf(httpResponse.status), authScheme }));
/*      */           }
/*      */           
/*  426 */           userProperties.put("Authorization", ((Authenticator)auth).getAuthorization(requestUri, 
/*  427 */             (String)wwwAuthenticateHeaders.get(0), userProperties));
/*      */           
/*  429 */           return connectToServerRecursive(endpoint, clientEndpointConfiguration, path, redirectSet);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  434 */         throw new DeploymentException(sm.getString("wsWebSocketContainer.invalidStatus", new Object[] {
/*  435 */           Integer.toString(httpResponse.status) }));
/*      */       }
/*      */       
/*  438 */       HandshakeResponse handshakeResponse = httpResponse.getHandshakeResponse();
/*  439 */       clientEndpointConfiguration.getConfigurator().afterResponse(handshakeResponse);
/*      */       
/*      */ 
/*  442 */       List<String> protocolHeaders = (List)handshakeResponse.getHeaders().get("Sec-WebSocket-Protocol");
/*      */       String subProtocol;
/*  444 */       if ((protocolHeaders == null) || (protocolHeaders.size() == 0)) {
/*  445 */         subProtocol = null; } else { String subProtocol;
/*  446 */         if (protocolHeaders.size() == 1) {
/*  447 */           subProtocol = (String)protocolHeaders.get(0);
/*      */         }
/*      */         else {
/*  450 */           throw new DeploymentException(sm.getString("wsWebSocketContainer.invalidSubProtocol"));
/*      */         }
/*      */       }
/*      */       
/*      */       String subProtocol;
/*      */       
/*  456 */       List<String> extHeaders = (List)handshakeResponse.getHeaders().get("Sec-WebSocket-Extensions");
/*      */       
/*  458 */       if (extHeaders != null) {
/*  459 */         for (auth = extHeaders.iterator(); ((Iterator)auth).hasNext();) { extHeader = (String)((Iterator)auth).next();
/*  460 */           Util.parseExtensionHeader(extensionsAgreed, (String)extHeader);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  465 */       TransformationFactory factory = TransformationFactory.getInstance();
/*  466 */       for (Object extHeader = extensionsAgreed.iterator(); ((Iterator)extHeader).hasNext();) { Extension extension = (Extension)((Iterator)extHeader).next();
/*  467 */         List<List<Extension.Parameter>> wrapper = new ArrayList(1);
/*  468 */         wrapper.add(extension.getParameters());
/*  469 */         Transformation t = factory.create(extension.getName(), wrapper, false);
/*  470 */         if (t == null) {
/*  471 */           throw new DeploymentException(sm.getString("wsWebSocketContainer.invalidExtensionParameters"));
/*      */         }
/*      */         
/*  474 */         if (transformation == null) {
/*  475 */           transformation = t;
/*      */         } else {
/*  477 */           transformation.setNext(t);
/*      */         }
/*      */       }
/*      */       
/*  481 */       success = true;
/*      */     }
/*      */     catch (ExecutionException|InterruptedException|SSLException|EOFException|TimeoutException|URISyntaxException|AuthenticationException e)
/*      */     {
/*  485 */       throw new DeploymentException(sm.getString("wsWebSocketContainer.httpRequestFailed"), e);
/*      */     } finally {
/*  487 */       if (!success) {
/*  488 */         channel.close();
/*      */       }
/*      */     }
/*      */     
/*      */     String subProtocol;
/*  493 */     WsRemoteEndpointImplClient wsRemoteEndpointClient = new WsRemoteEndpointImplClient(channel);
/*      */     
/*      */ 
/*      */ 
/*  497 */     WsSession wsSession = new WsSession(endpoint, wsRemoteEndpointClient, this, null, null, null, null, null, extensionsAgreed, subProtocol, Collections.emptyMap(), secure, clientEndpointConfiguration);
/*      */     
/*      */ 
/*  500 */     WsFrameClient wsFrameClient = new WsFrameClient(response, channel, wsSession, transformation);
/*      */     
/*      */ 
/*      */ 
/*  504 */     wsRemoteEndpointClient.setTransformation(wsFrameClient.getTransformation());
/*      */     
/*  506 */     endpoint.onOpen(wsSession, clientEndpointConfiguration);
/*  507 */     registerSession(endpoint, wsSession);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  519 */     wsFrameClient.startInputProcessing();
/*      */     
/*  521 */     return wsSession;
/*      */   }
/*      */   
/*      */   private static void writeRequest(AsyncChannelWrapper channel, ByteBuffer request, long timeout)
/*      */     throws TimeoutException, InterruptedException, ExecutionException
/*      */   {
/*  527 */     int toWrite = request.limit();
/*      */     
/*  529 */     Future<Integer> fWrite = channel.write(request);
/*  530 */     Integer thisWrite = (Integer)fWrite.get(timeout, TimeUnit.MILLISECONDS);
/*  531 */     toWrite -= thisWrite.intValue();
/*      */     
/*  533 */     while (toWrite > 0) {
/*  534 */       fWrite = channel.write(request);
/*  535 */       thisWrite = (Integer)fWrite.get(timeout, TimeUnit.MILLISECONDS);
/*  536 */       toWrite -= thisWrite.intValue();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static boolean isRedirectStatus(int httpResponseCode)
/*      */   {
/*  543 */     boolean isRedirect = false;
/*      */     
/*  545 */     switch (httpResponseCode) {
/*      */     case 300: 
/*      */     case 301: 
/*      */     case 302: 
/*      */     case 303: 
/*      */     case 305: 
/*      */     case 307: 
/*  552 */       isRedirect = true;
/*  553 */       break;
/*      */     }
/*      */     
/*      */     
/*      */ 
/*  558 */     return isRedirect;
/*      */   }
/*      */   
/*      */   private static ByteBuffer createProxyRequest(String host, int port)
/*      */   {
/*  563 */     StringBuilder request = new StringBuilder();
/*  564 */     request.append("CONNECT ");
/*  565 */     request.append(host);
/*  566 */     request.append(':');
/*  567 */     request.append(port);
/*      */     
/*  569 */     request.append(" HTTP/1.1\r\nProxy-Connection: keep-alive\r\nConnection: keepalive\r\nHost: ");
/*  570 */     request.append(host);
/*  571 */     request.append(':');
/*  572 */     request.append(port);
/*      */     
/*  574 */     request.append("\r\n\r\n");
/*      */     
/*  576 */     byte[] bytes = request.toString().getBytes(StandardCharsets.ISO_8859_1);
/*  577 */     return ByteBuffer.wrap(bytes);
/*      */   }
/*      */   
/*      */   protected void registerSession(Endpoint endpoint, WsSession wsSession)
/*      */   {
/*  582 */     if (!wsSession.isOpen())
/*      */     {
/*  584 */       return;
/*      */     }
/*  586 */     synchronized (this.endPointSessionMapLock) {
/*  587 */       if (this.endpointSessionMap.size() == 0) {
/*  588 */         BackgroundProcessManager.getInstance().register(this);
/*      */       }
/*  590 */       Set<WsSession> wsSessions = (Set)this.endpointSessionMap.get(endpoint);
/*  591 */       if (wsSessions == null) {
/*  592 */         wsSessions = new HashSet();
/*  593 */         this.endpointSessionMap.put(endpoint, wsSessions);
/*      */       }
/*  595 */       wsSessions.add(wsSession);
/*      */     }
/*  597 */     this.sessions.put(wsSession, wsSession);
/*      */   }
/*      */   
/*      */ 
/*      */   protected void unregisterSession(Endpoint endpoint, WsSession wsSession)
/*      */   {
/*  603 */     synchronized (this.endPointSessionMapLock) {
/*  604 */       Set<WsSession> wsSessions = (Set)this.endpointSessionMap.get(endpoint);
/*  605 */       if (wsSessions != null) {
/*  606 */         wsSessions.remove(wsSession);
/*  607 */         if (wsSessions.size() == 0) {
/*  608 */           this.endpointSessionMap.remove(endpoint);
/*      */         }
/*      */       }
/*  611 */       if (this.endpointSessionMap.size() == 0) {
/*  612 */         BackgroundProcessManager.getInstance().unregister(this);
/*      */       }
/*      */     }
/*  615 */     this.sessions.remove(wsSession);
/*      */   }
/*      */   
/*      */   Set<Session> getOpenSessions(Endpoint endpoint)
/*      */   {
/*  620 */     HashSet<Session> result = new HashSet();
/*  621 */     synchronized (this.endPointSessionMapLock) {
/*  622 */       Set<WsSession> sessions = (Set)this.endpointSessionMap.get(endpoint);
/*  623 */       if (sessions != null) {
/*  624 */         result.addAll(sessions);
/*      */       }
/*      */     }
/*  627 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */   private static Map<String, List<String>> createRequestHeaders(String host, int port, ClientEndpointConfig clientEndpointConfiguration)
/*      */   {
/*  633 */     Map<String, List<String>> headers = new HashMap();
/*  634 */     List<Extension> extensions = clientEndpointConfiguration.getExtensions();
/*  635 */     List<String> subProtocols = clientEndpointConfiguration.getPreferredSubprotocols();
/*  636 */     Map<String, Object> userProperties = clientEndpointConfiguration.getUserProperties();
/*      */     
/*  638 */     if (userProperties.get("Authorization") != null) {
/*  639 */       List<String> authValues = new ArrayList(1);
/*  640 */       authValues.add((String)userProperties.get("Authorization"));
/*  641 */       headers.put("Authorization", authValues);
/*      */     }
/*      */     
/*      */ 
/*  645 */     List<String> hostValues = new ArrayList(1);
/*  646 */     if (port == -1) {
/*  647 */       hostValues.add(host);
/*      */     } else {
/*  649 */       hostValues.add(host + ':' + port);
/*      */     }
/*      */     
/*  652 */     headers.put("Host", hostValues);
/*      */     
/*      */ 
/*  655 */     List<String> upgradeValues = new ArrayList(1);
/*  656 */     upgradeValues.add("websocket");
/*  657 */     headers.put("Upgrade", upgradeValues);
/*      */     
/*      */ 
/*  660 */     List<String> connectionValues = new ArrayList(1);
/*  661 */     connectionValues.add("upgrade");
/*  662 */     headers.put("Connection", connectionValues);
/*      */     
/*      */ 
/*  665 */     List<String> wsVersionValues = new ArrayList(1);
/*  666 */     wsVersionValues.add("13");
/*  667 */     headers.put("Sec-WebSocket-Version", wsVersionValues);
/*      */     
/*      */ 
/*  670 */     List<String> wsKeyValues = new ArrayList(1);
/*  671 */     wsKeyValues.add(generateWsKeyValue());
/*  672 */     headers.put("Sec-WebSocket-Key", wsKeyValues);
/*      */     
/*      */ 
/*  675 */     if ((subProtocols != null) && (subProtocols.size() > 0)) {
/*  676 */       headers.put("Sec-WebSocket-Protocol", subProtocols);
/*      */     }
/*      */     
/*      */ 
/*  680 */     if ((extensions != null) && (extensions.size() > 0)) {
/*  681 */       headers.put("Sec-WebSocket-Extensions", 
/*  682 */         generateExtensionHeaders(extensions));
/*      */     }
/*      */     
/*  685 */     return headers;
/*      */   }
/*      */   
/*      */   private static List<String> generateExtensionHeaders(List<Extension> extensions)
/*      */   {
/*  690 */     List<String> result = new ArrayList(extensions.size());
/*  691 */     for (Extension extension : extensions) {
/*  692 */       StringBuilder header = new StringBuilder();
/*  693 */       header.append(extension.getName());
/*  694 */       for (Extension.Parameter param : extension.getParameters()) {
/*  695 */         header.append(';');
/*  696 */         header.append(param.getName());
/*  697 */         String value = param.getValue();
/*  698 */         if ((value != null) && (value.length() > 0)) {
/*  699 */           header.append('=');
/*  700 */           header.append(value);
/*      */         }
/*      */       }
/*  703 */       result.add(header.toString());
/*      */     }
/*  705 */     return result;
/*      */   }
/*      */   
/*      */   private static String generateWsKeyValue()
/*      */   {
/*  710 */     byte[] keyBytes = new byte[16];
/*  711 */     RANDOM.nextBytes(keyBytes);
/*  712 */     return Base64.encodeBase64String(keyBytes);
/*      */   }
/*      */   
/*      */   private static ByteBuffer createRequest(URI uri, Map<String, List<String>> reqHeaders)
/*      */   {
/*  717 */     ByteBuffer result = ByteBuffer.allocate(4096);
/*      */     
/*      */ 
/*  720 */     result.put(GET_BYTES);
/*  721 */     if ((null == uri.getPath()) || ("".equals(uri.getPath()))) {
/*  722 */       result.put(ROOT_URI_BYTES);
/*      */     } else {
/*  724 */       result.put(uri.getRawPath().getBytes(StandardCharsets.ISO_8859_1));
/*      */     }
/*  726 */     String query = uri.getRawQuery();
/*  727 */     if (query != null) {
/*  728 */       result.put((byte)63);
/*  729 */       result.put(query.getBytes(StandardCharsets.ISO_8859_1));
/*      */     }
/*  731 */     result.put(HTTP_VERSION_BYTES);
/*      */     
/*      */ 
/*  734 */     for (Map.Entry<String, List<String>> entry : reqHeaders.entrySet()) {
/*  735 */       addHeader(result, (String)entry.getKey(), (List)entry.getValue());
/*      */     }
/*      */     
/*      */ 
/*  739 */     result.put(CRLF);
/*      */     
/*  741 */     result.flip();
/*      */     
/*  743 */     return result;
/*      */   }
/*      */   
/*      */   private static void addHeader(ByteBuffer result, String key, List<String> values)
/*      */   {
/*  748 */     if (values.isEmpty()) {
/*  749 */       return;
/*      */     }
/*      */     
/*  752 */     result.put(key.getBytes(StandardCharsets.ISO_8859_1));
/*  753 */     result.put(": ".getBytes(StandardCharsets.ISO_8859_1));
/*  754 */     result.put(StringUtils.join(values).getBytes(StandardCharsets.ISO_8859_1));
/*  755 */     result.put(CRLF);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private HttpResponse processResponse(ByteBuffer response, AsyncChannelWrapper channel, long timeout)
/*      */     throws InterruptedException, ExecutionException, DeploymentException, EOFException, TimeoutException
/*      */   {
/*  771 */     Map<String, List<String>> headers = new CaseInsensitiveKeyMap();
/*      */     
/*  773 */     int status = 0;
/*  774 */     boolean readStatus = false;
/*  775 */     boolean readHeaders = false;
/*  776 */     String line = null;
/*  777 */     for (; !readHeaders; 
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  803 */         goto 78)
/*      */     {
/*  780 */       response.clear();
/*      */       
/*  782 */       Future<Integer> read = channel.read(response);
/*  783 */       Integer bytesRead = (Integer)read.get(timeout, TimeUnit.MILLISECONDS);
/*  784 */       if (bytesRead.intValue() == -1) {
/*  785 */         throw new EOFException();
/*      */       }
/*  787 */       response.flip();
/*  788 */       while ((response.hasRemaining()) && (!readHeaders)) {
/*  789 */         if (line == null) {
/*  790 */           line = readLine(response);
/*      */         } else {
/*  792 */           line = line + readLine(response);
/*      */         }
/*  794 */         if ("\r\n".equals(line)) {
/*  795 */           readHeaders = true;
/*  796 */         } else if (line.endsWith("\r\n")) {
/*  797 */           if (readStatus) {
/*  798 */             parseHeaders(line, headers);
/*      */           } else {
/*  800 */             status = parseStatus(line);
/*  801 */             readStatus = true;
/*      */           }
/*  803 */           line = null;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  808 */     return new HttpResponse(status, new WsHandshakeResponse(headers));
/*      */   }
/*      */   
/*      */ 
/*      */   private int parseStatus(String line)
/*      */     throws DeploymentException
/*      */   {
/*  815 */     String[] parts = line.trim().split(" ");
/*      */     
/*  817 */     if ((parts.length < 2) || ((!"HTTP/1.0".equals(parts[0])) && (!"HTTP/1.1".equals(parts[0])))) {
/*  818 */       throw new DeploymentException(sm.getString("wsWebSocketContainer.invalidStatus", new Object[] { line }));
/*      */     }
/*      */     try
/*      */     {
/*  822 */       return Integer.parseInt(parts[1]);
/*      */     } catch (NumberFormatException nfe) {
/*  824 */       throw new DeploymentException(sm.getString("wsWebSocketContainer.invalidStatus", new Object[] { line }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void parseHeaders(String line, Map<String, List<String>> headers)
/*      */   {
/*  833 */     int index = line.indexOf(':');
/*  834 */     if (index == -1) {
/*  835 */       this.log.warn(sm.getString("wsWebSocketContainer.invalidHeader", new Object[] { line }));
/*  836 */       return;
/*      */     }
/*      */     
/*  839 */     String headerName = line.substring(0, index).trim().toLowerCase(Locale.ENGLISH);
/*      */     
/*      */ 
/*  842 */     String headerValue = line.substring(index + 1).trim();
/*      */     
/*  844 */     List<String> values = (List)headers.get(headerName);
/*  845 */     if (values == null) {
/*  846 */       values = new ArrayList(1);
/*  847 */       headers.put(headerName, values);
/*      */     }
/*  849 */     values.add(headerValue);
/*      */   }
/*      */   
/*      */   private String readLine(ByteBuffer response)
/*      */   {
/*  854 */     StringBuilder sb = new StringBuilder();
/*      */     
/*  856 */     char c = '\000';
/*  857 */     while (response.hasRemaining()) {
/*  858 */       c = (char)response.get();
/*  859 */       sb.append(c);
/*  860 */       if (c == '\n') {
/*      */         break;
/*      */       }
/*      */     }
/*      */     
/*  865 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private SSLEngine createSSLEngine(Map<String, Object> userProperties)
/*      */     throws DeploymentException
/*      */   {
/*      */     try
/*      */     {
/*  875 */       SSLContext sslContext = (SSLContext)userProperties.get("org.apache.tomcat.websocket.SSL_CONTEXT");
/*      */       
/*  877 */       if (sslContext == null)
/*      */       {
/*  879 */         sslContext = SSLContext.getInstance("TLS");
/*      */         
/*      */ 
/*      */ 
/*  883 */         String sslTrustStoreValue = (String)userProperties.get("org.apache.tomcat.websocket.SSL_TRUSTSTORE");
/*  884 */         if (sslTrustStoreValue != null) {
/*  885 */           String sslTrustStorePwdValue = (String)userProperties.get("org.apache.tomcat.websocket.SSL_TRUSTSTORE_PWD");
/*      */           
/*  887 */           if (sslTrustStorePwdValue == null) {
/*  888 */             sslTrustStorePwdValue = "changeit";
/*      */           }
/*      */           
/*  891 */           File keyStoreFile = new File(sslTrustStoreValue);
/*  892 */           KeyStore ks = KeyStore.getInstance("JKS");
/*  893 */           InputStream is = new FileInputStream(keyStoreFile);Throwable localThrowable3 = null;
/*  894 */           try { ks.load(is, sslTrustStorePwdValue.toCharArray());
/*      */           }
/*      */           catch (Throwable localThrowable1)
/*      */           {
/*  893 */             localThrowable3 = localThrowable1;throw localThrowable1;
/*      */           } finally {
/*  895 */             if (is != null) if (localThrowable3 != null) try { is.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else is.close();
/*      */           }
/*  897 */           TrustManagerFactory tmf = TrustManagerFactory.getInstance(
/*  898 */             TrustManagerFactory.getDefaultAlgorithm());
/*  899 */           tmf.init(ks);
/*      */           
/*  901 */           sslContext.init(null, tmf.getTrustManagers(), null);
/*      */         } else {
/*  903 */           sslContext.init(null, null, null);
/*      */         }
/*      */       }
/*      */       
/*  907 */       SSLEngine engine = sslContext.createSSLEngine();
/*      */       
/*      */ 
/*  910 */       String sslProtocolsValue = (String)userProperties.get("org.apache.tomcat.websocket.SSL_PROTOCOLS");
/*  911 */       if (sslProtocolsValue != null) {
/*  912 */         engine.setEnabledProtocols(sslProtocolsValue.split(","));
/*      */       }
/*      */       
/*  915 */       engine.setUseClientMode(true);
/*      */       
/*  917 */       return engine;
/*      */     } catch (Exception e) {
/*  919 */       throw new DeploymentException(sm.getString("wsWebSocketContainer.sslEngineFail"), e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public long getDefaultMaxSessionIdleTimeout()
/*      */   {
/*  927 */     return this.defaultMaxSessionIdleTimeout;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setDefaultMaxSessionIdleTimeout(long timeout)
/*      */   {
/*  933 */     this.defaultMaxSessionIdleTimeout = timeout;
/*      */   }
/*      */   
/*      */ 
/*      */   public int getDefaultMaxBinaryMessageBufferSize()
/*      */   {
/*  939 */     return this.maxBinaryMessageBufferSize;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setDefaultMaxBinaryMessageBufferSize(int max)
/*      */   {
/*  945 */     this.maxBinaryMessageBufferSize = max;
/*      */   }
/*      */   
/*      */ 
/*      */   public int getDefaultMaxTextMessageBufferSize()
/*      */   {
/*  951 */     return this.maxTextMessageBufferSize;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setDefaultMaxTextMessageBufferSize(int max)
/*      */   {
/*  957 */     this.maxTextMessageBufferSize = max;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<Extension> getInstalledExtensions()
/*      */   {
/*  968 */     return Collections.emptySet();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getDefaultAsyncSendTimeout()
/*      */   {
/*  979 */     return this.defaultAsyncTimeout;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAsyncSendTimeout(long timeout)
/*      */   {
/*  990 */     this.defaultAsyncTimeout = timeout;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void destroy()
/*      */   {
/* 1001 */     CloseReason cr = new CloseReason(CloseReason.CloseCodes.GOING_AWAY, sm.getString("wsWebSocketContainer.shutdown"));
/*      */     
/* 1003 */     for (WsSession session : this.sessions.keySet()) {
/*      */       try {
/* 1005 */         session.close(cr);
/*      */       } catch (IOException ioe) {
/* 1007 */         this.log.debug(sm.getString("wsWebSocketContainer.sessionCloseFail", new Object[] {session
/* 1008 */           .getId() }), ioe);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1014 */     if (this.asynchronousChannelGroup != null) {
/* 1015 */       synchronized (this.asynchronousChannelGroupLock) {
/* 1016 */         if (this.asynchronousChannelGroup != null) {
/* 1017 */           AsyncChannelGroupUtil.unregister();
/* 1018 */           this.asynchronousChannelGroup = null;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private AsynchronousChannelGroup getAsynchronousChannelGroup()
/*      */   {
/* 1028 */     AsynchronousChannelGroup result = this.asynchronousChannelGroup;
/* 1029 */     if (result == null) {
/* 1030 */       synchronized (this.asynchronousChannelGroupLock) {
/* 1031 */         if (this.asynchronousChannelGroup == null) {
/* 1032 */           this.asynchronousChannelGroup = AsyncChannelGroupUtil.register();
/*      */         }
/* 1034 */         result = this.asynchronousChannelGroup;
/*      */       }
/*      */     }
/* 1037 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void backgroundProcess()
/*      */   {
/* 1046 */     this.backgroundProcessCount += 1;
/* 1047 */     if (this.backgroundProcessCount >= this.processPeriod) {
/* 1048 */       this.backgroundProcessCount = 0;
/*      */       
/* 1050 */       for (WsSession wsSession : this.sessions.keySet()) {
/* 1051 */         wsSession.checkExpiration();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setProcessPeriod(int period)
/*      */   {
/* 1060 */     this.processPeriod = period;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getProcessPeriod()
/*      */   {
/* 1072 */     return this.processPeriod;
/*      */   }
/*      */   
/*      */   private static class HttpResponse
/*      */   {
/*      */     private final int status;
/*      */     private final HandshakeResponse handshakeResponse;
/*      */     
/*      */     public HttpResponse(int status, HandshakeResponse handshakeResponse) {
/* 1081 */       this.status = status;
/* 1082 */       this.handshakeResponse = handshakeResponse;
/*      */     }
/*      */     
/*      */     public int getStatus()
/*      */     {
/* 1087 */       return this.status;
/*      */     }
/*      */     
/*      */     public HandshakeResponse getHandshakeResponse()
/*      */     {
/* 1092 */       return this.handshakeResponse;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\WsWebSocketContainer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */