/*    */ package org.springframework.web.servlet.view.tiles3;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.apache.tiles.locale.impl.DefaultLocaleResolver;
/*    */ import org.apache.tiles.request.Request;
/*    */ import org.apache.tiles.request.servlet.NotAServletEnvironmentException;
/*    */ import org.apache.tiles.request.servlet.ServletRequest;
/*    */ import org.apache.tiles.request.servlet.ServletUtil;
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
/*    */ public class SpringLocaleResolver
/*    */   extends DefaultLocaleResolver
/*    */ {
/*    */   public Locale resolveLocale(Request request)
/*    */   {
/*    */     try
/*    */     {
/* 45 */       HttpServletRequest servletRequest = ServletUtil.getServletRequest(request).getRequest();
/* 46 */       if (servletRequest != null) {
/* 47 */         return RequestContextUtils.getLocale(servletRequest);
/*    */       }
/*    */     }
/*    */     catch (NotAServletEnvironmentException localNotAServletEnvironmentException) {}
/*    */     
/*    */ 
/* 53 */     return super.resolveLocale(request);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\tiles3\SpringLocaleResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */