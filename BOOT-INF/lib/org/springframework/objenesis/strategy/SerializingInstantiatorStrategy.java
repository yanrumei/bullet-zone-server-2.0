/*    */ package org.springframework.objenesis.strategy;
/*    */ 
/*    */ import java.io.NotSerializableException;
/*    */ import java.io.Serializable;
/*    */ import org.springframework.objenesis.ObjenesisException;
/*    */ import org.springframework.objenesis.instantiator.ObjectInstantiator;
/*    */ import org.springframework.objenesis.instantiator.android.AndroidSerializationInstantiator;
/*    */ import org.springframework.objenesis.instantiator.basic.ObjectInputStreamInstantiator;
/*    */ import org.springframework.objenesis.instantiator.basic.ObjectStreamClassInstantiator;
/*    */ import org.springframework.objenesis.instantiator.gcj.GCJSerializationInstantiator;
/*    */ import org.springframework.objenesis.instantiator.perc.PercSerializationInstantiator;
/*    */ import org.springframework.objenesis.instantiator.sun.SunReflectionFactorySerializationInstantiator;
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
/*    */ public class SerializingInstantiatorStrategy
/*    */   extends BaseInstantiatorStrategy
/*    */ {
/*    */   public <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> type)
/*    */   {
/* 57 */     if (!Serializable.class.isAssignableFrom(type)) {
/* 58 */       throw new ObjenesisException(new NotSerializableException(type + " not serializable"));
/*    */     }
/* 60 */     if ((PlatformDescription.JVM_NAME.startsWith("Java HotSpot")) || (PlatformDescription.isThisJVM("OpenJDK")))
/*    */     {
/* 62 */       if ((PlatformDescription.isGoogleAppEngine()) && (PlatformDescription.SPECIFICATION_VERSION.equals("1.7"))) {
/* 63 */         return new ObjectInputStreamInstantiator(type);
/*    */       }
/* 65 */       return new SunReflectionFactorySerializationInstantiator(type);
/*    */     }
/* 67 */     if (PlatformDescription.JVM_NAME.startsWith("Dalvik")) {
/* 68 */       if (PlatformDescription.isAndroidOpenJDK()) {
/* 69 */         return new ObjectStreamClassInstantiator(type);
/*    */       }
/* 71 */       return new AndroidSerializationInstantiator(type);
/*    */     }
/* 73 */     if (PlatformDescription.JVM_NAME.startsWith("GNU libgcj")) {
/* 74 */       return new GCJSerializationInstantiator(type);
/*    */     }
/* 76 */     if (PlatformDescription.JVM_NAME.startsWith("PERC")) {
/* 77 */       return new PercSerializationInstantiator(type);
/*    */     }
/*    */     
/* 80 */     return new SunReflectionFactorySerializationInstantiator(type);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\objenesis\strategy\SerializingInstantiatorStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */