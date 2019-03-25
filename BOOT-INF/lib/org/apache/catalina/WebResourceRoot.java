/*     */ package org.apache.catalina;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract interface WebResourceRoot
/*     */   extends Lifecycle
/*     */ {
/*     */   public abstract WebResource getResource(String paramString);
/*     */   
/*     */   public abstract WebResource[] getResources(String paramString);
/*     */   
/*     */   public abstract WebResource getClassLoaderResource(String paramString);
/*     */   
/*     */   public abstract WebResource[] getClassLoaderResources(String paramString);
/*     */   
/*     */   public abstract String[] list(String paramString);
/*     */   
/*     */   public abstract Set<String> listWebAppPaths(String paramString);
/*     */   
/*     */   public abstract WebResource[] listResources(String paramString);
/*     */   
/*     */   public abstract boolean mkdir(String paramString);
/*     */   
/*     */   public abstract boolean write(String paramString, InputStream paramInputStream, boolean paramBoolean);
/*     */   
/*     */   public abstract void createWebResourceSet(ResourceSetType paramResourceSetType, String paramString1, URL paramURL, String paramString2);
/*     */   
/*     */   public abstract void createWebResourceSet(ResourceSetType paramResourceSetType, String paramString1, String paramString2, String paramString3, String paramString4);
/*     */   
/*     */   public abstract void addPreResources(WebResourceSet paramWebResourceSet);
/*     */   
/*     */   public abstract WebResourceSet[] getPreResources();
/*     */   
/*     */   public abstract void addJarResources(WebResourceSet paramWebResourceSet);
/*     */   
/*     */   public abstract WebResourceSet[] getJarResources();
/*     */   
/*     */   public abstract void addPostResources(WebResourceSet paramWebResourceSet);
/*     */   
/*     */   public abstract WebResourceSet[] getPostResources();
/*     */   
/*     */   public abstract Context getContext();
/*     */   
/*     */   public abstract void setContext(Context paramContext);
/*     */   
/*     */   public abstract void setAllowLinking(boolean paramBoolean);
/*     */   
/*     */   public abstract boolean getAllowLinking();
/*     */   
/*     */   public abstract void setCachingAllowed(boolean paramBoolean);
/*     */   
/*     */   public abstract boolean isCachingAllowed();
/*     */   
/*     */   public abstract void setCacheTtl(long paramLong);
/*     */   
/*     */   public abstract long getCacheTtl();
/*     */   
/*     */   public abstract void setCacheMaxSize(long paramLong);
/*     */   
/*     */   public abstract long getCacheMaxSize();
/*     */   
/*     */   public abstract void setCacheObjectMaxSize(int paramInt);
/*     */   
/*     */   public abstract int getCacheObjectMaxSize();
/*     */   
/*     */   public abstract void setTrackLockedFiles(boolean paramBoolean);
/*     */   
/*     */   public abstract boolean getTrackLockedFiles();
/*     */   
/*     */   public abstract void backgroundProcess();
/*     */   
/*     */   public abstract void registerTrackedResource(TrackedWebResource paramTrackedWebResource);
/*     */   
/*     */   public abstract void deregisterTrackedResource(TrackedWebResource paramTrackedWebResource);
/*     */   
/*     */   public abstract List<URL> getBaseUrls();
/*     */   
/*     */   public abstract void gc();
/*     */   
/*     */   public static enum ResourceSetType
/*     */   {
/* 434 */     PRE, 
/* 435 */     RESOURCE_JAR, 
/* 436 */     POST, 
/* 437 */     CLASSES_JAR;
/*     */     
/*     */     private ResourceSetType() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\WebResourceRoot.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */