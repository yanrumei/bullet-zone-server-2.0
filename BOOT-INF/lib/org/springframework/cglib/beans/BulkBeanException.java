/*    */ package org.springframework.cglib.beans;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BulkBeanException
/*    */   extends RuntimeException
/*    */ {
/*    */   private int index;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private Throwable cause;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public BulkBeanException(String message, int index)
/*    */   {
/* 26 */     super(message);
/* 27 */     this.index = index;
/*    */   }
/*    */   
/*    */   public BulkBeanException(Throwable cause, int index) {
/* 31 */     super(cause.getMessage());
/* 32 */     this.index = index;
/* 33 */     this.cause = cause;
/*    */   }
/*    */   
/*    */   public int getIndex() {
/* 37 */     return this.index;
/*    */   }
/*    */   
/*    */   public Throwable getCause() {
/* 41 */     return this.cause;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\beans\BulkBeanException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */