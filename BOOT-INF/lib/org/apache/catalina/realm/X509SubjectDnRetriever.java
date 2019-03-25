/*    */ package org.apache.catalina.realm;
/*    */ 
/*    */ import java.security.Principal;
/*    */ import java.security.cert.X509Certificate;
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
/*    */ public class X509SubjectDnRetriever
/*    */   implements X509UsernameRetriever
/*    */ {
/*    */   public String getUsername(X509Certificate clientCert)
/*    */   {
/* 29 */     return clientCert.getSubjectDN().getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\realm\X509SubjectDnRetriever.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */