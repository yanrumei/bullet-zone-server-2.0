/*    */ package org.springframework.boot.autoconfigure.validation;
/*    */ 
/*    */ import org.springframework.beans.BeansException;
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.beans.factory.BeanFactoryAware;
/*    */ import org.springframework.beans.factory.BeanFactoryUtils;
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*    */ import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
/*    */ import org.springframework.core.type.AnnotationMetadata;
/*    */ import org.springframework.validation.Validator;
/*    */ import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
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
/*    */ class PrimaryDefaultValidatorPostProcessor
/*    */   implements ImportBeanDefinitionRegistrar, BeanFactoryAware
/*    */ {
/*    */   private static final String VALIDATOR_BEAN_NAME = "defaultValidator";
/*    */   private ConfigurableListableBeanFactory beanFactory;
/*    */   
/*    */   public void setBeanFactory(BeanFactory beanFactory)
/*    */     throws BeansException
/*    */   {
/* 53 */     if ((beanFactory instanceof ConfigurableListableBeanFactory)) {
/* 54 */       this.beanFactory = ((ConfigurableListableBeanFactory)beanFactory);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry)
/*    */   {
/* 61 */     BeanDefinition definition = getAutoConfiguredValidator(registry);
/* 62 */     if (definition != null) {
/* 63 */       definition.setPrimary(!hasPrimarySpringValidator(registry));
/*    */     }
/*    */   }
/*    */   
/*    */   private BeanDefinition getAutoConfiguredValidator(BeanDefinitionRegistry registry) {
/* 68 */     if (registry.containsBeanDefinition("defaultValidator")) {
/* 69 */       BeanDefinition definition = registry.getBeanDefinition("defaultValidator");
/* 70 */       if ((definition.getRole() == 2) && (isTypeMatch("defaultValidator", LocalValidatorFactoryBean.class)))
/*    */       {
/* 72 */         return definition;
/*    */       }
/*    */     }
/* 75 */     return null;
/*    */   }
/*    */   
/*    */   private boolean isTypeMatch(String name, Class<?> type) {
/* 79 */     return (this.beanFactory != null) && (this.beanFactory.isTypeMatch(name, type));
/*    */   }
/*    */   
/*    */   private boolean hasPrimarySpringValidator(BeanDefinitionRegistry registry) {
/* 83 */     String[] validatorBeans = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this.beanFactory, Validator.class, false, false);
/*    */     
/* 85 */     for (String validatorBean : validatorBeans) {
/* 86 */       BeanDefinition definition = registry.getBeanDefinition(validatorBean);
/* 87 */       if ((definition != null) && (definition.isPrimary())) {
/* 88 */         return true;
/*    */       }
/*    */     }
/* 91 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\validation\PrimaryDefaultValidatorPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */