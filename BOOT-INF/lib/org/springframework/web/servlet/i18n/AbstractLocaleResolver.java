/*    */ package org.springframework.web.servlet.i18n;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import org.springframework.web.servlet.LocaleResolver;
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
/*    */ public abstract class AbstractLocaleResolver
/*    */   implements LocaleResolver
/*    */ {
/*    */   private Locale defaultLocale;
/*    */   
/*    */   public void setDefaultLocale(Locale defaultLocale)
/*    */   {
/* 40 */     this.defaultLocale = defaultLocale;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected Locale getDefaultLocale()
/*    */   {
/* 47 */     return this.defaultLocale;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\i18n\AbstractLocaleResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */