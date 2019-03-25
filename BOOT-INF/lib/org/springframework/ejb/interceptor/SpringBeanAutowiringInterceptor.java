/*     */ package org.springframework.ejb.interceptor;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import javax.annotation.PostConstruct;
/*     */ import javax.annotation.PreDestroy;
/*     */ import javax.ejb.EJBException;
/*     */ import javax.ejb.PostActivate;
/*     */ import javax.ejb.PrePassivate;
/*     */ import javax.interceptor.InvocationContext;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.access.BeanFactoryLocator;
/*     */ import org.springframework.beans.factory.access.BeanFactoryReference;
/*     */ import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.access.ContextSingletonBeanFactoryLocator;
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
/*     */ public class SpringBeanAutowiringInterceptor
/*     */ {
/*  84 */   private final Map<Object, BeanFactoryReference> beanFactoryReferences = new WeakHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @PostConstruct
/*     */   @PostActivate
/*     */   public void autowireBean(InvocationContext invocationContext)
/*     */   {
/*  95 */     doAutowireBean(invocationContext.getTarget());
/*     */     try {
/*  97 */       invocationContext.proceed();
/*     */     }
/*     */     catch (RuntimeException ex) {
/* 100 */       doReleaseBean(invocationContext.getTarget());
/* 101 */       throw ex;
/*     */     }
/*     */     catch (Error err) {
/* 104 */       doReleaseBean(invocationContext.getTarget());
/* 105 */       throw err;
/*     */     }
/*     */     catch (Exception ex) {
/* 108 */       doReleaseBean(invocationContext.getTarget());
/*     */       
/* 110 */       throw new EJBException(ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doAutowireBean(Object target)
/*     */   {
/* 119 */     AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
/* 120 */     configureBeanPostProcessor(bpp, target);
/* 121 */     bpp.setBeanFactory(getBeanFactory(target));
/* 122 */     bpp.processInjection(target);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void configureBeanPostProcessor(AutowiredAnnotationBeanPostProcessor processor, Object target) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BeanFactory getBeanFactory(Object target)
/*     */   {
/* 141 */     BeanFactory factory = getBeanFactoryReference(target).getFactory();
/* 142 */     if ((factory instanceof ApplicationContext)) {
/* 143 */       factory = ((ApplicationContext)factory).getAutowireCapableBeanFactory();
/*     */     }
/* 145 */     return factory;
/*     */   }
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
/*     */   protected BeanFactoryReference getBeanFactoryReference(Object target)
/*     */   {
/* 159 */     String key = getBeanFactoryLocatorKey(target);
/* 160 */     BeanFactoryReference ref = getBeanFactoryLocator(target).useBeanFactory(key);
/* 161 */     this.beanFactoryReferences.put(target, ref);
/* 162 */     return ref;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BeanFactoryLocator getBeanFactoryLocator(Object target)
/*     */   {
/* 174 */     return ContextSingletonBeanFactoryLocator.getInstance();
/*     */   }
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
/*     */   protected String getBeanFactoryLocatorKey(Object target)
/*     */   {
/* 189 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @PreDestroy
/*     */   @PrePassivate
/*     */   public void releaseBean(InvocationContext invocationContext)
/*     */   {
/* 200 */     doReleaseBean(invocationContext.getTarget());
/*     */     try {
/* 202 */       invocationContext.proceed();
/*     */     }
/*     */     catch (RuntimeException ex) {
/* 205 */       throw ex;
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 209 */       throw new EJBException(ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doReleaseBean(Object target)
/*     */   {
/* 218 */     BeanFactoryReference ref = (BeanFactoryReference)this.beanFactoryReferences.remove(target);
/* 219 */     if (ref != null) {
/* 220 */       ref.release();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\ejb\interceptor\SpringBeanAutowiringInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */