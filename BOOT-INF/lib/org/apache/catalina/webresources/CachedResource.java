/*     */ package org.apache.catalina.webresources;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.security.cert.Certificate;
/*     */ import java.util.jar.Manifest;
/*     */ import org.apache.catalina.WebResource;
/*     */ import org.apache.catalina.WebResourceRoot;
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
/*     */ public class CachedResource
/*     */   implements WebResource
/*     */ {
/*     */   private static final long CACHE_ENTRY_SIZE = 500L;
/*     */   private final Cache cache;
/*     */   private final StandardRoot root;
/*     */   private final String webAppPath;
/*     */   private final long ttl;
/*     */   private final int objectMaxSizeBytes;
/*     */   private volatile WebResource webResource;
/*     */   private volatile WebResource[] webResources;
/*     */   private volatile long nextCheck;
/*  49 */   private volatile Long cachedLastModified = null;
/*  50 */   private volatile String cachedLastModifiedHttp = null;
/*  51 */   private volatile byte[] cachedContent = null;
/*  52 */   private volatile Boolean cachedIsFile = null;
/*  53 */   private volatile Boolean cachedIsDirectory = null;
/*  54 */   private volatile Boolean cachedExists = null;
/*  55 */   private volatile Boolean cachedIsVirtual = null;
/*  56 */   private volatile Long cachedContentLength = null;
/*     */   
/*     */ 
/*     */   public CachedResource(Cache cache, StandardRoot root, String path, long ttl, int objectMaxSizeBytes)
/*     */   {
/*  61 */     this.cache = cache;
/*  62 */     this.root = root;
/*  63 */     this.webAppPath = path;
/*  64 */     this.ttl = ttl;
/*  65 */     this.objectMaxSizeBytes = objectMaxSizeBytes;
/*     */   }
/*     */   
/*     */   protected boolean validateResource(boolean useClassLoaderResources) {
/*  69 */     long now = System.currentTimeMillis();
/*     */     
/*  71 */     if (this.webResource == null) {
/*  72 */       synchronized (this) {
/*  73 */         if (this.webResource == null) {
/*  74 */           this.webResource = this.root.getResourceInternal(this.webAppPath, useClassLoaderResources);
/*     */           
/*  76 */           getLastModified();
/*  77 */           getContentLength();
/*  78 */           this.nextCheck = (this.ttl + now);
/*     */           
/*     */ 
/*  81 */           if ((this.webResource instanceof EmptyResource)) {
/*  82 */             this.cachedExists = Boolean.FALSE;
/*     */           } else {
/*  84 */             this.cachedExists = Boolean.TRUE;
/*     */           }
/*  86 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  91 */     if (now < this.nextCheck) {
/*  92 */       return true;
/*     */     }
/*     */     
/*     */ 
/*  96 */     if (!this.root.isPackedWarFile()) {
/*  97 */       WebResource webResourceInternal = this.root.getResourceInternal(this.webAppPath, useClassLoaderResources);
/*     */       
/*  99 */       if ((!this.webResource.exists()) && (webResourceInternal.exists())) {
/* 100 */         return false;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 105 */       if ((this.webResource.getLastModified() != getLastModified()) || 
/* 106 */         (this.webResource.getContentLength() != getContentLength())) {
/* 107 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 111 */       if ((this.webResource.getLastModified() != webResourceInternal.getLastModified()) || 
/* 112 */         (this.webResource.getContentLength() != webResourceInternal.getContentLength())) {
/* 113 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 117 */     this.nextCheck = (this.ttl + now);
/* 118 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean validateResources(boolean useClassLoaderResources) {
/* 122 */     long now = System.currentTimeMillis();
/*     */     
/* 124 */     if (this.webResources == null) {
/* 125 */       synchronized (this) {
/* 126 */         if (this.webResources == null) {
/* 127 */           this.webResources = this.root.getResourcesInternal(this.webAppPath, useClassLoaderResources);
/*     */           
/* 129 */           this.nextCheck = (this.ttl + now);
/* 130 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 135 */     if (now < this.nextCheck) {
/* 136 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 140 */     if (this.root.isPackedWarFile()) {
/* 141 */       this.nextCheck = (this.ttl + now);
/* 142 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 146 */     return false;
/*     */   }
/*     */   
/*     */   protected long getNextCheck()
/*     */   {
/* 151 */     return this.nextCheck;
/*     */   }
/*     */   
/*     */   public long getLastModified()
/*     */   {
/* 156 */     Long cachedLastModified = this.cachedLastModified;
/* 157 */     if (cachedLastModified == null)
/*     */     {
/* 159 */       cachedLastModified = Long.valueOf(this.webResource.getLastModified());
/* 160 */       this.cachedLastModified = cachedLastModified;
/*     */     }
/* 162 */     return cachedLastModified.longValue();
/*     */   }
/*     */   
/*     */   public String getLastModifiedHttp()
/*     */   {
/* 167 */     String cachedLastModifiedHttp = this.cachedLastModifiedHttp;
/* 168 */     if (cachedLastModifiedHttp == null) {
/* 169 */       cachedLastModifiedHttp = this.webResource.getLastModifiedHttp();
/* 170 */       this.cachedLastModifiedHttp = cachedLastModifiedHttp;
/*     */     }
/* 172 */     return cachedLastModifiedHttp;
/*     */   }
/*     */   
/*     */   public boolean exists()
/*     */   {
/* 177 */     Boolean cachedExists = this.cachedExists;
/* 178 */     if (cachedExists == null) {
/* 179 */       cachedExists = Boolean.valueOf(this.webResource.exists());
/* 180 */       this.cachedExists = cachedExists;
/*     */     }
/* 182 */     return cachedExists.booleanValue();
/*     */   }
/*     */   
/*     */   public boolean isVirtual()
/*     */   {
/* 187 */     Boolean cachedIsVirtual = this.cachedIsVirtual;
/* 188 */     if (cachedIsVirtual == null) {
/* 189 */       cachedIsVirtual = Boolean.valueOf(this.webResource.isVirtual());
/* 190 */       this.cachedIsVirtual = cachedIsVirtual;
/*     */     }
/* 192 */     return cachedIsVirtual.booleanValue();
/*     */   }
/*     */   
/*     */   public boolean isDirectory()
/*     */   {
/* 197 */     Boolean cachedIsDirectory = this.cachedIsDirectory;
/* 198 */     if (cachedIsDirectory == null) {
/* 199 */       cachedIsDirectory = Boolean.valueOf(this.webResource.isDirectory());
/* 200 */       this.cachedIsDirectory = cachedIsDirectory;
/*     */     }
/* 202 */     return cachedIsDirectory.booleanValue();
/*     */   }
/*     */   
/*     */   public boolean isFile()
/*     */   {
/* 207 */     Boolean cachedIsFile = this.cachedIsFile;
/* 208 */     if (cachedIsFile == null) {
/* 209 */       cachedIsFile = Boolean.valueOf(this.webResource.isFile());
/* 210 */       this.cachedIsFile = cachedIsFile;
/*     */     }
/* 212 */     return cachedIsFile.booleanValue();
/*     */   }
/*     */   
/*     */   public boolean delete()
/*     */   {
/* 217 */     boolean deleteResult = this.webResource.delete();
/* 218 */     if (deleteResult) {
/* 219 */       this.cache.removeCacheEntry(this.webAppPath);
/*     */     }
/* 221 */     return deleteResult;
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/* 226 */     return this.webResource.getName();
/*     */   }
/*     */   
/*     */   public long getContentLength()
/*     */   {
/* 231 */     Long cachedContentLength = this.cachedContentLength;
/* 232 */     if (cachedContentLength == null) {
/* 233 */       long result = 0L;
/* 234 */       if (this.webResource != null) {
/* 235 */         result = this.webResource.getContentLength();
/* 236 */         cachedContentLength = Long.valueOf(result);
/* 237 */         this.cachedContentLength = cachedContentLength;
/*     */       }
/* 239 */       return result;
/*     */     }
/* 241 */     return cachedContentLength.longValue();
/*     */   }
/*     */   
/*     */   public String getCanonicalPath()
/*     */   {
/* 246 */     return this.webResource.getCanonicalPath();
/*     */   }
/*     */   
/*     */   public boolean canRead()
/*     */   {
/* 251 */     return this.webResource.canRead();
/*     */   }
/*     */   
/*     */   public String getWebappPath()
/*     */   {
/* 256 */     return this.webAppPath;
/*     */   }
/*     */   
/*     */   public String getETag()
/*     */   {
/* 261 */     return this.webResource.getETag();
/*     */   }
/*     */   
/*     */   public void setMimeType(String mimeType)
/*     */   {
/* 266 */     this.webResource.setMimeType(mimeType);
/*     */   }
/*     */   
/*     */   public String getMimeType()
/*     */   {
/* 271 */     return this.webResource.getMimeType();
/*     */   }
/*     */   
/*     */   public InputStream getInputStream()
/*     */   {
/* 276 */     byte[] content = getContent();
/* 277 */     if (content == null)
/*     */     {
/* 279 */       return this.webResource.getInputStream();
/*     */     }
/* 281 */     return new ByteArrayInputStream(content);
/*     */   }
/*     */   
/*     */   public byte[] getContent()
/*     */   {
/* 286 */     byte[] cachedContent = this.cachedContent;
/* 287 */     if (cachedContent == null) {
/* 288 */       if (getContentLength() > this.objectMaxSizeBytes) {
/* 289 */         return null;
/*     */       }
/* 291 */       cachedContent = this.webResource.getContent();
/* 292 */       this.cachedContent = cachedContent;
/*     */     }
/* 294 */     return cachedContent;
/*     */   }
/*     */   
/*     */   public long getCreation()
/*     */   {
/* 299 */     return this.webResource.getCreation();
/*     */   }
/*     */   
/*     */   public URL getURL()
/*     */   {
/* 304 */     return this.webResource.getURL();
/*     */   }
/*     */   
/*     */   public URL getCodeBase()
/*     */   {
/* 309 */     return this.webResource.getCodeBase();
/*     */   }
/*     */   
/*     */   public Certificate[] getCertificates()
/*     */   {
/* 314 */     return this.webResource.getCertificates();
/*     */   }
/*     */   
/*     */   public Manifest getManifest()
/*     */   {
/* 319 */     return this.webResource.getManifest();
/*     */   }
/*     */   
/*     */   public WebResourceRoot getWebResourceRoot()
/*     */   {
/* 324 */     return this.webResource.getWebResourceRoot();
/*     */   }
/*     */   
/*     */   WebResource getWebResource() {
/* 328 */     return this.webResource;
/*     */   }
/*     */   
/*     */   WebResource[] getWebResources() {
/* 332 */     return this.webResources;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   long getSize()
/*     */   {
/* 339 */     long result = 500L;
/* 340 */     if (getContentLength() <= this.objectMaxSizeBytes) {
/* 341 */       result += getContentLength();
/*     */     }
/* 343 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\webresources\CachedResource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */