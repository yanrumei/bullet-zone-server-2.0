/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import javax.annotation.Nullable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated=true)
/*     */ public final class ObjectArrays
/*     */ {
/*     */   @GwtIncompatible
/*     */   public static <T> T[] newArray(Class<T> type, int length)
/*     */   {
/*  49 */     return (Object[])Array.newInstance(type, length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> T[] newArray(T[] reference, int length)
/*     */   {
/*  60 */     return Platform.newArray(reference, length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   public static <T> T[] concat(T[] first, T[] second, Class<T> type)
/*     */   {
/*  72 */     T[] result = newArray(type, first.length + second.length);
/*  73 */     System.arraycopy(first, 0, result, 0, first.length);
/*  74 */     System.arraycopy(second, 0, result, first.length, second.length);
/*  75 */     return result;
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
/*     */   public static <T> T[] concat(@Nullable T element, T[] array)
/*     */   {
/*  88 */     T[] result = newArray(array, array.length + 1);
/*  89 */     result[0] = element;
/*  90 */     System.arraycopy(array, 0, result, 1, array.length);
/*  91 */     return result;
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
/*     */   public static <T> T[] concat(T[] array, @Nullable T element)
/*     */   {
/* 104 */     T[] result = Arrays.copyOf(array, array.length + 1);
/* 105 */     result[array.length] = element;
/* 106 */     return result;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static <T> T[] toArrayImpl(Collection<?> c, T[] array)
/*     */   {
/* 134 */     int size = c.size();
/* 135 */     if (array.length < size) {
/* 136 */       array = newArray(array, size);
/*     */     }
/* 138 */     fillArray(c, array);
/* 139 */     if (array.length > size) {
/* 140 */       array[size] = null;
/*     */     }
/* 142 */     return array;
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
/*     */   static <T> T[] toArrayImpl(Object[] src, int offset, int len, T[] dst)
/*     */   {
/* 157 */     Preconditions.checkPositionIndexes(offset, offset + len, src.length);
/* 158 */     if (dst.length < len) {
/* 159 */       dst = newArray(dst, len);
/* 160 */     } else if (dst.length > len) {
/* 161 */       dst[len] = null;
/*     */     }
/* 163 */     System.arraycopy(src, offset, dst, 0, len);
/* 164 */     return dst;
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
/*     */   static Object[] toArrayImpl(Collection<?> c)
/*     */   {
/* 182 */     return fillArray(c, new Object[c.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static Object[] copyAsObjectArray(Object[] elements, int offset, int length)
/*     */   {
/* 190 */     Preconditions.checkPositionIndexes(offset, offset + length, elements.length);
/* 191 */     if (length == 0) {
/* 192 */       return new Object[0];
/*     */     }
/* 194 */     Object[] result = new Object[length];
/* 195 */     System.arraycopy(elements, offset, result, 0, length);
/* 196 */     return result;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private static Object[] fillArray(Iterable<?> elements, Object[] array) {
/* 201 */     int i = 0;
/* 202 */     for (Object element : elements) {
/* 203 */       array[(i++)] = element;
/*     */     }
/* 205 */     return array;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static void swap(Object[] array, int i, int j)
/*     */   {
/* 212 */     Object temp = array[i];
/* 213 */     array[i] = array[j];
/* 214 */     array[j] = temp;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static Object[] checkElementsNotNull(Object... array) {
/* 219 */     return checkElementsNotNull(array, array.length);
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static Object[] checkElementsNotNull(Object[] array, int length) {
/* 224 */     for (int i = 0; i < length; i++) {
/* 225 */       checkElementNotNull(array[i], i);
/*     */     }
/* 227 */     return array;
/*     */   }
/*     */   
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   static Object checkElementNotNull(Object element, int index)
/*     */   {
/* 234 */     if (element == null) {
/* 235 */       throw new NullPointerException("at index " + index);
/*     */     }
/* 237 */     return element;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\ObjectArrays.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */