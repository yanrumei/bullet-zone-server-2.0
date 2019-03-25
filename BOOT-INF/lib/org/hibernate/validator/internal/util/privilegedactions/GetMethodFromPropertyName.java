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
/*    */ 
/*    */ 
/*    */ public final class GetMethodFromPropertyName
/*    */   implements PrivilegedAction<Method>
/*    */ {
/*    */   private final Class<?> clazz;
/*    */   private final String property;
/*    */   
/*    */   public static GetMethodFromPropertyName action(Class<?> clazz, String property)
/*    */   {
/* 24 */     return new GetMethodFromPropertyName(clazz, property);
/*    */   }
/*    */   
/*    */   private GetMethodFromPropertyName(Class<?> clazz, String property) {
/* 28 */     this.clazz = clazz;
/* 29 */     this.property = property;
/*    */   }
/*    */   
/*    */   public Method run()
/*    */   {
/*    */     try {
/* 35 */       char[] string = this.property.toCharArray();
/* 36 */       string[0] = Character.toUpperCase(string[0]);
/* 37 */       String fullMethodName = new String(string);
/*    */       try {
/* 39 */         return this.clazz.getMethod("get" + fullMethodName, new Class[0]);
/*    */       }
/*    */       catch (NoSuchMethodException e) {
/* 42 */         return this.clazz.getMethod("is" + fullMethodName, new Class[0]);
/*    */       }
/*    */       
/*    */ 
/* 46 */       return null;
/*    */     }
/*    */     catch (NoSuchMethodException e) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\privilegedactions\GetMethodFromPropertyName.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */