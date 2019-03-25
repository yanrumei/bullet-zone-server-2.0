/*    */ package org.springframework.validation.beanvalidation;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import java.util.ResourceBundle;
/*    */ import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;
/*    */ import org.springframework.context.MessageSource;
/*    */ import org.springframework.context.support.MessageSourceResourceBundle;
/*    */ import org.springframework.util.Assert;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MessageSourceResourceBundleLocator
/*    */   implements ResourceBundleLocator
/*    */ {
/*    */   private final MessageSource messageSource;
/*    */   
/*    */   public MessageSourceResourceBundleLocator(MessageSource messageSource)
/*    */   {
/* 47 */     Assert.notNull(messageSource, "MessageSource must not be null");
/* 48 */     this.messageSource = messageSource;
/*    */   }
/*    */   
/*    */   public ResourceBundle getResourceBundle(Locale locale)
/*    */   {
/* 53 */     return new MessageSourceResourceBundle(this.messageSource, locale);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\validation\beanvalidation\MessageSourceResourceBundleLocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */