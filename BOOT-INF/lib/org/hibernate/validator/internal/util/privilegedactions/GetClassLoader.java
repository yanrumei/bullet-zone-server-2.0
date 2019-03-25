/*    */ package org.hibernate.validator.internal.util.privilegedactions;
/*    */ 
/*    */ import java.security.PrivilegedAction;
/*    */ import org.hibernate.validator.internal.util.Contracts;
/*    */ import org.hibernate.validator.internal.util.logging.Messages;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class GetClassLoader
/*    */   implements PrivilegedAction<ClassLoader>
/*    */ {
/*    */   private final Class<?> clazz;
/*    */   
/*    */   public static GetClassLoader fromContext()
/*    */   {
/* 22 */     return new GetClassLoader(null);
/*    */   }
/*    */   
/*    */   public static GetClassLoader fromClass(Class<?> clazz) {
/* 26 */     Contracts.assertNotNull(clazz, Messages.MESSAGES.classIsNull());
/* 27 */     return new GetClassLoader(clazz);
/*    */   }
/*    */   
/*    */   private GetClassLoader(Class<?> clazz) {
/* 31 */     this.clazz = clazz;
/*    */   }
/*    */   
/*    */   public ClassLoader run()
/*    */   {
/* 36 */     if (this.clazz != null) {
/* 37 */       return this.clazz.getClassLoader();
/*    */     }
/*    */     
/* 40 */     return Thread.currentThread().getContextClassLoader();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\privilegedactions\GetClassLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */