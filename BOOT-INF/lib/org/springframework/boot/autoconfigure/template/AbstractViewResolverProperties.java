/*     */ package org.springframework.boot.autoconfigure.template;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.util.MimeType;
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
/*     */ public abstract class AbstractViewResolverProperties
/*     */ {
/*  37 */   private static final MimeType DEFAULT_CONTENT_TYPE = MimeType.valueOf("text/html");
/*     */   
/*  39 */   private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  44 */   private boolean enabled = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean cache;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  54 */   private MimeType contentType = DEFAULT_CONTENT_TYPE;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  59 */   private Charset charset = DEFAULT_CHARSET;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String[] viewNames;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  69 */   private boolean checkTemplateLocation = true;
/*     */   
/*     */   public void setEnabled(boolean enabled) {
/*  72 */     this.enabled = enabled;
/*     */   }
/*     */   
/*     */   public boolean isEnabled() {
/*  76 */     return this.enabled;
/*     */   }
/*     */   
/*     */   public void setCheckTemplateLocation(boolean checkTemplateLocation) {
/*  80 */     this.checkTemplateLocation = checkTemplateLocation;
/*     */   }
/*     */   
/*     */   public boolean isCheckTemplateLocation() {
/*  84 */     return this.checkTemplateLocation;
/*     */   }
/*     */   
/*     */   public String[] getViewNames() {
/*  88 */     return this.viewNames;
/*     */   }
/*     */   
/*     */   public void setViewNames(String[] viewNames) {
/*  92 */     this.viewNames = viewNames;
/*     */   }
/*     */   
/*     */   public boolean isCache() {
/*  96 */     return this.cache;
/*     */   }
/*     */   
/*     */   public void setCache(boolean cache) {
/* 100 */     this.cache = cache;
/*     */   }
/*     */   
/*     */   public MimeType getContentType() {
/* 104 */     if (this.contentType.getCharset() == null) {
/* 105 */       Map<String, String> parameters = new LinkedHashMap();
/* 106 */       parameters.put("charset", this.charset.name());
/* 107 */       parameters.putAll(this.contentType.getParameters());
/* 108 */       return new MimeType(this.contentType, parameters);
/*     */     }
/* 110 */     return this.contentType;
/*     */   }
/*     */   
/*     */   public void setContentType(MimeType contentType) {
/* 114 */     this.contentType = contentType;
/*     */   }
/*     */   
/*     */   public Charset getCharset() {
/* 118 */     return this.charset;
/*     */   }
/*     */   
/*     */   public String getCharsetName() {
/* 122 */     return this.charset != null ? this.charset.name() : null;
/*     */   }
/*     */   
/*     */   public void setCharset(Charset charset) {
/* 126 */     this.charset = charset;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\template\AbstractViewResolverProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */