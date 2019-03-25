/*    */ package org.springframework.objenesis.instantiator.sun;
/*    */ 
/*    */ import java.io.NotSerializableException;
/*    */ import java.lang.reflect.Constructor;
/*    */ import org.springframework.objenesis.ObjenesisException;
/*    */ import org.springframework.objenesis.instantiator.ObjectInstantiator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Instantiator(Typology.SERIALIZATION)
/*    */ public class SunReflectionFactorySerializationInstantiator<T>
/*    */   implements ObjectInstantiator<T>
/*    */ {
/*    */   private final Constructor<T> mungedConstructor;
/*    */   
/*    */   public SunReflectionFactorySerializationInstantiator(Class<T> type)
/*    */   {
/* 44 */     Class<? super T> nonSerializableAncestor = SerializationInstantiatorHelper.getNonSerializableSuperClass(type);
/*    */     
/*    */ 
/*    */     try
/*    */     {
/* 49 */       nonSerializableAncestorConstructor = nonSerializableAncestor.getDeclaredConstructor((Class[])null);
/*    */     } catch (NoSuchMethodException e) {
/*    */       Constructor<? super T> nonSerializableAncestorConstructor;
/* 52 */       throw new ObjenesisException(new NotSerializableException(type + " has no suitable superclass constructor"));
/*    */     }
/*    */     Constructor<? super T> nonSerializableAncestorConstructor;
/* 55 */     this.mungedConstructor = SunReflectionFactoryHelper.newConstructorForSerialization(type, nonSerializableAncestorConstructor);
/*    */     
/* 57 */     this.mungedConstructor.setAccessible(true);
/*    */   }
/*    */   
/*    */   public T newInstance() {
/*    */     try {
/* 62 */       return (T)this.mungedConstructor.newInstance((Object[])null);
/*    */     }
/*    */     catch (Exception e) {
/* 65 */       throw new ObjenesisException(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\objenesis\instantiator\sun\SunReflectionFactorySerializationInstantiator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */