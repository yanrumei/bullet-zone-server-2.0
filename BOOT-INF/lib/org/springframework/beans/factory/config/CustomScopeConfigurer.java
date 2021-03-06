/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class CustomScopeConfigurer
/*     */   implements BeanFactoryPostProcessor, BeanClassLoaderAware, Ordered
/*     */ {
/*     */   private Map<String, Object> scopes;
/*  50 */   private int order = Integer.MAX_VALUE;
/*     */   
/*  52 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setScopes(Map<String, Object> scopes)
/*     */   {
/*  62 */     this.scopes = scopes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addScope(String scopeName, Scope scope)
/*     */   {
/*  72 */     if (this.scopes == null) {
/*  73 */       this.scopes = new LinkedHashMap(1);
/*     */     }
/*  75 */     this.scopes.put(scopeName, scope);
/*     */   }
/*     */   
/*     */   public void setOrder(int order)
/*     */   {
/*  80 */     this.order = order;
/*     */   }
/*     */   
/*     */   public int getOrder()
/*     */   {
/*  85 */     return this.order;
/*     */   }
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader beanClassLoader)
/*     */   {
/*  90 */     this.beanClassLoader = beanClassLoader;
/*     */   }
/*     */   
/*     */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
/*     */     throws BeansException
/*     */   {
/*  96 */     if (this.scopes != null) {
/*  97 */       for (Map.Entry<String, Object> entry : this.scopes.entrySet()) {
/*  98 */         String scopeKey = (String)entry.getKey();
/*  99 */         Object value = entry.getValue();
/* 100 */         if ((value instanceof Scope)) {
/* 101 */           beanFactory.registerScope(scopeKey, (Scope)value);
/*     */         }
/* 103 */         else if ((value instanceof Class)) {
/* 104 */           Class<?> scopeClass = (Class)value;
/* 105 */           Assert.isAssignable(Scope.class, scopeClass, "Invalid scope class");
/* 106 */           beanFactory.registerScope(scopeKey, (Scope)BeanUtils.instantiateClass(scopeClass));
/*     */         }
/* 108 */         else if ((value instanceof String)) {
/* 109 */           Class<?> scopeClass = ClassUtils.resolveClassName((String)value, this.beanClassLoader);
/* 110 */           Assert.isAssignable(Scope.class, scopeClass, "Invalid scope class");
/* 111 */           beanFactory.registerScope(scopeKey, (Scope)BeanUtils.instantiateClass(scopeClass));
/*     */         }
/*     */         else
/*     */         {
/* 115 */           throw new IllegalArgumentException("Mapped value [" + value + "] for scope key [" + scopeKey + "] is not an instance of required type [" + Scope.class.getName() + "] or a corresponding Class or String value indicating a Scope implementation");
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\config\CustomScopeConfigurer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */