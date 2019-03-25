/*    */ package org.springframework.boot.diagnostics;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FailureAnalysis
/*    */ {
/*    */   private final String description;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private final String action;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private final Throwable cause;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public FailureAnalysis(String description, String action, Throwable cause)
/*    */   {
/* 42 */     this.description = description;
/* 43 */     this.action = action;
/* 44 */     this.cause = cause;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getDescription()
/*    */   {
/* 52 */     return this.description;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getAction()
/*    */   {
/* 60 */     return this.action;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Throwable getCause()
/*    */   {
/* 68 */     return this.cause;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\diagnostics\FailureAnalysis.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */