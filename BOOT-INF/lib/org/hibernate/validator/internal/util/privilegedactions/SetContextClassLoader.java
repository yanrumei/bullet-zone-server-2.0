/*    */ package org.hibernate.validator.internal.util.privilegedactions;
/*    */ 
/*    */ import java.security.PrivilegedAction;
/*    */ import org.hibernate.validator.internal.util.Contracts;
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
/*    */ public final class SetContextClassLoader
/*    */   implements PrivilegedAction<Void>
/*    */ {
/*    */   private final ClassLoader classLoader;
/*    */   
/*    */   public static SetContextClassLoader action(ClassLoader classLoader)
/*    */   {
/* 22 */     Contracts.assertNotNull(classLoader, "class loader must not be null");
/* 23 */     return new SetContextClassLoader(classLoader);
/*    */   }
/*    */   
/*    */   private SetContextClassLoader(ClassLoader classLoader) {
/* 27 */     this.classLoader = classLoader;
/*    */   }
/*    */   
/*    */   public Void run()
/*    */   {
/* 32 */     Thread.currentThread().setContextClassLoader(this.classLoader);
/* 33 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\privilegedactions\SetContextClassLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */