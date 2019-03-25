/*     */ package org.springframework.boot.autoconfigure.template;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.boot.bind.PropertySourcesPropertyValues;
/*     */ import org.springframework.boot.bind.RelaxedDataBinder;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class PathBasedTemplateAvailabilityProvider
/*     */   implements TemplateAvailabilityProvider
/*     */ {
/*     */   private final String className;
/*     */   private final Class<? extends TemplateAvailabilityProperties> propertiesClass;
/*     */   private final String propertyPrefix;
/*     */   
/*     */   public PathBasedTemplateAvailabilityProvider(String className, Class<? extends TemplateAvailabilityProperties> propertiesClass, String propertyPrefix)
/*     */   {
/*  49 */     this.className = className;
/*  50 */     this.propertiesClass = propertiesClass;
/*  51 */     this.propertyPrefix = propertyPrefix;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isTemplateAvailable(String view, Environment environment, ClassLoader classLoader, ResourceLoader resourceLoader)
/*     */   {
/*  57 */     if (ClassUtils.isPresent(this.className, classLoader))
/*     */     {
/*  59 */       TemplateAvailabilityProperties properties = (TemplateAvailabilityProperties)BeanUtils.instantiateClass(this.propertiesClass);
/*  60 */       RelaxedDataBinder binder = new RelaxedDataBinder(properties, this.propertyPrefix);
/*     */       
/*  62 */       binder.bind(new PropertySourcesPropertyValues(((ConfigurableEnvironment)environment)
/*  63 */         .getPropertySources()));
/*  64 */       return isTemplateAvailable(view, resourceLoader, properties);
/*     */     }
/*  66 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isTemplateAvailable(String view, ResourceLoader resourceLoader, TemplateAvailabilityProperties properties)
/*     */   {
/*  71 */     String location = properties.getPrefix() + view + properties.getSuffix();
/*  72 */     for (String path : properties.getLoaderPath()) {
/*  73 */       if (resourceLoader.getResource(path + location).exists()) {
/*  74 */         return true;
/*     */       }
/*     */     }
/*  77 */     return false;
/*     */   }
/*     */   
/*     */   protected static abstract class TemplateAvailabilityProperties
/*     */   {
/*     */     private String prefix;
/*     */     private String suffix;
/*     */     
/*     */     protected TemplateAvailabilityProperties(String prefix, String suffix)
/*     */     {
/*  87 */       this.prefix = prefix;
/*  88 */       this.suffix = suffix;
/*     */     }
/*     */     
/*     */     protected abstract List<String> getLoaderPath();
/*     */     
/*     */     public String getPrefix() {
/*  94 */       return this.prefix;
/*     */     }
/*     */     
/*     */     public void setPrefix(String prefix) {
/*  98 */       this.prefix = prefix;
/*     */     }
/*     */     
/*     */     public String getSuffix() {
/* 102 */       return this.suffix;
/*     */     }
/*     */     
/*     */     public void setSuffix(String suffix) {
/* 106 */       this.suffix = suffix;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\template\PathBasedTemplateAvailabilityProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */