/*    */ package ch.qos.logback.core.net;
/*    */ 
/*    */ import ch.qos.logback.core.net.ssl.ConfigurableSSLSocketFactory;
/*    */ import ch.qos.logback.core.net.ssl.SSLComponent;
/*    */ import ch.qos.logback.core.net.ssl.SSLConfiguration;
/*    */ import ch.qos.logback.core.net.ssl.SSLParametersConfiguration;
/*    */ import javax.net.SocketFactory;
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
/*    */ public abstract class AbstractSSLSocketAppender<E>
/*    */   extends AbstractSocketAppender<E>
/*    */   implements SSLComponent
/*    */ {
/*    */   private SSLConfiguration ssl;
/*    */   private SocketFactory socketFactory;
/*    */   
/*    */   protected SocketFactory getSocketFactory()
/*    */   {
/* 48 */     return this.socketFactory;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void start()
/*    */   {
/*    */     try
/*    */     {
/* 57 */       SSLContext sslContext = getSsl().createContext(this);
/* 58 */       SSLParametersConfiguration parameters = getSsl().getParameters();
/* 59 */       parameters.setContext(getContext());
/* 60 */       this.socketFactory = new ConfigurableSSLSocketFactory(parameters, sslContext.getSocketFactory());
/* 61 */       super.start();
/*    */     } catch (Exception ex) {
/* 63 */       addError(ex.getMessage(), ex);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public SSLConfiguration getSsl()
/*    */   {
/* 73 */     if (this.ssl == null) {
/* 74 */       this.ssl = new SSLConfiguration();
/*    */     }
/* 76 */     return this.ssl;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setSsl(SSLConfiguration ssl)
/*    */   {
/* 84 */     this.ssl = ssl;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\net\AbstractSSLSocketAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */