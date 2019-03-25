/*     */ package org.springframework.boot.loader;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.security.CodeSource;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.boot.loader.archive.Archive;
/*     */ import org.springframework.boot.loader.archive.ExplodedArchive;
/*     */ import org.springframework.boot.loader.archive.JarFileArchive;
/*     */ import org.springframework.boot.loader.jar.JarFile;
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
/*     */ 
/*     */ 
/*     */ public abstract class Launcher
/*     */ {
/*     */   protected void launch(String[] args)
/*     */     throws Exception
/*     */   {
/*  48 */     JarFile.registerUrlProtocolHandler();
/*  49 */     ClassLoader classLoader = createClassLoader(getClassPathArchives());
/*  50 */     launch(args, getMainClass(), classLoader);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ClassLoader createClassLoader(List<Archive> archives)
/*     */     throws Exception
/*     */   {
/*  60 */     List<URL> urls = new ArrayList(archives.size());
/*  61 */     for (Archive archive : archives) {
/*  62 */       urls.add(archive.getUrl());
/*     */     }
/*  64 */     return createClassLoader((URL[])urls.toArray(new URL[urls.size()]));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ClassLoader createClassLoader(URL[] urls)
/*     */     throws Exception
/*     */   {
/*  74 */     return new LaunchedURLClassLoader(urls, getClass().getClassLoader());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void launch(String[] args, String mainClass, ClassLoader classLoader)
/*     */     throws Exception
/*     */   {
/*  86 */     Thread.currentThread().setContextClassLoader(classLoader);
/*  87 */     createMainMethodRunner(mainClass, args, classLoader).run();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected MainMethodRunner createMainMethodRunner(String mainClass, String[] args, ClassLoader classLoader)
/*     */   {
/*  99 */     return new MainMethodRunner(mainClass, args);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract String getMainClass()
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract List<Archive> getClassPathArchives()
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */   protected final Archive createArchive()
/*     */     throws Exception
/*     */   {
/* 117 */     ProtectionDomain protectionDomain = getClass().getProtectionDomain();
/* 118 */     CodeSource codeSource = protectionDomain.getCodeSource();
/* 119 */     URI location = codeSource == null ? null : codeSource.getLocation().toURI();
/* 120 */     String path = location == null ? null : location.getSchemeSpecificPart();
/* 121 */     if (path == null) {
/* 122 */       throw new IllegalStateException("Unable to determine code source archive");
/*     */     }
/* 124 */     File root = new File(path);
/* 125 */     if (!root.exists()) {
/* 126 */       throw new IllegalStateException("Unable to determine code source archive from " + root);
/*     */     }
/*     */     
/* 129 */     return root.isDirectory() ? new ExplodedArchive(root) : new JarFileArchive(root);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\org\springframework\boot\loader\Launcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */