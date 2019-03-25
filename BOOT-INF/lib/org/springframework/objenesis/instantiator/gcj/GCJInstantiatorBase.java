/*    */ package org.springframework.objenesis.instantiator.gcj;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.lang.reflect.Method;
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
/*    */ public abstract class GCJInstantiatorBase<T>
/*    */   implements ObjectInstantiator<T>
/*    */ {
/* 33 */   static Method newObjectMethod = null;
/*    */   
/*    */   static ObjectInputStream dummyStream;
/*    */   
/*    */   protected final Class<T> type;
/*    */   
/*    */ 
/*    */   private static void initialize()
/*    */   {
/* 42 */     if (newObjectMethod == null) {
/*    */       try {
/* 44 */         newObjectMethod = ObjectInputStream.class.getDeclaredMethod("newObject", new Class[] { Class.class, Class.class });
/*    */         
/* 46 */         newObjectMethod.setAccessible(true);
/* 47 */         dummyStream = new DummyStream();
/*    */       }
/*    */       catch (RuntimeException e) {
/* 50 */         throw new ObjenesisException(e);
/*    */       }
/*    */       catch (NoSuchMethodException e) {
/* 53 */         throw new ObjenesisException(e);
/*    */       }
/*    */       catch (IOException e) {
/* 56 */         throw new ObjenesisException(e);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public GCJInstantiatorBase(Class<T> type)
/*    */   {
/* 64 */     this.type = type;
/* 65 */     initialize();
/*    */   }
/*    */   
/*    */   public abstract T newInstance();
/*    */   
/*    */   private static class DummyStream
/*    */     extends ObjectInputStream
/*    */   {
/*    */     public DummyStream()
/*    */       throws IOException
/*    */     {}
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\objenesis\instantiator\gcj\GCJInstantiatorBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */