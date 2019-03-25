/*    */ package org.apache.tomcat.util.net.openssl;
/*    */ 
/*    */ import javax.net.ssl.SSLSession;
/*    */ import org.apache.tomcat.util.net.SSLHostConfigCertificate;
/*    */ import org.apache.tomcat.util.net.SSLImplementation;
/*    */ import org.apache.tomcat.util.net.SSLSupport;
/*    */ import org.apache.tomcat.util.net.SSLUtil;
/*    */ import org.apache.tomcat.util.net.jsse.JSSESupport;
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
/*    */ public class OpenSSLImplementation
/*    */   extends SSLImplementation
/*    */ {
/*    */   public SSLSupport getSSLSupport(SSLSession session)
/*    */   {
/* 31 */     return new JSSESupport(session);
/*    */   }
/*    */   
/*    */   public SSLUtil getSSLUtil(SSLHostConfigCertificate certificate)
/*    */   {
/* 36 */     return new OpenSSLUtil(certificate);
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean isAlpnSupported()
/*    */   {
/* 42 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\openssl\OpenSSLImplementation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */