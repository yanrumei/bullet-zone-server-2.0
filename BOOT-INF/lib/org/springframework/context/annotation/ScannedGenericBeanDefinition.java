/*    */ package org.springframework.context.annotation;
/*    */ 
/*    */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*    */ import org.springframework.beans.factory.support.GenericBeanDefinition;
/*    */ import org.springframework.core.type.AnnotationMetadata;
/*    */ import org.springframework.core.type.MethodMetadata;
/*    */ import org.springframework.core.type.classreading.MetadataReader;
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
/*    */ public class ScannedGenericBeanDefinition
/*    */   extends GenericBeanDefinition
/*    */   implements AnnotatedBeanDefinition
/*    */ {
/*    */   private final AnnotationMetadata metadata;
/*    */   
/*    */   public ScannedGenericBeanDefinition(MetadataReader metadataReader)
/*    */   {
/* 59 */     Assert.notNull(metadataReader, "MetadataReader must not be null");
/* 60 */     this.metadata = metadataReader.getAnnotationMetadata();
/* 61 */     setBeanClassName(this.metadata.getClassName());
/*    */   }
/*    */   
/*    */ 
/*    */   public final AnnotationMetadata getMetadata()
/*    */   {
/* 67 */     return this.metadata;
/*    */   }
/*    */   
/*    */   public MethodMetadata getFactoryMethodMetadata()
/*    */   {
/* 72 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\annotation\ScannedGenericBeanDefinition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */