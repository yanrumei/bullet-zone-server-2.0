/*    */ package org.springframework.web.servlet.mvc.method.annotation;
/*    */ 
/*    */ import javax.servlet.http.Cookie;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
/*    */ import org.springframework.web.method.annotation.AbstractCookieValueMethodArgumentResolver;
/*    */ import org.springframework.web.util.UrlPathHelper;
/*    */ import org.springframework.web.util.WebUtils;
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
/*    */ public class ServletCookieValueMethodArgumentResolver
/*    */   extends AbstractCookieValueMethodArgumentResolver
/*    */ {
/* 38 */   private UrlPathHelper urlPathHelper = new UrlPathHelper();
/*    */   
/*    */   public ServletCookieValueMethodArgumentResolver(ConfigurableBeanFactory beanFactory)
/*    */   {
/* 42 */     super(beanFactory);
/*    */   }
/*    */   
/*    */   public void setUrlPathHelper(UrlPathHelper urlPathHelper)
/*    */   {
/* 47 */     this.urlPathHelper = urlPathHelper;
/*    */   }
/*    */   
/*    */   protected Object resolveName(String cookieName, MethodParameter parameter, NativeWebRequest webRequest)
/*    */     throws Exception
/*    */   {
/* 53 */     HttpServletRequest servletRequest = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/* 54 */     Cookie cookieValue = WebUtils.getCookie(servletRequest, cookieName);
/* 55 */     if (Cookie.class.isAssignableFrom(parameter.getNestedParameterType())) {
/* 56 */       return cookieValue;
/*    */     }
/* 58 */     if (cookieValue != null) {
/* 59 */       return this.urlPathHelper.decodeRequestString(servletRequest, cookieValue.getValue());
/*    */     }
/*    */     
/* 62 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\ServletCookieValueMethodArgumentResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */