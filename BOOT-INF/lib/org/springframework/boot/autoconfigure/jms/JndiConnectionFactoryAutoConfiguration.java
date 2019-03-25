/*    */ package org.springframework.boot.autoconfigure.jms;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import javax.jms.ConnectionFactory;
/*    */ import javax.naming.NamingException;
/*    */ import org.springframework.boot.autoconfigure.AutoConfigureBefore;
/*    */ import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnJndi;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*    */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Conditional;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.annotation.ConfigurationCondition.ConfigurationPhase;
/*    */ import org.springframework.jms.core.JmsTemplate;
/*    */ import org.springframework.jndi.JndiLocatorDelegate;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ @AutoConfigureBefore({JmsAutoConfiguration.class})
/*    */ @ConditionalOnClass({JmsTemplate.class})
/*    */ @ConditionalOnMissingBean({ConnectionFactory.class})
/*    */ @Conditional({JndiOrPropertyCondition.class})
/*    */ @EnableConfigurationProperties({JmsProperties.class})
/*    */ public class JndiConnectionFactoryAutoConfiguration
/*    */ {
/* 55 */   private static String[] JNDI_LOCATIONS = { "java:/JmsXA", "java:/XAConnectionFactory" };
/*    */   
/*    */   private final JmsProperties properties;
/*    */   
/*    */   public JndiConnectionFactoryAutoConfiguration(JmsProperties properties)
/*    */   {
/* 61 */     this.properties = properties;
/*    */   }
/*    */   
/*    */   @Bean
/*    */   public ConnectionFactory connectionFactory() throws NamingException {
/* 66 */     if (StringUtils.hasLength(this.properties.getJndiName())) {
/* 67 */       return (ConnectionFactory)new JndiLocatorDelegate().lookup(this.properties.getJndiName(), ConnectionFactory.class);
/*    */     }
/*    */     
/* 70 */     return findJndiConnectionFactory();
/*    */   }
/*    */   
/*    */   private ConnectionFactory findJndiConnectionFactory() {
/* 74 */     for (String name : JNDI_LOCATIONS) {
/*    */       try {
/* 76 */         return (ConnectionFactory)new JndiLocatorDelegate().lookup(name, ConnectionFactory.class);
/*    */       }
/*    */       catch (NamingException localNamingException) {}
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 84 */     throw new IllegalStateException("Unable to find ConnectionFactory in JNDI locations " + Arrays.asList(JNDI_LOCATIONS));
/*    */   }
/*    */   
/*    */ 
/*    */   static class JndiOrPropertyCondition
/*    */     extends AnyNestedCondition
/*    */   {
/*    */     JndiOrPropertyCondition()
/*    */     {
/* 93 */       super();
/*    */     }
/*    */     
/*    */     @ConditionalOnProperty(prefix="spring.jms", name={"jndi-name"})
/*    */     static class Property {}
/*    */     
/*    */     @ConditionalOnJndi({"java:/JmsXA", "java:/XAConnectionFactory"})
/*    */     static class Jndi {}
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jms\JndiConnectionFactoryAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */