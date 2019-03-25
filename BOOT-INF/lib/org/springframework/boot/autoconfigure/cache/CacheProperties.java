/*     */ package org.springframework.boot.autoconfigure.cache;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*     */ import org.springframework.boot.context.properties.DeprecatedConfigurationProperty;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ConfigurationProperties(prefix="spring.cache")
/*     */ public class CacheProperties
/*     */ {
/*     */   private CacheType type;
/*  47 */   private List<String> cacheNames = new ArrayList();
/*     */   
/*  49 */   private final Caffeine caffeine = new Caffeine();
/*     */   
/*  51 */   private final Couchbase couchbase = new Couchbase();
/*     */   
/*  53 */   private final EhCache ehcache = new EhCache();
/*     */   
/*  55 */   private final Hazelcast hazelcast = new Hazelcast();
/*     */   
/*  57 */   private final Infinispan infinispan = new Infinispan();
/*     */   
/*  59 */   private final JCache jcache = new JCache();
/*     */   
/*  61 */   private final Guava guava = new Guava();
/*     */   
/*     */   public CacheType getType() {
/*  64 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(CacheType mode) {
/*  68 */     this.type = mode;
/*     */   }
/*     */   
/*     */   public List<String> getCacheNames() {
/*  72 */     return this.cacheNames;
/*     */   }
/*     */   
/*     */   public void setCacheNames(List<String> cacheNames) {
/*  76 */     this.cacheNames = cacheNames;
/*     */   }
/*     */   
/*     */   public Caffeine getCaffeine() {
/*  80 */     return this.caffeine;
/*     */   }
/*     */   
/*     */   public Couchbase getCouchbase() {
/*  84 */     return this.couchbase;
/*     */   }
/*     */   
/*     */   public EhCache getEhcache() {
/*  88 */     return this.ehcache;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public Hazelcast getHazelcast() {
/*  93 */     return this.hazelcast;
/*     */   }
/*     */   
/*     */   public Infinispan getInfinispan() {
/*  97 */     return this.infinispan;
/*     */   }
/*     */   
/*     */   public JCache getJcache() {
/* 101 */     return this.jcache;
/*     */   }
/*     */   
/*     */   public Guava getGuava() {
/* 105 */     return this.guava;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Resource resolveConfigLocation(Resource config)
/*     */   {
/* 116 */     if (config != null) {
/* 117 */       Assert.isTrue(config.exists(), "Cache configuration does not exist '" + config
/* 118 */         .getDescription() + "'");
/* 119 */       return config;
/*     */     }
/* 121 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Caffeine
/*     */   {
/*     */     private String spec;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getSpec()
/*     */     {
/* 136 */       return this.spec;
/*     */     }
/*     */     
/*     */     public void setSpec(String spec) {
/* 140 */       this.spec = spec;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Couchbase
/*     */   {
/*     */     private int expiration;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getExpiration()
/*     */     {
/* 157 */       return this.expiration;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getExpirationSeconds()
/*     */     {
/* 165 */       return (int)TimeUnit.MILLISECONDS.toSeconds(this.expiration);
/*     */     }
/*     */     
/*     */     public void setExpiration(int expiration) {
/* 169 */       this.expiration = expiration;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class EhCache
/*     */   {
/*     */     private Resource config;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Resource getConfig()
/*     */     {
/* 185 */       return this.config;
/*     */     }
/*     */     
/*     */     public void setConfig(Resource config) {
/* 189 */       this.config = config;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static class Hazelcast
/*     */   {
/*     */     private Resource config;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     @DeprecatedConfigurationProperty(replacement="spring.hazelcast.config", reason="Use general hazelcast auto-configuration instead.")
/*     */     @Deprecated
/*     */     public Resource getConfig()
/*     */     {
/* 208 */       return this.config;
/*     */     }
/*     */     
/*     */     public void setConfig(Resource config) {
/* 212 */       this.config = config;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Infinispan
/*     */   {
/*     */     private Resource config;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Resource getConfig()
/*     */     {
/* 228 */       return this.config;
/*     */     }
/*     */     
/*     */     public void setConfig(Resource config) {
/* 232 */       this.config = config;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class JCache
/*     */   {
/*     */     private Resource config;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private String provider;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getProvider()
/*     */     {
/* 256 */       return this.provider;
/*     */     }
/*     */     
/*     */     public void setProvider(String provider) {
/* 260 */       this.provider = provider;
/*     */     }
/*     */     
/*     */     public Resource getConfig() {
/* 264 */       return this.config;
/*     */     }
/*     */     
/*     */     public void setConfig(Resource config) {
/* 268 */       this.config = config;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Guava
/*     */   {
/*     */     private String spec;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     @Deprecated
/*     */     @DeprecatedConfigurationProperty(reason="Caffeine will supersede the Guava support in Spring Boot 2.0", replacement="spring.cache.caffeine.spec")
/*     */     public String getSpec()
/*     */     {
/* 287 */       return this.spec;
/*     */     }
/*     */     
/*     */     @Deprecated
/*     */     public void setSpec(String spec) {
/* 292 */       this.spec = spec;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\cache\CacheProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */