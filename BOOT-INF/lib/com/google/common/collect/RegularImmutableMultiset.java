/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.util.Collection;
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
/*     */ @GwtCompatible(serializable=true)
/*     */ class RegularImmutableMultiset<E>
/*     */   extends ImmutableMultiset<E>
/*     */ {
/*  37 */   static final RegularImmutableMultiset<Object> EMPTY = new RegularImmutableMultiset(
/*  38 */     ImmutableList.of());
/*     */   
/*     */   private final transient Multisets.ImmutableEntry<E>[] entries;
/*     */   private final transient Multisets.ImmutableEntry<E>[] hashTable;
/*     */   private final transient int size;
/*     */   private final transient int hashCode;
/*     */   @LazyInit
/*     */   private transient ImmutableSet<E> elementSet;
/*     */   
/*     */   RegularImmutableMultiset(Collection<? extends Multiset.Entry<? extends E>> entries)
/*     */   {
/*  49 */     int distinct = entries.size();
/*     */     
/*  51 */     Multisets.ImmutableEntry<E>[] entryArray = new Multisets.ImmutableEntry[distinct];
/*  52 */     if (distinct == 0) {
/*  53 */       this.entries = entryArray;
/*  54 */       this.hashTable = null;
/*  55 */       this.size = 0;
/*  56 */       this.hashCode = 0;
/*  57 */       this.elementSet = ImmutableSet.of();
/*     */     } else {
/*  59 */       int tableSize = Hashing.closedTableSize(distinct, 1.0D);
/*  60 */       int mask = tableSize - 1;
/*     */       
/*  62 */       Multisets.ImmutableEntry<E>[] hashTable = new Multisets.ImmutableEntry[tableSize];
/*     */       
/*  64 */       int index = 0;
/*  65 */       int hashCode = 0;
/*  66 */       long size = 0L;
/*  67 */       for (Multiset.Entry<? extends E> entry : entries) {
/*  68 */         E element = Preconditions.checkNotNull(entry.getElement());
/*  69 */         int count = entry.getCount();
/*  70 */         int hash = element.hashCode();
/*  71 */         int bucket = Hashing.smear(hash) & mask;
/*  72 */         Multisets.ImmutableEntry<E> bucketHead = hashTable[bucket];
/*     */         Multisets.ImmutableEntry<E> newEntry;
/*  74 */         Multisets.ImmutableEntry<E> newEntry; if (bucketHead == null) {
/*  75 */           boolean canReuseEntry = ((entry instanceof Multisets.ImmutableEntry)) && (!(entry instanceof NonTerminalEntry));
/*     */           
/*  77 */           newEntry = canReuseEntry ? (Multisets.ImmutableEntry)entry : new Multisets.ImmutableEntry(element, count);
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*  82 */           newEntry = new NonTerminalEntry(element, count, bucketHead);
/*     */         }
/*  84 */         hashCode += (hash ^ count);
/*  85 */         entryArray[(index++)] = newEntry;
/*  86 */         hashTable[bucket] = newEntry;
/*  87 */         size += count;
/*     */       }
/*  89 */       this.entries = entryArray;
/*  90 */       this.hashTable = hashTable;
/*  91 */       this.size = Ints.saturatedCast(size);
/*  92 */       this.hashCode = hashCode;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class NonTerminalEntry<E> extends Multisets.ImmutableEntry<E> {
/*     */     private final Multisets.ImmutableEntry<E> nextInBucket;
/*     */     
/*     */     NonTerminalEntry(E element, int count, Multisets.ImmutableEntry<E> nextInBucket) {
/* 100 */       super(count);
/* 101 */       this.nextInBucket = nextInBucket;
/*     */     }
/*     */     
/*     */     public Multisets.ImmutableEntry<E> nextInBucket()
/*     */     {
/* 106 */       return this.nextInBucket;
/*     */     }
/*     */   }
/*     */   
/*     */   boolean isPartialView()
/*     */   {
/* 112 */     return false;
/*     */   }
/*     */   
/*     */   public int count(@Nullable Object element)
/*     */   {
/* 117 */     Multisets.ImmutableEntry<E>[] hashTable = this.hashTable;
/* 118 */     if ((element == null) || (hashTable == null)) {
/* 119 */       return 0;
/*     */     }
/* 121 */     int hash = Hashing.smearedHash(element);
/* 122 */     int mask = hashTable.length - 1;
/* 123 */     for (Multisets.ImmutableEntry<E> entry = hashTable[(hash & mask)]; 
/* 124 */         entry != null; 
/* 125 */         entry = entry.nextInBucket()) {
/* 126 */       if (Objects.equal(element, entry.getElement())) {
/* 127 */         return entry.getCount();
/*     */       }
/*     */     }
/* 130 */     return 0;
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/* 135 */     return this.size;
/*     */   }
/*     */   
/*     */   public ImmutableSet<E> elementSet()
/*     */   {
/* 140 */     ImmutableSet<E> result = this.elementSet;
/* 141 */     return result == null ? (this.elementSet = new ElementSet(null)) : result;
/*     */   }
/*     */   
/*     */   private final class ElementSet extends ImmutableSet.Indexed<E>
/*     */   {
/*     */     private ElementSet() {}
/*     */     
/*     */     E get(int index) {
/* 149 */       return (E)RegularImmutableMultiset.this.entries[index].getElement();
/*     */     }
/*     */     
/*     */     public boolean contains(@Nullable Object object)
/*     */     {
/* 154 */       return RegularImmutableMultiset.this.contains(object);
/*     */     }
/*     */     
/*     */     boolean isPartialView()
/*     */     {
/* 159 */       return true;
/*     */     }
/*     */     
/*     */     public int size()
/*     */     {
/* 164 */       return RegularImmutableMultiset.this.entries.length;
/*     */     }
/*     */   }
/*     */   
/*     */   Multiset.Entry<E> getEntry(int index)
/*     */   {
/* 170 */     return this.entries[index];
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 175 */     return this.hashCode;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\RegularImmutableMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */