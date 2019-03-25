/*    */ package org.springframework.boot.autoconfigure.sendgrid;
/*    */ 
/*    */ import com.sendgrid.SendGrid;
/*    */ import org.apache.http.HttpHost;
/*    */ import org.apache.http.impl.client.HttpClientBuilder;
/*    */ import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*    */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Conditional;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.annotation.ConfigurationCondition.ConfigurationPhase;
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
/*    */ @ConditionalOnClass({SendGrid.class})
/*    */ @Conditional({SendGridPropertyCondition.class})
/*    */ @EnableConfigurationProperties({SendGridProperties.class})
/*    */ public class SendGridAutoConfiguration
/*    */ {
/*    */   private final SendGridProperties properties;
/*    */   
/*    */   public SendGridAutoConfiguration(SendGridProperties properties)
/*    */   {
/* 49 */     this.properties = properties;
/*    */   }
/*    */   
/*    */   @Bean
/*    */   @ConditionalOnMissingBean({SendGrid.class})
/*    */   public SendGrid sendGrid() {
/* 55 */     SendGrid sendGrid = createSendGrid();
/* 56 */     if (this.properties.isProxyConfigured())
/*    */     {
/* 58 */       HttpHost proxy = new HttpHost(this.properties.getProxy().getHost(), this.properties.getProxy().getPort().intValue());
/* 59 */       sendGrid.setClient(HttpClientBuilder.create().setProxy(proxy)
/* 60 */         .setUserAgent("sendgrid/" + sendGrid.getVersion() + ";java").build());
/*    */     }
/* 62 */     return sendGrid;
/*    */   }
/*    */   
/*    */   private SendGrid createSendGrid() {
/* 66 */     if (this.properties.getApiKey() != null) {
/* 67 */       return new SendGrid(this.properties.getApiKey());
/*    */     }
/* 69 */     return new SendGrid(this.properties.getUsername(), this.properties.getPassword());
/*    */   }
/*    */   
/*    */   static class SendGridPropertyCondition extends AnyNestedCondition
/*    */   {
/*    */     SendGridPropertyCondition() {
/* 75 */       super();
/*    */     }
/*    */     
/*    */     @ConditionalOnProperty(prefix="spring.sendgrid", value={"api-key"})
/*    */     static class SendGridApiKeyProperty {}
/*    */     
/*    */     @ConditionalOnProperty(prefix="spring.sendgrid", value={"username"})
/*    */     static class SendGridUserProperty {}
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\sendgrid\SendGridAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */