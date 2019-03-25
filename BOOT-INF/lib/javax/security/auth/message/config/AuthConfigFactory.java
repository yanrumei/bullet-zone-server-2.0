/*     */ package javax.security.auth.message.config;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.security.AccessController;
/*     */ import java.security.Permission;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.security.Security;
/*     */ import java.security.SecurityPermission;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AuthConfigFactory
/*     */ {
/*     */   public static final String DEFAULT_FACTORY_SECURITY_PROPERTY = "authconfigprovider.factory";
/*     */   public static final String GET_FACTORY_PERMISSION_NAME = "getProperty.authconfigprovider.factory";
/*     */   public static final String SET_FACTORY_PERMISSION_NAME = "setProperty.authconfigprovider.factory";
/*     */   public static final String PROVIDER_REGISTRATION_PERMISSION_NAME = "setProperty.authconfigfactory.provider";
/*  40 */   public static final SecurityPermission getFactorySecurityPermission = new SecurityPermission("getProperty.authconfigprovider.factory");
/*     */   
/*     */ 
/*  43 */   public static final SecurityPermission setFactorySecurityPermission = new SecurityPermission("setProperty.authconfigprovider.factory");
/*     */   
/*     */ 
/*  46 */   public static final SecurityPermission providerRegistrationSecurityPermission = new SecurityPermission("setProperty.authconfigfactory.provider");
/*     */   
/*     */ 
/*     */   private static final String DEFAULT_JASPI_AUTHCONFIGFACTORYIMPL = "org.apache.catalina.authenticator.jaspic.AuthConfigFactoryImpl";
/*     */   
/*     */ 
/*     */   private static volatile AuthConfigFactory factory;
/*     */   
/*     */ 
/*     */ 
/*     */   public static AuthConfigFactory getFactory()
/*     */   {
/*  58 */     checkPermission(getFactorySecurityPermission);
/*  59 */     if (factory != null) {
/*  60 */       return factory;
/*     */     }
/*     */     
/*  63 */     synchronized (AuthConfigFactory.class) {
/*  64 */       if (factory == null) {
/*  65 */         String className = getFactoryClassName();
/*     */         try {
/*  67 */           factory = (AuthConfigFactory)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */             public AuthConfigFactory run()
/*     */               throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
/*     */             {
/*     */ 
/*     */ 
/*  77 */               Class<?> clazz = Class.forName(this.val$className);
/*  78 */               return (AuthConfigFactory)clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/*     */             }
/*     */           });
/*     */         } catch (PrivilegedActionException e) {
/*  82 */           Exception inner = e.getException();
/*  83 */           if ((inner instanceof InstantiationException))
/*     */           {
/*  85 */             throw ((SecurityException)new SecurityException("AuthConfigFactory error:" + inner.getCause().getMessage()).initCause(inner.getCause()));
/*     */           }
/*     */           
/*  88 */           throw ((SecurityException)new SecurityException("AuthConfigFactory error: " + inner).initCause(inner));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  94 */     return factory;
/*     */   }
/*     */   
/*     */   public static synchronized void setFactory(AuthConfigFactory factory) {
/*  98 */     checkPermission(setFactorySecurityPermission);
/*  99 */     factory = factory;
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract AuthConfigProvider getConfigProvider(String paramString1, String paramString2, RegistrationListener paramRegistrationListener);
/*     */   
/*     */ 
/*     */   public abstract String registerConfigProvider(String paramString1, Map paramMap, String paramString2, String paramString3, String paramString4);
/*     */   
/*     */ 
/*     */   public abstract String registerConfigProvider(AuthConfigProvider paramAuthConfigProvider, String paramString1, String paramString2, String paramString3);
/*     */   
/*     */ 
/*     */   public abstract boolean removeRegistration(String paramString);
/*     */   
/*     */   public abstract String[] detachListener(RegistrationListener paramRegistrationListener, String paramString1, String paramString2);
/*     */   
/*     */   public abstract String[] getRegistrationIDs(AuthConfigProvider paramAuthConfigProvider);
/*     */   
/*     */   public abstract RegistrationContext getRegistrationContext(String paramString);
/*     */   
/*     */   public abstract void refresh();
/*     */   
/*     */   private static void checkPermission(Permission permission)
/*     */   {
/* 124 */     SecurityManager securityManager = System.getSecurityManager();
/* 125 */     if (securityManager != null) {
/* 126 */       securityManager.checkPermission(permission);
/*     */     }
/*     */   }
/*     */   
/*     */   private static String getFactoryClassName() {
/* 131 */     String className = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public String run() {
/* 134 */         return Security.getProperty("authconfigprovider.factory");
/*     */       }
/*     */     });
/*     */     
/* 138 */     if (className != null) {
/* 139 */       return className;
/*     */     }
/*     */     
/* 142 */     return "org.apache.catalina.authenticator.jaspic.AuthConfigFactoryImpl";
/*     */   }
/*     */   
/*     */   public static abstract interface RegistrationContext
/*     */   {
/*     */     public abstract String getMessageLayer();
/*     */     
/*     */     public abstract String getAppContext();
/*     */     
/*     */     public abstract String getDescription();
/*     */     
/*     */     public abstract boolean isPersistent();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\security\auth\message\config\AuthConfigFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */