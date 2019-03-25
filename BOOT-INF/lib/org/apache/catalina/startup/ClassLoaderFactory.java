/*     */ package org.apache.catalina.startup;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
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
/*     */ public final class ClassLoaderFactory
/*     */ {
/*  54 */   private static final Log log = LogFactory.getLog(ClassLoaderFactory.class);
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
/*     */   public static ClassLoader createClassLoader(File[] unpacked, File[] packed, ClassLoader parent)
/*     */     throws Exception
/*     */   {
/*  80 */     if (log.isDebugEnabled()) {
/*  81 */       log.debug("Creating new class loader");
/*     */     }
/*     */     
/*  84 */     Set<URL> set = new LinkedHashSet();
/*     */     
/*     */ 
/*  87 */     if (unpacked != null) {
/*  88 */       for (int i = 0; i < unpacked.length; i++) {
/*  89 */         File file = unpacked[i];
/*  90 */         if (file.canRead())
/*     */         {
/*  92 */           file = new File(file.getCanonicalPath() + File.separator);
/*  93 */           URL url = file.toURI().toURL();
/*  94 */           if (log.isDebugEnabled())
/*  95 */             log.debug("  Including directory " + url);
/*  96 */           set.add(url);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 101 */     if (packed != null) {
/* 102 */       for (int i = 0; i < packed.length; i++) {
/* 103 */         File directory = packed[i];
/* 104 */         if ((directory.isDirectory()) && (directory.canRead()))
/*     */         {
/* 106 */           String[] filenames = directory.list();
/* 107 */           if (filenames != null)
/*     */           {
/*     */ 
/* 110 */             for (int j = 0; j < filenames.length; j++) {
/* 111 */               String filename = filenames[j].toLowerCase(Locale.ENGLISH);
/* 112 */               if (filename.endsWith(".jar"))
/*     */               {
/* 114 */                 File file = new File(directory, filenames[j]);
/* 115 */                 if (log.isDebugEnabled())
/* 116 */                   log.debug("  Including jar file " + file.getAbsolutePath());
/* 117 */                 URL url = file.toURI().toURL();
/* 118 */                 set.add(url);
/*     */               }
/*     */             } }
/*     */         }
/*     */       }
/*     */     }
/* 124 */     final URL[] array = (URL[])set.toArray(new URL[set.size()]);
/* 125 */     (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public URLClassLoader run()
/*     */       {
/* 129 */         if (this.val$parent == null) {
/* 130 */           return new URLClassLoader(array);
/*     */         }
/* 132 */         return new URLClassLoader(array, this.val$parent);
/*     */       }
/*     */     });
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
/*     */   public static ClassLoader createClassLoader(List<Repository> repositories, ClassLoader parent)
/*     */     throws Exception
/*     */   {
/* 155 */     if (log.isDebugEnabled()) {
/* 156 */       log.debug("Creating new class loader");
/*     */     }
/*     */     
/* 159 */     Set<URL> set = new LinkedHashSet();
/*     */     
/* 161 */     if (repositories != null) {
/* 162 */       for (Repository repository : repositories)
/* 163 */         if (repository.getType() == RepositoryType.URL) {
/* 164 */           URL url = buildClassLoaderUrl(repository.getLocation());
/* 165 */           if (log.isDebugEnabled())
/* 166 */             log.debug("  Including URL " + url);
/* 167 */           set.add(url);
/* 168 */         } else if (repository.getType() == RepositoryType.DIR) {
/* 169 */           File directory = new File(repository.getLocation());
/* 170 */           directory = directory.getCanonicalFile();
/* 171 */           if (validateFile(directory, RepositoryType.DIR))
/*     */           {
/*     */ 
/* 174 */             URL url = buildClassLoaderUrl(directory);
/* 175 */             if (log.isDebugEnabled())
/* 176 */               log.debug("  Including directory " + url);
/* 177 */             set.add(url);
/* 178 */           } } else if (repository.getType() == RepositoryType.JAR) {
/* 179 */           File file = new File(repository.getLocation());
/* 180 */           file = file.getCanonicalFile();
/* 181 */           if (validateFile(file, RepositoryType.JAR))
/*     */           {
/*     */ 
/* 184 */             URL url = buildClassLoaderUrl(file);
/* 185 */             if (log.isDebugEnabled())
/* 186 */               log.debug("  Including jar file " + url);
/* 187 */             set.add(url);
/* 188 */           } } else if (repository.getType() == RepositoryType.GLOB) {
/* 189 */           File directory = new File(repository.getLocation());
/* 190 */           directory = directory.getCanonicalFile();
/* 191 */           if (validateFile(directory, RepositoryType.GLOB))
/*     */           {
/*     */ 
/* 194 */             if (log.isDebugEnabled())
/* 195 */               log.debug("  Including directory glob " + directory
/* 196 */                 .getAbsolutePath());
/* 197 */             String[] filenames = directory.list();
/* 198 */             if (filenames != null)
/*     */             {
/*     */ 
/* 201 */               for (int j = 0; j < filenames.length; j++) {
/* 202 */                 String filename = filenames[j].toLowerCase(Locale.ENGLISH);
/* 203 */                 if (filename.endsWith(".jar"))
/*     */                 {
/* 205 */                   File file = new File(directory, filenames[j]);
/* 206 */                   file = file.getCanonicalFile();
/* 207 */                   if (validateFile(file, RepositoryType.JAR))
/*     */                   {
/*     */ 
/* 210 */                     if (log.isDebugEnabled())
/* 211 */                       log.debug("    Including glob jar file " + file
/* 212 */                         .getAbsolutePath());
/* 213 */                     URL url = buildClassLoaderUrl(file);
/* 214 */                     set.add(url);
/*     */                   }
/*     */                 }
/*     */               } }
/*     */           }
/*     */         }
/*     */     }
/* 221 */     final URL[] array = (URL[])set.toArray(new URL[set.size()]);
/* 222 */     if (log.isDebugEnabled()) {
/* 223 */       for (int i = 0; i < array.length; i++) {
/* 224 */         log.debug("  location " + i + " is " + array[i]);
/*     */       }
/*     */     }
/* 227 */     (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public URLClassLoader run()
/*     */       {
/* 231 */         if (this.val$parent == null) {
/* 232 */           return new URLClassLoader(array);
/*     */         }
/* 234 */         return new URLClassLoader(array, this.val$parent);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   private static boolean validateFile(File file, RepositoryType type) throws IOException
/*     */   {
/* 241 */     if ((RepositoryType.DIR == type) || (RepositoryType.GLOB == type)) {
/* 242 */       if ((!file.isDirectory()) || (!file.canRead()))
/*     */       {
/*     */ 
/*     */ 
/* 246 */         String msg = "Problem with directory [" + file + "], exists: [" + file.exists() + "], isDirectory: [" + file.isDirectory() + "], canRead: [" + file.canRead() + "]";
/*     */         
/* 248 */         File home = new File(Bootstrap.getCatalinaHome());
/* 249 */         home = home.getCanonicalFile();
/* 250 */         File base = new File(Bootstrap.getCatalinaBase());
/* 251 */         base = base.getCanonicalFile();
/* 252 */         File defaultValue = new File(base, "lib");
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 257 */         if ((!home.getPath().equals(base.getPath())) && 
/* 258 */           (file.getPath().equals(defaultValue.getPath())) && 
/* 259 */           (!file.exists())) {
/* 260 */           log.debug(msg);
/*     */         } else {
/* 262 */           log.warn(msg);
/*     */         }
/* 264 */         return false;
/*     */       }
/* 266 */     } else if ((RepositoryType.JAR == type) && 
/* 267 */       (!file.canRead())) {
/* 268 */       log.warn("Problem with JAR file [" + file + "], exists: [" + file
/* 269 */         .exists() + "], canRead: [" + file
/* 270 */         .canRead() + "]");
/* 271 */       return false;
/*     */     }
/*     */     
/* 274 */     return true;
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
/*     */   private static URL buildClassLoaderUrl(String urlString)
/*     */     throws MalformedURLException
/*     */   {
/* 288 */     String result = urlString.replaceAll("!/", "%21/");
/* 289 */     return new URL(result);
/*     */   }
/*     */   
/*     */   private static URL buildClassLoaderUrl(File file)
/*     */     throws MalformedURLException
/*     */   {
/* 295 */     String fileUrlString = file.toURI().toString();
/* 296 */     fileUrlString = fileUrlString.replaceAll("!/", "%21/");
/* 297 */     return new URL(fileUrlString);
/*     */   }
/*     */   
/*     */   public static enum RepositoryType
/*     */   {
/* 302 */     DIR, 
/* 303 */     GLOB, 
/* 304 */     JAR, 
/* 305 */     URL;
/*     */     
/*     */     private RepositoryType() {}
/*     */   }
/*     */   
/*     */   public static class Repository { private final String location;
/*     */     private final ClassLoaderFactory.RepositoryType type;
/*     */     
/* 313 */     public Repository(String location, ClassLoaderFactory.RepositoryType type) { this.location = location;
/* 314 */       this.type = type;
/*     */     }
/*     */     
/*     */     public String getLocation() {
/* 318 */       return this.location;
/*     */     }
/*     */     
/*     */     public ClassLoaderFactory.RepositoryType getType() {
/* 322 */       return this.type;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\ClassLoaderFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */