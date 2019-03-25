/*      */ package org.apache.catalina.core;
/*      */ 
/*      */ import java.beans.PropertyChangeSupport;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.reflect.Method;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Set;
/*      */ import java.util.Stack;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*      */ import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
/*      */ import javax.management.ListenerNotFoundException;
/*      */ import javax.management.MBeanNotificationInfo;
/*      */ import javax.management.Notification;
/*      */ import javax.management.NotificationBroadcasterSupport;
/*      */ import javax.management.NotificationEmitter;
/*      */ import javax.management.NotificationFilter;
/*      */ import javax.management.NotificationListener;
/*      */ import javax.management.ObjectName;
/*      */ import javax.servlet.MultipartConfigElement;
/*      */ import javax.servlet.Servlet;
/*      */ import javax.servlet.ServletConfig;
/*      */ import javax.servlet.ServletContext;
/*      */ import javax.servlet.ServletException;
/*      */ import javax.servlet.ServletSecurityElement;
/*      */ import javax.servlet.SingleThreadModel;
/*      */ import javax.servlet.UnavailableException;
/*      */ import javax.servlet.annotation.MultipartConfig;
/*      */ import javax.servlet.annotation.ServletSecurity;
/*      */ import javax.servlet.http.HttpServlet;
/*      */ import org.apache.catalina.Container;
/*      */ import org.apache.catalina.ContainerServlet;
/*      */ import org.apache.catalina.Context;
/*      */ import org.apache.catalina.Globals;
/*      */ import org.apache.catalina.LifecycleException;
/*      */ import org.apache.catalina.LifecycleState;
/*      */ import org.apache.catalina.Loader;
/*      */ import org.apache.catalina.Pipeline;
/*      */ import org.apache.catalina.Wrapper;
/*      */ import org.apache.catalina.security.SecurityUtil;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.tomcat.InstanceManager;
/*      */ import org.apache.tomcat.PeriodicEventListener;
/*      */ import org.apache.tomcat.util.ExceptionUtils;
/*      */ import org.apache.tomcat.util.log.SystemLogHandler;
/*      */ import org.apache.tomcat.util.modeler.Registry;
/*      */ import org.apache.tomcat.util.modeler.Util;
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
/*      */ public class StandardWrapper
/*      */   extends ContainerBase
/*      */   implements ServletConfig, Wrapper, NotificationEmitter
/*      */ {
/*   80 */   private static final Log log = LogFactory.getLog(StandardWrapper.class);
/*      */   
/*   82 */   protected static final String[] DEFAULT_SERVLET_METHODS = { "GET", "HEAD", "POST" };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public StandardWrapper()
/*      */   {
/*   94 */     this.swValve = new StandardWrapperValve();
/*   95 */     this.pipeline.setBasic(this.swValve);
/*   96 */     this.broadcaster = new NotificationBroadcasterSupport();
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
/*  110 */   protected long available = 0L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final NotificationBroadcasterSupport broadcaster;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  121 */   protected final AtomicInteger countAllocated = new AtomicInteger(0);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  127 */   protected final StandardWrapperFacade facade = new StandardWrapperFacade(this);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  133 */   protected volatile Servlet instance = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  139 */   protected volatile boolean instanceInitialized = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  146 */   protected int loadOnStartup = -1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  152 */   protected final ArrayList<String> mappings = new ArrayList();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  159 */   protected HashMap<String, String> parameters = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  167 */   protected HashMap<String, String> references = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  173 */   protected String runAs = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  178 */   protected long sequenceNumber = 0L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  183 */   protected String servletClass = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  189 */   protected volatile boolean singleThreadModel = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  195 */   protected volatile boolean unloading = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  201 */   protected int maxInstances = 20;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  207 */   protected int nInstances = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  213 */   protected Stack<Servlet> instancePool = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  219 */   protected long unloadDelay = 2000L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean isJspServlet;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectName jspMonitorON;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  237 */   protected boolean swallowOutput = false;
/*      */   
/*      */   protected StandardWrapperValve swValve;
/*      */   
/*  241 */   protected long loadTime = 0L;
/*  242 */   protected int classLoadTime = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  247 */   protected MultipartConfigElement multipartConfigElement = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  252 */   protected boolean asyncSupported = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  257 */   protected boolean enabled = true;
/*      */   
/*  259 */   protected volatile boolean servletSecurityAnnotationScanRequired = false;
/*      */   
/*  261 */   private boolean overridable = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  267 */   protected static Class<?>[] classType = { ServletConfig.class };
/*      */   
/*  269 */   private final ReentrantReadWriteLock parametersLock = new ReentrantReadWriteLock();
/*      */   
/*      */ 
/*  272 */   private final ReentrantReadWriteLock mappingsLock = new ReentrantReadWriteLock();
/*      */   
/*      */ 
/*  275 */   private final ReentrantReadWriteLock referencesLock = new ReentrantReadWriteLock();
/*      */   
/*      */ 
/*      */   protected MBeanNotificationInfo[] notificationInfo;
/*      */   
/*      */ 
/*      */   public boolean isOverridable()
/*      */   {
/*  283 */     return this.overridable;
/*      */   }
/*      */   
/*      */   public void setOverridable(boolean overridable)
/*      */   {
/*  288 */     this.overridable = overridable;
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
/*      */   public long getAvailable()
/*      */   {
/*  302 */     return this.available;
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
/*      */   public void setAvailable(long available)
/*      */   {
/*  319 */     long oldAvailable = this.available;
/*  320 */     if (available > System.currentTimeMillis()) {
/*  321 */       this.available = available;
/*      */     } else
/*  323 */       this.available = 0L;
/*  324 */     this.support.firePropertyChange("available", Long.valueOf(oldAvailable), 
/*  325 */       Long.valueOf(this.available));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getCountAllocated()
/*      */   {
/*  336 */     return this.countAllocated.get();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getLoadOnStartup()
/*      */   {
/*  347 */     if ((this.isJspServlet) && (this.loadOnStartup < 0))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  353 */       return Integer.MAX_VALUE;
/*      */     }
/*  355 */     return this.loadOnStartup;
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
/*      */   public void setLoadOnStartup(int value)
/*      */   {
/*  369 */     int oldLoadOnStartup = this.loadOnStartup;
/*  370 */     this.loadOnStartup = value;
/*  371 */     this.support.firePropertyChange("loadOnStartup", 
/*  372 */       Integer.valueOf(oldLoadOnStartup), 
/*  373 */       Integer.valueOf(this.loadOnStartup));
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
/*      */   public void setLoadOnStartupString(String value)
/*      */   {
/*      */     try
/*      */     {
/*  390 */       setLoadOnStartup(Integer.parseInt(value));
/*      */     } catch (NumberFormatException e) {
/*  392 */       setLoadOnStartup(0);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getLoadOnStartupString()
/*      */   {
/*  400 */     return Integer.toString(getLoadOnStartup());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxInstances()
/*      */   {
/*  410 */     return this.maxInstances;
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
/*      */   public void setMaxInstances(int maxInstances)
/*      */   {
/*  423 */     int oldMaxInstances = this.maxInstances;
/*  424 */     this.maxInstances = maxInstances;
/*  425 */     this.support.firePropertyChange("maxInstances", oldMaxInstances, this.maxInstances);
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
/*      */   public void setParent(Container container)
/*      */   {
/*  439 */     if ((container != null) && (!(container instanceof Context)))
/*      */     {
/*      */ 
/*  442 */       throw new IllegalArgumentException(sm.getString("standardWrapper.notContext")); }
/*  443 */     if ((container instanceof StandardContext)) {
/*  444 */       this.swallowOutput = ((StandardContext)container).getSwallowOutput();
/*  445 */       this.unloadDelay = ((StandardContext)container).getUnloadDelay();
/*      */     }
/*  447 */     super.setParent(container);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getRunAs()
/*      */   {
/*  458 */     return this.runAs;
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
/*      */   public void setRunAs(String runAs)
/*      */   {
/*  471 */     String oldRunAs = this.runAs;
/*  472 */     this.runAs = runAs;
/*  473 */     this.support.firePropertyChange("runAs", oldRunAs, this.runAs);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getServletClass()
/*      */   {
/*  484 */     return this.servletClass;
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
/*      */   public void setServletClass(String servletClass)
/*      */   {
/*  497 */     String oldServletClass = this.servletClass;
/*  498 */     this.servletClass = servletClass;
/*  499 */     this.support.firePropertyChange("servletClass", oldServletClass, this.servletClass);
/*      */     
/*  501 */     if ("org.apache.jasper.servlet.JspServlet".equals(servletClass)) {
/*  502 */       this.isJspServlet = true;
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
/*      */   public void setServletName(String name)
/*      */   {
/*  518 */     setName(name);
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
/*      */   public Boolean isSingleThreadModel()
/*      */   {
/*  536 */     if ((this.singleThreadModel) || (this.instance != null)) {
/*  537 */       return Boolean.valueOf(this.singleThreadModel);
/*      */     }
/*  539 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isUnavailable()
/*      */   {
/*  549 */     if (!isEnabled())
/*  550 */       return true;
/*  551 */     if (this.available == 0L)
/*  552 */       return false;
/*  553 */     if (this.available <= System.currentTimeMillis()) {
/*  554 */       this.available = 0L;
/*  555 */       return false;
/*      */     }
/*  557 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String[] getServletMethods()
/*      */     throws ServletException
/*      */   {
/*  565 */     this.instance = loadServlet();
/*      */     
/*  567 */     Class<? extends Servlet> servletClazz = this.instance.getClass();
/*  568 */     if (!HttpServlet.class.isAssignableFrom(servletClazz))
/*      */     {
/*  570 */       return DEFAULT_SERVLET_METHODS;
/*      */     }
/*      */     
/*  573 */     HashSet<String> allow = new HashSet();
/*  574 */     allow.add("TRACE");
/*  575 */     allow.add("OPTIONS");
/*      */     
/*  577 */     Method[] methods = getAllDeclaredMethods(servletClazz);
/*  578 */     for (int i = 0; (methods != null) && (i < methods.length); i++) {
/*  579 */       Method m = methods[i];
/*      */       
/*  581 */       if (m.getName().equals("doGet")) {
/*  582 */         allow.add("GET");
/*  583 */         allow.add("HEAD");
/*  584 */       } else if (m.getName().equals("doPost")) {
/*  585 */         allow.add("POST");
/*  586 */       } else if (m.getName().equals("doPut")) {
/*  587 */         allow.add("PUT");
/*  588 */       } else if (m.getName().equals("doDelete")) {
/*  589 */         allow.add("DELETE");
/*      */       }
/*      */     }
/*      */     
/*  593 */     String[] methodNames = new String[allow.size()];
/*  594 */     return (String[])allow.toArray(methodNames);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Servlet getServlet()
/*      */   {
/*  604 */     return this.instance;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setServlet(Servlet servlet)
/*      */   {
/*  613 */     this.instance = servlet;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setServletSecurityAnnotationScanRequired(boolean b)
/*      */   {
/*  622 */     this.servletSecurityAnnotationScanRequired = b;
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
/*      */   public void backgroundProcess()
/*      */   {
/*  635 */     super.backgroundProcess();
/*      */     
/*  637 */     if (!getState().isAvailable()) {
/*  638 */       return;
/*      */     }
/*  640 */     if ((getServlet() instanceof PeriodicEventListener)) {
/*  641 */       ((PeriodicEventListener)getServlet()).periodicEvent();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Throwable getRootCause(ServletException e)
/*      */   {
/*  653 */     Throwable rootCause = e;
/*  654 */     Throwable rootCauseCheck = null;
/*      */     
/*  656 */     int loops = 0;
/*      */     do {
/*  658 */       loops++;
/*  659 */       rootCauseCheck = rootCause.getCause();
/*  660 */       if (rootCauseCheck != null)
/*  661 */         rootCause = rootCauseCheck;
/*  662 */     } while ((rootCauseCheck != null) && (loops < 20));
/*  663 */     return rootCause;
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
/*      */   public void addChild(Container child)
/*      */   {
/*  677 */     throw new IllegalStateException(sm.getString("standardWrapper.notChild"));
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public void addInitParameter(String name, String value)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 40	org/apache/catalina/core/StandardWrapper:parametersLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
/*      */     //   4: invokevirtual 122	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
/*      */     //   7: invokevirtual 123	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:lock	()V
/*      */     //   10: aload_0
/*      */     //   11: getfield 17	org/apache/catalina/core/StandardWrapper:parameters	Ljava/util/HashMap;
/*      */     //   14: aload_1
/*      */     //   15: aload_2
/*      */     //   16: invokevirtual 124	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   19: pop
/*      */     //   20: aload_0
/*      */     //   21: getfield 40	org/apache/catalina/core/StandardWrapper:parametersLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
/*      */     //   24: invokevirtual 122	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
/*      */     //   27: invokevirtual 125	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
/*      */     //   30: goto +16 -> 46
/*      */     //   33: astore_3
/*      */     //   34: aload_0
/*      */     //   35: getfield 40	org/apache/catalina/core/StandardWrapper:parametersLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
/*      */     //   38: invokevirtual 122	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
/*      */     //   41: invokevirtual 125	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
/*      */     //   44: aload_3
/*      */     //   45: athrow
/*      */     //   46: aload_0
/*      */     //   47: ldc 126
/*      */     //   49: aload_1
/*      */     //   50: invokevirtual 127	org/apache/catalina/core/StandardWrapper:fireContainerEvent	(Ljava/lang/String;Ljava/lang/Object;)V
/*      */     //   53: return
/*      */     // Line number table:
/*      */     //   Java source line #691	-> byte code offset #0
/*      */     //   Java source line #693	-> byte code offset #10
/*      */     //   Java source line #695	-> byte code offset #20
/*      */     //   Java source line #696	-> byte code offset #30
/*      */     //   Java source line #695	-> byte code offset #33
/*      */     //   Java source line #697	-> byte code offset #46
/*      */     //   Java source line #699	-> byte code offset #53
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	54	0	this	StandardWrapper
/*      */     //   0	54	1	name	String
/*      */     //   0	54	2	value	String
/*      */     //   33	12	3	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   10	20	33	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public void addMapping(String mapping)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 41	org/apache/catalina/core/StandardWrapper:mappingsLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
/*      */     //   4: invokevirtual 122	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
/*      */     //   7: invokevirtual 123	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:lock	()V
/*      */     //   10: aload_0
/*      */     //   11: getfield 14	org/apache/catalina/core/StandardWrapper:mappings	Ljava/util/ArrayList;
/*      */     //   14: aload_1
/*      */     //   15: invokevirtual 128	java/util/ArrayList:add	(Ljava/lang/Object;)Z
/*      */     //   18: pop
/*      */     //   19: aload_0
/*      */     //   20: getfield 41	org/apache/catalina/core/StandardWrapper:mappingsLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
/*      */     //   23: invokevirtual 122	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
/*      */     //   26: invokevirtual 125	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
/*      */     //   29: goto +16 -> 45
/*      */     //   32: astore_2
/*      */     //   33: aload_0
/*      */     //   34: getfield 41	org/apache/catalina/core/StandardWrapper:mappingsLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
/*      */     //   37: invokevirtual 122	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
/*      */     //   40: invokevirtual 125	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
/*      */     //   43: aload_2
/*      */     //   44: athrow
/*      */     //   45: aload_0
/*      */     //   46: getfield 129	org/apache/catalina/core/StandardWrapper:parent	Lorg/apache/catalina/Container;
/*      */     //   49: invokeinterface 130 1 0
/*      */     //   54: getstatic 131	org/apache/catalina/LifecycleState:STARTED	Lorg/apache/catalina/LifecycleState;
/*      */     //   57: invokevirtual 132	org/apache/catalina/LifecycleState:equals	(Ljava/lang/Object;)Z
/*      */     //   60: ifeq +10 -> 70
/*      */     //   63: aload_0
/*      */     //   64: ldc -122
/*      */     //   66: aload_1
/*      */     //   67: invokevirtual 127	org/apache/catalina/core/StandardWrapper:fireContainerEvent	(Ljava/lang/String;Ljava/lang/Object;)V
/*      */     //   70: return
/*      */     // Line number table:
/*      */     //   Java source line #710	-> byte code offset #0
/*      */     //   Java source line #712	-> byte code offset #10
/*      */     //   Java source line #714	-> byte code offset #19
/*      */     //   Java source line #715	-> byte code offset #29
/*      */     //   Java source line #714	-> byte code offset #32
/*      */     //   Java source line #716	-> byte code offset #45
/*      */     //   Java source line #717	-> byte code offset #63
/*      */     //   Java source line #719	-> byte code offset #70
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	71	0	this	StandardWrapper
/*      */     //   0	71	1	mapping	String
/*      */     //   32	12	2	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   10	19	32	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public void addSecurityReference(String name, String link)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 42	org/apache/catalina/core/StandardWrapper:referencesLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
/*      */     //   4: invokevirtual 122	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
/*      */     //   7: invokevirtual 123	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:lock	()V
/*      */     //   10: aload_0
/*      */     //   11: getfield 18	org/apache/catalina/core/StandardWrapper:references	Ljava/util/HashMap;
/*      */     //   14: aload_1
/*      */     //   15: aload_2
/*      */     //   16: invokevirtual 124	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   19: pop
/*      */     //   20: aload_0
/*      */     //   21: getfield 42	org/apache/catalina/core/StandardWrapper:referencesLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
/*      */     //   24: invokevirtual 122	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
/*      */     //   27: invokevirtual 125	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
/*      */     //   30: goto +16 -> 46
/*      */     //   33: astore_3
/*      */     //   34: aload_0
/*      */     //   35: getfield 42	org/apache/catalina/core/StandardWrapper:referencesLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
/*      */     //   38: invokevirtual 122	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
/*      */     //   41: invokevirtual 125	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
/*      */     //   44: aload_3
/*      */     //   45: athrow
/*      */     //   46: aload_0
/*      */     //   47: ldc -121
/*      */     //   49: aload_1
/*      */     //   50: invokevirtual 127	org/apache/catalina/core/StandardWrapper:fireContainerEvent	(Ljava/lang/String;Ljava/lang/Object;)V
/*      */     //   53: return
/*      */     // Line number table:
/*      */     //   Java source line #732	-> byte code offset #0
/*      */     //   Java source line #734	-> byte code offset #10
/*      */     //   Java source line #736	-> byte code offset #20
/*      */     //   Java source line #737	-> byte code offset #30
/*      */     //   Java source line #736	-> byte code offset #33
/*      */     //   Java source line #738	-> byte code offset #46
/*      */     //   Java source line #740	-> byte code offset #53
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	54	0	this	StandardWrapper
/*      */     //   0	54	1	name	String
/*      */     //   0	54	2	link	String
/*      */     //   33	12	3	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   10	20	33	finally
/*      */   }
/*      */   
/*      */   public Servlet allocate()
/*      */     throws ServletException
/*      */   {
/*  760 */     if (this.unloading) {
/*  761 */       throw new ServletException(sm.getString("standardWrapper.unloading", new Object[] { getName() }));
/*      */     }
/*      */     
/*  764 */     boolean newInstance = false;
/*      */     
/*      */ 
/*  767 */     if (!this.singleThreadModel)
/*      */     {
/*  769 */       if ((this.instance == null) || (!this.instanceInitialized)) {
/*  770 */         synchronized (this) {
/*  771 */           if (this.instance == null) {
/*      */             try {
/*  773 */               if (log.isDebugEnabled()) {
/*  774 */                 log.debug("Allocating non-STM instance");
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*  779 */               this.instance = loadServlet();
/*  780 */               newInstance = true;
/*  781 */               if (!this.singleThreadModel)
/*      */               {
/*      */ 
/*      */ 
/*  785 */                 this.countAllocated.incrementAndGet();
/*      */               }
/*      */             } catch (ServletException e) {
/*  788 */               throw e;
/*      */             } catch (Throwable e) {
/*  790 */               ExceptionUtils.handleThrowable(e);
/*  791 */               throw new ServletException(sm.getString("standardWrapper.allocate"), e);
/*      */             }
/*      */           }
/*  794 */           if (!this.instanceInitialized) {
/*  795 */             initServlet(this.instance);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  800 */       if (this.singleThreadModel) {
/*  801 */         if (newInstance)
/*      */         {
/*      */ 
/*  804 */           synchronized (this.instancePool) {
/*  805 */             this.instancePool.push(this.instance);
/*  806 */             this.nInstances += 1;
/*      */           }
/*      */         }
/*      */       } else {
/*  810 */         if (log.isTraceEnabled()) {
/*  811 */           log.trace("  Returning non-STM instance");
/*      */         }
/*      */         
/*      */ 
/*  815 */         if (!newInstance) {
/*  816 */           this.countAllocated.incrementAndGet();
/*      */         }
/*  818 */         return this.instance;
/*      */       }
/*      */     }
/*      */     
/*  822 */     synchronized (this.instancePool) {
/*  823 */       while (this.countAllocated.get() >= this.nInstances)
/*      */       {
/*  825 */         if (this.nInstances < this.maxInstances) {
/*      */           try {
/*  827 */             this.instancePool.push(loadServlet());
/*  828 */             this.nInstances += 1;
/*      */           } catch (ServletException e) {
/*  830 */             throw e;
/*      */           } catch (Throwable e) {
/*  832 */             ExceptionUtils.handleThrowable(e);
/*  833 */             throw new ServletException(sm.getString("standardWrapper.allocate"), e);
/*      */           }
/*      */         } else {
/*      */           try {
/*  837 */             this.instancePool.wait();
/*      */           }
/*      */           catch (InterruptedException localInterruptedException) {}
/*      */         }
/*      */       }
/*      */       
/*  843 */       if (log.isTraceEnabled()) {
/*  844 */         log.trace("  Returning allocated STM instance");
/*      */       }
/*  846 */       this.countAllocated.incrementAndGet();
/*  847 */       return (Servlet)this.instancePool.pop();
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
/*      */   public void deallocate(Servlet servlet)
/*      */     throws ServletException
/*      */   {
/*  865 */     if (!this.singleThreadModel) {
/*  866 */       this.countAllocated.decrementAndGet();
/*  867 */       return;
/*      */     }
/*      */     
/*      */ 
/*  871 */     synchronized (this.instancePool) {
/*  872 */       this.countAllocated.decrementAndGet();
/*  873 */       this.instancePool.push(servlet);
/*  874 */       this.instancePool.notify();
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
/*      */   public String findInitParameter(String name)
/*      */   {
/*  889 */     this.parametersLock.readLock().lock();
/*      */     try {
/*  891 */       return (String)this.parameters.get(name);
/*      */     } finally {
/*  893 */       this.parametersLock.readLock().unlock();
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
/*      */   public String[] findInitParameters()
/*      */   {
/*  906 */     this.parametersLock.readLock().lock();
/*      */     try {
/*  908 */       String[] results = new String[this.parameters.size()];
/*  909 */       return (String[])this.parameters.keySet().toArray(results);
/*      */     } finally {
/*  911 */       this.parametersLock.readLock().unlock();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] findMappings()
/*      */   {
/*  923 */     this.mappingsLock.readLock().lock();
/*      */     try {
/*  925 */       return (String[])this.mappings.toArray(new String[this.mappings.size()]);
/*      */     } finally {
/*  927 */       this.mappingsLock.readLock().unlock();
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
/*      */   public String findSecurityReference(String name)
/*      */   {
/*  942 */     this.referencesLock.readLock().lock();
/*      */     try {
/*  944 */       return (String)this.references.get(name);
/*      */     } finally {
/*  946 */       this.referencesLock.readLock().unlock();
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
/*      */   public String[] findSecurityReferences()
/*      */   {
/*  959 */     this.referencesLock.readLock().lock();
/*      */     try {
/*  961 */       String[] results = new String[this.references.size()];
/*  962 */       return (String[])this.references.keySet().toArray(results);
/*      */     } finally {
/*  964 */       this.referencesLock.readLock().unlock();
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
/*      */   public synchronized void load()
/*      */     throws ServletException
/*      */   {
/*  989 */     this.instance = loadServlet();
/*      */     
/*  991 */     if (!this.instanceInitialized) {
/*  992 */       initServlet(this.instance);
/*      */     }
/*      */     
/*  995 */     if (this.isJspServlet) {
/*  996 */       StringBuilder oname = new StringBuilder(getDomain());
/*      */       
/*  998 */       oname.append(":type=JspMonitor");
/*      */       
/* 1000 */       oname.append(getWebModuleKeyProperties());
/*      */       
/* 1002 */       oname.append(",name=");
/* 1003 */       oname.append(getName());
/*      */       
/* 1005 */       oname.append(getJ2EEKeyProperties());
/*      */       try
/*      */       {
/* 1008 */         this.jspMonitorON = new ObjectName(oname.toString());
/* 1009 */         Registry.getRegistry(null, null)
/* 1010 */           .registerComponent(this.instance, this.jspMonitorON, null);
/*      */       } catch (Exception ex) {
/* 1012 */         log.info("Error registering JSP monitoring with jmx " + this.instance);
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
/*      */   public synchronized Servlet loadServlet()
/*      */     throws ServletException
/*      */   {
/* 1030 */     if ((!this.singleThreadModel) && (this.instance != null)) {
/* 1031 */       return this.instance;
/*      */     }
/* 1033 */     PrintStream out = System.out;
/* 1034 */     if (this.swallowOutput) {
/* 1035 */       SystemLogHandler.startCapture();
/*      */     }
/*      */     
/*      */     try
/*      */     {
/* 1040 */       long t1 = System.currentTimeMillis();
/*      */       
/* 1042 */       if (this.servletClass == null) {
/* 1043 */         unavailable(null);
/*      */         
/* 1045 */         throw new ServletException(sm.getString("standardWrapper.notClass", new Object[] {getName() }));
/*      */       }
/*      */       
/* 1048 */       InstanceManager instanceManager = ((StandardContext)getParent()).getInstanceManager();
/*      */       try {
/* 1050 */         servlet = (Servlet)instanceManager.newInstance(this.servletClass);
/*      */       } catch (ClassCastException e) { Servlet servlet;
/* 1052 */         unavailable(null);
/*      */         
/*      */ 
/* 1055 */         throw new ServletException(sm.getString("standardWrapper.notServlet", new Object[] { this.servletClass }), e);
/*      */       } catch (Throwable e) {
/* 1057 */         e = ExceptionUtils.unwrapInvocationTargetException(e);
/* 1058 */         ExceptionUtils.handleThrowable(e);
/* 1059 */         unavailable(null);
/*      */         
/*      */ 
/*      */ 
/* 1063 */         if (log.isDebugEnabled()) {
/* 1064 */           log.debug(sm.getString("standardWrapper.instantiate", new Object[] { this.servletClass }), e);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1069 */         throw new ServletException(sm.getString("standardWrapper.instantiate", new Object[] { this.servletClass }), e);
/*      */       }
/*      */       Servlet servlet;
/* 1072 */       if (this.multipartConfigElement == null)
/*      */       {
/* 1074 */         MultipartConfig annotation = (MultipartConfig)servlet.getClass().getAnnotation(MultipartConfig.class);
/* 1075 */         if (annotation != null) {
/* 1076 */           this.multipartConfigElement = new MultipartConfigElement(annotation);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1081 */       processServletSecurityAnnotation(servlet.getClass());
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1086 */       if ((servlet instanceof ContainerServlet)) {
/* 1087 */         ((ContainerServlet)servlet).setWrapper(this);
/*      */       }
/*      */       
/* 1090 */       this.classLoadTime = ((int)(System.currentTimeMillis() - t1));
/*      */       
/* 1092 */       if ((servlet instanceof SingleThreadModel)) {
/* 1093 */         if (this.instancePool == null) {
/* 1094 */           this.instancePool = new Stack();
/*      */         }
/* 1096 */         this.singleThreadModel = true;
/*      */       }
/*      */       
/* 1099 */       initServlet(servlet);
/*      */       
/* 1101 */       fireContainerEvent("load", this);
/*      */       
/* 1103 */       this.loadTime = (System.currentTimeMillis() - t1);
/*      */     } finally { String log;
/* 1105 */       if (this.swallowOutput) {
/* 1106 */         String log = SystemLogHandler.stopCapture();
/* 1107 */         if ((log != null) && (log.length() > 0)) {
/* 1108 */           if (getServletContext() != null) {
/* 1109 */             getServletContext().log(log);
/*      */           } else
/* 1111 */             out.println(log);
/*      */         }
/*      */       }
/*      */     }
/*      */     Servlet servlet;
/* 1116 */     return servlet;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void servletSecurityAnnotationScan()
/*      */     throws ServletException
/*      */   {
/* 1125 */     if (getServlet() == null) {
/* 1126 */       Class<?> clazz = null;
/*      */       try {
/* 1128 */         clazz = ((Context)getParent()).getLoader().getClassLoader().loadClass(
/* 1129 */           getServletClass());
/* 1130 */         processServletSecurityAnnotation(clazz);
/*      */ 
/*      */       }
/*      */       catch (ClassNotFoundException localClassNotFoundException) {}
/*      */     }
/* 1135 */     else if (this.servletSecurityAnnotationScanRequired) {
/* 1136 */       processServletSecurityAnnotation(getServlet().getClass());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void processServletSecurityAnnotation(Class<?> clazz)
/*      */   {
/* 1143 */     this.servletSecurityAnnotationScanRequired = false;
/*      */     
/* 1145 */     Context ctxt = (Context)getParent();
/*      */     
/* 1147 */     if (ctxt.getIgnoreAnnotations()) {
/* 1148 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1152 */     ServletSecurity secAnnotation = (ServletSecurity)clazz.getAnnotation(ServletSecurity.class);
/* 1153 */     if (secAnnotation != null) {
/* 1154 */       ctxt.addServletSecurity(new ApplicationServletRegistration(this, ctxt), new ServletSecurityElement(secAnnotation));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private synchronized void initServlet(Servlet servlet)
/*      */     throws ServletException
/*      */   {
/* 1163 */     if ((this.instanceInitialized) && (!this.singleThreadModel)) { return;
/*      */     }
/*      */     try
/*      */     {
/* 1167 */       if (Globals.IS_SECURITY_ENABLED) {
/* 1168 */         boolean success = false;
/*      */         try {
/* 1170 */           Object[] args = { this.facade };
/* 1171 */           SecurityUtil.doAsPrivilege("init", servlet, classType, args);
/*      */           
/*      */ 
/*      */ 
/* 1175 */           success = true;
/*      */         } finally {
/* 1177 */           if (!success)
/*      */           {
/* 1179 */             SecurityUtil.remove(servlet);
/*      */           }
/*      */         }
/*      */       } else {
/* 1183 */         servlet.init(this.facade);
/*      */       }
/*      */       
/* 1186 */       this.instanceInitialized = true;
/*      */     } catch (UnavailableException f) {
/* 1188 */       unavailable(f);
/* 1189 */       throw f;
/*      */     }
/*      */     catch (ServletException f)
/*      */     {
/* 1193 */       throw f;
/*      */     } catch (Throwable f) {
/* 1195 */       ExceptionUtils.handleThrowable(f);
/* 1196 */       getServletContext().log("StandardWrapper.Throwable", f);
/*      */       
/*      */ 
/*      */ 
/* 1200 */       throw new ServletException(sm.getString("standardWrapper.initException", new Object[] {getName() }), f);
/*      */     }
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public void removeInitParameter(String name)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 40	org/apache/catalina/core/StandardWrapper:parametersLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
/*      */     //   4: invokevirtual 122	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
/*      */     //   7: invokevirtual 123	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:lock	()V
/*      */     //   10: aload_0
/*      */     //   11: getfield 17	org/apache/catalina/core/StandardWrapper:parameters	Ljava/util/HashMap;
/*      */     //   14: aload_1
/*      */     //   15: invokevirtual 241	java/util/HashMap:remove	(Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   18: pop
/*      */     //   19: aload_0
/*      */     //   20: getfield 40	org/apache/catalina/core/StandardWrapper:parametersLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
/*      */     //   23: invokevirtual 122	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
/*      */     //   26: invokevirtual 125	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
/*      */     //   29: goto +16 -> 45
/*      */     //   32: astore_2
/*      */     //   33: aload_0
/*      */     //   34: getfield 40	org/apache/catalina/core/StandardWrapper:parametersLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
/*      */     //   37: invokevirtual 122	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
/*      */     //   40: invokevirtual 125	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
/*      */     //   43: aload_2
/*      */     //   44: athrow
/*      */     //   45: aload_0
/*      */     //   46: ldc -14
/*      */     //   48: aload_1
/*      */     //   49: invokevirtual 127	org/apache/catalina/core/StandardWrapper:fireContainerEvent	(Ljava/lang/String;Ljava/lang/Object;)V
/*      */     //   52: return
/*      */     // Line number table:
/*      */     //   Java source line #1212	-> byte code offset #0
/*      */     //   Java source line #1214	-> byte code offset #10
/*      */     //   Java source line #1216	-> byte code offset #19
/*      */     //   Java source line #1217	-> byte code offset #29
/*      */     //   Java source line #1216	-> byte code offset #32
/*      */     //   Java source line #1218	-> byte code offset #45
/*      */     //   Java source line #1220	-> byte code offset #52
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	53	0	this	StandardWrapper
/*      */     //   0	53	1	name	String
/*      */     //   32	12	2	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   10	19	32	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public void removeMapping(String mapping)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 41	org/apache/catalina/core/StandardWrapper:mappingsLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
/*      */     //   4: invokevirtual 122	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
/*      */     //   7: invokevirtual 123	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:lock	()V
/*      */     //   10: aload_0
/*      */     //   11: getfield 14	org/apache/catalina/core/StandardWrapper:mappings	Ljava/util/ArrayList;
/*      */     //   14: aload_1
/*      */     //   15: invokevirtual 243	java/util/ArrayList:remove	(Ljava/lang/Object;)Z
/*      */     //   18: pop
/*      */     //   19: aload_0
/*      */     //   20: getfield 41	org/apache/catalina/core/StandardWrapper:mappingsLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
/*      */     //   23: invokevirtual 122	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
/*      */     //   26: invokevirtual 125	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
/*      */     //   29: goto +16 -> 45
/*      */     //   32: astore_2
/*      */     //   33: aload_0
/*      */     //   34: getfield 41	org/apache/catalina/core/StandardWrapper:mappingsLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
/*      */     //   37: invokevirtual 122	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
/*      */     //   40: invokevirtual 125	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
/*      */     //   43: aload_2
/*      */     //   44: athrow
/*      */     //   45: aload_0
/*      */     //   46: getfield 129	org/apache/catalina/core/StandardWrapper:parent	Lorg/apache/catalina/Container;
/*      */     //   49: invokeinterface 130 1 0
/*      */     //   54: getstatic 131	org/apache/catalina/LifecycleState:STARTED	Lorg/apache/catalina/LifecycleState;
/*      */     //   57: invokevirtual 132	org/apache/catalina/LifecycleState:equals	(Ljava/lang/Object;)Z
/*      */     //   60: ifeq +10 -> 70
/*      */     //   63: aload_0
/*      */     //   64: ldc -12
/*      */     //   66: aload_1
/*      */     //   67: invokevirtual 127	org/apache/catalina/core/StandardWrapper:fireContainerEvent	(Ljava/lang/String;Ljava/lang/Object;)V
/*      */     //   70: return
/*      */     // Line number table:
/*      */     //   Java source line #1231	-> byte code offset #0
/*      */     //   Java source line #1233	-> byte code offset #10
/*      */     //   Java source line #1235	-> byte code offset #19
/*      */     //   Java source line #1236	-> byte code offset #29
/*      */     //   Java source line #1235	-> byte code offset #32
/*      */     //   Java source line #1237	-> byte code offset #45
/*      */     //   Java source line #1238	-> byte code offset #63
/*      */     //   Java source line #1240	-> byte code offset #70
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	71	0	this	StandardWrapper
/*      */     //   0	71	1	mapping	String
/*      */     //   32	12	2	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   10	19	32	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public void removeSecurityReference(String name)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 42	org/apache/catalina/core/StandardWrapper:referencesLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
/*      */     //   4: invokevirtual 122	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
/*      */     //   7: invokevirtual 123	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:lock	()V
/*      */     //   10: aload_0
/*      */     //   11: getfield 18	org/apache/catalina/core/StandardWrapper:references	Ljava/util/HashMap;
/*      */     //   14: aload_1
/*      */     //   15: invokevirtual 241	java/util/HashMap:remove	(Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   18: pop
/*      */     //   19: aload_0
/*      */     //   20: getfield 42	org/apache/catalina/core/StandardWrapper:referencesLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
/*      */     //   23: invokevirtual 122	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
/*      */     //   26: invokevirtual 125	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
/*      */     //   29: goto +16 -> 45
/*      */     //   32: astore_2
/*      */     //   33: aload_0
/*      */     //   34: getfield 42	org/apache/catalina/core/StandardWrapper:referencesLock	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
/*      */     //   37: invokevirtual 122	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
/*      */     //   40: invokevirtual 125	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
/*      */     //   43: aload_2
/*      */     //   44: athrow
/*      */     //   45: aload_0
/*      */     //   46: ldc -11
/*      */     //   48: aload_1
/*      */     //   49: invokevirtual 127	org/apache/catalina/core/StandardWrapper:fireContainerEvent	(Ljava/lang/String;Ljava/lang/Object;)V
/*      */     //   52: return
/*      */     // Line number table:
/*      */     //   Java source line #1251	-> byte code offset #0
/*      */     //   Java source line #1253	-> byte code offset #10
/*      */     //   Java source line #1255	-> byte code offset #19
/*      */     //   Java source line #1256	-> byte code offset #29
/*      */     //   Java source line #1255	-> byte code offset #32
/*      */     //   Java source line #1257	-> byte code offset #45
/*      */     //   Java source line #1259	-> byte code offset #52
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	53	0	this	StandardWrapper
/*      */     //   0	53	1	name	String
/*      */     //   32	12	2	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   10	19	32	finally
/*      */   }
/*      */   
/*      */   public void unavailable(UnavailableException unavailable)
/*      */   {
/* 1271 */     getServletContext().log(sm.getString("standardWrapper.unavailable", new Object[] { getName() }));
/* 1272 */     if (unavailable == null) {
/* 1273 */       setAvailable(Long.MAX_VALUE);
/* 1274 */     } else if (unavailable.isPermanent()) {
/* 1275 */       setAvailable(Long.MAX_VALUE);
/*      */     } else {
/* 1277 */       int unavailableSeconds = unavailable.getUnavailableSeconds();
/* 1278 */       if (unavailableSeconds <= 0)
/* 1279 */         unavailableSeconds = 60;
/* 1280 */       setAvailable(System.currentTimeMillis() + unavailableSeconds * 1000L);
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
/*      */   public synchronized void unload()
/*      */     throws ServletException
/*      */   {
/* 1301 */     if ((!this.singleThreadModel) && (this.instance == null))
/* 1302 */       return;
/* 1303 */     this.unloading = true;
/*      */     
/*      */ 
/*      */ 
/* 1307 */     if (this.countAllocated.get() > 0) {
/* 1308 */       int nRetries = 0;
/* 1309 */       long delay = this.unloadDelay / 20L;
/* 1310 */       while ((nRetries < 21) && (this.countAllocated.get() > 0)) {
/* 1311 */         if (nRetries % 10 == 0) {
/* 1312 */           log.info(sm.getString("standardWrapper.waiting", new Object[] {this.countAllocated
/* 1313 */             .toString(), 
/* 1314 */             getName() }));
/*      */         }
/*      */         try {
/* 1317 */           Thread.sleep(delay);
/*      */         }
/*      */         catch (InterruptedException localInterruptedException) {}
/*      */         
/* 1321 */         nRetries++;
/*      */       }
/*      */     }
/*      */     
/* 1325 */     if (this.instanceInitialized) {
/* 1326 */       PrintStream out = System.out;
/* 1327 */       if (this.swallowOutput) {
/* 1328 */         SystemLogHandler.startCapture();
/*      */       }
/*      */       
/*      */       try
/*      */       {
/* 1333 */         if (Globals.IS_SECURITY_ENABLED) {
/*      */           try {
/* 1335 */             SecurityUtil.doAsPrivilege("destroy", this.instance);
/*      */           } finally {
/* 1337 */             SecurityUtil.remove(this.instance);
/*      */           }
/*      */         } else {
/* 1340 */           this.instance.destroy();
/*      */         }
/*      */       } catch (Throwable t) {
/*      */         String log;
/* 1344 */         t = ExceptionUtils.unwrapInvocationTargetException(t);
/* 1345 */         ExceptionUtils.handleThrowable(t);
/* 1346 */         this.instance = null;
/* 1347 */         this.instancePool = null;
/* 1348 */         this.nInstances = 0;
/* 1349 */         fireContainerEvent("unload", this);
/* 1350 */         this.unloading = false;
/*      */         
/* 1352 */         throw new ServletException(sm.getString("standardWrapper.destroyException", new Object[] {getName() }), t);
/*      */       }
/*      */       finally
/*      */       {
/* 1356 */         if (!((Context)getParent()).getIgnoreAnnotations()) {
/*      */           try {
/* 1358 */             ((Context)getParent()).getInstanceManager().destroyInstance(this.instance);
/*      */           } catch (Throwable t) {
/* 1360 */             ExceptionUtils.handleThrowable(t);
/* 1361 */             log.error(sm.getString("standardWrapper.destroyInstance", new Object[] { getName() }), t);
/*      */           }
/*      */         }
/*      */         
/* 1365 */         if (this.swallowOutput) {
/* 1366 */           String log = SystemLogHandler.stopCapture();
/* 1367 */           if ((log != null) && (log.length() > 0)) {
/* 1368 */             if (getServletContext() != null) {
/* 1369 */               getServletContext().log(log);
/*      */             } else {
/* 1371 */               out.println(log);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1379 */     this.instance = null;
/* 1380 */     this.instanceInitialized = false;
/*      */     
/* 1382 */     if ((this.isJspServlet) && (this.jspMonitorON != null)) {
/* 1383 */       Registry.getRegistry(null, null).unregisterComponent(this.jspMonitorON);
/*      */     }
/*      */     
/* 1386 */     if ((this.singleThreadModel) && (this.instancePool != null)) {
/*      */       try {
/* 1388 */         while (!this.instancePool.isEmpty()) {
/* 1389 */           Servlet s = (Servlet)this.instancePool.pop();
/* 1390 */           if (Globals.IS_SECURITY_ENABLED) {
/*      */             try {
/* 1392 */               SecurityUtil.doAsPrivilege("destroy", s);
/*      */             } finally {
/* 1394 */               SecurityUtil.remove(s);
/*      */             }
/*      */           } else {
/* 1397 */             s.destroy();
/*      */           }
/*      */           
/* 1400 */           if (!((Context)getParent()).getIgnoreAnnotations()) {
/* 1401 */             ((StandardContext)getParent()).getInstanceManager().destroyInstance(s);
/*      */           }
/*      */         }
/*      */       } catch (Throwable t) {
/* 1405 */         t = ExceptionUtils.unwrapInvocationTargetException(t);
/* 1406 */         ExceptionUtils.handleThrowable(t);
/* 1407 */         this.instancePool = null;
/* 1408 */         this.nInstances = 0;
/* 1409 */         this.unloading = false;
/* 1410 */         fireContainerEvent("unload", this);
/*      */         
/* 1412 */         throw new ServletException(sm.getString("standardWrapper.destroyException", new Object[] {
/* 1413 */           getName() }), t);
/*      */       }
/* 1415 */       this.instancePool = null;
/* 1416 */       this.nInstances = 0;
/*      */     }
/*      */     
/* 1419 */     this.singleThreadModel = false;
/*      */     
/* 1421 */     this.unloading = false;
/* 1422 */     fireContainerEvent("unload", this);
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
/*      */   public String getInitParameter(String name)
/*      */   {
/* 1439 */     return findInitParameter(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Enumeration<String> getInitParameterNames()
/*      */   {
/* 1451 */     this.parametersLock.readLock().lock();
/*      */     try {
/* 1453 */       return Collections.enumeration(this.parameters.keySet());
/*      */     } finally {
/* 1455 */       this.parametersLock.readLock().unlock();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ServletContext getServletContext()
/*      */   {
/* 1467 */     if (this.parent == null)
/* 1468 */       return null;
/* 1469 */     if (!(this.parent instanceof Context)) {
/* 1470 */       return null;
/*      */     }
/* 1472 */     return ((Context)this.parent).getServletContext();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getServletName()
/*      */   {
/* 1483 */     return getName();
/*      */   }
/*      */   
/*      */   public long getProcessingTime()
/*      */   {
/* 1488 */     return this.swValve.getProcessingTime();
/*      */   }
/*      */   
/*      */   public long getMaxTime() {
/* 1492 */     return this.swValve.getMaxTime();
/*      */   }
/*      */   
/*      */   public long getMinTime() {
/* 1496 */     return this.swValve.getMinTime();
/*      */   }
/*      */   
/*      */   public int getRequestCount() {
/* 1500 */     return this.swValve.getRequestCount();
/*      */   }
/*      */   
/*      */   public int getErrorCount() {
/* 1504 */     return this.swValve.getErrorCount();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void incrementErrorCount()
/*      */   {
/* 1512 */     this.swValve.incrementErrorCount();
/*      */   }
/*      */   
/*      */   public long getLoadTime() {
/* 1516 */     return this.loadTime;
/*      */   }
/*      */   
/*      */   public int getClassLoadTime() {
/* 1520 */     return this.classLoadTime;
/*      */   }
/*      */   
/*      */   public MultipartConfigElement getMultipartConfigElement()
/*      */   {
/* 1525 */     return this.multipartConfigElement;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setMultipartConfigElement(MultipartConfigElement multipartConfigElement)
/*      */   {
/* 1531 */     this.multipartConfigElement = multipartConfigElement;
/*      */   }
/*      */   
/*      */   public boolean isAsyncSupported()
/*      */   {
/* 1536 */     return this.asyncSupported;
/*      */   }
/*      */   
/*      */   public void setAsyncSupported(boolean asyncSupported)
/*      */   {
/* 1541 */     this.asyncSupported = asyncSupported;
/*      */   }
/*      */   
/*      */   public boolean isEnabled()
/*      */   {
/* 1546 */     return this.enabled;
/*      */   }
/*      */   
/*      */   public void setEnabled(boolean enabled)
/*      */   {
/* 1551 */     this.enabled = enabled;
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
/*      */   @Deprecated
/*      */   protected boolean isContainerProvidedServlet(String classname)
/*      */   {
/* 1572 */     if (classname.startsWith("org.apache.catalina.")) {
/* 1573 */       return true;
/*      */     }
/*      */     try
/*      */     {
/* 1577 */       Class<?> clazz = getClass().getClassLoader().loadClass(classname);
/* 1578 */       return ContainerServlet.class.isAssignableFrom(clazz);
/*      */     } catch (Throwable t) {
/* 1580 */       ExceptionUtils.handleThrowable(t); }
/* 1581 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Method[] getAllDeclaredMethods(Class<?> c)
/*      */   {
/* 1589 */     if (c.equals(HttpServlet.class)) {
/* 1590 */       return null;
/*      */     }
/*      */     
/* 1593 */     Method[] parentMethods = getAllDeclaredMethods(c.getSuperclass());
/*      */     
/* 1595 */     Method[] thisMethods = c.getDeclaredMethods();
/* 1596 */     if (thisMethods.length == 0) {
/* 1597 */       return parentMethods;
/*      */     }
/*      */     
/* 1600 */     if ((parentMethods != null) && (parentMethods.length > 0)) {
/* 1601 */       Method[] allMethods = new Method[parentMethods.length + thisMethods.length];
/*      */       
/* 1603 */       System.arraycopy(parentMethods, 0, allMethods, 0, parentMethods.length);
/*      */       
/* 1605 */       System.arraycopy(thisMethods, 0, allMethods, parentMethods.length, thisMethods.length);
/*      */       
/*      */ 
/* 1608 */       thisMethods = allMethods;
/*      */     }
/*      */     
/* 1611 */     return thisMethods;
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
/*      */   protected synchronized void startInternal()
/*      */     throws LifecycleException
/*      */   {
/* 1629 */     if (getObjectName() != null)
/*      */     {
/* 1631 */       Notification notification = new Notification("j2ee.state.starting", getObjectName(), this.sequenceNumber++);
/*      */       
/* 1633 */       this.broadcaster.sendNotification(notification);
/*      */     }
/*      */     
/*      */ 
/* 1637 */     super.startInternal();
/*      */     
/* 1639 */     setAvailable(0L);
/*      */     
/*      */ 
/* 1642 */     if (getObjectName() != null)
/*      */     {
/* 1644 */       Notification notification = new Notification("j2ee.state.running", getObjectName(), this.sequenceNumber++);
/*      */       
/* 1646 */       this.broadcaster.sendNotification(notification);
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
/*      */   protected synchronized void stopInternal()
/*      */     throws LifecycleException
/*      */   {
/* 1662 */     setAvailable(Long.MAX_VALUE);
/*      */     
/*      */ 
/* 1665 */     if (getObjectName() != null)
/*      */     {
/* 1667 */       Notification notification = new Notification("j2ee.state.stopping", getObjectName(), this.sequenceNumber++);
/*      */       
/* 1669 */       this.broadcaster.sendNotification(notification);
/*      */     }
/*      */     
/*      */     try
/*      */     {
/* 1674 */       unload();
/*      */     } catch (ServletException e) {
/* 1676 */       getServletContext().log(sm
/* 1677 */         .getString("standardWrapper.unloadException", new Object[] {getName() }), e);
/*      */     }
/*      */     
/*      */ 
/* 1681 */     super.stopInternal();
/*      */     
/*      */ 
/* 1684 */     if (getObjectName() != null)
/*      */     {
/* 1686 */       Notification notification = new Notification("j2ee.state.stopped", getObjectName(), this.sequenceNumber++);
/*      */       
/* 1688 */       this.broadcaster.sendNotification(notification);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1693 */     Notification notification = new Notification("j2ee.object.deleted", getObjectName(), this.sequenceNumber++);
/*      */     
/* 1695 */     this.broadcaster.sendNotification(notification);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getObjectNameKeyProperties()
/*      */   {
/* 1703 */     StringBuilder keyProperties = new StringBuilder("j2eeType=Servlet");
/*      */     
/*      */ 
/* 1706 */     keyProperties.append(getWebModuleKeyProperties());
/*      */     
/* 1708 */     keyProperties.append(",name=");
/*      */     
/* 1710 */     String name = getName();
/* 1711 */     if (Util.objectNameValueNeedsQuote(name)) {
/* 1712 */       name = ObjectName.quote(name);
/*      */     }
/* 1714 */     keyProperties.append(name);
/*      */     
/* 1716 */     keyProperties.append(getJ2EEKeyProperties());
/*      */     
/* 1718 */     return keyProperties.toString();
/*      */   }
/*      */   
/*      */ 
/*      */   private String getWebModuleKeyProperties()
/*      */   {
/* 1724 */     StringBuilder keyProperties = new StringBuilder(",WebModule=//");
/* 1725 */     String hostName = getParent().getParent().getName();
/* 1726 */     if (hostName == null) {
/* 1727 */       keyProperties.append("DEFAULT");
/*      */     } else {
/* 1729 */       keyProperties.append(hostName);
/*      */     }
/*      */     
/* 1732 */     String contextName = ((Context)getParent()).getName();
/* 1733 */     if (!contextName.startsWith("/")) {
/* 1734 */       keyProperties.append('/');
/*      */     }
/* 1736 */     keyProperties.append(contextName);
/*      */     
/* 1738 */     return keyProperties.toString();
/*      */   }
/*      */   
/*      */   private String getJ2EEKeyProperties()
/*      */   {
/* 1743 */     StringBuilder keyProperties = new StringBuilder(",J2EEApplication=");
/*      */     
/* 1745 */     StandardContext ctx = null;
/* 1746 */     if ((this.parent instanceof StandardContext)) {
/* 1747 */       ctx = (StandardContext)getParent();
/*      */     }
/*      */     
/* 1750 */     if (ctx == null) {
/* 1751 */       keyProperties.append("none");
/*      */     } else {
/* 1753 */       keyProperties.append(ctx.getJ2EEApplication());
/*      */     }
/* 1755 */     keyProperties.append(",J2EEServer=");
/* 1756 */     if (ctx == null) {
/* 1757 */       keyProperties.append("none");
/*      */     } else {
/* 1759 */       keyProperties.append(ctx.getJ2EEServer());
/*      */     }
/*      */     
/* 1762 */     return keyProperties.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeNotificationListener(NotificationListener listener, NotificationFilter filter, Object object)
/*      */     throws ListenerNotFoundException
/*      */   {
/* 1773 */     this.broadcaster.removeNotificationListener(listener, filter, object);
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
/*      */   public MBeanNotificationInfo[] getNotificationInfo()
/*      */   {
/* 1786 */     if (this.notificationInfo == null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1815 */       this.notificationInfo = new MBeanNotificationInfo[] { new MBeanNotificationInfo(new String[] { "j2ee.object.created" }, Notification.class.getName(), "servlet is created"), new MBeanNotificationInfo(new String[] { "j2ee.state.starting" }, Notification.class.getName(), "servlet is starting"), new MBeanNotificationInfo(new String[] { "j2ee.state.running" }, Notification.class.getName(), "servlet is running"), new MBeanNotificationInfo(new String[] { "j2ee.state.stopped" }, Notification.class.getName(), "servlet start to stopped"), new MBeanNotificationInfo(new String[] { "j2ee.object.stopped" }, Notification.class.getName(), "servlet is stopped"), new MBeanNotificationInfo(new String[] { "j2ee.object.deleted" }, Notification.class.getName(), "servlet is deleted") };
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1821 */     return this.notificationInfo;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addNotificationListener(NotificationListener listener, NotificationFilter filter, Object object)
/*      */     throws IllegalArgumentException
/*      */   {
/* 1832 */     this.broadcaster.addNotificationListener(listener, filter, object);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeNotificationListener(NotificationListener listener)
/*      */     throws ListenerNotFoundException
/*      */   {
/* 1843 */     this.broadcaster.removeNotificationListener(listener);
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\StandardWrapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */