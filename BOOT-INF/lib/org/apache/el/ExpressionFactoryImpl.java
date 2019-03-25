/*    */ package org.apache.el;
/*    */ 
/*    */ import javax.el.ELContext;
/*    */ import javax.el.ELResolver;
/*    */ import javax.el.ExpressionFactory;
/*    */ import javax.el.MethodExpression;
/*    */ import javax.el.ValueExpression;
/*    */ import org.apache.el.lang.ELSupport;
/*    */ import org.apache.el.lang.ExpressionBuilder;
/*    */ import org.apache.el.stream.StreamELResolverImpl;
/*    */ import org.apache.el.util.MessageFactory;
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
/*    */ 
/*    */ 
/*    */ public class ExpressionFactoryImpl
/*    */   extends ExpressionFactory
/*    */ {
/*    */   public Object coerceToType(Object obj, Class<?> type)
/*    */   {
/* 47 */     return ELSupport.coerceToType(null, obj, type);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public MethodExpression createMethodExpression(ELContext context, String expression, Class<?> expectedReturnType, Class<?>[] expectedParamTypes)
/*    */   {
/* 54 */     ExpressionBuilder builder = new ExpressionBuilder(expression, context);
/* 55 */     return builder.createMethodExpression(expectedReturnType, expectedParamTypes);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public ValueExpression createValueExpression(ELContext context, String expression, Class<?> expectedType)
/*    */   {
/* 62 */     if (expectedType == null)
/*    */     {
/* 64 */       throw new NullPointerException(MessageFactory.get("error.value.expectedType"));
/*    */     }
/* 66 */     ExpressionBuilder builder = new ExpressionBuilder(expression, context);
/* 67 */     return builder.createValueExpression(expectedType);
/*    */   }
/*    */   
/*    */ 
/*    */   public ValueExpression createValueExpression(Object instance, Class<?> expectedType)
/*    */   {
/* 73 */     if (expectedType == null)
/*    */     {
/* 75 */       throw new NullPointerException(MessageFactory.get("error.value.expectedType"));
/*    */     }
/* 77 */     return new ValueExpressionLiteral(instance, expectedType);
/*    */   }
/*    */   
/*    */   public ELResolver getStreamELResolver()
/*    */   {
/* 82 */     return new StreamELResolverImpl();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\ExpressionFactoryImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */