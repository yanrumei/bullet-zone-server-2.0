/*    */ package org.springframework.boot.autoconfigure.mongo;
/*    */ 
/*    */ import com.mongodb.MongoClient;
/*    */ import com.mongodb.MongoClientOptions;
/*    */ import java.net.UnknownHostException;
/*    */ import javax.annotation.PreDestroy;
/*    */ import org.springframework.beans.factory.ObjectProvider;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.core.env.Environment;
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
/*    */ @ConditionalOnClass({MongoClient.class})
/*    */ @EnableConfigurationProperties({MongoProperties.class})
/*    */ @ConditionalOnMissingBean(type={"org.springframework.data.mongodb.MongoDbFactory"})
/*    */ public class MongoAutoConfiguration
/*    */ {
/*    */   private final MongoProperties properties;
/*    */   private final MongoClientOptions options;
/*    */   private final Environment environment;
/*    */   private MongoClient mongo;
/*    */   
/*    */   public MongoAutoConfiguration(MongoProperties properties, ObjectProvider<MongoClientOptions> options, Environment environment)
/*    */   {
/* 58 */     this.properties = properties;
/* 59 */     this.options = ((MongoClientOptions)options.getIfAvailable());
/* 60 */     this.environment = environment;
/*    */   }
/*    */   
/*    */   @PreDestroy
/*    */   public void close() {
/* 65 */     if (this.mongo != null) {
/* 66 */       this.mongo.close();
/*    */     }
/*    */   }
/*    */   
/*    */   @Bean
/*    */   @ConditionalOnMissingBean
/*    */   public MongoClient mongo() throws UnknownHostException {
/* 73 */     this.mongo = this.properties.createMongoClient(this.options, this.environment);
/* 74 */     return this.mongo;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\mongo\MongoAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */