/*    */ package org.springframework.context.event;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import org.springframework.aop.support.AopUtils;
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.context.ApplicationEvent;
/*    */ import org.springframework.context.expression.AnnotatedElementKey;
/*    */ import org.springframework.context.expression.BeanFactoryResolver;
/*    */ import org.springframework.context.expression.CachedExpressionEvaluator;
/*    */ import org.springframework.context.expression.CachedExpressionEvaluator.ExpressionKey;
/*    */ import org.springframework.context.expression.MethodBasedEvaluationContext;
/*    */ import org.springframework.expression.EvaluationContext;
/*    */ import org.springframework.expression.Expression;
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
/*    */ class EventExpressionEvaluator
/*    */   extends CachedExpressionEvaluator
/*    */ {
/* 43 */   private final Map<CachedExpressionEvaluator.ExpressionKey, Expression> conditionCache = new ConcurrentHashMap(64);
/*    */   
/* 45 */   private final Map<AnnotatedElementKey, Method> targetMethodCache = new ConcurrentHashMap(64);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public EvaluationContext createEvaluationContext(ApplicationEvent event, Class<?> targetClass, Method method, Object[] args, BeanFactory beanFactory)
/*    */   {
/* 55 */     Method targetMethod = getTargetMethod(targetClass, method);
/* 56 */     EventExpressionRootObject root = new EventExpressionRootObject(event, args);
/*    */     
/* 58 */     MethodBasedEvaluationContext evaluationContext = new MethodBasedEvaluationContext(root, targetMethod, args, getParameterNameDiscoverer());
/* 59 */     if (beanFactory != null) {
/* 60 */       evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
/*    */     }
/* 62 */     return evaluationContext;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean condition(String conditionExpression, AnnotatedElementKey elementKey, EvaluationContext evalContext)
/*    */   {
/* 71 */     return ((Boolean)getExpression(this.conditionCache, elementKey, conditionExpression).getValue(evalContext, Boolean.TYPE)).booleanValue();
/*    */   }
/*    */   
/*    */   private Method getTargetMethod(Class<?> targetClass, Method method)
/*    */   {
/* 76 */     AnnotatedElementKey methodKey = new AnnotatedElementKey(method, targetClass);
/* 77 */     Method targetMethod = (Method)this.targetMethodCache.get(methodKey);
/* 78 */     if (targetMethod == null) {
/* 79 */       targetMethod = AopUtils.getMostSpecificMethod(method, targetClass);
/* 80 */       this.targetMethodCache.put(methodKey, targetMethod);
/*    */     }
/* 82 */     return targetMethod;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\event\EventExpressionEvaluator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */