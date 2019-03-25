/*    */ package org.apache.tomcat.util.net.jsse;
/*    */ 
/*    */ import javax.net.ssl.SSLSession;
/*    */ import org.apache.tomcat.util.compat.JreCompat;
/*    */ import org.apache.tomcat.util.net.SSLHostConfigCertificate;
/*    */ import org.apache.tomcat.util.net.SSLImplementation;
/*    */ import org.apache.tomcat.util.net.SSLSupport;
/*    */ import org.apache.tomcat.util.net.SSLUtil;
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
/*    */ public class JSSEImplementation
/*    */   extends SSLImplementation
/*    */ {
/*    */   public JSSEImplementation()
/*    */   {
/* 40 */     JSSESupport.init();
/*    */   }
/*    */   
/*    */   public SSLSupport getSSLSupport(SSLSession session)
/*    */   {
/* 45 */     return new JSSESupport(session);
/*    */   }
/*    */   
/*    */   public SSLUtil getSSLUtil(SSLHostConfigCertificate certificate)
/*    */   {
/* 50 */     return new JSSEUtil(certificate);
/*    */   }
/*    */   
/*    */   public boolean isAlpnSupported()
/*    */   {
/* 55 */     return JreCompat.isJre9Available();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\jsse\JSSEImplementation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */