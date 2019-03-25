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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ParseException
/*    */   extends ExpressionException
/*    */ {
/*    */   public ParseException(String expressionString, int position, String message)
/*    */   {
/* 35 */     super(expressionString, position, message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ParseException(int position, String message, Throwable cause)
/*    */   {
/* 45 */     super(position, message, cause);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ParseException(int position, String message)
/*    */   {
/* 54 */     super(position, message);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\ParseException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */