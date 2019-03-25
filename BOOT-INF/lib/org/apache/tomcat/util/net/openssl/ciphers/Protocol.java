/*    */ package org.apache.tomcat.util.net.openssl.ciphers;
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
/*    */  enum Protocol
/*    */ {
/* 24 */   SSLv3("SSLv3"), 
/* 25 */   SSLv2("SSLv2"), 
/* 26 */   TLSv1("TLSv1"), 
/* 27 */   TLSv1_2("TLSv1.2");
/*    */   
/*    */   private final String openSSLName;
/*    */   
/*    */   private Protocol(String openSSLName) {
/* 32 */     this.openSSLName = openSSLName;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   String getOpenSSLName()
/*    */   {
/* 41 */     return this.openSSLName;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\openssl\ciphers\Protocol.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */