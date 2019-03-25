/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Doubles;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class PairedStatsAccumulator
/*     */ {
/*  38 */   private final StatsAccumulator xStats = new StatsAccumulator();
/*  39 */   private final StatsAccumulator yStats = new StatsAccumulator();
/*  40 */   private double sumOfProductsOfDeltas = 0.0D;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void add(double x, double y)
/*     */   {
/*  57 */     this.xStats.add(x);
/*  58 */     if ((Doubles.isFinite(x)) && (Doubles.isFinite(y))) {
/*  59 */       if (this.xStats.count() > 1L) {
/*  60 */         this.sumOfProductsOfDeltas += (x - this.xStats.mean()) * (y - this.yStats.mean());
/*     */       }
/*     */     } else {
/*  63 */       this.sumOfProductsOfDeltas = NaN.0D;
/*     */     }
/*  65 */     this.yStats.add(y);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addAll(PairedStats values)
/*     */   {
/*  73 */     if (values.count() == 0L) {
/*  74 */       return;
/*     */     }
/*     */     
/*  77 */     this.xStats.addAll(values.xStats());
/*  78 */     if (this.yStats.count() == 0L) {
/*  79 */       this.sumOfProductsOfDeltas = values.sumOfProductsOfDeltas();
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/*     */ 
/*  88 */       this.sumOfProductsOfDeltas = (this.sumOfProductsOfDeltas + (values.sumOfProductsOfDeltas() + (values.xStats().mean() - this.xStats.mean()) * (values.yStats().mean() - this.yStats.mean()) * values.count()));
/*     */     }
/*  90 */     this.yStats.addAll(values.yStats());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public PairedStats snapshot()
/*     */   {
/*  97 */     return new PairedStats(this.xStats.snapshot(), this.yStats.snapshot(), this.sumOfProductsOfDeltas);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long count()
/*     */   {
/* 104 */     return this.xStats.count();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Stats xStats()
/*     */   {
/* 111 */     return this.xStats.snapshot();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Stats yStats()
/*     */   {
/* 118 */     return this.yStats.snapshot();
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
/* 136 */     Preconditions.checkState(count() != 0L);
/* 137 */     return this.sumOfProductsOfDeltas / count();
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
/*     */   public final double sampleCovariance()
/*     */   {
/* 154 */     Preconditions.checkState(count() > 1L);
/* 155 */     return this.sumOfProductsOfDeltas / (count() - 1L);
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
/*     */   public final double pearsonsCorrelationCoefficient()
/*     */   {
/* 175 */     Preconditions.checkState(count() > 1L);
/* 176 */     if (Double.isNaN(this.sumOfProductsOfDeltas)) {
/* 177 */       return NaN.0D;
/*     */     }
/* 179 */     double xSumOfSquaresOfDeltas = this.xStats.sumOfSquaresOfDeltas();
/* 180 */     double ySumOfSquaresOfDeltas = this.yStats.sumOfSquaresOfDeltas();
/* 181 */     Preconditions.checkState(xSumOfSquaresOfDeltas > 0.0D);
/* 182 */     Preconditions.checkState(ySumOfSquaresOfDeltas > 0.0D);
/*     */     
/*     */ 
/*     */ 
/* 186 */     double productOfSumsOfSquaresOfDeltas = ensurePositive(xSumOfSquaresOfDeltas * ySumOfSquaresOfDeltas);
/* 187 */     return ensureInUnitRange(this.sumOfProductsOfDeltas / Math.sqrt(productOfSumsOfSquaresOfDeltas));
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
/*     */   public final LinearTransformation leastSquaresFit()
/*     */   {
/* 222 */     Preconditions.checkState(count() > 1L);
/* 223 */     if (Double.isNaN(this.sumOfProductsOfDeltas)) {
/* 224 */       return LinearTransformation.forNaN();
/*     */     }
/* 226 */     double xSumOfSquaresOfDeltas = this.xStats.sumOfSquaresOfDeltas();
/* 227 */     if (xSumOfSquaresOfDeltas > 0.0D) {
/* 228 */       if (this.yStats.sumOfSquaresOfDeltas() > 0.0D) {
/* 229 */         return 
/* 230 */           LinearTransformation.mapping(this.xStats.mean(), this.yStats.mean()).withSlope(this.sumOfProductsOfDeltas / xSumOfSquaresOfDeltas);
/*     */       }
/* 232 */       return LinearTransformation.horizontal(this.yStats.mean());
/*     */     }
/*     */     
/* 235 */     Preconditions.checkState(this.yStats.sumOfSquaresOfDeltas() > 0.0D);
/* 236 */     return LinearTransformation.vertical(this.xStats.mean());
/*     */   }
/*     */   
/*     */   private double ensurePositive(double value)
/*     */   {
/* 241 */     if (value > 0.0D) {
/* 242 */       return value;
/*     */     }
/* 244 */     return Double.MIN_VALUE;
/*     */   }
/*     */   
/*     */   private static double ensureInUnitRange(double value)
/*     */   {
/* 249 */     if (value >= 1.0D) {
/* 250 */       return 1.0D;
/*     */     }
/* 252 */     if (value <= -1.0D) {
/* 253 */       return -1.0D;
/*     */     }
/* 255 */     return value;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\math\PairedStatsAccumulator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */