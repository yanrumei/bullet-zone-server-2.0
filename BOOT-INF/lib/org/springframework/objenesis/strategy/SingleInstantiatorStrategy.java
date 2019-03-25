/*    */ package org.springframework.objenesis.strategy;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import org.springframework.objenesis.ObjenesisException;
/*    */ import org.springframework.objenesis.instantiator.ObjectInstantiator;
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
/*    */ 
/*    */ public class SingleInstantiatorStrategy
/*    */   implements InstantiatorStrategy
/*    */ {
/*    */   private Constructor<?> constructor;
/*    */   
/*    */   public <T extends ObjectInstantiator<?>> SingleInstantiatorStrategy(Class<T> instantiator)
/*    */   {
/*    */     try
/*    */     {
/* 43 */       this.constructor = instantiator.getConstructor(new Class[] { Class.class });
/*    */     }
/*    */     catch (NoSuchMethodException e) {
/* 46 */       throw new ObjenesisException(e);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> type)
/*    */   {
/*    */     try
/*    */     {
/* 61 */       return (ObjectInstantiator)this.constructor.newInstance(new Object[] { type });
/*    */     } catch (InstantiationException e) {
/* 63 */       throw new ObjenesisException(e);
/*    */     } catch (IllegalAccessException e) {
/* 65 */       throw new ObjenesisException(e);
/*    */     } catch (InvocationTargetException e) {
/* 67 */       throw new ObjenesisException(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\objenesis\strategy\SingleInstantiatorStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */