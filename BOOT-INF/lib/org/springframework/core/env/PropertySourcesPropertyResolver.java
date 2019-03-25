/*     */ package org.springframework.core.env;
/*     */ 
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.core.convert.ConversionException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertySourcesPropertyResolver
/*     */   extends AbstractPropertyResolver
/*     */ {
/*     */   private final PropertySources propertySources;
/*     */   
/*     */   public PropertySourcesPropertyResolver(PropertySources propertySources)
/*     */   {
/*  43 */     this.propertySources = propertySources;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean containsProperty(String key)
/*     */   {
/*  49 */     if (this.propertySources != null) {
/*  50 */       for (PropertySource<?> propertySource : this.propertySources) {
/*  51 */         if (propertySource.containsProperty(key)) {
/*  52 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*  56 */     return false;
/*     */   }
/*     */   
/*     */   public String getProperty(String key)
/*     */   {
/*  61 */     return (String)getProperty(key, String.class, true);
/*     */   }
/*     */   
/*     */   public <T> T getProperty(String key, Class<T> targetValueType)
/*     */   {
/*  66 */     return (T)getProperty(key, targetValueType, true);
/*     */   }
/*     */   
/*     */   protected String getPropertyAsRawString(String key)
/*     */   {
/*  71 */     return (String)getProperty(key, String.class, false);
/*     */   }
/*     */   
/*     */   protected <T> T getProperty(String key, Class<T> targetValueType, boolean resolveNestedPlaceholders) {
/*  75 */     if (this.propertySources != null) {
/*  76 */       for (PropertySource<?> propertySource : this.propertySources) {
/*  77 */         if (this.logger.isTraceEnabled()) {
/*  78 */           this.logger.trace("Searching for key '" + key + "' in PropertySource '" + propertySource
/*  79 */             .getName() + "'");
/*     */         }
/*  81 */         Object value = propertySource.getProperty(key);
/*  82 */         if (value != null) {
/*  83 */           if ((resolveNestedPlaceholders) && ((value instanceof String))) {
/*  84 */             value = resolveNestedPlaceholders((String)value);
/*     */           }
/*  86 */           logKeyFound(key, propertySource, value);
/*  87 */           return (T)convertValueIfNecessary(value, targetValueType);
/*     */         }
/*     */       }
/*     */     }
/*  91 */     if (this.logger.isDebugEnabled()) {
/*  92 */       this.logger.debug("Could not find key '" + key + "' in any property source");
/*     */     }
/*  94 */     return null;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public <T> Class<T> getPropertyAsClass(String key, Class<T> targetValueType)
/*     */   {
/* 100 */     if (this.propertySources != null) {
/* 101 */       for (PropertySource<?> propertySource : this.propertySources) {
/* 102 */         if (this.logger.isTraceEnabled()) {
/* 103 */           this.logger.trace(String.format("Searching for key '%s' in [%s]", new Object[] { key, propertySource.getName() }));
/*     */         }
/* 105 */         Object value = propertySource.getProperty(key);
/* 106 */         if (value != null) {
/* 107 */           logKeyFound(key, propertySource, value);
/*     */           Class<?> clazz;
/* 109 */           if ((value instanceof String)) {
/*     */             try {
/* 111 */               clazz = ClassUtils.forName((String)value, null);
/*     */             } catch (Exception ex) {
/*     */               Class<?> clazz;
/* 114 */               throw new ClassConversionException((String)value, targetValueType, ex);
/*     */             }
/*     */           } else { Class<?> clazz;
/* 117 */             if ((value instanceof Class)) {
/* 118 */               clazz = (Class)value;
/*     */             }
/*     */             else
/* 121 */               clazz = value.getClass();
/*     */           }
/* 123 */           if (!targetValueType.isAssignableFrom(clazz)) {
/* 124 */             throw new ClassConversionException(clazz, targetValueType);
/*     */           }
/*     */           
/* 127 */           Class<T> targetClass = clazz;
/* 128 */           return targetClass;
/*     */         }
/*     */       }
/*     */     }
/* 132 */     if (this.logger.isDebugEnabled()) {
/* 133 */       this.logger.debug(String.format("Could not find key '%s' in any property source", new Object[] { key }));
/*     */     }
/* 135 */     return null;
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
/*     */ 
/*     */   protected void logKeyFound(String key, PropertySource<?> propertySource, Object value)
/*     */   {
/* 151 */     if (this.logger.isDebugEnabled()) {
/* 152 */       this.logger.debug("Found key '" + key + "' in PropertySource '" + propertySource.getName() + "' with value of type " + value
/* 153 */         .getClass().getSimpleName());
/*     */     }
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   private static class ClassConversionException
/*     */     extends ConversionException
/*     */   {
/*     */     public ClassConversionException(Class<?> actual, Class<?> expected)
/*     */     {
/* 163 */       super();
/*     */     }
/*     */     
/*     */     public ClassConversionException(String actual, Class<?> expected, Exception ex) {
/* 168 */       super(ex);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\env\PropertySourcesPropertyResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */