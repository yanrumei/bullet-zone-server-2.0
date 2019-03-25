/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.math.DoubleMath;
/*     */ import com.google.common.primitives.SignedBytes;
/*     */ import com.google.common.primitives.UnsignedBytes;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.math.RoundingMode;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ public final class BloomFilter<T>
/*     */   implements Predicate<T>, Serializable
/*     */ {
/*     */   private final BloomFilterStrategies.BitArray bits;
/*     */   private final int numHashFunctions;
/*     */   private final Funnel<? super T> funnel;
/*     */   private final Strategy strategy;
/*     */   
/*     */   private BloomFilter(BloomFilterStrategies.BitArray bits, int numHashFunctions, Funnel<? super T> funnel, Strategy strategy)
/*     */   {
/* 114 */     Preconditions.checkArgument(numHashFunctions > 0, "numHashFunctions (%s) must be > 0", numHashFunctions);
/* 115 */     Preconditions.checkArgument(numHashFunctions <= 255, "numHashFunctions (%s) must be <= 255", numHashFunctions);
/*     */     
/* 117 */     this.bits = ((BloomFilterStrategies.BitArray)Preconditions.checkNotNull(bits));
/* 118 */     this.numHashFunctions = numHashFunctions;
/* 119 */     this.funnel = ((Funnel)Preconditions.checkNotNull(funnel));
/* 120 */     this.strategy = ((Strategy)Preconditions.checkNotNull(strategy));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BloomFilter<T> copy()
/*     */   {
/* 130 */     return new BloomFilter(this.bits.copy(), this.numHashFunctions, this.funnel, this.strategy);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean mightContain(T object)
/*     */   {
/* 138 */     return this.strategy.mightContain(object, this.funnel, this.numHashFunctions, this.bits);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public boolean apply(T input)
/*     */   {
/* 148 */     return mightContain(input);
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
/*     */   public boolean put(T object)
/*     */   {
/* 164 */     return this.strategy.put(object, this.funnel, this.numHashFunctions, this.bits);
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
/*     */   public double expectedFpp()
/*     */   {
/* 180 */     return Math.pow(this.bits.bitCount() / bitSize(), this.numHashFunctions);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long approximateElementCount()
/*     */   {
/* 191 */     long bitSize = this.bits.bitSize();
/* 192 */     long bitCount = this.bits.bitCount();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 200 */     double fractionOfBitsSet = bitCount / bitSize;
/* 201 */     return DoubleMath.roundToLong(
/* 202 */       -Math.log1p(-fractionOfBitsSet) * bitSize / this.numHashFunctions, RoundingMode.HALF_UP);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @VisibleForTesting
/*     */   long bitSize()
/*     */   {
/* 210 */     return this.bits.bitSize();
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
/*     */   public boolean isCompatible(BloomFilter<T> that)
/*     */   {
/* 229 */     Preconditions.checkNotNull(that);
/* 230 */     if ((this != that) && (this.numHashFunctions == that.numHashFunctions)) {} return 
/*     */     
/* 232 */       (bitSize() == that.bitSize()) && 
/* 233 */       (this.strategy.equals(that.strategy)) && 
/* 234 */       (this.funnel.equals(that.funnel));
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
/*     */   public void putAll(BloomFilter<T> that)
/*     */   {
/* 247 */     Preconditions.checkNotNull(that);
/* 248 */     Preconditions.checkArgument(this != that, "Cannot combine a BloomFilter with itself.");
/* 249 */     Preconditions.checkArgument(this.numHashFunctions == that.numHashFunctions, "BloomFilters must have the same number of hash functions (%s != %s)", this.numHashFunctions, that.numHashFunctions);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 254 */     Preconditions.checkArgument(
/* 255 */       bitSize() == that.bitSize(), "BloomFilters must have the same size underlying bit arrays (%s != %s)", 
/*     */       
/* 257 */       bitSize(), that
/* 258 */       .bitSize());
/* 259 */     Preconditions.checkArgument(this.strategy
/* 260 */       .equals(that.strategy), "BloomFilters must have equal strategies (%s != %s)", this.strategy, that.strategy);
/*     */     
/*     */ 
/*     */ 
/* 264 */     Preconditions.checkArgument(this.funnel
/* 265 */       .equals(that.funnel), "BloomFilters must have equal funnels (%s != %s)", this.funnel, that.funnel);
/*     */     
/*     */ 
/*     */ 
/* 269 */     this.bits.putAll(that.bits);
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object object)
/*     */   {
/* 274 */     if (object == this) {
/* 275 */       return true;
/*     */     }
/* 277 */     if ((object instanceof BloomFilter)) {
/* 278 */       BloomFilter<?> that = (BloomFilter)object;
/* 279 */       return (this.numHashFunctions == that.numHashFunctions) && 
/* 280 */         (this.funnel.equals(that.funnel)) && 
/* 281 */         (this.bits.equals(that.bits)) && 
/* 282 */         (this.strategy.equals(that.strategy));
/*     */     }
/* 284 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 289 */     return Objects.hashCode(new Object[] { Integer.valueOf(this.numHashFunctions), this.funnel, this.strategy, this.bits });
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
/*     */   public static <T> BloomFilter<T> create(Funnel<? super T> funnel, int expectedInsertions, double fpp)
/*     */   {
/* 314 */     return create(funnel, expectedInsertions, fpp);
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
/*     */   public static <T> BloomFilter<T> create(Funnel<? super T> funnel, long expectedInsertions, double fpp)
/*     */   {
/* 340 */     return create(funnel, expectedInsertions, fpp, BloomFilterStrategies.MURMUR128_MITZ_64);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static <T> BloomFilter<T> create(Funnel<? super T> funnel, long expectedInsertions, double fpp, Strategy strategy)
/*     */   {
/* 346 */     Preconditions.checkNotNull(funnel);
/* 347 */     Preconditions.checkArgument(expectedInsertions >= 0L, "Expected insertions (%s) must be >= 0", expectedInsertions);
/*     */     
/* 349 */     Preconditions.checkArgument(fpp > 0.0D, "False positive probability (%s) must be > 0.0", Double.valueOf(fpp));
/* 350 */     Preconditions.checkArgument(fpp < 1.0D, "False positive probability (%s) must be < 1.0", Double.valueOf(fpp));
/* 351 */     Preconditions.checkNotNull(strategy);
/*     */     
/* 353 */     if (expectedInsertions == 0L) {
/* 354 */       expectedInsertions = 1L;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 361 */     long numBits = optimalNumOfBits(expectedInsertions, fpp);
/* 362 */     int numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, numBits);
/*     */     try {
/* 364 */       return new BloomFilter(new BloomFilterStrategies.BitArray(numBits), numHashFunctions, funnel, strategy);
/*     */     } catch (IllegalArgumentException e) {
/* 366 */       throw new IllegalArgumentException("Could not create BloomFilter of " + numBits + " bits", e);
/*     */     }
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
/*     */   public static <T> BloomFilter<T> create(Funnel<? super T> funnel, int expectedInsertions)
/*     */   {
/* 390 */     return create(funnel, expectedInsertions);
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
/*     */   public static <T> BloomFilter<T> create(Funnel<? super T> funnel, long expectedInsertions)
/*     */   {
/* 414 */     return create(funnel, expectedInsertions, 0.03D);
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
/*     */   @VisibleForTesting
/*     */   static int optimalNumOfHashFunctions(long n, long m)
/*     */   {
/* 441 */     return Math.max(1, (int)Math.round(m / n * Math.log(2.0D)));
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
/*     */   @VisibleForTesting
/*     */   static long optimalNumOfBits(long n, double p)
/*     */   {
/* 455 */     if (p == 0.0D) {
/* 456 */       p = Double.MIN_VALUE;
/*     */     }
/* 458 */     return (-n * Math.log(p) / (Math.log(2.0D) * Math.log(2.0D)));
/*     */   }
/*     */   
/*     */ 
/* 462 */   private Object writeReplace() { return new SerialForm(this); }
/*     */   
/*     */   private static class SerialForm<T> implements Serializable {
/*     */     final long[] data;
/*     */     final int numHashFunctions;
/*     */     final Funnel<? super T> funnel;
/*     */     final BloomFilter.Strategy strategy;
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     SerialForm(BloomFilter<T> bf) {
/* 472 */       this.data = bf.bits.data;
/* 473 */       this.numHashFunctions = bf.numHashFunctions;
/* 474 */       this.funnel = bf.funnel;
/* 475 */       this.strategy = bf.strategy;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 479 */       return new BloomFilter(new BloomFilterStrategies.BitArray(this.data), this.numHashFunctions, this.funnel, this.strategy, null);
/*     */     }
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
/*     */   public void writeTo(OutputStream out)
/*     */     throws IOException
/*     */   {
/* 498 */     DataOutputStream dout = new DataOutputStream(out);
/* 499 */     dout.writeByte(SignedBytes.checkedCast(this.strategy.ordinal()));
/* 500 */     dout.writeByte(UnsignedBytes.checkedCast(this.numHashFunctions));
/* 501 */     dout.writeInt(this.bits.data.length);
/* 502 */     for (long value : this.bits.data) {
/* 503 */       dout.writeLong(value);
/*     */     }
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
/*     */   public static <T> BloomFilter<T> readFrom(InputStream in, Funnel<? super T> funnel)
/*     */     throws IOException
/*     */   {
/* 520 */     Preconditions.checkNotNull(in, "InputStream");
/* 521 */     Preconditions.checkNotNull(funnel, "Funnel");
/* 522 */     int strategyOrdinal = -1;
/* 523 */     int numHashFunctions = -1;
/* 524 */     int dataLength = -1;
/*     */     try {
/* 526 */       DataInputStream din = new DataInputStream(in);
/*     */       
/*     */ 
/*     */ 
/* 530 */       strategyOrdinal = din.readByte();
/* 531 */       numHashFunctions = UnsignedBytes.toInt(din.readByte());
/* 532 */       dataLength = din.readInt();
/*     */       
/* 534 */       Strategy strategy = BloomFilterStrategies.values()[strategyOrdinal];
/* 535 */       long[] data = new long[dataLength];
/* 536 */       for (int i = 0; i < data.length; i++) {
/* 537 */         data[i] = din.readLong();
/*     */       }
/* 539 */       return new BloomFilter(new BloomFilterStrategies.BitArray(data), numHashFunctions, funnel, strategy);
/*     */     } catch (RuntimeException e) {
/* 541 */       String message = "Unable to deserialize BloomFilter from InputStream. strategyOrdinal: " + strategyOrdinal + " numHashFunctions: " + numHashFunctions + " dataLength: " + dataLength;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 549 */       throw new IOException(message, e);
/*     */     }
/*     */   }
/*     */   
/*     */   static abstract interface Strategy
/*     */     extends Serializable
/*     */   {
/*     */     public abstract <T> boolean put(T paramT, Funnel<? super T> paramFunnel, int paramInt, BloomFilterStrategies.BitArray paramBitArray);
/*     */     
/*     */     public abstract <T> boolean mightContain(T paramT, Funnel<? super T> paramFunnel, int paramInt, BloomFilterStrategies.BitArray paramBitArray);
/*     */     
/*     */     public abstract int ordinal();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\hash\BloomFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */