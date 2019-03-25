/*     */ package org.springframework.boot.context.embedded.jetty;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.net.URLStreamHandler;
/*     */ import java.net.URLStreamHandlerFactory;
/*     */ import javax.servlet.ServletContainerInitializer;
/*     */ import org.eclipse.jetty.server.handler.ContextHandler.Context;
/*     */ import org.eclipse.jetty.util.component.AbstractLifeCycle;
/*     */ import org.eclipse.jetty.webapp.WebAppContext;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ class JasperInitializer
/*     */   extends AbstractLifeCycle
/*     */ {
/*  41 */   private static final String[] INITIALIZER_CLASSES = { "org.eclipse.jetty.apache.jsp.JettyJasperInitializer", "org.apache.jasper.servlet.JasperInitializer" };
/*     */   
/*     */   private final WebAppContext context;
/*     */   
/*     */   private final ServletContainerInitializer initializer;
/*     */   
/*     */ 
/*     */   JasperInitializer(WebAppContext context)
/*     */   {
/*  50 */     this.context = context;
/*  51 */     this.initializer = newInitializer();
/*     */   }
/*     */   
/*     */   private ServletContainerInitializer newInitializer() {
/*  55 */     for (String className : INITIALIZER_CLASSES) {
/*     */       try {
/*  57 */         Class<?> initializerClass = ClassUtils.forName(className, null);
/*  58 */         return (ServletContainerInitializer)initializerClass.newInstance();
/*     */       }
/*     */       catch (Exception localException) {}
/*     */     }
/*     */     
/*     */ 
/*  64 */     return null;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected void doStart()
/*     */     throws Exception
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 4	org/springframework/boot/context/embedded/jetty/JasperInitializer:initializer	Ljavax/servlet/ServletContainerInitializer;
/*     */     //   4: ifnonnull +4 -> 8
/*     */     //   7: return
/*     */     //   8: new 10	org/springframework/boot/context/embedded/jetty/JasperInitializer$WarUrlStreamHandlerFactory
/*     */     //   11: dup
/*     */     //   12: aconst_null
/*     */     //   13: invokespecial 11	org/springframework/boot/context/embedded/jetty/JasperInitializer$WarUrlStreamHandlerFactory:<init>	(Lorg/springframework/boot/context/embedded/jetty/JasperInitializer$1;)V
/*     */     //   16: invokestatic 12	java/net/URL:setURLStreamHandlerFactory	(Ljava/net/URLStreamHandlerFactory;)V
/*     */     //   19: goto +4 -> 23
/*     */     //   22: astore_1
/*     */     //   23: invokestatic 14	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   26: invokevirtual 15	java/lang/Thread:getContextClassLoader	()Ljava/lang/ClassLoader;
/*     */     //   29: astore_1
/*     */     //   30: invokestatic 14	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   33: aload_0
/*     */     //   34: getfield 2	org/springframework/boot/context/embedded/jetty/JasperInitializer:context	Lorg/eclipse/jetty/webapp/WebAppContext;
/*     */     //   37: invokevirtual 16	org/eclipse/jetty/webapp/WebAppContext:getClassLoader	()Ljava/lang/ClassLoader;
/*     */     //   40: invokevirtual 17	java/lang/Thread:setContextClassLoader	(Ljava/lang/ClassLoader;)V
/*     */     //   43: aload_0
/*     */     //   44: iconst_1
/*     */     //   45: invokespecial 18	org/springframework/boot/context/embedded/jetty/JasperInitializer:setExtendedListenerTypes	(Z)V
/*     */     //   48: aload_0
/*     */     //   49: getfield 4	org/springframework/boot/context/embedded/jetty/JasperInitializer:initializer	Ljavax/servlet/ServletContainerInitializer;
/*     */     //   52: aconst_null
/*     */     //   53: aload_0
/*     */     //   54: getfield 2	org/springframework/boot/context/embedded/jetty/JasperInitializer:context	Lorg/eclipse/jetty/webapp/WebAppContext;
/*     */     //   57: invokevirtual 19	org/eclipse/jetty/webapp/WebAppContext:getServletContext	()Lorg/eclipse/jetty/server/handler/ContextHandler$Context;
/*     */     //   60: invokeinterface 20 3 0
/*     */     //   65: aload_0
/*     */     //   66: iconst_0
/*     */     //   67: invokespecial 18	org/springframework/boot/context/embedded/jetty/JasperInitializer:setExtendedListenerTypes	(Z)V
/*     */     //   70: goto +11 -> 81
/*     */     //   73: astore_2
/*     */     //   74: aload_0
/*     */     //   75: iconst_0
/*     */     //   76: invokespecial 18	org/springframework/boot/context/embedded/jetty/JasperInitializer:setExtendedListenerTypes	(Z)V
/*     */     //   79: aload_2
/*     */     //   80: athrow
/*     */     //   81: invokestatic 14	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   84: aload_1
/*     */     //   85: invokevirtual 17	java/lang/Thread:setContextClassLoader	(Ljava/lang/ClassLoader;)V
/*     */     //   88: goto +13 -> 101
/*     */     //   91: astore_3
/*     */     //   92: invokestatic 14	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   95: aload_1
/*     */     //   96: invokevirtual 17	java/lang/Thread:setContextClassLoader	(Ljava/lang/ClassLoader;)V
/*     */     //   99: aload_3
/*     */     //   100: athrow
/*     */     //   101: return
/*     */     // Line number table:
/*     */     //   Java source line #69	-> byte code offset #0
/*     */     //   Java source line #70	-> byte code offset #7
/*     */     //   Java source line #73	-> byte code offset #8
/*     */     //   Java source line #77	-> byte code offset #19
/*     */     //   Java source line #75	-> byte code offset #22
/*     */     //   Java source line #78	-> byte code offset #23
/*     */     //   Java source line #80	-> byte code offset #30
/*     */     //   Java source line #82	-> byte code offset #43
/*     */     //   Java source line #83	-> byte code offset #48
/*     */     //   Java source line #86	-> byte code offset #65
/*     */     //   Java source line #87	-> byte code offset #70
/*     */     //   Java source line #86	-> byte code offset #73
/*     */     //   Java source line #90	-> byte code offset #81
/*     */     //   Java source line #91	-> byte code offset #88
/*     */     //   Java source line #90	-> byte code offset #91
/*     */     //   Java source line #92	-> byte code offset #101
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	102	0	this	JasperInitializer
/*     */     //   22	1	1	localError	Error
/*     */     //   29	67	1	classLoader	ClassLoader
/*     */     //   73	7	2	localObject1	Object
/*     */     //   91	9	3	localObject2	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   8	19	22	java/lang/Error
/*     */     //   43	65	73	finally
/*     */     //   30	81	91	finally
/*     */   }
/*     */   
/*     */   private void setExtendedListenerTypes(boolean extended)
/*     */   {
/*     */     try
/*     */     {
/*  96 */       this.context.getServletContext().setExtendedListenerTypes(extended);
/*     */     }
/*     */     catch (NoSuchMethodError localNoSuchMethodError) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class WarUrlStreamHandlerFactory
/*     */     implements URLStreamHandlerFactory
/*     */   {
/*     */     public URLStreamHandler createURLStreamHandler(String protocol)
/*     */     {
/* 110 */       if ("war".equals(protocol)) {
/* 111 */         return new JasperInitializer.WarUrlStreamHandler(null);
/*     */       }
/* 113 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class WarUrlStreamHandler
/*     */     extends URLStreamHandler
/*     */   {
/*     */     protected void parseURL(URL u, String spec, int start, int limit)
/*     */     {
/* 127 */       String path = "jar:" + spec.substring("war:".length());
/* 128 */       int separator = path.indexOf("*/");
/* 129 */       if (separator >= 0)
/*     */       {
/* 131 */         path = path.substring(0, separator) + "!/" + path.substring(separator + 2);
/*     */       }
/* 133 */       setURL(u, u.getProtocol(), "", -1, null, null, path, null, null);
/*     */     }
/*     */     
/*     */     protected URLConnection openConnection(URL u) throws IOException
/*     */     {
/* 138 */       return new JasperInitializer.WarURLConnection(u);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class WarURLConnection
/*     */     extends URLConnection
/*     */   {
/*     */     private final URLConnection connection;
/*     */     
/*     */     protected WarURLConnection(URL url)
/*     */       throws IOException
/*     */     {
/* 151 */       super();
/* 152 */       this.connection = new URL(url.getFile()).openConnection();
/*     */     }
/*     */     
/*     */     public void connect() throws IOException
/*     */     {
/* 157 */       if (!this.connected) {
/* 158 */         this.connection.connect();
/* 159 */         this.connected = true;
/*     */       }
/*     */     }
/*     */     
/*     */     public InputStream getInputStream() throws IOException
/*     */     {
/* 165 */       connect();
/* 166 */       return this.connection.getInputStream();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\jetty\JasperInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */