/*      */ package org.apache.catalina.authenticator;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.security.Principal;
/*      */ import java.security.cert.X509Certificate;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.Date;
/*      */ import java.util.Iterator;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import javax.security.auth.Subject;
/*      */ import javax.security.auth.callback.CallbackHandler;
/*      */ import javax.security.auth.message.AuthException;
/*      */ import javax.security.auth.message.AuthStatus;
/*      */ import javax.security.auth.message.MessageInfo;
/*      */ import javax.security.auth.message.config.AuthConfigFactory;
/*      */ import javax.security.auth.message.config.AuthConfigProvider;
/*      */ import javax.security.auth.message.config.ClientAuthConfig;
/*      */ import javax.security.auth.message.config.RegistrationListener;
/*      */ import javax.security.auth.message.config.ServerAuthConfig;
/*      */ import javax.security.auth.message.config.ServerAuthContext;
/*      */ import javax.servlet.ServletContext;
/*      */ import javax.servlet.ServletException;
/*      */ import javax.servlet.SessionCookieConfig;
/*      */ import javax.servlet.http.Cookie;
/*      */ import javax.servlet.http.HttpServletRequest;
/*      */ import javax.servlet.http.HttpServletResponse;
/*      */ import org.apache.catalina.Authenticator;
/*      */ import org.apache.catalina.Container;
/*      */ import org.apache.catalina.Context;
/*      */ import org.apache.catalina.LifecycleException;
/*      */ import org.apache.catalina.Manager;
/*      */ import org.apache.catalina.Pipeline;
/*      */ import org.apache.catalina.Realm;
/*      */ import org.apache.catalina.Session;
/*      */ import org.apache.catalina.TomcatPrincipal;
/*      */ import org.apache.catalina.Valve;
/*      */ import org.apache.catalina.Wrapper;
/*      */ import org.apache.catalina.authenticator.jaspic.CallbackHandlerImpl;
/*      */ import org.apache.catalina.authenticator.jaspic.MessageInfoImpl;
/*      */ import org.apache.catalina.connector.Response;
/*      */ import org.apache.catalina.realm.GenericPrincipal;
/*      */ import org.apache.catalina.util.SessionIdGeneratorBase;
/*      */ import org.apache.catalina.util.StandardSessionIdGenerator;
/*      */ import org.apache.catalina.valves.ValveBase;
/*      */ import org.apache.coyote.ActionCode;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.tomcat.util.ExceptionUtils;
/*      */ import org.apache.tomcat.util.buf.MessageBytes;
/*      */ import org.apache.tomcat.util.descriptor.web.LoginConfig;
/*      */ import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
/*      */ import org.apache.tomcat.util.http.MimeHeaders;
/*      */ import org.apache.tomcat.util.res.StringManager;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class AuthenticatorBase
/*      */   extends ValveBase
/*      */   implements Authenticator, RegistrationListener
/*      */ {
/*   94 */   private static final Log log = LogFactory.getLog(AuthenticatorBase.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*   99 */   private static final String DATE_ONE = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US)
/*  100 */     .format(new Date(1L));
/*      */   
/*  102 */   private static final AuthConfigProvider NO_PROVIDER_AVAILABLE = new NoOpAuthConfigProvider(null);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  107 */   protected static final StringManager sm = StringManager.getManager(AuthenticatorBase.class);
/*      */   
/*      */ 
/*      */ 
/*      */   protected static final String AUTH_HEADER_NAME = "WWW-Authenticate";
/*      */   
/*      */ 
/*      */   protected static final String REALM_NAME = "Authentication required";
/*      */   
/*      */ 
/*      */ 
/*      */   protected static String getRealmName(Context context)
/*      */   {
/*  120 */     if (context == null)
/*      */     {
/*  122 */       return "Authentication required";
/*      */     }
/*      */     
/*  125 */     LoginConfig config = context.getLoginConfig();
/*  126 */     if (config == null) {
/*  127 */       return "Authentication required";
/*      */     }
/*      */     
/*  130 */     String result = config.getRealmName();
/*  131 */     if (result == null) {
/*  132 */       return "Authentication required";
/*      */     }
/*      */     
/*  135 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */   public AuthenticatorBase()
/*      */   {
/*  141 */     super(true);
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
/*  156 */   protected boolean alwaysUseSession = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  162 */   protected boolean cache = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  168 */   protected boolean changeSessionIdOnAuthentication = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  173 */   protected Context context = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  179 */   protected boolean disableProxyCaching = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  185 */   protected boolean securePagesWithPragma = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  194 */   protected String secureRandomClass = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  205 */   protected String secureRandomAlgorithm = "SHA1PRNG";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  215 */   protected String secureRandomProvider = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  222 */   protected String jaspicCallbackHandlerClass = null;
/*      */   
/*  224 */   protected SessionIdGeneratorBase sessionIdGenerator = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  230 */   protected SingleSignOn sso = null;
/*      */   
/*  232 */   private volatile String jaspicAppContextID = null;
/*  233 */   private volatile AuthConfigProvider jaspicProvider = null;
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getAlwaysUseSession()
/*      */   {
/*  239 */     return this.alwaysUseSession;
/*      */   }
/*      */   
/*      */   public void setAlwaysUseSession(boolean alwaysUseSession) {
/*  243 */     this.alwaysUseSession = alwaysUseSession;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getCache()
/*      */   {
/*  253 */     return this.cache;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCache(boolean cache)
/*      */   {
/*  263 */     this.cache = cache;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Container getContainer()
/*      */   {
/*  271 */     return this.context;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setContainer(Container container)
/*      */   {
/*  283 */     if ((container != null) && (!(container instanceof Context))) {
/*  284 */       throw new IllegalArgumentException(sm.getString("authenticator.notContext"));
/*      */     }
/*      */     
/*  287 */     super.setContainer(container);
/*  288 */     this.context = ((Context)container);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getDisableProxyCaching()
/*      */   {
/*  300 */     return this.disableProxyCaching;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDisableProxyCaching(boolean nocache)
/*      */   {
/*  312 */     this.disableProxyCaching = nocache;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getSecurePagesWithPragma()
/*      */   {
/*  323 */     return this.securePagesWithPragma;
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
/*      */   public void setSecurePagesWithPragma(boolean securePagesWithPragma)
/*      */   {
/*  336 */     this.securePagesWithPragma = securePagesWithPragma;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getChangeSessionIdOnAuthentication()
/*      */   {
/*  347 */     return this.changeSessionIdOnAuthentication;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setChangeSessionIdOnAuthentication(boolean changeSessionIdOnAuthentication)
/*      */   {
/*  359 */     this.changeSessionIdOnAuthentication = changeSessionIdOnAuthentication;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSecureRandomClass()
/*      */   {
/*  369 */     return this.secureRandomClass;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSecureRandomClass(String secureRandomClass)
/*      */   {
/*  379 */     this.secureRandomClass = secureRandomClass;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSecureRandomAlgorithm()
/*      */   {
/*  388 */     return this.secureRandomAlgorithm;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSecureRandomAlgorithm(String secureRandomAlgorithm)
/*      */   {
/*  398 */     this.secureRandomAlgorithm = secureRandomAlgorithm;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSecureRandomProvider()
/*      */   {
/*  407 */     return this.secureRandomProvider;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSecureRandomProvider(String secureRandomProvider)
/*      */   {
/*  417 */     this.secureRandomProvider = secureRandomProvider;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getJaspicCallbackHandlerClass()
/*      */   {
/*  426 */     return this.jaspicCallbackHandlerClass;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setJaspicCallbackHandlerClass(String jaspicCallbackHandlerClass)
/*      */   {
/*  436 */     this.jaspicCallbackHandlerClass = jaspicCallbackHandlerClass;
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
/*      */   public void invoke(org.apache.catalina.connector.Request request, Response response)
/*      */     throws IOException, ServletException
/*      */   {
/*  458 */     if (log.isDebugEnabled()) {
/*  459 */       log.debug("Security checking request " + request.getMethod() + " " + request
/*  460 */         .getRequestURI());
/*      */     }
/*      */     
/*      */ 
/*  464 */     if (this.cache) {
/*  465 */       Principal principal = request.getUserPrincipal();
/*  466 */       if (principal == null) {
/*  467 */         Session session = request.getSessionInternal(false);
/*  468 */         if (session != null) {
/*  469 */           principal = session.getPrincipal();
/*  470 */           if (principal != null) {
/*  471 */             if (log.isDebugEnabled()) {
/*  472 */               log.debug("We have cached auth type " + session.getAuthType() + " for principal " + principal);
/*      */             }
/*      */             
/*  475 */             request.setAuthType(session.getAuthType());
/*  476 */             request.setUserPrincipal(principal);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  482 */     boolean authRequired = isContinuationRequired(request);
/*      */     
/*      */ 
/*      */ 
/*  486 */     Wrapper wrapper = request.getWrapper();
/*  487 */     if (wrapper != null) {
/*  488 */       wrapper.servletSecurityAnnotationScan();
/*      */     }
/*      */     
/*  491 */     Realm realm = this.context.getRealm();
/*      */     
/*  493 */     SecurityConstraint[] constraints = realm.findSecurityConstraints(request, this.context);
/*      */     
/*  495 */     AuthConfigProvider jaspicProvider = getJaspicProvider();
/*  496 */     if (jaspicProvider != null) {
/*  497 */       authRequired = true;
/*      */     }
/*      */     
/*  500 */     if ((constraints == null) && (!this.context.getPreemptiveAuthentication()) && (!authRequired)) {
/*  501 */       if (log.isDebugEnabled()) {
/*  502 */         log.debug(" Not subject to any constraint");
/*      */       }
/*  504 */       getNext().invoke(request, response);
/*  505 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  510 */     if ((constraints != null) && (this.disableProxyCaching) && 
/*  511 */       (!"POST".equalsIgnoreCase(request.getMethod()))) {
/*  512 */       if (this.securePagesWithPragma)
/*      */       {
/*  514 */         response.setHeader("Pragma", "No-cache");
/*  515 */         response.setHeader("Cache-Control", "no-cache");
/*      */       } else {
/*  517 */         response.setHeader("Cache-Control", "private");
/*      */       }
/*  519 */       response.setHeader("Expires", DATE_ONE);
/*      */     }
/*      */     
/*  522 */     if (constraints != null)
/*      */     {
/*  524 */       if (log.isDebugEnabled()) {
/*  525 */         log.debug(" Calling hasUserDataPermission()");
/*      */       }
/*  527 */       if (!realm.hasUserDataPermission(request, response, constraints)) {
/*  528 */         if (log.isDebugEnabled()) {
/*  529 */           log.debug(" Failed hasUserDataPermission() test");
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  535 */         return;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  541 */     boolean hasAuthConstraint = false;
/*  542 */     if (constraints != null) {
/*  543 */       hasAuthConstraint = true;
/*  544 */       for (int i = 0; (i < constraints.length) && (hasAuthConstraint); i++) {
/*  545 */         if (!constraints[i].getAuthConstraint()) {
/*  546 */           hasAuthConstraint = false;
/*  547 */         } else if ((!constraints[i].getAllRoles()) && 
/*  548 */           (!constraints[i].getAuthenticatedUsers())) {
/*  549 */           String[] roles = constraints[i].findAuthRoles();
/*  550 */           if ((roles == null) || (roles.length == 0)) {
/*  551 */             hasAuthConstraint = false;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  557 */     if ((!authRequired) && (hasAuthConstraint)) {
/*  558 */       authRequired = true;
/*      */     }
/*      */     
/*  561 */     if ((!authRequired) && (this.context.getPreemptiveAuthentication()))
/*      */     {
/*  563 */       authRequired = request.getCoyoteRequest().getMimeHeaders().getValue("authorization") != null;
/*      */     }
/*      */     
/*  566 */     if ((!authRequired) && (this.context.getPreemptiveAuthentication()) && 
/*  567 */       ("CLIENT_CERT".equals(getAuthMethod()))) {
/*  568 */       X509Certificate[] certs = getRequestCertificates(request);
/*  569 */       authRequired = (certs != null) && (certs.length > 0);
/*      */     }
/*      */     
/*  572 */     JaspicState jaspicState = null;
/*      */     
/*  574 */     if (authRequired) {
/*  575 */       if (log.isDebugEnabled()) {
/*  576 */         log.debug(" Calling authenticate()");
/*      */       }
/*      */       
/*  579 */       if (jaspicProvider != null) {
/*  580 */         jaspicState = getJaspicState(jaspicProvider, request, response, hasAuthConstraint);
/*  581 */         if (jaspicState == null) {
/*  582 */           return;
/*      */         }
/*      */       }
/*      */       
/*  586 */       if ((jaspicProvider != null) || (doAuthenticate(request, response))) { if (jaspicProvider != null)
/*      */         {
/*  588 */           if (authenticateJaspic(request, response, jaspicState, false)) {} }
/*  589 */       } else { if (log.isDebugEnabled()) {
/*  590 */           log.debug(" Failed authenticate() test");
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  596 */         return;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  601 */     if (constraints != null) {
/*  602 */       if (log.isDebugEnabled()) {
/*  603 */         log.debug(" Calling accessControl()");
/*      */       }
/*  605 */       if (!realm.hasResourcePermission(request, response, constraints, this.context)) {
/*  606 */         if (log.isDebugEnabled()) {
/*  607 */           log.debug(" Failed accessControl() test");
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  613 */         return;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  618 */     if (log.isDebugEnabled()) {
/*  619 */       log.debug(" Successfully passed all security constraints");
/*      */     }
/*  621 */     getNext().invoke(request, response);
/*      */     
/*  623 */     if (jaspicProvider != null) {
/*  624 */       secureResponseJspic(request, response, jaspicState);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean authenticate(org.apache.catalina.connector.Request request, HttpServletResponse httpResponse)
/*      */     throws IOException
/*      */   {
/*  633 */     AuthConfigProvider jaspicProvider = getJaspicProvider();
/*      */     
/*  635 */     if (jaspicProvider == null) {
/*  636 */       return doAuthenticate(request, httpResponse);
/*      */     }
/*  638 */     Response response = request.getResponse();
/*  639 */     JaspicState jaspicState = getJaspicState(jaspicProvider, request, response, true);
/*  640 */     if (jaspicState == null) {
/*  641 */       return false;
/*      */     }
/*      */     
/*  644 */     boolean result = authenticateJaspic(request, response, jaspicState, true);
/*      */     
/*  646 */     secureResponseJspic(request, response, jaspicState);
/*      */     
/*  648 */     return result;
/*      */   }
/*      */   
/*      */   private void secureResponseJspic(org.apache.catalina.connector.Request request, Response response, JaspicState state)
/*      */   {
/*      */     try
/*      */     {
/*  655 */       state.serverAuthContext.secureResponse(state.messageInfo, null);
/*  656 */       request.setRequest((HttpServletRequest)state.messageInfo.getRequestMessage());
/*  657 */       response.setResponse((HttpServletResponse)state.messageInfo.getResponseMessage());
/*      */     } catch (AuthException e) {
/*  659 */       log.warn(sm.getString("authenticator.jaspicSecureResponseFail"), e);
/*      */     }
/*      */   }
/*      */   
/*      */   private JaspicState getJaspicState(AuthConfigProvider jaspicProvider, org.apache.catalina.connector.Request request, Response response, boolean authMandatory)
/*      */     throws IOException
/*      */   {
/*  666 */     JaspicState jaspicState = new JaspicState(null);
/*      */     
/*      */ 
/*  669 */     jaspicState.messageInfo = new MessageInfoImpl(request.getRequest(), response.getResponse(), authMandatory);
/*      */     try
/*      */     {
/*  672 */       CallbackHandler callbackHandler = createCallbackHandler();
/*  673 */       ServerAuthConfig serverAuthConfig = jaspicProvider.getServerAuthConfig("HttpServlet", this.jaspicAppContextID, callbackHandler);
/*      */       
/*  675 */       String authContextID = serverAuthConfig.getAuthContextID(jaspicState.messageInfo);
/*  676 */       jaspicState.serverAuthContext = serverAuthConfig.getAuthContext(authContextID, null, null);
/*      */     } catch (AuthException e) {
/*  678 */       log.warn(sm.getString("authenticator.jaspicServerAuthContextFail"), e);
/*  679 */       response.sendError(500);
/*  680 */       return null;
/*      */     }
/*      */     
/*  683 */     return jaspicState;
/*      */   }
/*      */   
/*      */   private CallbackHandler createCallbackHandler() {
/*  687 */     CallbackHandler callbackHandler = null;
/*  688 */     if (this.jaspicCallbackHandlerClass == null) {
/*  689 */       callbackHandler = CallbackHandlerImpl.getInstance();
/*      */     } else {
/*  691 */       Class<?> clazz = null;
/*      */       try {
/*  693 */         clazz = Class.forName(this.jaspicCallbackHandlerClass, true, 
/*  694 */           Thread.currentThread().getContextClassLoader());
/*      */       }
/*      */       catch (ClassNotFoundException localClassNotFoundException) {}
/*      */       
/*      */       try
/*      */       {
/*  700 */         if (clazz == null) {
/*  701 */           clazz = Class.forName(this.jaspicCallbackHandlerClass);
/*      */         }
/*  703 */         callbackHandler = (CallbackHandler)clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/*      */       } catch (ReflectiveOperationException e) {
/*  705 */         throw new SecurityException(e);
/*      */       }
/*      */     }
/*      */     
/*  709 */     return callbackHandler;
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
/*      */   protected abstract boolean doAuthenticate(org.apache.catalina.connector.Request paramRequest, HttpServletResponse paramHttpServletResponse)
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean isContinuationRequired(org.apache.catalina.connector.Request request)
/*      */   {
/*  744 */     return false;
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
/*      */   protected X509Certificate[] getRequestCertificates(org.apache.catalina.connector.Request request)
/*      */     throws IllegalStateException
/*      */   {
/*  762 */     X509Certificate[] certs = (X509Certificate[])request.getAttribute("javax.servlet.request.X509Certificate");
/*      */     
/*  764 */     if ((certs == null) || (certs.length < 1)) {
/*      */       try {
/*  766 */         request.getCoyoteRequest().action(ActionCode.REQ_SSL_CERTIFICATE, null);
/*  767 */         certs = (X509Certificate[])request.getAttribute("javax.servlet.request.X509Certificate");
/*      */       }
/*      */       catch (IllegalStateException localIllegalStateException) {}
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  774 */     return certs;
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
/*      */   protected void associate(String ssoId, Session session)
/*      */   {
/*  788 */     if (this.sso == null) {
/*  789 */       return;
/*      */     }
/*  791 */     this.sso.associate(ssoId, session);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean authenticateJaspic(org.apache.catalina.connector.Request request, Response response, JaspicState state, boolean requirePrincipal)
/*      */   {
/*  799 */     boolean cachedAuth = checkForCachedAuthentication(request, response, false);
/*  800 */     Subject client = new Subject();
/*      */     try
/*      */     {
/*  803 */       authStatus = state.serverAuthContext.validateRequest(state.messageInfo, client, null);
/*      */     } catch (AuthException e) { AuthStatus authStatus;
/*  805 */       log.debug(sm.getString("authenticator.loginFail"), e);
/*  806 */       return false;
/*      */     }
/*      */     AuthStatus authStatus;
/*  809 */     request.setRequest((HttpServletRequest)state.messageInfo.getRequestMessage());
/*  810 */     response.setResponse((HttpServletResponse)state.messageInfo.getResponseMessage());
/*      */     
/*  812 */     if (authStatus == AuthStatus.SUCCESS) {
/*  813 */       GenericPrincipal principal = getPrincipal(client);
/*  814 */       if (log.isDebugEnabled()) {
/*  815 */         log.debug("Authenticated user: " + principal);
/*      */       }
/*  817 */       if (principal == null) {
/*  818 */         request.setUserPrincipal(null);
/*  819 */         request.setAuthType(null);
/*  820 */         if (requirePrincipal) {
/*  821 */           return false;
/*      */         }
/*  823 */       } else if ((!cachedAuth) || 
/*  824 */         (!principal.getUserPrincipal().equals(request.getUserPrincipal())))
/*      */       {
/*      */ 
/*  827 */         request.setNote("org.apache.catalina.authenticator.jaspic.SUBJECT", client);
/*      */         
/*  829 */         Map map = state.messageInfo.getMap();
/*  830 */         if ((map != null) && (map.containsKey("javax.servlet.http.registerSession"))) {
/*  831 */           register(request, response, principal, "JASPIC", null, null, true, true);
/*      */         } else {
/*  833 */           register(request, response, principal, "JASPIC", null, null);
/*      */         }
/*      */       }
/*  836 */       return true;
/*      */     }
/*  838 */     return false;
/*      */   }
/*      */   
/*      */   private GenericPrincipal getPrincipal(Subject subject)
/*      */   {
/*  843 */     if (subject == null) {
/*  844 */       return null;
/*      */     }
/*      */     
/*  847 */     Set<GenericPrincipal> principals = subject.getPrivateCredentials(GenericPrincipal.class);
/*  848 */     if (principals.isEmpty()) {
/*  849 */       return null;
/*      */     }
/*      */     
/*  852 */     return (GenericPrincipal)principals.iterator().next();
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
/*      */   protected boolean checkForCachedAuthentication(org.apache.catalina.connector.Request request, HttpServletResponse response, boolean useSSO)
/*      */   {
/*  875 */     Principal principal = request.getUserPrincipal();
/*  876 */     String ssoId = (String)request.getNote("org.apache.catalina.request.SSOID");
/*  877 */     if (principal != null) {
/*  878 */       if (log.isDebugEnabled()) {
/*  879 */         log.debug(sm.getString("authenticator.check.found", new Object[] { principal.getName() }));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  884 */       if (ssoId != null) {
/*  885 */         associate(ssoId, request.getSessionInternal(true));
/*      */       }
/*  887 */       return true;
/*      */     }
/*      */     
/*      */ 
/*  891 */     if ((useSSO) && (ssoId != null)) {
/*  892 */       if (log.isDebugEnabled()) {
/*  893 */         log.debug(sm.getString("authenticator.check.sso", new Object[] { ssoId }));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  903 */       if (reauthenticateFromSSO(ssoId, request)) {
/*  904 */         return true;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  910 */     if (request.getCoyoteRequest().getRemoteUserNeedsAuthorization()) {
/*  911 */       String username = request.getCoyoteRequest().getRemoteUser().toString();
/*  912 */       if (username != null) {
/*  913 */         if (log.isDebugEnabled()) {
/*  914 */           log.debug(sm.getString("authenticator.check.authorize", new Object[] { username }));
/*      */         }
/*  916 */         Principal authorized = this.context.getRealm().authenticate(username);
/*  917 */         if (authorized == null)
/*      */         {
/*      */ 
/*  920 */           if (log.isDebugEnabled()) {
/*  921 */             log.debug(sm.getString("authenticator.check.authorizeFail", new Object[] { username }));
/*      */           }
/*  923 */           authorized = new GenericPrincipal(username, null, null);
/*      */         }
/*  925 */         String authType = request.getAuthType();
/*  926 */         if ((authType == null) || (authType.length() == 0)) {
/*  927 */           authType = getAuthMethod();
/*      */         }
/*  929 */         register(request, response, authorized, authType, username, null);
/*  930 */         return true;
/*      */       }
/*      */     }
/*  933 */     return false;
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
/*      */   protected boolean reauthenticateFromSSO(String ssoId, org.apache.catalina.connector.Request request)
/*      */   {
/*  949 */     if ((this.sso == null) || (ssoId == null)) {
/*  950 */       return false;
/*      */     }
/*      */     
/*  953 */     boolean reauthenticated = false;
/*      */     
/*  955 */     Container parent = getContainer();
/*  956 */     if (parent != null) {
/*  957 */       Realm realm = parent.getRealm();
/*  958 */       if (realm != null) {
/*  959 */         reauthenticated = this.sso.reauthenticate(ssoId, realm, request);
/*      */       }
/*      */     }
/*      */     
/*  963 */     if (reauthenticated) {
/*  964 */       associate(ssoId, request.getSessionInternal(true));
/*      */       
/*  966 */       if (log.isDebugEnabled()) {
/*  967 */         log.debug(" Reauthenticated cached principal '" + request
/*  968 */           .getUserPrincipal().getName() + "' with auth type '" + request
/*  969 */           .getAuthType() + "'");
/*      */       }
/*      */     }
/*      */     
/*  973 */     return reauthenticated;
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
/*      */   public void register(org.apache.catalina.connector.Request request, HttpServletResponse response, Principal principal, String authType, String username, String password)
/*      */   {
/*  997 */     register(request, response, principal, authType, username, password, this.alwaysUseSession, this.cache);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void register(org.apache.catalina.connector.Request request, HttpServletResponse response, Principal principal, String authType, String username, String password, boolean alwaysUseSession, boolean cache)
/*      */   {
/* 1005 */     if (log.isDebugEnabled()) {
/* 1006 */       String name = principal == null ? "none" : principal.getName();
/* 1007 */       log.debug("Authenticated '" + name + "' with type '" + authType + "'");
/*      */     }
/*      */     
/*      */ 
/* 1011 */     request.setAuthType(authType);
/* 1012 */     request.setUserPrincipal(principal);
/*      */     
/* 1014 */     Session session = request.getSessionInternal(false);
/*      */     
/* 1016 */     if (session != null)
/*      */     {
/*      */ 
/* 1019 */       if ((this.changeSessionIdOnAuthentication) && (principal != null)) {
/* 1020 */         String oldId = null;
/* 1021 */         if (log.isDebugEnabled()) {
/* 1022 */           oldId = session.getId();
/*      */         }
/* 1024 */         Manager manager = request.getContext().getManager();
/* 1025 */         manager.changeSessionId(session);
/* 1026 */         request.changeSessionId(session.getId());
/* 1027 */         if (log.isDebugEnabled()) {
/* 1028 */           log.debug(sm.getString("authenticator.changeSessionId", new Object[] { oldId, session
/* 1029 */             .getId() }));
/*      */         }
/*      */       }
/* 1032 */     } else if (alwaysUseSession) {
/* 1033 */       session = request.getSessionInternal(true);
/*      */     }
/*      */     
/*      */ 
/* 1037 */     if ((cache) && 
/* 1038 */       (session != null)) {
/* 1039 */       session.setAuthType(authType);
/* 1040 */       session.setPrincipal(principal);
/* 1041 */       if (username != null) {
/* 1042 */         session.setNote("org.apache.catalina.session.USERNAME", username);
/*      */       } else {
/* 1044 */         session.removeNote("org.apache.catalina.session.USERNAME");
/*      */       }
/* 1046 */       if (password != null) {
/* 1047 */         session.setNote("org.apache.catalina.session.PASSWORD", password);
/*      */       } else {
/* 1049 */         session.removeNote("org.apache.catalina.session.PASSWORD");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1055 */     if (this.sso == null) {
/* 1056 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1062 */     String ssoId = (String)request.getNote("org.apache.catalina.request.SSOID");
/* 1063 */     if (ssoId == null)
/*      */     {
/* 1065 */       ssoId = this.sessionIdGenerator.generateSessionId();
/* 1066 */       Cookie cookie = new Cookie(Constants.SINGLE_SIGN_ON_COOKIE, ssoId);
/* 1067 */       cookie.setMaxAge(-1);
/* 1068 */       cookie.setPath("/");
/*      */       
/*      */ 
/* 1071 */       cookie.setSecure(request.isSecure());
/*      */       
/*      */ 
/* 1074 */       String ssoDomain = this.sso.getCookieDomain();
/* 1075 */       if (ssoDomain != null) {
/* 1076 */         cookie.setDomain(ssoDomain);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1081 */       if ((request.getServletContext().getSessionCookieConfig().isHttpOnly()) || 
/* 1082 */         (request.getContext().getUseHttpOnly())) {
/* 1083 */         cookie.setHttpOnly(true);
/*      */       }
/*      */       
/* 1086 */       response.addCookie(cookie);
/*      */       
/*      */ 
/* 1089 */       this.sso.register(ssoId, principal, authType, username, password);
/* 1090 */       request.setNote("org.apache.catalina.request.SSOID", ssoId);
/*      */     }
/*      */     else {
/* 1093 */       if (principal == null)
/*      */       {
/* 1095 */         this.sso.deregister(ssoId);
/* 1096 */         request.removeNote("org.apache.catalina.request.SSOID");
/* 1097 */         return;
/*      */       }
/*      */       
/* 1100 */       this.sso.update(ssoId, principal, authType, username, password);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1110 */     if (session == null) {
/* 1111 */       session = request.getSessionInternal(true);
/*      */     }
/* 1113 */     this.sso.associate(ssoId, session);
/*      */   }
/*      */   
/*      */   public void login(String username, String password, org.apache.catalina.connector.Request request)
/*      */     throws ServletException
/*      */   {
/* 1119 */     Principal principal = doLogin(request, username, password);
/* 1120 */     register(request, request.getResponse(), principal, getAuthMethod(), username, password);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract String getAuthMethod();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Principal doLogin(org.apache.catalina.connector.Request request, String username, String password)
/*      */     throws ServletException
/*      */   {
/* 1140 */     Principal p = this.context.getRealm().authenticate(username, password);
/* 1141 */     if (p == null) {
/* 1142 */       throw new ServletException(sm.getString("authenticator.loginFail"));
/*      */     }
/* 1144 */     return p;
/*      */   }
/*      */   
/*      */   public void logout(org.apache.catalina.connector.Request request)
/*      */   {
/* 1149 */     AuthConfigProvider provider = getJaspicProvider();
/* 1150 */     if (provider != null) {
/* 1151 */       MessageInfo messageInfo = new MessageInfoImpl(request, request.getResponse(), true);
/* 1152 */       Subject client = (Subject)request.getNote("org.apache.catalina.authenticator.jaspic.SUBJECT");
/* 1153 */       if (client == null) {
/* 1154 */         return;
/*      */       }
/*      */       
/*      */       try
/*      */       {
/* 1159 */         ServerAuthConfig serverAuthConfig = provider.getServerAuthConfig("HttpServlet", this.jaspicAppContextID, 
/* 1160 */           CallbackHandlerImpl.getInstance());
/* 1161 */         String authContextID = serverAuthConfig.getAuthContextID(messageInfo);
/* 1162 */         ServerAuthContext serverAuthContext = serverAuthConfig.getAuthContext(authContextID, null, null);
/* 1163 */         serverAuthContext.cleanSubject(messageInfo, client);
/*      */       } catch (AuthException e) {
/* 1165 */         log.debug(sm.getString("authenticator.jaspicCleanSubjectFail"), e);
/*      */       }
/*      */     }
/*      */     
/* 1169 */     Principal p = request.getPrincipal();
/* 1170 */     if ((p instanceof TomcatPrincipal)) {
/*      */       try {
/* 1172 */         ((TomcatPrincipal)p).logout();
/*      */       } catch (Throwable t) {
/* 1174 */         ExceptionUtils.handleThrowable(t);
/* 1175 */         log.debug(sm.getString("authenticator.tomcatPrincipalLogoutFail"), t);
/*      */       }
/*      */     }
/*      */     
/* 1179 */     register(request, request.getResponse(), null, null, null, null);
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
/*      */   protected synchronized void startInternal()
/*      */     throws LifecycleException
/*      */   {
/* 1193 */     ServletContext servletContext = this.context.getServletContext();
/*      */     
/* 1195 */     this.jaspicAppContextID = (servletContext.getVirtualServerName() + " " + servletContext.getContextPath());
/*      */     
/*      */ 
/*      */ 
/* 1199 */     Container parent = this.context.getParent();
/* 1200 */     while ((this.sso == null) && (parent != null)) {
/* 1201 */       Valve[] valves = parent.getPipeline().getValves();
/* 1202 */       for (int i = 0; i < valves.length; i++) {
/* 1203 */         if ((valves[i] instanceof SingleSignOn)) {
/* 1204 */           this.sso = ((SingleSignOn)valves[i]);
/* 1205 */           break;
/*      */         }
/*      */       }
/* 1208 */       if (this.sso == null) {
/* 1209 */         parent = parent.getParent();
/*      */       }
/*      */     }
/* 1212 */     if (log.isDebugEnabled()) {
/* 1213 */       if (this.sso != null) {
/* 1214 */         log.debug("Found SingleSignOn Valve at " + this.sso);
/*      */       } else {
/* 1216 */         log.debug("No SingleSignOn Valve is present");
/*      */       }
/*      */     }
/*      */     
/* 1220 */     this.sessionIdGenerator = new StandardSessionIdGenerator();
/* 1221 */     this.sessionIdGenerator.setSecureRandomAlgorithm(getSecureRandomAlgorithm());
/* 1222 */     this.sessionIdGenerator.setSecureRandomClass(getSecureRandomClass());
/* 1223 */     this.sessionIdGenerator.setSecureRandomProvider(getSecureRandomProvider());
/*      */     
/* 1225 */     super.startInternal();
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
/*      */   protected synchronized void stopInternal()
/*      */     throws LifecycleException
/*      */   {
/* 1239 */     super.stopInternal();
/*      */     
/* 1241 */     this.sso = null;
/*      */   }
/*      */   
/*      */   private AuthConfigProvider getJaspicProvider()
/*      */   {
/* 1246 */     AuthConfigProvider provider = this.jaspicProvider;
/* 1247 */     if (provider == null) {
/* 1248 */       provider = findJaspicProvider();
/*      */     }
/* 1250 */     if (provider == NO_PROVIDER_AVAILABLE) {
/* 1251 */       return null;
/*      */     }
/* 1253 */     return provider;
/*      */   }
/*      */   
/*      */   private AuthConfigProvider findJaspicProvider()
/*      */   {
/* 1258 */     AuthConfigFactory factory = AuthConfigFactory.getFactory();
/* 1259 */     AuthConfigProvider provider = null;
/* 1260 */     if (factory != null) {
/* 1261 */       provider = factory.getConfigProvider("HttpServlet", this.jaspicAppContextID, this);
/*      */     }
/* 1263 */     if (provider == null) {
/* 1264 */       provider = NO_PROVIDER_AVAILABLE;
/*      */     }
/* 1266 */     this.jaspicProvider = provider;
/* 1267 */     return provider;
/*      */   }
/*      */   
/*      */ 
/*      */   public void notify(String layer, String appContext)
/*      */   {
/* 1273 */     findJaspicProvider();
/*      */   }
/*      */   
/*      */   private static class JaspicState
/*      */   {
/* 1278 */     public MessageInfo messageInfo = null;
/* 1279 */     public ServerAuthContext serverAuthContext = null;
/*      */   }
/*      */   
/*      */   private static class NoOpAuthConfigProvider
/*      */     implements AuthConfigProvider
/*      */   {
/*      */     public ClientAuthConfig getClientAuthConfig(String layer, String appContext, CallbackHandler handler)
/*      */       throws AuthException
/*      */     {
/* 1288 */       return null;
/*      */     }
/*      */     
/*      */     public ServerAuthConfig getServerAuthConfig(String layer, String appContext, CallbackHandler handler)
/*      */       throws AuthException
/*      */     {
/* 1294 */       return null;
/*      */     }
/*      */     
/*      */     public void refresh() {}
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\authenticator\AuthenticatorBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */