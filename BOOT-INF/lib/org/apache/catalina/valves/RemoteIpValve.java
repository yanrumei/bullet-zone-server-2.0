/*     */ package org.apache.catalina.valves;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.servlet.ServletException;
/*     */ import org.apache.catalina.Valve;
/*     */ import org.apache.catalina.connector.Response;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.buf.MessageBytes;
/*     */ import org.apache.tomcat.util.http.MimeHeaders;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RemoteIpValve
/*     */   extends ValveBase
/*     */ {
/* 354 */   private static final Pattern commaSeparatedValuesPattern = Pattern.compile("\\s*,\\s*");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 359 */   private static final Log log = LogFactory.getLog(RemoteIpValve.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static String[] commaDelimitedListToStringArray(String commaDelimitedStrings)
/*     */   {
/* 367 */     return (commaDelimitedStrings == null) || (commaDelimitedStrings.length() == 0) ? new String[0] : commaSeparatedValuesPattern
/* 368 */       .split(commaDelimitedStrings);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static String listToCommaDelimitedString(List<String> stringList)
/*     */   {
/* 377 */     if (stringList == null) {
/* 378 */       return "";
/*     */     }
/* 380 */     StringBuilder result = new StringBuilder();
/* 381 */     for (Iterator<String> it = stringList.iterator(); it.hasNext();) {
/* 382 */       Object element = it.next();
/* 383 */       if (element != null) {
/* 384 */         result.append(element);
/* 385 */         if (it.hasNext()) {
/* 386 */           result.append(", ");
/*     */         }
/*     */       }
/*     */     }
/* 390 */     return result.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 396 */   private int httpServerPort = 80;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 401 */   private int httpsServerPort = 443;
/*     */   
/* 403 */   private boolean changeLocalPort = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 408 */   private Pattern internalProxies = Pattern.compile("10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}|192\\.168\\.\\d{1,3}\\.\\d{1,3}|169\\.254\\.\\d{1,3}\\.\\d{1,3}|127\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}|172\\.1[6-9]{1}\\.\\d{1,3}\\.\\d{1,3}|172\\.2[0-9]{1}\\.\\d{1,3}\\.\\d{1,3}|172\\.3[0-1]{1}\\.\\d{1,3}\\.\\d{1,3}");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 420 */   private String protocolHeader = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 425 */   private String protocolHeaderHttpsValue = "https";
/*     */   
/* 427 */   private String portHeader = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 432 */   private String proxiesHeader = "X-Forwarded-By";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 437 */   private String remoteIpHeader = "X-Forwarded-For";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 442 */   private boolean requestAttributesEnabled = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 447 */   private Pattern trustedProxies = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RemoteIpValve()
/*     */   {
/* 456 */     super(true);
/*     */   }
/*     */   
/*     */   public int getHttpsServerPort()
/*     */   {
/* 461 */     return this.httpsServerPort;
/*     */   }
/*     */   
/*     */   public int getHttpServerPort() {
/* 465 */     return this.httpServerPort;
/*     */   }
/*     */   
/*     */   public boolean isChangeLocalPort() {
/* 469 */     return this.changeLocalPort;
/*     */   }
/*     */   
/*     */   public void setChangeLocalPort(boolean changeLocalPort) {
/* 473 */     this.changeLocalPort = changeLocalPort;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPortHeader()
/*     */   {
/* 484 */     return this.portHeader;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPortHeader(String portHeader)
/*     */   {
/* 495 */     this.portHeader = portHeader;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getInternalProxies()
/*     */   {
/* 503 */     if (this.internalProxies == null) {
/* 504 */       return null;
/*     */     }
/* 506 */     return this.internalProxies.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getProtocolHeader()
/*     */   {
/* 514 */     return this.protocolHeader;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getProtocolHeaderHttpsValue()
/*     */   {
/* 522 */     return this.protocolHeaderHttpsValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getProxiesHeader()
/*     */   {
/* 530 */     return this.proxiesHeader;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getRemoteIpHeader()
/*     */   {
/* 538 */     return this.remoteIpHeader;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getRequestAttributesEnabled()
/*     */   {
/* 547 */     return this.requestAttributesEnabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getTrustedProxies()
/*     */   {
/* 555 */     if (this.trustedProxies == null) {
/* 556 */       return null;
/*     */     }
/* 558 */     return this.trustedProxies.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void invoke(org.apache.catalina.connector.Request request, Response response)
/*     */     throws IOException, ServletException
/*     */   {
/* 566 */     String originalRemoteAddr = request.getRemoteAddr();
/* 567 */     String originalRemoteHost = request.getRemoteHost();
/* 568 */     String originalScheme = request.getScheme();
/* 569 */     boolean originalSecure = request.isSecure();
/* 570 */     int originalServerPort = request.getServerPort();
/* 571 */     String originalProxiesHeader = request.getHeader(this.proxiesHeader);
/* 572 */     String originalRemoteIpHeader = request.getHeader(this.remoteIpHeader);
/*     */     
/* 574 */     if ((this.internalProxies != null) && 
/* 575 */       (this.internalProxies.matcher(originalRemoteAddr).matches())) {
/* 576 */       String remoteIp = null;
/*     */       
/* 578 */       LinkedList<String> proxiesHeaderValue = new LinkedList();
/* 579 */       StringBuilder concatRemoteIpHeaderValue = new StringBuilder();
/*     */       
/* 581 */       for (Enumeration<String> e = request.getHeaders(this.remoteIpHeader); e.hasMoreElements();) {
/* 582 */         if (concatRemoteIpHeaderValue.length() > 0) {
/* 583 */           concatRemoteIpHeaderValue.append(", ");
/*     */         }
/*     */         
/* 586 */         concatRemoteIpHeaderValue.append((String)e.nextElement());
/*     */       }
/*     */       
/* 589 */       String[] remoteIpHeaderValue = commaDelimitedListToStringArray(concatRemoteIpHeaderValue.toString());
/*     */       
/*     */ 
/* 592 */       for (int idx = remoteIpHeaderValue.length - 1; idx >= 0; idx--) {
/* 593 */         String currentRemoteIp = remoteIpHeaderValue[idx];
/* 594 */         remoteIp = currentRemoteIp;
/* 595 */         if (!this.internalProxies.matcher(currentRemoteIp).matches())
/*     */         {
/* 597 */           if ((this.trustedProxies != null) && 
/* 598 */             (this.trustedProxies.matcher(currentRemoteIp).matches())) {
/* 599 */             proxiesHeaderValue.addFirst(currentRemoteIp);
/*     */           } else {
/* 601 */             idx--;
/* 602 */             break;
/*     */           }
/*     */         }
/*     */       }
/* 606 */       LinkedList<String> newRemoteIpHeaderValue = new LinkedList();
/* 607 */       for (; idx >= 0; idx--) {
/* 608 */         String currentRemoteIp = remoteIpHeaderValue[idx];
/* 609 */         newRemoteIpHeaderValue.addFirst(currentRemoteIp);
/*     */       }
/* 611 */       if (remoteIp != null)
/*     */       {
/* 613 */         request.setRemoteAddr(remoteIp);
/* 614 */         request.setRemoteHost(remoteIp);
/*     */         
/*     */ 
/*     */ 
/* 618 */         if (proxiesHeaderValue.size() == 0) {
/* 619 */           request.getCoyoteRequest().getMimeHeaders().removeHeader(this.proxiesHeader);
/*     */         } else {
/* 621 */           String commaDelimitedListOfProxies = listToCommaDelimitedString(proxiesHeaderValue);
/* 622 */           request.getCoyoteRequest().getMimeHeaders().setValue(this.proxiesHeader).setString(commaDelimitedListOfProxies);
/*     */         }
/* 624 */         if (newRemoteIpHeaderValue.size() == 0) {
/* 625 */           request.getCoyoteRequest().getMimeHeaders().removeHeader(this.remoteIpHeader);
/*     */         } else {
/* 627 */           String commaDelimitedRemoteIpHeaderValue = listToCommaDelimitedString(newRemoteIpHeaderValue);
/* 628 */           request.getCoyoteRequest().getMimeHeaders().setValue(this.remoteIpHeader).setString(commaDelimitedRemoteIpHeaderValue);
/*     */         }
/*     */       }
/*     */       
/* 632 */       if (this.protocolHeader != null) {
/* 633 */         String protocolHeaderValue = request.getHeader(this.protocolHeader);
/* 634 */         if (protocolHeaderValue != null)
/*     */         {
/*     */ 
/* 637 */           if (this.protocolHeaderHttpsValue.equalsIgnoreCase(protocolHeaderValue)) {
/* 638 */             request.setSecure(true);
/*     */             
/* 640 */             request.getCoyoteRequest().scheme().setString("https");
/*     */             
/* 642 */             setPorts(request, this.httpsServerPort);
/*     */           } else {
/* 644 */             request.setSecure(false);
/*     */             
/* 646 */             request.getCoyoteRequest().scheme().setString("http");
/*     */             
/* 648 */             setPorts(request, this.httpServerPort);
/*     */           }
/*     */         }
/*     */       }
/* 652 */       if (log.isDebugEnabled()) {
/* 653 */         log.debug("Incoming request " + request.getRequestURI() + " with originalRemoteAddr '" + originalRemoteAddr + "', originalRemoteHost='" + originalRemoteHost + "', originalSecure='" + originalSecure + "', originalScheme='" + originalScheme + "' will be seen as newRemoteAddr='" + request
/*     */         
/* 655 */           .getRemoteAddr() + "', newRemoteHost='" + request
/* 656 */           .getRemoteHost() + "', newScheme='" + request.getScheme() + "', newSecure='" + request.isSecure() + "'");
/*     */       }
/*     */     }
/* 659 */     else if (log.isDebugEnabled()) {
/* 660 */       log.debug("Skip RemoteIpValve for request " + request.getRequestURI() + " with originalRemoteAddr '" + request
/* 661 */         .getRemoteAddr() + "'");
/*     */     }
/*     */     
/* 664 */     if (this.requestAttributesEnabled) {
/* 665 */       request.setAttribute("org.apache.catalina.AccessLog.RemoteAddr", request
/* 666 */         .getRemoteAddr());
/* 667 */       request.setAttribute("org.apache.tomcat.remoteAddr", request
/* 668 */         .getRemoteAddr());
/* 669 */       request.setAttribute("org.apache.catalina.AccessLog.RemoteHost", request
/* 670 */         .getRemoteHost());
/* 671 */       request.setAttribute("org.apache.catalina.AccessLog.Protocol", request
/* 672 */         .getProtocol());
/* 673 */       request.setAttribute("org.apache.catalina.AccessLog.ServerPort", 
/* 674 */         Integer.valueOf(request.getServerPort()));
/*     */     }
/*     */     try {
/* 677 */       getNext().invoke(request, response);
/*     */     } finally { MimeHeaders headers;
/* 679 */       request.setRemoteAddr(originalRemoteAddr);
/* 680 */       request.setRemoteHost(originalRemoteHost);
/*     */       
/* 682 */       request.setSecure(originalSecure);
/*     */       
/* 684 */       MimeHeaders headers = request.getCoyoteRequest().getMimeHeaders();
/*     */       
/* 686 */       request.getCoyoteRequest().scheme().setString(originalScheme);
/*     */       
/* 688 */       request.setServerPort(originalServerPort);
/*     */       
/* 690 */       if ((originalProxiesHeader == null) || (originalProxiesHeader.length() == 0)) {
/* 691 */         headers.removeHeader(this.proxiesHeader);
/*     */       } else {
/* 693 */         headers.setValue(this.proxiesHeader).setString(originalProxiesHeader);
/*     */       }
/*     */       
/* 696 */       if ((originalRemoteIpHeader == null) || (originalRemoteIpHeader.length() == 0)) {
/* 697 */         headers.removeHeader(this.remoteIpHeader);
/*     */       } else {
/* 699 */         headers.setValue(this.remoteIpHeader).setString(originalRemoteIpHeader);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void setPorts(org.apache.catalina.connector.Request request, int defaultPort) {
/* 705 */     int port = defaultPort;
/* 706 */     if (this.portHeader != null) {
/* 707 */       String portHeaderValue = request.getHeader(this.portHeader);
/* 708 */       if (portHeaderValue != null) {
/*     */         try {
/* 710 */           port = Integer.parseInt(portHeaderValue);
/*     */         } catch (NumberFormatException nfe) {
/* 712 */           if (log.isDebugEnabled()) {
/* 713 */             log.debug(sm.getString("remoteIpValve.invalidPortHeader", new Object[] { portHeaderValue, this.portHeader }), nfe);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 720 */     request.setServerPort(port);
/* 721 */     if (this.changeLocalPort) {
/* 722 */       request.setLocalPort(port);
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
/*     */   public void setHttpServerPort(int httpServerPort)
/*     */   {
/* 736 */     this.httpServerPort = httpServerPort;
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
/*     */   public void setHttpsServerPort(int httpsServerPort)
/*     */   {
/* 749 */     this.httpsServerPort = httpsServerPort;
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
/*     */   public void setInternalProxies(String internalProxies)
/*     */   {
/* 762 */     if ((internalProxies == null) || (internalProxies.length() == 0)) {
/* 763 */       this.internalProxies = null;
/*     */     } else {
/* 765 */       this.internalProxies = Pattern.compile(internalProxies);
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
/*     */   public void setProtocolHeader(String protocolHeader)
/*     */   {
/* 780 */     this.protocolHeader = protocolHeader;
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
/*     */   public void setProtocolHeaderHttpsValue(String protocolHeaderHttpsValue)
/*     */   {
/* 793 */     this.protocolHeaderHttpsValue = protocolHeaderHttpsValue;
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
/*     */   public void setProxiesHeader(String proxiesHeader)
/*     */   {
/* 814 */     this.proxiesHeader = proxiesHeader;
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
/*     */   public void setRemoteIpHeader(String remoteIpHeader)
/*     */   {
/* 831 */     this.remoteIpHeader = remoteIpHeader;
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
/*     */   public void setRequestAttributesEnabled(boolean requestAttributesEnabled)
/*     */   {
/* 854 */     this.requestAttributesEnabled = requestAttributesEnabled;
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
/*     */   public void setTrustedProxies(String trustedProxies)
/*     */   {
/* 868 */     if ((trustedProxies == null) || (trustedProxies.length() == 0)) {
/* 869 */       this.trustedProxies = null;
/*     */     } else {
/* 871 */       this.trustedProxies = Pattern.compile(trustedProxies);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\valves\RemoteIpValve.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */