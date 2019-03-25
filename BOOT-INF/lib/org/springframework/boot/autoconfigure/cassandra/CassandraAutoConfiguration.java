/*     */ package org.springframework.boot.autoconfigure.cassandra;
/*     */ 
/*     */ import com.datastax.driver.core.Cluster;
/*     */ import com.datastax.driver.core.Cluster.Builder;
/*     */ import com.datastax.driver.core.QueryOptions;
/*     */ import com.datastax.driver.core.SocketOptions;
/*     */ import com.datastax.driver.core.policies.LoadBalancingPolicy;
/*     */ import com.datastax.driver.core.policies.ReconnectionPolicy;
/*     */ import com.datastax.driver.core.policies.RetryPolicy;
/*     */ import java.util.List;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
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
/*     */ @Configuration
/*     */ @ConditionalOnClass({Cluster.class})
/*     */ @EnableConfigurationProperties({CassandraProperties.class})
/*     */ public class CassandraAutoConfiguration
/*     */ {
/*     */   private final CassandraProperties properties;
/*     */   private final List<ClusterBuilderCustomizer> builderCustomizers;
/*     */   
/*     */   public CassandraAutoConfiguration(CassandraProperties properties, ObjectProvider<List<ClusterBuilderCustomizer>> builderCustomizers)
/*     */   {
/*  58 */     this.properties = properties;
/*  59 */     this.builderCustomizers = ((List)builderCustomizers.getIfAvailable());
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean
/*     */   public Cluster cluster() {
/*  65 */     CassandraProperties properties = this.properties;
/*     */     
/*     */ 
/*  68 */     Cluster.Builder builder = Cluster.builder().withClusterName(properties.getClusterName()).withPort(properties.getPort());
/*  69 */     if (properties.getUsername() != null) {
/*  70 */       builder.withCredentials(properties.getUsername(), properties.getPassword());
/*     */     }
/*  72 */     if (properties.getCompression() != null) {
/*  73 */       builder.withCompression(properties.getCompression());
/*     */     }
/*  75 */     if (properties.getLoadBalancingPolicy() != null) {
/*  76 */       LoadBalancingPolicy policy = (LoadBalancingPolicy)instantiate(properties.getLoadBalancingPolicy());
/*  77 */       builder.withLoadBalancingPolicy(policy);
/*     */     }
/*  79 */     builder.withQueryOptions(getQueryOptions());
/*  80 */     if (properties.getReconnectionPolicy() != null) {
/*  81 */       ReconnectionPolicy policy = (ReconnectionPolicy)instantiate(properties.getReconnectionPolicy());
/*  82 */       builder.withReconnectionPolicy(policy);
/*     */     }
/*  84 */     if (properties.getRetryPolicy() != null) {
/*  85 */       RetryPolicy policy = (RetryPolicy)instantiate(properties.getRetryPolicy());
/*  86 */       builder.withRetryPolicy(policy);
/*     */     }
/*  88 */     builder.withSocketOptions(getSocketOptions());
/*  89 */     if (properties.isSsl()) {
/*  90 */       builder.withSSL();
/*     */     }
/*  92 */     String points = properties.getContactPoints();
/*  93 */     builder.addContactPoints(StringUtils.commaDelimitedListToStringArray(points));
/*     */     
/*  95 */     customize(builder);
/*  96 */     return builder.build();
/*     */   }
/*     */   
/*     */   private void customize(Cluster.Builder builder) {
/* 100 */     if (this.builderCustomizers != null) {
/* 101 */       for (ClusterBuilderCustomizer customizer : this.builderCustomizers) {
/* 102 */         customizer.customize(builder);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T> T instantiate(Class<T> type) {
/* 108 */     return (T)BeanUtils.instantiate(type);
/*     */   }
/*     */   
/*     */   private QueryOptions getQueryOptions() {
/* 112 */     QueryOptions options = new QueryOptions();
/* 113 */     CassandraProperties properties = this.properties;
/* 114 */     if (properties.getConsistencyLevel() != null) {
/* 115 */       options.setConsistencyLevel(properties.getConsistencyLevel());
/*     */     }
/* 117 */     if (properties.getSerialConsistencyLevel() != null) {
/* 118 */       options.setSerialConsistencyLevel(properties.getSerialConsistencyLevel());
/*     */     }
/* 120 */     options.setFetchSize(properties.getFetchSize());
/* 121 */     return options;
/*     */   }
/*     */   
/*     */   private SocketOptions getSocketOptions() {
/* 125 */     SocketOptions options = new SocketOptions();
/* 126 */     options.setConnectTimeoutMillis(this.properties.getConnectTimeoutMillis());
/* 127 */     options.setReadTimeoutMillis(this.properties.getReadTimeoutMillis());
/* 128 */     return options;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\cassandra\CassandraAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */