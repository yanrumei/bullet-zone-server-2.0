/*    */ package org.springframework.objenesis.instantiator.sun;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.objenesis.ObjenesisException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class SunReflectionFactoryHelper
/*    */ {
/*    */   public static <T> Constructor<T> newConstructorForSerialization(Class<T> type, Constructor<?> constructor)
/*    */   {
/* 37 */     Class<?> reflectionFactoryClass = getReflectionFactoryClass();
/* 38 */     Object reflectionFactory = createReflectionFactory(reflectionFactoryClass);
/*    */     
/* 40 */     Method newConstructorForSerializationMethod = getNewConstructorForSerializationMethod(reflectionFactoryClass);
/*    */     
/*    */     try
/*    */     {
/* 44 */       return (Constructor)newConstructorForSerializationMethod.invoke(reflectionFactory, new Object[] { type, constructor });
/*    */     }
/*    */     catch (IllegalArgumentException e)
/*    */     {
/* 48 */       throw new ObjenesisException(e);
/*    */     }
/*    */     catch (IllegalAccessException e) {
/* 51 */       throw new ObjenesisException(e);
/*    */     }
/*    */     catch (InvocationTargetException e) {
/* 54 */       throw new ObjenesisException(e);
/*    */     }
/*    */   }
/*    */   
/*    */   private static Class<?> getReflectionFactoryClass() {
/*    */     try {
/* 60 */       return Class.forName("sun.reflect.ReflectionFactory");
/*    */     }
/*    */     catch (ClassNotFoundException e) {
/* 63 */       throw new ObjenesisException(e);
/*    */     }
/*    */   }
/*    */   
/*    */   private static Object createReflectionFactory(Class<?> reflectionFactoryClass) {
/*    */     try {
/* 69 */       Method method = reflectionFactoryClass.getDeclaredMethod("getReflectionFactory", new Class[0]);
/*    */       
/* 71 */       return method.invoke(null, new Object[0]);
/*    */     }
/*    */     catch (NoSuchMethodException e) {
/* 74 */       throw new ObjenesisException(e);
/*    */     }
/*    */     catch (IllegalAccessException e) {
/* 77 */       throw new ObjenesisException(e);
/*    */     }
/*    */     catch (IllegalArgumentException e) {
/* 80 */       throw new ObjenesisException(e);
/*    */     }
/*    */     catch (InvocationTargetException e) {
/* 83 */       throw new ObjenesisException(e);
/*    */     }
/*    */   }
/*    */   
/*    */   private static Method getNewConstructorForSerializationMethod(Class<?> reflectionFactoryClass) {
/*    */     try {
/* 89 */       return reflectionFactoryClass.getDeclaredMethod("newConstructorForSerialization", new Class[] { Class.class, Constructor.class });
/*    */     }
/*    */     catch (NoSuchMethodException e)
/*    */     {
/* 93 */       throw new ObjenesisException(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\objenesis\instantiator\sun\SunReflectionFactoryHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */