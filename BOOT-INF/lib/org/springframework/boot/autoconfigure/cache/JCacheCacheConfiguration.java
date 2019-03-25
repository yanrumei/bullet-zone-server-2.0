/*     */ package org.springframework.boot.autoconfigure.cache;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import javax.cache.Caching;
/*     */ import javax.cache.configuration.MutableConfiguration;
/*     */ import javax.cache.spi.CachingProvider;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.Builder;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.ItemsBuilder;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
/*     */ import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
/*     */ import org.springframework.boot.bind.RelaxedPropertyResolver;
/*     */ import org.springframework.cache.jcache.JCacheCacheManager;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.ConditionContext;
/*     */ import org.springframework.context.annotation.Conditional;
/*     */ import org.springframework.context.annotation.ConfigurationCondition.ConfigurationPhase;
/*     */ import org.springframework.context.annotation.Import;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.core.annotation.Order;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*     */ import org.springframework.util.CollectionUtils;
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
/*     */ @org.springframework.context.annotation.Configuration
/*     */ @ConditionalOnClass({Caching.class, JCacheCacheManager.class})
/*     */ @ConditionalOnMissingBean({org.springframework.cache.CacheManager.class})
/*     */ @Conditional({CacheCondition.class, JCacheAvailableCondition.class})
/*     */ @Import({HazelcastJCacheCustomizationConfiguration.class})
/*     */ class JCacheCacheConfiguration
/*     */ {
/*     */   private final CacheProperties cacheProperties;
/*     */   private final CacheManagerCustomizers customizers;
/*     */   private final javax.cache.configuration.Configuration<?, ?> defaultCacheConfiguration;
/*     */   private final List<JCacheManagerCustomizer> cacheManagerCustomizers;
/*     */   private final List<JCachePropertiesCustomizer> cachePropertiesCustomizers;
/*     */   
/*     */   JCacheCacheConfiguration(CacheProperties cacheProperties, CacheManagerCustomizers customizers, ObjectProvider<javax.cache.configuration.Configuration<?, ?>> defaultCacheConfiguration, ObjectProvider<List<JCacheManagerCustomizer>> cacheManagerCustomizers, ObjectProvider<List<JCachePropertiesCustomizer>> cachePropertiesCustomizers)
/*     */   {
/*  81 */     this.cacheProperties = cacheProperties;
/*  82 */     this.customizers = customizers;
/*  83 */     this.defaultCacheConfiguration = ((javax.cache.configuration.Configuration)defaultCacheConfiguration.getIfAvailable());
/*  84 */     this.cacheManagerCustomizers = ((List)cacheManagerCustomizers.getIfAvailable());
/*  85 */     this.cachePropertiesCustomizers = ((List)cachePropertiesCustomizers.getIfAvailable());
/*     */   }
/*     */   
/*     */   @Bean
/*     */   public JCacheCacheManager cacheManager(javax.cache.CacheManager jCacheCacheManager) {
/*  90 */     JCacheCacheManager cacheManager = new JCacheCacheManager(jCacheCacheManager);
/*  91 */     return (JCacheCacheManager)this.customizers.customize(cacheManager);
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean
/*     */   public javax.cache.CacheManager jCacheCacheManager() throws IOException {
/*  97 */     javax.cache.CacheManager jCacheCacheManager = createCacheManager();
/*  98 */     List<String> cacheNames = this.cacheProperties.getCacheNames();
/*  99 */     if (!CollectionUtils.isEmpty(cacheNames)) {
/* 100 */       for (String cacheName : cacheNames) {
/* 101 */         jCacheCacheManager.createCache(cacheName, getDefaultCacheConfiguration());
/*     */       }
/*     */     }
/* 104 */     customize(jCacheCacheManager);
/* 105 */     return jCacheCacheManager;
/*     */   }
/*     */   
/*     */   private javax.cache.CacheManager createCacheManager() throws IOException {
/* 109 */     CachingProvider cachingProvider = getCachingProvider(this.cacheProperties
/* 110 */       .getJcache().getProvider());
/* 111 */     Properties properties = createCacheManagerProperties();
/*     */     
/* 113 */     Resource configLocation = this.cacheProperties.resolveConfigLocation(this.cacheProperties.getJcache().getConfig());
/* 114 */     if (configLocation != null) {
/* 115 */       return cachingProvider.getCacheManager(configLocation.getURI(), cachingProvider
/* 116 */         .getDefaultClassLoader(), properties);
/*     */     }
/* 118 */     return cachingProvider.getCacheManager(null, null, properties);
/*     */   }
/*     */   
/*     */   private CachingProvider getCachingProvider(String cachingProviderFqn) {
/* 122 */     if (StringUtils.hasText(cachingProviderFqn)) {
/* 123 */       return Caching.getCachingProvider(cachingProviderFqn);
/*     */     }
/* 125 */     return Caching.getCachingProvider();
/*     */   }
/*     */   
/*     */   private Properties createCacheManagerProperties() {
/* 129 */     Properties properties = new Properties();
/* 130 */     if (this.cachePropertiesCustomizers != null) {
/* 131 */       for (JCachePropertiesCustomizer customizer : this.cachePropertiesCustomizers) {
/* 132 */         customizer.customize(this.cacheProperties, properties);
/*     */       }
/*     */     }
/* 135 */     return properties;
/*     */   }
/*     */   
/*     */   private javax.cache.configuration.Configuration<?, ?> getDefaultCacheConfiguration() {
/* 139 */     if (this.defaultCacheConfiguration != null) {
/* 140 */       return this.defaultCacheConfiguration;
/*     */     }
/* 142 */     return new MutableConfiguration();
/*     */   }
/*     */   
/*     */   private void customize(javax.cache.CacheManager cacheManager) {
/* 146 */     if (this.cacheManagerCustomizers != null) {
/* 147 */       AnnotationAwareOrderComparator.sort(this.cacheManagerCustomizers);
/* 148 */       for (JCacheManagerCustomizer customizer : this.cacheManagerCustomizers) {
/* 149 */         customizer.customize(cacheManager);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Order(Integer.MAX_VALUE)
/*     */   static class JCacheAvailableCondition
/*     */     extends AnyNestedCondition
/*     */   {
/*     */     JCacheAvailableCondition()
/*     */     {
/* 163 */       super();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @ConditionalOnSingleCandidate(javax.cache.CacheManager.class)
/*     */     static class CustomJCacheCacheManager {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     @Conditional({JCacheCacheConfiguration.JCacheProviderAvailableCondition.class})
/*     */     static class JCacheProvider {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Order(Integer.MAX_VALUE)
/*     */   static class JCacheProviderAvailableCondition
/*     */     extends SpringBootCondition
/*     */   {
/*     */     public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*     */     {
/* 189 */       ConditionMessage.Builder message = ConditionMessage.forCondition("JCache", new Object[0]);
/*     */       
/* 191 */       RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(context.getEnvironment(), "spring.cache.jcache.");
/* 192 */       if (resolver.containsProperty("provider")) {
/* 193 */         return 
/* 194 */           ConditionOutcome.match(message.because("JCache provider specified"));
/*     */       }
/*     */       
/* 197 */       Iterator<CachingProvider> providers = Caching.getCachingProviders().iterator();
/* 198 */       if (!providers.hasNext()) {
/* 199 */         return 
/* 200 */           ConditionOutcome.noMatch(message.didNotFind("JSR-107 provider").atAll());
/*     */       }
/* 202 */       providers.next();
/* 203 */       if (providers.hasNext()) {
/* 204 */         return 
/* 205 */           ConditionOutcome.noMatch(message.foundExactly("multiple JSR-107 providers"));
/*     */       }
/*     */       
/* 208 */       return 
/* 209 */         ConditionOutcome.match(message.foundExactly("single JSR-107 provider"));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\cache\JCacheCacheConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */