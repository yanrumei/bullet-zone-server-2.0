/*     */ package org.springframework.boot.context.embedded.undertow;
/*     */ 
/*     */ import io.undertow.Undertow;
/*     */ import io.undertow.Undertow.Builder;
/*     */ import io.undertow.server.HandlerWrapper;
/*     */ import io.undertow.server.HttpHandler;
/*     */ import io.undertow.server.handlers.accesslog.AccessLogHandler;
/*     */ import io.undertow.server.handlers.accesslog.AccessLogReceiver;
/*     */ import io.undertow.server.handlers.accesslog.DefaultAccessLogReceiver;
/*     */ import io.undertow.server.handlers.resource.FileResourceManager;
/*     */ import io.undertow.server.handlers.resource.Resource;
/*     */ import io.undertow.server.handlers.resource.ResourceChangeListener;
/*     */ import io.undertow.server.handlers.resource.ResourceManager;
/*     */ import io.undertow.server.handlers.resource.URLResource;
/*     */ import io.undertow.server.session.SessionManager;
/*     */ import io.undertow.servlet.Servlets;
/*     */ import io.undertow.servlet.api.Deployment;
/*     */ import io.undertow.servlet.api.DeploymentInfo;
/*     */ import io.undertow.servlet.api.DeploymentManager;
/*     */ import io.undertow.servlet.api.MimeMapping;
/*     */ import io.undertow.servlet.api.ServletContainer;
/*     */ import io.undertow.servlet.api.ServletContainerInitializerInfo;
/*     */ import io.undertow.servlet.api.ServletStackTraces;
/*     */ import io.undertow.servlet.handlers.DefaultServlet;
/*     */ import io.undertow.servlet.util.ImmediateInstanceFactory;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.Socket;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.Charset;
/*     */ import java.security.KeyManagementException;
/*     */ import java.security.KeyStore;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.Principal;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.net.ssl.KeyManager;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.TrustManagerFactory;
/*     */ import javax.net.ssl.X509ExtendedKeyManager;
/*     */ import javax.servlet.ServletContainerInitializer;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import org.springframework.boot.context.embedded.AbstractEmbeddedServletContainerFactory;
/*     */ import org.springframework.boot.context.embedded.EmbeddedServletContainer;
/*     */ import org.springframework.boot.context.embedded.JspServlet;
/*     */ import org.springframework.boot.context.embedded.MimeMappings.Mapping;
/*     */ import org.springframework.boot.context.embedded.Ssl;
/*     */ import org.springframework.boot.context.embedded.Ssl.ClientAuth;
/*     */ import org.springframework.boot.context.embedded.SslStoreProvider;
/*     */ import org.springframework.boot.web.servlet.ServletContextInitializer;
/*     */ import org.springframework.context.ResourceLoaderAware;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ResourceUtils;
/*     */ import org.xnio.OptionMap;
/*     */ import org.xnio.OptionMap.Builder;
/*     */ import org.xnio.Options;
/*     */ import org.xnio.Sequence;
/*     */ import org.xnio.SslClientAuthMode;
/*     */ import org.xnio.Xnio;
/*     */ import org.xnio.XnioWorker;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UndertowEmbeddedServletContainerFactory
/*     */   extends AbstractEmbeddedServletContainerFactory
/*     */   implements ResourceLoaderAware
/*     */ {
/* 109 */   private static final Set<Class<?>> NO_CLASSES = ;
/*     */   
/* 111 */   private List<UndertowBuilderCustomizer> builderCustomizers = new ArrayList();
/*     */   
/* 113 */   private List<UndertowDeploymentInfoCustomizer> deploymentInfoCustomizers = new ArrayList();
/*     */   
/*     */   private ResourceLoader resourceLoader;
/*     */   
/*     */   private Integer bufferSize;
/*     */   
/*     */   private Integer ioThreads;
/*     */   
/*     */   private Integer workerThreads;
/*     */   
/*     */   private Boolean directBuffers;
/*     */   
/*     */   private File accessLogDirectory;
/*     */   
/*     */   private String accessLogPattern;
/*     */   
/*     */   private String accessLogPrefix;
/*     */   
/*     */   private String accessLogSuffix;
/*     */   
/* 133 */   private boolean accessLogEnabled = false;
/*     */   
/* 135 */   private boolean accessLogRotate = true;
/*     */   
/*     */ 
/*     */   private boolean useForwardHeaders;
/*     */   
/*     */ 
/*     */ 
/*     */   public UndertowEmbeddedServletContainerFactory()
/*     */   {
/* 144 */     getJspServlet().setRegistered(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UndertowEmbeddedServletContainerFactory(int port)
/*     */   {
/* 153 */     super(port);
/* 154 */     getJspServlet().setRegistered(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UndertowEmbeddedServletContainerFactory(String contextPath, int port)
/*     */   {
/* 164 */     super(contextPath, port);
/* 165 */     getJspServlet().setRegistered(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBuilderCustomizers(Collection<? extends UndertowBuilderCustomizer> customizers)
/*     */   {
/* 175 */     Assert.notNull(customizers, "Customizers must not be null");
/* 176 */     this.builderCustomizers = new ArrayList(customizers);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<UndertowBuilderCustomizer> getBuilderCustomizers()
/*     */   {
/* 185 */     return this.builderCustomizers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addBuilderCustomizers(UndertowBuilderCustomizer... customizers)
/*     */   {
/* 194 */     Assert.notNull(customizers, "Customizers must not be null");
/* 195 */     this.builderCustomizers.addAll(Arrays.asList(customizers));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDeploymentInfoCustomizers(Collection<? extends UndertowDeploymentInfoCustomizer> customizers)
/*     */   {
/* 206 */     Assert.notNull(customizers, "Customizers must not be null");
/* 207 */     this.deploymentInfoCustomizers = new ArrayList(customizers);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<UndertowDeploymentInfoCustomizer> getDeploymentInfoCustomizers()
/*     */   {
/* 217 */     return this.deploymentInfoCustomizers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addDeploymentInfoCustomizers(UndertowDeploymentInfoCustomizer... customizers)
/*     */   {
/* 227 */     Assert.notNull(customizers, "UndertowDeploymentInfoCustomizers must not be null");
/* 228 */     this.deploymentInfoCustomizers.addAll(Arrays.asList(customizers));
/*     */   }
/*     */   
/*     */ 
/*     */   public EmbeddedServletContainer getEmbeddedServletContainer(ServletContextInitializer... initializers)
/*     */   {
/* 234 */     DeploymentManager manager = createDeploymentManager(initializers);
/* 235 */     int port = getPort();
/* 236 */     Undertow.Builder builder = createBuilder(port);
/* 237 */     return getUndertowEmbeddedServletContainer(builder, manager, port);
/*     */   }
/*     */   
/*     */   private Undertow.Builder createBuilder(int port) {
/* 241 */     Undertow.Builder builder = Undertow.builder();
/* 242 */     if (this.bufferSize != null) {
/* 243 */       builder.setBufferSize(this.bufferSize.intValue());
/*     */     }
/* 245 */     if (this.ioThreads != null) {
/* 246 */       builder.setIoThreads(this.ioThreads.intValue());
/*     */     }
/* 248 */     if (this.workerThreads != null) {
/* 249 */       builder.setWorkerThreads(this.workerThreads.intValue());
/*     */     }
/* 251 */     if (this.directBuffers != null) {
/* 252 */       builder.setDirectBuffers(this.directBuffers.booleanValue());
/*     */     }
/* 254 */     if ((getSsl() != null) && (getSsl().isEnabled())) {
/* 255 */       configureSsl(getSsl(), port, builder);
/*     */     }
/*     */     else {
/* 258 */       builder.addHttpListener(port, getListenAddress());
/*     */     }
/* 260 */     for (UndertowBuilderCustomizer customizer : this.builderCustomizers) {
/* 261 */       customizer.customize(builder);
/*     */     }
/* 263 */     return builder;
/*     */   }
/*     */   
/*     */   private void configureSsl(Ssl ssl, int port, Undertow.Builder builder) {
/*     */     try {
/* 268 */       SSLContext sslContext = SSLContext.getInstance(ssl.getProtocol());
/* 269 */       sslContext.init(getKeyManagers(), getTrustManagers(), null);
/* 270 */       builder.addHttpsListener(port, getListenAddress(), sslContext);
/* 271 */       builder.setSocketOption(Options.SSL_CLIENT_AUTH_MODE, 
/* 272 */         getSslClientAuthMode(ssl));
/* 273 */       if (ssl.getEnabledProtocols() != null) {
/* 274 */         builder.setSocketOption(Options.SSL_ENABLED_PROTOCOLS, 
/* 275 */           Sequence.of(ssl.getEnabledProtocols()));
/*     */       }
/* 277 */       if (ssl.getCiphers() != null) {
/* 278 */         builder.setSocketOption(Options.SSL_ENABLED_CIPHER_SUITES, 
/* 279 */           Sequence.of(ssl.getCiphers()));
/*     */       }
/*     */     }
/*     */     catch (NoSuchAlgorithmException ex) {
/* 283 */       throw new IllegalStateException(ex);
/*     */     }
/*     */     catch (KeyManagementException ex) {
/* 286 */       throw new IllegalStateException(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private String getListenAddress() {
/* 291 */     if (getAddress() == null) {
/* 292 */       return "0.0.0.0";
/*     */     }
/* 294 */     return getAddress().getHostAddress();
/*     */   }
/*     */   
/*     */   private SslClientAuthMode getSslClientAuthMode(Ssl ssl) {
/* 298 */     if (ssl.getClientAuth() == Ssl.ClientAuth.NEED) {
/* 299 */       return SslClientAuthMode.REQUIRED;
/*     */     }
/* 301 */     if (ssl.getClientAuth() == Ssl.ClientAuth.WANT) {
/* 302 */       return SslClientAuthMode.REQUESTED;
/*     */     }
/* 304 */     return SslClientAuthMode.NOT_REQUESTED;
/*     */   }
/*     */   
/*     */   private KeyManager[] getKeyManagers() {
/*     */     try {
/* 309 */       KeyStore keyStore = getKeyStore();
/*     */       
/* 311 */       KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
/* 312 */       Ssl ssl = getSsl();
/*     */       
/* 314 */       char[] keyPassword = ssl.getKeyPassword() != null ? ssl.getKeyPassword().toCharArray() : null;
/* 315 */       if ((keyPassword == null) && (ssl.getKeyStorePassword() != null)) {
/* 316 */         keyPassword = ssl.getKeyStorePassword().toCharArray();
/*     */       }
/* 318 */       keyManagerFactory.init(keyStore, keyPassword);
/* 319 */       if (ssl.getKeyAlias() != null) {
/* 320 */         return getConfigurableAliasKeyManagers(ssl, keyManagerFactory
/* 321 */           .getKeyManagers());
/*     */       }
/* 323 */       return keyManagerFactory.getKeyManagers();
/*     */     }
/*     */     catch (Exception ex) {
/* 326 */       throw new IllegalStateException(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private KeyManager[] getConfigurableAliasKeyManagers(Ssl ssl, KeyManager[] keyManagers)
/*     */   {
/* 332 */     for (int i = 0; i < keyManagers.length; i++) {
/* 333 */       if ((keyManagers[i] instanceof X509ExtendedKeyManager))
/*     */       {
/* 335 */         keyManagers[i] = new ConfigurableAliasKeyManager((X509ExtendedKeyManager)keyManagers[i], ssl.getKeyAlias());
/*     */       }
/*     */     }
/* 338 */     return keyManagers;
/*     */   }
/*     */   
/*     */   private KeyStore getKeyStore() throws Exception {
/* 342 */     if (getSslStoreProvider() != null) {
/* 343 */       return getSslStoreProvider().getKeyStore();
/*     */     }
/* 345 */     Ssl ssl = getSsl();
/* 346 */     return loadKeyStore(ssl.getKeyStoreType(), ssl.getKeyStore(), ssl
/* 347 */       .getKeyStorePassword());
/*     */   }
/*     */   
/*     */   private TrustManager[] getTrustManagers() {
/*     */     try {
/* 352 */       KeyStore store = getTrustStore();
/*     */       
/* 354 */       TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
/* 355 */       trustManagerFactory.init(store);
/* 356 */       return trustManagerFactory.getTrustManagers();
/*     */     }
/*     */     catch (Exception ex) {
/* 359 */       throw new IllegalStateException(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private KeyStore getTrustStore() throws Exception {
/* 364 */     if (getSslStoreProvider() != null) {
/* 365 */       return getSslStoreProvider().getTrustStore();
/*     */     }
/* 367 */     Ssl ssl = getSsl();
/* 368 */     return loadKeyStore(ssl.getTrustStoreType(), ssl.getTrustStore(), ssl
/* 369 */       .getTrustStorePassword());
/*     */   }
/*     */   
/*     */   private KeyStore loadKeyStore(String type, String resource, String password) throws Exception
/*     */   {
/* 374 */     type = type == null ? "JKS" : type;
/* 375 */     if (resource == null) {
/* 376 */       return null;
/*     */     }
/* 378 */     KeyStore store = KeyStore.getInstance(type);
/* 379 */     URL url = ResourceUtils.getURL(resource);
/* 380 */     store.load(url.openStream(), password == null ? null : password.toCharArray());
/* 381 */     return store;
/*     */   }
/*     */   
/*     */   private DeploymentManager createDeploymentManager(ServletContextInitializer... initializers)
/*     */   {
/* 386 */     DeploymentInfo deployment = Servlets.deployment();
/* 387 */     registerServletContainerInitializerToDriveServletContextInitializers(deployment, initializers);
/*     */     
/* 389 */     deployment.setClassLoader(getServletClassLoader());
/* 390 */     deployment.setContextPath(getContextPath());
/* 391 */     deployment.setDisplayName(getDisplayName());
/* 392 */     deployment.setDeploymentName("spring-boot");
/* 393 */     if (isRegisterDefaultServlet()) {
/* 394 */       deployment.addServlet(Servlets.servlet("default", DefaultServlet.class));
/*     */     }
/* 396 */     configureErrorPages(deployment);
/* 397 */     deployment.setServletStackTraces(ServletStackTraces.NONE);
/* 398 */     deployment.setResourceManager(getDocumentRootResourceManager());
/* 399 */     configureMimeMappings(deployment);
/* 400 */     for (UndertowDeploymentInfoCustomizer customizer : this.deploymentInfoCustomizers) {
/* 401 */       customizer.customize(deployment);
/*     */     }
/* 403 */     if (isAccessLogEnabled()) {
/* 404 */       configureAccessLog(deployment);
/*     */     }
/* 406 */     if (isPersistSession()) {
/* 407 */       File dir = getValidSessionStoreDir();
/* 408 */       deployment.setSessionPersistenceManager(new FileSessionPersistence(dir));
/*     */     }
/* 410 */     addLocaleMappings(deployment);
/* 411 */     DeploymentManager manager = Servlets.newContainer().addDeployment(deployment);
/* 412 */     manager.deploy();
/* 413 */     SessionManager sessionManager = manager.getDeployment().getSessionManager();
/* 414 */     int sessionTimeout = getSessionTimeout() > 0 ? getSessionTimeout() : -1;
/* 415 */     sessionManager.setDefaultSessionTimeout(sessionTimeout);
/* 416 */     return manager;
/*     */   }
/*     */   
/*     */   private void configureAccessLog(DeploymentInfo deploymentInfo) {
/* 420 */     deploymentInfo.addInitialHandlerChainWrapper(new HandlerWrapper()
/*     */     {
/*     */       public HttpHandler wrap(HttpHandler handler)
/*     */       {
/* 424 */         return UndertowEmbeddedServletContainerFactory.this.createAccessLogHandler(handler);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   private AccessLogHandler createAccessLogHandler(HttpHandler handler)
/*     */   {
/*     */     try {
/* 432 */       createAccessLogDirectoryIfNecessary();
/* 433 */       String prefix = this.accessLogPrefix != null ? this.accessLogPrefix : "access_log.";
/*     */       
/*     */ 
/* 436 */       AccessLogReceiver accessLogReceiver = new DefaultAccessLogReceiver(createWorker(), this.accessLogDirectory, prefix, this.accessLogSuffix, this.accessLogRotate);
/*     */       
/* 438 */       String formatString = this.accessLogPattern != null ? this.accessLogPattern : "common";
/*     */       
/* 440 */       return new AccessLogHandler(handler, accessLogReceiver, formatString, Undertow.class
/* 441 */         .getClassLoader());
/*     */     }
/*     */     catch (IOException ex) {
/* 444 */       throw new IllegalStateException("Failed to create AccessLogHandler", ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private void createAccessLogDirectoryIfNecessary() {
/* 449 */     Assert.state(this.accessLogDirectory != null, "Access log directory is not set");
/* 450 */     if ((!this.accessLogDirectory.isDirectory()) && (!this.accessLogDirectory.mkdirs())) {
/* 451 */       throw new IllegalStateException("Failed to create access log directory '" + this.accessLogDirectory + "'");
/*     */     }
/*     */   }
/*     */   
/*     */   private XnioWorker createWorker() throws IOException
/*     */   {
/* 457 */     Xnio xnio = Xnio.getInstance(Undertow.class.getClassLoader());
/* 458 */     return xnio.createWorker(
/* 459 */       OptionMap.builder().set(Options.THREAD_DAEMON, true).getMap());
/*     */   }
/*     */   
/*     */   private void addLocaleMappings(DeploymentInfo deployment) {
/* 463 */     for (Map.Entry<Locale, Charset> entry : getLocaleCharsetMappings().entrySet()) {
/* 464 */       Locale locale = (Locale)entry.getKey();
/* 465 */       Charset charset = (Charset)entry.getValue();
/* 466 */       deployment.addLocaleCharsetMapping(locale.toString(), charset.toString());
/*     */     }
/*     */   }
/*     */   
/*     */   private void registerServletContainerInitializerToDriveServletContextInitializers(DeploymentInfo deployment, ServletContextInitializer... initializers)
/*     */   {
/* 472 */     ServletContextInitializer[] mergedInitializers = mergeInitializers(initializers);
/* 473 */     Initializer initializer = new Initializer(mergedInitializers);
/* 474 */     deployment.addServletContainerInitalizer(new ServletContainerInitializerInfo(Initializer.class, new ImmediateInstanceFactory(initializer), NO_CLASSES));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private ClassLoader getServletClassLoader()
/*     */   {
/* 481 */     if (this.resourceLoader != null) {
/* 482 */       return this.resourceLoader.getClassLoader();
/*     */     }
/* 484 */     return getClass().getClassLoader();
/*     */   }
/*     */   
/*     */   private ResourceManager getDocumentRootResourceManager() {
/* 488 */     File root = getCanonicalDocumentRoot();
/* 489 */     List<URL> metaInfResourceUrls = getUrlsOfJarsWithMetaInfResources();
/* 490 */     List<URL> resourceJarUrls = new ArrayList();
/* 491 */     List<ResourceManager> resourceManagers = new ArrayList();
/* 492 */     ResourceManager rootResourceManager = root.isDirectory() ? new FileResourceManager(root, 0L) : new JarResourceManager(root);
/*     */     
/* 494 */     resourceManagers.add(rootResourceManager);
/* 495 */     for (URL url : metaInfResourceUrls) {
/* 496 */       if ("file".equals(url.getProtocol())) {
/* 497 */         File file = new File(url.getFile());
/* 498 */         if (file.isFile()) {
/*     */           try {
/* 500 */             resourceJarUrls.add(new URL("jar:" + url + "!/"));
/*     */           }
/*     */           catch (MalformedURLException ex) {
/* 503 */             throw new RuntimeException(ex);
/*     */           }
/*     */           
/*     */         } else {
/* 507 */           resourceManagers.add(new FileResourceManager(new File(file, "META-INF/resources"), 0L));
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 512 */         resourceJarUrls.add(url);
/*     */       }
/*     */     }
/* 515 */     resourceManagers.add(new MetaInfResourcesResourceManager(resourceJarUrls, null));
/* 516 */     return new CompositeResourceManager(
/* 517 */       (ResourceManager[])resourceManagers.toArray(new ResourceManager[resourceManagers.size()]));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private File getCanonicalDocumentRoot()
/*     */   {
/*     */     try
/*     */     {
/* 528 */       File root = getValidDocumentRoot();
/* 529 */       root = root != null ? root : createTempDir("undertow-docbase");
/* 530 */       return root.getCanonicalFile();
/*     */     }
/*     */     catch (IOException e) {
/* 533 */       throw new IllegalStateException("Cannot get canonical document root", e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void configureErrorPages(DeploymentInfo servletBuilder) {
/* 538 */     for (org.springframework.boot.web.servlet.ErrorPage errorPage : getErrorPages()) {
/* 539 */       servletBuilder.addErrorPage(getUndertowErrorPage(errorPage));
/*     */     }
/*     */   }
/*     */   
/*     */   private io.undertow.servlet.api.ErrorPage getUndertowErrorPage(org.springframework.boot.web.servlet.ErrorPage errorPage) {
/* 544 */     if (errorPage.getStatus() != null) {
/* 545 */       return new io.undertow.servlet.api.ErrorPage(errorPage.getPath(), errorPage
/* 546 */         .getStatusCode());
/*     */     }
/* 548 */     if (errorPage.getException() != null) {
/* 549 */       return new io.undertow.servlet.api.ErrorPage(errorPage.getPath(), errorPage
/* 550 */         .getException());
/*     */     }
/* 552 */     return new io.undertow.servlet.api.ErrorPage(errorPage.getPath());
/*     */   }
/*     */   
/*     */   private void configureMimeMappings(DeploymentInfo servletBuilder) {
/* 556 */     for (MimeMappings.Mapping mimeMapping : getMimeMappings()) {
/* 557 */       servletBuilder.addMimeMapping(new MimeMapping(mimeMapping.getExtension(), mimeMapping
/* 558 */         .getMimeType()));
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
/*     */ 
/*     */   protected UndertowEmbeddedServletContainer getUndertowEmbeddedServletContainer(Undertow.Builder builder, DeploymentManager manager, int port)
/*     */   {
/* 574 */     return new UndertowEmbeddedServletContainer(builder, manager, getContextPath(), 
/* 575 */       isUseForwardHeaders(), port >= 0, getCompression(), getServerHeader());
/*     */   }
/*     */   
/*     */   public void setResourceLoader(ResourceLoader resourceLoader)
/*     */   {
/* 580 */     this.resourceLoader = resourceLoader;
/*     */   }
/*     */   
/*     */   public void setBufferSize(Integer bufferSize) {
/* 584 */     this.bufferSize = bufferSize;
/*     */   }
/*     */   
/*     */ 
/*     */   @Deprecated
/*     */   public void setBuffersPerRegion(Integer buffersPerRegion) {}
/*     */   
/*     */   public void setIoThreads(Integer ioThreads)
/*     */   {
/* 593 */     this.ioThreads = ioThreads;
/*     */   }
/*     */   
/*     */   public void setWorkerThreads(Integer workerThreads) {
/* 597 */     this.workerThreads = workerThreads;
/*     */   }
/*     */   
/*     */   public void setDirectBuffers(Boolean directBuffers) {
/* 601 */     this.directBuffers = directBuffers;
/*     */   }
/*     */   
/*     */   public void setAccessLogDirectory(File accessLogDirectory) {
/* 605 */     this.accessLogDirectory = accessLogDirectory;
/*     */   }
/*     */   
/*     */   public void setAccessLogPattern(String accessLogPattern) {
/* 609 */     this.accessLogPattern = accessLogPattern;
/*     */   }
/*     */   
/*     */   public String getAccessLogPrefix() {
/* 613 */     return this.accessLogPrefix;
/*     */   }
/*     */   
/*     */   public void setAccessLogPrefix(String accessLogPrefix) {
/* 617 */     this.accessLogPrefix = accessLogPrefix;
/*     */   }
/*     */   
/*     */   public void setAccessLogSuffix(String accessLogSuffix) {
/* 621 */     this.accessLogSuffix = accessLogSuffix;
/*     */   }
/*     */   
/*     */   public void setAccessLogEnabled(boolean accessLogEnabled) {
/* 625 */     this.accessLogEnabled = accessLogEnabled;
/*     */   }
/*     */   
/*     */   public boolean isAccessLogEnabled() {
/* 629 */     return this.accessLogEnabled;
/*     */   }
/*     */   
/*     */   public void setAccessLogRotate(boolean accessLogRotate) {
/* 633 */     this.accessLogRotate = accessLogRotate;
/*     */   }
/*     */   
/*     */   protected final boolean isUseForwardHeaders() {
/* 637 */     return this.useForwardHeaders;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUseForwardHeaders(boolean useForwardHeaders)
/*     */   {
/* 646 */     this.useForwardHeaders = useForwardHeaders;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static final class MetaInfResourcesResourceManager
/*     */     implements ResourceManager
/*     */   {
/*     */     private final List<URL> metaInfResourceJarUrls;
/*     */     
/*     */ 
/*     */     private MetaInfResourcesResourceManager(List<URL> metaInfResourceJarUrls)
/*     */     {
/* 659 */       this.metaInfResourceJarUrls = metaInfResourceJarUrls;
/*     */     }
/*     */     
/*     */     public void close()
/*     */       throws IOException
/*     */     {}
/*     */     
/*     */     public Resource getResource(String path)
/*     */     {
/* 668 */       for (URL url : this.metaInfResourceJarUrls) {
/* 669 */         URLResource resource = getMetaInfResource(url, path);
/* 670 */         if (resource != null) {
/* 671 */           return resource;
/*     */         }
/*     */       }
/* 674 */       return null;
/*     */     }
/*     */     
/*     */     public boolean isResourceChangeListenerSupported()
/*     */     {
/* 679 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     public void registerResourceChangeListener(ResourceChangeListener listener) {}
/*     */     
/*     */ 
/*     */     public void removeResourceChangeListener(ResourceChangeListener listener) {}
/*     */     
/*     */ 
/*     */     private URLResource getMetaInfResource(URL resourceJar, String path)
/*     */     {
/*     */       try
/*     */       {
/* 693 */         URL resourceUrl = new URL(resourceJar + "META-INF/resources" + path);
/* 694 */         URLResource resource = new URLResource(resourceUrl, path);
/* 695 */         if (resource.getContentLength().longValue() < 0L) {
/* 696 */           return null;
/*     */         }
/* 698 */         return resource;
/*     */       }
/*     */       catch (MalformedURLException ex) {}
/* 701 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class Initializer
/*     */     implements ServletContainerInitializer
/*     */   {
/*     */     private final ServletContextInitializer[] initializers;
/*     */     
/*     */ 
/*     */     Initializer(ServletContextInitializer[] initializers)
/*     */     {
/* 715 */       this.initializers = initializers;
/*     */     }
/*     */     
/*     */     public void onStartup(Set<Class<?>> classes, ServletContext servletContext)
/*     */       throws ServletException
/*     */     {
/* 721 */       for (ServletContextInitializer initializer : this.initializers) {
/* 722 */         initializer.onStartup(servletContext);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class ConfigurableAliasKeyManager
/*     */     extends X509ExtendedKeyManager
/*     */   {
/*     */     private final X509ExtendedKeyManager keyManager;
/*     */     
/*     */     private final String alias;
/*     */     
/*     */     ConfigurableAliasKeyManager(X509ExtendedKeyManager keyManager, String alias)
/*     */     {
/* 737 */       this.keyManager = keyManager;
/* 738 */       this.alias = alias;
/*     */     }
/*     */     
/*     */ 
/*     */     public String chooseEngineClientAlias(String[] strings, Principal[] principals, SSLEngine sslEngine)
/*     */     {
/* 744 */       return this.keyManager.chooseEngineClientAlias(strings, principals, sslEngine);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public String chooseEngineServerAlias(String s, Principal[] principals, SSLEngine sslEngine)
/*     */     {
/* 751 */       if (this.alias == null) {
/* 752 */         return this.keyManager.chooseEngineServerAlias(s, principals, sslEngine);
/*     */       }
/* 754 */       return this.alias;
/*     */     }
/*     */     
/*     */ 
/*     */     public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket)
/*     */     {
/* 760 */       return this.keyManager.chooseClientAlias(keyType, issuers, socket);
/*     */     }
/*     */     
/*     */ 
/*     */     public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket)
/*     */     {
/* 766 */       return this.keyManager.chooseServerAlias(keyType, issuers, socket);
/*     */     }
/*     */     
/*     */     public X509Certificate[] getCertificateChain(String alias)
/*     */     {
/* 771 */       return this.keyManager.getCertificateChain(alias);
/*     */     }
/*     */     
/*     */     public String[] getClientAliases(String keyType, Principal[] issuers)
/*     */     {
/* 776 */       return this.keyManager.getClientAliases(keyType, issuers);
/*     */     }
/*     */     
/*     */     public PrivateKey getPrivateKey(String alias)
/*     */     {
/* 781 */       return this.keyManager.getPrivateKey(alias);
/*     */     }
/*     */     
/*     */     public String[] getServerAliases(String keyType, Principal[] issuers)
/*     */     {
/* 786 */       return this.keyManager.getServerAliases(keyType, issuers);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedde\\undertow\UndertowEmbeddedServletContainerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */