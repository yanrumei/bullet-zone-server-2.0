/*      */ package org.springframework.boot.autoconfigure.web;
/*      */ 
/*      */ import io.undertow.Undertow.Builder;
/*      */ import io.undertow.UndertowOptions;
/*      */ import java.io.File;
/*      */ import java.lang.reflect.Method;
/*      */ import java.net.InetAddress;
/*      */ import java.nio.charset.Charset;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import javax.servlet.ServletContext;
/*      */ import javax.servlet.ServletException;
/*      */ import javax.servlet.SessionCookieConfig;
/*      */ import javax.servlet.SessionTrackingMode;
/*      */ import org.apache.catalina.Container;
/*      */ import org.apache.catalina.Context;
/*      */ import org.apache.catalina.Pipeline;
/*      */ import org.apache.catalina.Valve;
/*      */ import org.apache.catalina.valves.AccessLogValve;
/*      */ import org.apache.catalina.valves.ErrorReportValve;
/*      */ import org.apache.catalina.valves.RemoteIpValve;
/*      */ import org.apache.coyote.AbstractProtocol;
/*      */ import org.apache.coyote.ProtocolHandler;
/*      */ import org.apache.coyote.http11.AbstractHttp11Protocol;
/*      */ import org.eclipse.jetty.server.AbstractConnector;
/*      */ import org.eclipse.jetty.server.ConnectionFactory;
/*      */ import org.eclipse.jetty.server.Handler;
/*      */ import org.eclipse.jetty.server.HttpConfiguration;
/*      */ import org.eclipse.jetty.server.HttpConfiguration.ConnectionFactory;
/*      */ import org.eclipse.jetty.server.Server;
/*      */ import org.eclipse.jetty.server.handler.ContextHandler;
/*      */ import org.eclipse.jetty.server.handler.HandlerCollection;
/*      */ import org.eclipse.jetty.server.handler.HandlerWrapper;
/*      */ import org.springframework.boot.cloud.CloudPlatform;
/*      */ import org.springframework.boot.context.embedded.Compression;
/*      */ import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
/*      */ import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
/*      */ import org.springframework.boot.context.embedded.InitParameterConfiguringServletContextInitializer;
/*      */ import org.springframework.boot.context.embedded.JspServlet;
/*      */ import org.springframework.boot.context.embedded.Ssl;
/*      */ import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
/*      */ import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
/*      */ import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
/*      */ import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
/*      */ import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
/*      */ import org.springframework.boot.context.embedded.undertow.UndertowBuilderCustomizer;
/*      */ import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
/*      */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*      */ import org.springframework.boot.context.properties.DeprecatedConfigurationProperty;
/*      */ import org.springframework.boot.context.properties.NestedConfigurationProperty;
/*      */ import org.springframework.boot.web.servlet.ServletContextInitializer;
/*      */ import org.springframework.context.EnvironmentAware;
/*      */ import org.springframework.core.Ordered;
/*      */ import org.springframework.core.env.Environment;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.StringUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @ConfigurationProperties(prefix="server", ignoreUnknownFields=true)
/*      */ public class ServerProperties
/*      */   implements EmbeddedServletContainerCustomizer, EnvironmentAware, Ordered
/*      */ {
/*      */   private Integer port;
/*      */   private InetAddress address;
/*      */   private String contextPath;
/*  118 */   private String displayName = "application";
/*      */   @NestedConfigurationProperty
/*  120 */   private ErrorProperties error = new ErrorProperties();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  126 */   private String servletPath = "/";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  131 */   private final Map<String, String> contextParameters = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private Boolean useForwardHeaders;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private String serverHeader;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  146 */   private int maxHttpHeaderSize = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  151 */   private int maxHttpPostSize = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private Integer connectionTimeout;
/*      */   
/*      */ 
/*      */ 
/*  160 */   private Session session = new Session();
/*      */   
/*      */   @NestedConfigurationProperty
/*      */   private Ssl ssl;
/*      */   @NestedConfigurationProperty
/*  165 */   private Compression compression = new Compression();
/*      */   
/*      */ 
/*      */   @NestedConfigurationProperty
/*      */   private JspServlet jspServlet;
/*      */   
/*  171 */   private final Tomcat tomcat = new Tomcat();
/*      */   
/*  173 */   private final Jetty jetty = new Jetty();
/*      */   
/*  175 */   private final Undertow undertow = new Undertow();
/*      */   
/*      */   private Environment environment;
/*      */   
/*      */   public int getOrder()
/*      */   {
/*  181 */     return 0;
/*      */   }
/*      */   
/*      */   public void setEnvironment(Environment environment)
/*      */   {
/*  186 */     this.environment = environment;
/*      */   }
/*      */   
/*      */   public void customize(ConfigurableEmbeddedServletContainer container)
/*      */   {
/*  191 */     if (getPort() != null) {
/*  192 */       container.setPort(getPort().intValue());
/*      */     }
/*  194 */     if (getAddress() != null) {
/*  195 */       container.setAddress(getAddress());
/*      */     }
/*  197 */     if (getContextPath() != null) {
/*  198 */       container.setContextPath(getContextPath());
/*      */     }
/*  200 */     if (getDisplayName() != null) {
/*  201 */       container.setDisplayName(getDisplayName());
/*      */     }
/*  203 */     if (getSession().getTimeout() != null) {
/*  204 */       container.setSessionTimeout(getSession().getTimeout().intValue());
/*      */     }
/*  206 */     container.setPersistSession(getSession().isPersistent());
/*  207 */     container.setSessionStoreDir(getSession().getStoreDir());
/*  208 */     if (getSsl() != null) {
/*  209 */       container.setSsl(getSsl());
/*      */     }
/*  211 */     if (getJspServlet() != null) {
/*  212 */       container.setJspServlet(getJspServlet());
/*      */     }
/*  214 */     if (getCompression() != null) {
/*  215 */       container.setCompression(getCompression());
/*      */     }
/*  217 */     container.setServerHeader(getServerHeader());
/*  218 */     if ((container instanceof TomcatEmbeddedServletContainerFactory)) {
/*  219 */       getTomcat().customizeTomcat(this, (TomcatEmbeddedServletContainerFactory)container);
/*      */     }
/*      */     
/*  222 */     if ((container instanceof JettyEmbeddedServletContainerFactory)) {
/*  223 */       getJetty().customizeJetty(this, (JettyEmbeddedServletContainerFactory)container);
/*      */     }
/*      */     
/*      */ 
/*  227 */     if ((container instanceof UndertowEmbeddedServletContainerFactory)) {
/*  228 */       getUndertow().customizeUndertow(this, (UndertowEmbeddedServletContainerFactory)container);
/*      */     }
/*      */     
/*  231 */     container.addInitializers(new ServletContextInitializer[] { new SessionConfiguringInitializer(this.session) });
/*  232 */     container.addInitializers(new ServletContextInitializer[] { new InitParameterConfiguringServletContextInitializer(
/*  233 */       getContextParameters()) });
/*      */   }
/*      */   
/*      */   public String getServletMapping() {
/*  237 */     if ((this.servletPath.equals("")) || (this.servletPath.equals("/"))) {
/*  238 */       return "/";
/*      */     }
/*  240 */     if (this.servletPath.contains("*")) {
/*  241 */       return this.servletPath;
/*      */     }
/*  243 */     if (this.servletPath.endsWith("/")) {
/*  244 */       return this.servletPath + "*";
/*      */     }
/*  246 */     return this.servletPath + "/*";
/*      */   }
/*      */   
/*      */   public String getPath(String path) {
/*  250 */     String prefix = getServletPrefix();
/*  251 */     if (!path.startsWith("/")) {
/*  252 */       path = "/" + path;
/*      */     }
/*  254 */     return prefix + path;
/*      */   }
/*      */   
/*      */   public String getServletPrefix() {
/*  258 */     String result = this.servletPath;
/*  259 */     if (result.contains("*")) {
/*  260 */       result = result.substring(0, result.indexOf("*"));
/*      */     }
/*  262 */     if (result.endsWith("/")) {
/*  263 */       result = result.substring(0, result.length() - 1);
/*      */     }
/*  265 */     return result;
/*      */   }
/*      */   
/*      */   public String[] getPathsArray(Collection<String> paths) {
/*  269 */     String[] result = new String[paths.size()];
/*  270 */     int i = 0;
/*  271 */     for (String path : paths) {
/*  272 */       result[(i++)] = getPath(path);
/*      */     }
/*  274 */     return result;
/*      */   }
/*      */   
/*      */   public String[] getPathsArray(String[] paths) {
/*  278 */     String[] result = new String[paths.length];
/*  279 */     int i = 0;
/*  280 */     for (String path : paths) {
/*  281 */       result[(i++)] = getPath(path);
/*      */     }
/*  283 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setLoader(String value) {}
/*      */   
/*      */   public Integer getPort()
/*      */   {
/*  291 */     return this.port;
/*      */   }
/*      */   
/*      */   public void setPort(Integer port) {
/*  295 */     this.port = port;
/*      */   }
/*      */   
/*      */   public InetAddress getAddress() {
/*  299 */     return this.address;
/*      */   }
/*      */   
/*      */   public void setAddress(InetAddress address) {
/*  303 */     this.address = address;
/*      */   }
/*      */   
/*      */   public String getContextPath() {
/*  307 */     return this.contextPath;
/*      */   }
/*      */   
/*      */   public void setContextPath(String contextPath) {
/*  311 */     this.contextPath = cleanContextPath(contextPath);
/*      */   }
/*      */   
/*      */   private String cleanContextPath(String contextPath) {
/*  315 */     if ((StringUtils.hasText(contextPath)) && (contextPath.endsWith("/"))) {
/*  316 */       return contextPath.substring(0, contextPath.length() - 1);
/*      */     }
/*  318 */     return contextPath;
/*      */   }
/*      */   
/*      */   public String getDisplayName() {
/*  322 */     return this.displayName;
/*      */   }
/*      */   
/*      */   public void setDisplayName(String displayName) {
/*  326 */     this.displayName = displayName;
/*      */   }
/*      */   
/*      */   public String getServletPath() {
/*  330 */     return this.servletPath;
/*      */   }
/*      */   
/*      */   public void setServletPath(String servletPath) {
/*  334 */     Assert.notNull(servletPath, "ServletPath must not be null");
/*  335 */     this.servletPath = servletPath;
/*      */   }
/*      */   
/*      */   public Map<String, String> getContextParameters() {
/*  339 */     return this.contextParameters;
/*      */   }
/*      */   
/*      */   public Boolean isUseForwardHeaders() {
/*  343 */     return this.useForwardHeaders;
/*      */   }
/*      */   
/*      */   public void setUseForwardHeaders(Boolean useForwardHeaders) {
/*  347 */     this.useForwardHeaders = useForwardHeaders;
/*      */   }
/*      */   
/*      */   public String getServerHeader() {
/*  351 */     return this.serverHeader;
/*      */   }
/*      */   
/*      */   public void setServerHeader(String serverHeader) {
/*  355 */     this.serverHeader = serverHeader;
/*      */   }
/*      */   
/*      */   public int getMaxHttpHeaderSize() {
/*  359 */     return this.maxHttpHeaderSize;
/*      */   }
/*      */   
/*      */   public void setMaxHttpHeaderSize(int maxHttpHeaderSize) {
/*  363 */     this.maxHttpHeaderSize = maxHttpHeaderSize;
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   @DeprecatedConfigurationProperty(reason="Use dedicated property for each container.")
/*      */   public int getMaxHttpPostSize() {
/*  369 */     return this.maxHttpPostSize;
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public void setMaxHttpPostSize(int maxHttpPostSize) {
/*  374 */     this.maxHttpPostSize = maxHttpPostSize;
/*  375 */     this.jetty.setMaxHttpPostSize(maxHttpPostSize);
/*  376 */     this.tomcat.setMaxHttpPostSize(maxHttpPostSize);
/*  377 */     this.undertow.setMaxHttpPostSize(maxHttpPostSize);
/*      */   }
/*      */   
/*      */   protected final boolean getOrDeduceUseForwardHeaders() {
/*  381 */     if (this.useForwardHeaders != null) {
/*  382 */       return this.useForwardHeaders.booleanValue();
/*      */     }
/*  384 */     CloudPlatform platform = CloudPlatform.getActive(this.environment);
/*  385 */     return platform == null ? false : platform.isUsingForwardHeaders();
/*      */   }
/*      */   
/*      */   public Integer getConnectionTimeout() {
/*  389 */     return this.connectionTimeout;
/*      */   }
/*      */   
/*      */   public void setConnectionTimeout(Integer connectionTimeout) {
/*  393 */     this.connectionTimeout = connectionTimeout;
/*      */   }
/*      */   
/*      */   public ErrorProperties getError() {
/*  397 */     return this.error;
/*      */   }
/*      */   
/*      */   public Session getSession() {
/*  401 */     return this.session;
/*      */   }
/*      */   
/*      */   public void setSession(Session session) {
/*  405 */     this.session = session;
/*      */   }
/*      */   
/*      */   public Ssl getSsl() {
/*  409 */     return this.ssl;
/*      */   }
/*      */   
/*      */   public void setSsl(Ssl ssl) {
/*  413 */     this.ssl = ssl;
/*      */   }
/*      */   
/*      */   public Compression getCompression() {
/*  417 */     return this.compression;
/*      */   }
/*      */   
/*      */   public JspServlet getJspServlet() {
/*  421 */     return this.jspServlet;
/*      */   }
/*      */   
/*      */   public void setJspServlet(JspServlet jspServlet) {
/*  425 */     this.jspServlet = jspServlet;
/*      */   }
/*      */   
/*      */   public Tomcat getTomcat() {
/*  429 */     return this.tomcat;
/*      */   }
/*      */   
/*      */   public Jetty getJetty() {
/*  433 */     return this.jetty;
/*      */   }
/*      */   
/*      */   public Undertow getUndertow() {
/*  437 */     return this.undertow;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class Session
/*      */   {
/*      */     private Integer timeout;
/*      */     
/*      */ 
/*      */ 
/*      */     private Set<SessionTrackingMode> trackingModes;
/*      */     
/*      */ 
/*      */ 
/*      */     private boolean persistent;
/*      */     
/*      */ 
/*      */ 
/*      */     private File storeDir;
/*      */     
/*      */ 
/*      */ 
/*  462 */     private Cookie cookie = new Cookie();
/*      */     
/*      */     public Cookie getCookie() {
/*  465 */       return this.cookie;
/*      */     }
/*      */     
/*      */     public Integer getTimeout() {
/*  469 */       return this.timeout;
/*      */     }
/*      */     
/*      */     public void setTimeout(Integer sessionTimeout) {
/*  473 */       this.timeout = sessionTimeout;
/*      */     }
/*      */     
/*      */     public Set<SessionTrackingMode> getTrackingModes() {
/*  477 */       return this.trackingModes;
/*      */     }
/*      */     
/*      */     public void setTrackingModes(Set<SessionTrackingMode> trackingModes) {
/*  481 */       this.trackingModes = trackingModes;
/*      */     }
/*      */     
/*      */     public boolean isPersistent() {
/*  485 */       return this.persistent;
/*      */     }
/*      */     
/*      */     public void setPersistent(boolean persistent) {
/*  489 */       this.persistent = persistent;
/*      */     }
/*      */     
/*      */     public File getStoreDir() {
/*  493 */       return this.storeDir;
/*      */     }
/*      */     
/*      */     public void setStoreDir(File storeDir) {
/*  497 */       this.storeDir = storeDir;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public static class Cookie
/*      */     {
/*      */       private String name;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       private String domain;
/*      */       
/*      */ 
/*      */ 
/*      */       private String path;
/*      */       
/*      */ 
/*      */ 
/*      */       private String comment;
/*      */       
/*      */ 
/*      */ 
/*      */       private Boolean httpOnly;
/*      */       
/*      */ 
/*      */ 
/*      */       private Boolean secure;
/*      */       
/*      */ 
/*      */ 
/*      */       private Integer maxAge;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public String getName()
/*      */       {
/*  538 */         return this.name;
/*      */       }
/*      */       
/*      */       public void setName(String name) {
/*  542 */         this.name = name;
/*      */       }
/*      */       
/*      */       public String getDomain() {
/*  546 */         return this.domain;
/*      */       }
/*      */       
/*      */       public void setDomain(String domain) {
/*  550 */         this.domain = domain;
/*      */       }
/*      */       
/*      */       public String getPath() {
/*  554 */         return this.path;
/*      */       }
/*      */       
/*      */       public void setPath(String path) {
/*  558 */         this.path = path;
/*      */       }
/*      */       
/*      */       public String getComment() {
/*  562 */         return this.comment;
/*      */       }
/*      */       
/*      */       public void setComment(String comment) {
/*  566 */         this.comment = comment;
/*      */       }
/*      */       
/*      */       public Boolean getHttpOnly() {
/*  570 */         return this.httpOnly;
/*      */       }
/*      */       
/*      */       public void setHttpOnly(Boolean httpOnly) {
/*  574 */         this.httpOnly = httpOnly;
/*      */       }
/*      */       
/*      */       public Boolean getSecure() {
/*  578 */         return this.secure;
/*      */       }
/*      */       
/*      */       public void setSecure(Boolean secure) {
/*  582 */         this.secure = secure;
/*      */       }
/*      */       
/*      */       public Integer getMaxAge() {
/*  586 */         return this.maxAge;
/*      */       }
/*      */       
/*      */       public void setMaxAge(Integer maxAge) {
/*  590 */         this.maxAge = maxAge;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class Tomcat
/*      */   {
/*  602 */     private final Accesslog accesslog = new Accesslog();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  607 */     private String internalProxies = "10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}|192\\.168\\.\\d{1,3}\\.\\d{1,3}|169\\.254\\.\\d{1,3}\\.\\d{1,3}|127\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}|172\\.1[6-9]{1}\\.\\d{1,3}\\.\\d{1,3}|172\\.2[0-9]{1}\\.\\d{1,3}\\.\\d{1,3}|172\\.3[0-1]{1}\\.\\d{1,3}\\.\\d{1,3}";
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private String protocolHeader;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  623 */     private String protocolHeaderHttpsValue = "https";
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  628 */     private String portHeader = "X-Forwarded-Port";
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private String remoteIpHeader;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private File basedir;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  643 */     private int backgroundProcessorDelay = 30;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  648 */     private int maxThreads = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  653 */     private int minSpareThreads = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  658 */     private int maxHttpPostSize = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  663 */     private int maxHttpHeaderSize = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private Boolean redirectContextRoot;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private Charset uriEncoding;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  681 */     private int maxConnections = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  687 */     private int acceptCount = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  694 */     private List<String> additionalTldSkipPatterns = new ArrayList();
/*      */     
/*      */     public int getMaxThreads() {
/*  697 */       return this.maxThreads;
/*      */     }
/*      */     
/*      */     public void setMaxThreads(int maxThreads) {
/*  701 */       this.maxThreads = maxThreads;
/*      */     }
/*      */     
/*      */     public int getMinSpareThreads() {
/*  705 */       return this.minSpareThreads;
/*      */     }
/*      */     
/*      */     public void setMinSpareThreads(int minSpareThreads) {
/*  709 */       this.minSpareThreads = minSpareThreads;
/*      */     }
/*      */     
/*      */     public int getMaxHttpPostSize() {
/*  713 */       return this.maxHttpPostSize;
/*      */     }
/*      */     
/*      */     public void setMaxHttpPostSize(int maxHttpPostSize) {
/*  717 */       this.maxHttpPostSize = maxHttpPostSize;
/*      */     }
/*      */     
/*      */     public Accesslog getAccesslog() {
/*  721 */       return this.accesslog;
/*      */     }
/*      */     
/*      */     public int getBackgroundProcessorDelay() {
/*  725 */       return this.backgroundProcessorDelay;
/*      */     }
/*      */     
/*      */     public void setBackgroundProcessorDelay(int backgroundProcessorDelay) {
/*  729 */       this.backgroundProcessorDelay = backgroundProcessorDelay;
/*      */     }
/*      */     
/*      */     public File getBasedir() {
/*  733 */       return this.basedir;
/*      */     }
/*      */     
/*      */     public void setBasedir(File basedir) {
/*  737 */       this.basedir = basedir;
/*      */     }
/*      */     
/*      */     public String getInternalProxies() {
/*  741 */       return this.internalProxies;
/*      */     }
/*      */     
/*      */     public void setInternalProxies(String internalProxies) {
/*  745 */       this.internalProxies = internalProxies;
/*      */     }
/*      */     
/*      */     public String getProtocolHeader() {
/*  749 */       return this.protocolHeader;
/*      */     }
/*      */     
/*      */     public void setProtocolHeader(String protocolHeader) {
/*  753 */       this.protocolHeader = protocolHeader;
/*      */     }
/*      */     
/*      */     public String getProtocolHeaderHttpsValue() {
/*  757 */       return this.protocolHeaderHttpsValue;
/*      */     }
/*      */     
/*      */     public void setProtocolHeaderHttpsValue(String protocolHeaderHttpsValue) {
/*  761 */       this.protocolHeaderHttpsValue = protocolHeaderHttpsValue;
/*      */     }
/*      */     
/*      */     public String getPortHeader() {
/*  765 */       return this.portHeader;
/*      */     }
/*      */     
/*      */     public void setPortHeader(String portHeader) {
/*  769 */       this.portHeader = portHeader;
/*      */     }
/*      */     
/*      */     public Boolean getRedirectContextRoot() {
/*  773 */       return this.redirectContextRoot;
/*      */     }
/*      */     
/*      */     public void setRedirectContextRoot(Boolean redirectContextRoot) {
/*  777 */       this.redirectContextRoot = redirectContextRoot;
/*      */     }
/*      */     
/*      */     public String getRemoteIpHeader() {
/*  781 */       return this.remoteIpHeader;
/*      */     }
/*      */     
/*      */     public void setRemoteIpHeader(String remoteIpHeader) {
/*  785 */       this.remoteIpHeader = remoteIpHeader;
/*      */     }
/*      */     
/*      */     public Charset getUriEncoding() {
/*  789 */       return this.uriEncoding;
/*      */     }
/*      */     
/*      */     public void setUriEncoding(Charset uriEncoding) {
/*  793 */       this.uriEncoding = uriEncoding;
/*      */     }
/*      */     
/*      */     public int getMaxConnections() {
/*  797 */       return this.maxConnections;
/*      */     }
/*      */     
/*      */     public void setMaxConnections(int maxConnections) {
/*  801 */       this.maxConnections = maxConnections;
/*      */     }
/*      */     
/*      */     public int getAcceptCount() {
/*  805 */       return this.acceptCount;
/*      */     }
/*      */     
/*      */     public void setAcceptCount(int acceptCount) {
/*  809 */       this.acceptCount = acceptCount;
/*      */     }
/*      */     
/*      */     public List<String> getAdditionalTldSkipPatterns() {
/*  813 */       return this.additionalTldSkipPatterns;
/*      */     }
/*      */     
/*      */     public void setAdditionalTldSkipPatterns(List<String> additionalTldSkipPatterns) {
/*  817 */       this.additionalTldSkipPatterns = additionalTldSkipPatterns;
/*      */     }
/*      */     
/*      */     void customizeTomcat(ServerProperties serverProperties, TomcatEmbeddedServletContainerFactory factory)
/*      */     {
/*  822 */       if (getBasedir() != null) {
/*  823 */         factory.setBaseDirectory(getBasedir());
/*      */       }
/*  825 */       factory.setBackgroundProcessorDelay(this.backgroundProcessorDelay);
/*  826 */       customizeRemoteIpValve(serverProperties, factory);
/*  827 */       if (this.maxThreads > 0) {
/*  828 */         customizeMaxThreads(factory);
/*      */       }
/*  830 */       if (this.minSpareThreads > 0) {
/*  831 */         customizeMinThreads(factory);
/*      */       }
/*      */       
/*  834 */       int maxHttpHeaderSize = serverProperties.getMaxHttpHeaderSize() > 0 ? serverProperties.getMaxHttpHeaderSize() : this.maxHttpHeaderSize;
/*  835 */       if (maxHttpHeaderSize > 0) {
/*  836 */         customizeMaxHttpHeaderSize(factory, maxHttpHeaderSize);
/*      */       }
/*  838 */       if (this.maxHttpPostSize != 0) {
/*  839 */         customizeMaxHttpPostSize(factory, this.maxHttpPostSize);
/*      */       }
/*  841 */       if (this.accesslog.enabled) {
/*  842 */         customizeAccessLog(factory);
/*      */       }
/*  844 */       if (getUriEncoding() != null) {
/*  845 */         factory.setUriEncoding(getUriEncoding());
/*      */       }
/*  847 */       if (serverProperties.getConnectionTimeout() != null) {
/*  848 */         customizeConnectionTimeout(factory, serverProperties
/*  849 */           .getConnectionTimeout().intValue());
/*      */       }
/*  851 */       if (this.redirectContextRoot != null) {
/*  852 */         customizeRedirectContextRoot(factory, this.redirectContextRoot.booleanValue());
/*      */       }
/*  854 */       if (this.maxConnections > 0) {
/*  855 */         customizeMaxConnections(factory);
/*      */       }
/*  857 */       if (this.acceptCount > 0) {
/*  858 */         customizeAcceptCount(factory);
/*      */       }
/*  860 */       if (!ObjectUtils.isEmpty(this.additionalTldSkipPatterns)) {
/*  861 */         factory.getTldSkipPatterns().addAll(this.additionalTldSkipPatterns);
/*      */       }
/*      */       
/*  864 */       if (serverProperties.getError().getIncludeStacktrace() == ErrorProperties.IncludeStacktrace.NEVER) {
/*  865 */         customizeErrorReportValve(factory);
/*      */       }
/*      */     }
/*      */     
/*      */     private void customizeErrorReportValve(TomcatEmbeddedServletContainerFactory factory)
/*      */     {
/*  871 */       factory.addContextCustomizers(new TomcatContextCustomizer[] { new TomcatContextCustomizer()
/*      */       {
/*      */         public void customize(Context context)
/*      */         {
/*  875 */           ErrorReportValve valve = new ErrorReportValve();
/*  876 */           valve.setShowServerInfo(false);
/*  877 */           valve.setShowReport(false);
/*  878 */           context.getParent().getPipeline().addValve(valve);
/*      */         }
/*      */       } });
/*      */     }
/*      */     
/*      */     private void customizeAcceptCount(TomcatEmbeddedServletContainerFactory factory)
/*      */     {
/*  885 */       factory.addConnectorCustomizers(new TomcatConnectorCustomizer[] { new TomcatConnectorCustomizer()
/*      */       {
/*      */ 
/*      */         public void customize(org.apache.catalina.connector.Connector connector)
/*      */         {
/*  890 */           ProtocolHandler handler = connector.getProtocolHandler();
/*  891 */           if ((handler instanceof AbstractProtocol)) {
/*  892 */             AbstractProtocol<?> protocol = (AbstractProtocol)handler;
/*  893 */             protocol.setBacklog(ServerProperties.Tomcat.this.acceptCount);
/*      */           }
/*      */         }
/*      */       } });
/*      */     }
/*      */     
/*      */ 
/*      */     private void customizeMaxConnections(TomcatEmbeddedServletContainerFactory factory)
/*      */     {
/*  902 */       factory.addConnectorCustomizers(new TomcatConnectorCustomizer[] { new TomcatConnectorCustomizer()
/*      */       {
/*      */         public void customize(org.apache.catalina.connector.Connector connector)
/*      */         {
/*  906 */           ProtocolHandler handler = connector.getProtocolHandler();
/*  907 */           if ((handler instanceof AbstractProtocol)) {
/*  908 */             AbstractProtocol<?> protocol = (AbstractProtocol)handler;
/*  909 */             protocol.setMaxConnections(ServerProperties.Tomcat.this.maxConnections);
/*      */           }
/*      */         }
/*      */       } });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private void customizeConnectionTimeout(TomcatEmbeddedServletContainerFactory factory, final int connectionTimeout)
/*      */     {
/*  919 */       factory.addConnectorCustomizers(new TomcatConnectorCustomizer[] { new TomcatConnectorCustomizer()
/*      */       {
/*      */         public void customize(org.apache.catalina.connector.Connector connector)
/*      */         {
/*  923 */           ProtocolHandler handler = connector.getProtocolHandler();
/*  924 */           if ((handler instanceof AbstractProtocol)) {
/*  925 */             AbstractProtocol<?> protocol = (AbstractProtocol)handler;
/*  926 */             protocol.setConnectionTimeout(connectionTimeout);
/*      */           }
/*      */         }
/*      */       } });
/*      */     }
/*      */     
/*      */ 
/*      */     private void customizeRemoteIpValve(ServerProperties properties, TomcatEmbeddedServletContainerFactory factory)
/*      */     {
/*  935 */       String protocolHeader = getProtocolHeader();
/*  936 */       String remoteIpHeader = getRemoteIpHeader();
/*      */       
/*  938 */       if ((StringUtils.hasText(protocolHeader)) || (StringUtils.hasText(remoteIpHeader)) || 
/*  939 */         (properties.getOrDeduceUseForwardHeaders())) {
/*  940 */         RemoteIpValve valve = new RemoteIpValve();
/*  941 */         valve.setProtocolHeader(StringUtils.hasLength(protocolHeader) ? protocolHeader : "X-Forwarded-Proto");
/*      */         
/*  943 */         if (StringUtils.hasLength(remoteIpHeader)) {
/*  944 */           valve.setRemoteIpHeader(remoteIpHeader);
/*      */         }
/*      */         
/*      */ 
/*  948 */         valve.setInternalProxies(getInternalProxies());
/*  949 */         valve.setPortHeader(getPortHeader());
/*  950 */         valve.setProtocolHeaderHttpsValue(getProtocolHeaderHttpsValue());
/*      */         
/*  952 */         factory.addEngineValves(new Valve[] { valve });
/*      */       }
/*      */     }
/*      */     
/*      */     private void customizeMaxThreads(TomcatEmbeddedServletContainerFactory factory)
/*      */     {
/*  958 */       factory.addConnectorCustomizers(new TomcatConnectorCustomizer[] { new TomcatConnectorCustomizer()
/*      */       {
/*      */         public void customize(org.apache.catalina.connector.Connector connector)
/*      */         {
/*  962 */           ProtocolHandler handler = connector.getProtocolHandler();
/*  963 */           if ((handler instanceof AbstractProtocol)) {
/*  964 */             AbstractProtocol protocol = (AbstractProtocol)handler;
/*  965 */             protocol.setMaxThreads(ServerProperties.Tomcat.this.maxThreads);
/*      */           }
/*      */         }
/*      */       } });
/*      */     }
/*      */     
/*      */ 
/*      */     private void customizeMinThreads(TomcatEmbeddedServletContainerFactory factory)
/*      */     {
/*  974 */       factory.addConnectorCustomizers(new TomcatConnectorCustomizer[] { new TomcatConnectorCustomizer()
/*      */       {
/*      */         public void customize(org.apache.catalina.connector.Connector connector)
/*      */         {
/*  978 */           ProtocolHandler handler = connector.getProtocolHandler();
/*  979 */           if ((handler instanceof AbstractProtocol)) {
/*  980 */             AbstractProtocol protocol = (AbstractProtocol)handler;
/*  981 */             protocol.setMinSpareThreads(ServerProperties.Tomcat.this.minSpareThreads);
/*      */           }
/*      */         }
/*      */       } });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private void customizeMaxHttpHeaderSize(TomcatEmbeddedServletContainerFactory factory, final int maxHttpHeaderSize)
/*      */     {
/*  992 */       factory.addConnectorCustomizers(new TomcatConnectorCustomizer[] { new TomcatConnectorCustomizer()
/*      */       {
/*      */         public void customize(org.apache.catalina.connector.Connector connector)
/*      */         {
/*  996 */           ProtocolHandler handler = connector.getProtocolHandler();
/*  997 */           if ((handler instanceof AbstractHttp11Protocol)) {
/*  998 */             AbstractHttp11Protocol protocol = (AbstractHttp11Protocol)handler;
/*  999 */             protocol.setMaxHttpHeaderSize(maxHttpHeaderSize);
/*      */           }
/*      */         }
/*      */       } });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private void customizeMaxHttpPostSize(TomcatEmbeddedServletContainerFactory factory, final int maxHttpPostSize)
/*      */     {
/* 1009 */       factory.addConnectorCustomizers(new TomcatConnectorCustomizer[] { new TomcatConnectorCustomizer()
/*      */       {
/*      */         public void customize(org.apache.catalina.connector.Connector connector)
/*      */         {
/* 1013 */           connector.setMaxPostSize(maxHttpPostSize);
/*      */         }
/*      */       } });
/*      */     }
/*      */     
/*      */     private void customizeAccessLog(TomcatEmbeddedServletContainerFactory factory)
/*      */     {
/* 1020 */       AccessLogValve valve = new AccessLogValve();
/* 1021 */       valve.setPattern(this.accesslog.getPattern());
/* 1022 */       valve.setDirectory(this.accesslog.getDirectory());
/* 1023 */       valve.setPrefix(this.accesslog.getPrefix());
/* 1024 */       valve.setSuffix(this.accesslog.getSuffix());
/* 1025 */       valve.setRenameOnRotate(this.accesslog.isRenameOnRotate());
/* 1026 */       valve.setRequestAttributesEnabled(this.accesslog
/* 1027 */         .isRequestAttributesEnabled());
/* 1028 */       valve.setRotatable(this.accesslog.isRotate());
/* 1029 */       valve.setBuffered(this.accesslog.isBuffered());
/* 1030 */       valve.setFileDateFormat(this.accesslog.getFileDateFormat());
/* 1031 */       factory.addEngineValves(new Valve[] { valve });
/*      */     }
/*      */     
/*      */ 
/*      */     private void customizeRedirectContextRoot(TomcatEmbeddedServletContainerFactory factory, final boolean redirectContextRoot)
/*      */     {
/* 1037 */       factory.addContextCustomizers(new TomcatContextCustomizer[] { new TomcatContextCustomizer()
/*      */       {
/*      */         public void customize(Context context)
/*      */         {
/* 1041 */           context.setMapperContextRootRedirectEnabled(redirectContextRoot);
/*      */         }
/*      */       } });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public static class Accesslog
/*      */     {
/* 1052 */       private boolean enabled = false;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1057 */       private String pattern = "common";
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1063 */       private String directory = "logs";
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1068 */       protected String prefix = "access_log";
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1073 */       private String suffix = ".log";
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1078 */       private boolean rotate = true;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       private boolean renameOnRotate;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1088 */       private String fileDateFormat = ".yyyy-MM-dd";
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       private boolean requestAttributesEnabled;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1099 */       private boolean buffered = true;
/*      */       
/*      */       public boolean isEnabled() {
/* 1102 */         return this.enabled;
/*      */       }
/*      */       
/*      */       public void setEnabled(boolean enabled) {
/* 1106 */         this.enabled = enabled;
/*      */       }
/*      */       
/*      */       public String getPattern() {
/* 1110 */         return this.pattern;
/*      */       }
/*      */       
/*      */       public void setPattern(String pattern) {
/* 1114 */         this.pattern = pattern;
/*      */       }
/*      */       
/*      */       public String getDirectory() {
/* 1118 */         return this.directory;
/*      */       }
/*      */       
/*      */       public void setDirectory(String directory) {
/* 1122 */         this.directory = directory;
/*      */       }
/*      */       
/*      */       public String getPrefix() {
/* 1126 */         return this.prefix;
/*      */       }
/*      */       
/*      */       public void setPrefix(String prefix) {
/* 1130 */         this.prefix = prefix;
/*      */       }
/*      */       
/*      */       public String getSuffix() {
/* 1134 */         return this.suffix;
/*      */       }
/*      */       
/*      */       public void setSuffix(String suffix) {
/* 1138 */         this.suffix = suffix;
/*      */       }
/*      */       
/*      */       public boolean isRotate() {
/* 1142 */         return this.rotate;
/*      */       }
/*      */       
/*      */       public void setRotate(boolean rotate) {
/* 1146 */         this.rotate = rotate;
/*      */       }
/*      */       
/*      */       public boolean isRenameOnRotate() {
/* 1150 */         return this.renameOnRotate;
/*      */       }
/*      */       
/*      */       public void setRenameOnRotate(boolean renameOnRotate) {
/* 1154 */         this.renameOnRotate = renameOnRotate;
/*      */       }
/*      */       
/*      */       public String getFileDateFormat() {
/* 1158 */         return this.fileDateFormat;
/*      */       }
/*      */       
/*      */       public void setFileDateFormat(String fileDateFormat) {
/* 1162 */         this.fileDateFormat = fileDateFormat;
/*      */       }
/*      */       
/*      */       public boolean isRequestAttributesEnabled() {
/* 1166 */         return this.requestAttributesEnabled;
/*      */       }
/*      */       
/*      */       public void setRequestAttributesEnabled(boolean requestAttributesEnabled) {
/* 1170 */         this.requestAttributesEnabled = requestAttributesEnabled;
/*      */       }
/*      */       
/*      */       public boolean isBuffered() {
/* 1174 */         return this.buffered;
/*      */       }
/*      */       
/*      */       public void setBuffered(boolean buffered) {
/* 1178 */         this.buffered = buffered;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class Jetty
/*      */   {
/* 1190 */     private int maxHttpPostSize = 0;
/*      */     
/*      */ 
/*      */ 
/*      */     private Integer acceptors;
/*      */     
/*      */ 
/*      */     private Integer selectors;
/*      */     
/*      */ 
/*      */ 
/*      */     public int getMaxHttpPostSize()
/*      */     {
/* 1203 */       return this.maxHttpPostSize;
/*      */     }
/*      */     
/*      */     public void setMaxHttpPostSize(int maxHttpPostSize) {
/* 1207 */       this.maxHttpPostSize = maxHttpPostSize;
/*      */     }
/*      */     
/*      */     public Integer getAcceptors() {
/* 1211 */       return this.acceptors;
/*      */     }
/*      */     
/*      */     public void setAcceptors(Integer acceptors) {
/* 1215 */       this.acceptors = acceptors;
/*      */     }
/*      */     
/*      */     public Integer getSelectors() {
/* 1219 */       return this.selectors;
/*      */     }
/*      */     
/*      */     public void setSelectors(Integer selectors) {
/* 1223 */       this.selectors = selectors;
/*      */     }
/*      */     
/*      */     void customizeJetty(ServerProperties serverProperties, JettyEmbeddedServletContainerFactory factory)
/*      */     {
/* 1228 */       factory.setUseForwardHeaders(serverProperties.getOrDeduceUseForwardHeaders());
/* 1229 */       if (this.acceptors != null) {
/* 1230 */         factory.setAcceptors(this.acceptors.intValue());
/*      */       }
/* 1232 */       if (this.selectors != null) {
/* 1233 */         factory.setSelectors(this.selectors.intValue());
/*      */       }
/* 1235 */       if (serverProperties.getMaxHttpHeaderSize() > 0) {
/* 1236 */         customizeMaxHttpHeaderSize(factory, serverProperties
/* 1237 */           .getMaxHttpHeaderSize());
/*      */       }
/* 1239 */       if (this.maxHttpPostSize > 0) {
/* 1240 */         customizeMaxHttpPostSize(factory, this.maxHttpPostSize);
/*      */       }
/*      */       
/* 1243 */       if (serverProperties.getConnectionTimeout() != null) {
/* 1244 */         customizeConnectionTimeout(factory, serverProperties
/* 1245 */           .getConnectionTimeout().intValue());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     private void customizeConnectionTimeout(JettyEmbeddedServletContainerFactory factory, final int connectionTimeout)
/*      */     {
/* 1252 */       factory.addServerCustomizers(new JettyServerCustomizer[] { new JettyServerCustomizer()
/*      */       {
/*      */         public void customize(Server server)
/*      */         {
/* 1256 */           for (org.eclipse.jetty.server.Connector connector : server
/* 1257 */             .getConnectors()) {
/* 1258 */             if ((connector instanceof AbstractConnector))
/*      */             {
/* 1260 */               ((AbstractConnector)connector).setIdleTimeout(connectionTimeout);
/*      */             }
/*      */           }
/*      */         }
/*      */       } });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private void customizeMaxHttpHeaderSize(JettyEmbeddedServletContainerFactory factory, final int maxHttpHeaderSize)
/*      */     {
/* 1271 */       factory.addServerCustomizers(new JettyServerCustomizer[] { new JettyServerCustomizer()
/*      */       {
/*      */         public void customize(Server server)
/*      */         {
/* 1275 */           for (org.eclipse.jetty.server.Connector connector : server
/* 1276 */             .getConnectors()) {
/*      */             try {
/* 1278 */               for (ConnectionFactory connectionFactory : connector
/* 1279 */                 .getConnectionFactories()) {
/* 1280 */                 if ((connectionFactory instanceof HttpConfiguration.ConnectionFactory)) {
/* 1281 */                   customize((HttpConfiguration.ConnectionFactory)connectionFactory);
/*      */                 }
/*      */               }
/*      */             }
/*      */             catch (NoSuchMethodError ex)
/*      */             {
/* 1287 */               customizeOnJetty8(connector, maxHttpHeaderSize);
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */         private void customize(HttpConfiguration.ConnectionFactory factory)
/*      */         {
/* 1294 */           HttpConfiguration configuration = factory.getHttpConfiguration();
/* 1295 */           configuration.setRequestHeaderSize(maxHttpHeaderSize);
/* 1296 */           configuration.setResponseHeaderSize(maxHttpHeaderSize);
/*      */         }
/*      */         
/*      */ 
/*      */         private void customizeOnJetty8(org.eclipse.jetty.server.Connector connector, int maxHttpHeaderSize)
/*      */         {
/*      */           try
/*      */           {
/* 1304 */             connector.getClass().getMethod("setRequestHeaderSize", new Class[] { Integer.TYPE }).invoke(connector, new Object[] { Integer.valueOf(maxHttpHeaderSize) });
/* 1305 */             connector.getClass().getMethod("setResponseHeaderSize", new Class[] { Integer.TYPE })
/* 1306 */               .invoke(connector, new Object[] {Integer.valueOf(maxHttpHeaderSize) });
/*      */           }
/*      */           catch (Exception ex) {
/* 1309 */             throw new RuntimeException(ex);
/*      */           }
/*      */         }
/*      */       } });
/*      */     }
/*      */     
/*      */ 
/*      */     private void customizeMaxHttpPostSize(JettyEmbeddedServletContainerFactory factory, final int maxHttpPostSize)
/*      */     {
/* 1318 */       factory.addServerCustomizers(new JettyServerCustomizer[] { new JettyServerCustomizer()
/*      */       {
/*      */         public void customize(Server server)
/*      */         {
/* 1322 */           setHandlerMaxHttpPostSize(maxHttpPostSize, server.getHandlers());
/*      */         }
/*      */         
/*      */         private void setHandlerMaxHttpPostSize(int maxHttpPostSize, Handler... handlers)
/*      */         {
/* 1327 */           for (Handler handler : handlers) {
/* 1328 */             if ((handler instanceof ContextHandler))
/*      */             {
/* 1330 */               ((ContextHandler)handler).setMaxFormContentSize(maxHttpPostSize);
/*      */             }
/* 1332 */             else if ((handler instanceof HandlerWrapper)) {
/* 1333 */               setHandlerMaxHttpPostSize(maxHttpPostSize, new Handler[] {((HandlerWrapper)handler)
/* 1334 */                 .getHandler() });
/*      */             }
/* 1336 */             else if ((handler instanceof HandlerCollection)) {
/* 1337 */               setHandlerMaxHttpPostSize(maxHttpPostSize, ((HandlerCollection)handler)
/* 1338 */                 .getHandlers());
/*      */             }
/*      */           }
/*      */         }
/*      */       } });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class Undertow
/*      */   {
/* 1353 */     private long maxHttpPostSize = 0L;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private Integer bufferSize;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     @Deprecated
/*      */     private Integer buffersPerRegion;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private Integer ioThreads;
/*      */     
/*      */ 
/*      */ 
/*      */     private Integer workerThreads;
/*      */     
/*      */ 
/*      */ 
/*      */     private Boolean directBuffers;
/*      */     
/*      */ 
/*      */ 
/* 1381 */     private final Accesslog accesslog = new Accesslog();
/*      */     
/*      */     public long getMaxHttpPostSize() {
/* 1384 */       return this.maxHttpPostSize;
/*      */     }
/*      */     
/*      */     public void setMaxHttpPostSize(long maxHttpPostSize) {
/* 1388 */       this.maxHttpPostSize = maxHttpPostSize;
/*      */     }
/*      */     
/*      */     public Integer getBufferSize() {
/* 1392 */       return this.bufferSize;
/*      */     }
/*      */     
/*      */     public void setBufferSize(Integer bufferSize) {
/* 1396 */       this.bufferSize = bufferSize;
/*      */     }
/*      */     
/*      */     @DeprecatedConfigurationProperty(reason="The property is not used by Undertow. See https://issues.jboss.org/browse/UNDERTOW-587 for details")
/*      */     public Integer getBuffersPerRegion() {
/* 1401 */       return this.buffersPerRegion;
/*      */     }
/*      */     
/*      */     public void setBuffersPerRegion(Integer buffersPerRegion) {
/* 1405 */       this.buffersPerRegion = buffersPerRegion;
/*      */     }
/*      */     
/*      */     public Integer getIoThreads() {
/* 1409 */       return this.ioThreads;
/*      */     }
/*      */     
/*      */     public void setIoThreads(Integer ioThreads) {
/* 1413 */       this.ioThreads = ioThreads;
/*      */     }
/*      */     
/*      */     public Integer getWorkerThreads() {
/* 1417 */       return this.workerThreads;
/*      */     }
/*      */     
/*      */     public void setWorkerThreads(Integer workerThreads) {
/* 1421 */       this.workerThreads = workerThreads;
/*      */     }
/*      */     
/*      */     public Boolean getDirectBuffers() {
/* 1425 */       return this.directBuffers;
/*      */     }
/*      */     
/*      */     public void setDirectBuffers(Boolean directBuffers) {
/* 1429 */       this.directBuffers = directBuffers;
/*      */     }
/*      */     
/*      */     public Accesslog getAccesslog() {
/* 1433 */       return this.accesslog;
/*      */     }
/*      */     
/*      */     void customizeUndertow(ServerProperties serverProperties, UndertowEmbeddedServletContainerFactory factory)
/*      */     {
/* 1438 */       if (this.bufferSize != null) {
/* 1439 */         factory.setBufferSize(this.bufferSize);
/*      */       }
/* 1441 */       if (this.ioThreads != null) {
/* 1442 */         factory.setIoThreads(this.ioThreads);
/*      */       }
/* 1444 */       if (this.workerThreads != null) {
/* 1445 */         factory.setWorkerThreads(this.workerThreads);
/*      */       }
/* 1447 */       if (this.directBuffers != null) {
/* 1448 */         factory.setDirectBuffers(this.directBuffers);
/*      */       }
/* 1450 */       if (this.accesslog.enabled != null) {
/* 1451 */         factory.setAccessLogEnabled(this.accesslog.enabled.booleanValue());
/*      */       }
/* 1453 */       factory.setAccessLogDirectory(this.accesslog.dir);
/* 1454 */       factory.setAccessLogPattern(this.accesslog.pattern);
/* 1455 */       factory.setAccessLogPrefix(this.accesslog.prefix);
/* 1456 */       factory.setAccessLogSuffix(this.accesslog.suffix);
/* 1457 */       factory.setAccessLogRotate(this.accesslog.rotate);
/* 1458 */       factory.setUseForwardHeaders(serverProperties.getOrDeduceUseForwardHeaders());
/* 1459 */       if (serverProperties.getMaxHttpHeaderSize() > 0) {
/* 1460 */         customizeMaxHttpHeaderSize(factory, serverProperties
/* 1461 */           .getMaxHttpHeaderSize());
/*      */       }
/* 1463 */       if (this.maxHttpPostSize > 0L) {
/* 1464 */         customizeMaxHttpPostSize(factory, this.maxHttpPostSize);
/*      */       }
/*      */       
/* 1467 */       if (serverProperties.getConnectionTimeout() != null) {
/* 1468 */         customizeConnectionTimeout(factory, serverProperties
/* 1469 */           .getConnectionTimeout().intValue());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     private void customizeConnectionTimeout(UndertowEmbeddedServletContainerFactory factory, final int connectionTimeout)
/*      */     {
/* 1476 */       factory.addBuilderCustomizers(new UndertowBuilderCustomizer[] { new UndertowBuilderCustomizer()
/*      */       {
/*      */         public void customize(Undertow.Builder builder) {
/* 1479 */           builder.setSocketOption(UndertowOptions.NO_REQUEST_TIMEOUT, 
/* 1480 */             Integer.valueOf(connectionTimeout));
/*      */         }
/*      */       } });
/*      */     }
/*      */     
/*      */ 
/*      */     private void customizeMaxHttpHeaderSize(UndertowEmbeddedServletContainerFactory factory, final int maxHttpHeaderSize)
/*      */     {
/* 1488 */       factory.addBuilderCustomizers(new UndertowBuilderCustomizer[] { new UndertowBuilderCustomizer()
/*      */       {
/*      */         public void customize(Undertow.Builder builder)
/*      */         {
/* 1492 */           builder.setServerOption(UndertowOptions.MAX_HEADER_SIZE, 
/* 1493 */             Integer.valueOf(maxHttpHeaderSize));
/*      */         }
/*      */       } });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private void customizeMaxHttpPostSize(UndertowEmbeddedServletContainerFactory factory, final long maxHttpPostSize)
/*      */     {
/* 1502 */       factory.addBuilderCustomizers(new UndertowBuilderCustomizer[] { new UndertowBuilderCustomizer()
/*      */       {
/*      */         public void customize(Undertow.Builder builder)
/*      */         {
/* 1506 */           builder.setServerOption(UndertowOptions.MAX_ENTITY_SIZE, 
/* 1507 */             Long.valueOf(maxHttpPostSize));
/*      */         }
/*      */       } });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public static class Accesslog
/*      */     {
/*      */       private Boolean enabled;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1523 */       private String pattern = "common";
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1528 */       protected String prefix = "access_log.";
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1533 */       private String suffix = "log";
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1538 */       private File dir = new File("logs");
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1543 */       private boolean rotate = true;
/*      */       
/*      */       public Boolean getEnabled() {
/* 1546 */         return this.enabled;
/*      */       }
/*      */       
/*      */       public void setEnabled(Boolean enabled) {
/* 1550 */         this.enabled = enabled;
/*      */       }
/*      */       
/*      */       public String getPattern() {
/* 1554 */         return this.pattern;
/*      */       }
/*      */       
/*      */       public void setPattern(String pattern) {
/* 1558 */         this.pattern = pattern;
/*      */       }
/*      */       
/*      */       public String getPrefix() {
/* 1562 */         return this.prefix;
/*      */       }
/*      */       
/*      */       public void setPrefix(String prefix) {
/* 1566 */         this.prefix = prefix;
/*      */       }
/*      */       
/*      */       public String getSuffix() {
/* 1570 */         return this.suffix;
/*      */       }
/*      */       
/*      */       public void setSuffix(String suffix) {
/* 1574 */         this.suffix = suffix;
/*      */       }
/*      */       
/*      */       public File getDir() {
/* 1578 */         return this.dir;
/*      */       }
/*      */       
/*      */       public void setDir(File dir) {
/* 1582 */         this.dir = dir;
/*      */       }
/*      */       
/*      */       public boolean isRotate() {
/* 1586 */         return this.rotate;
/*      */       }
/*      */       
/*      */       public void setRotate(boolean rotate) {
/* 1590 */         this.rotate = rotate;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class SessionConfiguringInitializer
/*      */     implements ServletContextInitializer
/*      */   {
/*      */     private final ServerProperties.Session session;
/*      */     
/*      */ 
/*      */ 
/*      */     SessionConfiguringInitializer(ServerProperties.Session session)
/*      */     {
/* 1607 */       this.session = session;
/*      */     }
/*      */     
/*      */     public void onStartup(ServletContext servletContext) throws ServletException
/*      */     {
/* 1612 */       if (this.session.getTrackingModes() != null) {
/* 1613 */         servletContext.setSessionTrackingModes(this.session.getTrackingModes());
/*      */       }
/* 1615 */       configureSessionCookie(servletContext.getSessionCookieConfig());
/*      */     }
/*      */     
/*      */     private void configureSessionCookie(SessionCookieConfig config) {
/* 1619 */       ServerProperties.Session.Cookie cookie = this.session.getCookie();
/* 1620 */       if (cookie.getName() != null) {
/* 1621 */         config.setName(cookie.getName());
/*      */       }
/* 1623 */       if (cookie.getDomain() != null) {
/* 1624 */         config.setDomain(cookie.getDomain());
/*      */       }
/* 1626 */       if (cookie.getPath() != null) {
/* 1627 */         config.setPath(cookie.getPath());
/*      */       }
/* 1629 */       if (cookie.getComment() != null) {
/* 1630 */         config.setComment(cookie.getComment());
/*      */       }
/* 1632 */       if (cookie.getHttpOnly() != null) {
/* 1633 */         config.setHttpOnly(cookie.getHttpOnly().booleanValue());
/*      */       }
/* 1635 */       if (cookie.getSecure() != null) {
/* 1636 */         config.setSecure(cookie.getSecure().booleanValue());
/*      */       }
/* 1638 */       if (cookie.getMaxAge() != null) {
/* 1639 */         config.setMaxAge(cookie.getMaxAge().intValue());
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\web\ServerProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */