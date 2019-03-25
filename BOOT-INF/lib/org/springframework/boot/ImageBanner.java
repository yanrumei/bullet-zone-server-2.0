/*     */ package org.springframework.boot;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import javax.imageio.ImageIO;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.boot.ansi.AnsiBackground;
/*     */ import org.springframework.boot.ansi.AnsiColor;
/*     */ import org.springframework.boot.ansi.AnsiColors;
/*     */ import org.springframework.boot.ansi.AnsiElement;
/*     */ import org.springframework.boot.ansi.AnsiOutput;
/*     */ import org.springframework.boot.bind.RelaxedPropertyResolver;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.env.PropertyResolver;
/*     */ import org.springframework.core.io.Resource;
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
/*     */ public class ImageBanner
/*     */   implements Banner
/*     */ {
/*  52 */   private static final Log logger = LogFactory.getLog(ImageBanner.class);
/*     */   
/*  54 */   private static final double[] RGB_WEIGHT = { 0.2126D, 0.7152D, 0.0722D };
/*     */   
/*  56 */   private static final char[] PIXEL = { ' ', '.', '*', ':', 'o', '&', '8', '#', '@' };
/*     */   
/*     */   private static final int LUMINANCE_INCREMENT = 10;
/*     */   
/*  60 */   private static final int LUMINANCE_START = 10 * PIXEL.length;
/*     */   private final Resource image;
/*     */   
/*     */   public ImageBanner(Resource image)
/*     */   {
/*  65 */     Assert.notNull(image, "Image must not be null");
/*  66 */     Assert.isTrue(image.exists(), "Image must exist");
/*  67 */     this.image = image;
/*     */   }
/*     */   
/*     */ 
/*     */   public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out)
/*     */   {
/*  73 */     String headless = System.getProperty("java.awt.headless");
/*     */     try {
/*  75 */       System.setProperty("java.awt.headless", "true");
/*  76 */       printBanner(environment, out);
/*     */     }
/*     */     catch (Throwable ex) {
/*  79 */       logger.warn("Image banner not printable: " + this.image + " (" + ex.getClass() + ": '" + ex
/*  80 */         .getMessage() + "')");
/*  81 */       logger.debug("Image banner printing failure", ex);
/*     */     }
/*     */     finally {
/*  84 */       if (headless == null) {
/*  85 */         System.clearProperty("java.awt.headless");
/*     */       }
/*     */       else {
/*  88 */         System.setProperty("java.awt.headless", headless);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void printBanner(Environment environment, PrintStream out) throws IOException
/*     */   {
/*  95 */     PropertyResolver properties = new RelaxedPropertyResolver(environment, "banner.image.");
/*     */     
/*  97 */     int width = ((Integer)properties.getProperty("width", Integer.class, Integer.valueOf(76))).intValue();
/*  98 */     int height = ((Integer)properties.getProperty("height", Integer.class, Integer.valueOf(0))).intValue();
/*  99 */     int margin = ((Integer)properties.getProperty("margin", Integer.class, Integer.valueOf(2))).intValue();
/* 100 */     boolean invert = ((Boolean)properties.getProperty("invert", Boolean.class, Boolean.valueOf(false))).booleanValue();
/* 101 */     BufferedImage image = readImage(width, height);
/* 102 */     printBanner(image, margin, invert, out);
/*     */   }
/*     */   
/*     */   private BufferedImage readImage(int width, int height) throws IOException {
/* 106 */     InputStream inputStream = this.image.getInputStream();
/*     */     try {
/* 108 */       BufferedImage image = ImageIO.read(inputStream);
/* 109 */       return resizeImage(image, width, height);
/*     */     }
/*     */     finally {
/* 112 */       inputStream.close();
/*     */     }
/*     */   }
/*     */   
/*     */   private BufferedImage resizeImage(BufferedImage image, int width, int height) {
/* 117 */     if (width < 1) {
/* 118 */       width = 1;
/*     */     }
/* 120 */     if (height <= 0) {
/* 121 */       double aspectRatio = width / image.getWidth() * 0.5D;
/* 122 */       height = (int)Math.ceil(image.getHeight() * aspectRatio);
/*     */     }
/* 124 */     BufferedImage resized = new BufferedImage(width, height, 1);
/*     */     
/* 126 */     Image scaled = image.getScaledInstance(width, height, 1);
/* 127 */     resized.getGraphics().drawImage(scaled, 0, 0, null);
/* 128 */     return resized;
/*     */   }
/*     */   
/*     */   private void printBanner(BufferedImage image, int margin, boolean invert, PrintStream out)
/*     */   {
/* 133 */     AnsiElement background = invert ? AnsiBackground.BLACK : AnsiBackground.DEFAULT;
/* 134 */     out.print(AnsiOutput.encode(AnsiColor.DEFAULT));
/* 135 */     out.print(AnsiOutput.encode(background));
/* 136 */     out.println();
/* 137 */     out.println();
/* 138 */     AnsiColor lastColor = AnsiColor.DEFAULT;
/* 139 */     for (int y = 0; y < image.getHeight(); y++) {
/* 140 */       for (int i = 0; i < margin; i++) {
/* 141 */         out.print(" ");
/*     */       }
/* 143 */       for (int x = 0; x < image.getWidth(); x++) {
/* 144 */         Color color = new Color(image.getRGB(x, y), false);
/* 145 */         AnsiColor ansiColor = AnsiColors.getClosest(color);
/* 146 */         if (ansiColor != lastColor) {
/* 147 */           out.print(AnsiOutput.encode(ansiColor));
/* 148 */           lastColor = ansiColor;
/*     */         }
/* 150 */         out.print(getAsciiPixel(color, invert));
/*     */       }
/* 152 */       out.println();
/*     */     }
/* 154 */     out.print(AnsiOutput.encode(AnsiColor.DEFAULT));
/* 155 */     out.print(AnsiOutput.encode(AnsiBackground.DEFAULT));
/* 156 */     out.println();
/*     */   }
/*     */   
/*     */   private char getAsciiPixel(Color color, boolean dark) {
/* 160 */     double luminance = getLuminance(color, dark);
/* 161 */     for (int i = 0; i < PIXEL.length; i++) {
/* 162 */       if (luminance >= LUMINANCE_START - i * 10) {
/* 163 */         return PIXEL[i];
/*     */       }
/*     */     }
/* 166 */     return PIXEL[(PIXEL.length - 1)];
/*     */   }
/*     */   
/*     */   private int getLuminance(Color color, boolean inverse) {
/* 170 */     double luminance = 0.0D;
/* 171 */     luminance += getLuminance(color.getRed(), inverse, RGB_WEIGHT[0]);
/* 172 */     luminance += getLuminance(color.getGreen(), inverse, RGB_WEIGHT[1]);
/* 173 */     luminance += getLuminance(color.getBlue(), inverse, RGB_WEIGHT[2]);
/* 174 */     return (int)Math.ceil(luminance / 255.0D * 100.0D);
/*     */   }
/*     */   
/*     */   private double getLuminance(int component, boolean inverse, double weight) {
/* 178 */     return (inverse ? 255 - component : component) * weight;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\ImageBanner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */