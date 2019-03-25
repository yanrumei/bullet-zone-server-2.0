/*    */ package org.apache.catalina.startup;
/*    */ 
/*    */ import org.apache.tomcat.util.digester.Digester;
/*    */ import org.apache.tomcat.util.digester.Rule;
/*    */ import org.apache.tomcat.util.net.SSLHostConfig;
/*    */ import org.apache.tomcat.util.net.SSLHostConfigCertificate;
/*    */ import org.apache.tomcat.util.net.SSLHostConfigCertificate.Type;
/*    */ import org.xml.sax.Attributes;
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
/*    */ public class CertificateCreateRule
/*    */   extends Rule
/*    */ {
/*    */   public void begin(String namespace, String name, Attributes attributes)
/*    */     throws Exception
/*    */   {
/* 32 */     SSLHostConfig sslHostConfig = (SSLHostConfig)this.digester.peek();
/*    */     
/*    */ 
/* 35 */     String typeValue = attributes.getValue("type");
/* 36 */     SSLHostConfigCertificate.Type type; SSLHostConfigCertificate.Type type; if ((typeValue == null) || (typeValue.length() == 0)) {
/* 37 */       type = SSLHostConfigCertificate.Type.UNDEFINED;
/*    */     } else {
/* 39 */       type = SSLHostConfigCertificate.Type.valueOf(typeValue);
/*    */     }
/*    */     
/* 42 */     SSLHostConfigCertificate certificate = new SSLHostConfigCertificate(sslHostConfig, type);
/*    */     
/* 44 */     this.digester.push(certificate);
/*    */   }
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
/*    */   public void end(String namespace, String name)
/*    */     throws Exception
/*    */   {
/* 59 */     this.digester.pop();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\CertificateCreateRule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */