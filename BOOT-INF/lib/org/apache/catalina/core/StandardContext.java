/*      */ package org.apache.catalina.core;
/*      */ 
/*      */ import java.beans.PropertyChangeSupport;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Enumeration;
/*      */ import java.util.EventListener;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.Stack;
/*      */ import java.util.TreeMap;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.concurrent.CopyOnWriteArrayList;
/*      */ import java.util.concurrent.atomic.AtomicLong;
/*      */ import java.util.concurrent.locks.Lock;
/*      */ import java.util.concurrent.locks.ReadWriteLock;
/*      */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*      */ import javax.management.ListenerNotFoundException;
/*      */ import javax.management.MBeanNotificationInfo;
/*      */ import javax.management.Notification;
/*      */ import javax.management.NotificationBroadcasterSupport;
/*      */ import javax.management.NotificationEmitter;
/*      */ import javax.management.NotificationFilter;
/*      */ import javax.management.NotificationListener;
/*      */ import javax.naming.NamingException;
/*      */ import javax.servlet.Filter;
/*      */ import javax.servlet.FilterConfig;
/*      */ import javax.servlet.FilterRegistration;
/*      */ import javax.servlet.FilterRegistration.Dynamic;
/*      */ import javax.servlet.RequestDispatcher;
/*      */ import javax.servlet.Servlet;
/*      */ import javax.servlet.ServletContainerInitializer;
/*      */ import javax.servlet.ServletContextAttributeListener;
/*      */ import javax.servlet.ServletContextEvent;
/*      */ import javax.servlet.ServletContextListener;
/*      */ import javax.servlet.ServletException;
/*      */ import javax.servlet.ServletRegistration;
/*      */ import javax.servlet.ServletRegistration.Dynamic;
/*      */ import javax.servlet.ServletRequest;
/*      */ import javax.servlet.ServletRequestAttributeListener;
/*      */ import javax.servlet.ServletRequestEvent;
/*      */ import javax.servlet.ServletRequestListener;
/*      */ import javax.servlet.ServletSecurityElement;
/*      */ import javax.servlet.SessionCookieConfig;
/*      */ import javax.servlet.SessionTrackingMode;
/*      */ import javax.servlet.descriptor.JspConfigDescriptor;
/*      */ import javax.servlet.http.HttpSessionAttributeListener;
/*      */ import javax.servlet.http.HttpSessionIdListener;
/*      */ import javax.servlet.http.HttpSessionListener;
/*      */ import org.apache.catalina.Authenticator;
/*      */ import org.apache.catalina.Cluster;
/*      */ import org.apache.catalina.Container;
/*      */ import org.apache.catalina.ContainerListener;
/*      */ import org.apache.catalina.CredentialHandler;
/*      */ import org.apache.catalina.Globals;
/*      */ import org.apache.catalina.Lifecycle;
/*      */ import org.apache.catalina.LifecycleException;
/*      */ import org.apache.catalina.LifecycleListener;
/*      */ import org.apache.catalina.LifecycleState;
/*      */ import org.apache.catalina.Loader;
/*      */ import org.apache.catalina.Manager;
/*      */ import org.apache.catalina.Pipeline;
/*      */ import org.apache.catalina.Realm;
/*      */ import org.apache.catalina.ThreadBindingListener;
/*      */ import org.apache.catalina.Valve;
/*      */ import org.apache.catalina.WebResource;
/*      */ import org.apache.catalina.WebResourceRoot;
/*      */ import org.apache.catalina.WebResourceRoot.ResourceSetType;
/*      */ import org.apache.catalina.Wrapper;
/*      */ import org.apache.catalina.deploy.NamingResourcesImpl;
/*      */ import org.apache.catalina.loader.WebappLoader;
/*      */ import org.apache.catalina.session.StandardManager;
/*      */ import org.apache.catalina.util.CharsetMapper;
/*      */ import org.apache.catalina.util.ContextName;
/*      */ import org.apache.catalina.util.ExtensionValidator;
/*      */ import org.apache.catalina.util.URLEncoder;
/*      */ import org.apache.catalina.webresources.StandardRoot;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.naming.ContextBindings;
/*      */ import org.apache.tomcat.InstanceManager;
/*      */ import org.apache.tomcat.InstanceManagerBindings;
/*      */ import org.apache.tomcat.JarScanner;
/*      */ import org.apache.tomcat.util.ExceptionUtils;
/*      */ import org.apache.tomcat.util.IntrospectionUtils;
/*      */ import org.apache.tomcat.util.buf.StringUtils;
/*      */ import org.apache.tomcat.util.buf.UDecoder;
/*      */ import org.apache.tomcat.util.descriptor.web.ApplicationParameter;
/*      */ import org.apache.tomcat.util.descriptor.web.ErrorPage;
/*      */ import org.apache.tomcat.util.descriptor.web.FilterDef;
/*      */ import org.apache.tomcat.util.descriptor.web.FilterMap;
/*      */ import org.apache.tomcat.util.descriptor.web.Injectable;
/*      */ import org.apache.tomcat.util.descriptor.web.InjectionTarget;
/*      */ import org.apache.tomcat.util.descriptor.web.LoginConfig;
/*      */ import org.apache.tomcat.util.descriptor.web.MessageDestination;
/*      */ import org.apache.tomcat.util.descriptor.web.MessageDestinationRef;
/*      */ import org.apache.tomcat.util.descriptor.web.SecurityCollection;
/*      */ import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
/*      */ import org.apache.tomcat.util.http.CookieProcessor;
/*      */ import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
/*      */ import org.apache.tomcat.util.res.StringManager;
/*      */ import org.apache.tomcat.util.scan.StandardJarScanner;
/*      */ import org.apache.tomcat.util.security.PrivilegedGetTccl;
/*      */ import org.apache.tomcat.util.security.PrivilegedSetTccl;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class StandardContext
/*      */   extends ContainerBase
/*      */   implements org.apache.catalina.Context, NotificationEmitter
/*      */ {
/*  150 */   private static final Log log = LogFactory.getLog(StandardContext.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public StandardContext()
/*      */   {
/*  162 */     this.pipeline.setBasic(new StandardContextValve());
/*  163 */     this.broadcaster = new NotificationBroadcasterSupport();
/*      */     
/*  165 */     if (!Globals.STRICT_SERVLET_COMPLIANCE)
/*      */     {
/*      */ 
/*  168 */       this.resourceOnlyServlets.add("jsp");
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
/*  180 */   protected boolean allowCasualMultipartParsing = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  186 */   private boolean swallowAbortedUploads = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  191 */   private String altDDName = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  197 */   private InstanceManager instanceManager = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  203 */   private boolean antiResourceLocking = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  211 */   private String[] applicationListeners = new String[0];
/*      */   
/*  213 */   private final Object applicationListenersLock = new Object();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  219 */   private final Set<Object> noPluggabilityListeners = new HashSet();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  226 */   private List<Object> applicationEventListenersList = new CopyOnWriteArrayList();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  234 */   private Object[] applicationLifecycleListenersObjects = new Object[0];
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  241 */   private Map<ServletContainerInitializer, Set<Class<?>>> initializers = new LinkedHashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  248 */   private ApplicationParameter[] applicationParameters = new ApplicationParameter[0];
/*      */   
/*      */ 
/*  251 */   private final Object applicationParametersLock = new Object();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  257 */   private NotificationBroadcasterSupport broadcaster = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  262 */   private CharsetMapper charsetMapper = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  268 */   private String charsetMapperClass = "org.apache.catalina.util.CharsetMapper";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  275 */   private URL configFile = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  281 */   private boolean configured = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  287 */   private volatile SecurityConstraint[] constraints = new SecurityConstraint[0];
/*      */   
/*      */ 
/*  290 */   private final Object constraintsLock = new Object();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  296 */   protected ApplicationContext context = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  303 */   private NoPluggabilityServletContext noPluggabilityServletContext = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  309 */   private boolean cookies = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  316 */   private boolean crossContext = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  322 */   private String encodedPath = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  328 */   private String path = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  335 */   private boolean delegate = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean denyUncoveredHttpMethods;
/*      */   
/*      */ 
/*      */ 
/*  344 */   private String displayName = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String defaultContextXml;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String defaultWebXml;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  362 */   private boolean distributable = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  368 */   private String docBase = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  375 */   private HashMap<String, ErrorPage> exceptionPages = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  382 */   private HashMap<String, ApplicationFilterConfig> filterConfigs = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  390 */   private HashMap<String, FilterDef> filterDefs = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  399 */   private final ContextFilterMaps filterMaps = new ContextFilterMaps(null);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  404 */   private boolean ignoreAnnotations = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  410 */   private Loader loader = null;
/*  411 */   private final ReadWriteLock loaderLock = new ReentrantReadWriteLock();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  417 */   private LoginConfig loginConfig = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  423 */   protected Manager manager = null;
/*  424 */   private final ReadWriteLock managerLock = new ReentrantReadWriteLock();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  430 */   private NamingContextListener namingContextListener = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  436 */   private NamingResourcesImpl namingResources = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  441 */   private HashMap<String, MessageDestination> messageDestinations = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  448 */   private HashMap<String, String> mimeMappings = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  455 */   private final ConcurrentMap<String, String> parameters = new ConcurrentHashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  461 */   private volatile boolean paused = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  469 */   private String publicId = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  475 */   private boolean reloadable = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  481 */   private boolean unpackWAR = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  487 */   private boolean copyXML = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  493 */   private boolean override = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  499 */   private String originalDocBase = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  505 */   private boolean privileged = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  515 */   private boolean replaceWelcomeFiles = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  522 */   private HashMap<String, String> roleMappings = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  528 */   private String[] securityRoles = new String[0];
/*      */   
/*  530 */   private final Object securityRolesLock = new Object();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  537 */   private HashMap<String, String> servletMappings = new HashMap();
/*      */   
/*  539 */   private final Object servletMappingsLock = new Object();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  545 */   private int sessionTimeout = 30;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  550 */   private AtomicLong sequenceNumber = new AtomicLong(0L);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  557 */   private HashMap<Integer, ErrorPage> statusPages = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  564 */   private boolean swallowOutput = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  570 */   private long unloadDelay = 2000L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  576 */   private String[] watchedResources = new String[0];
/*      */   
/*  578 */   private final Object watchedResourcesLock = new Object();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  584 */   private String[] welcomeFiles = new String[0];
/*      */   
/*  586 */   private final Object welcomeFilesLock = new Object();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  593 */   private String[] wrapperLifecycles = new String[0];
/*      */   
/*  595 */   private final Object wrapperLifecyclesLock = new Object();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  601 */   private String[] wrapperListeners = new String[0];
/*      */   
/*  603 */   private final Object wrapperListenersLock = new Object();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  609 */   private String workDir = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  615 */   private String wrapperClassName = StandardWrapper.class.getName();
/*  616 */   private Class<?> wrapperClass = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  622 */   private boolean useNaming = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  628 */   private String namingContextName = null;
/*      */   
/*      */   private WebResourceRoot resources;
/*      */   
/*  632 */   private final ReadWriteLock resourcesLock = new ReentrantReadWriteLock();
/*      */   
/*      */ 
/*      */   private long startupTime;
/*      */   
/*      */   private long startTime;
/*      */   
/*      */   private long tldScanTime;
/*      */   
/*  641 */   private String j2EEApplication = "none";
/*  642 */   private String j2EEServer = "none";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  649 */   private boolean webXmlValidation = Globals.STRICT_SERVLET_COMPLIANCE;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  655 */   private boolean webXmlNamespaceAware = Globals.STRICT_SERVLET_COMPLIANCE;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  661 */   private boolean xmlBlockExternal = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  667 */   private boolean tldValidation = Globals.STRICT_SERVLET_COMPLIANCE;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String sessionCookieName;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  680 */   private boolean useHttpOnly = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String sessionCookieDomain;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String sessionCookiePath;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  702 */   private boolean sessionCookiePathUsesTrailingSlash = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  709 */   private JarScanner jarScanner = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  716 */   private boolean clearReferencesRmiTargets = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  727 */   private boolean clearReferencesStopThreads = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  734 */   private boolean clearReferencesStopTimerThreads = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  744 */   private boolean clearReferencesHttpClientKeepAliveThread = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  752 */   private boolean renewThreadsWhenStoppingContext = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  757 */   private boolean logEffectiveWebXml = false;
/*      */   
/*  759 */   private int effectiveMajorVersion = 3;
/*      */   
/*  761 */   private int effectiveMinorVersion = 0;
/*      */   
/*  763 */   private JspConfigDescriptor jspConfigDescriptor = null;
/*      */   
/*  765 */   private Set<String> resourceOnlyServlets = new HashSet();
/*      */   
/*  767 */   private String webappVersion = "";
/*      */   
/*  769 */   private boolean addWebinfClassesResources = false;
/*      */   
/*  771 */   private boolean fireRequestListenersOnForwards = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  777 */   private Set<Servlet> createdServlets = new HashSet();
/*      */   
/*  779 */   private boolean preemptiveAuthentication = false;
/*      */   
/*  781 */   private boolean sendRedirectBody = false;
/*      */   
/*  783 */   private boolean jndiExceptionOnFailedWrite = true;
/*      */   
/*  785 */   private Map<String, String> postConstructMethods = new HashMap();
/*  786 */   private Map<String, String> preDestroyMethods = new HashMap();
/*      */   
/*      */   private String containerSciFilter;
/*      */   
/*      */   private Boolean failCtxIfServletStartFails;
/*      */   
/*  792 */   protected static final ThreadBindingListener DEFAULT_NAMING_LISTENER = new ThreadBindingListener()
/*      */   {
/*      */     public void bind() {}
/*      */     
/*      */     public void unbind() {}
/*      */   };
/*  798 */   protected ThreadBindingListener threadBindingListener = DEFAULT_NAMING_LISTENER;
/*      */   
/*  800 */   private final Object namingToken = new Object();
/*      */   
/*      */   private CookieProcessor cookieProcessor;
/*      */   
/*  804 */   private boolean validateClientProvidedNewSessionId = true;
/*      */   
/*  806 */   private boolean mapperContextRootRedirectEnabled = true;
/*      */   
/*  808 */   private boolean mapperDirectoryRedirectEnabled = false;
/*      */   
/*  810 */   private boolean useRelativeRedirects = !Globals.STRICT_SERVLET_COMPLIANCE;
/*      */   
/*  812 */   private boolean dispatchersUseEncodedPaths = true;
/*      */   
/*  814 */   private String requestEncoding = null;
/*      */   
/*  816 */   private String responseEncoding = null;
/*      */   
/*      */   private MBeanNotificationInfo[] notificationInfo;
/*      */   
/*      */   public String getRequestCharacterEncoding()
/*      */   {
/*  822 */     return this.requestEncoding;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setRequestCharacterEncoding(String requestEncoding)
/*      */   {
/*  828 */     this.requestEncoding = requestEncoding;
/*      */   }
/*      */   
/*      */ 
/*      */   public String getResponseCharacterEncoding()
/*      */   {
/*  834 */     return this.responseEncoding;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setResponseCharacterEncoding(String responseEncoding)
/*      */   {
/*  840 */     this.responseEncoding = responseEncoding;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setDispatchersUseEncodedPaths(boolean dispatchersUseEncodedPaths)
/*      */   {
/*  846 */     this.dispatchersUseEncodedPaths = dispatchersUseEncodedPaths;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getDispatchersUseEncodedPaths()
/*      */   {
/*  857 */     return this.dispatchersUseEncodedPaths;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setUseRelativeRedirects(boolean useRelativeRedirects)
/*      */   {
/*  863 */     this.useRelativeRedirects = useRelativeRedirects;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getUseRelativeRedirects()
/*      */   {
/*  874 */     return this.useRelativeRedirects;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setMapperContextRootRedirectEnabled(boolean mapperContextRootRedirectEnabled)
/*      */   {
/*  880 */     this.mapperContextRootRedirectEnabled = mapperContextRootRedirectEnabled;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getMapperContextRootRedirectEnabled()
/*      */   {
/*  891 */     return this.mapperContextRootRedirectEnabled;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setMapperDirectoryRedirectEnabled(boolean mapperDirectoryRedirectEnabled)
/*      */   {
/*  897 */     this.mapperDirectoryRedirectEnabled = mapperDirectoryRedirectEnabled;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getMapperDirectoryRedirectEnabled()
/*      */   {
/*  908 */     return this.mapperDirectoryRedirectEnabled;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setValidateClientProvidedNewSessionId(boolean validateClientProvidedNewSessionId)
/*      */   {
/*  914 */     this.validateClientProvidedNewSessionId = validateClientProvidedNewSessionId;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getValidateClientProvidedNewSessionId()
/*      */   {
/*  925 */     return this.validateClientProvidedNewSessionId;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setCookieProcessor(CookieProcessor cookieProcessor)
/*      */   {
/*  931 */     if (cookieProcessor == null)
/*      */     {
/*  933 */       throw new IllegalArgumentException(sm.getString("standardContext.cookieProcessor.null"));
/*      */     }
/*  935 */     this.cookieProcessor = cookieProcessor;
/*      */   }
/*      */   
/*      */ 
/*      */   public CookieProcessor getCookieProcessor()
/*      */   {
/*  941 */     return this.cookieProcessor;
/*      */   }
/*      */   
/*      */ 
/*      */   public Object getNamingToken()
/*      */   {
/*  947 */     return this.namingToken;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setContainerSciFilter(String containerSciFilter)
/*      */   {
/*  953 */     this.containerSciFilter = containerSciFilter;
/*      */   }
/*      */   
/*      */ 
/*      */   public String getContainerSciFilter()
/*      */   {
/*  959 */     return this.containerSciFilter;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean getSendRedirectBody()
/*      */   {
/*  965 */     return this.sendRedirectBody;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setSendRedirectBody(boolean sendRedirectBody)
/*      */   {
/*  971 */     this.sendRedirectBody = sendRedirectBody;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean getPreemptiveAuthentication()
/*      */   {
/*  977 */     return this.preemptiveAuthentication;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setPreemptiveAuthentication(boolean preemptiveAuthentication)
/*      */   {
/*  983 */     this.preemptiveAuthentication = preemptiveAuthentication;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setFireRequestListenersOnForwards(boolean enable)
/*      */   {
/*  989 */     this.fireRequestListenersOnForwards = enable;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean getFireRequestListenersOnForwards()
/*      */   {
/*  995 */     return this.fireRequestListenersOnForwards;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setAddWebinfClassesResources(boolean addWebinfClassesResources)
/*      */   {
/* 1002 */     this.addWebinfClassesResources = addWebinfClassesResources;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean getAddWebinfClassesResources()
/*      */   {
/* 1008 */     return this.addWebinfClassesResources;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setWebappVersion(String webappVersion)
/*      */   {
/* 1014 */     if (null == webappVersion) {
/* 1015 */       this.webappVersion = "";
/*      */     } else {
/* 1017 */       this.webappVersion = webappVersion;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public String getWebappVersion()
/*      */   {
/* 1024 */     return this.webappVersion;
/*      */   }
/*      */   
/*      */ 
/*      */   public String getBaseName()
/*      */   {
/* 1030 */     return new ContextName(this.path, this.webappVersion).getBaseName();
/*      */   }
/*      */   
/*      */ 
/*      */   public String getResourceOnlyServlets()
/*      */   {
/* 1036 */     return StringUtils.join(this.resourceOnlyServlets);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setResourceOnlyServlets(String resourceOnlyServlets)
/*      */   {
/* 1042 */     this.resourceOnlyServlets.clear();
/* 1043 */     if (resourceOnlyServlets == null) {
/* 1044 */       return;
/*      */     }
/* 1046 */     for (String servletName : resourceOnlyServlets.split(",")) {
/* 1047 */       servletName = servletName.trim();
/* 1048 */       if (servletName.length() > 0) {
/* 1049 */         this.resourceOnlyServlets.add(servletName);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean isResourceOnlyServlet(String servletName)
/*      */   {
/* 1057 */     return this.resourceOnlyServlets.contains(servletName);
/*      */   }
/*      */   
/*      */ 
/*      */   public int getEffectiveMajorVersion()
/*      */   {
/* 1063 */     return this.effectiveMajorVersion;
/*      */   }
/*      */   
/*      */   public void setEffectiveMajorVersion(int effectiveMajorVersion)
/*      */   {
/* 1068 */     this.effectiveMajorVersion = effectiveMajorVersion;
/*      */   }
/*      */   
/*      */   public int getEffectiveMinorVersion()
/*      */   {
/* 1073 */     return this.effectiveMinorVersion;
/*      */   }
/*      */   
/*      */   public void setEffectiveMinorVersion(int effectiveMinorVersion)
/*      */   {
/* 1078 */     this.effectiveMinorVersion = effectiveMinorVersion;
/*      */   }
/*      */   
/*      */   public void setLogEffectiveWebXml(boolean logEffectiveWebXml)
/*      */   {
/* 1083 */     this.logEffectiveWebXml = logEffectiveWebXml;
/*      */   }
/*      */   
/*      */   public boolean getLogEffectiveWebXml()
/*      */   {
/* 1088 */     return this.logEffectiveWebXml;
/*      */   }
/*      */   
/*      */   public Authenticator getAuthenticator()
/*      */   {
/* 1093 */     Pipeline pipeline = getPipeline();
/* 1094 */     if (pipeline != null) {
/* 1095 */       Valve basic = pipeline.getBasic();
/* 1096 */       if ((basic instanceof Authenticator))
/* 1097 */         return (Authenticator)basic;
/* 1098 */       for (Valve valve : pipeline.getValves()) {
/* 1099 */         if ((valve instanceof Authenticator)) {
/* 1100 */           return (Authenticator)valve;
/*      */         }
/*      */       }
/*      */     }
/* 1104 */     return null;
/*      */   }
/*      */   
/*      */   public JarScanner getJarScanner()
/*      */   {
/* 1109 */     if (this.jarScanner == null) {
/* 1110 */       this.jarScanner = new StandardJarScanner();
/*      */     }
/* 1112 */     return this.jarScanner;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setJarScanner(JarScanner jarScanner)
/*      */   {
/* 1118 */     this.jarScanner = jarScanner;
/*      */   }
/*      */   
/*      */ 
/*      */   public InstanceManager getInstanceManager()
/*      */   {
/* 1124 */     return this.instanceManager;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setInstanceManager(InstanceManager instanceManager)
/*      */   {
/* 1130 */     this.instanceManager = instanceManager;
/*      */   }
/*      */   
/*      */ 
/*      */   public String getEncodedPath()
/*      */   {
/* 1136 */     return this.encodedPath;
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
/*      */   public void setAllowCasualMultipartParsing(boolean allowCasualMultipartParsing)
/*      */   {
/* 1152 */     this.allowCasualMultipartParsing = allowCasualMultipartParsing;
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
/*      */   public boolean getAllowCasualMultipartParsing()
/*      */   {
/* 1165 */     return this.allowCasualMultipartParsing;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSwallowAbortedUploads(boolean swallowAbortedUploads)
/*      */   {
/* 1177 */     this.swallowAbortedUploads = swallowAbortedUploads;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getSwallowAbortedUploads()
/*      */   {
/* 1189 */     return this.swallowAbortedUploads;
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
/*      */   public void addServletContainerInitializer(ServletContainerInitializer sci, Set<Class<?>> classes)
/*      */   {
/* 1202 */     this.initializers.put(sci, classes);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getDelegate()
/*      */   {
/* 1214 */     return this.delegate;
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
/*      */   public void setDelegate(boolean delegate)
/*      */   {
/* 1227 */     boolean oldDelegate = this.delegate;
/* 1228 */     this.delegate = delegate;
/* 1229 */     this.support.firePropertyChange("delegate", oldDelegate, this.delegate);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isUseNaming()
/*      */   {
/* 1240 */     return this.useNaming;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseNaming(boolean useNaming)
/*      */   {
/* 1251 */     this.useNaming = useNaming;
/*      */   }
/*      */   
/*      */ 
/*      */   public Object[] getApplicationEventListeners()
/*      */   {
/* 1257 */     return this.applicationEventListenersList.toArray();
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
/*      */   public void setApplicationEventListeners(Object[] listeners)
/*      */   {
/* 1270 */     this.applicationEventListenersList.clear();
/* 1271 */     if ((listeners != null) && (listeners.length > 0)) {
/* 1272 */       this.applicationEventListenersList.addAll(Arrays.asList(listeners));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addApplicationEventListener(Object listener)
/*      */   {
/* 1284 */     this.applicationEventListenersList.add(listener);
/*      */   }
/*      */   
/*      */ 
/*      */   public Object[] getApplicationLifecycleListeners()
/*      */   {
/* 1290 */     return this.applicationLifecycleListenersObjects;
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
/*      */   public void setApplicationLifecycleListeners(Object[] listeners)
/*      */   {
/* 1303 */     this.applicationLifecycleListenersObjects = listeners;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addApplicationLifecycleListener(Object listener)
/*      */   {
/* 1314 */     int len = this.applicationLifecycleListenersObjects.length;
/* 1315 */     Object[] newListeners = Arrays.copyOf(this.applicationLifecycleListenersObjects, len + 1);
/*      */     
/* 1317 */     newListeners[len] = listener;
/* 1318 */     this.applicationLifecycleListenersObjects = newListeners;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getAntiResourceLocking()
/*      */   {
/* 1327 */     return this.antiResourceLocking;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAntiResourceLocking(boolean antiResourceLocking)
/*      */   {
/* 1339 */     boolean oldAntiResourceLocking = this.antiResourceLocking;
/* 1340 */     this.antiResourceLocking = antiResourceLocking;
/* 1341 */     this.support.firePropertyChange("antiResourceLocking", oldAntiResourceLocking, this.antiResourceLocking);
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
/*      */   public CharsetMapper getCharsetMapper()
/*      */   {
/* 1354 */     if (this.charsetMapper == null) {
/*      */       try {
/* 1356 */         Class<?> clazz = Class.forName(this.charsetMapperClass);
/* 1357 */         this.charsetMapper = ((CharsetMapper)clazz.getConstructor(new Class[0]).newInstance(new Object[0]));
/*      */       } catch (Throwable t) {
/* 1359 */         ExceptionUtils.handleThrowable(t);
/* 1360 */         this.charsetMapper = new CharsetMapper();
/*      */       }
/*      */     }
/*      */     
/* 1364 */     return this.charsetMapper;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCharsetMapper(CharsetMapper mapper)
/*      */   {
/* 1376 */     CharsetMapper oldCharsetMapper = this.charsetMapper;
/* 1377 */     this.charsetMapper = mapper;
/* 1378 */     if (mapper != null)
/* 1379 */       this.charsetMapperClass = mapper.getClass().getName();
/* 1380 */     this.support.firePropertyChange("charsetMapper", oldCharsetMapper, this.charsetMapper);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getCharset(Locale locale)
/*      */   {
/* 1388 */     return getCharsetMapper().getCharset(locale);
/*      */   }
/*      */   
/*      */ 
/*      */   public URL getConfigFile()
/*      */   {
/* 1394 */     return this.configFile;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setConfigFile(URL configFile)
/*      */   {
/* 1400 */     this.configFile = configFile;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean getConfigured()
/*      */   {
/* 1406 */     return this.configured;
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
/*      */   public void setConfigured(boolean configured)
/*      */   {
/* 1420 */     boolean oldConfigured = this.configured;
/* 1421 */     this.configured = configured;
/* 1422 */     this.support.firePropertyChange("configured", oldConfigured, this.configured);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getCookies()
/*      */   {
/* 1431 */     return this.cookies;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCookies(boolean cookies)
/*      */   {
/* 1443 */     boolean oldCookies = this.cookies;
/* 1444 */     this.cookies = cookies;
/* 1445 */     this.support.firePropertyChange("cookies", oldCookies, this.cookies);
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
/*      */   public String getSessionCookieName()
/*      */   {
/* 1461 */     return this.sessionCookieName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSessionCookieName(String sessionCookieName)
/*      */   {
/* 1473 */     String oldSessionCookieName = this.sessionCookieName;
/* 1474 */     this.sessionCookieName = sessionCookieName;
/* 1475 */     this.support.firePropertyChange("sessionCookieName", oldSessionCookieName, sessionCookieName);
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
/*      */   public boolean getUseHttpOnly()
/*      */   {
/* 1488 */     return this.useHttpOnly;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseHttpOnly(boolean useHttpOnly)
/*      */   {
/* 1500 */     boolean oldUseHttpOnly = this.useHttpOnly;
/* 1501 */     this.useHttpOnly = useHttpOnly;
/* 1502 */     this.support.firePropertyChange("useHttpOnly", oldUseHttpOnly, this.useHttpOnly);
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
/*      */   public String getSessionCookieDomain()
/*      */   {
/* 1517 */     return this.sessionCookieDomain;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSessionCookieDomain(String sessionCookieDomain)
/*      */   {
/* 1529 */     String oldSessionCookieDomain = this.sessionCookieDomain;
/* 1530 */     this.sessionCookieDomain = sessionCookieDomain;
/* 1531 */     this.support.firePropertyChange("sessionCookieDomain", oldSessionCookieDomain, sessionCookieDomain);
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
/*      */   public String getSessionCookiePath()
/*      */   {
/* 1545 */     return this.sessionCookiePath;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSessionCookiePath(String sessionCookiePath)
/*      */   {
/* 1557 */     String oldSessionCookiePath = this.sessionCookiePath;
/* 1558 */     this.sessionCookiePath = sessionCookiePath;
/* 1559 */     this.support.firePropertyChange("sessionCookiePath", oldSessionCookiePath, sessionCookiePath);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getSessionCookiePathUsesTrailingSlash()
/*      */   {
/* 1566 */     return this.sessionCookiePathUsesTrailingSlash;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setSessionCookiePathUsesTrailingSlash(boolean sessionCookiePathUsesTrailingSlash)
/*      */   {
/* 1573 */     this.sessionCookiePathUsesTrailingSlash = sessionCookiePathUsesTrailingSlash;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getCrossContext()
/*      */   {
/* 1580 */     return this.crossContext;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCrossContext(boolean crossContext)
/*      */   {
/* 1592 */     boolean oldCrossContext = this.crossContext;
/* 1593 */     this.crossContext = crossContext;
/* 1594 */     this.support.firePropertyChange("crossContext", oldCrossContext, this.crossContext);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getDefaultContextXml()
/*      */   {
/* 1601 */     return this.defaultContextXml;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDefaultContextXml(String defaultContextXml)
/*      */   {
/* 1612 */     this.defaultContextXml = defaultContextXml;
/*      */   }
/*      */   
/*      */   public String getDefaultWebXml() {
/* 1616 */     return this.defaultWebXml;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDefaultWebXml(String defaultWebXml)
/*      */   {
/* 1627 */     this.defaultWebXml = defaultWebXml;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getStartupTime()
/*      */   {
/* 1636 */     return this.startupTime;
/*      */   }
/*      */   
/*      */   public void setStartupTime(long startupTime) {
/* 1640 */     this.startupTime = startupTime;
/*      */   }
/*      */   
/*      */   public long getTldScanTime() {
/* 1644 */     return this.tldScanTime;
/*      */   }
/*      */   
/*      */   public void setTldScanTime(long tldScanTime) {
/* 1648 */     this.tldScanTime = tldScanTime;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean getDenyUncoveredHttpMethods()
/*      */   {
/* 1654 */     return this.denyUncoveredHttpMethods;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setDenyUncoveredHttpMethods(boolean denyUncoveredHttpMethods)
/*      */   {
/* 1660 */     this.denyUncoveredHttpMethods = denyUncoveredHttpMethods;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDisplayName()
/*      */   {
/* 1670 */     return this.displayName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getAltDDName()
/*      */   {
/* 1680 */     return this.altDDName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAltDDName(String altDDName)
/*      */   {
/* 1691 */     this.altDDName = altDDName;
/* 1692 */     if (this.context != null) {
/* 1693 */       this.context.setAttribute("org.apache.catalina.deploy.alt_dd", altDDName);
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
/*      */   public void setDisplayName(String displayName)
/*      */   {
/* 1706 */     String oldDisplayName = this.displayName;
/* 1707 */     this.displayName = displayName;
/* 1708 */     this.support.firePropertyChange("displayName", oldDisplayName, this.displayName);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getDistributable()
/*      */   {
/* 1719 */     return this.distributable;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDistributable(boolean distributable)
/*      */   {
/* 1730 */     boolean oldDistributable = this.distributable;
/* 1731 */     this.distributable = distributable;
/* 1732 */     this.support.firePropertyChange("distributable", oldDistributable, this.distributable);
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
/*      */   public String getDocBase()
/*      */   {
/* 1745 */     return this.docBase;
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
/*      */   public void setDocBase(String docBase)
/*      */   {
/* 1759 */     this.docBase = docBase;
/*      */   }
/*      */   
/*      */   public String getJ2EEApplication()
/*      */   {
/* 1764 */     return this.j2EEApplication;
/*      */   }
/*      */   
/*      */   public void setJ2EEApplication(String j2EEApplication) {
/* 1768 */     this.j2EEApplication = j2EEApplication;
/*      */   }
/*      */   
/*      */   public String getJ2EEServer() {
/* 1772 */     return this.j2EEServer;
/*      */   }
/*      */   
/*      */   public void setJ2EEServer(String j2EEServer) {
/* 1776 */     this.j2EEServer = j2EEServer;
/*      */   }
/*      */   
/*      */ 
/*      */   public Loader getLoader()
/*      */   {
/* 1782 */     Lock readLock = this.loaderLock.readLock();
/* 1783 */     readLock.lock();
/*      */     try {
/* 1785 */       return this.loader;
/*      */     } finally {
/* 1787 */       readLock.unlock();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void setLoader(Loader loader)
/*      */   {
/* 1794 */     Lock writeLock = this.loaderLock.writeLock();
/* 1795 */     writeLock.lock();
/* 1796 */     Loader oldLoader = null;
/*      */     try
/*      */     {
/* 1799 */       oldLoader = this.loader;
/* 1800 */       if (oldLoader == loader)
/* 1801 */         return;
/* 1802 */       this.loader = loader;
/*      */       
/*      */ 
/* 1805 */       if ((getState().isAvailable()) && (oldLoader != null) && ((oldLoader instanceof Lifecycle))) {
/*      */         try
/*      */         {
/* 1808 */           ((Lifecycle)oldLoader).stop();
/*      */         } catch (LifecycleException e) {
/* 1810 */           log.error("StandardContext.setLoader: stop: ", e);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1815 */       if (loader != null)
/* 1816 */         loader.setContext(this);
/* 1817 */       if ((getState().isAvailable()) && (loader != null) && ((loader instanceof Lifecycle))) {
/*      */         try
/*      */         {
/* 1820 */           ((Lifecycle)loader).start();
/*      */         } catch (LifecycleException e) {
/* 1822 */           log.error("StandardContext.setLoader: start: ", e);
/*      */         }
/*      */       }
/*      */     } finally {
/* 1826 */       writeLock.unlock();
/*      */     }
/*      */     
/*      */ 
/* 1830 */     this.support.firePropertyChange("loader", oldLoader, loader);
/*      */   }
/*      */   
/*      */ 
/*      */   public Manager getManager()
/*      */   {
/* 1836 */     Lock readLock = this.managerLock.readLock();
/* 1837 */     readLock.lock();
/*      */     try {
/* 1839 */       return this.manager;
/*      */     } finally {
/* 1841 */       readLock.unlock();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setManager(Manager manager)
/*      */   {
/* 1849 */     Lock writeLock = this.managerLock.writeLock();
/* 1850 */     writeLock.lock();
/* 1851 */     Manager oldManager = null;
/*      */     try
/*      */     {
/* 1854 */       oldManager = this.manager;
/* 1855 */       if (oldManager == manager)
/* 1856 */         return;
/* 1857 */       this.manager = manager;
/*      */       
/*      */ 
/* 1860 */       if ((oldManager instanceof Lifecycle)) {
/*      */         try {
/* 1862 */           ((Lifecycle)oldManager).stop();
/* 1863 */           ((Lifecycle)oldManager).destroy();
/*      */         } catch (LifecycleException e) {
/* 1865 */           log.error("StandardContext.setManager: stop-destroy: ", e);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1870 */       if (manager != null) {
/* 1871 */         manager.setContext(this);
/*      */       }
/* 1873 */       if ((getState().isAvailable()) && ((manager instanceof Lifecycle))) {
/*      */         try {
/* 1875 */           ((Lifecycle)manager).start();
/*      */         } catch (LifecycleException e) {
/* 1877 */           log.error("StandardContext.setManager: start: ", e);
/*      */         }
/*      */       }
/*      */     } finally {
/* 1881 */       writeLock.unlock();
/*      */     }
/*      */     
/*      */ 
/* 1885 */     this.support.firePropertyChange("manager", oldManager, manager);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getIgnoreAnnotations()
/*      */   {
/* 1894 */     return this.ignoreAnnotations;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setIgnoreAnnotations(boolean ignoreAnnotations)
/*      */   {
/* 1906 */     boolean oldIgnoreAnnotations = this.ignoreAnnotations;
/* 1907 */     this.ignoreAnnotations = ignoreAnnotations;
/* 1908 */     this.support.firePropertyChange("ignoreAnnotations", oldIgnoreAnnotations, this.ignoreAnnotations);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public LoginConfig getLoginConfig()
/*      */   {
/* 1919 */     return this.loginConfig;
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
/*      */   public void setLoginConfig(LoginConfig config)
/*      */   {
/* 1933 */     if (config == null)
/*      */     {
/* 1935 */       throw new IllegalArgumentException(sm.getString("standardContext.loginConfig.required")); }
/* 1936 */     String loginPage = config.getLoginPage();
/* 1937 */     if ((loginPage != null) && (!loginPage.startsWith("/"))) {
/* 1938 */       if (isServlet22()) {
/* 1939 */         if (log.isDebugEnabled()) {
/* 1940 */           log.debug(sm.getString("standardContext.loginConfig.loginWarning", new Object[] { loginPage }));
/*      */         }
/* 1942 */         config.setLoginPage("/" + loginPage);
/*      */       }
/*      */       else {
/* 1945 */         throw new IllegalArgumentException(sm.getString("standardContext.loginConfig.loginPage", new Object[] { loginPage }));
/*      */       }
/*      */     }
/*      */     
/* 1949 */     String errorPage = config.getErrorPage();
/* 1950 */     if ((errorPage != null) && (!errorPage.startsWith("/"))) {
/* 1951 */       if (isServlet22()) {
/* 1952 */         if (log.isDebugEnabled()) {
/* 1953 */           log.debug(sm.getString("standardContext.loginConfig.errorWarning", new Object[] { errorPage }));
/*      */         }
/* 1955 */         config.setErrorPage("/" + errorPage);
/*      */       }
/*      */       else {
/* 1958 */         throw new IllegalArgumentException(sm.getString("standardContext.loginConfig.errorPage", new Object[] { errorPage }));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1964 */     LoginConfig oldLoginConfig = this.loginConfig;
/* 1965 */     this.loginConfig = config;
/* 1966 */     this.support.firePropertyChange("loginConfig", oldLoginConfig, this.loginConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public NamingResourcesImpl getNamingResources()
/*      */   {
/* 1978 */     if (this.namingResources == null) {
/* 1979 */       setNamingResources(new NamingResourcesImpl());
/*      */     }
/* 1981 */     return this.namingResources;
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
/*      */   public void setNamingResources(NamingResourcesImpl namingResources)
/*      */   {
/* 1995 */     NamingResourcesImpl oldNamingResources = this.namingResources;
/* 1996 */     this.namingResources = namingResources;
/* 1997 */     if (namingResources != null) {
/* 1998 */       namingResources.setContainer(this);
/*      */     }
/* 2000 */     this.support.firePropertyChange("namingResources", oldNamingResources, this.namingResources);
/*      */     
/*      */ 
/* 2003 */     if ((getState() == LifecycleState.NEW) || 
/* 2004 */       (getState() == LifecycleState.INITIALIZING) || 
/* 2005 */       (getState() == LifecycleState.INITIALIZED))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2016 */       return;
/*      */     }
/*      */     
/* 2019 */     if (oldNamingResources != null) {
/*      */       try {
/* 2021 */         oldNamingResources.stop();
/* 2022 */         oldNamingResources.destroy();
/*      */       } catch (LifecycleException e) {
/* 2024 */         log.warn("standardContext.namingResource.destroy.fail", e);
/*      */       }
/*      */     }
/* 2027 */     if (namingResources != null) {
/*      */       try {
/* 2029 */         namingResources.init();
/* 2030 */         namingResources.start();
/*      */       } catch (LifecycleException e) {
/* 2032 */         log.warn("standardContext.namingResource.init.fail", e);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getPath()
/*      */   {
/* 2043 */     return this.path;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPath(String path)
/*      */   {
/* 2054 */     boolean invalid = false;
/* 2055 */     if ((path == null) || (path.equals("/"))) {
/* 2056 */       invalid = true;
/* 2057 */       this.path = "";
/* 2058 */     } else if (("".equals(path)) || (path.startsWith("/"))) {
/* 2059 */       this.path = path;
/*      */     } else {
/* 2061 */       invalid = true;
/* 2062 */       this.path = ("/" + path);
/*      */     }
/* 2064 */     if (this.path.endsWith("/")) {
/* 2065 */       invalid = true;
/* 2066 */       this.path = this.path.substring(0, this.path.length() - 1);
/*      */     }
/* 2068 */     if (invalid) {
/* 2069 */       log.warn(sm.getString("standardContext.pathInvalid", new Object[] { path, this.path }));
/*      */     }
/*      */     
/* 2072 */     this.encodedPath = URLEncoder.DEFAULT.encode(this.path, StandardCharsets.UTF_8);
/* 2073 */     if (getName() == null) {
/* 2074 */       setName(this.path);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getPublicId()
/*      */   {
/* 2086 */     return this.publicId;
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
/*      */   public void setPublicId(String publicId)
/*      */   {
/* 2100 */     if (log.isDebugEnabled()) {
/* 2101 */       log.debug("Setting deployment descriptor public ID to '" + publicId + "'");
/*      */     }
/*      */     
/* 2104 */     String oldPublicId = this.publicId;
/* 2105 */     this.publicId = publicId;
/* 2106 */     this.support.firePropertyChange("publicId", oldPublicId, publicId);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getReloadable()
/*      */   {
/* 2117 */     return this.reloadable;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getOverride()
/*      */   {
/* 2128 */     return this.override;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getOriginalDocBase()
/*      */   {
/* 2140 */     return this.originalDocBase;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setOriginalDocBase(String docBase)
/*      */   {
/* 2152 */     this.originalDocBase = docBase;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ClassLoader getParentClassLoader()
/*      */   {
/* 2163 */     if (this.parentClassLoader != null)
/* 2164 */       return this.parentClassLoader;
/* 2165 */     if (getPrivileged())
/* 2166 */       return getClass().getClassLoader();
/* 2167 */     if (this.parent != null) {
/* 2168 */       return this.parent.getParentClassLoader();
/*      */     }
/* 2170 */     return ClassLoader.getSystemClassLoader();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getPrivileged()
/*      */   {
/* 2180 */     return this.privileged;
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
/*      */   public void setPrivileged(boolean privileged)
/*      */   {
/* 2193 */     boolean oldPrivileged = this.privileged;
/* 2194 */     this.privileged = privileged;
/* 2195 */     this.support.firePropertyChange("privileged", oldPrivileged, this.privileged);
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
/*      */   public void setReloadable(boolean reloadable)
/*      */   {
/* 2210 */     boolean oldReloadable = this.reloadable;
/* 2211 */     this.reloadable = reloadable;
/* 2212 */     this.support.firePropertyChange("reloadable", oldReloadable, this.reloadable);
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
/*      */   public void setOverride(boolean override)
/*      */   {
/* 2227 */     boolean oldOverride = this.override;
/* 2228 */     this.override = override;
/* 2229 */     this.support.firePropertyChange("override", oldOverride, this.override);
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
/*      */   public void setReplaceWelcomeFiles(boolean replaceWelcomeFiles)
/*      */   {
/* 2243 */     boolean oldReplaceWelcomeFiles = this.replaceWelcomeFiles;
/* 2244 */     this.replaceWelcomeFiles = replaceWelcomeFiles;
/* 2245 */     this.support.firePropertyChange("replaceWelcomeFiles", oldReplaceWelcomeFiles, this.replaceWelcomeFiles);
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
/*      */   public javax.servlet.ServletContext getServletContext()
/*      */   {
/* 2258 */     if (this.context == null) {
/* 2259 */       this.context = new ApplicationContext(this);
/* 2260 */       if (this.altDDName != null)
/* 2261 */         this.context.setAttribute("org.apache.catalina.deploy.alt_dd", this.altDDName);
/*      */     }
/* 2263 */     return this.context.getFacade();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getSessionTimeout()
/*      */   {
/* 2275 */     return this.sessionTimeout;
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
/*      */   public void setSessionTimeout(int timeout)
/*      */   {
/* 2289 */     int oldSessionTimeout = this.sessionTimeout;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2295 */     this.sessionTimeout = (timeout == 0 ? -1 : timeout);
/* 2296 */     this.support.firePropertyChange("sessionTimeout", oldSessionTimeout, this.sessionTimeout);
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
/*      */   public boolean getSwallowOutput()
/*      */   {
/* 2309 */     return this.swallowOutput;
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
/*      */   public void setSwallowOutput(boolean swallowOutput)
/*      */   {
/* 2324 */     boolean oldSwallowOutput = this.swallowOutput;
/* 2325 */     this.swallowOutput = swallowOutput;
/* 2326 */     this.support.firePropertyChange("swallowOutput", oldSwallowOutput, this.swallowOutput);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getUnloadDelay()
/*      */   {
/* 2338 */     return this.unloadDelay;
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
/*      */   public void setUnloadDelay(long unloadDelay)
/*      */   {
/* 2353 */     long oldUnloadDelay = this.unloadDelay;
/* 2354 */     this.unloadDelay = unloadDelay;
/* 2355 */     this.support.firePropertyChange("unloadDelay", 
/* 2356 */       Long.valueOf(oldUnloadDelay), 
/* 2357 */       Long.valueOf(this.unloadDelay));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getUnpackWAR()
/*      */   {
/* 2367 */     return this.unpackWAR;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUnpackWAR(boolean unpackWAR)
/*      */   {
/* 2379 */     this.unpackWAR = unpackWAR;
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
/*      */   public boolean getCopyXML()
/*      */   {
/* 2392 */     return this.copyXML;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCopyXML(boolean copyXML)
/*      */   {
/* 2403 */     this.copyXML = copyXML;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getWrapperClass()
/*      */   {
/* 2414 */     return this.wrapperClassName;
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
/*      */   public void setWrapperClass(String wrapperClassName)
/*      */   {
/* 2431 */     this.wrapperClassName = wrapperClassName;
/*      */     try
/*      */     {
/* 2434 */       this.wrapperClass = Class.forName(wrapperClassName);
/* 2435 */       if (!StandardWrapper.class.isAssignableFrom(this.wrapperClass))
/*      */       {
/* 2437 */         throw new IllegalArgumentException(sm.getString("standardContext.invalidWrapperClass", new Object[] { wrapperClassName }));
/*      */       }
/*      */     }
/*      */     catch (ClassNotFoundException cnfe) {
/* 2441 */       throw new IllegalArgumentException(cnfe.getMessage());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public WebResourceRoot getResources()
/*      */   {
/* 2448 */     Lock readLock = this.resourcesLock.readLock();
/* 2449 */     readLock.lock();
/*      */     try {
/* 2451 */       return this.resources;
/*      */     } finally {
/* 2453 */       readLock.unlock();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setResources(WebResourceRoot resources)
/*      */   {
/* 2461 */     Lock writeLock = this.resourcesLock.writeLock();
/* 2462 */     writeLock.lock();
/* 2463 */     WebResourceRoot oldResources = null;
/*      */     try {
/* 2465 */       if (getState().isAvailable())
/*      */       {
/* 2467 */         throw new IllegalStateException(sm.getString("standardContext.resourcesStart"));
/*      */       }
/*      */       
/* 2470 */       oldResources = this.resources;
/* 2471 */       if (oldResources == resources) {
/* 2472 */         return;
/*      */       }
/* 2474 */       this.resources = resources;
/* 2475 */       if (oldResources != null) {
/* 2476 */         oldResources.setContext(null);
/*      */       }
/* 2478 */       if (resources != null) {
/* 2479 */         resources.setContext(this);
/*      */       }
/*      */       
/* 2482 */       this.support.firePropertyChange("resources", oldResources, resources);
/*      */     }
/*      */     finally {
/* 2485 */       writeLock.unlock();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public JspConfigDescriptor getJspConfigDescriptor()
/*      */   {
/* 2492 */     return this.jspConfigDescriptor;
/*      */   }
/*      */   
/*      */   public void setJspConfigDescriptor(JspConfigDescriptor descriptor)
/*      */   {
/* 2497 */     this.jspConfigDescriptor = descriptor;
/*      */   }
/*      */   
/*      */   public ThreadBindingListener getThreadBindingListener()
/*      */   {
/* 2502 */     return this.threadBindingListener;
/*      */   }
/*      */   
/*      */   public void setThreadBindingListener(ThreadBindingListener threadBindingListener)
/*      */   {
/* 2507 */     this.threadBindingListener = threadBindingListener;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getJndiExceptionOnFailedWrite()
/*      */   {
/* 2518 */     return this.jndiExceptionOnFailedWrite;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setJndiExceptionOnFailedWrite(boolean jndiExceptionOnFailedWrite)
/*      */   {
/* 2530 */     this.jndiExceptionOnFailedWrite = jndiExceptionOnFailedWrite;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getCharsetMapperClass()
/*      */   {
/* 2539 */     return this.charsetMapperClass;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCharsetMapperClass(String mapper)
/*      */   {
/* 2551 */     String oldCharsetMapperClass = this.charsetMapperClass;
/* 2552 */     this.charsetMapperClass = mapper;
/* 2553 */     this.support.firePropertyChange("charsetMapperClass", oldCharsetMapperClass, this.charsetMapperClass);
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
/*      */   public String getWorkPath()
/*      */   {
/* 2566 */     if (getWorkDir() == null) {
/* 2567 */       return null;
/*      */     }
/* 2569 */     File workDir = new File(getWorkDir());
/* 2570 */     if (!workDir.isAbsolute()) {
/*      */       try
/*      */       {
/* 2573 */         workDir = new File(getCatalinaBase().getCanonicalFile(), getWorkDir());
/*      */       } catch (IOException e) {
/* 2575 */         log.warn(sm.getString("standardContext.workPath", new Object[] { getName() }), e);
/*      */       }
/*      */     }
/*      */     
/* 2579 */     return workDir.getAbsolutePath();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getWorkDir()
/*      */   {
/* 2587 */     return this.workDir;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setWorkDir(String workDir)
/*      */   {
/* 2599 */     this.workDir = workDir;
/*      */     
/* 2601 */     if (getState().isAvailable()) {
/* 2602 */       postWorkDirectory();
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean getClearReferencesRmiTargets()
/*      */   {
/* 2608 */     return this.clearReferencesRmiTargets;
/*      */   }
/*      */   
/*      */   public void setClearReferencesRmiTargets(boolean clearReferencesRmiTargets)
/*      */   {
/* 2613 */     boolean oldClearReferencesRmiTargets = this.clearReferencesRmiTargets;
/* 2614 */     this.clearReferencesRmiTargets = clearReferencesRmiTargets;
/* 2615 */     this.support.firePropertyChange("clearReferencesRmiTargets", oldClearReferencesRmiTargets, this.clearReferencesRmiTargets);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getClearReferencesStopThreads()
/*      */   {
/* 2625 */     return this.clearReferencesStopThreads;
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
/*      */   public void setClearReferencesStopThreads(boolean clearReferencesStopThreads)
/*      */   {
/* 2638 */     boolean oldClearReferencesStopThreads = this.clearReferencesStopThreads;
/* 2639 */     this.clearReferencesStopThreads = clearReferencesStopThreads;
/* 2640 */     this.support.firePropertyChange("clearReferencesStopThreads", oldClearReferencesStopThreads, this.clearReferencesStopThreads);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getClearReferencesStopTimerThreads()
/*      */   {
/* 2651 */     return this.clearReferencesStopTimerThreads;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setClearReferencesStopTimerThreads(boolean clearReferencesStopTimerThreads)
/*      */   {
/* 2663 */     boolean oldClearReferencesStopTimerThreads = this.clearReferencesStopTimerThreads;
/*      */     
/* 2665 */     this.clearReferencesStopTimerThreads = clearReferencesStopTimerThreads;
/* 2666 */     this.support.firePropertyChange("clearReferencesStopTimerThreads", oldClearReferencesStopTimerThreads, this.clearReferencesStopTimerThreads);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getClearReferencesHttpClientKeepAliveThread()
/*      */   {
/* 2677 */     return this.clearReferencesHttpClientKeepAliveThread;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setClearReferencesHttpClientKeepAliveThread(boolean clearReferencesHttpClientKeepAliveThread)
/*      */   {
/* 2689 */     this.clearReferencesHttpClientKeepAliveThread = clearReferencesHttpClientKeepAliveThread;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean getRenewThreadsWhenStoppingContext()
/*      */   {
/* 2695 */     return this.renewThreadsWhenStoppingContext;
/*      */   }
/*      */   
/*      */   public void setRenewThreadsWhenStoppingContext(boolean renewThreadsWhenStoppingContext)
/*      */   {
/* 2700 */     boolean oldRenewThreadsWhenStoppingContext = this.renewThreadsWhenStoppingContext;
/*      */     
/* 2702 */     this.renewThreadsWhenStoppingContext = renewThreadsWhenStoppingContext;
/* 2703 */     this.support.firePropertyChange("renewThreadsWhenStoppingContext", oldRenewThreadsWhenStoppingContext, this.renewThreadsWhenStoppingContext);
/*      */   }
/*      */   
/*      */ 
/*      */   public Boolean getFailCtxIfServletStartFails()
/*      */   {
/* 2709 */     return this.failCtxIfServletStartFails;
/*      */   }
/*      */   
/*      */   public void setFailCtxIfServletStartFails(Boolean failCtxIfServletStartFails)
/*      */   {
/* 2714 */     Boolean oldFailCtxIfServletStartFails = this.failCtxIfServletStartFails;
/* 2715 */     this.failCtxIfServletStartFails = failCtxIfServletStartFails;
/* 2716 */     this.support.firePropertyChange("failCtxIfServletStartFails", oldFailCtxIfServletStartFails, failCtxIfServletStartFails);
/*      */   }
/*      */   
/*      */ 
/*      */   protected boolean getComputedFailCtxIfServletStartFails()
/*      */   {
/* 2722 */     if (this.failCtxIfServletStartFails != null) {
/* 2723 */       return this.failCtxIfServletStartFails.booleanValue();
/*      */     }
/*      */     
/* 2726 */     if ((getParent() instanceof StandardHost)) {
/* 2727 */       return ((StandardHost)getParent()).isFailCtxIfServletStartFails();
/*      */     }
/*      */     
/* 2730 */     return false;
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
/*      */   public void addApplicationListener(String listener)
/*      */   {
/* 2744 */     synchronized (this.applicationListenersLock) {
/* 2745 */       String[] results = new String[this.applicationListeners.length + 1];
/* 2746 */       for (int i = 0; i < this.applicationListeners.length; i++) {
/* 2747 */         if (listener.equals(this.applicationListeners[i])) {
/* 2748 */           log.info(sm.getString("standardContext.duplicateListener", new Object[] { listener }));
/* 2749 */           return;
/*      */         }
/* 2751 */         results[i] = this.applicationListeners[i];
/*      */       }
/* 2753 */       results[this.applicationListeners.length] = listener;
/* 2754 */       this.applicationListeners = results;
/*      */     }
/* 2756 */     fireContainerEvent("addApplicationListener", listener);
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
/*      */   public void addApplicationParameter(ApplicationParameter parameter)
/*      */   {
/* 2770 */     synchronized (this.applicationParametersLock) {
/* 2771 */       String newName = parameter.getName();
/* 2772 */       for (ApplicationParameter p : this.applicationParameters) {
/* 2773 */         if ((newName.equals(p.getName())) && (!p.getOverride()))
/* 2774 */           return;
/*      */       }
/* 2776 */       ApplicationParameter[] results = (ApplicationParameter[])Arrays.copyOf(this.applicationParameters, this.applicationParameters.length + 1);
/*      */       
/* 2778 */       results[this.applicationParameters.length] = parameter;
/* 2779 */       this.applicationParameters = results;
/*      */     }
/* 2781 */     fireContainerEvent("addApplicationParameter", parameter);
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
/*      */   public void addChild(Container child)
/*      */   {
/* 2799 */     Wrapper oldJspServlet = null;
/*      */     
/* 2801 */     if (!(child instanceof Wrapper))
/*      */     {
/* 2803 */       throw new IllegalArgumentException(sm.getString("standardContext.notWrapper"));
/*      */     }
/*      */     
/* 2806 */     boolean isJspServlet = "jsp".equals(child.getName());
/*      */     
/*      */ 
/* 2809 */     if (isJspServlet) {
/* 2810 */       oldJspServlet = (Wrapper)findChild("jsp");
/* 2811 */       if (oldJspServlet != null) {
/* 2812 */         removeChild(oldJspServlet);
/*      */       }
/*      */     }
/*      */     
/* 2816 */     super.addChild(child);
/*      */     
/* 2818 */     if ((isJspServlet) && (oldJspServlet != null))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 2823 */       String[] jspMappings = oldJspServlet.findMappings();
/* 2824 */       for (int i = 0; (jspMappings != null) && (i < jspMappings.length); i++) {
/* 2825 */         addServletMappingDecoded(jspMappings[i], child.getName());
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
/*      */   public void addConstraint(SecurityConstraint constraint)
/*      */   {
/* 2840 */     SecurityCollection[] collections = constraint.findCollections();
/* 2841 */     for (int i = 0; i < collections.length; i++) {
/* 2842 */       String[] patterns = collections[i].findPatterns();
/* 2843 */       for (int j = 0; j < patterns.length; j++) {
/* 2844 */         patterns[j] = adjustURLPattern(patterns[j]);
/* 2845 */         if (!validateURLPattern(patterns[j]))
/*      */         {
/*      */ 
/* 2848 */           throw new IllegalArgumentException(sm.getString("standardContext.securityConstraint.pattern", new Object[] { patterns[j] }));
/*      */         }
/*      */       }
/* 2851 */       if ((collections[i].findMethods().length > 0) && 
/* 2852 */         (collections[i].findOmittedMethods().length > 0)) {
/* 2853 */         throw new IllegalArgumentException(sm.getString("standardContext.securityConstraint.mixHttpMethod"));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2859 */     synchronized (this.constraintsLock) {
/* 2860 */       SecurityConstraint[] results = new SecurityConstraint[this.constraints.length + 1];
/*      */       
/* 2862 */       for (int i = 0; i < this.constraints.length; i++)
/* 2863 */         results[i] = this.constraints[i];
/* 2864 */       results[this.constraints.length] = constraint;
/* 2865 */       this.constraints = results;
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
/*      */   public void addErrorPage(ErrorPage errorPage)
/*      */   {
/* 2880 */     if (errorPage == null)
/*      */     {
/* 2882 */       throw new IllegalArgumentException(sm.getString("standardContext.errorPage.required")); }
/* 2883 */     String location = errorPage.getLocation();
/* 2884 */     if ((location != null) && (!location.startsWith("/"))) {
/* 2885 */       if (isServlet22()) {
/* 2886 */         if (log.isDebugEnabled()) {
/* 2887 */           log.debug(sm.getString("standardContext.errorPage.warning", new Object[] { location }));
/*      */         }
/* 2889 */         errorPage.setLocation("/" + location);
/*      */       }
/*      */       else {
/* 2892 */         throw new IllegalArgumentException(sm.getString("standardContext.errorPage.error", new Object[] { location }));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2898 */     String exceptionType = errorPage.getExceptionType();
/* 2899 */     if (exceptionType != null) {
/* 2900 */       synchronized (this.exceptionPages) {
/* 2901 */         this.exceptionPages.put(exceptionType, errorPage);
/*      */       }
/*      */     } else {
/* 2904 */       synchronized (this.statusPages) {
/* 2905 */         this.statusPages.put(Integer.valueOf(errorPage.getErrorCode()), errorPage);
/*      */       }
/*      */     }
/*      */     
/* 2909 */     fireContainerEvent("addErrorPage", errorPage);
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
/*      */   public void addFilterDef(FilterDef filterDef)
/*      */   {
/* 2922 */     synchronized (this.filterDefs) {
/* 2923 */       this.filterDefs.put(filterDef.getFilterName(), filterDef);
/*      */     }
/* 2925 */     fireContainerEvent("addFilterDef", filterDef);
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
/*      */   public void addFilterMap(FilterMap filterMap)
/*      */   {
/* 2942 */     validateFilterMap(filterMap);
/*      */     
/* 2944 */     this.filterMaps.add(filterMap);
/* 2945 */     fireContainerEvent("addFilterMap", filterMap);
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
/*      */   public void addFilterMapBefore(FilterMap filterMap)
/*      */   {
/* 2961 */     validateFilterMap(filterMap);
/*      */     
/* 2963 */     this.filterMaps.addBefore(filterMap);
/* 2964 */     fireContainerEvent("addFilterMap", filterMap);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void validateFilterMap(FilterMap filterMap)
/*      */   {
/* 2975 */     String filterName = filterMap.getFilterName();
/* 2976 */     String[] servletNames = filterMap.getServletNames();
/* 2977 */     String[] urlPatterns = filterMap.getURLPatterns();
/* 2978 */     if (findFilterDef(filterName) == null)
/*      */     {
/* 2980 */       throw new IllegalArgumentException(sm.getString("standardContext.filterMap.name", new Object[] { filterName }));
/*      */     }
/* 2982 */     if ((!filterMap.getMatchAllServletNames()) && 
/* 2983 */       (!filterMap.getMatchAllUrlPatterns()) && (servletNames.length == 0) && (urlPatterns.length == 0))
/*      */     {
/*      */ 
/* 2986 */       throw new IllegalArgumentException(sm.getString("standardContext.filterMap.either"));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2993 */     for (int i = 0; i < urlPatterns.length; i++) {
/* 2994 */       if (!validateURLPattern(urlPatterns[i]))
/*      */       {
/* 2996 */         throw new IllegalArgumentException(sm.getString("standardContext.filterMap.pattern", new Object[] { urlPatterns[i] }));
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
/*      */   public void addLocaleEncodingMappingParameter(String locale, String encoding)
/*      */   {
/* 3011 */     getCharsetMapper().addCharsetMappingFromDeploymentDescriptor(locale, encoding);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addMessageDestination(MessageDestination md)
/*      */   {
/* 3022 */     synchronized (this.messageDestinations) {
/* 3023 */       this.messageDestinations.put(md.getName(), md);
/*      */     }
/* 3025 */     fireContainerEvent("addMessageDestination", md.getName());
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
/*      */   public void addMessageDestinationRef(MessageDestinationRef mdr)
/*      */   {
/* 3038 */     this.namingResources.addMessageDestinationRef(mdr);
/* 3039 */     fireContainerEvent("addMessageDestinationRef", mdr.getName());
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
/*      */   public void addMimeMapping(String extension, String mimeType)
/*      */   {
/* 3054 */     synchronized (this.mimeMappings) {
/* 3055 */       this.mimeMappings.put(extension.toLowerCase(Locale.ENGLISH), mimeType);
/*      */     }
/* 3057 */     fireContainerEvent("addMimeMapping", extension);
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
/*      */   public void addParameter(String name, String value)
/*      */   {
/* 3075 */     if ((name == null) || (value == null))
/*      */     {
/* 3077 */       throw new IllegalArgumentException(sm.getString("standardContext.parameter.required"));
/*      */     }
/*      */     
/*      */ 
/* 3081 */     String oldValue = (String)this.parameters.putIfAbsent(name, value);
/*      */     
/* 3083 */     if (oldValue != null)
/*      */     {
/* 3085 */       throw new IllegalArgumentException(sm.getString("standardContext.parameter.duplicate", new Object[] { name }));
/*      */     }
/*      */     
/* 3088 */     fireContainerEvent("addParameter", name);
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
/*      */   public void addRoleMapping(String role, String link)
/*      */   {
/* 3101 */     synchronized (this.roleMappings) {
/* 3102 */       this.roleMappings.put(role, link);
/*      */     }
/* 3104 */     fireContainerEvent("addRoleMapping", role);
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
/*      */   public void addSecurityRole(String role)
/*      */   {
/* 3117 */     synchronized (this.securityRolesLock) {
/* 3118 */       String[] results = new String[this.securityRoles.length + 1];
/* 3119 */       for (int i = 0; i < this.securityRoles.length; i++)
/* 3120 */         results[i] = this.securityRoles[i];
/* 3121 */       results[this.securityRoles.length] = role;
/* 3122 */       this.securityRoles = results;
/*      */     }
/* 3124 */     fireContainerEvent("addSecurityRole", role);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public void addServletMapping(String pattern, String name)
/*      */   {
/* 3132 */     addServletMappingDecoded(UDecoder.URLDecode(pattern, "UTF-8"), name);
/*      */   }
/*      */   
/*      */ 
/*      */   @Deprecated
/*      */   public void addServletMapping(String pattern, String name, boolean jspWildCard)
/*      */   {
/* 3139 */     addServletMappingDecoded(UDecoder.URLDecode(pattern, "UTF-8"), name, false);
/*      */   }
/*      */   
/*      */ 
/*      */   public void addServletMappingDecoded(String pattern, String name)
/*      */   {
/* 3145 */     addServletMappingDecoded(pattern, name, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addServletMappingDecoded(String pattern, String name, boolean jspWildCard)
/*      */   {
/* 3153 */     if (findChild(name) == null)
/*      */     {
/* 3155 */       throw new IllegalArgumentException(sm.getString("standardContext.servletMap.name", new Object[] { name })); }
/* 3156 */     String adjustedPattern = adjustURLPattern(pattern);
/* 3157 */     if (!validateURLPattern(adjustedPattern))
/*      */     {
/* 3159 */       throw new IllegalArgumentException(sm.getString("standardContext.servletMap.pattern", new Object[] { adjustedPattern }));
/*      */     }
/*      */     
/* 3162 */     synchronized (this.servletMappingsLock) {
/* 3163 */       String name2 = (String)this.servletMappings.get(adjustedPattern);
/* 3164 */       if (name2 != null)
/*      */       {
/* 3166 */         Wrapper wrapper = (Wrapper)findChild(name2);
/* 3167 */         wrapper.removeMapping(adjustedPattern);
/*      */       }
/* 3169 */       this.servletMappings.put(adjustedPattern, name);
/*      */     }
/* 3171 */     Wrapper wrapper = (Wrapper)findChild(name);
/* 3172 */     wrapper.addMapping(adjustedPattern);
/*      */     
/* 3174 */     fireContainerEvent("addServletMapping", adjustedPattern);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addWatchedResource(String name)
/*      */   {
/* 3186 */     synchronized (this.watchedResourcesLock) {
/* 3187 */       String[] results = new String[this.watchedResources.length + 1];
/* 3188 */       for (int i = 0; i < this.watchedResources.length; i++)
/* 3189 */         results[i] = this.watchedResources[i];
/* 3190 */       results[this.watchedResources.length] = name;
/* 3191 */       this.watchedResources = results;
/*      */     }
/* 3193 */     fireContainerEvent("addWatchedResource", name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addWelcomeFile(String name)
/*      */   {
/* 3205 */     synchronized (this.welcomeFilesLock)
/*      */     {
/*      */ 
/* 3208 */       if (this.replaceWelcomeFiles) {
/* 3209 */         fireContainerEvent("clearWelcomeFiles", null);
/* 3210 */         this.welcomeFiles = new String[0];
/* 3211 */         setReplaceWelcomeFiles(false);
/*      */       }
/* 3213 */       String[] results = new String[this.welcomeFiles.length + 1];
/* 3214 */       for (int i = 0; i < this.welcomeFiles.length; i++)
/* 3215 */         results[i] = this.welcomeFiles[i];
/* 3216 */       results[this.welcomeFiles.length] = name;
/* 3217 */       this.welcomeFiles = results;
/*      */     }
/* 3219 */     if (getState().equals(LifecycleState.STARTED)) {
/* 3220 */       fireContainerEvent("addWelcomeFile", name);
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
/*      */   public void addWrapperLifecycle(String listener)
/*      */   {
/* 3233 */     synchronized (this.wrapperLifecyclesLock) {
/* 3234 */       String[] results = new String[this.wrapperLifecycles.length + 1];
/* 3235 */       for (int i = 0; i < this.wrapperLifecycles.length; i++)
/* 3236 */         results[i] = this.wrapperLifecycles[i];
/* 3237 */       results[this.wrapperLifecycles.length] = listener;
/* 3238 */       this.wrapperLifecycles = results;
/*      */     }
/* 3240 */     fireContainerEvent("addWrapperLifecycle", listener);
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
/*      */   public void addWrapperListener(String listener)
/*      */   {
/* 3254 */     synchronized (this.wrapperListenersLock) {
/* 3255 */       String[] results = new String[this.wrapperListeners.length + 1];
/* 3256 */       for (int i = 0; i < this.wrapperListeners.length; i++)
/* 3257 */         results[i] = this.wrapperListeners[i];
/* 3258 */       results[this.wrapperListeners.length] = listener;
/* 3259 */       this.wrapperListeners = results;
/*      */     }
/* 3261 */     fireContainerEvent("addWrapperListener", listener);
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
/*      */   public Wrapper createWrapper()
/*      */   {
/* 3275 */     Wrapper wrapper = null;
/* 3276 */     if (this.wrapperClass != null) {
/*      */       try {
/* 3278 */         wrapper = (Wrapper)this.wrapperClass.getConstructor(new Class[0]).newInstance(new Object[0]);
/*      */       } catch (Throwable t) {
/* 3280 */         ExceptionUtils.handleThrowable(t);
/* 3281 */         log.error("createWrapper", t);
/* 3282 */         return null;
/*      */       }
/*      */     } else {
/* 3285 */       wrapper = new StandardWrapper();
/*      */     }
/*      */     
/* 3288 */     synchronized (this.wrapperLifecyclesLock) {
/* 3289 */       for (int i = 0; i < this.wrapperLifecycles.length; i++) {
/*      */         try {
/* 3291 */           Class<?> clazz = Class.forName(this.wrapperLifecycles[i]);
/*      */           
/* 3293 */           LifecycleListener listener = (LifecycleListener)clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/* 3294 */           wrapper.addLifecycleListener(listener);
/*      */         } catch (Throwable t) {
/* 3296 */           ExceptionUtils.handleThrowable(t);
/* 3297 */           log.error("createWrapper", t);
/* 3298 */           return null;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3303 */     synchronized (this.wrapperListenersLock) {
/* 3304 */       for (int i = 0; i < this.wrapperListeners.length; i++) {
/*      */         try {
/* 3306 */           Class<?> clazz = Class.forName(this.wrapperListeners[i]);
/*      */           
/* 3308 */           ContainerListener listener = (ContainerListener)clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/* 3309 */           wrapper.addContainerListener(listener);
/*      */         } catch (Throwable t) {
/* 3311 */           ExceptionUtils.handleThrowable(t);
/* 3312 */           log.error("createWrapper", t);
/* 3313 */           return null;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3318 */     return wrapper;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] findApplicationListeners()
/*      */   {
/* 3328 */     return this.applicationListeners;
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public ApplicationParameter[] findApplicationParameters()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 24	org/apache/catalina/core/StandardContext:applicationParametersLock	Ljava/lang/Object;
/*      */     //   4: dup
/*      */     //   5: astore_1
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 23	org/apache/catalina/core/StandardContext:applicationParameters	[Lorg/apache/tomcat/util/descriptor/web/ApplicationParameter;
/*      */     //   11: aload_1
/*      */     //   12: monitorexit
/*      */     //   13: areturn
/*      */     //   14: astore_2
/*      */     //   15: aload_1
/*      */     //   16: monitorexit
/*      */     //   17: aload_2
/*      */     //   18: athrow
/*      */     // Line number table:
/*      */     //   Java source line #3338	-> byte code offset #0
/*      */     //   Java source line #3339	-> byte code offset #7
/*      */     //   Java source line #3340	-> byte code offset #14
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	19	0	this	StandardContext
/*      */     //   5	11	1	Ljava/lang/Object;	Object
/*      */     //   14	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	13	14	finally
/*      */     //   14	17	14	finally
/*      */   }
/*      */   
/*      */   public SecurityConstraint[] findConstraints()
/*      */   {
/* 3352 */     return this.constraints;
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
/*      */   public ErrorPage findErrorPage(int errorCode)
/*      */   {
/* 3365 */     return (ErrorPage)this.statusPages.get(Integer.valueOf(errorCode));
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public ErrorPage findErrorPage(String exceptionType)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 46	org/apache/catalina/core/StandardContext:exceptionPages	Ljava/util/HashMap;
/*      */     //   4: dup
/*      */     //   5: astore_2
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 46	org/apache/catalina/core/StandardContext:exceptionPages	Ljava/util/HashMap;
/*      */     //   11: aload_1
/*      */     //   12: invokevirtual 413	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   15: checkcast 432	org/apache/tomcat/util/descriptor/web/ErrorPage
/*      */     //   18: aload_2
/*      */     //   19: monitorexit
/*      */     //   20: areturn
/*      */     //   21: astore_3
/*      */     //   22: aload_2
/*      */     //   23: monitorexit
/*      */     //   24: aload_3
/*      */     //   25: athrow
/*      */     // Line number table:
/*      */     //   Java source line #3378	-> byte code offset #0
/*      */     //   Java source line #3379	-> byte code offset #7
/*      */     //   Java source line #3380	-> byte code offset #21
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	26	0	this	StandardContext
/*      */     //   0	26	1	exceptionType	String
/*      */     //   5	18	2	Ljava/lang/Object;	Object
/*      */     //   21	4	3	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	20	21	finally
/*      */     //   21	24	21	finally
/*      */   }
/*      */   
/*      */   public ErrorPage[] findErrorPages()
/*      */   {
/* 3392 */     synchronized (this.exceptionPages) {
/* 3393 */       synchronized (this.statusPages) {
/* 3394 */         ErrorPage[] results1 = new ErrorPage[this.exceptionPages.size()];
/* 3395 */         results1 = (ErrorPage[])this.exceptionPages.values().toArray(results1);
/* 3396 */         ErrorPage[] results2 = new ErrorPage[this.statusPages.size()];
/* 3397 */         results2 = (ErrorPage[])this.statusPages.values().toArray(results2);
/* 3398 */         ErrorPage[] results = new ErrorPage[results1.length + results2.length];
/*      */         
/* 3400 */         for (int i = 0; i < results1.length; i++)
/* 3401 */           results[i] = results1[i];
/* 3402 */         for (int i = results1.length; i < results.length; i++)
/* 3403 */           results[i] = results2[(i - results1.length)];
/* 3404 */         return results;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public FilterDef findFilterDef(String filterName)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 48	org/apache/catalina/core/StandardContext:filterDefs	Ljava/util/HashMap;
/*      */     //   4: dup
/*      */     //   5: astore_2
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 48	org/apache/catalina/core/StandardContext:filterDefs	Ljava/util/HashMap;
/*      */     //   11: aload_1
/*      */     //   12: invokevirtual 413	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   15: checkcast 437	org/apache/tomcat/util/descriptor/web/FilterDef
/*      */     //   18: aload_2
/*      */     //   19: monitorexit
/*      */     //   20: areturn
/*      */     //   21: astore_3
/*      */     //   22: aload_2
/*      */     //   23: monitorexit
/*      */     //   24: aload_3
/*      */     //   25: athrow
/*      */     // Line number table:
/*      */     //   Java source line #3420	-> byte code offset #0
/*      */     //   Java source line #3421	-> byte code offset #7
/*      */     //   Java source line #3422	-> byte code offset #21
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	26	0	this	StandardContext
/*      */     //   0	26	1	filterName	String
/*      */     //   5	18	2	Ljava/lang/Object;	Object
/*      */     //   21	4	3	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	20	21	finally
/*      */     //   21	24	21	finally
/*      */   }
/*      */   
/*      */   public FilterDef[] findFilterDefs()
/*      */   {
/* 3433 */     synchronized (this.filterDefs) {
/* 3434 */       FilterDef[] results = new FilterDef[this.filterDefs.size()];
/* 3435 */       return (FilterDef[])this.filterDefs.values().toArray(results);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public FilterMap[] findFilterMaps()
/*      */   {
/* 3446 */     return this.filterMaps.asArray();
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public MessageDestination findMessageDestination(String name)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 62	org/apache/catalina/core/StandardContext:messageDestinations	Ljava/util/HashMap;
/*      */     //   4: dup
/*      */     //   5: astore_2
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 62	org/apache/catalina/core/StandardContext:messageDestinations	Ljava/util/HashMap;
/*      */     //   11: aload_1
/*      */     //   12: invokevirtual 413	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   15: checkcast 440	org/apache/tomcat/util/descriptor/web/MessageDestination
/*      */     //   18: aload_2
/*      */     //   19: monitorexit
/*      */     //   20: areturn
/*      */     //   21: astore_3
/*      */     //   22: aload_2
/*      */     //   23: monitorexit
/*      */     //   24: aload_3
/*      */     //   25: athrow
/*      */     // Line number table:
/*      */     //   Java source line #3458	-> byte code offset #0
/*      */     //   Java source line #3459	-> byte code offset #7
/*      */     //   Java source line #3460	-> byte code offset #21
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	26	0	this	StandardContext
/*      */     //   0	26	1	name	String
/*      */     //   5	18	2	Ljava/lang/Object;	Object
/*      */     //   21	4	3	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	20	21	finally
/*      */     //   21	24	21	finally
/*      */   }
/*      */   
/*      */   public MessageDestination[] findMessageDestinations()
/*      */   {
/* 3472 */     synchronized (this.messageDestinations)
/*      */     {
/* 3474 */       MessageDestination[] results = new MessageDestination[this.messageDestinations.size()];
/* 3475 */       return (MessageDestination[])this.messageDestinations.values().toArray(results);
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
/*      */   public MessageDestinationRef findMessageDestinationRef(String name)
/*      */   {
/* 3489 */     return this.namingResources.findMessageDestinationRef(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MessageDestinationRef[] findMessageDestinationRefs()
/*      */   {
/* 3501 */     return this.namingResources.findMessageDestinationRefs();
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
/*      */   public String findMimeMapping(String extension)
/*      */   {
/* 3515 */     return (String)this.mimeMappings.get(extension.toLowerCase(Locale.ENGLISH));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] findMimeMappings()
/*      */   {
/* 3527 */     synchronized (this.mimeMappings) {
/* 3528 */       String[] results = new String[this.mimeMappings.size()];
/* 3529 */       return 
/* 3530 */         (String[])this.mimeMappings.keySet().toArray(results);
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
/*      */   public String findParameter(String name)
/*      */   {
/* 3544 */     return (String)this.parameters.get(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] findParameters()
/*      */   {
/* 3555 */     List<String> parameterNames = new ArrayList(this.parameters.size());
/* 3556 */     parameterNames.addAll(this.parameters.keySet());
/* 3557 */     return (String[])parameterNames.toArray(new String[parameterNames.size()]);
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
/*      */   public String findRoleMapping(String role)
/*      */   {
/* 3572 */     String realRole = null;
/* 3573 */     synchronized (this.roleMappings) {
/* 3574 */       realRole = (String)this.roleMappings.get(role);
/*      */     }
/* 3576 */     if (realRole != null) {
/* 3577 */       return realRole;
/*      */     }
/* 3579 */     return role;
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
/*      */   public boolean findSecurityRole(String role)
/*      */   {
/* 3593 */     synchronized (this.securityRolesLock) {
/* 3594 */       for (int i = 0; i < this.securityRoles.length; i++) {
/* 3595 */         if (role.equals(this.securityRoles[i]))
/* 3596 */           return true;
/*      */       }
/*      */     }
/* 3599 */     return false;
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public String[] findSecurityRoles()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 78	org/apache/catalina/core/StandardContext:securityRolesLock	Ljava/lang/Object;
/*      */     //   4: dup
/*      */     //   5: astore_1
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 77	org/apache/catalina/core/StandardContext:securityRoles	[Ljava/lang/String;
/*      */     //   11: aload_1
/*      */     //   12: monitorexit
/*      */     //   13: areturn
/*      */     //   14: astore_2
/*      */     //   15: aload_1
/*      */     //   16: monitorexit
/*      */     //   17: aload_2
/*      */     //   18: athrow
/*      */     // Line number table:
/*      */     //   Java source line #3611	-> byte code offset #0
/*      */     //   Java source line #3612	-> byte code offset #7
/*      */     //   Java source line #3613	-> byte code offset #14
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	19	0	this	StandardContext
/*      */     //   5	11	1	Ljava/lang/Object;	Object
/*      */     //   14	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	13	14	finally
/*      */     //   14	17	14	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public String findServletMapping(String pattern)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 80	org/apache/catalina/core/StandardContext:servletMappingsLock	Ljava/lang/Object;
/*      */     //   4: dup
/*      */     //   5: astore_2
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 79	org/apache/catalina/core/StandardContext:servletMappings	Ljava/util/HashMap;
/*      */     //   11: aload_1
/*      */     //   12: invokevirtual 413	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   15: checkcast 7	java/lang/String
/*      */     //   18: aload_2
/*      */     //   19: monitorexit
/*      */     //   20: areturn
/*      */     //   21: astore_3
/*      */     //   22: aload_2
/*      */     //   23: monitorexit
/*      */     //   24: aload_3
/*      */     //   25: athrow
/*      */     // Line number table:
/*      */     //   Java source line #3627	-> byte code offset #0
/*      */     //   Java source line #3628	-> byte code offset #7
/*      */     //   Java source line #3629	-> byte code offset #21
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	26	0	this	StandardContext
/*      */     //   0	26	1	pattern	String
/*      */     //   5	18	2	Ljava/lang/Object;	Object
/*      */     //   21	4	3	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	20	21	finally
/*      */     //   21	24	21	finally
/*      */   }
/*      */   
/*      */   public String[] findServletMappings()
/*      */   {
/* 3641 */     synchronized (this.servletMappingsLock) {
/* 3642 */       String[] results = new String[this.servletMappings.size()];
/* 3643 */       return 
/* 3644 */         (String[])this.servletMappings.keySet().toArray(results);
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
/*      */   public String findStatusPage(int status)
/*      */   {
/* 3659 */     ErrorPage errorPage = (ErrorPage)this.statusPages.get(Integer.valueOf(status));
/* 3660 */     if (errorPage != null) {
/* 3661 */       return errorPage.getLocation();
/*      */     }
/* 3663 */     return null;
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
/*      */   public int[] findStatusPages()
/*      */   {
/* 3676 */     synchronized (this.statusPages) {
/* 3677 */       int[] results = new int[this.statusPages.size()];
/* 3678 */       Iterator<Integer> elements = this.statusPages.keySet().iterator();
/* 3679 */       int i = 0;
/* 3680 */       while (elements.hasNext())
/* 3681 */         results[(i++)] = ((Integer)elements.next()).intValue();
/* 3682 */       return results;
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
/*      */   public boolean findWelcomeFile(String name)
/*      */   {
/* 3697 */     synchronized (this.welcomeFilesLock) {
/* 3698 */       for (int i = 0; i < this.welcomeFiles.length; i++) {
/* 3699 */         if (name.equals(this.welcomeFiles[i]))
/* 3700 */           return true;
/*      */       }
/*      */     }
/* 3703 */     return false;
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public String[] findWatchedResources()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 91	org/apache/catalina/core/StandardContext:watchedResourcesLock	Ljava/lang/Object;
/*      */     //   4: dup
/*      */     //   5: astore_1
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 90	org/apache/catalina/core/StandardContext:watchedResources	[Ljava/lang/String;
/*      */     //   11: aload_1
/*      */     //   12: monitorexit
/*      */     //   13: areturn
/*      */     //   14: astore_2
/*      */     //   15: aload_1
/*      */     //   16: monitorexit
/*      */     //   17: aload_2
/*      */     //   18: athrow
/*      */     // Line number table:
/*      */     //   Java source line #3714	-> byte code offset #0
/*      */     //   Java source line #3715	-> byte code offset #7
/*      */     //   Java source line #3716	-> byte code offset #14
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	19	0	this	StandardContext
/*      */     //   5	11	1	Ljava/lang/Object;	Object
/*      */     //   14	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	13	14	finally
/*      */     //   14	17	14	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public String[] findWelcomeFiles()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 93	org/apache/catalina/core/StandardContext:welcomeFilesLock	Ljava/lang/Object;
/*      */     //   4: dup
/*      */     //   5: astore_1
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 92	org/apache/catalina/core/StandardContext:welcomeFiles	[Ljava/lang/String;
/*      */     //   11: aload_1
/*      */     //   12: monitorexit
/*      */     //   13: areturn
/*      */     //   14: astore_2
/*      */     //   15: aload_1
/*      */     //   16: monitorexit
/*      */     //   17: aload_2
/*      */     //   18: athrow
/*      */     // Line number table:
/*      */     //   Java source line #3727	-> byte code offset #0
/*      */     //   Java source line #3728	-> byte code offset #7
/*      */     //   Java source line #3729	-> byte code offset #14
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	19	0	this	StandardContext
/*      */     //   5	11	1	Ljava/lang/Object;	Object
/*      */     //   14	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	13	14	finally
/*      */     //   14	17	14	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public String[] findWrapperLifecycles()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 95	org/apache/catalina/core/StandardContext:wrapperLifecyclesLock	Ljava/lang/Object;
/*      */     //   4: dup
/*      */     //   5: astore_1
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 94	org/apache/catalina/core/StandardContext:wrapperLifecycles	[Ljava/lang/String;
/*      */     //   11: aload_1
/*      */     //   12: monitorexit
/*      */     //   13: areturn
/*      */     //   14: astore_2
/*      */     //   15: aload_1
/*      */     //   16: monitorexit
/*      */     //   17: aload_2
/*      */     //   18: athrow
/*      */     // Line number table:
/*      */     //   Java source line #3741	-> byte code offset #0
/*      */     //   Java source line #3742	-> byte code offset #7
/*      */     //   Java source line #3743	-> byte code offset #14
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	19	0	this	StandardContext
/*      */     //   5	11	1	Ljava/lang/Object;	Object
/*      */     //   14	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	13	14	finally
/*      */     //   14	17	14	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public String[] findWrapperListeners()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 97	org/apache/catalina/core/StandardContext:wrapperListenersLock	Ljava/lang/Object;
/*      */     //   4: dup
/*      */     //   5: astore_1
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 96	org/apache/catalina/core/StandardContext:wrapperListeners	[Ljava/lang/String;
/*      */     //   11: aload_1
/*      */     //   12: monitorexit
/*      */     //   13: areturn
/*      */     //   14: astore_2
/*      */     //   15: aload_1
/*      */     //   16: monitorexit
/*      */     //   17: aload_2
/*      */     //   18: athrow
/*      */     // Line number table:
/*      */     //   Java source line #3755	-> byte code offset #0
/*      */     //   Java source line #3756	-> byte code offset #7
/*      */     //   Java source line #3757	-> byte code offset #14
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	19	0	this	StandardContext
/*      */     //   5	11	1	Ljava/lang/Object;	Object
/*      */     //   14	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	13	14	finally
/*      */     //   14	17	14	finally
/*      */   }
/*      */   
/*      */   public synchronized void reload()
/*      */   {
/* 3781 */     if (!getState().isAvailable())
/*      */     {
/* 3783 */       throw new IllegalStateException(sm.getString("standardContext.notStarted", new Object[] {getName() }));
/*      */     }
/* 3785 */     if (log.isInfoEnabled()) {
/* 3786 */       log.info(sm.getString("standardContext.reloadingStarted", new Object[] {
/* 3787 */         getName() }));
/*      */     }
/*      */     
/* 3790 */     setPaused(true);
/*      */     try
/*      */     {
/* 3793 */       stop();
/*      */     } catch (LifecycleException e) {
/* 3795 */       log.error(sm
/* 3796 */         .getString("standardContext.stoppingContext", new Object[] {getName() }), e);
/*      */     }
/*      */     try
/*      */     {
/* 3800 */       start();
/*      */     } catch (LifecycleException e) {
/* 3802 */       log.error(sm
/* 3803 */         .getString("standardContext.startingContext", new Object[] {getName() }), e);
/*      */     }
/*      */     
/* 3806 */     setPaused(false);
/*      */     
/* 3808 */     if (log.isInfoEnabled()) {
/* 3809 */       log.info(sm.getString("standardContext.reloadingCompleted", new Object[] {
/* 3810 */         getName() }));
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
/*      */   public void removeApplicationListener(String listener)
/*      */   {
/* 3824 */     synchronized (this.applicationListenersLock)
/*      */     {
/*      */ 
/* 3827 */       int n = -1;
/* 3828 */       for (int i = 0; i < this.applicationListeners.length; i++) {
/* 3829 */         if (this.applicationListeners[i].equals(listener)) {
/* 3830 */           n = i;
/* 3831 */           break;
/*      */         }
/*      */       }
/* 3834 */       if (n < 0) {
/* 3835 */         return;
/*      */       }
/*      */       
/* 3838 */       int j = 0;
/* 3839 */       String[] results = new String[this.applicationListeners.length - 1];
/* 3840 */       for (int i = 0; i < this.applicationListeners.length; i++) {
/* 3841 */         if (i != n)
/* 3842 */           results[(j++)] = this.applicationListeners[i];
/*      */       }
/* 3844 */       this.applicationListeners = results;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 3849 */     fireContainerEvent("removeApplicationListener", listener);
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
/*      */   public void removeApplicationParameter(String name)
/*      */   {
/* 3864 */     synchronized (this.applicationParametersLock)
/*      */     {
/*      */ 
/* 3867 */       int n = -1;
/* 3868 */       for (int i = 0; i < this.applicationParameters.length; i++) {
/* 3869 */         if (name.equals(this.applicationParameters[i].getName())) {
/* 3870 */           n = i;
/* 3871 */           break;
/*      */         }
/*      */       }
/* 3874 */       if (n < 0) {
/* 3875 */         return;
/*      */       }
/*      */       
/* 3878 */       int j = 0;
/* 3879 */       ApplicationParameter[] results = new ApplicationParameter[this.applicationParameters.length - 1];
/*      */       
/* 3881 */       for (int i = 0; i < this.applicationParameters.length; i++) {
/* 3882 */         if (i != n)
/* 3883 */           results[(j++)] = this.applicationParameters[i];
/*      */       }
/* 3885 */       this.applicationParameters = results;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 3890 */     fireContainerEvent("removeApplicationParameter", name);
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
/*      */   public void removeChild(Container child)
/*      */   {
/* 3907 */     if (!(child instanceof Wrapper))
/*      */     {
/* 3909 */       throw new IllegalArgumentException(sm.getString("standardContext.notWrapper"));
/*      */     }
/*      */     
/* 3912 */     super.removeChild(child);
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
/*      */   public void removeConstraint(SecurityConstraint constraint)
/*      */   {
/* 3925 */     synchronized (this.constraintsLock)
/*      */     {
/*      */ 
/* 3928 */       int n = -1;
/* 3929 */       for (int i = 0; i < this.constraints.length; i++) {
/* 3930 */         if (this.constraints[i].equals(constraint)) {
/* 3931 */           n = i;
/* 3932 */           break;
/*      */         }
/*      */       }
/* 3935 */       if (n < 0) {
/* 3936 */         return;
/*      */       }
/*      */       
/* 3939 */       int j = 0;
/* 3940 */       SecurityConstraint[] results = new SecurityConstraint[this.constraints.length - 1];
/*      */       
/* 3942 */       for (int i = 0; i < this.constraints.length; i++) {
/* 3943 */         if (i != n)
/* 3944 */           results[(j++)] = this.constraints[i];
/*      */       }
/* 3946 */       this.constraints = results;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 3951 */     fireContainerEvent("removeConstraint", constraint);
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
/*      */   public void removeErrorPage(ErrorPage errorPage)
/*      */   {
/* 3965 */     String exceptionType = errorPage.getExceptionType();
/* 3966 */     if (exceptionType != null) {
/* 3967 */       synchronized (this.exceptionPages) {
/* 3968 */         this.exceptionPages.remove(exceptionType);
/*      */       }
/*      */     } else {
/* 3971 */       synchronized (this.statusPages) {
/* 3972 */         this.statusPages.remove(Integer.valueOf(errorPage.getErrorCode()));
/*      */       }
/*      */     }
/* 3975 */     fireContainerEvent("removeErrorPage", errorPage);
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
/*      */   public void removeFilterDef(FilterDef filterDef)
/*      */   {
/* 3989 */     synchronized (this.filterDefs) {
/* 3990 */       this.filterDefs.remove(filterDef.getFilterName());
/*      */     }
/* 3992 */     fireContainerEvent("removeFilterDef", filterDef);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeFilterMap(FilterMap filterMap)
/*      */   {
/* 4004 */     this.filterMaps.remove(filterMap);
/*      */     
/* 4006 */     fireContainerEvent("removeFilterMap", filterMap);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeMessageDestination(String name)
/*      */   {
/* 4017 */     synchronized (this.messageDestinations) {
/* 4018 */       this.messageDestinations.remove(name);
/*      */     }
/* 4020 */     fireContainerEvent("removeMessageDestination", name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeMessageDestinationRef(String name)
/*      */   {
/* 4032 */     this.namingResources.removeMessageDestinationRef(name);
/* 4033 */     fireContainerEvent("removeMessageDestinationRef", name);
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
/*      */   public void removeMimeMapping(String extension)
/*      */   {
/* 4047 */     synchronized (this.mimeMappings) {
/* 4048 */       this.mimeMappings.remove(extension);
/*      */     }
/* 4050 */     fireContainerEvent("removeMimeMapping", extension);
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
/*      */   public void removeParameter(String name)
/*      */   {
/* 4063 */     this.parameters.remove(name);
/* 4064 */     fireContainerEvent("removeParameter", name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeRoleMapping(String role)
/*      */   {
/* 4076 */     synchronized (this.roleMappings) {
/* 4077 */       this.roleMappings.remove(role);
/*      */     }
/* 4079 */     fireContainerEvent("removeRoleMapping", role);
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
/*      */   public void removeSecurityRole(String role)
/*      */   {
/* 4092 */     synchronized (this.securityRolesLock)
/*      */     {
/*      */ 
/* 4095 */       int n = -1;
/* 4096 */       for (int i = 0; i < this.securityRoles.length; i++) {
/* 4097 */         if (role.equals(this.securityRoles[i])) {
/* 4098 */           n = i;
/* 4099 */           break;
/*      */         }
/*      */       }
/* 4102 */       if (n < 0) {
/* 4103 */         return;
/*      */       }
/*      */       
/* 4106 */       int j = 0;
/* 4107 */       String[] results = new String[this.securityRoles.length - 1];
/* 4108 */       for (int i = 0; i < this.securityRoles.length; i++) {
/* 4109 */         if (i != n)
/* 4110 */           results[(j++)] = this.securityRoles[i];
/*      */       }
/* 4112 */       this.securityRoles = results;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 4117 */     fireContainerEvent("removeSecurityRole", role);
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
/*      */   public void removeServletMapping(String pattern)
/*      */   {
/* 4131 */     String name = null;
/* 4132 */     synchronized (this.servletMappingsLock) {
/* 4133 */       name = (String)this.servletMappings.remove(pattern);
/*      */     }
/* 4135 */     Wrapper wrapper = (Wrapper)findChild(name);
/* 4136 */     if (wrapper != null) {
/* 4137 */       wrapper.removeMapping(pattern);
/*      */     }
/* 4139 */     fireContainerEvent("removeServletMapping", pattern);
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
/*      */   public void removeWatchedResource(String name)
/*      */   {
/* 4152 */     synchronized (this.watchedResourcesLock)
/*      */     {
/*      */ 
/* 4155 */       int n = -1;
/* 4156 */       for (int i = 0; i < this.watchedResources.length; i++) {
/* 4157 */         if (this.watchedResources[i].equals(name)) {
/* 4158 */           n = i;
/* 4159 */           break;
/*      */         }
/*      */       }
/* 4162 */       if (n < 0) {
/* 4163 */         return;
/*      */       }
/*      */       
/* 4166 */       int j = 0;
/* 4167 */       String[] results = new String[this.watchedResources.length - 1];
/* 4168 */       for (int i = 0; i < this.watchedResources.length; i++) {
/* 4169 */         if (i != n)
/* 4170 */           results[(j++)] = this.watchedResources[i];
/*      */       }
/* 4172 */       this.watchedResources = results;
/*      */     }
/*      */     
/*      */ 
/* 4176 */     fireContainerEvent("removeWatchedResource", name);
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
/*      */   public void removeWelcomeFile(String name)
/*      */   {
/* 4190 */     synchronized (this.welcomeFilesLock)
/*      */     {
/*      */ 
/* 4193 */       int n = -1;
/* 4194 */       for (int i = 0; i < this.welcomeFiles.length; i++) {
/* 4195 */         if (this.welcomeFiles[i].equals(name)) {
/* 4196 */           n = i;
/* 4197 */           break;
/*      */         }
/*      */       }
/* 4200 */       if (n < 0) {
/* 4201 */         return;
/*      */       }
/*      */       
/* 4204 */       int j = 0;
/* 4205 */       String[] results = new String[this.welcomeFiles.length - 1];
/* 4206 */       for (int i = 0; i < this.welcomeFiles.length; i++) {
/* 4207 */         if (i != n)
/* 4208 */           results[(j++)] = this.welcomeFiles[i];
/*      */       }
/* 4210 */       this.welcomeFiles = results;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 4215 */     if (getState().equals(LifecycleState.STARTED)) {
/* 4216 */       fireContainerEvent("removeWelcomeFile", name);
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
/*      */   public void removeWrapperLifecycle(String listener)
/*      */   {
/* 4231 */     synchronized (this.wrapperLifecyclesLock)
/*      */     {
/*      */ 
/* 4234 */       int n = -1;
/* 4235 */       for (int i = 0; i < this.wrapperLifecycles.length; i++) {
/* 4236 */         if (this.wrapperLifecycles[i].equals(listener)) {
/* 4237 */           n = i;
/* 4238 */           break;
/*      */         }
/*      */       }
/* 4241 */       if (n < 0) {
/* 4242 */         return;
/*      */       }
/*      */       
/* 4245 */       int j = 0;
/* 4246 */       String[] results = new String[this.wrapperLifecycles.length - 1];
/* 4247 */       for (int i = 0; i < this.wrapperLifecycles.length; i++) {
/* 4248 */         if (i != n)
/* 4249 */           results[(j++)] = this.wrapperLifecycles[i];
/*      */       }
/* 4251 */       this.wrapperLifecycles = results;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 4256 */     fireContainerEvent("removeWrapperLifecycle", listener);
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
/*      */   public void removeWrapperListener(String listener)
/*      */   {
/* 4271 */     synchronized (this.wrapperListenersLock)
/*      */     {
/*      */ 
/* 4274 */       int n = -1;
/* 4275 */       for (int i = 0; i < this.wrapperListeners.length; i++) {
/* 4276 */         if (this.wrapperListeners[i].equals(listener)) {
/* 4277 */           n = i;
/* 4278 */           break;
/*      */         }
/*      */       }
/* 4281 */       if (n < 0) {
/* 4282 */         return;
/*      */       }
/*      */       
/* 4285 */       int j = 0;
/* 4286 */       String[] results = new String[this.wrapperListeners.length - 1];
/* 4287 */       for (int i = 0; i < this.wrapperListeners.length; i++) {
/* 4288 */         if (i != n)
/* 4289 */           results[(j++)] = this.wrapperListeners[i];
/*      */       }
/* 4291 */       this.wrapperListeners = results;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 4296 */     fireContainerEvent("removeWrapperListener", listener);
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
/*      */   public long getProcessingTime()
/*      */   {
/* 4310 */     long result = 0L;
/*      */     
/* 4312 */     Container[] children = findChildren();
/* 4313 */     if (children != null) {
/* 4314 */       for (int i = 0; i < children.length; i++) {
/* 4315 */         result += ((StandardWrapper)children[i]).getProcessingTime();
/*      */       }
/*      */     }
/*      */     
/* 4319 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getMaxTime()
/*      */   {
/* 4331 */     long result = 0L;
/*      */     
/*      */ 
/* 4334 */     Container[] children = findChildren();
/* 4335 */     if (children != null) {
/* 4336 */       for (int i = 0; i < children.length; i++) {
/* 4337 */         long time = ((StandardWrapper)children[i]).getMaxTime();
/* 4338 */         if (time > result) {
/* 4339 */           result = time;
/*      */         }
/*      */       }
/*      */     }
/* 4343 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getMinTime()
/*      */   {
/* 4355 */     long result = -1L;
/*      */     
/*      */ 
/* 4358 */     Container[] children = findChildren();
/* 4359 */     if (children != null) {
/* 4360 */       for (int i = 0; i < children.length; i++) {
/* 4361 */         long time = ((StandardWrapper)children[i]).getMinTime();
/* 4362 */         if ((result < 0L) || (time < result)) {
/* 4363 */           result = time;
/*      */         }
/*      */       }
/*      */     }
/* 4367 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getRequestCount()
/*      */   {
/* 4379 */     int result = 0;
/*      */     
/* 4381 */     Container[] children = findChildren();
/* 4382 */     if (children != null) {
/* 4383 */       for (int i = 0; i < children.length; i++) {
/* 4384 */         result += ((StandardWrapper)children[i]).getRequestCount();
/*      */       }
/*      */     }
/*      */     
/* 4388 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getErrorCount()
/*      */   {
/* 4400 */     int result = 0;
/*      */     
/* 4402 */     Container[] children = findChildren();
/* 4403 */     if (children != null) {
/* 4404 */       for (int i = 0; i < children.length; i++) {
/* 4405 */         result += ((StandardWrapper)children[i]).getErrorCount();
/*      */       }
/*      */     }
/*      */     
/* 4409 */     return result;
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
/*      */   public String getRealPath(String path)
/*      */   {
/* 4423 */     if ("".equals(path)) {
/* 4424 */       path = "/";
/*      */     }
/* 4426 */     if (this.resources != null) {
/*      */       try {
/* 4428 */         WebResource resource = this.resources.getResource(path);
/* 4429 */         String canonicalPath = resource.getCanonicalPath();
/* 4430 */         if (canonicalPath == null)
/* 4431 */           return null;
/* 4432 */         if (((resource.isDirectory()) && (!canonicalPath.endsWith(File.separator))) || (
/* 4433 */           (!resource.exists()) && (path.endsWith("/")))) {
/* 4434 */           return canonicalPath + File.separatorChar;
/*      */         }
/* 4436 */         return canonicalPath;
/*      */       }
/*      */       catch (IllegalArgumentException localIllegalArgumentException) {}
/*      */     }
/*      */     
/*      */ 
/* 4442 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ServletRegistration.Dynamic dynamicServletAdded(Wrapper wrapper)
/*      */   {
/* 4451 */     Servlet s = wrapper.getServlet();
/* 4452 */     if ((s != null) && (this.createdServlets.contains(s)))
/*      */     {
/* 4454 */       wrapper.setServletSecurityAnnotationScanRequired(true);
/*      */     }
/* 4456 */     return new ApplicationServletRegistration(wrapper, this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void dynamicServletCreated(Servlet servlet)
/*      */   {
/* 4464 */     this.createdServlets.add(servlet);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final class ContextFilterMaps
/*      */   {
/* 4472 */     private final Object lock = new Object();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4480 */     private FilterMap[] array = new FilterMap[0];
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4491 */     private int insertPoint = 0;
/*      */     
/*      */     /* Error */
/*      */     public FilterMap[] asArray()
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 4	org/apache/catalina/core/StandardContext$ContextFilterMaps:lock	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_1
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: getfield 6	org/apache/catalina/core/StandardContext$ContextFilterMaps:array	[Lorg/apache/tomcat/util/descriptor/web/FilterMap;
/*      */       //   11: aload_1
/*      */       //   12: monitorexit
/*      */       //   13: areturn
/*      */       //   14: astore_2
/*      */       //   15: aload_1
/*      */       //   16: monitorexit
/*      */       //   17: aload_2
/*      */       //   18: athrow
/*      */       // Line number table:
/*      */       //   Java source line #4497	-> byte code offset #0
/*      */       //   Java source line #4498	-> byte code offset #7
/*      */       //   Java source line #4499	-> byte code offset #14
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	19	0	this	ContextFilterMaps
/*      */       //   5	11	1	Ljava/lang/Object;	Object
/*      */       //   14	4	2	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	13	14	finally
/*      */       //   14	17	14	finally
/*      */     }
/*      */     
/*      */     public void add(FilterMap filterMap)
/*      */     {
/* 4510 */       synchronized (this.lock) {
/* 4511 */         FilterMap[] results = (FilterMap[])Arrays.copyOf(this.array, this.array.length + 1);
/* 4512 */         results[this.array.length] = filterMap;
/* 4513 */         this.array = results;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void addBefore(FilterMap filterMap)
/*      */     {
/* 4525 */       synchronized (this.lock) {
/* 4526 */         FilterMap[] results = new FilterMap[this.array.length + 1];
/* 4527 */         System.arraycopy(this.array, 0, results, 0, this.insertPoint);
/* 4528 */         System.arraycopy(this.array, this.insertPoint, results, this.insertPoint + 1, this.array.length - this.insertPoint);
/*      */         
/* 4530 */         results[this.insertPoint] = filterMap;
/* 4531 */         this.array = results;
/* 4532 */         this.insertPoint += 1;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void remove(FilterMap filterMap)
/*      */     {
/* 4542 */       synchronized (this.lock)
/*      */       {
/* 4544 */         int n = -1;
/* 4545 */         for (int i = 0; i < this.array.length; i++) {
/* 4546 */           if (this.array[i] == filterMap) {
/* 4547 */             n = i;
/* 4548 */             break;
/*      */           }
/*      */         }
/* 4551 */         if (n < 0) {
/* 4552 */           return;
/*      */         }
/*      */         
/* 4555 */         FilterMap[] results = new FilterMap[this.array.length - 1];
/* 4556 */         System.arraycopy(this.array, 0, results, 0, n);
/* 4557 */         System.arraycopy(this.array, n + 1, results, n, this.array.length - 1 - n);
/*      */         
/* 4559 */         this.array = results;
/* 4560 */         if (n < this.insertPoint) {
/* 4561 */           this.insertPoint -= 1;
/*      */         }
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
/*      */   public boolean filterStart()
/*      */   {
/* 4577 */     if (getLogger().isDebugEnabled()) {
/* 4578 */       getLogger().debug("Starting filters");
/*      */     }
/*      */     
/* 4581 */     boolean ok = true;
/* 4582 */     synchronized (this.filterConfigs) {
/* 4583 */       this.filterConfigs.clear();
/* 4584 */       for (Map.Entry<String, FilterDef> entry : this.filterDefs.entrySet()) {
/* 4585 */         String name = (String)entry.getKey();
/* 4586 */         if (getLogger().isDebugEnabled()) {
/* 4587 */           getLogger().debug(" Starting filter '" + name + "'");
/*      */         }
/*      */         try
/*      */         {
/* 4591 */           ApplicationFilterConfig filterConfig = new ApplicationFilterConfig(this, (FilterDef)entry.getValue());
/* 4592 */           this.filterConfigs.put(name, filterConfig);
/*      */         } catch (Throwable t) {
/* 4594 */           t = ExceptionUtils.unwrapInvocationTargetException(t);
/* 4595 */           ExceptionUtils.handleThrowable(t);
/* 4596 */           getLogger().error(sm.getString("standardContext.filterStart", new Object[] { name }), t);
/*      */           
/* 4598 */           ok = false;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 4603 */     return ok;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean filterStop()
/*      */   {
/* 4614 */     if (getLogger().isDebugEnabled()) {
/* 4615 */       getLogger().debug("Stopping filters");
/*      */     }
/*      */     
/* 4618 */     synchronized (this.filterConfigs) {
/* 4619 */       for (Map.Entry<String, ApplicationFilterConfig> entry : this.filterConfigs.entrySet()) {
/* 4620 */         if (getLogger().isDebugEnabled())
/* 4621 */           getLogger().debug(" Stopping filter '" + (String)entry.getKey() + "'");
/* 4622 */         ApplicationFilterConfig filterConfig = (ApplicationFilterConfig)entry.getValue();
/* 4623 */         filterConfig.release();
/*      */       }
/* 4625 */       this.filterConfigs.clear();
/*      */     }
/* 4627 */     return true;
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
/*      */   public FilterConfig findFilterConfig(String name)
/*      */   {
/* 4641 */     return (FilterConfig)this.filterConfigs.get(name);
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
/*      */   public boolean listenerStart()
/*      */   {
/* 4654 */     if (log.isDebugEnabled()) {
/* 4655 */       log.debug("Configuring application event listeners");
/*      */     }
/*      */     
/* 4658 */     String[] listeners = findApplicationListeners();
/* 4659 */     Object[] results = new Object[listeners.length];
/* 4660 */     boolean ok = true;
/* 4661 */     for (int i = 0; i < results.length; i++) {
/* 4662 */       if (getLogger().isDebugEnabled()) {
/* 4663 */         getLogger().debug(" Configuring event listener class '" + listeners[i] + "'");
/*      */       }
/*      */       try {
/* 4666 */         String listener = listeners[i];
/* 4667 */         results[i] = getInstanceManager().newInstance(listener);
/*      */       } catch (Throwable t) {
/* 4669 */         t = ExceptionUtils.unwrapInvocationTargetException(t);
/* 4670 */         ExceptionUtils.handleThrowable(t);
/* 4671 */         getLogger().error(sm.getString("standardContext.applicationListener", new Object[] { listeners[i] }), t);
/*      */         
/* 4673 */         ok = false;
/*      */       }
/*      */     }
/* 4676 */     if (!ok) {
/* 4677 */       getLogger().error(sm.getString("standardContext.applicationSkipped"));
/* 4678 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 4682 */     ArrayList<Object> eventListeners = new ArrayList();
/* 4683 */     ArrayList<Object> lifecycleListeners = new ArrayList();
/* 4684 */     for (int i = 0; i < results.length; i++) {
/* 4685 */       if (((results[i] instanceof ServletContextAttributeListener)) || ((results[i] instanceof ServletRequestAttributeListener)) || ((results[i] instanceof ServletRequestListener)) || ((results[i] instanceof HttpSessionIdListener)) || ((results[i] instanceof HttpSessionAttributeListener)))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 4690 */         eventListeners.add(results[i]);
/*      */       }
/* 4692 */       if (((results[i] instanceof ServletContextListener)) || ((results[i] instanceof HttpSessionListener)))
/*      */       {
/* 4694 */         lifecycleListeners.add(results[i]);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4703 */     for (Object eventListener : getApplicationEventListeners()) {
/* 4704 */       eventListeners.add(eventListener);
/*      */     }
/* 4706 */     setApplicationEventListeners(eventListeners.toArray());
/* 4707 */     for (Object lifecycleListener : getApplicationLifecycleListeners()) {
/* 4708 */       lifecycleListeners.add(lifecycleListener);
/* 4709 */       if ((lifecycleListener instanceof ServletContextListener)) {
/* 4710 */         this.noPluggabilityListeners.add(lifecycleListener);
/*      */       }
/*      */     }
/* 4713 */     setApplicationLifecycleListeners(lifecycleListeners.toArray());
/*      */     
/*      */ 
/*      */ 
/* 4717 */     if (getLogger().isDebugEnabled()) {
/* 4718 */       getLogger().debug("Sending application start events");
/*      */     }
/*      */     
/* 4721 */     getServletContext();
/* 4722 */     this.context.setNewServletContextListenerAllowed(false);
/*      */     
/* 4724 */     Object[] instances = getApplicationLifecycleListeners();
/* 4725 */     if ((instances == null) || (instances.length == 0)) {
/* 4726 */       return ok;
/*      */     }
/*      */     
/* 4729 */     ServletContextEvent event = new ServletContextEvent(getServletContext());
/* 4730 */     ServletContextEvent tldEvent = null;
/* 4731 */     if (this.noPluggabilityListeners.size() > 0) {
/* 4732 */       this.noPluggabilityServletContext = new NoPluggabilityServletContext(getServletContext());
/* 4733 */       tldEvent = new ServletContextEvent(this.noPluggabilityServletContext);
/*      */     }
/* 4735 */     for (int i = 0; i < instances.length; i++)
/* 4736 */       if ((instances[i] instanceof ServletContextListener))
/*      */       {
/* 4738 */         ServletContextListener listener = (ServletContextListener)instances[i];
/*      */         try
/*      */         {
/* 4741 */           fireContainerEvent("beforeContextInitialized", listener);
/* 4742 */           if (this.noPluggabilityListeners.contains(listener)) {
/* 4743 */             listener.contextInitialized(tldEvent);
/*      */           } else {
/* 4745 */             listener.contextInitialized(event);
/*      */           }
/* 4747 */           fireContainerEvent("afterContextInitialized", listener);
/*      */         } catch (Throwable t) {
/* 4749 */           ExceptionUtils.handleThrowable(t);
/* 4750 */           fireContainerEvent("afterContextInitialized", listener);
/* 4751 */           getLogger()
/* 4752 */             .error(sm.getString("standardContext.listenerStart", new Object[] {instances[i]
/* 4753 */             .getClass().getName() }), t);
/* 4754 */           ok = false;
/*      */         }
/*      */       }
/* 4757 */     return ok;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean listenerStop()
/*      */   {
/* 4769 */     if (log.isDebugEnabled()) {
/* 4770 */       log.debug("Sending application stop events");
/*      */     }
/* 4772 */     boolean ok = true;
/* 4773 */     Object[] listeners = getApplicationLifecycleListeners();
/* 4774 */     if ((listeners != null) && (listeners.length > 0)) {
/* 4775 */       ServletContextEvent event = new ServletContextEvent(getServletContext());
/* 4776 */       ServletContextEvent tldEvent = null;
/* 4777 */       if (this.noPluggabilityServletContext != null) {
/* 4778 */         tldEvent = new ServletContextEvent(this.noPluggabilityServletContext);
/*      */       }
/* 4780 */       for (int i = 0; i < listeners.length; i++) {
/* 4781 */         int j = listeners.length - 1 - i;
/* 4782 */         if (listeners[j] != null)
/*      */         {
/* 4784 */           if ((listeners[j] instanceof ServletContextListener)) {
/* 4785 */             ServletContextListener listener = (ServletContextListener)listeners[j];
/*      */             try
/*      */             {
/* 4788 */               fireContainerEvent("beforeContextDestroyed", listener);
/* 4789 */               if (this.noPluggabilityListeners.contains(listener)) {
/* 4790 */                 listener.contextDestroyed(tldEvent);
/*      */               } else {
/* 4792 */                 listener.contextDestroyed(event);
/*      */               }
/* 4794 */               fireContainerEvent("afterContextDestroyed", listener);
/*      */             } catch (Throwable t) {
/* 4796 */               ExceptionUtils.handleThrowable(t);
/* 4797 */               fireContainerEvent("afterContextDestroyed", listener);
/* 4798 */               getLogger()
/* 4799 */                 .error(sm.getString("standardContext.listenerStop", new Object[] {listeners[j]
/* 4800 */                 .getClass().getName() }), t);
/* 4801 */               ok = false;
/*      */             }
/*      */           }
/*      */           try {
/* 4805 */             if (getInstanceManager() != null) {
/* 4806 */               getInstanceManager().destroyInstance(listeners[j]);
/*      */             }
/*      */           } catch (Throwable t) {
/* 4809 */             t = ExceptionUtils.unwrapInvocationTargetException(t);
/* 4810 */             ExceptionUtils.handleThrowable(t);
/* 4811 */             getLogger()
/* 4812 */               .error(sm.getString("standardContext.listenerStop", new Object[] {listeners[j]
/* 4813 */               .getClass().getName() }), t);
/* 4814 */             ok = false;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 4820 */     listeners = getApplicationEventListeners();
/* 4821 */     if (listeners != null) {
/* 4822 */       for (int i = 0; i < listeners.length; i++) {
/* 4823 */         int j = listeners.length - 1 - i;
/* 4824 */         if (listeners[j] != null) {
/*      */           try
/*      */           {
/* 4827 */             if (getInstanceManager() != null) {
/* 4828 */               getInstanceManager().destroyInstance(listeners[j]);
/*      */             }
/*      */           } catch (Throwable t) {
/* 4831 */             t = ExceptionUtils.unwrapInvocationTargetException(t);
/* 4832 */             ExceptionUtils.handleThrowable(t);
/* 4833 */             getLogger()
/* 4834 */               .error(sm.getString("standardContext.listenerStop", new Object[] {listeners[j]
/* 4835 */               .getClass().getName() }), t);
/* 4836 */             ok = false;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 4841 */     setApplicationEventListeners(null);
/* 4842 */     setApplicationLifecycleListeners(null);
/*      */     
/* 4844 */     this.noPluggabilityServletContext = null;
/* 4845 */     this.noPluggabilityListeners.clear();
/*      */     
/* 4847 */     return ok;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void resourcesStart()
/*      */     throws LifecycleException
/*      */   {
/* 4859 */     if (!this.resources.getState().isAvailable()) {
/* 4860 */       this.resources.start();
/*      */     }
/*      */     
/* 4863 */     if ((this.effectiveMajorVersion >= 3) && (this.addWebinfClassesResources)) {
/* 4864 */       WebResource webinfClassesResource = this.resources.getResource("/WEB-INF/classes/META-INF/resources");
/*      */       
/* 4866 */       if (webinfClassesResource.isDirectory()) {
/* 4867 */         getResources().createWebResourceSet(WebResourceRoot.ResourceSetType.RESOURCE_JAR, "/", webinfClassesResource
/*      */         
/* 4869 */           .getURL(), "/");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean resourcesStop()
/*      */   {
/* 4881 */     boolean ok = true;
/*      */     
/* 4883 */     Lock writeLock = this.resourcesLock.writeLock();
/* 4884 */     writeLock.lock();
/*      */     try {
/* 4886 */       if (this.resources != null) {
/* 4887 */         this.resources.stop();
/*      */       }
/*      */     } catch (Throwable t) {
/* 4890 */       ExceptionUtils.handleThrowable(t);
/* 4891 */       log.error(sm.getString("standardContext.resourcesStop"), t);
/* 4892 */       ok = false;
/*      */     } finally {
/* 4894 */       writeLock.unlock();
/*      */     }
/*      */     
/* 4897 */     return ok;
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
/*      */   public boolean loadOnStartup(Container[] children)
/*      */   {
/* 4912 */     TreeMap<Integer, ArrayList<Wrapper>> map = new TreeMap();
/* 4913 */     int loadOnStartup; for (int i = 0; i < children.length; i++) {
/* 4914 */       Wrapper wrapper = (Wrapper)children[i];
/* 4915 */       loadOnStartup = wrapper.getLoadOnStartup();
/* 4916 */       if (loadOnStartup >= 0)
/*      */       {
/* 4918 */         Integer key = Integer.valueOf(loadOnStartup);
/* 4919 */         ArrayList<Wrapper> list = (ArrayList)map.get(key);
/* 4920 */         if (list == null) {
/* 4921 */           list = new ArrayList();
/* 4922 */           map.put(key, list);
/*      */         }
/* 4924 */         list.add(wrapper);
/*      */       }
/*      */     }
/*      */     
/* 4928 */     for (ArrayList<Wrapper> list : map.values()) {
/* 4929 */       for (Wrapper wrapper : list) {
/*      */         try {
/* 4931 */           wrapper.load();
/*      */         } catch (ServletException e) {
/* 4933 */           getLogger().error(sm.getString("standardContext.loadOnStartup.loadException", new Object[] {
/* 4934 */             getName(), wrapper.getName() }), StandardWrapper.getRootCause(e));
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 4939 */           if (getComputedFailCtxIfServletStartFails()) {
/* 4940 */             return false;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 4945 */     return true;
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
/*      */   protected synchronized void startInternal()
/*      */     throws LifecycleException
/*      */   {
/* 4960 */     if (log.isDebugEnabled()) {
/* 4961 */       log.debug("Starting " + getBaseName());
/*      */     }
/*      */     
/* 4964 */     if (getObjectName() != null)
/*      */     {
/* 4966 */       Notification notification = new Notification("j2ee.state.starting", getObjectName(), this.sequenceNumber.getAndIncrement());
/* 4967 */       this.broadcaster.sendNotification(notification);
/*      */     }
/*      */     
/* 4970 */     setConfigured(false);
/* 4971 */     boolean ok = true;
/*      */     
/*      */ 
/*      */ 
/* 4975 */     if (this.namingResources != null) {
/* 4976 */       this.namingResources.start();
/*      */     }
/*      */     
/*      */ 
/* 4980 */     postWorkDirectory();
/*      */     
/*      */ 
/* 4983 */     if (getResources() == null) {
/* 4984 */       if (log.isDebugEnabled()) {
/* 4985 */         log.debug("Configuring default Resources");
/*      */       }
/*      */       try {
/* 4988 */         setResources(new StandardRoot(this));
/*      */       } catch (IllegalArgumentException e) {
/* 4990 */         log.error(sm.getString("standardContext.resourcesInit"), e);
/* 4991 */         ok = false;
/*      */       }
/*      */     }
/* 4994 */     if (ok) {
/* 4995 */       resourcesStart();
/*      */     }
/*      */     
/* 4998 */     if (getLoader() == null) {
/* 4999 */       WebappLoader webappLoader = new WebappLoader(getParentClassLoader());
/* 5000 */       webappLoader.setDelegate(getDelegate());
/* 5001 */       setLoader(webappLoader);
/*      */     }
/*      */     
/*      */ 
/* 5005 */     if (this.cookieProcessor == null) {
/* 5006 */       this.cookieProcessor = new Rfc6265CookieProcessor();
/*      */     }
/*      */     
/*      */ 
/* 5010 */     getCharsetMapper();
/*      */     
/*      */ 
/* 5013 */     boolean dependencyCheck = true;
/*      */     try
/*      */     {
/* 5016 */       dependencyCheck = ExtensionValidator.validateApplication(getResources(), this);
/*      */     } catch (IOException ioe) {
/* 5018 */       log.error(sm.getString("standardContext.extensionValidationError"), ioe);
/* 5019 */       dependencyCheck = false;
/*      */     }
/*      */     
/* 5022 */     if (!dependencyCheck)
/*      */     {
/* 5024 */       ok = false;
/*      */     }
/*      */     
/*      */ 
/* 5028 */     String useNamingProperty = System.getProperty("catalina.useNaming");
/* 5029 */     if ((useNamingProperty != null) && 
/* 5030 */       (useNamingProperty.equals("false"))) {
/* 5031 */       this.useNaming = false;
/*      */     }
/*      */     
/* 5034 */     if ((ok) && (isUseNaming()) && 
/* 5035 */       (getNamingContextListener() == null)) {
/* 5036 */       NamingContextListener ncl = new NamingContextListener();
/* 5037 */       ncl.setName(getNamingContextName());
/* 5038 */       ncl.setExceptionOnFailedWrite(getJndiExceptionOnFailedWrite());
/* 5039 */       addLifecycleListener(ncl);
/* 5040 */       setNamingContextListener(ncl);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 5045 */     if (log.isDebugEnabled()) {
/* 5046 */       log.debug("Processing standard container startup");
/*      */     }
/*      */     
/*      */ 
/* 5050 */     ClassLoader oldCCL = bindThread();
/*      */     try
/*      */     {
/* 5053 */       if (ok)
/*      */       {
/* 5055 */         Loader loader = getLoader();
/* 5056 */         if ((loader instanceof Lifecycle)) {
/* 5057 */           ((Lifecycle)loader).start();
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 5062 */         setClassLoaderProperty("clearReferencesRmiTargets", 
/* 5063 */           getClearReferencesRmiTargets());
/* 5064 */         setClassLoaderProperty("clearReferencesStopThreads", 
/* 5065 */           getClearReferencesStopThreads());
/* 5066 */         setClassLoaderProperty("clearReferencesStopTimerThreads", 
/* 5067 */           getClearReferencesStopTimerThreads());
/* 5068 */         setClassLoaderProperty("clearReferencesHttpClientKeepAliveThread", 
/* 5069 */           getClearReferencesHttpClientKeepAliveThread());
/*      */         
/*      */ 
/*      */ 
/* 5073 */         unbindThread(oldCCL);
/* 5074 */         oldCCL = bindThread();
/*      */         
/*      */ 
/*      */ 
/* 5078 */         this.logger = null;
/* 5079 */         getLogger();
/*      */         
/* 5081 */         Realm realm = getRealmInternal();
/* 5082 */         CredentialHandler safeHandler; if (null != realm) {
/* 5083 */           if ((realm instanceof Lifecycle)) {
/* 5084 */             ((Lifecycle)realm).start();
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 5090 */           safeHandler = new CredentialHandler()
/*      */           {
/*      */             public boolean matches(String inputCredentials, String storedCredentials) {
/* 5093 */               return StandardContext.this.getRealmInternal().getCredentialHandler().matches(inputCredentials, storedCredentials);
/*      */             }
/*      */             
/*      */             public String mutate(String inputCredentials)
/*      */             {
/* 5098 */               return StandardContext.this.getRealmInternal().getCredentialHandler().mutate(inputCredentials);
/*      */             }
/* 5100 */           };
/* 5101 */           this.context.setAttribute("org.apache.catalina.CredentialHandler", safeHandler);
/*      */         }
/*      */         
/*      */ 
/* 5105 */         fireLifecycleEvent("configure_start", null);
/*      */         
/*      */ 
/* 5108 */         for (Container child : findChildren()) {
/* 5109 */           if (!child.getState().isAvailable()) {
/* 5110 */             child.start();
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 5116 */         if ((this.pipeline instanceof Lifecycle)) {
/* 5117 */           ((Lifecycle)this.pipeline).start();
/*      */         }
/*      */         
/*      */ 
/* 5121 */         Manager contextManager = null;
/* 5122 */         Manager manager = getManager();
/* 5123 */         if (manager == null) {
/* 5124 */           if (log.isDebugEnabled()) {
/* 5125 */             log.debug(sm.getString("standardContext.cluster.noManager", new Object[] {
/* 5126 */               Boolean.valueOf(getCluster() != null ? 1 : false), 
/* 5127 */               Boolean.valueOf(this.distributable) }));
/*      */           }
/* 5129 */           if ((getCluster() != null) && (this.distributable)) {
/*      */             try {
/* 5131 */               contextManager = getCluster().createManager(getName());
/*      */             } catch (Exception ex) {
/* 5133 */               log.error("standardContext.clusterFail", ex);
/* 5134 */               ok = false;
/*      */             }
/*      */           } else {
/* 5137 */             contextManager = new StandardManager();
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 5142 */         if (contextManager != null) {
/* 5143 */           if (log.isDebugEnabled()) {
/* 5144 */             log.debug(sm.getString("standardContext.manager", new Object[] {contextManager
/* 5145 */               .getClass().getName() }));
/*      */           }
/* 5147 */           setManager(contextManager);
/*      */         }
/*      */         
/* 5150 */         if ((manager != null) && (getCluster() != null) && (this.distributable))
/*      */         {
/*      */ 
/* 5153 */           getCluster().registerManager(manager);
/*      */         }
/*      */       }
/*      */       
/* 5157 */       if (!getConfigured()) {
/* 5158 */         log.error(sm.getString("standardContext.configurationFail"));
/* 5159 */         ok = false;
/*      */       }
/*      */       
/*      */ 
/* 5163 */       if (ok)
/*      */       {
/* 5165 */         getServletContext().setAttribute("org.apache.catalina.resources", getResources()); }
/*      */       javax.naming.Context context;
/* 5167 */       if (ok) {
/* 5168 */         if (getInstanceManager() == null) {
/* 5169 */           context = null;
/* 5170 */           if ((isUseNaming()) && (getNamingContextListener() != null)) {
/* 5171 */             context = getNamingContextListener().getEnvContext();
/*      */           }
/* 5173 */           Map<String, Map<String, String>> injectionMap = buildInjectionMap(
/* 5174 */             getIgnoreAnnotations() ? new NamingResourcesImpl() : getNamingResources());
/* 5175 */           setInstanceManager(new DefaultInstanceManager(context, injectionMap, this, 
/* 5176 */             getClass().getClassLoader()));
/*      */         }
/* 5178 */         getServletContext().setAttribute(InstanceManager.class
/* 5179 */           .getName(), getInstanceManager());
/* 5180 */         InstanceManagerBindings.bind(getLoader().getClassLoader(), getInstanceManager());
/*      */       }
/*      */       
/*      */ 
/* 5184 */       if (ok) {
/* 5185 */         getServletContext().setAttribute(JarScanner.class
/* 5186 */           .getName(), getJarScanner());
/*      */       }
/*      */       
/*      */ 
/* 5190 */       mergeParameters();
/*      */       
/*      */ 
/*      */ 
/* 5194 */       for (Map.Entry<ServletContainerInitializer, Set<Class<?>>> entry : this.initializers.entrySet()) {
/*      */         try {
/* 5196 */           ((ServletContainerInitializer)entry.getKey()).onStartup((Set)entry.getValue(), 
/* 5197 */             getServletContext());
/*      */         } catch (ServletException e) {
/* 5199 */           log.error(sm.getString("standardContext.sciFail"), e);
/* 5200 */           ok = false;
/* 5201 */           break;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 5206 */       if ((ok) && 
/* 5207 */         (!listenerStart())) {
/* 5208 */         log.error(sm.getString("standardContext.listenerFail"));
/* 5209 */         ok = false;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5216 */       if (ok) {
/* 5217 */         checkConstraintsForUncoveredMethods(findConstraints());
/*      */       }
/*      */       
/*      */       try
/*      */       {
/* 5222 */         Manager manager = getManager();
/* 5223 */         if ((manager instanceof Lifecycle)) {
/* 5224 */           ((Lifecycle)manager).start();
/*      */         }
/*      */       } catch (Exception e) {
/* 5227 */         log.error(sm.getString("standardContext.managerFail"), e);
/* 5228 */         ok = false;
/*      */       }
/*      */       
/*      */ 
/* 5232 */       if ((ok) && 
/* 5233 */         (!filterStart())) {
/* 5234 */         log.error(sm.getString("standardContext.filterFail"));
/* 5235 */         ok = false;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 5240 */       if ((ok) && 
/* 5241 */         (!loadOnStartup(findChildren()))) {
/* 5242 */         log.error(sm.getString("standardContext.servletFail"));
/* 5243 */         ok = false;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 5248 */       super.threadStart();
/*      */     }
/*      */     finally {
/* 5251 */       unbindThread(oldCCL);
/*      */     }
/*      */     
/*      */ 
/* 5255 */     if (ok) {
/* 5256 */       if (log.isDebugEnabled())
/* 5257 */         log.debug("Starting completed");
/*      */     } else {
/* 5259 */       log.error(sm.getString("standardContext.startFailed", new Object[] { getName() }));
/*      */     }
/*      */     
/* 5262 */     this.startTime = System.currentTimeMillis();
/*      */     
/*      */ 
/* 5265 */     if ((ok) && (getObjectName() != null))
/*      */     {
/*      */ 
/* 5268 */       Notification notification = new Notification("j2ee.state.running", getObjectName(), this.sequenceNumber.getAndIncrement());
/* 5269 */       this.broadcaster.sendNotification(notification);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5276 */     getResources().gc();
/*      */     
/*      */ 
/* 5279 */     if (!ok) {
/* 5280 */       setState(LifecycleState.FAILED);
/*      */     } else {
/* 5282 */       setState(LifecycleState.STARTING);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void checkConstraintsForUncoveredMethods(SecurityConstraint[] constraints)
/*      */   {
/* 5290 */     SecurityConstraint[] newConstraints = SecurityConstraint.findUncoveredHttpMethods(constraints, 
/* 5291 */       getDenyUncoveredHttpMethods(), getLogger());
/* 5292 */     for (SecurityConstraint constraint : newConstraints) {
/* 5293 */       addConstraint(constraint);
/*      */     }
/*      */   }
/*      */   
/*      */   private void setClassLoaderProperty(String name, boolean value)
/*      */   {
/* 5299 */     ClassLoader cl = getLoader().getClassLoader();
/* 5300 */     if (!IntrospectionUtils.setProperty(cl, name, Boolean.toString(value)))
/*      */     {
/* 5302 */       log.info(sm.getString("standardContext.webappClassLoader.missingProperty", new Object[] { name, 
/*      */       
/* 5304 */         Boolean.toString(value) }));
/*      */     }
/*      */   }
/*      */   
/*      */   private Map<String, Map<String, String>> buildInjectionMap(NamingResourcesImpl namingResources) {
/* 5309 */     Map<String, Map<String, String>> injectionMap = new HashMap();
/* 5310 */     for (Injectable resource : namingResources.findLocalEjbs()) {
/* 5311 */       addInjectionTarget(resource, injectionMap);
/*      */     }
/* 5313 */     for (Injectable resource : namingResources.findEjbs()) {
/* 5314 */       addInjectionTarget(resource, injectionMap);
/*      */     }
/* 5316 */     for (Injectable resource : namingResources.findEnvironments()) {
/* 5317 */       addInjectionTarget(resource, injectionMap);
/*      */     }
/* 5319 */     for (Injectable resource : namingResources.findMessageDestinationRefs()) {
/* 5320 */       addInjectionTarget(resource, injectionMap);
/*      */     }
/* 5322 */     for (Injectable resource : namingResources.findResourceEnvRefs()) {
/* 5323 */       addInjectionTarget(resource, injectionMap);
/*      */     }
/* 5325 */     for (Injectable resource : namingResources.findResources()) {
/* 5326 */       addInjectionTarget(resource, injectionMap);
/*      */     }
/* 5328 */     for (Injectable resource : namingResources.findServices()) {
/* 5329 */       addInjectionTarget(resource, injectionMap);
/*      */     }
/* 5331 */     return injectionMap;
/*      */   }
/*      */   
/*      */   private void addInjectionTarget(Injectable resource, Map<String, Map<String, String>> injectionMap) {
/* 5335 */     List<InjectionTarget> injectionTargets = resource.getInjectionTargets();
/* 5336 */     String jndiName; if ((injectionTargets != null) && (injectionTargets.size() > 0)) {
/* 5337 */       jndiName = resource.getName();
/* 5338 */       for (InjectionTarget injectionTarget : injectionTargets) {
/* 5339 */         String clazz = injectionTarget.getTargetClass();
/* 5340 */         Map<String, String> injections = (Map)injectionMap.get(clazz);
/* 5341 */         if (injections == null) {
/* 5342 */           injections = new HashMap();
/* 5343 */           injectionMap.put(clazz, injections);
/*      */         }
/* 5345 */         injections.put(injectionTarget.getTargetName(), jndiName);
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
/*      */   private void mergeParameters()
/*      */   {
/* 5359 */     Map<String, String> mergedParams = new HashMap();
/*      */     
/* 5361 */     String[] names = findParameters();
/* 5362 */     for (int i = 0; i < names.length; i++) {
/* 5363 */       mergedParams.put(names[i], findParameter(names[i]));
/*      */     }
/*      */     
/* 5366 */     ApplicationParameter[] params = findApplicationParameters();
/* 5367 */     for (int i = 0; i < params.length; i++) {
/* 5368 */       if (params[i].getOverride()) {
/* 5369 */         if (mergedParams.get(params[i].getName()) == null) {
/* 5370 */           mergedParams.put(params[i].getName(), params[i]
/* 5371 */             .getValue());
/*      */         }
/*      */       } else {
/* 5374 */         mergedParams.put(params[i].getName(), params[i].getValue());
/*      */       }
/*      */     }
/*      */     
/* 5378 */     javax.servlet.ServletContext sc = getServletContext();
/* 5379 */     for (Map.Entry<String, String> entry : mergedParams.entrySet()) {
/* 5380 */       sc.setInitParameter((String)entry.getKey(), (String)entry.getValue());
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
/*      */   protected synchronized void stopInternal()
/*      */     throws LifecycleException
/*      */   {
/* 5397 */     if (getObjectName() != null)
/*      */     {
/*      */ 
/* 5400 */       Notification notification = new Notification("j2ee.state.stopping", getObjectName(), this.sequenceNumber.getAndIncrement());
/* 5401 */       this.broadcaster.sendNotification(notification);
/*      */     }
/*      */     
/* 5404 */     setState(LifecycleState.STOPPING);
/*      */     
/*      */ 
/* 5407 */     ClassLoader oldCCL = bindThread();
/*      */     
/*      */     try
/*      */     {
/* 5411 */       Container[] children = findChildren();
/*      */       
/*      */ 
/* 5414 */       threadStop();
/*      */       
/* 5416 */       for (int i = 0; i < children.length; i++) {
/* 5417 */         children[i].stop();
/*      */       }
/*      */       
/*      */ 
/* 5421 */       filterStop();
/*      */       
/* 5423 */       Manager manager = getManager();
/* 5424 */       if (((manager instanceof Lifecycle)) && (((Lifecycle)manager).getState().isAvailable())) {
/* 5425 */         ((Lifecycle)manager).stop();
/*      */       }
/*      */       
/*      */ 
/* 5429 */       listenerStop();
/*      */       
/*      */ 
/* 5432 */       setCharsetMapper(null);
/*      */       
/*      */ 
/* 5435 */       if (log.isDebugEnabled()) {
/* 5436 */         log.debug("Processing standard container shutdown");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 5442 */       if (this.namingResources != null) {
/* 5443 */         this.namingResources.stop();
/*      */       }
/*      */       
/* 5446 */       fireLifecycleEvent("configure_stop", null);
/*      */       
/*      */ 
/* 5449 */       if (((this.pipeline instanceof Lifecycle)) && 
/* 5450 */         (((Lifecycle)this.pipeline).getState().isAvailable())) {
/* 5451 */         ((Lifecycle)this.pipeline).stop();
/*      */       }
/*      */       
/*      */ 
/* 5455 */       if (this.context != null) {
/* 5456 */         this.context.clearAttributes();
/*      */       }
/* 5458 */       Realm realm = getRealmInternal();
/* 5459 */       if ((realm instanceof Lifecycle)) {
/* 5460 */         ((Lifecycle)realm).stop();
/*      */       }
/* 5462 */       Loader loader = getLoader();
/* 5463 */       if ((loader instanceof Lifecycle)) {
/* 5464 */         ClassLoader classLoader = loader.getClassLoader();
/* 5465 */         ((Lifecycle)loader).stop();
/* 5466 */         if (classLoader != null) {
/* 5467 */           InstanceManagerBindings.unbind(classLoader);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 5472 */       resourcesStop();
/*      */ 
/*      */     }
/*      */     finally
/*      */     {
/* 5477 */       unbindThread(oldCCL);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 5482 */     if (getObjectName() != null)
/*      */     {
/*      */ 
/* 5485 */       Notification notification = new Notification("j2ee.state.stopped", getObjectName(), this.sequenceNumber.getAndIncrement());
/* 5486 */       this.broadcaster.sendNotification(notification);
/*      */     }
/*      */     
/*      */ 
/* 5490 */     this.context = null;
/*      */     
/*      */     try
/*      */     {
/* 5494 */       resetContext();
/*      */     } catch (Exception ex) {
/* 5496 */       log.error("Error resetting context " + this + " " + ex, ex);
/*      */     }
/*      */     
/*      */ 
/* 5500 */     setInstanceManager(null);
/*      */     
/* 5502 */     if (log.isDebugEnabled()) {
/* 5503 */       log.debug("Stopping complete");
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
/*      */   protected void destroyInternal()
/*      */     throws LifecycleException
/*      */   {
/* 5524 */     if (getObjectName() != null)
/*      */     {
/*      */ 
/*      */ 
/* 5528 */       Notification notification = new Notification("j2ee.object.deleted", getObjectName(), this.sequenceNumber.getAndIncrement());
/* 5529 */       this.broadcaster.sendNotification(notification);
/*      */     }
/*      */     
/* 5532 */     if (this.namingResources != null) {
/* 5533 */       this.namingResources.destroy();
/*      */     }
/*      */     
/* 5536 */     Loader loader = getLoader();
/* 5537 */     if ((loader instanceof Lifecycle)) {
/* 5538 */       ((Lifecycle)loader).destroy();
/*      */     }
/*      */     
/* 5541 */     Manager manager = getManager();
/* 5542 */     if ((manager instanceof Lifecycle)) {
/* 5543 */       ((Lifecycle)manager).destroy();
/*      */     }
/*      */     
/* 5546 */     if (this.resources != null) {
/* 5547 */       this.resources.destroy();
/*      */     }
/*      */     
/* 5550 */     super.destroyInternal();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void backgroundProcess()
/*      */   {
/* 5557 */     if (!getState().isAvailable()) {
/* 5558 */       return;
/*      */     }
/* 5560 */     Loader loader = getLoader();
/* 5561 */     if (loader != null) {
/*      */       try {
/* 5563 */         loader.backgroundProcess();
/*      */       } catch (Exception e) {
/* 5565 */         log.warn(sm.getString("standardContext.backgroundProcess.loader", new Object[] { loader }), e);
/*      */       }
/*      */     }
/*      */     
/* 5569 */     Manager manager = getManager();
/* 5570 */     if (manager != null) {
/*      */       try {
/* 5572 */         manager.backgroundProcess();
/*      */       } catch (Exception e) {
/* 5574 */         log.warn(sm.getString("standardContext.backgroundProcess.manager", new Object[] { manager }), e);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 5579 */     WebResourceRoot resources = getResources();
/* 5580 */     if (resources != null) {
/*      */       try {
/* 5582 */         resources.backgroundProcess();
/*      */       } catch (Exception e) {
/* 5584 */         log.warn(sm.getString("standardContext.backgroundProcess.resources", new Object[] { resources }), e);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 5589 */     InstanceManager instanceManager = getInstanceManager();
/* 5590 */     if ((instanceManager instanceof DefaultInstanceManager)) {
/*      */       try {
/* 5592 */         ((DefaultInstanceManager)instanceManager).backgroundProcess();
/*      */       } catch (Exception e) {
/* 5594 */         log.warn(sm.getString("standardContext.backgroundProcess.instanceManager", new Object[] { resources }), e);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 5599 */     super.backgroundProcess();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void resetContext()
/*      */     throws Exception
/*      */   {
/* 5610 */     for (Container child : findChildren()) {
/* 5611 */       removeChild(child);
/*      */     }
/* 5613 */     this.startupTime = 0L;
/* 5614 */     this.startTime = 0L;
/* 5615 */     this.tldScanTime = 0L;
/*      */     
/*      */ 
/* 5618 */     this.distributable = false;
/*      */     
/* 5620 */     this.applicationListeners = new String[0];
/* 5621 */     this.applicationEventListenersList.clear();
/* 5622 */     this.applicationLifecycleListenersObjects = new Object[0];
/* 5623 */     this.jspConfigDescriptor = null;
/*      */     
/* 5625 */     this.initializers.clear();
/*      */     
/* 5627 */     this.createdServlets.clear();
/*      */     
/* 5629 */     this.postConstructMethods.clear();
/* 5630 */     this.preDestroyMethods.clear();
/*      */     
/* 5632 */     if (log.isDebugEnabled()) {
/* 5633 */       log.debug("resetContext " + getObjectName());
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
/*      */   protected String adjustURLPattern(String urlPattern)
/*      */   {
/* 5650 */     if (urlPattern == null)
/* 5651 */       return urlPattern;
/* 5652 */     if ((urlPattern.startsWith("/")) || (urlPattern.startsWith("*.")))
/* 5653 */       return urlPattern;
/* 5654 */     if (!isServlet22())
/* 5655 */       return urlPattern;
/* 5656 */     if (log.isDebugEnabled()) {
/* 5657 */       log.debug(sm.getString("standardContext.urlPattern.patternWarning", new Object[] { urlPattern }));
/*      */     }
/* 5659 */     return "/" + urlPattern;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isServlet22()
/*      */   {
/* 5671 */     return "-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN".equals(this.publicId);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<String> addServletSecurity(ServletRegistration.Dynamic registration, ServletSecurityElement servletSecurityElement)
/*      */   {
/* 5680 */     Set<String> conflicts = new HashSet();
/*      */     
/* 5682 */     Collection<String> urlPatterns = registration.getMappings();
/* 5683 */     for (String urlPattern : urlPatterns) {
/* 5684 */       boolean foundConflict = false;
/*      */       
/*      */ 
/* 5687 */       SecurityConstraint[] securityConstraints = findConstraints();
/* 5688 */       SecurityConstraint[] arrayOfSecurityConstraint1 = securityConstraints;int i = arrayOfSecurityConstraint1.length; SecurityConstraint securityConstraint; for (SecurityConstraint localSecurityConstraint1 = 0; localSecurityConstraint1 < i; localSecurityConstraint1++) { securityConstraint = arrayOfSecurityConstraint1[localSecurityConstraint1];
/*      */         
/*      */ 
/* 5691 */         SecurityCollection[] collections = securityConstraint.findCollections();
/* 5692 */         for (SecurityCollection collection : collections) {
/* 5693 */           if (collection.findPattern(urlPattern))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/* 5698 */             if (collection.isFromDescriptor())
/*      */             {
/* 5700 */               foundConflict = true;
/* 5701 */               conflicts.add(urlPattern);
/* 5702 */               break;
/*      */             }
/*      */             
/* 5705 */             collection.removePattern(urlPattern);
/*      */             
/* 5707 */             if (collection.findPatterns().length == 0) {
/* 5708 */               securityConstraint.removeCollection(collection);
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 5715 */         if (securityConstraint.findCollections().length == 0) {
/* 5716 */           removeConstraint(securityConstraint);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 5721 */         if (foundConflict) {
/*      */           break;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5735 */       if (!foundConflict)
/*      */       {
/* 5737 */         SecurityConstraint[] newSecurityConstraints = SecurityConstraint.createConstraints(servletSecurityElement, urlPattern);
/*      */         
/*      */ 
/*      */ 
/* 5741 */         SecurityConstraint[] arrayOfSecurityConstraint2 = newSecurityConstraints;localSecurityConstraint1 = arrayOfSecurityConstraint2.length; for (securityConstraint = 0; securityConstraint < localSecurityConstraint1; securityConstraint++) { SecurityConstraint securityConstraint = arrayOfSecurityConstraint2[securityConstraint];
/* 5742 */           addConstraint(securityConstraint);
/*      */         }
/*      */         
/* 5745 */         checkConstraintsForUncoveredMethods(newSecurityConstraints);
/*      */       }
/*      */     }
/*      */     
/* 5749 */     return conflicts;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ClassLoader bindThread()
/*      */   {
/* 5761 */     ClassLoader oldContextClassLoader = bind(false, null);
/*      */     
/* 5763 */     if (isUseNaming()) {
/*      */       try {
/* 5765 */         ContextBindings.bindThread(this, getNamingToken());
/*      */       }
/*      */       catch (NamingException localNamingException) {}
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 5772 */     return oldContextClassLoader;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void unbindThread(ClassLoader oldContextClassLoader)
/*      */   {
/* 5783 */     if (isUseNaming()) {
/* 5784 */       ContextBindings.unbindThread(this, getNamingToken());
/*      */     }
/*      */     
/* 5787 */     unbind(false, oldContextClassLoader);
/*      */   }
/*      */   
/*      */ 
/*      */   public ClassLoader bind(boolean usePrivilegedAction, ClassLoader originalClassLoader)
/*      */   {
/* 5793 */     Loader loader = getLoader();
/* 5794 */     ClassLoader webApplicationClassLoader = null;
/* 5795 */     if (loader != null) {
/* 5796 */       webApplicationClassLoader = loader.getClassLoader();
/*      */     }
/*      */     
/* 5799 */     if (originalClassLoader == null) {
/* 5800 */       if (usePrivilegedAction) {
/* 5801 */         PrivilegedAction<ClassLoader> pa = new PrivilegedGetTccl();
/* 5802 */         originalClassLoader = (ClassLoader)AccessController.doPrivileged(pa);
/*      */       } else {
/* 5804 */         originalClassLoader = Thread.currentThread().getContextClassLoader();
/*      */       }
/*      */     }
/*      */     
/* 5808 */     if ((webApplicationClassLoader == null) || (webApplicationClassLoader == originalClassLoader))
/*      */     {
/*      */ 
/*      */ 
/* 5812 */       return null;
/*      */     }
/*      */     
/* 5815 */     ThreadBindingListener threadBindingListener = getThreadBindingListener();
/*      */     
/* 5817 */     if (usePrivilegedAction) {
/* 5818 */       PrivilegedAction<Void> pa = new PrivilegedSetTccl(webApplicationClassLoader);
/* 5819 */       AccessController.doPrivileged(pa);
/*      */     } else {
/* 5821 */       Thread.currentThread().setContextClassLoader(webApplicationClassLoader);
/*      */     }
/* 5823 */     if (threadBindingListener != null) {
/*      */       try {
/* 5825 */         threadBindingListener.bind();
/*      */       } catch (Throwable t) {
/* 5827 */         ExceptionUtils.handleThrowable(t);
/* 5828 */         log.error(sm.getString("standardContext.threadBindingListenerError", new Object[] {
/* 5829 */           getName() }), t);
/*      */       }
/*      */     }
/*      */     
/* 5833 */     return originalClassLoader;
/*      */   }
/*      */   
/*      */ 
/*      */   public void unbind(boolean usePrivilegedAction, ClassLoader originalClassLoader)
/*      */   {
/* 5839 */     if (originalClassLoader == null) {
/* 5840 */       return;
/*      */     }
/*      */     
/* 5843 */     if (this.threadBindingListener != null) {
/*      */       try {
/* 5845 */         this.threadBindingListener.unbind();
/*      */       } catch (Throwable t) {
/* 5847 */         ExceptionUtils.handleThrowable(t);
/* 5848 */         log.error(sm.getString("standardContext.threadBindingListenerError", new Object[] {
/* 5849 */           getName() }), t);
/*      */       }
/*      */     }
/*      */     
/* 5853 */     if (usePrivilegedAction) {
/* 5854 */       PrivilegedAction<Void> pa = new PrivilegedSetTccl(originalClassLoader);
/* 5855 */       AccessController.doPrivileged(pa);
/*      */     } else {
/* 5857 */       Thread.currentThread().setContextClassLoader(originalClassLoader);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String getNamingContextName()
/*      */   {
/* 5868 */     if (this.namingContextName == null) {
/* 5869 */       Container parent = getParent();
/* 5870 */       if (parent == null) {
/* 5871 */         this.namingContextName = getName();
/*      */       } else {
/* 5873 */         Stack<String> stk = new Stack();
/* 5874 */         StringBuilder buff = new StringBuilder();
/* 5875 */         while (parent != null) {
/* 5876 */           stk.push(parent.getName());
/* 5877 */           parent = parent.getParent();
/*      */         }
/* 5879 */         while (!stk.empty()) {
/* 5880 */           buff.append("/" + (String)stk.pop());
/*      */         }
/* 5882 */         buff.append(getName());
/* 5883 */         this.namingContextName = buff.toString();
/*      */       }
/*      */     }
/* 5886 */     return this.namingContextName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public NamingContextListener getNamingContextListener()
/*      */   {
/* 5896 */     return this.namingContextListener;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setNamingContextListener(NamingContextListener namingContextListener)
/*      */   {
/* 5906 */     this.namingContextListener = namingContextListener;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getPaused()
/*      */   {
/* 5916 */     return this.paused;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean fireRequestInitEvent(ServletRequest request)
/*      */   {
/* 5924 */     Object[] instances = getApplicationEventListeners();
/*      */     
/* 5926 */     if ((instances != null) && (instances.length > 0))
/*      */     {
/*      */ 
/* 5929 */       ServletRequestEvent event = new ServletRequestEvent(getServletContext(), request);
/*      */       
/* 5931 */       for (int i = 0; i < instances.length; i++)
/* 5932 */         if (instances[i] != null)
/*      */         {
/* 5934 */           if ((instances[i] instanceof ServletRequestListener))
/*      */           {
/* 5936 */             ServletRequestListener listener = (ServletRequestListener)instances[i];
/*      */             
/*      */             try
/*      */             {
/* 5940 */               listener.requestInitialized(event);
/*      */             } catch (Throwable t) {
/* 5942 */               ExceptionUtils.handleThrowable(t);
/* 5943 */               getLogger().error(sm.getString("standardContext.requestListener.requestInit", new Object[] {instances[i]
/*      */               
/* 5945 */                 .getClass().getName() }), t);
/* 5946 */               request.setAttribute("javax.servlet.error.exception", t);
/* 5947 */               return false;
/*      */             }
/*      */           } }
/*      */     }
/* 5951 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean fireRequestDestroyEvent(ServletRequest request)
/*      */   {
/* 5957 */     Object[] instances = getApplicationEventListeners();
/*      */     
/* 5959 */     if ((instances != null) && (instances.length > 0))
/*      */     {
/*      */ 
/* 5962 */       ServletRequestEvent event = new ServletRequestEvent(getServletContext(), request);
/*      */       
/* 5964 */       for (int i = 0; i < instances.length; i++) {
/* 5965 */         int j = instances.length - 1 - i;
/* 5966 */         if (instances[j] != null)
/*      */         {
/* 5968 */           if ((instances[j] instanceof ServletRequestListener))
/*      */           {
/* 5970 */             ServletRequestListener listener = (ServletRequestListener)instances[j];
/*      */             
/*      */             try
/*      */             {
/* 5974 */               listener.requestDestroyed(event);
/*      */             } catch (Throwable t) {
/* 5976 */               ExceptionUtils.handleThrowable(t);
/* 5977 */               getLogger().error(sm.getString("standardContext.requestListener.requestInit", new Object[] {instances[j]
/*      */               
/* 5979 */                 .getClass().getName() }), t);
/* 5980 */               request.setAttribute("javax.servlet.error.exception", t);
/* 5981 */               return false;
/*      */             }
/*      */           } }
/*      */       } }
/* 5985 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public void addPostConstructMethod(String clazz, String method)
/*      */   {
/* 5991 */     if ((clazz == null) || (method == null))
/*      */     {
/* 5993 */       throw new IllegalArgumentException(sm.getString("standardContext.postconstruct.required")); }
/* 5994 */     if (this.postConstructMethods.get(clazz) != null) {
/* 5995 */       throw new IllegalArgumentException(sm.getString("standardContext.postconstruct.duplicate", new Object[] { clazz }));
/*      */     }
/*      */     
/* 5998 */     this.postConstructMethods.put(clazz, method);
/* 5999 */     fireContainerEvent("addPostConstructMethod", clazz);
/*      */   }
/*      */   
/*      */ 
/*      */   public void removePostConstructMethod(String clazz)
/*      */   {
/* 6005 */     this.postConstructMethods.remove(clazz);
/* 6006 */     fireContainerEvent("removePostConstructMethod", clazz);
/*      */   }
/*      */   
/*      */ 
/*      */   public void addPreDestroyMethod(String clazz, String method)
/*      */   {
/* 6012 */     if ((clazz == null) || (method == null))
/*      */     {
/* 6014 */       throw new IllegalArgumentException(sm.getString("standardContext.predestroy.required")); }
/* 6015 */     if (this.preDestroyMethods.get(clazz) != null) {
/* 6016 */       throw new IllegalArgumentException(sm.getString("standardContext.predestroy.duplicate", new Object[] { clazz }));
/*      */     }
/*      */     
/* 6019 */     this.preDestroyMethods.put(clazz, method);
/* 6020 */     fireContainerEvent("addPreDestroyMethod", clazz);
/*      */   }
/*      */   
/*      */ 
/*      */   public void removePreDestroyMethod(String clazz)
/*      */   {
/* 6026 */     this.preDestroyMethods.remove(clazz);
/* 6027 */     fireContainerEvent("removePreDestroyMethod", clazz);
/*      */   }
/*      */   
/*      */ 
/*      */   public String findPostConstructMethod(String clazz)
/*      */   {
/* 6033 */     return (String)this.postConstructMethods.get(clazz);
/*      */   }
/*      */   
/*      */ 
/*      */   public String findPreDestroyMethod(String clazz)
/*      */   {
/* 6039 */     return (String)this.preDestroyMethods.get(clazz);
/*      */   }
/*      */   
/*      */ 
/*      */   public Map<String, String> findPostConstructMethods()
/*      */   {
/* 6045 */     return this.postConstructMethods;
/*      */   }
/*      */   
/*      */ 
/*      */   public Map<String, String> findPreDestroyMethods()
/*      */   {
/* 6051 */     return this.preDestroyMethods;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void postWorkDirectory()
/*      */   {
/* 6061 */     String workDir = getWorkDir();
/* 6062 */     if ((workDir == null) || (workDir.length() == 0))
/*      */     {
/*      */ 
/* 6065 */       String hostName = null;
/* 6066 */       String engineName = null;
/* 6067 */       String hostWorkDir = null;
/* 6068 */       Container parentHost = getParent();
/* 6069 */       if (parentHost != null) {
/* 6070 */         hostName = parentHost.getName();
/* 6071 */         if ((parentHost instanceof StandardHost)) {
/* 6072 */           hostWorkDir = ((StandardHost)parentHost).getWorkDir();
/*      */         }
/* 6074 */         Container parentEngine = parentHost.getParent();
/* 6075 */         if (parentEngine != null) {
/* 6076 */           engineName = parentEngine.getName();
/*      */         }
/*      */       }
/* 6079 */       if ((hostName == null) || (hostName.length() < 1))
/* 6080 */         hostName = "_";
/* 6081 */       if ((engineName == null) || (engineName.length() < 1)) {
/* 6082 */         engineName = "_";
/*      */       }
/* 6084 */       String temp = getBaseName();
/* 6085 */       if (temp.startsWith("/"))
/* 6086 */         temp = temp.substring(1);
/* 6087 */       temp = temp.replace('/', '_');
/* 6088 */       temp = temp.replace('\\', '_');
/* 6089 */       if (temp.length() < 1)
/* 6090 */         temp = "ROOT";
/* 6091 */       if (hostWorkDir != null) {
/* 6092 */         workDir = hostWorkDir + File.separator + temp;
/*      */       } else {
/* 6094 */         workDir = "work" + File.separator + engineName + File.separator + hostName + File.separator + temp;
/*      */       }
/*      */       
/* 6097 */       setWorkDir(workDir);
/*      */     }
/*      */     
/*      */ 
/* 6101 */     File dir = new File(workDir);
/* 6102 */     if (!dir.isAbsolute()) {
/* 6103 */       String catalinaHomePath = null;
/*      */       try {
/* 6105 */         catalinaHomePath = getCatalinaBase().getCanonicalPath();
/* 6106 */         dir = new File(catalinaHomePath, workDir);
/*      */       } catch (IOException e) {
/* 6108 */         log.warn(sm.getString("standardContext.workCreateException", new Object[] { workDir, catalinaHomePath, 
/* 6109 */           getName() }), e);
/*      */       }
/*      */     }
/* 6112 */     if ((!dir.mkdirs()) && (!dir.isDirectory())) {
/* 6113 */       log.warn(sm.getString("standardContext.workCreateFail", new Object[] { dir, 
/* 6114 */         getName() }));
/*      */     }
/*      */     
/*      */ 
/* 6118 */     if (this.context == null) {
/* 6119 */       getServletContext();
/*      */     }
/* 6121 */     this.context.setAttribute("javax.servlet.context.tempdir", dir);
/* 6122 */     this.context.setAttributeReadOnly("javax.servlet.context.tempdir");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void setPaused(boolean paused)
/*      */   {
/* 6133 */     this.paused = paused;
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
/*      */   private boolean validateURLPattern(String urlPattern)
/*      */   {
/* 6147 */     if (urlPattern == null)
/* 6148 */       return false;
/* 6149 */     if ((urlPattern.indexOf('\n') >= 0) || (urlPattern.indexOf('\r') >= 0)) {
/* 6150 */       return false;
/*      */     }
/* 6152 */     if (urlPattern.equals("")) {
/* 6153 */       return true;
/*      */     }
/* 6155 */     if (urlPattern.startsWith("*.")) {
/* 6156 */       if (urlPattern.indexOf('/') < 0) {
/* 6157 */         checkUnusualURLPattern(urlPattern);
/* 6158 */         return true;
/*      */       }
/* 6160 */       return false;
/*      */     }
/* 6162 */     if ((urlPattern.startsWith("/")) && 
/* 6163 */       (urlPattern.indexOf("*.") < 0)) {
/* 6164 */       checkUnusualURLPattern(urlPattern);
/* 6165 */       return true;
/*      */     }
/* 6167 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void checkUnusualURLPattern(String urlPattern)
/*      */   {
/* 6177 */     if (log.isInfoEnabled())
/*      */     {
/*      */ 
/* 6180 */       if (((urlPattern.endsWith("*")) && ((urlPattern.length() < 2) || 
/* 6181 */         (urlPattern.charAt(urlPattern.length() - 2) != '/'))) || (
/* 6182 */         (urlPattern.startsWith("*.")) && (urlPattern.length() > 2) && 
/* 6183 */         (urlPattern.lastIndexOf('.') > 1))) {
/* 6184 */         log.info("Suspicious url pattern: \"" + urlPattern + "\" in context [" + 
/* 6185 */           getName() + "] - see sections 12.1 and 12.2 of the Servlet specification");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getObjectNameKeyProperties()
/*      */   {
/* 6197 */     StringBuilder keyProperties = new StringBuilder("j2eeType=WebModule,");
/*      */     
/* 6199 */     keyProperties.append(getObjectKeyPropertiesNameOnly());
/* 6200 */     keyProperties.append(",J2EEApplication=");
/* 6201 */     keyProperties.append(getJ2EEApplication());
/* 6202 */     keyProperties.append(",J2EEServer=");
/* 6203 */     keyProperties.append(getJ2EEServer());
/*      */     
/* 6205 */     return keyProperties.toString();
/*      */   }
/*      */   
/*      */   private String getObjectKeyPropertiesNameOnly() {
/* 6209 */     StringBuilder result = new StringBuilder("name=//");
/* 6210 */     String hostname = getParent().getName();
/* 6211 */     if (hostname == null) {
/* 6212 */       result.append("DEFAULT");
/*      */     } else {
/* 6214 */       result.append(hostname);
/*      */     }
/*      */     
/* 6217 */     String contextName = getName();
/* 6218 */     if (!contextName.startsWith("/")) {
/* 6219 */       result.append('/');
/*      */     }
/* 6221 */     result.append(contextName);
/*      */     
/* 6223 */     return result.toString();
/*      */   }
/*      */   
/*      */   protected void initInternal() throws LifecycleException
/*      */   {
/* 6228 */     super.initInternal();
/*      */     
/*      */ 
/* 6231 */     if (this.namingResources != null) {
/* 6232 */       this.namingResources.init();
/*      */     }
/*      */     
/*      */ 
/* 6236 */     if (getObjectName() != null)
/*      */     {
/* 6238 */       Notification notification = new Notification("j2ee.object.created", getObjectName(), this.sequenceNumber.getAndIncrement());
/* 6239 */       this.broadcaster.sendNotification(notification);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeNotificationListener(NotificationListener listener, NotificationFilter filter, Object object)
/*      */     throws ListenerNotFoundException
/*      */   {
/* 6250 */     this.broadcaster.removeNotificationListener(listener, filter, object);
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
/*      */   public MBeanNotificationInfo[] getNotificationInfo()
/*      */   {
/* 6263 */     if (this.notificationInfo == null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 6292 */       this.notificationInfo = new MBeanNotificationInfo[] { new MBeanNotificationInfo(new String[] { "j2ee.object.created" }, Notification.class.getName(), "web application is created"), new MBeanNotificationInfo(new String[] { "j2ee.state.starting" }, Notification.class.getName(), "change web application is starting"), new MBeanNotificationInfo(new String[] { "j2ee.state.running" }, Notification.class.getName(), "web application is running"), new MBeanNotificationInfo(new String[] { "j2ee.state.stopping" }, Notification.class.getName(), "web application start to stopped"), new MBeanNotificationInfo(new String[] { "j2ee.object.stopped" }, Notification.class.getName(), "web application is stopped"), new MBeanNotificationInfo(new String[] { "j2ee.object.deleted" }, Notification.class.getName(), "web application is deleted") };
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 6299 */     return this.notificationInfo;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addNotificationListener(NotificationListener listener, NotificationFilter filter, Object object)
/*      */     throws IllegalArgumentException
/*      */   {
/* 6310 */     this.broadcaster.addNotificationListener(listener, filter, object);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeNotificationListener(NotificationListener listener)
/*      */     throws ListenerNotFoundException
/*      */   {
/* 6321 */     this.broadcaster.removeNotificationListener(listener);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] getWelcomeFiles()
/*      */   {
/* 6332 */     return findWelcomeFiles();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getXmlNamespaceAware()
/*      */   {
/* 6339 */     return this.webXmlNamespaceAware;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setXmlNamespaceAware(boolean webXmlNamespaceAware)
/*      */   {
/* 6345 */     this.webXmlNamespaceAware = webXmlNamespaceAware;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setXmlValidation(boolean webXmlValidation)
/*      */   {
/* 6351 */     this.webXmlValidation = webXmlValidation;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean getXmlValidation()
/*      */   {
/* 6357 */     return this.webXmlValidation;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setXmlBlockExternal(boolean xmlBlockExternal)
/*      */   {
/* 6363 */     this.xmlBlockExternal = xmlBlockExternal;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean getXmlBlockExternal()
/*      */   {
/* 6369 */     return this.xmlBlockExternal;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setTldValidation(boolean tldValidation)
/*      */   {
/* 6375 */     this.tldValidation = tldValidation;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean getTldValidation()
/*      */   {
/* 6381 */     return this.tldValidation;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 6388 */   private String server = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/* 6393 */   private String[] javaVMs = null;
/*      */   
/*      */   public String getServer() {
/* 6396 */     return this.server;
/*      */   }
/*      */   
/*      */   public String setServer(String server) {
/* 6400 */     return this.server = server;
/*      */   }
/*      */   
/*      */   public String[] getJavaVMs() {
/* 6404 */     return this.javaVMs;
/*      */   }
/*      */   
/*      */   public String[] setJavaVMs(String[] javaVMs) {
/* 6408 */     return this.javaVMs = javaVMs;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getStartTime()
/*      */   {
/* 6418 */     return this.startTime;
/*      */   }
/*      */   
/*      */ 
/*      */   private static class NoPluggabilityServletContext
/*      */     implements org.apache.catalina.servlet4preview.ServletContext
/*      */   {
/*      */     private final javax.servlet.ServletContext sc;
/*      */     
/*      */     public NoPluggabilityServletContext(javax.servlet.ServletContext sc)
/*      */     {
/* 6429 */       this.sc = sc;
/*      */     }
/*      */     
/*      */     public String getContextPath()
/*      */     {
/* 6434 */       return this.sc.getContextPath();
/*      */     }
/*      */     
/*      */     public javax.servlet.ServletContext getContext(String uripath)
/*      */     {
/* 6439 */       return this.sc.getContext(uripath);
/*      */     }
/*      */     
/*      */     public int getMajorVersion()
/*      */     {
/* 6444 */       return this.sc.getMajorVersion();
/*      */     }
/*      */     
/*      */     public int getMinorVersion()
/*      */     {
/* 6449 */       return this.sc.getMinorVersion();
/*      */     }
/*      */     
/*      */ 
/*      */     public int getEffectiveMajorVersion()
/*      */     {
/* 6455 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */ 
/*      */     public int getEffectiveMinorVersion()
/*      */     {
/* 6461 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */     public String getMimeType(String file)
/*      */     {
/* 6466 */       return this.sc.getMimeType(file);
/*      */     }
/*      */     
/*      */     public Set<String> getResourcePaths(String path)
/*      */     {
/* 6471 */       return this.sc.getResourcePaths(path);
/*      */     }
/*      */     
/*      */     public URL getResource(String path) throws MalformedURLException
/*      */     {
/* 6476 */       return this.sc.getResource(path);
/*      */     }
/*      */     
/*      */     public InputStream getResourceAsStream(String path)
/*      */     {
/* 6481 */       return this.sc.getResourceAsStream(path);
/*      */     }
/*      */     
/*      */     public RequestDispatcher getRequestDispatcher(String path)
/*      */     {
/* 6486 */       return this.sc.getRequestDispatcher(path);
/*      */     }
/*      */     
/*      */     public RequestDispatcher getNamedDispatcher(String name)
/*      */     {
/* 6491 */       return this.sc.getNamedDispatcher(name);
/*      */     }
/*      */     
/*      */     @Deprecated
/*      */     public Servlet getServlet(String name) throws ServletException
/*      */     {
/* 6497 */       return this.sc.getServlet(name);
/*      */     }
/*      */     
/*      */     @Deprecated
/*      */     public Enumeration<Servlet> getServlets()
/*      */     {
/* 6503 */       return this.sc.getServlets();
/*      */     }
/*      */     
/*      */     @Deprecated
/*      */     public Enumeration<String> getServletNames()
/*      */     {
/* 6509 */       return this.sc.getServletNames();
/*      */     }
/*      */     
/*      */     public void log(String msg)
/*      */     {
/* 6514 */       this.sc.log(msg);
/*      */     }
/*      */     
/*      */     @Deprecated
/*      */     public void log(Exception exception, String msg)
/*      */     {
/* 6520 */       this.sc.log(exception, msg);
/*      */     }
/*      */     
/*      */     public void log(String message, Throwable throwable)
/*      */     {
/* 6525 */       this.sc.log(message, throwable);
/*      */     }
/*      */     
/*      */     public String getRealPath(String path)
/*      */     {
/* 6530 */       return this.sc.getRealPath(path);
/*      */     }
/*      */     
/*      */     public String getServerInfo()
/*      */     {
/* 6535 */       return this.sc.getServerInfo();
/*      */     }
/*      */     
/*      */     public String getInitParameter(String name)
/*      */     {
/* 6540 */       return this.sc.getInitParameter(name);
/*      */     }
/*      */     
/*      */     public Enumeration<String> getInitParameterNames()
/*      */     {
/* 6545 */       return this.sc.getInitParameterNames();
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean setInitParameter(String name, String value)
/*      */     {
/* 6551 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */     public Object getAttribute(String name)
/*      */     {
/* 6556 */       return this.sc.getAttribute(name);
/*      */     }
/*      */     
/*      */     public Enumeration<String> getAttributeNames()
/*      */     {
/* 6561 */       return this.sc.getAttributeNames();
/*      */     }
/*      */     
/*      */     public void setAttribute(String name, Object object)
/*      */     {
/* 6566 */       this.sc.setAttribute(name, object);
/*      */     }
/*      */     
/*      */     public void removeAttribute(String name)
/*      */     {
/* 6571 */       this.sc.removeAttribute(name);
/*      */     }
/*      */     
/*      */     public String getServletContextName()
/*      */     {
/* 6576 */       return this.sc.getServletContextName();
/*      */     }
/*      */     
/*      */ 
/*      */     public ServletRegistration.Dynamic addServlet(String servletName, String className)
/*      */     {
/* 6582 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */ 
/*      */     public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet)
/*      */     {
/* 6588 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public ServletRegistration.Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass)
/*      */     {
/* 6595 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */ 
/*      */     public ServletRegistration.Dynamic addJspFile(String jspName, String jspFile)
/*      */     {
/* 6601 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */ 
/*      */     public <T extends Servlet> T createServlet(Class<T> c)
/*      */       throws ServletException
/*      */     {
/* 6608 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */ 
/*      */     public ServletRegistration getServletRegistration(String servletName)
/*      */     {
/* 6614 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */ 
/*      */     public Map<String, ? extends ServletRegistration> getServletRegistrations()
/*      */     {
/* 6620 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public FilterRegistration.Dynamic addFilter(String filterName, String className)
/*      */     {
/* 6627 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public FilterRegistration.Dynamic addFilter(String filterName, Filter filter)
/*      */     {
/* 6634 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass)
/*      */     {
/* 6641 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */ 
/*      */     public <T extends Filter> T createFilter(Class<T> c)
/*      */       throws ServletException
/*      */     {
/* 6648 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */ 
/*      */     public FilterRegistration getFilterRegistration(String filterName)
/*      */     {
/* 6654 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */ 
/*      */     public Map<String, ? extends FilterRegistration> getFilterRegistrations()
/*      */     {
/* 6660 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */ 
/*      */     public SessionCookieConfig getSessionCookieConfig()
/*      */     {
/* 6666 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes)
/*      */     {
/* 6673 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */ 
/*      */     public Set<SessionTrackingMode> getDefaultSessionTrackingModes()
/*      */     {
/* 6679 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */ 
/*      */     public Set<SessionTrackingMode> getEffectiveSessionTrackingModes()
/*      */     {
/* 6685 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */ 
/*      */     public void addListener(String className)
/*      */     {
/* 6691 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */ 
/*      */     public <T extends EventListener> void addListener(T t)
/*      */     {
/* 6697 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */ 
/*      */     public void addListener(Class<? extends EventListener> listenerClass)
/*      */     {
/* 6703 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */ 
/*      */     public <T extends EventListener> T createListener(Class<T> c)
/*      */       throws ServletException
/*      */     {
/* 6710 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */ 
/*      */     public JspConfigDescriptor getJspConfigDescriptor()
/*      */     {
/* 6716 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */ 
/*      */     public ClassLoader getClassLoader()
/*      */     {
/* 6722 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */ 
/*      */     public void declareRoles(String... roleNames)
/*      */     {
/* 6728 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */     public String getVirtualServerName()
/*      */     {
/* 6733 */       return this.sc.getVirtualServerName();
/*      */     }
/*      */     
/*      */ 
/*      */     public int getSessionTimeout()
/*      */     {
/* 6739 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */ 
/*      */     public void setSessionTimeout(int sessionTimeout)
/*      */     {
/* 6745 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */ 
/*      */     public String getRequestCharacterEncoding()
/*      */     {
/* 6751 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */ 
/*      */     public void setRequestCharacterEncoding(String encoding)
/*      */     {
/* 6757 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */ 
/*      */     public String getResponseCharacterEncoding()
/*      */     {
/* 6763 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */     
/*      */ 
/*      */     public void setResponseCharacterEncoding(String encoding)
/*      */     {
/* 6769 */       throw new UnsupportedOperationException(ContainerBase.sm.getString("noPluggabilityServletContext.notAllowed"));
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\StandardContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */