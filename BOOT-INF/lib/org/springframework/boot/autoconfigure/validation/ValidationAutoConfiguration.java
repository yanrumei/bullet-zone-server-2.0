/*    */ package org.springframework.boot.autoconfigure.validation;
/*    */ 
/*    */ import javax.validation.Validator;
/*    */ import javax.validation.executable.ExecutableValidator;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
/*    */ import org.springframework.boot.bind.RelaxedPropertyResolver;
/*    */ import org.springframework.boot.validation.MessageInterpolatorFactory;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.annotation.Import;
/*    */ import org.springframework.context.annotation.Lazy;
/*    */ import org.springframework.context.annotation.Role;
/*    */ import org.springframework.core.env.Environment;
/*    */ import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
/*    */ import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
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
/*    */ @Configuration
/*    */ @ConditionalOnClass({ExecutableValidator.class})
/*    */ @ConditionalOnResource(resources={"classpath:META-INF/services/javax.validation.spi.ValidationProvider"})
/*    */ @Import({PrimaryDefaultValidatorPostProcessor.class})
/*    */ public class ValidationAutoConfiguration
/*    */ {
/*    */   @Bean
/*    */   @Role(2)
/*    */   @ConditionalOnMissingBean({Validator.class})
/*    */   public static LocalValidatorFactoryBean defaultValidator()
/*    */   {
/* 55 */     LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
/* 56 */     MessageInterpolatorFactory interpolatorFactory = new MessageInterpolatorFactory();
/* 57 */     factoryBean.setMessageInterpolator(interpolatorFactory.getObject());
/* 58 */     return factoryBean;
/*    */   }
/*    */   
/*    */   @Bean
/*    */   @ConditionalOnMissingBean
/*    */   public static MethodValidationPostProcessor methodValidationPostProcessor(Environment environment, @Lazy Validator validator)
/*    */   {
/* 65 */     MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
/* 66 */     processor.setProxyTargetClass(determineProxyTargetClass(environment));
/* 67 */     processor.setValidator(validator);
/* 68 */     return processor;
/*    */   }
/*    */   
/*    */   private static boolean determineProxyTargetClass(Environment environment) {
/* 72 */     RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(environment, "spring.aop.");
/*    */     
/* 74 */     Boolean value = (Boolean)resolver.getProperty("proxyTargetClass", Boolean.class);
/* 75 */     return value != null ? value.booleanValue() : true;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\validation\ValidationAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */