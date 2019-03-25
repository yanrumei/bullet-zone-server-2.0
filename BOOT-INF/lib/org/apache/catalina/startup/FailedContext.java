/*     */ package org.apache.catalina.startup;
/*     */ 
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.File;
/*     */ import java.net.URL;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.servlet.ServletContainerInitializer;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletRegistration.Dynamic;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletSecurityElement;
/*     */ import javax.servlet.descriptor.JspConfigDescriptor;
/*     */ import org.apache.catalina.AccessLog;
/*     */ import org.apache.catalina.Authenticator;
/*     */ import org.apache.catalina.Cluster;
/*     */ import org.apache.catalina.Container;
/*     */ import org.apache.catalina.ContainerListener;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.Engine;
/*     */ import org.apache.catalina.Host;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.Loader;
/*     */ import org.apache.catalina.Manager;
/*     */ import org.apache.catalina.Pipeline;
/*     */ import org.apache.catalina.Realm;
/*     */ import org.apache.catalina.ThreadBindingListener;
/*     */ import org.apache.catalina.Valve;
/*     */ import org.apache.catalina.WebResourceRoot;
/*     */ import org.apache.catalina.Wrapper;
/*     */ import org.apache.catalina.connector.Request;
/*     */ import org.apache.catalina.connector.Response;
/*     */ import org.apache.catalina.deploy.NamingResourcesImpl;
/*     */ import org.apache.catalina.util.ContextName;
/*     */ import org.apache.catalina.util.LifecycleMBeanBase;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.tomcat.InstanceManager;
/*     */ import org.apache.tomcat.JarScanner;
/*     */ import org.apache.tomcat.util.descriptor.web.ApplicationParameter;
/*     */ import org.apache.tomcat.util.descriptor.web.ErrorPage;
/*     */ import org.apache.tomcat.util.descriptor.web.FilterDef;
/*     */ import org.apache.tomcat.util.descriptor.web.FilterMap;
/*     */ import org.apache.tomcat.util.descriptor.web.LoginConfig;
/*     */ import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
/*     */ import org.apache.tomcat.util.http.CookieProcessor;
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
/*     */ public class FailedContext
/*     */   extends LifecycleMBeanBase
/*     */   implements Context
/*     */ {
/*  76 */   protected static final StringManager sm = StringManager.getManager("org.apache.catalina.startup");
/*     */   
/*     */ 
/*     */   private URL configFile;
/*     */   
/*     */   private String docBase;
/*     */   
/*  83 */   public URL getConfigFile() { return this.configFile; }
/*     */   
/*  85 */   public void setConfigFile(URL configFile) { this.configFile = configFile; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  90 */   public String getDocBase() { return this.docBase; }
/*     */   
/*  92 */   public void setDocBase(String docBase) { this.docBase = docBase; }
/*     */   
/*     */ 
/*     */ 
/*  96 */   private String name = null;
/*     */   
/*  98 */   public String getName() { return this.name; }
/*     */   
/* 100 */   public void setName(String name) { this.name = name; }
/*     */   
/*     */   private Container parent;
/*     */   public Container getParent()
/*     */   {
/* 105 */     return this.parent; }
/*     */   
/* 107 */   public void setParent(Container parent) { this.parent = parent; }
/*     */   
/*     */ 
/* 110 */   private String path = null;
/*     */   
/* 112 */   public String getPath() { return this.path; }
/*     */   
/* 114 */   public void setPath(String path) { this.path = path; }
/*     */   
/*     */ 
/* 117 */   private String webappVersion = null;
/*     */   
/* 119 */   public String getWebappVersion() { return this.webappVersion; }
/*     */   
/*     */   public void setWebappVersion(String webappVersion) {
/* 122 */     this.webappVersion = webappVersion;
/*     */   }
/*     */   
/*     */ 
/*     */   protected String getDomainInternal()
/*     */   {
/* 128 */     Container p = getParent();
/* 129 */     if (p == null) {
/* 130 */       return null;
/*     */     }
/* 132 */     return p.getDomain();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getMBeanKeyProperties()
/*     */   {
/* 139 */     Container c = this;
/* 140 */     StringBuilder keyProperties = new StringBuilder();
/* 141 */     int containerCount = 0;
/*     */     
/*     */ 
/*     */ 
/* 145 */     while (!(c instanceof Engine)) {
/* 146 */       if ((c instanceof Context)) {
/* 147 */         keyProperties.append(",context=");
/* 148 */         ContextName cn = new ContextName(c.getName(), false);
/* 149 */         keyProperties.append(cn.getDisplayName());
/* 150 */       } else if ((c instanceof Host)) {
/* 151 */         keyProperties.append(",host=");
/* 152 */         keyProperties.append(c.getName());
/* 153 */       } else { if (c == null)
/*     */         {
/* 155 */           keyProperties.append(",container");
/* 156 */           keyProperties.append(containerCount++);
/* 157 */           keyProperties.append("=null");
/* 158 */           break;
/*     */         }
/*     */         
/* 161 */         keyProperties.append(",container");
/* 162 */         keyProperties.append(containerCount++);
/* 163 */         keyProperties.append('=');
/* 164 */         keyProperties.append(c.getName());
/*     */       }
/* 166 */       c = c.getParent();
/*     */     }
/* 168 */     return keyProperties.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getObjectNameKeyProperties()
/*     */   {
/* 175 */     StringBuilder keyProperties = new StringBuilder("j2eeType=WebModule,name=//");
/*     */     
/*     */ 
/* 178 */     String hostname = getParent().getName();
/* 179 */     if (hostname == null) {
/* 180 */       keyProperties.append("DEFAULT");
/*     */     } else {
/* 182 */       keyProperties.append(hostname);
/*     */     }
/*     */     
/* 185 */     String contextName = getName();
/* 186 */     if (!contextName.startsWith("/")) {
/* 187 */       keyProperties.append('/');
/*     */     }
/* 189 */     keyProperties.append(contextName);
/*     */     
/* 191 */     keyProperties.append(",J2EEApplication=none,J2EEServer=none");
/*     */     
/* 193 */     return keyProperties.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   protected void startInternal()
/*     */     throws LifecycleException
/*     */   {
/* 200 */     throw new LifecycleException(sm.getString("failedContext.start", new Object[] {getName() }));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void stopInternal()
/*     */     throws LifecycleException
/*     */   {}
/*     */   
/*     */ 
/*     */   public void addWatchedResource(String name) {}
/*     */   
/*     */ 
/*     */   public String[] findWatchedResources()
/*     */   {
/* 215 */     return new String[0];
/*     */   }
/*     */   
/*     */ 
/*     */   public void removeWatchedResource(String name) {}
/*     */   
/*     */   public void addChild(Container child) {}
/*     */   
/* 223 */   public Container findChild(String name) { return null; }
/*     */   
/* 225 */   public Container[] findChildren() { return new Container[0]; }
/*     */   
/*     */   public void removeChild(Container child) {}
/*     */   
/*     */   public String toString()
/*     */   {
/* 231 */     return getName();
/*     */   }
/*     */   
/*     */   public Loader getLoader()
/*     */   {
/* 236 */     return null;
/*     */   }
/*     */   
/*     */   public void setLoader(Loader loader) {}
/*     */   
/* 241 */   public Log getLogger() { return null; }
/*     */   
/*     */   public String getLogName() {
/* 244 */     return null;
/*     */   }
/*     */   
/* 247 */   public Manager getManager() { return null; }
/*     */   
/*     */   public void setManager(Manager manager) {}
/*     */   
/*     */   public Pipeline getPipeline() {
/* 252 */     return null;
/*     */   }
/*     */   
/* 255 */   public Cluster getCluster() { return null; }
/*     */   
/*     */   public void setCluster(Cluster cluster) {}
/*     */   
/*     */   public int getBackgroundProcessorDelay() {
/* 260 */     return -1;
/*     */   }
/*     */   
/*     */   public void setBackgroundProcessorDelay(int delay) {}
/*     */   
/* 265 */   public ClassLoader getParentClassLoader() { return null; }
/*     */   
/*     */   public void setParentClassLoader(ClassLoader parent) {}
/*     */   
/*     */   public Realm getRealm() {
/* 270 */     return null;
/*     */   }
/*     */   
/*     */   public void setRealm(Realm realm) {}
/*     */   
/* 275 */   public WebResourceRoot getResources() { return null; }
/*     */   
/*     */   public void setResources(WebResourceRoot resources) {}
/*     */   
/*     */   public void backgroundProcess() {}
/*     */   
/*     */   public void addContainerListener(ContainerListener listener) {}
/*     */   
/*     */   public ContainerListener[] findContainerListeners()
/*     */   {
/* 285 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void removeContainerListener(ContainerListener listener) {}
/*     */   
/*     */ 
/*     */   public void addPropertyChangeListener(PropertyChangeListener listener) {}
/*     */   
/*     */   public void removePropertyChangeListener(PropertyChangeListener listener) {}
/*     */   
/*     */   public void fireContainerEvent(String type, Object data) {}
/*     */   
/*     */   public void logAccess(Request request, Response response, long time, boolean useDefault) {}
/*     */   
/*     */   public AccessLog getAccessLog()
/*     */   {
/* 302 */     return null;
/*     */   }
/*     */   
/* 305 */   public int getStartStopThreads() { return 0; }
/*     */   
/*     */   public void setStartStopThreads(int startStopThreads) {}
/*     */   
/*     */   public boolean getAllowCasualMultipartParsing() {
/* 310 */     return false;
/*     */   }
/*     */   
/*     */   public void setAllowCasualMultipartParsing(boolean allowCasualMultipartParsing) {}
/*     */   
/*     */   public Object[] getApplicationEventListeners() {
/* 316 */     return null;
/*     */   }
/*     */   
/*     */   public void setApplicationEventListeners(Object[] listeners) {}
/*     */   
/* 321 */   public Object[] getApplicationLifecycleListeners() { return null; }
/*     */   
/*     */   public void setApplicationLifecycleListeners(Object[] listeners) {}
/*     */   
/*     */   public String getCharset(Locale locale) {
/* 326 */     return null;
/*     */   }
/*     */   
/* 329 */   public boolean getConfigured() { return false; }
/*     */   
/*     */   public void setConfigured(boolean configured) {}
/*     */   
/*     */   public boolean getCookies() {
/* 334 */     return false;
/*     */   }
/*     */   
/*     */   public void setCookies(boolean cookies) {}
/*     */   
/* 339 */   public String getSessionCookieName() { return null; }
/*     */   
/*     */   public void setSessionCookieName(String sessionCookieName) {}
/*     */   
/*     */   public boolean getUseHttpOnly() {
/* 344 */     return false;
/*     */   }
/*     */   
/*     */   public void setUseHttpOnly(boolean useHttpOnly) {}
/*     */   
/* 349 */   public String getSessionCookieDomain() { return null; }
/*     */   
/*     */   public void setSessionCookieDomain(String sessionCookieDomain) {}
/*     */   
/*     */   public String getSessionCookiePath() {
/* 354 */     return null;
/*     */   }
/*     */   
/*     */   public void setSessionCookiePath(String sessionCookiePath) {}
/*     */   
/* 359 */   public boolean getSessionCookiePathUsesTrailingSlash() { return false; }
/*     */   
/*     */   public void setSessionCookiePathUsesTrailingSlash(boolean sessionCookiePathUsesTrailingSlash) {}
/*     */   
/*     */   public boolean getCrossContext()
/*     */   {
/* 365 */     return false;
/*     */   }
/*     */   
/*     */   public void setCrossContext(boolean crossContext) {}
/*     */   
/* 370 */   public String getAltDDName() { return null; }
/*     */   
/*     */   public void setAltDDName(String altDDName) {}
/*     */   
/*     */   public boolean getDenyUncoveredHttpMethods() {
/* 375 */     return false;
/*     */   }
/*     */   
/*     */   public void setDenyUncoveredHttpMethods(boolean denyUncoveredHttpMethods) {}
/*     */   
/*     */   public String getDisplayName()
/*     */   {
/* 382 */     return null;
/*     */   }
/*     */   
/*     */   public void setDisplayName(String displayName) {}
/*     */   
/* 387 */   public boolean getDistributable() { return false; }
/*     */   
/*     */   public void setDistributable(boolean distributable) {}
/*     */   
/*     */   public String getEncodedPath() {
/* 392 */     return null;
/*     */   }
/*     */   
/* 395 */   public boolean getIgnoreAnnotations() { return false; }
/*     */   
/*     */   public void setIgnoreAnnotations(boolean ignoreAnnotations) {}
/*     */   
/*     */   public LoginConfig getLoginConfig() {
/* 400 */     return null;
/*     */   }
/*     */   
/*     */   public void setLoginConfig(LoginConfig config) {}
/*     */   
/* 405 */   public NamingResourcesImpl getNamingResources() { return null; }
/*     */   
/*     */   public void setNamingResources(NamingResourcesImpl namingResources) {}
/*     */   
/*     */   public String getPublicId() {
/* 410 */     return null;
/*     */   }
/*     */   
/*     */   public void setPublicId(String publicId) {}
/*     */   
/* 415 */   public boolean getReloadable() { return false; }
/*     */   
/*     */   public void setReloadable(boolean reloadable) {}
/*     */   
/*     */   public boolean getOverride() {
/* 420 */     return false;
/*     */   }
/*     */   
/*     */   public void setOverride(boolean override) {}
/*     */   
/* 425 */   public boolean getPrivileged() { return false; }
/*     */   
/*     */   public void setPrivileged(boolean privileged) {}
/*     */   
/*     */   public ServletContext getServletContext() {
/* 430 */     return null;
/*     */   }
/*     */   
/* 433 */   public int getSessionTimeout() { return 0; }
/*     */   
/*     */   public void setSessionTimeout(int timeout) {}
/*     */   
/*     */   public boolean getSwallowAbortedUploads() {
/* 438 */     return false;
/*     */   }
/*     */   
/*     */   public void setSwallowAbortedUploads(boolean swallowAbortedUploads) {}
/*     */   
/* 443 */   public boolean getSwallowOutput() { return false; }
/*     */   
/*     */   public void setSwallowOutput(boolean swallowOutput) {}
/*     */   
/*     */   public String getWrapperClass() {
/* 448 */     return null;
/*     */   }
/*     */   
/*     */   public void setWrapperClass(String wrapperClass) {}
/*     */   
/* 453 */   public boolean getXmlNamespaceAware() { return false; }
/*     */   
/*     */   public void setXmlNamespaceAware(boolean xmlNamespaceAware) {}
/*     */   
/*     */   public boolean getXmlValidation() {
/* 458 */     return false;
/*     */   }
/*     */   
/*     */   public void setXmlValidation(boolean xmlValidation) {}
/*     */   
/* 463 */   public boolean getXmlBlockExternal() { return true; }
/*     */   
/*     */   public void setXmlBlockExternal(boolean xmlBlockExternal) {}
/*     */   
/*     */   public boolean getTldValidation() {
/* 468 */     return false;
/*     */   }
/*     */   
/*     */   public void setTldValidation(boolean tldValidation) {}
/*     */   
/* 473 */   public JarScanner getJarScanner() { return null; }
/*     */   
/*     */   public void setJarScanner(JarScanner jarScanner) {}
/*     */   
/*     */   public Authenticator getAuthenticator() {
/* 478 */     return null;
/*     */   }
/*     */   
/*     */   public void setLogEffectiveWebXml(boolean logEffectiveWebXml) {}
/*     */   
/* 483 */   public boolean getLogEffectiveWebXml() { return false; }
/*     */   
/*     */   public void addApplicationListener(String listener) {}
/*     */   
/*     */   public String[] findApplicationListeners() {
/* 488 */     return null;
/*     */   }
/*     */   
/*     */   public void removeApplicationListener(String listener) {}
/*     */   
/*     */   public void addApplicationParameter(ApplicationParameter parameter) {}
/*     */   
/* 495 */   public ApplicationParameter[] findApplicationParameters() { return null; }
/*     */   
/*     */   public void removeApplicationParameter(String name) {}
/*     */   
/*     */   public void addConstraint(SecurityConstraint constraint) {}
/*     */   
/*     */   public SecurityConstraint[] findConstraints() {
/* 502 */     return null;
/*     */   }
/*     */   
/*     */   public void removeConstraint(SecurityConstraint constraint) {}
/*     */   
/*     */   public void addErrorPage(ErrorPage errorPage) {}
/*     */   
/* 509 */   public ErrorPage findErrorPage(int errorCode) { return null; }
/*     */   
/* 511 */   public ErrorPage findErrorPage(String exceptionType) { return null; }
/*     */   
/* 513 */   public ErrorPage[] findErrorPages() { return null; }
/*     */   
/*     */ 
/*     */   public void removeErrorPage(ErrorPage errorPage) {}
/*     */   
/*     */   public void addFilterDef(FilterDef filterDef) {}
/*     */   
/* 520 */   public FilterDef findFilterDef(String filterName) { return null; }
/*     */   
/* 522 */   public FilterDef[] findFilterDefs() { return null; }
/*     */   
/*     */   public void removeFilterDef(FilterDef filterDef) {}
/*     */   
/*     */   public void addFilterMap(FilterMap filterMap) {}
/*     */   
/*     */   public void addFilterMapBefore(FilterMap filterMap) {}
/*     */   
/*     */   public FilterMap[] findFilterMaps() {
/* 531 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void removeFilterMap(FilterMap filterMap) {}
/*     */   
/*     */   public void addLocaleEncodingMappingParameter(String locale, String encoding) {}
/*     */   
/*     */   public void addMimeMapping(String extension, String mimeType) {}
/*     */   
/* 541 */   public String findMimeMapping(String extension) { return null; }
/*     */   
/* 543 */   public String[] findMimeMappings() { return null; }
/*     */   
/*     */ 
/*     */   public void removeMimeMapping(String extension) {}
/*     */   
/*     */   public void addParameter(String name, String value) {}
/*     */   
/* 550 */   public String findParameter(String name) { return null; }
/*     */   
/* 552 */   public String[] findParameters() { return null; }
/*     */   
/*     */   public void removeParameter(String name) {}
/*     */   
/*     */   public void addRoleMapping(String role, String link) {}
/*     */   
/*     */   public String findRoleMapping(String role) {
/* 559 */     return null;
/*     */   }
/*     */   
/*     */   public void removeRoleMapping(String role) {}
/*     */   
/*     */   public void addSecurityRole(String role) {}
/*     */   
/* 566 */   public boolean findSecurityRole(String role) { return false; }
/*     */   
/* 568 */   public String[] findSecurityRoles() { return null; }
/*     */   
/*     */ 
/*     */ 
/*     */   public void removeSecurityRole(String role) {}
/*     */   
/*     */ 
/*     */   public void addServletMapping(String pattern, String name) {}
/*     */   
/*     */   public void addServletMapping(String pattern, String name, boolean jspWildcard) {}
/*     */   
/*     */   public void addServletMappingDecoded(String pattern, String name) {}
/*     */   
/*     */   public void addServletMappingDecoded(String pattern, String name, boolean jspWildcard) {}
/*     */   
/* 583 */   public String findServletMapping(String pattern) { return null; }
/*     */   
/* 585 */   public String[] findServletMappings() { return null; }
/*     */   
/*     */ 
/*     */   public void removeServletMapping(String pattern) {}
/*     */   
/*     */   public void addWelcomeFile(String name) {}
/*     */   
/* 592 */   public boolean findWelcomeFile(String name) { return false; }
/*     */   
/* 594 */   public String[] findWelcomeFiles() { return null; }
/*     */   
/*     */   public void removeWelcomeFile(String name) {}
/*     */   
/*     */   public void addWrapperLifecycle(String listener) {}
/*     */   
/*     */   public String[] findWrapperLifecycles() {
/* 601 */     return null;
/*     */   }
/*     */   
/*     */   public void removeWrapperLifecycle(String listener) {}
/*     */   
/*     */   public void addWrapperListener(String listener) {}
/*     */   
/* 608 */   public String[] findWrapperListeners() { return null; }
/*     */   
/*     */   public void removeWrapperListener(String listener) {}
/*     */   
/*     */   public Wrapper createWrapper() {
/* 613 */     return null;
/*     */   }
/*     */   
/* 616 */   public String findStatusPage(int status) { return null; }
/*     */   
/* 618 */   public int[] findStatusPages() { return null; }
/*     */   
/*     */ 
/* 621 */   public boolean fireRequestInitEvent(ServletRequest request) { return false; }
/*     */   
/* 623 */   public boolean fireRequestDestroyEvent(ServletRequest request) { return false; }
/*     */   
/*     */   public void reload() {}
/*     */   
/*     */   public String getRealPath(String path)
/*     */   {
/* 629 */     return null;
/*     */   }
/*     */   
/* 632 */   public int getEffectiveMajorVersion() { return 0; }
/*     */   
/*     */   public void setEffectiveMajorVersion(int major) {}
/*     */   
/*     */   public int getEffectiveMinorVersion() {
/* 637 */     return 0;
/*     */   }
/*     */   
/*     */   public void setEffectiveMinorVersion(int minor) {}
/*     */   
/* 642 */   public JspConfigDescriptor getJspConfigDescriptor() { return null; }
/*     */   
/*     */ 
/*     */   public void setJspConfigDescriptor(JspConfigDescriptor descriptor) {}
/*     */   
/*     */ 
/*     */   public void addServletContainerInitializer(ServletContainerInitializer sci, Set<Class<?>> classes) {}
/*     */   
/*     */   public boolean getPaused()
/*     */   {
/* 652 */     return false;
/*     */   }
/*     */   
/* 655 */   public boolean isServlet22() { return false; }
/*     */   
/*     */ 
/*     */   public Set<String> addServletSecurity(ServletRegistration.Dynamic registration, ServletSecurityElement servletSecurityElement)
/*     */   {
/* 660 */     return null;
/*     */   }
/*     */   
/*     */   public void setResourceOnlyServlets(String resourceOnlyServlets) {}
/*     */   
/* 665 */   public String getResourceOnlyServlets() { return null; }
/*     */   
/* 667 */   public boolean isResourceOnlyServlet(String servletName) { return false; }
/*     */   
/*     */   public String getBaseName() {
/* 670 */     return null;
/*     */   }
/*     */   
/*     */   public void setFireRequestListenersOnForwards(boolean enable) {}
/*     */   
/* 675 */   public boolean getFireRequestListenersOnForwards() { return false; }
/*     */   
/*     */   public void setPreemptiveAuthentication(boolean enable) {}
/*     */   
/*     */   public boolean getPreemptiveAuthentication() {
/* 680 */     return false;
/*     */   }
/*     */   
/*     */   public void setSendRedirectBody(boolean enable) {}
/*     */   
/* 685 */   public boolean getSendRedirectBody() { return false; }
/*     */   
/*     */   public synchronized void addValve(Valve valve) {}
/*     */   
/*     */   public File getCatalinaBase()
/*     */   {
/* 691 */     return null;
/*     */   }
/*     */   
/* 694 */   public File getCatalinaHome() { return null; }
/*     */   
/*     */ 
/*     */   public void setAddWebinfClassesResources(boolean addWebinfClassesResources) {}
/*     */   
/*     */   public boolean getAddWebinfClassesResources()
/*     */   {
/* 701 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public void addPostConstructMethod(String clazz, String method) {}
/*     */   
/*     */ 
/*     */   public void addPreDestroyMethod(String clazz, String method) {}
/*     */   
/*     */   public void removePostConstructMethod(String clazz) {}
/*     */   
/*     */   public void removePreDestroyMethod(String clazz) {}
/*     */   
/*     */   public String findPostConstructMethod(String clazz)
/*     */   {
/* 716 */     return null;
/*     */   }
/*     */   
/* 719 */   public String findPreDestroyMethod(String clazz) { return null; }
/*     */   
/*     */   public Map<String, String> findPostConstructMethods() {
/* 722 */     return null;
/*     */   }
/*     */   
/* 725 */   public Map<String, String> findPreDestroyMethods() { return null; }
/*     */   
/*     */   public InstanceManager getInstanceManager() {
/* 728 */     return null;
/*     */   }
/*     */   
/*     */   public void setInstanceManager(InstanceManager instanceManager) {}
/*     */   
/*     */   public void setContainerSciFilter(String containerSciFilter) {}
/*     */   
/*     */   public String getContainerSciFilter()
/*     */   {
/* 737 */     return null;
/*     */   }
/*     */   
/* 740 */   public ThreadBindingListener getThreadBindingListener() { return null; }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setThreadBindingListener(ThreadBindingListener threadBindingListener) {}
/*     */   
/*     */ 
/*     */   public ClassLoader bind(boolean usePrivilegedAction, ClassLoader originalClassLoader)
/*     */   {
/* 749 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void unbind(boolean usePrivilegedAction, ClassLoader originalClassLoader) {}
/*     */   
/*     */ 
/*     */   public Object getNamingToken()
/*     */   {
/* 758 */     return null;
/*     */   }
/*     */   
/*     */   public void setCookieProcessor(CookieProcessor cookieProcessor) {}
/*     */   
/*     */   public CookieProcessor getCookieProcessor() {
/* 764 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setValidateClientProvidedNewSessionId(boolean validateClientProvidedNewSessionId) {}
/*     */   
/*     */   public boolean getValidateClientProvidedNewSessionId()
/*     */   {
/* 772 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setMapperContextRootRedirectEnabled(boolean mapperContextRootRedirectEnabled) {}
/*     */   
/*     */   public boolean getMapperContextRootRedirectEnabled()
/*     */   {
/* 780 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setMapperDirectoryRedirectEnabled(boolean mapperDirectoryRedirectEnabled) {}
/*     */   
/*     */   public boolean getMapperDirectoryRedirectEnabled()
/*     */   {
/* 788 */     return false;
/*     */   }
/*     */   
/*     */   public void setUseRelativeRedirects(boolean useRelativeRedirects) {}
/*     */   
/* 793 */   public boolean getUseRelativeRedirects() { return true; }
/*     */   
/*     */   public void setDispatchersUseEncodedPaths(boolean dispatchersUseEncodedPaths) {}
/*     */   
/*     */   public boolean getDispatchersUseEncodedPaths() {
/* 798 */     return true;
/*     */   }
/*     */   
/*     */   public void setRequestCharacterEncoding(String encoding) {}
/*     */   
/* 803 */   public String getRequestCharacterEncoding() { return null; }
/*     */   
/*     */   public void setResponseCharacterEncoding(String encoding) {}
/*     */   
/*     */   public String getResponseCharacterEncoding() {
/* 808 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\FailedContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */