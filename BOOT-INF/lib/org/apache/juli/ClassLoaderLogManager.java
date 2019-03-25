/*     */ package org.apache.juli;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FilePermission;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.security.AccessControlException;
/*     */ import java.security.AccessController;
/*     */ import java.security.Permission;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.logging.Handler;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.LogManager;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassLoaderLogManager
/*     */   extends LogManager
/*     */ {
/*     */   private static final boolean isJava9;
/*  56 */   public static final String DEBUG_PROPERTY = ClassLoaderLogManager.class
/*  57 */     .getName() + ".debug";
/*     */   
/*     */   static {
/*  60 */     Class<?> c = null;
/*     */     try {
/*  62 */       c = Class.forName("java.lang.Runtime$Version");
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {}
/*     */     
/*  66 */     isJava9 = c != null;
/*     */   }
/*     */   
/*     */   private final class Cleaner extends Thread {
/*     */     private Cleaner() {}
/*     */     
/*     */     public void run() {
/*  73 */       if (ClassLoaderLogManager.this.useShutdownHook) {
/*  74 */         ClassLoaderLogManager.this.shutdown();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ClassLoaderLogManager()
/*     */   {
/*     */     try
/*     */     {
/*  86 */       Runtime.getRuntime().addShutdownHook(new Cleaner(null));
/*     */     }
/*     */     catch (IllegalStateException localIllegalStateException) {}
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
/* 101 */   protected final Map<ClassLoader, ClassLoaderLogInfo> classLoaderLoggers = new WeakHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 109 */   protected final ThreadLocal<String> prefix = new ThreadLocal();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 118 */   protected volatile boolean useShutdownHook = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUseShutdownHook()
/*     */   {
/* 125 */     return this.useShutdownHook;
/*     */   }
/*     */   
/*     */   public void setUseShutdownHook(boolean useShutdownHook)
/*     */   {
/* 130 */     this.useShutdownHook = useShutdownHook;
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
/*     */   public synchronized boolean addLogger(final Logger logger)
/*     */   {
/* 145 */     String loggerName = logger.getName();
/*     */     
/*     */ 
/* 148 */     ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
/* 149 */     ClassLoaderLogInfo info = getClassLoaderInfo(classLoader);
/* 150 */     if (info.loggers.containsKey(loggerName)) {
/* 151 */       return false;
/*     */     }
/* 153 */     info.loggers.put(loggerName, logger);
/*     */     
/*     */ 
/* 156 */     final String levelString = getProperty(loggerName + ".level");
/* 157 */     if (levelString != null) {
/*     */       try {
/* 159 */         AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Void run() {
/* 162 */             logger.setLevel(Level.parse(levelString.trim()));
/* 163 */             return null;
/*     */           }
/*     */         });
/*     */       }
/*     */       catch (IllegalArgumentException localIllegalArgumentException) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 173 */     int dotIndex = loggerName.lastIndexOf('.');
/* 174 */     if (dotIndex >= 0) {
/* 175 */       String parentName = loggerName.substring(0, dotIndex);
/* 176 */       Logger.getLogger(parentName);
/*     */     }
/*     */     
/*     */ 
/* 180 */     LogNode node = info.rootNode.findNode(loggerName);
/* 181 */     node.logger = logger;
/*     */     
/*     */ 
/* 184 */     Logger parentLogger = node.findParentLogger();
/* 185 */     if (parentLogger != null) {
/* 186 */       doSetParentLogger(logger, parentLogger);
/*     */     }
/*     */     
/*     */ 
/* 190 */     node.setParentLogger(logger);
/*     */     
/*     */ 
/*     */ 
/* 194 */     String handlers = getProperty(loggerName + ".handlers");
/* 195 */     if (handlers != null) {
/* 196 */       logger.setUseParentHandlers(false);
/* 197 */       StringTokenizer tok = new StringTokenizer(handlers, ",");
/* 198 */       while (tok.hasMoreTokens()) {
/* 199 */         String handlerName = tok.nextToken().trim();
/* 200 */         Handler handler = null;
/* 201 */         ClassLoader current = classLoader;
/* 202 */         while (current != null) {
/* 203 */           info = (ClassLoaderLogInfo)this.classLoaderLoggers.get(current);
/* 204 */           if (info != null) {
/* 205 */             handler = (Handler)info.handlers.get(handlerName);
/* 206 */             if (handler != null) {
/*     */               break;
/*     */             }
/*     */           }
/* 210 */           current = current.getParent();
/*     */         }
/* 212 */         if (handler != null) {
/* 213 */           logger.addHandler(handler);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 221 */     String useParentHandlersString = getProperty(loggerName + ".useParentHandlers");
/* 222 */     if (Boolean.parseBoolean(useParentHandlersString)) {
/* 223 */       logger.setUseParentHandlers(true);
/*     */     }
/*     */     
/* 226 */     return true;
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
/*     */   public synchronized Logger getLogger(String name)
/*     */   {
/* 242 */     ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
/* 243 */     return (Logger)getClassLoaderInfo(classLoader).loggers.get(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized Enumeration<String> getLoggerNames()
/*     */   {
/* 254 */     ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
/* 255 */     return Collections.enumeration(getClassLoaderInfo(classLoader).loggers.keySet());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getProperty(String name)
/*     */   {
/* 267 */     String prefix = (String)this.prefix.get();
/* 268 */     String result = null;
/*     */     
/*     */ 
/* 271 */     if (prefix != null) {
/* 272 */       result = findProperty(prefix + name);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 277 */     if (result == null) {
/* 278 */       result = findProperty(name);
/*     */     }
/*     */     
/*     */ 
/* 282 */     if (result != null) {
/* 283 */       result = replace(result);
/*     */     }
/* 285 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   private synchronized String findProperty(String name)
/*     */   {
/* 291 */     ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
/* 292 */     ClassLoaderLogInfo info = getClassLoaderInfo(classLoader);
/* 293 */     String result = info.props.getProperty(name);
/*     */     
/*     */ 
/*     */ 
/* 297 */     if ((result == null) && (info.props.isEmpty())) {
/* 298 */       ClassLoader current = classLoader.getParent();
/* 299 */       while (current != null) {
/* 300 */         info = (ClassLoaderLogInfo)this.classLoaderLoggers.get(current);
/* 301 */         if (info != null) {
/* 302 */           result = info.props.getProperty(name);
/* 303 */           if ((result != null) || (!info.props.isEmpty())) {
/*     */             break;
/*     */           }
/*     */         }
/* 307 */         current = current.getParent();
/*     */       }
/* 309 */       if (result == null) {
/* 310 */         result = super.getProperty(name);
/*     */       }
/*     */     }
/* 313 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public void readConfiguration()
/*     */     throws IOException, SecurityException
/*     */   {
/* 320 */     checkAccess();
/*     */     
/* 322 */     readConfiguration(Thread.currentThread().getContextClassLoader());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void readConfiguration(InputStream is)
/*     */     throws IOException, SecurityException
/*     */   {
/* 330 */     checkAccess();
/* 331 */     reset();
/*     */     
/* 333 */     readConfiguration(is, Thread.currentThread().getContextClassLoader());
/*     */   }
/*     */   
/*     */   public void reset()
/*     */     throws SecurityException
/*     */   {
/* 339 */     Thread thread = Thread.currentThread();
/* 340 */     if (thread.getClass().getName().startsWith("java.util.logging.LogManager$"))
/*     */     {
/*     */ 
/*     */ 
/* 344 */       return;
/*     */     }
/* 346 */     ClassLoader classLoader = thread.getContextClassLoader();
/* 347 */     ClassLoaderLogInfo clLogInfo = getClassLoaderInfo(classLoader);
/* 348 */     resetLoggers(clLogInfo);
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
/*     */   public synchronized void shutdown()
/*     */   {
/* 362 */     for (ClassLoaderLogInfo clLogInfo : this.classLoaderLoggers.values()) {
/* 363 */       resetLoggers(clLogInfo);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void resetLoggers(ClassLoaderLogInfo clLogInfo)
/*     */   {
/* 375 */     synchronized (clLogInfo) {
/* 376 */       for (Logger logger : clLogInfo.loggers.values()) {
/* 377 */         Handler[] handlers = logger.getHandlers();
/* 378 */         for (Handler handler : handlers) {
/* 379 */           logger.removeHandler(handler);
/*     */         }
/*     */       }
/* 382 */       for (Handler handler : clLogInfo.handlers.values()) {
/*     */         try {
/* 384 */           handler.close();
/*     */         }
/*     */         catch (Exception localException) {}
/*     */       }
/*     */       
/* 389 */       clLogInfo.handlers.clear();
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
/*     */   protected synchronized ClassLoaderLogInfo getClassLoaderInfo(ClassLoader classLoader)
/*     */   {
/* 406 */     if (classLoader == null) {
/* 407 */       classLoader = ClassLoader.getSystemClassLoader();
/*     */     }
/* 409 */     ClassLoaderLogInfo info = (ClassLoaderLogInfo)this.classLoaderLoggers.get(classLoader);
/* 410 */     if (info == null) {
/* 411 */       final ClassLoader classLoaderParam = classLoader;
/* 412 */       AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Void run() {
/*     */           try {
/* 416 */             ClassLoaderLogManager.this.readConfiguration(classLoaderParam);
/*     */           }
/*     */           catch (IOException localIOException) {}
/*     */           
/* 420 */           return null;
/*     */         }
/* 422 */       });
/* 423 */       info = (ClassLoaderLogInfo)this.classLoaderLoggers.get(classLoader);
/*     */     }
/* 425 */     return info;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected synchronized void readConfiguration(ClassLoader classLoader)
/*     */     throws IOException
/*     */   {
/* 438 */     InputStream is = null;
/*     */     
/*     */     try
/*     */     {
/* 442 */       if ((classLoader instanceof WebappProperties)) {
/* 443 */         if (((WebappProperties)classLoader).hasLoggingConfig()) {
/* 444 */           is = classLoader.getResourceAsStream("logging.properties");
/*     */         }
/* 446 */       } else if ((classLoader instanceof URLClassLoader)) {
/* 447 */         URL logConfig = ((URLClassLoader)classLoader).findResource("logging.properties");
/*     */         
/* 449 */         if (null != logConfig) {
/* 450 */           if (Boolean.getBoolean(DEBUG_PROPERTY)) {
/* 451 */             System.err.println(getClass().getName() + ".readConfiguration(): Found logging.properties at " + logConfig);
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 456 */           is = classLoader.getResourceAsStream("logging.properties");
/*     */         }
/* 458 */         else if (Boolean.getBoolean(DEBUG_PROPERTY)) {
/* 459 */           System.err.println(getClass().getName() + ".readConfiguration(): Found no logging.properties");
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */     }
/*     */     catch (AccessControlException ace)
/*     */     {
/* 467 */       ClassLoaderLogInfo info = (ClassLoaderLogInfo)this.classLoaderLoggers.get(ClassLoader.getSystemClassLoader());
/* 468 */       if (info != null) {
/* 469 */         Logger log = (Logger)info.loggers.get("");
/* 470 */         if (log != null) {
/* 471 */           Permission perm = ace.getPermission();
/* 472 */           if (((perm instanceof FilePermission)) && (perm.getActions().equals("read"))) {
/* 473 */             log.warning("Reading " + perm.getName() + " is not permitted. See \"per context logging\" in the default catalina.policy file.");
/*     */           }
/*     */           else {
/* 476 */             log.warning("Reading logging.properties is not permitted in some context. See \"per context logging\" in the default catalina.policy file.");
/* 477 */             log.warning("Original error was: " + ace.getMessage());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 482 */     if ((is == null) && (classLoader == ClassLoader.getSystemClassLoader())) {
/* 483 */       String configFileStr = System.getProperty("java.util.logging.config.file");
/* 484 */       if (configFileStr != null) {
/*     */         try {
/* 486 */           is = new FileInputStream(replace(configFileStr));
/*     */         } catch (IOException e) {
/* 488 */           System.err.println("Configuration error");
/* 489 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */       
/* 493 */       if (is == null) {
/* 494 */         File defaultFile = new File(new File(System.getProperty("java.home"), isJava9 ? "conf" : "lib"), "logging.properties");
/*     */         
/*     */         try
/*     */         {
/* 498 */           is = new FileInputStream(defaultFile);
/*     */         } catch (IOException e) {
/* 500 */           System.err.println("Configuration error");
/* 501 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 506 */     Logger localRootLogger = new RootLogger();
/* 507 */     if (is == null)
/*     */     {
/* 509 */       ClassLoader current = classLoader.getParent();
/* 510 */       ClassLoaderLogInfo info = null;
/* 511 */       while ((current != null) && (info == null)) {
/* 512 */         info = getClassLoaderInfo(current);
/* 513 */         current = current.getParent();
/*     */       }
/* 515 */       if (info != null) {
/* 516 */         localRootLogger.setParent(info.rootNode.logger);
/*     */       }
/*     */     }
/* 519 */     ClassLoaderLogInfo info = new ClassLoaderLogInfo(new LogNode(null, localRootLogger));
/*     */     
/* 521 */     this.classLoaderLoggers.put(classLoader, info);
/*     */     
/* 523 */     if (is != null) {
/* 524 */       readConfiguration(is, classLoader);
/*     */     }
/* 526 */     addLogger(localRootLogger);
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
/*     */   protected synchronized void readConfiguration(InputStream is, ClassLoader classLoader)
/*     */     throws IOException
/*     */   {
/* 541 */     ClassLoaderLogInfo info = (ClassLoaderLogInfo)this.classLoaderLoggers.get(classLoader);
/*     */     try
/*     */     {
/* 544 */       info.props.load(is);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */       try
/*     */       {
/* 551 */         is.close();
/*     */       }
/*     */       catch (IOException localIOException1) {}
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 558 */       rootHandlers = info.props.getProperty(".handlers");
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 547 */       System.err.println("Configuration error");
/* 548 */       e.printStackTrace();
/*     */     } finally {
/*     */       try {
/* 551 */         is.close();
/*     */       }
/*     */       catch (IOException localIOException3) {}
/*     */     }
/*     */     
/*     */ 
/*     */     String rootHandlers;
/*     */     
/* 559 */     String handlers = info.props.getProperty("handlers");
/* 560 */     Logger localRootLogger = info.rootNode.logger;
/* 561 */     if (handlers != null) {
/* 562 */       StringTokenizer tok = new StringTokenizer(handlers, ",");
/* 563 */       while (tok.hasMoreTokens()) {
/* 564 */         String handlerName = tok.nextToken().trim();
/* 565 */         String handlerClassName = handlerName;
/* 566 */         String prefix = "";
/* 567 */         if (handlerClassName.length() > 0)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 572 */           if (Character.isDigit(handlerClassName.charAt(0))) {
/* 573 */             int pos = handlerClassName.indexOf('.');
/* 574 */             if (pos >= 0) {
/* 575 */               prefix = handlerClassName.substring(0, pos + 1);
/* 576 */               handlerClassName = handlerClassName.substring(pos + 1);
/*     */             }
/*     */           }
/*     */           try {
/* 580 */             this.prefix.set(prefix);
/*     */             
/* 582 */             Handler handler = (Handler)classLoader.loadClass(handlerClassName).getConstructor(new Class[0]).newInstance(new Object[0]);
/*     */             
/*     */ 
/*     */ 
/* 586 */             this.prefix.set(null);
/* 587 */             info.handlers.put(handlerName, handler);
/* 588 */             if (rootHandlers == null) {
/* 589 */               localRootLogger.addHandler(handler);
/*     */             }
/*     */           }
/*     */           catch (Exception e) {
/* 593 */             System.err.println("Handler error");
/* 594 */             e.printStackTrace();
/*     */           }
/*     */         }
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
/*     */   protected static void doSetParentLogger(Logger logger, final Logger parent)
/*     */   {
/* 611 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Void run() {
/* 614 */         this.val$logger.setParent(parent);
/* 615 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String replace(String str)
/*     */   {
/* 628 */     String result = str;
/* 629 */     int pos_start = str.indexOf("${");
/* 630 */     if (pos_start >= 0) {
/* 631 */       StringBuilder builder = new StringBuilder();
/* 632 */       int pos_end = -1;
/* 633 */       while (pos_start >= 0) {
/* 634 */         builder.append(str, pos_end + 1, pos_start);
/* 635 */         pos_end = str.indexOf('}', pos_start + 2);
/* 636 */         if (pos_end < 0) {
/* 637 */           pos_end = pos_start - 1;
/* 638 */           break;
/*     */         }
/* 640 */         String propName = str.substring(pos_start + 2, pos_end);
/*     */         
/* 642 */         String replacement = replaceWebApplicationProperties(propName);
/* 643 */         if (replacement == null) {
/* 644 */           replacement = propName.length() > 0 ? System.getProperty(propName) : null;
/*     */         }
/* 646 */         if (replacement != null) {
/* 647 */           builder.append(replacement);
/*     */         } else {
/* 649 */           builder.append(str, pos_start, pos_end + 1);
/*     */         }
/* 651 */         pos_start = str.indexOf("${", pos_end + 1);
/*     */       }
/* 653 */       builder.append(str, pos_end + 1, str.length());
/* 654 */       result = builder.toString();
/*     */     }
/* 656 */     return result;
/*     */   }
/*     */   
/*     */   private String replaceWebApplicationProperties(String propName)
/*     */   {
/* 661 */     ClassLoader cl = Thread.currentThread().getContextClassLoader();
/* 662 */     if ((cl instanceof WebappProperties)) {
/* 663 */       WebappProperties wProps = (WebappProperties)cl;
/* 664 */       if ("classloader.webappName".equals(propName))
/* 665 */         return wProps.getWebappName();
/* 666 */       if ("classloader.hostName".equals(propName))
/* 667 */         return wProps.getHostName();
/* 668 */       if ("classloader.serviceName".equals(propName)) {
/* 669 */         return wProps.getServiceName();
/*     */       }
/* 671 */       return null;
/*     */     }
/*     */     
/* 674 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static final class LogNode
/*     */   {
/*     */     Logger logger;
/*     */     
/*     */ 
/* 685 */     final Map<String, LogNode> children = new HashMap();
/*     */     final LogNode parent;
/*     */     
/*     */     LogNode(LogNode parent, Logger logger)
/*     */     {
/* 690 */       this.parent = parent;
/* 691 */       this.logger = logger;
/*     */     }
/*     */     
/*     */     LogNode(LogNode parent) {
/* 695 */       this(parent, null);
/*     */     }
/*     */     
/*     */     LogNode findNode(String name) {
/* 699 */       LogNode currentNode = this;
/* 700 */       if (this.logger.getName().equals(name)) {
/* 701 */         return this;
/*     */       }
/* 703 */       while (name != null) {
/* 704 */         int dotIndex = name.indexOf('.');
/*     */         String nextName;
/* 706 */         if (dotIndex < 0) {
/* 707 */           String nextName = name;
/* 708 */           name = null;
/*     */         } else {
/* 710 */           nextName = name.substring(0, dotIndex);
/* 711 */           name = name.substring(dotIndex + 1);
/*     */         }
/* 713 */         LogNode childNode = (LogNode)currentNode.children.get(nextName);
/* 714 */         if (childNode == null) {
/* 715 */           childNode = new LogNode(currentNode);
/* 716 */           currentNode.children.put(nextName, childNode);
/*     */         }
/* 718 */         currentNode = childNode;
/*     */       }
/* 720 */       return currentNode;
/*     */     }
/*     */     
/*     */     Logger findParentLogger() {
/* 724 */       Logger logger = null;
/* 725 */       LogNode node = this.parent;
/* 726 */       while ((node != null) && (logger == null)) {
/* 727 */         logger = node.logger;
/* 728 */         node = node.parent;
/*     */       }
/* 730 */       return logger;
/*     */     }
/*     */     
/*     */     void setParentLogger(Logger parent)
/*     */     {
/* 735 */       for (Iterator<LogNode> iter = this.children.values().iterator(); iter.hasNext();) {
/* 736 */         LogNode childNode = (LogNode)iter.next();
/* 737 */         if (childNode.logger == null) {
/* 738 */           childNode.setParentLogger(parent);
/*     */         } else {
/* 740 */           ClassLoaderLogManager.doSetParentLogger(childNode.logger, parent);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected static final class ClassLoaderLogInfo
/*     */   {
/*     */     final ClassLoaderLogManager.LogNode rootNode;
/*     */     
/*     */ 
/* 753 */     final Map<String, Logger> loggers = new ConcurrentHashMap();
/* 754 */     final Map<String, Handler> handlers = new HashMap();
/* 755 */     final Properties props = new Properties();
/*     */     
/*     */     ClassLoaderLogInfo(ClassLoaderLogManager.LogNode rootNode) {
/* 758 */       this.rootNode = rootNode;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static class RootLogger
/*     */     extends Logger
/*     */   {
/*     */     public RootLogger()
/*     */     {
/* 773 */       super(null);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\juli\ClassLoaderLogManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */