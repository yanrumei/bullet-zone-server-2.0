/*     */ package org.apache.catalina.core;
/*     */ 
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.management.ObjectName;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.apache.catalina.Container;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.Engine;
/*     */ import org.apache.catalina.Globals;
/*     */ import org.apache.catalina.Host;
/*     */ import org.apache.catalina.JmxEnabled;
/*     */ import org.apache.catalina.LifecycleEvent;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.LifecycleListener;
/*     */ import org.apache.catalina.LifecycleState;
/*     */ import org.apache.catalina.Loader;
/*     */ import org.apache.catalina.Pipeline;
/*     */ import org.apache.catalina.Valve;
/*     */ import org.apache.catalina.loader.WebappClassLoaderBase;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
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
/*     */ public class StandardHost
/*     */   extends ContainerBase
/*     */   implements Host
/*     */ {
/*  57 */   private static final Log log = LogFactory.getLog(StandardHost.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StandardHost()
/*     */   {
/*  68 */     this.pipeline.setBasic(new StandardHostValve());
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
/*  79 */   private String[] aliases = new String[0];
/*     */   
/*  81 */   private final Object aliasesLock = new Object();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  87 */   private String appBase = "webapps";
/*  88 */   private volatile File appBaseFile = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  93 */   private String xmlBase = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  98 */   private volatile File hostConfigBase = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 103 */   private boolean autoDeploy = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 110 */   private String configClass = "org.apache.catalina.startup.ContextConfig";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 118 */   private String contextClass = "org.apache.catalina.core.StandardContext";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 125 */   private boolean deployOnStartup = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 131 */   private boolean deployXML = !Globals.IS_SECURITY_ENABLED;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 139 */   private boolean copyXML = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 146 */   private String errorReportValveClass = "org.apache.catalina.valves.ErrorReportValve";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 153 */   private boolean unpackWARs = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 159 */   private String workDir = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 165 */   private boolean createDirs = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 172 */   private final Map<ClassLoader, String> childClassLoaders = new WeakHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 181 */   private Pattern deployIgnore = null;
/*     */   
/*     */ 
/* 184 */   private boolean undeployOldVersions = false;
/*     */   
/* 186 */   private boolean failCtxIfServletStartFails = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getUndeployOldVersions()
/*     */   {
/* 193 */     return this.undeployOldVersions;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setUndeployOldVersions(boolean undeployOldVersions)
/*     */   {
/* 199 */     this.undeployOldVersions = undeployOldVersions;
/*     */   }
/*     */   
/*     */ 
/*     */   public ExecutorService getStartStopExecutor()
/*     */   {
/* 205 */     return this.startStopExecutor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getAppBase()
/*     */   {
/* 215 */     return this.appBase;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public File getAppBaseFile()
/*     */   {
/* 225 */     if (this.appBaseFile != null) {
/* 226 */       return this.appBaseFile;
/*     */     }
/*     */     
/* 229 */     File file = new File(getAppBase());
/*     */     
/*     */ 
/* 232 */     if (!file.isAbsolute()) {
/* 233 */       file = new File(getCatalinaBase(), file.getPath());
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 238 */       file = file.getCanonicalFile();
/*     */     }
/*     */     catch (IOException localIOException) {}
/*     */     
/*     */ 
/* 243 */     this.appBaseFile = file;
/* 244 */     return file;
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
/*     */   public void setAppBase(String appBase)
/*     */   {
/* 257 */     if (appBase.trim().equals("")) {
/* 258 */       log.warn(sm.getString("standardHost.problematicAppBase", new Object[] { getName() }));
/*     */     }
/* 260 */     String oldAppBase = this.appBase;
/* 261 */     this.appBase = appBase;
/* 262 */     this.support.firePropertyChange("appBase", oldAppBase, this.appBase);
/* 263 */     this.appBaseFile = null;
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
/*     */   public String getXmlBase()
/*     */   {
/* 276 */     return this.xmlBase;
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
/*     */   public void setXmlBase(String xmlBase)
/*     */   {
/* 292 */     String oldXmlBase = this.xmlBase;
/* 293 */     this.xmlBase = xmlBase;
/* 294 */     this.support.firePropertyChange("xmlBase", oldXmlBase, this.xmlBase);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public File getConfigBaseFile()
/*     */   {
/* 304 */     if (this.hostConfigBase != null) {
/* 305 */       return this.hostConfigBase;
/*     */     }
/* 307 */     String path = null;
/* 308 */     if (getXmlBase() != null) {
/* 309 */       path = getXmlBase();
/*     */     } else {
/* 311 */       StringBuilder xmlDir = new StringBuilder("conf");
/* 312 */       Container parent = getParent();
/* 313 */       if ((parent instanceof Engine)) {
/* 314 */         xmlDir.append('/');
/* 315 */         xmlDir.append(parent.getName());
/*     */       }
/* 317 */       xmlDir.append('/');
/* 318 */       xmlDir.append(getName());
/* 319 */       path = xmlDir.toString();
/*     */     }
/* 321 */     File file = new File(path);
/* 322 */     if (!file.isAbsolute())
/* 323 */       file = new File(getCatalinaBase(), path);
/*     */     try {
/* 325 */       file = file.getCanonicalFile();
/*     */     }
/*     */     catch (IOException localIOException) {}
/* 328 */     this.hostConfigBase = file;
/* 329 */     return file;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getCreateDirs()
/*     */   {
/* 339 */     return this.createDirs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCreateDirs(boolean createDirs)
/*     */   {
/* 348 */     this.createDirs = createDirs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getAutoDeploy()
/*     */   {
/* 358 */     return this.autoDeploy;
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
/*     */   public void setAutoDeploy(boolean autoDeploy)
/*     */   {
/* 371 */     boolean oldAutoDeploy = this.autoDeploy;
/* 372 */     this.autoDeploy = autoDeploy;
/* 373 */     this.support.firePropertyChange("autoDeploy", oldAutoDeploy, this.autoDeploy);
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
/*     */   public String getConfigClass()
/*     */   {
/* 386 */     return this.configClass;
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
/*     */   public void setConfigClass(String configClass)
/*     */   {
/* 400 */     String oldConfigClass = this.configClass;
/* 401 */     this.configClass = configClass;
/* 402 */     this.support.firePropertyChange("configClass", oldConfigClass, this.configClass);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getContextClass()
/*     */   {
/* 414 */     return this.contextClass;
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
/*     */   public void setContextClass(String contextClass)
/*     */   {
/* 427 */     String oldContextClass = this.contextClass;
/* 428 */     this.contextClass = contextClass;
/* 429 */     this.support.firePropertyChange("contextClass", oldContextClass, this.contextClass);
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
/*     */   public boolean getDeployOnStartup()
/*     */   {
/* 443 */     return this.deployOnStartup;
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
/*     */   public void setDeployOnStartup(boolean deployOnStartup)
/*     */   {
/* 456 */     boolean oldDeployOnStartup = this.deployOnStartup;
/* 457 */     this.deployOnStartup = deployOnStartup;
/* 458 */     this.support.firePropertyChange("deployOnStartup", oldDeployOnStartup, this.deployOnStartup);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isDeployXML()
/*     */   {
/* 469 */     return this.deployXML;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDeployXML(boolean deployXML)
/*     */   {
/* 481 */     this.deployXML = deployXML;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCopyXML()
/*     */   {
/* 491 */     return this.copyXML;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCopyXML(boolean copyXML)
/*     */   {
/* 503 */     this.copyXML = copyXML;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getErrorReportValveClass()
/*     */   {
/* 514 */     return this.errorReportValveClass;
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
/*     */   public void setErrorReportValveClass(String errorReportValveClass)
/*     */   {
/* 527 */     String oldErrorReportValveClassClass = this.errorReportValveClass;
/* 528 */     this.errorReportValveClass = errorReportValveClass;
/* 529 */     this.support.firePropertyChange("errorReportValveClass", oldErrorReportValveClassClass, this.errorReportValveClass);
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
/*     */   public String getName()
/*     */   {
/* 543 */     return this.name;
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
/*     */   public void setName(String name)
/*     */   {
/* 559 */     if (name == null)
/*     */     {
/* 561 */       throw new IllegalArgumentException(sm.getString("standardHost.nullName"));
/*     */     }
/* 563 */     name = name.toLowerCase(Locale.ENGLISH);
/*     */     
/* 565 */     String oldName = this.name;
/* 566 */     this.name = name;
/* 567 */     this.support.firePropertyChange("name", oldName, this.name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUnpackWARs()
/*     */   {
/* 577 */     return this.unpackWARs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUnpackWARs(boolean unpackWARs)
/*     */   {
/* 589 */     this.unpackWARs = unpackWARs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getWorkDir()
/*     */   {
/* 599 */     return this.workDir;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setWorkDir(String workDir)
/*     */   {
/* 610 */     this.workDir = workDir;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDeployIgnore()
/*     */   {
/* 621 */     if (this.deployIgnore == null) {
/* 622 */       return null;
/*     */     }
/* 624 */     return this.deployIgnore.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Pattern getDeployIgnorePattern()
/*     */   {
/* 635 */     return this.deployIgnore;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDeployIgnore(String deployIgnore)
/*     */   {
/*     */     String oldDeployIgnore;
/*     */     
/*     */ 
/*     */     String oldDeployIgnore;
/*     */     
/*     */ 
/* 649 */     if (this.deployIgnore == null) {
/* 650 */       oldDeployIgnore = null;
/*     */     } else {
/* 652 */       oldDeployIgnore = this.deployIgnore.toString();
/*     */     }
/* 654 */     if (deployIgnore == null) {
/* 655 */       this.deployIgnore = null;
/*     */     } else {
/* 657 */       this.deployIgnore = Pattern.compile(deployIgnore);
/*     */     }
/* 659 */     this.support.firePropertyChange("deployIgnore", oldDeployIgnore, deployIgnore);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isFailCtxIfServletStartFails()
/*     */   {
/* 669 */     return this.failCtxIfServletStartFails;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFailCtxIfServletStartFails(boolean failCtxIfServletStartFails)
/*     */   {
/* 680 */     boolean oldFailCtxIfServletStartFails = this.failCtxIfServletStartFails;
/* 681 */     this.failCtxIfServletStartFails = failCtxIfServletStartFails;
/* 682 */     this.support.firePropertyChange("failCtxIfServletStartFails", oldFailCtxIfServletStartFails, failCtxIfServletStartFails);
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
/*     */   public void addAlias(String alias)
/*     */   {
/* 699 */     alias = alias.toLowerCase(Locale.ENGLISH);
/*     */     
/* 701 */     synchronized (this.aliasesLock)
/*     */     {
/* 703 */       for (int i = 0; i < this.aliases.length; i++) {
/* 704 */         if (this.aliases[i].equals(alias)) {
/* 705 */           return;
/*     */         }
/*     */       }
/* 708 */       String[] newAliases = new String[this.aliases.length + 1];
/* 709 */       for (int i = 0; i < this.aliases.length; i++)
/* 710 */         newAliases[i] = this.aliases[i];
/* 711 */       newAliases[this.aliases.length] = alias;
/* 712 */       this.aliases = newAliases;
/*     */     }
/*     */     
/* 715 */     fireContainerEvent("addAlias", alias);
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
/*     */   public void addChild(Container child)
/*     */   {
/* 729 */     child.addLifecycleListener(new MemoryLeakTrackingListener(null));
/*     */     
/* 731 */     if (!(child instanceof Context))
/*     */     {
/* 733 */       throw new IllegalArgumentException(sm.getString("standardHost.notContext")); }
/* 734 */     super.addChild(child);
/*     */   }
/*     */   
/*     */ 
/*     */   private class MemoryLeakTrackingListener
/*     */     implements LifecycleListener
/*     */   {
/*     */     private MemoryLeakTrackingListener() {}
/*     */     
/*     */ 
/*     */     public void lifecycleEvent(LifecycleEvent event)
/*     */     {
/* 746 */       if ((event.getType().equals("after_start")) && 
/* 747 */         ((event.getSource() instanceof Context))) {
/* 748 */         Context context = (Context)event.getSource();
/* 749 */         StandardHost.this.childClassLoaders.put(context.getLoader().getClassLoader(), context
/* 750 */           .getServletContext().getContextPath());
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
/*     */   public String[] findReloadedContextMemoryLeaks()
/*     */   {
/* 767 */     System.gc();
/*     */     
/* 769 */     List<String> result = new ArrayList();
/*     */     
/*     */ 
/* 772 */     for (Map.Entry<ClassLoader, String> entry : this.childClassLoaders.entrySet()) {
/* 773 */       ClassLoader cl = (ClassLoader)entry.getKey();
/* 774 */       if (((cl instanceof WebappClassLoaderBase)) && 
/* 775 */         (!((WebappClassLoaderBase)cl).getState().isAvailable())) {
/* 776 */         result.add(entry.getValue());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 781 */     return (String[])result.toArray(new String[result.size()]);
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public String[] findAliases()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 7	org/apache/catalina/core/StandardHost:aliasesLock	Ljava/lang/Object;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 4	org/apache/catalina/core/StandardHost:aliases	[Ljava/lang/String;
/*     */     //   11: aload_1
/*     */     //   12: monitorexit
/*     */     //   13: areturn
/*     */     //   14: astore_2
/*     */     //   15: aload_1
/*     */     //   16: monitorexit
/*     */     //   17: aload_2
/*     */     //   18: athrow
/*     */     // Line number table:
/*     */     //   Java source line #791	-> byte code offset #0
/*     */     //   Java source line #792	-> byte code offset #7
/*     */     //   Java source line #793	-> byte code offset #14
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	19	0	this	StandardHost
/*     */     //   5	11	1	Ljava/lang/Object;	Object
/*     */     //   14	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	13	14	finally
/*     */     //   14	17	14	finally
/*     */   }
/*     */   
/*     */   public void removeAlias(String alias)
/*     */   {
/* 806 */     alias = alias.toLowerCase(Locale.ENGLISH);
/*     */     
/* 808 */     synchronized (this.aliasesLock)
/*     */     {
/*     */ 
/* 811 */       int n = -1;
/* 812 */       for (int i = 0; i < this.aliases.length; i++) {
/* 813 */         if (this.aliases[i].equals(alias)) {
/* 814 */           n = i;
/* 815 */           break;
/*     */         }
/*     */       }
/* 818 */       if (n < 0) {
/* 819 */         return;
/*     */       }
/*     */       
/* 822 */       int j = 0;
/* 823 */       String[] results = new String[this.aliases.length - 1];
/* 824 */       for (int i = 0; i < this.aliases.length; i++) {
/* 825 */         if (i != n)
/* 826 */           results[(j++)] = this.aliases[i];
/*     */       }
/* 828 */       this.aliases = results;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 833 */     fireContainerEvent("removeAlias", alias);
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
/*     */   protected synchronized void startInternal()
/*     */     throws LifecycleException
/*     */   {
/* 849 */     String errorValve = getErrorReportValveClass();
/* 850 */     if ((errorValve != null) && (!errorValve.equals(""))) {
/*     */       try {
/* 852 */         boolean found = false;
/* 853 */         Valve[] valves = getPipeline().getValves();
/* 854 */         for (Valve valve : valves) {
/* 855 */           if (errorValve.equals(valve.getClass().getName())) {
/* 856 */             found = true;
/* 857 */             break;
/*     */           }
/*     */         }
/* 860 */         if (!found)
/*     */         {
/* 862 */           Valve valve = (Valve)Class.forName(errorValve).getConstructor(new Class[0]).newInstance(new Object[0]);
/* 863 */           getPipeline().addValve(valve);
/*     */         }
/*     */       } catch (Throwable t) {
/* 866 */         ExceptionUtils.handleThrowable(t);
/* 867 */         log.error(sm.getString("standardHost.invalidErrorReportValveClass", new Object[] { errorValve }), t);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 872 */     super.startInternal();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getValveNames()
/*     */     throws Exception
/*     */   {
/* 883 */     Valve[] valves = getPipeline().getValves();
/* 884 */     String[] mbeanNames = new String[valves.length];
/* 885 */     for (int i = 0; i < valves.length; i++) {
/* 886 */       if ((valves[i] instanceof JmxEnabled)) {
/* 887 */         ObjectName oname = ((JmxEnabled)valves[i]).getObjectName();
/* 888 */         if (oname != null) {
/* 889 */           mbeanNames[i] = oname.toString();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 894 */     return mbeanNames;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public String[] getAliases()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 7	org/apache/catalina/core/StandardHost:aliasesLock	Ljava/lang/Object;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 4	org/apache/catalina/core/StandardHost:aliases	[Ljava/lang/String;
/*     */     //   11: aload_1
/*     */     //   12: monitorexit
/*     */     //   13: areturn
/*     */     //   14: astore_2
/*     */     //   15: aload_1
/*     */     //   16: monitorexit
/*     */     //   17: aload_2
/*     */     //   18: athrow
/*     */     // Line number table:
/*     */     //   Java source line #899	-> byte code offset #0
/*     */     //   Java source line #900	-> byte code offset #7
/*     */     //   Java source line #901	-> byte code offset #14
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	19	0	this	StandardHost
/*     */     //   5	11	1	Ljava/lang/Object;	Object
/*     */     //   14	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	13	14	finally
/*     */     //   14	17	14	finally
/*     */   }
/*     */   
/*     */   protected String getObjectNameKeyProperties()
/*     */   {
/* 907 */     StringBuilder keyProperties = new StringBuilder("type=Host");
/* 908 */     keyProperties.append(getMBeanKeyProperties());
/*     */     
/* 910 */     return keyProperties.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\StandardHost.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */