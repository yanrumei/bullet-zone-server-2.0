/*     */ package org.springframework.boot.bind;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.env.MutablePropertySources;
/*     */ import org.springframework.core.env.PropertySource;
/*     */ import org.springframework.core.env.PropertySources;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.validation.BindException;
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
/*     */ public class PropertySourcesBinder
/*     */ {
/*     */   private PropertySources propertySources;
/*     */   private ConversionService conversionService;
/*     */   
/*     */   public PropertySourcesBinder(PropertySources propertySources)
/*     */   {
/*  48 */     this.propertySources = propertySources;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PropertySourcesBinder(PropertySource<?> propertySource)
/*     */   {
/*  56 */     this(createPropertySources(propertySource));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PropertySourcesBinder(ConfigurableEnvironment environment)
/*     */   {
/*  64 */     this(environment.getPropertySources());
/*     */   }
/*     */   
/*     */   public void setPropertySources(PropertySources propertySources) {
/*  68 */     this.propertySources = propertySources;
/*     */   }
/*     */   
/*     */   public PropertySources getPropertySources() {
/*  72 */     return this.propertySources;
/*     */   }
/*     */   
/*     */   public void setConversionService(ConversionService conversionService) {
/*  76 */     this.conversionService = conversionService;
/*     */   }
/*     */   
/*     */   public ConversionService getConversionService() {
/*  80 */     return this.conversionService;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<String, Object> extractAll(String prefix)
/*     */   {
/*  91 */     Map<String, Object> content = new LinkedHashMap();
/*  92 */     bindTo(prefix, content);
/*  93 */     return content;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void bindTo(String prefix, Object target)
/*     */   {
/* 104 */     PropertiesConfigurationFactory<Object> factory = new PropertiesConfigurationFactory(target);
/*     */     
/* 106 */     if (StringUtils.hasText(prefix)) {
/* 107 */       factory.setTargetName(prefix);
/*     */     }
/* 109 */     if (this.conversionService != null) {
/* 110 */       factory.setConversionService(this.conversionService);
/*     */     }
/* 112 */     factory.setPropertySources(this.propertySources);
/*     */     try {
/* 114 */       factory.bindPropertiesToTarget();
/*     */     }
/*     */     catch (BindException ex) {
/* 117 */       throw new IllegalStateException("Cannot bind to " + target, ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private static PropertySources createPropertySources(PropertySource<?> propertySource)
/*     */   {
/* 123 */     MutablePropertySources propertySources = new MutablePropertySources();
/* 124 */     propertySources.addLast(propertySource);
/* 125 */     return propertySources;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\bind\PropertySourcesBinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */