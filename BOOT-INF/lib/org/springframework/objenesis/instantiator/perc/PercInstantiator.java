/*    */ package org.springframework.objenesis.instantiator.perc;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Instantiator(Typology.STANDARD)
/*    */ public class PercInstantiator<T>
/*    */   implements ObjectInstantiator<T>
/*    */ {
/*    */   private final Method newInstanceMethod;
/* 39 */   private final Object[] typeArgs = { null, Boolean.FALSE };
/*    */   
/*    */   public PercInstantiator(Class<T> type)
/*    */   {
/* 43 */     this.typeArgs[0] = type;
/*    */     try
/*    */     {
/* 46 */       this.newInstanceMethod = ObjectInputStream.class.getDeclaredMethod("newInstance", new Class[] { Class.class, Boolean.TYPE });
/*    */       
/* 48 */       this.newInstanceMethod.setAccessible(true);
/*    */     }
/*    */     catch (RuntimeException e) {
/* 51 */       throw new ObjenesisException(e);
/*    */     }
/*    */     catch (NoSuchMethodException e) {
/* 54 */       throw new ObjenesisException(e);
/*    */     }
/*    */   }
/*    */   
/*    */   public T newInstance()
/*    */   {
/*    */     try {
/* 61 */       return (T)this.newInstanceMethod.invoke(null, this.typeArgs);
/*    */     } catch (Exception e) {
/* 63 */       throw new ObjenesisException(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\objenesis\instantiator\perc\PercInstantiator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */