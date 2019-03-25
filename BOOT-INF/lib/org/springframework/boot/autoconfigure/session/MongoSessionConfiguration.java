/*    */ package org.springframework.boot.autoconfigure.session;
/*    */ 
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.context.annotation.Conditional;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.data.mongodb.core.MongoOperations;
/*    */ import org.springframework.session.SessionRepository;
/*    */ import org.springframework.session.data.mongo.config.annotation.web.http.MongoHttpSessionConfiguration;
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
/*    */ @ConditionalOnMissingBean({SessionRepository.class})
/*    */ @ConditionalOnBean({MongoOperations.class})
/*    */ @Conditional({SessionCondition.class})
/*    */ class MongoSessionConfiguration
/*    */ {
/*    */   @Configuration
/*    */   public static class SpringBootMongoHttpSessionConfiguration
/*    */     extends MongoHttpSessionConfiguration
/*    */   {
/*    */     @Autowired
/*    */     public void customize(SessionProperties sessionProperties)
/*    */     {
/* 46 */       Integer timeout = sessionProperties.getTimeout();
/* 47 */       if (timeout != null) {
/* 48 */         setMaxInactiveIntervalInSeconds(timeout);
/*    */       }
/* 50 */       setCollectionName(sessionProperties.getMongo().getCollectionName());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\session\MongoSessionConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */