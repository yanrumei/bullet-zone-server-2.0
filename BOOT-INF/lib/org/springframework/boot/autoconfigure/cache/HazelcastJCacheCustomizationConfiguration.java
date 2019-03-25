/*    */ package org.springframework.boot.autoconfigure.cache;
/*    */ 
/*    */ import com.hazelcast.core.HazelcastInstance;
/*    */ import java.io.IOException;
/*    */ import java.net.URI;
/*    */ import java.util.Properties;
/*    */ import org.springframework.beans.factory.ObjectProvider;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.core.io.Resource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Configuration
/*    */ @ConditionalOnClass({HazelcastInstance.class})
/*    */ class HazelcastJCacheCustomizationConfiguration
/*    */ {
/*    */   @Bean
/*    */   public HazelcastPropertiesCustomizer hazelcastPropertiesCustomizer(ObjectProvider<HazelcastInstance> hazelcastInstance)
/*    */   {
/* 43 */     return new HazelcastPropertiesCustomizer((HazelcastInstance)hazelcastInstance.getIfUnique());
/*    */   }
/*    */   
/*    */   private static class HazelcastPropertiesCustomizer implements JCachePropertiesCustomizer
/*    */   {
/*    */     private final HazelcastInstance hazelcastInstance;
/*    */     
/*    */     HazelcastPropertiesCustomizer(HazelcastInstance hazelcastInstance)
/*    */     {
/* 52 */       this.hazelcastInstance = hazelcastInstance;
/*    */     }
/*    */     
/*    */ 
/*    */     public void customize(CacheProperties cacheProperties, Properties properties)
/*    */     {
/* 58 */       Resource configLocation = cacheProperties.resolveConfigLocation(cacheProperties.getJcache().getConfig());
/* 59 */       if (configLocation != null)
/*    */       {
/* 61 */         properties.setProperty("hazelcast.config.location", 
/* 62 */           toUri(configLocation).toString());
/*    */       }
/* 64 */       else if (this.hazelcastInstance != null) {
/* 65 */         properties.put("hazelcast.instance.itself", this.hazelcastInstance);
/*    */       }
/*    */     }
/*    */     
/*    */     private static URI toUri(Resource config) {
/*    */       try {
/* 71 */         return config.getURI();
/*    */       }
/*    */       catch (IOException ex) {
/* 74 */         throw new IllegalArgumentException("Could not get URI from " + config, ex);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\cache\HazelcastJCacheCustomizationConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */