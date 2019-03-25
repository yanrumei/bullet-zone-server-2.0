/*      */ package com.google.common.math;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.primitives.UnsignedLongs;
/*      */ import java.math.RoundingMode;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible(emulated=true)
/*      */ public final class LongMath
/*      */ {
/*      */   @VisibleForTesting
/*      */   static final long MAX_SIGNED_POWER_OF_TWO = 4611686018427387904L;
/*      */   @VisibleForTesting
/*      */   static final long MAX_POWER_OF_SQRT2_UNSIGNED = -5402926248376769404L;
/*      */   
/*      */   @Beta
/*      */   public static long ceilingPowerOfTwo(long x)
/*      */   {
/*   67 */     MathPreconditions.checkPositive("x", x);
/*   68 */     if (x > 4611686018427387904L) {
/*   69 */       throw new ArithmeticException("ceilingPowerOfTwo(" + x + ") is not representable as a long");
/*      */     }
/*   71 */     return 1L << -Long.numberOfLeadingZeros(x - 1L);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Beta
/*      */   public static long floorPowerOfTwo(long x)
/*      */   {
/*   83 */     MathPreconditions.checkPositive("x", x);
/*      */     
/*      */ 
/*      */ 
/*   87 */     return 1L << 63 - Long.numberOfLeadingZeros(x);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isPowerOfTwo(long x)
/*      */   {
/*   97 */     return (x > 0L ? 1 : 0) & ((x & x - 1L) == 0L ? 1 : 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @VisibleForTesting
/*      */   static int lessThanBranchFree(long x, long y)
/*      */   {
/*  108 */     return (int)((x - y ^ 0xFFFFFFFFFFFFFFFF ^ 0xFFFFFFFFFFFFFFFF) >>> 63);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int log2(long x, RoundingMode mode)
/*      */   {
/*  121 */     MathPreconditions.checkPositive("x", x);
/*  122 */     switch (mode) {
/*      */     case UNNECESSARY: 
/*  124 */       MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
/*      */     
/*      */     case DOWN: 
/*      */     case FLOOR: 
/*  128 */       return 63 - Long.numberOfLeadingZeros(x);
/*      */     
/*      */     case UP: 
/*      */     case CEILING: 
/*  132 */       return 64 - Long.numberOfLeadingZeros(x - 1L);
/*      */     
/*      */ 
/*      */     case HALF_DOWN: 
/*      */     case HALF_UP: 
/*      */     case HALF_EVEN: 
/*  138 */       int leadingZeros = Long.numberOfLeadingZeros(x);
/*  139 */       long cmp = -5402926248376769404L >>> leadingZeros;
/*      */       
/*  141 */       int logFloor = 63 - leadingZeros;
/*  142 */       return logFloor + lessThanBranchFree(cmp, x);
/*      */     }
/*      */     
/*  145 */     throw new AssertionError("impossible");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   public static int log10(long x, RoundingMode mode)
/*      */   {
/*  163 */     MathPreconditions.checkPositive("x", x);
/*  164 */     int logFloor = log10Floor(x);
/*  165 */     long floorPow = powersOf10[logFloor];
/*  166 */     switch (mode) {
/*      */     case UNNECESSARY: 
/*  168 */       MathPreconditions.checkRoundingUnnecessary(x == floorPow);
/*      */     
/*      */     case DOWN: 
/*      */     case FLOOR: 
/*  172 */       return logFloor;
/*      */     case UP: 
/*      */     case CEILING: 
/*  175 */       return logFloor + lessThanBranchFree(floorPow, x);
/*      */     
/*      */     case HALF_DOWN: 
/*      */     case HALF_UP: 
/*      */     case HALF_EVEN: 
/*  180 */       return logFloor + lessThanBranchFree(halfPowersOf10[logFloor], x);
/*      */     }
/*  182 */     throw new AssertionError();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   static int log10Floor(long x)
/*      */   {
/*  195 */     int y = maxLog10ForLeadingZeros[Long.numberOfLeadingZeros(x)];
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  200 */     return y - lessThanBranchFree(x, powersOf10[y]);
/*      */   }
/*      */   
/*      */ 
/*      */   @VisibleForTesting
/*  205 */   static final byte[] maxLog10ForLeadingZeros = { 19, 18, 18, 18, 18, 17, 17, 17, 16, 16, 16, 15, 15, 15, 15, 14, 14, 14, 13, 13, 13, 12, 12, 12, 12, 11, 11, 11, 10, 10, 10, 9, 9, 9, 9, 8, 8, 8, 7, 7, 7, 6, 6, 6, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2, 1, 1, 1, 0, 0, 0 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   @VisibleForTesting
/*  213 */   static final long[] powersOf10 = { 1L, 10L, 100L, 1000L, 10000L, 100000L, 1000000L, 10000000L, 100000000L, 1000000000L, 10000000000L, 100000000000L, 1000000000000L, 10000000000000L, 100000000000000L, 1000000000000000L, 10000000000000000L, 100000000000000000L, 1000000000000000000L };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   @VisibleForTesting
/*  238 */   static final long[] halfPowersOf10 = { 3L, 31L, 316L, 3162L, 31622L, 316227L, 3162277L, 31622776L, 316227766L, 3162277660L, 31622776601L, 316227766016L, 3162277660168L, 31622776601683L, 316227766016837L, 3162277660168379L, 31622776601683793L, 316227766016837933L, 3162277660168379331L };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @VisibleForTesting
/*      */   static final long FLOOR_SQRT_MAX_LONG = 3037000499L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   public static long pow(long b, int k)
/*      */   {
/*  269 */     MathPreconditions.checkNonNegative("exponent", k);
/*  270 */     if ((-2L <= b) && (b <= 2L)) {
/*  271 */       switch ((int)b) {
/*      */       case 0: 
/*  273 */         return k == 0 ? 1L : 0L;
/*      */       case 1: 
/*  275 */         return 1L;
/*      */       case -1: 
/*  277 */         return (k & 0x1) == 0 ? 1L : -1L;
/*      */       case 2: 
/*  279 */         return k < 64 ? 1L << k : 0L;
/*      */       case -2: 
/*  281 */         if (k < 64) {
/*  282 */           return (k & 0x1) == 0 ? 1L << k : -(1L << k);
/*      */         }
/*  284 */         return 0L;
/*      */       }
/*      */       
/*  287 */       throw new AssertionError();
/*      */     }
/*      */     
/*  290 */     for (long accum = 1L;; k >>= 1) {
/*  291 */       switch (k) {
/*      */       case 0: 
/*  293 */         return accum;
/*      */       case 1: 
/*  295 */         return accum * b;
/*      */       }
/*  297 */       accum *= ((k & 0x1) == 0 ? 1L : b);
/*  298 */       b *= b;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   public static long sqrt(long x, RoundingMode mode)
/*      */   {
/*  313 */     MathPreconditions.checkNonNegative("x", x);
/*  314 */     if (fitsInInt(x)) {
/*  315 */       return IntMath.sqrt((int)x, mode);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  332 */     long guess = Math.sqrt(x);
/*      */     
/*  334 */     long guessSquared = guess * guess;
/*      */     
/*      */ 
/*  337 */     switch (mode) {
/*      */     case UNNECESSARY: 
/*  339 */       MathPreconditions.checkRoundingUnnecessary(guessSquared == x);
/*  340 */       return guess;
/*      */     case DOWN: 
/*      */     case FLOOR: 
/*  343 */       if (x < guessSquared) {
/*  344 */         return guess - 1L;
/*      */       }
/*  346 */       return guess;
/*      */     case UP: 
/*      */     case CEILING: 
/*  349 */       if (x > guessSquared) {
/*  350 */         return guess + 1L;
/*      */       }
/*  352 */       return guess;
/*      */     case HALF_DOWN: 
/*      */     case HALF_UP: 
/*      */     case HALF_EVEN: 
/*  356 */       long sqrtFloor = guess - (x < guessSquared ? 1 : 0);
/*  357 */       long halfSquare = sqrtFloor * sqrtFloor + sqrtFloor;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  369 */       return sqrtFloor + lessThanBranchFree(halfSquare, x);
/*      */     }
/*  371 */     throw new AssertionError();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   public static long divide(long p, long q, RoundingMode mode)
/*      */   {
/*  385 */     Preconditions.checkNotNull(mode);
/*  386 */     long div = p / q;
/*  387 */     long rem = p - q * div;
/*      */     
/*  389 */     if (rem == 0L) {
/*  390 */       return div;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  400 */     int signum = 0x1 | (int)((p ^ q) >> 63);
/*      */     boolean increment;
/*  402 */     boolean increment; boolean increment; boolean increment; boolean increment; switch (mode) {
/*      */     case UNNECESSARY: 
/*  404 */       MathPreconditions.checkRoundingUnnecessary(rem == 0L);
/*      */     
/*      */     case DOWN: 
/*  407 */       increment = false;
/*  408 */       break;
/*      */     case UP: 
/*  410 */       increment = true;
/*  411 */       break;
/*      */     case CEILING: 
/*  413 */       increment = signum > 0;
/*  414 */       break;
/*      */     case FLOOR: 
/*  416 */       increment = signum < 0;
/*  417 */       break;
/*      */     case HALF_DOWN: 
/*      */     case HALF_UP: 
/*      */     case HALF_EVEN: 
/*  421 */       long absRem = Math.abs(rem);
/*  422 */       long cmpRemToHalfDivisor = absRem - (Math.abs(q) - absRem);
/*      */       
/*      */       boolean increment;
/*  425 */       if (cmpRemToHalfDivisor == 0L) {
/*  426 */         increment = (mode == RoundingMode.HALF_UP ? 1 : 0) | (mode == RoundingMode.HALF_EVEN ? 1 : 0) & ((div & 1L) != 0L ? 1 : 0);
/*      */       } else {
/*  428 */         increment = cmpRemToHalfDivisor > 0L;
/*      */       }
/*  430 */       break;
/*      */     default: 
/*  432 */       throw new AssertionError(); }
/*      */     boolean increment;
/*  434 */     return increment ? div + signum : div;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   public static int mod(long x, int m)
/*      */   {
/*  458 */     return (int)mod(x, m);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   public static long mod(long x, long m)
/*      */   {
/*  481 */     if (m <= 0L) {
/*  482 */       throw new ArithmeticException("Modulus must be positive");
/*      */     }
/*  484 */     long result = x % m;
/*  485 */     return result >= 0L ? result : result + m;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static long gcd(long a, long b)
/*      */   {
/*  500 */     MathPreconditions.checkNonNegative("a", a);
/*  501 */     MathPreconditions.checkNonNegative("b", b);
/*  502 */     if (a == 0L)
/*      */     {
/*      */ 
/*  505 */       return b; }
/*  506 */     if (b == 0L) {
/*  507 */       return a;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  513 */     int aTwos = Long.numberOfTrailingZeros(a);
/*  514 */     a >>= aTwos;
/*  515 */     int bTwos = Long.numberOfTrailingZeros(b);
/*  516 */     b >>= bTwos;
/*  517 */     while (a != b)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  525 */       long delta = a - b;
/*      */       
/*  527 */       long minDeltaOrZero = delta & delta >> 63;
/*      */       
/*      */ 
/*  530 */       a = delta - minDeltaOrZero - minDeltaOrZero;
/*      */       
/*      */ 
/*  533 */       b += minDeltaOrZero;
/*  534 */       a >>= Long.numberOfTrailingZeros(a);
/*      */     }
/*  536 */     return a << Math.min(aTwos, bTwos);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   public static long checkedAdd(long a, long b)
/*      */   {
/*  546 */     long result = a + b;
/*  547 */     MathPreconditions.checkNoOverflow(((a ^ b) < 0L ? 1 : 0) | ((a ^ result) >= 0L ? 1 : 0));
/*  548 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   public static long checkedSubtract(long a, long b)
/*      */   {
/*  558 */     long result = a - b;
/*  559 */     MathPreconditions.checkNoOverflow(((a ^ b) >= 0L ? 1 : 0) | ((a ^ result) >= 0L ? 1 : 0));
/*  560 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   public static long checkedMultiply(long a, long b)
/*      */   {
/*  575 */     int leadingZeros = Long.numberOfLeadingZeros(a) + Long.numberOfLeadingZeros(a ^ 0xFFFFFFFFFFFFFFFF) + Long.numberOfLeadingZeros(b) + Long.numberOfLeadingZeros(b ^ 0xFFFFFFFFFFFFFFFF);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  586 */     if (leadingZeros > 65) {
/*  587 */       return a * b;
/*      */     }
/*  589 */     MathPreconditions.checkNoOverflow(leadingZeros >= 64);
/*  590 */     MathPreconditions.checkNoOverflow((a >= 0L ? 1 : 0) | (b != Long.MIN_VALUE ? 1 : 0));
/*  591 */     long result = a * b;
/*  592 */     MathPreconditions.checkNoOverflow((a == 0L) || (result / a == b));
/*  593 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   public static long checkedPow(long b, int k)
/*      */   {
/*  604 */     MathPreconditions.checkNonNegative("exponent", k);
/*  605 */     if (((b >= -2L ? 1 : 0) & (b <= 2L ? 1 : 0)) != 0) {
/*  606 */       switch ((int)b) {
/*      */       case 0: 
/*  608 */         return k == 0 ? 1L : 0L;
/*      */       case 1: 
/*  610 */         return 1L;
/*      */       case -1: 
/*  612 */         return (k & 0x1) == 0 ? 1L : -1L;
/*      */       case 2: 
/*  614 */         MathPreconditions.checkNoOverflow(k < 63);
/*  615 */         return 1L << k;
/*      */       case -2: 
/*  617 */         MathPreconditions.checkNoOverflow(k < 64);
/*  618 */         return (k & 0x1) == 0 ? 1L << k : -1L << k;
/*      */       }
/*  620 */       throw new AssertionError();
/*      */     }
/*      */     
/*  623 */     long accum = 1L;
/*      */     for (;;) {
/*  625 */       switch (k) {
/*      */       case 0: 
/*  627 */         return accum;
/*      */       case 1: 
/*  629 */         return checkedMultiply(accum, b);
/*      */       }
/*  631 */       if ((k & 0x1) != 0) {
/*  632 */         accum = checkedMultiply(accum, b);
/*      */       }
/*  634 */       k >>= 1;
/*  635 */       if (k > 0) {
/*  636 */         MathPreconditions.checkNoOverflow((-3037000499L <= b) && (b <= 3037000499L));
/*  637 */         b *= b;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Beta
/*      */   public static long saturatedAdd(long a, long b)
/*      */   {
/*  651 */     long naiveSum = a + b;
/*  652 */     if ((((a ^ b) < 0L ? 1 : 0) | ((a ^ naiveSum) >= 0L ? 1 : 0)) != 0)
/*      */     {
/*      */ 
/*  655 */       return naiveSum;
/*      */     }
/*      */     
/*  658 */     return Long.MAX_VALUE + (naiveSum >>> 63 ^ 1L);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Beta
/*      */   public static long saturatedSubtract(long a, long b)
/*      */   {
/*  669 */     long naiveDifference = a - b;
/*  670 */     if ((((a ^ b) >= 0L ? 1 : 0) | ((a ^ naiveDifference) >= 0L ? 1 : 0)) != 0)
/*      */     {
/*      */ 
/*  673 */       return naiveDifference;
/*      */     }
/*      */     
/*  676 */     return Long.MAX_VALUE + (naiveDifference >>> 63 ^ 1L);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Beta
/*      */   public static long saturatedMultiply(long a, long b)
/*      */   {
/*  692 */     int leadingZeros = Long.numberOfLeadingZeros(a) + Long.numberOfLeadingZeros(a ^ 0xFFFFFFFFFFFFFFFF) + Long.numberOfLeadingZeros(b) + Long.numberOfLeadingZeros(b ^ 0xFFFFFFFFFFFFFFFF);
/*  693 */     if (leadingZeros > 65) {
/*  694 */       return a * b;
/*      */     }
/*      */     
/*  697 */     long limit = Long.MAX_VALUE + ((a ^ b) >>> 63);
/*  698 */     if (((leadingZeros < 64 ? 1 : 0) | (a < 0L ? 1 : 0) & (b == Long.MIN_VALUE ? 1 : 0)) != 0)
/*      */     {
/*  700 */       return limit;
/*      */     }
/*  702 */     long result = a * b;
/*  703 */     if ((a == 0L) || (result / a == b)) {
/*  704 */       return result;
/*      */     }
/*  706 */     return limit;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Beta
/*      */   public static long saturatedPow(long b, int k)
/*      */   {
/*  717 */     MathPreconditions.checkNonNegative("exponent", k);
/*  718 */     if (((b >= -2L ? 1 : 0) & (b <= 2L ? 1 : 0)) != 0) {
/*  719 */       switch ((int)b) {
/*      */       case 0: 
/*  721 */         return k == 0 ? 1L : 0L;
/*      */       case 1: 
/*  723 */         return 1L;
/*      */       case -1: 
/*  725 */         return (k & 0x1) == 0 ? 1L : -1L;
/*      */       case 2: 
/*  727 */         if (k >= 63) {
/*  728 */           return Long.MAX_VALUE;
/*      */         }
/*  730 */         return 1L << k;
/*      */       case -2: 
/*  732 */         if (k >= 64) {
/*  733 */           return Long.MAX_VALUE + (k & 0x1);
/*      */         }
/*  735 */         return (k & 0x1) == 0 ? 1L << k : -1L << k;
/*      */       }
/*  737 */       throw new AssertionError();
/*      */     }
/*      */     
/*  740 */     long accum = 1L;
/*      */     
/*  742 */     long limit = Long.MAX_VALUE + (b >>> 63 & k & 0x1);
/*      */     for (;;) {
/*  744 */       switch (k) {
/*      */       case 0: 
/*  746 */         return accum;
/*      */       case 1: 
/*  748 */         return saturatedMultiply(accum, b);
/*      */       }
/*  750 */       if ((k & 0x1) != 0) {
/*  751 */         accum = saturatedMultiply(accum, b);
/*      */       }
/*  753 */       k >>= 1;
/*  754 */       if (k > 0) {
/*  755 */         if (((-3037000499L > b ? 1 : 0) | (b > 3037000499L ? 1 : 0)) != 0) {
/*  756 */           return limit;
/*      */         }
/*  758 */         b *= b;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   public static long factorial(int n)
/*      */   {
/*  774 */     MathPreconditions.checkNonNegative("n", n);
/*  775 */     return n < factorials.length ? factorials[n] : Long.MAX_VALUE;
/*      */   }
/*      */   
/*  778 */   static final long[] factorials = { 1L, 1L, 2L, 6L, 24L, 120L, 720L, 5040L, 40320L, 362880L, 3628800L, 39916800L, 479001600L, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L, 121645100408832000L, 2432902008176640000L };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static long binomial(int n, int k)
/*      */   {
/*  809 */     MathPreconditions.checkNonNegative("n", n);
/*  810 */     MathPreconditions.checkNonNegative("k", k);
/*  811 */     Preconditions.checkArgument(k <= n, "k (%s) > n (%s)", k, n);
/*  812 */     if (k > n >> 1) {
/*  813 */       k = n - k;
/*      */     }
/*  815 */     switch (k) {
/*      */     case 0: 
/*  817 */       return 1L;
/*      */     case 1: 
/*  819 */       return n;
/*      */     }
/*  821 */     if (n < factorials.length)
/*  822 */       return factorials[n] / (factorials[k] * factorials[(n - k)]);
/*  823 */     if ((k >= biggestBinomials.length) || (n > biggestBinomials[k]))
/*  824 */       return Long.MAX_VALUE;
/*  825 */     if ((k < biggestSimpleBinomials.length) && (n <= biggestSimpleBinomials[k]))
/*      */     {
/*  827 */       long result = n--;
/*  828 */       for (int i = 2; i <= k; i++) {
/*  829 */         result *= n;
/*  830 */         result /= i;n--;
/*      */       }
/*      */       
/*  832 */       return result;
/*      */     }
/*  834 */     int nBits = log2(n, RoundingMode.CEILING);
/*      */     
/*  836 */     long result = 1L;
/*  837 */     long numerator = n--;
/*  838 */     long denominator = 1L;
/*      */     
/*  840 */     int numeratorBits = nBits;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  848 */     for (int i = 2; i <= k; n--) {
/*  849 */       if (numeratorBits + nBits < 63)
/*      */       {
/*  851 */         numerator *= n;
/*  852 */         denominator *= i;
/*  853 */         numeratorBits += nBits;
/*      */       }
/*      */       else
/*      */       {
/*  857 */         result = multiplyFraction(result, numerator, denominator);
/*  858 */         numerator = n;
/*  859 */         denominator = i;
/*  860 */         numeratorBits = nBits;
/*      */       }
/*  848 */       i++;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  863 */     return multiplyFraction(result, numerator, denominator);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static long multiplyFraction(long x, long numerator, long denominator)
/*      */   {
/*  872 */     if (x == 1L) {
/*  873 */       return numerator / denominator;
/*      */     }
/*  875 */     long commonDivisor = gcd(x, denominator);
/*  876 */     x /= commonDivisor;
/*  877 */     denominator /= commonDivisor;
/*      */     
/*      */ 
/*  880 */     return x * (numerator / denominator);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  886 */   static final int[] biggestBinomials = { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 3810779, 121977, 16175, 4337, 1733, 887, 534, 361, 265, 206, 169, 143, 125, 111, 101, 94, 88, 83, 79, 76, 74, 72, 70, 69, 68, 67, 67, 66, 66, 66, 66 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @VisibleForTesting
/*  928 */   static final int[] biggestSimpleBinomials = { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 2642246, 86251, 11724, 3218, 1313, 684, 419, 287, 214, 169, 139, 119, 105, 95, 87, 81, 76, 73, 70, 68, 66, 64, 63, 62, 62, 61, 61, 61 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int SIEVE_30 = -545925251;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static boolean fitsInInt(long x)
/*      */   {
/*  965 */     return (int)x == x;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static long mean(long x, long y)
/*      */   {
/*  978 */     return (x & y) + ((x ^ y) >> 1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   @Beta
/*      */   public static boolean isPrime(long n)
/*      */   {
/* 1005 */     if (n < 2L) {
/* 1006 */       MathPreconditions.checkNonNegative("n", n);
/* 1007 */       return false;
/*      */     }
/* 1009 */     if ((n == 2L) || (n == 3L) || (n == 5L) || (n == 7L) || (n == 11L) || (n == 13L)) {
/* 1010 */       return true;
/*      */     }
/*      */     
/* 1013 */     if ((0xDF75D77D & 1 << (int)(n % 30L)) != 0) {
/* 1014 */       return false;
/*      */     }
/* 1016 */     if ((n % 7L == 0L) || (n % 11L == 0L) || (n % 13L == 0L)) {
/* 1017 */       return false;
/*      */     }
/* 1019 */     if (n < 289L) {
/* 1020 */       return true;
/*      */     }
/*      */     
/* 1023 */     for (long[] baseSet : millerRabinBaseSets) {
/* 1024 */       if (n <= baseSet[0]) {
/* 1025 */         for (int i = 1; i < baseSet.length; i++) {
/* 1026 */           if (!MillerRabinTester.test(baseSet[i], n)) {
/* 1027 */             return false;
/*      */           }
/*      */         }
/* 1030 */         return true;
/*      */       }
/*      */     }
/* 1033 */     throw new AssertionError();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1043 */   private static final long[][] millerRabinBaseSets = { { 291830L, 126401071349994536L }, { 885594168L, 725270293939359937L, 3569819667048198375L }, { 273919523040L, 15L, 7363882082L, 992620450144556L }, { 47636622961200L, 2L, 2570940L, 211991001L, 3749873356L }, { 7999252175582850L, 2L, 4130806001517L, 149795463772692060L, 186635894390467037L, 3967304179347715805L }, { 585226005592931976L, 2L, 123635709730000L, 9233062284813009L, 43835965440333360L, 761179012939631437L, 1263739024124850375L }, { Long.MAX_VALUE, 2L, 325L, 9375L, 28178L, 450775L, 9780504L, 1795265022L } };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static abstract enum MillerRabinTester
/*      */   {
/* 1070 */     SMALL, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1090 */     LARGE;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private MillerRabinTester() {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     static boolean test(long base, long n)
/*      */     {
/* 1170 */       return (n <= 3037000499L ? SMALL : LARGE).testWitness(base, n);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     abstract long mulMod(long paramLong1, long paramLong2, long paramLong3);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     abstract long squareMod(long paramLong1, long paramLong2);
/*      */     
/*      */ 
/*      */ 
/*      */     private long powMod(long a, long p, long m)
/*      */     {
/* 1187 */       long res = 1L;
/* 1188 */       for (; p != 0L; p >>= 1) {
/* 1189 */         if ((p & 1L) != 0L) {
/* 1190 */           res = mulMod(res, a, m);
/*      */         }
/* 1192 */         a = squareMod(a, m);
/*      */       }
/* 1194 */       return res;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private boolean testWitness(long base, long n)
/*      */     {
/* 1201 */       int r = Long.numberOfTrailingZeros(n - 1L);
/* 1202 */       long d = n - 1L >> r;
/* 1203 */       base %= n;
/* 1204 */       if (base == 0L) {
/* 1205 */         return true;
/*      */       }
/*      */       
/* 1208 */       long a = powMod(base, d, n);
/*      */       
/*      */ 
/*      */ 
/* 1212 */       if (a == 1L) {
/* 1213 */         return true;
/*      */       }
/* 1215 */       int j = 0;
/* 1216 */       while (a != n - 1L) {
/* 1217 */         j++; if (j == r) {
/* 1218 */           return false;
/*      */         }
/* 1220 */         a = squareMod(a, n);
/*      */       }
/* 1222 */       return true;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\math\LongMath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */