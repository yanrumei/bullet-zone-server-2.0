/*    */ package org.hibernate.validator.internal.util.privilegedactions;
/*    */ 
/*    */ import java.security.PrivilegedAction;
/*    */ import org.hibernate.validator.internal.util.logging.Log;
/*    */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
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
/*    */ 
/*    */ 
/*    */ public final class NewInstance<T>
/*    */   implements PrivilegedAction<T>
/*    */ {
/* 22 */   private static final Log log = ;
/*    */   private final Class<T> clazz;
/*    */   private final String message;
/*    */   
/*    */   public static <T> NewInstance<T> action(Class<T> clazz, String message)
/*    */   {
/* 28 */     return new NewInstance(clazz, message);
/*    */   }
/*    */   
/*    */   private NewInstance(Class<T> clazz, String message) {
/* 32 */     this.clazz = clazz;
/* 33 */     this.message = message;
/*    */   }
/*    */   
/*    */   public T run()
/*    */   {
/*    */     try {
/* 39 */       return (T)this.clazz.newInstance();
/*    */     }
/*    */     catch (InstantiationException e) {
/* 42 */       throw log.getUnableToInstantiateException(this.message, this.clazz, e);
/*    */     }
/*    */     catch (IllegalAccessException e) {
/* 45 */       throw log.getUnableToInstantiateException(this.clazz, e);
/*    */     }
/*    */     catch (RuntimeException e) {
/* 48 */       throw log.getUnableToInstantiateException(this.clazz, e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\privilegedactions\NewInstance.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */