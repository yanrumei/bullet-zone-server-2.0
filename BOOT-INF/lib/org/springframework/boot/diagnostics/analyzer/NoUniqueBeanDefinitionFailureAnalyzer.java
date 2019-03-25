/*    */ package org.springframework.boot.diagnostics.analyzer;
/*    */ 
/*    */ import org.springframework.beans.BeansException;
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.beans.factory.BeanFactoryAware;
/*    */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*    */ import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*    */ import org.springframework.boot.diagnostics.FailureAnalysis;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ class NoUniqueBeanDefinitionFailureAnalyzer
/*    */   extends AbstractInjectionFailureAnalyzer<NoUniqueBeanDefinitionException>
/*    */   implements BeanFactoryAware
/*    */ {
/*    */   private ConfigurableBeanFactory beanFactory;
/*    */   
/*    */   public void setBeanFactory(BeanFactory beanFactory)
/*    */     throws BeansException
/*    */   {
/* 44 */     Assert.isInstanceOf(ConfigurableBeanFactory.class, beanFactory);
/* 45 */     this.beanFactory = ((ConfigurableBeanFactory)beanFactory);
/*    */   }
/*    */   
/*    */ 
/*    */   protected FailureAnalysis analyze(Throwable rootFailure, NoUniqueBeanDefinitionException cause, String description)
/*    */   {
/* 51 */     if (description == null) {
/* 52 */       return null;
/*    */     }
/* 54 */     String[] beanNames = extractBeanNames(cause);
/* 55 */     if (beanNames == null) {
/* 56 */       return null;
/*    */     }
/* 58 */     StringBuilder message = new StringBuilder();
/* 59 */     message.append(String.format("%s required a single bean, but %d were found:%n", new Object[] { description, 
/* 60 */       Integer.valueOf(beanNames.length) }));
/* 61 */     for (String beanName : beanNames) {
/* 62 */       buildMessage(message, beanName);
/*    */     }
/* 64 */     return new FailureAnalysis(message.toString(), "Consider marking one of the beans as @Primary, updating the consumer to accept multiple beans, or using @Qualifier to identify the bean that should be consumed", cause);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private void buildMessage(StringBuilder message, String beanName)
/*    */   {
/*    */     try
/*    */     {
/* 74 */       BeanDefinition definition = this.beanFactory.getMergedBeanDefinition(beanName);
/* 75 */       message.append(getDefinitionDescription(beanName, definition));
/*    */     }
/*    */     catch (NoSuchBeanDefinitionException ex) {
/* 78 */       message.append(
/* 79 */         String.format("\t- %s: a programmatically registered singleton", new Object[] { beanName }));
/*    */     }
/*    */   }
/*    */   
/*    */   private String getDefinitionDescription(String beanName, BeanDefinition definition) {
/* 84 */     if (StringUtils.hasText(definition.getFactoryMethodName())) {
/* 85 */       return String.format("\t- %s: defined by method '%s' in %s%n", new Object[] { beanName, definition
/* 86 */         .getFactoryMethodName(), definition
/* 87 */         .getResourceDescription() });
/*    */     }
/* 89 */     return String.format("\t- %s: defined in %s%n", new Object[] { beanName, definition
/* 90 */       .getResourceDescription() });
/*    */   }
/*    */   
/*    */   private String[] extractBeanNames(NoUniqueBeanDefinitionException cause) {
/* 94 */     if (cause.getMessage().indexOf("but found") > -1) {
/* 95 */       return StringUtils.commaDelimitedListToStringArray(cause.getMessage()
/* 96 */         .substring(cause.getMessage().lastIndexOf(":") + 1).trim());
/*    */     }
/* 98 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\diagnostics\analyzer\NoUniqueBeanDefinitionFailureAnalyzer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */