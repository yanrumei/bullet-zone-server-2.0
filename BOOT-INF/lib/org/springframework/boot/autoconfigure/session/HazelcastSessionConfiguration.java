/*    */ package org.springframework.boot.autoconfigure.session;
/*    */ 
/*    */ import com.hazelcast.core.HazelcastInstance;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.context.annotation.Conditional;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.session.SessionRepository;
/*    */ import org.springframework.session.hazelcast.config.annotation.web.http.HazelcastHttpSessionConfiguration;
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
/*    */ @ConditionalOnMissingBean({SessionRepository.class})
/*    */ @ConditionalOnBean({HazelcastInstance.class})
/*    */ @Conditional({SessionCondition.class})
/*    */ class HazelcastSessionConfiguration
/*    */ {
/*    */   @Configuration
/*    */   public static class SpringBootHazelcastHttpSessionConfiguration
/*    */     extends HazelcastHttpSessionConfiguration
/*    */   {
/*    */     @Autowired
/*    */     public void customize(SessionProperties sessionProperties)
/*    */     {
/* 49 */       Integer timeout = sessionProperties.getTimeout();
/* 50 */       if (timeout != null) {
/* 51 */         setMaxInactiveIntervalInSeconds(timeout.intValue());
/*    */       }
/* 53 */       SessionProperties.Hazelcast hazelcast = sessionProperties.getHazelcast();
/* 54 */       setSessionMapName(hazelcast.getMapName());
/* 55 */       setHazelcastFlushMode(hazelcast.getFlushMode());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\session\HazelcastSessionConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */