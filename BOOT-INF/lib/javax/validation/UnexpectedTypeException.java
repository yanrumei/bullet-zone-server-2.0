/*    */ package javax.validation;
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
/*    */ public class UnexpectedTypeException
/*    */   extends ConstraintDeclarationException
/*    */ {
/*    */   public UnexpectedTypeException(String message)
/*    */   {
/* 27 */     super(message);
/*    */   }
/*    */   
/*    */ 
/*    */   public UnexpectedTypeException() {}
/*    */   
/*    */   public UnexpectedTypeException(String message, Throwable cause)
/*    */   {
/* 35 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public UnexpectedTypeException(Throwable cause) {
/* 39 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\UnexpectedTypeException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */