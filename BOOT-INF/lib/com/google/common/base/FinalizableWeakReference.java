/*    */ package com.google.common.base;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import java.lang.ref.WeakReference;
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
/*    */ 
/*    */ 
/*    */ @GwtIncompatible
/*    */ public abstract class FinalizableWeakReference<T>
/*    */   extends WeakReference<T>
/*    */   implements FinalizableReference
/*    */ {
/*    */   protected FinalizableWeakReference(T referent, FinalizableReferenceQueue queue)
/*    */   {
/* 39 */     super(referent, queue.queue);
/* 40 */     queue.cleanUp();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\base\FinalizableWeakReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */