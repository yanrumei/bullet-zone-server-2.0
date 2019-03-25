/*    */ package org.springframework.web.servlet.handler;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.web.context.request.ServletWebRequest;
/*    */ import org.springframework.web.servlet.support.RequestContextUtils;
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
/*    */ public class DispatcherServletWebRequest
/*    */   extends ServletWebRequest
/*    */ {
/*    */   public DispatcherServletWebRequest(HttpServletRequest request)
/*    */   {
/* 44 */     super(request);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public DispatcherServletWebRequest(HttpServletRequest request, HttpServletResponse response)
/*    */   {
/* 53 */     super(request, response);
/*    */   }
/*    */   
/*    */   public Locale getLocale()
/*    */   {
/* 58 */     return RequestContextUtils.getLocale(getRequest());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\handler\DispatcherServletWebRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */