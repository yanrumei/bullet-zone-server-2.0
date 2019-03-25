/*      */ package org.apache.catalina.core;
/*      */ 
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.beans.PropertyChangeSupport;
/*      */ import java.io.File;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.concurrent.BlockingQueue;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.CopyOnWriteArrayList;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.LinkedBlockingQueue;
/*      */ import java.util.concurrent.ThreadFactory;
/*      */ import java.util.concurrent.ThreadPoolExecutor;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.locks.Lock;
/*      */ import java.util.concurrent.locks.ReadWriteLock;
/*      */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*      */ import javax.management.ObjectName;
/*      */ import org.apache.catalina.AccessLog;
/*      */ import org.apache.catalina.Cluster;
/*      */ import org.apache.catalina.Container;
/*      */ import org.apache.catalina.ContainerEvent;
/*      */ import org.apache.catalina.ContainerListener;
/*      */ import org.apache.catalina.Context;
/*      */ import org.apache.catalina.Engine;
/*      */ import org.apache.catalina.Globals;
/*      */ import org.apache.catalina.Host;
/*      */ import org.apache.catalina.Lifecycle;
/*      */ import org.apache.catalina.LifecycleException;
/*      */ import org.apache.catalina.LifecycleState;
/*      */ import org.apache.catalina.Loader;
/*      */ import org.apache.catalina.Pipeline;
/*      */ import org.apache.catalina.Realm;
/*      */ import org.apache.catalina.Valve;
/*      */ import org.apache.catalina.Wrapper;
/*      */ import org.apache.catalina.connector.Request;
/*      */ import org.apache.catalina.connector.Response;
/*      */ import org.apache.catalina.util.ContextName;
/*      */ import org.apache.catalina.util.LifecycleMBeanBase;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.tomcat.util.ExceptionUtils;
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
/*      */ public abstract class ContainerBase
/*      */   extends LifecycleMBeanBase
/*      */   implements Container
/*      */ {
/*  132 */   private static final Log log = LogFactory.getLog(ContainerBase.class);
/*      */   
/*      */ 
/*      */ 
/*      */   protected class PrivilegedAddChild
/*      */     implements PrivilegedAction<Void>
/*      */   {
/*      */     private final Container child;
/*      */     
/*      */ 
/*      */ 
/*      */     PrivilegedAddChild(Container child)
/*      */     {
/*  145 */       this.child = child;
/*      */     }
/*      */     
/*      */     public Void run()
/*      */     {
/*  150 */       ContainerBase.this.addChildInternal(this.child);
/*  151 */       return null;
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
/*  163 */   protected final HashMap<String, Container> children = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  169 */   protected int backgroundProcessorDelay = -1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  178 */   protected final List<ContainerListener> listeners = new CopyOnWriteArrayList();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  183 */   protected Log logger = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  189 */   protected String logName = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  195 */   protected Cluster cluster = null;
/*  196 */   private final ReadWriteLock clusterLock = new ReentrantReadWriteLock();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  202 */   protected String name = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  208 */   protected Container parent = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  214 */   protected ClassLoader parentClassLoader = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  220 */   protected final Pipeline pipeline = new StandardPipeline(this);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  226 */   private volatile Realm realm = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  232 */   private final ReadWriteLock realmLock = new ReentrantReadWriteLock();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  239 */   protected static final StringManager sm = StringManager.getManager("org.apache.catalina.core");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  245 */   protected boolean startChildren = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  250 */   protected final PropertyChangeSupport support = new PropertyChangeSupport(this);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  257 */   private Thread thread = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  263 */   private volatile boolean threadDone = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  270 */   protected volatile AccessLog accessLog = null;
/*  271 */   private volatile boolean accessLogScanComplete = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  278 */   private int startStopThreads = 1;
/*      */   
/*      */ 
/*      */   protected ThreadPoolExecutor startStopExecutor;
/*      */   
/*      */ 
/*      */   public int getStartStopThreads()
/*      */   {
/*  286 */     return this.startStopThreads;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private int getStartStopThreadsInternal()
/*      */   {
/*  293 */     int result = getStartStopThreads();
/*      */     
/*      */ 
/*  296 */     if (result > 0) {
/*  297 */       return result;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  303 */     result = Runtime.getRuntime().availableProcessors() + result;
/*  304 */     if (result < 1) {
/*  305 */       result = 1;
/*      */     }
/*  307 */     return result;
/*      */   }
/*      */   
/*      */   public void setStartStopThreads(int startStopThreads)
/*      */   {
/*  312 */     this.startStopThreads = startStopThreads;
/*      */     
/*      */ 
/*  315 */     ThreadPoolExecutor executor = this.startStopExecutor;
/*  316 */     if (executor != null) {
/*  317 */       int newThreads = getStartStopThreadsInternal();
/*  318 */       executor.setMaximumPoolSize(newThreads);
/*  319 */       executor.setCorePoolSize(newThreads);
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
/*      */   public int getBackgroundProcessorDelay()
/*      */   {
/*  335 */     return this.backgroundProcessorDelay;
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
/*      */   public void setBackgroundProcessorDelay(int delay)
/*      */   {
/*  348 */     this.backgroundProcessorDelay = delay;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Log getLogger()
/*      */   {
/*  358 */     if (this.logger != null)
/*  359 */       return this.logger;
/*  360 */     this.logger = LogFactory.getLog(getLogName());
/*  361 */     return this.logger;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getLogName()
/*      */   {
/*  372 */     if (this.logName != null) {
/*  373 */       return this.logName;
/*      */     }
/*  375 */     String loggerName = null;
/*  376 */     Container current = this;
/*  377 */     while (current != null) {
/*  378 */       String name = current.getName();
/*  379 */       if ((name == null) || (name.equals(""))) {
/*  380 */         name = "/";
/*  381 */       } else if (name.startsWith("##")) {
/*  382 */         name = "/" + name;
/*      */       }
/*  384 */       loggerName = "[" + name + "]" + (loggerName != null ? "." + loggerName : "");
/*      */       
/*  386 */       current = current.getParent();
/*      */     }
/*  388 */     this.logName = (ContainerBase.class.getName() + "." + loggerName);
/*  389 */     return this.logName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Cluster getCluster()
/*      */   {
/*  401 */     Lock readLock = this.clusterLock.readLock();
/*  402 */     readLock.lock();
/*      */     try { Cluster localCluster;
/*  404 */       if (this.cluster != null) {
/*  405 */         return this.cluster;
/*      */       }
/*  407 */       if (this.parent != null) {
/*  408 */         return this.parent.getCluster();
/*      */       }
/*  410 */       return null;
/*      */     } finally {
/*  412 */       readLock.unlock();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Cluster getClusterInternal()
/*      */   {
/*  421 */     Lock readLock = this.clusterLock.readLock();
/*  422 */     readLock.lock();
/*      */     try {
/*  424 */       return this.cluster;
/*      */     } finally {
/*  426 */       readLock.unlock();
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
/*      */   public void setCluster(Cluster cluster)
/*      */   {
/*  439 */     Cluster oldCluster = null;
/*  440 */     Lock writeLock = this.clusterLock.writeLock();
/*  441 */     writeLock.lock();
/*      */     try
/*      */     {
/*  444 */       oldCluster = this.cluster;
/*  445 */       if (oldCluster == cluster)
/*  446 */         return;
/*  447 */       this.cluster = cluster;
/*      */       
/*      */ 
/*  450 */       if ((getState().isAvailable()) && (oldCluster != null) && ((oldCluster instanceof Lifecycle))) {
/*      */         try
/*      */         {
/*  453 */           ((Lifecycle)oldCluster).stop();
/*      */         } catch (LifecycleException e) {
/*  455 */           log.error("ContainerBase.setCluster: stop: ", e);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  460 */       if (cluster != null) {
/*  461 */         cluster.setContainer(this);
/*      */       }
/*  463 */       if ((getState().isAvailable()) && (cluster != null) && ((cluster instanceof Lifecycle))) {
/*      */         try
/*      */         {
/*  466 */           ((Lifecycle)cluster).start();
/*      */         } catch (LifecycleException e) {
/*  468 */           log.error("ContainerBase.setCluster: start: ", e);
/*      */         }
/*      */       }
/*      */     } finally {
/*  472 */       writeLock.unlock();
/*      */     }
/*      */     
/*      */ 
/*  476 */     this.support.firePropertyChange("cluster", oldCluster, cluster);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getName()
/*      */   {
/*  488 */     return this.name;
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
/*      */   public void setName(String name)
/*      */   {
/*  507 */     String oldName = this.name;
/*  508 */     this.name = name;
/*  509 */     this.support.firePropertyChange("name", oldName, this.name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getStartChildren()
/*      */   {
/*  521 */     return this.startChildren;
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
/*      */   public void setStartChildren(boolean startChildren)
/*      */   {
/*  534 */     boolean oldStartChildren = this.startChildren;
/*  535 */     this.startChildren = startChildren;
/*  536 */     this.support.firePropertyChange("startChildren", oldStartChildren, this.startChildren);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Container getParent()
/*      */   {
/*  547 */     return this.parent;
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
/*      */   public void setParent(Container container)
/*      */   {
/*  566 */     Container oldParent = this.parent;
/*  567 */     this.parent = container;
/*  568 */     this.support.firePropertyChange("parent", oldParent, this.parent);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ClassLoader getParentClassLoader()
/*      */   {
/*  580 */     if (this.parentClassLoader != null)
/*  581 */       return this.parentClassLoader;
/*  582 */     if (this.parent != null) {
/*  583 */       return this.parent.getParentClassLoader();
/*      */     }
/*  585 */     return ClassLoader.getSystemClassLoader();
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
/*      */   public void setParentClassLoader(ClassLoader parent)
/*      */   {
/*  601 */     ClassLoader oldParentClassLoader = this.parentClassLoader;
/*  602 */     this.parentClassLoader = parent;
/*  603 */     this.support.firePropertyChange("parentClassLoader", oldParentClassLoader, this.parentClassLoader);
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
/*      */   public Pipeline getPipeline()
/*      */   {
/*  616 */     return this.pipeline;
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
/*      */   public Realm getRealm()
/*      */   {
/*  629 */     Lock l = this.realmLock.readLock();
/*  630 */     l.lock();
/*      */     try { Realm localRealm;
/*  632 */       if (this.realm != null)
/*  633 */         return this.realm;
/*  634 */       if (this.parent != null)
/*  635 */         return this.parent.getRealm();
/*  636 */       return null;
/*      */     } finally {
/*  638 */       l.unlock();
/*      */     }
/*      */   }
/*      */   
/*      */   protected Realm getRealmInternal()
/*      */   {
/*  644 */     Lock l = this.realmLock.readLock();
/*  645 */     l.lock();
/*      */     try {
/*  647 */       return this.realm;
/*      */     } finally {
/*  649 */       l.unlock();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRealm(Realm realm)
/*      */   {
/*  661 */     Lock l = this.realmLock.writeLock();
/*  662 */     l.lock();
/*      */     try
/*      */     {
/*  665 */       Realm oldRealm = this.realm;
/*  666 */       if (oldRealm == realm)
/*  667 */         return;
/*  668 */       this.realm = realm;
/*      */       
/*      */ 
/*  671 */       if ((getState().isAvailable()) && (oldRealm != null) && ((oldRealm instanceof Lifecycle))) {
/*      */         try
/*      */         {
/*  674 */           ((Lifecycle)oldRealm).stop();
/*      */         } catch (LifecycleException e) {
/*  676 */           log.error("ContainerBase.setRealm: stop: ", e);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  681 */       if (realm != null)
/*  682 */         realm.setContainer(this);
/*  683 */       if ((getState().isAvailable()) && (realm != null) && ((realm instanceof Lifecycle))) {
/*      */         try
/*      */         {
/*  686 */           ((Lifecycle)realm).start();
/*      */         } catch (LifecycleException e) {
/*  688 */           log.error("ContainerBase.setRealm: start: ", e);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  693 */       this.support.firePropertyChange("realm", oldRealm, this.realm);
/*      */     } finally {
/*  695 */       l.unlock();
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
/*      */   public void addChild(Container child)
/*      */   {
/*  723 */     if (Globals.IS_SECURITY_ENABLED) {
/*  724 */       PrivilegedAction<Void> dp = new PrivilegedAddChild(child);
/*      */       
/*  726 */       AccessController.doPrivileged(dp);
/*      */     } else {
/*  728 */       addChildInternal(child);
/*      */     }
/*      */   }
/*      */   
/*      */   private void addChildInternal(Container child)
/*      */   {
/*  734 */     if (log.isDebugEnabled())
/*  735 */       log.debug("Add child " + child + " " + this);
/*  736 */     synchronized (this.children) {
/*  737 */       if (this.children.get(child.getName()) != null)
/*      */       {
/*  739 */         throw new IllegalArgumentException("addChild:  Child name '" + child.getName() + "' is not unique");
/*      */       }
/*  741 */       child.setParent(this);
/*  742 */       this.children.put(child.getName(), child);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  749 */       if (((getState().isAvailable()) || 
/*  750 */         (LifecycleState.STARTING_PREP.equals(getState()))) && (this.startChildren))
/*      */       {
/*  752 */         child.start();
/*      */       }
/*      */     } catch (LifecycleException e) {
/*  755 */       log.error("ContainerBase.addChild: start: ", e);
/*  756 */       throw new IllegalStateException("ContainerBase.addChild: start: " + e);
/*      */     } finally {
/*  758 */       fireContainerEvent("addChild", child);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addContainerListener(ContainerListener listener)
/*      */   {
/*  770 */     this.listeners.add(listener);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addPropertyChangeListener(PropertyChangeListener listener)
/*      */   {
/*  781 */     this.support.addPropertyChangeListener(listener);
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public Container findChild(String name)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_1
/*      */     //   1: ifnonnull +5 -> 6
/*      */     //   4: aconst_null
/*      */     //   5: areturn
/*      */     //   6: aload_0
/*      */     //   7: getfield 7	org/apache/catalina/core/ContainerBase:children	Ljava/util/HashMap;
/*      */     //   10: dup
/*      */     //   11: astore_2
/*      */     //   12: monitorenter
/*      */     //   13: aload_0
/*      */     //   14: getfield 7	org/apache/catalina/core/ContainerBase:children	Ljava/util/HashMap;
/*      */     //   17: aload_1
/*      */     //   18: invokevirtual 97	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   21: checkcast 107	org/apache/catalina/Container
/*      */     //   24: aload_2
/*      */     //   25: monitorexit
/*      */     //   26: areturn
/*      */     //   27: astore_3
/*      */     //   28: aload_2
/*      */     //   29: monitorexit
/*      */     //   30: aload_3
/*      */     //   31: athrow
/*      */     // Line number table:
/*      */     //   Java source line #793	-> byte code offset #0
/*      */     //   Java source line #794	-> byte code offset #4
/*      */     //   Java source line #796	-> byte code offset #6
/*      */     //   Java source line #797	-> byte code offset #13
/*      */     //   Java source line #798	-> byte code offset #27
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	32	0	this	ContainerBase
/*      */     //   0	32	1	name	String
/*      */     //   11	18	2	Ljava/lang/Object;	Object
/*      */     //   27	4	3	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   13	26	27	finally
/*      */     //   27	30	27	finally
/*      */   }
/*      */   
/*      */   public Container[] findChildren()
/*      */   {
/*  808 */     synchronized (this.children) {
/*  809 */       Container[] results = new Container[this.children.size()];
/*  810 */       return (Container[])this.children.values().toArray(results);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ContainerListener[] findContainerListeners()
/*      */   {
/*  822 */     ContainerListener[] results = new ContainerListener[0];
/*      */     
/*  824 */     return (ContainerListener[])this.listeners.toArray(results);
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
/*      */   public void removeChild(Container child)
/*      */   {
/*  837 */     if (child == null) {
/*  838 */       return;
/*      */     }
/*      */     try
/*      */     {
/*  842 */       if (child.getState().isAvailable()) {
/*  843 */         child.stop();
/*      */       }
/*      */     } catch (LifecycleException e) {
/*  846 */       log.error("ContainerBase.removeChild: stop: ", e);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  853 */       if (!LifecycleState.DESTROYING.equals(child.getState())) {
/*  854 */         child.destroy();
/*      */       }
/*      */     } catch (LifecycleException e) {
/*  857 */       log.error("ContainerBase.removeChild: destroy: ", e);
/*      */     }
/*      */     
/*  860 */     synchronized (this.children) {
/*  861 */       if (this.children.get(child.getName()) == null)
/*  862 */         return;
/*  863 */       this.children.remove(child.getName());
/*      */     }
/*      */     
/*  866 */     fireContainerEvent("removeChild", child);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeContainerListener(ContainerListener listener)
/*      */   {
/*  877 */     this.listeners.remove(listener);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removePropertyChangeListener(PropertyChangeListener listener)
/*      */   {
/*  889 */     this.support.removePropertyChangeListener(listener);
/*      */   }
/*      */   
/*      */ 
/*      */   protected void initInternal()
/*      */     throws LifecycleException
/*      */   {
/*  896 */     BlockingQueue<Runnable> startStopQueue = new LinkedBlockingQueue();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  901 */     this.startStopExecutor = new ThreadPoolExecutor(getStartStopThreadsInternal(), getStartStopThreadsInternal(), 10L, TimeUnit.SECONDS, startStopQueue, new StartStopThreadFactory(getName() + "-startStop-"));
/*  902 */     this.startStopExecutor.allowCoreThreadTimeOut(true);
/*  903 */     super.initInternal();
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
/*      */   protected synchronized void startInternal()
/*      */     throws LifecycleException
/*      */   {
/*  918 */     this.logger = null;
/*  919 */     getLogger();
/*  920 */     Cluster cluster = getClusterInternal();
/*  921 */     if ((cluster instanceof Lifecycle)) {
/*  922 */       ((Lifecycle)cluster).start();
/*      */     }
/*  924 */     Realm realm = getRealmInternal();
/*  925 */     if ((realm instanceof Lifecycle)) {
/*  926 */       ((Lifecycle)realm).start();
/*      */     }
/*      */     
/*      */ 
/*  930 */     Container[] children = findChildren();
/*  931 */     List<Future<Void>> results = new ArrayList();
/*  932 */     for (int i = 0; i < children.length; i++) {
/*  933 */       results.add(this.startStopExecutor.submit(new StartChild(children[i])));
/*      */     }
/*      */     
/*  936 */     boolean fail = false;
/*  937 */     for (Future<Void> result : results) {
/*      */       try {
/*  939 */         result.get();
/*      */       } catch (Exception e) {
/*  941 */         log.error(sm.getString("containerBase.threadedStartFailed"), e);
/*  942 */         fail = true;
/*      */       }
/*      */     }
/*      */     
/*  946 */     if (fail)
/*      */     {
/*  948 */       throw new LifecycleException(sm.getString("containerBase.threadedStartFailed"));
/*      */     }
/*      */     
/*      */ 
/*  952 */     if ((this.pipeline instanceof Lifecycle)) {
/*  953 */       ((Lifecycle)this.pipeline).start();
/*      */     }
/*      */     
/*  956 */     setState(LifecycleState.STARTING);
/*      */     
/*      */ 
/*  959 */     threadStart();
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
/*      */   protected synchronized void stopInternal()
/*      */     throws LifecycleException
/*      */   {
/*  975 */     threadStop();
/*      */     
/*  977 */     setState(LifecycleState.STOPPING);
/*      */     
/*      */ 
/*  980 */     if (((this.pipeline instanceof Lifecycle)) && 
/*  981 */       (((Lifecycle)this.pipeline).getState().isAvailable())) {
/*  982 */       ((Lifecycle)this.pipeline).stop();
/*      */     }
/*      */     
/*      */ 
/*  986 */     Container[] children = findChildren();
/*  987 */     List<Future<Void>> results = new ArrayList();
/*  988 */     for (int i = 0; i < children.length; i++) {
/*  989 */       results.add(this.startStopExecutor.submit(new StopChild(children[i])));
/*      */     }
/*      */     
/*  992 */     boolean fail = false;
/*  993 */     for (Future<Void> result : results) {
/*      */       try {
/*  995 */         result.get();
/*      */       } catch (Exception e) {
/*  997 */         log.error(sm.getString("containerBase.threadedStopFailed"), e);
/*  998 */         fail = true;
/*      */       }
/*      */     }
/* 1001 */     if (fail)
/*      */     {
/* 1003 */       throw new LifecycleException(sm.getString("containerBase.threadedStopFailed"));
/*      */     }
/*      */     
/*      */ 
/* 1007 */     Realm realm = getRealmInternal();
/* 1008 */     if ((realm instanceof Lifecycle)) {
/* 1009 */       ((Lifecycle)realm).stop();
/*      */     }
/* 1011 */     Cluster cluster = getClusterInternal();
/* 1012 */     if ((cluster instanceof Lifecycle)) {
/* 1013 */       ((Lifecycle)cluster).stop();
/*      */     }
/*      */   }
/*      */   
/*      */   protected void destroyInternal()
/*      */     throws LifecycleException
/*      */   {
/* 1020 */     Realm realm = getRealmInternal();
/* 1021 */     if ((realm instanceof Lifecycle)) {
/* 1022 */       ((Lifecycle)realm).destroy();
/*      */     }
/* 1024 */     Cluster cluster = getClusterInternal();
/* 1025 */     if ((cluster instanceof Lifecycle)) {
/* 1026 */       ((Lifecycle)cluster).destroy();
/*      */     }
/*      */     
/*      */ 
/* 1030 */     if ((this.pipeline instanceof Lifecycle)) {
/* 1031 */       ((Lifecycle)this.pipeline).destroy();
/*      */     }
/*      */     
/*      */ 
/* 1035 */     for (Container child : findChildren()) {
/* 1036 */       removeChild(child);
/*      */     }
/*      */     
/*      */ 
/* 1040 */     if (this.parent != null) {
/* 1041 */       this.parent.removeChild(this);
/*      */     }
/*      */     
/*      */ 
/* 1045 */     if (this.startStopExecutor != null) {
/* 1046 */       this.startStopExecutor.shutdownNow();
/*      */     }
/*      */     
/* 1049 */     super.destroyInternal();
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
/*      */   public void logAccess(Request request, Response response, long time, boolean useDefault)
/*      */   {
/* 1062 */     boolean logged = false;
/*      */     
/* 1064 */     if (getAccessLog() != null) {
/* 1065 */       getAccessLog().log(request, response, time);
/* 1066 */       logged = true;
/*      */     }
/*      */     
/* 1069 */     if (getParent() != null)
/*      */     {
/*      */ 
/* 1072 */       getParent().logAccess(request, response, time, (useDefault) && (!logged));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public AccessLog getAccessLog()
/*      */   {
/* 1079 */     if (this.accessLogScanComplete) {
/* 1080 */       return this.accessLog;
/*      */     }
/*      */     
/* 1083 */     AccessLogAdapter adapter = null;
/* 1084 */     Valve[] valves = getPipeline().getValves();
/* 1085 */     for (Valve valve : valves) {
/* 1086 */       if ((valve instanceof AccessLog)) {
/* 1087 */         if (adapter == null) {
/* 1088 */           adapter = new AccessLogAdapter((AccessLog)valve);
/*      */         } else {
/* 1090 */           adapter.add((AccessLog)valve);
/*      */         }
/*      */       }
/*      */     }
/* 1094 */     if (adapter != null) {
/* 1095 */       this.accessLog = adapter;
/*      */     }
/* 1097 */     this.accessLogScanComplete = true;
/* 1098 */     return this.accessLog;
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
/*      */   public synchronized void addValve(Valve valve)
/*      */   {
/* 1123 */     this.pipeline.addValve(valve);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void backgroundProcess()
/*      */   {
/* 1135 */     if (!getState().isAvailable()) {
/* 1136 */       return;
/*      */     }
/* 1138 */     Cluster cluster = getClusterInternal();
/* 1139 */     if (cluster != null) {
/*      */       try {
/* 1141 */         cluster.backgroundProcess();
/*      */       } catch (Exception e) {
/* 1143 */         log.warn(sm.getString("containerBase.backgroundProcess.cluster", new Object[] { cluster }), e);
/*      */       }
/*      */     }
/*      */     
/* 1147 */     Realm realm = getRealmInternal();
/* 1148 */     if (realm != null) {
/*      */       try {
/* 1150 */         realm.backgroundProcess();
/*      */       } catch (Exception e) {
/* 1152 */         log.warn(sm.getString("containerBase.backgroundProcess.realm", new Object[] { realm }), e);
/*      */       }
/*      */     }
/* 1155 */     Valve current = this.pipeline.getFirst();
/* 1156 */     while (current != null) {
/*      */       try {
/* 1158 */         current.backgroundProcess();
/*      */       } catch (Exception e) {
/* 1160 */         log.warn(sm.getString("containerBase.backgroundProcess.valve", new Object[] { current }), e);
/*      */       }
/* 1162 */       current = current.getNext();
/*      */     }
/* 1164 */     fireLifecycleEvent("periodic", null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public File getCatalinaBase()
/*      */   {
/* 1171 */     if (this.parent == null) {
/* 1172 */       return null;
/*      */     }
/*      */     
/* 1175 */     return this.parent.getCatalinaBase();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public File getCatalinaHome()
/*      */   {
/* 1182 */     if (this.parent == null) {
/* 1183 */       return null;
/*      */     }
/*      */     
/* 1186 */     return this.parent.getCatalinaHome();
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
/*      */   public void fireContainerEvent(String type, Object data)
/*      */   {
/* 1203 */     if (this.listeners.size() < 1) {
/* 1204 */       return;
/*      */     }
/* 1206 */     ContainerEvent event = new ContainerEvent(this, type, data);
/*      */     
/* 1208 */     for (ContainerListener listener : this.listeners) {
/* 1209 */       listener.containerEvent(event);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getDomainInternal()
/*      */   {
/* 1219 */     Container p = getParent();
/* 1220 */     if (p == null) {
/* 1221 */       return null;
/*      */     }
/* 1223 */     return p.getDomain();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getMBeanKeyProperties()
/*      */   {
/* 1230 */     Container c = this;
/* 1231 */     StringBuilder keyProperties = new StringBuilder();
/* 1232 */     int containerCount = 0;
/*      */     
/*      */ 
/*      */ 
/* 1236 */     while (!(c instanceof Engine)) {
/* 1237 */       if ((c instanceof Wrapper)) {
/* 1238 */         keyProperties.insert(0, ",servlet=");
/* 1239 */         keyProperties.insert(9, c.getName());
/* 1240 */       } else if ((c instanceof Context)) {
/* 1241 */         keyProperties.insert(0, ",context=");
/* 1242 */         ContextName cn = new ContextName(c.getName(), false);
/* 1243 */         keyProperties.insert(9, cn.getDisplayName());
/* 1244 */       } else if ((c instanceof Host)) {
/* 1245 */         keyProperties.insert(0, ",host=");
/* 1246 */         keyProperties.insert(6, c.getName());
/* 1247 */       } else { if (c == null)
/*      */         {
/* 1249 */           keyProperties.append(",container");
/* 1250 */           keyProperties.append(containerCount++);
/* 1251 */           keyProperties.append("=null");
/* 1252 */           break;
/*      */         }
/*      */         
/* 1255 */         keyProperties.append(",container");
/* 1256 */         keyProperties.append(containerCount++);
/* 1257 */         keyProperties.append('=');
/* 1258 */         keyProperties.append(c.getName());
/*      */       }
/* 1260 */       c = c.getParent();
/*      */     }
/* 1262 */     return keyProperties.toString();
/*      */   }
/*      */   
/*      */   public ObjectName[] getChildren() {
/* 1266 */     List<ObjectName> names = new ArrayList(this.children.size());
/* 1267 */     Iterator<Container> it = this.children.values().iterator();
/* 1268 */     while (it.hasNext()) {
/* 1269 */       Object next = it.next();
/* 1270 */       if ((next instanceof ContainerBase)) {
/* 1271 */         names.add(((ContainerBase)next).getObjectName());
/*      */       }
/*      */     }
/* 1274 */     return (ObjectName[])names.toArray(new ObjectName[names.size()]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void threadStart()
/*      */   {
/* 1286 */     if (this.thread != null)
/* 1287 */       return;
/* 1288 */     if (this.backgroundProcessorDelay <= 0) {
/* 1289 */       return;
/*      */     }
/* 1291 */     this.threadDone = false;
/* 1292 */     String threadName = "ContainerBackgroundProcessor[" + toString() + "]";
/* 1293 */     this.thread = new Thread(new ContainerBackgroundProcessor(), threadName);
/* 1294 */     this.thread.setDaemon(true);
/* 1295 */     this.thread.start();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void threadStop()
/*      */   {
/* 1306 */     if (this.thread == null) {
/* 1307 */       return;
/*      */     }
/* 1309 */     this.threadDone = true;
/* 1310 */     this.thread.interrupt();
/*      */     try {
/* 1312 */       this.thread.join();
/*      */     }
/*      */     catch (InterruptedException localInterruptedException) {}
/*      */     
/*      */ 
/* 1317 */     this.thread = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1324 */     StringBuilder sb = new StringBuilder();
/* 1325 */     Container parent = getParent();
/* 1326 */     if (parent != null) {
/* 1327 */       sb.append(parent.toString());
/* 1328 */       sb.append('.');
/*      */     }
/* 1330 */     sb.append(getClass().getSimpleName());
/* 1331 */     sb.append('[');
/* 1332 */     sb.append(getName());
/* 1333 */     sb.append(']');
/* 1334 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected class ContainerBackgroundProcessor
/*      */     implements Runnable
/*      */   {
/*      */     protected ContainerBackgroundProcessor() {}
/*      */     
/*      */ 
/*      */ 
/*      */     public void run()
/*      */     {
/* 1348 */       Throwable t = null;
/* 1349 */       String unexpectedDeathMessage = ContainerBase.sm.getString("containerBase.backgroundProcess.unexpectedThreadDeath", new Object[] {
/*      */       
/* 1351 */         Thread.currentThread().getName() });
/*      */       try {
/* 1353 */         while (!ContainerBase.this.threadDone) {
/*      */           try {
/* 1355 */             Thread.sleep(ContainerBase.this.backgroundProcessorDelay * 1000L);
/*      */           }
/*      */           catch (InterruptedException localInterruptedException) {}
/*      */           
/* 1359 */           if (!ContainerBase.this.threadDone) {
/* 1360 */             processChildren(ContainerBase.this);
/*      */           }
/*      */         }
/*      */       } catch (RuntimeException|Error e) {
/* 1364 */         t = e;
/* 1365 */         throw e;
/*      */       } finally {
/* 1367 */         if (!ContainerBase.this.threadDone) {
/* 1368 */           ContainerBase.log.error(unexpectedDeathMessage, t);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     protected void processChildren(Container container) {
/* 1374 */       ClassLoader originalClassLoader = null;
/*      */       try
/*      */       {
/* 1377 */         if ((container instanceof Context)) {
/* 1378 */           Loader loader = ((Context)container).getLoader();
/*      */           
/* 1380 */           if (loader == null) {
/* 1381 */             return;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 1386 */           originalClassLoader = ((Context)container).bind(false, null);
/*      */         }
/* 1388 */         container.backgroundProcess();
/* 1389 */         Container[] children = container.findChildren();
/* 1390 */         for (int i = 0; i < children.length; i++) {
/* 1391 */           if (children[i].getBackgroundProcessorDelay() <= 0) {
/* 1392 */             processChildren(children[i]);
/*      */           }
/*      */         }
/*      */       } catch (Throwable t) {
/* 1396 */         ExceptionUtils.handleThrowable(t);
/* 1397 */         ContainerBase.log.error("Exception invoking periodic operation: ", t);
/*      */       } finally {
/* 1399 */         if ((container instanceof Context)) {
/* 1400 */           ((Context)container).unbind(false, originalClassLoader);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static class StartChild
/*      */     implements Callable<Void>
/*      */   {
/*      */     private Container child;
/*      */     
/*      */     public StartChild(Container child)
/*      */     {
/* 1414 */       this.child = child;
/*      */     }
/*      */     
/*      */     public Void call() throws LifecycleException
/*      */     {
/* 1419 */       this.child.start();
/* 1420 */       return null;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class StopChild implements Callable<Void>
/*      */   {
/*      */     private Container child;
/*      */     
/*      */     public StopChild(Container child) {
/* 1429 */       this.child = child;
/*      */     }
/*      */     
/*      */     public Void call() throws LifecycleException
/*      */     {
/* 1434 */       if (this.child.getState().isAvailable()) {
/* 1435 */         this.child.stop();
/*      */       }
/* 1437 */       return null;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class StartStopThreadFactory implements ThreadFactory {
/*      */     private final ThreadGroup group;
/* 1443 */     private final AtomicInteger threadNumber = new AtomicInteger(1);
/*      */     private final String namePrefix;
/*      */     
/*      */     public StartStopThreadFactory(String namePrefix) {
/* 1447 */       SecurityManager s = System.getSecurityManager();
/* 1448 */       this.group = (s != null ? s.getThreadGroup() : Thread.currentThread().getThreadGroup());
/* 1449 */       this.namePrefix = namePrefix;
/*      */     }
/*      */     
/*      */     public Thread newThread(Runnable r)
/*      */     {
/* 1454 */       Thread thread = new Thread(this.group, r, this.namePrefix + this.threadNumber.getAndIncrement());
/* 1455 */       thread.setDaemon(true);
/* 1456 */       return thread;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\ContainerBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */