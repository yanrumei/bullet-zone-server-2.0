/*     */ package org.apache.tomcat.util.compat;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Deque;
/*     */ import java.util.jar.JarFile;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLParameters;
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
/*     */ public class JreCompat
/*     */ {
/*     */   private static final int RUNTIME_MAJOR_VERSION = 7;
/*     */   private static final JreCompat instance;
/*  42 */   private static StringManager sm = StringManager.getManager(JreCompat.class.getPackage().getName());
/*     */   
/*     */   private static final boolean jre9Available;
/*     */   
/*     */   private static final boolean jre8Available;
/*     */   
/*     */ 
/*     */   static
/*     */   {
/*  51 */     if (Jre9Compat.isSupported()) {
/*  52 */       instance = new Jre9Compat();
/*  53 */       jre9Available = true;
/*  54 */       jre8Available = true;
/*     */     }
/*  56 */     else if (Jre8Compat.isSupported()) {
/*  57 */       instance = new Jre8Compat();
/*  58 */       jre9Available = false;
/*  59 */       jre8Available = true;
/*     */     } else {
/*  61 */       instance = new JreCompat();
/*  62 */       jre9Available = false;
/*  63 */       jre8Available = false;
/*     */     }
/*     */   }
/*     */   
/*     */   public static JreCompat getInstance()
/*     */   {
/*  69 */     return instance;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean isJre8Available()
/*     */   {
/*  76 */     return jre8Available;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setUseServerCipherSuitesOrder(SSLEngine engine, boolean useCipherSuitesOrder)
/*     */   {
/*  82 */     throw new UnsupportedOperationException(sm.getString("jreCompat.noServerCipherSuiteOrder"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean isJre9Available()
/*     */   {
/*  89 */     return jre9Available;
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
/*     */   public boolean isInstanceOfInaccessibleObjectException(Throwable t)
/*     */   {
/* 104 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setApplicationProtocols(SSLParameters sslParameters, String[] protocols)
/*     */   {
/* 116 */     throw new UnsupportedOperationException(sm.getString("jreCompat.noApplicationProtocols"));
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
/*     */   public String getApplicationProtocol(SSLEngine sslEngine)
/*     */   {
/* 130 */     throw new UnsupportedOperationException(sm.getString("jreCompat.noApplicationProtocol"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void disableCachingForJarUrlConnections()
/*     */     throws IOException
/*     */   {
/* 143 */     URL url = new URL("jar:file://dummy.jar!/");
/* 144 */     URLConnection uConn = url.openConnection();
/* 145 */     uConn.setDefaultUseCaches(false);
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
/*     */   public final JarFile jarFileNewInstance(String s)
/*     */     throws IOException
/*     */   {
/* 173 */     return jarFileNewInstance(new File(s));
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
/*     */   public JarFile jarFileNewInstance(File f)
/*     */     throws IOException
/*     */   {
/* 188 */     return new JarFile(f);
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
/*     */   public boolean jarFileIsMultiRelease(JarFile jarFile)
/*     */   {
/* 202 */     return false;
/*     */   }
/*     */   
/*     */   public int jarFileRuntimeMajorVersion()
/*     */   {
/* 207 */     return 7;
/*     */   }
/*     */   
/*     */   public void addBootModulePath(Deque<URL> classPathUrlsToProcess) {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\compat\JreCompat.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */