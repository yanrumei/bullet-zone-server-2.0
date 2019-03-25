/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.beans.PropertyEditorSupport;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import org.springframework.core.env.PropertyResolver;
/*     */ import org.springframework.core.env.StandardEnvironment;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceEditor
/*     */   extends PropertyEditorSupport
/*     */ {
/*     */   private final ResourceLoader resourceLoader;
/*     */   private PropertyResolver propertyResolver;
/*     */   private final boolean ignoreUnresolvablePlaceholders;
/*     */   
/*     */   public ResourceEditor()
/*     */   {
/*  63 */     this(new DefaultResourceLoader(), null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResourceEditor(ResourceLoader resourceLoader, PropertyResolver propertyResolver)
/*     */   {
/*  73 */     this(resourceLoader, propertyResolver, true);
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
/*     */   public ResourceEditor(ResourceLoader resourceLoader, PropertyResolver propertyResolver, boolean ignoreUnresolvablePlaceholders)
/*     */   {
/*  87 */     Assert.notNull(resourceLoader, "ResourceLoader must not be null");
/*  88 */     this.resourceLoader = resourceLoader;
/*  89 */     this.propertyResolver = propertyResolver;
/*  90 */     this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setAsText(String text)
/*     */   {
/*  96 */     if (StringUtils.hasText(text)) {
/*  97 */       String locationToUse = resolvePath(text).trim();
/*  98 */       setValue(this.resourceLoader.getResource(locationToUse));
/*     */     }
/*     */     else {
/* 101 */       setValue(null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String resolvePath(String path)
/*     */   {
/* 114 */     if (this.propertyResolver == null) {
/* 115 */       this.propertyResolver = new StandardEnvironment();
/*     */     }
/* 117 */     return this.ignoreUnresolvablePlaceholders ? this.propertyResolver.resolvePlaceholders(path) : this.propertyResolver
/* 118 */       .resolveRequiredPlaceholders(path);
/*     */   }
/*     */   
/*     */ 
/*     */   public String getAsText()
/*     */   {
/* 124 */     Resource value = (Resource)getValue();
/*     */     try
/*     */     {
/* 127 */       return value != null ? value.getURL().toExternalForm() : "";
/*     */     }
/*     */     catch (IOException ex) {}
/*     */     
/*     */ 
/* 132 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\io\ResourceEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */