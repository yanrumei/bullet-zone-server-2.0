/*    */ package org.apache.tomcat.websocket.server;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import javax.websocket.Decoder;
/*    */ import javax.websocket.Encoder;
/*    */ import javax.websocket.Extension;
/*    */ import javax.websocket.server.ServerEndpointConfig;
/*    */ import javax.websocket.server.ServerEndpointConfig.Configurator;
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
/*    */ class WsPerSessionServerEndpointConfig
/*    */   implements ServerEndpointConfig
/*    */ {
/*    */   private final ServerEndpointConfig perEndpointConfig;
/* 37 */   private final Map<String, Object> perSessionUserProperties = new ConcurrentHashMap();
/*    */   
/*    */   WsPerSessionServerEndpointConfig(ServerEndpointConfig perEndpointConfig)
/*    */   {
/* 41 */     this.perEndpointConfig = perEndpointConfig;
/* 42 */     this.perSessionUserProperties.putAll(perEndpointConfig.getUserProperties());
/*    */   }
/*    */   
/*    */   public List<Class<? extends Encoder>> getEncoders()
/*    */   {
/* 47 */     return this.perEndpointConfig.getEncoders();
/*    */   }
/*    */   
/*    */   public List<Class<? extends Decoder>> getDecoders()
/*    */   {
/* 52 */     return this.perEndpointConfig.getDecoders();
/*    */   }
/*    */   
/*    */   public Map<String, Object> getUserProperties()
/*    */   {
/* 57 */     return this.perSessionUserProperties;
/*    */   }
/*    */   
/*    */   public Class<?> getEndpointClass()
/*    */   {
/* 62 */     return this.perEndpointConfig.getEndpointClass();
/*    */   }
/*    */   
/*    */   public String getPath()
/*    */   {
/* 67 */     return this.perEndpointConfig.getPath();
/*    */   }
/*    */   
/*    */   public List<String> getSubprotocols()
/*    */   {
/* 72 */     return this.perEndpointConfig.getSubprotocols();
/*    */   }
/*    */   
/*    */   public List<Extension> getExtensions()
/*    */   {
/* 77 */     return this.perEndpointConfig.getExtensions();
/*    */   }
/*    */   
/*    */   public ServerEndpointConfig.Configurator getConfigurator()
/*    */   {
/* 82 */     return this.perEndpointConfig.getConfigurator();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\server\WsPerSessionServerEndpointConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */