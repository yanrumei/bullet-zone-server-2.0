/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableCollection;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
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
/*     */ @GwtCompatible(emulated=true)
/*     */ abstract class CollectionFuture<V, C>
/*     */   extends AggregateFuture<V, C>
/*     */ {
/*     */   abstract class CollectionFutureRunningState
/*     */     extends AggregateFuture<V, C>.RunningState
/*     */   {
/*     */     private List<Optional<V>> values;
/*     */     
/*     */     CollectionFutureRunningState(boolean futures)
/*     */     {
/*  41 */       super(futures, allMustSucceed, true);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*  46 */       this.values = (futures.isEmpty() ? ImmutableList.of() : Lists.newArrayListWithCapacity(futures.size()));
/*     */       
/*     */ 
/*  49 */       for (int i = 0; i < futures.size(); i++) {
/*  50 */         this.values.add(null);
/*     */       }
/*     */     }
/*     */     
/*     */     final void collectOneValue(boolean allMustSucceed, int index, @Nullable V returnValue)
/*     */     {
/*  56 */       List<Optional<V>> localValues = this.values;
/*     */       
/*  58 */       if (localValues != null) {
/*  59 */         localValues.set(index, Optional.fromNullable(returnValue));
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*  64 */         Preconditions.checkState((allMustSucceed) || 
/*  65 */           (CollectionFuture.this.isCancelled()), "Future was done before all dependencies completed");
/*     */       }
/*     */     }
/*     */     
/*     */     final void handleAllCompleted()
/*     */     {
/*  71 */       List<Optional<V>> localValues = this.values;
/*  72 */       if (localValues != null) {
/*  73 */         CollectionFuture.this.set(combine(localValues));
/*     */       } else {
/*  75 */         Preconditions.checkState(CollectionFuture.this.isDone());
/*     */       }
/*     */     }
/*     */     
/*     */     void releaseResourcesAfterFailure()
/*     */     {
/*  81 */       super.releaseResourcesAfterFailure();
/*  82 */       this.values = null;
/*     */     }
/*     */     
/*     */     abstract C combine(List<Optional<V>> paramList);
/*     */   }
/*     */   
/*     */   static final class ListFuture<V>
/*     */     extends CollectionFuture<V, List<V>>
/*     */   {
/*     */     ListFuture(ImmutableCollection<? extends ListenableFuture<? extends V>> futures, boolean allMustSucceed)
/*     */     {
/*  93 */       init(new ListFutureRunningState(futures, allMustSucceed));
/*     */     }
/*     */     
/*     */     private final class ListFutureRunningState extends CollectionFuture<V, List<V>>.CollectionFutureRunningState
/*     */     {
/*     */       ListFutureRunningState(boolean futures)
/*     */       {
/* 100 */         super(futures, allMustSucceed);
/*     */       }
/*     */       
/*     */       public List<V> combine(List<Optional<V>> values)
/*     */       {
/* 105 */         List<V> result = Lists.newArrayListWithCapacity(values.size());
/* 106 */         for (Optional<V> element : values) {
/* 107 */           result.add(element != null ? element.orNull() : null);
/*     */         }
/* 109 */         return Collections.unmodifiableList(result);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\commo\\util\concurrent\CollectionFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */