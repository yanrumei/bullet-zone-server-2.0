/*     */ package org.springframework.boot.autoconfigure.data.elasticsearch;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Properties;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.elasticsearch.client.Client;
/*     */ import org.elasticsearch.client.transport.TransportClient;
/*     */ import org.elasticsearch.common.lease.Releasable;
/*     */ import org.elasticsearch.common.settings.Settings;
/*     */ import org.elasticsearch.common.settings.Settings.Builder;
/*     */ import org.elasticsearch.node.Node;
/*     */ import org.elasticsearch.node.NodeBuilder;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.data.elasticsearch.client.NodeClientFactoryBean;
/*     */ import org.springframework.data.elasticsearch.client.TransportClientFactoryBean;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ @Configuration
/*     */ @ConditionalOnClass({Client.class, TransportClientFactoryBean.class, NodeClientFactoryBean.class})
/*     */ @EnableConfigurationProperties({ElasticsearchProperties.class})
/*     */ public class ElasticsearchAutoConfiguration
/*     */   implements DisposableBean
/*     */ {
/*     */   private static final Map<String, String> DEFAULTS;
/*     */   
/*     */   static
/*     */   {
/*  62 */     Map<String, String> defaults = new LinkedHashMap();
/*  63 */     defaults.put("http.enabled", String.valueOf(false));
/*  64 */     defaults.put("node.local", String.valueOf(true));
/*  65 */     defaults.put("path.home", System.getProperty("user.dir"));
/*  66 */     DEFAULTS = Collections.unmodifiableMap(defaults);
/*     */   }
/*     */   
/*     */ 
/*  70 */   private static final Log logger = LogFactory.getLog(ElasticsearchAutoConfiguration.class);
/*     */   
/*     */   private final ElasticsearchProperties properties;
/*     */   private Releasable releasable;
/*     */   
/*     */   public ElasticsearchAutoConfiguration(ElasticsearchProperties properties)
/*     */   {
/*  77 */     this.properties = properties;
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean
/*     */   public Client elasticsearchClient() {
/*     */     try {
/*  84 */       return createClient();
/*     */     }
/*     */     catch (Exception ex) {
/*  87 */       throw new IllegalStateException(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private Client createClient() throws Exception {
/*  92 */     if (StringUtils.hasLength(this.properties.getClusterNodes())) {
/*  93 */       return createTransportClient();
/*     */     }
/*  95 */     return createNodeClient();
/*     */   }
/*     */   
/*     */   private Client createNodeClient() throws Exception {
/*  99 */     Settings.Builder settings = Settings.settingsBuilder();
/* 100 */     for (Map.Entry<String, String> entry : DEFAULTS.entrySet()) {
/* 101 */       if (!this.properties.getProperties().containsKey(entry.getKey())) {
/* 102 */         settings.put((String)entry.getKey(), (String)entry.getValue());
/*     */       }
/*     */     }
/* 105 */     settings.put(this.properties.getProperties());
/*     */     
/* 107 */     Node node = new NodeBuilder().settings(settings).clusterName(this.properties.getClusterName()).node();
/* 108 */     this.releasable = node;
/* 109 */     return node.client();
/*     */   }
/*     */   
/*     */   private Client createTransportClient() throws Exception {
/* 113 */     TransportClientFactoryBean factory = new TransportClientFactoryBean();
/* 114 */     factory.setClusterNodes(this.properties.getClusterNodes());
/* 115 */     factory.setProperties(createProperties());
/* 116 */     factory.afterPropertiesSet();
/* 117 */     TransportClient client = factory.getObject();
/* 118 */     this.releasable = client;
/* 119 */     return client;
/*     */   }
/*     */   
/*     */   private Properties createProperties() {
/* 123 */     Properties properties = new Properties();
/* 124 */     properties.put("cluster.name", this.properties.getClusterName());
/* 125 */     properties.putAll(this.properties.getProperties());
/* 126 */     return properties;
/*     */   }
/*     */   
/*     */   public void destroy() throws Exception
/*     */   {
/* 131 */     if (this.releasable != null) {
/*     */       try {
/* 133 */         if (logger.isInfoEnabled()) {
/* 134 */           logger.info("Closing Elasticsearch client");
/*     */         }
/*     */         try {
/* 137 */           this.releasable.close();
/*     */         }
/*     */         catch (NoSuchMethodError ex)
/*     */         {
/* 141 */           ReflectionUtils.invokeMethod(
/* 142 */             ReflectionUtils.findMethod(Releasable.class, "release"), this.releasable);
/*     */         }
/*     */       }
/*     */       catch (Exception ex)
/*     */       {
/* 147 */         if (logger.isErrorEnabled()) {
/* 148 */           logger.error("Error closing Elasticsearch client: ", ex);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\data\elasticsearch\ElasticsearchAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */