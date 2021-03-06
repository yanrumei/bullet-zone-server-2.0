/*    */ package org.springframework.objenesis.instantiator.basic;
/*    */ 
/*    */ import java.io.ObjectStreamClass;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @Instantiator(Typology.SERIALIZATION)
/*    */ public class ObjectStreamClassInstantiator<T>
/*    */   implements ObjectInstantiator<T>
/*    */ {
/*    */   private static Method newInstanceMethod;
/*    */   private final ObjectStreamClass objStreamClass;
/*    */   
/*    */   private static void initialize()
/*    */   {
/* 42 */     if (newInstanceMethod == null) {
/*    */       try {
/* 44 */         newInstanceMethod = ObjectStreamClass.class.getDeclaredMethod("newInstance", new Class[0]);
/* 45 */         newInstanceMethod.setAccessible(true);
/*    */       }
/*    */       catch (RuntimeException e) {
/* 48 */         throw new ObjenesisException(e);
/*    */       }
/*    */       catch (NoSuchMethodException e) {
/* 51 */         throw new ObjenesisException(e);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public ObjectStreamClassInstantiator(Class<T> type)
/*    */   {
/* 59 */     initialize();
/* 60 */     this.objStreamClass = ObjectStreamClass.lookup(type);
/*    */   }
/*    */   
/*    */   public T newInstance()
/*    */   {
/*    */     try
/*    */     {
/* 67 */       return (T)newInstanceMethod.invoke(this.objStreamClass, new Object[0]);
/*    */     }
/*    */     catch (Exception e) {
/* 70 */       throw new ObjenesisException(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\objenesis\instantiator\basic\ObjectStreamClassInstantiator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */