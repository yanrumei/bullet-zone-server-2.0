/*    */ package org.springframework.expression;
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
/*    */ public class ExpressionInvocationTargetException
/*    */   extends EvaluationException
/*    */ {
/*    */   public ExpressionInvocationTargetException(int position, String message, Throwable cause)
/*    */   {
/* 32 */     super(position, message, cause);
/*    */   }
/*    */   
/*    */   public ExpressionInvocationTargetException(int position, String message) {
/* 36 */     super(position, message);
/*    */   }
/*    */   
/*    */   public ExpressionInvocationTargetException(String expressionString, String message) {
/* 40 */     super(expressionString, message);
/*    */   }
/*    */   
/*    */   public ExpressionInvocationTargetException(String message, Throwable cause) {
/* 44 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public ExpressionInvocationTargetException(String message) {
/* 48 */     super(message);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\ExpressionInvocationTargetException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */