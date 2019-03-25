/*    */ package org.springframework.web.bind.support;
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
/*    */ public class SimpleSessionStatus
/*    */   implements SessionStatus
/*    */ {
/* 28 */   private boolean complete = false;
/*    */   
/*    */ 
/*    */   public void setComplete()
/*    */   {
/* 33 */     this.complete = true;
/*    */   }
/*    */   
/*    */   public boolean isComplete()
/*    */   {
/* 38 */     return this.complete;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\bind\support\SimpleSessionStatus.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */