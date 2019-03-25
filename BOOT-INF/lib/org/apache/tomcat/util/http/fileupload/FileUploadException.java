/*    */ package org.apache.tomcat.util.http.fileupload;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FileUploadException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = -4222909057964038517L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public FileUploadException() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public FileUploadException(String message, Throwable cause)
/*    */   {
/* 31 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public FileUploadException(String message) {
/* 35 */     super(message);
/*    */   }
/*    */   
/*    */   public FileUploadException(Throwable cause) {
/* 39 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\fileupload\FileUploadException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */