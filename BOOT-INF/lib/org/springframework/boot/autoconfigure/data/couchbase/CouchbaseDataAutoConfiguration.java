/*    */ package org.springframework.boot.autoconfigure.data.couchbase;
/*    */ 
/*    */ import com.couchbase.client.java.Bucket;
/*    */ import javax.validation.Validator;
/*    */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
/*    */ import org.springframework.boot.autoconfigure.couchbase.CouchbaseAutoConfiguration;
/*    */ import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
/*    */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.annotation.Import;
/*    */ import org.springframework.data.couchbase.core.mapping.event.ValidatingCouchbaseEventListener;
/*    */ import org.springframework.data.couchbase.repository.CouchbaseRepository;
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
/*    */ @ConditionalOnClass({Bucket.class, CouchbaseRepository.class})
/*    */ @AutoConfigureAfter({CouchbaseAutoConfiguration.class, ValidationAutoConfiguration.class})
/*    */ @EnableConfigurationProperties({CouchbaseDataProperties.class})
/*    */ @Import({CouchbaseConfigurerAdapterConfiguration.class, SpringBootCouchbaseDataConfiguration.class})
/*    */ public class CouchbaseDataAutoConfiguration
/*    */ {
/*    */   @Configuration
/*    */   @ConditionalOnClass({Validator.class})
/*    */   public static class ValidationConfiguration
/*    */   {
/*    */     @Bean
/*    */     @ConditionalOnSingleCandidate(Validator.class)
/*    */     public ValidatingCouchbaseEventListener validationEventListener(Validator validator)
/*    */     {
/* 60 */       return new ValidatingCouchbaseEventListener(validator);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\data\couchbase\CouchbaseDataAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */