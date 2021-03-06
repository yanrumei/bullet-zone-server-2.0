/*    */ package org.springframework.cglib.core;
/*    */ 
/*    */ import java.lang.reflect.Member;
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
/*    */ public class RejectModifierPredicate
/*    */   implements Predicate
/*    */ {
/*    */   private int rejectMask;
/*    */   
/*    */   public RejectModifierPredicate(int rejectMask)
/*    */   {
/* 24 */     this.rejectMask = rejectMask;
/*    */   }
/*    */   
/*    */   public boolean evaluate(Object arg) {
/* 28 */     return (((Member)arg).getModifiers() & this.rejectMask) == 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\core\RejectModifierPredicate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */