/*     */ package org.apache.catalina.startup;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.net.ConnectException;
/*     */ import java.net.Socket;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.logging.LogManager;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.LifecycleState;
/*     */ import org.apache.catalina.Server;
/*     */ import org.apache.catalina.security.SecurityConfig;
/*     */ import org.apache.juli.ClassLoaderLogManager;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
/*     */ import org.apache.tomcat.util.digester.Digester;
/*     */ import org.apache.tomcat.util.digester.RuleSet;
/*     */ import org.apache.tomcat.util.log.SystemLogHandler;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXParseException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Catalina
/*     */ {
/*  77 */   protected static final StringManager sm = StringManager.getManager("org.apache.catalina.startup");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  85 */   protected boolean await = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  90 */   protected String configFile = "conf/server.xml";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  96 */   protected ClassLoader parentClassLoader = Catalina.class
/*  97 */     .getClassLoader();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 103 */   protected Server server = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 109 */   protected boolean useShutdownHook = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 115 */   protected Thread shutdownHook = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 121 */   protected boolean useNaming = true;
/*     */   
/*     */ 
/*     */ 
/*     */   public Catalina()
/*     */   {
/* 127 */     setSecurityProtection();
/* 128 */     ExceptionUtils.preload();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setConfigFile(String file)
/*     */   {
/* 135 */     this.configFile = file;
/*     */   }
/*     */   
/*     */   public String getConfigFile()
/*     */   {
/* 140 */     return this.configFile;
/*     */   }
/*     */   
/*     */   public void setUseShutdownHook(boolean useShutdownHook)
/*     */   {
/* 145 */     this.useShutdownHook = useShutdownHook;
/*     */   }
/*     */   
/*     */   public boolean getUseShutdownHook()
/*     */   {
/* 150 */     return this.useShutdownHook;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setParentClassLoader(ClassLoader parentClassLoader)
/*     */   {
/* 160 */     this.parentClassLoader = parentClassLoader;
/*     */   }
/*     */   
/*     */   public ClassLoader getParentClassLoader() {
/* 164 */     if (this.parentClassLoader != null) {
/* 165 */       return this.parentClassLoader;
/*     */     }
/* 167 */     return ClassLoader.getSystemClassLoader();
/*     */   }
/*     */   
/*     */   public void setServer(Server server) {
/* 171 */     this.server = server;
/*     */   }
/*     */   
/*     */   public Server getServer()
/*     */   {
/* 176 */     return this.server;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUseNaming()
/*     */   {
/* 184 */     return this.useNaming;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUseNaming(boolean useNaming)
/*     */   {
/* 194 */     this.useNaming = useNaming;
/*     */   }
/*     */   
/*     */   public void setAwait(boolean b) {
/* 198 */     this.await = b;
/*     */   }
/*     */   
/*     */   public boolean isAwait() {
/* 202 */     return this.await;
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
/*     */   protected boolean arguments(String[] args)
/*     */   {
/* 216 */     boolean isConfig = false;
/*     */     
/* 218 */     if (args.length < 1) {
/* 219 */       usage();
/* 220 */       return false;
/*     */     }
/*     */     
/* 223 */     for (int i = 0; i < args.length; i++) {
/* 224 */       if (isConfig) {
/* 225 */         this.configFile = args[i];
/* 226 */         isConfig = false;
/* 227 */       } else if (args[i].equals("-config")) {
/* 228 */         isConfig = true;
/* 229 */       } else if (args[i].equals("-nonaming")) {
/* 230 */         setUseNaming(false);
/* 231 */       } else { if (args[i].equals("-help")) {
/* 232 */           usage();
/* 233 */           return false; }
/* 234 */         if (!args[i].equals("start"))
/*     */         {
/* 236 */           if (!args[i].equals("configtest"))
/*     */           {
/* 238 */             if (!args[i].equals("stop"))
/*     */             {
/*     */ 
/* 241 */               usage();
/* 242 */               return false;
/*     */             } } }
/*     */       }
/*     */     }
/* 246 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected File configFile()
/*     */   {
/* 256 */     File file = new File(this.configFile);
/* 257 */     if (!file.isAbsolute()) {
/* 258 */       file = new File(Bootstrap.getCatalinaBase(), this.configFile);
/*     */     }
/* 260 */     return file;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Digester createStartDigester()
/*     */   {
/* 270 */     long t1 = System.currentTimeMillis();
/*     */     
/* 272 */     Digester digester = new Digester();
/* 273 */     digester.setValidating(false);
/* 274 */     digester.setRulesValidation(true);
/* 275 */     HashMap<Class<?>, List<String>> fakeAttributes = new HashMap();
/* 276 */     ArrayList<String> attrs = new ArrayList();
/* 277 */     attrs.add("className");
/* 278 */     fakeAttributes.put(Object.class, attrs);
/* 279 */     digester.setFakeAttributes(fakeAttributes);
/* 280 */     digester.setUseContextClassLoader(true);
/*     */     
/*     */ 
/* 283 */     digester.addObjectCreate("Server", "org.apache.catalina.core.StandardServer", "className");
/*     */     
/*     */ 
/* 286 */     digester.addSetProperties("Server");
/* 287 */     digester.addSetNext("Server", "setServer", "org.apache.catalina.Server");
/*     */     
/*     */ 
/*     */ 
/* 291 */     digester.addObjectCreate("Server/GlobalNamingResources", "org.apache.catalina.deploy.NamingResourcesImpl");
/*     */     
/* 293 */     digester.addSetProperties("Server/GlobalNamingResources");
/* 294 */     digester.addSetNext("Server/GlobalNamingResources", "setGlobalNamingResources", "org.apache.catalina.deploy.NamingResourcesImpl");
/*     */     
/*     */ 
/*     */ 
/* 298 */     digester.addObjectCreate("Server/Listener", null, "className");
/*     */     
/*     */ 
/* 301 */     digester.addSetProperties("Server/Listener");
/* 302 */     digester.addSetNext("Server/Listener", "addLifecycleListener", "org.apache.catalina.LifecycleListener");
/*     */     
/*     */ 
/*     */ 
/* 306 */     digester.addObjectCreate("Server/Service", "org.apache.catalina.core.StandardService", "className");
/*     */     
/*     */ 
/* 309 */     digester.addSetProperties("Server/Service");
/* 310 */     digester.addSetNext("Server/Service", "addService", "org.apache.catalina.Service");
/*     */     
/*     */ 
/*     */ 
/* 314 */     digester.addObjectCreate("Server/Service/Listener", null, "className");
/*     */     
/*     */ 
/* 317 */     digester.addSetProperties("Server/Service/Listener");
/* 318 */     digester.addSetNext("Server/Service/Listener", "addLifecycleListener", "org.apache.catalina.LifecycleListener");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 323 */     digester.addObjectCreate("Server/Service/Executor", "org.apache.catalina.core.StandardThreadExecutor", "className");
/*     */     
/*     */ 
/* 326 */     digester.addSetProperties("Server/Service/Executor");
/*     */     
/* 328 */     digester.addSetNext("Server/Service/Executor", "addExecutor", "org.apache.catalina.Executor");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 333 */     digester.addRule("Server/Service/Connector", new ConnectorCreateRule());
/*     */     
/* 335 */     digester.addRule("Server/Service/Connector", new SetAllPropertiesRule(new String[] { "executor", "sslImplementationName" }));
/*     */     
/* 337 */     digester.addSetNext("Server/Service/Connector", "addConnector", "org.apache.catalina.connector.Connector");
/*     */     
/*     */ 
/*     */ 
/* 341 */     digester.addObjectCreate("Server/Service/Connector/SSLHostConfig", "org.apache.tomcat.util.net.SSLHostConfig");
/*     */     
/* 343 */     digester.addSetProperties("Server/Service/Connector/SSLHostConfig");
/* 344 */     digester.addSetNext("Server/Service/Connector/SSLHostConfig", "addSslHostConfig", "org.apache.tomcat.util.net.SSLHostConfig");
/*     */     
/*     */ 
/*     */ 
/* 348 */     digester.addRule("Server/Service/Connector/SSLHostConfig/Certificate", new CertificateCreateRule());
/*     */     
/* 350 */     digester.addRule("Server/Service/Connector/SSLHostConfig/Certificate", new SetAllPropertiesRule(new String[] { "type" }));
/*     */     
/* 352 */     digester.addSetNext("Server/Service/Connector/SSLHostConfig/Certificate", "addCertificate", "org.apache.tomcat.util.net.SSLHostConfigCertificate");
/*     */     
/*     */ 
/*     */ 
/* 356 */     digester.addObjectCreate("Server/Service/Connector/SSLHostConfig/OpenSSLConf", "org.apache.tomcat.util.net.openssl.OpenSSLConf");
/*     */     
/* 358 */     digester.addSetProperties("Server/Service/Connector/SSLHostConfig/OpenSSLConf");
/* 359 */     digester.addSetNext("Server/Service/Connector/SSLHostConfig/OpenSSLConf", "setOpenSslConf", "org.apache.tomcat.util.net.openssl.OpenSSLConf");
/*     */     
/*     */ 
/*     */ 
/* 363 */     digester.addObjectCreate("Server/Service/Connector/SSLHostConfig/OpenSSLConf/OpenSSLConfCmd", "org.apache.tomcat.util.net.openssl.OpenSSLConfCmd");
/*     */     
/* 365 */     digester.addSetProperties("Server/Service/Connector/SSLHostConfig/OpenSSLConf/OpenSSLConfCmd");
/* 366 */     digester.addSetNext("Server/Service/Connector/SSLHostConfig/OpenSSLConf/OpenSSLConfCmd", "addCmd", "org.apache.tomcat.util.net.openssl.OpenSSLConfCmd");
/*     */     
/*     */ 
/*     */ 
/* 370 */     digester.addObjectCreate("Server/Service/Connector/Listener", null, "className");
/*     */     
/*     */ 
/* 373 */     digester.addSetProperties("Server/Service/Connector/Listener");
/* 374 */     digester.addSetNext("Server/Service/Connector/Listener", "addLifecycleListener", "org.apache.catalina.LifecycleListener");
/*     */     
/*     */ 
/*     */ 
/* 378 */     digester.addObjectCreate("Server/Service/Connector/UpgradeProtocol", null, "className");
/*     */     
/*     */ 
/* 381 */     digester.addSetProperties("Server/Service/Connector/UpgradeProtocol");
/* 382 */     digester.addSetNext("Server/Service/Connector/UpgradeProtocol", "addUpgradeProtocol", "org.apache.coyote.UpgradeProtocol");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 387 */     digester.addRuleSet(new NamingRuleSet("Server/GlobalNamingResources/"));
/* 388 */     digester.addRuleSet(new EngineRuleSet("Server/Service/"));
/* 389 */     digester.addRuleSet(new HostRuleSet("Server/Service/Engine/"));
/* 390 */     digester.addRuleSet(new ContextRuleSet("Server/Service/Engine/Host/"));
/* 391 */     addClusterRuleSet(digester, "Server/Service/Engine/Host/Cluster/");
/* 392 */     digester.addRuleSet(new NamingRuleSet("Server/Service/Engine/Host/Context/"));
/*     */     
/*     */ 
/* 395 */     digester.addRule("Server/Service/Engine", new SetParentClassLoaderRule(this.parentClassLoader));
/*     */     
/* 397 */     addClusterRuleSet(digester, "Server/Service/Engine/Cluster/");
/*     */     
/* 399 */     long t2 = System.currentTimeMillis();
/* 400 */     if (log.isDebugEnabled()) {
/* 401 */       log.debug("Digester for server.xml created " + (t2 - t1));
/*     */     }
/* 403 */     return digester;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void addClusterRuleSet(Digester digester, String prefix)
/*     */   {
/* 411 */     Class<?> clazz = null;
/* 412 */     Constructor<?> constructor = null;
/*     */     try {
/* 414 */       clazz = Class.forName("org.apache.catalina.ha.ClusterRuleSet");
/* 415 */       constructor = clazz.getConstructor(new Class[] { String.class });
/* 416 */       RuleSet ruleSet = (RuleSet)constructor.newInstance(new Object[] { prefix });
/* 417 */       digester.addRuleSet(ruleSet);
/*     */     } catch (Exception e) {
/* 419 */       if (log.isDebugEnabled()) {
/* 420 */         log.debug(sm.getString("catalina.noCluster", new Object[] {e
/* 421 */           .getClass().getName() + ": " + e.getMessage() }), e);
/* 422 */       } else if (log.isInfoEnabled()) {
/* 423 */         log.info(sm.getString("catalina.noCluster", new Object[] {e
/* 424 */           .getClass().getName() + ": " + e.getMessage() }));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Digester createStopDigester()
/*     */   {
/* 436 */     Digester digester = new Digester();
/* 437 */     digester.setUseContextClassLoader(true);
/*     */     
/*     */ 
/* 440 */     digester.addObjectCreate("Server", "org.apache.catalina.core.StandardServer", "className");
/*     */     
/*     */ 
/* 443 */     digester.addSetProperties("Server");
/* 444 */     digester.addSetNext("Server", "setServer", "org.apache.catalina.Server");
/*     */     
/*     */ 
/*     */ 
/* 448 */     return digester;
/*     */   }
/*     */   
/*     */ 
/*     */   public void stopServer()
/*     */   {
/* 454 */     stopServer(null);
/*     */   }
/*     */   
/*     */   public void stopServer(String[] arguments)
/*     */   {
/* 459 */     if (arguments != null) {
/* 460 */       arguments(arguments);
/*     */     }
/*     */     
/* 463 */     Server s = getServer();
/* 464 */     File file; Throwable localThrowable9; if (s == null)
/*     */     {
/* 466 */       Digester digester = createStopDigester();
/* 467 */       file = configFile();
/* 468 */       try { FileInputStream fis = new FileInputStream(file);localThrowable9 = null;
/*     */         try {
/* 470 */           InputSource is = new InputSource(file.toURI().toURL().toString());
/* 471 */           is.setByteStream(fis);
/* 472 */           digester.push(this);
/* 473 */           digester.parse(is);
/*     */         }
/*     */         catch (Throwable localThrowable1)
/*     */         {
/* 468 */           localThrowable9 = localThrowable1;throw localThrowable1;
/*     */ 
/*     */         }
/*     */         finally
/*     */         {
/*     */ 
/* 474 */           if (fis != null) if (localThrowable9 != null) try { fis.close(); } catch (Throwable localThrowable2) { localThrowable9.addSuppressed(localThrowable2); } else fis.close();
/* 475 */         } } catch (Exception e) { log.error("Catalina.stop: ", e);
/* 476 */         System.exit(1);
/*     */       }
/*     */     }
/*     */     else {
/*     */       try {
/* 481 */         s.stop();
/*     */       } catch (LifecycleException e) {
/* 483 */         log.error("Catalina.stop: ", e);
/*     */       }
/* 485 */       return;
/*     */     }
/*     */     
/*     */ 
/* 489 */     s = getServer();
/* 490 */     if (s.getPort() > 0) {
/* 491 */       try { Socket socket = new Socket(s.getAddress(), s.getPort());file = null;
/* 492 */         try { OutputStream stream = socket.getOutputStream();localThrowable9 = null;
/* 493 */           try { String shutdown = s.getShutdown();
/* 494 */             for (int i = 0; i < shutdown.length(); i++) {
/* 495 */               stream.write(shutdown.charAt(i));
/*     */             }
/* 497 */             stream.flush();
/*     */           }
/*     */           catch (Throwable localThrowable4)
/*     */           {
/* 491 */             localThrowable9 = localThrowable4;throw localThrowable4; } finally {} } catch (Throwable localThrowable7) { file = localThrowable7;throw localThrowable7;
/*     */ 
/*     */ 
/*     */         }
/*     */         finally
/*     */         {
/*     */ 
/* 498 */           if (socket != null) if (file != null) try { socket.close(); } catch (Throwable localThrowable8) { file.addSuppressed(localThrowable8); } else socket.close();
/* 499 */         } } catch (ConnectException ce) { log.error(sm.getString("catalina.stopServer.connectException", new Object[] {s
/* 500 */           .getAddress(), 
/* 501 */           String.valueOf(s.getPort()) }));
/* 502 */         log.error("Catalina.stop: ", ce);
/* 503 */         System.exit(1);
/*     */       } catch (IOException e) {
/* 505 */         log.error("Catalina.stop: ", e);
/* 506 */         System.exit(1);
/*     */       }
/*     */     } else {
/* 509 */       log.error(sm.getString("catalina.stopServer"));
/* 510 */       System.exit(1);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void load()
/*     */   {
/* 520 */     long t1 = System.nanoTime();
/*     */     
/* 522 */     initDirs();
/*     */     
/*     */ 
/* 525 */     initNaming();
/*     */     
/*     */ 
/* 528 */     Digester digester = createStartDigester();
/*     */     
/* 530 */     InputSource inputSource = null;
/* 531 */     InputStream inputStream = null;
/* 532 */     File file = null;
/*     */     try {
/*     */       try {
/* 535 */         file = configFile();
/* 536 */         inputStream = new FileInputStream(file);
/* 537 */         inputSource = new InputSource(file.toURI().toURL().toString());
/*     */       } catch (Exception e) {
/* 539 */         if (log.isDebugEnabled()) {
/* 540 */           log.debug(sm.getString("catalina.configFail", new Object[] { file }), e);
/*     */         }
/*     */       }
/* 543 */       if (inputStream == null) {
/*     */         try
/*     */         {
/* 546 */           inputStream = getClass().getClassLoader().getResourceAsStream(getConfigFile());
/*     */           
/*     */ 
/* 549 */           inputSource = new InputSource(getClass().getClassLoader().getResource(getConfigFile()).toString());
/*     */         } catch (Exception e) {
/* 551 */           if (log.isDebugEnabled()) {
/* 552 */             log.debug(sm.getString("catalina.configFail", new Object[] {
/* 553 */               getConfigFile() }), e);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 560 */       if (inputStream == null) {
/*     */         try
/*     */         {
/* 563 */           inputStream = getClass().getClassLoader().getResourceAsStream("server-embed.xml");
/*     */           
/*     */ 
/* 566 */           inputSource = new InputSource(getClass().getClassLoader().getResource("server-embed.xml").toString());
/*     */         } catch (Exception e) {
/* 568 */           if (log.isDebugEnabled()) {
/* 569 */             log.debug(sm.getString("catalina.configFail", new Object[] { "server-embed.xml" }), e);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 576 */       if ((inputStream == null) || (inputSource == null)) {
/* 577 */         if (file == null) {
/* 578 */           log.warn(sm.getString("catalina.configFail", new Object[] {
/* 579 */             getConfigFile() + "] or [server-embed.xml]" }));
/*     */         } else {
/* 581 */           log.warn(sm.getString("catalina.configFail", new Object[] {file
/* 582 */             .getAbsolutePath() }));
/* 583 */           if ((file.exists()) && (!file.canRead())) {
/* 584 */             log.warn("Permissions incorrect, read permission is not allowed on the file.");
/*     */           }
/*     */         }
/* 587 */         return;
/*     */       }
/*     */       try
/*     */       {
/* 591 */         inputSource.setByteStream(inputStream);
/* 592 */         digester.push(this);
/* 593 */         digester.parse(inputSource);
/*     */       } catch (SAXParseException spe) {
/* 595 */         log.warn("Catalina.start using " + getConfigFile() + ": " + spe
/* 596 */           .getMessage());
/* 597 */         return;
/*     */       } catch (Exception e) {
/* 599 */         log.warn("Catalina.start using " + getConfigFile() + ": ", e);
/* 600 */         return;
/*     */       }
/*     */       
/* 603 */       if (inputStream != null) {
/*     */         try {
/* 605 */           inputStream.close();
/*     */         }
/*     */         catch (IOException localIOException3) {}
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 612 */       getServer().setCatalina(this);
/*     */     }
/*     */     finally
/*     */     {
/* 603 */       if (inputStream != null) {
/*     */         try {
/* 605 */           inputStream.close();
/*     */         }
/*     */         catch (IOException localIOException4) {}
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 613 */     getServer().setCatalinaHome(Bootstrap.getCatalinaHomeFile());
/* 614 */     getServer().setCatalinaBase(Bootstrap.getCatalinaBaseFile());
/*     */     
/*     */ 
/* 617 */     initStreams();
/*     */     
/*     */     try
/*     */     {
/* 621 */       getServer().init();
/*     */     } catch (LifecycleException e) {
/* 623 */       if (Boolean.getBoolean("org.apache.catalina.startup.EXIT_ON_INIT_FAILURE")) {
/* 624 */         throw new Error(e);
/*     */       }
/* 626 */       log.error("Catalina.start", e);
/*     */     }
/*     */     
/*     */ 
/* 630 */     long t2 = System.nanoTime();
/* 631 */     if (log.isInfoEnabled()) {
/* 632 */       log.info("Initialization processed in " + (t2 - t1) / 1000000L + " ms");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void load(String[] args)
/*     */   {
/*     */     try
/*     */     {
/* 643 */       if (arguments(args)) {
/* 644 */         load();
/*     */       }
/*     */     } catch (Exception e) {
/* 647 */       e.printStackTrace(System.out);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void start()
/*     */   {
/* 657 */     if (getServer() == null) {
/* 658 */       load();
/*     */     }
/*     */     
/* 661 */     if (getServer() == null) {
/* 662 */       log.fatal("Cannot start server. Server instance is not configured.");
/* 663 */       return;
/*     */     }
/*     */     
/* 666 */     long t1 = System.nanoTime();
/*     */     
/*     */     try
/*     */     {
/* 670 */       getServer().start();
/*     */     } catch (LifecycleException e) {
/* 672 */       log.fatal(sm.getString("catalina.serverStartFail"), e);
/*     */       try {
/* 674 */         getServer().destroy();
/*     */       } catch (LifecycleException e1) {
/* 676 */         log.debug("destroy() failed for failed Server ", e1);
/*     */       }
/* 678 */       return;
/*     */     }
/*     */     
/* 681 */     long t2 = System.nanoTime();
/* 682 */     if (log.isInfoEnabled()) {
/* 683 */       log.info("Server startup in " + (t2 - t1) / 1000000L + " ms");
/*     */     }
/*     */     
/*     */ 
/* 687 */     if (this.useShutdownHook) {
/* 688 */       if (this.shutdownHook == null) {
/* 689 */         this.shutdownHook = new CatalinaShutdownHook();
/*     */       }
/* 691 */       Runtime.getRuntime().addShutdownHook(this.shutdownHook);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 696 */       LogManager logManager = LogManager.getLogManager();
/* 697 */       if ((logManager instanceof ClassLoaderLogManager)) {
/* 698 */         ((ClassLoaderLogManager)logManager).setUseShutdownHook(false);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 703 */     if (this.await) {
/* 704 */       await();
/* 705 */       stop();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void stop()
/*     */   {
/*     */     try
/*     */     {
/* 718 */       if (this.useShutdownHook) {
/* 719 */         Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
/*     */         
/*     */ 
/*     */ 
/* 723 */         LogManager logManager = LogManager.getLogManager();
/* 724 */         if ((logManager instanceof ClassLoaderLogManager)) {
/* 725 */           ((ClassLoaderLogManager)logManager).setUseShutdownHook(true);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Throwable t) {
/* 730 */       ExceptionUtils.handleThrowable(t);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 737 */       Server s = getServer();
/* 738 */       LifecycleState state = s.getState();
/* 739 */       if ((LifecycleState.STOPPING_PREP.compareTo(state) > 0) || 
/* 740 */         (LifecycleState.DESTROYED.compareTo(state) < 0))
/*     */       {
/*     */ 
/* 743 */         s.stop();
/* 744 */         s.destroy();
/*     */       }
/*     */     } catch (LifecycleException e) {
/* 747 */       log.error("Catalina.stop", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void await()
/*     */   {
/* 758 */     getServer().await();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void usage()
/*     */   {
/* 769 */     System.out.println("usage: java org.apache.catalina.startup.Catalina [ -config {pathname} ] [ -nonaming ]  { -help | start | stop }");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initDirs()
/*     */   {
/* 778 */     String temp = System.getProperty("java.io.tmpdir");
/* 779 */     if ((temp == null) || (!new File(temp).isDirectory())) {
/* 780 */       log.error(sm.getString("embedded.notmp", new Object[] { temp }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void initStreams()
/*     */   {
/* 787 */     System.setOut(new SystemLogHandler(System.out));
/* 788 */     System.setErr(new SystemLogHandler(System.err));
/*     */   }
/*     */   
/*     */ 
/*     */   protected void initNaming()
/*     */   {
/* 794 */     if (!this.useNaming) {
/* 795 */       log.info("Catalina naming disabled");
/* 796 */       System.setProperty("catalina.useNaming", "false");
/*     */     } else {
/* 798 */       System.setProperty("catalina.useNaming", "true");
/* 799 */       String value = "org.apache.naming";
/*     */       
/* 801 */       String oldValue = System.getProperty("java.naming.factory.url.pkgs");
/* 802 */       if (oldValue != null) {
/* 803 */         value = value + ":" + oldValue;
/*     */       }
/* 805 */       System.setProperty("java.naming.factory.url.pkgs", value);
/* 806 */       if (log.isDebugEnabled()) {
/* 807 */         log.debug("Setting naming prefix=" + value);
/*     */       }
/*     */       
/* 810 */       value = System.getProperty("java.naming.factory.initial");
/* 811 */       if (value == null)
/*     */       {
/* 813 */         System.setProperty("java.naming.factory.initial", "org.apache.naming.java.javaURLContextFactory");
/*     */       }
/*     */       else {
/* 816 */         log.debug("INITIAL_CONTEXT_FACTORY already set " + value);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setSecurityProtection()
/*     */   {
/* 826 */     SecurityConfig securityConfig = SecurityConfig.newInstance();
/* 827 */     securityConfig.setPackageDefinition();
/* 828 */     securityConfig.setPackageAccess();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected class CatalinaShutdownHook
/*     */     extends Thread
/*     */   {
/*     */     protected CatalinaShutdownHook() {}
/*     */     
/*     */ 
/*     */     public void run()
/*     */     {
/*     */       try
/*     */       {
/* 843 */         if (Catalina.this.getServer() != null)
/* 844 */           Catalina.this.stop();
/*     */       } catch (Throwable ex) {
/*     */         LogManager logManager;
/* 847 */         ExceptionUtils.handleThrowable(ex);
/* 848 */         Catalina.log.error(Catalina.sm.getString("catalina.shutdownHookFail"), ex);
/*     */       }
/*     */       finally {
/*     */         LogManager logManager;
/* 852 */         LogManager logManager = LogManager.getLogManager();
/* 853 */         if ((logManager instanceof ClassLoaderLogManager)) {
/* 854 */           ((ClassLoaderLogManager)logManager).shutdown();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/* 861 */   private static final Log log = LogFactory.getLog(Catalina.class);
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\Catalina.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */