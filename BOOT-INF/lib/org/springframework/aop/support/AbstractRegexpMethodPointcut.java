/*     */ package org.springframework.aop.support;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractRegexpMethodPointcut
/*     */   extends StaticMethodMatcherPointcut
/*     */   implements Serializable
/*     */ {
/*  57 */   private String[] patterns = new String[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  62 */   private String[] excludedPatterns = new String[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPattern(String pattern)
/*     */   {
/*  71 */     setPatterns(new String[] { pattern });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPatterns(String... patterns)
/*     */   {
/*  80 */     Assert.notEmpty(patterns, "'patterns' must not be empty");
/*  81 */     this.patterns = new String[patterns.length];
/*  82 */     for (int i = 0; i < patterns.length; i++) {
/*  83 */       this.patterns[i] = StringUtils.trimWhitespace(patterns[i]);
/*     */     }
/*  85 */     initPatternRepresentation(this.patterns);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String[] getPatterns()
/*     */   {
/*  92 */     return this.patterns;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExcludedPattern(String excludedPattern)
/*     */   {
/* 101 */     setExcludedPatterns(new String[] { excludedPattern });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExcludedPatterns(String... excludedPatterns)
/*     */   {
/* 110 */     Assert.notEmpty(excludedPatterns, "'excludedPatterns' must not be empty");
/* 111 */     this.excludedPatterns = new String[excludedPatterns.length];
/* 112 */     for (int i = 0; i < excludedPatterns.length; i++) {
/* 113 */       this.excludedPatterns[i] = StringUtils.trimWhitespace(excludedPatterns[i]);
/*     */     }
/* 115 */     initExcludedPatternRepresentation(this.excludedPatterns);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String[] getExcludedPatterns()
/*     */   {
/* 122 */     return this.excludedPatterns;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean matches(Method method, Class<?> targetClass)
/*     */   {
/* 133 */     return ((targetClass != null) && (matchesPattern(ClassUtils.getQualifiedMethodName(method, targetClass)))) || 
/* 134 */       (matchesPattern(ClassUtils.getQualifiedMethodName(method)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean matchesPattern(String signatureString)
/*     */   {
/* 143 */     for (int i = 0; i < this.patterns.length; i++) {
/* 144 */       boolean matched = matches(signatureString, i);
/* 145 */       if (matched) {
/* 146 */         for (int j = 0; j < this.excludedPatterns.length; j++) {
/* 147 */           boolean excluded = matchesExclusion(signatureString, j);
/* 148 */           if (excluded) {
/* 149 */             return false;
/*     */           }
/*     */         }
/* 152 */         return true;
/*     */       }
/*     */     }
/* 155 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void initPatternRepresentation(String[] paramArrayOfString)
/*     */     throws IllegalArgumentException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void initExcludedPatternRepresentation(String[] paramArrayOfString)
/*     */     throws IllegalArgumentException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract boolean matches(String paramString, int paramInt);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract boolean matchesExclusion(String paramString, int paramInt);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 198 */     if (this == other) {
/* 199 */       return true;
/*     */     }
/* 201 */     if (!(other instanceof AbstractRegexpMethodPointcut)) {
/* 202 */       return false;
/*     */     }
/* 204 */     AbstractRegexpMethodPointcut otherPointcut = (AbstractRegexpMethodPointcut)other;
/* 205 */     return (Arrays.equals(this.patterns, otherPointcut.patterns)) && 
/* 206 */       (Arrays.equals(this.excludedPatterns, otherPointcut.excludedPatterns));
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 211 */     int result = 27;
/* 212 */     for (String pattern : this.patterns) {
/* 213 */       result = 13 * result + pattern.hashCode();
/*     */     }
/* 215 */     for (String excludedPattern : this.excludedPatterns) {
/* 216 */       result = 13 * result + excludedPattern.hashCode();
/*     */     }
/* 218 */     return result;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 223 */     return 
/* 224 */       getClass().getName() + ": patterns " + ObjectUtils.nullSafeToString(this.patterns) + ", excluded patterns " + ObjectUtils.nullSafeToString(this.excludedPatterns);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\support\AbstractRegexpMethodPointcut.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */