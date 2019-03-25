/*    */ package org.hibernate.validator;
/*    */ 
/*    */ import java.security.BasicPermission;
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
/*    */ public class HibernateValidatorPermission
/*    */   extends BasicPermission
/*    */ {
/* 20 */   public static final HibernateValidatorPermission ACCESS_PRIVATE_MEMBERS = new HibernateValidatorPermission("accessPrivateMembers");
/*    */   
/*    */   public HibernateValidatorPermission(String name) {
/* 23 */     super(name);
/*    */   }
/*    */   
/*    */   public HibernateValidatorPermission(String name, String actions) {
/* 27 */     super(name, actions);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\HibernateValidatorPermission.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */