/*     */ package org.springframework.boot.autoconfigure.condition;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.Documented;
/*     */ import java.lang.annotation.Retention;
/*     */ import java.lang.annotation.RetentionPolicy;
/*     */ import java.lang.annotation.Target;
/*     */ import org.springframework.context.annotation.Conditional;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Target({java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.METHOD})
/*     */ @Retention(RetentionPolicy.RUNTIME)
/*     */ @Documented
/*     */ @Conditional({OnJavaCondition.class})
/*     */ public @interface ConditionalOnJava
/*     */ {
/*     */   Range range() default Range.EQUAL_OR_NEWER;
/*     */   
/*     */   JavaVersion value();
/*     */   
/*     */   public static enum Range
/*     */   {
/*  67 */     EQUAL_OR_NEWER, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  72 */     OLDER_THAN;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private Range() {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static enum JavaVersion
/*     */   {
/*  84 */     NINE(9, "1.9", "java.security.cert.URICertStoreParameters"), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  89 */     EIGHT(8, "1.8", "java.util.function.Function"), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  94 */     SEVEN(7, "1.7", "java.nio.file.Files"), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  99 */     SIX(6, "1.6", "java.util.ServiceLoader");
/*     */     
/*     */ 
/*     */     private final int value;
/*     */     private final String name;
/*     */     private final boolean available;
/*     */     
/*     */     private JavaVersion(int value, String name, String className)
/*     */     {
/* 108 */       this.value = value;
/* 109 */       this.name = name;
/* 110 */       this.available = ClassUtils.isPresent(className, getClass().getClassLoader());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isWithin(ConditionalOnJava.Range range, JavaVersion version)
/*     */     {
/* 120 */       Assert.notNull(range, "Range must not be null");
/* 121 */       Assert.notNull(version, "Version must not be null");
/* 122 */       switch (ConditionalOnJava.1.$SwitchMap$org$springframework$boot$autoconfigure$condition$ConditionalOnJava$Range[range.ordinal()]) {
/*     */       case 1: 
/* 124 */         return this.value >= version.value;
/*     */       case 2: 
/* 126 */         return this.value < version.value;
/*     */       }
/* 128 */       throw new IllegalStateException("Unknown range " + range);
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 133 */       return this.name;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public static JavaVersion getJavaVersion()
/*     */     {
/* 141 */       for (JavaVersion candidate : ) {
/* 142 */         if (candidate.available) {
/* 143 */           return candidate;
/*     */         }
/*     */       }
/* 146 */       return SIX;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\condition\ConditionalOnJava.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */