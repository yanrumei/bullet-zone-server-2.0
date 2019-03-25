/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.CharMatcher;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.FluentIterable;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.ImmutableSet.Builder;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.MultimapBuilder;
/*     */ import com.google.common.collect.MultimapBuilder.MultimapBuilderWithKeys;
/*     */ import com.google.common.collect.MultimapBuilder.SetMultimapBuilder;
/*     */ import com.google.common.collect.SetMultimap;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.common.io.ByteSource;
/*     */ import com.google.common.io.CharSource;
/*     */ import com.google.common.io.Resources;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.Attributes.Name;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.Manifest;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nullable;
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
/*     */ @Beta
/*     */ public final class ClassPath
/*     */ {
/*  70 */   private static final Logger logger = Logger.getLogger(ClassPath.class.getName());
/*     */   
/*  72 */   private static final Predicate<ClassInfo> IS_TOP_LEVEL = new Predicate()
/*     */   {
/*     */     public boolean apply(ClassPath.ClassInfo info)
/*     */     {
/*  76 */       return info.className.indexOf('$') == -1;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*  82 */   private static final Splitter CLASS_PATH_ATTRIBUTE_SEPARATOR = Splitter.on(" ").omitEmptyStrings();
/*     */   
/*     */   private static final String CLASS_FILE_NAME_EXTENSION = ".class";
/*     */   private final ImmutableSet<ResourceInfo> resources;
/*     */   
/*     */   private ClassPath(ImmutableSet<ResourceInfo> resources)
/*     */   {
/*  89 */     this.resources = resources;
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
/*     */   public static ClassPath from(ClassLoader classloader)
/*     */     throws IOException
/*     */   {
/* 103 */     DefaultScanner scanner = new DefaultScanner();
/* 104 */     scanner.scan(classloader);
/* 105 */     return new ClassPath(scanner.getResources());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ImmutableSet<ResourceInfo> getResources()
/*     */   {
/* 113 */     return this.resources;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ImmutableSet<ClassInfo> getAllClasses()
/*     */   {
/* 122 */     return FluentIterable.from(this.resources).filter(ClassInfo.class).toSet();
/*     */   }
/*     */   
/*     */   public ImmutableSet<ClassInfo> getTopLevelClasses()
/*     */   {
/* 127 */     return FluentIterable.from(this.resources).filter(ClassInfo.class).filter(IS_TOP_LEVEL).toSet();
/*     */   }
/*     */   
/*     */   public ImmutableSet<ClassInfo> getTopLevelClasses(String packageName)
/*     */   {
/* 132 */     Preconditions.checkNotNull(packageName);
/* 133 */     ImmutableSet.Builder<ClassInfo> builder = ImmutableSet.builder();
/* 134 */     for (UnmodifiableIterator localUnmodifiableIterator = getTopLevelClasses().iterator(); localUnmodifiableIterator.hasNext();) { ClassInfo classInfo = (ClassInfo)localUnmodifiableIterator.next();
/* 135 */       if (classInfo.getPackageName().equals(packageName)) {
/* 136 */         builder.add(classInfo);
/*     */       }
/*     */     }
/* 139 */     return builder.build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ImmutableSet<ClassInfo> getTopLevelClassesRecursive(String packageName)
/*     */   {
/* 147 */     Preconditions.checkNotNull(packageName);
/* 148 */     String packagePrefix = packageName + '.';
/* 149 */     ImmutableSet.Builder<ClassInfo> builder = ImmutableSet.builder();
/* 150 */     for (UnmodifiableIterator localUnmodifiableIterator = getTopLevelClasses().iterator(); localUnmodifiableIterator.hasNext();) { ClassInfo classInfo = (ClassInfo)localUnmodifiableIterator.next();
/* 151 */       if (classInfo.getName().startsWith(packagePrefix)) {
/* 152 */         builder.add(classInfo);
/*     */       }
/*     */     }
/* 155 */     return builder.build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public static class ResourceInfo
/*     */   {
/*     */     private final String resourceName;
/*     */     
/*     */ 
/*     */     final ClassLoader loader;
/*     */     
/*     */ 
/*     */     static ResourceInfo of(String resourceName, ClassLoader loader)
/*     */     {
/* 171 */       if (resourceName.endsWith(".class")) {
/* 172 */         return new ClassPath.ClassInfo(resourceName, loader);
/*     */       }
/* 174 */       return new ResourceInfo(resourceName, loader);
/*     */     }
/*     */     
/*     */     ResourceInfo(String resourceName, ClassLoader loader)
/*     */     {
/* 179 */       this.resourceName = ((String)Preconditions.checkNotNull(resourceName));
/* 180 */       this.loader = ((ClassLoader)Preconditions.checkNotNull(loader));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final URL url()
/*     */     {
/* 192 */       URL url = this.loader.getResource(this.resourceName);
/* 193 */       if (url == null) {
/* 194 */         throw new NoSuchElementException(this.resourceName);
/*     */       }
/* 196 */       return url;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final ByteSource asByteSource()
/*     */     {
/* 207 */       return Resources.asByteSource(url());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final CharSource asCharSource(Charset charset)
/*     */     {
/* 219 */       return Resources.asCharSource(url(), charset);
/*     */     }
/*     */     
/*     */     public final String getResourceName()
/*     */     {
/* 224 */       return this.resourceName;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 229 */       return this.resourceName.hashCode();
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/* 234 */       if ((obj instanceof ResourceInfo)) {
/* 235 */         ResourceInfo that = (ResourceInfo)obj;
/* 236 */         return (this.resourceName.equals(that.resourceName)) && (this.loader == that.loader);
/*     */       }
/* 238 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 244 */       return this.resourceName;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @Beta
/*     */   public static final class ClassInfo
/*     */     extends ClassPath.ResourceInfo
/*     */   {
/*     */     private final String className;
/*     */     
/*     */ 
/*     */     ClassInfo(String resourceName, ClassLoader loader)
/*     */     {
/* 258 */       super(loader);
/* 259 */       this.className = ClassPath.getClassName(resourceName);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getPackageName()
/*     */     {
/* 269 */       return Reflection.getPackageName(this.className);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getSimpleName()
/*     */     {
/* 279 */       int lastDollarSign = this.className.lastIndexOf('$');
/* 280 */       if (lastDollarSign != -1) {
/* 281 */         String innerClassName = this.className.substring(lastDollarSign + 1);
/*     */         
/*     */ 
/* 284 */         return CharMatcher.digit().trimLeadingFrom(innerClassName);
/*     */       }
/* 286 */       String packageName = getPackageName();
/* 287 */       if (packageName.isEmpty()) {
/* 288 */         return this.className;
/*     */       }
/*     */       
/*     */ 
/* 292 */       return this.className.substring(packageName.length() + 1);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getName()
/*     */     {
/* 302 */       return this.className;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Class<?> load()
/*     */     {
/*     */       try
/*     */       {
/* 313 */         return this.loader.loadClass(this.className);
/*     */       }
/*     */       catch (ClassNotFoundException e) {
/* 316 */         throw new IllegalStateException(e);
/*     */       }
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 322 */       return this.className;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static abstract class Scanner
/*     */   {
/* 335 */     private final Set<File> scannedUris = Sets.newHashSet();
/*     */     
/*     */     public final void scan(ClassLoader classloader) throws IOException {
/* 338 */       for (UnmodifiableIterator localUnmodifiableIterator = getClassPathEntries(classloader).entrySet().iterator(); localUnmodifiableIterator.hasNext();) { Map.Entry<File, ClassLoader> entry = (Map.Entry)localUnmodifiableIterator.next();
/* 339 */         scan((File)entry.getKey(), (ClassLoader)entry.getValue());
/*     */       }
/*     */     }
/*     */     
/*     */     protected abstract void scanDirectory(ClassLoader paramClassLoader, File paramFile)
/*     */       throws IOException;
/*     */     
/*     */     protected abstract void scanJarFile(ClassLoader paramClassLoader, JarFile paramJarFile) throws IOException;
/*     */     
/*     */     @VisibleForTesting
/*     */     final void scan(File file, ClassLoader classloader) throws IOException
/*     */     {
/* 351 */       if (this.scannedUris.add(file.getCanonicalFile())) {
/* 352 */         scanFrom(file, classloader);
/*     */       }
/*     */     }
/*     */     
/*     */     private void scanFrom(File file, ClassLoader classloader) throws IOException {
/*     */       try {
/* 358 */         if (!file.exists()) {
/* 359 */           return;
/*     */         }
/*     */       } catch (SecurityException e) {
/* 362 */         ClassPath.logger.warning("Cannot access " + file + ": " + e);
/*     */         
/* 364 */         return;
/*     */       }
/* 366 */       if (file.isDirectory()) {
/* 367 */         scanDirectory(classloader, file);
/*     */       } else {
/* 369 */         scanJar(file, classloader);
/*     */       }
/*     */     }
/*     */     
/*     */     private void scanJar(File file, ClassLoader classloader) throws IOException
/*     */     {
/*     */       try {
/* 376 */         jarFile = new JarFile(file);
/*     */       } catch (IOException e) {
/*     */         JarFile jarFile;
/* 379 */         return;
/*     */       }
/*     */       try { JarFile jarFile;
/* 382 */         for (e = getClassPathFromManifest(file, jarFile.getManifest()).iterator(); e.hasNext();) { File path = (File)e.next();
/* 383 */           scan(path, classloader);
/*     */         }
/* 385 */         scanJarFile(classloader, jarFile); return;
/*     */       } finally {
/*     */         try {
/* 388 */           jarFile.close();
/*     */         }
/*     */         catch (IOException localIOException2) {}
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @VisibleForTesting
/*     */     static ImmutableSet<File> getClassPathFromManifest(File jarFile, @Nullable Manifest manifest)
/*     */     {
/* 403 */       if (manifest == null) {
/* 404 */         return ImmutableSet.of();
/*     */       }
/* 406 */       ImmutableSet.Builder<File> builder = ImmutableSet.builder();
/*     */       
/* 408 */       String classpathAttribute = manifest.getMainAttributes().getValue(Attributes.Name.CLASS_PATH.toString());
/* 409 */       if (classpathAttribute != null) {
/* 410 */         for (String path : ClassPath.CLASS_PATH_ATTRIBUTE_SEPARATOR.split(classpathAttribute))
/*     */         {
/*     */           try {
/* 413 */             url = getClassPathEntry(jarFile, path);
/*     */           } catch (MalformedURLException e) {
/*     */             URL url;
/* 416 */             ClassPath.logger.warning("Invalid Class-Path entry: " + path); }
/* 417 */           continue;
/*     */           URL url;
/* 419 */           if (url.getProtocol().equals("file")) {
/* 420 */             builder.add(ClassPath.toFile(url));
/*     */           }
/*     */         }
/*     */       }
/* 424 */       return builder.build();
/*     */     }
/*     */     
/*     */     @VisibleForTesting
/*     */     static ImmutableMap<File, ClassLoader> getClassPathEntries(ClassLoader classloader) {
/* 429 */       LinkedHashMap<File, ClassLoader> entries = Maps.newLinkedHashMap();
/*     */       
/* 431 */       ClassLoader parent = classloader.getParent();
/* 432 */       if (parent != null) {
/* 433 */         entries.putAll(getClassPathEntries(parent));
/*     */       }
/* 435 */       if ((classloader instanceof URLClassLoader)) {
/* 436 */         URLClassLoader urlClassLoader = (URLClassLoader)classloader;
/* 437 */         for (URL entry : urlClassLoader.getURLs()) {
/* 438 */           if (entry.getProtocol().equals("file")) {
/* 439 */             File file = ClassPath.toFile(entry);
/* 440 */             if (!entries.containsKey(file)) {
/* 441 */               entries.put(file, classloader);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 446 */       return ImmutableMap.copyOf(entries);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @VisibleForTesting
/*     */     static URL getClassPathEntry(File jarFile, String path)
/*     */       throws MalformedURLException
/*     */     {
/* 457 */       return new URL(jarFile.toURI().toURL(), path);
/*     */     }
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static final class DefaultScanner extends ClassPath.Scanner
/*     */   {
/* 464 */     private final SetMultimap<ClassLoader, String> resources = MultimapBuilder.hashKeys().linkedHashSetValues().build();
/*     */     
/*     */     ImmutableSet<ClassPath.ResourceInfo> getResources() {
/* 467 */       ImmutableSet.Builder<ClassPath.ResourceInfo> builder = ImmutableSet.builder();
/* 468 */       for (Map.Entry<ClassLoader, String> entry : this.resources.entries()) {
/* 469 */         builder.add(ClassPath.ResourceInfo.of((String)entry.getValue(), (ClassLoader)entry.getKey()));
/*     */       }
/* 471 */       return builder.build();
/*     */     }
/*     */     
/*     */     protected void scanJarFile(ClassLoader classloader, JarFile file)
/*     */     {
/* 476 */       Enumeration<JarEntry> entries = file.entries();
/* 477 */       while (entries.hasMoreElements()) {
/* 478 */         JarEntry entry = (JarEntry)entries.nextElement();
/* 479 */         if ((!entry.isDirectory()) && (!entry.getName().equals("META-INF/MANIFEST.MF")))
/*     */         {
/*     */ 
/* 482 */           this.resources.get(classloader).add(entry.getName());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     protected void scanDirectory(ClassLoader classloader, File directory) throws IOException {
/* 488 */       Set<File> currentPath = new HashSet();
/* 489 */       currentPath.add(directory.getCanonicalFile());
/* 490 */       scanDirectory(directory, classloader, "", currentPath);
/*     */     }
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
/*     */     private void scanDirectory(File directory, ClassLoader classloader, String packagePrefix, Set<File> currentPath)
/*     */       throws IOException
/*     */     {
/* 508 */       File[] files = directory.listFiles();
/* 509 */       if (files == null) {
/* 510 */         ClassPath.logger.warning("Cannot read directory " + directory);
/*     */         
/* 512 */         return;
/*     */       }
/* 514 */       for (File f : files) {
/* 515 */         String name = f.getName();
/* 516 */         if (f.isDirectory()) {
/* 517 */           File deref = f.getCanonicalFile();
/* 518 */           if (currentPath.add(deref)) {
/* 519 */             scanDirectory(deref, classloader, packagePrefix + name + "/", currentPath);
/* 520 */             currentPath.remove(deref);
/*     */           }
/*     */         } else {
/* 523 */           String resourceName = packagePrefix + name;
/* 524 */           if (!resourceName.equals("META-INF/MANIFEST.MF")) {
/* 525 */             this.resources.get(classloader).add(resourceName);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static String getClassName(String filename) {
/* 534 */     int classNameEnd = filename.length() - ".class".length();
/* 535 */     return filename.substring(0, classNameEnd).replace('/', '.');
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static File toFile(URL url)
/*     */   {
/* 541 */     Preconditions.checkArgument(url.getProtocol().equals("file"));
/*     */     try {
/* 543 */       return new File(url.toURI());
/*     */     } catch (URISyntaxException e) {}
/* 545 */     return new File(url.getPath());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\reflect\ClassPath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */