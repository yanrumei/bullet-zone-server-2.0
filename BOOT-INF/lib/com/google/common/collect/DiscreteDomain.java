/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.math.BigInteger;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class DiscreteDomain<C extends Comparable>
/*     */ {
/*     */   final boolean supportsFastOffset;
/*     */   
/*     */   public static DiscreteDomain<Integer> integers()
/*     */   {
/*  56 */     return IntegerDomain.INSTANCE;
/*     */   }
/*     */   
/*     */   private static final class IntegerDomain extends DiscreteDomain<Integer> implements Serializable {
/*  60 */     private static final IntegerDomain INSTANCE = new IntegerDomain();
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*  63 */     IntegerDomain() { super(null); }
/*     */     
/*     */ 
/*     */     public Integer next(Integer value)
/*     */     {
/*  68 */       int i = value.intValue();
/*  69 */       return i == Integer.MAX_VALUE ? null : Integer.valueOf(i + 1);
/*     */     }
/*     */     
/*     */     public Integer previous(Integer value)
/*     */     {
/*  74 */       int i = value.intValue();
/*  75 */       return i == Integer.MIN_VALUE ? null : Integer.valueOf(i - 1);
/*     */     }
/*     */     
/*     */     Integer offset(Integer origin, long distance)
/*     */     {
/*  80 */       CollectPreconditions.checkNonnegative(distance, "distance");
/*  81 */       return Integer.valueOf(Ints.checkedCast(origin.longValue() + distance));
/*     */     }
/*     */     
/*     */     public long distance(Integer start, Integer end)
/*     */     {
/*  86 */       return end.intValue() - start.intValue();
/*     */     }
/*     */     
/*     */     public Integer minValue()
/*     */     {
/*  91 */       return Integer.valueOf(Integer.MIN_VALUE);
/*     */     }
/*     */     
/*     */     public Integer maxValue()
/*     */     {
/*  96 */       return Integer.valueOf(Integer.MAX_VALUE);
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 100 */       return INSTANCE;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 105 */       return "DiscreteDomain.integers()";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static DiscreteDomain<Long> longs()
/*     */   {
/* 117 */     return LongDomain.INSTANCE;
/*     */   }
/*     */   
/*     */   private static final class LongDomain extends DiscreteDomain<Long> implements Serializable {
/* 121 */     private static final LongDomain INSTANCE = new LongDomain();
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 124 */     LongDomain() { super(null); }
/*     */     
/*     */ 
/*     */     public Long next(Long value)
/*     */     {
/* 129 */       long l = value.longValue();
/* 130 */       return l == Long.MAX_VALUE ? null : Long.valueOf(l + 1L);
/*     */     }
/*     */     
/*     */     public Long previous(Long value)
/*     */     {
/* 135 */       long l = value.longValue();
/* 136 */       return l == Long.MIN_VALUE ? null : Long.valueOf(l - 1L);
/*     */     }
/*     */     
/*     */     Long offset(Long origin, long distance)
/*     */     {
/* 141 */       CollectPreconditions.checkNonnegative(distance, "distance");
/* 142 */       long result = origin.longValue() + distance;
/* 143 */       if (result < 0L) {
/* 144 */         Preconditions.checkArgument(origin.longValue() < 0L, "overflow");
/*     */       }
/* 146 */       return Long.valueOf(result);
/*     */     }
/*     */     
/*     */     public long distance(Long start, Long end)
/*     */     {
/* 151 */       long result = end.longValue() - start.longValue();
/* 152 */       if ((end.longValue() > start.longValue()) && (result < 0L)) {
/* 153 */         return Long.MAX_VALUE;
/*     */       }
/* 155 */       if ((end.longValue() < start.longValue()) && (result > 0L)) {
/* 156 */         return Long.MIN_VALUE;
/*     */       }
/* 158 */       return result;
/*     */     }
/*     */     
/*     */     public Long minValue()
/*     */     {
/* 163 */       return Long.valueOf(Long.MIN_VALUE);
/*     */     }
/*     */     
/*     */     public Long maxValue()
/*     */     {
/* 168 */       return Long.valueOf(Long.MAX_VALUE);
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 172 */       return INSTANCE;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 177 */       return "DiscreteDomain.longs()";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static DiscreteDomain<BigInteger> bigIntegers()
/*     */   {
/* 189 */     return BigIntegerDomain.INSTANCE;
/*     */   }
/*     */   
/*     */   private static final class BigIntegerDomain extends DiscreteDomain<BigInteger> implements Serializable
/*     */   {
/* 194 */     private static final BigIntegerDomain INSTANCE = new BigIntegerDomain();
/*     */     
/*     */     BigIntegerDomain() {
/* 197 */       super(null);
/*     */     }
/*     */     
/* 200 */     private static final BigInteger MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);
/* 201 */     private static final BigInteger MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public BigInteger next(BigInteger value) {
/* 205 */       return value.add(BigInteger.ONE);
/*     */     }
/*     */     
/*     */     public BigInteger previous(BigInteger value)
/*     */     {
/* 210 */       return value.subtract(BigInteger.ONE);
/*     */     }
/*     */     
/*     */     BigInteger offset(BigInteger origin, long distance)
/*     */     {
/* 215 */       CollectPreconditions.checkNonnegative(distance, "distance");
/* 216 */       return origin.add(BigInteger.valueOf(distance));
/*     */     }
/*     */     
/*     */     public long distance(BigInteger start, BigInteger end)
/*     */     {
/* 221 */       return end.subtract(start).max(MIN_LONG).min(MAX_LONG).longValue();
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 225 */       return INSTANCE;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 230 */       return "DiscreteDomain.bigIntegers()";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected DiscreteDomain()
/*     */   {
/* 240 */     this(false);
/*     */   }
/*     */   
/*     */   private DiscreteDomain(boolean supportsFastOffset)
/*     */   {
/* 245 */     this.supportsFastOffset = supportsFastOffset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   C offset(C origin, long distance)
/*     */   {
/* 253 */     CollectPreconditions.checkNonnegative(distance, "distance");
/* 254 */     for (long i = 0L; i < distance; i += 1L) {
/* 255 */       origin = next(origin);
/*     */     }
/* 257 */     return origin;
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
/*     */   public abstract C next(C paramC);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract C previous(C paramC);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract long distance(C paramC1, C paramC2);
/*     */   
/*     */ 
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
/*     */   public C minValue()
/*     */   {
/* 312 */     throw new NoSuchElementException();
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
/*     */   public C maxValue()
/*     */   {
/* 328 */     throw new NoSuchElementException();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\DiscreteDomain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */