/*     */ package org.apache.catalina.core;
/*     */ 
/*     */ import java.awt.Toolkit;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.Security;
/*     */ import java.sql.DriverManager;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.apache.catalina.LifecycleEvent;
/*     */ import org.apache.catalina.LifecycleListener;
/*     */ import org.apache.catalina.startup.SafeForkJoinWorkerThreadFactory;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
/*     */ import org.apache.tomcat.util.compat.JreCompat;
/*     */ import org.apache.tomcat.util.compat.JreVendor;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.ls.DOMImplementationLS;
/*     */ import org.w3c.dom.ls.LSSerializer;
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
/*     */ public class JreMemoryLeakPreventionListener
/*     */   implements LifecycleListener
/*     */ {
/*  63 */   private static final Log log = LogFactory.getLog(JreMemoryLeakPreventionListener.class);
/*     */   
/*  65 */   private static final StringManager sm = StringManager.getManager("org.apache.catalina.core");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String FORK_JOIN_POOL_THREAD_FACTORY_PROPERTY = "java.util.concurrent.ForkJoinPool.common.threadFactory";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  77 */   private boolean appContextProtection = false;
/*  78 */   public boolean isAppContextProtection() { return this.appContextProtection; }
/*     */   
/*  80 */   public void setAppContextProtection(boolean appContextProtection) { this.appContextProtection = appContextProtection; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  89 */   private boolean awtThreadProtection = false;
/*  90 */   public boolean isAWTThreadProtection() { return this.awtThreadProtection; }
/*     */   
/*  92 */   public void setAWTThreadProtection(boolean awtThreadProtection) { this.awtThreadProtection = awtThreadProtection; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 102 */   private boolean gcDaemonProtection = true;
/* 103 */   public boolean isGcDaemonProtection() { return this.gcDaemonProtection; }
/*     */   
/* 105 */   public void setGcDaemonProtection(boolean gcDaemonProtection) { this.gcDaemonProtection = gcDaemonProtection; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 114 */   private boolean securityPolicyProtection = true;
/*     */   
/* 116 */   public boolean isSecurityPolicyProtection() { return this.securityPolicyProtection; }
/*     */   
/*     */   public void setSecurityPolicyProtection(boolean securityPolicyProtection) {
/* 119 */     this.securityPolicyProtection = securityPolicyProtection;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 128 */   private boolean securityLoginConfigurationProtection = true;
/*     */   
/* 130 */   public boolean isSecurityLoginConfigurationProtection() { return this.securityLoginConfigurationProtection; }
/*     */   
/*     */   public void setSecurityLoginConfigurationProtection(boolean securityLoginConfigurationProtection)
/*     */   {
/* 134 */     this.securityLoginConfigurationProtection = securityLoginConfigurationProtection;
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
/* 146 */   private boolean tokenPollerProtection = true;
/* 147 */   public boolean isTokenPollerProtection() { return this.tokenPollerProtection; }
/*     */   
/* 149 */   public void setTokenPollerProtection(boolean tokenPollerProtection) { this.tokenPollerProtection = tokenPollerProtection; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 158 */   private boolean urlCacheProtection = true;
/* 159 */   public boolean isUrlCacheProtection() { return this.urlCacheProtection; }
/*     */   
/* 161 */   public void setUrlCacheProtection(boolean urlCacheProtection) { this.urlCacheProtection = urlCacheProtection; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 171 */   private boolean xmlParsingProtection = true;
/* 172 */   public boolean isXmlParsingProtection() { return this.xmlParsingProtection; }
/*     */   
/* 174 */   public void setXmlParsingProtection(boolean xmlParsingProtection) { this.xmlParsingProtection = xmlParsingProtection; }
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
/* 185 */   private boolean ldapPoolProtection = true;
/* 186 */   public boolean isLdapPoolProtection() { return this.ldapPoolProtection; }
/*     */   
/* 188 */   public void setLdapPoolProtection(boolean ldapPoolProtection) { this.ldapPoolProtection = ldapPoolProtection; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 197 */   private boolean driverManagerProtection = true;
/*     */   
/* 199 */   public boolean isDriverManagerProtection() { return this.driverManagerProtection; }
/*     */   
/*     */   public void setDriverManagerProtection(boolean driverManagerProtection) {
/* 202 */     this.driverManagerProtection = driverManagerProtection;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 210 */   private boolean forkJoinCommonPoolProtection = true;
/*     */   
/* 212 */   public boolean getForkJoinCommonPoolProtection() { return this.forkJoinCommonPoolProtection; }
/*     */   
/*     */   public void setForkJoinCommonPoolProtection(boolean forkJoinCommonPoolProtection) {
/* 215 */     this.forkJoinCommonPoolProtection = forkJoinCommonPoolProtection;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 223 */   private String classesToInitialize = null;
/*     */   
/* 225 */   public String getClassesToInitialize() { return this.classesToInitialize; }
/*     */   
/*     */   public void setClassesToInitialize(String classesToInitialize) {
/* 228 */     this.classesToInitialize = classesToInitialize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void lifecycleEvent(LifecycleEvent event)
/*     */   {
/* 236 */     if ("before_init".equals(event.getType()))
/*     */     {
/* 238 */       ClassLoader loader = Thread.currentThread().getContextClassLoader();
/*     */       
/*     */ 
/*     */ 
/*     */       try
/*     */       {
/* 244 */         Thread.currentThread().setContextClassLoader(
/* 245 */           ClassLoader.getSystemClassLoader());
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 251 */         if (this.driverManagerProtection) {
/* 252 */           DriverManager.getDrivers();
/*     */         }
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
/* 277 */         if ((this.appContextProtection) && (!JreCompat.isJre8Available())) {
/* 278 */           ImageIO.getCacheDirectory();
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 283 */         if ((this.awtThreadProtection) && (!JreCompat.isJre9Available())) {
/* 284 */           Toolkit.getDefaultToolkit();
/*     */         }
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
/* 300 */         if ((this.gcDaemonProtection) && (!JreCompat.isJre9Available())) {
/*     */           try {
/* 302 */             Class<?> clazz = Class.forName("sun.misc.GC");
/* 303 */             Method method = clazz.getDeclaredMethod("requestLatency", new Class[] { Long.TYPE });
/*     */             
/*     */ 
/* 306 */             method.invoke(null, new Object[] { Long.valueOf(9223372036854775806L) });
/*     */           } catch (ClassNotFoundException e) {
/* 308 */             if (JreVendor.IS_ORACLE_JVM) {
/* 309 */               log.error(sm.getString("jreLeakListener.gcDaemonFail"), e);
/*     */             }
/*     */             else {
/* 312 */               log.debug(sm.getString("jreLeakListener.gcDaemonFail"), e);
/*     */             }
/*     */           }
/*     */           catch (SecurityException|NoSuchMethodException|IllegalArgumentException|IllegalAccessException e)
/*     */           {
/* 317 */             log.error(sm.getString("jreLeakListener.gcDaemonFail"), e);
/*     */           }
/*     */           catch (InvocationTargetException e) {
/* 320 */             ExceptionUtils.handleThrowable(e.getCause());
/* 321 */             log.error(sm.getString("jreLeakListener.gcDaemonFail"), e);
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 330 */         if ((this.securityPolicyProtection) && (!JreCompat.isJre8Available()))
/*     */         {
/*     */           try
/*     */           {
/* 334 */             Class<?> policyClass = Class.forName("javax.security.auth.Policy");
/* 335 */             Method method = policyClass.getMethod("getPolicy", new Class[0]);
/* 336 */             method.invoke(null, new Object[0]);
/*     */ 
/*     */ 
/*     */           }
/*     */           catch (ClassNotFoundException localClassNotFoundException1) {}catch (SecurityException localSecurityException) {}catch (NoSuchMethodException e)
/*     */           {
/*     */ 
/* 343 */             log.warn(sm.getString("jreLeakListener.authPolicyFail"), e);
/*     */           }
/*     */           catch (IllegalArgumentException e) {
/* 346 */             log.warn(sm.getString("jreLeakListener.authPolicyFail"), e);
/*     */           }
/*     */           catch (IllegalAccessException e) {
/* 349 */             log.warn(sm.getString("jreLeakListener.authPolicyFail"), e);
/*     */           }
/*     */           catch (InvocationTargetException e) {
/* 352 */             ExceptionUtils.handleThrowable(e.getCause());
/* 353 */             log.warn(sm.getString("jreLeakListener.authPolicyFail"), e);
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 363 */         if ((this.securityLoginConfigurationProtection) && (!JreCompat.isJre8Available())) {
/*     */           try {
/* 365 */             Class.forName("javax.security.auth.login.Configuration", true, ClassLoader.getSystemClassLoader());
/*     */           }
/*     */           catch (ClassNotFoundException localClassNotFoundException2) {}
/*     */         }
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
/* 381 */         if ((this.tokenPollerProtection) && (!JreCompat.isJre9Available())) {
/* 382 */           Security.getProviders();
/*     */         }
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
/* 401 */         if (this.urlCacheProtection) {
/*     */           try {
/* 403 */             JreCompat.getInstance().disableCachingForJarUrlConnections();
/*     */           } catch (IOException e) {
/* 405 */             log.error(sm.getString("jreLeakListener.jarUrlConnCacheFail"), e);
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 412 */         if ((this.xmlParsingProtection) && (!JreCompat.isJre9Available()))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 419 */           DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*     */           try {
/* 421 */             DocumentBuilder documentBuilder = factory.newDocumentBuilder();
/*     */             
/*     */ 
/* 424 */             Document document = documentBuilder.newDocument();
/* 425 */             document.createElement("dummy");
/*     */             
/* 427 */             DOMImplementationLS implementation = (DOMImplementationLS)document.getImplementation();
/* 428 */             implementation.createLSSerializer().writeToString(document);
/*     */             
/*     */ 
/* 431 */             document.normalize();
/*     */           } catch (ParserConfigurationException e) {
/* 433 */             log.error(sm.getString("jreLeakListener.xmlParseFail"), e);
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 438 */         if ((this.ldapPoolProtection) && (!JreCompat.isJre9Available())) {
/*     */           try {
/* 440 */             Class.forName("com.sun.jndi.ldap.LdapPoolManager");
/*     */           } catch (ClassNotFoundException e) {
/* 442 */             if (JreVendor.IS_ORACLE_JVM) {
/* 443 */               log.error(sm.getString("jreLeakListener.ldapPoolManagerFail"), e);
/*     */             }
/*     */             else {
/* 446 */               log.debug(sm.getString("jreLeakListener.ldapPoolManagerFail"), e);
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 455 */         if ((this.forkJoinCommonPoolProtection) && (JreCompat.isJre8Available()))
/*     */         {
/* 457 */           if (System.getProperty("java.util.concurrent.ForkJoinPool.common.threadFactory") == null) {
/* 458 */             System.setProperty("java.util.concurrent.ForkJoinPool.common.threadFactory", SafeForkJoinWorkerThreadFactory.class
/* 459 */               .getName());
/*     */           }
/*     */         }
/*     */         
/* 463 */         if (this.classesToInitialize != null) {
/* 464 */           StringTokenizer strTok = new StringTokenizer(this.classesToInitialize, ", \r\n\t");
/*     */           
/* 466 */           while (strTok.hasMoreTokens()) {
/* 467 */             String classNameToLoad = strTok.nextToken();
/*     */             try {
/* 469 */               Class.forName(classNameToLoad);
/*     */             } catch (ClassNotFoundException e) {
/* 471 */               log.error(sm
/* 472 */                 .getString("jreLeakListener.classToInitializeFail", new Object[] { classNameToLoad }), e);
/*     */             }
/*     */             
/*     */           }
/*     */         }
/*     */       }
/*     */       finally
/*     */       {
/* 480 */         Thread.currentThread().setContextClassLoader(loader);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\JreMemoryLeakPreventionListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */