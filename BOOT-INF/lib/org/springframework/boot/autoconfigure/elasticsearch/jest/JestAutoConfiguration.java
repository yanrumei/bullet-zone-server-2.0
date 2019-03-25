/*    */ package org.springframework.boot.autoconfigure.elasticsearch.jest;
/*    */ 
/*    */ import com.google.gson.Gson;
/*    */ import io.searchbox.client.JestClient;
/*    */ import io.searchbox.client.JestClientFactory;
/*    */ import io.searchbox.client.config.HttpClientConfig;
/*    */ import io.searchbox.client.config.HttpClientConfig.Builder;
/*    */ import java.util.List;
/*    */ import org.apache.http.HttpHost;
/*    */ import org.springframework.beans.factory.ObjectProvider;
/*    */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
/*    */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.util.Assert;
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
/*    */ 
/*    */ 
/*    */ @Configuration
/*    */ @ConditionalOnClass({JestClient.class})
/*    */ @EnableConfigurationProperties({JestProperties.class})
/*    */ @AutoConfigureAfter({GsonAutoConfiguration.class})
/*    */ public class JestAutoConfiguration
/*    */ {
/*    */   private final JestProperties properties;
/*    */   private final ObjectProvider<Gson> gsonProvider;
/*    */   private final List<HttpClientConfigBuilderCustomizer> builderCustomizers;
/*    */   
/*    */   public JestAutoConfiguration(JestProperties properties, ObjectProvider<Gson> gson, ObjectProvider<List<HttpClientConfigBuilderCustomizer>> builderCustomizers)
/*    */   {
/* 59 */     this.properties = properties;
/* 60 */     this.gsonProvider = gson;
/* 61 */     this.builderCustomizers = ((List)builderCustomizers.getIfAvailable());
/*    */   }
/*    */   
/*    */   @Bean(destroyMethod="shutdownClient")
/*    */   @ConditionalOnMissingBean
/*    */   public JestClient jestClient() {
/* 67 */     JestClientFactory factory = new JestClientFactory();
/* 68 */     factory.setHttpClientConfig(createHttpClientConfig());
/* 69 */     return factory.getObject();
/*    */   }
/*    */   
/*    */   protected HttpClientConfig createHttpClientConfig()
/*    */   {
/* 74 */     HttpClientConfig.Builder builder = new HttpClientConfig.Builder(this.properties.getUris());
/* 75 */     if (StringUtils.hasText(this.properties.getUsername())) {
/* 76 */       builder.defaultCredentials(this.properties.getUsername(), this.properties
/* 77 */         .getPassword());
/*    */     }
/* 79 */     String proxyHost = this.properties.getProxy().getHost();
/* 80 */     if (StringUtils.hasText(proxyHost)) {
/* 81 */       Integer proxyPort = this.properties.getProxy().getPort();
/* 82 */       Assert.notNull(proxyPort, "Proxy port must not be null");
/* 83 */       builder.proxy(new HttpHost(proxyHost, proxyPort.intValue()));
/*    */     }
/* 85 */     Gson gson = (Gson)this.gsonProvider.getIfUnique();
/* 86 */     if (gson != null) {
/* 87 */       builder.gson(gson);
/*    */     }
/* 89 */     builder.multiThreaded(this.properties.isMultiThreaded());
/* 90 */     ((HttpClientConfig.Builder)builder.connTimeout(this.properties.getConnectionTimeout()))
/* 91 */       .readTimeout(this.properties.getReadTimeout());
/* 92 */     customize(builder);
/* 93 */     return builder.build();
/*    */   }
/*    */   
/*    */   private void customize(HttpClientConfig.Builder builder) {
/* 97 */     if (this.builderCustomizers != null) {
/* 98 */       for (HttpClientConfigBuilderCustomizer customizer : this.builderCustomizers) {
/* 99 */         customizer.customize(builder);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\elasticsearch\jest\JestAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */