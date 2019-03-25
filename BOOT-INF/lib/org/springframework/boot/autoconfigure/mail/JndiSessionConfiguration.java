/*    */ package org.springframework.boot.autoconfigure.mail;
/*    */ 
/*    */ import javax.mail.Session;
/*    */ import javax.naming.NamingException;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnJndi;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.jndi.JndiLocatorDelegate;
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
/*    */ @ConditionalOnClass({Session.class})
/*    */ @ConditionalOnProperty(prefix="spring.mail", name={"jndi-name"})
/*    */ @ConditionalOnJndi
/*    */ class JndiSessionConfiguration
/*    */ {
/*    */   private final MailProperties properties;
/*    */   
/*    */   JndiSessionConfiguration(MailProperties properties)
/*    */   {
/* 45 */     this.properties = properties;
/*    */   }
/*    */   
/*    */   @Bean
/*    */   @ConditionalOnMissingBean
/*    */   public Session session() {
/* 51 */     String jndiName = this.properties.getJndiName();
/*    */     try {
/* 53 */       return (Session)new JndiLocatorDelegate().lookup(jndiName, Session.class);
/*    */     }
/*    */     catch (NamingException ex)
/*    */     {
/* 57 */       throw new IllegalStateException(String.format("Unable to find Session in JNDI location %s", new Object[] { jndiName }), ex);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\mail\JndiSessionConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */