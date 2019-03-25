/*     */ package org.springframework.core.type.filter;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.Inherited;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.core.type.classreading.MetadataReader;
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
/*     */ public class AnnotationTypeFilter
/*     */   extends AbstractTypeHierarchyTraversingFilter
/*     */ {
/*     */   private final Class<? extends Annotation> annotationType;
/*     */   private final boolean considerMetaAnnotations;
/*     */   
/*     */   public AnnotationTypeFilter(Class<? extends Annotation> annotationType)
/*     */   {
/*  54 */     this(annotationType, true, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotationTypeFilter(Class<? extends Annotation> annotationType, boolean considerMetaAnnotations)
/*     */   {
/*  64 */     this(annotationType, considerMetaAnnotations, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotationTypeFilter(Class<? extends Annotation> annotationType, boolean considerMetaAnnotations, boolean considerInterfaces)
/*     */   {
/*  74 */     super(annotationType.isAnnotationPresent(Inherited.class), considerInterfaces);
/*  75 */     this.annotationType = annotationType;
/*  76 */     this.considerMetaAnnotations = considerMetaAnnotations;
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean matchSelf(MetadataReader metadataReader)
/*     */   {
/*  82 */     AnnotationMetadata metadata = metadataReader.getAnnotationMetadata();
/*  83 */     return (metadata.hasAnnotation(this.annotationType.getName())) || ((this.considerMetaAnnotations) && 
/*  84 */       (metadata.hasMetaAnnotation(this.annotationType.getName())));
/*     */   }
/*     */   
/*     */   protected Boolean matchSuperClass(String superClassName)
/*     */   {
/*  89 */     return hasAnnotation(superClassName);
/*     */   }
/*     */   
/*     */   protected Boolean matchInterface(String interfaceName)
/*     */   {
/*  94 */     return hasAnnotation(interfaceName);
/*     */   }
/*     */   
/*     */   protected Boolean hasAnnotation(String typeName) {
/*  98 */     if (Object.class.getName().equals(typeName)) {
/*  99 */       return Boolean.valueOf(false);
/*     */     }
/* 101 */     if (typeName.startsWith("java")) {
/*     */       try {
/* 103 */         Class<?> clazz = ClassUtils.forName(typeName, getClass().getClassLoader());
/* 104 */         return Boolean.valueOf((this.considerMetaAnnotations ? AnnotationUtils.getAnnotation(clazz, this.annotationType) : clazz
/* 105 */           .getAnnotation(this.annotationType)) != null);
/*     */       }
/*     */       catch (Throwable localThrowable) {}
/*     */     }
/*     */     
/*     */ 
/* 111 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\type\filter\AnnotationTypeFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */