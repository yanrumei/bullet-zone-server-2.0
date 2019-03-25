/*      */ package org.apache.catalina.startup;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URI;
/*      */ import java.net.URL;
/*      */ import java.security.Principal;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Stack;
/*      */ import java.util.jar.JarEntry;
/*      */ import java.util.jar.JarFile;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.servlet.Servlet;
/*      */ import javax.servlet.ServletException;
/*      */ import javax.servlet.SingleThreadModel;
/*      */ import javax.servlet.annotation.WebServlet;
/*      */ import org.apache.catalina.Container;
/*      */ import org.apache.catalina.Context;
/*      */ import org.apache.catalina.Engine;
/*      */ import org.apache.catalina.Host;
/*      */ import org.apache.catalina.LifecycleEvent;
/*      */ import org.apache.catalina.LifecycleException;
/*      */ import org.apache.catalina.LifecycleListener;
/*      */ import org.apache.catalina.Pipeline;
/*      */ import org.apache.catalina.Realm;
/*      */ import org.apache.catalina.Server;
/*      */ import org.apache.catalina.Service;
/*      */ import org.apache.catalina.Wrapper;
/*      */ import org.apache.catalina.authenticator.NonLoginAuthenticator;
/*      */ import org.apache.catalina.connector.Connector;
/*      */ import org.apache.catalina.core.ContainerBase;
/*      */ import org.apache.catalina.core.NamingContextListener;
/*      */ import org.apache.catalina.core.StandardContext;
/*      */ import org.apache.catalina.core.StandardEngine;
/*      */ import org.apache.catalina.core.StandardHost;
/*      */ import org.apache.catalina.core.StandardServer;
/*      */ import org.apache.catalina.core.StandardService;
/*      */ import org.apache.catalina.core.StandardWrapper;
/*      */ import org.apache.catalina.realm.GenericPrincipal;
/*      */ import org.apache.catalina.realm.RealmBase;
/*      */ import org.apache.tomcat.util.ExceptionUtils;
/*      */ import org.apache.tomcat.util.buf.UriUtil;
/*      */ import org.apache.tomcat.util.descriptor.web.LoginConfig;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Tomcat
/*      */ {
/*  146 */   private final Map<String, Logger> pinnedLoggers = new HashMap();
/*      */   
/*      */   protected Server server;
/*      */   
/*  150 */   protected int port = 8080;
/*  151 */   protected String hostname = "localhost";
/*      */   protected String basedir;
/*  153 */   protected boolean defaultConnectorCreated = false;
/*      */   
/*  155 */   private final Map<String, String> userPass = new HashMap();
/*  156 */   private final Map<String, List<String>> userRoles = new HashMap();
/*  157 */   private final Map<String, Principal> userPrincipals = new HashMap();
/*      */   
/*      */   public Tomcat() {
/*  160 */     ExceptionUtils.preload();
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
/*      */   public void setBaseDir(String basedir)
/*      */   {
/*  182 */     this.basedir = basedir;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPort(int port)
/*      */   {
/*  191 */     this.port = port;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setHostname(String s)
/*      */   {
/*  200 */     this.hostname = s;
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
/*      */   public Context addWebapp(String contextPath, String docBase)
/*      */     throws ServletException
/*      */   {
/*  218 */     return addWebapp(getHost(), contextPath, docBase);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Context addContext(String contextPath, String docBase)
/*      */   {
/*  265 */     return addContext(getHost(), contextPath, docBase);
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
/*      */   public Wrapper addServlet(String contextPath, String servletName, String servletClass)
/*      */   {
/*  290 */     Container ctx = getHost().findChild(contextPath);
/*  291 */     return addServlet((Context)ctx, servletName, servletClass);
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
/*      */   public static Wrapper addServlet(Context ctx, String servletName, String servletClass)
/*      */   {
/*  305 */     Wrapper sw = ctx.createWrapper();
/*  306 */     sw.setServletClass(servletClass);
/*  307 */     sw.setName(servletName);
/*  308 */     ctx.addChild(sw);
/*      */     
/*  310 */     return sw;
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
/*      */   public Wrapper addServlet(String contextPath, String servletName, Servlet servlet)
/*      */   {
/*  324 */     Container ctx = getHost().findChild(contextPath);
/*  325 */     return addServlet((Context)ctx, servletName, servlet);
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
/*      */   public static Wrapper addServlet(Context ctx, String servletName, Servlet servlet)
/*      */   {
/*  339 */     Wrapper sw = new ExistingStandardWrapper(servlet);
/*  340 */     sw.setName(servletName);
/*  341 */     ctx.addChild(sw);
/*      */     
/*  343 */     return sw;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void init()
/*      */     throws LifecycleException
/*      */   {
/*  353 */     getServer();
/*  354 */     getConnector();
/*  355 */     this.server.init();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void start()
/*      */     throws LifecycleException
/*      */   {
/*  365 */     getServer();
/*  366 */     getConnector();
/*  367 */     this.server.start();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void stop()
/*      */     throws LifecycleException
/*      */   {
/*  376 */     getServer();
/*  377 */     this.server.stop();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void destroy()
/*      */     throws LifecycleException
/*      */   {
/*  388 */     getServer();
/*  389 */     this.server.destroy();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addUser(String user, String pass)
/*      */   {
/*  400 */     this.userPass.put(user, pass);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addRole(String user, String role)
/*      */   {
/*  410 */     List<String> roles = (List)this.userRoles.get(user);
/*  411 */     if (roles == null) {
/*  412 */       roles = new ArrayList();
/*  413 */       this.userRoles.put(user, roles);
/*      */     }
/*  415 */     roles.add(role);
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
/*      */   public Connector getConnector()
/*      */   {
/*  432 */     Service service = getService();
/*  433 */     if (service.findConnectors().length > 0) {
/*  434 */       return service.findConnectors()[0];
/*      */     }
/*      */     
/*  437 */     if (this.defaultConnectorCreated) {
/*  438 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  444 */     Connector connector = new Connector("HTTP/1.1");
/*  445 */     connector.setPort(this.port);
/*  446 */     service.addConnector(connector);
/*  447 */     this.defaultConnectorCreated = true;
/*  448 */     return connector;
/*      */   }
/*      */   
/*      */   public void setConnector(Connector connector) {
/*  452 */     this.defaultConnectorCreated = true;
/*  453 */     Service service = getService();
/*  454 */     boolean found = false;
/*  455 */     for (Connector serviceConnector : service.findConnectors()) {
/*  456 */       if (connector == serviceConnector) {
/*  457 */         found = true;
/*      */       }
/*      */     }
/*  460 */     if (!found) {
/*  461 */       service.addConnector(connector);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Service getService()
/*      */   {
/*  471 */     return getServer().findServices()[0];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setHost(Host host)
/*      */   {
/*  482 */     Engine engine = getEngine();
/*  483 */     boolean found = false;
/*  484 */     for (Container engineHost : engine.findChildren()) {
/*  485 */       if (engineHost == host) {
/*  486 */         found = true;
/*      */       }
/*      */     }
/*  489 */     if (!found) {
/*  490 */       engine.addChild(host);
/*      */     }
/*      */   }
/*      */   
/*      */   public Host getHost() {
/*  495 */     Engine engine = getEngine();
/*  496 */     if (engine.findChildren().length > 0) {
/*  497 */       return (Host)engine.findChildren()[0];
/*      */     }
/*      */     
/*  500 */     Host host = new StandardHost();
/*  501 */     host.setName(this.hostname);
/*  502 */     getEngine().addChild(host);
/*  503 */     return host;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Engine getEngine()
/*      */   {
/*  511 */     Service service = getServer().findServices()[0];
/*  512 */     if (service.getContainer() != null) {
/*  513 */       return service.getContainer();
/*      */     }
/*  515 */     Engine engine = new StandardEngine();
/*  516 */     engine.setName("Tomcat");
/*  517 */     engine.setDefaultHost(this.hostname);
/*  518 */     engine.setRealm(createDefaultRealm());
/*  519 */     service.setContainer(engine);
/*  520 */     return engine;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Server getServer()
/*      */   {
/*  530 */     if (this.server != null) {
/*  531 */       return this.server;
/*      */     }
/*      */     
/*  534 */     System.setProperty("catalina.useNaming", "false");
/*      */     
/*  536 */     this.server = new StandardServer();
/*      */     
/*  538 */     initBaseDir();
/*      */     
/*  540 */     this.server.setPort(-1);
/*      */     
/*  542 */     Service service = new StandardService();
/*  543 */     service.setName("Tomcat");
/*  544 */     this.server.addService(service);
/*  545 */     return this.server;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Context addContext(Host host, String contextPath, String dir)
/*      */   {
/*  557 */     return addContext(host, contextPath, contextPath, dir);
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
/*      */   public Context addContext(Host host, String contextPath, String contextName, String dir)
/*      */   {
/*  571 */     silence(host, contextName);
/*  572 */     Context ctx = createContext(host, contextPath);
/*  573 */     ctx.setName(contextName);
/*  574 */     ctx.setPath(contextPath);
/*  575 */     ctx.setDocBase(dir);
/*  576 */     ctx.addLifecycleListener(new FixContextListener());
/*      */     
/*  578 */     if (host == null) {
/*  579 */       getHost().addChild(ctx);
/*      */     } else {
/*  581 */       host.addChild(ctx);
/*      */     }
/*  583 */     return ctx;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Context addWebapp(Host host, String contextPath, String docBase)
/*      */   {
/*  595 */     LifecycleListener listener = null;
/*      */     try {
/*  597 */       Class<?> clazz = Class.forName(getHost().getConfigClass());
/*  598 */       listener = (LifecycleListener)clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/*      */     }
/*      */     catch (ReflectiveOperationException e)
/*      */     {
/*  602 */       throw new IllegalArgumentException(e);
/*      */     }
/*      */     
/*  605 */     return addWebapp(host, contextPath, docBase, listener);
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
/*      */   @Deprecated
/*      */   public Context addWebapp(Host host, String contextPath, String docBase, ContextConfig config)
/*      */   {
/*  622 */     return addWebapp(host, contextPath, docBase, config);
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
/*      */   public Context addWebapp(Host host, String contextPath, String docBase, LifecycleListener config)
/*      */   {
/*  638 */     silence(host, contextPath);
/*      */     
/*  640 */     Context ctx = createContext(host, contextPath);
/*  641 */     ctx.setPath(contextPath);
/*  642 */     ctx.setDocBase(docBase);
/*  643 */     ctx.addLifecycleListener(getDefaultWebXmlListener());
/*  644 */     ctx.setConfigFile(getWebappConfigFile(docBase, contextPath));
/*      */     
/*  646 */     ctx.addLifecycleListener(config);
/*      */     
/*  648 */     if ((config instanceof ContextConfig))
/*      */     {
/*  650 */       ((ContextConfig)config).setDefaultWebXml(noDefaultWebXmlPath());
/*      */     }
/*      */     
/*  653 */     if (host == null) {
/*  654 */       getHost().addChild(ctx);
/*      */     } else {
/*  656 */       host.addChild(ctx);
/*      */     }
/*      */     
/*  659 */     return ctx;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public LifecycleListener getDefaultWebXmlListener()
/*      */   {
/*  671 */     return new DefaultWebXmlListener();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String noDefaultWebXmlPath()
/*      */   {
/*  680 */     return "org/apache/catalina/startup/NO_DEFAULT_XML";
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
/*      */   protected Realm createDefaultRealm()
/*      */   {
/*  693 */     new RealmBase()
/*      */     {
/*      */       @Deprecated
/*      */       protected String getName() {
/*  697 */         return "Simple";
/*      */       }
/*      */       
/*      */       protected String getPassword(String username)
/*      */       {
/*  702 */         return (String)Tomcat.this.userPass.get(username);
/*      */       }
/*      */       
/*      */       protected Principal getPrincipal(String username)
/*      */       {
/*  707 */         Principal p = (Principal)Tomcat.this.userPrincipals.get(username);
/*  708 */         if (p == null) {
/*  709 */           String pass = (String)Tomcat.this.userPass.get(username);
/*  710 */           if (pass != null)
/*      */           {
/*  712 */             p = new GenericPrincipal(username, pass, (List)Tomcat.this.userRoles.get(username));
/*  713 */             Tomcat.this.userPrincipals.put(username, p);
/*      */           }
/*      */         }
/*  716 */         return p;
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */   protected void initBaseDir()
/*      */   {
/*  723 */     String catalinaHome = System.getProperty("catalina.home");
/*  724 */     if (this.basedir == null) {
/*  725 */       this.basedir = System.getProperty("catalina.base");
/*      */     }
/*  727 */     if (this.basedir == null) {
/*  728 */       this.basedir = catalinaHome;
/*      */     }
/*  730 */     if (this.basedir == null)
/*      */     {
/*  732 */       this.basedir = (System.getProperty("user.dir") + "/tomcat." + this.port);
/*      */     }
/*      */     
/*      */ 
/*  736 */     File baseFile = new File(this.basedir);
/*  737 */     baseFile.mkdirs();
/*      */     try {
/*  739 */       baseFile = baseFile.getCanonicalFile();
/*      */     } catch (IOException e) {
/*  741 */       baseFile = baseFile.getAbsoluteFile();
/*      */     }
/*  743 */     this.server.setCatalinaBase(baseFile);
/*  744 */     System.setProperty("catalina.base", baseFile.getPath());
/*  745 */     this.basedir = baseFile.getPath();
/*      */     
/*  747 */     if (catalinaHome == null) {
/*  748 */       this.server.setCatalinaHome(baseFile);
/*      */     } else {
/*  750 */       File homeFile = new File(catalinaHome);
/*  751 */       homeFile.mkdirs();
/*      */       try {
/*  753 */         homeFile = homeFile.getCanonicalFile();
/*      */       } catch (IOException e) {
/*  755 */         homeFile = homeFile.getAbsoluteFile();
/*      */       }
/*  757 */       this.server.setCatalinaHome(homeFile);
/*      */     }
/*  759 */     System.setProperty("catalina.home", this.server
/*  760 */       .getCatalinaHome().getPath());
/*      */   }
/*      */   
/*  763 */   static final String[] silences = { "org.apache.coyote.http11.Http11NioProtocol", "org.apache.catalina.core.StandardService", "org.apache.catalina.core.StandardEngine", "org.apache.catalina.startup.ContextConfig", "org.apache.catalina.core.ApplicationContext", "org.apache.catalina.core.AprLifecycleListener" };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  772 */   private boolean silent = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSilent(boolean silent)
/*      */   {
/*  783 */     this.silent = silent;
/*  784 */     for (String s : silences) {
/*  785 */       Logger logger = Logger.getLogger(s);
/*  786 */       this.pinnedLoggers.put(s, logger);
/*  787 */       if (silent) {
/*  788 */         logger.setLevel(Level.WARNING);
/*      */       } else {
/*  790 */         logger.setLevel(Level.INFO);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void silence(Host host, String contextPath) {
/*  796 */     String loggerName = getLoggerName(host, contextPath);
/*  797 */     Logger logger = Logger.getLogger(loggerName);
/*  798 */     this.pinnedLoggers.put(loggerName, logger);
/*  799 */     if (this.silent) {
/*  800 */       logger.setLevel(Level.WARNING);
/*      */     } else {
/*  802 */       logger.setLevel(Level.INFO);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private String getLoggerName(Host host, String contextName)
/*      */   {
/*  811 */     if (host == null) {
/*  812 */       host = getHost();
/*      */     }
/*  814 */     StringBuilder loggerName = new StringBuilder();
/*  815 */     loggerName.append(ContainerBase.class.getName());
/*  816 */     loggerName.append(".[");
/*      */     
/*  818 */     loggerName.append(host.getParent().getName());
/*  819 */     loggerName.append("].[");
/*      */     
/*  821 */     loggerName.append(host.getName());
/*  822 */     loggerName.append("].[");
/*      */     
/*  824 */     if ((contextName == null) || (contextName.equals(""))) {
/*  825 */       loggerName.append("/");
/*  826 */     } else if (contextName.startsWith("##")) {
/*  827 */       loggerName.append("/");
/*  828 */       loggerName.append(contextName);
/*      */     }
/*  830 */     loggerName.append(']');
/*      */     
/*  832 */     return loggerName.toString();
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
/*      */   private Context createContext(Host host, String url)
/*      */   {
/*  848 */     String contextClass = StandardContext.class.getName();
/*  849 */     if (host == null) {
/*  850 */       host = getHost();
/*      */     }
/*  852 */     if ((host instanceof StandardHost)) {
/*  853 */       contextClass = ((StandardHost)host).getContextClass();
/*      */     }
/*      */     try {
/*  856 */       return 
/*  857 */         (Context)Class.forName(contextClass).getConstructor(new Class[0]).newInstance(new Object[0]);
/*      */ 
/*      */     }
/*      */     catch (InstantiationException|IllegalAccessException|IllegalArgumentException|InvocationTargetException|NoSuchMethodException|SecurityException|ClassNotFoundException e)
/*      */     {
/*  862 */       throw new IllegalArgumentException("Can't instantiate context-class " + contextClass + " for host " + host + " and url " + url, e);
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
/*      */   public void enableNaming()
/*      */   {
/*  878 */     getServer();
/*  879 */     this.server.addLifecycleListener(new NamingContextListener());
/*      */     
/*  881 */     System.setProperty("catalina.useNaming", "true");
/*      */     
/*  883 */     String value = "org.apache.naming";
/*      */     
/*  885 */     String oldValue = System.getProperty("java.naming.factory.url.pkgs");
/*  886 */     if (oldValue != null) {
/*  887 */       if (oldValue.contains(value)) {
/*  888 */         value = oldValue;
/*      */       } else {
/*  890 */         value = value + ":" + oldValue;
/*      */       }
/*      */     }
/*  893 */     System.setProperty("java.naming.factory.url.pkgs", value);
/*      */     
/*      */ 
/*  896 */     value = System.getProperty("java.naming.factory.initial");
/*  897 */     if (value == null)
/*      */     {
/*  899 */       System.setProperty("java.naming.factory.initial", "org.apache.naming.java.javaURLContextFactory");
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
/*      */   public void initWebappDefaults(String contextPath)
/*      */   {
/*  914 */     Container ctx = getHost().findChild(contextPath);
/*  915 */     initWebappDefaults((Context)ctx);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void initWebappDefaults(Context ctx)
/*      */   {
/*  924 */     Wrapper servlet = addServlet(ctx, "default", "org.apache.catalina.servlets.DefaultServlet");
/*      */     
/*  926 */     servlet.setLoadOnStartup(1);
/*  927 */     servlet.setOverridable(true);
/*      */     
/*      */ 
/*  930 */     servlet = addServlet(ctx, "jsp", "org.apache.jasper.servlet.JspServlet");
/*      */     
/*  932 */     servlet.addInitParameter("fork", "false");
/*  933 */     servlet.setLoadOnStartup(3);
/*  934 */     servlet.setOverridable(true);
/*      */     
/*      */ 
/*  937 */     ctx.addServletMappingDecoded("/", "default");
/*  938 */     ctx.addServletMappingDecoded("*.jsp", "jsp");
/*  939 */     ctx.addServletMappingDecoded("*.jspx", "jsp");
/*      */     
/*      */ 
/*  942 */     ctx.setSessionTimeout(30);
/*      */     
/*      */ 
/*  945 */     for (int i = 0; i < DEFAULT_MIME_MAPPINGS.length;) {
/*  946 */       ctx.addMimeMapping(DEFAULT_MIME_MAPPINGS[(i++)], DEFAULT_MIME_MAPPINGS[(i++)]);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  951 */     ctx.addWelcomeFile("index.html");
/*  952 */     ctx.addWelcomeFile("index.htm");
/*  953 */     ctx.addWelcomeFile("index.jsp");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class FixContextListener
/*      */     implements LifecycleListener
/*      */   {
/*      */     public void lifecycleEvent(LifecycleEvent event)
/*      */     {
/*      */       try
/*      */       {
/*  969 */         Context context = (Context)event.getLifecycle();
/*  970 */         if (event.getType().equals("configure_start")) {
/*  971 */           context.setConfigured(true);
/*      */         }
/*      */         
/*      */ 
/*  975 */         if (context.getLoginConfig() == null) {
/*  976 */           context.setLoginConfig(new LoginConfig("NONE", null, null, null));
/*      */           
/*  978 */           context.getPipeline().addValve(new NonLoginAuthenticator());
/*      */         }
/*      */       }
/*      */       catch (ClassCastException e) {}
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class DefaultWebXmlListener
/*      */     implements LifecycleListener
/*      */   {
/*      */     public void lifecycleEvent(LifecycleEvent event)
/*      */     {
/*  996 */       if ("before_start".equals(event.getType())) {
/*  997 */         Tomcat.initWebappDefaults((Context)event.getLifecycle());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static class ExistingStandardWrapper
/*      */     extends StandardWrapper
/*      */   {
/*      */     private final Servlet existing;
/*      */     
/*      */ 
/*      */ 
/*      */     public ExistingStandardWrapper(Servlet existing)
/*      */     {
/* 1013 */       this.existing = existing;
/* 1014 */       if ((existing instanceof SingleThreadModel)) {
/* 1015 */         this.singleThreadModel = true;
/* 1016 */         this.instancePool = new Stack();
/*      */       }
/* 1018 */       this.asyncSupported = hasAsync(existing);
/*      */     }
/*      */     
/*      */     private static boolean hasAsync(Servlet existing) {
/* 1022 */       boolean result = false;
/* 1023 */       Class<?> clazz = existing.getClass();
/* 1024 */       WebServlet ws = (WebServlet)clazz.getAnnotation(WebServlet.class);
/* 1025 */       if (ws != null) {
/* 1026 */         result = ws.asyncSupported();
/*      */       }
/* 1028 */       return result;
/*      */     }
/*      */     
/*      */     public synchronized Servlet loadServlet() throws ServletException
/*      */     {
/* 1033 */       if (this.singleThreadModel)
/*      */       {
/*      */         try {
/* 1036 */           instance = (Servlet)this.existing.getClass().getConstructor(new Class[0]).newInstance(new Object[0]);
/*      */         } catch (ReflectiveOperationException e) { Servlet instance;
/* 1038 */           throw new ServletException(e); }
/*      */         Servlet instance;
/* 1040 */         instance.init(this.facade);
/* 1041 */         return instance;
/*      */       }
/* 1043 */       if (!this.instanceInitialized) {
/* 1044 */         this.existing.init(this.facade);
/* 1045 */         this.instanceInitialized = true;
/*      */       }
/* 1047 */       return this.existing;
/*      */     }
/*      */     
/*      */     public long getAvailable()
/*      */     {
/* 1052 */       return 0L;
/*      */     }
/*      */     
/*      */     public boolean isUnavailable() {
/* 1056 */       return false;
/*      */     }
/*      */     
/*      */     public Servlet getServlet() {
/* 1060 */       return this.existing;
/*      */     }
/*      */     
/*      */     public String getServletClass() {
/* 1064 */       return this.existing.getClass().getName();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1074 */   private static final String[] DEFAULT_MIME_MAPPINGS = { "abs", "audio/x-mpeg", "ai", "application/postscript", "aif", "audio/x-aiff", "aifc", "audio/x-aiff", "aiff", "audio/x-aiff", "aim", "application/x-aim", "art", "image/x-jg", "asf", "video/x-ms-asf", "asx", "video/x-ms-asf", "au", "audio/basic", "avi", "video/x-msvideo", "avx", "video/x-rad-screenplay", "bcpio", "application/x-bcpio", "bin", "application/octet-stream", "bmp", "image/bmp", "body", "text/html", "cdf", "application/x-cdf", "cer", "application/pkix-cert", "class", "application/java", "cpio", "application/x-cpio", "csh", "application/x-csh", "css", "text/css", "dib", "image/bmp", "doc", "application/msword", "dtd", "application/xml-dtd", "dv", "video/x-dv", "dvi", "application/x-dvi", "eps", "application/postscript", "etx", "text/x-setext", "exe", "application/octet-stream", "gif", "image/gif", "gtar", "application/x-gtar", "gz", "application/x-gzip", "hdf", "application/x-hdf", "hqx", "application/mac-binhex40", "htc", "text/x-component", "htm", "text/html", "html", "text/html", "ief", "image/ief", "jad", "text/vnd.sun.j2me.app-descriptor", "jar", "application/java-archive", "java", "text/x-java-source", "jnlp", "application/x-java-jnlp-file", "jpe", "image/jpeg", "jpeg", "image/jpeg", "jpg", "image/jpeg", "js", "application/javascript", "jsf", "text/plain", "jspf", "text/plain", "kar", "audio/midi", "latex", "application/x-latex", "m3u", "audio/x-mpegurl", "mac", "image/x-macpaint", "man", "text/troff", "mathml", "application/mathml+xml", "me", "text/troff", "mid", "audio/midi", "midi", "audio/midi", "mif", "application/x-mif", "mov", "video/quicktime", "movie", "video/x-sgi-movie", "mp1", "audio/mpeg", "mp2", "audio/mpeg", "mp3", "audio/mpeg", "mp4", "video/mp4", "mpa", "audio/mpeg", "mpe", "video/mpeg", "mpeg", "video/mpeg", "mpega", "audio/x-mpeg", "mpg", "video/mpeg", "mpv2", "video/mpeg2", "nc", "application/x-netcdf", "oda", "application/oda", "odb", "application/vnd.oasis.opendocument.database", "odc", "application/vnd.oasis.opendocument.chart", "odf", "application/vnd.oasis.opendocument.formula", "odg", "application/vnd.oasis.opendocument.graphics", "odi", "application/vnd.oasis.opendocument.image", "odm", "application/vnd.oasis.opendocument.text-master", "odp", "application/vnd.oasis.opendocument.presentation", "ods", "application/vnd.oasis.opendocument.spreadsheet", "odt", "application/vnd.oasis.opendocument.text", "otg", "application/vnd.oasis.opendocument.graphics-template", "oth", "application/vnd.oasis.opendocument.text-web", "otp", "application/vnd.oasis.opendocument.presentation-template", "ots", "application/vnd.oasis.opendocument.spreadsheet-template ", "ott", "application/vnd.oasis.opendocument.text-template", "ogx", "application/ogg", "ogv", "video/ogg", "oga", "audio/ogg", "ogg", "audio/ogg", "spx", "audio/ogg", "flac", "audio/flac", "anx", "application/annodex", "axa", "audio/annodex", "axv", "video/annodex", "xspf", "application/xspf+xml", "pbm", "image/x-portable-bitmap", "pct", "image/pict", "pdf", "application/pdf", "pgm", "image/x-portable-graymap", "pic", "image/pict", "pict", "image/pict", "pls", "audio/x-scpls", "png", "image/png", "pnm", "image/x-portable-anymap", "pnt", "image/x-macpaint", "ppm", "image/x-portable-pixmap", "ppt", "application/vnd.ms-powerpoint", "pps", "application/vnd.ms-powerpoint", "ps", "application/postscript", "psd", "image/vnd.adobe.photoshop", "qt", "video/quicktime", "qti", "image/x-quicktime", "qtif", "image/x-quicktime", "ras", "image/x-cmu-raster", "rdf", "application/rdf+xml", "rgb", "image/x-rgb", "rm", "application/vnd.rn-realmedia", "roff", "text/troff", "rtf", "application/rtf", "rtx", "text/richtext", "sh", "application/x-sh", "shar", "application/x-shar", "sit", "application/x-stuffit", "snd", "audio/basic", "src", "application/x-wais-source", "sv4cpio", "application/x-sv4cpio", "sv4crc", "application/x-sv4crc", "svg", "image/svg+xml", "svgz", "image/svg+xml", "swf", "application/x-shockwave-flash", "t", "text/troff", "tar", "application/x-tar", "tcl", "application/x-tcl", "tex", "application/x-tex", "texi", "application/x-texinfo", "texinfo", "application/x-texinfo", "tif", "image/tiff", "tiff", "image/tiff", "tr", "text/troff", "tsv", "text/tab-separated-values", "txt", "text/plain", "ulw", "audio/basic", "ustar", "application/x-ustar", "vxml", "application/voicexml+xml", "xbm", "image/x-xbitmap", "xht", "application/xhtml+xml", "xhtml", "application/xhtml+xml", "xls", "application/vnd.ms-excel", "xml", "application/xml", "xpm", "image/x-xpixmap", "xsl", "application/xml", "xslt", "application/xslt+xml", "xul", "application/vnd.mozilla.xul+xml", "xwd", "image/x-xwindowdump", "vsd", "application/vnd.visio", "wav", "audio/x-wav", "wbmp", "image/vnd.wap.wbmp", "wml", "text/vnd.wap.wml", "wmlc", "application/vnd.wap.wmlc", "wmls", "text/vnd.wap.wmlsc", "wmlscriptc", "application/vnd.wap.wmlscriptc", "wmv", "video/x-ms-wmv", "wrl", "model/vrml", "wspolicy", "application/wspolicy+xml", "Z", "application/x-compress", "z", "application/x-compress", "zip", "application/zip" };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected URL getWebappConfigFile(String path, String contextName)
/*      */   {
/* 1248 */     File docBase = new File(path);
/* 1249 */     if (docBase.isDirectory()) {
/* 1250 */       return getWebappConfigFileFromDirectory(docBase, contextName);
/*      */     }
/* 1252 */     return getWebappConfigFileFromJar(docBase, contextName);
/*      */   }
/*      */   
/*      */   private URL getWebappConfigFileFromDirectory(File docBase, String contextName)
/*      */   {
/* 1257 */     URL result = null;
/* 1258 */     File webAppContextXml = new File(docBase, "META-INF/context.xml");
/* 1259 */     if (webAppContextXml.exists()) {
/*      */       try {
/* 1261 */         result = webAppContextXml.toURI().toURL();
/*      */       } catch (MalformedURLException e) {
/* 1263 */         Logger.getLogger(getLoggerName(getHost(), contextName)).log(Level.WARNING, "Unable to determine web application context.xml " + docBase, e);
/*      */       }
/*      */     }
/*      */     
/* 1267 */     return result;
/*      */   }
/*      */   
/*      */   private URL getWebappConfigFileFromJar(File docBase, String contextName) {
/* 1271 */     URL result = null;
/* 1272 */     try { JarFile jar = new JarFile(docBase);Throwable localThrowable3 = null;
/* 1273 */       try { JarEntry entry = jar.getJarEntry("META-INF/context.xml");
/* 1274 */         if (entry != null) {
/* 1275 */           result = UriUtil.buildJarUrl(docBase, "META-INF/context.xml");
/*      */         }
/*      */       }
/*      */       catch (Throwable localThrowable1)
/*      */       {
/* 1272 */         localThrowable3 = localThrowable1;throw localThrowable1;
/*      */ 
/*      */       }
/*      */       finally
/*      */       {
/* 1277 */         if (jar != null) if (localThrowable3 != null) try { jar.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else jar.close();
/* 1278 */       } } catch (IOException e) { Logger.getLogger(getLoggerName(getHost(), contextName)).log(Level.WARNING, "Unable to determine web application context.xml " + docBase, e);
/*      */     }
/*      */     
/* 1281 */     return result;
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\Tomcat.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */