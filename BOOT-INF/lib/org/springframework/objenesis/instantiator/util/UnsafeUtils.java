/*    */ package org.springframework.objenesis.instantiator.util;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import org.springframework.objenesis.ObjenesisException;
/*    */ import sun.misc.Unsafe;
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
/*    */ public final class UnsafeUtils
/*    */ {
/*    */   private static final Unsafe unsafe;
/*    */   
/*    */   static
/*    */   {
/*    */     try
/*    */     {
/* 36 */       f = Unsafe.class.getDeclaredField("theUnsafe");
/*    */     } catch (NoSuchFieldException e) { Field f;
/* 38 */       throw new ObjenesisException(e); }
/*    */     Field f;
/* 40 */     f.setAccessible(true);
/*    */     try {
/* 42 */       unsafe = (Unsafe)f.get(null);
/*    */     } catch (IllegalAccessException e) {
/* 44 */       throw new ObjenesisException(e);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public static Unsafe getUnsafe()
/*    */   {
/* 51 */     return unsafe;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\objenesis\instantiato\\util\UnsafeUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */