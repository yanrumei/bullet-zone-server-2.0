/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import java.math.RoundingMode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class IntMath
/*     */ {
/*     */   @VisibleForTesting
/*     */   static final int MAX_SIGNED_POWER_OF_TWO = 1073741824;
/*     */   @VisibleForTesting
/*     */   static final int MAX_POWER_OF_SQRT2_UNSIGNED = -1257966797;
/*     */   
/*     */   @Beta
/*     */   public static int ceilingPowerOfTwo(int x)
/*     */   {
/*  67 */     MathPreconditions.checkPositive("x", x);
/*  68 */     if (x > 1073741824) {
/*  69 */       throw new ArithmeticException("ceilingPowerOfTwo(" + x + ") not representable as an int");
/*     */     }
/*  71 */     return 1 << -Integer.numberOfLeadingZeros(x - 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public static int floorPowerOfTwo(int x)
/*     */   {
/*  83 */     MathPreconditions.checkPositive("x", x);
/*  84 */     return Integer.highestOneBit(x);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isPowerOfTwo(int x)
/*     */   {
/*  95 */     return (x > 0 ? 1 : 0) & ((x & x - 1) == 0 ? 1 : 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @VisibleForTesting
/*     */   static int lessThanBranchFree(int x, int y)
/*     */   {
/* 107 */     return (x - y ^ 0xFFFFFFFF ^ 0xFFFFFFFF) >>> 31;
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
/*     */   public static int log2(int x, RoundingMode mode)
/*     */   {
/* 120 */     MathPreconditions.checkPositive("x", x);
/* 121 */     switch (mode) {
/*     */     case UNNECESSARY: 
/* 123 */       MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
/*     */     
/*     */     case DOWN: 
/*     */     case FLOOR: 
/* 127 */       return 31 - Integer.numberOfLeadingZeros(x);
/*     */     
/*     */     case UP: 
/*     */     case CEILING: 
/* 131 */       return 32 - Integer.numberOfLeadingZeros(x - 1);
/*     */     
/*     */ 
/*     */     case HALF_DOWN: 
/*     */     case HALF_UP: 
/*     */     case HALF_EVEN: 
/* 137 */       int leadingZeros = Integer.numberOfLeadingZeros(x);
/* 138 */       int cmp = -1257966797 >>> leadingZeros;
/*     */       
/* 140 */       int logFloor = 31 - leadingZeros;
/* 141 */       return logFloor + lessThanBranchFree(cmp, x);
/*     */     }
/*     */     
/* 144 */     throw new AssertionError();
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
/*     */   @GwtIncompatible
/*     */   public static int log10(int x, RoundingMode mode)
/*     */   {
/* 161 */     MathPreconditions.checkPositive("x", x);
/* 162 */     int logFloor = log10Floor(x);
/* 163 */     int floorPow = powersOf10[logFloor];
/* 164 */     switch (mode) {
/*     */     case UNNECESSARY: 
/* 166 */       MathPreconditions.checkRoundingUnnecessary(x == floorPow);
/*     */     
/*     */     case DOWN: 
/*     */     case FLOOR: 
/* 170 */       return logFloor;
/*     */     case UP: 
/*     */     case CEILING: 
/* 173 */       return logFloor + lessThanBranchFree(floorPow, x);
/*     */     
/*     */     case HALF_DOWN: 
/*     */     case HALF_UP: 
/*     */     case HALF_EVEN: 
/* 178 */       return logFloor + lessThanBranchFree(halfPowersOf10[logFloor], x);
/*     */     }
/* 180 */     throw new AssertionError();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int log10Floor(int x)
/*     */   {
/* 192 */     int y = maxLog10ForLeadingZeros[Integer.numberOfLeadingZeros(x)];
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 197 */     return y - lessThanBranchFree(x, powersOf10[y]);
/*     */   }
/*     */   
/*     */ 
/*     */   @VisibleForTesting
/* 202 */   static final byte[] maxLog10ForLeadingZeros = { 9, 9, 9, 8, 8, 8, 7, 7, 7, 6, 6, 6, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2, 1, 1, 1, 0, 0, 0, 0 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @VisibleForTesting
/* 208 */   static final int[] powersOf10 = { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @VisibleForTesting
/* 214 */   static final int[] halfPowersOf10 = { 3, 31, 316, 3162, 31622, 316227, 3162277, 31622776, 316227766, Integer.MAX_VALUE };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @VisibleForTesting
/*     */   static final int FLOOR_SQRT_MAX_INT = 46340;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   public static int pow(int b, int k)
/*     */   {
/* 229 */     MathPreconditions.checkNonNegative("exponent", k);
/* 230 */     switch (b) {
/*     */     case 0: 
/* 232 */       return k == 0 ? 1 : 0;
/*     */     case 1: 
/* 234 */       return 1;
/*     */     case -1: 
/* 236 */       return (k & 0x1) == 0 ? 1 : -1;
/*     */     case 2: 
/* 238 */       return k < 32 ? 1 << k : 0;
/*     */     case -2: 
/* 240 */       if (k < 32) {
/* 241 */         return (k & 0x1) == 0 ? 1 << k : -(1 << k);
/*     */       }
/* 243 */       return 0;
/*     */     }
/*     */     
/*     */     
/*     */ 
/* 248 */     for (int accum = 1;; k >>= 1) {
/* 249 */       switch (k) {
/*     */       case 0: 
/* 251 */         return accum;
/*     */       case 1: 
/* 253 */         return b * accum;
/*     */       }
/* 255 */       accum *= ((k & 0x1) == 0 ? 1 : b);
/* 256 */       b *= b;
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
/*     */   @GwtIncompatible
/*     */   public static int sqrt(int x, RoundingMode mode)
/*     */   {
/* 271 */     MathPreconditions.checkNonNegative("x", x);
/* 272 */     int sqrtFloor = sqrtFloor(x);
/* 273 */     switch (mode) {
/*     */     case UNNECESSARY: 
/* 275 */       MathPreconditions.checkRoundingUnnecessary(sqrtFloor * sqrtFloor == x);
/*     */     case DOWN: 
/*     */     case FLOOR: 
/* 278 */       return sqrtFloor;
/*     */     case UP: 
/*     */     case CEILING: 
/* 281 */       return sqrtFloor + lessThanBranchFree(sqrtFloor * sqrtFloor, x);
/*     */     case HALF_DOWN: 
/*     */     case HALF_UP: 
/*     */     case HALF_EVEN: 
/* 285 */       int halfSquare = sqrtFloor * sqrtFloor + sqrtFloor;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 297 */       return sqrtFloor + lessThanBranchFree(halfSquare, x);
/*     */     }
/* 299 */     throw new AssertionError();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static int sqrtFloor(int x)
/*     */   {
/* 306 */     return (int)Math.sqrt(x);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int divide(int p, int q, RoundingMode mode)
/*     */   {
/* 318 */     Preconditions.checkNotNull(mode);
/* 319 */     if (q == 0) {
/* 320 */       throw new ArithmeticException("/ by zero");
/*     */     }
/* 322 */     int div = p / q;
/* 323 */     int rem = p - q * div;
/*     */     
/* 325 */     if (rem == 0) {
/* 326 */       return div;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 336 */     int signum = 0x1 | (p ^ q) >> 31;
/*     */     boolean increment;
/* 338 */     boolean increment; boolean increment; boolean increment; boolean increment; switch (mode) {
/*     */     case UNNECESSARY: 
/* 340 */       MathPreconditions.checkRoundingUnnecessary(rem == 0);
/*     */     
/*     */     case DOWN: 
/* 343 */       increment = false;
/* 344 */       break;
/*     */     case UP: 
/* 346 */       increment = true;
/* 347 */       break;
/*     */     case CEILING: 
/* 349 */       increment = signum > 0;
/* 350 */       break;
/*     */     case FLOOR: 
/* 352 */       increment = signum < 0;
/* 353 */       break;
/*     */     case HALF_DOWN: 
/*     */     case HALF_UP: 
/*     */     case HALF_EVEN: 
/* 357 */       int absRem = Math.abs(rem);
/* 358 */       int cmpRemToHalfDivisor = absRem - (Math.abs(q) - absRem);
/*     */       
/*     */       boolean increment;
/* 361 */       if (cmpRemToHalfDivisor == 0) {
/* 362 */         if (mode != RoundingMode.HALF_UP) {} increment = ((mode == RoundingMode.HALF_EVEN ? 1 : 0) & ((div & 0x1) != 0 ? 1 : 0)) != 0;
/*     */       } else {
/* 364 */         increment = cmpRemToHalfDivisor > 0;
/*     */       }
/* 366 */       break;
/*     */     default: 
/* 368 */       throw new AssertionError(); }
/*     */     boolean increment;
/* 370 */     return increment ? div + signum : div;
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
/*     */   public static int mod(int x, int m)
/*     */   {
/* 390 */     if (m <= 0) {
/* 391 */       throw new ArithmeticException("Modulus " + m + " must be > 0");
/*     */     }
/* 393 */     int result = x % m;
/* 394 */     return result >= 0 ? result : result + m;
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
/*     */   public static int gcd(int a, int b)
/*     */   {
/* 409 */     MathPreconditions.checkNonNegative("a", a);
/* 410 */     MathPreconditions.checkNonNegative("b", b);
/* 411 */     if (a == 0)
/*     */     {
/*     */ 
/* 414 */       return b; }
/* 415 */     if (b == 0) {
/* 416 */       return a;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 422 */     int aTwos = Integer.numberOfTrailingZeros(a);
/* 423 */     a >>= aTwos;
/* 424 */     int bTwos = Integer.numberOfTrailingZeros(b);
/* 425 */     b >>= bTwos;
/* 426 */     while (a != b)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 434 */       int delta = a - b;
/*     */       
/* 436 */       int minDeltaOrZero = delta & delta >> 31;
/*     */       
/*     */ 
/* 439 */       a = delta - minDeltaOrZero - minDeltaOrZero;
/*     */       
/*     */ 
/* 442 */       b += minDeltaOrZero;
/* 443 */       a >>= Integer.numberOfTrailingZeros(a);
/*     */     }
/* 445 */     return a << Math.min(aTwos, bTwos);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int checkedAdd(int a, int b)
/*     */   {
/* 454 */     long result = a + b;
/* 455 */     MathPreconditions.checkNoOverflow(result == (int)result);
/* 456 */     return (int)result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int checkedSubtract(int a, int b)
/*     */   {
/* 465 */     long result = a - b;
/* 466 */     MathPreconditions.checkNoOverflow(result == (int)result);
/* 467 */     return (int)result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int checkedMultiply(int a, int b)
/*     */   {
/* 476 */     long result = a * b;
/* 477 */     MathPreconditions.checkNoOverflow(result == (int)result);
/* 478 */     return (int)result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int checkedPow(int b, int k)
/*     */   {
/* 490 */     MathPreconditions.checkNonNegative("exponent", k);
/* 491 */     switch (b) {
/*     */     case 0: 
/* 493 */       return k == 0 ? 1 : 0;
/*     */     case 1: 
/* 495 */       return 1;
/*     */     case -1: 
/* 497 */       return (k & 0x1) == 0 ? 1 : -1;
/*     */     case 2: 
/* 499 */       MathPreconditions.checkNoOverflow(k < 31);
/* 500 */       return 1 << k;
/*     */     case -2: 
/* 502 */       MathPreconditions.checkNoOverflow(k < 32);
/* 503 */       return (k & 0x1) == 0 ? 1 << k : -1 << k;
/*     */     }
/*     */     
/*     */     
/* 507 */     int accum = 1;
/*     */     for (;;) {
/* 509 */       switch (k) {
/*     */       case 0: 
/* 511 */         return accum;
/*     */       case 1: 
/* 513 */         return checkedMultiply(accum, b);
/*     */       }
/* 515 */       if ((k & 0x1) != 0) {
/* 516 */         accum = checkedMultiply(accum, b);
/*     */       }
/* 518 */       k >>= 1;
/* 519 */       if (k > 0) {
/* 520 */         MathPreconditions.checkNoOverflow((-46340 <= b ? 1 : 0) & (b <= 46340 ? 1 : 0));
/* 521 */         b *= b;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public static int saturatedAdd(int a, int b)
/*     */   {
/* 535 */     return Ints.saturatedCast(a + b);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public static int saturatedSubtract(int a, int b)
/*     */   {
/* 546 */     return Ints.saturatedCast(a - b);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public static int saturatedMultiply(int a, int b)
/*     */   {
/* 557 */     return Ints.saturatedCast(a * b);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public static int saturatedPow(int b, int k)
/*     */   {
/* 568 */     MathPreconditions.checkNonNegative("exponent", k);
/* 569 */     switch (b) {
/*     */     case 0: 
/* 571 */       return k == 0 ? 1 : 0;
/*     */     case 1: 
/* 573 */       return 1;
/*     */     case -1: 
/* 575 */       return (k & 0x1) == 0 ? 1 : -1;
/*     */     case 2: 
/* 577 */       if (k >= 31) {
/* 578 */         return Integer.MAX_VALUE;
/*     */       }
/* 580 */       return 1 << k;
/*     */     case -2: 
/* 582 */       if (k >= 32) {
/* 583 */         return Integer.MAX_VALUE + (k & 0x1);
/*     */       }
/* 585 */       return (k & 0x1) == 0 ? 1 << k : -1 << k;
/*     */     }
/*     */     
/*     */     
/* 589 */     int accum = 1;
/*     */     
/* 591 */     int limit = Integer.MAX_VALUE + (b >>> 31 & k & 0x1);
/*     */     for (;;) {
/* 593 */       switch (k) {
/*     */       case 0: 
/* 595 */         return accum;
/*     */       case 1: 
/* 597 */         return saturatedMultiply(accum, b);
/*     */       }
/* 599 */       if ((k & 0x1) != 0) {
/* 600 */         accum = saturatedMultiply(accum, b);
/*     */       }
/* 602 */       k >>= 1;
/* 603 */       if (k > 0) {
/* 604 */         if (((-46340 > b ? 1 : 0) | (b > 46340 ? 1 : 0)) != 0) {
/* 605 */           return limit;
/*     */         }
/* 607 */         b *= b;
/*     */       }
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
/*     */   public static int factorial(int n)
/*     */   {
/* 622 */     MathPreconditions.checkNonNegative("n", n);
/* 623 */     return n < factorials.length ? factorials[n] : Integer.MAX_VALUE;
/*     */   }
/*     */   
/* 626 */   private static final int[] factorials = { 1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static int binomial(int n, int k)
/*     */   {
/* 650 */     MathPreconditions.checkNonNegative("n", n);
/* 651 */     MathPreconditions.checkNonNegative("k", k);
/* 652 */     Preconditions.checkArgument(k <= n, "k (%s) > n (%s)", k, n);
/* 653 */     if (k > n >> 1) {
/* 654 */       k = n - k;
/*     */     }
/* 656 */     if ((k >= biggestBinomials.length) || (n > biggestBinomials[k])) {
/* 657 */       return Integer.MAX_VALUE;
/*     */     }
/* 659 */     switch (k) {
/*     */     case 0: 
/* 661 */       return 1;
/*     */     case 1: 
/* 663 */       return n;
/*     */     }
/* 665 */     long result = 1L;
/* 666 */     for (int i = 0; i < k; i++) {
/* 667 */       result *= (n - i);
/* 668 */       result /= (i + 1);
/*     */     }
/* 670 */     return (int)result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @VisibleForTesting
/* 676 */   static int[] biggestBinomials = { Integer.MAX_VALUE, Integer.MAX_VALUE, 65536, 2345, 477, 193, 110, 75, 58, 49, 43, 39, 37, 35, 34, 34, 33 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int mean(int x, int y)
/*     */   {
/* 706 */     return (x & y) + ((x ^ y) >> 1);
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
/*     */   @GwtIncompatible
/*     */   @Beta
/*     */   public static boolean isPrime(int n)
/*     */   {
/* 724 */     return LongMath.isPrime(n);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\math\IntMath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */