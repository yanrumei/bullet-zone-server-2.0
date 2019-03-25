/*     */ package org.apache.catalina.realm;
/*     */ 
/*     */ import java.security.Principal;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.security.auth.Subject;
/*     */ import javax.security.auth.callback.CallbackHandler;
/*     */ import javax.security.auth.login.AccountExpiredException;
/*     */ import javax.security.auth.login.Configuration;
/*     */ import javax.security.auth.login.CredentialExpiredException;
/*     */ import javax.security.auth.login.FailedLoginException;
/*     */ import javax.security.auth.login.LoginContext;
/*     */ import javax.security.auth.login.LoginException;
/*     */ import org.apache.catalina.Container;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
/*     */ import org.apache.tomcat.util.res.StringManager;
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
/*     */ public class JAASRealm
/*     */   extends RealmBase
/*     */ {
/* 130 */   private static final Log log = LogFactory.getLog(JAASRealm.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 139 */   protected String appName = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected static final String name = "JAASRealm";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 153 */   protected final List<String> roleClasses = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 159 */   protected final List<String> userClasses = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 167 */   protected boolean useContextClassLoader = true;
/*     */   
/*     */ 
/*     */ 
/*     */   protected String configFile;
/*     */   
/*     */ 
/*     */   protected Configuration jaasConfiguration;
/*     */   
/*     */ 
/* 177 */   protected volatile boolean jaasConfigurationLoaded = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getConfigFile()
/*     */   {
/* 186 */     return this.configFile;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setConfigFile(String configFile)
/*     */   {
/* 194 */     this.configFile = configFile;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAppName(String name)
/*     */   {
/* 203 */     this.appName = name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getAppName()
/*     */   {
/* 210 */     return this.appName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUseContextClassLoader(boolean useContext)
/*     */   {
/* 220 */     this.useContextClassLoader = useContext;
/* 221 */     log.info("Setting useContextClassLoader = " + useContext);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUseContextClassLoader()
/*     */   {
/* 231 */     return this.useContextClassLoader;
/*     */   }
/*     */   
/*     */   public void setContainer(Container container)
/*     */   {
/* 236 */     super.setContainer(container);
/*     */     
/* 238 */     if (this.appName == null) {
/* 239 */       String name = container.getName();
/* 240 */       if (!name.startsWith("/")) {
/* 241 */         name = "/" + name;
/*     */       }
/* 243 */       name = makeLegalForJAAS(name);
/*     */       
/* 245 */       this.appName = name;
/*     */       
/* 247 */       log.info("Set JAAS app name " + this.appName);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 255 */   protected String roleClassNames = null;
/*     */   
/*     */   public String getRoleClassNames() {
/* 258 */     return this.roleClassNames;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRoleClassNames(String roleClassNames)
/*     */   {
/* 269 */     this.roleClassNames = roleClassNames;
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
/*     */   protected void parseClassNames(String classNamesString, List<String> classNamesList)
/*     */   {
/* 282 */     classNamesList.clear();
/* 283 */     if (classNamesString == null) { return;
/*     */     }
/* 285 */     ClassLoader loader = getClass().getClassLoader();
/* 286 */     if (isUseContextClassLoader()) {
/* 287 */       loader = Thread.currentThread().getContextClassLoader();
/*     */     }
/* 289 */     String[] classNames = classNamesString.split("[ ]*,[ ]*");
/* 290 */     for (int i = 0; i < classNames.length; i++) {
/* 291 */       if (classNames[i].length() != 0) {
/*     */         try {
/* 293 */           Class<?> principalClass = Class.forName(classNames[i], false, loader);
/*     */           
/* 295 */           if (Principal.class.isAssignableFrom(principalClass)) {
/* 296 */             classNamesList.add(classNames[i]);
/*     */           } else {
/* 298 */             log.error("Class " + classNames[i] + " is not implementing java.security.Principal! Class not added.");
/*     */           }
/*     */         }
/*     */         catch (ClassNotFoundException e) {
/* 302 */           log.error("Class " + classNames[i] + " not found! Class not added.");
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 311 */   protected String userClassNames = null;
/*     */   
/*     */   public String getUserClassNames() {
/* 314 */     return this.userClassNames;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUserClassNames(String userClassNames)
/*     */   {
/* 325 */     this.userClassNames = userClassNames;
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
/*     */ 
/*     */ 
/*     */   public Principal authenticate(String username, String credentials)
/*     */   {
/* 342 */     return authenticate(username, new JAASCallbackHandler(this, username, credentials));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Principal authenticate(String username, String clientDigest, String nonce, String nc, String cnonce, String qop, String realmName, String md5a2)
/*     */   {
/* 366 */     return authenticate(username, new JAASCallbackHandler(this, username, clientDigest, nonce, nc, cnonce, qop, realmName, md5a2, "DIGEST"));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Principal authenticate(String username, CallbackHandler callbackHandler)
/*     */   {
/*     */     try
/*     */     {
/* 390 */       LoginContext loginContext = null;
/* 391 */       if (this.appName == null) { this.appName = "Tomcat";
/*     */       }
/* 393 */       if (log.isDebugEnabled()) {
/* 394 */         log.debug(sm.getString("jaasRealm.beginLogin", new Object[] { username, this.appName }));
/*     */       }
/*     */       
/* 397 */       ClassLoader ocl = null;
/*     */       
/* 399 */       if (!isUseContextClassLoader()) {
/* 400 */         ocl = Thread.currentThread().getContextClassLoader();
/* 401 */         Thread.currentThread().setContextClassLoader(
/* 402 */           getClass().getClassLoader());
/*     */       }
/*     */       try
/*     */       {
/* 406 */         Configuration config = getConfig();
/* 407 */         loginContext = new LoginContext(this.appName, null, callbackHandler, config);
/*     */       }
/*     */       catch (Throwable e) {
/* 410 */         ExceptionUtils.handleThrowable(e);
/* 411 */         log.error(sm.getString("jaasRealm.unexpectedError"), e);
/* 412 */         return null;
/*     */       } finally {
/* 414 */         if (!isUseContextClassLoader()) {
/* 415 */           Thread.currentThread().setContextClassLoader(ocl);
/*     */         }
/*     */       }
/*     */       
/* 419 */       if (log.isDebugEnabled()) {
/* 420 */         log.debug("Login context created " + username);
/*     */       }
/*     */       
/* 423 */       Subject subject = null;
/*     */       try {
/* 425 */         loginContext.login();
/* 426 */         subject = loginContext.getSubject();
/* 427 */         if (subject == null) {
/* 428 */           if (log.isDebugEnabled())
/* 429 */             log.debug(sm.getString("jaasRealm.failedLogin", new Object[] { username }));
/* 430 */           return null;
/*     */         }
/*     */       } catch (AccountExpiredException e) {
/* 433 */         if (log.isDebugEnabled())
/* 434 */           log.debug(sm.getString("jaasRealm.accountExpired", new Object[] { username }));
/* 435 */         return null;
/*     */       } catch (CredentialExpiredException e) {
/* 437 */         if (log.isDebugEnabled())
/* 438 */           log.debug(sm.getString("jaasRealm.credentialExpired", new Object[] { username }));
/* 439 */         return null;
/*     */       } catch (FailedLoginException e) {
/* 441 */         if (log.isDebugEnabled())
/* 442 */           log.debug(sm.getString("jaasRealm.failedLogin", new Object[] { username }));
/* 443 */         return null;
/*     */       } catch (LoginException e) {
/* 445 */         log.warn(sm.getString("jaasRealm.loginException", new Object[] { username }), e);
/* 446 */         return null;
/*     */       } catch (Throwable e) {
/* 448 */         ExceptionUtils.handleThrowable(e);
/* 449 */         log.error(sm.getString("jaasRealm.unexpectedError"), e);
/* 450 */         return null;
/*     */       }
/*     */       
/* 453 */       if (log.isDebugEnabled()) {
/* 454 */         log.debug(sm.getString("jaasRealm.loginContextCreated", new Object[] { username }));
/*     */       }
/*     */       
/* 457 */       Principal principal = createPrincipal(username, subject, loginContext);
/* 458 */       if (principal == null) {
/* 459 */         log.debug(sm.getString("jaasRealm.authenticateFailure", new Object[] { username }));
/* 460 */         return null;
/*     */       }
/* 462 */       if (log.isDebugEnabled()) {
/* 463 */         log.debug(sm.getString("jaasRealm.authenticateSuccess", new Object[] { username }));
/*     */       }
/*     */       
/* 466 */       return principal;
/*     */     } catch (Throwable t) {
/* 468 */       log.error("error ", t); }
/* 469 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected String getName()
/*     */   {
/* 477 */     return "JAASRealm";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getPassword(String username)
/*     */   {
/* 489 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Principal getPrincipal(String username)
/*     */   {
/* 500 */     return authenticate(username, new JAASCallbackHandler(this, username, null, null, null, null, null, null, null, "CLIENT_CERT"));
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
/*     */   protected Principal createPrincipal(String username, Subject subject, LoginContext loginContext)
/*     */   {
/* 527 */     List<String> roles = new ArrayList();
/* 528 */     Principal userPrincipal = null;
/*     */     
/*     */ 
/* 531 */     Iterator<Principal> principals = subject.getPrincipals().iterator();
/* 532 */     while (principals.hasNext()) {
/* 533 */       Principal principal = (Principal)principals.next();
/*     */       
/* 535 */       String principalClass = principal.getClass().getName();
/*     */       
/* 537 */       if (log.isDebugEnabled()) {
/* 538 */         log.debug(sm.getString("jaasRealm.checkPrincipal", new Object[] { principal, principalClass }));
/*     */       }
/*     */       
/* 541 */       if ((userPrincipal == null) && (this.userClasses.contains(principalClass))) {
/* 542 */         userPrincipal = principal;
/* 543 */         if (log.isDebugEnabled()) {
/* 544 */           log.debug(sm.getString("jaasRealm.userPrincipalSuccess", new Object[] { principal.getName() }));
/*     */         }
/*     */       }
/*     */       
/* 548 */       if (this.roleClasses.contains(principalClass)) {
/* 549 */         roles.add(principal.getName());
/* 550 */         if (log.isDebugEnabled()) {
/* 551 */           log.debug(sm.getString("jaasRealm.rolePrincipalAdd", new Object[] { principal.getName() }));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 557 */     if (userPrincipal == null) {
/* 558 */       if (log.isDebugEnabled()) {
/* 559 */         log.debug(sm.getString("jaasRealm.userPrincipalFailure"));
/* 560 */         log.debug(sm.getString("jaasRealm.rolePrincipalFailure"));
/*     */       }
/*     */     }
/* 563 */     else if ((roles.size() == 0) && 
/* 564 */       (log.isDebugEnabled())) {
/* 565 */       log.debug(sm.getString("jaasRealm.rolePrincipalFailure"));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 571 */     return new GenericPrincipal(username, null, roles, userPrincipal, loginContext);
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
/*     */   protected String makeLegalForJAAS(String src)
/*     */   {
/* 585 */     String result = src;
/*     */     
/*     */ 
/* 588 */     if (result == null) {
/* 589 */       result = "other";
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 594 */     if (result.startsWith("/")) {
/* 595 */       result = result.substring(1);
/*     */     }
/*     */     
/* 598 */     return result;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void startInternal()
/*     */     throws LifecycleException
/*     */   {
/* 618 */     parseClassNames(this.userClassNames, this.userClasses);
/* 619 */     parseClassNames(this.roleClassNames, this.roleClasses);
/*     */     
/* 621 */     super.startInternal();
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected Configuration getConfig()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 8	org/apache/catalina/realm/JAASRealm:jaasConfigurationLoaded	Z
/*     */     //   4: ifeq +8 -> 12
/*     */     //   7: aload_0
/*     */     //   8: getfield 105	org/apache/catalina/realm/JAASRealm:jaasConfiguration	Ljavax/security/auth/login/Configuration;
/*     */     //   11: areturn
/*     */     //   12: aload_0
/*     */     //   13: dup
/*     */     //   14: astore_1
/*     */     //   15: monitorenter
/*     */     //   16: aload_0
/*     */     //   17: getfield 11	org/apache/catalina/realm/JAASRealm:configFile	Ljava/lang/String;
/*     */     //   20: ifnonnull +12 -> 32
/*     */     //   23: aload_0
/*     */     //   24: iconst_1
/*     */     //   25: putfield 8	org/apache/catalina/realm/JAASRealm:jaasConfigurationLoaded	Z
/*     */     //   28: aconst_null
/*     */     //   29: aload_1
/*     */     //   30: monitorexit
/*     */     //   31: areturn
/*     */     //   32: invokestatic 30	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   35: invokevirtual 31	java/lang/Thread:getContextClassLoader	()Ljava/lang/ClassLoader;
/*     */     //   38: aload_0
/*     */     //   39: getfield 11	org/apache/catalina/realm/JAASRealm:configFile	Ljava/lang/String;
/*     */     //   42: invokevirtual 106	java/lang/ClassLoader:getResource	(Ljava/lang/String;)Ljava/net/URL;
/*     */     //   45: astore_2
/*     */     //   46: aload_2
/*     */     //   47: invokevirtual 107	java/net/URL:toURI	()Ljava/net/URI;
/*     */     //   50: astore_3
/*     */     //   51: ldc 108
/*     */     //   53: invokestatic 109	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
/*     */     //   56: astore 4
/*     */     //   58: aload 4
/*     */     //   60: iconst_1
/*     */     //   61: anewarray 110	java/lang/Class
/*     */     //   64: dup
/*     */     //   65: iconst_0
/*     */     //   66: ldc 111
/*     */     //   68: aastore
/*     */     //   69: invokevirtual 112	java/lang/Class:getConstructor	([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;
/*     */     //   72: astore 5
/*     */     //   74: aload 5
/*     */     //   76: iconst_1
/*     */     //   77: anewarray 54	java/lang/Object
/*     */     //   80: dup
/*     */     //   81: iconst_0
/*     */     //   82: aload_3
/*     */     //   83: aastore
/*     */     //   84: invokevirtual 113	java/lang/reflect/Constructor:newInstance	([Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   87: checkcast 114	javax/security/auth/login/Configuration
/*     */     //   90: astore 6
/*     */     //   92: aload_0
/*     */     //   93: aload 6
/*     */     //   95: putfield 105	org/apache/catalina/realm/JAASRealm:jaasConfiguration	Ljavax/security/auth/login/Configuration;
/*     */     //   98: aload_0
/*     */     //   99: iconst_1
/*     */     //   100: putfield 8	org/apache/catalina/realm/JAASRealm:jaasConfigurationLoaded	Z
/*     */     //   103: aload_0
/*     */     //   104: getfield 105	org/apache/catalina/realm/JAASRealm:jaasConfiguration	Ljavax/security/auth/login/Configuration;
/*     */     //   107: aload_1
/*     */     //   108: monitorexit
/*     */     //   109: areturn
/*     */     //   110: astore 7
/*     */     //   112: aload_1
/*     */     //   113: monitorexit
/*     */     //   114: aload 7
/*     */     //   116: athrow
/*     */     //   117: astore_1
/*     */     //   118: new 116	java/lang/RuntimeException
/*     */     //   121: dup
/*     */     //   122: aload_1
/*     */     //   123: invokespecial 117	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
/*     */     //   126: athrow
/*     */     //   127: astore_1
/*     */     //   128: new 116	java/lang/RuntimeException
/*     */     //   131: dup
/*     */     //   132: aload_1
/*     */     //   133: invokespecial 117	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
/*     */     //   136: athrow
/*     */     //   137: astore_1
/*     */     //   138: new 116	java/lang/RuntimeException
/*     */     //   141: dup
/*     */     //   142: aload_1
/*     */     //   143: invokespecial 117	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
/*     */     //   146: athrow
/*     */     //   147: astore_1
/*     */     //   148: new 116	java/lang/RuntimeException
/*     */     //   151: dup
/*     */     //   152: aload_1
/*     */     //   153: invokespecial 117	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
/*     */     //   156: athrow
/*     */     //   157: astore_1
/*     */     //   158: new 116	java/lang/RuntimeException
/*     */     //   161: dup
/*     */     //   162: aload_1
/*     */     //   163: invokespecial 117	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
/*     */     //   166: athrow
/*     */     //   167: astore_1
/*     */     //   168: new 116	java/lang/RuntimeException
/*     */     //   171: dup
/*     */     //   172: aload_1
/*     */     //   173: invokespecial 117	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
/*     */     //   176: athrow
/*     */     //   177: astore_1
/*     */     //   178: new 116	java/lang/RuntimeException
/*     */     //   181: dup
/*     */     //   182: aload_1
/*     */     //   183: invokevirtual 124	java/lang/reflect/InvocationTargetException:getCause	()Ljava/lang/Throwable;
/*     */     //   186: invokespecial 117	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
/*     */     //   189: athrow
/*     */     //   190: astore_1
/*     */     //   191: new 116	java/lang/RuntimeException
/*     */     //   194: dup
/*     */     //   195: aload_1
/*     */     //   196: invokespecial 117	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
/*     */     //   199: athrow
/*     */     // Line number table:
/*     */     //   Java source line #631	-> byte code offset #0
/*     */     //   Java source line #632	-> byte code offset #7
/*     */     //   Java source line #634	-> byte code offset #12
/*     */     //   Java source line #635	-> byte code offset #16
/*     */     //   Java source line #636	-> byte code offset #23
/*     */     //   Java source line #637	-> byte code offset #28
/*     */     //   Java source line #639	-> byte code offset #32
/*     */     //   Java source line #640	-> byte code offset #42
/*     */     //   Java source line #641	-> byte code offset #46
/*     */     //   Java source line #643	-> byte code offset #51
/*     */     //   Java source line #644	-> byte code offset #53
/*     */     //   Java source line #645	-> byte code offset #58
/*     */     //   Java source line #646	-> byte code offset #69
/*     */     //   Java source line #647	-> byte code offset #74
/*     */     //   Java source line #648	-> byte code offset #92
/*     */     //   Java source line #649	-> byte code offset #98
/*     */     //   Java source line #650	-> byte code offset #103
/*     */     //   Java source line #651	-> byte code offset #110
/*     */     //   Java source line #652	-> byte code offset #117
/*     */     //   Java source line #653	-> byte code offset #118
/*     */     //   Java source line #654	-> byte code offset #127
/*     */     //   Java source line #655	-> byte code offset #128
/*     */     //   Java source line #656	-> byte code offset #137
/*     */     //   Java source line #657	-> byte code offset #138
/*     */     //   Java source line #658	-> byte code offset #147
/*     */     //   Java source line #659	-> byte code offset #148
/*     */     //   Java source line #660	-> byte code offset #157
/*     */     //   Java source line #661	-> byte code offset #158
/*     */     //   Java source line #662	-> byte code offset #167
/*     */     //   Java source line #663	-> byte code offset #168
/*     */     //   Java source line #664	-> byte code offset #177
/*     */     //   Java source line #665	-> byte code offset #178
/*     */     //   Java source line #666	-> byte code offset #190
/*     */     //   Java source line #667	-> byte code offset #191
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	200	0	this	JAASRealm
/*     */     //   117	6	1	ex	java.net.URISyntaxException
/*     */     //   127	6	1	ex	NoSuchMethodException
/*     */     //   137	6	1	ex	SecurityException
/*     */     //   147	6	1	ex	InstantiationException
/*     */     //   157	6	1	ex	IllegalAccessException
/*     */     //   167	6	1	ex	IllegalArgumentException
/*     */     //   177	6	1	ex	java.lang.reflect.InvocationTargetException
/*     */     //   190	6	1	ex	ClassNotFoundException
/*     */     //   45	2	2	resource	java.net.URL
/*     */     //   50	33	3	uri	java.net.URI
/*     */     //   56	3	4	sunConfigFile	Class<Configuration>
/*     */     //   72	3	5	constructor	java.lang.reflect.Constructor<Configuration>
/*     */     //   90	4	6	config	Configuration
/*     */     //   110	5	7	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   16	31	110	finally
/*     */     //   32	109	110	finally
/*     */     //   110	114	110	finally
/*     */     //   0	11	117	java/net/URISyntaxException
/*     */     //   12	31	117	java/net/URISyntaxException
/*     */     //   32	109	117	java/net/URISyntaxException
/*     */     //   110	117	117	java/net/URISyntaxException
/*     */     //   0	11	127	java/lang/NoSuchMethodException
/*     */     //   12	31	127	java/lang/NoSuchMethodException
/*     */     //   32	109	127	java/lang/NoSuchMethodException
/*     */     //   110	117	127	java/lang/NoSuchMethodException
/*     */     //   0	11	137	java/lang/SecurityException
/*     */     //   12	31	137	java/lang/SecurityException
/*     */     //   32	109	137	java/lang/SecurityException
/*     */     //   110	117	137	java/lang/SecurityException
/*     */     //   0	11	147	java/lang/InstantiationException
/*     */     //   12	31	147	java/lang/InstantiationException
/*     */     //   32	109	147	java/lang/InstantiationException
/*     */     //   110	117	147	java/lang/InstantiationException
/*     */     //   0	11	157	java/lang/IllegalAccessException
/*     */     //   12	31	157	java/lang/IllegalAccessException
/*     */     //   32	109	157	java/lang/IllegalAccessException
/*     */     //   110	117	157	java/lang/IllegalAccessException
/*     */     //   0	11	167	java/lang/IllegalArgumentException
/*     */     //   12	31	167	java/lang/IllegalArgumentException
/*     */     //   32	109	167	java/lang/IllegalArgumentException
/*     */     //   110	117	167	java/lang/IllegalArgumentException
/*     */     //   0	11	177	java/lang/reflect/InvocationTargetException
/*     */     //   12	31	177	java/lang/reflect/InvocationTargetException
/*     */     //   32	109	177	java/lang/reflect/InvocationTargetException
/*     */     //   110	117	177	java/lang/reflect/InvocationTargetException
/*     */     //   0	11	190	java/lang/ClassNotFoundException
/*     */     //   12	31	190	java/lang/ClassNotFoundException
/*     */     //   32	109	190	java/lang/ClassNotFoundException
/*     */     //   110	117	190	java/lang/ClassNotFoundException
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\realm\JAASRealm.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */