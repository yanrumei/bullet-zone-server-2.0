/*    */ package org.springframework.boot.autoconfigure.integration;
/*    */ 
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.springframework.beans.BeansException;
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.beans.factory.BeanFactoryAware;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*    */ import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
/*    */ import org.springframework.core.type.AnnotationMetadata;
/*    */ import org.springframework.core.type.StandardAnnotationMetadata;
/*    */ import org.springframework.integration.annotation.IntegrationComponentScan;
/*    */ import org.springframework.integration.config.IntegrationComponentScanRegistrar;
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
/*    */ class IntegrationAutoConfigurationScanRegistrar
/*    */   extends IntegrationComponentScanRegistrar
/*    */   implements BeanFactoryAware
/*    */ {
/*    */   private BeanFactory beanFactory;
/*    */   
/*    */   public void setBeanFactory(BeanFactory beanFactory)
/*    */     throws BeansException
/*    */   {
/* 47 */     this.beanFactory = beanFactory;
/*    */   }
/*    */   
/*    */ 
/*    */   public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry)
/*    */   {
/* 53 */     super.registerBeanDefinitions(new IntegrationComponentScanConfigurationMetaData(this.beanFactory), registry);
/*    */   }
/*    */   
/*    */   @IntegrationComponentScan
/*    */   private static class IntegrationComponentScanConfiguration {}
/*    */   
/*    */   private static class IntegrationComponentScanConfigurationMetaData extends StandardAnnotationMetadata
/*    */   {
/*    */     private final BeanFactory beanFactory;
/*    */     
/*    */     IntegrationComponentScanConfigurationMetaData(BeanFactory beanFactory) {
/* 64 */       super(true);
/* 65 */       this.beanFactory = beanFactory;
/*    */     }
/*    */     
/*    */     public Map<String, Object> getAnnotationAttributes(String annotationName)
/*    */     {
/* 70 */       Map<String, Object> attributes = super.getAnnotationAttributes(annotationName);
/*    */       
/* 72 */       if ((IntegrationComponentScan.class.getName().equals(annotationName)) && 
/* 73 */         (AutoConfigurationPackages.has(this.beanFactory))) {
/* 74 */         List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
/* 75 */         attributes = new LinkedHashMap(attributes);
/* 76 */         attributes.put("value", packages.toArray(new String[packages.size()]));
/*    */       }
/* 78 */       return attributes;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\integration\IntegrationAutoConfigurationScanRegistrar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */