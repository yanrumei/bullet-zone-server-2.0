/*     */ package org.springframework.boot.autoconfigure;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.Aware;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.boot.bind.RelaxedPropertyResolver;
/*     */ import org.springframework.context.EnvironmentAware;
/*     */ import org.springframework.context.ResourceLoaderAware;
/*     */ import org.springframework.context.annotation.DeferredImportSelector;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.io.support.SpringFactoriesLoader;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
/*     */ import org.springframework.core.type.classreading.MetadataReaderFactory;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class AutoConfigurationImportSelector
/*     */   implements DeferredImportSelector, BeanClassLoaderAware, ResourceLoaderAware, BeanFactoryAware, EnvironmentAware, Ordered
/*     */ {
/*  73 */   private static final String[] NO_IMPORTS = new String[0];
/*     */   
/*     */ 
/*  76 */   private static final Log logger = LogFactory.getLog(AutoConfigurationImportSelector.class);
/*     */   
/*     */   private ConfigurableListableBeanFactory beanFactory;
/*     */   
/*     */   private Environment environment;
/*     */   
/*     */   private ClassLoader beanClassLoader;
/*     */   
/*     */   private ResourceLoader resourceLoader;
/*     */   
/*     */   public String[] selectImports(AnnotationMetadata annotationMetadata)
/*     */   {
/*  88 */     if (!isEnabled(annotationMetadata)) {
/*  89 */       return NO_IMPORTS;
/*     */     }
/*     */     try
/*     */     {
/*  93 */       AutoConfigurationMetadata autoConfigurationMetadata = AutoConfigurationMetadataLoader.loadMetadata(this.beanClassLoader);
/*  94 */       AnnotationAttributes attributes = getAttributes(annotationMetadata);
/*  95 */       List<String> configurations = getCandidateConfigurations(annotationMetadata, attributes);
/*     */       
/*  97 */       configurations = removeDuplicates(configurations);
/*  98 */       configurations = sort(configurations, autoConfigurationMetadata);
/*  99 */       Set<String> exclusions = getExclusions(annotationMetadata, attributes);
/* 100 */       checkExcludedClasses(configurations, exclusions);
/* 101 */       configurations.removeAll(exclusions);
/* 102 */       configurations = filter(configurations, autoConfigurationMetadata);
/* 103 */       fireAutoConfigurationImportEvents(configurations, exclusions);
/* 104 */       return (String[])configurations.toArray(new String[configurations.size()]);
/*     */     }
/*     */     catch (IOException ex) {
/* 107 */       throw new IllegalStateException(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean isEnabled(AnnotationMetadata metadata) {
/* 112 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AnnotationAttributes getAttributes(AnnotationMetadata metadata)
/*     */   {
/* 123 */     String name = getAnnotationClass().getName();
/*     */     
/* 125 */     AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(name, true));
/* 126 */     Assert.notNull(attributes, "No auto-configuration attributes found. Is " + metadata
/* 127 */       .getClassName() + " annotated with " + 
/* 128 */       ClassUtils.getShortName(name) + "?");
/* 129 */     return attributes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Class<?> getAnnotationClass()
/*     */   {
/* 137 */     return EnableAutoConfiguration.class;
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
/*     */   protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes)
/*     */   {
/* 151 */     List<String> configurations = SpringFactoriesLoader.loadFactoryNames(
/* 152 */       getSpringFactoriesLoaderFactoryClass(), getBeanClassLoader());
/* 153 */     Assert.notEmpty(configurations, "No auto configuration classes found in META-INF/spring.factories. If you are using a custom packaging, make sure that file is correct.");
/*     */     
/*     */ 
/* 156 */     return configurations;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Class<?> getSpringFactoriesLoaderFactoryClass()
/*     */   {
/* 165 */     return EnableAutoConfiguration.class;
/*     */   }
/*     */   
/*     */   private void checkExcludedClasses(List<String> configurations, Set<String> exclusions)
/*     */   {
/* 170 */     List<String> invalidExcludes = new ArrayList(exclusions.size());
/* 171 */     for (String exclusion : exclusions) {
/* 172 */       if ((ClassUtils.isPresent(exclusion, getClass().getClassLoader())) && 
/* 173 */         (!configurations.contains(exclusion))) {
/* 174 */         invalidExcludes.add(exclusion);
/*     */       }
/*     */     }
/* 177 */     if (!invalidExcludes.isEmpty()) {
/* 178 */       handleInvalidExcludes(invalidExcludes);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void handleInvalidExcludes(List<String> invalidExcludes)
/*     */   {
/* 188 */     StringBuilder message = new StringBuilder();
/* 189 */     for (String exclude : invalidExcludes) {
/* 190 */       message.append("\t- ").append(exclude).append(String.format("%n", new Object[0]));
/*     */     }
/*     */     
/* 193 */     throw new IllegalStateException(String.format("The following classes could not be excluded because they are not auto-configuration classes:%n%s", new Object[] { message }));
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
/*     */   protected Set<String> getExclusions(AnnotationMetadata metadata, AnnotationAttributes attributes)
/*     */   {
/* 206 */     Set<String> excluded = new LinkedHashSet();
/* 207 */     excluded.addAll(asList(attributes, "exclude"));
/* 208 */     excluded.addAll(Arrays.asList(attributes.getStringArray("excludeName")));
/* 209 */     excluded.addAll(getExcludeAutoConfigurationsProperty());
/* 210 */     return excluded;
/*     */   }
/*     */   
/*     */   private List<String> getExcludeAutoConfigurationsProperty() {
/* 214 */     if ((getEnvironment() instanceof ConfigurableEnvironment)) {
/* 215 */       RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(this.environment, "spring.autoconfigure.");
/*     */       
/* 217 */       Map<String, Object> properties = resolver.getSubProperties("exclude");
/* 218 */       if (properties.isEmpty()) {
/* 219 */         return Collections.emptyList();
/*     */       }
/* 221 */       List<String> excludes = new ArrayList();
/* 222 */       for (Map.Entry<String, Object> entry : properties.entrySet()) {
/* 223 */         String name = (String)entry.getKey();
/* 224 */         Object value = entry.getValue();
/* 225 */         if ((name.isEmpty()) || ((name.startsWith("[")) && (value != null))) {
/* 226 */           excludes.addAll(new HashSet(Arrays.asList(
/* 227 */             StringUtils.tokenizeToStringArray(String.valueOf(value), ","))));
/*     */         }
/*     */       }
/* 230 */       return excludes;
/*     */     }
/* 232 */     RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(getEnvironment(), "spring.autoconfigure.");
/*     */     
/* 234 */     String[] exclude = (String[])resolver.getProperty("exclude", String[].class);
/* 235 */     return Arrays.asList(exclude == null ? new String[0] : exclude);
/*     */   }
/*     */   
/*     */   private List<String> sort(List<String> configurations, AutoConfigurationMetadata autoConfigurationMetadata)
/*     */     throws IOException
/*     */   {
/* 241 */     configurations = new AutoConfigurationSorter(getMetadataReaderFactory(), autoConfigurationMetadata).getInPriorityOrder(configurations);
/* 242 */     return configurations;
/*     */   }
/*     */   
/*     */   private List<String> filter(List<String> configurations, AutoConfigurationMetadata autoConfigurationMetadata)
/*     */   {
/* 247 */     long startTime = System.nanoTime();
/* 248 */     String[] candidates = (String[])configurations.toArray(new String[configurations.size()]);
/* 249 */     boolean[] skip = new boolean[candidates.length];
/* 250 */     boolean skipped = false;
/* 251 */     for (AutoConfigurationImportFilter filter : getAutoConfigurationImportFilters()) {
/* 252 */       invokeAwareMethods(filter);
/* 253 */       boolean[] match = filter.match(candidates, autoConfigurationMetadata);
/* 254 */       for (int i = 0; i < match.length; i++) {
/* 255 */         if (match[i] == 0) {
/* 256 */           skip[i] = true;
/* 257 */           skipped = true;
/*     */         }
/*     */       }
/*     */     }
/* 261 */     if (!skipped) {
/* 262 */       return configurations;
/*     */     }
/* 264 */     Object result = new ArrayList(candidates.length);
/* 265 */     for (int i = 0; i < candidates.length; i++) {
/* 266 */       if (skip[i] == 0) {
/* 267 */         ((List)result).add(candidates[i]);
/*     */       }
/*     */     }
/* 270 */     if (logger.isTraceEnabled()) {
/* 271 */       int numberFiltered = configurations.size() - ((List)result).size();
/* 272 */       logger.trace("Filtered " + numberFiltered + " auto configuration class in " + TimeUnit.NANOSECONDS
/* 273 */         .toMillis(System.nanoTime() - startTime) + " ms");
/*     */     }
/*     */     
/* 276 */     return new ArrayList((Collection)result);
/*     */   }
/*     */   
/*     */   protected List<AutoConfigurationImportFilter> getAutoConfigurationImportFilters() {
/* 280 */     return SpringFactoriesLoader.loadFactories(AutoConfigurationImportFilter.class, this.beanClassLoader);
/*     */   }
/*     */   
/*     */   private MetadataReaderFactory getMetadataReaderFactory()
/*     */   {
/*     */     try {
/* 286 */       return (MetadataReaderFactory)getBeanFactory().getBean("org.springframework.boot.autoconfigure.internalCachingMetadataReaderFactory", MetadataReaderFactory.class);
/*     */     }
/*     */     catch (NoSuchBeanDefinitionException ex) {}
/*     */     
/*     */ 
/* 291 */     return new CachingMetadataReaderFactory(this.resourceLoader);
/*     */   }
/*     */   
/*     */   protected final <T> List<T> removeDuplicates(List<T> list)
/*     */   {
/* 296 */     return new ArrayList(new LinkedHashSet(list));
/*     */   }
/*     */   
/*     */   protected final List<String> asList(AnnotationAttributes attributes, String name) {
/* 300 */     String[] value = attributes.getStringArray(name);
/* 301 */     return Arrays.asList(value == null ? new String[0] : value);
/*     */   }
/*     */   
/*     */   private void fireAutoConfigurationImportEvents(List<String> configurations, Set<String> exclusions)
/*     */   {
/* 306 */     List<AutoConfigurationImportListener> listeners = getAutoConfigurationImportListeners();
/* 307 */     AutoConfigurationImportEvent event; if (!listeners.isEmpty()) {
/* 308 */       event = new AutoConfigurationImportEvent(this, configurations, exclusions);
/*     */       
/* 310 */       for (AutoConfigurationImportListener listener : listeners) {
/* 311 */         invokeAwareMethods(listener);
/* 312 */         listener.onAutoConfigurationImportEvent(event);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected List<AutoConfigurationImportListener> getAutoConfigurationImportListeners() {
/* 318 */     return SpringFactoriesLoader.loadFactories(AutoConfigurationImportListener.class, this.beanClassLoader);
/*     */   }
/*     */   
/*     */   private void invokeAwareMethods(Object instance)
/*     */   {
/* 323 */     if ((instance instanceof Aware)) {
/* 324 */       if ((instance instanceof BeanClassLoaderAware))
/*     */       {
/* 326 */         ((BeanClassLoaderAware)instance).setBeanClassLoader(this.beanClassLoader);
/*     */       }
/* 328 */       if ((instance instanceof BeanFactoryAware)) {
/* 329 */         ((BeanFactoryAware)instance).setBeanFactory(this.beanFactory);
/*     */       }
/* 331 */       if ((instance instanceof EnvironmentAware)) {
/* 332 */         ((EnvironmentAware)instance).setEnvironment(this.environment);
/*     */       }
/* 334 */       if ((instance instanceof ResourceLoaderAware)) {
/* 335 */         ((ResourceLoaderAware)instance).setResourceLoader(this.resourceLoader);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) throws BeansException
/*     */   {
/* 342 */     Assert.isInstanceOf(ConfigurableListableBeanFactory.class, beanFactory);
/* 343 */     this.beanFactory = ((ConfigurableListableBeanFactory)beanFactory);
/*     */   }
/*     */   
/*     */   protected final ConfigurableListableBeanFactory getBeanFactory() {
/* 347 */     return this.beanFactory;
/*     */   }
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader)
/*     */   {
/* 352 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */   
/*     */   protected ClassLoader getBeanClassLoader() {
/* 356 */     return this.beanClassLoader;
/*     */   }
/*     */   
/*     */   public void setEnvironment(Environment environment)
/*     */   {
/* 361 */     this.environment = environment;
/*     */   }
/*     */   
/*     */   protected final Environment getEnvironment() {
/* 365 */     return this.environment;
/*     */   }
/*     */   
/*     */   public void setResourceLoader(ResourceLoader resourceLoader)
/*     */   {
/* 370 */     this.resourceLoader = resourceLoader;
/*     */   }
/*     */   
/*     */   protected final ResourceLoader getResourceLoader() {
/* 374 */     return this.resourceLoader;
/*     */   }
/*     */   
/*     */   public int getOrder()
/*     */   {
/* 379 */     return 2147483646;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\AutoConfigurationImportSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */