/*     */ package org.springframework.boot.web.servlet;
/*     */ 
/*     */ import javax.servlet.Filter;
/*     */ import javax.servlet.ServletException;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.context.WebApplicationContext;
/*     */ import org.springframework.web.filter.DelegatingFilterProxy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DelegatingFilterProxyRegistrationBean
/*     */   extends AbstractFilterRegistrationBean
/*     */   implements ApplicationContextAware
/*     */ {
/*     */   private ApplicationContext applicationContext;
/*     */   private final String targetBeanName;
/*     */   
/*     */   public DelegatingFilterProxyRegistrationBean(String targetBeanName, ServletRegistrationBean... servletRegistrationBeans)
/*     */   {
/*  70 */     super(servletRegistrationBeans);
/*  71 */     Assert.hasLength(targetBeanName, "TargetBeanName must not be null or empty");
/*  72 */     this.targetBeanName = targetBeanName;
/*  73 */     setName(targetBeanName);
/*     */   }
/*     */   
/*     */   public void setApplicationContext(ApplicationContext applicationContext)
/*     */     throws BeansException
/*     */   {
/*  79 */     this.applicationContext = applicationContext;
/*     */   }
/*     */   
/*     */   protected String getTargetBeanName() {
/*  83 */     return this.targetBeanName;
/*     */   }
/*     */   
/*     */   public Filter getFilter()
/*     */   {
/*  88 */     new DelegatingFilterProxy(this.targetBeanName, 
/*  89 */       getWebApplicationContext())
/*     */       {
/*     */         protected void initFilterBean()
/*     */           throws ServletException
/*     */         {}
/*     */       };
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private WebApplicationContext getWebApplicationContext()
/*     */     {
/* 100 */       Assert.notNull(this.applicationContext, "ApplicationContext be injected");
/* 101 */       Assert.isInstanceOf(WebApplicationContext.class, this.applicationContext);
/* 102 */       return (WebApplicationContext)this.applicationContext;
/*     */     }
/*     */   }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\web\servlet\DelegatingFilterProxyRegistrationBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */