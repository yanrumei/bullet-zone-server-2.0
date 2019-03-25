/*     */ package org.apache.catalina.security;
/*     */ 
/*     */ import java.security.Security;
/*     */ import org.apache.catalina.startup.CatalinaProperties;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SecurityConfig
/*     */ {
/*  31 */   private static SecurityConfig singleton = null;
/*     */   
/*  33 */   private static final Log log = LogFactory.getLog(SecurityConfig.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String PACKAGE_ACCESS = "sun.,org.apache.catalina.,org.apache.jasper.,org.apache.coyote.,org.apache.tomcat.";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String PACKAGE_DEFINITION = "java.,sun.,org.apache.catalina.,org.apache.coyote.,org.apache.tomcat.,org.apache.jasper.";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final String packageDefinition;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final String packageAccess;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private SecurityConfig()
/*     */   {
/*  65 */     String definition = null;
/*  66 */     String access = null;
/*     */     try {
/*  68 */       definition = CatalinaProperties.getProperty("package.definition");
/*  69 */       access = CatalinaProperties.getProperty("package.access");
/*     */     } catch (Exception ex) {
/*  71 */       if (log.isDebugEnabled()) {
/*  72 */         log.debug("Unable to load properties using CatalinaProperties", ex);
/*     */       }
/*     */     } finally {
/*  75 */       this.packageDefinition = definition;
/*  76 */       this.packageAccess = access;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SecurityConfig newInstance()
/*     */   {
/*  86 */     if (singleton == null) {
/*  87 */       singleton = new SecurityConfig();
/*     */     }
/*  89 */     return singleton;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPackageAccess()
/*     */   {
/*  98 */     if (this.packageAccess == null) {
/*  99 */       setSecurityProperty("package.access", "sun.,org.apache.catalina.,org.apache.jasper.,org.apache.coyote.,org.apache.tomcat.");
/*     */     } else {
/* 101 */       setSecurityProperty("package.access", this.packageAccess);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPackageDefinition()
/*     */   {
/* 111 */     if (this.packageDefinition == null) {
/* 112 */       setSecurityProperty("package.definition", "java.,sun.,org.apache.catalina.,org.apache.coyote.,org.apache.tomcat.,org.apache.jasper.");
/*     */     } else {
/* 114 */       setSecurityProperty("package.definition", this.packageDefinition);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final void setSecurityProperty(String properties, String packageList)
/*     */   {
/* 124 */     if (System.getSecurityManager() != null) {
/* 125 */       String definition = Security.getProperty(properties);
/* 126 */       if ((definition != null) && (definition.length() > 0)) {
/* 127 */         if (packageList.length() > 0) {
/* 128 */           definition = definition + ',' + packageList;
/*     */         }
/*     */       } else {
/* 131 */         definition = packageList;
/*     */       }
/*     */       
/* 134 */       Security.setProperty(properties, definition);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\security\SecurityConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */