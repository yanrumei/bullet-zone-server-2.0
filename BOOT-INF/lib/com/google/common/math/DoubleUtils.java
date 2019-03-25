/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.math.BigInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ final class DoubleUtils
/*     */ {
/*     */   static final long SIGNIFICAND_MASK = 4503599627370495L;
/*     */   static final long EXPONENT_MASK = 9218868437227405312L;
/*     */   static final long SIGN_MASK = Long.MIN_VALUE;
/*     */   static final int SIGNIFICAND_BITS = 52;
/*     */   static final int EXPONENT_BIAS = 1023;
/*     */   static final long IMPLICIT_BIT = 4503599627370496L;
/*     */   
/*     */   static double nextDown(double d)
/*     */   {
/*  39 */     return -Math.nextUp(-d);
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
/*     */   static long getSignificand(double d)
/*     */   {
/*  64 */     Preconditions.checkArgument(isFinite(d), "not a normal value");
/*  65 */     int exponent = Math.getExponent(d);
/*  66 */     long bits = Double.doubleToRawLongBits(d);
/*  67 */     bits &= 0xFFFFFFFFFFFFF;
/*  68 */     return exponent == 64513 ? bits << 1 : bits | 0x10000000000000;
/*     */   }
/*     */   
/*     */ 
/*     */   static boolean isFinite(double d)
/*     */   {
/*  74 */     return Math.getExponent(d) <= 1023;
/*     */   }
/*     */   
/*     */   static boolean isNormal(double d) {
/*  78 */     return Math.getExponent(d) >= 64514;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static double scaleNormalize(double x)
/*     */   {
/*  86 */     long significand = Double.doubleToRawLongBits(x) & 0xFFFFFFFFFFFFF;
/*  87 */     return Double.longBitsToDouble(significand | ONE_BITS);
/*     */   }
/*     */   
/*     */   static double bigToDouble(BigInteger x)
/*     */   {
/*  92 */     BigInteger absX = x.abs();
/*  93 */     int exponent = absX.bitLength() - 1;
/*     */     
/*  95 */     if (exponent < 63)
/*  96 */       return x.longValue();
/*  97 */     if (exponent > 1023) {
/*  98 */       return x.signum() * Double.POSITIVE_INFINITY;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 109 */     int shift = exponent - 52 - 1;
/* 110 */     long twiceSignifFloor = absX.shiftRight(shift).longValue();
/* 111 */     long signifFloor = twiceSignifFloor >> 1;
/* 112 */     signifFloor &= 0xFFFFFFFFFFFFF;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 120 */     boolean increment = ((twiceSignifFloor & 1L) != 0L) && (((signifFloor & 1L) != 0L) || (absX.getLowestSetBit() < shift));
/* 121 */     long signifRounded = increment ? signifFloor + 1L : signifFloor;
/* 122 */     long bits = exponent + 1023 << 52;
/* 123 */     bits += signifRounded;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 130 */     bits |= x.signum() & 0x8000000000000000;
/* 131 */     return Double.longBitsToDouble(bits);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static double ensureNonNegative(double value)
/*     */   {
/* 138 */     Preconditions.checkArgument(!Double.isNaN(value));
/* 139 */     if (value > 0.0D) {
/* 140 */       return value;
/*     */     }
/* 142 */     return 0.0D;
/*     */   }
/*     */   
/*     */ 
/* 146 */   private static final long ONE_BITS = Double.doubleToRawLongBits(1.0D);
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\math\DoubleUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */