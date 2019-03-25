/*    */ package org.springframework.boot.autoconfigure.security.oauth2.client;
/*    */ 
/*    */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionMessage;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.Builder;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.ItemsBuilder;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
/*    */ import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
/*    */ import org.springframework.context.annotation.ConditionContext;
/*    */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*    */ import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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
/*    */ class EnableOAuth2SsoCondition
/*    */   extends SpringBootCondition
/*    */ {
/*    */   public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*    */   {
/* 38 */     String[] enablers = context.getBeanFactory().getBeanNamesForAnnotation(EnableOAuth2Sso.class);
/*    */     
/* 40 */     ConditionMessage.Builder message = ConditionMessage.forCondition("@EnableOAuth2Sso Condition", new Object[0]);
/* 41 */     for (String name : enablers) {
/* 42 */       if (context.getBeanFactory().isTypeMatch(name, WebSecurityConfigurerAdapter.class))
/*    */       {
/* 44 */         return ConditionOutcome.match(message
/* 45 */           .found("@EnableOAuth2Sso annotation on WebSecurityConfigurerAdapter")
/* 46 */           .items(new Object[] { name }));
/*    */       }
/*    */     }
/* 49 */     return ConditionOutcome.noMatch(message.didNotFind("@EnableOAuth2Sso annotation on any WebSecurityConfigurerAdapter")
/*    */     
/* 51 */       .atAll());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\oauth2\client\EnableOAuth2SsoCondition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */