/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Ascii;
/*     */ import com.google.common.base.Equivalence;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.MoreObjects.ToStringHelper;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
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
/*     */ @GwtCompatible(emulated=true)
/*     */ public final class MapMaker
/*     */ {
/*     */   private static final int DEFAULT_INITIAL_CAPACITY = 16;
/*     */   private static final int DEFAULT_CONCURRENCY_LEVEL = 4;
/*     */   static final int UNSET_INT = -1;
/*     */   boolean useCustomMap;
/*  96 */   int initialCapacity = -1;
/*  97 */   int concurrencyLevel = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   MapMakerInternalMap.Strength keyStrength;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   MapMakerInternalMap.Strength valueStrength;
/*     */   
/*     */ 
/*     */ 
/*     */   Equivalence<Object> keyEquivalence;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   MapMaker keyEquivalence(Equivalence<Object> equivalence)
/*     */   {
/* 120 */     Preconditions.checkState(this.keyEquivalence == null, "key equivalence was already set to %s", this.keyEquivalence);
/* 121 */     this.keyEquivalence = ((Equivalence)Preconditions.checkNotNull(equivalence));
/* 122 */     this.useCustomMap = true;
/* 123 */     return this;
/*     */   }
/*     */   
/*     */   Equivalence<Object> getKeyEquivalence() {
/* 127 */     return (Equivalence)MoreObjects.firstNonNull(this.keyEquivalence, getKeyStrength().defaultEquivalence());
/*     */   }
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
/*     */   @CanIgnoreReturnValue
/*     */   public MapMaker initialCapacity(int initialCapacity)
/*     */   {
/* 142 */     Preconditions.checkState(this.initialCapacity == -1, "initial capacity was already set to %s", this.initialCapacity);
/*     */     
/*     */ 
/*     */ 
/* 146 */     Preconditions.checkArgument(initialCapacity >= 0);
/* 147 */     this.initialCapacity = initialCapacity;
/* 148 */     return this;
/*     */   }
/*     */   
/*     */   int getInitialCapacity() {
/* 152 */     return this.initialCapacity == -1 ? 16 : this.initialCapacity;
/*     */   }
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
/*     */   @CanIgnoreReturnValue
/*     */   public MapMaker concurrencyLevel(int concurrencyLevel)
/*     */   {
/* 176 */     Preconditions.checkState(this.concurrencyLevel == -1, "concurrency level was already set to %s", this.concurrencyLevel);
/*     */     
/*     */ 
/*     */ 
/* 180 */     Preconditions.checkArgument(concurrencyLevel > 0);
/* 181 */     this.concurrencyLevel = concurrencyLevel;
/* 182 */     return this;
/*     */   }
/*     */   
/*     */   int getConcurrencyLevel() {
/* 186 */     return this.concurrencyLevel == -1 ? 4 : this.concurrencyLevel;
/*     */   }
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
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public MapMaker weakKeys()
/*     */   {
/* 203 */     return setKeyStrength(MapMakerInternalMap.Strength.WEAK);
/*     */   }
/*     */   
/*     */   MapMaker setKeyStrength(MapMakerInternalMap.Strength strength) {
/* 207 */     Preconditions.checkState(this.keyStrength == null, "Key strength was already set to %s", this.keyStrength);
/* 208 */     this.keyStrength = ((MapMakerInternalMap.Strength)Preconditions.checkNotNull(strength));
/* 209 */     if (strength != MapMakerInternalMap.Strength.STRONG)
/*     */     {
/* 211 */       this.useCustomMap = true;
/*     */     }
/* 213 */     return this;
/*     */   }
/*     */   
/*     */   MapMakerInternalMap.Strength getKeyStrength() {
/* 217 */     return (MapMakerInternalMap.Strength)MoreObjects.firstNonNull(this.keyStrength, MapMakerInternalMap.Strength.STRONG);
/*     */   }
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
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public MapMaker weakValues()
/*     */   {
/* 239 */     return setValueStrength(MapMakerInternalMap.Strength.WEAK);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static enum Dummy
/*     */   {
/* 249 */     VALUE;
/*     */     
/*     */     private Dummy() {} }
/*     */   
/* 253 */   MapMaker setValueStrength(MapMakerInternalMap.Strength strength) { Preconditions.checkState(this.valueStrength == null, "Value strength was already set to %s", this.valueStrength);
/* 254 */     this.valueStrength = ((MapMakerInternalMap.Strength)Preconditions.checkNotNull(strength));
/* 255 */     if (strength != MapMakerInternalMap.Strength.STRONG)
/*     */     {
/* 257 */       this.useCustomMap = true;
/*     */     }
/* 259 */     return this;
/*     */   }
/*     */   
/*     */   MapMakerInternalMap.Strength getValueStrength() {
/* 263 */     return (MapMakerInternalMap.Strength)MoreObjects.firstNonNull(this.valueStrength, MapMakerInternalMap.Strength.STRONG);
/*     */   }
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
/*     */   public <K, V> ConcurrentMap<K, V> makeMap()
/*     */   {
/* 278 */     if (!this.useCustomMap) {
/* 279 */       return new ConcurrentHashMap(getInitialCapacity(), 0.75F, getConcurrencyLevel());
/*     */     }
/* 281 */     return MapMakerInternalMap.create(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 290 */     MoreObjects.ToStringHelper s = MoreObjects.toStringHelper(this);
/* 291 */     if (this.initialCapacity != -1) {
/* 292 */       s.add("initialCapacity", this.initialCapacity);
/*     */     }
/* 294 */     if (this.concurrencyLevel != -1) {
/* 295 */       s.add("concurrencyLevel", this.concurrencyLevel);
/*     */     }
/* 297 */     if (this.keyStrength != null) {
/* 298 */       s.add("keyStrength", Ascii.toLowerCase(this.keyStrength.toString()));
/*     */     }
/* 300 */     if (this.valueStrength != null) {
/* 301 */       s.add("valueStrength", Ascii.toLowerCase(this.valueStrength.toString()));
/*     */     }
/* 303 */     if (this.keyEquivalence != null) {
/* 304 */       s.addValue("keyEquivalence");
/*     */     }
/* 306 */     return s.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\MapMaker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */