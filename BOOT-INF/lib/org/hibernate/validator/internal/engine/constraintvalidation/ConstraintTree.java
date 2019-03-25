/*     */ package org.hibernate.validator.internal.engine.constraintvalidation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.validation.ConstraintValidator;
/*     */ import javax.validation.ConstraintViolation;
/*     */ import org.hibernate.validator.constraints.CompositionType;
/*     */ import org.hibernate.validator.internal.engine.ValidationContext;
/*     */ import org.hibernate.validator.internal.engine.ValueContext;
/*     */ import org.hibernate.validator.internal.engine.path.PathImpl;
/*     */ import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
/*     */ import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
/*     */ import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl.ConstraintType;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
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
/*     */ public class ConstraintTree<A extends Annotation>
/*     */ {
/*     */   private static final String TYPE_USE = "TYPE_USE";
/*  45 */   private static final Log log = LoggerFactory.make();
/*     */   
/*     */   private final ConstraintTree<?> parent;
/*     */   
/*     */   private final List<ConstraintTree<?>> children;
/*     */   
/*     */   private final ConstraintDescriptorImpl<A> descriptor;
/*     */   
/*     */ 
/*     */   public ConstraintTree(ConstraintDescriptorImpl<A> descriptor)
/*     */   {
/*  56 */     this(descriptor, null);
/*     */   }
/*     */   
/*     */   private ConstraintTree(ConstraintDescriptorImpl<A> descriptor, ConstraintTree<?> parent) {
/*  60 */     this.parent = parent;
/*  61 */     this.descriptor = descriptor;
/*     */     
/*  63 */     Set<ConstraintDescriptorImpl<?>> composingConstraints = descriptor.getComposingConstraintImpls();
/*  64 */     this.children = CollectionHelper.newArrayList(composingConstraints.size());
/*     */     
/*  66 */     for (ConstraintDescriptorImpl<?> composingDescriptor : composingConstraints) {
/*  67 */       ConstraintTree<?> treeNode = createConstraintTree(composingDescriptor);
/*  68 */       this.children.add(treeNode);
/*     */     }
/*     */   }
/*     */   
/*     */   private <U extends Annotation> ConstraintTree<U> createConstraintTree(ConstraintDescriptorImpl<U> composingDescriptor) {
/*  73 */     return new ConstraintTree(composingDescriptor, this);
/*     */   }
/*     */   
/*     */   public final List<ConstraintTree<?>> getChildren() {
/*  77 */     return this.children;
/*     */   }
/*     */   
/*     */   public final ConstraintDescriptorImpl<A> getDescriptor() {
/*  81 */     return this.descriptor;
/*     */   }
/*     */   
/*     */   public final <T> boolean validateConstraints(ValidationContext<T> executionContext, ValueContext<?, ?> valueContext)
/*     */   {
/*  86 */     Set<ConstraintViolation<T>> constraintViolations = CollectionHelper.newHashSet();
/*  87 */     validateConstraints(executionContext, valueContext, constraintViolations);
/*  88 */     if (!constraintViolations.isEmpty()) {
/*  89 */       executionContext.addConstraintFailures(constraintViolations);
/*  90 */       return false;
/*     */     }
/*  92 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   private <T, V> void validateConstraints(ValidationContext<T> validationContext, ValueContext<?, V> valueContext, Set<ConstraintViolation<T>> constraintViolations)
/*     */   {
/*  98 */     CompositionResult compositionResult = validateComposingConstraints(validationContext, valueContext, constraintViolations);
/*     */     
/*     */ 
/*     */ 
/*     */     Set<ConstraintViolation<T>> localViolations;
/*     */     
/*     */ 
/* 105 */     if (mainConstraintNeedsEvaluation(validationContext, constraintViolations))
/*     */     {
/* 107 */       if (log.isTraceEnabled()) {
/* 108 */         log.tracef("Validating value %s against constraint defined by %s.", valueContext
/*     */         
/* 110 */           .getCurrentValidatedValue(), this.descriptor);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 116 */       ConstraintValidator<A, V> validator = getInitializedConstraintValidator(validationContext, valueContext);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 122 */       ConstraintValidatorContextImpl constraintValidatorContext = new ConstraintValidatorContextImpl(validationContext.getParameterNames(), validationContext.getTimeProvider(), valueContext.getPropertyPath(), this.descriptor);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 127 */       Set<ConstraintViolation<T>> localViolations = validateSingleConstraint(validationContext, valueContext, constraintValidatorContext, validator);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 136 */       if (localViolations.isEmpty()) {
/* 137 */         compositionResult.setAtLeastOneTrue(true);
/*     */       }
/*     */       else {
/* 140 */         compositionResult.setAllTrue(false);
/*     */       }
/*     */     }
/*     */     else {
/* 144 */       localViolations = Collections.emptySet();
/*     */     }
/*     */     
/* 147 */     if (!passesCompositionTypeRequirement(constraintViolations, compositionResult)) {
/* 148 */       prepareFinalConstraintViolations(validationContext, valueContext, constraintViolations, localViolations);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private <T, V> ConstraintValidator<A, V> getInitializedConstraintValidator(ValidationContext<T> validationContext, ValueContext<?, V> valueContext)
/*     */   {
/* 156 */     Type validatedValueType = valueContext.getDeclaredTypeOfValidatedElement();
/*     */     
/*     */ 
/* 159 */     ValidatedValueUnwrapper<V> validatedValueUnwrapper = validationContext.getValidatedValueUnwrapper(validatedValueType);
/*     */     
/*     */ 
/* 162 */     if (valueContext.getUnwrapMode().equals(UnwrapMode.AUTOMATIC)) {
/* 163 */       return getConstraintValidatorInstanceForAutomaticUnwrapping(validationContext, valueContext);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 169 */     if ((valueContext.getUnwrapMode().equals(UnwrapMode.UNWRAP)) || 
/* 170 */       ("TYPE_USE".equals(valueContext.getElementType().name()))) {
/* 171 */       return getInitializedValidatorInstanceForWrappedInstance(validationContext, valueContext, validatedValueType, validatedValueUnwrapper);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 181 */     return getConstraintValidatorNoUnwrapping(validationContext, valueContext);
/*     */   }
/*     */   
/*     */ 
/*     */   private <T, V> ConstraintValidator<A, V> getInitializedValidatorInstanceForWrappedInstance(ValidationContext<T> validationContext, ValueContext<?, V> valueContext, Type validatedValueType, ValidatedValueUnwrapper<V> validatedValueUnwrapper)
/*     */   {
/* 187 */     if (validatedValueUnwrapper == null) {
/* 188 */       throw log.getNoUnwrapperFoundForTypeException(valueContext
/* 189 */         .getDeclaredTypeOfValidatedElement()
/* 190 */         .toString());
/*     */     }
/*     */     
/*     */ 
/* 194 */     valueContext.setValidatedValueHandler(validatedValueUnwrapper);
/* 195 */     validatedValueType = validatedValueUnwrapper.getValidatedValueType(validatedValueType);
/*     */     
/*     */ 
/* 198 */     ConstraintValidator<A, V> validator = validationContext.getConstraintValidatorManager().getInitializedValidator(validatedValueType, this.descriptor, validationContext
/*     */     
/*     */ 
/* 201 */       .getConstraintValidatorFactory());
/*     */     
/*     */ 
/* 204 */     if (validator == null) {
/* 205 */       throwExceptionForNullValidator(validatedValueType, valueContext.getPropertyPath().asString());
/*     */     }
/*     */     
/* 208 */     return validator;
/*     */   }
/*     */   
/*     */   private void throwExceptionForNullValidator(Type validatedValueType, String path) {
/* 212 */     if (this.descriptor.getConstraintType() == ConstraintDescriptorImpl.ConstraintType.CROSS_PARAMETER) {
/* 213 */       throw log.getValidatorForCrossParameterConstraintMustEitherValidateObjectOrObjectArrayException(this.descriptor
/* 214 */         .getAnnotationType()
/* 215 */         .getName());
/*     */     }
/*     */     
/*     */ 
/* 219 */     String className = validatedValueType.toString();
/* 220 */     if ((validatedValueType instanceof Class)) {
/* 221 */       Class<?> clazz = (Class)validatedValueType;
/* 222 */       if (clazz.isArray()) {
/* 223 */         className = clazz.getComponentType().toString() + "[]";
/*     */       }
/*     */       else {
/* 226 */         className = clazz.getName();
/*     */       }
/*     */     }
/* 229 */     throw log.getNoValidatorFoundForTypeException(this.descriptor.getAnnotationType().getName(), className, path);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private <T, V> ConstraintValidator<A, V> getConstraintValidatorInstanceForAutomaticUnwrapping(ValidationContext<T> validationContext, ValueContext<?, V> valueContext)
/*     */   {
/* 237 */     Type validatedValueType = valueContext.getDeclaredTypeOfValidatedElement();
/*     */     
/*     */ 
/* 240 */     ValidatedValueUnwrapper<V> validatedValueUnwrapper = validationContext.getValidatedValueUnwrapper(validatedValueType);
/*     */     
/*     */ 
/* 243 */     if (validatedValueUnwrapper == null) {
/* 244 */       return getConstraintValidatorNoUnwrapping(validationContext, valueContext);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 250 */     ConstraintValidator<A, V> validatorForWrappedValue = validationContext.getConstraintValidatorManager().getInitializedValidator(validatedValueUnwrapper
/* 251 */       .getValidatedValueType(validatedValueType), this.descriptor, validationContext
/*     */       
/* 253 */       .getConstraintValidatorFactory());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 258 */     ConstraintValidator<A, V> validatorForWrapper = validationContext.getConstraintValidatorManager().getInitializedValidator(valueContext
/* 259 */       .getDeclaredTypeOfValidatedElement(), this.descriptor, validationContext
/*     */       
/* 261 */       .getConstraintValidatorFactory());
/*     */     
/*     */ 
/*     */ 
/* 265 */     if ((validatorForWrappedValue != null) && (validatorForWrapper != null)) {
/* 266 */       throw log.getConstraintValidatorExistsForWrapperAndWrappedValueException(valueContext
/* 267 */         .getPropertyPath().toString(), this.descriptor
/* 268 */         .getAnnotationType().getName(), validatedValueUnwrapper
/* 269 */         .getClass().getName());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 274 */     if ((validatorForWrappedValue == null) && (validatorForWrapper == null)) {
/* 275 */       throw log.getNoValidatorFoundForTypeException(this.descriptor
/* 276 */         .getAnnotationType().getName(), validatedValueType
/* 277 */         .toString(), valueContext
/* 278 */         .getPropertyPath().toString());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 284 */     if (validatorForWrappedValue != null) {
/* 285 */       valueContext.setValidatedValueHandler(validatedValueUnwrapper);
/* 286 */       return validatorForWrappedValue;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 291 */     valueContext.setValidatedValueHandler(null);
/* 292 */     return validatorForWrapper;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private <T, V> ConstraintValidator<A, V> getConstraintValidatorNoUnwrapping(ValidationContext<T> validationContext, ValueContext<?, V> valueContext)
/*     */   {
/* 299 */     valueContext.setValidatedValueHandler(null);
/*     */     
/* 301 */     Type validatedValueType = valueContext.getDeclaredTypeOfValidatedElement();
/*     */     
/* 303 */     ConstraintValidator<A, V> validator = validationContext.getConstraintValidatorManager().getInitializedValidator(validatedValueType, this.descriptor, validationContext
/*     */     
/*     */ 
/* 306 */       .getConstraintValidatorFactory());
/*     */     
/*     */ 
/* 309 */     if (validator == null) {
/* 310 */       throwExceptionForNullValidator(validatedValueType, valueContext.getPropertyPath().asString());
/*     */     }
/*     */     
/* 313 */     return validator;
/*     */   }
/*     */   
/*     */ 
/*     */   private <T> boolean mainConstraintNeedsEvaluation(ValidationContext<T> executionContext, Set<ConstraintViolation<T>> constraintViolations)
/*     */   {
/* 319 */     if ((!this.descriptor.getComposingConstraints().isEmpty()) && 
/* 320 */       (this.descriptor.getMatchingConstraintValidatorClasses().isEmpty())) {
/* 321 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 325 */     if ((this.descriptor.isReportAsSingleViolation()) && (this.descriptor.getCompositionType() == CompositionType.AND) && (!constraintViolations.isEmpty())) {
/* 326 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 330 */     if ((executionContext.isFailFastModeEnabled()) && (!constraintViolations.isEmpty())) {
/* 331 */       return false;
/*     */     }
/*     */     
/* 334 */     return true;
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
/*     */   private <T> void prepareFinalConstraintViolations(ValidationContext<T> executionContext, ValueContext<?, ?> valueContext, Set<ConstraintViolation<T>> constraintViolations, Set<ConstraintViolation<T>> localViolations)
/*     */   {
/* 350 */     if (reportAsSingleViolation())
/*     */     {
/* 352 */       constraintViolations.clear();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 358 */       if (localViolations.isEmpty()) {
/* 359 */         String message = (String)getDescriptor().getAttributes().get("message");
/*     */         
/*     */ 
/* 362 */         ConstraintViolationCreationContext constraintViolationCreationContext = new ConstraintViolationCreationContext(message, valueContext.getPropertyPath());
/*     */         
/* 364 */         ConstraintViolation<T> violation = executionContext.createConstraintViolation(valueContext, constraintViolationCreationContext, this.descriptor);
/*     */         
/*     */ 
/* 367 */         constraintViolations.add(violation);
/*     */       }
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
/* 379 */     constraintViolations.addAll(localViolations);
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
/*     */   private <T> CompositionResult validateComposingConstraints(ValidationContext<T> executionContext, ValueContext<?, ?> valueContext, Set<ConstraintViolation<T>> constraintViolations)
/*     */   {
/* 394 */     CompositionResult compositionResult = new CompositionResult(true, false);
/* 395 */     List<ConstraintTree<?>> children = getChildren();
/* 396 */     for (ConstraintTree<?> tree : children) {
/* 397 */       Set<ConstraintViolation<T>> tmpViolations = CollectionHelper.newHashSet();
/* 398 */       tree.validateConstraints(executionContext, valueContext, tmpViolations);
/* 399 */       constraintViolations.addAll(tmpViolations);
/*     */       
/* 401 */       if (tmpViolations.isEmpty()) {
/* 402 */         compositionResult.setAtLeastOneTrue(true);
/*     */         
/* 404 */         if (this.descriptor.getCompositionType() == CompositionType.OR) {
/*     */           break;
/*     */         }
/*     */       }
/*     */       else {
/* 409 */         compositionResult.setAllTrue(false);
/* 410 */         if ((this.descriptor.getCompositionType() == CompositionType.AND) && (
/* 411 */           (executionContext.isFailFastModeEnabled()) || (this.descriptor.isReportAsSingleViolation()))) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/* 416 */     return compositionResult;
/*     */   }
/*     */   
/*     */   private boolean passesCompositionTypeRequirement(Set<?> constraintViolations, CompositionResult compositionResult) {
/* 420 */     CompositionType compositionType = getDescriptor().getCompositionType();
/* 421 */     boolean passedValidation = false;
/* 422 */     switch (compositionType) {
/*     */     case OR: 
/* 424 */       passedValidation = compositionResult.isAtLeastOneTrue();
/* 425 */       break;
/*     */     case AND: 
/* 427 */       passedValidation = compositionResult.isAllTrue();
/* 428 */       break;
/*     */     case ALL_FALSE: 
/* 430 */       passedValidation = !compositionResult.isAtLeastOneTrue();
/*     */     }
/*     */     
/* 433 */     assert ((!passedValidation) || (compositionType != CompositionType.AND) || (constraintViolations.isEmpty()));
/* 434 */     if (passedValidation) {
/* 435 */       constraintViolations.clear();
/*     */     }
/* 437 */     return passedValidation;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private <T, V> Set<ConstraintViolation<T>> validateSingleConstraint(ValidationContext<T> executionContext, ValueContext<?, ?> valueContext, ConstraintValidatorContextImpl constraintValidatorContext, ConstraintValidator<A, V> validator)
/*     */   {
/*     */     try
/*     */     {
/* 447 */       V validatedValue = valueContext.getCurrentValidatedValue();
/* 448 */       isValid = validator.isValid(validatedValue, constraintValidatorContext);
/*     */     } catch (RuntimeException e) {
/*     */       boolean isValid;
/* 451 */       throw log.getExceptionDuringIsValidCallException(e); }
/*     */     boolean isValid;
/* 453 */     if (!isValid)
/*     */     {
/*     */ 
/* 456 */       return executionContext.createConstraintViolations(valueContext, constraintValidatorContext);
/*     */     }
/*     */     
/*     */ 
/* 460 */     return Collections.emptySet();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean reportAsSingleViolation()
/*     */   {
/* 469 */     return (getDescriptor().isReportAsSingleViolation()) || 
/* 470 */       (getDescriptor().getCompositionType() == CompositionType.ALL_FALSE);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 475 */     StringBuilder sb = new StringBuilder();
/* 476 */     sb.append("ConstraintTree");
/* 477 */     sb.append("{ descriptor=").append(this.descriptor);
/* 478 */     sb.append(", isRoot=").append(this.parent == null);
/* 479 */     sb.append('}');
/* 480 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private static final class CompositionResult {
/*     */     private boolean allTrue;
/*     */     private boolean atLeastOneTrue;
/*     */     
/*     */     CompositionResult(boolean allTrue, boolean atLeastOneTrue) {
/* 488 */       this.allTrue = allTrue;
/* 489 */       this.atLeastOneTrue = atLeastOneTrue;
/*     */     }
/*     */     
/*     */     public boolean isAllTrue() {
/* 493 */       return this.allTrue;
/*     */     }
/*     */     
/*     */     public boolean isAtLeastOneTrue() {
/* 497 */       return this.atLeastOneTrue;
/*     */     }
/*     */     
/*     */     public void setAllTrue(boolean allTrue) {
/* 501 */       this.allTrue = allTrue;
/*     */     }
/*     */     
/*     */     public void setAtLeastOneTrue(boolean atLeastOneTrue) {
/* 505 */       this.atLeastOneTrue = atLeastOneTrue;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\constraintvalidation\ConstraintTree.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */