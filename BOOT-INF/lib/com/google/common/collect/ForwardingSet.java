/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
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
/*     */ public abstract class ForwardingSet<E>
/*     */   extends ForwardingCollection<E>
/*     */   implements Set<E>
/*     */ {
/*     */   protected abstract Set<E> delegate();
/*     */   
/*     */   public boolean equals(@Nullable Object object)
/*     */   {
/*  62 */     return (object == this) || (delegate().equals(object));
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/*  67 */     return delegate().hashCode();
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
/*     */   protected boolean standardRemoveAll(Collection<?> collection)
/*     */   {
/*  80 */     return Sets.removeAllImpl(this, (Collection)Preconditions.checkNotNull(collection));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean standardEquals(@Nullable Object object)
/*     */   {
/*  91 */     return Sets.equalsImpl(this, object);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int standardHashCode()
/*     */   {
/* 102 */     return Sets.hashCodeImpl(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\ForwardingSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */