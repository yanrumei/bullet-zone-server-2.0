/*    */ package org.apache.tomcat.util.net.openssl;
/*    */ 
/*    */ import java.io.File;
/*    */ import javax.net.ssl.KeyManager;
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
/*    */ public class OpenSSLKeyManager
/*    */   implements KeyManager
/*    */ {
/*    */   private File certificateChain;
/*    */   private File privateKey;
/*    */   
/* 26 */   public File getCertificateChain() { return this.certificateChain; }
/* 27 */   public void setCertificateChain(File certificateChain) { this.certificateChain = certificateChain; }
/*    */   
/*    */ 
/* 30 */   public File getPrivateKey() { return this.privateKey; }
/* 31 */   public void setPrivateKey(File privateKey) { this.privateKey = privateKey; }
/*    */   
/*    */   OpenSSLKeyManager(String certChainFile, String keyFile) {
/* 34 */     if (certChainFile == null) {
/* 35 */       return;
/*    */     }
/* 37 */     if (keyFile == null) {
/* 38 */       return;
/*    */     }
/* 40 */     this.certificateChain = new File(certChainFile);
/* 41 */     this.privateKey = new File(keyFile);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\openssl\OpenSSLKeyManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */