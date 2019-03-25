/*    */ package org.springframework.web.multipart;
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
/*    */ public class MaxUploadSizeExceededException
/*    */   extends MultipartException
/*    */ {
/*    */   private final long maxUploadSize;
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
/*    */   public MaxUploadSizeExceededException(long maxUploadSize)
/*    */   {
/* 37 */     this(maxUploadSize, null);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public MaxUploadSizeExceededException(long maxUploadSize, Throwable ex)
/*    */   {
/* 46 */     super("Maximum upload size of " + maxUploadSize + " bytes exceeded", ex);
/* 47 */     this.maxUploadSize = maxUploadSize;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public long getMaxUploadSize()
/*    */   {
/* 55 */     return this.maxUploadSize;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\multipart\MaxUploadSizeExceededException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */