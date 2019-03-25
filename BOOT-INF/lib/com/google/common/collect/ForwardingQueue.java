/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Queue;
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
/*     */ public abstract class ForwardingQueue<E>
/*     */   extends ForwardingCollection<E>
/*     */   implements Queue<E>
/*     */ {
/*     */   protected abstract Queue<E> delegate();
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean offer(E o)
/*     */   {
/*  60 */     return delegate().offer(o);
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E poll()
/*     */   {
/*  66 */     return (E)delegate().poll();
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E remove()
/*     */   {
/*  72 */     return (E)delegate().remove();
/*     */   }
/*     */   
/*     */   public E peek()
/*     */   {
/*  77 */     return (E)delegate().peek();
/*     */   }
/*     */   
/*     */   public E element()
/*     */   {
/*  82 */     return (E)delegate().element();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean standardOffer(E e)
/*     */   {
/*     */     try
/*     */     {
/*  94 */       return add(e);
/*     */     } catch (IllegalStateException caught) {}
/*  96 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected E standardPeek()
/*     */   {
/*     */     try
/*     */     {
/* 109 */       return (E)element();
/*     */     } catch (NoSuchElementException caught) {}
/* 111 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected E standardPoll()
/*     */   {
/*     */     try
/*     */     {
/* 124 */       return (E)remove();
/*     */     } catch (NoSuchElementException caught) {}
/* 126 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\ForwardingQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */