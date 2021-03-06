/*    */ package org.springframework.context.support;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.HashMap;
/*    */ import java.util.Locale;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import org.apache.commons.logging.Log;
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
/*    */ public class StaticMessageSource
/*    */   extends AbstractMessageSource
/*    */ {
/* 39 */   private final Map<String, String> messages = new HashMap();
/*    */   
/* 41 */   private final Map<String, MessageFormat> cachedMessageFormats = new HashMap();
/*    */   
/*    */ 
/*    */   protected String resolveCodeWithoutArguments(String code, Locale locale)
/*    */   {
/* 46 */     return (String)this.messages.get(code + '_' + locale.toString());
/*    */   }
/*    */   
/*    */   protected MessageFormat resolveCode(String code, Locale locale)
/*    */   {
/* 51 */     String key = code + '_' + locale.toString();
/* 52 */     String msg = (String)this.messages.get(key);
/* 53 */     if (msg == null) {
/* 54 */       return null;
/*    */     }
/* 56 */     synchronized (this.cachedMessageFormats) {
/* 57 */       MessageFormat messageFormat = (MessageFormat)this.cachedMessageFormats.get(key);
/* 58 */       if (messageFormat == null) {
/* 59 */         messageFormat = createMessageFormat(msg, locale);
/* 60 */         this.cachedMessageFormats.put(key, messageFormat);
/*    */       }
/* 62 */       return messageFormat;
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void addMessage(String code, Locale locale, String msg)
/*    */   {
/* 73 */     Assert.notNull(code, "Code must not be null");
/* 74 */     Assert.notNull(locale, "Locale must not be null");
/* 75 */     Assert.notNull(msg, "Message must not be null");
/* 76 */     this.messages.put(code + '_' + locale.toString(), msg);
/* 77 */     if (this.logger.isDebugEnabled()) {
/* 78 */       this.logger.debug("Added message [" + msg + "] for code [" + code + "] and Locale [" + locale + "]");
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void addMessages(Map<String, String> messages, Locale locale)
/*    */   {
/* 89 */     Assert.notNull(messages, "Messages Map must not be null");
/* 90 */     for (Map.Entry<String, String> entry : messages.entrySet()) {
/* 91 */       addMessage((String)entry.getKey(), locale, (String)entry.getValue());
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 98 */     return getClass().getName() + ": " + this.messages;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\support\StaticMessageSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */