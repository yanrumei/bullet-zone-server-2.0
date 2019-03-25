/*     */ package org.springframework.boot;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.env.MutablePropertySources;
/*     */ import org.springframework.core.env.PropertySource;
/*     */ import org.springframework.core.env.StandardEnvironment;
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
/*     */ final class EnvironmentConverter
/*     */ {
/*     */   private static final String CONFIGURABLE_WEB_ENVIRONMENT_CLASS = "org.springframework.web.context.ConfigurableWebEnvironment";
/*     */   private static final Set<String> SERVLET_ENVIRONMENT_SOURCE_NAMES;
/*     */   private final ClassLoader classLoader;
/*     */   
/*     */   static
/*     */   {
/*  45 */     Set<String> names = new HashSet();
/*  46 */     names.add("servletContextInitParams");
/*  47 */     names.add("servletConfigInitParams");
/*  48 */     names.add("jndiProperties");
/*  49 */     SERVLET_ENVIRONMENT_SOURCE_NAMES = Collections.unmodifiableSet(names);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   EnvironmentConverter(ClassLoader classLoader)
/*     */   {
/*  60 */     this.classLoader = classLoader;
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
/*     */   StandardEnvironment convertToStandardEnvironmentIfNecessary(ConfigurableEnvironment environment)
/*     */   {
/*  73 */     if (((environment instanceof StandardEnvironment)) && 
/*  74 */       (!isWebEnvironment(environment, this.classLoader))) {
/*  75 */       return (StandardEnvironment)environment;
/*     */     }
/*  77 */     return convertToStandardEnvironment(environment);
/*     */   }
/*     */   
/*     */   private boolean isWebEnvironment(ConfigurableEnvironment environment, ClassLoader classLoader)
/*     */   {
/*     */     try
/*     */     {
/*  84 */       Class<?> webEnvironmentClass = ClassUtils.forName("org.springframework.web.context.ConfigurableWebEnvironment", classLoader);
/*  85 */       return webEnvironmentClass.isInstance(environment);
/*     */     }
/*     */     catch (Throwable ex) {}
/*  88 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   private StandardEnvironment convertToStandardEnvironment(ConfigurableEnvironment environment)
/*     */   {
/*  94 */     StandardEnvironment result = new StandardEnvironment();
/*  95 */     result.setActiveProfiles(environment.getActiveProfiles());
/*  96 */     result.setConversionService(environment.getConversionService());
/*  97 */     copyNonServletPropertySources(environment, result);
/*  98 */     return result;
/*     */   }
/*     */   
/*     */   private void copyNonServletPropertySources(ConfigurableEnvironment source, StandardEnvironment target)
/*     */   {
/* 103 */     removeAllPropertySources(target.getPropertySources());
/* 104 */     for (PropertySource<?> propertySource : source.getPropertySources()) {
/* 105 */       if (!SERVLET_ENVIRONMENT_SOURCE_NAMES.contains(propertySource.getName())) {
/* 106 */         target.getPropertySources().addLast(propertySource);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void removeAllPropertySources(MutablePropertySources propertySources) {
/* 112 */     Set<String> names = new HashSet();
/* 113 */     for (PropertySource<?> propertySource : propertySources) {
/* 114 */       names.add(propertySource.getName());
/*     */     }
/* 116 */     for (String name : names) {
/* 117 */       propertySources.remove(name);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\EnvironmentConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */