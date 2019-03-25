/*     */ package org.springframework.boot.autoconfigure.thymeleaf;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
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
/*     */ @ConfigurationProperties(prefix="spring.thymeleaf")
/*     */ public class ThymeleafProperties
/*     */ {
/*  33 */   private static final Charset DEFAULT_ENCODING = Charset.forName("UTF-8");
/*     */   
/*  35 */   private static final MimeType DEFAULT_CONTENT_TYPE = MimeType.valueOf("text/html");
/*     */   
/*     */ 
/*     */   public static final String DEFAULT_PREFIX = "classpath:/templates/";
/*     */   
/*     */ 
/*     */   public static final String DEFAULT_SUFFIX = ".html";
/*     */   
/*     */ 
/*  44 */   private boolean checkTemplate = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  49 */   private boolean checkTemplateLocation = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  54 */   private String prefix = "classpath:/templates/";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  59 */   private String suffix = ".html";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  64 */   private String mode = "HTML5";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  69 */   private Charset encoding = DEFAULT_ENCODING;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  74 */   private MimeType contentType = DEFAULT_CONTENT_TYPE;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  79 */   private boolean cache = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Integer templateResolverOrder;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String[] viewNames;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String[] excludedViewNames;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 101 */   private boolean enabled = true;
/*     */   
/*     */   public boolean isEnabled() {
/* 104 */     return this.enabled;
/*     */   }
/*     */   
/*     */   public void setEnabled(boolean enabled) {
/* 108 */     this.enabled = enabled;
/*     */   }
/*     */   
/*     */   public boolean isCheckTemplate() {
/* 112 */     return this.checkTemplate;
/*     */   }
/*     */   
/*     */   public void setCheckTemplate(boolean checkTemplate) {
/* 116 */     this.checkTemplate = checkTemplate;
/*     */   }
/*     */   
/*     */   public boolean isCheckTemplateLocation() {
/* 120 */     return this.checkTemplateLocation;
/*     */   }
/*     */   
/*     */   public void setCheckTemplateLocation(boolean checkTemplateLocation) {
/* 124 */     this.checkTemplateLocation = checkTemplateLocation;
/*     */   }
/*     */   
/*     */   public String getPrefix() {
/* 128 */     return this.prefix;
/*     */   }
/*     */   
/*     */   public void setPrefix(String prefix) {
/* 132 */     this.prefix = prefix;
/*     */   }
/*     */   
/*     */   public String getSuffix() {
/* 136 */     return this.suffix;
/*     */   }
/*     */   
/*     */   public void setSuffix(String suffix) {
/* 140 */     this.suffix = suffix;
/*     */   }
/*     */   
/*     */   public String getMode() {
/* 144 */     return this.mode;
/*     */   }
/*     */   
/*     */   public void setMode(String mode) {
/* 148 */     this.mode = mode;
/*     */   }
/*     */   
/*     */   public Charset getEncoding() {
/* 152 */     return this.encoding;
/*     */   }
/*     */   
/*     */   public void setEncoding(Charset encoding) {
/* 156 */     this.encoding = encoding;
/*     */   }
/*     */   
/*     */   public MimeType getContentType() {
/* 160 */     return this.contentType;
/*     */   }
/*     */   
/*     */   public void setContentType(MimeType contentType) {
/* 164 */     this.contentType = contentType;
/*     */   }
/*     */   
/*     */   public boolean isCache() {
/* 168 */     return this.cache;
/*     */   }
/*     */   
/*     */   public void setCache(boolean cache) {
/* 172 */     this.cache = cache;
/*     */   }
/*     */   
/*     */   public Integer getTemplateResolverOrder() {
/* 176 */     return this.templateResolverOrder;
/*     */   }
/*     */   
/*     */   public void setTemplateResolverOrder(Integer templateResolverOrder) {
/* 180 */     this.templateResolverOrder = templateResolverOrder;
/*     */   }
/*     */   
/*     */   public String[] getExcludedViewNames() {
/* 184 */     return this.excludedViewNames;
/*     */   }
/*     */   
/*     */   public void setExcludedViewNames(String[] excludedViewNames) {
/* 188 */     this.excludedViewNames = excludedViewNames;
/*     */   }
/*     */   
/*     */   public String[] getViewNames() {
/* 192 */     return this.viewNames;
/*     */   }
/*     */   
/*     */   public void setViewNames(String[] viewNames) {
/* 196 */     this.viewNames = viewNames;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\thymeleaf\ThymeleafProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */