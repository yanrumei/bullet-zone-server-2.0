/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.LongMath;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.common.primitives.Longs;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.Arrays;
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
/*     */  enum BloomFilterStrategies
/*     */   implements BloomFilter.Strategy
/*     */ {
/*  44 */   MURMUR128_MITZ_32, 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  92 */   MURMUR128_MITZ_64;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private BloomFilterStrategies() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static final class BitArray
/*     */   {
/*     */     final long[] data;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     long bitCount;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     BitArray(long bits)
/*     */     {
/* 147 */       this(new long[Ints.checkedCast(LongMath.divide(bits, 64L, RoundingMode.CEILING))]);
/*     */     }
/*     */     
/*     */     BitArray(long[] data)
/*     */     {
/* 152 */       Preconditions.checkArgument(data.length > 0, "data length is zero!");
/* 153 */       this.data = data;
/* 154 */       long bitCount = 0L;
/* 155 */       for (long value : data) {
/* 156 */         bitCount += Long.bitCount(value);
/*     */       }
/* 158 */       this.bitCount = bitCount;
/*     */     }
/*     */     
/*     */     boolean set(long index)
/*     */     {
/* 163 */       if (!get(index)) {
/* 164 */         this.data[((int)(index >>> 6))] |= 1L << (int)index;
/* 165 */         this.bitCount += 1L;
/* 166 */         return true;
/*     */       }
/* 168 */       return false;
/*     */     }
/*     */     
/*     */     boolean get(long index) {
/* 172 */       return (this.data[((int)(index >>> 6))] & 1L << (int)index) != 0L;
/*     */     }
/*     */     
/*     */     long bitSize()
/*     */     {
/* 177 */       return this.data.length * 64L;
/*     */     }
/*     */     
/*     */     long bitCount()
/*     */     {
/* 182 */       return this.bitCount;
/*     */     }
/*     */     
/*     */     BitArray copy() {
/* 186 */       return new BitArray((long[])this.data.clone());
/*     */     }
/*     */     
/*     */     void putAll(BitArray array)
/*     */     {
/* 191 */       Preconditions.checkArgument(this.data.length == array.data.length, "BitArrays must be of equal length (%s != %s)", this.data.length, array.data.length);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 196 */       this.bitCount = 0L;
/* 197 */       for (int i = 0; i < this.data.length; i++) {
/* 198 */         this.data[i] |= array.data[i];
/* 199 */         this.bitCount += Long.bitCount(this.data[i]);
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object o)
/*     */     {
/* 205 */       if ((o instanceof BitArray)) {
/* 206 */         BitArray bitArray = (BitArray)o;
/* 207 */         return Arrays.equals(this.data, bitArray.data);
/*     */       }
/* 209 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 214 */       return Arrays.hashCode(this.data);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\hash\BloomFilterStrategies.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */