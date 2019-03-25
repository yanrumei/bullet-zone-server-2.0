/*      */ package org.apache.catalina.filters;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.text.DateFormat;
/*      */ import java.text.ParseException;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Date;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import javax.servlet.Filter;
/*      */ import javax.servlet.FilterChain;
/*      */ import javax.servlet.FilterConfig;
/*      */ import javax.servlet.ServletException;
/*      */ import javax.servlet.ServletRequest;
/*      */ import javax.servlet.ServletRequestWrapper;
/*      */ import javax.servlet.ServletResponse;
/*      */ import javax.servlet.http.HttpServletRequest;
/*      */ import javax.servlet.http.HttpServletRequestWrapper;
/*      */ import javax.servlet.http.HttpServletResponse;
/*      */ import org.apache.catalina.connector.RequestFacade;
/*      */ import org.apache.catalina.servlet4preview.http.PushBuilder;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class RemoteIpFilter
/*      */   implements Filter
/*      */ {
/*      */   public static class XForwardedRequest
/*      */     extends HttpServletRequestWrapper
/*      */   {
/*  445 */     static final ThreadLocal<SimpleDateFormat[]> threadLocalDateFormats = new ThreadLocal()
/*      */     {
/*      */       protected SimpleDateFormat[] initialValue() {
/*  448 */         return new SimpleDateFormat[] { new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US), new SimpleDateFormat("EEEEEE, dd-MMM-yy HH:mm:ss zzz", Locale.US), new SimpleDateFormat("EEE MMMM d HH:mm:ss yyyy", Locale.US) };
/*      */       }
/*      */     };
/*      */     
/*      */ 
/*      */     protected final Map<String, List<String>> headers;
/*      */     
/*      */ 
/*      */     protected int localPort;
/*      */     
/*      */ 
/*      */     protected String remoteAddr;
/*      */     
/*      */     protected String remoteHost;
/*      */     
/*      */     protected String scheme;
/*      */     
/*      */     protected boolean secure;
/*      */     
/*      */     protected int serverPort;
/*      */     
/*      */ 
/*      */     public XForwardedRequest(HttpServletRequest request)
/*      */     {
/*  472 */       super();
/*  473 */       this.localPort = request.getLocalPort();
/*  474 */       this.remoteAddr = request.getRemoteAddr();
/*  475 */       this.remoteHost = request.getRemoteHost();
/*  476 */       this.scheme = request.getScheme();
/*  477 */       this.secure = request.isSecure();
/*  478 */       this.serverPort = request.getServerPort();
/*      */       
/*  480 */       this.headers = new HashMap();
/*  481 */       for (Enumeration<String> headerNames = request.getHeaderNames(); headerNames.hasMoreElements();) {
/*  482 */         String header = (String)headerNames.nextElement();
/*  483 */         this.headers.put(header, Collections.list(request.getHeaders(header)));
/*      */       }
/*      */     }
/*      */     
/*      */     public long getDateHeader(String name)
/*      */     {
/*  489 */       String value = getHeader(name);
/*  490 */       if (value == null) {
/*  491 */         return -1L;
/*      */       }
/*  493 */       DateFormat[] dateFormats = (DateFormat[])threadLocalDateFormats.get();
/*  494 */       Date date = null;
/*  495 */       for (int i = 0; (i < dateFormats.length) && (date == null); i++) {
/*  496 */         DateFormat dateFormat = dateFormats[i];
/*      */         try {
/*  498 */           date = dateFormat.parse(value);
/*      */         }
/*      */         catch (ParseException localParseException) {}
/*      */       }
/*      */       
/*  503 */       if (date == null) {
/*  504 */         throw new IllegalArgumentException(value);
/*      */       }
/*  506 */       return date.getTime();
/*      */     }
/*      */     
/*      */     public String getHeader(String name)
/*      */     {
/*  511 */       Map.Entry<String, List<String>> header = getHeaderEntry(name);
/*  512 */       if ((header == null) || (header.getValue() == null) || (((List)header.getValue()).isEmpty())) {
/*  513 */         return null;
/*      */       }
/*  515 */       return (String)((List)header.getValue()).get(0);
/*      */     }
/*      */     
/*      */     protected Map.Entry<String, List<String>> getHeaderEntry(String name) {
/*  519 */       for (Map.Entry<String, List<String>> entry : this.headers.entrySet()) {
/*  520 */         if (((String)entry.getKey()).equalsIgnoreCase(name)) {
/*  521 */           return entry;
/*      */         }
/*      */       }
/*  524 */       return null;
/*      */     }
/*      */     
/*      */     public Enumeration<String> getHeaderNames()
/*      */     {
/*  529 */       return Collections.enumeration(this.headers.keySet());
/*      */     }
/*      */     
/*      */     public Enumeration<String> getHeaders(String name)
/*      */     {
/*  534 */       Map.Entry<String, List<String>> header = getHeaderEntry(name);
/*  535 */       if ((header == null) || (header.getValue() == null)) {
/*  536 */         return Collections.enumeration(Collections.emptyList());
/*      */       }
/*  538 */       return Collections.enumeration((Collection)header.getValue());
/*      */     }
/*      */     
/*      */     public int getIntHeader(String name)
/*      */     {
/*  543 */       String value = getHeader(name);
/*  544 */       if (value == null) {
/*  545 */         return -1;
/*      */       }
/*  547 */       return Integer.parseInt(value);
/*      */     }
/*      */     
/*      */     public int getLocalPort()
/*      */     {
/*  552 */       return this.localPort;
/*      */     }
/*      */     
/*      */     public String getRemoteAddr()
/*      */     {
/*  557 */       return this.remoteAddr;
/*      */     }
/*      */     
/*      */     public String getRemoteHost()
/*      */     {
/*  562 */       return this.remoteHost;
/*      */     }
/*      */     
/*      */     public String getScheme()
/*      */     {
/*  567 */       return this.scheme;
/*      */     }
/*      */     
/*      */     public int getServerPort()
/*      */     {
/*  572 */       return this.serverPort;
/*      */     }
/*      */     
/*      */     public boolean isSecure()
/*      */     {
/*  577 */       return this.secure;
/*      */     }
/*      */     
/*      */     public void removeHeader(String name) {
/*  581 */       Map.Entry<String, List<String>> header = getHeaderEntry(name);
/*  582 */       if (header != null) {
/*  583 */         this.headers.remove(header.getKey());
/*      */       }
/*      */     }
/*      */     
/*      */     public void setHeader(String name, String value) {
/*  588 */       List<String> values = Arrays.asList(new String[] { value });
/*  589 */       Map.Entry<String, List<String>> header = getHeaderEntry(name);
/*  590 */       if (header == null) {
/*  591 */         this.headers.put(name, values);
/*      */       } else {
/*  593 */         header.setValue(values);
/*      */       }
/*      */     }
/*      */     
/*      */     public void setLocalPort(int localPort)
/*      */     {
/*  599 */       this.localPort = localPort;
/*      */     }
/*      */     
/*      */     public void setRemoteAddr(String remoteAddr) {
/*  603 */       this.remoteAddr = remoteAddr;
/*      */     }
/*      */     
/*      */     public void setRemoteHost(String remoteHost) {
/*  607 */       this.remoteHost = remoteHost;
/*      */     }
/*      */     
/*      */     public void setScheme(String scheme) {
/*  611 */       this.scheme = scheme;
/*      */     }
/*      */     
/*      */     public void setSecure(boolean secure) {
/*  615 */       this.secure = secure;
/*      */     }
/*      */     
/*      */     public void setServerPort(int serverPort) {
/*  619 */       this.serverPort = serverPort;
/*      */     }
/*      */     
/*      */     public StringBuffer getRequestURL()
/*      */     {
/*  624 */       StringBuffer url = new StringBuffer();
/*  625 */       String scheme = getScheme();
/*  626 */       int port = getServerPort();
/*  627 */       if (port < 0) {
/*  628 */         port = 80;
/*      */       }
/*  630 */       url.append(scheme);
/*  631 */       url.append("://");
/*  632 */       url.append(getServerName());
/*  633 */       if (((scheme.equals("http")) && (port != 80)) || (
/*  634 */         (scheme.equals("https")) && (port != 443))) {
/*  635 */         url.append(':');
/*  636 */         url.append(port);
/*      */       }
/*  638 */       url.append(getRequestURI());
/*      */       
/*  640 */       return url;
/*      */     }
/*      */     
/*      */     public PushBuilder getPushBuilder() {
/*  644 */       ServletRequest current = getRequest();
/*  645 */       while ((current instanceof ServletRequestWrapper)) {
/*  646 */         current = ((ServletRequestWrapper)current).getRequest();
/*      */       }
/*  648 */       if ((current instanceof RequestFacade)) {
/*  649 */         return ((RequestFacade)current).newPushBuilder(this);
/*      */       }
/*  651 */       return null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  660 */   private static final Pattern commaSeparatedValuesPattern = Pattern.compile("\\s*,\\s*");
/*      */   
/*      */ 
/*      */   protected static final String HTTP_SERVER_PORT_PARAMETER = "httpServerPort";
/*      */   
/*      */ 
/*      */   protected static final String HTTPS_SERVER_PORT_PARAMETER = "httpsServerPort";
/*      */   
/*      */ 
/*      */   protected static final String INTERNAL_PROXIES_PARAMETER = "internalProxies";
/*      */   
/*  671 */   private static final Log log = LogFactory.getLog(RemoteIpFilter.class);
/*      */   
/*      */ 
/*      */   protected static final String PROTOCOL_HEADER_PARAMETER = "protocolHeader";
/*      */   
/*      */ 
/*      */   protected static final String PROTOCOL_HEADER_HTTPS_VALUE_PARAMETER = "protocolHeaderHttpsValue";
/*      */   
/*      */ 
/*      */   protected static final String PORT_HEADER_PARAMETER = "portHeader";
/*      */   
/*      */ 
/*      */   protected static final String CHANGE_LOCAL_PORT_PARAMETER = "changeLocalPort";
/*      */   
/*      */   protected static final String PROXIES_HEADER_PARAMETER = "proxiesHeader";
/*      */   
/*      */   protected static final String REMOTE_IP_HEADER_PARAMETER = "remoteIpHeader";
/*      */   
/*      */   protected static final String TRUSTED_PROXIES_PARAMETER = "trustedProxies";
/*      */   
/*      */ 
/*      */   protected static String[] commaDelimitedListToStringArray(String commaDelimitedStrings)
/*      */   {
/*  694 */     return (commaDelimitedStrings == null) || (commaDelimitedStrings.length() == 0) ? new String[0] : commaSeparatedValuesPattern
/*  695 */       .split(commaDelimitedStrings);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static String listToCommaDelimitedString(List<String> stringList)
/*      */   {
/*  705 */     if (stringList == null) {
/*  706 */       return "";
/*      */     }
/*  708 */     StringBuilder result = new StringBuilder();
/*  709 */     for (Iterator<String> it = stringList.iterator(); it.hasNext();) {
/*  710 */       Object element = it.next();
/*  711 */       if (element != null) {
/*  712 */         result.append(element);
/*  713 */         if (it.hasNext()) {
/*  714 */           result.append(", ");
/*      */         }
/*      */       }
/*      */     }
/*  718 */     return result.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  724 */   private int httpServerPort = 80;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  729 */   private int httpsServerPort = 443;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  734 */   private Pattern internalProxies = Pattern.compile("10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}|192\\.168\\.\\d{1,3}\\.\\d{1,3}|169\\.254\\.\\d{1,3}\\.\\d{1,3}|127\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}|172\\.1[6-9]{1}\\.\\d{1,3}\\.\\d{1,3}|172\\.2[0-9]{1}\\.\\d{1,3}\\.\\d{1,3}|172\\.3[0-1]{1}\\.\\d{1,3}\\.\\d{1,3}");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  746 */   private String protocolHeader = null;
/*      */   
/*  748 */   private String protocolHeaderHttpsValue = "https";
/*      */   
/*  750 */   private String portHeader = null;
/*      */   
/*  752 */   private boolean changeLocalPort = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  757 */   private String proxiesHeader = "X-Forwarded-By";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  762 */   private String remoteIpHeader = "X-Forwarded-For";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  767 */   private boolean requestAttributesEnabled = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  772 */   private Pattern trustedProxies = null;
/*      */   
/*      */ 
/*      */   public void destroy() {}
/*      */   
/*      */ 
/*      */   public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
/*      */     throws IOException, ServletException
/*      */   {
/*  781 */     if ((this.internalProxies != null) && 
/*  782 */       (this.internalProxies.matcher(request.getRemoteAddr()).matches())) {
/*  783 */       String remoteIp = null;
/*      */       
/*  785 */       LinkedList<String> proxiesHeaderValue = new LinkedList();
/*  786 */       StringBuilder concatRemoteIpHeaderValue = new StringBuilder();
/*      */       
/*  788 */       for (Enumeration<String> e = request.getHeaders(this.remoteIpHeader); e.hasMoreElements();) {
/*  789 */         if (concatRemoteIpHeaderValue.length() > 0) {
/*  790 */           concatRemoteIpHeaderValue.append(", ");
/*      */         }
/*      */         
/*  793 */         concatRemoteIpHeaderValue.append((String)e.nextElement());
/*      */       }
/*      */       
/*  796 */       String[] remoteIpHeaderValue = commaDelimitedListToStringArray(concatRemoteIpHeaderValue.toString());
/*      */       
/*      */ 
/*  799 */       for (int idx = remoteIpHeaderValue.length - 1; idx >= 0; idx--) {
/*  800 */         String currentRemoteIp = remoteIpHeaderValue[idx];
/*  801 */         remoteIp = currentRemoteIp;
/*  802 */         if (!this.internalProxies.matcher(currentRemoteIp).matches())
/*      */         {
/*  804 */           if ((this.trustedProxies != null) && 
/*  805 */             (this.trustedProxies.matcher(currentRemoteIp).matches())) {
/*  806 */             proxiesHeaderValue.addFirst(currentRemoteIp);
/*      */           } else {
/*  808 */             idx--;
/*  809 */             break;
/*      */           }
/*      */         }
/*      */       }
/*  813 */       LinkedList<String> newRemoteIpHeaderValue = new LinkedList();
/*  814 */       for (; idx >= 0; idx--) {
/*  815 */         String currentRemoteIp = remoteIpHeaderValue[idx];
/*  816 */         newRemoteIpHeaderValue.addFirst(currentRemoteIp);
/*      */       }
/*      */       
/*  819 */       XForwardedRequest xRequest = new XForwardedRequest(request);
/*  820 */       if (remoteIp != null)
/*      */       {
/*  822 */         xRequest.setRemoteAddr(remoteIp);
/*  823 */         xRequest.setRemoteHost(remoteIp);
/*      */         
/*  825 */         if (proxiesHeaderValue.size() == 0) {
/*  826 */           xRequest.removeHeader(this.proxiesHeader);
/*      */         } else {
/*  828 */           String commaDelimitedListOfProxies = listToCommaDelimitedString(proxiesHeaderValue);
/*  829 */           xRequest.setHeader(this.proxiesHeader, commaDelimitedListOfProxies);
/*      */         }
/*  831 */         if (newRemoteIpHeaderValue.size() == 0) {
/*  832 */           xRequest.removeHeader(this.remoteIpHeader);
/*      */         } else {
/*  834 */           String commaDelimitedRemoteIpHeaderValue = listToCommaDelimitedString(newRemoteIpHeaderValue);
/*  835 */           xRequest.setHeader(this.remoteIpHeader, commaDelimitedRemoteIpHeaderValue);
/*      */         }
/*      */       }
/*      */       
/*  839 */       if (this.protocolHeader != null) {
/*  840 */         String protocolHeaderValue = request.getHeader(this.protocolHeader);
/*  841 */         if (protocolHeaderValue != null)
/*      */         {
/*  843 */           if (this.protocolHeaderHttpsValue.equalsIgnoreCase(protocolHeaderValue)) {
/*  844 */             xRequest.setSecure(true);
/*  845 */             xRequest.setScheme("https");
/*  846 */             setPorts(xRequest, this.httpsServerPort);
/*      */           } else {
/*  848 */             xRequest.setSecure(false);
/*  849 */             xRequest.setScheme("http");
/*  850 */             setPorts(xRequest, this.httpServerPort);
/*      */           }
/*      */         }
/*      */       }
/*  854 */       if (log.isDebugEnabled()) {
/*  855 */         log.debug("Incoming request " + request.getRequestURI() + " with originalRemoteAddr '" + request.getRemoteAddr() + "', originalRemoteHost='" + request
/*  856 */           .getRemoteHost() + "', originalSecure='" + request.isSecure() + "', originalScheme='" + request
/*  857 */           .getScheme() + "', original[" + this.remoteIpHeader + "]='" + concatRemoteIpHeaderValue + "', original[" + this.protocolHeader + "]='" + (this.protocolHeader == null ? null : request
/*      */           
/*  859 */           .getHeader(this.protocolHeader)) + "' will be seen as newRemoteAddr='" + xRequest
/*  860 */           .getRemoteAddr() + "', newRemoteHost='" + xRequest.getRemoteHost() + "', newScheme='" + xRequest
/*  861 */           .getScheme() + "', newSecure='" + xRequest.isSecure() + "', new[" + this.remoteIpHeader + "]='" + xRequest
/*  862 */           .getHeader(this.remoteIpHeader) + "', new[" + this.proxiesHeader + "]='" + xRequest.getHeader(this.proxiesHeader) + "'");
/*      */       }
/*  864 */       if (this.requestAttributesEnabled) {
/*  865 */         request.setAttribute("org.apache.catalina.AccessLog.RemoteAddr", xRequest
/*  866 */           .getRemoteAddr());
/*  867 */         request.setAttribute("org.apache.tomcat.remoteAddr", xRequest
/*  868 */           .getRemoteAddr());
/*  869 */         request.setAttribute("org.apache.catalina.AccessLog.RemoteHost", xRequest
/*  870 */           .getRemoteHost());
/*  871 */         request.setAttribute("org.apache.catalina.AccessLog.Protocol", xRequest
/*  872 */           .getProtocol());
/*  873 */         request.setAttribute("org.apache.catalina.AccessLog.ServerPort", 
/*  874 */           Integer.valueOf(xRequest.getServerPort()));
/*      */       }
/*  876 */       chain.doFilter(xRequest, response);
/*      */     } else {
/*  878 */       if (log.isDebugEnabled()) {
/*  879 */         log.debug("Skip RemoteIpFilter for request " + request.getRequestURI() + " with originalRemoteAddr '" + request
/*  880 */           .getRemoteAddr() + "'");
/*      */       }
/*  882 */       chain.doFilter(request, response);
/*      */     }
/*      */   }
/*      */   
/*      */   private void setPorts(XForwardedRequest xrequest, int defaultPort)
/*      */   {
/*  888 */     int port = defaultPort;
/*  889 */     if (getPortHeader() != null) {
/*  890 */       String portHeaderValue = xrequest.getHeader(getPortHeader());
/*  891 */       if (portHeaderValue != null) {
/*      */         try {
/*  893 */           port = Integer.parseInt(portHeaderValue);
/*      */         } catch (NumberFormatException nfe) {
/*  895 */           log.debug("Invalid port value [" + portHeaderValue + "] provided in header [" + 
/*  896 */             getPortHeader() + "]");
/*      */         }
/*      */       }
/*      */     }
/*  900 */     xrequest.setServerPort(port);
/*  901 */     if (isChangeLocalPort()) {
/*  902 */       xrequest.setLocalPort(port);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
/*      */     throws IOException, ServletException
/*      */   {
/*  912 */     if (((request instanceof HttpServletRequest)) && ((response instanceof HttpServletResponse))) {
/*  913 */       doFilter((HttpServletRequest)request, (HttpServletResponse)response, chain);
/*      */     } else {
/*  915 */       chain.doFilter(request, response);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isChangeLocalPort() {
/*  920 */     return this.changeLocalPort;
/*      */   }
/*      */   
/*      */   public int getHttpsServerPort() {
/*  924 */     return this.httpsServerPort;
/*      */   }
/*      */   
/*      */   public Pattern getInternalProxies() {
/*  928 */     return this.internalProxies;
/*      */   }
/*      */   
/*      */   public String getProtocolHeader() {
/*  932 */     return this.protocolHeader;
/*      */   }
/*      */   
/*      */   public String getPortHeader() {
/*  936 */     return this.portHeader;
/*      */   }
/*      */   
/*      */   public String getProtocolHeaderHttpsValue() {
/*  940 */     return this.protocolHeaderHttpsValue;
/*      */   }
/*      */   
/*      */   public String getProxiesHeader() {
/*  944 */     return this.proxiesHeader;
/*      */   }
/*      */   
/*      */   public String getRemoteIpHeader() {
/*  948 */     return this.remoteIpHeader;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getRequestAttributesEnabled()
/*      */   {
/*  957 */     return this.requestAttributesEnabled;
/*      */   }
/*      */   
/*      */   public Pattern getTrustedProxies() {
/*  961 */     return this.trustedProxies;
/*      */   }
/*      */   
/*      */   public void init(FilterConfig filterConfig) throws ServletException
/*      */   {
/*  966 */     if (filterConfig.getInitParameter("internalProxies") != null) {
/*  967 */       setInternalProxies(filterConfig.getInitParameter("internalProxies"));
/*      */     }
/*      */     
/*  970 */     if (filterConfig.getInitParameter("protocolHeader") != null) {
/*  971 */       setProtocolHeader(filterConfig.getInitParameter("protocolHeader"));
/*      */     }
/*      */     
/*  974 */     if (filterConfig.getInitParameter("protocolHeaderHttpsValue") != null) {
/*  975 */       setProtocolHeaderHttpsValue(filterConfig.getInitParameter("protocolHeaderHttpsValue"));
/*      */     }
/*      */     
/*  978 */     if (filterConfig.getInitParameter("portHeader") != null) {
/*  979 */       setPortHeader(filterConfig.getInitParameter("portHeader"));
/*      */     }
/*      */     
/*  982 */     if (filterConfig.getInitParameter("changeLocalPort") != null) {
/*  983 */       setChangeLocalPort(Boolean.parseBoolean(filterConfig.getInitParameter("changeLocalPort")));
/*      */     }
/*      */     
/*  986 */     if (filterConfig.getInitParameter("proxiesHeader") != null) {
/*  987 */       setProxiesHeader(filterConfig.getInitParameter("proxiesHeader"));
/*      */     }
/*      */     
/*  990 */     if (filterConfig.getInitParameter("remoteIpHeader") != null) {
/*  991 */       setRemoteIpHeader(filterConfig.getInitParameter("remoteIpHeader"));
/*      */     }
/*      */     
/*  994 */     if (filterConfig.getInitParameter("trustedProxies") != null) {
/*  995 */       setTrustedProxies(filterConfig.getInitParameter("trustedProxies"));
/*      */     }
/*      */     
/*  998 */     if (filterConfig.getInitParameter("httpServerPort") != null) {
/*      */       try {
/* 1000 */         setHttpServerPort(Integer.parseInt(filterConfig.getInitParameter("httpServerPort")));
/*      */       } catch (NumberFormatException e) {
/* 1002 */         throw new NumberFormatException("Illegal httpServerPort : " + e.getMessage());
/*      */       }
/*      */     }
/*      */     
/* 1006 */     if (filterConfig.getInitParameter("httpsServerPort") != null) {
/*      */       try {
/* 1008 */         setHttpsServerPort(Integer.parseInt(filterConfig.getInitParameter("httpsServerPort")));
/*      */       } catch (NumberFormatException e) {
/* 1010 */         throw new NumberFormatException("Illegal httpsServerPort : " + e.getMessage());
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
/*      */   public void setChangeLocalPort(boolean changeLocalPort)
/*      */   {
/* 1028 */     this.changeLocalPort = changeLocalPort;
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
/*      */   public void setHttpServerPort(int httpServerPort)
/*      */   {
/* 1042 */     this.httpServerPort = httpServerPort;
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
/*      */   public void setHttpsServerPort(int httpsServerPort)
/*      */   {
/* 1055 */     this.httpsServerPort = httpsServerPort;
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
/*      */   public void setInternalProxies(String internalProxies)
/*      */   {
/* 1068 */     if ((internalProxies == null) || (internalProxies.length() == 0)) {
/* 1069 */       this.internalProxies = null;
/*      */     } else {
/* 1071 */       this.internalProxies = Pattern.compile(internalProxies);
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
/*      */   public void setPortHeader(String portHeader)
/*      */   {
/* 1087 */     this.portHeader = portHeader;
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
/*      */   public void setProtocolHeader(String protocolHeader)
/*      */   {
/* 1101 */     this.protocolHeader = protocolHeader;
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
/*      */   public void setProtocolHeaderHttpsValue(String protocolHeaderHttpsValue)
/*      */   {
/* 1114 */     this.protocolHeaderHttpsValue = protocolHeaderHttpsValue;
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
/*      */   public void setProxiesHeader(String proxiesHeader)
/*      */   {
/* 1135 */     this.proxiesHeader = proxiesHeader;
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
/*      */   public void setRemoteIpHeader(String remoteIpHeader)
/*      */   {
/* 1151 */     this.remoteIpHeader = remoteIpHeader;
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
/*      */   public void setRequestAttributesEnabled(boolean requestAttributesEnabled)
/*      */   {
/* 1174 */     this.requestAttributesEnabled = requestAttributesEnabled;
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
/*      */   public void setTrustedProxies(String trustedProxies)
/*      */   {
/* 1188 */     if ((trustedProxies == null) || (trustedProxies.length() == 0)) {
/* 1189 */       this.trustedProxies = null;
/*      */     } else {
/* 1191 */       this.trustedProxies = Pattern.compile(trustedProxies);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\filters\RemoteIpFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */