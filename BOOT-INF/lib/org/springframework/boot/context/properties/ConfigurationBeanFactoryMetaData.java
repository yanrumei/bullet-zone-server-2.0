/*     */ package org.springframework.boot.context.properties;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.ReflectionUtils.MethodCallback;
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
/*     */ public class ConfigurationBeanFactoryMetaData
/*     */   implements BeanFactoryPostProcessor
/*     */ {
/*     */   private ConfigurableListableBeanFactory beanFactory;
/*  44 */   private Map<String, MetaData> beans = new HashMap();
/*     */   
/*     */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
/*     */     throws BeansException
/*     */   {
/*  49 */     this.beanFactory = beanFactory;
/*  50 */     for (String name : beanFactory.getBeanDefinitionNames()) {
/*  51 */       BeanDefinition definition = beanFactory.getBeanDefinition(name);
/*  52 */       String method = definition.getFactoryMethodName();
/*  53 */       String bean = definition.getFactoryBeanName();
/*  54 */       if ((method != null) && (bean != null)) {
/*  55 */         this.beans.put(name, new MetaData(bean, method));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public <A extends Annotation> Map<String, Object> getBeansWithFactoryAnnotation(Class<A> type)
/*     */   {
/*  62 */     Map<String, Object> result = new HashMap();
/*  63 */     for (String name : this.beans.keySet()) {
/*  64 */       if (findFactoryAnnotation(name, type) != null) {
/*  65 */         result.put(name, this.beanFactory.getBean(name));
/*     */       }
/*     */     }
/*  68 */     return result;
/*     */   }
/*     */   
/*     */   public <A extends Annotation> A findFactoryAnnotation(String beanName, Class<A> type)
/*     */   {
/*  73 */     Method method = findFactoryMethod(beanName);
/*  74 */     return method == null ? null : AnnotationUtils.findAnnotation(method, type);
/*     */   }
/*     */   
/*     */   private Method findFactoryMethod(String beanName) {
/*  78 */     if (!this.beans.containsKey(beanName)) {
/*  79 */       return null;
/*     */     }
/*  81 */     final AtomicReference<Method> found = new AtomicReference(null);
/*  82 */     MetaData meta = (MetaData)this.beans.get(beanName);
/*  83 */     final String factory = meta.getMethod();
/*  84 */     Class<?> type = this.beanFactory.getType(meta.getBean());
/*  85 */     ReflectionUtils.doWithMethods(type, new ReflectionUtils.MethodCallback()
/*     */     {
/*     */       public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException
/*     */       {
/*  89 */         if (method.getName().equals(factory)) {
/*  90 */           found.compareAndSet(null, method);
/*     */         }
/*     */       }
/*  93 */     });
/*  94 */     return (Method)found.get();
/*     */   }
/*     */   
/*     */   private static class MetaData
/*     */   {
/*     */     private String bean;
/*     */     private String method;
/*     */     
/*     */     MetaData(String bean, String method)
/*     */     {
/* 104 */       this.bean = bean;
/* 105 */       this.method = method;
/*     */     }
/*     */     
/*     */     public String getBean() {
/* 109 */       return this.bean;
/*     */     }
/*     */     
/*     */     public String getMethod() {
/* 113 */       return this.method;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\properties\ConfigurationBeanFactoryMetaData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */