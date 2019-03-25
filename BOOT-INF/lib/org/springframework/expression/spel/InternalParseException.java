/*    */ package org.springframework.expression.spel;
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
/*    */ public class InternalParseException
/*    */   extends RuntimeException
/*    */ {
/*    */   public InternalParseException(SpelParseException cause)
/*    */   {
/* 30 */     super(cause);
/*    */   }
/*    */   
/*    */   public SpelParseException getCause()
/*    */   {
/* 35 */     return (SpelParseException)super.getCause();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\spel\InternalParseException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */