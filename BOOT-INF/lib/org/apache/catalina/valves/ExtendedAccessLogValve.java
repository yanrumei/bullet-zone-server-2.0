/*     */ package org.apache.catalina.valves;
/*     */ 
/*     */ import java.io.CharArrayWriter;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringReader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.URLEncoder;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.http.Cookie;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.connector.Request;
/*     */ import org.apache.catalina.connector.Response;
/*     */ import org.apache.catalina.util.ServerInfo;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExtendedAccessLogValve
/*     */   extends AccessLogValve
/*     */ {
/* 129 */   private static final Log log = LogFactory.getLog(ExtendedAccessLogValve.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static final String extendedAccessLogInfo = "org.apache.catalina.valves.ExtendedAccessLogValve/2.1";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static String wrap(Object value)
/*     */   {
/* 156 */     if ((value == null) || ("-".equals(value))) {
/* 157 */       return "-";
/*     */     }
/*     */     try
/*     */     {
/* 161 */       svalue = value.toString();
/*     */     } catch (Throwable e) { String svalue;
/* 163 */       ExceptionUtils.handleThrowable(e);
/*     */       
/* 165 */       return "-";
/*     */     }
/*     */     
/*     */     String svalue;
/* 169 */     StringBuilder buffer = new StringBuilder(svalue.length() + 2);
/* 170 */     buffer.append('"');
/* 171 */     int i = 0;
/* 172 */     while (i < svalue.length()) {
/* 173 */       int j = svalue.indexOf('"', i);
/* 174 */       if (j == -1) {
/* 175 */         buffer.append(svalue.substring(i));
/* 176 */         i = svalue.length();
/*     */       } else {
/* 178 */         buffer.append(svalue.substring(i, j + 1));
/* 179 */         buffer.append('"');
/* 180 */         i = j + 1;
/*     */       }
/*     */     }
/*     */     
/* 184 */     buffer.append('"');
/* 185 */     return buffer.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected synchronized void open()
/*     */   {
/* 193 */     super.open();
/* 194 */     if (this.currentLogFile.length() == 0L) {
/* 195 */       this.writer.println("#Fields: " + this.pattern);
/* 196 */       this.writer.println("#Version: 2.0");
/* 197 */       this.writer.println("#Software: " + ServerInfo.getServerInfo());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected static class DateElement
/*     */     implements AbstractAccessLogValve.AccessLogElement
/*     */   {
/*     */     private static final long INTERVAL = 86400000L;
/*     */     
/*     */ 
/* 209 */     private static final ThreadLocal<ExtendedAccessLogValve.ElementTimestampStruct> currentDate = new ThreadLocal()
/*     */     {
/*     */       protected ExtendedAccessLogValve.ElementTimestampStruct initialValue()
/*     */       {
/* 213 */         return new ExtendedAccessLogValve.ElementTimestampStruct("yyyy-MM-dd");
/*     */       }
/*     */     };
/*     */     
/*     */ 
/*     */     public void addElement(CharArrayWriter buf, Date date, Request request, Response response, long time)
/*     */     {
/* 220 */       ExtendedAccessLogValve.ElementTimestampStruct eds = (ExtendedAccessLogValve.ElementTimestampStruct)currentDate.get();
/* 221 */       long millis = eds.currentTimestamp.getTime();
/* 222 */       if ((date.getTime() > millis + 86400000L - 1L) || 
/* 223 */         (date.getTime() < millis)) {
/* 224 */         eds.currentTimestamp.setTime(date
/* 225 */           .getTime() - date.getTime() % 86400000L);
/* 226 */         eds.currentTimestampString = 
/* 227 */           eds.currentTimestampFormat.format(eds.currentTimestamp);
/*     */       }
/* 229 */       buf.append(eds.currentTimestampString);
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class TimeElement
/*     */     implements AbstractAccessLogValve.AccessLogElement
/*     */   {
/*     */     private static final long INTERVAL = 1000L;
/* 237 */     private static final ThreadLocal<ExtendedAccessLogValve.ElementTimestampStruct> currentTime = new ThreadLocal()
/*     */     {
/*     */       protected ExtendedAccessLogValve.ElementTimestampStruct initialValue()
/*     */       {
/* 241 */         return new ExtendedAccessLogValve.ElementTimestampStruct("HH:mm:ss");
/*     */       }
/*     */     };
/*     */     
/*     */ 
/*     */     public void addElement(CharArrayWriter buf, Date date, Request request, Response response, long time)
/*     */     {
/* 248 */       ExtendedAccessLogValve.ElementTimestampStruct eds = (ExtendedAccessLogValve.ElementTimestampStruct)currentTime.get();
/* 249 */       long millis = eds.currentTimestamp.getTime();
/* 250 */       if ((date.getTime() > millis + 1000L - 1L) || 
/* 251 */         (date.getTime() < millis)) {
/* 252 */         eds.currentTimestamp.setTime(date
/* 253 */           .getTime() - date.getTime() % 1000L);
/* 254 */         eds.currentTimestampString = 
/* 255 */           eds.currentTimestampFormat.format(eds.currentTimestamp);
/*     */       }
/* 257 */       buf.append(eds.currentTimestampString);
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class RequestHeaderElement implements AbstractAccessLogValve.AccessLogElement {
/*     */     private final String header;
/*     */     
/*     */     public RequestHeaderElement(String header) {
/* 265 */       this.header = header;
/*     */     }
/*     */     
/*     */     public void addElement(CharArrayWriter buf, Date date, Request request, Response response, long time)
/*     */     {
/* 270 */       buf.append(ExtendedAccessLogValve.wrap(request.getHeader(this.header)));
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class ResponseHeaderElement implements AbstractAccessLogValve.AccessLogElement {
/*     */     private final String header;
/*     */     
/*     */     public ResponseHeaderElement(String header) {
/* 278 */       this.header = header;
/*     */     }
/*     */     
/*     */ 
/*     */     public void addElement(CharArrayWriter buf, Date date, Request request, Response response, long time)
/*     */     {
/* 284 */       buf.append(ExtendedAccessLogValve.wrap(response.getHeader(this.header)));
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class ServletContextElement implements AbstractAccessLogValve.AccessLogElement {
/*     */     private final String attribute;
/*     */     
/*     */     public ServletContextElement(String attribute) {
/* 292 */       this.attribute = attribute;
/*     */     }
/*     */     
/*     */     public void addElement(CharArrayWriter buf, Date date, Request request, Response response, long time)
/*     */     {
/* 297 */       buf.append(ExtendedAccessLogValve.wrap(request.getContext().getServletContext()
/* 298 */         .getAttribute(this.attribute)));
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class CookieElement implements AbstractAccessLogValve.AccessLogElement {
/*     */     private final String name;
/*     */     
/*     */     public CookieElement(String name) {
/* 306 */       this.name = name;
/*     */     }
/*     */     
/*     */     public void addElement(CharArrayWriter buf, Date date, Request request, Response response, long time)
/*     */     {
/* 311 */       Cookie[] c = request.getCookies();
/* 312 */       for (int i = 0; (c != null) && (i < c.length); i++) {
/* 313 */         if (this.name.equals(c[i].getName())) {
/* 314 */           buf.append(ExtendedAccessLogValve.wrap(c[i].getValue()));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class ResponseAllHeaderElement
/*     */     implements AbstractAccessLogValve.AccessLogElement
/*     */   {
/*     */     private final String header;
/*     */     
/*     */     public ResponseAllHeaderElement(String header)
/*     */     {
/* 327 */       this.header = header;
/*     */     }
/*     */     
/*     */ 
/*     */     public void addElement(CharArrayWriter buf, Date date, Request request, Response response, long time)
/*     */     {
/* 333 */       if (null != response) {
/* 334 */         Iterator<String> iter = response.getHeaders(this.header).iterator();
/* 335 */         if (iter.hasNext()) {
/* 336 */           StringBuilder buffer = new StringBuilder();
/* 337 */           boolean first = true;
/* 338 */           while (iter.hasNext()) {
/* 339 */             if (first) {
/* 340 */               first = false;
/*     */             } else {
/* 342 */               buffer.append(",");
/*     */             }
/* 344 */             buffer.append((String)iter.next());
/*     */           }
/* 346 */           buf.append(ExtendedAccessLogValve.wrap(buffer.toString()));
/*     */         }
/* 348 */         return;
/*     */       }
/* 350 */       buf.append("-");
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class RequestAttributeElement implements AbstractAccessLogValve.AccessLogElement {
/*     */     private final String attribute;
/*     */     
/*     */     public RequestAttributeElement(String attribute) {
/* 358 */       this.attribute = attribute;
/*     */     }
/*     */     
/*     */ 
/*     */     public void addElement(CharArrayWriter buf, Date date, Request request, Response response, long time)
/*     */     {
/* 364 */       buf.append(ExtendedAccessLogValve.wrap(request.getAttribute(this.attribute)));
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class SessionAttributeElement implements AbstractAccessLogValve.AccessLogElement {
/*     */     private final String attribute;
/*     */     
/*     */     public SessionAttributeElement(String attribute) {
/* 372 */       this.attribute = attribute;
/*     */     }
/*     */     
/*     */     public void addElement(CharArrayWriter buf, Date date, Request request, Response response, long time)
/*     */     {
/* 377 */       HttpSession session = null;
/* 378 */       if (request != null) {
/* 379 */         session = request.getSession(false);
/* 380 */         if (session != null) {
/* 381 */           buf.append(ExtendedAccessLogValve.wrap(session.getAttribute(this.attribute)));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class RequestParameterElement implements AbstractAccessLogValve.AccessLogElement {
/*     */     private final String parameter;
/*     */     
/*     */     public RequestParameterElement(String parameter) {
/* 391 */       this.parameter = parameter;
/*     */     }
/*     */     
/*     */ 
/*     */     private String urlEncode(String value)
/*     */     {
/* 397 */       if ((null == value) || (value.length() == 0)) {
/* 398 */         return null;
/*     */       }
/*     */       try {
/* 401 */         return URLEncoder.encode(value, "UTF-8");
/*     */       }
/*     */       catch (UnsupportedEncodingException e) {}
/* 404 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void addElement(CharArrayWriter buf, Date date, Request request, Response response, long time)
/*     */     {
/* 411 */       buf.append(ExtendedAccessLogValve.wrap(urlEncode(request.getParameter(this.parameter))));
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class PatternTokenizer {
/*     */     private final StringReader sr;
/* 417 */     private StringBuilder buf = new StringBuilder();
/* 418 */     private boolean ended = false;
/*     */     private boolean subToken;
/*     */     private boolean parameter;
/*     */     
/*     */     public PatternTokenizer(String str) {
/* 423 */       this.sr = new StringReader(str);
/*     */     }
/*     */     
/*     */     public boolean hasSubToken() {
/* 427 */       return this.subToken;
/*     */     }
/*     */     
/*     */     public boolean hasParameter() {
/* 431 */       return this.parameter;
/*     */     }
/*     */     
/*     */     public String getToken() throws IOException {
/* 435 */       if (this.ended) {
/* 436 */         return null;
/*     */       }
/*     */       
/* 439 */       String result = null;
/* 440 */       this.subToken = false;
/* 441 */       this.parameter = false;
/*     */       
/* 443 */       int c = this.sr.read();
/* 444 */       while (c != -1) {
/* 445 */         switch (c) {
/*     */         case 32: 
/* 447 */           result = this.buf.toString();
/* 448 */           this.buf = new StringBuilder();
/* 449 */           this.buf.append((char)c);
/* 450 */           return result;
/*     */         case 45: 
/* 452 */           result = this.buf.toString();
/* 453 */           this.buf = new StringBuilder();
/* 454 */           this.subToken = true;
/* 455 */           return result;
/*     */         case 40: 
/* 457 */           result = this.buf.toString();
/* 458 */           this.buf = new StringBuilder();
/* 459 */           this.parameter = true;
/* 460 */           return result;
/*     */         case 41: 
/* 462 */           result = this.buf.toString();
/* 463 */           this.buf = new StringBuilder();
/* 464 */           break;
/*     */         default: 
/* 466 */           this.buf.append((char)c);
/*     */         }
/* 468 */         c = this.sr.read();
/*     */       }
/* 470 */       this.ended = true;
/* 471 */       if (this.buf.length() != 0) {
/* 472 */         return this.buf.toString();
/*     */       }
/* 474 */       return null;
/*     */     }
/*     */     
/*     */     public String getParameter()
/*     */       throws IOException
/*     */     {
/* 480 */       if (!this.parameter) {
/* 481 */         return null;
/*     */       }
/* 483 */       this.parameter = false;
/* 484 */       int c = this.sr.read();
/* 485 */       while (c != -1) {
/* 486 */         if (c == 41) {
/* 487 */           String result = this.buf.toString();
/* 488 */           this.buf = new StringBuilder();
/* 489 */           return result;
/*     */         }
/* 491 */         this.buf.append((char)c);
/* 492 */         c = this.sr.read();
/*     */       }
/* 494 */       return null;
/*     */     }
/*     */     
/*     */     public String getWhiteSpaces() throws IOException {
/* 498 */       if (isEnded()) {
/* 499 */         return "";
/*     */       }
/* 501 */       StringBuilder whiteSpaces = new StringBuilder();
/* 502 */       if (this.buf.length() > 0) {
/* 503 */         whiteSpaces.append(this.buf);
/* 504 */         this.buf = new StringBuilder();
/*     */       }
/* 506 */       int c = this.sr.read();
/* 507 */       while (Character.isWhitespace((char)c)) {
/* 508 */         whiteSpaces.append((char)c);
/* 509 */         c = this.sr.read();
/*     */       }
/* 511 */       if (c == -1) {
/* 512 */         this.ended = true;
/*     */       } else {
/* 514 */         this.buf.append((char)c);
/*     */       }
/* 516 */       return whiteSpaces.toString();
/*     */     }
/*     */     
/*     */     public boolean isEnded() {
/* 520 */       return this.ended;
/*     */     }
/*     */     
/*     */     public String getRemains() throws IOException {
/* 524 */       StringBuilder remains = new StringBuilder();
/* 525 */       for (int c = this.sr.read(); c != -1; c = this.sr.read()) {
/* 526 */         remains.append((char)c);
/*     */       }
/* 528 */       return remains.toString();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected AbstractAccessLogValve.AccessLogElement[] createLogElements()
/*     */   {
/* 535 */     if (log.isDebugEnabled()) {
/* 536 */       log.debug("decodePattern, pattern =" + this.pattern);
/*     */     }
/* 538 */     List<AbstractAccessLogValve.AccessLogElement> list = new ArrayList();
/*     */     
/* 540 */     PatternTokenizer tokenizer = new PatternTokenizer(this.pattern);
/*     */     
/*     */     try
/*     */     {
/* 544 */       tokenizer.getWhiteSpaces();
/*     */       
/* 546 */       if (tokenizer.isEnded()) {
/* 547 */         log.info("pattern was just empty or whitespace");
/* 548 */         return null;
/*     */       }
/*     */       
/* 551 */       String token = tokenizer.getToken();
/* 552 */       while (token != null) {
/* 553 */         if (log.isDebugEnabled()) {
/* 554 */           log.debug("token = " + token);
/*     */         }
/* 556 */         AbstractAccessLogValve.AccessLogElement element = getLogElement(token, tokenizer);
/* 557 */         if (element == null) {
/*     */           break;
/*     */         }
/* 560 */         list.add(element);
/* 561 */         String whiteSpaces = tokenizer.getWhiteSpaces();
/* 562 */         if (whiteSpaces.length() > 0) {
/* 563 */           list.add(new AbstractAccessLogValve.StringElement(whiteSpaces));
/*     */         }
/* 565 */         if (tokenizer.isEnded()) {
/*     */           break;
/*     */         }
/* 568 */         token = tokenizer.getToken();
/*     */       }
/* 570 */       if (log.isDebugEnabled()) {
/* 571 */         log.debug("finished decoding with element size of: " + list.size());
/*     */       }
/* 573 */       return (AbstractAccessLogValve.AccessLogElement[])list.toArray(new AbstractAccessLogValve.AccessLogElement[0]);
/*     */     } catch (IOException e) {
/* 575 */       log.error("parse error", e); }
/* 576 */     return null;
/*     */   }
/*     */   
/*     */   protected AbstractAccessLogValve.AccessLogElement getLogElement(String token, PatternTokenizer tokenizer) throws IOException
/*     */   {
/* 581 */     if ("date".equals(token))
/* 582 */       return new DateElement();
/* 583 */     if ("time".equals(token)) {
/* 584 */       if (tokenizer.hasSubToken()) {
/* 585 */         String nextToken = tokenizer.getToken();
/* 586 */         if ("taken".equals(nextToken)) {
/* 587 */           return new AbstractAccessLogValve.ElapsedTimeElement(false);
/*     */         }
/*     */       } else {
/* 590 */         return new TimeElement();
/*     */       }
/* 592 */     } else { if ("bytes".equals(token))
/* 593 */         return new AbstractAccessLogValve.ByteSentElement(true);
/* 594 */       if ("cached".equals(token))
/*     */       {
/* 596 */         return new AbstractAccessLogValve.StringElement("-"); }
/* 597 */       if ("c".equals(token)) {
/* 598 */         String nextToken = tokenizer.getToken();
/* 599 */         if ("ip".equals(nextToken))
/* 600 */           return new AbstractAccessLogValve.RemoteAddrElement(this);
/* 601 */         if ("dns".equals(nextToken)) {
/* 602 */           return new AbstractAccessLogValve.HostElement(this);
/*     */         }
/* 604 */       } else if ("s".equals(token)) {
/* 605 */         String nextToken = tokenizer.getToken();
/* 606 */         if ("ip".equals(nextToken))
/* 607 */           return new AbstractAccessLogValve.LocalAddrElement();
/* 608 */         if ("dns".equals(nextToken))
/* 609 */           new AbstractAccessLogValve.AccessLogElement()
/*     */           {
/*     */             public void addElement(CharArrayWriter buf, Date date, Request request, Response response, long time)
/*     */             {
/*     */               String value;
/*     */               try {
/* 615 */                 value = InetAddress.getLocalHost().getHostName();
/*     */               } catch (Throwable e) { String value;
/* 617 */                 ExceptionUtils.handleThrowable(e);
/* 618 */                 value = "localhost";
/*     */               }
/* 620 */               buf.append(value);
/*     */             }
/*     */           };
/*     */       } else {
/* 624 */         if ("cs".equals(token))
/* 625 */           return getClientToServerElement(tokenizer);
/* 626 */         if ("sc".equals(token))
/* 627 */           return getServerToClientElement(tokenizer);
/* 628 */         if (("sr".equals(token)) || ("rs".equals(token)))
/* 629 */           return getProxyElement(tokenizer);
/* 630 */         if ("x".equals(token))
/* 631 */           return getXParameterElement(tokenizer);
/*     */       } }
/* 633 */     log.error("unable to decode with rest of chars starting: " + token);
/* 634 */     return null;
/*     */   }
/*     */   
/*     */   protected AbstractAccessLogValve.AccessLogElement getClientToServerElement(PatternTokenizer tokenizer) throws IOException
/*     */   {
/* 639 */     if (tokenizer.hasSubToken()) {
/* 640 */       String token = tokenizer.getToken();
/* 641 */       if ("method".equals(token))
/* 642 */         return new AbstractAccessLogValve.MethodElement();
/* 643 */       if ("uri".equals(token)) {
/* 644 */         if (tokenizer.hasSubToken()) {
/* 645 */           token = tokenizer.getToken();
/* 646 */           if ("stem".equals(token))
/* 647 */             return new AbstractAccessLogValve.RequestURIElement();
/* 648 */           if ("query".equals(token)) {
/* 649 */             new AbstractAccessLogValve.AccessLogElement()
/*     */             {
/*     */ 
/*     */               public void addElement(CharArrayWriter buf, Date date, Request request, Response response, long time)
/*     */               {
/* 654 */                 String query = request.getQueryString();
/* 655 */                 if (query != null) {
/* 656 */                   buf.append(query);
/*     */                 } else {
/* 658 */                   buf.append('-');
/*     */                 }
/*     */               }
/*     */             };
/*     */           }
/*     */         } else {
/* 664 */           new AbstractAccessLogValve.AccessLogElement()
/*     */           {
/*     */             public void addElement(CharArrayWriter buf, Date date, Request request, Response response, long time)
/*     */             {
/* 668 */               String query = request.getQueryString();
/* 669 */               if (query == null) {
/* 670 */                 buf.append(request.getRequestURI());
/*     */               } else {
/* 672 */                 buf.append(request.getRequestURI());
/* 673 */                 buf.append('?');
/* 674 */                 buf.append(request.getQueryString());
/*     */               }
/*     */             }
/*     */           };
/*     */         }
/*     */       }
/* 680 */     } else if (tokenizer.hasParameter()) {
/* 681 */       String parameter = tokenizer.getParameter();
/* 682 */       if (parameter == null) {
/* 683 */         log.error("No closing ) found for in decode");
/* 684 */         return null;
/*     */       }
/* 686 */       return new RequestHeaderElement(parameter);
/*     */     }
/* 688 */     log.error("The next characters couldn't be decoded: " + tokenizer
/* 689 */       .getRemains());
/* 690 */     return null;
/*     */   }
/*     */   
/*     */   protected AbstractAccessLogValve.AccessLogElement getServerToClientElement(PatternTokenizer tokenizer) throws IOException
/*     */   {
/* 695 */     if (tokenizer.hasSubToken()) {
/* 696 */       String token = tokenizer.getToken();
/* 697 */       if ("status".equals(token))
/* 698 */         return new AbstractAccessLogValve.HttpStatusCodeElement();
/* 699 */       if ("comment".equals(token)) {
/* 700 */         return new AbstractAccessLogValve.StringElement("?");
/*     */       }
/* 702 */     } else if (tokenizer.hasParameter()) {
/* 703 */       String parameter = tokenizer.getParameter();
/* 704 */       if (parameter == null) {
/* 705 */         log.error("No closing ) found for in decode");
/* 706 */         return null;
/*     */       }
/* 708 */       return new ResponseHeaderElement(parameter);
/*     */     }
/* 710 */     log.error("The next characters couldn't be decoded: " + tokenizer
/* 711 */       .getRemains());
/* 712 */     return null;
/*     */   }
/*     */   
/*     */   protected AbstractAccessLogValve.AccessLogElement getProxyElement(PatternTokenizer tokenizer) throws IOException
/*     */   {
/* 717 */     String token = null;
/* 718 */     if (tokenizer.hasSubToken()) {
/* 719 */       tokenizer.getToken();
/* 720 */       return new AbstractAccessLogValve.StringElement("-"); }
/* 721 */     if (tokenizer.hasParameter()) {
/* 722 */       tokenizer.getParameter();
/* 723 */       return new AbstractAccessLogValve.StringElement("-");
/*     */     }
/* 725 */     log.error("The next characters couldn't be decoded: " + token);
/* 726 */     return null;
/*     */   }
/*     */   
/*     */   protected AbstractAccessLogValve.AccessLogElement getXParameterElement(PatternTokenizer tokenizer) throws IOException
/*     */   {
/* 731 */     if (!tokenizer.hasSubToken()) {
/* 732 */       log.error("x param in wrong format. Needs to be 'x-#(...)' read the docs!");
/* 733 */       return null;
/*     */     }
/* 735 */     String token = tokenizer.getToken();
/* 736 */     if ("threadname".equals(token)) {
/* 737 */       return new AbstractAccessLogValve.ThreadNameElement();
/*     */     }
/*     */     
/* 740 */     if (!tokenizer.hasParameter()) {
/* 741 */       log.error("x param in wrong format. Needs to be 'x-#(...)' read the docs!");
/* 742 */       return null;
/*     */     }
/* 744 */     String parameter = tokenizer.getParameter();
/* 745 */     if (parameter == null) {
/* 746 */       log.error("No closing ) found for in decode");
/* 747 */       return null;
/*     */     }
/* 749 */     if ("A".equals(token))
/* 750 */       return new ServletContextElement(parameter);
/* 751 */     if ("C".equals(token))
/* 752 */       return new CookieElement(parameter);
/* 753 */     if ("R".equals(token))
/* 754 */       return new RequestAttributeElement(parameter);
/* 755 */     if ("S".equals(token))
/* 756 */       return new SessionAttributeElement(parameter);
/* 757 */     if ("H".equals(token))
/* 758 */       return getServletRequestElement(parameter);
/* 759 */     if ("P".equals(token))
/* 760 */       return new RequestParameterElement(parameter);
/* 761 */     if ("O".equals(token)) {
/* 762 */       return new ResponseAllHeaderElement(parameter);
/*     */     }
/* 764 */     log.error("x param for servlet request, couldn't decode value: " + token);
/*     */     
/* 766 */     return null;
/*     */   }
/*     */   
/*     */   protected AbstractAccessLogValve.AccessLogElement getServletRequestElement(String parameter) {
/* 770 */     if ("authType".equals(parameter))
/* 771 */       new AbstractAccessLogValve.AccessLogElement()
/*     */       {
/*     */         public void addElement(CharArrayWriter buf, Date date, Request request, Response response, long time)
/*     */         {
/* 775 */           buf.append(ExtendedAccessLogValve.wrap(request.getAuthType()));
/*     */         }
/*     */       };
/* 778 */     if ("remoteUser".equals(parameter))
/* 779 */       new AbstractAccessLogValve.AccessLogElement()
/*     */       {
/*     */         public void addElement(CharArrayWriter buf, Date date, Request request, Response response, long time)
/*     */         {
/* 783 */           buf.append(ExtendedAccessLogValve.wrap(request.getRemoteUser()));
/*     */         }
/*     */       };
/* 786 */     if ("requestedSessionId".equals(parameter))
/* 787 */       new AbstractAccessLogValve.AccessLogElement()
/*     */       {
/*     */         public void addElement(CharArrayWriter buf, Date date, Request request, Response response, long time)
/*     */         {
/* 791 */           buf.append(ExtendedAccessLogValve.wrap(request.getRequestedSessionId()));
/*     */         }
/*     */       };
/* 794 */     if ("requestedSessionIdFromCookie".equals(parameter))
/* 795 */       new AbstractAccessLogValve.AccessLogElement()
/*     */       {
/*     */         public void addElement(CharArrayWriter buf, Date date, Request request, Response response, long time)
/*     */         {
/* 799 */           buf.append(ExtendedAccessLogValve.wrap("" + request
/* 800 */             .isRequestedSessionIdFromCookie()));
/*     */         }
/*     */       };
/* 803 */     if ("requestedSessionIdValid".equals(parameter))
/* 804 */       new AbstractAccessLogValve.AccessLogElement()
/*     */       {
/*     */         public void addElement(CharArrayWriter buf, Date date, Request request, Response response, long time)
/*     */         {
/* 808 */           buf.append(ExtendedAccessLogValve.wrap("" + request.isRequestedSessionIdValid()));
/*     */         }
/*     */       };
/* 811 */     if ("contentLength".equals(parameter))
/* 812 */       new AbstractAccessLogValve.AccessLogElement()
/*     */       {
/*     */         public void addElement(CharArrayWriter buf, Date date, Request request, Response response, long time)
/*     */         {
/* 816 */           buf.append(ExtendedAccessLogValve.wrap("" + request.getContentLengthLong()));
/*     */         }
/*     */       };
/* 819 */     if ("characterEncoding".equals(parameter))
/* 820 */       new AbstractAccessLogValve.AccessLogElement()
/*     */       {
/*     */         public void addElement(CharArrayWriter buf, Date date, Request request, Response response, long time)
/*     */         {
/* 824 */           buf.append(ExtendedAccessLogValve.wrap(request.getCharacterEncoding()));
/*     */         }
/*     */       };
/* 827 */     if ("locale".equals(parameter))
/* 828 */       new AbstractAccessLogValve.AccessLogElement()
/*     */       {
/*     */         public void addElement(CharArrayWriter buf, Date date, Request request, Response response, long time)
/*     */         {
/* 832 */           buf.append(ExtendedAccessLogValve.wrap(request.getLocale()));
/*     */         }
/*     */       };
/* 835 */     if ("protocol".equals(parameter))
/* 836 */       new AbstractAccessLogValve.AccessLogElement()
/*     */       {
/*     */         public void addElement(CharArrayWriter buf, Date date, Request request, Response response, long time)
/*     */         {
/* 840 */           buf.append(ExtendedAccessLogValve.wrap(request.getProtocol()));
/*     */         }
/*     */       };
/* 843 */     if ("scheme".equals(parameter))
/* 844 */       new AbstractAccessLogValve.AccessLogElement()
/*     */       {
/*     */         public void addElement(CharArrayWriter buf, Date date, Request request, Response response, long time)
/*     */         {
/* 848 */           buf.append(request.getScheme());
/*     */         }
/*     */       };
/* 851 */     if ("secure".equals(parameter)) {
/* 852 */       new AbstractAccessLogValve.AccessLogElement()
/*     */       {
/*     */         public void addElement(CharArrayWriter buf, Date date, Request request, Response response, long time)
/*     */         {
/* 856 */           buf.append(ExtendedAccessLogValve.wrap("" + request.isSecure()));
/*     */         }
/*     */       };
/*     */     }
/* 860 */     log.error("x param for servlet request, couldn't decode value: " + parameter);
/*     */     
/* 862 */     return null;
/*     */   }
/*     */   
/*     */   private static class ElementTimestampStruct {
/* 866 */     private final Date currentTimestamp = new Date(0L);
/*     */     private final SimpleDateFormat currentTimestampFormat;
/*     */     private String currentTimestampString;
/*     */     
/*     */     ElementTimestampStruct(String format) {
/* 871 */       this.currentTimestampFormat = new SimpleDateFormat(format, Locale.US);
/* 872 */       this.currentTimestampFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\valves\ExtendedAccessLogValve.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */