/*     */ package org.springframework.web.servlet.view;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.xml.ResourceEntityResolver;
/*     */ import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.web.context.support.GenericWebApplicationContext;
/*     */ import org.springframework.web.servlet.View;
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
/*     */ public class XmlViewResolver
/*     */   extends AbstractCachingViewResolver
/*     */   implements Ordered, InitializingBean, DisposableBean
/*     */ {
/*     */   public static final String DEFAULT_LOCATION = "/WEB-INF/views.xml";
/*  63 */   private int order = Integer.MAX_VALUE;
/*     */   
/*     */   private Resource location;
/*     */   
/*     */   private ConfigurableApplicationContext cachedFactory;
/*     */   
/*     */   public void setOrder(int order)
/*     */   {
/*  71 */     this.order = order;
/*     */   }
/*     */   
/*     */   public int getOrder()
/*     */   {
/*  76 */     return this.order;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLocation(Resource location)
/*     */   {
/*  85 */     this.location = location;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void afterPropertiesSet()
/*     */     throws BeansException
/*     */   {
/*  94 */     if (isCache()) {
/*  95 */       initFactory();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object getCacheKey(String viewName, Locale locale)
/*     */   {
/* 106 */     return viewName;
/*     */   }
/*     */   
/*     */   protected View loadView(String viewName, Locale locale) throws BeansException
/*     */   {
/* 111 */     BeanFactory factory = initFactory();
/*     */     try {
/* 113 */       return (View)factory.getBean(viewName, View.class);
/*     */     }
/*     */     catch (NoSuchBeanDefinitionException ex) {}
/*     */     
/* 117 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected synchronized BeanFactory initFactory()
/*     */     throws BeansException
/*     */   {
/* 127 */     if (this.cachedFactory != null) {
/* 128 */       return this.cachedFactory;
/*     */     }
/*     */     
/* 131 */     Resource actualLocation = this.location;
/* 132 */     if (actualLocation == null) {
/* 133 */       actualLocation = getApplicationContext().getResource("/WEB-INF/views.xml");
/*     */     }
/*     */     
/*     */ 
/* 137 */     GenericWebApplicationContext factory = new GenericWebApplicationContext();
/* 138 */     factory.setParent(getApplicationContext());
/* 139 */     factory.setServletContext(getServletContext());
/*     */     
/*     */ 
/* 142 */     XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
/* 143 */     reader.setEnvironment(getApplicationContext().getEnvironment());
/* 144 */     reader.setEntityResolver(new ResourceEntityResolver(getApplicationContext()));
/* 145 */     reader.loadBeanDefinitions(actualLocation);
/*     */     
/* 147 */     factory.refresh();
/*     */     
/* 149 */     if (isCache()) {
/* 150 */       this.cachedFactory = factory;
/*     */     }
/* 152 */     return factory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void destroy()
/*     */     throws BeansException
/*     */   {
/* 161 */     if (this.cachedFactory != null) {
/* 162 */       this.cachedFactory.close();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\XmlViewResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */