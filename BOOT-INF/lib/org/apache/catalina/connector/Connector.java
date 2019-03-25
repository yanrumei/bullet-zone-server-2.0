/*      */ package org.apache.catalina.connector;
/*      */ 
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.net.InetAddress;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Locale;
/*      */ import javax.management.ObjectName;
/*      */ import org.apache.catalina.Executor;
/*      */ import org.apache.catalina.Globals;
/*      */ import org.apache.catalina.LifecycleException;
/*      */ import org.apache.catalina.LifecycleState;
/*      */ import org.apache.catalina.Service;
/*      */ import org.apache.catalina.core.AprLifecycleListener;
/*      */ import org.apache.catalina.util.LifecycleMBeanBase;
/*      */ import org.apache.coyote.Adapter;
/*      */ import org.apache.coyote.ProtocolHandler;
/*      */ import org.apache.coyote.UpgradeProtocol;
/*      */ import org.apache.coyote.http11.AbstractHttp11JsseProtocol;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.tomcat.util.IntrospectionUtils;
/*      */ import org.apache.tomcat.util.buf.B2CConverter;
/*      */ import org.apache.tomcat.util.net.SSLHostConfig;
/*      */ import org.apache.tomcat.util.net.openssl.OpenSSLImplementation;
/*      */ import org.apache.tomcat.util.res.StringManager;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Connector
/*      */   extends LifecycleMBeanBase
/*      */ {
/*   57 */   private static final Log log = LogFactory.getLog(Connector.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   64 */   public static final boolean RECYCLE_FACADES = Boolean.parseBoolean(System.getProperty("org.apache.catalina.connector.RECYCLE_FACADES", "false"));
/*      */   
/*      */ 
/*      */ 
/*      */   public Connector()
/*      */   {
/*   70 */     this(null);
/*      */   }
/*      */   
/*      */   public Connector(String protocol) {
/*   74 */     setProtocol(protocol);
/*      */     
/*   76 */     ProtocolHandler p = null;
/*      */     try {
/*   78 */       Class<?> clazz = Class.forName(this.protocolHandlerClassName);
/*   79 */       p = (ProtocolHandler)clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/*      */     } catch (Exception e) {
/*   81 */       log.error(sm.getString("coyoteConnector.protocolHandlerInstantiationFailed"), e);
/*      */     }
/*      */     finally {
/*   84 */       this.protocolHandler = p;
/*      */     }
/*      */     
/*   87 */     if (Globals.STRICT_SERVLET_COMPLIANCE) {
/*   88 */       this.uriCharset = StandardCharsets.ISO_8859_1;
/*      */     } else {
/*   90 */       this.uriCharset = StandardCharsets.UTF_8;
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
/*  101 */   protected Service service = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  107 */   protected boolean allowTrace = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  113 */   protected long asyncTimeout = 30000L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  119 */   protected boolean enableLookups = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  125 */   protected boolean xpoweredBy = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  131 */   protected int port = -1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  140 */   protected String proxyName = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  149 */   protected int proxyPort = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  155 */   protected int redirectPort = 443;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  162 */   protected String scheme = "http";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  169 */   protected boolean secure = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  175 */   protected static final StringManager sm = StringManager.getManager(Connector.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  182 */   private int maxCookieCount = 200;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  189 */   protected int maxParameterCount = 10000;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  195 */   protected int maxPostSize = 2097152;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  202 */   protected int maxSavePostSize = 4096;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  208 */   protected String parseBodyMethods = "POST";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected HashSet<String> parseBodyMethodsSet;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  219 */   protected boolean useIPVHosts = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  226 */   protected String protocolHandlerClassName = "org.apache.coyote.http11.Http11NioProtocol";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final ProtocolHandler protocolHandler;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  239 */   protected Adapter adapter = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*  247 */   protected String URIEncoding = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*  254 */   protected String URIEncodingLower = null;
/*      */   
/*      */ 
/*      */ 
/*  258 */   private Charset uriCharset = StandardCharsets.UTF_8;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  264 */   protected boolean useBodyEncodingForURI = false;
/*      */   
/*      */ 
/*  267 */   protected static final HashMap<String, String> replacements = new HashMap();
/*      */   
/*  269 */   static { replacements.put("acceptCount", "backlog");
/*  270 */     replacements.put("connectionLinger", "soLinger");
/*  271 */     replacements.put("connectionTimeout", "soTimeout");
/*  272 */     replacements.put("rootFile", "rootfile");
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
/*      */   public Object getProperty(String name)
/*      */   {
/*  285 */     String repl = name;
/*  286 */     if (replacements.get(name) != null) {
/*  287 */       repl = (String)replacements.get(name);
/*      */     }
/*  289 */     return IntrospectionUtils.getProperty(this.protocolHandler, repl);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean setProperty(String name, String value)
/*      */   {
/*  301 */     String repl = name;
/*  302 */     if (replacements.get(name) != null) {
/*  303 */       repl = (String)replacements.get(name);
/*      */     }
/*  305 */     return IntrospectionUtils.setProperty(this.protocolHandler, repl, value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object getAttribute(String name)
/*      */   {
/*  316 */     return getProperty(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAttribute(String name, Object value)
/*      */   {
/*  327 */     setProperty(name, String.valueOf(value));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Service getService()
/*      */   {
/*  335 */     return this.service;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setService(Service service)
/*      */   {
/*  345 */     this.service = service;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getAllowTrace()
/*      */   {
/*  354 */     return this.allowTrace;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAllowTrace(boolean allowTrace)
/*      */   {
/*  364 */     this.allowTrace = allowTrace;
/*  365 */     setProperty("allowTrace", String.valueOf(allowTrace));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getAsyncTimeout()
/*      */   {
/*  373 */     return this.asyncTimeout;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAsyncTimeout(long asyncTimeout)
/*      */   {
/*  383 */     this.asyncTimeout = asyncTimeout;
/*  384 */     setProperty("asyncTimeout", String.valueOf(asyncTimeout));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getEnableLookups()
/*      */   {
/*  392 */     return this.enableLookups;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setEnableLookups(boolean enableLookups)
/*      */   {
/*  402 */     this.enableLookups = enableLookups;
/*  403 */     setProperty("enableLookups", String.valueOf(enableLookups));
/*      */   }
/*      */   
/*      */   public int getMaxCookieCount()
/*      */   {
/*  408 */     return this.maxCookieCount;
/*      */   }
/*      */   
/*      */   public void setMaxCookieCount(int maxCookieCount)
/*      */   {
/*  413 */     this.maxCookieCount = maxCookieCount;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxParameterCount()
/*      */   {
/*  423 */     return this.maxParameterCount;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMaxParameterCount(int maxParameterCount)
/*      */   {
/*  435 */     this.maxParameterCount = maxParameterCount;
/*  436 */     setProperty("maxParameterCount", String.valueOf(maxParameterCount));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxPostSize()
/*      */   {
/*  445 */     return this.maxPostSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMaxPostSize(int maxPostSize)
/*      */   {
/*  457 */     this.maxPostSize = maxPostSize;
/*  458 */     setProperty("maxPostSize", String.valueOf(maxPostSize));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxSavePostSize()
/*      */   {
/*  467 */     return this.maxSavePostSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMaxSavePostSize(int maxSavePostSize)
/*      */   {
/*  479 */     this.maxSavePostSize = maxSavePostSize;
/*  480 */     setProperty("maxSavePostSize", String.valueOf(maxSavePostSize));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getParseBodyMethods()
/*      */   {
/*  488 */     return this.parseBodyMethods;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setParseBodyMethods(String methods)
/*      */   {
/*  500 */     HashSet<String> methodSet = new HashSet();
/*      */     
/*  502 */     if (null != methods) {
/*  503 */       methodSet.addAll(Arrays.asList(methods.split("\\s*,\\s*")));
/*      */     }
/*      */     
/*  506 */     if (methodSet.contains("TRACE")) {
/*  507 */       throw new IllegalArgumentException(sm.getString("coyoteConnector.parseBodyMethodNoTrace"));
/*      */     }
/*      */     
/*  510 */     this.parseBodyMethods = methods;
/*  511 */     this.parseBodyMethodsSet = methodSet;
/*  512 */     setProperty("parseBodyMethods", methods);
/*      */   }
/*      */   
/*      */   protected boolean isParseBodyMethod(String method)
/*      */   {
/*  517 */     return this.parseBodyMethodsSet.contains(method);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getPort()
/*      */   {
/*  527 */     return this.port;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPort(int port)
/*      */   {
/*  537 */     this.port = port;
/*  538 */     setProperty("port", String.valueOf(port));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getLocalPort()
/*      */   {
/*  548 */     return ((Integer)getProperty("localPort")).intValue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getProtocol()
/*      */   {
/*  556 */     if ((("org.apache.coyote.http11.Http11NioProtocol".equals(getProtocolHandlerClassName())) && (
/*  557 */       (!AprLifecycleListener.isAprAvailable()) || (!AprLifecycleListener.getUseAprConnector()))) || (
/*  558 */       ("org.apache.coyote.http11.Http11AprProtocol".equals(getProtocolHandlerClassName())) && 
/*  559 */       (AprLifecycleListener.getUseAprConnector())))
/*  560 */       return "HTTP/1.1";
/*  561 */     if ((("org.apache.coyote.ajp.AjpNioProtocol".equals(getProtocolHandlerClassName())) && (
/*  562 */       (!AprLifecycleListener.isAprAvailable()) || (!AprLifecycleListener.getUseAprConnector()))) || (
/*  563 */       ("org.apache.coyote.ajp.AjpAprProtocol".equals(getProtocolHandlerClassName())) && 
/*  564 */       (AprLifecycleListener.getUseAprConnector()))) {
/*  565 */       return "AJP/1.3";
/*      */     }
/*  567 */     return getProtocolHandlerClassName();
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
/*      */   @Deprecated
/*      */   public void setProtocol(String protocol)
/*      */   {
/*  583 */     boolean aprConnector = (AprLifecycleListener.isAprAvailable()) && (AprLifecycleListener.getUseAprConnector());
/*      */     
/*  585 */     if (("HTTP/1.1".equals(protocol)) || (protocol == null)) {
/*  586 */       if (aprConnector) {
/*  587 */         setProtocolHandlerClassName("org.apache.coyote.http11.Http11AprProtocol");
/*      */       } else {
/*  589 */         setProtocolHandlerClassName("org.apache.coyote.http11.Http11NioProtocol");
/*      */       }
/*  591 */     } else if ("AJP/1.3".equals(protocol)) {
/*  592 */       if (aprConnector) {
/*  593 */         setProtocolHandlerClassName("org.apache.coyote.ajp.AjpAprProtocol");
/*      */       } else {
/*  595 */         setProtocolHandlerClassName("org.apache.coyote.ajp.AjpNioProtocol");
/*      */       }
/*      */     } else {
/*  598 */       setProtocolHandlerClassName(protocol);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getProtocolHandlerClassName()
/*      */   {
/*  607 */     return this.protocolHandlerClassName;
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
/*      */   @Deprecated
/*      */   public void setProtocolHandlerClassName(String protocolHandlerClassName)
/*      */   {
/*  622 */     this.protocolHandlerClassName = protocolHandlerClassName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ProtocolHandler getProtocolHandler()
/*      */   {
/*  630 */     return this.protocolHandler;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getProxyName()
/*      */   {
/*  638 */     return this.proxyName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setProxyName(String proxyName)
/*      */   {
/*  649 */     if ((proxyName != null) && (proxyName.length() > 0)) {
/*  650 */       this.proxyName = proxyName;
/*      */     } else {
/*  652 */       this.proxyName = null;
/*      */     }
/*  654 */     setProperty("proxyName", this.proxyName);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getProxyPort()
/*      */   {
/*  662 */     return this.proxyPort;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setProxyPort(int proxyPort)
/*      */   {
/*  672 */     this.proxyPort = proxyPort;
/*  673 */     setProperty("proxyPort", String.valueOf(proxyPort));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getRedirectPort()
/*      */   {
/*  683 */     return this.redirectPort;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRedirectPort(int redirectPort)
/*      */   {
/*  693 */     this.redirectPort = redirectPort;
/*  694 */     setProperty("redirectPort", String.valueOf(redirectPort));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getScheme()
/*      */   {
/*  703 */     return this.scheme;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setScheme(String scheme)
/*      */   {
/*  714 */     this.scheme = scheme;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getSecure()
/*      */   {
/*  723 */     return this.secure;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSecure(boolean secure)
/*      */   {
/*  734 */     this.secure = secure;
/*  735 */     setProperty("secure", Boolean.toString(secure));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getURIEncoding()
/*      */   {
/*  744 */     return this.uriCharset.name();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public String getURIEncodingLower()
/*      */   {
/*  755 */     return this.uriCharset.name().toLowerCase(Locale.ENGLISH);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Charset getURICharset()
/*      */   {
/*  765 */     return this.uriCharset;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setURIEncoding(String URIEncoding)
/*      */   {
/*      */     try
/*      */     {
/*  775 */       this.uriCharset = B2CConverter.getCharset(URIEncoding);
/*      */     } catch (UnsupportedEncodingException e) {
/*  777 */       log.warn(sm.getString("coyoteConnector.invalidEncoding", new Object[] { URIEncoding, this.uriCharset
/*  778 */         .name() }), e);
/*      */     }
/*  780 */     setProperty("uRIEncoding", URIEncoding);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getUseBodyEncodingForURI()
/*      */   {
/*  788 */     return this.useBodyEncodingForURI;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseBodyEncodingForURI(boolean useBodyEncodingForURI)
/*      */   {
/*  798 */     this.useBodyEncodingForURI = useBodyEncodingForURI;
/*  799 */     setProperty("useBodyEncodingForURI", String.valueOf(useBodyEncodingForURI));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getXpoweredBy()
/*      */   {
/*  810 */     return this.xpoweredBy;
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
/*      */   public void setXpoweredBy(boolean xpoweredBy)
/*      */   {
/*  823 */     this.xpoweredBy = xpoweredBy;
/*  824 */     setProperty("xpoweredBy", String.valueOf(xpoweredBy));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseIPVHosts(boolean useIPVHosts)
/*      */   {
/*  835 */     this.useIPVHosts = useIPVHosts;
/*  836 */     setProperty("useIPVHosts", String.valueOf(useIPVHosts));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getUseIPVHosts()
/*      */   {
/*  846 */     return this.useIPVHosts;
/*      */   }
/*      */   
/*      */   public String getExecutorName()
/*      */   {
/*  851 */     Object obj = this.protocolHandler.getExecutor();
/*  852 */     if ((obj instanceof Executor)) {
/*  853 */       return ((Executor)obj).getName();
/*      */     }
/*  855 */     return "Internal";
/*      */   }
/*      */   
/*      */   public void addSslHostConfig(SSLHostConfig sslHostConfig)
/*      */   {
/*  860 */     this.protocolHandler.addSslHostConfig(sslHostConfig);
/*      */   }
/*      */   
/*      */   public SSLHostConfig[] findSslHostConfigs()
/*      */   {
/*  865 */     return this.protocolHandler.findSslHostConfigs();
/*      */   }
/*      */   
/*      */   public void addUpgradeProtocol(UpgradeProtocol upgradeProtocol)
/*      */   {
/*  870 */     this.protocolHandler.addUpgradeProtocol(upgradeProtocol);
/*      */   }
/*      */   
/*      */   public UpgradeProtocol[] findUpgradeProtocols()
/*      */   {
/*  875 */     return this.protocolHandler.findUpgradeProtocols();
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
/*      */   public Request createRequest()
/*      */   {
/*  889 */     Request request = new Request();
/*  890 */     request.setConnector(this);
/*  891 */     return request;
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
/*      */   public Response createResponse()
/*      */   {
/*  904 */     Response response = new Response();
/*  905 */     response.setConnector(this);
/*  906 */     return response;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected String createObjectNameKeyProperties(String type)
/*      */   {
/*  913 */     Object addressObj = getProperty("address");
/*      */     
/*  915 */     StringBuilder sb = new StringBuilder("type=");
/*  916 */     sb.append(type);
/*  917 */     sb.append(",port=");
/*  918 */     int port = getPort();
/*  919 */     if (port > 0) {
/*  920 */       sb.append(port);
/*      */     } else {
/*  922 */       sb.append("auto-");
/*  923 */       sb.append(getProperty("nameIndex"));
/*      */     }
/*  925 */     String address = "";
/*  926 */     if ((addressObj instanceof InetAddress)) {
/*  927 */       address = ((InetAddress)addressObj).getHostAddress();
/*  928 */     } else if (addressObj != null) {
/*  929 */       address = addressObj.toString();
/*      */     }
/*  931 */     if (address.length() > 0) {
/*  932 */       sb.append(",address=");
/*  933 */       sb.append(ObjectName.quote(address));
/*      */     }
/*  935 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void pause()
/*      */   {
/*      */     try
/*      */     {
/*  944 */       this.protocolHandler.pause();
/*      */     } catch (Exception e) {
/*  946 */       log.error(sm.getString("coyoteConnector.protocolHandlerPauseFailed"), e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void resume()
/*      */   {
/*      */     try
/*      */     {
/*  956 */       this.protocolHandler.resume();
/*      */     } catch (Exception e) {
/*  958 */       log.error(sm.getString("coyoteConnector.protocolHandlerResumeFailed"), e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected void initInternal()
/*      */     throws LifecycleException
/*      */   {
/*  966 */     super.initInternal();
/*      */     
/*      */ 
/*  969 */     this.adapter = new CoyoteAdapter(this);
/*  970 */     this.protocolHandler.setAdapter(this.adapter);
/*      */     
/*      */ 
/*  973 */     if (null == this.parseBodyMethodsSet) {
/*  974 */       setParseBodyMethods(getParseBodyMethods());
/*      */     }
/*      */     
/*  977 */     if ((this.protocolHandler.isAprRequired()) && (!AprLifecycleListener.isAprAvailable())) {
/*  978 */       throw new LifecycleException(sm.getString("coyoteConnector.protocolHandlerNoApr", new Object[] {
/*  979 */         getProtocolHandlerClassName() }));
/*      */     }
/*  981 */     if ((AprLifecycleListener.isAprAvailable()) && (AprLifecycleListener.getUseOpenSSL()) && ((this.protocolHandler instanceof AbstractHttp11JsseProtocol)))
/*      */     {
/*  983 */       AbstractHttp11JsseProtocol<?> jsseProtocolHandler = (AbstractHttp11JsseProtocol)this.protocolHandler;
/*      */       
/*  985 */       if ((jsseProtocolHandler.isSSLEnabled()) && 
/*  986 */         (jsseProtocolHandler.getSslImplementationName() == null))
/*      */       {
/*  988 */         jsseProtocolHandler.setSslImplementationName(OpenSSLImplementation.class.getName());
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/*  993 */       this.protocolHandler.init();
/*      */     }
/*      */     catch (Exception e) {
/*  996 */       throw new LifecycleException(sm.getString("coyoteConnector.protocolHandlerInitializationFailed"), e);
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
/*      */   protected void startInternal()
/*      */     throws LifecycleException
/*      */   {
/* 1010 */     if (getPort() < 0) {
/* 1011 */       throw new LifecycleException(sm.getString("coyoteConnector.invalidPort", new Object[] {
/* 1012 */         Integer.valueOf(getPort()) }));
/*      */     }
/*      */     
/* 1015 */     setState(LifecycleState.STARTING);
/*      */     try
/*      */     {
/* 1018 */       this.protocolHandler.start();
/*      */     }
/*      */     catch (Exception e) {
/* 1021 */       throw new LifecycleException(sm.getString("coyoteConnector.protocolHandlerStartFailed"), e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void stopInternal()
/*      */     throws LifecycleException
/*      */   {
/* 1034 */     setState(LifecycleState.STOPPING);
/*      */     try
/*      */     {
/* 1037 */       this.protocolHandler.stop();
/*      */     }
/*      */     catch (Exception e) {
/* 1040 */       throw new LifecycleException(sm.getString("coyoteConnector.protocolHandlerStopFailed"), e);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void destroyInternal() throws LifecycleException
/*      */   {
/*      */     try
/*      */     {
/* 1048 */       this.protocolHandler.destroy();
/*      */     }
/*      */     catch (Exception e) {
/* 1051 */       throw new LifecycleException(sm.getString("coyoteConnector.protocolHandlerDestroyFailed"), e);
/*      */     }
/*      */     
/* 1054 */     if (getService() != null) {
/* 1055 */       getService().removeConnector(this);
/*      */     }
/*      */     
/* 1058 */     super.destroyInternal();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1069 */     StringBuilder sb = new StringBuilder("Connector[");
/* 1070 */     sb.append(getProtocol());
/* 1071 */     sb.append('-');
/* 1072 */     int port = getPort();
/* 1073 */     if (port > 0) {
/* 1074 */       sb.append(port);
/*      */     } else {
/* 1076 */       sb.append("auto-");
/* 1077 */       sb.append(getProperty("nameIndex"));
/*      */     }
/* 1079 */     sb.append(']');
/* 1080 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getDomainInternal()
/*      */   {
/* 1088 */     Service s = getService();
/* 1089 */     if (s == null) {
/* 1090 */       return null;
/*      */     }
/* 1092 */     return this.service.getDomain();
/*      */   }
/*      */   
/*      */ 
/*      */   protected String getObjectNameKeyProperties()
/*      */   {
/* 1098 */     return createObjectNameKeyProperties("Connector");
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\connector\Connector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */