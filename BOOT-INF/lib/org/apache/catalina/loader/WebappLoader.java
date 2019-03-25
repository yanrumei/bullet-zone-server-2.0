/*     */ package org.apache.catalina.loader;
/*     */ 
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.io.File;
/*     */ import java.io.FilePermission;
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.net.URLDecoder;
/*     */ import javax.management.ObjectName;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.apache.catalina.Container;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.Globals;
/*     */ import org.apache.catalina.Lifecycle;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.LifecycleState;
/*     */ import org.apache.catalina.Loader;
/*     */ import org.apache.catalina.WebResourceRoot;
/*     */ import org.apache.catalina.util.LifecycleMBeanBase;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
/*     */ import org.apache.tomcat.util.modeler.Registry;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WebappLoader
/*     */   extends LifecycleMBeanBase
/*     */   implements Loader, PropertyChangeListener
/*     */ {
/*     */   public WebappLoader()
/*     */   {
/*  74 */     this(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WebappLoader(ClassLoader parent)
/*     */   {
/*  86 */     this.parentClassLoader = parent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  95 */   private WebappClassLoaderBase classLoader = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 101 */   private Context context = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 108 */   private boolean delegate = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 116 */   private String loaderClass = ParallelWebappClassLoader.class.getName();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 122 */   private ClassLoader parentClassLoader = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 128 */   private boolean reloadable = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 135 */   protected static final StringManager sm = StringManager.getManager("org.apache.catalina.loader");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 141 */   protected final PropertyChangeSupport support = new PropertyChangeSupport(this);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 147 */   private String classpath = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ClassLoader getClassLoader()
/*     */   {
/* 157 */     return this.classLoader;
/*     */   }
/*     */   
/*     */ 
/*     */   public Context getContext()
/*     */   {
/* 163 */     return this.context;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setContext(Context context)
/*     */   {
/* 170 */     if (this.context == context) {
/* 171 */       return;
/*     */     }
/*     */     
/* 174 */     if (getState().isAvailable())
/*     */     {
/* 176 */       throw new IllegalStateException(sm.getString("webappLoader.setContext.ise"));
/*     */     }
/*     */     
/*     */ 
/* 180 */     if (this.context != null) {
/* 181 */       this.context.removePropertyChangeListener(this);
/*     */     }
/*     */     
/*     */ 
/* 185 */     Context oldContext = this.context;
/* 186 */     this.context = context;
/* 187 */     this.support.firePropertyChange("context", oldContext, this.context);
/*     */     
/*     */ 
/* 190 */     if (this.context != null) {
/* 191 */       setReloadable(this.context.getReloadable());
/* 192 */       this.context.addPropertyChangeListener(this);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getDelegate()
/*     */   {
/* 203 */     return this.delegate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDelegate(boolean delegate)
/*     */   {
/* 215 */     boolean oldDelegate = this.delegate;
/* 216 */     this.delegate = delegate;
/* 217 */     this.support.firePropertyChange("delegate", Boolean.valueOf(oldDelegate), 
/* 218 */       Boolean.valueOf(this.delegate));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getLoaderClass()
/*     */   {
/* 226 */     return this.loaderClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLoaderClass(String loaderClass)
/*     */   {
/* 236 */     this.loaderClass = loaderClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getReloadable()
/*     */   {
/* 245 */     return this.reloadable;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setReloadable(boolean reloadable)
/*     */   {
/* 257 */     boolean oldReloadable = this.reloadable;
/* 258 */     this.reloadable = reloadable;
/* 259 */     this.support.firePropertyChange("reloadable", 
/* 260 */       Boolean.valueOf(oldReloadable), 
/* 261 */       Boolean.valueOf(this.reloadable));
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
/*     */   public void addPropertyChangeListener(PropertyChangeListener listener)
/*     */   {
/* 275 */     this.support.addPropertyChangeListener(listener);
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void backgroundProcess()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 10	org/apache/catalina/loader/WebappLoader:reloadable	Z
/*     */     //   4: ifeq +121 -> 125
/*     */     //   7: aload_0
/*     */     //   8: invokevirtual 32	org/apache/catalina/loader/WebappLoader:modified	()Z
/*     */     //   11: ifeq +114 -> 125
/*     */     //   14: invokestatic 33	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   17: ldc 34
/*     */     //   19: invokevirtual 35	java/lang/Class:getClassLoader	()Ljava/lang/ClassLoader;
/*     */     //   22: invokevirtual 36	java/lang/Thread:setContextClassLoader	(Ljava/lang/ClassLoader;)V
/*     */     //   25: aload_0
/*     */     //   26: getfield 4	org/apache/catalina/loader/WebappLoader:context	Lorg/apache/catalina/Context;
/*     */     //   29: ifnull +12 -> 41
/*     */     //   32: aload_0
/*     */     //   33: getfield 4	org/apache/catalina/loader/WebappLoader:context	Lorg/apache/catalina/Context;
/*     */     //   36: invokeinterface 37 1 0
/*     */     //   41: aload_0
/*     */     //   42: getfield 4	org/apache/catalina/loader/WebappLoader:context	Lorg/apache/catalina/Context;
/*     */     //   45: ifnull +80 -> 125
/*     */     //   48: aload_0
/*     */     //   49: getfield 4	org/apache/catalina/loader/WebappLoader:context	Lorg/apache/catalina/Context;
/*     */     //   52: invokeinterface 38 1 0
/*     */     //   57: ifnull +68 -> 125
/*     */     //   60: invokestatic 33	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   63: aload_0
/*     */     //   64: getfield 4	org/apache/catalina/loader/WebappLoader:context	Lorg/apache/catalina/Context;
/*     */     //   67: invokeinterface 38 1 0
/*     */     //   72: invokeinterface 39 1 0
/*     */     //   77: invokevirtual 36	java/lang/Thread:setContextClassLoader	(Ljava/lang/ClassLoader;)V
/*     */     //   80: goto +45 -> 125
/*     */     //   83: astore_1
/*     */     //   84: aload_0
/*     */     //   85: getfield 4	org/apache/catalina/loader/WebappLoader:context	Lorg/apache/catalina/Context;
/*     */     //   88: ifnull +35 -> 123
/*     */     //   91: aload_0
/*     */     //   92: getfield 4	org/apache/catalina/loader/WebappLoader:context	Lorg/apache/catalina/Context;
/*     */     //   95: invokeinterface 38 1 0
/*     */     //   100: ifnull +23 -> 123
/*     */     //   103: invokestatic 33	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   106: aload_0
/*     */     //   107: getfield 4	org/apache/catalina/loader/WebappLoader:context	Lorg/apache/catalina/Context;
/*     */     //   110: invokeinterface 38 1 0
/*     */     //   115: invokeinterface 39 1 0
/*     */     //   120: invokevirtual 36	java/lang/Thread:setContextClassLoader	(Ljava/lang/ClassLoader;)V
/*     */     //   123: aload_1
/*     */     //   124: athrow
/*     */     //   125: return
/*     */     // Line number table:
/*     */     //   Java source line #287	-> byte code offset #0
/*     */     //   Java source line #289	-> byte code offset #14
/*     */     //   Java source line #290	-> byte code offset #19
/*     */     //   Java source line #291	-> byte code offset #25
/*     */     //   Java source line #292	-> byte code offset #32
/*     */     //   Java source line #295	-> byte code offset #41
/*     */     //   Java source line #296	-> byte code offset #60
/*     */     //   Java source line #297	-> byte code offset #67
/*     */     //   Java source line #295	-> byte code offset #83
/*     */     //   Java source line #296	-> byte code offset #103
/*     */     //   Java source line #297	-> byte code offset #110
/*     */     //   Java source line #301	-> byte code offset #125
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	126	0	this	WebappLoader
/*     */     //   83	41	1	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   14	41	83	finally
/*     */   }
/*     */   
/*     */   public String[] getLoaderRepositories()
/*     */   {
/* 305 */     if (this.classLoader == null) {
/* 306 */       return new String[0];
/*     */     }
/* 308 */     URL[] urls = this.classLoader.getURLs();
/* 309 */     String[] result = new String[urls.length];
/* 310 */     for (int i = 0; i < urls.length; i++) {
/* 311 */       result[i] = urls[i].toExternalForm();
/*     */     }
/* 313 */     return result;
/*     */   }
/*     */   
/*     */   public String getLoaderRepositoriesString() {
/* 317 */     String[] repositories = getLoaderRepositories();
/* 318 */     StringBuilder sb = new StringBuilder();
/* 319 */     for (int i = 0; i < repositories.length; i++) {
/* 320 */       sb.append(repositories[i]).append(":");
/*     */     }
/* 322 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getClasspath()
/*     */   {
/* 333 */     return this.classpath;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean modified()
/*     */   {
/* 343 */     return this.classLoader != null ? this.classLoader.modified() : false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removePropertyChangeListener(PropertyChangeListener listener)
/*     */   {
/* 354 */     this.support.removePropertyChangeListener(listener);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 363 */     StringBuilder sb = new StringBuilder("WebappLoader[");
/* 364 */     if (this.context != null)
/* 365 */       sb.append(this.context.getName());
/* 366 */     sb.append("]");
/* 367 */     return sb.toString();
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
/*     */   protected void startInternal()
/*     */     throws LifecycleException
/*     */   {
/* 381 */     if (log.isDebugEnabled()) {
/* 382 */       log.debug(sm.getString("webappLoader.starting"));
/*     */     }
/* 384 */     if (this.context.getResources() == null) {
/* 385 */       log.info("No resources for " + this.context);
/* 386 */       setState(LifecycleState.STARTING);
/* 387 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 393 */       this.classLoader = createClassLoader();
/* 394 */       this.classLoader.setResources(this.context.getResources());
/* 395 */       this.classLoader.setDelegate(this.delegate);
/*     */       
/*     */ 
/* 398 */       setClassPath();
/*     */       
/* 400 */       setPermissions();
/*     */       
/* 402 */       this.classLoader.start();
/*     */       
/* 404 */       String contextName = this.context.getName();
/* 405 */       if (!contextName.startsWith("/")) {
/* 406 */         contextName = "/" + contextName;
/*     */       }
/*     */       
/*     */ 
/* 410 */       ObjectName cloname = new ObjectName(this.context.getDomain() + ":type=" + this.classLoader.getClass().getSimpleName() + ",host=" + this.context.getParent().getName() + ",context=" + contextName);
/* 411 */       Registry.getRegistry(null, null)
/* 412 */         .registerComponent(this.classLoader, cloname, null);
/*     */     }
/*     */     catch (Throwable t) {
/* 415 */       t = ExceptionUtils.unwrapInvocationTargetException(t);
/* 416 */       ExceptionUtils.handleThrowable(t);
/* 417 */       log.error("LifecycleException ", t);
/* 418 */       throw new LifecycleException("start: ", t);
/*     */     }
/*     */     
/* 421 */     setState(LifecycleState.STARTING);
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected void stopInternal()
/*     */     throws LifecycleException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: getstatic 55	org/apache/catalina/loader/WebappLoader:log	Lorg/apache/juli/logging/Log;
/*     */     //   3: invokeinterface 56 1 0
/*     */     //   8: ifeq +19 -> 27
/*     */     //   11: getstatic 55	org/apache/catalina/loader/WebappLoader:log	Lorg/apache/juli/logging/Log;
/*     */     //   14: getstatic 18	org/apache/catalina/loader/WebappLoader:sm	Lorg/apache/tomcat/util/res/StringManager;
/*     */     //   17: ldc 93
/*     */     //   19: invokevirtual 20	org/apache/tomcat/util/res/StringManager:getString	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   22: invokeinterface 58 2 0
/*     */     //   27: aload_0
/*     */     //   28: getstatic 94	org/apache/catalina/LifecycleState:STOPPING	Lorg/apache/catalina/LifecycleState;
/*     */     //   31: invokevirtual 64	org/apache/catalina/loader/WebappLoader:setState	(Lorg/apache/catalina/LifecycleState;)V
/*     */     //   34: aload_0
/*     */     //   35: getfield 4	org/apache/catalina/loader/WebappLoader:context	Lorg/apache/catalina/Context;
/*     */     //   38: invokeinterface 95 1 0
/*     */     //   43: astore_1
/*     */     //   44: aload_1
/*     */     //   45: ldc 97
/*     */     //   47: invokeinterface 98 2 0
/*     */     //   52: aload_0
/*     */     //   53: getfield 3	org/apache/catalina/loader/WebappLoader:classLoader	Lorg/apache/catalina/loader/WebappClassLoaderBase;
/*     */     //   56: ifnull +172 -> 228
/*     */     //   59: aload_0
/*     */     //   60: getfield 3	org/apache/catalina/loader/WebappLoader:classLoader	Lorg/apache/catalina/loader/WebappClassLoaderBase;
/*     */     //   63: invokevirtual 99	org/apache/catalina/loader/WebappClassLoaderBase:stop	()V
/*     */     //   66: aload_0
/*     */     //   67: getfield 3	org/apache/catalina/loader/WebappLoader:classLoader	Lorg/apache/catalina/loader/WebappClassLoaderBase;
/*     */     //   70: invokevirtual 100	org/apache/catalina/loader/WebappClassLoaderBase:destroy	()V
/*     */     //   73: goto +13 -> 86
/*     */     //   76: astore_2
/*     */     //   77: aload_0
/*     */     //   78: getfield 3	org/apache/catalina/loader/WebappLoader:classLoader	Lorg/apache/catalina/loader/WebappClassLoaderBase;
/*     */     //   81: invokevirtual 100	org/apache/catalina/loader/WebappClassLoaderBase:destroy	()V
/*     */     //   84: aload_2
/*     */     //   85: athrow
/*     */     //   86: aload_0
/*     */     //   87: getfield 4	org/apache/catalina/loader/WebappLoader:context	Lorg/apache/catalina/Context;
/*     */     //   90: invokeinterface 53 1 0
/*     */     //   95: astore_2
/*     */     //   96: aload_2
/*     */     //   97: ldc 71
/*     */     //   99: invokevirtual 72	java/lang/String:startsWith	(Ljava/lang/String;)Z
/*     */     //   102: ifne +23 -> 125
/*     */     //   105: new 44	java/lang/StringBuilder
/*     */     //   108: dup
/*     */     //   109: invokespecial 45	java/lang/StringBuilder:<init>	()V
/*     */     //   112: ldc 71
/*     */     //   114: invokevirtual 46	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   117: aload_2
/*     */     //   118: invokevirtual 46	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   121: invokevirtual 48	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   124: astore_2
/*     */     //   125: new 73	javax/management/ObjectName
/*     */     //   128: dup
/*     */     //   129: new 44	java/lang/StringBuilder
/*     */     //   132: dup
/*     */     //   133: invokespecial 45	java/lang/StringBuilder:<init>	()V
/*     */     //   136: aload_0
/*     */     //   137: getfield 4	org/apache/catalina/loader/WebappLoader:context	Lorg/apache/catalina/Context;
/*     */     //   140: invokeinterface 74 1 0
/*     */     //   145: invokevirtual 46	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   148: ldc 75
/*     */     //   150: invokevirtual 46	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   153: aload_0
/*     */     //   154: getfield 3	org/apache/catalina/loader/WebappLoader:classLoader	Lorg/apache/catalina/loader/WebappClassLoaderBase;
/*     */     //   157: invokevirtual 76	java/lang/Object:getClass	()Ljava/lang/Class;
/*     */     //   160: invokevirtual 77	java/lang/Class:getSimpleName	()Ljava/lang/String;
/*     */     //   163: invokevirtual 46	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   166: ldc 78
/*     */     //   168: invokevirtual 46	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   171: aload_0
/*     */     //   172: getfield 4	org/apache/catalina/loader/WebappLoader:context	Lorg/apache/catalina/Context;
/*     */     //   175: invokeinterface 79 1 0
/*     */     //   180: invokeinterface 80 1 0
/*     */     //   185: invokevirtual 46	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   188: ldc 81
/*     */     //   190: invokevirtual 46	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   193: aload_2
/*     */     //   194: invokevirtual 46	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   197: invokevirtual 48	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   200: invokespecial 82	javax/management/ObjectName:<init>	(Ljava/lang/String;)V
/*     */     //   203: astore_3
/*     */     //   204: aconst_null
/*     */     //   205: aconst_null
/*     */     //   206: invokestatic 83	org/apache/tomcat/util/modeler/Registry:getRegistry	(Ljava/lang/Object;Ljava/lang/Object;)Lorg/apache/tomcat/util/modeler/Registry;
/*     */     //   209: aload_3
/*     */     //   210: invokevirtual 101	org/apache/tomcat/util/modeler/Registry:unregisterComponent	(Ljavax/management/ObjectName;)V
/*     */     //   213: goto +15 -> 228
/*     */     //   216: astore_2
/*     */     //   217: getstatic 55	org/apache/catalina/loader/WebappLoader:log	Lorg/apache/juli/logging/Log;
/*     */     //   220: ldc 88
/*     */     //   222: aload_2
/*     */     //   223: invokeinterface 89 3 0
/*     */     //   228: aload_0
/*     */     //   229: aconst_null
/*     */     //   230: putfield 3	org/apache/catalina/loader/WebappLoader:classLoader	Lorg/apache/catalina/loader/WebappClassLoaderBase;
/*     */     //   233: return
/*     */     // Line number table:
/*     */     //   Java source line #435	-> byte code offset #0
/*     */     //   Java source line #436	-> byte code offset #11
/*     */     //   Java source line #438	-> byte code offset #27
/*     */     //   Java source line #441	-> byte code offset #34
/*     */     //   Java source line #442	-> byte code offset #44
/*     */     //   Java source line #445	-> byte code offset #52
/*     */     //   Java source line #447	-> byte code offset #59
/*     */     //   Java source line #449	-> byte code offset #66
/*     */     //   Java source line #450	-> byte code offset #73
/*     */     //   Java source line #449	-> byte code offset #76
/*     */     //   Java source line #454	-> byte code offset #86
/*     */     //   Java source line #455	-> byte code offset #96
/*     */     //   Java source line #456	-> byte code offset #105
/*     */     //   Java source line #458	-> byte code offset #125
/*     */     //   Java source line #459	-> byte code offset #157
/*     */     //   Java source line #460	-> byte code offset #175
/*     */     //   Java source line #461	-> byte code offset #204
/*     */     //   Java source line #464	-> byte code offset #213
/*     */     //   Java source line #462	-> byte code offset #216
/*     */     //   Java source line #463	-> byte code offset #217
/*     */     //   Java source line #468	-> byte code offset #228
/*     */     //   Java source line #469	-> byte code offset #233
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	234	0	this	WebappLoader
/*     */     //   43	2	1	servletContext	ServletContext
/*     */     //   76	9	2	localObject	Object
/*     */     //   95	99	2	contextName	String
/*     */     //   216	7	2	e	Exception
/*     */     //   203	7	3	cloname	ObjectName
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   59	66	76	finally
/*     */     //   86	213	216	java/lang/Exception
/*     */   }
/*     */   
/*     */   public void propertyChange(PropertyChangeEvent event)
/*     */   {
/* 484 */     if (!(event.getSource() instanceof Context)) {
/* 485 */       return;
/*     */     }
/*     */     
/* 488 */     if (event.getPropertyName().equals("reloadable")) {
/*     */       try
/*     */       {
/* 491 */         setReloadable(((Boolean)event.getNewValue()).booleanValue());
/*     */       } catch (NumberFormatException e) {
/* 493 */         log.error(sm.getString("webappLoader.reloadable", new Object[] {event
/* 494 */           .getNewValue().toString() }));
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
/*     */   private WebappClassLoaderBase createClassLoader()
/*     */     throws Exception
/*     */   {
/* 508 */     Class<?> clazz = Class.forName(this.loaderClass);
/* 509 */     WebappClassLoaderBase classLoader = null;
/*     */     
/* 511 */     if (this.parentClassLoader == null) {
/* 512 */       this.parentClassLoader = this.context.getParentClassLoader();
/*     */     }
/* 514 */     Class<?>[] argTypes = { ClassLoader.class };
/* 515 */     Object[] args = { this.parentClassLoader };
/* 516 */     Constructor<?> constr = clazz.getConstructor(argTypes);
/* 517 */     classLoader = (WebappClassLoaderBase)constr.newInstance(args);
/*     */     
/* 519 */     return classLoader;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setPermissions()
/*     */   {
/* 528 */     if (!Globals.IS_SECURITY_ENABLED)
/* 529 */       return;
/* 530 */     if (this.context == null) {
/* 531 */       return;
/*     */     }
/*     */     
/* 534 */     ServletContext servletContext = this.context.getServletContext();
/*     */     
/*     */ 
/*     */ 
/* 538 */     File workDir = (File)servletContext.getAttribute("javax.servlet.context.tempdir");
/* 539 */     if (workDir != null) {
/*     */       try {
/* 541 */         String workDirPath = workDir.getCanonicalPath();
/* 542 */         this.classLoader
/* 543 */           .addPermission(new FilePermission(workDirPath, "read,write"));
/* 544 */         this.classLoader
/* 545 */           .addPermission(new FilePermission(workDirPath + File.separator + "-", "read,write,delete"));
/*     */       }
/*     */       catch (IOException localIOException) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 552 */     for (URL url : this.context.getResources().getBaseUrls()) {
/* 553 */       this.classLoader.addPermission(url);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setClassPath()
/*     */   {
/* 565 */     if (this.context == null)
/* 566 */       return;
/* 567 */     ServletContext servletContext = this.context.getServletContext();
/* 568 */     if (servletContext == null) {
/* 569 */       return;
/*     */     }
/* 571 */     StringBuilder classpath = new StringBuilder();
/*     */     
/*     */ 
/* 574 */     ClassLoader loader = getClassLoader();
/*     */     
/* 576 */     if ((this.delegate) && (loader != null))
/*     */     {
/* 578 */       loader = loader.getParent();
/*     */     }
/*     */     
/* 581 */     while ((loader != null) && 
/* 582 */       (buildClassPath(classpath, loader)))
/*     */     {
/*     */ 
/* 585 */       loader = loader.getParent();
/*     */     }
/*     */     
/* 588 */     if (this.delegate)
/*     */     {
/* 590 */       loader = getClassLoader();
/* 591 */       if (loader != null) {
/* 592 */         buildClassPath(classpath, loader);
/*     */       }
/*     */     }
/*     */     
/* 596 */     this.classpath = classpath.toString();
/*     */     
/*     */ 
/* 599 */     servletContext.setAttribute("org.apache.catalina.jsp_classpath", this.classpath);
/*     */   }
/*     */   
/*     */   private boolean buildClassPath(StringBuilder classpath, ClassLoader loader)
/*     */   {
/* 604 */     if ((loader instanceof URLClassLoader)) {
/* 605 */       URL[] repositories = ((URLClassLoader)loader).getURLs();
/* 606 */       for (int i = 0; i < repositories.length; i++) {
/* 607 */         String repository = repositories[i].toString();
/* 608 */         if (repository.startsWith("file://")) {
/* 609 */           repository = utf8Decode(repository.substring(7));
/* 610 */         } else { if (!repository.startsWith("file:")) continue;
/* 611 */           repository = utf8Decode(repository.substring(5));
/*     */         }
/*     */         
/* 614 */         if (repository != null)
/*     */         {
/* 616 */           if (classpath.length() > 0)
/* 617 */             classpath.append(File.pathSeparator);
/* 618 */           classpath.append(repository);
/*     */         }
/* 620 */       } } else { if (loader == ClassLoader.getSystemClassLoader())
/*     */       {
/*     */ 
/* 623 */         String cp = System.getProperty("java.class.path");
/* 624 */         if ((cp != null) && (cp.length() > 0)) {
/* 625 */           if (classpath.length() > 0) {
/* 626 */             classpath.append(File.pathSeparator);
/*     */           }
/* 628 */           classpath.append(cp);
/*     */         }
/* 630 */         return false;
/*     */       }
/* 632 */       log.info("Unknown loader " + loader + " " + loader.getClass());
/* 633 */       return false;
/*     */     }
/* 635 */     return true;
/*     */   }
/*     */   
/*     */   private String utf8Decode(String input) {
/* 639 */     String result = null;
/*     */     try {
/* 641 */       result = URLDecoder.decode(input, "UTF-8");
/*     */     }
/*     */     catch (UnsupportedEncodingException localUnsupportedEncodingException) {}
/*     */     
/* 645 */     return result;
/*     */   }
/*     */   
/*     */ 
/* 649 */   private static final Log log = LogFactory.getLog(WebappLoader.class);
/*     */   
/*     */ 
/*     */   protected String getDomainInternal()
/*     */   {
/* 654 */     return this.context.getDomain();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getObjectNameKeyProperties()
/*     */   {
/* 661 */     StringBuilder name = new StringBuilder("type=Loader");
/*     */     
/* 663 */     name.append(",host=");
/* 664 */     name.append(this.context.getParent().getName());
/*     */     
/* 666 */     name.append(",context=");
/*     */     
/* 668 */     String contextName = this.context.getName();
/* 669 */     if (!contextName.startsWith("/")) {
/* 670 */       name.append("/");
/*     */     }
/* 672 */     name.append(contextName);
/*     */     
/* 674 */     return name.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\loader\WebappLoader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */