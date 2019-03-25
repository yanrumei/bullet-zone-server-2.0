/*     */ package org.springframework.boot.builder;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*     */ import org.springframework.boot.Banner;
/*     */ import org.springframework.boot.Banner.Mode;
/*     */ import org.springframework.boot.SpringApplication;
/*     */ import org.springframework.context.ApplicationContextInitializer;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.io.ResourceLoader;
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
/*     */ 
/*     */ public class SpringApplicationBuilder
/*     */ {
/*     */   private final SpringApplication application;
/*     */   private ConfigurableApplicationContext context;
/*     */   private SpringApplicationBuilder parent;
/*  72 */   private final AtomicBoolean running = new AtomicBoolean(false);
/*     */   
/*  74 */   private final Set<Object> sources = new LinkedHashSet();
/*     */   
/*  76 */   private final Map<String, Object> defaultProperties = new LinkedHashMap();
/*     */   
/*     */   private ConfigurableEnvironment environment;
/*     */   
/*  80 */   private Set<String> additionalProfiles = new LinkedHashSet();
/*     */   
/*     */   private boolean registerShutdownHookApplied;
/*     */   
/*  84 */   private boolean configuredAsChild = false;
/*     */   
/*     */   public SpringApplicationBuilder(Object... sources) {
/*  87 */     this.application = createSpringApplication(sources);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SpringApplication createSpringApplication(Object... sources)
/*     */   {
/*  99 */     return new SpringApplication(sources);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConfigurableApplicationContext context()
/*     */   {
/* 107 */     return this.context;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpringApplication application()
/*     */   {
/* 115 */     return this.application;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConfigurableApplicationContext run(String... args)
/*     */   {
/* 126 */     if (this.running.get())
/*     */     {
/* 128 */       return this.context;
/*     */     }
/* 130 */     configureAsChildIfNecessary(args);
/* 131 */     if (this.running.compareAndSet(false, true)) {
/* 132 */       synchronized (this.running)
/*     */       {
/* 134 */         this.context = build().run(args);
/*     */       }
/*     */     }
/* 137 */     return this.context;
/*     */   }
/*     */   
/*     */   private void configureAsChildIfNecessary(String... args) {
/* 141 */     if ((this.parent != null) && (!this.configuredAsChild)) {
/* 142 */       this.configuredAsChild = true;
/* 143 */       if (!this.registerShutdownHookApplied) {
/* 144 */         this.application.setRegisterShutdownHook(false);
/*     */       }
/* 146 */       initializers(new ApplicationContextInitializer[] { new ParentContextApplicationContextInitializer(this.parent
/* 147 */         .run(args)) });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpringApplication build()
/*     */   {
/* 156 */     return build(new String[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpringApplication build(String... args)
/*     */   {
/* 166 */     configureAsChildIfNecessary(args);
/* 167 */     this.application.setSources(this.sources);
/* 168 */     return this.application;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpringApplicationBuilder child(Object... sources)
/*     */   {
/* 178 */     SpringApplicationBuilder child = new SpringApplicationBuilder(new Object[0]);
/* 179 */     child.sources(sources);
/*     */     
/*     */ 
/* 182 */     child.properties(this.defaultProperties).environment(this.environment)
/* 183 */       .additionalProfiles(this.additionalProfiles);
/* 184 */     child.parent = this;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 189 */     web(false);
/*     */     
/*     */ 
/* 192 */     bannerMode(Banner.Mode.OFF);
/*     */     
/*     */ 
/* 195 */     this.application.setSources(this.sources);
/*     */     
/* 197 */     return child;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpringApplicationBuilder parent(Object... sources)
/*     */   {
/* 207 */     if (this.parent == null)
/*     */     {
/* 209 */       this.parent = new SpringApplicationBuilder(sources).web(false).properties(this.defaultProperties).environment(this.environment);
/*     */     }
/*     */     else {
/* 212 */       this.parent.sources(sources);
/*     */     }
/* 214 */     return this.parent;
/*     */   }
/*     */   
/*     */   private SpringApplicationBuilder runAndExtractParent(String... args) {
/* 218 */     if (this.context == null) {
/* 219 */       run(args);
/*     */     }
/* 221 */     if (this.parent != null) {
/* 222 */       return this.parent;
/*     */     }
/* 224 */     throw new IllegalStateException("No parent defined yet (please use the other overloaded parent methods to set one)");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpringApplicationBuilder parent(ConfigurableApplicationContext parent)
/*     */   {
/* 234 */     this.parent = new SpringApplicationBuilder(new Object[0]);
/* 235 */     this.parent.context = parent;
/* 236 */     this.parent.running.set(true);
/* 237 */     return this;
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
/*     */   public SpringApplicationBuilder sibling(Object... sources)
/*     */   {
/* 250 */     return runAndExtractParent(new String[0]).child(sources);
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
/*     */   public SpringApplicationBuilder sibling(Object[] sources, String... args)
/*     */   {
/* 263 */     return runAndExtractParent(args).child(sources);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpringApplicationBuilder contextClass(Class<? extends ConfigurableApplicationContext> cls)
/*     */   {
/* 273 */     this.application.setApplicationContextClass(cls);
/* 274 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpringApplicationBuilder sources(Object... sources)
/*     */   {
/* 283 */     this.sources.addAll(new LinkedHashSet(Arrays.asList(sources)));
/* 284 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpringApplicationBuilder sources(Class<?>... sources)
/*     */   {
/* 293 */     this.sources.addAll(new LinkedHashSet(Arrays.asList(sources)));
/* 294 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpringApplicationBuilder web(boolean webEnvironment)
/*     */   {
/* 304 */     this.application.setWebEnvironment(webEnvironment);
/* 305 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpringApplicationBuilder logStartupInfo(boolean logStartupInfo)
/*     */   {
/* 314 */     this.application.setLogStartupInfo(logStartupInfo);
/* 315 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpringApplicationBuilder banner(Banner banner)
/*     */   {
/* 325 */     this.application.setBanner(banner);
/* 326 */     return this;
/*     */   }
/*     */   
/*     */   public SpringApplicationBuilder bannerMode(Banner.Mode bannerMode) {
/* 330 */     this.application.setBannerMode(bannerMode);
/* 331 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpringApplicationBuilder headless(boolean headless)
/*     */   {
/* 341 */     this.application.setHeadless(headless);
/* 342 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpringApplicationBuilder registerShutdownHook(boolean registerShutdownHook)
/*     */   {
/* 352 */     this.registerShutdownHookApplied = true;
/* 353 */     this.application.setRegisterShutdownHook(registerShutdownHook);
/* 354 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpringApplicationBuilder main(Class<?> mainApplicationClass)
/*     */   {
/* 363 */     this.application.setMainApplicationClass(mainApplicationClass);
/* 364 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpringApplicationBuilder addCommandLineProperties(boolean addCommandLineProperties)
/*     */   {
/* 374 */     this.application.setAddCommandLineProperties(addCommandLineProperties);
/* 375 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpringApplicationBuilder properties(String... defaultProperties)
/*     */   {
/* 385 */     return properties(getMapFromKeyValuePairs(defaultProperties));
/*     */   }
/*     */   
/*     */   private Map<String, Object> getMapFromKeyValuePairs(String[] properties) {
/* 389 */     Map<String, Object> map = new HashMap();
/* 390 */     for (String property : properties) {
/* 391 */       int index = lowestIndexOf(property, new String[] { ":", "=" });
/* 392 */       String key = property.substring(0, index > 0 ? index : property.length());
/* 393 */       String value = index > 0 ? property.substring(index + 1) : "";
/* 394 */       map.put(key, value);
/*     */     }
/* 396 */     return map;
/*     */   }
/*     */   
/*     */   private int lowestIndexOf(String property, String... candidates) {
/* 400 */     int index = -1;
/* 401 */     for (String candidate : candidates) {
/* 402 */       int candidateIndex = property.indexOf(candidate);
/* 403 */       if (candidateIndex > 0) {
/* 404 */         index = index == -1 ? candidateIndex : Math.min(index, candidateIndex);
/*     */       }
/*     */     }
/* 407 */     return index;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpringApplicationBuilder properties(Properties defaultProperties)
/*     */   {
/* 417 */     return properties(getMapFromProperties(defaultProperties));
/*     */   }
/*     */   
/*     */   private Map<String, Object> getMapFromProperties(Properties properties) {
/* 421 */     HashMap<String, Object> map = new HashMap();
/* 422 */     for (Object key : Collections.list(properties.propertyNames())) {
/* 423 */       map.put((String)key, properties.get(key));
/*     */     }
/* 425 */     return map;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpringApplicationBuilder properties(Map<String, Object> defaults)
/*     */   {
/* 436 */     this.defaultProperties.putAll(defaults);
/* 437 */     this.application.setDefaultProperties(this.defaultProperties);
/* 438 */     if (this.parent != null) {
/* 439 */       this.parent.properties(this.defaultProperties);
/* 440 */       this.parent.environment(this.environment);
/*     */     }
/* 442 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpringApplicationBuilder profiles(String... profiles)
/*     */   {
/* 451 */     this.additionalProfiles.addAll(Arrays.asList(profiles));
/* 452 */     this.application.setAdditionalProfiles(
/* 453 */       (String[])this.additionalProfiles.toArray(new String[this.additionalProfiles.size()]));
/* 454 */     return this;
/*     */   }
/*     */   
/*     */   private SpringApplicationBuilder additionalProfiles(Collection<String> additionalProfiles)
/*     */   {
/* 459 */     this.additionalProfiles = new LinkedHashSet(additionalProfiles);
/* 460 */     this.application.setAdditionalProfiles(
/* 461 */       (String[])this.additionalProfiles.toArray(new String[this.additionalProfiles.size()]));
/* 462 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpringApplicationBuilder beanNameGenerator(BeanNameGenerator beanNameGenerator)
/*     */   {
/* 473 */     this.application.setBeanNameGenerator(beanNameGenerator);
/* 474 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpringApplicationBuilder environment(ConfigurableEnvironment environment)
/*     */   {
/* 483 */     this.application.setEnvironment(environment);
/* 484 */     this.environment = environment;
/* 485 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpringApplicationBuilder resourceLoader(ResourceLoader resourceLoader)
/*     */   {
/* 495 */     this.application.setResourceLoader(resourceLoader);
/* 496 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpringApplicationBuilder initializers(ApplicationContextInitializer<?>... initializers)
/*     */   {
/* 507 */     this.application.addInitializers(initializers);
/* 508 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpringApplicationBuilder listeners(ApplicationListener<?>... listeners)
/*     */   {
/* 520 */     this.application.addListeners(listeners);
/* 521 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\builder\SpringApplicationBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */