/*    */ package org.hibernate.validator.internal.util.privilegedactions;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.security.PrivilegedAction;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class GetDeclaredConstructors
/*    */   implements PrivilegedAction<Constructor<?>[]>
/*    */ {
/*    */   private final Class<?> clazz;
/*    */   
/*    */   public static GetDeclaredConstructors action(Class<?> clazz)
/*    */   {
/* 21 */     return new GetDeclaredConstructors(clazz);
/*    */   }
/*    */   
/*    */   private GetDeclaredConstructors(Class<?> clazz) {
/* 25 */     this.clazz = clazz;
/*    */   }
/*    */   
/*    */   public Constructor<?>[] run()
/*    */   {
/* 30 */     return this.clazz.getDeclaredConstructors();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\privilegedactions\GetDeclaredConstructors.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */