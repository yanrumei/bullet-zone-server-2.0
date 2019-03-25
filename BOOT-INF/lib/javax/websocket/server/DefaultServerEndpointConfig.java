/*    */ package javax.websocket.server;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import javax.websocket.Decoder;
/*    */ import javax.websocket.Encoder;
/*    */ import javax.websocket.Extension;
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
/*    */ final class DefaultServerEndpointConfig
/*    */   implements ServerEndpointConfig
/*    */ {
/*    */   private final Class<?> endpointClass;
/*    */   private final String path;
/*    */   private final List<String> subprotocols;
/*    */   private final List<Extension> extensions;
/*    */   private final List<Class<? extends Encoder>> encoders;
/*    */   private final List<Class<? extends Decoder>> decoders;
/*    */   private final ServerEndpointConfig.Configurator serverEndpointConfigurator;
/* 39 */   private final Map<String, Object> userProperties = new ConcurrentHashMap();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   DefaultServerEndpointConfig(Class<?> endpointClass, String path, List<String> subprotocols, List<Extension> extensions, List<Class<? extends Encoder>> encoders, List<Class<? extends Decoder>> decoders, ServerEndpointConfig.Configurator serverEndpointConfigurator)
/*    */   {
/* 47 */     this.endpointClass = endpointClass;
/* 48 */     this.path = path;
/* 49 */     this.subprotocols = subprotocols;
/* 50 */     this.extensions = extensions;
/* 51 */     this.encoders = encoders;
/* 52 */     this.decoders = decoders;
/* 53 */     this.serverEndpointConfigurator = serverEndpointConfigurator;
/*    */   }
/*    */   
/*    */   public Class<?> getEndpointClass()
/*    */   {
/* 58 */     return this.endpointClass;
/*    */   }
/*    */   
/*    */   public List<Class<? extends Encoder>> getEncoders()
/*    */   {
/* 63 */     return this.encoders;
/*    */   }
/*    */   
/*    */   public List<Class<? extends Decoder>> getDecoders()
/*    */   {
/* 68 */     return this.decoders;
/*    */   }
/*    */   
/*    */   public String getPath()
/*    */   {
/* 73 */     return this.path;
/*    */   }
/*    */   
/*    */   public ServerEndpointConfig.Configurator getConfigurator()
/*    */   {
/* 78 */     return this.serverEndpointConfigurator;
/*    */   }
/*    */   
/*    */   public final Map<String, Object> getUserProperties()
/*    */   {
/* 83 */     return this.userProperties;
/*    */   }
/*    */   
/*    */   public final List<String> getSubprotocols()
/*    */   {
/* 88 */     return this.subprotocols;
/*    */   }
/*    */   
/*    */   public final List<Extension> getExtensions()
/*    */   {
/* 93 */     return this.extensions;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\javax\websocket\server\DefaultServerEndpointConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */