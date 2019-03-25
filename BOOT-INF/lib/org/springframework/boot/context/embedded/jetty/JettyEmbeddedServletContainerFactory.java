/*      */ package org.springframework.boot.context.embedded.jetty;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Method;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.net.URL;
/*      */ import java.nio.charset.Charset;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import javax.servlet.ServletException;
/*      */ import javax.servlet.http.HttpServletRequest;
/*      */ import javax.servlet.http.HttpServletResponse;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.eclipse.jetty.http.HttpVersion;
/*      */ import org.eclipse.jetty.http.MimeTypes;
/*      */ import org.eclipse.jetty.server.AbstractConnector;
/*      */ import org.eclipse.jetty.server.ConnectionFactory;
/*      */ import org.eclipse.jetty.server.Connector;
/*      */ import org.eclipse.jetty.server.ForwardedRequestCustomizer;
/*      */ import org.eclipse.jetty.server.Handler;
/*      */ import org.eclipse.jetty.server.HttpConfiguration;
/*      */ import org.eclipse.jetty.server.HttpConfiguration.ConnectionFactory;
/*      */ import org.eclipse.jetty.server.HttpConnectionFactory;
/*      */ import org.eclipse.jetty.server.Request;
/*      */ import org.eclipse.jetty.server.SecureRequestCustomizer;
/*      */ import org.eclipse.jetty.server.Server;
/*      */ import org.eclipse.jetty.server.ServerConnector;
/*      */ import org.eclipse.jetty.server.SslConnectionFactory;
/*      */ import org.eclipse.jetty.server.handler.ErrorHandler;
/*      */ import org.eclipse.jetty.server.handler.HandlerWrapper;
/*      */ import org.eclipse.jetty.server.handler.gzip.GzipHandler;
/*      */ import org.eclipse.jetty.server.session.DefaultSessionCache;
/*      */ import org.eclipse.jetty.server.session.FileSessionDataStore;
/*      */ import org.eclipse.jetty.server.session.SessionHandler;
/*      */ import org.eclipse.jetty.servlet.ErrorPageErrorHandler;
/*      */ import org.eclipse.jetty.servlet.ServletHandler;
/*      */ import org.eclipse.jetty.servlet.ServletHolder;
/*      */ import org.eclipse.jetty.servlet.ServletMapping;
/*      */ import org.eclipse.jetty.util.resource.JarResource;
/*      */ import org.eclipse.jetty.util.resource.Resource;
/*      */ import org.eclipse.jetty.util.resource.ResourceCollection;
/*      */ import org.eclipse.jetty.util.ssl.SslContextFactory;
/*      */ import org.eclipse.jetty.util.thread.ThreadPool;
/*      */ import org.eclipse.jetty.webapp.AbstractConfiguration;
/*      */ import org.eclipse.jetty.webapp.Configuration;
/*      */ import org.eclipse.jetty.webapp.WebAppContext;
/*      */ import org.springframework.boot.context.embedded.AbstractEmbeddedServletContainerFactory;
/*      */ import org.springframework.boot.context.embedded.Compression;
/*      */ import org.springframework.boot.context.embedded.EmbeddedServletContainer;
/*      */ import org.springframework.boot.context.embedded.EmbeddedServletContainerException;
/*      */ import org.springframework.boot.context.embedded.JspServlet;
/*      */ import org.springframework.boot.context.embedded.MimeMappings.Mapping;
/*      */ import org.springframework.boot.context.embedded.Ssl;
/*      */ import org.springframework.boot.context.embedded.Ssl.ClientAuth;
/*      */ import org.springframework.boot.context.embedded.SslStoreProvider;
/*      */ import org.springframework.boot.web.servlet.ErrorPage;
/*      */ import org.springframework.boot.web.servlet.ServletContextInitializer;
/*      */ import org.springframework.context.ResourceLoaderAware;
/*      */ import org.springframework.core.io.ResourceLoader;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.ReflectionUtils;
/*      */ import org.springframework.util.ResourceUtils;
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
/*      */ public class JettyEmbeddedServletContainerFactory
/*      */   extends AbstractEmbeddedServletContainerFactory
/*      */   implements ResourceLoaderAware
/*      */ {
/*      */   private static final String GZIP_HANDLER_JETTY_9_2 = "org.eclipse.jetty.servlets.gzip.GzipHandler";
/*      */   private static final String GZIP_HANDLER_JETTY_8 = "org.eclipse.jetty.server.handler.GzipHandler";
/*      */   private static final String GZIP_HANDLER_JETTY_9_3 = "org.eclipse.jetty.server.handler.gzip.GzipHandler";
/*      */   private static final String CONNECTOR_JETTY_8 = "org.eclipse.jetty.server.nio.SelectChannelConnector";
/*      */   private static final String SESSION_JETTY_9_3 = "org.eclipse.jetty.server.session.HashSessionManager";
/*  121 */   private List<Configuration> configurations = new ArrayList();
/*      */   
/*      */ 
/*      */ 
/*      */   private boolean useForwardHeaders;
/*      */   
/*      */ 
/*  128 */   private int acceptors = -1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  133 */   private int selectors = -1;
/*      */   
/*  135 */   private List<JettyServerCustomizer> jettyServerCustomizers = new ArrayList();
/*      */   
/*      */ 
/*      */ 
/*      */   private ResourceLoader resourceLoader;
/*      */   
/*      */ 
/*      */ 
/*      */   private ThreadPool threadPool;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JettyEmbeddedServletContainerFactory() {}
/*      */   
/*      */ 
/*      */ 
/*      */   public JettyEmbeddedServletContainerFactory(int port)
/*      */   {
/*  154 */     super(port);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JettyEmbeddedServletContainerFactory(String contextPath, int port)
/*      */   {
/*  164 */     super(contextPath, port);
/*      */   }
/*      */   
/*      */ 
/*      */   public EmbeddedServletContainer getEmbeddedServletContainer(ServletContextInitializer... initializers)
/*      */   {
/*  170 */     JettyEmbeddedWebAppContext context = new JettyEmbeddedWebAppContext();
/*  171 */     int port = getPort() >= 0 ? getPort() : 0;
/*  172 */     InetSocketAddress address = new InetSocketAddress(getAddress(), port);
/*  173 */     Server server = createServer(address);
/*  174 */     configureWebAppContext(context, initializers);
/*  175 */     server.setHandler(addHandlerWrappers(context));
/*  176 */     this.logger.info("Server initialized with port: " + port);
/*  177 */     SslContextFactory sslContextFactory; if ((getSsl() != null) && (getSsl().isEnabled())) {
/*  178 */       sslContextFactory = new SslContextFactory();
/*  179 */       configureSsl(sslContextFactory, getSsl());
/*      */       
/*  181 */       AbstractConnector connector = getSslServerConnectorFactory().getConnector(server, sslContextFactory, port);
/*  182 */       server.setConnectors(new Connector[] { connector });
/*      */     }
/*  184 */     for (JettyServerCustomizer customizer : getServerCustomizers()) {
/*  185 */       customizer.customize(server);
/*      */     }
/*  187 */     if (this.useForwardHeaders) {
/*  188 */       new ForwardHeadersCustomizer(null).customize(server);
/*      */     }
/*  190 */     return getJettyEmbeddedServletContainer(server);
/*      */   }
/*      */   
/*      */   private Server createServer(InetSocketAddress address) { Server server;
/*      */     Server server;
/*  195 */     if (ClassUtils.hasConstructor(Server.class, new Class[] { ThreadPool.class })) {
/*  196 */       server = new Jetty9ServerFactory(null).createServer(getThreadPool());
/*      */     }
/*      */     else {
/*  199 */       server = new Jetty8ServerFactory(null).createServer(getThreadPool());
/*      */     }
/*  201 */     server.setConnectors(new Connector[] { createConnector(address, server) });
/*  202 */     return server;
/*      */   }
/*      */   
/*      */   private AbstractConnector createConnector(InetSocketAddress address, Server server) {
/*  206 */     if (ClassUtils.isPresent("org.eclipse.jetty.server.nio.SelectChannelConnector", getClass().getClassLoader())) {
/*  207 */       return new Jetty8ConnectorFactory(null).createConnector(server, address, this.acceptors, this.selectors);
/*      */     }
/*      */     
/*  210 */     return new Jetty9ConnectorFactory(null).createConnector(server, address, this.acceptors, this.selectors);
/*      */   }
/*      */   
/*      */   private Handler addHandlerWrappers(Handler handler)
/*      */   {
/*  215 */     if ((getCompression() != null) && (getCompression().getEnabled())) {
/*  216 */       handler = applyWrapper(handler, createGzipHandler());
/*      */     }
/*  218 */     if (StringUtils.hasText(getServerHeader())) {
/*  219 */       handler = applyWrapper(handler, new ServerHeaderHandler(getServerHeader()));
/*      */     }
/*  221 */     return handler;
/*      */   }
/*      */   
/*      */   private Handler applyWrapper(Handler handler, HandlerWrapper wrapper) {
/*  225 */     wrapper.setHandler(handler);
/*  226 */     return wrapper;
/*      */   }
/*      */   
/*      */   private HandlerWrapper createGzipHandler() {
/*  230 */     ClassLoader classLoader = getClass().getClassLoader();
/*  231 */     if (ClassUtils.isPresent("org.eclipse.jetty.servlets.gzip.GzipHandler", classLoader)) {
/*  232 */       return new Jetty92GzipHandlerFactory(null).createGzipHandler(getCompression());
/*      */     }
/*  234 */     if (ClassUtils.isPresent("org.eclipse.jetty.server.handler.GzipHandler", getClass().getClassLoader())) {
/*  235 */       return new Jetty8GzipHandlerFactory(null).createGzipHandler(getCompression());
/*      */     }
/*  237 */     if (ClassUtils.isPresent("org.eclipse.jetty.server.handler.gzip.GzipHandler", getClass().getClassLoader())) {
/*  238 */       return new Jetty93GzipHandlerFactory(null).createGzipHandler(getCompression());
/*      */     }
/*  240 */     throw new IllegalStateException("Compression is enabled, but GzipHandler is not on the classpath");
/*      */   }
/*      */   
/*      */   private SslServerConnectorFactory getSslServerConnectorFactory()
/*      */   {
/*  245 */     if (ClassUtils.isPresent("org.eclipse.jetty.server.ssl.SslSocketConnector", null))
/*      */     {
/*  247 */       return new Jetty8SslServerConnectorFactory(null);
/*      */     }
/*  249 */     return new Jetty9SslServerConnectorFactory(null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void configureSsl(SslContextFactory factory, Ssl ssl)
/*      */   {
/*  258 */     factory.setProtocol(ssl.getProtocol());
/*  259 */     configureSslClientAuth(factory, ssl);
/*  260 */     configureSslPasswords(factory, ssl);
/*  261 */     factory.setCertAlias(ssl.getKeyAlias());
/*  262 */     if (!ObjectUtils.isEmpty(ssl.getCiphers())) {
/*  263 */       factory.setIncludeCipherSuites(ssl.getCiphers());
/*  264 */       factory.setExcludeCipherSuites(new String[0]);
/*      */     }
/*  266 */     if (ssl.getEnabledProtocols() != null) {
/*  267 */       factory.setIncludeProtocols(ssl.getEnabledProtocols());
/*      */     }
/*  269 */     if (getSslStoreProvider() != null) {
/*      */       try {
/*  271 */         factory.setKeyStore(getSslStoreProvider().getKeyStore());
/*  272 */         factory.setTrustStore(getSslStoreProvider().getTrustStore());
/*      */       }
/*      */       catch (Exception ex) {
/*  275 */         throw new IllegalStateException("Unable to set SSL store", ex);
/*      */       }
/*      */     }
/*      */     else {
/*  279 */       configureSslKeyStore(factory, ssl);
/*  280 */       configureSslTrustStore(factory, ssl);
/*      */     }
/*      */   }
/*      */   
/*      */   private void configureSslClientAuth(SslContextFactory factory, Ssl ssl) {
/*  285 */     if (ssl.getClientAuth() == Ssl.ClientAuth.NEED) {
/*  286 */       factory.setNeedClientAuth(true);
/*  287 */       factory.setWantClientAuth(true);
/*      */     }
/*  289 */     else if (ssl.getClientAuth() == Ssl.ClientAuth.WANT) {
/*  290 */       factory.setWantClientAuth(true);
/*      */     }
/*      */   }
/*      */   
/*      */   private void configureSslPasswords(SslContextFactory factory, Ssl ssl) {
/*  295 */     if (ssl.getKeyStorePassword() != null) {
/*  296 */       factory.setKeyStorePassword(ssl.getKeyStorePassword());
/*      */     }
/*  298 */     if (ssl.getKeyPassword() != null) {
/*  299 */       factory.setKeyManagerPassword(ssl.getKeyPassword());
/*      */     }
/*      */   }
/*      */   
/*      */   private void configureSslKeyStore(SslContextFactory factory, Ssl ssl) {
/*      */     try {
/*  305 */       URL url = ResourceUtils.getURL(ssl.getKeyStore());
/*  306 */       factory.setKeyStoreResource(Resource.newResource(url));
/*      */     }
/*      */     catch (IOException ex)
/*      */     {
/*  310 */       throw new EmbeddedServletContainerException("Could not find key store '" + ssl.getKeyStore() + "'", ex);
/*      */     }
/*  312 */     if (ssl.getKeyStoreType() != null) {
/*  313 */       factory.setKeyStoreType(ssl.getKeyStoreType());
/*      */     }
/*  315 */     if (ssl.getKeyStoreProvider() != null) {
/*  316 */       factory.setKeyStoreProvider(ssl.getKeyStoreProvider());
/*      */     }
/*      */   }
/*      */   
/*      */   private void configureSslTrustStore(SslContextFactory factory, Ssl ssl) {
/*  321 */     if (ssl.getTrustStorePassword() != null) {
/*  322 */       factory.setTrustStorePassword(ssl.getTrustStorePassword());
/*      */     }
/*  324 */     if (ssl.getTrustStore() != null) {
/*      */       try {
/*  326 */         URL url = ResourceUtils.getURL(ssl.getTrustStore());
/*  327 */         factory.setTrustStoreResource(Resource.newResource(url));
/*      */       }
/*      */       catch (IOException ex)
/*      */       {
/*  331 */         throw new EmbeddedServletContainerException("Could not find trust store '" + ssl.getTrustStore() + "'", ex);
/*      */       }
/*      */     }
/*  334 */     if (ssl.getTrustStoreType() != null) {
/*  335 */       factory.setTrustStoreType(ssl.getTrustStoreType());
/*      */     }
/*  337 */     if (ssl.getTrustStoreProvider() != null) {
/*  338 */       factory.setTrustStoreProvider(ssl.getTrustStoreProvider());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void configureWebAppContext(WebAppContext context, ServletContextInitializer... initializers)
/*      */   {
/*  349 */     Assert.notNull(context, "Context must not be null");
/*  350 */     context.setTempDirectory(getTempDirectory());
/*  351 */     if (this.resourceLoader != null) {
/*  352 */       context.setClassLoader(this.resourceLoader.getClassLoader());
/*      */     }
/*  354 */     String contextPath = getContextPath();
/*  355 */     context.setContextPath(StringUtils.hasLength(contextPath) ? contextPath : "/");
/*  356 */     context.setDisplayName(getDisplayName());
/*  357 */     configureDocumentRoot(context);
/*  358 */     if (isRegisterDefaultServlet()) {
/*  359 */       addDefaultServlet(context);
/*      */     }
/*  361 */     if (shouldRegisterJspServlet()) {
/*  362 */       addJspServlet(context);
/*  363 */       context.addBean(new JasperInitializer(context), true);
/*      */     }
/*  365 */     addLocaleMappings(context);
/*  366 */     ServletContextInitializer[] initializersToUse = mergeInitializers(initializers);
/*  367 */     Configuration[] configurations = getWebAppContextConfigurations(context, initializersToUse);
/*      */     
/*  369 */     context.setConfigurations(configurations);
/*  370 */     configureSession(context);
/*  371 */     postProcessWebAppContext(context);
/*      */   }
/*      */   
/*      */   private void configureSession(WebAppContext context) {
/*  375 */     SessionConfigurer configurer = getSessionConfigurer();
/*  376 */     configurer.configure(context, getSessionTimeout(), isPersistSession(), new SessionDirectory()
/*      */     {
/*      */ 
/*      */       public File get()
/*      */       {
/*  381 */         return 
/*  382 */           JettyEmbeddedServletContainerFactory.this.getValidSessionStoreDir();
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   private void addLocaleMappings(WebAppContext context)
/*      */   {
/*  389 */     for (Map.Entry<Locale, Charset> entry : getLocaleCharsetMappings().entrySet()) {
/*  390 */       Locale locale = (Locale)entry.getKey();
/*  391 */       Charset charset = (Charset)entry.getValue();
/*  392 */       context.addLocaleEncoding(locale.toString(), charset.toString());
/*      */     }
/*      */   }
/*      */   
/*      */   private SessionConfigurer getSessionConfigurer() {
/*  397 */     if (ClassUtils.isPresent("org.eclipse.jetty.server.session.HashSessionManager", getClass().getClassLoader())) {
/*  398 */       return new Jetty93SessionConfigurer(null);
/*      */     }
/*  400 */     return new Jetty94SessionConfigurer(null);
/*      */   }
/*      */   
/*      */   private File getTempDirectory() {
/*  404 */     String temp = System.getProperty("java.io.tmpdir");
/*  405 */     return temp == null ? null : new File(temp);
/*      */   }
/*      */   
/*      */   private void configureDocumentRoot(WebAppContext handler) {
/*  409 */     File root = getValidDocumentRoot();
/*  410 */     root = root != null ? root : createTempDir("jetty-docbase");
/*      */     try {
/*  412 */       List<Resource> resources = new ArrayList();
/*  413 */       resources.add(root
/*  414 */         .isDirectory() ? Resource.newResource(root.getCanonicalFile()) : 
/*  415 */         JarResource.newJarResource(Resource.newResource(root)));
/*  416 */       for (URL resourceJarUrl : getUrlsOfJarsWithMetaInfResources()) {
/*  417 */         Resource resource = createResource(resourceJarUrl);
/*      */         
/*      */ 
/*  420 */         if ((resource.exists()) && (resource.isDirectory())) {
/*  421 */           resources.add(resource);
/*      */         }
/*      */       }
/*  424 */       handler.setBaseResource(new ResourceCollection(
/*  425 */         (Resource[])resources.toArray(new Resource[resources.size()])));
/*      */     }
/*      */     catch (Exception ex) {
/*  428 */       throw new IllegalStateException(ex);
/*      */     }
/*      */   }
/*      */   
/*      */   private Resource createResource(URL url) throws IOException {
/*  433 */     if ("file".equals(url.getProtocol())) {
/*  434 */       File file = new File(url.getFile());
/*  435 */       if (file.isFile()) {
/*  436 */         return Resource.newResource("jar:" + url + "!/META-INF/resources");
/*      */       }
/*      */     }
/*  439 */     return Resource.newResource(url + "META-INF/resources");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void addDefaultServlet(WebAppContext context)
/*      */   {
/*  447 */     Assert.notNull(context, "Context must not be null");
/*  448 */     ServletHolder holder = new ServletHolder();
/*  449 */     holder.setName("default");
/*  450 */     holder.setClassName("org.eclipse.jetty.servlet.DefaultServlet");
/*  451 */     holder.setInitParameter("dirAllowed", "false");
/*  452 */     holder.setInitOrder(1);
/*  453 */     context.getServletHandler().addServletWithMapping(holder, "/");
/*  454 */     context.getServletHandler().getServletMapping("/").setDefault(true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void addJspServlet(WebAppContext context)
/*      */   {
/*  462 */     Assert.notNull(context, "Context must not be null");
/*  463 */     ServletHolder holder = new ServletHolder();
/*  464 */     holder.setName("jsp");
/*  465 */     holder.setClassName(getJspServlet().getClassName());
/*  466 */     holder.setInitParameter("fork", "false");
/*  467 */     holder.setInitParameters(getJspServlet().getInitParameters());
/*  468 */     holder.setInitOrder(3);
/*  469 */     context.getServletHandler().addServlet(holder);
/*  470 */     ServletMapping mapping = new ServletMapping();
/*  471 */     mapping.setServletName("jsp");
/*  472 */     mapping.setPathSpecs(new String[] { "*.jsp", "*.jspx" });
/*  473 */     context.getServletHandler().addServletMapping(mapping);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Configuration[] getWebAppContextConfigurations(WebAppContext webAppContext, ServletContextInitializer... initializers)
/*      */   {
/*  484 */     List<Configuration> configurations = new ArrayList();
/*  485 */     configurations.add(
/*  486 */       getServletContextInitializerConfiguration(webAppContext, initializers));
/*  487 */     configurations.addAll(getConfigurations());
/*  488 */     configurations.add(getErrorPageConfiguration());
/*  489 */     configurations.add(getMimeTypeConfiguration());
/*  490 */     return (Configuration[])configurations.toArray(new Configuration[configurations.size()]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private Configuration getErrorPageConfiguration()
/*      */   {
/*  498 */     new AbstractConfiguration()
/*      */     {
/*      */       public void configure(WebAppContext context) throws Exception
/*      */       {
/*  502 */         ErrorHandler errorHandler = context.getErrorHandler();
/*  503 */         context.setErrorHandler(new JettyEmbeddedErrorHandler(errorHandler));
/*  504 */         JettyEmbeddedServletContainerFactory.this.addJettyErrorPages(errorHandler, JettyEmbeddedServletContainerFactory.this.getErrorPages());
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Configuration getMimeTypeConfiguration()
/*      */   {
/*  515 */     new AbstractConfiguration()
/*      */     {
/*      */       public void configure(WebAppContext context) throws Exception
/*      */       {
/*  519 */         MimeTypes mimeTypes = context.getMimeTypes();
/*  520 */         for (MimeMappings.Mapping mapping : JettyEmbeddedServletContainerFactory.this.getMimeMappings()) {
/*  521 */           mimeTypes.addMimeMapping(mapping.getExtension(), mapping
/*  522 */             .getMimeType());
/*      */         }
/*      */       }
/*      */     };
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
/*      */   protected Configuration getServletContextInitializerConfiguration(WebAppContext webAppContext, ServletContextInitializer... initializers)
/*      */   {
/*  539 */     return new ServletContextInitializerConfiguration(initializers);
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
/*      */   protected void postProcessWebAppContext(WebAppContext webAppContext) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JettyEmbeddedServletContainer getJettyEmbeddedServletContainer(Server server)
/*      */   {
/*  561 */     return new JettyEmbeddedServletContainer(server, getPort() >= 0);
/*      */   }
/*      */   
/*      */   public void setResourceLoader(ResourceLoader resourceLoader)
/*      */   {
/*  566 */     this.resourceLoader = resourceLoader;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseForwardHeaders(boolean useForwardHeaders)
/*      */   {
/*  575 */     this.useForwardHeaders = useForwardHeaders;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAcceptors(int acceptors)
/*      */   {
/*  584 */     this.acceptors = acceptors;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSelectors(int selectors)
/*      */   {
/*  593 */     this.selectors = selectors;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setServerCustomizers(Collection<? extends JettyServerCustomizer> customizers)
/*      */   {
/*  603 */     Assert.notNull(customizers, "Customizers must not be null");
/*  604 */     this.jettyServerCustomizers = new ArrayList(customizers);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Collection<JettyServerCustomizer> getServerCustomizers()
/*      */   {
/*  613 */     return this.jettyServerCustomizers;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addServerCustomizers(JettyServerCustomizer... customizers)
/*      */   {
/*  622 */     Assert.notNull(customizers, "Customizers must not be null");
/*  623 */     this.jettyServerCustomizers.addAll(Arrays.asList(customizers));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setConfigurations(Collection<? extends Configuration> configurations)
/*      */   {
/*  633 */     Assert.notNull(configurations, "Configurations must not be null");
/*  634 */     this.configurations = new ArrayList(configurations);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Collection<Configuration> getConfigurations()
/*      */   {
/*  643 */     return this.configurations;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addConfigurations(Configuration... configurations)
/*      */   {
/*  652 */     Assert.notNull(configurations, "Configurations must not be null");
/*  653 */     this.configurations.addAll(Arrays.asList(configurations));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ThreadPool getThreadPool()
/*      */   {
/*  661 */     return this.threadPool;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setThreadPool(ThreadPool threadPool)
/*      */   {
/*  670 */     this.threadPool = threadPool;
/*      */   }
/*      */   
/*      */   private void addJettyErrorPages(ErrorHandler errorHandler, Collection<ErrorPage> errorPages) {
/*      */     ErrorPageErrorHandler handler;
/*  675 */     if ((errorHandler instanceof ErrorPageErrorHandler)) {
/*  676 */       handler = (ErrorPageErrorHandler)errorHandler;
/*  677 */       for (ErrorPage errorPage : errorPages) {
/*  678 */         if (errorPage.isGlobal()) {
/*  679 */           handler.addErrorPage("org.eclipse.jetty.server.error_page.global", errorPage
/*  680 */             .getPath());
/*      */ 
/*      */         }
/*  683 */         else if (errorPage.getExceptionName() != null) {
/*  684 */           handler.addErrorPage(errorPage.getExceptionName(), errorPage
/*  685 */             .getPath());
/*      */         }
/*      */         else {
/*  688 */           handler.addErrorPage(errorPage.getStatusCode(), errorPage
/*  689 */             .getPath());
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
/*      */   private static abstract interface SslServerConnectorFactory
/*      */   {
/*      */     public abstract AbstractConnector getConnector(Server paramServer, SslContextFactory paramSslContextFactory, int paramInt);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class Jetty9SslServerConnectorFactory
/*      */     implements JettyEmbeddedServletContainerFactory.SslServerConnectorFactory
/*      */   {
/*      */     public ServerConnector getConnector(Server server, SslContextFactory sslContextFactory, int port)
/*      */     {
/*  715 */       HttpConfiguration config = new HttpConfiguration();
/*  716 */       config.setSendServerVersion(false);
/*  717 */       config.addCustomizer(new SecureRequestCustomizer());
/*  718 */       HttpConnectionFactory connectionFactory = new HttpConnectionFactory(config);
/*      */       
/*  720 */       SslConnectionFactory sslConnectionFactory = new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString());
/*  721 */       ServerConnector serverConnector = new ServerConnector(server, new ConnectionFactory[] { sslConnectionFactory, connectionFactory });
/*      */       
/*  723 */       serverConnector.setPort(port);
/*  724 */       return serverConnector;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class Jetty8SslServerConnectorFactory
/*      */     implements JettyEmbeddedServletContainerFactory.SslServerConnectorFactory
/*      */   {
/*      */     public AbstractConnector getConnector(Server server, SslContextFactory sslContextFactory, int port)
/*      */     {
/*      */       try
/*      */       {
/*  740 */         Class<?> connectorClass = Class.forName("org.eclipse.jetty.server.ssl.SslSocketConnector");
/*      */         
/*      */ 
/*  743 */         AbstractConnector connector = (AbstractConnector)connectorClass.getConstructor(new Class[] { SslContextFactory.class }).newInstance(new Object[] { sslContextFactory });
/*  744 */         connector.getClass().getMethod("setPort", new Class[] { Integer.TYPE }).invoke(connector, new Object[] {
/*  745 */           Integer.valueOf(port) });
/*  746 */         return connector;
/*      */       }
/*      */       catch (Exception ex) {
/*  749 */         throw new IllegalStateException(ex);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static abstract interface GzipHandlerFactory
/*      */   {
/*      */     public abstract HandlerWrapper createGzipHandler(Compression paramCompression);
/*      */   }
/*      */   
/*      */   private static class Jetty8GzipHandlerFactory
/*      */     implements JettyEmbeddedServletContainerFactory.GzipHandlerFactory
/*      */   {
/*      */     public HandlerWrapper createGzipHandler(Compression compression)
/*      */     {
/*      */       try
/*      */       {
/*  766 */         Class<?> handlerClass = ClassUtils.forName("org.eclipse.jetty.server.handler.GzipHandler", 
/*  767 */           getClass().getClassLoader());
/*  768 */         HandlerWrapper handler = (HandlerWrapper)handlerClass.newInstance();
/*  769 */         ReflectionUtils.findMethod(handlerClass, "setMinGzipSize", new Class[] { Integer.TYPE })
/*  770 */           .invoke(handler, new Object[] {Integer.valueOf(compression.getMinResponseSize()) });
/*  771 */         ReflectionUtils.findMethod(handlerClass, "setMimeTypes", new Class[] { Set.class })
/*  772 */           .invoke(handler, new Object[] { new HashSet(
/*  773 */           Arrays.asList(compression.getMimeTypes())) });
/*  774 */         if (compression.getExcludedUserAgents() != null)
/*      */         {
/*  776 */           ReflectionUtils.findMethod(handlerClass, "setExcluded", new Class[] { Set.class }).invoke(handler, new Object[] { new HashSet(
/*  777 */             Arrays.asList(compression.getExcludedUserAgents())) });
/*      */         }
/*  779 */         return handler;
/*      */       }
/*      */       catch (Exception ex) {
/*  782 */         throw new RuntimeException("Failed to configure Jetty 8 gzip handler", ex);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static class Jetty92GzipHandlerFactory
/*      */     implements JettyEmbeddedServletContainerFactory.GzipHandlerFactory
/*      */   {
/*      */     public HandlerWrapper createGzipHandler(Compression compression)
/*      */     {
/*      */       try
/*      */       {
/*  794 */         Class<?> handlerClass = ClassUtils.forName("org.eclipse.jetty.servlets.gzip.GzipHandler", 
/*  795 */           getClass().getClassLoader());
/*  796 */         HandlerWrapper gzipHandler = (HandlerWrapper)handlerClass.newInstance();
/*  797 */         ReflectionUtils.findMethod(handlerClass, "setMinGzipSize", new Class[] { Integer.TYPE })
/*  798 */           .invoke(gzipHandler, new Object[] {Integer.valueOf(compression.getMinResponseSize()) });
/*      */         
/*  800 */         ReflectionUtils.findMethod(handlerClass, "addIncludedMimeTypes", new Class[] { String[].class })
/*  801 */           .invoke(gzipHandler, new Object[] {compression.getMimeTypes() });
/*  802 */         if (compression.getExcludedUserAgents() != null)
/*      */         {
/*  804 */           ReflectionUtils.findMethod(handlerClass, "setExcluded", new Class[] { Set.class }).invoke(gzipHandler, new Object[] { new HashSet(
/*  805 */             Arrays.asList(compression.getExcludedUserAgents())) });
/*      */         }
/*  807 */         return gzipHandler;
/*      */       }
/*      */       catch (Exception ex) {
/*  810 */         throw new RuntimeException("Failed to configure Jetty 9.2 gzip handler", ex);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static class Jetty93GzipHandlerFactory
/*      */     implements JettyEmbeddedServletContainerFactory.GzipHandlerFactory
/*      */   {
/*      */     public HandlerWrapper createGzipHandler(Compression compression)
/*      */     {
/*  821 */       GzipHandler handler = new GzipHandler();
/*  822 */       handler.setMinGzipSize(compression.getMinResponseSize());
/*  823 */       handler.setIncludedMimeTypes(compression.getMimeTypes());
/*  824 */       if (compression.getExcludedUserAgents() != null) {
/*  825 */         handler.setExcludedAgentPatterns(compression.getExcludedUserAgents());
/*      */       }
/*  827 */       return handler;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class ForwardHeadersCustomizer
/*      */     implements JettyServerCustomizer
/*      */   {
/*      */     public void customize(Server server)
/*      */     {
/*  840 */       ForwardedRequestCustomizer customizer = new ForwardedRequestCustomizer();
/*  841 */       for (Connector connector : server.getConnectors()) {
/*  842 */         for (ConnectionFactory connectionFactory : connector
/*  843 */           .getConnectionFactories()) {
/*  844 */           if ((connectionFactory instanceof HttpConfiguration.ConnectionFactory))
/*      */           {
/*  846 */             ((HttpConfiguration.ConnectionFactory)connectionFactory).getHttpConfiguration().addCustomizer(customizer);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static class ServerHeaderHandler
/*      */     extends HandlerWrapper
/*      */   {
/*      */     private static final String SERVER_HEADER = "server";
/*      */     
/*      */     private final String value;
/*      */     
/*      */ 
/*      */     ServerHeaderHandler(String value)
/*      */     {
/*  864 */       this.value = value;
/*      */     }
/*      */     
/*      */     public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
/*      */       throws IOException, ServletException
/*      */     {
/*  870 */       if (!response.getHeaderNames().contains("server")) {
/*  871 */         response.setHeader("server", this.value);
/*      */       }
/*  873 */       super.handle(target, baseRequest, request, response);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static abstract interface ConnectorFactory
/*      */   {
/*      */     public abstract AbstractConnector createConnector(Server paramServer, InetSocketAddress paramInetSocketAddress, int paramInt1, int paramInt2);
/*      */   }
/*      */   
/*      */ 
/*      */   private static class Jetty8ConnectorFactory
/*      */     implements JettyEmbeddedServletContainerFactory.ConnectorFactory
/*      */   {
/*      */     public AbstractConnector createConnector(Server server, InetSocketAddress address, int acceptors, int selectors)
/*      */     {
/*      */       try
/*      */       {
/*  891 */         Class<?> connectorClass = ClassUtils.forName("org.eclipse.jetty.server.nio.SelectChannelConnector", 
/*  892 */           getClass().getClassLoader());
/*      */         
/*  894 */         AbstractConnector connector = (AbstractConnector)connectorClass.newInstance();
/*  895 */         ReflectionUtils.findMethod(connectorClass, "setPort", new Class[] { Integer.TYPE })
/*  896 */           .invoke(connector, new Object[] {Integer.valueOf(address.getPort()) });
/*  897 */         ReflectionUtils.findMethod(connectorClass, "setHost", new Class[] { String.class })
/*  898 */           .invoke(connector, new Object[] {address.getHostName() });
/*  899 */         if (acceptors > 0)
/*      */         {
/*  901 */           ReflectionUtils.findMethod(connectorClass, "setAcceptors", new Class[] { Integer.TYPE }).invoke(connector, new Object[] { Integer.valueOf(acceptors) });
/*      */         }
/*  903 */         if (selectors > 0)
/*      */         {
/*      */ 
/*  906 */           Object selectorManager = ReflectionUtils.findMethod(connectorClass, "getSelectorManager").invoke(connector, new Object[0]);
/*  907 */           ReflectionUtils.findMethod(selectorManager.getClass(), "setSelectSets", new Class[] { Integer.TYPE })
/*      */           
/*  909 */             .invoke(selectorManager, new Object[] {Integer.valueOf(selectors) });
/*      */         }
/*      */         
/*  912 */         return connector;
/*      */       }
/*      */       catch (Exception ex) {
/*  915 */         throw new RuntimeException("Failed to configure Jetty 8 connector", ex);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static class Jetty9ConnectorFactory
/*      */     implements JettyEmbeddedServletContainerFactory.ConnectorFactory
/*      */   {
/*      */     public AbstractConnector createConnector(Server server, InetSocketAddress address, int acceptors, int selectors)
/*      */     {
/*  926 */       ServerConnector connector = new ServerConnector(server, acceptors, selectors);
/*  927 */       connector.setHost(address.getHostName());
/*  928 */       connector.setPort(address.getPort());
/*  929 */       for (ConnectionFactory connectionFactory : connector
/*  930 */         .getConnectionFactories()) {
/*  931 */         if ((connectionFactory instanceof HttpConfiguration.ConnectionFactory))
/*      */         {
/*  933 */           ((HttpConfiguration.ConnectionFactory)connectionFactory).getHttpConfiguration().setSendServerVersion(false);
/*      */         }
/*      */       }
/*  936 */       return connector;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static abstract interface ServerFactory
/*      */   {
/*      */     public abstract Server createServer(ThreadPool paramThreadPool);
/*      */   }
/*      */   
/*      */   private static class Jetty8ServerFactory
/*      */     implements JettyEmbeddedServletContainerFactory.ServerFactory
/*      */   {
/*      */     public Server createServer(ThreadPool threadPool)
/*      */     {
/*  951 */       Server server = new Server();
/*      */       
/*      */       try
/*      */       {
/*  955 */         ReflectionUtils.findMethod(Server.class, "setThreadPool", new Class[] { ThreadPool.class }).invoke(server, new Object[] { threadPool });
/*      */       }
/*      */       catch (Exception ex) {
/*  958 */         throw new RuntimeException("Failed to configure Jetty 8 ThreadPool", ex);
/*      */       }
/*      */       
/*      */       try
/*      */       {
/*  963 */         ReflectionUtils.findMethod(Server.class, "setSendServerVersion", new Class[] { Boolean.TYPE }).invoke(server, new Object[] { Boolean.valueOf(false) });
/*      */       }
/*      */       catch (Exception ex) {
/*  966 */         throw new RuntimeException("Failed to disable Server header", ex);
/*      */       }
/*  968 */       return server;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class Jetty9ServerFactory
/*      */     implements JettyEmbeddedServletContainerFactory.ServerFactory
/*      */   {
/*      */     public Server createServer(ThreadPool threadPool)
/*      */     {
/*  977 */       return new Server(threadPool);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static abstract interface SessionDirectory
/*      */   {
/*      */     public abstract File get();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static abstract interface SessionConfigurer
/*      */   {
/*      */     public abstract void configure(WebAppContext paramWebAppContext, int paramInt, boolean paramBoolean, JettyEmbeddedServletContainerFactory.SessionDirectory paramSessionDirectory);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class Jetty93SessionConfigurer
/*      */     implements JettyEmbeddedServletContainerFactory.SessionConfigurer
/*      */   {
/*      */     public void configure(WebAppContext context, int timeout, boolean persist, JettyEmbeddedServletContainerFactory.SessionDirectory sessionDirectory)
/*      */     {
/* 1009 */       SessionHandler handler = context.getSessionHandler();
/* 1010 */       Object manager = getSessionManager(handler);
/* 1011 */       setMaxInactiveInterval(manager, timeout > 0 ? timeout : -1);
/* 1012 */       if (persist) {
/* 1013 */         Class<?> hashSessionManagerClass = ClassUtils.resolveClassName("org.eclipse.jetty.server.session.HashSessionManager", handler
/*      */         
/* 1015 */           .getClass().getClassLoader());
/* 1016 */         Assert.isInstanceOf(hashSessionManagerClass, manager, "Unable to use persistent sessions");
/*      */         
/* 1018 */         configurePersistSession(manager, sessionDirectory);
/*      */       }
/*      */     }
/*      */     
/*      */     private Object getSessionManager(SessionHandler handler) {
/* 1023 */       Method method = ReflectionUtils.findMethod(SessionHandler.class, "getSessionManager");
/*      */       
/* 1025 */       return ReflectionUtils.invokeMethod(method, handler);
/*      */     }
/*      */     
/*      */     private void setMaxInactiveInterval(Object manager, int interval) {
/* 1029 */       Method method = ReflectionUtils.findMethod(manager.getClass(), "setMaxInactiveInterval", new Class[] { Integer.TYPE });
/*      */       
/* 1031 */       ReflectionUtils.invokeMethod(method, manager, new Object[] { Integer.valueOf(interval) });
/*      */     }
/*      */     
/*      */     private void configurePersistSession(Object manager, JettyEmbeddedServletContainerFactory.SessionDirectory sessionDirectory)
/*      */     {
/*      */       try {
/* 1037 */         setStoreDirectory(manager, sessionDirectory.get());
/*      */       }
/*      */       catch (IOException ex) {
/* 1040 */         throw new IllegalStateException(ex);
/*      */       }
/*      */     }
/*      */     
/*      */     private void setStoreDirectory(Object manager, File file) throws IOException {
/* 1045 */       Method method = ReflectionUtils.findMethod(manager.getClass(), "setStoreDirectory", new Class[] { File.class });
/*      */       
/* 1047 */       ReflectionUtils.invokeMethod(method, manager, new Object[] { file });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class Jetty94SessionConfigurer
/*      */     implements JettyEmbeddedServletContainerFactory.SessionConfigurer
/*      */   {
/*      */     public void configure(WebAppContext context, int timeout, boolean persist, JettyEmbeddedServletContainerFactory.SessionDirectory sessionDirectory)
/*      */     {
/* 1060 */       SessionHandler handler = context.getSessionHandler();
/* 1061 */       handler.setMaxInactiveInterval(timeout > 0 ? timeout : -1);
/* 1062 */       if (persist) {
/* 1063 */         DefaultSessionCache cache = new DefaultSessionCache(handler);
/* 1064 */         FileSessionDataStore store = new FileSessionDataStore();
/* 1065 */         store.setStoreDir(sessionDirectory.get());
/* 1066 */         cache.setSessionDataStore(store);
/* 1067 */         handler.setSessionCache(cache);
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\jetty\JettyEmbeddedServletContainerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */