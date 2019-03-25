/*     */ package org.springframework.boot.autoconfigure.template;
/*     */ 
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.MimeType;
/*     */ import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
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
/*     */ public abstract class AbstractTemplateViewResolverProperties
/*     */   extends AbstractViewResolverProperties
/*     */ {
/*     */   private String prefix;
/*     */   private String suffix;
/*     */   private String requestContextAttribute;
/*  53 */   private boolean exposeRequestAttributes = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  59 */   private boolean exposeSessionAttributes = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  65 */   private boolean allowRequestOverride = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  71 */   private boolean exposeSpringMacroHelpers = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  77 */   private boolean allowSessionOverride = false;
/*     */   
/*     */   protected AbstractTemplateViewResolverProperties(String defaultPrefix, String defaultSuffix)
/*     */   {
/*  81 */     this.prefix = defaultPrefix;
/*  82 */     this.suffix = defaultSuffix;
/*     */   }
/*     */   
/*     */   public String getPrefix() {
/*  86 */     return this.prefix;
/*     */   }
/*     */   
/*     */   public void setPrefix(String prefix) {
/*  90 */     this.prefix = prefix;
/*     */   }
/*     */   
/*     */   public String getSuffix() {
/*  94 */     return this.suffix;
/*     */   }
/*     */   
/*     */   public void setSuffix(String suffix) {
/*  98 */     this.suffix = suffix;
/*     */   }
/*     */   
/*     */   public String getRequestContextAttribute() {
/* 102 */     return this.requestContextAttribute;
/*     */   }
/*     */   
/*     */   public void setRequestContextAttribute(String requestContextAttribute) {
/* 106 */     this.requestContextAttribute = requestContextAttribute;
/*     */   }
/*     */   
/*     */   public boolean isExposeRequestAttributes() {
/* 110 */     return this.exposeRequestAttributes;
/*     */   }
/*     */   
/*     */   public void setExposeRequestAttributes(boolean exposeRequestAttributes) {
/* 114 */     this.exposeRequestAttributes = exposeRequestAttributes;
/*     */   }
/*     */   
/*     */   public boolean isExposeSessionAttributes() {
/* 118 */     return this.exposeSessionAttributes;
/*     */   }
/*     */   
/*     */   public void setExposeSessionAttributes(boolean exposeSessionAttributes) {
/* 122 */     this.exposeSessionAttributes = exposeSessionAttributes;
/*     */   }
/*     */   
/*     */   public boolean isAllowRequestOverride() {
/* 126 */     return this.allowRequestOverride;
/*     */   }
/*     */   
/*     */   public void setAllowRequestOverride(boolean allowRequestOverride) {
/* 130 */     this.allowRequestOverride = allowRequestOverride;
/*     */   }
/*     */   
/*     */   public boolean isAllowSessionOverride() {
/* 134 */     return this.allowSessionOverride;
/*     */   }
/*     */   
/*     */   public void setAllowSessionOverride(boolean allowSessionOverride) {
/* 138 */     this.allowSessionOverride = allowSessionOverride;
/*     */   }
/*     */   
/*     */   public boolean isExposeSpringMacroHelpers() {
/* 142 */     return this.exposeSpringMacroHelpers;
/*     */   }
/*     */   
/*     */   public void setExposeSpringMacroHelpers(boolean exposeSpringMacroHelpers) {
/* 146 */     this.exposeSpringMacroHelpers = exposeSpringMacroHelpers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void applyToViewResolver(Object viewResolver)
/*     */   {
/* 156 */     Assert.isInstanceOf(AbstractTemplateViewResolver.class, viewResolver, "ViewResolver is not an instance of AbstractTemplateViewResolver :" + viewResolver);
/*     */     
/*     */ 
/* 159 */     AbstractTemplateViewResolver resolver = (AbstractTemplateViewResolver)viewResolver;
/* 160 */     resolver.setPrefix(getPrefix());
/* 161 */     resolver.setSuffix(getSuffix());
/* 162 */     resolver.setCache(isCache());
/* 163 */     if (getContentType() != null) {
/* 164 */       resolver.setContentType(getContentType().toString());
/*     */     }
/* 166 */     resolver.setViewNames(getViewNames());
/* 167 */     resolver.setExposeRequestAttributes(isExposeRequestAttributes());
/* 168 */     resolver.setAllowRequestOverride(isAllowRequestOverride());
/* 169 */     resolver.setAllowSessionOverride(isAllowSessionOverride());
/* 170 */     resolver.setExposeSessionAttributes(isExposeSessionAttributes());
/* 171 */     resolver.setExposeSpringMacroHelpers(isExposeSpringMacroHelpers());
/* 172 */     resolver.setRequestContextAttribute(getRequestContextAttribute());
/*     */     
/*     */ 
/* 175 */     resolver.setOrder(2147483642);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\template\AbstractTemplateViewResolverProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */