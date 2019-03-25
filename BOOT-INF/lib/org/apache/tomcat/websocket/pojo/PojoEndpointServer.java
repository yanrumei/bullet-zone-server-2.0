/*    */ package org.apache.tomcat.websocket.pojo;
/*    */ 
/*    */ import java.util.Map;
/*    */ import javax.websocket.EndpointConfig;
/*    */ import javax.websocket.Session;
/*    */ import javax.websocket.server.ServerEndpointConfig;
/*    */ import javax.websocket.server.ServerEndpointConfig.Configurator;
/*    */ import org.apache.tomcat.util.res.StringManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PojoEndpointServer
/*    */   extends PojoEndpointBase
/*    */ {
/* 35 */   private static final StringManager sm = StringManager.getManager(PojoEndpointServer.class);
/*    */   
/*    */ 
/*    */   public void onOpen(Session session, EndpointConfig endpointConfig)
/*    */   {
/* 40 */     ServerEndpointConfig sec = (ServerEndpointConfig)endpointConfig;
/*    */     
/*    */     try
/*    */     {
/* 44 */       pojo = sec.getConfigurator().getEndpointInstance(sec
/* 45 */         .getEndpointClass());
/*    */     } catch (InstantiationException e) { Object pojo;
/* 47 */       throw new IllegalArgumentException(sm.getString("pojoEndpointServer.getPojoInstanceFail", new Object[] {sec
/*    */       
/* 49 */         .getEndpointClass().getName() }), e); }
/*    */     Object pojo;
/* 51 */     setPojo(pojo);
/*    */     
/*    */ 
/*    */ 
/* 55 */     Map<String, String> pathParameters = (Map)sec.getUserProperties().get("org.apache.tomcat.websocket.pojo.PojoEndpoint.pathParams");
/*    */     
/* 57 */     setPathParameters(pathParameters);
/*    */     
/*    */ 
/* 60 */     PojoMethodMapping methodMapping = (PojoMethodMapping)sec.getUserProperties().get("org.apache.tomcat.websocket.pojo.PojoEndpoint.methodMapping");
/*    */     
/* 62 */     setMethodMapping(methodMapping);
/*    */     
/* 64 */     doOnOpen(session, endpointConfig);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\pojo\PojoEndpointServer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */