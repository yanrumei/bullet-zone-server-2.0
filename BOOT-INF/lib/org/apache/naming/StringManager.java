/*     */ package org.apache.naming;
/*     */ 
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Locale;
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
/*     */   private final ResourceBundle bundle;
/*     */   private final Locale locale;
/*     */   
/*     */   private StringManager(String packageName)
/*     */   {
/*  67 */     String bundleName = packageName + ".LocalStrings";
/*  68 */     ResourceBundle tempBundle = null;
/*     */     try {
/*  70 */       tempBundle = ResourceBundle.getBundle(bundleName, Locale.getDefault());
/*     */ 
/*     */     }
/*     */     catch (MissingResourceException ex)
/*     */     {
/*  75 */       ClassLoader cl = Thread.currentThread().getContextClassLoader();
/*  76 */       if (cl != null) {
/*     */         try {
/*  78 */           tempBundle = ResourceBundle.getBundle(bundleName, 
/*  79 */             Locale.getDefault(), cl);
/*     */         }
/*     */         catch (MissingResourceException localMissingResourceException1) {}
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  86 */     if (tempBundle != null) {
/*  87 */       this.locale = tempBundle.getLocale();
/*     */     } else {
/*  89 */       this.locale = null;
/*     */     }
/*  91 */     this.bundle = tempBundle;
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
/*     */   public String getString(String key)
/*     */   {
/* 104 */     if (key == null) {
/* 105 */       String msg = "key may not have a null value";
/*     */       
/* 107 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */     
/* 110 */     String str = null;
/*     */     try
/*     */     {
/* 113 */       str = this.bundle.getString(key);
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
/*     */ 
/* 125 */       str = null;
/*     */     }
/*     */     
/* 128 */     return str;
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
/*     */   public String getString(String key, Object... args)
/*     */   {
/* 142 */     String value = getString(key);
/* 143 */     if (value == null) {
/* 144 */       value = key;
/*     */     }
/*     */     
/* 147 */     MessageFormat mf = new MessageFormat(value);
/* 148 */     mf.setLocale(this.locale);
/* 149 */     return mf.format(args, new StringBuffer(), null).toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 156 */   private static final Hashtable<String, StringManager> managers = new Hashtable();
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
/*     */   public static final synchronized StringManager getManager(String packageName)
/*     */   {
/* 169 */     StringManager mgr = (StringManager)managers.get(packageName);
/* 170 */     if (mgr == null) {
/* 171 */       mgr = new StringManager(packageName);
/* 172 */       managers.put(packageName, mgr);
/*     */     }
/* 174 */     return mgr;
/*     */   }
/*     */   
/*     */   public static final StringManager getManager(Class<?> clazz)
/*     */   {
/* 179 */     return getManager(clazz.getPackage().getName());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\naming\StringManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */