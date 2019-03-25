/*     */ package org.springframework.cache.annotation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.springframework.cache.interceptor.AbstractFallbackCacheOperationSource;
/*     */ import org.springframework.cache.interceptor.CacheOperation;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotationCacheOperationSource
/*     */   extends AbstractFallbackCacheOperationSource
/*     */   implements Serializable
/*     */ {
/*     */   private final boolean publicMethodsOnly;
/*     */   private final Set<CacheAnnotationParser> annotationParsers;
/*     */   
/*     */   public AnnotationCacheOperationSource()
/*     */   {
/*  58 */     this(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotationCacheOperationSource(boolean publicMethodsOnly)
/*     */   {
/*  69 */     this.publicMethodsOnly = publicMethodsOnly;
/*  70 */     this.annotationParsers = new LinkedHashSet(1);
/*  71 */     this.annotationParsers.add(new SpringCacheAnnotationParser());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotationCacheOperationSource(CacheAnnotationParser annotationParser)
/*     */   {
/*  79 */     this.publicMethodsOnly = true;
/*  80 */     Assert.notNull(annotationParser, "CacheAnnotationParser must not be null");
/*  81 */     this.annotationParsers = Collections.singleton(annotationParser);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotationCacheOperationSource(CacheAnnotationParser... annotationParsers)
/*     */   {
/*  89 */     this.publicMethodsOnly = true;
/*  90 */     Assert.notEmpty(annotationParsers, "At least one CacheAnnotationParser needs to be specified");
/*  91 */     Set<CacheAnnotationParser> parsers = new LinkedHashSet(annotationParsers.length);
/*  92 */     Collections.addAll(parsers, annotationParsers);
/*  93 */     this.annotationParsers = parsers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotationCacheOperationSource(Set<CacheAnnotationParser> annotationParsers)
/*     */   {
/* 101 */     this.publicMethodsOnly = true;
/* 102 */     Assert.notEmpty(annotationParsers, "At least one CacheAnnotationParser needs to be specified");
/* 103 */     this.annotationParsers = annotationParsers;
/*     */   }
/*     */   
/*     */ 
/*     */   protected Collection<CacheOperation> findCacheOperations(final Class<?> clazz)
/*     */   {
/* 109 */     determineCacheOperations(new CacheOperationProvider()
/*     */     {
/*     */       public Collection<CacheOperation> getCacheOperations(CacheAnnotationParser parser) {
/* 112 */         return parser.parseCacheAnnotations(clazz);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */   protected Collection<CacheOperation> findCacheOperations(final Method method)
/*     */   {
/* 120 */     determineCacheOperations(new CacheOperationProvider()
/*     */     {
/*     */       public Collection<CacheOperation> getCacheOperations(CacheAnnotationParser parser) {
/* 123 */         return parser.parseCacheAnnotations(method);
/*     */       }
/*     */     });
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
/*     */   protected Collection<CacheOperation> determineCacheOperations(CacheOperationProvider provider)
/*     */   {
/* 139 */     Collection<CacheOperation> ops = null;
/* 140 */     for (CacheAnnotationParser annotationParser : this.annotationParsers) {
/* 141 */       Collection<CacheOperation> annOps = provider.getCacheOperations(annotationParser);
/* 142 */       if (annOps != null) {
/* 143 */         if (ops == null) {
/* 144 */           ops = new ArrayList();
/*     */         }
/* 146 */         ops.addAll(annOps);
/*     */       }
/*     */     }
/* 149 */     return ops;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean allowPublicMethodsOnly()
/*     */   {
/* 157 */     return this.publicMethodsOnly;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 163 */     if (this == other) {
/* 164 */       return true;
/*     */     }
/* 166 */     if (!(other instanceof AnnotationCacheOperationSource)) {
/* 167 */       return false;
/*     */     }
/* 169 */     AnnotationCacheOperationSource otherCos = (AnnotationCacheOperationSource)other;
/* 170 */     return (this.annotationParsers.equals(otherCos.annotationParsers)) && (this.publicMethodsOnly == otherCos.publicMethodsOnly);
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 176 */     return this.annotationParsers.hashCode();
/*     */   }
/*     */   
/*     */   protected static abstract interface CacheOperationProvider
/*     */   {
/*     */     public abstract Collection<CacheOperation> getCacheOperations(CacheAnnotationParser paramCacheAnnotationParser);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\annotation\AnnotationCacheOperationSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */