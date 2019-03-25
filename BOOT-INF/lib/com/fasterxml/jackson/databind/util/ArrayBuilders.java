/*     */ package com.fasterxml.jackson.databind.util;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ArrayBuilders
/*     */ {
/*  17 */   private BooleanBuilder _booleanBuilder = null;
/*     */   
/*     */ 
/*     */ 
/*  21 */   private ByteBuilder _byteBuilder = null;
/*  22 */   private ShortBuilder _shortBuilder = null;
/*  23 */   private IntBuilder _intBuilder = null;
/*  24 */   private LongBuilder _longBuilder = null;
/*     */   
/*  26 */   private FloatBuilder _floatBuilder = null;
/*  27 */   private DoubleBuilder _doubleBuilder = null;
/*     */   
/*     */ 
/*     */ 
/*     */   public BooleanBuilder getBooleanBuilder()
/*     */   {
/*  33 */     if (this._booleanBuilder == null) {
/*  34 */       this._booleanBuilder = new BooleanBuilder();
/*     */     }
/*  36 */     return this._booleanBuilder;
/*     */   }
/*     */   
/*     */   public ByteBuilder getByteBuilder()
/*     */   {
/*  41 */     if (this._byteBuilder == null) {
/*  42 */       this._byteBuilder = new ByteBuilder();
/*     */     }
/*  44 */     return this._byteBuilder;
/*     */   }
/*     */   
/*     */   public ShortBuilder getShortBuilder() {
/*  48 */     if (this._shortBuilder == null) {
/*  49 */       this._shortBuilder = new ShortBuilder();
/*     */     }
/*  51 */     return this._shortBuilder;
/*     */   }
/*     */   
/*     */   public IntBuilder getIntBuilder() {
/*  55 */     if (this._intBuilder == null) {
/*  56 */       this._intBuilder = new IntBuilder();
/*     */     }
/*  58 */     return this._intBuilder;
/*     */   }
/*     */   
/*     */   public LongBuilder getLongBuilder() {
/*  62 */     if (this._longBuilder == null) {
/*  63 */       this._longBuilder = new LongBuilder();
/*     */     }
/*  65 */     return this._longBuilder;
/*     */   }
/*     */   
/*     */   public FloatBuilder getFloatBuilder()
/*     */   {
/*  70 */     if (this._floatBuilder == null) {
/*  71 */       this._floatBuilder = new FloatBuilder();
/*     */     }
/*  73 */     return this._floatBuilder;
/*     */   }
/*     */   
/*     */   public DoubleBuilder getDoubleBuilder() {
/*  77 */     if (this._doubleBuilder == null) {
/*  78 */       this._doubleBuilder = new DoubleBuilder();
/*     */     }
/*  80 */     return this._doubleBuilder;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final class BooleanBuilder
/*     */     extends PrimitiveArrayBuilder<boolean[]>
/*     */   {
/*     */     public final boolean[] _constructArray(int len)
/*     */     {
/*  94 */       return new boolean[len];
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ByteBuilder extends PrimitiveArrayBuilder<byte[]>
/*     */   {
/*     */     public final byte[] _constructArray(int len)
/*     */     {
/* 102 */       return new byte[len];
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ShortBuilder extends PrimitiveArrayBuilder<short[]>
/*     */   {
/*     */     public final short[] _constructArray(int len) {
/* 109 */       return new short[len];
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class IntBuilder extends PrimitiveArrayBuilder<int[]>
/*     */   {
/*     */     public final int[] _constructArray(int len) {
/* 116 */       return new int[len];
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class LongBuilder extends PrimitiveArrayBuilder<long[]>
/*     */   {
/*     */     public final long[] _constructArray(int len) {
/* 123 */       return new long[len];
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class FloatBuilder extends PrimitiveArrayBuilder<float[]>
/*     */   {
/*     */     public final float[] _constructArray(int len)
/*     */     {
/* 131 */       return new float[len];
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class DoubleBuilder extends PrimitiveArrayBuilder<double[]>
/*     */   {
/*     */     public final double[] _constructArray(int len) {
/* 138 */       return new double[len];
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Object getArrayComparator(final Object defaultValue)
/*     */   {
/* 159 */     final int length = Array.getLength(defaultValue);
/* 160 */     Class<?> defaultValueType = defaultValue.getClass();
/* 161 */     new Object()
/*     */     {
/*     */       public boolean equals(Object other) {
/* 164 */         if (other == this) return true;
/* 165 */         if ((other == null) || (other.getClass() != this.val$defaultValueType)) {
/* 166 */           return false;
/*     */         }
/* 168 */         if (Array.getLength(other) != length) { return false;
/*     */         }
/* 170 */         for (int i = 0; i < length; i++) {
/* 171 */           Object value1 = Array.get(defaultValue, i);
/* 172 */           Object value2 = Array.get(other, i);
/* 173 */           if ((value1 != value2) && 
/* 174 */             (value1 != null) && 
/* 175 */             (!value1.equals(value2))) {
/* 176 */             return false;
/*     */           }
/*     */         }
/*     */         
/* 180 */         return true;
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   public static <T> HashSet<T> arrayToSet(T[] elements)
/*     */   {
/* 187 */     HashSet<T> result = new HashSet();
/* 188 */     if (elements != null) {
/* 189 */       for (T elem : elements) {
/* 190 */         result.add(elem);
/*     */       }
/*     */     }
/* 193 */     return result;
/*     */   }
/*     */   
/*     */   public static <T> ArrayList<T> arrayToList(T[] elements)
/*     */   {
/* 198 */     ArrayList<T> result = new ArrayList();
/* 199 */     if (elements != null) {
/* 200 */       for (T elem : elements) {
/* 201 */         result.add(elem);
/*     */       }
/*     */     }
/* 204 */     return result;
/*     */   }
/*     */   
/*     */   public static <T> HashSet<T> setAndArray(Set<T> set, T[] elements)
/*     */   {
/* 209 */     HashSet<T> result = new HashSet();
/* 210 */     if (set != null) {
/* 211 */       result.addAll(set);
/*     */     }
/* 213 */     if (elements != null) {
/* 214 */       for (T value : elements) {
/* 215 */         result.add(value);
/*     */       }
/*     */     }
/* 218 */     return result;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> List<T> addToList(List<T> list, T element)
/*     */   {
/* 235 */     if (list == null) {
/* 236 */       list = new ArrayList();
/*     */     }
/* 238 */     list.add(element);
/* 239 */     return list;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> T[] insertInList(T[] array, T element)
/*     */   {
/* 249 */     int len = array.length;
/*     */     
/* 251 */     T[] result = (Object[])Array.newInstance(array.getClass().getComponentType(), len + 1);
/* 252 */     if (len > 0) {
/* 253 */       System.arraycopy(array, 0, result, 1, len);
/*     */     }
/* 255 */     result[0] = element;
/* 256 */     return result;
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
/*     */   public static <T> T[] insertInListNoDup(T[] array, T element)
/*     */   {
/* 270 */     int len = array.length;
/*     */     
/*     */ 
/* 273 */     for (int ix = 0; ix < len; ix++) {
/* 274 */       if (array[ix] == element)
/*     */       {
/* 276 */         if (ix == 0) {
/* 277 */           return array;
/*     */         }
/*     */         
/* 280 */         T[] result = (Object[])Array.newInstance(array.getClass().getComponentType(), len);
/* 281 */         System.arraycopy(array, 0, result, 1, ix);
/* 282 */         result[0] = element;
/* 283 */         ix++;
/* 284 */         int left = len - ix;
/* 285 */         if (left > 0) {
/* 286 */           System.arraycopy(array, ix, result, ix, left);
/*     */         }
/* 288 */         return result;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 293 */     T[] result = (Object[])Array.newInstance(array.getClass().getComponentType(), len + 1);
/* 294 */     if (len > 0) {
/* 295 */       System.arraycopy(array, 0, result, 1, len);
/*     */     }
/* 297 */     result[0] = element;
/* 298 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databin\\util\ArrayBuilders.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */