/*     */ package org.springframework.boot.autoconfigure.cache;
/*     */ 
/*     */ import java.util.List;
/*     */ import javax.annotation.PostConstruct;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureBefore;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.couchbase.CouchbaseAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.data.jpa.EntityManagerFactoryDependsOnPostProcessor;
/*     */ import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.cache.CacheManager;
/*     */ import org.springframework.cache.interceptor.CacheAspectSupport;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.context.annotation.Import;
/*     */ import org.springframework.context.annotation.ImportSelector;
/*     */ import org.springframework.context.annotation.Role;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
/*     */ import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
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
/*     */ @Configuration
/*     */ @ConditionalOnClass({CacheManager.class})
/*     */ @ConditionalOnBean({CacheAspectSupport.class})
/*     */ @ConditionalOnMissingBean(value={CacheManager.class}, name={"cacheResolver"})
/*     */ @EnableConfigurationProperties({CacheProperties.class})
/*     */ @AutoConfigureBefore({HibernateJpaAutoConfiguration.class})
/*     */ @AutoConfigureAfter({CouchbaseAutoConfiguration.class, HazelcastAutoConfiguration.class, RedisAutoConfiguration.class})
/*     */ @Import({CacheConfigurationImportSelector.class})
/*     */ public class CacheAutoConfiguration
/*     */ {
/*     */   static final String VALIDATOR_BEAN_NAME = "cacheAutoConfigurationValidator";
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean
/*     */   public CacheManagerCustomizers cacheManagerCustomizers(ObjectProvider<List<CacheManagerCustomizer<?>>> customizers)
/*     */   {
/*  82 */     return new CacheManagerCustomizers((List)customizers.getIfAvailable());
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @Role(2)
/*     */   public static CacheManagerValidatorPostProcessor cacheAutoConfigurationValidatorPostProcessor() {
/*  88 */     return new CacheManagerValidatorPostProcessor();
/*     */   }
/*     */   
/*     */   @Bean(name={"cacheAutoConfigurationValidator"})
/*     */   public CacheManagerValidator cacheAutoConfigurationValidator() {
/*  93 */     return new CacheManagerValidator();
/*     */   }
/*     */   
/*     */   @Configuration
/*     */   @ConditionalOnClass({LocalContainerEntityManagerFactoryBean.class})
/*     */   @ConditionalOnBean({AbstractEntityManagerFactoryBean.class})
/*     */   protected static class CacheManagerJpaDependencyConfiguration extends EntityManagerFactoryDependsOnPostProcessor
/*     */   {
/*     */     public CacheManagerJpaDependencyConfiguration()
/*     */     {
/* 103 */       super();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class CacheManagerValidatorPostProcessor
/*     */     implements BeanFactoryPostProcessor
/*     */   {
/*     */     public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
/*     */       throws BeansException
/*     */     {
/* 118 */       for (String name : beanFactory.getBeanNamesForType(CacheAspectSupport.class, false, false))
/*     */       {
/* 120 */         BeanDefinition definition = beanFactory.getBeanDefinition(name);
/* 121 */         definition.setDependsOn(
/* 122 */           append(definition.getDependsOn(), "cacheAutoConfigurationValidator"));
/*     */       }
/*     */     }
/*     */     
/*     */     private String[] append(String[] array, String value) {
/* 127 */       String[] result = new String[array == null ? 1 : array.length + 1];
/* 128 */       if (array != null) {
/* 129 */         System.arraycopy(array, 0, result, 0, array.length);
/*     */       }
/* 131 */       result[(result.length - 1)] = value;
/* 132 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static class CacheManagerValidator
/*     */   {
/*     */     @Autowired
/*     */     private CacheProperties cacheProperties;
/*     */     
/*     */ 
/*     */     @Autowired(required=false)
/*     */     private CacheManager cacheManager;
/*     */     
/*     */ 
/*     */     @PostConstruct
/*     */     public void checkHasCacheManager()
/*     */     {
/* 151 */       Assert.notNull(this.cacheManager, "No cache manager could be auto-configured, check your configuration (caching type is '" + this.cacheProperties
/*     */       
/*     */ 
/* 154 */         .getType() + "')");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static class CacheConfigurationImportSelector
/*     */     implements ImportSelector
/*     */   {
/*     */     public String[] selectImports(AnnotationMetadata importingClassMetadata)
/*     */     {
/* 166 */       CacheType[] types = CacheType.values();
/* 167 */       String[] imports = new String[types.length];
/* 168 */       for (int i = 0; i < types.length; i++) {
/* 169 */         imports[i] = CacheConfigurations.getConfigurationClass(types[i]);
/*     */       }
/* 171 */       return imports;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\cache\CacheAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */