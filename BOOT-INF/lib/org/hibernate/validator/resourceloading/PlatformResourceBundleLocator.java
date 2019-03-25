/*     */ package org.hibernate.validator.resourceloading;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.Properties;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.ResourceBundle.Control;
/*     */ import java.util.Set;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.Contracts;
/*     */ import org.hibernate.validator.internal.util.logging.Messages;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetClassLoader;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetResources;
/*     */ import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;
/*     */ import org.jboss.logging.Logger;
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
/*     */ public class PlatformResourceBundleLocator
/*     */   implements ResourceBundleLocator
/*     */ {
/*  41 */   private static final Logger log = Logger.getLogger(PlatformResourceBundleLocator.class.getName());
/*  42 */   private static final boolean RESOURCE_BUNDLE_CONTROL_INSTANTIABLE = determineAvailabilityOfResourceBundleControl();
/*     */   private final String bundleName;
/*     */   private final ClassLoader classLoader;
/*     */   private final boolean aggregate;
/*     */   
/*     */   public PlatformResourceBundleLocator(String bundleName)
/*     */   {
/*  49 */     this(bundleName, null);
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
/*     */   public PlatformResourceBundleLocator(String bundleName, ClassLoader classLoader)
/*     */   {
/*  63 */     this(bundleName, classLoader, false);
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
/*     */   public PlatformResourceBundleLocator(String bundleName, ClassLoader classLoader, boolean aggregate)
/*     */   {
/*  78 */     Contracts.assertNotNull(bundleName, "bundleName");
/*     */     
/*  80 */     this.bundleName = bundleName;
/*  81 */     this.classLoader = classLoader;
/*     */     
/*  83 */     this.aggregate = ((aggregate) && (RESOURCE_BUNDLE_CONTROL_INSTANTIABLE));
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
/*     */   public ResourceBundle getResourceBundle(Locale locale)
/*     */   {
/*  96 */     ResourceBundle rb = null;
/*     */     
/*  98 */     if (this.classLoader != null) {
/*  99 */       rb = loadBundle(this.classLoader, locale, this.bundleName + " not found by user-provided classloader");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 105 */     if (rb == null) {
/* 106 */       ClassLoader classLoader = (ClassLoader)run(GetClassLoader.fromContext());
/* 107 */       if (classLoader != null) {
/* 108 */         rb = loadBundle(classLoader, locale, this.bundleName + " not found by thread context classloader");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 115 */     if (rb == null) {
/* 116 */       ClassLoader classLoader = (ClassLoader)run(GetClassLoader.fromClass(PlatformResourceBundleLocator.class));
/* 117 */       rb = loadBundle(classLoader, locale, this.bundleName + " not found by validator classloader");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 122 */     if (rb != null) {
/* 123 */       log.debugf("%s found.", this.bundleName);
/*     */     }
/*     */     else {
/* 126 */       log.debugf("%s not found.", this.bundleName);
/*     */     }
/* 128 */     return rb;
/*     */   }
/*     */   
/*     */   private ResourceBundle loadBundle(ClassLoader classLoader, Locale locale, String message) {
/* 132 */     ResourceBundle rb = null;
/*     */     try {
/* 134 */       if (this.aggregate) {
/* 135 */         rb = ResourceBundle.getBundle(this.bundleName, locale, classLoader, AggregateResourceBundle.CONTROL);
/*     */ 
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/*     */ 
/* 143 */         rb = ResourceBundle.getBundle(this.bundleName, locale, classLoader);
/*     */       }
/*     */       
/*     */ 
/*     */     }
/*     */     catch (MissingResourceException e)
/*     */     {
/*     */ 
/* 151 */       log.trace(message);
/*     */     }
/* 153 */     return rb;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static <T> T run(PrivilegedAction<T> action)
/*     */   {
/* 163 */     return (T)(System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run());
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
/*     */   private static boolean determineAvailabilityOfResourceBundleControl()
/*     */   {
/*     */     try
/*     */     {
/* 178 */       ResourceBundle.Control dummyControl = AggregateResourceBundle.CONTROL;
/* 179 */       return true;
/*     */     }
/*     */     catch (NoClassDefFoundError e) {
/* 182 */       log.info(Messages.MESSAGES.unableToUseResourceBundleAggregation()); }
/* 183 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class AggregateResourceBundle
/*     */     extends ResourceBundle
/*     */   {
/* 193 */     protected static final ResourceBundle.Control CONTROL = new PlatformResourceBundleLocator.AggregateResourceBundleControl(null);
/*     */     private final Properties properties;
/*     */     
/*     */     protected AggregateResourceBundle(Properties properties) {
/* 197 */       this.properties = properties;
/*     */     }
/*     */     
/*     */     protected Object handleGetObject(String key)
/*     */     {
/* 202 */       return this.properties.get(key);
/*     */     }
/*     */     
/*     */     public Enumeration<String> getKeys()
/*     */     {
/* 207 */       Set<String> keySet = CollectionHelper.newHashSet();
/* 208 */       keySet.addAll(this.properties.stringPropertyNames());
/* 209 */       if (this.parent != null) {
/* 210 */         keySet.addAll(Collections.list(this.parent.getKeys()));
/*     */       }
/* 212 */       return Collections.enumeration(keySet);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class AggregateResourceBundleControl
/*     */     extends ResourceBundle.Control
/*     */   {
/*     */     public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
/*     */       throws IllegalAccessException, InstantiationException, IOException
/*     */     {
/* 226 */       if (!"java.properties".equals(format)) {
/* 227 */         return super.newBundle(baseName, locale, format, loader, reload);
/*     */       }
/*     */       
/* 230 */       String resourceName = toBundleName(baseName, locale) + ".properties";
/* 231 */       Properties properties = load(resourceName, loader);
/* 232 */       return properties.size() == 0 ? null : new PlatformResourceBundleLocator.AggregateResourceBundle(properties);
/*     */     }
/*     */     
/*     */     private Properties load(String resourceName, ClassLoader loader) throws IOException {
/* 236 */       Properties aggregatedProperties = new Properties();
/* 237 */       Enumeration<URL> urls = (Enumeration)PlatformResourceBundleLocator.run(GetResources.action(loader, resourceName));
/* 238 */       while (urls.hasMoreElements()) {
/* 239 */         URL url = (URL)urls.nextElement();
/* 240 */         Properties properties = new Properties();
/* 241 */         properties.load(url.openStream());
/* 242 */         aggregatedProperties.putAll(properties);
/*     */       }
/* 244 */       return aggregatedProperties;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\resourceloading\PlatformResourceBundleLocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */