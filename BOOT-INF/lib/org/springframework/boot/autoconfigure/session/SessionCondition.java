/*    */ package org.springframework.boot.autoconfigure.session;
/*    */ 
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionMessage;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.Builder;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.ItemsBuilder;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
/*    */ import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
/*    */ import org.springframework.boot.bind.RelaxedPropertyResolver;
/*    */ import org.springframework.context.annotation.ConditionContext;
/*    */ import org.springframework.core.type.AnnotatedTypeMetadata;
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
/*    */ class SessionCondition
/*    */   extends SpringBootCondition
/*    */ {
/*    */   public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*    */   {
/* 39 */     ConditionMessage.Builder message = ConditionMessage.forCondition("Session Condition", new Object[0]);
/*    */     
/* 41 */     RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(context.getEnvironment(), "spring.session.");
/*    */     
/* 43 */     StoreType sessionStoreType = SessionStoreMappings.getType(((AnnotationMetadata)metadata).getClassName());
/* 44 */     if (!resolver.containsProperty("store-type")) {
/* 45 */       return ConditionOutcome.noMatch(message
/* 46 */         .didNotFind("spring.session.store-type property").atAll());
/*    */     }
/* 48 */     String value = resolver.getProperty("store-type").replace('-', '_').toUpperCase();
/* 49 */     if (value.equals(sessionStoreType.name())) {
/* 50 */       return ConditionOutcome.match(message
/* 51 */         .found("spring.session.store-type property").items(new Object[] { sessionStoreType }));
/*    */     }
/* 53 */     return ConditionOutcome.noMatch(message
/* 54 */       .found("spring.session.store-type property").items(new Object[] { value }));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\session\SessionCondition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */