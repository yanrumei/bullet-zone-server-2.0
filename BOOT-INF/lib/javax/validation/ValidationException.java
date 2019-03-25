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
/*    */ public class ValidationException
/*    */   extends RuntimeException
/*    */ {
/*    */   public ValidationException(String message)
/*    */   {
/* 26 */     super(message);
/*    */   }
/*    */   
/*    */ 
/*    */   public ValidationException() {}
/*    */   
/*    */   public ValidationException(String message, Throwable cause)
/*    */   {
/* 34 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public ValidationException(Throwable cause) {
/* 38 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\ValidationException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */