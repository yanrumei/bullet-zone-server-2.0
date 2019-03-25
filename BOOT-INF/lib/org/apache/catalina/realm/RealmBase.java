/*      */ package org.apache.catalina.realm;
/*      */ 
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.beans.PropertyChangeSupport;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.security.MessageDigest;
/*      */ import java.security.NoSuchAlgorithmException;
/*      */ import java.security.Principal;
/*      */ import java.security.cert.X509Certificate;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import javax.servlet.annotation.ServletSecurity.TransportGuarantee;
/*      */ import javax.servlet.http.HttpServletRequest;
/*      */ import org.apache.catalina.Container;
/*      */ import org.apache.catalina.Context;
/*      */ import org.apache.catalina.CredentialHandler;
/*      */ import org.apache.catalina.Engine;
/*      */ import org.apache.catalina.Host;
/*      */ import org.apache.catalina.LifecycleException;
/*      */ import org.apache.catalina.LifecycleState;
/*      */ import org.apache.catalina.Realm;
/*      */ import org.apache.catalina.Server;
/*      */ import org.apache.catalina.Service;
/*      */ import org.apache.catalina.Wrapper;
/*      */ import org.apache.catalina.connector.Connector;
/*      */ import org.apache.catalina.connector.Request;
/*      */ import org.apache.catalina.connector.Response;
/*      */ import org.apache.catalina.util.LifecycleMBeanBase;
/*      */ import org.apache.catalina.util.SessionConfig;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.tomcat.util.IntrospectionUtils;
/*      */ import org.apache.tomcat.util.buf.B2CConverter;
/*      */ import org.apache.tomcat.util.buf.HexUtils;
/*      */ import org.apache.tomcat.util.buf.MessageBytes;
/*      */ import org.apache.tomcat.util.descriptor.web.SecurityCollection;
/*      */ import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
/*      */ import org.apache.tomcat.util.res.StringManager;
/*      */ import org.apache.tomcat.util.security.ConcurrentMessageDigest;
/*      */ import org.apache.tomcat.util.security.MD5Encoder;
/*      */ import org.ietf.jgss.GSSContext;
/*      */ import org.ietf.jgss.GSSCredential;
/*      */ import org.ietf.jgss.GSSException;
/*      */ import org.ietf.jgss.GSSName;
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
/*      */ public abstract class RealmBase
/*      */   extends LifecycleMBeanBase
/*      */   implements Realm
/*      */ {
/*   76 */   private static final Log log = LogFactory.getLog(RealmBase.class);
/*      */   
/*   78 */   private static final List<Class<? extends DigestCredentialHandlerBase>> credentialHandlerClasses = new ArrayList();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static
/*      */   {
/*   85 */     credentialHandlerClasses.add(MessageDigestCredentialHandler.class);
/*   86 */     credentialHandlerClasses.add(SecretKeyCredentialHandler.class);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   95 */   protected Container container = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  101 */   protected Log containerLog = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private CredentialHandler credentialHandler;
/*      */   
/*      */ 
/*      */ 
/*  110 */   protected static final StringManager sm = StringManager.getManager(RealmBase.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  116 */   protected final PropertyChangeSupport support = new PropertyChangeSupport(this);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  122 */   protected boolean validate = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String x509UsernameRetrieverClassName;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected X509UsernameRetriever x509UsernameRetriever;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  138 */   protected AllRolesMode allRolesMode = AllRolesMode.STRICT_MODE;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  145 */   protected boolean stripRealmForGss = true;
/*      */   
/*      */ 
/*  148 */   private int transportGuaranteeRedirectStatus = 302;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getTransportGuaranteeRedirectStatus()
/*      */   {
/*  160 */     return this.transportGuaranteeRedirectStatus;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTransportGuaranteeRedirectStatus(int transportGuaranteeRedirectStatus)
/*      */   {
/*  172 */     this.transportGuaranteeRedirectStatus = transportGuaranteeRedirectStatus;
/*      */   }
/*      */   
/*      */ 
/*      */   public CredentialHandler getCredentialHandler()
/*      */   {
/*  178 */     return this.credentialHandler;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setCredentialHandler(CredentialHandler credentialHandler)
/*      */   {
/*  184 */     this.credentialHandler = credentialHandler;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Container getContainer()
/*      */   {
/*  194 */     return this.container;
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
/*      */   public void setContainer(Container container)
/*      */   {
/*  207 */     Container oldContainer = this.container;
/*  208 */     this.container = container;
/*  209 */     this.support.firePropertyChange("container", oldContainer, this.container);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getAllRolesMode()
/*      */   {
/*  218 */     return this.allRolesMode.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAllRolesMode(String allRolesMode)
/*      */   {
/*  227 */     this.allRolesMode = AllRolesMode.toMode(allRolesMode);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getValidate()
/*      */   {
/*  236 */     return this.validate;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setValidate(boolean validate)
/*      */   {
/*  247 */     this.validate = validate;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getX509UsernameRetrieverClassName()
/*      */   {
/*  258 */     return this.x509UsernameRetrieverClassName;
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
/*      */   public void setX509UsernameRetrieverClassName(String className)
/*      */   {
/*  271 */     this.x509UsernameRetrieverClassName = className;
/*      */   }
/*      */   
/*      */   public boolean isStripRealmForGss() {
/*  275 */     return this.stripRealmForGss;
/*      */   }
/*      */   
/*      */   public void setStripRealmForGss(boolean stripRealmForGss)
/*      */   {
/*  280 */     this.stripRealmForGss = stripRealmForGss;
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
/*      */   public void addPropertyChangeListener(PropertyChangeListener listener)
/*      */   {
/*  295 */     this.support.addPropertyChangeListener(listener);
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
/*      */   public Principal authenticate(String username)
/*      */   {
/*  309 */     if (username == null) {
/*  310 */       return null;
/*      */     }
/*      */     
/*  313 */     if (this.containerLog.isTraceEnabled()) {
/*  314 */       this.containerLog.trace(sm.getString("realmBase.authenticateSuccess", new Object[] { username }));
/*      */     }
/*      */     
/*  317 */     return getPrincipal(username);
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
/*      */   public Principal authenticate(String username, String credentials)
/*      */   {
/*  334 */     if ((username == null) || (credentials == null)) {
/*  335 */       if (this.containerLog.isTraceEnabled()) {
/*  336 */         this.containerLog.trace(sm.getString("realmBase.authenticateFailure", new Object[] { username }));
/*      */       }
/*      */       
/*  339 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  343 */     String serverCredentials = getPassword(username);
/*      */     
/*  345 */     if (serverCredentials == null)
/*      */     {
/*      */ 
/*  348 */       getCredentialHandler().mutate(credentials);
/*      */       
/*  350 */       if (this.containerLog.isTraceEnabled()) {
/*  351 */         this.containerLog.trace(sm.getString("realmBase.authenticateFailure", new Object[] { username }));
/*      */       }
/*      */       
/*  354 */       return null;
/*      */     }
/*      */     
/*  357 */     boolean validated = getCredentialHandler().matches(credentials, serverCredentials);
/*      */     
/*  359 */     if (validated) {
/*  360 */       if (this.containerLog.isTraceEnabled()) {
/*  361 */         this.containerLog.trace(sm.getString("realmBase.authenticateSuccess", new Object[] { username }));
/*      */       }
/*      */       
/*  364 */       return getPrincipal(username);
/*      */     }
/*  366 */     if (this.containerLog.isTraceEnabled()) {
/*  367 */       this.containerLog.trace(sm.getString("realmBase.authenticateFailure", new Object[] { username }));
/*      */     }
/*      */     
/*  370 */     return null;
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
/*      */   public Principal authenticate(String username, String clientDigest, String nonce, String nc, String cnonce, String qop, String realm, String md5a2)
/*      */   {
/*  399 */     String md5a1 = getDigest(username, realm);
/*  400 */     if (md5a1 == null)
/*  401 */       return null;
/*  402 */     md5a1 = md5a1.toLowerCase(Locale.ENGLISH);
/*      */     String serverDigestValue;
/*  404 */     String serverDigestValue; if (qop == null) {
/*  405 */       serverDigestValue = md5a1 + ":" + nonce + ":" + md5a2;
/*      */     } else {
/*  407 */       serverDigestValue = md5a1 + ":" + nonce + ":" + nc + ":" + cnonce + ":" + qop + ":" + md5a2;
/*      */     }
/*      */     
/*      */ 
/*  411 */     byte[] valueBytes = null;
/*      */     try {
/*  413 */       valueBytes = serverDigestValue.getBytes(getDigestCharset());
/*      */     } catch (UnsupportedEncodingException uee) {
/*  415 */       log.error("Illegal digestEncoding: " + getDigestEncoding(), uee);
/*  416 */       throw new IllegalArgumentException(uee.getMessage());
/*      */     }
/*      */     
/*  419 */     String serverDigest = MD5Encoder.encode(ConcurrentMessageDigest.digestMD5(new byte[][] { valueBytes }));
/*      */     
/*  421 */     if (log.isDebugEnabled()) {
/*  422 */       log.debug("Digest : " + clientDigest + " Username:" + username + " ClientDigest:" + clientDigest + " nonce:" + nonce + " nc:" + nc + " cnonce:" + cnonce + " qop:" + qop + " realm:" + realm + "md5a2:" + md5a2 + " Server digest:" + serverDigest);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  429 */     if (serverDigest.equals(clientDigest)) {
/*  430 */       return getPrincipal(username);
/*      */     }
/*      */     
/*  433 */     return null;
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
/*      */   public Principal authenticate(X509Certificate[] certs)
/*      */   {
/*  447 */     if ((certs == null) || (certs.length < 1)) {
/*  448 */       return null;
/*      */     }
/*      */     
/*  451 */     if (log.isDebugEnabled())
/*  452 */       log.debug("Authenticating client certificate chain");
/*  453 */     if (this.validate) {
/*  454 */       for (int i = 0; i < certs.length; i++) {
/*  455 */         if (log.isDebugEnabled())
/*  456 */           log.debug(" Checking validity for '" + certs[i]
/*  457 */             .getSubjectDN().getName() + "'");
/*      */         try {
/*  459 */           certs[i].checkValidity();
/*      */         } catch (Exception e) {
/*  461 */           if (log.isDebugEnabled())
/*  462 */             log.debug("  Validity exception", e);
/*  463 */           return null;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  469 */     return getPrincipal(certs[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Principal authenticate(GSSContext gssContext, boolean storeCreds)
/*      */   {
/*  479 */     if (gssContext.isEstablished()) {
/*  480 */       GSSName gssName = null;
/*      */       try {
/*  482 */         gssName = gssContext.getSrcName();
/*      */       } catch (GSSException e) {
/*  484 */         log.warn(sm.getString("realmBase.gssNameFail"), e);
/*      */       }
/*      */       
/*  487 */       if (gssName != null) {
/*  488 */         String name = gssName.toString();
/*      */         
/*  490 */         if (isStripRealmForGss()) {
/*  491 */           int i = name.indexOf('@');
/*  492 */           if (i > 0)
/*      */           {
/*  494 */             name = name.substring(0, i);
/*      */           }
/*      */         }
/*  497 */         GSSCredential gssCredential = null;
/*  498 */         if ((storeCreds) && (gssContext.getCredDelegState())) {
/*      */           try {
/*  500 */             gssCredential = gssContext.getDelegCred();
/*      */           } catch (GSSException e) {
/*  502 */             if (log.isDebugEnabled()) {
/*  503 */               log.debug(sm.getString("realmBase.delegatedCredentialFail", new Object[] { name }), e);
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  509 */         return getPrincipal(name, gssCredential);
/*      */       }
/*      */     } else {
/*  512 */       log.error(sm.getString("realmBase.gssContextNotEstablished"));
/*      */     }
/*      */     
/*      */ 
/*  516 */     return null;
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
/*      */   public SecurityConstraint[] findSecurityConstraints(Request request, Context context)
/*      */   {
/*  542 */     ArrayList<SecurityConstraint> results = null;
/*      */     
/*  544 */     SecurityConstraint[] constraints = context.findConstraints();
/*  545 */     if ((constraints == null) || (constraints.length == 0)) {
/*  546 */       if (log.isDebugEnabled())
/*  547 */         log.debug("  No applicable constraints defined");
/*  548 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  552 */     String uri = request.getRequestPathMB().toString();
/*      */     
/*      */ 
/*  555 */     if (uri == null) {
/*  556 */       uri = "/";
/*      */     }
/*      */     
/*  559 */     String method = request.getMethod();
/*      */     
/*  561 */     boolean found = false;
/*  562 */     for (int i = 0; i < constraints.length; i++) {
/*  563 */       SecurityCollection[] collection = constraints[i].findCollections();
/*      */       
/*      */ 
/*      */ 
/*  567 */       if (collection != null)
/*      */       {
/*      */ 
/*      */ 
/*  571 */         if (log.isDebugEnabled()) {
/*  572 */           log.debug("  Checking constraint '" + constraints[i] + "' against " + method + " " + uri + " --> " + constraints[i]
/*      */           
/*  574 */             .included(uri, method));
/*      */         }
/*      */         
/*  577 */         for (int j = 0; j < collection.length; j++) {
/*  578 */           String[] patterns = collection[j].findPatterns();
/*      */           
/*      */ 
/*      */ 
/*  582 */           if (patterns != null)
/*      */           {
/*      */ 
/*      */ 
/*  586 */             for (int k = 0; k < patterns.length; k++)
/*  587 */               if (uri.equals(patterns[k])) {
/*  588 */                 found = true;
/*  589 */                 if (collection[j].findMethod(method)) {
/*  590 */                   if (results == null) {
/*  591 */                     results = new ArrayList();
/*      */                   }
/*  593 */                   results.add(constraints[i]);
/*      */                 }
/*      */               }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  600 */     if (found) {
/*  601 */       return resultsToArray(results);
/*      */     }
/*      */     
/*  604 */     int longest = -1;
/*      */     
/*  606 */     for (i = 0; i < constraints.length; i++) {
/*  607 */       SecurityCollection[] collection = constraints[i].findCollections();
/*      */       
/*      */ 
/*      */ 
/*  611 */       if (collection != null)
/*      */       {
/*      */ 
/*      */ 
/*  615 */         if (log.isDebugEnabled()) {
/*  616 */           log.debug("  Checking constraint '" + constraints[i] + "' against " + method + " " + uri + " --> " + constraints[i]
/*      */           
/*  618 */             .included(uri, method));
/*      */         }
/*      */         
/*  621 */         for (int j = 0; j < collection.length; j++) {
/*  622 */           String[] patterns = collection[j].findPatterns();
/*      */           
/*      */ 
/*      */ 
/*  626 */           if (patterns != null)
/*      */           {
/*      */ 
/*      */ 
/*  630 */             boolean matched = false;
/*  631 */             int length = -1;
/*  632 */             for (int k = 0; k < patterns.length; k++) {
/*  633 */               String pattern = patterns[k];
/*  634 */               if ((pattern.startsWith("/")) && (pattern.endsWith("/*")) && 
/*  635 */                 (pattern.length() >= longest))
/*      */               {
/*  637 */                 if (pattern.length() == 2) {
/*  638 */                   matched = true;
/*  639 */                   length = pattern.length();
/*  640 */                 } else if ((pattern.regionMatches(0, uri, 0, pattern
/*  641 */                   .length() - 1)) || (
/*  642 */                   (pattern.length() - 2 == uri.length()) && 
/*  643 */                   (pattern.regionMatches(0, uri, 0, pattern
/*  644 */                   .length() - 2)))) {
/*  645 */                   matched = true;
/*  646 */                   length = pattern.length();
/*      */                 }
/*      */               }
/*      */             }
/*  650 */             if (matched) {
/*  651 */               if (length > longest) {
/*  652 */                 found = false;
/*  653 */                 if (results != null) {
/*  654 */                   results.clear();
/*      */                 }
/*  656 */                 longest = length;
/*      */               }
/*  658 */               if (collection[j].findMethod(method)) {
/*  659 */                 found = true;
/*  660 */                 if (results == null) {
/*  661 */                   results = new ArrayList();
/*      */                 }
/*  663 */                 results.add(constraints[i]);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       } }
/*  669 */     if (found) {
/*  670 */       return resultsToArray(results);
/*      */     }
/*      */     
/*  673 */     for (i = 0; i < constraints.length; i++) {
/*  674 */       SecurityCollection[] collection = constraints[i].findCollections();
/*      */       
/*      */ 
/*      */ 
/*  678 */       if (collection != null)
/*      */       {
/*      */ 
/*      */ 
/*  682 */         if (log.isDebugEnabled()) {
/*  683 */           log.debug("  Checking constraint '" + constraints[i] + "' against " + method + " " + uri + " --> " + constraints[i]
/*      */           
/*  685 */             .included(uri, method));
/*      */         }
/*      */         
/*  688 */         boolean matched = false;
/*  689 */         int pos = -1;
/*  690 */         for (int j = 0; j < collection.length; j++) {
/*  691 */           String[] patterns = collection[j].findPatterns();
/*      */           
/*      */ 
/*      */ 
/*  695 */           if (patterns != null)
/*      */           {
/*      */ 
/*      */ 
/*  699 */             for (int k = 0; (k < patterns.length) && (!matched); k++) {
/*  700 */               String pattern = patterns[k];
/*  701 */               if (pattern.startsWith("*.")) {
/*  702 */                 int slash = uri.lastIndexOf('/');
/*  703 */                 int dot = uri.lastIndexOf('.');
/*  704 */                 if ((slash >= 0) && (dot > slash) && 
/*  705 */                   (dot != uri.length() - 1) && 
/*  706 */                   (uri.length() - dot == pattern.length() - 1) && 
/*  707 */                   (pattern.regionMatches(1, uri, dot, uri.length() - dot))) {
/*  708 */                   matched = true;
/*  709 */                   pos = j;
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*  715 */         if (matched) {
/*  716 */           found = true;
/*  717 */           if (collection[pos].findMethod(method)) {
/*  718 */             if (results == null) {
/*  719 */               results = new ArrayList();
/*      */             }
/*  721 */             results.add(constraints[i]);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  726 */     if (found) {
/*  727 */       return resultsToArray(results);
/*      */     }
/*      */     
/*  730 */     for (i = 0; i < constraints.length; i++) {
/*  731 */       SecurityCollection[] collection = constraints[i].findCollections();
/*      */       
/*      */ 
/*      */ 
/*  735 */       if (collection != null)
/*      */       {
/*      */ 
/*      */ 
/*  739 */         if (log.isDebugEnabled()) {
/*  740 */           log.debug("  Checking constraint '" + constraints[i] + "' against " + method + " " + uri + " --> " + constraints[i]
/*      */           
/*  742 */             .included(uri, method));
/*      */         }
/*      */         
/*  745 */         for (int j = 0; j < collection.length; j++) {
/*  746 */           String[] patterns = collection[j].findPatterns();
/*      */           
/*      */ 
/*      */ 
/*  750 */           if (patterns != null)
/*      */           {
/*      */ 
/*      */ 
/*  754 */             boolean matched = false;
/*  755 */             for (int k = 0; (k < patterns.length) && (!matched); k++) {
/*  756 */               String pattern = patterns[k];
/*  757 */               if (pattern.equals("/")) {
/*  758 */                 matched = true;
/*      */               }
/*      */             }
/*  761 */             if (matched) {
/*  762 */               if (results == null) {
/*  763 */                 results = new ArrayList();
/*      */               }
/*  765 */               results.add(constraints[i]);
/*      */             }
/*      */           }
/*      */         }
/*      */       } }
/*  770 */     if (results == null)
/*      */     {
/*  772 */       if (log.isDebugEnabled())
/*  773 */         log.debug("  No applicable constraint located");
/*      */     }
/*  775 */     return resultsToArray(results);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private SecurityConstraint[] resultsToArray(ArrayList<SecurityConstraint> results)
/*      */   {
/*  783 */     if ((results == null) || (results.size() == 0)) {
/*  784 */       return null;
/*      */     }
/*  786 */     SecurityConstraint[] array = new SecurityConstraint[results.size()];
/*  787 */     results.toArray(array);
/*  788 */     return array;
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
/*      */   public boolean hasResourcePermission(Request request, Response response, SecurityConstraint[] constraints, Context context)
/*      */     throws IOException
/*      */   {
/*  811 */     if ((constraints == null) || (constraints.length == 0)) {
/*  812 */       return true;
/*      */     }
/*      */     
/*  815 */     Principal principal = request.getPrincipal();
/*  816 */     boolean status = false;
/*  817 */     boolean denyfromall = false;
/*  818 */     for (int i = 0; i < constraints.length; i++) {
/*  819 */       SecurityConstraint constraint = constraints[i];
/*      */       String[] roles;
/*      */       String[] roles;
/*  822 */       if (constraint.getAllRoles())
/*      */       {
/*  824 */         roles = request.getContext().findSecurityRoles();
/*      */       } else {
/*  826 */         roles = constraint.findAuthRoles();
/*      */       }
/*      */       
/*  829 */       if (roles == null) {
/*  830 */         roles = new String[0];
/*      */       }
/*  832 */       if (log.isDebugEnabled()) {
/*  833 */         log.debug("  Checking roles " + principal);
/*      */       }
/*  835 */       if ((constraint.getAuthenticatedUsers()) && (principal != null)) {
/*  836 */         if (log.isDebugEnabled()) {
/*  837 */           log.debug("Passing all authenticated users");
/*      */         }
/*  839 */         status = true;
/*  840 */       } else if ((roles.length == 0) && (!constraint.getAllRoles()) && 
/*  841 */         (!constraint.getAuthenticatedUsers())) {
/*  842 */         if (constraint.getAuthConstraint()) {
/*  843 */           if (log.isDebugEnabled())
/*  844 */             log.debug("No roles");
/*  845 */           status = false;
/*  846 */           denyfromall = true;
/*  847 */           break;
/*      */         }
/*      */         
/*  850 */         if (log.isDebugEnabled())
/*  851 */           log.debug("Passing all access");
/*  852 */         status = true;
/*  853 */       } else if (principal == null) {
/*  854 */         if (log.isDebugEnabled())
/*  855 */           log.debug("  No user authenticated, cannot grant access");
/*      */       } else {
/*  857 */         for (int j = 0; j < roles.length; j++) {
/*  858 */           if (hasRole(null, principal, roles[j])) {
/*  859 */             status = true;
/*  860 */             if (log.isDebugEnabled()) {
/*  861 */               log.debug("Role found:  " + roles[j]);
/*      */             }
/*  863 */           } else if (log.isDebugEnabled()) {
/*  864 */             log.debug("No role found:  " + roles[j]);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  869 */     if ((!denyfromall) && (this.allRolesMode != AllRolesMode.STRICT_MODE) && (!status) && (principal != null))
/*      */     {
/*  871 */       if (log.isDebugEnabled()) {
/*  872 */         log.debug("Checking for all roles mode: " + this.allRolesMode);
/*      */       }
/*      */       
/*  875 */       for (int i = 0; i < constraints.length; i++) {
/*  876 */         SecurityConstraint constraint = constraints[i];
/*      */         
/*      */ 
/*  879 */         if (constraint.getAllRoles()) {
/*  880 */           if (this.allRolesMode == AllRolesMode.AUTH_ONLY_MODE) {
/*  881 */             if (log.isDebugEnabled()) {
/*  882 */               log.debug("Granting access for role-name=*, auth-only");
/*      */             }
/*  884 */             status = true;
/*  885 */             break;
/*      */           }
/*      */           
/*      */ 
/*  889 */           String[] roles = request.getContext().findSecurityRoles();
/*  890 */           if ((roles.length == 0) && (this.allRolesMode == AllRolesMode.STRICT_AUTH_ONLY_MODE)) {
/*  891 */             if (log.isDebugEnabled()) {
/*  892 */               log.debug("Granting access for role-name=*, strict auth-only");
/*      */             }
/*  894 */             status = true;
/*  895 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  902 */     if (!status)
/*      */     {
/*  904 */       response.sendError(403, sm
/*  905 */         .getString("realmBase.forbidden"));
/*      */     }
/*  907 */     return status;
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
/*      */   public boolean hasRole(Wrapper wrapper, Principal principal, String role)
/*      */   {
/*  923 */     if (wrapper != null) {
/*  924 */       String realRole = wrapper.findSecurityReference(role);
/*  925 */       if (realRole != null) {
/*  926 */         role = realRole;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  931 */     if ((principal == null) || (role == null)) {
/*  932 */       return false;
/*      */     }
/*      */     
/*  935 */     boolean result = hasRoleInternal(principal, role);
/*      */     
/*  937 */     if (log.isDebugEnabled()) {
/*  938 */       String name = principal.getName();
/*  939 */       if (result) {
/*  940 */         log.debug(sm.getString("realmBase.hasRoleSuccess", new Object[] { name, role }));
/*      */       } else {
/*  942 */         log.debug(sm.getString("realmBase.hasRoleFailure", new Object[] { name, role }));
/*      */       }
/*      */     }
/*  945 */     return result;
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
/*      */   protected boolean hasRoleInternal(Principal principal, String role)
/*      */   {
/*  967 */     if (!(principal instanceof GenericPrincipal)) {
/*  968 */       return false;
/*      */     }
/*      */     
/*  971 */     GenericPrincipal gp = (GenericPrincipal)principal;
/*  972 */     return gp.hasRole(role);
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
/*      */   public boolean hasUserDataPermission(Request request, Response response, SecurityConstraint[] constraints)
/*      */     throws IOException
/*      */   {
/*  995 */     if ((constraints == null) || (constraints.length == 0)) {
/*  996 */       if (log.isDebugEnabled())
/*  997 */         log.debug("  No applicable security constraint defined");
/*  998 */       return true;
/*      */     }
/* 1000 */     for (int i = 0; i < constraints.length; i++) {
/* 1001 */       SecurityConstraint constraint = constraints[i];
/* 1002 */       String userConstraint = constraint.getUserConstraint();
/* 1003 */       if (userConstraint == null) {
/* 1004 */         if (log.isDebugEnabled())
/* 1005 */           log.debug("  No applicable user data constraint defined");
/* 1006 */         return true;
/*      */       }
/* 1008 */       if (userConstraint.equals(ServletSecurity.TransportGuarantee.NONE.name())) {
/* 1009 */         if (log.isDebugEnabled())
/* 1010 */           log.debug("  User data constraint has no restrictions");
/* 1011 */         return true;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1016 */     if (request.getRequest().isSecure()) {
/* 1017 */       if (log.isDebugEnabled())
/* 1018 */         log.debug("  User data constraint already satisfied");
/* 1019 */       return true;
/*      */     }
/*      */     
/* 1022 */     int redirectPort = request.getConnector().getRedirectPort();
/*      */     
/*      */ 
/* 1025 */     if (redirectPort <= 0) {
/* 1026 */       if (log.isDebugEnabled()) {
/* 1027 */         log.debug("  SSL redirect is disabled");
/*      */       }
/* 1029 */       response.sendError(403, request
/* 1030 */         .getRequestURI());
/* 1031 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1035 */     StringBuilder file = new StringBuilder();
/* 1036 */     String protocol = "https";
/* 1037 */     String host = request.getServerName();
/*      */     
/* 1039 */     file.append(protocol).append("://").append(host);
/*      */     
/* 1041 */     if (redirectPort != 443) {
/* 1042 */       file.append(":").append(redirectPort);
/*      */     }
/*      */     
/* 1045 */     file.append(request.getRequestURI());
/* 1046 */     String requestedSessionId = request.getRequestedSessionId();
/* 1047 */     if ((requestedSessionId != null) && 
/* 1048 */       (request.isRequestedSessionIdFromURL())) {
/* 1049 */       file.append(";");
/* 1050 */       file.append(SessionConfig.getSessionUriParamName(request
/* 1051 */         .getContext()));
/* 1052 */       file.append("=");
/* 1053 */       file.append(requestedSessionId);
/*      */     }
/* 1055 */     String queryString = request.getQueryString();
/* 1056 */     if (queryString != null) {
/* 1057 */       file.append('?');
/* 1058 */       file.append(queryString);
/*      */     }
/* 1060 */     if (log.isDebugEnabled())
/* 1061 */       log.debug("  Redirecting to " + file.toString());
/* 1062 */     response.sendRedirect(file.toString(), this.transportGuaranteeRedirectStatus);
/* 1063 */     return false;
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
/*      */   public void removePropertyChangeListener(PropertyChangeListener listener)
/*      */   {
/* 1076 */     this.support.removePropertyChangeListener(listener);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean isAvailable()
/*      */   {
/* 1083 */     return true;
/*      */   }
/*      */   
/*      */   protected void initInternal()
/*      */     throws LifecycleException
/*      */   {
/* 1089 */     super.initInternal();
/*      */     
/*      */ 
/* 1092 */     if (this.container != null) {
/* 1093 */       this.containerLog = this.container.getLogger();
/*      */     }
/*      */     
/* 1096 */     this.x509UsernameRetriever = createUsernameRetriever(this.x509UsernameRetrieverClassName);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void startInternal()
/*      */     throws LifecycleException
/*      */   {
/* 1109 */     if (this.credentialHandler == null) {
/* 1110 */       this.credentialHandler = new MessageDigestCredentialHandler();
/*      */     }
/*      */     
/* 1113 */     setState(LifecycleState.STARTING);
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
/*      */   protected void stopInternal()
/*      */     throws LifecycleException
/*      */   {
/* 1127 */     setState(LifecycleState.STOPPING);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1136 */     StringBuilder sb = new StringBuilder("Realm[");
/* 1137 */     sb.append(getName());
/* 1138 */     sb.append(']');
/* 1139 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected boolean hasMessageDigest()
/*      */   {
/* 1146 */     CredentialHandler ch = this.credentialHandler;
/* 1147 */     if ((ch instanceof MessageDigestCredentialHandler)) {
/* 1148 */       return ((MessageDigestCredentialHandler)ch).getAlgorithm() != null;
/*      */     }
/* 1150 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getDigest(String username, String realmName)
/*      */   {
/* 1161 */     if (hasMessageDigest())
/*      */     {
/* 1163 */       return getPassword(username);
/*      */     }
/*      */     
/*      */ 
/* 1167 */     String digestValue = username + ":" + realmName + ":" + getPassword(username);
/*      */     
/* 1169 */     byte[] valueBytes = null;
/*      */     try {
/* 1171 */       valueBytes = digestValue.getBytes(getDigestCharset());
/*      */     } catch (UnsupportedEncodingException uee) {
/* 1173 */       log.error("Illegal digestEncoding: " + getDigestEncoding(), uee);
/* 1174 */       throw new IllegalArgumentException(uee.getMessage());
/*      */     }
/*      */     
/* 1177 */     return MD5Encoder.encode(ConcurrentMessageDigest.digestMD5(new byte[][] { valueBytes }));
/*      */   }
/*      */   
/*      */   private String getDigestEncoding()
/*      */   {
/* 1182 */     CredentialHandler ch = this.credentialHandler;
/* 1183 */     if ((ch instanceof MessageDigestCredentialHandler)) {
/* 1184 */       return ((MessageDigestCredentialHandler)ch).getEncoding();
/*      */     }
/* 1186 */     return null;
/*      */   }
/*      */   
/*      */   private Charset getDigestCharset() throws UnsupportedEncodingException
/*      */   {
/* 1191 */     String charset = getDigestEncoding();
/* 1192 */     if (charset == null) {
/* 1193 */       return StandardCharsets.ISO_8859_1;
/*      */     }
/* 1195 */     return B2CConverter.getCharset(charset);
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
/*      */   protected Principal getPrincipal(X509Certificate usercert)
/*      */   {
/* 1225 */     String username = this.x509UsernameRetriever.getUsername(usercert);
/*      */     
/* 1227 */     if (log.isDebugEnabled()) {
/* 1228 */       log.debug(sm.getString("realmBase.gotX509Username", new Object[] { username }));
/*      */     }
/* 1230 */     return getPrincipal(username);
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
/*      */   protected Principal getPrincipal(String username, GSSCredential gssCredential)
/*      */   {
/* 1244 */     Principal p = getPrincipal(username);
/*      */     
/* 1246 */     if ((p instanceof GenericPrincipal)) {
/* 1247 */       ((GenericPrincipal)p).setGssCredential(gssCredential);
/*      */     }
/*      */     
/* 1250 */     return p;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Server getServer()
/*      */   {
/* 1261 */     Container c = this.container;
/* 1262 */     if ((c instanceof Context)) {
/* 1263 */       c = c.getParent();
/*      */     }
/* 1265 */     if ((c instanceof Host)) {
/* 1266 */       c = c.getParent();
/*      */     }
/* 1268 */     if ((c instanceof Engine)) {
/* 1269 */       Service s = ((Engine)c).getService();
/* 1270 */       if (s != null) {
/* 1271 */         return s.getServer();
/*      */       }
/*      */     }
/* 1274 */     return null;
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
/*      */   @Deprecated
/*      */   public static final String Digest(String credentials, String algorithm, String encoding)
/*      */   {
/*      */     try
/*      */     {
/* 1301 */       MessageDigest md = (MessageDigest)MessageDigest.getInstance(algorithm).clone();
/*      */       
/*      */ 
/*      */ 
/* 1305 */       if (encoding == null) {
/* 1306 */         md.update(credentials.getBytes());
/*      */       } else {
/* 1308 */         md.update(credentials.getBytes(encoding));
/*      */       }
/*      */       
/*      */ 
/* 1312 */       return HexUtils.toHexString(md.digest());
/*      */     } catch (Exception ex) {
/* 1314 */       log.error(ex); }
/* 1315 */     return credentials;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void main(String[] args)
/*      */   {
/* 1359 */     int saltLength = -1;
/* 1360 */     int iterations = -1;
/* 1361 */     int keyLength = -1;
/*      */     
/* 1363 */     String encoding = Charset.defaultCharset().name();
/*      */     
/*      */ 
/* 1366 */     String algorithm = null;
/* 1367 */     String handlerClassName = null;
/*      */     
/* 1369 */     if (args.length == 0) {
/* 1370 */       usage();
/* 1371 */       return;
/*      */     }
/*      */     
/* 1374 */     int argIndex = 0;
/*      */     
/* 1376 */     while ((args.length > argIndex + 2) && (args[argIndex].length() == 2) && 
/* 1377 */       (args[argIndex].charAt(0) == '-')) {
/* 1378 */       switch (args[argIndex].charAt(1)) {
/*      */       case 'a': 
/* 1380 */         algorithm = args[(argIndex + 1)];
/* 1381 */         break;
/*      */       
/*      */       case 'e': 
/* 1384 */         encoding = args[(argIndex + 1)];
/* 1385 */         break;
/*      */       
/*      */       case 'i': 
/* 1388 */         iterations = Integer.parseInt(args[(argIndex + 1)]);
/* 1389 */         break;
/*      */       
/*      */       case 's': 
/* 1392 */         saltLength = Integer.parseInt(args[(argIndex + 1)]);
/* 1393 */         break;
/*      */       
/*      */       case 'k': 
/* 1396 */         keyLength = Integer.parseInt(args[(argIndex + 1)]);
/* 1397 */         break;
/*      */       
/*      */       case 'h': 
/* 1400 */         handlerClassName = args[(argIndex + 1)];
/* 1401 */         break;
/*      */       case 'b': case 'c': case 'd': case 'f': case 'g': case 'j': case 'l': 
/*      */       case 'm': case 'n': case 'o': case 'p': case 'q': case 'r': default: 
/* 1404 */         usage();
/* 1405 */         return;
/*      */       }
/*      */       
/* 1408 */       argIndex += 2;
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
/* 1420 */     if ((algorithm == null) && (handlerClassName == null)) {
/* 1421 */       algorithm = "SHA-512";
/*      */     }
/*      */     
/* 1424 */     CredentialHandler handler = null;
/*      */     
/* 1426 */     if (handlerClassName == null) {
/* 1427 */       for (Class<? extends DigestCredentialHandlerBase> clazz : credentialHandlerClasses) {
/*      */         try {
/* 1429 */           handler = (CredentialHandler)clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/* 1430 */           if (IntrospectionUtils.setProperty(handler, "algorithm", algorithm)) {
/*      */             break;
/*      */           }
/*      */         }
/*      */         catch (ReflectiveOperationException e) {
/* 1435 */           throw new RuntimeException(e);
/*      */         }
/*      */       }
/*      */     } else {
/*      */       try {
/* 1440 */         Object clazz = Class.forName(handlerClassName);
/* 1441 */         handler = (DigestCredentialHandlerBase)((Class)clazz).getConstructor(new Class[0]).newInstance(new Object[0]);
/* 1442 */         IntrospectionUtils.setProperty(handler, "algorithm", algorithm);
/*      */       } catch (ReflectiveOperationException e) {
/* 1444 */         throw new RuntimeException(e);
/*      */       }
/*      */     }
/*      */     
/* 1448 */     if (handler == null) {
/* 1449 */       throw new RuntimeException(new NoSuchAlgorithmException(algorithm));
/*      */     }
/*      */     
/* 1452 */     IntrospectionUtils.setProperty(handler, "encoding", encoding);
/* 1453 */     if (iterations > 0) {
/* 1454 */       IntrospectionUtils.setProperty(handler, "iterations", Integer.toString(iterations));
/*      */     }
/* 1456 */     if (saltLength > -1) {
/* 1457 */       IntrospectionUtils.setProperty(handler, "saltLength", Integer.toString(saltLength));
/*      */     }
/* 1459 */     if (keyLength > 0) {
/* 1460 */       IntrospectionUtils.setProperty(handler, "keyLength", Integer.toString(keyLength));
/*      */     }
/* 1463 */     for (; 
/* 1463 */         argIndex < args.length; argIndex++) {
/* 1464 */       String credential = args[argIndex];
/* 1465 */       System.out.print(credential + ":");
/* 1466 */       System.out.println(handler.mutate(credential));
/*      */     }
/*      */   }
/*      */   
/*      */   private static void usage()
/*      */   {
/* 1472 */     System.out.println("Usage: RealmBase [-a <algorithm>] [-e <encoding>] [-i <iterations>] [-s <salt-length>] [-k <key-length>] [-h <handler-class-name>] <credentials>");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getObjectNameKeyProperties()
/*      */   {
/* 1483 */     StringBuilder keyProperties = new StringBuilder("type=Realm");
/* 1484 */     keyProperties.append(getRealmSuffix());
/* 1485 */     keyProperties.append(this.container.getMBeanKeyProperties());
/*      */     
/* 1487 */     return keyProperties.toString();
/*      */   }
/*      */   
/*      */   public String getDomainInternal()
/*      */   {
/* 1492 */     return this.container.getDomain();
/*      */   }
/*      */   
/* 1495 */   protected String realmPath = "/realm0";
/*      */   
/*      */   public String getRealmPath() {
/* 1498 */     return this.realmPath;
/*      */   }
/*      */   
/*      */   public void setRealmPath(String theRealmPath) {
/* 1502 */     this.realmPath = theRealmPath;
/*      */   }
/*      */   
/*      */   protected String getRealmSuffix() {
/* 1506 */     return ",realmPath=" + getRealmPath();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected static class AllRolesMode
/*      */   {
/*      */     private final String name;
/*      */     
/*      */ 
/* 1516 */     public static final AllRolesMode STRICT_MODE = new AllRolesMode("strict");
/*      */     
/*      */ 
/* 1519 */     public static final AllRolesMode AUTH_ONLY_MODE = new AllRolesMode("authOnly");
/*      */     
/*      */ 
/* 1522 */     public static final AllRolesMode STRICT_AUTH_ONLY_MODE = new AllRolesMode("strictAuthOnly");
/*      */     
/*      */     static AllRolesMode toMode(String name)
/*      */     {
/*      */       AllRolesMode mode;
/* 1527 */       if (name.equalsIgnoreCase(STRICT_MODE.name)) {
/* 1528 */         mode = STRICT_MODE; } else { AllRolesMode mode;
/* 1529 */         if (name.equalsIgnoreCase(AUTH_ONLY_MODE.name)) {
/* 1530 */           mode = AUTH_ONLY_MODE; } else { AllRolesMode mode;
/* 1531 */           if (name.equalsIgnoreCase(STRICT_AUTH_ONLY_MODE.name)) {
/* 1532 */             mode = STRICT_AUTH_ONLY_MODE;
/*      */           } else
/* 1534 */             throw new IllegalStateException("Unknown mode, must be one of: strict, authOnly, strictAuthOnly"); } }
/* 1535 */       AllRolesMode mode; return mode;
/*      */     }
/*      */     
/*      */     private AllRolesMode(String name)
/*      */     {
/* 1540 */       this.name = name;
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean equals(Object o)
/*      */     {
/* 1546 */       boolean equals = false;
/* 1547 */       if ((o instanceof AllRolesMode))
/*      */       {
/* 1549 */         AllRolesMode mode = (AllRolesMode)o;
/* 1550 */         equals = this.name.equals(mode.name);
/*      */       }
/* 1552 */       return equals;
/*      */     }
/*      */     
/*      */     public int hashCode()
/*      */     {
/* 1557 */       return this.name.hashCode();
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/* 1562 */       return this.name;
/*      */     }
/*      */   }
/*      */   
/*      */   private static X509UsernameRetriever createUsernameRetriever(String className) throws LifecycleException
/*      */   {
/* 1568 */     if ((null == className) || ("".equals(className.trim()))) {
/* 1569 */       return new X509SubjectDnRetriever();
/*      */     }
/*      */     try
/*      */     {
/* 1573 */       Class<? extends X509UsernameRetriever> clazz = Class.forName(className);
/* 1574 */       return (X509UsernameRetriever)clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/*      */     } catch (ReflectiveOperationException e) {
/* 1576 */       throw new LifecycleException(sm.getString("realmBase.createUsernameRetriever.newInstance", new Object[] { className }), e);
/*      */     } catch (ClassCastException e) {
/* 1578 */       throw new LifecycleException(sm.getString("realmBase.createUsernameRetriever.ClassCastException", new Object[] { className }), e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public String[] getRoles(Principal principal)
/*      */   {
/* 1585 */     if ((principal instanceof GenericPrincipal)) {
/* 1586 */       return ((GenericPrincipal)principal).getRoles();
/*      */     }
/*      */     
/* 1589 */     String className = principal.getClass().getSimpleName();
/* 1590 */     throw new IllegalStateException(sm.getString("realmBase.cannotGetRoles", new Object[] { className }));
/*      */   }
/*      */   
/*      */   public void backgroundProcess() {}
/*      */   
/*      */   @Deprecated
/*      */   protected abstract String getName();
/*      */   
/*      */   protected abstract String getPassword(String paramString);
/*      */   
/*      */   protected abstract Principal getPrincipal(String paramString);
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\realm\RealmBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */