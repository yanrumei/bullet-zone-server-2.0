/*    */ package javax.servlet;
/*    */ 
/*    */ import javax.servlet.annotation.MultipartConfig;
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
/*    */ public class MultipartConfigElement
/*    */ {
/*    */   private final String location;
/*    */   private final long maxFileSize;
/*    */   private final long maxRequestSize;
/*    */   private final int fileSizeThreshold;
/*    */   
/*    */   public MultipartConfigElement(String location)
/*    */   {
/* 34 */     if (location != null) {
/* 35 */       this.location = location;
/*    */     } else {
/* 37 */       this.location = "";
/*    */     }
/* 39 */     this.maxFileSize = -1L;
/* 40 */     this.maxRequestSize = -1L;
/* 41 */     this.fileSizeThreshold = 0;
/*    */   }
/*    */   
/*    */ 
/*    */   public MultipartConfigElement(String location, long maxFileSize, long maxRequestSize, int fileSizeThreshold)
/*    */   {
/* 47 */     if (location != null) {
/* 48 */       this.location = location;
/*    */     } else {
/* 50 */       this.location = "";
/*    */     }
/* 52 */     this.maxFileSize = maxFileSize;
/* 53 */     this.maxRequestSize = maxRequestSize;
/*    */     
/*    */ 
/* 56 */     if (fileSizeThreshold > 0) {
/* 57 */       this.fileSizeThreshold = fileSizeThreshold;
/*    */     } else {
/* 59 */       this.fileSizeThreshold = 0;
/*    */     }
/*    */   }
/*    */   
/*    */   public MultipartConfigElement(MultipartConfig annotation) {
/* 64 */     this.location = annotation.location();
/* 65 */     this.maxFileSize = annotation.maxFileSize();
/* 66 */     this.maxRequestSize = annotation.maxRequestSize();
/* 67 */     this.fileSizeThreshold = annotation.fileSizeThreshold();
/*    */   }
/*    */   
/*    */   public String getLocation() {
/* 71 */     return this.location;
/*    */   }
/*    */   
/*    */   public long getMaxFileSize() {
/* 75 */     return this.maxFileSize;
/*    */   }
/*    */   
/*    */   public long getMaxRequestSize() {
/* 79 */     return this.maxRequestSize;
/*    */   }
/*    */   
/*    */   public int getFileSizeThreshold() {
/* 83 */     return this.fileSizeThreshold;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\MultipartConfigElement.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */