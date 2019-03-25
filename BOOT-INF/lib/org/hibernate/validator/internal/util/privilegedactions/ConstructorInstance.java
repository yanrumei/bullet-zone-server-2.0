/*    */ package org.hibernate.validator.internal.util.privilegedactions;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.InvocationTargetException;
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
/*    */ 
/*    */ 
/*    */ public final class ConstructorInstance<T>
/*    */   implements PrivilegedAction<T>
/*    */ {
/* 26 */   private static final Log log = ;
/*    */   private final Constructor<T> constructor;
/*    */   private final Object[] initArgs;
/*    */   
/*    */   public static <T> ConstructorInstance<T> action(Constructor<T> constructor, Object... initArgs)
/*    */   {
/* 32 */     return new ConstructorInstance(constructor, initArgs);
/*    */   }
/*    */   
/*    */   private ConstructorInstance(Constructor<T> constructor, Object... initArgs) {
/* 36 */     this.constructor = constructor;
/* 37 */     this.initArgs = initArgs;
/*    */   }
/*    */   
/*    */   public T run()
/*    */   {
/*    */     try {
/* 43 */       return (T)this.constructor.newInstance(this.initArgs);
/*    */     }
/*    */     catch (InstantiationException e) {
/* 46 */       throw log.getUnableToInstantiateException(this.constructor.getName(), e);
/*    */     }
/*    */     catch (IllegalAccessException e) {
/* 49 */       throw log.getUnableToInstantiateException(this.constructor.getName(), e);
/*    */     }
/*    */     catch (InvocationTargetException e) {
/* 52 */       throw log.getUnableToInstantiateException(this.constructor.getName(), e);
/*    */     }
/*    */     catch (RuntimeException e) {
/* 55 */       throw log.getUnableToInstantiateException(this.constructor.getName(), e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\privilegedactions\ConstructorInstance.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */