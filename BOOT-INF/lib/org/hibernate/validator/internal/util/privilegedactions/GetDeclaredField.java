/*    */ package org.hibernate.validator.internal.util.privilegedactions;
/*    */ 
/*    */ import java.lang.reflect.Field;
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
/*    */ public final class GetDeclaredField
/*    */   implements PrivilegedAction<Field>
/*    */ {
/*    */   private final Class<?> clazz;
/*    */   private final String fieldName;
/*    */   
/*    */   public static GetDeclaredField action(Class<?> clazz, String fieldName)
/*    */   {
/* 22 */     return new GetDeclaredField(clazz, fieldName);
/*    */   }
/*    */   
/*    */   private GetDeclaredField(Class<?> clazz, String fieldName) {
/* 26 */     this.clazz = clazz;
/* 27 */     this.fieldName = fieldName;
/*    */   }
/*    */   
/*    */   public Field run()
/*    */   {
/*    */     try {
/* 33 */       return this.clazz.getDeclaredField(this.fieldName);
/*    */     }
/*    */     catch (NoSuchFieldException e) {}
/*    */     
/* 37 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\privilegedactions\GetDeclaredField.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */