/*     */ package org.springframework.boot.autoconfigure.web;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*     */ import org.springframework.boot.context.properties.NestedConfigurationProperty;
/*     */ import org.springframework.context.ResourceLoaderAware;
/*     */ import org.springframework.core.io.ClassPathResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ConfigurationProperties(prefix="spring.resources", ignoreUnknownFields=false)
/*     */ public class ResourceProperties
/*     */   implements ResourceLoaderAware
/*     */ {
/*  42 */   private static final String[] SERVLET_RESOURCE_LOCATIONS = { "/" };
/*     */   
/*  44 */   private static final String[] CLASSPATH_RESOURCE_LOCATIONS = { "classpath:/META-INF/resources/", "classpath:/resources/", "classpath:/static/", "classpath:/public/" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  51 */   private static final String[] RESOURCE_LOCATIONS = new String[CLASSPATH_RESOURCE_LOCATIONS.length + SERVLET_RESOURCE_LOCATIONS.length];
/*     */   
/*  53 */   static { System.arraycopy(SERVLET_RESOURCE_LOCATIONS, 0, RESOURCE_LOCATIONS, 0, SERVLET_RESOURCE_LOCATIONS.length);
/*     */     
/*  55 */     System.arraycopy(CLASSPATH_RESOURCE_LOCATIONS, 0, RESOURCE_LOCATIONS, SERVLET_RESOURCE_LOCATIONS.length, CLASSPATH_RESOURCE_LOCATIONS.length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  63 */   private String[] staticLocations = RESOURCE_LOCATIONS;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Integer cachePeriod;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  73 */   private boolean addMappings = true;
/*     */   
/*  75 */   private final Chain chain = new Chain();
/*     */   
/*     */   private ResourceLoader resourceLoader;
/*     */   
/*     */   public void setResourceLoader(ResourceLoader resourceLoader)
/*     */   {
/*  81 */     this.resourceLoader = resourceLoader;
/*     */   }
/*     */   
/*     */   public String[] getStaticLocations() {
/*  85 */     return this.staticLocations;
/*     */   }
/*     */   
/*     */   public void setStaticLocations(String[] staticLocations) {
/*  89 */     this.staticLocations = appendSlashIfNecessary(staticLocations);
/*     */   }
/*     */   
/*     */   private String[] appendSlashIfNecessary(String[] staticLocations) {
/*  93 */     String[] normalized = new String[staticLocations.length];
/*  94 */     for (int i = 0; i < staticLocations.length; i++) {
/*  95 */       String location = staticLocations[i];
/*  96 */       normalized[i] = (location + "/");
/*     */     }
/*  98 */     return normalized;
/*     */   }
/*     */   
/*     */   public Resource getWelcomePage() {
/* 102 */     for (String location : getStaticWelcomePageLocations()) {
/* 103 */       Resource resource = this.resourceLoader.getResource(location);
/*     */       try {
/* 105 */         if (resource.exists()) {
/* 106 */           resource.getURL();
/* 107 */           return resource;
/*     */         }
/*     */       }
/*     */       catch (Exception localException) {}
/*     */     }
/*     */     
/*     */ 
/* 114 */     return null;
/*     */   }
/*     */   
/*     */   private String[] getStaticWelcomePageLocations() {
/* 118 */     String[] result = new String[this.staticLocations.length];
/* 119 */     for (int i = 0; i < result.length; i++) {
/* 120 */       String location = this.staticLocations[i];
/* 121 */       if (!location.endsWith("/")) {
/* 122 */         location = location + "/";
/*     */       }
/* 124 */       result[i] = (location + "index.html");
/*     */     }
/* 126 */     return result;
/*     */   }
/*     */   
/*     */   List<Resource> getFaviconLocations() {
/* 130 */     List<Resource> locations = new ArrayList(this.staticLocations.length + 1);
/*     */     
/* 132 */     if (this.resourceLoader != null) {
/* 133 */       for (String location : this.staticLocations) {
/* 134 */         locations.add(this.resourceLoader.getResource(location));
/*     */       }
/*     */     }
/* 137 */     locations.add(new ClassPathResource("/"));
/* 138 */     return Collections.unmodifiableList(locations);
/*     */   }
/*     */   
/*     */   public Integer getCachePeriod() {
/* 142 */     return this.cachePeriod;
/*     */   }
/*     */   
/*     */   public void setCachePeriod(Integer cachePeriod) {
/* 146 */     this.cachePeriod = cachePeriod;
/*     */   }
/*     */   
/*     */   public boolean isAddMappings() {
/* 150 */     return this.addMappings;
/*     */   }
/*     */   
/*     */   public void setAddMappings(boolean addMappings) {
/* 154 */     this.addMappings = addMappings;
/*     */   }
/*     */   
/*     */   public Chain getChain() {
/* 158 */     return this.chain;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Chain
/*     */   {
/*     */     private Boolean enabled;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 175 */     private boolean cache = true;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 180 */     private boolean htmlApplicationCache = false;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 186 */     private boolean gzipped = false;
/*     */     @NestedConfigurationProperty
/* 188 */     private final ResourceProperties.Strategy strategy = new ResourceProperties.Strategy();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Boolean getEnabled()
/*     */     {
/* 198 */       return getEnabled(getStrategy().getFixed().isEnabled(), 
/* 199 */         getStrategy().getContent().isEnabled(), this.enabled);
/*     */     }
/*     */     
/*     */     public void setEnabled(boolean enabled) {
/* 203 */       this.enabled = Boolean.valueOf(enabled);
/*     */     }
/*     */     
/*     */     public boolean isCache() {
/* 207 */       return this.cache;
/*     */     }
/*     */     
/*     */     public void setCache(boolean cache) {
/* 211 */       this.cache = cache;
/*     */     }
/*     */     
/*     */     public ResourceProperties.Strategy getStrategy() {
/* 215 */       return this.strategy;
/*     */     }
/*     */     
/*     */     public boolean isHtmlApplicationCache() {
/* 219 */       return this.htmlApplicationCache;
/*     */     }
/*     */     
/*     */     public void setHtmlApplicationCache(boolean htmlApplicationCache) {
/* 223 */       this.htmlApplicationCache = htmlApplicationCache;
/*     */     }
/*     */     
/*     */     public boolean isGzipped() {
/* 227 */       return this.gzipped;
/*     */     }
/*     */     
/*     */     public void setGzipped(boolean gzipped) {
/* 231 */       this.gzipped = gzipped;
/*     */     }
/*     */     
/*     */     static Boolean getEnabled(boolean fixedEnabled, boolean contentEnabled, Boolean chainEnabled)
/*     */     {
/* 236 */       return (fixedEnabled) || (contentEnabled) ? Boolean.TRUE : chainEnabled;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Strategy
/*     */   {
/*     */     @NestedConfigurationProperty
/* 246 */     private final ResourceProperties.Fixed fixed = new ResourceProperties.Fixed();
/*     */     
/*     */     @NestedConfigurationProperty
/* 249 */     private final ResourceProperties.Content content = new ResourceProperties.Content();
/*     */     
/*     */     public ResourceProperties.Fixed getFixed()
/*     */     {
/* 253 */       return this.fixed;
/*     */     }
/*     */     
/*     */     public ResourceProperties.Content getContent() {
/* 257 */       return this.content;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Content
/*     */   {
/*     */     private boolean enabled;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 275 */     private String[] paths = { "/**" };
/*     */     
/*     */     public boolean isEnabled() {
/* 278 */       return this.enabled;
/*     */     }
/*     */     
/*     */     public void setEnabled(boolean enabled) {
/* 282 */       this.enabled = enabled;
/*     */     }
/*     */     
/*     */     public String[] getPaths() {
/* 286 */       return this.paths;
/*     */     }
/*     */     
/*     */     public void setPaths(String[] paths) {
/* 290 */       this.paths = paths;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Fixed
/*     */   {
/*     */     private boolean enabled;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 308 */     private String[] paths = { "/**" };
/*     */     
/*     */ 
/*     */     private String version;
/*     */     
/*     */ 
/*     */     public boolean isEnabled()
/*     */     {
/* 316 */       return this.enabled;
/*     */     }
/*     */     
/*     */     public void setEnabled(boolean enabled) {
/* 320 */       this.enabled = enabled;
/*     */     }
/*     */     
/*     */     public String[] getPaths() {
/* 324 */       return this.paths;
/*     */     }
/*     */     
/*     */     public void setPaths(String[] paths) {
/* 328 */       this.paths = paths;
/*     */     }
/*     */     
/*     */     public String getVersion() {
/* 332 */       return this.version;
/*     */     }
/*     */     
/*     */     public void setVersion(String version) {
/* 336 */       this.version = version;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\web\ResourceProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */