/*     */ package org.springframework.boot.autoconfigure.couchbase;
/*     */ 
/*     */ import com.couchbase.client.java.Bucket;
/*     */ import com.couchbase.client.java.Cluster;
/*     */ import com.couchbase.client.java.CouchbaseBucket;
/*     */ import com.couchbase.client.java.CouchbaseCluster;
/*     */ import com.couchbase.client.java.cluster.ClusterInfo;
/*     */ import com.couchbase.client.java.cluster.ClusterManager;
/*     */ import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
/*     */ import com.couchbase.client.java.env.DefaultCouchbaseEnvironment.Builder;
/*     */ import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Conditional;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.context.annotation.ConfigurationCondition.ConfigurationPhase;
/*     */ import org.springframework.context.annotation.DependsOn;
/*     */ import org.springframework.context.annotation.Primary;
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
/*     */ @ConditionalOnClass({CouchbaseBucket.class, Cluster.class})
/*     */ @Conditional({CouchbaseCondition.class})
/*     */ @EnableConfigurationProperties({CouchbaseProperties.class})
/*     */ public class CouchbaseAutoConfiguration
/*     */ {
/*     */   @Configuration
/*     */   @ConditionalOnMissingBean(value={CouchbaseConfiguration.class}, type={"org.springframework.data.couchbase.config.CouchbaseConfigurer"})
/*     */   public static class CouchbaseConfiguration
/*     */   {
/*     */     private final CouchbaseProperties properties;
/*     */     
/*     */     public CouchbaseConfiguration(CouchbaseProperties properties)
/*     */     {
/*  58 */       this.properties = properties;
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @Primary
/*     */     public DefaultCouchbaseEnvironment couchbaseEnvironment() throws Exception {
/*  64 */       return initializeEnvironmentBuilder(this.properties).build();
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @Primary
/*     */     public Cluster couchbaseCluster() throws Exception {
/*  70 */       return CouchbaseCluster.create(couchbaseEnvironment(), this.properties
/*  71 */         .getBootstrapHosts());
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @Primary
/*     */     @DependsOn({"couchbaseClient"})
/*     */     public ClusterInfo couchbaseClusterInfo() throws Exception {
/*  78 */       return 
/*     */       
/*     */ 
/*  81 */         couchbaseCluster().clusterManager(this.properties.getBucket().getName(), this.properties.getBucket().getPassword()).info();
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @Primary
/*     */     public Bucket couchbaseClient() throws Exception {
/*  87 */       return couchbaseCluster().openBucket(this.properties.getBucket().getName(), this.properties
/*  88 */         .getBucket().getPassword());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected DefaultCouchbaseEnvironment.Builder initializeEnvironmentBuilder(CouchbaseProperties properties)
/*     */     {
/*  98 */       CouchbaseProperties.Endpoints endpoints = properties.getEnv().getEndpoints();
/*  99 */       CouchbaseProperties.Timeouts timeouts = properties.getEnv().getTimeouts();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 107 */       DefaultCouchbaseEnvironment.Builder builder = DefaultCouchbaseEnvironment.builder().connectTimeout(timeouts.getConnect()).kvEndpoints(endpoints.getKeyValue()).kvTimeout(timeouts.getKeyValue()).queryEndpoints(endpoints.getQuery()).queryTimeout(timeouts.getQuery()).viewEndpoints(endpoints.getView()).socketConnectTimeout(timeouts.getSocketConnect()).viewTimeout(timeouts.getView());
/* 108 */       CouchbaseProperties.Ssl ssl = properties.getEnv().getSsl();
/* 109 */       if (ssl.getEnabled().booleanValue()) {
/* 110 */         builder.sslEnabled(true);
/* 111 */         if (ssl.getKeyStore() != null) {
/* 112 */           builder.sslKeystoreFile(ssl.getKeyStore());
/*     */         }
/* 114 */         if (ssl.getKeyStorePassword() != null) {
/* 115 */           builder.sslKeystorePassword(ssl.getKeyStorePassword());
/*     */         }
/*     */       }
/* 118 */       return builder;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class CouchbaseCondition
/*     */     extends AnyNestedCondition
/*     */   {
/*     */     CouchbaseCondition()
/*     */     {
/* 134 */       super();
/*     */     }
/*     */     
/*     */     @ConditionalOnBean(type={"org.springframework.data.couchbase.config.CouchbaseConfigurer"})
/*     */     static class CouchbaseConfigurerAvailable {}
/*     */     
/*     */     @Conditional({OnBootstrapHostsCondition.class})
/*     */     static class BootstrapHostsProperty {}
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\couchbase\CouchbaseAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */