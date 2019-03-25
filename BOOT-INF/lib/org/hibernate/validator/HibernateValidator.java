/*    */ package org.hibernate.validator;
/*    */ 
/*    */ import javax.validation.Configuration;
/*    */ import javax.validation.ValidatorFactory;
/*    */ import javax.validation.spi.BootstrapState;
/*    */ import javax.validation.spi.ConfigurationState;
/*    */ import javax.validation.spi.ValidationProvider;
/*    */ import org.hibernate.validator.internal.engine.ConfigurationImpl;
/*    */ import org.hibernate.validator.internal.engine.ValidatorFactoryImpl;
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
/*    */ public class HibernateValidator
/*    */   implements ValidationProvider<HibernateValidatorConfiguration>
/*    */ {
/*    */   public HibernateValidatorConfiguration createSpecializedConfiguration(BootstrapState state)
/*    */   {
/* 28 */     return (HibernateValidatorConfiguration)HibernateValidatorConfiguration.class.cast(new ConfigurationImpl(this));
/*    */   }
/*    */   
/*    */   public Configuration<?> createGenericConfiguration(BootstrapState state)
/*    */   {
/* 33 */     return new ConfigurationImpl(state);
/*    */   }
/*    */   
/*    */   public ValidatorFactory buildValidatorFactory(ConfigurationState configurationState)
/*    */   {
/* 38 */     return new ValidatorFactoryImpl(configurationState);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\HibernateValidator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */