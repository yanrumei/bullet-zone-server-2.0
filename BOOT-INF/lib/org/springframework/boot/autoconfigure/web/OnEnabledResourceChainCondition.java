/*    */ package org.springframework.boot.autoconfigure.web;
/*    */ 
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionMessage;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.Builder;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.ItemsBuilder;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
/*    */ import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
/*    */ import org.springframework.boot.bind.RelaxedPropertyResolver;
/*    */ import org.springframework.context.annotation.ConditionContext;
/*    */ import org.springframework.core.env.ConfigurableEnvironment;
/*    */ import org.springframework.core.env.PropertyResolver;
/*    */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ class OnEnabledResourceChainCondition
/*    */   extends SpringBootCondition
/*    */ {
/*    */   private static final String WEBJAR_ASSET_LOCATOR = "org.webjars.WebJarAssetLocator";
/*    */   
/*    */   public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*    */   {
/* 46 */     ConfigurableEnvironment environment = (ConfigurableEnvironment)context.getEnvironment();
/* 47 */     boolean fixed = getEnabledProperty(environment, "strategy.fixed.", Boolean.valueOf(false)).booleanValue();
/* 48 */     boolean content = getEnabledProperty(environment, "strategy.content.", Boolean.valueOf(false)).booleanValue();
/* 49 */     Boolean chain = getEnabledProperty(environment, "", null);
/* 50 */     Boolean match = ResourceProperties.Chain.getEnabled(fixed, content, chain);
/*    */     
/* 52 */     ConditionMessage.Builder message = ConditionMessage.forCondition(ConditionalOnEnabledResourceChain.class, new Object[0]);
/* 53 */     if (match == null) {
/* 54 */       if (ClassUtils.isPresent("org.webjars.WebJarAssetLocator", getClass().getClassLoader())) {
/* 55 */         return 
/* 56 */           ConditionOutcome.match(message.found("class").items(new Object[] { "org.webjars.WebJarAssetLocator" }));
/*    */       }
/* 58 */       return 
/* 59 */         ConditionOutcome.noMatch(message.didNotFind("class").items(new Object[] { "org.webjars.WebJarAssetLocator" }));
/*    */     }
/* 61 */     if (match.booleanValue()) {
/* 62 */       return ConditionOutcome.match(message.because("enabled"));
/*    */     }
/* 64 */     return ConditionOutcome.noMatch(message.because("disabled"));
/*    */   }
/*    */   
/*    */   private Boolean getEnabledProperty(ConfigurableEnvironment environment, String key, Boolean defaultValue)
/*    */   {
/* 69 */     PropertyResolver resolver = new RelaxedPropertyResolver(environment, "spring.resources.chain." + key);
/*    */     
/* 71 */     return (Boolean)resolver.getProperty("enabled", Boolean.class, defaultValue);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\web\OnEnabledResourceChainCondition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */