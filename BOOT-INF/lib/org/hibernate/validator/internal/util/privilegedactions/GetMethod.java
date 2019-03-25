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
/*    */ public final class GetMethod
/*    */   implements PrivilegedAction<Method>
/*    */ {
/*    */   private final Class<?> clazz;
/*    */   private final String methodName;
/*    */   
/*    */   public static GetMethod action(Class<?> clazz, String methodName)
/*    */   {
/* 22 */     return new GetMethod(clazz, methodName);
/*    */   }
/*    */   
/*    */   private GetMethod(Class<?> clazz, String methodName) {
/* 26 */     this.clazz = clazz;
/* 27 */     this.methodName = methodName;
/*    */   }
/*    */   
/*    */   public Method run()
/*    */   {
/*    */     try {
/* 33 */       return this.clazz.getMethod(this.methodName, new Class[0]);
/*    */     }
/*    */     catch (NoSuchMethodException e) {}
/* 36 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\privilegedactions\GetMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */