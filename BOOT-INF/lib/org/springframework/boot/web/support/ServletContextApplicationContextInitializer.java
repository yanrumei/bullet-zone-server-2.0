/*    */ package org.springframework.boot.web.support;
/*    */ 
/*    */ import javax.servlet.ServletContext;
/*    */ import org.springframework.context.ApplicationContextInitializer;
/*    */ import org.springframework.core.Ordered;
/*    */ import org.springframework.web.context.ConfigurableWebApplicationContext;
/*    */ import org.springframework.web.context.WebApplicationContext;
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
/*    */ public class ServletContextApplicationContextInitializer
/*    */   implements ApplicationContextInitializer<ConfigurableWebApplicationContext>, Ordered
/*    */ {
/* 37 */   private int order = Integer.MIN_VALUE;
/*    */   
/*    */ 
/*    */   private final ServletContext servletContext;
/*    */   
/*    */ 
/*    */   private final boolean addApplicationContextAttribute;
/*    */   
/*    */ 
/*    */   public ServletContextApplicationContextInitializer(ServletContext servletContext)
/*    */   {
/* 48 */     this(servletContext, false);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ServletContextApplicationContextInitializer(ServletContext servletContext, boolean addApplicationContextAttribute)
/*    */   {
/* 60 */     this.servletContext = servletContext;
/* 61 */     this.addApplicationContextAttribute = addApplicationContextAttribute;
/*    */   }
/*    */   
/*    */   public void setOrder(int order) {
/* 65 */     this.order = order;
/*    */   }
/*    */   
/*    */   public int getOrder()
/*    */   {
/* 70 */     return this.order;
/*    */   }
/*    */   
/*    */   public void initialize(ConfigurableWebApplicationContext applicationContext)
/*    */   {
/* 75 */     applicationContext.setServletContext(this.servletContext);
/* 76 */     if (this.addApplicationContextAttribute) {
/* 77 */       this.servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\web\support\ServletContextApplicationContextInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */