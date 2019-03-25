/*    */ package org.apache.tomcat.websocket.server;
/*    */ 
/*    */ import java.util.Map;
/*    */ import javax.websocket.server.ServerEndpointConfig;
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
/*    */ class WsMappingResult
/*    */ {
/*    */   private final ServerEndpointConfig config;
/*    */   private final Map<String, String> pathParams;
/*    */   
/*    */   WsMappingResult(ServerEndpointConfig config, Map<String, String> pathParams)
/*    */   {
/* 31 */     this.config = config;
/* 32 */     this.pathParams = pathParams;
/*    */   }
/*    */   
/*    */   ServerEndpointConfig getConfig()
/*    */   {
/* 37 */     return this.config;
/*    */   }
/*    */   
/*    */   Map<String, String> getPathParams()
/*    */   {
/* 42 */     return this.pathParams;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\server\WsMappingResult.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */