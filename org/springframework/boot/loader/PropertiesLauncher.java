/*     */ package org.springframework.boot.loader;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.Manifest;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.springframework.boot.loader.archive.Archive;
/*     */ import org.springframework.boot.loader.archive.Archive.Entry;
/*     */ import org.springframework.boot.loader.archive.Archive.EntryFilter;
/*     */ import org.springframework.boot.loader.archive.ExplodedArchive;
/*     */ import org.springframework.boot.loader.archive.JarFileArchive;
/*     */ import org.springframework.boot.loader.util.SystemPropertyUtils;
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
/*     */ public class PropertiesLauncher
/*     */   extends Launcher
/*     */ {
/*     */   private static final String DEBUG = "loader.debug";
/*     */   public static final String MAIN = "loader.main";
/*     */   public static final String PATH = "loader.path";
/*     */   public static final String HOME = "loader.home";
/*     */   public static final String ARGS = "loader.args";
/*     */   public static final String CONFIG_NAME = "loader.config.name";
/*     */   public static final String CONFIG_LOCATION = "loader.config.location";
/*     */   public static final String SET_SYSTEM_PROPERTIES = "loader.system";
/* 122 */   private static final Pattern WORD_SEPARATOR = Pattern.compile("\\W+");
/*     */   
/*     */   private final File home;
/*     */   
/* 126 */   private List<String> paths = new ArrayList();
/*     */   
/* 128 */   private final Properties properties = new Properties();
/*     */   private Archive parent;
/*     */   
/*     */   public PropertiesLauncher()
/*     */   {
/*     */     try {
/* 134 */       this.home = getHomeDirectory();
/* 135 */       initializeProperties();
/* 136 */       initializePaths();
/* 137 */       this.parent = createArchive();
/*     */     }
/*     */     catch (Exception ex) {
/* 140 */       throw new IllegalStateException(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   protected File getHomeDirectory() {
/*     */     try {
/* 146 */       return new File(getPropertyWithDefault("loader.home", "${user.dir}"));
/*     */     }
/*     */     catch (Exception ex) {
/* 149 */       throw new IllegalStateException(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private void initializeProperties() throws Exception, IOException {
/* 154 */     List<String> configs = new ArrayList();
/* 155 */     String[] names; if (getProperty("loader.config.location") != null) {
/* 156 */       configs.add(getProperty("loader.config.location"));
/*     */     }
/*     */     else
/*     */     {
/* 160 */       names = getPropertyWithDefault("loader.config.name", "loader,application").split(",");
/* 161 */       for (String name : names) {
/* 162 */         configs.add("file:" + getHomeDirectory() + "/" + name + ".properties");
/* 163 */         configs.add("classpath:" + name + ".properties");
/* 164 */         configs.add("classpath:BOOT-INF/classes/" + name + ".properties");
/*     */       }
/*     */     }
/* 167 */     for (String config : configs) {
/* 168 */       InputStream resource = getResource(config);
/* 169 */       if (resource != null) {
/* 170 */         debug("Found: " + config);
/*     */         try {
/* 172 */           this.properties.load(resource);
/*     */         }
/*     */         finally {
/* 175 */           resource.close();
/*     */         }
/* 177 */         for (Object key : Collections.list(this.properties.propertyNames())) {
/* 178 */           if ((config.endsWith("application.properties")) && 
/* 179 */             (((String)key).startsWith("loader."))) {
/* 180 */             warn("Use of application.properties for PropertiesLauncher is deprecated");
/*     */           }
/* 182 */           String text = this.properties.getProperty((String)key);
/*     */           
/* 184 */           String value = SystemPropertyUtils.resolvePlaceholders(this.properties, text);
/* 185 */           if (value != null) {
/* 186 */             this.properties.put(key, value);
/*     */           }
/*     */         }
/* 189 */         if ("true".equals(getProperty("loader.system"))) {
/* 190 */           debug("Adding resolved properties to System properties");
/* 191 */           for (Object key : Collections.list(this.properties.propertyNames())) {
/* 192 */             String value = this.properties.getProperty((String)key);
/* 193 */             System.setProperty((String)key, value);
/*     */           }
/*     */         }
/*     */         
/* 197 */         return;
/*     */       }
/*     */       
/* 200 */       debug("Not found: " + config);
/*     */     }
/*     */   }
/*     */   
/*     */   private InputStream getResource(String config) throws Exception
/*     */   {
/* 206 */     if (config.startsWith("classpath:")) {
/* 207 */       return getClasspathResource(config.substring("classpath:".length()));
/*     */     }
/* 209 */     config = stripFileUrlPrefix(config);
/* 210 */     if (isUrl(config)) {
/* 211 */       return getURLResource(config);
/*     */     }
/* 213 */     return getFileResource(config);
/*     */   }
/*     */   
/*     */   private String stripFileUrlPrefix(String config) {
/* 217 */     if (config.startsWith("file:")) {
/* 218 */       config = config.substring("file:".length());
/* 219 */       if (config.startsWith("//")) {
/* 220 */         config = config.substring(2);
/*     */       }
/*     */     }
/* 223 */     return config;
/*     */   }
/*     */   
/*     */   private boolean isUrl(String config) {
/* 227 */     return config.contains("://");
/*     */   }
/*     */   
/*     */   private InputStream getClasspathResource(String config) {
/* 231 */     while (config.startsWith("/")) {
/* 232 */       config = config.substring(1);
/*     */     }
/* 234 */     config = "/" + config;
/* 235 */     debug("Trying classpath: " + config);
/* 236 */     return getClass().getResourceAsStream(config);
/*     */   }
/*     */   
/*     */   private InputStream getFileResource(String config) throws Exception {
/* 240 */     File file = new File(config);
/* 241 */     debug("Trying file: " + config);
/* 242 */     if (file.canRead()) {
/* 243 */       return new FileInputStream(file);
/*     */     }
/* 245 */     return null;
/*     */   }
/*     */   
/*     */   private InputStream getURLResource(String config) throws Exception {
/* 249 */     URL url = new URL(config);
/* 250 */     if (exists(url)) {
/* 251 */       URLConnection con = url.openConnection();
/*     */       try {
/* 253 */         return con.getInputStream();
/*     */       }
/*     */       catch (IOException ex)
/*     */       {
/* 257 */         if ((con instanceof HttpURLConnection)) {
/* 258 */           ((HttpURLConnection)con).disconnect();
/*     */         }
/* 260 */         throw ex;
/*     */       }
/*     */     }
/* 263 */     return null;
/*     */   }
/*     */   
/*     */   private boolean exists(URL url) throws IOException
/*     */   {
/* 268 */     URLConnection connection = url.openConnection();
/*     */     try {
/* 270 */       connection.setUseCaches(connection
/* 271 */         .getClass().getSimpleName().startsWith("JNLP"));
/* 272 */       HttpURLConnection httpConnection; if ((connection instanceof HttpURLConnection)) {
/* 273 */         httpConnection = (HttpURLConnection)connection;
/* 274 */         httpConnection.setRequestMethod("HEAD");
/* 275 */         int responseCode = httpConnection.getResponseCode();
/* 276 */         boolean bool; if (responseCode == 200) {
/* 277 */           return true;
/*     */         }
/* 279 */         if (responseCode == 404) {
/* 280 */           return false;
/*     */         }
/*     */       }
/* 283 */       return connection.getContentLength() >= 0 ? 1 : 0;
/*     */     }
/*     */     finally {
/* 286 */       if ((connection instanceof HttpURLConnection)) {
/* 287 */         ((HttpURLConnection)connection).disconnect();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void initializePaths() throws Exception {
/* 293 */     String path = getProperty("loader.path");
/* 294 */     if (path != null) {
/* 295 */       this.paths = parsePathsProperty(path);
/*     */     }
/* 297 */     debug("Nested archive paths: " + this.paths);
/*     */   }
/*     */   
/*     */   private List<String> parsePathsProperty(String commaSeparatedPaths) {
/* 301 */     List<String> paths = new ArrayList();
/* 302 */     for (String path : commaSeparatedPaths.split(",")) {
/* 303 */       path = cleanupPath(path);
/*     */       
/* 305 */       path = "".equals(path) ? "/" : path;
/* 306 */       paths.add(path);
/*     */     }
/* 308 */     if (paths.isEmpty()) {
/* 309 */       paths.add("lib");
/*     */     }
/* 311 */     return paths;
/*     */   }
/*     */   
/*     */   protected String[] getArgs(String... args) throws Exception {
/* 315 */     String loaderArgs = getProperty("loader.args");
/* 316 */     if (loaderArgs != null) {
/* 317 */       String[] defaultArgs = loaderArgs.split("\\s+");
/* 318 */       String[] additionalArgs = args;
/* 319 */       args = new String[defaultArgs.length + additionalArgs.length];
/* 320 */       System.arraycopy(defaultArgs, 0, args, 0, defaultArgs.length);
/* 321 */       System.arraycopy(additionalArgs, 0, args, defaultArgs.length, additionalArgs.length);
/*     */     }
/*     */     
/* 324 */     return args;
/*     */   }
/*     */   
/*     */   protected String getMainClass() throws Exception
/*     */   {
/* 329 */     String mainClass = getProperty("loader.main", "Start-Class");
/* 330 */     if (mainClass == null) {
/* 331 */       throw new IllegalStateException("No 'loader.main' or 'Start-Class' specified");
/*     */     }
/*     */     
/* 334 */     return mainClass;
/*     */   }
/*     */   
/*     */   protected ClassLoader createClassLoader(List<Archive> archives) throws Exception
/*     */   {
/* 339 */     Set<URL> urls = new LinkedHashSet(archives.size());
/* 340 */     for (Archive archive : archives) {
/* 341 */       urls.add(archive.getUrl());
/*     */     }
/*     */     
/* 344 */     Object loader = new LaunchedURLClassLoader((URL[])urls.toArray(new URL[0]), getClass().getClassLoader());
/* 345 */     debug("Classpath: " + urls);
/* 346 */     String customLoaderClassName = getProperty("loader.classLoader");
/* 347 */     if (customLoaderClassName != null) {
/* 348 */       loader = wrapWithCustomClassLoader((ClassLoader)loader, customLoaderClassName);
/* 349 */       debug("Using custom class loader: " + customLoaderClassName);
/*     */     }
/* 351 */     return (ClassLoader)loader;
/*     */   }
/*     */   
/*     */ 
/*     */   private ClassLoader wrapWithCustomClassLoader(ClassLoader parent, String loaderClassName)
/*     */     throws Exception
/*     */   {
/* 358 */     Class<ClassLoader> loaderClass = Class.forName(loaderClassName, true, parent);
/*     */     try
/*     */     {
/* 361 */       return (ClassLoader)loaderClass.getConstructor(new Class[] { ClassLoader.class }).newInstance(new Object[] { parent });
/*     */     }
/*     */     catch (NoSuchMethodException localNoSuchMethodException)
/*     */     {
/*     */       try
/*     */       {
/* 367 */         return 
/* 368 */           (ClassLoader)loaderClass.getConstructor(new Class[] { URL[].class, ClassLoader.class }).newInstance(new Object[] { new URL[0], parent });
/*     */       }
/*     */       catch (NoSuchMethodException localNoSuchMethodException1) {}
/*     */     }
/*     */     
/* 373 */     return (ClassLoader)loaderClass.newInstance();
/*     */   }
/*     */   
/*     */   private String getProperty(String propertyKey) throws Exception {
/* 377 */     return getProperty(propertyKey, null, null);
/*     */   }
/*     */   
/*     */   private String getProperty(String propertyKey, String manifestKey) throws Exception {
/* 381 */     return getProperty(propertyKey, manifestKey, null);
/*     */   }
/*     */   
/*     */   private String getPropertyWithDefault(String propertyKey, String defaultValue) throws Exception
/*     */   {
/* 386 */     return getProperty(propertyKey, null, defaultValue);
/*     */   }
/*     */   
/*     */   private String getProperty(String propertyKey, String manifestKey, String defaultValue) throws Exception
/*     */   {
/* 391 */     if (manifestKey == null) {
/* 392 */       manifestKey = propertyKey.replace('.', '-');
/* 393 */       manifestKey = toCamelCase(manifestKey);
/*     */     }
/* 395 */     String property = SystemPropertyUtils.getProperty(propertyKey);
/* 396 */     if (property != null) {
/* 397 */       String value = SystemPropertyUtils.resolvePlaceholders(this.properties, property);
/*     */       
/* 399 */       debug("Property '" + propertyKey + "' from environment: " + value);
/* 400 */       return value;
/*     */     }
/* 402 */     if (this.properties.containsKey(propertyKey)) {
/* 403 */       String value = SystemPropertyUtils.resolvePlaceholders(this.properties, this.properties
/* 404 */         .getProperty(propertyKey));
/* 405 */       debug("Property '" + propertyKey + "' from properties: " + value);
/* 406 */       return value;
/*     */     }
/*     */     try {
/* 409 */       if (this.home != null)
/*     */       {
/* 411 */         Manifest manifest = new ExplodedArchive(this.home, false).getManifest();
/* 412 */         if (manifest != null) {
/* 413 */           String value = manifest.getMainAttributes().getValue(manifestKey);
/* 414 */           if (value != null) {
/* 415 */             debug("Property '" + manifestKey + "' from home directory manifest: " + value);
/*     */             
/* 417 */             return SystemPropertyUtils.resolvePlaceholders(this.properties, value);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (IllegalStateException localIllegalStateException) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 427 */     Manifest manifest = createArchive().getManifest();
/* 428 */     if (manifest != null) {
/* 429 */       String value = manifest.getMainAttributes().getValue(manifestKey);
/* 430 */       if (value != null) {
/* 431 */         debug("Property '" + manifestKey + "' from archive manifest: " + value);
/* 432 */         return SystemPropertyUtils.resolvePlaceholders(this.properties, value);
/*     */       }
/*     */     }
/* 435 */     return defaultValue == null ? defaultValue : 
/* 436 */       SystemPropertyUtils.resolvePlaceholders(this.properties, defaultValue);
/*     */   }
/*     */   
/*     */   protected List<Archive> getClassPathArchives() throws Exception
/*     */   {
/* 441 */     List<Archive> lib = new ArrayList();
/* 442 */     for (String path : this.paths) {
/* 443 */       for (Archive archive : getClassPathArchives(path)) {
/* 444 */         if ((archive instanceof ExplodedArchive))
/*     */         {
/* 446 */           List<Archive> nested = new ArrayList(archive.getNestedArchives(new ArchiveEntryFilter(null)));
/* 447 */           nested.add(0, archive);
/* 448 */           lib.addAll(nested);
/*     */         }
/*     */         else {
/* 451 */           lib.add(archive);
/*     */         }
/*     */       }
/*     */     }
/* 455 */     addNestedEntries(lib);
/* 456 */     return lib;
/*     */   }
/*     */   
/*     */   private List<Archive> getClassPathArchives(String path) throws Exception {
/* 460 */     String root = cleanupPath(stripFileUrlPrefix(path));
/* 461 */     List<Archive> lib = new ArrayList();
/* 462 */     File file = new File(root);
/* 463 */     if (!"/".equals(root)) {
/* 464 */       if (!isAbsolutePath(root)) {
/* 465 */         file = new File(this.home, root);
/*     */       }
/* 467 */       if (file.isDirectory()) {
/* 468 */         debug("Adding classpath entries from " + file);
/* 469 */         Archive archive = new ExplodedArchive(file, false);
/* 470 */         lib.add(archive);
/*     */       }
/*     */     }
/* 473 */     Archive archive = getArchive(file);
/* 474 */     if (archive != null) {
/* 475 */       debug("Adding classpath entries from archive " + archive.getUrl() + root);
/* 476 */       lib.add(archive);
/*     */     }
/* 478 */     List<Archive> nestedArchives = getNestedArchives(root);
/* 479 */     if (nestedArchives != null) {
/* 480 */       debug("Adding classpath entries from nested " + root);
/* 481 */       lib.addAll(nestedArchives);
/*     */     }
/* 483 */     return lib;
/*     */   }
/*     */   
/*     */   private boolean isAbsolutePath(String root)
/*     */   {
/* 488 */     return (root.contains(":")) || (root.startsWith("/"));
/*     */   }
/*     */   
/*     */   private Archive getArchive(File file) throws IOException {
/* 492 */     String name = file.getName().toLowerCase();
/* 493 */     if ((name.endsWith(".jar")) || (name.endsWith(".zip"))) {
/* 494 */       return new JarFileArchive(file);
/*     */     }
/* 496 */     return null;
/*     */   }
/*     */   
/*     */   private List<Archive> getNestedArchives(String path) throws Exception {
/* 500 */     Archive parent = this.parent;
/* 501 */     String root = path;
/* 502 */     if (((!root.equals("/")) && (root.startsWith("/"))) || 
/* 503 */       (parent.getUrl().equals(this.home.toURI().toURL())))
/*     */     {
/* 505 */       return null;
/*     */     }
/* 507 */     if (root.contains("!")) {
/* 508 */       int index = root.indexOf("!");
/* 509 */       File file = new File(this.home, root.substring(0, index));
/* 510 */       if (root.startsWith("jar:file:")) {
/* 511 */         file = new File(root.substring("jar:file:".length(), index));
/*     */       }
/* 513 */       parent = new JarFileArchive(file);
/* 514 */       root = root.substring(index + 1, root.length());
/* 515 */       while (root.startsWith("/")) {
/* 516 */         root = root.substring(1);
/*     */       }
/*     */     }
/* 519 */     if (root.endsWith(".jar")) {
/* 520 */       File file = new File(this.home, root);
/* 521 */       if (file.exists()) {
/* 522 */         parent = new JarFileArchive(file);
/* 523 */         root = "";
/*     */       }
/*     */     }
/* 526 */     if ((root.equals("/")) || (root.equals("./")) || (root.equals(".")))
/*     */     {
/* 528 */       root = "";
/*     */     }
/* 530 */     Archive.EntryFilter filter = new PrefixMatchingArchiveFilter(root, null);
/* 531 */     List<Archive> archives = new ArrayList(parent.getNestedArchives(filter));
/* 532 */     if ((("".equals(root)) || (".".equals(root))) && (!path.endsWith(".jar")) && (parent != this.parent))
/*     */     {
/*     */ 
/*     */ 
/* 536 */       archives.add(parent);
/*     */     }
/* 538 */     return archives;
/*     */   }
/*     */   
/*     */ 
/*     */   private void addNestedEntries(List<Archive> lib)
/*     */   {
/*     */     try
/*     */     {
/* 546 */       lib.addAll(this.parent.getNestedArchives(new Archive.EntryFilter()
/*     */       {
/*     */         public boolean matches(Archive.Entry entry)
/*     */         {
/* 550 */           if (entry.isDirectory()) {
/* 551 */             return entry.getName().equals("BOOT-INF/classes/");
/*     */           }
/* 553 */           return entry.getName().startsWith("BOOT-INF/lib/");
/*     */         }
/*     */       }));
/*     */     }
/*     */     catch (IOException localIOException) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private String cleanupPath(String path)
/*     */   {
/* 564 */     path = path.trim();
/*     */     
/* 566 */     if (path.startsWith("./")) {
/* 567 */       path = path.substring(2);
/*     */     }
/* 569 */     if ((path.toLowerCase().endsWith(".jar")) || (path.toLowerCase().endsWith(".zip"))) {
/* 570 */       return path;
/*     */     }
/* 572 */     if (path.endsWith("/*")) {
/* 573 */       path = path.substring(0, path.length() - 1);
/*     */ 
/*     */ 
/*     */     }
/* 577 */     else if ((!path.endsWith("/")) && (!path.equals("."))) {
/* 578 */       path = path + "/";
/*     */     }
/*     */     
/* 581 */     return path;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/* 585 */     PropertiesLauncher launcher = new PropertiesLauncher();
/* 586 */     args = launcher.getArgs(args);
/* 587 */     launcher.launch(args);
/*     */   }
/*     */   
/*     */   public static String toCamelCase(CharSequence string) {
/* 591 */     if (string == null) {
/* 592 */       return null;
/*     */     }
/* 594 */     StringBuilder builder = new StringBuilder();
/* 595 */     Matcher matcher = WORD_SEPARATOR.matcher(string);
/* 596 */     int pos = 0;
/* 597 */     while (matcher.find()) {
/* 598 */       builder.append(capitalize(string.subSequence(pos, matcher.end()).toString()));
/* 599 */       pos = matcher.end();
/*     */     }
/* 601 */     builder.append(capitalize(string.subSequence(pos, string.length()).toString()));
/* 602 */     return builder.toString();
/*     */   }
/*     */   
/*     */   private static String capitalize(String str) {
/* 606 */     return Character.toUpperCase(str.charAt(0)) + str.substring(1);
/*     */   }
/*     */   
/*     */   private void debug(String message) {
/* 610 */     if (Boolean.getBoolean("loader.debug")) {
/* 611 */       log(message);
/*     */     }
/*     */   }
/*     */   
/*     */   private void warn(String message) {
/* 616 */     log("WARNING: " + message);
/*     */   }
/*     */   
/*     */   private void log(String message)
/*     */   {
/* 621 */     System.out.println(message);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static final class PrefixMatchingArchiveFilter
/*     */     implements Archive.EntryFilter
/*     */   {
/*     */     private final String prefix;
/*     */     
/*     */ 
/* 632 */     private final PropertiesLauncher.ArchiveEntryFilter filter = new PropertiesLauncher.ArchiveEntryFilter(null);
/*     */     
/*     */     private PrefixMatchingArchiveFilter(String prefix) {
/* 635 */       this.prefix = prefix;
/*     */     }
/*     */     
/*     */     public boolean matches(Archive.Entry entry)
/*     */     {
/* 640 */       if (entry.isDirectory()) {
/* 641 */         return entry.getName().equals(this.prefix);
/*     */       }
/* 643 */       return (entry.getName().startsWith(this.prefix)) && (this.filter.matches(entry));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static final class ArchiveEntryFilter
/*     */     implements Archive.EntryFilter
/*     */   {
/*     */     private static final String DOT_JAR = ".jar";
/*     */     
/*     */ 
/*     */     private static final String DOT_ZIP = ".zip";
/*     */     
/*     */ 
/*     */     public boolean matches(Archive.Entry entry)
/*     */     {
/* 660 */       return (entry.getName().endsWith(".jar")) || (entry.getName().endsWith(".zip"));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\org\springframework\boot\loader\PropertiesLauncher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */