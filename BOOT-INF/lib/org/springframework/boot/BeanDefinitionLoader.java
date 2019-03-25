/*     */ package org.springframework.boot;
/*     */ 
/*     */ import groovy.lang.Closure;
/*     */ import java.io.IOException;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionReader;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*     */ import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
/*     */ import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
/*     */ import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.io.ClassPathResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
/*     */ import org.springframework.core.io.support.ResourcePatternResolver;
/*     */ import org.springframework.core.type.filter.AbstractTypeHierarchyTraversingFilter;
/*     */ import org.springframework.stereotype.Component;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class BeanDefinitionLoader
/*     */ {
/*     */   private final Object[] sources;
/*     */   private final AnnotatedBeanDefinitionReader annotatedReader;
/*     */   private final XmlBeanDefinitionReader xmlReader;
/*     */   private BeanDefinitionReader groovyReader;
/*     */   private final ClassPathBeanDefinitionScanner scanner;
/*     */   private ResourceLoader resourceLoader;
/*     */   
/*     */   BeanDefinitionLoader(BeanDefinitionRegistry registry, Object... sources)
/*     */   {
/*  78 */     Assert.notNull(registry, "Registry must not be null");
/*  79 */     Assert.notEmpty(sources, "Sources must not be empty");
/*  80 */     this.sources = sources;
/*  81 */     this.annotatedReader = new AnnotatedBeanDefinitionReader(registry);
/*  82 */     this.xmlReader = new XmlBeanDefinitionReader(registry);
/*  83 */     if (isGroovyPresent()) {
/*  84 */       this.groovyReader = new GroovyBeanDefinitionReader(registry);
/*     */     }
/*  86 */     this.scanner = new ClassPathBeanDefinitionScanner(registry);
/*  87 */     this.scanner.addExcludeFilter(new ClassExcludeFilter(sources));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator)
/*     */   {
/*  95 */     this.annotatedReader.setBeanNameGenerator(beanNameGenerator);
/*  96 */     this.xmlReader.setBeanNameGenerator(beanNameGenerator);
/*  97 */     this.scanner.setBeanNameGenerator(beanNameGenerator);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setResourceLoader(ResourceLoader resourceLoader)
/*     */   {
/* 105 */     this.resourceLoader = resourceLoader;
/* 106 */     this.xmlReader.setResourceLoader(resourceLoader);
/* 107 */     this.scanner.setResourceLoader(resourceLoader);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEnvironment(ConfigurableEnvironment environment)
/*     */   {
/* 115 */     this.annotatedReader.setEnvironment(environment);
/* 116 */     this.xmlReader.setEnvironment(environment);
/* 117 */     this.scanner.setEnvironment(environment);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int load()
/*     */   {
/* 125 */     int count = 0;
/* 126 */     for (Object source : this.sources) {
/* 127 */       count += load(source);
/*     */     }
/* 129 */     return count;
/*     */   }
/*     */   
/*     */   private int load(Object source) {
/* 133 */     Assert.notNull(source, "Source must not be null");
/* 134 */     if ((source instanceof Class)) {
/* 135 */       return load((Class)source);
/*     */     }
/* 137 */     if ((source instanceof Resource)) {
/* 138 */       return load((Resource)source);
/*     */     }
/* 140 */     if ((source instanceof Package)) {
/* 141 */       return load((Package)source);
/*     */     }
/* 143 */     if ((source instanceof CharSequence)) {
/* 144 */       return load((CharSequence)source);
/*     */     }
/* 146 */     throw new IllegalArgumentException("Invalid source type " + source.getClass());
/*     */   }
/*     */   
/*     */   private int load(Class<?> source) {
/* 150 */     if (isGroovyPresent())
/*     */     {
/* 152 */       if (GroovyBeanDefinitionSource.class.isAssignableFrom(source)) {
/* 153 */         GroovyBeanDefinitionSource loader = (GroovyBeanDefinitionSource)BeanUtils.instantiateClass(source, GroovyBeanDefinitionSource.class);
/*     */         
/* 155 */         load(loader);
/*     */       }
/*     */     }
/* 158 */     if (isComponent(source)) {
/* 159 */       this.annotatedReader.register(new Class[] { source });
/* 160 */       return 1;
/*     */     }
/* 162 */     return 0;
/*     */   }
/*     */   
/*     */   private int load(GroovyBeanDefinitionSource source) {
/* 166 */     int before = this.xmlReader.getRegistry().getBeanDefinitionCount();
/* 167 */     ((GroovyBeanDefinitionReader)this.groovyReader).beans(source.getBeans());
/* 168 */     int after = this.xmlReader.getRegistry().getBeanDefinitionCount();
/* 169 */     return after - before;
/*     */   }
/*     */   
/*     */   private int load(Resource source) {
/* 173 */     if (source.getFilename().endsWith(".groovy")) {
/* 174 */       if (this.groovyReader == null) {
/* 175 */         throw new BeanDefinitionStoreException("Cannot load Groovy beans without Groovy on classpath");
/*     */       }
/*     */       
/* 178 */       return this.groovyReader.loadBeanDefinitions(source);
/*     */     }
/* 180 */     return this.xmlReader.loadBeanDefinitions(source);
/*     */   }
/*     */   
/*     */   private int load(Package source) {
/* 184 */     return this.scanner.scan(new String[] { source.getName() });
/*     */   }
/*     */   
/*     */   private int load(CharSequence source)
/*     */   {
/* 189 */     String resolvedSource = this.xmlReader.getEnvironment().resolvePlaceholders(source.toString());
/*     */     try
/*     */     {
/* 192 */       return load(ClassUtils.forName(resolvedSource, null));
/*     */     }
/*     */     catch (IllegalArgumentException localIllegalArgumentException) {}catch (ClassNotFoundException localClassNotFoundException) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 201 */     Resource[] resources = findResources(resolvedSource);
/* 202 */     int loadCount = 0;
/* 203 */     boolean atLeastOneResourceExists = false;
/* 204 */     for (Resource resource : resources) {
/* 205 */       if (isLoadCandidate(resource)) {
/* 206 */         atLeastOneResourceExists = true;
/* 207 */         loadCount += load(resource);
/*     */       }
/*     */     }
/* 210 */     if (atLeastOneResourceExists) {
/* 211 */       return loadCount;
/*     */     }
/*     */     
/* 214 */     Package packageResource = findPackage(resolvedSource);
/* 215 */     if (packageResource != null) {
/* 216 */       return load(packageResource);
/*     */     }
/* 218 */     throw new IllegalArgumentException("Invalid source '" + resolvedSource + "'");
/*     */   }
/*     */   
/*     */   private boolean isGroovyPresent() {
/* 222 */     return ClassUtils.isPresent("groovy.lang.MetaClass", null);
/*     */   }
/*     */   
/*     */   private Resource[] findResources(String source) {
/* 226 */     ResourceLoader loader = this.resourceLoader != null ? this.resourceLoader : new PathMatchingResourcePatternResolver();
/*     */     try
/*     */     {
/* 229 */       if ((loader instanceof ResourcePatternResolver)) {
/* 230 */         return ((ResourcePatternResolver)loader).getResources(source);
/*     */       }
/* 232 */       return new Resource[] { loader.getResource(source) };
/*     */     }
/*     */     catch (IOException ex) {
/* 235 */       throw new IllegalStateException("Error reading source '" + source + "'");
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isLoadCandidate(Resource resource) {
/* 240 */     if ((resource == null) || (!resource.exists())) {
/* 241 */       return false;
/*     */     }
/* 243 */     if ((resource instanceof ClassPathResource))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 248 */       String path = ((ClassPathResource)resource).getPath();
/* 249 */       if (path.indexOf(".") == -1) {
/*     */         try {
/* 251 */           return Package.getPackage(path) == null;
/*     */         }
/*     */         catch (Exception localException) {}
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 258 */     return true;
/*     */   }
/*     */   
/*     */   private Package findPackage(CharSequence source) {
/* 262 */     Package pkg = Package.getPackage(source.toString());
/* 263 */     if (pkg != null) {
/* 264 */       return pkg;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 269 */       ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(getClass().getClassLoader());
/* 270 */       Resource[] resources = resolver.getResources(
/* 271 */         ClassUtils.convertClassNameToResourcePath(source.toString()) + "/*.class");
/*     */       
/* 273 */       Resource[] arrayOfResource1 = resources;int i = arrayOfResource1.length;int j = 0; if (j < i) { Resource resource = arrayOfResource1[j];
/*     */         
/* 275 */         String className = StringUtils.stripFilenameExtension(resource.getFilename());
/* 276 */         load(Class.forName(source.toString() + "." + className));
/*     */       }
/*     */     }
/*     */     catch (Exception localException) {}
/*     */     
/*     */ 
/*     */ 
/* 283 */     return Package.getPackage(source.toString());
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean isComponent(Class<?> type)
/*     */   {
/* 289 */     if (AnnotationUtils.findAnnotation(type, Component.class) != null) {
/* 290 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 294 */     if ((type.getName().matches(".*\\$_.*closure.*")) || (type.isAnonymousClass()) || 
/* 295 */       (type.getConstructors() == null) || (type.getConstructors().length == 0)) {
/* 296 */       return false;
/*     */     }
/* 298 */     return true;
/*     */   }
/*     */   
/*     */   protected static abstract interface GroovyBeanDefinitionSource
/*     */   {
/*     */     public abstract Closure<?> getBeans();
/*     */   }
/*     */   
/*     */   private static class ClassExcludeFilter extends AbstractTypeHierarchyTraversingFilter
/*     */   {
/* 308 */     private final Set<String> classNames = new HashSet();
/*     */     
/*     */     ClassExcludeFilter(Object... sources) {
/* 311 */       super(false);
/* 312 */       for (Object source : sources) {
/* 313 */         if ((source instanceof Class)) {
/* 314 */           this.classNames.add(((Class)source).getName());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     protected boolean matchClassName(String className)
/*     */     {
/* 321 */       return this.classNames.contains(className);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\BeanDefinitionLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */