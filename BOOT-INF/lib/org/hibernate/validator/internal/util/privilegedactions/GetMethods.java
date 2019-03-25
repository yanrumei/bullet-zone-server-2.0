/*    */ package org.hibernate.validator.internal.util.privilegedactions;
/*    */ 
/*    */ import java.lang.reflect.Method;
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
/*    */ public final class GetMethods
/*    */   implements PrivilegedAction<Method[]>
/*    */ {
/*    */   private final Class<?> clazz;
/*    */   
/*    */   public static GetMethods action(Class<?> clazz)
/*    */   {
/* 21 */     return new GetMethods(clazz);
/*    */   }
/*    */   
/*    */   private GetMethods(Class<?> clazz) {
/* 25 */     this.clazz = clazz;
/*    */   }
/*    */   
/*    */   public Method[] run()
/*    */   {
/* 30 */     return this.clazz.getMethods();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\privilegedactions\GetMethods.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */