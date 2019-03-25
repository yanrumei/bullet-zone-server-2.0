/*      */ package org.apache.catalina.manager;
/*      */ 
/*      */ import java.io.PrintWriter;
/*      */ import java.lang.management.ManagementFactory;
/*      */ import java.lang.management.MemoryPoolMXBean;
/*      */ import java.lang.management.MemoryUsage;
/*      */ import java.lang.reflect.Method;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.Date;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Iterator;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import java.util.TreeMap;
/*      */ import java.util.Vector;
/*      */ import javax.management.MBeanServer;
/*      */ import javax.management.ObjectInstance;
/*      */ import javax.management.ObjectName;
/*      */ import javax.servlet.http.HttpServletResponse;
/*      */ import org.apache.tomcat.util.ExceptionUtils;
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
/*      */ public class StatusTransformer
/*      */ {
/*      */   public static void setContentType(HttpServletResponse response, int mode)
/*      */   {
/*   58 */     if (mode == 0) {
/*   59 */       response.setContentType("text/html;charset=utf-8");
/*   60 */     } else if (mode == 1) {
/*   61 */       response.setContentType("text/xml;charset=utf-8");
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
/*      */   public static void writeHeader(PrintWriter writer, Object[] args, int mode)
/*      */   {
/*   75 */     if (mode == 0)
/*      */     {
/*   77 */       writer.print(Constants.HTML_HEADER_SECTION);
/*   78 */     } else if (mode == 1) {
/*   79 */       writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
/*   80 */       writer.print(
/*   81 */         MessageFormat.format("<?xml-stylesheet type=\"text/xsl\" href=\"{0}/xform.xsl\" ?>\n", args));
/*   82 */       writer.write("<status>");
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
/*      */   public static void writeBody(PrintWriter writer, Object[] args, int mode)
/*      */   {
/*   96 */     if (mode == 0) {
/*   97 */       writer.print(
/*   98 */         MessageFormat.format(Constants.BODY_HEADER_SECTION, args));
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
/*      */   public static void writeManager(PrintWriter writer, Object[] args, int mode)
/*      */   {
/*  112 */     if (mode == 0) {
/*  113 */       writer.print(MessageFormat.format(Constants.MANAGER_SECTION, args));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static void writePageHeading(PrintWriter writer, Object[] args, int mode)
/*      */   {
/*  120 */     if (mode == 0) {
/*  121 */       writer.print(
/*  122 */         MessageFormat.format(Constants.SERVER_HEADER_SECTION, args));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static void writeServerInfo(PrintWriter writer, Object[] args, int mode)
/*      */   {
/*  129 */     if (mode == 0) {
/*  130 */       writer.print(MessageFormat.format(Constants.SERVER_ROW_SECTION, args));
/*      */     }
/*      */   }
/*      */   
/*      */   public static void writeFooter(PrintWriter writer, int mode)
/*      */   {
/*  136 */     if (mode == 0)
/*      */     {
/*  138 */       writer.print(Constants.HTML_TAIL_SECTION);
/*  139 */     } else if (mode == 1) {
/*  140 */       writer.write("</status>");
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
/*      */   public static void writeOSState(PrintWriter writer, int mode)
/*      */   {
/*  153 */     long[] result = new long[16];
/*  154 */     boolean ok = false;
/*      */     try {
/*  156 */       String methodName = "info";
/*  157 */       Class<?>[] paramTypes = new Class[1];
/*  158 */       paramTypes[0] = result.getClass();
/*  159 */       Object[] paramValues = new Object[1];
/*  160 */       paramValues[0] = result;
/*      */       
/*  162 */       Method method = Class.forName("org.apache.tomcat.jni.OS").getMethod(methodName, paramTypes);
/*  163 */       method.invoke(null, paramValues);
/*  164 */       ok = true;
/*      */     } catch (Throwable t) {
/*  166 */       t = ExceptionUtils.unwrapInvocationTargetException(t);
/*  167 */       ExceptionUtils.handleThrowable(t);
/*      */     }
/*      */     
/*  170 */     if (ok) {
/*  171 */       if (mode == 0) {
/*  172 */         writer.print("<h1>OS</h1>");
/*      */         
/*  174 */         writer.print("<p>");
/*  175 */         writer.print(" Physical memory: ");
/*  176 */         writer.print(formatSize(Long.valueOf(result[0]), true));
/*  177 */         writer.print(" Available memory: ");
/*  178 */         writer.print(formatSize(Long.valueOf(result[1]), true));
/*  179 */         writer.print(" Total page file: ");
/*  180 */         writer.print(formatSize(Long.valueOf(result[2]), true));
/*  181 */         writer.print(" Free page file: ");
/*  182 */         writer.print(formatSize(Long.valueOf(result[3]), true));
/*  183 */         writer.print(" Memory load: ");
/*  184 */         writer.print(Long.valueOf(result[6]));
/*  185 */         writer.print("<br>");
/*  186 */         writer.print(" Process kernel time: ");
/*  187 */         writer.print(formatTime(Long.valueOf(result[11] / 1000L), true));
/*  188 */         writer.print(" Process user time: ");
/*  189 */         writer.print(formatTime(Long.valueOf(result[12] / 1000L), true));
/*  190 */         writer.print("</p>");
/*  191 */       } else if (mode != 1) {}
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
/*      */   public static void writeVMState(PrintWriter writer, int mode)
/*      */     throws Exception
/*      */   {
/*  209 */     SortedMap<String, MemoryPoolMXBean> memoryPoolMBeans = new TreeMap();
/*  210 */     for (MemoryPoolMXBean mbean : ManagementFactory.getMemoryPoolMXBeans()) {
/*  211 */       String sortKey = mbean.getType() + ":" + mbean.getName();
/*  212 */       memoryPoolMBeans.put(sortKey, mbean);
/*      */     }
/*      */     
/*  215 */     if (mode == 0) {
/*  216 */       writer.print("<h1>JVM</h1>");
/*      */       
/*  218 */       writer.print("<p>");
/*  219 */       writer.print(" Free memory: ");
/*  220 */       writer.print(formatSize(
/*  221 */         Long.valueOf(Runtime.getRuntime().freeMemory()), true));
/*  222 */       writer.print(" Total memory: ");
/*  223 */       writer.print(formatSize(
/*  224 */         Long.valueOf(Runtime.getRuntime().totalMemory()), true));
/*  225 */       writer.print(" Max memory: ");
/*  226 */       writer.print(formatSize(
/*  227 */         Long.valueOf(Runtime.getRuntime().maxMemory()), true));
/*  228 */       writer.print("</p>");
/*      */       
/*  230 */       writer.write("<table border=\"0\"><thead><tr><th>Memory Pool</th><th>Type</th><th>Initial</th><th>Total</th><th>Maximum</th><th>Used</th></tr></thead><tbody>");
/*  231 */       for (MemoryPoolMXBean memoryPoolMBean : memoryPoolMBeans.values()) {
/*  232 */         MemoryUsage usage = memoryPoolMBean.getUsage();
/*  233 */         writer.write("<tr><td>");
/*  234 */         writer.print(memoryPoolMBean.getName());
/*  235 */         writer.write("</td><td>");
/*  236 */         writer.print(memoryPoolMBean.getType());
/*  237 */         writer.write("</td><td>");
/*  238 */         writer.print(formatSize(Long.valueOf(usage.getInit()), true));
/*  239 */         writer.write("</td><td>");
/*  240 */         writer.print(formatSize(Long.valueOf(usage.getCommitted()), true));
/*  241 */         writer.write("</td><td>");
/*  242 */         writer.print(formatSize(Long.valueOf(usage.getMax()), true));
/*  243 */         writer.write("</td><td>");
/*  244 */         writer.print(formatSize(Long.valueOf(usage.getUsed()), true));
/*  245 */         if (usage.getMax() > 0L) {
/*  246 */           writer.write(" (" + usage
/*  247 */             .getUsed() * 100L / usage.getMax() + "%)");
/*      */         }
/*  249 */         writer.write("</td></tr>");
/*      */       }
/*  251 */       writer.write("</tbody></table>");
/*  252 */     } else if (mode == 1) {
/*  253 */       writer.write("<jvm>");
/*      */       
/*  255 */       writer.write("<memory");
/*  256 */       writer.write(" free='" + Runtime.getRuntime().freeMemory() + "'");
/*  257 */       writer.write(" total='" + Runtime.getRuntime().totalMemory() + "'");
/*  258 */       writer.write(" max='" + Runtime.getRuntime().maxMemory() + "'/>");
/*      */       
/*  260 */       for (MemoryPoolMXBean memoryPoolMBean : memoryPoolMBeans.values()) {
/*  261 */         MemoryUsage usage = memoryPoolMBean.getUsage();
/*  262 */         writer.write("<memorypool");
/*  263 */         writer.write(" name='" + Escape.xml("", memoryPoolMBean.getName()) + "'");
/*  264 */         writer.write(" type='" + memoryPoolMBean.getType() + "'");
/*  265 */         writer.write(" usageInit='" + usage.getInit() + "'");
/*  266 */         writer.write(" usageCommitted='" + usage.getCommitted() + "'");
/*  267 */         writer.write(" usageMax='" + usage.getMax() + "'");
/*  268 */         writer.write(" usageUsed='" + usage.getUsed() + "'/>");
/*      */       }
/*      */       
/*  271 */       writer.write("</jvm>");
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
/*      */   public static void writeConnectorState(PrintWriter writer, ObjectName tpName, String name, MBeanServer mBeanServer, Vector<ObjectName> globalRequestProcessors, Vector<ObjectName> requestProcessors, int mode)
/*      */     throws Exception
/*      */   {
/*  294 */     if (mode == 0) {
/*  295 */       writer.print("<h1>");
/*  296 */       writer.print(name);
/*  297 */       writer.print("</h1>");
/*      */       
/*  299 */       writer.print("<p>");
/*  300 */       writer.print(" Max threads: ");
/*  301 */       writer.print(mBeanServer.getAttribute(tpName, "maxThreads"));
/*  302 */       writer.print(" Current thread count: ");
/*  303 */       writer.print(mBeanServer.getAttribute(tpName, "currentThreadCount"));
/*  304 */       writer.print(" Current thread busy: ");
/*  305 */       writer.print(mBeanServer.getAttribute(tpName, "currentThreadsBusy"));
/*      */       try {
/*  307 */         Object value = mBeanServer.getAttribute(tpName, "keepAliveCount");
/*  308 */         writer.print(" Keep alive sockets count: ");
/*  309 */         writer.print(value);
/*      */       }
/*      */       catch (Exception localException) {}
/*      */       
/*      */ 
/*  314 */       writer.print("<br>");
/*      */       
/*  316 */       ObjectName grpName = null;
/*      */       
/*      */ 
/*  319 */       Enumeration<ObjectName> enumeration = globalRequestProcessors.elements();
/*  320 */       while (enumeration.hasMoreElements()) {
/*  321 */         ObjectName objectName = (ObjectName)enumeration.nextElement();
/*  322 */         if (name.equals(objectName.getKeyProperty("name"))) {
/*  323 */           grpName = objectName;
/*      */         }
/*      */       }
/*      */       
/*  327 */       if (grpName == null) {
/*  328 */         return;
/*      */       }
/*      */       
/*  331 */       writer.print(" Max processing time: ");
/*  332 */       writer.print(formatTime(mBeanServer
/*  333 */         .getAttribute(grpName, "maxTime"), false));
/*  334 */       writer.print(" Processing time: ");
/*  335 */       writer.print(formatTime(mBeanServer
/*  336 */         .getAttribute(grpName, "processingTime"), true));
/*  337 */       writer.print(" Request count: ");
/*  338 */       writer.print(mBeanServer.getAttribute(grpName, "requestCount"));
/*  339 */       writer.print(" Error count: ");
/*  340 */       writer.print(mBeanServer.getAttribute(grpName, "errorCount"));
/*  341 */       writer.print(" Bytes received: ");
/*  342 */       writer.print(formatSize(mBeanServer
/*  343 */         .getAttribute(grpName, "bytesReceived"), true));
/*  344 */       writer.print(" Bytes sent: ");
/*  345 */       writer.print(formatSize(mBeanServer
/*  346 */         .getAttribute(grpName, "bytesSent"), true));
/*  347 */       writer.print("</p>");
/*      */       
/*  349 */       writer.print("<table border=\"0\"><tr><th>Stage</th><th>Time</th><th>B Sent</th><th>B Recv</th><th>Client (Forwarded)</th><th>Client (Actual)</th><th>VHost</th><th>Request</th></tr>");
/*      */       
/*  351 */       enumeration = requestProcessors.elements();
/*  352 */       while (enumeration.hasMoreElements()) {
/*  353 */         ObjectName objectName = (ObjectName)enumeration.nextElement();
/*  354 */         if (name.equals(objectName.getKeyProperty("worker"))) {
/*  355 */           writer.print("<tr>");
/*  356 */           writeProcessorState(writer, objectName, mBeanServer, mode);
/*  357 */           writer.print("</tr>");
/*      */         }
/*      */       }
/*      */       
/*  361 */       writer.print("</table>");
/*      */       
/*  363 */       writer.print("<p>");
/*  364 */       writer.print("P: Parse and prepare request S: Service F: Finishing R: Ready K: Keepalive");
/*  365 */       writer.print("</p>");
/*  366 */     } else if (mode == 1) {
/*  367 */       writer.write("<connector name='" + name + "'>");
/*      */       
/*  369 */       writer.write("<threadInfo ");
/*  370 */       writer.write(" maxThreads=\"" + mBeanServer.getAttribute(tpName, "maxThreads") + "\"");
/*  371 */       writer.write(" currentThreadCount=\"" + mBeanServer.getAttribute(tpName, "currentThreadCount") + "\"");
/*  372 */       writer.write(" currentThreadsBusy=\"" + mBeanServer.getAttribute(tpName, "currentThreadsBusy") + "\"");
/*  373 */       writer.write(" />");
/*      */       
/*  375 */       ObjectName grpName = null;
/*      */       
/*      */ 
/*  378 */       Enumeration<ObjectName> enumeration = globalRequestProcessors.elements();
/*  379 */       while (enumeration.hasMoreElements()) {
/*  380 */         ObjectName objectName = (ObjectName)enumeration.nextElement();
/*  381 */         if (name.equals(objectName.getKeyProperty("name"))) {
/*  382 */           grpName = objectName;
/*      */         }
/*      */       }
/*      */       
/*  386 */       if (grpName != null)
/*      */       {
/*  388 */         writer.write("<requestInfo ");
/*  389 */         writer.write(" maxTime=\"" + mBeanServer.getAttribute(grpName, "maxTime") + "\"");
/*  390 */         writer.write(" processingTime=\"" + mBeanServer.getAttribute(grpName, "processingTime") + "\"");
/*  391 */         writer.write(" requestCount=\"" + mBeanServer.getAttribute(grpName, "requestCount") + "\"");
/*  392 */         writer.write(" errorCount=\"" + mBeanServer.getAttribute(grpName, "errorCount") + "\"");
/*  393 */         writer.write(" bytesReceived=\"" + mBeanServer.getAttribute(grpName, "bytesReceived") + "\"");
/*  394 */         writer.write(" bytesSent=\"" + mBeanServer.getAttribute(grpName, "bytesSent") + "\"");
/*  395 */         writer.write(" />");
/*      */         
/*  397 */         writer.write("<workers>");
/*  398 */         enumeration = requestProcessors.elements();
/*  399 */         while (enumeration.hasMoreElements()) {
/*  400 */           ObjectName objectName = (ObjectName)enumeration.nextElement();
/*  401 */           if (name.equals(objectName.getKeyProperty("worker"))) {
/*  402 */             writeProcessorState(writer, objectName, mBeanServer, mode);
/*      */           }
/*      */         }
/*  405 */         writer.write("</workers>");
/*      */       }
/*      */       
/*  408 */       writer.write("</connector>");
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
/*      */   protected static void writeProcessorState(PrintWriter writer, ObjectName pName, MBeanServer mBeanServer, int mode)
/*      */     throws Exception
/*      */   {
/*  430 */     Integer stageValue = (Integer)mBeanServer.getAttribute(pName, "stage");
/*  431 */     int stage = stageValue.intValue();
/*  432 */     boolean fullStatus = true;
/*  433 */     boolean showRequest = true;
/*  434 */     String stageStr = null;
/*      */     
/*  436 */     switch (stage)
/*      */     {
/*      */     case 1: 
/*  439 */       stageStr = "P";
/*  440 */       fullStatus = false;
/*  441 */       break;
/*      */     case 2: 
/*  443 */       stageStr = "P";
/*  444 */       fullStatus = false;
/*  445 */       break;
/*      */     case 3: 
/*  447 */       stageStr = "S";
/*  448 */       break;
/*      */     case 4: 
/*  450 */       stageStr = "F";
/*  451 */       break;
/*      */     case 5: 
/*  453 */       stageStr = "F";
/*  454 */       break;
/*      */     case 7: 
/*  456 */       stageStr = "R";
/*  457 */       fullStatus = false;
/*  458 */       break;
/*      */     case 6: 
/*  460 */       stageStr = "K";
/*  461 */       fullStatus = true;
/*  462 */       showRequest = false;
/*  463 */       break;
/*      */     case 0: 
/*  465 */       stageStr = "R";
/*  466 */       fullStatus = false;
/*  467 */       break;
/*      */     
/*      */     default: 
/*  470 */       stageStr = "?";
/*  471 */       fullStatus = false;
/*      */     }
/*      */     
/*      */     
/*  475 */     if (mode == 0) {
/*  476 */       writer.write("<td><strong>");
/*  477 */       writer.write(stageStr);
/*  478 */       writer.write("</strong></td>");
/*      */       
/*  480 */       if (fullStatus) {
/*  481 */         writer.write("<td>");
/*  482 */         writer.print(formatTime(mBeanServer
/*  483 */           .getAttribute(pName, "requestProcessingTime"), false));
/*  484 */         writer.write("</td>");
/*  485 */         writer.write("<td>");
/*  486 */         if (showRequest) {
/*  487 */           writer.print(formatSize(mBeanServer
/*  488 */             .getAttribute(pName, "requestBytesSent"), false));
/*      */         } else {
/*  490 */           writer.write("?");
/*      */         }
/*  492 */         writer.write("</td>");
/*  493 */         writer.write("<td>");
/*  494 */         if (showRequest) {
/*  495 */           writer.print(formatSize(mBeanServer
/*  496 */             .getAttribute(pName, "requestBytesReceived"), false));
/*      */         }
/*      */         else {
/*  499 */           writer.write("?");
/*      */         }
/*  501 */         writer.write("</td>");
/*  502 */         writer.write("<td>");
/*  503 */         writer.print(Escape.htmlElementContext(mBeanServer
/*  504 */           .getAttribute(pName, "remoteAddrForwarded")));
/*  505 */         writer.write("</td>");
/*  506 */         writer.write("<td>");
/*  507 */         writer.print(Escape.htmlElementContext(mBeanServer
/*  508 */           .getAttribute(pName, "remoteAddr")));
/*  509 */         writer.write("</td>");
/*  510 */         writer.write("<td nowrap>");
/*  511 */         writer.write(Escape.htmlElementContext(mBeanServer
/*  512 */           .getAttribute(pName, "virtualHost")));
/*  513 */         writer.write("</td>");
/*  514 */         writer.write("<td nowrap class=\"row-left\">");
/*  515 */         if (showRequest) {
/*  516 */           writer.write(Escape.htmlElementContext(mBeanServer
/*  517 */             .getAttribute(pName, "method")));
/*  518 */           writer.write(" ");
/*  519 */           writer.write(Escape.htmlElementContext(mBeanServer
/*  520 */             .getAttribute(pName, "currentUri")));
/*      */           
/*  522 */           String queryString = (String)mBeanServer.getAttribute(pName, "currentQueryString");
/*  523 */           if ((queryString != null) && (!queryString.equals(""))) {
/*  524 */             writer.write("?");
/*  525 */             writer.print(Escape.htmlElementContent(queryString));
/*      */           }
/*  527 */           writer.write(" ");
/*  528 */           writer.write(Escape.htmlElementContext(mBeanServer
/*  529 */             .getAttribute(pName, "protocol")));
/*      */         } else {
/*  531 */           writer.write("?");
/*      */         }
/*  533 */         writer.write("</td>");
/*      */       } else {
/*  535 */         writer.write("<td>?</td><td>?</td><td>?</td><td>?</td><td>?</td><td>?</td>");
/*      */       }
/*  537 */     } else if (mode == 1) {
/*  538 */       writer.write("<worker ");
/*  539 */       writer.write(" stage=\"" + stageStr + "\"");
/*      */       
/*  541 */       if (fullStatus) {
/*  542 */         writer.write(" requestProcessingTime=\"" + mBeanServer
/*      */         
/*  544 */           .getAttribute(pName, "requestProcessingTime") + "\"");
/*  545 */         writer.write(" requestBytesSent=\"");
/*  546 */         if (showRequest) {
/*  547 */           writer.write("" + mBeanServer
/*  548 */             .getAttribute(pName, "requestBytesSent"));
/*      */         } else {
/*  550 */           writer.write("0");
/*      */         }
/*  552 */         writer.write("\"");
/*  553 */         writer.write(" requestBytesReceived=\"");
/*  554 */         if (showRequest) {
/*  555 */           writer.write("" + mBeanServer
/*  556 */             .getAttribute(pName, "requestBytesReceived"));
/*      */         } else {
/*  558 */           writer.write("0");
/*      */         }
/*  560 */         writer.write("\"");
/*  561 */         writer.write(" remoteAddr=\"" + 
/*  562 */           Escape.htmlElementContext(mBeanServer
/*  563 */           .getAttribute(pName, "remoteAddr")) + "\"");
/*  564 */         writer.write(" virtualHost=\"" + 
/*  565 */           Escape.htmlElementContext(mBeanServer
/*  566 */           .getAttribute(pName, "virtualHost")) + "\"");
/*      */         
/*      */ 
/*  568 */         if (showRequest) {
/*  569 */           writer.write(" method=\"" + 
/*  570 */             Escape.htmlElementContext(mBeanServer
/*  571 */             .getAttribute(pName, "method")) + "\"");
/*  572 */           writer.write(" currentUri=\"" + 
/*  573 */             Escape.htmlElementContext(mBeanServer
/*  574 */             .getAttribute(pName, "currentUri")) + "\"");
/*      */           
/*      */ 
/*  577 */           String queryString = (String)mBeanServer.getAttribute(pName, "currentQueryString");
/*  578 */           if ((queryString != null) && (!queryString.equals(""))) {
/*  579 */             writer.write(" currentQueryString=\"" + 
/*  580 */               Escape.htmlElementContent(queryString) + "\"");
/*      */           } else {
/*  582 */             writer.write(" currentQueryString=\"&#63;\"");
/*      */           }
/*  584 */           writer.write(" protocol=\"" + 
/*  585 */             Escape.htmlElementContext(mBeanServer
/*  586 */             .getAttribute(pName, "protocol")) + "\"");
/*      */         }
/*      */         else {
/*  588 */           writer.write(" method=\"&#63;\"");
/*  589 */           writer.write(" currentUri=\"&#63;\"");
/*  590 */           writer.write(" currentQueryString=\"&#63;\"");
/*  591 */           writer.write(" protocol=\"&#63;\"");
/*      */         }
/*      */       } else {
/*  594 */         writer.write(" requestProcessingTime=\"0\"");
/*  595 */         writer.write(" requestBytesSent=\"0\"");
/*  596 */         writer.write(" requestBytesReceived=\"0\"");
/*  597 */         writer.write(" remoteAddr=\"&#63;\"");
/*  598 */         writer.write(" virtualHost=\"&#63;\"");
/*  599 */         writer.write(" method=\"&#63;\"");
/*  600 */         writer.write(" currentUri=\"&#63;\"");
/*  601 */         writer.write(" currentQueryString=\"&#63;\"");
/*  602 */         writer.write(" protocol=\"&#63;\"");
/*      */       }
/*  604 */       writer.write(" />");
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
/*      */   public static void writeDetailedState(PrintWriter writer, MBeanServer mBeanServer, int mode)
/*      */     throws Exception
/*      */   {
/*  622 */     if (mode == 0) {
/*  623 */       ObjectName queryHosts = new ObjectName("*:j2eeType=WebModule,*");
/*  624 */       Set<ObjectName> hostsON = mBeanServer.queryNames(queryHosts, null);
/*      */       
/*      */ 
/*  627 */       writer.print("<h1>");
/*  628 */       writer.print("Application list");
/*  629 */       writer.print("</h1>");
/*      */       
/*  631 */       writer.print("<p>");
/*  632 */       int count = 0;
/*  633 */       Iterator<ObjectName> iterator = hostsON.iterator();
/*  634 */       while (iterator.hasNext()) {
/*  635 */         ObjectName contextON = (ObjectName)iterator.next();
/*  636 */         String webModuleName = contextON.getKeyProperty("name");
/*  637 */         if (webModuleName.startsWith("//")) {
/*  638 */           webModuleName = webModuleName.substring(2);
/*      */         }
/*  640 */         int slash = webModuleName.indexOf('/');
/*  641 */         if (slash == -1) {
/*  642 */           count++;
/*      */         }
/*      */         else
/*      */         {
/*  646 */           writer.print("<a href=\"#" + count++ + ".0\">");
/*  647 */           writer.print(Escape.htmlElementContext(webModuleName));
/*  648 */           writer.print("</a>");
/*  649 */           if (iterator.hasNext()) {
/*  650 */             writer.print("<br>");
/*      */           }
/*      */         }
/*      */       }
/*  654 */       writer.print("</p>");
/*      */       
/*      */ 
/*  657 */       count = 0;
/*  658 */       iterator = hostsON.iterator();
/*  659 */       while (iterator.hasNext()) {
/*  660 */         ObjectName contextON = (ObjectName)iterator.next();
/*  661 */         writer.print("<a class=\"A.name\" name=\"" + count++ + ".0\">");
/*      */         
/*  663 */         writeContext(writer, contextON, mBeanServer, mode);
/*      */       }
/*      */     }
/*  666 */     else if (mode != 1) {}
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
/*      */   protected static void writeContext(PrintWriter writer, ObjectName objectName, MBeanServer mBeanServer, int mode)
/*      */     throws Exception
/*      */   {
/*  687 */     if (mode == 0) {
/*  688 */       String webModuleName = objectName.getKeyProperty("name");
/*  689 */       String name = webModuleName;
/*  690 */       if (name == null) {
/*  691 */         return;
/*      */       }
/*      */       
/*  694 */       String hostName = null;
/*  695 */       String contextName = null;
/*  696 */       if (name.startsWith("//")) {
/*  697 */         name = name.substring(2);
/*      */       }
/*  699 */       int slash = name.indexOf('/');
/*  700 */       if (slash != -1) {
/*  701 */         hostName = name.substring(0, slash);
/*  702 */         contextName = name.substring(slash);
/*      */       } else {
/*  704 */         return;
/*      */       }
/*      */       
/*      */ 
/*  708 */       ObjectName queryManager = new ObjectName(objectName.getDomain() + ":type=Manager,context=" + contextName + ",host=" + hostName + ",*");
/*      */       
/*      */ 
/*  711 */       Set<ObjectName> managersON = mBeanServer.queryNames(queryManager, null);
/*  712 */       ObjectName managerON = null;
/*  713 */       Iterator<ObjectName> iterator2 = managersON.iterator();
/*  714 */       while (iterator2.hasNext()) {
/*  715 */         managerON = (ObjectName)iterator2.next();
/*      */       }
/*      */       
/*      */ 
/*  719 */       ObjectName queryJspMonitor = new ObjectName(objectName.getDomain() + ":type=JspMonitor,WebModule=" + webModuleName + ",*");
/*      */       
/*      */ 
/*  722 */       Set<ObjectName> jspMonitorONs = mBeanServer.queryNames(queryJspMonitor, null);
/*      */       
/*      */ 
/*  725 */       if (contextName.equals("/")) {
/*  726 */         contextName = "";
/*      */       }
/*      */       
/*  729 */       writer.print("<h1>");
/*  730 */       writer.print(Escape.htmlElementContext(name));
/*  731 */       writer.print("</h1>");
/*  732 */       writer.print("</a>");
/*      */       
/*  734 */       writer.print("<p>");
/*  735 */       Object startTime = mBeanServer.getAttribute(objectName, "startTime");
/*      */       
/*  737 */       writer.print(" Start time: " + new Date(((Long)startTime)
/*  738 */         .longValue()));
/*  739 */       writer.print(" Startup time: ");
/*  740 */       writer.print(formatTime(mBeanServer
/*  741 */         .getAttribute(objectName, "startupTime"), false));
/*  742 */       writer.print(" TLD scan time: ");
/*  743 */       writer.print(formatTime(mBeanServer
/*  744 */         .getAttribute(objectName, "tldScanTime"), false));
/*  745 */       if (managerON != null) {
/*  746 */         writeManager(writer, managerON, mBeanServer, mode);
/*      */       }
/*  748 */       if (jspMonitorONs != null) {
/*  749 */         writeJspMonitor(writer, jspMonitorONs, mBeanServer, mode);
/*      */       }
/*  751 */       writer.print("</p>");
/*      */       
/*  753 */       String onStr = objectName.getDomain() + ":j2eeType=Servlet,WebModule=" + webModuleName + ",*";
/*      */       
/*  755 */       ObjectName servletObjectName = new ObjectName(onStr);
/*      */       
/*  757 */       Set<ObjectInstance> set = mBeanServer.queryMBeans(servletObjectName, null);
/*  758 */       Iterator<ObjectInstance> iterator = set.iterator();
/*  759 */       while (iterator.hasNext()) {
/*  760 */         ObjectInstance oi = (ObjectInstance)iterator.next();
/*  761 */         writeWrapper(writer, oi.getObjectName(), mBeanServer, mode);
/*      */       }
/*      */     }
/*  764 */     else if (mode != 1) {}
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
/*      */   public static void writeManager(PrintWriter writer, ObjectName objectName, MBeanServer mBeanServer, int mode)
/*      */     throws Exception
/*      */   {
/*  784 */     if (mode == 0) {
/*  785 */       writer.print("<br>");
/*  786 */       writer.print(" Active sessions: ");
/*  787 */       writer.print(mBeanServer
/*  788 */         .getAttribute(objectName, "activeSessions"));
/*  789 */       writer.print(" Session count: ");
/*  790 */       writer.print(mBeanServer
/*  791 */         .getAttribute(objectName, "sessionCounter"));
/*  792 */       writer.print(" Max active sessions: ");
/*  793 */       writer.print(mBeanServer.getAttribute(objectName, "maxActive"));
/*  794 */       writer.print(" Rejected session creations: ");
/*  795 */       writer.print(mBeanServer
/*  796 */         .getAttribute(objectName, "rejectedSessions"));
/*  797 */       writer.print(" Expired sessions: ");
/*  798 */       writer.print(mBeanServer
/*  799 */         .getAttribute(objectName, "expiredSessions"));
/*  800 */       writer.print(" Longest session alive time: ");
/*  801 */       writer.print(formatSeconds(mBeanServer.getAttribute(objectName, "sessionMaxAliveTime")));
/*      */       
/*      */ 
/*  804 */       writer.print(" Average session alive time: ");
/*  805 */       writer.print(formatSeconds(mBeanServer.getAttribute(objectName, "sessionAverageAliveTime")));
/*      */       
/*      */ 
/*  808 */       writer.print(" Processing time: ");
/*  809 */       writer.print(formatTime(mBeanServer
/*  810 */         .getAttribute(objectName, "processingTime"), false));
/*  811 */     } else if (mode != 1) {}
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
/*      */   public static void writeJspMonitor(PrintWriter writer, Set<ObjectName> jspMonitorONs, MBeanServer mBeanServer, int mode)
/*      */     throws Exception
/*      */   {
/*  833 */     int jspCount = 0;
/*  834 */     int jspReloadCount = 0;
/*      */     
/*  836 */     Iterator<ObjectName> iter = jspMonitorONs.iterator();
/*  837 */     while (iter.hasNext()) {
/*  838 */       ObjectName jspMonitorON = (ObjectName)iter.next();
/*  839 */       Object obj = mBeanServer.getAttribute(jspMonitorON, "jspCount");
/*  840 */       jspCount += ((Integer)obj).intValue();
/*  841 */       obj = mBeanServer.getAttribute(jspMonitorON, "jspReloadCount");
/*  842 */       jspReloadCount += ((Integer)obj).intValue();
/*      */     }
/*      */     
/*  845 */     if (mode == 0) {
/*  846 */       writer.print("<br>");
/*  847 */       writer.print(" JSPs loaded: ");
/*  848 */       writer.print(jspCount);
/*  849 */       writer.print(" JSPs reloaded: ");
/*  850 */       writer.print(jspReloadCount);
/*  851 */     } else if (mode != 1) {}
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
/*      */   public static void writeWrapper(PrintWriter writer, ObjectName objectName, MBeanServer mBeanServer, int mode)
/*      */     throws Exception
/*      */   {
/*  870 */     if (mode == 0) {
/*  871 */       String servletName = objectName.getKeyProperty("name");
/*      */       
/*      */ 
/*  874 */       String[] mappings = (String[])mBeanServer.invoke(objectName, "findMappings", null, null);
/*      */       
/*  876 */       writer.print("<h2>");
/*  877 */       writer.print(Escape.htmlElementContext(servletName));
/*  878 */       if ((mappings != null) && (mappings.length > 0)) {
/*  879 */         writer.print(" [ ");
/*  880 */         for (int i = 0; i < mappings.length; i++) {
/*  881 */           writer.print(Escape.htmlElementContext(mappings[i]));
/*  882 */           if (i < mappings.length - 1) {
/*  883 */             writer.print(" , ");
/*      */           }
/*      */         }
/*  886 */         writer.print(" ] ");
/*      */       }
/*  888 */       writer.print("</h2>");
/*      */       
/*  890 */       writer.print("<p>");
/*  891 */       writer.print(" Processing time: ");
/*  892 */       writer.print(formatTime(mBeanServer
/*  893 */         .getAttribute(objectName, "processingTime"), true));
/*  894 */       writer.print(" Max time: ");
/*  895 */       writer.print(formatTime(mBeanServer
/*  896 */         .getAttribute(objectName, "maxTime"), false));
/*  897 */       writer.print(" Request count: ");
/*  898 */       writer.print(mBeanServer.getAttribute(objectName, "requestCount"));
/*  899 */       writer.print(" Error count: ");
/*  900 */       writer.print(mBeanServer.getAttribute(objectName, "errorCount"));
/*  901 */       writer.print(" Load time: ");
/*  902 */       writer.print(formatTime(mBeanServer
/*  903 */         .getAttribute(objectName, "loadTime"), false));
/*  904 */       writer.print(" Classloading time: ");
/*  905 */       writer.print(formatTime(mBeanServer
/*  906 */         .getAttribute(objectName, "classLoadTime"), false));
/*  907 */       writer.print("</p>");
/*  908 */     } else if (mode != 1) {}
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
/*      */   @Deprecated
/*      */   public static String filter(Object obj)
/*      */   {
/*  928 */     if (obj == null)
/*  929 */       return "?";
/*  930 */     String message = obj.toString();
/*      */     
/*  932 */     char[] content = new char[message.length()];
/*  933 */     message.getChars(0, message.length(), content, 0);
/*  934 */     StringBuilder result = new StringBuilder(content.length + 50);
/*  935 */     for (int i = 0; i < content.length; i++) {
/*  936 */       switch (content[i]) {
/*      */       case '<': 
/*  938 */         result.append("&lt;");
/*  939 */         break;
/*      */       case '>': 
/*  941 */         result.append("&gt;");
/*  942 */         break;
/*      */       case '&': 
/*  944 */         result.append("&amp;");
/*  945 */         break;
/*      */       case '"': 
/*  947 */         result.append("&quot;");
/*  948 */         break;
/*      */       default: 
/*  950 */         result.append(content[i]);
/*      */       }
/*      */     }
/*  953 */     return result.toString();
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
/*      */   @Deprecated
/*      */   public static String filterXml(String s)
/*      */   {
/*  967 */     if (s == null)
/*  968 */       return "";
/*  969 */     StringBuilder sb = new StringBuilder();
/*  970 */     for (int i = 0; i < s.length(); i++) {
/*  971 */       char c = s.charAt(i);
/*  972 */       if (c == '<') {
/*  973 */         sb.append("&lt;");
/*  974 */       } else if (c == '>') {
/*  975 */         sb.append("&gt;");
/*  976 */       } else if (c == '\'') {
/*  977 */         sb.append("&apos;");
/*  978 */       } else if (c == '&') {
/*  979 */         sb.append("&amp;");
/*  980 */       } else if (c == '"') {
/*  981 */         sb.append("&quot;");
/*      */       } else {
/*  983 */         sb.append(c);
/*      */       }
/*      */     }
/*  986 */     return sb.toString();
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
/*      */   public static String formatSize(Object obj, boolean mb)
/*      */   {
/*  999 */     long bytes = -1L;
/*      */     
/* 1001 */     if ((obj instanceof Long)) {
/* 1002 */       bytes = ((Long)obj).longValue();
/* 1003 */     } else if ((obj instanceof Integer)) {
/* 1004 */       bytes = ((Integer)obj).intValue();
/*      */     }
/*      */     
/* 1007 */     if (mb) {
/* 1008 */       StringBuilder buff = new StringBuilder();
/* 1009 */       if (bytes < 0L) {
/* 1010 */         buff.append('-');
/* 1011 */         bytes = -bytes;
/*      */       }
/* 1013 */       long mbytes = bytes / 1048576L;
/* 1014 */       long rest = (bytes - mbytes * 1048576L) * 100L / 1048576L;
/*      */       
/* 1016 */       buff.append(mbytes).append('.');
/* 1017 */       if (rest < 10L) {
/* 1018 */         buff.append('0');
/*      */       }
/* 1020 */       buff.append(rest).append(" MB");
/* 1021 */       return buff.toString();
/*      */     }
/* 1023 */     return bytes / 1024L + " KB";
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
/*      */   public static String formatTime(Object obj, boolean seconds)
/*      */   {
/* 1038 */     long time = -1L;
/*      */     
/* 1040 */     if ((obj instanceof Long)) {
/* 1041 */       time = ((Long)obj).longValue();
/* 1042 */     } else if ((obj instanceof Integer)) {
/* 1043 */       time = ((Integer)obj).intValue();
/*      */     }
/*      */     
/* 1046 */     if (seconds) {
/* 1047 */       return (float)time / 1000.0F + " s";
/*      */     }
/* 1049 */     return time + " ms";
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
/*      */   public static String formatSeconds(Object obj)
/*      */   {
/* 1062 */     long time = -1L;
/*      */     
/* 1064 */     if ((obj instanceof Long)) {
/* 1065 */       time = ((Long)obj).longValue();
/* 1066 */     } else if ((obj instanceof Integer)) {
/* 1067 */       time = ((Integer)obj).intValue();
/*      */     }
/*      */     
/* 1070 */     return time + " s";
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\manager\StatusTransformer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */