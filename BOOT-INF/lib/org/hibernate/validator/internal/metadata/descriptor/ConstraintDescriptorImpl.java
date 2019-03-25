/*     */ package org.hibernate.validator.internal.metadata.descriptor;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.Documented;
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.annotation.Retention;
/*     */ import java.lang.annotation.Target;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.validation.Constraint;
/*     */ import javax.validation.ConstraintTarget;
/*     */ import javax.validation.ConstraintValidator;
/*     */ import javax.validation.OverridesAttribute;
/*     */ import javax.validation.OverridesAttribute.List;
/*     */ import javax.validation.Payload;
/*     */ import javax.validation.ReportAsSingleViolation;
/*     */ import javax.validation.ValidationException;
/*     */ import javax.validation.constraintvalidation.SupportedValidationTarget;
/*     */ import javax.validation.constraintvalidation.ValidationTarget;
/*     */ import javax.validation.groups.Default;
/*     */ import javax.validation.metadata.ConstraintDescriptor;
/*     */ import org.hibernate.validator.constraints.CompositionType;
/*     */ import org.hibernate.validator.constraints.ConstraintComposition;
/*     */ import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
/*     */ import org.hibernate.validator.internal.metadata.core.ConstraintOrigin;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.TypeHelper;
/*     */ import org.hibernate.validator.internal.util.annotationfactory.AnnotationDescriptor;
/*     */ import org.hibernate.validator.internal.util.annotationfactory.AnnotationFactory;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetAnnotationParameter;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredMethods;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetMethod;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConstraintDescriptorImpl<T extends Annotation>
/*     */   implements ConstraintDescriptor<T>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -2563102960314069246L;
/*  67 */   private static final Log log = ;
/*     */   
/*     */ 
/*     */   private static final int OVERRIDES_PARAMETER_DEFAULT_INDEX = -1;
/*     */   
/*     */ 
/*  73 */   private static final List<String> NON_COMPOSING_CONSTRAINT_ANNOTATIONS = Arrays.asList(new String[] {Documented.class
/*  74 */     .getName(), Retention.class
/*  75 */     .getName(), Target.class
/*  76 */     .getName(), Constraint.class
/*  77 */     .getName(), ReportAsSingleViolation.class
/*  78 */     .getName() });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final T annotation;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Class<T> annotationType;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final List<Class<? extends ConstraintValidator<T, ?>>> constraintValidatorClasses;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final List<Class<? extends ConstraintValidator<T, ?>>> matchingConstraintValidatorClasses;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Set<Class<?>> groups;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Map<String, Object> attributes;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Set<Class<? extends Payload>> payloads;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Set<ConstraintDescriptorImpl<?>> composingConstraints;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final boolean isReportAsSingleInvalidConstraint;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ElementType elementType;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ConstraintOrigin definedOn;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ConstraintType constraintType;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final CompositionType compositionType;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int hashCode;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConstraintDescriptorImpl(ConstraintHelper constraintHelper, Member member, T annotation, ElementType type, Class<?> implicitGroup, ConstraintOrigin definedOn, ConstraintType externalConstraintType)
/*     */   {
/* 157 */     this.annotation = annotation;
/* 158 */     this.annotationType = this.annotation.annotationType();
/* 159 */     this.elementType = type;
/* 160 */     this.definedOn = definedOn;
/* 161 */     this.isReportAsSingleInvalidConstraint = this.annotationType.isAnnotationPresent(ReportAsSingleViolation.class);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 167 */     this.attributes = buildAnnotationParameterMap(annotation);
/* 168 */     this.groups = buildGroupSet(implicitGroup);
/* 169 */     this.payloads = buildPayloadSet(annotation);
/*     */     
/* 171 */     this.constraintValidatorClasses = constraintHelper.getAllValidatorClasses(this.annotationType);
/* 172 */     List<Class<? extends ConstraintValidator<T, ?>>> crossParameterValidatorClasses = constraintHelper.findValidatorClasses(this.annotationType, ValidationTarget.PARAMETERS);
/*     */     
/*     */ 
/*     */ 
/* 176 */     List<Class<? extends ConstraintValidator<T, ?>>> genericValidatorClasses = constraintHelper.findValidatorClasses(this.annotationType, ValidationTarget.ANNOTATED_ELEMENT);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 181 */     if (crossParameterValidatorClasses.size() > 1) {
/* 182 */       throw log.getMultipleCrossParameterValidatorClassesException(this.annotationType.getName());
/*     */     }
/*     */     
/* 185 */     this.constraintType = determineConstraintType(annotation
/* 186 */       .annotationType(), member, type, 
/*     */       
/*     */ 
/* 189 */       !genericValidatorClasses.isEmpty(), 
/* 190 */       !crossParameterValidatorClasses.isEmpty(), externalConstraintType);
/*     */     
/*     */ 
/* 193 */     this.composingConstraints = parseComposingConstraints(member, constraintHelper, this.constraintType);
/* 194 */     this.compositionType = parseCompositionType(constraintHelper);
/* 195 */     validateComposingConstraintTypes();
/*     */     
/* 197 */     if (this.constraintType == ConstraintType.GENERIC) {
/* 198 */       this.matchingConstraintValidatorClasses = Collections.unmodifiableList(genericValidatorClasses);
/*     */     }
/*     */     else {
/* 201 */       this.matchingConstraintValidatorClasses = Collections.unmodifiableList(crossParameterValidatorClasses);
/*     */     }
/*     */     
/* 204 */     this.hashCode = annotation.hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ConstraintDescriptorImpl(ConstraintHelper constraintHelper, Member member, T annotation, ElementType type)
/*     */   {
/* 211 */     this(constraintHelper, member, annotation, type, null, ConstraintOrigin.DEFINED_LOCALLY, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ConstraintDescriptorImpl(ConstraintHelper constraintHelper, Member member, T annotation, ElementType type, ConstraintType constraintType)
/*     */   {
/* 218 */     this(constraintHelper, member, annotation, type, null, ConstraintOrigin.DEFINED_LOCALLY, constraintType);
/*     */   }
/*     */   
/*     */   public T getAnnotation()
/*     */   {
/* 223 */     return this.annotation;
/*     */   }
/*     */   
/*     */   public Class<T> getAnnotationType() {
/* 227 */     return this.annotationType;
/*     */   }
/*     */   
/*     */   public String getMessageTemplate()
/*     */   {
/* 232 */     return (String)getAttributes().get("message");
/*     */   }
/*     */   
/*     */   public Set<Class<?>> getGroups()
/*     */   {
/* 237 */     return this.groups;
/*     */   }
/*     */   
/*     */   public Set<Class<? extends Payload>> getPayload()
/*     */   {
/* 242 */     return this.payloads;
/*     */   }
/*     */   
/*     */   public ConstraintTarget getValidationAppliesTo()
/*     */   {
/* 247 */     return (ConstraintTarget)this.attributes.get("validationAppliesTo");
/*     */   }
/*     */   
/*     */   public List<Class<? extends ConstraintValidator<T, ?>>> getConstraintValidatorClasses()
/*     */   {
/* 252 */     return this.constraintValidatorClasses;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<Class<? extends ConstraintValidator<T, ?>>> getMatchingConstraintValidatorClasses()
/*     */   {
/* 262 */     return this.matchingConstraintValidatorClasses;
/*     */   }
/*     */   
/*     */   public Map<Type, Class<? extends ConstraintValidator<T, ?>>> getAvailableValidatorTypes()
/*     */   {
/* 267 */     Map<Type, Class<? extends ConstraintValidator<T, ?>>> availableValidatorTypes = TypeHelper.getValidatorsTypes(
/* 268 */       getAnnotationType(), 
/* 269 */       getMatchingConstraintValidatorClasses());
/*     */     
/* 271 */     return availableValidatorTypes;
/*     */   }
/*     */   
/*     */   public Map<String, Object> getAttributes()
/*     */   {
/* 276 */     return this.attributes;
/*     */   }
/*     */   
/*     */   public Set<ConstraintDescriptor<?>> getComposingConstraints()
/*     */   {
/* 281 */     return Collections.unmodifiableSet(this.composingConstraints);
/*     */   }
/*     */   
/*     */   public Set<ConstraintDescriptorImpl<?>> getComposingConstraintImpls() {
/* 285 */     return this.composingConstraints;
/*     */   }
/*     */   
/*     */   public boolean isReportAsSingleViolation()
/*     */   {
/* 290 */     return this.isReportAsSingleInvalidConstraint;
/*     */   }
/*     */   
/*     */   public ElementType getElementType() {
/* 294 */     return this.elementType;
/*     */   }
/*     */   
/*     */   public ConstraintOrigin getDefinedOn() {
/* 298 */     return this.definedOn;
/*     */   }
/*     */   
/*     */   public ConstraintType getConstraintType() {
/* 302 */     return this.constraintType;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 307 */     if (this == o) {
/* 308 */       return true;
/*     */     }
/* 310 */     if ((o == null) || (getClass() != o.getClass())) {
/* 311 */       return false;
/*     */     }
/*     */     
/* 314 */     ConstraintDescriptorImpl<?> that = (ConstraintDescriptorImpl)o;
/*     */     
/* 316 */     if (this.annotation != null ? !this.annotation.equals(that.annotation) : that.annotation != null) {
/* 317 */       return false;
/*     */     }
/*     */     
/* 320 */     return true;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 325 */     return this.hashCode;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 330 */     StringBuilder sb = new StringBuilder();
/* 331 */     sb.append("ConstraintDescriptorImpl");
/* 332 */     sb.append("{annotation=").append(this.annotationType.getName());
/* 333 */     sb.append(", payloads=").append(this.payloads);
/* 334 */     sb.append(", hasComposingConstraints=").append(this.composingConstraints.isEmpty());
/* 335 */     sb.append(", isReportAsSingleInvalidConstraint=").append(this.isReportAsSingleInvalidConstraint);
/* 336 */     sb.append(", elementType=").append(this.elementType);
/* 337 */     sb.append(", definedOn=").append(this.definedOn);
/* 338 */     sb.append(", groups=").append(this.groups);
/* 339 */     sb.append(", attributes=").append(this.attributes);
/* 340 */     sb.append(", constraintType=").append(this.constraintType);
/* 341 */     sb.append('}');
/* 342 */     return sb.toString();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ConstraintType determineConstraintType(Class<? extends Annotation> constraintAnnotationType, Member member, ElementType elementType, boolean hasGenericValidators, boolean hasCrossParameterValidator, ConstraintType externalConstraintType)
/*     */   {
/* 378 */     ConstraintTarget constraintTarget = (ConstraintTarget)this.attributes.get("validationAppliesTo");
/* 379 */     ConstraintType constraintType = null;
/* 380 */     boolean isExecutable = isExecutable(elementType);
/*     */     
/*     */ 
/* 383 */     if (constraintTarget == ConstraintTarget.RETURN_VALUE) {
/* 384 */       if (!isExecutable) {
/* 385 */         throw log.getParametersOrReturnValueConstraintTargetGivenAtNonExecutableException(this.annotationType
/* 386 */           .getName(), ConstraintTarget.RETURN_VALUE);
/*     */       }
/*     */       
/*     */ 
/* 390 */       constraintType = ConstraintType.GENERIC;
/*     */ 
/*     */     }
/* 393 */     else if (constraintTarget == ConstraintTarget.PARAMETERS) {
/* 394 */       if (!isExecutable) {
/* 395 */         throw log.getParametersOrReturnValueConstraintTargetGivenAtNonExecutableException(this.annotationType
/* 396 */           .getName(), ConstraintTarget.PARAMETERS);
/*     */       }
/*     */       
/*     */ 
/* 400 */       constraintType = ConstraintType.CROSS_PARAMETER;
/*     */ 
/*     */     }
/* 403 */     else if (externalConstraintType != null) {
/* 404 */       constraintType = externalConstraintType;
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/* 409 */     else if ((hasGenericValidators) && (!hasCrossParameterValidator)) {
/* 410 */       constraintType = ConstraintType.GENERIC;
/*     */     }
/* 412 */     else if ((!hasGenericValidators) && (hasCrossParameterValidator)) {
/* 413 */       constraintType = ConstraintType.CROSS_PARAMETER;
/*     */     }
/* 415 */     else if (!isExecutable) {
/* 416 */       constraintType = ConstraintType.GENERIC;
/*     */     }
/* 418 */     else if (constraintAnnotationType.isAnnotationPresent(SupportedValidationTarget.class)) {
/* 419 */       SupportedValidationTarget supportedValidationTarget = (SupportedValidationTarget)constraintAnnotationType.getAnnotation(SupportedValidationTarget.class);
/* 420 */       if (supportedValidationTarget.value().length == 1) {
/* 421 */         constraintType = supportedValidationTarget.value()[0] == ValidationTarget.ANNOTATED_ELEMENT ? ConstraintType.GENERIC : ConstraintType.CROSS_PARAMETER;
/*     */       }
/*     */       
/*     */     }
/*     */     else
/*     */     {
/* 427 */       boolean hasParameters = hasParameters(member);
/* 428 */       boolean hasReturnValue = hasReturnValue(member);
/*     */       
/* 430 */       if ((!hasParameters) && (hasReturnValue)) {
/* 431 */         constraintType = ConstraintType.GENERIC;
/*     */       }
/* 433 */       else if ((hasParameters) && (!hasReturnValue)) {
/* 434 */         constraintType = ConstraintType.CROSS_PARAMETER;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 440 */     if (constraintType == null) {
/* 441 */       throw log.getImplicitConstraintTargetInAmbiguousConfigurationException(this.annotationType.getName());
/*     */     }
/*     */     
/* 444 */     if (constraintType == ConstraintType.CROSS_PARAMETER) {
/* 445 */       validateCrossParameterConstraintType(member, hasCrossParameterValidator);
/*     */     }
/*     */     
/* 448 */     return constraintType;
/*     */   }
/*     */   
/*     */   private void validateCrossParameterConstraintType(Member member, boolean hasCrossParameterValidator) {
/* 452 */     if (!hasCrossParameterValidator) {
/* 453 */       throw log.getCrossParameterConstraintHasNoValidatorException(this.annotationType.getName());
/*     */     }
/* 455 */     if (member == null) {
/* 456 */       throw log.getCrossParameterConstraintOnClassException(this.annotationType.getName());
/*     */     }
/* 458 */     if ((member instanceof Field)) {
/* 459 */       throw log.getCrossParameterConstraintOnFieldException(this.annotationType.getName(), member.toString());
/*     */     }
/* 461 */     if (!hasParameters(member)) {
/* 462 */       throw log.getCrossParameterConstraintOnMethodWithoutParametersException(this.annotationType
/* 463 */         .getName(), member
/* 464 */         .toString());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void validateComposingConstraintTypes()
/*     */   {
/* 474 */     for (ConstraintDescriptorImpl<?> composingConstraint : this.composingConstraints) {
/* 475 */       if (composingConstraint.constraintType != this.constraintType) {
/* 476 */         throw log.getComposedAndComposingConstraintsHaveDifferentTypesException(this.annotationType
/* 477 */           .getName(), composingConstraint.annotationType
/* 478 */           .getName(), this.constraintType, composingConstraint.constraintType);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean hasParameters(Member member)
/*     */   {
/* 487 */     boolean hasParameters = false;
/* 488 */     if ((member instanceof Constructor)) {
/* 489 */       Constructor<?> constructor = (Constructor)member;
/* 490 */       hasParameters = constructor.getParameterTypes().length > 0;
/*     */     }
/* 492 */     else if ((member instanceof Method)) {
/* 493 */       Method method = (Method)member;
/* 494 */       hasParameters = method.getParameterTypes().length > 0;
/*     */     }
/* 496 */     return hasParameters;
/*     */   }
/*     */   
/*     */   private boolean hasReturnValue(Member member) { boolean hasReturnValue;
/*     */     boolean hasReturnValue;
/* 501 */     if ((member instanceof Constructor)) {
/* 502 */       hasReturnValue = true;
/*     */     } else { boolean hasReturnValue;
/* 504 */       if ((member instanceof Method)) {
/* 505 */         Method method = (Method)member;
/* 506 */         hasReturnValue = method.getGenericReturnType() != Void.TYPE;
/*     */       }
/*     */       else
/*     */       {
/* 510 */         hasReturnValue = false;
/*     */       } }
/* 512 */     return hasReturnValue;
/*     */   }
/*     */   
/*     */   private boolean isExecutable(ElementType elementType) {
/* 516 */     return (elementType == ElementType.METHOD) || (elementType == ElementType.CONSTRUCTOR);
/*     */   }
/*     */   
/*     */   private Set<Class<? extends Payload>> buildPayloadSet(T annotation)
/*     */   {
/* 521 */     Set<Class<? extends Payload>> payloadSet = CollectionHelper.newHashSet();
/*     */     Class<Payload>[] payloadFromAnnotation;
/*     */     try
/*     */     {
/* 525 */       payloadFromAnnotation = (Class[])run(
/* 526 */         GetAnnotationParameter.action(annotation, "payload", Class[].class));
/*     */     }
/*     */     catch (ValidationException e)
/*     */     {
/*     */       Class<Payload>[] payloadFromAnnotation;
/* 531 */       payloadFromAnnotation = null;
/*     */     }
/* 533 */     if (payloadFromAnnotation != null) {
/* 534 */       payloadSet.addAll(Arrays.asList(payloadFromAnnotation));
/*     */     }
/* 536 */     return Collections.unmodifiableSet(payloadSet);
/*     */   }
/*     */   
/*     */   private Set<Class<?>> buildGroupSet(Class<?> implicitGroup) {
/* 540 */     Set<Class<?>> groupSet = CollectionHelper.newHashSet();
/* 541 */     Class<?>[] groupsFromAnnotation = (Class[])run(
/* 542 */       GetAnnotationParameter.action(this.annotation, "groups", Class[].class));
/*     */     
/* 544 */     if (groupsFromAnnotation.length == 0) {
/* 545 */       groupSet.add(Default.class);
/*     */     }
/*     */     else {
/* 548 */       groupSet.addAll(Arrays.asList(groupsFromAnnotation));
/*     */     }
/*     */     
/*     */ 
/* 552 */     if ((implicitGroup != null) && (groupSet.contains(Default.class))) {
/* 553 */       groupSet.add(implicitGroup);
/*     */     }
/* 555 */     return Collections.unmodifiableSet(groupSet);
/*     */   }
/*     */   
/*     */   private Map<String, Object> buildAnnotationParameterMap(Annotation annotation) {
/* 559 */     Method[] declaredMethods = (Method[])run(GetDeclaredMethods.action(annotation.annotationType()));
/* 560 */     Map<String, Object> parameters = CollectionHelper.newHashMap(declaredMethods.length);
/* 561 */     for (Method m : declaredMethods) {
/* 562 */       Object value = run(GetAnnotationParameter.action(annotation, m.getName(), Object.class));
/* 563 */       parameters.put(m.getName(), value);
/*     */     }
/* 565 */     return Collections.unmodifiableMap(parameters);
/*     */   }
/*     */   
/*     */   private Map<ConstraintDescriptorImpl<T>.ClassIndexWrapper, Map<String, Object>> parseOverrideParameters() {
/* 569 */     Map<ConstraintDescriptorImpl<T>.ClassIndexWrapper, Map<String, Object>> overrideParameters = CollectionHelper.newHashMap();
/* 570 */     Method[] methods = (Method[])run(GetDeclaredMethods.action(this.annotationType));
/* 571 */     for (Method m : methods) {
/* 572 */       if (m.getAnnotation(OverridesAttribute.class) != null) {
/* 573 */         addOverrideAttributes(overrideParameters, m, new OverridesAttribute[] {
/* 574 */           (OverridesAttribute)m.getAnnotation(OverridesAttribute.class) });
/*     */ 
/*     */       }
/* 577 */       else if (m.getAnnotation(OverridesAttribute.List.class) != null) {
/* 578 */         addOverrideAttributes(overrideParameters, m, 
/*     */         
/*     */ 
/* 581 */           ((OverridesAttribute.List)m.getAnnotation(OverridesAttribute.List.class)).value());
/*     */       }
/*     */     }
/*     */     
/* 585 */     return overrideParameters;
/*     */   }
/*     */   
/*     */   private void addOverrideAttributes(Map<ConstraintDescriptorImpl<T>.ClassIndexWrapper, Map<String, Object>> overrideParameters, Method m, OverridesAttribute... attributes) {
/* 589 */     Object value = run(GetAnnotationParameter.action(this.annotation, m.getName(), Object.class));
/* 590 */     for (OverridesAttribute overridesAttribute : attributes) {
/* 591 */       ensureAttributeIsOverridable(m, overridesAttribute);
/*     */       
/*     */ 
/* 594 */       ConstraintDescriptorImpl<T>.ClassIndexWrapper wrapper = new ClassIndexWrapper(overridesAttribute.constraint(), overridesAttribute.constraintIndex());
/*     */       
/* 596 */       Map<String, Object> map = (Map)overrideParameters.get(wrapper);
/* 597 */       if (map == null) {
/* 598 */         map = CollectionHelper.newHashMap();
/* 599 */         overrideParameters.put(wrapper, map);
/*     */       }
/* 601 */       map.put(overridesAttribute.name(), value);
/*     */     }
/*     */   }
/*     */   
/*     */   private void ensureAttributeIsOverridable(Method m, OverridesAttribute overridesAttribute) {
/* 606 */     Method method = (Method)run(GetMethod.action(overridesAttribute.constraint(), overridesAttribute.name()));
/* 607 */     if (method == null) {
/* 608 */       throw log.getOverriddenConstraintAttributeNotFoundException(overridesAttribute.name());
/*     */     }
/* 610 */     Class<?> returnTypeOfOverriddenConstraint = method.getReturnType();
/* 611 */     if (!returnTypeOfOverriddenConstraint.equals(m.getReturnType())) {
/* 612 */       throw log.getWrongAttributeTypeForOverriddenConstraintException(returnTypeOfOverriddenConstraint
/* 613 */         .getName(), m
/* 614 */         .getReturnType());
/*     */     }
/*     */   }
/*     */   
/*     */   private Set<ConstraintDescriptorImpl<?>> parseComposingConstraints(Member member, ConstraintHelper constraintHelper, ConstraintType constraintType)
/*     */   {
/* 620 */     Set<ConstraintDescriptorImpl<?>> composingConstraintsSet = CollectionHelper.newHashSet();
/* 621 */     Map<ConstraintDescriptorImpl<T>.ClassIndexWrapper, Map<String, Object>> overrideParameters = parseOverrideParameters();
/*     */     int index;
/* 623 */     for (Annotation declaredAnnotation : this.annotationType.getDeclaredAnnotations()) {
/* 624 */       Class<? extends Annotation> declaredAnnotationType = declaredAnnotation.annotationType();
/* 625 */       if (!NON_COMPOSING_CONSTRAINT_ANNOTATIONS.contains(declaredAnnotationType.getName()))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 630 */         if (constraintHelper.isConstraintAnnotation(declaredAnnotationType)) {
/* 631 */           ConstraintDescriptorImpl<?> descriptor = createComposingConstraintDescriptor(member, overrideParameters, -1, declaredAnnotation, constraintType, constraintHelper);
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 639 */           composingConstraintsSet.add(descriptor);
/* 640 */           log.debugf("Adding composing constraint: %s.", descriptor);
/*     */         }
/* 642 */         else if (constraintHelper.isMultiValueConstraint(declaredAnnotationType)) {
/* 643 */           List<Annotation> multiValueConstraints = constraintHelper.getConstraintsFromMultiValueConstraint(declaredAnnotation);
/* 644 */           index = 0;
/* 645 */           for (Annotation constraintAnnotation : multiValueConstraints) {
/* 646 */             ConstraintDescriptorImpl<?> descriptor = createComposingConstraintDescriptor(member, overrideParameters, index, constraintAnnotation, constraintType, constraintHelper);
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 654 */             composingConstraintsSet.add(descriptor);
/* 655 */             log.debugf("Adding composing constraint: %s.", descriptor);
/* 656 */             index++;
/*     */           }
/*     */         } }
/*     */     }
/* 660 */     return Collections.unmodifiableSet(composingConstraintsSet);
/*     */   }
/*     */   
/*     */   private CompositionType parseCompositionType(ConstraintHelper constraintHelper) {
/* 664 */     for (Annotation declaredAnnotation : this.annotationType.getDeclaredAnnotations()) {
/* 665 */       Class<? extends Annotation> declaredAnnotationType = declaredAnnotation.annotationType();
/* 666 */       if (!NON_COMPOSING_CONSTRAINT_ANNOTATIONS.contains(declaredAnnotationType.getName()))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 671 */         if (constraintHelper.isConstraintComposition(declaredAnnotationType)) {
/* 672 */           if (log.isDebugEnabled()) {
/* 673 */             log.debugf("Adding Bool %s.", declaredAnnotationType.getName());
/*     */           }
/* 675 */           return ((ConstraintComposition)declaredAnnotation).value();
/*     */         } }
/*     */     }
/* 678 */     return CompositionType.AND;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private <U extends Annotation> ConstraintDescriptorImpl<U> createComposingConstraintDescriptor(Member member, Map<ConstraintDescriptorImpl<T>.ClassIndexWrapper, Map<String, Object>> overrideParameters, int index, U constraintAnnotation, ConstraintType constraintType, ConstraintHelper constraintHelper)
/*     */   {
/* 690 */     Class<U> annotationType = constraintAnnotation.annotationType();
/*     */     
/*     */ 
/*     */ 
/* 694 */     AnnotationDescriptor<U> annotationDescriptor = new AnnotationDescriptor(annotationType, buildAnnotationParameterMap(constraintAnnotation));
/*     */     
/*     */ 
/*     */ 
/* 698 */     Map<String, Object> overrides = (Map)overrideParameters.get(new ClassIndexWrapper(annotationType, index));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 703 */     if (overrides != null) {
/* 704 */       for (Map.Entry<String, Object> entry : overrides.entrySet()) {
/* 705 */         annotationDescriptor.setValue((String)entry.getKey(), entry.getValue());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 710 */     annotationDescriptor.setValue("groups", this.groups.toArray(new Class[this.groups.size()]));
/* 711 */     annotationDescriptor.setValue("payload", this.payloads.toArray(new Class[this.payloads.size()]));
/* 712 */     if (annotationDescriptor.getElements().containsKey("validationAppliesTo")) {
/* 713 */       ConstraintTarget validationAppliesTo = getValidationAppliesTo();
/*     */       
/*     */ 
/* 716 */       if (validationAppliesTo == null) {
/* 717 */         if (constraintType == ConstraintType.CROSS_PARAMETER) {
/* 718 */           validationAppliesTo = ConstraintTarget.PARAMETERS;
/*     */         }
/*     */         else {
/* 721 */           validationAppliesTo = ConstraintTarget.IMPLICIT;
/*     */         }
/*     */       }
/*     */       
/* 725 */       annotationDescriptor.setValue("validationAppliesTo", validationAppliesTo);
/*     */     }
/*     */     
/* 728 */     Object annotationProxy = AnnotationFactory.create(annotationDescriptor);
/* 729 */     return new ConstraintDescriptorImpl(constraintHelper, member, (Annotation)annotationProxy, this.elementType, null, this.definedOn, constraintType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private <P> P run(PrivilegedAction<P> action)
/*     */   {
/* 741 */     return (P)(System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public CompositionType getCompositionType()
/*     */   {
/* 748 */     return this.compositionType;
/*     */   }
/*     */   
/*     */ 
/*     */   private class ClassIndexWrapper
/*     */   {
/*     */     final Class<?> clazz;
/*     */     final int index;
/*     */     
/*     */     ClassIndexWrapper(int clazz)
/*     */     {
/* 759 */       this.clazz = clazz;
/* 760 */       this.index = index;
/*     */     }
/*     */     
/*     */     public boolean equals(Object o)
/*     */     {
/* 765 */       if (this == o) {
/* 766 */         return true;
/*     */       }
/* 768 */       if ((o == null) || (getClass() != o.getClass())) {
/* 769 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 773 */       ConstraintDescriptorImpl<T>.ClassIndexWrapper that = (ClassIndexWrapper)o;
/*     */       
/* 775 */       if (this.index != that.index) {
/* 776 */         return false;
/*     */       }
/* 778 */       if ((this.clazz != null) && (!this.clazz.equals(that.clazz))) {
/* 779 */         return false;
/*     */       }
/* 781 */       if ((this.clazz == null) && (that.clazz != null)) {
/* 782 */         return false;
/*     */       }
/*     */       
/* 785 */       return true;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 790 */       int result = this.clazz != null ? this.clazz.hashCode() : 0;
/* 791 */       result = 31 * result + this.index;
/* 792 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static enum ConstraintType
/*     */   {
/* 803 */     GENERIC, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 808 */     CROSS_PARAMETER;
/*     */     
/*     */     private ConstraintType() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\descriptor\ConstraintDescriptorImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */