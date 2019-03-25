/*     */ package org.springframework.core.type.classreading;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class AnnotationAttributesReadingVisitor
/*     */   extends RecursiveAnnotationAttributesVisitor
/*     */ {
/*     */   private final MultiValueMap<String, AnnotationAttributes> attributesMap;
/*     */   private final Map<String, Set<String>> metaAnnotationMap;
/*     */   
/*     */   public AnnotationAttributesReadingVisitor(String annotationType, MultiValueMap<String, AnnotationAttributes> attributesMap, Map<String, Set<String>> metaAnnotationMap, ClassLoader classLoader)
/*     */   {
/*  56 */     super(annotationType, new AnnotationAttributes(annotationType, classLoader), classLoader);
/*  57 */     this.attributesMap = attributesMap;
/*  58 */     this.metaAnnotationMap = metaAnnotationMap;
/*     */   }
/*     */   
/*     */ 
/*     */   public void visitEnd()
/*     */   {
/*  64 */     super.visitEnd();
/*     */     
/*  66 */     Class<?> annotationClass = this.attributes.annotationType();
/*  67 */     if (annotationClass != null) {
/*  68 */       List<AnnotationAttributes> attributeList = (List)this.attributesMap.get(this.annotationType);
/*  69 */       if (attributeList == null) {
/*  70 */         this.attributesMap.add(this.annotationType, this.attributes);
/*     */       }
/*     */       else {
/*  73 */         attributeList.add(0, this.attributes);
/*     */       }
/*  75 */       Set<Annotation> visited = new LinkedHashSet();
/*  76 */       Annotation[] metaAnnotations = AnnotationUtils.getAnnotations(annotationClass);
/*  77 */       if (!ObjectUtils.isEmpty(metaAnnotations)) {
/*  78 */         for (Annotation metaAnnotation : metaAnnotations) {
/*  79 */           if (!AnnotationUtils.isInJavaLangAnnotationPackage(metaAnnotation)) {
/*  80 */             recursivelyCollectMetaAnnotations(visited, metaAnnotation);
/*     */           }
/*     */         }
/*     */       }
/*  84 */       if (this.metaAnnotationMap != null) {
/*  85 */         Object metaAnnotationTypeNames = new LinkedHashSet(visited.size());
/*  86 */         for (Annotation ann : visited) {
/*  87 */           ((Set)metaAnnotationTypeNames).add(ann.annotationType().getName());
/*     */         }
/*  89 */         this.metaAnnotationMap.put(annotationClass.getName(), metaAnnotationTypeNames);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void recursivelyCollectMetaAnnotations(Set<Annotation> visited, Annotation annotation) {
/*  95 */     Class<? extends Annotation> annotationType = annotation.annotationType();
/*  96 */     String annotationName = annotationType.getName();
/*  97 */     if ((!AnnotationUtils.isInJavaLangAnnotationPackage(annotationName)) && (visited.add(annotation)))
/*     */     {
/*     */       try
/*     */       {
/*     */ 
/* 102 */         if (Modifier.isPublic(annotationType.getModifiers())) {
/* 103 */           this.attributesMap.add(annotationName, 
/* 104 */             AnnotationUtils.getAnnotationAttributes(annotation, false, true));
/*     */         }
/* 106 */         for (Annotation metaMetaAnnotation : annotationType.getAnnotations()) {
/* 107 */           recursivelyCollectMetaAnnotations(visited, metaMetaAnnotation);
/*     */         }
/*     */       }
/*     */       catch (Throwable ex) {
/* 111 */         if (this.logger.isDebugEnabled()) {
/* 112 */           this.logger.debug("Failed to introspect meta-annotations on [" + annotation + "]: " + ex);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\type\classreading\AnnotationAttributesReadingVisitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */