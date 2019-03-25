/*    */ package org.springframework.boot.autoconfigure.hazelcast;
/*    */ 
/*    */ import com.hazelcast.core.HazelcastInstance;
/*    */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*    */ import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.data.jpa.EntityManagerFactoryDependsOnPostProcessor;
/*    */ import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Conditional;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.annotation.ConfigurationCondition.ConfigurationPhase;
/*    */ import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
/*    */ import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
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
/*    */ @ConditionalOnClass({HazelcastInstance.class, LocalContainerEntityManagerFactoryBean.class})
/*    */ @AutoConfigureAfter({HazelcastAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
/*    */ public class HazelcastJpaDependencyAutoConfiguration
/*    */ {
/*    */   @Bean
/*    */   @Conditional({OnHazelcastAndJpaCondition.class})
/*    */   public static HazelcastInstanceJpaDependencyPostProcessor hazelcastInstanceJpaDependencyPostProcessor()
/*    */   {
/* 52 */     return new HazelcastInstanceJpaDependencyPostProcessor();
/*    */   }
/*    */   
/*    */   private static class HazelcastInstanceJpaDependencyPostProcessor extends EntityManagerFactoryDependsOnPostProcessor
/*    */   {
/*    */     HazelcastInstanceJpaDependencyPostProcessor()
/*    */     {
/* 59 */       super();
/*    */     }
/*    */   }
/*    */   
/*    */   static class OnHazelcastAndJpaCondition extends AllNestedConditions
/*    */   {
/*    */     OnHazelcastAndJpaCondition()
/*    */     {
/* 67 */       super();
/*    */     }
/*    */     
/*    */     @ConditionalOnBean({AbstractEntityManagerFactoryBean.class})
/*    */     static class HasJpa {}
/*    */     
/*    */     @ConditionalOnBean(name={"hazelcastInstance"})
/*    */     static class HasHazelcastInstance {}
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\hazelcast\HazelcastJpaDependencyAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */