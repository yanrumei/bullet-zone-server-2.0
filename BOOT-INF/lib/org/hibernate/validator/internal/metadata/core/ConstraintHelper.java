/*     */ package org.hibernate.validator.internal.metadata.core;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import javax.validation.Constraint;
/*     */ import javax.validation.ConstraintTarget;
/*     */ import javax.validation.ConstraintValidator;
/*     */ import javax.validation.ValidationException;
/*     */ import javax.validation.constraints.AssertFalse;
/*     */ import javax.validation.constraints.AssertTrue;
/*     */ import javax.validation.constraints.DecimalMax;
/*     */ import javax.validation.constraints.DecimalMin;
/*     */ import javax.validation.constraints.Digits;
/*     */ import javax.validation.constraints.Future;
/*     */ import javax.validation.constraints.Max;
/*     */ import javax.validation.constraints.Min;
/*     */ import javax.validation.constraints.NotNull;
/*     */ import javax.validation.constraints.Null;
/*     */ import javax.validation.constraints.Past;
/*     */ import javax.validation.constraints.Pattern;
/*     */ import javax.validation.constraints.Size;
/*     */ import javax.validation.constraintvalidation.SupportedValidationTarget;
/*     */ import javax.validation.constraintvalidation.ValidationTarget;
/*     */ import org.hibernate.validator.constraints.ConstraintComposition;
/*     */ import org.hibernate.validator.constraints.EAN;
/*     */ import org.hibernate.validator.constraints.Email;
/*     */ import org.hibernate.validator.constraints.Length;
/*     */ import org.hibernate.validator.constraints.LuhnCheck;
/*     */ import org.hibernate.validator.constraints.Mod10Check;
/*     */ import org.hibernate.validator.constraints.Mod11Check;
/*     */ import org.hibernate.validator.constraints.ModCheck;
/*     */ import org.hibernate.validator.constraints.NotBlank;
/*     */ import org.hibernate.validator.constraints.ParameterScriptAssert;
/*     */ import org.hibernate.validator.constraints.SafeHtml;
/*     */ import org.hibernate.validator.constraints.ScriptAssert;
/*     */ import org.hibernate.validator.constraints.URL;
/*     */ import org.hibernate.validator.constraints.br.CNPJ;
/*     */ import org.hibernate.validator.constraints.br.CPF;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.AssertFalseValidator;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.AssertTrueValidator;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.DecimalMaxValidatorForCharSequence;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.DecimalMaxValidatorForNumber;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.DecimalMinValidatorForCharSequence;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.DecimalMinValidatorForNumber;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.DigitsValidatorForCharSequence;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.DigitsValidatorForNumber;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.MaxValidatorForCharSequence;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.MaxValidatorForNumber;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.MinValidatorForCharSequence;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.MinValidatorForNumber;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.NotNullValidator;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.NullValidator;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.PatternValidator;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.future.FutureValidatorForCalendar;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.future.FutureValidatorForChronoZonedDateTime;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.future.FutureValidatorForDate;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.future.FutureValidatorForInstant;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.future.FutureValidatorForOffsetDateTime;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.future.FutureValidatorForReadableInstant;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.future.FutureValidatorForReadablePartial;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.past.PastValidatorForCalendar;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.past.PastValidatorForChronoZonedDateTime;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.past.PastValidatorForDate;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.past.PastValidatorForInstant;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.past.PastValidatorForOffsetDateTime;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.past.PastValidatorForReadableInstant;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.past.PastValidatorForReadablePartial;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.size.SizeValidatorForArray;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.size.SizeValidatorForArraysOfBoolean;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.size.SizeValidatorForArraysOfByte;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.size.SizeValidatorForArraysOfChar;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.size.SizeValidatorForArraysOfDouble;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.size.SizeValidatorForArraysOfFloat;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.size.SizeValidatorForArraysOfInt;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.size.SizeValidatorForArraysOfLong;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.size.SizeValidatorForCharSequence;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.size.SizeValidatorForCollection;
/*     */ import org.hibernate.validator.internal.constraintvalidators.bv.size.SizeValidatorForMap;
/*     */ import org.hibernate.validator.internal.constraintvalidators.hv.EANValidator;
/*     */ import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
/*     */ import org.hibernate.validator.internal.constraintvalidators.hv.LengthValidator;
/*     */ import org.hibernate.validator.internal.constraintvalidators.hv.LuhnCheckValidator;
/*     */ import org.hibernate.validator.internal.constraintvalidators.hv.Mod10CheckValidator;
/*     */ import org.hibernate.validator.internal.constraintvalidators.hv.Mod11CheckValidator;
/*     */ import org.hibernate.validator.internal.constraintvalidators.hv.ModCheckValidator;
/*     */ import org.hibernate.validator.internal.constraintvalidators.hv.NotBlankValidator;
/*     */ import org.hibernate.validator.internal.constraintvalidators.hv.ParameterScriptAssertValidator;
/*     */ import org.hibernate.validator.internal.constraintvalidators.hv.SafeHtmlValidator;
/*     */ import org.hibernate.validator.internal.constraintvalidators.hv.ScriptAssertValidator;
/*     */ import org.hibernate.validator.internal.constraintvalidators.hv.URLValidator;
/*     */ import org.hibernate.validator.internal.constraintvalidators.hv.br.CNPJValidator;
/*     */ import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.Contracts;
/*     */ import org.hibernate.validator.internal.util.Version;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*     */ import org.hibernate.validator.internal.util.logging.Messages;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetAnnotationParameter;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredMethods;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetMethod;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.LoadClass;
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
/*     */ public class ConstraintHelper
/*     */ {
/*     */   public static final String GROUPS = "groups";
/*     */   public static final String PAYLOAD = "payload";
/*     */   public static final String MESSAGE = "message";
/*     */   public static final String VALIDATION_APPLIES_TO = "validationAppliesTo";
/* 135 */   private static final Log log = ;
/*     */   
/*     */   private static final String JODA_TIME_CLASS_NAME = "org.joda.time.ReadableInstant";
/*     */   
/*     */   private final Map<Class<? extends Annotation>, List<? extends Class<?>>> builtinConstraints;
/*     */   
/* 141 */   private final ValidatorClassMap validatorClasses = new ValidatorClassMap(null);
/*     */   
/*     */   public ConstraintHelper() {
/* 144 */     Map<Class<? extends Annotation>, List<? extends Class<?>>> tmpConstraints = CollectionHelper.newHashMap();
/*     */     
/* 146 */     putConstraint(tmpConstraints, AssertFalse.class, AssertFalseValidator.class);
/* 147 */     putConstraint(tmpConstraints, AssertTrue.class, AssertTrueValidator.class);
/* 148 */     putConstraint(tmpConstraints, CNPJ.class, CNPJValidator.class);
/* 149 */     putConstraint(tmpConstraints, CPF.class, CPFValidator.class);
/*     */     
/* 151 */     putConstraints(tmpConstraints, DecimalMax.class, DecimalMaxValidatorForNumber.class, DecimalMaxValidatorForCharSequence.class);
/* 152 */     putConstraints(tmpConstraints, DecimalMin.class, DecimalMinValidatorForNumber.class, DecimalMinValidatorForCharSequence.class);
/* 153 */     putConstraints(tmpConstraints, Digits.class, DigitsValidatorForCharSequence.class, DigitsValidatorForNumber.class);
/*     */     
/* 155 */     List<Class<? extends ConstraintValidator<Future, ?>>> futureValidators = CollectionHelper.newArrayList(11);
/* 156 */     futureValidators.add(FutureValidatorForCalendar.class);
/* 157 */     futureValidators.add(FutureValidatorForDate.class);
/* 158 */     if (isJodaTimeInClasspath()) {
/* 159 */       futureValidators.add(FutureValidatorForReadableInstant.class);
/* 160 */       futureValidators.add(FutureValidatorForReadablePartial.class);
/*     */     }
/* 162 */     if (Version.getJavaRelease() >= 8)
/*     */     {
/* 164 */       futureValidators.add(FutureValidatorForChronoZonedDateTime.class);
/* 165 */       futureValidators.add(FutureValidatorForInstant.class);
/* 166 */       futureValidators.add(FutureValidatorForOffsetDateTime.class);
/*     */     }
/* 168 */     putConstraints(tmpConstraints, Future.class, futureValidators);
/*     */     
/* 170 */     putConstraints(tmpConstraints, Max.class, MaxValidatorForNumber.class, MaxValidatorForCharSequence.class);
/* 171 */     putConstraints(tmpConstraints, Min.class, MinValidatorForNumber.class, MinValidatorForCharSequence.class);
/* 172 */     putConstraint(tmpConstraints, NotNull.class, NotNullValidator.class);
/* 173 */     putConstraint(tmpConstraints, Null.class, NullValidator.class);
/*     */     
/* 175 */     List<Class<? extends ConstraintValidator<Past, ?>>> pastValidators = CollectionHelper.newArrayList(11);
/* 176 */     pastValidators.add(PastValidatorForCalendar.class);
/* 177 */     pastValidators.add(PastValidatorForDate.class);
/* 178 */     if (isJodaTimeInClasspath()) {
/* 179 */       pastValidators.add(PastValidatorForReadableInstant.class);
/* 180 */       pastValidators.add(PastValidatorForReadablePartial.class);
/*     */     }
/* 182 */     if (Version.getJavaRelease() >= 8)
/*     */     {
/* 184 */       pastValidators.add(PastValidatorForChronoZonedDateTime.class);
/* 185 */       pastValidators.add(PastValidatorForInstant.class);
/* 186 */       pastValidators.add(PastValidatorForOffsetDateTime.class);
/*     */     }
/* 188 */     putConstraints(tmpConstraints, Past.class, pastValidators);
/*     */     
/* 190 */     putConstraint(tmpConstraints, Pattern.class, PatternValidator.class);
/*     */     
/* 192 */     List<Class<? extends ConstraintValidator<Size, ?>>> sizeValidators = CollectionHelper.newArrayList(11);
/* 193 */     sizeValidators.add(SizeValidatorForCharSequence.class);
/* 194 */     sizeValidators.add(SizeValidatorForCollection.class);
/* 195 */     sizeValidators.add(SizeValidatorForArray.class);
/* 196 */     sizeValidators.add(SizeValidatorForMap.class);
/* 197 */     sizeValidators.add(SizeValidatorForArraysOfBoolean.class);
/* 198 */     sizeValidators.add(SizeValidatorForArraysOfByte.class);
/* 199 */     sizeValidators.add(SizeValidatorForArraysOfChar.class);
/* 200 */     sizeValidators.add(SizeValidatorForArraysOfDouble.class);
/* 201 */     sizeValidators.add(SizeValidatorForArraysOfFloat.class);
/* 202 */     sizeValidators.add(SizeValidatorForArraysOfInt.class);
/* 203 */     sizeValidators.add(SizeValidatorForArraysOfLong.class);
/*     */     
/* 205 */     putConstraints(tmpConstraints, Size.class, sizeValidators);
/*     */     
/* 207 */     putConstraint(tmpConstraints, EAN.class, EANValidator.class);
/* 208 */     putConstraint(tmpConstraints, Email.class, EmailValidator.class);
/* 209 */     putConstraint(tmpConstraints, Length.class, LengthValidator.class);
/* 210 */     putConstraint(tmpConstraints, ModCheck.class, ModCheckValidator.class);
/* 211 */     putConstraint(tmpConstraints, LuhnCheck.class, LuhnCheckValidator.class);
/* 212 */     putConstraint(tmpConstraints, Mod10Check.class, Mod10CheckValidator.class);
/* 213 */     putConstraint(tmpConstraints, Mod11Check.class, Mod11CheckValidator.class);
/* 214 */     putConstraint(tmpConstraints, NotBlank.class, NotBlankValidator.class);
/* 215 */     putConstraint(tmpConstraints, ParameterScriptAssert.class, ParameterScriptAssertValidator.class);
/* 216 */     putConstraint(tmpConstraints, SafeHtml.class, SafeHtmlValidator.class);
/* 217 */     putConstraint(tmpConstraints, ScriptAssert.class, ScriptAssertValidator.class);
/* 218 */     putConstraint(tmpConstraints, URL.class, URLValidator.class);
/*     */     
/* 220 */     this.builtinConstraints = Collections.unmodifiableMap(tmpConstraints);
/*     */   }
/*     */   
/*     */   private static <A extends Annotation> void putConstraint(Map<Class<? extends Annotation>, List<? extends Class<?>>> validators, Class<A> constraintType, Class<? extends ConstraintValidator<A, ?>> validatorType) {
/* 224 */     validators.put(constraintType, Collections.singletonList(validatorType));
/*     */   }
/*     */   
/*     */   private static <A extends Annotation> void putConstraints(Map<Class<? extends Annotation>, List<? extends Class<?>>> validators, Class<A> constraintType, Class<? extends ConstraintValidator<A, ?>> validatorType1, Class<? extends ConstraintValidator<A, ?>> validatorType2) {
/* 228 */     validators.put(constraintType, Collections.unmodifiableList(Arrays.asList(new Class[] { validatorType1, validatorType2 })));
/*     */   }
/*     */   
/*     */   private static <A extends Annotation> void putConstraints(Map<Class<? extends Annotation>, List<? extends Class<?>>> validators, Class<A> constraintType, List<Class<? extends ConstraintValidator<A, ?>>> validatorTypes) {
/* 232 */     validators.put(constraintType, Collections.unmodifiableList(validatorTypes));
/*     */   }
/*     */   
/*     */   private boolean isBuiltinConstraint(Class<? extends Annotation> annotationType) {
/* 236 */     return this.builtinConstraints.containsKey(annotationType);
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
/*     */   public <A extends Annotation> List<Class<? extends ConstraintValidator<A, ?>>> getAllValidatorClasses(Class<A> annotationType)
/*     */   {
/* 259 */     Contracts.assertNotNull(annotationType, Messages.MESSAGES.classCannotBeNull());
/*     */     
/* 261 */     List<Class<? extends ConstraintValidator<A, ?>>> classes = this.validatorClasses.get(annotationType);
/*     */     
/* 263 */     if (classes == null) {
/* 264 */       classes = getDefaultValidatorClasses(annotationType);
/*     */       
/* 266 */       List<Class<? extends ConstraintValidator<A, ?>>> cachedValidatorClasses = this.validatorClasses.putIfAbsent(annotationType, classes);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 271 */       if (cachedValidatorClasses != null) {
/* 272 */         classes = cachedValidatorClasses;
/*     */       }
/*     */     }
/*     */     
/* 276 */     return Collections.unmodifiableList(classes);
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
/*     */   public <A extends Annotation> List<Class<? extends ConstraintValidator<A, ?>>> findValidatorClasses(Class<A> annotationType, ValidationTarget validationTarget)
/*     */   {
/* 290 */     List<Class<? extends ConstraintValidator<A, ?>>> validatorClasses = getAllValidatorClasses(annotationType);
/* 291 */     List<Class<? extends ConstraintValidator<A, ?>>> matchingValidatorClasses = CollectionHelper.newArrayList();
/*     */     
/* 293 */     for (Class<? extends ConstraintValidator<A, ?>> validatorClass : validatorClasses) {
/* 294 */       if (supportsValidationTarget(validatorClass, validationTarget)) {
/* 295 */         matchingValidatorClasses.add(validatorClass);
/*     */       }
/*     */     }
/*     */     
/* 299 */     return matchingValidatorClasses;
/*     */   }
/*     */   
/*     */   private boolean supportsValidationTarget(Class<? extends ConstraintValidator<?, ?>> validatorClass, ValidationTarget target) {
/* 303 */     SupportedValidationTarget supportedTargetAnnotation = (SupportedValidationTarget)validatorClass.getAnnotation(SupportedValidationTarget.class);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 308 */     if (supportedTargetAnnotation == null) {
/* 309 */       return target == ValidationTarget.ANNOTATED_ELEMENT;
/*     */     }
/*     */     
/* 312 */     return Arrays.asList(supportedTargetAnnotation.value()).contains(target);
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
/*     */   public <A extends Annotation> void putValidatorClasses(Class<A> annotationType, List<Class<? extends ConstraintValidator<A, ?>>> definitionClasses, boolean keepExistingClasses)
/*     */   {
/* 327 */     if (keepExistingClasses) {
/* 328 */       List<Class<? extends ConstraintValidator<A, ?>>> existingClasses = getAllValidatorClasses(annotationType);
/* 329 */       if (existingClasses != null) {
/* 330 */         definitionClasses.addAll(0, existingClasses);
/*     */       }
/*     */     }
/*     */     
/* 334 */     this.validatorClasses.put(annotationType, definitionClasses);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isMultiValueConstraint(Class<? extends Annotation> annotationType)
/*     */   {
/* 346 */     boolean isMultiValueConstraint = false;
/* 347 */     Method method = (Method)run(GetMethod.action(annotationType, "value"));
/* 348 */     if (method != null) {
/* 349 */       Class<?> returnType = method.getReturnType();
/* 350 */       if ((returnType.isArray()) && (returnType.getComponentType().isAnnotation()))
/*     */       {
/* 352 */         Class<? extends Annotation> componentType = returnType.getComponentType();
/* 353 */         if ((isConstraintAnnotation(componentType)) || (isBuiltinConstraint(componentType))) {
/* 354 */           isMultiValueConstraint = true;
/*     */         }
/*     */         else {
/* 357 */           isMultiValueConstraint = false;
/*     */         }
/*     */       }
/*     */     }
/* 361 */     return isMultiValueConstraint;
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
/*     */   public <A extends Annotation> List<Annotation> getConstraintsFromMultiValueConstraint(A multiValueConstraint)
/*     */   {
/* 376 */     Annotation[] annotations = (Annotation[])run(
/* 377 */       GetAnnotationParameter.action(multiValueConstraint, "value", Annotation[].class));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 383 */     return Arrays.asList(annotations);
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
/*     */   public boolean isConstraintAnnotation(Class<? extends Annotation> annotationType)
/*     */   {
/* 401 */     if (annotationType.getAnnotation(Constraint.class) == null) {
/* 402 */       return false;
/*     */     }
/*     */     
/* 405 */     assertMessageParameterExists(annotationType);
/* 406 */     assertGroupsParameterExists(annotationType);
/* 407 */     assertPayloadParameterExists(annotationType);
/* 408 */     assertValidationAppliesToParameterSetUpCorrectly(annotationType);
/* 409 */     assertNoParameterStartsWithValid(annotationType);
/*     */     
/* 411 */     return true;
/*     */   }
/*     */   
/*     */   private void assertNoParameterStartsWithValid(Class<? extends Annotation> annotationType) {
/* 415 */     Method[] methods = (Method[])run(GetDeclaredMethods.action(annotationType));
/* 416 */     for (Method m : methods) {
/* 417 */       if ((m.getName().startsWith("valid")) && (!m.getName().equals("validationAppliesTo"))) {
/* 418 */         throw log.getConstraintParametersCannotStartWithValidException();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void assertPayloadParameterExists(Class<? extends Annotation> annotationType) {
/*     */     try {
/* 425 */       Method method = (Method)run(GetMethod.action(annotationType, "payload"));
/* 426 */       if (method == null) {
/* 427 */         throw log.getConstraintWithoutMandatoryParameterException("payload", annotationType.getName());
/*     */       }
/* 429 */       Class<?>[] defaultPayload = (Class[])method.getDefaultValue();
/* 430 */       if (defaultPayload.length != 0) {
/* 431 */         throw log.getWrongDefaultValueForPayloadParameterException(annotationType.getName());
/*     */       }
/*     */     }
/*     */     catch (ClassCastException e) {
/* 435 */       throw log.getWrongTypeForPayloadParameterException(annotationType.getName(), e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void assertGroupsParameterExists(Class<? extends Annotation> annotationType) {
/*     */     try {
/* 441 */       Method method = (Method)run(GetMethod.action(annotationType, "groups"));
/* 442 */       if (method == null) {
/* 443 */         throw log.getConstraintWithoutMandatoryParameterException("groups", annotationType.getName());
/*     */       }
/* 445 */       Class<?>[] defaultGroups = (Class[])method.getDefaultValue();
/* 446 */       if (defaultGroups.length != 0) {
/* 447 */         throw log.getWrongDefaultValueForGroupsParameterException(annotationType.getName());
/*     */       }
/*     */     }
/*     */     catch (ClassCastException e) {
/* 451 */       throw log.getWrongTypeForGroupsParameterException(annotationType.getName(), e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void assertMessageParameterExists(Class<? extends Annotation> annotationType) {
/* 456 */     Method method = (Method)run(GetMethod.action(annotationType, "message"));
/* 457 */     if (method == null) {
/* 458 */       throw log.getConstraintWithoutMandatoryParameterException("message", annotationType.getName());
/*     */     }
/* 460 */     if (method.getReturnType() != String.class) {
/* 461 */       throw log.getWrongTypeForMessageParameterException(annotationType.getName());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void assertValidationAppliesToParameterSetUpCorrectly(Class<? extends Annotation> annotationType)
/*     */   {
/* 469 */     boolean hasGenericValidators = !findValidatorClasses(annotationType, ValidationTarget.ANNOTATED_ELEMENT).isEmpty();
/*     */     
/*     */ 
/*     */ 
/* 473 */     boolean hasCrossParameterValidator = !findValidatorClasses(annotationType, ValidationTarget.PARAMETERS).isEmpty();
/* 474 */     Method method = (Method)run(GetMethod.action(annotationType, "validationAppliesTo"));
/*     */     
/* 476 */     if ((hasGenericValidators) && (hasCrossParameterValidator)) {
/* 477 */       if (method == null) {
/* 478 */         throw log.getGenericAndCrossParameterConstraintDoesNotDefineValidationAppliesToParameterException(annotationType
/* 479 */           .getName());
/*     */       }
/*     */       
/* 482 */       if (method.getReturnType() != ConstraintTarget.class) {
/* 483 */         throw log.getValidationAppliesToParameterMustHaveReturnTypeConstraintTargetException(annotationType.getName());
/*     */       }
/* 485 */       ConstraintTarget defaultValue = (ConstraintTarget)method.getDefaultValue();
/* 486 */       if (defaultValue != ConstraintTarget.IMPLICIT) {
/* 487 */         throw log.getValidationAppliesToParameterMustHaveDefaultValueImplicitException(annotationType.getName());
/*     */       }
/*     */     }
/* 490 */     else if (method != null) {
/* 491 */       throw log.getValidationAppliesToParameterMustNotBeDefinedForNonGenericAndCrossParameterConstraintException(annotationType
/* 492 */         .getName());
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isConstraintComposition(Class<? extends Annotation> annotationType)
/*     */   {
/* 498 */     return annotationType == ConstraintComposition.class;
/*     */   }
/*     */   
/*     */   private static boolean isJodaTimeInClasspath() {
/* 502 */     return isClassPresent("org.joda.time.ReadableInstant");
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
/*     */   private <A extends Annotation> List<Class<? extends ConstraintValidator<A, ?>>> getDefaultValidatorClasses(Class<A> annotationType)
/*     */   {
/* 518 */     List<Class<? extends ConstraintValidator<A, ?>>> builtInValidators = (List)this.builtinConstraints.get(annotationType);
/*     */     
/* 520 */     if (builtInValidators != null) {
/* 521 */       return builtInValidators;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 526 */     Class<? extends ConstraintValidator<A, ?>>[] validatedBy = (Class[])((Constraint)annotationType.getAnnotation(Constraint.class)).validatedBy();
/*     */     
/* 528 */     return Arrays.asList(validatedBy);
/*     */   }
/*     */   
/*     */   private static boolean isClassPresent(String className) {
/*     */     try {
/* 533 */       run(LoadClass.action(className, ConstraintHelper.class.getClassLoader()));
/* 534 */       return true;
/*     */     }
/*     */     catch (ValidationException e) {}
/* 537 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static <T> T run(PrivilegedAction<T> action)
/*     */   {
/* 548 */     return (T)(System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run());
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
/*     */   private static class ValidatorClassMap
/*     */   {
/* 561 */     private final ConcurrentMap<Class<? extends Annotation>, List<? extends Class<?>>> constraintValidatorClasses = CollectionHelper.newConcurrentHashMap();
/*     */     
/*     */     private <A extends Annotation> List<Class<? extends ConstraintValidator<A, ?>>> get(Class<A> annotationType) {
/* 564 */       return (List)this.constraintValidatorClasses.get(annotationType);
/*     */     }
/*     */     
/*     */     private <A extends Annotation> void put(Class<A> annotationType, List<Class<? extends ConstraintValidator<A, ?>>> validatorClasses) {
/* 568 */       this.constraintValidatorClasses.put(annotationType, validatorClasses);
/*     */     }
/*     */     
/*     */     private <A extends Annotation> List<Class<? extends ConstraintValidator<A, ?>>> putIfAbsent(Class<A> annotationType, List<Class<? extends ConstraintValidator<A, ?>>> classes) {
/* 572 */       return (List)this.constraintValidatorClasses.putIfAbsent(annotationType, classes);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\core\ConstraintHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */