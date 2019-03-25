/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import org.springframework.util.ResourceUtils;
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
/*     */ public abstract class AbstractFileResolvingResource
/*     */   extends AbstractResource
/*     */ {
/*     */   public File getFile()
/*     */     throws IOException
/*     */   {
/*  49 */     URL url = getURL();
/*  50 */     if (url.getProtocol().startsWith("vfs")) {
/*  51 */       return VfsResourceDelegate.getResource(url).getFile();
/*     */     }
/*  53 */     return ResourceUtils.getFile(url, getDescription());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected File getFileForLastModifiedCheck()
/*     */     throws IOException
/*     */   {
/*  62 */     URL url = getURL();
/*  63 */     if (ResourceUtils.isJarURL(url)) {
/*  64 */       URL actualUrl = ResourceUtils.extractArchiveURL(url);
/*  65 */       if (actualUrl.getProtocol().startsWith("vfs")) {
/*  66 */         return VfsResourceDelegate.getResource(actualUrl).getFile();
/*     */       }
/*  68 */       return ResourceUtils.getFile(actualUrl, "Jar URL");
/*     */     }
/*     */     
/*  71 */     return getFile();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected File getFile(URI uri)
/*     */     throws IOException
/*     */   {
/*  81 */     if (uri.getScheme().startsWith("vfs")) {
/*  82 */       return VfsResourceDelegate.getResource(uri).getFile();
/*     */     }
/*  84 */     return ResourceUtils.getFile(uri, getDescription());
/*     */   }
/*     */   
/*     */   public boolean exists()
/*     */   {
/*     */     try
/*     */     {
/*  91 */       URL url = getURL();
/*  92 */       if (ResourceUtils.isFileURL(url))
/*     */       {
/*  94 */         return getFile().exists();
/*     */       }
/*     */       
/*     */ 
/*  98 */       URLConnection con = url.openConnection();
/*  99 */       customizeConnection(con);
/* 100 */       HttpURLConnection httpCon = (con instanceof HttpURLConnection) ? (HttpURLConnection)con : null;
/*     */       
/* 102 */       if (httpCon != null) {
/* 103 */         int code = httpCon.getResponseCode();
/* 104 */         if (code == 200) {
/* 105 */           return true;
/*     */         }
/* 107 */         if (code == 404) {
/* 108 */           return false;
/*     */         }
/*     */       }
/* 111 */       if (con.getContentLength() >= 0) {
/* 112 */         return true;
/*     */       }
/* 114 */       if (httpCon != null)
/*     */       {
/* 116 */         httpCon.disconnect();
/* 117 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 121 */       InputStream is = getInputStream();
/* 122 */       is.close();
/* 123 */       return true;
/*     */     }
/*     */     catch (IOException ex) {}
/*     */     
/*     */ 
/* 128 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isReadable()
/*     */   {
/*     */     try
/*     */     {
/* 135 */       URL url = getURL();
/* 136 */       if (ResourceUtils.isFileURL(url))
/*     */       {
/* 138 */         File file = getFile();
/* 139 */         return (file.canRead()) && (!file.isDirectory());
/*     */       }
/*     */       
/* 142 */       return true;
/*     */     }
/*     */     catch (IOException ex) {}
/*     */     
/* 146 */     return false;
/*     */   }
/*     */   
/*     */   public long contentLength()
/*     */     throws IOException
/*     */   {
/* 152 */     URL url = getURL();
/* 153 */     if (ResourceUtils.isFileURL(url))
/*     */     {
/* 155 */       return getFile().length();
/*     */     }
/*     */     
/*     */ 
/* 159 */     URLConnection con = url.openConnection();
/* 160 */     customizeConnection(con);
/* 161 */     return con.getContentLength();
/*     */   }
/*     */   
/*     */   public long lastModified()
/*     */     throws IOException
/*     */   {
/* 167 */     URL url = getURL();
/* 168 */     if ((ResourceUtils.isFileURL(url)) || (ResourceUtils.isJarURL(url))) {
/*     */       try
/*     */       {
/* 171 */         return super.lastModified();
/*     */       }
/*     */       catch (FileNotFoundException localFileNotFoundException) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 178 */     URLConnection con = url.openConnection();
/* 179 */     customizeConnection(con);
/* 180 */     return con.getLastModified();
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
/*     */   protected void customizeConnection(URLConnection con)
/*     */     throws IOException
/*     */   {
/* 194 */     ResourceUtils.useCachesIfNecessary(con);
/* 195 */     if ((con instanceof HttpURLConnection)) {
/* 196 */       customizeConnection((HttpURLConnection)con);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void customizeConnection(HttpURLConnection con)
/*     */     throws IOException
/*     */   {
/* 208 */     con.setRequestMethod("HEAD");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class VfsResourceDelegate
/*     */   {
/*     */     public static Resource getResource(URL url)
/*     */       throws IOException
/*     */     {
/* 218 */       return new VfsResource(VfsUtils.getRoot(url));
/*     */     }
/*     */     
/*     */     public static Resource getResource(URI uri) throws IOException {
/* 222 */       return new VfsResource(VfsUtils.getRoot(uri));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\io\AbstractFileResolvingResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */