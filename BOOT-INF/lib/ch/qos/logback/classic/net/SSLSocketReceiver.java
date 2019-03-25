/*    */ package ch.qos.logback.classic.net;
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
/*    */ public class SSLSocketReceiver
/*    */   extends SocketReceiver
/*    */   implements SSLComponent
/*    */ {
/*    */   private SSLConfiguration ssl;
/*    */   private SocketFactory socketFactory;
/*    */   
/*    */   protected SocketFactory getSocketFactory()
/*    */   {
/* 41 */     return this.socketFactory;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected boolean shouldStart()
/*    */   {
/*    */     try
/*    */     {
/* 50 */       SSLContext sslContext = getSsl().createContext(this);
/* 51 */       SSLParametersConfiguration parameters = getSsl().getParameters();
/* 52 */       parameters.setContext(getContext());
/* 53 */       this.socketFactory = new ConfigurableSSLSocketFactory(parameters, sslContext.getSocketFactory());
/* 54 */       return super.shouldStart();
/*    */     } catch (Exception ex) {
/* 56 */       addError(ex.getMessage(), ex); }
/* 57 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public SSLConfiguration getSsl()
/*    */   {
/* 67 */     if (this.ssl == null) {
/* 68 */       this.ssl = new SSLConfiguration();
/*    */     }
/* 70 */     return this.ssl;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setSsl(SSLConfiguration ssl)
/*    */   {
/* 78 */     this.ssl = ssl;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\net\SSLSocketReceiver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */