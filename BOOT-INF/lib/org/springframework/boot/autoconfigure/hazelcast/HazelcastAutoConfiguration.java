/*    */ package org.springframework.boot.autoconfigure.hazelcast;
/*    */ 
/*    */ import com.hazelcast.config.Config;
/*    */ import com.hazelcast.core.Hazelcast;
/*    */ import com.hazelcast.core.HazelcastInstance;
/*    */ import java.io.IOException;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
/*    */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Conditional;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Configuration
/*    */ @ConditionalOnClass({HazelcastInstance.class})
/*    */ @ConditionalOnMissingBean({HazelcastInstance.class})
/*    */ @EnableConfigurationProperties({HazelcastProperties.class})
/*    */ public class HazelcastAutoConfiguration
/*    */ {
/*    */   @Configuration
/*    */   @ConditionalOnMissingBean({Config.class})
/*    */   @Conditional({HazelcastAutoConfiguration.ConfigAvailableCondition.class})
/*    */   static class HazelcastConfigFileConfiguration
/*    */   {
/*    */     private final HazelcastProperties hazelcastProperties;
/*    */     
/*    */     HazelcastConfigFileConfiguration(HazelcastProperties hazelcastProperties)
/*    */     {
/* 58 */       this.hazelcastProperties = hazelcastProperties;
/*    */     }
/*    */     
/*    */     @Bean
/*    */     public HazelcastInstance hazelcastInstance() throws IOException {
/* 63 */       Resource config = this.hazelcastProperties.resolveConfigLocation();
/* 64 */       if (config != null) {
/* 65 */         return new HazelcastInstanceFactory(config).getHazelcastInstance();
/*    */       }
/* 67 */       return Hazelcast.newHazelcastInstance();
/*    */     }
/*    */   }
/*    */   
/*    */   @Configuration
/*    */   @ConditionalOnSingleCandidate(Config.class)
/*    */   static class HazelcastConfigConfiguration
/*    */   {
/*    */     @Bean
/*    */     public HazelcastInstance hazelcastInstance(Config config)
/*    */     {
/* 78 */       return new HazelcastInstanceFactory(config).getHazelcastInstance();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   static class ConfigAvailableCondition
/*    */     extends HazelcastConfigResourceCondition
/*    */   {
/*    */     ConfigAvailableCondition()
/*    */     {
/* 90 */       super("config");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\hazelcast\HazelcastAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */