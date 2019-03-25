/*     */ package org.jboss.logging;
/*     */ 
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
/*     */ class LoggingLocale
/*     */ {
/*  33 */   private static final Locale LOCALE = ;
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
/*     */   static Locale getLocale()
/*     */   {
/*  50 */     return LOCALE;
/*     */   }
/*     */   
/*     */   private static Locale getDefaultLocale() {
/*  54 */     String bcp47Tag = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public String run() {
/*  57 */         return System.getProperty("org.jboss.logging.locale", "");
/*     */       }
/*     */     });
/*  60 */     if (bcp47Tag.trim().isEmpty()) {
/*  61 */       return Locale.getDefault();
/*     */     }
/*     */     
/*     */ 
/*  65 */     return forLanguageTag(bcp47Tag);
/*     */   }
/*     */   
/*     */   private static Locale forLanguageTag(String locale)
/*     */   {
/*  70 */     if ("en-CA".equalsIgnoreCase(locale))
/*  71 */       return Locale.CANADA;
/*  72 */     if ("fr-CA".equalsIgnoreCase(locale))
/*  73 */       return Locale.CANADA_FRENCH;
/*  74 */     if ("zh".equalsIgnoreCase(locale))
/*  75 */       return Locale.CHINESE;
/*  76 */     if ("en".equalsIgnoreCase(locale))
/*  77 */       return Locale.ENGLISH;
/*  78 */     if ("fr-FR".equalsIgnoreCase(locale))
/*  79 */       return Locale.FRANCE;
/*  80 */     if ("fr".equalsIgnoreCase(locale))
/*  81 */       return Locale.FRENCH;
/*  82 */     if ("de".equalsIgnoreCase(locale))
/*  83 */       return Locale.GERMAN;
/*  84 */     if ("de-DE".equalsIgnoreCase(locale))
/*  85 */       return Locale.GERMANY;
/*  86 */     if ("it".equalsIgnoreCase(locale))
/*  87 */       return Locale.ITALIAN;
/*  88 */     if ("it-IT".equalsIgnoreCase(locale))
/*  89 */       return Locale.ITALY;
/*  90 */     if ("ja-JP".equalsIgnoreCase(locale))
/*  91 */       return Locale.JAPAN;
/*  92 */     if ("ja".equalsIgnoreCase(locale))
/*  93 */       return Locale.JAPANESE;
/*  94 */     if ("ko-KR".equalsIgnoreCase(locale))
/*  95 */       return Locale.KOREA;
/*  96 */     if ("ko".equalsIgnoreCase(locale))
/*  97 */       return Locale.KOREAN;
/*  98 */     if ("zh-CN".equalsIgnoreCase(locale))
/*  99 */       return Locale.SIMPLIFIED_CHINESE;
/* 100 */     if ("zh-TW".equalsIgnoreCase(locale))
/* 101 */       return Locale.TRADITIONAL_CHINESE;
/* 102 */     if ("en-UK".equalsIgnoreCase(locale))
/* 103 */       return Locale.UK;
/* 104 */     if ("en-US".equalsIgnoreCase(locale)) {
/* 105 */       return Locale.US;
/*     */     }
/*     */     
/*     */ 
/* 109 */     String[] parts = locale.split("-");
/* 110 */     int len = parts.length;
/* 111 */     int index = 0;
/* 112 */     int count = 0;
/* 113 */     String language = parts[(index++)];
/* 114 */     String region = "";
/* 115 */     String variant = "";
/*     */     
/* 117 */     while ((index < len) && 
/* 118 */       (count++ != 2) && (isAlpha(parts[index], 3, 3)))
/*     */     {
/*     */ 
/* 121 */       index++;
/*     */     }
/*     */     
/* 124 */     if ((index != len) && (isAlpha(parts[index], 4, 4))) {
/* 125 */       index++;
/*     */     }
/*     */     
/* 128 */     if ((index != len) && ((isAlpha(parts[index], 2, 2)) || (isNumeric(parts[index], 3, 3)))) {
/* 129 */       region = parts[(index++)];
/*     */     }
/*     */     
/* 132 */     if ((index != len) && (isAlphaOrNumeric(parts[index], 5, 8))) {
/* 133 */       variant = parts[index];
/*     */     }
/* 135 */     return new Locale(language, region, variant);
/*     */   }
/*     */   
/*     */   private static boolean isAlpha(String value, int minLen, int maxLen) {
/* 139 */     int len = value.length();
/* 140 */     if ((len < minLen) || (len > maxLen)) {
/* 141 */       return false;
/*     */     }
/* 143 */     for (int i = 0; i < len; i++) {
/* 144 */       if (!Character.isLetter(value.charAt(i))) {
/* 145 */         return false;
/*     */       }
/*     */     }
/* 148 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean isNumeric(String value, int minLen, int maxLen) {
/* 152 */     int len = value.length();
/* 153 */     if ((len < minLen) || (len > maxLen)) {
/* 154 */       return false;
/*     */     }
/* 156 */     for (int i = 0; i < len; i++) {
/* 157 */       if (!Character.isDigit(value.charAt(i))) {
/* 158 */         return false;
/*     */       }
/*     */     }
/* 161 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean isAlphaOrNumeric(String value, int minLen, int maxLen)
/*     */   {
/* 166 */     int len = value.length();
/* 167 */     if ((len < minLen) || (len > maxLen)) {
/* 168 */       return false;
/*     */     }
/* 170 */     for (int i = 0; i < len; i++) {
/* 171 */       if (!Character.isLetterOrDigit(value.charAt(i))) {
/* 172 */         return false;
/*     */       }
/*     */     }
/* 175 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jboss-logging-3.3.1.Final.jar!\org\jboss\logging\LoggingLocale.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */