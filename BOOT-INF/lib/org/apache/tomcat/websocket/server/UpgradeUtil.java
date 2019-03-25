/*     */ package org.apache.tomcat.websocket.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.websocket.Endpoint;
/*     */ import javax.websocket.Extension;
/*     */ import javax.websocket.Extension.Parameter;
/*     */ import javax.websocket.server.ServerEndpointConfig;
/*     */ import javax.websocket.server.ServerEndpointConfig.Configurator;
/*     */ import org.apache.tomcat.util.codec.binary.Base64;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ import org.apache.tomcat.util.security.ConcurrentMessageDigest;
/*     */ import org.apache.tomcat.websocket.Constants;
/*     */ import org.apache.tomcat.websocket.Transformation;
/*     */ import org.apache.tomcat.websocket.TransformationFactory;
/*     */ import org.apache.tomcat.websocket.Util;
/*     */ import org.apache.tomcat.websocket.WsHandshakeResponse;
/*     */ import org.apache.tomcat.websocket.pojo.PojoEndpointServer;
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
/*     */ public class UpgradeUtil
/*     */ {
/*  52 */   private static final StringManager sm = StringManager.getManager(UpgradeUtil.class.getPackage().getName());
/*  53 */   private static final byte[] WS_ACCEPT = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11"
/*  54 */     .getBytes(StandardCharsets.ISO_8859_1);
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
/*     */   public static boolean isWebSocketUpgradeRequest(ServletRequest request, ServletResponse response)
/*     */   {
/*  77 */     if (((request instanceof HttpServletRequest)) && ((response instanceof HttpServletResponse)))
/*     */     {
/*  79 */       if (!headerContainsToken((HttpServletRequest)request, "Upgrade", "websocket")) {}
/*     */     }
/*  77 */     return 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  82 */       "GET".equals(((HttpServletRequest)request).getMethod());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void doUpgrade(WsServerContainer sc, HttpServletRequest req, HttpServletResponse resp, ServerEndpointConfig sec, Map<String, String> pathParams)
/*     */     throws ServletException, IOException
/*     */   {
/*  94 */     String subProtocol = null;
/*  95 */     if (!headerContainsToken(req, "Connection", "upgrade"))
/*     */     {
/*  97 */       resp.sendError(400);
/*  98 */       return;
/*     */     }
/* 100 */     if (!headerContainsToken(req, "Sec-WebSocket-Version", "13"))
/*     */     {
/* 102 */       resp.setStatus(426);
/* 103 */       resp.setHeader("Sec-WebSocket-Version", "13");
/*     */       
/* 105 */       return;
/*     */     }
/* 107 */     String key = req.getHeader("Sec-WebSocket-Key");
/* 108 */     if (key == null) {
/* 109 */       resp.sendError(400);
/* 110 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 115 */     String origin = req.getHeader("Origin");
/* 116 */     if (!sec.getConfigurator().checkOrigin(origin)) {
/* 117 */       resp.sendError(403);
/* 118 */       return;
/*     */     }
/*     */     
/* 121 */     List<String> subProtocols = getTokensFromHeader(req, "Sec-WebSocket-Protocol");
/*     */     
/* 123 */     subProtocol = sec.getConfigurator().getNegotiatedSubprotocol(sec
/* 124 */       .getSubprotocols(), subProtocols);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 129 */     List<Extension> extensionsRequested = new ArrayList();
/* 130 */     Enumeration<String> extHeaders = req.getHeaders("Sec-WebSocket-Extensions");
/* 131 */     while (extHeaders.hasMoreElements()) {
/* 132 */       Util.parseExtensionHeader(extensionsRequested, (String)extHeaders.nextElement());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 137 */     List<Extension> installedExtensions = null;
/* 138 */     if (sec.getExtensions().size() == 0) {
/* 139 */       installedExtensions = Constants.INSTALLED_EXTENSIONS;
/*     */     } else {
/* 141 */       installedExtensions = new ArrayList();
/* 142 */       installedExtensions.addAll(sec.getExtensions());
/* 143 */       installedExtensions.addAll(Constants.INSTALLED_EXTENSIONS);
/*     */     }
/* 145 */     List<Extension> negotiatedExtensionsPhase1 = sec.getConfigurator().getNegotiatedExtensions(installedExtensions, extensionsRequested);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 152 */     List<Transformation> transformations = createTransformations(negotiatedExtensionsPhase1);
/*     */     List<Extension> negotiatedExtensionsPhase2;
/*     */     List<Extension> negotiatedExtensionsPhase2;
/* 155 */     if (transformations.isEmpty()) {
/* 156 */       negotiatedExtensionsPhase2 = Collections.emptyList();
/*     */     } else {
/* 158 */       negotiatedExtensionsPhase2 = new ArrayList(transformations.size());
/* 159 */       for (Transformation t : transformations) {
/* 160 */         negotiatedExtensionsPhase2.add(t.getExtensionResponse());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 165 */     Transformation transformation = null;
/* 166 */     StringBuilder responseHeaderExtensions = new StringBuilder();
/* 167 */     boolean first = true;
/* 168 */     for (Transformation t : transformations) {
/* 169 */       if (first) {
/* 170 */         first = false;
/*     */       } else {
/* 172 */         responseHeaderExtensions.append(',');
/*     */       }
/* 174 */       append(responseHeaderExtensions, t.getExtensionResponse());
/* 175 */       if (transformation == null) {
/* 176 */         transformation = t;
/*     */       } else {
/* 178 */         transformation.setNext(t);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 183 */     if ((transformation != null) && (!transformation.validateRsvBits(0))) {
/* 184 */       throw new ServletException(sm.getString("upgradeUtil.incompatibleRsv"));
/*     */     }
/*     */     
/*     */ 
/* 188 */     resp.setHeader("Upgrade", "websocket");
/*     */     
/* 190 */     resp.setHeader("Connection", "upgrade");
/*     */     
/* 192 */     resp.setHeader("Sec-WebSocket-Accept", 
/* 193 */       getWebSocketAccept(key));
/* 194 */     if ((subProtocol != null) && (subProtocol.length() > 0))
/*     */     {
/* 196 */       resp.setHeader("Sec-WebSocket-Protocol", subProtocol);
/*     */     }
/* 198 */     if (!transformations.isEmpty()) {
/* 199 */       resp.setHeader("Sec-WebSocket-Extensions", responseHeaderExtensions.toString());
/*     */     }
/*     */     
/* 202 */     WsHandshakeRequest wsRequest = new WsHandshakeRequest(req, pathParams);
/* 203 */     WsHandshakeResponse wsResponse = new WsHandshakeResponse();
/* 204 */     WsPerSessionServerEndpointConfig perSessionServerEndpointConfig = new WsPerSessionServerEndpointConfig(sec);
/*     */     
/* 206 */     sec.getConfigurator().modifyHandshake(perSessionServerEndpointConfig, wsRequest, wsResponse);
/*     */     
/* 208 */     wsRequest.finished();
/*     */     
/*     */ 
/*     */ 
/* 212 */     for (Iterator localIterator3 = wsResponse.getHeaders().entrySet().iterator(); localIterator3.hasNext();) { entry = (Map.Entry)localIterator3.next();
/* 213 */       for (String headerValue : (List)entry.getValue()) {
/* 214 */         resp.addHeader((String)entry.getKey(), headerValue);
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/*     */       Map.Entry<String, List<String>> entry;
/* 220 */       Class<?> clazz = sec.getEndpointClass();
/* 221 */       Endpoint ep; if (Endpoint.class.isAssignableFrom(clazz)) {
/* 222 */         ep = (Endpoint)sec.getConfigurator().getEndpointInstance(clazz);
/*     */       }
/*     */       else {
/* 225 */         Object ep = new PojoEndpointServer();
/*     */         
/* 227 */         perSessionServerEndpointConfig.getUserProperties().put("org.apache.tomcat.websocket.pojo.PojoEndpoint.pathParams", pathParams);
/*     */       }
/*     */     }
/*     */     catch (InstantiationException e) {
/* 231 */       throw new ServletException(e);
/*     */     }
/*     */     
/*     */     Endpoint ep;
/* 235 */     WsHttpUpgradeHandler wsHandler = (WsHttpUpgradeHandler)req.upgrade(WsHttpUpgradeHandler.class);
/* 236 */     wsHandler.preInit(ep, perSessionServerEndpointConfig, sc, wsRequest, negotiatedExtensionsPhase2, subProtocol, transformation, pathParams, req
/*     */     
/* 238 */       .isSecure());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static List<Transformation> createTransformations(List<Extension> negotiatedExtensions)
/*     */   {
/* 246 */     TransformationFactory factory = TransformationFactory.getInstance();
/*     */     
/* 248 */     LinkedHashMap<String, List<List<Extension.Parameter>>> extensionPreferences = new LinkedHashMap();
/*     */     
/*     */ 
/*     */ 
/* 252 */     List<Transformation> result = new ArrayList(negotiatedExtensions.size());
/*     */     
/* 254 */     for (Extension extension : negotiatedExtensions)
/*     */     {
/* 256 */       List<List<Extension.Parameter>> preferences = (List)extensionPreferences.get(extension.getName());
/*     */       
/* 258 */       if (preferences == null) {
/* 259 */         preferences = new ArrayList();
/* 260 */         extensionPreferences.put(extension.getName(), preferences);
/*     */       }
/*     */       
/* 263 */       preferences.add(extension.getParameters());
/*     */     }
/*     */     
/*     */ 
/* 267 */     for (Map.Entry<String, List<List<Extension.Parameter>>> entry : extensionPreferences.entrySet()) {
/* 268 */       Transformation transformation = factory.create((String)entry.getKey(), (List)entry.getValue(), true);
/* 269 */       if (transformation != null) {
/* 270 */         result.add(transformation);
/*     */       }
/*     */     }
/* 273 */     return result;
/*     */   }
/*     */   
/*     */   private static void append(StringBuilder sb, Extension extension)
/*     */   {
/* 278 */     if ((extension == null) || (extension.getName() == null) || (extension.getName().length() == 0)) {
/* 279 */       return;
/*     */     }
/*     */     
/* 282 */     sb.append(extension.getName());
/*     */     
/* 284 */     for (Extension.Parameter p : extension.getParameters()) {
/* 285 */       sb.append(';');
/* 286 */       sb.append(p.getName());
/* 287 */       if (p.getValue() != null) {
/* 288 */         sb.append('=');
/* 289 */         sb.append(p.getValue());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean headerContainsToken(HttpServletRequest req, String headerName, String target)
/*     */   {
/* 301 */     Enumeration<String> headers = req.getHeaders(headerName);
/* 302 */     while (headers.hasMoreElements()) {
/* 303 */       String header = (String)headers.nextElement();
/* 304 */       String[] tokens = header.split(",");
/* 305 */       for (String token : tokens) {
/* 306 */         if (target.equalsIgnoreCase(token.trim())) {
/* 307 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 311 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static List<String> getTokensFromHeader(HttpServletRequest req, String headerName)
/*     */   {
/* 321 */     List<String> result = new ArrayList();
/* 322 */     Enumeration<String> headers = req.getHeaders(headerName);
/* 323 */     while (headers.hasMoreElements()) {
/* 324 */       String header = (String)headers.nextElement();
/* 325 */       String[] tokens = header.split(",");
/* 326 */       for (String token : tokens) {
/* 327 */         result.add(token.trim());
/*     */       }
/*     */     }
/* 330 */     return result;
/*     */   }
/*     */   
/*     */   private static String getWebSocketAccept(String key)
/*     */   {
/* 335 */     byte[] digest = ConcurrentMessageDigest.digestSHA1(new byte[][] {key
/* 336 */       .getBytes(StandardCharsets.ISO_8859_1), WS_ACCEPT });
/* 337 */     return Base64.encodeBase64String(digest);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\server\UpgradeUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */