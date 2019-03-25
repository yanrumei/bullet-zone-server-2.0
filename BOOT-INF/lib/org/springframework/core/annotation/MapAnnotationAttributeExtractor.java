/*     */ package org.springframework.core.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class MapAnnotationAttributeExtractor
/*     */   extends AbstractAliasAwareAnnotationAttributeExtractor<Map<String, Object>>
/*     */ {
/*     */   MapAnnotationAttributeExtractor(Map<String, Object> attributes, Class<? extends Annotation> annotationType, AnnotatedElement annotatedElement)
/*     */   {
/*  57 */     super(annotationType, annotatedElement, enrichAndValidateAttributes(attributes, annotationType));
/*     */   }
/*     */   
/*     */ 
/*     */   protected Object getRawAttributeValue(Method attributeMethod)
/*     */   {
/*  63 */     return getRawAttributeValue(attributeMethod.getName());
/*     */   }
/*     */   
/*     */   protected Object getRawAttributeValue(String attributeName)
/*     */   {
/*  68 */     return ((Map)getSource()).get(attributeName);
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
/*     */   private static Map<String, Object> enrichAndValidateAttributes(Map<String, Object> originalAttributes, Class<? extends Annotation> annotationType)
/*     */   {
/*  90 */     Map<String, Object> attributes = new LinkedHashMap(originalAttributes);
/*  91 */     Map<String, List<String>> attributeAliasMap = AnnotationUtils.getAttributeAliasMap(annotationType);
/*     */     
/*  93 */     for (Method attributeMethod : AnnotationUtils.getAttributeMethods(annotationType)) {
/*  94 */       String attributeName = attributeMethod.getName();
/*  95 */       Object attributeValue = attributes.get(attributeName);
/*     */       
/*     */ 
/*  98 */       if (attributeValue == null) {
/*  99 */         List<String> aliasNames = (List)attributeAliasMap.get(attributeName);
/* 100 */         if (aliasNames != null) {
/* 101 */           for (String aliasName : aliasNames) {
/* 102 */             Object aliasValue = attributes.get(aliasName);
/* 103 */             if (aliasValue != null) {
/* 104 */               attributeValue = aliasValue;
/* 105 */               attributes.put(attributeName, attributeValue);
/* 106 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 113 */       if (attributeValue == null) {
/* 114 */         Object defaultValue = AnnotationUtils.getDefaultValue(annotationType, attributeName);
/* 115 */         if (defaultValue != null) {
/* 116 */           attributeValue = defaultValue;
/* 117 */           attributes.put(attributeName, attributeValue);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 122 */       if (attributeValue == null) {
/* 123 */         throw new IllegalArgumentException(String.format("Attributes map %s returned null for required attribute '%s' defined by annotation type [%s].", new Object[] { attributes, attributeName, annotationType
/*     */         
/* 125 */           .getName() }));
/*     */       }
/*     */       
/*     */ 
/* 129 */       Class<?> requiredReturnType = attributeMethod.getReturnType();
/* 130 */       Object actualReturnType = attributeValue.getClass();
/*     */       
/* 132 */       if (!ClassUtils.isAssignable(requiredReturnType, (Class)actualReturnType)) {
/* 133 */         boolean converted = false;
/*     */         
/*     */ 
/* 136 */         if ((requiredReturnType.isArray()) && (requiredReturnType.getComponentType() == actualReturnType)) {
/* 137 */           Object array = Array.newInstance(requiredReturnType.getComponentType(), 1);
/* 138 */           Array.set(array, 0, attributeValue);
/* 139 */           attributes.put(attributeName, array);
/* 140 */           converted = true;
/*     */ 
/*     */ 
/*     */         }
/* 144 */         else if ((Annotation.class.isAssignableFrom(requiredReturnType)) && 
/* 145 */           (Map.class.isAssignableFrom((Class)actualReturnType))) {
/* 146 */           Class<? extends Annotation> nestedAnnotationType = requiredReturnType;
/*     */           
/* 148 */           Map<String, Object> map = (Map)attributeValue;
/* 149 */           attributes.put(attributeName, AnnotationUtils.synthesizeAnnotation(map, nestedAnnotationType, null));
/* 150 */           converted = true;
/*     */ 
/*     */ 
/*     */         }
/* 154 */         else if ((requiredReturnType.isArray()) && (((Class)actualReturnType).isArray()) && 
/* 155 */           (Annotation.class.isAssignableFrom(requiredReturnType.getComponentType())) && 
/* 156 */           (Map.class.isAssignableFrom(((Class)actualReturnType).getComponentType())))
/*     */         {
/* 158 */           Class<? extends Annotation> nestedAnnotationType = requiredReturnType.getComponentType();
/* 159 */           Map<String, Object>[] maps = (Map[])attributeValue;
/* 160 */           attributes.put(attributeName, AnnotationUtils.synthesizeAnnotationArray(maps, nestedAnnotationType));
/* 161 */           converted = true;
/*     */         }
/*     */         
/* 164 */         if (!converted) {
/* 165 */           throw new IllegalArgumentException(String.format("Attributes map %s returned a value of type [%s] for attribute '%s', but a value of type [%s] is required as defined by annotation type [%s].", new Object[] { attributes, ((Class)actualReturnType)
/*     */           
/*     */ 
/* 168 */             .getName(), attributeName, requiredReturnType.getName(), annotationType
/* 169 */             .getName() }));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 174 */     return attributes;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\annotation\MapAnnotationAttributeExtractor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */