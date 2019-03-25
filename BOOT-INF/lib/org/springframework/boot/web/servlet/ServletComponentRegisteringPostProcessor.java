/*     */ package org.springframework.boot.web.servlet;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
/*     */ import org.springframework.context.annotation.ScannedGenericBeanDefinition;
/*     */ import org.springframework.web.context.WebApplicationContext;
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
/*     */ class ServletComponentRegisteringPostProcessor
/*     */   implements BeanFactoryPostProcessor, ApplicationContextAware
/*     */ {
/*     */   private static final List<ServletComponentHandler> HANDLERS;
/*     */   private final Set<String> packagesToScan;
/*     */   private ApplicationContext applicationContext;
/*     */   
/*     */   static
/*     */   {
/*  49 */     List<ServletComponentHandler> servletComponentHandlers = new ArrayList();
/*  50 */     servletComponentHandlers.add(new WebServletHandler());
/*  51 */     servletComponentHandlers.add(new WebFilterHandler());
/*  52 */     servletComponentHandlers.add(new WebListenerHandler());
/*  53 */     HANDLERS = Collections.unmodifiableList(servletComponentHandlers);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   ServletComponentRegisteringPostProcessor(Set<String> packagesToScan)
/*     */   {
/*  61 */     this.packagesToScan = packagesToScan;
/*     */   }
/*     */   
/*     */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException
/*     */   {
/*     */     ClassPathScanningCandidateComponentProvider componentProvider;
/*  67 */     if (isRunningInEmbeddedContainer()) {
/*  68 */       componentProvider = createComponentProvider();
/*  69 */       for (String packageToScan : this.packagesToScan) {
/*  70 */         scanPackage(componentProvider, packageToScan);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void scanPackage(ClassPathScanningCandidateComponentProvider componentProvider, String packageToScan)
/*     */   {
/*  78 */     for (Iterator localIterator1 = componentProvider
/*  79 */           .findCandidateComponents(packageToScan).iterator(); localIterator1.hasNext();) { candidate = (BeanDefinition)localIterator1.next();
/*     */       
/*  80 */       if ((candidate instanceof ScannedGenericBeanDefinition)) {
/*  81 */         for (ServletComponentHandler handler : HANDLERS) {
/*  82 */           handler.handle((ScannedGenericBeanDefinition)candidate, (BeanDefinitionRegistry)this.applicationContext);
/*     */         }
/*     */       }
/*     */     }
/*     */     BeanDefinition candidate;
/*     */   }
/*     */   
/*     */   private boolean isRunningInEmbeddedContainer() {
/*  90 */     if ((this.applicationContext instanceof WebApplicationContext)) {} return 
/*     */     
/*  92 */       ((WebApplicationContext)this.applicationContext).getServletContext() == null;
/*     */   }
/*     */   
/*     */   private ClassPathScanningCandidateComponentProvider createComponentProvider() {
/*  96 */     ClassPathScanningCandidateComponentProvider componentProvider = new ClassPathScanningCandidateComponentProvider(false);
/*     */     
/*  98 */     componentProvider.setEnvironment(this.applicationContext.getEnvironment());
/*  99 */     componentProvider.setResourceLoader(this.applicationContext);
/* 100 */     for (ServletComponentHandler handler : HANDLERS) {
/* 101 */       componentProvider.addIncludeFilter(handler.getTypeFilter());
/*     */     }
/* 103 */     return componentProvider;
/*     */   }
/*     */   
/*     */   Set<String> getPackagesToScan() {
/* 107 */     return Collections.unmodifiableSet(this.packagesToScan);
/*     */   }
/*     */   
/*     */   public void setApplicationContext(ApplicationContext applicationContext)
/*     */     throws BeansException
/*     */   {
/* 113 */     this.applicationContext = applicationContext;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\web\servlet\ServletComponentRegisteringPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */