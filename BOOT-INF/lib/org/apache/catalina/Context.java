package org.apache.catalina;

import java.net.URL;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.ServletRequest;
import javax.servlet.ServletSecurityElement;
import javax.servlet.descriptor.JspConfigDescriptor;
import org.apache.catalina.deploy.NamingResourcesImpl;
import org.apache.tomcat.ContextBind;
import org.apache.tomcat.InstanceManager;
import org.apache.tomcat.JarScanner;
import org.apache.tomcat.util.descriptor.web.ApplicationParameter;
import org.apache.tomcat.util.descriptor.web.ErrorPage;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.apache.tomcat.util.descriptor.web.LoginConfig;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.apache.tomcat.util.http.CookieProcessor;

public abstract interface Context
  extends Container, ContextBind
{
  public static final String ADD_WELCOME_FILE_EVENT = "addWelcomeFile";
  public static final String REMOVE_WELCOME_FILE_EVENT = "removeWelcomeFile";
  public static final String CLEAR_WELCOME_FILES_EVENT = "clearWelcomeFiles";
  public static final String CHANGE_SESSION_ID_EVENT = "changeSessionId";
  
  public abstract boolean getAllowCasualMultipartParsing();
  
  public abstract void setAllowCasualMultipartParsing(boolean paramBoolean);
  
  public abstract Object[] getApplicationEventListeners();
  
  public abstract void setApplicationEventListeners(Object[] paramArrayOfObject);
  
  public abstract Object[] getApplicationLifecycleListeners();
  
  public abstract void setApplicationLifecycleListeners(Object[] paramArrayOfObject);
  
  public abstract String getCharset(Locale paramLocale);
  
  public abstract URL getConfigFile();
  
  public abstract void setConfigFile(URL paramURL);
  
  public abstract boolean getConfigured();
  
  public abstract void setConfigured(boolean paramBoolean);
  
  public abstract boolean getCookies();
  
  public abstract void setCookies(boolean paramBoolean);
  
  public abstract String getSessionCookieName();
  
  public abstract void setSessionCookieName(String paramString);
  
  public abstract boolean getUseHttpOnly();
  
  public abstract void setUseHttpOnly(boolean paramBoolean);
  
  public abstract String getSessionCookieDomain();
  
  public abstract void setSessionCookieDomain(String paramString);
  
  public abstract String getSessionCookiePath();
  
  public abstract void setSessionCookiePath(String paramString);
  
  public abstract boolean getSessionCookiePathUsesTrailingSlash();
  
  public abstract void setSessionCookiePathUsesTrailingSlash(boolean paramBoolean);
  
  public abstract boolean getCrossContext();
  
  public abstract String getAltDDName();
  
  public abstract void setAltDDName(String paramString);
  
  public abstract void setCrossContext(boolean paramBoolean);
  
  public abstract boolean getDenyUncoveredHttpMethods();
  
  public abstract void setDenyUncoveredHttpMethods(boolean paramBoolean);
  
  public abstract String getDisplayName();
  
  public abstract void setDisplayName(String paramString);
  
  public abstract boolean getDistributable();
  
  public abstract void setDistributable(boolean paramBoolean);
  
  public abstract String getDocBase();
  
  public abstract void setDocBase(String paramString);
  
  public abstract String getEncodedPath();
  
  public abstract boolean getIgnoreAnnotations();
  
  public abstract void setIgnoreAnnotations(boolean paramBoolean);
  
  public abstract LoginConfig getLoginConfig();
  
  public abstract void setLoginConfig(LoginConfig paramLoginConfig);
  
  public abstract NamingResourcesImpl getNamingResources();
  
  public abstract void setNamingResources(NamingResourcesImpl paramNamingResourcesImpl);
  
  public abstract String getPath();
  
  public abstract void setPath(String paramString);
  
  public abstract String getPublicId();
  
  public abstract void setPublicId(String paramString);
  
  public abstract boolean getReloadable();
  
  public abstract void setReloadable(boolean paramBoolean);
  
  public abstract boolean getOverride();
  
  public abstract void setOverride(boolean paramBoolean);
  
  public abstract boolean getPrivileged();
  
  public abstract void setPrivileged(boolean paramBoolean);
  
  public abstract ServletContext getServletContext();
  
  public abstract int getSessionTimeout();
  
  public abstract void setSessionTimeout(int paramInt);
  
  public abstract boolean getSwallowAbortedUploads();
  
  public abstract void setSwallowAbortedUploads(boolean paramBoolean);
  
  public abstract boolean getSwallowOutput();
  
  public abstract void setSwallowOutput(boolean paramBoolean);
  
  public abstract String getWrapperClass();
  
  public abstract void setWrapperClass(String paramString);
  
  public abstract boolean getXmlNamespaceAware();
  
  public abstract void setXmlNamespaceAware(boolean paramBoolean);
  
  public abstract boolean getXmlValidation();
  
  public abstract void setXmlValidation(boolean paramBoolean);
  
  public abstract boolean getXmlBlockExternal();
  
  public abstract void setXmlBlockExternal(boolean paramBoolean);
  
  public abstract boolean getTldValidation();
  
  public abstract void setTldValidation(boolean paramBoolean);
  
  public abstract JarScanner getJarScanner();
  
  public abstract void setJarScanner(JarScanner paramJarScanner);
  
  public abstract Authenticator getAuthenticator();
  
  public abstract void setLogEffectiveWebXml(boolean paramBoolean);
  
  public abstract boolean getLogEffectiveWebXml();
  
  public abstract InstanceManager getInstanceManager();
  
  public abstract void setInstanceManager(InstanceManager paramInstanceManager);
  
  public abstract void setContainerSciFilter(String paramString);
  
  public abstract String getContainerSciFilter();
  
  public abstract void addApplicationListener(String paramString);
  
  public abstract void addApplicationParameter(ApplicationParameter paramApplicationParameter);
  
  public abstract void addConstraint(SecurityConstraint paramSecurityConstraint);
  
  public abstract void addErrorPage(ErrorPage paramErrorPage);
  
  public abstract void addFilterDef(FilterDef paramFilterDef);
  
  public abstract void addFilterMap(FilterMap paramFilterMap);
  
  public abstract void addFilterMapBefore(FilterMap paramFilterMap);
  
  public abstract void addLocaleEncodingMappingParameter(String paramString1, String paramString2);
  
  public abstract void addMimeMapping(String paramString1, String paramString2);
  
  public abstract void addParameter(String paramString1, String paramString2);
  
  public abstract void addRoleMapping(String paramString1, String paramString2);
  
  public abstract void addSecurityRole(String paramString);
  
  @Deprecated
  public abstract void addServletMapping(String paramString1, String paramString2);
  
  @Deprecated
  public abstract void addServletMapping(String paramString1, String paramString2, boolean paramBoolean);
  
  public abstract void addServletMappingDecoded(String paramString1, String paramString2);
  
  public abstract void addServletMappingDecoded(String paramString1, String paramString2, boolean paramBoolean);
  
  public abstract void addWatchedResource(String paramString);
  
  public abstract void addWelcomeFile(String paramString);
  
  public abstract void addWrapperLifecycle(String paramString);
  
  public abstract void addWrapperListener(String paramString);
  
  public abstract Wrapper createWrapper();
  
  public abstract String[] findApplicationListeners();
  
  public abstract ApplicationParameter[] findApplicationParameters();
  
  public abstract SecurityConstraint[] findConstraints();
  
  public abstract ErrorPage findErrorPage(int paramInt);
  
  public abstract ErrorPage findErrorPage(String paramString);
  
  public abstract ErrorPage[] findErrorPages();
  
  public abstract FilterDef findFilterDef(String paramString);
  
  public abstract FilterDef[] findFilterDefs();
  
  public abstract FilterMap[] findFilterMaps();
  
  public abstract String findMimeMapping(String paramString);
  
  public abstract String[] findMimeMappings();
  
  public abstract String findParameter(String paramString);
  
  public abstract String[] findParameters();
  
  public abstract String findRoleMapping(String paramString);
  
  public abstract boolean findSecurityRole(String paramString);
  
  public abstract String[] findSecurityRoles();
  
  public abstract String findServletMapping(String paramString);
  
  public abstract String[] findServletMappings();
  
  public abstract String findStatusPage(int paramInt);
  
  public abstract int[] findStatusPages();
  
  public abstract ThreadBindingListener getThreadBindingListener();
  
  public abstract void setThreadBindingListener(ThreadBindingListener paramThreadBindingListener);
  
  public abstract String[] findWatchedResources();
  
  public abstract boolean findWelcomeFile(String paramString);
  
  public abstract String[] findWelcomeFiles();
  
  public abstract String[] findWrapperLifecycles();
  
  public abstract String[] findWrapperListeners();
  
  public abstract boolean fireRequestInitEvent(ServletRequest paramServletRequest);
  
  public abstract boolean fireRequestDestroyEvent(ServletRequest paramServletRequest);
  
  public abstract void reload();
  
  public abstract void removeApplicationListener(String paramString);
  
  public abstract void removeApplicationParameter(String paramString);
  
  public abstract void removeConstraint(SecurityConstraint paramSecurityConstraint);
  
  public abstract void removeErrorPage(ErrorPage paramErrorPage);
  
  public abstract void removeFilterDef(FilterDef paramFilterDef);
  
  public abstract void removeFilterMap(FilterMap paramFilterMap);
  
  public abstract void removeMimeMapping(String paramString);
  
  public abstract void removeParameter(String paramString);
  
  public abstract void removeRoleMapping(String paramString);
  
  public abstract void removeSecurityRole(String paramString);
  
  public abstract void removeServletMapping(String paramString);
  
  public abstract void removeWatchedResource(String paramString);
  
  public abstract void removeWelcomeFile(String paramString);
  
  public abstract void removeWrapperLifecycle(String paramString);
  
  public abstract void removeWrapperListener(String paramString);
  
  public abstract String getRealPath(String paramString);
  
  public abstract int getEffectiveMajorVersion();
  
  public abstract void setEffectiveMajorVersion(int paramInt);
  
  public abstract int getEffectiveMinorVersion();
  
  public abstract void setEffectiveMinorVersion(int paramInt);
  
  public abstract JspConfigDescriptor getJspConfigDescriptor();
  
  public abstract void setJspConfigDescriptor(JspConfigDescriptor paramJspConfigDescriptor);
  
  public abstract void addServletContainerInitializer(ServletContainerInitializer paramServletContainerInitializer, Set<Class<?>> paramSet);
  
  public abstract boolean getPaused();
  
  public abstract boolean isServlet22();
  
  public abstract Set<String> addServletSecurity(ServletRegistration.Dynamic paramDynamic, ServletSecurityElement paramServletSecurityElement);
  
  public abstract void setResourceOnlyServlets(String paramString);
  
  public abstract String getResourceOnlyServlets();
  
  public abstract boolean isResourceOnlyServlet(String paramString);
  
  public abstract String getBaseName();
  
  public abstract void setWebappVersion(String paramString);
  
  public abstract String getWebappVersion();
  
  public abstract void setFireRequestListenersOnForwards(boolean paramBoolean);
  
  public abstract boolean getFireRequestListenersOnForwards();
  
  public abstract void setPreemptiveAuthentication(boolean paramBoolean);
  
  public abstract boolean getPreemptiveAuthentication();
  
  public abstract void setSendRedirectBody(boolean paramBoolean);
  
  public abstract boolean getSendRedirectBody();
  
  public abstract Loader getLoader();
  
  public abstract void setLoader(Loader paramLoader);
  
  public abstract WebResourceRoot getResources();
  
  public abstract void setResources(WebResourceRoot paramWebResourceRoot);
  
  public abstract Manager getManager();
  
  public abstract void setManager(Manager paramManager);
  
  public abstract void setAddWebinfClassesResources(boolean paramBoolean);
  
  public abstract boolean getAddWebinfClassesResources();
  
  public abstract void addPostConstructMethod(String paramString1, String paramString2);
  
  public abstract void addPreDestroyMethod(String paramString1, String paramString2);
  
  public abstract void removePostConstructMethod(String paramString);
  
  public abstract void removePreDestroyMethod(String paramString);
  
  public abstract String findPostConstructMethod(String paramString);
  
  public abstract String findPreDestroyMethod(String paramString);
  
  public abstract Map<String, String> findPostConstructMethods();
  
  public abstract Map<String, String> findPreDestroyMethods();
  
  public abstract Object getNamingToken();
  
  public abstract void setCookieProcessor(CookieProcessor paramCookieProcessor);
  
  public abstract CookieProcessor getCookieProcessor();
  
  public abstract void setValidateClientProvidedNewSessionId(boolean paramBoolean);
  
  public abstract boolean getValidateClientProvidedNewSessionId();
  
  public abstract void setMapperContextRootRedirectEnabled(boolean paramBoolean);
  
  public abstract boolean getMapperContextRootRedirectEnabled();
  
  public abstract void setMapperDirectoryRedirectEnabled(boolean paramBoolean);
  
  public abstract boolean getMapperDirectoryRedirectEnabled();
  
  public abstract void setUseRelativeRedirects(boolean paramBoolean);
  
  public abstract boolean getUseRelativeRedirects();
  
  public abstract void setDispatchersUseEncodedPaths(boolean paramBoolean);
  
  public abstract boolean getDispatchersUseEncodedPaths();
  
  public abstract void setRequestCharacterEncoding(String paramString);
  
  public abstract String getRequestCharacterEncoding();
  
  public abstract void setResponseCharacterEncoding(String paramString);
  
  public abstract String getResponseCharacterEncoding();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\Context.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */