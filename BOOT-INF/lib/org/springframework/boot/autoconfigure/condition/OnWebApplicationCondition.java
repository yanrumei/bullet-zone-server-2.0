/*    */ package org.springframework.boot.autoconfigure.condition;
/*    */ 
/*    */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*    */ import org.springframework.context.annotation.ConditionContext;
/*    */ import org.springframework.core.annotation.Order;
/*    */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*    */ import org.springframework.util.ClassUtils;
/*    */ import org.springframework.util.ObjectUtils;
/*    */ import org.springframework.web.context.WebApplicationContext;
/*    */ import org.springframework.web.context.support.StandardServletEnvironment;
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
/*    */ @Order(-2147483628)
/*    */ class OnWebApplicationCondition
/*    */   extends SpringBootCondition
/*    */ {
/*    */   private static final String WEB_CONTEXT_CLASS = "org.springframework.web.context.support.GenericWebApplicationContext";
/*    */   
/*    */   public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*    */   {
/* 47 */     boolean required = metadata.isAnnotated(ConditionalOnWebApplication.class.getName());
/* 48 */     ConditionOutcome outcome = isWebApplication(context, metadata, required);
/* 49 */     if ((required) && (!outcome.isMatch())) {
/* 50 */       return ConditionOutcome.noMatch(outcome.getConditionMessage());
/*    */     }
/* 52 */     if ((!required) && (outcome.isMatch())) {
/* 53 */       return ConditionOutcome.noMatch(outcome.getConditionMessage());
/*    */     }
/* 55 */     return ConditionOutcome.match(outcome.getConditionMessage());
/*    */   }
/*    */   
/*    */   private ConditionOutcome isWebApplication(ConditionContext context, AnnotatedTypeMetadata metadata, boolean required)
/*    */   {
/* 60 */     ConditionMessage.Builder message = ConditionMessage.forCondition(ConditionalOnWebApplication.class, new Object[] { required ? "(required)" : "" });
/*    */     
/* 62 */     if (!ClassUtils.isPresent("org.springframework.web.context.support.GenericWebApplicationContext", context.getClassLoader())) {
/* 63 */       return 
/* 64 */         ConditionOutcome.noMatch(message.didNotFind("web application classes").atAll());
/*    */     }
/* 66 */     if (context.getBeanFactory() != null) {
/* 67 */       String[] scopes = context.getBeanFactory().getRegisteredScopeNames();
/* 68 */       if (ObjectUtils.containsElement(scopes, "session")) {
/* 69 */         return ConditionOutcome.match(message.foundExactly("'session' scope"));
/*    */       }
/*    */     }
/* 72 */     if ((context.getEnvironment() instanceof StandardServletEnvironment)) {
/* 73 */       return 
/* 74 */         ConditionOutcome.match(message.foundExactly("StandardServletEnvironment"));
/*    */     }
/* 76 */     if ((context.getResourceLoader() instanceof WebApplicationContext)) {
/* 77 */       return ConditionOutcome.match(message.foundExactly("WebApplicationContext"));
/*    */     }
/* 79 */     return ConditionOutcome.noMatch(message.because("not a web application"));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\condition\OnWebApplicationCondition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */