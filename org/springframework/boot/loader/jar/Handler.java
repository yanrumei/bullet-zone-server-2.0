/*     */ package org.springframework.boot.loader.jar;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.net.URLDecoder;
/*     */ import java.net.URLStreamHandler;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Handler
/*     */   extends URLStreamHandler
/*     */ {
/*     */   private static final String JAR_PROTOCOL = "jar:";
/*     */   private static final String FILE_PROTOCOL = "file:";
/*     */   private static final String SEPARATOR = "!/";
/*     */   private static final String CURRENT_DIR = "/./";
/*  54 */   private static final Pattern CURRENT_DIR_PATTERN = Pattern.compile("/./");
/*     */   
/*     */   private static final String PARENT_DIR = "/../";
/*     */   
/*  58 */   private static final String[] FALLBACK_HANDLERS = { "sun.net.www.protocol.jar.Handler" };
/*     */   
/*     */   private static final Method OPEN_CONNECTION_METHOD;
/*     */   
/*     */   static
/*     */   {
/*  64 */     Method method = null;
/*     */     try {
/*  66 */       method = URLStreamHandler.class.getDeclaredMethod("openConnection", new Class[] { URL.class });
/*     */     }
/*     */     catch (Exception localException) {}
/*     */     
/*     */ 
/*     */ 
/*  72 */     OPEN_CONNECTION_METHOD = method;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  78 */   private static SoftReference<Map<File, JarFile>> rootFileCache = new SoftReference(null);
/*     */   
/*     */   private final JarFile jarFile;
/*     */   
/*     */   private URLStreamHandler fallbackHandler;
/*     */   
/*     */   public Handler()
/*     */   {
/*  86 */     this(null);
/*     */   }
/*     */   
/*     */   public Handler(JarFile jarFile) {
/*  90 */     this.jarFile = jarFile;
/*     */   }
/*     */   
/*     */   protected URLConnection openConnection(URL url) throws IOException
/*     */   {
/*  95 */     if (this.jarFile != null) {
/*  96 */       return JarURLConnection.get(url, this.jarFile);
/*     */     }
/*     */     try {
/*  99 */       return JarURLConnection.get(url, getRootJarFileFromUrl(url));
/*     */     }
/*     */     catch (Exception ex) {
/* 102 */       return openFallbackConnection(url, ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private URLConnection openFallbackConnection(URL url, Exception reason) throws IOException
/*     */   {
/*     */     try {
/* 109 */       return openConnection(getFallbackHandler(), url);
/*     */     }
/*     */     catch (Exception ex) {
/* 112 */       if ((reason instanceof IOException)) {
/* 113 */         log(false, "Unable to open fallback handler", ex);
/* 114 */         throw ((IOException)reason);
/*     */       }
/* 116 */       log(true, "Unable to open fallback handler", ex);
/* 117 */       if ((reason instanceof RuntimeException)) {
/* 118 */         throw ((RuntimeException)reason);
/*     */       }
/* 120 */       throw new IllegalStateException(reason);
/*     */     }
/*     */   }
/*     */   
/*     */   private void log(boolean warning, String message, Exception cause)
/*     */   {
/*     */     try {
/* 127 */       Logger.getLogger(getClass().getName()).log(warning ? Level.WARNING : Level.FINEST, message, cause);
/*     */     }
/*     */     catch (Exception ex) {
/* 130 */       if (warning) {
/* 131 */         System.err.println("WARNING: " + message);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private URLStreamHandler getFallbackHandler() {
/* 137 */     if (this.fallbackHandler != null) {
/* 138 */       return this.fallbackHandler;
/*     */     }
/* 140 */     for (String handlerClassName : FALLBACK_HANDLERS) {
/*     */       try {
/* 142 */         Class<?> handlerClass = Class.forName(handlerClassName);
/* 143 */         this.fallbackHandler = ((URLStreamHandler)handlerClass.newInstance());
/* 144 */         return this.fallbackHandler;
/*     */       }
/*     */       catch (Exception localException) {}
/*     */     }
/*     */     
/*     */ 
/* 150 */     throw new IllegalStateException("Unable to find fallback handler");
/*     */   }
/*     */   
/*     */   private URLConnection openConnection(URLStreamHandler handler, URL url) throws Exception
/*     */   {
/* 155 */     if (OPEN_CONNECTION_METHOD == null) {
/* 156 */       throw new IllegalStateException("Unable to invoke fallback open connection method");
/*     */     }
/*     */     
/* 159 */     OPEN_CONNECTION_METHOD.setAccessible(true);
/* 160 */     return (URLConnection)OPEN_CONNECTION_METHOD.invoke(handler, new Object[] { url });
/*     */   }
/*     */   
/*     */   protected void parseURL(URL context, String spec, int start, int limit)
/*     */   {
/* 165 */     if (spec.regionMatches(true, 0, "jar:", 0, "jar:".length())) {
/* 166 */       setFile(context, getFileFromSpec(spec.substring(start, limit)));
/*     */     }
/*     */     else {
/* 169 */       setFile(context, getFileFromContext(context, spec.substring(start, limit)));
/*     */     }
/*     */   }
/*     */   
/*     */   private String getFileFromSpec(String spec) {
/* 174 */     int separatorIndex = spec.lastIndexOf("!/");
/* 175 */     if (separatorIndex == -1) {
/* 176 */       throw new IllegalArgumentException("No !/ in spec '" + spec + "'");
/*     */     }
/*     */     try {
/* 179 */       new URL(spec.substring(0, separatorIndex));
/* 180 */       return spec;
/*     */     }
/*     */     catch (MalformedURLException ex) {
/* 183 */       throw new IllegalArgumentException("Invalid spec URL '" + spec + "'", ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private String getFileFromContext(URL context, String spec) {
/* 188 */     String file = context.getFile();
/* 189 */     if (spec.startsWith("/")) {
/* 190 */       return trimToJarRoot(file) + "!/" + spec.substring(1);
/*     */     }
/* 192 */     if (file.endsWith("/")) {
/* 193 */       return file + spec;
/*     */     }
/* 195 */     int lastSlashIndex = file.lastIndexOf('/');
/* 196 */     if (lastSlashIndex == -1) {
/* 197 */       throw new IllegalArgumentException("No / found in context URL's file '" + file + "'");
/*     */     }
/*     */     
/* 200 */     return file.substring(0, lastSlashIndex + 1) + spec;
/*     */   }
/*     */   
/*     */   private String trimToJarRoot(String file) {
/* 204 */     int lastSeparatorIndex = file.lastIndexOf("!/");
/* 205 */     if (lastSeparatorIndex == -1) {
/* 206 */       throw new IllegalArgumentException("No !/ found in context URL's file '" + file + "'");
/*     */     }
/*     */     
/* 209 */     return file.substring(0, lastSeparatorIndex);
/*     */   }
/*     */   
/*     */   private void setFile(URL context, String file) {
/* 213 */     setURL(context, "jar:", null, -1, null, null, normalize(file), null, null);
/*     */   }
/*     */   
/*     */   private String normalize(String file) {
/* 217 */     if ((!file.contains("/./")) && (!file.contains("/../"))) {
/* 218 */       return file;
/*     */     }
/* 220 */     int afterLastSeparatorIndex = file.lastIndexOf("!/") + "!/".length();
/* 221 */     String afterSeparator = file.substring(afterLastSeparatorIndex);
/* 222 */     afterSeparator = replaceParentDir(afterSeparator);
/* 223 */     afterSeparator = replaceCurrentDir(afterSeparator);
/* 224 */     return file.substring(0, afterLastSeparatorIndex) + afterSeparator;
/*     */   }
/*     */   
/*     */   private String replaceParentDir(String file) {
/*     */     int parentDirIndex;
/* 229 */     while ((parentDirIndex = file.indexOf("/../")) >= 0) {
/* 230 */       int precedingSlashIndex = file.lastIndexOf('/', parentDirIndex - 1);
/* 231 */       if (precedingSlashIndex >= 0)
/*     */       {
/* 233 */         file = file.substring(0, precedingSlashIndex) + file.substring(parentDirIndex + 3);
/*     */       }
/*     */       else {
/* 236 */         file = file.substring(parentDirIndex + 4);
/*     */       }
/*     */     }
/* 239 */     return file;
/*     */   }
/*     */   
/*     */   private String replaceCurrentDir(String file) {
/* 243 */     return CURRENT_DIR_PATTERN.matcher(file).replaceAll("/");
/*     */   }
/*     */   
/*     */   protected int hashCode(URL u)
/*     */   {
/* 248 */     return hashCode(u.getProtocol(), u.getFile());
/*     */   }
/*     */   
/*     */   private int hashCode(String protocol, String file) {
/* 252 */     int result = protocol == null ? 0 : protocol.hashCode();
/* 253 */     int separatorIndex = file.indexOf("!/");
/* 254 */     if (separatorIndex == -1) {
/* 255 */       return result + file.hashCode();
/*     */     }
/* 257 */     String source = file.substring(0, separatorIndex);
/* 258 */     String entry = canonicalize(file.substring(separatorIndex + 2));
/*     */     try {
/* 260 */       result += new URL(source).hashCode();
/*     */     }
/*     */     catch (MalformedURLException ex) {
/* 263 */       result += source.hashCode();
/*     */     }
/* 265 */     result += entry.hashCode();
/* 266 */     return result;
/*     */   }
/*     */   
/*     */   protected boolean sameFile(URL u1, URL u2)
/*     */   {
/* 271 */     if ((!u1.getProtocol().equals("jar")) || (!u2.getProtocol().equals("jar"))) {
/* 272 */       return false;
/*     */     }
/* 274 */     int separator1 = u1.getFile().indexOf("!/");
/* 275 */     int separator2 = u2.getFile().indexOf("!/");
/* 276 */     if ((separator1 == -1) || (separator2 == -1)) {
/* 277 */       return super.sameFile(u1, u2);
/*     */     }
/* 279 */     String nested1 = u1.getFile().substring(separator1 + "!/".length());
/* 280 */     String nested2 = u2.getFile().substring(separator2 + "!/".length());
/* 281 */     if (!nested1.equals(nested2)) {
/* 282 */       String canonical1 = canonicalize(nested1);
/* 283 */       String canonical2 = canonicalize(nested2);
/* 284 */       if (!canonical1.equals(canonical2)) {
/* 285 */         return false;
/*     */       }
/*     */     }
/* 288 */     String root1 = u1.getFile().substring(0, separator1);
/* 289 */     String root2 = u2.getFile().substring(0, separator2);
/*     */     try {
/* 291 */       return super.sameFile(new URL(root1), new URL(root2));
/*     */     }
/*     */     catch (MalformedURLException localMalformedURLException) {}
/*     */     
/*     */ 
/* 296 */     return super.sameFile(u1, u2);
/*     */   }
/*     */   
/*     */   private String canonicalize(String path) {
/* 300 */     return path.replace("!/", "/");
/*     */   }
/*     */   
/*     */   public JarFile getRootJarFileFromUrl(URL url) throws IOException {
/* 304 */     String spec = url.getFile();
/* 305 */     int separatorIndex = spec.indexOf("!/");
/* 306 */     if (separatorIndex == -1) {
/* 307 */       throw new MalformedURLException("Jar URL does not contain !/ separator");
/*     */     }
/* 309 */     String name = spec.substring(0, separatorIndex);
/* 310 */     return getRootJarFile(name);
/*     */   }
/*     */   
/*     */   private JarFile getRootJarFile(String name) throws IOException {
/*     */     try {
/* 315 */       if (!name.startsWith("file:")) {
/* 316 */         throw new IllegalStateException("Not a file URL");
/*     */       }
/* 318 */       String path = name.substring("file:".length());
/* 319 */       File file = new File(URLDecoder.decode(path, "UTF-8"));
/* 320 */       Map<File, JarFile> cache = (Map)rootFileCache.get();
/* 321 */       JarFile result = cache == null ? null : (JarFile)cache.get(file);
/* 322 */       if (result == null) {
/* 323 */         result = new JarFile(file);
/* 324 */         addToRootFileCache(file, result);
/*     */       }
/* 326 */       return result;
/*     */     }
/*     */     catch (Exception ex) {
/* 329 */       throw new IOException("Unable to open root Jar file '" + name + "'", ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static void addToRootFileCache(File sourceFile, JarFile jarFile)
/*     */   {
/* 339 */     Map<File, JarFile> cache = (Map)rootFileCache.get();
/* 340 */     if (cache == null) {
/* 341 */       cache = new ConcurrentHashMap();
/* 342 */       rootFileCache = new SoftReference(cache);
/*     */     }
/* 344 */     cache.put(sourceFile, jarFile);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setUseFastConnectionExceptions(boolean useFastConnectionExceptions)
/*     */   {
/* 355 */     JarURLConnection.setUseFastExceptions(useFastConnectionExceptions);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\org\springframework\boot\loader\jar\Handler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */