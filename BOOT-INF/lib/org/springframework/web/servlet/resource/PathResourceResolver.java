/*     */ package org.springframework.web.servlet.resource;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URL;
/*     */ import java.net.URLDecoder;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.core.io.ClassPathResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.UrlResource;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.context.support.ServletContextResource;
/*     */ import org.springframework.web.util.UriUtils;
/*     */ import org.springframework.web.util.UrlPathHelper;
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
/*     */ public class PathResourceResolver
/*     */   extends AbstractResourceResolver
/*     */ {
/*  53 */   private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
/*     */   
/*     */ 
/*     */   private Resource[] allowedLocations;
/*     */   
/*  58 */   private final Map<Resource, Charset> locationCharsets = new HashMap(4);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private UrlPathHelper urlPathHelper;
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
/*     */   public void setAllowedLocations(Resource... locations)
/*     */   {
/*  81 */     this.allowedLocations = locations;
/*     */   }
/*     */   
/*     */   public Resource[] getAllowedLocations() {
/*  85 */     return this.allowedLocations;
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
/*     */   public void setLocationCharsets(Map<Resource, Charset> locationCharsets)
/*     */   {
/*  98 */     this.locationCharsets.clear();
/*  99 */     this.locationCharsets.putAll(locationCharsets);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<Resource, Charset> getLocationCharsets()
/*     */   {
/* 107 */     return Collections.unmodifiableMap(this.locationCharsets);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUrlPathHelper(UrlPathHelper urlPathHelper)
/*     */   {
/* 117 */     this.urlPathHelper = urlPathHelper;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public UrlPathHelper getUrlPathHelper()
/*     */   {
/* 125 */     return this.urlPathHelper;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Resource resolveResourceInternal(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain)
/*     */   {
/* 133 */     return getResource(requestPath, request, locations);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String resolveUrlPathInternal(String resourcePath, List<? extends Resource> locations, ResourceResolverChain chain)
/*     */   {
/* 140 */     return (StringUtils.hasText(resourcePath)) && 
/* 141 */       (getResource(resourcePath, null, locations) != null) ? resourcePath : null;
/*     */   }
/*     */   
/*     */ 
/*     */   private Resource getResource(String resourcePath, HttpServletRequest request, List<? extends Resource> locations)
/*     */   {
/* 147 */     for (Resource location : locations) {
/*     */       try {
/* 149 */         if (this.logger.isTraceEnabled()) {
/* 150 */           this.logger.trace("Checking location: " + location);
/*     */         }
/* 152 */         String pathToUse = encodeIfNecessary(resourcePath, request, location);
/* 153 */         Resource resource = getResource(pathToUse, location);
/* 154 */         if (resource != null) {
/* 155 */           if (this.logger.isTraceEnabled()) {
/* 156 */             this.logger.trace("Found match: " + resource);
/*     */           }
/* 158 */           return resource;
/*     */         }
/* 160 */         if (this.logger.isTraceEnabled()) {
/* 161 */           this.logger.trace("No match for location: " + location);
/*     */         }
/*     */       }
/*     */       catch (IOException ex) {
/* 165 */         this.logger.trace("Failure checking for relative resource - trying next location", ex);
/*     */       }
/*     */     }
/* 168 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Resource getResource(String resourcePath, Resource location)
/*     */     throws IOException
/*     */   {
/* 180 */     Resource resource = location.createRelative(resourcePath);
/* 181 */     if ((resource.exists()) && (resource.isReadable())) {
/* 182 */       if (checkResource(resource, location)) {
/* 183 */         return resource;
/*     */       }
/* 185 */       if (this.logger.isTraceEnabled()) {
/* 186 */         this.logger.trace("Resource path=\"" + resourcePath + "\" was successfully resolved but resource=\"" + resource
/* 187 */           .getURL() + "\" is neither under the current location=\"" + location
/* 188 */           .getURL() + "\" nor under any of the allowed locations=" + 
/* 189 */           Arrays.asList(getAllowedLocations()));
/*     */       }
/*     */     }
/* 192 */     return null;
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
/*     */   protected boolean checkResource(Resource resource, Resource location)
/*     */     throws IOException
/*     */   {
/* 206 */     if (isResourceUnderLocation(resource, location)) {
/* 207 */       return true;
/*     */     }
/* 209 */     if (getAllowedLocations() != null) {
/* 210 */       for (Resource current : getAllowedLocations()) {
/* 211 */         if (isResourceUnderLocation(resource, current)) {
/* 212 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 216 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isResourceUnderLocation(Resource resource, Resource location) throws IOException {
/* 220 */     if (resource.getClass() != location.getClass()) {
/* 221 */       return false;
/*     */     }
/*     */     
/*     */     String locationPath;
/*     */     
/*     */     String resourcePath;
/* 227 */     if ((resource instanceof UrlResource)) {
/* 228 */       String resourcePath = resource.getURL().toExternalForm();
/* 229 */       locationPath = StringUtils.cleanPath(location.getURL().toString());
/*     */     } else { String locationPath;
/* 231 */       if ((resource instanceof ClassPathResource)) {
/* 232 */         String resourcePath = ((ClassPathResource)resource).getPath();
/* 233 */         locationPath = StringUtils.cleanPath(((ClassPathResource)location).getPath());
/*     */       } else { String locationPath;
/* 235 */         if ((resource instanceof ServletContextResource)) {
/* 236 */           String resourcePath = ((ServletContextResource)resource).getPath();
/* 237 */           locationPath = StringUtils.cleanPath(((ServletContextResource)location).getPath());
/*     */         }
/*     */         else {
/* 240 */           resourcePath = resource.getURL().getPath();
/* 241 */           locationPath = StringUtils.cleanPath(location.getURL().getPath());
/*     */         }
/*     */       } }
/* 244 */     if (locationPath.equals(resourcePath)) {
/* 245 */       return true;
/*     */     }
/* 247 */     String locationPath = locationPath + "/";
/* 248 */     if (!resourcePath.startsWith(locationPath)) {
/* 249 */       return false;
/*     */     }
/*     */     
/* 252 */     if (resourcePath.contains("%"))
/*     */     {
/* 254 */       if (URLDecoder.decode(resourcePath, "UTF-8").contains("../")) {
/* 255 */         if (this.logger.isTraceEnabled()) {
/* 256 */           this.logger.trace("Resolved resource path contains \"../\" after decoding: " + resourcePath);
/*     */         }
/* 258 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 262 */     return true;
/*     */   }
/*     */   
/*     */   private String encodeIfNecessary(String path, HttpServletRequest request, Resource location) {
/* 266 */     if ((shouldEncodeRelativePath(location)) && (request != null)) {
/* 267 */       Charset charset = (Charset)this.locationCharsets.get(location);
/* 268 */       charset = charset != null ? charset : DEFAULT_CHARSET;
/* 269 */       StringBuilder sb = new StringBuilder();
/* 270 */       StringTokenizer tokenizer = new StringTokenizer(path, "/");
/* 271 */       while (tokenizer.hasMoreTokens()) {
/* 272 */         String value = null;
/*     */         try {
/* 274 */           value = UriUtils.encode(tokenizer.nextToken(), charset.name());
/*     */         }
/*     */         catch (UnsupportedEncodingException ex)
/*     */         {
/* 278 */           throw new IllegalStateException("Unexpected error", ex);
/*     */         }
/* 280 */         sb.append(value);
/* 281 */         sb.append("/");
/*     */       }
/* 283 */       if (!path.endsWith("/")) {
/* 284 */         sb.setLength(sb.length() - 1);
/*     */       }
/* 286 */       return sb.toString();
/*     */     }
/*     */     
/* 289 */     return path;
/*     */   }
/*     */   
/*     */   private boolean shouldEncodeRelativePath(Resource location)
/*     */   {
/* 294 */     return ((location instanceof UrlResource)) && (this.urlPathHelper != null) && 
/* 295 */       (this.urlPathHelper.isUrlDecode());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\resource\PathResourceResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */