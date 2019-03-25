/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import java.util.function.Consumer;
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
/*    */ @GwtCompatible(emulated=true)
/*    */ class RegularImmutableAsList<E>
/*    */   extends ImmutableAsList<E>
/*    */ {
/*    */   private final ImmutableCollection<E> delegate;
/*    */   private final ImmutableList<? extends E> delegateList;
/*    */   
/*    */   RegularImmutableAsList(ImmutableCollection<E> delegate, ImmutableList<? extends E> delegateList)
/*    */   {
/* 36 */     this.delegate = delegate;
/* 37 */     this.delegateList = delegateList;
/*    */   }
/*    */   
/*    */   RegularImmutableAsList(ImmutableCollection<E> delegate, Object[] array) {
/* 41 */     this(delegate, ImmutableList.asImmutableList(array));
/*    */   }
/*    */   
/*    */   ImmutableCollection<E> delegateCollection()
/*    */   {
/* 46 */     return this.delegate;
/*    */   }
/*    */   
/*    */   ImmutableList<? extends E> delegateList() {
/* 50 */     return this.delegateList;
/*    */   }
/*    */   
/*    */ 
/*    */   public UnmodifiableListIterator<E> listIterator(int index)
/*    */   {
/* 56 */     return this.delegateList.listIterator(index);
/*    */   }
/*    */   
/*    */   @GwtIncompatible
/*    */   public void forEach(Consumer<? super E> action)
/*    */   {
/* 62 */     this.delegateList.forEach(action);
/*    */   }
/*    */   
/*    */   @GwtIncompatible
/*    */   int copyIntoArray(Object[] dst, int offset)
/*    */   {
/* 68 */     return this.delegateList.copyIntoArray(dst, offset);
/*    */   }
/*    */   
/*    */   public E get(int index)
/*    */   {
/* 73 */     return (E)this.delegateList.get(index);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\RegularImmutableAsList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */