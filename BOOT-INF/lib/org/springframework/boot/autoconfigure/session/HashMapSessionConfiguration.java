/*    */ package org.springframework.boot.autoconfigure.session;
/*    */ 
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Conditional;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.session.ExpiringSession;
/*    */ import org.springframework.session.MapSessionRepository;
/*    */ import org.springframework.session.SessionRepository;
/*    */ import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
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
/*    */ @EnableSpringHttpSession
/*    */ @Conditional({SessionCondition.class})
/*    */ @ConditionalOnMissingBean({SessionRepository.class})
/*    */ class HashMapSessionConfiguration
/*    */ {
/*    */   @Bean
/*    */   public SessionRepository<ExpiringSession> sessionRepository(SessionProperties properties)
/*    */   {
/* 43 */     MapSessionRepository repository = new MapSessionRepository();
/* 44 */     Integer timeout = properties.getTimeout();
/* 45 */     if (timeout != null) {
/* 46 */       repository.setDefaultMaxInactiveInterval(timeout.intValue());
/*    */     }
/* 48 */     return repository;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\session\HashMapSessionConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */