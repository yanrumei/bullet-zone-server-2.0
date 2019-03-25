/*     */ package org.springframework.boot.ansi;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.color.ColorSpace;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class AnsiColors
/*     */ {
/*     */   private static final Map<AnsiColor, LabColor> ANSI_COLOR_MAP;
/*     */   
/*     */   static
/*     */   {
/*  42 */     Map<AnsiColor, LabColor> colorMap = new LinkedHashMap();
/*  43 */     colorMap.put(AnsiColor.BLACK, new LabColor(Integer.valueOf(0)));
/*  44 */     colorMap.put(AnsiColor.RED, new LabColor(Integer.valueOf(11141120)));
/*  45 */     colorMap.put(AnsiColor.GREEN, new LabColor(Integer.valueOf(43520)));
/*  46 */     colorMap.put(AnsiColor.YELLOW, new LabColor(Integer.valueOf(11162880)));
/*  47 */     colorMap.put(AnsiColor.BLUE, new LabColor(Integer.valueOf(170)));
/*  48 */     colorMap.put(AnsiColor.MAGENTA, new LabColor(Integer.valueOf(11141290)));
/*  49 */     colorMap.put(AnsiColor.CYAN, new LabColor(Integer.valueOf(43690)));
/*  50 */     colorMap.put(AnsiColor.WHITE, new LabColor(Integer.valueOf(11184810)));
/*  51 */     colorMap.put(AnsiColor.BRIGHT_BLACK, new LabColor(Integer.valueOf(5592405)));
/*  52 */     colorMap.put(AnsiColor.BRIGHT_RED, new LabColor(Integer.valueOf(16733525)));
/*  53 */     colorMap.put(AnsiColor.BRIGHT_GREEN, new LabColor(Integer.valueOf(5635840)));
/*  54 */     colorMap.put(AnsiColor.BRIGHT_YELLOW, new LabColor(Integer.valueOf(16777045)));
/*  55 */     colorMap.put(AnsiColor.BRIGHT_BLUE, new LabColor(Integer.valueOf(5592575)));
/*  56 */     colorMap.put(AnsiColor.BRIGHT_MAGENTA, new LabColor(Integer.valueOf(16733695)));
/*  57 */     colorMap.put(AnsiColor.BRIGHT_CYAN, new LabColor(Integer.valueOf(5636095)));
/*  58 */     colorMap.put(AnsiColor.BRIGHT_WHITE, new LabColor(Integer.valueOf(16777215)));
/*  59 */     ANSI_COLOR_MAP = Collections.unmodifiableMap(colorMap);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static AnsiColor getClosest(Color color)
/*     */   {
/*  66 */     return getClosest(new LabColor(color));
/*     */   }
/*     */   
/*     */   private static AnsiColor getClosest(LabColor color) {
/*  70 */     AnsiColor result = null;
/*  71 */     double resultDistance = 3.4028234663852886E38D;
/*  72 */     for (Map.Entry<AnsiColor, LabColor> entry : ANSI_COLOR_MAP.entrySet()) {
/*  73 */       double distance = color.getDistance((LabColor)entry.getValue());
/*  74 */       if ((result == null) || (distance < resultDistance)) {
/*  75 */         resultDistance = distance;
/*  76 */         result = (AnsiColor)entry.getKey();
/*     */       }
/*     */     }
/*  79 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final class LabColor
/*     */   {
/*  88 */     private static final ColorSpace XYZ_COLOR_SPACE = ColorSpace.getInstance(1001);
/*     */     
/*     */     private final double l;
/*     */     
/*     */     private final double a;
/*     */     private final double b;
/*     */     
/*     */     LabColor(Integer rgb)
/*     */     {
/*  97 */       this(rgb == null ? (Color)null : new Color(rgb.intValue()));
/*     */     }
/*     */     
/*     */     LabColor(Color color) {
/* 101 */       Assert.notNull(color, "Color must not be null");
/* 102 */       float[] lab = fromXyz(color.getColorComponents(XYZ_COLOR_SPACE, null));
/* 103 */       this.l = lab[0];
/* 104 */       this.a = lab[1];
/* 105 */       this.b = lab[2];
/*     */     }
/*     */     
/*     */     private float[] fromXyz(float[] xyz) {
/* 109 */       return fromXyz(xyz[0], xyz[1], xyz[2]);
/*     */     }
/*     */     
/*     */     private float[] fromXyz(float x, float y, float z) {
/* 113 */       double l = (f(y) - 16.0D) * 116.0D;
/* 114 */       double a = (f(x) - f(y)) * 500.0D;
/* 115 */       double b = (f(y) - f(z)) * 200.0D;
/* 116 */       return new float[] { (float)l, (float)a, (float)b };
/*     */     }
/*     */     
/*     */     private double f(double t) {
/* 120 */       return t > 0.008856451679035631D ? Math.cbrt(t) : 0.3333333333333333D * 
/* 121 */         Math.pow(4.833333333333333D, 2.0D) * t + 0.13793103448275862D;
/*     */     }
/*     */     
/*     */     public double getDistance(LabColor other)
/*     */     {
/* 126 */       double c1 = Math.sqrt(this.a * this.a + this.b * this.b);
/* 127 */       double deltaC = c1 - Math.sqrt(other.a * other.a + other.b * other.b);
/* 128 */       double deltaA = this.a - other.a;
/* 129 */       double deltaB = this.b - other.b;
/* 130 */       double deltaH = Math.sqrt(
/* 131 */         Math.max(0.0D, deltaA * deltaA + deltaB * deltaB - deltaC * deltaC));
/* 132 */       return Math.sqrt(Math.max(0.0D, 
/* 133 */         Math.pow((this.l - other.l) / 1.0D, 2.0D) + 
/* 134 */         Math.pow(deltaC / (1.0D + 0.045D * c1), 2.0D) + 
/* 135 */         Math.pow(deltaH / (1.0D + 0.015D * c1), 2.0D)));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\ansi\AnsiColors.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */