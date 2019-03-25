/*    */ package org.springframework.web.servlet.support;
/*    */ 
/*    */ import org.springframework.util.ObjectUtils;
/*    */ import org.springframework.web.context.WebApplicationContext;
/*    */ import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
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
/*    */ public abstract class AbstractAnnotationConfigDispatcherServletInitializer
/*    */   extends AbstractDispatcherServletInitializer
/*    */ {
/*    */   protected WebApplicationContext createRootApplicationContext()
/*    */   {
/* 53 */     Class<?>[] configClasses = getRootConfigClasses();
/* 54 */     if (!ObjectUtils.isEmpty(configClasses)) {
/* 55 */       AnnotationConfigWebApplicationContext rootAppContext = new AnnotationConfigWebApplicationContext();
/* 56 */       rootAppContext.register(configClasses);
/* 57 */       return rootAppContext;
/*    */     }
/*    */     
/* 60 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected WebApplicationContext createServletApplicationContext()
/*    */   {
/* 71 */     AnnotationConfigWebApplicationContext servletAppContext = new AnnotationConfigWebApplicationContext();
/* 72 */     Class<?>[] configClasses = getServletConfigClasses();
/* 73 */     if (!ObjectUtils.isEmpty(configClasses)) {
/* 74 */       servletAppContext.register(configClasses);
/*    */     }
/* 76 */     return servletAppContext;
/*    */   }
/*    */   
/*    */   protected abstract Class<?>[] getRootConfigClasses();
/*    */   
/*    */   protected abstract Class<?>[] getServletConfigClasses();
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\support\AbstractAnnotationConfigDispatcherServletInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */