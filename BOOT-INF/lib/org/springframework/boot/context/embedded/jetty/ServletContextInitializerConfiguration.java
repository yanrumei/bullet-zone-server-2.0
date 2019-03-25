/*    */ package org.springframework.boot.context.embedded.jetty;
/*    */ 
/*    */ import javax.servlet.ServletException;
/*    */ import org.eclipse.jetty.server.handler.ContextHandler.Context;
/*    */ import org.eclipse.jetty.util.component.AbstractLifeCycle;
/*    */ import org.eclipse.jetty.webapp.AbstractConfiguration;
/*    */ import org.eclipse.jetty.webapp.WebAppContext;
/*    */ import org.springframework.boot.web.servlet.ServletContextInitializer;
/*    */ import org.springframework.util.Assert;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServletContextInitializerConfiguration
/*    */   extends AbstractConfiguration
/*    */ {
/*    */   private final ServletContextInitializer[] initializers;
/*    */   
/*    */   public ServletContextInitializerConfiguration(ServletContextInitializer... initializers)
/*    */   {
/* 46 */     Assert.notNull(initializers, "Initializers must not be null");
/* 47 */     this.initializers = initializers;
/*    */   }
/*    */   
/*    */   public void configure(WebAppContext context) throws Exception
/*    */   {
/* 52 */     context.addBean(new Initializer(context), true);
/*    */   }
/*    */   
/*    */ 
/*    */   private class Initializer
/*    */     extends AbstractLifeCycle
/*    */   {
/*    */     private final WebAppContext context;
/*    */     
/*    */ 
/*    */     Initializer(WebAppContext context)
/*    */     {
/* 64 */       this.context = context;
/*    */     }
/*    */     
/*    */     /* Error */
/*    */     protected void doStart()
/*    */       throws Exception
/*    */     {
/*    */       // Byte code:
/*    */       //   0: invokestatic 4	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*    */       //   3: invokevirtual 5	java/lang/Thread:getContextClassLoader	()Ljava/lang/ClassLoader;
/*    */       //   6: astore_1
/*    */       //   7: invokestatic 4	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*    */       //   10: aload_0
/*    */       //   11: getfield 3	org/springframework/boot/context/embedded/jetty/ServletContextInitializerConfiguration$Initializer:context	Lorg/eclipse/jetty/webapp/WebAppContext;
/*    */       //   14: invokevirtual 6	org/eclipse/jetty/webapp/WebAppContext:getClassLoader	()Ljava/lang/ClassLoader;
/*    */       //   17: invokevirtual 7	java/lang/Thread:setContextClassLoader	(Ljava/lang/ClassLoader;)V
/*    */       //   20: aload_0
/*    */       //   21: invokespecial 8	org/springframework/boot/context/embedded/jetty/ServletContextInitializerConfiguration$Initializer:callInitializers	()V
/*    */       //   24: invokestatic 4	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*    */       //   27: aload_1
/*    */       //   28: invokevirtual 7	java/lang/Thread:setContextClassLoader	(Ljava/lang/ClassLoader;)V
/*    */       //   31: goto +13 -> 44
/*    */       //   34: astore_2
/*    */       //   35: invokestatic 4	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*    */       //   38: aload_1
/*    */       //   39: invokevirtual 7	java/lang/Thread:setContextClassLoader	(Ljava/lang/ClassLoader;)V
/*    */       //   42: aload_2
/*    */       //   43: athrow
/*    */       //   44: return
/*    */       // Line number table:
/*    */       //   Java source line #69	-> byte code offset #0
/*    */       //   Java source line #70	-> byte code offset #7
/*    */       //   Java source line #72	-> byte code offset #20
/*    */       //   Java source line #75	-> byte code offset #24
/*    */       //   Java source line #76	-> byte code offset #31
/*    */       //   Java source line #75	-> byte code offset #34
/*    */       //   Java source line #77	-> byte code offset #44
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	45	0	this	Initializer
/*    */       //   6	33	1	classLoader	ClassLoader
/*    */       //   34	9	2	localObject	Object
/*    */       // Exception table:
/*    */       //   from	to	target	type
/*    */       //   20	24	34	finally
/*    */     }
/*    */     
/*    */     private void callInitializers()
/*    */       throws ServletException
/*    */     {
/*    */       try
/*    */       {
/* 81 */         setExtendedListenerTypes(true);
/* 82 */         for (ServletContextInitializer initializer : ServletContextInitializerConfiguration.this.initializers) {
/* 83 */           initializer.onStartup(this.context.getServletContext());
/*    */         }
/*    */       }
/*    */       finally {
/* 87 */         setExtendedListenerTypes(false);
/*    */       }
/*    */     }
/*    */     
/*    */     private void setExtendedListenerTypes(boolean extended) {
/*    */       try {
/* 93 */         this.context.getServletContext().setExtendedListenerTypes(extended);
/*    */       }
/*    */       catch (NoSuchMethodError localNoSuchMethodError) {}
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\jetty\ServletContextInitializerConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */