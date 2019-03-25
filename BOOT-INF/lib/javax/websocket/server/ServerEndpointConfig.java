/*     */ package javax.websocket.server;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ServiceLoader;
/*     */ import javax.websocket.Decoder;
/*     */ import javax.websocket.Encoder;
/*     */ import javax.websocket.EndpointConfig;
/*     */ import javax.websocket.Extension;
/*     */ import javax.websocket.HandshakeResponse;
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
/*     */ public abstract interface ServerEndpointConfig
/*     */   extends EndpointConfig
/*     */ {
/*     */   public abstract Class<?> getEndpointClass();
/*     */   
/*     */   public abstract String getPath();
/*     */   
/*     */   public abstract List<String> getSubprotocols();
/*     */   
/*     */   public abstract List<Extension> getExtensions();
/*     */   
/*     */   public abstract Configurator getConfigurator();
/*     */   
/*     */   public static final class Builder
/*     */   {
/*     */     private final Class<?> endpointClass;
/*     */     private final String path;
/*     */     
/*     */     public static Builder create(Class<?> endpointClass, String path)
/*     */     {
/*  58 */       return new Builder(endpointClass, path);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  65 */     private List<Class<? extends Encoder>> encoders = Collections.emptyList();
/*     */     
/*  67 */     private List<Class<? extends Decoder>> decoders = Collections.emptyList();
/*  68 */     private List<String> subprotocols = Collections.emptyList();
/*  69 */     private List<Extension> extensions = Collections.emptyList();
/*     */     
/*  71 */     private ServerEndpointConfig.Configurator configurator = ServerEndpointConfig.Configurator.fetchContainerDefaultConfigurator();
/*     */     
/*     */ 
/*     */     private Builder(Class<?> endpointClass, String path)
/*     */     {
/*  76 */       this.endpointClass = endpointClass;
/*  77 */       this.path = path;
/*     */     }
/*     */     
/*     */     public ServerEndpointConfig build() {
/*  81 */       return new DefaultServerEndpointConfig(this.endpointClass, this.path, this.subprotocols, this.extensions, this.encoders, this.decoders, this.configurator);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Builder encoders(List<Class<? extends Encoder>> encoders)
/*     */     {
/*  88 */       if ((encoders == null) || (encoders.size() == 0)) {
/*  89 */         this.encoders = Collections.emptyList();
/*     */       } else {
/*  91 */         this.encoders = Collections.unmodifiableList(encoders);
/*     */       }
/*  93 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */     public Builder decoders(List<Class<? extends Decoder>> decoders)
/*     */     {
/*  99 */       if ((decoders == null) || (decoders.size() == 0)) {
/* 100 */         this.decoders = Collections.emptyList();
/*     */       } else {
/* 102 */         this.decoders = Collections.unmodifiableList(decoders);
/*     */       }
/* 104 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */     public Builder subprotocols(List<String> subprotocols)
/*     */     {
/* 110 */       if ((subprotocols == null) || (subprotocols.size() == 0)) {
/* 111 */         this.subprotocols = Collections.emptyList();
/*     */       } else {
/* 113 */         this.subprotocols = Collections.unmodifiableList(subprotocols);
/*     */       }
/* 115 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */     public Builder extensions(List<Extension> extensions)
/*     */     {
/* 121 */       if ((extensions == null) || (extensions.size() == 0)) {
/* 122 */         this.extensions = Collections.emptyList();
/*     */       } else {
/* 124 */         this.extensions = Collections.unmodifiableList(extensions);
/*     */       }
/* 126 */       return this;
/*     */     }
/*     */     
/*     */     public Builder configurator(ServerEndpointConfig.Configurator serverEndpointConfigurator)
/*     */     {
/* 131 */       if (serverEndpointConfigurator == null) {
/* 132 */         this.configurator = ServerEndpointConfig.Configurator.fetchContainerDefaultConfigurator();
/*     */       } else {
/* 134 */         this.configurator = serverEndpointConfigurator;
/*     */       }
/* 136 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class Configurator
/*     */   {
/* 143 */     private static volatile Configurator defaultImpl = null;
/* 144 */     private static final Object defaultImplLock = new Object();
/*     */     
/*     */     private static final String DEFAULT_IMPL_CLASSNAME = "org.apache.tomcat.websocket.server.DefaultServerEndpointConfigurator";
/*     */     
/*     */     static Configurator fetchContainerDefaultConfigurator()
/*     */     {
/* 150 */       if (defaultImpl == null) {
/* 151 */         synchronized (defaultImplLock) {
/* 152 */           if (defaultImpl == null) {
/* 153 */             defaultImpl = loadDefault();
/*     */           }
/*     */         }
/*     */       }
/* 157 */       return defaultImpl;
/*     */     }
/*     */     
/*     */     private static Configurator loadDefault()
/*     */     {
/* 162 */       Configurator result = null;
/*     */       
/*     */ 
/* 165 */       ServiceLoader<Configurator> serviceLoader = ServiceLoader.load(Configurator.class);
/*     */       
/* 167 */       Iterator<Configurator> iter = serviceLoader.iterator();
/* 168 */       while ((result == null) && (iter.hasNext())) {
/* 169 */         result = (Configurator)iter.next();
/*     */       }
/*     */       
/*     */ 
/* 173 */       if (result == null)
/*     */       {
/*     */         try
/*     */         {
/* 177 */           Class<Configurator> clazz = Class.forName("org.apache.tomcat.websocket.server.DefaultServerEndpointConfigurator");
/*     */           
/* 179 */           result = (Configurator)clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/*     */         }
/*     */         catch (ClassNotFoundException|InstantiationException|IllegalAccessException|IllegalArgumentException|InvocationTargetException|NoSuchMethodException|SecurityException localClassNotFoundException) {}
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 186 */       return result;
/*     */     }
/*     */     
/*     */     public String getNegotiatedSubprotocol(List<String> supported, List<String> requested)
/*     */     {
/* 191 */       return fetchContainerDefaultConfigurator().getNegotiatedSubprotocol(supported, requested);
/*     */     }
/*     */     
/*     */     public List<Extension> getNegotiatedExtensions(List<Extension> installed, List<Extension> requested)
/*     */     {
/* 196 */       return fetchContainerDefaultConfigurator().getNegotiatedExtensions(installed, requested);
/*     */     }
/*     */     
/*     */     public boolean checkOrigin(String originHeaderValue) {
/* 200 */       return fetchContainerDefaultConfigurator().checkOrigin(originHeaderValue);
/*     */     }
/*     */     
/*     */     public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response)
/*     */     {
/* 205 */       fetchContainerDefaultConfigurator().modifyHandshake(sec, request, response);
/*     */     }
/*     */     
/*     */     public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException
/*     */     {
/* 210 */       return (T)fetchContainerDefaultConfigurator().getEndpointInstance(clazz);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\javax\websocket\server\ServerEndpointConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */