/*    */ package org.springframework.boot.autoconfigure;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.core.io.support.SpringFactoriesLoader;
/*    */ import org.springframework.core.type.AnnotationMetadata;
/*    */ import org.springframework.core.type.ClassMetadata;
/*    */ import org.springframework.core.type.classreading.MetadataReader;
/*    */ import org.springframework.core.type.classreading.MetadataReaderFactory;
/*    */ import org.springframework.core.type.filter.TypeFilter;
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
/*    */ public class AutoConfigurationExcludeFilter
/*    */   implements TypeFilter, BeanClassLoaderAware
/*    */ {
/*    */   private ClassLoader beanClassLoader;
/*    */   private volatile List<String> autoConfigurations;
/*    */   
/*    */   public void setBeanClassLoader(ClassLoader beanClassLoader)
/*    */   {
/* 43 */     this.beanClassLoader = beanClassLoader;
/*    */   }
/*    */   
/*    */   public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
/*    */     throws IOException
/*    */   {
/* 49 */     return (isConfiguration(metadataReader)) && (isAutoConfiguration(metadataReader));
/*    */   }
/*    */   
/*    */   private boolean isConfiguration(MetadataReader metadataReader) {
/* 53 */     return 
/* 54 */       metadataReader.getAnnotationMetadata().isAnnotated(Configuration.class.getName());
/*    */   }
/*    */   
/*    */   private boolean isAutoConfiguration(MetadataReader metadataReader) {
/* 58 */     return 
/* 59 */       getAutoConfigurations().contains(metadataReader.getClassMetadata().getClassName());
/*    */   }
/*    */   
/*    */   protected List<String> getAutoConfigurations() {
/* 63 */     if (this.autoConfigurations == null) {
/* 64 */       this.autoConfigurations = SpringFactoriesLoader.loadFactoryNames(EnableAutoConfiguration.class, this.beanClassLoader);
/*    */     }
/*    */     
/* 67 */     return this.autoConfigurations;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\AutoConfigurationExcludeFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */