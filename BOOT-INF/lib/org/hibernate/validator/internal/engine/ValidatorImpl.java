/*      */ package org.hibernate.validator.internal.engine;
/*      */ 
/*      */ import java.lang.annotation.ElementType;
/*      */ import java.lang.reflect.AccessibleObject;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Member;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Type;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import javax.validation.ConstraintValidatorFactory;
/*      */ import javax.validation.ConstraintViolation;
/*      */ import javax.validation.ElementKind;
/*      */ import javax.validation.MessageInterpolator;
/*      */ import javax.validation.ParameterNameProvider;
/*      */ import javax.validation.Path;
/*      */ import javax.validation.Path.Node;
/*      */ import javax.validation.TraversableResolver;
/*      */ import javax.validation.Validator;
/*      */ import javax.validation.executable.ExecutableValidator;
/*      */ import javax.validation.groups.Default;
/*      */ import javax.validation.metadata.BeanDescriptor;
/*      */ import org.hibernate.validator.HibernateValidatorPermission;
/*      */ import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorManager;
/*      */ import org.hibernate.validator.internal.engine.groups.Group;
/*      */ import org.hibernate.validator.internal.engine.groups.GroupWithInheritance;
/*      */ import org.hibernate.validator.internal.engine.groups.Sequence;
/*      */ import org.hibernate.validator.internal.engine.groups.ValidationOrder;
/*      */ import org.hibernate.validator.internal.engine.groups.ValidationOrderGenerator;
/*      */ import org.hibernate.validator.internal.engine.path.NodeImpl;
/*      */ import org.hibernate.validator.internal.engine.path.PathImpl;
/*      */ import org.hibernate.validator.internal.engine.resolver.CachingTraversableResolverForSingleValidation;
/*      */ import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
/*      */ import org.hibernate.validator.internal.metadata.BeanMetaDataManager;
/*      */ import org.hibernate.validator.internal.metadata.aggregated.BeanMetaData;
/*      */ import org.hibernate.validator.internal.metadata.aggregated.ExecutableMetaData;
/*      */ import org.hibernate.validator.internal.metadata.aggregated.ParameterMetaData;
/*      */ import org.hibernate.validator.internal.metadata.aggregated.PropertyMetaData;
/*      */ import org.hibernate.validator.internal.metadata.aggregated.ReturnValueMetaData;
/*      */ import org.hibernate.validator.internal.metadata.core.MetaConstraint;
/*      */ import org.hibernate.validator.internal.metadata.facets.Cascadable;
/*      */ import org.hibernate.validator.internal.metadata.facets.Validatable;
/*      */ import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
/*      */ import org.hibernate.validator.internal.metadata.raw.ExecutableElement;
/*      */ import org.hibernate.validator.internal.util.CollectionHelper;
/*      */ import org.hibernate.validator.internal.util.ConcurrentReferenceHashMap;
/*      */ import org.hibernate.validator.internal.util.ConcurrentReferenceHashMap.ReferenceType;
/*      */ import org.hibernate.validator.internal.util.Contracts;
/*      */ import org.hibernate.validator.internal.util.ReflectionHelper;
/*      */ import org.hibernate.validator.internal.util.TypeHelper;
/*      */ import org.hibernate.validator.internal.util.TypeResolutionHelper;
/*      */ import org.hibernate.validator.internal.util.logging.Log;
/*      */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*      */ import org.hibernate.validator.internal.util.logging.Messages;
/*      */ import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredField;
/*      */ import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredMethod;
/*      */ import org.hibernate.validator.internal.util.privilegedactions.SetAccessibility;
/*      */ import org.hibernate.validator.spi.time.TimeProvider;
/*      */ import org.hibernate.validator.spi.valuehandling.ValidatedValueUnwrapper;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ValidatorImpl
/*      */   implements Validator, ExecutableValidator
/*      */ {
/*      */   private static final String TYPE_USE = "TYPE_USE";
/*   95 */   private static final Log log = ;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  100 */   private static final Collection<Class<?>> DEFAULT_GROUPS = Collections.singletonList(Default.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final transient ValidationOrderGenerator validationOrderGenerator;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final ConstraintValidatorFactory constraintValidatorFactory;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final MessageInterpolator messageInterpolator;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final TraversableResolver traversableResolver;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final BeanMetaDataManager beanMetaDataManager;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final ConstraintValidatorManager constraintValidatorManager;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final ParameterNameProvider parameterNameProvider;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final TimeProvider timeProvider;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final boolean failFast;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final TypeResolutionHelper typeResolutionHelper;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final List<ValidatedValueUnwrapper<?>> validatedValueHandlers;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final ConcurrentMap<Member, Member> accessibleMembers;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ValidatorImpl(ConstraintValidatorFactory constraintValidatorFactory, MessageInterpolator messageInterpolator, TraversableResolver traversableResolver, BeanMetaDataManager beanMetaDataManager, ParameterNameProvider parameterNameProvider, TimeProvider timeProvider, TypeResolutionHelper typeResolutionHelper, List<ValidatedValueUnwrapper<?>> validatedValueHandlers, ConstraintValidatorManager constraintValidatorManager, boolean failFast)
/*      */   {
/*  171 */     this.constraintValidatorFactory = constraintValidatorFactory;
/*  172 */     this.messageInterpolator = messageInterpolator;
/*  173 */     this.traversableResolver = traversableResolver;
/*  174 */     this.beanMetaDataManager = beanMetaDataManager;
/*  175 */     this.parameterNameProvider = parameterNameProvider;
/*  176 */     this.timeProvider = timeProvider;
/*  177 */     this.typeResolutionHelper = typeResolutionHelper;
/*  178 */     this.validatedValueHandlers = validatedValueHandlers;
/*  179 */     this.constraintValidatorManager = constraintValidatorManager;
/*  180 */     this.failFast = failFast;
/*      */     
/*  182 */     this.validationOrderGenerator = new ValidationOrderGenerator();
/*      */     
/*  184 */     this.accessibleMembers = new ConcurrentReferenceHashMap(100, ConcurrentReferenceHashMap.ReferenceType.SOFT, ConcurrentReferenceHashMap.ReferenceType.SOFT);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups)
/*      */   {
/*  193 */     Contracts.assertNotNull(object, Messages.MESSAGES.validatedObjectMustNotBeNull());
/*      */     
/*  195 */     if (!this.beanMetaDataManager.isConstrained(object.getClass())) {
/*  196 */       return Collections.emptySet();
/*      */     }
/*      */     
/*  199 */     ValidationOrder validationOrder = determineGroupValidationOrder(groups);
/*  200 */     ValidationContext<T> validationContext = getValidationContext().forValidate(object);
/*      */     
/*  202 */     ValueContext<?, Object> valueContext = ValueContext.getLocalExecutionContext(object, this.beanMetaDataManager
/*      */     
/*  204 */       .getBeanMetaData(object.getClass()), 
/*  205 */       PathImpl.createRootPath());
/*      */     
/*      */ 
/*  208 */     return validateInContext(valueContext, validationContext, validationOrder);
/*      */   }
/*      */   
/*      */   public final <T> Set<ConstraintViolation<T>> validateProperty(T object, String propertyName, Class<?>... groups)
/*      */   {
/*  213 */     Contracts.assertNotNull(object, Messages.MESSAGES.validatedObjectMustNotBeNull());
/*      */     
/*  215 */     sanityCheckPropertyPath(propertyName);
/*  216 */     ValidationOrder validationOrder = determineGroupValidationOrder(groups);
/*  217 */     ValidationContext<T> context = getValidationContext().forValidateProperty(object);
/*      */     
/*  219 */     if (!this.beanMetaDataManager.isConstrained(context.getRootBeanClass())) {
/*  220 */       return Collections.emptySet();
/*      */     }
/*      */     
/*  223 */     return validatePropertyInContext(context, 
/*      */     
/*  225 */       PathImpl.createPathFromString(propertyName), validationOrder);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public final <T> Set<ConstraintViolation<T>> validateValue(Class<T> beanType, String propertyName, Object value, Class<?>... groups)
/*      */   {
/*  232 */     Contracts.assertNotNull(beanType, Messages.MESSAGES.beanTypeCannotBeNull());
/*      */     
/*  234 */     if (!this.beanMetaDataManager.isConstrained(beanType)) {
/*  235 */       return Collections.emptySet();
/*      */     }
/*      */     
/*  238 */     sanityCheckPropertyPath(propertyName);
/*  239 */     ValidationOrder validationOrder = determineGroupValidationOrder(groups);
/*  240 */     ValidationContext<T> context = getValidationContext().forValidateValue(beanType);
/*      */     
/*  242 */     return validateValueInContext(context, value, 
/*      */     
/*      */ 
/*  245 */       PathImpl.createPathFromString(propertyName), validationOrder);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public <T> Set<ConstraintViolation<T>> validateParameters(T object, Method method, Object[] parameterValues, Class<?>... groups)
/*      */   {
/*  252 */     Contracts.assertNotNull(object, Messages.MESSAGES.validatedObjectMustNotBeNull());
/*  253 */     Contracts.assertNotNull(method, Messages.MESSAGES.validatedMethodMustNotBeNull());
/*  254 */     Contracts.assertNotNull(parameterValues, Messages.MESSAGES.validatedParameterArrayMustNotBeNull());
/*      */     
/*  256 */     return validateParameters(object, ExecutableElement.forMethod(method), parameterValues, groups);
/*      */   }
/*      */   
/*      */   public <T> Set<ConstraintViolation<T>> validateConstructorParameters(Constructor<? extends T> constructor, Object[] parameterValues, Class<?>... groups)
/*      */   {
/*  261 */     Contracts.assertNotNull(constructor, Messages.MESSAGES.validatedConstructorMustNotBeNull());
/*  262 */     Contracts.assertNotNull(parameterValues, Messages.MESSAGES.validatedParameterArrayMustNotBeNull());
/*      */     
/*  264 */     return validateParameters(null, ExecutableElement.forConstructor(constructor), parameterValues, groups);
/*      */   }
/*      */   
/*      */   public <T> Set<ConstraintViolation<T>> validateConstructorReturnValue(Constructor<? extends T> constructor, T createdObject, Class<?>... groups)
/*      */   {
/*  269 */     Contracts.assertNotNull(constructor, Messages.MESSAGES.validatedConstructorMustNotBeNull());
/*  270 */     Contracts.assertNotNull(createdObject, Messages.MESSAGES.validatedConstructorCreatedInstanceMustNotBeNull());
/*      */     
/*  272 */     return validateReturnValue(null, ExecutableElement.forConstructor(constructor), createdObject, groups);
/*      */   }
/*      */   
/*      */   public <T> Set<ConstraintViolation<T>> validateReturnValue(T object, Method method, Object returnValue, Class<?>... groups)
/*      */   {
/*  277 */     Contracts.assertNotNull(object, Messages.MESSAGES.validatedObjectMustNotBeNull());
/*  278 */     Contracts.assertNotNull(method, Messages.MESSAGES.validatedMethodMustNotBeNull());
/*      */     
/*  280 */     return validateReturnValue(object, ExecutableElement.forMethod(method), returnValue, groups);
/*      */   }
/*      */   
/*      */   private <T> Set<ConstraintViolation<T>> validateParameters(T object, ExecutableElement executable, Object[] parameterValues, Class<?>... groups)
/*      */   {
/*  285 */     if (parameterValues == null) {
/*  286 */       return Collections.emptySet();
/*      */     }
/*      */     
/*  289 */     ValidationOrder validationOrder = determineGroupValidationOrder(groups);
/*      */     
/*  291 */     ValidationContext<T> context = getValidationContext().forValidateParameters(this.parameterNameProvider, object, executable, parameterValues);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  298 */     if (!this.beanMetaDataManager.isConstrained(context.getRootBeanClass())) {
/*  299 */       return Collections.emptySet();
/*      */     }
/*      */     
/*  302 */     validateParametersInContext(context, parameterValues, validationOrder);
/*      */     
/*  304 */     return context.getFailingConstraints();
/*      */   }
/*      */   
/*      */   private <T> Set<ConstraintViolation<T>> validateReturnValue(T object, ExecutableElement executable, Object returnValue, Class<?>... groups) {
/*  308 */     ValidationOrder validationOrder = determineGroupValidationOrder(groups);
/*      */     
/*  310 */     ValidationContext<T> context = getValidationContext().forValidateReturnValue(object, executable, returnValue);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  316 */     if (!this.beanMetaDataManager.isConstrained(context.getRootBeanClass())) {
/*  317 */       return Collections.emptySet();
/*      */     }
/*      */     
/*  320 */     validateReturnValueInContext(context, object, returnValue, validationOrder);
/*      */     
/*  322 */     return context.getFailingConstraints();
/*      */   }
/*      */   
/*      */   public final BeanDescriptor getConstraintsForClass(Class<?> clazz)
/*      */   {
/*  327 */     return this.beanMetaDataManager.getBeanMetaData(clazz).getBeanDescriptor();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public final <T> T unwrap(Class<T> type)
/*      */   {
/*  335 */     if (type.isAssignableFrom(Validator.class)) {
/*  336 */       return (T)type.cast(this);
/*      */     }
/*      */     
/*  339 */     throw log.getTypeNotSupportedForUnwrappingException(type);
/*      */   }
/*      */   
/*      */   public ExecutableValidator forExecutables()
/*      */   {
/*  344 */     return this;
/*      */   }
/*      */   
/*      */   private ValidationContext.ValidationContextBuilder getValidationContext() {
/*  348 */     return ValidationContext.getValidationContext(this.constraintValidatorManager, this.messageInterpolator, this.constraintValidatorFactory, 
/*      */     
/*      */ 
/*      */ 
/*  352 */       getCachingTraversableResolver(), this.timeProvider, this.validatedValueHandlers, this.typeResolutionHelper, this.failFast);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void sanityCheckPropertyPath(String propertyName)
/*      */   {
/*  361 */     if ((propertyName == null) || (propertyName.length() == 0)) {
/*  362 */       throw log.getInvalidPropertyPathException();
/*      */     }
/*      */   }
/*      */   
/*      */   private ValidationOrder determineGroupValidationOrder(Class<?>[] groups) {
/*  367 */     Contracts.assertNotNull(groups, Messages.MESSAGES.groupMustNotBeNull());
/*  368 */     for (Class<?> clazz : groups) {
/*  369 */       if (clazz == null) {
/*  370 */         throw new IllegalArgumentException(Messages.MESSAGES.groupMustNotBeNull());
/*      */       }
/*      */     }
/*      */     
/*      */     Object resultGroups;
/*      */     Object resultGroups;
/*  376 */     if (groups.length == 0) {
/*  377 */       resultGroups = DEFAULT_GROUPS;
/*      */     }
/*      */     else {
/*  380 */       resultGroups = Arrays.asList(groups);
/*      */     }
/*  382 */     return this.validationOrderGenerator.getValidationOrder((Collection)resultGroups);
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
/*      */   private <T, U> Set<ConstraintViolation<T>> validateInContext(ValueContext<U, Object> valueContext, ValidationContext<T> context, ValidationOrder validationOrder)
/*      */   {
/*  396 */     if (valueContext.getCurrentBean() == null) {
/*  397 */       return Collections.emptySet();
/*      */     }
/*      */     
/*  400 */     BeanMetaData<U> beanMetaData = this.beanMetaDataManager.getBeanMetaData(valueContext.getCurrentBeanType());
/*  401 */     if (beanMetaData.defaultGroupSequenceIsRedefined()) {
/*  402 */       validationOrder.assertDefaultGroupSequenceIsExpandable(beanMetaData.getDefaultGroupSequence(valueContext.getCurrentBean()));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  407 */     Iterator<Group> groupIterator = validationOrder.getGroupIterator();
/*  408 */     while (groupIterator.hasNext()) {
/*  409 */       Group group = (Group)groupIterator.next();
/*  410 */       valueContext.setCurrentGroup(group.getDefiningClass());
/*  411 */       validateConstraintsForCurrentGroup(context, valueContext);
/*  412 */       if (shouldFailFast(context)) {
/*  413 */         return context.getFailingConstraints();
/*      */       }
/*      */     }
/*  416 */     groupIterator = validationOrder.getGroupIterator();
/*  417 */     while (groupIterator.hasNext()) {
/*  418 */       Group group = (Group)groupIterator.next();
/*  419 */       valueContext.setCurrentGroup(group.getDefiningClass());
/*  420 */       validateCascadedConstraints(context, valueContext);
/*  421 */       if (shouldFailFast(context)) {
/*  422 */         return context.getFailingConstraints();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  427 */     Iterator<Sequence> sequenceIterator = validationOrder.getSequenceIterator();
/*  428 */     while (sequenceIterator.hasNext()) {
/*  429 */       Sequence sequence = (Sequence)sequenceIterator.next();
/*  430 */       for (GroupWithInheritance groupOfGroups : sequence) {
/*  431 */         int numberOfViolations = context.getFailingConstraints().size();
/*      */         
/*  433 */         for (Group group : groupOfGroups) {
/*  434 */           valueContext.setCurrentGroup(group.getDefiningClass());
/*      */           
/*  436 */           validateConstraintsForCurrentGroup(context, valueContext);
/*  437 */           if (shouldFailFast(context)) {
/*  438 */             return context.getFailingConstraints();
/*      */           }
/*      */           
/*  441 */           validateCascadedConstraints(context, valueContext);
/*  442 */           if (shouldFailFast(context)) {
/*  443 */             return context.getFailingConstraints();
/*      */           }
/*      */         }
/*  446 */         if (context.getFailingConstraints().size() > numberOfViolations) {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/*  451 */     return context.getFailingConstraints();
/*      */   }
/*      */   
/*      */ 
/*      */   private void validateConstraintsForCurrentGroup(ValidationContext<?> validationContext, ValueContext<?, Object> valueContext)
/*      */   {
/*  457 */     if (!valueContext.validatingDefault()) {
/*  458 */       validateConstraintsForNonDefaultGroup(validationContext, valueContext);
/*      */     }
/*      */     else {
/*  461 */       validateConstraintsForDefaultGroup(validationContext, valueContext);
/*      */     }
/*      */   }
/*      */   
/*      */   private <U> void validateConstraintsForDefaultGroup(ValidationContext<?> validationContext, ValueContext<U, Object> valueContext) {
/*  466 */     BeanMetaData<U> beanMetaData = this.beanMetaDataManager.getBeanMetaData(valueContext.getCurrentBeanType());
/*  467 */     Map<Class<?>, Class<?>> validatedInterfaces = CollectionHelper.newHashMap();
/*      */     
/*      */ 
/*  470 */     for (Class<? super U> clazz : beanMetaData.getClassHierarchy()) {
/*  471 */       BeanMetaData<? super U> hostingBeanMetaData = this.beanMetaDataManager.getBeanMetaData(clazz);
/*  472 */       boolean defaultGroupSequenceIsRedefined = hostingBeanMetaData.defaultGroupSequenceIsRedefined();
/*      */       
/*      */ 
/*  475 */       if (defaultGroupSequenceIsRedefined) {
/*  476 */         Iterator<Sequence> defaultGroupSequence = hostingBeanMetaData.getDefaultValidationSequence(valueContext.getCurrentBean());
/*  477 */         Set<MetaConstraint<?>> metaConstraints = hostingBeanMetaData.getMetaConstraints();
/*      */         
/*  479 */         while (defaultGroupSequence.hasNext()) {
/*  480 */           for (GroupWithInheritance groupOfGroups : (Sequence)defaultGroupSequence.next()) {
/*  481 */             boolean validationSuccessful = true;
/*      */             
/*  483 */             for (Group defaultSequenceMember : groupOfGroups) {
/*  484 */               validationSuccessful = validateConstraintsForSingleDefaultGroupElement(validationContext, valueContext, validatedInterfaces, clazz, metaConstraints, defaultSequenceMember);
/*      */             }
/*      */             
/*  487 */             if (!validationSuccessful) {
/*      */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  495 */         Set<MetaConstraint<?>> metaConstraints = hostingBeanMetaData.getDirectMetaConstraints();
/*  496 */         validateConstraintsForSingleDefaultGroupElement(validationContext, valueContext, validatedInterfaces, clazz, metaConstraints, Group.DEFAULT_GROUP);
/*      */       }
/*      */       
/*      */ 
/*  500 */       validationContext.markCurrentBeanAsProcessed(valueContext);
/*      */       
/*      */ 
/*  503 */       if (defaultGroupSequenceIsRedefined) {
/*      */         break;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private <U> boolean validateConstraintsForSingleDefaultGroupElement(ValidationContext<?> validationContext, ValueContext<U, Object> valueContext, Map<Class<?>, Class<?>> validatedInterfaces, Class<? super U> clazz, Set<MetaConstraint<?>> metaConstraints, Group defaultSequenceMember)
/*      */   {
/*  511 */     boolean validationSuccessful = true;
/*      */     
/*  513 */     valueContext.setCurrentGroup(defaultSequenceMember.getDefiningClass());
/*  514 */     PathImpl currentPath = valueContext.getPropertyPath();
/*      */     
/*  516 */     for (MetaConstraint<?> metaConstraint : metaConstraints)
/*      */     {
/*      */ 
/*  519 */       Class<?> declaringClass = metaConstraint.getLocation().getDeclaringClass();
/*  520 */       if (declaringClass.isInterface()) {
/*  521 */         Class<?> validatedForClass = (Class)validatedInterfaces.get(declaringClass);
/*  522 */         if ((validatedForClass == null) || (validatedForClass.equals(clazz)))
/*      */         {
/*      */ 
/*  525 */           validatedInterfaces.put(declaringClass, clazz);
/*      */         }
/*      */       } else {
/*  528 */         boolean tmp = validateConstraint(validationContext, valueContext, false, metaConstraint);
/*  529 */         if (shouldFailFast(validationContext)) {
/*  530 */           return false;
/*      */         }
/*      */         
/*  533 */         validationSuccessful = (validationSuccessful) && (tmp);
/*      */         
/*  535 */         valueContext.setPropertyPath(currentPath);
/*      */       } }
/*  537 */     return validationSuccessful;
/*      */   }
/*      */   
/*      */   private void validateConstraintsForNonDefaultGroup(ValidationContext<?> validationContext, ValueContext<?, Object> valueContext) {
/*  541 */     BeanMetaData<?> beanMetaData = this.beanMetaDataManager.getBeanMetaData(valueContext.getCurrentBeanType());
/*  542 */     PathImpl currentPath = valueContext.getPropertyPath();
/*  543 */     for (MetaConstraint<?> metaConstraint : beanMetaData.getMetaConstraints()) {
/*  544 */       validateConstraint(validationContext, valueContext, false, metaConstraint);
/*  545 */       if (shouldFailFast(validationContext)) {
/*  546 */         return;
/*      */       }
/*      */       
/*  549 */       valueContext.setPropertyPath(currentPath);
/*      */     }
/*  551 */     validationContext.markCurrentBeanAsProcessed(valueContext);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean validateConstraint(ValidationContext<?> validationContext, ValueContext<?, Object> valueContext, boolean propertyPathComplete, MetaConstraint<?> metaConstraint)
/*      */   {
/*  561 */     if (metaConstraint.getElementType() != ElementType.TYPE)
/*      */     {
/*  563 */       PropertyMetaData propertyMetaData = this.beanMetaDataManager.getBeanMetaData(valueContext.getCurrentBeanType()).getMetaDataFor(
/*  564 */         ReflectionHelper.getPropertyName(metaConstraint.getLocation().getMember()));
/*      */       
/*      */ 
/*  567 */       if (!propertyPathComplete) {
/*  568 */         valueContext.appendNode(propertyMetaData);
/*      */       }
/*      */       
/*  571 */       if ("TYPE_USE".equals(metaConstraint.getElementType().name()))
/*      */       {
/*  573 */         valueContext.setUnwrapMode(UnwrapMode.UNWRAP);
/*      */       }
/*      */       else
/*      */       {
/*  577 */         valueContext.setUnwrapMode(propertyMetaData.unwrapMode());
/*      */       }
/*      */     }
/*      */     else {
/*  581 */       valueContext.appendBeanNode();
/*      */     }
/*      */     
/*  584 */     boolean validationSuccessful = validateMetaConstraint(validationContext, valueContext, metaConstraint);
/*      */     
/*      */ 
/*  587 */     valueContext.setUnwrapMode(UnwrapMode.AUTOMATIC);
/*  588 */     return validationSuccessful;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean validatePropertyTypeConstraint(ValidationContext<?> validationContext, ValueContext<?, Object> valueContext, boolean propertyPathComplete, MetaConstraint<?> metaConstraint)
/*      */   {
/*  597 */     PropertyMetaData propertyMetaData = this.beanMetaDataManager.getBeanMetaData(valueContext.getCurrentBeanType()).getMetaDataFor(
/*  598 */       ReflectionHelper.getPropertyName(metaConstraint.getLocation().getMember()));
/*      */     
/*      */ 
/*  601 */     if (!propertyPathComplete) {
/*  602 */       valueContext.appendNode(propertyMetaData);
/*      */     }
/*      */     
/*  605 */     valueContext.setUnwrapMode(UnwrapMode.UNWRAP);
/*  606 */     boolean validationSuccessful = validateMetaConstraint(validationContext, valueContext, metaConstraint);
/*  607 */     valueContext.setUnwrapMode(UnwrapMode.AUTOMATIC);
/*      */     
/*  609 */     return validationSuccessful;
/*      */   }
/*      */   
/*      */   private boolean validateMetaConstraint(ValidationContext<?> validationContext, ValueContext<?, Object> valueContext, MetaConstraint<?> metaConstraint) {
/*  613 */     if (isValidationRequired(validationContext, valueContext, metaConstraint)) {
/*  614 */       if (valueContext.getCurrentBean() != null) {
/*  615 */         Object valueToValidate = getBeanMemberValue(valueContext
/*  616 */           .getCurrentBean(), metaConstraint
/*  617 */           .getLocation().getMember());
/*      */         
/*  619 */         valueContext.setCurrentValidatedValue(valueToValidate);
/*      */       }
/*  621 */       return metaConstraint.validateConstraint(validationContext, valueContext);
/*      */     }
/*  623 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void validateCascadedConstraints(ValidationContext<?> validationContext, ValueContext<?, Object> valueContext)
/*      */   {
/*  634 */     Validatable validatable = valueContext.getCurrentValidatable();
/*  635 */     PathImpl originalPath = valueContext.getPropertyPath();
/*  636 */     Class<?> originalGroup = valueContext.getCurrentGroup();
/*      */     
/*  638 */     for (Cascadable cascadable : validatable.getCascadables()) {
/*  639 */       valueContext.appendNode(cascadable);
/*  640 */       Class<?> group = cascadable.convertGroup(originalGroup);
/*  641 */       valueContext.setCurrentGroup(group);
/*      */       
/*  643 */       ElementType elementType = cascadable.getElementType();
/*  644 */       if (isCascadeRequired(validationContext, valueContext
/*      */       
/*  646 */         .getCurrentBean(), valueContext
/*  647 */         .getPropertyPath(), elementType))
/*      */       {
/*      */ 
/*      */ 
/*  651 */         Object value = getBeanPropertyValue(validationContext, valueContext.getCurrentBean(), cascadable);
/*      */         
/*  653 */         if (value != null)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*  658 */           ValidationOrder validationOrder = this.validationOrderGenerator.getValidationOrder(group, group != originalGroup);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  663 */           Type type = value.getClass();
/*      */           
/*      */ 
/*  666 */           if ((ReflectionHelper.isIterable(type)) || (ReflectionHelper.isMap(type))) {
/*  667 */             Iterator<?> valueIter = Collections.singletonList(value).iterator();
/*  668 */             validateCascadedConstraint(validationContext, valueIter, false, valueContext, validationOrder, 
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  674 */               Collections.emptySet());
/*      */             
/*  676 */             if (shouldFailFast(validationContext)) {
/*  677 */               return;
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*  682 */           Iterator<?> elementsIter = createIteratorForCascadedValue(type, value, valueContext);
/*  683 */           boolean isIndexable = ReflectionHelper.isIndexable(type);
/*      */           
/*  685 */           validateCascadedConstraint(validationContext, elementsIter, isIndexable, valueContext, validationOrder, cascadable
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  691 */             .getTypeArgumentsConstraints());
/*      */           
/*  693 */           if (shouldFailFast(validationContext)) {
/*  694 */             return;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  700 */       valueContext.setPropertyPath(originalPath);
/*  701 */       valueContext.setCurrentGroup(originalGroup);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Iterator<?> createIteratorForCascadedValue(Type type, Object value, ValueContext<?, ?> valueContext)
/*      */   {
/*      */     Iterator<?> iter;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  717 */     if (ReflectionHelper.isIterable(type)) {
/*  718 */       Iterator<?> iter = ((Iterable)value).iterator();
/*  719 */       valueContext.markCurrentPropertyAsIterable();
/*      */     }
/*  721 */     else if (ReflectionHelper.isMap(type)) {
/*  722 */       Map<?, ?> map = (Map)value;
/*  723 */       Iterator<?> iter = map.entrySet().iterator();
/*  724 */       valueContext.markCurrentPropertyAsIterable();
/*      */     }
/*  726 */     else if (TypeHelper.isArray(type)) {
/*  727 */       List<?> arrayList = Arrays.asList((Object[])value);
/*  728 */       Iterator<?> iter = arrayList.iterator();
/*  729 */       valueContext.markCurrentPropertyAsIterable();
/*      */     }
/*      */     else {
/*  732 */       iter = Collections.singletonList(value).iterator();
/*      */     }
/*  734 */     return iter;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void validateCascadedConstraint(ValidationContext<?> context, Iterator<?> iter, boolean isIndexable, ValueContext<?, Object> valueContext, ValidationOrder validationOrder, Set<MetaConstraint<?>> typeArgumentsConstraint)
/*      */   {
/*  741 */     int i = 0;
/*  742 */     while (iter.hasNext()) {
/*  743 */       Object value = iter.next();
/*  744 */       if ((value instanceof Map.Entry)) {
/*  745 */         Object mapKey = ((Map.Entry)value).getKey();
/*  746 */         valueContext.setKey(mapKey);
/*  747 */         value = ((Map.Entry)value).getValue();
/*      */       }
/*  749 */       else if (isIndexable) {
/*  750 */         valueContext.setIndex(Integer.valueOf(i));
/*      */       }
/*      */       
/*  753 */       if (!context.isBeanAlreadyValidated(value, valueContext
/*      */       
/*  755 */         .getCurrentGroup(), valueContext
/*  756 */         .getPropertyPath()))
/*      */       {
/*      */ 
/*  759 */         validateTypeArgumentConstraints(context, buildNewLocalExecutionContext(valueContext, value), value, typeArgumentsConstraint);
/*      */         
/*      */ 
/*  762 */         validateInContext(buildNewLocalExecutionContext(valueContext, value), context, validationOrder);
/*  763 */         if (shouldFailFast(context)) {
/*  764 */           return;
/*      */         }
/*      */       }
/*  767 */       i++;
/*      */     }
/*      */   }
/*      */   
/*      */   private ValueContext<?, Object> buildNewLocalExecutionContext(ValueContext<?, Object> valueContext, Object value) { ValueContext<?, Object> newValueContext;
/*      */     ValueContext<?, Object> newValueContext;
/*  773 */     if (value != null) {
/*  774 */       newValueContext = ValueContext.getLocalExecutionContext(value, this.beanMetaDataManager
/*      */       
/*  776 */         .getBeanMetaData(value.getClass()), valueContext
/*  777 */         .getPropertyPath());
/*      */     }
/*      */     else
/*      */     {
/*  781 */       newValueContext = ValueContext.getLocalExecutionContext(valueContext
/*  782 */         .getCurrentBeanType(), this.beanMetaDataManager
/*  783 */         .getBeanMetaData(valueContext.getCurrentBeanType()), valueContext
/*  784 */         .getPropertyPath());
/*      */     }
/*      */     
/*  787 */     return newValueContext;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void validateTypeArgumentConstraints(ValidationContext<?> context, ValueContext<?, Object> valueContext, Object value, Set<MetaConstraint<?>> typeArgumentsConstraints)
/*      */   {
/*  794 */     valueContext.setCurrentValidatedValue(value);
/*  795 */     valueContext.appendCollectionElementNode();
/*  796 */     for (MetaConstraint<?> metaConstraint : typeArgumentsConstraints) {
/*  797 */       metaConstraint.validateConstraint(context, valueContext);
/*  798 */       if (shouldFailFast(context)) {
/*  799 */         return;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private <T> Set<ConstraintViolation<T>> validatePropertyInContext(ValidationContext<T> context, PathImpl propertyPath, ValidationOrder validationOrder) {
/*  805 */     List<MetaConstraint<?>> metaConstraints = CollectionHelper.newArrayList();
/*  806 */     List<MetaConstraint<?>> typeUseConstraints = CollectionHelper.newArrayList();
/*  807 */     ValueContext<?, Object> valueContext = collectMetaConstraintsForPathWithValue(context, propertyPath, metaConstraints, typeUseConstraints);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  814 */     if (valueContext.getCurrentBean() == null) {
/*  815 */       throw log.getUnableToReachPropertyToValidateException(context.getRootBean(), propertyPath);
/*      */     }
/*      */     
/*  818 */     if ((metaConstraints.size() == 0) && (typeUseConstraints.size() == 0)) {
/*  819 */       return context.getFailingConstraints();
/*      */     }
/*      */     
/*  822 */     assertDefaultGroupSequenceIsExpandable(valueContext, validationOrder);
/*      */     
/*      */ 
/*  825 */     Iterator<Group> groupIterator = validationOrder.getGroupIterator();
/*  826 */     while (groupIterator.hasNext()) {
/*  827 */       Group group = (Group)groupIterator.next();
/*  828 */       valueContext.setCurrentGroup(group.getDefiningClass());
/*  829 */       validatePropertyForCurrentGroup(valueContext, context, metaConstraints, typeUseConstraints);
/*  830 */       if (shouldFailFast(context)) {
/*  831 */         return context.getFailingConstraints();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  836 */     Iterator<Sequence> sequenceIterator = validationOrder.getSequenceIterator();
/*  837 */     while (sequenceIterator.hasNext()) {
/*  838 */       Sequence sequence = (Sequence)sequenceIterator.next();
/*      */       
/*  840 */       for (GroupWithInheritance groupOfGroups : sequence) {
/*  841 */         int numberOfConstraintViolations = 0;
/*      */         
/*  843 */         for (Group group : groupOfGroups) {
/*  844 */           valueContext.setCurrentGroup(group.getDefiningClass());
/*  845 */           numberOfConstraintViolations += validatePropertyForCurrentGroup(valueContext, context, metaConstraints, typeUseConstraints);
/*      */           
/*      */ 
/*  848 */           if (shouldFailFast(context)) {
/*  849 */             return context.getFailingConstraints();
/*      */           }
/*      */         }
/*  852 */         if (numberOfConstraintViolations > 0) {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  858 */     return context.getFailingConstraints();
/*      */   }
/*      */   
/*      */   private <T> void assertDefaultGroupSequenceIsExpandable(ValueContext<T, ?> valueContext, ValidationOrder validationOrder) {
/*  862 */     BeanMetaData<T> beanMetaData = this.beanMetaDataManager.getBeanMetaData(valueContext.getCurrentBeanType());
/*  863 */     if (beanMetaData.defaultGroupSequenceIsRedefined()) {
/*  864 */       validationOrder.assertDefaultGroupSequenceIsExpandable(beanMetaData.getDefaultGroupSequence(valueContext.getCurrentBean()));
/*      */     }
/*      */   }
/*      */   
/*      */   private <T> Set<ConstraintViolation<T>> validateValueInContext(ValidationContext<T> context, Object value, PathImpl propertyPath, ValidationOrder validationOrder) {
/*  869 */     List<MetaConstraint<?>> metaConstraints = CollectionHelper.newArrayList();
/*  870 */     List<MetaConstraint<?>> typeArgumentConstraints = CollectionHelper.newArrayList();
/*  871 */     ValueContext<?, Object> valueContext = collectMetaConstraintsForPathWithoutValue(context, propertyPath, metaConstraints, typeArgumentConstraints);
/*      */     
/*      */ 
/*  874 */     valueContext.setCurrentValidatedValue(value);
/*      */     
/*  876 */     if ((metaConstraints.size() == 0) && (typeArgumentConstraints.size() == 0)) {
/*  877 */       return context.getFailingConstraints();
/*      */     }
/*      */     
/*  880 */     BeanMetaData<?> beanMetaData = this.beanMetaDataManager.getBeanMetaData(valueContext.getCurrentBeanType());
/*  881 */     if (beanMetaData.defaultGroupSequenceIsRedefined()) {
/*  882 */       validationOrder.assertDefaultGroupSequenceIsExpandable(beanMetaData.getDefaultGroupSequence(null));
/*      */     }
/*      */     
/*      */ 
/*  886 */     Iterator<Group> groupIterator = validationOrder.getGroupIterator();
/*  887 */     while (groupIterator.hasNext()) {
/*  888 */       Group group = (Group)groupIterator.next();
/*  889 */       valueContext.setCurrentGroup(group.getDefiningClass());
/*  890 */       validatePropertyForCurrentGroup(valueContext, context, metaConstraints, typeArgumentConstraints);
/*  891 */       if (shouldFailFast(context)) {
/*  892 */         return context.getFailingConstraints();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  897 */     Iterator<Sequence> sequenceIterator = validationOrder.getSequenceIterator();
/*  898 */     while (sequenceIterator.hasNext()) {
/*  899 */       Sequence sequence = (Sequence)sequenceIterator.next();
/*  900 */       for (GroupWithInheritance groupOfGroups : sequence) {
/*  901 */         int numberOfConstraintViolations = 0;
/*  902 */         for (Group group : groupOfGroups) {
/*  903 */           valueContext.setCurrentGroup(group.getDefiningClass());
/*  904 */           numberOfConstraintViolations += validatePropertyForCurrentGroup(valueContext, context, metaConstraints, typeArgumentConstraints);
/*      */           
/*      */ 
/*  907 */           if (shouldFailFast(context)) {
/*  908 */             return context.getFailingConstraints();
/*      */           }
/*      */         }
/*  911 */         if (numberOfConstraintViolations > 0) {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  917 */     return context.getFailingConstraints();
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
/*      */   private int validatePropertyForCurrentGroup(ValueContext<?, Object> valueContext, ValidationContext<?> validationContext, List<MetaConstraint<?>> metaConstraints, List<MetaConstraint<?>> typeUseConstraints)
/*      */   {
/*  932 */     if (!valueContext.validatingDefault()) {
/*  933 */       return validatePropertyForNonDefaultGroup(valueContext, validationContext, metaConstraints, typeUseConstraints);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  942 */     return validatePropertyForDefaultGroup(valueContext, validationContext, metaConstraints, typeUseConstraints);
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
/*      */   private int validatePropertyForNonDefaultGroup(ValueContext<?, Object> valueContext, ValidationContext<?> validationContext, List<MetaConstraint<?>> metaConstraints, List<MetaConstraint<?>> typeArgumentConstraints)
/*      */   {
/*  960 */     int numberOfConstraintViolationsBefore = validationContext.getFailingConstraints().size();
/*  961 */     for (MetaConstraint<?> metaConstraint : metaConstraints) {
/*  962 */       validateConstraint(validationContext, valueContext, true, metaConstraint);
/*  963 */       if (shouldFailFast(validationContext)) {
/*  964 */         return 
/*  965 */           validationContext.getFailingConstraints().size() - numberOfConstraintViolationsBefore;
/*      */       }
/*      */     }
/*  968 */     for (MetaConstraint<?> metaConstraint : typeArgumentConstraints) {
/*  969 */       validatePropertyTypeConstraint(validationContext, valueContext, true, metaConstraint);
/*  970 */       if (shouldFailFast(validationContext)) {
/*  971 */         return 
/*  972 */           validationContext.getFailingConstraints().size() - numberOfConstraintViolationsBefore;
/*      */       }
/*      */     }
/*  975 */     return validationContext.getFailingConstraints().size() - numberOfConstraintViolationsBefore;
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
/*      */   private <U> int validatePropertyForDefaultGroup(ValueContext<U, Object> valueContext, ValidationContext<?> validationContext, List<MetaConstraint<?>> constraintList, List<MetaConstraint<?>> typeUseConstraints)
/*      */   {
/*  995 */     int numberOfConstraintViolationsBefore = validationContext.getFailingConstraints().size();
/*  996 */     BeanMetaData<U> beanMetaData = this.beanMetaDataManager.getBeanMetaData(valueContext.getCurrentBeanType());
/*  997 */     Map<Class<?>, Class<?>> validatedInterfaces = CollectionHelper.newHashMap();
/*      */     
/*      */ 
/* 1000 */     for (Class<? super U> clazz : beanMetaData.getClassHierarchy()) {
/* 1001 */       BeanMetaData<? super U> hostingBeanMetaData = this.beanMetaDataManager.getBeanMetaData(clazz);
/* 1002 */       boolean defaultGroupSequenceIsRedefined = hostingBeanMetaData.defaultGroupSequenceIsRedefined();
/*      */       
/* 1004 */       if (defaultGroupSequenceIsRedefined) {
/* 1005 */         Iterator<Sequence> defaultGroupSequence = hostingBeanMetaData.getDefaultValidationSequence(valueContext.getCurrentBean());
/* 1006 */         Set<MetaConstraint<?>> metaConstraints = hostingBeanMetaData.getMetaConstraints();
/*      */         
/* 1008 */         while (defaultGroupSequence.hasNext()) {
/* 1009 */           for (GroupWithInheritance groupOfGroups : (Sequence)defaultGroupSequence.next()) {
/* 1010 */             boolean validationSuccessful = true;
/*      */             
/* 1012 */             for (Group groupClass : groupOfGroups) {
/* 1013 */               validationSuccessful = validatePropertyForSingleDefaultGroupElement(valueContext, validationContext, constraintList, typeUseConstraints, validatedInterfaces, clazz, metaConstraints, groupClass);
/*      */             }
/*      */             
/*      */ 
/* 1017 */             if (!validationSuccessful) {
/*      */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1025 */         Set<MetaConstraint<?>> metaConstraints = hostingBeanMetaData.getDirectMetaConstraints();
/*      */         
/* 1027 */         validatePropertyForSingleDefaultGroupElement(valueContext, validationContext, constraintList, typeUseConstraints, validatedInterfaces, clazz, metaConstraints, Group.DEFAULT_GROUP);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1032 */       if (defaultGroupSequenceIsRedefined) {
/*      */         break;
/*      */       }
/*      */     }
/* 1036 */     return validationContext.getFailingConstraints().size() - numberOfConstraintViolationsBefore;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private <U> boolean validatePropertyForSingleDefaultGroupElement(ValueContext<U, Object> valueContext, ValidationContext<?> validationContext, List<MetaConstraint<?>> constraintList, List<MetaConstraint<?>> typeUseConstraints, Map<Class<?>, Class<?>> validatedInterfaces, Class<? super U> clazz, Set<MetaConstraint<?>> metaConstraints, Group groupClass)
/*      */   {
/* 1043 */     valueContext.setCurrentGroup(groupClass.getDefiningClass());
/* 1044 */     boolean validationSuccessful = true;
/*      */     
/* 1046 */     for (MetaConstraint<?> metaConstraint : metaConstraints)
/*      */     {
/*      */ 
/* 1049 */       Class<?> declaringClass = metaConstraint.getLocation().getDeclaringClass();
/* 1050 */       if (declaringClass.isInterface()) {
/* 1051 */         Class<?> validatedForClass = (Class)validatedInterfaces.get(declaringClass);
/* 1052 */         if ((validatedForClass == null) || (validatedForClass.equals(clazz)))
/*      */         {
/*      */ 
/* 1055 */           validatedInterfaces.put(declaringClass, clazz);
/*      */         }
/*      */       } else {
/* 1058 */         if (constraintList.contains(metaConstraint)) {
/* 1059 */           boolean tmp = validateConstraint(validationContext, valueContext, true, metaConstraint);
/*      */           
/* 1061 */           validationSuccessful = (validationSuccessful) && (tmp);
/* 1062 */           if (shouldFailFast(validationContext)) {
/* 1063 */             return false;
/*      */           }
/*      */         }
/*      */         
/* 1067 */         if (typeUseConstraints.contains(metaConstraint)) {
/* 1068 */           boolean tmp = validatePropertyTypeConstraint(validationContext, valueContext, true, metaConstraint);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1074 */           validationSuccessful = (validationSuccessful) && (tmp);
/* 1075 */           if (shouldFailFast(validationContext)) {
/* 1076 */             return false;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1081 */     return validationSuccessful;
/*      */   }
/*      */   
/*      */ 
/*      */   private <T> void validateParametersInContext(ValidationContext<T> validationContext, Object[] parameterValues, ValidationOrder validationOrder)
/*      */   {
/* 1087 */     BeanMetaData<T> beanMetaData = this.beanMetaDataManager.getBeanMetaData(validationContext.getRootBeanClass());
/* 1088 */     ExecutableMetaData executableMetaData = beanMetaData.getMetaDataFor(validationContext.getExecutable());
/*      */     
/* 1090 */     if (executableMetaData == null)
/*      */     {
/* 1092 */       throw log.getMethodOrConstructorNotDefinedByValidatedTypeException(beanMetaData
/* 1093 */         .getBeanClass().getName(), validationContext
/* 1094 */         .getExecutable().getMember());
/*      */     }
/*      */     
/*      */ 
/* 1098 */     if (beanMetaData.defaultGroupSequenceIsRedefined()) {
/* 1099 */       validationOrder.assertDefaultGroupSequenceIsExpandable(beanMetaData
/* 1100 */         .getDefaultGroupSequence(validationContext
/* 1101 */         .getRootBean()));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1107 */     Iterator<Group> groupIterator = validationOrder.getGroupIterator();
/* 1108 */     while (groupIterator.hasNext()) {
/* 1109 */       validateParametersForGroup(validationContext, parameterValues, (Group)groupIterator.next());
/* 1110 */       if (shouldFailFast(validationContext)) {
/* 1111 */         return;
/*      */       }
/*      */     }
/*      */     
/* 1115 */     ValueContext<Object[], Object> cascadingValueContext = ValueContext.getLocalExecutionContext(parameterValues, executableMetaData
/*      */     
/* 1117 */       .getValidatableParametersMetaData(), 
/* 1118 */       PathImpl.createPathForExecutable(executableMetaData));
/*      */     
/* 1120 */     cascadingValueContext.setUnwrapMode(executableMetaData.unwrapMode());
/*      */     
/* 1122 */     groupIterator = validationOrder.getGroupIterator();
/* 1123 */     while (groupIterator.hasNext()) {
/* 1124 */       Group group = (Group)groupIterator.next();
/* 1125 */       cascadingValueContext.setCurrentGroup(group.getDefiningClass());
/* 1126 */       validateCascadedConstraints(validationContext, cascadingValueContext);
/* 1127 */       if (shouldFailFast(validationContext)) {
/* 1128 */         return;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1133 */     Iterator<Sequence> sequenceIterator = validationOrder.getSequenceIterator();
/* 1134 */     while (sequenceIterator.hasNext()) {
/* 1135 */       Sequence sequence = (Sequence)sequenceIterator.next();
/* 1136 */       for (GroupWithInheritance groupOfGroups : sequence) {
/* 1137 */         int numberOfViolations = validationContext.getFailingConstraints().size();
/*      */         
/* 1139 */         for (Group group : groupOfGroups) {
/* 1140 */           validateParametersForGroup(validationContext, parameterValues, group);
/* 1141 */           if (shouldFailFast(validationContext)) {
/* 1142 */             return;
/*      */           }
/*      */           
/* 1145 */           cascadingValueContext.setCurrentGroup(group.getDefiningClass());
/* 1146 */           validateCascadedConstraints(validationContext, cascadingValueContext);
/*      */           
/* 1148 */           if (shouldFailFast(validationContext)) {
/* 1149 */             return;
/*      */           }
/*      */         }
/*      */         
/* 1153 */         if (validationContext.getFailingConstraints().size() > numberOfViolations) {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private <T> int validateParametersForGroup(ValidationContext<T> validationContext, Object[] parameterValues, Group group) {
/* 1161 */     int numberOfViolationsBefore = validationContext.getFailingConstraints().size();
/*      */     
/* 1163 */     BeanMetaData<T> beanMetaData = this.beanMetaDataManager.getBeanMetaData(validationContext.getRootBeanClass());
/* 1164 */     ExecutableMetaData executableMetaData = beanMetaData.getMetaDataFor(validationContext.getExecutable());
/*      */     
/* 1166 */     if (parameterValues.length != executableMetaData.getParameterTypes().length) {
/* 1167 */       throw log.getInvalidParameterCountForExecutableException(
/* 1168 */         ExecutableElement.getExecutableAsString(executableMetaData
/* 1169 */         .getType().toString() + "#" + executableMetaData.getName(), executableMetaData
/* 1170 */         .getParameterTypes()), parameterValues.length, executableMetaData
/* 1171 */         .getParameterTypes().length);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1180 */     if (group.isDefaultGroup()) {
/* 1181 */       Iterator<Sequence> defaultGroupSequence = beanMetaData.getDefaultValidationSequence(validationContext.getRootBean());
/*      */       
/* 1183 */       while (defaultGroupSequence.hasNext()) {
/* 1184 */         Sequence sequence = (Sequence)defaultGroupSequence.next();
/* 1185 */         for (GroupWithInheritance expandedGroup : sequence) {
/* 1186 */           int numberOfViolationsOfCurrentGroup = 0;
/*      */           
/* 1188 */           for (Group defaultGroupSequenceElement : expandedGroup) {
/* 1189 */             numberOfViolationsOfCurrentGroup += validateParametersForSingleGroup(validationContext, parameterValues, executableMetaData, defaultGroupSequenceElement.getDefiningClass());
/*      */             
/* 1191 */             if (shouldFailFast(validationContext)) {
/* 1192 */               return validationContext.getFailingConstraints().size() - numberOfViolationsBefore;
/*      */             }
/*      */           }
/*      */           
/* 1196 */           if (numberOfViolationsOfCurrentGroup > 0) {
/* 1197 */             return validationContext.getFailingConstraints().size() - numberOfViolationsBefore;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     else {
/* 1203 */       validateParametersForSingleGroup(validationContext, parameterValues, executableMetaData, group.getDefiningClass());
/*      */     }
/*      */     
/* 1206 */     return validationContext.getFailingConstraints().size() - numberOfViolationsBefore;
/*      */   }
/*      */   
/*      */   private <T> int validateParametersForSingleGroup(ValidationContext<T> validationContext, Object[] parameterValues, ExecutableMetaData executableMetaData, Class<?> currentValidatedGroup) {
/* 1210 */     int numberOfViolationsBefore = validationContext.getFailingConstraints().size();
/*      */     
/* 1212 */     ValueContext<T, Object> valueContext = getExecutableValueContext(validationContext
/* 1213 */       .getRootBean(), executableMetaData, currentValidatedGroup);
/*      */     
/* 1215 */     valueContext.appendCrossParameterNode();
/* 1216 */     valueContext.setCurrentValidatedValue(parameterValues);
/*      */     
/*      */ 
/* 1219 */     validateConstraintsForGroup(validationContext, valueContext, executableMetaData
/* 1220 */       .getCrossParameterConstraints());
/*      */     
/* 1222 */     if (shouldFailFast(validationContext)) {
/* 1223 */       return validationContext.getFailingConstraints().size() - numberOfViolationsBefore;
/*      */     }
/*      */     
/* 1226 */     valueContext = getExecutableValueContext(validationContext
/* 1227 */       .getRootBean(), executableMetaData, currentValidatedGroup);
/*      */     
/* 1229 */     valueContext.setCurrentValidatedValue(parameterValues);
/*      */     
/*      */ 
/* 1232 */     for (int i = 0; i < parameterValues.length; i++) {
/* 1233 */       PathImpl originalPath = valueContext.getPropertyPath();
/*      */       
/* 1235 */       ParameterMetaData parameterMetaData = executableMetaData.getParameterMetaData(i);
/* 1236 */       Object value = parameterValues[i];
/*      */       
/* 1238 */       if (value != null) {
/* 1239 */         Class<?> valueType = value.getClass();
/* 1240 */         if (((parameterMetaData.getType() instanceof Class)) && (((Class)parameterMetaData.getType()).isPrimitive())) {
/* 1241 */           valueType = ReflectionHelper.unBoxedType(valueType);
/*      */         }
/* 1243 */         if (!TypeHelper.isAssignable(
/* 1244 */           TypeHelper.getErasedType(parameterMetaData.getType()), valueType))
/*      */         {
/*      */ 
/* 1247 */           throw log.getParameterTypesDoNotMatchException(valueType
/* 1248 */             .getName(), parameterMetaData
/* 1249 */             .getType().toString(), i, validationContext
/*      */             
/* 1251 */             .getExecutable().getMember());
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1256 */       valueContext.appendNode(parameterMetaData);
/* 1257 */       valueContext.setUnwrapMode(parameterMetaData.unwrapMode());
/* 1258 */       valueContext.setCurrentValidatedValue(value);
/*      */       
/* 1260 */       validateConstraintsForGroup(validationContext, valueContext, parameterMetaData);
/*      */       
/*      */ 
/* 1263 */       if (shouldFailFast(validationContext)) {
/* 1264 */         return validationContext.getFailingConstraints().size() - numberOfViolationsBefore;
/*      */       }
/*      */       
/* 1267 */       if (!parameterMetaData.isCascading()) {
/* 1268 */         validateConstraintsForGroup(validationContext, valueContext, parameterMetaData
/* 1269 */           .getTypeArgumentsConstraints());
/*      */         
/* 1271 */         if (shouldFailFast(validationContext)) {
/* 1272 */           return validationContext.getFailingConstraints().size() - numberOfViolationsBefore;
/*      */         }
/*      */       }
/*      */       
/* 1276 */       valueContext.setPropertyPath(originalPath);
/*      */     }
/*      */     
/* 1279 */     return validationContext.getFailingConstraints().size() - numberOfViolationsBefore;
/*      */   }
/*      */   
/*      */   private <T> ValueContext<T, Object> getExecutableValueContext(T object, ExecutableMetaData executableMetaData, Class<?> group) {
/*      */     ValueContext<T, Object> valueContext;
/*      */     ValueContext<T, Object> valueContext;
/* 1285 */     if (object != null) {
/* 1286 */       valueContext = ValueContext.getLocalExecutionContext(object, null, 
/*      */       
/*      */ 
/* 1289 */         PathImpl.createPathForExecutable(executableMetaData));
/*      */     }
/*      */     else
/*      */     {
/* 1293 */       valueContext = ValueContext.getLocalExecutionContext((Class)null, null, 
/*      */       
/*      */ 
/* 1296 */         PathImpl.createPathForExecutable(executableMetaData));
/*      */     }
/*      */     
/*      */ 
/* 1300 */     valueContext.setCurrentGroup(group);
/*      */     
/* 1302 */     return valueContext;
/*      */   }
/*      */   
/*      */   private <V, T> void validateReturnValueInContext(ValidationContext<T> context, T bean, V value, ValidationOrder validationOrder) {
/* 1306 */     BeanMetaData<T> beanMetaData = this.beanMetaDataManager.getBeanMetaData(context.getRootBeanClass());
/* 1307 */     ExecutableMetaData executableMetaData = beanMetaData.getMetaDataFor(context.getExecutable());
/*      */     
/* 1309 */     if (executableMetaData == null) {
/* 1310 */       return;
/*      */     }
/*      */     
/* 1313 */     if (beanMetaData.defaultGroupSequenceIsRedefined()) {
/* 1314 */       validationOrder.assertDefaultGroupSequenceIsExpandable(beanMetaData.getDefaultGroupSequence(bean));
/*      */     }
/*      */     
/* 1317 */     Iterator<Group> groupIterator = validationOrder.getGroupIterator();
/*      */     
/*      */ 
/* 1320 */     while (groupIterator.hasNext()) {
/* 1321 */       validateReturnValueForGroup(context, bean, value, (Group)groupIterator.next());
/* 1322 */       if (shouldFailFast(context)) {
/* 1323 */         return;
/*      */       }
/*      */     }
/*      */     
/* 1327 */     ValueContext<V, Object> cascadingValueContext = null;
/*      */     
/* 1329 */     if (value != null) {
/* 1330 */       cascadingValueContext = ValueContext.getLocalExecutionContext(value, executableMetaData
/*      */       
/* 1332 */         .getReturnValueMetaData(), 
/* 1333 */         PathImpl.createPathForExecutable(executableMetaData));
/*      */       
/*      */ 
/* 1336 */       groupIterator = validationOrder.getGroupIterator();
/* 1337 */       while (groupIterator.hasNext()) {
/* 1338 */         Group group = (Group)groupIterator.next();
/* 1339 */         cascadingValueContext.setCurrentGroup(group.getDefiningClass());
/* 1340 */         validateCascadedConstraints(context, cascadingValueContext);
/* 1341 */         if (shouldFailFast(context)) {
/* 1342 */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1348 */     Iterator<Sequence> sequenceIterator = validationOrder.getSequenceIterator();
/* 1349 */     while (sequenceIterator.hasNext()) {
/* 1350 */       Sequence sequence = (Sequence)sequenceIterator.next();
/* 1351 */       for (GroupWithInheritance groupOfGroups : sequence) {
/* 1352 */         int numberOfFailingConstraintsBeforeGroup = context.getFailingConstraints().size();
/* 1353 */         for (Group group : groupOfGroups) {
/* 1354 */           validateReturnValueForGroup(context, bean, value, group);
/* 1355 */           if (shouldFailFast(context)) {
/* 1356 */             return;
/*      */           }
/*      */           
/* 1359 */           if (value != null) {
/* 1360 */             cascadingValueContext.setCurrentGroup(group.getDefiningClass());
/* 1361 */             validateCascadedConstraints(context, cascadingValueContext);
/*      */             
/* 1363 */             if (shouldFailFast(context)) {
/* 1364 */               return;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 1369 */         if (context.getFailingConstraints().size() > numberOfFailingConstraintsBeforeGroup) {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private <T> int validateReturnValueForGroup(ValidationContext<T> validationContext, T bean, Object value, Group group)
/*      */   {
/* 1378 */     int numberOfViolationsBefore = validationContext.getFailingConstraints().size();
/*      */     
/* 1380 */     BeanMetaData<T> beanMetaData = this.beanMetaDataManager.getBeanMetaData(validationContext.getRootBeanClass());
/* 1381 */     ExecutableMetaData executableMetaData = beanMetaData.getMetaDataFor(validationContext.getExecutable());
/*      */     
/* 1383 */     if (executableMetaData == null)
/*      */     {
/* 1385 */       return 0;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1394 */     if (group.isDefaultGroup()) {
/* 1395 */       Iterator<Sequence> defaultGroupSequence = beanMetaData.getDefaultValidationSequence(bean);
/*      */       
/* 1397 */       while (defaultGroupSequence.hasNext()) {
/* 1398 */         Sequence sequence = (Sequence)defaultGroupSequence.next();
/* 1399 */         for (GroupWithInheritance expandedGroup : sequence) {
/* 1400 */           int numberOfViolationsOfCurrentGroup = 0;
/*      */           
/* 1402 */           for (Group defaultGroupSequenceElement : expandedGroup) {
/* 1403 */             numberOfViolationsOfCurrentGroup += validateReturnValueForSingleGroup(validationContext, executableMetaData, bean, value, defaultGroupSequenceElement.getDefiningClass());
/*      */             
/* 1405 */             if (shouldFailFast(validationContext)) {
/* 1406 */               return validationContext.getFailingConstraints().size() - numberOfViolationsBefore;
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 1411 */           if (numberOfViolationsOfCurrentGroup > 0) {
/* 1412 */             return validationContext.getFailingConstraints().size() - numberOfViolationsBefore;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     else {
/* 1418 */       validateReturnValueForSingleGroup(validationContext, executableMetaData, bean, value, group.getDefiningClass());
/*      */     }
/*      */     
/* 1421 */     return validationContext.getFailingConstraints().size() - numberOfViolationsBefore;
/*      */   }
/*      */   
/*      */   private <T> int validateReturnValueForSingleGroup(ValidationContext<T> validationContext, ExecutableMetaData executableMetaData, T bean, Object value, Class<?> oneGroup) {
/* 1425 */     int numberOfViolationsBefore = validationContext.getFailingConstraints().size();
/*      */     
/*      */ 
/* 1428 */     ValueContext<?, Object> valueContext = getExecutableValueContext(executableMetaData
/* 1429 */       .getKind() == ElementKind.CONSTRUCTOR ? value : bean, executableMetaData, oneGroup);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1434 */     valueContext.setCurrentValidatedValue(value);
/* 1435 */     ReturnValueMetaData returnValueMetaData = executableMetaData.getReturnValueMetaData();
/* 1436 */     valueContext.appendNode(returnValueMetaData);
/* 1437 */     valueContext.setUnwrapMode(returnValueMetaData.unwrapMode());
/*      */     
/*      */ 
/* 1440 */     int numberOfViolationsOfCurrentGroup = validateConstraintsForGroup(validationContext, valueContext, returnValueMetaData);
/*      */     
/*      */ 
/* 1443 */     if (shouldFailFast(validationContext)) {
/* 1444 */       return validationContext.getFailingConstraints().size() - numberOfViolationsBefore;
/*      */     }
/*      */     
/* 1447 */     if (!returnValueMetaData.isCascading()) {
/* 1448 */       numberOfViolationsOfCurrentGroup += validateConstraintsForGroup(validationContext, valueContext, returnValueMetaData
/* 1449 */         .getTypeArgumentsConstraints());
/*      */       
/* 1451 */       if (shouldFailFast(validationContext)) {
/* 1452 */         return validationContext.getFailingConstraints().size() - numberOfViolationsBefore;
/*      */       }
/*      */     }
/*      */     
/* 1456 */     return numberOfViolationsOfCurrentGroup;
/*      */   }
/*      */   
/*      */ 
/*      */   private int validateConstraintsForGroup(ValidationContext<?> validationContext, ValueContext<?, ?> valueContext, Iterable<MetaConstraint<?>> constraints)
/*      */   {
/* 1462 */     int numberOfViolationsBefore = validationContext.getFailingConstraints().size();
/*      */     
/* 1464 */     for (MetaConstraint<?> metaConstraint : constraints) {
/* 1465 */       if (isValidationRequired(validationContext, valueContext, metaConstraint))
/*      */       {
/*      */ 
/*      */ 
/* 1469 */         metaConstraint.validateConstraint(validationContext, valueContext);
/* 1470 */         if (shouldFailFast(validationContext)) {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/* 1475 */     return validationContext.getFailingConstraints().size() - numberOfViolationsBefore;
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
/*      */   private <V> ValueContext<?, V> collectMetaConstraintsForPathWithValue(ValidationContext<?> validationContext, PathImpl propertyPath, List<MetaConstraint<?>> metaConstraints, List<MetaConstraint<?>> typeArgumentConstraints)
/*      */   {
/* 1492 */     Class<?> clazz = validationContext.getRootBeanClass();
/* 1493 */     Object value = validationContext.getRootBean();
/* 1494 */     PropertyMetaData propertyMetaData = null;
/*      */     
/* 1496 */     Iterator<Path.Node> propertyPathIter = propertyPath.iterator();
/*      */     
/* 1498 */     while (propertyPathIter.hasNext())
/*      */     {
/* 1500 */       NodeImpl propertyPathNode = (NodeImpl)propertyPathIter.next();
/* 1501 */       propertyMetaData = getBeanPropertyMetaData(clazz, propertyPathNode);
/*      */       
/*      */ 
/* 1504 */       if (propertyPathIter.hasNext()) {
/* 1505 */         if (!propertyMetaData.isCascading()) {
/* 1506 */           throw log.getInvalidPropertyPathException(validationContext.getRootBeanClass().getName(), propertyPath.asString());
/*      */         }
/*      */         
/* 1509 */         value = getBeanPropertyValue(validationContext, value, propertyMetaData);
/* 1510 */         if (value == null) {
/* 1511 */           throw log.getUnableToReachPropertyToValidateException(validationContext.getRootBean(), propertyPath);
/*      */         }
/* 1513 */         clazz = value.getClass();
/*      */         
/*      */ 
/*      */ 
/* 1517 */         if (propertyPathNode.isIterable()) {
/* 1518 */           propertyPathNode = (NodeImpl)propertyPathIter.next();
/*      */           
/* 1520 */           if (propertyPathNode.getIndex() != null) {
/* 1521 */             value = ReflectionHelper.getIndexedValue(value, propertyPathNode.getIndex());
/*      */           }
/* 1523 */           else if (propertyPathNode.getKey() != null) {
/* 1524 */             value = ReflectionHelper.getMappedValue(value, propertyPathNode.getKey());
/*      */           }
/*      */           else {
/* 1527 */             throw log.getPropertyPathMustProvideIndexOrMapKeyException();
/*      */           }
/*      */           
/* 1530 */           if (value == null) {
/* 1531 */             throw log.getUnableToReachPropertyToValidateException(validationContext.getRootBean(), propertyPath);
/*      */           }
/*      */           
/* 1534 */           clazz = value.getClass();
/* 1535 */           propertyMetaData = getBeanPropertyMetaData(clazz, propertyPathNode);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1540 */     if (propertyMetaData == null)
/*      */     {
/* 1542 */       throw log.getInvalidPropertyPathException(clazz.getName(), propertyPath.asString());
/*      */     }
/*      */     
/* 1545 */     metaConstraints.addAll(propertyMetaData.getConstraints());
/* 1546 */     typeArgumentConstraints.addAll(propertyMetaData.getTypeArgumentsConstraints());
/*      */     
/* 1548 */     return ValueContext.getLocalExecutionContext(value, null, propertyPath);
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
/*      */   private <V> ValueContext<?, V> collectMetaConstraintsForPathWithoutValue(ValidationContext<?> validationContext, PathImpl propertyPath, List<MetaConstraint<?>> metaConstraints, List<MetaConstraint<?>> typeArgumentConstraints)
/*      */   {
/* 1568 */     Class<?> clazz = validationContext.getRootBeanClass();
/* 1569 */     PropertyMetaData propertyMetaData = null;
/*      */     
/* 1571 */     Iterator<Path.Node> propertyPathIter = propertyPath.iterator();
/*      */     
/* 1573 */     while (propertyPathIter.hasNext())
/*      */     {
/* 1575 */       NodeImpl propertyPathNode = (NodeImpl)propertyPathIter.next();
/* 1576 */       propertyMetaData = getBeanPropertyMetaData(clazz, propertyPathNode);
/*      */       
/*      */ 
/* 1579 */       if (propertyPathIter.hasNext())
/*      */       {
/*      */ 
/* 1582 */         if (propertyPathNode.isIterable()) {
/* 1583 */           propertyPathNode = (NodeImpl)propertyPathIter.next();
/*      */           
/* 1585 */           clazz = ReflectionHelper.getClassFromType(ReflectionHelper.getCollectionElementType(propertyMetaData.getType()));
/* 1586 */           propertyMetaData = getBeanPropertyMetaData(clazz, propertyPathNode);
/*      */         }
/*      */         else {
/* 1589 */           clazz = ReflectionHelper.getClassFromType(propertyMetaData.getType());
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1594 */     if (propertyMetaData == null)
/*      */     {
/* 1596 */       throw log.getInvalidPropertyPathException(clazz.getName(), propertyPath.asString());
/*      */     }
/*      */     
/* 1599 */     metaConstraints.addAll(propertyMetaData.getConstraints());
/* 1600 */     typeArgumentConstraints.addAll(propertyMetaData.getTypeArgumentsConstraints());
/*      */     
/* 1602 */     return ValueContext.getLocalExecutionContext(clazz, null, propertyPath);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private TraversableResolver getCachingTraversableResolver()
/*      */   {
/* 1612 */     return new CachingTraversableResolverForSingleValidation(this.traversableResolver);
/*      */   }
/*      */   
/*      */ 
/*      */   private boolean isValidationRequired(ValidationContext<?> validationContext, ValueContext<?, ?> valueContext, MetaConstraint<?> metaConstraint)
/*      */   {
/* 1618 */     if (validationContext.hasMetaConstraintBeenProcessed(valueContext
/* 1619 */       .getCurrentBean(), valueContext
/* 1620 */       .getPropertyPath(), metaConstraint))
/*      */     {
/*      */ 
/* 1623 */       return false;
/*      */     }
/*      */     
/* 1626 */     if (!metaConstraint.getGroupList().contains(valueContext.getCurrentGroup())) {
/* 1627 */       return false;
/*      */     }
/* 1629 */     return isReachable(validationContext, valueContext
/*      */     
/* 1631 */       .getCurrentBean(), valueContext
/* 1632 */       .getPropertyPath(), metaConstraint
/* 1633 */       .getElementType());
/*      */   }
/*      */   
/*      */   private boolean isReachable(ValidationContext<?> validationContext, Object traversableObject, PathImpl path, ElementType type)
/*      */   {
/* 1638 */     if (needToCallTraversableResolver(path, type)) {
/* 1639 */       return true;
/*      */     }
/*      */     
/* 1642 */     Path pathToObject = path.getPathWithoutLeafNode();
/*      */     try {
/* 1644 */       return validationContext.getTraversableResolver().isReachable(traversableObject, path
/*      */       
/* 1646 */         .getLeafNode(), validationContext
/* 1647 */         .getRootBeanClass(), pathToObject, type);
/*      */ 
/*      */     }
/*      */     catch (RuntimeException e)
/*      */     {
/*      */ 
/* 1653 */       throw log.getErrorDuringCallOfTraversableResolverIsReachableException(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean needToCallTraversableResolver(PathImpl path, ElementType type)
/*      */   {
/* 1662 */     return (isClassLevelConstraint(type)) || 
/* 1663 */       (isCrossParameterValidation(path)) || 
/* 1664 */       (isParameterValidation(path)) || 
/* 1665 */       (isReturnValueValidation(path));
/*      */   }
/*      */   
/*      */   private boolean isCascadeRequired(ValidationContext<?> validationContext, Object traversableObject, PathImpl path, ElementType type) {
/* 1669 */     if (needToCallTraversableResolver(path, type)) {
/* 1670 */       return true;
/*      */     }
/*      */     
/* 1673 */     boolean isReachable = isReachable(validationContext, traversableObject, path, type);
/* 1674 */     if (!isReachable) {
/* 1675 */       return false;
/*      */     }
/*      */     
/* 1678 */     Path pathToObject = path.getPathWithoutLeafNode();
/*      */     try {
/* 1680 */       return validationContext.getTraversableResolver().isCascadable(traversableObject, path
/*      */       
/* 1682 */         .getLeafNode(), validationContext
/* 1683 */         .getRootBeanClass(), pathToObject, type);
/*      */ 
/*      */     }
/*      */     catch (RuntimeException e)
/*      */     {
/*      */ 
/* 1689 */       throw log.getErrorDuringCallOfTraversableResolverIsCascadableException(e);
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean isClassLevelConstraint(ElementType type) {
/* 1694 */     return ElementType.TYPE.equals(type);
/*      */   }
/*      */   
/*      */   private boolean isCrossParameterValidation(PathImpl path) {
/* 1698 */     return path.getLeafNode().getKind() == ElementKind.CROSS_PARAMETER;
/*      */   }
/*      */   
/*      */   private boolean isParameterValidation(PathImpl path) {
/* 1702 */     return path.getLeafNode().getKind() == ElementKind.PARAMETER;
/*      */   }
/*      */   
/*      */   private boolean isReturnValueValidation(PathImpl path) {
/* 1706 */     return path.getLeafNode().getKind() == ElementKind.RETURN_VALUE;
/*      */   }
/*      */   
/*      */   private boolean shouldFailFast(ValidationContext<?> context) {
/* 1710 */     return (context.isFailFastModeEnabled()) && (!context.getFailingConstraints().isEmpty());
/*      */   }
/*      */   
/*      */   private PropertyMetaData getBeanPropertyMetaData(Class<?> beanClass, Path.Node propertyNode) {
/* 1714 */     if (!ElementKind.PROPERTY.equals(propertyNode.getKind())) {
/* 1715 */       throw log.getInvalidPropertyPathException(beanClass.getName(), propertyNode.getName());
/*      */     }
/*      */     
/* 1718 */     BeanMetaData<?> beanMetaData = this.beanMetaDataManager.getBeanMetaData(beanClass);
/* 1719 */     PropertyMetaData propertyMetaData = beanMetaData.getMetaDataFor(propertyNode.getName());
/*      */     
/* 1721 */     if (propertyMetaData == null) {
/* 1722 */       throw log.getInvalidPropertyPathException(beanClass.getName(), propertyNode.getName());
/*      */     }
/* 1724 */     return propertyMetaData;
/*      */   }
/*      */   
/*      */   private Object getBeanPropertyValue(ValidationContext<?> validationContext, Object object, Cascadable cascadable)
/*      */   {
/* 1729 */     Object value = cascadable.getValue(object);
/*      */     
/*      */ 
/* 1732 */     UnwrapMode unwrapMode = cascadable.unwrapMode();
/* 1733 */     if ((UnwrapMode.UNWRAP.equals(unwrapMode)) || (UnwrapMode.AUTOMATIC.equals(unwrapMode))) {
/* 1734 */       ValidatedValueUnwrapper valueHandler = validationContext.getValidatedValueUnwrapper(cascadable.getCascadableType());
/* 1735 */       if (valueHandler != null) {
/* 1736 */         value = valueHandler.handleValidatedValue(value);
/*      */       }
/*      */     }
/*      */     
/* 1740 */     return value;
/*      */   }
/*      */   
/*      */   private Object getBeanMemberValue(Object object, Member member) {
/* 1744 */     if (member == null) {
/* 1745 */       return object;
/*      */     }
/*      */     
/* 1748 */     member = getAccessible(member);
/*      */     
/* 1750 */     if ((member instanceof Method)) {
/* 1751 */       return ReflectionHelper.getValue((Method)member, object);
/*      */     }
/* 1753 */     if ((member instanceof Field)) {
/* 1754 */       return ReflectionHelper.getValue((Field)member, object);
/*      */     }
/* 1756 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Member getAccessible(Member original)
/*      */   {
/* 1765 */     if (((AccessibleObject)original).isAccessible()) {
/* 1766 */       return original;
/*      */     }
/*      */     
/* 1769 */     Member member = (Member)this.accessibleMembers.get(original);
/*      */     
/* 1771 */     if (member != null) {
/* 1772 */       return member;
/*      */     }
/*      */     
/* 1775 */     SecurityManager sm = System.getSecurityManager();
/* 1776 */     if (sm != null) {
/* 1777 */       sm.checkPermission(HibernateValidatorPermission.ACCESS_PRIVATE_MEMBERS);
/*      */     }
/*      */     
/* 1780 */     Class<?> clazz = original.getDeclaringClass();
/*      */     
/* 1782 */     if ((original instanceof Field)) {
/* 1783 */       member = (Member)run(GetDeclaredField.action(clazz, original.getName()));
/*      */     }
/*      */     else {
/* 1786 */       member = (Member)run(GetDeclaredMethod.action(clazz, original.getName(), new Class[0]));
/*      */     }
/*      */     
/* 1789 */     run(SetAccessibility.action(member));
/*      */     
/* 1791 */     Member cached = (Member)this.accessibleMembers.putIfAbsent(original, member);
/* 1792 */     if (cached != null) {
/* 1793 */       member = cached;
/*      */     }
/*      */     
/* 1796 */     return member;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private <T> T run(PrivilegedAction<T> action)
/*      */   {
/* 1806 */     return (T)(System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run());
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\ValidatorImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */