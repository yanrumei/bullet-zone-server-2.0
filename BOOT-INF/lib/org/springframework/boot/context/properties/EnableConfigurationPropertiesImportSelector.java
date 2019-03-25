/*     */ package org.springframework.boot.context.properties;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
/*     */ import org.springframework.context.annotation.ImportSelector;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class EnableConfigurationPropertiesImportSelector
/*     */   implements ImportSelector
/*     */ {
/*     */   public String[] selectImports(AnnotationMetadata metadata)
/*     */   {
/*  51 */     MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(EnableConfigurationProperties.class
/*  52 */       .getName(), false);
/*     */     
/*  54 */     Object[] type = attributes == null ? null : (Object[])attributes.getFirst("value");
/*  55 */     if ((type == null) || (type.length == 0)) {
/*  56 */       return new String[] {ConfigurationPropertiesBindingPostProcessorRegistrar.class
/*     */       
/*  58 */         .getName() };
/*     */     }
/*  60 */     return new String[] { ConfigurationPropertiesBeanRegistrar.class.getName(), ConfigurationPropertiesBindingPostProcessorRegistrar.class
/*  61 */       .getName() };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class ConfigurationPropertiesBeanRegistrar
/*     */     implements ImportBeanDefinitionRegistrar
/*     */   {
/*     */     public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry)
/*     */     {
/*  74 */       MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(EnableConfigurationProperties.class
/*  75 */         .getName(), false);
/*  76 */       List<Class<?>> types = collectClasses((List)attributes.get("value"));
/*  77 */       for (Class<?> type : types) {
/*  78 */         String prefix = extractPrefix(type);
/*     */         
/*  80 */         String name = StringUtils.hasText(prefix) ? prefix + "-" + type.getName() : type.getName();
/*  81 */         if (!registry.containsBeanDefinition(name)) {
/*  82 */           registerBeanDefinition(registry, type, name);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private String extractPrefix(Class<?> type) {
/*  88 */       ConfigurationProperties annotation = (ConfigurationProperties)AnnotationUtils.findAnnotation(type, ConfigurationProperties.class);
/*     */       
/*  90 */       if (annotation != null) {
/*  91 */         return annotation.prefix();
/*     */       }
/*  93 */       return "";
/*     */     }
/*     */     
/*     */     private List<Class<?>> collectClasses(List<Object> list) {
/*  97 */       ArrayList<Class<?>> result = new ArrayList();
/*  98 */       for (Object object : list) {
/*  99 */         for (Object value : (Object[])object) {
/* 100 */           if (((value instanceof Class)) && (value != Void.TYPE)) {
/* 101 */             result.add((Class)value);
/*     */           }
/*     */         }
/*     */       }
/* 105 */       return result;
/*     */     }
/*     */     
/*     */ 
/*     */     private void registerBeanDefinition(BeanDefinitionRegistry registry, Class<?> type, String name)
/*     */     {
/* 111 */       BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(type);
/* 112 */       AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
/* 113 */       registry.registerBeanDefinition(name, beanDefinition);
/*     */       
/* 115 */       ConfigurationProperties properties = (ConfigurationProperties)AnnotationUtils.findAnnotation(type, ConfigurationProperties.class);
/*     */       
/* 117 */       Assert.notNull(properties, "No " + ConfigurationProperties.class
/* 118 */         .getSimpleName() + " annotation found on  '" + type
/* 119 */         .getName() + "'.");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\properties\EnableConfigurationPropertiesImportSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */