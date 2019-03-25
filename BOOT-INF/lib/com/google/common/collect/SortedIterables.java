/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.Comparator;
/*    */ import java.util.SortedSet;
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
/*    */ @GwtCompatible
/*    */ final class SortedIterables
/*    */ {
/*    */   public static boolean hasSameComparator(Comparator<?> comparator, Iterable<?> elements)
/*    */   {
/* 37 */     Preconditions.checkNotNull(comparator);
/* 38 */     Preconditions.checkNotNull(elements);
/*    */     Comparator<?> comparator2;
/* 40 */     if ((elements instanceof SortedSet)) {
/* 41 */       comparator2 = comparator((SortedSet)elements); } else { Comparator<?> comparator2;
/* 42 */       if ((elements instanceof SortedIterable)) {
/* 43 */         comparator2 = ((SortedIterable)elements).comparator();
/*    */       } else
/* 45 */         return false; }
/*    */     Comparator<?> comparator2;
/* 47 */     return comparator.equals(comparator2);
/*    */   }
/*    */   
/*    */ 
/*    */   public static <E> Comparator<? super E> comparator(SortedSet<E> sortedSet)
/*    */   {
/* 53 */     Comparator<? super E> result = sortedSet.comparator();
/* 54 */     if (result == null) {
/* 55 */       result = Ordering.natural();
/*    */     }
/* 57 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\SortedIterables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */