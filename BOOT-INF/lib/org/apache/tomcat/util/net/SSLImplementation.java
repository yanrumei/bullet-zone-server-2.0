/*    */ package org.apache.tomcat.util.net;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import javax.net.ssl.SSLSession;
/*    */ import org.apache.juli.logging.Log;
/*    */ import org.apache.juli.logging.LogFactory;
/*    */ import org.apache.tomcat.util.net.jsse.JSSEImplementation;
/*    */ import org.apache.tomcat.util.res.StringManager;
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
/*    */ public abstract class SSLImplementation
/*    */ {
/* 34 */   private static final Log logger = LogFactory.getLog(SSLImplementation.class);
/* 35 */   private static final StringManager sm = StringManager.getManager(SSLImplementation.class);
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
/*    */   public static SSLImplementation getInstance(String className)
/*    */     throws ClassNotFoundException
/*    */   {
/* 51 */     if (className == null) {
/* 52 */       return new JSSEImplementation();
/*    */     }
/*    */     try {
/* 55 */       Class<?> clazz = Class.forName(className);
/* 56 */       return (SSLImplementation)clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/*    */     } catch (Exception e) {
/* 58 */       String msg = sm.getString("sslImplementation.cnfe", new Object[] { className });
/* 59 */       if (logger.isDebugEnabled()) {
/* 60 */         logger.debug(msg, e);
/*    */       }
/* 62 */       throw new ClassNotFoundException(msg, e);
/*    */     }
/*    */   }
/*    */   
/*    */   public abstract SSLSupport getSSLSupport(SSLSession paramSSLSession);
/*    */   
/*    */   public abstract SSLUtil getSSLUtil(SSLHostConfigCertificate paramSSLHostConfigCertificate);
/*    */   
/*    */   public abstract boolean isAlpnSupported();
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\SSLImplementation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */