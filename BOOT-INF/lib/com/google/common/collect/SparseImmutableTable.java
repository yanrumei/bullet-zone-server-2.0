/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.annotation.concurrent.Immutable;
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
/*     */ @Immutable
/*     */ final class SparseImmutableTable<R, C, V>
/*     */   extends RegularImmutableTable<R, C, V>
/*     */ {
/*  28 */   static final ImmutableTable<Object, Object, Object> EMPTY = new SparseImmutableTable(
/*     */   
/*  30 */     ImmutableList.of(), ImmutableSet.of(), ImmutableSet.of());
/*     */   
/*     */ 
/*     */   private final ImmutableMap<R, Map<C, V>> rowMap;
/*     */   
/*     */   private final ImmutableMap<C, Map<R, V>> columnMap;
/*     */   
/*     */   private final int[] cellRowIndices;
/*     */   
/*     */   private final int[] cellColumnInRowIndices;
/*     */   
/*     */ 
/*     */   SparseImmutableTable(ImmutableList<Table.Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace)
/*     */   {
/*  44 */     Map<R, Integer> rowIndex = Maps.indexMap(rowSpace);
/*  45 */     Map<R, Map<C, V>> rows = Maps.newLinkedHashMap();
/*  46 */     for (UnmodifiableIterator localUnmodifiableIterator = rowSpace.iterator(); localUnmodifiableIterator.hasNext();) { row = localUnmodifiableIterator.next();
/*  47 */       rows.put(row, new LinkedHashMap());
/*     */     }
/*  49 */     Object columns = Maps.newLinkedHashMap();
/*  50 */     for (R row = columnSpace.iterator(); row.hasNext();) { C col = row.next();
/*  51 */       ((Map)columns).put(col, new LinkedHashMap());
/*     */     }
/*  53 */     int[] cellRowIndices = new int[cellList.size()];
/*  54 */     int[] cellColumnInRowIndices = new int[cellList.size()];
/*  55 */     for (int i = 0; i < cellList.size(); i++) {
/*  56 */       cell = (Table.Cell)cellList.get(i);
/*  57 */       R rowKey = cell.getRowKey();
/*  58 */       C columnKey = cell.getColumnKey();
/*  59 */       V value = cell.getValue();
/*     */       
/*  61 */       cellRowIndices[i] = ((Integer)rowIndex.get(rowKey)).intValue();
/*  62 */       Map<C, V> thisRow = (Map)rows.get(rowKey);
/*  63 */       cellColumnInRowIndices[i] = thisRow.size();
/*  64 */       V oldValue = thisRow.put(columnKey, value);
/*  65 */       if (oldValue != null) {
/*  66 */         throw new IllegalArgumentException("Duplicate value for row=" + rowKey + ", column=" + columnKey + ": " + value + ", " + oldValue);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  76 */       ((Map)((Map)columns).get(columnKey)).put(rowKey, value);
/*     */     }
/*  78 */     this.cellRowIndices = cellRowIndices;
/*  79 */     this.cellColumnInRowIndices = cellColumnInRowIndices;
/*     */     
/*  81 */     ImmutableMap.Builder<R, Map<C, V>> rowBuilder = new ImmutableMap.Builder(rows.size());
/*  82 */     for (Table.Cell<R, C, V> cell = rows.entrySet().iterator(); cell.hasNext();) { row = (Map.Entry)cell.next();
/*  83 */       rowBuilder.put(row.getKey(), ImmutableMap.copyOf((Map)row.getValue())); }
/*     */     Map.Entry<R, Map<C, V>> row;
/*  85 */     this.rowMap = rowBuilder.build();
/*     */     
/*     */ 
/*  88 */     ImmutableMap.Builder<C, Map<R, V>> columnBuilder = new ImmutableMap.Builder(((Map)columns).size());
/*  89 */     for (Map.Entry<C, Map<R, V>> col : ((Map)columns).entrySet()) {
/*  90 */       columnBuilder.put(col.getKey(), ImmutableMap.copyOf((Map)col.getValue()));
/*     */     }
/*  92 */     this.columnMap = columnBuilder.build();
/*     */   }
/*     */   
/*     */   public ImmutableMap<C, Map<R, V>> columnMap()
/*     */   {
/*  97 */     return this.columnMap;
/*     */   }
/*     */   
/*     */   public ImmutableMap<R, Map<C, V>> rowMap()
/*     */   {
/* 102 */     return this.rowMap;
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/* 107 */     return this.cellRowIndices.length;
/*     */   }
/*     */   
/*     */   Table.Cell<R, C, V> getCell(int index)
/*     */   {
/* 112 */     int rowIndex = this.cellRowIndices[index];
/* 113 */     Map.Entry<R, Map<C, V>> rowEntry = (Map.Entry)this.rowMap.entrySet().asList().get(rowIndex);
/* 114 */     ImmutableMap<C, V> row = (ImmutableMap)rowEntry.getValue();
/* 115 */     int columnIndex = this.cellColumnInRowIndices[index];
/* 116 */     Map.Entry<C, V> colEntry = (Map.Entry)row.entrySet().asList().get(columnIndex);
/* 117 */     return cellOf(rowEntry.getKey(), colEntry.getKey(), colEntry.getValue());
/*     */   }
/*     */   
/*     */   V getValue(int index)
/*     */   {
/* 122 */     int rowIndex = this.cellRowIndices[index];
/* 123 */     ImmutableMap<C, V> row = (ImmutableMap)this.rowMap.values().asList().get(rowIndex);
/* 124 */     int columnIndex = this.cellColumnInRowIndices[index];
/* 125 */     return (V)row.values().asList().get(columnIndex);
/*     */   }
/*     */   
/*     */   ImmutableTable.SerializedForm createSerializedForm()
/*     */   {
/* 130 */     Map<C, Integer> columnKeyToIndex = Maps.indexMap(columnKeySet());
/* 131 */     int[] cellColumnIndices = new int[cellSet().size()];
/* 132 */     int i = 0;
/* 133 */     for (UnmodifiableIterator localUnmodifiableIterator = cellSet().iterator(); localUnmodifiableIterator.hasNext();) { Table.Cell<R, C, V> cell = (Table.Cell)localUnmodifiableIterator.next();
/* 134 */       cellColumnIndices[(i++)] = ((Integer)columnKeyToIndex.get(cell.getColumnKey())).intValue();
/*     */     }
/* 136 */     return ImmutableTable.SerializedForm.create(this, this.cellRowIndices, cellColumnIndices);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\SparseImmutableTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */