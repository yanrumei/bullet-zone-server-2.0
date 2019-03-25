/*    */ package com.google.common.base;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import javax.annotation.Nullable;
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
/*    */ @GwtIncompatible
/*    */ public final class Defaults
/*    */ {
/*    */   private static final Map<Class<?>, Object> DEFAULTS;
/*    */   
/*    */   static
/*    */   {
/* 39 */     Map<Class<?>, Object> map = new HashMap();
/* 40 */     put(map, Boolean.TYPE, Boolean.valueOf(false));
/* 41 */     put(map, Character.TYPE, Character.valueOf('\000'));
/* 42 */     put(map, Byte.TYPE, Byte.valueOf((byte)0));
/* 43 */     put(map, Short.TYPE, Short.valueOf((short)0));
/* 44 */     put(map, Integer.TYPE, Integer.valueOf(0));
/* 45 */     put(map, Long.TYPE, Long.valueOf(0L));
/* 46 */     put(map, Float.TYPE, Float.valueOf(0.0F));
/* 47 */     put(map, Double.TYPE, Double.valueOf(0.0D));
/* 48 */     DEFAULTS = Collections.unmodifiableMap(map);
/*    */   }
/*    */   
/*    */   private static <T> void put(Map<Class<?>, Object> map, Class<T> type, T value) {
/* 52 */     map.put(type, value);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   @Nullable
/*    */   public static <T> T defaultValue(Class<T> type)
/*    */   {
/* 64 */     T t = DEFAULTS.get(Preconditions.checkNotNull(type));
/* 65 */     return t;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\base\Defaults.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */