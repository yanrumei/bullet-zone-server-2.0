/*    */ package org.springframework.objenesis;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public final class ObjenesisHelper
/*    */ {
/* 29 */   private static final Objenesis OBJENESIS_STD = new ObjenesisStd();
/*    */   
/* 31 */   private static final Objenesis OBJENESIS_SERIALIZER = new ObjenesisSerializer();
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
/*    */   public static <T> T newInstance(Class<T> clazz)
/*    */   {
/* 44 */     return (T)OBJENESIS_STD.newInstance(clazz);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static <T extends Serializable> T newSerializableInstance(Class<T> clazz)
/*    */   {
/* 56 */     return (Serializable)OBJENESIS_SERIALIZER.newInstance(clazz);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static <T> ObjectInstantiator<T> getInstantiatorOf(Class<T> clazz)
/*    */   {
/* 69 */     return OBJENESIS_STD.getInstantiatorOf(clazz);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static <T extends Serializable> ObjectInstantiator<T> getSerializableObjectInstantiatorOf(Class<T> clazz)
/*    */   {
/* 82 */     return OBJENESIS_SERIALIZER.getInstantiatorOf(clazz);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\objenesis\ObjenesisHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */