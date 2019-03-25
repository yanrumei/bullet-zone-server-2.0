/*      */ package org.apache.tomcat.util.descriptor.web;
/*      */ 
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.net.URL;
/*      */ import java.net.URLEncoder;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.EnumSet;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import javax.servlet.DispatcherType;
/*      */ import javax.servlet.ServletContext;
/*      */ import javax.servlet.SessionTrackingMode;
/*      */ import javax.servlet.descriptor.JspConfigDescriptor;
/*      */ import javax.servlet.descriptor.JspPropertyGroupDescriptor;
/*      */ import javax.servlet.descriptor.TaglibDescriptor;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.tomcat.util.buf.UDecoder;
/*      */ import org.apache.tomcat.util.digester.DocumentProperties.Charset;
/*      */ import org.apache.tomcat.util.digester.DocumentProperties.Encoding;
/*      */ import org.apache.tomcat.util.res.StringManager;
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
/*      */ public class WebXml
/*      */   extends XmlEncodingBase
/*      */   implements DocumentProperties.Encoding, DocumentProperties.Charset
/*      */ {
/*      */   protected static final String ORDER_OTHERS = "org.apache.catalina.order.others";
/*   66 */   private static final StringManager sm = StringManager.getManager(Constants.PACKAGE_NAME);
/*      */   
/*   68 */   private static final Log log = LogFactory.getLog(WebXml.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   75 */   private boolean overridable = false;
/*      */   
/*   77 */   public boolean isOverridable() { return this.overridable; }
/*      */   
/*      */   public void setOverridable(boolean overridable) {
/*   80 */     this.overridable = overridable;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   87 */   private Set<String> absoluteOrdering = null;
/*      */   
/*   89 */   public void createAbsoluteOrdering() { if (this.absoluteOrdering == null)
/*   90 */       this.absoluteOrdering = new LinkedHashSet();
/*      */   }
/*      */   
/*      */   public void addAbsoluteOrdering(String fragmentName) {
/*   94 */     createAbsoluteOrdering();
/*   95 */     this.absoluteOrdering.add(fragmentName);
/*      */   }
/*      */   
/*   98 */   public void addAbsoluteOrderingOthers() { createAbsoluteOrdering();
/*   99 */     this.absoluteOrdering.add("org.apache.catalina.order.others");
/*      */   }
/*      */   
/*  102 */   public Set<String> getAbsoluteOrdering() { return this.absoluteOrdering; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  109 */   private final Set<String> after = new LinkedHashSet();
/*      */   
/*  111 */   public void addAfterOrdering(String fragmentName) { this.after.add(fragmentName); }
/*      */   
/*      */   public void addAfterOrderingOthers() {
/*  114 */     if (this.before.contains("org.apache.catalina.order.others")) {
/*  115 */       throw new IllegalArgumentException(sm.getString("webXml.multipleOther"));
/*      */     }
/*      */     
/*  118 */     this.after.add("org.apache.catalina.order.others"); }
/*      */   
/*  120 */   public Set<String> getAfterOrdering() { return this.after; }
/*      */   
/*  122 */   private final Set<String> before = new LinkedHashSet();
/*      */   
/*  124 */   public void addBeforeOrdering(String fragmentName) { this.before.add(fragmentName); }
/*      */   
/*      */   public void addBeforeOrderingOthers() {
/*  127 */     if (this.after.contains("org.apache.catalina.order.others")) {
/*  128 */       throw new IllegalArgumentException(sm.getString("webXml.multipleOther"));
/*      */     }
/*      */     
/*  131 */     this.before.add("org.apache.catalina.order.others"); }
/*      */   
/*  133 */   public Set<String> getBeforeOrdering() { return this.before; }
/*      */   
/*      */ 
/*      */   public String getVersion()
/*      */   {
/*  138 */     StringBuilder sb = new StringBuilder(3);
/*  139 */     sb.append(this.majorVersion);
/*  140 */     sb.append('.');
/*  141 */     sb.append(this.minorVersion);
/*  142 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setVersion(String version)
/*      */   {
/*  149 */     if (version == null) {
/*  150 */       return;
/*      */     }
/*  152 */     switch (version) {
/*      */     case "2.4": 
/*  154 */       this.majorVersion = 2;
/*  155 */       this.minorVersion = 4;
/*  156 */       break;
/*      */     case "2.5": 
/*  158 */       this.majorVersion = 2;
/*  159 */       this.minorVersion = 5;
/*  160 */       break;
/*      */     case "3.0": 
/*  162 */       this.majorVersion = 3;
/*  163 */       this.minorVersion = 0;
/*  164 */       break;
/*      */     case "3.1": 
/*  166 */       this.majorVersion = 3;
/*  167 */       this.minorVersion = 1;
/*  168 */       break;
/*      */     default: 
/*  170 */       log.warn(sm.getString("webXml.version.unknown", new Object[] { version }));
/*      */     }
/*      */     
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*  177 */   private String publicId = null;
/*  178 */   public String getPublicId() { return this.publicId; }
/*      */   
/*      */   public void setPublicId(String publicId) {
/*  181 */     if (publicId == null) {
/*  182 */       return;
/*      */     }
/*  184 */     switch (publicId) {
/*      */     case "-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN": 
/*  186 */       this.majorVersion = 2;
/*  187 */       this.minorVersion = 2;
/*  188 */       this.publicId = publicId;
/*  189 */       break;
/*      */     case "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN": 
/*  191 */       this.majorVersion = 2;
/*  192 */       this.minorVersion = 3;
/*  193 */       this.publicId = publicId;
/*  194 */       break;
/*      */     default: 
/*  196 */       log.warn(sm.getString("webXml.unrecognisedPublicId", new Object[] { publicId }));
/*      */     }
/*      */     
/*      */   }
/*      */   
/*      */ 
/*  202 */   private boolean metadataComplete = false;
/*  203 */   public boolean isMetadataComplete() { return this.metadataComplete; }
/*      */   
/*  205 */   public void setMetadataComplete(boolean metadataComplete) { this.metadataComplete = metadataComplete; }
/*      */   
/*      */ 
/*  208 */   private String name = null;
/*  209 */   public String getName() { return this.name; }
/*      */   
/*  211 */   public void setName(String name) { if ("org.apache.catalina.order.others".equalsIgnoreCase(name))
/*      */     {
/*  213 */       log.warn(sm.getString("webXml.reservedName", new Object[] { name }));
/*      */     } else {
/*  215 */       this.name = name;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*  221 */   private int majorVersion = 3;
/*  222 */   private int minorVersion = 1;
/*  223 */   public int getMajorVersion() { return this.majorVersion; }
/*  224 */   public int getMinorVersion() { return this.minorVersion; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  232 */   private String displayName = null;
/*  233 */   public String getDisplayName() { return this.displayName; }
/*      */   
/*  235 */   public void setDisplayName(String displayName) { this.displayName = displayName; }
/*      */   
/*      */ 
/*      */ 
/*  239 */   private boolean distributable = false;
/*  240 */   public boolean isDistributable() { return this.distributable; }
/*      */   
/*  242 */   public void setDistributable(boolean distributable) { this.distributable = distributable; }
/*      */   
/*      */ 
/*      */ 
/*  246 */   private boolean denyUncoveredHttpMethods = false;
/*      */   
/*  248 */   public boolean getDenyUncoveredHttpMethods() { return this.denyUncoveredHttpMethods; }
/*      */   
/*      */   public void setDenyUncoveredHttpMethods(boolean denyUncoveredHttpMethods) {
/*  251 */     this.denyUncoveredHttpMethods = denyUncoveredHttpMethods;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*  256 */   private final Map<String, String> contextParams = new HashMap();
/*      */   
/*  258 */   public void addContextParam(String param, String value) { this.contextParams.put(param, value); }
/*      */   
/*  260 */   public Map<String, String> getContextParams() { return this.contextParams; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  267 */   private final Map<String, FilterDef> filters = new LinkedHashMap();
/*      */   
/*  269 */   public void addFilter(FilterDef filter) { if (this.filters.containsKey(filter.getFilterName()))
/*      */     {
/*      */ 
/*  272 */       throw new IllegalArgumentException(sm.getString("webXml.duplicateFilter", new Object[] {filter
/*  273 */         .getFilterName() }));
/*      */     }
/*  275 */     this.filters.put(filter.getFilterName(), filter); }
/*      */   
/*  277 */   public Map<String, FilterDef> getFilters() { return this.filters; }
/*      */   
/*      */ 
/*  280 */   private final Set<FilterMap> filterMaps = new LinkedHashSet();
/*  281 */   private final Set<String> filterMappingNames = new HashSet();
/*      */   
/*  283 */   public void addFilterMapping(FilterMap filterMap) { this.filterMaps.add(filterMap);
/*  284 */     this.filterMappingNames.add(filterMap.getFilterName()); }
/*      */   
/*  286 */   public Set<FilterMap> getFilterMappings() { return this.filterMaps; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  292 */   private final Set<String> listeners = new LinkedHashSet();
/*      */   
/*  294 */   public void addListener(String className) { this.listeners.add(className); }
/*      */   
/*  296 */   public Set<String> getListeners() { return this.listeners; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  304 */   private final Map<String, ServletDef> servlets = new HashMap();
/*      */   
/*  306 */   public void addServlet(ServletDef servletDef) { this.servlets.put(servletDef.getServletName(), servletDef);
/*  307 */     if (this.overridable)
/*  308 */       servletDef.setOverridable(this.overridable);
/*      */   }
/*      */   
/*  311 */   public Map<String, ServletDef> getServlets() { return this.servlets; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  316 */   private final Map<String, String> servletMappings = new HashMap();
/*  317 */   private final Set<String> servletMappingNames = new HashSet();
/*      */   
/*  319 */   public void addServletMapping(String urlPattern, String servletName) { addServletMappingDecoded(UDecoder.URLDecode(urlPattern, getCharset()), servletName); }
/*      */   
/*      */   public void addServletMappingDecoded(String urlPattern, String servletName) {
/*  322 */     String oldServletName = (String)this.servletMappings.put(urlPattern, servletName);
/*  323 */     if (oldServletName != null)
/*      */     {
/*      */ 
/*  326 */       throw new IllegalArgumentException(sm.getString("webXml.duplicateServletMapping", new Object[] { oldServletName, servletName, urlPattern }));
/*      */     }
/*      */     
/*      */ 
/*  330 */     this.servletMappingNames.add(servletName); }
/*      */   
/*  332 */   public Map<String, String> getServletMappings() { return this.servletMappings; }
/*      */   
/*      */ 
/*      */ 
/*  336 */   private SessionConfig sessionConfig = new SessionConfig();
/*      */   
/*  338 */   public void setSessionConfig(SessionConfig sessionConfig) { this.sessionConfig = sessionConfig; }
/*      */   
/*  340 */   public SessionConfig getSessionConfig() { return this.sessionConfig; }
/*      */   
/*      */ 
/*  343 */   private final Map<String, String> mimeMappings = new HashMap();
/*      */   
/*  345 */   public void addMimeMapping(String extension, String mimeType) { this.mimeMappings.put(extension, mimeType); }
/*      */   
/*  347 */   public Map<String, String> getMimeMappings() { return this.mimeMappings; }
/*      */   
/*      */ 
/*  350 */   private boolean replaceWelcomeFiles = false;
/*  351 */   private boolean alwaysAddWelcomeFiles = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setReplaceWelcomeFiles(boolean replaceWelcomeFiles)
/*      */   {
/*  359 */     this.replaceWelcomeFiles = replaceWelcomeFiles;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAlwaysAddWelcomeFiles(boolean alwaysAddWelcomeFiles)
/*      */   {
/*  367 */     this.alwaysAddWelcomeFiles = alwaysAddWelcomeFiles;
/*      */   }
/*      */   
/*      */ 
/*  371 */   private final Set<String> welcomeFiles = new LinkedHashSet();
/*      */   
/*  373 */   public void addWelcomeFile(String welcomeFile) { if (this.replaceWelcomeFiles) {
/*  374 */       this.welcomeFiles.clear();
/*  375 */       this.replaceWelcomeFiles = false;
/*      */     }
/*  377 */     this.welcomeFiles.add(welcomeFile); }
/*      */   
/*  379 */   public Set<String> getWelcomeFiles() { return this.welcomeFiles; }
/*      */   
/*      */ 
/*  382 */   private final Map<String, ErrorPage> errorPages = new HashMap();
/*      */   
/*  384 */   public void addErrorPage(ErrorPage errorPage) { this.errorPages.put(errorPage.getName(), errorPage); }
/*      */   
/*  386 */   public Map<String, ErrorPage> getErrorPages() { return this.errorPages; }
/*      */   
/*      */ 
/*      */ 
/*  390 */   private final Map<String, String> taglibs = new HashMap();
/*      */   
/*  392 */   public void addTaglib(String uri, String location) { if (this.taglibs.containsKey(uri))
/*      */     {
/*      */ 
/*  395 */       throw new IllegalArgumentException(sm.getString("webXml.duplicateTaglibUri", new Object[] { uri }));
/*      */     }
/*  397 */     this.taglibs.put(uri, location); }
/*      */   
/*  399 */   public Map<String, String> getTaglibs() { return this.taglibs; }
/*      */   
/*      */ 
/*  402 */   private final Set<JspPropertyGroup> jspPropertyGroups = new LinkedHashSet();
/*      */   
/*  404 */   public void addJspPropertyGroup(JspPropertyGroup propertyGroup) { propertyGroup.setCharset(getCharset());
/*  405 */     this.jspPropertyGroups.add(propertyGroup);
/*      */   }
/*      */   
/*  408 */   public Set<JspPropertyGroup> getJspPropertyGroups() { return this.jspPropertyGroups; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  414 */   private final Set<SecurityConstraint> securityConstraints = new HashSet();
/*      */   
/*  416 */   public void addSecurityConstraint(SecurityConstraint securityConstraint) { securityConstraint.setCharset(getCharset());
/*  417 */     this.securityConstraints.add(securityConstraint);
/*      */   }
/*      */   
/*  420 */   public Set<SecurityConstraint> getSecurityConstraints() { return this.securityConstraints; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  425 */   private LoginConfig loginConfig = null;
/*      */   
/*  427 */   public void setLoginConfig(LoginConfig loginConfig) { this.loginConfig = loginConfig; }
/*      */   
/*  429 */   public LoginConfig getLoginConfig() { return this.loginConfig; }
/*      */   
/*      */ 
/*      */ 
/*  433 */   private final Set<String> securityRoles = new HashSet();
/*      */   
/*  435 */   public void addSecurityRole(String securityRole) { this.securityRoles.add(securityRole); }
/*      */   
/*  437 */   public Set<String> getSecurityRoles() { return this.securityRoles; }
/*      */   
/*      */ 
/*      */ 
/*  441 */   private final Map<String, ContextEnvironment> envEntries = new HashMap();
/*      */   
/*  443 */   public void addEnvEntry(ContextEnvironment envEntry) { if (this.envEntries.containsKey(envEntry.getName()))
/*      */     {
/*      */ 
/*  446 */       throw new IllegalArgumentException(sm.getString("webXml.duplicateEnvEntry", new Object[] {envEntry
/*  447 */         .getName() }));
/*      */     }
/*  449 */     this.envEntries.put(envEntry.getName(), envEntry); }
/*      */   
/*  451 */   public Map<String, ContextEnvironment> getEnvEntries() { return this.envEntries; }
/*      */   
/*      */ 
/*      */ 
/*  455 */   private final Map<String, ContextEjb> ejbRefs = new HashMap();
/*      */   
/*  457 */   public void addEjbRef(ContextEjb ejbRef) { this.ejbRefs.put(ejbRef.getName(), ejbRef); }
/*      */   
/*  459 */   public Map<String, ContextEjb> getEjbRefs() { return this.ejbRefs; }
/*      */   
/*      */ 
/*      */ 
/*  463 */   private final Map<String, ContextLocalEjb> ejbLocalRefs = new HashMap();
/*      */   
/*  465 */   public void addEjbLocalRef(ContextLocalEjb ejbLocalRef) { this.ejbLocalRefs.put(ejbLocalRef.getName(), ejbLocalRef); }
/*      */   
/*      */   public Map<String, ContextLocalEjb> getEjbLocalRefs() {
/*  468 */     return this.ejbLocalRefs;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  475 */   private final Map<String, ContextService> serviceRefs = new HashMap();
/*      */   
/*  477 */   public void addServiceRef(ContextService serviceRef) { this.serviceRefs.put(serviceRef.getName(), serviceRef); }
/*      */   
/*  479 */   public Map<String, ContextService> getServiceRefs() { return this.serviceRefs; }
/*      */   
/*      */ 
/*      */ 
/*  483 */   private final Map<String, ContextResource> resourceRefs = new HashMap();
/*      */   
/*  485 */   public void addResourceRef(ContextResource resourceRef) { if (this.resourceRefs.containsKey(resourceRef.getName()))
/*      */     {
/*      */ 
/*  488 */       throw new IllegalArgumentException(sm.getString("webXml.duplicateResourceRef", new Object[] {resourceRef
/*  489 */         .getName() }));
/*      */     }
/*  491 */     this.resourceRefs.put(resourceRef.getName(), resourceRef);
/*      */   }
/*      */   
/*  494 */   public Map<String, ContextResource> getResourceRefs() { return this.resourceRefs; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  499 */   private final Map<String, ContextResourceEnvRef> resourceEnvRefs = new HashMap();
/*      */   
/*  501 */   public void addResourceEnvRef(ContextResourceEnvRef resourceEnvRef) { if (this.resourceEnvRefs.containsKey(resourceEnvRef.getName()))
/*      */     {
/*      */ 
/*  504 */       throw new IllegalArgumentException(sm.getString("webXml.duplicateResourceEnvRef", new Object[] {resourceEnvRef
/*  505 */         .getName() }));
/*      */     }
/*  507 */     this.resourceEnvRefs.put(resourceEnvRef.getName(), resourceEnvRef);
/*      */   }
/*      */   
/*  510 */   public Map<String, ContextResourceEnvRef> getResourceEnvRefs() { return this.resourceEnvRefs; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  515 */   private final Map<String, MessageDestinationRef> messageDestinationRefs = new HashMap();
/*      */   
/*      */   public void addMessageDestinationRef(MessageDestinationRef messageDestinationRef)
/*      */   {
/*  519 */     if (this.messageDestinationRefs.containsKey(messageDestinationRef
/*  520 */       .getName()))
/*      */     {
/*      */ 
/*  523 */       throw new IllegalArgumentException(sm.getString("webXml.duplicateMessageDestinationRef", new Object[] {messageDestinationRef
/*      */       
/*  525 */         .getName() }));
/*      */     }
/*  527 */     this.messageDestinationRefs.put(messageDestinationRef.getName(), messageDestinationRef);
/*      */   }
/*      */   
/*      */   public Map<String, MessageDestinationRef> getMessageDestinationRefs() {
/*  531 */     return this.messageDestinationRefs;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  538 */   private final Map<String, MessageDestination> messageDestinations = new HashMap();
/*      */   
/*      */   public void addMessageDestination(MessageDestination messageDestination)
/*      */   {
/*  542 */     if (this.messageDestinations.containsKey(messageDestination
/*  543 */       .getName()))
/*      */     {
/*      */ 
/*      */ 
/*  547 */       throw new IllegalArgumentException(sm.getString("webXml.duplicateMessageDestination", new Object[] {messageDestination
/*  548 */         .getName() }));
/*      */     }
/*  550 */     this.messageDestinations.put(messageDestination.getName(), messageDestination);
/*      */   }
/*      */   
/*      */   public Map<String, MessageDestination> getMessageDestinations() {
/*  554 */     return this.messageDestinations;
/*      */   }
/*      */   
/*      */ 
/*  558 */   private final Map<String, String> localeEncodingMappings = new HashMap();
/*      */   
/*  560 */   public void addLocaleEncodingMapping(String locale, String encoding) { this.localeEncodingMappings.put(locale, encoding); }
/*      */   
/*      */   public Map<String, String> getLocaleEncodingMappings() {
/*  563 */     return this.localeEncodingMappings;
/*      */   }
/*      */   
/*      */ 
/*  567 */   private Map<String, String> postConstructMethods = new HashMap();
/*      */   
/*  569 */   public void addPostConstructMethods(String clazz, String method) { if (!this.postConstructMethods.containsKey(clazz))
/*  570 */       this.postConstructMethods.put(clazz, method);
/*      */   }
/*      */   
/*      */   public Map<String, String> getPostConstructMethods() {
/*  574 */     return this.postConstructMethods;
/*      */   }
/*      */   
/*      */ 
/*  578 */   private Map<String, String> preDestroyMethods = new HashMap();
/*      */   
/*  580 */   public void addPreDestroyMethods(String clazz, String method) { if (!this.preDestroyMethods.containsKey(clazz))
/*  581 */       this.preDestroyMethods.put(clazz, method);
/*      */   }
/*      */   
/*      */   public Map<String, String> getPreDestroyMethods() {
/*  585 */     return this.preDestroyMethods;
/*      */   }
/*      */   
/*      */   public JspConfigDescriptor getJspConfigDescriptor() {
/*  589 */     if ((this.jspPropertyGroups.isEmpty()) && (this.taglibs.isEmpty())) {
/*  590 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  594 */     Collection<JspPropertyGroupDescriptor> descriptors = new ArrayList(this.jspPropertyGroups.size());
/*  595 */     for (Iterator localIterator = this.jspPropertyGroups.iterator(); localIterator.hasNext();) { jspPropertyGroup = (JspPropertyGroup)localIterator.next();
/*  596 */       JspPropertyGroupDescriptor descriptor = new JspPropertyGroupDescriptorImpl(jspPropertyGroup);
/*      */       
/*  598 */       descriptors.add(descriptor);
/*      */     }
/*      */     
/*      */     JspPropertyGroup jspPropertyGroup;
/*  602 */     Object tlds = new HashSet(this.taglibs.size());
/*  603 */     for (Map.Entry<String, String> entry : this.taglibs.entrySet())
/*      */     {
/*  605 */       TaglibDescriptor descriptor = new TaglibDescriptorImpl((String)entry.getValue(), (String)entry.getKey());
/*  606 */       ((Collection)tlds).add(descriptor);
/*      */     }
/*  608 */     return new JspConfigDescriptorImpl(descriptors, (Collection)tlds);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  614 */   private URL uRL = null;
/*  615 */   public void setURL(URL url) { this.uRL = url; }
/*  616 */   public URL getURL() { return this.uRL; }
/*      */   
/*      */ 
/*  619 */   private String jarName = null;
/*  620 */   public void setJarName(String jarName) { this.jarName = jarName; }
/*  621 */   public String getJarName() { return this.jarName; }
/*      */   
/*      */ 
/*      */ 
/*  625 */   private boolean webappJar = true;
/*  626 */   public void setWebappJar(boolean webappJar) { this.webappJar = webappJar; }
/*  627 */   public boolean getWebappJar() { return this.webappJar; }
/*      */   
/*      */ 
/*  630 */   private boolean delegate = false;
/*  631 */   public boolean getDelegate() { return this.delegate; }
/*  632 */   public void setDelegate(boolean delegate) { this.delegate = delegate; }
/*      */   
/*      */   public String toString()
/*      */   {
/*  636 */     StringBuilder buf = new StringBuilder(32);
/*  637 */     buf.append("Name: ");
/*  638 */     buf.append(getName());
/*  639 */     buf.append(", URL: ");
/*  640 */     buf.append(getURL());
/*  641 */     return buf.toString();
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
/*      */   public String toXml()
/*      */   {
/*  655 */     StringBuilder sb = new StringBuilder(2048);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  668 */     sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
/*      */     
/*      */     String javaeeNamespace;
/*  671 */     if (this.publicId != null) {
/*  672 */       sb.append("<!DOCTYPE web-app PUBLIC\n");
/*  673 */       sb.append("  \"");
/*  674 */       sb.append(this.publicId);
/*  675 */       sb.append("\"\n");
/*  676 */       sb.append("  \"");
/*  677 */       if ("-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN".equals(this.publicId)) {
/*  678 */         sb.append("http://java.sun.com/dtd/web-app_2_2.dtd");
/*      */       } else {
/*  680 */         sb.append("http://java.sun.com/dtd/web-app_2_3.dtd");
/*      */       }
/*  682 */       sb.append("\">\n");
/*  683 */       sb.append("<web-app>");
/*      */     } else {
/*  685 */       javaeeNamespace = null;
/*  686 */       String webXmlSchemaLocation = null;
/*  687 */       String version = getVersion();
/*  688 */       if ("2.4".equals(version)) {
/*  689 */         javaeeNamespace = "http://java.sun.com/xml/ns/j2ee";
/*  690 */         webXmlSchemaLocation = "http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd";
/*  691 */       } else if ("2.5".equals(version)) {
/*  692 */         javaeeNamespace = "http://java.sun.com/xml/ns/javaee";
/*  693 */         webXmlSchemaLocation = "http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd";
/*  694 */       } else if ("3.0".equals(version)) {
/*  695 */         javaeeNamespace = "http://java.sun.com/xml/ns/javaee";
/*  696 */         webXmlSchemaLocation = "http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd";
/*  697 */       } else if ("3.1".equals(version)) {
/*  698 */         javaeeNamespace = "http://xmlns.jcp.org/xml/ns/javaee";
/*  699 */         webXmlSchemaLocation = "http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd";
/*      */       }
/*  701 */       sb.append("<web-app xmlns=\"");
/*  702 */       sb.append(javaeeNamespace);
/*  703 */       sb.append("\"\n");
/*  704 */       sb.append("         xmlns:xsi=");
/*  705 */       sb.append("\"http://www.w3.org/2001/XMLSchema-instance\"\n");
/*  706 */       sb.append("         xsi:schemaLocation=\"");
/*  707 */       sb.append(javaeeNamespace);
/*  708 */       sb.append(" ");
/*  709 */       sb.append(webXmlSchemaLocation);
/*  710 */       sb.append("\"\n");
/*  711 */       sb.append("         version=\"");
/*  712 */       sb.append(getVersion());
/*  713 */       sb.append("\"");
/*  714 */       if ("2.4".equals(version)) {
/*  715 */         sb.append(">\n\n");
/*      */       } else {
/*  717 */         sb.append("\n         metadata-complete=\"true\">\n\n");
/*      */       }
/*      */     }
/*      */     
/*  721 */     appendElement(sb, "  ", "display-name", this.displayName);
/*      */     
/*  723 */     if (isDistributable()) {
/*  724 */       sb.append("  <distributable/>\n\n");
/*      */     }
/*      */     
/*  727 */     for (Map.Entry<String, String> entry : this.contextParams.entrySet()) {
/*  728 */       sb.append("  <context-param>\n");
/*  729 */       appendElement(sb, "    ", "param-name", (String)entry.getKey());
/*  730 */       appendElement(sb, "    ", "param-value", (String)entry.getValue());
/*  731 */       sb.append("  </context-param>\n");
/*      */     }
/*  733 */     sb.append('\n');
/*      */     
/*      */     Object localObject1;
/*  736 */     if ((getMajorVersion() > 2) || (getMinorVersion() > 2)) {
/*  737 */       for (Map.Entry<String, FilterDef> entry : this.filters.entrySet()) {
/*  738 */         filterDef = (FilterDef)entry.getValue();
/*  739 */         sb.append("  <filter>\n");
/*  740 */         appendElement(sb, "    ", "description", filterDef
/*  741 */           .getDescription());
/*  742 */         appendElement(sb, "    ", "display-name", filterDef
/*  743 */           .getDisplayName());
/*  744 */         appendElement(sb, "    ", "filter-name", filterDef
/*  745 */           .getFilterName());
/*  746 */         appendElement(sb, "    ", "filter-class", filterDef
/*  747 */           .getFilterClass());
/*      */         
/*  749 */         if (getMajorVersion() != 2) {
/*  750 */           appendElement(sb, "    ", "async-supported", filterDef
/*  751 */             .getAsyncSupported());
/*      */         }
/*      */         
/*  754 */         for (Iterator localIterator = filterDef.getParameterMap().entrySet().iterator(); localIterator.hasNext();) { param = (Map.Entry)localIterator.next();
/*  755 */           sb.append("    <init-param>\n");
/*  756 */           appendElement(sb, "      ", "param-name", (String)param.getKey());
/*  757 */           appendElement(sb, "      ", "param-value", (String)param.getValue());
/*  758 */           sb.append("    </init-param>\n");
/*      */         }
/*  760 */         sb.append("  </filter>\n"); }
/*      */       FilterDef filterDef;
/*  762 */       Map.Entry<String, String> param; sb.append('\n');
/*      */       
/*  764 */       for (FilterMap filterMap : this.filterMaps) {
/*  765 */         sb.append("  <filter-mapping>\n");
/*  766 */         appendElement(sb, "    ", "filter-name", filterMap
/*  767 */           .getFilterName());
/*  768 */         if (filterMap.getMatchAllServletNames()) {
/*  769 */           sb.append("    <servlet-name>*</servlet-name>\n");
/*      */         } else {
/*  771 */           filterDef = filterMap.getServletNames();Map.Entry<String, String> localEntry1 = filterDef.length; for (param = 0; param < localEntry1; param++) { String servletName = filterDef[param];
/*  772 */             appendElement(sb, "    ", "servlet-name", servletName);
/*      */           }
/*      */         }
/*  775 */         if (filterMap.getMatchAllUrlPatterns()) {
/*  776 */           sb.append("    <url-pattern>*</url-pattern>\n");
/*      */         } else {
/*  778 */           filterDef = filterMap.getURLPatterns();Map.Entry<String, String> localEntry2 = filterDef.length; for (param = 0; param < localEntry2; param++) { String urlPattern = filterDef[param];
/*  779 */             appendElement(sb, "    ", "url-pattern", encodeUrl(urlPattern));
/*      */           }
/*      */         }
/*      */         
/*  783 */         if ((getMajorVersion() > 2) || (getMinorVersion() > 3)) {
/*  784 */           filterDef = filterMap.getDispatcherNames();localObject1 = filterDef.length; for (param = 0; param < localObject1; param++) { String dispatcher = filterDef[param];
/*  785 */             if ((getMajorVersion() != 2) || 
/*  786 */               (!DispatcherType.ASYNC.name().equals(dispatcher)))
/*      */             {
/*      */ 
/*  789 */               appendElement(sb, "    ", "dispatcher", dispatcher); }
/*      */           }
/*      */         }
/*  792 */         sb.append("  </filter-mapping>\n");
/*      */       }
/*  794 */       sb.append('\n');
/*      */     }
/*      */     
/*      */ 
/*  798 */     if ((getMajorVersion() > 2) || (getMinorVersion() > 2)) {
/*  799 */       for (String listener : this.listeners) {
/*  800 */         sb.append("  <listener>\n");
/*  801 */         appendElement(sb, "    ", "listener-class", listener);
/*  802 */         sb.append("  </listener>\n");
/*      */       }
/*  804 */       sb.append('\n');
/*      */     }
/*      */     
/*  807 */     for (Map.Entry<String, ServletDef> entry : this.servlets.entrySet()) {
/*  808 */       ServletDef servletDef = (ServletDef)entry.getValue();
/*  809 */       sb.append("  <servlet>\n");
/*  810 */       appendElement(sb, "    ", "description", servletDef
/*  811 */         .getDescription());
/*  812 */       appendElement(sb, "    ", "display-name", servletDef
/*  813 */         .getDisplayName());
/*  814 */       appendElement(sb, "    ", "servlet-name", (String)entry.getKey());
/*  815 */       appendElement(sb, "    ", "servlet-class", servletDef
/*  816 */         .getServletClass());
/*  817 */       appendElement(sb, "    ", "jsp-file", servletDef.getJspFile());
/*      */       
/*  819 */       for (localObject1 = servletDef.getParameterMap().entrySet().iterator(); ((Iterator)localObject1).hasNext();) { Map.Entry<String, String> param = (Map.Entry)((Iterator)localObject1).next();
/*  820 */         sb.append("    <init-param>\n");
/*  821 */         appendElement(sb, "      ", "param-name", (String)param.getKey());
/*  822 */         appendElement(sb, "      ", "param-value", (String)param.getValue());
/*  823 */         sb.append("    </init-param>\n");
/*      */       }
/*  825 */       appendElement(sb, "    ", "load-on-startup", servletDef
/*  826 */         .getLoadOnStartup());
/*  827 */       appendElement(sb, "    ", "enabled", servletDef.getEnabled());
/*      */       
/*  829 */       if (getMajorVersion() != 2) {
/*  830 */         appendElement(sb, "    ", "async-supported", servletDef
/*  831 */           .getAsyncSupported());
/*      */       }
/*      */       
/*  834 */       if (((getMajorVersion() > 2) || (getMinorVersion() > 2)) && 
/*  835 */         (servletDef.getRunAs() != null)) {
/*  836 */         sb.append("    <run-as>\n");
/*  837 */         appendElement(sb, "      ", "role-name", servletDef.getRunAs());
/*  838 */         sb.append("    </run-as>\n");
/*      */       }
/*      */       
/*  841 */       for (localObject1 = servletDef.getSecurityRoleRefs().iterator(); ((Iterator)localObject1).hasNext();) { roleRef = (SecurityRoleRef)((Iterator)localObject1).next();
/*  842 */         sb.append("    <security-role-ref>\n");
/*  843 */         appendElement(sb, "      ", "role-name", roleRef.getName());
/*  844 */         appendElement(sb, "      ", "role-link", roleRef.getLink());
/*  845 */         sb.append("    </security-role-ref>\n");
/*      */       }
/*      */       
/*  848 */       if (getMajorVersion() != 2) {
/*  849 */         MultipartDef multipartDef = servletDef.getMultipartDef();
/*  850 */         if (multipartDef != null) {
/*  851 */           sb.append("    <multipart-config>\n");
/*  852 */           appendElement(sb, "      ", "location", multipartDef
/*  853 */             .getLocation());
/*  854 */           appendElement(sb, "      ", "max-file-size", multipartDef
/*  855 */             .getMaxFileSize());
/*  856 */           appendElement(sb, "      ", "max-request-size", multipartDef
/*  857 */             .getMaxRequestSize());
/*  858 */           appendElement(sb, "      ", "file-size-threshold", multipartDef
/*  859 */             .getFileSizeThreshold());
/*  860 */           sb.append("    </multipart-config>\n");
/*      */         }
/*      */       }
/*  863 */       sb.append("  </servlet>\n"); }
/*      */     SecurityRoleRef roleRef;
/*  865 */     sb.append('\n');
/*      */     
/*  867 */     for (Map.Entry<String, String> entry : this.servletMappings.entrySet()) {
/*  868 */       sb.append("  <servlet-mapping>\n");
/*  869 */       appendElement(sb, "    ", "servlet-name", (String)entry.getValue());
/*  870 */       appendElement(sb, "    ", "url-pattern", encodeUrl((String)entry.getKey()));
/*  871 */       sb.append("  </servlet-mapping>\n");
/*      */     }
/*  873 */     sb.append('\n');
/*      */     
/*  875 */     if (this.sessionConfig != null) {
/*  876 */       sb.append("  <session-config>\n");
/*  877 */       appendElement(sb, "    ", "session-timeout", this.sessionConfig
/*  878 */         .getSessionTimeout());
/*  879 */       if (this.majorVersion >= 3) {
/*  880 */         sb.append("    <cookie-config>\n");
/*  881 */         appendElement(sb, "      ", "name", this.sessionConfig.getCookieName());
/*  882 */         appendElement(sb, "      ", "domain", this.sessionConfig
/*  883 */           .getCookieDomain());
/*  884 */         appendElement(sb, "      ", "path", this.sessionConfig.getCookiePath());
/*  885 */         appendElement(sb, "      ", "comment", this.sessionConfig
/*  886 */           .getCookieComment());
/*  887 */         appendElement(sb, "      ", "http-only", this.sessionConfig
/*  888 */           .getCookieHttpOnly());
/*  889 */         appendElement(sb, "      ", "secure", this.sessionConfig
/*  890 */           .getCookieSecure());
/*  891 */         appendElement(sb, "      ", "max-age", this.sessionConfig
/*  892 */           .getCookieMaxAge());
/*  893 */         sb.append("    </cookie-config>\n");
/*      */         
/*  895 */         for (SessionTrackingMode stm : this.sessionConfig.getSessionTrackingModes()) {
/*  896 */           appendElement(sb, "    ", "tracking-mode", stm.name());
/*      */         }
/*      */       }
/*  899 */       sb.append("  </session-config>\n\n");
/*      */     }
/*      */     
/*  902 */     for (Map.Entry<String, String> entry : this.mimeMappings.entrySet()) {
/*  903 */       sb.append("  <mime-mapping>\n");
/*  904 */       appendElement(sb, "    ", "extension", (String)entry.getKey());
/*  905 */       appendElement(sb, "    ", "mime-type", (String)entry.getValue());
/*  906 */       sb.append("  </mime-mapping>\n");
/*      */     }
/*  908 */     sb.append('\n');
/*      */     
/*  910 */     if (this.welcomeFiles.size() > 0) {
/*  911 */       sb.append("  <welcome-file-list>\n");
/*  912 */       for (String welcomeFile : this.welcomeFiles) {
/*  913 */         appendElement(sb, "    ", "welcome-file", welcomeFile);
/*      */       }
/*  915 */       sb.append("  </welcome-file-list>\n\n");
/*      */     }
/*      */     
/*  918 */     for (ErrorPage errorPage : this.errorPages.values()) {
/*  919 */       exceptionType = errorPage.getExceptionType();
/*  920 */       int errorCode = errorPage.getErrorCode();
/*      */       
/*  922 */       if ((exceptionType != null) || (errorCode != 0) || (getMajorVersion() != 2))
/*      */       {
/*      */ 
/*      */ 
/*  926 */         sb.append("  <error-page>\n");
/*  927 */         if (errorPage.getExceptionType() != null) {
/*  928 */           appendElement(sb, "    ", "exception-type", exceptionType);
/*  929 */         } else if (errorPage.getErrorCode() > 0) {
/*  930 */           appendElement(sb, "    ", "error-code", 
/*  931 */             Integer.toString(errorCode));
/*      */         }
/*  933 */         appendElement(sb, "    ", "location", errorPage.getLocation());
/*  934 */         sb.append("  </error-page>\n"); } }
/*      */     String exceptionType;
/*  936 */     sb.append('\n');
/*      */     
/*      */ 
/*      */ 
/*  940 */     if ((this.taglibs.size() > 0) || (this.jspPropertyGroups.size() > 0)) {
/*  941 */       if ((getMajorVersion() > 2) || (getMinorVersion() > 3)) {
/*  942 */         sb.append("  <jsp-config>\n");
/*      */       }
/*  944 */       for (Map.Entry<String, String> entry : this.taglibs.entrySet()) {
/*  945 */         sb.append("    <taglib>\n");
/*  946 */         appendElement(sb, "      ", "taglib-uri", (String)entry.getKey());
/*  947 */         appendElement(sb, "      ", "taglib-location", (String)entry.getValue());
/*  948 */         sb.append("    </taglib>\n");
/*      */       }
/*  950 */       if ((getMajorVersion() > 2) || (getMinorVersion() > 3)) {
/*  951 */         for (JspPropertyGroup jpg : this.jspPropertyGroups) {
/*  952 */           sb.append("    <jsp-property-group>\n");
/*  953 */           for (String urlPattern : jpg.getUrlPatterns()) {
/*  954 */             appendElement(sb, "      ", "url-pattern", encodeUrl(urlPattern));
/*      */           }
/*  956 */           appendElement(sb, "      ", "el-ignored", jpg.getElIgnored());
/*  957 */           appendElement(sb, "      ", "page-encoding", jpg
/*  958 */             .getPageEncoding());
/*  959 */           appendElement(sb, "      ", "scripting-invalid", jpg
/*  960 */             .getScriptingInvalid());
/*  961 */           appendElement(sb, "      ", "is-xml", jpg.getIsXml());
/*  962 */           for (String prelude : jpg.getIncludePreludes()) {
/*  963 */             appendElement(sb, "      ", "include-prelude", prelude);
/*      */           }
/*  965 */           for (String coda : jpg.getIncludeCodas()) {
/*  966 */             appendElement(sb, "      ", "include-coda", coda);
/*      */           }
/*  968 */           appendElement(sb, "      ", "deferred-syntax-allowed-as-literal", jpg
/*  969 */             .getDeferredSyntax());
/*  970 */           appendElement(sb, "      ", "trim-directive-whitespaces", jpg
/*  971 */             .getTrimWhitespace());
/*  972 */           appendElement(sb, "      ", "default-content-type", jpg
/*  973 */             .getDefaultContentType());
/*  974 */           appendElement(sb, "      ", "buffer", jpg.getBuffer());
/*  975 */           appendElement(sb, "      ", "error-on-undeclared-namespace", jpg
/*  976 */             .getErrorOnUndeclaredNamespace());
/*  977 */           sb.append("    </jsp-property-group>\n");
/*      */         }
/*  979 */         sb.append("  </jsp-config>\n\n");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  984 */     if ((getMajorVersion() > 2) || (getMinorVersion() > 2)) {
/*  985 */       for (ContextResourceEnvRef resourceEnvRef : this.resourceEnvRefs.values()) {
/*  986 */         sb.append("  <resource-env-ref>\n");
/*  987 */         appendElement(sb, "    ", "description", resourceEnvRef
/*  988 */           .getDescription());
/*  989 */         appendElement(sb, "    ", "resource-env-ref-name", resourceEnvRef
/*  990 */           .getName());
/*  991 */         appendElement(sb, "    ", "resource-env-ref-type", resourceEnvRef
/*  992 */           .getType());
/*      */         
/*      */ 
/*  995 */         for (InjectionTarget target : resourceEnvRef.getInjectionTargets()) {
/*  996 */           sb.append("    <injection-target>\n");
/*  997 */           appendElement(sb, "      ", "injection-target-class", target
/*  998 */             .getTargetClass());
/*  999 */           appendElement(sb, "      ", "injection-target-name", target
/* 1000 */             .getTargetName());
/* 1001 */           sb.append("    </injection-target>\n");
/*      */         }
/*      */         
/* 1004 */         sb.append("  </resource-env-ref>\n");
/*      */       }
/* 1006 */       sb.append('\n');
/*      */     }
/*      */     
/* 1009 */     for (ContextResource resourceRef : this.resourceRefs.values()) {
/* 1010 */       sb.append("  <resource-ref>\n");
/* 1011 */       appendElement(sb, "    ", "description", resourceRef
/* 1012 */         .getDescription());
/* 1013 */       appendElement(sb, "    ", "res-ref-name", resourceRef.getName());
/* 1014 */       appendElement(sb, "    ", "res-type", resourceRef.getType());
/* 1015 */       appendElement(sb, "    ", "res-auth", resourceRef.getAuth());
/*      */       
/* 1017 */       if ((getMajorVersion() > 2) || (getMinorVersion() > 2)) {
/* 1018 */         appendElement(sb, "    ", "res-sharing-scope", resourceRef
/* 1019 */           .getScope());
/*      */       }
/*      */       
/* 1022 */       for (exceptionType = resourceRef.getInjectionTargets().iterator(); exceptionType.hasNext();) { target = (InjectionTarget)exceptionType.next();
/* 1023 */         sb.append("    <injection-target>\n");
/* 1024 */         appendElement(sb, "      ", "injection-target-class", target
/* 1025 */           .getTargetClass());
/* 1026 */         appendElement(sb, "      ", "injection-target-name", target
/* 1027 */           .getTargetName());
/* 1028 */         sb.append("    </injection-target>\n");
/*      */       }
/*      */       
/* 1031 */       sb.append("  </resource-ref>\n"); }
/*      */     InjectionTarget target;
/* 1033 */     sb.append('\n');
/*      */     
/* 1035 */     for (SecurityConstraint constraint : this.securityConstraints) {
/* 1036 */       sb.append("  <security-constraint>\n");
/*      */       
/* 1038 */       if ((getMajorVersion() > 2) || (getMinorVersion() > 2)) {
/* 1039 */         appendElement(sb, "    ", "display-name", constraint
/* 1040 */           .getDisplayName());
/*      */       }
/* 1042 */       exceptionType = constraint.findCollections();SecurityRoleRef localSecurityRoleRef1 = exceptionType.length; for (roleRef = 0; roleRef < localSecurityRoleRef1; roleRef++) { SecurityCollection collection = exceptionType[roleRef];
/* 1043 */         sb.append("    <web-resource-collection>\n");
/* 1044 */         appendElement(sb, "      ", "web-resource-name", collection
/* 1045 */           .getName());
/* 1046 */         appendElement(sb, "      ", "description", collection
/* 1047 */           .getDescription());
/* 1048 */         for (String urlPattern : collection.findPatterns()) {
/* 1049 */           appendElement(sb, "      ", "url-pattern", encodeUrl(urlPattern));
/*      */         }
/* 1051 */         for (String method : collection.findMethods()) {
/* 1052 */           appendElement(sb, "      ", "http-method", method);
/*      */         }
/* 1054 */         for (String method : collection.findOmittedMethods()) {
/* 1055 */           appendElement(sb, "      ", "http-method-omission", method);
/*      */         }
/* 1057 */         sb.append("    </web-resource-collection>\n");
/*      */       }
/* 1059 */       if (constraint.findAuthRoles().length > 0) {
/* 1060 */         sb.append("    <auth-constraint>\n");
/* 1061 */         exceptionType = constraint.findAuthRoles();SecurityRoleRef localSecurityRoleRef2 = exceptionType.length; for (roleRef = 0; roleRef < localSecurityRoleRef2; roleRef++) { String role = exceptionType[roleRef];
/* 1062 */           appendElement(sb, "      ", "role-name", role);
/*      */         }
/* 1064 */         sb.append("    </auth-constraint>\n");
/*      */       }
/* 1066 */       if (constraint.getUserConstraint() != null) {
/* 1067 */         sb.append("    <user-data-constraint>\n");
/* 1068 */         appendElement(sb, "      ", "transport-guarantee", constraint
/* 1069 */           .getUserConstraint());
/* 1070 */         sb.append("    </user-data-constraint>\n");
/*      */       }
/* 1072 */       sb.append("  </security-constraint>\n");
/*      */     }
/* 1074 */     sb.append('\n');
/*      */     
/* 1076 */     if (this.loginConfig != null) {
/* 1077 */       sb.append("  <login-config>\n");
/* 1078 */       appendElement(sb, "    ", "auth-method", this.loginConfig
/* 1079 */         .getAuthMethod());
/* 1080 */       appendElement(sb, "    ", "realm-name", this.loginConfig
/* 1081 */         .getRealmName());
/* 1082 */       if ((this.loginConfig.getErrorPage() != null) || 
/* 1083 */         (this.loginConfig.getLoginPage() != null)) {
/* 1084 */         sb.append("    <form-login-config>\n");
/* 1085 */         appendElement(sb, "      ", "form-login-page", this.loginConfig
/* 1086 */           .getLoginPage());
/* 1087 */         appendElement(sb, "      ", "form-error-page", this.loginConfig
/* 1088 */           .getErrorPage());
/* 1089 */         sb.append("    </form-login-config>\n");
/*      */       }
/* 1091 */       sb.append("  </login-config>\n\n");
/*      */     }
/*      */     
/* 1094 */     for (String roleName : this.securityRoles) {
/* 1095 */       sb.append("  <security-role>\n");
/* 1096 */       appendElement(sb, "    ", "role-name", roleName);
/* 1097 */       sb.append("  </security-role>\n");
/*      */     }
/*      */     
/* 1100 */     for (ContextEnvironment envEntry : this.envEntries.values()) {
/* 1101 */       sb.append("  <env-entry>\n");
/* 1102 */       appendElement(sb, "    ", "description", envEntry
/* 1103 */         .getDescription());
/* 1104 */       appendElement(sb, "    ", "env-entry-name", envEntry.getName());
/* 1105 */       appendElement(sb, "    ", "env-entry-type", envEntry.getType());
/* 1106 */       appendElement(sb, "    ", "env-entry-value", envEntry.getValue());
/*      */       
/* 1108 */       for (InjectionTarget target : envEntry.getInjectionTargets()) {
/* 1109 */         sb.append("    <injection-target>\n");
/* 1110 */         appendElement(sb, "      ", "injection-target-class", target
/* 1111 */           .getTargetClass());
/* 1112 */         appendElement(sb, "      ", "injection-target-name", target
/* 1113 */           .getTargetName());
/* 1114 */         sb.append("    </injection-target>\n");
/*      */       }
/*      */       
/* 1117 */       sb.append("  </env-entry>\n");
/*      */     }
/* 1119 */     sb.append('\n');
/*      */     
/* 1121 */     for (ContextEjb ejbRef : this.ejbRefs.values()) {
/* 1122 */       sb.append("  <ejb-ref>\n");
/* 1123 */       appendElement(sb, "    ", "description", ejbRef.getDescription());
/* 1124 */       appendElement(sb, "    ", "ejb-ref-name", ejbRef.getName());
/* 1125 */       appendElement(sb, "    ", "ejb-ref-type", ejbRef.getType());
/* 1126 */       appendElement(sb, "    ", "home", ejbRef.getHome());
/* 1127 */       appendElement(sb, "    ", "remote", ejbRef.getRemote());
/* 1128 */       appendElement(sb, "    ", "ejb-link", ejbRef.getLink());
/*      */       
/* 1130 */       for (InjectionTarget target : ejbRef.getInjectionTargets()) {
/* 1131 */         sb.append("    <injection-target>\n");
/* 1132 */         appendElement(sb, "      ", "injection-target-class", target
/* 1133 */           .getTargetClass());
/* 1134 */         appendElement(sb, "      ", "injection-target-name", target
/* 1135 */           .getTargetName());
/* 1136 */         sb.append("    </injection-target>\n");
/*      */       }
/*      */       
/* 1139 */       sb.append("  </ejb-ref>\n");
/*      */     }
/* 1141 */     sb.append('\n');
/*      */     
/*      */ 
/* 1144 */     if ((getMajorVersion() > 2) || (getMinorVersion() > 2)) {
/* 1145 */       for (ContextLocalEjb ejbLocalRef : this.ejbLocalRefs.values()) {
/* 1146 */         sb.append("  <ejb-local-ref>\n");
/* 1147 */         appendElement(sb, "    ", "description", ejbLocalRef
/* 1148 */           .getDescription());
/* 1149 */         appendElement(sb, "    ", "ejb-ref-name", ejbLocalRef.getName());
/* 1150 */         appendElement(sb, "    ", "ejb-ref-type", ejbLocalRef.getType());
/* 1151 */         appendElement(sb, "    ", "local-home", ejbLocalRef.getHome());
/* 1152 */         appendElement(sb, "    ", "local", ejbLocalRef.getLocal());
/* 1153 */         appendElement(sb, "    ", "ejb-link", ejbLocalRef.getLink());
/*      */         
/* 1155 */         for (InjectionTarget target : ejbLocalRef.getInjectionTargets()) {
/* 1156 */           sb.append("    <injection-target>\n");
/* 1157 */           appendElement(sb, "      ", "injection-target-class", target
/* 1158 */             .getTargetClass());
/* 1159 */           appendElement(sb, "      ", "injection-target-name", target
/* 1160 */             .getTargetName());
/* 1161 */           sb.append("    </injection-target>\n");
/*      */         }
/*      */         
/* 1164 */         sb.append("  </ejb-local-ref>\n");
/*      */       }
/* 1166 */       sb.append('\n');
/*      */     }
/*      */     
/*      */     String qname;
/* 1170 */     if ((getMajorVersion() > 2) || (getMinorVersion() > 3)) {
/* 1171 */       for (ContextService serviceRef : this.serviceRefs.values()) {
/* 1172 */         sb.append("  <service-ref>\n");
/* 1173 */         appendElement(sb, "    ", "description", serviceRef
/* 1174 */           .getDescription());
/* 1175 */         appendElement(sb, "    ", "display-name", serviceRef
/* 1176 */           .getDisplayname());
/* 1177 */         appendElement(sb, "    ", "service-ref-name", serviceRef
/* 1178 */           .getName());
/* 1179 */         appendElement(sb, "    ", "service-interface", serviceRef
/* 1180 */           .getInterface());
/* 1181 */         appendElement(sb, "    ", "service-ref-type", serviceRef
/* 1182 */           .getType());
/* 1183 */         appendElement(sb, "    ", "wsdl-file", serviceRef.getWsdlfile());
/* 1184 */         appendElement(sb, "    ", "jaxrpc-mapping-file", serviceRef
/* 1185 */           .getJaxrpcmappingfile());
/* 1186 */         qname = serviceRef.getServiceqnameNamespaceURI();
/* 1187 */         if (qname != null) {
/* 1188 */           qname = qname + ":";
/*      */         }
/* 1190 */         qname = qname + serviceRef.getServiceqnameLocalpart();
/* 1191 */         appendElement(sb, "    ", "service-qname", qname);
/* 1192 */         Object endpointIter = serviceRef.getServiceendpoints();
/* 1193 */         while (((Iterator)endpointIter).hasNext()) {
/* 1194 */           String endpoint = (String)((Iterator)endpointIter).next();
/* 1195 */           sb.append("    <port-component-ref>\n");
/* 1196 */           appendElement(sb, "      ", "service-endpoint-interface", endpoint);
/*      */           
/* 1198 */           appendElement(sb, "      ", "port-component-link", serviceRef
/* 1199 */             .getProperty(endpoint));
/* 1200 */           sb.append("    </port-component-ref>\n");
/*      */         }
/* 1202 */         Iterator<String> handlerIter = serviceRef.getHandlers();
/* 1203 */         String handler; while (handlerIter.hasNext()) {
/* 1204 */           handler = (String)handlerIter.next();
/* 1205 */           sb.append("    <handler>\n");
/* 1206 */           ContextHandler ch = serviceRef.getHandler(handler);
/* 1207 */           appendElement(sb, "      ", "handler-name", ch.getName());
/* 1208 */           appendElement(sb, "      ", "handler-class", ch
/* 1209 */             .getHandlerclass());
/* 1210 */           sb.append("    </handler>\n");
/*      */         }
/*      */         
/*      */ 
/* 1214 */         for (InjectionTarget target : serviceRef.getInjectionTargets()) {
/* 1215 */           sb.append("    <injection-target>\n");
/* 1216 */           appendElement(sb, "      ", "injection-target-class", target
/* 1217 */             .getTargetClass());
/* 1218 */           appendElement(sb, "      ", "injection-target-name", target
/* 1219 */             .getTargetName());
/* 1220 */           sb.append("    </injection-target>\n");
/*      */         }
/*      */         
/* 1223 */         sb.append("  </service-ref>\n");
/*      */       }
/* 1225 */       sb.append('\n');
/*      */     }
/*      */     
/* 1228 */     if (!this.postConstructMethods.isEmpty()) {
/* 1229 */       for (Map.Entry<String, String> entry : this.postConstructMethods
/* 1230 */         .entrySet()) {
/* 1231 */         sb.append("  <post-construct>\n");
/* 1232 */         appendElement(sb, "    ", "lifecycle-callback-class", 
/* 1233 */           (String)entry.getKey());
/* 1234 */         appendElement(sb, "    ", "lifecycle-callback-method", 
/* 1235 */           (String)entry.getValue());
/* 1236 */         sb.append("  </post-construct>\n");
/*      */       }
/* 1238 */       sb.append('\n');
/*      */     }
/*      */     
/* 1241 */     if (!this.preDestroyMethods.isEmpty()) {
/* 1242 */       for (Map.Entry<String, String> entry : this.preDestroyMethods
/* 1243 */         .entrySet()) {
/* 1244 */         sb.append("  <pre-destroy>\n");
/* 1245 */         appendElement(sb, "    ", "lifecycle-callback-class", 
/* 1246 */           (String)entry.getKey());
/* 1247 */         appendElement(sb, "    ", "lifecycle-callback-method", 
/* 1248 */           (String)entry.getValue());
/* 1249 */         sb.append("  </pre-destroy>\n");
/*      */       }
/* 1251 */       sb.append('\n');
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1256 */     if ((getMajorVersion() > 2) || (getMinorVersion() > 3)) {
/* 1257 */       for (MessageDestinationRef mdr : this.messageDestinationRefs.values()) {
/* 1258 */         sb.append("  <message-destination-ref>\n");
/* 1259 */         appendElement(sb, "    ", "description", mdr.getDescription());
/* 1260 */         appendElement(sb, "    ", "message-destination-ref-name", mdr
/* 1261 */           .getName());
/* 1262 */         appendElement(sb, "    ", "message-destination-type", mdr
/* 1263 */           .getType());
/* 1264 */         appendElement(sb, "    ", "message-destination-usage", mdr
/* 1265 */           .getUsage());
/* 1266 */         appendElement(sb, "    ", "message-destination-link", mdr
/* 1267 */           .getLink());
/*      */         
/* 1269 */         for (InjectionTarget target : mdr.getInjectionTargets()) {
/* 1270 */           sb.append("    <injection-target>\n");
/* 1271 */           appendElement(sb, "      ", "injection-target-class", target
/* 1272 */             .getTargetClass());
/* 1273 */           appendElement(sb, "      ", "injection-target-name", target
/* 1274 */             .getTargetName());
/* 1275 */           sb.append("    </injection-target>\n");
/*      */         }
/*      */         
/* 1278 */         sb.append("  </message-destination-ref>\n");
/*      */       }
/* 1280 */       sb.append('\n');
/*      */       
/* 1282 */       for (MessageDestination md : this.messageDestinations.values()) {
/* 1283 */         sb.append("  <message-destination>\n");
/* 1284 */         appendElement(sb, "    ", "description", md.getDescription());
/* 1285 */         appendElement(sb, "    ", "display-name", md.getDisplayName());
/* 1286 */         appendElement(sb, "    ", "message-destination-name", md
/* 1287 */           .getName());
/*      */         
/* 1289 */         sb.append("  </message-destination>\n");
/*      */       }
/* 1291 */       sb.append('\n');
/*      */     }
/*      */     
/*      */ 
/* 1295 */     if (((getMajorVersion() > 2) || (getMinorVersion() > 3)) && 
/* 1296 */       (this.localeEncodingMappings.size() > 0)) {
/* 1297 */       sb.append("  <locale-encoding-mapping-list>\n");
/*      */       
/* 1299 */       for (Map.Entry<String, String> entry : this.localeEncodingMappings.entrySet()) {
/* 1300 */         sb.append("    <locale-encoding-mapping>\n");
/* 1301 */         appendElement(sb, "      ", "locale", (String)entry.getKey());
/* 1302 */         appendElement(sb, "      ", "encoding", (String)entry.getValue());
/* 1303 */         sb.append("    </locale-encoding-mapping>\n");
/*      */       }
/* 1305 */       sb.append("  </locale-encoding-mapping-list>\n");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1310 */     if (((getMajorVersion() > 3) || (
/* 1311 */       (getMajorVersion() == 3) && (getMinorVersion() > 0))) && 
/* 1312 */       (this.denyUncoveredHttpMethods)) {
/* 1313 */       sb.append("\n");
/* 1314 */       sb.append("  <deny-uncovered-http-methods/>");
/*      */     }
/*      */     
/*      */ 
/* 1318 */     sb.append("</web-app>");
/* 1319 */     return sb.toString();
/*      */   }
/*      */   
/*      */   private String encodeUrl(String input)
/*      */   {
/*      */     try {
/* 1325 */       return URLEncoder.encode(input, "UTF-8");
/*      */     }
/*      */     catch (UnsupportedEncodingException e) {}
/* 1328 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static void appendElement(StringBuilder sb, String indent, String elementName, String value)
/*      */   {
/* 1335 */     if (value == null) {
/* 1336 */       return;
/*      */     }
/* 1338 */     if (value.length() == 0) {
/* 1339 */       sb.append(indent);
/* 1340 */       sb.append('<');
/* 1341 */       sb.append(elementName);
/* 1342 */       sb.append("/>\n");
/*      */     } else {
/* 1344 */       sb.append(indent);
/* 1345 */       sb.append('<');
/* 1346 */       sb.append(elementName);
/* 1347 */       sb.append('>');
/* 1348 */       sb.append(Escape.xml(value));
/* 1349 */       sb.append("</");
/* 1350 */       sb.append(elementName);
/* 1351 */       sb.append(">\n");
/*      */     }
/*      */   }
/*      */   
/*      */   private static void appendElement(StringBuilder sb, String indent, String elementName, Object value)
/*      */   {
/* 1357 */     if (value == null) return;
/* 1358 */     appendElement(sb, indent, elementName, value.toString());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final String INDENT2 = "  ";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean merge(Set<WebXml> fragments)
/*      */   {
/* 1375 */     WebXml temp = new WebXml();
/*      */     
/* 1377 */     for (WebXml fragment : fragments) {
/* 1378 */       if (!mergeMap(fragment.getContextParams(), this.contextParams, temp
/* 1379 */         .getContextParams(), fragment, "Context Parameter")) {
/* 1380 */         return false;
/*      */       }
/*      */     }
/* 1383 */     this.contextParams.putAll(temp.getContextParams());
/*      */     
/* 1385 */     if (this.displayName == null) {
/* 1386 */       for (WebXml fragment : fragments) {
/* 1387 */         String value = fragment.getDisplayName();
/* 1388 */         if (value != null) {
/* 1389 */           if (temp.getDisplayName() == null) {
/* 1390 */             temp.setDisplayName(value);
/*      */           } else {
/* 1392 */             log.error(sm.getString("webXml.mergeConflictDisplayName", new Object[] {fragment
/*      */             
/* 1394 */               .getName(), fragment
/* 1395 */               .getURL() }));
/* 1396 */             return false;
/*      */           }
/*      */         }
/*      */       }
/* 1400 */       this.displayName = temp.getDisplayName();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1405 */     if (!this.denyUncoveredHttpMethods) {
/* 1406 */       for (WebXml fragment : fragments) {
/* 1407 */         if (fragment.getDenyUncoveredHttpMethods()) {
/* 1408 */           this.denyUncoveredHttpMethods = true;
/* 1409 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1414 */     if (this.distributable) {
/* 1415 */       for (WebXml fragment : fragments) {
/* 1416 */         if (!fragment.isDistributable()) {
/* 1417 */           this.distributable = false;
/* 1418 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1423 */     for (WebXml fragment : fragments) {
/* 1424 */       if (!mergeResourceMap(fragment.getEjbLocalRefs(), this.ejbLocalRefs, temp
/* 1425 */         .getEjbLocalRefs(), fragment)) {
/* 1426 */         return false;
/*      */       }
/*      */     }
/* 1429 */     this.ejbLocalRefs.putAll(temp.getEjbLocalRefs());
/*      */     
/* 1431 */     for (WebXml fragment : fragments) {
/* 1432 */       if (!mergeResourceMap(fragment.getEjbRefs(), this.ejbRefs, temp
/* 1433 */         .getEjbRefs(), fragment)) {
/* 1434 */         return false;
/*      */       }
/*      */     }
/* 1437 */     this.ejbRefs.putAll(temp.getEjbRefs());
/*      */     
/* 1439 */     for (WebXml fragment : fragments) {
/* 1440 */       if (!mergeResourceMap(fragment.getEnvEntries(), this.envEntries, temp
/* 1441 */         .getEnvEntries(), fragment)) {
/* 1442 */         return false;
/*      */       }
/*      */     }
/* 1445 */     this.envEntries.putAll(temp.getEnvEntries());
/*      */     
/* 1447 */     for (??? = fragments.iterator(); ???.hasNext();) { fragment = (WebXml)???.next();
/* 1448 */       if (!mergeMap(fragment.getErrorPages(), this.errorPages, temp
/* 1449 */         .getErrorPages(), fragment, "Error Page")) {
/* 1450 */         return false;
/*      */       }
/*      */     }
/* 1453 */     this.errorPages.putAll(temp.getErrorPages());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1458 */     Object filterMapsToAdd = new ArrayList();
/* 1459 */     for (WebXml fragment : fragments) {
/* 1460 */       for (FilterMap filterMap : fragment.getFilterMappings()) {
/* 1461 */         if (!this.filterMappingNames.contains(filterMap.getFilterName())) {
/* 1462 */           ((List)filterMapsToAdd).add(filterMap);
/*      */         }
/*      */       }
/*      */     }
/* 1466 */     for (FilterMap filterMap : (List)filterMapsToAdd)
/*      */     {
/* 1468 */       addFilterMapping(filterMap);
/*      */     }
/*      */     
/* 1471 */     for (WebXml fragment = fragments.iterator(); fragment.hasNext();) { fragment = (WebXml)fragment.next();
/*      */       
/* 1473 */       for (Map.Entry<String, FilterDef> entry : fragment.getFilters().entrySet()) {
/* 1474 */         if (this.filters.containsKey(entry.getKey())) {
/* 1475 */           mergeFilter((FilterDef)entry.getValue(), 
/* 1476 */             (FilterDef)this.filters.get(entry.getKey()), false);
/*      */         }
/* 1478 */         else if (temp.getFilters().containsKey(entry.getKey())) {
/* 1479 */           if (!mergeFilter((FilterDef)entry.getValue(), 
/* 1480 */             (FilterDef)temp.getFilters().get(entry.getKey()), true)) {
/* 1481 */             log.error(sm.getString("webXml.mergeConflictFilter", new Object[] {entry
/*      */             
/* 1483 */               .getKey(), fragment
/* 1484 */               .getName(), fragment
/* 1485 */               .getURL() }));
/*      */             
/* 1487 */             return false;
/*      */           }
/*      */         } else {
/* 1490 */           temp.getFilters().put(entry.getKey(), entry.getValue());
/*      */         }
/*      */       }
/*      */     }
/*      */     WebXml fragment;
/* 1495 */     this.filters.putAll(temp.getFilters());
/*      */     
/* 1497 */     for (WebXml fragment : fragments)
/*      */     {
/* 1499 */       for (JspPropertyGroup jspPropertyGroup : fragment.getJspPropertyGroups())
/*      */       {
/* 1501 */         addJspPropertyGroup(jspPropertyGroup);
/*      */       }
/*      */     }
/*      */     
/* 1505 */     for (WebXml fragment : fragments) {
/* 1506 */       for (String listener : fragment.getListeners())
/*      */       {
/* 1508 */         addListener(listener);
/*      */       }
/*      */     }
/*      */     
/* 1512 */     for (fragment = fragments.iterator(); fragment.hasNext();) { fragment = (WebXml)fragment.next();
/* 1513 */       if (!mergeMap(fragment.getLocaleEncodingMappings(), this.localeEncodingMappings, temp
/* 1514 */         .getLocaleEncodingMappings(), fragment, "Locale Encoding Mapping"))
/*      */       {
/* 1516 */         return false; }
/*      */     }
/*      */     WebXml fragment;
/* 1519 */     this.localeEncodingMappings.putAll(temp.getLocaleEncodingMappings());
/*      */     Object fragment;
/* 1521 */     if (getLoginConfig() == null) {
/* 1522 */       tempLoginConfig = null;
/* 1523 */       for (fragment = fragments.iterator(); fragment.hasNext();) { fragment = (WebXml)fragment.next();
/* 1524 */         LoginConfig fragmentLoginConfig = ((WebXml)fragment).loginConfig;
/* 1525 */         if (fragmentLoginConfig != null) {
/* 1526 */           if ((tempLoginConfig == null) || 
/* 1527 */             (fragmentLoginConfig.equals(tempLoginConfig))) {
/* 1528 */             tempLoginConfig = fragmentLoginConfig;
/*      */           } else {
/* 1530 */             log.error(sm.getString("webXml.mergeConflictLoginConfig", new Object[] {((WebXml)fragment)
/*      */             
/* 1532 */               .getName(), ((WebXml)fragment)
/* 1533 */               .getURL() }));
/*      */           }
/*      */         }
/*      */       }
/* 1537 */       this.loginConfig = tempLoginConfig;
/*      */     }
/*      */     
/* 1540 */     for (WebXml fragment : fragments) {
/* 1541 */       if (!mergeResourceMap(fragment.getMessageDestinationRefs(), this.messageDestinationRefs, temp
/* 1542 */         .getMessageDestinationRefs(), fragment)) {
/* 1543 */         return false;
/*      */       }
/*      */     }
/* 1546 */     this.messageDestinationRefs.putAll(temp.getMessageDestinationRefs());
/*      */     
/* 1548 */     for (WebXml fragment : fragments) {
/* 1549 */       if (!mergeResourceMap(fragment.getMessageDestinations(), this.messageDestinations, temp
/* 1550 */         .getMessageDestinations(), fragment)) {
/* 1551 */         return false;
/*      */       }
/*      */     }
/* 1554 */     this.messageDestinations.putAll(temp.getMessageDestinations());
/*      */     
/* 1556 */     for (WebXml fragment : fragments) {
/* 1557 */       if (!mergeMap(fragment.getMimeMappings(), this.mimeMappings, temp
/* 1558 */         .getMimeMappings(), fragment, "Mime Mapping")) {
/* 1559 */         return false;
/*      */       }
/*      */     }
/* 1562 */     this.mimeMappings.putAll(temp.getMimeMappings());
/*      */     
/* 1564 */     for (WebXml fragment : fragments) {
/* 1565 */       if (!mergeResourceMap(fragment.getResourceEnvRefs(), this.resourceEnvRefs, temp
/* 1566 */         .getResourceEnvRefs(), fragment)) {
/* 1567 */         return false;
/*      */       }
/*      */     }
/* 1570 */     this.resourceEnvRefs.putAll(temp.getResourceEnvRefs());
/*      */     
/* 1572 */     for (WebXml fragment : fragments) {
/* 1573 */       if (!mergeResourceMap(fragment.getResourceRefs(), this.resourceRefs, temp
/* 1574 */         .getResourceRefs(), fragment)) {
/* 1575 */         return false;
/*      */       }
/*      */     }
/* 1578 */     this.resourceRefs.putAll(temp.getResourceRefs());
/*      */     
/* 1580 */     for (WebXml fragment : fragments) {
/* 1581 */       for (fragment = fragment.getSecurityConstraints().iterator(); ((Iterator)fragment).hasNext();) { SecurityConstraint constraint = (SecurityConstraint)((Iterator)fragment).next();
/*      */         
/* 1583 */         addSecurityConstraint(constraint);
/*      */       }
/*      */     }
/*      */     
/* 1587 */     for (WebXml fragment : fragments) {
/* 1588 */       for (fragment = fragment.getSecurityRoles().iterator(); ((Iterator)fragment).hasNext();) { role = (String)((Iterator)fragment).next();
/*      */         
/* 1590 */         addSecurityRole(role);
/*      */       }
/*      */     }
/*      */     String role;
/* 1594 */     for (LoginConfig tempLoginConfig = fragments.iterator(); tempLoginConfig.hasNext();) { fragment = (WebXml)tempLoginConfig.next();
/* 1595 */       if (!mergeResourceMap(fragment.getServiceRefs(), this.serviceRefs, temp
/* 1596 */         .getServiceRefs(), fragment)) {
/* 1597 */         return false;
/*      */       }
/*      */     }
/* 1600 */     this.serviceRefs.putAll(temp.getServiceRefs());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1607 */     List<Map.Entry<String, String>> servletMappingsToAdd = new ArrayList();
/* 1608 */     for (WebXml fragment : fragments)
/*      */     {
/* 1610 */       for (Map.Entry<String, String> servletMap : fragment.getServletMappings().entrySet()) {
/* 1611 */         if ((!this.servletMappingNames.contains(servletMap.getValue())) && 
/* 1612 */           (!this.servletMappings.containsKey(servletMap.getKey()))) {
/* 1613 */           servletMappingsToAdd.add(servletMap);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1619 */     for (Object mapping : servletMappingsToAdd) {
/* 1620 */       addServletMappingDecoded((String)((Map.Entry)mapping).getKey(), (String)((Map.Entry)mapping).getValue());
/*      */     }
/*      */     
/* 1623 */     for (WebXml fragment = fragments.iterator(); fragment.hasNext();) { fragment = (WebXml)fragment.next();
/*      */       
/* 1625 */       for (Map.Entry<String, ServletDef> entry : fragment.getServlets().entrySet()) {
/* 1626 */         if (this.servlets.containsKey(entry.getKey())) {
/* 1627 */           mergeServlet((ServletDef)entry.getValue(), 
/* 1628 */             (ServletDef)this.servlets.get(entry.getKey()), false);
/*      */         }
/* 1630 */         else if (temp.getServlets().containsKey(entry.getKey())) {
/* 1631 */           if (!mergeServlet((ServletDef)entry.getValue(), 
/* 1632 */             (ServletDef)temp.getServlets().get(entry.getKey()), true)) {
/* 1633 */             log.error(sm.getString("webXml.mergeConflictServlet", new Object[] {entry
/*      */             
/* 1635 */               .getKey(), fragment
/* 1636 */               .getName(), fragment
/* 1637 */               .getURL() }));
/*      */             
/* 1639 */             return false;
/*      */           }
/*      */         } else {
/* 1642 */           temp.getServlets().put(entry.getKey(), entry.getValue());
/*      */         }
/*      */       }
/*      */     }
/*      */     WebXml fragment;
/* 1647 */     this.servlets.putAll(temp.getServlets());
/*      */     
/* 1649 */     if (this.sessionConfig.getSessionTimeout() == null) {
/* 1650 */       for (WebXml fragment : fragments) {
/* 1651 */         Integer value = fragment.getSessionConfig().getSessionTimeout();
/* 1652 */         if (value != null) {
/* 1653 */           if (temp.getSessionConfig().getSessionTimeout() == null) {
/* 1654 */             temp.getSessionConfig().setSessionTimeout(value.toString());
/* 1655 */           } else if (!value.equals(temp
/* 1656 */             .getSessionConfig().getSessionTimeout()))
/*      */           {
/*      */ 
/* 1659 */             log.error(sm.getString("webXml.mergeConflictSessionTimeout", new Object[] {fragment
/*      */             
/* 1661 */               .getName(), fragment
/* 1662 */               .getURL() }));
/* 1663 */             return false;
/*      */           }
/*      */         }
/*      */       }
/* 1667 */       if (temp.getSessionConfig().getSessionTimeout() != null) {
/* 1668 */         this.sessionConfig.setSessionTimeout(temp
/* 1669 */           .getSessionConfig().getSessionTimeout().toString());
/*      */       }
/*      */     }
/*      */     
/* 1673 */     if (this.sessionConfig.getCookieName() == null) {
/* 1674 */       for (WebXml fragment : fragments) {
/* 1675 */         String value = fragment.getSessionConfig().getCookieName();
/* 1676 */         if (value != null) {
/* 1677 */           if (temp.getSessionConfig().getCookieName() == null) {
/* 1678 */             temp.getSessionConfig().setCookieName(value);
/* 1679 */           } else if (!value.equals(temp
/* 1680 */             .getSessionConfig().getCookieName()))
/*      */           {
/*      */ 
/* 1683 */             log.error(sm.getString("webXml.mergeConflictSessionCookieName", new Object[] {fragment
/*      */             
/* 1685 */               .getName(), fragment
/* 1686 */               .getURL() }));
/* 1687 */             return false;
/*      */           }
/*      */         }
/*      */       }
/* 1691 */       this.sessionConfig.setCookieName(temp
/* 1692 */         .getSessionConfig().getCookieName());
/*      */     }
/* 1694 */     if (this.sessionConfig.getCookieDomain() == null) {
/* 1695 */       for (WebXml fragment : fragments) {
/* 1696 */         String value = fragment.getSessionConfig().getCookieDomain();
/* 1697 */         if (value != null) {
/* 1698 */           if (temp.getSessionConfig().getCookieDomain() == null) {
/* 1699 */             temp.getSessionConfig().setCookieDomain(value);
/* 1700 */           } else if (!value.equals(temp
/* 1701 */             .getSessionConfig().getCookieDomain()))
/*      */           {
/*      */ 
/* 1704 */             log.error(sm.getString("webXml.mergeConflictSessionCookieDomain", new Object[] {fragment
/*      */             
/* 1706 */               .getName(), fragment
/* 1707 */               .getURL() }));
/* 1708 */             return false;
/*      */           }
/*      */         }
/*      */       }
/* 1712 */       this.sessionConfig.setCookieDomain(temp
/* 1713 */         .getSessionConfig().getCookieDomain());
/*      */     }
/* 1715 */     if (this.sessionConfig.getCookiePath() == null) {
/* 1716 */       for (WebXml fragment : fragments) {
/* 1717 */         String value = fragment.getSessionConfig().getCookiePath();
/* 1718 */         if (value != null) {
/* 1719 */           if (temp.getSessionConfig().getCookiePath() == null) {
/* 1720 */             temp.getSessionConfig().setCookiePath(value);
/* 1721 */           } else if (!value.equals(temp
/* 1722 */             .getSessionConfig().getCookiePath()))
/*      */           {
/*      */ 
/* 1725 */             log.error(sm.getString("webXml.mergeConflictSessionCookiePath", new Object[] {fragment
/*      */             
/* 1727 */               .getName(), fragment
/* 1728 */               .getURL() }));
/* 1729 */             return false;
/*      */           }
/*      */         }
/*      */       }
/* 1733 */       this.sessionConfig.setCookiePath(temp
/* 1734 */         .getSessionConfig().getCookiePath());
/*      */     }
/* 1736 */     if (this.sessionConfig.getCookieComment() == null) {
/* 1737 */       for (WebXml fragment : fragments) {
/* 1738 */         String value = fragment.getSessionConfig().getCookieComment();
/* 1739 */         if (value != null) {
/* 1740 */           if (temp.getSessionConfig().getCookieComment() == null) {
/* 1741 */             temp.getSessionConfig().setCookieComment(value);
/* 1742 */           } else if (!value.equals(temp
/* 1743 */             .getSessionConfig().getCookieComment()))
/*      */           {
/*      */ 
/* 1746 */             log.error(sm.getString("webXml.mergeConflictSessionCookieComment", new Object[] {fragment
/*      */             
/* 1748 */               .getName(), fragment
/* 1749 */               .getURL() }));
/* 1750 */             return false;
/*      */           }
/*      */         }
/*      */       }
/* 1754 */       this.sessionConfig.setCookieComment(temp
/* 1755 */         .getSessionConfig().getCookieComment());
/*      */     }
/* 1757 */     if (this.sessionConfig.getCookieHttpOnly() == null) {
/* 1758 */       for (WebXml fragment : fragments) {
/* 1759 */         Boolean value = fragment.getSessionConfig().getCookieHttpOnly();
/* 1760 */         if (value != null) {
/* 1761 */           if (temp.getSessionConfig().getCookieHttpOnly() == null) {
/* 1762 */             temp.getSessionConfig().setCookieHttpOnly(value.toString());
/* 1763 */           } else if (!value.equals(temp
/* 1764 */             .getSessionConfig().getCookieHttpOnly()))
/*      */           {
/*      */ 
/* 1767 */             log.error(sm.getString("webXml.mergeConflictSessionCookieHttpOnly", new Object[] {fragment
/*      */             
/* 1769 */               .getName(), fragment
/* 1770 */               .getURL() }));
/* 1771 */             return false;
/*      */           }
/*      */         }
/*      */       }
/* 1775 */       if (temp.getSessionConfig().getCookieHttpOnly() != null) {
/* 1776 */         this.sessionConfig.setCookieHttpOnly(temp
/* 1777 */           .getSessionConfig().getCookieHttpOnly().toString());
/*      */       }
/*      */     }
/* 1780 */     if (this.sessionConfig.getCookieSecure() == null) {
/* 1781 */       for (WebXml fragment : fragments) {
/* 1782 */         Boolean value = fragment.getSessionConfig().getCookieSecure();
/* 1783 */         if (value != null) {
/* 1784 */           if (temp.getSessionConfig().getCookieSecure() == null) {
/* 1785 */             temp.getSessionConfig().setCookieSecure(value.toString());
/* 1786 */           } else if (!value.equals(temp
/* 1787 */             .getSessionConfig().getCookieSecure()))
/*      */           {
/*      */ 
/* 1790 */             log.error(sm.getString("webXml.mergeConflictSessionCookieSecure", new Object[] {fragment
/*      */             
/* 1792 */               .getName(), fragment
/* 1793 */               .getURL() }));
/* 1794 */             return false;
/*      */           }
/*      */         }
/*      */       }
/* 1798 */       if (temp.getSessionConfig().getCookieSecure() != null) {
/* 1799 */         this.sessionConfig.setCookieSecure(temp
/* 1800 */           .getSessionConfig().getCookieSecure().toString());
/*      */       }
/*      */     }
/* 1803 */     if (this.sessionConfig.getCookieMaxAge() == null) {
/* 1804 */       for (WebXml fragment : fragments) {
/* 1805 */         Integer value = fragment.getSessionConfig().getCookieMaxAge();
/* 1806 */         if (value != null) {
/* 1807 */           if (temp.getSessionConfig().getCookieMaxAge() == null) {
/* 1808 */             temp.getSessionConfig().setCookieMaxAge(value.toString());
/* 1809 */           } else if (!value.equals(temp
/* 1810 */             .getSessionConfig().getCookieMaxAge()))
/*      */           {
/*      */ 
/* 1813 */             log.error(sm.getString("webXml.mergeConflictSessionCookieMaxAge", new Object[] {fragment
/*      */             
/* 1815 */               .getName(), fragment
/* 1816 */               .getURL() }));
/* 1817 */             return false;
/*      */           }
/*      */         }
/*      */       }
/* 1821 */       if (temp.getSessionConfig().getCookieMaxAge() != null) {
/* 1822 */         this.sessionConfig.setCookieMaxAge(temp
/* 1823 */           .getSessionConfig().getCookieMaxAge().toString());
/*      */       }
/*      */     }
/*      */     EnumSet<SessionTrackingMode> value;
/* 1827 */     if (this.sessionConfig.getSessionTrackingModes().size() == 0) {
/* 1828 */       for (WebXml fragment : fragments)
/*      */       {
/* 1830 */         value = fragment.getSessionConfig().getSessionTrackingModes();
/* 1831 */         if (value.size() > 0) {
/* 1832 */           if (temp.getSessionConfig().getSessionTrackingModes().size() == 0) {
/* 1833 */             temp.getSessionConfig().getSessionTrackingModes().addAll(value);
/* 1834 */           } else if (!value.equals(temp
/* 1835 */             .getSessionConfig().getSessionTrackingModes()))
/*      */           {
/*      */ 
/* 1838 */             log.error(sm.getString("webXml.mergeConflictSessionTrackingMode", new Object[] {fragment
/*      */             
/* 1840 */               .getName(), fragment
/* 1841 */               .getURL() }));
/* 1842 */             return false;
/*      */           }
/*      */         }
/*      */       }
/* 1846 */       this.sessionConfig.getSessionTrackingModes().addAll(temp
/* 1847 */         .getSessionConfig().getSessionTrackingModes());
/*      */     }
/*      */     
/* 1850 */     for (WebXml fragment : fragments) {
/* 1851 */       if (!mergeMap(fragment.getTaglibs(), this.taglibs, temp
/* 1852 */         .getTaglibs(), fragment, "Taglibs")) {
/* 1853 */         return false;
/*      */       }
/*      */     }
/* 1856 */     this.taglibs.putAll(temp.getTaglibs());
/*      */     
/* 1858 */     for (WebXml fragment : fragments) {
/* 1859 */       if ((fragment.alwaysAddWelcomeFiles) || (this.welcomeFiles.size() == 0)) {
/* 1860 */         for (String welcomeFile : fragment.getWelcomeFiles()) {
/* 1861 */           addWelcomeFile(welcomeFile);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1866 */     if (this.postConstructMethods.isEmpty()) {
/* 1867 */       for (WebXml fragment : fragments) {
/* 1868 */         if (!mergeLifecycleCallback(fragment.getPostConstructMethods(), temp
/* 1869 */           .getPostConstructMethods(), fragment, "Post Construct Methods"))
/*      */         {
/* 1871 */           return false;
/*      */         }
/*      */       }
/* 1874 */       this.postConstructMethods.putAll(temp.getPostConstructMethods());
/*      */     }
/*      */     
/* 1877 */     if (this.preDestroyMethods.isEmpty()) {
/* 1878 */       for (WebXml fragment : fragments) {
/* 1879 */         if (!mergeLifecycleCallback(fragment.getPreDestroyMethods(), temp
/* 1880 */           .getPreDestroyMethods(), fragment, "Pre Destroy Methods"))
/*      */         {
/* 1882 */           return false;
/*      */         }
/*      */       }
/* 1885 */       this.preDestroyMethods.putAll(temp.getPreDestroyMethods());
/*      */     }
/*      */     
/* 1888 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   private static <T extends ResourceBase> boolean mergeResourceMap(Map<String, T> fragmentResources, Map<String, T> mainResources, Map<String, T> tempResources, WebXml fragment)
/*      */   {
/* 1894 */     for (T resource : fragmentResources.values()) {
/* 1895 */       String resourceName = resource.getName();
/* 1896 */       if (mainResources.containsKey(resourceName)) {
/* 1897 */         ((ResourceBase)mainResources.get(resourceName)).getInjectionTargets().addAll(resource
/* 1898 */           .getInjectionTargets());
/*      */       }
/*      */       else {
/* 1901 */         T existingResource = (ResourceBase)tempResources.get(resourceName);
/* 1902 */         if (existingResource != null) {
/* 1903 */           if (!existingResource.equals(resource)) {
/* 1904 */             log.error(sm.getString("webXml.mergeConflictResource", new Object[] { resourceName, fragment
/*      */             
/*      */ 
/* 1907 */               .getName(), fragment
/* 1908 */               .getURL() }));
/* 1909 */             return false;
/*      */           }
/*      */         } else {
/* 1912 */           tempResources.put(resourceName, resource);
/*      */         }
/*      */       }
/*      */     }
/* 1916 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   private static <T> boolean mergeMap(Map<String, T> fragmentMap, Map<String, T> mainMap, Map<String, T> tempMap, WebXml fragment, String mapName)
/*      */   {
/* 1922 */     for (Map.Entry<String, T> entry : fragmentMap.entrySet()) {
/* 1923 */       String key = (String)entry.getKey();
/* 1924 */       if (!mainMap.containsKey(key))
/*      */       {
/* 1926 */         T value = entry.getValue();
/* 1927 */         if (tempMap.containsKey(key)) {
/* 1928 */           if ((value != null) && (!value.equals(tempMap
/* 1929 */             .get(key)))) {
/* 1930 */             log.error(sm.getString("webXml.mergeConflictString", new Object[] { mapName, key, fragment
/*      */             
/*      */ 
/*      */ 
/* 1934 */               .getName(), fragment
/* 1935 */               .getURL() }));
/* 1936 */             return false;
/*      */           }
/*      */         } else {
/* 1939 */           tempMap.put(key, value);
/*      */         }
/*      */       }
/*      */     }
/* 1943 */     return true;
/*      */   }
/*      */   
/*      */   private static boolean mergeFilter(FilterDef src, FilterDef dest, boolean failOnConflict)
/*      */   {
/* 1948 */     if (dest.getAsyncSupported() == null) {
/* 1949 */       dest.setAsyncSupported(src.getAsyncSupported());
/* 1950 */     } else if ((src.getAsyncSupported() != null) && 
/* 1951 */       (failOnConflict) && 
/* 1952 */       (!src.getAsyncSupported().equals(dest.getAsyncSupported()))) {
/* 1953 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1957 */     if (dest.getFilterClass() == null) {
/* 1958 */       dest.setFilterClass(src.getFilterClass());
/* 1959 */     } else if ((src.getFilterClass() != null) && 
/* 1960 */       (failOnConflict) && 
/* 1961 */       (!src.getFilterClass().equals(dest.getFilterClass()))) {
/* 1962 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1967 */     for (Map.Entry<String, String> srcEntry : src.getParameterMap().entrySet()) {
/* 1968 */       if (dest.getParameterMap().containsKey(srcEntry.getKey())) {
/* 1969 */         if ((failOnConflict) && 
/* 1970 */           (!((String)dest.getParameterMap().get(srcEntry.getKey())).equals(srcEntry.getValue()))) {
/* 1971 */           return false;
/*      */         }
/*      */       } else {
/* 1974 */         dest.addInitParameter((String)srcEntry.getKey(), (String)srcEntry.getValue());
/*      */       }
/*      */     }
/* 1977 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   private static boolean mergeServlet(ServletDef src, ServletDef dest, boolean failOnConflict)
/*      */   {
/* 1983 */     if ((dest.getServletClass() != null) && (dest.getJspFile() != null)) {
/* 1984 */       return false;
/*      */     }
/* 1986 */     if ((src.getServletClass() != null) && (src.getJspFile() != null)) {
/* 1987 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1991 */     if ((dest.getServletClass() == null) && (dest.getJspFile() == null)) {
/* 1992 */       dest.setServletClass(src.getServletClass());
/* 1993 */       dest.setJspFile(src.getJspFile());
/* 1994 */     } else if (failOnConflict) {
/* 1995 */       if ((src.getServletClass() != null) && (
/* 1996 */         (dest.getJspFile() != null) || 
/* 1997 */         (!src.getServletClass().equals(dest.getServletClass())))) {
/* 1998 */         return false;
/*      */       }
/* 2000 */       if ((src.getJspFile() != null) && (
/* 2001 */         (dest.getServletClass() != null) || 
/* 2002 */         (!src.getJspFile().equals(dest.getJspFile())))) {
/* 2003 */         return false;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2008 */     for (SecurityRoleRef securityRoleRef : src.getSecurityRoleRefs()) {
/* 2009 */       dest.addSecurityRoleRef(securityRoleRef);
/*      */     }
/*      */     
/* 2012 */     if (dest.getLoadOnStartup() == null) {
/* 2013 */       if (src.getLoadOnStartup() != null) {
/* 2014 */         dest.setLoadOnStartup(src.getLoadOnStartup().toString());
/*      */       }
/* 2016 */     } else if ((src.getLoadOnStartup() != null) && 
/* 2017 */       (failOnConflict) && 
/* 2018 */       (!src.getLoadOnStartup().equals(dest.getLoadOnStartup()))) {
/* 2019 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 2023 */     if (dest.getEnabled() == null) {
/* 2024 */       if (src.getEnabled() != null) {
/* 2025 */         dest.setEnabled(src.getEnabled().toString());
/*      */       }
/* 2027 */     } else if ((src.getEnabled() != null) && 
/* 2028 */       (failOnConflict) && 
/* 2029 */       (!src.getEnabled().equals(dest.getEnabled()))) {
/* 2030 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2035 */     for (Map.Entry<String, String> srcEntry : src.getParameterMap().entrySet()) {
/* 2036 */       if (dest.getParameterMap().containsKey(srcEntry.getKey())) {
/* 2037 */         if ((failOnConflict) && 
/* 2038 */           (!((String)dest.getParameterMap().get(srcEntry.getKey())).equals(srcEntry.getValue()))) {
/* 2039 */           return false;
/*      */         }
/*      */       } else {
/* 2042 */         dest.addInitParameter((String)srcEntry.getKey(), (String)srcEntry.getValue());
/*      */       }
/*      */     }
/*      */     
/* 2046 */     if (dest.getMultipartDef() == null) {
/* 2047 */       dest.setMultipartDef(src.getMultipartDef());
/* 2048 */     } else if (src.getMultipartDef() != null) {
/* 2049 */       return mergeMultipartDef(src.getMultipartDef(), dest
/* 2050 */         .getMultipartDef(), failOnConflict);
/*      */     }
/*      */     
/* 2053 */     if (dest.getAsyncSupported() == null) {
/* 2054 */       if (src.getAsyncSupported() != null) {
/* 2055 */         dest.setAsyncSupported(src.getAsyncSupported().toString());
/*      */       }
/* 2057 */     } else if ((src.getAsyncSupported() != null) && 
/* 2058 */       (failOnConflict) && 
/* 2059 */       (!src.getAsyncSupported().equals(dest.getAsyncSupported()))) {
/* 2060 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 2064 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   private static boolean mergeMultipartDef(MultipartDef src, MultipartDef dest, boolean failOnConflict)
/*      */   {
/* 2070 */     if (dest.getLocation() == null) {
/* 2071 */       dest.setLocation(src.getLocation());
/* 2072 */     } else if ((src.getLocation() != null) && 
/* 2073 */       (failOnConflict) && 
/* 2074 */       (!src.getLocation().equals(dest.getLocation()))) {
/* 2075 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 2079 */     if (dest.getFileSizeThreshold() == null) {
/* 2080 */       dest.setFileSizeThreshold(src.getFileSizeThreshold());
/* 2081 */     } else if ((src.getFileSizeThreshold() != null) && 
/* 2082 */       (failOnConflict) && 
/* 2083 */       (!src.getFileSizeThreshold().equals(dest
/* 2084 */       .getFileSizeThreshold()))) {
/* 2085 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 2089 */     if (dest.getMaxFileSize() == null) {
/* 2090 */       dest.setMaxFileSize(src.getMaxFileSize());
/* 2091 */     } else if ((src.getMaxFileSize() != null) && 
/* 2092 */       (failOnConflict) && 
/* 2093 */       (!src.getMaxFileSize().equals(dest.getMaxFileSize()))) {
/* 2094 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 2098 */     if (dest.getMaxRequestSize() == null) {
/* 2099 */       dest.setMaxRequestSize(src.getMaxRequestSize());
/* 2100 */     } else if ((src.getMaxRequestSize() != null) && 
/* 2101 */       (failOnConflict) && 
/* 2102 */       (!src.getMaxRequestSize().equals(dest
/* 2103 */       .getMaxRequestSize()))) {
/* 2104 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 2108 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static boolean mergeLifecycleCallback(Map<String, String> fragmentMap, Map<String, String> tempMap, WebXml fragment, String mapName)
/*      */   {
/* 2115 */     for (Map.Entry<String, String> entry : fragmentMap.entrySet()) {
/* 2116 */       String key = (String)entry.getKey();
/* 2117 */       String value = (String)entry.getValue();
/* 2118 */       if (tempMap.containsKey(key)) {
/* 2119 */         if ((value != null) && (!value.equals(tempMap.get(key)))) {
/* 2120 */           log.error(sm.getString("webXml.mergeConflictString", new Object[] { mapName, key, fragment
/* 2121 */             .getName(), fragment.getURL() }));
/* 2122 */           return false;
/*      */         }
/*      */       } else {
/* 2125 */         tempMap.put(key, value);
/*      */       }
/*      */     }
/* 2128 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final String INDENT4 = "    ";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final String INDENT6 = "      ";
/*      */   
/*      */ 
/*      */ 
/*      */   public static Set<WebXml> orderWebFragments(WebXml application, Map<String, WebXml> fragments, ServletContext servletContext)
/*      */   {
/* 2146 */     Set<WebXml> orderedFragments = new LinkedHashSet();
/*      */     
/*      */ 
/* 2149 */     boolean absoluteOrdering = application.getAbsoluteOrdering() != null;
/* 2150 */     boolean orderingPresent = false;
/*      */     Set<String> requestedOrder;
/* 2152 */     if (absoluteOrdering) {
/* 2153 */       orderingPresent = true;
/*      */       
/* 2155 */       requestedOrder = application.getAbsoluteOrdering();
/*      */       
/* 2157 */       for (String requestedName : requestedOrder) {
/* 2158 */         if ("org.apache.catalina.order.others".equals(requestedName))
/*      */         {
/* 2160 */           for (Map.Entry<String, WebXml> entry : fragments.entrySet()) {
/* 2161 */             if (!requestedOrder.contains(entry.getKey())) {
/* 2162 */               WebXml fragment = (WebXml)entry.getValue();
/* 2163 */               if (fragment != null) {
/* 2164 */                 orderedFragments.add(fragment);
/*      */               }
/*      */             }
/*      */           }
/*      */         } else {
/* 2169 */           WebXml fragment = (WebXml)fragments.get(requestedName);
/* 2170 */           if (fragment != null) {
/* 2171 */             orderedFragments.add(fragment);
/*      */           } else {
/* 2173 */             log.warn(sm.getString("webXml.wrongFragmentName", new Object[] { requestedName }));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 2180 */       for (WebXml fragment : fragments.values())
/*      */       {
/* 2182 */         Iterator<String> before = fragment.getBeforeOrdering().iterator();
/* 2183 */         while (before.hasNext()) {
/* 2184 */           orderingPresent = true;
/* 2185 */           String beforeEntry = (String)before.next();
/* 2186 */           if (!beforeEntry.equals("org.apache.catalina.order.others")) {
/* 2187 */             WebXml beforeFragment = (WebXml)fragments.get(beforeEntry);
/* 2188 */             if (beforeFragment == null) {
/* 2189 */               before.remove();
/*      */             } else {
/* 2191 */               beforeFragment.addAfterOrdering(fragment.getName());
/*      */             }
/*      */           }
/*      */         }
/* 2195 */         after = fragment.getAfterOrdering().iterator();
/* 2196 */         while (((Iterator)after).hasNext()) {
/* 2197 */           orderingPresent = true;
/* 2198 */           String afterEntry = (String)((Iterator)after).next();
/* 2199 */           if (!afterEntry.equals("org.apache.catalina.order.others")) {
/* 2200 */             WebXml afterFragment = (WebXml)fragments.get(afterEntry);
/* 2201 */             if (afterFragment == null) {
/* 2202 */               ((Iterator)after).remove();
/*      */             } else {
/* 2204 */               afterFragment.addBeforeOrdering(fragment.getName());
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2213 */       for (WebXml fragment : fragments.values()) {
/* 2214 */         if (fragment.getBeforeOrdering().contains("org.apache.catalina.order.others")) {
/* 2215 */           makeBeforeOthersExplicit(fragment.getAfterOrdering(), fragments);
/*      */         }
/* 2217 */         if (fragment.getAfterOrdering().contains("org.apache.catalina.order.others")) {
/* 2218 */           makeAfterOthersExplicit(fragment.getBeforeOrdering(), fragments);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 2223 */       Set<WebXml> beforeSet = new HashSet();
/* 2224 */       othersSet = new HashSet();
/* 2225 */       Set<WebXml> afterSet = new HashSet();
/*      */       
/* 2227 */       for (Object after = fragments.values().iterator(); ((Iterator)after).hasNext();) { WebXml fragment = (WebXml)((Iterator)after).next();
/* 2228 */         if (fragment.getBeforeOrdering().contains("org.apache.catalina.order.others")) {
/* 2229 */           beforeSet.add(fragment);
/* 2230 */           fragment.getBeforeOrdering().remove("org.apache.catalina.order.others");
/* 2231 */         } else if (fragment.getAfterOrdering().contains("org.apache.catalina.order.others")) {
/* 2232 */           afterSet.add(fragment);
/* 2233 */           fragment.getAfterOrdering().remove("org.apache.catalina.order.others");
/*      */         } else {
/* 2235 */           ((Set)othersSet).add(fragment);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2245 */       decoupleOtherGroups(beforeSet);
/* 2246 */       decoupleOtherGroups((Set)othersSet);
/* 2247 */       decoupleOtherGroups(afterSet);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2252 */       orderFragments(orderedFragments, beforeSet);
/* 2253 */       orderFragments(orderedFragments, (Set)othersSet);
/* 2254 */       orderFragments(orderedFragments, afterSet);
/*      */     }
/*      */     
/*      */ 
/* 2258 */     Set<WebXml> containerFragments = new LinkedHashSet();
/*      */     
/*      */ 
/* 2261 */     for (Object othersSet = fragments.values().iterator(); ((Iterator)othersSet).hasNext();) { fragment = (WebXml)((Iterator)othersSet).next();
/* 2262 */       if (!fragment.getWebappJar()) {
/* 2263 */         containerFragments.add(fragment);
/* 2264 */         orderedFragments.remove(fragment);
/*      */       }
/*      */     }
/*      */     
/*      */     WebXml fragment;
/* 2269 */     if (servletContext != null)
/*      */     {
/*      */ 
/* 2272 */       Object orderedJarFileNames = null;
/* 2273 */       if (orderingPresent) {
/* 2274 */         orderedJarFileNames = new ArrayList();
/* 2275 */         for (WebXml fragment : orderedFragments) {
/* 2276 */           ((List)orderedJarFileNames).add(fragment.getJarName());
/*      */         }
/*      */       }
/* 2279 */       servletContext.setAttribute("javax.servlet.context.orderedLibs", orderedJarFileNames);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2285 */     if (containerFragments.size() > 0) {
/* 2286 */       Object result = new LinkedHashSet();
/* 2287 */       if (((WebXml)containerFragments.iterator().next()).getDelegate()) {
/* 2288 */         ((Set)result).addAll(containerFragments);
/* 2289 */         ((Set)result).addAll(orderedFragments);
/*      */       } else {
/* 2291 */         ((Set)result).addAll(orderedFragments);
/* 2292 */         ((Set)result).addAll(containerFragments);
/*      */       }
/* 2294 */       return (Set<WebXml>)result;
/*      */     }
/* 2296 */     return orderedFragments;
/*      */   }
/*      */   
/*      */   private static void decoupleOtherGroups(Set<WebXml> group)
/*      */   {
/* 2301 */     Set<String> names = new HashSet();
/* 2302 */     for (WebXml fragment : group) {
/* 2303 */       names.add(fragment.getName());
/*      */     }
/* 2305 */     for (WebXml fragment : group) {
/* 2306 */       Iterator<String> after = fragment.getAfterOrdering().iterator();
/* 2307 */       while (after.hasNext()) {
/* 2308 */         String entry = (String)after.next();
/* 2309 */         if (!names.contains(entry)) {
/* 2310 */           after.remove();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static void orderFragments(Set<WebXml> orderedFragments, Set<WebXml> unordered) {
/* 2317 */     Set<WebXml> addedThisRound = new HashSet();
/* 2318 */     Set<WebXml> addedLastRound = new HashSet();
/* 2319 */     while (unordered.size() > 0) {
/* 2320 */       Iterator<WebXml> source = unordered.iterator();
/* 2321 */       while (source.hasNext()) {
/* 2322 */         WebXml fragment = (WebXml)source.next();
/* 2323 */         for (WebXml toRemove : addedLastRound) {
/* 2324 */           fragment.getAfterOrdering().remove(toRemove.getName());
/*      */         }
/* 2326 */         if (fragment.getAfterOrdering().isEmpty()) {
/* 2327 */           addedThisRound.add(fragment);
/* 2328 */           orderedFragments.add(fragment);
/* 2329 */           source.remove();
/*      */         }
/*      */       }
/* 2332 */       if (addedThisRound.size() == 0)
/*      */       {
/*      */ 
/* 2335 */         throw new IllegalArgumentException(sm.getString("webXml.mergeConflictOrder"));
/*      */       }
/* 2337 */       addedLastRound.clear();
/* 2338 */       addedLastRound.addAll(addedThisRound);
/* 2339 */       addedThisRound.clear();
/*      */     }
/*      */   }
/*      */   
/*      */   private static void makeBeforeOthersExplicit(Set<String> beforeOrdering, Map<String, WebXml> fragments)
/*      */   {
/* 2345 */     for (String before : beforeOrdering) {
/* 2346 */       if (!before.equals("org.apache.catalina.order.others")) {
/* 2347 */         WebXml webXml = (WebXml)fragments.get(before);
/* 2348 */         if (!webXml.getBeforeOrdering().contains("org.apache.catalina.order.others")) {
/* 2349 */           webXml.addBeforeOrderingOthers();
/* 2350 */           makeBeforeOthersExplicit(webXml.getAfterOrdering(), fragments);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static void makeAfterOthersExplicit(Set<String> afterOrdering, Map<String, WebXml> fragments)
/*      */   {
/* 2358 */     for (String after : afterOrdering) {
/* 2359 */       if (!after.equals("org.apache.catalina.order.others")) {
/* 2360 */         WebXml webXml = (WebXml)fragments.get(after);
/* 2361 */         if (!webXml.getAfterOrdering().contains("org.apache.catalina.order.others")) {
/* 2362 */           webXml.addAfterOrderingOthers();
/* 2363 */           makeAfterOthersExplicit(webXml.getBeforeOrdering(), fragments);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\WebXml.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */