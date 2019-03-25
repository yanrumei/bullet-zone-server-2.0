/*     */ package org.springframework.boot.autoconfigure;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import org.springframework.core.io.UrlResource;
/*     */ import org.springframework.core.io.support.PropertiesLoaderUtils;
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
/*     */ final class AutoConfigurationMetadataLoader
/*     */ {
/*     */   protected static final String PATH = "META-INF/spring-autoconfigure-metadata.properties";
/*     */   
/*     */   public static AutoConfigurationMetadata loadMetadata(ClassLoader classLoader)
/*     */   {
/*  43 */     return loadMetadata(classLoader, "META-INF/spring-autoconfigure-metadata.properties");
/*     */   }
/*     */   
/*     */   static AutoConfigurationMetadata loadMetadata(ClassLoader classLoader, String path)
/*     */   {
/*     */     try {
/*  49 */       Enumeration<URL> urls = classLoader != null ? classLoader.getResources(path) : ClassLoader.getSystemResources(path);
/*  50 */       Properties properties = new Properties();
/*  51 */       while (urls.hasMoreElements()) {
/*  52 */         properties.putAll(
/*  53 */           PropertiesLoaderUtils.loadProperties(new UrlResource((URL)urls.nextElement())));
/*     */       }
/*  55 */       return loadMetadata(properties);
/*     */     }
/*     */     catch (IOException ex) {
/*  58 */       throw new IllegalArgumentException("Unable to load @ConditionalOnClass location [" + path + "]", ex);
/*     */     }
/*     */   }
/*     */   
/*     */   static AutoConfigurationMetadata loadMetadata(Properties properties)
/*     */   {
/*  64 */     return new PropertiesAutoConfigurationMetadata(properties);
/*     */   }
/*     */   
/*     */ 
/*     */   private static class PropertiesAutoConfigurationMetadata
/*     */     implements AutoConfigurationMetadata
/*     */   {
/*     */     private final Properties properties;
/*     */     
/*     */ 
/*     */     PropertiesAutoConfigurationMetadata(Properties properties)
/*     */     {
/*  76 */       this.properties = properties;
/*     */     }
/*     */     
/*     */     public boolean wasProcessed(String className)
/*     */     {
/*  81 */       return this.properties.containsKey(className);
/*     */     }
/*     */     
/*     */     public Integer getInteger(String className, String key)
/*     */     {
/*  86 */       return getInteger(className, key, null);
/*     */     }
/*     */     
/*     */     public Integer getInteger(String className, String key, Integer defaultValue)
/*     */     {
/*  91 */       String value = get(className, key);
/*  92 */       return value != null ? Integer.valueOf(value) : defaultValue;
/*     */     }
/*     */     
/*     */     public Set<String> getSet(String className, String key)
/*     */     {
/*  97 */       return getSet(className, key, null);
/*     */     }
/*     */     
/*     */ 
/*     */     public Set<String> getSet(String className, String key, Set<String> defaultValue)
/*     */     {
/* 103 */       String value = get(className, key);
/* 104 */       return value != null ? StringUtils.commaDelimitedListToSet(value) : defaultValue;
/*     */     }
/*     */     
/*     */ 
/*     */     public String get(String className, String key)
/*     */     {
/* 110 */       return get(className, key, null);
/*     */     }
/*     */     
/*     */     public String get(String className, String key, String defaultValue)
/*     */     {
/* 115 */       String value = this.properties.getProperty(className + "." + key);
/* 116 */       return value != null ? value : defaultValue;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\AutoConfigurationMetadataLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */