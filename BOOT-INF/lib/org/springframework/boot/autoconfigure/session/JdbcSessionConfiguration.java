/*    */ package org.springframework.boot.autoconfigure.session;
/*    */ 
/*    */ import javax.sql.DataSource;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Conditional;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.core.io.ResourceLoader;
/*    */ import org.springframework.jdbc.core.JdbcTemplate;
/*    */ import org.springframework.session.SessionRepository;
/*    */ import org.springframework.session.jdbc.config.annotation.web.http.JdbcHttpSessionConfiguration;
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
/*    */ @ConditionalOnClass({JdbcTemplate.class})
/*    */ @ConditionalOnMissingBean({SessionRepository.class})
/*    */ @ConditionalOnBean({DataSource.class})
/*    */ @Conditional({SessionCondition.class})
/*    */ class JdbcSessionConfiguration
/*    */ {
/*    */   @Bean
/*    */   @ConditionalOnMissingBean
/*    */   public JdbcSessionDatabaseInitializer jdbcSessionDatabaseInitializer(DataSource dataSource, ResourceLoader resourceLoader, SessionProperties properties)
/*    */   {
/* 52 */     return new JdbcSessionDatabaseInitializer(dataSource, resourceLoader, properties);
/*    */   }
/*    */   
/*    */   @Configuration
/*    */   public static class SpringBootJdbcHttpSessionConfiguration extends JdbcHttpSessionConfiguration
/*    */   {
/*    */     @Autowired
/*    */     public void customize(SessionProperties sessionProperties)
/*    */     {
/* 61 */       Integer timeout = sessionProperties.getTimeout();
/* 62 */       if (timeout != null) {
/* 63 */         setMaxInactiveIntervalInSeconds(timeout);
/*    */       }
/* 65 */       setTableName(sessionProperties.getJdbc().getTableName());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\session\JdbcSessionConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */