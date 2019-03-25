/*    */ package org.springframework.objenesis.instantiator.gcj;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.objenesis.ObjenesisException;
/*    */ import org.springframework.objenesis.instantiator.SerializationInstantiatorHelper;
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
/*    */ @Instantiator(Typology.SERIALIZATION)
/*    */ public class GCJSerializationInstantiator<T>
/*    */   extends GCJInstantiatorBase<T>
/*    */ {
/*    */   private Class<? super T> superType;
/*    */   
/*    */   public GCJSerializationInstantiator(Class<T> type)
/*    */   {
/* 36 */     super(type);
/* 37 */     this.superType = SerializationInstantiatorHelper.getNonSerializableSuperClass(type);
/*    */   }
/*    */   
/*    */   public T newInstance()
/*    */   {
/*    */     try {
/* 43 */       return (T)this.type.cast(newObjectMethod.invoke(dummyStream, new Object[] { this.type, this.superType }));
/*    */     }
/*    */     catch (Exception e) {
/* 46 */       throw new ObjenesisException(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\objenesis\instantiator\gcj\GCJSerializationInstantiator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */