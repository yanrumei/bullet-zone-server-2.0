/*    */ package org.hibernate.validator.resourceloading;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import java.util.ResourceBundle;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ConcurrentMap;
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
/*    */ public class CachingResourceBundleLocator
/*    */   extends DelegatingResourceBundleLocator
/*    */ {
/* 25 */   private final ConcurrentMap<Locale, ResourceBundle> bundleCache = new ConcurrentHashMap();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public CachingResourceBundleLocator(ResourceBundleLocator delegate)
/*    */   {
/* 33 */     super(delegate);
/*    */   }
/*    */   
/*    */   public ResourceBundle getResourceBundle(Locale locale)
/*    */   {
/* 38 */     ResourceBundle cachedResourceBundle = (ResourceBundle)this.bundleCache.get(locale);
/* 39 */     if (cachedResourceBundle == null) {
/* 40 */       ResourceBundle bundle = super.getResourceBundle(locale);
/* 41 */       if (bundle != null) {
/* 42 */         cachedResourceBundle = (ResourceBundle)this.bundleCache.putIfAbsent(locale, bundle);
/* 43 */         if (cachedResourceBundle == null) {
/* 44 */           return bundle;
/*    */         }
/*    */       }
/*    */     }
/* 48 */     return cachedResourceBundle;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\resourceloading\CachingResourceBundleLocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */