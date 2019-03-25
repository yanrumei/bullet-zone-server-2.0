/*    */ package org.springframework.boot.context.properties;
/*    */ 
/*    */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*    */ import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
/*    */ import org.springframework.core.type.AnnotationMetadata;
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
/*    */ public class ConfigurationPropertiesBindingPostProcessorRegistrar
/*    */   implements ImportBeanDefinitionRegistrar
/*    */ {
/* 37 */   public static final String BINDER_BEAN_NAME = ConfigurationPropertiesBindingPostProcessor.class
/* 38 */     .getName();
/*    */   
/* 40 */   private static final String METADATA_BEAN_NAME = BINDER_BEAN_NAME + ".store";
/*    */   
/*    */ 
/*    */   public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry)
/*    */   {
/* 45 */     if (!registry.containsBeanDefinition(BINDER_BEAN_NAME))
/*    */     {
/* 47 */       BeanDefinitionBuilder meta = BeanDefinitionBuilder.genericBeanDefinition(ConfigurationBeanFactoryMetaData.class);
/* 48 */       BeanDefinitionBuilder bean = BeanDefinitionBuilder.genericBeanDefinition(ConfigurationPropertiesBindingPostProcessor.class);
/*    */       
/* 50 */       bean.addPropertyReference("beanMetaDataStore", METADATA_BEAN_NAME);
/* 51 */       registry.registerBeanDefinition(BINDER_BEAN_NAME, bean.getBeanDefinition());
/* 52 */       registry.registerBeanDefinition(METADATA_BEAN_NAME, meta.getBeanDefinition());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\properties\ConfigurationPropertiesBindingPostProcessorRegistrar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */