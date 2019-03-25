/*     */ package org.springframework.boot.context.embedded;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.net.InetAddress;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.springframework.boot.web.servlet.ErrorPage;
/*     */ import org.springframework.boot.web.servlet.ServletContextInitializer;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public abstract class AbstractConfigurableEmbeddedServletContainer
/*     */   implements ConfigurableEmbeddedServletContainer
/*     */ {
/*  53 */   private static final int DEFAULT_SESSION_TIMEOUT = (int)TimeUnit.MINUTES.toSeconds(30L);
/*     */   
/*  55 */   private String contextPath = "";
/*     */   
/*     */   private String displayName;
/*     */   
/*  59 */   private boolean registerDefaultServlet = true;
/*     */   
/*  61 */   private int port = 8080;
/*     */   
/*  63 */   private List<ServletContextInitializer> initializers = new ArrayList();
/*     */   
/*     */   private File documentRoot;
/*     */   
/*  67 */   private Set<ErrorPage> errorPages = new LinkedHashSet();
/*     */   
/*  69 */   private MimeMappings mimeMappings = new MimeMappings(MimeMappings.DEFAULT);
/*     */   
/*     */   private InetAddress address;
/*     */   
/*  73 */   private int sessionTimeout = DEFAULT_SESSION_TIMEOUT;
/*     */   
/*     */   private boolean persistSession;
/*     */   
/*     */   private File sessionStoreDir;
/*     */   
/*     */   private Ssl ssl;
/*     */   
/*     */   private SslStoreProvider sslStoreProvider;
/*     */   
/*  83 */   private JspServlet jspServlet = new JspServlet();
/*     */   
/*     */   private Compression compression;
/*     */   
/*     */   private String serverHeader;
/*     */   
/*  89 */   private Map<Locale, Charset> localeCharsetMappings = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractConfigurableEmbeddedServletContainer() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractConfigurableEmbeddedServletContainer(int port)
/*     */   {
/* 103 */     this.port = port;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractConfigurableEmbeddedServletContainer(String contextPath, int port)
/*     */   {
/* 113 */     checkContextPath(contextPath);
/* 114 */     this.contextPath = contextPath;
/* 115 */     this.port = port;
/*     */   }
/*     */   
/*     */   public void setContextPath(String contextPath)
/*     */   {
/* 120 */     checkContextPath(contextPath);
/* 121 */     this.contextPath = contextPath;
/*     */   }
/*     */   
/*     */   private void checkContextPath(String contextPath) {
/* 125 */     Assert.notNull(contextPath, "ContextPath must not be null");
/* 126 */     if (contextPath.length() > 0) {
/* 127 */       if ("/".equals(contextPath)) {
/* 128 */         throw new IllegalArgumentException("Root ContextPath must be specified using an empty string");
/*     */       }
/*     */       
/* 131 */       if ((!contextPath.startsWith("/")) || (contextPath.endsWith("/"))) {
/* 132 */         throw new IllegalArgumentException("ContextPath must start with '/' and not end with '/'");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getContextPath()
/*     */   {
/* 144 */     return this.contextPath;
/*     */   }
/*     */   
/*     */   public void setDisplayName(String displayName)
/*     */   {
/* 149 */     this.displayName = displayName;
/*     */   }
/*     */   
/*     */   public String getDisplayName() {
/* 153 */     return this.displayName;
/*     */   }
/*     */   
/*     */   public void setPort(int port)
/*     */   {
/* 158 */     this.port = port;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPort()
/*     */   {
/* 166 */     return this.port;
/*     */   }
/*     */   
/*     */   public void setAddress(InetAddress address)
/*     */   {
/* 171 */     this.address = address;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public InetAddress getAddress()
/*     */   {
/* 179 */     return this.address;
/*     */   }
/*     */   
/*     */   public void setSessionTimeout(int sessionTimeout)
/*     */   {
/* 184 */     this.sessionTimeout = sessionTimeout;
/*     */   }
/*     */   
/*     */   public void setSessionTimeout(int sessionTimeout, TimeUnit timeUnit)
/*     */   {
/* 189 */     Assert.notNull(timeUnit, "TimeUnit must not be null");
/* 190 */     this.sessionTimeout = ((int)timeUnit.toSeconds(sessionTimeout));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSessionTimeout()
/*     */   {
/* 198 */     return this.sessionTimeout;
/*     */   }
/*     */   
/*     */   public void setPersistSession(boolean persistSession)
/*     */   {
/* 203 */     this.persistSession = persistSession;
/*     */   }
/*     */   
/*     */   public boolean isPersistSession() {
/* 207 */     return this.persistSession;
/*     */   }
/*     */   
/*     */   public void setSessionStoreDir(File sessionStoreDir)
/*     */   {
/* 212 */     this.sessionStoreDir = sessionStoreDir;
/*     */   }
/*     */   
/*     */   public File getSessionStoreDir() {
/* 216 */     return this.sessionStoreDir;
/*     */   }
/*     */   
/*     */   public void setInitializers(List<? extends ServletContextInitializer> initializers)
/*     */   {
/* 221 */     Assert.notNull(initializers, "Initializers must not be null");
/* 222 */     this.initializers = new ArrayList(initializers);
/*     */   }
/*     */   
/*     */   public void addInitializers(ServletContextInitializer... initializers)
/*     */   {
/* 227 */     Assert.notNull(initializers, "Initializers must not be null");
/* 228 */     this.initializers.addAll(Arrays.asList(initializers));
/*     */   }
/*     */   
/*     */   public void setDocumentRoot(File documentRoot)
/*     */   {
/* 233 */     this.documentRoot = documentRoot;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public File getDocumentRoot()
/*     */   {
/* 242 */     return this.documentRoot;
/*     */   }
/*     */   
/*     */   public void setErrorPages(Set<? extends ErrorPage> errorPages)
/*     */   {
/* 247 */     Assert.notNull(errorPages, "ErrorPages must not be null");
/* 248 */     this.errorPages = new LinkedHashSet(errorPages);
/*     */   }
/*     */   
/*     */   public void addErrorPages(ErrorPage... errorPages)
/*     */   {
/* 253 */     Assert.notNull(errorPages, "ErrorPages must not be null");
/* 254 */     this.errorPages.addAll(Arrays.asList(errorPages));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<ErrorPage> getErrorPages()
/*     */   {
/* 263 */     return this.errorPages;
/*     */   }
/*     */   
/*     */   public void setMimeMappings(MimeMappings mimeMappings)
/*     */   {
/* 268 */     this.mimeMappings = new MimeMappings(mimeMappings);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MimeMappings getMimeMappings()
/*     */   {
/* 276 */     return this.mimeMappings;
/*     */   }
/*     */   
/*     */   public void setRegisterDefaultServlet(boolean registerDefaultServlet)
/*     */   {
/* 281 */     this.registerDefaultServlet = registerDefaultServlet;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isRegisterDefaultServlet()
/*     */   {
/* 289 */     return this.registerDefaultServlet;
/*     */   }
/*     */   
/*     */   public void setSsl(Ssl ssl)
/*     */   {
/* 294 */     this.ssl = ssl;
/*     */   }
/*     */   
/*     */   public Ssl getSsl() {
/* 298 */     return this.ssl;
/*     */   }
/*     */   
/*     */   public void setSslStoreProvider(SslStoreProvider sslStoreProvider)
/*     */   {
/* 303 */     this.sslStoreProvider = sslStoreProvider;
/*     */   }
/*     */   
/*     */   public SslStoreProvider getSslStoreProvider() {
/* 307 */     return this.sslStoreProvider;
/*     */   }
/*     */   
/*     */   public void setJspServlet(JspServlet jspServlet)
/*     */   {
/* 312 */     this.jspServlet = jspServlet;
/*     */   }
/*     */   
/*     */   public JspServlet getJspServlet() {
/* 316 */     return this.jspServlet;
/*     */   }
/*     */   
/*     */   public Compression getCompression() {
/* 320 */     return this.compression;
/*     */   }
/*     */   
/*     */   public void setCompression(Compression compression)
/*     */   {
/* 325 */     this.compression = compression;
/*     */   }
/*     */   
/*     */   public String getServerHeader() {
/* 329 */     return this.serverHeader;
/*     */   }
/*     */   
/*     */   public void setServerHeader(String serverHeader)
/*     */   {
/* 334 */     this.serverHeader = serverHeader;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<Locale, Charset> getLocaleCharsetMappings()
/*     */   {
/* 342 */     return this.localeCharsetMappings;
/*     */   }
/*     */   
/*     */   public void setLocaleCharsetMappings(Map<Locale, Charset> localeCharsetMappings)
/*     */   {
/* 347 */     Assert.notNull(localeCharsetMappings, "localeCharsetMappings must not be null");
/* 348 */     this.localeCharsetMappings = localeCharsetMappings;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final ServletContextInitializer[] mergeInitializers(ServletContextInitializer... initializers)
/*     */   {
/* 360 */     List<ServletContextInitializer> mergedInitializers = new ArrayList();
/* 361 */     mergedInitializers.addAll(Arrays.asList(initializers));
/* 362 */     mergedInitializers.addAll(this.initializers);
/* 363 */     return 
/* 364 */       (ServletContextInitializer[])mergedInitializers.toArray(new ServletContextInitializer[mergedInitializers.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean shouldRegisterJspServlet()
/*     */   {
/* 373 */     return (this.jspServlet != null) && (this.jspServlet.getRegistered()) && 
/* 374 */       (ClassUtils.isPresent(this.jspServlet.getClassName(), getClass().getClassLoader()));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\AbstractConfigurableEmbeddedServletContainer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */