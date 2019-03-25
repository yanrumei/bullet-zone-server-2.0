/*     */ package org.springframework.boot.bind;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.PropertyValue;
/*     */ import org.springframework.beans.PropertyValues;
/*     */ import org.springframework.core.env.CompositePropertySource;
/*     */ import org.springframework.core.env.EnumerablePropertySource;
/*     */ import org.springframework.core.env.PropertySource;
/*     */ import org.springframework.core.env.PropertySources;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertySourcesPropertyValues
/*     */   implements PropertyValues
/*     */ {
/*  47 */   private static final Pattern COLLECTION_PROPERTY = Pattern.compile("\\[(\\d+)\\](\\.\\S+)?");
/*     */   
/*     */   private final PropertySources propertySources;
/*     */   
/*     */   private final Collection<String> nonEnumerableFallbackNames;
/*     */   
/*     */   private final PropertyNamePatternsMatcher includes;
/*     */   
/*  55 */   private final Map<String, PropertyValue> propertyValues = new LinkedHashMap();
/*     */   
/*  57 */   private final ConcurrentHashMap<String, PropertySource<?>> collectionOwners = new ConcurrentHashMap();
/*     */   
/*     */ 
/*     */   private final boolean resolvePlaceholders;
/*     */   
/*     */ 
/*     */ 
/*     */   public PropertySourcesPropertyValues(PropertySources propertySources)
/*     */   {
/*  66 */     this(propertySources, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PropertySourcesPropertyValues(PropertySources propertySources, boolean resolvePlaceholders)
/*     */   {
/*  78 */     this(propertySources, (Collection)null, PropertyNamePatternsMatcher.ALL, resolvePlaceholders);
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
/*     */ 
/*     */   public PropertySourcesPropertyValues(PropertySources propertySources, Collection<String> includePatterns, Collection<String> nonEnumerableFallbackNames)
/*     */   {
/*  93 */     this(propertySources, nonEnumerableFallbackNames, new PatternPropertyNamePatternsMatcher(includePatterns), true);
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
/*     */ 
/*     */   PropertySourcesPropertyValues(PropertySources propertySources, Collection<String> nonEnumerableFallbackNames, PropertyNamePatternsMatcher includes, boolean resolvePlaceholders)
/*     */   {
/* 108 */     Assert.notNull(propertySources, "PropertySources must not be null");
/* 109 */     Assert.notNull(includes, "Includes must not be null");
/* 110 */     this.propertySources = propertySources;
/* 111 */     this.nonEnumerableFallbackNames = nonEnumerableFallbackNames;
/* 112 */     this.includes = includes;
/* 113 */     this.resolvePlaceholders = resolvePlaceholders;
/* 114 */     PropertySourcesPropertyResolver resolver = new PropertySourcesPropertyResolver(propertySources);
/*     */     
/* 116 */     for (PropertySource<?> source : propertySources) {
/* 117 */       processPropertySource(source, resolver);
/*     */     }
/*     */   }
/*     */   
/*     */   private void processPropertySource(PropertySource<?> source, PropertySourcesPropertyResolver resolver)
/*     */   {
/* 123 */     if ((source instanceof CompositePropertySource)) {
/* 124 */       processCompositePropertySource((CompositePropertySource)source, resolver);
/*     */     }
/* 126 */     else if ((source instanceof EnumerablePropertySource)) {
/* 127 */       processEnumerablePropertySource((EnumerablePropertySource)source, resolver, this.includes);
/*     */     }
/*     */     else
/*     */     {
/* 131 */       processNonEnumerablePropertySource(source, resolver);
/*     */     }
/*     */   }
/*     */   
/*     */   private void processCompositePropertySource(CompositePropertySource source, PropertySourcesPropertyResolver resolver)
/*     */   {
/* 137 */     for (PropertySource<?> nested : source.getPropertySources()) {
/* 138 */       processPropertySource(nested, resolver);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void processEnumerablePropertySource(EnumerablePropertySource<?> source, PropertySourcesPropertyResolver resolver, PropertyNamePatternsMatcher includes)
/*     */   {
/* 145 */     if (source.getPropertyNames().length > 0) {
/* 146 */       for (String propertyName : source.getPropertyNames()) {
/* 147 */         if (includes.matches(propertyName)) {
/* 148 */           Object value = getEnumerableProperty(source, resolver, propertyName);
/* 149 */           putIfAbsent(propertyName, value, source);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private Object getEnumerableProperty(EnumerablePropertySource<?> source, PropertySourcesPropertyResolver resolver, String propertyName)
/*     */   {
/*     */     try {
/* 158 */       if (this.resolvePlaceholders) {
/* 159 */         return resolver.getProperty(propertyName, Object.class);
/*     */       }
/*     */     }
/*     */     catch (RuntimeException localRuntimeException) {}
/*     */     
/*     */ 
/* 165 */     return source.getProperty(propertyName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void processNonEnumerablePropertySource(PropertySource<?> source, PropertySourcesPropertyResolver resolver)
/*     */   {
/* 172 */     if (this.nonEnumerableFallbackNames == null) {
/* 173 */       return;
/*     */     }
/* 175 */     for (String propertyName : this.nonEnumerableFallbackNames) {
/* 176 */       if (source.containsProperty(propertyName))
/*     */       {
/*     */ 
/* 179 */         Object value = null;
/*     */         try {
/* 181 */           value = resolver.getProperty(propertyName, Object.class);
/*     */         }
/*     */         catch (RuntimeException localRuntimeException) {}
/*     */         
/*     */ 
/* 186 */         if (value == null) {
/* 187 */           value = source.getProperty(propertyName.toUpperCase());
/*     */         }
/* 189 */         putIfAbsent(propertyName, value, source);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public PropertyValue[] getPropertyValues() {
/* 195 */     Collection<PropertyValue> values = this.propertyValues.values();
/* 196 */     return (PropertyValue[])values.toArray(new PropertyValue[values.size()]);
/*     */   }
/*     */   
/*     */   public PropertyValue getPropertyValue(String propertyName)
/*     */   {
/* 201 */     PropertyValue propertyValue = (PropertyValue)this.propertyValues.get(propertyName);
/* 202 */     if (propertyValue != null) {
/* 203 */       return propertyValue;
/*     */     }
/* 205 */     for (PropertySource<?> source : this.propertySources) {
/* 206 */       Object value = source.getProperty(propertyName);
/* 207 */       propertyValue = putIfAbsent(propertyName, value, source);
/* 208 */       if (propertyValue != null) {
/* 209 */         return propertyValue;
/*     */       }
/*     */     }
/* 212 */     return null;
/*     */   }
/*     */   
/*     */   private PropertyValue putIfAbsent(String propertyName, Object value, PropertySource<?> source)
/*     */   {
/* 217 */     if ((value != null) && (!this.propertyValues.containsKey(propertyName))) {
/* 218 */       PropertySource<?> collectionOwner = (PropertySource)this.collectionOwners.putIfAbsent(COLLECTION_PROPERTY
/* 219 */         .matcher(propertyName).replaceAll("[]"), source);
/* 220 */       if ((collectionOwner == null) || (collectionOwner == source)) {
/* 221 */         PropertyValue propertyValue = new OriginCapablePropertyValue(propertyName, value, propertyName, source);
/*     */         
/* 223 */         this.propertyValues.put(propertyName, propertyValue);
/* 224 */         return propertyValue;
/*     */       }
/*     */     }
/* 227 */     return null;
/*     */   }
/*     */   
/*     */   public PropertyValues changesSince(PropertyValues old)
/*     */   {
/* 232 */     MutablePropertyValues changes = new MutablePropertyValues();
/*     */     
/* 234 */     for (PropertyValue newValue : getPropertyValues())
/*     */     {
/* 236 */       PropertyValue oldValue = old.getPropertyValue(newValue.getName());
/* 237 */       if ((oldValue == null) || (!oldValue.equals(newValue))) {
/* 238 */         changes.addPropertyValue(newValue);
/*     */       }
/*     */     }
/* 241 */     return changes;
/*     */   }
/*     */   
/*     */   public boolean contains(String propertyName)
/*     */   {
/* 246 */     return getPropertyValue(propertyName) != null;
/*     */   }
/*     */   
/*     */   public boolean isEmpty()
/*     */   {
/* 251 */     return this.propertyValues.isEmpty();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\bind\PropertySourcesPropertyValues.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */