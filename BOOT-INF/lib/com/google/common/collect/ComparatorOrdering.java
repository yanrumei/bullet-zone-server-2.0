/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.Serializable;
/*    */ import java.util.Comparator;
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
/*    */ @GwtCompatible(serializable=true)
/*    */ final class ComparatorOrdering<T>
/*    */   extends Ordering<T>
/*    */   implements Serializable
/*    */ {
/*    */   final Comparator<T> comparator;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   ComparatorOrdering(Comparator<T> comparator)
/*    */   {
/* 32 */     this.comparator = ((Comparator)Preconditions.checkNotNull(comparator));
/*    */   }
/*    */   
/*    */   public int compare(T a, T b)
/*    */   {
/* 37 */     return this.comparator.compare(a, b);
/*    */   }
/*    */   
/*    */   public boolean equals(@Nullable Object object)
/*    */   {
/* 42 */     if (object == this) {
/* 43 */       return true;
/*    */     }
/* 45 */     if ((object instanceof ComparatorOrdering)) {
/* 46 */       ComparatorOrdering<?> that = (ComparatorOrdering)object;
/* 47 */       return this.comparator.equals(that.comparator);
/*    */     }
/* 49 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 54 */     return this.comparator.hashCode();
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 59 */     return this.comparator.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\ComparatorOrdering.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */