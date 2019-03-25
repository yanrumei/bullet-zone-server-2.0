/*     */ package ch.qos.logback.core.util;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ public class COWArrayList<E>
/*     */   implements List<E>
/*     */ {
/*  40 */   AtomicBoolean fresh = new AtomicBoolean(false);
/*  41 */   CopyOnWriteArrayList<E> underlyingList = new CopyOnWriteArrayList();
/*     */   E[] copyOfArray;
/*     */   final E[] modelArray;
/*     */   
/*     */   public COWArrayList(E[] modelArray) {
/*  46 */     this.modelArray = modelArray;
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/*  51 */     return this.underlyingList.size();
/*     */   }
/*     */   
/*     */   public boolean isEmpty()
/*     */   {
/*  56 */     return this.underlyingList.isEmpty();
/*     */   }
/*     */   
/*     */   public boolean contains(Object o)
/*     */   {
/*  61 */     return this.underlyingList.contains(o);
/*     */   }
/*     */   
/*     */   public Iterator<E> iterator()
/*     */   {
/*  66 */     return this.underlyingList.iterator();
/*     */   }
/*     */   
/*     */   private void refreshCopyIfNecessary() {
/*  70 */     if (!isFresh()) {
/*  71 */       refreshCopy();
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isFresh() {
/*  76 */     return this.fresh.get();
/*     */   }
/*     */   
/*     */   private void refreshCopy() {
/*  80 */     this.copyOfArray = this.underlyingList.toArray(this.modelArray);
/*  81 */     this.fresh.set(true);
/*     */   }
/*     */   
/*     */   public Object[] toArray()
/*     */   {
/*  86 */     refreshCopyIfNecessary();
/*  87 */     return this.copyOfArray;
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> T[] toArray(T[] a)
/*     */   {
/*  93 */     refreshCopyIfNecessary();
/*  94 */     return (Object[])this.copyOfArray;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public E[] asTypedArray()
/*     */   {
/* 105 */     refreshCopyIfNecessary();
/* 106 */     return this.copyOfArray;
/*     */   }
/*     */   
/*     */   private void markAsStale() {
/* 110 */     this.fresh.set(false);
/*     */   }
/*     */   
/*     */   public void addIfAbsent(E e) {
/* 114 */     markAsStale();
/* 115 */     this.underlyingList.addIfAbsent(e);
/*     */   }
/*     */   
/*     */   public boolean add(E e)
/*     */   {
/* 120 */     markAsStale();
/* 121 */     return this.underlyingList.add(e);
/*     */   }
/*     */   
/*     */   public boolean remove(Object o)
/*     */   {
/* 126 */     markAsStale();
/* 127 */     return this.underlyingList.remove(o);
/*     */   }
/*     */   
/*     */   public boolean containsAll(Collection<?> c)
/*     */   {
/* 132 */     return this.underlyingList.containsAll(c);
/*     */   }
/*     */   
/*     */   public boolean addAll(Collection<? extends E> c)
/*     */   {
/* 137 */     markAsStale();
/* 138 */     return this.underlyingList.addAll(c);
/*     */   }
/*     */   
/*     */   public boolean addAll(int index, Collection<? extends E> c)
/*     */   {
/* 143 */     markAsStale();
/* 144 */     return this.underlyingList.addAll(index, c);
/*     */   }
/*     */   
/*     */   public boolean removeAll(Collection<?> c)
/*     */   {
/* 149 */     markAsStale();
/* 150 */     return this.underlyingList.removeAll(c);
/*     */   }
/*     */   
/*     */   public boolean retainAll(Collection<?> c)
/*     */   {
/* 155 */     markAsStale();
/* 156 */     return this.underlyingList.retainAll(c);
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 161 */     markAsStale();
/* 162 */     this.underlyingList.clear();
/*     */   }
/*     */   
/*     */   public E get(int index)
/*     */   {
/* 167 */     refreshCopyIfNecessary();
/* 168 */     return (E)this.copyOfArray[index];
/*     */   }
/*     */   
/*     */   public E set(int index, E element)
/*     */   {
/* 173 */     markAsStale();
/* 174 */     return (E)this.underlyingList.set(index, element);
/*     */   }
/*     */   
/*     */   public void add(int index, E element)
/*     */   {
/* 179 */     markAsStale();
/* 180 */     this.underlyingList.add(index, element);
/*     */   }
/*     */   
/*     */   public E remove(int index)
/*     */   {
/* 185 */     markAsStale();
/* 186 */     return (E)this.underlyingList.remove(index);
/*     */   }
/*     */   
/*     */   public int indexOf(Object o)
/*     */   {
/* 191 */     return this.underlyingList.indexOf(o);
/*     */   }
/*     */   
/*     */   public int lastIndexOf(Object o)
/*     */   {
/* 196 */     return this.underlyingList.lastIndexOf(o);
/*     */   }
/*     */   
/*     */   public ListIterator<E> listIterator()
/*     */   {
/* 201 */     return this.underlyingList.listIterator();
/*     */   }
/*     */   
/*     */   public ListIterator<E> listIterator(int index)
/*     */   {
/* 206 */     return this.underlyingList.listIterator(index);
/*     */   }
/*     */   
/*     */   public List<E> subList(int fromIndex, int toIndex)
/*     */   {
/* 211 */     return this.underlyingList.subList(fromIndex, toIndex);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\cor\\util\COWArrayList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */