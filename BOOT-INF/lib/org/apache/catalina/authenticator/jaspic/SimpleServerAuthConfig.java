/*     */ package org.apache.catalina.authenticator.jaspic;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.security.auth.Subject;
/*     */ import javax.security.auth.callback.CallbackHandler;
/*     */ import javax.security.auth.message.AuthException;
/*     */ import javax.security.auth.message.MessageInfo;
/*     */ import javax.security.auth.message.config.ServerAuthConfig;
/*     */ import javax.security.auth.message.config.ServerAuthContext;
/*     */ import javax.security.auth.message.module.ServerAuthModule;
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
/*     */ public class SimpleServerAuthConfig
/*     */   implements ServerAuthConfig
/*     */ {
/*  43 */   private static StringManager sm = StringManager.getManager(SimpleServerAuthConfig.class);
/*     */   
/*     */   private static final String SERVER_AUTH_MODULE_KEY_PREFIX = "org.apache.catalina.authenticator.jaspic.ServerAuthModule.";
/*     */   
/*     */   private final String layer;
/*     */   
/*     */   private final String appContext;
/*     */   
/*     */   private final CallbackHandler handler;
/*     */   private final Map<String, String> properties;
/*     */   private volatile ServerAuthContext serverAuthContext;
/*     */   
/*     */   public SimpleServerAuthConfig(String layer, String appContext, CallbackHandler handler, Map<String, String> properties)
/*     */   {
/*  57 */     this.layer = layer;
/*  58 */     this.appContext = appContext;
/*  59 */     this.handler = handler;
/*  60 */     this.properties = properties;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getMessageLayer()
/*     */   {
/*  66 */     return this.layer;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getAppContext()
/*     */   {
/*  72 */     return this.appContext;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getAuthContextID(MessageInfo messageInfo)
/*     */   {
/*  78 */     return messageInfo.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public void refresh()
/*     */   {
/*  84 */     this.serverAuthContext = null;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isProtected()
/*     */   {
/*  90 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ServerAuthContext getAuthContext(String authContextID, Subject serviceSubject, Map properties)
/*     */     throws AuthException
/*     */   {
/*  98 */     ServerAuthContext serverAuthContext = this.serverAuthContext;
/*  99 */     if (serverAuthContext == null) {
/* 100 */       synchronized (this) {
/* 101 */         if (this.serverAuthContext == null) {
/* 102 */           Map<String, String> mergedProperties = new HashMap();
/* 103 */           if (this.properties != null) {
/* 104 */             mergedProperties.putAll(this.properties);
/*     */           }
/* 106 */           if (properties != null) {
/* 107 */             mergedProperties.putAll(properties);
/*     */           }
/*     */           
/* 110 */           List<ServerAuthModule> modules = new ArrayList();
/* 111 */           int moduleIndex = 1;
/* 112 */           String key = "org.apache.catalina.authenticator.jaspic.ServerAuthModule." + moduleIndex;
/* 113 */           String moduleClassName = (String)mergedProperties.get(key);
/* 114 */           while (moduleClassName != null) {
/*     */             try {
/* 116 */               Class<?> clazz = Class.forName(moduleClassName);
/*     */               
/* 118 */               ServerAuthModule module = (ServerAuthModule)clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/* 119 */               module.initialize(null, null, this.handler, mergedProperties);
/* 120 */               modules.add(module);
/*     */ 
/*     */             }
/*     */             catch (ClassNotFoundException|InstantiationException|IllegalAccessException|IllegalArgumentException|InvocationTargetException|NoSuchMethodException|SecurityException e)
/*     */             {
/* 125 */               AuthException ae = new AuthException();
/* 126 */               ae.initCause(e);
/* 127 */               throw ae;
/*     */             }
/*     */             
/*     */ 
/* 131 */             moduleIndex++;
/* 132 */             key = "org.apache.catalina.authenticator.jaspic.ServerAuthModule." + moduleIndex;
/* 133 */             moduleClassName = (String)mergedProperties.get(key);
/*     */           }
/*     */           
/* 136 */           if (modules.size() == 0) {
/* 137 */             throw new AuthException(sm.getString("simpleServerAuthConfig.noModules"));
/*     */           }
/*     */           
/* 140 */           this.serverAuthContext = createServerAuthContext(modules);
/*     */         }
/* 142 */         serverAuthContext = this.serverAuthContext;
/*     */       }
/*     */     }
/*     */     
/* 146 */     return serverAuthContext;
/*     */   }
/*     */   
/*     */   protected ServerAuthContext createServerAuthContext(List<ServerAuthModule> modules)
/*     */   {
/* 151 */     return new SimpleServerAuthContext(modules);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\authenticator\jaspic\SimpleServerAuthConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */