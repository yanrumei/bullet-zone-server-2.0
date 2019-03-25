/*    */ package org.springframework.boot.autoconfigure.data.couchbase;
/*    */ 
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.couchbase.CouchbaseAutoConfiguration.CouchbaseConfiguration;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.data.couchbase.config.CouchbaseConfigurer;
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
/*    */ @ConditionalOnClass({CouchbaseConfigurer.class})
/*    */ @ConditionalOnBean({CouchbaseAutoConfiguration.CouchbaseConfiguration.class})
/*    */ class CouchbaseConfigurerAdapterConfiguration
/*    */ {
/*    */   private final CouchbaseAutoConfiguration.CouchbaseConfiguration configuration;
/*    */   
/*    */   CouchbaseConfigurerAdapterConfiguration(CouchbaseAutoConfiguration.CouchbaseConfiguration configuration)
/*    */   {
/* 41 */     this.configuration = configuration;
/*    */   }
/*    */   
/*    */   @Bean
/*    */   @ConditionalOnMissingBean
/*    */   public CouchbaseConfigurer springBootCouchbaseConfigurer() throws Exception {
/* 47 */     return new SpringBootCouchbaseConfigurer(this.configuration
/* 48 */       .couchbaseEnvironment(), this.configuration
/* 49 */       .couchbaseCluster(), this.configuration
/* 50 */       .couchbaseClusterInfo(), this.configuration
/* 51 */       .couchbaseClient());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\data\couchbase\CouchbaseConfigurerAdapterConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */