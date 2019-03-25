/*    */ package org.springframework.boot.autoconfigure.mail;
/*    */ 
/*    */ import javax.annotation.PostConstruct;
/*    */ import javax.mail.MessagingException;
/*    */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.mail.javamail.JavaMailSenderImpl;
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
/*    */ @AutoConfigureAfter({MailSenderAutoConfiguration.class})
/*    */ @ConditionalOnProperty(prefix="spring.mail", value={"test-connection"})
/*    */ @ConditionalOnSingleCandidate(JavaMailSenderImpl.class)
/*    */ public class MailSenderValidatorAutoConfiguration
/*    */ {
/*    */   private final JavaMailSenderImpl mailSender;
/*    */   
/*    */   public MailSenderValidatorAutoConfiguration(JavaMailSenderImpl mailSender)
/*    */   {
/* 46 */     this.mailSender = mailSender;
/*    */   }
/*    */   
/*    */   @PostConstruct
/*    */   public void validateConnection() {
/*    */     try {
/* 52 */       this.mailSender.testConnection();
/*    */     }
/*    */     catch (MessagingException ex) {
/* 55 */       throw new IllegalStateException("Mail server is not available", ex);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\mail\MailSenderValidatorAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */