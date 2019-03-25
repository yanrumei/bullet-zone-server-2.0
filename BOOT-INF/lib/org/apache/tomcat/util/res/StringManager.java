/*     */ package org.apache.tomcat.util.res;
/*     */ 
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
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
/*     */ public class StringManager
/*     */ {
/*  54 */   private static int LOCALE_CACHE_SIZE = 10;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ResourceBundle bundle;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Locale locale;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private StringManager(String packageName, Locale locale)
/*     */   {
/*  72 */     String bundleName = packageName + ".LocalStrings";
/*  73 */     ResourceBundle bnd = null;
/*     */     try {
/*  75 */       bnd = ResourceBundle.getBundle(bundleName, locale);
/*     */ 
/*     */     }
/*     */     catch (MissingResourceException ex)
/*     */     {
/*  80 */       ClassLoader cl = Thread.currentThread().getContextClassLoader();
/*  81 */       if (cl != null) {
/*     */         try {
/*  83 */           bnd = ResourceBundle.getBundle(bundleName, locale, cl);
/*     */         }
/*     */         catch (MissingResourceException localMissingResourceException1) {}
/*     */       }
/*     */     }
/*     */     
/*  89 */     this.bundle = bnd;
/*     */     
/*  91 */     if (this.bundle != null) {
/*  92 */       Locale bundleLocale = this.bundle.getLocale();
/*  93 */       if (bundleLocale.equals(Locale.ROOT)) {
/*  94 */         this.locale = Locale.ENGLISH;
/*     */       } else {
/*  96 */         this.locale = bundleLocale;
/*     */       }
/*     */     } else {
/*  99 */       this.locale = null;
/*     */     }
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
/*     */   public String getString(String key)
/*     */   {
/* 116 */     if (key == null) {
/* 117 */       String msg = "key may not have a null value";
/* 118 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */     
/* 121 */     String str = null;
/*     */     
/*     */     try
/*     */     {
/* 125 */       if (this.bundle != null) {
/* 126 */         str = this.bundle.getString(key);
/*     */ 
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (MissingResourceException mre)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 140 */       str = null;
/*     */     }
/*     */     
/* 143 */     return str;
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
/*     */   public String getString(String key, Object... args)
/*     */   {
/* 158 */     String value = getString(key);
/* 159 */     if (value == null) {
/* 160 */       value = key;
/*     */     }
/*     */     
/* 163 */     MessageFormat mf = new MessageFormat(value);
/* 164 */     mf.setLocale(this.locale);
/* 165 */     return mf.format(args, new StringBuffer(), null).toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Locale getLocale()
/*     */   {
/* 175 */     return this.locale;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 183 */   private static final Map<String, Map<Locale, StringManager>> managers = new Hashtable();
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
/*     */   public static final StringManager getManager(Class<?> clazz)
/*     */   {
/* 198 */     return getManager(clazz.getPackage().getName());
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
/*     */   public static final StringManager getManager(String packageName)
/*     */   {
/* 213 */     return getManager(packageName, Locale.getDefault());
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
/*     */   public static final synchronized StringManager getManager(String packageName, Locale locale)
/*     */   {
/* 230 */     Map<Locale, StringManager> map = (Map)managers.get(packageName);
/* 231 */     if (map == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 240 */       map = new LinkedHashMap(LOCALE_CACHE_SIZE, 1.0F, true)
/*     */       {
/*     */         private static final long serialVersionUID = 1L;
/*     */         
/*     */         protected boolean removeEldestEntry(Map.Entry<Locale, StringManager> eldest) {
/* 245 */           if (size() > StringManager.LOCALE_CACHE_SIZE - 1) {
/* 246 */             return true;
/*     */           }
/* 248 */           return false;
/*     */         }
/* 250 */       };
/* 251 */       managers.put(packageName, map);
/*     */     }
/*     */     
/* 254 */     StringManager mgr = (StringManager)map.get(locale);
/* 255 */     if (mgr == null) {
/* 256 */       mgr = new StringManager(packageName, locale);
/* 257 */       map.put(locale, mgr);
/*     */     }
/* 259 */     return mgr;
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
/*     */   public static StringManager getManager(String packageName, Enumeration<Locale> requestedLocales)
/*     */   {
/* 275 */     while (requestedLocales.hasMoreElements()) {
/* 276 */       Locale locale = (Locale)requestedLocales.nextElement();
/* 277 */       StringManager result = getManager(packageName, locale);
/* 278 */       if (result.getLocale().equals(locale)) {
/* 279 */         return result;
/*     */       }
/*     */     }
/*     */     
/* 283 */     return getManager(packageName);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\res\StringManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */