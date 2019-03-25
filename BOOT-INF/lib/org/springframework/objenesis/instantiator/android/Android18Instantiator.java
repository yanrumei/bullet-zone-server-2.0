/*    */ package org.springframework.objenesis.instantiator.android;
/*    */ 
/*    */ import java.io.ObjectStreamClass;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.objenesis.ObjenesisException;
/*    */ import org.springframework.objenesis.instantiator.ObjectInstantiator;
/*    */ import org.springframework.objenesis.instantiator.annotations.Instantiator;
/*    */ import org.springframework.objenesis.instantiator.annotations.Typology;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Instantiator(Typology.STANDARD)
/*    */ public class Android18Instantiator<T>
/*    */   implements ObjectInstantiator<T>
/*    */ {
/*    */   private final Class<T> type;
/*    */   private final Method newInstanceMethod;
/*    */   private final Long objectConstructorId;
/*    */   
/*    */   public Android18Instantiator(Class<T> type)
/*    */   {
/* 40 */     this.type = type;
/* 41 */     this.newInstanceMethod = getNewInstanceMethod();
/* 42 */     this.objectConstructorId = findConstructorIdForJavaLangObjectConstructor();
/*    */   }
/*    */   
/*    */   public T newInstance() {
/*    */     try {
/* 47 */       return (T)this.type.cast(this.newInstanceMethod.invoke(null, new Object[] { this.type, this.objectConstructorId }));
/*    */     }
/*    */     catch (Exception e) {
/* 50 */       throw new ObjenesisException(e);
/*    */     }
/*    */   }
/*    */   
/*    */   private static Method getNewInstanceMethod() {
/*    */     try {
/* 56 */       Method newInstanceMethod = ObjectStreamClass.class.getDeclaredMethod("newInstance", new Class[] { Class.class, Long.TYPE });
/*    */       
/* 58 */       newInstanceMethod.setAccessible(true);
/* 59 */       return newInstanceMethod;
/*    */     }
/*    */     catch (RuntimeException e) {
/* 62 */       throw new ObjenesisException(e);
/*    */     }
/*    */     catch (NoSuchMethodException e) {
/* 65 */       throw new ObjenesisException(e);
/*    */     }
/*    */   }
/*    */   
/*    */   private static Long findConstructorIdForJavaLangObjectConstructor() {
/*    */     try {
/* 71 */       Method newInstanceMethod = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", new Class[] { Class.class });
/*    */       
/* 73 */       newInstanceMethod.setAccessible(true);
/*    */       
/* 75 */       return (Long)newInstanceMethod.invoke(null, new Object[] { Object.class });
/*    */     }
/*    */     catch (RuntimeException e) {
/* 78 */       throw new ObjenesisException(e);
/*    */     }
/*    */     catch (NoSuchMethodException e) {
/* 81 */       throw new ObjenesisException(e);
/*    */     }
/*    */     catch (IllegalAccessException e) {
/* 84 */       throw new ObjenesisException(e);
/*    */     }
/*    */     catch (InvocationTargetException e) {
/* 87 */       throw new ObjenesisException(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\objenesis\instantiator\android\Android18Instantiator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */