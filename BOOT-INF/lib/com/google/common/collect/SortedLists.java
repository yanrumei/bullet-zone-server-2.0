/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.RandomAccess;
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
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ @Beta
/*     */ final class SortedLists
/*     */ {
/*     */   public static abstract enum KeyPresentBehavior
/*     */   {
/*  51 */     ANY_PRESENT, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  61 */     LAST_PRESENT, 
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
/*  85 */     FIRST_PRESENT, 
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
/* 111 */     FIRST_AFTER, 
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
/* 122 */     LAST_BEFORE;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private KeyPresentBehavior() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     abstract <E> int resultIndex(Comparator<? super E> paramComparator, E paramE, List<? extends E> paramList, int paramInt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static abstract enum KeyAbsentBehavior
/*     */   {
/* 143 */     NEXT_LOWER, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 153 */     NEXT_HIGHER, 
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
/* 171 */     INVERTED_INSERTION_INDEX;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private KeyAbsentBehavior() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     abstract int resultIndex(int paramInt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E extends Comparable> int binarySearch(List<? extends E> list, E e, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior)
/*     */   {
/* 193 */     Preconditions.checkNotNull(e);
/* 194 */     return binarySearch(list, e, Ordering.natural(), presentBehavior, absentBehavior);
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
/*     */   public static <E, K extends Comparable> int binarySearch(List<E> list, Function<? super E, K> keyFunction, @Nullable K key, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior)
/*     */   {
/* 209 */     return binarySearch(list, keyFunction, key, 
/* 210 */       Ordering.natural(), presentBehavior, absentBehavior);
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
/*     */   public static <E, K> int binarySearch(List<E> list, Function<? super E, K> keyFunction, @Nullable K key, Comparator<? super K> keyComparator, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior)
/*     */   {
/* 227 */     return binarySearch(
/* 228 */       Lists.transform(list, keyFunction), key, keyComparator, presentBehavior, absentBehavior);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> int binarySearch(List<? extends E> list, @Nullable E key, Comparator<? super E> comparator, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior)
/*     */   {
/* 260 */     Preconditions.checkNotNull(comparator);
/* 261 */     Preconditions.checkNotNull(list);
/* 262 */     Preconditions.checkNotNull(presentBehavior);
/* 263 */     Preconditions.checkNotNull(absentBehavior);
/* 264 */     if (!(list instanceof RandomAccess)) {
/* 265 */       list = Lists.newArrayList(list);
/*     */     }
/*     */     
/*     */ 
/* 269 */     int lower = 0;
/* 270 */     int upper = list.size() - 1;
/*     */     
/* 272 */     while (lower <= upper) {
/* 273 */       int middle = lower + upper >>> 1;
/* 274 */       int c = comparator.compare(key, list.get(middle));
/* 275 */       if (c < 0) {
/* 276 */         upper = middle - 1;
/* 277 */       } else if (c > 0) {
/* 278 */         lower = middle + 1;
/*     */       } else {
/* 280 */         return 
/* 281 */           lower + presentBehavior.resultIndex(comparator, key, list
/* 282 */           .subList(lower, upper + 1), middle - lower);
/*     */       }
/*     */     }
/* 285 */     return absentBehavior.resultIndex(lower);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\SortedLists.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */