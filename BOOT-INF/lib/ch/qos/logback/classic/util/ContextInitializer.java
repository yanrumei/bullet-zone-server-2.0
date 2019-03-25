/*     */ package ch.qos.logback.classic.util;
/*     */ 
/*     */ import ch.qos.logback.classic.BasicConfigurator;
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.gaffer.GafferUtil;
/*     */ import ch.qos.logback.classic.joran.JoranConfigurator;
/*     */ import ch.qos.logback.classic.spi.Configurator;
/*     */ import ch.qos.logback.core.LogbackException;
/*     */ import ch.qos.logback.core.joran.spi.JoranException;
/*     */ import ch.qos.logback.core.status.ErrorStatus;
/*     */ import ch.qos.logback.core.status.InfoStatus;
/*     */ import ch.qos.logback.core.status.StatusManager;
/*     */ import ch.qos.logback.core.status.WarnStatus;
/*     */ import ch.qos.logback.core.util.Loader;
/*     */ import ch.qos.logback.core.util.OptionHelper;
/*     */ import ch.qos.logback.core.util.StatusListenerConfigHelper;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.Set;
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
/*     */ public class ContextInitializer
/*     */ {
/*     */   public static final String GROOVY_AUTOCONFIG_FILE = "logback.groovy";
/*     */   public static final String AUTOCONFIG_FILE = "logback.xml";
/*     */   public static final String TEST_AUTOCONFIG_FILE = "logback-test.xml";
/*     */   public static final String CONFIG_FILE_PROPERTY = "logback.configurationFile";
/*     */   final LoggerContext loggerContext;
/*     */   
/*     */   public ContextInitializer(LoggerContext loggerContext)
/*     */   {
/*  55 */     this.loggerContext = loggerContext;
/*     */   }
/*     */   
/*     */   public void configureByResource(URL url) throws JoranException {
/*  59 */     if (url == null) {
/*  60 */       throw new IllegalArgumentException("URL argument cannot be null");
/*     */     }
/*  62 */     String urlString = url.toString();
/*  63 */     if (urlString.endsWith("groovy")) {
/*  64 */       if (EnvUtil.isGroovyAvailable())
/*     */       {
/*     */ 
/*  67 */         GafferUtil.runGafferConfiguratorOn(this.loggerContext, this, url);
/*     */       } else {
/*  69 */         StatusManager sm = this.loggerContext.getStatusManager();
/*  70 */         sm.add(new ErrorStatus("Groovy classes are not available on the class path. ABORTING INITIALIZATION.", this.loggerContext));
/*     */       }
/*  72 */     } else if (urlString.endsWith("xml")) {
/*  73 */       JoranConfigurator configurator = new JoranConfigurator();
/*  74 */       configurator.setContext(this.loggerContext);
/*  75 */       configurator.doConfigure(url);
/*     */     } else {
/*  77 */       throw new LogbackException("Unexpected filename extension of file [" + url.toString() + "]. Should be either .groovy or .xml");
/*     */     }
/*     */   }
/*     */   
/*     */   void joranConfigureByResource(URL url) throws JoranException {
/*  82 */     JoranConfigurator configurator = new JoranConfigurator();
/*  83 */     configurator.setContext(this.loggerContext);
/*  84 */     configurator.doConfigure(url);
/*     */   }
/*     */   
/*     */   private URL findConfigFileURLFromSystemProperties(ClassLoader classLoader, boolean updateStatus) {
/*  88 */     String logbackConfigFile = OptionHelper.getSystemProperty("logback.configurationFile");
/*  89 */     if (logbackConfigFile != null) {
/*  90 */       URL result = null;
/*     */       try {
/*  92 */         result = new URL(logbackConfigFile);
/*  93 */         return result;
/*     */       }
/*     */       catch (MalformedURLException localMalformedURLException1) {
/*     */         URL localURL1;
/*  97 */         result = Loader.getResource(logbackConfigFile, classLoader);
/*  98 */         if (result != null) {
/*  99 */           return result;
/*     */         }
/* 101 */         File f = new File(logbackConfigFile);
/* 102 */         if ((f.exists()) && (f.isFile())) {
/*     */           try {
/* 104 */             result = f.toURI().toURL();
/* 105 */             return result;
/*     */           }
/*     */           catch (MalformedURLException localMalformedURLException2) {}
/*     */         }
/*     */       } finally {
/* 110 */         if (updateStatus) {
/* 111 */           statusOnResourceSearch(logbackConfigFile, classLoader, result);
/*     */         }
/*     */       }
/*     */     }
/* 115 */     return null;
/*     */   }
/*     */   
/*     */   public URL findURLOfDefaultConfigurationFile(boolean updateStatus) {
/* 119 */     ClassLoader myClassLoader = Loader.getClassLoaderOfObject(this);
/* 120 */     URL url = findConfigFileURLFromSystemProperties(myClassLoader, updateStatus);
/* 121 */     if (url != null) {
/* 122 */       return url;
/*     */     }
/*     */     
/* 125 */     url = getResource("logback.groovy", myClassLoader, updateStatus);
/* 126 */     if (url != null) {
/* 127 */       return url;
/*     */     }
/*     */     
/* 130 */     url = getResource("logback-test.xml", myClassLoader, updateStatus);
/* 131 */     if (url != null) {
/* 132 */       return url;
/*     */     }
/*     */     
/* 135 */     return getResource("logback.xml", myClassLoader, updateStatus);
/*     */   }
/*     */   
/*     */   private URL getResource(String filename, ClassLoader myClassLoader, boolean updateStatus) {
/* 139 */     URL url = Loader.getResource(filename, myClassLoader);
/* 140 */     if (updateStatus) {
/* 141 */       statusOnResourceSearch(filename, myClassLoader, url);
/*     */     }
/* 143 */     return url;
/*     */   }
/*     */   
/*     */   public void autoConfig() throws JoranException {
/* 147 */     StatusListenerConfigHelper.installIfAsked(this.loggerContext);
/* 148 */     URL url = findURLOfDefaultConfigurationFile(true);
/* 149 */     if (url != null) {
/* 150 */       configureByResource(url);
/*     */     } else {
/* 152 */       Configurator c = (Configurator)EnvUtil.loadFromServiceLoader(Configurator.class);
/* 153 */       if (c != null) {
/*     */         try {
/* 155 */           c.setContext(this.loggerContext);
/* 156 */           c.configure(this.loggerContext);
/*     */         } catch (Exception e) {
/* 158 */           throw new LogbackException(String.format("Failed to initialize Configurator: %s using ServiceLoader", new Object[] { c != null ? c.getClass()
/* 159 */             .getCanonicalName() : "null" }), e);
/*     */         }
/*     */       } else {
/* 162 */         BasicConfigurator basicConfigurator = new BasicConfigurator();
/* 163 */         basicConfigurator.setContext(this.loggerContext);
/* 164 */         basicConfigurator.configure(this.loggerContext);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void statusOnResourceSearch(String resourceName, ClassLoader classLoader, URL url) {
/* 170 */     StatusManager sm = this.loggerContext.getStatusManager();
/* 171 */     if (url == null) {
/* 172 */       sm.add(new InfoStatus("Could NOT find resource [" + resourceName + "]", this.loggerContext));
/*     */     } else {
/* 174 */       sm.add(new InfoStatus("Found resource [" + resourceName + "] at [" + url.toString() + "]", this.loggerContext));
/* 175 */       multiplicityWarning(resourceName, classLoader);
/*     */     }
/*     */   }
/*     */   
/*     */   private void multiplicityWarning(String resourceName, ClassLoader classLoader) {
/* 180 */     Set<URL> urlSet = null;
/* 181 */     StatusManager sm = this.loggerContext.getStatusManager();
/*     */     try {
/* 183 */       urlSet = Loader.getResources(resourceName, classLoader);
/*     */     } catch (IOException e) {
/* 185 */       sm.add(new ErrorStatus("Failed to get url list for resource [" + resourceName + "]", this.loggerContext, e));
/*     */     }
/* 187 */     if ((urlSet != null) && (urlSet.size() > 1)) {
/* 188 */       sm.add(new WarnStatus("Resource [" + resourceName + "] occurs multiple times on the classpath.", this.loggerContext));
/* 189 */       for (URL url : urlSet) {
/* 190 */         sm.add(new WarnStatus("Resource [" + resourceName + "] occurs at [" + url.toString() + "]", this.loggerContext));
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classi\\util\ContextInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */