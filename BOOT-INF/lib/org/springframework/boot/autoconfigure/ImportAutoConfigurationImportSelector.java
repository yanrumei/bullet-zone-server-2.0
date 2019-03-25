/*     */ package org.springframework.boot.autoconfigure;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import org.springframework.boot.context.annotation.DeterminableImports;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.core.io.support.SpringFactoriesLoader;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.LinkedMultiValueMap;
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
/*     */ class ImportAutoConfigurationImportSelector
/*     */   extends AutoConfigurationImportSelector
/*     */   implements DeterminableImports
/*     */ {
/*     */   private static final Set<String> ANNOTATION_NAMES;
/*     */   
/*     */   static
/*     */   {
/*  54 */     Set<String> names = new LinkedHashSet();
/*  55 */     names.add(ImportAutoConfiguration.class.getName());
/*  56 */     names.add("org.springframework.boot.autoconfigure.test.ImportAutoConfiguration");
/*  57 */     ANNOTATION_NAMES = Collections.unmodifiableSet(names);
/*     */   }
/*     */   
/*     */   public Set<Object> determineImports(AnnotationMetadata metadata)
/*     */   {
/*  62 */     Set<String> result = new LinkedHashSet();
/*  63 */     result.addAll(getCandidateConfigurations(metadata, null));
/*  64 */     result.removeAll(getExclusions(metadata, null));
/*  65 */     return Collections.unmodifiableSet(result);
/*     */   }
/*     */   
/*     */   protected AnnotationAttributes getAttributes(AnnotationMetadata metadata)
/*     */   {
/*  70 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes)
/*     */   {
/*  76 */     List<String> candidates = new ArrayList();
/*  77 */     Map<Class<?>, List<Annotation>> annotations = getAnnotations(metadata);
/*  78 */     for (Map.Entry<Class<?>, List<Annotation>> entry : annotations.entrySet()) {
/*  79 */       collectCandidateConfigurations((Class)entry.getKey(), (List)entry.getValue(), candidates);
/*     */     }
/*  81 */     return candidates;
/*     */   }
/*     */   
/*     */   private void collectCandidateConfigurations(Class<?> source, List<Annotation> annotations, List<String> candidates)
/*     */   {
/*  86 */     for (Annotation annotation : annotations) {
/*  87 */       candidates.addAll(getConfigurationsForAnnotation(source, annotation));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private Collection<String> getConfigurationsForAnnotation(Class<?> source, Annotation annotation)
/*     */   {
/*  94 */     String[] classes = (String[])AnnotationUtils.getAnnotationAttributes(annotation, true).get("classes");
/*  95 */     if (classes.length > 0) {
/*  96 */       return Arrays.asList(classes);
/*     */     }
/*  98 */     return loadFactoryNames(source);
/*     */   }
/*     */   
/*     */   protected Collection<String> loadFactoryNames(Class<?> source) {
/* 102 */     return SpringFactoriesLoader.loadFactoryNames(source, 
/* 103 */       getClass().getClassLoader());
/*     */   }
/*     */   
/*     */ 
/*     */   protected Set<String> getExclusions(AnnotationMetadata metadata, AnnotationAttributes attributes)
/*     */   {
/* 109 */     Set<String> exclusions = new LinkedHashSet();
/* 110 */     Class<?> source = ClassUtils.resolveClassName(metadata.getClassName(), null);
/* 111 */     for (String annotationName : ANNOTATION_NAMES)
/*     */     {
/* 113 */       merged = AnnotatedElementUtils.getMergedAnnotationAttributes(source, annotationName);
/*     */       
/* 115 */       Class<?>[] exclude = merged == null ? null : merged.getClassArray("exclude");
/* 116 */       if (exclude != null) {
/* 117 */         for (Class<?> excludeClass : exclude)
/* 118 */           exclusions.add(excludeClass.getName());
/*     */       }
/*     */     }
/*     */     AnnotationAttributes merged;
/* 122 */     for (List<Annotation> annotations : getAnnotations(metadata).values()) {
/* 123 */       for (Annotation annotation : annotations)
/*     */       {
/* 125 */         String[] exclude = (String[])AnnotationUtils.getAnnotationAttributes(annotation, true).get("exclude");
/* 126 */         if (!ObjectUtils.isEmpty(exclude)) {
/* 127 */           exclusions.addAll(Arrays.asList(exclude));
/*     */         }
/*     */       }
/*     */     }
/* 131 */     return exclusions;
/*     */   }
/*     */   
/*     */   protected final Map<Class<?>, List<Annotation>> getAnnotations(AnnotationMetadata metadata)
/*     */   {
/* 136 */     MultiValueMap<Class<?>, Annotation> annotations = new LinkedMultiValueMap();
/* 137 */     Class<?> source = ClassUtils.resolveClassName(metadata.getClassName(), null);
/* 138 */     collectAnnotations(source, annotations, new HashSet());
/* 139 */     return Collections.unmodifiableMap(annotations);
/*     */   }
/*     */   
/*     */   private void collectAnnotations(Class<?> source, MultiValueMap<Class<?>, Annotation> annotations, HashSet<Class<?>> seen)
/*     */   {
/* 144 */     if ((source != null) && (seen.add(source))) {
/* 145 */       for (Annotation annotation : source.getDeclaredAnnotations()) {
/* 146 */         if (!AnnotationUtils.isInJavaLangAnnotationPackage(annotation))
/*     */         {
/* 148 */           if (ANNOTATION_NAMES.contains(annotation.annotationType().getName())) {
/* 149 */             annotations.add(source, annotation);
/*     */           }
/* 151 */           collectAnnotations(annotation.annotationType(), annotations, seen);
/*     */         }
/*     */       }
/* 154 */       collectAnnotations(source.getSuperclass(), annotations, seen);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getOrder()
/*     */   {
/* 160 */     return super.getOrder() - 1;
/*     */   }
/*     */   
/*     */   protected void handleInvalidExcludes(List<String> invalidExcludes) {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\ImportAutoConfigurationImportSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */