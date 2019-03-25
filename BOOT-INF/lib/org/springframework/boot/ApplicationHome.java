/*     */ package org.springframework.boot;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.JarURLConnection;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.security.CodeSource;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.Enumeration;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.Manifest;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ 
/*     */ public class ApplicationHome
/*     */ {
/*     */   private final File source;
/*     */   private final File dir;
/*     */   
/*     */   public ApplicationHome()
/*     */   {
/*  51 */     this(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ApplicationHome(Class<?> sourceClass)
/*     */   {
/*  59 */     this.source = findSource(sourceClass == null ? getStartClass() : sourceClass);
/*  60 */     this.dir = findHomeDir(this.source);
/*     */   }
/*     */   
/*     */   private Class<?> getStartClass() {
/*     */     try {
/*  65 */       ClassLoader classLoader = getClass().getClassLoader();
/*  66 */       return getStartClass(classLoader.getResources("META-INF/MANIFEST.MF"));
/*     */     }
/*     */     catch (Exception ex) {}
/*  69 */     return null;
/*     */   }
/*     */   
/*     */   private Class<?> getStartClass(Enumeration<URL> manifestResources)
/*     */   {
/*  74 */     while (manifestResources.hasMoreElements()) {
/*     */       try {
/*  76 */         InputStream inputStream = ((URL)manifestResources.nextElement()).openStream();
/*     */         try {
/*  78 */           Manifest manifest = new Manifest(inputStream);
/*     */           
/*  80 */           String startClass = manifest.getMainAttributes().getValue("Start-Class");
/*  81 */           if (startClass != null) {
/*  82 */             return ClassUtils.forName(startClass, 
/*  83 */               getClass().getClassLoader());
/*     */           }
/*     */         }
/*     */         finally {
/*  87 */           inputStream.close();
/*     */         }
/*     */       }
/*     */       catch (Exception localException) {}
/*     */     }
/*     */     
/*  93 */     return null;
/*     */   }
/*     */   
/*     */   private File findSource(Class<?> sourceClass)
/*     */   {
/*     */     try {
/*  99 */       ProtectionDomain domain = sourceClass == null ? null : sourceClass.getProtectionDomain();
/* 100 */       CodeSource codeSource = domain == null ? null : domain.getCodeSource();
/* 101 */       URL location = codeSource == null ? null : codeSource.getLocation();
/* 102 */       File source = location == null ? null : findSource(location);
/* 103 */       if ((source != null) && (source.exists()) && (!isUnitTest())) {
/* 104 */         return source.getAbsoluteFile();
/*     */       }
/* 106 */       return null;
/*     */     }
/*     */     catch (Exception ex) {}
/* 109 */     return null;
/*     */   }
/*     */   
/*     */   private boolean isUnitTest()
/*     */   {
/*     */     try {
/* 115 */       for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
/* 116 */         if (element.getClassName().startsWith("org.junit.")) {
/* 117 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception localException1) {}
/*     */     
/* 123 */     return false;
/*     */   }
/*     */   
/*     */   private File findSource(URL location) throws IOException {
/* 127 */     URLConnection connection = location.openConnection();
/* 128 */     if ((connection instanceof JarURLConnection)) {
/* 129 */       return getRootJarFile(((JarURLConnection)connection).getJarFile());
/*     */     }
/* 131 */     return new File(location.getPath());
/*     */   }
/*     */   
/*     */   private File getRootJarFile(JarFile jarFile) {
/* 135 */     String name = jarFile.getName();
/* 136 */     int separator = name.indexOf("!/");
/* 137 */     if (separator > 0) {
/* 138 */       name = name.substring(0, separator);
/*     */     }
/* 140 */     return new File(name);
/*     */   }
/*     */   
/*     */   private File findHomeDir(File source) {
/* 144 */     File homeDir = source;
/* 145 */     homeDir = homeDir == null ? findDefaultHomeDir() : homeDir;
/* 146 */     if (homeDir.isFile()) {
/* 147 */       homeDir = homeDir.getParentFile();
/*     */     }
/* 149 */     homeDir = homeDir.exists() ? homeDir : new File(".");
/* 150 */     return homeDir.getAbsoluteFile();
/*     */   }
/*     */   
/*     */   private File findDefaultHomeDir() {
/* 154 */     String userDir = System.getProperty("user.dir");
/* 155 */     return new File(StringUtils.hasLength(userDir) ? userDir : ".");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public File getSource()
/*     */   {
/* 165 */     return this.source;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public File getDir()
/*     */   {
/* 173 */     return this.dir;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 178 */     return getDir().toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\ApplicationHome.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */