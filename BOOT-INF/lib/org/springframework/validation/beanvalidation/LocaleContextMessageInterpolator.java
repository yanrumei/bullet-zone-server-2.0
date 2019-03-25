/*    */ package org.springframework.validation.beanvalidation;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import javax.validation.MessageInterpolator;
/*    */ import javax.validation.MessageInterpolator.Context;
/*    */ import org.springframework.context.i18n.LocaleContextHolder;
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
/*    */ public class LocaleContextMessageInterpolator
/*    */   implements MessageInterpolator
/*    */ {
/*    */   private final MessageInterpolator targetInterpolator;
/*    */   
/*    */   public LocaleContextMessageInterpolator(MessageInterpolator targetInterpolator)
/*    */   {
/* 43 */     Assert.notNull(targetInterpolator, "Target MessageInterpolator must not be null");
/* 44 */     this.targetInterpolator = targetInterpolator;
/*    */   }
/*    */   
/*    */ 
/*    */   public String interpolate(String message, MessageInterpolator.Context context)
/*    */   {
/* 50 */     return this.targetInterpolator.interpolate(message, context, LocaleContextHolder.getLocale());
/*    */   }
/*    */   
/*    */   public String interpolate(String message, MessageInterpolator.Context context, Locale locale)
/*    */   {
/* 55 */     return this.targetInterpolator.interpolate(message, context, locale);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\validation\beanvalidation\LocaleContextMessageInterpolator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */