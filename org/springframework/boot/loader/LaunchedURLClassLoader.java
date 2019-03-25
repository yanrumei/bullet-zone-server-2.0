/*     */ package org.springframework.boot.loader;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.JarURLConnection;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.net.URLConnection;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Enumeration;
/*     */ import org.springframework.boot.loader.jar.Handler;
/*     */ import org.springframework.lang.UsesJava7;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LaunchedURLClassLoader
/*     */   extends URLClassLoader
/*     */ {
/*     */   public LaunchedURLClassLoader(URL[] urls, ClassLoader parent)
/*     */   {
/*  51 */     super(urls, parent);
/*     */   }
/*     */   
/*     */   public URL findResource(String name)
/*     */   {
/*  56 */     Handler.setUseFastConnectionExceptions(true);
/*     */     try {
/*  58 */       return super.findResource(name);
/*     */     }
/*     */     finally {
/*  61 */       Handler.setUseFastConnectionExceptions(false);
/*     */     }
/*     */   }
/*     */   
/*     */   public Enumeration<URL> findResources(String name) throws IOException
/*     */   {
/*  67 */     Handler.setUseFastConnectionExceptions(true);
/*     */     try {
/*  69 */       return super.findResources(name);
/*     */     }
/*     */     finally {
/*  72 */       Handler.setUseFastConnectionExceptions(false);
/*     */     }
/*     */   }
/*     */   
/*     */   protected Class<?> loadClass(String name, boolean resolve)
/*     */     throws ClassNotFoundException
/*     */   {
/*  79 */     Handler.setUseFastConnectionExceptions(true);
/*     */     try {
/*     */       try {
/*  82 */         definePackageIfNecessary(name);
/*     */       }
/*     */       catch (IllegalArgumentException ex)
/*     */       {
/*  86 */         if (getPackage(name) == null)
/*     */         {
/*     */ 
/*     */ 
/*  90 */           throw new AssertionError("Package " + name + " has already been defined but it could not be found");
/*     */         }
/*     */       }
/*     */       
/*  94 */       return super.loadClass(name, resolve);
/*     */     }
/*     */     finally {
/*  97 */       Handler.setUseFastConnectionExceptions(false);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void definePackageIfNecessary(String className)
/*     */   {
/* 108 */     int lastDot = className.lastIndexOf('.');
/* 109 */     if (lastDot >= 0) {
/* 110 */       String packageName = className.substring(0, lastDot);
/* 111 */       if (getPackage(packageName) == null) {
/*     */         try {
/* 113 */           definePackage(className, packageName);
/*     */         }
/*     */         catch (IllegalArgumentException ex)
/*     */         {
/* 117 */           if (getPackage(packageName) == null)
/*     */           {
/*     */ 
/*     */ 
/* 121 */             throw new AssertionError("Package " + packageName + " has already been defined but it could not be found");
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void definePackage(final String className, final String packageName)
/*     */   {
/*     */     try
/*     */     {
/* 132 */       AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Object run() throws ClassNotFoundException {
/* 135 */           String packageEntryName = packageName.replace('.', '/') + "/";
/* 136 */           String classEntryName = className.replace('.', '/') + ".class";
/* 137 */           for (URL url : LaunchedURLClassLoader.this.getURLs()) {
/*     */             try {
/* 139 */               URLConnection connection = url.openConnection();
/* 140 */               if ((connection instanceof JarURLConnection))
/*     */               {
/* 142 */                 java.util.jar.JarFile jarFile = ((JarURLConnection)connection).getJarFile();
/* 143 */                 if ((jarFile.getEntry(classEntryName) != null) && 
/* 144 */                   (jarFile.getEntry(packageEntryName) != null) && 
/* 145 */                   (jarFile.getManifest() != null)) {
/* 146 */                   LaunchedURLClassLoader.this.definePackage(packageName, jarFile.getManifest(), url);
/*     */                   
/* 148 */                   return null;
/*     */                 }
/*     */               }
/*     */             }
/*     */             catch (IOException localIOException) {}
/*     */           }
/*     */           
/*     */ 
/* 156 */           return null;
/*     */         }
/* 158 */       }, AccessController.getContext());
/*     */     }
/*     */     catch (PrivilegedActionException localPrivilegedActionException) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clearCache()
/*     */   {
/* 169 */     for (URL url : getURLs()) {
/*     */       try {
/* 171 */         URLConnection connection = url.openConnection();
/* 172 */         if ((connection instanceof JarURLConnection)) {
/* 173 */           clearCache(connection);
/*     */         }
/*     */       }
/*     */       catch (IOException localIOException) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void clearCache(URLConnection connection)
/*     */     throws IOException
/*     */   {
/* 184 */     Object jarFile = ((JarURLConnection)connection).getJarFile();
/* 185 */     if ((jarFile instanceof org.springframework.boot.loader.jar.JarFile)) {
/* 186 */       ((org.springframework.boot.loader.jar.JarFile)jarFile).clearCache();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava7
/*     */   private static void performParallelCapableRegistration() {
/*     */     try {
/* 193 */       ClassLoader.registerAsParallelCapable();
/*     */     }
/*     */     catch (NoSuchMethodError localNoSuchMethodError) {}
/*     */   }
/*     */   
/*     */   static {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\org\springframework\boot\loader\LaunchedURLClassLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */