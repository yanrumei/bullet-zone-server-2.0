/*    */ package org.springframework.boot.autoconfigure.data.elasticsearch;
/*    */ 
/*    */ import org.elasticsearch.client.Client;
/*    */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
/*    */ import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
/*    */ import org.springframework.data.elasticsearch.core.convert.MappingElasticsearchConverter;
/*    */ import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @Configuration
/*    */ @ConditionalOnClass({Client.class, ElasticsearchTemplate.class})
/*    */ @AutoConfigureAfter({ElasticsearchAutoConfiguration.class})
/*    */ public class ElasticsearchDataAutoConfiguration
/*    */ {
/*    */   @Bean
/*    */   @ConditionalOnMissingBean
/*    */   @ConditionalOnBean({Client.class})
/*    */   public ElasticsearchTemplate elasticsearchTemplate(Client client, ElasticsearchConverter converter)
/*    */   {
/*    */     try
/*    */     {
/* 57 */       return new ElasticsearchTemplate(client, converter);
/*    */     }
/*    */     catch (Exception ex) {
/* 60 */       throw new IllegalStateException(ex);
/*    */     }
/*    */   }
/*    */   
/*    */   @Bean
/*    */   @ConditionalOnMissingBean
/*    */   public ElasticsearchConverter elasticsearchConverter(SimpleElasticsearchMappingContext mappingContext)
/*    */   {
/* 68 */     return new MappingElasticsearchConverter(mappingContext);
/*    */   }
/*    */   
/*    */   @Bean
/*    */   @ConditionalOnMissingBean
/*    */   public SimpleElasticsearchMappingContext mappingContext() {
/* 74 */     return new SimpleElasticsearchMappingContext();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\data\elasticsearch\ElasticsearchDataAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */