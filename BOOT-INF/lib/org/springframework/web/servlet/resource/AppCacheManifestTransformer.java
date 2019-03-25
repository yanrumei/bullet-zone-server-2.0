/*     */ package org.springframework.web.servlet.resource;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.StringWriter;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Scanner;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.util.DigestUtils;
/*     */ import org.springframework.util.FileCopyUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class AppCacheManifestTransformer
/*     */   extends ResourceTransformerSupport
/*     */ {
/*     */   private static final String MANIFEST_HEADER = "CACHE MANIFEST";
/*  63 */   private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
/*     */   
/*  65 */   private static final Log logger = LogFactory.getLog(AppCacheManifestTransformer.class);
/*     */   
/*     */ 
/*  68 */   private final Map<String, SectionTransformer> sectionTransformers = new HashMap();
/*     */   
/*     */ 
/*     */   private final String fileExtension;
/*     */   
/*     */ 
/*     */ 
/*     */   public AppCacheManifestTransformer()
/*     */   {
/*  77 */     this("manifest");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AppCacheManifestTransformer(String fileExtension)
/*     */   {
/*  85 */     this.fileExtension = fileExtension;
/*     */     
/*  87 */     SectionTransformer noOpSection = new NoOpSection(null);
/*  88 */     this.sectionTransformers.put("CACHE MANIFEST", noOpSection);
/*  89 */     this.sectionTransformers.put("NETWORK:", noOpSection);
/*  90 */     this.sectionTransformers.put("FALLBACK:", noOpSection);
/*  91 */     this.sectionTransformers.put("CACHE:", new CacheSection(null));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Resource transform(HttpServletRequest request, Resource resource, ResourceTransformerChain transformerChain)
/*     */     throws IOException
/*     */   {
/*  99 */     resource = transformerChain.transform(request, resource);
/* 100 */     if (!this.fileExtension.equals(StringUtils.getFilenameExtension(resource.getFilename()))) {
/* 101 */       return resource;
/*     */     }
/*     */     
/* 104 */     byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
/* 105 */     String content = new String(bytes, DEFAULT_CHARSET);
/*     */     
/* 107 */     if (!content.startsWith("CACHE MANIFEST")) {
/* 108 */       if (logger.isTraceEnabled()) {
/* 109 */         logger.trace("AppCache manifest does not start with 'CACHE MANIFEST', skipping: " + resource);
/*     */       }
/* 111 */       return resource;
/*     */     }
/*     */     
/* 114 */     if (logger.isTraceEnabled()) {
/* 115 */       logger.trace("Transforming resource: " + resource);
/*     */     }
/*     */     
/* 118 */     StringWriter contentWriter = new StringWriter();
/* 119 */     HashBuilder hashBuilder = new HashBuilder(content.length());
/*     */     
/* 121 */     Scanner scanner = new Scanner(content);
/* 122 */     SectionTransformer currentTransformer = (SectionTransformer)this.sectionTransformers.get("CACHE MANIFEST");
/* 123 */     while (scanner.hasNextLine()) {
/* 124 */       String line = scanner.nextLine();
/* 125 */       if (this.sectionTransformers.containsKey(line.trim())) {
/* 126 */         currentTransformer = (SectionTransformer)this.sectionTransformers.get(line.trim());
/* 127 */         contentWriter.write(line + "\n");
/* 128 */         hashBuilder.appendString(line);
/*     */       }
/*     */       else {
/* 131 */         contentWriter.write(currentTransformer
/* 132 */           .transform(line, hashBuilder, resource, transformerChain, request) + "\n");
/*     */       }
/*     */     }
/*     */     
/* 136 */     String hash = hashBuilder.build();
/* 137 */     contentWriter.write("\n# Hash: " + hash);
/* 138 */     if (logger.isTraceEnabled()) {
/* 139 */       logger.trace("AppCache file: [" + resource.getFilename() + "] hash: [" + hash + "]");
/*     */     }
/*     */     
/* 142 */     return new TransformedResource(resource, contentWriter.toString().getBytes(DEFAULT_CHARSET));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static abstract interface SectionTransformer
/*     */   {
/*     */     public abstract String transform(String paramString, AppCacheManifestTransformer.HashBuilder paramHashBuilder, Resource paramResource, ResourceTransformerChain paramResourceTransformerChain, HttpServletRequest paramHttpServletRequest)
/*     */       throws IOException;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class NoOpSection
/*     */     implements AppCacheManifestTransformer.SectionTransformer
/*     */   {
/*     */     public String transform(String line, AppCacheManifestTransformer.HashBuilder builder, Resource resource, ResourceTransformerChain transformerChain, HttpServletRequest request)
/*     */       throws IOException
/*     */     {
/* 163 */       builder.appendString(line);
/* 164 */       return line;
/*     */     }
/*     */   }
/*     */   
/*     */   private class CacheSection
/*     */     implements AppCacheManifestTransformer.SectionTransformer
/*     */   {
/*     */     private static final String COMMENT_DIRECTIVE = "#";
/*     */     
/*     */     private CacheSection() {}
/*     */     
/*     */     public String transform(String line, AppCacheManifestTransformer.HashBuilder builder, Resource resource, ResourceTransformerChain transformerChain, HttpServletRequest request) throws IOException
/*     */     {
/* 177 */       if ((isLink(line)) && (!hasScheme(line))) {
/* 178 */         ResourceResolverChain resolverChain = transformerChain.getResolverChain();
/*     */         
/* 180 */         Resource appCacheResource = resolverChain.resolveResource(null, line, Collections.singletonList(resource));
/* 181 */         String path = AppCacheManifestTransformer.this.resolveUrlPath(line, request, resource, transformerChain);
/* 182 */         builder.appendResource(appCacheResource);
/* 183 */         if (AppCacheManifestTransformer.logger.isTraceEnabled()) {
/* 184 */           AppCacheManifestTransformer.logger.trace("Link modified: " + path + " (original: " + line + ")");
/*     */         }
/* 186 */         return path;
/*     */       }
/* 188 */       builder.appendString(line);
/* 189 */       return line;
/*     */     }
/*     */     
/*     */     private boolean hasScheme(String link) {
/* 193 */       int schemeIndex = link.indexOf(":");
/* 194 */       return (link.startsWith("//")) || ((schemeIndex > 0) && (!link.substring(0, schemeIndex).contains("/")));
/*     */     }
/*     */     
/*     */     private boolean isLink(String line) {
/* 198 */       return (StringUtils.hasText(line)) && (!line.startsWith("#"));
/*     */     }
/*     */   }
/*     */   
/*     */   private static class HashBuilder
/*     */   {
/*     */     private final ByteArrayOutputStream baos;
/*     */     
/*     */     public HashBuilder(int initialSize)
/*     */     {
/* 208 */       this.baos = new ByteArrayOutputStream(initialSize);
/*     */     }
/*     */     
/*     */     public void appendResource(Resource resource) throws IOException {
/* 212 */       byte[] content = FileCopyUtils.copyToByteArray(resource.getInputStream());
/* 213 */       this.baos.write(DigestUtils.md5Digest(content));
/*     */     }
/*     */     
/*     */     public void appendString(String content) throws IOException {
/* 217 */       this.baos.write(content.getBytes(AppCacheManifestTransformer.DEFAULT_CHARSET));
/*     */     }
/*     */     
/*     */     public String build() {
/* 221 */       return DigestUtils.md5DigestAsHex(this.baos.toByteArray());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\resource\AppCacheManifestTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */