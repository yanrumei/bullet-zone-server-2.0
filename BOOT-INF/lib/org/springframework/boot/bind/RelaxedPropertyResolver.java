/*     */ package org.springframework.boot.bind;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.env.PropertyResolver;
/*     */ import org.springframework.core.env.PropertySourcesPropertyResolver;
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
/*     */ public class RelaxedPropertyResolver
/*     */   implements PropertyResolver
/*     */ {
/*     */   private final PropertyResolver resolver;
/*     */   private final String prefix;
/*     */   
/*     */   public RelaxedPropertyResolver(PropertyResolver resolver)
/*     */   {
/*  40 */     this(resolver, null);
/*     */   }
/*     */   
/*     */   public RelaxedPropertyResolver(PropertyResolver resolver, String prefix) {
/*  44 */     Assert.notNull(resolver, "PropertyResolver must not be null");
/*  45 */     this.resolver = resolver;
/*  46 */     this.prefix = (prefix == null ? "" : prefix);
/*     */   }
/*     */   
/*     */   public String getRequiredProperty(String key) throws IllegalStateException
/*     */   {
/*  51 */     return (String)getRequiredProperty(key, String.class);
/*     */   }
/*     */   
/*     */   public <T> T getRequiredProperty(String key, Class<T> targetType)
/*     */     throws IllegalStateException
/*     */   {
/*  57 */     T value = getProperty(key, targetType);
/*  58 */     Assert.state(value != null, String.format("required key [%s] not found", new Object[] { key }));
/*  59 */     return value;
/*     */   }
/*     */   
/*     */   public String getProperty(String key)
/*     */   {
/*  64 */     return (String)getProperty(key, String.class, null);
/*     */   }
/*     */   
/*     */   public String getProperty(String key, String defaultValue)
/*     */   {
/*  69 */     return (String)getProperty(key, String.class, defaultValue);
/*     */   }
/*     */   
/*     */   public <T> T getProperty(String key, Class<T> targetType)
/*     */   {
/*  74 */     return (T)getProperty(key, targetType, null);
/*     */   }
/*     */   
/*     */   public <T> T getProperty(String key, Class<T> targetType, T defaultValue)
/*     */   {
/*  79 */     RelaxedNames prefixes = new RelaxedNames(this.prefix);
/*  80 */     RelaxedNames keys = new RelaxedNames(key);
/*  81 */     for (Iterator localIterator1 = prefixes.iterator(); localIterator1.hasNext();) { prefix = (String)localIterator1.next();
/*  82 */       for (String relaxedKey : keys) {
/*  83 */         if (this.resolver.containsProperty(prefix + relaxedKey))
/*  84 */           return (T)this.resolver.getProperty(prefix + relaxedKey, targetType);
/*     */       }
/*     */     }
/*     */     String prefix;
/*  88 */     return defaultValue;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public <T> Class<T> getPropertyAsClass(String key, Class<T> targetType)
/*     */   {
/*  94 */     RelaxedNames prefixes = new RelaxedNames(this.prefix);
/*  95 */     RelaxedNames keys = new RelaxedNames(key);
/*  96 */     for (Iterator localIterator1 = prefixes.iterator(); localIterator1.hasNext();) { prefix = (String)localIterator1.next();
/*  97 */       for (String relaxedKey : keys) {
/*  98 */         if (this.resolver.containsProperty(prefix + relaxedKey)) {
/*  99 */           return this.resolver.getPropertyAsClass(prefix + relaxedKey, targetType);
/*     */         }
/*     */       }
/*     */     }
/*     */     String prefix;
/* 104 */     return null;
/*     */   }
/*     */   
/*     */   public boolean containsProperty(String key)
/*     */   {
/* 109 */     RelaxedNames prefixes = new RelaxedNames(this.prefix);
/* 110 */     RelaxedNames keys = new RelaxedNames(key);
/* 111 */     for (Iterator localIterator1 = prefixes.iterator(); localIterator1.hasNext();) { prefix = (String)localIterator1.next();
/* 112 */       for (String relaxedKey : keys) {
/* 113 */         if (this.resolver.containsProperty(prefix + relaxedKey))
/* 114 */           return true;
/*     */       }
/*     */     }
/*     */     String prefix;
/* 118 */     return false;
/*     */   }
/*     */   
/*     */   public String resolvePlaceholders(String text)
/*     */   {
/* 123 */     throw new UnsupportedOperationException("Unable to resolve placeholders with relaxed properties");
/*     */   }
/*     */   
/*     */ 
/*     */   public String resolveRequiredPlaceholders(String text)
/*     */     throws IllegalArgumentException
/*     */   {
/* 130 */     throw new UnsupportedOperationException("Unable to resolve placeholders with relaxed properties");
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
/*     */   public Map<String, Object> getSubProperties(String keyPrefix)
/*     */   {
/* 143 */     Assert.isInstanceOf(ConfigurableEnvironment.class, this.resolver, "SubProperties not available.");
/*     */     
/* 145 */     ConfigurableEnvironment env = (ConfigurableEnvironment)this.resolver;
/* 146 */     return PropertySourceUtils.getSubProperties(env.getPropertySources(), this.prefix, keyPrefix);
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
/*     */   public static RelaxedPropertyResolver ignoringUnresolvableNestedPlaceholders(Environment environment, String prefix)
/*     */   {
/* 160 */     Assert.notNull(environment, "Environment must not be null");
/* 161 */     PropertyResolver resolver = environment;
/* 162 */     if ((environment instanceof ConfigurableEnvironment))
/*     */     {
/* 164 */       resolver = new PropertySourcesPropertyResolver(((ConfigurableEnvironment)environment).getPropertySources());
/* 165 */       ((PropertySourcesPropertyResolver)resolver)
/* 166 */         .setIgnoreUnresolvableNestedPlaceholders(true);
/*     */     }
/* 168 */     return new RelaxedPropertyResolver(resolver, prefix);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\bind\RelaxedPropertyResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */