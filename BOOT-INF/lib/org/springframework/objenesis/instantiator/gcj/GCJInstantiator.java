/*    */ package org.springframework.objenesis.instantiator.gcj;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.objenesis.ObjenesisException;
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
/*    */ public class GCJInstantiator<T>
/*    */   extends GCJInstantiatorBase<T>
/*    */ {
/*    */   public GCJInstantiator(Class<T> type)
/*    */   {
/* 34 */     super(type);
/*    */   }
/*    */   
/*    */   public T newInstance()
/*    */   {
/*    */     try {
/* 40 */       return (T)this.type.cast(newObjectMethod.invoke(dummyStream, new Object[] { this.type, Object.class }));
/*    */     }
/*    */     catch (RuntimeException e) {
/* 43 */       throw new ObjenesisException(e);
/*    */     }
/*    */     catch (IllegalAccessException e) {
/* 46 */       throw new ObjenesisException(e);
/*    */     }
/*    */     catch (InvocationTargetException e) {
/* 49 */       throw new ObjenesisException(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\objenesis\instantiator\gcj\GCJInstantiator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */