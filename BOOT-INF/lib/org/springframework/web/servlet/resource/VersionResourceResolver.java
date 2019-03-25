/*     */ package org.springframework.web.servlet.resource;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.core.io.AbstractResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.util.AntPathMatcher;
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
/*     */ public class VersionResourceResolver
/*     */   extends AbstractResourceResolver
/*     */ {
/*  65 */   private AntPathMatcher pathMatcher = new AntPathMatcher();
/*     */   
/*     */ 
/*  68 */   private final Map<String, VersionStrategy> versionStrategyMap = new LinkedHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setStrategyMap(Map<String, VersionStrategy> map)
/*     */   {
/*  78 */     this.versionStrategyMap.clear();
/*  79 */     this.versionStrategyMap.putAll(map);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Map<String, VersionStrategy> getStrategyMap()
/*     */   {
/*  86 */     return this.versionStrategyMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public VersionResourceResolver addContentVersionStrategy(String... pathPatterns)
/*     */   {
/* 102 */     addVersionStrategy(new ContentVersionStrategy(), pathPatterns);
/* 103 */     return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public VersionResourceResolver addFixedVersionStrategy(String version, String... pathPatterns)
/*     */   {
/* 125 */     List<String> patternsList = Arrays.asList(pathPatterns);
/* 126 */     List<String> prefixedPatterns = new ArrayList(pathPatterns.length);
/* 127 */     String versionPrefix = "/" + version;
/* 128 */     for (String pattern : patternsList) {
/* 129 */       prefixedPatterns.add(pattern);
/* 130 */       if ((!pattern.startsWith(versionPrefix)) && (!patternsList.contains(versionPrefix + pattern))) {
/* 131 */         prefixedPatterns.add(versionPrefix + pattern);
/*     */       }
/*     */     }
/* 134 */     return addVersionStrategy(new FixedVersionStrategy(version), (String[])prefixedPatterns.toArray(new String[0]));
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
/*     */   public VersionResourceResolver addVersionStrategy(VersionStrategy strategy, String... pathPatterns)
/*     */   {
/* 147 */     for (String pattern : pathPatterns) {
/* 148 */       getStrategyMap().put(pattern, strategy);
/*     */     }
/* 150 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Resource resolveResourceInternal(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain)
/*     */   {
/* 158 */     Resource resolved = chain.resolveResource(request, requestPath, locations);
/* 159 */     if (resolved != null) {
/* 160 */       return resolved;
/*     */     }
/*     */     
/* 163 */     VersionStrategy versionStrategy = getStrategyForPath(requestPath);
/* 164 */     if (versionStrategy == null) {
/* 165 */       return null;
/*     */     }
/*     */     
/* 168 */     String candidateVersion = versionStrategy.extractVersion(requestPath);
/* 169 */     if (StringUtils.isEmpty(candidateVersion)) {
/* 170 */       if (this.logger.isTraceEnabled()) {
/* 171 */         this.logger.trace("No version found in path \"" + requestPath + "\"");
/*     */       }
/* 173 */       return null;
/*     */     }
/*     */     
/* 176 */     String simplePath = versionStrategy.removeVersion(requestPath, candidateVersion);
/* 177 */     if (this.logger.isTraceEnabled()) {
/* 178 */       this.logger.trace("Extracted version from path, re-resolving without version: \"" + simplePath + "\"");
/*     */     }
/*     */     
/* 181 */     Resource baseResource = chain.resolveResource(request, simplePath, locations);
/* 182 */     if (baseResource == null) {
/* 183 */       return null;
/*     */     }
/*     */     
/* 186 */     String actualVersion = versionStrategy.getResourceVersion(baseResource);
/* 187 */     if (candidateVersion.equals(actualVersion)) {
/* 188 */       if (this.logger.isTraceEnabled()) {
/* 189 */         this.logger.trace("Resource matches extracted version [" + candidateVersion + "]");
/*     */       }
/* 191 */       return new FileNameVersionedResource(baseResource, candidateVersion);
/*     */     }
/*     */     
/* 194 */     if (this.logger.isTraceEnabled()) {
/* 195 */       this.logger.trace("Potential resource found for \"" + requestPath + "\", but version [" + candidateVersion + "] does not match");
/*     */     }
/*     */     
/* 198 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   protected String resolveUrlPathInternal(String resourceUrlPath, List<? extends Resource> locations, ResourceResolverChain chain)
/*     */   {
/* 204 */     String baseUrl = chain.resolveUrlPath(resourceUrlPath, locations);
/* 205 */     if (StringUtils.hasText(baseUrl)) {
/* 206 */       VersionStrategy versionStrategy = getStrategyForPath(resourceUrlPath);
/* 207 */       if (versionStrategy == null) {
/* 208 */         return baseUrl;
/*     */       }
/* 210 */       if (this.logger.isTraceEnabled()) {
/* 211 */         this.logger.trace("Getting the original resource to determine version for path \"" + resourceUrlPath + "\"");
/*     */       }
/* 213 */       Resource resource = chain.resolveResource(null, baseUrl, locations);
/* 214 */       String version = versionStrategy.getResourceVersion(resource);
/* 215 */       if (this.logger.isTraceEnabled()) {
/* 216 */         this.logger.trace("Determined version [" + version + "] for " + resource);
/*     */       }
/* 218 */       return versionStrategy.addVersion(baseUrl, version);
/*     */     }
/* 220 */     return baseUrl;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected VersionStrategy getStrategyForPath(String requestPath)
/*     */   {
/* 228 */     String path = "/".concat(requestPath);
/* 229 */     List<String> matchingPatterns = new ArrayList();
/* 230 */     for (String pattern : this.versionStrategyMap.keySet()) {
/* 231 */       if (this.pathMatcher.match(pattern, path)) {
/* 232 */         matchingPatterns.add(pattern);
/*     */       }
/*     */     }
/* 235 */     if (!matchingPatterns.isEmpty()) {
/* 236 */       Object comparator = this.pathMatcher.getPatternComparator(path);
/* 237 */       Collections.sort(matchingPatterns, (Comparator)comparator);
/* 238 */       return (VersionStrategy)this.versionStrategyMap.get(matchingPatterns.get(0));
/*     */     }
/* 240 */     return null;
/*     */   }
/*     */   
/*     */   private class FileNameVersionedResource
/*     */     extends AbstractResource implements VersionedResource
/*     */   {
/*     */     private final Resource original;
/*     */     private final String version;
/*     */     
/*     */     public FileNameVersionedResource(Resource original, String version)
/*     */     {
/* 251 */       this.original = original;
/* 252 */       this.version = version;
/*     */     }
/*     */     
/*     */     public boolean exists()
/*     */     {
/* 257 */       return this.original.exists();
/*     */     }
/*     */     
/*     */     public boolean isReadable()
/*     */     {
/* 262 */       return this.original.isReadable();
/*     */     }
/*     */     
/*     */     public boolean isOpen()
/*     */     {
/* 267 */       return this.original.isOpen();
/*     */     }
/*     */     
/*     */     public URL getURL() throws IOException
/*     */     {
/* 272 */       return this.original.getURL();
/*     */     }
/*     */     
/*     */     public URI getURI() throws IOException
/*     */     {
/* 277 */       return this.original.getURI();
/*     */     }
/*     */     
/*     */     public File getFile() throws IOException
/*     */     {
/* 282 */       return this.original.getFile();
/*     */     }
/*     */     
/*     */     public String getFilename()
/*     */     {
/* 287 */       return this.original.getFilename();
/*     */     }
/*     */     
/*     */     public long contentLength() throws IOException
/*     */     {
/* 292 */       return this.original.contentLength();
/*     */     }
/*     */     
/*     */     public long lastModified() throws IOException
/*     */     {
/* 297 */       return this.original.lastModified();
/*     */     }
/*     */     
/*     */     public Resource createRelative(String relativePath) throws IOException
/*     */     {
/* 302 */       return this.original.createRelative(relativePath);
/*     */     }
/*     */     
/*     */     public String getDescription()
/*     */     {
/* 307 */       return this.original.getDescription();
/*     */     }
/*     */     
/*     */     public InputStream getInputStream() throws IOException
/*     */     {
/* 312 */       return this.original.getInputStream();
/*     */     }
/*     */     
/*     */     public String getVersion()
/*     */     {
/* 317 */       return this.version;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\resource\VersionResourceResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */