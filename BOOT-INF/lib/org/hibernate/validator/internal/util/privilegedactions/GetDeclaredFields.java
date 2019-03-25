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
/*    */ public final class GetDeclaredFields
/*    */   implements PrivilegedAction<Field[]>
/*    */ {
/*    */   private final Class<?> clazz;
/*    */   
/*    */   public static GetDeclaredFields action(Class<?> clazz)
/*    */   {
/* 21 */     return new GetDeclaredFields(clazz);
/*    */   }
/*    */   
/*    */   private GetDeclaredFields(Class<?> clazz) {
/* 25 */     this.clazz = clazz;
/*    */   }
/*    */   
/*    */   public Field[] run()
/*    */   {
/* 30 */     return this.clazz.getDeclaredFields();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\privilegedactions\GetDeclaredFields.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */