/*      */ package org.apache.catalina.startup;
/*      */ 
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URI;
/*      */ import java.net.URL;
/*      */ import java.nio.file.CopyOption;
/*      */ import java.nio.file.Files;
/*      */ import java.security.CodeSource;
/*      */ import java.security.Permission;
/*      */ import java.security.PermissionCollection;
/*      */ import java.security.Policy;
/*      */ import java.security.cert.Certificate;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.SortedSet;
/*      */ import java.util.TreeSet;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ExecutorService;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.jar.JarEntry;
/*      */ import java.util.jar.JarFile;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import javax.management.ObjectName;
/*      */ import org.apache.catalina.Container;
/*      */ import org.apache.catalina.Context;
/*      */ import org.apache.catalina.DistributedManager;
/*      */ import org.apache.catalina.Globals;
/*      */ import org.apache.catalina.Host;
/*      */ import org.apache.catalina.LifecycleEvent;
/*      */ import org.apache.catalina.LifecycleListener;
/*      */ import org.apache.catalina.LifecycleState;
/*      */ import org.apache.catalina.Manager;
/*      */ import org.apache.catalina.core.StandardContext;
/*      */ import org.apache.catalina.core.StandardHost;
/*      */ import org.apache.catalina.security.DeployXmlPermission;
/*      */ import org.apache.catalina.util.ContextName;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.tomcat.util.ExceptionUtils;
/*      */ import org.apache.tomcat.util.buf.UriUtil;
/*      */ import org.apache.tomcat.util.digester.Digester;
/*      */ import org.apache.tomcat.util.modeler.Registry;
/*      */ import org.apache.tomcat.util.res.StringManager;
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
/*      */ public class HostConfig
/*      */   implements LifecycleListener
/*      */ {
/*   84 */   private static final Log log = LogFactory.getLog(HostConfig.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*   89 */   protected static final StringManager sm = StringManager.getManager(HostConfig.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final long FILE_MODIFICATION_RESOLUTION_MS = 1000L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  102 */   protected String contextClass = "org.apache.catalina.core.StandardContext";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  108 */   protected Host host = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  114 */   protected ObjectName oname = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  121 */   protected boolean deployXML = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  129 */   protected boolean copyXML = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  136 */   protected boolean unpackWARs = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  142 */   protected final Map<String, DeployedApplication> deployed = new ConcurrentHashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  150 */   protected final ArrayList<String> serviced = new ArrayList();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  156 */   protected Digester digester = createDigester(this.contextClass);
/*  157 */   private final Object digesterLock = new Object();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  163 */   protected final Set<String> invalidWars = new HashSet();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getContextClass()
/*      */   {
/*  173 */     return this.contextClass;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setContextClass(String contextClass)
/*      */   {
/*  185 */     String oldContextClass = this.contextClass;
/*  186 */     this.contextClass = contextClass;
/*      */     
/*  188 */     if (!oldContextClass.equals(contextClass)) {
/*  189 */       synchronized (this.digesterLock) {
/*  190 */         this.digester = createDigester(getContextClass());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isDeployXML()
/*      */   {
/*  201 */     return this.deployXML;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDeployXML(boolean deployXML)
/*      */   {
/*  212 */     this.deployXML = deployXML;
/*      */   }
/*      */   
/*      */   private boolean isDeployThisXML(File docBase, ContextName cn)
/*      */   {
/*  217 */     boolean deployThisXML = isDeployXML();
/*  218 */     if ((Globals.IS_SECURITY_ENABLED) && (!deployThisXML))
/*      */     {
/*      */ 
/*  221 */       Policy currentPolicy = Policy.getPolicy();
/*  222 */       if (currentPolicy != null) {
/*      */         try
/*      */         {
/*  225 */           URL contextRootUrl = docBase.toURI().toURL();
/*  226 */           CodeSource cs = new CodeSource(contextRootUrl, (Certificate[])null);
/*  227 */           PermissionCollection pc = currentPolicy.getPermissions(cs);
/*  228 */           Permission p = new DeployXmlPermission(cn.getBaseName());
/*  229 */           if (pc.implies(p)) {
/*  230 */             deployThisXML = true;
/*      */           }
/*      */         }
/*      */         catch (MalformedURLException e) {
/*  234 */           log.warn("hostConfig.docBaseUrlInvalid", e);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  239 */     return deployThisXML;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isCopyXML()
/*      */   {
/*  248 */     return this.copyXML;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCopyXML(boolean copyXML)
/*      */   {
/*  260 */     this.copyXML = copyXML;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isUnpackWARs()
/*      */   {
/*  270 */     return this.unpackWARs;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUnpackWARs(boolean unpackWARs)
/*      */   {
/*  282 */     this.unpackWARs = unpackWARs;
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
/*      */   public void lifecycleEvent(LifecycleEvent event)
/*      */   {
/*      */     try
/*      */     {
/*  300 */       this.host = ((Host)event.getLifecycle());
/*  301 */       if ((this.host instanceof StandardHost)) {
/*  302 */         setCopyXML(((StandardHost)this.host).isCopyXML());
/*  303 */         setDeployXML(((StandardHost)this.host).isDeployXML());
/*  304 */         setUnpackWARs(((StandardHost)this.host).isUnpackWARs());
/*  305 */         setContextClass(((StandardHost)this.host).getContextClass());
/*      */       }
/*      */     } catch (ClassCastException e) {
/*  308 */       log.error(sm.getString("hostConfig.cce", new Object[] { event.getLifecycle() }), e);
/*  309 */       return;
/*      */     }
/*      */     
/*      */ 
/*  313 */     if (event.getType().equals("periodic")) {
/*  314 */       check();
/*  315 */     } else if (event.getType().equals("before_start")) {
/*  316 */       beforeStart();
/*  317 */     } else if (event.getType().equals("start")) {
/*  318 */       start();
/*  319 */     } else if (event.getType().equals("stop")) {
/*  320 */       stop();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void addServiced(String name)
/*      */   {
/*  330 */     this.serviced.add(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized boolean isServiced(String name)
/*      */   {
/*  340 */     return this.serviced.contains(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void removeServiced(String name)
/*      */   {
/*  349 */     this.serviced.remove(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getDeploymentTime(String name)
/*      */   {
/*  360 */     DeployedApplication app = (DeployedApplication)this.deployed.get(name);
/*  361 */     if (app == null) {
/*  362 */       return 0L;
/*      */     }
/*      */     
/*  365 */     return app.timestamp;
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
/*      */   public boolean isDeployed(String name)
/*      */   {
/*  378 */     DeployedApplication app = (DeployedApplication)this.deployed.get(name);
/*  379 */     if (app == null) {
/*  380 */       return false;
/*      */     }
/*      */     
/*  383 */     return true;
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
/*      */   protected static Digester createDigester(String contextClassName)
/*      */   {
/*  397 */     Digester digester = new Digester();
/*  398 */     digester.setValidating(false);
/*      */     
/*  400 */     digester.addObjectCreate("Context", contextClassName, "className");
/*      */     
/*      */ 
/*  403 */     digester.addSetProperties("Context");
/*  404 */     return digester;
/*      */   }
/*      */   
/*      */   protected File returnCanonicalPath(String path) {
/*  408 */     File file = new File(path);
/*  409 */     if (!file.isAbsolute())
/*  410 */       file = new File(this.host.getCatalinaBase(), path);
/*      */     try {
/*  412 */       return file.getCanonicalFile();
/*      */     } catch (IOException e) {}
/*  414 */     return file;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getConfigBaseName()
/*      */   {
/*  425 */     return this.host.getConfigBaseFile().getAbsolutePath();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void deployApps()
/*      */   {
/*  435 */     File appBase = this.host.getAppBaseFile();
/*  436 */     File configBase = this.host.getConfigBaseFile();
/*  437 */     String[] filteredAppPaths = filterAppPaths(appBase.list());
/*      */     
/*  439 */     deployDescriptors(configBase, configBase.list());
/*      */     
/*  441 */     deployWARs(appBase, filteredAppPaths);
/*      */     
/*  443 */     deployDirectories(appBase, filteredAppPaths);
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
/*      */   protected String[] filterAppPaths(String[] unfilteredAppPaths)
/*      */   {
/*  457 */     Pattern filter = this.host.getDeployIgnorePattern();
/*  458 */     if ((filter == null) || (unfilteredAppPaths == null)) {
/*  459 */       return unfilteredAppPaths;
/*      */     }
/*      */     
/*  462 */     List<String> filteredList = new ArrayList();
/*  463 */     Matcher matcher = null;
/*  464 */     for (String appPath : unfilteredAppPaths) {
/*  465 */       if (matcher == null) {
/*  466 */         matcher = filter.matcher(appPath);
/*      */       } else {
/*  468 */         matcher.reset(appPath);
/*      */       }
/*  470 */       if (matcher.matches()) {
/*  471 */         if (log.isDebugEnabled()) {
/*  472 */           log.debug(sm.getString("hostConfig.ignorePath", new Object[] { appPath }));
/*      */         }
/*      */       } else {
/*  475 */         filteredList.add(appPath);
/*      */       }
/*      */     }
/*  478 */     return (String[])filteredList.toArray(new String[filteredList.size()]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void deployApps(String name)
/*      */   {
/*  489 */     File appBase = this.host.getAppBaseFile();
/*  490 */     File configBase = this.host.getConfigBaseFile();
/*  491 */     ContextName cn = new ContextName(name, false);
/*  492 */     String baseName = cn.getBaseName();
/*      */     
/*  494 */     if (deploymentExists(cn.getName())) {
/*  495 */       return;
/*      */     }
/*      */     
/*      */ 
/*  499 */     File xml = new File(configBase, baseName + ".xml");
/*  500 */     if (xml.exists()) {
/*  501 */       deployDescriptor(cn, xml);
/*  502 */       return;
/*      */     }
/*      */     
/*  505 */     File war = new File(appBase, baseName + ".war");
/*  506 */     if (war.exists()) {
/*  507 */       deployWAR(cn, war);
/*  508 */       return;
/*      */     }
/*      */     
/*  511 */     File dir = new File(appBase, baseName);
/*  512 */     if (dir.exists()) {
/*  513 */       deployDirectory(cn, dir);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void deployDescriptors(File configBase, String[] files)
/*      */   {
/*  524 */     if (files == null) {
/*  525 */       return;
/*      */     }
/*  527 */     ExecutorService es = this.host.getStartStopExecutor();
/*  528 */     List<Future<?>> results = new ArrayList();
/*      */     
/*  530 */     for (int i = 0; i < files.length; i++) {
/*  531 */       File contextXml = new File(configBase, files[i]);
/*      */       
/*  533 */       if (files[i].toLowerCase(Locale.ENGLISH).endsWith(".xml")) {
/*  534 */         ContextName cn = new ContextName(files[i], true);
/*      */         
/*  536 */         if ((!isServiced(cn.getName())) && (!deploymentExists(cn.getName())))
/*      */         {
/*      */ 
/*  539 */           results.add(es
/*  540 */             .submit(new DeployDescriptor(this, cn, contextXml)));
/*      */         }
/*      */       }
/*      */     }
/*  544 */     for (Future<?> result : results) {
/*      */       try {
/*  546 */         result.get();
/*      */       } catch (Exception e) {
/*  548 */         log.error(sm.getString("hostConfig.deployDescriptor.threaded.error"), e);
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
/*      */   protected void deployDescriptor(ContextName cn, File contextXml)
/*      */   {
/*  564 */     DeployedApplication deployedApp = new DeployedApplication(cn.getName(), true);
/*      */     
/*  566 */     long startTime = 0L;
/*      */     
/*  568 */     if (log.isInfoEnabled()) {
/*  569 */       startTime = System.currentTimeMillis();
/*  570 */       log.info(sm.getString("hostConfig.deployDescriptor", new Object[] {contextXml
/*  571 */         .getAbsolutePath() }));
/*      */     }
/*      */     
/*  574 */     Context context = null;
/*  575 */     boolean isExternalWar = false;
/*  576 */     boolean isExternal = false;
/*  577 */     File expandedDocBase = null;
/*      */     try {
/*  579 */       FileInputStream fis = new FileInputStream(contextXml);Throwable localThrowable4 = null;
/*  580 */       try { synchronized (this.digesterLock) {
/*      */           try {
/*  582 */             context = (Context)this.digester.parse(fis);
/*      */           } catch (Exception e) {
/*  584 */             log.error(sm.getString("hostConfig.deployDescriptor.error", new Object[] {contextXml
/*      */             
/*  586 */               .getAbsolutePath() }), e);
/*      */           } finally {
/*  588 */             this.digester.reset();
/*  589 */             if (context == null) {
/*  590 */               context = new FailedContext();
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*  595 */         Class<?> clazz = Class.forName(this.host.getConfigClass());
/*  596 */         LifecycleListener listener = (LifecycleListener)clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/*  597 */         context.addLifecycleListener(listener);
/*      */         
/*  599 */         context.setConfigFile(contextXml.toURI().toURL());
/*  600 */         context.setName(cn.getName());
/*  601 */         context.setPath(cn.getPath());
/*  602 */         context.setWebappVersion(cn.getVersion());
/*      */         
/*  604 */         if (context.getDocBase() != null) {
/*  605 */           File docBase = new File(context.getDocBase());
/*  606 */           if (!docBase.isAbsolute()) {
/*  607 */             docBase = new File(this.host.getAppBaseFile(), context.getDocBase());
/*      */           }
/*      */           
/*  610 */           if (!docBase.getCanonicalPath().startsWith(this.host
/*  611 */             .getAppBaseFile().getAbsolutePath() + File.separator)) {
/*  612 */             isExternal = true;
/*  613 */             deployedApp.redeployResources.put(contextXml
/*  614 */               .getAbsolutePath(), 
/*  615 */               Long.valueOf(contextXml.lastModified()));
/*  616 */             deployedApp.redeployResources.put(docBase.getAbsolutePath(), 
/*  617 */               Long.valueOf(docBase.lastModified()));
/*  618 */             if (docBase.getAbsolutePath().toLowerCase(Locale.ENGLISH).endsWith(".war")) {
/*  619 */               isExternalWar = true;
/*      */             }
/*      */           } else {
/*  622 */             log.warn(sm.getString("hostConfig.deployDescriptor.localDocBaseSpecified", new Object[] { docBase }));
/*      */             
/*      */ 
/*  625 */             context.setDocBase(null);
/*      */           }
/*      */         }
/*      */         
/*  629 */         this.host.addChild(context);
/*      */       }
/*      */       catch (Throwable localThrowable2)
/*      */       {
/*  579 */         localThrowable4 = localThrowable2;throw localThrowable2;
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
/*  630 */         if (fis != null) if (localThrowable4 != null) try { fis.close(); } catch (Throwable localThrowable3) { localThrowable4.addSuppressed(localThrowable3); } else fis.close(); } } catch (Throwable t) { boolean unpackWAR;
/*  631 */       File warDocBase; ExceptionUtils.handleThrowable(t);
/*  632 */       log.error(sm.getString("hostConfig.deployDescriptor.error", new Object[] {contextXml
/*  633 */         .getAbsolutePath() }), t);
/*      */     }
/*      */     finally {
/*      */       boolean unpackWAR;
/*      */       File warDocBase;
/*  638 */       expandedDocBase = new File(this.host.getAppBaseFile(), cn.getBaseName());
/*  639 */       if ((context.getDocBase() != null) && 
/*  640 */         (!context.getDocBase().toLowerCase(Locale.ENGLISH).endsWith(".war")))
/*      */       {
/*  642 */         expandedDocBase = new File(context.getDocBase());
/*  643 */         if (!expandedDocBase.isAbsolute())
/*      */         {
/*  645 */           expandedDocBase = new File(this.host.getAppBaseFile(), context.getDocBase());
/*      */         }
/*      */       }
/*      */       
/*  649 */       boolean unpackWAR = this.unpackWARs;
/*  650 */       if ((unpackWAR) && ((context instanceof StandardContext))) {
/*  651 */         unpackWAR = ((StandardContext)context).getUnpackWAR();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  656 */       if (isExternalWar) {
/*  657 */         if (unpackWAR) {
/*  658 */           deployedApp.redeployResources.put(expandedDocBase.getAbsolutePath(), 
/*  659 */             Long.valueOf(expandedDocBase.lastModified()));
/*  660 */           addWatchedResources(deployedApp, expandedDocBase.getAbsolutePath(), context);
/*      */         } else {
/*  662 */           addWatchedResources(deployedApp, null, context);
/*      */         }
/*      */       }
/*      */       else {
/*  666 */         if (!isExternal) {
/*  667 */           File warDocBase = new File(expandedDocBase.getAbsolutePath() + ".war");
/*  668 */           if (warDocBase.exists()) {
/*  669 */             deployedApp.redeployResources.put(warDocBase.getAbsolutePath(), 
/*  670 */               Long.valueOf(warDocBase.lastModified()));
/*      */           }
/*      */           else {
/*  673 */             deployedApp.redeployResources.put(warDocBase
/*  674 */               .getAbsolutePath(), 
/*  675 */               Long.valueOf(0L));
/*      */           }
/*      */         }
/*  678 */         if (unpackWAR) {
/*  679 */           deployedApp.redeployResources.put(expandedDocBase.getAbsolutePath(), 
/*  680 */             Long.valueOf(expandedDocBase.lastModified()));
/*  681 */           addWatchedResources(deployedApp, expandedDocBase
/*  682 */             .getAbsolutePath(), context);
/*      */         } else {
/*  684 */           addWatchedResources(deployedApp, null, context);
/*      */         }
/*  686 */         if (!isExternal)
/*      */         {
/*      */ 
/*  689 */           deployedApp.redeployResources.put(contextXml
/*  690 */             .getAbsolutePath(), 
/*  691 */             Long.valueOf(contextXml.lastModified()));
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  696 */       addGlobalRedeployResources(deployedApp);
/*      */     }
/*      */     
/*  699 */     if (this.host.findChild(context.getName()) != null) {
/*  700 */       this.deployed.put(context.getName(), deployedApp);
/*      */     }
/*      */     
/*  703 */     if (log.isInfoEnabled()) {
/*  704 */       log.info(sm.getString("hostConfig.deployDescriptor.finished", new Object[] {contextXml
/*  705 */         .getAbsolutePath(), Long.valueOf(System.currentTimeMillis() - startTime) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void deployWARs(File appBase, String[] files)
/*      */   {
/*  717 */     if (files == null) {
/*  718 */       return;
/*      */     }
/*  720 */     ExecutorService es = this.host.getStartStopExecutor();
/*  721 */     List<Future<?>> results = new ArrayList();
/*      */     
/*  723 */     for (int i = 0; i < files.length; i++)
/*      */     {
/*  725 */       if (!files[i].equalsIgnoreCase("META-INF"))
/*      */       {
/*  727 */         if (!files[i].equalsIgnoreCase("WEB-INF"))
/*      */         {
/*  729 */           File war = new File(appBase, files[i]);
/*  730 */           if ((files[i].toLowerCase(Locale.ENGLISH).endsWith(".war")) && 
/*  731 */             (war.isFile()) && (!this.invalidWars.contains(files[i])))
/*      */           {
/*  733 */             ContextName cn = new ContextName(files[i], true);
/*      */             
/*  735 */             if (!isServiced(cn.getName()))
/*      */             {
/*      */ 
/*  738 */               if (deploymentExists(cn.getName())) {
/*  739 */                 DeployedApplication app = (DeployedApplication)this.deployed.get(cn.getName());
/*  740 */                 boolean unpackWAR = this.unpackWARs;
/*  741 */                 if ((unpackWAR) && ((this.host.findChild(cn.getName()) instanceof StandardContext))) {
/*  742 */                   unpackWAR = ((StandardContext)this.host.findChild(cn.getName())).getUnpackWAR();
/*      */                 }
/*  744 */                 if ((!unpackWAR) && (app != null))
/*      */                 {
/*      */ 
/*  747 */                   File dir = new File(appBase, cn.getBaseName());
/*  748 */                   if (dir.exists()) {
/*  749 */                     if (!app.loggedDirWarning) {
/*  750 */                       log.warn(sm.getString("hostConfig.deployWar.hiddenDir", new Object[] {dir
/*      */                       
/*  752 */                         .getAbsoluteFile(), war
/*  753 */                         .getAbsoluteFile() }));
/*  754 */                       app.loggedDirWarning = true;
/*      */                     }
/*      */                   } else {
/*  757 */                     app.loggedDirWarning = false;
/*      */                   }
/*      */                   
/*      */                 }
/*      */                 
/*      */ 
/*      */               }
/*  764 */               else if (!validateContextPath(appBase, cn.getBaseName())) {
/*  765 */                 log.error(sm.getString("hostConfig.illegalWarName", new Object[] { files[i] }));
/*      */                 
/*  767 */                 this.invalidWars.add(files[i]);
/*      */               }
/*      */               else
/*      */               {
/*  771 */                 results.add(es.submit(new DeployWar(this, cn, war)));
/*      */               } }
/*      */           }
/*      */         } } }
/*  775 */     for (Future<?> result : results) {
/*      */       try {
/*  777 */         result.get();
/*      */       } catch (Exception e) {
/*  779 */         log.error(sm.getString("hostConfig.deployWar.threaded.error"), e);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean validateContextPath(File appBase, String contextPath)
/*      */   {
/*  791 */     String canonicalDocBase = null;
/*      */     try
/*      */     {
/*  794 */       String canonicalAppBase = appBase.getCanonicalPath();
/*  795 */       StringBuilder docBase = new StringBuilder(canonicalAppBase);
/*  796 */       if (canonicalAppBase.endsWith(File.separator)) {
/*  797 */         docBase.append(contextPath.substring(1).replace('/', File.separatorChar));
/*      */       }
/*      */       else {
/*  800 */         docBase.append(contextPath.replace('/', File.separatorChar));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  806 */       canonicalDocBase = new File(docBase.toString()).getCanonicalPath();
/*      */       
/*      */ 
/*      */ 
/*  810 */       if (canonicalDocBase.endsWith(File.separator)) {
/*  811 */         docBase.append(File.separator);
/*      */       }
/*      */     } catch (IOException ioe) {
/*  814 */       return false;
/*      */     }
/*      */     
/*      */     StringBuilder docBase;
/*      */     
/*  819 */     return canonicalDocBase.equals(docBase.toString());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void deployWAR(ContextName cn, File war)
/*      */   {
/*  830 */     File xml = new File(this.host.getAppBaseFile(), cn.getBaseName() + "/" + "META-INF/context.xml");
/*      */     
/*      */ 
/*  833 */     File warTracker = new File(this.host.getAppBaseFile(), cn.getBaseName() + "/" + "/META-INF/war-tracker");
/*      */     
/*  835 */     boolean xmlInWar = false;
/*  836 */     try { JarFile jar = new JarFile(war);Throwable localThrowable22 = null;
/*  837 */       try { JarEntry entry = jar.getJarEntry("META-INF/context.xml");
/*  838 */         if (entry != null) {
/*  839 */           xmlInWar = true;
/*      */         }
/*      */       }
/*      */       catch (Throwable localThrowable2)
/*      */       {
/*  836 */         localThrowable22 = localThrowable2;throw localThrowable2;
/*      */ 
/*      */       }
/*      */       finally
/*      */       {
/*  841 */         if (jar != null) { if (localThrowable22 != null) try { jar.close(); } catch (Throwable localThrowable3) { localThrowable22.addSuppressed(localThrowable3); } else { jar.close();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (IOException localIOException) {}
/*      */     
/*  848 */     boolean useXml = false;
/*      */     
/*      */ 
/*  851 */     if ((xml.exists()) && (this.unpackWARs) && (
/*  852 */       (!warTracker.exists()) || (warTracker.lastModified() == war.lastModified()))) {
/*  853 */       useXml = true;
/*      */     }
/*      */     
/*  856 */     Object context = null;
/*  857 */     boolean deployThisXML = isDeployThisXML(war, cn);
/*      */     Throwable localThrowable23;
/*      */     Throwable localThrowable28;
/*  860 */     try { if ((deployThisXML) && (useXml) && (!this.copyXML)) {
/*  861 */         synchronized (this.digesterLock) {
/*      */           try {
/*  863 */             context = (Context)this.digester.parse(xml);
/*      */           } catch (Exception e) {
/*  865 */             log.error(sm.getString("hostConfig.deployDescriptor.error", new Object[] {war
/*      */             
/*  867 */               .getAbsolutePath() }), e);
/*      */           } finally {
/*  869 */             this.digester.reset();
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  875 */         ((Context)context).setConfigFile(xml.toURI().toURL());
/*  876 */       } else if ((deployThisXML) && (xmlInWar)) {
/*  877 */         synchronized (this.digesterLock) {
/*  878 */           try { JarFile jar = new JarFile(war);localThrowable23 = null;
/*  879 */             try { JarEntry entry = jar.getJarEntry("META-INF/context.xml");
/*  880 */               InputStream istream = jar.getInputStream(entry);localThrowable28 = null;
/*  881 */               try { context = (Context)this.digester.parse(istream);
/*      */               }
/*      */               catch (Throwable localThrowable5)
/*      */               {
/*  880 */                 localThrowable28 = localThrowable5;throw localThrowable5;
/*      */               }
/*      */               finally {}
/*      */             }
/*      */             catch (Throwable localThrowable25)
/*      */             {
/*  878 */               localThrowable23 = localThrowable25;throw localThrowable25;
/*      */ 
/*      */             }
/*      */             finally
/*      */             {
/*  883 */               if (jar != null) if (localThrowable23 != null) try { jar.close(); } catch (Throwable localThrowable9) { localThrowable23.addSuppressed(localThrowable9); } else jar.close();
/*  884 */             } } catch (Exception e) { log.error(sm.getString("hostConfig.deployDescriptor.error", new Object[] {war
/*      */             
/*  886 */               .getAbsolutePath() }), e);
/*      */           } finally {
/*  888 */             this.digester.reset();
/*  889 */             if (context == null) {
/*  890 */               context = new FailedContext();
/*      */             }
/*  892 */             ((Context)context).setConfigFile(
/*  893 */               UriUtil.buildJarUrl(war, "META-INF/context.xml"));
/*      */           }
/*      */         }
/*  896 */       } else if ((!deployThisXML) && (xmlInWar))
/*      */       {
/*      */ 
/*  899 */         log.error(sm.getString("hostConfig.deployDescriptor.blocked", new Object[] {cn
/*  900 */           .getPath(), "META-INF/context.xml", new File(this.host
/*  901 */           .getConfigBaseFile(), cn.getBaseName() + ".xml") }));
/*      */       } else {
/*  903 */         context = (Context)Class.forName(this.contextClass).getConstructor(new Class[0]).newInstance(new Object[0]);
/*      */       }
/*      */     } catch (Throwable t) {
/*  906 */       ExceptionUtils.handleThrowable(t);
/*  907 */       log.error(sm.getString("hostConfig.deployWar.error", new Object[] {war
/*  908 */         .getAbsolutePath() }), t);
/*      */     } finally {
/*  910 */       if (context == null) {
/*  911 */         context = new FailedContext();
/*      */       }
/*      */     }
/*      */     
/*  915 */     boolean copyThisXml = false;
/*  916 */     if (deployThisXML) {
/*  917 */       if ((this.host instanceof StandardHost)) {
/*  918 */         copyThisXml = ((StandardHost)this.host).isCopyXML();
/*      */       }
/*      */       
/*      */ 
/*  922 */       if ((!copyThisXml) && ((context instanceof StandardContext))) {
/*  923 */         copyThisXml = ((StandardContext)context).getCopyXML();
/*      */       }
/*      */       
/*  926 */       if ((xmlInWar) && (copyThisXml))
/*      */       {
/*      */ 
/*  929 */         xml = new File(this.host.getConfigBaseFile(), cn.getBaseName() + ".xml");
/*  930 */         try { JarFile jar = new JarFile(war);localThrowable23 = null;
/*  931 */           try { JarEntry entry = jar.getJarEntry("META-INF/context.xml");
/*  932 */             InputStream istream = jar.getInputStream(entry);localThrowable28 = null;
/*  933 */             try { FileOutputStream fos = new FileOutputStream(xml);Throwable localThrowable29 = null;
/*  934 */               try { BufferedOutputStream ostream = new BufferedOutputStream(fos, 1024);Throwable localThrowable30 = null;
/*  935 */                 try { byte[] buffer = new byte['Ѐ'];
/*      */                   for (;;) {
/*  937 */                     int n = istream.read(buffer);
/*  938 */                     if (n < 0) {
/*      */                       break;
/*      */                     }
/*  941 */                     ostream.write(buffer, 0, n);
/*      */                   }
/*  943 */                   ostream.flush();
/*      */                 }
/*      */                 catch (Throwable localThrowable11)
/*      */                 {
/*  932 */                   localThrowable30 = localThrowable11;throw localThrowable11; } finally {} } catch (Throwable localThrowable14) { localThrowable29 = localThrowable14;throw localThrowable14; } finally {} } catch (Throwable localThrowable17) { localThrowable28 = localThrowable17;throw localThrowable17;
/*      */             }
/*      */             finally {}
/*      */           }
/*      */           catch (Throwable localThrowable27)
/*      */           {
/*  930 */             localThrowable23 = localThrowable27;throw localThrowable27;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           }
/*      */           finally
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  945 */             if (jar != null) if (localThrowable23 != null) try { jar.close(); } catch (Throwable localThrowable21) { localThrowable23.addSuppressed(localThrowable21); } else { jar.close();
/*      */               }
/*      */           }
/*      */         }
/*      */         catch (IOException localIOException1) {}
/*      */       }
/*      */     }
/*  952 */     DeployedApplication deployedApp = new DeployedApplication(cn.getName(), (xml.exists()) && (deployThisXML) && (copyThisXml));
/*      */     
/*  954 */     long startTime = 0L;
/*      */     
/*  956 */     if (log.isInfoEnabled()) {
/*  957 */       startTime = System.currentTimeMillis();
/*  958 */       log.info(sm.getString("hostConfig.deployWar", new Object[] {war
/*  959 */         .getAbsolutePath() }));
/*      */     }
/*      */     
/*      */ 
/*      */     try
/*      */     {
/*  965 */       deployedApp.redeployResources.put(war.getAbsolutePath(), Long.valueOf(war.lastModified()));
/*      */       
/*  967 */       if ((deployThisXML) && (xml.exists()) && (copyThisXml)) {
/*  968 */         deployedApp.redeployResources.put(xml.getAbsolutePath(), 
/*  969 */           Long.valueOf(xml.lastModified()));
/*      */       }
/*      */       else {
/*  972 */         deployedApp.redeployResources.put(new File(this.host
/*  973 */           .getConfigBaseFile(), cn
/*  974 */           .getBaseName() + ".xml").getAbsolutePath(), 
/*  975 */           Long.valueOf(0L));
/*      */       }
/*      */       
/*  978 */       Class<?> clazz = Class.forName(this.host.getConfigClass());
/*  979 */       LifecycleListener listener = (LifecycleListener)clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/*  980 */       ((Context)context).addLifecycleListener(listener);
/*      */       
/*  982 */       ((Context)context).setName(cn.getName());
/*  983 */       ((Context)context).setPath(cn.getPath());
/*  984 */       ((Context)context).setWebappVersion(cn.getVersion());
/*  985 */       ((Context)context).setDocBase(cn.getBaseName() + ".war");
/*  986 */       this.host.addChild((Container)context); } catch (Throwable t) { boolean unpackWAR;
/*      */       File docBase;
/*  988 */       ExceptionUtils.handleThrowable(t);
/*  989 */       log.error(sm.getString("hostConfig.deployWar.error", new Object[] {war
/*  990 */         .getAbsolutePath() }), t);
/*      */     } finally {
/*      */       boolean unpackWAR;
/*      */       File docBase;
/*  994 */       boolean unpackWAR = this.unpackWARs;
/*  995 */       if ((unpackWAR) && ((context instanceof StandardContext))) {
/*  996 */         unpackWAR = ((StandardContext)context).getUnpackWAR();
/*      */       }
/*  998 */       if ((unpackWAR) && (((Context)context).getDocBase() != null)) {
/*  999 */         File docBase = new File(this.host.getAppBaseFile(), cn.getBaseName());
/* 1000 */         deployedApp.redeployResources.put(docBase.getAbsolutePath(), 
/* 1001 */           Long.valueOf(docBase.lastModified()));
/* 1002 */         addWatchedResources(deployedApp, docBase.getAbsolutePath(), (Context)context);
/*      */         
/* 1004 */         if ((deployThisXML) && (!copyThisXml) && ((xmlInWar) || (xml.exists()))) {
/* 1005 */           deployedApp.redeployResources.put(xml.getAbsolutePath(), 
/* 1006 */             Long.valueOf(xml.lastModified()));
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1011 */         addWatchedResources(deployedApp, null, (Context)context);
/*      */       }
/*      */       
/*      */ 
/* 1015 */       addGlobalRedeployResources(deployedApp);
/*      */     }
/*      */     
/* 1018 */     this.deployed.put(cn.getName(), deployedApp);
/*      */     
/* 1020 */     if (log.isInfoEnabled()) {
/* 1021 */       log.info(sm.getString("hostConfig.deployWar.finished", new Object[] {war
/* 1022 */         .getAbsolutePath(), Long.valueOf(System.currentTimeMillis() - startTime) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void deployDirectories(File appBase, String[] files)
/*      */   {
/* 1034 */     if (files == null) {
/* 1035 */       return;
/*      */     }
/* 1037 */     ExecutorService es = this.host.getStartStopExecutor();
/* 1038 */     List<Future<?>> results = new ArrayList();
/*      */     
/* 1040 */     for (int i = 0; i < files.length; i++)
/*      */     {
/* 1042 */       if (!files[i].equalsIgnoreCase("META-INF"))
/*      */       {
/* 1044 */         if (!files[i].equalsIgnoreCase("WEB-INF"))
/*      */         {
/* 1046 */           File dir = new File(appBase, files[i]);
/* 1047 */           if (dir.isDirectory()) {
/* 1048 */             ContextName cn = new ContextName(files[i], false);
/*      */             
/* 1050 */             if ((!isServiced(cn.getName())) && (!deploymentExists(cn.getName())))
/*      */             {
/*      */ 
/* 1053 */               results.add(es.submit(new DeployDirectory(this, cn, dir))); }
/*      */           }
/*      */         } }
/*      */     }
/* 1057 */     for (Future<?> result : results) {
/*      */       try {
/* 1059 */         result.get();
/*      */       } catch (Exception e) {
/* 1061 */         log.error(sm.getString("hostConfig.deployDir.threaded.error"), e);
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
/*      */   protected void deployDirectory(ContextName cn, File dir)
/*      */   {
/* 1076 */     long startTime = 0L;
/*      */     
/* 1078 */     if (log.isInfoEnabled()) {
/* 1079 */       startTime = System.currentTimeMillis();
/* 1080 */       log.info(sm.getString("hostConfig.deployDir", new Object[] {dir
/* 1081 */         .getAbsolutePath() }));
/*      */     }
/*      */     
/* 1084 */     Context context = null;
/* 1085 */     File xml = new File(dir, "META-INF/context.xml");
/*      */     
/* 1087 */     File xmlCopy = new File(this.host.getConfigBaseFile(), cn.getBaseName() + ".xml");
/*      */     
/*      */ 
/*      */ 
/* 1091 */     boolean copyThisXml = isCopyXML();
/* 1092 */     boolean deployThisXML = isDeployThisXML(dir, cn);
/*      */     DeployedApplication deployedApp;
/*      */     try {
/* 1095 */       if ((deployThisXML) && (xml.exists())) {
/* 1096 */         synchronized (this.digesterLock) {
/*      */           try {
/* 1098 */             context = (Context)this.digester.parse(xml);
/*      */           } catch (Exception e) {
/* 1100 */             log.error(sm.getString("hostConfig.deployDescriptor.error", new Object[] { xml }), e);
/*      */             
/*      */ 
/* 1103 */             context = new FailedContext();
/*      */           } finally {
/* 1105 */             this.digester.reset();
/* 1106 */             if (context == null) {
/* 1107 */               context = new FailedContext();
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 1112 */         if ((!copyThisXml) && ((context instanceof StandardContext)))
/*      */         {
/* 1114 */           copyThisXml = ((StandardContext)context).getCopyXML();
/*      */         }
/*      */         
/* 1117 */         if (copyThisXml) {
/* 1118 */           Files.copy(xml.toPath(), xmlCopy.toPath(), new CopyOption[0]);
/* 1119 */           context.setConfigFile(xmlCopy.toURI().toURL());
/*      */         } else {
/* 1121 */           context.setConfigFile(xml.toURI().toURL());
/*      */         }
/* 1123 */       } else if ((!deployThisXML) && (xml.exists()))
/*      */       {
/*      */ 
/* 1126 */         log.error(sm.getString("hostConfig.deployDescriptor.blocked", new Object[] {cn
/* 1127 */           .getPath(), xml, xmlCopy }));
/* 1128 */         context = new FailedContext();
/*      */       } else {
/* 1130 */         context = (Context)Class.forName(this.contextClass).getConstructor(new Class[0]).newInstance(new Object[0]);
/*      */       }
/*      */       
/* 1133 */       Class<?> clazz = Class.forName(this.host.getConfigClass());
/* 1134 */       LifecycleListener listener = (LifecycleListener)clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/* 1135 */       context.addLifecycleListener(listener);
/*      */       
/* 1137 */       context.setName(cn.getName());
/* 1138 */       context.setPath(cn.getPath());
/* 1139 */       context.setWebappVersion(cn.getVersion());
/* 1140 */       context.setDocBase(cn.getBaseName());
/* 1141 */       this.host.addChild(context);
/*      */     } catch (Throwable t) { DeployedApplication deployedApp;
/* 1143 */       ExceptionUtils.handleThrowable(t);
/* 1144 */       log.error(sm.getString("hostConfig.deployDir.error", new Object[] {dir
/* 1145 */         .getAbsolutePath() }), t);
/*      */     } finally {
/*      */       DeployedApplication deployedApp;
/* 1148 */       deployedApp = new DeployedApplication(cn.getName(), (xml.exists()) && (deployThisXML) && (copyThisXml));
/*      */       
/*      */ 
/*      */ 
/* 1152 */       deployedApp.redeployResources.put(dir.getAbsolutePath() + ".war", 
/* 1153 */         Long.valueOf(0L));
/* 1154 */       deployedApp.redeployResources.put(dir.getAbsolutePath(), 
/* 1155 */         Long.valueOf(dir.lastModified()));
/* 1156 */       if ((deployThisXML) && (xml.exists())) {
/* 1157 */         if (copyThisXml) {
/* 1158 */           deployedApp.redeployResources.put(xmlCopy
/* 1159 */             .getAbsolutePath(), 
/* 1160 */             Long.valueOf(xmlCopy.lastModified()));
/*      */         } else {
/* 1162 */           deployedApp.redeployResources.put(xml
/* 1163 */             .getAbsolutePath(), 
/* 1164 */             Long.valueOf(xml.lastModified()));
/*      */           
/*      */ 
/* 1167 */           deployedApp.redeployResources.put(xmlCopy
/* 1168 */             .getAbsolutePath(), 
/* 1169 */             Long.valueOf(0L));
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1174 */         deployedApp.redeployResources.put(xmlCopy
/* 1175 */           .getAbsolutePath(), 
/* 1176 */           Long.valueOf(0L));
/* 1177 */         if (!xml.exists()) {
/* 1178 */           deployedApp.redeployResources.put(xml
/* 1179 */             .getAbsolutePath(), 
/* 1180 */             Long.valueOf(0L));
/*      */         }
/*      */       }
/* 1183 */       addWatchedResources(deployedApp, dir.getAbsolutePath(), context);
/*      */       
/*      */ 
/* 1186 */       addGlobalRedeployResources(deployedApp);
/*      */     }
/*      */     
/* 1189 */     this.deployed.put(cn.getName(), deployedApp);
/*      */     
/* 1191 */     if (log.isInfoEnabled()) {
/* 1192 */       log.info(sm.getString("hostConfig.deployDir.finished", new Object[] {dir
/* 1193 */         .getAbsolutePath(), Long.valueOf(System.currentTimeMillis() - startTime) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean deploymentExists(String contextName)
/*      */   {
/* 1205 */     return (this.deployed.containsKey(contextName)) || 
/* 1206 */       (this.host.findChild(contextName) != null);
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
/*      */   protected void addWatchedResources(DeployedApplication app, String docBase, Context context)
/*      */   {
/* 1221 */     File docBaseFile = null;
/* 1222 */     if (docBase != null) {
/* 1223 */       docBaseFile = new File(docBase);
/* 1224 */       if (!docBaseFile.isAbsolute()) {
/* 1225 */         docBaseFile = new File(this.host.getAppBaseFile(), docBase);
/*      */       }
/*      */     }
/* 1228 */     String[] watchedResources = context.findWatchedResources();
/* 1229 */     for (int i = 0; i < watchedResources.length; i++) {
/* 1230 */       File resource = new File(watchedResources[i]);
/* 1231 */       if (!resource.isAbsolute()) {
/* 1232 */         if (docBase != null) {
/* 1233 */           resource = new File(docBaseFile, watchedResources[i]);
/*      */         } else {
/* 1235 */           if (!log.isDebugEnabled()) continue;
/* 1236 */           log.debug("Ignoring non-existent WatchedResource '" + resource
/* 1237 */             .getAbsolutePath() + "'"); continue;
/*      */         }
/*      */       }
/*      */       
/* 1241 */       if (log.isDebugEnabled())
/* 1242 */         log.debug("Watching WatchedResource '" + resource
/* 1243 */           .getAbsolutePath() + "'");
/* 1244 */       app.reloadResources.put(resource.getAbsolutePath(), 
/* 1245 */         Long.valueOf(resource.lastModified()));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void addGlobalRedeployResources(DeployedApplication app)
/*      */   {
/* 1253 */     File hostContextXml = new File(getConfigBaseName(), "context.xml.default");
/* 1254 */     if (hostContextXml.isFile()) {
/* 1255 */       app.redeployResources.put(hostContextXml.getAbsolutePath(), 
/* 1256 */         Long.valueOf(hostContextXml.lastModified()));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1261 */     File globalContextXml = returnCanonicalPath("conf/context.xml");
/* 1262 */     if (globalContextXml.isFile()) {
/* 1263 */       app.redeployResources.put(globalContextXml.getAbsolutePath(), 
/* 1264 */         Long.valueOf(globalContextXml.lastModified()));
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
/*      */ 
/*      */ 
/*      */   protected synchronized void checkResources(DeployedApplication app, boolean skipFileModificationResolutionCheck)
/*      */   {
/* 1282 */     String[] resources = (String[])app.redeployResources.keySet().toArray(new String[0]);
/*      */     
/*      */ 
/* 1285 */     long currentTimeWithResolutionOffset = System.currentTimeMillis() - 1000L;
/* 1286 */     for (int i = 0; i < resources.length; i++) {
/* 1287 */       File resource = new File(resources[i]);
/* 1288 */       if (log.isDebugEnabled()) {
/* 1289 */         log.debug("Checking context[" + app.name + "] redeploy resource " + resource);
/*      */       }
/*      */       
/* 1292 */       long lastModified = ((Long)app.redeployResources.get(resources[i])).longValue();
/* 1293 */       if ((resource.exists()) || (lastModified == 0L))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 1298 */         if ((resource.lastModified() != lastModified) && ((!this.host.getAutoDeploy()) || 
/* 1299 */           (resource.lastModified() < currentTimeWithResolutionOffset) || (skipFileModificationResolutionCheck)))
/*      */         {
/* 1301 */           if (resource.isDirectory())
/*      */           {
/* 1303 */             app.redeployResources.put(resources[i], 
/* 1304 */               Long.valueOf(resource.lastModified()));
/* 1305 */           } else { if (app.hasDescriptor)
/*      */             {
/* 1307 */               if (resource.getName().toLowerCase(Locale.ENGLISH).endsWith(".war"))
/*      */               {
/*      */ 
/*      */ 
/*      */ 
/* 1312 */                 Context context = (Context)this.host.findChild(app.name);
/* 1313 */                 String docBase = context.getDocBase();
/* 1314 */                 if (!docBase.toLowerCase(Locale.ENGLISH).endsWith(".war"))
/*      */                 {
/* 1316 */                   File docBaseFile = new File(docBase);
/* 1317 */                   if (!docBaseFile.isAbsolute()) {
/* 1318 */                     docBaseFile = new File(this.host.getAppBaseFile(), docBase);
/*      */                   }
/*      */                   
/* 1321 */                   reload(app, docBaseFile, resource.getAbsolutePath());
/*      */                 } else {
/* 1323 */                   reload(app, null, null);
/*      */                 }
/*      */                 
/* 1326 */                 app.redeployResources.put(resources[i], 
/* 1327 */                   Long.valueOf(resource.lastModified()));
/* 1328 */                 app.timestamp = System.currentTimeMillis();
/* 1329 */                 boolean unpackWAR = this.unpackWARs;
/* 1330 */                 if ((unpackWAR) && ((context instanceof StandardContext))) {
/* 1331 */                   unpackWAR = ((StandardContext)context).getUnpackWAR();
/*      */                 }
/* 1333 */                 if (unpackWAR) {
/* 1334 */                   addWatchedResources(app, context.getDocBase(), context);
/*      */                 } else {
/* 1336 */                   addWatchedResources(app, null, context);
/*      */                 }
/* 1338 */                 return;
/*      */               }
/*      */             }
/*      */             
/* 1342 */             undeploy(app);
/* 1343 */             deleteRedeployResources(app, resources, i, false);
/*      */           }
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*      */         try
/*      */         {
/* 1351 */           Thread.sleep(500L);
/*      */         }
/*      */         catch (InterruptedException localInterruptedException) {}
/*      */         
/*      */ 
/* 1356 */         if (!resource.exists())
/*      */         {
/*      */ 
/*      */ 
/* 1360 */           undeploy(app);
/* 1361 */           deleteRedeployResources(app, resources, i, true);
/* 1362 */           return;
/*      */         }
/*      */       } }
/* 1365 */     resources = (String[])app.reloadResources.keySet().toArray(new String[0]);
/* 1366 */     boolean update = false;
/* 1367 */     for (int i = 0; i < resources.length; i++) {
/* 1368 */       File resource = new File(resources[i]);
/* 1369 */       if (log.isDebugEnabled()) {
/* 1370 */         log.debug("Checking context[" + app.name + "] reload resource " + resource);
/*      */       }
/* 1372 */       long lastModified = ((Long)app.reloadResources.get(resources[i])).longValue();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1377 */       if (((resource.lastModified() != lastModified) && (
/* 1378 */         (!this.host.getAutoDeploy()) || 
/* 1379 */         (resource.lastModified() < currentTimeWithResolutionOffset) || (skipFileModificationResolutionCheck))) || (update))
/*      */       {
/*      */ 
/* 1382 */         if (!update)
/*      */         {
/* 1384 */           reload(app, null, null);
/* 1385 */           update = true;
/*      */         }
/*      */         
/*      */ 
/* 1389 */         app.reloadResources.put(resources[i], 
/* 1390 */           Long.valueOf(resource.lastModified()));
/*      */       }
/* 1392 */       app.timestamp = System.currentTimeMillis();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void reload(DeployedApplication app, File fileToRemove, String newDocBase)
/*      */   {
/* 1402 */     if (log.isInfoEnabled())
/* 1403 */       log.info(sm.getString("hostConfig.reload", new Object[] { app.name }));
/* 1404 */     Context context = (Context)this.host.findChild(app.name);
/* 1405 */     if (context.getState().isAvailable()) {
/* 1406 */       if ((fileToRemove != null) && (newDocBase != null)) {
/* 1407 */         context.addLifecycleListener(new ExpandedDirectoryRemovalListener(fileToRemove, newDocBase));
/*      */       }
/*      */       
/*      */ 
/* 1411 */       context.reload();
/*      */     }
/*      */     else
/*      */     {
/* 1415 */       if ((fileToRemove != null) && (newDocBase != null)) {
/* 1416 */         ExpandWar.delete(fileToRemove);
/* 1417 */         context.setDocBase(newDocBase);
/*      */       }
/*      */       try {
/* 1420 */         context.start();
/*      */       } catch (Exception e) {
/* 1422 */         log.warn(sm
/* 1423 */           .getString("hostConfig.context.restart", new Object[] { app.name }), e);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void undeploy(DeployedApplication app)
/*      */   {
/* 1430 */     if (log.isInfoEnabled())
/* 1431 */       log.info(sm.getString("hostConfig.undeploy", new Object[] { app.name }));
/* 1432 */     Container context = this.host.findChild(app.name);
/*      */     try {
/* 1434 */       this.host.removeChild(context);
/*      */     } catch (Throwable t) {
/* 1436 */       ExceptionUtils.handleThrowable(t);
/* 1437 */       log.warn(sm
/* 1438 */         .getString("hostConfig.context.remove", new Object[] { app.name }), t);
/*      */     }
/* 1440 */     this.deployed.remove(app.name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void deleteRedeployResources(DeployedApplication app, String[] resources, int i, boolean deleteReloadResources)
/*      */   {
/* 1448 */     for (int j = i + 1; j < resources.length; j++) {
/* 1449 */       File current = new File(resources[j]);
/*      */       
/* 1451 */       if (!"context.xml.default".equals(current.getName()))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 1456 */         if (isDeletableResource(app, current)) {
/* 1457 */           if (log.isDebugEnabled()) {
/* 1458 */             log.debug("Delete " + current);
/*      */           }
/* 1460 */           ExpandWar.delete(current);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1465 */     if (deleteReloadResources) {
/* 1466 */       String[] resources2 = (String[])app.reloadResources.keySet().toArray(new String[0]);
/* 1467 */       for (int j = 0; j < resources2.length; j++) {
/* 1468 */         File current = new File(resources2[j]);
/*      */         
/* 1470 */         if (!"context.xml.default".equals(current.getName()))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 1475 */           if (isDeletableResource(app, current)) {
/* 1476 */             if (log.isDebugEnabled()) {
/* 1477 */               log.debug("Delete " + current);
/*      */             }
/* 1479 */             ExpandWar.delete(current);
/*      */           }
/*      */         }
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
/*      */   private boolean isDeletableResource(DeployedApplication app, File resource)
/*      */   {
/* 1498 */     if (!resource.isAbsolute()) {
/* 1499 */       log.warn(sm.getString("hostConfig.resourceNotAbsolute", new Object[] { app.name, resource }));
/* 1500 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */     try
/*      */     {
/* 1506 */       canonicalLocation = resource.getParentFile().getCanonicalPath();
/*      */     } catch (IOException e) { String canonicalLocation;
/* 1508 */       log.warn(sm.getString("hostConfig.canonicalizing", new Object[] {resource
/* 1509 */         .getParentFile(), app.name }), e);
/* 1510 */       return false;
/*      */     }
/*      */     String canonicalLocation;
/*      */     try
/*      */     {
/* 1515 */       canonicalAppBase = this.host.getAppBaseFile().getCanonicalPath();
/*      */     } catch (IOException e) { String canonicalAppBase;
/* 1517 */       log.warn(sm.getString("hostConfig.canonicalizing", new Object[] {this.host
/* 1518 */         .getAppBaseFile(), app.name }), e);
/* 1519 */       return false;
/*      */     }
/*      */     String canonicalAppBase;
/* 1522 */     if (canonicalLocation.equals(canonicalAppBase))
/*      */     {
/* 1524 */       return true;
/*      */     }
/*      */     
/*      */     try
/*      */     {
/* 1529 */       canonicalConfigBase = this.host.getConfigBaseFile().getCanonicalPath();
/*      */     } catch (IOException e) { String canonicalConfigBase;
/* 1531 */       log.warn(sm.getString("hostConfig.canonicalizing", new Object[] {this.host
/* 1532 */         .getConfigBaseFile(), app.name }), e);
/* 1533 */       return false;
/*      */     }
/*      */     String canonicalConfigBase;
/* 1536 */     if ((canonicalLocation.equals(canonicalConfigBase)) && 
/* 1537 */       (resource.getName().endsWith(".xml")))
/*      */     {
/* 1539 */       return true;
/*      */     }
/*      */     
/*      */ 
/* 1543 */     return false;
/*      */   }
/*      */   
/*      */   public void beforeStart()
/*      */   {
/* 1548 */     if (this.host.getCreateDirs()) {
/* 1549 */       File[] dirs = { this.host.getAppBaseFile(), this.host.getConfigBaseFile() };
/* 1550 */       for (int i = 0; i < dirs.length; i++) {
/* 1551 */         if ((!dirs[i].mkdirs()) && (!dirs[i].isDirectory())) {
/* 1552 */           log.error(sm.getString("hostConfig.createDirs", new Object[] { dirs[i] }));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void start()
/*      */   {
/* 1564 */     if (log.isDebugEnabled()) {
/* 1565 */       log.debug(sm.getString("hostConfig.start"));
/*      */     }
/*      */     try {
/* 1568 */       ObjectName hostON = this.host.getObjectName();
/*      */       
/* 1570 */       this.oname = new ObjectName(hostON.getDomain() + ":type=Deployer,host=" + this.host.getName());
/* 1571 */       Registry.getRegistry(null, null)
/* 1572 */         .registerComponent(this, this.oname, getClass().getName());
/*      */     } catch (Exception e) {
/* 1574 */       log.error(sm.getString("hostConfig.jmx.register", new Object[] { this.oname }), e);
/*      */     }
/*      */     
/* 1577 */     if (!this.host.getAppBaseFile().isDirectory()) {
/* 1578 */       log.error(sm.getString("hostConfig.appBase", new Object[] { this.host.getName(), this.host
/* 1579 */         .getAppBaseFile().getPath() }));
/* 1580 */       this.host.setDeployOnStartup(false);
/* 1581 */       this.host.setAutoDeploy(false);
/*      */     }
/*      */     
/* 1584 */     if (this.host.getDeployOnStartup()) {
/* 1585 */       deployApps();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void stop()
/*      */   {
/* 1595 */     if (log.isDebugEnabled()) {
/* 1596 */       log.debug(sm.getString("hostConfig.stop"));
/*      */     }
/* 1598 */     if (this.oname != null) {
/*      */       try {
/* 1600 */         Registry.getRegistry(null, null).unregisterComponent(this.oname);
/*      */       } catch (Exception e) {
/* 1602 */         log.error(sm.getString("hostConfig.jmx.unregister", new Object[] { this.oname }), e);
/*      */       }
/*      */     }
/* 1605 */     this.oname = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void check()
/*      */   {
/* 1614 */     if (this.host.getAutoDeploy())
/*      */     {
/*      */ 
/* 1617 */       DeployedApplication[] apps = (DeployedApplication[])this.deployed.values().toArray(new DeployedApplication[0]);
/* 1618 */       for (int i = 0; i < apps.length; i++) {
/* 1619 */         if (!isServiced(apps[i].name)) {
/* 1620 */           checkResources(apps[i], false);
/*      */         }
/*      */       }
/*      */       
/* 1624 */       if (this.host.getUndeployOldVersions()) {
/* 1625 */         checkUndeploy();
/*      */       }
/*      */       
/*      */ 
/* 1629 */       deployApps();
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
/*      */ 
/*      */ 
/*      */   public void check(String name)
/*      */   {
/* 1647 */     DeployedApplication app = (DeployedApplication)this.deployed.get(name);
/* 1648 */     if (app != null) {
/* 1649 */       checkResources(app, true);
/*      */     }
/* 1651 */     deployApps(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void checkUndeploy()
/*      */   {
/* 1660 */     SortedSet<String> sortedAppNames = new TreeSet();
/* 1661 */     sortedAppNames.addAll(this.deployed.keySet());
/*      */     
/* 1663 */     if (sortedAppNames.size() < 2) {
/* 1664 */       return;
/*      */     }
/* 1666 */     Iterator<String> iter = sortedAppNames.iterator();
/*      */     
/* 1668 */     ContextName previous = new ContextName((String)iter.next(), false);
/*      */     do {
/* 1670 */       ContextName current = new ContextName((String)iter.next(), false);
/*      */       
/* 1672 */       if (current.getPath().equals(previous.getPath()))
/*      */       {
/*      */ 
/* 1675 */         Context previousContext = (Context)this.host.findChild(previous.getName());
/* 1676 */         Context currentContext = (Context)this.host.findChild(current.getName());
/* 1677 */         if ((previousContext != null) && (currentContext != null) && 
/* 1678 */           (currentContext.getState().isAvailable()) && 
/* 1679 */           (!isServiced(previous.getName()))) {
/* 1680 */           Manager manager = previousContext.getManager();
/* 1681 */           if (manager != null) { int sessionCount;
/*      */             int sessionCount;
/* 1683 */             if ((manager instanceof DistributedManager)) {
/* 1684 */               sessionCount = ((DistributedManager)manager).getActiveSessionsFull();
/*      */             } else {
/* 1686 */               sessionCount = manager.getActiveSessions();
/*      */             }
/* 1688 */             if (sessionCount == 0) {
/* 1689 */               if (log.isInfoEnabled()) {
/* 1690 */                 log.info(sm.getString("hostConfig.undeployVersion", new Object[] {previous
/* 1691 */                   .getName() }));
/*      */               }
/* 1693 */               DeployedApplication app = (DeployedApplication)this.deployed.get(previous.getName());
/* 1694 */               String[] resources = (String[])app.redeployResources.keySet().toArray(new String[0]);
/*      */               
/*      */ 
/*      */ 
/* 1698 */               undeploy(app);
/* 1699 */               deleteRedeployResources(app, resources, -1, true);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1704 */       previous = current;
/* 1705 */     } while (iter.hasNext());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void manageApp(Context context)
/*      */   {
/* 1715 */     String contextName = context.getName();
/*      */     
/* 1717 */     if (this.deployed.containsKey(contextName)) {
/* 1718 */       return;
/*      */     }
/* 1720 */     DeployedApplication deployedApp = new DeployedApplication(contextName, false);
/*      */     
/*      */ 
/*      */ 
/* 1724 */     boolean isWar = false;
/* 1725 */     if (context.getDocBase() != null) {
/* 1726 */       File docBase = new File(context.getDocBase());
/* 1727 */       if (!docBase.isAbsolute()) {
/* 1728 */         docBase = new File(this.host.getAppBaseFile(), context.getDocBase());
/*      */       }
/* 1730 */       deployedApp.redeployResources.put(docBase.getAbsolutePath(), 
/* 1731 */         Long.valueOf(docBase.lastModified()));
/* 1732 */       if (docBase.getAbsolutePath().toLowerCase(Locale.ENGLISH).endsWith(".war")) {
/* 1733 */         isWar = true;
/*      */       }
/*      */     }
/* 1736 */     this.host.addChild(context);
/*      */     
/*      */ 
/* 1739 */     boolean unpackWAR = this.unpackWARs;
/* 1740 */     if ((unpackWAR) && ((context instanceof StandardContext))) {
/* 1741 */       unpackWAR = ((StandardContext)context).getUnpackWAR();
/*      */     }
/* 1743 */     if ((isWar) && (unpackWAR)) {
/* 1744 */       File docBase = new File(this.host.getAppBaseFile(), context.getBaseName());
/* 1745 */       deployedApp.redeployResources.put(docBase.getAbsolutePath(), 
/* 1746 */         Long.valueOf(docBase.lastModified()));
/* 1747 */       addWatchedResources(deployedApp, docBase.getAbsolutePath(), context);
/*      */     } else {
/* 1749 */       addWatchedResources(deployedApp, null, context);
/*      */     }
/* 1751 */     this.deployed.put(contextName, deployedApp);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void unmanageApp(String contextName)
/*      */   {
/* 1760 */     if (isServiced(contextName)) {
/* 1761 */       this.deployed.remove(contextName);
/* 1762 */       this.host.removeChild(this.host.findChild(contextName));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected static class DeployedApplication
/*      */   {
/*      */     public final String name;
/*      */     
/*      */     public final boolean hasDescriptor;
/*      */     
/*      */     public DeployedApplication(String name, boolean hasDescriptor)
/*      */     {
/* 1775 */       this.name = name;
/* 1776 */       this.hasDescriptor = hasDescriptor;
/*      */     }
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
/* 1798 */     public final LinkedHashMap<String, Long> redeployResources = new LinkedHashMap();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1808 */     public final HashMap<String, Long> reloadResources = new HashMap();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1813 */     public long timestamp = System.currentTimeMillis();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1821 */     public boolean loggedDirWarning = false;
/*      */   }
/*      */   
/*      */   private static class DeployDescriptor implements Runnable
/*      */   {
/*      */     private HostConfig config;
/*      */     private ContextName cn;
/*      */     private File descriptor;
/*      */     
/*      */     public DeployDescriptor(HostConfig config, ContextName cn, File descriptor)
/*      */     {
/* 1832 */       this.config = config;
/* 1833 */       this.cn = cn;
/* 1834 */       this.descriptor = descriptor;
/*      */     }
/*      */     
/*      */     public void run()
/*      */     {
/* 1839 */       this.config.deployDescriptor(this.cn, this.descriptor);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class DeployWar implements Runnable
/*      */   {
/*      */     private HostConfig config;
/*      */     private ContextName cn;
/*      */     private File war;
/*      */     
/*      */     public DeployWar(HostConfig config, ContextName cn, File war) {
/* 1850 */       this.config = config;
/* 1851 */       this.cn = cn;
/* 1852 */       this.war = war;
/*      */     }
/*      */     
/*      */     public void run()
/*      */     {
/* 1857 */       this.config.deployWAR(this.cn, this.war);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class DeployDirectory implements Runnable
/*      */   {
/*      */     private HostConfig config;
/*      */     private ContextName cn;
/*      */     private File dir;
/*      */     
/*      */     public DeployDirectory(HostConfig config, ContextName cn, File dir) {
/* 1868 */       this.config = config;
/* 1869 */       this.cn = cn;
/* 1870 */       this.dir = dir;
/*      */     }
/*      */     
/*      */     public void run()
/*      */     {
/* 1875 */       this.config.deployDirectory(this.cn, this.dir);
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
/*      */   private static class ExpandedDirectoryRemovalListener
/*      */     implements LifecycleListener
/*      */   {
/*      */     private final File toDelete;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private final String newDocBase;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public ExpandedDirectoryRemovalListener(File toDelete, String newDocBase)
/*      */     {
/* 1906 */       this.toDelete = toDelete;
/* 1907 */       this.newDocBase = newDocBase;
/*      */     }
/*      */     
/*      */     public void lifecycleEvent(LifecycleEvent event)
/*      */     {
/* 1912 */       if ("after_stop".equals(event.getType()))
/*      */       {
/* 1914 */         Context context = (Context)event.getLifecycle();
/*      */         
/*      */ 
/* 1917 */         ExpandWar.delete(this.toDelete);
/*      */         
/*      */ 
/* 1920 */         context.setDocBase(this.newDocBase);
/*      */         
/*      */ 
/*      */ 
/* 1924 */         context.removeLifecycleListener(this);
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\HostConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */