/*    */ package org.springframework.objenesis.instantiator.android;
/*    */ 
/*    */ import java.io.ObjectInputStream;
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
/*    */ @Instantiator(Typology.STANDARD)
/*    */ public class Android10Instantiator<T>
/*    */   implements ObjectInstantiator<T>
/*    */ {
/*    */   private final Class<T> type;
/*    */   private final Method newStaticMethod;
/*    */   
/*    */   public Android10Instantiator(Class<T> type)
/*    */   {
/* 39 */     this.type = type;
/* 40 */     this.newStaticMethod = getNewStaticMethod();
/*    */   }
/*    */   
/*    */   public T newInstance() {
/*    */     try {
/* 45 */       return (T)this.type.cast(this.newStaticMethod.invoke(null, new Object[] { this.type, Object.class }));
/*    */     }
/*    */     catch (Exception e) {
/* 48 */       throw new ObjenesisException(e);
/*    */     }
/*    */   }
/*    */   
/*    */   private static Method getNewStaticMethod() {
/*    */     try {
/* 54 */       Method newStaticMethod = ObjectInputStream.class.getDeclaredMethod("newInstance", new Class[] { Class.class, Class.class });
/*    */       
/* 56 */       newStaticMethod.setAccessible(true);
/* 57 */       return newStaticMethod;
/*    */     }
/*    */     catch (RuntimeException e) {
/* 60 */       throw new ObjenesisException(e);
/*    */     }
/*    */     catch (NoSuchMethodException e) {
/* 63 */       throw new ObjenesisException(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\objenesis\instantiator\android\Android10Instantiator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */