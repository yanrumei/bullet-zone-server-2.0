/*    */ package org.apache.tomcat.websocket.pojo;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import javax.websocket.Decoder;
/*    */ import javax.websocket.DeploymentException;
/*    */ import javax.websocket.EndpointConfig;
/*    */ import javax.websocket.Session;
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
/*    */ public class PojoEndpointClient
/*    */   extends PojoEndpointBase
/*    */ {
/*    */   public PojoEndpointClient(Object pojo, List<Class<? extends Decoder>> decoders)
/*    */     throws DeploymentException
/*    */   {
/* 37 */     setPojo(pojo);
/* 38 */     setMethodMapping(new PojoMethodMapping(pojo
/* 39 */       .getClass(), decoders, null));
/* 40 */     setPathParameters(Collections.emptyMap());
/*    */   }
/*    */   
/*    */   public void onOpen(Session session, EndpointConfig config)
/*    */   {
/* 45 */     doOnOpen(session, config);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\pojo\PojoEndpointClient.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */