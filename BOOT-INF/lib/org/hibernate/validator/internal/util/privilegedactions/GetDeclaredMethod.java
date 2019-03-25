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
/*    */ public final class GetDeclaredMethod
/*    */   implements PrivilegedAction<Method>
/*    */ {
/*    */   private final Class<?> clazz;
/*    */   private final String methodName;
/*    */   private final Class<?>[] parameterTypes;
/*    */   
/*    */   public static GetDeclaredMethod action(Class<?> clazz, String methodName, Class<?>... parameterTypes)
/*    */   {
/* 23 */     return new GetDeclaredMethod(clazz, methodName, parameterTypes);
/*    */   }
/*    */   
/*    */   private GetDeclaredMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
/* 27 */     this.clazz = clazz;
/* 28 */     this.methodName = methodName;
/* 29 */     this.parameterTypes = parameterTypes;
/*    */   }
/*    */   
/*    */   public Method run()
/*    */   {
/*    */     try {
/* 35 */       return this.clazz.getDeclaredMethod(this.methodName, this.parameterTypes);
/*    */     }
/*    */     catch (NoSuchMethodException e) {}
/* 38 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\privilegedactions\GetDeclaredMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */