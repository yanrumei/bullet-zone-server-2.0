/*    */ package org.hibernate.validator.internal.util.privilegedactions;
/*    */ 
/*    */ import java.lang.reflect.AccessibleObject;
/*    */ import java.lang.reflect.Member;
/*    */ import java.security.PrivilegedAction;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SetAccessibility
/*    */   implements PrivilegedAction<Object>
/*    */ {
/*    */   private final Member member;
/*    */   
/*    */   public static SetAccessibility action(Member member)
/*    */   {
/* 21 */     return new SetAccessibility(member);
/*    */   }
/*    */   
/*    */   private SetAccessibility(Member member) {
/* 25 */     this.member = member;
/*    */   }
/*    */   
/*    */   public Object run()
/*    */   {
/* 30 */     ((AccessibleObject)this.member).setAccessible(true);
/* 31 */     return this.member;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\privilegedactions\SetAccessibility.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */