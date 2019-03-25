/*     */ package org.springframework.web.accept;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ public class MappingMediaTypeFileExtensionResolver
/*     */   implements MediaTypeFileExtensionResolver
/*     */ {
/*  45 */   private final ConcurrentMap<String, MediaType> mediaTypes = new ConcurrentHashMap(64);
/*     */   
/*     */ 
/*  48 */   private final MultiValueMap<MediaType, String> fileExtensions = new LinkedMultiValueMap();
/*     */   
/*     */ 
/*  51 */   private final List<String> allFileExtensions = new LinkedList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MappingMediaTypeFileExtensionResolver(Map<String, MediaType> mediaTypes)
/*     */   {
/*  58 */     if (mediaTypes != null) {
/*  59 */       for (Map.Entry<String, MediaType> entries : mediaTypes.entrySet()) {
/*  60 */         String extension = ((String)entries.getKey()).toLowerCase(Locale.ENGLISH);
/*  61 */         MediaType mediaType = (MediaType)entries.getValue();
/*  62 */         this.mediaTypes.put(extension, mediaType);
/*  63 */         this.fileExtensions.add(mediaType, extension);
/*  64 */         this.allFileExtensions.add(extension);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Map<String, MediaType> getMediaTypes()
/*     */   {
/*  71 */     return this.mediaTypes;
/*     */   }
/*     */   
/*     */   protected List<MediaType> getAllMediaTypes() {
/*  75 */     return new ArrayList(this.mediaTypes.values());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void addMapping(String extension, MediaType mediaType)
/*     */   {
/*  82 */     MediaType previous = (MediaType)this.mediaTypes.putIfAbsent(extension, mediaType);
/*  83 */     if (previous == null) {
/*  84 */       this.fileExtensions.add(mediaType, extension);
/*  85 */       this.allFileExtensions.add(extension);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public List<String> resolveFileExtensions(MediaType mediaType)
/*     */   {
/*  92 */     List<String> fileExtensions = (List)this.fileExtensions.get(mediaType);
/*  93 */     return fileExtensions != null ? fileExtensions : Collections.emptyList();
/*     */   }
/*     */   
/*     */   public List<String> getAllFileExtensions()
/*     */   {
/*  98 */     return Collections.unmodifiableList(this.allFileExtensions);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected MediaType lookupMediaType(String extension)
/*     */   {
/* 106 */     return (MediaType)this.mediaTypes.get(extension.toLowerCase(Locale.ENGLISH));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\accept\MappingMediaTypeFileExtensionResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */