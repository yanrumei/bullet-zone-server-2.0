/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.math.BigInteger;
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
/*     */ @GwtCompatible(emulated=true)
/*     */ public final class UnsignedInteger
/*     */   extends Number
/*     */   implements Comparable<UnsignedInteger>
/*     */ {
/*  43 */   public static final UnsignedInteger ZERO = fromIntBits(0);
/*  44 */   public static final UnsignedInteger ONE = fromIntBits(1);
/*  45 */   public static final UnsignedInteger MAX_VALUE = fromIntBits(-1);
/*     */   
/*     */   private final int value;
/*     */   
/*     */   private UnsignedInteger(int value)
/*     */   {
/*  51 */     this.value = (value & 0xFFFFFFFF);
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
/*     */   public static UnsignedInteger fromIntBits(int bits)
/*     */   {
/*  67 */     return new UnsignedInteger(bits);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static UnsignedInteger valueOf(long value)
/*     */   {
/*  75 */     Preconditions.checkArgument((value & 0xFFFFFFFF) == value, "value (%s) is outside the range for an unsigned integer value", value);
/*     */     
/*     */ 
/*     */ 
/*  79 */     return fromIntBits((int)value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static UnsignedInteger valueOf(BigInteger value)
/*     */   {
/*  89 */     Preconditions.checkNotNull(value);
/*  90 */     Preconditions.checkArgument(
/*  91 */       (value.signum() >= 0) && (value.bitLength() <= 32), "value (%s) is outside the range for an unsigned integer value", value);
/*     */     
/*     */ 
/*  94 */     return fromIntBits(value.intValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static UnsignedInteger valueOf(String string)
/*     */   {
/* 105 */     return valueOf(string, 10);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static UnsignedInteger valueOf(String string, int radix)
/*     */   {
/* 116 */     return fromIntBits(UnsignedInts.parseUnsignedInt(string, radix));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UnsignedInteger plus(UnsignedInteger val)
/*     */   {
/* 126 */     return fromIntBits(this.value + ((UnsignedInteger)Preconditions.checkNotNull(val)).value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UnsignedInteger minus(UnsignedInteger val)
/*     */   {
/* 136 */     return fromIntBits(this.value - ((UnsignedInteger)Preconditions.checkNotNull(val)).value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   public UnsignedInteger times(UnsignedInteger val)
/*     */   {
/* 148 */     return fromIntBits(this.value * ((UnsignedInteger)Preconditions.checkNotNull(val)).value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UnsignedInteger dividedBy(UnsignedInteger val)
/*     */   {
/* 158 */     return fromIntBits(UnsignedInts.divide(this.value, ((UnsignedInteger)Preconditions.checkNotNull(val)).value));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UnsignedInteger mod(UnsignedInteger val)
/*     */   {
/* 168 */     return fromIntBits(UnsignedInts.remainder(this.value, ((UnsignedInteger)Preconditions.checkNotNull(val)).value));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int intValue()
/*     */   {
/* 180 */     return this.value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long longValue()
/*     */   {
/* 188 */     return UnsignedInts.toLong(this.value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public float floatValue()
/*     */   {
/* 197 */     return (float)longValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double doubleValue()
/*     */   {
/* 206 */     return longValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public BigInteger bigIntegerValue()
/*     */   {
/* 213 */     return BigInteger.valueOf(longValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int compareTo(UnsignedInteger other)
/*     */   {
/* 223 */     Preconditions.checkNotNull(other);
/* 224 */     return UnsignedInts.compare(this.value, other.value);
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 229 */     return this.value;
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object obj)
/*     */   {
/* 234 */     if ((obj instanceof UnsignedInteger)) {
/* 235 */       UnsignedInteger other = (UnsignedInteger)obj;
/* 236 */       return this.value == other.value;
/*     */     }
/* 238 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 246 */     return toString(10);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString(int radix)
/*     */   {
/* 255 */     return UnsignedInts.toString(this.value, radix);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\primitives\UnsignedInteger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */