/*     */ package org.springframework.boot;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SpringApplicationBannerPrinter
/*     */ {
/*     */   static final String BANNER_LOCATION_PROPERTY = "banner.location";
/*     */   static final String BANNER_IMAGE_LOCATION_PROPERTY = "banner.image.location";
/*     */   static final String DEFAULT_BANNER_LOCATION = "banner.txt";
/*  45 */   static final String[] IMAGE_EXTENSION = { "gif", "jpg", "png" };
/*     */   
/*  47 */   private static final Banner DEFAULT_BANNER = new SpringBootBanner();
/*     */   
/*     */   private final ResourceLoader resourceLoader;
/*     */   private final Banner fallbackBanner;
/*     */   
/*     */   SpringApplicationBannerPrinter(ResourceLoader resourceLoader, Banner fallbackBanner)
/*     */   {
/*  54 */     this.resourceLoader = resourceLoader;
/*  55 */     this.fallbackBanner = fallbackBanner;
/*     */   }
/*     */   
/*     */   public Banner print(Environment environment, Class<?> sourceClass, Log logger) {
/*  59 */     Banner banner = getBanner(environment, this.fallbackBanner);
/*     */     try {
/*  61 */       logger.info(createStringFromBanner(banner, environment, sourceClass));
/*     */     }
/*     */     catch (UnsupportedEncodingException ex) {
/*  64 */       logger.warn("Failed to create String for banner", ex);
/*     */     }
/*  66 */     return new PrintedBanner(banner, sourceClass);
/*     */   }
/*     */   
/*     */   public Banner print(Environment environment, Class<?> sourceClass, PrintStream out) {
/*  70 */     Banner banner = getBanner(environment, this.fallbackBanner);
/*  71 */     banner.printBanner(environment, sourceClass, out);
/*  72 */     return new PrintedBanner(banner, sourceClass);
/*     */   }
/*     */   
/*     */   private Banner getBanner(Environment environment, Banner definedBanner) {
/*  76 */     Banners banners = new Banners(null);
/*  77 */     banners.addIfNotNull(getImageBanner(environment));
/*  78 */     banners.addIfNotNull(getTextBanner(environment));
/*  79 */     if (banners.hasAtLeastOneBanner()) {
/*  80 */       return banners;
/*     */     }
/*  82 */     if (this.fallbackBanner != null) {
/*  83 */       return this.fallbackBanner;
/*     */     }
/*  85 */     return DEFAULT_BANNER;
/*     */   }
/*     */   
/*     */   private Banner getTextBanner(Environment environment) {
/*  89 */     String location = environment.getProperty("banner.location", "banner.txt");
/*     */     
/*  91 */     Resource resource = this.resourceLoader.getResource(location);
/*  92 */     if (resource.exists()) {
/*  93 */       return new ResourceBanner(resource);
/*     */     }
/*  95 */     return null;
/*     */   }
/*     */   
/*     */   private Banner getImageBanner(Environment environment) {
/*  99 */     String location = environment.getProperty("banner.image.location");
/* 100 */     Resource resource; if (StringUtils.hasLength(location)) {
/* 101 */       resource = this.resourceLoader.getResource(location);
/* 102 */       return resource.exists() ? new ImageBanner(resource) : null;
/*     */     }
/* 104 */     for (String ext : IMAGE_EXTENSION) {
/* 105 */       Resource resource = this.resourceLoader.getResource("banner." + ext);
/* 106 */       if (resource.exists()) {
/* 107 */         return new ImageBanner(resource);
/*     */       }
/*     */     }
/* 110 */     return null;
/*     */   }
/*     */   
/*     */   private String createStringFromBanner(Banner banner, Environment environment, Class<?> mainApplicationClass) throws UnsupportedEncodingException
/*     */   {
/* 115 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 116 */     banner.printBanner(environment, mainApplicationClass, new PrintStream(baos));
/* 117 */     String charset = environment.getProperty("banner.charset", "UTF-8");
/* 118 */     return baos.toString(charset);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class Banners
/*     */     implements Banner
/*     */   {
/* 126 */     private final List<Banner> banners = new ArrayList();
/*     */     
/*     */     public void addIfNotNull(Banner banner) {
/* 129 */       if (banner != null) {
/* 130 */         this.banners.add(banner);
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean hasAtLeastOneBanner() {
/* 135 */       return !this.banners.isEmpty();
/*     */     }
/*     */     
/*     */ 
/*     */     public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out)
/*     */     {
/* 141 */       for (Banner banner : this.banners) {
/* 142 */         banner.printBanner(environment, sourceClass, out);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class PrintedBanner
/*     */     implements Banner
/*     */   {
/*     */     private final Banner banner;
/*     */     
/*     */     private final Class<?> sourceClass;
/*     */     
/*     */ 
/*     */     PrintedBanner(Banner banner, Class<?> sourceClass)
/*     */     {
/* 159 */       this.banner = banner;
/* 160 */       this.sourceClass = sourceClass;
/*     */     }
/*     */     
/*     */ 
/*     */     public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out)
/*     */     {
/* 166 */       sourceClass = sourceClass == null ? this.sourceClass : sourceClass;
/* 167 */       this.banner.printBanner(environment, sourceClass, out);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\SpringApplicationBannerPrinter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */