/*      */ package org.springframework.core.annotation;
/*      */ 
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.lang.reflect.AnnotatedElement;
/*      */ import java.lang.reflect.Method;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import org.springframework.core.BridgeMethodResolver;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.LinkedMultiValueMap;
/*      */ import org.springframework.util.MultiValueMap;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class AnnotatedElementUtils
/*      */ {
/*  102 */   private static final Boolean CONTINUE = null;
/*      */   
/*  104 */   private static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];
/*      */   
/*  106 */   private static final Processor<Boolean> alwaysTrueAnnotationProcessor = new AlwaysTrueBooleanAnnotationProcessor();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static AnnotatedElement forAnnotations(Annotation... annotations)
/*      */   {
/*  116 */     new AnnotatedElement()
/*      */     {
/*      */       public <T extends Annotation> T getAnnotation(Class<T> annotationClass)
/*      */       {
/*  120 */         for (Annotation ann : this.val$annotations) {
/*  121 */           if (ann.annotationType() == annotationClass) {
/*  122 */             return ann;
/*      */           }
/*      */         }
/*  125 */         return null;
/*      */       }
/*      */       
/*      */       public Annotation[] getAnnotations() {
/*  129 */         return this.val$annotations;
/*      */       }
/*      */       
/*      */       public Annotation[] getDeclaredAnnotations() {
/*  133 */         return this.val$annotations;
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Set<String> getMetaAnnotationTypes(AnnotatedElement element, Class<? extends Annotation> annotationType)
/*      */   {
/*  153 */     Assert.notNull(element, "AnnotatedElement must not be null");
/*  154 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/*      */     
/*  156 */     return getMetaAnnotationTypes(element, element.getAnnotation(annotationType));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Set<String> getMetaAnnotationTypes(AnnotatedElement element, String annotationName)
/*      */   {
/*  174 */     Assert.notNull(element, "AnnotatedElement must not be null");
/*  175 */     Assert.hasLength(annotationName, "'annotationName' must not be null or empty");
/*      */     
/*  177 */     return getMetaAnnotationTypes(element, AnnotationUtils.getAnnotation(element, annotationName));
/*      */   }
/*      */   
/*      */   private static Set<String> getMetaAnnotationTypes(AnnotatedElement element, Annotation composed) {
/*  181 */     if (composed == null) {
/*  182 */       return null;
/*      */     }
/*      */     try
/*      */     {
/*  186 */       final Set<String> types = new LinkedHashSet();
/*  187 */       searchWithGetSemantics(composed.annotationType(), null, null, null, new SimpleAnnotationProcessor(true)
/*      */       
/*      */ 
/*      */ 
/*  191 */         new HashSet
/*      */         {
/*      */           public Object process(AnnotatedElement annotatedElement, Annotation annotation, int metaDepth) {
/*  190 */             types.add(annotation.annotationType().getName());
/*  191 */             return AnnotatedElementUtils.CONTINUE; } }, new HashSet(), 1);
/*      */       
/*      */ 
/*  194 */       return !types.isEmpty() ? types : null;
/*      */     }
/*      */     catch (Throwable ex) {
/*  197 */       AnnotationUtils.rethrowAnnotationConfigurationException(ex);
/*  198 */       throw new IllegalStateException("Failed to introspect annotations on " + element, ex);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean hasMetaAnnotationTypes(AnnotatedElement element, Class<? extends Annotation> annotationType)
/*      */   {
/*  215 */     Assert.notNull(element, "AnnotatedElement must not be null");
/*  216 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/*      */     
/*  218 */     return hasMetaAnnotationTypes(element, annotationType, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean hasMetaAnnotationTypes(AnnotatedElement element, String annotationName)
/*      */   {
/*  234 */     Assert.notNull(element, "AnnotatedElement must not be null");
/*  235 */     Assert.hasLength(annotationName, "'annotationName' must not be null or empty");
/*      */     
/*  237 */     return hasMetaAnnotationTypes(element, null, annotationName);
/*      */   }
/*      */   
/*      */ 
/*      */   private static boolean hasMetaAnnotationTypes(AnnotatedElement element, Class<? extends Annotation> annotationType, String annotationName)
/*      */   {
/*  243 */     Boolean.TRUE.equals(
/*  244 */       searchWithGetSemantics(element, annotationType, annotationName, new SimpleAnnotationProcessor()
/*      */       {
/*      */ 
/*      */         public Boolean process(AnnotatedElement annotatedElement, Annotation annotation, int metaDepth)
/*      */         {
/*  248 */           return metaDepth > 0 ? Boolean.TRUE : AnnotatedElementUtils.CONTINUE;
/*      */         }
/*      */       }));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isAnnotated(AnnotatedElement element, Class<? extends Annotation> annotationType)
/*      */   {
/*  268 */     Assert.notNull(element, "AnnotatedElement must not be null");
/*  269 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/*      */     
/*      */ 
/*  272 */     if (element.isAnnotationPresent(annotationType)) {
/*  273 */       return true;
/*      */     }
/*      */     
/*  276 */     return Boolean.TRUE.equals(searchWithGetSemantics(element, annotationType, null, alwaysTrueAnnotationProcessor));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isAnnotated(AnnotatedElement element, String annotationName)
/*      */   {
/*  292 */     Assert.notNull(element, "AnnotatedElement must not be null");
/*  293 */     Assert.hasLength(annotationName, "'annotationName' must not be null or empty");
/*      */     
/*  295 */     return Boolean.TRUE.equals(searchWithGetSemantics(element, null, annotationName, alwaysTrueAnnotationProcessor));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public static AnnotationAttributes getAnnotationAttributes(AnnotatedElement element, String annotationName)
/*      */   {
/*  303 */     return getMergedAnnotationAttributes(element, annotationName);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public static AnnotationAttributes getAnnotationAttributes(AnnotatedElement element, String annotationName, boolean classValuesAsString, boolean nestedAnnotationsAsMap)
/*      */   {
/*  313 */     return getMergedAnnotationAttributes(element, annotationName, classValuesAsString, nestedAnnotationsAsMap);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static AnnotationAttributes getMergedAnnotationAttributes(AnnotatedElement element, Class<? extends Annotation> annotationType)
/*      */   {
/*  336 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/*  337 */     AnnotationAttributes attributes = (AnnotationAttributes)searchWithGetSemantics(element, annotationType, null, new MergedAnnotationAttributesProcessor());
/*      */     
/*  339 */     AnnotationUtils.postProcessAnnotationAttributes(element, attributes, false, false);
/*  340 */     return attributes;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static AnnotationAttributes getMergedAnnotationAttributes(AnnotatedElement element, String annotationName)
/*      */   {
/*  362 */     return getMergedAnnotationAttributes(element, annotationName, false, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static AnnotationAttributes getMergedAnnotationAttributes(AnnotatedElement element, String annotationName, boolean classValuesAsString, boolean nestedAnnotationsAsMap)
/*      */   {
/*  394 */     Assert.hasLength(annotationName, "'annotationName' must not be null or empty");
/*  395 */     AnnotationAttributes attributes = (AnnotationAttributes)searchWithGetSemantics(element, null, annotationName, new MergedAnnotationAttributesProcessor(classValuesAsString, nestedAnnotationsAsMap));
/*      */     
/*  397 */     AnnotationUtils.postProcessAnnotationAttributes(element, attributes, classValuesAsString, nestedAnnotationsAsMap);
/*  398 */     return attributes;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <A extends Annotation> A getMergedAnnotation(AnnotatedElement element, Class<A> annotationType)
/*      */   {
/*  420 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/*      */     
/*      */ 
/*  423 */     if (!(element instanceof Class))
/*      */     {
/*      */ 
/*  426 */       A annotation = element.getAnnotation(annotationType);
/*  427 */       if (annotation != null) {
/*  428 */         return AnnotationUtils.synthesizeAnnotation(annotation, element);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  433 */     AnnotationAttributes attributes = getMergedAnnotationAttributes(element, annotationType);
/*  434 */     return AnnotationUtils.synthesizeAnnotation(attributes, annotationType, element);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <A extends Annotation> Set<A> getAllMergedAnnotations(AnnotatedElement element, Class<A> annotationType)
/*      */   {
/*  460 */     Assert.notNull(element, "AnnotatedElement must not be null");
/*  461 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/*      */     
/*  463 */     MergedAnnotationAttributesProcessor processor = new MergedAnnotationAttributesProcessor(false, false, true);
/*  464 */     searchWithGetSemantics(element, annotationType, null, processor);
/*  465 */     return postProcessAndSynthesizeAggregatedResults(element, annotationType, processor.getAggregatedResults());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <A extends Annotation> Set<A> getMergedRepeatableAnnotations(AnnotatedElement element, Class<A> annotationType)
/*      */   {
/*  495 */     return getMergedRepeatableAnnotations(element, annotationType, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <A extends Annotation> Set<A> getMergedRepeatableAnnotations(AnnotatedElement element, Class<A> annotationType, Class<? extends Annotation> containerType)
/*      */   {
/*  527 */     Assert.notNull(element, "AnnotatedElement must not be null");
/*  528 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/*      */     
/*  530 */     if (containerType == null) {
/*  531 */       containerType = resolveContainerType(annotationType);
/*      */     }
/*      */     else {
/*  534 */       validateContainerType(annotationType, containerType);
/*      */     }
/*      */     
/*  537 */     MergedAnnotationAttributesProcessor processor = new MergedAnnotationAttributesProcessor(false, false, true);
/*  538 */     searchWithGetSemantics(element, annotationType, null, containerType, processor);
/*  539 */     return postProcessAndSynthesizeAggregatedResults(element, annotationType, processor.getAggregatedResults());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static MultiValueMap<String, Object> getAllAnnotationAttributes(AnnotatedElement element, String annotationName)
/*      */   {
/*  557 */     return getAllAnnotationAttributes(element, annotationName, false, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static MultiValueMap<String, Object> getAllAnnotationAttributes(AnnotatedElement element, String annotationName, boolean classValuesAsString, final boolean nestedAnnotationsAsMap)
/*      */   {
/*  581 */     final MultiValueMap<String, Object> attributesMap = new LinkedMultiValueMap();
/*      */     
/*  583 */     searchWithGetSemantics(element, null, annotationName, new SimpleAnnotationProcessor()
/*      */     {
/*      */       public Object process(AnnotatedElement annotatedElement, Annotation annotation, int metaDepth) {
/*  586 */         AnnotationAttributes annotationAttributes = AnnotationUtils.getAnnotationAttributes(annotation, this.val$classValuesAsString, nestedAnnotationsAsMap);
/*      */         
/*  588 */         for (Map.Entry<String, Object> entry : annotationAttributes.entrySet()) {
/*  589 */           attributesMap.add(entry.getKey(), entry.getValue());
/*      */         }
/*  591 */         return AnnotatedElementUtils.CONTINUE;
/*      */       }
/*      */       
/*  594 */     });
/*  595 */     return !attributesMap.isEmpty() ? attributesMap : null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean hasAnnotation(AnnotatedElement element, Class<? extends Annotation> annotationType)
/*      */   {
/*  613 */     Assert.notNull(element, "AnnotatedElement must not be null");
/*  614 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/*      */     
/*      */ 
/*  617 */     if (element.isAnnotationPresent(annotationType)) {
/*  618 */       return true;
/*      */     }
/*      */     
/*  621 */     return Boolean.TRUE.equals(searchWithFindSemantics(element, annotationType, null, alwaysTrueAnnotationProcessor));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static AnnotationAttributes findMergedAnnotationAttributes(AnnotatedElement element, Class<? extends Annotation> annotationType, boolean classValuesAsString, boolean nestedAnnotationsAsMap)
/*      */   {
/*  656 */     AnnotationAttributes attributes = (AnnotationAttributes)searchWithFindSemantics(element, annotationType, null, new MergedAnnotationAttributesProcessor(classValuesAsString, nestedAnnotationsAsMap));
/*      */     
/*  658 */     AnnotationUtils.postProcessAnnotationAttributes(element, attributes, classValuesAsString, nestedAnnotationsAsMap);
/*  659 */     return attributes;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static AnnotationAttributes findMergedAnnotationAttributes(AnnotatedElement element, String annotationName, boolean classValuesAsString, boolean nestedAnnotationsAsMap)
/*      */   {
/*  692 */     AnnotationAttributes attributes = (AnnotationAttributes)searchWithFindSemantics(element, null, annotationName, new MergedAnnotationAttributesProcessor(classValuesAsString, nestedAnnotationsAsMap));
/*      */     
/*  694 */     AnnotationUtils.postProcessAnnotationAttributes(element, attributes, classValuesAsString, nestedAnnotationsAsMap);
/*  695 */     return attributes;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <A extends Annotation> A findMergedAnnotation(AnnotatedElement element, Class<A> annotationType)
/*      */   {
/*  717 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/*      */     
/*      */ 
/*  720 */     if (!(element instanceof Class))
/*      */     {
/*      */ 
/*  723 */       A annotation = element.getAnnotation(annotationType);
/*  724 */       if (annotation != null) {
/*  725 */         return AnnotationUtils.synthesizeAnnotation(annotation, element);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  730 */     AnnotationAttributes attributes = findMergedAnnotationAttributes(element, annotationType, false, false);
/*  731 */     return AnnotationUtils.synthesizeAnnotation(attributes, annotationType, element);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public static <A extends Annotation> A findMergedAnnotation(AnnotatedElement element, String annotationName)
/*      */   {
/*  759 */     AnnotationAttributes attributes = findMergedAnnotationAttributes(element, annotationName, false, false);
/*  760 */     return AnnotationUtils.synthesizeAnnotation(attributes, attributes.annotationType(), element);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <A extends Annotation> Set<A> findAllMergedAnnotations(AnnotatedElement element, Class<A> annotationType)
/*      */   {
/*  785 */     Assert.notNull(element, "AnnotatedElement must not be null");
/*  786 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/*      */     
/*  788 */     MergedAnnotationAttributesProcessor processor = new MergedAnnotationAttributesProcessor(false, false, true);
/*  789 */     searchWithFindSemantics(element, annotationType, null, processor);
/*  790 */     return postProcessAndSynthesizeAggregatedResults(element, annotationType, processor.getAggregatedResults());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <A extends Annotation> Set<A> findMergedRepeatableAnnotations(AnnotatedElement element, Class<A> annotationType)
/*      */   {
/*  820 */     return findMergedRepeatableAnnotations(element, annotationType, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <A extends Annotation> Set<A> findMergedRepeatableAnnotations(AnnotatedElement element, Class<A> annotationType, Class<? extends Annotation> containerType)
/*      */   {
/*  852 */     Assert.notNull(element, "AnnotatedElement must not be null");
/*  853 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/*      */     
/*  855 */     if (containerType == null) {
/*  856 */       containerType = resolveContainerType(annotationType);
/*      */     }
/*      */     else {
/*  859 */       validateContainerType(annotationType, containerType);
/*      */     }
/*      */     
/*  862 */     MergedAnnotationAttributesProcessor processor = new MergedAnnotationAttributesProcessor(false, false, true);
/*  863 */     searchWithFindSemantics(element, annotationType, null, containerType, processor);
/*  864 */     return postProcessAndSynthesizeAggregatedResults(element, annotationType, processor.getAggregatedResults());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static <T> T searchWithGetSemantics(AnnotatedElement element, Class<? extends Annotation> annotationType, String annotationName, Processor<T> processor)
/*      */   {
/*  881 */     return (T)searchWithGetSemantics(element, annotationType, annotationName, null, processor);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static <T> T searchWithGetSemantics(AnnotatedElement element, Class<? extends Annotation> annotationType, String annotationName, Class<? extends Annotation> containerType, Processor<T> processor)
/*      */   {
/*      */     try
/*      */     {
/*  902 */       return (T)searchWithGetSemantics(element, annotationType, annotationName, containerType, processor, new HashSet(), 0);
/*      */     }
/*      */     catch (Throwable ex)
/*      */     {
/*  906 */       AnnotationUtils.rethrowAnnotationConfigurationException(ex);
/*  907 */       throw new IllegalStateException("Failed to introspect annotations on " + element, ex);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static <T> T searchWithGetSemantics(AnnotatedElement element, Class<? extends Annotation> annotationType, String annotationName, Class<? extends Annotation> containerType, Processor<T> processor, Set<AnnotatedElement> visited, int metaDepth)
/*      */   {
/*  932 */     Assert.notNull(element, "AnnotatedElement must not be null");
/*      */     
/*  934 */     if (visited.add(element)) {
/*      */       try
/*      */       {
/*  937 */         List<Annotation> declaredAnnotations = Arrays.asList(element.getDeclaredAnnotations());
/*  938 */         T result = searchWithGetSemanticsInAnnotations(element, declaredAnnotations, annotationType, annotationName, containerType, processor, visited, metaDepth);
/*      */         
/*  940 */         if (result != null) {
/*  941 */           return result;
/*      */         }
/*      */         
/*  944 */         if ((element instanceof Class)) {
/*  945 */           List<Annotation> inheritedAnnotations = new ArrayList();
/*  946 */           for (Annotation annotation : element.getAnnotations()) {
/*  947 */             if (!declaredAnnotations.contains(annotation)) {
/*  948 */               inheritedAnnotations.add(annotation);
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*  953 */           result = searchWithGetSemanticsInAnnotations(element, inheritedAnnotations, annotationType, annotationName, containerType, processor, visited, metaDepth);
/*      */           
/*  955 */           if (result != null) {
/*  956 */             return result;
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (Throwable ex) {
/*  961 */         AnnotationUtils.handleIntrospectionFailure(element, ex);
/*      */       }
/*      */     }
/*      */     
/*  965 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static <T> T searchWithGetSemanticsInAnnotations(AnnotatedElement element, List<Annotation> annotations, Class<? extends Annotation> annotationType, String annotationName, Class<? extends Annotation> containerType, Processor<T> processor, Set<AnnotatedElement> visited, int metaDepth)
/*      */   {
/*  996 */     for (Annotation annotation : annotations) {
/*  997 */       Class<? extends Annotation> currentAnnotationType = annotation.annotationType();
/*  998 */       if (!AnnotationUtils.isInJavaLangAnnotationPackage(currentAnnotationType)) { T result;
/*  999 */         if ((currentAnnotationType == annotationType) || 
/* 1000 */           (currentAnnotationType.getName().equals(annotationName)) || 
/* 1001 */           (processor.alwaysProcesses())) {
/* 1002 */           result = processor.process(element, annotation, metaDepth);
/* 1003 */           if (result != null) {
/* 1004 */             if ((processor.aggregates()) && (metaDepth == 0)) {
/* 1005 */               processor.getAggregatedResults().add(result);
/*      */             }
/*      */             else {
/* 1008 */               return result;
/*      */             }
/*      */             
/*      */           }
/*      */         }
/* 1013 */         else if (currentAnnotationType == containerType) {
/* 1014 */           for (Annotation contained : getRawAnnotationsFromContainer(element, annotation)) {
/* 1015 */             T result = processor.process(element, contained, metaDepth);
/* 1016 */             if (result != null)
/*      */             {
/*      */ 
/* 1019 */               processor.getAggregatedResults().add(result);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1027 */     for (Annotation annotation : annotations) {
/* 1028 */       Class<? extends Annotation> currentAnnotationType = annotation.annotationType();
/* 1029 */       if (!AnnotationUtils.isInJavaLangAnnotationPackage(currentAnnotationType)) {
/* 1030 */         T result = searchWithGetSemantics(currentAnnotationType, annotationType, annotationName, containerType, processor, visited, metaDepth + 1);
/*      */         
/* 1032 */         if (result != null) {
/* 1033 */           processor.postProcess(element, annotation, result);
/* 1034 */           if ((processor.aggregates()) && (metaDepth == 0)) {
/* 1035 */             processor.getAggregatedResults().add(result);
/*      */           }
/*      */           else {
/* 1038 */             return result;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1044 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static <T> T searchWithFindSemantics(AnnotatedElement element, Class<? extends Annotation> annotationType, String annotationName, Processor<T> processor)
/*      */   {
/* 1062 */     return (T)searchWithFindSemantics(element, annotationType, annotationName, null, processor);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static <T> T searchWithFindSemantics(AnnotatedElement element, Class<? extends Annotation> annotationType, String annotationName, Class<? extends Annotation> containerType, Processor<T> processor)
/*      */   {
/* 1082 */     if ((containerType != null) && (!processor.aggregates())) {
/* 1083 */       throw new IllegalArgumentException("Searches for repeatable annotations must supply an aggregating Processor");
/*      */     }
/*      */     
/*      */     try
/*      */     {
/* 1088 */       return (T)searchWithFindSemantics(element, annotationType, annotationName, containerType, processor, new HashSet(), 0);
/*      */     }
/*      */     catch (Throwable ex)
/*      */     {
/* 1092 */       AnnotationUtils.rethrowAnnotationConfigurationException(ex);
/* 1093 */       throw new IllegalStateException("Failed to introspect annotations on " + element, ex);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static <T> T searchWithFindSemantics(AnnotatedElement element, Class<? extends Annotation> annotationType, String annotationName, Class<? extends Annotation> containerType, Processor<T> processor, Set<AnnotatedElement> visited, int metaDepth)
/*      */   {
/* 1119 */     Assert.notNull(element, "AnnotatedElement must not be null");
/*      */     
/* 1121 */     if (visited.add(element)) {
/*      */       try
/*      */       {
/* 1124 */         Annotation[] annotations = element.getDeclaredAnnotations();
/* 1125 */         List<T> aggregatedResults = processor.aggregates() ? new ArrayList() : null;
/*      */         
/*      */ 
/* 1128 */         for (Annotation annotation : annotations) {
/* 1129 */           Class<? extends Annotation> currentAnnotationType = annotation.annotationType();
/* 1130 */           if (!AnnotationUtils.isInJavaLangAnnotationPackage(currentAnnotationType)) { T result;
/* 1131 */             if ((currentAnnotationType == annotationType) || 
/* 1132 */               (currentAnnotationType.getName().equals(annotationName)) || 
/* 1133 */               (processor.alwaysProcesses())) {
/* 1134 */               result = processor.process(element, annotation, metaDepth);
/* 1135 */               if (result != null) {
/* 1136 */                 if ((processor.aggregates()) && (metaDepth == 0)) {
/* 1137 */                   aggregatedResults.add(result);
/*      */                 }
/*      */                 else {
/* 1140 */                   return result;
/*      */                 }
/*      */                 
/*      */               }
/*      */             }
/* 1145 */             else if (currentAnnotationType == containerType) {
/* 1146 */               for (Annotation contained : getRawAnnotationsFromContainer(element, annotation)) {
/* 1147 */                 T result = processor.process(element, contained, metaDepth);
/* 1148 */                 if (result != null)
/*      */                 {
/*      */ 
/* 1151 */                   aggregatedResults.add(result);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 1159 */         for (Annotation annotation : annotations) {
/* 1160 */           Class<? extends Annotation> currentAnnotationType = annotation.annotationType();
/* 1161 */           if (!AnnotationUtils.isInJavaLangAnnotationPackage(currentAnnotationType)) {
/* 1162 */             T result = searchWithFindSemantics(currentAnnotationType, annotationType, annotationName, containerType, processor, visited, metaDepth + 1);
/*      */             
/* 1164 */             if (result != null) {
/* 1165 */               processor.postProcess(currentAnnotationType, annotation, result);
/* 1166 */               if ((processor.aggregates()) && (metaDepth == 0)) {
/* 1167 */                 aggregatedResults.add(result);
/*      */               }
/*      */               else {
/* 1170 */                 return result;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 1176 */         if (processor.aggregates())
/*      */         {
/* 1178 */           processor.getAggregatedResults().addAll(0, aggregatedResults); }
/*      */         Object resolvedMethod;
/*      */         Object result;
/* 1181 */         Class<?>[] ifcs; if ((element instanceof Method)) {
/* 1182 */           Method method = (Method)element;
/*      */           
/*      */ 
/* 1185 */           resolvedMethod = BridgeMethodResolver.findBridgedMethod(method);
/* 1186 */           result = searchWithFindSemantics((AnnotatedElement)resolvedMethod, annotationType, annotationName, containerType, processor, visited, metaDepth);
/*      */           
/* 1188 */           if (result != null) {
/* 1189 */             return (T)result;
/*      */           }
/*      */           
/*      */ 
/* 1193 */           ifcs = method.getDeclaringClass().getInterfaces();
/* 1194 */           result = searchOnInterfaces(method, annotationType, annotationName, containerType, processor, visited, metaDepth, ifcs);
/*      */           
/* 1196 */           if (result != null) {
/* 1197 */             return (T)result;
/*      */           }
/*      */           
/*      */ 
/* 1201 */           Class<?> clazz = method.getDeclaringClass();
/*      */           do {
/* 1203 */             clazz = clazz.getSuperclass();
/* 1204 */             if ((clazz == null) || (Object.class == clazz)) {
/*      */               break;
/*      */             }
/*      */             try
/*      */             {
/* 1209 */               Method equivalentMethod = clazz.getDeclaredMethod(method.getName(), method.getParameterTypes());
/* 1210 */               Method resolvedEquivalentMethod = BridgeMethodResolver.findBridgedMethod(equivalentMethod);
/* 1211 */               result = searchWithFindSemantics(resolvedEquivalentMethod, annotationType, annotationName, containerType, processor, visited, metaDepth);
/*      */               
/* 1213 */               if (result != null) {
/* 1214 */                 return (T)result;
/*      */               }
/*      */             }
/*      */             catch (NoSuchMethodException localNoSuchMethodException) {}
/*      */             
/*      */ 
/*      */ 
/*      */ 
/* 1222 */             result = searchOnInterfaces(method, annotationType, annotationName, containerType, processor, visited, metaDepth, clazz
/* 1223 */               .getInterfaces());
/* 1224 */           } while (result == null);
/* 1225 */           return (T)result;
/*      */ 
/*      */ 
/*      */         }
/* 1229 */         else if ((element instanceof Class)) {
/* 1230 */           Object clazz = (Class)element;
/*      */           
/*      */ 
/* 1233 */           resolvedMethod = ((Class)clazz).getInterfaces();Class<?>[] arrayOfClass1 = resolvedMethod.length; for (ifcs = 0; ifcs < arrayOfClass1; ifcs++) { Class<?> ifc = resolvedMethod[ifcs];
/* 1234 */             T result = searchWithFindSemantics(ifc, annotationType, annotationName, containerType, processor, visited, metaDepth);
/*      */             
/* 1236 */             if (result != null) {
/* 1237 */               return result;
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 1242 */           Object superclass = ((Class)clazz).getSuperclass();
/* 1243 */           if ((superclass != null) && (Object.class != superclass)) {
/* 1244 */             Object result = searchWithFindSemantics((AnnotatedElement)superclass, annotationType, annotationName, containerType, processor, visited, metaDepth);
/*      */             
/* 1246 */             if (result != null) {
/* 1247 */               return (T)result;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (Throwable ex) {
/* 1253 */         AnnotationUtils.handleIntrospectionFailure(element, ex);
/*      */       }
/*      */     }
/* 1256 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static <T> T searchOnInterfaces(Method method, Class<? extends Annotation> annotationType, String annotationName, Class<? extends Annotation> containerType, Processor<T> processor, Set<AnnotatedElement> visited, int metaDepth, Class<?>[] ifcs)
/*      */   {
/* 1263 */     for (Class<?> iface : ifcs) {
/* 1264 */       if (AnnotationUtils.isInterfaceWithAnnotatedMethods(iface)) {
/*      */         try {
/* 1266 */           Method equivalentMethod = iface.getMethod(method.getName(), method.getParameterTypes());
/* 1267 */           T result = searchWithFindSemantics(equivalentMethod, annotationType, annotationName, containerType, processor, visited, metaDepth);
/*      */           
/* 1269 */           if (result != null) {
/* 1270 */             return result;
/*      */           }
/*      */         }
/*      */         catch (NoSuchMethodException localNoSuchMethodException) {}
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1279 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static <A extends Annotation> A[] getRawAnnotationsFromContainer(AnnotatedElement element, Annotation container)
/*      */   {
/*      */     try
/*      */     {
/* 1292 */       return (Annotation[])AnnotationUtils.getValue(container);
/*      */     }
/*      */     catch (Throwable ex) {
/* 1295 */       AnnotationUtils.handleIntrospectionFailure(element, ex);
/*      */     }
/*      */     
/* 1298 */     return (Annotation[])EMPTY_ANNOTATION_ARRAY;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static Class<? extends Annotation> resolveContainerType(Class<? extends Annotation> annotationType)
/*      */   {
/* 1310 */     Class<? extends Annotation> containerType = AnnotationUtils.resolveContainerAnnotationType(annotationType);
/* 1311 */     if (containerType == null)
/*      */     {
/*      */ 
/* 1314 */       throw new IllegalArgumentException("Annotation type must be a repeatable annotation: failed to resolve container type for " + annotationType.getName());
/*      */     }
/* 1316 */     return containerType;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static void validateContainerType(Class<? extends Annotation> annotationType, Class<? extends Annotation> containerType)
/*      */   {
/*      */     try
/*      */     {
/* 1332 */       Method method = containerType.getDeclaredMethod("value", new Class[0]);
/* 1333 */       Class<?> returnType = method.getReturnType();
/* 1334 */       if ((!returnType.isArray()) || (returnType.getComponentType() != annotationType)) {
/* 1335 */         String msg = String.format("Container type [%s] must declare a 'value' attribute for an array of type [%s]", new Object[] {containerType
/*      */         
/* 1337 */           .getName(), annotationType.getName() });
/* 1338 */         throw new AnnotationConfigurationException(msg);
/*      */       }
/*      */     }
/*      */     catch (Throwable ex) {
/* 1342 */       AnnotationUtils.rethrowAnnotationConfigurationException(ex);
/* 1343 */       String msg = String.format("Invalid declaration of container type [%s] for repeatable annotation [%s]", new Object[] {containerType
/* 1344 */         .getName(), annotationType.getName() });
/* 1345 */       throw new AnnotationConfigurationException(msg, ex);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static <A extends Annotation> Set<A> postProcessAndSynthesizeAggregatedResults(AnnotatedElement element, Class<A> annotationType, List<AnnotationAttributes> aggregatedResults)
/*      */   {
/* 1355 */     Set<A> annotations = new LinkedHashSet();
/* 1356 */     for (AnnotationAttributes attributes : aggregatedResults) {
/* 1357 */       AnnotationUtils.postProcessAnnotationAttributes(element, attributes, false, false);
/* 1358 */       annotations.add(AnnotationUtils.synthesizeAnnotation(attributes, annotationType, element));
/*      */     }
/* 1360 */     return annotations;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static abstract interface Processor<T>
/*      */   {
/*      */     public abstract T process(AnnotatedElement paramAnnotatedElement, Annotation paramAnnotation, int paramInt);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public abstract void postProcess(AnnotatedElement paramAnnotatedElement, Annotation paramAnnotation, T paramT);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public abstract boolean alwaysProcesses();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public abstract boolean aggregates();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public abstract List<T> getAggregatedResults();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static abstract class SimpleAnnotationProcessor<T>
/*      */     implements AnnotatedElementUtils.Processor<T>
/*      */   {
/*      */     private final boolean alwaysProcesses;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public SimpleAnnotationProcessor()
/*      */     {
/* 1475 */       this(false);
/*      */     }
/*      */     
/*      */     public SimpleAnnotationProcessor(boolean alwaysProcesses) {
/* 1479 */       this.alwaysProcesses = alwaysProcesses;
/*      */     }
/*      */     
/*      */     public final boolean alwaysProcesses()
/*      */     {
/* 1484 */       return this.alwaysProcesses;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public final void postProcess(AnnotatedElement annotatedElement, Annotation annotation, T result) {}
/*      */     
/*      */ 
/*      */     public final boolean aggregates()
/*      */     {
/* 1494 */       return false;
/*      */     }
/*      */     
/*      */     public final List<T> getAggregatedResults()
/*      */     {
/* 1499 */       throw new UnsupportedOperationException("SimpleAnnotationProcessor does not support aggregated results");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static class AlwaysTrueBooleanAnnotationProcessor
/*      */     extends AnnotatedElementUtils.SimpleAnnotationProcessor<Boolean>
/*      */   {
/*      */     public final Boolean process(AnnotatedElement annotatedElement, Annotation annotation, int metaDepth)
/*      */     {
/* 1514 */       return Boolean.TRUE;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class MergedAnnotationAttributesProcessor
/*      */     implements AnnotatedElementUtils.Processor<AnnotationAttributes>
/*      */   {
/*      */     private final boolean classValuesAsString;
/*      */     
/*      */ 
/*      */ 
/*      */     private final boolean nestedAnnotationsAsMap;
/*      */     
/*      */ 
/*      */ 
/*      */     private final boolean aggregates;
/*      */     
/*      */ 
/*      */     private final List<AnnotationAttributes> aggregatedResults;
/*      */     
/*      */ 
/*      */ 
/*      */     MergedAnnotationAttributesProcessor()
/*      */     {
/* 1541 */       this(false, false, false);
/*      */     }
/*      */     
/*      */     MergedAnnotationAttributesProcessor(boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
/* 1545 */       this(classValuesAsString, nestedAnnotationsAsMap, false);
/*      */     }
/*      */     
/*      */ 
/*      */     MergedAnnotationAttributesProcessor(boolean classValuesAsString, boolean nestedAnnotationsAsMap, boolean aggregates)
/*      */     {
/* 1551 */       this.classValuesAsString = classValuesAsString;
/* 1552 */       this.nestedAnnotationsAsMap = nestedAnnotationsAsMap;
/* 1553 */       this.aggregates = aggregates;
/* 1554 */       this.aggregatedResults = (aggregates ? new ArrayList() : null);
/*      */     }
/*      */     
/*      */     public boolean alwaysProcesses()
/*      */     {
/* 1559 */       return false;
/*      */     }
/*      */     
/*      */     public boolean aggregates()
/*      */     {
/* 1564 */       return this.aggregates;
/*      */     }
/*      */     
/*      */     public List<AnnotationAttributes> getAggregatedResults()
/*      */     {
/* 1569 */       return this.aggregatedResults;
/*      */     }
/*      */     
/*      */     public AnnotationAttributes process(AnnotatedElement annotatedElement, Annotation annotation, int metaDepth)
/*      */     {
/* 1574 */       return AnnotationUtils.retrieveAnnotationAttributes(annotatedElement, annotation, this.classValuesAsString, this.nestedAnnotationsAsMap);
/*      */     }
/*      */     
/*      */ 
/*      */     public void postProcess(AnnotatedElement element, Annotation annotation, AnnotationAttributes attributes)
/*      */     {
/* 1580 */       annotation = AnnotationUtils.synthesizeAnnotation(annotation, element);
/* 1581 */       Class<? extends Annotation> targetAnnotationType = attributes.annotationType();
/*      */       
/*      */ 
/*      */ 
/* 1585 */       Set<String> valuesAlreadyReplaced = new HashSet();
/*      */       
/* 1587 */       for (Method attributeMethod : AnnotationUtils.getAttributeMethods(annotation.annotationType())) {
/* 1588 */         String attributeName = attributeMethod.getName();
/* 1589 */         String attributeOverrideName = AnnotationUtils.getAttributeOverrideName(attributeMethod, targetAnnotationType);
/*      */         
/*      */ 
/* 1592 */         if (attributeOverrideName != null) {
/* 1593 */           if (!valuesAlreadyReplaced.contains(attributeOverrideName))
/*      */           {
/*      */ 
/*      */ 
/* 1597 */             List<String> targetAttributeNames = new ArrayList();
/* 1598 */             targetAttributeNames.add(attributeOverrideName);
/* 1599 */             valuesAlreadyReplaced.add(attributeOverrideName);
/*      */             
/*      */ 
/* 1602 */             List<String> aliases = (List)AnnotationUtils.getAttributeAliasMap(targetAnnotationType).get(attributeOverrideName);
/* 1603 */             if (aliases != null) {
/* 1604 */               for (String alias : aliases) {
/* 1605 */                 if (!valuesAlreadyReplaced.contains(alias)) {
/* 1606 */                   targetAttributeNames.add(alias);
/* 1607 */                   valuesAlreadyReplaced.add(alias);
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/* 1612 */             overrideAttributes(element, annotation, attributes, attributeName, targetAttributeNames);
/*      */           }
/*      */         }
/* 1615 */         else if ((!"value".equals(attributeName)) && (attributes.containsKey(attributeName))) {
/* 1616 */           overrideAttribute(element, annotation, attributes, attributeName, attributeName);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     private void overrideAttributes(AnnotatedElement element, Annotation annotation, AnnotationAttributes attributes, String sourceAttributeName, List<String> targetAttributeNames)
/*      */     {
/* 1624 */       Object adaptedValue = getAdaptedValue(element, annotation, sourceAttributeName);
/*      */       
/* 1626 */       for (String targetAttributeName : targetAttributeNames) {
/* 1627 */         attributes.put(targetAttributeName, adaptedValue);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     private void overrideAttribute(AnnotatedElement element, Annotation annotation, AnnotationAttributes attributes, String sourceAttributeName, String targetAttributeName)
/*      */     {
/* 1634 */       attributes.put(targetAttributeName, getAdaptedValue(element, annotation, sourceAttributeName));
/*      */     }
/*      */     
/*      */     private Object getAdaptedValue(AnnotatedElement element, Annotation annotation, String sourceAttributeName) {
/* 1638 */       Object value = AnnotationUtils.getValue(annotation, sourceAttributeName);
/* 1639 */       return AnnotationUtils.adaptValue(element, value, this.classValuesAsString, this.nestedAnnotationsAsMap);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\annotation\AnnotatedElementUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */