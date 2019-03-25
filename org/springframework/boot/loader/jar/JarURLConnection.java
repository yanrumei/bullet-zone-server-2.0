/*     */ package org.springframework.boot.loader.jar;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FilePermission;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.net.URLEncoder;
/*     */ import java.net.URLStreamHandler;
/*     */ import java.security.Permission;
/*     */ import org.springframework.boot.loader.data.RandomAccessData;
/*     */ import org.springframework.boot.loader.data.RandomAccessData.ResourceAccess;
/*     */ import org.springframework.boot.loader.data.RandomAccessDataFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class JarURLConnection
/*     */   extends java.net.JarURLConnection
/*     */ {
/*  43 */   private static ThreadLocal<Boolean> useFastExceptions = new ThreadLocal();
/*     */   
/*  45 */   private static final FileNotFoundException FILE_NOT_FOUND_EXCEPTION = new FileNotFoundException("Jar file or entry not found");
/*     */   
/*     */ 
/*  48 */   private static final IllegalStateException NOT_FOUND_CONNECTION_EXCEPTION = new IllegalStateException(FILE_NOT_FOUND_EXCEPTION);
/*     */   
/*     */   private static final String SEPARATOR = "!/";
/*     */   private static final URL EMPTY_JAR_URL;
/*     */   
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  57 */       EMPTY_JAR_URL = new URL("jar:", null, 0, "file:!/", new URLStreamHandler()
/*     */       {
/*     */         protected URLConnection openConnection(URL u)
/*     */           throws IOException
/*     */         {
/*  62 */           return null;
/*     */         }
/*     */       });
/*     */     }
/*     */     catch (MalformedURLException ex) {
/*  67 */       throw new IllegalStateException(ex);
/*     */     }
/*     */   }
/*     */   
/*  71 */   private static final JarEntryName EMPTY_JAR_ENTRY_NAME = new JarEntryName("");
/*     */   
/*     */ 
/*     */   private static final String READ_ACTION = "read";
/*     */   
/*  76 */   private static final JarURLConnection NOT_FOUND_CONNECTION = notFound();
/*     */   
/*     */   private final JarFile jarFile;
/*     */   
/*     */   private Permission permission;
/*     */   
/*     */   private URL jarFileUrl;
/*     */   
/*     */   private final JarEntryName jarEntryName;
/*     */   
/*     */   private JarEntry jarEntry;
/*     */   
/*     */   private JarURLConnection(URL url, JarFile jarFile, JarEntryName jarEntryName)
/*     */     throws IOException
/*     */   {
/*  91 */     super(EMPTY_JAR_URL);
/*  92 */     this.url = url;
/*  93 */     this.jarFile = jarFile;
/*  94 */     this.jarEntryName = jarEntryName;
/*     */   }
/*     */   
/*     */   public void connect() throws IOException
/*     */   {
/*  99 */     if (this.jarFile == null) {
/* 100 */       throw FILE_NOT_FOUND_EXCEPTION;
/*     */     }
/* 102 */     if ((!this.jarEntryName.isEmpty()) && (this.jarEntry == null)) {
/* 103 */       this.jarEntry = this.jarFile.getJarEntry(getEntryName());
/* 104 */       if (this.jarEntry == null) {
/* 105 */         throwFileNotFound(this.jarEntryName, this.jarFile);
/*     */       }
/*     */     }
/* 108 */     this.connected = true;
/*     */   }
/*     */   
/*     */   public JarFile getJarFile() throws IOException
/*     */   {
/* 113 */     connect();
/* 114 */     return this.jarFile;
/*     */   }
/*     */   
/*     */   public URL getJarFileURL()
/*     */   {
/* 119 */     if (this.jarFile == null) {
/* 120 */       throw NOT_FOUND_CONNECTION_EXCEPTION;
/*     */     }
/* 122 */     if (this.jarFileUrl == null) {
/* 123 */       this.jarFileUrl = buildJarFileUrl();
/*     */     }
/* 125 */     return this.jarFileUrl;
/*     */   }
/*     */   
/*     */   private URL buildJarFileUrl() {
/*     */     try {
/* 130 */       String spec = this.jarFile.getUrl().getFile();
/* 131 */       if (spec.endsWith("!/")) {
/* 132 */         spec = spec.substring(0, spec.length() - "!/".length());
/*     */       }
/* 134 */       if (spec.indexOf("!/") == -1) {
/* 135 */         return new URL(spec);
/*     */       }
/* 137 */       return new URL("jar:" + spec);
/*     */     }
/*     */     catch (MalformedURLException ex) {
/* 140 */       throw new IllegalStateException(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   public JarEntry getJarEntry() throws IOException
/*     */   {
/* 146 */     if ((this.jarEntryName == null) || (this.jarEntryName.isEmpty())) {
/* 147 */       return null;
/*     */     }
/* 149 */     connect();
/* 150 */     return this.jarEntry;
/*     */   }
/*     */   
/*     */   public String getEntryName()
/*     */   {
/* 155 */     if (this.jarFile == null) {
/* 156 */       throw NOT_FOUND_CONNECTION_EXCEPTION;
/*     */     }
/* 158 */     return this.jarEntryName.toString();
/*     */   }
/*     */   
/*     */   public InputStream getInputStream() throws IOException
/*     */   {
/* 163 */     if (this.jarFile == null) {
/* 164 */       throw FILE_NOT_FOUND_EXCEPTION;
/*     */     }
/* 166 */     if ((this.jarEntryName.isEmpty()) && 
/* 167 */       (this.jarFile.getType() == JarFile.JarFileType.DIRECT)) {
/* 168 */       throw new IOException("no entry name specified");
/*     */     }
/* 170 */     connect();
/*     */     
/*     */ 
/* 173 */     InputStream inputStream = this.jarEntryName.isEmpty() ? this.jarFile.getData().getInputStream(RandomAccessData.ResourceAccess.ONCE) : this.jarFile.getInputStream(this.jarEntry);
/* 174 */     if (inputStream == null) {
/* 175 */       throwFileNotFound(this.jarEntryName, this.jarFile);
/*     */     }
/* 177 */     return inputStream;
/*     */   }
/*     */   
/*     */   private void throwFileNotFound(Object entry, JarFile jarFile) throws FileNotFoundException
/*     */   {
/* 182 */     if (Boolean.TRUE.equals(useFastExceptions.get())) {
/* 183 */       throw FILE_NOT_FOUND_EXCEPTION;
/*     */     }
/*     */     
/* 186 */     throw new FileNotFoundException("JAR entry " + entry + " not found in " + jarFile.getName());
/*     */   }
/*     */   
/*     */   public int getContentLength()
/*     */   {
/* 191 */     long length = getContentLengthLong();
/* 192 */     if (length > 2147483647L) {
/* 193 */       return -1;
/*     */     }
/* 195 */     return (int)length;
/*     */   }
/*     */   
/*     */   public long getContentLengthLong()
/*     */   {
/* 200 */     if (this.jarFile == null) {
/* 201 */       return -1L;
/*     */     }
/*     */     try {
/* 204 */       if (this.jarEntryName.isEmpty()) {
/* 205 */         return this.jarFile.size();
/*     */       }
/* 207 */       JarEntry entry = getJarEntry();
/* 208 */       return entry == null ? -1 : (int)entry.getSize();
/*     */     }
/*     */     catch (IOException ex) {}
/* 211 */     return -1L;
/*     */   }
/*     */   
/*     */   public Object getContent()
/*     */     throws IOException
/*     */   {
/* 217 */     connect();
/* 218 */     return this.jarEntryName.isEmpty() ? this.jarFile : super.getContent();
/*     */   }
/*     */   
/*     */   public String getContentType()
/*     */   {
/* 223 */     return this.jarEntryName == null ? null : this.jarEntryName.getContentType();
/*     */   }
/*     */   
/*     */   public Permission getPermission() throws IOException
/*     */   {
/* 228 */     if (this.jarFile == null) {
/* 229 */       throw FILE_NOT_FOUND_EXCEPTION;
/*     */     }
/* 231 */     if (this.permission == null)
/*     */     {
/* 233 */       this.permission = new FilePermission(this.jarFile.getRootJarFile().getFile().getPath(), "read");
/*     */     }
/* 235 */     return this.permission;
/*     */   }
/*     */   
/*     */   public long getLastModified()
/*     */   {
/* 240 */     if ((this.jarFile == null) || (this.jarEntryName.isEmpty())) {
/* 241 */       return 0L;
/*     */     }
/*     */     try {
/* 244 */       JarEntry entry = getJarEntry();
/* 245 */       return entry == null ? 0L : entry.getTime();
/*     */     }
/*     */     catch (IOException ex) {}
/* 248 */     return 0L;
/*     */   }
/*     */   
/*     */   static void setUseFastExceptions(boolean useFastExceptions)
/*     */   {
/* 253 */     useFastExceptions.set(Boolean.valueOf(useFastExceptions));
/*     */   }
/*     */   
/*     */   static JarURLConnection get(URL url, JarFile jarFile) throws IOException {
/* 257 */     String spec = extractFullSpec(url, jarFile.getPathFromRoot());
/*     */     
/* 259 */     int index = 0;
/* 260 */     int separator; while ((separator = spec.indexOf("!/", index)) > 0) {
/* 261 */       String entryName = spec.substring(index, separator);
/* 262 */       JarEntry jarEntry = jarFile.getJarEntry(entryName);
/* 263 */       if (jarEntry == null) {
/* 264 */         return notFound(jarFile, JarEntryName.get(entryName));
/*     */       }
/* 266 */       jarFile = jarFile.getNestedJarFile(jarEntry);
/* 267 */       index += separator + "!/".length();
/*     */     }
/* 269 */     JarEntryName jarEntryName = JarEntryName.get(spec, index);
/* 270 */     if ((Boolean.TRUE.equals(useFastExceptions.get())) && 
/* 271 */       (!jarEntryName.isEmpty()) && 
/* 272 */       (!jarFile.containsEntry(jarEntryName.toString()))) {
/* 273 */       return NOT_FOUND_CONNECTION;
/*     */     }
/*     */     
/* 276 */     return new JarURLConnection(url, jarFile, jarEntryName);
/*     */   }
/*     */   
/*     */   private static String extractFullSpec(URL url, String pathFromRoot) {
/* 280 */     String file = url.getFile();
/* 281 */     int separatorIndex = file.indexOf("!/");
/* 282 */     if (separatorIndex < 0) {
/* 283 */       return "";
/*     */     }
/* 285 */     int specIndex = separatorIndex + "!/".length() + pathFromRoot.length();
/* 286 */     return file.substring(specIndex);
/*     */   }
/*     */   
/*     */   private static JarURLConnection notFound() {
/*     */     try {
/* 291 */       return notFound(null, null);
/*     */     }
/*     */     catch (IOException ex) {
/* 294 */       throw new IllegalStateException(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private static JarURLConnection notFound(JarFile jarFile, JarEntryName jarEntryName) throws IOException
/*     */   {
/* 300 */     if (Boolean.TRUE.equals(useFastExceptions.get())) {
/* 301 */       return NOT_FOUND_CONNECTION;
/*     */     }
/* 303 */     return new JarURLConnection(null, jarFile, jarEntryName);
/*     */   }
/*     */   
/*     */ 
/*     */   static class JarEntryName
/*     */   {
/*     */     private final String name;
/*     */     
/*     */     private String contentType;
/*     */     
/*     */ 
/*     */     JarEntryName(String spec)
/*     */     {
/* 316 */       this.name = decode(spec);
/*     */     }
/*     */     
/*     */     private String decode(String source) {
/* 320 */       if ((source.isEmpty()) || (source.indexOf('%') < 0)) {
/* 321 */         return source;
/*     */       }
/* 323 */       ByteArrayOutputStream bos = new ByteArrayOutputStream(source.length());
/* 324 */       write(source, bos);
/*     */       
/* 326 */       return AsciiBytes.toString(bos.toByteArray());
/*     */     }
/*     */     
/*     */     private void write(String source, ByteArrayOutputStream outputStream) {
/* 330 */       int length = source.length();
/* 331 */       for (int i = 0; i < length; i++) {
/* 332 */         int c = source.charAt(i);
/* 333 */         if (c > 127) {
/*     */           try {
/* 335 */             String encoded = URLEncoder.encode(String.valueOf((char)c), "UTF-8");
/*     */             
/* 337 */             write(encoded, outputStream);
/*     */           }
/*     */           catch (UnsupportedEncodingException ex) {
/* 340 */             throw new IllegalStateException(ex);
/*     */           }
/*     */         }
/*     */         else {
/* 344 */           if (c == 37) {
/* 345 */             if (i + 2 >= length)
/*     */             {
/* 347 */               throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
/*     */             }
/*     */             
/* 350 */             c = decodeEscapeSequence(source, i);
/* 351 */             i += 2;
/*     */           }
/* 353 */           outputStream.write(c);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private char decodeEscapeSequence(String source, int i) {
/* 359 */       int hi = Character.digit(source.charAt(i + 1), 16);
/* 360 */       int lo = Character.digit(source.charAt(i + 2), 16);
/* 361 */       if ((hi == -1) || (lo == -1))
/*     */       {
/* 363 */         throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
/*     */       }
/* 365 */       return (char)((hi << 4) + lo);
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 370 */       return this.name;
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 374 */       return this.name.isEmpty();
/*     */     }
/*     */     
/*     */     public String getContentType() {
/* 378 */       if (this.contentType == null) {
/* 379 */         this.contentType = deduceContentType();
/*     */       }
/* 381 */       return this.contentType;
/*     */     }
/*     */     
/*     */     private String deduceContentType()
/*     */     {
/* 386 */       String type = isEmpty() ? "x-java/jar" : null;
/* 387 */       type = type != null ? type : URLConnection.guessContentTypeFromName(toString());
/* 388 */       type = type != null ? type : "content/unknown";
/* 389 */       return type;
/*     */     }
/*     */     
/*     */     public static JarEntryName get(String spec) {
/* 393 */       return get(spec, 0);
/*     */     }
/*     */     
/*     */     public static JarEntryName get(String spec, int beginIndex) {
/* 397 */       if (spec.length() <= beginIndex) {
/* 398 */         return JarURLConnection.EMPTY_JAR_ENTRY_NAME;
/*     */       }
/* 400 */       return new JarEntryName(spec.substring(beginIndex));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\org\springframework\boot\loader\jar\JarURLConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */