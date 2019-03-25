/*     */ package org.apache.tomcat.util.file;
/*     */ 
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
/*     */ public final class Matcher
/*     */ {
/*     */   public static boolean matchName(Set<String> patternSet, String fileName)
/*     */   {
/*  48 */     char[] fileNameArray = fileName.toCharArray();
/*  49 */     for (String pattern : patternSet) {
/*  50 */       if (match(pattern, fileNameArray, true)) {
/*  51 */         return true;
/*     */       }
/*     */     }
/*  54 */     return false;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean match(String pattern, String str, boolean caseSensitive)
/*     */   {
/*  78 */     return match(pattern, str.toCharArray(), caseSensitive);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean match(String pattern, char[] strArr, boolean caseSensitive)
/*     */   {
/* 101 */     char[] patArr = pattern.toCharArray();
/* 102 */     int patIdxStart = 0;
/* 103 */     int patIdxEnd = patArr.length - 1;
/* 104 */     int strIdxStart = 0;
/* 105 */     int strIdxEnd = strArr.length - 1;
/*     */     
/*     */ 
/* 108 */     boolean containsStar = false;
/* 109 */     for (int i = 0; i < patArr.length; i++) {
/* 110 */       if (patArr[i] == '*') {
/* 111 */         containsStar = true;
/* 112 */         break;
/*     */       }
/*     */     }
/*     */     
/* 116 */     if (!containsStar)
/*     */     {
/* 118 */       if (patIdxEnd != strIdxEnd) {
/* 119 */         return false;
/*     */       }
/* 121 */       for (int i = 0; i <= patIdxEnd; i++) {
/* 122 */         char ch = patArr[i];
/* 123 */         if ((ch != '?') && 
/* 124 */           (different(caseSensitive, ch, strArr[i]))) {
/* 125 */           return false;
/*     */         }
/*     */       }
/*     */       
/* 129 */       return true;
/*     */     }
/*     */     
/* 132 */     if (patIdxEnd == 0) {
/* 133 */       return true;
/*     */     }
/*     */     char ch;
/*     */     for (;;)
/*     */     {
/* 138 */       ch = patArr[patIdxStart];
/* 139 */       if ((ch == '*') || (strIdxStart > strIdxEnd)) {
/*     */         break;
/*     */       }
/* 142 */       if ((ch != '?') && 
/* 143 */         (different(caseSensitive, ch, strArr[strIdxStart]))) {
/* 144 */         return false;
/*     */       }
/*     */       
/* 147 */       patIdxStart++;
/* 148 */       strIdxStart++;
/*     */     }
/* 150 */     if (strIdxStart > strIdxEnd)
/*     */     {
/*     */ 
/* 153 */       return allStars(patArr, patIdxStart, patIdxEnd);
/*     */     }
/*     */     
/*     */     for (;;)
/*     */     {
/* 158 */       ch = patArr[patIdxEnd];
/* 159 */       if ((ch == '*') || (strIdxStart > strIdxEnd)) {
/*     */         break;
/*     */       }
/* 162 */       if ((ch != '?') && 
/* 163 */         (different(caseSensitive, ch, strArr[strIdxEnd]))) {
/* 164 */         return false;
/*     */       }
/*     */       
/* 167 */       patIdxEnd--;
/* 168 */       strIdxEnd--;
/*     */     }
/* 170 */     if (strIdxStart > strIdxEnd)
/*     */     {
/*     */ 
/* 173 */       return allStars(patArr, patIdxStart, patIdxEnd);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 178 */     while ((patIdxStart != patIdxEnd) && (strIdxStart <= strIdxEnd)) {
/* 179 */       int patIdxTmp = -1;
/* 180 */       for (int i = patIdxStart + 1; i <= patIdxEnd; i++) {
/* 181 */         if (patArr[i] == '*') {
/* 182 */           patIdxTmp = i;
/* 183 */           break;
/*     */         }
/*     */       }
/* 186 */       if (patIdxTmp == patIdxStart + 1)
/*     */       {
/* 188 */         patIdxStart++;
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 193 */         int patLength = patIdxTmp - patIdxStart - 1;
/* 194 */         int strLength = strIdxEnd - strIdxStart + 1;
/* 195 */         int foundIdx = -1;
/*     */         label431:
/* 197 */         for (int i = 0; i <= strLength - patLength; i++) {
/* 198 */           for (int j = 0; j < patLength; j++) {
/* 199 */             ch = patArr[(patIdxStart + j + 1)];
/* 200 */             if ((ch != '?') && 
/* 201 */               (different(caseSensitive, ch, strArr[(strIdxStart + i + j)]))) {
/*     */               break label431;
/*     */             }
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 208 */           foundIdx = strIdxStart + i;
/* 209 */           break;
/*     */         }
/*     */         
/* 212 */         if (foundIdx == -1) {
/* 213 */           return false;
/*     */         }
/*     */         
/* 216 */         patIdxStart = patIdxTmp;
/* 217 */         strIdxStart = foundIdx + patLength;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 222 */     return allStars(patArr, patIdxStart, patIdxEnd);
/*     */   }
/*     */   
/*     */   private static boolean allStars(char[] chars, int start, int end) {
/* 226 */     for (int i = start; i <= end; i++) {
/* 227 */       if (chars[i] != '*') {
/* 228 */         return false;
/*     */       }
/*     */     }
/* 231 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean different(boolean caseSensitive, char ch, char other)
/*     */   {
/* 236 */     return ch != other;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\file\Matcher.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */