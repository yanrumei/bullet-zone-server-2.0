/*    */ package org.springframework.boot.autoconfigure.context;
/*    */ 
/*    */ import org.springframework.boot.autoconfigure.AutoConfigureOrder;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.condition.SearchStrategy;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
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
/*    */ @AutoConfigureOrder(Integer.MIN_VALUE)
/*    */ public class PropertyPlaceholderAutoConfiguration
/*    */ {
/*    */   @Bean
/*    */   @ConditionalOnMissingBean(search=SearchStrategy.CURRENT)
/*    */   public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer()
/*    */   {
/* 42 */     return new PropertySourcesPlaceholderConfigurer();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\context\PropertyPlaceholderAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */