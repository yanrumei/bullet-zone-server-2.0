/*     */ package org.apache.catalina.webresources;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.security.cert.Certificate;
/*     */ import java.util.jar.Manifest;
/*     */ import org.apache.catalina.WebResource;
/*     */ import org.apache.catalina.WebResourceRoot;
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
/*     */ public class EmptyResource
/*     */   implements WebResource
/*     */ {
/*     */   private final WebResourceRoot root;
/*     */   private final String webAppPath;
/*     */   private final File file;
/*     */   
/*     */   public EmptyResource(WebResourceRoot root, String webAppPath)
/*     */   {
/*  36 */     this(root, webAppPath, null);
/*     */   }
/*     */   
/*     */   public EmptyResource(WebResourceRoot root, String webAppPath, File file) {
/*  40 */     this.root = root;
/*  41 */     this.webAppPath = webAppPath;
/*  42 */     this.file = file;
/*     */   }
/*     */   
/*     */   public long getLastModified()
/*     */   {
/*  47 */     return 0L;
/*     */   }
/*     */   
/*     */   public String getLastModifiedHttp()
/*     */   {
/*  52 */     return null;
/*     */   }
/*     */   
/*     */   public boolean exists()
/*     */   {
/*  57 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isVirtual()
/*     */   {
/*  62 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isDirectory()
/*     */   {
/*  67 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isFile()
/*     */   {
/*  72 */     return false;
/*     */   }
/*     */   
/*     */   public boolean delete()
/*     */   {
/*  77 */     return false;
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/*  82 */     int index = this.webAppPath.lastIndexOf('/');
/*  83 */     if (index == -1) {
/*  84 */       return this.webAppPath;
/*     */     }
/*  86 */     return this.webAppPath.substring(index + 1);
/*     */   }
/*     */   
/*     */ 
/*     */   public long getContentLength()
/*     */   {
/*  92 */     return -1L;
/*     */   }
/*     */   
/*     */   public String getCanonicalPath()
/*     */   {
/*  97 */     if (this.file == null) {
/*  98 */       return null;
/*     */     }
/*     */     try {
/* 101 */       return this.file.getCanonicalPath();
/*     */     } catch (IOException e) {}
/* 103 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean canRead()
/*     */   {
/* 110 */     return false;
/*     */   }
/*     */   
/*     */   public String getWebappPath()
/*     */   {
/* 115 */     return this.webAppPath;
/*     */   }
/*     */   
/*     */   public String getETag()
/*     */   {
/* 120 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setMimeType(String mimeType) {}
/*     */   
/*     */ 
/*     */   public String getMimeType()
/*     */   {
/* 130 */     return null;
/*     */   }
/*     */   
/*     */   public InputStream getInputStream()
/*     */   {
/* 135 */     return null;
/*     */   }
/*     */   
/*     */   public byte[] getContent()
/*     */   {
/* 140 */     return null;
/*     */   }
/*     */   
/*     */   public long getCreation()
/*     */   {
/* 145 */     return 0L;
/*     */   }
/*     */   
/*     */   public URL getURL()
/*     */   {
/* 150 */     return null;
/*     */   }
/*     */   
/*     */   public URL getCodeBase()
/*     */   {
/* 155 */     return null;
/*     */   }
/*     */   
/*     */   public Certificate[] getCertificates()
/*     */   {
/* 160 */     return null;
/*     */   }
/*     */   
/*     */   public Manifest getManifest()
/*     */   {
/* 165 */     return null;
/*     */   }
/*     */   
/*     */   public WebResourceRoot getWebResourceRoot()
/*     */   {
/* 170 */     return this.root;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\webresources\EmptyResource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */