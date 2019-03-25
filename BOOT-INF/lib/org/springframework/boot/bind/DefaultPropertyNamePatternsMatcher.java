/*     */ package org.springframework.boot.bind;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
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
/*     */ class DefaultPropertyNamePatternsMatcher
/*     */   implements PropertyNamePatternsMatcher
/*     */ {
/*     */   private final char[] delimiters;
/*     */   private final boolean ignoreCase;
/*     */   private final String[] names;
/*     */   
/*     */   protected DefaultPropertyNamePatternsMatcher(char[] delimiters, String... names)
/*     */   {
/*  40 */     this(delimiters, false, names);
/*     */   }
/*     */   
/*     */   protected DefaultPropertyNamePatternsMatcher(char[] delimiters, boolean ignoreCase, String... names)
/*     */   {
/*  45 */     this(delimiters, ignoreCase, new HashSet(Arrays.asList(names)));
/*     */   }
/*     */   
/*     */   DefaultPropertyNamePatternsMatcher(char[] delimiters, boolean ignoreCase, Set<String> names)
/*     */   {
/*  50 */     this.delimiters = delimiters;
/*  51 */     this.ignoreCase = ignoreCase;
/*  52 */     this.names = ((String[])names.toArray(new String[names.size()]));
/*     */   }
/*     */   
/*     */   public boolean matches(String propertyName)
/*     */   {
/*  57 */     char[] propertyNameChars = propertyName.toCharArray();
/*  58 */     boolean[] match = new boolean[this.names.length];
/*  59 */     boolean noneMatched = true;
/*  60 */     for (int i = 0; i < this.names.length; i++) {
/*  61 */       if (this.names[i].length() <= propertyNameChars.length) {
/*  62 */         match[i] = true;
/*  63 */         noneMatched = false;
/*     */       }
/*     */     }
/*  66 */     if (noneMatched) {
/*  67 */       return false;
/*     */     }
/*  69 */     for (int charIndex = 0; charIndex < propertyNameChars.length; charIndex++) {
/*  70 */       for (int nameIndex = 0; nameIndex < this.names.length; nameIndex++) {
/*  71 */         if (match[nameIndex] != 0) {
/*  72 */           match[nameIndex] = false;
/*  73 */           if (charIndex < this.names[nameIndex].length()) {
/*  74 */             if (isCharMatch(this.names[nameIndex].charAt(charIndex), propertyNameChars[charIndex]))
/*     */             {
/*  76 */               match[nameIndex] = true;
/*  77 */               noneMatched = false;
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/*  82 */             char charAfter = propertyNameChars[this.names[nameIndex].length()];
/*  83 */             if (isDelimiter(charAfter)) {
/*  84 */               match[nameIndex] = true;
/*  85 */               noneMatched = false;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*  90 */       if (noneMatched) {
/*  91 */         return false;
/*     */       }
/*     */     }
/*  94 */     for (int i = 0; i < match.length; i++) {
/*  95 */       if (match[i] != 0) {
/*  96 */         return true;
/*     */       }
/*     */     }
/*  99 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isCharMatch(char c1, char c2) {
/* 103 */     if (this.ignoreCase) {
/* 104 */       return Character.toLowerCase(c1) == Character.toLowerCase(c2);
/*     */     }
/* 106 */     return c1 == c2;
/*     */   }
/*     */   
/*     */   private boolean isDelimiter(char c) {
/* 110 */     for (char delimiter : this.delimiters) {
/* 111 */       if (c == delimiter) {
/* 112 */         return true;
/*     */       }
/*     */     }
/* 115 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\bind\DefaultPropertyNamePatternsMatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */