/*     */ package org.springframework.boot.context.embedded;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.JarURLConnection;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.net.URLConnection;
/*     */ import java.security.CodeSource;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.jar.JarFile;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.boot.ApplicationHome;
/*     */ import org.springframework.boot.ApplicationTemp;
/*     */ import org.springframework.util.Assert;
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
/*     */ public abstract class AbstractEmbeddedServletContainerFactory
/*     */   extends AbstractConfigurableEmbeddedServletContainer
/*     */   implements EmbeddedServletContainerFactory
/*     */ {
/*  48 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  50 */   private static final String[] COMMON_DOC_ROOTS = { "src/main/webapp", "public", "static" };
/*     */   
/*     */ 
/*     */   public AbstractEmbeddedServletContainerFactory() {}
/*     */   
/*     */ 
/*     */   public AbstractEmbeddedServletContainerFactory(int port)
/*     */   {
/*  58 */     super(port);
/*     */   }
/*     */   
/*     */   public AbstractEmbeddedServletContainerFactory(String contextPath, int port) {
/*  62 */     super(contextPath, port);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final File getValidDocumentRoot()
/*     */   {
/*  71 */     File file = getDocumentRoot();
/*     */     
/*  73 */     file = file != null ? file : getWarFileDocumentRoot();
/*     */     
/*  75 */     file = file != null ? file : getExplodedWarFileDocumentRoot();
/*     */     
/*  77 */     file = file != null ? file : getCommonDocumentRoot();
/*  78 */     if ((file == null) && (this.logger.isDebugEnabled()))
/*     */     {
/*  80 */       this.logger.debug("None of the document roots " + Arrays.asList(COMMON_DOC_ROOTS) + " point to a directory and will be ignored.");
/*     */ 
/*     */     }
/*  83 */     else if (this.logger.isDebugEnabled()) {
/*  84 */       this.logger.debug("Document root: " + file);
/*     */     }
/*  86 */     return file;
/*     */   }
/*     */   
/*     */   private File getExplodedWarFileDocumentRoot() {
/*  90 */     return getExplodedWarFileDocumentRoot(getCodeSourceArchive());
/*     */   }
/*     */   
/*     */   protected List<URL> getUrlsOfJarsWithMetaInfResources() {
/*  94 */     ClassLoader classLoader = getClass().getClassLoader();
/*  95 */     List<URL> staticResourceUrls = new ArrayList();
/*  96 */     if ((classLoader instanceof URLClassLoader)) {
/*  97 */       for (URL url : ((URLClassLoader)classLoader).getURLs()) {
/*     */         try {
/*  99 */           if ("file".equals(url.getProtocol())) {
/* 100 */             File file = new File(url.getFile());
/* 101 */             if ((file.isDirectory()) && 
/* 102 */               (new File(file, "META-INF/resources").isDirectory())) {
/* 103 */               staticResourceUrls.add(url);
/*     */             }
/* 105 */             else if (isResourcesJar(file)) {
/* 106 */               staticResourceUrls.add(url);
/*     */             }
/*     */           }
/*     */           else {
/* 110 */             URLConnection connection = url.openConnection();
/* 111 */             if (((connection instanceof JarURLConnection)) && 
/* 112 */               (isResourcesJar((JarURLConnection)connection))) {
/* 113 */               staticResourceUrls.add(url);
/*     */             }
/*     */           }
/*     */         }
/*     */         catch (IOException ex)
/*     */         {
/* 119 */           throw new IllegalStateException(ex);
/*     */         }
/*     */       }
/*     */     }
/* 123 */     return staticResourceUrls;
/*     */   }
/*     */   
/*     */   private boolean isResourcesJar(JarURLConnection connection) {
/*     */     try {
/* 128 */       return isResourcesJar(connection.getJarFile());
/*     */     }
/*     */     catch (IOException ex) {}
/* 131 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isResourcesJar(File file)
/*     */   {
/*     */     try {
/* 137 */       return isResourcesJar(new JarFile(file));
/*     */     }
/*     */     catch (IOException ex) {}
/* 140 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isResourcesJar(JarFile jar) throws IOException
/*     */   {
/*     */     try {
/* 146 */       return (jar.getName().endsWith(".jar")) && 
/* 147 */         (jar.getJarEntry("META-INF/resources") != null);
/*     */     }
/*     */     finally {
/* 150 */       jar.close();
/*     */     }
/*     */   }
/*     */   
/*     */   File getExplodedWarFileDocumentRoot(File codeSourceFile) {
/* 155 */     if (this.logger.isDebugEnabled()) {
/* 156 */       this.logger.debug("Code archive: " + codeSourceFile);
/*     */     }
/* 158 */     if ((codeSourceFile != null) && (codeSourceFile.exists())) {
/* 159 */       String path = codeSourceFile.getAbsolutePath();
/*     */       
/* 161 */       int webInfPathIndex = path.indexOf(File.separatorChar + "WEB-INF" + File.separatorChar);
/* 162 */       if (webInfPathIndex >= 0) {
/* 163 */         path = path.substring(0, webInfPathIndex);
/* 164 */         return new File(path);
/*     */       }
/*     */     }
/* 167 */     return null;
/*     */   }
/*     */   
/*     */   private File getWarFileDocumentRoot() {
/* 171 */     return getArchiveFileDocumentRoot(".war");
/*     */   }
/*     */   
/*     */   private File getArchiveFileDocumentRoot(String extension) {
/* 175 */     File file = getCodeSourceArchive();
/* 176 */     if (this.logger.isDebugEnabled()) {
/* 177 */       this.logger.debug("Code archive: " + file);
/*     */     }
/* 179 */     if ((file != null) && (file.exists()) && (!file.isDirectory()) && 
/* 180 */       (file.getName().toLowerCase().endsWith(extension))) {
/* 181 */       return file.getAbsoluteFile();
/*     */     }
/* 183 */     return null;
/*     */   }
/*     */   
/*     */   private File getCommonDocumentRoot() {
/* 187 */     for (String commonDocRoot : COMMON_DOC_ROOTS) {
/* 188 */       File root = new File(commonDocRoot);
/* 189 */       if ((root.exists()) && (root.isDirectory())) {
/* 190 */         return root.getAbsoluteFile();
/*     */       }
/*     */     }
/* 193 */     return null;
/*     */   }
/*     */   
/*     */   private File getCodeSourceArchive() {
/* 197 */     return getCodeSourceArchive(getClass().getProtectionDomain().getCodeSource());
/*     */   }
/*     */   
/*     */   File getCodeSourceArchive(CodeSource codeSource) {
/*     */     try {
/* 202 */       URL location = codeSource == null ? null : codeSource.getLocation();
/* 203 */       if (location == null) {
/* 204 */         return null;
/*     */       }
/*     */       
/* 207 */       URLConnection connection = location.openConnection();
/* 208 */       String path; String path; if ((connection instanceof JarURLConnection)) {
/* 209 */         path = ((JarURLConnection)connection).getJarFile().getName();
/*     */       }
/*     */       else {
/* 212 */         path = location.toURI().getPath();
/*     */       }
/* 214 */       if (path.contains("!/")) {
/* 215 */         path = path.substring(0, path.indexOf("!/"));
/*     */       }
/* 217 */       return new File(path);
/*     */     }
/*     */     catch (Exception ex) {}
/* 220 */     return null;
/*     */   }
/*     */   
/*     */   protected final File getValidSessionStoreDir()
/*     */   {
/* 225 */     return getValidSessionStoreDir(true);
/*     */   }
/*     */   
/*     */   protected final File getValidSessionStoreDir(boolean mkdirs) {
/* 229 */     File dir = getSessionStoreDir();
/* 230 */     if (dir == null) {
/* 231 */       return new ApplicationTemp().getDir("servlet-sessions");
/*     */     }
/* 233 */     if (!dir.isAbsolute()) {
/* 234 */       dir = new File(new ApplicationHome().getDir(), dir.getPath());
/*     */     }
/* 236 */     if ((!dir.exists()) && (mkdirs)) {
/* 237 */       dir.mkdirs();
/*     */     }
/* 239 */     Assert.state((!mkdirs) || (dir.exists()), "Session dir " + dir + " does not exist");
/* 240 */     Assert.state(!dir.isFile(), "Session dir " + dir + " points to a file");
/* 241 */     return dir;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected File createTempDir(String prefix)
/*     */   {
/*     */     try
/*     */     {
/* 251 */       File tempDir = File.createTempFile(prefix + ".", "." + getPort());
/* 252 */       tempDir.delete();
/* 253 */       tempDir.mkdir();
/* 254 */       tempDir.deleteOnExit();
/* 255 */       return tempDir;
/*     */ 
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/* 260 */       throw new EmbeddedServletContainerException("Unable to create tempDir. java.io.tmpdir is set to " + System.getProperty("java.io.tmpdir"), ex);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\AbstractEmbeddedServletContainerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */