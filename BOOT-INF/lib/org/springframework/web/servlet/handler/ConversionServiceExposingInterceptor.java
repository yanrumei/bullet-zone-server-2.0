/*    */ package org.springframework.web.servlet.handler;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.core.convert.ConversionService;
/*    */ import org.springframework.util.Assert;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConversionServiceExposingInterceptor
/*    */   extends HandlerInterceptorAdapter
/*    */ {
/*    */   private final ConversionService conversionService;
/*    */   
/*    */   public ConversionServiceExposingInterceptor(ConversionService conversionService)
/*    */   {
/* 48 */     Assert.notNull(conversionService, "The ConversionService may not be null");
/* 49 */     this.conversionService = conversionService;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
/*    */     throws ServletException, IOException
/*    */   {
/* 57 */     request.setAttribute(ConversionService.class.getName(), this.conversionService);
/* 58 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\handler\ConversionServiceExposingInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */