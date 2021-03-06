/*     */ package org.springframework.cache.interceptor;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.PatternMatchUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NameMatchCacheOperationSource
/*     */   implements CacheOperationSource, Serializable
/*     */ {
/*  45 */   protected static final Log logger = LogFactory.getLog(NameMatchCacheOperationSource.class);
/*     */   
/*     */ 
/*     */ 
/*  49 */   private Map<String, Collection<CacheOperation>> nameMap = new LinkedHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setNameMap(Map<String, Collection<CacheOperation>> nameMap)
/*     */   {
/*  59 */     for (Map.Entry<String, Collection<CacheOperation>> entry : nameMap.entrySet()) {
/*  60 */       addCacheMethod((String)entry.getKey(), (Collection)entry.getValue());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addCacheMethod(String methodName, Collection<CacheOperation> ops)
/*     */   {
/*  72 */     if (logger.isDebugEnabled()) {
/*  73 */       logger.debug("Adding method [" + methodName + "] with cache operations [" + ops + "]");
/*     */     }
/*  75 */     this.nameMap.put(methodName, ops);
/*     */   }
/*     */   
/*     */ 
/*     */   public Collection<CacheOperation> getCacheOperations(Method method, Class<?> targetClass)
/*     */   {
/*  81 */     String methodName = method.getName();
/*  82 */     Collection<CacheOperation> ops = (Collection)this.nameMap.get(methodName);
/*     */     String bestNameMatch;
/*  84 */     if (ops == null)
/*     */     {
/*  86 */       bestNameMatch = null;
/*  87 */       for (String mappedName : this.nameMap.keySet()) {
/*  88 */         if ((isMatch(methodName, mappedName)) && ((bestNameMatch == null) || 
/*  89 */           (bestNameMatch.length() <= mappedName.length()))) {
/*  90 */           ops = (Collection)this.nameMap.get(mappedName);
/*  91 */           bestNameMatch = mappedName;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  96 */     return ops;
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
/*     */   protected boolean isMatch(String methodName, String mappedName)
/*     */   {
/* 109 */     return PatternMatchUtils.simpleMatch(mappedName, methodName);
/*     */   }
/*     */   
/*     */   public boolean equals(Object other)
/*     */   {
/* 114 */     if (this == other) {
/* 115 */       return true;
/*     */     }
/* 117 */     if (!(other instanceof NameMatchCacheOperationSource)) {
/* 118 */       return false;
/*     */     }
/* 120 */     NameMatchCacheOperationSource otherTas = (NameMatchCacheOperationSource)other;
/* 121 */     return ObjectUtils.nullSafeEquals(this.nameMap, otherTas.nameMap);
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 126 */     return NameMatchCacheOperationSource.class.hashCode();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 131 */     return getClass().getName() + ": " + this.nameMap;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\interceptor\NameMatchCacheOperationSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */