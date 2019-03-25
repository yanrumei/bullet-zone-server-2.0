/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtIncompatible
/*     */ public final class Primitives
/*     */ {
/*     */   private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER_TYPE;
/*     */   private static final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE_TYPE;
/*     */   
/*     */   static
/*     */   {
/*  45 */     Map<Class<?>, Class<?>> primToWrap = new HashMap(16);
/*  46 */     Map<Class<?>, Class<?>> wrapToPrim = new HashMap(16);
/*     */     
/*  48 */     add(primToWrap, wrapToPrim, Boolean.TYPE, Boolean.class);
/*  49 */     add(primToWrap, wrapToPrim, Byte.TYPE, Byte.class);
/*  50 */     add(primToWrap, wrapToPrim, Character.TYPE, Character.class);
/*  51 */     add(primToWrap, wrapToPrim, Double.TYPE, Double.class);
/*  52 */     add(primToWrap, wrapToPrim, Float.TYPE, Float.class);
/*  53 */     add(primToWrap, wrapToPrim, Integer.TYPE, Integer.class);
/*  54 */     add(primToWrap, wrapToPrim, Long.TYPE, Long.class);
/*  55 */     add(primToWrap, wrapToPrim, Short.TYPE, Short.class);
/*  56 */     add(primToWrap, wrapToPrim, Void.TYPE, Void.class);
/*     */     
/*  58 */     PRIMITIVE_TO_WRAPPER_TYPE = Collections.unmodifiableMap(primToWrap);
/*  59 */     WRAPPER_TO_PRIMITIVE_TYPE = Collections.unmodifiableMap(wrapToPrim);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void add(Map<Class<?>, Class<?>> forward, Map<Class<?>, Class<?>> backward, Class<?> key, Class<?> value)
/*     */   {
/*  67 */     forward.put(key, value);
/*  68 */     backward.put(value, key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Set<Class<?>> allPrimitiveTypes()
/*     */   {
/*  79 */     return PRIMITIVE_TO_WRAPPER_TYPE.keySet();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Set<Class<?>> allWrapperTypes()
/*     */   {
/*  88 */     return WRAPPER_TO_PRIMITIVE_TYPE.keySet();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isWrapperType(Class<?> type)
/*     */   {
/*  98 */     return WRAPPER_TO_PRIMITIVE_TYPE.containsKey(Preconditions.checkNotNull(type));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> Class<T> wrap(Class<T> type)
/*     */   {
/* 112 */     Preconditions.checkNotNull(type);
/*     */     
/*     */ 
/*     */ 
/* 116 */     Class<T> wrapped = (Class)PRIMITIVE_TO_WRAPPER_TYPE.get(type);
/* 117 */     return wrapped == null ? type : wrapped;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> Class<T> unwrap(Class<T> type)
/*     */   {
/* 131 */     Preconditions.checkNotNull(type);
/*     */     
/*     */ 
/*     */ 
/* 135 */     Class<T> unwrapped = (Class)WRAPPER_TO_PRIMITIVE_TYPE.get(type);
/* 136 */     return unwrapped == null ? type : unwrapped;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\primitives\Primitives.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */