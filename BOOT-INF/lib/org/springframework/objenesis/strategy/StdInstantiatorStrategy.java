/*    */ package org.springframework.objenesis.strategy;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.springframework.objenesis.instantiator.ObjectInstantiator;
/*    */ import org.springframework.objenesis.instantiator.android.Android10Instantiator;
/*    */ import org.springframework.objenesis.instantiator.android.Android17Instantiator;
/*    */ import org.springframework.objenesis.instantiator.android.Android18Instantiator;
/*    */ import org.springframework.objenesis.instantiator.basic.AccessibleInstantiator;
/*    */ import org.springframework.objenesis.instantiator.basic.ObjectInputStreamInstantiator;
/*    */ import org.springframework.objenesis.instantiator.gcj.GCJInstantiator;
/*    */ import org.springframework.objenesis.instantiator.perc.PercInstantiator;
/*    */ import org.springframework.objenesis.instantiator.sun.SunReflectionFactoryInstantiator;
/*    */ import org.springframework.objenesis.instantiator.sun.UnsafeFactoryInstantiator;
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
/*    */ public class StdInstantiatorStrategy
/*    */   extends BaseInstantiatorStrategy
/*    */ {
/*    */   public <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> type)
/*    */   {
/* 58 */     if ((PlatformDescription.isThisJVM("Java HotSpot")) || (PlatformDescription.isThisJVM("OpenJDK")))
/*    */     {
/* 60 */       if ((PlatformDescription.isGoogleAppEngine()) && (PlatformDescription.SPECIFICATION_VERSION.equals("1.7"))) {
/* 61 */         if (Serializable.class.isAssignableFrom(type)) {
/* 62 */           return new ObjectInputStreamInstantiator(type);
/*    */         }
/* 64 */         return new AccessibleInstantiator(type);
/*    */       }
/*    */       
/*    */ 
/* 68 */       return new SunReflectionFactoryInstantiator(type);
/*    */     }
/* 70 */     if (PlatformDescription.isThisJVM("Dalvik")) {
/* 71 */       if (PlatformDescription.isAndroidOpenJDK())
/*    */       {
/* 73 */         return new UnsafeFactoryInstantiator(type);
/*    */       }
/* 75 */       if (PlatformDescription.ANDROID_VERSION <= 10)
/*    */       {
/* 77 */         return new Android10Instantiator(type);
/*    */       }
/* 79 */       if (PlatformDescription.ANDROID_VERSION <= 17)
/*    */       {
/* 81 */         return new Android17Instantiator(type);
/*    */       }
/*    */       
/* 84 */       return new Android18Instantiator(type);
/*    */     }
/* 86 */     if (PlatformDescription.isThisJVM("BEA"))
/*    */     {
/* 88 */       return new SunReflectionFactoryInstantiator(type);
/*    */     }
/* 90 */     if (PlatformDescription.isThisJVM("GNU libgcj")) {
/* 91 */       return new GCJInstantiator(type);
/*    */     }
/* 93 */     if (PlatformDescription.isThisJVM("PERC")) {
/* 94 */       return new PercInstantiator(type);
/*    */     }
/*    */     
/*    */ 
/* 98 */     return new UnsafeFactoryInstantiator(type);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\objenesis\strategy\StdInstantiatorStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */