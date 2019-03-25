/*     */ package org.apache.catalina.webresources;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.Host;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.LifecycleState;
/*     */ import org.apache.catalina.TrackedWebResource;
/*     */ import org.apache.catalina.WebResource;
/*     */ import org.apache.catalina.WebResourceRoot;
/*     */ import org.apache.catalina.WebResourceRoot.ResourceSetType;
/*     */ import org.apache.catalina.WebResourceSet;
/*     */ import org.apache.catalina.util.LifecycleMBeanBase;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.buf.UriUtil;
/*     */ import org.apache.tomcat.util.http.RequestUtil;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StandardRoot
/*     */   extends LifecycleMBeanBase
/*     */   implements WebResourceRoot
/*     */ {
/*  65 */   private static final Log log = LogFactory.getLog(StandardRoot.class);
/*  66 */   protected static final StringManager sm = StringManager.getManager(StandardRoot.class);
/*     */   
/*     */   private Context context;
/*  69 */   private boolean allowLinking = false;
/*  70 */   private final List<WebResourceSet> preResources = new ArrayList();
/*     */   private WebResourceSet main;
/*  72 */   private final List<WebResourceSet> classResources = new ArrayList();
/*  73 */   private final List<WebResourceSet> jarResources = new ArrayList();
/*  74 */   private final List<WebResourceSet> postResources = new ArrayList();
/*     */   
/*  76 */   private final Cache cache = new Cache(this);
/*  77 */   private boolean cachingAllowed = true;
/*  78 */   private ObjectName cacheJmxName = null;
/*     */   
/*  80 */   private boolean trackLockedFiles = false;
/*     */   
/*  82 */   private final Set<TrackedWebResource> trackedResources = Collections.newSetFromMap(new ConcurrentHashMap());
/*     */   
/*     */ 
/*  85 */   private final List<WebResourceSet> mainResources = new ArrayList();
/*  86 */   private final List<List<WebResourceSet>> allResources = new ArrayList();
/*     */   
/*     */   public StandardRoot() {
/*  89 */     this.allResources.add(this.preResources);
/*  90 */     this.allResources.add(this.mainResources);
/*  91 */     this.allResources.add(this.classResources);
/*  92 */     this.allResources.add(this.jarResources);
/*  93 */     this.allResources.add(this.postResources);
/*     */   }
/*     */   
/*     */   public StandardRoot(Context context)
/*     */   {
/*  89 */     this.allResources.add(this.preResources);
/*  90 */     this.allResources.add(this.mainResources);
/*  91 */     this.allResources.add(this.classResources);
/*  92 */     this.allResources.add(this.jarResources);
/*  93 */     this.allResources.add(this.postResources);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 108 */     this.context = context;
/*     */   }
/*     */   
/*     */   public String[] list(String path)
/*     */   {
/* 113 */     return list(path, true);
/*     */   }
/*     */   
/*     */   private String[] list(String path, boolean validate) {
/* 117 */     if (validate) {
/* 118 */       path = validate(path);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 125 */     HashSet<String> result = new LinkedHashSet();
/* 126 */     for (List<WebResourceSet> list : this.allResources) {
/* 127 */       for (WebResourceSet webResourceSet : list) {
/* 128 */         if (!webResourceSet.getClassLoaderOnly()) {
/* 129 */           String[] entries = webResourceSet.list(path);
/* 130 */           for (String entry : entries) {
/* 131 */             result.add(entry);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 136 */     return (String[])result.toArray(new String[result.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */   public Set<String> listWebAppPaths(String path)
/*     */   {
/* 142 */     path = validate(path);
/*     */     
/*     */ 
/* 145 */     HashSet<String> result = new HashSet();
/* 146 */     for (List<WebResourceSet> list : this.allResources) {
/* 147 */       for (WebResourceSet webResourceSet : list) {
/* 148 */         if (!webResourceSet.getClassLoaderOnly()) {
/* 149 */           result.addAll(webResourceSet.listWebAppPaths(path));
/*     */         }
/*     */       }
/*     */     }
/* 153 */     if (result.size() == 0) {
/* 154 */       return null;
/*     */     }
/* 156 */     return result;
/*     */   }
/*     */   
/*     */   public boolean mkdir(String path)
/*     */   {
/* 161 */     path = validate(path);
/*     */     
/* 163 */     if (preResourceExists(path)) {
/* 164 */       return false;
/*     */     }
/*     */     
/* 167 */     boolean mkdirResult = this.main.mkdir(path);
/*     */     
/* 169 */     if ((mkdirResult) && (isCachingAllowed()))
/*     */     {
/* 171 */       this.cache.removeCacheEntry(path);
/*     */     }
/* 173 */     return mkdirResult;
/*     */   }
/*     */   
/*     */   public boolean write(String path, InputStream is, boolean overwrite)
/*     */   {
/* 178 */     path = validate(path);
/*     */     
/* 180 */     if ((!overwrite) && (preResourceExists(path))) {
/* 181 */       return false;
/*     */     }
/*     */     
/* 184 */     boolean writeResult = this.main.write(path, is, overwrite);
/*     */     
/* 186 */     if ((writeResult) && (isCachingAllowed()))
/*     */     {
/* 188 */       this.cache.removeCacheEntry(path);
/*     */     }
/*     */     
/* 191 */     return writeResult;
/*     */   }
/*     */   
/*     */   private boolean preResourceExists(String path) {
/* 195 */     for (WebResourceSet webResourceSet : this.preResources) {
/* 196 */       WebResource webResource = webResourceSet.getResource(path);
/* 197 */       if (webResource.exists()) {
/* 198 */         return true;
/*     */       }
/*     */     }
/* 201 */     return false;
/*     */   }
/*     */   
/*     */   public WebResource getResource(String path)
/*     */   {
/* 206 */     return getResource(path, true, false);
/*     */   }
/*     */   
/*     */   private WebResource getResource(String path, boolean validate, boolean useClassLoaderResources)
/*     */   {
/* 211 */     if (validate) {
/* 212 */       path = validate(path);
/*     */     }
/*     */     
/* 215 */     if (isCachingAllowed()) {
/* 216 */       return this.cache.getResource(path, useClassLoaderResources);
/*     */     }
/* 218 */     return getResourceInternal(path, useClassLoaderResources);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public WebResource getClassLoaderResource(String path)
/*     */   {
/* 225 */     return getResource("/WEB-INF/classes" + path, true, true);
/*     */   }
/*     */   
/*     */ 
/*     */   public WebResource[] getClassLoaderResources(String path)
/*     */   {
/* 231 */     return getResources("/WEB-INF/classes" + path, true);
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
/*     */   private String validate(String path)
/*     */   {
/* 244 */     if (!getState().isAvailable())
/*     */     {
/* 246 */       throw new IllegalStateException(sm.getString("standardRoot.checkStateNotStarted"));
/*     */     }
/*     */     
/* 249 */     if ((path == null) || (path.length() == 0) || (!path.startsWith("/")))
/*     */     {
/* 251 */       throw new IllegalArgumentException(sm.getString("standardRoot.invalidPath", new Object[] { path }));
/*     */     }
/*     */     String result;
/*     */     String result;
/* 255 */     if (File.separatorChar == '\\')
/*     */     {
/*     */ 
/* 258 */       result = RequestUtil.normalize(path, true);
/*     */     }
/*     */     else
/*     */     {
/* 262 */       result = RequestUtil.normalize(path, false);
/*     */     }
/* 264 */     if ((result == null) || (result.length() == 0) || (!result.startsWith("/")))
/*     */     {
/* 266 */       throw new IllegalArgumentException(sm.getString("standardRoot.invalidPathNormal", new Object[] { path, result }));
/*     */     }
/*     */     
/* 269 */     return result;
/*     */   }
/*     */   
/*     */   protected final WebResource getResourceInternal(String path, boolean useClassLoaderResources)
/*     */   {
/* 274 */     WebResource result = null;
/* 275 */     WebResource virtual = null;
/* 276 */     WebResource mainEmpty = null;
/* 277 */     for (List<WebResourceSet> list : this.allResources) {
/* 278 */       for (WebResourceSet webResourceSet : list) {
/* 279 */         if (((!useClassLoaderResources) && (!webResourceSet.getClassLoaderOnly())) || ((useClassLoaderResources) && 
/* 280 */           (!webResourceSet.getStaticOnly()))) {
/* 281 */           result = webResourceSet.getResource(path);
/* 282 */           if (result.exists()) {
/* 283 */             return result;
/*     */           }
/* 285 */           if (virtual == null) {
/* 286 */             if (result.isVirtual()) {
/* 287 */               virtual = result;
/* 288 */             } else if (this.main.equals(webResourceSet)) {
/* 289 */               mainEmpty = result;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 297 */     if (virtual != null) {
/* 298 */       return virtual;
/*     */     }
/*     */     
/*     */ 
/* 302 */     return mainEmpty;
/*     */   }
/*     */   
/*     */   public WebResource[] getResources(String path)
/*     */   {
/* 307 */     return getResources(path, false);
/*     */   }
/*     */   
/*     */   private WebResource[] getResources(String path, boolean useClassLoaderResources)
/*     */   {
/* 312 */     path = validate(path);
/*     */     
/* 314 */     if (isCachingAllowed()) {
/* 315 */       return this.cache.getResources(path, useClassLoaderResources);
/*     */     }
/* 317 */     return getResourcesInternal(path, useClassLoaderResources);
/*     */   }
/*     */   
/*     */ 
/*     */   protected WebResource[] getResourcesInternal(String path, boolean useClassLoaderResources)
/*     */   {
/* 323 */     List<WebResource> result = new ArrayList();
/* 324 */     for (List<WebResourceSet> list : this.allResources) {
/* 325 */       for (WebResourceSet webResourceSet : list) {
/* 326 */         if ((useClassLoaderResources) || (!webResourceSet.getClassLoaderOnly())) {
/* 327 */           WebResource webResource = webResourceSet.getResource(path);
/* 328 */           if (webResource.exists()) {
/* 329 */             result.add(webResource);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 335 */     if (result.size() == 0) {
/* 336 */       result.add(this.main.getResource(path));
/*     */     }
/*     */     
/* 339 */     return (WebResource[])result.toArray(new WebResource[result.size()]);
/*     */   }
/*     */   
/*     */   public WebResource[] listResources(String path)
/*     */   {
/* 344 */     return listResources(path, true);
/*     */   }
/*     */   
/*     */   protected WebResource[] listResources(String path, boolean validate) {
/* 348 */     if (validate) {
/* 349 */       path = validate(path);
/*     */     }
/*     */     
/* 352 */     String[] resources = list(path, false);
/* 353 */     WebResource[] result = new WebResource[resources.length];
/* 354 */     for (int i = 0; i < resources.length; i++) {
/* 355 */       if (path.charAt(path.length() - 1) == '/') {
/* 356 */         result[i] = getResource(path + resources[i], false, false);
/*     */       } else {
/* 358 */         result[i] = getResource(path + '/' + resources[i], false, false);
/*     */       }
/*     */     }
/* 361 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void createWebResourceSet(WebResourceRoot.ResourceSetType type, String webAppMount, URL url, String internalPath)
/*     */   {
/* 370 */     BaseLocation baseLocation = new BaseLocation(url);
/* 371 */     createWebResourceSet(type, webAppMount, baseLocation.getBasePath(), baseLocation
/* 372 */       .getArchivePath(), internalPath);
/*     */   }
/*     */   
/*     */   public void createWebResourceSet(WebResourceRoot.ResourceSetType type, String webAppMount, String base, String archivePath, String internalPath)
/*     */   {
/*     */     List<WebResourceSet> resourceList;
/*     */     List<WebResourceSet> resourceList;
/*     */     List<WebResourceSet> resourceList;
/*     */     List<WebResourceSet> resourceList;
/* 381 */     switch (type) {
/*     */     case PRE: 
/* 383 */       resourceList = this.preResources;
/* 384 */       break;
/*     */     case CLASSES_JAR: 
/* 386 */       resourceList = this.classResources;
/* 387 */       break;
/*     */     case RESOURCE_JAR: 
/* 389 */       resourceList = this.jarResources;
/* 390 */       break;
/*     */     case POST: 
/* 392 */       resourceList = this.postResources;
/* 393 */       break;
/*     */     
/*     */     default: 
/* 396 */       throw new IllegalArgumentException(sm.getString("standardRoot.createUnknownType", new Object[] { type }));
/*     */     }
/*     */     
/*     */     
/*     */     List<WebResourceSet> resourceList;
/* 401 */     File file = new File(base);
/*     */     WebResourceSet resourceSet;
/* 403 */     if (file.isFile()) { WebResourceSet resourceSet;
/* 404 */       if (archivePath != null)
/*     */       {
/* 406 */         resourceSet = new JarWarResourceSet(this, webAppMount, base, archivePath, internalPath);
/*     */       } else { WebResourceSet resourceSet;
/* 408 */         if (file.getName().toLowerCase(Locale.ENGLISH).endsWith(".jar")) {
/* 409 */           resourceSet = new JarResourceSet(this, webAppMount, base, internalPath);
/*     */         }
/*     */         else
/* 412 */           resourceSet = new FileResourceSet(this, webAppMount, base, internalPath);
/*     */       }
/*     */     } else { WebResourceSet resourceSet;
/* 415 */       if (file.isDirectory()) {
/* 416 */         resourceSet = new DirResourceSet(this, webAppMount, base, internalPath);
/*     */       }
/*     */       else
/*     */       {
/* 420 */         throw new IllegalArgumentException(sm.getString("standardRoot.createInvalidFile", new Object[] { file })); }
/*     */     }
/*     */     WebResourceSet resourceSet;
/* 423 */     if (type.equals(WebResourceRoot.ResourceSetType.CLASSES_JAR)) {
/* 424 */       resourceSet.setClassLoaderOnly(true);
/* 425 */     } else if (type.equals(WebResourceRoot.ResourceSetType.RESOURCE_JAR)) {
/* 426 */       resourceSet.setStaticOnly(true);
/*     */     }
/*     */     
/* 429 */     resourceList.add(resourceSet);
/*     */   }
/*     */   
/*     */   public void addPreResources(WebResourceSet webResourceSet)
/*     */   {
/* 434 */     webResourceSet.setRoot(this);
/* 435 */     this.preResources.add(webResourceSet);
/*     */   }
/*     */   
/*     */   public WebResourceSet[] getPreResources()
/*     */   {
/* 440 */     return (WebResourceSet[])this.preResources.toArray(new WebResourceSet[this.preResources.size()]);
/*     */   }
/*     */   
/*     */   public void addJarResources(WebResourceSet webResourceSet)
/*     */   {
/* 445 */     webResourceSet.setRoot(this);
/* 446 */     this.jarResources.add(webResourceSet);
/*     */   }
/*     */   
/*     */   public WebResourceSet[] getJarResources()
/*     */   {
/* 451 */     return (WebResourceSet[])this.jarResources.toArray(new WebResourceSet[this.jarResources.size()]);
/*     */   }
/*     */   
/*     */   public void addPostResources(WebResourceSet webResourceSet)
/*     */   {
/* 456 */     webResourceSet.setRoot(this);
/* 457 */     this.postResources.add(webResourceSet);
/*     */   }
/*     */   
/*     */   public WebResourceSet[] getPostResources()
/*     */   {
/* 462 */     return (WebResourceSet[])this.postResources.toArray(new WebResourceSet[this.postResources.size()]);
/*     */   }
/*     */   
/*     */   protected WebResourceSet[] getClassResources() {
/* 466 */     return (WebResourceSet[])this.classResources.toArray(new WebResourceSet[this.classResources.size()]);
/*     */   }
/*     */   
/*     */   protected void addClassResources(WebResourceSet webResourceSet) {
/* 470 */     webResourceSet.setRoot(this);
/* 471 */     this.classResources.add(webResourceSet);
/*     */   }
/*     */   
/*     */   public void setAllowLinking(boolean allowLinking)
/*     */   {
/* 476 */     if ((this.allowLinking != allowLinking) && (this.cachingAllowed))
/*     */     {
/* 478 */       this.cache.clear();
/*     */     }
/* 480 */     this.allowLinking = allowLinking;
/*     */   }
/*     */   
/*     */   public boolean getAllowLinking()
/*     */   {
/* 485 */     return this.allowLinking;
/*     */   }
/*     */   
/*     */   public void setCachingAllowed(boolean cachingAllowed)
/*     */   {
/* 490 */     this.cachingAllowed = cachingAllowed;
/* 491 */     if (!cachingAllowed) {
/* 492 */       this.cache.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isCachingAllowed()
/*     */   {
/* 498 */     return this.cachingAllowed;
/*     */   }
/*     */   
/*     */   public long getCacheTtl()
/*     */   {
/* 503 */     return this.cache.getTtl();
/*     */   }
/*     */   
/*     */   public void setCacheTtl(long cacheTtl)
/*     */   {
/* 508 */     this.cache.setTtl(cacheTtl);
/*     */   }
/*     */   
/*     */   public long getCacheMaxSize()
/*     */   {
/* 513 */     return this.cache.getMaxSize();
/*     */   }
/*     */   
/*     */   public void setCacheMaxSize(long cacheMaxSize)
/*     */   {
/* 518 */     this.cache.setMaxSize(cacheMaxSize);
/*     */   }
/*     */   
/*     */   public void setCacheObjectMaxSize(int cacheObjectMaxSize)
/*     */   {
/* 523 */     this.cache.setObjectMaxSize(cacheObjectMaxSize);
/*     */     
/*     */ 
/* 526 */     if (getState().isAvailable()) {
/* 527 */       this.cache.enforceObjectMaxSizeLimit();
/*     */     }
/*     */   }
/*     */   
/*     */   public int getCacheObjectMaxSize()
/*     */   {
/* 533 */     return this.cache.getObjectMaxSize();
/*     */   }
/*     */   
/*     */   public void setTrackLockedFiles(boolean trackLockedFiles)
/*     */   {
/* 538 */     this.trackLockedFiles = trackLockedFiles;
/* 539 */     if (!trackLockedFiles) {
/* 540 */       this.trackedResources.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean getTrackLockedFiles()
/*     */   {
/* 546 */     return this.trackLockedFiles;
/*     */   }
/*     */   
/*     */   public List<String> getTrackedResources() {
/* 550 */     List<String> result = new ArrayList(this.trackedResources.size());
/* 551 */     for (TrackedWebResource resource : this.trackedResources) {
/* 552 */       result.add(resource.toString());
/*     */     }
/* 554 */     return result;
/*     */   }
/*     */   
/*     */   public Context getContext()
/*     */   {
/* 559 */     return this.context;
/*     */   }
/*     */   
/*     */   public void setContext(Context context)
/*     */   {
/* 564 */     this.context = context;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void processWebInfLib()
/*     */     throws LifecycleException
/*     */   {
/* 582 */     WebResource[] possibleJars = listResources("/WEB-INF/lib", false);
/*     */     
/* 584 */     for (WebResource possibleJar : possibleJars) {
/* 585 */       if ((possibleJar.isFile()) && (possibleJar.getName().endsWith(".jar"))) {
/* 586 */         createWebResourceSet(WebResourceRoot.ResourceSetType.CLASSES_JAR, "/WEB-INF/classes", possibleJar
/* 587 */           .getURL(), "/");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void setMainResources(WebResourceSet main)
/*     */   {
/* 597 */     this.main = main;
/* 598 */     this.mainResources.clear();
/* 599 */     this.mainResources.add(main);
/*     */   }
/*     */   
/*     */ 
/*     */   public void backgroundProcess()
/*     */   {
/* 605 */     this.cache.backgroundProcess();
/* 606 */     gc();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void gc()
/*     */   {
/* 613 */     for (List<WebResourceSet> list : this.allResources) {
/* 614 */       for (WebResourceSet webResourceSet : list) {
/* 615 */         webResourceSet.gc();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void registerTrackedResource(TrackedWebResource trackedResource)
/*     */   {
/* 622 */     this.trackedResources.add(trackedResource);
/*     */   }
/*     */   
/*     */ 
/*     */   public void deregisterTrackedResource(TrackedWebResource trackedResource)
/*     */   {
/* 628 */     this.trackedResources.remove(trackedResource);
/*     */   }
/*     */   
/*     */ 
/*     */   public List<URL> getBaseUrls()
/*     */   {
/* 634 */     List<URL> result = new ArrayList();
/* 635 */     for (List<WebResourceSet> list : this.allResources) {
/* 636 */       for (WebResourceSet webResourceSet : list) {
/* 637 */         if (!webResourceSet.getClassLoaderOnly()) {
/* 638 */           URL url = webResourceSet.getBaseUrl();
/* 639 */           if (url != null) {
/* 640 */             result.add(url);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 645 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isPackedWarFile()
/*     */   {
/* 656 */     return ((this.main instanceof WarResourceSet)) && (this.preResources.isEmpty()) && (this.postResources.isEmpty());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getDomainInternal()
/*     */   {
/* 663 */     return this.context.getDomain();
/*     */   }
/*     */   
/*     */   protected String getObjectNameKeyProperties()
/*     */   {
/* 668 */     StringBuilder keyProperties = new StringBuilder("type=WebResourceRoot");
/* 669 */     keyProperties.append(this.context.getMBeanKeyProperties());
/*     */     
/* 671 */     return keyProperties.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   protected void initInternal()
/*     */     throws LifecycleException
/*     */   {
/* 678 */     super.initInternal();
/*     */     
/* 680 */     this.cacheJmxName = register(this.cache, getObjectNameKeyProperties() + ",name=Cache");
/*     */     
/* 682 */     registerURLStreamHandlerFactory();
/*     */     
/* 684 */     if (this.context == null)
/*     */     {
/* 686 */       throw new IllegalStateException(sm.getString("standardRoot.noContext"));
/*     */     }
/*     */     
/* 689 */     for (List<WebResourceSet> list : this.allResources) {
/* 690 */       for (WebResourceSet webResourceSet : list) {
/* 691 */         webResourceSet.init();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void registerURLStreamHandlerFactory()
/*     */   {
/* 699 */     TomcatURLStreamHandlerFactory.register();
/*     */   }
/*     */   
/*     */   protected void startInternal() throws LifecycleException
/*     */   {
/* 704 */     this.mainResources.clear();
/*     */     
/* 706 */     this.main = createMainResourceSet();
/*     */     
/* 708 */     this.mainResources.add(this.main);
/*     */     
/* 710 */     for (List<WebResourceSet> list : this.allResources)
/*     */     {
/* 712 */       if (list != this.classResources) {
/* 713 */         for (WebResourceSet webResourceSet : list) {
/* 714 */           webResourceSet.start();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 721 */     processWebInfLib();
/*     */     
/* 723 */     for (WebResourceSet classResource : this.classResources) {
/* 724 */       classResource.start();
/*     */     }
/*     */     
/* 727 */     this.cache.enforceObjectMaxSizeLimit();
/*     */     
/* 729 */     setState(LifecycleState.STARTING);
/*     */   }
/*     */   
/*     */   protected WebResourceSet createMainResourceSet() {
/* 733 */     String docBase = this.context.getDocBase();
/*     */     
/*     */     WebResourceSet mainResourceSet;
/* 736 */     if (docBase == null) {
/* 737 */       mainResourceSet = new EmptyResourceSet(this);
/*     */     } else {
/* 739 */       File f = new File(docBase);
/* 740 */       if (!f.isAbsolute())
/* 741 */         f = new File(((Host)this.context.getParent()).getAppBaseFile(), f.getPath());
/*     */       WebResourceSet mainResourceSet;
/* 743 */       if (f.isDirectory()) {
/* 744 */         mainResourceSet = new DirResourceSet(this, "/", f.getAbsolutePath(), "/"); } else { WebResourceSet mainResourceSet;
/* 745 */         if ((f.isFile()) && (docBase.endsWith(".war"))) {
/* 746 */           mainResourceSet = new WarResourceSet(this, "/", f.getAbsolutePath());
/*     */         }
/*     */         else
/* 749 */           throw new IllegalArgumentException(sm.getString("standardRoot.startInvalidMain", new Object[] {f
/* 750 */             .getAbsolutePath() }));
/*     */       }
/*     */     }
/*     */     WebResourceSet mainResourceSet;
/* 754 */     return mainResourceSet;
/*     */   }
/*     */   
/*     */   protected void stopInternal() throws LifecycleException
/*     */   {
/* 759 */     for (List<WebResourceSet> list : this.allResources) {
/* 760 */       for (WebResourceSet webResourceSet : list) {
/* 761 */         webResourceSet.stop();
/*     */       }
/*     */     }
/*     */     
/* 765 */     if (this.main != null) {
/* 766 */       this.main.destroy();
/*     */     }
/* 768 */     this.mainResources.clear();
/*     */     
/* 770 */     for (WebResourceSet webResourceSet : this.jarResources) {
/* 771 */       webResourceSet.destroy();
/*     */     }
/* 773 */     this.jarResources.clear();
/*     */     
/* 775 */     for (WebResourceSet webResourceSet : this.classResources) {
/* 776 */       webResourceSet.destroy();
/*     */     }
/* 778 */     this.classResources.clear();
/*     */     
/* 780 */     for (TrackedWebResource trackedResource : this.trackedResources) {
/* 781 */       log.error(sm.getString("standardRoot.lockedFile", new Object[] {this.context
/* 782 */         .getName(), trackedResource
/* 783 */         .getName() }), trackedResource
/* 784 */         .getCreatedBy());
/*     */       try {
/* 786 */         trackedResource.close();
/*     */       }
/*     */       catch (IOException localIOException1) {}
/*     */     }
/*     */     
/* 791 */     this.cache.clear();
/*     */     
/* 793 */     setState(LifecycleState.STOPPING);
/*     */   }
/*     */   
/*     */   protected void destroyInternal() throws LifecycleException
/*     */   {
/* 798 */     for (List<WebResourceSet> list : this.allResources) {
/* 799 */       for (WebResourceSet webResourceSet : list) {
/* 800 */         webResourceSet.destroy();
/*     */       }
/*     */     }
/*     */     
/* 804 */     unregister(this.cacheJmxName);
/*     */     
/* 806 */     super.destroyInternal();
/*     */   }
/*     */   
/*     */ 
/*     */   static class BaseLocation
/*     */   {
/*     */     private final String basePath;
/*     */     private final String archivePath;
/*     */     
/*     */     BaseLocation(URL url)
/*     */     {
/* 817 */       File f = null;
/*     */       
/* 819 */       if (("jar".equals(url.getProtocol())) || ("war".equals(url.getProtocol()))) {
/* 820 */         String jarUrl = url.toString();
/* 821 */         int endOfFileUrl = -1;
/* 822 */         if ("jar".equals(url.getProtocol())) {
/* 823 */           endOfFileUrl = jarUrl.indexOf("!/");
/*     */         } else {
/* 825 */           endOfFileUrl = jarUrl.indexOf(UriUtil.getWarSeparator());
/*     */         }
/* 827 */         String fileUrl = jarUrl.substring(4, endOfFileUrl);
/*     */         try {
/* 829 */           f = new File(new URL(fileUrl).toURI());
/*     */         } catch (MalformedURLException|URISyntaxException e) {
/* 831 */           throw new IllegalArgumentException(e);
/*     */         }
/* 833 */         int startOfArchivePath = endOfFileUrl + 2;
/* 834 */         if (jarUrl.length() > startOfArchivePath) {
/* 835 */           this.archivePath = jarUrl.substring(startOfArchivePath);
/*     */         } else {
/* 837 */           this.archivePath = null;
/*     */         }
/* 839 */       } else if ("file".equals(url.getProtocol())) {
/*     */         try {
/* 841 */           f = new File(url.toURI());
/*     */         } catch (URISyntaxException e) {
/* 843 */           throw new IllegalArgumentException(e);
/*     */         }
/* 845 */         this.archivePath = null;
/*     */       } else {
/* 847 */         throw new IllegalArgumentException(StandardRoot.sm.getString("standardRoot.unsupportedProtocol", new Object[] {url
/* 848 */           .getProtocol() }));
/*     */       }
/*     */       
/* 851 */       this.basePath = f.getAbsolutePath();
/*     */     }
/*     */     
/*     */     String getBasePath()
/*     */     {
/* 856 */       return this.basePath;
/*     */     }
/*     */     
/*     */     String getArchivePath()
/*     */     {
/* 861 */       return this.archivePath;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\webresources\StandardRoot.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */