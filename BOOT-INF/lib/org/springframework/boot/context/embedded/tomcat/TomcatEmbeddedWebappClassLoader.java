/*     */ package org.springframework.boot.context.embedded.tomcat;
/*     */ 
/*     */ import java.net.URL;
/*     */ import org.apache.catalina.loader.WebappClassLoader;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TomcatEmbeddedWebappClassLoader
/*     */   extends WebappClassLoader
/*     */ {
/*  36 */   private static final Log logger = LogFactory.getLog(TomcatEmbeddedWebappClassLoader.class);
/*     */   
/*     */ 
/*     */   public TomcatEmbeddedWebappClassLoader() {}
/*     */   
/*     */   public TomcatEmbeddedWebappClassLoader(ClassLoader parent)
/*     */   {
/*  43 */     super(parent);
/*     */   }
/*     */   
/*     */   public synchronized Class<?> loadClass(String name, boolean resolve)
/*     */     throws ClassNotFoundException
/*     */   {
/*  49 */     Class<?> result = findExistingLoadedClass(name);
/*  50 */     result = result == null ? doLoadClass(name) : result;
/*  51 */     if (result == null) {
/*  52 */       throw new ClassNotFoundException(name);
/*     */     }
/*  54 */     return resolveIfNecessary(result, resolve);
/*     */   }
/*     */   
/*     */   private Class<?> findExistingLoadedClass(String name) {
/*  58 */     Class<?> resultClass = findLoadedClass0(name);
/*  59 */     resultClass = resultClass == null ? findLoadedClass(name) : resultClass;
/*  60 */     return resultClass;
/*     */   }
/*     */   
/*     */   private Class<?> doLoadClass(String name) throws ClassNotFoundException {
/*  64 */     checkPackageAccess(name);
/*  65 */     if ((this.delegate) || (filter(name, true))) {
/*  66 */       Class<?> result = loadFromParent(name);
/*  67 */       return result == null ? findClassIgnoringNotFound(name) : result;
/*     */     }
/*  69 */     Class<?> result = findClassIgnoringNotFound(name);
/*  70 */     return result == null ? loadFromParent(name) : result;
/*     */   }
/*     */   
/*     */   private Class<?> resolveIfNecessary(Class<?> resultClass, boolean resolve) {
/*  74 */     if (resolve) {
/*  75 */       resolveClass(resultClass);
/*     */     }
/*  77 */     return resultClass;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void addURL(URL url)
/*     */   {
/*  83 */     if (logger.isTraceEnabled()) {
/*  84 */       logger.trace("Ignoring request to add " + url + " to the tomcat classloader");
/*     */     }
/*     */   }
/*     */   
/*     */   private Class<?> loadFromParent(String name) {
/*  89 */     if (this.parent == null) {
/*  90 */       return null;
/*     */     }
/*     */     try {
/*  93 */       return Class.forName(name, false, this.parent);
/*     */     }
/*     */     catch (ClassNotFoundException ex) {}
/*  96 */     return null;
/*     */   }
/*     */   
/*     */   private Class<?> findClassIgnoringNotFound(String name)
/*     */   {
/*     */     try {
/* 102 */       return findClass(name);
/*     */     }
/*     */     catch (ClassNotFoundException ex) {}
/* 105 */     return null;
/*     */   }
/*     */   
/*     */   private void checkPackageAccess(String name) throws ClassNotFoundException
/*     */   {
/* 110 */     if ((this.securityManager != null) && (name.lastIndexOf('.') >= 0)) {
/*     */       try
/*     */       {
/* 113 */         this.securityManager.checkPackageAccess(name.substring(0, name.lastIndexOf('.')));
/*     */       }
/*     */       catch (SecurityException ex) {
/* 116 */         throw new ClassNotFoundException("Security Violation, attempt to use Restricted Class: " + name, ex);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\tomcat\TomcatEmbeddedWebappClassLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */