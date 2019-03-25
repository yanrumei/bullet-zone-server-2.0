/*     */ package org.apache.tomcat.util.scan;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Deque;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Set;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.Manifest;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.Jar;
/*     */ import org.apache.tomcat.JarScanFilter;
/*     */ import org.apache.tomcat.JarScanType;
/*     */ import org.apache.tomcat.JarScanner;
/*     */ import org.apache.tomcat.JarScannerCallback;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
/*     */ import org.apache.tomcat.util.buf.UriUtil;
/*     */ import org.apache.tomcat.util.compat.JreCompat;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StandardJarScanner
/*     */   implements JarScanner
/*     */ {
/*  64 */   private static final Log log = LogFactory.getLog(StandardJarScanner.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  69 */   private static final StringManager sm = StringManager.getManager("org.apache.tomcat.util.scan");
/*     */   private static final Set<ClassLoader> CLASSLOADER_HIERARCHY;
/*     */   
/*     */   static
/*     */   {
/*  74 */     Set<ClassLoader> cls = new HashSet();
/*     */     
/*  76 */     ClassLoader cl = StandardJarScanner.class.getClassLoader();
/*  77 */     while (cl != null) {
/*  78 */       cls.add(cl);
/*  79 */       cl = cl.getParent();
/*     */     }
/*     */     
/*  82 */     CLASSLOADER_HIERARCHY = Collections.unmodifiableSet(cls);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  88 */   private boolean scanClassPath = true;
/*     */   
/*  90 */   public boolean isScanClassPath() { return this.scanClassPath; }
/*     */   
/*     */   public void setScanClassPath(boolean scanClassPath) {
/*  93 */     this.scanClassPath = scanClassPath;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  99 */   private boolean scanManifest = true;
/*     */   
/* 101 */   public boolean isScanManifest() { return this.scanManifest; }
/*     */   
/*     */   public void setScanManifest(boolean scanManifest) {
/* 104 */     this.scanManifest = scanManifest;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 110 */   private boolean scanAllFiles = false;
/*     */   
/* 112 */   public boolean isScanAllFiles() { return this.scanAllFiles; }
/*     */   
/*     */   public void setScanAllFiles(boolean scanAllFiles) {
/* 115 */     this.scanAllFiles = scanAllFiles;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 122 */   private boolean scanAllDirectories = true;
/*     */   
/* 124 */   public boolean isScanAllDirectories() { return this.scanAllDirectories; }
/*     */   
/*     */   public void setScanAllDirectories(boolean scanAllDirectories) {
/* 127 */     this.scanAllDirectories = scanAllDirectories;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 134 */   private boolean scanBootstrapClassPath = false;
/*     */   
/* 136 */   public boolean isScanBootstrapClassPath() { return this.scanBootstrapClassPath; }
/*     */   
/*     */   public void setScanBootstrapClassPath(boolean scanBootstrapClassPath) {
/* 139 */     this.scanBootstrapClassPath = scanBootstrapClassPath;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 145 */   private JarScanFilter jarScanFilter = new StandardJarScanFilter();
/*     */   
/*     */   public JarScanFilter getJarScanFilter() {
/* 148 */     return this.jarScanFilter;
/*     */   }
/*     */   
/*     */   public void setJarScanFilter(JarScanFilter jarScanFilter) {
/* 152 */     this.jarScanFilter = jarScanFilter;
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
/*     */ 
/*     */   public void scan(JarScanType scanType, ServletContext context, JarScannerCallback callback)
/*     */   {
/* 170 */     if (log.isTraceEnabled()) {
/* 171 */       log.trace(sm.getString("jarScan.webinflibStart"));
/*     */     }
/*     */     
/* 174 */     Set<URL> processedURLs = new HashSet();
/*     */     
/*     */ 
/* 177 */     Set<String> dirList = context.getResourcePaths("/WEB-INF/lib/");
/* 178 */     if (dirList != null) {
/* 179 */       for (String path : dirList) {
/* 180 */         if ((path.endsWith(".jar")) && 
/* 181 */           (getJarScanFilter().check(scanType, path
/* 182 */           .substring(path.lastIndexOf('/') + 1))))
/*     */         {
/* 184 */           if (log.isDebugEnabled()) {
/* 185 */             log.debug(sm.getString("jarScan.webinflibJarScan", new Object[] { path }));
/*     */           }
/* 187 */           URL url = null;
/*     */           try {
/* 189 */             url = context.getResource(path);
/* 190 */             processedURLs.add(url);
/* 191 */             process(scanType, callback, url, path, true, null);
/*     */           } catch (IOException e) {
/* 193 */             log.warn(sm.getString("jarScan.webinflibFail", new Object[] { url }), e);
/*     */           }
/*     */         }
/* 196 */         else if (log.isTraceEnabled()) {
/* 197 */           log.trace(sm.getString("jarScan.webinflibJarNoScan", new Object[] { path }));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 205 */       URL webInfURL = context.getResource("/WEB-INF/classes");
/* 206 */       if (webInfURL != null)
/*     */       {
/*     */ 
/*     */ 
/* 210 */         processedURLs.add(webInfURL);
/*     */         
/* 212 */         if (isScanAllDirectories()) {
/* 213 */           URL url = context.getResource("/WEB-INF/classes/META-INF");
/* 214 */           if (url != null) {
/*     */             try {
/* 216 */               callback.scanWebInfClasses();
/*     */             } catch (IOException e) {
/* 218 */               log.warn(sm.getString("jarScan.webinfclassesFail"), e);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (MalformedURLException localMalformedURLException1) {}
/*     */     
/*     */ 
/*     */ 
/* 228 */     if (isScanClassPath()) {
/* 229 */       doScanClassPath(scanType, context, callback, processedURLs);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void doScanClassPath(JarScanType scanType, ServletContext context, JarScannerCallback callback, Set<URL> processedURLs)
/*     */   {
/* 236 */     if (log.isTraceEnabled()) {
/* 237 */       log.trace(sm.getString("jarScan.classloaderStart"));
/*     */     }
/*     */     
/* 240 */     ClassLoader stopLoader = null;
/* 241 */     if (!isScanBootstrapClassPath())
/*     */     {
/* 243 */       stopLoader = ClassLoader.getSystemClassLoader().getParent();
/*     */     }
/*     */     
/* 246 */     ClassLoader classLoader = context.getClassLoader();
/*     */     
/*     */ 
/*     */ 
/* 250 */     boolean isWebapp = true;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 255 */     Deque<URL> classPathUrlsToProcess = new LinkedList();
/*     */     
/* 257 */     while ((classLoader != null) && (classLoader != stopLoader)) {
/* 258 */       if ((classLoader instanceof URLClassLoader)) {
/* 259 */         if (isWebapp) {
/* 260 */           isWebapp = isWebappClassLoader(classLoader);
/*     */         }
/*     */         
/* 263 */         classPathUrlsToProcess.addAll(
/* 264 */           Arrays.asList(((URLClassLoader)classLoader).getURLs()));
/*     */         
/* 266 */         processURLs(scanType, callback, processedURLs, isWebapp, classPathUrlsToProcess);
/*     */       }
/* 268 */       classLoader = classLoader.getParent();
/*     */     }
/*     */     
/* 271 */     if (JreCompat.isJre9Available())
/*     */     {
/*     */ 
/*     */ 
/* 275 */       addClassPath(classPathUrlsToProcess);
/*     */       
/* 277 */       JreCompat.getInstance().addBootModulePath(classPathUrlsToProcess);
/* 278 */       processURLs(scanType, callback, processedURLs, false, classPathUrlsToProcess);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void processURLs(JarScanType scanType, JarScannerCallback callback, Set<URL> processedURLs, boolean isWebapp, Deque<URL> classPathUrlsToProcess)
/*     */   {
/* 285 */     while (!classPathUrlsToProcess.isEmpty()) {
/* 286 */       URL url = (URL)classPathUrlsToProcess.pop();
/*     */       
/* 288 */       if (!processedURLs.contains(url))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 293 */         ClassPathEntry cpe = new ClassPathEntry(url);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 299 */         if (((cpe.isJar()) || (scanType == JarScanType.PLUGGABILITY) || 
/*     */         
/* 301 */           (isScanAllDirectories())) && 
/* 302 */           (getJarScanFilter().check(scanType, cpe
/* 303 */           .getName()))) {
/* 304 */           if (log.isDebugEnabled()) {
/* 305 */             log.debug(sm.getString("jarScan.classloaderJarScan", new Object[] { url }));
/*     */           }
/*     */           try {
/* 308 */             processedURLs.add(url);
/* 309 */             process(scanType, callback, url, null, isWebapp, classPathUrlsToProcess);
/*     */           } catch (IOException ioe) {
/* 311 */             log.warn(sm.getString("jarScan.classloaderFail", new Object[] { url }), ioe);
/*     */           }
/*     */           
/*     */         }
/* 315 */         else if (log.isTraceEnabled()) {
/* 316 */           log.trace(sm.getString("jarScan.classloaderJarNoScan", new Object[] { url }));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void addClassPath(Deque<URL> classPathUrlsToProcess)
/*     */   {
/* 324 */     String classPath = System.getProperty("java.class.path");
/*     */     
/* 326 */     if ((classPath == null) || (classPath.length() == 0)) {
/* 327 */       return;
/*     */     }
/*     */     
/* 330 */     String[] classPathEntries = classPath.split(File.pathSeparator);
/* 331 */     for (String classPathEntry : classPathEntries) {
/* 332 */       File f = new File(classPathEntry);
/*     */       try {
/* 334 */         classPathUrlsToProcess.add(f.toURI().toURL());
/*     */       } catch (MalformedURLException e) {
/* 336 */         log.warn(sm.getString("jarScan.classPath.badEntry", new Object[] { classPathEntry }), e);
/*     */       }
/*     */     }
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
/*     */ 
/*     */ 
/*     */   private static boolean isWebappClassLoader(ClassLoader classLoader)
/*     */   {
/* 357 */     return !CLASSLOADER_HIERARCHY.contains(classLoader);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void process(JarScanType scanType, JarScannerCallback callback, URL url, String webappPath, boolean isWebapp, Deque<URL> classPathUrlsToProcess)
/*     */     throws IOException
/*     */   {
/* 369 */     if (log.isTraceEnabled()) {
/* 370 */       log.trace(sm.getString("jarScan.jarUrlStart", new Object[] { url }));
/*     */     }
/*     */     
/* 373 */     if (("jar".equals(url.getProtocol())) || (url.getPath().endsWith(".jar"))) {
/* 374 */       Jar jar = JarFactory.newInstance(url);Throwable localThrowable7 = null;
/* 375 */       try { if (isScanManifest()) {
/* 376 */           processManifest(jar, isWebapp, classPathUrlsToProcess);
/*     */         }
/* 378 */         callback.scan(jar, webappPath, isWebapp);
/*     */       }
/*     */       catch (Throwable localThrowable2)
/*     */       {
/* 374 */         localThrowable7 = localThrowable2;throw localThrowable2;
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/* 379 */         if (jar != null) if (localThrowable7 != null) try { jar.close(); } catch (Throwable localThrowable3) { localThrowable7.addSuppressed(localThrowable3); } else jar.close();
/* 380 */       } } else if ("file".equals(url.getProtocol()))
/*     */     {
/*     */       try {
/* 383 */         File f = new File(url.toURI());
/* 384 */         if ((f.isFile()) && (isScanAllFiles()))
/*     */         {
/* 386 */           URL jarURL = UriUtil.buildJarUrl(f);
/* 387 */           Jar jar = JarFactory.newInstance(jarURL);Throwable localThrowable8 = null;
/* 388 */           try { if (isScanManifest()) {
/* 389 */               processManifest(jar, isWebapp, classPathUrlsToProcess);
/*     */             }
/* 391 */             callback.scan(jar, webappPath, isWebapp);
/*     */           }
/*     */           catch (Throwable localThrowable5)
/*     */           {
/* 387 */             localThrowable8 = localThrowable5;throw localThrowable5;
/*     */ 
/*     */           }
/*     */           finally
/*     */           {
/* 392 */             if (jar != null) if (localThrowable8 != null) try { jar.close(); } catch (Throwable localThrowable6) { localThrowable8.addSuppressed(localThrowable6); } else jar.close();
/* 393 */           } } else if (f.isDirectory()) {
/* 394 */           if (scanType == JarScanType.PLUGGABILITY) {
/* 395 */             callback.scan(f, webappPath, isWebapp);
/*     */           } else {
/* 397 */             File metainf = new File(f.getAbsoluteFile() + File.separator + "META-INF");
/* 398 */             if (metainf.isDirectory()) {
/* 399 */               callback.scan(f, webappPath, isWebapp);
/*     */             }
/*     */           }
/*     */         }
/*     */       } catch (Throwable t) {
/* 404 */         ExceptionUtils.handleThrowable(t);
/*     */         
/* 406 */         IOException ioe = new IOException();
/* 407 */         ioe.initCause(t);
/* 408 */         throw ioe;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void processManifest(Jar jar, boolean isWebapp, Deque<URL> classPathUrlsToProcess)
/*     */     throws IOException
/*     */   {
/* 419 */     if ((isWebapp) || (classPathUrlsToProcess == null)) {
/* 420 */       return;
/*     */     }
/*     */     
/* 423 */     Manifest manifest = jar.getManifest();
/* 424 */     if (manifest != null) {
/* 425 */       Attributes attributes = manifest.getMainAttributes();
/* 426 */       String classPathAttribute = attributes.getValue("Class-Path");
/* 427 */       if (classPathAttribute == null) {
/* 428 */         return;
/*     */       }
/* 430 */       String[] classPathEntries = classPathAttribute.split(" ");
/* 431 */       for (String classPathEntry : classPathEntries) {
/* 432 */         classPathEntry = classPathEntry.trim();
/* 433 */         if (classPathEntry.length() != 0)
/*     */         {
/*     */ 
/* 436 */           URL jarURL = jar.getJarFileURL();
/*     */           try
/*     */           {
/* 439 */             URI jarURI = jarURL.toURI();
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 450 */             URI classPathEntryURI = jarURI.resolve(classPathEntry);
/* 451 */             classPathEntryURL = classPathEntryURI.toURL();
/*     */           } catch (Exception e) { URL classPathEntryURL;
/* 453 */             if (log.isDebugEnabled()) {
/* 454 */               log.debug(sm.getString("jarScan.invalidUri", new Object[] { jarURL }), e);
/*     */             }
/* 456 */             continue; }
/*     */           URL classPathEntryURL;
/* 458 */           classPathUrlsToProcess.add(classPathEntryURL);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ClassPathEntry
/*     */   {
/*     */     private final boolean jar;
/*     */     private final String name;
/*     */     
/*     */     public ClassPathEntry(URL url) {
/* 470 */       String path = url.getPath();
/* 471 */       int end = path.lastIndexOf(".jar");
/* 472 */       if (end != -1) {
/* 473 */         this.jar = true;
/* 474 */         int start = path.lastIndexOf('/', end);
/* 475 */         this.name = path.substring(start + 1, end + 4);
/*     */       } else {
/* 477 */         this.jar = false;
/* 478 */         if (path.endsWith("/")) {
/* 479 */           path = path.substring(0, path.length() - 1);
/*     */         }
/* 481 */         int start = path.lastIndexOf('/');
/* 482 */         this.name = path.substring(start + 1);
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean isJar()
/*     */     {
/* 488 */       return this.jar;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 492 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\scan\StandardJarScanner.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */