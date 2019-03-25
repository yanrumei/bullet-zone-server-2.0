/*    */ package org.springframework.boot.autoconfigure.solr;
/*    */ 
/*    */ import org.apache.solr.client.solrj.SolrClient;
/*    */ import org.apache.solr.client.solrj.impl.CloudSolrClient;
/*    */ import org.apache.solr.client.solrj.impl.HttpSolrClient;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ @ConditionalOnClass({HttpSolrClient.class, CloudSolrClient.class})
/*    */ @EnableConfigurationProperties({SolrProperties.class})
/*    */ public class SolrAutoConfiguration
/*    */ {
/*    */   private final SolrProperties properties;
/*    */   private SolrClient solrClient;
/*    */   
/*    */   public SolrAutoConfiguration(SolrProperties properties)
/*    */   {
/* 47 */     this.properties = properties;
/*    */   }
/*    */   
/*    */   @Bean
/*    */   @ConditionalOnMissingBean
/*    */   public SolrClient solrClient() {
/* 53 */     this.solrClient = createSolrClient();
/* 54 */     return this.solrClient;
/*    */   }
/*    */   
/*    */   private SolrClient createSolrClient() {
/* 58 */     if (StringUtils.hasText(this.properties.getZkHost())) {
/* 59 */       return new CloudSolrClient(this.properties.getZkHost());
/*    */     }
/* 61 */     return new HttpSolrClient(this.properties.getHost());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\solr\SolrAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */