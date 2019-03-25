/*    */ package org.springframework.objenesis.instantiator.basic;
/*    */ 
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
/*    */ @Instantiator(Typology.NOT_COMPLIANT)
/*    */ public class NewInstanceInstantiator<T>
/*    */   implements ObjectInstantiator<T>
/*    */ {
/*    */   private final Class<T> type;
/*    */   
/*    */   public NewInstanceInstantiator(Class<T> type)
/*    */   {
/* 36 */     this.type = type;
/*    */   }
/*    */   
/*    */   public T newInstance() {
/*    */     try {
/* 41 */       return (T)this.type.newInstance();
/*    */     }
/*    */     catch (Exception e) {
/* 44 */       throw new ObjenesisException(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\objenesis\instantiator\basic\NewInstanceInstantiator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */