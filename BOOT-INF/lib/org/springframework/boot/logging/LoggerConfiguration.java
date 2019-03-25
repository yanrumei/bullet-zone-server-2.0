/*     */ package org.springframework.boot.logging;
/*     */ 
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class LoggerConfiguration
/*     */ {
/*     */   private final String name;
/*     */   private final LogLevel configuredLevel;
/*     */   private final LogLevel effectiveLevel;
/*     */   
/*     */   public LoggerConfiguration(String name, LogLevel configuredLevel, LogLevel effectiveLevel)
/*     */   {
/*  44 */     Assert.notNull(name, "Name must not be null");
/*  45 */     Assert.notNull(effectiveLevel, "EffectiveLevel must not be null");
/*  46 */     this.name = name;
/*  47 */     this.configuredLevel = configuredLevel;
/*  48 */     this.effectiveLevel = effectiveLevel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public LogLevel getConfiguredLevel()
/*     */   {
/*  56 */     return this.configuredLevel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public LogLevel getEffectiveLevel()
/*     */   {
/*  64 */     return this.effectiveLevel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/*  72 */     return this.name;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  77 */     return "LoggerConfiguration [name=" + this.name + ", configuredLevel=" + this.configuredLevel + ", effectiveLevel=" + this.effectiveLevel + "]";
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  83 */     int prime = 31;
/*  84 */     int result = 1;
/*  85 */     result = 31 * result + ObjectUtils.nullSafeHashCode(this.name);
/*  86 */     result = 31 * result + ObjectUtils.nullSafeHashCode(this.configuredLevel);
/*  87 */     result = 31 * result + ObjectUtils.nullSafeHashCode(this.effectiveLevel);
/*  88 */     return result;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/*  93 */     if (this == obj) {
/*  94 */       return true;
/*     */     }
/*  96 */     if (obj == null) {
/*  97 */       return false;
/*     */     }
/*  99 */     if ((obj instanceof LoggerConfiguration)) {
/* 100 */       LoggerConfiguration other = (LoggerConfiguration)obj;
/* 101 */       boolean rtn = true;
/* 102 */       rtn = (rtn) && (ObjectUtils.nullSafeEquals(this.name, other.name));
/* 103 */       rtn = (rtn) && (ObjectUtils.nullSafeEquals(this.configuredLevel, other.configuredLevel));
/*     */       
/* 105 */       rtn = (rtn) && (ObjectUtils.nullSafeEquals(this.effectiveLevel, other.effectiveLevel));
/*     */       
/* 107 */       return rtn;
/*     */     }
/* 109 */     return super.equals(obj);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\logging\LoggerConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */