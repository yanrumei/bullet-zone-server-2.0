/*    */ package javax.validation;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
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
/*    */ public class ConstraintViolationException
/*    */   extends ValidationException
/*    */ {
/*    */   private final Set<ConstraintViolation<?>> constraintViolations;
/*    */   
/*    */   public ConstraintViolationException(String message, Set<? extends ConstraintViolation<?>> constraintViolations)
/*    */   {
/* 39 */     super(message);
/*    */     
/* 41 */     if (constraintViolations == null) {
/* 42 */       this.constraintViolations = null;
/*    */     }
/*    */     else {
/* 45 */       this.constraintViolations = new HashSet(constraintViolations);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ConstraintViolationException(Set<? extends ConstraintViolation<?>> constraintViolations)
/*    */   {
/* 55 */     this(null, constraintViolations);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Set<ConstraintViolation<?>> getConstraintViolations()
/*    */   {
/* 64 */     return this.constraintViolations;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\ConstraintViolationException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */