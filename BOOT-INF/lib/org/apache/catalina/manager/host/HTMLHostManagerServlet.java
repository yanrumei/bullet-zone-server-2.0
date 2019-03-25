/*     */ package org.apache.catalina.manager.host;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.net.URLEncoder;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.catalina.Container;
/*     */ import org.apache.catalina.Engine;
/*     */ import org.apache.catalina.Host;
/*     */ import org.apache.catalina.LifecycleState;
/*     */ import org.apache.catalina.manager.Constants;
/*     */ import org.apache.catalina.util.ServerInfo;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ import org.apache.tomcat.util.security.Escape;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class HTMLHostManagerServlet
/*     */   extends HostManagerServlet
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private static final String HOSTS_HEADER_SECTION = "<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\">\n<tr>\n <td colspan=\"5\" class=\"title\">{0}</td>\n</tr>\n<tr>\n <td class=\"header-left\"><small>{0}</small></td>\n <td class=\"header-center\"><small>{1}</small></td>\n <td class=\"header-center\"><small>{2}</small></td>\n</tr>\n";
/*     */   private static final String HOSTS_ROW_DETAILS_SECTION = "<tr>\n <td class=\"row-left\"><small><a href=\"http://{0}\">{0}</a></small></td>\n <td class=\"row-center\"><small>{1}</small></td>\n";
/*     */   
/*     */   public void doGet(HttpServletRequest request, HttpServletResponse response)
/*     */     throws IOException, ServletException
/*     */   {
/*  81 */     StringManager smClient = StringManager.getManager("org.apache.catalina.manager.host", request
/*  82 */       .getLocales());
/*     */     
/*     */ 
/*  85 */     String command = request.getPathInfo();
/*     */     
/*     */ 
/*  88 */     response.setContentType("text/html; charset=utf-8");
/*     */     
/*  90 */     String message = "";
/*     */     
/*  92 */     if (command != null)
/*     */     {
/*  94 */       if (!command.equals("/list"))
/*     */       {
/*  96 */         if ((command.equals("/add")) || (command.equals("/remove")) || 
/*  97 */           (command.equals("/start")) || (command.equals("/stop")) || 
/*  98 */           (command.equals("/persist"))) {
/*  99 */           message = smClient.getString("hostManagerServlet.postCommand", new Object[] { command });
/*     */         }
/*     */         else {
/* 102 */           message = smClient.getString("hostManagerServlet.unknownCommand", new Object[] { command });
/*     */         }
/*     */       }
/*     */     }
/* 106 */     list(request, response, message, smClient);
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
/*     */   public void doPost(HttpServletRequest request, HttpServletResponse response)
/*     */     throws ServletException, IOException
/*     */   {
/* 123 */     StringManager smClient = StringManager.getManager("org.apache.catalina.manager.host", request
/* 124 */       .getLocales());
/*     */     
/*     */ 
/* 127 */     String command = request.getPathInfo();
/*     */     
/* 129 */     String name = request.getParameter("name");
/*     */     
/*     */ 
/* 132 */     response.setContentType("text/html; charset=utf-8");
/*     */     
/* 134 */     String message = "";
/*     */     
/*     */ 
/* 137 */     if (command != null)
/*     */     {
/* 139 */       if (command.equals("/add")) {
/* 140 */         message = add(request, name, smClient);
/* 141 */       } else if (command.equals("/remove")) {
/* 142 */         message = remove(name, smClient);
/* 143 */       } else if (command.equals("/start")) {
/* 144 */         message = start(name, smClient);
/* 145 */       } else if (command.equals("/stop")) {
/* 146 */         message = stop(name, smClient);
/* 147 */       } else if (command.equals("/persist")) {
/* 148 */         message = persist(smClient);
/*     */       }
/*     */       else {
/* 151 */         doGet(request, response);
/*     */       }
/*     */     }
/* 154 */     list(request, response, message, smClient);
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
/*     */   protected String add(HttpServletRequest request, String name, StringManager smClient)
/*     */   {
/* 169 */     StringWriter stringWriter = new StringWriter();
/* 170 */     PrintWriter printWriter = new PrintWriter(stringWriter);
/*     */     
/* 172 */     super.add(request, printWriter, name, true, smClient);
/*     */     
/* 174 */     return stringWriter.toString();
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
/*     */   protected String remove(String name, StringManager smClient)
/*     */   {
/* 187 */     StringWriter stringWriter = new StringWriter();
/* 188 */     PrintWriter printWriter = new PrintWriter(stringWriter);
/*     */     
/* 190 */     super.remove(printWriter, name, smClient);
/*     */     
/* 192 */     return stringWriter.toString();
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
/*     */   protected String start(String name, StringManager smClient)
/*     */   {
/* 205 */     StringWriter stringWriter = new StringWriter();
/* 206 */     PrintWriter printWriter = new PrintWriter(stringWriter);
/*     */     
/* 208 */     super.start(printWriter, name, smClient);
/*     */     
/* 210 */     return stringWriter.toString();
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
/*     */   protected String stop(String name, StringManager smClient)
/*     */   {
/* 223 */     StringWriter stringWriter = new StringWriter();
/* 224 */     PrintWriter printWriter = new PrintWriter(stringWriter);
/*     */     
/* 226 */     super.stop(printWriter, name, smClient);
/*     */     
/* 228 */     return stringWriter.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String persist(StringManager smClient)
/*     */   {
/* 240 */     StringWriter stringWriter = new StringWriter();
/* 241 */     PrintWriter printWriter = new PrintWriter(stringWriter);
/*     */     
/* 243 */     super.persist(printWriter, smClient);
/*     */     
/* 245 */     return stringWriter.toString();
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
/*     */   public void list(HttpServletRequest request, HttpServletResponse response, String message, StringManager smClient)
/*     */     throws IOException
/*     */   {
/* 264 */     if (this.debug >= 1) {
/* 265 */       log(sm.getString("hostManagerServlet.list", new Object[] { this.engine.getName() }));
/*     */     }
/*     */     
/* 268 */     PrintWriter writer = response.getWriter();
/*     */     
/*     */ 
/* 271 */     writer.print(Constants.HTML_HEADER_SECTION);
/*     */     
/*     */ 
/* 274 */     Object[] args = new Object[2];
/* 275 */     args[0] = request.getContextPath();
/* 276 */     args[1] = smClient.getString("htmlHostManagerServlet.title");
/* 277 */     writer.print(MessageFormat.format(Constants.BODY_HEADER_SECTION, args));
/*     */     
/*     */ 
/*     */ 
/* 281 */     args = new Object[3];
/* 282 */     args[0] = smClient.getString("htmlHostManagerServlet.messageLabel");
/* 283 */     if ((message == null) || (message.length() == 0)) {
/* 284 */       args[1] = "OK";
/*     */     } else {
/* 286 */       args[1] = Escape.htmlElementContent(message);
/*     */     }
/* 288 */     writer.print(MessageFormat.format("<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\">\n <tr>\n  <td class=\"row-left\" width=\"10%\"><small><strong>{0}</strong></small>&nbsp;</td>\n  <td class=\"row-left\"><pre>{1}</pre></td>\n </tr>\n</table>\n<br>\n\n", args));
/*     */     
/*     */ 
/* 291 */     args = new Object[9];
/* 292 */     args[0] = smClient.getString("htmlHostManagerServlet.manager");
/* 293 */     args[1] = response.encodeURL(request.getContextPath() + "/html/list");
/* 294 */     args[2] = smClient.getString("htmlHostManagerServlet.list");
/* 295 */     args[3] = response
/* 296 */       .encodeURL(request.getContextPath() + "/" + smClient
/* 297 */       .getString("htmlHostManagerServlet.helpHtmlManagerFile"));
/* 298 */     args[4] = smClient.getString("htmlHostManagerServlet.helpHtmlManager");
/* 299 */     args[5] = response
/* 300 */       .encodeURL(request.getContextPath() + "/" + smClient
/* 301 */       .getString("htmlHostManagerServlet.helpManagerFile"));
/* 302 */     args[6] = smClient.getString("htmlHostManagerServlet.helpManager");
/* 303 */     args[7] = response.encodeURL("/manager/status");
/* 304 */     args[8] = smClient.getString("statusServlet.title");
/* 305 */     writer.print(MessageFormat.format("<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\">\n<tr>\n <td colspan=\"4\" class=\"title\">{0}</td>\n</tr>\n <tr>\n  <td class=\"row-left\"><a href=\"{1}\">{2}</a></td>\n  <td class=\"row-center\"><a href=\"{3}\">{4}</a></td>\n  <td class=\"row-center\"><a href=\"{5}\">{6}</a></td>\n  <td class=\"row-right\"><a href=\"{7}\">{8}</a></td>\n </tr>\n</table>\n<br>\n\n", args));
/*     */     
/*     */ 
/* 308 */     args = new Object[3];
/* 309 */     args[0] = smClient.getString("htmlHostManagerServlet.hostName");
/* 310 */     args[1] = smClient.getString("htmlHostManagerServlet.hostAliases");
/* 311 */     args[2] = smClient.getString("htmlHostManagerServlet.hostTasks");
/* 312 */     writer.print(MessageFormat.format("<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\">\n<tr>\n <td colspan=\"5\" class=\"title\">{0}</td>\n</tr>\n<tr>\n <td class=\"header-left\"><small>{0}</small></td>\n <td class=\"header-center\"><small>{1}</small></td>\n <td class=\"header-center\"><small>{2}</small></td>\n</tr>\n", args));
/*     */     
/*     */ 
/*     */ 
/* 316 */     Container[] children = this.engine.findChildren();
/* 317 */     String[] hostNames = new String[children.length];
/* 318 */     for (int i = 0; i < children.length; i++) {
/* 319 */       hostNames[i] = children[i].getName();
/*     */     }
/* 321 */     TreeMap<String, String> sortedHostNamesMap = new TreeMap();
/*     */     
/* 323 */     for (int i = 0; i < hostNames.length; i++) {
/* 324 */       String displayPath = hostNames[i];
/* 325 */       sortedHostNamesMap.put(displayPath, hostNames[i]);
/*     */     }
/*     */     
/*     */ 
/* 329 */     String hostsStart = smClient.getString("htmlHostManagerServlet.hostsStart");
/*     */     
/* 331 */     String hostsStop = smClient.getString("htmlHostManagerServlet.hostsStop");
/*     */     
/* 333 */     String hostsRemove = smClient.getString("htmlHostManagerServlet.hostsRemove");
/*     */     
/*     */ 
/* 336 */     Iterator<Map.Entry<String, String>> iterator = sortedHostNamesMap.entrySet().iterator();
/* 337 */     while (iterator.hasNext()) {
/* 338 */       Map.Entry<String, String> entry = (Map.Entry)iterator.next();
/* 339 */       String hostName = (String)entry.getKey();
/* 340 */       Host host = (Host)this.engine.findChild(hostName);
/*     */       
/* 342 */       if (host != null) {
/* 343 */         args = new Object[2];
/* 344 */         args[0] = Escape.htmlElementContent(hostName);
/* 345 */         String[] aliases = host.findAliases();
/* 346 */         StringBuilder buf = new StringBuilder();
/* 347 */         if (aliases.length > 0) {
/* 348 */           buf.append(aliases[0]);
/* 349 */           for (int j = 1; j < aliases.length; j++) {
/* 350 */             buf.append(", ").append(aliases[j]);
/*     */           }
/*     */         }
/*     */         
/* 354 */         if (buf.length() == 0) {
/* 355 */           buf.append("&nbsp;");
/* 356 */           args[1] = buf.toString();
/*     */         } else {
/* 358 */           args[1] = Escape.htmlElementContent(buf.toString());
/*     */         }
/*     */         
/*     */ 
/* 362 */         writer.print(MessageFormat.format("<tr>\n <td class=\"row-left\"><small><a href=\"http://{0}\">{0}</a></small></td>\n <td class=\"row-center\"><small>{1}</small></td>\n", args));
/*     */         
/* 364 */         args = new Object[4];
/* 365 */         if (host.getState().isAvailable())
/*     */         {
/* 367 */           args[0] = response.encodeURL(request.getContextPath() + "/html/stop?name=" + 
/*     */           
/* 369 */             URLEncoder.encode(hostName, "UTF-8"));
/* 370 */           args[1] = hostsStop;
/*     */         }
/*     */         else {
/* 373 */           args[0] = response.encodeURL(request.getContextPath() + "/html/start?name=" + 
/*     */           
/* 375 */             URLEncoder.encode(hostName, "UTF-8"));
/* 376 */           args[1] = hostsStart;
/*     */         }
/*     */         
/* 379 */         args[2] = response.encodeURL(request.getContextPath() + "/html/remove?name=" + 
/*     */         
/* 381 */           URLEncoder.encode(hostName, "UTF-8"));
/* 382 */         args[3] = hostsRemove;
/* 383 */         if (host == this.installedHost) {
/* 384 */           writer.print(MessageFormat.format(MANAGER_HOST_ROW_BUTTON_SECTION, args));
/*     */         }
/*     */         else {
/* 387 */           writer.print(MessageFormat.format(" <td class=\"row-left\" NOWRAP>\n  <form class=\"inline\" method=\"POST\" action=\"{0}\">   <small><input type=\"submit\" value=\"{1}\"></small>  </form>\n  <form class=\"inline\" method=\"POST\" action=\"{2}\">   <small><input type=\"submit\" value=\"{3}\"></small>  </form>\n </td>\n</tr>\n", args));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 394 */     args = new Object[6];
/* 395 */     args[0] = smClient.getString("htmlHostManagerServlet.addTitle");
/* 396 */     args[1] = smClient.getString("htmlHostManagerServlet.addHost");
/* 397 */     args[2] = response.encodeURL(request.getContextPath() + "/html/add");
/* 398 */     args[3] = smClient.getString("htmlHostManagerServlet.addName");
/* 399 */     args[4] = smClient.getString("htmlHostManagerServlet.addAliases");
/* 400 */     args[5] = smClient.getString("htmlHostManagerServlet.addAppBase");
/* 401 */     writer.print(MessageFormat.format("</table>\n<br>\n<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\">\n<tr>\n <td colspan=\"2\" class=\"title\">{0}</td>\n</tr>\n<tr>\n <td colspan=\"2\" class=\"header-left\"><small>{1}</small></td>\n</tr>\n<tr>\n <td colspan=\"2\">\n<form method=\"post\" action=\"{2}\">\n<table cellspacing=\"0\" cellpadding=\"3\">\n<tr>\n <td class=\"row-right\">\n  <small>{3}</small>\n </td>\n <td class=\"row-left\">\n  <input type=\"text\" name=\"name\" size=\"20\">\n </td>\n</tr>\n<tr>\n <td class=\"row-right\">\n  <small>{4}</small>\n </td>\n <td class=\"row-left\">\n  <input type=\"text\" name=\"aliases\" size=\"64\">\n </td>\n</tr>\n<tr>\n <td class=\"row-right\">\n  <small>{5}</small>\n </td>\n <td class=\"row-left\">\n  <input type=\"text\" name=\"appBase\" size=\"64\">\n </td>\n</tr>\n", args));
/*     */     
/* 403 */     args = new Object[3];
/* 404 */     args[0] = smClient.getString("htmlHostManagerServlet.addAutoDeploy");
/* 405 */     args[1] = "autoDeploy";
/* 406 */     args[2] = "checked";
/* 407 */     writer.print(MessageFormat.format("<tr>\n <td class=\"row-right\">\n  <small>{0}</small>\n </td>\n <td class=\"row-left\">\n  <input type=\"checkbox\" name=\"{1}\" {2}>\n </td>\n</tr>\n", args));
/* 408 */     args[0] = smClient.getString("htmlHostManagerServlet.addDeployOnStartup");
/*     */     
/* 410 */     args[1] = "deployOnStartup";
/* 411 */     args[2] = "checked";
/* 412 */     writer.print(MessageFormat.format("<tr>\n <td class=\"row-right\">\n  <small>{0}</small>\n </td>\n <td class=\"row-left\">\n  <input type=\"checkbox\" name=\"{1}\" {2}>\n </td>\n</tr>\n", args));
/* 413 */     args[0] = smClient.getString("htmlHostManagerServlet.addDeployXML");
/* 414 */     args[1] = "deployXML";
/* 415 */     args[2] = "checked";
/* 416 */     writer.print(MessageFormat.format("<tr>\n <td class=\"row-right\">\n  <small>{0}</small>\n </td>\n <td class=\"row-left\">\n  <input type=\"checkbox\" name=\"{1}\" {2}>\n </td>\n</tr>\n", args));
/* 417 */     args[0] = smClient.getString("htmlHostManagerServlet.addUnpackWARs");
/* 418 */     args[1] = "unpackWARs";
/* 419 */     args[2] = "checked";
/* 420 */     writer.print(MessageFormat.format("<tr>\n <td class=\"row-right\">\n  <small>{0}</small>\n </td>\n <td class=\"row-left\">\n  <input type=\"checkbox\" name=\"{1}\" {2}>\n </td>\n</tr>\n", args));
/*     */     
/* 422 */     args[0] = smClient.getString("htmlHostManagerServlet.addManager");
/* 423 */     args[1] = "manager";
/* 424 */     args[2] = "checked";
/* 425 */     writer.print(MessageFormat.format("<tr>\n <td class=\"row-right\">\n  <small>{0}</small>\n </td>\n <td class=\"row-left\">\n  <input type=\"checkbox\" name=\"{1}\" {2}>\n </td>\n</tr>\n", args));
/*     */     
/* 427 */     args[0] = smClient.getString("htmlHostManagerServlet.addCopyXML");
/* 428 */     args[1] = "copyXML";
/* 429 */     args[2] = "";
/* 430 */     writer.print(MessageFormat.format("<tr>\n <td class=\"row-right\">\n  <small>{0}</small>\n </td>\n <td class=\"row-left\">\n  <input type=\"checkbox\" name=\"{1}\" {2}>\n </td>\n</tr>\n", args));
/*     */     
/* 432 */     args = new Object[1];
/* 433 */     args[0] = smClient.getString("htmlHostManagerServlet.addButton");
/* 434 */     writer.print(MessageFormat.format("<tr>\n <td class=\"row-right\">\n  &nbsp;\n </td>\n <td class=\"row-left\">\n  <input type=\"submit\" value=\"{0}\">\n </td>\n</tr>\n</table>\n</form>\n</td>\n</tr>\n</table>\n<br>\n\n", args));
/*     */     
/*     */ 
/* 437 */     args = new Object[4];
/* 438 */     args[0] = smClient.getString("htmlHostManagerServlet.persistTitle");
/* 439 */     args[1] = response.encodeURL(request.getContextPath() + "/html/persist");
/* 440 */     args[2] = smClient.getString("htmlHostManagerServlet.persistAllButton");
/* 441 */     args[3] = smClient.getString("htmlHostManagerServlet.persistAll");
/* 442 */     writer.print(MessageFormat.format("<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\">\n<tr>\n <td class=\"title\">{0}</td>\n</tr>\n<tr>\n <td class=\"row-left\">\n  <form class=\"inline\" method=\"POST\" action=\"{1}\">   <small><input type=\"submit\" value=\"{2}\"></small>  </form> {3}\n </td>\n</tr>\n</table>\n<br>\n\n", args));
/*     */     
/*     */ 
/* 445 */     args = new Object[7];
/* 446 */     args[0] = smClient.getString("htmlHostManagerServlet.serverTitle");
/* 447 */     args[1] = smClient.getString("htmlHostManagerServlet.serverVersion");
/* 448 */     args[2] = smClient.getString("htmlHostManagerServlet.serverJVMVersion");
/* 449 */     args[3] = smClient.getString("htmlHostManagerServlet.serverJVMVendor");
/* 450 */     args[4] = smClient.getString("htmlHostManagerServlet.serverOSName");
/* 451 */     args[5] = smClient.getString("htmlHostManagerServlet.serverOSVersion");
/* 452 */     args[6] = smClient.getString("htmlHostManagerServlet.serverOSArch");
/* 453 */     writer.print(
/* 454 */       MessageFormat.format("<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\">\n<tr>\n <td colspan=\"6\" class=\"title\">{0}</td>\n</tr>\n<tr>\n <td class=\"header-center\"><small>{1}</small></td>\n <td class=\"header-center\"><small>{2}</small></td>\n <td class=\"header-center\"><small>{3}</small></td>\n <td class=\"header-center\"><small>{4}</small></td>\n <td class=\"header-center\"><small>{5}</small></td>\n <td class=\"header-center\"><small>{6}</small></td>\n</tr>\n", args));
/*     */     
/*     */ 
/* 457 */     args = new Object[6];
/* 458 */     args[0] = ServerInfo.getServerInfo();
/* 459 */     args[1] = System.getProperty("java.runtime.version");
/* 460 */     args[2] = System.getProperty("java.vm.vendor");
/* 461 */     args[3] = System.getProperty("os.name");
/* 462 */     args[4] = System.getProperty("os.version");
/* 463 */     args[5] = System.getProperty("os.arch");
/* 464 */     writer.print(MessageFormat.format("<tr>\n <td class=\"row-center\"><small>{0}</small></td>\n <td class=\"row-center\"><small>{1}</small></td>\n <td class=\"row-center\"><small>{2}</small></td>\n <td class=\"row-center\"><small>{3}</small></td>\n <td class=\"row-center\"><small>{4}</small></td>\n <td class=\"row-center\"><small>{5}</small></td>\n</tr>\n</table>\n<br>\n\n", args));
/*     */     
/*     */ 
/* 467 */     writer.print("<hr size=\"1\" noshade=\"noshade\">\n<center><font size=\"-1\" color=\"#525D76\">\n <em>Copyright &copy; 1999-2018, Apache Software Foundation</em></font></center>\n\n</body>\n</html>");
/*     */     
/*     */ 
/* 470 */     writer.flush();
/* 471 */     writer.close();
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
/* 498 */   private static final String MANAGER_HOST_ROW_BUTTON_SECTION = " <td class=\"row-left\">\n  <small>\n" + sm
/*     */   
/*     */ 
/* 501 */     .getString("htmlHostManagerServlet.hostThis") + "  </small>\n </td>\n</tr>\n";
/*     */   private static final String HOSTS_ROW_BUTTON_SECTION = " <td class=\"row-left\" NOWRAP>\n  <form class=\"inline\" method=\"POST\" action=\"{0}\">   <small><input type=\"submit\" value=\"{1}\"></small>  </form>\n  <form class=\"inline\" method=\"POST\" action=\"{2}\">   <small><input type=\"submit\" value=\"{3}\"></small>  </form>\n </td>\n</tr>\n";
/*     */   private static final String ADD_SECTION_START = "</table>\n<br>\n<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\">\n<tr>\n <td colspan=\"2\" class=\"title\">{0}</td>\n</tr>\n<tr>\n <td colspan=\"2\" class=\"header-left\"><small>{1}</small></td>\n</tr>\n<tr>\n <td colspan=\"2\">\n<form method=\"post\" action=\"{2}\">\n<table cellspacing=\"0\" cellpadding=\"3\">\n<tr>\n <td class=\"row-right\">\n  <small>{3}</small>\n </td>\n <td class=\"row-left\">\n  <input type=\"text\" name=\"name\" size=\"20\">\n </td>\n</tr>\n<tr>\n <td class=\"row-right\">\n  <small>{4}</small>\n </td>\n <td class=\"row-left\">\n  <input type=\"text\" name=\"aliases\" size=\"64\">\n </td>\n</tr>\n<tr>\n <td class=\"row-right\">\n  <small>{5}</small>\n </td>\n <td class=\"row-left\">\n  <input type=\"text\" name=\"appBase\" size=\"64\">\n </td>\n</tr>\n";
/*     */   private static final String ADD_SECTION_BOOLEAN = "<tr>\n <td class=\"row-right\">\n  <small>{0}</small>\n </td>\n <td class=\"row-left\">\n  <input type=\"checkbox\" name=\"{1}\" {2}>\n </td>\n</tr>\n";
/*     */   private static final String ADD_SECTION_END = "<tr>\n <td class=\"row-right\">\n  &nbsp;\n </td>\n <td class=\"row-left\">\n  <input type=\"submit\" value=\"{0}\">\n </td>\n</tr>\n</table>\n</form>\n</td>\n</tr>\n</table>\n<br>\n\n";
/*     */   private static final String PERSIST_SECTION = "<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\">\n<tr>\n <td class=\"title\">{0}</td>\n</tr>\n<tr>\n <td class=\"row-left\">\n  <form class=\"inline\" method=\"POST\" action=\"{1}\">   <small><input type=\"submit\" value=\"{2}\"></small>  </form> {3}\n </td>\n</tr>\n</table>\n<br>\n\n";
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\manager\host\HTMLHostManagerServlet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */