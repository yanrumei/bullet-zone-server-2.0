/*     */ package org.apache.catalina.startup;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.catalina.security.SecurityClassLoad;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
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
/*     */ public final class Bootstrap
/*     */ {
/*  52 */   private static final Log log = LogFactory.getLog(Bootstrap.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  57 */   private static Bootstrap daemon = null;
/*     */   
/*     */   private static final File catalinaBaseFile;
/*     */   
/*     */   private static final File catalinaHomeFile;
/*  62 */   private static final Pattern PATH_PATTERN = Pattern.compile("(\".*?\")|(([^,])*)");
/*     */   
/*     */   static
/*     */   {
/*  66 */     String userDir = System.getProperty("user.dir");
/*     */     
/*     */ 
/*  69 */     String home = System.getProperty("catalina.home");
/*  70 */     File homeFile = null;
/*     */     
/*  72 */     if (home != null) {
/*  73 */       File f = new File(home);
/*     */       try {
/*  75 */         homeFile = f.getCanonicalFile();
/*     */       } catch (IOException ioe) {
/*  77 */         homeFile = f.getAbsoluteFile();
/*     */       }
/*     */     }
/*     */     
/*  81 */     if (homeFile == null)
/*     */     {
/*     */ 
/*  84 */       File bootstrapJar = new File(userDir, "bootstrap.jar");
/*     */       
/*  86 */       if (bootstrapJar.exists()) {
/*  87 */         File f = new File(userDir, "..");
/*     */         try {
/*  89 */           homeFile = f.getCanonicalFile();
/*     */         } catch (IOException ioe) {
/*  91 */           homeFile = f.getAbsoluteFile();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  96 */     if (homeFile == null)
/*     */     {
/*  98 */       File f = new File(userDir);
/*     */       try {
/* 100 */         homeFile = f.getCanonicalFile();
/*     */       } catch (IOException ioe) {
/* 102 */         homeFile = f.getAbsoluteFile();
/*     */       }
/*     */     }
/*     */     
/* 106 */     catalinaHomeFile = homeFile;
/* 107 */     System.setProperty("catalina.home", catalinaHomeFile
/* 108 */       .getPath());
/*     */     
/*     */ 
/* 111 */     String base = System.getProperty("catalina.base");
/* 112 */     if (base == null) {
/* 113 */       catalinaBaseFile = catalinaHomeFile;
/*     */     } else {
/* 115 */       File baseFile = new File(base);
/*     */       try {
/* 117 */         baseFile = baseFile.getCanonicalFile();
/*     */       } catch (IOException ioe) {
/* 119 */         baseFile = baseFile.getAbsoluteFile();
/*     */       }
/* 121 */       catalinaBaseFile = baseFile;
/*     */     }
/* 123 */     System.setProperty("catalina.base", catalinaBaseFile
/* 124 */       .getPath());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 133 */   private Object catalinaDaemon = null;
/*     */   
/*     */ 
/* 136 */   ClassLoader commonLoader = null;
/* 137 */   ClassLoader catalinaLoader = null;
/* 138 */   ClassLoader sharedLoader = null;
/*     */   
/*     */ 
/*     */ 
/*     */   private void initClassLoaders()
/*     */   {
/*     */     try
/*     */     {
/* 146 */       this.commonLoader = createClassLoader("common", null);
/* 147 */       if (this.commonLoader == null)
/*     */       {
/* 149 */         this.commonLoader = getClass().getClassLoader();
/*     */       }
/* 151 */       this.catalinaLoader = createClassLoader("server", this.commonLoader);
/* 152 */       this.sharedLoader = createClassLoader("shared", this.commonLoader);
/*     */     } catch (Throwable t) {
/* 154 */       handleThrowable(t);
/* 155 */       log.error("Class loader creation threw exception", t);
/* 156 */       System.exit(1);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private ClassLoader createClassLoader(String name, ClassLoader parent)
/*     */     throws Exception
/*     */   {
/* 164 */     String value = CatalinaProperties.getProperty(name + ".loader");
/* 165 */     if ((value == null) || (value.equals(""))) {
/* 166 */       return parent;
/*     */     }
/* 168 */     value = replace(value);
/*     */     
/* 170 */     List<ClassLoaderFactory.Repository> repositories = new ArrayList();
/*     */     
/* 172 */     String[] repositoryPaths = getPaths(value);
/*     */     
/* 174 */     for (String repository : repositoryPaths)
/*     */     {
/*     */       try
/*     */       {
/* 178 */         URL url = new URL(repository);
/* 179 */         repositories.add(new ClassLoaderFactory.Repository(repository, ClassLoaderFactory.RepositoryType.URL));
/*     */ 
/*     */ 
/*     */       }
/*     */       catch (MalformedURLException localMalformedURLException)
/*     */       {
/*     */ 
/*     */ 
/* 187 */         if (repository.endsWith("*.jar"))
/*     */         {
/* 189 */           repository = repository.substring(0, repository.length() - "*.jar".length());
/* 190 */           repositories.add(new ClassLoaderFactory.Repository(repository, ClassLoaderFactory.RepositoryType.GLOB));
/*     */         }
/* 192 */         else if (repository.endsWith(".jar")) {
/* 193 */           repositories.add(new ClassLoaderFactory.Repository(repository, ClassLoaderFactory.RepositoryType.JAR));
/*     */         }
/*     */         else {
/* 196 */           repositories.add(new ClassLoaderFactory.Repository(repository, ClassLoaderFactory.RepositoryType.DIR));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 201 */     return ClassLoaderFactory.createClassLoader(repositories, parent);
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
/*     */   protected String replace(String str)
/*     */   {
/* 214 */     String result = str;
/* 215 */     int pos_start = str.indexOf("${");
/* 216 */     if (pos_start >= 0) {
/* 217 */       StringBuilder builder = new StringBuilder();
/* 218 */       int pos_end = -1;
/* 219 */       while (pos_start >= 0) {
/* 220 */         builder.append(str, pos_end + 1, pos_start);
/* 221 */         pos_end = str.indexOf('}', pos_start + 2);
/* 222 */         if (pos_end < 0) {
/* 223 */           pos_end = pos_start - 1;
/* 224 */           break;
/*     */         }
/* 226 */         String propName = str.substring(pos_start + 2, pos_end);
/*     */         String replacement;
/* 228 */         String replacement; if (propName.length() == 0) {
/* 229 */           replacement = null; } else { String replacement;
/* 230 */           if ("catalina.home".equals(propName)) {
/* 231 */             replacement = getCatalinaHome(); } else { String replacement;
/* 232 */             if ("catalina.base".equals(propName)) {
/* 233 */               replacement = getCatalinaBase();
/*     */             } else
/* 235 */               replacement = System.getProperty(propName);
/*     */           } }
/* 237 */         if (replacement != null) {
/* 238 */           builder.append(replacement);
/*     */         } else {
/* 240 */           builder.append(str, pos_start, pos_end + 1);
/*     */         }
/* 242 */         pos_start = str.indexOf("${", pos_end + 1);
/*     */       }
/* 244 */       builder.append(str, pos_end + 1, str.length());
/* 245 */       result = builder.toString();
/*     */     }
/* 247 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void init()
/*     */     throws Exception
/*     */   {
/* 257 */     initClassLoaders();
/*     */     
/* 259 */     Thread.currentThread().setContextClassLoader(this.catalinaLoader);
/*     */     
/* 261 */     SecurityClassLoad.securityClassLoad(this.catalinaLoader);
/*     */     
/*     */ 
/* 264 */     if (log.isDebugEnabled())
/* 265 */       log.debug("Loading startup class");
/* 266 */     Class<?> startupClass = this.catalinaLoader.loadClass("org.apache.catalina.startup.Catalina");
/* 267 */     Object startupInstance = startupClass.getConstructor(new Class[0]).newInstance(new Object[0]);
/*     */     
/*     */ 
/* 270 */     if (log.isDebugEnabled())
/* 271 */       log.debug("Setting startup class properties");
/* 272 */     String methodName = "setParentClassLoader";
/* 273 */     Class<?>[] paramTypes = new Class[1];
/* 274 */     paramTypes[0] = Class.forName("java.lang.ClassLoader");
/* 275 */     Object[] paramValues = new Object[1];
/* 276 */     paramValues[0] = this.sharedLoader;
/*     */     
/* 278 */     Method method = startupInstance.getClass().getMethod(methodName, paramTypes);
/* 279 */     method.invoke(startupInstance, paramValues);
/*     */     
/* 281 */     this.catalinaDaemon = startupInstance;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void load(String[] arguments)
/*     */     throws Exception
/*     */   {
/* 293 */     String methodName = "load";
/*     */     Object[] param;
/*     */     Class<?>[] paramTypes;
/* 296 */     Object[] param; if ((arguments == null) || (arguments.length == 0)) {
/* 297 */       Class<?>[] paramTypes = null;
/* 298 */       param = null;
/*     */     } else {
/* 300 */       paramTypes = new Class[1];
/* 301 */       paramTypes[0] = arguments.getClass();
/* 302 */       param = new Object[1];
/* 303 */       param[0] = arguments;
/*     */     }
/*     */     
/* 306 */     Method method = this.catalinaDaemon.getClass().getMethod(methodName, paramTypes);
/* 307 */     if (log.isDebugEnabled())
/* 308 */       log.debug("Calling startup class " + method);
/* 309 */     method.invoke(this.catalinaDaemon, param);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Object getServer()
/*     */     throws Exception
/*     */   {
/* 319 */     String methodName = "getServer";
/*     */     
/* 321 */     Method method = this.catalinaDaemon.getClass().getMethod(methodName, new Class[0]);
/* 322 */     return method.invoke(this.catalinaDaemon, new Object[0]);
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
/*     */   public void init(String[] arguments)
/*     */     throws Exception
/*     */   {
/* 338 */     init();
/* 339 */     load(arguments);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void start()
/*     */     throws Exception
/*     */   {
/* 350 */     if (this.catalinaDaemon == null) { init();
/*     */     }
/* 352 */     Method method = this.catalinaDaemon.getClass().getMethod("start", (Class[])null);
/* 353 */     method.invoke(this.catalinaDaemon, (Object[])null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void stop()
/*     */     throws Exception
/*     */   {
/* 365 */     Method method = this.catalinaDaemon.getClass().getMethod("stop", (Class[])null);
/* 366 */     method.invoke(this.catalinaDaemon, (Object[])null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void stopServer()
/*     */     throws Exception
/*     */   {
/* 379 */     Method method = this.catalinaDaemon.getClass().getMethod("stopServer", (Class[])null);
/* 380 */     method.invoke(this.catalinaDaemon, (Object[])null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void stopServer(String[] arguments)
/*     */     throws Exception
/*     */   {
/*     */     Object[] param;
/*     */     
/*     */ 
/*     */     Class<?>[] paramTypes;
/*     */     
/*     */     Object[] param;
/*     */     
/* 395 */     if ((arguments == null) || (arguments.length == 0)) {
/* 396 */       Class<?>[] paramTypes = null;
/* 397 */       param = null;
/*     */     } else {
/* 399 */       paramTypes = new Class[1];
/* 400 */       paramTypes[0] = arguments.getClass();
/* 401 */       param = new Object[1];
/* 402 */       param[0] = arguments;
/*     */     }
/*     */     
/* 405 */     Method method = this.catalinaDaemon.getClass().getMethod("stopServer", paramTypes);
/* 406 */     method.invoke(this.catalinaDaemon, param);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAwait(boolean await)
/*     */     throws Exception
/*     */   {
/* 419 */     Class<?>[] paramTypes = new Class[1];
/* 420 */     paramTypes[0] = Boolean.TYPE;
/* 421 */     Object[] paramValues = new Object[1];
/* 422 */     paramValues[0] = Boolean.valueOf(await);
/*     */     
/* 424 */     Method method = this.catalinaDaemon.getClass().getMethod("setAwait", paramTypes);
/* 425 */     method.invoke(this.catalinaDaemon, paramValues);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean getAwait()
/*     */     throws Exception
/*     */   {
/* 432 */     Class<?>[] paramTypes = new Class[0];
/* 433 */     Object[] paramValues = new Object[0];
/*     */     
/* 435 */     Method method = this.catalinaDaemon.getClass().getMethod("getAwait", paramTypes);
/* 436 */     Boolean b = (Boolean)method.invoke(this.catalinaDaemon, paramValues);
/* 437 */     return b.booleanValue();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 459 */     if (daemon == null)
/*     */     {
/* 461 */       Bootstrap bootstrap = new Bootstrap();
/*     */       try {
/* 463 */         bootstrap.init();
/*     */       } catch (Throwable t) {
/* 465 */         handleThrowable(t);
/* 466 */         t.printStackTrace();
/* 467 */         return;
/*     */       }
/* 469 */       daemon = bootstrap;
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 474 */       Thread.currentThread().setContextClassLoader(daemon.catalinaLoader);
/*     */     }
/*     */     try
/*     */     {
/* 478 */       String command = "start";
/* 479 */       if (args.length > 0) {
/* 480 */         command = args[(args.length - 1)];
/*     */       }
/*     */       
/* 483 */       if (command.equals("startd")) {
/* 484 */         args[(args.length - 1)] = "start";
/* 485 */         daemon.load(args);
/* 486 */         daemon.start();
/* 487 */       } else if (command.equals("stopd")) {
/* 488 */         args[(args.length - 1)] = "stop";
/* 489 */         daemon.stop();
/* 490 */       } else if (command.equals("start")) {
/* 491 */         daemon.setAwait(true);
/* 492 */         daemon.load(args);
/* 493 */         daemon.start();
/* 494 */       } else if (command.equals("stop")) {
/* 495 */         daemon.stopServer(args);
/* 496 */       } else if (command.equals("configtest")) {
/* 497 */         daemon.load(args);
/* 498 */         if (null == daemon.getServer()) {
/* 499 */           System.exit(1);
/*     */         }
/* 501 */         System.exit(0);
/*     */       } else {
/* 503 */         log.warn("Bootstrap: command \"" + command + "\" does not exist.");
/*     */       }
/*     */     }
/*     */     catch (Throwable t) {
/* 507 */       if (((t instanceof InvocationTargetException)) && 
/* 508 */         (t.getCause() != null)) {
/* 509 */         t = t.getCause();
/*     */       }
/* 511 */       handleThrowable(t);
/* 512 */       t.printStackTrace();
/* 513 */       System.exit(1);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getCatalinaHome()
/*     */   {
/* 525 */     return catalinaHomeFile.getPath();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getCatalinaBase()
/*     */   {
/* 536 */     return catalinaBaseFile.getPath();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static File getCatalinaHomeFile()
/*     */   {
/* 546 */     return catalinaHomeFile;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static File getCatalinaBaseFile()
/*     */   {
/* 557 */     return catalinaBaseFile;
/*     */   }
/*     */   
/*     */ 
/*     */   private static void handleThrowable(Throwable t)
/*     */   {
/* 563 */     if ((t instanceof ThreadDeath)) {
/* 564 */       throw ((ThreadDeath)t);
/*     */     }
/* 566 */     if ((t instanceof VirtualMachineError)) {
/* 567 */       throw ((VirtualMachineError)t);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static String[] getPaths(String value)
/*     */   {
/* 576 */     List<String> result = new ArrayList();
/* 577 */     Matcher matcher = PATH_PATTERN.matcher(value);
/*     */     
/* 579 */     while (matcher.find()) {
/* 580 */       String path = value.substring(matcher.start(), matcher.end());
/*     */       
/* 582 */       path = path.trim();
/* 583 */       if (path.length() != 0)
/*     */       {
/*     */ 
/*     */ 
/* 587 */         char first = path.charAt(0);
/* 588 */         char last = path.charAt(path.length() - 1);
/*     */         
/* 590 */         if ((first == '"') && (last == '"') && (path.length() > 1)) {
/* 591 */           path = path.substring(1, path.length() - 1);
/* 592 */           path = path.trim();
/* 593 */           if (path.length() != 0) {}
/*     */ 
/*     */         }
/* 596 */         else if (path.contains("\""))
/*     */         {
/*     */ 
/*     */ 
/* 600 */           throw new IllegalArgumentException("The double quote [\"] character only be used to quote paths. It must not appear in a path. This loader path is not valid: [" + value + "]");
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 607 */         result.add(path);
/*     */       } }
/* 609 */     return (String[])result.toArray(new String[result.size()]);
/*     */   }
/*     */   
/*     */   public void destroy() {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\Bootstrap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */