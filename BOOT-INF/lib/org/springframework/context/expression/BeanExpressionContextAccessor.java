/*    */ package org.springframework.context.expression;
/*    */ 
/*    */ import org.springframework.beans.factory.config.BeanExpressionContext;
/*    */ import org.springframework.expression.AccessException;
/*    */ import org.springframework.expression.EvaluationContext;
/*    */ import org.springframework.expression.PropertyAccessor;
/*    */ import org.springframework.expression.TypedValue;
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
/*    */ public class BeanExpressionContextAccessor
/*    */   implements PropertyAccessor
/*    */ {
/*    */   public boolean canRead(EvaluationContext context, Object target, String name)
/*    */     throws AccessException
/*    */   {
/* 37 */     return ((BeanExpressionContext)target).containsObject(name);
/*    */   }
/*    */   
/*    */   public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException
/*    */   {
/* 42 */     return new TypedValue(((BeanExpressionContext)target).getObject(name));
/*    */   }
/*    */   
/*    */   public boolean canWrite(EvaluationContext context, Object target, String name) throws AccessException
/*    */   {
/* 47 */     return false;
/*    */   }
/*    */   
/*    */   public void write(EvaluationContext context, Object target, String name, Object newValue) throws AccessException
/*    */   {
/* 52 */     throw new AccessException("Beans in a BeanFactory are read-only");
/*    */   }
/*    */   
/*    */   public Class<?>[] getSpecificTargetClasses()
/*    */   {
/* 57 */     return new Class[] { BeanExpressionContext.class };
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\expression\BeanExpressionContextAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */