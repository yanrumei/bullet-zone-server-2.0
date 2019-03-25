/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.MoreObjects.ToStringHelper;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class PairedStats
/*     */   implements Serializable
/*     */ {
/*     */   private final Stats xStats;
/*     */   private final Stats yStats;
/*     */   private final double sumOfProductsOfDeltas;
/*     */   private static final int BYTES = 88;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   PairedStats(Stats xStats, Stats yStats, double sumOfProductsOfDeltas)
/*     */   {
/*  61 */     this.xStats = xStats;
/*  62 */     this.yStats = yStats;
/*  63 */     this.sumOfProductsOfDeltas = sumOfProductsOfDeltas;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long count()
/*     */   {
/*  70 */     return this.xStats.count();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Stats xStats()
/*     */   {
/*  77 */     return this.xStats;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Stats yStats()
/*     */   {
/*  84 */     return this.yStats;
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
/*     */   public double populationCovariance()
/*     */   {
/* 102 */     Preconditions.checkState(count() != 0L);
/* 103 */     return this.sumOfProductsOfDeltas / count();
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
/*     */   public double sampleCovariance()
/*     */   {
/* 120 */     Preconditions.checkState(count() > 1L);
/* 121 */     return this.sumOfProductsOfDeltas / (count() - 1L);
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
/*     */   public double pearsonsCorrelationCoefficient()
/*     */   {
/* 141 */     Preconditions.checkState(count() > 1L);
/* 142 */     if (Double.isNaN(this.sumOfProductsOfDeltas)) {
/* 143 */       return NaN.0D;
/*     */     }
/* 145 */     double xSumOfSquaresOfDeltas = xStats().sumOfSquaresOfDeltas();
/* 146 */     double ySumOfSquaresOfDeltas = yStats().sumOfSquaresOfDeltas();
/* 147 */     Preconditions.checkState(xSumOfSquaresOfDeltas > 0.0D);
/* 148 */     Preconditions.checkState(ySumOfSquaresOfDeltas > 0.0D);
/*     */     
/*     */ 
/*     */ 
/* 152 */     double productOfSumsOfSquaresOfDeltas = ensurePositive(xSumOfSquaresOfDeltas * ySumOfSquaresOfDeltas);
/* 153 */     return ensureInUnitRange(this.sumOfProductsOfDeltas / Math.sqrt(productOfSumsOfSquaresOfDeltas));
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
/*     */   public LinearTransformation leastSquaresFit()
/*     */   {
/* 188 */     Preconditions.checkState(count() > 1L);
/* 189 */     if (Double.isNaN(this.sumOfProductsOfDeltas)) {
/* 190 */       return LinearTransformation.forNaN();
/*     */     }
/* 192 */     double xSumOfSquaresOfDeltas = this.xStats.sumOfSquaresOfDeltas();
/* 193 */     if (xSumOfSquaresOfDeltas > 0.0D) {
/* 194 */       if (this.yStats.sumOfSquaresOfDeltas() > 0.0D) {
/* 195 */         return 
/* 196 */           LinearTransformation.mapping(this.xStats.mean(), this.yStats.mean()).withSlope(this.sumOfProductsOfDeltas / xSumOfSquaresOfDeltas);
/*     */       }
/* 198 */       return LinearTransformation.horizontal(this.yStats.mean());
/*     */     }
/*     */     
/* 201 */     Preconditions.checkState(this.yStats.sumOfSquaresOfDeltas() > 0.0D);
/* 202 */     return LinearTransformation.vertical(this.xStats.mean());
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
/*     */   public boolean equals(@Nullable Object obj)
/*     */   {
/* 217 */     if (obj == null) {
/* 218 */       return false;
/*     */     }
/* 220 */     if (getClass() != obj.getClass()) {
/* 221 */       return false;
/*     */     }
/* 223 */     PairedStats other = (PairedStats)obj;
/* 224 */     if ((this.xStats.equals(other.xStats)) && 
/* 225 */       (this.yStats.equals(other.yStats))) {}
/* 224 */     return 
/*     */     
/*     */ 
/* 227 */       Double.doubleToLongBits(this.sumOfProductsOfDeltas) == Double.doubleToLongBits(other.sumOfProductsOfDeltas);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 238 */     return Objects.hashCode(new Object[] { this.xStats, this.yStats, Double.valueOf(this.sumOfProductsOfDeltas) });
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 243 */     if (count() > 0L) {
/* 244 */       return 
/*     */       
/*     */ 
/*     */ 
/* 248 */         MoreObjects.toStringHelper(this).add("xStats", this.xStats).add("yStats", this.yStats).add("populationCovariance", populationCovariance()).toString();
/*     */     }
/* 250 */     return 
/*     */     
/*     */ 
/* 253 */       MoreObjects.toStringHelper(this).add("xStats", this.xStats).add("yStats", this.yStats).toString();
/*     */   }
/*     */   
/*     */   double sumOfProductsOfDeltas()
/*     */   {
/* 258 */     return this.sumOfProductsOfDeltas;
/*     */   }
/*     */   
/*     */   private static double ensurePositive(double value) {
/* 262 */     if (value > 0.0D) {
/* 263 */       return value;
/*     */     }
/* 265 */     return Double.MIN_VALUE;
/*     */   }
/*     */   
/*     */   private static double ensureInUnitRange(double value)
/*     */   {
/* 270 */     if (value >= 1.0D) {
/* 271 */       return 1.0D;
/*     */     }
/* 273 */     if (value <= -1.0D) {
/* 274 */       return -1.0D;
/*     */     }
/* 276 */     return value;
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
/*     */   public byte[] toByteArray()
/*     */   {
/* 293 */     ByteBuffer buffer = ByteBuffer.allocate(88).order(ByteOrder.LITTLE_ENDIAN);
/* 294 */     this.xStats.writeTo(buffer);
/* 295 */     this.yStats.writeTo(buffer);
/* 296 */     buffer.putDouble(this.sumOfProductsOfDeltas);
/* 297 */     return buffer.array();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static PairedStats fromByteArray(byte[] byteArray)
/*     */   {
/* 308 */     Preconditions.checkNotNull(byteArray);
/* 309 */     Preconditions.checkArgument(byteArray.length == 88, "Expected PairedStats.BYTES = %s, got %s", 88, byteArray.length);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 314 */     ByteBuffer buffer = ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN);
/* 315 */     Stats xStats = Stats.readFrom(buffer);
/* 316 */     Stats yStats = Stats.readFrom(buffer);
/* 317 */     double sumOfProductsOfDeltas = buffer.getDouble();
/* 318 */     return new PairedStats(xStats, yStats, sumOfProductsOfDeltas);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\math\PairedStats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */