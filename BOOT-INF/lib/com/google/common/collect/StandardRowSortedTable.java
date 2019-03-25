/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
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
/*     */ class StandardRowSortedTable<R, C, V>
/*     */   extends StandardTable<R, C, V>
/*     */   implements RowSortedTable<R, C, V>
/*     */ {
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   StandardRowSortedTable(SortedMap<R, Map<C, V>> backingMap, Supplier<? extends Map<C, V>> factory)
/*     */   {
/*  59 */     super(backingMap, factory);
/*     */   }
/*     */   
/*     */   private SortedMap<R, Map<C, V>> sortedBackingMap() {
/*  63 */     return (SortedMap)this.backingMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SortedSet<R> rowKeySet()
/*     */   {
/*  74 */     return (SortedSet)rowMap().keySet();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SortedMap<R, Map<C, V>> rowMap()
/*     */   {
/*  85 */     return (SortedMap)super.rowMap();
/*     */   }
/*     */   
/*     */   SortedMap<R, Map<C, V>> createRowMap()
/*     */   {
/*  90 */     return new RowSortedMap(null);
/*     */   }
/*     */   
/*     */   private class RowSortedMap extends StandardTable<R, C, V>.RowMap implements SortedMap<R, Map<C, V>> {
/*  94 */     private RowSortedMap() { super(); }
/*     */     
/*     */     public SortedSet<R> keySet() {
/*  97 */       return (SortedSet)super.keySet();
/*     */     }
/*     */     
/*     */     SortedSet<R> createKeySet()
/*     */     {
/* 102 */       return new Maps.SortedKeySet(this);
/*     */     }
/*     */     
/*     */     public Comparator<? super R> comparator()
/*     */     {
/* 107 */       return StandardRowSortedTable.this.sortedBackingMap().comparator();
/*     */     }
/*     */     
/*     */     public R firstKey()
/*     */     {
/* 112 */       return (R)StandardRowSortedTable.this.sortedBackingMap().firstKey();
/*     */     }
/*     */     
/*     */     public R lastKey()
/*     */     {
/* 117 */       return (R)StandardRowSortedTable.this.sortedBackingMap().lastKey();
/*     */     }
/*     */     
/*     */     public SortedMap<R, Map<C, V>> headMap(R toKey)
/*     */     {
/* 122 */       Preconditions.checkNotNull(toKey);
/* 123 */       return new StandardRowSortedTable(StandardRowSortedTable.this.sortedBackingMap().headMap(toKey), StandardRowSortedTable.this.factory)
/* 124 */         .rowMap();
/*     */     }
/*     */     
/*     */     public SortedMap<R, Map<C, V>> subMap(R fromKey, R toKey)
/*     */     {
/* 129 */       Preconditions.checkNotNull(fromKey);
/* 130 */       Preconditions.checkNotNull(toKey);
/* 131 */       return new StandardRowSortedTable(StandardRowSortedTable.this.sortedBackingMap().subMap(fromKey, toKey), StandardRowSortedTable.this.factory)
/* 132 */         .rowMap();
/*     */     }
/*     */     
/*     */     public SortedMap<R, Map<C, V>> tailMap(R fromKey)
/*     */     {
/* 137 */       Preconditions.checkNotNull(fromKey);
/* 138 */       return new StandardRowSortedTable(StandardRowSortedTable.this.sortedBackingMap().tailMap(fromKey), StandardRowSortedTable.this.factory)
/* 139 */         .rowMap();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\StandardRowSortedTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */