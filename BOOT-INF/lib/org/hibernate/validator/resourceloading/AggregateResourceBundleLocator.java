/*     */ package org.hibernate.validator.resourceloading;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.Set;
/*     */ import org.hibernate.validator.internal.util.Contracts;
/*     */ import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;
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
/*     */ public class AggregateResourceBundleLocator
/*     */   extends DelegatingResourceBundleLocator
/*     */ {
/*     */   private final List<String> bundleNames;
/*     */   private final ClassLoader classLoader;
/*     */   
/*     */   public AggregateResourceBundleLocator(List<String> bundleNames)
/*     */   {
/*  43 */     this(bundleNames, null);
/*     */   }
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
/*     */   public AggregateResourceBundleLocator(List<String> bundleNames, ResourceBundleLocator delegate)
/*     */   {
/*  59 */     this(bundleNames, delegate, null);
/*     */   }
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
/*     */   public AggregateResourceBundleLocator(List<String> bundleNames, ResourceBundleLocator delegate, ClassLoader classLoader)
/*     */   {
/*  78 */     super(delegate);
/*  79 */     Contracts.assertValueNotNull(bundleNames, "bundleNames");
/*     */     
/*  81 */     this.bundleNames = Collections.unmodifiableList(bundleNames);
/*  82 */     this.classLoader = classLoader;
/*     */   }
/*     */   
/*     */   public ResourceBundle getResourceBundle(Locale locale)
/*     */   {
/*  87 */     List<ResourceBundle> sourceBundles = new ArrayList();
/*     */     
/*  89 */     for (String bundleName : this.bundleNames) {
/*  90 */       ResourceBundleLocator resourceBundleLocator = new PlatformResourceBundleLocator(bundleName, this.classLoader);
/*     */       
/*     */ 
/*  93 */       ResourceBundle resourceBundle = resourceBundleLocator.getResourceBundle(locale);
/*     */       
/*  95 */       if (resourceBundle != null) {
/*  96 */         sourceBundles.add(resourceBundle);
/*     */       }
/*     */     }
/*     */     
/* 100 */     ResourceBundle bundleFromDelegate = super.getResourceBundle(locale);
/*     */     
/* 102 */     if (bundleFromDelegate != null) {
/* 103 */       sourceBundles.add(bundleFromDelegate);
/*     */     }
/*     */     
/* 106 */     return sourceBundles.isEmpty() ? null : new AggregateBundle(sourceBundles);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class AggregateBundle
/*     */     extends ResourceBundle
/*     */   {
/* 118 */     private final Map<String, Object> contents = new HashMap();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public AggregateBundle(List<ResourceBundle> bundles)
/*     */     {
/* 130 */       if (bundles != null)
/*     */       {
/* 132 */         for (ResourceBundle bundle : bundles) {
/* 133 */           Enumeration<String> keys = bundle.getKeys();
/* 134 */           while (keys.hasMoreElements()) {
/* 135 */             String oneKey = (String)keys.nextElement();
/* 136 */             if (!this.contents.containsKey(oneKey)) {
/* 137 */               this.contents.put(oneKey, bundle.getObject(oneKey));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public Enumeration<String> getKeys()
/*     */     {
/* 146 */       return new AggregateResourceBundleLocator.IteratorEnumeration(this.contents.keySet().iterator());
/*     */     }
/*     */     
/*     */     protected Object handleGetObject(String key)
/*     */     {
/* 151 */       return this.contents.get(key);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class IteratorEnumeration<T>
/*     */     implements Enumeration<T>
/*     */   {
/*     */     private final Iterator<T> source;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public IteratorEnumeration(Iterator<T> source)
/*     */     {
/* 174 */       if (source == null) {
/* 175 */         throw new IllegalArgumentException("Source must not be null");
/*     */       }
/*     */       
/* 178 */       this.source = source;
/*     */     }
/*     */     
/*     */     public boolean hasMoreElements()
/*     */     {
/* 183 */       return this.source.hasNext();
/*     */     }
/*     */     
/*     */     public T nextElement()
/*     */     {
/* 188 */       return (T)this.source.next();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\resourceloading\AggregateResourceBundleLocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */