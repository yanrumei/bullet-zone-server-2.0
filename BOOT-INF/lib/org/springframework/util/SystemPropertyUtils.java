/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.PrintStream;
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
/*  46 */   private static final PropertyPlaceholderHelper strictHelper = new PropertyPlaceholderHelper("${", "}", ":", false);
/*     */   
/*     */ 
/*  49 */   private static final PropertyPlaceholderHelper nonStrictHelper = new PropertyPlaceholderHelper("${", "}", ":", true);
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
/*     */   public static String resolvePlaceholders(String text)
/*     */   {
/*  63 */     return resolvePlaceholders(text, false);
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
/*     */   public static String resolvePlaceholders(String text, boolean ignoreUnresolvablePlaceholders)
/*     */   {
/*  79 */     PropertyPlaceholderHelper helper = ignoreUnresolvablePlaceholders ? nonStrictHelper : strictHelper;
/*  80 */     return helper.replacePlaceholders(text, new SystemPropertyPlaceholderResolver(text));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class SystemPropertyPlaceholderResolver
/*     */     implements PropertyPlaceholderHelper.PlaceholderResolver
/*     */   {
/*     */     private final String text;
/*     */     
/*     */ 
/*     */     public SystemPropertyPlaceholderResolver(String text)
/*     */     {
/*  93 */       this.text = text;
/*     */     }
/*     */     
/*     */     public String resolvePlaceholder(String placeholderName)
/*     */     {
/*     */       try {
/*  99 */         String propVal = System.getProperty(placeholderName);
/* 100 */         if (propVal == null) {}
/*     */         
/* 102 */         return System.getenv(placeholderName);
/*     */ 
/*     */       }
/*     */       catch (Throwable ex)
/*     */       {
/* 107 */         System.err.println("Could not resolve placeholder '" + placeholderName + "' in [" + this.text + "] as system property: " + ex);
/*     */       }
/* 109 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\SystemPropertyUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */