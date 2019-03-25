/*     */ package org.hibernate.validator.internal.util;
/*     */ 
/*     */ import java.util.Arrays;
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
/*     */ public class StringHelper
/*     */ {
/*     */   public static String join(Object[] array, String separator)
/*     */   {
/*  32 */     return array != null ? join(Arrays.asList(array), separator) : null;
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
/*     */   public static String join(Iterable<?> iterable, String separator)
/*     */   {
/*  45 */     if (iterable == null) {
/*  46 */       return null;
/*     */     }
/*     */     
/*  49 */     StringBuilder sb = new StringBuilder();
/*  50 */     boolean isFirst = true;
/*     */     
/*  52 */     for (Object object : iterable) {
/*  53 */       if (!isFirst) {
/*  54 */         sb.append(separator);
/*     */       }
/*     */       else {
/*  57 */         isFirst = false;
/*     */       }
/*     */       
/*  60 */       sb.append(object);
/*     */     }
/*     */     
/*  63 */     return sb.toString();
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
/*     */   public static String decapitalize(String string)
/*     */   {
/*  81 */     if ((string == null) || (string.isEmpty()) || (startsWithSeveralUpperCaseLetters(string))) {
/*  82 */       return string;
/*     */     }
/*     */     
/*  85 */     return string.substring(0, 1).toLowerCase(Locale.ROOT) + string.substring(1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isNullOrEmptyString(String value)
/*     */   {
/*  96 */     return (value == null) || (value.trim().isEmpty());
/*     */   }
/*     */   
/*     */   private static boolean startsWithSeveralUpperCaseLetters(String string) {
/* 100 */     return (string.length() > 1) && 
/* 101 */       (Character.isUpperCase(string.charAt(0))) && 
/* 102 */       (Character.isUpperCase(string.charAt(1)));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\StringHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */