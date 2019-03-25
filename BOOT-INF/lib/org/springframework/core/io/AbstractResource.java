/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import org.springframework.core.NestedIOException;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ public abstract class AbstractResource
/*     */   implements Resource
/*     */ {
/*     */   public boolean exists()
/*     */   {
/*     */     try
/*     */     {
/*  53 */       return getFile().exists();
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/*     */       try {
/*  58 */         InputStream is = getInputStream();
/*  59 */         is.close();
/*  60 */         return true;
/*     */       } catch (Throwable isEx) {}
/*     */     }
/*  63 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isReadable()
/*     */   {
/*  73 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpen()
/*     */   {
/*  81 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public URL getURL()
/*     */     throws IOException
/*     */   {
/*  90 */     throw new FileNotFoundException(getDescription() + " cannot be resolved to URL");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public URI getURI()
/*     */     throws IOException
/*     */   {
/*  99 */     URL url = getURL();
/*     */     try {
/* 101 */       return ResourceUtils.toURI(url);
/*     */     }
/*     */     catch (URISyntaxException ex) {
/* 104 */       throw new NestedIOException("Invalid URI [" + url + "]", ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public File getFile()
/*     */     throws IOException
/*     */   {
/* 114 */     throw new FileNotFoundException(getDescription() + " cannot be resolved to absolute file path");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long contentLength()
/*     */     throws IOException
/*     */   {
/* 125 */     InputStream is = getInputStream();
/* 126 */     Assert.state(is != null, "Resource InputStream must not be null");
/*     */     try {
/* 128 */       long size = 0L;
/* 129 */       byte[] buf = new byte['ÿ'];
/*     */       int read;
/* 131 */       while ((read = is.read(buf)) != -1) {
/* 132 */         size += read;
/*     */       }
/* 134 */       return size;
/*     */     }
/*     */     finally {
/*     */       try {
/* 138 */         is.close();
/*     */       }
/*     */       catch (IOException localIOException1) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long lastModified()
/*     */     throws IOException
/*     */   {
/* 152 */     long lastModified = getFileForLastModifiedCheck().lastModified();
/* 153 */     if (lastModified == 0L) {
/* 154 */       throw new FileNotFoundException(getDescription() + " cannot be resolved in the file system for resolving its last-modified timestamp");
/*     */     }
/*     */     
/* 157 */     return lastModified;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected File getFileForLastModifiedCheck()
/*     */     throws IOException
/*     */   {
/* 169 */     return getFile();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Resource createRelative(String relativePath)
/*     */     throws IOException
/*     */   {
/* 178 */     throw new FileNotFoundException("Cannot create a relative resource for " + getDescription());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFilename()
/*     */   {
/* 187 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 197 */     return getDescription();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 206 */     return (obj == this) || (((obj instanceof Resource)) && 
/* 207 */       (((Resource)obj).getDescription().equals(getDescription())));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 216 */     return getDescription().hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\io\AbstractResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */