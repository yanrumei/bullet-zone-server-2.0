/*     */ package org.hibernate.validator.internal.metadata.provider;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.validation.GroupSequence;
/*     */ import javax.validation.ParameterNameProvider;
/*     */ import javax.validation.Valid;
/*     */ import javax.validation.groups.ConvertGroup;
/*     */ import javax.validation.groups.ConvertGroup.List;
/*     */ import org.hibernate.validator.group.GroupSequenceProvider;
/*     */ import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
/*     */ import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptions;
/*     */ import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptionsImpl;
/*     */ import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
/*     */ import org.hibernate.validator.internal.metadata.core.MetaConstraint;
/*     */ import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
/*     */ import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl.ConstraintType;
/*     */ import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
/*     */ import org.hibernate.validator.internal.metadata.raw.BeanConfiguration;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConfigurationSource;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedElement;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedExecutable;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedField;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedParameter;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedType;
/*     */ import org.hibernate.validator.internal.metadata.raw.ExecutableElement;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper.Partitioner;
/*     */ import org.hibernate.validator.internal.util.ConcurrentReferenceHashMap;
/*     */ import org.hibernate.validator.internal.util.ConcurrentReferenceHashMap.ReferenceType;
/*     */ import org.hibernate.validator.internal.util.ReflectionHelper;
/*     */ import org.hibernate.validator.internal.util.classhierarchy.ClassHierarchyHelper;
/*     */ import org.hibernate.validator.internal.util.classhierarchy.Filter;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredConstructors;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredFields;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredMethods;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetMethods;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.NewInstance;
/*     */ import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;
/*     */ import org.hibernate.validator.valuehandling.UnwrapValidatedValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotationMetaDataProvider
/*     */   implements MetaDataProvider
/*     */ {
/*  74 */   private static final Log log = ;
/*     */   
/*     */   static final int DEFAULT_INITIAL_CAPACITY = 16;
/*     */   
/*     */   protected final ConstraintHelper constraintHelper;
/*     */   
/*     */   protected final ConcurrentReferenceHashMap<Class<?>, BeanConfiguration<?>> configuredBeans;
/*     */   
/*     */   protected final AnnotationProcessingOptions annotationProcessingOptions;
/*     */   
/*     */   protected final ParameterNameProvider parameterNameProvider;
/*     */   
/*     */   public AnnotationMetaDataProvider(ConstraintHelper constraintHelper, ParameterNameProvider parameterNameProvider, AnnotationProcessingOptions annotationProcessingOptions)
/*     */   {
/*  88 */     this.constraintHelper = constraintHelper;
/*  89 */     this.parameterNameProvider = parameterNameProvider;
/*  90 */     this.annotationProcessingOptions = annotationProcessingOptions;
/*  91 */     this.configuredBeans = new ConcurrentReferenceHashMap(16, ConcurrentReferenceHashMap.ReferenceType.SOFT, ConcurrentReferenceHashMap.ReferenceType.SOFT);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotationProcessingOptions getAnnotationProcessingOptions()
/*     */   {
/* 100 */     return new AnnotationProcessingOptionsImpl();
/*     */   }
/*     */   
/*     */   public <T> List<BeanConfiguration<? super T>> getBeanConfigurationForHierarchy(Class<T> beanClass)
/*     */   {
/* 105 */     List<BeanConfiguration<? super T>> configurations = CollectionHelper.newArrayList();
/*     */     
/* 107 */     for (Class<? super T> hierarchyClass : ClassHierarchyHelper.getHierarchy(beanClass, new Filter[0])) {
/* 108 */       BeanConfiguration<? super T> configuration = getBeanConfiguration(hierarchyClass);
/* 109 */       if (configuration != null) {
/* 110 */         configurations.add(configuration);
/*     */       }
/*     */     }
/*     */     
/* 114 */     return configurations;
/*     */   }
/*     */   
/*     */   private <T> BeanConfiguration<T> getBeanConfiguration(Class<T> beanClass)
/*     */   {
/* 119 */     BeanConfiguration<T> configuration = (BeanConfiguration)this.configuredBeans.get(beanClass);
/*     */     
/* 121 */     if (configuration != null) {
/* 122 */       return configuration;
/*     */     }
/*     */     
/* 125 */     configuration = retrieveBeanConfiguration(beanClass);
/* 126 */     this.configuredBeans.put(beanClass, configuration);
/*     */     
/* 128 */     return configuration;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private <T> BeanConfiguration<T> retrieveBeanConfiguration(Class<T> beanClass)
/*     */   {
/* 137 */     Set<ConstrainedElement> constrainedElements = getFieldMetaData(beanClass);
/* 138 */     constrainedElements.addAll(getMethodMetaData(beanClass));
/* 139 */     constrainedElements.addAll(getConstructorMetaData(beanClass));
/*     */     
/*     */ 
/*     */ 
/* 143 */     Set<MetaConstraint<?>> classLevelConstraints = getClassLevelConstraints(beanClass);
/* 144 */     if (!classLevelConstraints.isEmpty())
/*     */     {
/*     */ 
/*     */ 
/* 148 */       ConstrainedType classLevelMetaData = new ConstrainedType(ConfigurationSource.ANNOTATION, ConstraintLocation.forClass(beanClass), classLevelConstraints);
/*     */       
/*     */ 
/* 151 */       constrainedElements.add(classLevelMetaData);
/*     */     }
/*     */     
/* 154 */     return new BeanConfiguration(ConfigurationSource.ANNOTATION, beanClass, constrainedElements, 
/*     */     
/*     */ 
/*     */ 
/* 158 */       getDefaultGroupSequence(beanClass), 
/* 159 */       getDefaultGroupSequenceProvider(beanClass));
/*     */   }
/*     */   
/*     */   private List<Class<?>> getDefaultGroupSequence(Class<?> beanClass)
/*     */   {
/* 164 */     GroupSequence groupSequenceAnnotation = (GroupSequence)beanClass.getAnnotation(GroupSequence.class);
/* 165 */     return groupSequenceAnnotation != null ? Arrays.asList(groupSequenceAnnotation.value()) : null;
/*     */   }
/*     */   
/*     */   private <T> DefaultGroupSequenceProvider<? super T> getDefaultGroupSequenceProvider(Class<T> beanClass) {
/* 169 */     GroupSequenceProvider groupSequenceProviderAnnotation = (GroupSequenceProvider)beanClass.getAnnotation(GroupSequenceProvider.class);
/*     */     
/* 171 */     if (groupSequenceProviderAnnotation != null)
/*     */     {
/*     */ 
/* 174 */       Class<? extends DefaultGroupSequenceProvider<? super T>> providerClass = groupSequenceProviderAnnotation.value();
/* 175 */       return newGroupSequenceProviderClassInstance(beanClass, providerClass);
/*     */     }
/*     */     
/* 178 */     return null;
/*     */   }
/*     */   
/*     */   private <T> DefaultGroupSequenceProvider<? super T> newGroupSequenceProviderClassInstance(Class<T> beanClass, Class<? extends DefaultGroupSequenceProvider<? super T>> providerClass)
/*     */   {
/* 183 */     Method[] providerMethods = (Method[])run(GetMethods.action(providerClass));
/* 184 */     for (Method method : providerMethods) {
/* 185 */       Class<?>[] paramTypes = method.getParameterTypes();
/* 186 */       if (("getValidationGroups".equals(method.getName())) && (!method.isBridge()) && (paramTypes.length == 1) && 
/* 187 */         (paramTypes[0].isAssignableFrom(beanClass)))
/*     */       {
/* 189 */         return (DefaultGroupSequenceProvider)run(
/* 190 */           NewInstance.action(providerClass, "the default group sequence provider"));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 195 */     throw log.getWrongDefaultGroupSequenceProviderTypeException(beanClass.getName());
/*     */   }
/*     */   
/*     */   private Set<MetaConstraint<?>> getClassLevelConstraints(Class<?> clazz) {
/* 199 */     if (this.annotationProcessingOptions.areClassLevelConstraintsIgnoredFor(clazz)) {
/* 200 */       return Collections.emptySet();
/*     */     }
/*     */     
/* 203 */     Set<MetaConstraint<?>> classLevelConstraints = CollectionHelper.newHashSet();
/*     */     
/*     */ 
/* 206 */     List<ConstraintDescriptorImpl<?>> classMetaData = findClassLevelConstraints(clazz);
/*     */     
/* 208 */     for (ConstraintDescriptorImpl<?> constraintDescription : classMetaData) {
/* 209 */       classLevelConstraints.add(createMetaConstraint(clazz, constraintDescription));
/*     */     }
/*     */     
/* 212 */     return classLevelConstraints;
/*     */   }
/*     */   
/*     */   private Set<ConstrainedElement> getFieldMetaData(Class<?> beanClass) {
/* 216 */     Set<ConstrainedElement> propertyMetaData = CollectionHelper.newHashSet();
/*     */     
/* 218 */     for (Field field : (Field[])run(GetDeclaredFields.action(beanClass)))
/*     */     {
/* 220 */       if ((!Modifier.isStatic(field.getModifiers())) && 
/* 221 */         (!this.annotationProcessingOptions.areMemberConstraintsIgnoredFor(field)) && 
/* 222 */         (!field.isSynthetic()))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 227 */         propertyMetaData.add(findPropertyMetaData(field)); }
/*     */     }
/* 229 */     return propertyMetaData;
/*     */   }
/*     */   
/*     */   private ConstrainedField findPropertyMetaData(Field field) {
/* 233 */     Set<MetaConstraint<?>> constraints = convertToMetaConstraints(
/* 234 */       findConstraints(field, ElementType.FIELD), field);
/*     */     
/*     */ 
/*     */ 
/* 238 */     Map<Class<?>, Class<?>> groupConversions = getGroupConversions(
/* 239 */       (ConvertGroup)field.getAnnotation(ConvertGroup.class), 
/* 240 */       (ConvertGroup.List)field.getAnnotation(ConvertGroup.List.class));
/*     */     
/*     */ 
/* 243 */     boolean isCascading = field.isAnnotationPresent(Valid.class);
/* 244 */     Set<MetaConstraint<?>> typeArgumentsConstraints = findTypeAnnotationConstraintsForMember(field);
/*     */     
/* 246 */     boolean typeArgumentAnnotated = !typeArgumentsConstraints.isEmpty();
/* 247 */     UnwrapMode unwrapMode = unwrapMode(field, typeArgumentAnnotated);
/*     */     
/* 249 */     return new ConstrainedField(ConfigurationSource.ANNOTATION, 
/*     */     
/* 251 */       ConstraintLocation.forProperty(field), constraints, typeArgumentsConstraints, groupConversions, isCascading, unwrapMode);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private UnwrapMode unwrapMode(Field field, boolean typeArgumentAnnotated)
/*     */   {
/* 261 */     boolean isCollection = ReflectionHelper.isCollection(ReflectionHelper.typeOf(field));
/* 262 */     UnwrapValidatedValue unwrapValidatedValue = (UnwrapValidatedValue)field.getAnnotation(UnwrapValidatedValue.class);
/* 263 */     return unwrapMode(typeArgumentAnnotated, isCollection, unwrapValidatedValue);
/*     */   }
/*     */   
/*     */   private UnwrapMode unwrapMode(ExecutableElement executable, boolean typeArgumentAnnotated) {
/* 267 */     boolean isCollection = ReflectionHelper.isCollection(ReflectionHelper.typeOf(executable.getMember()));
/* 268 */     UnwrapValidatedValue unwrapValidatedValue = (UnwrapValidatedValue)executable.getAccessibleObject().getAnnotation(UnwrapValidatedValue.class);
/* 269 */     return unwrapMode(typeArgumentAnnotated, isCollection, unwrapValidatedValue);
/*     */   }
/*     */   
/*     */   private Set<MetaConstraint<?>> convertToMetaConstraints(List<ConstraintDescriptorImpl<?>> constraintDescriptors, Field field) {
/* 273 */     Set<MetaConstraint<?>> constraints = CollectionHelper.newHashSet();
/*     */     
/* 275 */     for (ConstraintDescriptorImpl<?> constraintDescription : constraintDescriptors) {
/* 276 */       constraints.add(createMetaConstraint(field, constraintDescription));
/*     */     }
/* 278 */     return constraints;
/*     */   }
/*     */   
/*     */   private UnwrapMode unwrapMode(boolean typeArgumentAnnotated, boolean isCollection, UnwrapValidatedValue unwrapValidatedValue) {
/* 282 */     if ((unwrapValidatedValue == null) && (typeArgumentAnnotated) && (!isCollection))
/*     */     {
/*     */ 
/*     */ 
/* 286 */       return UnwrapMode.UNWRAP;
/*     */     }
/* 288 */     if (unwrapValidatedValue != null)
/*     */     {
/*     */ 
/*     */ 
/* 292 */       return unwrapValidatedValue.value() ? UnwrapMode.UNWRAP : UnwrapMode.SKIP_UNWRAP;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 302 */     return UnwrapMode.AUTOMATIC;
/*     */   }
/*     */   
/*     */   private Set<ConstrainedExecutable> getConstructorMetaData(Class<?> clazz) {
/* 306 */     List<ExecutableElement> declaredConstructors = ExecutableElement.forConstructors(
/* 307 */       (Constructor[])run(GetDeclaredConstructors.action(clazz)));
/*     */     
/*     */ 
/* 310 */     return getMetaData(declaredConstructors);
/*     */   }
/*     */   
/*     */   private Set<ConstrainedExecutable> getMethodMetaData(Class<?> clazz) {
/* 314 */     List<ExecutableElement> declaredMethods = ExecutableElement.forMethods(
/* 315 */       (Method[])run(GetDeclaredMethods.action(clazz)));
/*     */     
/*     */ 
/* 318 */     return getMetaData(declaredMethods);
/*     */   }
/*     */   
/*     */   private Set<ConstrainedExecutable> getMetaData(List<ExecutableElement> executableElements) {
/* 322 */     Set<ConstrainedExecutable> executableMetaData = CollectionHelper.newHashSet();
/*     */     
/* 324 */     for (ExecutableElement executable : executableElements)
/*     */     {
/*     */ 
/* 327 */       Member member = executable.getMember();
/* 328 */       if ((!Modifier.isStatic(member.getModifiers())) && (!member.isSynthetic()))
/*     */       {
/*     */ 
/*     */ 
/* 332 */         executableMetaData.add(findExecutableMetaData(executable));
/*     */       }
/*     */     }
/* 335 */     return executableMetaData;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ConstrainedExecutable findExecutableMetaData(ExecutableElement executable)
/*     */   {
/* 347 */     List<ConstrainedParameter> parameterConstraints = getParameterMetaData(executable);
/*     */     
/* 349 */     AccessibleObject member = executable.getAccessibleObject();
/*     */     
/* 351 */     Map<ConstraintDescriptorImpl.ConstraintType, List<ConstraintDescriptorImpl<?>>> executableConstraints = CollectionHelper.partition(
/* 352 */       findConstraints(executable
/* 353 */       .getMember(), executable
/* 354 */       .getElementType()), 
/* 355 */       byType());
/*     */     
/*     */     Set<MetaConstraint<?>> crossParameterConstraints;
/*     */     Set<MetaConstraint<?>> crossParameterConstraints;
/* 359 */     if (this.annotationProcessingOptions.areCrossParameterConstraintsIgnoredFor(executable.getMember())) {
/* 360 */       crossParameterConstraints = Collections.emptySet();
/*     */     }
/*     */     else {
/* 363 */       crossParameterConstraints = convertToMetaConstraints(
/* 364 */         (List)executableConstraints.get(ConstraintDescriptorImpl.ConstraintType.CROSS_PARAMETER), executable);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 374 */     UnwrapMode unwrapMode = UnwrapMode.AUTOMATIC;
/* 375 */     boolean isCascading; Set<MetaConstraint<?>> typeArgumentsConstraints; Set<MetaConstraint<?>> returnValueConstraints; Map<Class<?>, Class<?>> groupConversions; boolean isCascading; if (this.annotationProcessingOptions.areReturnValueConstraintsIgnoredFor(executable.getMember())) {
/* 376 */       Set<MetaConstraint<?>> returnValueConstraints = Collections.emptySet();
/* 377 */       Set<MetaConstraint<?>> typeArgumentsConstraints = Collections.emptySet();
/* 378 */       Map<Class<?>, Class<?>> groupConversions = Collections.emptyMap();
/* 379 */       isCascading = false;
/*     */     }
/*     */     else {
/* 382 */       typeArgumentsConstraints = findTypeAnnotationConstraintsForMember(executable.getMember());
/* 383 */       boolean typeArgumentAnnotated = !typeArgumentsConstraints.isEmpty();
/* 384 */       unwrapMode = unwrapMode(executable, typeArgumentAnnotated);
/* 385 */       returnValueConstraints = convertToMetaConstraints(
/* 386 */         (List)executableConstraints.get(ConstraintDescriptorImpl.ConstraintType.GENERIC), executable);
/*     */       
/*     */ 
/* 389 */       groupConversions = getGroupConversions(
/* 390 */         (ConvertGroup)member.getAnnotation(ConvertGroup.class), 
/* 391 */         (ConvertGroup.List)member.getAnnotation(ConvertGroup.List.class));
/*     */       
/* 393 */       isCascading = executable.getAccessibleObject().isAnnotationPresent(Valid.class);
/*     */     }
/*     */     
/* 396 */     return new ConstrainedExecutable(ConfigurationSource.ANNOTATION, 
/*     */     
/* 398 */       ConstraintLocation.forReturnValue(executable), parameterConstraints, crossParameterConstraints, returnValueConstraints, typeArgumentsConstraints, groupConversions, isCascading, unwrapMode);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Set<MetaConstraint<?>> convertToMetaConstraints(List<ConstraintDescriptorImpl<?>> constraintsDescriptors, ExecutableElement executable)
/*     */   {
/* 410 */     if (constraintsDescriptors == null) {
/* 411 */       return Collections.emptySet();
/*     */     }
/*     */     
/* 414 */     Set<MetaConstraint<?>> constraints = CollectionHelper.newHashSet();
/*     */     
/* 416 */     for (ConstraintDescriptorImpl<?> constraintDescriptor : constraintsDescriptors) {
/* 417 */       constraints.add(constraintDescriptor
/* 418 */         .getConstraintType() == ConstraintDescriptorImpl.ConstraintType.GENERIC ? 
/* 419 */         createReturnValueMetaConstraint(executable, constraintDescriptor) : 
/* 420 */         createCrossParameterMetaConstraint(executable, constraintDescriptor));
/*     */     }
/*     */     
/*     */ 
/* 424 */     return constraints;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private List<ConstrainedParameter> getParameterMetaData(ExecutableElement executable)
/*     */   {
/* 436 */     List<ConstrainedParameter> metaData = CollectionHelper.newArrayList();
/*     */     
/* 438 */     List<String> parameterNames = executable.getParameterNames(this.parameterNameProvider);
/* 439 */     int i = 0;
/* 440 */     for (Annotation[] parameterAnnotations : executable.getParameterAnnotations()) {
/* 441 */       boolean parameterIsCascading = false;
/* 442 */       String parameterName = (String)parameterNames.get(i);
/* 443 */       Set<MetaConstraint<?>> parameterConstraints = CollectionHelper.newHashSet();
/* 444 */       Set<MetaConstraint<?>> typeArgumentsConstraints = CollectionHelper.newHashSet();
/* 445 */       ConvertGroup groupConversion = null;
/* 446 */       ConvertGroup.List groupConversionList = null;
/*     */       
/* 448 */       if (this.annotationProcessingOptions.areParameterConstraintsIgnoredFor(executable.getMember(), i)) {
/* 449 */         metaData.add(new ConstrainedParameter(ConfigurationSource.ANNOTATION, 
/*     */         
/*     */ 
/* 452 */           ConstraintLocation.forParameter(executable, i), 
/* 453 */           ReflectionHelper.typeOf(executable, i), i, parameterName, parameterConstraints, typeArgumentsConstraints, 
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 458 */           getGroupConversions(groupConversion, groupConversionList), false, UnwrapMode.AUTOMATIC));
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 463 */         i++;
/*     */       }
/*     */       else
/*     */       {
/* 467 */         UnwrapValidatedValue unwrapValidatedValue = null;
/* 468 */         for (Annotation parameterAnnotation : parameterAnnotations)
/*     */         {
/* 470 */           if (parameterAnnotation.annotationType().equals(Valid.class)) {
/* 471 */             parameterIsCascading = true;
/*     */ 
/*     */ 
/*     */           }
/* 475 */           else if (parameterAnnotation.annotationType().equals(ConvertGroup.class)) {
/* 476 */             groupConversion = (ConvertGroup)parameterAnnotation;
/*     */           }
/* 478 */           else if (parameterAnnotation.annotationType().equals(ConvertGroup.List.class)) {
/* 479 */             groupConversionList = (ConvertGroup.List)parameterAnnotation;
/*     */ 
/*     */ 
/*     */           }
/* 483 */           else if (parameterAnnotation.annotationType().equals(UnwrapValidatedValue.class)) {
/* 484 */             unwrapValidatedValue = (UnwrapValidatedValue)parameterAnnotation;
/*     */           }
/*     */           
/*     */ 
/* 488 */           List<ConstraintDescriptorImpl<?>> constraints = findConstraintAnnotations(executable
/* 489 */             .getMember(), parameterAnnotation, ElementType.PARAMETER);
/*     */           
/* 491 */           for (ConstraintDescriptorImpl<?> constraintDescriptorImpl : constraints) {
/* 492 */             parameterConstraints.add(
/* 493 */               createParameterMetaConstraint(executable, i, constraintDescriptorImpl));
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 498 */         typeArgumentsConstraints = findTypeAnnotationConstraintsForExecutableParameter(executable.getMember(), i);
/* 499 */         boolean typeArgumentAnnotated = !typeArgumentsConstraints.isEmpty();
/* 500 */         boolean isCollection = ReflectionHelper.isCollection(ReflectionHelper.typeOf(executable, i));
/* 501 */         UnwrapMode unwrapMode = unwrapMode(typeArgumentAnnotated, isCollection, unwrapValidatedValue);
/*     */         
/* 503 */         metaData.add(new ConstrainedParameter(ConfigurationSource.ANNOTATION, 
/*     */         
/*     */ 
/* 506 */           ConstraintLocation.forParameter(executable, i), 
/* 507 */           ReflectionHelper.typeOf(executable, i), i, parameterName, parameterConstraints, typeArgumentsConstraints, 
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 512 */           getGroupConversions(groupConversion, groupConversionList), parameterIsCascading, unwrapMode));
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 517 */         i++;
/*     */       }
/*     */     }
/* 520 */     return metaData;
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
/*     */   private List<ConstraintDescriptorImpl<?>> findConstraints(Member member, ElementType type)
/*     */   {
/* 533 */     List<ConstraintDescriptorImpl<?>> metaData = CollectionHelper.newArrayList();
/* 534 */     for (Annotation annotation : ((AccessibleObject)member).getDeclaredAnnotations()) {
/* 535 */       metaData.addAll(findConstraintAnnotations(member, annotation, type));
/*     */     }
/*     */     
/* 538 */     return metaData;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private List<ConstraintDescriptorImpl<?>> findClassLevelConstraints(Class<?> beanClass)
/*     */   {
/* 550 */     List<ConstraintDescriptorImpl<?>> metaData = CollectionHelper.newArrayList();
/* 551 */     for (Annotation annotation : beanClass.getDeclaredAnnotations()) {
/* 552 */       metaData.addAll(findConstraintAnnotations(null, annotation, ElementType.TYPE));
/*     */     }
/* 554 */     return metaData;
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
/*     */   protected <A extends Annotation> List<ConstraintDescriptorImpl<?>> findConstraintAnnotations(Member member, A annotation, ElementType type)
/*     */   {
/* 574 */     if (isJdkInternalType(annotation)) {
/* 575 */       return Collections.emptyList();
/*     */     }
/*     */     
/* 578 */     List<ConstraintDescriptorImpl<?>> constraintDescriptors = CollectionHelper.newArrayList();
/*     */     
/* 580 */     List<Annotation> constraints = CollectionHelper.newArrayList();
/* 581 */     Class<? extends Annotation> annotationType = annotation.annotationType();
/* 582 */     if (this.constraintHelper.isConstraintAnnotation(annotationType)) {
/* 583 */       constraints.add(annotation);
/*     */     }
/* 585 */     else if (this.constraintHelper.isMultiValueConstraint(annotationType)) {
/* 586 */       constraints.addAll(this.constraintHelper.getConstraintsFromMultiValueConstraint(annotation));
/*     */     }
/*     */     
/* 589 */     for (Annotation constraint : constraints) {
/* 590 */       ConstraintDescriptorImpl<?> constraintDescriptor = buildConstraintDescriptor(member, constraint, type);
/*     */       
/*     */ 
/* 593 */       constraintDescriptors.add(constraintDescriptor);
/*     */     }
/* 595 */     return constraintDescriptors;
/*     */   }
/*     */   
/*     */   private <A extends Annotation> boolean isJdkInternalType(A annotation) {
/* 599 */     Package pakkage = annotation.annotationType().getPackage();
/* 600 */     return (pakkage != null) && ("jdk.internal".equals(pakkage.getName()));
/*     */   }
/*     */   
/*     */   private Map<Class<?>, Class<?>> getGroupConversions(ConvertGroup groupConversion, ConvertGroup.List groupConversionList) {
/* 604 */     Map<Class<?>, Class<?>> groupConversions = CollectionHelper.newHashMap();
/*     */     
/* 606 */     if (groupConversion != null) {
/* 607 */       groupConversions.put(groupConversion.from(), groupConversion.to());
/*     */     }
/*     */     
/* 610 */     if (groupConversionList != null) {
/* 611 */       for (ConvertGroup conversion : groupConversionList.value()) {
/* 612 */         if (groupConversions.containsKey(conversion.from())) {
/* 613 */           throw log.getMultipleGroupConversionsForSameSourceException(conversion
/* 614 */             .from(), 
/* 615 */             CollectionHelper.asSet(new Class[] {
/* 616 */             (Class)groupConversions.get(conversion.from()), conversion
/* 617 */             .to() }));
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 622 */         groupConversions.put(conversion.from(), conversion.to());
/*     */       }
/*     */     }
/*     */     
/* 626 */     return groupConversions;
/*     */   }
/*     */   
/*     */   private CollectionHelper.Partitioner<ConstraintDescriptorImpl.ConstraintType, ConstraintDescriptorImpl<?>> byType() {
/* 630 */     new CollectionHelper.Partitioner()
/*     */     {
/*     */       public ConstraintDescriptorImpl.ConstraintType getPartition(ConstraintDescriptorImpl<?> v)
/*     */       {
/* 634 */         return v.getConstraintType();
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   private <A extends Annotation> MetaConstraint<?> createMetaConstraint(Class<?> declaringClass, ConstraintDescriptorImpl<A> descriptor)
/*     */   {
/* 641 */     return new MetaConstraint(descriptor, ConstraintLocation.forClass(declaringClass));
/*     */   }
/*     */   
/*     */   private <A extends Annotation> MetaConstraint<?> createMetaConstraint(Member member, ConstraintDescriptorImpl<A> descriptor) {
/* 645 */     return new MetaConstraint(descriptor, ConstraintLocation.forProperty(member));
/*     */   }
/*     */   
/*     */   private <A extends Annotation> MetaConstraint<A> createParameterMetaConstraint(ExecutableElement member, int parameterIndex, ConstraintDescriptorImpl<A> descriptor)
/*     */   {
/* 650 */     return new MetaConstraint(descriptor, 
/*     */     
/* 652 */       ConstraintLocation.forParameter(member, parameterIndex));
/*     */   }
/*     */   
/*     */ 
/*     */   private <A extends Annotation> MetaConstraint<A> createReturnValueMetaConstraint(ExecutableElement member, ConstraintDescriptorImpl<A> descriptor)
/*     */   {
/* 658 */     return new MetaConstraint(descriptor, ConstraintLocation.forReturnValue(member));
/*     */   }
/*     */   
/*     */   private <A extends Annotation> MetaConstraint<A> createCrossParameterMetaConstraint(ExecutableElement member, ConstraintDescriptorImpl<A> descriptor)
/*     */   {
/* 663 */     return new MetaConstraint(descriptor, ConstraintLocation.forCrossParameter(member));
/*     */   }
/*     */   
/*     */ 
/*     */   private <A extends Annotation> ConstraintDescriptorImpl<A> buildConstraintDescriptor(Member member, A annotation, ElementType type)
/*     */   {
/* 669 */     return new ConstraintDescriptorImpl(this.constraintHelper, member, annotation, type);
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
/*     */   private <T> T run(PrivilegedAction<T> action)
/*     */   {
/* 684 */     return (T)(System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Set<MetaConstraint<?>> findTypeAnnotationConstraintsForMember(Member member)
/*     */   {
/* 696 */     return Collections.emptySet();
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
/*     */   protected Set<MetaConstraint<?>> findTypeAnnotationConstraintsForExecutableParameter(Member member, int i)
/*     */   {
/* 709 */     return Collections.emptySet();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\provider\AnnotationMetaDataProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */