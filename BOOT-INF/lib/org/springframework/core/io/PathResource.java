/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.attribute.FileTime;
/*     */ import org.springframework.lang.UsesJava7;
/*     */ import org.springframework.util.Assert;
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
/*     */ @UsesJava7
/*     */ public class PathResource
/*     */   extends AbstractResource
/*     */   implements WritableResource
/*     */ {
/*     */   private final Path path;
/*     */   
/*     */   public PathResource(Path path)
/*     */   {
/*  58 */     Assert.notNull(path, "Path must not be null");
/*  59 */     this.path = path.normalize();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PathResource(String path)
/*     */   {
/*  71 */     Assert.notNull(path, "Path must not be null");
/*  72 */     this.path = Paths.get(path, new String[0]).normalize();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PathResource(URI uri)
/*     */   {
/*  84 */     Assert.notNull(uri, "URI must not be null");
/*  85 */     this.path = Paths.get(uri).normalize();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String getPath()
/*     */   {
/*  93 */     return this.path.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean exists()
/*     */   {
/* 102 */     return Files.exists(this.path, new LinkOption[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isReadable()
/*     */   {
/* 113 */     return (Files.isReadable(this.path)) && (!Files.isDirectory(this.path, new LinkOption[0]));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public InputStream getInputStream()
/*     */     throws IOException
/*     */   {
/* 122 */     if (!exists()) {
/* 123 */       throw new FileNotFoundException(getPath() + " (no such file or directory)");
/*     */     }
/* 125 */     if (Files.isDirectory(this.path, new LinkOption[0])) {
/* 126 */       throw new FileNotFoundException(getPath() + " (is a directory)");
/*     */     }
/* 128 */     return Files.newInputStream(this.path, new OpenOption[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isWritable()
/*     */   {
/* 139 */     return (Files.isWritable(this.path)) && (!Files.isDirectory(this.path, new LinkOption[0]));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public OutputStream getOutputStream()
/*     */     throws IOException
/*     */   {
/* 148 */     if (Files.isDirectory(this.path, new LinkOption[0])) {
/* 149 */       throw new FileNotFoundException(getPath() + " (is a directory)");
/*     */     }
/* 151 */     return Files.newOutputStream(this.path, new OpenOption[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public URL getURL()
/*     */     throws IOException
/*     */   {
/* 161 */     return this.path.toUri().toURL();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public URI getURI()
/*     */     throws IOException
/*     */   {
/* 170 */     return this.path.toUri();
/*     */   }
/*     */   
/*     */ 
/*     */   public File getFile()
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 179 */       return this.path.toFile();
/*     */ 
/*     */     }
/*     */     catch (UnsupportedOperationException ex)
/*     */     {
/* 184 */       throw new FileNotFoundException(this.path + " cannot be resolved to absolute file path");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long contentLength()
/*     */     throws IOException
/*     */   {
/* 193 */     return Files.size(this.path);
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
/* 204 */     return Files.getLastModifiedTime(this.path, new LinkOption[0]).toMillis();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Resource createRelative(String relativePath)
/*     */     throws IOException
/*     */   {
/* 214 */     return new PathResource(this.path.resolve(relativePath));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFilename()
/*     */   {
/* 223 */     return this.path.getFileName().toString();
/*     */   }
/*     */   
/*     */   public String getDescription()
/*     */   {
/* 228 */     return "path [" + this.path.toAbsolutePath() + "]";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 237 */     return (this == obj) || (((obj instanceof PathResource)) && 
/* 238 */       (this.path.equals(((PathResource)obj).path)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 246 */     return this.path.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\io\PathResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */