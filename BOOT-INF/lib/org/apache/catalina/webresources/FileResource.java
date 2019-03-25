/*     */ package org.apache.catalina.webresources;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.FileTime;
/*     */ import java.security.cert.Certificate;
/*     */ import java.util.jar.Manifest;
/*     */ import org.apache.catalina.WebResource;
/*     */ import org.apache.catalina.WebResourceRoot;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
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
/*     */ public class FileResource
/*     */   extends AbstractResource
/*     */ {
/*  43 */   private static final Log log = LogFactory.getLog(FileResource.class);
/*     */   private static final boolean PROPERTIES_NEED_CONVERT;
/*     */   
/*     */   static {
/*  47 */     boolean isEBCDIC = false;
/*     */     try {
/*  49 */       String encoding = System.getProperty("file.encoding");
/*  50 */       if (encoding.indexOf("EBCDIC") != -1) {
/*  51 */         isEBCDIC = true;
/*     */       }
/*     */     }
/*     */     catch (SecurityException localSecurityException) {}
/*     */     
/*  56 */     PROPERTIES_NEED_CONVERT = isEBCDIC;
/*     */   }
/*     */   
/*     */ 
/*     */   private final File resource;
/*     */   
/*     */   private final String name;
/*     */   private final boolean readOnly;
/*     */   private final Manifest manifest;
/*     */   private final boolean needConvert;
/*     */   public FileResource(WebResourceRoot root, String webAppPath, File resource, boolean readOnly, Manifest manifest)
/*     */   {
/*  68 */     super(root, webAppPath);
/*  69 */     this.resource = resource;
/*     */     
/*  71 */     if (webAppPath.charAt(webAppPath.length() - 1) == '/') {
/*  72 */       String realName = resource.getName() + '/';
/*  73 */       if (webAppPath.endsWith(realName)) {
/*  74 */         this.name = resource.getName();
/*     */       }
/*     */       else
/*     */       {
/*  78 */         int endOfName = webAppPath.length() - 1;
/*  79 */         this.name = webAppPath.substring(webAppPath
/*  80 */           .lastIndexOf('/', endOfName - 1) + 1, endOfName);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*  85 */       this.name = resource.getName();
/*     */     }
/*     */     
/*  88 */     this.readOnly = readOnly;
/*  89 */     this.manifest = manifest;
/*  90 */     this.needConvert = ((PROPERTIES_NEED_CONVERT) && (this.name.endsWith(".properties")));
/*     */   }
/*     */   
/*     */   public long getLastModified()
/*     */   {
/*  95 */     return this.resource.lastModified();
/*     */   }
/*     */   
/*     */   public boolean exists()
/*     */   {
/* 100 */     return this.resource.exists();
/*     */   }
/*     */   
/*     */   public boolean isVirtual()
/*     */   {
/* 105 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isDirectory()
/*     */   {
/* 110 */     return this.resource.isDirectory();
/*     */   }
/*     */   
/*     */   public boolean isFile()
/*     */   {
/* 115 */     return this.resource.isFile();
/*     */   }
/*     */   
/*     */   public boolean delete()
/*     */   {
/* 120 */     if (this.readOnly) {
/* 121 */       return false;
/*     */     }
/* 123 */     return this.resource.delete();
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/* 128 */     return this.name;
/*     */   }
/*     */   
/*     */   public long getContentLength()
/*     */   {
/* 133 */     return getContentLengthInternal(this.needConvert);
/*     */   }
/*     */   
/*     */   private long getContentLengthInternal(boolean convert) {
/* 137 */     if (convert) {
/* 138 */       byte[] content = getContent();
/* 139 */       if (content == null) {
/* 140 */         return -1L;
/*     */       }
/* 142 */       return content.length;
/*     */     }
/*     */     
/*     */ 
/* 146 */     if (isDirectory()) {
/* 147 */       return -1L;
/*     */     }
/*     */     
/* 150 */     return this.resource.length();
/*     */   }
/*     */   
/*     */   public String getCanonicalPath()
/*     */   {
/*     */     try {
/* 156 */       return this.resource.getCanonicalPath();
/*     */     } catch (IOException ioe) {
/* 158 */       if (log.isDebugEnabled())
/* 159 */         log.debug(sm.getString("fileResource.getCanonicalPathFail", new Object[] {this.resource
/* 160 */           .getPath() }), ioe);
/*     */     }
/* 162 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean canRead()
/*     */   {
/* 168 */     return this.resource.canRead();
/*     */   }
/*     */   
/*     */   protected InputStream doGetInputStream()
/*     */   {
/* 173 */     if (this.needConvert) {
/* 174 */       byte[] content = getContent();
/* 175 */       if (content == null) {
/* 176 */         return null;
/*     */       }
/* 178 */       return new ByteArrayInputStream(content);
/*     */     }
/*     */     try
/*     */     {
/* 182 */       return new FileInputStream(this.resource);
/*     */     }
/*     */     catch (FileNotFoundException fnfe) {}
/* 185 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final byte[] getContent()
/*     */   {
/* 192 */     long len = getContentLengthInternal(false);
/*     */     
/* 194 */     if (len > 2147483647L)
/*     */     {
/* 196 */       throw new ArrayIndexOutOfBoundsException(sm.getString("abstractResource.getContentTooLarge", new Object[] {
/* 197 */         getWebappPath(), 
/* 198 */         Long.valueOf(len) }));
/*     */     }
/*     */     
/* 201 */     if (len < 0L)
/*     */     {
/* 203 */       return null;
/*     */     }
/*     */     
/* 206 */     int size = (int)len;
/* 207 */     byte[] result = new byte[size];
/*     */     
/* 209 */     int pos = 0;
/* 210 */     try { InputStream is = new FileInputStream(this.resource);Throwable localThrowable3 = null;
/* 211 */       try { while (pos < size) {
/* 212 */           int n = is.read(result, pos, size - pos);
/* 213 */           if (n < 0) {
/*     */             break;
/*     */           }
/* 216 */           pos += n;
/*     */         }
/*     */       }
/*     */       catch (Throwable localThrowable1)
/*     */       {
/* 210 */         localThrowable3 = localThrowable1;throw localThrowable1;
/*     */ 
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/*     */ 
/*     */ 
/* 218 */         if (is != null) if (localThrowable3 != null) try { is.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else is.close();
/* 219 */       } } catch (IOException ioe) { if (getLog().isDebugEnabled()) {
/* 220 */         getLog().debug(sm.getString("abstractResource.getContentFail", new Object[] {
/* 221 */           getWebappPath() }), ioe);
/*     */       }
/* 223 */       return null;
/*     */     }
/*     */     
/* 226 */     if (this.needConvert)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 231 */       String str = new String(result);
/*     */       try {
/* 233 */         result = str.getBytes(StandardCharsets.UTF_8);
/*     */       } catch (Exception e) {
/* 235 */         result = null;
/*     */       }
/*     */     }
/* 238 */     return result;
/*     */   }
/*     */   
/*     */   public long getCreation()
/*     */   {
/*     */     try
/*     */     {
/* 245 */       BasicFileAttributes attrs = Files.readAttributes(this.resource.toPath(), BasicFileAttributes.class, new LinkOption[0]);
/*     */       
/* 247 */       return attrs.creationTime().toMillis();
/*     */     } catch (IOException e) {
/* 249 */       if (log.isDebugEnabled())
/* 250 */         log.debug(sm.getString("fileResource.getCreationFail", new Object[] {this.resource
/* 251 */           .getPath() }), e);
/*     */     }
/* 253 */     return 0L;
/*     */   }
/*     */   
/*     */ 
/*     */   public URL getURL()
/*     */   {
/* 259 */     if (this.resource.exists()) {
/*     */       try {
/* 261 */         return this.resource.toURI().toURL();
/*     */       } catch (MalformedURLException e) {
/* 263 */         if (log.isDebugEnabled()) {
/* 264 */           log.debug(sm.getString("fileResource.getUrlFail", new Object[] {this.resource
/* 265 */             .getPath() }), e);
/*     */         }
/* 267 */         return null;
/*     */       }
/*     */     }
/* 270 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public URL getCodeBase()
/*     */   {
/* 276 */     if ((getWebappPath().startsWith("/WEB-INF/classes/")) && (this.name.endsWith(".class"))) {
/* 277 */       return getWebResourceRoot().getResource("/WEB-INF/classes/").getURL();
/*     */     }
/* 279 */     return getURL();
/*     */   }
/*     */   
/*     */ 
/*     */   public Certificate[] getCertificates()
/*     */   {
/* 285 */     return null;
/*     */   }
/*     */   
/*     */   public Manifest getManifest()
/*     */   {
/* 290 */     return this.manifest;
/*     */   }
/*     */   
/*     */   protected Log getLog()
/*     */   {
/* 295 */     return log;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\webresources\FileResource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */