/*     */ package org.springframework.boot.env;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.env.MutablePropertySources;
/*     */ import org.springframework.core.env.PropertySource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.support.SpringFactoriesLoader;
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
/*     */ public class PropertySourcesLoader
/*     */ {
/*  43 */   private static final Log logger = LogFactory.getLog(PropertySourcesLoader.class);
/*     */   
/*     */ 
/*     */   private final MutablePropertySources propertySources;
/*     */   
/*     */ 
/*     */   private final List<PropertySourceLoader> loaders;
/*     */   
/*     */ 
/*     */   public PropertySourcesLoader()
/*     */   {
/*  54 */     this(new MutablePropertySources());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PropertySourcesLoader(MutablePropertySources propertySources)
/*     */   {
/*  63 */     Assert.notNull(propertySources, "PropertySources must not be null");
/*  64 */     this.propertySources = propertySources;
/*  65 */     this.loaders = SpringFactoriesLoader.loadFactories(PropertySourceLoader.class, 
/*  66 */       getClass().getClassLoader());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PropertySource<?> load(Resource resource)
/*     */     throws IOException
/*     */   {
/*  76 */     return load(resource, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PropertySource<?> load(Resource resource, String profile)
/*     */     throws IOException
/*     */   {
/*  88 */     return load(resource, resource.getDescription(), profile);
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
/*     */   public PropertySource<?> load(Resource resource, String name, String profile)
/*     */     throws IOException
/*     */   {
/* 102 */     return load(resource, null, name, profile);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PropertySource<?> load(Resource resource, String group, String name, String profile)
/*     */     throws IOException
/*     */   {
/*     */     String sourceName;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 123 */     if (isFile(resource)) {
/* 124 */       sourceName = generatePropertySourceName(name, profile);
/* 125 */       for (PropertySourceLoader loader : this.loaders) {
/* 126 */         if (canLoadFileExtension(loader, resource)) {
/* 127 */           PropertySource<?> specific = loader.load(sourceName, resource, profile);
/*     */           
/* 129 */           addPropertySource(group, specific, profile);
/* 130 */           return specific;
/*     */         }
/*     */       }
/*     */     }
/* 134 */     return null;
/*     */   }
/*     */   
/*     */   private boolean isFile(Resource resource) {
/* 138 */     return (resource != null) && (resource.exists()) && 
/* 139 */       (StringUtils.hasText(StringUtils.getFilenameExtension(resource.getFilename())));
/*     */   }
/*     */   
/*     */   private String generatePropertySourceName(String name, String profile) {
/* 143 */     return name + "#" + profile;
/*     */   }
/*     */   
/*     */   private boolean canLoadFileExtension(PropertySourceLoader loader, Resource resource) {
/* 147 */     String filename = resource.getFilename().toLowerCase();
/* 148 */     for (String extension : loader.getFileExtensions()) {
/* 149 */       if (filename.endsWith("." + extension.toLowerCase())) {
/* 150 */         return true;
/*     */       }
/*     */     }
/* 153 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   private void addPropertySource(String basename, PropertySource<?> source, String profile)
/*     */   {
/* 159 */     if (source == null) {
/* 160 */       return;
/*     */     }
/*     */     
/* 163 */     if (basename == null) {
/* 164 */       this.propertySources.addLast(source);
/* 165 */       return;
/*     */     }
/*     */     
/* 168 */     EnumerableCompositePropertySource group = getGeneric(basename);
/* 169 */     group.add(source);
/* 170 */     logger.trace("Adding PropertySource: " + source + " in group: " + basename);
/* 171 */     if (this.propertySources.contains(group.getName())) {
/* 172 */       this.propertySources.replace(group.getName(), group);
/*     */     }
/*     */     else {
/* 175 */       this.propertySources.addFirst(group);
/*     */     }
/*     */   }
/*     */   
/*     */   private EnumerableCompositePropertySource getGeneric(String name)
/*     */   {
/* 181 */     PropertySource<?> source = this.propertySources.get(name);
/* 182 */     if ((source instanceof EnumerableCompositePropertySource)) {
/* 183 */       return (EnumerableCompositePropertySource)source;
/*     */     }
/* 185 */     EnumerableCompositePropertySource composite = new EnumerableCompositePropertySource(name);
/*     */     
/* 187 */     return composite;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MutablePropertySources getPropertySources()
/*     */   {
/* 195 */     return this.propertySources;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<String> getAllFileExtensions()
/*     */   {
/* 203 */     Set<String> fileExtensions = new LinkedHashSet();
/* 204 */     for (PropertySourceLoader loader : this.loaders) {
/* 205 */       fileExtensions.addAll(Arrays.asList(loader.getFileExtensions()));
/*     */     }
/* 207 */     return fileExtensions;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\env\PropertySourcesLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */