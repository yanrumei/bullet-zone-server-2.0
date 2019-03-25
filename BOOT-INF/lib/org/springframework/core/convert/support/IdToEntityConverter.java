/*     */ package org.springframework.core.convert.support;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Collections;
/*     */ import java.util.Set;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/*     */ import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class IdToEntityConverter
/*     */   implements ConditionalGenericConverter
/*     */ {
/*     */   private final ConversionService conversionService;
/*     */   
/*     */   public IdToEntityConverter(ConversionService conversionService)
/*     */   {
/*  47 */     this.conversionService = conversionService;
/*     */   }
/*     */   
/*     */ 
/*     */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes()
/*     */   {
/*  53 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Object.class, Object.class));
/*     */   }
/*     */   
/*     */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
/*     */   {
/*  58 */     Method finder = getFinder(targetType.getType());
/*  59 */     return (finder != null) && 
/*  60 */       (this.conversionService.canConvert(sourceType, TypeDescriptor.valueOf(finder.getParameterTypes()[0])));
/*     */   }
/*     */   
/*     */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
/*     */   {
/*  65 */     if (source == null) {
/*  66 */       return null;
/*     */     }
/*  68 */     Method finder = getFinder(targetType.getType());
/*  69 */     Object id = this.conversionService.convert(source, sourceType, 
/*  70 */       TypeDescriptor.valueOf(finder.getParameterTypes()[0]));
/*  71 */     return ReflectionUtils.invokeMethod(finder, source, new Object[] { id });
/*     */   }
/*     */   
/*     */   private Method getFinder(Class<?> entityClass)
/*     */   {
/*  76 */     String finderMethod = "find" + getEntityName(entityClass);
/*     */     Method[] methods;
/*     */     boolean localOnlyFiltered;
/*     */     try {
/*  80 */       Method[] methods = entityClass.getDeclaredMethods();
/*  81 */       localOnlyFiltered = true;
/*     */     }
/*     */     catch (SecurityException ex)
/*     */     {
/*     */       boolean localOnlyFiltered;
/*  86 */       methods = entityClass.getMethods();
/*  87 */       localOnlyFiltered = false;
/*     */     }
/*  89 */     for (Method method : methods) {
/*  90 */       if ((Modifier.isStatic(method.getModifiers())) && (method.getName().equals(finderMethod)) && 
/*  91 */         (method.getParameterTypes().length == 1) && (method.getReturnType().equals(entityClass)) && ((localOnlyFiltered) || 
/*  92 */         (method.getDeclaringClass().equals(entityClass)))) {
/*  93 */         return method;
/*     */       }
/*     */     }
/*  96 */     return null;
/*     */   }
/*     */   
/*     */   private String getEntityName(Class<?> entityClass) {
/* 100 */     String shortName = ClassUtils.getShortName(entityClass);
/* 101 */     int lastDot = shortName.lastIndexOf('.');
/* 102 */     if (lastDot != -1) {
/* 103 */       return shortName.substring(lastDot + 1);
/*     */     }
/*     */     
/* 106 */     return shortName;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\convert\support\IdToEntityConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */