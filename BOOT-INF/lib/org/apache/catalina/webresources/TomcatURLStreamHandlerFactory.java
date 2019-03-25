/*     */ package org.apache.catalina.webresources;
/*     */ 
/*     */ import java.net.URL;
/*     */ import java.net.URLStreamHandler;
/*     */ import java.net.URLStreamHandlerFactory;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import org.apache.catalina.webresources.war.Handler;
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
/*     */ public class TomcatURLStreamHandlerFactory
/*     */   implements URLStreamHandlerFactory
/*     */ {
/*     */   private static final String WAR_PROTOCOL = "war";
/*     */   private static final String CLASSPATH_PROTOCOL = "classpath";
/*  33 */   private static volatile TomcatURLStreamHandlerFactory instance = null;
/*     */   
/*     */ 
/*     */ 
/*     */   private final boolean registered;
/*     */   
/*     */ 
/*     */ 
/*     */   public static TomcatURLStreamHandlerFactory getInstance()
/*     */   {
/*  43 */     getInstanceInternal(true);
/*  44 */     return instance;
/*     */   }
/*     */   
/*     */ 
/*     */   private static TomcatURLStreamHandlerFactory getInstanceInternal(boolean register)
/*     */   {
/*  50 */     if (instance == null) {
/*  51 */       synchronized (TomcatURLStreamHandlerFactory.class) {
/*  52 */         if (instance == null) {
/*  53 */           instance = new TomcatURLStreamHandlerFactory(register);
/*     */         }
/*     */       }
/*     */     }
/*  57 */     return instance;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  64 */   private final List<URLStreamHandlerFactory> userFactories = new CopyOnWriteArrayList();
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
/*     */   public static boolean register()
/*     */   {
/*  77 */     return getInstanceInternal(true).isRegistered();
/*     */   }
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
/*     */   public static boolean disable()
/*     */   {
/*  92 */     return !getInstanceInternal(false).isRegistered();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void release(ClassLoader classLoader)
/*     */   {
/* 104 */     if (instance == null) {
/* 105 */       return;
/*     */     }
/* 107 */     List<URLStreamHandlerFactory> factories = instance.userFactories;
/* 108 */     for (URLStreamHandlerFactory factory : factories) {
/* 109 */       ClassLoader factoryLoader = factory.getClass().getClassLoader();
/* 110 */       while (factoryLoader != null) {
/* 111 */         if (classLoader.equals(factoryLoader))
/*     */         {
/*     */ 
/*     */ 
/* 115 */           factories.remove(factory);
/* 116 */           break;
/*     */         }
/* 118 */         factoryLoader = factoryLoader.getParent();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private TomcatURLStreamHandlerFactory(boolean register)
/*     */   {
/* 128 */     this.registered = register;
/* 129 */     if (register) {
/* 130 */       URL.setURLStreamHandlerFactory(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isRegistered()
/*     */   {
/* 136 */     return this.registered;
/*     */   }
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
/*     */   public void addUserFactory(URLStreamHandlerFactory factory)
/*     */   {
/* 150 */     this.userFactories.add(factory);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public URLStreamHandler createURLStreamHandler(String protocol)
/*     */   {
/* 159 */     if ("war".equals(protocol))
/* 160 */       return new Handler();
/* 161 */     if ("classpath".equals(protocol)) {
/* 162 */       return new ClasspathURLStreamHandler();
/*     */     }
/*     */     
/*     */ 
/* 166 */     for (URLStreamHandlerFactory factory : this.userFactories)
/*     */     {
/* 168 */       URLStreamHandler handler = factory.createURLStreamHandler(protocol);
/* 169 */       if (handler != null) {
/* 170 */         return handler;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 175 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\webresources\TomcatURLStreamHandlerFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */