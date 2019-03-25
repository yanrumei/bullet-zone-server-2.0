/*      */ package org.apache.catalina.manager;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintWriter;
/*      */ import java.io.StringWriter;
/*      */ import java.net.InetAddress;
/*      */ import java.net.UnknownHostException;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.Date;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Set;
/*      */ import javax.servlet.RequestDispatcher;
/*      */ import javax.servlet.ServletConfig;
/*      */ import javax.servlet.ServletContext;
/*      */ import javax.servlet.ServletException;
/*      */ import javax.servlet.http.HttpServletRequest;
/*      */ import javax.servlet.http.HttpServletResponse;
/*      */ import javax.servlet.http.HttpSession;
/*      */ import javax.servlet.http.Part;
/*      */ import org.apache.catalina.Container;
/*      */ import org.apache.catalina.Context;
/*      */ import org.apache.catalina.DistributedManager;
/*      */ import org.apache.catalina.Host;
/*      */ import org.apache.catalina.LifecycleState;
/*      */ import org.apache.catalina.Manager;
/*      */ import org.apache.catalina.Session;
/*      */ import org.apache.catalina.manager.util.BaseSessionComparator;
/*      */ import org.apache.catalina.manager.util.SessionUtils;
/*      */ import org.apache.catalina.util.ContextName;
/*      */ import org.apache.catalina.util.ServerInfo;
/*      */ import org.apache.catalina.util.URLEncoder;
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
/*      */ public final class HTMLManagerServlet
/*      */   extends ManagerServlet
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*      */   static final String APPLICATION_MESSAGE = "message";
/*      */   static final String APPLICATION_ERROR = "error";
/*      */   static final String sessionsListJspPath = "/WEB-INF/jsp/sessionsList.jsp";
/*      */   static final String sessionDetailJspPath = "/WEB-INF/jsp/sessionDetail.jsp";
/*      */   static final String connectorCiphersJspPath = "/WEB-INF/jsp/connectorCiphers.jsp";
/*      */   static final String connectorCertsJspPath = "/WEB-INF/jsp/connectorCerts.jsp";
/*      */   static final String connectorTrustedCertsJspPath = "/WEB-INF/jsp/connectorTrustedCerts.jsp";
/*   91 */   private boolean showProxySessions = false;
/*      */   
/*      */   private static final String APPS_HEADER_SECTION = "<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\">\n<tr>\n <td colspan=\"6\" class=\"title\">{0}</td>\n</tr>\n<tr>\n <td class=\"header-left\"><small>{1}</small></td>\n <td class=\"header-left\"><small>{2}</small></td>\n <td class=\"header-center\"><small>{3}</small></td>\n <td class=\"header-center\"><small>{4}</small></td>\n <td class=\"header-left\"><small>{5}</small></td>\n <td class=\"header-left\"><small>{6}</small></td>\n</tr>\n";
/*      */   
/*      */   private static final String APPS_ROW_DETAILS_SECTION = "<tr>\n <td class=\"row-left\" bgcolor=\"{6}\" rowspan=\"2\"><small>{0}</small></td>\n <td class=\"row-left\" bgcolor=\"{6}\" rowspan=\"2\"><small>{1}</small></td>\n <td class=\"row-left\" bgcolor=\"{6}\" rowspan=\"2\"><small>{2}</small></td>\n <td class=\"row-center\" bgcolor=\"{6}\" rowspan=\"2\"><small>{3}</small></td>\n <td class=\"row-center\" bgcolor=\"{6}\" rowspan=\"2\"><small><a href=\"{4}\">{5}</a></small></td>\n";
/*      */   
/*      */   private static final String MANAGER_APP_ROW_BUTTON_SECTION = " <td class=\"row-left\" bgcolor=\"{13}\">\n  <small>\n  &nbsp;{1}&nbsp;\n  &nbsp;{3}&nbsp;\n  &nbsp;{5}&nbsp;\n  &nbsp;{7}&nbsp;\n  </small>\n </td>\n</tr><tr>\n <td class=\"row-left\" bgcolor=\"{13}\">\n  <form method=\"POST\" action=\"{8}\">\n  <small>\n  &nbsp;<input type=\"submit\" value=\"{9}\">&nbsp;{10}&nbsp;<input type=\"text\" name=\"idle\" size=\"5\" value=\"{11}\">&nbsp;{12}&nbsp;\n  </small>\n  </form>\n </td>\n</tr>\n";
/*      */   private static final String STARTED_DEPLOYED_APPS_ROW_BUTTON_SECTION = " <td class=\"row-left\" bgcolor=\"{13}\">\n  &nbsp;<small>{1}</small>&nbsp;\n  <form class=\"inline\" method=\"POST\" action=\"{2}\">  <small><input type=\"submit\" value=\"{3}\"></small>  </form>\n  <form class=\"inline\" method=\"POST\" action=\"{4}\">  <small><input type=\"submit\" value=\"{5}\"></small>  </form>\n  <form class=\"inline\" method=\"POST\" action=\"{6}\">  <small><input type=\"submit\" value=\"{7}\"></small>  </form>\n </td>\n </tr><tr>\n <td class=\"row-left\" bgcolor=\"{13}\">\n  <form method=\"POST\" action=\"{8}\">\n  <small>\n  &nbsp;<input type=\"submit\" value=\"{9}\">&nbsp;{10}&nbsp;<input type=\"text\" name=\"idle\" size=\"5\" value=\"{11}\">&nbsp;{12}&nbsp;\n  </small>\n  </form>\n </td>\n</tr>\n";
/*      */   private static final String STOPPED_DEPLOYED_APPS_ROW_BUTTON_SECTION = " <td class=\"row-left\" bgcolor=\"{13}\" rowspan=\"2\">\n  <form class=\"inline\" method=\"POST\" action=\"{0}\">  <small><input type=\"submit\" value=\"{1}\"></small>  </form>\n  &nbsp;<small>{3}</small>&nbsp;\n  &nbsp;<small>{5}</small>&nbsp;\n  <form class=\"inline\" method=\"POST\" action=\"{6}\">  <small><input type=\"submit\" value=\"{7}\"></small>  </form>\n </td>\n</tr>\n<tr></tr>\n";
/*      */   private static final String STARTED_NONDEPLOYED_APPS_ROW_BUTTON_SECTION = " <td class=\"row-left\" bgcolor=\"{13}\">\n  &nbsp;<small>{1}</small>&nbsp;\n  <form class=\"inline\" method=\"POST\" action=\"{2}\">  <small><input type=\"submit\" value=\"{3}\"></small>  </form>\n  <form class=\"inline\" method=\"POST\" action=\"{4}\">  <small><input type=\"submit\" value=\"{5}\"></small>  </form>\n  &nbsp;<small>{7}</small>&nbsp;\n </td>\n </tr><tr>\n <td class=\"row-left\" bgcolor=\"{13}\">\n  <form method=\"POST\" action=\"{8}\">\n  <small>\n  &nbsp;<input type=\"submit\" value=\"{9}\">&nbsp;{10}&nbsp;<input type=\"text\" name=\"idle\" size=\"5\" value=\"{11}\">&nbsp;{12}&nbsp;\n  </small>\n  </form>\n </td>\n</tr>\n";
/*      */   private static final String STOPPED_NONDEPLOYED_APPS_ROW_BUTTON_SECTION = " <td class=\"row-left\" bgcolor=\"{13}\" rowspan=\"2\">\n  <form class=\"inline\" method=\"POST\" action=\"{0}\">  <small><input type=\"submit\" value=\"{1}\"></small>  </form>\n  &nbsp;<small>{3}</small>&nbsp;\n  &nbsp;<small>{5}</small>&nbsp;\n  &nbsp;<small>{7}</small>&nbsp;\n </td>\n</tr>\n<tr></tr>\n";
/*      */   private static final String DEPLOY_SECTION = "</table>\n<br>\n<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\">\n<tr>\n <td colspan=\"2\" class=\"title\">{0}</td>\n</tr>\n<tr>\n <td colspan=\"2\" class=\"header-left\"><small>{1}</small></td>\n</tr>\n<tr>\n <td colspan=\"2\">\n<form method=\"post\" action=\"{2}\">\n<table cellspacing=\"0\" cellpadding=\"3\">\n<tr>\n <td class=\"row-right\">\n  <small>{3}</small>\n </td>\n <td class=\"row-left\">\n  <input type=\"text\" name=\"deployPath\" size=\"20\">\n </td>\n</tr>\n<tr>\n <td class=\"row-right\">\n  <small>{4}</small>\n </td>\n <td class=\"row-left\">\n  <input type=\"text\" name=\"deployConfig\" size=\"20\">\n </td>\n</tr>\n<tr>\n <td class=\"row-right\">\n  <small>{5}</small>\n </td>\n <td class=\"row-left\">\n  <input type=\"text\" name=\"deployWar\" size=\"40\">\n </td>\n</tr>\n<tr>\n <td class=\"row-right\">\n  &nbsp;\n </td>\n <td class=\"row-left\">\n  <input type=\"submit\" value=\"{6}\">\n </td>\n</tr>\n</table>\n</form>\n</td>\n</tr>\n";
/*      */   private static final String UPLOAD_SECTION = "<tr>\n <td colspan=\"2\" class=\"header-left\"><small>{0}</small></td>\n</tr>\n<tr>\n <td colspan=\"2\">\n<form method=\"post\" action=\"{1}\" enctype=\"multipart/form-data\">\n<table cellspacing=\"0\" cellpadding=\"3\">\n<tr>\n <td class=\"row-right\">\n  <small>{2}</small>\n </td>\n <td class=\"row-left\">\n  <input type=\"file\" name=\"deployWar\" size=\"40\">\n </td>\n</tr>\n<tr>\n <td class=\"row-right\">\n  &nbsp;\n </td>\n <td class=\"row-left\">\n  <input type=\"submit\" value=\"{3}\">\n </td>\n</tr>\n</table>\n</form>\n</td>\n</tr>\n</table>\n<br>\n\n";
/*      */   private static final String DIAGNOSTICS_SECTION = "<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\">\n<tr>\n <td colspan=\"2\" class=\"title\">{0}</td>\n</tr>\n<tr>\n <td colspan=\"2\" class=\"header-left\"><small>{1}</small></td>\n</tr>\n<tr>\n <td class=\"row-left\">\n  <form method=\"post\" action=\"{2}\">\n   <input type=\"submit\" value=\"{4}\">\n  </form>\n </td>\n <td class=\"row-left\">\n  <small>{3}</small>\n </td>\n</tr>\n<tr>\n <td colspan=\"2\" class=\"header-left\"><small>{5}</small></td>\n</tr>\n<tr>\n <td class=\"row-left\">\n  <form method=\"post\" action=\"{6}\">\n   <input type=\"submit\" value=\"{7}\">\n  </form>\n </td>\n <td class=\"row-left\">\n  <small>{8}</small>\n </td>\n</tr>\n<tr>\n <td class=\"row-left\">\n  <form method=\"post\" action=\"{9}\">\n   <input type=\"submit\" value=\"{10}\">\n  </form>\n </td>\n <td class=\"row-left\">\n  <small>{11}</small>\n </td>\n</tr>\n<tr>\n <td class=\"row-left\">\n  <form method=\"post\" action=\"{12}\">\n   <input type=\"submit\" value=\"{13}\">\n  </form>\n </td>\n <td class=\"row-left\">\n  <small>{14}</small>\n </td>\n</tr>\n</table>\n<br>";
/*      */   
/*      */   public void doGet(HttpServletRequest request, HttpServletResponse response)
/*      */     throws IOException, ServletException
/*      */   {
/*  109 */     StringManager smClient = StringManager.getManager("org.apache.catalina.manager", request
/*  110 */       .getLocales());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  115 */     String command = request.getPathInfo();
/*      */     
/*  117 */     String path = request.getParameter("path");
/*  118 */     ContextName cn = null;
/*  119 */     if (path != null) {
/*  120 */       cn = new ContextName(path, request.getParameter("version"));
/*      */     }
/*      */     
/*      */ 
/*  124 */     response.setContentType("text/html; charset=utf-8");
/*      */     
/*  126 */     String message = "";
/*      */     
/*  128 */     if ((command != null) && (!command.equals("/")))
/*      */     {
/*  130 */       if (!command.equals("/list"))
/*      */       {
/*  132 */         if (command.equals("/sessions")) {
/*      */           try {
/*  134 */             doSessions(cn, request, response, smClient);
/*  135 */             return;
/*      */           } catch (Exception e) {
/*  137 */             log("HTMLManagerServlet.sessions[" + cn + "]", e);
/*  138 */             message = smClient.getString("managerServlet.exception", new Object[] {e
/*  139 */               .toString() });
/*      */           }
/*  141 */         } else if (command.equals("/sslConnectorCiphers")) {
/*  142 */           sslConnectorCiphers(request, response);
/*  143 */         } else if (command.equals("/sslConnectorCerts")) {
/*  144 */           sslConnectorCerts(request, response);
/*  145 */         } else if (command.equals("/sslConnectorTrustedCerts")) {
/*  146 */           sslConnectorTrustedCerts(request, response);
/*  147 */         } else if ((command.equals("/upload")) || (command.equals("/deploy")) || 
/*  148 */           (command.equals("/reload")) || (command.equals("/undeploy")) || 
/*  149 */           (command.equals("/expire")) || (command.equals("/start")) || 
/*  150 */           (command.equals("/stop")))
/*      */         {
/*  152 */           message = smClient.getString("managerServlet.postCommand", new Object[] { command });
/*      */         }
/*      */         else
/*  155 */           message = smClient.getString("managerServlet.unknownCommand", new Object[] { command });
/*      */       }
/*      */     }
/*  158 */     list(request, response, message, smClient);
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
/*      */   public void doPost(HttpServletRequest request, HttpServletResponse response)
/*      */     throws IOException, ServletException
/*      */   {
/*  175 */     StringManager smClient = StringManager.getManager("org.apache.catalina.manager", request
/*  176 */       .getLocales());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  181 */     String command = request.getPathInfo();
/*      */     
/*  183 */     String path = request.getParameter("path");
/*  184 */     ContextName cn = null;
/*  185 */     if (path != null) {
/*  186 */       cn = new ContextName(path, request.getParameter("version"));
/*      */     }
/*  188 */     String deployPath = request.getParameter("deployPath");
/*  189 */     ContextName deployCn = null;
/*  190 */     if (deployPath != null)
/*      */     {
/*  192 */       deployCn = new ContextName(deployPath, request.getParameter("deployVersion"));
/*      */     }
/*  194 */     String deployConfig = request.getParameter("deployConfig");
/*  195 */     String deployWar = request.getParameter("deployWar");
/*      */     
/*      */ 
/*  198 */     response.setContentType("text/html; charset=utf-8");
/*      */     
/*  200 */     String message = "";
/*      */     
/*  202 */     if ((command != null) && (command.length() != 0))
/*      */     {
/*      */ 
/*  205 */       if (command.equals("/upload")) {
/*  206 */         message = upload(request, smClient);
/*  207 */       } else if (command.equals("/deploy")) {
/*  208 */         message = deployInternal(deployConfig, deployCn, deployWar, smClient);
/*      */       }
/*  210 */       else if (command.equals("/reload")) {
/*  211 */         message = reload(cn, smClient);
/*  212 */       } else if (command.equals("/undeploy")) {
/*  213 */         message = undeploy(cn, smClient);
/*  214 */       } else if (command.equals("/expire")) {
/*  215 */         message = expireSessions(cn, request, smClient);
/*  216 */       } else if (command.equals("/start")) {
/*  217 */         message = start(cn, smClient);
/*  218 */       } else if (command.equals("/stop")) {
/*  219 */         message = stop(cn, smClient);
/*  220 */       } else if (command.equals("/findleaks")) {
/*  221 */         message = findleaks(smClient);
/*      */       }
/*      */       else {
/*  224 */         doGet(request, response);
/*  225 */         return;
/*      */       }
/*      */     }
/*  228 */     list(request, response, message, smClient);
/*      */   }
/*      */   
/*      */   protected String upload(HttpServletRequest request, StringManager smClient) {
/*  232 */     String message = "";
/*      */     
/*      */     try
/*      */     {
/*  236 */       Part warPart = request.getPart("deployWar");
/*  237 */       if (warPart == null) {
/*  238 */         message = smClient.getString("htmlManagerServlet.deployUploadNoFile");
/*      */       }
/*      */       else
/*      */       {
/*  242 */         String filename = warPart.getSubmittedFileName();
/*  243 */         if (!filename.toLowerCase(Locale.ENGLISH).endsWith(".war")) {
/*  244 */           message = smClient.getString("htmlManagerServlet.deployUploadNotWar", new Object[] { filename });
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*  249 */           if (filename.lastIndexOf('\\') >= 0)
/*      */           {
/*  251 */             filename = filename.substring(filename.lastIndexOf('\\') + 1);
/*      */           }
/*  253 */           if (filename.lastIndexOf('/') >= 0)
/*      */           {
/*  255 */             filename = filename.substring(filename.lastIndexOf('/') + 1);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*  260 */           File file = new File(this.host.getAppBaseFile(), filename);
/*  261 */           if (file.exists()) {
/*  262 */             message = smClient.getString("htmlManagerServlet.deployUploadWarExists", new Object[] { filename });
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*      */ 
/*  268 */             ContextName cn = new ContextName(filename, true);
/*  269 */             String name = cn.getName();
/*      */             
/*  271 */             if ((this.host.findChild(name) != null) && (!isDeployed(name))) {
/*  272 */               message = smClient.getString("htmlManagerServlet.deployUploadInServerXml", new Object[] { filename });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             }
/*  278 */             else if (isServiced(name)) {
/*  279 */               message = smClient.getString("managerServlet.inService", new Object[] { name });
/*      */             } else {
/*  281 */               addServiced(name);
/*      */               try {
/*  283 */                 warPart.write(file.getAbsolutePath());
/*      */                 
/*  285 */                 check(name);
/*      */               } finally {
/*  287 */                 removeServiced(name);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     } catch (Exception e) {
/*  294 */       message = smClient.getString("htmlManagerServlet.deployUploadFail", new Object[] { e.getMessage() });
/*  295 */       log(message, e);
/*      */     }
/*  297 */     return message;
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
/*      */   protected String deployInternal(String config, ContextName cn, String war, StringManager smClient)
/*      */   {
/*  313 */     StringWriter stringWriter = new StringWriter();
/*  314 */     PrintWriter printWriter = new PrintWriter(stringWriter);
/*      */     
/*  316 */     super.deploy(printWriter, config, cn, war, false, smClient);
/*      */     
/*  318 */     return stringWriter.toString();
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
/*      */   protected void list(HttpServletRequest request, HttpServletResponse response, String message, StringManager smClient)
/*      */     throws IOException
/*      */   {
/*  336 */     if (this.debug >= 1) {
/*  337 */       log("list: Listing contexts for virtual host '" + this.host
/*  338 */         .getName() + "'");
/*      */     }
/*  340 */     PrintWriter writer = response.getWriter();
/*      */     
/*      */ 
/*  343 */     writer.print(Constants.HTML_HEADER_SECTION);
/*      */     
/*      */ 
/*  346 */     Object[] args = new Object[2];
/*  347 */     args[0] = request.getContextPath();
/*  348 */     args[1] = smClient.getString("htmlManagerServlet.title");
/*  349 */     writer.print(
/*  350 */       MessageFormat.format(Constants.BODY_HEADER_SECTION, args));
/*      */     
/*      */ 
/*  353 */     args = new Object[3];
/*  354 */     args[0] = smClient.getString("htmlManagerServlet.messageLabel");
/*  355 */     if ((message == null) || (message.length() == 0)) {
/*  356 */       args[1] = "OK";
/*      */     } else {
/*  358 */       args[1] = Escape.htmlElementContent(message);
/*      */     }
/*  360 */     writer.print(MessageFormat.format(Constants.MESSAGE_SECTION, args));
/*      */     
/*      */ 
/*  363 */     args = new Object[9];
/*  364 */     args[0] = smClient.getString("htmlManagerServlet.manager");
/*  365 */     args[1] = response.encodeURL(request.getContextPath() + "/html/list");
/*  366 */     args[2] = smClient.getString("htmlManagerServlet.list");
/*  367 */     args[3] = response
/*  368 */       .encodeURL(request.getContextPath() + "/" + smClient
/*  369 */       .getString("htmlManagerServlet.helpHtmlManagerFile"));
/*  370 */     args[4] = smClient.getString("htmlManagerServlet.helpHtmlManager");
/*  371 */     args[5] = response
/*  372 */       .encodeURL(request.getContextPath() + "/" + smClient
/*  373 */       .getString("htmlManagerServlet.helpManagerFile"));
/*  374 */     args[6] = smClient.getString("htmlManagerServlet.helpManager");
/*  375 */     args[7] = response
/*  376 */       .encodeURL(request.getContextPath() + "/status");
/*  377 */     args[8] = smClient.getString("statusServlet.title");
/*  378 */     writer.print(MessageFormat.format(Constants.MANAGER_SECTION, args));
/*      */     
/*      */ 
/*  381 */     args = new Object[7];
/*  382 */     args[0] = smClient.getString("htmlManagerServlet.appsTitle");
/*  383 */     args[1] = smClient.getString("htmlManagerServlet.appsPath");
/*  384 */     args[2] = smClient.getString("htmlManagerServlet.appsVersion");
/*  385 */     args[3] = smClient.getString("htmlManagerServlet.appsName");
/*  386 */     args[4] = smClient.getString("htmlManagerServlet.appsAvailable");
/*  387 */     args[5] = smClient.getString("htmlManagerServlet.appsSessions");
/*  388 */     args[6] = smClient.getString("htmlManagerServlet.appsTasks");
/*  389 */     writer.print(MessageFormat.format("<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\">\n<tr>\n <td colspan=\"6\" class=\"title\">{0}</td>\n</tr>\n<tr>\n <td class=\"header-left\"><small>{1}</small></td>\n <td class=\"header-left\"><small>{2}</small></td>\n <td class=\"header-center\"><small>{3}</small></td>\n <td class=\"header-center\"><small>{4}</small></td>\n <td class=\"header-left\"><small>{5}</small></td>\n <td class=\"header-left\"><small>{6}</small></td>\n</tr>\n", args));
/*      */     
/*      */ 
/*      */ 
/*  393 */     Container[] children = this.host.findChildren();
/*  394 */     String[] contextNames = new String[children.length];
/*  395 */     for (int i = 0; i < children.length; i++) {
/*  396 */       contextNames[i] = children[i].getName();
/*      */     }
/*  398 */     Arrays.sort(contextNames);
/*      */     
/*  400 */     String appsStart = smClient.getString("htmlManagerServlet.appsStart");
/*  401 */     String appsStop = smClient.getString("htmlManagerServlet.appsStop");
/*  402 */     String appsReload = smClient.getString("htmlManagerServlet.appsReload");
/*      */     
/*  404 */     String appsUndeploy = smClient.getString("htmlManagerServlet.appsUndeploy");
/*  405 */     String appsExpire = smClient.getString("htmlManagerServlet.appsExpire");
/*      */     
/*  407 */     String noVersion = "<i>" + smClient.getString("htmlManagerServlet.noVersion") + "</i>";
/*      */     
/*  409 */     boolean isHighlighted = true;
/*  410 */     boolean isDeployed = true;
/*  411 */     String highlightColor = null;
/*      */     
/*  413 */     for (String contextName : contextNames) {
/*  414 */       Context ctxt = (Context)this.host.findChild(contextName);
/*      */       
/*  416 */       if (ctxt != null)
/*      */       {
/*  418 */         isHighlighted = !isHighlighted;
/*  419 */         if (isHighlighted) {
/*  420 */           highlightColor = "#C3F3C3";
/*      */         } else {
/*  422 */           highlightColor = "#FFFFFF";
/*      */         }
/*      */         
/*  425 */         String contextPath = ctxt.getPath();
/*  426 */         String displayPath = contextPath;
/*  427 */         if (displayPath.equals("")) {
/*  428 */           displayPath = "/";
/*      */         }
/*      */         
/*  431 */         StringBuilder tmp = new StringBuilder();
/*  432 */         tmp.append("path=");
/*  433 */         tmp.append(URLEncoder.DEFAULT.encode(displayPath, StandardCharsets.UTF_8));
/*  434 */         if (ctxt.getWebappVersion().length() > 0) {
/*  435 */           tmp.append("&version=");
/*  436 */           tmp.append(URLEncoder.DEFAULT.encode(ctxt
/*  437 */             .getWebappVersion(), StandardCharsets.UTF_8));
/*      */         }
/*  439 */         String pathVersion = tmp.toString();
/*      */         try
/*      */         {
/*  442 */           isDeployed = isDeployed(contextName);
/*      */         }
/*      */         catch (Exception e) {
/*  445 */           isDeployed = false;
/*      */         }
/*      */         
/*  448 */         args = new Object[7];
/*  449 */         args[0] = 
/*      */         
/*  451 */           ("<a href=\"" + URLEncoder.DEFAULT.encode(new StringBuilder().append(contextPath).append("/").toString(), StandardCharsets.UTF_8) + "\">" + Escape.htmlElementContent(displayPath) + "</a>");
/*  452 */         if ("".equals(ctxt.getWebappVersion())) {
/*  453 */           args[1] = noVersion;
/*      */         } else {
/*  455 */           args[1] = Escape.htmlElementContent(ctxt.getWebappVersion());
/*      */         }
/*  457 */         if (ctxt.getDisplayName() == null) {
/*  458 */           args[2] = "&nbsp;";
/*      */         } else {
/*  460 */           args[2] = Escape.htmlElementContent(ctxt.getDisplayName());
/*      */         }
/*  462 */         args[3] = Boolean.valueOf(ctxt.getState().isAvailable());
/*  463 */         args[4] = Escape.htmlElementContent(response.encodeURL(request.getContextPath() + "/html/sessions?" + pathVersion));
/*      */         
/*  465 */         Manager manager = ctxt.getManager();
/*  466 */         if (((manager instanceof DistributedManager)) && (this.showProxySessions)) {
/*  467 */           args[5] = Integer.valueOf(((DistributedManager)manager)
/*  468 */             .getActiveSessionsFull());
/*  469 */         } else if (manager != null) {
/*  470 */           args[5] = Integer.valueOf(manager.getActiveSessions());
/*      */         } else {
/*  472 */           args[5] = Integer.valueOf(0);
/*      */         }
/*      */         
/*  475 */         args[6] = highlightColor;
/*      */         
/*  477 */         writer
/*  478 */           .print(MessageFormat.format("<tr>\n <td class=\"row-left\" bgcolor=\"{6}\" rowspan=\"2\"><small>{0}</small></td>\n <td class=\"row-left\" bgcolor=\"{6}\" rowspan=\"2\"><small>{1}</small></td>\n <td class=\"row-left\" bgcolor=\"{6}\" rowspan=\"2\"><small>{2}</small></td>\n <td class=\"row-center\" bgcolor=\"{6}\" rowspan=\"2\"><small>{3}</small></td>\n <td class=\"row-center\" bgcolor=\"{6}\" rowspan=\"2\"><small><a href=\"{4}\">{5}</a></small></td>\n", args));
/*      */         
/*  480 */         args = new Object[14];
/*  481 */         args[0] = Escape.htmlElementContent(response.encodeURL(request
/*  482 */           .getContextPath() + "/html/start?" + pathVersion));
/*  483 */         args[1] = appsStart;
/*  484 */         args[2] = Escape.htmlElementContent(response.encodeURL(request
/*  485 */           .getContextPath() + "/html/stop?" + pathVersion));
/*  486 */         args[3] = appsStop;
/*  487 */         args[4] = Escape.htmlElementContent(response.encodeURL(request
/*  488 */           .getContextPath() + "/html/reload?" + pathVersion));
/*  489 */         args[5] = appsReload;
/*  490 */         args[6] = Escape.htmlElementContent(response.encodeURL(request
/*  491 */           .getContextPath() + "/html/undeploy?" + pathVersion));
/*  492 */         args[7] = appsUndeploy;
/*  493 */         args[8] = Escape.htmlElementContent(response.encodeURL(request
/*  494 */           .getContextPath() + "/html/expire?" + pathVersion));
/*  495 */         args[9] = appsExpire;
/*  496 */         args[10] = smClient.getString("htmlManagerServlet.expire.explain");
/*  497 */         if (manager == null) {
/*  498 */           args[11] = smClient.getString("htmlManagerServlet.noManager");
/*      */         } else {
/*  500 */           args[11] = Integer.valueOf(ctxt.getSessionTimeout());
/*      */         }
/*  502 */         args[12] = smClient.getString("htmlManagerServlet.expire.unit");
/*  503 */         args[13] = highlightColor;
/*      */         
/*  505 */         if (ctxt.getName().equals(this.context.getName())) {
/*  506 */           writer.print(MessageFormat.format(" <td class=\"row-left\" bgcolor=\"{13}\">\n  <small>\n  &nbsp;{1}&nbsp;\n  &nbsp;{3}&nbsp;\n  &nbsp;{5}&nbsp;\n  &nbsp;{7}&nbsp;\n  </small>\n </td>\n</tr><tr>\n <td class=\"row-left\" bgcolor=\"{13}\">\n  <form method=\"POST\" action=\"{8}\">\n  <small>\n  &nbsp;<input type=\"submit\" value=\"{9}\">&nbsp;{10}&nbsp;<input type=\"text\" name=\"idle\" size=\"5\" value=\"{11}\">&nbsp;{12}&nbsp;\n  </small>\n  </form>\n </td>\n</tr>\n", args));
/*      */         }
/*  508 */         else if ((ctxt.getState().isAvailable()) && (isDeployed)) {
/*  509 */           writer.print(MessageFormat.format(" <td class=\"row-left\" bgcolor=\"{13}\">\n  &nbsp;<small>{1}</small>&nbsp;\n  <form class=\"inline\" method=\"POST\" action=\"{2}\">  <small><input type=\"submit\" value=\"{3}\"></small>  </form>\n  <form class=\"inline\" method=\"POST\" action=\"{4}\">  <small><input type=\"submit\" value=\"{5}\"></small>  </form>\n  <form class=\"inline\" method=\"POST\" action=\"{6}\">  <small><input type=\"submit\" value=\"{7}\"></small>  </form>\n </td>\n </tr><tr>\n <td class=\"row-left\" bgcolor=\"{13}\">\n  <form method=\"POST\" action=\"{8}\">\n  <small>\n  &nbsp;<input type=\"submit\" value=\"{9}\">&nbsp;{10}&nbsp;<input type=\"text\" name=\"idle\" size=\"5\" value=\"{11}\">&nbsp;{12}&nbsp;\n  </small>\n  </form>\n </td>\n</tr>\n", args));
/*      */         }
/*  511 */         else if ((ctxt.getState().isAvailable()) && (!isDeployed)) {
/*  512 */           writer.print(MessageFormat.format(" <td class=\"row-left\" bgcolor=\"{13}\">\n  &nbsp;<small>{1}</small>&nbsp;\n  <form class=\"inline\" method=\"POST\" action=\"{2}\">  <small><input type=\"submit\" value=\"{3}\"></small>  </form>\n  <form class=\"inline\" method=\"POST\" action=\"{4}\">  <small><input type=\"submit\" value=\"{5}\"></small>  </form>\n  &nbsp;<small>{7}</small>&nbsp;\n </td>\n </tr><tr>\n <td class=\"row-left\" bgcolor=\"{13}\">\n  <form method=\"POST\" action=\"{8}\">\n  <small>\n  &nbsp;<input type=\"submit\" value=\"{9}\">&nbsp;{10}&nbsp;<input type=\"text\" name=\"idle\" size=\"5\" value=\"{11}\">&nbsp;{12}&nbsp;\n  </small>\n  </form>\n </td>\n</tr>\n", args));
/*      */         }
/*  514 */         else if ((!ctxt.getState().isAvailable()) && (isDeployed)) {
/*  515 */           writer.print(MessageFormat.format(" <td class=\"row-left\" bgcolor=\"{13}\" rowspan=\"2\">\n  <form class=\"inline\" method=\"POST\" action=\"{0}\">  <small><input type=\"submit\" value=\"{1}\"></small>  </form>\n  &nbsp;<small>{3}</small>&nbsp;\n  &nbsp;<small>{5}</small>&nbsp;\n  <form class=\"inline\" method=\"POST\" action=\"{6}\">  <small><input type=\"submit\" value=\"{7}\"></small>  </form>\n </td>\n</tr>\n<tr></tr>\n", args));
/*      */         }
/*      */         else {
/*  518 */           writer.print(MessageFormat.format(" <td class=\"row-left\" bgcolor=\"{13}\" rowspan=\"2\">\n  <form class=\"inline\" method=\"POST\" action=\"{0}\">  <small><input type=\"submit\" value=\"{1}\"></small>  </form>\n  &nbsp;<small>{3}</small>&nbsp;\n  &nbsp;<small>{5}</small>&nbsp;\n  &nbsp;<small>{7}</small>&nbsp;\n </td>\n</tr>\n<tr></tr>\n", args));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  526 */     args = new Object[7];
/*  527 */     args[0] = smClient.getString("htmlManagerServlet.deployTitle");
/*  528 */     args[1] = smClient.getString("htmlManagerServlet.deployServer");
/*  529 */     args[2] = response.encodeURL(request.getContextPath() + "/html/deploy");
/*  530 */     args[3] = smClient.getString("htmlManagerServlet.deployPath");
/*  531 */     args[4] = smClient.getString("htmlManagerServlet.deployConfig");
/*  532 */     args[5] = smClient.getString("htmlManagerServlet.deployWar");
/*  533 */     args[6] = smClient.getString("htmlManagerServlet.deployButton");
/*  534 */     writer.print(MessageFormat.format("</table>\n<br>\n<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\">\n<tr>\n <td colspan=\"2\" class=\"title\">{0}</td>\n</tr>\n<tr>\n <td colspan=\"2\" class=\"header-left\"><small>{1}</small></td>\n</tr>\n<tr>\n <td colspan=\"2\">\n<form method=\"post\" action=\"{2}\">\n<table cellspacing=\"0\" cellpadding=\"3\">\n<tr>\n <td class=\"row-right\">\n  <small>{3}</small>\n </td>\n <td class=\"row-left\">\n  <input type=\"text\" name=\"deployPath\" size=\"20\">\n </td>\n</tr>\n<tr>\n <td class=\"row-right\">\n  <small>{4}</small>\n </td>\n <td class=\"row-left\">\n  <input type=\"text\" name=\"deployConfig\" size=\"20\">\n </td>\n</tr>\n<tr>\n <td class=\"row-right\">\n  <small>{5}</small>\n </td>\n <td class=\"row-left\">\n  <input type=\"text\" name=\"deployWar\" size=\"40\">\n </td>\n</tr>\n<tr>\n <td class=\"row-right\">\n  &nbsp;\n </td>\n <td class=\"row-left\">\n  <input type=\"submit\" value=\"{6}\">\n </td>\n</tr>\n</table>\n</form>\n</td>\n</tr>\n", args));
/*      */     
/*  536 */     args = new Object[4];
/*  537 */     args[0] = smClient.getString("htmlManagerServlet.deployUpload");
/*  538 */     args[1] = response.encodeURL(request.getContextPath() + "/html/upload");
/*  539 */     args[2] = smClient.getString("htmlManagerServlet.deployUploadFile");
/*  540 */     args[3] = smClient.getString("htmlManagerServlet.deployButton");
/*  541 */     writer.print(MessageFormat.format("<tr>\n <td colspan=\"2\" class=\"header-left\"><small>{0}</small></td>\n</tr>\n<tr>\n <td colspan=\"2\">\n<form method=\"post\" action=\"{1}\" enctype=\"multipart/form-data\">\n<table cellspacing=\"0\" cellpadding=\"3\">\n<tr>\n <td class=\"row-right\">\n  <small>{2}</small>\n </td>\n <td class=\"row-left\">\n  <input type=\"file\" name=\"deployWar\" size=\"40\">\n </td>\n</tr>\n<tr>\n <td class=\"row-right\">\n  &nbsp;\n </td>\n <td class=\"row-left\">\n  <input type=\"submit\" value=\"{3}\">\n </td>\n</tr>\n</table>\n</form>\n</td>\n</tr>\n</table>\n<br>\n\n", args));
/*      */     
/*      */ 
/*  544 */     args = new Object[15];
/*  545 */     args[0] = smClient.getString("htmlManagerServlet.diagnosticsTitle");
/*  546 */     args[1] = smClient.getString("htmlManagerServlet.diagnosticsLeak");
/*  547 */     args[2] = response.encodeURL(request
/*  548 */       .getContextPath() + "/html/findleaks");
/*  549 */     args[3] = smClient.getString("htmlManagerServlet.diagnosticsLeakWarning");
/*  550 */     args[4] = smClient.getString("htmlManagerServlet.diagnosticsLeakButton");
/*  551 */     args[5] = smClient.getString("htmlManagerServlet.diagnosticsSsl");
/*  552 */     args[6] = response.encodeURL(request
/*  553 */       .getContextPath() + "/html/sslConnectorCiphers");
/*  554 */     args[7] = smClient.getString("htmlManagerServlet.diagnosticsSslConnectorCipherButton");
/*  555 */     args[8] = smClient.getString("htmlManagerServlet.diagnosticsSslConnectorCipherText");
/*  556 */     args[9] = response.encodeURL(request
/*  557 */       .getContextPath() + "/html/sslConnectorCerts");
/*  558 */     args[10] = smClient.getString("htmlManagerServlet.diagnosticsSslConnectorCertsButton");
/*  559 */     args[11] = smClient.getString("htmlManagerServlet.diagnosticsSslConnectorCertsText");
/*  560 */     args[12] = response.encodeURL(request
/*  561 */       .getContextPath() + "/html/sslConnectorTrustedCerts");
/*  562 */     args[13] = smClient.getString("htmlManagerServlet.diagnosticsSslConnectorTrustedCertsButton");
/*  563 */     args[14] = smClient.getString("htmlManagerServlet.diagnosticsSslConnectorTrustedCertsText");
/*  564 */     writer.print(MessageFormat.format("<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\">\n<tr>\n <td colspan=\"2\" class=\"title\">{0}</td>\n</tr>\n<tr>\n <td colspan=\"2\" class=\"header-left\"><small>{1}</small></td>\n</tr>\n<tr>\n <td class=\"row-left\">\n  <form method=\"post\" action=\"{2}\">\n   <input type=\"submit\" value=\"{4}\">\n  </form>\n </td>\n <td class=\"row-left\">\n  <small>{3}</small>\n </td>\n</tr>\n<tr>\n <td colspan=\"2\" class=\"header-left\"><small>{5}</small></td>\n</tr>\n<tr>\n <td class=\"row-left\">\n  <form method=\"post\" action=\"{6}\">\n   <input type=\"submit\" value=\"{7}\">\n  </form>\n </td>\n <td class=\"row-left\">\n  <small>{8}</small>\n </td>\n</tr>\n<tr>\n <td class=\"row-left\">\n  <form method=\"post\" action=\"{9}\">\n   <input type=\"submit\" value=\"{10}\">\n  </form>\n </td>\n <td class=\"row-left\">\n  <small>{11}</small>\n </td>\n</tr>\n<tr>\n <td class=\"row-left\">\n  <form method=\"post\" action=\"{12}\">\n   <input type=\"submit\" value=\"{13}\">\n  </form>\n </td>\n <td class=\"row-left\">\n  <small>{14}</small>\n </td>\n</tr>\n</table>\n<br>", args));
/*      */     
/*      */ 
/*  567 */     args = new Object[9];
/*  568 */     args[0] = smClient.getString("htmlManagerServlet.serverTitle");
/*  569 */     args[1] = smClient.getString("htmlManagerServlet.serverVersion");
/*  570 */     args[2] = smClient.getString("htmlManagerServlet.serverJVMVersion");
/*  571 */     args[3] = smClient.getString("htmlManagerServlet.serverJVMVendor");
/*  572 */     args[4] = smClient.getString("htmlManagerServlet.serverOSName");
/*  573 */     args[5] = smClient.getString("htmlManagerServlet.serverOSVersion");
/*  574 */     args[6] = smClient.getString("htmlManagerServlet.serverOSArch");
/*  575 */     args[7] = smClient.getString("htmlManagerServlet.serverHostname");
/*  576 */     args[8] = smClient.getString("htmlManagerServlet.serverIPAddress");
/*  577 */     writer.print(
/*  578 */       MessageFormat.format(Constants.SERVER_HEADER_SECTION, args));
/*      */     
/*      */ 
/*  581 */     args = new Object[8];
/*  582 */     args[0] = ServerInfo.getServerInfo();
/*  583 */     args[1] = System.getProperty("java.runtime.version");
/*  584 */     args[2] = System.getProperty("java.vm.vendor");
/*  585 */     args[3] = System.getProperty("os.name");
/*  586 */     args[4] = System.getProperty("os.version");
/*  587 */     args[5] = System.getProperty("os.arch");
/*      */     try {
/*  589 */       InetAddress address = InetAddress.getLocalHost();
/*  590 */       args[6] = address.getHostName();
/*  591 */       args[7] = address.getHostAddress();
/*      */     } catch (UnknownHostException e) {
/*  593 */       args[6] = "-";
/*  594 */       args[7] = "-";
/*      */     }
/*  596 */     writer.print(MessageFormat.format(Constants.SERVER_ROW_SECTION, args));
/*      */     
/*      */ 
/*  599 */     writer.print(Constants.HTML_TAIL_SECTION);
/*      */     
/*      */ 
/*  602 */     writer.flush();
/*  603 */     writer.close();
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
/*      */   protected String reload(ContextName cn, StringManager smClient)
/*      */   {
/*  617 */     StringWriter stringWriter = new StringWriter();
/*  618 */     PrintWriter printWriter = new PrintWriter(stringWriter);
/*      */     
/*  620 */     super.reload(printWriter, cn, smClient);
/*      */     
/*  622 */     return stringWriter.toString();
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
/*      */   protected String undeploy(ContextName cn, StringManager smClient)
/*      */   {
/*  636 */     StringWriter stringWriter = new StringWriter();
/*  637 */     PrintWriter printWriter = new PrintWriter(stringWriter);
/*      */     
/*  639 */     super.undeploy(printWriter, cn, smClient);
/*      */     
/*  641 */     return stringWriter.toString();
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
/*      */   protected String sessions(ContextName cn, int idle, StringManager smClient)
/*      */   {
/*  657 */     StringWriter stringWriter = new StringWriter();
/*  658 */     PrintWriter printWriter = new PrintWriter(stringWriter);
/*      */     
/*  660 */     super.sessions(printWriter, cn, idle, smClient);
/*      */     
/*  662 */     return stringWriter.toString();
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
/*      */   protected String start(ContextName cn, StringManager smClient)
/*      */   {
/*  676 */     StringWriter stringWriter = new StringWriter();
/*  677 */     PrintWriter printWriter = new PrintWriter(stringWriter);
/*      */     
/*  679 */     super.start(printWriter, cn, smClient);
/*      */     
/*  681 */     return stringWriter.toString();
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
/*      */   protected String stop(ContextName cn, StringManager smClient)
/*      */   {
/*  695 */     StringWriter stringWriter = new StringWriter();
/*  696 */     PrintWriter printWriter = new PrintWriter(stringWriter);
/*      */     
/*  698 */     super.stop(printWriter, cn, smClient);
/*      */     
/*  700 */     return stringWriter.toString();
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
/*      */   protected String findleaks(StringManager smClient)
/*      */   {
/*  714 */     StringBuilder msg = new StringBuilder();
/*      */     
/*  716 */     StringWriter stringWriter = new StringWriter();
/*  717 */     PrintWriter printWriter = new PrintWriter(stringWriter);
/*      */     
/*  719 */     super.findleaks(false, printWriter, smClient);
/*      */     
/*  721 */     String writerText = stringWriter.toString();
/*      */     
/*  723 */     if (writerText.length() > 0) {
/*  724 */       if (!writerText.startsWith("FAIL -")) {
/*  725 */         msg.append(smClient.getString("htmlManagerServlet.findleaksList"));
/*      */       }
/*      */       
/*  728 */       msg.append(writerText);
/*      */     } else {
/*  730 */       msg.append(smClient.getString("htmlManagerServlet.findleaksNone"));
/*      */     }
/*      */     
/*  733 */     return msg.toString();
/*      */   }
/*      */   
/*      */   protected void sslConnectorCiphers(HttpServletRequest request, HttpServletResponse response)
/*      */     throws ServletException, IOException
/*      */   {
/*  739 */     request.setAttribute("cipherList", getConnectorCiphers());
/*  740 */     getServletContext().getRequestDispatcher("/WEB-INF/jsp/connectorCiphers.jsp")
/*  741 */       .forward(request, response);
/*      */   }
/*      */   
/*      */   protected void sslConnectorCerts(HttpServletRequest request, HttpServletResponse response)
/*      */     throws ServletException, IOException
/*      */   {
/*  747 */     request.setAttribute("certList", getConnectorCerts());
/*  748 */     getServletContext().getRequestDispatcher("/WEB-INF/jsp/connectorCerts.jsp")
/*  749 */       .forward(request, response);
/*      */   }
/*      */   
/*      */   protected void sslConnectorTrustedCerts(HttpServletRequest request, HttpServletResponse response)
/*      */     throws ServletException, IOException
/*      */   {
/*  755 */     request.setAttribute("trustedCertList", getConnectorTrustedCerts());
/*  756 */     getServletContext().getRequestDispatcher("/WEB-INF/jsp/connectorTrustedCerts.jsp")
/*  757 */       .forward(request, response);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getServletInfo()
/*      */   {
/*  766 */     return "HTMLManagerServlet, Copyright (c) 1999-2018, The Apache Software Foundation";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void init()
/*      */     throws ServletException
/*      */   {
/*  774 */     super.init();
/*      */     
/*      */ 
/*  777 */     String value = null;
/*  778 */     value = getServletConfig().getInitParameter("showProxySessions");
/*  779 */     this.showProxySessions = Boolean.parseBoolean(value);
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
/*      */   protected String expireSessions(ContextName cn, HttpServletRequest req, StringManager smClient)
/*      */   {
/*  795 */     int idle = -1;
/*  796 */     String idleParam = req.getParameter("idle");
/*  797 */     if (idleParam != null) {
/*      */       try {
/*  799 */         idle = Integer.parseInt(idleParam);
/*      */       } catch (NumberFormatException e) {
/*  801 */         log("Could not parse idle parameter to an int: " + idleParam);
/*      */       }
/*      */     }
/*  804 */     return sessions(cn, idle, smClient);
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
/*      */   protected void doSessions(ContextName cn, HttpServletRequest req, HttpServletResponse resp, StringManager smClient)
/*      */     throws ServletException, IOException
/*      */   {
/*  820 */     req.setAttribute("path", cn.getPath());
/*  821 */     req.setAttribute("version", cn.getVersion());
/*  822 */     String action = req.getParameter("action");
/*  823 */     if (this.debug >= 1) {
/*  824 */       log("sessions: Session action '" + action + "' for web application '" + cn
/*  825 */         .getDisplayName() + "'");
/*      */     }
/*  827 */     if ("sessionDetail".equals(action)) {
/*  828 */       String sessionId = req.getParameter("sessionId");
/*  829 */       displaySessionDetailPage(req, resp, cn, sessionId, smClient);
/*  830 */       return; }
/*  831 */     if ("invalidateSessions".equals(action)) {
/*  832 */       String[] sessionIds = req.getParameterValues("sessionIds");
/*  833 */       int i = invalidateSessions(cn, sessionIds, smClient);
/*  834 */       req.setAttribute("message", "" + i + " sessions invalidated.");
/*  835 */     } else if ("removeSessionAttribute".equals(action)) {
/*  836 */       String sessionId = req.getParameter("sessionId");
/*  837 */       String name = req.getParameter("attributeName");
/*      */       
/*  839 */       boolean removed = removeSessionAttribute(cn, sessionId, name, smClient);
/*  840 */       String outMessage = "Session did not contain any attribute named '" + name + "'";
/*  841 */       req.setAttribute("message", outMessage);
/*  842 */       displaySessionDetailPage(req, resp, cn, sessionId, smClient);
/*  843 */       return;
/*      */     }
/*  845 */     displaySessionsListPage(cn, req, resp, smClient);
/*      */   }
/*      */   
/*      */   protected List<Session> getSessionsForName(ContextName cn, StringManager smClient)
/*      */   {
/*  850 */     if ((cn == null) || ((!cn.getPath().startsWith("/")) && 
/*  851 */       (!cn.getPath().equals("")))) {
/*  852 */       String path = null;
/*  853 */       if (cn != null) {
/*  854 */         path = cn.getPath();
/*      */       }
/*  856 */       throw new IllegalArgumentException(smClient.getString("managerServlet.invalidPath", new Object[] {
/*      */       
/*  858 */         Escape.htmlElementContent(path) }));
/*      */     }
/*      */     
/*  861 */     Context ctxt = (Context)this.host.findChild(cn.getName());
/*  862 */     if (null == ctxt) {
/*  863 */       throw new IllegalArgumentException(smClient.getString("managerServlet.noContext", new Object[] {
/*      */       
/*  865 */         Escape.htmlElementContent(cn.getDisplayName()) }));
/*      */     }
/*  867 */     Manager manager = ctxt.getManager();
/*  868 */     List<Session> sessions = new ArrayList();
/*  869 */     sessions.addAll(Arrays.asList(manager.findSessions()));
/*  870 */     if (((manager instanceof DistributedManager)) && (this.showProxySessions))
/*      */     {
/*      */ 
/*  873 */       Set<String> sessionIds = ((DistributedManager)manager).getSessionIdsFull();
/*      */       
/*  875 */       for (Session session : sessions) {
/*  876 */         sessionIds.remove(session.getId());
/*      */       }
/*      */       
/*  879 */       for (String sessionId : sessionIds) {
/*  880 */         sessions.add(new DummyProxySession(sessionId));
/*      */       }
/*      */     }
/*  883 */     return sessions;
/*      */   }
/*      */   
/*      */ 
/*      */   protected Session getSessionForNameAndId(ContextName cn, String id, StringManager smClient)
/*      */   {
/*  889 */     List<Session> sessions = getSessionsForName(cn, smClient);
/*  890 */     if (sessions.isEmpty()) return null;
/*  891 */     for (Session session : sessions) {
/*  892 */       if (session.getId().equals(id)) {
/*  893 */         return session;
/*      */       }
/*      */     }
/*  896 */     return null;
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
/*      */   protected void displaySessionsListPage(ContextName cn, HttpServletRequest req, HttpServletResponse resp, StringManager smClient)
/*      */     throws ServletException, IOException
/*      */   {
/*  912 */     List<Session> sessions = getSessionsForName(cn, smClient);
/*  913 */     String sortBy = req.getParameter("sort");
/*  914 */     String orderBy = null;
/*  915 */     if ((null != sortBy) && (!"".equals(sortBy.trim()))) {
/*  916 */       Comparator<Session> comparator = getComparator(sortBy);
/*  917 */       if (comparator != null) {
/*  918 */         orderBy = req.getParameter("order");
/*  919 */         if ("DESC".equalsIgnoreCase(orderBy)) {
/*  920 */           comparator = Collections.reverseOrder(comparator);
/*  921 */           orderBy = "ASC";
/*      */         } else {
/*  923 */           orderBy = "DESC";
/*      */         }
/*      */         try {
/*  926 */           Collections.sort(sessions, comparator);
/*      */         }
/*      */         catch (IllegalStateException ise) {
/*  929 */           req.setAttribute("error", "Can't sort session list: one session is invalidated");
/*      */         }
/*      */       } else {
/*  932 */         log("WARNING: unknown sort order: " + sortBy);
/*      */       }
/*      */     }
/*      */     
/*  936 */     req.setAttribute("sort", sortBy);
/*  937 */     req.setAttribute("order", orderBy);
/*  938 */     req.setAttribute("activeSessions", sessions);
/*      */     
/*      */ 
/*      */ 
/*  942 */     resp.setHeader("Pragma", "No-cache");
/*  943 */     resp.setHeader("Cache-Control", "no-cache,no-store,max-age=0");
/*  944 */     resp.setDateHeader("Expires", 0L);
/*  945 */     getServletContext().getRequestDispatcher("/WEB-INF/jsp/sessionsList.jsp").include(req, resp);
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
/*      */   protected void displaySessionDetailPage(HttpServletRequest req, HttpServletResponse resp, ContextName cn, String sessionId, StringManager smClient)
/*      */     throws ServletException, IOException
/*      */   {
/*  962 */     Session session = getSessionForNameAndId(cn, sessionId, smClient);
/*      */     
/*      */ 
/*      */ 
/*  966 */     resp.setHeader("Pragma", "No-cache");
/*  967 */     resp.setHeader("Cache-Control", "no-cache,no-store,max-age=0");
/*  968 */     resp.setDateHeader("Expires", 0L);
/*  969 */     req.setAttribute("currentSession", session);
/*  970 */     getServletContext().getRequestDispatcher(resp.encodeURL("/WEB-INF/jsp/sessionDetail.jsp")).include(req, resp);
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
/*      */   protected int invalidateSessions(ContextName cn, String[] sessionIds, StringManager smClient)
/*      */   {
/*  984 */     if (null == sessionIds) {
/*  985 */       return 0;
/*      */     }
/*  987 */     int nbAffectedSessions = 0;
/*  988 */     for (int i = 0; i < sessionIds.length; i++) {
/*  989 */       String sessionId = sessionIds[i];
/*      */       
/*  991 */       HttpSession session = getSessionForNameAndId(cn, sessionId, smClient).getSession();
/*  992 */       if (null == session)
/*      */       {
/*  994 */         if (this.debug >= 1) {
/*  995 */           log("WARNING: can't invalidate null session " + sessionId);
/*      */         }
/*      */       }
/*      */       else {
/*      */         try {
/* 1000 */           session.invalidate();
/* 1001 */           nbAffectedSessions++;
/* 1002 */           if (this.debug >= 1) {
/* 1003 */             log("Invalidating session id " + sessionId);
/*      */           }
/*      */         } catch (IllegalStateException ise) {
/* 1006 */           if (this.debug >= 1)
/* 1007 */             log("Can't invalidate already invalidated session id " + sessionId);
/*      */         }
/*      */       }
/*      */     }
/* 1011 */     return nbAffectedSessions;
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
/*      */   protected boolean removeSessionAttribute(ContextName cn, String sessionId, String attributeName, StringManager smClient)
/*      */   {
/* 1026 */     HttpSession session = getSessionForNameAndId(cn, sessionId, smClient).getSession();
/* 1027 */     if (null == session)
/*      */     {
/* 1029 */       if (this.debug >= 1) {
/* 1030 */         log("WARNING: can't remove attribute '" + attributeName + "' for null session " + sessionId);
/*      */       }
/* 1032 */       return false;
/*      */     }
/* 1034 */     boolean wasPresent = null != session.getAttribute(attributeName);
/*      */     try {
/* 1036 */       session.removeAttribute(attributeName);
/*      */     } catch (IllegalStateException ise) {
/* 1038 */       if (this.debug >= 1) {
/* 1039 */         log("Can't remote attribute '" + attributeName + "' for invalidated session id " + sessionId);
/*      */       }
/*      */     }
/* 1042 */     return wasPresent;
/*      */   }
/*      */   
/*      */   protected Comparator<Session> getComparator(String sortBy) {
/* 1046 */     Comparator<Session> comparator = null;
/* 1047 */     if ("CreationTime".equalsIgnoreCase(sortBy)) {
/* 1048 */       comparator = new BaseSessionComparator()
/*      */       {
/*      */         public Comparable<Date> getComparableObject(Session session) {
/* 1051 */           return new Date(session.getCreationTime());
/*      */         }
/*      */       };
/* 1054 */     } else if ("id".equalsIgnoreCase(sortBy)) {
/* 1055 */       comparator = new BaseSessionComparator()
/*      */       {
/*      */         public Comparable<String> getComparableObject(Session session) {
/* 1058 */           return session.getId();
/*      */         }
/*      */       };
/* 1061 */     } else if ("LastAccessedTime".equalsIgnoreCase(sortBy)) {
/* 1062 */       comparator = new BaseSessionComparator()
/*      */       {
/*      */         public Comparable<Date> getComparableObject(Session session) {
/* 1065 */           return new Date(session.getLastAccessedTime());
/*      */         }
/*      */       };
/* 1068 */     } else if ("MaxInactiveInterval".equalsIgnoreCase(sortBy)) {
/* 1069 */       comparator = new BaseSessionComparator()
/*      */       {
/*      */         public Comparable<Integer> getComparableObject(Session session) {
/* 1072 */           return Integer.valueOf(session.getMaxInactiveInterval());
/*      */         }
/*      */       };
/* 1075 */     } else if ("new".equalsIgnoreCase(sortBy)) {
/* 1076 */       comparator = new BaseSessionComparator()
/*      */       {
/*      */         public Comparable<Boolean> getComparableObject(Session session) {
/* 1079 */           return Boolean.valueOf(session.getSession().isNew());
/*      */         }
/*      */       };
/* 1082 */     } else if ("locale".equalsIgnoreCase(sortBy)) {
/* 1083 */       comparator = new BaseSessionComparator()
/*      */       {
/*      */         public Comparable<String> getComparableObject(Session session) {
/* 1086 */           return JspHelper.guessDisplayLocaleFromSession(session);
/*      */         }
/*      */       };
/* 1089 */     } else if ("user".equalsIgnoreCase(sortBy)) {
/* 1090 */       comparator = new BaseSessionComparator()
/*      */       {
/*      */         public Comparable<String> getComparableObject(Session session) {
/* 1093 */           return JspHelper.guessDisplayUserFromSession(session);
/*      */         }
/*      */       };
/* 1096 */     } else if ("UsedTime".equalsIgnoreCase(sortBy)) {
/* 1097 */       comparator = new BaseSessionComparator()
/*      */       {
/*      */         public Comparable<Date> getComparableObject(Session session) {
/* 1100 */           return new Date(SessionUtils.getUsedTimeForSession(session));
/*      */         }
/*      */       };
/* 1103 */     } else if ("InactiveTime".equalsIgnoreCase(sortBy)) {
/* 1104 */       comparator = new BaseSessionComparator()
/*      */       {
/*      */         public Comparable<Date> getComparableObject(Session session) {
/* 1107 */           return new Date(SessionUtils.getInactiveTimeForSession(session));
/*      */         }
/*      */       };
/* 1110 */     } else if ("TTL".equalsIgnoreCase(sortBy)) {
/* 1111 */       comparator = new BaseSessionComparator()
/*      */       {
/*      */         public Comparable<Date> getComparableObject(Session session) {
/* 1114 */           return new Date(SessionUtils.getTTLForSession(session));
/*      */         }
/*      */       };
/*      */     }
/*      */     
/* 1119 */     return comparator;
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\manager\HTMLManagerServlet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */