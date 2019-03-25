/*    */ package org.springframework.boot.autoconfigure.condition;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.springframework.beans.factory.config.BeanExpressionContext;
/*    */ import org.springframework.beans.factory.config.BeanExpressionResolver;
/*    */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*    */ import org.springframework.context.annotation.ConditionContext;
/*    */ import org.springframework.context.expression.StandardBeanExpressionResolver;
/*    */ import org.springframework.core.annotation.Order;
/*    */ import org.springframework.core.env.Environment;
/*    */ import org.springframework.core.type.AnnotatedTypeMetadata;
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
/*    */ @Order(2147483627)
/*    */ class OnExpressionCondition
/*    */   extends SpringBootCondition
/*    */ {
/*    */   public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*    */   {
/* 42 */     String expression = (String)metadata.getAnnotationAttributes(ConditionalOnExpression.class.getName()).get("value");
/* 43 */     expression = wrapIfNecessary(expression);
/* 44 */     String rawExpression = expression;
/* 45 */     expression = context.getEnvironment().resolvePlaceholders(expression);
/* 46 */     ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
/*    */     
/* 48 */     BeanExpressionResolver resolver = beanFactory != null ? beanFactory.getBeanExpressionResolver() : null;
/* 49 */     BeanExpressionContext expressionContext = beanFactory != null ? new BeanExpressionContext(beanFactory, null) : null;
/*    */     
/* 51 */     if (resolver == null) {
/* 52 */       resolver = new StandardBeanExpressionResolver();
/*    */     }
/* 54 */     boolean result = ((Boolean)resolver.evaluate(expression, expressionContext)).booleanValue();
/* 55 */     return new ConditionOutcome(result, 
/* 56 */       ConditionMessage.forCondition(ConditionalOnExpression.class, new Object[] { "(" + rawExpression + ")" })
/* 57 */       .resultedIn(Boolean.valueOf(result)));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private String wrapIfNecessary(String expression)
/*    */   {
/* 66 */     if (!expression.startsWith("#{")) {
/* 67 */       return "#{" + expression + "}";
/*    */     }
/* 69 */     return expression;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\condition\OnExpressionCondition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */