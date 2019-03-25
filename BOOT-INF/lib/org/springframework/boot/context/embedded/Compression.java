/*    */ package org.springframework.boot.context.embedded;
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
/*    */ 
/*    */ public class Compression
/*    */ {
/* 31 */   private boolean enabled = false;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 36 */   private String[] mimeTypes = { "text/html", "text/xml", "text/plain", "text/css", "text/javascript", "application/javascript" };
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 42 */   private String[] excludedUserAgents = null;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 47 */   private int minResponseSize = 2048;
/*    */   
/*    */   public boolean getEnabled() {
/* 50 */     return this.enabled;
/*    */   }
/*    */   
/*    */   public void setEnabled(boolean enabled) {
/* 54 */     this.enabled = enabled;
/*    */   }
/*    */   
/*    */   public String[] getMimeTypes() {
/* 58 */     return this.mimeTypes;
/*    */   }
/*    */   
/*    */   public void setMimeTypes(String[] mimeTypes) {
/* 62 */     this.mimeTypes = mimeTypes;
/*    */   }
/*    */   
/*    */   public int getMinResponseSize() {
/* 66 */     return this.minResponseSize;
/*    */   }
/*    */   
/*    */   public void setMinResponseSize(int minSize) {
/* 70 */     this.minResponseSize = minSize;
/*    */   }
/*    */   
/*    */   public String[] getExcludedUserAgents() {
/* 74 */     return this.excludedUserAgents;
/*    */   }
/*    */   
/*    */   public void setExcludedUserAgents(String[] excludedUserAgents) {
/* 78 */     this.excludedUserAgents = excludedUserAgents;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\Compression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */