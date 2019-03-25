/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtIncompatible
/*     */ class ImmutableMapEntry<K, V>
/*     */   extends ImmutableEntry<K, V>
/*     */ {
/*     */   static <K, V> ImmutableMapEntry<K, V>[] createEntryArray(int size)
/*     */   {
/*  44 */     return new ImmutableMapEntry[size];
/*     */   }
/*     */   
/*     */   ImmutableMapEntry(K key, V value) {
/*  48 */     super(key, value);
/*  49 */     CollectPreconditions.checkEntryNotNull(key, value);
/*     */   }
/*     */   
/*     */   ImmutableMapEntry(ImmutableMapEntry<K, V> contents) {
/*  53 */     super(contents.getKey(), contents.getValue());
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   ImmutableMapEntry<K, V> getNextInKeyBucket()
/*     */   {
/*  59 */     return null;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   ImmutableMapEntry<K, V> getNextInValueBucket() {
/*  64 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean isReusable()
/*     */   {
/*  72 */     return true;
/*     */   }
/*     */   
/*     */   static class NonTerminalImmutableMapEntry<K, V> extends ImmutableMapEntry<K, V> {
/*     */     private final transient ImmutableMapEntry<K, V> nextInKeyBucket;
/*     */     
/*     */     NonTerminalImmutableMapEntry(K key, V value, ImmutableMapEntry<K, V> nextInKeyBucket) {
/*  79 */       super(value);
/*  80 */       this.nextInKeyBucket = nextInKeyBucket;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     final ImmutableMapEntry<K, V> getNextInKeyBucket()
/*     */     {
/*  86 */       return this.nextInKeyBucket;
/*     */     }
/*     */     
/*     */     final boolean isReusable()
/*     */     {
/*  91 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static final class NonTerminalImmutableBiMapEntry<K, V>
/*     */     extends ImmutableMapEntry.NonTerminalImmutableMapEntry<K, V>
/*     */   {
/*     */     private final transient ImmutableMapEntry<K, V> nextInValueBucket;
/*     */     
/*     */ 
/*     */     NonTerminalImmutableBiMapEntry(K key, V value, ImmutableMapEntry<K, V> nextInKeyBucket, ImmutableMapEntry<K, V> nextInValueBucket)
/*     */     {
/* 104 */       super(value, nextInKeyBucket);
/* 105 */       this.nextInValueBucket = nextInValueBucket;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     ImmutableMapEntry<K, V> getNextInValueBucket()
/*     */     {
/* 111 */       return this.nextInValueBucket;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\ImmutableMapEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */