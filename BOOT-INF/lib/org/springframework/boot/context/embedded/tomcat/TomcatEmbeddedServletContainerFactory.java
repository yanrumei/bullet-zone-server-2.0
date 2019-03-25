/*     */ package org.springframework.boot.context.embedded.tomcat;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.servlet.ServletContainerInitializer;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.Engine;
/*     */ import org.apache.catalina.Host;
/*     */ import org.apache.catalina.LifecycleEvent;
/*     */ import org.apache.catalina.LifecycleListener;
/*     */ import org.apache.catalina.Manager;
/*     */ import org.apache.catalina.Pipeline;
/*     */ import org.apache.catalina.Service;
/*     */ import org.apache.catalina.Valve;
/*     */ import org.apache.catalina.Wrapper;
/*     */ import org.apache.catalina.connector.Connector;
/*     */ import org.apache.catalina.loader.WebappLoader;
/*     */ import org.apache.catalina.session.StandardManager;
/*     */ import org.apache.catalina.startup.Tomcat;
/*     */ import org.apache.catalina.startup.Tomcat.FixContextListener;
/*     */ import org.apache.catalina.webresources.TomcatURLStreamHandlerFactory;
/*     */ import org.apache.coyote.AbstractProtocol;
/*     */ import org.apache.coyote.ProtocolHandler;
/*     */ import org.apache.coyote.http11.AbstractHttp11JsseProtocol;
/*     */ import org.apache.coyote.http11.AbstractHttp11Protocol;
/*     */ import org.apache.coyote.http11.Http11NioProtocol;
/*     */ import org.apache.tomcat.util.net.SSLHostConfig;
/*     */ import org.springframework.boot.context.embedded.AbstractEmbeddedServletContainerFactory;
/*     */ import org.springframework.boot.context.embedded.Compression;
/*     */ import org.springframework.boot.context.embedded.EmbeddedServletContainer;
/*     */ import org.springframework.boot.context.embedded.EmbeddedServletContainerException;
/*     */ import org.springframework.boot.context.embedded.JspServlet;
/*     */ import org.springframework.boot.context.embedded.MimeMappings.Mapping;
/*     */ import org.springframework.boot.context.embedded.Ssl;
/*     */ import org.springframework.boot.context.embedded.Ssl.ClientAuth;
/*     */ import org.springframework.boot.context.embedded.SslStoreProvider;
/*     */ import org.springframework.boot.web.servlet.ErrorPage;
/*     */ import org.springframework.boot.web.servlet.ServletContextInitializer;
/*     */ import org.springframework.context.ResourceLoaderAware;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ResourceUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TomcatEmbeddedServletContainerFactory
/*     */   extends AbstractEmbeddedServletContainerFactory
/*     */   implements ResourceLoaderAware
/*     */ {
/* 102 */   private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
/*     */   
/* 104 */   private static final Set<Class<?>> NO_CLASSES = Collections.emptySet();
/*     */   
/*     */ 
/*     */   public static final String DEFAULT_PROTOCOL = "org.apache.coyote.http11.Http11NioProtocol";
/*     */   
/*     */ 
/*     */   private File baseDirectory;
/*     */   
/*     */ 
/* 113 */   private List<Valve> engineValves = new ArrayList();
/*     */   
/* 115 */   private List<Valve> contextValves = new ArrayList();
/*     */   
/* 117 */   private List<LifecycleListener> contextLifecycleListeners = new ArrayList();
/*     */   
/* 119 */   private List<TomcatContextCustomizer> tomcatContextCustomizers = new ArrayList();
/*     */   
/* 121 */   private List<TomcatConnectorCustomizer> tomcatConnectorCustomizers = new ArrayList();
/*     */   
/* 123 */   private List<Connector> additionalTomcatConnectors = new ArrayList();
/*     */   
/*     */   private ResourceLoader resourceLoader;
/*     */   
/* 127 */   private String protocol = "org.apache.coyote.http11.Http11NioProtocol";
/*     */   
/* 129 */   private Set<String> tldSkipPatterns = new LinkedHashSet(TldSkipPatterns.DEFAULT);
/*     */   
/*     */ 
/* 132 */   private Charset uriEncoding = DEFAULT_CHARSET;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int backgroundProcessorDelay;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TomcatEmbeddedServletContainerFactory() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TomcatEmbeddedServletContainerFactory(int port)
/*     */   {
/* 149 */     super(port);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TomcatEmbeddedServletContainerFactory(String contextPath, int port)
/*     */   {
/* 159 */     super(contextPath, port);
/*     */   }
/*     */   
/*     */ 
/*     */   public EmbeddedServletContainer getEmbeddedServletContainer(ServletContextInitializer... initializers)
/*     */   {
/* 165 */     Tomcat tomcat = new Tomcat();
/*     */     
/* 167 */     File baseDir = this.baseDirectory != null ? this.baseDirectory : createTempDir("tomcat");
/* 168 */     tomcat.setBaseDir(baseDir.getAbsolutePath());
/* 169 */     Connector connector = new Connector(this.protocol);
/* 170 */     tomcat.getService().addConnector(connector);
/* 171 */     customizeConnector(connector);
/* 172 */     tomcat.setConnector(connector);
/* 173 */     tomcat.getHost().setAutoDeploy(false);
/* 174 */     configureEngine(tomcat.getEngine());
/* 175 */     for (Connector additionalConnector : this.additionalTomcatConnectors) {
/* 176 */       tomcat.getService().addConnector(additionalConnector);
/*     */     }
/* 178 */     prepareContext(tomcat.getHost(), initializers);
/* 179 */     return getTomcatEmbeddedServletContainer(tomcat);
/*     */   }
/*     */   
/*     */   private void configureEngine(Engine engine) {
/* 183 */     engine.setBackgroundProcessorDelay(this.backgroundProcessorDelay);
/* 184 */     for (Valve valve : this.engineValves) {
/* 185 */       engine.getPipeline().addValve(valve);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void prepareContext(Host host, ServletContextInitializer[] initializers) {
/* 190 */     File docBase = getValidDocumentRoot();
/* 191 */     docBase = docBase != null ? docBase : createTempDir("tomcat-docbase");
/* 192 */     final TomcatEmbeddedContext context = new TomcatEmbeddedContext();
/* 193 */     context.setName(getContextPath());
/* 194 */     context.setDisplayName(getDisplayName());
/* 195 */     context.setPath(getContextPath());
/* 196 */     context.setDocBase(docBase.getAbsolutePath());
/* 197 */     context.addLifecycleListener(new Tomcat.FixContextListener());
/* 198 */     context.setParentClassLoader(this.resourceLoader != null ? this.resourceLoader
/* 199 */       .getClassLoader() : 
/* 200 */       ClassUtils.getDefaultClassLoader());
/* 201 */     resetDefaultLocaleMapping(context);
/* 202 */     addLocaleMappings(context);
/*     */     try {
/* 204 */       context.setUseRelativeRedirects(false);
/*     */     }
/*     */     catch (NoSuchMethodError localNoSuchMethodError) {}
/*     */     
/*     */ 
/* 209 */     SkipPatternJarScanner.apply(context, this.tldSkipPatterns);
/* 210 */     WebappLoader loader = new WebappLoader(context.getParentClassLoader());
/* 211 */     loader.setLoaderClass(TomcatEmbeddedWebappClassLoader.class.getName());
/* 212 */     loader.setDelegate(true);
/* 213 */     context.setLoader(loader);
/* 214 */     if (isRegisterDefaultServlet()) {
/* 215 */       addDefaultServlet(context);
/*     */     }
/* 217 */     if (shouldRegisterJspServlet()) {
/* 218 */       addJspServlet(context);
/* 219 */       addJasperInitializer(context);
/* 220 */       context.addLifecycleListener(new StoreMergedWebXmlListener(null));
/*     */     }
/* 222 */     context.addLifecycleListener(new LifecycleListener()
/*     */     {
/*     */       public void lifecycleEvent(LifecycleEvent event)
/*     */       {
/* 226 */         if (event.getType().equals("configure_start"))
/*     */         {
/* 228 */           TomcatResources.get(context).addResourceJars(TomcatEmbeddedServletContainerFactory.this.getUrlsOfJarsWithMetaInfResources());
/*     */         }
/*     */         
/*     */       }
/* 232 */     });
/* 233 */     ServletContextInitializer[] initializersToUse = mergeInitializers(initializers);
/* 234 */     host.addChild(context);
/* 235 */     configureContext(context, initializersToUse);
/* 236 */     postProcessContext(context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void resetDefaultLocaleMapping(TomcatEmbeddedContext context)
/*     */   {
/* 245 */     context.addLocaleEncodingMappingParameter(Locale.ENGLISH.toString(), DEFAULT_CHARSET
/* 246 */       .displayName());
/* 247 */     context.addLocaleEncodingMappingParameter(Locale.FRENCH.toString(), DEFAULT_CHARSET
/* 248 */       .displayName());
/*     */   }
/*     */   
/*     */   private void addLocaleMappings(TomcatEmbeddedContext context) {
/* 252 */     for (Map.Entry<Locale, Charset> entry : getLocaleCharsetMappings().entrySet()) {
/* 253 */       Locale locale = (Locale)entry.getKey();
/* 254 */       Charset charset = (Charset)entry.getValue();
/* 255 */       context.addLocaleEncodingMappingParameter(locale.toString(), charset
/* 256 */         .toString());
/*     */     }
/*     */   }
/*     */   
/*     */   private void addDefaultServlet(Context context) {
/* 261 */     Wrapper defaultServlet = context.createWrapper();
/* 262 */     defaultServlet.setName("default");
/* 263 */     defaultServlet.setServletClass("org.apache.catalina.servlets.DefaultServlet");
/* 264 */     defaultServlet.addInitParameter("debug", "0");
/* 265 */     defaultServlet.addInitParameter("listings", "false");
/* 266 */     defaultServlet.setLoadOnStartup(1);
/*     */     
/* 268 */     defaultServlet.setOverridable(true);
/* 269 */     context.addChild(defaultServlet);
/* 270 */     addServletMapping(context, "/", "default");
/*     */   }
/*     */   
/*     */   private void addJspServlet(Context context) {
/* 274 */     Wrapper jspServlet = context.createWrapper();
/* 275 */     jspServlet.setName("jsp");
/* 276 */     jspServlet.setServletClass(getJspServlet().getClassName());
/* 277 */     jspServlet.addInitParameter("fork", "false");
/* 278 */     for (Map.Entry<String, String> initParameter : getJspServlet().getInitParameters()
/* 279 */       .entrySet()) {
/* 280 */       jspServlet.addInitParameter((String)initParameter.getKey(), (String)initParameter.getValue());
/*     */     }
/* 282 */     jspServlet.setLoadOnStartup(3);
/* 283 */     context.addChild(jspServlet);
/* 284 */     addServletMapping(context, "*.jsp", "jsp");
/* 285 */     addServletMapping(context, "*.jspx", "jsp");
/*     */   }
/*     */   
/*     */   private void addServletMapping(Context context, String pattern, String name)
/*     */   {
/* 290 */     context.addServletMapping(pattern, name);
/*     */   }
/*     */   
/*     */   private void addJasperInitializer(TomcatEmbeddedContext context)
/*     */   {
/*     */     try
/*     */     {
/* 297 */       ServletContainerInitializer initializer = (ServletContainerInitializer)ClassUtils.forName("org.apache.jasper.servlet.JasperInitializer", null).newInstance();
/* 298 */       context.addServletContainerInitializer(initializer, null);
/*     */     }
/*     */     catch (Exception localException) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void customizeConnector(Connector connector)
/*     */   {
/* 307 */     int port = getPort() >= 0 ? getPort() : 0;
/* 308 */     connector.setPort(port);
/* 309 */     if (StringUtils.hasText(getServerHeader())) {
/* 310 */       connector.setAttribute("server", getServerHeader());
/*     */     }
/* 312 */     if ((connector.getProtocolHandler() instanceof AbstractProtocol)) {
/* 313 */       customizeProtocol((AbstractProtocol)connector.getProtocolHandler());
/*     */     }
/* 315 */     if (getUriEncoding() != null) {
/* 316 */       connector.setURIEncoding(getUriEncoding().name());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 321 */     connector.setProperty("bindOnInit", "false");
/*     */     
/* 323 */     if ((getSsl() != null) && (getSsl().isEnabled())) {
/* 324 */       customizeSsl(connector);
/*     */     }
/* 326 */     if ((getCompression() != null) && (getCompression().getEnabled())) {
/* 327 */       customizeCompression(connector);
/*     */     }
/* 329 */     for (TomcatConnectorCustomizer customizer : this.tomcatConnectorCustomizers) {
/* 330 */       customizer.customize(connector);
/*     */     }
/*     */   }
/*     */   
/*     */   private void customizeProtocol(AbstractProtocol<?> protocol) {
/* 335 */     if (getAddress() != null) {
/* 336 */       protocol.setAddress(getAddress());
/*     */     }
/*     */   }
/*     */   
/*     */   private void customizeSsl(Connector connector) {
/* 341 */     ProtocolHandler handler = connector.getProtocolHandler();
/* 342 */     Assert.state(handler instanceof AbstractHttp11JsseProtocol, "To use SSL, the connector's protocol handler must be an AbstractHttp11JsseProtocol subclass");
/*     */     
/*     */ 
/* 345 */     configureSsl((AbstractHttp11JsseProtocol)handler, getSsl());
/* 346 */     connector.setScheme("https");
/* 347 */     connector.setSecure(true);
/*     */   }
/*     */   
/*     */   private void customizeCompression(Connector connector) {
/* 351 */     ProtocolHandler handler = connector.getProtocolHandler();
/* 352 */     if ((handler instanceof AbstractHttp11Protocol)) {
/* 353 */       AbstractHttp11Protocol<?> protocol = (AbstractHttp11Protocol)handler;
/* 354 */       Compression compression = getCompression();
/* 355 */       protocol.setCompression("on");
/* 356 */       protocol.setCompressionMinSize(compression.getMinResponseSize());
/* 357 */       configureCompressibleMimeTypes(protocol, compression);
/* 358 */       if (getCompression().getExcludedUserAgents() != null) {
/* 359 */         protocol.setNoCompressionUserAgents(
/* 360 */           StringUtils.arrayToCommaDelimitedString(
/* 361 */           getCompression().getExcludedUserAgents()));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void configureCompressibleMimeTypes(AbstractHttp11Protocol<?> protocol, Compression compression)
/*     */   {
/* 369 */     protocol.setCompressableMimeType(
/* 370 */       StringUtils.arrayToCommaDelimitedString(compression.getMimeTypes()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void configureSsl(AbstractHttp11JsseProtocol<?> protocol, Ssl ssl)
/*     */   {
/* 379 */     protocol.setSSLEnabled(true);
/* 380 */     protocol.setSslProtocol(ssl.getProtocol());
/* 381 */     configureSslClientAuth(protocol, ssl);
/* 382 */     protocol.setKeystorePass(ssl.getKeyStorePassword());
/* 383 */     protocol.setKeyPass(ssl.getKeyPassword());
/* 384 */     protocol.setKeyAlias(ssl.getKeyAlias());
/* 385 */     String ciphers = StringUtils.arrayToCommaDelimitedString(ssl.getCiphers());
/* 386 */     protocol.setCiphers(StringUtils.hasText(ciphers) ? ciphers : null);
/* 387 */     if (ssl.getEnabledProtocols() != null) {
/*     */       try {
/* 389 */         for (SSLHostConfig sslHostConfig : protocol.findSslHostConfigs()) {
/* 390 */           sslHostConfig.setProtocols(
/* 391 */             StringUtils.arrayToCommaDelimitedString(ssl.getEnabledProtocols()));
/*     */         }
/*     */       }
/*     */       catch (NoSuchMethodError ex)
/*     */       {
/* 396 */         Assert.isTrue(
/* 397 */           protocol.setProperty("sslEnabledProtocols", 
/* 398 */           StringUtils.arrayToCommaDelimitedString(ssl
/* 399 */           .getEnabledProtocols())), "Failed to set sslEnabledProtocols");
/*     */       }
/*     */     }
/*     */     
/* 403 */     if (getSslStoreProvider() != null)
/*     */     {
/* 405 */       TomcatURLStreamHandlerFactory instance = TomcatURLStreamHandlerFactory.getInstance();
/* 406 */       instance.addUserFactory(new SslStoreProviderUrlStreamHandlerFactory(
/* 407 */         getSslStoreProvider()));
/* 408 */       protocol.setKeystoreFile("springbootssl:keyStore");
/*     */       
/* 410 */       protocol.setTruststoreFile("springbootssl:trustStore");
/*     */     }
/*     */     else
/*     */     {
/* 414 */       configureSslKeyStore(protocol, ssl);
/* 415 */       configureSslTrustStore(protocol, ssl);
/*     */     }
/*     */   }
/*     */   
/*     */   private void configureSslClientAuth(AbstractHttp11JsseProtocol<?> protocol, Ssl ssl) {
/* 420 */     if (ssl.getClientAuth() == Ssl.ClientAuth.NEED) {
/* 421 */       protocol.setClientAuth(Boolean.TRUE.toString());
/*     */     }
/* 423 */     else if (ssl.getClientAuth() == Ssl.ClientAuth.WANT) {
/* 424 */       protocol.setClientAuth("want");
/*     */     }
/*     */   }
/*     */   
/*     */   protected void configureSslStoreProvider(AbstractHttp11JsseProtocol<?> protocol, SslStoreProvider sslStoreProvider)
/*     */   {
/* 430 */     Assert.isInstanceOf(Http11NioProtocol.class, protocol, "SslStoreProvider can only be used with Http11NioProtocol");
/*     */   }
/*     */   
/*     */   private void configureSslKeyStore(AbstractHttp11JsseProtocol<?> protocol, Ssl ssl)
/*     */   {
/*     */     try {
/* 436 */       protocol.setKeystoreFile(ResourceUtils.getURL(ssl.getKeyStore()).toString());
/*     */     }
/*     */     catch (FileNotFoundException ex)
/*     */     {
/* 440 */       throw new EmbeddedServletContainerException("Could not load key store: " + ex.getMessage(), ex);
/*     */     }
/* 442 */     if (ssl.getKeyStoreType() != null) {
/* 443 */       protocol.setKeystoreType(ssl.getKeyStoreType());
/*     */     }
/* 445 */     if (ssl.getKeyStoreProvider() != null) {
/* 446 */       protocol.setKeystoreProvider(ssl.getKeyStoreProvider());
/*     */     }
/*     */   }
/*     */   
/*     */   private void configureSslTrustStore(AbstractHttp11JsseProtocol<?> protocol, Ssl ssl)
/*     */   {
/* 452 */     if (ssl.getTrustStore() != null) {
/*     */       try {
/* 454 */         protocol.setTruststoreFile(
/* 455 */           ResourceUtils.getURL(ssl.getTrustStore()).toString());
/*     */       }
/*     */       catch (FileNotFoundException ex)
/*     */       {
/* 459 */         throw new EmbeddedServletContainerException("Could not load trust store: " + ex.getMessage(), ex);
/*     */       }
/*     */     }
/* 462 */     protocol.setTruststorePass(ssl.getTrustStorePassword());
/* 463 */     if (ssl.getTrustStoreType() != null) {
/* 464 */       protocol.setTruststoreType(ssl.getTrustStoreType());
/*     */     }
/* 466 */     if (ssl.getTrustStoreProvider() != null) {
/* 467 */       protocol.setTruststoreProvider(ssl.getTrustStoreProvider());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void configureContext(Context context, ServletContextInitializer[] initializers)
/*     */   {
/* 478 */     TomcatStarter starter = new TomcatStarter(initializers);
/* 479 */     if ((context instanceof TomcatEmbeddedContext))
/*     */     {
/* 481 */       ((TomcatEmbeddedContext)context).setStarter(starter);
/*     */     }
/* 483 */     context.addServletContainerInitializer(starter, NO_CLASSES);
/* 484 */     for (LifecycleListener lifecycleListener : this.contextLifecycleListeners) {
/* 485 */       context.addLifecycleListener(lifecycleListener);
/*     */     }
/* 487 */     for (Valve valve : this.contextValves) {
/* 488 */       context.getPipeline().addValve(valve);
/*     */     }
/* 490 */     for (ErrorPage errorPage : getErrorPages()) {
/* 491 */       new TomcatErrorPage(errorPage).addToContext(context);
/*     */     }
/* 493 */     for (MimeMappings.Mapping mapping : getMimeMappings()) {
/* 494 */       context.addMimeMapping(mapping.getExtension(), mapping.getMimeType());
/*     */     }
/* 496 */     configureSession(context);
/* 497 */     for (TomcatContextCustomizer customizer : this.tomcatContextCustomizers) {
/* 498 */       customizer.customize(context);
/*     */     }
/*     */   }
/*     */   
/*     */   private void configureSession(Context context) {
/* 503 */     long sessionTimeout = getSessionTimeoutInMinutes();
/* 504 */     context.setSessionTimeout((int)sessionTimeout);
/* 505 */     if (isPersistSession()) {
/* 506 */       Manager manager = context.getManager();
/* 507 */       if (manager == null) {
/* 508 */         manager = new StandardManager();
/* 509 */         context.setManager(manager);
/*     */       }
/* 511 */       configurePersistSession(manager);
/*     */     }
/*     */     else {
/* 514 */       context.addLifecycleListener(new DisablePersistSessionListener(null));
/*     */     }
/*     */   }
/*     */   
/*     */   private void configurePersistSession(Manager manager) {
/* 519 */     Assert.state(manager instanceof StandardManager, "Unable to persist HTTP session state using manager type " + manager
/*     */     
/* 521 */       .getClass().getName());
/* 522 */     File dir = getValidSessionStoreDir();
/* 523 */     File file = new File(dir, "SESSIONS.ser");
/* 524 */     ((StandardManager)manager).setPathname(file.getAbsolutePath());
/*     */   }
/*     */   
/*     */   private long getSessionTimeoutInMinutes() {
/* 528 */     long sessionTimeout = getSessionTimeout();
/* 529 */     if (sessionTimeout > 0L) {
/* 530 */       sessionTimeout = Math.max(TimeUnit.SECONDS.toMinutes(sessionTimeout), 1L);
/*     */     }
/* 532 */     return sessionTimeout;
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
/*     */   protected void postProcessContext(Context context) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected TomcatEmbeddedServletContainer getTomcatEmbeddedServletContainer(Tomcat tomcat)
/*     */   {
/* 554 */     return new TomcatEmbeddedServletContainer(tomcat, getPort() >= 0);
/*     */   }
/*     */   
/*     */   public void setResourceLoader(ResourceLoader resourceLoader)
/*     */   {
/* 559 */     this.resourceLoader = resourceLoader;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBaseDirectory(File baseDirectory)
/*     */   {
/* 567 */     this.baseDirectory = baseDirectory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void setTldSkip(String tldSkip)
/*     */   {
/* 578 */     Assert.notNull(tldSkip, "TldSkip must not be null");
/* 579 */     setTldSkipPatterns(StringUtils.commaDelimitedListToSet(tldSkip));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<String> getTldSkipPatterns()
/*     */   {
/* 587 */     return this.tldSkipPatterns;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTldSkipPatterns(Collection<String> patterns)
/*     */   {
/* 596 */     Assert.notNull(patterns, "Patterns must not be null");
/* 597 */     this.tldSkipPatterns = new LinkedHashSet(patterns);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addTldSkipPatterns(String... patterns)
/*     */   {
/* 606 */     Assert.notNull(patterns, "Patterns must not be null");
/* 607 */     this.tldSkipPatterns.addAll(Arrays.asList(patterns));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setProtocol(String protocol)
/*     */   {
/* 616 */     Assert.hasLength(protocol, "Protocol must not be empty");
/* 617 */     this.protocol = protocol;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEngineValves(Collection<? extends Valve> engineValves)
/*     */   {
/* 626 */     Assert.notNull(engineValves, "Valves must not be null");
/* 627 */     this.engineValves = new ArrayList(engineValves);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<Valve> getEngineValves()
/*     */   {
/* 636 */     return this.engineValves;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addEngineValves(Valve... engineValves)
/*     */   {
/* 644 */     Assert.notNull(engineValves, "Valves must not be null");
/* 645 */     this.engineValves.addAll(Arrays.asList(engineValves));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setContextValves(Collection<? extends Valve> contextValves)
/*     */   {
/* 654 */     Assert.notNull(contextValves, "Valves must not be null");
/* 655 */     this.contextValves = new ArrayList(contextValves);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<Valve> getContextValves()
/*     */   {
/* 665 */     return this.contextValves;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addContextValves(Valve... contextValves)
/*     */   {
/* 673 */     Assert.notNull(contextValves, "Valves must not be null");
/* 674 */     this.contextValves.addAll(Arrays.asList(contextValves));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setContextLifecycleListeners(Collection<? extends LifecycleListener> contextLifecycleListeners)
/*     */   {
/* 684 */     Assert.notNull(contextLifecycleListeners, "ContextLifecycleListeners must not be null");
/*     */     
/* 686 */     this.contextLifecycleListeners = new ArrayList(contextLifecycleListeners);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<LifecycleListener> getContextLifecycleListeners()
/*     */   {
/* 696 */     return this.contextLifecycleListeners;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addContextLifecycleListeners(LifecycleListener... contextLifecycleListeners)
/*     */   {
/* 705 */     Assert.notNull(contextLifecycleListeners, "ContextLifecycleListeners must not be null");
/*     */     
/* 707 */     this.contextLifecycleListeners.addAll(Arrays.asList(contextLifecycleListeners));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTomcatContextCustomizers(Collection<? extends TomcatContextCustomizer> tomcatContextCustomizers)
/*     */   {
/* 717 */     Assert.notNull(tomcatContextCustomizers, "TomcatContextCustomizers must not be null");
/*     */     
/* 719 */     this.tomcatContextCustomizers = new ArrayList(tomcatContextCustomizers);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<TomcatContextCustomizer> getTomcatContextCustomizers()
/*     */   {
/* 729 */     return this.tomcatContextCustomizers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addContextCustomizers(TomcatContextCustomizer... tomcatContextCustomizers)
/*     */   {
/* 739 */     Assert.notNull(tomcatContextCustomizers, "TomcatContextCustomizers must not be null");
/*     */     
/* 741 */     this.tomcatContextCustomizers.addAll(Arrays.asList(tomcatContextCustomizers));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTomcatConnectorCustomizers(Collection<? extends TomcatConnectorCustomizer> tomcatConnectorCustomizers)
/*     */   {
/* 751 */     Assert.notNull(tomcatConnectorCustomizers, "TomcatConnectorCustomizers must not be null");
/*     */     
/* 753 */     this.tomcatConnectorCustomizers = new ArrayList(tomcatConnectorCustomizers);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addConnectorCustomizers(TomcatConnectorCustomizer... tomcatConnectorCustomizers)
/*     */   {
/* 764 */     Assert.notNull(tomcatConnectorCustomizers, "TomcatConnectorCustomizers must not be null");
/*     */     
/* 766 */     this.tomcatConnectorCustomizers.addAll(Arrays.asList(tomcatConnectorCustomizers));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<TomcatConnectorCustomizer> getTomcatConnectorCustomizers()
/*     */   {
/* 775 */     return this.tomcatConnectorCustomizers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addAdditionalTomcatConnectors(Connector... connectors)
/*     */   {
/* 783 */     Assert.notNull(connectors, "Connectors must not be null");
/* 784 */     this.additionalTomcatConnectors.addAll(Arrays.asList(connectors));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<Connector> getAdditionalTomcatConnectors()
/*     */   {
/* 793 */     return this.additionalTomcatConnectors;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUriEncoding(Charset uriEncoding)
/*     */   {
/* 802 */     this.uriEncoding = uriEncoding;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Charset getUriEncoding()
/*     */   {
/* 810 */     return this.uriEncoding;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBackgroundProcessorDelay(int delay)
/*     */   {
/* 819 */     this.backgroundProcessorDelay = delay;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class StoreMergedWebXmlListener
/*     */     implements LifecycleListener
/*     */   {
/*     */     private static final String MERGED_WEB_XML = "org.apache.tomcat.util.scan.MergedWebXml";
/*     */     
/*     */ 
/*     */ 
/*     */     public void lifecycleEvent(LifecycleEvent event)
/*     */     {
/* 833 */       if (event.getType().equals("configure_start")) {
/* 834 */         onStart((Context)event.getLifecycle());
/*     */       }
/*     */     }
/*     */     
/*     */     private void onStart(Context context) {
/* 839 */       ServletContext servletContext = context.getServletContext();
/* 840 */       if (servletContext.getAttribute("org.apache.tomcat.util.scan.MergedWebXml") == null) {
/* 841 */         servletContext.setAttribute("org.apache.tomcat.util.scan.MergedWebXml", getEmptyWebXml());
/*     */       }
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     private String getEmptyWebXml()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: ldc 16
/*     */       //   2: ldc 17
/*     */       //   4: invokevirtual 18	java/lang/Class:getResourceAsStream	(Ljava/lang/String;)Ljava/io/InputStream;
/*     */       //   7: astore_1
/*     */       //   8: aload_1
/*     */       //   9: ifnull +7 -> 16
/*     */       //   12: iconst_1
/*     */       //   13: goto +4 -> 17
/*     */       //   16: iconst_0
/*     */       //   17: ldc 19
/*     */       //   19: invokestatic 20	org/springframework/util/Assert:state	(ZLjava/lang/String;)V
/*     */       //   22: aload_1
/*     */       //   23: ldc 21
/*     */       //   25: invokestatic 22	java/nio/charset/Charset:forName	(Ljava/lang/String;)Ljava/nio/charset/Charset;
/*     */       //   28: invokestatic 23	org/springframework/util/StreamUtils:copyToString	(Ljava/io/InputStream;Ljava/nio/charset/Charset;)Ljava/lang/String;
/*     */       //   31: astore_2
/*     */       //   32: aload_1
/*     */       //   33: invokevirtual 24	java/io/InputStream:close	()V
/*     */       //   36: aload_2
/*     */       //   37: areturn
/*     */       //   38: astore_3
/*     */       //   39: aload_1
/*     */       //   40: invokevirtual 24	java/io/InputStream:close	()V
/*     */       //   43: aload_3
/*     */       //   44: athrow
/*     */       //   45: astore_2
/*     */       //   46: new 26	java/lang/IllegalStateException
/*     */       //   49: dup
/*     */       //   50: aload_2
/*     */       //   51: invokespecial 27	java/lang/IllegalStateException:<init>	(Ljava/lang/Throwable;)V
/*     */       //   54: athrow
/*     */       // Line number table:
/*     */       //   Java source line #846	-> byte code offset #0
/*     */       //   Java source line #847	-> byte code offset #4
/*     */       //   Java source line #848	-> byte code offset #8
/*     */       //   Java source line #851	-> byte code offset #22
/*     */       //   Java source line #854	-> byte code offset #32
/*     */       //   Java source line #851	-> byte code offset #36
/*     */       //   Java source line #854	-> byte code offset #38
/*     */       //   Java source line #857	-> byte code offset #45
/*     */       //   Java source line #858	-> byte code offset #46
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	55	0	this	StoreMergedWebXmlListener
/*     */       //   7	33	1	stream	java.io.InputStream
/*     */       //   45	6	2	ex	java.io.IOException
/*     */       //   38	6	3	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   22	32	38	finally
/*     */       //   22	36	45	java/io/IOException
/*     */       //   38	45	45	java/io/IOException
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DisablePersistSessionListener
/*     */     implements LifecycleListener
/*     */   {
/*     */     public void lifecycleEvent(LifecycleEvent event)
/*     */     {
/* 873 */       if (event.getType().equals("start")) {
/* 874 */         Context context = (Context)event.getLifecycle();
/* 875 */         Manager manager = context.getManager();
/* 876 */         if ((manager != null) && ((manager instanceof StandardManager))) {
/* 877 */           ((StandardManager)manager).setPathname(null);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\tomcat\TomcatEmbeddedServletContainerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */