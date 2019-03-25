/*     */ package org.springframework.boot.autoconfigure.mail;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import javax.activation.MimeType;
/*     */ import javax.mail.Session;
/*     */ import javax.mail.internet.MimeMessage;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Conditional;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.context.annotation.ConfigurationCondition.ConfigurationPhase;
/*     */ import org.springframework.context.annotation.Import;
/*     */ import org.springframework.mail.MailSender;
/*     */ import org.springframework.mail.javamail.JavaMailSenderImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Configuration
/*     */ @ConditionalOnClass({MimeMessage.class, MimeType.class})
/*     */ @ConditionalOnMissingBean({MailSender.class})
/*     */ @Conditional({MailSenderCondition.class})
/*     */ @EnableConfigurationProperties({MailProperties.class})
/*     */ @Import({JndiSessionConfiguration.class})
/*     */ public class MailSenderAutoConfiguration
/*     */ {
/*     */   private final MailProperties properties;
/*     */   private final Session session;
/*     */   
/*     */   public MailSenderAutoConfiguration(MailProperties properties, ObjectProvider<Session> session)
/*     */   {
/*  63 */     this.properties = properties;
/*  64 */     this.session = ((Session)session.getIfAvailable());
/*     */   }
/*     */   
/*     */   @Bean
/*     */   public JavaMailSenderImpl mailSender() {
/*  69 */     JavaMailSenderImpl sender = new JavaMailSenderImpl();
/*  70 */     if (this.session != null) {
/*  71 */       sender.setSession(this.session);
/*     */     }
/*     */     else {
/*  74 */       applyProperties(sender);
/*     */     }
/*  76 */     return sender;
/*     */   }
/*     */   
/*     */   private void applyProperties(JavaMailSenderImpl sender) {
/*  80 */     sender.setHost(this.properties.getHost());
/*  81 */     if (this.properties.getPort() != null) {
/*  82 */       sender.setPort(this.properties.getPort().intValue());
/*     */     }
/*  84 */     sender.setUsername(this.properties.getUsername());
/*  85 */     sender.setPassword(this.properties.getPassword());
/*  86 */     sender.setProtocol(this.properties.getProtocol());
/*  87 */     if (this.properties.getDefaultEncoding() != null) {
/*  88 */       sender.setDefaultEncoding(this.properties.getDefaultEncoding().name());
/*     */     }
/*  90 */     if (!this.properties.getProperties().isEmpty()) {
/*  91 */       sender.setJavaMailProperties(asProperties(this.properties.getProperties()));
/*     */     }
/*     */   }
/*     */   
/*     */   private Properties asProperties(Map<String, String> source) {
/*  96 */     Properties properties = new Properties();
/*  97 */     properties.putAll(source);
/*  98 */     return properties;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static class MailSenderCondition
/*     */     extends AnyNestedCondition
/*     */   {
/*     */     MailSenderCondition()
/*     */     {
/* 108 */       super();
/*     */     }
/*     */     
/*     */     @ConditionalOnProperty(prefix="spring.mail", name={"jndi-name"})
/*     */     static class JndiNameProperty {}
/*     */     
/*     */     @ConditionalOnProperty(prefix="spring.mail", name={"host"})
/*     */     static class HostProperty {}
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\mail\MailSenderAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */