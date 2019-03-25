/*    */ package org.springframework.util;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class PatternMatchUtils
/*    */ {
/*    */   public static boolean simpleMatch(String pattern, String str)
/*    */   {
/* 37 */     if ((pattern == null) || (str == null)) {
/* 38 */       return false;
/*    */     }
/* 40 */     int firstIndex = pattern.indexOf('*');
/* 41 */     if (firstIndex == -1) {
/* 42 */       return pattern.equals(str);
/*    */     }
/* 44 */     if (firstIndex == 0) {
/* 45 */       if (pattern.length() == 1) {
/* 46 */         return true;
/*    */       }
/* 48 */       int nextIndex = pattern.indexOf('*', firstIndex + 1);
/* 49 */       if (nextIndex == -1) {
/* 50 */         return str.endsWith(pattern.substring(1));
/*    */       }
/* 52 */       String part = pattern.substring(1, nextIndex);
/* 53 */       if ("".equals(part)) {
/* 54 */         return simpleMatch(pattern.substring(nextIndex), str);
/*    */       }
/* 56 */       int partIndex = str.indexOf(part);
/* 57 */       while (partIndex != -1) {
/* 58 */         if (simpleMatch(pattern.substring(nextIndex), str.substring(partIndex + part.length()))) {
/* 59 */           return true;
/*    */         }
/* 61 */         partIndex = str.indexOf(part, partIndex + 1);
/*    */       }
/* 63 */       return false;
/*    */     }
/* 65 */     return (str.length() >= firstIndex) && 
/* 66 */       (pattern.substring(0, firstIndex).equals(str.substring(0, firstIndex))) && 
/* 67 */       (simpleMatch(pattern.substring(firstIndex), str.substring(firstIndex)));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static boolean simpleMatch(String[] patterns, String str)
/*    */   {
/* 79 */     if (patterns != null) {
/* 80 */       for (String pattern : patterns) {
/* 81 */         if (simpleMatch(pattern, str)) {
/* 82 */           return true;
/*    */         }
/*    */       }
/*    */     }
/* 86 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\PatternMatchUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */