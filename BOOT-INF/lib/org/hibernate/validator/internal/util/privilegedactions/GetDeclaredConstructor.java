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
/*    */ public final class GetDeclaredConstructor<T>
/*    */   implements PrivilegedAction<Constructor<T>>
/*    */ {
/*    */   private final Class<T> clazz;
/*    */   private final Class<?>[] params;
/*    */   
/*    */   public static <T> GetDeclaredConstructor<T> action(Class<T> clazz, Class<?>... params)
/*    */   {
/* 22 */     return new GetDeclaredConstructor(clazz, params);
/*    */   }
/*    */   
/*    */   private GetDeclaredConstructor(Class<T> clazz, Class<?>... params) {
/* 26 */     this.clazz = clazz;
/* 27 */     this.params = params;
/*    */   }
/*    */   
/*    */   public Constructor<T> run()
/*    */   {
/*    */     try {
/* 33 */       return this.clazz.getDeclaredConstructor(this.params);
/*    */     }
/*    */     catch (NoSuchMethodException e) {}
/* 36 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\privilegedactions\GetDeclaredConstructor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */