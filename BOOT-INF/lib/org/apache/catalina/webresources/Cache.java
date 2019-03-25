/*     */ package org.apache.catalina.webresources;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.TreeSet;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.WebResource;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.res.StringManager;
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
/*     */ public class Cache
/*     */ {
/*  33 */   private static final Log log = LogFactory.getLog(Cache.class);
/*  34 */   protected static final StringManager sm = StringManager.getManager(Cache.class);
/*     */   
/*     */   private static final long TARGET_FREE_PERCENT_GET = 5L;
/*     */   
/*     */   private static final long TARGET_FREE_PERCENT_BACKGROUND = 10L;
/*     */   
/*     */   private static final int OBJECT_MAX_SIZE_FACTOR = 20;
/*     */   
/*     */   private final StandardRoot root;
/*  43 */   private final AtomicLong size = new AtomicLong(0L);
/*     */   
/*  45 */   private long ttl = 5000L;
/*  46 */   private long maxSize = 10485760L;
/*  47 */   private int objectMaxSize = (int)this.maxSize / 20;
/*     */   
/*  49 */   private AtomicLong lookupCount = new AtomicLong(0L);
/*  50 */   private AtomicLong hitCount = new AtomicLong(0L);
/*     */   
/*  52 */   private final ConcurrentMap<String, CachedResource> resourceCache = new ConcurrentHashMap();
/*     */   
/*     */   public Cache(StandardRoot root)
/*     */   {
/*  56 */     this.root = root;
/*     */   }
/*     */   
/*     */   protected WebResource getResource(String path, boolean useClassLoaderResources)
/*     */   {
/*  61 */     if (noCache(path)) {
/*  62 */       return this.root.getResourceInternal(path, useClassLoaderResources);
/*     */     }
/*     */     
/*  65 */     this.lookupCount.incrementAndGet();
/*     */     
/*  67 */     CachedResource cacheEntry = (CachedResource)this.resourceCache.get(path);
/*     */     
/*  69 */     if ((cacheEntry != null) && (!cacheEntry.validateResource(useClassLoaderResources))) {
/*  70 */       removeCacheEntry(path);
/*  71 */       cacheEntry = null;
/*     */     }
/*     */     
/*  74 */     if (cacheEntry == null)
/*     */     {
/*  76 */       int objectMaxSizeBytes = getObjectMaxSizeBytes();
/*     */       
/*  78 */       CachedResource newCacheEntry = new CachedResource(this, this.root, path, getTtl(), objectMaxSizeBytes);
/*     */       
/*     */ 
/*     */ 
/*  82 */       cacheEntry = (CachedResource)this.resourceCache.putIfAbsent(path, newCacheEntry);
/*     */       
/*  84 */       if (cacheEntry == null)
/*     */       {
/*  86 */         cacheEntry = newCacheEntry;
/*  87 */         cacheEntry.validateResource(useClassLoaderResources);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*  92 */         long delta = cacheEntry.getSize();
/*  93 */         this.size.addAndGet(delta);
/*     */         
/*  95 */         if (this.size.get() > this.maxSize)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 100 */           long targetSize = this.maxSize * 95L / 100L;
/* 101 */           long newSize = evict(targetSize, this.resourceCache.values().iterator());
/* 102 */           if (newSize > this.maxSize)
/*     */           {
/*     */ 
/* 105 */             removeCacheEntry(path);
/* 106 */             log.warn(sm.getString("cache.addFail", new Object[] { path, this.root.getContext().getName() }));
/*     */           }
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 112 */         cacheEntry.validateResource(useClassLoaderResources);
/*     */       }
/*     */     } else {
/* 115 */       this.hitCount.incrementAndGet();
/*     */     }
/*     */     
/* 118 */     return cacheEntry;
/*     */   }
/*     */   
/*     */   protected WebResource[] getResources(String path, boolean useClassLoaderResources) {
/* 122 */     this.lookupCount.incrementAndGet();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 127 */     CachedResource cacheEntry = (CachedResource)this.resourceCache.get(path);
/*     */     
/* 129 */     if ((cacheEntry != null) && (!cacheEntry.validateResources(useClassLoaderResources))) {
/* 130 */       removeCacheEntry(path);
/* 131 */       cacheEntry = null;
/*     */     }
/*     */     
/* 134 */     if (cacheEntry == null)
/*     */     {
/* 136 */       int objectMaxSizeBytes = getObjectMaxSizeBytes();
/*     */       
/* 138 */       CachedResource newCacheEntry = new CachedResource(this, this.root, path, getTtl(), objectMaxSizeBytes);
/*     */       
/*     */ 
/*     */ 
/* 142 */       cacheEntry = (CachedResource)this.resourceCache.putIfAbsent(path, newCacheEntry);
/*     */       
/* 144 */       if (cacheEntry == null)
/*     */       {
/* 146 */         cacheEntry = newCacheEntry;
/* 147 */         cacheEntry.validateResources(useClassLoaderResources);
/*     */         
/*     */ 
/* 150 */         long delta = cacheEntry.getSize();
/* 151 */         this.size.addAndGet(delta);
/*     */         
/* 153 */         if (this.size.get() > this.maxSize)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 158 */           long targetSize = this.maxSize * 95L / 100L;
/* 159 */           long newSize = evict(targetSize, this.resourceCache.values().iterator());
/* 160 */           if (newSize > this.maxSize)
/*     */           {
/*     */ 
/* 163 */             removeCacheEntry(path);
/* 164 */             log.warn(sm.getString("cache.addFail", new Object[] { path }));
/*     */           }
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 170 */         cacheEntry.validateResources(useClassLoaderResources);
/*     */       }
/*     */     } else {
/* 173 */       this.hitCount.incrementAndGet();
/*     */     }
/*     */     
/* 176 */     return cacheEntry.getWebResources();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void backgroundProcess()
/*     */   {
/* 183 */     TreeSet<CachedResource> orderedResources = new TreeSet(new EvictionOrder(null));
/*     */     
/* 185 */     orderedResources.addAll(this.resourceCache.values());
/*     */     
/* 187 */     Iterator<CachedResource> iter = orderedResources.iterator();
/*     */     
/* 189 */     long targetSize = this.maxSize * 90L / 100L;
/*     */     
/* 191 */     long newSize = evict(targetSize, iter);
/*     */     
/* 193 */     if (newSize > targetSize) {
/* 194 */       log.info(sm.getString("cache.backgroundEvictFail", new Object[] {
/* 195 */         Long.valueOf(10L), this.root
/* 196 */         .getContext().getName(), 
/* 197 */         Long.valueOf(newSize / 1024L) }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean noCache(String path)
/*     */   {
/* 204 */     if (((path.endsWith(".class")) && (
/* 205 */       (path.startsWith("/WEB-INF/classes/")) || (path.startsWith("/WEB-INF/lib/")))) || (
/*     */       
/* 207 */       (path.startsWith("/WEB-INF/lib/")) && (path.endsWith(".jar")))) {
/* 208 */       return true;
/*     */     }
/* 210 */     return false;
/*     */   }
/*     */   
/*     */   private long evict(long targetSize, Iterator<CachedResource> iter)
/*     */   {
/* 215 */     long now = System.currentTimeMillis();
/*     */     
/* 217 */     long newSize = this.size.get();
/*     */     
/* 219 */     while ((newSize > targetSize) && (iter.hasNext())) {
/* 220 */       CachedResource resource = (CachedResource)iter.next();
/*     */       
/*     */ 
/* 223 */       if (resource.getNextCheck() <= now)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 228 */         removeCacheEntry(resource.getWebappPath());
/*     */         
/* 230 */         newSize = this.size.get();
/*     */       }
/*     */     }
/* 233 */     return newSize;
/*     */   }
/*     */   
/*     */ 
/*     */   void removeCacheEntry(String path)
/*     */   {
/* 239 */     CachedResource cachedResource = (CachedResource)this.resourceCache.remove(path);
/* 240 */     if (cachedResource != null) {
/* 241 */       long delta = cachedResource.getSize();
/* 242 */       this.size.addAndGet(-delta);
/*     */     }
/*     */   }
/*     */   
/*     */   public long getTtl() {
/* 247 */     return this.ttl;
/*     */   }
/*     */   
/*     */   public void setTtl(long ttl) {
/* 251 */     this.ttl = ttl;
/*     */   }
/*     */   
/*     */   public long getMaxSize()
/*     */   {
/* 256 */     return this.maxSize / 1024L;
/*     */   }
/*     */   
/*     */   public void setMaxSize(long maxSize)
/*     */   {
/* 261 */     this.maxSize = (maxSize * 1024L);
/*     */   }
/*     */   
/*     */   public long getLookupCount() {
/* 265 */     return this.lookupCount.get();
/*     */   }
/*     */   
/*     */   public long getHitCount() {
/* 269 */     return this.hitCount.get();
/*     */   }
/*     */   
/*     */   public void setObjectMaxSize(int objectMaxSize) {
/* 273 */     if (objectMaxSize * 1024L > 2147483647L) {
/* 274 */       log.warn(sm.getString("cache.objectMaxSizeTooBigBytes", new Object[] { Integer.valueOf(objectMaxSize) }));
/* 275 */       this.objectMaxSize = Integer.MAX_VALUE;
/*     */     }
/*     */     
/* 278 */     this.objectMaxSize = (objectMaxSize * 1024);
/*     */   }
/*     */   
/*     */   public int getObjectMaxSize()
/*     */   {
/* 283 */     return this.objectMaxSize / 1024;
/*     */   }
/*     */   
/*     */   public int getObjectMaxSizeBytes() {
/* 287 */     return this.objectMaxSize;
/*     */   }
/*     */   
/*     */   void enforceObjectMaxSizeLimit() {
/* 291 */     long limit = this.maxSize / 20L;
/* 292 */     if (limit > 2147483647L) {
/* 293 */       return;
/*     */     }
/* 295 */     if (this.objectMaxSize > limit) {
/* 296 */       log.warn(sm.getString("cache.objectMaxSizeTooBig", new Object[] {
/* 297 */         Integer.valueOf(this.objectMaxSize / 1024), Integer.valueOf((int)limit / 1024) }));
/* 298 */       this.objectMaxSize = ((int)limit);
/*     */     }
/*     */   }
/*     */   
/*     */   public void clear() {
/* 303 */     this.resourceCache.clear();
/* 304 */     this.size.set(0L);
/*     */   }
/*     */   
/*     */   public long getSize() {
/* 308 */     return this.size.get() / 1024L;
/*     */   }
/*     */   
/*     */   private static class EvictionOrder implements Comparator<CachedResource>
/*     */   {
/*     */     public int compare(CachedResource cr1, CachedResource cr2)
/*     */     {
/* 315 */       long nc1 = cr1.getNextCheck();
/* 316 */       long nc2 = cr2.getNextCheck();
/*     */       
/*     */ 
/*     */ 
/* 320 */       if (nc1 == nc2)
/* 321 */         return 0;
/* 322 */       if (nc1 > nc2) {
/* 323 */         return -1;
/*     */       }
/* 325 */       return 1;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\webresources\Cache.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */