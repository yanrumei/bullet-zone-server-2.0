/*    */ package org.apache.tomcat.util.net.openssl;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
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
/*    */ public class OpenSSLProtocols
/*    */ {
/* 30 */   private List<String> openSSLProtocols = new ArrayList();
/*    */   
/*    */   public OpenSSLProtocols(String preferredJSSEProto) {
/* 33 */     Collections.addAll(this.openSSLProtocols, new String[] { "TLSv1.2", "TLSv1.1", "TLSv1", "SSLv3", "SSLv2" });
/*    */     
/*    */ 
/* 36 */     if (this.openSSLProtocols.contains(preferredJSSEProto)) {
/* 37 */       this.openSSLProtocols.remove(preferredJSSEProto);
/* 38 */       this.openSSLProtocols.add(0, preferredJSSEProto);
/*    */     }
/*    */   }
/*    */   
/*    */   public String[] getProtocols() {
/* 43 */     return (String[])this.openSSLProtocols.toArray(new String[this.openSSLProtocols.size()]);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\openssl\OpenSSLProtocols.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */