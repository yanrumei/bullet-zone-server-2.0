/*     */ package ch.qos.logback.core.net;
/*     */ 
/*     */ import ch.qos.logback.core.AppenderBase;
/*     */ import java.util.Properties;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.NameNotFoundException;
/*     */ import javax.naming.NamingException;
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
/*     */ public abstract class JMSAppenderBase<E>
/*     */   extends AppenderBase<E>
/*     */ {
/*     */   protected String securityPrincipalName;
/*     */   protected String securityCredentials;
/*     */   protected String initialContextFactoryName;
/*     */   protected String urlPkgPrefixes;
/*     */   protected String providerURL;
/*     */   protected String userName;
/*     */   protected String password;
/*     */   
/*     */   protected Object lookup(Context ctx, String name)
/*     */     throws NamingException
/*     */   {
/*     */     try
/*     */     {
/*  48 */       return ctx.lookup(name);
/*     */     } catch (NameNotFoundException e) {
/*  50 */       addError("Could not find name [" + name + "].");
/*  51 */       throw e;
/*     */     }
/*     */   }
/*     */   
/*     */   public Context buildJNDIContext() throws NamingException {
/*  56 */     Context jndi = null;
/*     */     
/*     */ 
/*  59 */     if (this.initialContextFactoryName != null) {
/*  60 */       Properties env = buildEnvProperties();
/*  61 */       jndi = new InitialContext(env);
/*     */     } else {
/*  63 */       jndi = new InitialContext();
/*     */     }
/*  65 */     return jndi;
/*     */   }
/*     */   
/*     */   public Properties buildEnvProperties() {
/*  69 */     Properties env = new Properties();
/*  70 */     env.put("java.naming.factory.initial", this.initialContextFactoryName);
/*  71 */     if (this.providerURL != null) {
/*  72 */       env.put("java.naming.provider.url", this.providerURL);
/*     */     } else {
/*  74 */       addWarn("You have set InitialContextFactoryName option but not the ProviderURL. This is likely to cause problems.");
/*     */     }
/*  76 */     if (this.urlPkgPrefixes != null) {
/*  77 */       env.put("java.naming.factory.url.pkgs", this.urlPkgPrefixes);
/*     */     }
/*     */     
/*  80 */     if (this.securityPrincipalName != null) {
/*  81 */       env.put("java.naming.security.principal", this.securityPrincipalName);
/*  82 */       if (this.securityCredentials != null) {
/*  83 */         env.put("java.naming.security.credentials", this.securityCredentials);
/*     */       } else {
/*  85 */         addWarn("You have set SecurityPrincipalName option but not the SecurityCredentials. This is likely to cause problems.");
/*     */       }
/*     */     }
/*  88 */     return env;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getInitialContextFactoryName()
/*     */   {
/*  97 */     return this.initialContextFactoryName;
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
/*     */   public void setInitialContextFactoryName(String initialContextFactoryName)
/*     */   {
/* 111 */     this.initialContextFactoryName = initialContextFactoryName;
/*     */   }
/*     */   
/*     */   public String getProviderURL() {
/* 115 */     return this.providerURL;
/*     */   }
/*     */   
/*     */   public void setProviderURL(String providerURL) {
/* 119 */     this.providerURL = providerURL;
/*     */   }
/*     */   
/*     */   public String getURLPkgPrefixes() {
/* 123 */     return this.urlPkgPrefixes;
/*     */   }
/*     */   
/*     */   public void setURLPkgPrefixes(String urlPkgPrefixes) {
/* 127 */     this.urlPkgPrefixes = urlPkgPrefixes;
/*     */   }
/*     */   
/*     */   public String getSecurityCredentials() {
/* 131 */     return this.securityCredentials;
/*     */   }
/*     */   
/*     */   public void setSecurityCredentials(String securityCredentials) {
/* 135 */     this.securityCredentials = securityCredentials;
/*     */   }
/*     */   
/*     */   public String getSecurityPrincipalName() {
/* 139 */     return this.securityPrincipalName;
/*     */   }
/*     */   
/*     */   public void setSecurityPrincipalName(String securityPrincipalName) {
/* 143 */     this.securityPrincipalName = securityPrincipalName;
/*     */   }
/*     */   
/*     */   public String getUserName() {
/* 147 */     return this.userName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUserName(String userName)
/*     */   {
/* 157 */     this.userName = userName;
/*     */   }
/*     */   
/*     */   public String getPassword() {
/* 161 */     return this.password;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setPassword(String password)
/*     */   {
/* 168 */     this.password = password;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\net\JMSAppenderBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */