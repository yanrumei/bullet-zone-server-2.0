/*     */ package org.springframework.boot.autoconfigure.condition;
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
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.HierarchicalBeanFactory;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.ConditionContext;
/*     */ import org.springframework.context.annotation.ConfigurationCondition;
/*     */ import org.springframework.context.annotation.ConfigurationCondition.ConfigurationPhase;
/*     */ import org.springframework.core.annotation.Order;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*     */ import org.springframework.core.type.MethodMetadata;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ @Order(Integer.MAX_VALUE)
/*     */ class OnBeanCondition
/*     */   extends SpringBootCondition
/*     */   implements ConfigurationCondition
/*     */ {
/*     */   public static final String FACTORY_BEAN_OBJECT_TYPE = "factoryBeanObjectType";
/*     */   
/*     */   public ConfigurationCondition.ConfigurationPhase getConfigurationPhase()
/*     */   {
/*  68 */     return ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN;
/*     */   }
/*     */   
/*     */ 
/*     */   public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*     */   {
/*  74 */     ConditionMessage matchMessage = ConditionMessage.empty();
/*  75 */     if (metadata.isAnnotated(ConditionalOnBean.class.getName())) {
/*  76 */       BeanSearchSpec spec = new BeanSearchSpec(context, metadata, ConditionalOnBean.class);
/*     */       
/*  78 */       List<String> matching = getMatchingBeans(context, spec);
/*  79 */       if (matching.isEmpty()) {
/*  80 */         return ConditionOutcome.noMatch(
/*  81 */           ConditionMessage.forCondition(ConditionalOnBean.class, new Object[] { spec })
/*  82 */           .didNotFind("any beans").atAll());
/*     */       }
/*     */       
/*  85 */       matchMessage = matchMessage.andCondition(ConditionalOnBean.class, new Object[] { spec }).found("bean", "beans").items(ConditionMessage.Style.QUOTE, matching);
/*     */     }
/*  87 */     if (metadata.isAnnotated(ConditionalOnSingleCandidate.class.getName())) {
/*  88 */       BeanSearchSpec spec = new SingleCandidateBeanSearchSpec(context, metadata, ConditionalOnSingleCandidate.class);
/*     */       
/*  90 */       List<String> matching = getMatchingBeans(context, spec);
/*  91 */       if (matching.isEmpty()) {
/*  92 */         return ConditionOutcome.noMatch(
/*  93 */           ConditionMessage.forCondition(ConditionalOnSingleCandidate.class, new Object[] { spec })
/*  94 */           .didNotFind("any beans").atAll());
/*     */       }
/*  96 */       if (!hasSingleAutowireCandidate(context.getBeanFactory(), matching, spec
/*  97 */         .getStrategy() == SearchStrategy.ALL)) {
/*  98 */         return ConditionOutcome.noMatch(
/*  99 */           ConditionMessage.forCondition(ConditionalOnSingleCandidate.class, new Object[] { spec })
/* 100 */           .didNotFind("a primary bean from beans")
/* 101 */           .items(ConditionMessage.Style.QUOTE, matching));
/*     */       }
/*     */       
/*     */ 
/* 105 */       matchMessage = matchMessage.andCondition(ConditionalOnSingleCandidate.class, new Object[] { spec }).found("a primary bean from beans").items(ConditionMessage.Style.QUOTE, matching);
/*     */     }
/* 107 */     if (metadata.isAnnotated(ConditionalOnMissingBean.class.getName())) {
/* 108 */       BeanSearchSpec spec = new BeanSearchSpec(context, metadata, ConditionalOnMissingBean.class);
/*     */       
/* 110 */       List<String> matching = getMatchingBeans(context, spec);
/* 111 */       if (!matching.isEmpty()) {
/* 112 */         return ConditionOutcome.noMatch(
/* 113 */           ConditionMessage.forCondition(ConditionalOnMissingBean.class, new Object[] { spec })
/* 114 */           .found("bean", "beans").items(ConditionMessage.Style.QUOTE, matching));
/*     */       }
/*     */       
/* 117 */       matchMessage = matchMessage.andCondition(ConditionalOnMissingBean.class, new Object[] { spec }).didNotFind("any beans").atAll();
/*     */     }
/* 119 */     return ConditionOutcome.match(matchMessage);
/*     */   }
/*     */   
/*     */ 
/*     */   private List<String> getMatchingBeans(ConditionContext context, BeanSearchSpec beans)
/*     */   {
/* 125 */     ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
/* 126 */     if ((beans.getStrategy() == SearchStrategy.PARENTS) || 
/* 127 */       (beans.getStrategy() == SearchStrategy.ANCESTORS)) {
/* 128 */       BeanFactory parent = beanFactory.getParentBeanFactory();
/* 129 */       Assert.isInstanceOf(ConfigurableListableBeanFactory.class, parent, "Unable to use SearchStrategy.PARENTS");
/*     */       
/* 131 */       beanFactory = (ConfigurableListableBeanFactory)parent;
/*     */     }
/* 133 */     if (beanFactory == null) {
/* 134 */       return Collections.emptyList();
/*     */     }
/* 136 */     List<String> beanNames = new ArrayList();
/* 137 */     boolean considerHierarchy = beans.getStrategy() != SearchStrategy.CURRENT;
/* 138 */     for (String type : beans.getTypes()) {
/* 139 */       beanNames.addAll(getBeanNamesForType(beanFactory, type, context
/* 140 */         .getClassLoader(), considerHierarchy));
/*     */     }
/* 142 */     for (String ignoredType : beans.getIgnoredTypes()) {
/* 143 */       beanNames.removeAll(getBeanNamesForType(beanFactory, ignoredType, context
/* 144 */         .getClassLoader(), considerHierarchy));
/*     */     }
/* 146 */     for (String annotation : beans.getAnnotations()) {
/* 147 */       beanNames.addAll(Arrays.asList(getBeanNamesForAnnotation(beanFactory, annotation, context
/* 148 */         .getClassLoader(), considerHierarchy)));
/*     */     }
/* 150 */     for (String beanName : beans.getNames()) {
/* 151 */       if (containsBean(beanFactory, beanName, considerHierarchy)) {
/* 152 */         beanNames.add(beanName);
/*     */       }
/*     */     }
/* 155 */     return beanNames;
/*     */   }
/*     */   
/*     */   private boolean containsBean(ConfigurableListableBeanFactory beanFactory, String beanName, boolean considerHierarchy)
/*     */   {
/* 160 */     if (considerHierarchy) {
/* 161 */       return beanFactory.containsBean(beanName);
/*     */     }
/* 163 */     return beanFactory.containsLocalBean(beanName);
/*     */   }
/*     */   
/*     */   private Collection<String> getBeanNamesForType(ListableBeanFactory beanFactory, String type, ClassLoader classLoader, boolean considerHierarchy) throws LinkageError
/*     */   {
/*     */     try
/*     */     {
/* 170 */       Set<String> result = new LinkedHashSet();
/* 171 */       collectBeanNamesForType(result, beanFactory, 
/* 172 */         ClassUtils.forName(type, classLoader), considerHierarchy);
/* 173 */       return result;
/*     */     }
/*     */     catch (ClassNotFoundException ex) {
/* 176 */       return Collections.emptySet();
/*     */     }
/*     */     catch (NoClassDefFoundError ex) {}
/* 179 */     return Collections.emptySet();
/*     */   }
/*     */   
/*     */ 
/*     */   private void collectBeanNamesForType(Set<String> result, ListableBeanFactory beanFactory, Class<?> type, boolean considerHierarchy)
/*     */   {
/* 185 */     result.addAll(BeanTypeRegistry.get(beanFactory).getNamesForType(type));
/* 186 */     if ((considerHierarchy) && ((beanFactory instanceof HierarchicalBeanFactory)))
/*     */     {
/* 188 */       BeanFactory parent = ((HierarchicalBeanFactory)beanFactory).getParentBeanFactory();
/* 189 */       if ((parent instanceof ListableBeanFactory)) {
/* 190 */         collectBeanNamesForType(result, (ListableBeanFactory)parent, type, considerHierarchy);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private String[] getBeanNamesForAnnotation(ConfigurableListableBeanFactory beanFactory, String type, ClassLoader classLoader, boolean considerHierarchy)
/*     */     throws LinkageError
/*     */   {
/* 199 */     Set<String> names = new HashSet();
/*     */     
/*     */     try
/*     */     {
/* 203 */       Class<? extends Annotation> annotationType = ClassUtils.forName(type, classLoader);
/* 204 */       collectBeanNamesForAnnotation(names, beanFactory, annotationType, considerHierarchy);
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {}
/*     */     
/*     */ 
/*     */ 
/* 210 */     return StringUtils.toStringArray(names);
/*     */   }
/*     */   
/*     */ 
/*     */   private void collectBeanNamesForAnnotation(Set<String> names, ListableBeanFactory beanFactory, Class<? extends Annotation> annotationType, boolean considerHierarchy)
/*     */   {
/* 216 */     names.addAll(
/* 217 */       BeanTypeRegistry.get(beanFactory).getNamesForAnnotation(annotationType));
/* 218 */     if (considerHierarchy)
/*     */     {
/* 220 */       BeanFactory parent = ((HierarchicalBeanFactory)beanFactory).getParentBeanFactory();
/* 221 */       if ((parent instanceof ListableBeanFactory)) {
/* 222 */         collectBeanNamesForAnnotation(names, (ListableBeanFactory)parent, annotationType, considerHierarchy);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean hasSingleAutowireCandidate(ConfigurableListableBeanFactory beanFactory, List<String> beanNames, boolean considerHierarchy)
/*     */   {
/* 231 */     return (beanNames.size() == 1) || 
/*     */     
/* 233 */       (getPrimaryBeans(beanFactory, beanNames, considerHierarchy).size() == 1);
/*     */   }
/*     */   
/*     */   private List<String> getPrimaryBeans(ConfigurableListableBeanFactory beanFactory, List<String> beanNames, boolean considerHierarchy)
/*     */   {
/* 238 */     List<String> primaryBeans = new ArrayList();
/* 239 */     for (String beanName : beanNames) {
/* 240 */       BeanDefinition beanDefinition = findBeanDefinition(beanFactory, beanName, considerHierarchy);
/*     */       
/* 242 */       if ((beanDefinition != null) && (beanDefinition.isPrimary())) {
/* 243 */         primaryBeans.add(beanName);
/*     */       }
/*     */     }
/* 246 */     return primaryBeans;
/*     */   }
/*     */   
/*     */   private BeanDefinition findBeanDefinition(ConfigurableListableBeanFactory beanFactory, String beanName, boolean considerHierarchy)
/*     */   {
/* 251 */     if (beanFactory.containsBeanDefinition(beanName)) {
/* 252 */       return beanFactory.getBeanDefinition(beanName);
/*     */     }
/* 254 */     if ((considerHierarchy) && 
/* 255 */       ((beanFactory.getParentBeanFactory() instanceof ConfigurableListableBeanFactory))) {
/* 256 */       return findBeanDefinition(
/* 257 */         (ConfigurableListableBeanFactory)beanFactory.getParentBeanFactory(), beanName, considerHierarchy);
/*     */     }
/* 259 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   private static class BeanSearchSpec
/*     */   {
/*     */     private final Class<?> annotationType;
/*     */     
/* 267 */     private final List<String> names = new ArrayList();
/*     */     
/* 269 */     private final List<String> types = new ArrayList();
/*     */     
/* 271 */     private final List<String> annotations = new ArrayList();
/*     */     
/* 273 */     private final List<String> ignoredTypes = new ArrayList();
/*     */     
/*     */     private final SearchStrategy strategy;
/*     */     
/*     */     BeanSearchSpec(ConditionContext context, AnnotatedTypeMetadata metadata, Class<?> annotationType)
/*     */     {
/* 279 */       this.annotationType = annotationType;
/*     */       
/* 281 */       MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(annotationType.getName(), true);
/* 282 */       collect(attributes, "name", this.names);
/* 283 */       collect(attributes, "value", this.types);
/* 284 */       collect(attributes, "type", this.types);
/* 285 */       collect(attributes, "annotation", this.annotations);
/* 286 */       collect(attributes, "ignored", this.ignoredTypes);
/* 287 */       collect(attributes, "ignoredType", this.ignoredTypes);
/*     */       
/* 289 */       this.strategy = ((SearchStrategy)metadata.getAnnotationAttributes(annotationType.getName()).get("search"));
/* 290 */       OnBeanCondition.BeanTypeDeductionException deductionException = null;
/*     */       try {
/* 292 */         if ((this.types.isEmpty()) && (this.names.isEmpty())) {
/* 293 */           addDeducedBeanType(context, metadata, this.types);
/*     */         }
/*     */       }
/*     */       catch (OnBeanCondition.BeanTypeDeductionException ex) {
/* 297 */         deductionException = ex;
/*     */       }
/* 299 */       validate(deductionException);
/*     */     }
/*     */     
/*     */     protected void validate(OnBeanCondition.BeanTypeDeductionException ex) {
/* 303 */       if (!hasAtLeastOne(new List[] { this.types, this.names, this.annotations })) {
/* 304 */         String message = annotationName() + " did not specify a bean using type, name or annotation";
/*     */         
/* 306 */         if (ex == null) {
/* 307 */           throw new IllegalStateException(message);
/*     */         }
/* 309 */         throw new IllegalStateException(message + " and the attempt to deduce the bean's type failed", ex);
/*     */       }
/*     */     }
/*     */     
/*     */     private boolean hasAtLeastOne(List<?>... lists)
/*     */     {
/* 315 */       for (List<?> list : lists) {
/* 316 */         if (!list.isEmpty()) {
/* 317 */           return true;
/*     */         }
/*     */       }
/* 320 */       return false;
/*     */     }
/*     */     
/*     */     protected String annotationName() {
/* 324 */       return "@" + ClassUtils.getShortName(this.annotationType);
/*     */     }
/*     */     
/*     */     protected void collect(MultiValueMap<String, Object> attributes, String key, List<String> destination)
/*     */     {
/* 329 */       List<?> values = (List)attributes.get(key);
/* 330 */       if (values != null) {
/* 331 */         for (Object value : values) {
/* 332 */           if ((value instanceof String[])) {
/* 333 */             Collections.addAll(destination, (String[])value);
/*     */           }
/*     */           else {
/* 336 */             destination.add((String)value);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private void addDeducedBeanType(ConditionContext context, AnnotatedTypeMetadata metadata, List<String> beanTypes)
/*     */     {
/* 344 */       if (((metadata instanceof MethodMetadata)) && 
/* 345 */         (metadata.isAnnotated(Bean.class.getName()))) {
/* 346 */         addDeducedBeanTypeForBeanMethod(context, (MethodMetadata)metadata, beanTypes);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private void addDeducedBeanTypeForBeanMethod(ConditionContext context, MethodMetadata metadata, List<String> beanTypes)
/*     */     {
/*     */       try
/*     */       {
/* 356 */         Class<?> returnType = ClassUtils.forName(metadata.getReturnTypeName(), context
/* 357 */           .getClassLoader());
/* 358 */         beanTypes.add(returnType.getName());
/*     */       }
/*     */       catch (Throwable ex)
/*     */       {
/* 362 */         throw new OnBeanCondition.BeanTypeDeductionException(metadata.getDeclaringClassName(), metadata.getMethodName(), ex, null);
/*     */       }
/*     */     }
/*     */     
/*     */     public SearchStrategy getStrategy() {
/* 367 */       return this.strategy != null ? this.strategy : SearchStrategy.ALL;
/*     */     }
/*     */     
/*     */     public List<String> getNames() {
/* 371 */       return this.names;
/*     */     }
/*     */     
/*     */     public List<String> getTypes() {
/* 375 */       return this.types;
/*     */     }
/*     */     
/*     */     public List<String> getAnnotations() {
/* 379 */       return this.annotations;
/*     */     }
/*     */     
/*     */     public List<String> getIgnoredTypes() {
/* 383 */       return this.ignoredTypes;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 388 */       StringBuilder string = new StringBuilder();
/* 389 */       string.append("(");
/* 390 */       if (!this.names.isEmpty()) {
/* 391 */         string.append("names: ");
/* 392 */         string.append(StringUtils.collectionToCommaDelimitedString(this.names));
/* 393 */         if (!this.types.isEmpty()) {
/* 394 */           string.append("; ");
/*     */         }
/*     */       }
/* 397 */       if (!this.types.isEmpty()) {
/* 398 */         string.append("types: ");
/* 399 */         string.append(StringUtils.collectionToCommaDelimitedString(this.types));
/*     */       }
/* 401 */       string.append("; SearchStrategy: ");
/* 402 */       string.append(this.strategy.toString().toLowerCase());
/* 403 */       string.append(")");
/* 404 */       return string.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SingleCandidateBeanSearchSpec
/*     */     extends OnBeanCondition.BeanSearchSpec
/*     */   {
/*     */     SingleCandidateBeanSearchSpec(ConditionContext context, AnnotatedTypeMetadata metadata, Class<?> annotationType)
/*     */     {
/* 413 */       super(metadata, annotationType);
/*     */     }
/*     */     
/*     */ 
/*     */     protected void collect(MultiValueMap<String, Object> attributes, String key, List<String> destination)
/*     */     {
/* 419 */       super.collect(attributes, key, destination);
/* 420 */       destination.removeAll(Arrays.asList(new String[] { "", Object.class.getName() }));
/*     */     }
/*     */     
/*     */     protected void validate(OnBeanCondition.BeanTypeDeductionException ex)
/*     */     {
/* 425 */       Assert.isTrue(getTypes().size() == 1, annotationName() + " annotations must specify only one type (got " + 
/* 426 */         getTypes() + ")");
/*     */     }
/*     */   }
/*     */   
/*     */   static final class BeanTypeDeductionException
/*     */     extends RuntimeException
/*     */   {
/*     */     private BeanTypeDeductionException(String className, String beanMethodName, Throwable cause)
/*     */     {
/* 435 */       super(cause);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\condition\OnBeanCondition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */