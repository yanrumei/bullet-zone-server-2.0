/*     */ package org.springframework.boot.autoconfigure.template;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.boot.bind.RelaxedPropertyResolver;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.io.support.SpringFactoriesLoader;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class TemplateAvailabilityProviders
/*     */ {
/*     */   private final List<TemplateAvailabilityProvider> providers;
/*     */   private static final int CACHE_LIMIT = 1024;
/*  47 */   private static final TemplateAvailabilityProvider NONE = new NoTemplateAvailabilityProvider(null);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  52 */   private final Map<String, TemplateAvailabilityProvider> resolved = new ConcurrentHashMap(1024);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  58 */   private final Map<String, TemplateAvailabilityProvider> cache = new LinkedHashMap(1024, 0.75F, true)
/*     */   {
/*     */ 
/*     */ 
/*     */     protected boolean removeEldestEntry(Map.Entry<String, TemplateAvailabilityProvider> eldest)
/*     */     {
/*     */ 
/*  65 */       if (size() > 1024) {
/*  66 */         TemplateAvailabilityProviders.this.resolved.remove(eldest.getKey());
/*  67 */         return true;
/*     */       }
/*  69 */       return false;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TemplateAvailabilityProviders(ApplicationContext applicationContext)
/*     */   {
/*  79 */     this(applicationContext == null ? null : applicationContext.getClassLoader());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TemplateAvailabilityProviders(ClassLoader classLoader)
/*     */   {
/*  87 */     Assert.notNull(classLoader, "ClassLoader must not be null");
/*     */     
/*  89 */     this.providers = SpringFactoriesLoader.loadFactories(TemplateAvailabilityProvider.class, classLoader);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected TemplateAvailabilityProviders(Collection<? extends TemplateAvailabilityProvider> providers)
/*     */   {
/*  98 */     Assert.notNull(providers, "Providers must not be null");
/*  99 */     this.providers = new ArrayList(providers);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<TemplateAvailabilityProvider> getProviders()
/*     */   {
/* 107 */     return this.providers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TemplateAvailabilityProvider getProvider(String view, ApplicationContext applicationContext)
/*     */   {
/* 118 */     Assert.notNull(applicationContext, "ApplicationContext must not be null");
/* 119 */     return getProvider(view, applicationContext.getEnvironment(), applicationContext
/* 120 */       .getClassLoader(), applicationContext);
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
/*     */   public TemplateAvailabilityProvider getProvider(String view, Environment environment, ClassLoader classLoader, ResourceLoader resourceLoader)
/*     */   {
/* 133 */     Assert.notNull(view, "View must not be null");
/* 134 */     Assert.notNull(environment, "Environment must not be null");
/* 135 */     Assert.notNull(classLoader, "ClassLoader must not be null");
/* 136 */     Assert.notNull(resourceLoader, "ResourceLoader must not be null");
/*     */     
/* 138 */     RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(environment, "spring.template.provider.");
/*     */     
/* 140 */     if (!((Boolean)propertyResolver.getProperty("cache", Boolean.class, Boolean.valueOf(true))).booleanValue()) {
/* 141 */       return findProvider(view, environment, classLoader, resourceLoader);
/*     */     }
/* 143 */     TemplateAvailabilityProvider provider = (TemplateAvailabilityProvider)this.resolved.get(view);
/* 144 */     if (provider == null) {
/* 145 */       synchronized (this.cache) {
/* 146 */         provider = findProvider(view, environment, classLoader, resourceLoader);
/* 147 */         provider = provider == null ? NONE : provider;
/* 148 */         this.resolved.put(view, provider);
/* 149 */         this.cache.put(view, provider);
/*     */       }
/*     */     }
/* 152 */     return provider == NONE ? null : provider;
/*     */   }
/*     */   
/*     */ 
/*     */   private TemplateAvailabilityProvider findProvider(String view, Environment environment, ClassLoader classLoader, ResourceLoader resourceLoader)
/*     */   {
/* 158 */     for (TemplateAvailabilityProvider candidate : this.providers) {
/* 159 */       if (candidate.isTemplateAvailable(view, environment, classLoader, resourceLoader))
/*     */       {
/* 161 */         return candidate;
/*     */       }
/*     */     }
/* 164 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   private static class NoTemplateAvailabilityProvider
/*     */     implements TemplateAvailabilityProvider
/*     */   {
/*     */     public boolean isTemplateAvailable(String view, Environment environment, ClassLoader classLoader, ResourceLoader resourceLoader)
/*     */     {
/* 173 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\template\TemplateAvailabilityProviders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */