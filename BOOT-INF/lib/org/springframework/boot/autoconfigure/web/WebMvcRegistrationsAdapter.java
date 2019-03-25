/*    */ package org.springframework.boot.autoconfigure.web;
/*    */ 
/*    */ import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
/*    */ import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
/*    */ import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
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
/*    */ public class WebMvcRegistrationsAdapter
/*    */   implements WebMvcRegistrations
/*    */ {
/*    */   public RequestMappingHandlerMapping getRequestMappingHandlerMapping()
/*    */   {
/* 34 */     return null;
/*    */   }
/*    */   
/*    */   public RequestMappingHandlerAdapter getRequestMappingHandlerAdapter()
/*    */   {
/* 39 */     return null;
/*    */   }
/*    */   
/*    */   public ExceptionHandlerExceptionResolver getExceptionHandlerExceptionResolver()
/*    */   {
/* 44 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\web\WebMvcRegistrationsAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */