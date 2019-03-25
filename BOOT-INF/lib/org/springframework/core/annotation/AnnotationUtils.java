/*      */ package org.springframework.core.annotation;
/*      */ 
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.lang.reflect.AnnotatedElement;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.InvocationHandler;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.lang.reflect.Proxy;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.apache.commons.logging.LogFactory;
/*      */ import org.springframework.core.BridgeMethodResolver;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ConcurrentReferenceHashMap;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.ReflectionUtils;
/*      */ import org.springframework.util.StringUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class AnnotationUtils
/*      */ {
/*      */   public static final String VALUE = "value";
/*      */   private static final String REPEATABLE_CLASS_NAME = "java.lang.annotation.Repeatable";
/*  117 */   private static final Map<AnnotationCacheKey, Annotation> findAnnotationCache = new ConcurrentReferenceHashMap(256);
/*      */   
/*      */ 
/*  120 */   private static final Map<AnnotationCacheKey, Boolean> metaPresentCache = new ConcurrentReferenceHashMap(256);
/*      */   
/*      */ 
/*  123 */   private static final Map<Class<?>, Boolean> annotatedInterfaceCache = new ConcurrentReferenceHashMap(256);
/*      */   
/*      */ 
/*  126 */   private static final Map<Class<? extends Annotation>, Boolean> synthesizableCache = new ConcurrentReferenceHashMap(256);
/*      */   
/*      */ 
/*  129 */   private static final Map<Class<? extends Annotation>, Map<String, List<String>>> attributeAliasesCache = new ConcurrentReferenceHashMap(256);
/*      */   
/*      */ 
/*  132 */   private static final Map<Class<? extends Annotation>, List<Method>> attributeMethodsCache = new ConcurrentReferenceHashMap(256);
/*      */   
/*      */ 
/*  135 */   private static final Map<Method, AliasDescriptor> aliasDescriptorCache = new ConcurrentReferenceHashMap(256);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static transient Log logger;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <A extends Annotation> A getAnnotation(Annotation ann, Class<A> annotationType)
/*      */   {
/*  155 */     if (annotationType.isInstance(ann)) {
/*  156 */       return synthesizeAnnotation(ann);
/*      */     }
/*  158 */     Class<? extends Annotation> annotatedElement = ann.annotationType();
/*      */     try {
/*  160 */       return synthesizeAnnotation(annotatedElement.getAnnotation(annotationType), annotatedElement);
/*      */     }
/*      */     catch (Throwable ex) {
/*  163 */       handleIntrospectionFailure(annotatedElement, ex); }
/*  164 */     return null;
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
/*      */   public static <A extends Annotation> A getAnnotation(AnnotatedElement annotatedElement, Class<A> annotationType)
/*      */   {
/*      */     try
/*      */     {
/*  182 */       A annotation = annotatedElement.getAnnotation(annotationType);
/*  183 */       if (annotation == null) {
/*  184 */         for (Annotation metaAnn : annotatedElement.getAnnotations()) {
/*  185 */           annotation = metaAnn.annotationType().getAnnotation(annotationType);
/*  186 */           if (annotation != null) {
/*      */             break;
/*      */           }
/*      */         }
/*      */       }
/*  191 */       return synthesizeAnnotation(annotation, annotatedElement);
/*      */     }
/*      */     catch (Throwable ex) {
/*  194 */       handleIntrospectionFailure(annotatedElement, ex); }
/*  195 */     return null;
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
/*      */   public static <A extends Annotation> A getAnnotation(Method method, Class<A> annotationType)
/*      */   {
/*  214 */     Method resolvedMethod = BridgeMethodResolver.findBridgedMethod(method);
/*  215 */     return getAnnotation(resolvedMethod, annotationType);
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
/*      */   public static Annotation[] getAnnotations(AnnotatedElement annotatedElement)
/*      */   {
/*      */     try
/*      */     {
/*  231 */       return synthesizeAnnotationArray(annotatedElement.getAnnotations(), annotatedElement);
/*      */     }
/*      */     catch (Throwable ex) {
/*  234 */       handleIntrospectionFailure(annotatedElement, ex); }
/*  235 */     return null;
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
/*      */   public static Annotation[] getAnnotations(Method method)
/*      */   {
/*      */     try
/*      */     {
/*  253 */       return synthesizeAnnotationArray(BridgeMethodResolver.findBridgedMethod(method).getAnnotations(), method);
/*      */     }
/*      */     catch (Throwable ex) {
/*  256 */       handleIntrospectionFailure(method, ex); }
/*  257 */     return null;
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
/*      */   @Deprecated
/*      */   public static <A extends Annotation> Set<A> getRepeatableAnnotation(Method method, Class<? extends Annotation> containerAnnotationType, Class<A> annotationType)
/*      */   {
/*  273 */     return getRepeatableAnnotations(method, annotationType, containerAnnotationType);
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
/*      */   @Deprecated
/*      */   public static <A extends Annotation> Set<A> getRepeatableAnnotation(AnnotatedElement annotatedElement, Class<? extends Annotation> containerAnnotationType, Class<A> annotationType)
/*      */   {
/*  288 */     return getRepeatableAnnotations(annotatedElement, annotationType, containerAnnotationType);
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
/*      */   public static <A extends Annotation> Set<A> getRepeatableAnnotations(AnnotatedElement annotatedElement, Class<A> annotationType)
/*      */   {
/*  321 */     return getRepeatableAnnotations(annotatedElement, annotationType, null);
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
/*      */ 
/*      */   public static <A extends Annotation> Set<A> getRepeatableAnnotations(AnnotatedElement annotatedElement, Class<A> annotationType, Class<? extends Annotation> containerAnnotationType)
/*      */   {
/*  357 */     Set<A> annotations = getDeclaredRepeatableAnnotations(annotatedElement, annotationType, containerAnnotationType);
/*  358 */     if (!annotations.isEmpty()) {
/*  359 */       return annotations;
/*      */     }
/*      */     
/*  362 */     if ((annotatedElement instanceof Class)) {
/*  363 */       Class<?> superclass = ((Class)annotatedElement).getSuperclass();
/*  364 */       if ((superclass != null) && (Object.class != superclass)) {
/*  365 */         return getRepeatableAnnotations(superclass, annotationType, containerAnnotationType);
/*      */       }
/*      */     }
/*      */     
/*  369 */     return getRepeatableAnnotations(annotatedElement, annotationType, containerAnnotationType, false);
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
/*      */   public static <A extends Annotation> Set<A> getDeclaredRepeatableAnnotations(AnnotatedElement annotatedElement, Class<A> annotationType)
/*      */   {
/*  403 */     return getDeclaredRepeatableAnnotations(annotatedElement, annotationType, null);
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
/*      */ 
/*      */   public static <A extends Annotation> Set<A> getDeclaredRepeatableAnnotations(AnnotatedElement annotatedElement, Class<A> annotationType, Class<? extends Annotation> containerAnnotationType)
/*      */   {
/*  439 */     return getRepeatableAnnotations(annotatedElement, annotationType, containerAnnotationType, true);
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
/*      */   private static <A extends Annotation> Set<A> getRepeatableAnnotations(AnnotatedElement annotatedElement, Class<A> annotationType, Class<? extends Annotation> containerAnnotationType, boolean declaredMode)
/*      */   {
/*  465 */     Assert.notNull(annotatedElement, "AnnotatedElement must not be null");
/*  466 */     Assert.notNull(annotationType, "Annotation type must not be null");
/*      */     try
/*      */     {
/*  469 */       if ((annotatedElement instanceof Method)) {
/*  470 */         annotatedElement = BridgeMethodResolver.findBridgedMethod((Method)annotatedElement);
/*      */       }
/*  472 */       return new AnnotationCollector(annotationType, containerAnnotationType, declaredMode).getResult(annotatedElement);
/*      */     }
/*      */     catch (Throwable ex) {
/*  475 */       handleIntrospectionFailure(annotatedElement, ex); }
/*  476 */     return Collections.emptySet();
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
/*      */   public static <A extends Annotation> A findAnnotation(AnnotatedElement annotatedElement, Class<A> annotationType)
/*      */   {
/*  497 */     Assert.notNull(annotatedElement, "AnnotatedElement must not be null");
/*  498 */     if (annotationType == null) {
/*  499 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  504 */     A ann = findAnnotation(annotatedElement, annotationType, new HashSet());
/*  505 */     return synthesizeAnnotation(ann, annotatedElement);
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
/*      */   private static <A extends Annotation> A findAnnotation(AnnotatedElement annotatedElement, Class<A> annotationType, Set<Annotation> visited)
/*      */   {
/*      */     try
/*      */     {
/*  522 */       Annotation[] anns = annotatedElement.getDeclaredAnnotations();
/*  523 */       for (Annotation ann : anns) {
/*  524 */         if (ann.annotationType() == annotationType) {
/*  525 */           return ann;
/*      */         }
/*      */       }
/*  528 */       for (Annotation ann : anns) {
/*  529 */         if ((!isInJavaLangAnnotationPackage(ann)) && (visited.add(ann))) {
/*  530 */           A annotation = findAnnotation(ann.annotationType(), annotationType, visited);
/*  531 */           if (annotation != null) {
/*  532 */             return annotation;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Throwable ex) {
/*  538 */       handleIntrospectionFailure(annotatedElement, ex);
/*      */     }
/*  540 */     return null;
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
/*      */   public static <A extends Annotation> A findAnnotation(Method method, Class<A> annotationType)
/*      */   {
/*  560 */     Assert.notNull(method, "Method must not be null");
/*  561 */     if (annotationType == null) {
/*  562 */       return null;
/*      */     }
/*      */     
/*  565 */     AnnotationCacheKey cacheKey = new AnnotationCacheKey(method, annotationType);
/*  566 */     A result = (Annotation)findAnnotationCache.get(cacheKey);
/*      */     
/*  568 */     if (result == null) {
/*  569 */       Method resolvedMethod = BridgeMethodResolver.findBridgedMethod(method);
/*  570 */       result = findAnnotation(resolvedMethod, annotationType);
/*      */       
/*  572 */       if (result == null) {
/*  573 */         result = searchOnInterfaces(method, annotationType, method.getDeclaringClass().getInterfaces());
/*      */       }
/*      */       
/*  576 */       Class<?> clazz = method.getDeclaringClass();
/*  577 */       while (result == null) {
/*  578 */         clazz = clazz.getSuperclass();
/*  579 */         if ((clazz == null) || (Object.class == clazz)) {
/*      */           break;
/*      */         }
/*      */         try {
/*  583 */           Method equivalentMethod = clazz.getDeclaredMethod(method.getName(), method.getParameterTypes());
/*  584 */           Method resolvedEquivalentMethod = BridgeMethodResolver.findBridgedMethod(equivalentMethod);
/*  585 */           result = findAnnotation(resolvedEquivalentMethod, annotationType);
/*      */         }
/*      */         catch (NoSuchMethodException localNoSuchMethodException) {}
/*      */         
/*      */ 
/*  590 */         if (result == null) {
/*  591 */           result = searchOnInterfaces(method, annotationType, clazz.getInterfaces());
/*      */         }
/*      */       }
/*      */       
/*  595 */       if (result != null) {
/*  596 */         result = synthesizeAnnotation(result, method);
/*  597 */         findAnnotationCache.put(cacheKey, result);
/*      */       }
/*      */     }
/*      */     
/*  601 */     return result;
/*      */   }
/*      */   
/*      */   private static <A extends Annotation> A searchOnInterfaces(Method method, Class<A> annotationType, Class<?>... ifcs) {
/*  605 */     A annotation = null;
/*  606 */     for (Class<?> iface : ifcs) {
/*  607 */       if (isInterfaceWithAnnotatedMethods(iface)) {
/*      */         try {
/*  609 */           Method equivalentMethod = iface.getMethod(method.getName(), method.getParameterTypes());
/*  610 */           annotation = getAnnotation(equivalentMethod, annotationType);
/*      */         }
/*      */         catch (NoSuchMethodException localNoSuchMethodException) {}
/*      */         
/*      */ 
/*  615 */         if (annotation != null) {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/*  620 */     return annotation;
/*      */   }
/*      */   
/*      */   static boolean isInterfaceWithAnnotatedMethods(Class<?> iface) {
/*  624 */     Boolean found = (Boolean)annotatedInterfaceCache.get(iface);
/*  625 */     if (found != null) {
/*  626 */       return found.booleanValue();
/*      */     }
/*  628 */     found = Boolean.FALSE;
/*  629 */     for (Method ifcMethod : iface.getMethods()) {
/*      */       try {
/*  631 */         if (ifcMethod.getAnnotations().length > 0) {
/*  632 */           found = Boolean.TRUE;
/*  633 */           break;
/*      */         }
/*      */       }
/*      */       catch (Throwable ex) {
/*  637 */         handleIntrospectionFailure(ifcMethod, ex);
/*      */       }
/*      */     }
/*  640 */     annotatedInterfaceCache.put(iface, found);
/*  641 */     return found.booleanValue();
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
/*      */   public static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType)
/*      */   {
/*  667 */     return findAnnotation(clazz, annotationType, true);
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
/*      */   private static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType, boolean synthesize)
/*      */   {
/*  682 */     Assert.notNull(clazz, "Class must not be null");
/*  683 */     if (annotationType == null) {
/*  684 */       return null;
/*      */     }
/*      */     
/*  687 */     AnnotationCacheKey cacheKey = new AnnotationCacheKey(clazz, annotationType);
/*  688 */     A result = (Annotation)findAnnotationCache.get(cacheKey);
/*  689 */     if (result == null) {
/*  690 */       result = findAnnotation(clazz, annotationType, new HashSet());
/*  691 */       if ((result != null) && (synthesize)) {
/*  692 */         result = synthesizeAnnotation(result, clazz);
/*  693 */         findAnnotationCache.put(cacheKey, result);
/*      */       }
/*      */     }
/*  696 */     return result;
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
/*      */   private static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType, Set<Annotation> visited)
/*      */   {
/*      */     try
/*      */     {
/*  711 */       Annotation[] anns = clazz.getDeclaredAnnotations();
/*  712 */       for (Annotation ann : anns) {
/*  713 */         if (ann.annotationType() == annotationType) {
/*  714 */           return ann;
/*      */         }
/*      */       }
/*  717 */       for (Annotation ann : anns) {
/*  718 */         if ((!isInJavaLangAnnotationPackage(ann)) && (visited.add(ann))) {
/*  719 */           A annotation = findAnnotation(ann.annotationType(), annotationType, visited);
/*  720 */           if (annotation != null) {
/*  721 */             return annotation;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Throwable ex) {
/*  727 */       handleIntrospectionFailure(clazz, ex);
/*  728 */       return null;
/*      */     }
/*      */     
/*  731 */     for (Class<?> ifc : clazz.getInterfaces()) {
/*  732 */       A annotation = findAnnotation(ifc, annotationType, visited);
/*  733 */       if (annotation != null) {
/*  734 */         return annotation;
/*      */       }
/*      */     }
/*      */     
/*  738 */     Class<?> superclass = clazz.getSuperclass();
/*  739 */     if ((superclass == null) || (Object.class == superclass)) {
/*  740 */       return null;
/*      */     }
/*  742 */     return findAnnotation(superclass, annotationType, visited);
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
/*      */   public static Class<?> findAnnotationDeclaringClass(Class<? extends Annotation> annotationType, Class<?> clazz)
/*      */   {
/*  768 */     Assert.notNull(annotationType, "Annotation type must not be null");
/*  769 */     if ((clazz == null) || (Object.class == clazz)) {
/*  770 */       return null;
/*      */     }
/*  772 */     if (isAnnotationDeclaredLocally(annotationType, clazz)) {
/*  773 */       return clazz;
/*      */     }
/*  775 */     return findAnnotationDeclaringClass(annotationType, clazz.getSuperclass());
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
/*      */   public static Class<?> findAnnotationDeclaringClassForTypes(List<Class<? extends Annotation>> annotationTypes, Class<?> clazz)
/*      */   {
/*  803 */     Assert.notEmpty(annotationTypes, "List of annotation types must not be empty");
/*  804 */     if ((clazz == null) || (Object.class == clazz)) {
/*  805 */       return null;
/*      */     }
/*  807 */     for (Class<? extends Annotation> annotationType : annotationTypes) {
/*  808 */       if (isAnnotationDeclaredLocally(annotationType, clazz)) {
/*  809 */         return clazz;
/*      */       }
/*      */     }
/*  812 */     return findAnnotationDeclaringClassForTypes(annotationTypes, clazz.getSuperclass());
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
/*      */   public static boolean isAnnotationDeclaredLocally(Class<? extends Annotation> annotationType, Class<?> clazz)
/*      */   {
/*  834 */     Assert.notNull(annotationType, "Annotation type must not be null");
/*  835 */     Assert.notNull(clazz, "Class must not be null");
/*      */     try {
/*  837 */       for (Annotation ann : clazz.getDeclaredAnnotations()) {
/*  838 */         if (ann.annotationType() == annotationType) {
/*  839 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Throwable ex) {
/*  844 */       handleIntrospectionFailure(clazz, ex);
/*      */     }
/*  846 */     return false;
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
/*      */   public static boolean isAnnotationInherited(Class<? extends Annotation> annotationType, Class<?> clazz)
/*      */   {
/*  869 */     Assert.notNull(annotationType, "Annotation type must not be null");
/*  870 */     Assert.notNull(clazz, "Class must not be null");
/*  871 */     return (clazz.isAnnotationPresent(annotationType)) && (!isAnnotationDeclaredLocally(annotationType, clazz));
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
/*      */   public static boolean isAnnotationMetaPresent(Class<? extends Annotation> annotationType, Class<? extends Annotation> metaAnnotationType)
/*      */   {
/*  885 */     Assert.notNull(annotationType, "Annotation type must not be null");
/*  886 */     if (metaAnnotationType == null) {
/*  887 */       return false;
/*      */     }
/*      */     
/*  890 */     AnnotationCacheKey cacheKey = new AnnotationCacheKey(annotationType, metaAnnotationType);
/*  891 */     Boolean metaPresent = (Boolean)metaPresentCache.get(cacheKey);
/*  892 */     if (metaPresent != null) {
/*  893 */       return metaPresent.booleanValue();
/*      */     }
/*  895 */     metaPresent = Boolean.FALSE;
/*  896 */     if (findAnnotation(annotationType, metaAnnotationType, false) != null) {
/*  897 */       metaPresent = Boolean.TRUE;
/*      */     }
/*  899 */     metaPresentCache.put(cacheKey, metaPresent);
/*  900 */     return metaPresent.booleanValue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isInJavaLangAnnotationPackage(Annotation annotation)
/*      */   {
/*  910 */     return (annotation != null) && (isInJavaLangAnnotationPackage(annotation.annotationType()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static boolean isInJavaLangAnnotationPackage(Class<? extends Annotation> annotationType)
/*      */   {
/*  921 */     return (annotationType != null) && (isInJavaLangAnnotationPackage(annotationType.getName()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isInJavaLangAnnotationPackage(String annotationType)
/*      */   {
/*  932 */     return (annotationType != null) && (annotationType.startsWith("java.lang.annotation"));
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
/*      */   public static Map<String, Object> getAnnotationAttributes(Annotation annotation)
/*      */   {
/*  951 */     return getAnnotationAttributes(null, annotation);
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
/*      */   public static Map<String, Object> getAnnotationAttributes(Annotation annotation, boolean classValuesAsString)
/*      */   {
/*  969 */     return getAnnotationAttributes(annotation, classValuesAsString, false);
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
/*      */   public static AnnotationAttributes getAnnotationAttributes(Annotation annotation, boolean classValuesAsString, boolean nestedAnnotationsAsMap)
/*      */   {
/*  991 */     return getAnnotationAttributes(null, annotation, classValuesAsString, nestedAnnotationsAsMap);
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
/*      */   public static AnnotationAttributes getAnnotationAttributes(AnnotatedElement annotatedElement, Annotation annotation)
/*      */   {
/* 1008 */     return getAnnotationAttributes(annotatedElement, annotation, false, false);
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
/*      */   public static AnnotationAttributes getAnnotationAttributes(AnnotatedElement annotatedElement, Annotation annotation, boolean classValuesAsString, boolean nestedAnnotationsAsMap)
/*      */   {
/* 1032 */     return getAnnotationAttributes(annotatedElement, annotation, classValuesAsString, nestedAnnotationsAsMap);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static AnnotationAttributes getAnnotationAttributes(Object annotatedElement, Annotation annotation, boolean classValuesAsString, boolean nestedAnnotationsAsMap)
/*      */   {
/* 1040 */     AnnotationAttributes attributes = retrieveAnnotationAttributes(annotatedElement, annotation, classValuesAsString, nestedAnnotationsAsMap);
/* 1041 */     postProcessAnnotationAttributes(annotatedElement, attributes, classValuesAsString, nestedAnnotationsAsMap);
/* 1042 */     return attributes;
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
/*      */   static AnnotationAttributes retrieveAnnotationAttributes(Object annotatedElement, Annotation annotation, boolean classValuesAsString, boolean nestedAnnotationsAsMap)
/*      */   {
/* 1076 */     Class<? extends Annotation> annotationType = annotation.annotationType();
/* 1077 */     AnnotationAttributes attributes = new AnnotationAttributes(annotationType);
/*      */     
/* 1079 */     for (Method method : getAttributeMethods(annotationType)) {
/*      */       try {
/* 1081 */         Object attributeValue = method.invoke(annotation, new Object[0]);
/* 1082 */         Object defaultValue = method.getDefaultValue();
/* 1083 */         if ((defaultValue != null) && (ObjectUtils.nullSafeEquals(attributeValue, defaultValue))) {
/* 1084 */           attributeValue = new DefaultValueHolder(defaultValue);
/*      */         }
/* 1086 */         attributes.put(method.getName(), 
/* 1087 */           adaptValue(annotatedElement, attributeValue, classValuesAsString, nestedAnnotationsAsMap));
/*      */       }
/*      */       catch (Throwable ex) {
/* 1090 */         if ((ex instanceof InvocationTargetException)) {
/* 1091 */           Throwable targetException = ((InvocationTargetException)ex).getTargetException();
/* 1092 */           rethrowAnnotationConfigurationException(targetException);
/*      */         }
/* 1094 */         throw new IllegalStateException("Could not obtain annotation attribute value for " + method, ex);
/*      */       }
/*      */     }
/*      */     
/* 1098 */     return attributes;
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
/*      */   static Object adaptValue(Object annotatedElement, Object value, boolean classValuesAsString, boolean nestedAnnotationsAsMap)
/*      */   {
/* 1120 */     if (classValuesAsString) {
/* 1121 */       if ((value instanceof Class)) {
/* 1122 */         return ((Class)value).getName();
/*      */       }
/* 1124 */       if ((value instanceof Class[])) {
/* 1125 */         Class<?>[] clazzArray = (Class[])value;
/* 1126 */         String[] classNames = new String[clazzArray.length];
/* 1127 */         for (int i = 0; i < clazzArray.length; i++) {
/* 1128 */           classNames[i] = clazzArray[i].getName();
/*      */         }
/* 1130 */         return classNames;
/*      */       }
/*      */     }
/*      */     
/* 1134 */     if ((value instanceof Annotation)) {
/* 1135 */       Annotation annotation = (Annotation)value;
/* 1136 */       if (nestedAnnotationsAsMap) {
/* 1137 */         return getAnnotationAttributes(annotatedElement, annotation, classValuesAsString, true);
/*      */       }
/*      */       
/* 1140 */       return synthesizeAnnotation(annotation, annotatedElement);
/*      */     }
/*      */     
/*      */ 
/* 1144 */     if ((value instanceof Annotation[])) {
/* 1145 */       Annotation[] annotations = (Annotation[])value;
/* 1146 */       if (nestedAnnotationsAsMap) {
/* 1147 */         AnnotationAttributes[] mappedAnnotations = new AnnotationAttributes[annotations.length];
/* 1148 */         for (int i = 0; i < annotations.length; i++)
/*      */         {
/* 1150 */           mappedAnnotations[i] = getAnnotationAttributes(annotatedElement, annotations[i], classValuesAsString, true);
/*      */         }
/* 1152 */         return mappedAnnotations;
/*      */       }
/*      */       
/* 1155 */       return synthesizeAnnotationArray(annotations, annotatedElement);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1160 */     return value;
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
/*      */   public static void registerDefaultValues(AnnotationAttributes attributes)
/*      */   {
/* 1173 */     Class<? extends Annotation> annotationType = attributes.annotationType();
/* 1174 */     if ((annotationType != null) && (Modifier.isPublic(annotationType.getModifiers())))
/*      */     {
/* 1176 */       for (Method annotationAttribute : getAttributeMethods(annotationType)) {
/* 1177 */         String attributeName = annotationAttribute.getName();
/* 1178 */         Object defaultValue = annotationAttribute.getDefaultValue();
/* 1179 */         if ((defaultValue != null) && (!attributes.containsKey(attributeName))) {
/* 1180 */           if ((defaultValue instanceof Annotation)) {
/* 1181 */             defaultValue = getAnnotationAttributes((Annotation)defaultValue, false, true);
/*      */           }
/* 1183 */           else if ((defaultValue instanceof Annotation[])) {
/* 1184 */             Annotation[] realAnnotations = (Annotation[])defaultValue;
/* 1185 */             AnnotationAttributes[] mappedAnnotations = new AnnotationAttributes[realAnnotations.length];
/* 1186 */             for (int i = 0; i < realAnnotations.length; i++) {
/* 1187 */               mappedAnnotations[i] = getAnnotationAttributes(realAnnotations[i], false, true);
/*      */             }
/* 1189 */             defaultValue = mappedAnnotations;
/*      */           }
/* 1191 */           attributes.put(attributeName, new DefaultValueHolder(defaultValue));
/*      */         }
/*      */       }
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
/*      */   public static void postProcessAnnotationAttributes(Object annotatedElement, AnnotationAttributes attributes, boolean classValuesAsString)
/*      */   {
/* 1217 */     postProcessAnnotationAttributes(annotatedElement, attributes, classValuesAsString, false);
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
/*      */   static void postProcessAnnotationAttributes(Object annotatedElement, AnnotationAttributes attributes, boolean classValuesAsString, boolean nestedAnnotationsAsMap)
/*      */   {
/* 1244 */     if (attributes == null) {
/* 1245 */       return;
/*      */     }
/*      */     
/* 1248 */     Class<? extends Annotation> annotationType = attributes.annotationType();
/*      */     
/*      */ 
/*      */ 
/* 1252 */     Set<String> valuesAlreadyReplaced = new HashSet();
/*      */     Map<String, List<String>> aliasMap;
/* 1254 */     if (!attributes.validated)
/*      */     {
/* 1256 */       aliasMap = getAttributeAliasMap(annotationType);
/* 1257 */       for (Iterator localIterator1 = aliasMap.keySet().iterator(); localIterator1.hasNext();) { attributeName = (String)localIterator1.next();
/* 1258 */         if (!valuesAlreadyReplaced.contains(attributeName))
/*      */         {
/*      */ 
/* 1261 */           value = attributes.get(attributeName);
/* 1262 */           valuePresent = (value != null) && (!(value instanceof DefaultValueHolder));
/*      */           
/* 1264 */           for (String aliasedAttributeName : (List)aliasMap.get(attributeName))
/* 1265 */             if (!valuesAlreadyReplaced.contains(aliasedAttributeName))
/*      */             {
/*      */ 
/*      */ 
/* 1269 */               Object aliasedValue = attributes.get(aliasedAttributeName);
/* 1270 */               boolean aliasPresent = (aliasedValue != null) && (!(aliasedValue instanceof DefaultValueHolder));
/*      */               
/*      */ 
/* 1273 */               if ((valuePresent) || (aliasPresent))
/* 1274 */                 if ((valuePresent) && (aliasPresent))
/*      */                 {
/* 1276 */                   if (!ObjectUtils.nullSafeEquals(value, aliasedValue))
/*      */                   {
/* 1278 */                     String elementAsString = annotatedElement != null ? annotatedElement.toString() : "unknown element";
/* 1279 */                     throw new AnnotationConfigurationException(String.format("In AnnotationAttributes for annotation [%s] declared on %s, attribute '%s' and its alias '%s' are declared with values of [%s] and [%s], but only one is permitted.", new Object[] {annotationType
/*      */                     
/*      */ 
/* 1282 */                       .getName(), elementAsString, attributeName, aliasedAttributeName, 
/* 1283 */                       ObjectUtils.nullSafeToString(value), 
/* 1284 */                       ObjectUtils.nullSafeToString(aliasedValue) }));
/*      */                   }
/*      */                 }
/* 1287 */                 else if (aliasPresent)
/*      */                 {
/* 1289 */                   attributes.put(attributeName, 
/* 1290 */                     adaptValue(annotatedElement, aliasedValue, classValuesAsString, nestedAnnotationsAsMap));
/* 1291 */                   valuesAlreadyReplaced.add(attributeName);
/*      */                 }
/*      */                 else
/*      */                 {
/* 1295 */                   attributes.put(aliasedAttributeName, 
/* 1296 */                     adaptValue(annotatedElement, value, classValuesAsString, nestedAnnotationsAsMap));
/* 1297 */                   valuesAlreadyReplaced.add(aliasedAttributeName);
/*      */                 } } } }
/*      */       String attributeName;
/*      */       Object value;
/*      */       boolean valuePresent;
/* 1302 */       attributes.validated = true;
/*      */     }
/*      */     
/*      */ 
/* 1306 */     for (String attributeName : attributes.keySet()) {
/* 1307 */       if (!valuesAlreadyReplaced.contains(attributeName))
/*      */       {
/*      */ 
/* 1310 */         Object value = attributes.get(attributeName);
/* 1311 */         if ((value instanceof DefaultValueHolder)) {
/* 1312 */           value = ((DefaultValueHolder)value).defaultValue;
/* 1313 */           attributes.put(attributeName, 
/* 1314 */             adaptValue(annotatedElement, value, classValuesAsString, nestedAnnotationsAsMap));
/*      */         }
/*      */       }
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
/*      */   public static Object getValue(Annotation annotation)
/*      */   {
/* 1329 */     return getValue(annotation, "value");
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
/*      */   public static Object getValue(Annotation annotation, String attributeName)
/*      */   {
/* 1343 */     if ((annotation == null) || (!StringUtils.hasText(attributeName))) {
/* 1344 */       return null;
/*      */     }
/*      */     try {
/* 1347 */       Method method = annotation.annotationType().getDeclaredMethod(attributeName, new Class[0]);
/* 1348 */       ReflectionUtils.makeAccessible(method);
/* 1349 */       return method.invoke(annotation, new Object[0]);
/*      */     }
/*      */     catch (InvocationTargetException ex) {
/* 1352 */       rethrowAnnotationConfigurationException(ex.getTargetException());
/* 1353 */       throw new IllegalStateException("Could not obtain value for annotation attribute '" + attributeName + "' in " + annotation, ex);
/*      */     }
/*      */     catch (Throwable ex)
/*      */     {
/* 1357 */       handleIntrospectionFailure(annotation.getClass(), ex); }
/* 1358 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Object getDefaultValue(Annotation annotation)
/*      */   {
/* 1370 */     return getDefaultValue(annotation, "value");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Object getDefaultValue(Annotation annotation, String attributeName)
/*      */   {
/* 1381 */     if (annotation == null) {
/* 1382 */       return null;
/*      */     }
/* 1384 */     return getDefaultValue(annotation.annotationType(), attributeName);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Object getDefaultValue(Class<? extends Annotation> annotationType)
/*      */   {
/* 1395 */     return getDefaultValue(annotationType, "value");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Object getDefaultValue(Class<? extends Annotation> annotationType, String attributeName)
/*      */   {
/* 1407 */     if ((annotationType == null) || (!StringUtils.hasText(attributeName))) {
/* 1408 */       return null;
/*      */     }
/*      */     try {
/* 1411 */       return annotationType.getDeclaredMethod(attributeName, new Class[0]).getDefaultValue();
/*      */     }
/*      */     catch (Throwable ex) {
/* 1414 */       handleIntrospectionFailure(annotationType, ex); }
/* 1415 */     return null;
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
/*      */   static <A extends Annotation> A synthesizeAnnotation(A annotation)
/*      */   {
/* 1434 */     return synthesizeAnnotation(annotation, null);
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
/*      */   public static <A extends Annotation> A synthesizeAnnotation(A annotation, AnnotatedElement annotatedElement)
/*      */   {
/* 1455 */     return synthesizeAnnotation(annotation, annotatedElement);
/*      */   }
/*      */   
/*      */   static <A extends Annotation> A synthesizeAnnotation(A annotation, Object annotatedElement)
/*      */   {
/* 1460 */     if (annotation == null) {
/* 1461 */       return null;
/*      */     }
/* 1463 */     if ((annotation instanceof SynthesizedAnnotation)) {
/* 1464 */       return annotation;
/*      */     }
/*      */     
/* 1467 */     Class<? extends Annotation> annotationType = annotation.annotationType();
/* 1468 */     if (!isSynthesizable(annotationType)) {
/* 1469 */       return annotation;
/*      */     }
/*      */     
/* 1472 */     DefaultAnnotationAttributeExtractor attributeExtractor = new DefaultAnnotationAttributeExtractor(annotation, annotatedElement);
/*      */     
/* 1474 */     InvocationHandler handler = new SynthesizedAnnotationInvocationHandler(attributeExtractor);
/*      */     
/*      */ 
/*      */ 
/* 1478 */     Class<?>[] exposedInterfaces = { annotationType, SynthesizedAnnotation.class };
/* 1479 */     return (Annotation)Proxy.newProxyInstance(annotation.getClass().getClassLoader(), exposedInterfaces, handler);
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
/*      */ 
/*      */ 
/*      */   public static <A extends Annotation> A synthesizeAnnotation(Map<String, Object> attributes, Class<A> annotationType, AnnotatedElement annotatedElement)
/*      */   {
/* 1516 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/* 1517 */     if (attributes == null) {
/* 1518 */       return null;
/*      */     }
/*      */     
/* 1521 */     MapAnnotationAttributeExtractor attributeExtractor = new MapAnnotationAttributeExtractor(attributes, annotationType, annotatedElement);
/*      */     
/* 1523 */     InvocationHandler handler = new SynthesizedAnnotationInvocationHandler(attributeExtractor);
/* 1524 */     Class<?>[] exposedInterfaces = { canExposeSynthesizedMarker(annotationType) ? new Class[] { annotationType, SynthesizedAnnotation.class } : annotationType };
/*      */     
/* 1526 */     return (Annotation)Proxy.newProxyInstance(annotationType.getClassLoader(), exposedInterfaces, handler);
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
/*      */   public static <A extends Annotation> A synthesizeAnnotation(Class<A> annotationType)
/*      */   {
/* 1545 */     return synthesizeAnnotation(Collections.emptyMap(), annotationType, null);
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
/*      */   static Annotation[] synthesizeAnnotationArray(Annotation[] annotations, Object annotatedElement)
/*      */   {
/* 1565 */     if (annotations == null) {
/* 1566 */       return null;
/*      */     }
/*      */     
/* 1569 */     Annotation[] synthesized = (Annotation[])Array.newInstance(annotations
/* 1570 */       .getClass().getComponentType(), annotations.length);
/* 1571 */     for (int i = 0; i < annotations.length; i++) {
/* 1572 */       synthesized[i] = synthesizeAnnotation(annotations[i], annotatedElement);
/*      */     }
/* 1574 */     return synthesized;
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
/*      */   static <A extends Annotation> A[] synthesizeAnnotationArray(Map<String, Object>[] maps, Class<A> annotationType)
/*      */   {
/* 1596 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/* 1597 */     if (maps == null) {
/* 1598 */       return null;
/*      */     }
/*      */     
/* 1601 */     A[] synthesized = (Annotation[])Array.newInstance(annotationType, maps.length);
/* 1602 */     for (int i = 0; i < maps.length; i++) {
/* 1603 */       synthesized[i] = synthesizeAnnotation(maps[i], annotationType, null);
/*      */     }
/* 1605 */     return synthesized;
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
/*      */   static Map<String, List<String>> getAttributeAliasMap(Class<? extends Annotation> annotationType)
/*      */   {
/* 1628 */     if (annotationType == null) {
/* 1629 */       return Collections.emptyMap();
/*      */     }
/*      */     
/* 1632 */     Map<String, List<String>> map = (Map)attributeAliasesCache.get(annotationType);
/* 1633 */     if (map != null) {
/* 1634 */       return map;
/*      */     }
/*      */     
/* 1637 */     map = new LinkedHashMap();
/* 1638 */     for (Method attribute : getAttributeMethods(annotationType)) {
/* 1639 */       List<String> aliasNames = getAttributeAliasNames(attribute);
/* 1640 */       if (!aliasNames.isEmpty()) {
/* 1641 */         map.put(attribute.getName(), aliasNames);
/*      */       }
/*      */     }
/*      */     
/* 1645 */     attributeAliasesCache.put(annotationType, map);
/* 1646 */     return map;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static boolean canExposeSynthesizedMarker(Class<? extends Annotation> annotationType)
/*      */   {
/*      */     try
/*      */     {
/* 1655 */       return Class.forName(SynthesizedAnnotation.class.getName(), false, annotationType.getClassLoader()) == SynthesizedAnnotation.class;
/*      */     }
/*      */     catch (ClassNotFoundException ex) {}
/*      */     
/* 1659 */     return false;
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
/*      */   private static boolean isSynthesizable(Class<? extends Annotation> annotationType)
/*      */   {
/* 1678 */     Boolean synthesizable = (Boolean)synthesizableCache.get(annotationType);
/* 1679 */     if (synthesizable != null) {
/* 1680 */       return synthesizable.booleanValue();
/*      */     }
/*      */     
/* 1683 */     synthesizable = Boolean.FALSE;
/* 1684 */     for (Method attribute : getAttributeMethods(annotationType)) {
/* 1685 */       if (!getAttributeAliasNames(attribute).isEmpty()) {
/* 1686 */         synthesizable = Boolean.TRUE;
/* 1687 */         break;
/*      */       }
/* 1689 */       Class<?> returnType = attribute.getReturnType();
/* 1690 */       if (Annotation[].class.isAssignableFrom(returnType))
/*      */       {
/* 1692 */         Class<? extends Annotation> nestedAnnotationType = returnType.getComponentType();
/* 1693 */         if (isSynthesizable(nestedAnnotationType)) {
/* 1694 */           synthesizable = Boolean.TRUE;
/* 1695 */           break;
/*      */         }
/*      */       }
/* 1698 */       else if (Annotation.class.isAssignableFrom(returnType)) {
/* 1699 */         Class<? extends Annotation> nestedAnnotationType = returnType;
/* 1700 */         if (isSynthesizable(nestedAnnotationType)) {
/* 1701 */           synthesizable = Boolean.TRUE;
/* 1702 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1707 */     synthesizableCache.put(annotationType, synthesizable);
/* 1708 */     return synthesizable.booleanValue();
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
/*      */   static List<String> getAttributeAliasNames(Method attribute)
/*      */   {
/* 1725 */     Assert.notNull(attribute, "attribute must not be null");
/* 1726 */     AliasDescriptor descriptor = AliasDescriptor.from(attribute);
/* 1727 */     return descriptor != null ? descriptor.getAttributeAliasNames() : Collections.emptyList();
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
/*      */   static String getAttributeOverrideName(Method attribute, Class<? extends Annotation> metaAnnotationType)
/*      */   {
/* 1747 */     Assert.notNull(attribute, "attribute must not be null");
/* 1748 */     Assert.notNull(metaAnnotationType, "metaAnnotationType must not be null");
/* 1749 */     Assert.isTrue(Annotation.class != metaAnnotationType, "metaAnnotationType must not be [java.lang.annotation.Annotation]");
/*      */     
/*      */ 
/* 1752 */     AliasDescriptor descriptor = AliasDescriptor.from(attribute);
/* 1753 */     return descriptor != null ? descriptor.getAttributeOverrideName(metaAnnotationType) : null;
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
/*      */   static List<Method> getAttributeMethods(Class<? extends Annotation> annotationType)
/*      */   {
/* 1768 */     List<Method> methods = (List)attributeMethodsCache.get(annotationType);
/* 1769 */     if (methods != null) {
/* 1770 */       return methods;
/*      */     }
/*      */     
/* 1773 */     methods = new ArrayList();
/* 1774 */     for (Method method : annotationType.getDeclaredMethods()) {
/* 1775 */       if (isAttributeMethod(method)) {
/* 1776 */         ReflectionUtils.makeAccessible(method);
/* 1777 */         methods.add(method);
/*      */       }
/*      */     }
/*      */     
/* 1781 */     attributeMethodsCache.put(annotationType, methods);
/* 1782 */     return methods;
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
/*      */   static Annotation getAnnotation(AnnotatedElement element, String annotationName)
/*      */   {
/* 1795 */     for (Annotation annotation : element.getAnnotations()) {
/* 1796 */       if (annotation.annotationType().getName().equals(annotationName)) {
/* 1797 */         return annotation;
/*      */       }
/*      */     }
/* 1800 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static boolean isAttributeMethod(Method method)
/*      */   {
/* 1810 */     return (method != null) && (method.getParameterTypes().length == 0) && (method.getReturnType() != Void.TYPE);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static boolean isAnnotationTypeMethod(Method method)
/*      */   {
/* 1820 */     return (method != null) && (method.getName().equals("annotationType")) && (method.getParameterTypes().length == 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static Class<? extends Annotation> resolveContainerAnnotationType(Class<? extends Annotation> annotationType)
/*      */   {
/*      */     try
/*      */     {
/* 1834 */       Annotation repeatable = getAnnotation(annotationType, "java.lang.annotation.Repeatable");
/* 1835 */       if (repeatable != null) {
/* 1836 */         Object value = getValue(repeatable);
/* 1837 */         return (Class)value;
/*      */       }
/*      */     }
/*      */     catch (Exception ex) {
/* 1841 */       handleIntrospectionFailure(annotationType, ex);
/*      */     }
/* 1843 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static void rethrowAnnotationConfigurationException(Throwable ex)
/*      */   {
/* 1855 */     if ((ex instanceof AnnotationConfigurationException)) {
/* 1856 */       throw ((AnnotationConfigurationException)ex);
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
/*      */   static void handleIntrospectionFailure(AnnotatedElement element, Throwable ex)
/*      */   {
/* 1875 */     rethrowAnnotationConfigurationException(ex);
/*      */     
/* 1877 */     Log loggerToUse = logger;
/* 1878 */     if (loggerToUse == null) {
/* 1879 */       loggerToUse = LogFactory.getLog(AnnotationUtils.class);
/* 1880 */       logger = loggerToUse;
/*      */     }
/* 1882 */     if (((element instanceof Class)) && (Annotation.class.isAssignableFrom((Class)element)))
/*      */     {
/* 1884 */       if (loggerToUse.isDebugEnabled()) {
/* 1885 */         loggerToUse.debug("Failed to meta-introspect annotation [" + element + "]: " + ex);
/*      */       }
/*      */       
/*      */ 
/*      */     }
/* 1890 */     else if (loggerToUse.isInfoEnabled()) {
/* 1891 */       loggerToUse.info("Failed to introspect annotations on [" + element + "]: " + ex);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static final class AnnotationCacheKey
/*      */     implements Comparable<AnnotationCacheKey>
/*      */   {
/*      */     private final AnnotatedElement element;
/*      */     
/*      */     private final Class<? extends Annotation> annotationType;
/*      */     
/*      */ 
/*      */     public AnnotationCacheKey(AnnotatedElement element, Class<? extends Annotation> annotationType)
/*      */     {
/* 1907 */       this.element = element;
/* 1908 */       this.annotationType = annotationType;
/*      */     }
/*      */     
/*      */     public boolean equals(Object other)
/*      */     {
/* 1913 */       if (this == other) {
/* 1914 */         return true;
/*      */       }
/* 1916 */       if (!(other instanceof AnnotationCacheKey)) {
/* 1917 */         return false;
/*      */       }
/* 1919 */       AnnotationCacheKey otherKey = (AnnotationCacheKey)other;
/* 1920 */       return (this.element.equals(otherKey.element)) && (this.annotationType.equals(otherKey.annotationType));
/*      */     }
/*      */     
/*      */     public int hashCode()
/*      */     {
/* 1925 */       return this.element.hashCode() * 29 + this.annotationType.hashCode();
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/* 1930 */       return "@" + this.annotationType + " on " + this.element;
/*      */     }
/*      */     
/*      */     public int compareTo(AnnotationCacheKey other)
/*      */     {
/* 1935 */       int result = this.element.toString().compareTo(other.element.toString());
/* 1936 */       if (result == 0) {
/* 1937 */         result = this.annotationType.getName().compareTo(other.annotationType.getName());
/*      */       }
/* 1939 */       return result;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static class AnnotationCollector<A extends Annotation>
/*      */   {
/*      */     private final Class<A> annotationType;
/*      */     
/*      */     private final Class<? extends Annotation> containerAnnotationType;
/*      */     
/*      */     private final boolean declaredMode;
/*      */     
/* 1952 */     private final Set<AnnotatedElement> visited = new HashSet();
/*      */     
/* 1954 */     private final Set<A> result = new LinkedHashSet();
/*      */     
/*      */     AnnotationCollector(Class<A> annotationType, Class<? extends Annotation> containerAnnotationType, boolean declaredMode) {
/* 1957 */       this.annotationType = annotationType;
/* 1958 */       this.containerAnnotationType = (containerAnnotationType != null ? containerAnnotationType : 
/* 1959 */         AnnotationUtils.resolveContainerAnnotationType(annotationType));
/* 1960 */       this.declaredMode = declaredMode;
/*      */     }
/*      */     
/*      */     Set<A> getResult(AnnotatedElement element) {
/* 1964 */       process(element);
/* 1965 */       return Collections.unmodifiableSet(this.result);
/*      */     }
/*      */     
/*      */     private void process(AnnotatedElement element)
/*      */     {
/* 1970 */       if (this.visited.add(element)) {
/*      */         try {
/* 1972 */           Annotation[] annotations = this.declaredMode ? element.getDeclaredAnnotations() : element.getAnnotations();
/* 1973 */           for (Annotation ann : annotations) {
/* 1974 */             Class<? extends Annotation> currentAnnotationType = ann.annotationType();
/* 1975 */             if (ObjectUtils.nullSafeEquals(this.annotationType, currentAnnotationType)) {
/* 1976 */               this.result.add(AnnotationUtils.synthesizeAnnotation(ann, element));
/*      */             }
/* 1978 */             else if (ObjectUtils.nullSafeEquals(this.containerAnnotationType, currentAnnotationType)) {
/* 1979 */               this.result.addAll(getValue(element, ann));
/*      */             }
/* 1981 */             else if (!AnnotationUtils.isInJavaLangAnnotationPackage(currentAnnotationType)) {
/* 1982 */               process(currentAnnotationType);
/*      */             }
/*      */           }
/*      */         }
/*      */         catch (Throwable ex) {
/* 1987 */           AnnotationUtils.handleIntrospectionFailure(element, ex);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     private List<A> getValue(AnnotatedElement element, Annotation annotation)
/*      */     {
/*      */       try {
/* 1995 */         List<A> synthesizedAnnotations = new ArrayList();
/* 1996 */         for (A anno : (Annotation[])AnnotationUtils.getValue(annotation)) {
/* 1997 */           synthesizedAnnotations.add(AnnotationUtils.synthesizeAnnotation(anno, element));
/*      */         }
/* 1999 */         return synthesizedAnnotations;
/*      */       }
/*      */       catch (Throwable ex) {
/* 2002 */         AnnotationUtils.handleIntrospectionFailure(element, ex);
/*      */       }
/*      */       
/* 2005 */       return Collections.emptyList();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class AliasDescriptor
/*      */   {
/*      */     private final Method sourceAttribute;
/*      */     
/*      */ 
/*      */ 
/*      */     private final Class<? extends Annotation> sourceAnnotationType;
/*      */     
/*      */ 
/*      */ 
/*      */     private final String sourceAttributeName;
/*      */     
/*      */ 
/*      */ 
/*      */     private final Method aliasedAttribute;
/*      */     
/*      */ 
/*      */ 
/*      */     private final Class<? extends Annotation> aliasedAnnotationType;
/*      */     
/*      */ 
/*      */ 
/*      */     private final String aliasedAttributeName;
/*      */     
/*      */ 
/*      */ 
/*      */     private final boolean isAliasPair;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static AliasDescriptor from(Method attribute)
/*      */     {
/* 2046 */       AliasDescriptor descriptor = (AliasDescriptor)AnnotationUtils.aliasDescriptorCache.get(attribute);
/* 2047 */       if (descriptor != null) {
/* 2048 */         return descriptor;
/*      */       }
/*      */       
/* 2051 */       AliasFor aliasFor = (AliasFor)attribute.getAnnotation(AliasFor.class);
/* 2052 */       if (aliasFor == null) {
/* 2053 */         return null;
/*      */       }
/*      */       
/* 2056 */       descriptor = new AliasDescriptor(attribute, aliasFor);
/* 2057 */       descriptor.validate();
/* 2058 */       AnnotationUtils.aliasDescriptorCache.put(attribute, descriptor);
/* 2059 */       return descriptor;
/*      */     }
/*      */     
/*      */     private AliasDescriptor(Method sourceAttribute, AliasFor aliasFor)
/*      */     {
/* 2064 */       Class<?> declaringClass = sourceAttribute.getDeclaringClass();
/* 2065 */       Assert.isTrue(declaringClass.isAnnotation(), "sourceAttribute must be from an annotation");
/*      */       
/* 2067 */       this.sourceAttribute = sourceAttribute;
/* 2068 */       this.sourceAnnotationType = declaringClass;
/* 2069 */       this.sourceAttributeName = sourceAttribute.getName();
/*      */       
/*      */ 
/* 2072 */       this.aliasedAnnotationType = (Annotation.class == aliasFor.annotation() ? this.sourceAnnotationType : aliasFor.annotation());
/* 2073 */       this.aliasedAttributeName = getAliasedAttributeName(aliasFor, sourceAttribute);
/* 2074 */       if ((this.aliasedAnnotationType == this.sourceAnnotationType) && 
/* 2075 */         (this.aliasedAttributeName.equals(this.sourceAttributeName))) {
/* 2076 */         String msg = String.format("@AliasFor declaration on attribute '%s' in annotation [%s] points to itself. Specify 'annotation' to point to a same-named attribute on a meta-annotation.", new Object[] {sourceAttribute
/*      */         
/* 2078 */           .getName(), declaringClass.getName() });
/* 2079 */         throw new AnnotationConfigurationException(msg);
/*      */       }
/*      */       try {
/* 2082 */         this.aliasedAttribute = this.aliasedAnnotationType.getDeclaredMethod(this.aliasedAttributeName, new Class[0]);
/*      */       }
/*      */       catch (NoSuchMethodException ex) {
/* 2085 */         String msg = String.format("Attribute '%s' in annotation [%s] is declared as an @AliasFor nonexistent attribute '%s' in annotation [%s].", new Object[] { this.sourceAttributeName, this.sourceAnnotationType
/*      */         
/* 2087 */           .getName(), this.aliasedAttributeName, this.aliasedAnnotationType
/* 2088 */           .getName() });
/* 2089 */         throw new AnnotationConfigurationException(msg, ex);
/*      */       }
/*      */       
/* 2092 */       this.isAliasPair = (this.sourceAnnotationType == this.aliasedAnnotationType);
/*      */     }
/*      */     
/*      */     private void validate()
/*      */     {
/* 2097 */       if ((!this.isAliasPair) && (!AnnotationUtils.isAnnotationMetaPresent(this.sourceAnnotationType, this.aliasedAnnotationType))) {
/* 2098 */         String msg = String.format("@AliasFor declaration on attribute '%s' in annotation [%s] declares an alias for attribute '%s' in meta-annotation [%s] which is not meta-present.", new Object[] { this.sourceAttributeName, this.sourceAnnotationType
/*      */         
/* 2100 */           .getName(), this.aliasedAttributeName, this.aliasedAnnotationType
/* 2101 */           .getName() });
/* 2102 */         throw new AnnotationConfigurationException(msg);
/*      */       }
/*      */       
/* 2105 */       if (this.isAliasPair) {
/* 2106 */         AliasFor mirrorAliasFor = (AliasFor)this.aliasedAttribute.getAnnotation(AliasFor.class);
/* 2107 */         if (mirrorAliasFor == null) {
/* 2108 */           String msg = String.format("Attribute '%s' in annotation [%s] must be declared as an @AliasFor [%s].", new Object[] { this.aliasedAttributeName, this.sourceAnnotationType
/* 2109 */             .getName(), this.sourceAttributeName });
/* 2110 */           throw new AnnotationConfigurationException(msg);
/*      */         }
/*      */         
/* 2113 */         String mirrorAliasedAttributeName = getAliasedAttributeName(mirrorAliasFor, this.aliasedAttribute);
/* 2114 */         if (!this.sourceAttributeName.equals(mirrorAliasedAttributeName)) {
/* 2115 */           String msg = String.format("Attribute '%s' in annotation [%s] must be declared as an @AliasFor [%s], not [%s].", new Object[] { this.aliasedAttributeName, this.sourceAnnotationType
/* 2116 */             .getName(), this.sourceAttributeName, mirrorAliasedAttributeName });
/*      */           
/* 2118 */           throw new AnnotationConfigurationException(msg);
/*      */         }
/*      */       }
/*      */       
/* 2122 */       Class<?> returnType = this.sourceAttribute.getReturnType();
/* 2123 */       Class<?> aliasedReturnType = this.aliasedAttribute.getReturnType();
/* 2124 */       if ((returnType != aliasedReturnType) && (
/* 2125 */         (!aliasedReturnType.isArray()) || (returnType != aliasedReturnType.getComponentType()))) {
/* 2126 */         String msg = String.format("Misconfigured aliases: attribute '%s' in annotation [%s] and attribute '%s' in annotation [%s] must declare the same return type.", new Object[] { this.sourceAttributeName, this.sourceAnnotationType
/*      */         
/* 2128 */           .getName(), this.aliasedAttributeName, this.aliasedAnnotationType
/* 2129 */           .getName() });
/* 2130 */         throw new AnnotationConfigurationException(msg);
/*      */       }
/*      */       
/* 2133 */       if (this.isAliasPair) {
/* 2134 */         validateDefaultValueConfiguration(this.aliasedAttribute);
/*      */       }
/*      */     }
/*      */     
/*      */     private void validateDefaultValueConfiguration(Method aliasedAttribute) {
/* 2139 */       Assert.notNull(aliasedAttribute, "aliasedAttribute must not be null");
/* 2140 */       Object defaultValue = this.sourceAttribute.getDefaultValue();
/* 2141 */       Object aliasedDefaultValue = aliasedAttribute.getDefaultValue();
/*      */       
/* 2143 */       if ((defaultValue == null) || (aliasedDefaultValue == null)) {
/* 2144 */         String msg = String.format("Misconfigured aliases: attribute '%s' in annotation [%s] and attribute '%s' in annotation [%s] must declare default values.", new Object[] { this.sourceAttributeName, this.sourceAnnotationType
/*      */         
/* 2146 */           .getName(), aliasedAttribute.getName(), aliasedAttribute
/* 2147 */           .getDeclaringClass().getName() });
/* 2148 */         throw new AnnotationConfigurationException(msg);
/*      */       }
/*      */       
/* 2151 */       if (!ObjectUtils.nullSafeEquals(defaultValue, aliasedDefaultValue)) {
/* 2152 */         String msg = String.format("Misconfigured aliases: attribute '%s' in annotation [%s] and attribute '%s' in annotation [%s] must declare the same default value.", new Object[] { this.sourceAttributeName, this.sourceAnnotationType
/*      */         
/* 2154 */           .getName(), aliasedAttribute.getName(), aliasedAttribute
/* 2155 */           .getDeclaringClass().getName() });
/* 2156 */         throw new AnnotationConfigurationException(msg);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private void validateAgainst(AliasDescriptor otherDescriptor)
/*      */     {
/* 2167 */       validateDefaultValueConfiguration(otherDescriptor.sourceAttribute);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private boolean isOverrideFor(Class<? extends Annotation> metaAnnotationType)
/*      */     {
/* 2176 */       return this.aliasedAnnotationType == metaAnnotationType;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private boolean isAliasFor(AliasDescriptor otherDescriptor)
/*      */     {
/* 2191 */       for (AliasDescriptor lhs = this; lhs != null; lhs = lhs.getAttributeOverrideDescriptor()) {
/* 2192 */         for (AliasDescriptor rhs = otherDescriptor; rhs != null; rhs = rhs.getAttributeOverrideDescriptor()) {
/* 2193 */           if (lhs.aliasedAttribute.equals(rhs.aliasedAttribute)) {
/* 2194 */             return true;
/*      */           }
/*      */         }
/*      */       }
/* 2198 */       return false;
/*      */     }
/*      */     
/*      */     public List<String> getAttributeAliasNames()
/*      */     {
/* 2203 */       if (this.isAliasPair) {
/* 2204 */         return Collections.singletonList(this.aliasedAttributeName);
/*      */       }
/*      */       
/*      */ 
/* 2208 */       List<String> aliases = new ArrayList();
/* 2209 */       for (AliasDescriptor otherDescriptor : getOtherDescriptors()) {
/* 2210 */         if (isAliasFor(otherDescriptor)) {
/* 2211 */           validateAgainst(otherDescriptor);
/* 2212 */           aliases.add(otherDescriptor.sourceAttributeName);
/*      */         }
/*      */       }
/* 2215 */       return aliases;
/*      */     }
/*      */     
/*      */     private List<AliasDescriptor> getOtherDescriptors() {
/* 2219 */       List<AliasDescriptor> otherDescriptors = new ArrayList();
/* 2220 */       for (Method currentAttribute : AnnotationUtils.getAttributeMethods(this.sourceAnnotationType)) {
/* 2221 */         if (!this.sourceAttribute.equals(currentAttribute)) {
/* 2222 */           AliasDescriptor otherDescriptor = from(currentAttribute);
/* 2223 */           if (otherDescriptor != null) {
/* 2224 */             otherDescriptors.add(otherDescriptor);
/*      */           }
/*      */         }
/*      */       }
/* 2228 */       return otherDescriptors;
/*      */     }
/*      */     
/*      */     public String getAttributeOverrideName(Class<? extends Annotation> metaAnnotationType) {
/* 2232 */       Assert.notNull(metaAnnotationType, "metaAnnotationType must not be null");
/* 2233 */       Assert.isTrue(Annotation.class != metaAnnotationType, "metaAnnotationType must not be [java.lang.annotation.Annotation]");
/*      */       
/*      */ 
/*      */ 
/* 2237 */       for (AliasDescriptor desc = this; desc != null; desc = desc.getAttributeOverrideDescriptor()) {
/* 2238 */         if (desc.isOverrideFor(metaAnnotationType)) {
/* 2239 */           return desc.aliasedAttributeName;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 2244 */       return null;
/*      */     }
/*      */     
/*      */     private AliasDescriptor getAttributeOverrideDescriptor() {
/* 2248 */       if (this.isAliasPair) {
/* 2249 */         return null;
/*      */       }
/* 2251 */       return from(this.aliasedAttribute);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private String getAliasedAttributeName(AliasFor aliasFor, Method attribute)
/*      */     {
/* 2271 */       String attributeName = aliasFor.attribute();
/* 2272 */       String value = aliasFor.value();
/* 2273 */       boolean attributeDeclared = StringUtils.hasText(attributeName);
/* 2274 */       boolean valueDeclared = StringUtils.hasText(value);
/*      */       
/*      */ 
/* 2277 */       if ((attributeDeclared) && (valueDeclared)) {
/* 2278 */         String msg = String.format("In @AliasFor declared on attribute '%s' in annotation [%s], attribute 'attribute' and its alias 'value' are present with values of [%s] and [%s], but only one is permitted.", new Object[] {attribute
/*      */         
/* 2280 */           .getName(), attribute.getDeclaringClass().getName(), attributeName, value });
/* 2281 */         throw new AnnotationConfigurationException(msg);
/*      */       }
/*      */       
/*      */ 
/* 2285 */       attributeName = attributeDeclared ? attributeName : value;
/* 2286 */       return StringUtils.hasText(attributeName) ? attributeName.trim() : attribute.getName();
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/* 2291 */       return String.format("%s: @%s(%s) is an alias for @%s(%s)", new Object[] { getClass().getSimpleName(), this.sourceAnnotationType
/* 2292 */         .getSimpleName(), this.sourceAttributeName, this.aliasedAnnotationType
/* 2293 */         .getSimpleName(), this.aliasedAttributeName });
/*      */     }
/*      */   }
/*      */   
/*      */   private static class DefaultValueHolder
/*      */   {
/*      */     final Object defaultValue;
/*      */     
/*      */     public DefaultValueHolder(Object defaultValue)
/*      */     {
/* 2303 */       this.defaultValue = defaultValue;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\annotation\AnnotationUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */