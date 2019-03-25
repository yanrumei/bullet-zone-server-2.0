/*    */ package org.springframework.web.servlet.mvc.method.annotation;
/*    */ 
/*    */ import org.springframework.web.bind.support.WebArgumentResolver;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
/*    */ import org.springframework.web.context.request.RequestAttributes;
/*    */ import org.springframework.web.context.request.RequestContextHolder;
/*    */ import org.springframework.web.context.request.ServletRequestAttributes;
/*    */ import org.springframework.web.context.request.ServletWebRequest;
/*    */ import org.springframework.web.method.annotation.AbstractWebArgumentResolverAdapter;
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
/*    */ public class ServletWebArgumentResolverAdapter
/*    */   extends AbstractWebArgumentResolverAdapter
/*    */ {
/*    */   public ServletWebArgumentResolverAdapter(WebArgumentResolver adaptee)
/*    */   {
/* 42 */     super(adaptee);
/*    */   }
/*    */   
/*    */   protected NativeWebRequest getWebRequest()
/*    */   {
/* 47 */     RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
/* 48 */     if ((requestAttributes instanceof ServletRequestAttributes)) {
/* 49 */       ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)requestAttributes;
/* 50 */       return new ServletWebRequest(servletRequestAttributes.getRequest());
/*    */     }
/* 52 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\ServletWebArgumentResolverAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */