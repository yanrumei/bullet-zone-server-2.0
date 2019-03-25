/*     */ package org.springframework.boot;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.boot.ansi.AnsiPropertySource;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.env.MapPropertySource;
/*     */ import org.springframework.core.env.MutablePropertySources;
/*     */ import org.springframework.core.env.PropertyResolver;
/*     */ import org.springframework.core.env.PropertySourcesPropertyResolver;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StreamUtils;
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
/*     */ public class ResourceBanner
/*     */   implements Banner
/*     */ {
/*  49 */   private static final Log logger = LogFactory.getLog(ResourceBanner.class);
/*     */   private Resource resource;
/*     */   
/*     */   public ResourceBanner(Resource resource)
/*     */   {
/*  54 */     Assert.notNull(resource, "Resource must not be null");
/*  55 */     Assert.isTrue(resource.exists(), "Resource must exist");
/*  56 */     this.resource = resource;
/*     */   }
/*     */   
/*     */   public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out)
/*     */   {
/*     */     try
/*     */     {
/*  63 */       String banner = StreamUtils.copyToString(this.resource.getInputStream(), 
/*  64 */         (Charset)environment.getProperty("banner.charset", Charset.class, 
/*  65 */         Charset.forName("UTF-8")));
/*     */       
/*  67 */       for (PropertyResolver resolver : getPropertyResolvers(environment, sourceClass))
/*     */       {
/*  69 */         banner = resolver.resolvePlaceholders(banner);
/*     */       }
/*  71 */       out.println(banner);
/*     */     }
/*     */     catch (Exception ex) {
/*  74 */       logger.warn("Banner not printable: " + this.resource + " (" + ex.getClass() + ": '" + ex
/*  75 */         .getMessage() + "')", ex);
/*     */     }
/*     */   }
/*     */   
/*     */   protected List<PropertyResolver> getPropertyResolvers(Environment environment, Class<?> sourceClass)
/*     */   {
/*  81 */     List<PropertyResolver> resolvers = new ArrayList();
/*  82 */     resolvers.add(environment);
/*  83 */     resolvers.add(getVersionResolver(sourceClass));
/*  84 */     resolvers.add(getAnsiResolver());
/*  85 */     resolvers.add(getTitleResolver(sourceClass));
/*  86 */     return resolvers;
/*     */   }
/*     */   
/*     */   private PropertyResolver getVersionResolver(Class<?> sourceClass) {
/*  90 */     MutablePropertySources propertySources = new MutablePropertySources();
/*  91 */     propertySources
/*  92 */       .addLast(new MapPropertySource("version", getVersionsMap(sourceClass)));
/*  93 */     return new PropertySourcesPropertyResolver(propertySources);
/*     */   }
/*     */   
/*     */   private Map<String, Object> getVersionsMap(Class<?> sourceClass) {
/*  97 */     String appVersion = getApplicationVersion(sourceClass);
/*  98 */     String bootVersion = getBootVersion();
/*  99 */     Map<String, Object> versions = new HashMap();
/* 100 */     versions.put("application.version", getVersionString(appVersion, false));
/* 101 */     versions.put("spring-boot.version", getVersionString(bootVersion, false));
/* 102 */     versions.put("application.formatted-version", getVersionString(appVersion, true));
/* 103 */     versions.put("spring-boot.formatted-version", 
/* 104 */       getVersionString(bootVersion, true));
/* 105 */     return versions;
/*     */   }
/*     */   
/*     */   protected String getApplicationVersion(Class<?> sourceClass) {
/* 109 */     Package sourcePackage = sourceClass == null ? null : sourceClass.getPackage();
/* 110 */     return sourcePackage == null ? null : sourcePackage.getImplementationVersion();
/*     */   }
/*     */   
/*     */   protected String getBootVersion() {
/* 114 */     return SpringBootVersion.getVersion();
/*     */   }
/*     */   
/*     */   private String getVersionString(String version, boolean format) {
/* 118 */     if (version == null) {
/* 119 */       return "";
/*     */     }
/* 121 */     return format ? " (v" + version + ")" : version;
/*     */   }
/*     */   
/*     */   private PropertyResolver getAnsiResolver() {
/* 125 */     MutablePropertySources sources = new MutablePropertySources();
/* 126 */     sources.addFirst(new AnsiPropertySource("ansi", true));
/* 127 */     return new PropertySourcesPropertyResolver(sources);
/*     */   }
/*     */   
/*     */   private PropertyResolver getTitleResolver(Class<?> sourceClass) {
/* 131 */     MutablePropertySources sources = new MutablePropertySources();
/* 132 */     String applicationTitle = getApplicationTitle(sourceClass);
/* 133 */     Map<String, Object> titleMap = Collections.singletonMap("application.title", applicationTitle == null ? "" : applicationTitle);
/*     */     
/* 135 */     sources.addFirst(new MapPropertySource("title", titleMap));
/* 136 */     return new PropertySourcesPropertyResolver(sources);
/*     */   }
/*     */   
/*     */   protected String getApplicationTitle(Class<?> sourceClass) {
/* 140 */     Package sourcePackage = sourceClass == null ? null : sourceClass.getPackage();
/* 141 */     return sourcePackage == null ? null : sourcePackage.getImplementationTitle();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\ResourceBanner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */