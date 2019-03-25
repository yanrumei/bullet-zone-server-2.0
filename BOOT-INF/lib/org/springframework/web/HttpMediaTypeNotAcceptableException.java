/*    */ package org.springframework.web;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.springframework.http.MediaType;
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
/*    */ public class HttpMediaTypeNotAcceptableException
/*    */   extends HttpMediaTypeException
/*    */ {
/*    */   public HttpMediaTypeNotAcceptableException(String message)
/*    */   {
/* 37 */     super(message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public HttpMediaTypeNotAcceptableException(List<MediaType> supportedMediaTypes)
/*    */   {
/* 45 */     super("Could not find acceptable representation", supportedMediaTypes);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\HttpMediaTypeNotAcceptableException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */