/*    */ package org.apache.tomcat.util.net.openssl;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public class OpenSSLConfCmd
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 25 */   private String name = null;
/* 26 */   private String value = null;
/*    */   
/*    */   public String getName() {
/* 29 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 33 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 37 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(String value) {
/* 41 */     this.value = value;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\openssl\OpenSSLConfCmd.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */