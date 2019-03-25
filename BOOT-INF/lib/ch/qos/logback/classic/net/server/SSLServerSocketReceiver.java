/*    */ package ch.qos.logback.classic.net.server;
/*    */ 
/*    */ import ch.qos.logback.core.net.ssl.ConfigurableSSLServerSocketFactory;
/*    */ import ch.qos.logback.core.net.ssl.SSLComponent;
/*    */ import ch.qos.logback.core.net.ssl.SSLConfiguration;
/*    */ import ch.qos.logback.core.net.ssl.SSLParametersConfiguration;
/*    */ import javax.net.ServerSocketFactory;
/*    */ import javax.net.ssl.SSLContext;
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
/*    */ public class SSLServerSocketReceiver
/*    */   extends ServerSocketReceiver
/*    */   implements SSLComponent
/*    */ {
/*    */   private SSLConfiguration ssl;
/*    */   private ServerSocketFactory socketFactory;
/*    */   
/*    */   protected ServerSocketFactory getServerSocketFactory()
/*    */     throws Exception
/*    */   {
/* 39 */     if (this.socketFactory == null) {
/* 40 */       SSLContext sslContext = getSsl().createContext(this);
/* 41 */       SSLParametersConfiguration parameters = getSsl().getParameters();
/* 42 */       parameters.setContext(getContext());
/* 43 */       this.socketFactory = new ConfigurableSSLServerSocketFactory(parameters, sslContext.getServerSocketFactory());
/*    */     }
/* 45 */     return this.socketFactory;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public SSLConfiguration getSsl()
/*    */   {
/* 54 */     if (this.ssl == null) {
/* 55 */       this.ssl = new SSLConfiguration();
/*    */     }
/* 57 */     return this.ssl;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setSsl(SSLConfiguration ssl)
/*    */   {
/* 65 */     this.ssl = ssl;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\net\server\SSLServerSocketReceiver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */