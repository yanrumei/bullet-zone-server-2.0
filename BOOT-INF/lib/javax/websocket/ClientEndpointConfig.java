/*     */ package javax.websocket;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract interface ClientEndpointConfig
/*     */   extends EndpointConfig
/*     */ {
/*     */   public abstract List<String> getPreferredSubprotocols();
/*     */   
/*     */   public abstract List<Extension> getExtensions();
/*     */   
/*     */   public abstract Configurator getConfigurator();
/*     */   
/*     */   public static class Configurator
/*     */   {
/*     */     public void beforeRequest(Map<String, List<String>> headers) {}
/*     */     
/*     */     public void afterResponse(HandshakeResponse handshakeResponse) {}
/*     */   }
/*     */   
/*     */   public static final class Builder
/*     */   {
/*  33 */     private static final ClientEndpointConfig.Configurator DEFAULT_CONFIGURATOR = new ClientEndpointConfig.Configurator() {};
/*     */     
/*     */ 
/*     */     public static Builder create()
/*     */     {
/*  38 */       return new Builder();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  46 */     private ClientEndpointConfig.Configurator configurator = DEFAULT_CONFIGURATOR;
/*  47 */     private List<String> preferredSubprotocols = Collections.emptyList();
/*  48 */     private List<Extension> extensions = Collections.emptyList();
/*     */     
/*  50 */     private List<Class<? extends Encoder>> encoders = Collections.emptyList();
/*     */     
/*  52 */     private List<Class<? extends Decoder>> decoders = Collections.emptyList();
/*     */     
/*     */     public ClientEndpointConfig build()
/*     */     {
/*  56 */       return new DefaultClientEndpointConfig(this.preferredSubprotocols, this.extensions, this.encoders, this.decoders, this.configurator);
/*     */     }
/*     */     
/*     */ 
/*     */     public Builder configurator(ClientEndpointConfig.Configurator configurator)
/*     */     {
/*  62 */       if (configurator == null) {
/*  63 */         this.configurator = DEFAULT_CONFIGURATOR;
/*     */       } else {
/*  65 */         this.configurator = configurator;
/*     */       }
/*  67 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */     public Builder preferredSubprotocols(List<String> preferredSubprotocols)
/*     */     {
/*  73 */       if ((preferredSubprotocols == null) || 
/*  74 */         (preferredSubprotocols.size() == 0)) {
/*  75 */         this.preferredSubprotocols = Collections.emptyList();
/*     */       }
/*     */       else {
/*  78 */         this.preferredSubprotocols = Collections.unmodifiableList(preferredSubprotocols);
/*     */       }
/*  80 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */     public Builder extensions(List<Extension> extensions)
/*     */     {
/*  86 */       if ((extensions == null) || (extensions.size() == 0)) {
/*  87 */         this.extensions = Collections.emptyList();
/*     */       } else {
/*  89 */         this.extensions = Collections.unmodifiableList(extensions);
/*     */       }
/*  91 */       return this;
/*     */     }
/*     */     
/*     */     public Builder encoders(List<Class<? extends Encoder>> encoders)
/*     */     {
/*  96 */       if ((encoders == null) || (encoders.size() == 0)) {
/*  97 */         this.encoders = Collections.emptyList();
/*     */       } else {
/*  99 */         this.encoders = Collections.unmodifiableList(encoders);
/*     */       }
/* 101 */       return this;
/*     */     }
/*     */     
/*     */     public Builder decoders(List<Class<? extends Decoder>> decoders)
/*     */     {
/* 106 */       if ((decoders == null) || (decoders.size() == 0)) {
/* 107 */         this.decoders = Collections.emptyList();
/*     */       } else {
/* 109 */         this.decoders = Collections.unmodifiableList(decoders);
/*     */       }
/* 111 */       return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\javax\websocket\ClientEndpointConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */