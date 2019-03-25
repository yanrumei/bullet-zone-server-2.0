/*     */ package org.springframework.core.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ public class AnnotationAttributes
/*     */   extends LinkedHashMap<String, Object>
/*     */ {
/*     */   private static final String UNKNOWN = "unknown";
/*     */   private final Class<? extends Annotation> annotationType;
/*     */   private final String displayName;
/*  57 */   boolean validated = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotationAttributes()
/*     */   {
/*  64 */     this.annotationType = null;
/*  65 */     this.displayName = "unknown";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotationAttributes(int initialCapacity)
/*     */   {
/*  74 */     super(initialCapacity);
/*  75 */     this.annotationType = null;
/*  76 */     this.displayName = "unknown";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotationAttributes(Class<? extends Annotation> annotationType)
/*     */   {
/*  87 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/*  88 */     this.annotationType = annotationType;
/*  89 */     this.displayName = annotationType.getName();
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
/*     */   public AnnotationAttributes(String annotationType, ClassLoader classLoader)
/*     */   {
/* 102 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/* 103 */     this.annotationType = getAnnotationType(annotationType, classLoader);
/* 104 */     this.displayName = annotationType;
/*     */   }
/*     */   
/*     */   private static Class<? extends Annotation> getAnnotationType(String annotationType, ClassLoader classLoader)
/*     */   {
/* 109 */     if (classLoader != null) {
/*     */       try {
/* 111 */         return classLoader.loadClass(annotationType);
/*     */       }
/*     */       catch (ClassNotFoundException localClassNotFoundException) {}
/*     */     }
/*     */     
/*     */ 
/* 117 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotationAttributes(Map<String, Object> map)
/*     */   {
/* 127 */     super(map);
/* 128 */     this.annotationType = null;
/* 129 */     this.displayName = "unknown";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotationAttributes(AnnotationAttributes other)
/*     */   {
/* 139 */     super(other);
/* 140 */     this.annotationType = other.annotationType;
/* 141 */     this.displayName = other.displayName;
/* 142 */     this.validated = other.validated;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<? extends Annotation> annotationType()
/*     */   {
/* 153 */     return this.annotationType;
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
/*     */   public String getString(String attributeName)
/*     */   {
/* 166 */     return (String)getRequiredAttribute(attributeName, String.class);
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
/*     */   @Deprecated
/*     */   public String getAliasedString(String attributeName, Class<? extends Annotation> annotationType, Object annotationSource)
/*     */   {
/* 196 */     return (String)getRequiredAttributeWithAlias(attributeName, annotationType, annotationSource, String.class);
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
/*     */   public String[] getStringArray(String attributeName)
/*     */   {
/* 212 */     return (String[])getRequiredAttribute(attributeName, String[].class);
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
/*     */   @Deprecated
/*     */   public String[] getAliasedStringArray(String attributeName, Class<? extends Annotation> annotationType, Object annotationSource)
/*     */   {
/* 242 */     return (String[])getRequiredAttributeWithAlias(attributeName, annotationType, annotationSource, String[].class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getBoolean(String attributeName)
/*     */   {
/* 254 */     return ((Boolean)getRequiredAttribute(attributeName, Boolean.class)).booleanValue();
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
/*     */   public <N extends Number> N getNumber(String attributeName)
/*     */   {
/* 267 */     return (Number)getRequiredAttribute(attributeName, Number.class);
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
/*     */   public <E extends Enum<?>> E getEnum(String attributeName)
/*     */   {
/* 280 */     return (Enum)getRequiredAttribute(attributeName, Enum.class);
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
/*     */   public <T> Class<? extends T> getClass(String attributeName)
/*     */   {
/* 293 */     return (Class)getRequiredAttribute(attributeName, Class.class);
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
/*     */   public Class<?>[] getClassArray(String attributeName)
/*     */   {
/* 308 */     return (Class[])getRequiredAttribute(attributeName, Class[].class);
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
/*     */   @Deprecated
/*     */   public Class<?>[] getAliasedClassArray(String attributeName, Class<? extends Annotation> annotationType, Object annotationSource)
/*     */   {
/* 338 */     return (Class[])getRequiredAttributeWithAlias(attributeName, annotationType, annotationSource, Class[].class);
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
/*     */   public AnnotationAttributes getAnnotation(String attributeName)
/*     */   {
/* 353 */     return (AnnotationAttributes)getRequiredAttribute(attributeName, AnnotationAttributes.class);
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
/*     */   public <A extends Annotation> A getAnnotation(String attributeName, Class<A> annotationType)
/*     */   {
/* 368 */     return (Annotation)getRequiredAttribute(attributeName, annotationType);
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
/*     */ 
/*     */ 
/*     */   public AnnotationAttributes[] getAnnotationArray(String attributeName)
/*     */   {
/* 386 */     return (AnnotationAttributes[])getRequiredAttribute(attributeName, AnnotationAttributes[].class);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public <A extends Annotation> A[] getAnnotationArray(String attributeName, Class<A> annotationType)
/*     */   {
/* 405 */     Object array = Array.newInstance(annotationType, 0);
/* 406 */     return (Annotation[])getRequiredAttribute(attributeName, array.getClass());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private <T> T getRequiredAttribute(String attributeName, Class<T> expectedType)
/*     */   {
/* 426 */     Assert.hasText(attributeName, "'attributeName' must not be null or empty");
/* 427 */     Object value = get(attributeName);
/* 428 */     assertAttributePresence(attributeName, value);
/* 429 */     assertNotException(attributeName, value);
/* 430 */     if ((!expectedType.isInstance(value)) && (expectedType.isArray()) && 
/* 431 */       (expectedType.getComponentType().isInstance(value))) {
/* 432 */       Object array = Array.newInstance(expectedType.getComponentType(), 1);
/* 433 */       Array.set(array, 0, value);
/* 434 */       value = array;
/*     */     }
/* 436 */     assertAttributeType(attributeName, value, expectedType);
/* 437 */     return (T)value;
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
/*     */   private <T> T getRequiredAttributeWithAlias(String attributeName, Class<? extends Annotation> annotationType, Object annotationSource, Class<T> expectedType)
/*     */   {
/* 466 */     Assert.hasText(attributeName, "'attributeName' must not be null or empty");
/* 467 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/* 468 */     Assert.notNull(expectedType, "'expectedType' must not be null");
/*     */     
/* 470 */     T attributeValue = getAttribute(attributeName, expectedType);
/*     */     
/* 472 */     List<String> aliasNames = (List)AnnotationUtils.getAttributeAliasMap(annotationType).get(attributeName);
/* 473 */     if (aliasNames != null) {
/* 474 */       for (String aliasName : aliasNames) {
/* 475 */         T aliasValue = getAttribute(aliasName, expectedType);
/* 476 */         boolean attributeEmpty = ObjectUtils.isEmpty(attributeValue);
/* 477 */         boolean aliasEmpty = ObjectUtils.isEmpty(aliasValue);
/*     */         
/* 479 */         if ((!attributeEmpty) && (!aliasEmpty) && (!ObjectUtils.nullSafeEquals(attributeValue, aliasValue))) {
/* 480 */           String elementName = annotationSource == null ? "unknown element" : annotationSource.toString();
/* 481 */           String msg = String.format("In annotation [%s] declared on [%s], attribute [%s] and its alias [%s] are present with values of [%s] and [%s], but only one is permitted.", new Object[] {annotationType
/*     */           
/* 483 */             .getName(), elementName, attributeName, aliasName, 
/* 484 */             ObjectUtils.nullSafeToString(attributeValue), ObjectUtils.nullSafeToString(aliasValue) });
/* 485 */           throw new AnnotationConfigurationException(msg);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 491 */         if ((expectedType.isArray()) && (attributeValue == null) && (aliasValue != null)) {
/* 492 */           attributeValue = aliasValue;
/*     */ 
/*     */ 
/*     */         }
/* 496 */         else if ((attributeEmpty) && (!aliasEmpty)) {
/* 497 */           attributeValue = aliasValue;
/*     */         }
/*     */       }
/* 500 */       assertAttributePresence(attributeName, aliasNames, attributeValue);
/*     */     }
/*     */     
/* 503 */     return attributeValue;
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
/*     */   private <T> T getAttribute(String attributeName, Class<T> expectedType)
/*     */   {
/* 519 */     Object value = get(attributeName);
/* 520 */     if (value != null) {
/* 521 */       assertNotException(attributeName, value);
/* 522 */       assertAttributeType(attributeName, value, expectedType);
/*     */     }
/* 524 */     return (T)value;
/*     */   }
/*     */   
/*     */   private void assertAttributePresence(String attributeName, Object attributeValue) {
/* 528 */     if (attributeValue == null) {
/* 529 */       throw new IllegalArgumentException(String.format("Attribute '%s' not found in attributes for annotation [%s]", new Object[] { attributeName, this.displayName }));
/*     */     }
/*     */   }
/*     */   
/*     */   private void assertAttributePresence(String attributeName, List<String> aliases, Object attributeValue)
/*     */   {
/* 535 */     if (attributeValue == null) {
/* 536 */       throw new IllegalArgumentException(String.format("Neither attribute '%s' nor one of its aliases %s was found in attributes for annotation [%s]", new Object[] { attributeName, aliases, this.displayName }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void assertNotException(String attributeName, Object attributeValue)
/*     */   {
/* 543 */     if ((attributeValue instanceof Exception)) {
/* 544 */       throw new IllegalArgumentException(String.format("Attribute '%s' for annotation [%s] was not resolvable due to exception [%s]", new Object[] { attributeName, this.displayName, attributeValue }), (Exception)attributeValue);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void assertAttributeType(String attributeName, Object attributeValue, Class<?> expectedType)
/*     */   {
/* 551 */     if (!expectedType.isInstance(attributeValue)) {
/* 552 */       throw new IllegalArgumentException(String.format("Attribute '%s' is of type [%s], but [%s] was expected in attributes for annotation [%s]", new Object[] { attributeName, attributeValue
/*     */       
/* 554 */         .getClass().getSimpleName(), expectedType.getSimpleName(), this.displayName }));
/*     */     }
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
/*     */ 
/*     */   public Object putIfAbsent(String key, Object value)
/*     */   {
/* 572 */     Object obj = get(key);
/* 573 */     if (obj == null) {
/* 574 */       obj = put(key, value);
/*     */     }
/* 576 */     return obj;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 581 */     Iterator<Map.Entry<String, Object>> entries = entrySet().iterator();
/* 582 */     StringBuilder sb = new StringBuilder("{");
/* 583 */     while (entries.hasNext()) {
/* 584 */       Map.Entry<String, Object> entry = (Map.Entry)entries.next();
/* 585 */       sb.append((String)entry.getKey());
/* 586 */       sb.append('=');
/* 587 */       sb.append(valueToString(entry.getValue()));
/* 588 */       sb.append(entries.hasNext() ? ", " : "");
/*     */     }
/* 590 */     sb.append("}");
/* 591 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private String valueToString(Object value) {
/* 595 */     if (value == this) {
/* 596 */       return "(this Map)";
/*     */     }
/* 598 */     if ((value instanceof Object[])) {
/* 599 */       return "[" + StringUtils.arrayToDelimitedString((Object[])value, ", ") + "]";
/*     */     }
/* 601 */     return String.valueOf(value);
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
/*     */   public static AnnotationAttributes fromMap(Map<String, Object> map)
/*     */   {
/* 614 */     if (map == null) {
/* 615 */       return null;
/*     */     }
/* 617 */     if ((map instanceof AnnotationAttributes)) {
/* 618 */       return (AnnotationAttributes)map;
/*     */     }
/* 620 */     return new AnnotationAttributes(map);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\annotation\AnnotationAttributes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */