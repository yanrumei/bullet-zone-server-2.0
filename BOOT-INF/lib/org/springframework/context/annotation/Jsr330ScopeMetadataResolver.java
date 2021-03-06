/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.core.type.AnnotationMetadata;
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
/*     */ public class Jsr330ScopeMetadataResolver
/*     */   implements ScopeMetadataResolver
/*     */ {
/*  44 */   private final Map<String, String> scopeMap = new HashMap();
/*     */   
/*     */   public Jsr330ScopeMetadataResolver()
/*     */   {
/*  48 */     registerScope("javax.inject.Singleton", "singleton");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void registerScope(Class<?> annotationType, String scopeName)
/*     */   {
/*  59 */     this.scopeMap.put(annotationType.getName(), scopeName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void registerScope(String annotationType, String scopeName)
/*     */   {
/*  69 */     this.scopeMap.put(annotationType, scopeName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String resolveScopeName(String annotationType)
/*     */   {
/*  80 */     return (String)this.scopeMap.get(annotationType);
/*     */   }
/*     */   
/*     */ 
/*     */   public ScopeMetadata resolveScopeMetadata(BeanDefinition definition)
/*     */   {
/*  86 */     ScopeMetadata metadata = new ScopeMetadata();
/*  87 */     metadata.setScopeName("prototype");
/*  88 */     AnnotatedBeanDefinition annDef; String found; if ((definition instanceof AnnotatedBeanDefinition)) {
/*  89 */       annDef = (AnnotatedBeanDefinition)definition;
/*  90 */       Set<String> annTypes = annDef.getMetadata().getAnnotationTypes();
/*  91 */       found = null;
/*  92 */       for (String annType : annTypes) {
/*  93 */         Set<String> metaAnns = annDef.getMetadata().getMetaAnnotationTypes(annType);
/*  94 */         if (metaAnns.contains("javax.inject.Scope")) {
/*  95 */           if (found != null)
/*     */           {
/*  97 */             throw new IllegalStateException("Found ambiguous scope annotations on bean class [" + definition.getBeanClassName() + "]: " + found + ", " + annType);
/*     */           }
/*  99 */           found = annType;
/* 100 */           String scopeName = resolveScopeName(annType);
/* 101 */           if (scopeName == null) {
/* 102 */             throw new IllegalStateException("Unsupported scope annotation - not mapped onto Spring scope name: " + annType);
/*     */           }
/*     */           
/* 105 */           metadata.setScopeName(scopeName);
/*     */         }
/*     */       }
/*     */     }
/* 109 */     return metadata;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\annotation\Jsr330ScopeMetadataResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */