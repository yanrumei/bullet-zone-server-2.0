/*     */ package org.springframework.web.servlet.view;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.web.context.ServletContextAware;
/*     */ import org.springframework.web.servlet.View;
/*     */ import org.springframework.web.servlet.ViewResolver;
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
/*     */ public class ViewResolverComposite
/*     */   implements ViewResolver, Ordered, InitializingBean, ApplicationContextAware, ServletContextAware
/*     */ {
/*  45 */   private final List<ViewResolver> viewResolvers = new ArrayList();
/*     */   
/*  47 */   private int order = Integer.MAX_VALUE;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setViewResolvers(List<ViewResolver> viewResolvers)
/*     */   {
/*  54 */     this.viewResolvers.clear();
/*  55 */     if (!CollectionUtils.isEmpty(viewResolvers)) {
/*  56 */       this.viewResolvers.addAll(viewResolvers);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<ViewResolver> getViewResolvers()
/*     */   {
/*  64 */     return Collections.unmodifiableList(this.viewResolvers);
/*     */   }
/*     */   
/*     */   public void setOrder(int order) {
/*  68 */     this.order = order;
/*     */   }
/*     */   
/*     */   public int getOrder()
/*     */   {
/*  73 */     return this.order;
/*     */   }
/*     */   
/*     */   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
/*     */   {
/*  78 */     for (ViewResolver viewResolver : this.viewResolvers) {
/*  79 */       if ((viewResolver instanceof ApplicationContextAware)) {
/*  80 */         ((ApplicationContextAware)viewResolver).setApplicationContext(applicationContext);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setServletContext(ServletContext servletContext)
/*     */   {
/*  87 */     for (ViewResolver viewResolver : this.viewResolvers) {
/*  88 */       if ((viewResolver instanceof ServletContextAware)) {
/*  89 */         ((ServletContextAware)viewResolver).setServletContext(servletContext);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void afterPropertiesSet() throws Exception
/*     */   {
/*  96 */     for (ViewResolver viewResolver : this.viewResolvers) {
/*  97 */       if ((viewResolver instanceof InitializingBean)) {
/*  98 */         ((InitializingBean)viewResolver).afterPropertiesSet();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public View resolveViewName(String viewName, Locale locale) throws Exception
/*     */   {
/* 105 */     for (ViewResolver viewResolver : this.viewResolvers) {
/* 106 */       View view = viewResolver.resolveViewName(viewName, locale);
/* 107 */       if (view != null) {
/* 108 */         return view;
/*     */       }
/*     */     }
/* 111 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\ViewResolverComposite.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */