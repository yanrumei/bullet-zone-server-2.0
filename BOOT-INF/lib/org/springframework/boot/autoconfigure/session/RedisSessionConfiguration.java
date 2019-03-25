/*    */ package org.springframework.boot.autoconfigure.session;
/*    */ 
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.context.annotation.Conditional;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.data.redis.connection.RedisConnectionFactory;
/*    */ import org.springframework.data.redis.core.RedisTemplate;
/*    */ import org.springframework.session.SessionRepository;
/*    */ import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;
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
/*    */ @ConditionalOnClass({RedisTemplate.class})
/*    */ @ConditionalOnMissingBean({SessionRepository.class})
/*    */ @ConditionalOnBean({RedisConnectionFactory.class})
/*    */ @Conditional({SessionCondition.class})
/*    */ class RedisSessionConfiguration
/*    */ {
/*    */   @Configuration
/*    */   public static class SpringBootRedisHttpSessionConfiguration
/*    */     extends RedisHttpSessionConfiguration
/*    */   {
/*    */     private SessionProperties sessionProperties;
/*    */     
/*    */     @Autowired
/*    */     public void customize(SessionProperties sessionProperties)
/*    */     {
/* 54 */       this.sessionProperties = sessionProperties;
/* 55 */       Integer timeout = this.sessionProperties.getTimeout();
/* 56 */       if (timeout != null) {
/* 57 */         setMaxInactiveIntervalInSeconds(timeout.intValue());
/*    */       }
/* 59 */       SessionProperties.Redis redis = this.sessionProperties.getRedis();
/* 60 */       setRedisNamespace(redis.getNamespace());
/* 61 */       setRedisFlushMode(redis.getFlushMode());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\session\RedisSessionConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */