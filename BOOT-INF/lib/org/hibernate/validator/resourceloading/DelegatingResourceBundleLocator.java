/*    */ package org.hibernate.validator.resourceloading;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import java.util.ResourceBundle;
/*    */ import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;
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
/*    */ public abstract class DelegatingResourceBundleLocator
/*    */   implements ResourceBundleLocator
/*    */ {
/*    */   private final ResourceBundleLocator delegate;
/*    */   
/*    */   public DelegatingResourceBundleLocator(ResourceBundleLocator delegate)
/*    */   {
/* 30 */     this.delegate = delegate;
/*    */   }
/*    */   
/*    */   public ResourceBundle getResourceBundle(Locale locale)
/*    */   {
/* 35 */     return this.delegate == null ? null : this.delegate.getResourceBundle(locale);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\resourceloading\DelegatingResourceBundleLocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */