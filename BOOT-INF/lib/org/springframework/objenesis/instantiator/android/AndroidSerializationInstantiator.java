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
/*    */ 
/*    */ @Instantiator(Typology.SERIALIZATION)
/*    */ public class AndroidSerializationInstantiator<T>
/*    */   implements ObjectInstantiator<T>
/*    */ {
/*    */   private final Class<T> type;
/*    */   private final ObjectStreamClass objectStreamClass;
/*    */   private final Method newInstanceMethod;
/*    */   
/*    */   public AndroidSerializationInstantiator(Class<T> type)
/*    */   {
/* 41 */     this.type = type;
/* 42 */     this.newInstanceMethod = getNewInstanceMethod();
/* 43 */     Method m = null;
/*    */     try {
/* 45 */       m = ObjectStreamClass.class.getMethod("lookupAny", new Class[] { Class.class });
/*    */     } catch (NoSuchMethodException e) {
/* 47 */       throw new ObjenesisException(e);
/*    */     }
/*    */     try {
/* 50 */       this.objectStreamClass = ((ObjectStreamClass)m.invoke(null, new Object[] { type }));
/*    */     } catch (IllegalAccessException e) {
/* 52 */       throw new ObjenesisException(e);
/*    */     } catch (InvocationTargetException e) {
/* 54 */       throw new ObjenesisException(e);
/*    */     }
/*    */   }
/*    */   
/*    */   public T newInstance() {
/*    */     try {
/* 60 */       return (T)this.type.cast(this.newInstanceMethod.invoke(this.objectStreamClass, new Object[] { this.type }));
/*    */     }
/*    */     catch (IllegalAccessException e) {
/* 63 */       throw new ObjenesisException(e);
/*    */     }
/*    */     catch (IllegalArgumentException e) {
/* 66 */       throw new ObjenesisException(e);
/*    */     }
/*    */     catch (InvocationTargetException e) {
/* 69 */       throw new ObjenesisException(e);
/*    */     }
/*    */   }
/*    */   
/*    */   private static Method getNewInstanceMethod() {
/*    */     try {
/* 75 */       Method newInstanceMethod = ObjectStreamClass.class.getDeclaredMethod("newInstance", new Class[] { Class.class });
/*    */       
/* 77 */       newInstanceMethod.setAccessible(true);
/* 78 */       return newInstanceMethod;
/*    */     }
/*    */     catch (RuntimeException e) {
/* 81 */       throw new ObjenesisException(e);
/*    */     }
/*    */     catch (NoSuchMethodException e) {
/* 84 */       throw new ObjenesisException(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\objenesis\instantiator\android\AndroidSerializationInstantiator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */