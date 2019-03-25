/*    */ package org.springframework.cglib.core;
/*    */ 
/*    */ import java.lang.reflect.Method;
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
/*    */ public class DuplicatesPredicate
/*    */   implements Predicate
/*    */ {
/* 22 */   private Set unique = new HashSet();
/*    */   
/*    */   public boolean evaluate(Object arg) {
/* 25 */     return this.unique.add(MethodWrapper.create((Method)arg));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\core\DuplicatesPredicate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */