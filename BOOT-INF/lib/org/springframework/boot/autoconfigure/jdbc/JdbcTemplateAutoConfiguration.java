/*    */ package org.springframework.boot.autoconfigure.jdbc;
/*    */ 
/*    */ import javax.sql.DataSource;
/*    */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.annotation.Primary;
/*    */ import org.springframework.jdbc.core.JdbcOperations;
/*    */ import org.springframework.jdbc.core.JdbcTemplate;
/*    */ import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
/*    */ import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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
/*    */ @ConditionalOnClass({DataSource.class, JdbcTemplate.class})
/*    */ @ConditionalOnSingleCandidate(DataSource.class)
/*    */ @AutoConfigureAfter({DataSourceAutoConfiguration.class})
/*    */ public class JdbcTemplateAutoConfiguration
/*    */ {
/*    */   private final DataSource dataSource;
/*    */   
/*    */   public JdbcTemplateAutoConfiguration(DataSource dataSource)
/*    */   {
/* 53 */     this.dataSource = dataSource;
/*    */   }
/*    */   
/*    */   @Bean
/*    */   @Primary
/*    */   @ConditionalOnMissingBean({JdbcOperations.class})
/*    */   public JdbcTemplate jdbcTemplate() {
/* 60 */     return new JdbcTemplate(this.dataSource);
/*    */   }
/*    */   
/*    */   @Bean
/*    */   @Primary
/*    */   @ConditionalOnMissingBean({NamedParameterJdbcOperations.class})
/*    */   public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
/* 67 */     return new NamedParameterJdbcTemplate(this.dataSource);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jdbc\JdbcTemplateAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */