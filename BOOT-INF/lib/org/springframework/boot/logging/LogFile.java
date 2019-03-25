/*     */ package org.springframework.boot.logging;
/*     */ 
/*     */ import java.util.Properties;
/*     */ import org.springframework.core.env.PropertyResolver;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LogFile
/*     */ {
/*     */   public static final String FILE_PROPERTY = "logging.file";
/*     */   public static final String PATH_PROPERTY = "logging.path";
/*     */   private final String file;
/*     */   private final String path;
/*     */   
/*     */   LogFile(String file)
/*     */   {
/*  59 */     this(file, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   LogFile(String file, String path)
/*     */   {
/*  68 */     Assert.isTrue((StringUtils.hasLength(file)) || (StringUtils.hasLength(path)), "File or Path must not be empty");
/*     */     
/*  70 */     this.file = file;
/*  71 */     this.path = path;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void applyToSystemProperties()
/*     */   {
/*  78 */     applyTo(System.getProperties());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void applyTo(Properties properties)
/*     */   {
/*  86 */     put(properties, "LOG_PATH", this.path);
/*  87 */     put(properties, "LOG_FILE", toString());
/*     */   }
/*     */   
/*     */   private void put(Properties properties, String key, String value) {
/*  91 */     if (StringUtils.hasLength(value)) {
/*  92 */       properties.put(key, value);
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  98 */     if (StringUtils.hasLength(this.file)) {
/*  99 */       return this.file;
/*     */     }
/* 101 */     String path = this.path;
/* 102 */     if (!path.endsWith("/")) {
/* 103 */       path = path + "/";
/*     */     }
/* 105 */     return StringUtils.applyRelativePath(path, "spring.log");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static LogFile get(PropertyResolver propertyResolver)
/*     */   {
/* 116 */     String file = propertyResolver.getProperty("logging.file");
/* 117 */     String path = propertyResolver.getProperty("logging.path");
/* 118 */     if ((StringUtils.hasLength(file)) || (StringUtils.hasLength(path))) {
/* 119 */       return new LogFile(file, path);
/*     */     }
/* 121 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\logging\LogFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */