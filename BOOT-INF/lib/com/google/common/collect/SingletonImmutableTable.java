/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.Map;
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
/*    */ @GwtCompatible
/*    */ class SingletonImmutableTable<R, C, V>
/*    */   extends ImmutableTable<R, C, V>
/*    */ {
/*    */   final R singleRowKey;
/*    */   final C singleColumnKey;
/*    */   final V singleValue;
/*    */   
/*    */   SingletonImmutableTable(R rowKey, C columnKey, V value)
/*    */   {
/* 36 */     this.singleRowKey = Preconditions.checkNotNull(rowKey);
/* 37 */     this.singleColumnKey = Preconditions.checkNotNull(columnKey);
/* 38 */     this.singleValue = Preconditions.checkNotNull(value);
/*    */   }
/*    */   
/*    */   SingletonImmutableTable(Table.Cell<R, C, V> cell) {
/* 42 */     this(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
/*    */   }
/*    */   
/*    */   public ImmutableMap<R, V> column(C columnKey)
/*    */   {
/* 47 */     Preconditions.checkNotNull(columnKey);
/* 48 */     return containsColumn(columnKey) ? 
/* 49 */       ImmutableMap.of(this.singleRowKey, this.singleValue) : 
/* 50 */       ImmutableMap.of();
/*    */   }
/*    */   
/*    */   public ImmutableMap<C, Map<R, V>> columnMap()
/*    */   {
/* 55 */     return ImmutableMap.of(this.singleColumnKey, ImmutableMap.of(this.singleRowKey, this.singleValue));
/*    */   }
/*    */   
/*    */   public ImmutableMap<R, Map<C, V>> rowMap()
/*    */   {
/* 60 */     return ImmutableMap.of(this.singleRowKey, ImmutableMap.of(this.singleColumnKey, this.singleValue));
/*    */   }
/*    */   
/*    */   public int size()
/*    */   {
/* 65 */     return 1;
/*    */   }
/*    */   
/*    */   ImmutableSet<Table.Cell<R, C, V>> createCellSet()
/*    */   {
/* 70 */     return ImmutableSet.of(cellOf(this.singleRowKey, this.singleColumnKey, this.singleValue));
/*    */   }
/*    */   
/*    */   ImmutableCollection<V> createValues()
/*    */   {
/* 75 */     return ImmutableSet.of(this.singleValue);
/*    */   }
/*    */   
/*    */   ImmutableTable.SerializedForm createSerializedForm()
/*    */   {
/* 80 */     return ImmutableTable.SerializedForm.create(this, new int[] { 0 }, new int[] { 0 });
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\SingletonImmutableTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */