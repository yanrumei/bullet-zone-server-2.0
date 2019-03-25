/*    */ package org.springframework.boot.web.servlet;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*    */ import org.springframework.context.annotation.ScannedGenericBeanDefinition;
/*    */ import org.springframework.core.annotation.AnnotationAttributes;
/*    */ import org.springframework.core.type.AnnotationMetadata;
/*    */ import org.springframework.core.type.filter.AnnotationTypeFilter;
/*    */ import org.springframework.core.type.filter.TypeFilter;
/*    */ import org.springframework.util.Assert;
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
/*    */ abstract class ServletComponentHandler
/*    */ {
/*    */   private final Class<? extends Annotation> annotationType;
/*    */   private final TypeFilter typeFilter;
/*    */   
/*    */   protected ServletComponentHandler(Class<? extends Annotation> annotationType)
/*    */   {
/* 43 */     this.typeFilter = new AnnotationTypeFilter(annotationType);
/* 44 */     this.annotationType = annotationType;
/*    */   }
/*    */   
/*    */   TypeFilter getTypeFilter() {
/* 48 */     return this.typeFilter;
/*    */   }
/*    */   
/*    */   protected String[] extractUrlPatterns(String attribute, Map<String, Object> attributes)
/*    */   {
/* 53 */     String[] value = (String[])attributes.get("value");
/* 54 */     String[] urlPatterns = (String[])attributes.get("urlPatterns");
/* 55 */     if (urlPatterns.length > 0) {
/* 56 */       Assert.state(value.length == 0, "The urlPatterns and value attributes are mutually exclusive.");
/*    */       
/* 58 */       return urlPatterns;
/*    */     }
/* 60 */     return value;
/*    */   }
/*    */   
/*    */   protected final Map<String, String> extractInitParameters(Map<String, Object> attributes)
/*    */   {
/* 65 */     Map<String, String> initParameters = new HashMap();
/* 66 */     for (AnnotationAttributes initParam : (AnnotationAttributes[])attributes.get("initParams")) {
/* 68 */       String name = (String)initParam.get("name");
/* 69 */       String value = (String)initParam.get("value");
/* 70 */       initParameters.put(name, value);
/*    */     }
/* 72 */     return initParameters;
/*    */   }
/*    */   
/*    */ 
/*    */   void handle(ScannedGenericBeanDefinition beanDefinition, BeanDefinitionRegistry registry)
/*    */   {
/* 78 */     Map<String, Object> attributes = beanDefinition.getMetadata().getAnnotationAttributes(this.annotationType.getName());
/* 79 */     if (attributes != null) {
/* 80 */       doHandle(attributes, beanDefinition, registry);
/*    */     }
/*    */   }
/*    */   
/*    */   protected abstract void doHandle(Map<String, Object> paramMap, ScannedGenericBeanDefinition paramScannedGenericBeanDefinition, BeanDefinitionRegistry paramBeanDefinitionRegistry);
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\web\servlet\ServletComponentHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */