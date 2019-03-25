/*      */ package org.apache.catalina.manager;
/*      */ 
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintWriter;
/*      */ import java.security.cert.Certificate;
/*      */ import java.security.cert.X509Certificate;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import javax.management.MBeanServer;
/*      */ import javax.management.MalformedObjectNameException;
/*      */ import javax.management.ObjectName;
/*      */ import javax.naming.Binding;
/*      */ import javax.naming.NamingEnumeration;
/*      */ import javax.servlet.ServletConfig;
/*      */ import javax.servlet.ServletContext;
/*      */ import javax.servlet.ServletException;
/*      */ import javax.servlet.ServletInputStream;
/*      */ import javax.servlet.UnavailableException;
/*      */ import javax.servlet.http.HttpServlet;
/*      */ import javax.servlet.http.HttpServletRequest;
/*      */ import javax.servlet.http.HttpServletResponse;
/*      */ import org.apache.catalina.Container;
/*      */ import org.apache.catalina.ContainerServlet;
/*      */ import org.apache.catalina.Engine;
/*      */ import org.apache.catalina.Host;
/*      */ import org.apache.catalina.LifecycleState;
/*      */ import org.apache.catalina.Manager;
/*      */ import org.apache.catalina.Server;
/*      */ import org.apache.catalina.Service;
/*      */ import org.apache.catalina.Session;
/*      */ import org.apache.catalina.Wrapper;
/*      */ import org.apache.catalina.connector.Connector;
/*      */ import org.apache.catalina.core.StandardHost;
/*      */ import org.apache.catalina.startup.ExpandWar;
/*      */ import org.apache.catalina.util.ContextName;
/*      */ import org.apache.catalina.util.ServerInfo;
/*      */ import org.apache.coyote.ProtocolHandler;
/*      */ import org.apache.tomcat.util.Diagnostics;
/*      */ import org.apache.tomcat.util.ExceptionUtils;
/*      */ import org.apache.tomcat.util.modeler.Registry;
/*      */ import org.apache.tomcat.util.net.SSLContext;
/*      */ import org.apache.tomcat.util.net.SSLHostConfig;
/*      */ import org.apache.tomcat.util.net.SSLHostConfigCertificate;
/*      */ import org.apache.tomcat.util.res.StringManager;
/*      */ import org.apache.tomcat.util.security.Escape;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ManagerServlet
/*      */   extends HttpServlet
/*      */   implements ContainerServlet
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*  174 */   protected File configBase = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  180 */   protected transient org.apache.catalina.Context context = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  186 */   protected int debug = 1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  192 */   protected File versioned = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  198 */   protected transient Host host = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  204 */   protected transient MBeanServer mBeanServer = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  210 */   protected ObjectName oname = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  217 */   protected transient javax.naming.Context global = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  224 */   protected static final StringManager sm = StringManager.getManager("org.apache.catalina.manager");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  230 */   protected transient Wrapper wrapper = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Wrapper getWrapper()
/*      */   {
/*  242 */     return this.wrapper;
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
/*      */   public void setWrapper(Wrapper wrapper)
/*      */   {
/*  255 */     this.wrapper = wrapper;
/*  256 */     if (wrapper == null) {
/*  257 */       this.context = null;
/*  258 */       this.host = null;
/*  259 */       this.oname = null;
/*      */     } else {
/*  261 */       this.context = ((org.apache.catalina.Context)wrapper.getParent());
/*  262 */       this.host = ((Host)this.context.getParent());
/*  263 */       Engine engine = (Engine)this.host.getParent();
/*      */       
/*  265 */       String name = engine.getName() + ":type=Deployer,host=" + this.host.getName();
/*      */       try {
/*  267 */         this.oname = new ObjectName(name);
/*      */       } catch (Exception e) {
/*  269 */         log(sm.getString("managerServlet.objectNameFail", new Object[] { name }), e);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  274 */     this.mBeanServer = Registry.getRegistry(null, null).getMBeanServer();
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
/*      */   public void destroy() {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void doGet(HttpServletRequest request, HttpServletResponse response)
/*      */     throws IOException, ServletException
/*      */   {
/*  307 */     StringManager smClient = StringManager.getManager("org.apache.catalina.manager", request
/*  308 */       .getLocales());
/*      */     
/*      */ 
/*  311 */     String command = request.getPathInfo();
/*  312 */     if (command == null)
/*  313 */       command = request.getServletPath();
/*  314 */     String config = request.getParameter("config");
/*  315 */     String path = request.getParameter("path");
/*  316 */     ContextName cn = null;
/*  317 */     if (path != null) {
/*  318 */       cn = new ContextName(path, request.getParameter("version"));
/*      */     }
/*  320 */     String type = request.getParameter("type");
/*  321 */     String war = request.getParameter("war");
/*  322 */     String tag = request.getParameter("tag");
/*  323 */     boolean update = false;
/*  324 */     if ((request.getParameter("update") != null) && 
/*  325 */       (request.getParameter("update").equals("true"))) {
/*  326 */       update = true;
/*      */     }
/*      */     
/*  329 */     boolean statusLine = false;
/*  330 */     if ("true".equals(request.getParameter("statusLine"))) {
/*  331 */       statusLine = true;
/*      */     }
/*      */     
/*      */ 
/*  335 */     response.setContentType("text/plain; charset=utf-8");
/*  336 */     PrintWriter writer = response.getWriter();
/*      */     
/*      */ 
/*  339 */     if (command == null) {
/*  340 */       writer.println(smClient.getString("managerServlet.noCommand"));
/*  341 */     } else if (command.equals("/deploy")) {
/*  342 */       if ((war != null) || (config != null)) {
/*  343 */         deploy(writer, config, cn, war, update, smClient);
/*  344 */       } else if (tag != null) {
/*  345 */         deploy(writer, cn, tag, smClient);
/*      */       } else {
/*  347 */         writer.println(smClient.getString("managerServlet.invalidCommand", new Object[] { command }));
/*      */       }
/*      */     }
/*  350 */     else if (command.equals("/list")) {
/*  351 */       list(writer, smClient);
/*  352 */     } else if (command.equals("/reload")) {
/*  353 */       reload(writer, cn, smClient);
/*  354 */     } else if (command.equals("/resources")) {
/*  355 */       resources(writer, type, smClient);
/*  356 */     } else if (command.equals("/save")) {
/*  357 */       save(writer, path, smClient);
/*  358 */     } else if (command.equals("/serverinfo")) {
/*  359 */       serverinfo(writer, smClient);
/*  360 */     } else if (command.equals("/sessions")) {
/*  361 */       expireSessions(writer, cn, request, smClient);
/*  362 */     } else if (command.equals("/expire")) {
/*  363 */       expireSessions(writer, cn, request, smClient);
/*  364 */     } else if (command.equals("/start")) {
/*  365 */       start(writer, cn, smClient);
/*  366 */     } else if (command.equals("/stop")) {
/*  367 */       stop(writer, cn, smClient);
/*  368 */     } else if (command.equals("/undeploy")) {
/*  369 */       undeploy(writer, cn, smClient);
/*  370 */     } else if (command.equals("/findleaks")) {
/*  371 */       findleaks(statusLine, writer, smClient);
/*  372 */     } else if (command.equals("/vminfo")) {
/*  373 */       vmInfo(writer, smClient, request.getLocales());
/*  374 */     } else if (command.equals("/threaddump")) {
/*  375 */       threadDump(writer, smClient, request.getLocales());
/*  376 */     } else if (command.equals("/sslConnectorCiphers")) {
/*  377 */       sslConnectorCiphers(writer, smClient);
/*  378 */     } else if (command.equals("/sslConnectorCerts")) {
/*  379 */       sslConnectorCerts(writer, smClient);
/*  380 */     } else if (command.equals("/sslConnectorTrustedCerts")) {
/*  381 */       sslConnectorTrustedCerts(writer, smClient);
/*      */     } else {
/*  383 */       writer.println(smClient.getString("managerServlet.unknownCommand", new Object[] { command }));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  388 */     writer.flush();
/*  389 */     writer.close();
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
/*      */   public void doPut(HttpServletRequest request, HttpServletResponse response)
/*      */     throws IOException, ServletException
/*      */   {
/*  408 */     StringManager smClient = StringManager.getManager("org.apache.catalina.manager", request
/*  409 */       .getLocales());
/*      */     
/*      */ 
/*  412 */     String command = request.getPathInfo();
/*  413 */     if (command == null)
/*  414 */       command = request.getServletPath();
/*  415 */     String path = request.getParameter("path");
/*  416 */     ContextName cn = null;
/*  417 */     if (path != null) {
/*  418 */       cn = new ContextName(path, request.getParameter("version"));
/*      */     }
/*  420 */     String tag = request.getParameter("tag");
/*  421 */     boolean update = false;
/*  422 */     if ((request.getParameter("update") != null) && 
/*  423 */       (request.getParameter("update").equals("true"))) {
/*  424 */       update = true;
/*      */     }
/*      */     
/*      */ 
/*  428 */     response.setContentType("text/plain;charset=utf-8");
/*  429 */     PrintWriter writer = response.getWriter();
/*      */     
/*      */ 
/*  432 */     if (command == null) {
/*  433 */       writer.println(smClient.getString("managerServlet.noCommand"));
/*  434 */     } else if (command.equals("/deploy")) {
/*  435 */       deploy(writer, cn, tag, update, request, smClient);
/*      */     } else {
/*  437 */       writer.println(smClient.getString("managerServlet.unknownCommand", new Object[] { command }));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  442 */     writer.flush();
/*  443 */     writer.close();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void init()
/*      */     throws ServletException
/*      */   {
/*  455 */     if ((this.wrapper == null) || (this.context == null))
/*      */     {
/*  457 */       throw new UnavailableException(sm.getString("managerServlet.noWrapper"));
/*      */     }
/*      */     
/*  460 */     String value = null;
/*      */     try {
/*  462 */       value = getServletConfig().getInitParameter("debug");
/*  463 */       this.debug = Integer.parseInt(value);
/*      */     } catch (Throwable t) {
/*  465 */       ExceptionUtils.handleThrowable(t);
/*      */     }
/*      */     
/*      */ 
/*  469 */     Server server = ((Engine)this.host.getParent()).getService().getServer();
/*  470 */     if (server != null) {
/*  471 */       this.global = server.getGlobalNamingContext();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  476 */     this.versioned = ((File)getServletContext().getAttribute("javax.servlet.context.tempdir"));
/*      */     
/*  478 */     this.configBase = new File(this.context.getCatalinaBase(), "conf");
/*  479 */     Container container = this.context;
/*  480 */     Container host = null;
/*  481 */     Container engine = null;
/*  482 */     while (container != null) {
/*  483 */       if ((container instanceof Host))
/*  484 */         host = container;
/*  485 */       if ((container instanceof Engine))
/*  486 */         engine = container;
/*  487 */       container = container.getParent();
/*      */     }
/*  489 */     if (engine != null) {
/*  490 */       this.configBase = new File(this.configBase, engine.getName());
/*      */     }
/*  492 */     if (host != null) {
/*  493 */       this.configBase = new File(this.configBase, host.getName());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  498 */     if (this.debug >= 1) {
/*  499 */       log("init: Associated with Deployer '" + this.oname + "'");
/*      */       
/*  501 */       if (this.global != null) {
/*  502 */         log("init: Global resources are available");
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void findleaks(boolean statusLine, PrintWriter writer, StringManager smClient)
/*      */   {
/*  523 */     if (!(this.host instanceof StandardHost)) {
/*  524 */       writer.println(smClient.getString("managerServlet.findleaksFail"));
/*  525 */       return;
/*      */     }
/*      */     
/*      */ 
/*  529 */     String[] results = ((StandardHost)this.host).findReloadedContextMemoryLeaks();
/*      */     
/*  531 */     if (results.length > 0) {
/*  532 */       if (statusLine) {
/*  533 */         writer.println(smClient
/*  534 */           .getString("managerServlet.findleaksList"));
/*      */       }
/*  536 */       for (String result : results) {
/*  537 */         if ("".equals(result)) {
/*  538 */           result = "/";
/*      */         }
/*  540 */         writer.println(result);
/*      */       }
/*  542 */     } else if (statusLine) {
/*  543 */       writer.println(smClient.getString("managerServlet.findleaksNone"));
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
/*      */   protected void vmInfo(PrintWriter writer, StringManager smClient, Enumeration<Locale> requestedLocales)
/*      */   {
/*  557 */     writer.println(smClient.getString("managerServlet.vminfo"));
/*  558 */     writer.print(Diagnostics.getVMInfo(requestedLocales));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void threadDump(PrintWriter writer, StringManager smClient, Enumeration<Locale> requestedLocales)
/*      */   {
/*  570 */     writer.println(smClient.getString("managerServlet.threaddump"));
/*  571 */     writer.print(Diagnostics.getThreadDump(requestedLocales));
/*      */   }
/*      */   
/*      */   protected void sslConnectorCiphers(PrintWriter writer, StringManager smClient)
/*      */   {
/*  576 */     writer.println(smClient.getString("managerServlet.sslConnectorCiphers"));
/*  577 */     Map<String, List<String>> connectorCiphers = getConnectorCiphers();
/*  578 */     for (Map.Entry<String, List<String>> entry : connectorCiphers.entrySet()) {
/*  579 */       writer.println((String)entry.getKey());
/*  580 */       for (String cipher : (List)entry.getValue()) {
/*  581 */         writer.print("  ");
/*  582 */         writer.println(cipher);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void sslConnectorCerts(PrintWriter writer, StringManager smClient)
/*      */   {
/*  589 */     writer.println(smClient.getString("managerServlet.sslConnectorCerts"));
/*  590 */     Map<String, List<String>> connectorCerts = getConnectorCerts();
/*  591 */     for (Map.Entry<String, List<String>> entry : connectorCerts.entrySet()) {
/*  592 */       writer.println((String)entry.getKey());
/*  593 */       for (String cert : (List)entry.getValue()) {
/*  594 */         writer.println(cert);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void sslConnectorTrustedCerts(PrintWriter writer, StringManager smClient)
/*      */   {
/*  601 */     writer.println(smClient.getString("managerServlet.sslConnectorTrustedCerts"));
/*  602 */     Map<String, List<String>> connectorTrustedCerts = getConnectorTrustedCerts();
/*  603 */     for (Map.Entry<String, List<String>> entry : connectorTrustedCerts.entrySet()) {
/*  604 */       writer.println((String)entry.getKey());
/*  605 */       for (String cert : (List)entry.getValue()) {
/*  606 */         writer.println(cert);
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
/*      */   protected synchronized void save(PrintWriter writer, String path, StringManager smClient)
/*      */   {
/*      */     try
/*      */     {
/*  624 */       storeConfigOname = new ObjectName("Catalina:type=StoreConfig");
/*      */     } catch (MalformedObjectNameException e) {
/*      */       ObjectName storeConfigOname;
/*  627 */       log(sm.getString("managerServlet.exception"), e);
/*  628 */       writer.println(smClient.getString("managerServlet.exception", new Object[] { e.toString() })); return;
/*      */     }
/*      */     
/*      */     ObjectName storeConfigOname;
/*  632 */     if (!this.mBeanServer.isRegistered(storeConfigOname)) {
/*  633 */       writer.println(smClient.getString("managerServlet.storeConfig.noMBean", new Object[] { storeConfigOname }));
/*      */       
/*  635 */       return;
/*      */     }
/*      */     
/*  638 */     if ((path == null) || (path.length() == 0) || (!path.startsWith("/"))) {
/*      */       try {
/*  640 */         this.mBeanServer.invoke(storeConfigOname, "storeConfig", null, null);
/*  641 */         writer.println(smClient.getString("managerServlet.saved"));
/*      */       } catch (Exception e) {
/*  643 */         log("managerServlet.storeConfig", e);
/*  644 */         writer.println(smClient.getString("managerServlet.exception", new Object[] {e
/*  645 */           .toString() }));
/*  646 */         return;
/*      */       }
/*      */     } else {
/*  649 */       String contextPath = path;
/*  650 */       if (path.equals("/")) {
/*  651 */         contextPath = "";
/*      */       }
/*  653 */       org.apache.catalina.Context context = (org.apache.catalina.Context)this.host.findChild(contextPath);
/*  654 */       if (context == null) {
/*  655 */         writer.println(smClient.getString("managerServlet.noContext", new Object[] { path }));
/*      */         
/*  657 */         return;
/*      */       }
/*      */       try {
/*  660 */         this.mBeanServer.invoke(storeConfigOname, "store", new Object[] { context }, new String[] { "java.lang.String" });
/*      */         
/*      */ 
/*  663 */         writer.println(smClient.getString("managerServlet.savedContext", new Object[] { path }));
/*      */       }
/*      */       catch (Exception e) {
/*  666 */         log("managerServlet.save[" + path + "]", e);
/*  667 */         writer.println(smClient.getString("managerServlet.exception", new Object[] {e
/*  668 */           .toString() }));
/*  669 */         return;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected synchronized void deploy(PrintWriter writer, ContextName cn, String tag, boolean update, HttpServletRequest request, StringManager smClient)
/*      */   {
/*  692 */     if (this.debug >= 1) {
/*  693 */       log("deploy: Deploying web application '" + cn + "'");
/*      */     }
/*      */     
/*      */ 
/*  697 */     if (!validateContextName(cn, writer, smClient)) {
/*  698 */       return;
/*      */     }
/*  700 */     String name = cn.getName();
/*  701 */     String baseName = cn.getBaseName();
/*  702 */     String displayPath = cn.getDisplayName();
/*      */     
/*      */ 
/*      */ 
/*  706 */     org.apache.catalina.Context context = (org.apache.catalina.Context)this.host.findChild(name);
/*  707 */     if ((context != null) && (!update)) {
/*  708 */       writer.println(smClient.getString("managerServlet.alreadyContext", new Object[] { displayPath }));
/*      */       
/*  710 */       return;
/*      */     }
/*      */     
/*  713 */     File deployedWar = new File(this.host.getAppBaseFile(), baseName + ".war");
/*      */     
/*      */     File uploadedWar;
/*      */     File uploadedWar;
/*  717 */     if (tag == null) {
/*  718 */       if (update)
/*      */       {
/*      */ 
/*      */ 
/*  722 */         File uploadedWar = new File(deployedWar.getAbsolutePath() + ".tmp");
/*  723 */         if ((uploadedWar.exists()) && (!uploadedWar.delete())) {
/*  724 */           writer.println(smClient.getString("managerServlet.deleteFail", new Object[] { uploadedWar }));
/*      */         }
/*      */       }
/*      */       else {
/*  728 */         uploadedWar = deployedWar;
/*      */       }
/*      */     } else {
/*  731 */       File uploadPath = new File(this.versioned, tag);
/*  732 */       if ((!uploadPath.mkdirs()) && (!uploadPath.isDirectory())) {
/*  733 */         writer.println(smClient.getString("managerServlet.mkdirFail", new Object[] { uploadPath }));
/*      */         
/*  735 */         return;
/*      */       }
/*  737 */       uploadedWar = new File(uploadPath, baseName + ".war");
/*      */     }
/*  739 */     if (this.debug >= 2) {
/*  740 */       log("Uploading WAR file to " + uploadedWar);
/*      */     }
/*      */     try
/*      */     {
/*  744 */       if (isServiced(name)) {
/*  745 */         writer.println(smClient.getString("managerServlet.inService", new Object[] { displayPath }));
/*      */       } else {
/*  747 */         addServiced(name);
/*      */         try
/*      */         {
/*  750 */           uploadWar(writer, request, uploadedWar, smClient);
/*  751 */           if ((update) && (tag == null)) {
/*  752 */             if ((deployedWar.exists()) && (!deployedWar.delete())) {
/*  753 */               writer.println(smClient.getString("managerServlet.deleteFail", new Object[] { deployedWar }));
/*      */               
/*  755 */               return;
/*      */             }
/*      */             
/*  758 */             uploadedWar.renameTo(deployedWar);
/*      */           }
/*  760 */           if (tag != null)
/*      */           {
/*  762 */             copy(uploadedWar, deployedWar);
/*      */           }
/*      */           
/*  765 */           check(name);
/*      */         } finally {
/*  767 */           removeServiced(name);
/*      */         }
/*      */       }
/*      */     } catch (Exception e) {
/*  771 */       log("managerServlet.check[" + displayPath + "]", e);
/*  772 */       writer.println(smClient.getString("managerServlet.exception", new Object[] {e
/*  773 */         .toString() }));
/*  774 */       return;
/*      */     }
/*      */     
/*  777 */     writeDeployResult(writer, smClient, name, displayPath);
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
/*      */   protected void deploy(PrintWriter writer, ContextName cn, String tag, StringManager smClient)
/*      */   {
/*  796 */     if (!validateContextName(cn, writer, smClient)) {
/*  797 */       return;
/*      */     }
/*      */     
/*  800 */     String baseName = cn.getBaseName();
/*  801 */     String name = cn.getName();
/*  802 */     String displayPath = cn.getDisplayName();
/*      */     
/*      */ 
/*  805 */     File localWar = new File(new File(this.versioned, tag), baseName + ".war");
/*      */     
/*  807 */     File deployedWar = new File(this.host.getAppBaseFile(), baseName + ".war");
/*      */     
/*      */     try
/*      */     {
/*  811 */       if (isServiced(name)) {
/*  812 */         writer.println(smClient.getString("managerServlet.inService", new Object[] { displayPath }));
/*      */       } else {
/*  814 */         addServiced(name);
/*      */         try {
/*  816 */           if (!deployedWar.delete()) {
/*  817 */             writer.println(smClient.getString("managerServlet.deleteFail", new Object[] { deployedWar }));
/*      */             
/*  819 */             return;
/*      */           }
/*  821 */           copy(localWar, deployedWar);
/*      */           
/*  823 */           check(name);
/*      */         } finally {
/*  825 */           removeServiced(name);
/*      */         }
/*      */       }
/*      */     } catch (Exception e) {
/*  829 */       log("managerServlet.check[" + displayPath + "]", e);
/*  830 */       writer.println(smClient.getString("managerServlet.exception", new Object[] {e
/*  831 */         .toString() }));
/*  832 */       return;
/*      */     }
/*      */     
/*  835 */     writeDeployResult(writer, smClient, name, displayPath);
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
/*      */   protected void deploy(PrintWriter writer, String config, ContextName cn, String war, boolean update, StringManager smClient)
/*      */   {
/*  853 */     if ((config != null) && (config.length() == 0)) {
/*  854 */       config = null;
/*      */     }
/*  856 */     if ((war != null) && (war.length() == 0)) {
/*  857 */       war = null;
/*      */     }
/*      */     
/*  860 */     if (this.debug >= 1) {
/*  861 */       if ((config != null) && (config.length() > 0)) {
/*  862 */         if (war != null) {
/*  863 */           log("install: Installing context configuration at '" + config + "' from '" + war + "'");
/*      */         }
/*      */         else {
/*  866 */           log("install: Installing context configuration at '" + config + "'");
/*      */         }
/*      */         
/*      */       }
/*  870 */       else if (cn != null) {
/*  871 */         log("install: Installing web application '" + cn + "' from '" + war + "'");
/*      */       }
/*      */       else {
/*  874 */         log("install: Installing web application from '" + war + "'");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  879 */     if (!validateContextName(cn, writer, smClient)) {
/*  880 */       return;
/*      */     }
/*      */     
/*  883 */     String name = cn.getName();
/*  884 */     String baseName = cn.getBaseName();
/*  885 */     String displayPath = cn.getDisplayName();
/*      */     
/*      */ 
/*      */ 
/*  889 */     org.apache.catalina.Context context = (org.apache.catalina.Context)this.host.findChild(name);
/*  890 */     if ((context != null) && (!update)) {
/*  891 */       writer.println(smClient.getString("managerServlet.alreadyContext", new Object[] { displayPath }));
/*      */       
/*  893 */       return;
/*      */     }
/*      */     
/*  896 */     if ((config != null) && (config.startsWith("file:"))) {
/*  897 */       config = config.substring("file:".length());
/*      */     }
/*  899 */     if ((war != null) && (war.startsWith("file:"))) {
/*  900 */       war = war.substring("file:".length());
/*      */     }
/*      */     try
/*      */     {
/*  904 */       if (isServiced(name)) {
/*  905 */         writer.println(smClient.getString("managerServlet.inService", new Object[] { displayPath }));
/*      */       } else {
/*  907 */         addServiced(name);
/*      */         try {
/*  909 */           if (config != null) {
/*  910 */             if ((!this.configBase.mkdirs()) && (!this.configBase.isDirectory())) {
/*  911 */               writer.println(smClient.getString("managerServlet.mkdirFail", new Object[] { this.configBase }));
/*      */               
/*  913 */               return;
/*      */             }
/*  915 */             File localConfig = new File(this.configBase, baseName + ".xml");
/*  916 */             if ((localConfig.isFile()) && (!localConfig.delete())) {
/*  917 */               writer.println(smClient.getString("managerServlet.deleteFail", new Object[] { localConfig }));
/*      */               
/*  919 */               return;
/*      */             }
/*  921 */             copy(new File(config), localConfig);
/*      */           }
/*  923 */           if (war != null) { File localWar;
/*      */             File localWar;
/*  925 */             if (war.endsWith(".war")) {
/*  926 */               localWar = new File(this.host.getAppBaseFile(), baseName + ".war");
/*      */             } else {
/*  928 */               localWar = new File(this.host.getAppBaseFile(), baseName);
/*      */             }
/*  930 */             if ((localWar.exists()) && (!ExpandWar.delete(localWar))) {
/*  931 */               writer.println(smClient.getString("managerServlet.deleteFail", new Object[] { localWar }));
/*      */               
/*  933 */               return;
/*      */             }
/*  935 */             copy(new File(war), localWar);
/*      */           }
/*      */           
/*  938 */           check(name);
/*      */         } finally {
/*  940 */           removeServiced(name);
/*      */         }
/*      */       }
/*  943 */       writeDeployResult(writer, smClient, name, displayPath);
/*      */     } catch (Throwable t) {
/*  945 */       ExceptionUtils.handleThrowable(t);
/*  946 */       log("ManagerServlet.install[" + displayPath + "]", t);
/*  947 */       writer.println(smClient.getString("managerServlet.exception", new Object[] {t
/*  948 */         .toString() }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void writeDeployResult(PrintWriter writer, StringManager smClient, String name, String displayPath)
/*      */   {
/*  956 */     org.apache.catalina.Context deployed = (org.apache.catalina.Context)this.host.findChild(name);
/*  957 */     if ((deployed != null) && (deployed.getConfigured()) && 
/*  958 */       (deployed.getState().isAvailable())) {
/*  959 */       writer.println(smClient.getString("managerServlet.deployed", new Object[] { displayPath }));
/*      */     }
/*  961 */     else if ((deployed != null) && (!deployed.getState().isAvailable())) {
/*  962 */       writer.println(smClient.getString("managerServlet.deployedButNotStarted", new Object[] { displayPath }));
/*      */     }
/*      */     else
/*      */     {
/*  966 */       writer.println(smClient.getString("managerServlet.deployFailed", new Object[] { displayPath }));
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
/*      */   protected void list(PrintWriter writer, StringManager smClient)
/*      */   {
/*  980 */     if (this.debug >= 1) {
/*  981 */       log("list: Listing contexts for virtual host '" + this.host
/*  982 */         .getName() + "'");
/*      */     }
/*  984 */     writer.println(smClient.getString("managerServlet.listed", new Object[] {this.host
/*  985 */       .getName() }));
/*  986 */     Container[] contexts = this.host.findChildren();
/*  987 */     for (int i = 0; i < contexts.length; i++) {
/*  988 */       org.apache.catalina.Context context = (org.apache.catalina.Context)contexts[i];
/*  989 */       if (context != null) {
/*  990 */         String displayPath = context.getPath();
/*  991 */         if (displayPath.equals(""))
/*  992 */           displayPath = "/";
/*  993 */         if (context.getState().isAvailable()) {
/*  994 */           writer.println(smClient.getString("managerServlet.listitem", new Object[] { displayPath, "running", "" + context
/*      */           
/*      */ 
/*  997 */             .getManager().findSessions().length, context
/*  998 */             .getDocBase() }));
/*      */         } else {
/* 1000 */           writer.println(smClient.getString("managerServlet.listitem", new Object[] { displayPath, "stopped", "0", context
/*      */           
/*      */ 
/*      */ 
/* 1004 */             .getDocBase() }));
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
/*      */   protected void reload(PrintWriter writer, ContextName cn, StringManager smClient)
/*      */   {
/* 1021 */     if (this.debug >= 1) {
/* 1022 */       log("restart: Reloading web application '" + cn + "'");
/*      */     }
/* 1024 */     if (!validateContextName(cn, writer, smClient)) {
/* 1025 */       return;
/*      */     }
/*      */     try
/*      */     {
/* 1029 */       org.apache.catalina.Context context = (org.apache.catalina.Context)this.host.findChild(cn.getName());
/* 1030 */       if (context == null) {
/* 1031 */         writer.println(smClient.getString("managerServlet.noContext", new Object[] {
/* 1032 */           Escape.htmlElementContent(cn.getDisplayName()) }));
/* 1033 */         return;
/*      */       }
/*      */       
/* 1036 */       if (context.getName().equals(this.context.getName())) {
/* 1037 */         writer.println(smClient.getString("managerServlet.noSelf"));
/* 1038 */         return;
/*      */       }
/* 1040 */       context.reload();
/* 1041 */       writer.println(smClient.getString("managerServlet.reloaded", new Object[] {cn
/* 1042 */         .getDisplayName() }));
/*      */     } catch (Throwable t) {
/* 1044 */       ExceptionUtils.handleThrowable(t);
/* 1045 */       log("ManagerServlet.reload[" + cn.getDisplayName() + "]", t);
/* 1046 */       writer.println(smClient.getString("managerServlet.exception", new Object[] {t
/* 1047 */         .toString() }));
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
/*      */   protected void resources(PrintWriter writer, String type, StringManager smClient)
/*      */   {
/* 1064 */     if (this.debug >= 1) {
/* 1065 */       if (type != null) {
/* 1066 */         log("resources:  Listing resources of type " + type);
/*      */       } else {
/* 1068 */         log("resources:  Listing resources of all types");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1073 */     if (this.global == null) {
/* 1074 */       writer.println(smClient.getString("managerServlet.noGlobal"));
/* 1075 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1079 */     if (type != null) {
/* 1080 */       writer.println(smClient.getString("managerServlet.resourcesType", new Object[] { type }));
/*      */     }
/*      */     else {
/* 1083 */       writer.println(smClient.getString("managerServlet.resourcesAll"));
/*      */     }
/*      */     
/* 1086 */     Class<?> clazz = null;
/*      */     try {
/* 1088 */       if (type != null) {
/* 1089 */         clazz = Class.forName(type);
/*      */       }
/*      */     } catch (Throwable t) {
/* 1092 */       ExceptionUtils.handleThrowable(t);
/* 1093 */       log("ManagerServlet.resources[" + type + "]", t);
/* 1094 */       writer.println(smClient.getString("managerServlet.exception", new Object[] {t
/* 1095 */         .toString() }));
/* 1096 */       return;
/*      */     }
/*      */     
/* 1099 */     printResources(writer, "", this.global, type, clazz, smClient);
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
/*      */   protected void printResources(PrintWriter writer, String prefix, javax.naming.Context namingContext, String type, Class<?> clazz, StringManager smClient)
/*      */   {
/*      */     try
/*      */     {
/* 1121 */       NamingEnumeration<Binding> items = namingContext.listBindings("");
/* 1122 */       while (items.hasMore()) {
/* 1123 */         Binding item = (Binding)items.next();
/* 1124 */         if ((item.getObject() instanceof javax.naming.Context))
/*      */         {
/* 1126 */           printResources(writer, prefix + item.getName() + "/", 
/* 1127 */             (javax.naming.Context)item.getObject(), type, clazz, smClient);
/*      */ 
/*      */         }
/* 1130 */         else if ((clazz == null) || 
/* 1131 */           (clazz.isInstance(item.getObject())))
/*      */         {
/*      */ 
/* 1134 */           writer.print(prefix + item.getName());
/* 1135 */           writer.print(':');
/* 1136 */           writer.print(item.getClassName());
/*      */           
/* 1138 */           writer.println();
/*      */         }
/*      */       }
/*      */     } catch (Throwable t) {
/* 1142 */       ExceptionUtils.handleThrowable(t);
/* 1143 */       log("ManagerServlet.resources[" + type + "]", t);
/* 1144 */       writer.println(smClient.getString("managerServlet.exception", new Object[] {t
/* 1145 */         .toString() }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void serverinfo(PrintWriter writer, StringManager smClient)
/*      */   {
/* 1157 */     if (this.debug >= 1)
/* 1158 */       log("serverinfo");
/*      */     try {
/* 1160 */       StringBuilder props = new StringBuilder();
/* 1161 */       props.append("OK - Server info");
/* 1162 */       props.append("\nTomcat Version: ");
/* 1163 */       props.append(ServerInfo.getServerInfo());
/* 1164 */       props.append("\nOS Name: ");
/* 1165 */       props.append(System.getProperty("os.name"));
/* 1166 */       props.append("\nOS Version: ");
/* 1167 */       props.append(System.getProperty("os.version"));
/* 1168 */       props.append("\nOS Architecture: ");
/* 1169 */       props.append(System.getProperty("os.arch"));
/* 1170 */       props.append("\nJVM Version: ");
/* 1171 */       props.append(System.getProperty("java.runtime.version"));
/* 1172 */       props.append("\nJVM Vendor: ");
/* 1173 */       props.append(System.getProperty("java.vm.vendor"));
/* 1174 */       writer.println(props.toString());
/*      */     } catch (Throwable t) {
/* 1176 */       ExceptionUtils.handleThrowable(t);
/* 1177 */       getServletContext().log("ManagerServlet.serverinfo", t);
/* 1178 */       writer.println(smClient.getString("managerServlet.exception", new Object[] {t
/* 1179 */         .toString() }));
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
/*      */   protected void sessions(PrintWriter writer, ContextName cn, int idle, StringManager smClient)
/*      */   {
/* 1196 */     if (this.debug >= 1) {
/* 1197 */       log("sessions: Session information for web application '" + cn + "'");
/* 1198 */       if (idle >= 0) {
/* 1199 */         log("sessions: Session expiration for " + idle + " minutes '" + cn + "'");
/*      */       }
/*      */     }
/* 1202 */     if (!validateContextName(cn, writer, smClient)) {
/* 1203 */       return;
/*      */     }
/*      */     
/* 1206 */     String displayPath = cn.getDisplayName();
/*      */     try
/*      */     {
/* 1209 */       org.apache.catalina.Context context = (org.apache.catalina.Context)this.host.findChild(cn.getName());
/* 1210 */       if (context == null) {
/* 1211 */         writer.println(smClient.getString("managerServlet.noContext", new Object[] {
/* 1212 */           Escape.htmlElementContent(displayPath) }));
/* 1213 */         return;
/*      */       }
/* 1215 */       Manager manager = context.getManager();
/* 1216 */       if (manager == null) {
/* 1217 */         writer.println(smClient.getString("managerServlet.noManager", new Object[] {
/* 1218 */           Escape.htmlElementContent(displayPath) }));
/* 1219 */         return;
/*      */       }
/* 1221 */       int maxCount = 60;
/* 1222 */       int histoInterval = 1;
/* 1223 */       int maxInactiveInterval = context.getSessionTimeout();
/* 1224 */       if (maxInactiveInterval > 0) {
/* 1225 */         histoInterval = maxInactiveInterval / maxCount;
/* 1226 */         if (histoInterval * maxCount < maxInactiveInterval)
/* 1227 */           histoInterval++;
/* 1228 */         if (0 == histoInterval)
/* 1229 */           histoInterval = 1;
/* 1230 */         maxCount = maxInactiveInterval / histoInterval;
/* 1231 */         if (histoInterval * maxCount < maxInactiveInterval) {
/* 1232 */           maxCount++;
/*      */         }
/*      */       }
/* 1235 */       writer.println(smClient.getString("managerServlet.sessions", new Object[] { displayPath }));
/*      */       
/* 1237 */       writer.println(smClient.getString("managerServlet.sessiondefaultmax", new Object[] { "" + maxInactiveInterval }));
/*      */       
/*      */ 
/* 1240 */       Session[] sessions = manager.findSessions();
/* 1241 */       int[] timeout = new int[maxCount + 1];
/* 1242 */       int notimeout = 0;
/* 1243 */       int expired = 0;
/* 1244 */       for (int i = 0; i < sessions.length; i++) {
/* 1245 */         int time = (int)(sessions[i].getIdleTimeInternal() / 1000L);
/* 1246 */         if ((idle >= 0) && (time >= idle * 60)) {
/* 1247 */           sessions[i].expire();
/* 1248 */           expired++;
/*      */         }
/* 1250 */         time = time / 60 / histoInterval;
/* 1251 */         if (time < 0) {
/* 1252 */           notimeout++;
/* 1253 */         } else if (time >= maxCount) {
/* 1254 */           timeout[maxCount] += 1;
/*      */         } else
/* 1256 */           timeout[time] += 1;
/*      */       }
/* 1258 */       if (timeout[0] > 0) {
/* 1259 */         writer.println(smClient.getString("managerServlet.sessiontimeout", new Object[] { "<" + histoInterval, "" + timeout[0] }));
/*      */       }
/*      */       
/* 1262 */       for (int i = 1; i < maxCount; i++) {
/* 1263 */         if (timeout[i] > 0) {
/* 1264 */           writer.println(smClient.getString("managerServlet.sessiontimeout", new Object[] { "" + i * histoInterval + " - <" + (i + 1) * histoInterval, "" + timeout[i] }));
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1269 */       if (timeout[maxCount] > 0) {
/* 1270 */         writer.println(smClient.getString("managerServlet.sessiontimeout", new Object[] { ">=" + maxCount * histoInterval, "" + timeout[maxCount] }));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1275 */       if (notimeout > 0) {
/* 1276 */         writer.println(smClient.getString("managerServlet.sessiontimeout.unlimited", new Object[] { "" + notimeout }));
/*      */       }
/*      */       
/* 1279 */       if (idle >= 0) {
/* 1280 */         writer.println(smClient.getString("managerServlet.sessiontimeout.expired", new Object[] { ">" + idle, "" + expired }));
/*      */       }
/*      */     }
/*      */     catch (Throwable t) {
/* 1284 */       ExceptionUtils.handleThrowable(t);
/* 1285 */       log("ManagerServlet.sessions[" + displayPath + "]", t);
/* 1286 */       writer.println(smClient.getString("managerServlet.exception", new Object[] {t
/* 1287 */         .toString() }));
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
/*      */   protected void expireSessions(PrintWriter writer, ContextName cn, HttpServletRequest req, StringManager smClient)
/*      */   {
/* 1303 */     int idle = -1;
/* 1304 */     String idleParam = req.getParameter("idle");
/* 1305 */     if (idleParam != null) {
/*      */       try {
/* 1307 */         idle = Integer.parseInt(idleParam);
/*      */       } catch (NumberFormatException e) {
/* 1309 */         log("Could not parse idle parameter to an int: " + idleParam);
/*      */       }
/*      */     }
/* 1312 */     sessions(writer, cn, idle, smClient);
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
/*      */   protected void start(PrintWriter writer, ContextName cn, StringManager smClient)
/*      */   {
/* 1325 */     if (this.debug >= 1) {
/* 1326 */       log("start: Starting web application '" + cn + "'");
/*      */     }
/* 1328 */     if (!validateContextName(cn, writer, smClient)) {
/* 1329 */       return;
/*      */     }
/*      */     
/* 1332 */     String displayPath = cn.getDisplayName();
/*      */     try
/*      */     {
/* 1335 */       org.apache.catalina.Context context = (org.apache.catalina.Context)this.host.findChild(cn.getName());
/* 1336 */       if (context == null) {
/* 1337 */         writer.println(smClient.getString("managerServlet.noContext", new Object[] {
/* 1338 */           Escape.htmlElementContent(displayPath) }));
/* 1339 */         return;
/*      */       }
/* 1341 */       context.start();
/* 1342 */       if (context.getState().isAvailable()) {
/* 1343 */         writer.println(smClient.getString("managerServlet.started", new Object[] { displayPath }));
/*      */       }
/*      */       else {
/* 1346 */         writer.println(smClient.getString("managerServlet.startFailed", new Object[] { displayPath }));
/*      */       }
/*      */     } catch (Throwable t) {
/* 1349 */       ExceptionUtils.handleThrowable(t);
/* 1350 */       getServletContext().log(sm.getString("managerServlet.startFailed", new Object[] { displayPath }), t);
/*      */       
/* 1352 */       writer.println(smClient.getString("managerServlet.startFailed", new Object[] { displayPath }));
/*      */       
/* 1354 */       writer.println(smClient.getString("managerServlet.exception", new Object[] {t
/* 1355 */         .toString() }));
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
/*      */   protected void stop(PrintWriter writer, ContextName cn, StringManager smClient)
/*      */   {
/* 1371 */     if (this.debug >= 1) {
/* 1372 */       log("stop: Stopping web application '" + cn + "'");
/*      */     }
/* 1374 */     if (!validateContextName(cn, writer, smClient)) {
/* 1375 */       return;
/*      */     }
/*      */     
/* 1378 */     String displayPath = cn.getDisplayName();
/*      */     try
/*      */     {
/* 1381 */       org.apache.catalina.Context context = (org.apache.catalina.Context)this.host.findChild(cn.getName());
/* 1382 */       if (context == null) {
/* 1383 */         writer.println(smClient.getString("managerServlet.noContext", new Object[] {
/* 1384 */           Escape.htmlElementContent(displayPath) }));
/* 1385 */         return;
/*      */       }
/*      */       
/* 1388 */       if (context.getName().equals(this.context.getName())) {
/* 1389 */         writer.println(smClient.getString("managerServlet.noSelf"));
/* 1390 */         return;
/*      */       }
/* 1392 */       context.stop();
/* 1393 */       writer.println(smClient.getString("managerServlet.stopped", new Object[] { displayPath }));
/*      */     }
/*      */     catch (Throwable t) {
/* 1396 */       ExceptionUtils.handleThrowable(t);
/* 1397 */       log("ManagerServlet.stop[" + displayPath + "]", t);
/* 1398 */       writer.println(smClient.getString("managerServlet.exception", new Object[] {t
/* 1399 */         .toString() }));
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
/*      */   protected void undeploy(PrintWriter writer, ContextName cn, StringManager smClient)
/*      */   {
/* 1415 */     if (this.debug >= 1) {
/* 1416 */       log("undeploy: Undeploying web application at '" + cn + "'");
/*      */     }
/* 1418 */     if (!validateContextName(cn, writer, smClient)) {
/* 1419 */       return;
/*      */     }
/*      */     
/* 1422 */     String name = cn.getName();
/* 1423 */     String baseName = cn.getBaseName();
/* 1424 */     String displayPath = cn.getDisplayName();
/*      */     
/*      */ 
/*      */     try
/*      */     {
/* 1429 */       org.apache.catalina.Context context = (org.apache.catalina.Context)this.host.findChild(name);
/* 1430 */       if (context == null) {
/* 1431 */         writer.println(smClient.getString("managerServlet.noContext", new Object[] {
/* 1432 */           Escape.htmlElementContent(displayPath) }));
/* 1433 */         return;
/*      */       }
/*      */       
/* 1436 */       if (!isDeployed(name)) {
/* 1437 */         writer.println(smClient.getString("managerServlet.notDeployed", new Object[] {
/* 1438 */           Escape.htmlElementContent(displayPath) }));
/* 1439 */         return;
/*      */       }
/*      */       
/* 1442 */       if (isServiced(name)) {
/* 1443 */         writer.println(smClient.getString("managerServlet.inService", new Object[] { displayPath }));
/*      */       } else {
/* 1445 */         addServiced(name);
/*      */         try
/*      */         {
/* 1448 */           context.stop();
/*      */         } catch (Throwable t) {
/* 1450 */           ExceptionUtils.handleThrowable(t);
/*      */         }
/*      */         try {
/* 1453 */           File war = new File(this.host.getAppBaseFile(), baseName + ".war");
/* 1454 */           File dir = new File(this.host.getAppBaseFile(), baseName);
/* 1455 */           File xml = new File(this.configBase, baseName + ".xml");
/* 1456 */           if ((war.exists()) && (!war.delete())) {
/* 1457 */             writer.println(smClient.getString("managerServlet.deleteFail", new Object[] { war }));
/*      */             
/* 1459 */             return; }
/* 1460 */           if ((dir.exists()) && (!undeployDir(dir))) {
/* 1461 */             writer.println(smClient.getString("managerServlet.deleteFail", new Object[] { dir }));
/*      */             
/* 1463 */             return; }
/* 1464 */           if ((xml.exists()) && (!xml.delete())) {
/* 1465 */             writer.println(smClient.getString("managerServlet.deleteFail", new Object[] { xml }));
/*      */             
/* 1467 */             return;
/*      */           }
/*      */           
/* 1470 */           check(name);
/*      */         } finally {
/* 1472 */           removeServiced(name);
/*      */         }
/*      */       }
/* 1475 */       writer.println(smClient.getString("managerServlet.undeployed", new Object[] { displayPath }));
/*      */     }
/*      */     catch (Throwable t) {
/* 1478 */       ExceptionUtils.handleThrowable(t);
/* 1479 */       log("ManagerServlet.undeploy[" + displayPath + "]", t);
/* 1480 */       writer.println(smClient.getString("managerServlet.exception", new Object[] {t
/* 1481 */         .toString() }));
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
/*      */   protected boolean isDeployed(String name)
/*      */     throws Exception
/*      */   {
/* 1499 */     String[] params = { name };
/* 1500 */     String[] signature = { "java.lang.String" };
/*      */     
/* 1502 */     Boolean result = (Boolean)this.mBeanServer.invoke(this.oname, "isDeployed", params, signature);
/* 1503 */     return result.booleanValue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void check(String name)
/*      */     throws Exception
/*      */   {
/* 1515 */     String[] params = { name };
/* 1516 */     String[] signature = { "java.lang.String" };
/* 1517 */     this.mBeanServer.invoke(this.oname, "check", params, signature);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean isServiced(String name)
/*      */     throws Exception
/*      */   {
/* 1530 */     String[] params = { name };
/* 1531 */     String[] signature = { "java.lang.String" };
/*      */     
/* 1533 */     Boolean result = (Boolean)this.mBeanServer.invoke(this.oname, "isServiced", params, signature);
/* 1534 */     return result.booleanValue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void addServiced(String name)
/*      */     throws Exception
/*      */   {
/* 1546 */     String[] params = { name };
/* 1547 */     String[] signature = { "java.lang.String" };
/* 1548 */     this.mBeanServer.invoke(this.oname, "addServiced", params, signature);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void removeServiced(String name)
/*      */     throws Exception
/*      */   {
/* 1560 */     String[] params = { name };
/* 1561 */     String[] signature = { "java.lang.String" };
/* 1562 */     this.mBeanServer.invoke(this.oname, "removeServiced", params, signature);
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
/*      */   protected boolean undeployDir(File dir)
/*      */   {
/* 1575 */     String[] files = dir.list();
/* 1576 */     if (files == null) {
/* 1577 */       files = new String[0];
/*      */     }
/* 1579 */     for (int i = 0; i < files.length; i++) {
/* 1580 */       File file = new File(dir, files[i]);
/* 1581 */       if (file.isDirectory()) {
/* 1582 */         if (!undeployDir(file)) {
/* 1583 */           return false;
/*      */         }
/*      */       }
/* 1586 */       else if (!file.delete()) {
/* 1587 */         return false;
/*      */       }
/*      */     }
/*      */     
/* 1591 */     return dir.delete();
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
/*      */   protected void uploadWar(PrintWriter writer, HttpServletRequest request, File war, StringManager smClient)
/*      */     throws IOException
/*      */   {
/* 1610 */     if ((war.exists()) && (!war.delete())) {
/* 1611 */       String msg = smClient.getString("managerServlet.deleteFail", new Object[] { war });
/* 1612 */       throw new IOException(msg);
/*      */     }
/*      */     try {
/* 1615 */       ServletInputStream istream = request.getInputStream();Throwable localThrowable6 = null;
/* 1616 */       try { BufferedOutputStream ostream = new BufferedOutputStream(new FileOutputStream(war), 1024);Throwable localThrowable7 = null;
/*      */         try {
/* 1618 */           byte[] buffer = new byte['Ѐ'];
/*      */           for (;;) {
/* 1620 */             int n = istream.read(buffer);
/* 1621 */             if (n < 0) {
/*      */               break;
/*      */             }
/* 1624 */             ostream.write(buffer, 0, n);
/*      */           }
/*      */         }
/*      */         catch (Throwable localThrowable1)
/*      */         {
/* 1615 */           localThrowable7 = localThrowable1;throw localThrowable1; } finally {} } catch (Throwable localThrowable4) { localThrowable6 = localThrowable4;throw localThrowable4;
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
/* 1626 */         if (istream != null) if (localThrowable6 != null) try { istream.close(); } catch (Throwable localThrowable5) { localThrowable6.addSuppressed(localThrowable5); } else istream.close();
/* 1627 */       } } catch (IOException e) { if ((war.exists()) && (!war.delete())) {
/* 1628 */         writer.println(smClient
/* 1629 */           .getString("managerServlet.deleteFail", new Object[] { war }));
/*      */       }
/* 1631 */       throw e;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static boolean validateContextName(ContextName cn, PrintWriter writer, StringManager sm)
/*      */   {
/* 1642 */     if ((cn != null) && (
/* 1643 */       (cn.getPath().startsWith("/")) || (cn.getPath().equals("")))) {
/* 1644 */       return true;
/*      */     }
/*      */     
/* 1647 */     String path = null;
/* 1648 */     if (cn != null) {
/* 1649 */       path = Escape.htmlElementContent(cn.getPath());
/*      */     }
/* 1651 */     writer.println(sm.getString("managerServlet.invalidPath", new Object[] { path }));
/* 1652 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean copy(File src, File dest)
/*      */   {
/* 1663 */     boolean result = false;
/*      */     try {
/* 1665 */       if ((src != null) && 
/* 1666 */         (!src.getCanonicalPath().equals(dest.getCanonicalPath()))) {
/* 1667 */         result = copyInternal(src, dest, new byte['က']);
/*      */       }
/*      */     } catch (IOException e) {
/* 1670 */       e.printStackTrace();
/*      */     }
/* 1672 */     return result;
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
/*      */   public static boolean copyInternal(File src, File dest, byte[] buf)
/*      */   {
/* 1686 */     boolean result = true;
/*      */     
/* 1688 */     String[] files = null;
/* 1689 */     if (src.isDirectory()) {
/* 1690 */       files = src.list();
/* 1691 */       result = dest.mkdir();
/*      */     } else {
/* 1693 */       files = new String[1];
/* 1694 */       files[0] = "";
/*      */     }
/* 1696 */     if (files == null) {
/* 1697 */       files = new String[0];
/*      */     }
/* 1699 */     for (int i = 0; (i < files.length) && (result); i++) {
/* 1700 */       File fileSrc = new File(src, files[i]);
/* 1701 */       File fileDest = new File(dest, files[i]);
/* 1702 */       if (fileSrc.isDirectory())
/* 1703 */         result = copyInternal(fileSrc, fileDest, buf); else {
/*      */         try {
/* 1705 */           FileInputStream is = new FileInputStream(fileSrc);Throwable localThrowable6 = null;
/* 1706 */           try { FileOutputStream os = new FileOutputStream(fileDest);Throwable localThrowable7 = null;
/* 1707 */             try { int len = 0;
/*      */               for (;;) {
/* 1709 */                 len = is.read(buf);
/* 1710 */                 if (len == -1)
/*      */                   break;
/* 1712 */                 os.write(buf, 0, len);
/*      */               }
/*      */             }
/*      */             catch (Throwable localThrowable1)
/*      */             {
/* 1705 */               localThrowable7 = localThrowable1;throw localThrowable1; } finally {} } catch (Throwable localThrowable4) { localThrowable6 = localThrowable4;throw localThrowable4;
/*      */ 
/*      */ 
/*      */ 
/*      */           }
/*      */           finally
/*      */           {
/*      */ 
/*      */ 
/* 1714 */             if (is != null) if (localThrowable6 != null) try { is.close(); } catch (Throwable localThrowable5) { localThrowable6.addSuppressed(localThrowable5); } else is.close();
/* 1715 */           } } catch (IOException e) { e.printStackTrace();
/* 1716 */           result = false;
/*      */         }
/*      */       }
/*      */     }
/* 1720 */     return result;
/*      */   }
/*      */   
/*      */   protected Map<String, List<String>> getConnectorCiphers()
/*      */   {
/* 1725 */     Map<String, List<String>> result = new HashMap();
/*      */     
/* 1727 */     Engine e = (Engine)this.host.getParent();
/* 1728 */     Service s = e.getService();
/* 1729 */     Connector[] connectors = s.findConnectors();
/* 1730 */     for (Connector connector : connectors) {
/* 1731 */       if (Boolean.TRUE.equals(connector.getProperty("SSLEnabled"))) {
/* 1732 */         SSLHostConfig[] sslHostConfigs = connector.getProtocolHandler().findSslHostConfigs();
/* 1733 */         for (SSLHostConfig sslHostConfig : sslHostConfigs) {
/* 1734 */           String name = connector.toString() + "-" + sslHostConfig.getHostName();
/*      */           
/* 1736 */           result.put(name, new ArrayList(new LinkedHashSet(
/* 1737 */             Arrays.asList(sslHostConfig.getEnabledCiphers()))));
/*      */         }
/*      */       } else {
/* 1740 */         ArrayList<String> cipherList = new ArrayList(1);
/* 1741 */         cipherList.add(sm.getString("managerServlet.notSslConnector"));
/* 1742 */         result.put(connector.toString(), cipherList);
/*      */       }
/*      */     }
/* 1745 */     return result;
/*      */   }
/*      */   
/*      */   protected Map<String, List<String>> getConnectorCerts()
/*      */   {
/* 1750 */     Map<String, List<String>> result = new HashMap();
/*      */     
/* 1752 */     Engine e = (Engine)this.host.getParent();
/* 1753 */     Service s = e.getService();
/* 1754 */     Connector[] connectors = s.findConnectors();
/* 1755 */     for (Connector connector : connectors) {
/* 1756 */       if (Boolean.TRUE.equals(connector.getProperty("SSLEnabled"))) {
/* 1757 */         SSLHostConfig[] sslHostConfigs = connector.getProtocolHandler().findSslHostConfigs();
/* 1758 */         SSLHostConfig sslHostConfig; for (sslHostConfig : sslHostConfigs)
/*      */         {
/* 1760 */           Set<SSLHostConfigCertificate> sslHostConfigCerts = sslHostConfig.getCertificates();
/* 1761 */           for (SSLHostConfigCertificate sslHostConfigCert : sslHostConfigCerts)
/*      */           {
/* 1763 */             String name = connector.toString() + "-" + sslHostConfig.getHostName() + "-" + sslHostConfigCert.getType();
/* 1764 */             List<String> certList = new ArrayList();
/* 1765 */             SSLContext sslContext = sslHostConfigCert.getSslContext();
/* 1766 */             String alias = sslHostConfigCert.getCertificateKeyAlias();
/* 1767 */             if (alias == null) {
/* 1768 */               alias = "tomcat";
/*      */             }
/* 1770 */             X509Certificate[] certs = sslContext.getCertificateChain(alias);
/* 1771 */             if (certs == null) {
/* 1772 */               certList.add(sm.getString("managerServlet.certsNotAvailable"));
/*      */             } else {
/* 1774 */               for (Certificate cert : certs) {
/* 1775 */                 certList.add(cert.toString());
/*      */               }
/*      */             }
/* 1778 */             result.put(name, certList);
/*      */           }
/*      */         }
/*      */       } else {
/* 1782 */         List<String> certList = new ArrayList(1);
/* 1783 */         certList.add(sm.getString("managerServlet.notSslConnector"));
/* 1784 */         result.put(connector.toString(), certList);
/*      */       }
/*      */     }
/*      */     
/* 1788 */     return result;
/*      */   }
/*      */   
/*      */   protected Map<String, List<String>> getConnectorTrustedCerts()
/*      */   {
/* 1793 */     Map<String, List<String>> result = new HashMap();
/*      */     
/* 1795 */     Engine e = (Engine)this.host.getParent();
/* 1796 */     Service s = e.getService();
/* 1797 */     Connector[] connectors = s.findConnectors();
/* 1798 */     for (Connector connector : connectors) {
/* 1799 */       if (Boolean.TRUE.equals(connector.getProperty("SSLEnabled"))) {
/* 1800 */         SSLHostConfig[] sslHostConfigs = connector.getProtocolHandler().findSslHostConfigs();
/* 1801 */         for (SSLHostConfig sslHostConfig : sslHostConfigs) {
/* 1802 */           String name = connector.toString() + "-" + sslHostConfig.getHostName();
/* 1803 */           List<String> certList = new ArrayList();
/*      */           
/* 1805 */           SSLContext sslContext = ((SSLHostConfigCertificate)sslHostConfig.getCertificates().iterator().next()).getSslContext();
/* 1806 */           X509Certificate[] certs = sslContext.getAcceptedIssuers();
/* 1807 */           if (certs == null) {
/* 1808 */             certList.add(sm.getString("managerServlet.certsNotAvailable"));
/* 1809 */           } else if (certs.length == 0) {
/* 1810 */             certList.add(sm.getString("managerServlet.trustedCertsNotConfigured"));
/*      */           } else {
/* 1812 */             for (Certificate cert : certs) {
/* 1813 */               certList.add(cert.toString());
/*      */             }
/*      */           }
/* 1816 */           result.put(name, certList);
/*      */         }
/*      */       } else {
/* 1819 */         List<String> certList = new ArrayList(1);
/* 1820 */         certList.add(sm.getString("managerServlet.notSslConnector"));
/* 1821 */         result.put(connector.toString(), certList);
/*      */       }
/*      */     }
/*      */     
/* 1825 */     return result;
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\manager\ManagerServlet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */