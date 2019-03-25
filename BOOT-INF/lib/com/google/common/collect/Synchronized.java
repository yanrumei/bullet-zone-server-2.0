/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.j2objc.annotations.RetainedWith;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.Deque;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.NavigableMap;
/*      */ import java.util.NavigableSet;
/*      */ import java.util.Queue;
/*      */ import java.util.RandomAccess;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import java.util.SortedSet;
/*      */ import java.util.function.BiConsumer;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.UnaryOperator;
/*      */ import javax.annotation.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible(emulated=true)
/*      */ final class Synchronized
/*      */ {
/*      */   static class SynchronizedObject
/*      */     implements Serializable
/*      */   {
/*      */     final Object delegate;
/*      */     final Object mutex;
/*      */     @GwtIncompatible
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedObject(Object delegate, @Nullable Object mutex)
/*      */     {
/*   75 */       this.delegate = Preconditions.checkNotNull(delegate);
/*   76 */       this.mutex = (mutex == null ? this : mutex);
/*      */     }
/*      */     
/*      */     Object delegate() {
/*   80 */       return this.delegate;
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public String toString()
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedObject:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_1
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: getfield 3	com/google/common/collect/Synchronized$SynchronizedObject:delegate	Ljava/lang/Object;
/*      */       //   11: invokevirtual 5	java/lang/Object:toString	()Ljava/lang/String;
/*      */       //   14: aload_1
/*      */       //   15: monitorexit
/*      */       //   16: areturn
/*      */       //   17: astore_2
/*      */       //   18: aload_1
/*      */       //   19: monitorexit
/*      */       //   20: aload_2
/*      */       //   21: athrow
/*      */       // Line number table:
/*      */       //   Java source line #87	-> byte code offset #0
/*      */       //   Java source line #88	-> byte code offset #7
/*      */       //   Java source line #89	-> byte code offset #17
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	22	0	this	SynchronizedObject
/*      */       //   5	14	1	Ljava/lang/Object;	Object
/*      */       //   17	4	2	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	16	17	finally
/*      */       //   17	20	17	finally
/*      */     }
/*      */     
/*      */     @GwtIncompatible
/*      */     private void writeObject(ObjectOutputStream stream)
/*      */       throws IOException
/*      */     {
/*   99 */       synchronized (this.mutex) {
/*  100 */         stream.defaultWriteObject();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  109 */   private static <E> Collection<E> collection(Collection<E> collection, @Nullable Object mutex) { return new SynchronizedCollection(collection, mutex, null); }
/*      */   
/*      */   @VisibleForTesting
/*      */   static class SynchronizedCollection<E> extends Synchronized.SynchronizedObject implements Collection<E> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*  115 */     private SynchronizedCollection(Collection<E> delegate, @Nullable Object mutex) { super(mutex); }
/*      */     
/*      */ 
/*      */ 
/*      */     Collection<E> delegate()
/*      */     {
/*  121 */       return (Collection)super.delegate();
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public boolean add(E e)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 5	com/google/common/collect/Synchronized$SynchronizedCollection:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_2
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 6	com/google/common/collect/Synchronized$SynchronizedCollection:delegate	()Ljava/util/Collection;
/*      */       //   11: aload_1
/*      */       //   12: invokeinterface 7 2 0
/*      */       //   17: aload_2
/*      */       //   18: monitorexit
/*      */       //   19: ireturn
/*      */       //   20: astore_3
/*      */       //   21: aload_2
/*      */       //   22: monitorexit
/*      */       //   23: aload_3
/*      */       //   24: athrow
/*      */       // Line number table:
/*      */       //   Java source line #126	-> byte code offset #0
/*      */       //   Java source line #127	-> byte code offset #7
/*      */       //   Java source line #128	-> byte code offset #20
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	25	0	this	SynchronizedCollection<E>
/*      */       //   0	25	1	e	E
/*      */       //   5	17	2	Ljava/lang/Object;	Object
/*      */       //   20	4	3	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	19	20	finally
/*      */       //   20	23	20	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public boolean addAll(Collection<? extends E> c)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 5	com/google/common/collect/Synchronized$SynchronizedCollection:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_2
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 6	com/google/common/collect/Synchronized$SynchronizedCollection:delegate	()Ljava/util/Collection;
/*      */       //   11: aload_1
/*      */       //   12: invokeinterface 8 2 0
/*      */       //   17: aload_2
/*      */       //   18: monitorexit
/*      */       //   19: ireturn
/*      */       //   20: astore_3
/*      */       //   21: aload_2
/*      */       //   22: monitorexit
/*      */       //   23: aload_3
/*      */       //   24: athrow
/*      */       // Line number table:
/*      */       //   Java source line #133	-> byte code offset #0
/*      */       //   Java source line #134	-> byte code offset #7
/*      */       //   Java source line #135	-> byte code offset #20
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	25	0	this	SynchronizedCollection<E>
/*      */       //   0	25	1	c	Collection<? extends E>
/*      */       //   5	17	2	Ljava/lang/Object;	Object
/*      */       //   20	4	3	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	19	20	finally
/*      */       //   20	23	20	finally
/*      */     }
/*      */     
/*      */     public void clear()
/*      */     {
/*  140 */       synchronized (this.mutex) {
/*  141 */         delegate().clear();
/*      */       }
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public boolean contains(Object o)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 5	com/google/common/collect/Synchronized$SynchronizedCollection:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_2
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 6	com/google/common/collect/Synchronized$SynchronizedCollection:delegate	()Ljava/util/Collection;
/*      */       //   11: aload_1
/*      */       //   12: invokeinterface 10 2 0
/*      */       //   17: aload_2
/*      */       //   18: monitorexit
/*      */       //   19: ireturn
/*      */       //   20: astore_3
/*      */       //   21: aload_2
/*      */       //   22: monitorexit
/*      */       //   23: aload_3
/*      */       //   24: athrow
/*      */       // Line number table:
/*      */       //   Java source line #147	-> byte code offset #0
/*      */       //   Java source line #148	-> byte code offset #7
/*      */       //   Java source line #149	-> byte code offset #20
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	25	0	this	SynchronizedCollection<E>
/*      */       //   0	25	1	o	Object
/*      */       //   5	17	2	Ljava/lang/Object;	Object
/*      */       //   20	4	3	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	19	20	finally
/*      */       //   20	23	20	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public boolean containsAll(Collection<?> c)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 5	com/google/common/collect/Synchronized$SynchronizedCollection:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_2
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 6	com/google/common/collect/Synchronized$SynchronizedCollection:delegate	()Ljava/util/Collection;
/*      */       //   11: aload_1
/*      */       //   12: invokeinterface 11 2 0
/*      */       //   17: aload_2
/*      */       //   18: monitorexit
/*      */       //   19: ireturn
/*      */       //   20: astore_3
/*      */       //   21: aload_2
/*      */       //   22: monitorexit
/*      */       //   23: aload_3
/*      */       //   24: athrow
/*      */       // Line number table:
/*      */       //   Java source line #154	-> byte code offset #0
/*      */       //   Java source line #155	-> byte code offset #7
/*      */       //   Java source line #156	-> byte code offset #20
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	25	0	this	SynchronizedCollection<E>
/*      */       //   0	25	1	c	Collection<?>
/*      */       //   5	17	2	Ljava/lang/Object;	Object
/*      */       //   20	4	3	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	19	20	finally
/*      */       //   20	23	20	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public boolean isEmpty()
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 5	com/google/common/collect/Synchronized$SynchronizedCollection:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_1
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 6	com/google/common/collect/Synchronized$SynchronizedCollection:delegate	()Ljava/util/Collection;
/*      */       //   11: invokeinterface 12 1 0
/*      */       //   16: aload_1
/*      */       //   17: monitorexit
/*      */       //   18: ireturn
/*      */       //   19: astore_2
/*      */       //   20: aload_1
/*      */       //   21: monitorexit
/*      */       //   22: aload_2
/*      */       //   23: athrow
/*      */       // Line number table:
/*      */       //   Java source line #161	-> byte code offset #0
/*      */       //   Java source line #162	-> byte code offset #7
/*      */       //   Java source line #163	-> byte code offset #19
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	24	0	this	SynchronizedCollection<E>
/*      */       //   5	16	1	Ljava/lang/Object;	Object
/*      */       //   19	4	2	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	18	19	finally
/*      */       //   19	22	19	finally
/*      */     }
/*      */     
/*      */     public Iterator<E> iterator()
/*      */     {
/*  168 */       return delegate().iterator();
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public java.util.Spliterator<E> spliterator()
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 5	com/google/common/collect/Synchronized$SynchronizedCollection:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_1
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 6	com/google/common/collect/Synchronized$SynchronizedCollection:delegate	()Ljava/util/Collection;
/*      */       //   11: invokeinterface 14 1 0
/*      */       //   16: aload_1
/*      */       //   17: monitorexit
/*      */       //   18: areturn
/*      */       //   19: astore_2
/*      */       //   20: aload_1
/*      */       //   21: monitorexit
/*      */       //   22: aload_2
/*      */       //   23: athrow
/*      */       // Line number table:
/*      */       //   Java source line #173	-> byte code offset #0
/*      */       //   Java source line #174	-> byte code offset #7
/*      */       //   Java source line #175	-> byte code offset #19
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	24	0	this	SynchronizedCollection<E>
/*      */       //   5	16	1	Ljava/lang/Object;	Object
/*      */       //   19	4	2	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	18	19	finally
/*      */       //   19	22	19	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public java.util.stream.Stream<E> stream()
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 5	com/google/common/collect/Synchronized$SynchronizedCollection:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_1
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 6	com/google/common/collect/Synchronized$SynchronizedCollection:delegate	()Ljava/util/Collection;
/*      */       //   11: invokeinterface 15 1 0
/*      */       //   16: aload_1
/*      */       //   17: monitorexit
/*      */       //   18: areturn
/*      */       //   19: astore_2
/*      */       //   20: aload_1
/*      */       //   21: monitorexit
/*      */       //   22: aload_2
/*      */       //   23: athrow
/*      */       // Line number table:
/*      */       //   Java source line #180	-> byte code offset #0
/*      */       //   Java source line #181	-> byte code offset #7
/*      */       //   Java source line #182	-> byte code offset #19
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	24	0	this	SynchronizedCollection<E>
/*      */       //   5	16	1	Ljava/lang/Object;	Object
/*      */       //   19	4	2	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	18	19	finally
/*      */       //   19	22	19	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public java.util.stream.Stream<E> parallelStream()
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 5	com/google/common/collect/Synchronized$SynchronizedCollection:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_1
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 6	com/google/common/collect/Synchronized$SynchronizedCollection:delegate	()Ljava/util/Collection;
/*      */       //   11: invokeinterface 16 1 0
/*      */       //   16: aload_1
/*      */       //   17: monitorexit
/*      */       //   18: areturn
/*      */       //   19: astore_2
/*      */       //   20: aload_1
/*      */       //   21: monitorexit
/*      */       //   22: aload_2
/*      */       //   23: athrow
/*      */       // Line number table:
/*      */       //   Java source line #187	-> byte code offset #0
/*      */       //   Java source line #188	-> byte code offset #7
/*      */       //   Java source line #189	-> byte code offset #19
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	24	0	this	SynchronizedCollection<E>
/*      */       //   5	16	1	Ljava/lang/Object;	Object
/*      */       //   19	4	2	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	18	19	finally
/*      */       //   19	22	19	finally
/*      */     }
/*      */     
/*      */     public void forEach(Consumer<? super E> action)
/*      */     {
/*  194 */       synchronized (this.mutex) {
/*  195 */         delegate().forEach(action);
/*      */       }
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public boolean remove(Object o)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 5	com/google/common/collect/Synchronized$SynchronizedCollection:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_2
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 6	com/google/common/collect/Synchronized$SynchronizedCollection:delegate	()Ljava/util/Collection;
/*      */       //   11: aload_1
/*      */       //   12: invokeinterface 18 2 0
/*      */       //   17: aload_2
/*      */       //   18: monitorexit
/*      */       //   19: ireturn
/*      */       //   20: astore_3
/*      */       //   21: aload_2
/*      */       //   22: monitorexit
/*      */       //   23: aload_3
/*      */       //   24: athrow
/*      */       // Line number table:
/*      */       //   Java source line #201	-> byte code offset #0
/*      */       //   Java source line #202	-> byte code offset #7
/*      */       //   Java source line #203	-> byte code offset #20
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	25	0	this	SynchronizedCollection<E>
/*      */       //   0	25	1	o	Object
/*      */       //   5	17	2	Ljava/lang/Object;	Object
/*      */       //   20	4	3	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	19	20	finally
/*      */       //   20	23	20	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public boolean removeAll(Collection<?> c)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 5	com/google/common/collect/Synchronized$SynchronizedCollection:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_2
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 6	com/google/common/collect/Synchronized$SynchronizedCollection:delegate	()Ljava/util/Collection;
/*      */       //   11: aload_1
/*      */       //   12: invokeinterface 19 2 0
/*      */       //   17: aload_2
/*      */       //   18: monitorexit
/*      */       //   19: ireturn
/*      */       //   20: astore_3
/*      */       //   21: aload_2
/*      */       //   22: monitorexit
/*      */       //   23: aload_3
/*      */       //   24: athrow
/*      */       // Line number table:
/*      */       //   Java source line #208	-> byte code offset #0
/*      */       //   Java source line #209	-> byte code offset #7
/*      */       //   Java source line #210	-> byte code offset #20
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	25	0	this	SynchronizedCollection<E>
/*      */       //   0	25	1	c	Collection<?>
/*      */       //   5	17	2	Ljava/lang/Object;	Object
/*      */       //   20	4	3	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	19	20	finally
/*      */       //   20	23	20	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public boolean retainAll(Collection<?> c)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 5	com/google/common/collect/Synchronized$SynchronizedCollection:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_2
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 6	com/google/common/collect/Synchronized$SynchronizedCollection:delegate	()Ljava/util/Collection;
/*      */       //   11: aload_1
/*      */       //   12: invokeinterface 20 2 0
/*      */       //   17: aload_2
/*      */       //   18: monitorexit
/*      */       //   19: ireturn
/*      */       //   20: astore_3
/*      */       //   21: aload_2
/*      */       //   22: monitorexit
/*      */       //   23: aload_3
/*      */       //   24: athrow
/*      */       // Line number table:
/*      */       //   Java source line #215	-> byte code offset #0
/*      */       //   Java source line #216	-> byte code offset #7
/*      */       //   Java source line #217	-> byte code offset #20
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	25	0	this	SynchronizedCollection<E>
/*      */       //   0	25	1	c	Collection<?>
/*      */       //   5	17	2	Ljava/lang/Object;	Object
/*      */       //   20	4	3	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	19	20	finally
/*      */       //   20	23	20	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public boolean removeIf(java.util.function.Predicate<? super E> filter)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 5	com/google/common/collect/Synchronized$SynchronizedCollection:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_2
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 6	com/google/common/collect/Synchronized$SynchronizedCollection:delegate	()Ljava/util/Collection;
/*      */       //   11: aload_1
/*      */       //   12: invokeinterface 21 2 0
/*      */       //   17: aload_2
/*      */       //   18: monitorexit
/*      */       //   19: ireturn
/*      */       //   20: astore_3
/*      */       //   21: aload_2
/*      */       //   22: monitorexit
/*      */       //   23: aload_3
/*      */       //   24: athrow
/*      */       // Line number table:
/*      */       //   Java source line #222	-> byte code offset #0
/*      */       //   Java source line #223	-> byte code offset #7
/*      */       //   Java source line #224	-> byte code offset #20
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	25	0	this	SynchronizedCollection<E>
/*      */       //   0	25	1	filter	java.util.function.Predicate<? super E>
/*      */       //   5	17	2	Ljava/lang/Object;	Object
/*      */       //   20	4	3	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	19	20	finally
/*      */       //   20	23	20	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public int size()
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 5	com/google/common/collect/Synchronized$SynchronizedCollection:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_1
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 6	com/google/common/collect/Synchronized$SynchronizedCollection:delegate	()Ljava/util/Collection;
/*      */       //   11: invokeinterface 22 1 0
/*      */       //   16: aload_1
/*      */       //   17: monitorexit
/*      */       //   18: ireturn
/*      */       //   19: astore_2
/*      */       //   20: aload_1
/*      */       //   21: monitorexit
/*      */       //   22: aload_2
/*      */       //   23: athrow
/*      */       // Line number table:
/*      */       //   Java source line #229	-> byte code offset #0
/*      */       //   Java source line #230	-> byte code offset #7
/*      */       //   Java source line #231	-> byte code offset #19
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	24	0	this	SynchronizedCollection<E>
/*      */       //   5	16	1	Ljava/lang/Object;	Object
/*      */       //   19	4	2	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	18	19	finally
/*      */       //   19	22	19	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public Object[] toArray()
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 5	com/google/common/collect/Synchronized$SynchronizedCollection:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_1
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 6	com/google/common/collect/Synchronized$SynchronizedCollection:delegate	()Ljava/util/Collection;
/*      */       //   11: invokeinterface 23 1 0
/*      */       //   16: aload_1
/*      */       //   17: monitorexit
/*      */       //   18: areturn
/*      */       //   19: astore_2
/*      */       //   20: aload_1
/*      */       //   21: monitorexit
/*      */       //   22: aload_2
/*      */       //   23: athrow
/*      */       // Line number table:
/*      */       //   Java source line #236	-> byte code offset #0
/*      */       //   Java source line #237	-> byte code offset #7
/*      */       //   Java source line #238	-> byte code offset #19
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	24	0	this	SynchronizedCollection<E>
/*      */       //   5	16	1	Ljava/lang/Object;	Object
/*      */       //   19	4	2	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	18	19	finally
/*      */       //   19	22	19	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public <T> T[] toArray(T[] a)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 5	com/google/common/collect/Synchronized$SynchronizedCollection:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_2
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 6	com/google/common/collect/Synchronized$SynchronizedCollection:delegate	()Ljava/util/Collection;
/*      */       //   11: aload_1
/*      */       //   12: invokeinterface 24 2 0
/*      */       //   17: aload_2
/*      */       //   18: monitorexit
/*      */       //   19: areturn
/*      */       //   20: astore_3
/*      */       //   21: aload_2
/*      */       //   22: monitorexit
/*      */       //   23: aload_3
/*      */       //   24: athrow
/*      */       // Line number table:
/*      */       //   Java source line #243	-> byte code offset #0
/*      */       //   Java source line #244	-> byte code offset #7
/*      */       //   Java source line #245	-> byte code offset #20
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	25	0	this	SynchronizedCollection<E>
/*      */       //   0	25	1	a	T[]
/*      */       //   5	17	2	Ljava/lang/Object;	Object
/*      */       //   20	4	3	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	19	20	finally
/*      */       //   20	23	20	finally
/*      */     }
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   static <E> Set<E> set(Set<E> set, @Nullable Object mutex)
/*      */   {
/*  253 */     return new SynchronizedSet(set, mutex);
/*      */   }
/*      */   
/*      */   static class SynchronizedSet<E> extends Synchronized.SynchronizedCollection<E> implements Set<E> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*  259 */     SynchronizedSet(Set<E> delegate, @Nullable Object mutex) { super(mutex, null); }
/*      */     
/*      */ 
/*      */     Set<E> delegate()
/*      */     {
/*  264 */       return (Set)super.delegate();
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public boolean equals(Object o)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_1
/*      */       //   1: aload_0
/*      */       //   2: if_acmpne +5 -> 7
/*      */       //   5: iconst_1
/*      */       //   6: ireturn
/*      */       //   7: aload_0
/*      */       //   8: getfield 4	com/google/common/collect/Synchronized$SynchronizedSet:mutex	Ljava/lang/Object;
/*      */       //   11: dup
/*      */       //   12: astore_2
/*      */       //   13: monitorenter
/*      */       //   14: aload_0
/*      */       //   15: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedSet:delegate	()Ljava/util/Set;
/*      */       //   18: aload_1
/*      */       //   19: invokeinterface 6 2 0
/*      */       //   24: aload_2
/*      */       //   25: monitorexit
/*      */       //   26: ireturn
/*      */       //   27: astore_3
/*      */       //   28: aload_2
/*      */       //   29: monitorexit
/*      */       //   30: aload_3
/*      */       //   31: athrow
/*      */       // Line number table:
/*      */       //   Java source line #269	-> byte code offset #0
/*      */       //   Java source line #270	-> byte code offset #5
/*      */       //   Java source line #272	-> byte code offset #7
/*      */       //   Java source line #273	-> byte code offset #14
/*      */       //   Java source line #274	-> byte code offset #27
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	32	0	this	SynchronizedSet<E>
/*      */       //   0	32	1	o	Object
/*      */       //   12	17	2	Ljava/lang/Object;	Object
/*      */       //   27	4	3	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   14	26	27	finally
/*      */       //   27	30	27	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public int hashCode()
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedSet:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_1
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedSet:delegate	()Ljava/util/Set;
/*      */       //   11: invokeinterface 7 1 0
/*      */       //   16: aload_1
/*      */       //   17: monitorexit
/*      */       //   18: ireturn
/*      */       //   19: astore_2
/*      */       //   20: aload_1
/*      */       //   21: monitorexit
/*      */       //   22: aload_2
/*      */       //   23: athrow
/*      */       // Line number table:
/*      */       //   Java source line #279	-> byte code offset #0
/*      */       //   Java source line #280	-> byte code offset #7
/*      */       //   Java source line #281	-> byte code offset #19
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	24	0	this	SynchronizedSet<E>
/*      */       //   5	16	1	Ljava/lang/Object;	Object
/*      */       //   19	4	2	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	18	19	finally
/*      */       //   19	22	19	finally
/*      */     }
/*      */   }
/*      */   
/*  288 */   private static <E> SortedSet<E> sortedSet(SortedSet<E> set, @Nullable Object mutex) { return new SynchronizedSortedSet(set, mutex); }
/*      */   
/*      */   static class SynchronizedSortedSet<E> extends Synchronized.SynchronizedSet<E> implements SortedSet<E> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*  293 */     SynchronizedSortedSet(SortedSet<E> delegate, @Nullable Object mutex) { super(mutex); }
/*      */     
/*      */ 
/*      */     SortedSet<E> delegate()
/*      */     {
/*  298 */       return (SortedSet)super.delegate();
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public Comparator<? super E> comparator()
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedSortedSet:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_1
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedSortedSet:delegate	()Ljava/util/SortedSet;
/*      */       //   11: invokeinterface 6 1 0
/*      */       //   16: aload_1
/*      */       //   17: monitorexit
/*      */       //   18: areturn
/*      */       //   19: astore_2
/*      */       //   20: aload_1
/*      */       //   21: monitorexit
/*      */       //   22: aload_2
/*      */       //   23: athrow
/*      */       // Line number table:
/*      */       //   Java source line #303	-> byte code offset #0
/*      */       //   Java source line #304	-> byte code offset #7
/*      */       //   Java source line #305	-> byte code offset #19
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	24	0	this	SynchronizedSortedSet<E>
/*      */       //   5	16	1	Ljava/lang/Object;	Object
/*      */       //   19	4	2	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	18	19	finally
/*      */       //   19	22	19	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public SortedSet<E> subSet(E fromElement, E toElement)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedSortedSet:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_3
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedSortedSet:delegate	()Ljava/util/SortedSet;
/*      */       //   11: aload_1
/*      */       //   12: aload_2
/*      */       //   13: invokeinterface 7 3 0
/*      */       //   18: aload_0
/*      */       //   19: getfield 4	com/google/common/collect/Synchronized$SynchronizedSortedSet:mutex	Ljava/lang/Object;
/*      */       //   22: invokestatic 8	com/google/common/collect/Synchronized:access$100	(Ljava/util/SortedSet;Ljava/lang/Object;)Ljava/util/SortedSet;
/*      */       //   25: aload_3
/*      */       //   26: monitorexit
/*      */       //   27: areturn
/*      */       //   28: astore 4
/*      */       //   30: aload_3
/*      */       //   31: monitorexit
/*      */       //   32: aload 4
/*      */       //   34: athrow
/*      */       // Line number table:
/*      */       //   Java source line #310	-> byte code offset #0
/*      */       //   Java source line #311	-> byte code offset #7
/*      */       //   Java source line #312	-> byte code offset #28
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	35	0	this	SynchronizedSortedSet<E>
/*      */       //   0	35	1	fromElement	E
/*      */       //   0	35	2	toElement	E
/*      */       //   5	26	3	Ljava/lang/Object;	Object
/*      */       //   28	5	4	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	27	28	finally
/*      */       //   28	32	28	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public SortedSet<E> headSet(E toElement)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedSortedSet:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_2
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedSortedSet:delegate	()Ljava/util/SortedSet;
/*      */       //   11: aload_1
/*      */       //   12: invokeinterface 9 2 0
/*      */       //   17: aload_0
/*      */       //   18: getfield 4	com/google/common/collect/Synchronized$SynchronizedSortedSet:mutex	Ljava/lang/Object;
/*      */       //   21: invokestatic 8	com/google/common/collect/Synchronized:access$100	(Ljava/util/SortedSet;Ljava/lang/Object;)Ljava/util/SortedSet;
/*      */       //   24: aload_2
/*      */       //   25: monitorexit
/*      */       //   26: areturn
/*      */       //   27: astore_3
/*      */       //   28: aload_2
/*      */       //   29: monitorexit
/*      */       //   30: aload_3
/*      */       //   31: athrow
/*      */       // Line number table:
/*      */       //   Java source line #317	-> byte code offset #0
/*      */       //   Java source line #318	-> byte code offset #7
/*      */       //   Java source line #319	-> byte code offset #27
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	32	0	this	SynchronizedSortedSet<E>
/*      */       //   0	32	1	toElement	E
/*      */       //   5	24	2	Ljava/lang/Object;	Object
/*      */       //   27	4	3	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	26	27	finally
/*      */       //   27	30	27	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public SortedSet<E> tailSet(E fromElement)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedSortedSet:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_2
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedSortedSet:delegate	()Ljava/util/SortedSet;
/*      */       //   11: aload_1
/*      */       //   12: invokeinterface 10 2 0
/*      */       //   17: aload_0
/*      */       //   18: getfield 4	com/google/common/collect/Synchronized$SynchronizedSortedSet:mutex	Ljava/lang/Object;
/*      */       //   21: invokestatic 8	com/google/common/collect/Synchronized:access$100	(Ljava/util/SortedSet;Ljava/lang/Object;)Ljava/util/SortedSet;
/*      */       //   24: aload_2
/*      */       //   25: monitorexit
/*      */       //   26: areturn
/*      */       //   27: astore_3
/*      */       //   28: aload_2
/*      */       //   29: monitorexit
/*      */       //   30: aload_3
/*      */       //   31: athrow
/*      */       // Line number table:
/*      */       //   Java source line #324	-> byte code offset #0
/*      */       //   Java source line #325	-> byte code offset #7
/*      */       //   Java source line #326	-> byte code offset #27
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	32	0	this	SynchronizedSortedSet<E>
/*      */       //   0	32	1	fromElement	E
/*      */       //   5	24	2	Ljava/lang/Object;	Object
/*      */       //   27	4	3	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	26	27	finally
/*      */       //   27	30	27	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public E first()
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedSortedSet:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_1
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedSortedSet:delegate	()Ljava/util/SortedSet;
/*      */       //   11: invokeinterface 11 1 0
/*      */       //   16: aload_1
/*      */       //   17: monitorexit
/*      */       //   18: areturn
/*      */       //   19: astore_2
/*      */       //   20: aload_1
/*      */       //   21: monitorexit
/*      */       //   22: aload_2
/*      */       //   23: athrow
/*      */       // Line number table:
/*      */       //   Java source line #331	-> byte code offset #0
/*      */       //   Java source line #332	-> byte code offset #7
/*      */       //   Java source line #333	-> byte code offset #19
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	24	0	this	SynchronizedSortedSet<E>
/*      */       //   5	16	1	Ljava/lang/Object;	Object
/*      */       //   19	4	2	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	18	19	finally
/*      */       //   19	22	19	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public E last()
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedSortedSet:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_1
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedSortedSet:delegate	()Ljava/util/SortedSet;
/*      */       //   11: invokeinterface 12 1 0
/*      */       //   16: aload_1
/*      */       //   17: monitorexit
/*      */       //   18: areturn
/*      */       //   19: astore_2
/*      */       //   20: aload_1
/*      */       //   21: monitorexit
/*      */       //   22: aload_2
/*      */       //   23: athrow
/*      */       // Line number table:
/*      */       //   Java source line #338	-> byte code offset #0
/*      */       //   Java source line #339	-> byte code offset #7
/*      */       //   Java source line #340	-> byte code offset #19
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	24	0	this	SynchronizedSortedSet<E>
/*      */       //   5	16	1	Ljava/lang/Object;	Object
/*      */       //   19	4	2	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	18	19	finally
/*      */       //   19	22	19	finally
/*      */     }
/*      */   }
/*      */   
/*      */   private static <E> List<E> list(List<E> list, @Nullable Object mutex)
/*      */   {
/*  347 */     return (list instanceof RandomAccess) ? new SynchronizedRandomAccessList(list, mutex) : new SynchronizedList(list, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedList<E> extends Synchronized.SynchronizedCollection<E> implements List<E> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedList(List<E> delegate, @Nullable Object mutex) {
/*  354 */       super(mutex, null);
/*      */     }
/*      */     
/*      */     List<E> delegate()
/*      */     {
/*  359 */       return (List)super.delegate();
/*      */     }
/*      */     
/*      */     public void add(int index, E element)
/*      */     {
/*  364 */       synchronized (this.mutex) {
/*  365 */         delegate().add(index, element);
/*      */       }
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public boolean addAll(int index, Collection<? extends E> c)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedList:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_3
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedList:delegate	()Ljava/util/List;
/*      */       //   11: iload_1
/*      */       //   12: aload_2
/*      */       //   13: invokeinterface 7 3 0
/*      */       //   18: aload_3
/*      */       //   19: monitorexit
/*      */       //   20: ireturn
/*      */       //   21: astore 4
/*      */       //   23: aload_3
/*      */       //   24: monitorexit
/*      */       //   25: aload 4
/*      */       //   27: athrow
/*      */       // Line number table:
/*      */       //   Java source line #371	-> byte code offset #0
/*      */       //   Java source line #372	-> byte code offset #7
/*      */       //   Java source line #373	-> byte code offset #21
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	28	0	this	SynchronizedList<E>
/*      */       //   0	28	1	index	int
/*      */       //   0	28	2	c	Collection<? extends E>
/*      */       //   5	19	3	Ljava/lang/Object;	Object
/*      */       //   21	5	4	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	20	21	finally
/*      */       //   21	25	21	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public E get(int index)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedList:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_2
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedList:delegate	()Ljava/util/List;
/*      */       //   11: iload_1
/*      */       //   12: invokeinterface 8 2 0
/*      */       //   17: aload_2
/*      */       //   18: monitorexit
/*      */       //   19: areturn
/*      */       //   20: astore_3
/*      */       //   21: aload_2
/*      */       //   22: monitorexit
/*      */       //   23: aload_3
/*      */       //   24: athrow
/*      */       // Line number table:
/*      */       //   Java source line #378	-> byte code offset #0
/*      */       //   Java source line #379	-> byte code offset #7
/*      */       //   Java source line #380	-> byte code offset #20
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	25	0	this	SynchronizedList<E>
/*      */       //   0	25	1	index	int
/*      */       //   5	17	2	Ljava/lang/Object;	Object
/*      */       //   20	4	3	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	19	20	finally
/*      */       //   20	23	20	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public int indexOf(Object o)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedList:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_2
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedList:delegate	()Ljava/util/List;
/*      */       //   11: aload_1
/*      */       //   12: invokeinterface 9 2 0
/*      */       //   17: aload_2
/*      */       //   18: monitorexit
/*      */       //   19: ireturn
/*      */       //   20: astore_3
/*      */       //   21: aload_2
/*      */       //   22: monitorexit
/*      */       //   23: aload_3
/*      */       //   24: athrow
/*      */       // Line number table:
/*      */       //   Java source line #385	-> byte code offset #0
/*      */       //   Java source line #386	-> byte code offset #7
/*      */       //   Java source line #387	-> byte code offset #20
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	25	0	this	SynchronizedList<E>
/*      */       //   0	25	1	o	Object
/*      */       //   5	17	2	Ljava/lang/Object;	Object
/*      */       //   20	4	3	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	19	20	finally
/*      */       //   20	23	20	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public int lastIndexOf(Object o)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedList:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_2
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedList:delegate	()Ljava/util/List;
/*      */       //   11: aload_1
/*      */       //   12: invokeinterface 10 2 0
/*      */       //   17: aload_2
/*      */       //   18: monitorexit
/*      */       //   19: ireturn
/*      */       //   20: astore_3
/*      */       //   21: aload_2
/*      */       //   22: monitorexit
/*      */       //   23: aload_3
/*      */       //   24: athrow
/*      */       // Line number table:
/*      */       //   Java source line #392	-> byte code offset #0
/*      */       //   Java source line #393	-> byte code offset #7
/*      */       //   Java source line #394	-> byte code offset #20
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	25	0	this	SynchronizedList<E>
/*      */       //   0	25	1	o	Object
/*      */       //   5	17	2	Ljava/lang/Object;	Object
/*      */       //   20	4	3	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	19	20	finally
/*      */       //   20	23	20	finally
/*      */     }
/*      */     
/*      */     public ListIterator<E> listIterator()
/*      */     {
/*  399 */       return delegate().listIterator();
/*      */     }
/*      */     
/*      */     public ListIterator<E> listIterator(int index)
/*      */     {
/*  404 */       return delegate().listIterator(index);
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public E remove(int index)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedList:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_2
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedList:delegate	()Ljava/util/List;
/*      */       //   11: iload_1
/*      */       //   12: invokeinterface 13 2 0
/*      */       //   17: aload_2
/*      */       //   18: monitorexit
/*      */       //   19: areturn
/*      */       //   20: astore_3
/*      */       //   21: aload_2
/*      */       //   22: monitorexit
/*      */       //   23: aload_3
/*      */       //   24: athrow
/*      */       // Line number table:
/*      */       //   Java source line #409	-> byte code offset #0
/*      */       //   Java source line #410	-> byte code offset #7
/*      */       //   Java source line #411	-> byte code offset #20
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	25	0	this	SynchronizedList<E>
/*      */       //   0	25	1	index	int
/*      */       //   5	17	2	Ljava/lang/Object;	Object
/*      */       //   20	4	3	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	19	20	finally
/*      */       //   20	23	20	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public E set(int index, E element)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedList:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_3
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedList:delegate	()Ljava/util/List;
/*      */       //   11: iload_1
/*      */       //   12: aload_2
/*      */       //   13: invokeinterface 14 3 0
/*      */       //   18: aload_3
/*      */       //   19: monitorexit
/*      */       //   20: areturn
/*      */       //   21: astore 4
/*      */       //   23: aload_3
/*      */       //   24: monitorexit
/*      */       //   25: aload 4
/*      */       //   27: athrow
/*      */       // Line number table:
/*      */       //   Java source line #416	-> byte code offset #0
/*      */       //   Java source line #417	-> byte code offset #7
/*      */       //   Java source line #418	-> byte code offset #21
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	28	0	this	SynchronizedList<E>
/*      */       //   0	28	1	index	int
/*      */       //   0	28	2	element	E
/*      */       //   5	19	3	Ljava/lang/Object;	Object
/*      */       //   21	5	4	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	20	21	finally
/*      */       //   21	25	21	finally
/*      */     }
/*      */     
/*      */     public void replaceAll(UnaryOperator<E> operator)
/*      */     {
/*  423 */       synchronized (this.mutex) {
/*  424 */         delegate().replaceAll(operator);
/*      */       }
/*      */     }
/*      */     
/*      */     public void sort(Comparator<? super E> c)
/*      */     {
/*  430 */       synchronized (this.mutex) {
/*  431 */         delegate().sort(c);
/*      */       }
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public List<E> subList(int fromIndex, int toIndex)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedList:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_3
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedList:delegate	()Ljava/util/List;
/*      */       //   11: iload_1
/*      */       //   12: iload_2
/*      */       //   13: invokeinterface 17 3 0
/*      */       //   18: aload_0
/*      */       //   19: getfield 4	com/google/common/collect/Synchronized$SynchronizedList:mutex	Ljava/lang/Object;
/*      */       //   22: invokestatic 18	com/google/common/collect/Synchronized:access$200	(Ljava/util/List;Ljava/lang/Object;)Ljava/util/List;
/*      */       //   25: aload_3
/*      */       //   26: monitorexit
/*      */       //   27: areturn
/*      */       //   28: astore 4
/*      */       //   30: aload_3
/*      */       //   31: monitorexit
/*      */       //   32: aload 4
/*      */       //   34: athrow
/*      */       // Line number table:
/*      */       //   Java source line #437	-> byte code offset #0
/*      */       //   Java source line #438	-> byte code offset #7
/*      */       //   Java source line #439	-> byte code offset #28
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	35	0	this	SynchronizedList<E>
/*      */       //   0	35	1	fromIndex	int
/*      */       //   0	35	2	toIndex	int
/*      */       //   5	26	3	Ljava/lang/Object;	Object
/*      */       //   28	5	4	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	27	28	finally
/*      */       //   28	32	28	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public boolean equals(Object o)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_1
/*      */       //   1: aload_0
/*      */       //   2: if_acmpne +5 -> 7
/*      */       //   5: iconst_1
/*      */       //   6: ireturn
/*      */       //   7: aload_0
/*      */       //   8: getfield 4	com/google/common/collect/Synchronized$SynchronizedList:mutex	Ljava/lang/Object;
/*      */       //   11: dup
/*      */       //   12: astore_2
/*      */       //   13: monitorenter
/*      */       //   14: aload_0
/*      */       //   15: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedList:delegate	()Ljava/util/List;
/*      */       //   18: aload_1
/*      */       //   19: invokeinterface 19 2 0
/*      */       //   24: aload_2
/*      */       //   25: monitorexit
/*      */       //   26: ireturn
/*      */       //   27: astore_3
/*      */       //   28: aload_2
/*      */       //   29: monitorexit
/*      */       //   30: aload_3
/*      */       //   31: athrow
/*      */       // Line number table:
/*      */       //   Java source line #444	-> byte code offset #0
/*      */       //   Java source line #445	-> byte code offset #5
/*      */       //   Java source line #447	-> byte code offset #7
/*      */       //   Java source line #448	-> byte code offset #14
/*      */       //   Java source line #449	-> byte code offset #27
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	32	0	this	SynchronizedList<E>
/*      */       //   0	32	1	o	Object
/*      */       //   12	17	2	Ljava/lang/Object;	Object
/*      */       //   27	4	3	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   14	26	27	finally
/*      */       //   27	30	27	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public int hashCode()
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedList:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_1
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedList:delegate	()Ljava/util/List;
/*      */       //   11: invokeinterface 20 1 0
/*      */       //   16: aload_1
/*      */       //   17: monitorexit
/*      */       //   18: ireturn
/*      */       //   19: astore_2
/*      */       //   20: aload_1
/*      */       //   21: monitorexit
/*      */       //   22: aload_2
/*      */       //   23: athrow
/*      */       // Line number table:
/*      */       //   Java source line #454	-> byte code offset #0
/*      */       //   Java source line #455	-> byte code offset #7
/*      */       //   Java source line #456	-> byte code offset #19
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	24	0	this	SynchronizedList<E>
/*      */       //   5	16	1	Ljava/lang/Object;	Object
/*      */       //   19	4	2	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	18	19	finally
/*      */       //   19	22	19	finally
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SynchronizedRandomAccessList<E>
/*      */     extends Synchronized.SynchronizedList<E>
/*      */     implements RandomAccess
/*      */   {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedRandomAccessList(List<E> list, @Nullable Object mutex)
/*      */     {
/*  465 */       super(mutex);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   static <E> Multiset<E> multiset(Multiset<E> multiset, @Nullable Object mutex)
/*      */   {
/*  472 */     if (((multiset instanceof SynchronizedMultiset)) || ((multiset instanceof ImmutableMultiset))) {
/*  473 */       return multiset;
/*      */     }
/*  475 */     return new SynchronizedMultiset(multiset, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedMultiset<E> extends Synchronized.SynchronizedCollection<E> implements Multiset<E> {
/*      */     transient Set<E> elementSet;
/*      */     transient Set<Multiset.Entry<E>> entrySet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedMultiset(Multiset<E> delegate, @Nullable Object mutex) {
/*  484 */       super(mutex, null);
/*      */     }
/*      */     
/*      */     Multiset<E> delegate()
/*      */     {
/*  489 */       return (Multiset)super.delegate();
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public int count(Object o)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMultiset:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_2
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMultiset:delegate	()Lcom/google/common/collect/Multiset;
/*      */       //   11: aload_1
/*      */       //   12: invokeinterface 6 2 0
/*      */       //   17: aload_2
/*      */       //   18: monitorexit
/*      */       //   19: ireturn
/*      */       //   20: astore_3
/*      */       //   21: aload_2
/*      */       //   22: monitorexit
/*      */       //   23: aload_3
/*      */       //   24: athrow
/*      */       // Line number table:
/*      */       //   Java source line #494	-> byte code offset #0
/*      */       //   Java source line #495	-> byte code offset #7
/*      */       //   Java source line #496	-> byte code offset #20
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	25	0	this	SynchronizedMultiset<E>
/*      */       //   0	25	1	o	Object
/*      */       //   5	17	2	Ljava/lang/Object;	Object
/*      */       //   20	4	3	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	19	20	finally
/*      */       //   20	23	20	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public int add(E e, int n)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMultiset:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_3
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMultiset:delegate	()Lcom/google/common/collect/Multiset;
/*      */       //   11: aload_1
/*      */       //   12: iload_2
/*      */       //   13: invokeinterface 7 3 0
/*      */       //   18: aload_3
/*      */       //   19: monitorexit
/*      */       //   20: ireturn
/*      */       //   21: astore 4
/*      */       //   23: aload_3
/*      */       //   24: monitorexit
/*      */       //   25: aload 4
/*      */       //   27: athrow
/*      */       // Line number table:
/*      */       //   Java source line #501	-> byte code offset #0
/*      */       //   Java source line #502	-> byte code offset #7
/*      */       //   Java source line #503	-> byte code offset #21
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	28	0	this	SynchronizedMultiset<E>
/*      */       //   0	28	1	e	E
/*      */       //   0	28	2	n	int
/*      */       //   5	19	3	Ljava/lang/Object;	Object
/*      */       //   21	5	4	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	20	21	finally
/*      */       //   21	25	21	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public int remove(Object o, int n)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMultiset:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_3
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMultiset:delegate	()Lcom/google/common/collect/Multiset;
/*      */       //   11: aload_1
/*      */       //   12: iload_2
/*      */       //   13: invokeinterface 8 3 0
/*      */       //   18: aload_3
/*      */       //   19: monitorexit
/*      */       //   20: ireturn
/*      */       //   21: astore 4
/*      */       //   23: aload_3
/*      */       //   24: monitorexit
/*      */       //   25: aload 4
/*      */       //   27: athrow
/*      */       // Line number table:
/*      */       //   Java source line #508	-> byte code offset #0
/*      */       //   Java source line #509	-> byte code offset #7
/*      */       //   Java source line #510	-> byte code offset #21
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	28	0	this	SynchronizedMultiset<E>
/*      */       //   0	28	1	o	Object
/*      */       //   0	28	2	n	int
/*      */       //   5	19	3	Ljava/lang/Object;	Object
/*      */       //   21	5	4	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	20	21	finally
/*      */       //   21	25	21	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public int setCount(E element, int count)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMultiset:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_3
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMultiset:delegate	()Lcom/google/common/collect/Multiset;
/*      */       //   11: aload_1
/*      */       //   12: iload_2
/*      */       //   13: invokeinterface 9 3 0
/*      */       //   18: aload_3
/*      */       //   19: monitorexit
/*      */       //   20: ireturn
/*      */       //   21: astore 4
/*      */       //   23: aload_3
/*      */       //   24: monitorexit
/*      */       //   25: aload 4
/*      */       //   27: athrow
/*      */       // Line number table:
/*      */       //   Java source line #515	-> byte code offset #0
/*      */       //   Java source line #516	-> byte code offset #7
/*      */       //   Java source line #517	-> byte code offset #21
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	28	0	this	SynchronizedMultiset<E>
/*      */       //   0	28	1	element	E
/*      */       //   0	28	2	count	int
/*      */       //   5	19	3	Ljava/lang/Object;	Object
/*      */       //   21	5	4	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	20	21	finally
/*      */       //   21	25	21	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public boolean setCount(E element, int oldCount, int newCount)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMultiset:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore 4
/*      */       //   7: monitorenter
/*      */       //   8: aload_0
/*      */       //   9: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMultiset:delegate	()Lcom/google/common/collect/Multiset;
/*      */       //   12: aload_1
/*      */       //   13: iload_2
/*      */       //   14: iload_3
/*      */       //   15: invokeinterface 10 4 0
/*      */       //   20: aload 4
/*      */       //   22: monitorexit
/*      */       //   23: ireturn
/*      */       //   24: astore 5
/*      */       //   26: aload 4
/*      */       //   28: monitorexit
/*      */       //   29: aload 5
/*      */       //   31: athrow
/*      */       // Line number table:
/*      */       //   Java source line #522	-> byte code offset #0
/*      */       //   Java source line #523	-> byte code offset #8
/*      */       //   Java source line #524	-> byte code offset #24
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	32	0	this	SynchronizedMultiset<E>
/*      */       //   0	32	1	element	E
/*      */       //   0	32	2	oldCount	int
/*      */       //   0	32	3	newCount	int
/*      */       //   5	22	4	Ljava/lang/Object;	Object
/*      */       //   24	6	5	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   8	23	24	finally
/*      */       //   24	29	24	finally
/*      */     }
/*      */     
/*      */     public Set<E> elementSet()
/*      */     {
/*  529 */       synchronized (this.mutex) {
/*  530 */         if (this.elementSet == null) {
/*  531 */           this.elementSet = Synchronized.typePreservingSet(delegate().elementSet(), this.mutex);
/*      */         }
/*  533 */         return this.elementSet;
/*      */       }
/*      */     }
/*      */     
/*      */     public Set<Multiset.Entry<E>> entrySet()
/*      */     {
/*  539 */       synchronized (this.mutex) {
/*  540 */         if (this.entrySet == null) {
/*  541 */           this.entrySet = Synchronized.typePreservingSet(delegate().entrySet(), this.mutex);
/*      */         }
/*  543 */         return this.entrySet;
/*      */       }
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public boolean equals(Object o)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_1
/*      */       //   1: aload_0
/*      */       //   2: if_acmpne +5 -> 7
/*      */       //   5: iconst_1
/*      */       //   6: ireturn
/*      */       //   7: aload_0
/*      */       //   8: getfield 4	com/google/common/collect/Synchronized$SynchronizedMultiset:mutex	Ljava/lang/Object;
/*      */       //   11: dup
/*      */       //   12: astore_2
/*      */       //   13: monitorenter
/*      */       //   14: aload_0
/*      */       //   15: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMultiset:delegate	()Lcom/google/common/collect/Multiset;
/*      */       //   18: aload_1
/*      */       //   19: invokeinterface 16 2 0
/*      */       //   24: aload_2
/*      */       //   25: monitorexit
/*      */       //   26: ireturn
/*      */       //   27: astore_3
/*      */       //   28: aload_2
/*      */       //   29: monitorexit
/*      */       //   30: aload_3
/*      */       //   31: athrow
/*      */       // Line number table:
/*      */       //   Java source line #549	-> byte code offset #0
/*      */       //   Java source line #550	-> byte code offset #5
/*      */       //   Java source line #552	-> byte code offset #7
/*      */       //   Java source line #553	-> byte code offset #14
/*      */       //   Java source line #554	-> byte code offset #27
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	32	0	this	SynchronizedMultiset<E>
/*      */       //   0	32	1	o	Object
/*      */       //   12	17	2	Ljava/lang/Object;	Object
/*      */       //   27	4	3	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   14	26	27	finally
/*      */       //   27	30	27	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public int hashCode()
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMultiset:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_1
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMultiset:delegate	()Lcom/google/common/collect/Multiset;
/*      */       //   11: invokeinterface 17 1 0
/*      */       //   16: aload_1
/*      */       //   17: monitorexit
/*      */       //   18: ireturn
/*      */       //   19: astore_2
/*      */       //   20: aload_1
/*      */       //   21: monitorexit
/*      */       //   22: aload_2
/*      */       //   23: athrow
/*      */       // Line number table:
/*      */       //   Java source line #559	-> byte code offset #0
/*      */       //   Java source line #560	-> byte code offset #7
/*      */       //   Java source line #561	-> byte code offset #19
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	24	0	this	SynchronizedMultiset<E>
/*      */       //   5	16	1	Ljava/lang/Object;	Object
/*      */       //   19	4	2	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	18	19	finally
/*      */       //   19	22	19	finally
/*      */     }
/*      */   }
/*      */   
/*      */   static <K, V> Multimap<K, V> multimap(Multimap<K, V> multimap, @Nullable Object mutex)
/*      */   {
/*  568 */     if (((multimap instanceof SynchronizedMultimap)) || ((multimap instanceof ImmutableMultimap))) {
/*  569 */       return multimap;
/*      */     }
/*  571 */     return new SynchronizedMultimap(multimap, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedMultimap<K, V> extends Synchronized.SynchronizedObject implements Multimap<K, V>
/*      */   {
/*      */     transient Set<K> keySet;
/*      */     transient Collection<V> valuesCollection;
/*      */     transient Collection<Map.Entry<K, V>> entries;
/*      */     transient Map<K, Collection<V>> asMap;
/*      */     transient Multiset<K> keys;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     Multimap<K, V> delegate()
/*      */     {
/*  585 */       return (Multimap)super.delegate();
/*      */     }
/*      */     
/*      */     SynchronizedMultimap(Multimap<K, V> delegate, @Nullable Object mutex) {
/*  589 */       super(mutex);
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public int size()
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMultimap:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_1
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMultimap:delegate	()Lcom/google/common/collect/Multimap;
/*      */       //   11: invokeinterface 6 1 0
/*      */       //   16: aload_1
/*      */       //   17: monitorexit
/*      */       //   18: ireturn
/*      */       //   19: astore_2
/*      */       //   20: aload_1
/*      */       //   21: monitorexit
/*      */       //   22: aload_2
/*      */       //   23: athrow
/*      */       // Line number table:
/*      */       //   Java source line #594	-> byte code offset #0
/*      */       //   Java source line #595	-> byte code offset #7
/*      */       //   Java source line #596	-> byte code offset #19
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	24	0	this	SynchronizedMultimap<K, V>
/*      */       //   5	16	1	Ljava/lang/Object;	Object
/*      */       //   19	4	2	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	18	19	finally
/*      */       //   19	22	19	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public boolean isEmpty()
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMultimap:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_1
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMultimap:delegate	()Lcom/google/common/collect/Multimap;
/*      */       //   11: invokeinterface 7 1 0
/*      */       //   16: aload_1
/*      */       //   17: monitorexit
/*      */       //   18: ireturn
/*      */       //   19: astore_2
/*      */       //   20: aload_1
/*      */       //   21: monitorexit
/*      */       //   22: aload_2
/*      */       //   23: athrow
/*      */       // Line number table:
/*      */       //   Java source line #601	-> byte code offset #0
/*      */       //   Java source line #602	-> byte code offset #7
/*      */       //   Java source line #603	-> byte code offset #19
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	24	0	this	SynchronizedMultimap<K, V>
/*      */       //   5	16	1	Ljava/lang/Object;	Object
/*      */       //   19	4	2	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	18	19	finally
/*      */       //   19	22	19	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public boolean containsKey(Object key)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMultimap:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_2
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMultimap:delegate	()Lcom/google/common/collect/Multimap;
/*      */       //   11: aload_1
/*      */       //   12: invokeinterface 8 2 0
/*      */       //   17: aload_2
/*      */       //   18: monitorexit
/*      */       //   19: ireturn
/*      */       //   20: astore_3
/*      */       //   21: aload_2
/*      */       //   22: monitorexit
/*      */       //   23: aload_3
/*      */       //   24: athrow
/*      */       // Line number table:
/*      */       //   Java source line #608	-> byte code offset #0
/*      */       //   Java source line #609	-> byte code offset #7
/*      */       //   Java source line #610	-> byte code offset #20
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	25	0	this	SynchronizedMultimap<K, V>
/*      */       //   0	25	1	key	Object
/*      */       //   5	17	2	Ljava/lang/Object;	Object
/*      */       //   20	4	3	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	19	20	finally
/*      */       //   20	23	20	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public boolean containsValue(Object value)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMultimap:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_2
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMultimap:delegate	()Lcom/google/common/collect/Multimap;
/*      */       //   11: aload_1
/*      */       //   12: invokeinterface 9 2 0
/*      */       //   17: aload_2
/*      */       //   18: monitorexit
/*      */       //   19: ireturn
/*      */       //   20: astore_3
/*      */       //   21: aload_2
/*      */       //   22: monitorexit
/*      */       //   23: aload_3
/*      */       //   24: athrow
/*      */       // Line number table:
/*      */       //   Java source line #615	-> byte code offset #0
/*      */       //   Java source line #616	-> byte code offset #7
/*      */       //   Java source line #617	-> byte code offset #20
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	25	0	this	SynchronizedMultimap<K, V>
/*      */       //   0	25	1	value	Object
/*      */       //   5	17	2	Ljava/lang/Object;	Object
/*      */       //   20	4	3	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	19	20	finally
/*      */       //   20	23	20	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public boolean containsEntry(Object key, Object value)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMultimap:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_3
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMultimap:delegate	()Lcom/google/common/collect/Multimap;
/*      */       //   11: aload_1
/*      */       //   12: aload_2
/*      */       //   13: invokeinterface 10 3 0
/*      */       //   18: aload_3
/*      */       //   19: monitorexit
/*      */       //   20: ireturn
/*      */       //   21: astore 4
/*      */       //   23: aload_3
/*      */       //   24: monitorexit
/*      */       //   25: aload 4
/*      */       //   27: athrow
/*      */       // Line number table:
/*      */       //   Java source line #622	-> byte code offset #0
/*      */       //   Java source line #623	-> byte code offset #7
/*      */       //   Java source line #624	-> byte code offset #21
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	28	0	this	SynchronizedMultimap<K, V>
/*      */       //   0	28	1	key	Object
/*      */       //   0	28	2	value	Object
/*      */       //   5	19	3	Ljava/lang/Object;	Object
/*      */       //   21	5	4	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	20	21	finally
/*      */       //   21	25	21	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public Collection<V> get(K key)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMultimap:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_2
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMultimap:delegate	()Lcom/google/common/collect/Multimap;
/*      */       //   11: aload_1
/*      */       //   12: invokeinterface 11 2 0
/*      */       //   17: aload_0
/*      */       //   18: getfield 4	com/google/common/collect/Synchronized$SynchronizedMultimap:mutex	Ljava/lang/Object;
/*      */       //   21: invokestatic 12	com/google/common/collect/Synchronized:access$400	(Ljava/util/Collection;Ljava/lang/Object;)Ljava/util/Collection;
/*      */       //   24: aload_2
/*      */       //   25: monitorexit
/*      */       //   26: areturn
/*      */       //   27: astore_3
/*      */       //   28: aload_2
/*      */       //   29: monitorexit
/*      */       //   30: aload_3
/*      */       //   31: athrow
/*      */       // Line number table:
/*      */       //   Java source line #629	-> byte code offset #0
/*      */       //   Java source line #630	-> byte code offset #7
/*      */       //   Java source line #631	-> byte code offset #27
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	32	0	this	SynchronizedMultimap<K, V>
/*      */       //   0	32	1	key	K
/*      */       //   5	24	2	Ljava/lang/Object;	Object
/*      */       //   27	4	3	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	26	27	finally
/*      */       //   27	30	27	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public boolean put(K key, V value)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMultimap:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_3
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMultimap:delegate	()Lcom/google/common/collect/Multimap;
/*      */       //   11: aload_1
/*      */       //   12: aload_2
/*      */       //   13: invokeinterface 13 3 0
/*      */       //   18: aload_3
/*      */       //   19: monitorexit
/*      */       //   20: ireturn
/*      */       //   21: astore 4
/*      */       //   23: aload_3
/*      */       //   24: monitorexit
/*      */       //   25: aload 4
/*      */       //   27: athrow
/*      */       // Line number table:
/*      */       //   Java source line #636	-> byte code offset #0
/*      */       //   Java source line #637	-> byte code offset #7
/*      */       //   Java source line #638	-> byte code offset #21
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	28	0	this	SynchronizedMultimap<K, V>
/*      */       //   0	28	1	key	K
/*      */       //   0	28	2	value	V
/*      */       //   5	19	3	Ljava/lang/Object;	Object
/*      */       //   21	5	4	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	20	21	finally
/*      */       //   21	25	21	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public boolean putAll(K key, Iterable<? extends V> values)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMultimap:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_3
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMultimap:delegate	()Lcom/google/common/collect/Multimap;
/*      */       //   11: aload_1
/*      */       //   12: aload_2
/*      */       //   13: invokeinterface 14 3 0
/*      */       //   18: aload_3
/*      */       //   19: monitorexit
/*      */       //   20: ireturn
/*      */       //   21: astore 4
/*      */       //   23: aload_3
/*      */       //   24: monitorexit
/*      */       //   25: aload 4
/*      */       //   27: athrow
/*      */       // Line number table:
/*      */       //   Java source line #643	-> byte code offset #0
/*      */       //   Java source line #644	-> byte code offset #7
/*      */       //   Java source line #645	-> byte code offset #21
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	28	0	this	SynchronizedMultimap<K, V>
/*      */       //   0	28	1	key	K
/*      */       //   0	28	2	values	Iterable<? extends V>
/*      */       //   5	19	3	Ljava/lang/Object;	Object
/*      */       //   21	5	4	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	20	21	finally
/*      */       //   21	25	21	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public boolean putAll(Multimap<? extends K, ? extends V> multimap)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMultimap:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_2
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMultimap:delegate	()Lcom/google/common/collect/Multimap;
/*      */       //   11: aload_1
/*      */       //   12: invokeinterface 15 2 0
/*      */       //   17: aload_2
/*      */       //   18: monitorexit
/*      */       //   19: ireturn
/*      */       //   20: astore_3
/*      */       //   21: aload_2
/*      */       //   22: monitorexit
/*      */       //   23: aload_3
/*      */       //   24: athrow
/*      */       // Line number table:
/*      */       //   Java source line #650	-> byte code offset #0
/*      */       //   Java source line #651	-> byte code offset #7
/*      */       //   Java source line #652	-> byte code offset #20
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	25	0	this	SynchronizedMultimap<K, V>
/*      */       //   0	25	1	multimap	Multimap<? extends K, ? extends V>
/*      */       //   5	17	2	Ljava/lang/Object;	Object
/*      */       //   20	4	3	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	19	20	finally
/*      */       //   20	23	20	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public Collection<V> replaceValues(K key, Iterable<? extends V> values)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMultimap:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_3
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMultimap:delegate	()Lcom/google/common/collect/Multimap;
/*      */       //   11: aload_1
/*      */       //   12: aload_2
/*      */       //   13: invokeinterface 16 3 0
/*      */       //   18: aload_3
/*      */       //   19: monitorexit
/*      */       //   20: areturn
/*      */       //   21: astore 4
/*      */       //   23: aload_3
/*      */       //   24: monitorexit
/*      */       //   25: aload 4
/*      */       //   27: athrow
/*      */       // Line number table:
/*      */       //   Java source line #657	-> byte code offset #0
/*      */       //   Java source line #658	-> byte code offset #7
/*      */       //   Java source line #659	-> byte code offset #21
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	28	0	this	SynchronizedMultimap<K, V>
/*      */       //   0	28	1	key	K
/*      */       //   0	28	2	values	Iterable<? extends V>
/*      */       //   5	19	3	Ljava/lang/Object;	Object
/*      */       //   21	5	4	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	20	21	finally
/*      */       //   21	25	21	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public boolean remove(Object key, Object value)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMultimap:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_3
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMultimap:delegate	()Lcom/google/common/collect/Multimap;
/*      */       //   11: aload_1
/*      */       //   12: aload_2
/*      */       //   13: invokeinterface 17 3 0
/*      */       //   18: aload_3
/*      */       //   19: monitorexit
/*      */       //   20: ireturn
/*      */       //   21: astore 4
/*      */       //   23: aload_3
/*      */       //   24: monitorexit
/*      */       //   25: aload 4
/*      */       //   27: athrow
/*      */       // Line number table:
/*      */       //   Java source line #664	-> byte code offset #0
/*      */       //   Java source line #665	-> byte code offset #7
/*      */       //   Java source line #666	-> byte code offset #21
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	28	0	this	SynchronizedMultimap<K, V>
/*      */       //   0	28	1	key	Object
/*      */       //   0	28	2	value	Object
/*      */       //   5	19	3	Ljava/lang/Object;	Object
/*      */       //   21	5	4	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	20	21	finally
/*      */       //   21	25	21	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public Collection<V> removeAll(Object key)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMultimap:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_2
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMultimap:delegate	()Lcom/google/common/collect/Multimap;
/*      */       //   11: aload_1
/*      */       //   12: invokeinterface 18 2 0
/*      */       //   17: aload_2
/*      */       //   18: monitorexit
/*      */       //   19: areturn
/*      */       //   20: astore_3
/*      */       //   21: aload_2
/*      */       //   22: monitorexit
/*      */       //   23: aload_3
/*      */       //   24: athrow
/*      */       // Line number table:
/*      */       //   Java source line #671	-> byte code offset #0
/*      */       //   Java source line #672	-> byte code offset #7
/*      */       //   Java source line #673	-> byte code offset #20
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	25	0	this	SynchronizedMultimap<K, V>
/*      */       //   0	25	1	key	Object
/*      */       //   5	17	2	Ljava/lang/Object;	Object
/*      */       //   20	4	3	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	19	20	finally
/*      */       //   20	23	20	finally
/*      */     }
/*      */     
/*      */     public void clear()
/*      */     {
/*  678 */       synchronized (this.mutex) {
/*  679 */         delegate().clear();
/*      */       }
/*      */     }
/*      */     
/*      */     public Set<K> keySet()
/*      */     {
/*  685 */       synchronized (this.mutex) {
/*  686 */         if (this.keySet == null) {
/*  687 */           this.keySet = Synchronized.typePreservingSet(delegate().keySet(), this.mutex);
/*      */         }
/*  689 */         return this.keySet;
/*      */       }
/*      */     }
/*      */     
/*      */     public Collection<V> values()
/*      */     {
/*  695 */       synchronized (this.mutex) {
/*  696 */         if (this.valuesCollection == null) {
/*  697 */           this.valuesCollection = Synchronized.collection(delegate().values(), this.mutex);
/*      */         }
/*  699 */         return this.valuesCollection;
/*      */       }
/*      */     }
/*      */     
/*      */     public Collection<Map.Entry<K, V>> entries()
/*      */     {
/*  705 */       synchronized (this.mutex) {
/*  706 */         if (this.entries == null) {
/*  707 */           this.entries = Synchronized.typePreservingCollection(delegate().entries(), this.mutex);
/*      */         }
/*  709 */         return this.entries;
/*      */       }
/*      */     }
/*      */     
/*      */     public void forEach(BiConsumer<? super K, ? super V> action)
/*      */     {
/*  715 */       synchronized (this.mutex) {
/*  716 */         delegate().forEach(action);
/*      */       }
/*      */     }
/*      */     
/*      */     public Map<K, Collection<V>> asMap()
/*      */     {
/*  722 */       synchronized (this.mutex) {
/*  723 */         if (this.asMap == null) {
/*  724 */           this.asMap = new Synchronized.SynchronizedAsMap(delegate().asMap(), this.mutex);
/*      */         }
/*  726 */         return this.asMap;
/*      */       }
/*      */     }
/*      */     
/*      */     public Multiset<K> keys()
/*      */     {
/*  732 */       synchronized (this.mutex) {
/*  733 */         if (this.keys == null) {
/*  734 */           this.keys = Synchronized.multiset(delegate().keys(), this.mutex);
/*      */         }
/*  736 */         return this.keys;
/*      */       }
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public boolean equals(Object o)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_1
/*      */       //   1: aload_0
/*      */       //   2: if_acmpne +5 -> 7
/*      */       //   5: iconst_1
/*      */       //   6: ireturn
/*      */       //   7: aload_0
/*      */       //   8: getfield 4	com/google/common/collect/Synchronized$SynchronizedMultimap:mutex	Ljava/lang/Object;
/*      */       //   11: dup
/*      */       //   12: astore_2
/*      */       //   13: monitorenter
/*      */       //   14: aload_0
/*      */       //   15: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMultimap:delegate	()Lcom/google/common/collect/Multimap;
/*      */       //   18: aload_1
/*      */       //   19: invokeinterface 36 2 0
/*      */       //   24: aload_2
/*      */       //   25: monitorexit
/*      */       //   26: ireturn
/*      */       //   27: astore_3
/*      */       //   28: aload_2
/*      */       //   29: monitorexit
/*      */       //   30: aload_3
/*      */       //   31: athrow
/*      */       // Line number table:
/*      */       //   Java source line #742	-> byte code offset #0
/*      */       //   Java source line #743	-> byte code offset #5
/*      */       //   Java source line #745	-> byte code offset #7
/*      */       //   Java source line #746	-> byte code offset #14
/*      */       //   Java source line #747	-> byte code offset #27
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	32	0	this	SynchronizedMultimap<K, V>
/*      */       //   0	32	1	o	Object
/*      */       //   12	17	2	Ljava/lang/Object;	Object
/*      */       //   27	4	3	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   14	26	27	finally
/*      */       //   27	30	27	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public int hashCode()
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMultimap:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_1
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMultimap:delegate	()Lcom/google/common/collect/Multimap;
/*      */       //   11: invokeinterface 37 1 0
/*      */       //   16: aload_1
/*      */       //   17: monitorexit
/*      */       //   18: ireturn
/*      */       //   19: astore_2
/*      */       //   20: aload_1
/*      */       //   21: monitorexit
/*      */       //   22: aload_2
/*      */       //   23: athrow
/*      */       // Line number table:
/*      */       //   Java source line #752	-> byte code offset #0
/*      */       //   Java source line #753	-> byte code offset #7
/*      */       //   Java source line #754	-> byte code offset #19
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	24	0	this	SynchronizedMultimap<K, V>
/*      */       //   5	16	1	Ljava/lang/Object;	Object
/*      */       //   19	4	2	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	18	19	finally
/*      */       //   19	22	19	finally
/*      */     }
/*      */   }
/*      */   
/*      */   static <K, V> ListMultimap<K, V> listMultimap(ListMultimap<K, V> multimap, @Nullable Object mutex)
/*      */   {
/*  762 */     if (((multimap instanceof SynchronizedListMultimap)) || ((multimap instanceof ImmutableListMultimap))) {
/*  763 */       return multimap;
/*      */     }
/*  765 */     return new SynchronizedListMultimap(multimap, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedListMultimap<K, V> extends Synchronized.SynchronizedMultimap<K, V> implements ListMultimap<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*  771 */     SynchronizedListMultimap(ListMultimap<K, V> delegate, @Nullable Object mutex) { super(mutex); }
/*      */     
/*      */ 
/*      */     ListMultimap<K, V> delegate()
/*      */     {
/*  776 */       return (ListMultimap)super.delegate();
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public List<V> get(K key)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedListMultimap:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_2
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedListMultimap:delegate	()Lcom/google/common/collect/ListMultimap;
/*      */       //   11: aload_1
/*      */       //   12: invokeinterface 6 2 0
/*      */       //   17: aload_0
/*      */       //   18: getfield 4	com/google/common/collect/Synchronized$SynchronizedListMultimap:mutex	Ljava/lang/Object;
/*      */       //   21: invokestatic 7	com/google/common/collect/Synchronized:access$200	(Ljava/util/List;Ljava/lang/Object;)Ljava/util/List;
/*      */       //   24: aload_2
/*      */       //   25: monitorexit
/*      */       //   26: areturn
/*      */       //   27: astore_3
/*      */       //   28: aload_2
/*      */       //   29: monitorexit
/*      */       //   30: aload_3
/*      */       //   31: athrow
/*      */       // Line number table:
/*      */       //   Java source line #781	-> byte code offset #0
/*      */       //   Java source line #782	-> byte code offset #7
/*      */       //   Java source line #783	-> byte code offset #27
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	32	0	this	SynchronizedListMultimap<K, V>
/*      */       //   0	32	1	key	K
/*      */       //   5	24	2	Ljava/lang/Object;	Object
/*      */       //   27	4	3	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	26	27	finally
/*      */       //   27	30	27	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public List<V> removeAll(Object key)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedListMultimap:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_2
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedListMultimap:delegate	()Lcom/google/common/collect/ListMultimap;
/*      */       //   11: aload_1
/*      */       //   12: invokeinterface 8 2 0
/*      */       //   17: aload_2
/*      */       //   18: monitorexit
/*      */       //   19: areturn
/*      */       //   20: astore_3
/*      */       //   21: aload_2
/*      */       //   22: monitorexit
/*      */       //   23: aload_3
/*      */       //   24: athrow
/*      */       // Line number table:
/*      */       //   Java source line #788	-> byte code offset #0
/*      */       //   Java source line #789	-> byte code offset #7
/*      */       //   Java source line #790	-> byte code offset #20
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	25	0	this	SynchronizedListMultimap<K, V>
/*      */       //   0	25	1	key	Object
/*      */       //   5	17	2	Ljava/lang/Object;	Object
/*      */       //   20	4	3	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	19	20	finally
/*      */       //   20	23	20	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public List<V> replaceValues(K key, Iterable<? extends V> values)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedListMultimap:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_3
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedListMultimap:delegate	()Lcom/google/common/collect/ListMultimap;
/*      */       //   11: aload_1
/*      */       //   12: aload_2
/*      */       //   13: invokeinterface 9 3 0
/*      */       //   18: aload_3
/*      */       //   19: monitorexit
/*      */       //   20: areturn
/*      */       //   21: astore 4
/*      */       //   23: aload_3
/*      */       //   24: monitorexit
/*      */       //   25: aload 4
/*      */       //   27: athrow
/*      */       // Line number table:
/*      */       //   Java source line #795	-> byte code offset #0
/*      */       //   Java source line #796	-> byte code offset #7
/*      */       //   Java source line #797	-> byte code offset #21
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	28	0	this	SynchronizedListMultimap<K, V>
/*      */       //   0	28	1	key	K
/*      */       //   0	28	2	values	Iterable<? extends V>
/*      */       //   5	19	3	Ljava/lang/Object;	Object
/*      */       //   21	5	4	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	20	21	finally
/*      */       //   21	25	21	finally
/*      */     }
/*      */   }
/*      */   
/*      */   static <K, V> SetMultimap<K, V> setMultimap(SetMultimap<K, V> multimap, @Nullable Object mutex)
/*      */   {
/*  804 */     if (((multimap instanceof SynchronizedSetMultimap)) || ((multimap instanceof ImmutableSetMultimap))) {
/*  805 */       return multimap;
/*      */     }
/*  807 */     return new SynchronizedSetMultimap(multimap, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedSetMultimap<K, V> extends Synchronized.SynchronizedMultimap<K, V> implements SetMultimap<K, V> {
/*      */     transient Set<Map.Entry<K, V>> entrySet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedSetMultimap(SetMultimap<K, V> delegate, @Nullable Object mutex) {
/*  815 */       super(mutex);
/*      */     }
/*      */     
/*      */     SetMultimap<K, V> delegate()
/*      */     {
/*  820 */       return (SetMultimap)super.delegate();
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public Set<V> get(K key)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedSetMultimap:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_2
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedSetMultimap:delegate	()Lcom/google/common/collect/SetMultimap;
/*      */       //   11: aload_1
/*      */       //   12: invokeinterface 6 2 0
/*      */       //   17: aload_0
/*      */       //   18: getfield 4	com/google/common/collect/Synchronized$SynchronizedSetMultimap:mutex	Ljava/lang/Object;
/*      */       //   21: invokestatic 7	com/google/common/collect/Synchronized:set	(Ljava/util/Set;Ljava/lang/Object;)Ljava/util/Set;
/*      */       //   24: aload_2
/*      */       //   25: monitorexit
/*      */       //   26: areturn
/*      */       //   27: astore_3
/*      */       //   28: aload_2
/*      */       //   29: monitorexit
/*      */       //   30: aload_3
/*      */       //   31: athrow
/*      */       // Line number table:
/*      */       //   Java source line #825	-> byte code offset #0
/*      */       //   Java source line #826	-> byte code offset #7
/*      */       //   Java source line #827	-> byte code offset #27
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	32	0	this	SynchronizedSetMultimap<K, V>
/*      */       //   0	32	1	key	K
/*      */       //   5	24	2	Ljava/lang/Object;	Object
/*      */       //   27	4	3	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	26	27	finally
/*      */       //   27	30	27	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public Set<V> removeAll(Object key)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedSetMultimap:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_2
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedSetMultimap:delegate	()Lcom/google/common/collect/SetMultimap;
/*      */       //   11: aload_1
/*      */       //   12: invokeinterface 8 2 0
/*      */       //   17: aload_2
/*      */       //   18: monitorexit
/*      */       //   19: areturn
/*      */       //   20: astore_3
/*      */       //   21: aload_2
/*      */       //   22: monitorexit
/*      */       //   23: aload_3
/*      */       //   24: athrow
/*      */       // Line number table:
/*      */       //   Java source line #832	-> byte code offset #0
/*      */       //   Java source line #833	-> byte code offset #7
/*      */       //   Java source line #834	-> byte code offset #20
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	25	0	this	SynchronizedSetMultimap<K, V>
/*      */       //   0	25	1	key	Object
/*      */       //   5	17	2	Ljava/lang/Object;	Object
/*      */       //   20	4	3	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	19	20	finally
/*      */       //   20	23	20	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public Set<V> replaceValues(K key, Iterable<? extends V> values)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedSetMultimap:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_3
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedSetMultimap:delegate	()Lcom/google/common/collect/SetMultimap;
/*      */       //   11: aload_1
/*      */       //   12: aload_2
/*      */       //   13: invokeinterface 9 3 0
/*      */       //   18: aload_3
/*      */       //   19: monitorexit
/*      */       //   20: areturn
/*      */       //   21: astore 4
/*      */       //   23: aload_3
/*      */       //   24: monitorexit
/*      */       //   25: aload 4
/*      */       //   27: athrow
/*      */       // Line number table:
/*      */       //   Java source line #839	-> byte code offset #0
/*      */       //   Java source line #840	-> byte code offset #7
/*      */       //   Java source line #841	-> byte code offset #21
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	28	0	this	SynchronizedSetMultimap<K, V>
/*      */       //   0	28	1	key	K
/*      */       //   0	28	2	values	Iterable<? extends V>
/*      */       //   5	19	3	Ljava/lang/Object;	Object
/*      */       //   21	5	4	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	20	21	finally
/*      */       //   21	25	21	finally
/*      */     }
/*      */     
/*      */     public Set<Map.Entry<K, V>> entries()
/*      */     {
/*  846 */       synchronized (this.mutex) {
/*  847 */         if (this.entrySet == null) {
/*  848 */           this.entrySet = Synchronized.set(delegate().entries(), this.mutex);
/*      */         }
/*  850 */         return this.entrySet;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   static <K, V> SortedSetMultimap<K, V> sortedSetMultimap(SortedSetMultimap<K, V> multimap, @Nullable Object mutex)
/*      */   {
/*  859 */     if ((multimap instanceof SynchronizedSortedSetMultimap)) {
/*  860 */       return multimap;
/*      */     }
/*  862 */     return new SynchronizedSortedSetMultimap(multimap, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedSortedSetMultimap<K, V> extends Synchronized.SynchronizedSetMultimap<K, V> implements SortedSetMultimap<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*  868 */     SynchronizedSortedSetMultimap(SortedSetMultimap<K, V> delegate, @Nullable Object mutex) { super(mutex); }
/*      */     
/*      */ 
/*      */     SortedSetMultimap<K, V> delegate()
/*      */     {
/*  873 */       return (SortedSetMultimap)super.delegate();
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public SortedSet<V> get(K key)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedSortedSetMultimap:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_2
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedSortedSetMultimap:delegate	()Lcom/google/common/collect/SortedSetMultimap;
/*      */       //   11: aload_1
/*      */       //   12: invokeinterface 6 2 0
/*      */       //   17: aload_0
/*      */       //   18: getfield 4	com/google/common/collect/Synchronized$SynchronizedSortedSetMultimap:mutex	Ljava/lang/Object;
/*      */       //   21: invokestatic 7	com/google/common/collect/Synchronized:access$100	(Ljava/util/SortedSet;Ljava/lang/Object;)Ljava/util/SortedSet;
/*      */       //   24: aload_2
/*      */       //   25: monitorexit
/*      */       //   26: areturn
/*      */       //   27: astore_3
/*      */       //   28: aload_2
/*      */       //   29: monitorexit
/*      */       //   30: aload_3
/*      */       //   31: athrow
/*      */       // Line number table:
/*      */       //   Java source line #878	-> byte code offset #0
/*      */       //   Java source line #879	-> byte code offset #7
/*      */       //   Java source line #880	-> byte code offset #27
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	32	0	this	SynchronizedSortedSetMultimap<K, V>
/*      */       //   0	32	1	key	K
/*      */       //   5	24	2	Ljava/lang/Object;	Object
/*      */       //   27	4	3	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	26	27	finally
/*      */       //   27	30	27	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public SortedSet<V> removeAll(Object key)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedSortedSetMultimap:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_2
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedSortedSetMultimap:delegate	()Lcom/google/common/collect/SortedSetMultimap;
/*      */       //   11: aload_1
/*      */       //   12: invokeinterface 8 2 0
/*      */       //   17: aload_2
/*      */       //   18: monitorexit
/*      */       //   19: areturn
/*      */       //   20: astore_3
/*      */       //   21: aload_2
/*      */       //   22: monitorexit
/*      */       //   23: aload_3
/*      */       //   24: athrow
/*      */       // Line number table:
/*      */       //   Java source line #885	-> byte code offset #0
/*      */       //   Java source line #886	-> byte code offset #7
/*      */       //   Java source line #887	-> byte code offset #20
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	25	0	this	SynchronizedSortedSetMultimap<K, V>
/*      */       //   0	25	1	key	Object
/*      */       //   5	17	2	Ljava/lang/Object;	Object
/*      */       //   20	4	3	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	19	20	finally
/*      */       //   20	23	20	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public SortedSet<V> replaceValues(K key, Iterable<? extends V> values)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedSortedSetMultimap:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_3
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedSortedSetMultimap:delegate	()Lcom/google/common/collect/SortedSetMultimap;
/*      */       //   11: aload_1
/*      */       //   12: aload_2
/*      */       //   13: invokeinterface 9 3 0
/*      */       //   18: aload_3
/*      */       //   19: monitorexit
/*      */       //   20: areturn
/*      */       //   21: astore 4
/*      */       //   23: aload_3
/*      */       //   24: monitorexit
/*      */       //   25: aload 4
/*      */       //   27: athrow
/*      */       // Line number table:
/*      */       //   Java source line #892	-> byte code offset #0
/*      */       //   Java source line #893	-> byte code offset #7
/*      */       //   Java source line #894	-> byte code offset #21
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	28	0	this	SynchronizedSortedSetMultimap<K, V>
/*      */       //   0	28	1	key	K
/*      */       //   0	28	2	values	Iterable<? extends V>
/*      */       //   5	19	3	Ljava/lang/Object;	Object
/*      */       //   21	5	4	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	20	21	finally
/*      */       //   21	25	21	finally
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public Comparator<? super V> valueComparator()
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedSortedSetMultimap:mutex	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_1
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedSortedSetMultimap:delegate	()Lcom/google/common/collect/SortedSetMultimap;
/*      */       //   11: invokeinterface 10 1 0
/*      */       //   16: aload_1
/*      */       //   17: monitorexit
/*      */       //   18: areturn
/*      */       //   19: astore_2
/*      */       //   20: aload_1
/*      */       //   21: monitorexit
/*      */       //   22: aload_2
/*      */       //   23: athrow
/*      */       // Line number table:
/*      */       //   Java source line #899	-> byte code offset #0
/*      */       //   Java source line #900	-> byte code offset #7
/*      */       //   Java source line #901	-> byte code offset #19
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	24	0	this	SynchronizedSortedSetMultimap<K, V>
/*      */       //   5	16	1	Ljava/lang/Object;	Object
/*      */       //   19	4	2	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	18	19	finally
/*      */       //   19	22	19	finally
/*      */     }
/*      */   }
/*      */   
/*      */   private static <E> Collection<E> typePreservingCollection(Collection<E> collection, @Nullable Object mutex)
/*      */   {
/*  909 */     if ((collection instanceof SortedSet)) {
/*  910 */       return sortedSet((SortedSet)collection, mutex);
/*      */     }
/*  912 */     if ((collection instanceof Set)) {
/*  913 */       return set((Set)collection, mutex);
/*      */     }
/*  915 */     if ((collection instanceof List)) {
/*  916 */       return list((List)collection, mutex);
/*      */     }
/*  918 */     return collection(collection, mutex);
/*      */   }
/*      */   
/*      */   private static <E> Set<E> typePreservingSet(Set<E> set, @Nullable Object mutex) {
/*  922 */     if ((set instanceof SortedSet)) {
/*  923 */       return sortedSet((SortedSet)set, mutex);
/*      */     }
/*  925 */     return set(set, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedAsMapEntries<K, V> extends Synchronized.SynchronizedSet<Map.Entry<K, Collection<V>>> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedAsMapEntries(Set<Map.Entry<K, Collection<V>>> delegate, @Nullable Object mutex) {
/*  932 */       super(mutex);
/*      */     }
/*      */     
/*      */ 
/*      */     public Iterator<Map.Entry<K, Collection<V>>> iterator()
/*      */     {
/*  938 */       new TransformedIterator(
/*  939 */         super.iterator())
/*      */         {
/*      */           Map.Entry<K, Collection<V>> transform(final Map.Entry<K, Collection<V>> entry)
/*      */           {
/*  942 */             new ForwardingMapEntry()
/*      */             {
/*      */               protected Map.Entry<K, Collection<V>> delegate() {
/*  945 */                 return entry;
/*      */               }
/*      */               
/*      */               public Collection<V> getValue()
/*      */               {
/*  950 */                 return Synchronized.typePreservingCollection((Collection)entry.getValue(), Synchronized.SynchronizedAsMapEntries.this.mutex);
/*      */               }
/*      */             };
/*      */           }
/*      */         };
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public Object[] toArray()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 5	com/google/common/collect/Synchronized$SynchronizedAsMapEntries:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 6	com/google/common/collect/Synchronized$SynchronizedAsMapEntries:delegate	()Ljava/util/Set;
/*      */         //   11: invokestatic 7	com/google/common/collect/ObjectArrays:toArrayImpl	(Ljava/util/Collection;)[Ljava/lang/Object;
/*      */         //   14: aload_1
/*      */         //   15: monitorexit
/*      */         //   16: areturn
/*      */         //   17: astore_2
/*      */         //   18: aload_1
/*      */         //   19: monitorexit
/*      */         //   20: aload_2
/*      */         //   21: athrow
/*      */         // Line number table:
/*      */         //   Java source line #961	-> byte code offset #0
/*      */         //   Java source line #962	-> byte code offset #7
/*      */         //   Java source line #963	-> byte code offset #17
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	22	0	this	SynchronizedAsMapEntries<K, V>
/*      */         //   5	14	1	Ljava/lang/Object;	Object
/*      */         //   17	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	16	17	finally
/*      */         //   17	20	17	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public <T> T[] toArray(T[] array)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 5	com/google/common/collect/Synchronized$SynchronizedAsMapEntries:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 6	com/google/common/collect/Synchronized$SynchronizedAsMapEntries:delegate	()Ljava/util/Set;
/*      */         //   11: aload_1
/*      */         //   12: invokestatic 8	com/google/common/collect/ObjectArrays:toArrayImpl	(Ljava/util/Collection;[Ljava/lang/Object;)[Ljava/lang/Object;
/*      */         //   15: aload_2
/*      */         //   16: monitorexit
/*      */         //   17: areturn
/*      */         //   18: astore_3
/*      */         //   19: aload_2
/*      */         //   20: monitorexit
/*      */         //   21: aload_3
/*      */         //   22: athrow
/*      */         // Line number table:
/*      */         //   Java source line #968	-> byte code offset #0
/*      */         //   Java source line #969	-> byte code offset #7
/*      */         //   Java source line #970	-> byte code offset #18
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	23	0	this	SynchronizedAsMapEntries<K, V>
/*      */         //   0	23	1	array	T[]
/*      */         //   5	15	2	Ljava/lang/Object;	Object
/*      */         //   18	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	17	18	finally
/*      */         //   18	21	18	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public boolean contains(Object o)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 5	com/google/common/collect/Synchronized$SynchronizedAsMapEntries:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 6	com/google/common/collect/Synchronized$SynchronizedAsMapEntries:delegate	()Ljava/util/Set;
/*      */         //   11: aload_1
/*      */         //   12: invokestatic 9	com/google/common/collect/Maps:containsEntryImpl	(Ljava/util/Collection;Ljava/lang/Object;)Z
/*      */         //   15: aload_2
/*      */         //   16: monitorexit
/*      */         //   17: ireturn
/*      */         //   18: astore_3
/*      */         //   19: aload_2
/*      */         //   20: monitorexit
/*      */         //   21: aload_3
/*      */         //   22: athrow
/*      */         // Line number table:
/*      */         //   Java source line #975	-> byte code offset #0
/*      */         //   Java source line #976	-> byte code offset #7
/*      */         //   Java source line #977	-> byte code offset #18
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	23	0	this	SynchronizedAsMapEntries<K, V>
/*      */         //   0	23	1	o	Object
/*      */         //   5	15	2	Ljava/lang/Object;	Object
/*      */         //   18	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	17	18	finally
/*      */         //   18	21	18	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public boolean containsAll(Collection<?> c)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 5	com/google/common/collect/Synchronized$SynchronizedAsMapEntries:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 6	com/google/common/collect/Synchronized$SynchronizedAsMapEntries:delegate	()Ljava/util/Set;
/*      */         //   11: aload_1
/*      */         //   12: invokestatic 10	com/google/common/collect/Collections2:containsAllImpl	(Ljava/util/Collection;Ljava/util/Collection;)Z
/*      */         //   15: aload_2
/*      */         //   16: monitorexit
/*      */         //   17: ireturn
/*      */         //   18: astore_3
/*      */         //   19: aload_2
/*      */         //   20: monitorexit
/*      */         //   21: aload_3
/*      */         //   22: athrow
/*      */         // Line number table:
/*      */         //   Java source line #982	-> byte code offset #0
/*      */         //   Java source line #983	-> byte code offset #7
/*      */         //   Java source line #984	-> byte code offset #18
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	23	0	this	SynchronizedAsMapEntries<K, V>
/*      */         //   0	23	1	c	Collection<?>
/*      */         //   5	15	2	Ljava/lang/Object;	Object
/*      */         //   18	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	17	18	finally
/*      */         //   18	21	18	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public boolean equals(Object o)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_1
/*      */         //   1: aload_0
/*      */         //   2: if_acmpne +5 -> 7
/*      */         //   5: iconst_1
/*      */         //   6: ireturn
/*      */         //   7: aload_0
/*      */         //   8: getfield 5	com/google/common/collect/Synchronized$SynchronizedAsMapEntries:mutex	Ljava/lang/Object;
/*      */         //   11: dup
/*      */         //   12: astore_2
/*      */         //   13: monitorenter
/*      */         //   14: aload_0
/*      */         //   15: invokevirtual 6	com/google/common/collect/Synchronized$SynchronizedAsMapEntries:delegate	()Ljava/util/Set;
/*      */         //   18: aload_1
/*      */         //   19: invokestatic 11	com/google/common/collect/Sets:equalsImpl	(Ljava/util/Set;Ljava/lang/Object;)Z
/*      */         //   22: aload_2
/*      */         //   23: monitorexit
/*      */         //   24: ireturn
/*      */         //   25: astore_3
/*      */         //   26: aload_2
/*      */         //   27: monitorexit
/*      */         //   28: aload_3
/*      */         //   29: athrow
/*      */         // Line number table:
/*      */         //   Java source line #989	-> byte code offset #0
/*      */         //   Java source line #990	-> byte code offset #5
/*      */         //   Java source line #992	-> byte code offset #7
/*      */         //   Java source line #993	-> byte code offset #14
/*      */         //   Java source line #994	-> byte code offset #25
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	30	0	this	SynchronizedAsMapEntries<K, V>
/*      */         //   0	30	1	o	Object
/*      */         //   12	15	2	Ljava/lang/Object;	Object
/*      */         //   25	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   14	24	25	finally
/*      */         //   25	28	25	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public boolean remove(Object o)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 5	com/google/common/collect/Synchronized$SynchronizedAsMapEntries:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 6	com/google/common/collect/Synchronized$SynchronizedAsMapEntries:delegate	()Ljava/util/Set;
/*      */         //   11: aload_1
/*      */         //   12: invokestatic 12	com/google/common/collect/Maps:removeEntryImpl	(Ljava/util/Collection;Ljava/lang/Object;)Z
/*      */         //   15: aload_2
/*      */         //   16: monitorexit
/*      */         //   17: ireturn
/*      */         //   18: astore_3
/*      */         //   19: aload_2
/*      */         //   20: monitorexit
/*      */         //   21: aload_3
/*      */         //   22: athrow
/*      */         // Line number table:
/*      */         //   Java source line #999	-> byte code offset #0
/*      */         //   Java source line #1000	-> byte code offset #7
/*      */         //   Java source line #1001	-> byte code offset #18
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	23	0	this	SynchronizedAsMapEntries<K, V>
/*      */         //   0	23	1	o	Object
/*      */         //   5	15	2	Ljava/lang/Object;	Object
/*      */         //   18	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	17	18	finally
/*      */         //   18	21	18	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public boolean removeAll(Collection<?> c)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 5	com/google/common/collect/Synchronized$SynchronizedAsMapEntries:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 6	com/google/common/collect/Synchronized$SynchronizedAsMapEntries:delegate	()Ljava/util/Set;
/*      */         //   11: invokeinterface 13 1 0
/*      */         //   16: aload_1
/*      */         //   17: invokestatic 14	com/google/common/collect/Iterators:removeAll	(Ljava/util/Iterator;Ljava/util/Collection;)Z
/*      */         //   20: aload_2
/*      */         //   21: monitorexit
/*      */         //   22: ireturn
/*      */         //   23: astore_3
/*      */         //   24: aload_2
/*      */         //   25: monitorexit
/*      */         //   26: aload_3
/*      */         //   27: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1006	-> byte code offset #0
/*      */         //   Java source line #1007	-> byte code offset #7
/*      */         //   Java source line #1008	-> byte code offset #23
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	28	0	this	SynchronizedAsMapEntries<K, V>
/*      */         //   0	28	1	c	Collection<?>
/*      */         //   5	20	2	Ljava/lang/Object;	Object
/*      */         //   23	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	22	23	finally
/*      */         //   23	26	23	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public boolean retainAll(Collection<?> c)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 5	com/google/common/collect/Synchronized$SynchronizedAsMapEntries:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 6	com/google/common/collect/Synchronized$SynchronizedAsMapEntries:delegate	()Ljava/util/Set;
/*      */         //   11: invokeinterface 13 1 0
/*      */         //   16: aload_1
/*      */         //   17: invokestatic 15	com/google/common/collect/Iterators:retainAll	(Ljava/util/Iterator;Ljava/util/Collection;)Z
/*      */         //   20: aload_2
/*      */         //   21: monitorexit
/*      */         //   22: ireturn
/*      */         //   23: astore_3
/*      */         //   24: aload_2
/*      */         //   25: monitorexit
/*      */         //   26: aload_3
/*      */         //   27: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1013	-> byte code offset #0
/*      */         //   Java source line #1014	-> byte code offset #7
/*      */         //   Java source line #1015	-> byte code offset #23
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	28	0	this	SynchronizedAsMapEntries<K, V>
/*      */         //   0	28	1	c	Collection<?>
/*      */         //   5	20	2	Ljava/lang/Object;	Object
/*      */         //   23	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	22	23	finally
/*      */         //   23	26	23	finally
/*      */       }
/*      */     }
/*      */     
/*      */     @VisibleForTesting
/*      */     static <K, V> Map<K, V> map(Map<K, V> map, @Nullable Object mutex)
/*      */     {
/* 1023 */       return new SynchronizedMap(map, mutex);
/*      */     }
/*      */     
/*      */     private static class SynchronizedMap<K, V> extends Synchronized.SynchronizedObject implements Map<K, V> {
/*      */       transient Set<K> keySet;
/*      */       transient Collection<V> values;
/*      */       transient Set<Map.Entry<K, V>> entrySet;
/*      */       private static final long serialVersionUID = 0L;
/*      */       
/* 1032 */       SynchronizedMap(Map<K, V> delegate, @Nullable Object mutex) { super(mutex); }
/*      */       
/*      */ 
/*      */ 
/*      */       Map<K, V> delegate()
/*      */       {
/* 1038 */         return (Map)super.delegate();
/*      */       }
/*      */       
/*      */       public void clear()
/*      */       {
/* 1043 */         synchronized (this.mutex) {
/* 1044 */           delegate().clear();
/*      */         }
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public boolean containsKey(Object key)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMap:delegate	()Ljava/util/Map;
/*      */         //   11: aload_1
/*      */         //   12: invokeinterface 7 2 0
/*      */         //   17: aload_2
/*      */         //   18: monitorexit
/*      */         //   19: ireturn
/*      */         //   20: astore_3
/*      */         //   21: aload_2
/*      */         //   22: monitorexit
/*      */         //   23: aload_3
/*      */         //   24: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1050	-> byte code offset #0
/*      */         //   Java source line #1051	-> byte code offset #7
/*      */         //   Java source line #1052	-> byte code offset #20
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	25	0	this	SynchronizedMap<K, V>
/*      */         //   0	25	1	key	Object
/*      */         //   5	17	2	Ljava/lang/Object;	Object
/*      */         //   20	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	19	20	finally
/*      */         //   20	23	20	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public boolean containsValue(Object value)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMap:delegate	()Ljava/util/Map;
/*      */         //   11: aload_1
/*      */         //   12: invokeinterface 8 2 0
/*      */         //   17: aload_2
/*      */         //   18: monitorexit
/*      */         //   19: ireturn
/*      */         //   20: astore_3
/*      */         //   21: aload_2
/*      */         //   22: monitorexit
/*      */         //   23: aload_3
/*      */         //   24: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1057	-> byte code offset #0
/*      */         //   Java source line #1058	-> byte code offset #7
/*      */         //   Java source line #1059	-> byte code offset #20
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	25	0	this	SynchronizedMap<K, V>
/*      */         //   0	25	1	value	Object
/*      */         //   5	17	2	Ljava/lang/Object;	Object
/*      */         //   20	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	19	20	finally
/*      */         //   20	23	20	finally
/*      */       }
/*      */       
/*      */       public Set<Map.Entry<K, V>> entrySet()
/*      */       {
/* 1064 */         synchronized (this.mutex) {
/* 1065 */           if (this.entrySet == null) {
/* 1066 */             this.entrySet = Synchronized.set(delegate().entrySet(), this.mutex);
/*      */           }
/* 1068 */           return this.entrySet;
/*      */         }
/*      */       }
/*      */       
/*      */       public void forEach(BiConsumer<? super K, ? super V> action)
/*      */       {
/* 1074 */         synchronized (this.mutex) {
/* 1075 */           delegate().forEach(action);
/*      */         }
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public V get(Object key)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMap:delegate	()Ljava/util/Map;
/*      */         //   11: aload_1
/*      */         //   12: invokeinterface 13 2 0
/*      */         //   17: aload_2
/*      */         //   18: monitorexit
/*      */         //   19: areturn
/*      */         //   20: astore_3
/*      */         //   21: aload_2
/*      */         //   22: monitorexit
/*      */         //   23: aload_3
/*      */         //   24: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1081	-> byte code offset #0
/*      */         //   Java source line #1082	-> byte code offset #7
/*      */         //   Java source line #1083	-> byte code offset #20
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	25	0	this	SynchronizedMap<K, V>
/*      */         //   0	25	1	key	Object
/*      */         //   5	17	2	Ljava/lang/Object;	Object
/*      */         //   20	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	19	20	finally
/*      */         //   20	23	20	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public V getOrDefault(Object key, V defaultValue)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_3
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMap:delegate	()Ljava/util/Map;
/*      */         //   11: aload_1
/*      */         //   12: aload_2
/*      */         //   13: invokeinterface 14 3 0
/*      */         //   18: aload_3
/*      */         //   19: monitorexit
/*      */         //   20: areturn
/*      */         //   21: astore 4
/*      */         //   23: aload_3
/*      */         //   24: monitorexit
/*      */         //   25: aload 4
/*      */         //   27: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1088	-> byte code offset #0
/*      */         //   Java source line #1089	-> byte code offset #7
/*      */         //   Java source line #1090	-> byte code offset #21
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	28	0	this	SynchronizedMap<K, V>
/*      */         //   0	28	1	key	Object
/*      */         //   0	28	2	defaultValue	V
/*      */         //   5	19	3	Ljava/lang/Object;	Object
/*      */         //   21	5	4	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	20	21	finally
/*      */         //   21	25	21	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public boolean isEmpty()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMap:delegate	()Ljava/util/Map;
/*      */         //   11: invokeinterface 15 1 0
/*      */         //   16: aload_1
/*      */         //   17: monitorexit
/*      */         //   18: ireturn
/*      */         //   19: astore_2
/*      */         //   20: aload_1
/*      */         //   21: monitorexit
/*      */         //   22: aload_2
/*      */         //   23: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1095	-> byte code offset #0
/*      */         //   Java source line #1096	-> byte code offset #7
/*      */         //   Java source line #1097	-> byte code offset #19
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	24	0	this	SynchronizedMap<K, V>
/*      */         //   5	16	1	Ljava/lang/Object;	Object
/*      */         //   19	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	18	19	finally
/*      */         //   19	22	19	finally
/*      */       }
/*      */       
/*      */       public Set<K> keySet()
/*      */       {
/* 1102 */         synchronized (this.mutex) {
/* 1103 */           if (this.keySet == null) {
/* 1104 */             this.keySet = Synchronized.set(delegate().keySet(), this.mutex);
/*      */           }
/* 1106 */           return this.keySet;
/*      */         }
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public V put(K key, V value)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_3
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMap:delegate	()Ljava/util/Map;
/*      */         //   11: aload_1
/*      */         //   12: aload_2
/*      */         //   13: invokeinterface 18 3 0
/*      */         //   18: aload_3
/*      */         //   19: monitorexit
/*      */         //   20: areturn
/*      */         //   21: astore 4
/*      */         //   23: aload_3
/*      */         //   24: monitorexit
/*      */         //   25: aload 4
/*      */         //   27: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1112	-> byte code offset #0
/*      */         //   Java source line #1113	-> byte code offset #7
/*      */         //   Java source line #1114	-> byte code offset #21
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	28	0	this	SynchronizedMap<K, V>
/*      */         //   0	28	1	key	K
/*      */         //   0	28	2	value	V
/*      */         //   5	19	3	Ljava/lang/Object;	Object
/*      */         //   21	5	4	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	20	21	finally
/*      */         //   21	25	21	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public V putIfAbsent(K key, V value)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_3
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMap:delegate	()Ljava/util/Map;
/*      */         //   11: aload_1
/*      */         //   12: aload_2
/*      */         //   13: invokeinterface 19 3 0
/*      */         //   18: aload_3
/*      */         //   19: monitorexit
/*      */         //   20: areturn
/*      */         //   21: astore 4
/*      */         //   23: aload_3
/*      */         //   24: monitorexit
/*      */         //   25: aload 4
/*      */         //   27: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1119	-> byte code offset #0
/*      */         //   Java source line #1120	-> byte code offset #7
/*      */         //   Java source line #1121	-> byte code offset #21
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	28	0	this	SynchronizedMap<K, V>
/*      */         //   0	28	1	key	K
/*      */         //   0	28	2	value	V
/*      */         //   5	19	3	Ljava/lang/Object;	Object
/*      */         //   21	5	4	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	20	21	finally
/*      */         //   21	25	21	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public boolean replace(K key, V oldValue, V newValue)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore 4
/*      */         //   7: monitorenter
/*      */         //   8: aload_0
/*      */         //   9: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMap:delegate	()Ljava/util/Map;
/*      */         //   12: aload_1
/*      */         //   13: aload_2
/*      */         //   14: aload_3
/*      */         //   15: invokeinterface 20 4 0
/*      */         //   20: aload 4
/*      */         //   22: monitorexit
/*      */         //   23: ireturn
/*      */         //   24: astore 5
/*      */         //   26: aload 4
/*      */         //   28: monitorexit
/*      */         //   29: aload 5
/*      */         //   31: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1126	-> byte code offset #0
/*      */         //   Java source line #1127	-> byte code offset #8
/*      */         //   Java source line #1128	-> byte code offset #24
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	32	0	this	SynchronizedMap<K, V>
/*      */         //   0	32	1	key	K
/*      */         //   0	32	2	oldValue	V
/*      */         //   0	32	3	newValue	V
/*      */         //   5	22	4	Ljava/lang/Object;	Object
/*      */         //   24	6	5	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   8	23	24	finally
/*      */         //   24	29	24	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public V replace(K key, V value)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_3
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMap:delegate	()Ljava/util/Map;
/*      */         //   11: aload_1
/*      */         //   12: aload_2
/*      */         //   13: invokeinterface 21 3 0
/*      */         //   18: aload_3
/*      */         //   19: monitorexit
/*      */         //   20: areturn
/*      */         //   21: astore 4
/*      */         //   23: aload_3
/*      */         //   24: monitorexit
/*      */         //   25: aload 4
/*      */         //   27: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1133	-> byte code offset #0
/*      */         //   Java source line #1134	-> byte code offset #7
/*      */         //   Java source line #1135	-> byte code offset #21
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	28	0	this	SynchronizedMap<K, V>
/*      */         //   0	28	1	key	K
/*      */         //   0	28	2	value	V
/*      */         //   5	19	3	Ljava/lang/Object;	Object
/*      */         //   21	5	4	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	20	21	finally
/*      */         //   21	25	21	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public V computeIfAbsent(K key, java.util.function.Function<? super K, ? extends V> mappingFunction)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_3
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMap:delegate	()Ljava/util/Map;
/*      */         //   11: aload_1
/*      */         //   12: aload_2
/*      */         //   13: invokeinterface 22 3 0
/*      */         //   18: aload_3
/*      */         //   19: monitorexit
/*      */         //   20: areturn
/*      */         //   21: astore 4
/*      */         //   23: aload_3
/*      */         //   24: monitorexit
/*      */         //   25: aload 4
/*      */         //   27: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1140	-> byte code offset #0
/*      */         //   Java source line #1141	-> byte code offset #7
/*      */         //   Java source line #1142	-> byte code offset #21
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	28	0	this	SynchronizedMap<K, V>
/*      */         //   0	28	1	key	K
/*      */         //   0	28	2	mappingFunction	java.util.function.Function<? super K, ? extends V>
/*      */         //   5	19	3	Ljava/lang/Object;	Object
/*      */         //   21	5	4	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	20	21	finally
/*      */         //   21	25	21	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_3
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMap:delegate	()Ljava/util/Map;
/*      */         //   11: aload_1
/*      */         //   12: aload_2
/*      */         //   13: invokeinterface 23 3 0
/*      */         //   18: aload_3
/*      */         //   19: monitorexit
/*      */         //   20: areturn
/*      */         //   21: astore 4
/*      */         //   23: aload_3
/*      */         //   24: monitorexit
/*      */         //   25: aload 4
/*      */         //   27: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1148	-> byte code offset #0
/*      */         //   Java source line #1149	-> byte code offset #7
/*      */         //   Java source line #1150	-> byte code offset #21
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	28	0	this	SynchronizedMap<K, V>
/*      */         //   0	28	1	key	K
/*      */         //   0	28	2	remappingFunction	BiFunction<? super K, ? super V, ? extends V>
/*      */         //   5	19	3	Ljava/lang/Object;	Object
/*      */         //   21	5	4	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	20	21	finally
/*      */         //   21	25	21	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_3
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMap:delegate	()Ljava/util/Map;
/*      */         //   11: aload_1
/*      */         //   12: aload_2
/*      */         //   13: invokeinterface 24 3 0
/*      */         //   18: aload_3
/*      */         //   19: monitorexit
/*      */         //   20: areturn
/*      */         //   21: astore 4
/*      */         //   23: aload_3
/*      */         //   24: monitorexit
/*      */         //   25: aload 4
/*      */         //   27: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1155	-> byte code offset #0
/*      */         //   Java source line #1156	-> byte code offset #7
/*      */         //   Java source line #1157	-> byte code offset #21
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	28	0	this	SynchronizedMap<K, V>
/*      */         //   0	28	1	key	K
/*      */         //   0	28	2	remappingFunction	BiFunction<? super K, ? super V, ? extends V>
/*      */         //   5	19	3	Ljava/lang/Object;	Object
/*      */         //   21	5	4	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	20	21	finally
/*      */         //   21	25	21	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore 4
/*      */         //   7: monitorenter
/*      */         //   8: aload_0
/*      */         //   9: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMap:delegate	()Ljava/util/Map;
/*      */         //   12: aload_1
/*      */         //   13: aload_2
/*      */         //   14: aload_3
/*      */         //   15: invokeinterface 25 4 0
/*      */         //   20: aload 4
/*      */         //   22: monitorexit
/*      */         //   23: areturn
/*      */         //   24: astore 5
/*      */         //   26: aload 4
/*      */         //   28: monitorexit
/*      */         //   29: aload 5
/*      */         //   31: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1163	-> byte code offset #0
/*      */         //   Java source line #1164	-> byte code offset #8
/*      */         //   Java source line #1165	-> byte code offset #24
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	32	0	this	SynchronizedMap<K, V>
/*      */         //   0	32	1	key	K
/*      */         //   0	32	2	value	V
/*      */         //   0	32	3	remappingFunction	BiFunction<? super V, ? super V, ? extends V>
/*      */         //   5	22	4	Ljava/lang/Object;	Object
/*      */         //   24	6	5	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   8	23	24	finally
/*      */         //   24	29	24	finally
/*      */       }
/*      */       
/*      */       public void putAll(Map<? extends K, ? extends V> map)
/*      */       {
/* 1170 */         synchronized (this.mutex) {
/* 1171 */           delegate().putAll(map);
/*      */         }
/*      */       }
/*      */       
/*      */       public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function)
/*      */       {
/* 1177 */         synchronized (this.mutex) {
/* 1178 */           delegate().replaceAll(function);
/*      */         }
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public V remove(Object key)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMap:delegate	()Ljava/util/Map;
/*      */         //   11: aload_1
/*      */         //   12: invokeinterface 28 2 0
/*      */         //   17: aload_2
/*      */         //   18: monitorexit
/*      */         //   19: areturn
/*      */         //   20: astore_3
/*      */         //   21: aload_2
/*      */         //   22: monitorexit
/*      */         //   23: aload_3
/*      */         //   24: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1184	-> byte code offset #0
/*      */         //   Java source line #1185	-> byte code offset #7
/*      */         //   Java source line #1186	-> byte code offset #20
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	25	0	this	SynchronizedMap<K, V>
/*      */         //   0	25	1	key	Object
/*      */         //   5	17	2	Ljava/lang/Object;	Object
/*      */         //   20	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	19	20	finally
/*      */         //   20	23	20	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public boolean remove(Object key, Object value)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_3
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMap:delegate	()Ljava/util/Map;
/*      */         //   11: aload_1
/*      */         //   12: aload_2
/*      */         //   13: invokeinterface 29 3 0
/*      */         //   18: aload_3
/*      */         //   19: monitorexit
/*      */         //   20: ireturn
/*      */         //   21: astore 4
/*      */         //   23: aload_3
/*      */         //   24: monitorexit
/*      */         //   25: aload 4
/*      */         //   27: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1191	-> byte code offset #0
/*      */         //   Java source line #1192	-> byte code offset #7
/*      */         //   Java source line #1193	-> byte code offset #21
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	28	0	this	SynchronizedMap<K, V>
/*      */         //   0	28	1	key	Object
/*      */         //   0	28	2	value	Object
/*      */         //   5	19	3	Ljava/lang/Object;	Object
/*      */         //   21	5	4	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	20	21	finally
/*      */         //   21	25	21	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public int size()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMap:delegate	()Ljava/util/Map;
/*      */         //   11: invokeinterface 30 1 0
/*      */         //   16: aload_1
/*      */         //   17: monitorexit
/*      */         //   18: ireturn
/*      */         //   19: astore_2
/*      */         //   20: aload_1
/*      */         //   21: monitorexit
/*      */         //   22: aload_2
/*      */         //   23: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1198	-> byte code offset #0
/*      */         //   Java source line #1199	-> byte code offset #7
/*      */         //   Java source line #1200	-> byte code offset #19
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	24	0	this	SynchronizedMap<K, V>
/*      */         //   5	16	1	Ljava/lang/Object;	Object
/*      */         //   19	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	18	19	finally
/*      */         //   19	22	19	finally
/*      */       }
/*      */       
/*      */       public Collection<V> values()
/*      */       {
/* 1205 */         synchronized (this.mutex) {
/* 1206 */           if (this.values == null) {
/* 1207 */             this.values = Synchronized.collection(delegate().values(), this.mutex);
/*      */           }
/* 1209 */           return this.values;
/*      */         }
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public boolean equals(Object o)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_1
/*      */         //   1: aload_0
/*      */         //   2: if_acmpne +5 -> 7
/*      */         //   5: iconst_1
/*      */         //   6: ireturn
/*      */         //   7: aload_0
/*      */         //   8: getfield 4	com/google/common/collect/Synchronized$SynchronizedMap:mutex	Ljava/lang/Object;
/*      */         //   11: dup
/*      */         //   12: astore_2
/*      */         //   13: monitorenter
/*      */         //   14: aload_0
/*      */         //   15: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMap:delegate	()Ljava/util/Map;
/*      */         //   18: aload_1
/*      */         //   19: invokeinterface 34 2 0
/*      */         //   24: aload_2
/*      */         //   25: monitorexit
/*      */         //   26: ireturn
/*      */         //   27: astore_3
/*      */         //   28: aload_2
/*      */         //   29: monitorexit
/*      */         //   30: aload_3
/*      */         //   31: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1215	-> byte code offset #0
/*      */         //   Java source line #1216	-> byte code offset #5
/*      */         //   Java source line #1218	-> byte code offset #7
/*      */         //   Java source line #1219	-> byte code offset #14
/*      */         //   Java source line #1220	-> byte code offset #27
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	32	0	this	SynchronizedMap<K, V>
/*      */         //   0	32	1	o	Object
/*      */         //   12	17	2	Ljava/lang/Object;	Object
/*      */         //   27	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   14	26	27	finally
/*      */         //   27	30	27	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public int hashCode()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedMap:delegate	()Ljava/util/Map;
/*      */         //   11: invokeinterface 35 1 0
/*      */         //   16: aload_1
/*      */         //   17: monitorexit
/*      */         //   18: ireturn
/*      */         //   19: astore_2
/*      */         //   20: aload_1
/*      */         //   21: monitorexit
/*      */         //   22: aload_2
/*      */         //   23: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1225	-> byte code offset #0
/*      */         //   Java source line #1226	-> byte code offset #7
/*      */         //   Java source line #1227	-> byte code offset #19
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	24	0	this	SynchronizedMap<K, V>
/*      */         //   5	16	1	Ljava/lang/Object;	Object
/*      */         //   19	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	18	19	finally
/*      */         //   19	22	19	finally
/*      */       }
/*      */     }
/*      */     
/*      */     static <K, V> SortedMap<K, V> sortedMap(SortedMap<K, V> sortedMap, @Nullable Object mutex)
/*      */     {
/* 1234 */       return new SynchronizedSortedMap(sortedMap, mutex);
/*      */     }
/*      */     
/*      */     static class SynchronizedSortedMap<K, V> extends Synchronized.SynchronizedMap<K, V> implements SortedMap<K, V> {
/*      */       private static final long serialVersionUID = 0L;
/*      */       
/*      */       SynchronizedSortedMap(SortedMap<K, V> delegate, @Nullable Object mutex) {
/* 1241 */         super(mutex);
/*      */       }
/*      */       
/*      */       SortedMap<K, V> delegate()
/*      */       {
/* 1246 */         return (SortedMap)super.delegate();
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public Comparator<? super K> comparator()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedSortedMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedSortedMap:delegate	()Ljava/util/SortedMap;
/*      */         //   11: invokeinterface 6 1 0
/*      */         //   16: aload_1
/*      */         //   17: monitorexit
/*      */         //   18: areturn
/*      */         //   19: astore_2
/*      */         //   20: aload_1
/*      */         //   21: monitorexit
/*      */         //   22: aload_2
/*      */         //   23: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1251	-> byte code offset #0
/*      */         //   Java source line #1252	-> byte code offset #7
/*      */         //   Java source line #1253	-> byte code offset #19
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	24	0	this	SynchronizedSortedMap<K, V>
/*      */         //   5	16	1	Ljava/lang/Object;	Object
/*      */         //   19	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	18	19	finally
/*      */         //   19	22	19	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public K firstKey()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedSortedMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedSortedMap:delegate	()Ljava/util/SortedMap;
/*      */         //   11: invokeinterface 7 1 0
/*      */         //   16: aload_1
/*      */         //   17: monitorexit
/*      */         //   18: areturn
/*      */         //   19: astore_2
/*      */         //   20: aload_1
/*      */         //   21: monitorexit
/*      */         //   22: aload_2
/*      */         //   23: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1258	-> byte code offset #0
/*      */         //   Java source line #1259	-> byte code offset #7
/*      */         //   Java source line #1260	-> byte code offset #19
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	24	0	this	SynchronizedSortedMap<K, V>
/*      */         //   5	16	1	Ljava/lang/Object;	Object
/*      */         //   19	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	18	19	finally
/*      */         //   19	22	19	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public SortedMap<K, V> headMap(K toKey)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedSortedMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedSortedMap:delegate	()Ljava/util/SortedMap;
/*      */         //   11: aload_1
/*      */         //   12: invokeinterface 8 2 0
/*      */         //   17: aload_0
/*      */         //   18: getfield 4	com/google/common/collect/Synchronized$SynchronizedSortedMap:mutex	Ljava/lang/Object;
/*      */         //   21: invokestatic 9	com/google/common/collect/Synchronized:sortedMap	(Ljava/util/SortedMap;Ljava/lang/Object;)Ljava/util/SortedMap;
/*      */         //   24: aload_2
/*      */         //   25: monitorexit
/*      */         //   26: areturn
/*      */         //   27: astore_3
/*      */         //   28: aload_2
/*      */         //   29: monitorexit
/*      */         //   30: aload_3
/*      */         //   31: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1265	-> byte code offset #0
/*      */         //   Java source line #1266	-> byte code offset #7
/*      */         //   Java source line #1267	-> byte code offset #27
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	32	0	this	SynchronizedSortedMap<K, V>
/*      */         //   0	32	1	toKey	K
/*      */         //   5	24	2	Ljava/lang/Object;	Object
/*      */         //   27	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	26	27	finally
/*      */         //   27	30	27	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public K lastKey()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedSortedMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedSortedMap:delegate	()Ljava/util/SortedMap;
/*      */         //   11: invokeinterface 10 1 0
/*      */         //   16: aload_1
/*      */         //   17: monitorexit
/*      */         //   18: areturn
/*      */         //   19: astore_2
/*      */         //   20: aload_1
/*      */         //   21: monitorexit
/*      */         //   22: aload_2
/*      */         //   23: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1272	-> byte code offset #0
/*      */         //   Java source line #1273	-> byte code offset #7
/*      */         //   Java source line #1274	-> byte code offset #19
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	24	0	this	SynchronizedSortedMap<K, V>
/*      */         //   5	16	1	Ljava/lang/Object;	Object
/*      */         //   19	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	18	19	finally
/*      */         //   19	22	19	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public SortedMap<K, V> subMap(K fromKey, K toKey)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedSortedMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_3
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedSortedMap:delegate	()Ljava/util/SortedMap;
/*      */         //   11: aload_1
/*      */         //   12: aload_2
/*      */         //   13: invokeinterface 11 3 0
/*      */         //   18: aload_0
/*      */         //   19: getfield 4	com/google/common/collect/Synchronized$SynchronizedSortedMap:mutex	Ljava/lang/Object;
/*      */         //   22: invokestatic 9	com/google/common/collect/Synchronized:sortedMap	(Ljava/util/SortedMap;Ljava/lang/Object;)Ljava/util/SortedMap;
/*      */         //   25: aload_3
/*      */         //   26: monitorexit
/*      */         //   27: areturn
/*      */         //   28: astore 4
/*      */         //   30: aload_3
/*      */         //   31: monitorexit
/*      */         //   32: aload 4
/*      */         //   34: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1279	-> byte code offset #0
/*      */         //   Java source line #1280	-> byte code offset #7
/*      */         //   Java source line #1281	-> byte code offset #28
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	35	0	this	SynchronizedSortedMap<K, V>
/*      */         //   0	35	1	fromKey	K
/*      */         //   0	35	2	toKey	K
/*      */         //   5	26	3	Ljava/lang/Object;	Object
/*      */         //   28	5	4	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	27	28	finally
/*      */         //   28	32	28	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public SortedMap<K, V> tailMap(K fromKey)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedSortedMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedSortedMap:delegate	()Ljava/util/SortedMap;
/*      */         //   11: aload_1
/*      */         //   12: invokeinterface 12 2 0
/*      */         //   17: aload_0
/*      */         //   18: getfield 4	com/google/common/collect/Synchronized$SynchronizedSortedMap:mutex	Ljava/lang/Object;
/*      */         //   21: invokestatic 9	com/google/common/collect/Synchronized:sortedMap	(Ljava/util/SortedMap;Ljava/lang/Object;)Ljava/util/SortedMap;
/*      */         //   24: aload_2
/*      */         //   25: monitorexit
/*      */         //   26: areturn
/*      */         //   27: astore_3
/*      */         //   28: aload_2
/*      */         //   29: monitorexit
/*      */         //   30: aload_3
/*      */         //   31: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1286	-> byte code offset #0
/*      */         //   Java source line #1287	-> byte code offset #7
/*      */         //   Java source line #1288	-> byte code offset #27
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	32	0	this	SynchronizedSortedMap<K, V>
/*      */         //   0	32	1	fromKey	K
/*      */         //   5	24	2	Ljava/lang/Object;	Object
/*      */         //   27	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	26	27	finally
/*      */         //   27	30	27	finally
/*      */       }
/*      */     }
/*      */     
/*      */     static <K, V> BiMap<K, V> biMap(BiMap<K, V> bimap, @Nullable Object mutex)
/*      */     {
/* 1295 */       if (((bimap instanceof SynchronizedBiMap)) || ((bimap instanceof ImmutableBiMap))) {
/* 1296 */         return bimap;
/*      */       }
/* 1298 */       return new SynchronizedBiMap(bimap, mutex, null, null);
/*      */     }
/*      */     
/*      */     @VisibleForTesting
/*      */     static class SynchronizedBiMap<K, V> extends Synchronized.SynchronizedMap<K, V> implements BiMap<K, V>, Serializable
/*      */     {
/*      */       private transient Set<V> valueSet;
/*      */       @RetainedWith
/*      */       private transient BiMap<V, K> inverse;
/*      */       private static final long serialVersionUID = 0L;
/*      */       
/*      */       private SynchronizedBiMap(BiMap<K, V> delegate, @Nullable Object mutex, @Nullable BiMap<V, K> inverse) {
/* 1310 */         super(mutex);
/* 1311 */         this.inverse = inverse;
/*      */       }
/*      */       
/*      */       BiMap<K, V> delegate()
/*      */       {
/* 1316 */         return (BiMap)super.delegate();
/*      */       }
/*      */       
/*      */       public Set<V> values()
/*      */       {
/* 1321 */         synchronized (this.mutex) {
/* 1322 */           if (this.valueSet == null) {
/* 1323 */             this.valueSet = Synchronized.set(delegate().values(), this.mutex);
/*      */           }
/* 1325 */           return this.valueSet;
/*      */         }
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public V forcePut(K key, V value)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 6	com/google/common/collect/Synchronized$SynchronizedBiMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_3
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 8	com/google/common/collect/Synchronized$SynchronizedBiMap:delegate	()Lcom/google/common/collect/BiMap;
/*      */         //   11: aload_1
/*      */         //   12: aload_2
/*      */         //   13: invokeinterface 11 3 0
/*      */         //   18: aload_3
/*      */         //   19: monitorexit
/*      */         //   20: areturn
/*      */         //   21: astore 4
/*      */         //   23: aload_3
/*      */         //   24: monitorexit
/*      */         //   25: aload 4
/*      */         //   27: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1331	-> byte code offset #0
/*      */         //   Java source line #1332	-> byte code offset #7
/*      */         //   Java source line #1333	-> byte code offset #21
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	28	0	this	SynchronizedBiMap<K, V>
/*      */         //   0	28	1	key	K
/*      */         //   0	28	2	value	V
/*      */         //   5	19	3	Ljava/lang/Object;	Object
/*      */         //   21	5	4	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	20	21	finally
/*      */         //   21	25	21	finally
/*      */       }
/*      */       
/*      */       public BiMap<V, K> inverse()
/*      */       {
/* 1338 */         synchronized (this.mutex) {
/* 1339 */           if (this.inverse == null) {
/* 1340 */             this.inverse = new SynchronizedBiMap(delegate().inverse(), this.mutex, this);
/*      */           }
/* 1342 */           return this.inverse;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     private static class SynchronizedAsMap<K, V> extends Synchronized.SynchronizedMap<K, Collection<V>>
/*      */     {
/*      */       transient Set<Map.Entry<K, Collection<V>>> asMapEntrySet;
/*      */       transient Collection<Collection<V>> asMapValues;
/*      */       private static final long serialVersionUID = 0L;
/*      */       
/*      */       SynchronizedAsMap(Map<K, Collection<V>> delegate, @Nullable Object mutex) {
/* 1354 */         super(mutex);
/*      */       }
/*      */       
/*      */       public Collection<V> get(Object key)
/*      */       {
/* 1359 */         synchronized (this.mutex) {
/* 1360 */           Collection<V> collection = (Collection)super.get(key);
/* 1361 */           return collection == null ? null : Synchronized.typePreservingCollection(collection, this.mutex);
/*      */         }
/*      */       }
/*      */       
/*      */       public Set<Map.Entry<K, Collection<V>>> entrySet()
/*      */       {
/* 1367 */         synchronized (this.mutex) {
/* 1368 */           if (this.asMapEntrySet == null) {
/* 1369 */             this.asMapEntrySet = new Synchronized.SynchronizedAsMapEntries(delegate().entrySet(), this.mutex);
/*      */           }
/* 1371 */           return this.asMapEntrySet;
/*      */         }
/*      */       }
/*      */       
/*      */       public Collection<Collection<V>> values()
/*      */       {
/* 1377 */         synchronized (this.mutex) {
/* 1378 */           if (this.asMapValues == null) {
/* 1379 */             this.asMapValues = new Synchronized.SynchronizedAsMapValues(delegate().values(), this.mutex);
/*      */           }
/* 1381 */           return this.asMapValues;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */       public boolean containsValue(Object o)
/*      */       {
/* 1388 */         return values().contains(o);
/*      */       }
/*      */     }
/*      */     
/*      */     private static class SynchronizedAsMapValues<V> extends Synchronized.SynchronizedCollection<Collection<V>> {
/*      */       private static final long serialVersionUID = 0L;
/*      */       
/*      */       SynchronizedAsMapValues(Collection<Collection<V>> delegate, @Nullable Object mutex) {
/* 1396 */         super(mutex, null);
/*      */       }
/*      */       
/*      */ 
/*      */       public Iterator<Collection<V>> iterator()
/*      */       {
/* 1402 */         new TransformedIterator(super.iterator())
/*      */         {
/*      */           Collection<V> transform(Collection<V> from) {
/* 1405 */             return Synchronized.typePreservingCollection(from, Synchronized.SynchronizedAsMapValues.this.mutex);
/*      */           }
/*      */         };
/*      */       }
/*      */     }
/*      */     
/*      */     @GwtIncompatible
/*      */     @VisibleForTesting
/*      */     static class SynchronizedNavigableSet<E> extends Synchronized.SynchronizedSortedSet<E> implements NavigableSet<E> {
/*      */       transient NavigableSet<E> descendingSet;
/*      */       private static final long serialVersionUID = 0L;
/*      */       
/*      */       SynchronizedNavigableSet(NavigableSet<E> delegate, @Nullable Object mutex) {
/* 1418 */         super(mutex);
/*      */       }
/*      */       
/*      */       NavigableSet<E> delegate()
/*      */       {
/* 1423 */         return (NavigableSet)super.delegate();
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public E ceiling(E e)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableSet:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedNavigableSet:delegate	()Ljava/util/NavigableSet;
/*      */         //   11: aload_1
/*      */         //   12: invokeinterface 6 2 0
/*      */         //   17: aload_2
/*      */         //   18: monitorexit
/*      */         //   19: areturn
/*      */         //   20: astore_3
/*      */         //   21: aload_2
/*      */         //   22: monitorexit
/*      */         //   23: aload_3
/*      */         //   24: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1428	-> byte code offset #0
/*      */         //   Java source line #1429	-> byte code offset #7
/*      */         //   Java source line #1430	-> byte code offset #20
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	25	0	this	SynchronizedNavigableSet<E>
/*      */         //   0	25	1	e	E
/*      */         //   5	17	2	Ljava/lang/Object;	Object
/*      */         //   20	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	19	20	finally
/*      */         //   20	23	20	finally
/*      */       }
/*      */       
/*      */       public Iterator<E> descendingIterator()
/*      */       {
/* 1435 */         return delegate().descendingIterator();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */       public NavigableSet<E> descendingSet()
/*      */       {
/* 1442 */         synchronized (this.mutex) {
/* 1443 */           if (this.descendingSet == null) {
/* 1444 */             NavigableSet<E> dS = Synchronized.navigableSet(delegate().descendingSet(), this.mutex);
/* 1445 */             this.descendingSet = dS;
/* 1446 */             return dS;
/*      */           }
/* 1448 */           return this.descendingSet;
/*      */         }
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public E floor(E e)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableSet:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedNavigableSet:delegate	()Ljava/util/NavigableSet;
/*      */         //   11: aload_1
/*      */         //   12: invokeinterface 11 2 0
/*      */         //   17: aload_2
/*      */         //   18: monitorexit
/*      */         //   19: areturn
/*      */         //   20: astore_3
/*      */         //   21: aload_2
/*      */         //   22: monitorexit
/*      */         //   23: aload_3
/*      */         //   24: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1454	-> byte code offset #0
/*      */         //   Java source line #1455	-> byte code offset #7
/*      */         //   Java source line #1456	-> byte code offset #20
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	25	0	this	SynchronizedNavigableSet<E>
/*      */         //   0	25	1	e	E
/*      */         //   5	17	2	Ljava/lang/Object;	Object
/*      */         //   20	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	19	20	finally
/*      */         //   20	23	20	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public NavigableSet<E> headSet(E toElement, boolean inclusive)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableSet:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_3
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedNavigableSet:delegate	()Ljava/util/NavigableSet;
/*      */         //   11: aload_1
/*      */         //   12: iload_2
/*      */         //   13: invokeinterface 12 3 0
/*      */         //   18: aload_0
/*      */         //   19: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableSet:mutex	Ljava/lang/Object;
/*      */         //   22: invokestatic 10	com/google/common/collect/Synchronized:navigableSet	(Ljava/util/NavigableSet;Ljava/lang/Object;)Ljava/util/NavigableSet;
/*      */         //   25: aload_3
/*      */         //   26: monitorexit
/*      */         //   27: areturn
/*      */         //   28: astore 4
/*      */         //   30: aload_3
/*      */         //   31: monitorexit
/*      */         //   32: aload 4
/*      */         //   34: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1461	-> byte code offset #0
/*      */         //   Java source line #1462	-> byte code offset #7
/*      */         //   Java source line #1463	-> byte code offset #28
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	35	0	this	SynchronizedNavigableSet<E>
/*      */         //   0	35	1	toElement	E
/*      */         //   0	35	2	inclusive	boolean
/*      */         //   5	26	3	Ljava/lang/Object;	Object
/*      */         //   28	5	4	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	27	28	finally
/*      */         //   28	32	28	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public E higher(E e)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableSet:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedNavigableSet:delegate	()Ljava/util/NavigableSet;
/*      */         //   11: aload_1
/*      */         //   12: invokeinterface 13 2 0
/*      */         //   17: aload_2
/*      */         //   18: monitorexit
/*      */         //   19: areturn
/*      */         //   20: astore_3
/*      */         //   21: aload_2
/*      */         //   22: monitorexit
/*      */         //   23: aload_3
/*      */         //   24: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1468	-> byte code offset #0
/*      */         //   Java source line #1469	-> byte code offset #7
/*      */         //   Java source line #1470	-> byte code offset #20
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	25	0	this	SynchronizedNavigableSet<E>
/*      */         //   0	25	1	e	E
/*      */         //   5	17	2	Ljava/lang/Object;	Object
/*      */         //   20	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	19	20	finally
/*      */         //   20	23	20	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public E lower(E e)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableSet:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedNavigableSet:delegate	()Ljava/util/NavigableSet;
/*      */         //   11: aload_1
/*      */         //   12: invokeinterface 14 2 0
/*      */         //   17: aload_2
/*      */         //   18: monitorexit
/*      */         //   19: areturn
/*      */         //   20: astore_3
/*      */         //   21: aload_2
/*      */         //   22: monitorexit
/*      */         //   23: aload_3
/*      */         //   24: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1475	-> byte code offset #0
/*      */         //   Java source line #1476	-> byte code offset #7
/*      */         //   Java source line #1477	-> byte code offset #20
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	25	0	this	SynchronizedNavigableSet<E>
/*      */         //   0	25	1	e	E
/*      */         //   5	17	2	Ljava/lang/Object;	Object
/*      */         //   20	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	19	20	finally
/*      */         //   20	23	20	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public E pollFirst()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableSet:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedNavigableSet:delegate	()Ljava/util/NavigableSet;
/*      */         //   11: invokeinterface 15 1 0
/*      */         //   16: aload_1
/*      */         //   17: monitorexit
/*      */         //   18: areturn
/*      */         //   19: astore_2
/*      */         //   20: aload_1
/*      */         //   21: monitorexit
/*      */         //   22: aload_2
/*      */         //   23: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1482	-> byte code offset #0
/*      */         //   Java source line #1483	-> byte code offset #7
/*      */         //   Java source line #1484	-> byte code offset #19
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	24	0	this	SynchronizedNavigableSet<E>
/*      */         //   5	16	1	Ljava/lang/Object;	Object
/*      */         //   19	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	18	19	finally
/*      */         //   19	22	19	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public E pollLast()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableSet:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedNavigableSet:delegate	()Ljava/util/NavigableSet;
/*      */         //   11: invokeinterface 16 1 0
/*      */         //   16: aload_1
/*      */         //   17: monitorexit
/*      */         //   18: areturn
/*      */         //   19: astore_2
/*      */         //   20: aload_1
/*      */         //   21: monitorexit
/*      */         //   22: aload_2
/*      */         //   23: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1489	-> byte code offset #0
/*      */         //   Java source line #1490	-> byte code offset #7
/*      */         //   Java source line #1491	-> byte code offset #19
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	24	0	this	SynchronizedNavigableSet<E>
/*      */         //   5	16	1	Ljava/lang/Object;	Object
/*      */         //   19	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	18	19	finally
/*      */         //   19	22	19	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableSet:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore 5
/*      */         //   7: monitorenter
/*      */         //   8: aload_0
/*      */         //   9: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedNavigableSet:delegate	()Ljava/util/NavigableSet;
/*      */         //   12: aload_1
/*      */         //   13: iload_2
/*      */         //   14: aload_3
/*      */         //   15: iload 4
/*      */         //   17: invokeinterface 17 5 0
/*      */         //   22: aload_0
/*      */         //   23: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableSet:mutex	Ljava/lang/Object;
/*      */         //   26: invokestatic 10	com/google/common/collect/Synchronized:navigableSet	(Ljava/util/NavigableSet;Ljava/lang/Object;)Ljava/util/NavigableSet;
/*      */         //   29: aload 5
/*      */         //   31: monitorexit
/*      */         //   32: areturn
/*      */         //   33: astore 6
/*      */         //   35: aload 5
/*      */         //   37: monitorexit
/*      */         //   38: aload 6
/*      */         //   40: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1497	-> byte code offset #0
/*      */         //   Java source line #1498	-> byte code offset #8
/*      */         //   Java source line #1499	-> byte code offset #9
/*      */         //   Java source line #1498	-> byte code offset #26
/*      */         //   Java source line #1500	-> byte code offset #33
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	41	0	this	SynchronizedNavigableSet<E>
/*      */         //   0	41	1	fromElement	E
/*      */         //   0	41	2	fromInclusive	boolean
/*      */         //   0	41	3	toElement	E
/*      */         //   0	41	4	toInclusive	boolean
/*      */         //   5	31	5	Ljava/lang/Object;	Object
/*      */         //   33	6	6	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   8	32	33	finally
/*      */         //   33	38	33	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public NavigableSet<E> tailSet(E fromElement, boolean inclusive)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableSet:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_3
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedNavigableSet:delegate	()Ljava/util/NavigableSet;
/*      */         //   11: aload_1
/*      */         //   12: iload_2
/*      */         //   13: invokeinterface 18 3 0
/*      */         //   18: aload_0
/*      */         //   19: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableSet:mutex	Ljava/lang/Object;
/*      */         //   22: invokestatic 10	com/google/common/collect/Synchronized:navigableSet	(Ljava/util/NavigableSet;Ljava/lang/Object;)Ljava/util/NavigableSet;
/*      */         //   25: aload_3
/*      */         //   26: monitorexit
/*      */         //   27: areturn
/*      */         //   28: astore 4
/*      */         //   30: aload_3
/*      */         //   31: monitorexit
/*      */         //   32: aload 4
/*      */         //   34: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1505	-> byte code offset #0
/*      */         //   Java source line #1506	-> byte code offset #7
/*      */         //   Java source line #1507	-> byte code offset #28
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	35	0	this	SynchronizedNavigableSet<E>
/*      */         //   0	35	1	fromElement	E
/*      */         //   0	35	2	inclusive	boolean
/*      */         //   5	26	3	Ljava/lang/Object;	Object
/*      */         //   28	5	4	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	27	28	finally
/*      */         //   28	32	28	finally
/*      */       }
/*      */       
/*      */       public SortedSet<E> headSet(E toElement)
/*      */       {
/* 1512 */         return headSet(toElement, false);
/*      */       }
/*      */       
/*      */       public SortedSet<E> subSet(E fromElement, E toElement)
/*      */       {
/* 1517 */         return subSet(fromElement, true, toElement, false);
/*      */       }
/*      */       
/*      */       public SortedSet<E> tailSet(E fromElement)
/*      */       {
/* 1522 */         return tailSet(fromElement, true);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     @GwtIncompatible
/*      */     static <E> NavigableSet<E> navigableSet(NavigableSet<E> navigableSet, @Nullable Object mutex)
/*      */     {
/* 1530 */       return new SynchronizedNavigableSet(navigableSet, mutex);
/*      */     }
/*      */     
/*      */     @GwtIncompatible
/*      */     static <E> NavigableSet<E> navigableSet(NavigableSet<E> navigableSet) {
/* 1535 */       return navigableSet(navigableSet, null);
/*      */     }
/*      */     
/*      */     @GwtIncompatible
/*      */     static <K, V> NavigableMap<K, V> navigableMap(NavigableMap<K, V> navigableMap) {
/* 1540 */       return navigableMap(navigableMap, null);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     @GwtIncompatible
/* 1546 */     static <K, V> NavigableMap<K, V> navigableMap(NavigableMap<K, V> navigableMap, @Nullable Object mutex) { return new SynchronizedNavigableMap(navigableMap, mutex); }
/*      */     
/*      */     @GwtIncompatible
/*      */     @VisibleForTesting
/*      */     static class SynchronizedNavigableMap<K, V> extends Synchronized.SynchronizedSortedMap<K, V> implements NavigableMap<K, V> { transient NavigableSet<K> descendingKeySet;
/*      */       transient NavigableMap<K, V> descendingMap;
/*      */       transient NavigableSet<K> navigableKeySet;
/*      */       private static final long serialVersionUID = 0L;
/*      */       
/* 1555 */       SynchronizedNavigableMap(NavigableMap<K, V> delegate, @Nullable Object mutex) { super(mutex); }
/*      */       
/*      */ 
/*      */       NavigableMap<K, V> delegate()
/*      */       {
/* 1560 */         return (NavigableMap)super.delegate();
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public Map.Entry<K, V> ceilingEntry(K key)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedNavigableMap:delegate	()Ljava/util/NavigableMap;
/*      */         //   11: aload_1
/*      */         //   12: invokeinterface 6 2 0
/*      */         //   17: aload_0
/*      */         //   18: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableMap:mutex	Ljava/lang/Object;
/*      */         //   21: invokestatic 7	com/google/common/collect/Synchronized:access$700	(Ljava/util/Map$Entry;Ljava/lang/Object;)Ljava/util/Map$Entry;
/*      */         //   24: aload_2
/*      */         //   25: monitorexit
/*      */         //   26: areturn
/*      */         //   27: astore_3
/*      */         //   28: aload_2
/*      */         //   29: monitorexit
/*      */         //   30: aload_3
/*      */         //   31: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1565	-> byte code offset #0
/*      */         //   Java source line #1566	-> byte code offset #7
/*      */         //   Java source line #1567	-> byte code offset #27
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	32	0	this	SynchronizedNavigableMap<K, V>
/*      */         //   0	32	1	key	K
/*      */         //   5	24	2	Ljava/lang/Object;	Object
/*      */         //   27	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	26	27	finally
/*      */         //   27	30	27	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public K ceilingKey(K key)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedNavigableMap:delegate	()Ljava/util/NavigableMap;
/*      */         //   11: aload_1
/*      */         //   12: invokeinterface 8 2 0
/*      */         //   17: aload_2
/*      */         //   18: monitorexit
/*      */         //   19: areturn
/*      */         //   20: astore_3
/*      */         //   21: aload_2
/*      */         //   22: monitorexit
/*      */         //   23: aload_3
/*      */         //   24: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1572	-> byte code offset #0
/*      */         //   Java source line #1573	-> byte code offset #7
/*      */         //   Java source line #1574	-> byte code offset #20
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	25	0	this	SynchronizedNavigableMap<K, V>
/*      */         //   0	25	1	key	K
/*      */         //   5	17	2	Ljava/lang/Object;	Object
/*      */         //   20	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	19	20	finally
/*      */         //   20	23	20	finally
/*      */       }
/*      */       
/*      */       public NavigableSet<K> descendingKeySet()
/*      */       {
/* 1581 */         synchronized (this.mutex) {
/* 1582 */           if (this.descendingKeySet == null) {
/* 1583 */             return this.descendingKeySet = Synchronized.navigableSet(delegate().descendingKeySet(), this.mutex);
/*      */           }
/* 1585 */           return this.descendingKeySet;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */       public NavigableMap<K, V> descendingMap()
/*      */       {
/* 1593 */         synchronized (this.mutex) {
/* 1594 */           if (this.descendingMap == null) {
/* 1595 */             return this.descendingMap = Synchronized.navigableMap(delegate().descendingMap(), this.mutex);
/*      */           }
/* 1597 */           return this.descendingMap;
/*      */         }
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public Map.Entry<K, V> firstEntry()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedNavigableMap:delegate	()Ljava/util/NavigableMap;
/*      */         //   11: invokeinterface 15 1 0
/*      */         //   16: aload_0
/*      */         //   17: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableMap:mutex	Ljava/lang/Object;
/*      */         //   20: invokestatic 7	com/google/common/collect/Synchronized:access$700	(Ljava/util/Map$Entry;Ljava/lang/Object;)Ljava/util/Map$Entry;
/*      */         //   23: aload_1
/*      */         //   24: monitorexit
/*      */         //   25: areturn
/*      */         //   26: astore_2
/*      */         //   27: aload_1
/*      */         //   28: monitorexit
/*      */         //   29: aload_2
/*      */         //   30: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1603	-> byte code offset #0
/*      */         //   Java source line #1604	-> byte code offset #7
/*      */         //   Java source line #1605	-> byte code offset #26
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	31	0	this	SynchronizedNavigableMap<K, V>
/*      */         //   5	23	1	Ljava/lang/Object;	Object
/*      */         //   26	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	25	26	finally
/*      */         //   26	29	26	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public Map.Entry<K, V> floorEntry(K key)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedNavigableMap:delegate	()Ljava/util/NavigableMap;
/*      */         //   11: aload_1
/*      */         //   12: invokeinterface 16 2 0
/*      */         //   17: aload_0
/*      */         //   18: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableMap:mutex	Ljava/lang/Object;
/*      */         //   21: invokestatic 7	com/google/common/collect/Synchronized:access$700	(Ljava/util/Map$Entry;Ljava/lang/Object;)Ljava/util/Map$Entry;
/*      */         //   24: aload_2
/*      */         //   25: monitorexit
/*      */         //   26: areturn
/*      */         //   27: astore_3
/*      */         //   28: aload_2
/*      */         //   29: monitorexit
/*      */         //   30: aload_3
/*      */         //   31: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1610	-> byte code offset #0
/*      */         //   Java source line #1611	-> byte code offset #7
/*      */         //   Java source line #1612	-> byte code offset #27
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	32	0	this	SynchronizedNavigableMap<K, V>
/*      */         //   0	32	1	key	K
/*      */         //   5	24	2	Ljava/lang/Object;	Object
/*      */         //   27	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	26	27	finally
/*      */         //   27	30	27	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public K floorKey(K key)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedNavigableMap:delegate	()Ljava/util/NavigableMap;
/*      */         //   11: aload_1
/*      */         //   12: invokeinterface 17 2 0
/*      */         //   17: aload_2
/*      */         //   18: monitorexit
/*      */         //   19: areturn
/*      */         //   20: astore_3
/*      */         //   21: aload_2
/*      */         //   22: monitorexit
/*      */         //   23: aload_3
/*      */         //   24: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1617	-> byte code offset #0
/*      */         //   Java source line #1618	-> byte code offset #7
/*      */         //   Java source line #1619	-> byte code offset #20
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	25	0	this	SynchronizedNavigableMap<K, V>
/*      */         //   0	25	1	key	K
/*      */         //   5	17	2	Ljava/lang/Object;	Object
/*      */         //   20	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	19	20	finally
/*      */         //   20	23	20	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public NavigableMap<K, V> headMap(K toKey, boolean inclusive)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_3
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedNavigableMap:delegate	()Ljava/util/NavigableMap;
/*      */         //   11: aload_1
/*      */         //   12: iload_2
/*      */         //   13: invokeinterface 18 3 0
/*      */         //   18: aload_0
/*      */         //   19: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableMap:mutex	Ljava/lang/Object;
/*      */         //   22: invokestatic 14	com/google/common/collect/Synchronized:navigableMap	(Ljava/util/NavigableMap;Ljava/lang/Object;)Ljava/util/NavigableMap;
/*      */         //   25: aload_3
/*      */         //   26: monitorexit
/*      */         //   27: areturn
/*      */         //   28: astore 4
/*      */         //   30: aload_3
/*      */         //   31: monitorexit
/*      */         //   32: aload 4
/*      */         //   34: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1624	-> byte code offset #0
/*      */         //   Java source line #1625	-> byte code offset #7
/*      */         //   Java source line #1626	-> byte code offset #28
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	35	0	this	SynchronizedNavigableMap<K, V>
/*      */         //   0	35	1	toKey	K
/*      */         //   0	35	2	inclusive	boolean
/*      */         //   5	26	3	Ljava/lang/Object;	Object
/*      */         //   28	5	4	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	27	28	finally
/*      */         //   28	32	28	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public Map.Entry<K, V> higherEntry(K key)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedNavigableMap:delegate	()Ljava/util/NavigableMap;
/*      */         //   11: aload_1
/*      */         //   12: invokeinterface 19 2 0
/*      */         //   17: aload_0
/*      */         //   18: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableMap:mutex	Ljava/lang/Object;
/*      */         //   21: invokestatic 7	com/google/common/collect/Synchronized:access$700	(Ljava/util/Map$Entry;Ljava/lang/Object;)Ljava/util/Map$Entry;
/*      */         //   24: aload_2
/*      */         //   25: monitorexit
/*      */         //   26: areturn
/*      */         //   27: astore_3
/*      */         //   28: aload_2
/*      */         //   29: monitorexit
/*      */         //   30: aload_3
/*      */         //   31: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1631	-> byte code offset #0
/*      */         //   Java source line #1632	-> byte code offset #7
/*      */         //   Java source line #1633	-> byte code offset #27
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	32	0	this	SynchronizedNavigableMap<K, V>
/*      */         //   0	32	1	key	K
/*      */         //   5	24	2	Ljava/lang/Object;	Object
/*      */         //   27	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	26	27	finally
/*      */         //   27	30	27	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public K higherKey(K key)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedNavigableMap:delegate	()Ljava/util/NavigableMap;
/*      */         //   11: aload_1
/*      */         //   12: invokeinterface 20 2 0
/*      */         //   17: aload_2
/*      */         //   18: monitorexit
/*      */         //   19: areturn
/*      */         //   20: astore_3
/*      */         //   21: aload_2
/*      */         //   22: monitorexit
/*      */         //   23: aload_3
/*      */         //   24: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1638	-> byte code offset #0
/*      */         //   Java source line #1639	-> byte code offset #7
/*      */         //   Java source line #1640	-> byte code offset #20
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	25	0	this	SynchronizedNavigableMap<K, V>
/*      */         //   0	25	1	key	K
/*      */         //   5	17	2	Ljava/lang/Object;	Object
/*      */         //   20	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	19	20	finally
/*      */         //   20	23	20	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public Map.Entry<K, V> lastEntry()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedNavigableMap:delegate	()Ljava/util/NavigableMap;
/*      */         //   11: invokeinterface 21 1 0
/*      */         //   16: aload_0
/*      */         //   17: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableMap:mutex	Ljava/lang/Object;
/*      */         //   20: invokestatic 7	com/google/common/collect/Synchronized:access$700	(Ljava/util/Map$Entry;Ljava/lang/Object;)Ljava/util/Map$Entry;
/*      */         //   23: aload_1
/*      */         //   24: monitorexit
/*      */         //   25: areturn
/*      */         //   26: astore_2
/*      */         //   27: aload_1
/*      */         //   28: monitorexit
/*      */         //   29: aload_2
/*      */         //   30: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1645	-> byte code offset #0
/*      */         //   Java source line #1646	-> byte code offset #7
/*      */         //   Java source line #1647	-> byte code offset #26
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	31	0	this	SynchronizedNavigableMap<K, V>
/*      */         //   5	23	1	Ljava/lang/Object;	Object
/*      */         //   26	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	25	26	finally
/*      */         //   26	29	26	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public Map.Entry<K, V> lowerEntry(K key)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedNavigableMap:delegate	()Ljava/util/NavigableMap;
/*      */         //   11: aload_1
/*      */         //   12: invokeinterface 22 2 0
/*      */         //   17: aload_0
/*      */         //   18: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableMap:mutex	Ljava/lang/Object;
/*      */         //   21: invokestatic 7	com/google/common/collect/Synchronized:access$700	(Ljava/util/Map$Entry;Ljava/lang/Object;)Ljava/util/Map$Entry;
/*      */         //   24: aload_2
/*      */         //   25: monitorexit
/*      */         //   26: areturn
/*      */         //   27: astore_3
/*      */         //   28: aload_2
/*      */         //   29: monitorexit
/*      */         //   30: aload_3
/*      */         //   31: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1652	-> byte code offset #0
/*      */         //   Java source line #1653	-> byte code offset #7
/*      */         //   Java source line #1654	-> byte code offset #27
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	32	0	this	SynchronizedNavigableMap<K, V>
/*      */         //   0	32	1	key	K
/*      */         //   5	24	2	Ljava/lang/Object;	Object
/*      */         //   27	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	26	27	finally
/*      */         //   27	30	27	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public K lowerKey(K key)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedNavigableMap:delegate	()Ljava/util/NavigableMap;
/*      */         //   11: aload_1
/*      */         //   12: invokeinterface 23 2 0
/*      */         //   17: aload_2
/*      */         //   18: monitorexit
/*      */         //   19: areturn
/*      */         //   20: astore_3
/*      */         //   21: aload_2
/*      */         //   22: monitorexit
/*      */         //   23: aload_3
/*      */         //   24: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1659	-> byte code offset #0
/*      */         //   Java source line #1660	-> byte code offset #7
/*      */         //   Java source line #1661	-> byte code offset #20
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	25	0	this	SynchronizedNavigableMap<K, V>
/*      */         //   0	25	1	key	K
/*      */         //   5	17	2	Ljava/lang/Object;	Object
/*      */         //   20	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	19	20	finally
/*      */         //   20	23	20	finally
/*      */       }
/*      */       
/*      */       public Set<K> keySet()
/*      */       {
/* 1666 */         return navigableKeySet();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */       public NavigableSet<K> navigableKeySet()
/*      */       {
/* 1673 */         synchronized (this.mutex) {
/* 1674 */           if (this.navigableKeySet == null) {
/* 1675 */             return this.navigableKeySet = Synchronized.navigableSet(delegate().navigableKeySet(), this.mutex);
/*      */           }
/* 1677 */           return this.navigableKeySet;
/*      */         }
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public Map.Entry<K, V> pollFirstEntry()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedNavigableMap:delegate	()Ljava/util/NavigableMap;
/*      */         //   11: invokeinterface 27 1 0
/*      */         //   16: aload_0
/*      */         //   17: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableMap:mutex	Ljava/lang/Object;
/*      */         //   20: invokestatic 7	com/google/common/collect/Synchronized:access$700	(Ljava/util/Map$Entry;Ljava/lang/Object;)Ljava/util/Map$Entry;
/*      */         //   23: aload_1
/*      */         //   24: monitorexit
/*      */         //   25: areturn
/*      */         //   26: astore_2
/*      */         //   27: aload_1
/*      */         //   28: monitorexit
/*      */         //   29: aload_2
/*      */         //   30: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1683	-> byte code offset #0
/*      */         //   Java source line #1684	-> byte code offset #7
/*      */         //   Java source line #1685	-> byte code offset #26
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	31	0	this	SynchronizedNavigableMap<K, V>
/*      */         //   5	23	1	Ljava/lang/Object;	Object
/*      */         //   26	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	25	26	finally
/*      */         //   26	29	26	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public Map.Entry<K, V> pollLastEntry()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedNavigableMap:delegate	()Ljava/util/NavigableMap;
/*      */         //   11: invokeinterface 28 1 0
/*      */         //   16: aload_0
/*      */         //   17: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableMap:mutex	Ljava/lang/Object;
/*      */         //   20: invokestatic 7	com/google/common/collect/Synchronized:access$700	(Ljava/util/Map$Entry;Ljava/lang/Object;)Ljava/util/Map$Entry;
/*      */         //   23: aload_1
/*      */         //   24: monitorexit
/*      */         //   25: areturn
/*      */         //   26: astore_2
/*      */         //   27: aload_1
/*      */         //   28: monitorexit
/*      */         //   29: aload_2
/*      */         //   30: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1690	-> byte code offset #0
/*      */         //   Java source line #1691	-> byte code offset #7
/*      */         //   Java source line #1692	-> byte code offset #26
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	31	0	this	SynchronizedNavigableMap<K, V>
/*      */         //   5	23	1	Ljava/lang/Object;	Object
/*      */         //   26	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	25	26	finally
/*      */         //   26	29	26	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore 5
/*      */         //   7: monitorenter
/*      */         //   8: aload_0
/*      */         //   9: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedNavigableMap:delegate	()Ljava/util/NavigableMap;
/*      */         //   12: aload_1
/*      */         //   13: iload_2
/*      */         //   14: aload_3
/*      */         //   15: iload 4
/*      */         //   17: invokeinterface 29 5 0
/*      */         //   22: aload_0
/*      */         //   23: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableMap:mutex	Ljava/lang/Object;
/*      */         //   26: invokestatic 14	com/google/common/collect/Synchronized:navigableMap	(Ljava/util/NavigableMap;Ljava/lang/Object;)Ljava/util/NavigableMap;
/*      */         //   29: aload 5
/*      */         //   31: monitorexit
/*      */         //   32: areturn
/*      */         //   33: astore 6
/*      */         //   35: aload 5
/*      */         //   37: monitorexit
/*      */         //   38: aload 6
/*      */         //   40: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1698	-> byte code offset #0
/*      */         //   Java source line #1699	-> byte code offset #8
/*      */         //   Java source line #1700	-> byte code offset #33
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	41	0	this	SynchronizedNavigableMap<K, V>
/*      */         //   0	41	1	fromKey	K
/*      */         //   0	41	2	fromInclusive	boolean
/*      */         //   0	41	3	toKey	K
/*      */         //   0	41	4	toInclusive	boolean
/*      */         //   5	31	5	Ljava/lang/Object;	Object
/*      */         //   33	6	6	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   8	32	33	finally
/*      */         //   33	38	33	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableMap:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_3
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedNavigableMap:delegate	()Ljava/util/NavigableMap;
/*      */         //   11: aload_1
/*      */         //   12: iload_2
/*      */         //   13: invokeinterface 30 3 0
/*      */         //   18: aload_0
/*      */         //   19: getfield 4	com/google/common/collect/Synchronized$SynchronizedNavigableMap:mutex	Ljava/lang/Object;
/*      */         //   22: invokestatic 14	com/google/common/collect/Synchronized:navigableMap	(Ljava/util/NavigableMap;Ljava/lang/Object;)Ljava/util/NavigableMap;
/*      */         //   25: aload_3
/*      */         //   26: monitorexit
/*      */         //   27: areturn
/*      */         //   28: astore 4
/*      */         //   30: aload_3
/*      */         //   31: monitorexit
/*      */         //   32: aload 4
/*      */         //   34: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1705	-> byte code offset #0
/*      */         //   Java source line #1706	-> byte code offset #7
/*      */         //   Java source line #1707	-> byte code offset #28
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	35	0	this	SynchronizedNavigableMap<K, V>
/*      */         //   0	35	1	fromKey	K
/*      */         //   0	35	2	inclusive	boolean
/*      */         //   5	26	3	Ljava/lang/Object;	Object
/*      */         //   28	5	4	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	27	28	finally
/*      */         //   28	32	28	finally
/*      */       }
/*      */       
/*      */       public SortedMap<K, V> headMap(K toKey)
/*      */       {
/* 1712 */         return headMap(toKey, false);
/*      */       }
/*      */       
/*      */       public SortedMap<K, V> subMap(K fromKey, K toKey)
/*      */       {
/* 1717 */         return subMap(fromKey, true, toKey, false);
/*      */       }
/*      */       
/*      */       public SortedMap<K, V> tailMap(K fromKey)
/*      */       {
/* 1722 */         return tailMap(fromKey, true);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     @GwtIncompatible
/*      */     private static <K, V> Map.Entry<K, V> nullableSynchronizedEntry(@Nullable Map.Entry<K, V> entry, @Nullable Object mutex)
/*      */     {
/* 1731 */       if (entry == null) {
/* 1732 */         return null;
/*      */       }
/* 1734 */       return new SynchronizedEntry(entry, mutex);
/*      */     }
/*      */     
/*      */     @GwtIncompatible
/*      */     private static class SynchronizedEntry<K, V> extends Synchronized.SynchronizedObject implements Map.Entry<K, V> {
/*      */       private static final long serialVersionUID = 0L;
/*      */       
/* 1741 */       SynchronizedEntry(Map.Entry<K, V> delegate, @Nullable Object mutex) { super(mutex); }
/*      */       
/*      */ 
/*      */ 
/*      */       Map.Entry<K, V> delegate()
/*      */       {
/* 1747 */         return (Map.Entry)super.delegate();
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public boolean equals(Object obj)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedEntry:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedEntry:delegate	()Ljava/util/Map$Entry;
/*      */         //   11: aload_1
/*      */         //   12: invokeinterface 6 2 0
/*      */         //   17: aload_2
/*      */         //   18: monitorexit
/*      */         //   19: ireturn
/*      */         //   20: astore_3
/*      */         //   21: aload_2
/*      */         //   22: monitorexit
/*      */         //   23: aload_3
/*      */         //   24: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1752	-> byte code offset #0
/*      */         //   Java source line #1753	-> byte code offset #7
/*      */         //   Java source line #1754	-> byte code offset #20
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	25	0	this	SynchronizedEntry<K, V>
/*      */         //   0	25	1	obj	Object
/*      */         //   5	17	2	Ljava/lang/Object;	Object
/*      */         //   20	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	19	20	finally
/*      */         //   20	23	20	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public int hashCode()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedEntry:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedEntry:delegate	()Ljava/util/Map$Entry;
/*      */         //   11: invokeinterface 7 1 0
/*      */         //   16: aload_1
/*      */         //   17: monitorexit
/*      */         //   18: ireturn
/*      */         //   19: astore_2
/*      */         //   20: aload_1
/*      */         //   21: monitorexit
/*      */         //   22: aload_2
/*      */         //   23: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1759	-> byte code offset #0
/*      */         //   Java source line #1760	-> byte code offset #7
/*      */         //   Java source line #1761	-> byte code offset #19
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	24	0	this	SynchronizedEntry<K, V>
/*      */         //   5	16	1	Ljava/lang/Object;	Object
/*      */         //   19	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	18	19	finally
/*      */         //   19	22	19	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public K getKey()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedEntry:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedEntry:delegate	()Ljava/util/Map$Entry;
/*      */         //   11: invokeinterface 8 1 0
/*      */         //   16: aload_1
/*      */         //   17: monitorexit
/*      */         //   18: areturn
/*      */         //   19: astore_2
/*      */         //   20: aload_1
/*      */         //   21: monitorexit
/*      */         //   22: aload_2
/*      */         //   23: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1766	-> byte code offset #0
/*      */         //   Java source line #1767	-> byte code offset #7
/*      */         //   Java source line #1768	-> byte code offset #19
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	24	0	this	SynchronizedEntry<K, V>
/*      */         //   5	16	1	Ljava/lang/Object;	Object
/*      */         //   19	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	18	19	finally
/*      */         //   19	22	19	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public V getValue()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedEntry:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedEntry:delegate	()Ljava/util/Map$Entry;
/*      */         //   11: invokeinterface 9 1 0
/*      */         //   16: aload_1
/*      */         //   17: monitorexit
/*      */         //   18: areturn
/*      */         //   19: astore_2
/*      */         //   20: aload_1
/*      */         //   21: monitorexit
/*      */         //   22: aload_2
/*      */         //   23: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1773	-> byte code offset #0
/*      */         //   Java source line #1774	-> byte code offset #7
/*      */         //   Java source line #1775	-> byte code offset #19
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	24	0	this	SynchronizedEntry<K, V>
/*      */         //   5	16	1	Ljava/lang/Object;	Object
/*      */         //   19	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	18	19	finally
/*      */         //   19	22	19	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public V setValue(V value)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedEntry:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedEntry:delegate	()Ljava/util/Map$Entry;
/*      */         //   11: aload_1
/*      */         //   12: invokeinterface 10 2 0
/*      */         //   17: aload_2
/*      */         //   18: monitorexit
/*      */         //   19: areturn
/*      */         //   20: astore_3
/*      */         //   21: aload_2
/*      */         //   22: monitorexit
/*      */         //   23: aload_3
/*      */         //   24: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1780	-> byte code offset #0
/*      */         //   Java source line #1781	-> byte code offset #7
/*      */         //   Java source line #1782	-> byte code offset #20
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	25	0	this	SynchronizedEntry<K, V>
/*      */         //   0	25	1	value	V
/*      */         //   5	17	2	Ljava/lang/Object;	Object
/*      */         //   20	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	19	20	finally
/*      */         //   20	23	20	finally
/*      */       }
/*      */     }
/*      */     
/*      */     static <E> Queue<E> queue(Queue<E> queue, @Nullable Object mutex)
/*      */     {
/* 1789 */       return (queue instanceof SynchronizedQueue) ? queue : new SynchronizedQueue(queue, mutex);
/*      */     }
/*      */     
/*      */     private static class SynchronizedQueue<E> extends Synchronized.SynchronizedCollection<E> implements Queue<E> {
/*      */       private static final long serialVersionUID = 0L;
/*      */       
/* 1795 */       SynchronizedQueue(Queue<E> delegate, @Nullable Object mutex) { super(mutex, null); }
/*      */       
/*      */ 
/*      */       Queue<E> delegate()
/*      */       {
/* 1800 */         return (Queue)super.delegate();
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public E element()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedQueue:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedQueue:delegate	()Ljava/util/Queue;
/*      */         //   11: invokeinterface 6 1 0
/*      */         //   16: aload_1
/*      */         //   17: monitorexit
/*      */         //   18: areturn
/*      */         //   19: astore_2
/*      */         //   20: aload_1
/*      */         //   21: monitorexit
/*      */         //   22: aload_2
/*      */         //   23: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1805	-> byte code offset #0
/*      */         //   Java source line #1806	-> byte code offset #7
/*      */         //   Java source line #1807	-> byte code offset #19
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	24	0	this	SynchronizedQueue<E>
/*      */         //   5	16	1	Ljava/lang/Object;	Object
/*      */         //   19	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	18	19	finally
/*      */         //   19	22	19	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public boolean offer(E e)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedQueue:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedQueue:delegate	()Ljava/util/Queue;
/*      */         //   11: aload_1
/*      */         //   12: invokeinterface 7 2 0
/*      */         //   17: aload_2
/*      */         //   18: monitorexit
/*      */         //   19: ireturn
/*      */         //   20: astore_3
/*      */         //   21: aload_2
/*      */         //   22: monitorexit
/*      */         //   23: aload_3
/*      */         //   24: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1812	-> byte code offset #0
/*      */         //   Java source line #1813	-> byte code offset #7
/*      */         //   Java source line #1814	-> byte code offset #20
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	25	0	this	SynchronizedQueue<E>
/*      */         //   0	25	1	e	E
/*      */         //   5	17	2	Ljava/lang/Object;	Object
/*      */         //   20	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	19	20	finally
/*      */         //   20	23	20	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public E peek()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedQueue:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedQueue:delegate	()Ljava/util/Queue;
/*      */         //   11: invokeinterface 8 1 0
/*      */         //   16: aload_1
/*      */         //   17: monitorexit
/*      */         //   18: areturn
/*      */         //   19: astore_2
/*      */         //   20: aload_1
/*      */         //   21: monitorexit
/*      */         //   22: aload_2
/*      */         //   23: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1819	-> byte code offset #0
/*      */         //   Java source line #1820	-> byte code offset #7
/*      */         //   Java source line #1821	-> byte code offset #19
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	24	0	this	SynchronizedQueue<E>
/*      */         //   5	16	1	Ljava/lang/Object;	Object
/*      */         //   19	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	18	19	finally
/*      */         //   19	22	19	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public E poll()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedQueue:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedQueue:delegate	()Ljava/util/Queue;
/*      */         //   11: invokeinterface 9 1 0
/*      */         //   16: aload_1
/*      */         //   17: monitorexit
/*      */         //   18: areturn
/*      */         //   19: astore_2
/*      */         //   20: aload_1
/*      */         //   21: monitorexit
/*      */         //   22: aload_2
/*      */         //   23: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1826	-> byte code offset #0
/*      */         //   Java source line #1827	-> byte code offset #7
/*      */         //   Java source line #1828	-> byte code offset #19
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	24	0	this	SynchronizedQueue<E>
/*      */         //   5	16	1	Ljava/lang/Object;	Object
/*      */         //   19	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	18	19	finally
/*      */         //   19	22	19	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public E remove()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedQueue:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedQueue:delegate	()Ljava/util/Queue;
/*      */         //   11: invokeinterface 10 1 0
/*      */         //   16: aload_1
/*      */         //   17: monitorexit
/*      */         //   18: areturn
/*      */         //   19: astore_2
/*      */         //   20: aload_1
/*      */         //   21: monitorexit
/*      */         //   22: aload_2
/*      */         //   23: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1833	-> byte code offset #0
/*      */         //   Java source line #1834	-> byte code offset #7
/*      */         //   Java source line #1835	-> byte code offset #19
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	24	0	this	SynchronizedQueue<E>
/*      */         //   5	16	1	Ljava/lang/Object;	Object
/*      */         //   19	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	18	19	finally
/*      */         //   19	22	19	finally
/*      */       }
/*      */     }
/*      */     
/*      */     static <E> Deque<E> deque(Deque<E> deque, @Nullable Object mutex)
/*      */     {
/* 1842 */       return new SynchronizedDeque(deque, mutex);
/*      */     }
/*      */     
/*      */     private static final class SynchronizedDeque<E> extends Synchronized.SynchronizedQueue<E> implements Deque<E> {
/*      */       private static final long serialVersionUID = 0L;
/*      */       
/* 1848 */       SynchronizedDeque(Deque<E> delegate, @Nullable Object mutex) { super(mutex); }
/*      */       
/*      */ 
/*      */       Deque<E> delegate()
/*      */       {
/* 1853 */         return (Deque)super.delegate();
/*      */       }
/*      */       
/*      */       public void addFirst(E e)
/*      */       {
/* 1858 */         synchronized (this.mutex) {
/* 1859 */           delegate().addFirst(e);
/*      */         }
/*      */       }
/*      */       
/*      */       public void addLast(E e)
/*      */       {
/* 1865 */         synchronized (this.mutex) {
/* 1866 */           delegate().addLast(e);
/*      */         }
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public boolean offerFirst(E e)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedDeque:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedDeque:delegate	()Ljava/util/Deque;
/*      */         //   11: aload_1
/*      */         //   12: invokeinterface 8 2 0
/*      */         //   17: aload_2
/*      */         //   18: monitorexit
/*      */         //   19: ireturn
/*      */         //   20: astore_3
/*      */         //   21: aload_2
/*      */         //   22: monitorexit
/*      */         //   23: aload_3
/*      */         //   24: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1872	-> byte code offset #0
/*      */         //   Java source line #1873	-> byte code offset #7
/*      */         //   Java source line #1874	-> byte code offset #20
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	25	0	this	SynchronizedDeque<E>
/*      */         //   0	25	1	e	E
/*      */         //   5	17	2	Ljava/lang/Object;	Object
/*      */         //   20	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	19	20	finally
/*      */         //   20	23	20	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public boolean offerLast(E e)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedDeque:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedDeque:delegate	()Ljava/util/Deque;
/*      */         //   11: aload_1
/*      */         //   12: invokeinterface 9 2 0
/*      */         //   17: aload_2
/*      */         //   18: monitorexit
/*      */         //   19: ireturn
/*      */         //   20: astore_3
/*      */         //   21: aload_2
/*      */         //   22: monitorexit
/*      */         //   23: aload_3
/*      */         //   24: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1879	-> byte code offset #0
/*      */         //   Java source line #1880	-> byte code offset #7
/*      */         //   Java source line #1881	-> byte code offset #20
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	25	0	this	SynchronizedDeque<E>
/*      */         //   0	25	1	e	E
/*      */         //   5	17	2	Ljava/lang/Object;	Object
/*      */         //   20	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	19	20	finally
/*      */         //   20	23	20	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public E removeFirst()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedDeque:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedDeque:delegate	()Ljava/util/Deque;
/*      */         //   11: invokeinterface 10 1 0
/*      */         //   16: aload_1
/*      */         //   17: monitorexit
/*      */         //   18: areturn
/*      */         //   19: astore_2
/*      */         //   20: aload_1
/*      */         //   21: monitorexit
/*      */         //   22: aload_2
/*      */         //   23: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1886	-> byte code offset #0
/*      */         //   Java source line #1887	-> byte code offset #7
/*      */         //   Java source line #1888	-> byte code offset #19
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	24	0	this	SynchronizedDeque<E>
/*      */         //   5	16	1	Ljava/lang/Object;	Object
/*      */         //   19	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	18	19	finally
/*      */         //   19	22	19	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public E removeLast()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedDeque:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedDeque:delegate	()Ljava/util/Deque;
/*      */         //   11: invokeinterface 11 1 0
/*      */         //   16: aload_1
/*      */         //   17: monitorexit
/*      */         //   18: areturn
/*      */         //   19: astore_2
/*      */         //   20: aload_1
/*      */         //   21: monitorexit
/*      */         //   22: aload_2
/*      */         //   23: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1893	-> byte code offset #0
/*      */         //   Java source line #1894	-> byte code offset #7
/*      */         //   Java source line #1895	-> byte code offset #19
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	24	0	this	SynchronizedDeque<E>
/*      */         //   5	16	1	Ljava/lang/Object;	Object
/*      */         //   19	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	18	19	finally
/*      */         //   19	22	19	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public E pollFirst()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedDeque:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedDeque:delegate	()Ljava/util/Deque;
/*      */         //   11: invokeinterface 12 1 0
/*      */         //   16: aload_1
/*      */         //   17: monitorexit
/*      */         //   18: areturn
/*      */         //   19: astore_2
/*      */         //   20: aload_1
/*      */         //   21: monitorexit
/*      */         //   22: aload_2
/*      */         //   23: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1900	-> byte code offset #0
/*      */         //   Java source line #1901	-> byte code offset #7
/*      */         //   Java source line #1902	-> byte code offset #19
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	24	0	this	SynchronizedDeque<E>
/*      */         //   5	16	1	Ljava/lang/Object;	Object
/*      */         //   19	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	18	19	finally
/*      */         //   19	22	19	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public E pollLast()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedDeque:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedDeque:delegate	()Ljava/util/Deque;
/*      */         //   11: invokeinterface 13 1 0
/*      */         //   16: aload_1
/*      */         //   17: monitorexit
/*      */         //   18: areturn
/*      */         //   19: astore_2
/*      */         //   20: aload_1
/*      */         //   21: monitorexit
/*      */         //   22: aload_2
/*      */         //   23: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1907	-> byte code offset #0
/*      */         //   Java source line #1908	-> byte code offset #7
/*      */         //   Java source line #1909	-> byte code offset #19
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	24	0	this	SynchronizedDeque<E>
/*      */         //   5	16	1	Ljava/lang/Object;	Object
/*      */         //   19	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	18	19	finally
/*      */         //   19	22	19	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public E getFirst()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedDeque:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedDeque:delegate	()Ljava/util/Deque;
/*      */         //   11: invokeinterface 14 1 0
/*      */         //   16: aload_1
/*      */         //   17: monitorexit
/*      */         //   18: areturn
/*      */         //   19: astore_2
/*      */         //   20: aload_1
/*      */         //   21: monitorexit
/*      */         //   22: aload_2
/*      */         //   23: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1914	-> byte code offset #0
/*      */         //   Java source line #1915	-> byte code offset #7
/*      */         //   Java source line #1916	-> byte code offset #19
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	24	0	this	SynchronizedDeque<E>
/*      */         //   5	16	1	Ljava/lang/Object;	Object
/*      */         //   19	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	18	19	finally
/*      */         //   19	22	19	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public E getLast()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedDeque:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedDeque:delegate	()Ljava/util/Deque;
/*      */         //   11: invokeinterface 15 1 0
/*      */         //   16: aload_1
/*      */         //   17: monitorexit
/*      */         //   18: areturn
/*      */         //   19: astore_2
/*      */         //   20: aload_1
/*      */         //   21: monitorexit
/*      */         //   22: aload_2
/*      */         //   23: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1921	-> byte code offset #0
/*      */         //   Java source line #1922	-> byte code offset #7
/*      */         //   Java source line #1923	-> byte code offset #19
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	24	0	this	SynchronizedDeque<E>
/*      */         //   5	16	1	Ljava/lang/Object;	Object
/*      */         //   19	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	18	19	finally
/*      */         //   19	22	19	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public E peekFirst()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedDeque:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedDeque:delegate	()Ljava/util/Deque;
/*      */         //   11: invokeinterface 16 1 0
/*      */         //   16: aload_1
/*      */         //   17: monitorexit
/*      */         //   18: areturn
/*      */         //   19: astore_2
/*      */         //   20: aload_1
/*      */         //   21: monitorexit
/*      */         //   22: aload_2
/*      */         //   23: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1928	-> byte code offset #0
/*      */         //   Java source line #1929	-> byte code offset #7
/*      */         //   Java source line #1930	-> byte code offset #19
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	24	0	this	SynchronizedDeque<E>
/*      */         //   5	16	1	Ljava/lang/Object;	Object
/*      */         //   19	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	18	19	finally
/*      */         //   19	22	19	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public E peekLast()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedDeque:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedDeque:delegate	()Ljava/util/Deque;
/*      */         //   11: invokeinterface 17 1 0
/*      */         //   16: aload_1
/*      */         //   17: monitorexit
/*      */         //   18: areturn
/*      */         //   19: astore_2
/*      */         //   20: aload_1
/*      */         //   21: monitorexit
/*      */         //   22: aload_2
/*      */         //   23: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1935	-> byte code offset #0
/*      */         //   Java source line #1936	-> byte code offset #7
/*      */         //   Java source line #1937	-> byte code offset #19
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	24	0	this	SynchronizedDeque<E>
/*      */         //   5	16	1	Ljava/lang/Object;	Object
/*      */         //   19	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	18	19	finally
/*      */         //   19	22	19	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public boolean removeFirstOccurrence(Object o)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedDeque:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedDeque:delegate	()Ljava/util/Deque;
/*      */         //   11: aload_1
/*      */         //   12: invokeinterface 18 2 0
/*      */         //   17: aload_2
/*      */         //   18: monitorexit
/*      */         //   19: ireturn
/*      */         //   20: astore_3
/*      */         //   21: aload_2
/*      */         //   22: monitorexit
/*      */         //   23: aload_3
/*      */         //   24: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1942	-> byte code offset #0
/*      */         //   Java source line #1943	-> byte code offset #7
/*      */         //   Java source line #1944	-> byte code offset #20
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	25	0	this	SynchronizedDeque<E>
/*      */         //   0	25	1	o	Object
/*      */         //   5	17	2	Ljava/lang/Object;	Object
/*      */         //   20	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	19	20	finally
/*      */         //   20	23	20	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public boolean removeLastOccurrence(Object o)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedDeque:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedDeque:delegate	()Ljava/util/Deque;
/*      */         //   11: aload_1
/*      */         //   12: invokeinterface 19 2 0
/*      */         //   17: aload_2
/*      */         //   18: monitorexit
/*      */         //   19: ireturn
/*      */         //   20: astore_3
/*      */         //   21: aload_2
/*      */         //   22: monitorexit
/*      */         //   23: aload_3
/*      */         //   24: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1949	-> byte code offset #0
/*      */         //   Java source line #1950	-> byte code offset #7
/*      */         //   Java source line #1951	-> byte code offset #20
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	25	0	this	SynchronizedDeque<E>
/*      */         //   0	25	1	o	Object
/*      */         //   5	17	2	Ljava/lang/Object;	Object
/*      */         //   20	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	19	20	finally
/*      */         //   20	23	20	finally
/*      */       }
/*      */       
/*      */       public void push(E e)
/*      */       {
/* 1956 */         synchronized (this.mutex) {
/* 1957 */           delegate().push(e);
/*      */         }
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public E pop()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedDeque:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedDeque:delegate	()Ljava/util/Deque;
/*      */         //   11: invokeinterface 21 1 0
/*      */         //   16: aload_1
/*      */         //   17: monitorexit
/*      */         //   18: areturn
/*      */         //   19: astore_2
/*      */         //   20: aload_1
/*      */         //   21: monitorexit
/*      */         //   22: aload_2
/*      */         //   23: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1963	-> byte code offset #0
/*      */         //   Java source line #1964	-> byte code offset #7
/*      */         //   Java source line #1965	-> byte code offset #19
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	24	0	this	SynchronizedDeque<E>
/*      */         //   5	16	1	Ljava/lang/Object;	Object
/*      */         //   19	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	18	19	finally
/*      */         //   19	22	19	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public Iterator<E> descendingIterator()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedDeque:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedDeque:delegate	()Ljava/util/Deque;
/*      */         //   11: invokeinterface 22 1 0
/*      */         //   16: aload_1
/*      */         //   17: monitorexit
/*      */         //   18: areturn
/*      */         //   19: astore_2
/*      */         //   20: aload_1
/*      */         //   21: monitorexit
/*      */         //   22: aload_2
/*      */         //   23: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1970	-> byte code offset #0
/*      */         //   Java source line #1971	-> byte code offset #7
/*      */         //   Java source line #1972	-> byte code offset #19
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	24	0	this	SynchronizedDeque<E>
/*      */         //   5	16	1	Ljava/lang/Object;	Object
/*      */         //   19	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	18	19	finally
/*      */         //   19	22	19	finally
/*      */       }
/*      */     }
/*      */     
/*      */     static <R, C, V> Table<R, C, V> table(Table<R, C, V> table, Object mutex)
/*      */     {
/* 1979 */       return new SynchronizedTable(table, mutex);
/*      */     }
/*      */     
/*      */     private static final class SynchronizedTable<R, C, V> extends Synchronized.SynchronizedObject implements Table<R, C, V>
/*      */     {
/*      */       SynchronizedTable(Table<R, C, V> delegate, Object mutex)
/*      */       {
/* 1986 */         super(mutex);
/*      */       }
/*      */       
/*      */ 
/*      */       Table<R, C, V> delegate()
/*      */       {
/* 1992 */         return (Table)super.delegate();
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedTable:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_3
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedTable:delegate	()Lcom/google/common/collect/Table;
/*      */         //   11: aload_1
/*      */         //   12: aload_2
/*      */         //   13: invokeinterface 6 3 0
/*      */         //   18: aload_3
/*      */         //   19: monitorexit
/*      */         //   20: ireturn
/*      */         //   21: astore 4
/*      */         //   23: aload_3
/*      */         //   24: monitorexit
/*      */         //   25: aload 4
/*      */         //   27: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1997	-> byte code offset #0
/*      */         //   Java source line #1998	-> byte code offset #7
/*      */         //   Java source line #1999	-> byte code offset #21
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	28	0	this	SynchronizedTable<R, C, V>
/*      */         //   0	28	1	rowKey	Object
/*      */         //   0	28	2	columnKey	Object
/*      */         //   5	19	3	Ljava/lang/Object;	Object
/*      */         //   21	5	4	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	20	21	finally
/*      */         //   21	25	21	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public boolean containsRow(@Nullable Object rowKey)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedTable:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedTable:delegate	()Lcom/google/common/collect/Table;
/*      */         //   11: aload_1
/*      */         //   12: invokeinterface 7 2 0
/*      */         //   17: aload_2
/*      */         //   18: monitorexit
/*      */         //   19: ireturn
/*      */         //   20: astore_3
/*      */         //   21: aload_2
/*      */         //   22: monitorexit
/*      */         //   23: aload_3
/*      */         //   24: athrow
/*      */         // Line number table:
/*      */         //   Java source line #2004	-> byte code offset #0
/*      */         //   Java source line #2005	-> byte code offset #7
/*      */         //   Java source line #2006	-> byte code offset #20
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	25	0	this	SynchronizedTable<R, C, V>
/*      */         //   0	25	1	rowKey	Object
/*      */         //   5	17	2	Ljava/lang/Object;	Object
/*      */         //   20	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	19	20	finally
/*      */         //   20	23	20	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public boolean containsColumn(@Nullable Object columnKey)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedTable:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedTable:delegate	()Lcom/google/common/collect/Table;
/*      */         //   11: aload_1
/*      */         //   12: invokeinterface 8 2 0
/*      */         //   17: aload_2
/*      */         //   18: monitorexit
/*      */         //   19: ireturn
/*      */         //   20: astore_3
/*      */         //   21: aload_2
/*      */         //   22: monitorexit
/*      */         //   23: aload_3
/*      */         //   24: athrow
/*      */         // Line number table:
/*      */         //   Java source line #2011	-> byte code offset #0
/*      */         //   Java source line #2012	-> byte code offset #7
/*      */         //   Java source line #2013	-> byte code offset #20
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	25	0	this	SynchronizedTable<R, C, V>
/*      */         //   0	25	1	columnKey	Object
/*      */         //   5	17	2	Ljava/lang/Object;	Object
/*      */         //   20	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	19	20	finally
/*      */         //   20	23	20	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public boolean containsValue(@Nullable Object value)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedTable:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedTable:delegate	()Lcom/google/common/collect/Table;
/*      */         //   11: aload_1
/*      */         //   12: invokeinterface 9 2 0
/*      */         //   17: aload_2
/*      */         //   18: monitorexit
/*      */         //   19: ireturn
/*      */         //   20: astore_3
/*      */         //   21: aload_2
/*      */         //   22: monitorexit
/*      */         //   23: aload_3
/*      */         //   24: athrow
/*      */         // Line number table:
/*      */         //   Java source line #2018	-> byte code offset #0
/*      */         //   Java source line #2019	-> byte code offset #7
/*      */         //   Java source line #2020	-> byte code offset #20
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	25	0	this	SynchronizedTable<R, C, V>
/*      */         //   0	25	1	value	Object
/*      */         //   5	17	2	Ljava/lang/Object;	Object
/*      */         //   20	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	19	20	finally
/*      */         //   20	23	20	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public V get(@Nullable Object rowKey, @Nullable Object columnKey)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedTable:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_3
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedTable:delegate	()Lcom/google/common/collect/Table;
/*      */         //   11: aload_1
/*      */         //   12: aload_2
/*      */         //   13: invokeinterface 10 3 0
/*      */         //   18: aload_3
/*      */         //   19: monitorexit
/*      */         //   20: areturn
/*      */         //   21: astore 4
/*      */         //   23: aload_3
/*      */         //   24: monitorexit
/*      */         //   25: aload 4
/*      */         //   27: athrow
/*      */         // Line number table:
/*      */         //   Java source line #2025	-> byte code offset #0
/*      */         //   Java source line #2026	-> byte code offset #7
/*      */         //   Java source line #2027	-> byte code offset #21
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	28	0	this	SynchronizedTable<R, C, V>
/*      */         //   0	28	1	rowKey	Object
/*      */         //   0	28	2	columnKey	Object
/*      */         //   5	19	3	Ljava/lang/Object;	Object
/*      */         //   21	5	4	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	20	21	finally
/*      */         //   21	25	21	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public boolean isEmpty()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedTable:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedTable:delegate	()Lcom/google/common/collect/Table;
/*      */         //   11: invokeinterface 11 1 0
/*      */         //   16: aload_1
/*      */         //   17: monitorexit
/*      */         //   18: ireturn
/*      */         //   19: astore_2
/*      */         //   20: aload_1
/*      */         //   21: monitorexit
/*      */         //   22: aload_2
/*      */         //   23: athrow
/*      */         // Line number table:
/*      */         //   Java source line #2032	-> byte code offset #0
/*      */         //   Java source line #2033	-> byte code offset #7
/*      */         //   Java source line #2034	-> byte code offset #19
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	24	0	this	SynchronizedTable<R, C, V>
/*      */         //   5	16	1	Ljava/lang/Object;	Object
/*      */         //   19	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	18	19	finally
/*      */         //   19	22	19	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public int size()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedTable:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedTable:delegate	()Lcom/google/common/collect/Table;
/*      */         //   11: invokeinterface 12 1 0
/*      */         //   16: aload_1
/*      */         //   17: monitorexit
/*      */         //   18: ireturn
/*      */         //   19: astore_2
/*      */         //   20: aload_1
/*      */         //   21: monitorexit
/*      */         //   22: aload_2
/*      */         //   23: athrow
/*      */         // Line number table:
/*      */         //   Java source line #2039	-> byte code offset #0
/*      */         //   Java source line #2040	-> byte code offset #7
/*      */         //   Java source line #2041	-> byte code offset #19
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	24	0	this	SynchronizedTable<R, C, V>
/*      */         //   5	16	1	Ljava/lang/Object;	Object
/*      */         //   19	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	18	19	finally
/*      */         //   19	22	19	finally
/*      */       }
/*      */       
/*      */       public void clear()
/*      */       {
/* 2046 */         synchronized (this.mutex) {
/* 2047 */           delegate().clear();
/*      */         }
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public V put(@Nullable R rowKey, @Nullable C columnKey, @Nullable V value)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedTable:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore 4
/*      */         //   7: monitorenter
/*      */         //   8: aload_0
/*      */         //   9: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedTable:delegate	()Lcom/google/common/collect/Table;
/*      */         //   12: aload_1
/*      */         //   13: aload_2
/*      */         //   14: aload_3
/*      */         //   15: invokeinterface 14 4 0
/*      */         //   20: aload 4
/*      */         //   22: monitorexit
/*      */         //   23: areturn
/*      */         //   24: astore 5
/*      */         //   26: aload 4
/*      */         //   28: monitorexit
/*      */         //   29: aload 5
/*      */         //   31: athrow
/*      */         // Line number table:
/*      */         //   Java source line #2053	-> byte code offset #0
/*      */         //   Java source line #2054	-> byte code offset #8
/*      */         //   Java source line #2055	-> byte code offset #24
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	32	0	this	SynchronizedTable<R, C, V>
/*      */         //   0	32	1	rowKey	R
/*      */         //   0	32	2	columnKey	C
/*      */         //   0	32	3	value	V
/*      */         //   5	22	4	Ljava/lang/Object;	Object
/*      */         //   24	6	5	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   8	23	24	finally
/*      */         //   24	29	24	finally
/*      */       }
/*      */       
/*      */       public void putAll(Table<? extends R, ? extends C, ? extends V> table)
/*      */       {
/* 2060 */         synchronized (this.mutex) {
/* 2061 */           delegate().putAll(table);
/*      */         }
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public V remove(@Nullable Object rowKey, @Nullable Object columnKey)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedTable:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_3
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedTable:delegate	()Lcom/google/common/collect/Table;
/*      */         //   11: aload_1
/*      */         //   12: aload_2
/*      */         //   13: invokeinterface 16 3 0
/*      */         //   18: aload_3
/*      */         //   19: monitorexit
/*      */         //   20: areturn
/*      */         //   21: astore 4
/*      */         //   23: aload_3
/*      */         //   24: monitorexit
/*      */         //   25: aload 4
/*      */         //   27: athrow
/*      */         // Line number table:
/*      */         //   Java source line #2067	-> byte code offset #0
/*      */         //   Java source line #2068	-> byte code offset #7
/*      */         //   Java source line #2069	-> byte code offset #21
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	28	0	this	SynchronizedTable<R, C, V>
/*      */         //   0	28	1	rowKey	Object
/*      */         //   0	28	2	columnKey	Object
/*      */         //   5	19	3	Ljava/lang/Object;	Object
/*      */         //   21	5	4	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	20	21	finally
/*      */         //   21	25	21	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public Map<C, V> row(@Nullable R rowKey)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedTable:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedTable:delegate	()Lcom/google/common/collect/Table;
/*      */         //   11: aload_1
/*      */         //   12: invokeinterface 17 2 0
/*      */         //   17: aload_0
/*      */         //   18: getfield 4	com/google/common/collect/Synchronized$SynchronizedTable:mutex	Ljava/lang/Object;
/*      */         //   21: invokestatic 18	com/google/common/collect/Synchronized:map	(Ljava/util/Map;Ljava/lang/Object;)Ljava/util/Map;
/*      */         //   24: aload_2
/*      */         //   25: monitorexit
/*      */         //   26: areturn
/*      */         //   27: astore_3
/*      */         //   28: aload_2
/*      */         //   29: monitorexit
/*      */         //   30: aload_3
/*      */         //   31: athrow
/*      */         // Line number table:
/*      */         //   Java source line #2074	-> byte code offset #0
/*      */         //   Java source line #2075	-> byte code offset #7
/*      */         //   Java source line #2076	-> byte code offset #27
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	32	0	this	SynchronizedTable<R, C, V>
/*      */         //   0	32	1	rowKey	R
/*      */         //   5	24	2	Ljava/lang/Object;	Object
/*      */         //   27	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	26	27	finally
/*      */         //   27	30	27	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public Map<R, V> column(@Nullable C columnKey)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedTable:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_2
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedTable:delegate	()Lcom/google/common/collect/Table;
/*      */         //   11: aload_1
/*      */         //   12: invokeinterface 19 2 0
/*      */         //   17: aload_0
/*      */         //   18: getfield 4	com/google/common/collect/Synchronized$SynchronizedTable:mutex	Ljava/lang/Object;
/*      */         //   21: invokestatic 18	com/google/common/collect/Synchronized:map	(Ljava/util/Map;Ljava/lang/Object;)Ljava/util/Map;
/*      */         //   24: aload_2
/*      */         //   25: monitorexit
/*      */         //   26: areturn
/*      */         //   27: astore_3
/*      */         //   28: aload_2
/*      */         //   29: monitorexit
/*      */         //   30: aload_3
/*      */         //   31: athrow
/*      */         // Line number table:
/*      */         //   Java source line #2081	-> byte code offset #0
/*      */         //   Java source line #2082	-> byte code offset #7
/*      */         //   Java source line #2083	-> byte code offset #27
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	32	0	this	SynchronizedTable<R, C, V>
/*      */         //   0	32	1	columnKey	C
/*      */         //   5	24	2	Ljava/lang/Object;	Object
/*      */         //   27	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	26	27	finally
/*      */         //   27	30	27	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public Set<Table.Cell<R, C, V>> cellSet()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedTable:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedTable:delegate	()Lcom/google/common/collect/Table;
/*      */         //   11: invokeinterface 20 1 0
/*      */         //   16: aload_0
/*      */         //   17: getfield 4	com/google/common/collect/Synchronized$SynchronizedTable:mutex	Ljava/lang/Object;
/*      */         //   20: invokestatic 21	com/google/common/collect/Synchronized:set	(Ljava/util/Set;Ljava/lang/Object;)Ljava/util/Set;
/*      */         //   23: aload_1
/*      */         //   24: monitorexit
/*      */         //   25: areturn
/*      */         //   26: astore_2
/*      */         //   27: aload_1
/*      */         //   28: monitorexit
/*      */         //   29: aload_2
/*      */         //   30: athrow
/*      */         // Line number table:
/*      */         //   Java source line #2088	-> byte code offset #0
/*      */         //   Java source line #2089	-> byte code offset #7
/*      */         //   Java source line #2090	-> byte code offset #26
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	31	0	this	SynchronizedTable<R, C, V>
/*      */         //   5	23	1	Ljava/lang/Object;	Object
/*      */         //   26	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	25	26	finally
/*      */         //   26	29	26	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public Set<R> rowKeySet()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedTable:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedTable:delegate	()Lcom/google/common/collect/Table;
/*      */         //   11: invokeinterface 22 1 0
/*      */         //   16: aload_0
/*      */         //   17: getfield 4	com/google/common/collect/Synchronized$SynchronizedTable:mutex	Ljava/lang/Object;
/*      */         //   20: invokestatic 21	com/google/common/collect/Synchronized:set	(Ljava/util/Set;Ljava/lang/Object;)Ljava/util/Set;
/*      */         //   23: aload_1
/*      */         //   24: monitorexit
/*      */         //   25: areturn
/*      */         //   26: astore_2
/*      */         //   27: aload_1
/*      */         //   28: monitorexit
/*      */         //   29: aload_2
/*      */         //   30: athrow
/*      */         // Line number table:
/*      */         //   Java source line #2095	-> byte code offset #0
/*      */         //   Java source line #2096	-> byte code offset #7
/*      */         //   Java source line #2097	-> byte code offset #26
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	31	0	this	SynchronizedTable<R, C, V>
/*      */         //   5	23	1	Ljava/lang/Object;	Object
/*      */         //   26	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	25	26	finally
/*      */         //   26	29	26	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public Set<C> columnKeySet()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedTable:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedTable:delegate	()Lcom/google/common/collect/Table;
/*      */         //   11: invokeinterface 23 1 0
/*      */         //   16: aload_0
/*      */         //   17: getfield 4	com/google/common/collect/Synchronized$SynchronizedTable:mutex	Ljava/lang/Object;
/*      */         //   20: invokestatic 21	com/google/common/collect/Synchronized:set	(Ljava/util/Set;Ljava/lang/Object;)Ljava/util/Set;
/*      */         //   23: aload_1
/*      */         //   24: monitorexit
/*      */         //   25: areturn
/*      */         //   26: astore_2
/*      */         //   27: aload_1
/*      */         //   28: monitorexit
/*      */         //   29: aload_2
/*      */         //   30: athrow
/*      */         // Line number table:
/*      */         //   Java source line #2102	-> byte code offset #0
/*      */         //   Java source line #2103	-> byte code offset #7
/*      */         //   Java source line #2104	-> byte code offset #26
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	31	0	this	SynchronizedTable<R, C, V>
/*      */         //   5	23	1	Ljava/lang/Object;	Object
/*      */         //   26	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	25	26	finally
/*      */         //   26	29	26	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public Collection<V> values()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedTable:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedTable:delegate	()Lcom/google/common/collect/Table;
/*      */         //   11: invokeinterface 24 1 0
/*      */         //   16: aload_0
/*      */         //   17: getfield 4	com/google/common/collect/Synchronized$SynchronizedTable:mutex	Ljava/lang/Object;
/*      */         //   20: invokestatic 25	com/google/common/collect/Synchronized:access$500	(Ljava/util/Collection;Ljava/lang/Object;)Ljava/util/Collection;
/*      */         //   23: aload_1
/*      */         //   24: monitorexit
/*      */         //   25: areturn
/*      */         //   26: astore_2
/*      */         //   27: aload_1
/*      */         //   28: monitorexit
/*      */         //   29: aload_2
/*      */         //   30: athrow
/*      */         // Line number table:
/*      */         //   Java source line #2109	-> byte code offset #0
/*      */         //   Java source line #2110	-> byte code offset #7
/*      */         //   Java source line #2111	-> byte code offset #26
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	31	0	this	SynchronizedTable<R, C, V>
/*      */         //   5	23	1	Ljava/lang/Object;	Object
/*      */         //   26	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	25	26	finally
/*      */         //   26	29	26	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public Map<R, Map<C, V>> rowMap()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedTable:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedTable:delegate	()Lcom/google/common/collect/Table;
/*      */         //   11: invokeinterface 26 1 0
/*      */         //   16: new 27	com/google/common/collect/Synchronized$SynchronizedTable$1
/*      */         //   19: dup
/*      */         //   20: aload_0
/*      */         //   21: invokespecial 28	com/google/common/collect/Synchronized$SynchronizedTable$1:<init>	(Lcom/google/common/collect/Synchronized$SynchronizedTable;)V
/*      */         //   24: invokestatic 29	com/google/common/collect/Maps:transformValues	(Ljava/util/Map;Lcom/google/common/base/Function;)Ljava/util/Map;
/*      */         //   27: aload_0
/*      */         //   28: getfield 4	com/google/common/collect/Synchronized$SynchronizedTable:mutex	Ljava/lang/Object;
/*      */         //   31: invokestatic 18	com/google/common/collect/Synchronized:map	(Ljava/util/Map;Ljava/lang/Object;)Ljava/util/Map;
/*      */         //   34: aload_1
/*      */         //   35: monitorexit
/*      */         //   36: areturn
/*      */         //   37: astore_2
/*      */         //   38: aload_1
/*      */         //   39: monitorexit
/*      */         //   40: aload_2
/*      */         //   41: athrow
/*      */         // Line number table:
/*      */         //   Java source line #2116	-> byte code offset #0
/*      */         //   Java source line #2117	-> byte code offset #7
/*      */         //   Java source line #2119	-> byte code offset #8
/*      */         //   Java source line #2118	-> byte code offset #24
/*      */         //   Java source line #2117	-> byte code offset #31
/*      */         //   Java source line #2127	-> byte code offset #37
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	42	0	this	SynchronizedTable<R, C, V>
/*      */         //   5	34	1	Ljava/lang/Object;	Object
/*      */         //   37	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	36	37	finally
/*      */         //   37	40	37	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public Map<C, Map<R, V>> columnMap()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedTable:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedTable:delegate	()Lcom/google/common/collect/Table;
/*      */         //   11: invokeinterface 30 1 0
/*      */         //   16: new 31	com/google/common/collect/Synchronized$SynchronizedTable$2
/*      */         //   19: dup
/*      */         //   20: aload_0
/*      */         //   21: invokespecial 32	com/google/common/collect/Synchronized$SynchronizedTable$2:<init>	(Lcom/google/common/collect/Synchronized$SynchronizedTable;)V
/*      */         //   24: invokestatic 29	com/google/common/collect/Maps:transformValues	(Ljava/util/Map;Lcom/google/common/base/Function;)Ljava/util/Map;
/*      */         //   27: aload_0
/*      */         //   28: getfield 4	com/google/common/collect/Synchronized$SynchronizedTable:mutex	Ljava/lang/Object;
/*      */         //   31: invokestatic 18	com/google/common/collect/Synchronized:map	(Ljava/util/Map;Ljava/lang/Object;)Ljava/util/Map;
/*      */         //   34: aload_1
/*      */         //   35: monitorexit
/*      */         //   36: areturn
/*      */         //   37: astore_2
/*      */         //   38: aload_1
/*      */         //   39: monitorexit
/*      */         //   40: aload_2
/*      */         //   41: athrow
/*      */         // Line number table:
/*      */         //   Java source line #2132	-> byte code offset #0
/*      */         //   Java source line #2133	-> byte code offset #7
/*      */         //   Java source line #2135	-> byte code offset #8
/*      */         //   Java source line #2134	-> byte code offset #24
/*      */         //   Java source line #2133	-> byte code offset #31
/*      */         //   Java source line #2143	-> byte code offset #37
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	42	0	this	SynchronizedTable<R, C, V>
/*      */         //   5	34	1	Ljava/lang/Object;	Object
/*      */         //   37	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	36	37	finally
/*      */         //   37	40	37	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public int hashCode()
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 4	com/google/common/collect/Synchronized$SynchronizedTable:mutex	Ljava/lang/Object;
/*      */         //   4: dup
/*      */         //   5: astore_1
/*      */         //   6: monitorenter
/*      */         //   7: aload_0
/*      */         //   8: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedTable:delegate	()Lcom/google/common/collect/Table;
/*      */         //   11: invokeinterface 33 1 0
/*      */         //   16: aload_1
/*      */         //   17: monitorexit
/*      */         //   18: ireturn
/*      */         //   19: astore_2
/*      */         //   20: aload_1
/*      */         //   21: monitorexit
/*      */         //   22: aload_2
/*      */         //   23: athrow
/*      */         // Line number table:
/*      */         //   Java source line #2148	-> byte code offset #0
/*      */         //   Java source line #2149	-> byte code offset #7
/*      */         //   Java source line #2150	-> byte code offset #19
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	24	0	this	SynchronizedTable<R, C, V>
/*      */         //   5	16	1	Ljava/lang/Object;	Object
/*      */         //   19	4	2	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   7	18	19	finally
/*      */         //   19	22	19	finally
/*      */       }
/*      */       
/*      */       /* Error */
/*      */       public boolean equals(@Nullable Object obj)
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: aload_1
/*      */         //   2: if_acmpne +5 -> 7
/*      */         //   5: iconst_1
/*      */         //   6: ireturn
/*      */         //   7: aload_0
/*      */         //   8: getfield 4	com/google/common/collect/Synchronized$SynchronizedTable:mutex	Ljava/lang/Object;
/*      */         //   11: dup
/*      */         //   12: astore_2
/*      */         //   13: monitorenter
/*      */         //   14: aload_0
/*      */         //   15: invokevirtual 5	com/google/common/collect/Synchronized$SynchronizedTable:delegate	()Lcom/google/common/collect/Table;
/*      */         //   18: aload_1
/*      */         //   19: invokeinterface 34 2 0
/*      */         //   24: aload_2
/*      */         //   25: monitorexit
/*      */         //   26: ireturn
/*      */         //   27: astore_3
/*      */         //   28: aload_2
/*      */         //   29: monitorexit
/*      */         //   30: aload_3
/*      */         //   31: athrow
/*      */         // Line number table:
/*      */         //   Java source line #2155	-> byte code offset #0
/*      */         //   Java source line #2156	-> byte code offset #5
/*      */         //   Java source line #2158	-> byte code offset #7
/*      */         //   Java source line #2159	-> byte code offset #14
/*      */         //   Java source line #2160	-> byte code offset #27
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	32	0	this	SynchronizedTable<R, C, V>
/*      */         //   0	32	1	obj	Object
/*      */         //   12	17	2	Ljava/lang/Object;	Object
/*      */         //   27	4	3	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   14	26	27	finally
/*      */         //   27	30	27	finally
/*      */       }
/*      */     }
/*      */   }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\Synchronized.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */