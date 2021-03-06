/*    */ package org.springframework.web;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import javax.servlet.ServletException;
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
/*    */ 
/*    */ public abstract class HttpMediaTypeException
/*    */   extends ServletException
/*    */ {
/*    */   private final List<MediaType> supportedMediaTypes;
/*    */   
/*    */   protected HttpMediaTypeException(String message)
/*    */   {
/* 42 */     super(message);
/* 43 */     this.supportedMediaTypes = Collections.emptyList();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected HttpMediaTypeException(String message, List<MediaType> supportedMediaTypes)
/*    */   {
/* 51 */     super(message);
/* 52 */     this.supportedMediaTypes = Collections.unmodifiableList(supportedMediaTypes);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public List<MediaType> getSupportedMediaTypes()
/*    */   {
/* 60 */     return this.supportedMediaTypes;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\HttpMediaTypeException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */