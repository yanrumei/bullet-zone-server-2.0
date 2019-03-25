/*    */ package javax.websocket;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
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
/*    */ final class DefaultClientEndpointConfig
/*    */   implements ClientEndpointConfig
/*    */ {
/*    */   private final List<String> preferredSubprotocols;
/*    */   private final List<Extension> extensions;
/*    */   private final List<Class<? extends Encoder>> encoders;
/*    */   private final List<Class<? extends Decoder>> decoders;
/* 29 */   private final Map<String, Object> userProperties = new ConcurrentHashMap();
/*    */   
/*    */ 
/*    */   private final ClientEndpointConfig.Configurator configurator;
/*    */   
/*    */ 
/*    */ 
/*    */   DefaultClientEndpointConfig(List<String> preferredSubprotocols, List<Extension> extensions, List<Class<? extends Encoder>> encoders, List<Class<? extends Decoder>> decoders, ClientEndpointConfig.Configurator configurator)
/*    */   {
/* 38 */     this.preferredSubprotocols = preferredSubprotocols;
/* 39 */     this.extensions = extensions;
/* 40 */     this.decoders = decoders;
/* 41 */     this.encoders = encoders;
/* 42 */     this.configurator = configurator;
/*    */   }
/*    */   
/*    */ 
/*    */   public List<String> getPreferredSubprotocols()
/*    */   {
/* 48 */     return this.preferredSubprotocols;
/*    */   }
/*    */   
/*    */ 
/*    */   public List<Extension> getExtensions()
/*    */   {
/* 54 */     return this.extensions;
/*    */   }
/*    */   
/*    */ 
/*    */   public List<Class<? extends Encoder>> getEncoders()
/*    */   {
/* 60 */     return this.encoders;
/*    */   }
/*    */   
/*    */ 
/*    */   public List<Class<? extends Decoder>> getDecoders()
/*    */   {
/* 66 */     return this.decoders;
/*    */   }
/*    */   
/*    */ 
/*    */   public final Map<String, Object> getUserProperties()
/*    */   {
/* 72 */     return this.userProperties;
/*    */   }
/*    */   
/*    */ 
/*    */   public ClientEndpointConfig.Configurator getConfigurator()
/*    */   {
/* 78 */     return this.configurator;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\javax\websocket\DefaultClientEndpointConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */