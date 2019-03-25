/*     */ package org.springframework.boot.loader.util;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.HashSet;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
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
/*     */ public abstract class SystemPropertyUtils
/*     */ {
/*     */   public static final String PLACEHOLDER_PREFIX = "${";
/*     */   public static final String PLACEHOLDER_SUFFIX = "}";
/*     */   public static final String VALUE_SEPARATOR = ":";
/*  54 */   private static final String SIMPLE_PREFIX = "${".substring(1);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String resolvePlaceholders(String text)
/*     */   {
/*  66 */     if (text == null) {
/*  67 */       return text;
/*     */     }
/*  69 */     return parseStringValue(null, text, text, new HashSet());
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
/*     */   public static String resolvePlaceholders(Properties properties, String text)
/*     */   {
/*  83 */     if (text == null) {
/*  84 */       return text;
/*     */     }
/*  86 */     return parseStringValue(properties, text, text, new HashSet());
/*     */   }
/*     */   
/*     */ 
/*     */   private static String parseStringValue(Properties properties, String value, String current, Set<String> visitedPlaceholders)
/*     */   {
/*  92 */     StringBuilder buf = new StringBuilder(current);
/*     */     
/*  94 */     int startIndex = current.indexOf("${");
/*  95 */     while (startIndex != -1) {
/*  96 */       int endIndex = findPlaceholderEndIndex(buf, startIndex);
/*  97 */       if (endIndex != -1)
/*     */       {
/*  99 */         String placeholder = buf.substring(startIndex + "${".length(), endIndex);
/* 100 */         String originalPlaceholder = placeholder;
/* 101 */         if (!visitedPlaceholders.add(originalPlaceholder)) {
/* 102 */           throw new IllegalArgumentException("Circular placeholder reference '" + originalPlaceholder + "' in property definitions");
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 108 */         placeholder = parseStringValue(properties, value, placeholder, visitedPlaceholders);
/*     */         
/*     */ 
/* 111 */         String propVal = resolvePlaceholder(properties, value, placeholder);
/* 112 */         if ((propVal == null) && (":" != null)) {
/* 113 */           int separatorIndex = placeholder.indexOf(":");
/* 114 */           if (separatorIndex != -1) {
/* 115 */             String actualPlaceholder = placeholder.substring(0, separatorIndex);
/*     */             
/*     */ 
/* 118 */             String defaultValue = placeholder.substring(separatorIndex + ":".length());
/* 119 */             propVal = resolvePlaceholder(properties, value, actualPlaceholder);
/*     */             
/* 121 */             if (propVal == null) {
/* 122 */               propVal = defaultValue;
/*     */             }
/*     */           }
/*     */         }
/* 126 */         if (propVal != null)
/*     */         {
/*     */ 
/* 129 */           propVal = parseStringValue(properties, value, propVal, visitedPlaceholders);
/*     */           
/* 131 */           buf.replace(startIndex, endIndex + "}".length(), propVal);
/*     */           
/* 133 */           startIndex = buf.indexOf("${", startIndex + propVal
/* 134 */             .length());
/*     */         }
/*     */         else
/*     */         {
/* 138 */           startIndex = buf.indexOf("${", endIndex + "}"
/* 139 */             .length());
/*     */         }
/* 141 */         visitedPlaceholders.remove(originalPlaceholder);
/*     */       }
/*     */       else {
/* 144 */         startIndex = -1;
/*     */       }
/*     */     }
/*     */     
/* 148 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private static String resolvePlaceholder(Properties properties, String text, String placeholderName)
/*     */   {
/* 153 */     String propVal = getProperty(placeholderName, null, text);
/* 154 */     if (propVal != null) {
/* 155 */       return propVal;
/*     */     }
/* 157 */     return properties == null ? null : properties.getProperty(placeholderName);
/*     */   }
/*     */   
/*     */   public static String getProperty(String key) {
/* 161 */     return getProperty(key, null, "");
/*     */   }
/*     */   
/*     */   public static String getProperty(String key, String defaultValue) {
/* 165 */     return getProperty(key, defaultValue, "");
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
/*     */   public static String getProperty(String key, String defaultValue, String text)
/*     */   {
/*     */     try
/*     */     {
/* 180 */       String propVal = System.getProperty(key);
/* 181 */       if (propVal == null)
/*     */       {
/* 183 */         propVal = System.getenv(key);
/*     */       }
/* 185 */       if (propVal == null)
/*     */       {
/* 187 */         propVal = System.getenv(key.replace('.', '_'));
/*     */       }
/* 189 */       if (propVal == null)
/*     */       {
/* 191 */         propVal = System.getenv(key.toUpperCase().replace('.', '_'));
/*     */       }
/* 193 */       if (propVal != null) {
/* 194 */         return propVal;
/*     */       }
/*     */     }
/*     */     catch (Throwable ex) {
/* 198 */       System.err.println("Could not resolve key '" + key + "' in '" + text + "' as system property or in environment: " + ex);
/*     */     }
/*     */     
/* 201 */     return defaultValue;
/*     */   }
/*     */   
/*     */   private static int findPlaceholderEndIndex(CharSequence buf, int startIndex) {
/* 205 */     int index = startIndex + "${".length();
/* 206 */     int withinNestedPlaceholder = 0;
/* 207 */     while (index < buf.length()) {
/* 208 */       if (substringMatch(buf, index, "}")) {
/* 209 */         if (withinNestedPlaceholder > 0) {
/* 210 */           withinNestedPlaceholder--;
/* 211 */           index += "}".length();
/*     */         }
/*     */         else {
/* 214 */           return index;
/*     */         }
/*     */       }
/* 217 */       else if (substringMatch(buf, index, SIMPLE_PREFIX)) {
/* 218 */         withinNestedPlaceholder++;
/* 219 */         index += SIMPLE_PREFIX.length();
/*     */       }
/*     */       else {
/* 222 */         index++;
/*     */       }
/*     */     }
/* 225 */     return -1;
/*     */   }
/*     */   
/*     */   private static boolean substringMatch(CharSequence str, int index, CharSequence substring)
/*     */   {
/* 230 */     for (int j = 0; j < substring.length(); j++) {
/* 231 */       int i = index + j;
/* 232 */       if ((i >= str.length()) || (str.charAt(i) != substring.charAt(j))) {
/* 233 */         return false;
/*     */       }
/*     */     }
/* 236 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\org\springframework\boot\loade\\util\SystemPropertyUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */