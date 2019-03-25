/*     */ package org.springframework.web.servlet;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ public final class FlashMap
/*     */   extends HashMap<String, Object>
/*     */   implements Comparable<FlashMap>
/*     */ {
/*     */   private String targetRequestPath;
/*  53 */   private final MultiValueMap<String, String> targetRequestParams = new LinkedMultiValueMap(4);
/*     */   
/*  55 */   private long expirationTime = -1L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTargetRequestPath(String path)
/*     */   {
/*  64 */     this.targetRequestPath = path;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getTargetRequestPath()
/*     */   {
/*  71 */     return this.targetRequestPath;
/*     */   }
/*     */   
/*     */ 
/*     */   public FlashMap addTargetRequestParams(MultiValueMap<String, String> params)
/*     */   {
/*     */     Iterator localIterator1;
/*     */     
/*  79 */     if (params != null) {
/*  80 */       for (localIterator1 = params.keySet().iterator(); localIterator1.hasNext();) { key = (String)localIterator1.next();
/*  81 */         for (String value : (List)params.get(key))
/*  82 */           addTargetRequestParam(key, value);
/*     */       }
/*     */     }
/*     */     String key;
/*  86 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FlashMap addTargetRequestParam(String name, String value)
/*     */   {
/*  95 */     if ((StringUtils.hasText(name)) && (StringUtils.hasText(value))) {
/*  96 */       this.targetRequestParams.add(name, value);
/*     */     }
/*  98 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public MultiValueMap<String, String> getTargetRequestParams()
/*     */   {
/* 105 */     return this.targetRequestParams;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void startExpirationPeriod(int timeToLive)
/*     */   {
/* 113 */     this.expirationTime = (System.currentTimeMillis() + timeToLive * 1000);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExpirationTime(long expirationTime)
/*     */   {
/* 122 */     this.expirationTime = expirationTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getExpirationTime()
/*     */   {
/* 131 */     return this.expirationTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isExpired()
/*     */   {
/* 139 */     return (this.expirationTime != -1L) && (System.currentTimeMillis() > this.expirationTime);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int compareTo(FlashMap other)
/*     */   {
/* 150 */     int thisUrlPath = this.targetRequestPath != null ? 1 : 0;
/* 151 */     int otherUrlPath = other.targetRequestPath != null ? 1 : 0;
/* 152 */     if (thisUrlPath != otherUrlPath) {
/* 153 */       return otherUrlPath - thisUrlPath;
/*     */     }
/*     */     
/* 156 */     return other.targetRequestParams.size() - this.targetRequestParams.size();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 162 */     if (this == other) {
/* 163 */       return true;
/*     */     }
/* 165 */     if (!(other instanceof FlashMap)) {
/* 166 */       return false;
/*     */     }
/* 168 */     FlashMap otherFlashMap = (FlashMap)other;
/* 169 */     return (super.equals(otherFlashMap)) && 
/* 170 */       (ObjectUtils.nullSafeEquals(this.targetRequestPath, otherFlashMap.targetRequestPath)) && 
/* 171 */       (this.targetRequestParams.equals(otherFlashMap.targetRequestParams));
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 176 */     int result = super.hashCode();
/* 177 */     result = 31 * result + ObjectUtils.nullSafeHashCode(this.targetRequestPath);
/* 178 */     result = 31 * result + this.targetRequestParams.hashCode();
/* 179 */     return result;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 184 */     return "FlashMap [attributes=" + super.toString() + ", targetRequestPath=" + this.targetRequestPath + ", targetRequestParams=" + this.targetRequestParams + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\FlashMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */