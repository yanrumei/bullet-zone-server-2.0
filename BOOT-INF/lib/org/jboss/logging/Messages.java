/*     */ package org.jboss.logging;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Locale;
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
/*     */ public final class Messages
/*     */ {
/*     */   public static <T> T getBundle(Class<T> type)
/*     */   {
/*  46 */     return (T)getBundle(type, LoggingLocale.getLocale());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> T getBundle(final Class<T> type, Locale locale)
/*     */   {
/*  58 */     (T)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public T run() {
/*  60 */         String language = this.val$locale.getLanguage();
/*  61 */         String country = this.val$locale.getCountry();
/*  62 */         String variant = this.val$locale.getVariant();
/*     */         
/*  64 */         Class<? extends T> bundleClass = null;
/*  65 */         if ((variant != null) && (variant.length() > 0)) {
/*  66 */           try { bundleClass = Class.forName(Messages.join(type.getName(), "$bundle", language, country, variant), true, type.getClassLoader()).asSubclass(type);
/*     */           }
/*     */           catch (ClassNotFoundException localClassNotFoundException1) {}
/*     */         }
/*  70 */         if ((bundleClass == null) && (country != null) && (country.length() > 0)) {
/*  71 */           try { bundleClass = Class.forName(Messages.join(type.getName(), "$bundle", language, country, null), true, type.getClassLoader()).asSubclass(type);
/*     */           }
/*     */           catch (ClassNotFoundException localClassNotFoundException2) {}
/*     */         }
/*  75 */         if ((bundleClass == null) && (language != null) && (language.length() > 0)) {
/*  76 */           try { bundleClass = Class.forName(Messages.join(type.getName(), "$bundle", language, null, null), true, type.getClassLoader()).asSubclass(type);
/*     */           }
/*     */           catch (ClassNotFoundException localClassNotFoundException3) {}
/*     */         }
/*  80 */         if (bundleClass == null) {
/*  81 */           try { bundleClass = Class.forName(Messages.join(type.getName(), "$bundle", null, null, null), true, type.getClassLoader()).asSubclass(type);
/*     */           } catch (ClassNotFoundException e) {
/*  83 */             throw new IllegalArgumentException("Invalid bundle " + type + " (implementation not found)");
/*     */           }
/*     */         }
/*     */         try {
/*  87 */           field = bundleClass.getField("INSTANCE");
/*     */         } catch (NoSuchFieldException e) { Field field;
/*  89 */           throw new IllegalArgumentException("Bundle implementation " + bundleClass + " has no instance field");
/*     */         }
/*     */         try { Field field;
/*  92 */           return (T)type.cast(field.get(null));
/*     */         } catch (IllegalAccessException e) {
/*  94 */           throw new IllegalArgumentException("Bundle implementation " + bundleClass + " could not be instantiated", e);
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   private static String join(String interfaceName, String a, String b, String c, String d) {
/* 101 */     StringBuilder build = new StringBuilder();
/* 102 */     build.append(interfaceName).append('_').append(a);
/* 103 */     if ((b != null) && (b.length() > 0)) {
/* 104 */       build.append('_');
/* 105 */       build.append(b);
/*     */     }
/* 107 */     if ((c != null) && (c.length() > 0)) {
/* 108 */       build.append('_');
/* 109 */       build.append(c);
/*     */     }
/* 111 */     if ((d != null) && (d.length() > 0)) {
/* 112 */       build.append('_');
/* 113 */       build.append(d);
/*     */     }
/* 115 */     return build.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jboss-logging-3.3.1.Final.jar!\org\jboss\logging\Messages.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */