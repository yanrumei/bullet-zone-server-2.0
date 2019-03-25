/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Function;
/*    */ import com.google.common.base.Objects;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.Serializable;
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
/*    */ @GwtCompatible(serializable=true)
/*    */ final class ByFunctionOrdering<F, T>
/*    */   extends Ordering<F>
/*    */   implements Serializable
/*    */ {
/*    */   final Function<F, ? extends T> function;
/*    */   final Ordering<T> ordering;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   ByFunctionOrdering(Function<F, ? extends T> function, Ordering<T> ordering)
/*    */   {
/* 37 */     this.function = ((Function)Preconditions.checkNotNull(function));
/* 38 */     this.ordering = ((Ordering)Preconditions.checkNotNull(ordering));
/*    */   }
/*    */   
/*    */   public int compare(F left, F right)
/*    */   {
/* 43 */     return this.ordering.compare(this.function.apply(left), this.function.apply(right));
/*    */   }
/*    */   
/*    */   public boolean equals(@Nullable Object object)
/*    */   {
/* 48 */     if (object == this) {
/* 49 */       return true;
/*    */     }
/* 51 */     if ((object instanceof ByFunctionOrdering)) {
/* 52 */       ByFunctionOrdering<?, ?> that = (ByFunctionOrdering)object;
/* 53 */       return (this.function.equals(that.function)) && (this.ordering.equals(that.ordering));
/*    */     }
/* 55 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 60 */     return Objects.hashCode(new Object[] { this.function, this.ordering });
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 65 */     return this.ordering + ".onResultOf(" + this.function + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\ByFunctionOrdering.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */