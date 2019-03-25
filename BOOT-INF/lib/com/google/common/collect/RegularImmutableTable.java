/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
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
/*     */ @GwtCompatible
/*     */ abstract class RegularImmutableTable<R, C, V>
/*     */   extends ImmutableTable<R, C, V>
/*     */ {
/*     */   abstract Table.Cell<R, C, V> getCell(int paramInt);
/*     */   
/*  44 */   final ImmutableSet<Table.Cell<R, C, V>> createCellSet() { return isEmpty() ? ImmutableSet.of() : new CellSet(null); }
/*     */   
/*     */   abstract V getValue(int paramInt);
/*     */   
/*     */   private final class CellSet extends ImmutableSet.Indexed<Table.Cell<R, C, V>> {
/*     */     private CellSet() {}
/*     */     
/*  51 */     public int size() { return RegularImmutableTable.this.size(); }
/*     */     
/*     */ 
/*     */     Table.Cell<R, C, V> get(int index)
/*     */     {
/*  56 */       return RegularImmutableTable.this.getCell(index);
/*     */     }
/*     */     
/*     */     public boolean contains(@Nullable Object object)
/*     */     {
/*  61 */       if ((object instanceof Table.Cell)) {
/*  62 */         Table.Cell<?, ?, ?> cell = (Table.Cell)object;
/*  63 */         Object value = RegularImmutableTable.this.get(cell.getRowKey(), cell.getColumnKey());
/*  64 */         return (value != null) && (value.equals(cell.getValue()));
/*     */       }
/*  66 */       return false;
/*     */     }
/*     */     
/*     */     boolean isPartialView()
/*     */     {
/*  71 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   final ImmutableCollection<V> createValues()
/*     */   {
/*  79 */     return isEmpty() ? ImmutableList.of() : new Values(null);
/*     */   }
/*     */   
/*     */   private final class Values extends ImmutableList<V> {
/*     */     private Values() {}
/*     */     
/*     */     public int size() {
/*  86 */       return RegularImmutableTable.this.size();
/*     */     }
/*     */     
/*     */     public V get(int index)
/*     */     {
/*  91 */       return (V)RegularImmutableTable.this.getValue(index);
/*     */     }
/*     */     
/*     */     boolean isPartialView()
/*     */     {
/*  96 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static <R, C, V> RegularImmutableTable<R, C, V> forCells(List<Table.Cell<R, C, V>> cells, @Nullable Comparator<? super R> rowComparator, @Nullable final Comparator<? super C> columnComparator)
/*     */   {
/* 104 */     Preconditions.checkNotNull(cells);
/* 105 */     if ((rowComparator != null) || (columnComparator != null))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 113 */       Comparator<Table.Cell<R, C, V>> comparator = new Comparator()
/*     */       {
/*     */ 
/*     */ 
/*     */         public int compare(Table.Cell<R, C, V> cell1, Table.Cell<R, C, V> cell2)
/*     */         {
/*     */ 
/* 120 */           int rowCompare = this.val$rowComparator == null ? 0 : this.val$rowComparator.compare(cell1.getRowKey(), cell2.getRowKey());
/* 121 */           if (rowCompare != 0) {
/* 122 */             return rowCompare;
/*     */           }
/* 124 */           return columnComparator == null ? 0 : columnComparator
/*     */           
/* 126 */             .compare(cell1.getColumnKey(), cell2.getColumnKey());
/*     */         }
/* 128 */       };
/* 129 */       Collections.sort(cells, comparator);
/*     */     }
/* 131 */     return forCellsInternal(cells, rowComparator, columnComparator);
/*     */   }
/*     */   
/*     */   static <R, C, V> RegularImmutableTable<R, C, V> forCells(Iterable<Table.Cell<R, C, V>> cells) {
/* 135 */     return forCellsInternal(cells, null, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static final <R, C, V> RegularImmutableTable<R, C, V> forCellsInternal(Iterable<Table.Cell<R, C, V>> cells, @Nullable Comparator<? super R> rowComparator, @Nullable Comparator<? super C> columnComparator)
/*     */   {
/* 142 */     Set<R> rowSpaceBuilder = new LinkedHashSet();
/* 143 */     Set<C> columnSpaceBuilder = new LinkedHashSet();
/* 144 */     ImmutableList<Table.Cell<R, C, V>> cellList = ImmutableList.copyOf(cells);
/* 145 */     for (Table.Cell<R, C, V> cell : cells) {
/* 146 */       rowSpaceBuilder.add(cell.getRowKey());
/* 147 */       columnSpaceBuilder.add(cell.getColumnKey());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 153 */     Object rowSpace = rowComparator == null ? ImmutableSet.copyOf(rowSpaceBuilder) : ImmutableSet.copyOf(ImmutableList.sortedCopyOf(rowComparator, rowSpaceBuilder));
/*     */     
/*     */ 
/*     */ 
/* 157 */     ImmutableSet<C> columnSpace = columnComparator == null ? ImmutableSet.copyOf(columnSpaceBuilder) : ImmutableSet.copyOf(ImmutableList.sortedCopyOf(columnComparator, columnSpaceBuilder));
/*     */     
/* 159 */     return forOrderedComponents(cellList, (ImmutableSet)rowSpace, columnSpace);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static <R, C, V> RegularImmutableTable<R, C, V> forOrderedComponents(ImmutableList<Table.Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace)
/*     */   {
/* 169 */     return cellList.size() > rowSpace.size() * columnSpace.size() / 2L ? new DenseImmutableTable(cellList, rowSpace, columnSpace) : new SparseImmutableTable(cellList, rowSpace, columnSpace);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\RegularImmutableTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */