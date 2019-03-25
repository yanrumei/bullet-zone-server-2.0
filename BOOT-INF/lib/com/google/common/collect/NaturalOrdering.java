/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.Serializable;
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
/*    */ @GwtCompatible(serializable=true)
/*    */ final class NaturalOrdering
/*    */   extends Ordering<Comparable>
/*    */   implements Serializable
/*    */ {
/* 28 */   static final NaturalOrdering INSTANCE = new NaturalOrdering();
/*    */   private transient Ordering<Comparable> nullsFirst;
/*    */   private transient Ordering<Comparable> nullsLast;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   public int compare(Comparable left, Comparable right)
/*    */   {
/* 35 */     Preconditions.checkNotNull(left);
/* 36 */     Preconditions.checkNotNull(right);
/* 37 */     return left.compareTo(right);
/*    */   }
/*    */   
/*    */   public <S extends Comparable> Ordering<S> nullsFirst()
/*    */   {
/* 42 */     Ordering<Comparable> result = this.nullsFirst;
/* 43 */     if (result == null) {
/* 44 */       result = this.nullsFirst = super.nullsFirst();
/*    */     }
/* 46 */     return result;
/*    */   }
/*    */   
/*    */   public <S extends Comparable> Ordering<S> nullsLast()
/*    */   {
/* 51 */     Ordering<Comparable> result = this.nullsLast;
/* 52 */     if (result == null) {
/* 53 */       result = this.nullsLast = super.nullsLast();
/*    */     }
/* 55 */     return result;
/*    */   }
/*    */   
/*    */   public <S extends Comparable> Ordering<S> reverse()
/*    */   {
/* 60 */     return ReverseNaturalOrdering.INSTANCE;
/*    */   }
/*    */   
/*    */   private Object readResolve()
/*    */   {
/* 65 */     return INSTANCE;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 70 */     return "Ordering.natural()";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\NaturalOrdering.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */