/*     */ package org.apache.catalina.manager.host;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.ObjectName;
/*     */ import javax.servlet.ServletConfig;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.UnavailableException;
/*     */ import javax.servlet.http.HttpServlet;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.catalina.Container;
/*     */ import org.apache.catalina.ContainerServlet;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.Engine;
/*     */ import org.apache.catalina.Host;
/*     */ import org.apache.catalina.LifecycleState;
/*     */ import org.apache.catalina.Wrapper;
/*     */ import org.apache.catalina.core.ContainerBase;
/*     */ import org.apache.catalina.core.StandardHost;
/*     */ import org.apache.catalina.startup.HostConfig;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
/*     */ import org.apache.tomcat.util.buf.StringUtils;
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
/*     */ public class HostManagerServlet
/*     */   extends HttpServlet
/*     */   implements ContainerServlet
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/* 102 */   protected transient Context context = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 108 */   protected int debug = 1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 114 */   protected transient Host installedHost = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 120 */   protected transient Engine engine = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 127 */   protected static final StringManager sm = StringManager.getManager("org.apache.catalina.manager.host");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 133 */   protected transient Wrapper wrapper = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Wrapper getWrapper()
/*     */   {
/* 145 */     return this.wrapper;
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
/*     */   public void setWrapper(Wrapper wrapper)
/*     */   {
/* 158 */     this.wrapper = wrapper;
/* 159 */     if (wrapper == null) {
/* 160 */       this.context = null;
/* 161 */       this.installedHost = null;
/* 162 */       this.engine = null;
/*     */     } else {
/* 164 */       this.context = ((Context)wrapper.getParent());
/* 165 */       this.installedHost = ((Host)this.context.getParent());
/* 166 */       this.engine = ((Engine)this.installedHost.getParent());
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
/*     */ 
/*     */   public void destroy() {}
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
/*     */   public void doGet(HttpServletRequest request, HttpServletResponse response)
/*     */     throws IOException, ServletException
/*     */   {
/* 199 */     StringManager smClient = StringManager.getManager("org.apache.catalina.manager.host", request
/* 200 */       .getLocales());
/*     */     
/*     */ 
/* 203 */     String command = request.getPathInfo();
/* 204 */     if (command == null)
/* 205 */       command = request.getServletPath();
/* 206 */     String name = request.getParameter("name");
/*     */     
/*     */ 
/* 209 */     response.setContentType("text/plain; charset=utf-8");
/* 210 */     PrintWriter writer = response.getWriter();
/*     */     
/*     */ 
/* 213 */     if (command == null) {
/* 214 */       writer.println(sm.getString("hostManagerServlet.noCommand"));
/* 215 */     } else if (command.equals("/add")) {
/* 216 */       add(request, writer, name, false, smClient);
/* 217 */     } else if (command.equals("/remove")) {
/* 218 */       remove(writer, name, smClient);
/* 219 */     } else if (command.equals("/list")) {
/* 220 */       list(writer, smClient);
/* 221 */     } else if (command.equals("/start")) {
/* 222 */       start(writer, name, smClient);
/* 223 */     } else if (command.equals("/stop")) {
/* 224 */       stop(writer, name, smClient);
/* 225 */     } else if (command.equals("/persist")) {
/* 226 */       persist(writer, smClient);
/*     */     } else {
/* 228 */       writer.println(sm.getString("hostManagerServlet.unknownCommand", new Object[] { command }));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 233 */     writer.flush();
/* 234 */     writer.close();
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
/*     */   protected void add(HttpServletRequest request, PrintWriter writer, String name, boolean htmlMode, StringManager smClient)
/*     */   {
/* 249 */     String aliases = request.getParameter("aliases");
/* 250 */     String appBase = request.getParameter("appBase");
/* 251 */     boolean manager = booleanParameter(request, "manager", false, htmlMode);
/* 252 */     boolean autoDeploy = booleanParameter(request, "autoDeploy", true, htmlMode);
/* 253 */     boolean deployOnStartup = booleanParameter(request, "deployOnStartup", true, htmlMode);
/* 254 */     boolean deployXML = booleanParameter(request, "deployXML", true, htmlMode);
/* 255 */     boolean unpackWARs = booleanParameter(request, "unpackWARs", true, htmlMode);
/* 256 */     boolean copyXML = booleanParameter(request, "copyXML", false, htmlMode);
/* 257 */     add(writer, name, aliases, appBase, manager, autoDeploy, deployOnStartup, deployXML, unpackWARs, copyXML, smClient);
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
/*     */   protected boolean booleanParameter(HttpServletRequest request, String parameter, boolean theDefault, boolean htmlMode)
/*     */   {
/* 277 */     String value = request.getParameter(parameter);
/* 278 */     boolean booleanValue = theDefault;
/* 279 */     if (value != null) {
/* 280 */       if (htmlMode) {
/* 281 */         if (value.equals("on")) {
/* 282 */           booleanValue = true;
/*     */         }
/* 284 */       } else if (theDefault) {
/* 285 */         if (value.equals("false")) {
/* 286 */           booleanValue = false;
/*     */         }
/* 288 */       } else if (value.equals("true")) {
/* 289 */         booleanValue = true;
/*     */       }
/* 291 */     } else if (htmlMode)
/* 292 */       booleanValue = false;
/* 293 */     return booleanValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void init()
/*     */     throws ServletException
/*     */   {
/* 301 */     if ((this.wrapper == null) || (this.context == null))
/*     */     {
/* 303 */       throw new UnavailableException(sm.getString("hostManagerServlet.noWrapper"));
/*     */     }
/*     */     
/* 306 */     String value = null;
/*     */     try {
/* 308 */       value = getServletConfig().getInitParameter("debug");
/* 309 */       this.debug = Integer.parseInt(value);
/*     */     } catch (Throwable t) {
/* 311 */       ExceptionUtils.handleThrowable(t);
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
/*     */   protected synchronized void add(PrintWriter writer, String name, String aliases, String appBase, boolean manager, boolean autoDeploy, boolean deployOnStartup, boolean deployXML, boolean unpackWARs, boolean copyXML, StringManager smClient)
/*     */   {
/* 345 */     if (this.debug >= 1) {
/* 346 */       log(sm.getString("hostManagerServlet.add", new Object[] { name }));
/*     */     }
/*     */     
/*     */ 
/* 350 */     if ((name == null) || (name.length() == 0)) {
/* 351 */       writer.println(smClient.getString("hostManagerServlet.invalidHostName", new Object[] { name }));
/*     */       
/* 353 */       return;
/*     */     }
/*     */     
/*     */ 
/* 357 */     if (this.engine.findChild(name) != null) {
/* 358 */       writer.println(smClient.getString("hostManagerServlet.alreadyHost", new Object[] { name }));
/*     */       
/* 360 */       return;
/*     */     }
/*     */     
/*     */ 
/* 364 */     File appBaseFile = null;
/* 365 */     File file = null;
/* 366 */     String applicationBase = appBase;
/* 367 */     if ((applicationBase == null) || (applicationBase.length() == 0)) {
/* 368 */       applicationBase = name;
/*     */     }
/* 370 */     file = new File(applicationBase);
/* 371 */     if (!file.isAbsolute())
/* 372 */       file = new File(this.engine.getCatalinaBase(), file.getPath());
/*     */     try {
/* 374 */       appBaseFile = file.getCanonicalFile();
/*     */     } catch (IOException e) {
/* 376 */       appBaseFile = file;
/*     */     }
/* 378 */     if ((!appBaseFile.mkdirs()) && (!appBaseFile.isDirectory())) {
/* 379 */       writer.println(smClient.getString("hostManagerServlet.appBaseCreateFail", new Object[] {appBaseFile
/*     */       
/* 381 */         .toString(), name }));
/* 382 */       return;
/*     */     }
/*     */     
/*     */ 
/* 386 */     File configBaseFile = getConfigBase(name);
/*     */     
/*     */ 
/* 389 */     if (manager) {
/* 390 */       if (configBaseFile == null) {
/* 391 */         writer.println(smClient.getString("hostManagerServlet.configBaseCreateFail", new Object[] { name }));
/*     */         
/* 393 */         return;
/*     */       }
/* 395 */       try { InputStream is = getServletContext().getResourceAsStream("/manager.xml");Throwable localThrowable3 = null;
/* 396 */         try { Path dest = new File(configBaseFile, "manager.xml").toPath();
/* 397 */           Files.copy(is, dest, new CopyOption[0]);
/*     */         }
/*     */         catch (Throwable localThrowable1)
/*     */         {
/* 395 */           localThrowable3 = localThrowable1;throw localThrowable1;
/*     */         }
/*     */         finally {
/* 398 */           if (is != null) if (localThrowable3 != null) try { is.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else is.close();
/* 399 */         } } catch (IOException e) { writer.println(smClient.getString("hostManagerServlet.managerXml"));
/* 400 */         return;
/*     */       }
/*     */     }
/*     */     
/* 404 */     StandardHost host = new StandardHost();
/* 405 */     host.setAppBase(applicationBase);
/* 406 */     host.setName(name);
/*     */     
/* 408 */     host.addLifecycleListener(new HostConfig());
/*     */     
/*     */ 
/* 411 */     if ((aliases != null) && (!"".equals(aliases))) {
/* 412 */       StringTokenizer tok = new StringTokenizer(aliases, ", ");
/* 413 */       while (tok.hasMoreTokens()) {
/* 414 */         host.addAlias(tok.nextToken());
/*     */       }
/*     */     }
/* 417 */     host.setAutoDeploy(autoDeploy);
/* 418 */     host.setDeployOnStartup(deployOnStartup);
/* 419 */     host.setDeployXML(deployXML);
/* 420 */     host.setUnpackWARs(unpackWARs);
/* 421 */     host.setCopyXML(copyXML);
/*     */     
/*     */     try
/*     */     {
/* 425 */       this.engine.addChild(host);
/*     */     } catch (Exception e) {
/* 427 */       writer.println(smClient.getString("hostManagerServlet.exception", new Object[] {e
/* 428 */         .toString() }));
/* 429 */       return;
/*     */     }
/*     */     
/* 432 */     host = (StandardHost)this.engine.findChild(name);
/* 433 */     if (host != null) {
/* 434 */       writer.println(smClient.getString("hostManagerServlet.add", new Object[] { name }));
/*     */     }
/*     */     else {
/* 437 */       writer.println(smClient.getString("hostManagerServlet.addFailed", new Object[] { name }));
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
/*     */   protected synchronized void remove(PrintWriter writer, String name, StringManager smClient)
/*     */   {
/* 454 */     if (this.debug >= 1) {
/* 455 */       log(sm.getString("hostManagerServlet.remove", new Object[] { name }));
/*     */     }
/*     */     
/*     */ 
/* 459 */     if ((name == null) || (name.length() == 0)) {
/* 460 */       writer.println(smClient.getString("hostManagerServlet.invalidHostName", new Object[] { name }));
/*     */       
/* 462 */       return;
/*     */     }
/*     */     
/*     */ 
/* 466 */     if (this.engine.findChild(name) == null) {
/* 467 */       writer.println(smClient.getString("hostManagerServlet.noHost", new Object[] { name }));
/*     */       
/* 469 */       return;
/*     */     }
/*     */     
/*     */ 
/* 473 */     if (this.engine.findChild(name) == this.installedHost) {
/* 474 */       writer.println(smClient.getString("hostManagerServlet.cannotRemoveOwnHost", new Object[] { name }));
/*     */       
/* 476 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 482 */       Container child = this.engine.findChild(name);
/* 483 */       this.engine.removeChild(child);
/* 484 */       if ((child instanceof ContainerBase)) ((ContainerBase)child).destroy();
/*     */     } catch (Exception e) {
/* 486 */       writer.println(smClient.getString("hostManagerServlet.exception", new Object[] {e
/* 487 */         .toString() }));
/* 488 */       return;
/*     */     }
/*     */     
/* 491 */     Host host = (StandardHost)this.engine.findChild(name);
/* 492 */     if (host == null) {
/* 493 */       writer.println(smClient.getString("hostManagerServlet.remove", new Object[] { name }));
/*     */     }
/*     */     else
/*     */     {
/* 497 */       writer.println(smClient.getString("hostManagerServlet.removeFailed", new Object[] { name }));
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
/*     */   protected void list(PrintWriter writer, StringManager smClient)
/*     */   {
/* 512 */     if (this.debug >= 1) {
/* 513 */       log(sm.getString("hostManagerServlet.list", new Object[] { this.engine.getName() }));
/*     */     }
/*     */     
/* 516 */     writer.println(smClient.getString("hostManagerServlet.listed", new Object[] {this.engine
/* 517 */       .getName() }));
/* 518 */     Container[] hosts = this.engine.findChildren();
/* 519 */     for (int i = 0; i < hosts.length; i++) {
/* 520 */       Host host = (Host)hosts[i];
/* 521 */       String name = host.getName();
/* 522 */       String[] aliases = host.findAliases();
/* 523 */       writer.println(smClient.getString("hostManagerServlet.listitem", new Object[] { name, 
/* 524 */         StringUtils.join(aliases) }));
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
/*     */   protected void start(PrintWriter writer, String name, StringManager smClient)
/*     */   {
/* 539 */     if (this.debug >= 1) {
/* 540 */       log(sm.getString("hostManagerServlet.start", new Object[] { name }));
/*     */     }
/*     */     
/*     */ 
/* 544 */     if ((name == null) || (name.length() == 0)) {
/* 545 */       writer.println(smClient.getString("hostManagerServlet.invalidHostName", new Object[] { name }));
/*     */       
/* 547 */       return;
/*     */     }
/*     */     
/* 550 */     Container host = this.engine.findChild(name);
/*     */     
/*     */ 
/* 553 */     if (host == null) {
/* 554 */       writer.println(smClient.getString("hostManagerServlet.noHost", new Object[] { name }));
/*     */       
/* 556 */       return;
/*     */     }
/*     */     
/*     */ 
/* 560 */     if (host == this.installedHost) {
/* 561 */       writer.println(smClient.getString("hostManagerServlet.cannotStartOwnHost", new Object[] { name }));
/*     */       
/* 563 */       return;
/*     */     }
/*     */     
/*     */ 
/* 567 */     if (host.getState().isAvailable()) {
/* 568 */       writer.println(smClient.getString("hostManagerServlet.alreadyStarted", new Object[] { name }));
/*     */       
/* 570 */       return;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 575 */       host.start();
/* 576 */       writer.println(smClient.getString("hostManagerServlet.started", new Object[] { name }));
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 580 */       getServletContext().log(sm.getString("hostManagerServlet.startFailed", new Object[] { name }), e);
/* 581 */       writer.println(smClient.getString("hostManagerServlet.startFailed", new Object[] { name }));
/*     */       
/* 583 */       writer.println(smClient.getString("hostManagerServlet.exception", new Object[] {e
/* 584 */         .toString() }));
/* 585 */       return;
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
/*     */   protected void stop(PrintWriter writer, String name, StringManager smClient)
/*     */   {
/* 601 */     if (this.debug >= 1) {
/* 602 */       log(sm.getString("hostManagerServlet.stop", new Object[] { name }));
/*     */     }
/*     */     
/*     */ 
/* 606 */     if ((name == null) || (name.length() == 0)) {
/* 607 */       writer.println(smClient.getString("hostManagerServlet.invalidHostName", new Object[] { name }));
/*     */       
/* 609 */       return;
/*     */     }
/*     */     
/* 612 */     Container host = this.engine.findChild(name);
/*     */     
/*     */ 
/* 615 */     if (host == null) {
/* 616 */       writer.println(smClient.getString("hostManagerServlet.noHost", new Object[] { name }));
/*     */       
/* 618 */       return;
/*     */     }
/*     */     
/*     */ 
/* 622 */     if (host == this.installedHost) {
/* 623 */       writer.println(smClient.getString("hostManagerServlet.cannotStopOwnHost", new Object[] { name }));
/*     */       
/* 625 */       return;
/*     */     }
/*     */     
/*     */ 
/* 629 */     if (!host.getState().isAvailable()) {
/* 630 */       writer.println(smClient.getString("hostManagerServlet.alreadyStopped", new Object[] { name }));
/*     */       
/* 632 */       return;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 637 */       host.stop();
/* 638 */       writer.println(smClient.getString("hostManagerServlet.stopped", new Object[] { name }));
/*     */     }
/*     */     catch (Exception e) {
/* 641 */       getServletContext().log(sm.getString("hostManagerServlet.stopFailed", new Object[] { name }), e);
/*     */       
/* 643 */       writer.println(smClient.getString("hostManagerServlet.stopFailed", new Object[] { name }));
/*     */       
/* 645 */       writer.println(smClient.getString("hostManagerServlet.exception", new Object[] {e
/* 646 */         .toString() }));
/* 647 */       return;
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
/*     */   protected void persist(PrintWriter writer, StringManager smClient)
/*     */   {
/* 661 */     if (this.debug >= 1) {
/* 662 */       log(sm.getString("hostManagerServlet.persist"));
/*     */     }
/*     */     try
/*     */     {
/* 666 */       MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
/* 667 */       ObjectName oname = new ObjectName(this.engine.getDomain() + ":type=StoreConfig");
/* 668 */       platformMBeanServer.invoke(oname, "storeConfig", null, null);
/* 669 */       writer.println(smClient.getString("hostManagerServlet.persisted"));
/*     */     } catch (Exception e) {
/* 671 */       getServletContext().log(sm.getString("hostManagerServlet.persistFailed"), e);
/* 672 */       writer.println(smClient.getString("hostManagerServlet.persistFailed"));
/*     */       
/*     */ 
/* 675 */       if ((e instanceof InstanceNotFoundException)) {
/* 676 */         writer.println("Please enable StoreConfig to use this feature.");
/*     */       } else {
/* 678 */         writer.println(smClient.getString("hostManagerServlet.exception", new Object[] { e.toString() }));
/*     */       }
/* 680 */       return;
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
/*     */   protected File getConfigBase(String hostName)
/*     */   {
/* 693 */     File configBase = new File(this.context.getCatalinaBase(), "conf");
/* 694 */     if (!configBase.exists()) {
/* 695 */       return null;
/*     */     }
/* 697 */     if (this.engine != null) {
/* 698 */       configBase = new File(configBase, this.engine.getName());
/*     */     }
/* 700 */     if (this.installedHost != null) {
/* 701 */       configBase = new File(configBase, hostName);
/*     */     }
/* 703 */     if ((!configBase.mkdirs()) && (!configBase.isDirectory())) {
/* 704 */       return null;
/*     */     }
/* 706 */     return configBase;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\manager\host\HostManagerServlet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */