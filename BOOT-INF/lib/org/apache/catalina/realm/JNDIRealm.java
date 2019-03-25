/*      */ package org.apache.catalina.realm;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.security.GeneralSecurityException;
/*      */ import java.security.KeyManagementException;
/*      */ import java.security.NoSuchAlgorithmException;
/*      */ import java.security.Principal;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import javax.naming.AuthenticationException;
/*      */ import javax.naming.CommunicationException;
/*      */ import javax.naming.CompositeName;
/*      */ import javax.naming.InvalidNameException;
/*      */ import javax.naming.Name;
/*      */ import javax.naming.NameNotFoundException;
/*      */ import javax.naming.NameParser;
/*      */ import javax.naming.NamingEnumeration;
/*      */ import javax.naming.NamingException;
/*      */ import javax.naming.PartialResultException;
/*      */ import javax.naming.ServiceUnavailableException;
/*      */ import javax.naming.directory.Attribute;
/*      */ import javax.naming.directory.Attributes;
/*      */ import javax.naming.directory.DirContext;
/*      */ import javax.naming.directory.InitialDirContext;
/*      */ import javax.naming.directory.SearchControls;
/*      */ import javax.naming.directory.SearchResult;
/*      */ import javax.naming.ldap.InitialLdapContext;
/*      */ import javax.naming.ldap.LdapContext;
/*      */ import javax.naming.ldap.StartTlsRequest;
/*      */ import javax.naming.ldap.StartTlsResponse;
/*      */ import javax.net.ssl.HostnameVerifier;
/*      */ import javax.net.ssl.SSLContext;
/*      */ import javax.net.ssl.SSLParameters;
/*      */ import javax.net.ssl.SSLSession;
/*      */ import javax.net.ssl.SSLSocketFactory;
/*      */ import org.apache.catalina.CredentialHandler;
/*      */ import org.apache.catalina.LifecycleException;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.tomcat.util.res.StringManager;
/*      */ import org.ietf.jgss.GSSCredential;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class JNDIRealm
/*      */   extends RealmBase
/*      */ {
/*  191 */   protected String authentication = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  196 */   protected String connectionName = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  202 */   protected String connectionPassword = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  208 */   protected String connectionURL = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  214 */   protected DirContext context = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  222 */   protected String contextFactory = "com.sun.jndi.ldap.LdapCtxFactory";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  228 */   protected String derefAliases = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final String DEREF_ALIASES = "java.naming.ldap.derefAliases";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   protected static final String name = "JNDIRealm";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  249 */   protected String protocol = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  259 */   protected boolean adCompat = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  268 */   protected String referrals = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  274 */   protected String userBase = "";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  281 */   protected String userSearch = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  290 */   private boolean userSearchAsUser = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  297 */   protected MessageFormat userSearchFormat = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  303 */   protected boolean userSubtree = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  309 */   protected String userPassword = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  317 */   protected String userRoleAttribute = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  328 */   protected String[] userPatternArray = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  336 */   protected String userPattern = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  343 */   protected MessageFormat[] userPatternFormatArray = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  348 */   protected String roleBase = "";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  355 */   protected MessageFormat roleBaseFormat = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  362 */   protected MessageFormat roleFormat = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  369 */   protected String userRoleName = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  375 */   protected String roleName = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  383 */   protected String roleSearch = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  389 */   protected boolean roleSubtree = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  394 */   protected boolean roleNested = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  402 */   protected boolean roleSearchAsUser = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String alternateURL;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  413 */   protected int connectionAttempt = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  418 */   protected String commonRole = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  425 */   protected String connectionTimeout = "5000";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  431 */   protected String readTimeout = "5000";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  437 */   protected long sizeLimit = 0L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  443 */   protected int timeLimit = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  450 */   protected boolean useDelegatedCredential = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  459 */   protected String spnegoDelegationQop = "auth-conf";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  464 */   private boolean useStartTls = false;
/*      */   
/*  466 */   private StartTlsResponse tls = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  472 */   private String[] cipherSuitesArray = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  478 */   private HostnameVerifier hostnameVerifier = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  483 */   private SSLSocketFactory sslSocketFactory = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String sslSocketFactoryClassName;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String cipherSuites;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String hostNameVerifierClassName;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String sslProtocol;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getAuthentication()
/*      */   {
/*  515 */     return this.authentication;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAuthentication(String authentication)
/*      */   {
/*  526 */     this.authentication = authentication;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getConnectionName()
/*      */   {
/*  535 */     return this.connectionName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setConnectionName(String connectionName)
/*      */   {
/*  547 */     this.connectionName = connectionName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getConnectionPassword()
/*      */   {
/*  557 */     return this.connectionPassword;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setConnectionPassword(String connectionPassword)
/*      */   {
/*  569 */     this.connectionPassword = connectionPassword;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getConnectionURL()
/*      */   {
/*  579 */     return this.connectionURL;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setConnectionURL(String connectionURL)
/*      */   {
/*  591 */     this.connectionURL = connectionURL;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getContextFactory()
/*      */   {
/*  601 */     return this.contextFactory;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setContextFactory(String contextFactory)
/*      */   {
/*  613 */     this.contextFactory = contextFactory;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDerefAliases()
/*      */   {
/*  621 */     return this.derefAliases;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDerefAliases(String derefAliases)
/*      */   {
/*  630 */     this.derefAliases = derefAliases;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getProtocol()
/*      */   {
/*  638 */     return this.protocol;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setProtocol(String protocol)
/*      */   {
/*  649 */     this.protocol = protocol;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getAdCompat()
/*      */   {
/*  658 */     return this.adCompat;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAdCompat(boolean adCompat)
/*      */   {
/*  668 */     this.adCompat = adCompat;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getReferrals()
/*      */   {
/*  676 */     return this.referrals;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setReferrals(String referrals)
/*      */   {
/*  686 */     this.referrals = referrals;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getUserBase()
/*      */   {
/*  695 */     return this.userBase;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUserBase(String userBase)
/*      */   {
/*  707 */     this.userBase = userBase;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getUserSearch()
/*      */   {
/*  717 */     return this.userSearch;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUserSearch(String userSearch)
/*      */   {
/*  729 */     this.userSearch = userSearch;
/*  730 */     if (userSearch == null) {
/*  731 */       this.userSearchFormat = null;
/*      */     } else {
/*  733 */       this.userSearchFormat = new MessageFormat(userSearch);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isUserSearchAsUser()
/*      */   {
/*  739 */     return this.userSearchAsUser;
/*      */   }
/*      */   
/*      */   public void setUserSearchAsUser(boolean userSearchAsUser)
/*      */   {
/*  744 */     this.userSearchAsUser = userSearchAsUser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getUserSubtree()
/*      */   {
/*  753 */     return this.userSubtree;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUserSubtree(boolean userSubtree)
/*      */   {
/*  765 */     this.userSubtree = userSubtree;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getUserRoleName()
/*      */   {
/*  775 */     return this.userRoleName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUserRoleName(String userRoleName)
/*      */   {
/*  786 */     this.userRoleName = userRoleName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getRoleBase()
/*      */   {
/*  796 */     return this.roleBase;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRoleBase(String roleBase)
/*      */   {
/*  808 */     this.roleBase = roleBase;
/*  809 */     if (roleBase == null) {
/*  810 */       this.roleBaseFormat = null;
/*      */     } else {
/*  812 */       this.roleBaseFormat = new MessageFormat(roleBase);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getRoleName()
/*      */   {
/*  822 */     return this.roleName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRoleName(String roleName)
/*      */   {
/*  834 */     this.roleName = roleName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getRoleSearch()
/*      */   {
/*  844 */     return this.roleSearch;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRoleSearch(String roleSearch)
/*      */   {
/*  856 */     this.roleSearch = roleSearch;
/*  857 */     if (roleSearch == null) {
/*  858 */       this.roleFormat = null;
/*      */     } else {
/*  860 */       this.roleFormat = new MessageFormat(roleSearch);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isRoleSearchAsUser()
/*      */   {
/*  866 */     return this.roleSearchAsUser;
/*      */   }
/*      */   
/*      */   public void setRoleSearchAsUser(boolean roleSearchAsUser)
/*      */   {
/*  871 */     this.roleSearchAsUser = roleSearchAsUser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getRoleSubtree()
/*      */   {
/*  880 */     return this.roleSubtree;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRoleSubtree(boolean roleSubtree)
/*      */   {
/*  892 */     this.roleSubtree = roleSubtree;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getRoleNested()
/*      */   {
/*  901 */     return this.roleNested;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRoleNested(boolean roleNested)
/*      */   {
/*  913 */     this.roleNested = roleNested;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getUserPassword()
/*      */   {
/*  923 */     return this.userPassword;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUserPassword(String userPassword)
/*      */   {
/*  935 */     this.userPassword = userPassword;
/*      */   }
/*      */   
/*      */ 
/*      */   public String getUserRoleAttribute()
/*      */   {
/*  941 */     return this.userRoleAttribute;
/*      */   }
/*      */   
/*      */   public void setUserRoleAttribute(String userRoleAttribute) {
/*  945 */     this.userRoleAttribute = userRoleAttribute;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getUserPattern()
/*      */   {
/*  953 */     return this.userPattern;
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
/*      */   public void setUserPattern(String userPattern)
/*      */   {
/*  972 */     this.userPattern = userPattern;
/*  973 */     if (userPattern == null) {
/*  974 */       this.userPatternArray = null;
/*      */     } else {
/*  976 */       this.userPatternArray = parseUserPatternString(userPattern);
/*  977 */       int len = this.userPatternArray.length;
/*  978 */       this.userPatternFormatArray = new MessageFormat[len];
/*  979 */       for (int i = 0; i < len; i++) {
/*  980 */         this.userPatternFormatArray[i] = new MessageFormat(this.userPatternArray[i]);
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
/*      */   public String getAlternateURL()
/*      */   {
/*  994 */     return this.alternateURL;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAlternateURL(String alternateURL)
/*      */   {
/* 1006 */     this.alternateURL = alternateURL;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getCommonRole()
/*      */   {
/* 1016 */     return this.commonRole;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCommonRole(String commonRole)
/*      */   {
/* 1028 */     this.commonRole = commonRole;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getConnectionTimeout()
/*      */   {
/* 1038 */     return this.connectionTimeout;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setConnectionTimeout(String timeout)
/*      */   {
/* 1050 */     this.connectionTimeout = timeout;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getReadTimeout()
/*      */   {
/* 1059 */     return this.readTimeout;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setReadTimeout(String timeout)
/*      */   {
/* 1071 */     this.readTimeout = timeout;
/*      */   }
/*      */   
/*      */ 
/*      */   public long getSizeLimit()
/*      */   {
/* 1077 */     return this.sizeLimit;
/*      */   }
/*      */   
/*      */   public void setSizeLimit(long sizeLimit)
/*      */   {
/* 1082 */     this.sizeLimit = sizeLimit;
/*      */   }
/*      */   
/*      */   public int getTimeLimit()
/*      */   {
/* 1087 */     return this.timeLimit;
/*      */   }
/*      */   
/*      */   public void setTimeLimit(int timeLimit)
/*      */   {
/* 1092 */     this.timeLimit = timeLimit;
/*      */   }
/*      */   
/*      */   public boolean isUseDelegatedCredential()
/*      */   {
/* 1097 */     return this.useDelegatedCredential;
/*      */   }
/*      */   
/*      */   public void setUseDelegatedCredential(boolean useDelegatedCredential) {
/* 1101 */     this.useDelegatedCredential = useDelegatedCredential;
/*      */   }
/*      */   
/*      */   public String getSpnegoDelegationQop()
/*      */   {
/* 1106 */     return this.spnegoDelegationQop;
/*      */   }
/*      */   
/*      */   public void setSpnegoDelegationQop(String spnegoDelegationQop) {
/* 1110 */     this.spnegoDelegationQop = spnegoDelegationQop;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getUseStartTls()
/*      */   {
/* 1118 */     return this.useStartTls;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseStartTls(boolean useStartTls)
/*      */   {
/* 1129 */     this.useStartTls = useStartTls;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private String[] getCipherSuitesArray()
/*      */   {
/* 1137 */     if ((this.cipherSuites == null) || (this.cipherSuitesArray != null)) {
/* 1138 */       return this.cipherSuitesArray;
/*      */     }
/* 1140 */     if (this.cipherSuites.trim().isEmpty()) {
/* 1141 */       this.containerLog.warn(sm.getString("jndiRealm.emptyCipherSuites"));
/* 1142 */       this.cipherSuitesArray = null;
/*      */     } else {
/* 1144 */       this.cipherSuitesArray = this.cipherSuites.trim().split("\\s*,\\s*");
/* 1145 */       this.containerLog.debug(sm.getString("jndiRealm.cipherSuites", new Object[] {
/* 1146 */         Arrays.toString(this.cipherSuitesArray) }));
/*      */     }
/* 1148 */     return this.cipherSuitesArray;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCipherSuites(String suites)
/*      */   {
/* 1159 */     this.cipherSuites = suites;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getHostnameVerifierClassName()
/*      */   {
/* 1168 */     if (this.hostnameVerifier == null) {
/* 1169 */       return "";
/*      */     }
/* 1171 */     return this.hostnameVerifier.getClass().getCanonicalName();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setHostnameVerifierClassName(String verifierClassName)
/*      */   {
/* 1183 */     if (verifierClassName != null) {
/* 1184 */       this.hostNameVerifierClassName = verifierClassName.trim();
/*      */     } else {
/* 1186 */       this.hostNameVerifierClassName = null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public HostnameVerifier getHostnameVerifier()
/*      */   {
/* 1195 */     if (this.hostnameVerifier != null) {
/* 1196 */       return this.hostnameVerifier;
/*      */     }
/* 1198 */     if ((this.hostNameVerifierClassName == null) || 
/* 1199 */       (this.hostNameVerifierClassName.equals(""))) {
/* 1200 */       return null;
/*      */     }
/*      */     try {
/* 1203 */       Object o = constructInstance(this.hostNameVerifierClassName);
/* 1204 */       if ((o instanceof HostnameVerifier)) {
/* 1205 */         this.hostnameVerifier = ((HostnameVerifier)o);
/* 1206 */         return this.hostnameVerifier;
/*      */       }
/* 1208 */       throw new IllegalArgumentException(sm.getString("jndiRealm.invalidHostnameVerifier", new Object[] { this.hostNameVerifierClassName }));
/*      */ 
/*      */     }
/*      */     catch (ReflectiveOperationException|SecurityException e)
/*      */     {
/* 1213 */       throw new IllegalArgumentException(sm.getString("jndiRealm.invalidHostnameVerifier", new Object[] { this.hostNameVerifierClassName }), e);
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
/*      */   public void setSslSocketFactoryClassName(String factoryClassName)
/*      */   {
/* 1229 */     this.sslSocketFactoryClassName = factoryClassName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSslProtocol(String protocol)
/*      */   {
/* 1239 */     this.sslProtocol = protocol;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private String[] getSupportedSslProtocols()
/*      */   {
/*      */     try
/*      */     {
/* 1248 */       SSLContext sslContext = SSLContext.getDefault();
/* 1249 */       return sslContext.getSupportedSSLParameters().getProtocols();
/*      */     } catch (NoSuchAlgorithmException e) {
/* 1251 */       throw new RuntimeException(sm.getString("jndiRealm.exception"), e);
/*      */     }
/*      */   }
/*      */   
/*      */   private Object constructInstance(String className) throws ReflectiveOperationException
/*      */   {
/* 1257 */     Class<?> clazz = Class.forName(className);
/* 1258 */     return clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
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
/*      */   public Principal authenticate(String username, String credentials)
/*      */   {
/* 1280 */     DirContext context = null;
/* 1281 */     Principal principal = null;
/*      */     
/*      */ 
/*      */     try
/*      */     {
/* 1286 */       context = open();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       try
/*      */       {
/* 1293 */         principal = authenticate(context, username, credentials);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/*      */       catch (NullPointerException|NamingException e)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1312 */         this.containerLog.info(sm.getString("jndiRealm.exception.retry"), e);
/*      */         
/*      */ 
/* 1315 */         if (context != null) {
/* 1316 */           close(context);
/*      */         }
/*      */         
/* 1319 */         context = open();
/*      */         
/*      */ 
/* 1322 */         principal = authenticate(context, username, credentials);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1327 */       release(context);
/*      */       
/*      */ 
/* 1330 */       return principal;
/*      */ 
/*      */     }
/*      */     catch (NamingException e)
/*      */     {
/* 1335 */       this.containerLog.error(sm.getString("jndiRealm.exception"), e);
/*      */       
/*      */ 
/* 1338 */       if (context != null) {
/* 1339 */         close(context);
/*      */       }
/*      */       
/* 1342 */       if (this.containerLog.isDebugEnabled())
/* 1343 */         this.containerLog.debug("Returning null principal."); }
/* 1344 */     return null;
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
/*      */   public synchronized Principal authenticate(DirContext context, String username, String credentials)
/*      */     throws NamingException
/*      */   {
/* 1374 */     if ((username == null) || (username.equals("")) || (credentials == null) || 
/* 1375 */       (credentials.equals(""))) {
/* 1376 */       if (this.containerLog.isDebugEnabled())
/* 1377 */         this.containerLog.debug("username null or empty: returning null principal.");
/* 1378 */       return null;
/*      */     }
/*      */     
/* 1381 */     if (this.userPatternArray != null) {
/* 1382 */       for (int curUserPattern = 0; 
/* 1383 */           curUserPattern < this.userPatternFormatArray.length; 
/* 1384 */           curUserPattern++)
/*      */       {
/* 1386 */         User user = getUser(context, username, credentials, curUserPattern);
/* 1387 */         if (user != null) {
/*      */           try
/*      */           {
/* 1390 */             if (checkCredentials(context, user, credentials))
/*      */             {
/* 1392 */               List<String> roles = getRoles(context, user);
/* 1393 */               if (this.containerLog.isDebugEnabled()) {
/* 1394 */                 this.containerLog.debug("Found roles: " + roles.toString());
/*      */               }
/* 1396 */               return new GenericPrincipal(username, credentials, roles);
/*      */             }
/*      */           }
/*      */           catch (InvalidNameException ine) {
/* 1400 */             this.containerLog.warn(sm.getString("jndiRealm.exception"), ine);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1408 */       return null;
/*      */     }
/*      */     
/* 1411 */     User user = getUser(context, username, credentials);
/* 1412 */     if (user == null) {
/* 1413 */       return null;
/*      */     }
/*      */     
/* 1416 */     if (!checkCredentials(context, user, credentials)) {
/* 1417 */       return null;
/*      */     }
/*      */     
/* 1420 */     List<String> roles = getRoles(context, user);
/* 1421 */     if (this.containerLog.isDebugEnabled()) {
/* 1422 */       this.containerLog.debug("Found roles: " + roles.toString());
/*      */     }
/*      */     
/*      */ 
/* 1426 */     return new GenericPrincipal(username, credentials, roles);
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
/*      */   protected User getUser(DirContext context, String username)
/*      */     throws NamingException
/*      */   {
/* 1446 */     return getUser(context, username, null, -1);
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
/*      */   protected User getUser(DirContext context, String username, String credentials)
/*      */     throws NamingException
/*      */   {
/* 1466 */     return getUser(context, username, credentials, -1);
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
/*      */   protected User getUser(DirContext context, String username, String credentials, int curUserPattern)
/*      */     throws NamingException
/*      */   {
/* 1492 */     User user = null;
/*      */     
/*      */ 
/* 1495 */     ArrayList<String> list = new ArrayList();
/* 1496 */     if (this.userPassword != null)
/* 1497 */       list.add(this.userPassword);
/* 1498 */     if (this.userRoleName != null)
/* 1499 */       list.add(this.userRoleName);
/* 1500 */     if (this.userRoleAttribute != null) {
/* 1501 */       list.add(this.userRoleAttribute);
/*      */     }
/* 1503 */     String[] attrIds = new String[list.size()];
/* 1504 */     list.toArray(attrIds);
/*      */     
/*      */ 
/* 1507 */     if ((this.userPatternFormatArray != null) && (curUserPattern >= 0)) {
/* 1508 */       user = getUserByPattern(context, username, credentials, attrIds, curUserPattern);
/* 1509 */       if (this.containerLog.isDebugEnabled()) {
/* 1510 */         this.containerLog.debug("Found user by pattern [" + user + "]");
/*      */       }
/*      */     } else {
/* 1513 */       boolean thisUserSearchAsUser = isUserSearchAsUser();
/*      */       try {
/* 1515 */         if (thisUserSearchAsUser) {
/* 1516 */           userCredentialsAdd(context, username, credentials);
/*      */         }
/* 1518 */         user = getUserBySearch(context, username, attrIds);
/*      */       } finally {
/* 1520 */         if (thisUserSearchAsUser) {
/* 1521 */           userCredentialsRemove(context);
/*      */         }
/*      */       }
/* 1524 */       if (this.containerLog.isDebugEnabled()) {
/* 1525 */         this.containerLog.debug("Found user by search [" + user + "]");
/*      */       }
/*      */     }
/*      */     
/* 1529 */     if ((this.userPassword == null) && (credentials != null) && (user != null))
/*      */     {
/*      */ 
/* 1532 */       return new User(user.getUserName(), user.getDN(), credentials, user
/* 1533 */         .getRoles(), user.getUserRoleId());
/*      */     }
/*      */     
/* 1536 */     return user;
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
/*      */   protected User getUserByPattern(DirContext context, String username, String[] attrIds, String dn)
/*      */     throws NamingException
/*      */   {
/* 1560 */     if ((attrIds == null) || (attrIds.length == 0)) {
/* 1561 */       return new User(username, dn, null, null, null);
/*      */     }
/*      */     
/*      */ 
/* 1565 */     Attributes attrs = null;
/*      */     try {
/* 1567 */       attrs = context.getAttributes(dn, attrIds);
/*      */     } catch (NameNotFoundException e) {
/* 1569 */       return null;
/*      */     }
/* 1571 */     if (attrs == null) {
/* 1572 */       return null;
/*      */     }
/*      */     
/* 1575 */     String password = null;
/* 1576 */     if (this.userPassword != null) {
/* 1577 */       password = getAttributeValue(this.userPassword, attrs);
/*      */     }
/* 1579 */     String userRoleAttrValue = null;
/* 1580 */     if (this.userRoleAttribute != null) {
/* 1581 */       userRoleAttrValue = getAttributeValue(this.userRoleAttribute, attrs);
/*      */     }
/*      */     
/*      */ 
/* 1585 */     ArrayList<String> roles = null;
/* 1586 */     if (this.userRoleName != null) {
/* 1587 */       roles = addAttributeValues(this.userRoleName, attrs, roles);
/*      */     }
/* 1589 */     return new User(username, dn, password, roles, userRoleAttrValue);
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
/*      */   protected User getUserByPattern(DirContext context, String username, String credentials, String[] attrIds, int curUserPattern)
/*      */     throws NamingException
/*      */   {
/* 1615 */     User user = null;
/*      */     
/* 1617 */     if ((username == null) || (this.userPatternFormatArray[curUserPattern] == null)) {
/* 1618 */       return null;
/*      */     }
/*      */     
/* 1621 */     String dn = this.userPatternFormatArray[curUserPattern].format(new String[] { username });
/*      */     try
/*      */     {
/* 1624 */       user = getUserByPattern(context, username, attrIds, dn);
/*      */     } catch (NameNotFoundException e) {
/* 1626 */       return null;
/*      */     }
/*      */     catch (NamingException e)
/*      */     {
/*      */       try {
/* 1631 */         userCredentialsAdd(context, dn, credentials);
/*      */         
/* 1633 */         user = getUserByPattern(context, username, attrIds, dn);
/*      */       } finally {
/* 1635 */         userCredentialsRemove(context);
/*      */       }
/*      */     }
/* 1638 */     return user;
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
/*      */   protected User getUserBySearch(DirContext context, String username, String[] attrIds)
/*      */     throws NamingException
/*      */   {
/* 1658 */     if ((username == null) || (this.userSearchFormat == null)) {
/* 1659 */       return null;
/*      */     }
/*      */     
/* 1662 */     String filter = this.userSearchFormat.format(new String[] { username });
/*      */     
/*      */ 
/* 1665 */     SearchControls constraints = new SearchControls();
/*      */     
/* 1667 */     if (this.userSubtree) {
/* 1668 */       constraints.setSearchScope(2);
/*      */     }
/*      */     else {
/* 1671 */       constraints.setSearchScope(1);
/*      */     }
/*      */     
/* 1674 */     constraints.setCountLimit(this.sizeLimit);
/* 1675 */     constraints.setTimeLimit(this.timeLimit);
/*      */     
/*      */ 
/* 1678 */     if (attrIds == null)
/* 1679 */       attrIds = new String[0];
/* 1680 */     constraints.setReturningAttributes(attrIds);
/*      */     
/*      */ 
/* 1683 */     NamingEnumeration<SearchResult> results = context.search(this.userBase, filter, constraints);
/*      */     try
/*      */     {
/*      */       User localUser2;
/*      */       try {
/* 1688 */         if ((results == null) || (!results.hasMore())) {
/* 1689 */           return null;
/*      */         }
/*      */       } catch (PartialResultException ex) {
/* 1692 */         if (!this.adCompat) {
/* 1693 */           throw ex;
/*      */         }
/* 1695 */         return null;
/*      */       }
/*      */       
/*      */ 
/* 1699 */       SearchResult result = (SearchResult)results.next();
/*      */       
/*      */       try
/*      */       {
/* 1703 */         if (results.hasMore()) {
/* 1704 */           if (this.containerLog.isInfoEnabled())
/* 1705 */             this.containerLog.info("username " + username + " has multiple entries");
/* 1706 */           return null;
/*      */         }
/*      */       } catch (PartialResultException ex) {
/* 1709 */         if (!this.adCompat) {
/* 1710 */           throw ex;
/*      */         }
/*      */       }
/* 1713 */       String dn = getDistinguishedName(context, this.userBase, result);
/*      */       
/* 1715 */       if (this.containerLog.isTraceEnabled()) {
/* 1716 */         this.containerLog.trace("  entry found for " + username + " with dn " + dn);
/*      */       }
/*      */       
/* 1719 */       Attributes attrs = result.getAttributes();
/* 1720 */       if (attrs == null) {
/* 1721 */         return null;
/*      */       }
/*      */       
/* 1724 */       String password = null;
/* 1725 */       if (this.userPassword != null) {
/* 1726 */         password = getAttributeValue(this.userPassword, attrs);
/*      */       }
/* 1728 */       String userRoleAttrValue = null;
/* 1729 */       if (this.userRoleAttribute != null) {
/* 1730 */         userRoleAttrValue = getAttributeValue(this.userRoleAttribute, attrs);
/*      */       }
/*      */       
/*      */ 
/* 1734 */       ArrayList<String> roles = null;
/* 1735 */       if (this.userRoleName != null) {
/* 1736 */         roles = addAttributeValues(this.userRoleName, attrs, roles);
/*      */       }
/* 1738 */       return new User(username, dn, password, roles, userRoleAttrValue);
/*      */     } finally {
/* 1740 */       if (results != null) {
/* 1741 */         results.close();
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
/*      */ 
/*      */ 
/*      */   protected boolean checkCredentials(DirContext context, User user, String credentials)
/*      */     throws NamingException
/*      */   {
/* 1767 */     boolean validated = false;
/*      */     
/* 1769 */     if (this.userPassword == null) {
/* 1770 */       validated = bindAsUser(context, user, credentials);
/*      */     } else {
/* 1772 */       validated = compareCredentials(context, user, credentials);
/*      */     }
/*      */     
/* 1775 */     if (this.containerLog.isTraceEnabled()) {
/* 1776 */       if (validated) {
/* 1777 */         this.containerLog.trace(sm.getString("jndiRealm.authenticateSuccess", new Object[] {user
/* 1778 */           .getUserName() }));
/*      */       } else {
/* 1780 */         this.containerLog.trace(sm.getString("jndiRealm.authenticateFailure", new Object[] {user
/* 1781 */           .getUserName() }));
/*      */       }
/*      */     }
/* 1784 */     return validated;
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
/*      */   protected boolean compareCredentials(DirContext context, User info, String credentials)
/*      */     throws NamingException
/*      */   {
/* 1804 */     if (this.containerLog.isTraceEnabled()) {
/* 1805 */       this.containerLog.trace("  validating credentials");
/*      */     }
/* 1807 */     if ((info == null) || (credentials == null)) {
/* 1808 */       return false;
/*      */     }
/* 1810 */     String password = info.getPassword();
/*      */     
/* 1812 */     return getCredentialHandler().matches(credentials, password);
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
/*      */   protected boolean bindAsUser(DirContext context, User user, String credentials)
/*      */     throws NamingException
/*      */   {
/* 1830 */     if ((credentials == null) || (user == null)) {
/* 1831 */       return false;
/*      */     }
/* 1833 */     String dn = user.getDN();
/* 1834 */     if (dn == null) {
/* 1835 */       return false;
/*      */     }
/*      */     
/* 1838 */     if (this.containerLog.isTraceEnabled()) {
/* 1839 */       this.containerLog.trace("  validating credentials by binding as the user");
/*      */     }
/*      */     
/* 1842 */     userCredentialsAdd(context, dn, credentials);
/*      */     
/*      */ 
/* 1845 */     boolean validated = false;
/*      */     try {
/* 1847 */       if (this.containerLog.isTraceEnabled()) {
/* 1848 */         this.containerLog.trace("  binding as " + dn);
/*      */       }
/* 1850 */       context.getAttributes("", null);
/* 1851 */       validated = true;
/*      */     }
/*      */     catch (AuthenticationException e) {
/* 1854 */       if (this.containerLog.isTraceEnabled()) {
/* 1855 */         this.containerLog.trace("  bind attempt failed");
/*      */       }
/*      */     }
/*      */     
/* 1859 */     userCredentialsRemove(context);
/*      */     
/* 1861 */     return validated;
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
/*      */   private void userCredentialsAdd(DirContext context, String dn, String credentials)
/*      */     throws NamingException
/*      */   {
/* 1876 */     context.addToEnvironment("java.naming.security.principal", dn);
/* 1877 */     context.addToEnvironment("java.naming.security.credentials", credentials);
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
/*      */   private void userCredentialsRemove(DirContext context)
/*      */     throws NamingException
/*      */   {
/* 1891 */     if (this.connectionName != null) {
/* 1892 */       context.addToEnvironment("java.naming.security.principal", this.connectionName);
/*      */     }
/*      */     else {
/* 1895 */       context.removeFromEnvironment("java.naming.security.principal");
/*      */     }
/*      */     
/* 1898 */     if (this.connectionPassword != null) {
/* 1899 */       context.addToEnvironment("java.naming.security.credentials", this.connectionPassword);
/*      */     }
/*      */     else
/*      */     {
/* 1903 */       context.removeFromEnvironment("java.naming.security.credentials");
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
/*      */   protected List<String> getRoles(DirContext context, User user)
/*      */     throws NamingException
/*      */   {
/* 1921 */     if (user == null) {
/* 1922 */       return null;
/*      */     }
/* 1924 */     String dn = user.getDN();
/* 1925 */     String username = user.getUserName();
/* 1926 */     String userRoleId = user.getUserRoleId();
/*      */     
/* 1928 */     if ((dn == null) || (username == null)) {
/* 1929 */       return null;
/*      */     }
/* 1931 */     if (this.containerLog.isTraceEnabled()) {
/* 1932 */       this.containerLog.trace("  getRoles(" + dn + ")");
/*      */     }
/*      */     
/* 1935 */     List<String> list = new ArrayList();
/* 1936 */     List<String> userRoles = user.getRoles();
/* 1937 */     if (userRoles != null) {
/* 1938 */       list.addAll(userRoles);
/*      */     }
/* 1940 */     if (this.commonRole != null) {
/* 1941 */       list.add(this.commonRole);
/*      */     }
/* 1943 */     if (this.containerLog.isTraceEnabled()) {
/* 1944 */       this.containerLog.trace("  Found " + list.size() + " user internal roles");
/* 1945 */       this.containerLog.trace("  Found user internal roles " + list.toString());
/*      */     }
/*      */     
/*      */ 
/* 1949 */     if ((this.roleFormat == null) || (this.roleName == null)) {
/* 1950 */       return list;
/*      */     }
/*      */     
/* 1953 */     String filter = this.roleFormat.format(new String[] { doRFC2254Encoding(dn), username, userRoleId });
/* 1954 */     SearchControls controls = new SearchControls();
/* 1955 */     if (this.roleSubtree) {
/* 1956 */       controls.setSearchScope(2);
/*      */     } else
/* 1958 */       controls.setSearchScope(1);
/* 1959 */     controls.setReturningAttributes(new String[] { this.roleName });
/*      */     
/* 1961 */     String base = null;
/* 1962 */     if (this.roleBaseFormat != null) {
/* 1963 */       NameParser np = context.getNameParser("");
/* 1964 */       Name name = np.parse(dn);
/* 1965 */       String[] nameParts = new String[name.size()];
/* 1966 */       for (int i = 0; i < name.size(); i++) {
/* 1967 */         nameParts[i] = name.get(i);
/*      */       }
/* 1969 */       base = this.roleBaseFormat.format(nameParts);
/*      */     } else {
/* 1971 */       base = "";
/*      */     }
/*      */     
/*      */ 
/* 1975 */     NamingEnumeration<SearchResult> results = searchAsUser(context, user, base, filter, controls, 
/* 1976 */       isRoleSearchAsUser());
/*      */     
/* 1978 */     if (results == null) {
/* 1979 */       return list;
/*      */     }
/* 1981 */     HashMap<String, String> groupMap = new HashMap();
/*      */     Attributes attrs;
/* 1983 */     try { while (results.hasMore()) {
/* 1984 */         SearchResult result = (SearchResult)results.next();
/* 1985 */         attrs = result.getAttributes();
/* 1986 */         if (attrs != null)
/*      */         {
/* 1988 */           String dname = getDistinguishedName(context, this.roleBase, result);
/* 1989 */           String name = getAttributeValue(this.roleName, attrs);
/* 1990 */           if ((name != null) && (dname != null))
/* 1991 */             groupMap.put(dname, name);
/*      */         }
/*      */       }
/*      */     } catch (PartialResultException ex) {
/* 1995 */       if (!this.adCompat)
/* 1996 */         throw ex;
/*      */     } finally {
/* 1998 */       results.close();
/*      */     }
/*      */     
/* 2001 */     if (this.containerLog.isTraceEnabled()) {
/* 2002 */       Set<Map.Entry<String, String>> entries = groupMap.entrySet();
/* 2003 */       this.containerLog.trace("  Found " + entries.size() + " direct roles");
/* 2004 */       for (attrs = entries.iterator(); attrs.hasNext();) { entry = (Map.Entry)attrs.next();
/* 2005 */         this.containerLog.trace("  Found direct role " + (String)entry.getKey() + " -> " + (String)entry.getValue());
/*      */       }
/*      */     }
/*      */     
/*      */     Map.Entry<String, String> entry;
/* 2010 */     if (getRoleNested())
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2016 */       Map<String, String> newGroups = new HashMap(groupMap);
/* 2017 */       while (!newGroups.isEmpty()) {
/* 2018 */         Map<String, String> newThisRound = new HashMap();
/*      */         
/* 2020 */         for (Map.Entry<String, String> group : newGroups.entrySet()) {
/* 2021 */           filter = this.roleFormat.format(new String[] { (String)group.getKey(), (String)group.getValue(), (String)group.getValue() });
/*      */           
/* 2023 */           if (this.containerLog.isTraceEnabled()) {
/* 2024 */             this.containerLog.trace("Perform a nested group search with base " + this.roleBase + " and filter " + filter);
/*      */           }
/*      */           
/* 2027 */           results = searchAsUser(context, user, this.roleBase, filter, controls, 
/* 2028 */             isRoleSearchAsUser());
/*      */           try
/*      */           {
/* 2031 */             while (results.hasMore()) {
/* 2032 */               SearchResult result = (SearchResult)results.next();
/* 2033 */               Attributes attrs = result.getAttributes();
/* 2034 */               if (attrs != null)
/*      */               {
/* 2036 */                 String dname = getDistinguishedName(context, this.roleBase, result);
/* 2037 */                 String name = getAttributeValue(this.roleName, attrs);
/* 2038 */                 if ((name != null) && (dname != null) && (!groupMap.keySet().contains(dname))) {
/* 2039 */                   groupMap.put(dname, name);
/* 2040 */                   newThisRound.put(dname, name);
/*      */                   
/* 2042 */                   if (this.containerLog.isTraceEnabled()) {
/* 2043 */                     this.containerLog.trace("  Found nested role " + dname + " -> " + name);
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */           } catch (PartialResultException ex) {
/* 2049 */             if (!this.adCompat)
/* 2050 */               throw ex;
/*      */           } finally {
/* 2052 */             results.close();
/*      */           }
/*      */         }
/*      */         
/* 2056 */         newGroups = newThisRound;
/*      */       }
/*      */     }
/*      */     
/* 2060 */     list.addAll(groupMap.values());
/* 2061 */     return list;
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
/*      */   private NamingEnumeration<SearchResult> searchAsUser(DirContext context, User user, String base, String filter, SearchControls controls, boolean searchAsUser)
/*      */     throws NamingException
/*      */   {
/*      */     try
/*      */     {
/* 2091 */       if (searchAsUser) {
/* 2092 */         userCredentialsAdd(context, user.getDN(), user.getPassword());
/*      */       }
/* 2094 */       results = context.search(base, filter, controls);
/*      */     } finally { NamingEnumeration<SearchResult> results;
/* 2096 */       if (searchAsUser)
/* 2097 */         userCredentialsRemove(context);
/*      */     }
/*      */     NamingEnumeration<SearchResult> results;
/* 2100 */     return results;
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
/*      */   private String getAttributeValue(String attrId, Attributes attrs)
/*      */     throws NamingException
/*      */   {
/* 2115 */     if (this.containerLog.isTraceEnabled()) {
/* 2116 */       this.containerLog.trace("  retrieving attribute " + attrId);
/*      */     }
/* 2118 */     if ((attrId == null) || (attrs == null)) {
/* 2119 */       return null;
/*      */     }
/* 2121 */     Attribute attr = attrs.get(attrId);
/* 2122 */     if (attr == null)
/* 2123 */       return null;
/* 2124 */     Object value = attr.get();
/* 2125 */     if (value == null)
/* 2126 */       return null;
/* 2127 */     String valueString = null;
/* 2128 */     if ((value instanceof byte[])) {
/* 2129 */       valueString = new String((byte[])value);
/*      */     } else {
/* 2131 */       valueString = value.toString();
/*      */     }
/* 2133 */     return valueString;
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
/*      */   private ArrayList<String> addAttributeValues(String attrId, Attributes attrs, ArrayList<String> values)
/*      */     throws NamingException
/*      */   {
/* 2151 */     if (this.containerLog.isTraceEnabled())
/* 2152 */       this.containerLog.trace("  retrieving values for attribute " + attrId);
/* 2153 */     if ((attrId == null) || (attrs == null))
/* 2154 */       return values;
/* 2155 */     if (values == null)
/* 2156 */       values = new ArrayList();
/* 2157 */     Attribute attr = attrs.get(attrId);
/* 2158 */     if (attr == null)
/* 2159 */       return values;
/* 2160 */     NamingEnumeration<?> e = attr.getAll();
/*      */     try {
/* 2162 */       while (e.hasMore()) {
/* 2163 */         String value = (String)e.next();
/* 2164 */         values.add(value);
/*      */       }
/*      */     } catch (PartialResultException ex) {
/* 2167 */       if (!this.adCompat)
/* 2168 */         throw ex;
/*      */     } finally {
/* 2170 */       e.close();
/*      */     }
/* 2172 */     return values;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void close(DirContext context)
/*      */   {
/* 2184 */     if (context == null) {
/* 2185 */       return;
/*      */     }
/*      */     
/* 2188 */     if (this.tls != null) {
/*      */       try {
/* 2190 */         this.tls.close();
/*      */       } catch (IOException e) {
/* 2192 */         this.containerLog.error(sm.getString("jndiRealm.tlsClose"), e);
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/* 2197 */       if (this.containerLog.isDebugEnabled())
/* 2198 */         this.containerLog.debug("Closing directory context");
/* 2199 */       context.close();
/*      */     } catch (NamingException e) {
/* 2201 */       this.containerLog.error(sm.getString("jndiRealm.close"), e);
/*      */     }
/* 2203 */     this.context = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   protected String getName()
/*      */   {
/* 2211 */     return "JNDIRealm";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getPassword(String username)
/*      */   {
/* 2222 */     String userPassword = getUserPassword();
/* 2223 */     if ((userPassword == null) || (userPassword.isEmpty())) {
/* 2224 */       return null;
/*      */     }
/*      */     try
/*      */     {
/* 2228 */       User user = getUser(open(), username, null);
/* 2229 */       if (user == null)
/*      */       {
/* 2231 */         return null;
/*      */       }
/*      */       
/* 2234 */       return user.getPassword();
/*      */     }
/*      */     catch (NamingException e) {}
/* 2237 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Principal getPrincipal(String username)
/*      */   {
/* 2249 */     return getPrincipal(username, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected Principal getPrincipal(String username, GSSCredential gssCredential)
/*      */   {
/* 2256 */     DirContext context = null;
/* 2257 */     Principal principal = null;
/*      */     
/*      */ 
/*      */     try
/*      */     {
/* 2262 */       context = open();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       try
/*      */       {
/* 2269 */         principal = getPrincipal(context, username, gssCredential);
/*      */ 
/*      */       }
/*      */       catch (CommunicationException|ServiceUnavailableException e)
/*      */       {
/* 2274 */         this.containerLog.info(sm.getString("jndiRealm.exception.retry"), e);
/*      */         
/*      */ 
/* 2277 */         if (context != null) {
/* 2278 */           close(context);
/*      */         }
/*      */         
/* 2281 */         context = open();
/*      */         
/*      */ 
/* 2284 */         principal = getPrincipal(context, username, gssCredential);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2290 */       release(context);
/*      */       
/*      */ 
/* 2293 */       return principal;
/*      */ 
/*      */     }
/*      */     catch (NamingException e)
/*      */     {
/* 2298 */       this.containerLog.error(sm.getString("jndiRealm.exception"), e);
/*      */       
/*      */ 
/* 2301 */       if (context != null) {
/* 2302 */         close(context);
/*      */       }
/*      */     }
/* 2305 */     return null;
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
/*      */   protected synchronized Principal getPrincipal(DirContext context, String username, GSSCredential gssCredential)
/*      */     throws NamingException
/*      */   {
/* 2325 */     User user = null;
/* 2326 */     List<String> roles = null;
/* 2327 */     Hashtable<?, ?> preservedEnvironment = null;
/*      */     try
/*      */     {
/* 2330 */       if ((gssCredential != null) && (isUseDelegatedCredential()))
/*      */       {
/* 2332 */         preservedEnvironment = context.getEnvironment();
/*      */         
/* 2334 */         context.addToEnvironment("java.naming.security.authentication", "GSSAPI");
/*      */         
/* 2336 */         context.addToEnvironment("javax.security.sasl.server.authentication", "true");
/*      */         
/* 2338 */         context.addToEnvironment("javax.security.sasl.qop", this.spnegoDelegationQop);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 2343 */       user = getUser(context, username);
/* 2344 */       if (user != null) {
/* 2345 */         roles = getRoles(context, user);
/*      */       }
/*      */     } finally {
/* 2348 */       restoreEnvironmentParameter(context, "java.naming.security.authentication", preservedEnvironment);
/*      */       
/* 2350 */       restoreEnvironmentParameter(context, "javax.security.sasl.server.authentication", preservedEnvironment);
/*      */       
/* 2352 */       restoreEnvironmentParameter(context, "javax.security.sasl.qop", preservedEnvironment);
/*      */     }
/*      */     
/*      */ 
/* 2356 */     if (user != null) {
/* 2357 */       return new GenericPrincipal(user.getUserName(), user.getPassword(), roles, null, null, gssCredential);
/*      */     }
/*      */     
/*      */ 
/* 2361 */     return null;
/*      */   }
/*      */   
/*      */   private void restoreEnvironmentParameter(DirContext context, String parameterName, Hashtable<?, ?> preservedEnvironment)
/*      */   {
/*      */     try {
/* 2367 */       context.removeFromEnvironment(parameterName);
/* 2368 */       if ((preservedEnvironment != null) && (preservedEnvironment.containsKey(parameterName))) {
/* 2369 */         context.addToEnvironment(parameterName, preservedEnvironment
/* 2370 */           .get(parameterName));
/*      */       }
/*      */     }
/*      */     catch (NamingException localNamingException) {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected DirContext open()
/*      */     throws NamingException
/*      */   {
/* 2386 */     if (this.context != null) {
/* 2387 */       return this.context;
/*      */     }
/*      */     
/*      */     try
/*      */     {
/* 2392 */       this.context = createDirContext(getDirectoryContextEnvironment());
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/* 2396 */       this.connectionAttempt = 1;
/*      */       
/*      */ 
/* 2399 */       this.containerLog.info(sm.getString("jndiRealm.exception.retry"), e);
/*      */       
/*      */ 
/* 2402 */       this.context = createDirContext(getDirectoryContextEnvironment());
/*      */ 
/*      */     }
/*      */     finally
/*      */     {
/*      */ 
/* 2408 */       this.connectionAttempt = 0;
/*      */     }
/*      */     
/*      */ 
/* 2412 */     return this.context;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean isAvailable()
/*      */   {
/* 2419 */     return this.context != null;
/*      */   }
/*      */   
/*      */   private DirContext createDirContext(Hashtable<String, String> env) throws NamingException {
/* 2423 */     if (this.useStartTls) {
/* 2424 */       return createTlsDirContext(env);
/*      */     }
/* 2426 */     return new InitialDirContext(env);
/*      */   }
/*      */   
/*      */   private SSLSocketFactory getSSLSocketFactory()
/*      */   {
/* 2431 */     if (this.sslSocketFactory != null)
/* 2432 */       return this.sslSocketFactory;
/*      */     SSLSocketFactory result;
/*      */     SSLSocketFactory result;
/* 2435 */     if ((this.sslSocketFactoryClassName != null) && 
/* 2436 */       (!this.sslSocketFactoryClassName.trim().equals(""))) {
/* 2437 */       result = createSSLSocketFactoryFromClassName(this.sslSocketFactoryClassName);
/*      */     } else {
/* 2439 */       result = createSSLContextFactoryFromProtocol(this.sslProtocol);
/*      */     }
/* 2441 */     this.sslSocketFactory = result;
/* 2442 */     return result;
/*      */   }
/*      */   
/*      */   private SSLSocketFactory createSSLSocketFactoryFromClassName(String className) {
/*      */     try {
/* 2447 */       Object o = constructInstance(className);
/* 2448 */       if ((o instanceof SSLSocketFactory)) {
/* 2449 */         return this.sslSocketFactory;
/*      */       }
/* 2451 */       throw new IllegalArgumentException(sm.getString("jndiRealm.invalidSslSocketFactory", new Object[] { className }));
/*      */ 
/*      */     }
/*      */     catch (ReflectiveOperationException|SecurityException e)
/*      */     {
/* 2456 */       throw new IllegalArgumentException(sm.getString("jndiRealm.invalidSslSocketFactory", new Object[] { className }), e);
/*      */     }
/*      */   }
/*      */   
/*      */   private SSLSocketFactory createSSLContextFactoryFromProtocol(String protocol)
/*      */   {
/*      */     try
/*      */     {
/*      */       SSLContext sslContext;
/* 2465 */       if (protocol != null) {
/* 2466 */         SSLContext sslContext = SSLContext.getInstance(protocol);
/* 2467 */         sslContext.init(null, null, null);
/*      */       } else {
/* 2469 */         sslContext = SSLContext.getDefault();
/*      */       }
/* 2471 */       return sslContext.getSocketFactory();
/*      */     }
/*      */     catch (NoSuchAlgorithmException|KeyManagementException e) {
/* 2474 */       List<String> allowedProtocols = Arrays.asList(getSupportedSslProtocols());
/*      */       
/* 2476 */       throw new IllegalArgumentException(sm.getString("jndiRealm.invalidSslProtocol", new Object[] { protocol, allowedProtocols }), e);
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
/*      */   private DirContext createTlsDirContext(Hashtable<String, String> env)
/*      */     throws NamingException
/*      */   {
/* 2493 */     Map<String, Object> savedEnv = new HashMap();
/* 2494 */     for (String key : Arrays.asList(new String[] { "java.naming.security.authentication", "java.naming.security.credentials", "java.naming.security.principal", "java.naming.security.protocol" }))
/*      */     {
/*      */ 
/* 2497 */       Object entry = env.remove(key);
/* 2498 */       if (entry != null) {
/* 2499 */         savedEnv.put(key, entry);
/*      */       }
/*      */     }
/* 2502 */     Object result = null;
/*      */     try {
/* 2504 */       result = new InitialLdapContext(env, null);
/*      */       
/* 2506 */       this.tls = ((StartTlsResponse)((LdapContext)result).extendedOperation(new StartTlsRequest()));
/* 2507 */       if (getHostnameVerifier() != null) {
/* 2508 */         this.tls.setHostnameVerifier(getHostnameVerifier());
/*      */       }
/* 2510 */       if (getCipherSuitesArray() != null) {
/* 2511 */         this.tls.setEnabledCipherSuites(getCipherSuitesArray());
/*      */       }
/*      */       try {
/* 2514 */         SSLSession negotiate = this.tls.negotiate(getSSLSocketFactory());
/* 2515 */         this.containerLog.debug(sm.getString("jndiRealm.negotiatedTls", new Object[] {negotiate
/* 2516 */           .getProtocol() }));
/*      */       } catch (IOException e) {
/* 2518 */         throw new NamingException(e.getMessage());
/*      */       }
/*      */     } finally { Map.Entry<String, Object> savedEntry;
/* 2521 */       if (result != null) {
/* 2522 */         for (Map.Entry<String, Object> savedEntry : savedEnv.entrySet()) {
/* 2523 */           ((LdapContext)result).addToEnvironment((String)savedEntry.getKey(), savedEntry
/* 2524 */             .getValue());
/*      */         }
/*      */       }
/*      */     }
/* 2528 */     return (DirContext)result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Hashtable<String, String> getDirectoryContextEnvironment()
/*      */   {
/* 2538 */     Hashtable<String, String> env = new Hashtable();
/*      */     
/*      */ 
/* 2541 */     if ((this.containerLog.isDebugEnabled()) && (this.connectionAttempt == 0)) {
/* 2542 */       this.containerLog.debug("Connecting to URL " + this.connectionURL);
/* 2543 */     } else if ((this.containerLog.isDebugEnabled()) && (this.connectionAttempt > 0))
/* 2544 */       this.containerLog.debug("Connecting to URL " + this.alternateURL);
/* 2545 */     env.put("java.naming.factory.initial", this.contextFactory);
/* 2546 */     if (this.connectionName != null)
/* 2547 */       env.put("java.naming.security.principal", this.connectionName);
/* 2548 */     if (this.connectionPassword != null)
/* 2549 */       env.put("java.naming.security.credentials", this.connectionPassword);
/* 2550 */     if ((this.connectionURL != null) && (this.connectionAttempt == 0)) {
/* 2551 */       env.put("java.naming.provider.url", this.connectionURL);
/* 2552 */     } else if ((this.alternateURL != null) && (this.connectionAttempt > 0))
/* 2553 */       env.put("java.naming.provider.url", this.alternateURL);
/* 2554 */     if (this.authentication != null)
/* 2555 */       env.put("java.naming.security.authentication", this.authentication);
/* 2556 */     if (this.protocol != null)
/* 2557 */       env.put("java.naming.security.protocol", this.protocol);
/* 2558 */     if (this.referrals != null)
/* 2559 */       env.put("java.naming.referral", this.referrals);
/* 2560 */     if (this.derefAliases != null)
/* 2561 */       env.put("java.naming.ldap.derefAliases", this.derefAliases);
/* 2562 */     if (this.connectionTimeout != null)
/* 2563 */       env.put("com.sun.jndi.ldap.connect.timeout", this.connectionTimeout);
/* 2564 */     if (this.readTimeout != null) {
/* 2565 */       env.put("com.sun.jndi.ldap.read.timeout", this.readTimeout);
/*      */     }
/* 2567 */     return env;
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
/*      */   protected void release(DirContext context) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
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
/*      */     try
/*      */     {
/* 2600 */       open();
/*      */ 
/*      */     }
/*      */     catch (NamingException e)
/*      */     {
/*      */ 
/* 2606 */       this.containerLog.error(sm.getString("jndiRealm.open"), e);
/*      */     }
/*      */     
/* 2609 */     super.startInternal();
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
/*      */   protected void stopInternal()
/*      */     throws LifecycleException
/*      */   {
/* 2624 */     super.stopInternal();
/*      */     
/*      */ 
/* 2627 */     close(this.context);
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
/*      */   protected String[] parseUserPatternString(String userPatternString)
/*      */   {
/* 2643 */     if (userPatternString != null) {
/* 2644 */       ArrayList<String> pathList = new ArrayList();
/* 2645 */       int startParenLoc = userPatternString.indexOf('(');
/* 2646 */       if (startParenLoc == -1)
/*      */       {
/* 2648 */         return new String[] { userPatternString };
/*      */       }
/* 2650 */       int startingPoint = 0;
/* 2651 */       while (startParenLoc > -1) {
/* 2652 */         int endParenLoc = 0;
/*      */         
/*      */ 
/*      */ 
/* 2656 */         while ((userPatternString.charAt(startParenLoc + 1) == '|') || ((startParenLoc != 0) && 
/* 2657 */           (userPatternString.charAt(startParenLoc - 1) == '\\'))) {
/* 2658 */           startParenLoc = userPatternString.indexOf('(', startParenLoc + 1);
/*      */         }
/* 2660 */         endParenLoc = userPatternString.indexOf(')', startParenLoc + 1);
/*      */         
/* 2662 */         while (userPatternString.charAt(endParenLoc - 1) == '\\') {
/* 2663 */           endParenLoc = userPatternString.indexOf(')', endParenLoc + 1);
/*      */         }
/*      */         
/* 2666 */         String nextPathPart = userPatternString.substring(startParenLoc + 1, endParenLoc);
/* 2667 */         pathList.add(nextPathPart);
/* 2668 */         startingPoint = endParenLoc + 1;
/* 2669 */         startParenLoc = userPatternString.indexOf('(', startingPoint);
/*      */       }
/* 2671 */       return (String[])pathList.toArray(new String[0]);
/*      */     }
/* 2673 */     return null;
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
/*      */   protected String doRFC2254Encoding(String inString)
/*      */   {
/* 2693 */     StringBuilder buf = new StringBuilder(inString.length());
/* 2694 */     for (int i = 0; i < inString.length(); i++) {
/* 2695 */       char c = inString.charAt(i);
/* 2696 */       switch (c) {
/*      */       case '\\': 
/* 2698 */         buf.append("\\5c");
/* 2699 */         break;
/*      */       case '*': 
/* 2701 */         buf.append("\\2a");
/* 2702 */         break;
/*      */       case '(': 
/* 2704 */         buf.append("\\28");
/* 2705 */         break;
/*      */       case ')': 
/* 2707 */         buf.append("\\29");
/* 2708 */         break;
/*      */       case '\000': 
/* 2710 */         buf.append("\\00");
/* 2711 */         break;
/*      */       default: 
/* 2713 */         buf.append(c);
/*      */       }
/*      */       
/*      */     }
/* 2717 */     return buf.toString();
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
/*      */   protected String getDistinguishedName(DirContext context, String base, SearchResult result)
/*      */     throws NamingException
/*      */   {
/* 2735 */     String resultName = result.getName();
/* 2736 */     if (result.isRelative()) {
/* 2737 */       if (this.containerLog.isTraceEnabled()) {
/* 2738 */         this.containerLog.trace("  search returned relative name: " + resultName);
/*      */       }
/* 2740 */       NameParser parser = context.getNameParser("");
/* 2741 */       Name contextName = parser.parse(context.getNameInNamespace());
/* 2742 */       Name baseName = parser.parse(base);
/*      */       
/*      */ 
/* 2745 */       Name entryName = parser.parse(new CompositeName(resultName).get(0));
/*      */       
/* 2747 */       Name name = contextName.addAll(baseName);
/* 2748 */       name = name.addAll(entryName);
/* 2749 */       return name.toString();
/*      */     }
/* 2751 */     if (this.containerLog.isTraceEnabled()) {
/* 2752 */       this.containerLog.trace("  search returned absolute name: " + resultName);
/*      */     }
/*      */     try
/*      */     {
/* 2756 */       NameParser parser = context.getNameParser("");
/* 2757 */       URI userNameUri = new URI(resultName);
/* 2758 */       String pathComponent = userNameUri.getPath();
/*      */       
/* 2760 */       if (pathComponent.length() < 1) {
/* 2761 */         throw new InvalidNameException("Search returned unparseable absolute name: " + resultName);
/*      */       }
/*      */       
/*      */ 
/* 2765 */       Name name = parser.parse(pathComponent.substring(1));
/* 2766 */       return name.toString();
/*      */     } catch (URISyntaxException e) {
/* 2768 */       throw new InvalidNameException("Search returned unparseable absolute name: " + resultName);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected static class User
/*      */   {
/*      */     private final String username;
/*      */     
/*      */ 
/*      */     private final String dn;
/*      */     
/*      */ 
/*      */     private final String password;
/*      */     
/*      */ 
/*      */     private final List<String> roles;
/*      */     
/*      */     private final String userRoleId;
/*      */     
/*      */ 
/*      */     public User(String username, String dn, String password, List<String> roles, String userRoleId)
/*      */     {
/* 2792 */       this.username = username;
/* 2793 */       this.dn = dn;
/* 2794 */       this.password = password;
/* 2795 */       if (roles == null) {
/* 2796 */         this.roles = Collections.emptyList();
/*      */       } else {
/* 2798 */         this.roles = Collections.unmodifiableList(roles);
/*      */       }
/* 2800 */       this.userRoleId = userRoleId;
/*      */     }
/*      */     
/*      */     public String getUserName() {
/* 2804 */       return this.username;
/*      */     }
/*      */     
/*      */     public String getDN() {
/* 2808 */       return this.dn;
/*      */     }
/*      */     
/*      */     public String getPassword() {
/* 2812 */       return this.password;
/*      */     }
/*      */     
/*      */     public List<String> getRoles() {
/* 2816 */       return this.roles;
/*      */     }
/*      */     
/*      */     public String getUserRoleId() {
/* 2820 */       return this.userRoleId;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\realm\JNDIRealm.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */