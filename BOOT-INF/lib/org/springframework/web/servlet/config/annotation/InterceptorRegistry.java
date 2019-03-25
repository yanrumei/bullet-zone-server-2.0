/*    */ package org.springframework.web.servlet.config.annotation;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.web.context.request.WebRequestInterceptor;
/*    */ import org.springframework.web.servlet.HandlerInterceptor;
/*    */ import org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter;
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
/*    */ public class InterceptorRegistry
/*    */ {
/* 35 */   private final List<InterceptorRegistration> registrations = new ArrayList();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public InterceptorRegistration addInterceptor(HandlerInterceptor interceptor)
/*    */   {
/* 45 */     InterceptorRegistration registration = new InterceptorRegistration(interceptor);
/* 46 */     this.registrations.add(registration);
/* 47 */     return registration;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public InterceptorRegistration addWebRequestInterceptor(WebRequestInterceptor interceptor)
/*    */   {
/* 57 */     WebRequestHandlerInterceptorAdapter adapted = new WebRequestHandlerInterceptorAdapter(interceptor);
/* 58 */     InterceptorRegistration registration = new InterceptorRegistration(adapted);
/* 59 */     this.registrations.add(registration);
/* 60 */     return registration;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected List<Object> getInterceptors()
/*    */   {
/* 67 */     List<Object> interceptors = new ArrayList(this.registrations.size());
/* 68 */     for (InterceptorRegistration registration : this.registrations) {
/* 69 */       interceptors.add(registration.getInterceptor());
/*    */     }
/* 71 */     return interceptors;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\annotation\InterceptorRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */