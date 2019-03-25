/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.BlockingDeque;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ @Deprecated
/*     */ @GwtIncompatible
/*     */ public abstract class ForwardingBlockingDeque<E>
/*     */   extends ForwardingDeque<E>
/*     */   implements BlockingDeque<E>
/*     */ {
/*     */   protected abstract BlockingDeque<E> delegate();
/*     */   
/*     */   public int remainingCapacity()
/*     */   {
/*  61 */     return delegate().remainingCapacity();
/*     */   }
/*     */   
/*     */   public void putFirst(E e) throws InterruptedException
/*     */   {
/*  66 */     delegate().putFirst(e);
/*     */   }
/*     */   
/*     */   public void putLast(E e) throws InterruptedException
/*     */   {
/*  71 */     delegate().putLast(e);
/*     */   }
/*     */   
/*     */   public boolean offerFirst(E e, long timeout, TimeUnit unit) throws InterruptedException
/*     */   {
/*  76 */     return delegate().offerFirst(e, timeout, unit);
/*     */   }
/*     */   
/*     */   public boolean offerLast(E e, long timeout, TimeUnit unit) throws InterruptedException
/*     */   {
/*  81 */     return delegate().offerLast(e, timeout, unit);
/*     */   }
/*     */   
/*     */   public E takeFirst() throws InterruptedException
/*     */   {
/*  86 */     return (E)delegate().takeFirst();
/*     */   }
/*     */   
/*     */   public E takeLast() throws InterruptedException
/*     */   {
/*  91 */     return (E)delegate().takeLast();
/*     */   }
/*     */   
/*     */   public E pollFirst(long timeout, TimeUnit unit) throws InterruptedException
/*     */   {
/*  96 */     return (E)delegate().pollFirst(timeout, unit);
/*     */   }
/*     */   
/*     */   public E pollLast(long timeout, TimeUnit unit) throws InterruptedException
/*     */   {
/* 101 */     return (E)delegate().pollLast(timeout, unit);
/*     */   }
/*     */   
/*     */   public void put(E e) throws InterruptedException
/*     */   {
/* 106 */     delegate().put(e);
/*     */   }
/*     */   
/*     */   public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException
/*     */   {
/* 111 */     return delegate().offer(e, timeout, unit);
/*     */   }
/*     */   
/*     */   public E take() throws InterruptedException
/*     */   {
/* 116 */     return (E)delegate().take();
/*     */   }
/*     */   
/*     */   public E poll(long timeout, TimeUnit unit) throws InterruptedException
/*     */   {
/* 121 */     return (E)delegate().poll(timeout, unit);
/*     */   }
/*     */   
/*     */   public int drainTo(Collection<? super E> c)
/*     */   {
/* 126 */     return delegate().drainTo(c);
/*     */   }
/*     */   
/*     */   public int drainTo(Collection<? super E> c, int maxElements)
/*     */   {
/* 131 */     return delegate().drainTo(c, maxElements);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\ForwardingBlockingDeque.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */