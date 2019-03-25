/*     */ package org.hibernate.validator.internal.engine;
/*     */ 
/*     */ import com.fasterxml.classmate.ResolvedType;
/*     */ import com.fasterxml.classmate.TypeResolver;
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.validation.ConstraintValidatorFactory;
/*     */ import javax.validation.ConstraintViolation;
/*     */ import javax.validation.MessageInterpolator;
/*     */ import javax.validation.ParameterNameProvider;
/*     */ import javax.validation.Path;
/*     */ import javax.validation.Path.Node;
/*     */ import javax.validation.TraversableResolver;
/*     */ import javax.validation.ValidationException;
/*     */ import javax.validation.metadata.ConstraintDescriptor;
/*     */ import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
/*     */ import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorManager;
/*     */ import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintViolationCreationContext;
/*     */ import org.hibernate.validator.internal.engine.path.PathImpl;
/*     */ import org.hibernate.validator.internal.metadata.core.MetaConstraint;
/*     */ import org.hibernate.validator.internal.metadata.raw.ExecutableElement;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.IdentitySet;
/*     */ import org.hibernate.validator.internal.util.TypeHelper;
/*     */ import org.hibernate.validator.internal.util.TypeResolutionHelper;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*     */ import org.hibernate.validator.spi.time.TimeProvider;
/*     */ import org.hibernate.validator.spi.valuehandling.ValidatedValueUnwrapper;
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
/*     */ public class ValidationContext<T>
/*     */ {
/*  62 */   private static final Log log = ;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ConstraintValidatorManager constraintValidatorManager;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final T rootBean;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Class<T> rootBeanClass;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ExecutableElement executable;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Object[] executableParameters;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Object executableReturnValue;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Map<Class<?>, IdentitySet> processedBeansPerGroup;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Map<Object, Set<PathImpl>> processedPathsPerBean;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Map<BeanAndPath, IdentitySet> processedMetaConstraints;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Set<ConstraintViolation<T>> failingConstraintViolations;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final MessageInterpolator messageInterpolator;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ConstraintValidatorFactory constraintValidatorFactory;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final TraversableResolver traversableResolver;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ParameterNameProvider parameterNameProvider;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final List<ValidatedValueUnwrapper<?>> validatedValueUnwrappers;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final TypeResolutionHelper typeResolutionHelper;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final boolean failFast;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final TimeProvider timeProvider;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ValidationContext(ConstraintValidatorManager constraintValidatorManager, MessageInterpolator messageInterpolator, ConstraintValidatorFactory constraintValidatorFactory, TraversableResolver traversableResolver, ParameterNameProvider parameterNameProvider, TimeProvider timeProvider, List<ValidatedValueUnwrapper<?>> validatedValueUnwrappers, TypeResolutionHelper typeResolutionHelper, boolean failFast, T rootBean, Class<T> rootBeanClass, ExecutableElement executable, Object[] executableParameters, Object executableReturnValue)
/*     */   {
/* 166 */     this.constraintValidatorManager = constraintValidatorManager;
/* 167 */     this.messageInterpolator = messageInterpolator;
/* 168 */     this.constraintValidatorFactory = constraintValidatorFactory;
/* 169 */     this.traversableResolver = traversableResolver;
/* 170 */     this.parameterNameProvider = parameterNameProvider;
/* 171 */     this.timeProvider = timeProvider;
/* 172 */     this.validatedValueUnwrappers = validatedValueUnwrappers;
/* 173 */     this.typeResolutionHelper = typeResolutionHelper;
/* 174 */     this.failFast = failFast;
/*     */     
/* 176 */     this.rootBean = rootBean;
/* 177 */     this.rootBeanClass = rootBeanClass;
/* 178 */     this.executable = executable;
/* 179 */     this.executableParameters = executableParameters;
/* 180 */     this.executableReturnValue = executableReturnValue;
/*     */     
/* 182 */     this.processedBeansPerGroup = CollectionHelper.newHashMap();
/* 183 */     this.processedPathsPerBean = new IdentityHashMap();
/* 184 */     this.processedMetaConstraints = CollectionHelper.newHashMap();
/* 185 */     this.failingConstraintViolations = CollectionHelper.newHashSet();
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
/*     */   public static ValidationContextBuilder getValidationContext(ConstraintValidatorManager constraintValidatorManager, MessageInterpolator messageInterpolator, ConstraintValidatorFactory constraintValidatorFactory, TraversableResolver traversableResolver, TimeProvider timeProvider, List<ValidatedValueUnwrapper<?>> validatedValueUnwrappers, TypeResolutionHelper typeResolutionHelper, boolean failFast)
/*     */   {
/* 198 */     return new ValidationContextBuilder(constraintValidatorManager, messageInterpolator, constraintValidatorFactory, traversableResolver, timeProvider, validatedValueUnwrappers, typeResolutionHelper, failFast, null);
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
/*     */   public T getRootBean()
/*     */   {
/* 211 */     return (T)this.rootBean;
/*     */   }
/*     */   
/*     */   public Class<T> getRootBeanClass() {
/* 215 */     return this.rootBeanClass;
/*     */   }
/*     */   
/*     */   public ExecutableElement getExecutable() {
/* 219 */     return this.executable;
/*     */   }
/*     */   
/*     */   public TraversableResolver getTraversableResolver() {
/* 223 */     return this.traversableResolver;
/*     */   }
/*     */   
/*     */   public boolean isFailFastModeEnabled() {
/* 227 */     return this.failFast;
/*     */   }
/*     */   
/*     */   public ConstraintValidatorManager getConstraintValidatorManager() {
/* 231 */     return this.constraintValidatorManager;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> getParameterNames()
/*     */   {
/* 242 */     if (this.parameterNameProvider == null) {
/* 243 */       return null;
/*     */     }
/* 245 */     if (this.executable.getElementType() == ElementType.METHOD) {
/* 246 */       return this.parameterNameProvider.getParameterNames((Method)this.executable.getMember());
/*     */     }
/*     */     
/* 249 */     return this.parameterNameProvider.getParameterNames((Constructor)this.executable.getMember());
/*     */   }
/*     */   
/*     */   public TimeProvider getTimeProvider()
/*     */   {
/* 254 */     return this.timeProvider;
/*     */   }
/*     */   
/*     */   public Set<ConstraintViolation<T>> createConstraintViolations(ValueContext<?, ?> localContext, ConstraintValidatorContextImpl constraintValidatorContext)
/*     */   {
/* 259 */     Set<ConstraintViolation<T>> constraintViolations = CollectionHelper.newHashSet();
/* 260 */     for (ConstraintViolationCreationContext constraintViolationCreationContext : constraintValidatorContext.getConstraintViolationCreationContexts()) {
/* 261 */       ConstraintViolation<T> violation = createConstraintViolation(localContext, constraintViolationCreationContext, constraintValidatorContext
/*     */       
/* 263 */         .getConstraintDescriptor());
/*     */       
/* 265 */       constraintViolations.add(violation);
/*     */     }
/* 267 */     return constraintViolations;
/*     */   }
/*     */   
/*     */   public ConstraintValidatorFactory getConstraintValidatorFactory() {
/* 271 */     return this.constraintValidatorFactory;
/*     */   }
/*     */   
/*     */   public boolean isBeanAlreadyValidated(Object value, Class<?> group, PathImpl path)
/*     */   {
/* 276 */     boolean alreadyValidated = isAlreadyValidatedForCurrentGroup(value, group);
/*     */     
/* 278 */     if (alreadyValidated) {
/* 279 */       alreadyValidated = isAlreadyValidatedForPath(value, path);
/*     */     }
/* 281 */     return alreadyValidated;
/*     */   }
/*     */   
/*     */   public void markCurrentBeanAsProcessed(ValueContext<?, ?> valueContext) {
/* 285 */     markCurrentBeanAsProcessedForCurrentGroup(valueContext.getCurrentBean(), valueContext.getCurrentGroup());
/* 286 */     markCurrentBeanAsProcessedForCurrentPath(valueContext.getCurrentBean(), valueContext.getPropertyPath());
/*     */   }
/*     */   
/*     */   public void addConstraintFailures(Set<ConstraintViolation<T>> failingConstraintViolations) {
/* 290 */     this.failingConstraintViolations.addAll(failingConstraintViolations);
/*     */   }
/*     */   
/*     */   public Set<ConstraintViolation<T>> getFailingConstraints() {
/* 294 */     return this.failingConstraintViolations;
/*     */   }
/*     */   
/*     */   public ConstraintViolation<T> createConstraintViolation(ValueContext<?, ?> localContext, ConstraintViolationCreationContext constraintViolationCreationContext, ConstraintDescriptor<?> descriptor)
/*     */   {
/* 299 */     String messageTemplate = constraintViolationCreationContext.getMessage();
/* 300 */     String interpolatedMessage = interpolate(messageTemplate, localContext
/*     */     
/* 302 */       .getCurrentValidatedValue(), descriptor, constraintViolationCreationContext
/*     */       
/* 304 */       .getExpressionVariables());
/*     */     
/*     */ 
/* 307 */     Path path = PathImpl.createCopy(constraintViolationCreationContext.getPath());
/*     */     
/*     */ 
/* 310 */     Map<String, Object> expressionVariables = Collections.unmodifiableMap(constraintViolationCreationContext.getExpressionVariables());
/* 311 */     Object dynamicPayload = constraintViolationCreationContext.getDynamicPayload();
/* 312 */     if (this.executableParameters != null) {
/* 313 */       return ConstraintViolationImpl.forParameterValidation(messageTemplate, expressionVariables, interpolatedMessage, 
/*     */       
/*     */ 
/*     */ 
/* 317 */         getRootBeanClass(), 
/* 318 */         getRootBean(), localContext
/* 319 */         .getCurrentBean(), localContext
/* 320 */         .getCurrentValidatedValue(), path, descriptor, localContext
/*     */         
/*     */ 
/* 323 */         .getElementType(), this.executableParameters, dynamicPayload);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 328 */     if (this.executableReturnValue != null) {
/* 329 */       return ConstraintViolationImpl.forReturnValueValidation(messageTemplate, expressionVariables, interpolatedMessage, 
/*     */       
/*     */ 
/*     */ 
/* 333 */         getRootBeanClass(), 
/* 334 */         getRootBean(), localContext
/* 335 */         .getCurrentBean(), localContext
/* 336 */         .getCurrentValidatedValue(), path, descriptor, localContext
/*     */         
/*     */ 
/* 339 */         .getElementType(), this.executableReturnValue, dynamicPayload);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 345 */     return ConstraintViolationImpl.forBeanValidation(messageTemplate, expressionVariables, interpolatedMessage, 
/*     */     
/*     */ 
/*     */ 
/* 349 */       getRootBeanClass(), 
/* 350 */       getRootBean(), localContext
/* 351 */       .getCurrentBean(), localContext
/* 352 */       .getCurrentValidatedValue(), path, descriptor, localContext
/*     */       
/*     */ 
/* 355 */       .getElementType(), dynamicPayload);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasMetaConstraintBeenProcessed(Object bean, Path path, MetaConstraint<?> metaConstraint)
/*     */   {
/* 363 */     IdentitySet processedConstraints = (IdentitySet)this.processedMetaConstraints.get(new BeanAndPath(bean, path, null));
/* 364 */     return (processedConstraints != null) && (processedConstraints.contains(metaConstraint));
/*     */   }
/*     */   
/*     */   public void markConstraintProcessed(Object bean, Path path, MetaConstraint<?> metaConstraint)
/*     */   {
/* 369 */     BeanAndPath beanAndPath = new BeanAndPath(bean, path, null);
/* 370 */     if (this.processedMetaConstraints.containsKey(beanAndPath)) {
/* 371 */       ((IdentitySet)this.processedMetaConstraints.get(beanAndPath)).add(metaConstraint);
/*     */     }
/*     */     else {
/* 374 */       IdentitySet set = new IdentitySet();
/* 375 */       set.add(metaConstraint);
/* 376 */       this.processedMetaConstraints.put(beanAndPath, set);
/*     */     }
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
/*     */   public ValidatedValueUnwrapper<?> getValidatedValueUnwrapper(Type type)
/*     */   {
/* 391 */     TypeResolver typeResolver = this.typeResolutionHelper.getTypeResolver();
/*     */     
/* 393 */     for (ValidatedValueUnwrapper<?> handler : this.validatedValueUnwrappers) {
/* 394 */       ResolvedType handlerType = typeResolver.resolve(handler.getClass(), new Type[0]);
/* 395 */       List<ResolvedType> typeParameters = handlerType.typeParametersFor(ValidatedValueUnwrapper.class);
/*     */       
/* 397 */       if (TypeHelper.isAssignable(((ResolvedType)typeParameters.get(0)).getErasedType(), type)) {
/* 398 */         return handler;
/*     */       }
/*     */     }
/*     */     
/* 402 */     return null;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 407 */     StringBuilder sb = new StringBuilder();
/* 408 */     sb.append("ValidationContext");
/* 409 */     sb.append("{rootBean=").append(this.rootBean);
/* 410 */     sb.append('}');
/* 411 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String interpolate(String messageTemplate, Object validatedValue, ConstraintDescriptor<?> descriptor, Map<String, Object> messageParameters)
/*     */   {
/* 421 */     MessageInterpolatorContext context = new MessageInterpolatorContext(descriptor, validatedValue, getRootBeanClass(), messageParameters);
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 426 */       return this.messageInterpolator.interpolate(messageTemplate, context);
/*     */ 
/*     */     }
/*     */     catch (ValidationException ve)
/*     */     {
/*     */ 
/* 432 */       throw ve;
/*     */     }
/*     */     catch (Exception e) {
/* 435 */       throw log.getExceptionOccurredDuringMessageInterpolationException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isAlreadyValidatedForPath(Object value, PathImpl path) {
/* 440 */     Set<PathImpl> pathSet = (Set)this.processedPathsPerBean.get(value);
/* 441 */     if (pathSet == null) {
/* 442 */       return false;
/*     */     }
/*     */     
/* 445 */     for (PathImpl p : pathSet) {
/* 446 */       if ((path.isRootPath()) || (p.isRootPath()) || (isSubPathOf(path, p)) || (isSubPathOf(p, path))) {
/* 447 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 451 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isSubPathOf(Path p1, Path p2) {
/* 455 */     Iterator<Path.Node> p1Iter = p1.iterator();
/* 456 */     Iterator<Path.Node> p2Iter = p2.iterator();
/* 457 */     while (p1Iter.hasNext()) {
/* 458 */       Path.Node p1Node = (Path.Node)p1Iter.next();
/* 459 */       if (!p2Iter.hasNext()) {
/* 460 */         return false;
/*     */       }
/* 462 */       Path.Node p2Node = (Path.Node)p2Iter.next();
/* 463 */       if (!p1Node.equals(p2Node)) {
/* 464 */         return false;
/*     */       }
/*     */     }
/* 467 */     return true;
/*     */   }
/*     */   
/*     */   private boolean isAlreadyValidatedForCurrentGroup(Object value, Class<?> group) {
/* 471 */     IdentitySet objectsProcessedInCurrentGroups = (IdentitySet)this.processedBeansPerGroup.get(group);
/* 472 */     return (objectsProcessedInCurrentGroups != null) && (objectsProcessedInCurrentGroups.contains(value));
/*     */   }
/*     */   
/*     */   private void markCurrentBeanAsProcessedForCurrentPath(Object value, PathImpl path)
/*     */   {
/* 477 */     path = PathImpl.createCopy(path);
/*     */     
/* 479 */     if (this.processedPathsPerBean.containsKey(value)) {
/* 480 */       ((Set)this.processedPathsPerBean.get(value)).add(path);
/*     */     }
/*     */     else {
/* 483 */       Set<PathImpl> set = new HashSet();
/* 484 */       set.add(path);
/* 485 */       this.processedPathsPerBean.put(value, set);
/*     */     }
/*     */   }
/*     */   
/*     */   private void markCurrentBeanAsProcessedForCurrentGroup(Object value, Class<?> group) {
/* 490 */     if (this.processedBeansPerGroup.containsKey(group)) {
/* 491 */       ((IdentitySet)this.processedBeansPerGroup.get(group)).add(value);
/*     */     }
/*     */     else {
/* 494 */       IdentitySet set = new IdentitySet();
/* 495 */       set.add(value);
/* 496 */       this.processedBeansPerGroup.put(group, set);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static class ValidationContextBuilder
/*     */   {
/*     */     private final ConstraintValidatorManager constraintValidatorManager;
/*     */     
/*     */ 
/*     */     private final MessageInterpolator messageInterpolator;
/*     */     
/*     */     private final ConstraintValidatorFactory constraintValidatorFactory;
/*     */     
/*     */     private final TraversableResolver traversableResolver;
/*     */     
/*     */     private final TimeProvider timeProvider;
/*     */     
/*     */     private final List<ValidatedValueUnwrapper<?>> validatedValueUnwrappers;
/*     */     
/*     */     private final TypeResolutionHelper typeResolutionHelper;
/*     */     
/*     */     private final boolean failFast;
/*     */     
/*     */ 
/*     */     private ValidationContextBuilder(ConstraintValidatorManager constraintValidatorManager, MessageInterpolator messageInterpolator, ConstraintValidatorFactory constraintValidatorFactory, TraversableResolver traversableResolver, TimeProvider timeProvider, List<ValidatedValueUnwrapper<?>> validatedValueUnwrappers, TypeResolutionHelper typeResolutionHelper, boolean failFast)
/*     */     {
/* 524 */       this.constraintValidatorManager = constraintValidatorManager;
/* 525 */       this.messageInterpolator = messageInterpolator;
/* 526 */       this.constraintValidatorFactory = constraintValidatorFactory;
/* 527 */       this.traversableResolver = traversableResolver;
/* 528 */       this.timeProvider = timeProvider;
/* 529 */       this.validatedValueUnwrappers = validatedValueUnwrappers;
/* 530 */       this.typeResolutionHelper = typeResolutionHelper;
/* 531 */       this.failFast = failFast;
/*     */     }
/*     */     
/*     */     public <T> ValidationContext<T> forValidate(T rootBean)
/*     */     {
/* 536 */       Class<T> rootBeanClass = rootBean.getClass();
/* 537 */       return new ValidationContext(this.constraintValidatorManager, this.messageInterpolator, this.constraintValidatorFactory, this.traversableResolver, null, this.timeProvider, this.validatedValueUnwrappers, this.typeResolutionHelper, this.failFast, rootBean, rootBeanClass, null, null, null, null);
/*     */     }
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
/*     */     public <T> ValidationContext<T> forValidateProperty(T rootBean)
/*     */     {
/* 557 */       Class<T> rootBeanClass = rootBean.getClass();
/* 558 */       return new ValidationContext(this.constraintValidatorManager, this.messageInterpolator, this.constraintValidatorFactory, this.traversableResolver, null, this.timeProvider, this.validatedValueUnwrappers, this.typeResolutionHelper, this.failFast, rootBean, rootBeanClass, null, null, null, null);
/*     */     }
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
/*     */     public <T> ValidationContext<T> forValidateValue(Class<T> rootBeanClass)
/*     */     {
/* 577 */       return new ValidationContext(this.constraintValidatorManager, this.messageInterpolator, this.constraintValidatorFactory, this.traversableResolver, null, this.timeProvider, this.validatedValueUnwrappers, this.typeResolutionHelper, this.failFast, null, rootBeanClass, null, null, null, null);
/*     */     }
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
/*     */     public <T> ValidationContext<T> forValidateParameters(ParameterNameProvider parameterNameProvider, T rootBean, ExecutableElement executable, Object[] executableParameters)
/*     */     {
/* 602 */       Class<T> rootBeanClass = rootBean != null ? rootBean.getClass() : executable.getMember().getDeclaringClass();
/* 603 */       return new ValidationContext(this.constraintValidatorManager, this.messageInterpolator, this.constraintValidatorFactory, this.traversableResolver, parameterNameProvider, this.timeProvider, this.validatedValueUnwrappers, this.typeResolutionHelper, this.failFast, rootBean, rootBeanClass, executable, executableParameters, null, null);
/*     */     }
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
/*     */     public <T> ValidationContext<T> forValidateReturnValue(T rootBean, ExecutableElement executable, Object executableReturnValue)
/*     */     {
/* 627 */       Class<T> rootBeanClass = rootBean != null ? rootBean.getClass() : executable.getMember().getDeclaringClass();
/* 628 */       return new ValidationContext(this.constraintValidatorManager, this.messageInterpolator, this.constraintValidatorFactory, this.traversableResolver, null, this.timeProvider, this.validatedValueUnwrappers, this.typeResolutionHelper, this.failFast, rootBean, rootBeanClass, executable, null, executableReturnValue, null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final class BeanAndPath
/*     */   {
/*     */     private final Object bean;
/*     */     
/*     */ 
/*     */ 
/*     */     private final Path path;
/*     */     
/*     */ 
/*     */ 
/*     */     private final int hashCode;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private BeanAndPath(Object bean, Path path)
/*     */     {
/* 653 */       this.bean = bean;
/* 654 */       this.path = path;
/*     */       
/* 656 */       this.hashCode = createHashCode();
/*     */     }
/*     */     
/*     */     public boolean equals(Object o)
/*     */     {
/* 661 */       if (this == o) {
/* 662 */         return true;
/*     */       }
/* 664 */       if ((o == null) || (getClass() != o.getClass())) {
/* 665 */         return false;
/*     */       }
/*     */       
/* 668 */       BeanAndPath that = (BeanAndPath)o;
/*     */       
/* 670 */       if (this.bean != that.bean) {
/* 671 */         return false;
/*     */       }
/* 673 */       if (!this.path.equals(that.path)) {
/* 674 */         return false;
/*     */       }
/*     */       
/* 677 */       return true;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 682 */       return this.hashCode;
/*     */     }
/*     */     
/*     */     private int createHashCode() {
/* 686 */       int result = System.identityHashCode(this.bean);
/* 687 */       result = 31 * result + this.path.hashCode();
/* 688 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\ValidationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */