/*      */ package org.apache.catalina.startup;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.net.URL;
/*      */ import java.net.URLConnection;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.EnumSet;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import javax.servlet.MultipartConfigElement;
/*      */ import javax.servlet.ServletContainerInitializer;
/*      */ import javax.servlet.ServletContext;
/*      */ import javax.servlet.SessionCookieConfig;
/*      */ import javax.servlet.annotation.HandlesTypes;
/*      */ import org.apache.catalina.Authenticator;
/*      */ import org.apache.catalina.Container;
/*      */ import org.apache.catalina.Context;
/*      */ import org.apache.catalina.Engine;
/*      */ import org.apache.catalina.Host;
/*      */ import org.apache.catalina.LifecycleEvent;
/*      */ import org.apache.catalina.LifecycleListener;
/*      */ import org.apache.catalina.LifecycleState;
/*      */ import org.apache.catalina.Loader;
/*      */ import org.apache.catalina.Pipeline;
/*      */ import org.apache.catalina.Server;
/*      */ import org.apache.catalina.Service;
/*      */ import org.apache.catalina.Valve;
/*      */ import org.apache.catalina.WebResource;
/*      */ import org.apache.catalina.WebResourceRoot;
/*      */ import org.apache.catalina.WebResourceRoot.ResourceSetType;
/*      */ import org.apache.catalina.Wrapper;
/*      */ import org.apache.catalina.core.StandardContext;
/*      */ import org.apache.catalina.core.StandardHost;
/*      */ import org.apache.catalina.deploy.NamingResourcesImpl;
/*      */ import org.apache.catalina.util.ContextName;
/*      */ import org.apache.catalina.util.Introspection;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.tomcat.Jar;
/*      */ import org.apache.tomcat.JarScanType;
/*      */ import org.apache.tomcat.JarScanner;
/*      */ import org.apache.tomcat.util.ExceptionUtils;
/*      */ import org.apache.tomcat.util.bcel.classfile.AnnotationElementValue;
/*      */ import org.apache.tomcat.util.bcel.classfile.AnnotationEntry;
/*      */ import org.apache.tomcat.util.bcel.classfile.ArrayElementValue;
/*      */ import org.apache.tomcat.util.bcel.classfile.ClassFormatException;
/*      */ import org.apache.tomcat.util.bcel.classfile.ClassParser;
/*      */ import org.apache.tomcat.util.bcel.classfile.ElementValue;
/*      */ import org.apache.tomcat.util.bcel.classfile.ElementValuePair;
/*      */ import org.apache.tomcat.util.bcel.classfile.JavaClass;
/*      */ import org.apache.tomcat.util.buf.UriUtil;
/*      */ import org.apache.tomcat.util.descriptor.InputSourceUtil;
/*      */ import org.apache.tomcat.util.descriptor.XmlErrorHandler;
/*      */ import org.apache.tomcat.util.descriptor.web.ContextEjb;
/*      */ import org.apache.tomcat.util.descriptor.web.ContextEnvironment;
/*      */ import org.apache.tomcat.util.descriptor.web.ContextLocalEjb;
/*      */ import org.apache.tomcat.util.descriptor.web.ContextResource;
/*      */ import org.apache.tomcat.util.descriptor.web.ContextResourceEnvRef;
/*      */ import org.apache.tomcat.util.descriptor.web.ContextService;
/*      */ import org.apache.tomcat.util.descriptor.web.ErrorPage;
/*      */ import org.apache.tomcat.util.descriptor.web.FilterDef;
/*      */ import org.apache.tomcat.util.descriptor.web.FilterMap;
/*      */ import org.apache.tomcat.util.descriptor.web.FragmentJarScannerCallback;
/*      */ import org.apache.tomcat.util.descriptor.web.JspPropertyGroup;
/*      */ import org.apache.tomcat.util.descriptor.web.LoginConfig;
/*      */ import org.apache.tomcat.util.descriptor.web.MessageDestinationRef;
/*      */ import org.apache.tomcat.util.descriptor.web.MultipartDef;
/*      */ import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
/*      */ import org.apache.tomcat.util.descriptor.web.SecurityRoleRef;
/*      */ import org.apache.tomcat.util.descriptor.web.ServletDef;
/*      */ import org.apache.tomcat.util.descriptor.web.SessionConfig;
/*      */ import org.apache.tomcat.util.descriptor.web.WebXml;
/*      */ import org.apache.tomcat.util.descriptor.web.WebXmlParser;
/*      */ import org.apache.tomcat.util.digester.Digester;
/*      */ import org.apache.tomcat.util.digester.RuleSet;
/*      */ import org.apache.tomcat.util.res.StringManager;
/*      */ import org.apache.tomcat.util.scan.JarFactory;
/*      */ import org.xml.sax.InputSource;
/*      */ import org.xml.sax.SAXParseException;
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
/*      */ public class ContextConfig
/*      */   implements LifecycleListener
/*      */ {
/*  120 */   private static final Log log = LogFactory.getLog(ContextConfig.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  127 */   protected static final StringManager sm = StringManager.getManager("org.apache.catalina.startup");
/*      */   
/*      */ 
/*  130 */   protected static final LoginConfig DUMMY_LOGIN_CONFIG = new LoginConfig("NONE", null, null, null);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final Properties authenticators;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static
/*      */   {
/*  143 */     Properties props = new Properties();
/*  144 */     try { InputStream is = ContextConfig.class.getClassLoader().getResourceAsStream("org/apache/catalina/startup/Authenticators.properties");Throwable localThrowable3 = null;
/*      */       try {
/*  146 */         if (is != null) {
/*  147 */           props.load(is);
/*      */         }
/*      */       }
/*      */       catch (Throwable localThrowable1)
/*      */       {
/*  144 */         localThrowable3 = localThrowable1;throw localThrowable1;
/*      */ 
/*      */       }
/*      */       finally
/*      */       {
/*  149 */         if (is != null) if (localThrowable3 != null) try { is.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else is.close();
/*  150 */       } } catch (IOException ioe) { props = null;
/*      */     }
/*  152 */     authenticators = props;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  158 */   protected static long deploymentCount = 0L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  164 */   protected static final Map<Host, DefaultWebXmlCacheEntry> hostWebXmlCache = new ConcurrentHashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  172 */   private static final Set<ServletContainerInitializer> EMPTY_SCI_SET = Collections.emptySet();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Map<String, Authenticator> customAuthenticators;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  185 */   protected Context context = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  191 */   protected String defaultWebXml = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  197 */   protected boolean ok = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  203 */   protected String originalDocBase = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  210 */   private File antiLockingDocBase = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  216 */   protected final Map<ServletContainerInitializer, Set<Class<?>>> initializerClassMap = new LinkedHashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  223 */   protected final Map<Class<?>, Set<ServletContainerInitializer>> typeInitializerMap = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  230 */   protected boolean handlesTypesAnnotations = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  236 */   protected boolean handlesTypesNonAnnotations = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDefaultWebXml()
/*      */   {
/*  248 */     if (this.defaultWebXml == null) {
/*  249 */       this.defaultWebXml = "conf/web.xml";
/*      */     }
/*  251 */     return this.defaultWebXml;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDefaultWebXml(String path)
/*      */   {
/*  262 */     this.defaultWebXml = path;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCustomAuthenticators(Map<String, Authenticator> customAuthenticators)
/*      */   {
/*  274 */     this.customAuthenticators = customAuthenticators;
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
/*      */   public void lifecycleEvent(LifecycleEvent event)
/*      */   {
/*      */     try
/*      */     {
/*  291 */       this.context = ((Context)event.getLifecycle());
/*      */     } catch (ClassCastException e) {
/*  293 */       log.error(sm.getString("contextConfig.cce", new Object[] { event.getLifecycle() }), e);
/*  294 */       return;
/*      */     }
/*      */     
/*      */ 
/*  298 */     if (event.getType().equals("configure_start")) {
/*  299 */       configureStart();
/*  300 */     } else if (event.getType().equals("before_start")) {
/*  301 */       beforeStart();
/*  302 */     } else if (event.getType().equals("after_start"))
/*      */     {
/*  304 */       if (this.originalDocBase != null) {
/*  305 */         this.context.setDocBase(this.originalDocBase);
/*      */       }
/*  307 */     } else if (event.getType().equals("configure_stop")) {
/*  308 */       configureStop();
/*  309 */     } else if (event.getType().equals("after_init")) {
/*  310 */       init();
/*  311 */     } else if (event.getType().equals("after_destroy")) {
/*  312 */       destroy();
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
/*      */   protected void applicationAnnotationsConfig()
/*      */   {
/*  326 */     long t1 = System.currentTimeMillis();
/*      */     
/*  328 */     WebAnnotationSet.loadApplicationAnnotations(this.context);
/*      */     
/*  330 */     long t2 = System.currentTimeMillis();
/*  331 */     if ((this.context instanceof StandardContext)) {
/*  332 */       ((StandardContext)this.context).setStartupTime(t2 - t1 + ((StandardContext)this.context)
/*  333 */         .getStartupTime());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void authenticatorConfig()
/*      */   {
/*  344 */     LoginConfig loginConfig = this.context.getLoginConfig();
/*      */     
/*  346 */     SecurityConstraint[] constraints = this.context.findConstraints();
/*  347 */     if ((this.context.getIgnoreAnnotations()) && ((constraints == null) || (constraints.length == 0)))
/*      */     {
/*  349 */       if (!this.context.getPreemptiveAuthentication())
/*  350 */         return;
/*      */     }
/*  352 */     if (loginConfig == null)
/*      */     {
/*      */ 
/*      */ 
/*  356 */       loginConfig = DUMMY_LOGIN_CONFIG;
/*  357 */       this.context.setLoginConfig(loginConfig);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  362 */     if (this.context.getAuthenticator() != null) {
/*  363 */       return;
/*      */     }
/*      */     
/*      */ 
/*  367 */     if (this.context.getRealm() == null) {
/*  368 */       log.error(sm.getString("contextConfig.missingRealm"));
/*  369 */       this.ok = false;
/*  370 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  378 */     Valve authenticator = null;
/*  379 */     if (this.customAuthenticators != null) {
/*  380 */       authenticator = (Valve)this.customAuthenticators.get(loginConfig.getAuthMethod());
/*      */     }
/*      */     
/*  383 */     if (authenticator == null) {
/*  384 */       if (authenticators == null) {
/*  385 */         log.error(sm.getString("contextConfig.authenticatorResources"));
/*  386 */         this.ok = false;
/*  387 */         return;
/*      */       }
/*      */       
/*      */ 
/*  391 */       String authenticatorName = authenticators.getProperty(loginConfig.getAuthMethod());
/*  392 */       if (authenticatorName == null) {
/*  393 */         log.error(sm.getString("contextConfig.authenticatorMissing", new Object[] {loginConfig
/*  394 */           .getAuthMethod() }));
/*  395 */         this.ok = false;
/*  396 */         return;
/*      */       }
/*      */       
/*      */       try
/*      */       {
/*  401 */         Class<?> authenticatorClass = Class.forName(authenticatorName);
/*  402 */         authenticator = (Valve)authenticatorClass.getConstructor(new Class[0]).newInstance(new Object[0]);
/*      */       } catch (Throwable t) {
/*  404 */         ExceptionUtils.handleThrowable(t);
/*  405 */         log.error(sm.getString("contextConfig.authenticatorInstantiate", new Object[] { authenticatorName }), t);
/*      */         
/*      */ 
/*      */ 
/*  409 */         this.ok = false;
/*      */       }
/*      */     }
/*      */     
/*  413 */     if (authenticator != null) {
/*  414 */       Pipeline pipeline = this.context.getPipeline();
/*  415 */       if (pipeline != null) {
/*  416 */         pipeline.addValve(authenticator);
/*  417 */         if (log.isDebugEnabled()) {
/*  418 */           log.debug(sm.getString("contextConfig.authenticatorConfigured", new Object[] {loginConfig
/*      */           
/*  420 */             .getAuthMethod() }));
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
/*      */   protected Digester createContextDigester()
/*      */   {
/*  433 */     Digester digester = new Digester();
/*  434 */     digester.setValidating(false);
/*  435 */     digester.setRulesValidation(true);
/*  436 */     HashMap<Class<?>, List<String>> fakeAttributes = new HashMap();
/*  437 */     ArrayList<String> attrs = new ArrayList();
/*  438 */     attrs.add("className");
/*  439 */     fakeAttributes.put(Object.class, attrs);
/*  440 */     digester.setFakeAttributes(fakeAttributes);
/*  441 */     RuleSet contextRuleSet = new ContextRuleSet("", false);
/*  442 */     digester.addRuleSet(contextRuleSet);
/*  443 */     RuleSet namingRuleSet = new NamingRuleSet("Context/");
/*  444 */     digester.addRuleSet(namingRuleSet);
/*  445 */     return digester;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void contextConfig(Digester digester)
/*      */   {
/*  455 */     String defaultContextXml = null;
/*      */     
/*      */ 
/*  458 */     if ((this.context instanceof StandardContext)) {
/*  459 */       defaultContextXml = ((StandardContext)this.context).getDefaultContextXml();
/*      */     }
/*      */     
/*  462 */     if (defaultContextXml == null) {
/*  463 */       defaultContextXml = "conf/context.xml";
/*      */     }
/*      */     
/*  466 */     if (!this.context.getOverride()) {
/*  467 */       File defaultContextFile = new File(defaultContextXml);
/*  468 */       if (!defaultContextFile.isAbsolute())
/*      */       {
/*  470 */         defaultContextFile = new File(this.context.getCatalinaBase(), defaultContextXml);
/*      */       }
/*  472 */       if (defaultContextFile.exists()) {
/*      */         try {
/*  474 */           URL defaultContextUrl = defaultContextFile.toURI().toURL();
/*  475 */           processContextConfig(digester, defaultContextUrl);
/*      */         } catch (MalformedURLException e) {
/*  477 */           log.error(sm.getString("contextConfig.badUrl", new Object[] { defaultContextFile }), e);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  482 */       File hostContextFile = new File(getHostConfigBase(), "context.xml.default");
/*  483 */       if (hostContextFile.exists()) {
/*      */         try {
/*  485 */           URL hostContextUrl = hostContextFile.toURI().toURL();
/*  486 */           processContextConfig(digester, hostContextUrl);
/*      */         } catch (MalformedURLException e) {
/*  488 */           log.error(sm.getString("contextConfig.badUrl", new Object[] { hostContextFile }), e);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  493 */     if (this.context.getConfigFile() != null) {
/*  494 */       processContextConfig(digester, this.context.getConfigFile());
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
/*      */   protected void processContextConfig(Digester digester, URL contextXml)
/*      */   {
/*  507 */     if (log.isDebugEnabled()) {
/*  508 */       log.debug("Processing context [" + this.context.getName() + "] configuration file [" + contextXml + "]");
/*      */     }
/*      */     
/*      */ 
/*  512 */     InputSource source = null;
/*  513 */     InputStream stream = null;
/*      */     try
/*      */     {
/*  516 */       source = new InputSource(contextXml.toString());
/*  517 */       URLConnection xmlConn = contextXml.openConnection();
/*  518 */       xmlConn.setUseCaches(false);
/*  519 */       stream = xmlConn.getInputStream();
/*      */     } catch (Exception e) {
/*  521 */       log.error(sm.getString("contextConfig.contextMissing", new Object[] { contextXml }), e);
/*      */     }
/*      */     
/*      */ 
/*  525 */     if (source == null) {
/*  526 */       return;
/*      */     }
/*      */     try
/*      */     {
/*  530 */       source.setByteStream(stream);
/*  531 */       digester.setClassLoader(getClass().getClassLoader());
/*  532 */       digester.setUseContextClassLoader(false);
/*  533 */       digester.push(this.context.getParent());
/*  534 */       digester.push(this.context);
/*  535 */       XmlErrorHandler errorHandler = new XmlErrorHandler();
/*  536 */       digester.setErrorHandler(errorHandler);
/*  537 */       digester.parse(source);
/*  538 */       if ((errorHandler.getWarnings().size() > 0) || 
/*  539 */         (errorHandler.getErrors().size() > 0)) {
/*  540 */         errorHandler.logFindings(log, contextXml.toString());
/*  541 */         this.ok = false;
/*      */       }
/*  543 */       if (log.isDebugEnabled()) {
/*  544 */         log.debug("Successfully processed context [" + this.context.getName() + "] configuration file [" + contextXml + "]");
/*      */       }
/*      */       return;
/*      */     } catch (SAXParseException e) {
/*  548 */       log.error(sm.getString("contextConfig.contextParse", new Object[] {this.context
/*  549 */         .getName() }), e);
/*  550 */       log.error(sm.getString("contextConfig.defaultPosition", new Object[] {"" + e
/*  551 */         .getLineNumber(), "" + e
/*  552 */         .getColumnNumber() }));
/*  553 */       this.ok = false;
/*      */     } catch (Exception e) {
/*  555 */       log.error(sm.getString("contextConfig.contextParse", new Object[] {this.context
/*  556 */         .getName() }), e);
/*  557 */       this.ok = false;
/*      */     } finally {
/*      */       try {
/*  560 */         if (stream != null) {
/*  561 */           stream.close();
/*      */         }
/*      */       } catch (IOException e) {
/*  564 */         log.error(sm.getString("contextConfig.contextClose"), e);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void fixDocBase()
/*      */     throws IOException
/*      */   {
/*  576 */     Host host = (Host)this.context.getParent();
/*  577 */     File appBase = host.getAppBaseFile();
/*      */     
/*  579 */     String docBase = this.context.getDocBase();
/*  580 */     if (docBase == null)
/*      */     {
/*  582 */       String path = this.context.getPath();
/*  583 */       if (path == null) {
/*  584 */         return;
/*      */       }
/*  586 */       ContextName cn = new ContextName(path, this.context.getWebappVersion());
/*  587 */       docBase = cn.getBaseName();
/*      */     }
/*      */     
/*  590 */     File file = new File(docBase);
/*  591 */     if (!file.isAbsolute()) {
/*  592 */       docBase = new File(appBase, docBase).getPath();
/*      */     } else {
/*  594 */       docBase = file.getCanonicalPath();
/*      */     }
/*  596 */     file = new File(docBase);
/*  597 */     String origDocBase = docBase;
/*      */     
/*  599 */     ContextName cn = new ContextName(this.context.getPath(), this.context.getWebappVersion());
/*  600 */     String pathName = cn.getBaseName();
/*      */     
/*  602 */     boolean unpackWARs = true;
/*  603 */     if ((host instanceof StandardHost)) {
/*  604 */       unpackWARs = ((StandardHost)host).isUnpackWARs();
/*  605 */       if ((unpackWARs) && ((this.context instanceof StandardContext))) {
/*  606 */         unpackWARs = ((StandardContext)this.context).getUnpackWAR();
/*      */       }
/*      */     }
/*      */     
/*  610 */     boolean docBaseInAppBase = docBase.startsWith(appBase.getPath() + File.separatorChar);
/*      */     
/*  612 */     if ((docBase.toLowerCase(Locale.ENGLISH).endsWith(".war")) && (!file.isDirectory())) {
/*  613 */       URL war = UriUtil.buildJarUrl(new File(docBase));
/*  614 */       if (unpackWARs) {
/*  615 */         docBase = ExpandWar.expand(host, war, pathName);
/*  616 */         file = new File(docBase);
/*  617 */         docBase = file.getCanonicalPath();
/*  618 */         if ((this.context instanceof StandardContext)) {
/*  619 */           ((StandardContext)this.context).setOriginalDocBase(origDocBase);
/*      */         }
/*      */       } else {
/*  622 */         ExpandWar.validate(host, war, pathName);
/*      */       }
/*      */     } else {
/*  625 */       File docDir = new File(docBase);
/*  626 */       File warFile = new File(docBase + ".war");
/*  627 */       URL war = null;
/*  628 */       if ((warFile.exists()) && (docBaseInAppBase)) {
/*  629 */         war = UriUtil.buildJarUrl(warFile);
/*      */       }
/*  631 */       if (docDir.exists()) {
/*  632 */         if ((war != null) && (unpackWARs))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*  637 */           ExpandWar.expand(host, war, pathName);
/*      */         }
/*      */       } else {
/*  640 */         if (war != null) {
/*  641 */           if (unpackWARs) {
/*  642 */             docBase = ExpandWar.expand(host, war, pathName);
/*  643 */             file = new File(docBase);
/*  644 */             docBase = file.getCanonicalPath();
/*      */           } else {
/*  646 */             docBase = warFile.getCanonicalPath();
/*  647 */             ExpandWar.validate(host, war, pathName);
/*      */           }
/*      */         }
/*  650 */         if ((this.context instanceof StandardContext)) {
/*  651 */           ((StandardContext)this.context).setOriginalDocBase(origDocBase);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  657 */     docBaseInAppBase = docBase.startsWith(appBase.getPath() + File.separatorChar);
/*      */     
/*  659 */     if (docBaseInAppBase) {
/*  660 */       docBase = docBase.substring(appBase.getPath().length());
/*  661 */       docBase = docBase.replace(File.separatorChar, '/');
/*  662 */       if (docBase.startsWith("/")) {
/*  663 */         docBase = docBase.substring(1);
/*      */       }
/*      */     } else {
/*  666 */       docBase = docBase.replace(File.separatorChar, '/');
/*      */     }
/*      */     
/*  669 */     this.context.setDocBase(docBase);
/*      */   }
/*      */   
/*      */ 
/*      */   protected void antiLocking()
/*      */   {
/*  675 */     if (((this.context instanceof StandardContext)) && 
/*  676 */       (((StandardContext)this.context).getAntiResourceLocking()))
/*      */     {
/*  678 */       Host host = (Host)this.context.getParent();
/*  679 */       String docBase = this.context.getDocBase();
/*  680 */       if (docBase == null) {
/*  681 */         return;
/*      */       }
/*  683 */       this.originalDocBase = docBase;
/*      */       
/*  685 */       File docBaseFile = new File(docBase);
/*  686 */       if (!docBaseFile.isAbsolute()) {
/*  687 */         docBaseFile = new File(host.getAppBaseFile(), docBase);
/*      */       }
/*      */       
/*  690 */       String path = this.context.getPath();
/*  691 */       if (path == null) {
/*  692 */         return;
/*      */       }
/*  694 */       ContextName cn = new ContextName(path, this.context.getWebappVersion());
/*  695 */       docBase = cn.getBaseName();
/*      */       
/*  697 */       if (this.originalDocBase.toLowerCase(Locale.ENGLISH).endsWith(".war"))
/*      */       {
/*  699 */         this.antiLockingDocBase = new File(System.getProperty("java.io.tmpdir"), deploymentCount++ + "-" + docBase + ".war");
/*      */       }
/*      */       else
/*      */       {
/*  703 */         this.antiLockingDocBase = new File(System.getProperty("java.io.tmpdir"), deploymentCount++ + "-" + docBase);
/*      */       }
/*      */       
/*  706 */       this.antiLockingDocBase = this.antiLockingDocBase.getAbsoluteFile();
/*      */       
/*  708 */       if (log.isDebugEnabled()) {
/*  709 */         log.debug("Anti locking context[" + this.context.getName() + "] setting docBase to " + this.antiLockingDocBase
/*      */         
/*  711 */           .getPath());
/*      */       }
/*      */       
/*      */ 
/*  715 */       ExpandWar.delete(this.antiLockingDocBase);
/*  716 */       if (ExpandWar.copy(docBaseFile, this.antiLockingDocBase)) {
/*  717 */         this.context.setDocBase(this.antiLockingDocBase.getPath());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void init()
/*      */   {
/*  729 */     Digester contextDigester = createContextDigester();
/*  730 */     contextDigester.getParser();
/*      */     
/*  732 */     if (log.isDebugEnabled()) {
/*  733 */       log.debug(sm.getString("contextConfig.init"));
/*      */     }
/*  735 */     this.context.setConfigured(false);
/*  736 */     this.ok = true;
/*      */     
/*  738 */     contextConfig(contextDigester);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected synchronized void beforeStart()
/*      */   {
/*      */     try
/*      */     {
/*  748 */       fixDocBase();
/*      */     } catch (IOException e) {
/*  750 */       log.error(sm.getString("contextConfig.fixDocBase", new Object[] {this.context
/*  751 */         .getName() }), e);
/*      */     }
/*      */     
/*  754 */     antiLocking();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected synchronized void configureStart()
/*      */   {
/*  764 */     if (log.isDebugEnabled()) {
/*  765 */       log.debug(sm.getString("contextConfig.start"));
/*      */     }
/*      */     
/*  768 */     if (log.isDebugEnabled()) {
/*  769 */       log.debug(sm.getString("contextConfig.xmlSettings", new Object[] {this.context
/*  770 */         .getName(), 
/*  771 */         Boolean.valueOf(this.context.getXmlValidation()), 
/*  772 */         Boolean.valueOf(this.context.getXmlNamespaceAware()) }));
/*      */     }
/*      */     
/*  775 */     webConfig();
/*      */     
/*  777 */     if (!this.context.getIgnoreAnnotations()) {
/*  778 */       applicationAnnotationsConfig();
/*      */     }
/*  780 */     if (this.ok) {
/*  781 */       validateSecurityRoles();
/*      */     }
/*      */     
/*      */ 
/*  785 */     if (this.ok) {
/*  786 */       authenticatorConfig();
/*      */     }
/*      */     
/*      */ 
/*  790 */     if (log.isDebugEnabled()) {
/*  791 */       log.debug("Pipeline Configuration:");
/*  792 */       Pipeline pipeline = this.context.getPipeline();
/*  793 */       Valve[] valves = null;
/*  794 */       if (pipeline != null) {
/*  795 */         valves = pipeline.getValves();
/*      */       }
/*  797 */       if (valves != null) {
/*  798 */         for (int i = 0; i < valves.length; i++) {
/*  799 */           log.debug("  " + valves[i].getClass().getName());
/*      */         }
/*      */       }
/*  802 */       log.debug("======================");
/*      */     }
/*      */     
/*      */ 
/*  806 */     if (this.ok) {
/*  807 */       this.context.setConfigured(true);
/*      */     } else {
/*  809 */       log.error(sm.getString("contextConfig.unavailable"));
/*  810 */       this.context.setConfigured(false);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected synchronized void configureStop()
/*      */   {
/*  821 */     if (log.isDebugEnabled()) {
/*  822 */       log.debug(sm.getString("contextConfig.stop"));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  828 */     Container[] children = this.context.findChildren();
/*  829 */     for (int i = 0; i < children.length; i++) {
/*  830 */       this.context.removeChild(children[i]);
/*      */     }
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
/*  844 */     SecurityConstraint[] securityConstraints = this.context.findConstraints();
/*  845 */     for (i = 0; i < securityConstraints.length; i++) {
/*  846 */       this.context.removeConstraint(securityConstraints[i]);
/*      */     }
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
/*  866 */     ErrorPage[] errorPages = this.context.findErrorPages();
/*  867 */     for (i = 0; i < errorPages.length; i++) {
/*  868 */       this.context.removeErrorPage(errorPages[i]);
/*      */     }
/*      */     
/*      */ 
/*  872 */     FilterDef[] filterDefs = this.context.findFilterDefs();
/*  873 */     for (i = 0; i < filterDefs.length; i++) {
/*  874 */       this.context.removeFilterDef(filterDefs[i]);
/*      */     }
/*      */     
/*      */ 
/*  878 */     FilterMap[] filterMaps = this.context.findFilterMaps();
/*  879 */     for (i = 0; i < filterMaps.length; i++) {
/*  880 */       this.context.removeFilterMap(filterMaps[i]);
/*      */     }
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
/*  892 */     String[] mimeMappings = this.context.findMimeMappings();
/*  893 */     for (i = 0; i < mimeMappings.length; i++) {
/*  894 */       this.context.removeMimeMapping(mimeMappings[i]);
/*      */     }
/*      */     
/*      */ 
/*  898 */     String[] parameters = this.context.findParameters();
/*  899 */     for (i = 0; i < parameters.length; i++) {
/*  900 */       this.context.removeParameter(parameters[i]);
/*      */     }
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
/*  929 */     String[] securityRoles = this.context.findSecurityRoles();
/*  930 */     for (i = 0; i < securityRoles.length; i++) {
/*  931 */       this.context.removeSecurityRole(securityRoles[i]);
/*      */     }
/*      */     
/*      */ 
/*  935 */     String[] servletMappings = this.context.findServletMappings();
/*  936 */     for (i = 0; i < servletMappings.length; i++) {
/*  937 */       this.context.removeServletMapping(servletMappings[i]);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  943 */     String[] welcomeFiles = this.context.findWelcomeFiles();
/*  944 */     for (i = 0; i < welcomeFiles.length; i++) {
/*  945 */       this.context.removeWelcomeFile(welcomeFiles[i]);
/*      */     }
/*      */     
/*      */ 
/*  949 */     String[] wrapperLifecycles = this.context.findWrapperLifecycles();
/*  950 */     for (i = 0; i < wrapperLifecycles.length; i++) {
/*  951 */       this.context.removeWrapperLifecycle(wrapperLifecycles[i]);
/*      */     }
/*      */     
/*      */ 
/*  955 */     String[] wrapperListeners = this.context.findWrapperListeners();
/*  956 */     for (i = 0; i < wrapperListeners.length; i++) {
/*  957 */       this.context.removeWrapperListener(wrapperListeners[i]);
/*      */     }
/*      */     
/*      */ 
/*  961 */     if (this.antiLockingDocBase != null)
/*      */     {
/*  963 */       ExpandWar.delete(this.antiLockingDocBase, false);
/*      */     }
/*      */     
/*      */ 
/*  967 */     this.initializerClassMap.clear();
/*  968 */     this.typeInitializerMap.clear();
/*      */     
/*  970 */     this.ok = true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected synchronized void destroy()
/*      */   {
/*  980 */     if (log.isDebugEnabled()) {
/*  981 */       log.debug(sm.getString("contextConfig.destroy"));
/*      */     }
/*      */     
/*      */ 
/*  985 */     Server s = getServer();
/*  986 */     if ((s != null) && (!s.getState().isAvailable())) {
/*  987 */       return;
/*      */     }
/*      */     
/*      */ 
/*  991 */     if ((this.context instanceof StandardContext)) {
/*  992 */       String workDir = ((StandardContext)this.context).getWorkPath();
/*  993 */       if (workDir != null) {
/*  994 */         ExpandWar.delete(new File(workDir));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private Server getServer()
/*      */   {
/* 1001 */     Container c = this.context;
/* 1002 */     while ((c != null) && (!(c instanceof Engine))) {
/* 1003 */       c = c.getParent();
/*      */     }
/*      */     
/* 1006 */     if (c == null) {
/* 1007 */       return null;
/*      */     }
/*      */     
/* 1010 */     Service s = ((Engine)c).getService();
/*      */     
/* 1012 */     if (s == null) {
/* 1013 */       return null;
/*      */     }
/*      */     
/* 1016 */     return s.getServer();
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
/*      */   protected void validateSecurityRoles()
/*      */   {
/* 1029 */     SecurityConstraint[] constraints = this.context.findConstraints();
/* 1030 */     for (int i = 0; i < constraints.length; i++) {
/* 1031 */       String[] roles = constraints[i].findAuthRoles();
/* 1032 */       for (int j = 0; j < roles.length; j++) {
/* 1033 */         if ((!"*".equals(roles[j])) && 
/* 1034 */           (!this.context.findSecurityRole(roles[j]))) {
/* 1035 */           log.warn(sm.getString("contextConfig.role.auth", new Object[] { roles[j] }));
/* 1036 */           this.context.addSecurityRole(roles[j]);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1042 */     Container[] wrappers = this.context.findChildren();
/* 1043 */     for (int i = 0; i < wrappers.length; i++) {
/* 1044 */       Wrapper wrapper = (Wrapper)wrappers[i];
/* 1045 */       String runAs = wrapper.getRunAs();
/* 1046 */       if ((runAs != null) && (!this.context.findSecurityRole(runAs))) {
/* 1047 */         log.warn(sm.getString("contextConfig.role.runas", new Object[] { runAs }));
/* 1048 */         this.context.addSecurityRole(runAs);
/*      */       }
/* 1050 */       String[] names = wrapper.findSecurityReferences();
/* 1051 */       for (int j = 0; j < names.length; j++) {
/* 1052 */         String link = wrapper.findSecurityReference(names[j]);
/* 1053 */         if ((link != null) && (!this.context.findSecurityRole(link))) {
/* 1054 */           log.warn(sm.getString("contextConfig.role.link", new Object[] { link }));
/* 1055 */           this.context.addSecurityRole(link);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected File getHostConfigBase()
/*      */   {
/* 1064 */     File file = null;
/* 1065 */     if ((this.context.getParent() instanceof Host)) {
/* 1066 */       file = ((Host)this.context.getParent()).getConfigBaseFile();
/*      */     }
/* 1068 */     return file;
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
/*      */   protected void webConfig()
/*      */   {
/* 1105 */     WebXmlParser webXmlParser = new WebXmlParser(this.context.getXmlNamespaceAware(), this.context.getXmlValidation(), this.context.getXmlBlockExternal());
/*      */     
/* 1107 */     Set<WebXml> defaults = new HashSet();
/* 1108 */     defaults.add(getDefaultWebXmlFragment(webXmlParser));
/*      */     
/* 1110 */     WebXml webXml = createWebXml();
/*      */     
/*      */ 
/* 1113 */     InputSource contextWebXml = getContextWebXmlSource();
/* 1114 */     if (!webXmlParser.parseWebXml(contextWebXml, webXml, false)) {
/* 1115 */       this.ok = false;
/*      */     }
/*      */     
/* 1118 */     ServletContext sContext = this.context.getServletContext();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1126 */     Map<String, WebXml> fragments = processJarsForWebFragments(webXml, webXmlParser);
/*      */     
/*      */ 
/* 1129 */     Set<WebXml> orderedFragments = null;
/*      */     
/* 1131 */     orderedFragments = WebXml.orderWebFragments(webXml, fragments, sContext);
/*      */     
/*      */ 
/* 1134 */     if (this.ok) {
/* 1135 */       processServletContainerInitializers();
/*      */     }
/*      */     WebResource[] webResources;
/* 1138 */     if ((!webXml.isMetadataComplete()) || (this.typeInitializerMap.size() > 0))
/*      */     {
/*      */ 
/* 1141 */       Map<String, JavaClassCacheEntry> javaClassCache = new HashMap();
/*      */       
/* 1143 */       if (this.ok)
/*      */       {
/* 1145 */         webResources = this.context.getResources().listResources("/WEB-INF/classes");
/*      */         
/* 1147 */         for (WebResource webResource : webResources)
/*      */         {
/*      */ 
/* 1150 */           if (!"META-INF".equals(webResource.getName()))
/*      */           {
/*      */ 
/* 1153 */             processAnnotationsWebResource(webResource, webXml, webXml
/* 1154 */               .isMetadataComplete(), javaClassCache);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1162 */       if (this.ok) {
/* 1163 */         processAnnotations(orderedFragments, webXml
/* 1164 */           .isMetadataComplete(), javaClassCache);
/*      */       }
/*      */       
/*      */ 
/* 1168 */       javaClassCache.clear();
/*      */     }
/*      */     
/* 1171 */     if (!webXml.isMetadataComplete())
/*      */     {
/*      */ 
/* 1174 */       if (this.ok) {
/* 1175 */         this.ok = webXml.merge(orderedFragments);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1181 */       webXml.merge(defaults);
/*      */       
/*      */ 
/* 1184 */       if (this.ok) {
/* 1185 */         convertJsps(webXml);
/*      */       }
/*      */       
/*      */ 
/* 1189 */       if (this.ok) {
/* 1190 */         configureContext(webXml);
/*      */       }
/*      */     } else {
/* 1193 */       webXml.merge(defaults);
/* 1194 */       convertJsps(webXml);
/* 1195 */       configureContext(webXml);
/*      */     }
/*      */     
/* 1198 */     if (this.context.getLogEffectiveWebXml()) {
/* 1199 */       log.info("web.xml:\n" + webXml.toXml());
/*      */     }
/*      */     
/*      */     Set<WebXml> resourceJars;
/*      */     
/* 1204 */     if (this.ok)
/*      */     {
/*      */ 
/* 1207 */       resourceJars = new LinkedHashSet();
/* 1208 */       for (WebXml fragment : orderedFragments) {
/* 1209 */         resourceJars.add(fragment);
/*      */       }
/* 1211 */       for (WebXml fragment : fragments.values()) {
/* 1212 */         if (!resourceJars.contains(fragment)) {
/* 1213 */           resourceJars.add(fragment);
/*      */         }
/*      */       }
/* 1216 */       processResourceJARs(resourceJars);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1223 */     if (this.ok)
/*      */     {
/*      */ 
/* 1226 */       for (Map.Entry<ServletContainerInitializer, Set<Class<?>>> entry : this.initializerClassMap.entrySet()) {
/* 1227 */         if (((Set)entry.getValue()).isEmpty()) {
/* 1228 */           this.context.addServletContainerInitializer(
/* 1229 */             (ServletContainerInitializer)entry.getKey(), null);
/*      */         } else {
/* 1231 */           this.context.addServletContainerInitializer(
/* 1232 */             (ServletContainerInitializer)entry.getKey(), (Set)entry.getValue());
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void configureContext(WebXml webxml)
/*      */   {
/* 1243 */     this.context.setPublicId(webxml.getPublicId());
/*      */     
/*      */ 
/* 1246 */     this.context.setEffectiveMajorVersion(webxml.getMajorVersion());
/* 1247 */     this.context.setEffectiveMinorVersion(webxml.getMinorVersion());
/*      */     
/* 1249 */     for (Map.Entry<String, String> entry : webxml.getContextParams().entrySet()) {
/* 1250 */       this.context.addParameter((String)entry.getKey(), (String)entry.getValue());
/*      */     }
/* 1252 */     this.context.setDenyUncoveredHttpMethods(webxml
/* 1253 */       .getDenyUncoveredHttpMethods());
/* 1254 */     this.context.setDisplayName(webxml.getDisplayName());
/* 1255 */     this.context.setDistributable(webxml.isDistributable());
/* 1256 */     for (ContextLocalEjb ejbLocalRef : webxml.getEjbLocalRefs().values()) {
/* 1257 */       this.context.getNamingResources().addLocalEjb(ejbLocalRef);
/*      */     }
/* 1259 */     for (ContextEjb ejbRef : webxml.getEjbRefs().values()) {
/* 1260 */       this.context.getNamingResources().addEjb(ejbRef);
/*      */     }
/* 1262 */     for (ContextEnvironment environment : webxml.getEnvEntries().values()) {
/* 1263 */       this.context.getNamingResources().addEnvironment(environment);
/*      */     }
/* 1265 */     for (ErrorPage errorPage : webxml.getErrorPages().values()) {
/* 1266 */       this.context.addErrorPage(errorPage);
/*      */     }
/* 1268 */     for (FilterDef filter : webxml.getFilters().values()) {
/* 1269 */       if (filter.getAsyncSupported() == null) {
/* 1270 */         filter.setAsyncSupported("false");
/*      */       }
/* 1272 */       this.context.addFilterDef(filter);
/*      */     }
/* 1274 */     for (FilterMap filterMap : webxml.getFilterMappings()) {
/* 1275 */       this.context.addFilterMap(filterMap);
/*      */     }
/* 1277 */     this.context.setJspConfigDescriptor(webxml.getJspConfigDescriptor());
/* 1278 */     for (String listener : webxml.getListeners()) {
/* 1279 */       this.context.addApplicationListener(listener);
/*      */     }
/*      */     
/* 1282 */     for (Map.Entry<String, String> entry : webxml.getLocaleEncodingMappings().entrySet()) {
/* 1283 */       this.context.addLocaleEncodingMappingParameter((String)entry.getKey(), 
/* 1284 */         (String)entry.getValue());
/*      */     }
/*      */     
/* 1287 */     if (webxml.getLoginConfig() != null) {
/* 1288 */       this.context.setLoginConfig(webxml.getLoginConfig());
/*      */     }
/*      */     
/* 1291 */     for (MessageDestinationRef mdr : webxml.getMessageDestinationRefs().values()) {
/* 1292 */       this.context.getNamingResources().addMessageDestinationRef(mdr);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1297 */     this.context.setIgnoreAnnotations(webxml.isMetadataComplete());
/*      */     
/* 1299 */     for (Map.Entry<String, String> entry : webxml.getMimeMappings().entrySet()) {
/* 1300 */       this.context.addMimeMapping((String)entry.getKey(), (String)entry.getValue());
/*      */     }
/*      */     
/*      */ 
/* 1304 */     for (ContextResourceEnvRef resource : webxml.getResourceEnvRefs().values()) {
/* 1305 */       this.context.getNamingResources().addResourceEnvRef(resource);
/*      */     }
/* 1307 */     for (??? = webxml.getResourceRefs().values().iterator(); ???.hasNext();) { resource = (ContextResource)???.next();
/* 1308 */       this.context.getNamingResources().addResource(resource);
/*      */     }
/*      */     ContextResource resource;
/* 1311 */     boolean allAuthenticatedUsersIsAppRole = webxml.getSecurityRoles().contains("**");
/*      */     
/* 1313 */     for (SecurityConstraint constraint : webxml.getSecurityConstraints()) {
/* 1314 */       if (allAuthenticatedUsersIsAppRole) {
/* 1315 */         constraint.treatAllAuthenticatedUsersAsApplicationRole();
/*      */       }
/* 1317 */       this.context.addConstraint(constraint);
/*      */     }
/* 1319 */     for (String role : webxml.getSecurityRoles()) {
/* 1320 */       this.context.addSecurityRole(role);
/*      */     }
/* 1322 */     for (ContextService service : webxml.getServiceRefs().values()) {
/* 1323 */       this.context.getNamingResources().addService(service);
/*      */     }
/* 1325 */     for (ServletDef servlet : webxml.getServlets().values()) {
/* 1326 */       Wrapper wrapper = this.context.createWrapper();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1333 */       if (servlet.getLoadOnStartup() != null) {
/* 1334 */         wrapper.setLoadOnStartup(servlet.getLoadOnStartup().intValue());
/*      */       }
/* 1336 */       if (servlet.getEnabled() != null) {
/* 1337 */         wrapper.setEnabled(servlet.getEnabled().booleanValue());
/*      */       }
/* 1339 */       wrapper.setName(servlet.getServletName());
/* 1340 */       Map<String, String> params = servlet.getParameterMap();
/* 1341 */       for (Iterator localIterator2 = params.entrySet().iterator(); localIterator2.hasNext();) { entry = (Map.Entry)localIterator2.next();
/* 1342 */         wrapper.addInitParameter((String)entry.getKey(), (String)entry.getValue()); }
/*      */       Map.Entry<String, String> entry;
/* 1344 */       wrapper.setRunAs(servlet.getRunAs());
/* 1345 */       roleRefs = servlet.getSecurityRoleRefs();
/* 1346 */       for (SecurityRoleRef roleRef : (Set)roleRefs) {
/* 1347 */         wrapper.addSecurityReference(roleRef
/* 1348 */           .getName(), roleRef.getLink());
/*      */       }
/* 1350 */       wrapper.setServletClass(servlet.getServletClass());
/* 1351 */       MultipartDef multipartdef = servlet.getMultipartDef();
/* 1352 */       if (multipartdef != null) {
/* 1353 */         if ((multipartdef.getMaxFileSize() != null) && 
/* 1354 */           (multipartdef.getMaxRequestSize() != null) && 
/* 1355 */           (multipartdef.getFileSizeThreshold() != null)) {
/* 1356 */           wrapper.setMultipartConfigElement(new MultipartConfigElement(multipartdef
/* 1357 */             .getLocation(), 
/* 1358 */             Long.parseLong(multipartdef.getMaxFileSize()), 
/* 1359 */             Long.parseLong(multipartdef.getMaxRequestSize()), 
/* 1360 */             Integer.parseInt(multipartdef
/* 1361 */             .getFileSizeThreshold())));
/*      */         } else {
/* 1363 */           wrapper.setMultipartConfigElement(new MultipartConfigElement(multipartdef
/* 1364 */             .getLocation()));
/*      */         }
/*      */       }
/* 1367 */       if (servlet.getAsyncSupported() != null) {
/* 1368 */         wrapper.setAsyncSupported(servlet
/* 1369 */           .getAsyncSupported().booleanValue());
/*      */       }
/* 1371 */       wrapper.setOverridable(servlet.isOverridable());
/* 1372 */       this.context.addChild(wrapper);
/*      */     }
/*      */     Object roleRefs;
/* 1375 */     for (Map.Entry<String, String> entry : webxml.getServletMappings().entrySet()) {
/* 1376 */       this.context.addServletMappingDecoded((String)entry.getKey(), (String)entry.getValue());
/*      */     }
/* 1378 */     SessionConfig sessionConfig = webxml.getSessionConfig();
/* 1379 */     SessionCookieConfig scc; if (sessionConfig != null) {
/* 1380 */       if (sessionConfig.getSessionTimeout() != null) {
/* 1381 */         this.context.setSessionTimeout(sessionConfig
/* 1382 */           .getSessionTimeout().intValue());
/*      */       }
/*      */       
/* 1385 */       scc = this.context.getServletContext().getSessionCookieConfig();
/* 1386 */       scc.setName(sessionConfig.getCookieName());
/* 1387 */       scc.setDomain(sessionConfig.getCookieDomain());
/* 1388 */       scc.setPath(sessionConfig.getCookiePath());
/* 1389 */       scc.setComment(sessionConfig.getCookieComment());
/* 1390 */       if (sessionConfig.getCookieHttpOnly() != null) {
/* 1391 */         scc.setHttpOnly(sessionConfig.getCookieHttpOnly().booleanValue());
/*      */       }
/* 1393 */       if (sessionConfig.getCookieSecure() != null) {
/* 1394 */         scc.setSecure(sessionConfig.getCookieSecure().booleanValue());
/*      */       }
/* 1396 */       if (sessionConfig.getCookieMaxAge() != null) {
/* 1397 */         scc.setMaxAge(sessionConfig.getCookieMaxAge().intValue());
/*      */       }
/* 1399 */       if (sessionConfig.getSessionTrackingModes().size() > 0) {
/* 1400 */         this.context.getServletContext().setSessionTrackingModes(sessionConfig
/* 1401 */           .getSessionTrackingModes());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1407 */     for (String welcomeFile : webxml.getWelcomeFiles())
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1415 */       if ((welcomeFile != null) && (welcomeFile.length() > 0)) {
/* 1416 */         this.context.addWelcomeFile(welcomeFile);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1422 */     for (JspPropertyGroup jspPropertyGroup : webxml.getJspPropertyGroups()) {
/* 1423 */       jspServletName = this.context.findServletMapping("*.jsp");
/* 1424 */       if (jspServletName == null) {
/* 1425 */         jspServletName = "jsp";
/*      */       }
/* 1427 */       if (this.context.findChild(jspServletName) != null) {
/* 1428 */         for (roleRefs = jspPropertyGroup.getUrlPatterns().iterator(); ((Iterator)roleRefs).hasNext();) { String urlPattern = (String)((Iterator)roleRefs).next();
/* 1429 */           this.context.addServletMappingDecoded(urlPattern, jspServletName, true);
/*      */         }
/*      */         
/* 1432 */       } else if (log.isDebugEnabled()) {
/* 1433 */         for (roleRefs = jspPropertyGroup.getUrlPatterns().iterator(); ((Iterator)roleRefs).hasNext();) { String urlPattern = (String)((Iterator)roleRefs).next();
/* 1434 */           log.debug("Skipping " + urlPattern + " , no servlet " + jspServletName);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     String jspServletName;
/*      */     
/* 1442 */     for (Map.Entry<String, String> entry : webxml.getPostConstructMethods().entrySet()) {
/* 1443 */       this.context.addPostConstructMethod((String)entry.getKey(), (String)entry.getValue());
/*      */     }
/*      */     
/*      */ 
/* 1447 */     for (Map.Entry<String, String> entry : webxml.getPreDestroyMethods().entrySet()) {
/* 1448 */       this.context.addPreDestroyMethod((String)entry.getKey(), (String)entry.getValue());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private WebXml getDefaultWebXmlFragment(WebXmlParser webXmlParser)
/*      */   {
/* 1456 */     Host host = (Host)this.context.getParent();
/*      */     
/* 1458 */     DefaultWebXmlCacheEntry entry = (DefaultWebXmlCacheEntry)hostWebXmlCache.get(host);
/*      */     
/* 1460 */     InputSource globalWebXml = getGlobalWebXmlSource();
/* 1461 */     InputSource hostWebXml = getHostWebXmlSource();
/*      */     
/* 1463 */     long globalTimeStamp = 0L;
/* 1464 */     long hostTimeStamp = 0L;
/*      */     
/* 1466 */     if (globalWebXml != null) {
/* 1467 */       URLConnection uc = null;
/*      */       try {
/* 1469 */         URL url = new URL(globalWebXml.getSystemId());
/* 1470 */         uc = url.openConnection();
/* 1471 */         globalTimeStamp = uc.getLastModified();
/*      */         
/*      */ 
/*      */ 
/* 1475 */         if (uc != null) {
/*      */           try {
/* 1477 */             uc.getInputStream().close();
/*      */           } catch (IOException e) {
/* 1479 */             ExceptionUtils.handleThrowable(e);
/* 1480 */             globalTimeStamp = -1L;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1486 */         if (hostWebXml == null) {
/*      */           break label320;
/*      */         }
/*      */       }
/*      */       catch (IOException e)
/*      */       {
/* 1473 */         globalTimeStamp = -1L;
/*      */       } finally {
/* 1475 */         if (uc != null) {
/*      */           try {
/* 1477 */             uc.getInputStream().close();
/*      */           } catch (IOException e) {
/* 1479 */             ExceptionUtils.handleThrowable(e);
/* 1480 */             globalTimeStamp = -1L;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1487 */     URLConnection uc = null;
/*      */     try {
/* 1489 */       URL url = new URL(hostWebXml.getSystemId());
/* 1490 */       uc = url.openConnection();
/* 1491 */       hostTimeStamp = uc.getLastModified();
/*      */       
/*      */ 
/*      */ 
/* 1495 */       if (uc != null) {
/*      */         try {
/* 1497 */           uc.getInputStream().close();
/*      */         } catch (IOException e) {
/* 1499 */           ExceptionUtils.handleThrowable(e);
/* 1500 */           hostTimeStamp = -1L;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1506 */       if (entry == null) {
/*      */         break label359;
/*      */       }
/*      */     }
/*      */     catch (IOException e)
/*      */     {
/* 1493 */       hostTimeStamp = -1L;
/*      */     } finally {
/* 1495 */       if (uc != null) {
/*      */         try {
/* 1497 */           uc.getInputStream().close();
/*      */         } catch (IOException e) {
/* 1499 */           ExceptionUtils.handleThrowable(e);
/* 1500 */           hostTimeStamp = -1L;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     label320:
/* 1506 */     if ((entry.getGlobalTimeStamp() == globalTimeStamp) && 
/* 1507 */       (entry.getHostTimeStamp() == hostTimeStamp)) {
/* 1508 */       InputSourceUtil.close(globalWebXml);
/* 1509 */       InputSourceUtil.close(hostWebXml);
/* 1510 */       return entry.getWebXml();
/*      */     }
/*      */     
/*      */ 
/*      */     label359:
/*      */     
/* 1516 */     synchronized (host.getPipeline()) {
/* 1517 */       entry = (DefaultWebXmlCacheEntry)hostWebXmlCache.get(host);
/* 1518 */       if ((entry != null) && (entry.getGlobalTimeStamp() == globalTimeStamp) && 
/* 1519 */         (entry.getHostTimeStamp() == hostTimeStamp)) {
/* 1520 */         return entry.getWebXml();
/*      */       }
/*      */       
/* 1523 */       WebXml webXmlDefaultFragment = createWebXml();
/* 1524 */       webXmlDefaultFragment.setOverridable(true);
/*      */       
/*      */ 
/*      */ 
/* 1528 */       webXmlDefaultFragment.setDistributable(true);
/*      */       
/*      */ 
/* 1531 */       webXmlDefaultFragment.setAlwaysAddWelcomeFiles(false);
/*      */       
/*      */ 
/* 1534 */       if (globalWebXml == null)
/*      */       {
/* 1536 */         log.info(sm.getString("contextConfig.defaultMissing"));
/*      */       }
/* 1538 */       else if (!webXmlParser.parseWebXml(globalWebXml, webXmlDefaultFragment, false))
/*      */       {
/* 1540 */         this.ok = false;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1546 */       webXmlDefaultFragment.setReplaceWelcomeFiles(true);
/*      */       
/* 1548 */       if (!webXmlParser.parseWebXml(hostWebXml, webXmlDefaultFragment, false))
/*      */       {
/* 1550 */         this.ok = false;
/*      */       }
/*      */       
/*      */ 
/* 1554 */       if ((globalTimeStamp != -1L) && (hostTimeStamp != -1L)) {
/* 1555 */         entry = new DefaultWebXmlCacheEntry(webXmlDefaultFragment, globalTimeStamp, hostTimeStamp);
/*      */         
/* 1557 */         hostWebXmlCache.put(host, entry);
/*      */       }
/*      */       
/* 1560 */       return webXmlDefaultFragment;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void convertJsps(WebXml webXml)
/*      */   {
/* 1567 */     ServletDef jspServlet = (ServletDef)webXml.getServlets().get("jsp");
/* 1568 */     Wrapper w; Map<String, String> jspInitParams; if (jspServlet == null) {
/* 1569 */       Map<String, String> jspInitParams = new HashMap();
/* 1570 */       w = (Wrapper)this.context.findChild("jsp");
/* 1571 */       if (w != null) {
/* 1572 */         String[] params = w.findInitParameters();
/* 1573 */         for (String param : params) {
/* 1574 */           jspInitParams.put(param, w.findInitParameter(param));
/*      */         }
/*      */       }
/*      */     } else {
/* 1578 */       jspInitParams = jspServlet.getParameterMap();
/*      */     }
/* 1580 */     for (ServletDef servletDef : webXml.getServlets().values()) {
/* 1581 */       if (servletDef.getJspFile() != null) {
/* 1582 */         convertJsp(servletDef, jspInitParams);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void convertJsp(ServletDef servletDef, Map<String, String> jspInitParams)
/*      */   {
/* 1589 */     servletDef.setServletClass("org.apache.jasper.servlet.JspServlet");
/* 1590 */     String jspFile = servletDef.getJspFile();
/* 1591 */     if ((jspFile != null) && (!jspFile.startsWith("/"))) {
/* 1592 */       if (this.context.isServlet22()) {
/* 1593 */         if (log.isDebugEnabled()) {
/* 1594 */           log.debug(sm.getString("contextConfig.jspFile.warning", new Object[] { jspFile }));
/*      */         }
/*      */         
/* 1597 */         jspFile = "/" + jspFile;
/*      */       }
/*      */       else {
/* 1600 */         throw new IllegalArgumentException(sm.getString("contextConfig.jspFile.error", new Object[] { jspFile }));
/*      */       }
/*      */     }
/* 1603 */     servletDef.getParameterMap().put("jspFile", jspFile);
/* 1604 */     servletDef.setJspFile(null);
/* 1605 */     for (Map.Entry<String, String> initParam : jspInitParams.entrySet()) {
/* 1606 */       servletDef.addInitParameter((String)initParam.getKey(), (String)initParam.getValue());
/*      */     }
/*      */   }
/*      */   
/*      */   protected WebXml createWebXml() {
/* 1611 */     return new WebXml();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void processServletContainerInitializers()
/*      */   {
/*      */     try
/*      */     {
/* 1621 */       WebappServiceLoader<ServletContainerInitializer> loader = new WebappServiceLoader(this.context);
/* 1622 */       detectedScis = loader.load(ServletContainerInitializer.class);
/*      */     } catch (IOException e) { List<ServletContainerInitializer> detectedScis;
/* 1624 */       log.error(sm.getString("contextConfig.servletContainerInitializerFail", new Object[] {this.context
/*      */       
/* 1626 */         .getName() }), e);
/*      */       
/* 1628 */       this.ok = false; return;
/*      */     }
/*      */     
/*      */     List<ServletContainerInitializer> detectedScis;
/* 1632 */     for (ServletContainerInitializer sci : detectedScis) {
/* 1633 */       this.initializerClassMap.put(sci, new HashSet());
/*      */       
/*      */       try
/*      */       {
/* 1637 */         ht = (HandlesTypes)sci.getClass().getAnnotation(HandlesTypes.class);
/*      */       } catch (Exception e) { HandlesTypes ht;
/* 1639 */         if (log.isDebugEnabled()) {
/* 1640 */           log.info(sm.getString("contextConfig.sci.debug", new Object[] {sci
/* 1641 */             .getClass().getName() }), e);
/*      */         }
/*      */         else
/* 1644 */           log.info(sm.getString("contextConfig.sci.info", new Object[] {sci
/* 1645 */             .getClass().getName() }));
/*      */       }
/* 1647 */       continue;
/*      */       HandlesTypes ht;
/* 1649 */       if (ht != null)
/*      */       {
/*      */ 
/* 1652 */         Class<?>[] types = ht.value();
/* 1653 */         if (types != null)
/*      */         {
/*      */ 
/*      */ 
/* 1657 */           for (Class<?> type : types) {
/* 1658 */             if (type.isAnnotation()) {
/* 1659 */               this.handlesTypesAnnotations = true;
/*      */             } else {
/* 1661 */               this.handlesTypesNonAnnotations = true;
/*      */             }
/*      */             
/* 1664 */             Set<ServletContainerInitializer> scis = (Set)this.typeInitializerMap.get(type);
/* 1665 */             if (scis == null) {
/* 1666 */               scis = new HashSet();
/* 1667 */               this.typeInitializerMap.put(type, scis);
/*      */             }
/* 1669 */             scis.add(sci);
/*      */           }
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
/*      */   protected void processResourceJARs(Set<WebXml> fragments)
/*      */   {
/* 1683 */     for (WebXml fragment : fragments) {
/* 1684 */       URL url = fragment.getURL();
/*      */       try {
/* 1686 */         if (("jar".equals(url.getProtocol())) || (url.toString().endsWith(".jar"))) {
/* 1687 */           Jar jar = JarFactory.newInstance(url);Throwable localThrowable3 = null;
/* 1688 */           try { jar.nextEntry();
/* 1689 */             String entryName = jar.getEntryName();
/* 1690 */             while (entryName != null) {
/* 1691 */               if (entryName.startsWith("META-INF/resources/")) {
/* 1692 */                 this.context.getResources().createWebResourceSet(WebResourceRoot.ResourceSetType.RESOURCE_JAR, "/", url, "/META-INF/resources");
/*      */                 
/*      */ 
/* 1695 */                 break;
/*      */               }
/* 1697 */               jar.nextEntry();
/* 1698 */               entryName = jar.getEntryName();
/*      */             }
/*      */           }
/*      */           catch (Throwable localThrowable1)
/*      */           {
/* 1687 */             localThrowable3 = localThrowable1;throw localThrowable1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           }
/*      */           finally
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1700 */             if (jar != null) if (localThrowable3 != null) try { jar.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else jar.close();
/* 1701 */           } } else if ("file".equals(url.getProtocol())) {
/* 1702 */           File file = new File(url.toURI());
/* 1703 */           File resources = new File(file, "META-INF/resources/");
/* 1704 */           if (resources.isDirectory()) {
/* 1705 */             this.context.getResources().createWebResourceSet(WebResourceRoot.ResourceSetType.RESOURCE_JAR, "/", resources
/*      */             
/* 1707 */               .getAbsolutePath(), null, "/");
/*      */           }
/*      */         }
/*      */       } catch (IOException ioe) {
/* 1711 */         log.error(sm.getString("contextConfig.resourceJarFail", new Object[] { url, this.context
/* 1712 */           .getName() }));
/*      */       } catch (URISyntaxException e) {
/* 1714 */         log.error(sm.getString("contextConfig.resourceJarFail", new Object[] { url, this.context
/* 1715 */           .getName() }));
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
/*      */   protected InputSource getGlobalWebXmlSource()
/*      */   {
/* 1728 */     if ((this.defaultWebXml == null) && ((this.context instanceof StandardContext))) {
/* 1729 */       this.defaultWebXml = ((StandardContext)this.context).getDefaultWebXml();
/*      */     }
/*      */     
/* 1732 */     if (this.defaultWebXml == null) {
/* 1733 */       getDefaultWebXml();
/*      */     }
/*      */     
/*      */ 
/* 1737 */     if ("org/apache/catalina/startup/NO_DEFAULT_XML".equals(this.defaultWebXml)) {
/* 1738 */       return null;
/*      */     }
/* 1740 */     return getWebXmlSource(this.defaultWebXml, this.context
/* 1741 */       .getCatalinaBase().getPath());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected InputSource getHostWebXmlSource()
/*      */   {
/* 1750 */     File hostConfigBase = getHostConfigBase();
/* 1751 */     if (hostConfigBase == null) {
/* 1752 */       return null;
/*      */     }
/* 1754 */     return getWebXmlSource("web.xml.default", hostConfigBase.getPath());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected InputSource getContextWebXmlSource()
/*      */   {
/* 1763 */     InputStream stream = null;
/* 1764 */     source = null;
/* 1765 */     URL url = null;
/*      */     
/* 1767 */     String altDDName = null;
/*      */     
/*      */ 
/* 1770 */     ServletContext servletContext = this.context.getServletContext();
/*      */     try {
/* 1772 */       if (servletContext != null) {
/* 1773 */         altDDName = (String)servletContext.getAttribute("org.apache.catalina.deploy.alt_dd");
/* 1774 */         if (altDDName != null) {
/*      */           try {
/* 1776 */             stream = new FileInputStream(altDDName);
/* 1777 */             url = new File(altDDName).toURI().toURL();
/*      */           } catch (FileNotFoundException e) {
/* 1779 */             log.error(sm.getString("contextConfig.altDDNotFound", new Object[] { altDDName }));
/*      */           }
/*      */           catch (MalformedURLException e) {
/* 1782 */             log.error(sm.getString("contextConfig.applicationUrl"));
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 1787 */           stream = servletContext.getResourceAsStream("/WEB-INF/web.xml");
/*      */           try {
/* 1789 */             url = servletContext.getResource("/WEB-INF/web.xml");
/*      */           }
/*      */           catch (MalformedURLException e) {
/* 1792 */             log.error(sm.getString("contextConfig.applicationUrl"));
/*      */           }
/*      */         }
/*      */       }
/* 1796 */       if ((stream == null) || (url == null)) {
/* 1797 */         if (log.isDebugEnabled()) {
/* 1798 */           log.debug(sm.getString("contextConfig.applicationMissing") + " " + this.context);
/*      */         }
/*      */       } else {
/* 1801 */         source = new InputSource(url.toExternalForm());
/* 1802 */         source.setByteStream(stream);
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
/* 1814 */       return source;
/*      */     }
/*      */     finally
/*      */     {
/* 1805 */       if ((source == null) && (stream != null)) {
/*      */         try {
/* 1807 */           stream.close();
/*      */         }
/*      */         catch (IOException localIOException1) {}
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
/*      */ 
/*      */   protected InputSource getWebXmlSource(String filename, String path)
/*      */   {
/* 1825 */     File file = new File(filename);
/* 1826 */     if (!file.isAbsolute()) {
/* 1827 */       file = new File(path, filename);
/*      */     }
/*      */     
/* 1830 */     InputStream stream = null;
/* 1831 */     source = null;
/*      */     try
/*      */     {
/* 1834 */       if (!file.exists())
/*      */       {
/*      */ 
/* 1837 */         stream = getClass().getClassLoader().getResourceAsStream(filename);
/* 1838 */         if (stream != null)
/*      */         {
/*      */ 
/* 1841 */           source = new InputSource(getClass().getClassLoader().getResource(filename).toURI().toString());
/*      */         }
/*      */       } else {
/* 1844 */         source = new InputSource(file.getAbsoluteFile().toURI().toString());
/* 1845 */         stream = new FileInputStream(file);
/*      */       }
/*      */       
/* 1848 */       if ((stream != null) && (source != null)) {
/* 1849 */         source.setByteStream(stream);
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
/*      */ 
/*      */ 
/*      */ 
/* 1864 */       return source;
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/* 1852 */       log.error(sm.getString("contextConfig.defaultError", new Object[] { filename, file }), e);
/*      */     }
/*      */     finally {
/* 1855 */       if ((source == null) && (stream != null)) {
/*      */         try {
/* 1857 */           stream.close();
/*      */         }
/*      */         catch (IOException localIOException2) {}
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Map<String, WebXml> processJarsForWebFragments(WebXml application, WebXmlParser webXmlParser)
/*      */   {
/* 1882 */     JarScanner jarScanner = this.context.getJarScanner();
/* 1883 */     boolean delegate = false;
/* 1884 */     if ((this.context instanceof StandardContext)) {
/* 1885 */       delegate = ((StandardContext)this.context).getDelegate();
/*      */     }
/* 1887 */     boolean parseRequired = true;
/* 1888 */     Set<String> absoluteOrder = application.getAbsoluteOrdering();
/* 1889 */     if ((absoluteOrder != null) && (absoluteOrder.isEmpty()) && 
/* 1890 */       (!this.context.getXmlValidation()))
/*      */     {
/*      */ 
/* 1893 */       parseRequired = false;
/*      */     }
/* 1895 */     FragmentJarScannerCallback callback = new FragmentJarScannerCallback(webXmlParser, delegate, parseRequired);
/*      */     
/*      */ 
/* 1898 */     jarScanner.scan(JarScanType.PLUGGABILITY, this.context
/* 1899 */       .getServletContext(), callback);
/*      */     
/* 1901 */     if (!callback.isOk()) {
/* 1902 */       this.ok = false;
/*      */     }
/* 1904 */     return callback.getFragments();
/*      */   }
/*      */   
/*      */   protected void processAnnotations(Set<WebXml> fragments, boolean handlesTypesOnly, Map<String, JavaClassCacheEntry> javaClassCache)
/*      */   {
/* 1909 */     for (WebXml fragment : fragments)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1917 */       boolean htOnly = (handlesTypesOnly) || (!fragment.getWebappJar()) || (fragment.isMetadataComplete());
/*      */       
/* 1919 */       WebXml annotations = new WebXml();
/*      */       
/* 1921 */       annotations.setDistributable(true);
/* 1922 */       URL url = fragment.getURL();
/* 1923 */       processAnnotationsUrl(url, annotations, htOnly, javaClassCache);
/* 1924 */       Set<WebXml> set = new HashSet();
/* 1925 */       set.add(annotations);
/*      */       
/* 1927 */       fragment.merge(set);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void processAnnotationsWebResource(WebResource webResource, WebXml fragment, boolean handlesTypesOnly, Map<String, JavaClassCacheEntry> javaClassCache)
/*      */   {
/* 1935 */     if (webResource.isDirectory())
/*      */     {
/* 1937 */       WebResource[] webResources = webResource.getWebResourceRoot().listResources(webResource
/* 1938 */         .getWebappPath());
/* 1939 */       if (webResources.length > 0) {
/* 1940 */         if (log.isDebugEnabled()) {
/* 1941 */           log.debug(sm.getString("contextConfig.processAnnotationsWebDir.debug", new Object[] {webResource
/*      */           
/* 1943 */             .getURL() }));
/*      */         }
/* 1945 */         for (WebResource r : webResources) {
/* 1946 */           processAnnotationsWebResource(r, fragment, handlesTypesOnly, javaClassCache);
/*      */         }
/*      */       }
/* 1949 */     } else if ((webResource.isFile()) && 
/* 1950 */       (webResource.getName().endsWith(".class"))) {
/* 1951 */       try { InputStream is = webResource.getInputStream();??? = null;
/* 1952 */         try { processAnnotationsStream(is, fragment, handlesTypesOnly, javaClassCache);
/*      */         }
/*      */         catch (Throwable localThrowable4)
/*      */         {
/* 1951 */           ??? = localThrowable4;throw localThrowable4;
/*      */         } finally {
/* 1953 */           if (is != null) if (??? != null) try { is.close(); } catch (Throwable localThrowable2) { ((Throwable)???).addSuppressed(localThrowable2); } else is.close();
/* 1954 */         } } catch (IOException e) { log.error(sm.getString("contextConfig.inputStreamWebResource", new Object[] {webResource
/* 1955 */           .getWebappPath() }), e);
/*      */       } catch (ClassFormatException e) {
/* 1957 */         log.error(sm.getString("contextConfig.inputStreamWebResource", new Object[] {webResource
/* 1958 */           .getWebappPath() }), e);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected void processAnnotationsUrl(URL url, WebXml fragment, boolean handlesTypesOnly, Map<String, JavaClassCacheEntry> javaClassCache)
/*      */   {
/* 1966 */     if (url == null)
/*      */     {
/* 1968 */       return; }
/* 1969 */     if (("jar".equals(url.getProtocol())) || (url.toString().endsWith(".jar"))) {
/* 1970 */       processAnnotationsJar(url, fragment, handlesTypesOnly, javaClassCache);
/* 1971 */     } else if ("file".equals(url.getProtocol())) {
/*      */       try {
/* 1973 */         processAnnotationsFile(new File(url
/* 1974 */           .toURI()), fragment, handlesTypesOnly, javaClassCache);
/*      */       } catch (URISyntaxException e) {
/* 1976 */         log.error(sm.getString("contextConfig.fileUrl", new Object[] { url }), e);
/*      */       }
/*      */     } else {
/* 1979 */       log.error(sm.getString("contextConfig.unknownUrlProtocol", new Object[] {url
/* 1980 */         .getProtocol(), url }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected void processAnnotationsJar(URL url, WebXml fragment, boolean handlesTypesOnly, Map<String, JavaClassCacheEntry> javaClassCache)
/*      */   {
/*      */     try
/*      */     {
/* 1989 */       Jar jar = JarFactory.newInstance(url);Throwable localThrowable6 = null;
/* 1990 */       try { if (log.isDebugEnabled()) {
/* 1991 */           log.debug(sm.getString("contextConfig.processAnnotationsJar.debug", new Object[] { url }));
/*      */         }
/*      */         
/*      */ 
/* 1995 */         jar.nextEntry();
/* 1996 */         String entryName = jar.getEntryName();
/* 1997 */         while (entryName != null) {
/* 1998 */           if (entryName.endsWith(".class")) {
/* 1999 */             try { InputStream is = jar.getEntryInputStream();Throwable localThrowable7 = null;
/* 2000 */               try { processAnnotationsStream(is, fragment, handlesTypesOnly, javaClassCache);
/*      */               }
/*      */               catch (Throwable localThrowable1)
/*      */               {
/* 1999 */                 localThrowable7 = localThrowable1;throw localThrowable1;
/*      */               } finally {
/* 2001 */                 if (is != null) if (localThrowable7 != null) try { is.close(); } catch (Throwable localThrowable2) { localThrowable7.addSuppressed(localThrowable2); } else is.close();
/* 2002 */               } } catch (IOException e) { log.error(sm.getString("contextConfig.inputStreamJar", new Object[] { entryName, url }), e);
/*      */             }
/*      */             catch (ClassFormatException e) {
/* 2005 */               log.error(sm.getString("contextConfig.inputStreamJar", new Object[] { entryName, url }), e);
/*      */             }
/*      */           }
/*      */           
/* 2009 */           jar.nextEntry();
/* 2010 */           entryName = jar.getEntryName();
/*      */         }
/*      */       }
/*      */       catch (Throwable localThrowable4)
/*      */       {
/* 1989 */         localThrowable6 = localThrowable4;throw localThrowable4;
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
/*      */       }
/*      */       finally
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2012 */         if (jar != null) if (localThrowable6 != null) try { jar.close(); } catch (Throwable localThrowable5) { localThrowable6.addSuppressed(localThrowable5); } else jar.close();
/* 2013 */       } } catch (IOException e) { log.error(sm.getString("contextConfig.jarFile", new Object[] { url }), e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void processAnnotationsFile(File file, WebXml fragment, boolean handlesTypesOnly, Map<String, JavaClassCacheEntry> javaClassCache)
/*      */   {
/* 2021 */     if (file.isDirectory())
/*      */     {
/* 2023 */       String[] dirs = file.list();
/* 2024 */       if (dirs != null) {
/* 2025 */         if (log.isDebugEnabled()) {
/* 2026 */           log.debug(sm.getString("contextConfig.processAnnotationsDir.debug", new Object[] { file }));
/*      */         }
/*      */         
/* 2029 */         for (String dir : dirs) {
/* 2030 */           processAnnotationsFile(new File(file, dir), fragment, handlesTypesOnly, javaClassCache);
/*      */         }
/*      */       }
/*      */     }
/* 2034 */     else if ((file.getName().endsWith(".class")) && (file.canRead())) {
/* 2035 */       try { FileInputStream fis = new FileInputStream(file);??? = null;
/* 2036 */         try { processAnnotationsStream(fis, fragment, handlesTypesOnly, javaClassCache);
/*      */         }
/*      */         catch (Throwable localThrowable4)
/*      */         {
/* 2035 */           ??? = localThrowable4;throw localThrowable4;
/*      */         } finally {
/* 2037 */           if (fis != null) if (??? != null) try { fis.close(); } catch (Throwable localThrowable2) { ((Throwable)???).addSuppressed(localThrowable2); } else fis.close();
/* 2038 */         } } catch (IOException e) { log.error(sm.getString("contextConfig.inputStreamFile", new Object[] {file
/* 2039 */           .getAbsolutePath() }), e);
/*      */       } catch (ClassFormatException e) {
/* 2041 */         log.error(sm.getString("contextConfig.inputStreamFile", new Object[] {file
/* 2042 */           .getAbsolutePath() }), e);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void processAnnotationsStream(InputStream is, WebXml fragment, boolean handlesTypesOnly, Map<String, JavaClassCacheEntry> javaClassCache)
/*      */     throws ClassFormatException, IOException
/*      */   {
/* 2052 */     ClassParser parser = new ClassParser(is);
/* 2053 */     JavaClass clazz = parser.parse();
/* 2054 */     checkHandlesTypes(clazz, javaClassCache);
/*      */     
/* 2056 */     if (handlesTypesOnly) {
/* 2057 */       return;
/*      */     }
/*      */     
/* 2060 */     AnnotationEntry[] annotationsEntries = clazz.getAnnotationEntries();
/* 2061 */     if (annotationsEntries != null) {
/* 2062 */       String className = clazz.getClassName();
/* 2063 */       for (AnnotationEntry ae : annotationsEntries) {
/* 2064 */         String type = ae.getAnnotationType();
/* 2065 */         if ("Ljavax/servlet/annotation/WebServlet;".equals(type)) {
/* 2066 */           processAnnotationWebServlet(className, ae, fragment);
/* 2067 */         } else if ("Ljavax/servlet/annotation/WebFilter;".equals(type)) {
/* 2068 */           processAnnotationWebFilter(className, ae, fragment);
/* 2069 */         } else if ("Ljavax/servlet/annotation/WebListener;".equals(type)) {
/* 2070 */           fragment.addListener(className);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void checkHandlesTypes(JavaClass javaClass, Map<String, JavaClassCacheEntry> javaClassCache)
/*      */   {
/* 2089 */     if (this.typeInitializerMap.size() == 0) {
/* 2090 */       return;
/*      */     }
/*      */     
/* 2093 */     if ((javaClass.getAccessFlags() & 0x2000) != 0)
/*      */     {
/*      */ 
/* 2096 */       return;
/*      */     }
/*      */     
/* 2099 */     String className = javaClass.getClassName();
/*      */     
/* 2101 */     Class<?> clazz = null;
/* 2102 */     if (this.handlesTypesNonAnnotations)
/*      */     {
/* 2104 */       populateJavaClassCache(className, javaClass, javaClassCache);
/* 2105 */       JavaClassCacheEntry entry = (JavaClassCacheEntry)javaClassCache.get(className);
/* 2106 */       if (entry.getSciSet() == null) {
/*      */         try {
/* 2108 */           populateSCIsForCacheEntry(entry, javaClassCache);
/*      */         } catch (StackOverflowError soe) {
/* 2110 */           throw new IllegalStateException(sm.getString("contextConfig.annotationsStackOverflow", new Object[] {this.context
/*      */           
/* 2112 */             .getName(), 
/* 2113 */             classHierarchyToString(className, entry, javaClassCache) }));
/*      */         }
/*      */       }
/* 2116 */       if (!entry.getSciSet().isEmpty())
/*      */       {
/* 2118 */         clazz = Introspection.loadClass(this.context, className);
/* 2119 */         if (clazz == null)
/*      */         {
/* 2121 */           return;
/*      */         }
/*      */         
/* 2124 */         for (ServletContainerInitializer sci : entry.getSciSet()) {
/* 2125 */           Set<Class<?>> classes = (Set)this.initializerClassMap.get(sci);
/* 2126 */           if (classes == null) {
/* 2127 */             classes = new HashSet();
/* 2128 */             this.initializerClassMap.put(sci, classes);
/*      */           }
/* 2130 */           classes.add(clazz);
/*      */         }
/*      */       }
/*      */     }
/*      */     AnnotationEntry[] annotationEntries;
/* 2135 */     if (this.handlesTypesAnnotations) {
/* 2136 */       annotationEntries = javaClass.getAnnotationEntries();
/* 2137 */       if (annotationEntries != null)
/*      */       {
/* 2139 */         for (Map.Entry<Class<?>, Set<ServletContainerInitializer>> entry : this.typeInitializerMap.entrySet()) {
/* 2140 */           if (((Class)entry.getKey()).isAnnotation()) {
/* 2141 */             String entryClassName = ((Class)entry.getKey()).getName();
/* 2142 */             for (AnnotationEntry annotationEntry : annotationEntries) {
/* 2143 */               if (entryClassName.equals(
/* 2144 */                 getClassName(annotationEntry.getAnnotationType()))) {
/* 2145 */                 if (clazz == null) {
/* 2146 */                   clazz = Introspection.loadClass(this.context, className);
/*      */                   
/* 2148 */                   if (clazz == null)
/*      */                   {
/*      */ 
/* 2151 */                     return;
/*      */                   }
/*      */                 }
/* 2154 */                 for (ServletContainerInitializer sci : (Set)entry.getValue()) {
/* 2155 */                   ((Set)this.initializerClassMap.get(sci)).add(clazz);
/*      */                 }
/* 2157 */                 break;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private String classHierarchyToString(String className, JavaClassCacheEntry entry, Map<String, JavaClassCacheEntry> javaClassCache)
/*      */   {
/* 2169 */     JavaClassCacheEntry start = entry;
/* 2170 */     StringBuilder msg = new StringBuilder(className);
/* 2171 */     msg.append("->");
/*      */     
/* 2173 */     String parentName = entry.getSuperclassName();
/* 2174 */     JavaClassCacheEntry parent = (JavaClassCacheEntry)javaClassCache.get(parentName);
/* 2175 */     int count = 0;
/*      */     
/* 2177 */     while ((count < 100) && (parent != null) && (parent != start)) {
/* 2178 */       msg.append(parentName);
/* 2179 */       msg.append("->");
/*      */       
/* 2181 */       count++;
/* 2182 */       parentName = parent.getSuperclassName();
/* 2183 */       parent = (JavaClassCacheEntry)javaClassCache.get(parentName);
/*      */     }
/*      */     
/* 2186 */     msg.append(parentName);
/*      */     
/* 2188 */     return msg.toString();
/*      */   }
/*      */   
/*      */   private void populateJavaClassCache(String className, JavaClass javaClass, Map<String, JavaClassCacheEntry> javaClassCache)
/*      */   {
/* 2193 */     if (javaClassCache.containsKey(className)) {
/* 2194 */       return;
/*      */     }
/*      */     
/*      */ 
/* 2198 */     javaClassCache.put(className, new JavaClassCacheEntry(javaClass));
/*      */     
/* 2200 */     populateJavaClassCache(javaClass.getSuperclassName(), javaClassCache);
/*      */     
/* 2202 */     for (String interfaceName : javaClass.getInterfaceNames()) {
/* 2203 */       populateJavaClassCache(interfaceName, javaClassCache);
/*      */     }
/*      */   }
/*      */   
/*      */   private void populateJavaClassCache(String className, Map<String, JavaClassCacheEntry> javaClassCache)
/*      */   {
/* 2209 */     if (!javaClassCache.containsKey(className)) {
/* 2210 */       String name = className.replace('.', '/') + ".class";
/* 2211 */       try { InputStream is = this.context.getLoader().getClassLoader().getResourceAsStream(name);Throwable localThrowable4 = null;
/* 2212 */         try { if (is == null) {
/* 2213 */             return;
/*      */           }
/* 2215 */           ClassParser parser = new ClassParser(is);
/* 2216 */           JavaClass clazz = parser.parse();
/* 2217 */           populateJavaClassCache(clazz.getClassName(), clazz, javaClassCache);
/*      */         }
/*      */         catch (Throwable localThrowable2)
/*      */         {
/* 2211 */           localThrowable4 = localThrowable2;throw localThrowable2;
/*      */ 
/*      */ 
/*      */         }
/*      */         finally
/*      */         {
/*      */ 
/* 2218 */           if (is != null) if (localThrowable4 != null) try { is.close(); } catch (Throwable localThrowable3) { localThrowable4.addSuppressed(localThrowable3); } else is.close();
/* 2219 */         } } catch (ClassFormatException e) { log.debug(sm.getString("contextConfig.invalidSciHandlesTypes", new Object[] { className }), e);
/*      */       }
/*      */       catch (IOException e) {
/* 2222 */         log.debug(sm.getString("contextConfig.invalidSciHandlesTypes", new Object[] { className }), e);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void populateSCIsForCacheEntry(JavaClassCacheEntry cacheEntry, Map<String, JavaClassCacheEntry> javaClassCache)
/*      */   {
/* 2230 */     Set<ServletContainerInitializer> result = new HashSet();
/*      */     
/*      */ 
/* 2233 */     String superClassName = cacheEntry.getSuperclassName();
/*      */     
/* 2235 */     JavaClassCacheEntry superClassCacheEntry = (JavaClassCacheEntry)javaClassCache.get(superClassName);
/*      */     
/*      */ 
/* 2238 */     if (cacheEntry.equals(superClassCacheEntry)) {
/* 2239 */       cacheEntry.setSciSet(EMPTY_SCI_SET);
/* 2240 */       return;
/*      */     }
/*      */     
/*      */ 
/* 2244 */     if (superClassCacheEntry != null) {
/* 2245 */       if (superClassCacheEntry.getSciSet() == null) {
/* 2246 */         populateSCIsForCacheEntry(superClassCacheEntry, javaClassCache);
/*      */       }
/* 2248 */       result.addAll(superClassCacheEntry.getSciSet());
/*      */     }
/* 2250 */     result.addAll(getSCIsForClass(superClassName));
/*      */     
/*      */ 
/* 2253 */     for (String interfaceName : cacheEntry.getInterfaceNames())
/*      */     {
/* 2255 */       JavaClassCacheEntry interfaceEntry = (JavaClassCacheEntry)javaClassCache.get(interfaceName);
/*      */       
/*      */ 
/*      */ 
/* 2259 */       if (interfaceEntry != null) {
/* 2260 */         if (interfaceEntry.getSciSet() == null) {
/* 2261 */           populateSCIsForCacheEntry(interfaceEntry, javaClassCache);
/*      */         }
/* 2263 */         result.addAll(interfaceEntry.getSciSet());
/*      */       }
/* 2265 */       result.addAll(getSCIsForClass(interfaceName));
/*      */     }
/*      */     
/* 2268 */     cacheEntry.setSciSet(result.isEmpty() ? EMPTY_SCI_SET : result);
/*      */   }
/*      */   
/*      */   private Set<ServletContainerInitializer> getSCIsForClass(String className)
/*      */   {
/* 2273 */     for (Map.Entry<Class<?>, Set<ServletContainerInitializer>> entry : this.typeInitializerMap.entrySet()) {
/* 2274 */       Class<?> clazz = (Class)entry.getKey();
/* 2275 */       if ((!clazz.isAnnotation()) && 
/* 2276 */         (clazz.getName().equals(className))) {
/* 2277 */         return (Set)entry.getValue();
/*      */       }
/*      */     }
/*      */     
/* 2281 */     return EMPTY_SCI_SET;
/*      */   }
/*      */   
/*      */   private static final String getClassName(String internalForm) {
/* 2285 */     if (!internalForm.startsWith("L")) {
/* 2286 */       return internalForm;
/*      */     }
/*      */     
/*      */ 
/* 2290 */     return 
/* 2291 */       internalForm.substring(1, internalForm.length() - 1).replace('/', '.');
/*      */   }
/*      */   
/*      */   protected void processAnnotationWebServlet(String className, AnnotationEntry ae, WebXml fragment)
/*      */   {
/* 2296 */     String servletName = null;
/*      */     
/* 2298 */     List<ElementValuePair> evps = ae.getElementValuePairs();
/* 2299 */     for (ElementValuePair evp : evps) {
/* 2300 */       String name = evp.getNameString();
/* 2301 */       if ("name".equals(name)) {
/* 2302 */         servletName = evp.getValue().stringifyValue();
/* 2303 */         break;
/*      */       }
/*      */     }
/* 2306 */     if (servletName == null)
/*      */     {
/* 2308 */       servletName = className;
/*      */     }
/* 2310 */     ServletDef servletDef = (ServletDef)fragment.getServlets().get(servletName);
/*      */     boolean isWebXMLservletDef;
/*      */     boolean isWebXMLservletDef;
/* 2313 */     if (servletDef == null) {
/* 2314 */       servletDef = new ServletDef();
/* 2315 */       servletDef.setServletName(servletName);
/* 2316 */       servletDef.setServletClass(className);
/* 2317 */       isWebXMLservletDef = false;
/*      */     } else {
/* 2319 */       isWebXMLservletDef = true;
/*      */     }
/*      */     
/* 2322 */     boolean urlPatternsSet = false;
/* 2323 */     String[] urlPatterns = null;
/*      */     
/*      */ 
/* 2326 */     for (Object localObject1 = evps.iterator(); ((Iterator)localObject1).hasNext();) { evp = (ElementValuePair)((Iterator)localObject1).next();
/* 2327 */       name = evp.getNameString();
/* 2328 */       if (("value".equals(name)) || ("urlPatterns".equals(name))) {
/* 2329 */         if (urlPatternsSet) {
/* 2330 */           throw new IllegalArgumentException(sm.getString("contextConfig.urlPatternValue", new Object[] { "WebServlet", className }));
/*      */         }
/*      */         
/* 2333 */         urlPatternsSet = true;
/* 2334 */         urlPatterns = processAnnotationsStringArray(evp.getValue());
/* 2335 */       } else if ("description".equals(name)) {
/* 2336 */         if (servletDef.getDescription() == null) {
/* 2337 */           servletDef.setDescription(evp.getValue().stringifyValue());
/*      */         }
/* 2339 */       } else if ("displayName".equals(name)) {
/* 2340 */         if (servletDef.getDisplayName() == null) {
/* 2341 */           servletDef.setDisplayName(evp.getValue().stringifyValue());
/*      */         }
/* 2343 */       } else if ("largeIcon".equals(name)) {
/* 2344 */         if (servletDef.getLargeIcon() == null) {
/* 2345 */           servletDef.setLargeIcon(evp.getValue().stringifyValue());
/*      */         }
/* 2347 */       } else if ("smallIcon".equals(name)) {
/* 2348 */         if (servletDef.getSmallIcon() == null) {
/* 2349 */           servletDef.setSmallIcon(evp.getValue().stringifyValue());
/*      */         }
/* 2351 */       } else if ("asyncSupported".equals(name)) {
/* 2352 */         if (servletDef.getAsyncSupported() == null) {
/* 2353 */           servletDef.setAsyncSupported(evp.getValue()
/* 2354 */             .stringifyValue());
/*      */         }
/* 2356 */       } else if ("loadOnStartup".equals(name)) {
/* 2357 */         if (servletDef.getLoadOnStartup() == null)
/*      */         {
/* 2359 */           servletDef.setLoadOnStartup(evp.getValue().stringifyValue());
/*      */         }
/* 2361 */       } else if ("initParams".equals(name)) {
/* 2362 */         Map<String, String> initParams = processAnnotationWebInitParams(evp
/* 2363 */           .getValue());
/* 2364 */         if (isWebXMLservletDef)
/*      */         {
/* 2366 */           webXMLInitParams = servletDef.getParameterMap();
/* 2367 */           for (Map.Entry<String, String> entry : initParams
/* 2368 */             .entrySet()) {
/* 2369 */             if (webXMLInitParams.get(entry.getKey()) == null) {
/* 2370 */               servletDef.addInitParameter((String)entry.getKey(), 
/* 2371 */                 (String)entry.getValue());
/*      */             }
/*      */           }
/*      */         } else {
/* 2375 */           for (Object entry : initParams
/* 2376 */             .entrySet())
/* 2377 */             servletDef.addInitParameter((String)((Map.Entry)entry).getKey(), 
/* 2378 */               (String)((Map.Entry)entry).getValue());
/*      */         } } }
/*      */     ElementValuePair evp;
/*      */     String name;
/*      */     Map<String, String> webXMLInitParams;
/* 2383 */     if ((!isWebXMLservletDef) && (urlPatterns != null)) {
/* 2384 */       fragment.addServlet(servletDef);
/*      */     }
/* 2386 */     if ((urlPatterns != null) && 
/* 2387 */       (!fragment.getServletMappings().containsValue(servletName))) {
/* 2388 */       localObject1 = urlPatterns;evp = localObject1.length; for (name = 0; name < evp; name++) { String urlPattern = localObject1[name];
/* 2389 */         fragment.addServletMapping(urlPattern, servletName);
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
/*      */ 
/*      */ 
/*      */   protected void processAnnotationWebFilter(String className, AnnotationEntry ae, WebXml fragment)
/*      */   {
/* 2406 */     String filterName = null;
/*      */     
/* 2408 */     List<ElementValuePair> evps = ae.getElementValuePairs();
/* 2409 */     for (ElementValuePair evp : evps) {
/* 2410 */       String name = evp.getNameString();
/* 2411 */       if ("filterName".equals(name)) {
/* 2412 */         filterName = evp.getValue().stringifyValue();
/* 2413 */         break;
/*      */       }
/*      */     }
/* 2416 */     if (filterName == null)
/*      */     {
/* 2418 */       filterName = className;
/*      */     }
/* 2420 */     FilterDef filterDef = (FilterDef)fragment.getFilters().get(filterName);
/* 2421 */     FilterMap filterMap = new FilterMap();
/*      */     boolean isWebXMLfilterDef;
/*      */     boolean isWebXMLfilterDef;
/* 2424 */     if (filterDef == null) {
/* 2425 */       filterDef = new FilterDef();
/* 2426 */       filterDef.setFilterName(filterName);
/* 2427 */       filterDef.setFilterClass(className);
/* 2428 */       isWebXMLfilterDef = false;
/*      */     } else {
/* 2430 */       isWebXMLfilterDef = true;
/*      */     }
/*      */     
/* 2433 */     boolean urlPatternsSet = false;
/* 2434 */     boolean servletNamesSet = false;
/* 2435 */     boolean dispatchTypesSet = false;
/* 2436 */     String[] urlPatterns = null;
/*      */     
/* 2438 */     for (ElementValuePair evp : evps) {
/* 2439 */       name = evp.getNameString();
/* 2440 */       String str1; String urlPattern; if (("value".equals(name)) || ("urlPatterns".equals(name))) {
/* 2441 */         if (urlPatternsSet) {
/* 2442 */           throw new IllegalArgumentException(sm.getString("contextConfig.urlPatternValue", new Object[] { "WebFilter", className }));
/*      */         }
/*      */         
/* 2445 */         urlPatterns = processAnnotationsStringArray(evp.getValue());
/* 2446 */         urlPatternsSet = urlPatterns.length > 0;
/* 2447 */         String[] arrayOfString1 = urlPatterns;int i = arrayOfString1.length; for (str1 = 0; str1 < i; str1++) { urlPattern = arrayOfString1[str1];
/*      */           
/* 2449 */           filterMap.addURLPattern(urlPattern);
/*      */         } } else { String[] arrayOfString2;
/* 2451 */         if ("servletNames".equals(name)) {
/* 2452 */           String[] servletNames = processAnnotationsStringArray(evp
/* 2453 */             .getValue());
/* 2454 */           servletNamesSet = servletNames.length > 0;
/* 2455 */           arrayOfString2 = servletNames;str1 = arrayOfString2.length; for (urlPattern = 0; urlPattern < str1; urlPattern++) { String servletName = arrayOfString2[urlPattern];
/* 2456 */             filterMap.addServletName(servletName);
/*      */           } } else { Object localObject2;
/* 2458 */           if ("dispatcherTypes".equals(name)) {
/* 2459 */             String[] dispatcherTypes = processAnnotationsStringArray(evp
/* 2460 */               .getValue());
/* 2461 */             dispatchTypesSet = dispatcherTypes.length > 0;
/* 2462 */             arrayOfString2 = dispatcherTypes;localObject2 = arrayOfString2.length; for (urlPattern = 0; urlPattern < localObject2; urlPattern++) { String dispatcherType = arrayOfString2[urlPattern];
/* 2463 */               filterMap.setDispatcher(dispatcherType);
/*      */             }
/* 2465 */           } else if ("description".equals(name)) {
/* 2466 */             if (filterDef.getDescription() == null) {
/* 2467 */               filterDef.setDescription(evp.getValue().stringifyValue());
/*      */             }
/* 2469 */           } else if ("displayName".equals(name)) {
/* 2470 */             if (filterDef.getDisplayName() == null) {
/* 2471 */               filterDef.setDisplayName(evp.getValue().stringifyValue());
/*      */             }
/* 2473 */           } else if ("largeIcon".equals(name)) {
/* 2474 */             if (filterDef.getLargeIcon() == null) {
/* 2475 */               filterDef.setLargeIcon(evp.getValue().stringifyValue());
/*      */             }
/* 2477 */           } else if ("smallIcon".equals(name)) {
/* 2478 */             if (filterDef.getSmallIcon() == null) {
/* 2479 */               filterDef.setSmallIcon(evp.getValue().stringifyValue());
/*      */             }
/* 2481 */           } else if ("asyncSupported".equals(name)) {
/* 2482 */             if (filterDef.getAsyncSupported() == null)
/*      */             {
/* 2484 */               filterDef.setAsyncSupported(evp.getValue().stringifyValue());
/*      */             }
/* 2486 */           } else if ("initParams".equals(name)) {
/* 2487 */             Object initParams = processAnnotationWebInitParams(evp
/* 2488 */               .getValue());
/* 2489 */             if (isWebXMLfilterDef)
/*      */             {
/* 2491 */               webXMLInitParams = filterDef.getParameterMap();
/* 2492 */               for (localObject2 = ((Map)initParams)
/* 2493 */                     .entrySet().iterator(); ((Iterator)localObject2).hasNext();) { Map.Entry<String, String> entry = (Map.Entry)((Iterator)localObject2).next();
/*      */                 
/* 2494 */                 if (((Map)webXMLInitParams).get(entry.getKey()) == null) {
/* 2495 */                   filterDef.addInitParameter((String)entry.getKey(), 
/* 2496 */                     (String)entry.getValue());
/*      */                 }
/*      */               }
/*      */             } else {
/* 2500 */               for (webXMLInitParams = ((Map)initParams)
/* 2501 */                     .entrySet().iterator(); ((Iterator)webXMLInitParams).hasNext();) { entry = (Map.Entry)((Iterator)webXMLInitParams).next();
/*      */                 
/* 2502 */                 filterDef.addInitParameter((String)((Map.Entry)entry).getKey(), 
/* 2503 */                   (String)((Map.Entry)entry).getValue());
/*      */               }
/*      */             } } } } }
/*      */     String name;
/*      */     Object webXMLInitParams;
/*      */     Object entry;
/* 2509 */     if (!isWebXMLfilterDef) {
/* 2510 */       fragment.addFilter(filterDef);
/* 2511 */       if ((urlPatternsSet) || (servletNamesSet)) {
/* 2512 */         filterMap.setFilterName(filterName);
/* 2513 */         fragment.addFilterMapping(filterMap);
/*      */       }
/*      */     }
/* 2516 */     if ((urlPatternsSet) || (dispatchTypesSet)) {
/* 2517 */       Object fmap = fragment.getFilterMappings();
/* 2518 */       FilterMap descMap = null;
/* 2519 */       for (name = ((Set)fmap).iterator(); name.hasNext();) { map = (FilterMap)name.next();
/* 2520 */         if (filterName.equals(((FilterMap)map).getFilterName())) {
/* 2521 */           descMap = (FilterMap)map;
/* 2522 */           break;
/*      */         } }
/*      */       Object map;
/* 2525 */       if (descMap != null) {
/* 2526 */         String[] urlsPatterns = descMap.getURLPatterns();
/* 2527 */         Object localObject1; String urlPattern; if ((urlPatternsSet) && ((urlsPatterns == null) || (urlsPatterns.length == 0)))
/*      */         {
/* 2529 */           map = filterMap.getURLPatterns();localObject1 = map.length; for (Object localObject3 = 0; localObject3 < localObject1; localObject3++) { urlPattern = map[localObject3];
/*      */             
/* 2531 */             descMap.addURLPattern(urlPattern);
/*      */           }
/*      */         }
/* 2534 */         String[] dispatcherNames = descMap.getDispatcherNames();
/* 2535 */         if ((dispatchTypesSet) && ((dispatcherNames == null) || (dispatcherNames.length == 0)))
/*      */         {
/* 2537 */           localObject1 = filterMap.getDispatcherNames();String str2 = localObject1.length; for (urlPattern = 0; urlPattern < str2; urlPattern++) { String dis = localObject1[urlPattern];
/* 2538 */             descMap.setDispatcher(dis);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected String[] processAnnotationsStringArray(ElementValue ev)
/*      */   {
/* 2547 */     ArrayList<String> values = new ArrayList();
/* 2548 */     if ((ev instanceof ArrayElementValue))
/*      */     {
/* 2550 */       ElementValue[] arrayValues = ((ArrayElementValue)ev).getElementValuesArray();
/* 2551 */       for (ElementValue value : arrayValues) {
/* 2552 */         values.add(value.stringifyValue());
/*      */       }
/*      */     } else {
/* 2555 */       values.add(ev.stringifyValue());
/*      */     }
/* 2557 */     String[] result = new String[values.size()];
/* 2558 */     return (String[])values.toArray(result);
/*      */   }
/*      */   
/*      */   protected Map<String, String> processAnnotationWebInitParams(ElementValue ev)
/*      */   {
/* 2563 */     Map<String, String> result = new HashMap();
/* 2564 */     if ((ev instanceof ArrayElementValue))
/*      */     {
/* 2566 */       ElementValue[] arrayValues = ((ArrayElementValue)ev).getElementValuesArray();
/* 2567 */       for (ElementValue value : arrayValues) {
/* 2568 */         if ((value instanceof AnnotationElementValue))
/*      */         {
/* 2570 */           List<ElementValuePair> evps = ((AnnotationElementValue)value).getAnnotationEntry().getElementValuePairs();
/* 2571 */           String initParamName = null;
/* 2572 */           String initParamValue = null;
/* 2573 */           for (ElementValuePair evp : evps) {
/* 2574 */             if ("name".equals(evp.getNameString())) {
/* 2575 */               initParamName = evp.getValue().stringifyValue();
/* 2576 */             } else if ("value".equals(evp.getNameString())) {
/* 2577 */               initParamValue = evp.getValue().stringifyValue();
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 2582 */           result.put(initParamName, initParamValue);
/*      */         }
/*      */       }
/*      */     }
/* 2586 */     return result;
/*      */   }
/*      */   
/*      */   private static class DefaultWebXmlCacheEntry
/*      */   {
/*      */     private final WebXml webXml;
/*      */     private final long globalTimeStamp;
/*      */     private final long hostTimeStamp;
/*      */     
/*      */     public DefaultWebXmlCacheEntry(WebXml webXml, long globalTimeStamp, long hostTimeStamp) {
/* 2596 */       this.webXml = webXml;
/* 2597 */       this.globalTimeStamp = globalTimeStamp;
/* 2598 */       this.hostTimeStamp = hostTimeStamp;
/*      */     }
/*      */     
/*      */     public WebXml getWebXml() {
/* 2602 */       return this.webXml;
/*      */     }
/*      */     
/*      */     public long getGlobalTimeStamp() {
/* 2606 */       return this.globalTimeStamp;
/*      */     }
/*      */     
/*      */     public long getHostTimeStamp() {
/* 2610 */       return this.hostTimeStamp;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   static class JavaClassCacheEntry
/*      */   {
/*      */     public final String superclassName;
/*      */     public final String[] interfaceNames;
/* 2619 */     private Set<ServletContainerInitializer> sciSet = null;
/*      */     
/*      */     public JavaClassCacheEntry(JavaClass javaClass) {
/* 2622 */       this.superclassName = javaClass.getSuperclassName();
/* 2623 */       this.interfaceNames = javaClass.getInterfaceNames();
/*      */     }
/*      */     
/*      */     public String getSuperclassName() {
/* 2627 */       return this.superclassName;
/*      */     }
/*      */     
/*      */     public String[] getInterfaceNames() {
/* 2631 */       return this.interfaceNames;
/*      */     }
/*      */     
/*      */     public Set<ServletContainerInitializer> getSciSet() {
/* 2635 */       return this.sciSet;
/*      */     }
/*      */     
/*      */     public void setSciSet(Set<ServletContainerInitializer> sciSet) {
/* 2639 */       this.sciSet = sciSet;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\ContextConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */