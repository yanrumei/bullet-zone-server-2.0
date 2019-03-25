/*     */ package org.apache.catalina.security;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
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
/*     */ public final class SecurityClassLoad
/*     */ {
/*     */   public static void securityClassLoad(ClassLoader loader)
/*     */     throws Exception
/*     */   {
/*  30 */     securityClassLoad(loader, true);
/*     */   }
/*     */   
/*     */   static void securityClassLoad(ClassLoader loader, boolean requireSecurityManager) throws Exception
/*     */   {
/*  35 */     if ((requireSecurityManager) && (System.getSecurityManager() == null)) {
/*  36 */       return;
/*     */     }
/*     */     
/*  39 */     loadCorePackage(loader);
/*  40 */     loadCoyotePackage(loader);
/*  41 */     loadLoaderPackage(loader);
/*  42 */     loadRealmPackage(loader);
/*  43 */     loadServletsPackage(loader);
/*  44 */     loadSessionPackage(loader);
/*  45 */     loadUtilPackage(loader);
/*  46 */     loadValvesPackage(loader);
/*  47 */     loadJavaxPackage(loader);
/*  48 */     loadConnectorPackage(loader);
/*  49 */     loadTomcatPackage(loader);
/*     */   }
/*     */   
/*     */   private static final void loadCorePackage(ClassLoader loader) throws Exception {
/*  53 */     String basePackage = "org.apache.catalina.core.";
/*  54 */     loader.loadClass("org.apache.catalina.core.AccessLogAdapter");
/*  55 */     loader.loadClass("org.apache.catalina.core.ApplicationContextFacade$PrivilegedExecuteMethod");
/*  56 */     loader.loadClass("org.apache.catalina.core.ApplicationDispatcher$PrivilegedForward");
/*  57 */     loader.loadClass("org.apache.catalina.core.ApplicationDispatcher$PrivilegedInclude");
/*  58 */     loader.loadClass("org.apache.catalina.core.ApplicationPushBuilder");
/*  59 */     loader.loadClass("org.apache.catalina.core.AsyncContextImpl");
/*  60 */     loader.loadClass("org.apache.catalina.core.AsyncContextImpl$AsyncRunnable");
/*  61 */     loader.loadClass("org.apache.catalina.core.AsyncContextImpl$DebugException");
/*  62 */     loader.loadClass("org.apache.catalina.core.AsyncListenerWrapper");
/*  63 */     loader.loadClass("org.apache.catalina.core.ContainerBase$PrivilegedAddChild");
/*  64 */     loadAnonymousInnerClasses(loader, "org.apache.catalina.core.DefaultInstanceManager");
/*  65 */     loader.loadClass("org.apache.catalina.core.DefaultInstanceManager$AnnotationCacheEntry");
/*  66 */     loader.loadClass("org.apache.catalina.core.DefaultInstanceManager$AnnotationCacheEntryType");
/*  67 */     loader.loadClass("org.apache.catalina.core.ApplicationHttpRequest$AttributeNamesEnumerator");
/*     */   }
/*     */   
/*     */   private static final void loadLoaderPackage(ClassLoader loader) throws Exception {
/*  71 */     String basePackage = "org.apache.catalina.loader.";
/*  72 */     loader.loadClass("org.apache.catalina.loader.WebappClassLoaderBase$PrivilegedFindClassByName");
/*  73 */     loader.loadClass("org.apache.catalina.loader.WebappClassLoaderBase$PrivilegedHasLoggingConfig");
/*     */   }
/*     */   
/*     */   private static final void loadRealmPackage(ClassLoader loader) throws Exception {
/*  77 */     String basePackage = "org.apache.catalina.realm.";
/*  78 */     loader.loadClass("org.apache.catalina.realm.LockOutRealm$LockRecord");
/*     */   }
/*     */   
/*     */   private static final void loadServletsPackage(ClassLoader loader) throws Exception {
/*  82 */     String basePackage = "org.apache.catalina.servlets.";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  88 */     loader.loadClass("org.apache.catalina.servlets.DefaultServlet");
/*     */   }
/*     */   
/*     */   private static final void loadSessionPackage(ClassLoader loader) throws Exception {
/*  92 */     String basePackage = "org.apache.catalina.session.";
/*  93 */     loader.loadClass("org.apache.catalina.session.StandardSession");
/*  94 */     loadAnonymousInnerClasses(loader, "org.apache.catalina.session.StandardSession");
/*  95 */     loader.loadClass("org.apache.catalina.session.StandardManager$PrivilegedDoUnload");
/*     */   }
/*     */   
/*     */   private static final void loadUtilPackage(ClassLoader loader) throws Exception {
/*  99 */     String basePackage = "org.apache.catalina.util.";
/* 100 */     loader.loadClass("org.apache.catalina.util.ParameterMap");
/* 101 */     loader.loadClass("org.apache.catalina.util.RequestUtil");
/* 102 */     loader.loadClass("org.apache.catalina.util.TLSUtil");
/*     */   }
/*     */   
/*     */   private static final void loadValvesPackage(ClassLoader loader) throws Exception {
/* 106 */     String basePackage = "org.apache.catalina.valves.";
/* 107 */     loadAnonymousInnerClasses(loader, "org.apache.catalina.valves.AbstractAccessLogValve");
/*     */   }
/*     */   
/*     */   private static final void loadCoyotePackage(ClassLoader loader) throws Exception {
/* 111 */     String basePackage = "org.apache.coyote.";
/* 112 */     loader.loadClass("org.apache.coyote.http11.Constants");
/*     */     
/* 114 */     Class<?> clazz = loader.loadClass("org.apache.coyote.Constants");
/* 115 */     clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/* 116 */     loader.loadClass("org.apache.coyote.http2.Stream$PrivilegedPush");
/*     */   }
/*     */   
/*     */   private static final void loadJavaxPackage(ClassLoader loader) throws Exception {
/* 120 */     loader.loadClass("javax.servlet.http.Cookie");
/*     */   }
/*     */   
/*     */   private static final void loadConnectorPackage(ClassLoader loader) throws Exception {
/* 124 */     String basePackage = "org.apache.catalina.connector.";
/* 125 */     loader.loadClass("org.apache.catalina.connector.RequestFacade$GetAttributePrivilegedAction");
/* 126 */     loader.loadClass("org.apache.catalina.connector.RequestFacade$GetParameterMapPrivilegedAction");
/* 127 */     loader.loadClass("org.apache.catalina.connector.RequestFacade$GetRequestDispatcherPrivilegedAction");
/* 128 */     loader.loadClass("org.apache.catalina.connector.RequestFacade$GetParameterPrivilegedAction");
/* 129 */     loader.loadClass("org.apache.catalina.connector.RequestFacade$GetParameterNamesPrivilegedAction");
/* 130 */     loader.loadClass("org.apache.catalina.connector.RequestFacade$GetParameterValuePrivilegedAction");
/* 131 */     loader.loadClass("org.apache.catalina.connector.RequestFacade$GetCharacterEncodingPrivilegedAction");
/* 132 */     loader.loadClass("org.apache.catalina.connector.RequestFacade$GetHeadersPrivilegedAction");
/* 133 */     loader.loadClass("org.apache.catalina.connector.RequestFacade$GetHeaderNamesPrivilegedAction");
/* 134 */     loader.loadClass("org.apache.catalina.connector.RequestFacade$GetCookiesPrivilegedAction");
/* 135 */     loader.loadClass("org.apache.catalina.connector.RequestFacade$GetLocalePrivilegedAction");
/* 136 */     loader.loadClass("org.apache.catalina.connector.RequestFacade$GetLocalesPrivilegedAction");
/* 137 */     loader.loadClass("org.apache.catalina.connector.ResponseFacade$SetContentTypePrivilegedAction");
/* 138 */     loader.loadClass("org.apache.catalina.connector.ResponseFacade$DateHeaderPrivilegedAction");
/* 139 */     loader.loadClass("org.apache.catalina.connector.RequestFacade$GetSessionPrivilegedAction");
/* 140 */     loadAnonymousInnerClasses(loader, "org.apache.catalina.connector.ResponseFacade");
/* 141 */     loadAnonymousInnerClasses(loader, "org.apache.catalina.connector.OutputBuffer");
/* 142 */     loadAnonymousInnerClasses(loader, "org.apache.catalina.connector.CoyoteInputStream");
/* 143 */     loadAnonymousInnerClasses(loader, "org.apache.catalina.connector.InputBuffer");
/* 144 */     loadAnonymousInnerClasses(loader, "org.apache.catalina.connector.Response");
/*     */   }
/*     */   
/*     */   private static final void loadTomcatPackage(ClassLoader loader) throws Exception {
/* 148 */     String basePackage = "org.apache.tomcat.";
/*     */     
/* 150 */     loader.loadClass("org.apache.tomcat.util.buf.B2CConverter");
/* 151 */     loader.loadClass("org.apache.tomcat.util.buf.ByteBufferUtils");
/* 152 */     loader.loadClass("org.apache.tomcat.util.buf.C2BConverter");
/* 153 */     loader.loadClass("org.apache.tomcat.util.buf.HexUtils");
/* 154 */     loader.loadClass("org.apache.tomcat.util.buf.StringCache");
/* 155 */     loader.loadClass("org.apache.tomcat.util.buf.StringCache$ByteEntry");
/* 156 */     loader.loadClass("org.apache.tomcat.util.buf.StringCache$CharEntry");
/* 157 */     loader.loadClass("org.apache.tomcat.util.buf.UriUtil");
/*     */     
/* 159 */     Class<?> clazz = loader.loadClass("org.apache.tomcat.util.collections.CaseInsensitiveKeyMap");
/*     */     
/* 161 */     clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/* 162 */     loader.loadClass("org.apache.tomcat.util.collections.CaseInsensitiveKeyMap$EntryImpl");
/* 163 */     loader.loadClass("org.apache.tomcat.util.collections.CaseInsensitiveKeyMap$EntryIterator");
/* 164 */     loader.loadClass("org.apache.tomcat.util.collections.CaseInsensitiveKeyMap$EntrySet");
/* 165 */     loader.loadClass("org.apache.tomcat.util.collections.CaseInsensitiveKeyMap$Key");
/*     */     
/* 167 */     loader.loadClass("org.apache.tomcat.util.http.CookieProcessor");
/* 168 */     loader.loadClass("org.apache.tomcat.util.http.NamesEnumerator");
/*     */     
/* 170 */     clazz = loader.loadClass("org.apache.tomcat.util.http.FastHttpDateFormat");
/* 171 */     clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/* 172 */     loader.loadClass("org.apache.tomcat.util.http.parser.HttpParser");
/* 173 */     loader.loadClass("org.apache.tomcat.util.http.parser.MediaType");
/* 174 */     loader.loadClass("org.apache.tomcat.util.http.parser.MediaTypeCache");
/* 175 */     loader.loadClass("org.apache.tomcat.util.http.parser.SkipResult");
/*     */     
/* 177 */     loader.loadClass("org.apache.tomcat.util.net.Constants");
/* 178 */     loader.loadClass("org.apache.tomcat.util.net.DispatchType");
/* 179 */     loader.loadClass("org.apache.tomcat.util.net.NioBlockingSelector$BlockPoller$RunnableAdd");
/* 180 */     loader.loadClass("org.apache.tomcat.util.net.NioBlockingSelector$BlockPoller$RunnableCancel");
/* 181 */     loader.loadClass("org.apache.tomcat.util.net.NioBlockingSelector$BlockPoller$RunnableRemove");
/*     */     
/* 183 */     loader.loadClass("org.apache.tomcat.util.security.PrivilegedGetTccl");
/* 184 */     loader.loadClass("org.apache.tomcat.util.security.PrivilegedSetTccl");
/*     */   }
/*     */   
/*     */   private static final void loadAnonymousInnerClasses(ClassLoader loader, String enclosingClass) {
/*     */     try {
/* 189 */       for (int i = 1;; i++) {
/* 190 */         loader.loadClass(enclosingClass + '$' + i);
/*     */       }
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\security\SecurityClassLoad.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */