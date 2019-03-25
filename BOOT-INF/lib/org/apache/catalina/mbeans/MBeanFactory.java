/*     */ package org.apache.catalina.mbeans;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.net.URI;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.catalina.Container;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.Engine;
/*     */ import org.apache.catalina.Host;
/*     */ import org.apache.catalina.JmxEnabled;
/*     */ import org.apache.catalina.Pipeline;
/*     */ import org.apache.catalina.Server;
/*     */ import org.apache.catalina.Service;
/*     */ import org.apache.catalina.Valve;
/*     */ import org.apache.catalina.connector.Connector;
/*     */ import org.apache.catalina.core.StandardContext;
/*     */ import org.apache.catalina.core.StandardEngine;
/*     */ import org.apache.catalina.core.StandardHost;
/*     */ import org.apache.catalina.core.StandardService;
/*     */ import org.apache.catalina.loader.WebappLoader;
/*     */ import org.apache.catalina.realm.DataSourceRealm;
/*     */ import org.apache.catalina.realm.JDBCRealm;
/*     */ import org.apache.catalina.realm.JNDIRealm;
/*     */ import org.apache.catalina.realm.MemoryRealm;
/*     */ import org.apache.catalina.realm.UserDatabaseRealm;
/*     */ import org.apache.catalina.session.StandardManager;
/*     */ import org.apache.catalina.startup.ContextConfig;
/*     */ import org.apache.catalina.startup.HostConfig;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
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
/*     */ public class MBeanFactory
/*     */ {
/*  57 */   private static final Log log = LogFactory.getLog(MBeanFactory.class);
/*     */   
/*  59 */   protected static final StringManager sm = StringManager.getManager(MBeanFactory.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  64 */   private static final MBeanServer mserver = MBeanUtils.createServer();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Object container;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setContainer(Object container)
/*     */   {
/*  82 */     this.container = container;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final String getPathStr(String t)
/*     */   {
/*  94 */     if ((t == null) || (t.equals("/"))) {
/*  95 */       return "";
/*     */     }
/*  97 */     return t;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Container getParentContainerFromParent(ObjectName pname)
/*     */     throws Exception
/*     */   {
/* 107 */     String type = pname.getKeyProperty("type");
/* 108 */     String j2eeType = pname.getKeyProperty("j2eeType");
/* 109 */     Service service = getService(pname);
/* 110 */     StandardEngine engine = (StandardEngine)service.getContainer();
/* 111 */     if ((j2eeType != null) && (j2eeType.equals("WebModule"))) {
/* 112 */       String name = pname.getKeyProperty("name");
/* 113 */       name = name.substring(2);
/* 114 */       int i = name.indexOf('/');
/* 115 */       String hostName = name.substring(0, i);
/* 116 */       String path = name.substring(i);
/* 117 */       Container host = engine.findChild(hostName);
/* 118 */       String pathStr = getPathStr(path);
/* 119 */       Container context = host.findChild(pathStr);
/* 120 */       return context; }
/* 121 */     if (type != null) {
/* 122 */       if (type.equals("Engine"))
/* 123 */         return engine;
/* 124 */       if (type.equals("Host")) {
/* 125 */         String hostName = pname.getKeyProperty("host");
/* 126 */         Container host = engine.findChild(hostName);
/* 127 */         return host;
/*     */       }
/*     */     }
/* 130 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Container getParentContainerFromChild(ObjectName oname)
/*     */     throws Exception
/*     */   {
/* 142 */     String hostName = oname.getKeyProperty("host");
/* 143 */     String path = oname.getKeyProperty("path");
/* 144 */     Service service = getService(oname);
/* 145 */     Container engine = service.getContainer();
/* 146 */     if (hostName == null)
/*     */     {
/* 148 */       return engine; }
/* 149 */     if (path == null)
/*     */     {
/* 151 */       Container host = engine.findChild(hostName);
/* 152 */       return host;
/*     */     }
/*     */     
/* 155 */     Container host = engine.findChild(hostName);
/* 156 */     path = getPathStr(path);
/* 157 */     Container context = host.findChild(path);
/* 158 */     return context;
/*     */   }
/*     */   
/*     */ 
/*     */   private Service getService(ObjectName oname)
/*     */     throws Exception
/*     */   {
/* 165 */     if ((this.container instanceof Service))
/*     */     {
/* 167 */       return (Service)this.container;
/*     */     }
/*     */     
/* 170 */     StandardService service = null;
/* 171 */     String domain = oname.getDomain();
/* 172 */     if ((this.container instanceof Server)) {
/* 173 */       Service[] services = ((Server)this.container).findServices();
/* 174 */       for (int i = 0; i < services.length; i++) {
/* 175 */         service = (StandardService)services[i];
/* 176 */         if (domain.equals(service.getObjectName().getDomain())) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/* 181 */     if ((service == null) || 
/* 182 */       (!service.getObjectName().getDomain().equals(domain))) {
/* 183 */       throw new Exception("Service with the domain is not found");
/*     */     }
/* 185 */     return service;
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
/*     */   public String createAjpConnector(String parent, String address, int port)
/*     */     throws Exception
/*     */   {
/* 203 */     return createConnector(parent, address, port, true, false);
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
/*     */   public String createDataSourceRealm(String parent, String dataSourceName, String roleNameCol, String userCredCol, String userNameCol, String userRoleTable, String userTable)
/*     */     throws Exception
/*     */   {
/* 224 */     DataSourceRealm realm = new DataSourceRealm();
/* 225 */     realm.setDataSourceName(dataSourceName);
/* 226 */     realm.setRoleNameCol(roleNameCol);
/* 227 */     realm.setUserCredCol(userCredCol);
/* 228 */     realm.setUserNameCol(userNameCol);
/* 229 */     realm.setUserRoleTable(userRoleTable);
/* 230 */     realm.setUserTable(userTable);
/*     */     
/*     */ 
/* 233 */     ObjectName pname = new ObjectName(parent);
/* 234 */     Container container = getParentContainerFromParent(pname);
/*     */     
/* 236 */     container.setRealm(realm);
/*     */     
/* 238 */     ObjectName oname = realm.getObjectName();
/* 239 */     if (oname != null) {
/* 240 */       return oname.toString();
/*     */     }
/* 242 */     return null;
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
/*     */   public String createHttpConnector(String parent, String address, int port)
/*     */     throws Exception
/*     */   {
/* 259 */     return createConnector(parent, address, port, false, false);
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
/*     */   private String createConnector(String parent, String address, int port, boolean isAjp, boolean isSSL)
/*     */     throws Exception
/*     */   {
/* 276 */     String protocol = isAjp ? "AJP/1.3" : "HTTP/1.1";
/* 277 */     Connector retobj = new Connector(protocol);
/* 278 */     if ((address != null) && (address.length() > 0)) {
/* 279 */       retobj.setProperty("address", address);
/*     */     }
/*     */     
/* 282 */     retobj.setPort(port);
/*     */     
/* 284 */     retobj.setSecure(isSSL);
/* 285 */     retobj.setScheme(isSSL ? "https" : "http");
/*     */     
/*     */ 
/* 288 */     ObjectName pname = new ObjectName(parent);
/* 289 */     Service service = getService(pname);
/* 290 */     service.addConnector(retobj);
/*     */     
/*     */ 
/* 293 */     ObjectName coname = retobj.getObjectName();
/*     */     
/* 295 */     return coname.toString();
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
/*     */   public String createHttpsConnector(String parent, String address, int port)
/*     */     throws Exception
/*     */   {
/* 311 */     return createConnector(parent, address, port, false, true);
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
/*     */   public String createJDBCRealm(String parent, String driverName, String connectionName, String connectionPassword, String connectionURL)
/*     */     throws Exception
/*     */   {
/* 331 */     JDBCRealm realm = new JDBCRealm();
/* 332 */     realm.setDriverName(driverName);
/* 333 */     realm.setConnectionName(connectionName);
/* 334 */     realm.setConnectionPassword(connectionPassword);
/* 335 */     realm.setConnectionURL(connectionURL);
/*     */     
/*     */ 
/* 338 */     ObjectName pname = new ObjectName(parent);
/* 339 */     Container container = getParentContainerFromParent(pname);
/*     */     
/* 341 */     container.setRealm(realm);
/*     */     
/* 343 */     ObjectName oname = realm.getObjectName();
/*     */     
/* 345 */     if (oname != null) {
/* 346 */       return oname.toString();
/*     */     }
/* 348 */     return null;
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
/*     */   public String createJNDIRealm(String parent)
/*     */     throws Exception
/*     */   {
/* 366 */     JNDIRealm realm = new JNDIRealm();
/*     */     
/*     */ 
/* 369 */     ObjectName pname = new ObjectName(parent);
/* 370 */     Container container = getParentContainerFromParent(pname);
/*     */     
/* 372 */     container.setRealm(realm);
/*     */     
/* 374 */     ObjectName oname = realm.getObjectName();
/*     */     
/* 376 */     if (oname != null) {
/* 377 */       return oname.toString();
/*     */     }
/* 379 */     return null;
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
/*     */   public String createMemoryRealm(String parent)
/*     */     throws Exception
/*     */   {
/* 398 */     MemoryRealm realm = new MemoryRealm();
/*     */     
/*     */ 
/* 401 */     ObjectName pname = new ObjectName(parent);
/* 402 */     Container container = getParentContainerFromParent(pname);
/*     */     
/* 404 */     container.setRealm(realm);
/*     */     
/* 406 */     ObjectName oname = realm.getObjectName();
/* 407 */     if (oname != null) {
/* 408 */       return oname.toString();
/*     */     }
/* 410 */     return null;
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
/*     */   public String createStandardContext(String parent, String path, String docBase)
/*     */     throws Exception
/*     */   {
/* 431 */     return createStandardContext(parent, path, docBase, false, false);
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
/*     */ 
/*     */   public String createStandardContext(String parent, String path, String docBase, boolean xmlValidation, boolean xmlNamespaceAware)
/*     */     throws Exception
/*     */   {
/* 455 */     StandardContext context = new StandardContext();
/* 456 */     path = getPathStr(path);
/* 457 */     context.setPath(path);
/* 458 */     context.setDocBase(docBase);
/* 459 */     context.setXmlValidation(xmlValidation);
/* 460 */     context.setXmlNamespaceAware(xmlNamespaceAware);
/*     */     
/* 462 */     ContextConfig contextConfig = new ContextConfig();
/* 463 */     context.addLifecycleListener(contextConfig);
/*     */     
/*     */ 
/* 466 */     ObjectName pname = new ObjectName(parent);
/*     */     
/*     */ 
/* 469 */     ObjectName deployer = new ObjectName(pname.getDomain() + ":type=Deployer,host=" + pname.getKeyProperty("host"));
/* 470 */     if (mserver.isRegistered(deployer)) {
/* 471 */       String contextName = context.getName();
/* 472 */       mserver.invoke(deployer, "addServiced", new Object[] { contextName }, new String[] { "java.lang.String" });
/*     */       
/*     */ 
/* 475 */       String configPath = (String)mserver.getAttribute(deployer, "configBaseName");
/*     */       
/* 477 */       String baseName = context.getBaseName();
/* 478 */       File configFile = new File(new File(configPath), baseName + ".xml");
/* 479 */       if (configFile.isFile()) {
/* 480 */         context.setConfigFile(configFile.toURI().toURL());
/*     */       }
/* 482 */       mserver.invoke(deployer, "manageApp", new Object[] { context }, new String[] { "org.apache.catalina.Context" });
/*     */       
/*     */ 
/* 485 */       mserver.invoke(deployer, "removeServiced", new Object[] { contextName }, new String[] { "java.lang.String" });
/*     */     }
/*     */     else
/*     */     {
/* 489 */       log.warn("Deployer not found for " + pname.getKeyProperty("host"));
/* 490 */       Service service = getService(pname);
/* 491 */       Engine engine = service.getContainer();
/* 492 */       Host host = (Host)engine.findChild(pname.getKeyProperty("host"));
/* 493 */       host.addChild(context);
/*     */     }
/*     */     
/*     */ 
/* 497 */     return context.getObjectName().toString();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String createStandardHost(String parent, String name, String appBase, boolean autoDeploy, boolean deployOnStartup, boolean deployXML, boolean unpackWARs)
/*     */     throws Exception
/*     */   {
/* 525 */     StandardHost host = new StandardHost();
/* 526 */     host.setName(name);
/* 527 */     host.setAppBase(appBase);
/* 528 */     host.setAutoDeploy(autoDeploy);
/* 529 */     host.setDeployOnStartup(deployOnStartup);
/* 530 */     host.setDeployXML(deployXML);
/* 531 */     host.setUnpackWARs(unpackWARs);
/*     */     
/*     */ 
/* 534 */     HostConfig hostConfig = new HostConfig();
/* 535 */     host.addLifecycleListener(hostConfig);
/*     */     
/*     */ 
/* 538 */     ObjectName pname = new ObjectName(parent);
/* 539 */     Service service = getService(pname);
/* 540 */     Engine engine = service.getContainer();
/* 541 */     engine.addChild(host);
/*     */     
/*     */ 
/* 544 */     return host.getObjectName().toString();
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
/*     */   public String createStandardServiceEngine(String domain, String defaultHost, String baseDir)
/*     */     throws Exception
/*     */   {
/* 562 */     if (!(this.container instanceof Server)) {
/* 563 */       throw new Exception("Container not Server");
/*     */     }
/*     */     
/* 566 */     StandardEngine engine = new StandardEngine();
/* 567 */     engine.setDomain(domain);
/* 568 */     engine.setName(domain);
/* 569 */     engine.setDefaultHost(defaultHost);
/*     */     
/* 571 */     Service service = new StandardService();
/* 572 */     service.setContainer(engine);
/* 573 */     service.setName(domain);
/*     */     
/* 575 */     ((Server)this.container).addService(service);
/*     */     
/* 577 */     return engine.getObjectName().toString();
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
/*     */   public String createStandardManager(String parent)
/*     */     throws Exception
/*     */   {
/* 593 */     StandardManager manager = new StandardManager();
/*     */     
/*     */ 
/* 596 */     ObjectName pname = new ObjectName(parent);
/* 597 */     Container container = getParentContainerFromParent(pname);
/* 598 */     if ((container instanceof Context)) {
/* 599 */       ((Context)container).setManager(manager);
/*     */     } else {
/* 601 */       throw new Exception(sm.getString("mBeanFactory.managerContext"));
/*     */     }
/* 603 */     ObjectName oname = manager.getObjectName();
/* 604 */     if (oname != null) {
/* 605 */       return oname.toString();
/*     */     }
/* 607 */     return null;
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
/*     */   public String createUserDatabaseRealm(String parent, String resourceName)
/*     */     throws Exception
/*     */   {
/* 627 */     UserDatabaseRealm realm = new UserDatabaseRealm();
/* 628 */     realm.setResourceName(resourceName);
/*     */     
/*     */ 
/* 631 */     ObjectName pname = new ObjectName(parent);
/* 632 */     Container container = getParentContainerFromParent(pname);
/*     */     
/* 634 */     container.setRealm(realm);
/*     */     
/* 636 */     ObjectName oname = realm.getObjectName();
/*     */     
/*     */ 
/*     */ 
/* 640 */     if (oname != null) {
/* 641 */       return oname.toString();
/*     */     }
/* 643 */     return null;
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
/*     */   public String createValve(String className, String parent)
/*     */     throws Exception
/*     */   {
/* 666 */     ObjectName parentName = new ObjectName(parent);
/* 667 */     Container container = getParentContainerFromParent(parentName);
/*     */     
/* 669 */     if (container == null)
/*     */     {
/* 671 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/* 674 */     Valve valve = (Valve)Class.forName(className).getConstructor(new Class[0]).newInstance(new Object[0]);
/*     */     
/* 676 */     container.getPipeline().addValve(valve);
/*     */     
/* 678 */     if ((valve instanceof JmxEnabled)) {
/* 679 */       return ((JmxEnabled)valve).getObjectName().toString();
/*     */     }
/* 681 */     return null;
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
/*     */   public String createWebappLoader(String parent)
/*     */     throws Exception
/*     */   {
/* 698 */     WebappLoader loader = new WebappLoader();
/*     */     
/*     */ 
/* 701 */     ObjectName pname = new ObjectName(parent);
/* 702 */     Container container = getParentContainerFromParent(pname);
/* 703 */     if ((container instanceof Context)) {
/* 704 */       ((Context)container).setLoader(loader);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 709 */     ObjectName oname = MBeanUtils.createObjectName(pname.getDomain(), loader);
/* 710 */     return oname.toString();
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
/*     */   public void removeConnector(String name)
/*     */     throws Exception
/*     */   {
/* 725 */     ObjectName oname = new ObjectName(name);
/* 726 */     Service service = getService(oname);
/* 727 */     String port = oname.getKeyProperty("port");
/*     */     
/*     */ 
/* 730 */     Connector[] conns = service.findConnectors();
/*     */     
/* 732 */     for (int i = 0; i < conns.length; i++) {
/* 733 */       String connAddress = String.valueOf(conns[i].getProperty("address"));
/* 734 */       String connPort = "" + conns[i].getPort();
/*     */       
/*     */ 
/* 737 */       if ((connAddress == null) && (port.equals(connPort))) {
/* 738 */         service.removeConnector(conns[i]);
/* 739 */         conns[i].destroy();
/* 740 */         break;
/*     */       }
/*     */       
/* 743 */       if (port.equals(connPort))
/*     */       {
/* 745 */         service.removeConnector(conns[i]);
/* 746 */         conns[i].destroy();
/* 747 */         break;
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
/*     */   public void removeContext(String contextName)
/*     */     throws Exception
/*     */   {
/* 764 */     ObjectName oname = new ObjectName(contextName);
/* 765 */     String domain = oname.getDomain();
/* 766 */     StandardService service = (StandardService)getService(oname);
/*     */     
/* 768 */     Engine engine = service.getContainer();
/* 769 */     String name = oname.getKeyProperty("name");
/* 770 */     name = name.substring(2);
/* 771 */     int i = name.indexOf('/');
/* 772 */     String hostName = name.substring(0, i);
/* 773 */     String path = name.substring(i);
/* 774 */     ObjectName deployer = new ObjectName(domain + ":type=Deployer,host=" + hostName);
/*     */     
/* 776 */     String pathStr = getPathStr(path);
/* 777 */     if (mserver.isRegistered(deployer)) {
/* 778 */       mserver.invoke(deployer, "addServiced", new Object[] { pathStr }, new String[] { "java.lang.String" });
/*     */       
/*     */ 
/* 781 */       mserver.invoke(deployer, "unmanageApp", new Object[] { pathStr }, new String[] { "java.lang.String" });
/*     */       
/*     */ 
/* 784 */       mserver.invoke(deployer, "removeServiced", new Object[] { pathStr }, new String[] { "java.lang.String" });
/*     */     }
/*     */     else
/*     */     {
/* 788 */       log.warn("Deployer not found for " + hostName);
/* 789 */       Host host = (Host)engine.findChild(hostName);
/* 790 */       Context context = (Context)host.findChild(pathStr);
/*     */       
/* 792 */       host.removeChild(context);
/* 793 */       if ((context instanceof StandardContext)) {
/*     */         try {
/* 795 */           ((StandardContext)context).destroy();
/*     */         } catch (Exception e) {
/* 797 */           log.warn("Error during context [" + context.getName() + "] destroy ", e);
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
/*     */ 
/*     */   public void removeHost(String name)
/*     */     throws Exception
/*     */   {
/* 815 */     ObjectName oname = new ObjectName(name);
/* 816 */     String hostName = oname.getKeyProperty("host");
/* 817 */     Service service = getService(oname);
/* 818 */     Engine engine = service.getContainer();
/* 819 */     Host host = (Host)engine.findChild(hostName);
/*     */     
/*     */ 
/* 822 */     if (host != null) {
/* 823 */       engine.removeChild(host);
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
/*     */   public void removeLoader(String name)
/*     */     throws Exception
/*     */   {
/* 837 */     ObjectName oname = new ObjectName(name);
/*     */     
/* 839 */     Container container = getParentContainerFromChild(oname);
/* 840 */     if ((container instanceof Context)) {
/* 841 */       ((Context)container).setLoader(null);
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
/*     */   public void removeManager(String name)
/*     */     throws Exception
/*     */   {
/* 855 */     ObjectName oname = new ObjectName(name);
/*     */     
/* 857 */     Container container = getParentContainerFromChild(oname);
/* 858 */     if ((container instanceof Context)) {
/* 859 */       ((Context)container).setManager(null);
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
/*     */   public void removeRealm(String name)
/*     */     throws Exception
/*     */   {
/* 873 */     ObjectName oname = new ObjectName(name);
/*     */     
/* 875 */     Container container = getParentContainerFromChild(oname);
/* 876 */     container.setRealm(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeService(String name)
/*     */     throws Exception
/*     */   {
/* 889 */     if (!(this.container instanceof Server)) {
/* 890 */       throw new Exception();
/*     */     }
/*     */     
/*     */ 
/* 894 */     ObjectName oname = new ObjectName(name);
/* 895 */     Service service = getService(oname);
/* 896 */     ((Server)this.container).removeService(service);
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
/*     */   public void removeValve(String name)
/*     */     throws Exception
/*     */   {
/* 910 */     ObjectName oname = new ObjectName(name);
/* 911 */     Container container = getParentContainerFromChild(oname);
/* 912 */     Valve[] valves = container.getPipeline().getValves();
/* 913 */     for (int i = 0; i < valves.length; i++) {
/* 914 */       ObjectName voname = ((JmxEnabled)valves[i]).getObjectName();
/* 915 */       if (voname.equals(oname)) {
/* 916 */         container.getPipeline().removeValve(valves[i]);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\mbeans\MBeanFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */