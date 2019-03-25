/*     */ package org.springframework.web.servlet.resource;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.core.io.AbstractResource;
/*     */ import org.springframework.core.io.Resource;
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
/*     */ public class GzipResourceResolver
/*     */   extends AbstractResourceResolver
/*     */ {
/*     */   protected Resource resolveResourceInternal(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain)
/*     */   {
/*  48 */     Resource resource = chain.resolveResource(request, requestPath, locations);
/*  49 */     if ((resource == null) || ((request != null) && (!isGzipAccepted(request)))) {
/*  50 */       return resource;
/*     */     }
/*     */     try
/*     */     {
/*  54 */       Resource gzipped = new GzippedResource(resource);
/*  55 */       if (gzipped.exists()) {
/*  56 */         return gzipped;
/*     */       }
/*     */     }
/*     */     catch (IOException ex) {
/*  60 */       this.logger.trace("No gzipped resource for [" + resource.getFilename() + "]", ex);
/*     */     }
/*     */     
/*  63 */     return resource;
/*     */   }
/*     */   
/*     */   private boolean isGzipAccepted(HttpServletRequest request) {
/*  67 */     String value = request.getHeader("Accept-Encoding");
/*  68 */     return (value != null) && (value.toLowerCase().contains("gzip"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String resolveUrlPathInternal(String resourceUrlPath, List<? extends Resource> locations, ResourceResolverChain chain)
/*     */   {
/*  75 */     return chain.resolveUrlPath(resourceUrlPath, locations);
/*     */   }
/*     */   
/*     */   private static final class GzippedResource
/*     */     extends AbstractResource implements EncodedResource
/*     */   {
/*     */     private final Resource original;
/*     */     private final Resource gzipped;
/*     */     
/*     */     public GzippedResource(Resource original) throws IOException
/*     */     {
/*  86 */       this.original = original;
/*  87 */       this.gzipped = original.createRelative(original.getFilename() + ".gz");
/*     */     }
/*     */     
/*     */     public InputStream getInputStream() throws IOException {
/*  91 */       return this.gzipped.getInputStream();
/*     */     }
/*     */     
/*     */     public boolean exists() {
/*  95 */       return this.gzipped.exists();
/*     */     }
/*     */     
/*     */     public boolean isReadable() {
/*  99 */       return this.gzipped.isReadable();
/*     */     }
/*     */     
/*     */     public boolean isOpen() {
/* 103 */       return this.gzipped.isOpen();
/*     */     }
/*     */     
/*     */     public URL getURL() throws IOException {
/* 107 */       return this.gzipped.getURL();
/*     */     }
/*     */     
/*     */     public URI getURI() throws IOException {
/* 111 */       return this.gzipped.getURI();
/*     */     }
/*     */     
/*     */     public File getFile() throws IOException {
/* 115 */       return this.gzipped.getFile();
/*     */     }
/*     */     
/*     */     public long contentLength() throws IOException {
/* 119 */       return this.gzipped.contentLength();
/*     */     }
/*     */     
/*     */     public long lastModified() throws IOException {
/* 123 */       return this.gzipped.lastModified();
/*     */     }
/*     */     
/*     */     public Resource createRelative(String relativePath) throws IOException {
/* 127 */       return this.gzipped.createRelative(relativePath);
/*     */     }
/*     */     
/*     */     public String getFilename() {
/* 131 */       return this.original.getFilename();
/*     */     }
/*     */     
/*     */     public String getDescription() {
/* 135 */       return this.gzipped.getDescription();
/*     */     }
/*     */     
/*     */     public String getContentEncoding() {
/* 139 */       return "gzip";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\resource\GzipResourceResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */