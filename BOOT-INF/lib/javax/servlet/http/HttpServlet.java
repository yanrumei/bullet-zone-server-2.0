/*     */ package javax.servlet.http;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Enumeration;
/*     */ import java.util.ResourceBundle;
/*     */ import javax.servlet.DispatcherType;
/*     */ import javax.servlet.GenericServlet;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletOutputStream;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class HttpServlet
/*     */   extends GenericServlet
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private static final String METHOD_DELETE = "DELETE";
/*     */   private static final String METHOD_HEAD = "HEAD";
/*     */   private static final String METHOD_GET = "GET";
/*     */   private static final String METHOD_OPTIONS = "OPTIONS";
/*     */   private static final String METHOD_POST = "POST";
/*     */   private static final String METHOD_PUT = "PUT";
/*     */   private static final String METHOD_TRACE = "TRACE";
/*     */   private static final String HEADER_IFMODSINCE = "If-Modified-Since";
/*     */   private static final String HEADER_LASTMOD = "Last-Modified";
/*     */   private static final String LSTRING_FILE = "javax.servlet.http.LocalStrings";
/*  93 */   private static final ResourceBundle lStrings = ResourceBundle.getBundle("javax.servlet.http.LocalStrings");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doGet(HttpServletRequest req, HttpServletResponse resp)
/*     */     throws ServletException, IOException
/*     */   {
/* 171 */     String protocol = req.getProtocol();
/* 172 */     String msg = lStrings.getString("http.method_get_not_supported");
/* 173 */     if (protocol.endsWith("1.1")) {
/* 174 */       resp.sendError(405, msg);
/*     */     } else {
/* 176 */       resp.sendError(400, msg);
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
/*     */   protected long getLastModified(HttpServletRequest req)
/*     */   {
/* 203 */     return -1L;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doHead(HttpServletRequest req, HttpServletResponse resp)
/*     */     throws ServletException, IOException
/*     */   {
/* 241 */     if (DispatcherType.INCLUDE.equals(req.getDispatcherType())) {
/* 242 */       doGet(req, resp);
/*     */     } else {
/* 244 */       NoBodyResponse response = new NoBodyResponse(resp);
/* 245 */       doGet(req, response);
/* 246 */       response.setContentLength();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doPost(HttpServletRequest req, HttpServletResponse resp)
/*     */     throws ServletException, IOException
/*     */   {
/* 311 */     String protocol = req.getProtocol();
/* 312 */     String msg = lStrings.getString("http.method_post_not_supported");
/* 313 */     if (protocol.endsWith("1.1")) {
/* 314 */       resp.sendError(405, msg);
/*     */     } else {
/* 316 */       resp.sendError(400, msg);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doPut(HttpServletRequest req, HttpServletResponse resp)
/*     */     throws ServletException, IOException
/*     */   {
/* 366 */     String protocol = req.getProtocol();
/* 367 */     String msg = lStrings.getString("http.method_put_not_supported");
/* 368 */     if (protocol.endsWith("1.1")) {
/* 369 */       resp.sendError(405, msg);
/*     */     } else {
/* 371 */       resp.sendError(400, msg);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
/*     */     throws ServletException, IOException
/*     */   {
/* 414 */     String protocol = req.getProtocol();
/* 415 */     String msg = lStrings.getString("http.method_delete_not_supported");
/* 416 */     if (protocol.endsWith("1.1")) {
/* 417 */       resp.sendError(405, msg);
/*     */     } else {
/* 419 */       resp.sendError(400, msg);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static Method[] getAllDeclaredMethods(Class<?> c)
/*     */   {
/* 426 */     if (c.equals(HttpServlet.class)) {
/* 427 */       return null;
/*     */     }
/*     */     
/* 430 */     Method[] parentMethods = getAllDeclaredMethods(c.getSuperclass());
/* 431 */     Method[] thisMethods = c.getDeclaredMethods();
/*     */     
/* 433 */     if ((parentMethods != null) && (parentMethods.length > 0)) {
/* 434 */       Method[] allMethods = new Method[parentMethods.length + thisMethods.length];
/*     */       
/* 436 */       System.arraycopy(parentMethods, 0, allMethods, 0, parentMethods.length);
/*     */       
/* 438 */       System.arraycopy(thisMethods, 0, allMethods, parentMethods.length, thisMethods.length);
/*     */       
/*     */ 
/* 441 */       thisMethods = allMethods;
/*     */     }
/*     */     
/* 444 */     return thisMethods;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
/*     */     throws ServletException, IOException
/*     */   {
/* 483 */     Method[] methods = getAllDeclaredMethods(getClass());
/*     */     
/* 485 */     boolean ALLOW_GET = false;
/* 486 */     boolean ALLOW_HEAD = false;
/* 487 */     boolean ALLOW_POST = false;
/* 488 */     boolean ALLOW_PUT = false;
/* 489 */     boolean ALLOW_DELETE = false;
/* 490 */     boolean ALLOW_TRACE = true;
/* 491 */     boolean ALLOW_OPTIONS = true;
/*     */     
/*     */ 
/* 494 */     Class<?> clazz = null;
/*     */     try {
/* 496 */       clazz = Class.forName("org.apache.catalina.connector.RequestFacade");
/* 497 */       Method getAllowTrace = clazz.getMethod("getAllowTrace", (Class[])null);
/* 498 */       ALLOW_TRACE = ((Boolean)getAllowTrace.invoke(req, (Object[])null)).booleanValue();
/*     */     }
/*     */     catch (ClassNotFoundException|NoSuchMethodException|SecurityException|IllegalAccessException|IllegalArgumentException|InvocationTargetException localClassNotFoundException) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 505 */     for (int i = 0; i < methods.length; i++) {
/* 506 */       Method m = methods[i];
/*     */       
/* 508 */       if (m.getName().equals("doGet")) {
/* 509 */         ALLOW_GET = true;
/* 510 */         ALLOW_HEAD = true;
/*     */       }
/* 512 */       if (m.getName().equals("doPost"))
/* 513 */         ALLOW_POST = true;
/* 514 */       if (m.getName().equals("doPut"))
/* 515 */         ALLOW_PUT = true;
/* 516 */       if (m.getName().equals("doDelete")) {
/* 517 */         ALLOW_DELETE = true;
/*     */       }
/*     */     }
/* 520 */     String allow = null;
/* 521 */     if (ALLOW_GET)
/* 522 */       allow = "GET";
/* 523 */     if (ALLOW_HEAD)
/* 524 */       if (allow == null) allow = "HEAD"; else
/* 525 */         allow = allow + ", HEAD";
/* 526 */     if (ALLOW_POST)
/* 527 */       if (allow == null) allow = "POST"; else
/* 528 */         allow = allow + ", POST";
/* 529 */     if (ALLOW_PUT)
/* 530 */       if (allow == null) allow = "PUT"; else
/* 531 */         allow = allow + ", PUT";
/* 532 */     if (ALLOW_DELETE)
/* 533 */       if (allow == null) allow = "DELETE"; else
/* 534 */         allow = allow + ", DELETE";
/* 535 */     if (ALLOW_TRACE)
/* 536 */       if (allow == null) allow = "TRACE"; else
/* 537 */         allow = allow + ", TRACE";
/* 538 */     if (ALLOW_OPTIONS) {
/* 539 */       if (allow == null) allow = "OPTIONS"; else
/* 540 */         allow = allow + ", OPTIONS";
/*     */     }
/* 542 */     resp.setHeader("Allow", allow);
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
/*     */   protected void doTrace(HttpServletRequest req, HttpServletResponse resp)
/*     */     throws ServletException, IOException
/*     */   {
/* 575 */     String CRLF = "\r\n";
/*     */     
/* 577 */     StringBuilder buffer = new StringBuilder("TRACE ").append(req.getRequestURI()).append(" ").append(req.getProtocol());
/*     */     
/* 579 */     Enumeration<String> reqHeaderEnum = req.getHeaderNames();
/*     */     
/* 581 */     while (reqHeaderEnum.hasMoreElements()) {
/* 582 */       String headerName = (String)reqHeaderEnum.nextElement();
/* 583 */       buffer.append(CRLF).append(headerName).append(": ")
/* 584 */         .append(req.getHeader(headerName));
/*     */     }
/*     */     
/* 587 */     buffer.append(CRLF);
/*     */     
/* 589 */     int responseLength = buffer.length();
/*     */     
/* 591 */     resp.setContentType("message/http");
/* 592 */     resp.setContentLength(responseLength);
/* 593 */     ServletOutputStream out = resp.getOutputStream();
/* 594 */     out.print(buffer.toString());
/* 595 */     out.close();
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
/*     */   protected void service(HttpServletRequest req, HttpServletResponse resp)
/*     */     throws ServletException, IOException
/*     */   {
/* 628 */     String method = req.getMethod();
/*     */     
/* 630 */     if (method.equals("GET")) {
/* 631 */       long lastModified = getLastModified(req);
/* 632 */       if (lastModified == -1L)
/*     */       {
/*     */ 
/* 635 */         doGet(req, resp);
/*     */       } else {
/*     */         long ifModifiedSince;
/*     */         try {
/* 639 */           ifModifiedSince = req.getDateHeader("If-Modified-Since");
/*     */         } catch (IllegalArgumentException iae) {
/*     */           long ifModifiedSince;
/* 642 */           ifModifiedSince = -1L;
/*     */         }
/* 644 */         if (ifModifiedSince < lastModified / 1000L * 1000L)
/*     */         {
/*     */ 
/*     */ 
/* 648 */           maybeSetLastModified(resp, lastModified);
/* 649 */           doGet(req, resp);
/*     */         } else {
/* 651 */           resp.setStatus(304);
/*     */         }
/*     */       }
/*     */     }
/* 655 */     else if (method.equals("HEAD")) {
/* 656 */       long lastModified = getLastModified(req);
/* 657 */       maybeSetLastModified(resp, lastModified);
/* 658 */       doHead(req, resp);
/*     */     }
/* 660 */     else if (method.equals("POST")) {
/* 661 */       doPost(req, resp);
/*     */     }
/* 663 */     else if (method.equals("PUT")) {
/* 664 */       doPut(req, resp);
/*     */     }
/* 666 */     else if (method.equals("DELETE")) {
/* 667 */       doDelete(req, resp);
/*     */     }
/* 669 */     else if (method.equals("OPTIONS")) {
/* 670 */       doOptions(req, resp);
/*     */     }
/* 672 */     else if (method.equals("TRACE")) {
/* 673 */       doTrace(req, resp);
/*     */ 
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/*     */ 
/* 681 */       String errMsg = lStrings.getString("http.method_not_implemented");
/* 682 */       Object[] errArgs = new Object[1];
/* 683 */       errArgs[0] = method;
/* 684 */       errMsg = MessageFormat.format(errMsg, errArgs);
/*     */       
/* 686 */       resp.sendError(501, errMsg);
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
/*     */   private void maybeSetLastModified(HttpServletResponse resp, long lastModified)
/*     */   {
/* 700 */     if (resp.containsHeader("Last-Modified"))
/* 701 */       return;
/* 702 */     if (lastModified >= 0L) {
/* 703 */       resp.setDateHeader("Last-Modified", lastModified);
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
/*     */   public void service(ServletRequest req, ServletResponse res)
/*     */     throws ServletException, IOException
/*     */   {
/*     */     try
/*     */     {
/* 737 */       HttpServletRequest request = (HttpServletRequest)req;
/* 738 */       response = (HttpServletResponse)res;
/*     */     } catch (ClassCastException e) { HttpServletResponse response;
/* 740 */       throw new ServletException("non-HTTP request or response"); }
/*     */     HttpServletResponse response;
/* 742 */     HttpServletRequest request; service(request, response);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\http\HttpServlet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */