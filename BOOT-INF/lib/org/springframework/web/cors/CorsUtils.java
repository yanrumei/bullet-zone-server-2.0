/*    */ package org.springframework.web.cors;
/*    */ 
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.springframework.http.HttpMethod;
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
/*    */ public abstract class CorsUtils
/*    */ {
/*    */   public static boolean isCorsRequest(HttpServletRequest request)
/*    */   {
/* 37 */     return request.getHeader("Origin") != null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static boolean isPreFlightRequest(HttpServletRequest request)
/*    */   {
/* 44 */     return (isCorsRequest(request)) && (HttpMethod.OPTIONS.matches(request.getMethod())) && 
/* 45 */       (request.getHeader("Access-Control-Request-Method") != null);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\cors\CorsUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */