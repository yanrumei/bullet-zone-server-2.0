/*     */ package com.google.common.cache;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Ascii;
/*     */ import com.google.common.base.Equivalence;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.MoreObjects.ToStringHelper;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.base.Ticker;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.CheckReturnValue;
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
/*     */ public final class CacheBuilder<K, V>
/*     */ {
/*     */   private static final int DEFAULT_INITIAL_CAPACITY = 16;
/*     */   private static final int DEFAULT_CONCURRENCY_LEVEL = 4;
/*     */   private static final int DEFAULT_EXPIRATION_NANOS = 0;
/*     */   private static final int DEFAULT_REFRESH_NANOS = 0;
/* 153 */   static final Supplier<? extends AbstractCache.StatsCounter> NULL_STATS_COUNTER = Suppliers.ofInstance(new AbstractCache.StatsCounter()
/*     */   {
/*     */     public void recordHits(int count) {}
/*     */     
/*     */ 
/*     */ 
/*     */     public void recordMisses(int count) {}
/*     */     
/*     */ 
/*     */     public void recordLoadSuccess(long loadTime) {}
/*     */     
/*     */ 
/*     */     public void recordLoadException(long loadTime) {}
/*     */     
/*     */ 
/*     */     public void recordEviction() {}
/*     */     
/*     */ 
/*     */     public CacheStats snapshot()
/*     */     {
/* 172 */       return CacheBuilder.EMPTY_STATS;
/*     */     }
/* 153 */   });
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
/* 175 */   static final CacheStats EMPTY_STATS = new CacheStats(0L, 0L, 0L, 0L, 0L, 0L);
/*     */   
/* 177 */   static final Supplier<AbstractCache.StatsCounter> CACHE_STATS_COUNTER = new Supplier()
/*     */   {
/*     */     public AbstractCache.StatsCounter get()
/*     */     {
/* 181 */       return new AbstractCache.SimpleStatsCounter();
/*     */     }
/*     */   };
/*     */   
/*     */   static enum NullListener implements RemovalListener<Object, Object> {
/* 186 */     INSTANCE;
/*     */     
/*     */     private NullListener() {}
/*     */     
/*     */     public void onRemoval(RemovalNotification<Object, Object> notification) {}
/*     */   }
/*     */   
/* 193 */   static enum OneWeigher implements Weigher<Object, Object> { INSTANCE;
/*     */     
/*     */     private OneWeigher() {}
/*     */     
/* 197 */     public int weigh(Object key, Object value) { return 1; }
/*     */   }
/*     */   
/*     */ 
/* 201 */   static final Ticker NULL_TICKER = new Ticker()
/*     */   {
/*     */     public long read()
/*     */     {
/* 205 */       return 0L;
/*     */     }
/*     */   };
/*     */   
/* 209 */   private static final Logger logger = Logger.getLogger(CacheBuilder.class.getName());
/*     */   
/*     */   static final int UNSET_INT = -1;
/*     */   
/* 213 */   boolean strictParsing = true;
/*     */   
/* 215 */   int initialCapacity = -1;
/* 216 */   int concurrencyLevel = -1;
/* 217 */   long maximumSize = -1L;
/* 218 */   long maximumWeight = -1L;
/*     */   
/*     */   Weigher<? super K, ? super V> weigher;
/*     */   
/*     */   LocalCache.Strength keyStrength;
/*     */   LocalCache.Strength valueStrength;
/* 224 */   long expireAfterWriteNanos = -1L;
/* 225 */   long expireAfterAccessNanos = -1L;
/* 226 */   long refreshNanos = -1L;
/*     */   
/*     */   Equivalence<Object> keyEquivalence;
/*     */   
/*     */   Equivalence<Object> valueEquivalence;
/*     */   
/*     */   RemovalListener<? super K, ? super V> removalListener;
/*     */   Ticker ticker;
/* 234 */   Supplier<? extends AbstractCache.StatsCounter> statsCounterSupplier = NULL_STATS_COUNTER;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static CacheBuilder<Object, Object> newBuilder()
/*     */   {
/* 244 */     return new CacheBuilder();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   public static CacheBuilder<Object, Object> from(CacheBuilderSpec spec)
/*     */   {
/* 254 */     return spec.toCacheBuilder().lenientParsing();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   public static CacheBuilder<Object, Object> from(String spec)
/*     */   {
/* 266 */     return from(CacheBuilderSpec.parse(spec));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   CacheBuilder<K, V> lenientParsing()
/*     */   {
/* 276 */     this.strictParsing = false;
/* 277 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   CacheBuilder<K, V> keyEquivalence(Equivalence<Object> equivalence)
/*     */   {
/* 290 */     Preconditions.checkState(this.keyEquivalence == null, "key equivalence was already set to %s", this.keyEquivalence);
/* 291 */     this.keyEquivalence = ((Equivalence)Preconditions.checkNotNull(equivalence));
/* 292 */     return this;
/*     */   }
/*     */   
/*     */   Equivalence<Object> getKeyEquivalence() {
/* 296 */     return (Equivalence)MoreObjects.firstNonNull(this.keyEquivalence, getKeyStrength().defaultEquivalence());
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
/*     */   @GwtIncompatible
/*     */   CacheBuilder<K, V> valueEquivalence(Equivalence<Object> equivalence)
/*     */   {
/* 310 */     Preconditions.checkState(this.valueEquivalence == null, "value equivalence was already set to %s", this.valueEquivalence);
/*     */     
/* 312 */     this.valueEquivalence = ((Equivalence)Preconditions.checkNotNull(equivalence));
/* 313 */     return this;
/*     */   }
/*     */   
/*     */   Equivalence<Object> getValueEquivalence() {
/* 317 */     return (Equivalence)MoreObjects.firstNonNull(this.valueEquivalence, getValueStrength().defaultEquivalence());
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
/*     */   public CacheBuilder<K, V> initialCapacity(int initialCapacity)
/*     */   {
/* 332 */     Preconditions.checkState(this.initialCapacity == -1, "initial capacity was already set to %s", this.initialCapacity);
/*     */     
/*     */ 
/*     */ 
/* 336 */     Preconditions.checkArgument(initialCapacity >= 0);
/* 337 */     this.initialCapacity = initialCapacity;
/* 338 */     return this;
/*     */   }
/*     */   
/*     */   int getInitialCapacity() {
/* 342 */     return this.initialCapacity == -1 ? 16 : this.initialCapacity;
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
/*     */   public CacheBuilder<K, V> concurrencyLevel(int concurrencyLevel)
/*     */   {
/* 377 */     Preconditions.checkState(this.concurrencyLevel == -1, "concurrency level was already set to %s", this.concurrencyLevel);
/*     */     
/*     */ 
/*     */ 
/* 381 */     Preconditions.checkArgument(concurrencyLevel > 0);
/* 382 */     this.concurrencyLevel = concurrencyLevel;
/* 383 */     return this;
/*     */   }
/*     */   
/*     */   int getConcurrencyLevel() {
/* 387 */     return this.concurrencyLevel == -1 ? 4 : this.concurrencyLevel;
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
/*     */ 
/*     */ 
/*     */   public CacheBuilder<K, V> maximumSize(long maximumSize)
/*     */   {
/* 412 */     Preconditions.checkState(this.maximumSize == -1L, "maximum size was already set to %s", this.maximumSize);
/*     */     
/* 414 */     Preconditions.checkState(this.maximumWeight == -1L, "maximum weight was already set to %s", this.maximumWeight);
/*     */     
/*     */ 
/*     */ 
/* 418 */     Preconditions.checkState(this.weigher == null, "maximum size can not be combined with weigher");
/* 419 */     Preconditions.checkArgument(maximumSize >= 0L, "maximum size must not be negative");
/* 420 */     this.maximumSize = maximumSize;
/* 421 */     return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   public CacheBuilder<K, V> maximumWeight(long maximumWeight)
/*     */   {
/* 453 */     Preconditions.checkState(this.maximumWeight == -1L, "maximum weight was already set to %s", this.maximumWeight);
/*     */     
/*     */ 
/*     */ 
/* 457 */     Preconditions.checkState(this.maximumSize == -1L, "maximum size was already set to %s", this.maximumSize);
/*     */     
/* 459 */     this.maximumWeight = maximumWeight;
/* 460 */     Preconditions.checkArgument(maximumWeight >= 0L, "maximum weight must not be negative");
/* 461 */     return this;
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
/*     */   @GwtIncompatible
/*     */   public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> weigher(Weigher<? super K1, ? super V1> weigher)
/*     */   {
/* 496 */     Preconditions.checkState(this.weigher == null);
/* 497 */     if (this.strictParsing) {
/* 498 */       Preconditions.checkState(this.maximumSize == -1L, "weigher can not be combined with maximum size", this.maximumSize);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 506 */     CacheBuilder<K1, V1> me = this;
/* 507 */     me.weigher = ((Weigher)Preconditions.checkNotNull(weigher));
/* 508 */     return me;
/*     */   }
/*     */   
/*     */   long getMaximumWeight() {
/* 512 */     if ((this.expireAfterWriteNanos == 0L) || (this.expireAfterAccessNanos == 0L)) {
/* 513 */       return 0L;
/*     */     }
/* 515 */     return this.weigher == null ? this.maximumSize : this.maximumWeight;
/*     */   }
/*     */   
/*     */ 
/*     */   <K1 extends K, V1 extends V> Weigher<K1, V1> getWeigher()
/*     */   {
/* 521 */     return (Weigher)MoreObjects.firstNonNull(this.weigher, OneWeigher.INSTANCE);
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
/*     */   @GwtIncompatible
/*     */   public CacheBuilder<K, V> weakKeys()
/*     */   {
/* 540 */     return setKeyStrength(LocalCache.Strength.WEAK);
/*     */   }
/*     */   
/*     */   CacheBuilder<K, V> setKeyStrength(LocalCache.Strength strength) {
/* 544 */     Preconditions.checkState(this.keyStrength == null, "Key strength was already set to %s", this.keyStrength);
/* 545 */     this.keyStrength = ((LocalCache.Strength)Preconditions.checkNotNull(strength));
/* 546 */     return this;
/*     */   }
/*     */   
/*     */   LocalCache.Strength getKeyStrength() {
/* 550 */     return (LocalCache.Strength)MoreObjects.firstNonNull(this.keyStrength, LocalCache.Strength.STRONG);
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
/*     */   @GwtIncompatible
/*     */   public CacheBuilder<K, V> weakValues()
/*     */   {
/* 572 */     return setValueStrength(LocalCache.Strength.WEAK);
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
/*     */ 
/*     */   @GwtIncompatible
/*     */   public CacheBuilder<K, V> softValues()
/*     */   {
/* 597 */     return setValueStrength(LocalCache.Strength.SOFT);
/*     */   }
/*     */   
/*     */   CacheBuilder<K, V> setValueStrength(LocalCache.Strength strength) {
/* 601 */     Preconditions.checkState(this.valueStrength == null, "Value strength was already set to %s", this.valueStrength);
/* 602 */     this.valueStrength = ((LocalCache.Strength)Preconditions.checkNotNull(strength));
/* 603 */     return this;
/*     */   }
/*     */   
/*     */   LocalCache.Strength getValueStrength() {
/* 607 */     return (LocalCache.Strength)MoreObjects.firstNonNull(this.valueStrength, LocalCache.Strength.STRONG);
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
/*     */   public CacheBuilder<K, V> expireAfterWrite(long duration, TimeUnit unit)
/*     */   {
/* 630 */     Preconditions.checkState(this.expireAfterWriteNanos == -1L, "expireAfterWrite was already set to %s ns", this.expireAfterWriteNanos);
/*     */     
/*     */ 
/*     */ 
/* 634 */     Preconditions.checkArgument(duration >= 0L, "duration cannot be negative: %s %s", duration, unit);
/* 635 */     this.expireAfterWriteNanos = unit.toNanos(duration);
/* 636 */     return this;
/*     */   }
/*     */   
/*     */   long getExpireAfterWriteNanos() {
/* 640 */     return this.expireAfterWriteNanos == -1L ? 0L : this.expireAfterWriteNanos;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public CacheBuilder<K, V> expireAfterAccess(long duration, TimeUnit unit)
/*     */   {
/* 666 */     Preconditions.checkState(this.expireAfterAccessNanos == -1L, "expireAfterAccess was already set to %s ns", this.expireAfterAccessNanos);
/*     */     
/*     */ 
/*     */ 
/* 670 */     Preconditions.checkArgument(duration >= 0L, "duration cannot be negative: %s %s", duration, unit);
/* 671 */     this.expireAfterAccessNanos = unit.toNanos(duration);
/* 672 */     return this;
/*     */   }
/*     */   
/*     */   long getExpireAfterAccessNanos() {
/* 676 */     return this.expireAfterAccessNanos == -1L ? 0L : this.expireAfterAccessNanos;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   public CacheBuilder<K, V> refreshAfterWrite(long duration, TimeUnit unit)
/*     */   {
/* 709 */     Preconditions.checkNotNull(unit);
/* 710 */     Preconditions.checkState(this.refreshNanos == -1L, "refresh was already set to %s ns", this.refreshNanos);
/* 711 */     Preconditions.checkArgument(duration > 0L, "duration must be positive: %s %s", duration, unit);
/* 712 */     this.refreshNanos = unit.toNanos(duration);
/* 713 */     return this;
/*     */   }
/*     */   
/*     */   long getRefreshNanos() {
/* 717 */     return this.refreshNanos == -1L ? 0L : this.refreshNanos;
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
/*     */   public CacheBuilder<K, V> ticker(Ticker ticker)
/*     */   {
/* 731 */     Preconditions.checkState(this.ticker == null);
/* 732 */     this.ticker = ((Ticker)Preconditions.checkNotNull(ticker));
/* 733 */     return this;
/*     */   }
/*     */   
/*     */   Ticker getTicker(boolean recordsTime) {
/* 737 */     if (this.ticker != null) {
/* 738 */       return this.ticker;
/*     */     }
/* 740 */     return recordsTime ? Ticker.systemTicker() : NULL_TICKER;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   @CheckReturnValue
/*     */   public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> removalListener(RemovalListener<? super K1, ? super V1> listener)
/*     */   {
/* 767 */     Preconditions.checkState(this.removalListener == null);
/*     */     
/*     */ 
/*     */ 
/* 771 */     CacheBuilder<K1, V1> me = this;
/* 772 */     me.removalListener = ((RemovalListener)Preconditions.checkNotNull(listener));
/* 773 */     return me;
/*     */   }
/*     */   
/*     */ 
/*     */   <K1 extends K, V1 extends V> RemovalListener<K1, V1> getRemovalListener()
/*     */   {
/* 779 */     return 
/* 780 */       (RemovalListener)MoreObjects.firstNonNull(this.removalListener, NullListener.INSTANCE);
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
/*     */   public CacheBuilder<K, V> recordStats()
/*     */   {
/* 793 */     this.statsCounterSupplier = CACHE_STATS_COUNTER;
/* 794 */     return this;
/*     */   }
/*     */   
/*     */   boolean isRecordingStats() {
/* 798 */     return this.statsCounterSupplier == CACHE_STATS_COUNTER;
/*     */   }
/*     */   
/*     */   Supplier<? extends AbstractCache.StatsCounter> getStatsCounterSupplier() {
/* 802 */     return this.statsCounterSupplier;
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
/*     */   public <K1 extends K, V1 extends V> LoadingCache<K1, V1> build(CacheLoader<? super K1, V1> loader)
/*     */   {
/* 819 */     checkWeightWithWeigher();
/* 820 */     return new LocalCache.LocalLoadingCache(this, loader);
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
/*     */   public <K1 extends K, V1 extends V> Cache<K1, V1> build()
/*     */   {
/* 836 */     checkWeightWithWeigher();
/* 837 */     checkNonLoadingCache();
/* 838 */     return new LocalCache.LocalManualCache(this);
/*     */   }
/*     */   
/*     */   private void checkNonLoadingCache() {
/* 842 */     Preconditions.checkState(this.refreshNanos == -1L, "refreshAfterWrite requires a LoadingCache");
/*     */   }
/*     */   
/*     */   private void checkWeightWithWeigher() {
/* 846 */     if (this.weigher == null) {
/* 847 */       Preconditions.checkState(this.maximumWeight == -1L, "maximumWeight requires weigher");
/*     */     }
/* 849 */     else if (this.strictParsing) {
/* 850 */       Preconditions.checkState(this.maximumWeight != -1L, "weigher requires maximumWeight");
/*     */     }
/* 852 */     else if (this.maximumWeight == -1L) {
/* 853 */       logger.log(Level.WARNING, "ignoring weigher specified without maximumWeight");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 865 */     MoreObjects.ToStringHelper s = MoreObjects.toStringHelper(this);
/* 866 */     if (this.initialCapacity != -1) {
/* 867 */       s.add("initialCapacity", this.initialCapacity);
/*     */     }
/* 869 */     if (this.concurrencyLevel != -1) {
/* 870 */       s.add("concurrencyLevel", this.concurrencyLevel);
/*     */     }
/* 872 */     if (this.maximumSize != -1L) {
/* 873 */       s.add("maximumSize", this.maximumSize);
/*     */     }
/* 875 */     if (this.maximumWeight != -1L) {
/* 876 */       s.add("maximumWeight", this.maximumWeight);
/*     */     }
/* 878 */     if (this.expireAfterWriteNanos != -1L) {
/* 879 */       s.add("expireAfterWrite", this.expireAfterWriteNanos + "ns");
/*     */     }
/* 881 */     if (this.expireAfterAccessNanos != -1L) {
/* 882 */       s.add("expireAfterAccess", this.expireAfterAccessNanos + "ns");
/*     */     }
/* 884 */     if (this.keyStrength != null) {
/* 885 */       s.add("keyStrength", Ascii.toLowerCase(this.keyStrength.toString()));
/*     */     }
/* 887 */     if (this.valueStrength != null) {
/* 888 */       s.add("valueStrength", Ascii.toLowerCase(this.valueStrength.toString()));
/*     */     }
/* 890 */     if (this.keyEquivalence != null) {
/* 891 */       s.addValue("keyEquivalence");
/*     */     }
/* 893 */     if (this.valueEquivalence != null) {
/* 894 */       s.addValue("valueEquivalence");
/*     */     }
/* 896 */     if (this.removalListener != null) {
/* 897 */       s.addValue("removalListener");
/*     */     }
/* 899 */     return s.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\cache\CacheBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */