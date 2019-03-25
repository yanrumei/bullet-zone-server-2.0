/*      */ package org.apache.catalina.loader;
/*      */ 
/*      */ import java.beans.Introspector;
/*      */ import java.io.File;
/*      */ import java.io.FilePermission;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.lang.instrument.ClassFileTransformer;
/*      */ import java.lang.instrument.IllegalClassFormatException;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Method;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.net.URL;
/*      */ import java.net.URLClassLoader;
/*      */ import java.security.AccessControlException;
/*      */ import java.security.AccessController;
/*      */ import java.security.AllPermission;
/*      */ import java.security.CodeSource;
/*      */ import java.security.Permission;
/*      */ import java.security.PermissionCollection;
/*      */ import java.security.Policy;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.ProtectionDomain;
/*      */ import java.security.cert.Certificate;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.ConcurrentModificationException;
/*      */ import java.util.Date;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.CopyOnWriteArrayList;
/*      */ import java.util.concurrent.ThreadPoolExecutor;
/*      */ import java.util.jar.Attributes;
/*      */ import java.util.jar.Attributes.Name;
/*      */ import java.util.jar.Manifest;
/*      */ import org.apache.catalina.Container;
/*      */ import org.apache.catalina.Context;
/*      */ import org.apache.catalina.Globals;
/*      */ import org.apache.catalina.Lifecycle;
/*      */ import org.apache.catalina.LifecycleException;
/*      */ import org.apache.catalina.LifecycleListener;
/*      */ import org.apache.catalina.LifecycleState;
/*      */ import org.apache.catalina.WebResource;
/*      */ import org.apache.catalina.WebResourceRoot;
/*      */ import org.apache.catalina.webresources.TomcatURLStreamHandlerFactory;
/*      */ import org.apache.juli.WebappProperties;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.tomcat.InstrumentableClassLoader;
/*      */ import org.apache.tomcat.util.ExceptionUtils;
/*      */ import org.apache.tomcat.util.IntrospectionUtils;
/*      */ import org.apache.tomcat.util.compat.JreCompat;
/*      */ import org.apache.tomcat.util.res.StringManager;
/*      */ import org.apache.tomcat.util.security.PermissionCheck;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class WebappClassLoaderBase
/*      */   extends URLClassLoader
/*      */   implements Lifecycle, InstrumentableClassLoader, WebappProperties, PermissionCheck
/*      */ {
/*  126 */   private static final Log log = LogFactory.getLog(WebappClassLoaderBase.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  132 */   private static final List<String> JVM_THREAD_GROUP_NAMES = new ArrayList();
/*      */   
/*      */   private static final String JVM_THREAD_GROUP_SYSTEM = "system";
/*      */   private static final String CLASS_FILE_SUFFIX = ".class";
/*      */   
/*      */   static
/*      */   {
/*  139 */     ClassLoader.registerAsParallelCapable();
/*  140 */     JVM_THREAD_GROUP_NAMES.add("system");
/*  141 */     JVM_THREAD_GROUP_NAMES.add("RMI Runtime");
/*      */   }
/*      */   
/*      */   protected class PrivilegedFindClassByName implements PrivilegedAction<Class<?>>
/*      */   {
/*      */     protected final String name;
/*      */     
/*      */     PrivilegedFindClassByName(String name)
/*      */     {
/*  150 */       this.name = name;
/*      */     }
/*      */     
/*      */     public Class<?> run()
/*      */     {
/*  155 */       return WebappClassLoaderBase.this.findClassInternal(this.name);
/*      */     }
/*      */   }
/*      */   
/*      */   protected static final class PrivilegedGetClassLoader
/*      */     implements PrivilegedAction<ClassLoader>
/*      */   {
/*      */     public final Class<?> clazz;
/*      */     
/*      */     public PrivilegedGetClassLoader(Class<?> clazz)
/*      */     {
/*  166 */       this.clazz = clazz;
/*      */     }
/*      */     
/*      */     public ClassLoader run()
/*      */     {
/*  171 */       return this.clazz.getClassLoader();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  182 */   protected static final StringManager sm = StringManager.getManager("org.apache.catalina.loader");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected WebappClassLoaderBase()
/*      */   {
/*  193 */     super(new URL[0]);
/*      */     
/*  195 */     ClassLoader p = getParent();
/*  196 */     if (p == null) {
/*  197 */       p = getSystemClassLoader();
/*      */     }
/*  199 */     this.parent = p;
/*      */     
/*  201 */     ClassLoader j = String.class.getClassLoader();
/*  202 */     if (j == null) {
/*  203 */       j = getSystemClassLoader();
/*  204 */       while (j.getParent() != null) {
/*  205 */         j = j.getParent();
/*      */       }
/*      */     }
/*  208 */     this.javaseClassLoader = j;
/*      */     
/*  210 */     this.securityManager = System.getSecurityManager();
/*  211 */     if (this.securityManager != null) {
/*  212 */       refreshPolicy();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected WebappClassLoaderBase(ClassLoader parent)
/*      */   {
/*  228 */     super(new URL[0], parent);
/*      */     
/*  230 */     ClassLoader p = getParent();
/*  231 */     if (p == null) {
/*  232 */       p = getSystemClassLoader();
/*      */     }
/*  234 */     this.parent = p;
/*      */     
/*  236 */     ClassLoader j = String.class.getClassLoader();
/*  237 */     if (j == null) {
/*  238 */       j = getSystemClassLoader();
/*  239 */       while (j.getParent() != null) {
/*  240 */         j = j.getParent();
/*      */       }
/*      */     }
/*  243 */     this.javaseClassLoader = j;
/*      */     
/*  245 */     this.securityManager = System.getSecurityManager();
/*  246 */     if (this.securityManager != null) {
/*  247 */       refreshPolicy();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  257 */   protected WebResourceRoot resources = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  267 */   protected final Map<String, ResourceEntry> resourceEntries = new ConcurrentHashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  280 */   protected boolean delegate = false;
/*      */   
/*      */ 
/*  283 */   private final HashMap<String, Long> jarModificationTimes = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  290 */   protected final ArrayList<Permission> permissionList = new ArrayList();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  297 */   protected final HashMap<String, PermissionCollection> loaderPC = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final SecurityManager securityManager;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final ClassLoader parent;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private ClassLoader javaseClassLoader;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*  325 */   protected final Permission allPermission = new AllPermission();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  334 */   private boolean clearReferencesRmiTargets = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  345 */   private boolean clearReferencesStopThreads = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  352 */   private boolean clearReferencesStopTimerThreads = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  360 */   private boolean clearReferencesLogFactoryRelease = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  370 */   private boolean clearReferencesHttpClientKeepAliveThread = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  379 */   private final List<ClassFileTransformer> transformers = new CopyOnWriteArrayList();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  387 */   private boolean hasExternalRepositories = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  393 */   private List<URL> localRepositories = new ArrayList();
/*      */   
/*      */ 
/*  396 */   private volatile LifecycleState state = LifecycleState.NEW;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public WebResourceRoot getResources()
/*      */   {
/*  405 */     return this.resources;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setResources(WebResourceRoot resources)
/*      */   {
/*  415 */     this.resources = resources;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getContextName()
/*      */   {
/*  423 */     if (this.resources == null) {
/*  424 */       return "Unknown";
/*      */     }
/*  426 */     return this.resources.getContext().getBaseName();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getDelegate()
/*      */   {
/*  438 */     return this.delegate;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDelegate(boolean delegate)
/*      */   {
/*  457 */     this.delegate = delegate;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void addPermission(URL url)
/*      */   {
/*  468 */     if (url == null) {
/*  469 */       return;
/*      */     }
/*  471 */     if (this.securityManager != null) {
/*  472 */       String protocol = url.getProtocol();
/*  473 */       if ("file".equalsIgnoreCase(protocol))
/*      */       {
/*      */ 
/*      */         try
/*      */         {
/*  478 */           URI uri = url.toURI();
/*  479 */           File f = new File(uri);
/*  480 */           path = f.getCanonicalPath();
/*      */         } catch (IOException|URISyntaxException e) { String path;
/*  482 */           log.warn(sm.getString("webappClassLoader.addPermisionNoCanonicalFile", new Object[] {url
/*      */           
/*  484 */             .toExternalForm() })); return; }
/*      */         String path;
/*      */         File f;
/*  487 */         URI uri; if (f.isFile())
/*      */         {
/*  489 */           addPermission(new FilePermission(path, "read"));
/*  490 */         } else if (f.isDirectory()) {
/*  491 */           addPermission(new FilePermission(path, "read"));
/*  492 */           addPermission(new FilePermission(path + File.separator + "-", "read"));
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  499 */         log.warn(sm.getString("webappClassLoader.addPermisionNoProtocol", new Object[] { protocol, url
/*      */         
/*  501 */           .toExternalForm() }));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void addPermission(Permission permission)
/*      */   {
/*  513 */     if ((this.securityManager != null) && (permission != null)) {
/*  514 */       this.permissionList.add(permission);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean getClearReferencesRmiTargets()
/*      */   {
/*  520 */     return this.clearReferencesRmiTargets;
/*      */   }
/*      */   
/*      */   public void setClearReferencesRmiTargets(boolean clearReferencesRmiTargets)
/*      */   {
/*  525 */     this.clearReferencesRmiTargets = clearReferencesRmiTargets;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getClearReferencesStopThreads()
/*      */   {
/*  533 */     return this.clearReferencesStopThreads;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setClearReferencesStopThreads(boolean clearReferencesStopThreads)
/*      */   {
/*  544 */     this.clearReferencesStopThreads = clearReferencesStopThreads;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getClearReferencesStopTimerThreads()
/*      */   {
/*  552 */     return this.clearReferencesStopTimerThreads;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setClearReferencesStopTimerThreads(boolean clearReferencesStopTimerThreads)
/*      */   {
/*  563 */     this.clearReferencesStopTimerThreads = clearReferencesStopTimerThreads;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getClearReferencesLogFactoryRelease()
/*      */   {
/*  571 */     return this.clearReferencesLogFactoryRelease;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setClearReferencesLogFactoryRelease(boolean clearReferencesLogFactoryRelease)
/*      */   {
/*  582 */     this.clearReferencesLogFactoryRelease = clearReferencesLogFactoryRelease;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getClearReferencesHttpClientKeepAliveThread()
/*      */   {
/*  592 */     return this.clearReferencesHttpClientKeepAliveThread;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setClearReferencesHttpClientKeepAliveThread(boolean clearReferencesHttpClientKeepAliveThread)
/*      */   {
/*  604 */     this.clearReferencesHttpClientKeepAliveThread = clearReferencesHttpClientKeepAliveThread;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addTransformer(ClassFileTransformer transformer)
/*      */   {
/*  621 */     if (transformer == null) {
/*  622 */       throw new IllegalArgumentException(sm.getString("webappClassLoader.addTransformer.illegalArgument", new Object[] {
/*  623 */         getContextName() }));
/*      */     }
/*      */     
/*  626 */     if (this.transformers.contains(transformer))
/*      */     {
/*  628 */       log.warn(sm.getString("webappClassLoader.addTransformer.duplicate", new Object[] { transformer, 
/*  629 */         getContextName() }));
/*  630 */       return;
/*      */     }
/*  632 */     this.transformers.add(transformer);
/*      */     
/*  634 */     log.info(sm.getString("webappClassLoader.addTransformer", new Object[] { transformer, getContextName() }));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeTransformer(ClassFileTransformer transformer)
/*      */   {
/*  649 */     if (transformer == null) {
/*  650 */       return;
/*      */     }
/*      */     
/*  653 */     if (this.transformers.remove(transformer)) {
/*  654 */       log.info(sm.getString("webappClassLoader.removeTransformer", new Object[] { transformer, 
/*  655 */         getContextName() }));
/*  656 */       return;
/*      */     }
/*      */   }
/*      */   
/*      */   protected void copyStateWithoutTransformers(WebappClassLoaderBase base)
/*      */   {
/*  662 */     base.resources = this.resources;
/*  663 */     base.delegate = this.delegate;
/*  664 */     base.state = LifecycleState.NEW;
/*  665 */     base.clearReferencesStopThreads = this.clearReferencesStopThreads;
/*  666 */     base.clearReferencesStopTimerThreads = this.clearReferencesStopTimerThreads;
/*  667 */     base.clearReferencesLogFactoryRelease = this.clearReferencesLogFactoryRelease;
/*  668 */     base.clearReferencesHttpClientKeepAliveThread = this.clearReferencesHttpClientKeepAliveThread;
/*  669 */     base.jarModificationTimes.putAll(this.jarModificationTimes);
/*  670 */     base.permissionList.addAll(this.permissionList);
/*  671 */     base.loaderPC.putAll(this.loaderPC);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean modified()
/*      */   {
/*  681 */     if (log.isDebugEnabled()) {
/*  682 */       log.debug("modified()");
/*      */     }
/*  684 */     for (Map.Entry<String, ResourceEntry> entry : this.resourceEntries.entrySet()) {
/*  685 */       cachedLastModified = ((ResourceEntry)entry.getValue()).lastModified;
/*      */       
/*  687 */       lastModified = this.resources.getClassLoaderResource((String)entry.getKey()).getLastModified();
/*  688 */       if (lastModified != cachedLastModified) {
/*  689 */         if (log.isDebugEnabled()) {
/*  690 */           log.debug(sm.getString("webappClassLoader.resourceModified", new Object[] {entry
/*  691 */             .getKey(), new Date(cachedLastModified), new Date(lastModified) }));
/*      */         }
/*      */         
/*  694 */         return true;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  699 */     WebResource[] jars = this.resources.listResources("/WEB-INF/lib");
/*      */     
/*      */ 
/*  702 */     int jarCount = 0;
/*  703 */     long cachedLastModified = jars;long l1 = cachedLastModified.length; for (long lastModified = 0; lastModified < l1; lastModified++) { WebResource jar = cachedLastModified[lastModified];
/*  704 */       if ((jar.getName().endsWith(".jar")) && (jar.isFile()) && (jar.canRead())) {
/*  705 */         jarCount++;
/*  706 */         Long recordedLastModified = (Long)this.jarModificationTimes.get(jar.getName());
/*  707 */         if (recordedLastModified == null)
/*      */         {
/*  709 */           log.info(sm.getString("webappClassLoader.jarsAdded", new Object[] {this.resources
/*  710 */             .getContext().getName() }));
/*  711 */           return true;
/*      */         }
/*  713 */         if (recordedLastModified.longValue() != jar.getLastModified())
/*      */         {
/*  715 */           log.info(sm.getString("webappClassLoader.jarsModified", new Object[] {this.resources
/*  716 */             .getContext().getName() }));
/*  717 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  722 */     if (jarCount < this.jarModificationTimes.size()) {
/*  723 */       log.info(sm.getString("webappClassLoader.jarsRemoved", new Object[] {this.resources
/*  724 */         .getContext().getName() }));
/*  725 */       return true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  730 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/*  737 */     StringBuilder sb = new StringBuilder(getClass().getSimpleName());
/*  738 */     sb.append("\r\n  context: ");
/*  739 */     sb.append(getContextName());
/*  740 */     sb.append("\r\n  delegate: ");
/*  741 */     sb.append(this.delegate);
/*  742 */     sb.append("\r\n");
/*  743 */     if (this.parent != null) {
/*  744 */       sb.append("----------> Parent Classloader:\r\n");
/*  745 */       sb.append(this.parent.toString());
/*  746 */       sb.append("\r\n");
/*      */     }
/*  748 */     if (this.transformers.size() > 0) {
/*  749 */       sb.append("----------> Class file transformers:\r\n");
/*  750 */       for (ClassFileTransformer transformer : this.transformers) {
/*  751 */         sb.append(transformer).append("\r\n");
/*      */       }
/*      */     }
/*  754 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final Class<?> doDefineClass(String name, byte[] b, int off, int len, ProtectionDomain protectionDomain)
/*      */   {
/*  764 */     return super.defineClass(name, b, off, len, protectionDomain);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Class<?> findClass(String name)
/*      */     throws ClassNotFoundException
/*      */   {
/*  778 */     if (log.isDebugEnabled()) {
/*  779 */       log.debug("    findClass(" + name + ")");
/*      */     }
/*  781 */     checkStateForClassLoading(name);
/*      */     
/*      */ 
/*  784 */     if (this.securityManager != null) {
/*  785 */       int i = name.lastIndexOf('.');
/*  786 */       if (i >= 0) {
/*      */         try {
/*  788 */           if (log.isTraceEnabled())
/*  789 */             log.trace("      securityManager.checkPackageDefinition");
/*  790 */           this.securityManager.checkPackageDefinition(name.substring(0, i));
/*      */         } catch (Exception se) {
/*  792 */           if (log.isTraceEnabled())
/*  793 */             log.trace("      -->Exception-->ClassNotFoundException", se);
/*  794 */           throw new ClassNotFoundException(name, se);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  801 */     Class<?> clazz = null;
/*      */     try {
/*  803 */       if (log.isTraceEnabled())
/*  804 */         log.trace("      findClassInternal(" + name + ")");
/*      */       try {
/*  806 */         if (this.securityManager != null) {
/*  807 */           PrivilegedAction<Class<?>> dp = new PrivilegedFindClassByName(name);
/*      */           
/*  809 */           clazz = (Class)AccessController.doPrivileged(dp);
/*      */         } else {
/*  811 */           clazz = findClassInternal(name);
/*      */         }
/*      */       } catch (AccessControlException ace) {
/*  814 */         log.warn("WebappClassLoader.findClassInternal(" + name + ") security exception: " + ace
/*  815 */           .getMessage(), ace);
/*  816 */         throw new ClassNotFoundException(name, ace);
/*      */       } catch (RuntimeException e) {
/*  818 */         if (log.isTraceEnabled())
/*  819 */           log.trace("      -->RuntimeException Rethrown", e);
/*  820 */         throw e;
/*      */       }
/*  822 */       if ((clazz == null) && (this.hasExternalRepositories)) {
/*      */         try {
/*  824 */           clazz = super.findClass(name);
/*      */         } catch (AccessControlException ace) {
/*  826 */           log.warn("WebappClassLoader.findClassInternal(" + name + ") security exception: " + ace
/*  827 */             .getMessage(), ace);
/*  828 */           throw new ClassNotFoundException(name, ace);
/*      */         } catch (RuntimeException e) {
/*  830 */           if (log.isTraceEnabled())
/*  831 */             log.trace("      -->RuntimeException Rethrown", e);
/*  832 */           throw e;
/*      */         }
/*      */       }
/*  835 */       if (clazz == null) {
/*  836 */         if (log.isDebugEnabled())
/*  837 */           log.debug("    --> Returning ClassNotFoundException");
/*  838 */         throw new ClassNotFoundException(name);
/*      */       }
/*      */     } catch (ClassNotFoundException e) {
/*  841 */       if (log.isTraceEnabled())
/*  842 */         log.trace("    --> Passing on ClassNotFoundException");
/*  843 */       throw e;
/*      */     }
/*      */     
/*      */ 
/*  847 */     if (log.isTraceEnabled()) {
/*  848 */       log.debug("      Returning class " + clazz);
/*      */     }
/*  850 */     if (log.isTraceEnabled()) { ClassLoader cl;
/*      */       ClassLoader cl;
/*  852 */       if (Globals.IS_SECURITY_ENABLED) {
/*  853 */         cl = (ClassLoader)AccessController.doPrivileged(new PrivilegedGetClassLoader(clazz));
/*      */       }
/*      */       else {
/*  856 */         cl = clazz.getClassLoader();
/*      */       }
/*  858 */       log.debug("      Loaded by " + cl.toString());
/*      */     }
/*  860 */     return clazz;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public URL findResource(String name)
/*      */   {
/*  875 */     if (log.isDebugEnabled()) {
/*  876 */       log.debug("    findResource(" + name + ")");
/*      */     }
/*  878 */     checkStateForResourceLoading(name);
/*      */     
/*  880 */     URL url = null;
/*      */     
/*  882 */     String path = nameToPath(name);
/*      */     
/*  884 */     WebResource resource = this.resources.getClassLoaderResource(path);
/*  885 */     if (resource.exists()) {
/*  886 */       url = resource.getURL();
/*  887 */       trackLastModified(path, resource);
/*      */     }
/*      */     
/*  890 */     if ((url == null) && (this.hasExternalRepositories)) {
/*  891 */       url = super.findResource(name);
/*      */     }
/*      */     
/*  894 */     if (log.isDebugEnabled()) {
/*  895 */       if (url != null) {
/*  896 */         log.debug("    --> Returning '" + url.toString() + "'");
/*      */       } else
/*  898 */         log.debug("    --> Resource not found, returning null");
/*      */     }
/*  900 */     return url;
/*      */   }
/*      */   
/*      */   private void trackLastModified(String path, WebResource resource)
/*      */   {
/*  905 */     if (this.resourceEntries.containsKey(path)) {
/*  906 */       return;
/*      */     }
/*  908 */     ResourceEntry entry = new ResourceEntry();
/*  909 */     entry.lastModified = resource.getLastModified();
/*  910 */     synchronized (this.resourceEntries) {
/*  911 */       if (!this.resourceEntries.containsKey(path)) {
/*  912 */         this.resourceEntries.put(path, entry);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Enumeration<URL> findResources(String name)
/*      */     throws IOException
/*      */   {
/*  930 */     if (log.isDebugEnabled()) {
/*  931 */       log.debug("    findResources(" + name + ")");
/*      */     }
/*  933 */     checkStateForResourceLoading(name);
/*      */     
/*  935 */     LinkedHashSet<URL> result = new LinkedHashSet();
/*      */     
/*  937 */     String path = nameToPath(name);
/*      */     
/*  939 */     WebResource[] webResources = this.resources.getClassLoaderResources(path);
/*  940 */     for (WebResource webResource : webResources) {
/*  941 */       if (webResource.exists()) {
/*  942 */         result.add(webResource.getURL());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  947 */     if (this.hasExternalRepositories) {
/*  948 */       Object otherResourcePaths = super.findResources(name);
/*  949 */       while (((Enumeration)otherResourcePaths).hasMoreElements()) {
/*  950 */         result.add(((Enumeration)otherResourcePaths).nextElement());
/*      */       }
/*      */     }
/*      */     
/*  954 */     return Collections.enumeration(result);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public URL getResource(String name)
/*      */   {
/*  983 */     if (log.isDebugEnabled()) {
/*  984 */       log.debug("getResource(" + name + ")");
/*      */     }
/*  986 */     checkStateForResourceLoading(name);
/*      */     
/*  988 */     URL url = null;
/*      */     
/*  990 */     boolean delegateFirst = (this.delegate) || (filter(name, false));
/*      */     
/*      */ 
/*  993 */     if (delegateFirst) {
/*  994 */       if (log.isDebugEnabled())
/*  995 */         log.debug("  Delegating to parent classloader " + this.parent);
/*  996 */       url = this.parent.getResource(name);
/*  997 */       if (url != null) {
/*  998 */         if (log.isDebugEnabled())
/*  999 */           log.debug("  --> Returning '" + url.toString() + "'");
/* 1000 */         return url;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1005 */     url = findResource(name);
/* 1006 */     if (url != null) {
/* 1007 */       if (log.isDebugEnabled())
/* 1008 */         log.debug("  --> Returning '" + url.toString() + "'");
/* 1009 */       return url;
/*      */     }
/*      */     
/*      */ 
/* 1013 */     if (!delegateFirst) {
/* 1014 */       url = this.parent.getResource(name);
/* 1015 */       if (url != null) {
/* 1016 */         if (log.isDebugEnabled())
/* 1017 */           log.debug("  --> Returning '" + url.toString() + "'");
/* 1018 */         return url;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1023 */     if (log.isDebugEnabled())
/* 1024 */       log.debug("  --> Resource not found, returning null");
/* 1025 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public InputStream getResourceAsStream(String name)
/*      */   {
/* 1042 */     if (log.isDebugEnabled()) {
/* 1043 */       log.debug("getResourceAsStream(" + name + ")");
/*      */     }
/* 1045 */     checkStateForResourceLoading(name);
/*      */     
/* 1047 */     InputStream stream = null;
/*      */     
/* 1049 */     boolean delegateFirst = (this.delegate) || (filter(name, false));
/*      */     
/*      */ 
/* 1052 */     if (delegateFirst) {
/* 1053 */       if (log.isDebugEnabled())
/* 1054 */         log.debug("  Delegating to parent classloader " + this.parent);
/* 1055 */       stream = this.parent.getResourceAsStream(name);
/* 1056 */       if (stream != null) {
/* 1057 */         if (log.isDebugEnabled())
/* 1058 */           log.debug("  --> Returning stream from parent");
/* 1059 */         return stream;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1064 */     if (log.isDebugEnabled())
/* 1065 */       log.debug("  Searching local repositories");
/* 1066 */     String path = nameToPath(name);
/* 1067 */     WebResource resource = this.resources.getClassLoaderResource(path);
/* 1068 */     if (resource.exists()) {
/* 1069 */       stream = resource.getInputStream();
/* 1070 */       trackLastModified(path, resource);
/*      */     }
/*      */     try {
/* 1073 */       if ((this.hasExternalRepositories) && (stream == null)) {
/* 1074 */         URL url = super.findResource(name);
/* 1075 */         if (url != null) {
/* 1076 */           stream = url.openStream();
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (IOException localIOException) {}
/*      */     
/* 1082 */     if (stream != null) {
/* 1083 */       if (log.isDebugEnabled())
/* 1084 */         log.debug("  --> Returning stream from local");
/* 1085 */       return stream;
/*      */     }
/*      */     
/*      */ 
/* 1089 */     if (!delegateFirst) {
/* 1090 */       if (log.isDebugEnabled())
/* 1091 */         log.debug("  Delegating to parent classloader unconditionally " + this.parent);
/* 1092 */       stream = this.parent.getResourceAsStream(name);
/* 1093 */       if (stream != null) {
/* 1094 */         if (log.isDebugEnabled())
/* 1095 */           log.debug("  --> Returning stream from parent");
/* 1096 */         return stream;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1101 */     if (log.isDebugEnabled())
/* 1102 */       log.debug("  --> Resource not found, returning null");
/* 1103 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Class<?> loadClass(String name)
/*      */     throws ClassNotFoundException
/*      */   {
/* 1119 */     return loadClass(name, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Class<?> loadClass(String name, boolean resolve)
/*      */     throws ClassNotFoundException
/*      */   {
/* 1152 */     synchronized (getClassLoadingLock(name)) {
/* 1153 */       if (log.isDebugEnabled())
/* 1154 */         log.debug("loadClass(" + name + ", " + resolve + ")");
/* 1155 */       Class<?> clazz = null;
/*      */       
/*      */ 
/* 1158 */       checkStateForClassLoading(name);
/*      */       
/*      */ 
/* 1161 */       clazz = findLoadedClass0(name);
/* 1162 */       if (clazz != null) {
/* 1163 */         if (log.isDebugEnabled())
/* 1164 */           log.debug("  Returning class from cache");
/* 1165 */         if (resolve)
/* 1166 */           resolveClass(clazz);
/* 1167 */         return clazz;
/*      */       }
/*      */       
/*      */ 
/* 1171 */       clazz = findLoadedClass(name);
/* 1172 */       if (clazz != null) {
/* 1173 */         if (log.isDebugEnabled())
/* 1174 */           log.debug("  Returning class from cache");
/* 1175 */         if (resolve)
/* 1176 */           resolveClass(clazz);
/* 1177 */         return clazz;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1183 */       String resourceName = binaryNameToPath(name, false);
/*      */       
/* 1185 */       ClassLoader javaseLoader = getJavaseClassLoader();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       boolean tryLoadingFromJavaseLoader;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       try
/*      */       {
/* 1198 */         tryLoadingFromJavaseLoader = javaseLoader.getResource(resourceName) != null;
/*      */       } catch (Throwable t) {
/*      */         boolean tryLoadingFromJavaseLoader;
/* 1201 */         ExceptionUtils.handleThrowable(t);
/*      */         
/*      */ 
/*      */ 
/* 1205 */         tryLoadingFromJavaseLoader = true;
/*      */       }
/*      */       
/* 1208 */       if (tryLoadingFromJavaseLoader) {
/*      */         try {
/* 1210 */           clazz = javaseLoader.loadClass(name);
/* 1211 */           if (clazz != null) {
/* 1212 */             if (resolve)
/* 1213 */               resolveClass(clazz);
/* 1214 */             return clazz;
/*      */           }
/*      */         }
/*      */         catch (ClassNotFoundException localClassNotFoundException) {}
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1222 */       if (this.securityManager != null) {
/* 1223 */         int i = name.lastIndexOf('.');
/* 1224 */         if (i >= 0) {
/*      */           try {
/* 1226 */             this.securityManager.checkPackageAccess(name.substring(0, i));
/*      */           } catch (SecurityException se) {
/* 1228 */             String error = "Security Violation, attempt to use Restricted Class: " + name;
/*      */             
/* 1230 */             log.info(error, se);
/* 1231 */             throw new ClassNotFoundException(error, se);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1236 */       boolean delegateLoad = (this.delegate) || (filter(name, true));
/*      */       
/*      */ 
/* 1239 */       if (delegateLoad) {
/* 1240 */         if (log.isDebugEnabled())
/* 1241 */           log.debug("  Delegating to parent classloader1 " + this.parent);
/*      */         try {
/* 1243 */           clazz = Class.forName(name, false, this.parent);
/* 1244 */           if (clazz != null) {
/* 1245 */             if (log.isDebugEnabled())
/* 1246 */               log.debug("  Loading class from parent");
/* 1247 */             if (resolve)
/* 1248 */               resolveClass(clazz);
/* 1249 */             return clazz;
/*      */           }
/*      */         }
/*      */         catch (ClassNotFoundException localClassNotFoundException1) {}
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1257 */       if (log.isDebugEnabled())
/* 1258 */         log.debug("  Searching local repositories");
/*      */       try {
/* 1260 */         clazz = findClass(name);
/* 1261 */         if (clazz != null) {
/* 1262 */           if (log.isDebugEnabled())
/* 1263 */             log.debug("  Loading class from local repository");
/* 1264 */           if (resolve)
/* 1265 */             resolveClass(clazz);
/* 1266 */           return clazz;
/*      */         }
/*      */       }
/*      */       catch (ClassNotFoundException localClassNotFoundException2) {}
/*      */       
/*      */ 
/*      */ 
/* 1273 */       if (!delegateLoad) {
/* 1274 */         if (log.isDebugEnabled())
/* 1275 */           log.debug("  Delegating to parent classloader at end: " + this.parent);
/*      */         try {
/* 1277 */           clazz = Class.forName(name, false, this.parent);
/* 1278 */           if (clazz != null) {
/* 1279 */             if (log.isDebugEnabled())
/* 1280 */               log.debug("  Loading class from parent");
/* 1281 */             if (resolve)
/* 1282 */               resolveClass(clazz);
/* 1283 */             return clazz;
/*      */           }
/*      */         }
/*      */         catch (ClassNotFoundException localClassNotFoundException3) {}
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1291 */     throw new ClassNotFoundException(name);
/*      */   }
/*      */   
/*      */   protected void checkStateForClassLoading(String className)
/*      */     throws ClassNotFoundException
/*      */   {
/*      */     try
/*      */     {
/* 1299 */       checkStateForResourceLoading(className);
/*      */     } catch (IllegalStateException ise) {
/* 1301 */       throw new ClassNotFoundException(ise.getMessage(), ise);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected void checkStateForResourceLoading(String resource)
/*      */     throws IllegalStateException
/*      */   {
/* 1309 */     if (!this.state.isAvailable()) {
/* 1310 */       String msg = sm.getString("webappClassLoader.stopped", new Object[] { resource });
/* 1311 */       IllegalStateException ise = new IllegalStateException(msg);
/* 1312 */       log.info(msg, ise);
/* 1313 */       throw ise;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected PermissionCollection getPermissions(CodeSource codeSource)
/*      */   {
/* 1328 */     String codeUrl = codeSource.getLocation().toString();
/*      */     PermissionCollection pc;
/* 1330 */     if ((pc = (PermissionCollection)this.loaderPC.get(codeUrl)) == null) {
/* 1331 */       pc = super.getPermissions(codeSource);
/* 1332 */       if (pc != null) {
/* 1333 */         Iterator<Permission> perms = this.permissionList.iterator();
/* 1334 */         while (perms.hasNext()) {
/* 1335 */           Permission p = (Permission)perms.next();
/* 1336 */           pc.add(p);
/*      */         }
/* 1338 */         this.loaderPC.put(codeUrl, pc);
/*      */       }
/*      */     }
/* 1341 */     return pc;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean check(Permission permission)
/*      */   {
/* 1348 */     if (!Globals.IS_SECURITY_ENABLED) {
/* 1349 */       return true;
/*      */     }
/* 1351 */     Policy currentPolicy = Policy.getPolicy();
/* 1352 */     if (currentPolicy != null) {
/* 1353 */       URL contextRootUrl = this.resources.getResource("/").getCodeBase();
/* 1354 */       CodeSource cs = new CodeSource(contextRootUrl, (Certificate[])null);
/* 1355 */       PermissionCollection pc = currentPolicy.getPermissions(cs);
/* 1356 */       if (pc.implies(permission)) {
/* 1357 */         return true;
/*      */       }
/*      */     }
/* 1360 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public URL[] getURLs()
/*      */   {
/* 1376 */     ArrayList<URL> result = new ArrayList();
/* 1377 */     result.addAll(this.localRepositories);
/* 1378 */     result.addAll(Arrays.asList(super.getURLs()));
/* 1379 */     return (URL[])result.toArray(new URL[result.size()]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public LifecycleListener[] findLifecycleListeners()
/*      */   {
/* 1403 */     return new LifecycleListener[0];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public LifecycleState getState()
/*      */   {
/* 1425 */     return this.state;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getStateName()
/*      */   {
/* 1434 */     return getState().toString();
/*      */   }
/*      */   
/*      */ 
/*      */   public void init()
/*      */   {
/* 1440 */     this.state = LifecycleState.INITIALIZED;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void start()
/*      */     throws LifecycleException
/*      */   {
/* 1452 */     this.state = LifecycleState.STARTING_PREP;
/*      */     
/* 1454 */     WebResource classes = this.resources.getResource("/WEB-INF/classes");
/* 1455 */     if ((classes.isDirectory()) && (classes.canRead())) {
/* 1456 */       this.localRepositories.add(classes.getURL());
/*      */     }
/* 1458 */     WebResource[] jars = this.resources.listResources("/WEB-INF/lib");
/* 1459 */     for (WebResource jar : jars) {
/* 1460 */       if ((jar.getName().endsWith(".jar")) && (jar.isFile()) && (jar.canRead())) {
/* 1461 */         this.localRepositories.add(jar.getURL());
/* 1462 */         this.jarModificationTimes.put(jar
/* 1463 */           .getName(), Long.valueOf(jar.getLastModified()));
/*      */       }
/*      */     }
/*      */     
/* 1467 */     this.state = LifecycleState.STARTED;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void stop()
/*      */     throws LifecycleException
/*      */   {
/* 1479 */     this.state = LifecycleState.STOPPING_PREP;
/*      */     
/*      */ 
/*      */ 
/* 1483 */     clearReferences();
/*      */     
/* 1485 */     this.state = LifecycleState.STOPPING;
/*      */     
/* 1487 */     this.resourceEntries.clear();
/* 1488 */     this.jarModificationTimes.clear();
/* 1489 */     this.resources = null;
/*      */     
/* 1491 */     this.permissionList.clear();
/* 1492 */     this.loaderPC.clear();
/*      */     
/* 1494 */     this.state = LifecycleState.STOPPED;
/*      */   }
/*      */   
/*      */ 
/*      */   public void destroy()
/*      */   {
/* 1500 */     this.state = LifecycleState.DESTROYING;
/*      */     try
/*      */     {
/* 1503 */       super.close();
/*      */     } catch (IOException ioe) {
/* 1505 */       log.warn(sm.getString("webappClassLoader.superCloseFail"), ioe);
/*      */     }
/* 1507 */     this.state = LifecycleState.DESTROYED;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected ClassLoader getJavaseClassLoader()
/*      */   {
/* 1514 */     return this.javaseClassLoader;
/*      */   }
/*      */   
/*      */   protected void setJavaseClassLoader(ClassLoader classLoader) {
/* 1518 */     if (classLoader == null)
/*      */     {
/* 1520 */       throw new IllegalArgumentException(sm.getString("webappClassLoader.javaseClassLoaderNull"));
/*      */     }
/* 1522 */     this.javaseClassLoader = classLoader;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void clearReferences()
/*      */   {
/* 1531 */     clearReferencesJdbc();
/*      */     
/*      */ 
/* 1534 */     clearReferencesThreads();
/*      */     
/*      */ 
/* 1537 */     checkThreadLocalsForLeaks();
/*      */     
/*      */ 
/* 1540 */     if (this.clearReferencesRmiTargets) {
/* 1541 */       clearReferencesRmiTargets();
/*      */     }
/*      */     
/*      */ 
/* 1545 */     IntrospectionUtils.clear();
/*      */     
/*      */ 
/* 1548 */     if (this.clearReferencesLogFactoryRelease) {
/* 1549 */       LogFactory.release(this);
/*      */     }
/*      */     
/*      */ 
/* 1553 */     Introspector.flushCaches();
/*      */     
/*      */ 
/* 1556 */     TomcatURLStreamHandlerFactory.release(this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void clearReferencesJdbc()
/*      */   {
/* 1581 */     byte[] classBytes = new byte['ࠀ'];
/* 1582 */     int offset = 0;
/* 1583 */     try { InputStream is = getResourceAsStream("org/apache/catalina/loader/JdbcLeakPrevention.class");Throwable localThrowable4 = null;
/*      */       try {
/* 1585 */         int read = is.read(classBytes, offset, classBytes.length - offset);
/* 1586 */         while (read > -1) {
/* 1587 */           offset += read;
/* 1588 */           if (offset == classBytes.length)
/*      */           {
/* 1590 */             byte[] tmp = new byte[classBytes.length * 2];
/* 1591 */             System.arraycopy(classBytes, 0, tmp, 0, classBytes.length);
/* 1592 */             classBytes = tmp;
/*      */           }
/* 1594 */           read = is.read(classBytes, offset, classBytes.length - offset);
/*      */         }
/*      */         
/* 1597 */         Class<?> lpClass = defineClass("org.apache.catalina.loader.JdbcLeakPrevention", classBytes, 0, offset, 
/* 1598 */           getClass().getProtectionDomain());
/* 1599 */         Object obj = lpClass.getConstructor(new Class[0]).newInstance(new Object[0]);
/*      */         
/*      */ 
/* 1602 */         List<String> driverNames = (List)obj.getClass().getMethod("clearJdbcDriverRegistrations", new Class[0]).invoke(obj, new Object[0]);
/* 1603 */         for (String name : driverNames) {
/* 1604 */           log.warn(sm.getString("webappClassLoader.clearJdbc", new Object[] {
/* 1605 */             getContextName(), name }));
/*      */         }
/*      */       }
/*      */       catch (Throwable localThrowable2)
/*      */       {
/* 1583 */         localThrowable4 = localThrowable2;throw localThrowable2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/*      */       finally
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1607 */         if (is != null) if (localThrowable4 != null) try { is.close(); } catch (Throwable localThrowable3) { localThrowable4.addSuppressed(localThrowable3); } else is.close();
/*      */       }
/* 1609 */     } catch (Exception e) { Throwable t = ExceptionUtils.unwrapInvocationTargetException(e);
/* 1610 */       ExceptionUtils.handleThrowable(t);
/* 1611 */       log.warn(sm.getString("webappClassLoader.jdbcRemoveFailed", new Object[] {
/* 1612 */         getContextName() }), t);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void clearReferencesThreads()
/*      */   {
/* 1619 */     Thread[] threads = getThreads();
/* 1620 */     List<Thread> executorThreadsToStop = new ArrayList();
/*      */     
/*      */ 
/* 1623 */     for (Thread thread : threads) {
/* 1624 */       if (thread != null) {
/* 1625 */         ClassLoader ccl = thread.getContextClassLoader();
/* 1626 */         if (ccl == this)
/*      */         {
/* 1628 */           if (thread != Thread.currentThread())
/*      */           {
/*      */ 
/*      */ 
/* 1632 */             String threadName = thread.getName();
/*      */             
/*      */ 
/* 1635 */             ThreadGroup tg = thread.getThreadGroup();
/* 1636 */             if ((tg != null) && (JVM_THREAD_GROUP_NAMES.contains(tg.getName())))
/*      */             {
/* 1638 */               if ((this.clearReferencesHttpClientKeepAliveThread) && 
/* 1639 */                 (threadName.equals("Keep-Alive-Timer"))) {
/* 1640 */                 thread.setContextClassLoader(this.parent);
/* 1641 */                 log.debug(sm.getString("webappClassLoader.checkThreadsHttpClient"));
/*      */ 
/*      */ 
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*      */             }
/* 1649 */             else if (thread.isAlive())
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1656 */               if ((thread.getClass().getName().startsWith("java.util.Timer")) && (this.clearReferencesStopTimerThreads))
/*      */               {
/* 1658 */                 clearReferencesStopTimerThread(thread);
/*      */               }
/*      */               else
/*      */               {
/* 1662 */                 if (isRequestThread(thread)) {
/* 1663 */                   log.warn(sm.getString("webappClassLoader.stackTraceRequestThread", new Object[] {
/* 1664 */                     getContextName(), threadName, getStackTrace(thread) }));
/*      */                 } else {
/* 1666 */                   log.warn(sm.getString("webappClassLoader.stackTrace", new Object[] {
/* 1667 */                     getContextName(), threadName, getStackTrace(thread) }));
/*      */                 }
/*      */                 
/*      */ 
/*      */ 
/* 1672 */                 if (this.clearReferencesStopThreads)
/*      */                 {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1678 */                   boolean usingExecutor = false;
/*      */                   
/*      */ 
/*      */ 
/*      */ 
/*      */                   try
/*      */                   {
/* 1685 */                     Object target = null;
/* 1686 */                     for (String fieldName : new String[] { "target", "runnable", "action" }) {
/*      */                       try {
/* 1688 */                         Field targetField = thread.getClass().getDeclaredField(fieldName);
/* 1689 */                         targetField.setAccessible(true);
/* 1690 */                         target = targetField.get(thread);
/*      */                       }
/*      */                       catch (NoSuchFieldException nfe) {}
/*      */                     }
/*      */                     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1699 */                     if ((target != null) && (target.getClass().getCanonicalName() != null) && 
/* 1700 */                       (target.getClass().getCanonicalName().equals("java.util.concurrent.ThreadPoolExecutor.Worker")))
/*      */                     {
/* 1702 */                       Field executorField = target.getClass().getDeclaredField("this$0");
/* 1703 */                       executorField.setAccessible(true);
/* 1704 */                       Object executor = executorField.get(target);
/* 1705 */                       if ((executor instanceof ThreadPoolExecutor)) {
/* 1706 */                         ((ThreadPoolExecutor)executor).shutdownNow();
/* 1707 */                         usingExecutor = true;
/*      */                       }
/*      */                     }
/*      */                   }
/*      */                   catch (SecurityException|NoSuchFieldException|IllegalArgumentException|IllegalAccessException e) {
/* 1712 */                     log.warn(sm.getString("webappClassLoader.stopThreadFail", new Object[] {thread
/* 1713 */                       .getName(), getContextName() }), e);
/*      */                   }
/*      */                   
/* 1716 */                   if (usingExecutor)
/*      */                   {
/*      */ 
/*      */ 
/* 1720 */                     executorThreadsToStop.add(thread);
/*      */ 
/*      */                   }
/*      */                   else
/*      */                   {
/*      */ 
/* 1726 */                     thread.stop();
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1737 */     int count = 0;
/* 1738 */     for (Thread t : executorThreadsToStop) {
/* 1739 */       while ((t.isAlive()) && (count < 100)) {
/*      */         try {
/* 1741 */           Thread.sleep(20L);
/*      */         }
/*      */         catch (InterruptedException e) {
/*      */           break;
/*      */         }
/* 1746 */         count++;
/*      */       }
/* 1748 */       if (t.isAlive())
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 1753 */         t.stop();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean isRequestThread(Thread thread)
/*      */   {
/* 1765 */     StackTraceElement[] elements = thread.getStackTrace();
/*      */     
/* 1767 */     if ((elements == null) || (elements.length == 0))
/*      */     {
/*      */ 
/* 1770 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1777 */     for (int i = 0; i < elements.length; i++) {
/* 1778 */       StackTraceElement element = elements[(elements.length - (i + 1))];
/* 1779 */       if ("org.apache.catalina.connector.CoyoteAdapter".equals(element
/* 1780 */         .getClassName())) {
/* 1781 */         return true;
/*      */       }
/*      */     }
/* 1784 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void clearReferencesStopTimerThread(Thread thread)
/*      */   {
/*      */     try
/*      */     {
/*      */       try
/*      */       {
/* 1802 */         Field newTasksMayBeScheduledField = thread.getClass().getDeclaredField("newTasksMayBeScheduled");
/* 1803 */         newTasksMayBeScheduledField.setAccessible(true);
/* 1804 */         Field queueField = thread.getClass().getDeclaredField("queue");
/* 1805 */         queueField.setAccessible(true);
/*      */         
/* 1807 */         Object queue = queueField.get(thread);
/*      */         
/* 1809 */         Method clearMethod = queue.getClass().getDeclaredMethod("clear", new Class[0]);
/* 1810 */         clearMethod.setAccessible(true);
/*      */         
/* 1812 */         synchronized (queue) {
/* 1813 */           newTasksMayBeScheduledField.setBoolean(thread, false);
/* 1814 */           clearMethod.invoke(queue, new Object[0]);
/* 1815 */           queue.notify();
/*      */         }
/*      */       }
/*      */       catch (NoSuchFieldException nfe) {
/* 1819 */         Method cancelMethod = thread.getClass().getDeclaredMethod("cancel", new Class[0]);
/* 1820 */         synchronized (thread) {
/* 1821 */           cancelMethod.setAccessible(true);
/* 1822 */           cancelMethod.invoke(thread, new Object[0]);
/*      */         }
/*      */       }
/*      */       
/* 1826 */       log.warn(sm.getString("webappClassLoader.warnTimerThread", new Object[] {
/* 1827 */         getContextName(), thread.getName() }));
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/* 1831 */       Throwable t = ExceptionUtils.unwrapInvocationTargetException(e);
/* 1832 */       ExceptionUtils.handleThrowable(t);
/* 1833 */       log.warn(sm.getString("webappClassLoader.stopTimerThreadFail", new Object[] {thread
/*      */       
/* 1835 */         .getName(), getContextName() }), t);
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkThreadLocalsForLeaks() {
/* 1840 */     Thread[] threads = getThreads();
/*      */     
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/* 1846 */       Field threadLocalsField = Thread.class.getDeclaredField("threadLocals");
/* 1847 */       threadLocalsField.setAccessible(true);
/*      */       
/* 1849 */       Field inheritableThreadLocalsField = Thread.class.getDeclaredField("inheritableThreadLocals");
/* 1850 */       inheritableThreadLocalsField.setAccessible(true);
/*      */       
/*      */ 
/* 1853 */       Class<?> tlmClass = Class.forName("java.lang.ThreadLocal$ThreadLocalMap");
/* 1854 */       Field tableField = tlmClass.getDeclaredField("table");
/* 1855 */       tableField.setAccessible(true);
/* 1856 */       Method expungeStaleEntriesMethod = tlmClass.getDeclaredMethod("expungeStaleEntries", new Class[0]);
/* 1857 */       expungeStaleEntriesMethod.setAccessible(true);
/*      */       
/* 1859 */       for (int i = 0; i < threads.length; i++)
/*      */       {
/* 1861 */         if (threads[i] != null)
/*      */         {
/*      */ 
/* 1864 */           Object threadLocalMap = threadLocalsField.get(threads[i]);
/* 1865 */           if (null != threadLocalMap) {
/* 1866 */             expungeStaleEntriesMethod.invoke(threadLocalMap, new Object[0]);
/* 1867 */             checkThreadLocalMapForLeaks(threadLocalMap, tableField);
/*      */           }
/*      */           
/*      */ 
/* 1871 */           threadLocalMap = inheritableThreadLocalsField.get(threads[i]);
/* 1872 */           if (null != threadLocalMap) {
/* 1873 */             expungeStaleEntriesMethod.invoke(threadLocalMap, new Object[0]);
/* 1874 */             checkThreadLocalMapForLeaks(threadLocalMap, tableField);
/*      */           }
/*      */         }
/*      */       }
/*      */     } catch (Throwable t) {
/* 1879 */       JreCompat jreCompat = JreCompat.getInstance();
/* 1880 */       if (jreCompat.isInstanceOfInaccessibleObjectException(t))
/*      */       {
/*      */ 
/* 1883 */         log.warn(sm.getString("webappClassLoader.addExportsThreadLocal"));
/*      */       } else {
/* 1885 */         ExceptionUtils.handleThrowable(t);
/* 1886 */         log.warn(sm.getString("webappClassLoader.checkThreadLocalsForLeaksFail", new Object[] {
/*      */         
/* 1888 */           getContextName() }), t);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void checkThreadLocalMapForLeaks(Object map, Field internalTableField)
/*      */     throws IllegalAccessException, NoSuchFieldException
/*      */   {
/* 1902 */     if (map != null) {
/* 1903 */       Object[] table = (Object[])internalTableField.get(map);
/* 1904 */       if (table != null) {
/* 1905 */         for (int j = 0; j < table.length; j++) {
/* 1906 */           Object obj = table[j];
/* 1907 */           if (obj != null) {
/* 1908 */             boolean keyLoadedByWebapp = false;
/* 1909 */             boolean valueLoadedByWebapp = false;
/*      */             
/* 1911 */             Object key = ((Reference)obj).get();
/* 1912 */             if ((equals(key)) || (loadedByThisOrChild(key))) {
/* 1913 */               keyLoadedByWebapp = true;
/*      */             }
/*      */             
/*      */ 
/* 1917 */             Field valueField = obj.getClass().getDeclaredField("value");
/* 1918 */             valueField.setAccessible(true);
/* 1919 */             Object value = valueField.get(obj);
/* 1920 */             if ((equals(value)) || (loadedByThisOrChild(value))) {
/* 1921 */               valueLoadedByWebapp = true;
/*      */             }
/* 1923 */             if ((keyLoadedByWebapp) || (valueLoadedByWebapp)) {
/* 1924 */               Object[] args = new Object[5];
/* 1925 */               args[0] = getContextName();
/* 1926 */               if (key != null) {
/* 1927 */                 args[1] = getPrettyClassName(key.getClass());
/*      */                 try {
/* 1929 */                   args[2] = key.toString();
/*      */                 } catch (Exception e) {
/* 1931 */                   log.warn(sm.getString("webappClassLoader.checkThreadLocalsForLeaks.badKey", new Object[] { args[1] }), e);
/*      */                   
/*      */ 
/* 1934 */                   args[2] = sm.getString("webappClassLoader.checkThreadLocalsForLeaks.unknown");
/*      */                 }
/*      */               }
/*      */               
/* 1938 */               if (value != null) {
/* 1939 */                 args[3] = getPrettyClassName(value.getClass());
/*      */                 try {
/* 1941 */                   args[4] = value.toString();
/*      */                 } catch (Exception e) {
/* 1943 */                   log.warn(sm.getString("webappClassLoader.checkThreadLocalsForLeaks.badValue", new Object[] { args[3] }), e);
/*      */                   
/*      */ 
/* 1946 */                   args[4] = sm.getString("webappClassLoader.checkThreadLocalsForLeaks.unknown");
/*      */                 }
/*      */               }
/*      */               
/* 1950 */               if (valueLoadedByWebapp) {
/* 1951 */                 log.error(sm.getString("webappClassLoader.checkThreadLocalsForLeaks", args));
/*      */ 
/*      */               }
/* 1954 */               else if (value == null) {
/* 1955 */                 if (log.isDebugEnabled()) {
/* 1956 */                   log.debug(sm.getString("webappClassLoader.checkThreadLocalsForLeaksNull", args));
/*      */                 }
/*      */                 
/*      */ 
/*      */               }
/* 1961 */               else if (log.isDebugEnabled()) {
/* 1962 */                 log.debug(sm.getString("webappClassLoader.checkThreadLocalsForLeaksNone", args));
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private String getPrettyClassName(Class<?> clazz)
/*      */   {
/* 1975 */     String name = clazz.getCanonicalName();
/* 1976 */     if (name == null) {
/* 1977 */       name = clazz.getName();
/*      */     }
/* 1979 */     return name;
/*      */   }
/*      */   
/*      */   private String getStackTrace(Thread thread) {
/* 1983 */     StringBuilder builder = new StringBuilder();
/* 1984 */     for (StackTraceElement ste : thread.getStackTrace()) {
/* 1985 */       builder.append("\n ").append(ste);
/*      */     }
/* 1987 */     return builder.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean loadedByThisOrChild(Object o)
/*      */   {
/* 1996 */     if (o == null) {
/* 1997 */       return false;
/*      */     }
/*      */     Class<?> clazz;
/*      */     Class<?> clazz;
/* 2001 */     if ((o instanceof Class)) {
/* 2002 */       clazz = (Class)o;
/*      */     } else {
/* 2004 */       clazz = o.getClass();
/*      */     }
/*      */     
/* 2007 */     ClassLoader cl = clazz.getClassLoader();
/* 2008 */     while (cl != null) {
/* 2009 */       if (cl == this) {
/* 2010 */         return true;
/*      */       }
/* 2012 */       cl = cl.getParent();
/*      */     }
/*      */     
/* 2015 */     if ((o instanceof Collection)) {
/* 2016 */       Iterator<?> iter = ((Collection)o).iterator();
/*      */       try {
/* 2018 */         while (iter.hasNext()) {
/* 2019 */           Object entry = iter.next();
/* 2020 */           if (loadedByThisOrChild(entry)) {
/* 2021 */             return true;
/*      */           }
/*      */         }
/*      */       } catch (ConcurrentModificationException e) {
/* 2025 */         log.warn(sm.getString("webappClassLoader.loadedByThisOrChildFail", new Object[] {clazz
/* 2026 */           .getName(), getContextName() }), e);
/*      */       }
/*      */     }
/*      */     
/* 2030 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private Thread[] getThreads()
/*      */   {
/* 2038 */     ThreadGroup tg = Thread.currentThread().getThreadGroup();
/*      */     try
/*      */     {
/* 2041 */       while (tg.getParent() != null) {
/* 2042 */         tg = tg.getParent();
/*      */       }
/*      */     } catch (SecurityException se) {
/* 2045 */       String msg = sm.getString("webappClassLoader.getThreadGroupError", new Object[] {tg
/* 2046 */         .getName() });
/* 2047 */       if (log.isDebugEnabled()) {
/* 2048 */         log.debug(msg, se);
/*      */       } else {
/* 2050 */         log.warn(msg);
/*      */       }
/*      */     }
/*      */     
/* 2054 */     int threadCountGuess = tg.activeCount() + 50;
/* 2055 */     Thread[] threads = new Thread[threadCountGuess];
/* 2056 */     int threadCountActual = tg.enumerate(threads);
/*      */     
/* 2058 */     while (threadCountActual == threadCountGuess) {
/* 2059 */       threadCountGuess *= 2;
/* 2060 */       threads = new Thread[threadCountGuess];
/*      */       
/*      */ 
/* 2063 */       threadCountActual = tg.enumerate(threads);
/*      */     }
/*      */     
/* 2066 */     return threads;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void clearReferencesRmiTargets()
/*      */   {
/*      */     try
/*      */     {
/* 2079 */       Class<?> objectTargetClass = Class.forName("sun.rmi.transport.Target");
/* 2080 */       Field cclField = objectTargetClass.getDeclaredField("ccl");
/* 2081 */       cclField.setAccessible(true);
/*      */       
/* 2083 */       Field stubField = objectTargetClass.getDeclaredField("stub");
/* 2084 */       stubField.setAccessible(true);
/*      */       
/*      */ 
/*      */ 
/* 2088 */       Class<?> objectTableClass = Class.forName("sun.rmi.transport.ObjectTable");
/* 2089 */       Field objTableField = objectTableClass.getDeclaredField("objTable");
/* 2090 */       objTableField.setAccessible(true);
/* 2091 */       Object objTable = objTableField.get(null);
/* 2092 */       if (objTable == null) {
/* 2093 */         return;
/*      */       }
/*      */       
/* 2096 */       synchronized (objTable)
/*      */       {
/* 2098 */         if ((objTable instanceof Map)) {
/* 2099 */           Iterator<?> iter = ((Map)objTable).values().iterator();
/* 2100 */           while (iter.hasNext()) {
/* 2101 */             Object obj = iter.next();
/* 2102 */             Object cclObject = cclField.get(obj);
/* 2103 */             if (this == cclObject) {
/* 2104 */               iter.remove();
/* 2105 */               Object stubObject = stubField.get(obj);
/* 2106 */               log.error(sm.getString("webappClassLoader.clearRmi", new Object[] {stubObject
/* 2107 */                 .getClass().getName(), stubObject }));
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 2113 */         Field implTableField = objectTableClass.getDeclaredField("implTable");
/* 2114 */         implTableField.setAccessible(true);
/* 2115 */         Object implTable = implTableField.get(null);
/* 2116 */         if (implTable == null) {
/* 2117 */           return;
/*      */         }
/*      */         
/*      */ 
/* 2121 */         if ((implTable instanceof Map)) {
/* 2122 */           Iterator<?> iter = ((Map)implTable).values().iterator();
/* 2123 */           while (iter.hasNext()) {
/* 2124 */             Object obj = iter.next();
/* 2125 */             Object cclObject = cclField.get(obj);
/* 2126 */             if (this == cclObject) {
/* 2127 */               iter.remove();
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     } catch (ClassNotFoundException e) {
/* 2133 */       log.info(sm.getString("webappClassLoader.clearRmiInfo", new Object[] {
/* 2134 */         getContextName() }), e);
/*      */     }
/*      */     catch (SecurityException|NoSuchFieldException|IllegalArgumentException|IllegalAccessException e) {
/* 2137 */       log.warn(sm.getString("webappClassLoader.clearRmiFail", new Object[] {
/* 2138 */         getContextName() }), e);
/*      */     } catch (Exception e) {
/* 2140 */       JreCompat jreCompat = JreCompat.getInstance();
/* 2141 */       if (jreCompat.isInstanceOfInaccessibleObjectException(e))
/*      */       {
/*      */ 
/* 2144 */         log.warn(sm.getString("webappClassLoader.addExportsRmi"));
/*      */       }
/*      */       else {
/* 2147 */         throw e;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Class<?> findClassInternal(String name)
/*      */   {
/* 2162 */     checkStateForResourceLoading(name);
/*      */     
/* 2164 */     if (name == null) {
/* 2165 */       return null;
/*      */     }
/* 2167 */     String path = binaryNameToPath(name, true);
/*      */     
/* 2169 */     ResourceEntry entry = (ResourceEntry)this.resourceEntries.get(path);
/* 2170 */     WebResource resource = null;
/*      */     
/* 2172 */     if (entry == null) {
/* 2173 */       resource = this.resources.getClassLoaderResource(path);
/*      */       
/* 2175 */       if (!resource.exists()) {
/* 2176 */         return null;
/*      */       }
/*      */       
/* 2179 */       entry = new ResourceEntry();
/* 2180 */       entry.lastModified = resource.getLastModified();
/*      */       
/*      */ 
/* 2183 */       synchronized (this.resourceEntries)
/*      */       {
/*      */ 
/*      */ 
/* 2187 */         ResourceEntry entry2 = (ResourceEntry)this.resourceEntries.get(path);
/* 2188 */         if (entry2 == null) {
/* 2189 */           this.resourceEntries.put(path, entry);
/*      */         } else {
/* 2191 */           entry = entry2;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2196 */     Class<?> clazz = entry.loadedClass;
/* 2197 */     if (clazz != null) {
/* 2198 */       return clazz;
/*      */     }
/* 2200 */     synchronized (getClassLoadingLock(name)) {
/* 2201 */       clazz = entry.loadedClass;
/* 2202 */       if (clazz != null) {
/* 2203 */         return clazz;
/*      */       }
/* 2205 */       if (resource == null) {
/* 2206 */         resource = this.resources.getClassLoaderResource(path);
/*      */       }
/*      */       
/* 2209 */       if (!resource.exists()) {
/* 2210 */         return null;
/*      */       }
/*      */       
/* 2213 */       byte[] binaryContent = resource.getContent();
/* 2214 */       Manifest manifest = resource.getManifest();
/* 2215 */       URL codeBase = resource.getCodeBase();
/* 2216 */       Certificate[] certificates = resource.getCertificates();
/*      */       String internalName;
/* 2218 */       if (this.transformers.size() > 0)
/*      */       {
/*      */ 
/*      */ 
/* 2222 */         String className = name.endsWith(".class") ? name.substring(0, name.length() - ".class".length()) : name;
/* 2223 */         internalName = className.replace(".", "/");
/*      */         
/* 2225 */         for (ClassFileTransformer transformer : this.transformers) {
/*      */           try {
/* 2227 */             byte[] transformed = transformer.transform(this, internalName, null, null, binaryContent);
/*      */             
/* 2229 */             if (transformed != null) {
/* 2230 */               binaryContent = transformed;
/*      */             }
/*      */           } catch (IllegalClassFormatException e) {
/* 2233 */             log.error(sm.getString("webappClassLoader.transformError", new Object[] { name }), e);
/* 2234 */             return null;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 2240 */       String packageName = null;
/* 2241 */       int pos = name.lastIndexOf('.');
/* 2242 */       if (pos != -1) {
/* 2243 */         packageName = name.substring(0, pos);
/*      */       }
/* 2245 */       Package pkg = null;
/*      */       
/* 2247 */       if (packageName != null) {
/* 2248 */         pkg = getPackage(packageName);
/*      */         
/* 2250 */         if (pkg == null) {
/*      */           try {
/* 2252 */             if (manifest == null) {
/* 2253 */               definePackage(packageName, null, null, null, null, null, null, null);
/*      */             } else {
/* 2255 */               definePackage(packageName, manifest, codeBase);
/*      */             }
/*      */           }
/*      */           catch (IllegalArgumentException localIllegalArgumentException) {}
/*      */           
/* 2260 */           pkg = getPackage(packageName);
/*      */         }
/*      */       }
/*      */       
/* 2264 */       if (this.securityManager != null)
/*      */       {
/*      */ 
/* 2267 */         if (pkg != null) {
/* 2268 */           boolean sealCheck = true;
/* 2269 */           if (pkg.isSealed()) {
/* 2270 */             sealCheck = pkg.isSealed(codeBase);
/*      */           } else {
/* 2272 */             sealCheck = (manifest == null) || (!isPackageSealed(packageName, manifest));
/*      */           }
/* 2274 */           if (!sealCheck) {
/* 2275 */             throw new SecurityException("Sealing violation loading " + name + " : Package " + packageName + " is sealed.");
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */       try
/*      */       {
/* 2283 */         clazz = defineClass(name, binaryContent, 0, binaryContent.length, new CodeSource(codeBase, certificates));
/*      */ 
/*      */       }
/*      */       catch (UnsupportedClassVersionError ucve)
/*      */       {
/* 2288 */         throw new UnsupportedClassVersionError(ucve.getLocalizedMessage() + " " + sm.getString("webappClassLoader.wrongVersion", new Object[] { name }));
/*      */       }
/*      */       
/* 2291 */       entry.loadedClass = clazz;
/*      */     }
/*      */     
/* 2294 */     return clazz;
/*      */   }
/*      */   
/*      */ 
/*      */   private String binaryNameToPath(String binaryName, boolean withLeadingSlash)
/*      */   {
/* 2300 */     StringBuilder path = new StringBuilder(7 + binaryName.length());
/* 2301 */     if (withLeadingSlash) {
/* 2302 */       path.append('/');
/*      */     }
/* 2304 */     path.append(binaryName.replace('.', '/'));
/* 2305 */     path.append(".class");
/* 2306 */     return path.toString();
/*      */   }
/*      */   
/*      */   private String nameToPath(String name)
/*      */   {
/* 2311 */     if (name.startsWith("/")) {
/* 2312 */       return name;
/*      */     }
/*      */     
/* 2315 */     StringBuilder path = new StringBuilder(1 + name.length());
/* 2316 */     path.append('/');
/* 2317 */     path.append(name);
/* 2318 */     return path.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean isPackageSealed(String name, Manifest man)
/*      */   {
/* 2332 */     String path = name.replace('.', '/') + '/';
/* 2333 */     Attributes attr = man.getAttributes(path);
/* 2334 */     String sealed = null;
/* 2335 */     if (attr != null) {
/* 2336 */       sealed = attr.getValue(Attributes.Name.SEALED);
/*      */     }
/* 2338 */     if ((sealed == null) && 
/* 2339 */       ((attr = man.getMainAttributes()) != null)) {
/* 2340 */       sealed = attr.getValue(Attributes.Name.SEALED);
/*      */     }
/*      */     
/* 2343 */     return "true".equalsIgnoreCase(sealed);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Class<?> findLoadedClass0(String name)
/*      */   {
/* 2358 */     String path = binaryNameToPath(name, true);
/*      */     
/* 2360 */     ResourceEntry entry = (ResourceEntry)this.resourceEntries.get(path);
/* 2361 */     if (entry != null) {
/* 2362 */       return entry.loadedClass;
/*      */     }
/* 2364 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void refreshPolicy()
/*      */   {
/*      */     try
/*      */     {
/* 2377 */       Policy policy = Policy.getPolicy();
/* 2378 */       policy.refresh();
/*      */     }
/*      */     catch (AccessControlException localAccessControlException) {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean filter(String name, boolean isClassName)
/*      */   {
/* 2397 */     if (name == null) {
/* 2398 */       return false;
/*      */     }
/*      */     
/* 2401 */     if (name.startsWith("javax"))
/*      */     {
/* 2403 */       if (name.length() == 5) {
/* 2404 */         return false;
/*      */       }
/* 2406 */       char ch = name.charAt(5);
/* 2407 */       if ((isClassName) && (ch == '.'))
/*      */       {
/* 2409 */         if (name.startsWith("servlet.jsp.jstl.", 6)) {
/* 2410 */           return false;
/*      */         }
/* 2412 */         if ((name.startsWith("el.", 6)) || 
/* 2413 */           (name.startsWith("servlet.", 6)) || 
/* 2414 */           (name.startsWith("websocket.", 6)) || 
/* 2415 */           (name.startsWith("security.auth.message.", 6))) {
/* 2416 */           return true;
/*      */         }
/* 2418 */       } else if ((!isClassName) && (ch == '/'))
/*      */       {
/* 2420 */         if (name.startsWith("servlet/jsp/jstl/", 6)) {
/* 2421 */           return false;
/*      */         }
/* 2423 */         if ((name.startsWith("el/", 6)) || 
/* 2424 */           (name.startsWith("servlet/", 6)) || 
/* 2425 */           (name.startsWith("websocket/", 6)) || 
/* 2426 */           (name.startsWith("security/auth/message/", 6))) {
/* 2427 */           return true;
/*      */         }
/*      */       }
/* 2430 */     } else if (name.startsWith("org"))
/*      */     {
/* 2432 */       if (name.length() == 3) {
/* 2433 */         return false;
/*      */       }
/* 2435 */       char ch = name.charAt(3);
/* 2436 */       if ((isClassName) && (ch == '.'))
/*      */       {
/* 2438 */         if (name.startsWith("apache.", 4))
/*      */         {
/* 2440 */           if (name.startsWith("tomcat.jdbc.", 11)) {
/* 2441 */             return false;
/*      */           }
/* 2443 */           if ((name.startsWith("el.", 11)) || 
/* 2444 */             (name.startsWith("catalina.", 11)) || 
/* 2445 */             (name.startsWith("jasper.", 11)) || 
/* 2446 */             (name.startsWith("juli.", 11)) || 
/* 2447 */             (name.startsWith("tomcat.", 11)) || 
/* 2448 */             (name.startsWith("naming.", 11)) || 
/* 2449 */             (name.startsWith("coyote.", 11))) {
/* 2450 */             return true;
/*      */           }
/*      */         }
/* 2453 */       } else if ((!isClassName) && (ch == '/'))
/*      */       {
/* 2455 */         if (name.startsWith("apache/", 4))
/*      */         {
/* 2457 */           if (name.startsWith("tomcat/jdbc/", 11)) {
/* 2458 */             return false;
/*      */           }
/* 2460 */           if ((name.startsWith("el/", 11)) || 
/* 2461 */             (name.startsWith("catalina/", 11)) || 
/* 2462 */             (name.startsWith("jasper/", 11)) || 
/* 2463 */             (name.startsWith("juli/", 11)) || 
/* 2464 */             (name.startsWith("tomcat/", 11)) || 
/* 2465 */             (name.startsWith("naming/", 11)) || 
/* 2466 */             (name.startsWith("coyote/", 11))) {
/* 2467 */             return true;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 2472 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   protected boolean filter(String name)
/*      */   {
/* 2486 */     return (filter(name, true)) || (filter(name, false));
/*      */   }
/*      */   
/*      */ 
/*      */   protected void addURL(URL url)
/*      */   {
/* 2492 */     super.addURL(url);
/* 2493 */     this.hasExternalRepositories = true;
/*      */   }
/*      */   
/*      */ 
/*      */   public String getWebappName()
/*      */   {
/* 2499 */     return getContextName();
/*      */   }
/*      */   
/*      */ 
/*      */   public String getHostName()
/*      */   {
/* 2505 */     if (this.resources != null) {
/* 2506 */       Container host = this.resources.getContext().getParent();
/* 2507 */       if (host != null) {
/* 2508 */         return host.getName();
/*      */       }
/*      */     }
/* 2511 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public String getServiceName()
/*      */   {
/* 2517 */     if (this.resources != null) {
/* 2518 */       Container host = this.resources.getContext().getParent();
/* 2519 */       if (host != null) {
/* 2520 */         Container engine = host.getParent();
/* 2521 */         if (engine != null) {
/* 2522 */           return engine.getName();
/*      */         }
/*      */       }
/*      */     }
/* 2526 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean hasLoggingConfig()
/*      */   {
/* 2532 */     if (Globals.IS_SECURITY_ENABLED) {
/* 2533 */       Boolean result = (Boolean)AccessController.doPrivileged(new PrivilegedHasLoggingConfig(null));
/* 2534 */       return result.booleanValue();
/*      */     }
/* 2536 */     return findResource("logging.properties") != null;
/*      */   }
/*      */   
/*      */   public void addLifecycleListener(LifecycleListener listener) {}
/*      */   
/*      */   public void removeLifecycleListener(LifecycleListener listener) {}
/*      */   
/*      */   private class PrivilegedHasLoggingConfig implements PrivilegedAction<Boolean> { private PrivilegedHasLoggingConfig() {}
/*      */     
/* 2545 */     public Boolean run() { return Boolean.valueOf(WebappClassLoaderBase.this.findResource("logging.properties") != null); }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\loader\WebappClassLoaderBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */