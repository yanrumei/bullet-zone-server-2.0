/*    */ package org.springframework.boot.autoconfigure.cache;
/*    */ 
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionMessage;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.Builder;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
/*    */ import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
/*    */ import org.springframework.boot.bind.RelaxedPropertyResolver;
/*    */ import org.springframework.context.annotation.ConditionContext;
/*    */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*    */ import org.springframework.core.type.AnnotationMetadata;
/*    */ import org.springframework.core.type.ClassMetadata;
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
/*    */ class CacheCondition
/*    */   extends SpringBootCondition
/*    */ {
/*    */   public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*    */   {
/* 40 */     String sourceClass = "";
/* 41 */     if ((metadata instanceof ClassMetadata)) {
/* 42 */       sourceClass = ((ClassMetadata)metadata).getClassName();
/*    */     }
/* 44 */     ConditionMessage.Builder message = ConditionMessage.forCondition("Cache", new Object[] { sourceClass });
/*    */     
/*    */ 
/* 47 */     RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(context.getEnvironment(), "spring.cache.");
/* 48 */     if (!resolver.containsProperty("type")) {
/* 49 */       return ConditionOutcome.match(message.because("automatic cache type"));
/*    */     }
/*    */     
/* 52 */     CacheType cacheType = CacheConfigurations.getType(((AnnotationMetadata)metadata).getClassName());
/* 53 */     String value = resolver.getProperty("type").replace('-', '_').toUpperCase();
/* 54 */     if (value.equals(cacheType.name())) {
/* 55 */       return ConditionOutcome.match(message.because(value + " cache type"));
/*    */     }
/* 57 */     return ConditionOutcome.noMatch(message.because(value + " cache type"));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\cache\CacheCondition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */