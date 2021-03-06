/*    */ package org.springframework.objenesis.instantiator.basic;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
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
/*    */ @Instantiator(Typology.NOT_COMPLIANT)
/*    */ public class ConstructorInstantiator<T>
/*    */   implements ObjectInstantiator<T>
/*    */ {
/*    */   protected Constructor<T> constructor;
/*    */   
/*    */   public ConstructorInstantiator(Class<T> type)
/*    */   {
/*    */     try
/*    */     {
/* 40 */       this.constructor = type.getDeclaredConstructor((Class[])null);
/*    */     }
/*    */     catch (Exception e) {
/* 43 */       throw new ObjenesisException(e);
/*    */     }
/*    */   }
/*    */   
/*    */   public T newInstance() {
/*    */     try {
/* 49 */       return (T)this.constructor.newInstance((Object[])null);
/*    */     }
/*    */     catch (Exception e) {
/* 52 */       throw new ObjenesisException(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\objenesis\instantiator\basic\ConstructorInstantiator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */