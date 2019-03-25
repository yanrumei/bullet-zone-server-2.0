/*     */ package org.springframework.web.servlet.handler;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import javax.servlet.Servlet;
/*     */ import javax.servlet.ServletConfig;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanInitializationException;
/*     */ import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
/*     */ import org.springframework.web.context.ServletConfigAware;
/*     */ import org.springframework.web.context.ServletContextAware;
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
/*     */ 
/*     */ 
/*     */ public class SimpleServletPostProcessor
/*     */   implements DestructionAwareBeanPostProcessor, ServletContextAware, ServletConfigAware
/*     */ {
/*  69 */   private boolean useSharedServletConfig = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private ServletContext servletContext;
/*     */   
/*     */ 
/*     */ 
/*     */   private ServletConfig servletConfig;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUseSharedServletConfig(boolean useSharedServletConfig)
/*     */   {
/*  85 */     this.useSharedServletConfig = useSharedServletConfig;
/*     */   }
/*     */   
/*     */   public void setServletContext(ServletContext servletContext)
/*     */   {
/*  90 */     this.servletContext = servletContext;
/*     */   }
/*     */   
/*     */   public void setServletConfig(ServletConfig servletConfig)
/*     */   {
/*  95 */     this.servletConfig = servletConfig;
/*     */   }
/*     */   
/*     */   public Object postProcessBeforeInitialization(Object bean, String beanName)
/*     */     throws BeansException
/*     */   {
/* 101 */     return bean;
/*     */   }
/*     */   
/*     */   public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException
/*     */   {
/* 106 */     if ((bean instanceof Servlet)) {
/* 107 */       ServletConfig config = this.servletConfig;
/* 108 */       if ((config == null) || (!this.useSharedServletConfig)) {
/* 109 */         config = new DelegatingServletConfig(beanName, this.servletContext);
/*     */       }
/*     */       try {
/* 112 */         ((Servlet)bean).init(config);
/*     */       }
/*     */       catch (ServletException ex) {
/* 115 */         throw new BeanInitializationException("Servlet.init threw exception", ex);
/*     */       }
/*     */     }
/* 118 */     return bean;
/*     */   }
/*     */   
/*     */   public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException
/*     */   {
/* 123 */     if ((bean instanceof Servlet)) {
/* 124 */       ((Servlet)bean).destroy();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean requiresDestruction(Object bean)
/*     */   {
/* 130 */     return bean instanceof Servlet;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class DelegatingServletConfig
/*     */     implements ServletConfig
/*     */   {
/*     */     private final String servletName;
/*     */     
/*     */     private final ServletContext servletContext;
/*     */     
/*     */ 
/*     */     public DelegatingServletConfig(String servletName, ServletContext servletContext)
/*     */     {
/* 145 */       this.servletName = servletName;
/* 146 */       this.servletContext = servletContext;
/*     */     }
/*     */     
/*     */     public String getServletName()
/*     */     {
/* 151 */       return this.servletName;
/*     */     }
/*     */     
/*     */     public ServletContext getServletContext()
/*     */     {
/* 156 */       return this.servletContext;
/*     */     }
/*     */     
/*     */     public String getInitParameter(String paramName)
/*     */     {
/* 161 */       return null;
/*     */     }
/*     */     
/*     */     public Enumeration<String> getInitParameterNames()
/*     */     {
/* 166 */       return Collections.enumeration(Collections.emptySet());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\handler\SimpleServletPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */