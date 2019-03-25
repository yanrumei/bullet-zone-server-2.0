/*    */ package org.springframework.http;
/*    */ 
/*    */ import org.springframework.util.InvalidMimeTypeException;
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
/*    */ 
/*    */ 
/*    */ public class InvalidMediaTypeException
/*    */   extends IllegalArgumentException
/*    */ {
/*    */   private String mediaType;
/*    */   
/*    */   public InvalidMediaTypeException(String mediaType, String message)
/*    */   {
/* 40 */     super("Invalid media type \"" + mediaType + "\": " + message);
/* 41 */     this.mediaType = mediaType;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   InvalidMediaTypeException(InvalidMimeTypeException ex)
/*    */   {
/* 48 */     super(ex.getMessage(), ex);
/* 49 */     this.mediaType = ex.getMimeType();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getMediaType()
/*    */   {
/* 57 */     return this.mediaType;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\InvalidMediaTypeException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */