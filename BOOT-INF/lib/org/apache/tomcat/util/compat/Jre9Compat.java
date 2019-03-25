/*     */ package org.apache.tomcat.util.compat;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Deque;
/*     */ import java.util.Set;
/*     */ import java.util.jar.JarFile;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLParameters;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
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
/*     */ class Jre9Compat
/*     */   extends Jre8Compat
/*     */ {
/*  42 */   private static final Log log = LogFactory.getLog(Jre9Compat.class);
/*  43 */   private static final StringManager sm = StringManager.getManager(Jre9Compat.class);
/*     */   
/*     */   private static final Class<?> inaccessibleObjectExceptionClazz;
/*     */   private static final Method setApplicationProtocolsMethod;
/*     */   private static final Method getApplicationProtocolMethod;
/*     */   private static final Method setDefaultUseCachesMethod;
/*     */   private static final Method bootMethod;
/*     */   private static final Method configurationMethod;
/*     */   private static final Method modulesMethod;
/*     */   private static final Method referenceMethod;
/*     */   private static final Method locationMethod;
/*     */   private static final Method isPresentMethod;
/*     */   private static final Method getMethod;
/*     */   private static final Constructor<JarFile> jarFileConstructor;
/*     */   private static final Method isMultiReleaseMethod;
/*     */   private static final Object RUNTIME_VERSION;
/*     */   private static final int RUNTIME_MAJOR_VERSION;
/*     */   
/*     */   static
/*     */   {
/*  63 */     Class<?> c1 = null;
/*  64 */     Method m2 = null;
/*  65 */     Method m3 = null;
/*  66 */     Method m4 = null;
/*  67 */     Method m5 = null;
/*  68 */     Method m6 = null;
/*  69 */     Method m7 = null;
/*  70 */     Method m8 = null;
/*  71 */     Method m9 = null;
/*  72 */     Method m10 = null;
/*  73 */     Method m11 = null;
/*  74 */     Constructor<JarFile> c12 = null;
/*  75 */     Method m13 = null;
/*  76 */     Object o14 = null;
/*  77 */     Object o15 = null;
/*     */     try
/*     */     {
/*  80 */       Class<?> moduleLayerClazz = Class.forName("java.lang.ModuleLayer");
/*  81 */       Class<?> configurationClazz = Class.forName("java.lang.module.Configuration");
/*  82 */       Class<?> resolvedModuleClazz = Class.forName("java.lang.module.ResolvedModule");
/*  83 */       Class<?> moduleReferenceClazz = Class.forName("java.lang.module.ModuleReference");
/*  84 */       Class<?> optionalClazz = Class.forName("java.util.Optional");
/*  85 */       Class<?> versionClazz = Class.forName("java.lang.Runtime$Version");
/*  86 */       Method runtimeVersionMethod = JarFile.class.getMethod("runtimeVersion", new Class[0]);
/*  87 */       Method majorMethod = versionClazz.getMethod("major", new Class[0]);
/*     */       
/*  89 */       c1 = Class.forName("java.lang.reflect.InaccessibleObjectException");
/*  90 */       m2 = SSLParameters.class.getMethod("setApplicationProtocols", new Class[] { String[].class });
/*  91 */       m3 = SSLEngine.class.getMethod("getApplicationProtocol", new Class[0]);
/*  92 */       m4 = URLConnection.class.getMethod("setDefaultUseCaches", new Class[] { String.class, Boolean.TYPE });
/*  93 */       m5 = moduleLayerClazz.getMethod("boot", new Class[0]);
/*  94 */       m6 = moduleLayerClazz.getMethod("configuration", new Class[0]);
/*  95 */       m7 = configurationClazz.getMethod("modules", new Class[0]);
/*  96 */       m8 = resolvedModuleClazz.getMethod("reference", new Class[0]);
/*  97 */       m9 = moduleReferenceClazz.getMethod("location", new Class[0]);
/*  98 */       m10 = optionalClazz.getMethod("isPresent", new Class[0]);
/*  99 */       m11 = optionalClazz.getMethod("get", new Class[0]);
/* 100 */       c12 = JarFile.class.getConstructor(new Class[] { File.class, Boolean.TYPE, Integer.TYPE, versionClazz });
/* 101 */       m13 = JarFile.class.getMethod("isMultiRelease", new Class[0]);
/* 102 */       o14 = runtimeVersionMethod.invoke(null, new Object[0]);
/* 103 */       o15 = majorMethod.invoke(o14, new Object[0]);
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {}catch (ReflectiveOperationException|IllegalArgumentException localReflectiveOperationException) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 111 */     inaccessibleObjectExceptionClazz = c1;
/* 112 */     setApplicationProtocolsMethod = m2;
/* 113 */     getApplicationProtocolMethod = m3;
/* 114 */     setDefaultUseCachesMethod = m4;
/* 115 */     bootMethod = m5;
/* 116 */     configurationMethod = m6;
/* 117 */     modulesMethod = m7;
/* 118 */     referenceMethod = m8;
/* 119 */     locationMethod = m9;
/* 120 */     isPresentMethod = m10;
/* 121 */     getMethod = m11;
/* 122 */     jarFileConstructor = c12;
/* 123 */     isMultiReleaseMethod = m13;
/*     */     
/* 125 */     RUNTIME_VERSION = o14;
/* 126 */     if (o15 != null) {
/* 127 */       RUNTIME_MAJOR_VERSION = ((Integer)o15).intValue();
/*     */     }
/*     */     else {
/* 130 */       RUNTIME_MAJOR_VERSION = 8;
/*     */     }
/*     */   }
/*     */   
/*     */   static boolean isSupported()
/*     */   {
/* 136 */     return inaccessibleObjectExceptionClazz != null;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isInstanceOfInaccessibleObjectException(Throwable t)
/*     */   {
/* 142 */     if (t == null) {
/* 143 */       return false;
/*     */     }
/*     */     
/* 146 */     return inaccessibleObjectExceptionClazz.isAssignableFrom(t.getClass());
/*     */   }
/*     */   
/*     */   public void setApplicationProtocols(SSLParameters sslParameters, String[] protocols)
/*     */   {
/*     */     try
/*     */     {
/* 153 */       setApplicationProtocolsMethod.invoke(sslParameters, new Object[] { protocols });
/*     */     } catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException e) {
/* 155 */       throw new UnsupportedOperationException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public String getApplicationProtocol(SSLEngine sslEngine)
/*     */   {
/*     */     try
/*     */     {
/* 163 */       return (String)getApplicationProtocolMethod.invoke(sslEngine, new Object[0]);
/*     */     } catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException e) {
/* 165 */       throw new UnsupportedOperationException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void disableCachingForJarUrlConnections() throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 173 */       setDefaultUseCachesMethod.invoke(null, new Object[] { "JAR", Boolean.FALSE });
/*     */     } catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException e) {
/* 175 */       throw new UnsupportedOperationException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addBootModulePath(Deque<URL> classPathUrlsToProcess)
/*     */   {
/*     */     try
/*     */     {
/* 183 */       Object bootLayer = bootMethod.invoke(null, new Object[0]);
/* 184 */       Object bootConfiguration = configurationMethod.invoke(bootLayer, new Object[0]);
/* 185 */       Set<?> resolvedModules = (Set)modulesMethod.invoke(bootConfiguration, new Object[0]);
/* 186 */       for (Object resolvedModule : resolvedModules) {
/* 187 */         Object moduleReference = referenceMethod.invoke(resolvedModule, new Object[0]);
/* 188 */         Object optionalURI = locationMethod.invoke(moduleReference, new Object[0]);
/* 189 */         Boolean isPresent = (Boolean)isPresentMethod.invoke(optionalURI, new Object[0]);
/* 190 */         if (isPresent.booleanValue()) {
/* 191 */           URI uri = (URI)getMethod.invoke(optionalURI, new Object[0]);
/*     */           try {
/* 193 */             URL url = uri.toURL();
/* 194 */             classPathUrlsToProcess.add(url);
/*     */           } catch (MalformedURLException e) {
/* 196 */             log.warn(sm.getString("jre9Compat.invalidModuleUri", new Object[] { uri }), e);
/*     */           }
/*     */         }
/*     */       }
/*     */     } catch (ReflectiveOperationException e) {
/* 201 */       throw new UnsupportedOperationException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public JarFile jarFileNewInstance(File f) throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 209 */       return (JarFile)jarFileConstructor.newInstance(new Object[] { f, Boolean.TRUE, 
/* 210 */         Integer.valueOf(1), RUNTIME_VERSION });
/*     */     } catch (ReflectiveOperationException|IllegalArgumentException e) {
/* 212 */       throw new IOException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean jarFileIsMultiRelease(JarFile jarFile)
/*     */   {
/*     */     try
/*     */     {
/* 220 */       return ((Boolean)isMultiReleaseMethod.invoke(jarFile, new Object[0])).booleanValue();
/*     */     } catch (ReflectiveOperationException|IllegalArgumentException e) {}
/* 222 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int jarFileRuntimeMajorVersion()
/*     */   {
/* 229 */     return RUNTIME_MAJOR_VERSION;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\compat\Jre9Compat.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */