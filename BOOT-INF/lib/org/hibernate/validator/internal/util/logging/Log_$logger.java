/*      */ package org.hibernate.validator.internal.util.logging;
/*      */ 
/*      */ import java.io.Serializable;
/*      */ import java.lang.annotation.ElementType;
/*      */ import java.lang.reflect.Member;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Type;
/*      */ import java.lang.reflect.TypeVariable;
/*      */ import java.util.Arrays;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import java.util.regex.PatternSyntaxException;
/*      */ import javax.validation.ConstraintDeclarationException;
/*      */ import javax.validation.ConstraintDefinitionException;
/*      */ import javax.validation.ConstraintTarget;
/*      */ import javax.validation.ElementKind;
/*      */ import javax.validation.GroupDefinitionException;
/*      */ import javax.validation.Path;
/*      */ import javax.validation.UnexpectedTypeException;
/*      */ import javax.validation.ValidationException;
/*      */ import javax.xml.stream.XMLStreamException;
/*      */ import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageDescriptorFormatException;
/*      */ import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl.ConstraintType;
/*      */ import org.jboss.logging.BasicLogger;
/*      */ import org.jboss.logging.DelegatingBasicLogger;
/*      */ import org.jboss.logging.Logger;
/*      */ import org.jboss.logging.Logger.Level;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Log_$logger
/*      */   extends DelegatingBasicLogger
/*      */   implements Log, BasicLogger, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*   47 */   private static final String FQCN = logger.class.getName();
/*      */   private static final String version = "HV000001: Hibernate Validator %s"; private static final String ignoringXmlConfiguration = "HV000002: Ignoring XML configuration."; private static final String usingConstraintFactory = "HV000003: Using %s as constraint factory."; private static final String usingMessageInterpolator = "HV000004: Using %s as message interpolator."; private static final String usingTraversableResolver = "HV000005: Using %s as traversable resolver."; private static final String usingValidationProvider = "HV000006: Using %s as validation provider."; private static final String parsingXMLFile = "HV000007: %s found. Parsing XML based configuration."; private static final String unableToCloseInputStream = "HV000008: Unable to close input stream."; private static final String unableToCloseXMLFileInputStream = "HV000010: Unable to close input stream for %s."; private static final String unableToCreateSchema = "HV000011: Unable to create schema for %1$s: %2$s"; private static final String getUnableToCreateAnnotationForConfiguredConstraintException = "HV000012: Unable to create annotation for configured constraint"; private static final String getUnableToFindPropertyWithAccessException = "HV000013: The class %1$s does not have a property '%2$s' with access %3$s."; private static final String getUnableToFindMethodException = "HV000014: Type %1$s doesn't have a method %2$s."; private static final String getInvalidBigDecimalFormatException = "HV000016: %s does not represent a valid BigDecimal format."; private static final String getInvalidLengthForIntegerPartException = "HV000017: The length of the integer part cannot be negative."; private static final String getInvalidLengthForFractionPartException = "HV000018: The length of the fraction part cannot be negative."; private static final String getMinCannotBeNegativeException = "HV000019: The min parameter cannot be negative."; private static final String getMaxCannotBeNegativeException = "HV000020: The max parameter cannot be negative."; private static final String getLengthCannotBeNegativeException = "HV000021: The length cannot be negative."; private static final String getInvalidRegularExpressionException = "HV000022: Invalid regular expression."; private static final String getErrorDuringScriptExecutionException = "HV000023: Error during execution of script \"%s\" occurred."; private static final String getScriptMustReturnTrueOrFalseException1 = "HV000024: Script \"%s\" returned null, but must return either true or false."; private static final String getScriptMustReturnTrueOrFalseException3 = "HV000025: Script \"%1$s\" returned %2$s (of type %3$s), but must return either true or false."; private static final String getInconsistentConfigurationException = "HV000026: Assertion error: inconsistent ConfigurationImpl construction."; private static final String getUnableToFindProviderException = "HV000027: Unable to find provider: %s."; private static final String getExceptionDuringIsValidCallException = "HV000028: Unexpected exception during isValid call."; private static final String getConstraintFactoryMustNotReturnNullException = "HV000029: Constraint factory returned null when trying to create instance of %s."; private static final String getNoValidatorFoundForTypeException = "HV000030: No validator could be found for constraint '%s' validating type '%s'. Check configuration for '%s'"; private static final String getMoreThanOneValidatorFoundForTypeException = "HV000031: There are multiple validator classes which could validate the type %1$s. The validator classes are: %2$s."; private static final String getUnableToInitializeConstraintValidatorException = "HV000032: Unable to initialize %s."; private static final String getAtLeastOneCustomMessageMustBeCreatedException = "HV000033: At least one custom message must be created if the default error message gets disabled."; private static final String getInvalidJavaIdentifierException = "HV000034: %s is not a valid Java Identifier."; private static final String getUnableToParsePropertyPathException = "HV000035: Unable to parse property path %s."; private static final String getTypeNotSupportedForUnwrappingException = "HV000036: Type %s not supported for unwrapping."; private static final String getInconsistentFailFastConfigurationException = "HV000037: Inconsistent fail fast configuration. Fail fast enabled via programmatic API, but explicitly disabled via properties."; private static final String getInvalidPropertyPathException0 = "HV000038: Invalid property path."; private static final String getInvalidPropertyPathException2 = "HV000039: Invalid property path. Either there is no property %2$s in entity %1$s or it is not possible to cascade to the property."; private static final String getPropertyPathMustProvideIndexOrMapKeyException = "HV000040: Property path must provide index or map key."; private static final String getErrorDuringCallOfTraversableResolverIsReachableException = "HV000041: Call to TraversableResolver.isReachable() threw an exception.";
/*   49 */   public Log_$logger(Logger log) { super(log); }
/*      */   
/*      */   private static final String getErrorDuringCallOfTraversableResolverIsCascadableException = "HV000042: Call to TraversableResolver.isCascadable() threw an exception.";
/*      */   private static final String getUnableToExpandDefaultGroupListException = "HV000043: Unable to expand default group list %1$s into sequence %2$s."; private static final String getAtLeastOneGroupHasToBeSpecifiedException = "HV000044: At least one group has to be specified."; private static final String getGroupHasToBeAnInterfaceException = "HV000045: A group has to be an interface. %s is not."; private static final String getSequenceDefinitionsNotAllowedException = "HV000046: Sequence definitions are not allowed as composing parts of a sequence."; private static final String getCyclicDependencyInGroupsDefinitionException = "HV000047: Cyclic dependency in groups definition"; private static final String getUnableToExpandGroupSequenceException = "HV000048: Unable to expand group sequence."; private static final String getInvalidDefaultGroupSequenceDefinitionException = "HV000052: Default group sequence and default group sequence provider cannot be defined at the same time."; private static final String getNoDefaultGroupInGroupSequenceException = "HV000053: 'Default.class' cannot appear in default group sequence list."; private static final String getBeanClassMustBePartOfRedefinedDefaultGroupSequenceException = "HV000054: %s must be part of the redefined default group sequence."; private static final String getWrongDefaultGroupSequenceProviderTypeException = "HV000055: The default group sequence provider defined for %s has the wrong type"; private static final String getInvalidExecutableParameterIndexException = "HV000056: Method or constructor %1$s doesn't have a parameter with index %2$d."; private static final String getUnableToRetrieveAnnotationParameterValueException = "HV000059: Unable to retrieve annotation parameter value."; private static final String getInvalidLengthOfParameterMetaDataListException = "HV000062: Method or constructor %1$s has %2$s parameters, but the passed list of parameter meta data has a size of %3$s."; private static final String getUnableToInstantiateException1 = "HV000063: Unable to instantiate %s."; private static final String getUnableToInstantiateException2 = "HV000064: Unable to instantiate %1$s: %2$s."; private static final String getUnableToLoadClassException = "HV000065: Unable to load class: %s from %s."; private static final String getStartIndexCannotBeNegativeException = "HV000068: Start index cannot be negative: %d."; private static final String getEndIndexCannotBeNegativeException = "HV000069: End index cannot be negative: %d."; private static final String getInvalidRangeException = "HV000070: Invalid Range: %1$d > %2$d."; private static final String getInvalidCheckDigitException = "HV000071: A explicitly specified check digit must lie outside the interval: [%1$d, %2$d]."; private static final String getCharacterIsNotADigitException = "HV000072: '%c' is not a digit."; private static final String getConstraintParametersCannotStartWithValidException = "HV000073: Parameters starting with 'valid' are not allowed in a constraint."; private static final String getConstraintWithoutMandatoryParameterException = "HV000074: %2$s contains Constraint annotation, but does not contain a %1$s parameter."; private static final String getWrongDefaultValueForPayloadParameterException = "HV000075: %s contains Constraint annotation, but the payload parameter default value is not the empty array."; private static final String getWrongTypeForPayloadParameterException = "HV000076: %s contains Constraint annotation, but the payload parameter is of wrong type."; private static final String getWrongDefaultValueForGroupsParameterException = "HV000077: %s contains Constraint annotation, but the groups parameter default value is not the empty array."; private static final String getWrongTypeForGroupsParameterException = "HV000078: %s contains Constraint annotation, but the groups parameter is of wrong type."; private static final String getWrongTypeForMessageParameterException = "HV000079: %s contains Constraint annotation, but the message parameter is not of type java.lang.String."; private static final String getOverriddenConstraintAttributeNotFoundException = "HV000080: Overridden constraint does not define an attribute with name %s."; private static final String getWrongAttributeTypeForOverriddenConstraintException = "HV000081: The overriding type of a composite constraint must be identical to the overridden one. Expected %1$s found %2$s."; private static final String getWrongParameterTypeException = "HV000082: Wrong parameter type. Expected: %1$s Actual: %2$s."; private static final String getUnableToFindAnnotationParameterException = "HV000083: The specified annotation defines no parameter '%s'."; private static final String getUnableToGetAnnotationParameterException = "HV000084: Unable to get '%1$s' from %2$s."; private static final String getNoValueProvidedForAnnotationParameterException = "HV000085: No value provided for parameter '%1$s' of annotation @%2$s."; private static final String getTryingToInstantiateAnnotationWithUnknownParametersException = "HV000086: Trying to instantiate %1$s with unknown parameter(s): %2$s."; private static final String getPropertyNameCannotBeNullOrEmptyException = "HV000087: Property name cannot be null or empty.";
/*   53 */   public final void version(String version) { this.log.logf(FQCN, Logger.Level.INFO, null, version$str(), version); }
/*      */   
/*      */   private static final String getElementTypeHasToBeFieldOrMethodException = "HV000088: Element type has to be FIELD or METHOD.";
/*      */   private static final String getMemberIsNeitherAFieldNorAMethodException = "HV000089: Member %s is neither a field nor a method."; private static final String getUnableToAccessMemberException = "HV000090: Unable to access %s."; private static final String getHasToBeAPrimitiveTypeException = "HV000091: %s has to be a primitive type."; private static final String getNullIsAnInvalidTypeForAConstraintValidatorException = "HV000093: null is an invalid type for a constraint validator."; private static final String getMissingActualTypeArgumentForTypeParameterException = "HV000094: Missing actual type argument for type parameter: %s."; private static final String getUnableToInstantiateConstraintFactoryClassException = "HV000095: Unable to instantiate constraint factory class %s."; private static final String getUnableToOpenInputStreamForMappingFileException = "HV000096: Unable to open input stream for mapping file %s."; private static final String getUnableToInstantiateMessageInterpolatorClassException = "HV000097: Unable to instantiate message interpolator class %s."; private static final String getUnableToInstantiateTraversableResolverClassException = "HV000098: Unable to instantiate traversable resolver class %s."; private static final String getUnableToInstantiateValidationProviderClassException = "HV000099: Unable to instantiate validation provider class %s."; private static final String getUnableToParseValidationXmlFileException = "HV000100: Unable to parse %s."; private static final String getIsNotAnAnnotationException = "HV000101: %s is not an annotation."; private static final String getIsNotAConstraintValidatorClassException = "HV000102: %s is not a constraint validator class."; private static final String getBeanClassHasAlreadyBeConfiguredInXmlException = "HV000103: %s is configured at least twice in xml."; private static final String getIsDefinedTwiceInMappingXmlForBeanException = "HV000104: %1$s is defined twice in mapping xml for bean %2$s."; private static final String getBeanDoesNotContainTheFieldException = "HV000105: %1$s does not contain the fieldType %2$s."; private static final String getBeanDoesNotContainThePropertyException = "HV000106: %1$s does not contain the property %2$s."; private static final String getAnnotationDoesNotContainAParameterException = "HV000107: Annotation of type %1$s does not contain a parameter %2$s."; private static final String getAttemptToSpecifyAnArrayWhereSingleValueIsExpectedException = "HV000108: Attempt to specify an array where single value is expected."; private static final String getUnexpectedParameterValueException = "HV000109: Unexpected parameter value."; private static final String getInvalidNumberFormatException = "HV000110: Invalid %s format."; private static final String getInvalidCharValueException = "HV000111: Invalid char value: %s."; private static final String getInvalidReturnTypeException = "HV000112: Invalid return type: %s. Should be a enumeration type."; private static final String getReservedParameterNamesException = "HV000113: %s, %s, %s are reserved parameter names."; private static final String getWrongPayloadClassException = "HV000114: Specified payload class %s does not implement javax.validation.Payload"; private static final String getErrorParsingMappingFileException = "HV000115: Error parsing mapping file."; private static final String getIllegalArgumentException = "HV000116: %s"; private static final String getUnableToNarrowNodeTypeException = "HV000118: Unable to cast %s (with element kind %s) to %s"; private static final String usingParameterNameProvider = "HV000119: Using %s as parameter name provider."; private static final String getUnableToInstantiateParameterNameProviderClassException = "HV000120: Unable to instantiate parameter name provider class %s."; private static final String getUnableToDetermineSchemaVersionException = "HV000121: Unable to parse %s."; private static final String getUnsupportedSchemaVersionException = "HV000122: Unsupported schema version for %s: %s."; private static final String getMultipleGroupConversionsForSameSourceException = "HV000124: Found multiple group conversions for source group %s: %s."; private static final String getGroupConversionOnNonCascadingElementException = "HV000125: Found group conversions for non-cascading element: %s.";
/*   57 */   protected String version$str() { return "HV000001: Hibernate Validator %s"; }
/*      */   
/*      */   private static final String getGroupConversionForSequenceException = "HV000127: Found group conversion using a group sequence as source: %s.";
/*      */   private static final String unknownPropertyInExpressionLanguage = "HV000129: EL expression '%s' references an unknown property"; private static final String errorInExpressionLanguage = "HV000130: Error in EL expression '%s'"; private static final String getMethodReturnValueMustNotBeMarkedMoreThanOnceForCascadedValidationException = "HV000131: A method return value must not be marked for cascaded validation more than once in a class hierarchy, but the following two methods are marked as such: %s, %s."; private static final String getVoidMethodsMustNotBeConstrainedException = "HV000132: Void methods must not be constrained or marked for cascaded validation, but method %s is."; private static final String getBeanDoesNotContainConstructorException = "HV000133: %1$s does not contain a constructor with the parameter types %2$s."; private static final String getInvalidParameterTypeException = "HV000134: Unable to load parameter of type '%1$s' in %2$s."; private static final String getBeanDoesNotContainMethodException = "HV000135: %1$s does not contain a method with the name '%2$s' and parameter types %3$s."; private static final String getUnableToLoadConstraintAnnotationClassException = "HV000136: The specified constraint annotation class %1$s cannot be loaded."; private static final String getMethodIsDefinedTwiceInMappingXmlForBeanException = "HV000137: The method '%1$s' is defined twice in the mapping xml for bean %2$s."; private static final String getConstructorIsDefinedTwiceInMappingXmlForBeanException = "HV000138: The constructor '%1$s' is defined twice in the mapping xml for bean %2$s."; private static final String getMultipleCrossParameterValidatorClassesException = "HV000139: The constraint '%1$s' defines multiple cross parameter validators. Only one is allowed."; private static final String getImplicitConstraintTargetInAmbiguousConfigurationException = "HV000141: The constraint %1$s used ConstraintTarget#IMPLICIT where the target cannot be inferred."; private static final String getCrossParameterConstraintOnMethodWithoutParametersException = "HV000142: Cross parameter constraint %1$s is illegally placed on a parameterless method or constructor '%2$s'."; private static final String getCrossParameterConstraintOnClassException = "HV000143: Cross parameter constraint %1$s is illegally placed on class level."; private static final String getCrossParameterConstraintOnFieldException = "HV000144: Cross parameter constraint %1$s is illegally placed on field '%2$s'.";
/*   61 */   public final void ignoringXmlConfiguration() { this.log.logf(FQCN, Logger.Level.INFO, null, ignoringXmlConfiguration$str(), new Object[0]); }
/*      */   
/*      */   private static final String getParameterNodeAddedForNonCrossParameterConstraintException = "HV000146: No parameter nodes may be added since path %s doesn't refer to a cross-parameter constraint.";
/*      */   private static final String getConstrainedElementConfiguredMultipleTimesException = "HV000147: %1$s is configured multiple times (note, <getter> and <method> nodes for the same method are not allowed)"; private static final String evaluatingExpressionLanguageExpressionCausedException = "HV000148: An exception occurred during evaluation of EL expression '%s'"; private static final String getExceptionOccurredDuringMessageInterpolationException = "HV000149: An exception occurred during message interpolation"; private static final String getMultipleValidatorsForSameTypeException = "HV000150: The constraint '%s' defines multiple validators for the type '%s'. Only one is allowed."; private static final String getParameterConfigurationAlteredInSubTypeException = "HV000151: A method overriding another method must not alter the parameter constraint configuration, but method %2$s changes the configuration of %1$s."; private static final String getParameterConstraintsDefinedInMethodsFromParallelTypesException = "HV000152: Two methods defined in parallel types must not declare parameter constraints, if they are overridden by the same method, but methods %s and %s both define parameter constraints."; private static final String getParametersOrReturnValueConstraintTargetGivenAtNonExecutableException = "HV000153: The constraint %1$s used ConstraintTarget#%2$s but is not specified on a method or constructor."; private static final String getCrossParameterConstraintHasNoValidatorException = "HV000154: Cross parameter constraint %1$s has no cross-parameter validator."; private static final String getComposedAndComposingConstraintsHaveDifferentTypesException = "HV000155: Composed and composing constraints must have the same constraint type, but composed constraint %1$s has type %3$s, while composing constraint %2$s has type %4$s."; private static final String getGenericAndCrossParameterConstraintDoesNotDefineValidationAppliesToParameterException = "HV000156: Constraints with generic as well as cross-parameter validators must define an attribute validationAppliesTo(), but constraint %s doesn't."; private static final String getValidationAppliesToParameterMustHaveReturnTypeConstraintTargetException = "HV000157: Return type of the attribute validationAppliesTo() of the constraint %s must be javax.validation.ConstraintTarget."; private static final String getValidationAppliesToParameterMustHaveDefaultValueImplicitException = "HV000158: Default value of the attribute validationAppliesTo() of the constraint %s must be ConstraintTarget#IMPLICIT."; private static final String getValidationAppliesToParameterMustNotBeDefinedForNonGenericAndCrossParameterConstraintException = "HV000159: Only constraints with generic as well as cross-parameter validators must define an attribute validationAppliesTo(), but constraint %s does.";
/*   65 */   protected String ignoringXmlConfiguration$str() { return "HV000002: Ignoring XML configuration."; }
/*      */   
/*      */   private static final String getValidatorForCrossParameterConstraintMustEitherValidateObjectOrObjectArrayException = "HV000160: Validator for cross-parameter constraint %s does not validate Object nor Object[].";
/*      */   private static final String getMethodsFromParallelTypesMustNotDefineGroupConversionsForCascadedReturnValueException = "HV000161: Two methods defined in parallel types must not define group conversions for a cascaded method return value, if they are overridden by the same method, but methods %s and %s both define parameter constraints."; private static final String getMethodOrConstructorNotDefinedByValidatedTypeException = "HV000162: The validated type %1$s does not specify the constructor/method: %2$s"; private static final String getParameterTypesDoNotMatchException = "HV000163: The actual parameter type '%1$s' is not assignable to the expected one '%2$s' for parameter %3$d of '%4$s'"; private static final String getHasToBeABoxedTypeException = "HV000164: %s has to be a auto-boxed type."; private static final String getMixingImplicitWithOtherExecutableTypesException = "HV000165: Mixing IMPLICIT and other executable types is not allowed."; private static final String getValidateOnExecutionOnOverriddenOrInterfaceMethodException = "HV000166: @ValidateOnExecution is not allowed on methods overriding a superclass method or implementing an interface. Check configuration for %1$s"; private static final String getOverridingConstraintDefinitionsInMultipleMappingFilesException = "HV000167: A given constraint definition can only be overridden in one mapping file. %1$s is overridden in multiple files"; private static final String getNonTerminatedParameterException = "HV000168: The message descriptor '%1$s' contains an unbalanced meta character '%2$c' parameter."; private static final String getNestedParameterException = "HV000169: The message descriptor '%1$s' has nested parameters."; private static final String getCreationOfScriptExecutorFailedException = "HV000170: No JSR-223 scripting engine could be bootstrapped for language \"%s\"."; private static final String getBeanClassHasAlreadyBeConfiguredViaProgrammaticApiException = "HV000171: %s is configured more than once via the programmatic constraint declaration API.";
/*   69 */   public final void usingConstraintFactory(String constraintFactoryClassName) { this.log.logf(FQCN, Logger.Level.INFO, null, usingConstraintFactory$str(), constraintFactoryClassName); }
/*      */   
/*      */   private static final String getPropertyHasAlreadyBeConfiguredViaProgrammaticApiException = "HV000172: Property \"%2$s\" of type %1$s is configured more than once via the programmatic constraint declaration API.";
/*      */   private static final String getMethodHasAlreadyBeConfiguredViaProgrammaticApiException = "HV000173: Method %2$s of type %1$s is configured more than once via the programmatic constraint declaration API."; private static final String getParameterHasAlreadyBeConfiguredViaProgrammaticApiException = "HV000174: Parameter %3$s of method or constructor %2$s of type %1$s is configured more than once via the programmatic constraint declaration API."; private static final String getReturnValueHasAlreadyBeConfiguredViaProgrammaticApiException = "HV000175: The return value of method or constructor %2$s of type %1$s is configured more than once via the programmatic constraint declaration API."; private static final String getConstructorHasAlreadyBeConfiguredViaProgrammaticApiException = "HV000176: Constructor %2$s of type %1$s is configured more than once via the programmatic constraint declaration API."; private static final String getCrossParameterElementHasAlreadyBeConfiguredViaProgrammaticApiException = "HV000177: Cross-parameter constraints for the method or constructor %2$s of type %1$s are declared more than once via the programmatic constraint declaration API."; private static final String getMultiplierCannotBeNegativeException = "HV000178: Multiplier cannot be negative: %d."; private static final String getWeightCannotBeNegativeException = "HV000179: Weight cannot be negative: %d."; private static final String getTreatCheckAsIsNotADigitNorALetterException = "HV000180: '%c' is not a digit nor a letter."; private static final String getInvalidParameterCountForExecutableException = "HV000181: Wrong number of parameters. Method or constructor %1$s expects %2$d parameters, but got %3$d."; private static final String getNoUnwrapperFoundForTypeException = "HV000182: No validation value unwrapper is registered for type '%1$s'."; private static final String getUnableToInitializeELExpressionFactoryException = "HV000183: Unable to initialize 'javax.el.ExpressionFactory'. Check that you have the EL dependencies on the classpath, or use ParameterMessageInterpolator instead";
/*   73 */   protected String usingConstraintFactory$str() { return "HV000003: Using %s as constraint factory."; }
/*      */   
/*      */   private static final String creationOfParameterMessageInterpolation = "HV000184: ParameterMessageInterpolator has been chosen, EL interpolation will not be supported";
/*      */   private static final String getElUnsupported = "HV000185: Message contains EL expression: %1s, which is unsupported with chosen Interpolator";
/*   77 */   private static final String getConstraintValidatorExistsForWrapperAndWrappedValueException = "HV000186: The constraint of type '%2$s' defined on '%1$s' has multiple matching constraint validators which is due to an additional value handler of type '%3$s'. It is unclear which value needs validating. Clarify configuration via @UnwrapValidatedValue."; private static final String getTypeAnnotationConstraintOnIterableRequiresUseOfValidAnnotationException = "HV000187: When using type annotation constraints on parameterized iterables or map @Valid must be used. Check %s#%s"; private static final String parameterizedTypeWithMoreThanOneTypeArgumentIsNotSupported = "HV000188: Parameterized type with more than one argument is not supported: %s"; private static final String getInconsistentValueUnwrappingConfigurationBetweenFieldAndItsGetterException = "HV000189: The configuration of value unwrapping for property '%s' of bean '%s' is inconsistent between the field and its getter."; private static final String getUnableToCreateXMLEventReader = "HV000190: Unable to parse %s."; private static final String validatedValueUnwrapperCannotBeCreated = "HV000191: Error creating unwrapper: %s"; private static final String unknownJvmVersion = "HV000192: Couldn't determine Java version from value %1s; Not enabling features requiring Java 8"; private static final String getConstraintHasAlreadyBeenConfiguredViaProgrammaticApiException = "HV000193: %s is configured more than once via the programmatic constraint definition API."; private static final String getEmptyElementOnlySupportedWhenCharSequenceIsExpectedExpection = "HV000194: An empty element is only supported when a CharSequence is expected."; private static final String getUnableToReachPropertyToValidateException = "HV000195: Unable to reach the property to validate for the bean %s and the property path %s. A property is null along the way."; private static final String getUnableToConvertTypeToClassException = "HV000196: Unable to convert the Type %s to a Class."; public final void usingMessageInterpolator(String messageInterpolatorClassName) { this.log.logf(FQCN, Logger.Level.INFO, null, usingMessageInterpolator$str(), messageInterpolatorClassName); }
/*      */   
/*      */ 
/*      */   protected String usingMessageInterpolator$str() {
/*   81 */     return "HV000004: Using %s as message interpolator.";
/*      */   }
/*      */   
/*      */   public final void usingTraversableResolver(String traversableResolverClassName) {
/*   85 */     this.log.logf(FQCN, Logger.Level.INFO, null, usingTraversableResolver$str(), traversableResolverClassName);
/*      */   }
/*      */   
/*      */   protected String usingTraversableResolver$str() {
/*   89 */     return "HV000005: Using %s as traversable resolver.";
/*      */   }
/*      */   
/*      */   public final void usingValidationProvider(String validationProviderClassName) {
/*   93 */     this.log.logf(FQCN, Logger.Level.INFO, null, usingValidationProvider$str(), validationProviderClassName);
/*      */   }
/*      */   
/*      */   protected String usingValidationProvider$str() {
/*   97 */     return "HV000006: Using %s as validation provider.";
/*      */   }
/*      */   
/*      */   public final void parsingXMLFile(String fileName) {
/*  101 */     this.log.logf(FQCN, Logger.Level.INFO, null, parsingXMLFile$str(), fileName);
/*      */   }
/*      */   
/*      */   protected String parsingXMLFile$str() {
/*  105 */     return "HV000007: %s found. Parsing XML based configuration.";
/*      */   }
/*      */   
/*      */   public final void unableToCloseInputStream() {
/*  109 */     this.log.logf(FQCN, Logger.Level.WARN, null, unableToCloseInputStream$str(), new Object[0]);
/*      */   }
/*      */   
/*      */   protected String unableToCloseInputStream$str() {
/*  113 */     return "HV000008: Unable to close input stream.";
/*      */   }
/*      */   
/*      */   public final void unableToCloseXMLFileInputStream(String fileName) {
/*  117 */     this.log.logf(FQCN, Logger.Level.WARN, null, unableToCloseXMLFileInputStream$str(), fileName);
/*      */   }
/*      */   
/*      */   protected String unableToCloseXMLFileInputStream$str() {
/*  121 */     return "HV000010: Unable to close input stream for %s.";
/*      */   }
/*      */   
/*      */   public final void unableToCreateSchema(String fileName, String message) {
/*  125 */     this.log.logf(FQCN, Logger.Level.WARN, null, unableToCreateSchema$str(), fileName, message);
/*      */   }
/*      */   
/*      */   protected String unableToCreateSchema$str() {
/*  129 */     return "HV000011: Unable to create schema for %1$s: %2$s";
/*      */   }
/*      */   
/*      */   protected String getUnableToCreateAnnotationForConfiguredConstraintException$str() {
/*  133 */     return "HV000012: Unable to create annotation for configured constraint";
/*      */   }
/*      */   
/*      */   public final ValidationException getUnableToCreateAnnotationForConfiguredConstraintException(RuntimeException e) {
/*  137 */     ValidationException result = new ValidationException(String.format(getUnableToCreateAnnotationForConfiguredConstraintException$str(), new Object[0]), e);
/*  138 */     StackTraceElement[] st = result.getStackTrace();
/*  139 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  140 */     return result;
/*      */   }
/*      */   
/*      */   protected String getUnableToFindPropertyWithAccessException$str() {
/*  144 */     return "HV000013: The class %1$s does not have a property '%2$s' with access %3$s.";
/*      */   }
/*      */   
/*      */   public final ValidationException getUnableToFindPropertyWithAccessException(Class<? extends Object> beanClass, String property, ElementType elementType) {
/*  148 */     ValidationException result = new ValidationException(String.format(getUnableToFindPropertyWithAccessException$str(), new Object[] { beanClass, property, elementType }));
/*  149 */     StackTraceElement[] st = result.getStackTrace();
/*  150 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  151 */     return result;
/*      */   }
/*      */   
/*      */   protected String getUnableToFindMethodException$str() {
/*  155 */     return "HV000014: Type %1$s doesn't have a method %2$s.";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getUnableToFindMethodException(Class<? extends Object> beanClass, String method) {
/*  159 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getUnableToFindMethodException$str(), new Object[] { beanClass, method }));
/*  160 */     StackTraceElement[] st = result.getStackTrace();
/*  161 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  162 */     return result;
/*      */   }
/*      */   
/*      */   protected String getInvalidBigDecimalFormatException$str() {
/*  166 */     return "HV000016: %s does not represent a valid BigDecimal format.";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getInvalidBigDecimalFormatException(String value, NumberFormatException e) {
/*  170 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getInvalidBigDecimalFormatException$str(), new Object[] { value }), e);
/*  171 */     StackTraceElement[] st = result.getStackTrace();
/*  172 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  173 */     return result;
/*      */   }
/*      */   
/*      */   protected String getInvalidLengthForIntegerPartException$str() {
/*  177 */     return "HV000017: The length of the integer part cannot be negative.";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getInvalidLengthForIntegerPartException() {
/*  181 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getInvalidLengthForIntegerPartException$str(), new Object[0]));
/*  182 */     StackTraceElement[] st = result.getStackTrace();
/*  183 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  184 */     return result;
/*      */   }
/*      */   
/*      */   protected String getInvalidLengthForFractionPartException$str() {
/*  188 */     return "HV000018: The length of the fraction part cannot be negative.";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getInvalidLengthForFractionPartException() {
/*  192 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getInvalidLengthForFractionPartException$str(), new Object[0]));
/*  193 */     StackTraceElement[] st = result.getStackTrace();
/*  194 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  195 */     return result;
/*      */   }
/*      */   
/*      */   protected String getMinCannotBeNegativeException$str() {
/*  199 */     return "HV000019: The min parameter cannot be negative.";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getMinCannotBeNegativeException() {
/*  203 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getMinCannotBeNegativeException$str(), new Object[0]));
/*  204 */     StackTraceElement[] st = result.getStackTrace();
/*  205 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  206 */     return result;
/*      */   }
/*      */   
/*      */   protected String getMaxCannotBeNegativeException$str() {
/*  210 */     return "HV000020: The max parameter cannot be negative.";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getMaxCannotBeNegativeException() {
/*  214 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getMaxCannotBeNegativeException$str(), new Object[0]));
/*  215 */     StackTraceElement[] st = result.getStackTrace();
/*  216 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  217 */     return result;
/*      */   }
/*      */   
/*      */   protected String getLengthCannotBeNegativeException$str() {
/*  221 */     return "HV000021: The length cannot be negative.";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getLengthCannotBeNegativeException() {
/*  225 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getLengthCannotBeNegativeException$str(), new Object[0]));
/*  226 */     StackTraceElement[] st = result.getStackTrace();
/*  227 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  228 */     return result;
/*      */   }
/*      */   
/*      */   protected String getInvalidRegularExpressionException$str() {
/*  232 */     return "HV000022: Invalid regular expression.";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getInvalidRegularExpressionException(PatternSyntaxException e) {
/*  236 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getInvalidRegularExpressionException$str(), new Object[0]), e);
/*  237 */     StackTraceElement[] st = result.getStackTrace();
/*  238 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  239 */     return result;
/*      */   }
/*      */   
/*      */   protected String getErrorDuringScriptExecutionException$str() {
/*  243 */     return "HV000023: Error during execution of script \"%s\" occurred.";
/*      */   }
/*      */   
/*      */   public final ConstraintDeclarationException getErrorDuringScriptExecutionException(String script, Exception e) {
/*  247 */     ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getErrorDuringScriptExecutionException$str(), new Object[] { script }), e);
/*  248 */     StackTraceElement[] st = result.getStackTrace();
/*  249 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  250 */     return result;
/*      */   }
/*      */   
/*      */   protected String getScriptMustReturnTrueOrFalseException1$str() {
/*  254 */     return "HV000024: Script \"%s\" returned null, but must return either true or false.";
/*      */   }
/*      */   
/*      */   public final ConstraintDeclarationException getScriptMustReturnTrueOrFalseException(String script) {
/*  258 */     ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getScriptMustReturnTrueOrFalseException1$str(), new Object[] { script }));
/*  259 */     StackTraceElement[] st = result.getStackTrace();
/*  260 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  261 */     return result;
/*      */   }
/*      */   
/*      */   protected String getScriptMustReturnTrueOrFalseException3$str() {
/*  265 */     return "HV000025: Script \"%1$s\" returned %2$s (of type %3$s), but must return either true or false.";
/*      */   }
/*      */   
/*      */   public final ConstraintDeclarationException getScriptMustReturnTrueOrFalseException(String script, Object executionResult, String type) {
/*  269 */     ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getScriptMustReturnTrueOrFalseException3$str(), new Object[] { script, executionResult, type }));
/*  270 */     StackTraceElement[] st = result.getStackTrace();
/*  271 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  272 */     return result;
/*      */   }
/*      */   
/*      */   protected String getInconsistentConfigurationException$str() {
/*  276 */     return "HV000026: Assertion error: inconsistent ConfigurationImpl construction.";
/*      */   }
/*      */   
/*      */   public final ValidationException getInconsistentConfigurationException() {
/*  280 */     ValidationException result = new ValidationException(String.format(getInconsistentConfigurationException$str(), new Object[0]));
/*  281 */     StackTraceElement[] st = result.getStackTrace();
/*  282 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  283 */     return result;
/*      */   }
/*      */   
/*      */   protected String getUnableToFindProviderException$str() {
/*  287 */     return "HV000027: Unable to find provider: %s.";
/*      */   }
/*      */   
/*      */   public final ValidationException getUnableToFindProviderException(Class<? extends Object> providerClass) {
/*  291 */     ValidationException result = new ValidationException(String.format(getUnableToFindProviderException$str(), new Object[] { providerClass }));
/*  292 */     StackTraceElement[] st = result.getStackTrace();
/*  293 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  294 */     return result;
/*      */   }
/*      */   
/*      */   protected String getExceptionDuringIsValidCallException$str() {
/*  298 */     return "HV000028: Unexpected exception during isValid call.";
/*      */   }
/*      */   
/*      */   public final ValidationException getExceptionDuringIsValidCallException(RuntimeException e) {
/*  302 */     ValidationException result = new ValidationException(String.format(getExceptionDuringIsValidCallException$str(), new Object[0]), e);
/*  303 */     StackTraceElement[] st = result.getStackTrace();
/*  304 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  305 */     return result;
/*      */   }
/*      */   
/*      */   protected String getConstraintFactoryMustNotReturnNullException$str() {
/*  309 */     return "HV000029: Constraint factory returned null when trying to create instance of %s.";
/*      */   }
/*      */   
/*      */   public final ValidationException getConstraintFactoryMustNotReturnNullException(String validatorClassName) {
/*  313 */     ValidationException result = new ValidationException(String.format(getConstraintFactoryMustNotReturnNullException$str(), new Object[] { validatorClassName }));
/*  314 */     StackTraceElement[] st = result.getStackTrace();
/*  315 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  316 */     return result;
/*      */   }
/*      */   
/*      */   protected String getNoValidatorFoundForTypeException$str() {
/*  320 */     return "HV000030: No validator could be found for constraint '%s' validating type '%s'. Check configuration for '%s'";
/*      */   }
/*      */   
/*      */   public final UnexpectedTypeException getNoValidatorFoundForTypeException(String constraintType, String validatedValueType, String path) {
/*  324 */     UnexpectedTypeException result = new UnexpectedTypeException(String.format(getNoValidatorFoundForTypeException$str(), new Object[] { constraintType, validatedValueType, path }));
/*  325 */     StackTraceElement[] st = result.getStackTrace();
/*  326 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  327 */     return result;
/*      */   }
/*      */   
/*      */   protected String getMoreThanOneValidatorFoundForTypeException$str() {
/*  331 */     return "HV000031: There are multiple validator classes which could validate the type %1$s. The validator classes are: %2$s.";
/*      */   }
/*      */   
/*      */   public final UnexpectedTypeException getMoreThanOneValidatorFoundForTypeException(Type type, String validatorClasses) {
/*  335 */     UnexpectedTypeException result = new UnexpectedTypeException(String.format(getMoreThanOneValidatorFoundForTypeException$str(), new Object[] { type, validatorClasses }));
/*  336 */     StackTraceElement[] st = result.getStackTrace();
/*  337 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  338 */     return result;
/*      */   }
/*      */   
/*      */   protected String getUnableToInitializeConstraintValidatorException$str() {
/*  342 */     return "HV000032: Unable to initialize %s.";
/*      */   }
/*      */   
/*      */   public final ValidationException getUnableToInitializeConstraintValidatorException(String validatorClassName, RuntimeException e) {
/*  346 */     ValidationException result = new ValidationException(String.format(getUnableToInitializeConstraintValidatorException$str(), new Object[] { validatorClassName }), e);
/*  347 */     StackTraceElement[] st = result.getStackTrace();
/*  348 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  349 */     return result;
/*      */   }
/*      */   
/*      */   protected String getAtLeastOneCustomMessageMustBeCreatedException$str() {
/*  353 */     return "HV000033: At least one custom message must be created if the default error message gets disabled.";
/*      */   }
/*      */   
/*      */   public final ValidationException getAtLeastOneCustomMessageMustBeCreatedException() {
/*  357 */     ValidationException result = new ValidationException(String.format(getAtLeastOneCustomMessageMustBeCreatedException$str(), new Object[0]));
/*  358 */     StackTraceElement[] st = result.getStackTrace();
/*  359 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  360 */     return result;
/*      */   }
/*      */   
/*      */   protected String getInvalidJavaIdentifierException$str() {
/*  364 */     return "HV000034: %s is not a valid Java Identifier.";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getInvalidJavaIdentifierException(String identifier) {
/*  368 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getInvalidJavaIdentifierException$str(), new Object[] { identifier }));
/*  369 */     StackTraceElement[] st = result.getStackTrace();
/*  370 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  371 */     return result;
/*      */   }
/*      */   
/*      */   protected String getUnableToParsePropertyPathException$str() {
/*  375 */     return "HV000035: Unable to parse property path %s.";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getUnableToParsePropertyPathException(String propertyPath) {
/*  379 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getUnableToParsePropertyPathException$str(), new Object[] { propertyPath }));
/*  380 */     StackTraceElement[] st = result.getStackTrace();
/*  381 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  382 */     return result;
/*      */   }
/*      */   
/*      */   protected String getTypeNotSupportedForUnwrappingException$str() {
/*  386 */     return "HV000036: Type %s not supported for unwrapping.";
/*      */   }
/*      */   
/*      */   public final ValidationException getTypeNotSupportedForUnwrappingException(Class<? extends Object> type) {
/*  390 */     ValidationException result = new ValidationException(String.format(getTypeNotSupportedForUnwrappingException$str(), new Object[] { type }));
/*  391 */     StackTraceElement[] st = result.getStackTrace();
/*  392 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  393 */     return result;
/*      */   }
/*      */   
/*      */   protected String getInconsistentFailFastConfigurationException$str() {
/*  397 */     return "HV000037: Inconsistent fail fast configuration. Fail fast enabled via programmatic API, but explicitly disabled via properties.";
/*      */   }
/*      */   
/*      */   public final ValidationException getInconsistentFailFastConfigurationException() {
/*  401 */     ValidationException result = new ValidationException(String.format(getInconsistentFailFastConfigurationException$str(), new Object[0]));
/*  402 */     StackTraceElement[] st = result.getStackTrace();
/*  403 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  404 */     return result;
/*      */   }
/*      */   
/*      */   protected String getInvalidPropertyPathException0$str() {
/*  408 */     return "HV000038: Invalid property path.";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getInvalidPropertyPathException() {
/*  412 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getInvalidPropertyPathException0$str(), new Object[0]));
/*  413 */     StackTraceElement[] st = result.getStackTrace();
/*  414 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  415 */     return result;
/*      */   }
/*      */   
/*      */   protected String getInvalidPropertyPathException2$str() {
/*  419 */     return "HV000039: Invalid property path. Either there is no property %2$s in entity %1$s or it is not possible to cascade to the property.";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getInvalidPropertyPathException(String beanClassName, String propertyName) {
/*  423 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getInvalidPropertyPathException2$str(), new Object[] { beanClassName, propertyName }));
/*  424 */     StackTraceElement[] st = result.getStackTrace();
/*  425 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  426 */     return result;
/*      */   }
/*      */   
/*      */   protected String getPropertyPathMustProvideIndexOrMapKeyException$str() {
/*  430 */     return "HV000040: Property path must provide index or map key.";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getPropertyPathMustProvideIndexOrMapKeyException() {
/*  434 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getPropertyPathMustProvideIndexOrMapKeyException$str(), new Object[0]));
/*  435 */     StackTraceElement[] st = result.getStackTrace();
/*  436 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  437 */     return result;
/*      */   }
/*      */   
/*      */   protected String getErrorDuringCallOfTraversableResolverIsReachableException$str() {
/*  441 */     return "HV000041: Call to TraversableResolver.isReachable() threw an exception.";
/*      */   }
/*      */   
/*      */   public final ValidationException getErrorDuringCallOfTraversableResolverIsReachableException(RuntimeException e) {
/*  445 */     ValidationException result = new ValidationException(String.format(getErrorDuringCallOfTraversableResolverIsReachableException$str(), new Object[0]), e);
/*  446 */     StackTraceElement[] st = result.getStackTrace();
/*  447 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  448 */     return result;
/*      */   }
/*      */   
/*      */   protected String getErrorDuringCallOfTraversableResolverIsCascadableException$str() {
/*  452 */     return "HV000042: Call to TraversableResolver.isCascadable() threw an exception.";
/*      */   }
/*      */   
/*      */   public final ValidationException getErrorDuringCallOfTraversableResolverIsCascadableException(RuntimeException e) {
/*  456 */     ValidationException result = new ValidationException(String.format(getErrorDuringCallOfTraversableResolverIsCascadableException$str(), new Object[0]), e);
/*  457 */     StackTraceElement[] st = result.getStackTrace();
/*  458 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  459 */     return result;
/*      */   }
/*      */   
/*      */   protected String getUnableToExpandDefaultGroupListException$str() {
/*  463 */     return "HV000043: Unable to expand default group list %1$s into sequence %2$s.";
/*      */   }
/*      */   
/*      */   public final GroupDefinitionException getUnableToExpandDefaultGroupListException(List<? extends Object> defaultGroupList, List<? extends Object> groupList) {
/*  467 */     GroupDefinitionException result = new GroupDefinitionException(String.format(getUnableToExpandDefaultGroupListException$str(), new Object[] { defaultGroupList, groupList }));
/*  468 */     StackTraceElement[] st = result.getStackTrace();
/*  469 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  470 */     return result;
/*      */   }
/*      */   
/*      */   protected String getAtLeastOneGroupHasToBeSpecifiedException$str() {
/*  474 */     return "HV000044: At least one group has to be specified.";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getAtLeastOneGroupHasToBeSpecifiedException() {
/*  478 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getAtLeastOneGroupHasToBeSpecifiedException$str(), new Object[0]));
/*  479 */     StackTraceElement[] st = result.getStackTrace();
/*  480 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  481 */     return result;
/*      */   }
/*      */   
/*      */   protected String getGroupHasToBeAnInterfaceException$str() {
/*  485 */     return "HV000045: A group has to be an interface. %s is not.";
/*      */   }
/*      */   
/*      */   public final ValidationException getGroupHasToBeAnInterfaceException(String className) {
/*  489 */     ValidationException result = new ValidationException(String.format(getGroupHasToBeAnInterfaceException$str(), new Object[] { className }));
/*  490 */     StackTraceElement[] st = result.getStackTrace();
/*  491 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  492 */     return result;
/*      */   }
/*      */   
/*      */   protected String getSequenceDefinitionsNotAllowedException$str() {
/*  496 */     return "HV000046: Sequence definitions are not allowed as composing parts of a sequence.";
/*      */   }
/*      */   
/*      */   public final GroupDefinitionException getSequenceDefinitionsNotAllowedException() {
/*  500 */     GroupDefinitionException result = new GroupDefinitionException(String.format(getSequenceDefinitionsNotAllowedException$str(), new Object[0]));
/*  501 */     StackTraceElement[] st = result.getStackTrace();
/*  502 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  503 */     return result;
/*      */   }
/*      */   
/*      */   protected String getCyclicDependencyInGroupsDefinitionException$str() {
/*  507 */     return "HV000047: Cyclic dependency in groups definition";
/*      */   }
/*      */   
/*      */   public final GroupDefinitionException getCyclicDependencyInGroupsDefinitionException() {
/*  511 */     GroupDefinitionException result = new GroupDefinitionException(String.format(getCyclicDependencyInGroupsDefinitionException$str(), new Object[0]));
/*  512 */     StackTraceElement[] st = result.getStackTrace();
/*  513 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  514 */     return result;
/*      */   }
/*      */   
/*      */   protected String getUnableToExpandGroupSequenceException$str() {
/*  518 */     return "HV000048: Unable to expand group sequence.";
/*      */   }
/*      */   
/*      */   public final GroupDefinitionException getUnableToExpandGroupSequenceException() {
/*  522 */     GroupDefinitionException result = new GroupDefinitionException(String.format(getUnableToExpandGroupSequenceException$str(), new Object[0]));
/*  523 */     StackTraceElement[] st = result.getStackTrace();
/*  524 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  525 */     return result;
/*      */   }
/*      */   
/*      */   protected String getInvalidDefaultGroupSequenceDefinitionException$str() {
/*  529 */     return "HV000052: Default group sequence and default group sequence provider cannot be defined at the same time.";
/*      */   }
/*      */   
/*      */   public final GroupDefinitionException getInvalidDefaultGroupSequenceDefinitionException() {
/*  533 */     GroupDefinitionException result = new GroupDefinitionException(String.format(getInvalidDefaultGroupSequenceDefinitionException$str(), new Object[0]));
/*  534 */     StackTraceElement[] st = result.getStackTrace();
/*  535 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  536 */     return result;
/*      */   }
/*      */   
/*      */   protected String getNoDefaultGroupInGroupSequenceException$str() {
/*  540 */     return "HV000053: 'Default.class' cannot appear in default group sequence list.";
/*      */   }
/*      */   
/*      */   public final GroupDefinitionException getNoDefaultGroupInGroupSequenceException() {
/*  544 */     GroupDefinitionException result = new GroupDefinitionException(String.format(getNoDefaultGroupInGroupSequenceException$str(), new Object[0]));
/*  545 */     StackTraceElement[] st = result.getStackTrace();
/*  546 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  547 */     return result;
/*      */   }
/*      */   
/*      */   protected String getBeanClassMustBePartOfRedefinedDefaultGroupSequenceException$str() {
/*  551 */     return "HV000054: %s must be part of the redefined default group sequence.";
/*      */   }
/*      */   
/*      */   public final GroupDefinitionException getBeanClassMustBePartOfRedefinedDefaultGroupSequenceException(String beanClassName) {
/*  555 */     GroupDefinitionException result = new GroupDefinitionException(String.format(getBeanClassMustBePartOfRedefinedDefaultGroupSequenceException$str(), new Object[] { beanClassName }));
/*  556 */     StackTraceElement[] st = result.getStackTrace();
/*  557 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  558 */     return result;
/*      */   }
/*      */   
/*      */   protected String getWrongDefaultGroupSequenceProviderTypeException$str() {
/*  562 */     return "HV000055: The default group sequence provider defined for %s has the wrong type";
/*      */   }
/*      */   
/*      */   public final GroupDefinitionException getWrongDefaultGroupSequenceProviderTypeException(String beanClassName) {
/*  566 */     GroupDefinitionException result = new GroupDefinitionException(String.format(getWrongDefaultGroupSequenceProviderTypeException$str(), new Object[] { beanClassName }));
/*  567 */     StackTraceElement[] st = result.getStackTrace();
/*  568 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  569 */     return result;
/*      */   }
/*      */   
/*      */   protected String getInvalidExecutableParameterIndexException$str() {
/*  573 */     return "HV000056: Method or constructor %1$s doesn't have a parameter with index %2$d.";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getInvalidExecutableParameterIndexException(String executable, int index) {
/*  577 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getInvalidExecutableParameterIndexException$str(), new Object[] { executable, Integer.valueOf(index) }));
/*  578 */     StackTraceElement[] st = result.getStackTrace();
/*  579 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  580 */     return result;
/*      */   }
/*      */   
/*      */   protected String getUnableToRetrieveAnnotationParameterValueException$str() {
/*  584 */     return "HV000059: Unable to retrieve annotation parameter value.";
/*      */   }
/*      */   
/*      */   public final ValidationException getUnableToRetrieveAnnotationParameterValueException(Exception e) {
/*  588 */     ValidationException result = new ValidationException(String.format(getUnableToRetrieveAnnotationParameterValueException$str(), new Object[0]), e);
/*  589 */     StackTraceElement[] st = result.getStackTrace();
/*  590 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  591 */     return result;
/*      */   }
/*      */   
/*      */   protected String getInvalidLengthOfParameterMetaDataListException$str() {
/*  595 */     return "HV000062: Method or constructor %1$s has %2$s parameters, but the passed list of parameter meta data has a size of %3$s.";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getInvalidLengthOfParameterMetaDataListException(String executableName, int nbParameters, int listSize) {
/*  599 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getInvalidLengthOfParameterMetaDataListException$str(), new Object[] { executableName, Integer.valueOf(nbParameters), Integer.valueOf(listSize) }));
/*  600 */     StackTraceElement[] st = result.getStackTrace();
/*  601 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  602 */     return result;
/*      */   }
/*      */   
/*      */   protected String getUnableToInstantiateException1$str() {
/*  606 */     return "HV000063: Unable to instantiate %s.";
/*      */   }
/*      */   
/*      */   public final ValidationException getUnableToInstantiateException(String className, Exception e) {
/*  610 */     ValidationException result = new ValidationException(String.format(getUnableToInstantiateException1$str(), new Object[] { className }), e);
/*  611 */     StackTraceElement[] st = result.getStackTrace();
/*  612 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  613 */     return result;
/*      */   }
/*      */   
/*      */   public final ValidationException getUnableToInstantiateException(Class<? extends Object> clazz, Exception e) {
/*  617 */     ValidationException result = new ValidationException(String.format(getUnableToInstantiateException1$str(), new Object[] { clazz }), e);
/*  618 */     StackTraceElement[] st = result.getStackTrace();
/*  619 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  620 */     return result;
/*      */   }
/*      */   
/*      */   protected String getUnableToInstantiateException2$str() {
/*  624 */     return "HV000064: Unable to instantiate %1$s: %2$s.";
/*      */   }
/*      */   
/*      */   public final ValidationException getUnableToInstantiateException(String message, Class<? extends Object> clazz, Exception e) {
/*  628 */     ValidationException result = new ValidationException(String.format(getUnableToInstantiateException2$str(), new Object[] { message, clazz }), e);
/*  629 */     StackTraceElement[] st = result.getStackTrace();
/*  630 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  631 */     return result;
/*      */   }
/*      */   
/*      */   protected String getUnableToLoadClassException$str() {
/*  635 */     return "HV000065: Unable to load class: %s from %s.";
/*      */   }
/*      */   
/*      */   public final ValidationException getUnableToLoadClassException(String className, ClassLoader loader, Exception e) {
/*  639 */     ValidationException result = new ValidationException(String.format(getUnableToLoadClassException$str(), new Object[] { className, loader }), e);
/*  640 */     StackTraceElement[] st = result.getStackTrace();
/*  641 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  642 */     return result;
/*      */   }
/*      */   
/*      */   protected String getStartIndexCannotBeNegativeException$str() {
/*  646 */     return "HV000068: Start index cannot be negative: %d.";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getStartIndexCannotBeNegativeException(int startIndex) {
/*  650 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getStartIndexCannotBeNegativeException$str(), new Object[] { Integer.valueOf(startIndex) }));
/*  651 */     StackTraceElement[] st = result.getStackTrace();
/*  652 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  653 */     return result;
/*      */   }
/*      */   
/*      */   protected String getEndIndexCannotBeNegativeException$str() {
/*  657 */     return "HV000069: End index cannot be negative: %d.";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getEndIndexCannotBeNegativeException(int endIndex) {
/*  661 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getEndIndexCannotBeNegativeException$str(), new Object[] { Integer.valueOf(endIndex) }));
/*  662 */     StackTraceElement[] st = result.getStackTrace();
/*  663 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  664 */     return result;
/*      */   }
/*      */   
/*      */   protected String getInvalidRangeException$str() {
/*  668 */     return "HV000070: Invalid Range: %1$d > %2$d.";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getInvalidRangeException(int startIndex, int endIndex) {
/*  672 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getInvalidRangeException$str(), new Object[] { Integer.valueOf(startIndex), Integer.valueOf(endIndex) }));
/*  673 */     StackTraceElement[] st = result.getStackTrace();
/*  674 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  675 */     return result;
/*      */   }
/*      */   
/*      */   protected String getInvalidCheckDigitException$str() {
/*  679 */     return "HV000071: A explicitly specified check digit must lie outside the interval: [%1$d, %2$d].";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getInvalidCheckDigitException(int startIndex, int endIndex) {
/*  683 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getInvalidCheckDigitException$str(), new Object[] { Integer.valueOf(startIndex), Integer.valueOf(endIndex) }));
/*  684 */     StackTraceElement[] st = result.getStackTrace();
/*  685 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  686 */     return result;
/*      */   }
/*      */   
/*      */   protected String getCharacterIsNotADigitException$str() {
/*  690 */     return "HV000072: '%c' is not a digit.";
/*      */   }
/*      */   
/*      */   public final NumberFormatException getCharacterIsNotADigitException(char c) {
/*  694 */     NumberFormatException result = new NumberFormatException(String.format(getCharacterIsNotADigitException$str(), new Object[] { Character.valueOf(c) }));
/*  695 */     StackTraceElement[] st = result.getStackTrace();
/*  696 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  697 */     return result;
/*      */   }
/*      */   
/*      */   protected String getConstraintParametersCannotStartWithValidException$str() {
/*  701 */     return "HV000073: Parameters starting with 'valid' are not allowed in a constraint.";
/*      */   }
/*      */   
/*      */   public final ConstraintDefinitionException getConstraintParametersCannotStartWithValidException() {
/*  705 */     ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getConstraintParametersCannotStartWithValidException$str(), new Object[0]));
/*  706 */     StackTraceElement[] st = result.getStackTrace();
/*  707 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  708 */     return result;
/*      */   }
/*      */   
/*      */   protected String getConstraintWithoutMandatoryParameterException$str() {
/*  712 */     return "HV000074: %2$s contains Constraint annotation, but does not contain a %1$s parameter.";
/*      */   }
/*      */   
/*      */   public final ConstraintDefinitionException getConstraintWithoutMandatoryParameterException(String parameterName, String constraintName) {
/*  716 */     ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getConstraintWithoutMandatoryParameterException$str(), new Object[] { parameterName, constraintName }));
/*  717 */     StackTraceElement[] st = result.getStackTrace();
/*  718 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  719 */     return result;
/*      */   }
/*      */   
/*      */   protected String getWrongDefaultValueForPayloadParameterException$str() {
/*  723 */     return "HV000075: %s contains Constraint annotation, but the payload parameter default value is not the empty array.";
/*      */   }
/*      */   
/*      */   public final ConstraintDefinitionException getWrongDefaultValueForPayloadParameterException(String constraintName) {
/*  727 */     ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getWrongDefaultValueForPayloadParameterException$str(), new Object[] { constraintName }));
/*  728 */     StackTraceElement[] st = result.getStackTrace();
/*  729 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  730 */     return result;
/*      */   }
/*      */   
/*      */   protected String getWrongTypeForPayloadParameterException$str() {
/*  734 */     return "HV000076: %s contains Constraint annotation, but the payload parameter is of wrong type.";
/*      */   }
/*      */   
/*      */   public final ConstraintDefinitionException getWrongTypeForPayloadParameterException(String constraintName, ClassCastException e) {
/*  738 */     ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getWrongTypeForPayloadParameterException$str(), new Object[] { constraintName }), e);
/*  739 */     StackTraceElement[] st = result.getStackTrace();
/*  740 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  741 */     return result;
/*      */   }
/*      */   
/*      */   protected String getWrongDefaultValueForGroupsParameterException$str() {
/*  745 */     return "HV000077: %s contains Constraint annotation, but the groups parameter default value is not the empty array.";
/*      */   }
/*      */   
/*      */   public final ConstraintDefinitionException getWrongDefaultValueForGroupsParameterException(String constraintName) {
/*  749 */     ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getWrongDefaultValueForGroupsParameterException$str(), new Object[] { constraintName }));
/*  750 */     StackTraceElement[] st = result.getStackTrace();
/*  751 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  752 */     return result;
/*      */   }
/*      */   
/*      */   protected String getWrongTypeForGroupsParameterException$str() {
/*  756 */     return "HV000078: %s contains Constraint annotation, but the groups parameter is of wrong type.";
/*      */   }
/*      */   
/*      */   public final ConstraintDefinitionException getWrongTypeForGroupsParameterException(String constraintName, ClassCastException e) {
/*  760 */     ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getWrongTypeForGroupsParameterException$str(), new Object[] { constraintName }), e);
/*  761 */     StackTraceElement[] st = result.getStackTrace();
/*  762 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  763 */     return result;
/*      */   }
/*      */   
/*      */   protected String getWrongTypeForMessageParameterException$str() {
/*  767 */     return "HV000079: %s contains Constraint annotation, but the message parameter is not of type java.lang.String.";
/*      */   }
/*      */   
/*      */   public final ConstraintDefinitionException getWrongTypeForMessageParameterException(String constraintName) {
/*  771 */     ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getWrongTypeForMessageParameterException$str(), new Object[] { constraintName }));
/*  772 */     StackTraceElement[] st = result.getStackTrace();
/*  773 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  774 */     return result;
/*      */   }
/*      */   
/*      */   protected String getOverriddenConstraintAttributeNotFoundException$str() {
/*  778 */     return "HV000080: Overridden constraint does not define an attribute with name %s.";
/*      */   }
/*      */   
/*      */   public final ConstraintDefinitionException getOverriddenConstraintAttributeNotFoundException(String attributeName) {
/*  782 */     ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getOverriddenConstraintAttributeNotFoundException$str(), new Object[] { attributeName }));
/*  783 */     StackTraceElement[] st = result.getStackTrace();
/*  784 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  785 */     return result;
/*      */   }
/*      */   
/*      */   protected String getWrongAttributeTypeForOverriddenConstraintException$str() {
/*  789 */     return "HV000081: The overriding type of a composite constraint must be identical to the overridden one. Expected %1$s found %2$s.";
/*      */   }
/*      */   
/*      */   public final ConstraintDefinitionException getWrongAttributeTypeForOverriddenConstraintException(String expectedReturnType, Class<? extends Object> currentReturnType) {
/*  793 */     ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getWrongAttributeTypeForOverriddenConstraintException$str(), new Object[] { expectedReturnType, currentReturnType }));
/*  794 */     StackTraceElement[] st = result.getStackTrace();
/*  795 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  796 */     return result;
/*      */   }
/*      */   
/*      */   protected String getWrongParameterTypeException$str() {
/*  800 */     return "HV000082: Wrong parameter type. Expected: %1$s Actual: %2$s.";
/*      */   }
/*      */   
/*      */   public final ValidationException getWrongParameterTypeException(String expectedType, String currentType) {
/*  804 */     ValidationException result = new ValidationException(String.format(getWrongParameterTypeException$str(), new Object[] { expectedType, currentType }));
/*  805 */     StackTraceElement[] st = result.getStackTrace();
/*  806 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  807 */     return result;
/*      */   }
/*      */   
/*      */   protected String getUnableToFindAnnotationParameterException$str() {
/*  811 */     return "HV000083: The specified annotation defines no parameter '%s'.";
/*      */   }
/*      */   
/*      */   public final ValidationException getUnableToFindAnnotationParameterException(String parameterName, NoSuchMethodException e) {
/*  815 */     ValidationException result = new ValidationException(String.format(getUnableToFindAnnotationParameterException$str(), new Object[] { parameterName }), e);
/*  816 */     StackTraceElement[] st = result.getStackTrace();
/*  817 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  818 */     return result;
/*      */   }
/*      */   
/*      */   protected String getUnableToGetAnnotationParameterException$str() {
/*  822 */     return "HV000084: Unable to get '%1$s' from %2$s.";
/*      */   }
/*      */   
/*      */   public final ValidationException getUnableToGetAnnotationParameterException(String parameterName, String annotationName, Exception e) {
/*  826 */     ValidationException result = new ValidationException(String.format(getUnableToGetAnnotationParameterException$str(), new Object[] { parameterName, annotationName }), e);
/*  827 */     StackTraceElement[] st = result.getStackTrace();
/*  828 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  829 */     return result;
/*      */   }
/*      */   
/*      */   protected String getNoValueProvidedForAnnotationParameterException$str() {
/*  833 */     return "HV000085: No value provided for parameter '%1$s' of annotation @%2$s.";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getNoValueProvidedForAnnotationParameterException(String parameterName, String annotation) {
/*  837 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getNoValueProvidedForAnnotationParameterException$str(), new Object[] { parameterName, annotation }));
/*  838 */     StackTraceElement[] st = result.getStackTrace();
/*  839 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  840 */     return result;
/*      */   }
/*      */   
/*      */   protected String getTryingToInstantiateAnnotationWithUnknownParametersException$str() {
/*  844 */     return "HV000086: Trying to instantiate %1$s with unknown parameter(s): %2$s.";
/*      */   }
/*      */   
/*      */   public final RuntimeException getTryingToInstantiateAnnotationWithUnknownParametersException(Class<? extends Object> annotationType, Set<String> unknownParameters) {
/*  848 */     RuntimeException result = new RuntimeException(String.format(getTryingToInstantiateAnnotationWithUnknownParametersException$str(), new Object[] { annotationType, unknownParameters }));
/*  849 */     StackTraceElement[] st = result.getStackTrace();
/*  850 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  851 */     return result;
/*      */   }
/*      */   
/*      */   protected String getPropertyNameCannotBeNullOrEmptyException$str() {
/*  855 */     return "HV000087: Property name cannot be null or empty.";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getPropertyNameCannotBeNullOrEmptyException() {
/*  859 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getPropertyNameCannotBeNullOrEmptyException$str(), new Object[0]));
/*  860 */     StackTraceElement[] st = result.getStackTrace();
/*  861 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  862 */     return result;
/*      */   }
/*      */   
/*      */   protected String getElementTypeHasToBeFieldOrMethodException$str() {
/*  866 */     return "HV000088: Element type has to be FIELD or METHOD.";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getElementTypeHasToBeFieldOrMethodException() {
/*  870 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getElementTypeHasToBeFieldOrMethodException$str(), new Object[0]));
/*  871 */     StackTraceElement[] st = result.getStackTrace();
/*  872 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  873 */     return result;
/*      */   }
/*      */   
/*      */   protected String getMemberIsNeitherAFieldNorAMethodException$str() {
/*  877 */     return "HV000089: Member %s is neither a field nor a method.";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getMemberIsNeitherAFieldNorAMethodException(Member member) {
/*  881 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getMemberIsNeitherAFieldNorAMethodException$str(), new Object[] { member }));
/*  882 */     StackTraceElement[] st = result.getStackTrace();
/*  883 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  884 */     return result;
/*      */   }
/*      */   
/*      */   protected String getUnableToAccessMemberException$str() {
/*  888 */     return "HV000090: Unable to access %s.";
/*      */   }
/*      */   
/*      */   public final ValidationException getUnableToAccessMemberException(String memberName, Exception e) {
/*  892 */     ValidationException result = new ValidationException(String.format(getUnableToAccessMemberException$str(), new Object[] { memberName }), e);
/*  893 */     StackTraceElement[] st = result.getStackTrace();
/*  894 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  895 */     return result;
/*      */   }
/*      */   
/*      */   protected String getHasToBeAPrimitiveTypeException$str() {
/*  899 */     return "HV000091: %s has to be a primitive type.";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getHasToBeAPrimitiveTypeException(Class<? extends Object> clazz) {
/*  903 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getHasToBeAPrimitiveTypeException$str(), new Object[] { clazz }));
/*  904 */     StackTraceElement[] st = result.getStackTrace();
/*  905 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  906 */     return result;
/*      */   }
/*      */   
/*      */   protected String getNullIsAnInvalidTypeForAConstraintValidatorException$str() {
/*  910 */     return "HV000093: null is an invalid type for a constraint validator.";
/*      */   }
/*      */   
/*      */   public final ValidationException getNullIsAnInvalidTypeForAConstraintValidatorException() {
/*  914 */     ValidationException result = new ValidationException(String.format(getNullIsAnInvalidTypeForAConstraintValidatorException$str(), new Object[0]));
/*  915 */     StackTraceElement[] st = result.getStackTrace();
/*  916 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  917 */     return result;
/*      */   }
/*      */   
/*      */   protected String getMissingActualTypeArgumentForTypeParameterException$str() {
/*  921 */     return "HV000094: Missing actual type argument for type parameter: %s.";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getMissingActualTypeArgumentForTypeParameterException(TypeVariable<? extends Object> typeParameter) {
/*  925 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getMissingActualTypeArgumentForTypeParameterException$str(), new Object[] { typeParameter }));
/*  926 */     StackTraceElement[] st = result.getStackTrace();
/*  927 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  928 */     return result;
/*      */   }
/*      */   
/*      */   protected String getUnableToInstantiateConstraintFactoryClassException$str() {
/*  932 */     return "HV000095: Unable to instantiate constraint factory class %s.";
/*      */   }
/*      */   
/*      */   public final ValidationException getUnableToInstantiateConstraintFactoryClassException(String constraintFactoryClassName, ValidationException e) {
/*  936 */     ValidationException result = new ValidationException(String.format(getUnableToInstantiateConstraintFactoryClassException$str(), new Object[] { constraintFactoryClassName }), e);
/*  937 */     StackTraceElement[] st = result.getStackTrace();
/*  938 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  939 */     return result;
/*      */   }
/*      */   
/*      */   protected String getUnableToOpenInputStreamForMappingFileException$str() {
/*  943 */     return "HV000096: Unable to open input stream for mapping file %s.";
/*      */   }
/*      */   
/*      */   public final ValidationException getUnableToOpenInputStreamForMappingFileException(String mappingFileName) {
/*  947 */     ValidationException result = new ValidationException(String.format(getUnableToOpenInputStreamForMappingFileException$str(), new Object[] { mappingFileName }));
/*  948 */     StackTraceElement[] st = result.getStackTrace();
/*  949 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  950 */     return result;
/*      */   }
/*      */   
/*      */   protected String getUnableToInstantiateMessageInterpolatorClassException$str() {
/*  954 */     return "HV000097: Unable to instantiate message interpolator class %s.";
/*      */   }
/*      */   
/*      */   public final ValidationException getUnableToInstantiateMessageInterpolatorClassException(String messageInterpolatorClassName, Exception e) {
/*  958 */     ValidationException result = new ValidationException(String.format(getUnableToInstantiateMessageInterpolatorClassException$str(), new Object[] { messageInterpolatorClassName }), e);
/*  959 */     StackTraceElement[] st = result.getStackTrace();
/*  960 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  961 */     return result;
/*      */   }
/*      */   
/*      */   protected String getUnableToInstantiateTraversableResolverClassException$str() {
/*  965 */     return "HV000098: Unable to instantiate traversable resolver class %s.";
/*      */   }
/*      */   
/*      */   public final ValidationException getUnableToInstantiateTraversableResolverClassException(String traversableResolverClassName, Exception e) {
/*  969 */     ValidationException result = new ValidationException(String.format(getUnableToInstantiateTraversableResolverClassException$str(), new Object[] { traversableResolverClassName }), e);
/*  970 */     StackTraceElement[] st = result.getStackTrace();
/*  971 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  972 */     return result;
/*      */   }
/*      */   
/*      */   protected String getUnableToInstantiateValidationProviderClassException$str() {
/*  976 */     return "HV000099: Unable to instantiate validation provider class %s.";
/*      */   }
/*      */   
/*      */   public final ValidationException getUnableToInstantiateValidationProviderClassException(String providerClassName, Exception e) {
/*  980 */     ValidationException result = new ValidationException(String.format(getUnableToInstantiateValidationProviderClassException$str(), new Object[] { providerClassName }), e);
/*  981 */     StackTraceElement[] st = result.getStackTrace();
/*  982 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  983 */     return result;
/*      */   }
/*      */   
/*      */   protected String getUnableToParseValidationXmlFileException$str() {
/*  987 */     return "HV000100: Unable to parse %s.";
/*      */   }
/*      */   
/*      */   public final ValidationException getUnableToParseValidationXmlFileException(String file, Exception e) {
/*  991 */     ValidationException result = new ValidationException(String.format(getUnableToParseValidationXmlFileException$str(), new Object[] { file }), e);
/*  992 */     StackTraceElement[] st = result.getStackTrace();
/*  993 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/*  994 */     return result;
/*      */   }
/*      */   
/*      */   protected String getIsNotAnAnnotationException$str() {
/*  998 */     return "HV000101: %s is not an annotation.";
/*      */   }
/*      */   
/*      */   public final ValidationException getIsNotAnAnnotationException(String annotationClassName) {
/* 1002 */     ValidationException result = new ValidationException(String.format(getIsNotAnAnnotationException$str(), new Object[] { annotationClassName }));
/* 1003 */     StackTraceElement[] st = result.getStackTrace();
/* 1004 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1005 */     return result;
/*      */   }
/*      */   
/*      */   protected String getIsNotAConstraintValidatorClassException$str() {
/* 1009 */     return "HV000102: %s is not a constraint validator class.";
/*      */   }
/*      */   
/*      */   public final ValidationException getIsNotAConstraintValidatorClassException(Class<? extends Object> validatorClass) {
/* 1013 */     ValidationException result = new ValidationException(String.format(getIsNotAConstraintValidatorClassException$str(), new Object[] { validatorClass }));
/* 1014 */     StackTraceElement[] st = result.getStackTrace();
/* 1015 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1016 */     return result;
/*      */   }
/*      */   
/*      */   protected String getBeanClassHasAlreadyBeConfiguredInXmlException$str() {
/* 1020 */     return "HV000103: %s is configured at least twice in xml.";
/*      */   }
/*      */   
/*      */   public final ValidationException getBeanClassHasAlreadyBeConfiguredInXmlException(String beanClassName) {
/* 1024 */     ValidationException result = new ValidationException(String.format(getBeanClassHasAlreadyBeConfiguredInXmlException$str(), new Object[] { beanClassName }));
/* 1025 */     StackTraceElement[] st = result.getStackTrace();
/* 1026 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1027 */     return result;
/*      */   }
/*      */   
/*      */   protected String getIsDefinedTwiceInMappingXmlForBeanException$str() {
/* 1031 */     return "HV000104: %1$s is defined twice in mapping xml for bean %2$s.";
/*      */   }
/*      */   
/*      */   public final ValidationException getIsDefinedTwiceInMappingXmlForBeanException(String name, String beanClassName) {
/* 1035 */     ValidationException result = new ValidationException(String.format(getIsDefinedTwiceInMappingXmlForBeanException$str(), new Object[] { name, beanClassName }));
/* 1036 */     StackTraceElement[] st = result.getStackTrace();
/* 1037 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1038 */     return result;
/*      */   }
/*      */   
/*      */   protected String getBeanDoesNotContainTheFieldException$str() {
/* 1042 */     return "HV000105: %1$s does not contain the fieldType %2$s.";
/*      */   }
/*      */   
/*      */   public final ValidationException getBeanDoesNotContainTheFieldException(String beanClassName, String fieldName) {
/* 1046 */     ValidationException result = new ValidationException(String.format(getBeanDoesNotContainTheFieldException$str(), new Object[] { beanClassName, fieldName }));
/* 1047 */     StackTraceElement[] st = result.getStackTrace();
/* 1048 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1049 */     return result;
/*      */   }
/*      */   
/*      */   protected String getBeanDoesNotContainThePropertyException$str() {
/* 1053 */     return "HV000106: %1$s does not contain the property %2$s.";
/*      */   }
/*      */   
/*      */   public final ValidationException getBeanDoesNotContainThePropertyException(String beanClassName, String getterName) {
/* 1057 */     ValidationException result = new ValidationException(String.format(getBeanDoesNotContainThePropertyException$str(), new Object[] { beanClassName, getterName }));
/* 1058 */     StackTraceElement[] st = result.getStackTrace();
/* 1059 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1060 */     return result;
/*      */   }
/*      */   
/*      */   protected String getAnnotationDoesNotContainAParameterException$str() {
/* 1064 */     return "HV000107: Annotation of type %1$s does not contain a parameter %2$s.";
/*      */   }
/*      */   
/*      */   public final ValidationException getAnnotationDoesNotContainAParameterException(String annotationClassName, String parameterName) {
/* 1068 */     ValidationException result = new ValidationException(String.format(getAnnotationDoesNotContainAParameterException$str(), new Object[] { annotationClassName, parameterName }));
/* 1069 */     StackTraceElement[] st = result.getStackTrace();
/* 1070 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1071 */     return result;
/*      */   }
/*      */   
/*      */   protected String getAttemptToSpecifyAnArrayWhereSingleValueIsExpectedException$str() {
/* 1075 */     return "HV000108: Attempt to specify an array where single value is expected.";
/*      */   }
/*      */   
/*      */   public final ValidationException getAttemptToSpecifyAnArrayWhereSingleValueIsExpectedException() {
/* 1079 */     ValidationException result = new ValidationException(String.format(getAttemptToSpecifyAnArrayWhereSingleValueIsExpectedException$str(), new Object[0]));
/* 1080 */     StackTraceElement[] st = result.getStackTrace();
/* 1081 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1082 */     return result;
/*      */   }
/*      */   
/*      */   protected String getUnexpectedParameterValueException$str() {
/* 1086 */     return "HV000109: Unexpected parameter value.";
/*      */   }
/*      */   
/*      */   public final ValidationException getUnexpectedParameterValueException() {
/* 1090 */     ValidationException result = new ValidationException(String.format(getUnexpectedParameterValueException$str(), new Object[0]));
/* 1091 */     StackTraceElement[] st = result.getStackTrace();
/* 1092 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1093 */     return result;
/*      */   }
/*      */   
/*      */   public final ValidationException getUnexpectedParameterValueException(ClassCastException e) {
/* 1097 */     ValidationException result = new ValidationException(String.format(getUnexpectedParameterValueException$str(), new Object[0]), e);
/* 1098 */     StackTraceElement[] st = result.getStackTrace();
/* 1099 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1100 */     return result;
/*      */   }
/*      */   
/*      */   protected String getInvalidNumberFormatException$str() {
/* 1104 */     return "HV000110: Invalid %s format.";
/*      */   }
/*      */   
/*      */   public final ValidationException getInvalidNumberFormatException(String formatName, NumberFormatException e) {
/* 1108 */     ValidationException result = new ValidationException(String.format(getInvalidNumberFormatException$str(), new Object[] { formatName }), e);
/* 1109 */     StackTraceElement[] st = result.getStackTrace();
/* 1110 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1111 */     return result;
/*      */   }
/*      */   
/*      */   protected String getInvalidCharValueException$str() {
/* 1115 */     return "HV000111: Invalid char value: %s.";
/*      */   }
/*      */   
/*      */   public final ValidationException getInvalidCharValueException(String value) {
/* 1119 */     ValidationException result = new ValidationException(String.format(getInvalidCharValueException$str(), new Object[] { value }));
/* 1120 */     StackTraceElement[] st = result.getStackTrace();
/* 1121 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1122 */     return result;
/*      */   }
/*      */   
/*      */   protected String getInvalidReturnTypeException$str() {
/* 1126 */     return "HV000112: Invalid return type: %s. Should be a enumeration type.";
/*      */   }
/*      */   
/*      */   public final ValidationException getInvalidReturnTypeException(Class<? extends Object> returnType, ClassCastException e) {
/* 1130 */     ValidationException result = new ValidationException(String.format(getInvalidReturnTypeException$str(), new Object[] { returnType }), e);
/* 1131 */     StackTraceElement[] st = result.getStackTrace();
/* 1132 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1133 */     return result;
/*      */   }
/*      */   
/*      */   protected String getReservedParameterNamesException$str() {
/* 1137 */     return "HV000113: %s, %s, %s are reserved parameter names.";
/*      */   }
/*      */   
/*      */   public final ValidationException getReservedParameterNamesException(String messageParameterName, String groupsParameterName, String payloadParameterName) {
/* 1141 */     ValidationException result = new ValidationException(String.format(getReservedParameterNamesException$str(), new Object[] { messageParameterName, groupsParameterName, payloadParameterName }));
/* 1142 */     StackTraceElement[] st = result.getStackTrace();
/* 1143 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1144 */     return result;
/*      */   }
/*      */   
/*      */   protected String getWrongPayloadClassException$str() {
/* 1148 */     return "HV000114: Specified payload class %s does not implement javax.validation.Payload";
/*      */   }
/*      */   
/*      */   public final ValidationException getWrongPayloadClassException(String payloadClassName) {
/* 1152 */     ValidationException result = new ValidationException(String.format(getWrongPayloadClassException$str(), new Object[] { payloadClassName }));
/* 1153 */     StackTraceElement[] st = result.getStackTrace();
/* 1154 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1155 */     return result;
/*      */   }
/*      */   
/*      */   protected String getErrorParsingMappingFileException$str() {
/* 1159 */     return "HV000115: Error parsing mapping file.";
/*      */   }
/*      */   
/*      */   public final ValidationException getErrorParsingMappingFileException(Exception e) {
/* 1163 */     ValidationException result = new ValidationException(String.format(getErrorParsingMappingFileException$str(), new Object[0]), e);
/* 1164 */     StackTraceElement[] st = result.getStackTrace();
/* 1165 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1166 */     return result;
/*      */   }
/*      */   
/*      */   protected String getIllegalArgumentException$str() {
/* 1170 */     return "HV000116: %s";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getIllegalArgumentException(String message) {
/* 1174 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getIllegalArgumentException$str(), new Object[] { message }));
/* 1175 */     StackTraceElement[] st = result.getStackTrace();
/* 1176 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1177 */     return result;
/*      */   }
/*      */   
/*      */   protected String getUnableToNarrowNodeTypeException$str() {
/* 1181 */     return "HV000118: Unable to cast %s (with element kind %s) to %s";
/*      */   }
/*      */   
/*      */   public final ClassCastException getUnableToNarrowNodeTypeException(String actualDescriptorType, ElementKind kind, String expectedDescriptorType) {
/* 1185 */     ClassCastException result = new ClassCastException(String.format(getUnableToNarrowNodeTypeException$str(), new Object[] { actualDescriptorType, kind, expectedDescriptorType }));
/* 1186 */     StackTraceElement[] st = result.getStackTrace();
/* 1187 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1188 */     return result;
/*      */   }
/*      */   
/*      */   public final void usingParameterNameProvider(String parameterNameProviderClassName) {
/* 1192 */     this.log.logf(FQCN, Logger.Level.INFO, null, usingParameterNameProvider$str(), parameterNameProviderClassName);
/*      */   }
/*      */   
/*      */   protected String usingParameterNameProvider$str() {
/* 1196 */     return "HV000119: Using %s as parameter name provider.";
/*      */   }
/*      */   
/*      */   protected String getUnableToInstantiateParameterNameProviderClassException$str() {
/* 1200 */     return "HV000120: Unable to instantiate parameter name provider class %s.";
/*      */   }
/*      */   
/*      */   public final ValidationException getUnableToInstantiateParameterNameProviderClassException(String parameterNameProviderClassName, ValidationException e) {
/* 1204 */     ValidationException result = new ValidationException(String.format(getUnableToInstantiateParameterNameProviderClassException$str(), new Object[] { parameterNameProviderClassName }), e);
/* 1205 */     StackTraceElement[] st = result.getStackTrace();
/* 1206 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1207 */     return result;
/*      */   }
/*      */   
/*      */   protected String getUnableToDetermineSchemaVersionException$str() {
/* 1211 */     return "HV000121: Unable to parse %s.";
/*      */   }
/*      */   
/*      */   public final ValidationException getUnableToDetermineSchemaVersionException(String file, XMLStreamException e) {
/* 1215 */     ValidationException result = new ValidationException(String.format(getUnableToDetermineSchemaVersionException$str(), new Object[] { file }), e);
/* 1216 */     StackTraceElement[] st = result.getStackTrace();
/* 1217 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1218 */     return result;
/*      */   }
/*      */   
/*      */   protected String getUnsupportedSchemaVersionException$str() {
/* 1222 */     return "HV000122: Unsupported schema version for %s: %s.";
/*      */   }
/*      */   
/*      */   public final ValidationException getUnsupportedSchemaVersionException(String file, String version) {
/* 1226 */     ValidationException result = new ValidationException(String.format(getUnsupportedSchemaVersionException$str(), new Object[] { file, version }));
/* 1227 */     StackTraceElement[] st = result.getStackTrace();
/* 1228 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1229 */     return result;
/*      */   }
/*      */   
/*      */   protected String getMultipleGroupConversionsForSameSourceException$str() {
/* 1233 */     return "HV000124: Found multiple group conversions for source group %s: %s.";
/*      */   }
/*      */   
/*      */   public final ConstraintDeclarationException getMultipleGroupConversionsForSameSourceException(Class<? extends Object> from, Set<Class<? extends Object>> tos) {
/* 1237 */     ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getMultipleGroupConversionsForSameSourceException$str(), new Object[] { from, tos }));
/* 1238 */     StackTraceElement[] st = result.getStackTrace();
/* 1239 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1240 */     return result;
/*      */   }
/*      */   
/*      */   protected String getGroupConversionOnNonCascadingElementException$str() {
/* 1244 */     return "HV000125: Found group conversions for non-cascading element: %s.";
/*      */   }
/*      */   
/*      */   public final ConstraintDeclarationException getGroupConversionOnNonCascadingElementException(String location) {
/* 1248 */     ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getGroupConversionOnNonCascadingElementException$str(), new Object[] { location }));
/* 1249 */     StackTraceElement[] st = result.getStackTrace();
/* 1250 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1251 */     return result;
/*      */   }
/*      */   
/*      */   protected String getGroupConversionForSequenceException$str() {
/* 1255 */     return "HV000127: Found group conversion using a group sequence as source: %s.";
/*      */   }
/*      */   
/*      */   public final ConstraintDeclarationException getGroupConversionForSequenceException(Class<? extends Object> from) {
/* 1259 */     ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getGroupConversionForSequenceException$str(), new Object[] { from }));
/* 1260 */     StackTraceElement[] st = result.getStackTrace();
/* 1261 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1262 */     return result;
/*      */   }
/*      */   
/*      */   public final void unknownPropertyInExpressionLanguage(String expression, Exception e) {
/* 1266 */     this.log.logf(FQCN, Logger.Level.WARN, e, unknownPropertyInExpressionLanguage$str(), expression);
/*      */   }
/*      */   
/*      */   protected String unknownPropertyInExpressionLanguage$str() {
/* 1270 */     return "HV000129: EL expression '%s' references an unknown property";
/*      */   }
/*      */   
/*      */   public final void errorInExpressionLanguage(String expression, Exception e) {
/* 1274 */     this.log.logf(FQCN, Logger.Level.WARN, e, errorInExpressionLanguage$str(), expression);
/*      */   }
/*      */   
/*      */   protected String errorInExpressionLanguage$str() {
/* 1278 */     return "HV000130: Error in EL expression '%s'";
/*      */   }
/*      */   
/*      */   protected String getMethodReturnValueMustNotBeMarkedMoreThanOnceForCascadedValidationException$str() {
/* 1282 */     return "HV000131: A method return value must not be marked for cascaded validation more than once in a class hierarchy, but the following two methods are marked as such: %s, %s.";
/*      */   }
/*      */   
/*      */   public final ConstraintDeclarationException getMethodReturnValueMustNotBeMarkedMoreThanOnceForCascadedValidationException(Member member1, Member member2) {
/* 1286 */     ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getMethodReturnValueMustNotBeMarkedMoreThanOnceForCascadedValidationException$str(), new Object[] { member1, member2 }));
/* 1287 */     StackTraceElement[] st = result.getStackTrace();
/* 1288 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1289 */     return result;
/*      */   }
/*      */   
/*      */   protected String getVoidMethodsMustNotBeConstrainedException$str() {
/* 1293 */     return "HV000132: Void methods must not be constrained or marked for cascaded validation, but method %s is.";
/*      */   }
/*      */   
/*      */   public final ConstraintDeclarationException getVoidMethodsMustNotBeConstrainedException(Member member) {
/* 1297 */     ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getVoidMethodsMustNotBeConstrainedException$str(), new Object[] { member }));
/* 1298 */     StackTraceElement[] st = result.getStackTrace();
/* 1299 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1300 */     return result;
/*      */   }
/*      */   
/*      */   protected String getBeanDoesNotContainConstructorException$str() {
/* 1304 */     return "HV000133: %1$s does not contain a constructor with the parameter types %2$s.";
/*      */   }
/*      */   
/*      */   public final ValidationException getBeanDoesNotContainConstructorException(String beanClassName, String parameterTypes) {
/* 1308 */     ValidationException result = new ValidationException(String.format(getBeanDoesNotContainConstructorException$str(), new Object[] { beanClassName, parameterTypes }));
/* 1309 */     StackTraceElement[] st = result.getStackTrace();
/* 1310 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1311 */     return result;
/*      */   }
/*      */   
/*      */   protected String getInvalidParameterTypeException$str() {
/* 1315 */     return "HV000134: Unable to load parameter of type '%1$s' in %2$s.";
/*      */   }
/*      */   
/*      */   public final ValidationException getInvalidParameterTypeException(String type, String beanClassName) {
/* 1319 */     ValidationException result = new ValidationException(String.format(getInvalidParameterTypeException$str(), new Object[] { type, beanClassName }));
/* 1320 */     StackTraceElement[] st = result.getStackTrace();
/* 1321 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1322 */     return result;
/*      */   }
/*      */   
/*      */   protected String getBeanDoesNotContainMethodException$str() {
/* 1326 */     return "HV000135: %1$s does not contain a method with the name '%2$s' and parameter types %3$s.";
/*      */   }
/*      */   
/*      */   public final ValidationException getBeanDoesNotContainMethodException(String beanClassName, String methodName, List<Class<? extends Object>> parameterTypes) {
/* 1330 */     ValidationException result = new ValidationException(String.format(getBeanDoesNotContainMethodException$str(), new Object[] { beanClassName, methodName, parameterTypes }));
/* 1331 */     StackTraceElement[] st = result.getStackTrace();
/* 1332 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1333 */     return result;
/*      */   }
/*      */   
/*      */   protected String getUnableToLoadConstraintAnnotationClassException$str() {
/* 1337 */     return "HV000136: The specified constraint annotation class %1$s cannot be loaded.";
/*      */   }
/*      */   
/*      */   public final ValidationException getUnableToLoadConstraintAnnotationClassException(String constraintAnnotationClass, Exception e) {
/* 1341 */     ValidationException result = new ValidationException(String.format(getUnableToLoadConstraintAnnotationClassException$str(), new Object[] { constraintAnnotationClass }), e);
/* 1342 */     StackTraceElement[] st = result.getStackTrace();
/* 1343 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1344 */     return result;
/*      */   }
/*      */   
/*      */   protected String getMethodIsDefinedTwiceInMappingXmlForBeanException$str() {
/* 1348 */     return "HV000137: The method '%1$s' is defined twice in the mapping xml for bean %2$s.";
/*      */   }
/*      */   
/*      */   public final ValidationException getMethodIsDefinedTwiceInMappingXmlForBeanException(String name, String beanClassName) {
/* 1352 */     ValidationException result = new ValidationException(String.format(getMethodIsDefinedTwiceInMappingXmlForBeanException$str(), new Object[] { name, beanClassName }));
/* 1353 */     StackTraceElement[] st = result.getStackTrace();
/* 1354 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1355 */     return result;
/*      */   }
/*      */   
/*      */   protected String getConstructorIsDefinedTwiceInMappingXmlForBeanException$str() {
/* 1359 */     return "HV000138: The constructor '%1$s' is defined twice in the mapping xml for bean %2$s.";
/*      */   }
/*      */   
/*      */   public final ValidationException getConstructorIsDefinedTwiceInMappingXmlForBeanException(String name, String beanClassName) {
/* 1363 */     ValidationException result = new ValidationException(String.format(getConstructorIsDefinedTwiceInMappingXmlForBeanException$str(), new Object[] { name, beanClassName }));
/* 1364 */     StackTraceElement[] st = result.getStackTrace();
/* 1365 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1366 */     return result;
/*      */   }
/*      */   
/*      */   protected String getMultipleCrossParameterValidatorClassesException$str() {
/* 1370 */     return "HV000139: The constraint '%1$s' defines multiple cross parameter validators. Only one is allowed.";
/*      */   }
/*      */   
/*      */   public final ConstraintDefinitionException getMultipleCrossParameterValidatorClassesException(String constraint) {
/* 1374 */     ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getMultipleCrossParameterValidatorClassesException$str(), new Object[] { constraint }));
/* 1375 */     StackTraceElement[] st = result.getStackTrace();
/* 1376 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1377 */     return result;
/*      */   }
/*      */   
/*      */   protected String getImplicitConstraintTargetInAmbiguousConfigurationException$str() {
/* 1381 */     return "HV000141: The constraint %1$s used ConstraintTarget#IMPLICIT where the target cannot be inferred.";
/*      */   }
/*      */   
/*      */   public final ConstraintDeclarationException getImplicitConstraintTargetInAmbiguousConfigurationException(String constraint) {
/* 1385 */     ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getImplicitConstraintTargetInAmbiguousConfigurationException$str(), new Object[] { constraint }));
/* 1386 */     StackTraceElement[] st = result.getStackTrace();
/* 1387 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1388 */     return result;
/*      */   }
/*      */   
/*      */   protected String getCrossParameterConstraintOnMethodWithoutParametersException$str() {
/* 1392 */     return "HV000142: Cross parameter constraint %1$s is illegally placed on a parameterless method or constructor '%2$s'.";
/*      */   }
/*      */   
/*      */   public final ConstraintDeclarationException getCrossParameterConstraintOnMethodWithoutParametersException(String constraint, String member) {
/* 1396 */     ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getCrossParameterConstraintOnMethodWithoutParametersException$str(), new Object[] { constraint, member }));
/* 1397 */     StackTraceElement[] st = result.getStackTrace();
/* 1398 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1399 */     return result;
/*      */   }
/*      */   
/*      */   protected String getCrossParameterConstraintOnClassException$str() {
/* 1403 */     return "HV000143: Cross parameter constraint %1$s is illegally placed on class level.";
/*      */   }
/*      */   
/*      */   public final ConstraintDeclarationException getCrossParameterConstraintOnClassException(String constraint) {
/* 1407 */     ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getCrossParameterConstraintOnClassException$str(), new Object[] { constraint }));
/* 1408 */     StackTraceElement[] st = result.getStackTrace();
/* 1409 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1410 */     return result;
/*      */   }
/*      */   
/*      */   protected String getCrossParameterConstraintOnFieldException$str() {
/* 1414 */     return "HV000144: Cross parameter constraint %1$s is illegally placed on field '%2$s'.";
/*      */   }
/*      */   
/*      */   public final ConstraintDeclarationException getCrossParameterConstraintOnFieldException(String constraint, String field) {
/* 1418 */     ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getCrossParameterConstraintOnFieldException$str(), new Object[] { constraint, field }));
/* 1419 */     StackTraceElement[] st = result.getStackTrace();
/* 1420 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1421 */     return result;
/*      */   }
/*      */   
/*      */   protected String getParameterNodeAddedForNonCrossParameterConstraintException$str() {
/* 1425 */     return "HV000146: No parameter nodes may be added since path %s doesn't refer to a cross-parameter constraint.";
/*      */   }
/*      */   
/*      */   public final IllegalStateException getParameterNodeAddedForNonCrossParameterConstraintException(Path path) {
/* 1429 */     IllegalStateException result = new IllegalStateException(String.format(getParameterNodeAddedForNonCrossParameterConstraintException$str(), new Object[] { path }));
/* 1430 */     StackTraceElement[] st = result.getStackTrace();
/* 1431 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1432 */     return result;
/*      */   }
/*      */   
/*      */   protected String getConstrainedElementConfiguredMultipleTimesException$str() {
/* 1436 */     return "HV000147: %1$s is configured multiple times (note, <getter> and <method> nodes for the same method are not allowed)";
/*      */   }
/*      */   
/*      */   public final ValidationException getConstrainedElementConfiguredMultipleTimesException(String location) {
/* 1440 */     ValidationException result = new ValidationException(String.format(getConstrainedElementConfiguredMultipleTimesException$str(), new Object[] { location }));
/* 1441 */     StackTraceElement[] st = result.getStackTrace();
/* 1442 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1443 */     return result;
/*      */   }
/*      */   
/*      */   public final void evaluatingExpressionLanguageExpressionCausedException(String expression, Exception e) {
/* 1447 */     this.log.logf(FQCN, Logger.Level.WARN, e, evaluatingExpressionLanguageExpressionCausedException$str(), expression);
/*      */   }
/*      */   
/*      */   protected String evaluatingExpressionLanguageExpressionCausedException$str() {
/* 1451 */     return "HV000148: An exception occurred during evaluation of EL expression '%s'";
/*      */   }
/*      */   
/*      */   protected String getExceptionOccurredDuringMessageInterpolationException$str() {
/* 1455 */     return "HV000149: An exception occurred during message interpolation";
/*      */   }
/*      */   
/*      */   public final ValidationException getExceptionOccurredDuringMessageInterpolationException(Exception e) {
/* 1459 */     ValidationException result = new ValidationException(String.format(getExceptionOccurredDuringMessageInterpolationException$str(), new Object[0]), e);
/* 1460 */     StackTraceElement[] st = result.getStackTrace();
/* 1461 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1462 */     return result;
/*      */   }
/*      */   
/*      */   protected String getMultipleValidatorsForSameTypeException$str() {
/* 1466 */     return "HV000150: The constraint '%s' defines multiple validators for the type '%s'. Only one is allowed.";
/*      */   }
/*      */   
/*      */   public final UnexpectedTypeException getMultipleValidatorsForSameTypeException(String constraint, String type) {
/* 1470 */     UnexpectedTypeException result = new UnexpectedTypeException(String.format(getMultipleValidatorsForSameTypeException$str(), new Object[] { constraint, type }));
/* 1471 */     StackTraceElement[] st = result.getStackTrace();
/* 1472 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1473 */     return result;
/*      */   }
/*      */   
/*      */   protected String getParameterConfigurationAlteredInSubTypeException$str() {
/* 1477 */     return "HV000151: A method overriding another method must not alter the parameter constraint configuration, but method %2$s changes the configuration of %1$s.";
/*      */   }
/*      */   
/*      */   public final ConstraintDeclarationException getParameterConfigurationAlteredInSubTypeException(Member superMethod, Member subMethod) {
/* 1481 */     ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getParameterConfigurationAlteredInSubTypeException$str(), new Object[] { superMethod, subMethod }));
/* 1482 */     StackTraceElement[] st = result.getStackTrace();
/* 1483 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1484 */     return result;
/*      */   }
/*      */   
/*      */   protected String getParameterConstraintsDefinedInMethodsFromParallelTypesException$str() {
/* 1488 */     return "HV000152: Two methods defined in parallel types must not declare parameter constraints, if they are overridden by the same method, but methods %s and %s both define parameter constraints.";
/*      */   }
/*      */   
/*      */   public final ConstraintDeclarationException getParameterConstraintsDefinedInMethodsFromParallelTypesException(Member method1, Member method2) {
/* 1492 */     ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getParameterConstraintsDefinedInMethodsFromParallelTypesException$str(), new Object[] { method1, method2 }));
/* 1493 */     StackTraceElement[] st = result.getStackTrace();
/* 1494 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1495 */     return result;
/*      */   }
/*      */   
/*      */   protected String getParametersOrReturnValueConstraintTargetGivenAtNonExecutableException$str() {
/* 1499 */     return "HV000153: The constraint %1$s used ConstraintTarget#%2$s but is not specified on a method or constructor.";
/*      */   }
/*      */   
/*      */   public final ConstraintDeclarationException getParametersOrReturnValueConstraintTargetGivenAtNonExecutableException(String constraint, ConstraintTarget target) {
/* 1503 */     ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getParametersOrReturnValueConstraintTargetGivenAtNonExecutableException$str(), new Object[] { constraint, target }));
/* 1504 */     StackTraceElement[] st = result.getStackTrace();
/* 1505 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1506 */     return result;
/*      */   }
/*      */   
/*      */   protected String getCrossParameterConstraintHasNoValidatorException$str() {
/* 1510 */     return "HV000154: Cross parameter constraint %1$s has no cross-parameter validator.";
/*      */   }
/*      */   
/*      */   public final ConstraintDefinitionException getCrossParameterConstraintHasNoValidatorException(String constraint) {
/* 1514 */     ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getCrossParameterConstraintHasNoValidatorException$str(), new Object[] { constraint }));
/* 1515 */     StackTraceElement[] st = result.getStackTrace();
/* 1516 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1517 */     return result;
/*      */   }
/*      */   
/*      */   protected String getComposedAndComposingConstraintsHaveDifferentTypesException$str() {
/* 1521 */     return "HV000155: Composed and composing constraints must have the same constraint type, but composed constraint %1$s has type %3$s, while composing constraint %2$s has type %4$s.";
/*      */   }
/*      */   
/*      */   public final ConstraintDefinitionException getComposedAndComposingConstraintsHaveDifferentTypesException(String composedConstraintTypeName, String composingConstraintTypeName, ConstraintDescriptorImpl.ConstraintType composedConstraintType, ConstraintDescriptorImpl.ConstraintType composingConstraintType) {
/* 1525 */     ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getComposedAndComposingConstraintsHaveDifferentTypesException$str(), new Object[] { composedConstraintTypeName, composingConstraintTypeName, composedConstraintType, composingConstraintType }));
/* 1526 */     StackTraceElement[] st = result.getStackTrace();
/* 1527 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1528 */     return result;
/*      */   }
/*      */   
/*      */   protected String getGenericAndCrossParameterConstraintDoesNotDefineValidationAppliesToParameterException$str() {
/* 1532 */     return "HV000156: Constraints with generic as well as cross-parameter validators must define an attribute validationAppliesTo(), but constraint %s doesn't.";
/*      */   }
/*      */   
/*      */   public final ConstraintDefinitionException getGenericAndCrossParameterConstraintDoesNotDefineValidationAppliesToParameterException(String constraint) {
/* 1536 */     ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getGenericAndCrossParameterConstraintDoesNotDefineValidationAppliesToParameterException$str(), new Object[] { constraint }));
/* 1537 */     StackTraceElement[] st = result.getStackTrace();
/* 1538 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1539 */     return result;
/*      */   }
/*      */   
/*      */   protected String getValidationAppliesToParameterMustHaveReturnTypeConstraintTargetException$str() {
/* 1543 */     return "HV000157: Return type of the attribute validationAppliesTo() of the constraint %s must be javax.validation.ConstraintTarget.";
/*      */   }
/*      */   
/*      */   public final ConstraintDefinitionException getValidationAppliesToParameterMustHaveReturnTypeConstraintTargetException(String constraint) {
/* 1547 */     ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getValidationAppliesToParameterMustHaveReturnTypeConstraintTargetException$str(), new Object[] { constraint }));
/* 1548 */     StackTraceElement[] st = result.getStackTrace();
/* 1549 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1550 */     return result;
/*      */   }
/*      */   
/*      */   protected String getValidationAppliesToParameterMustHaveDefaultValueImplicitException$str() {
/* 1554 */     return "HV000158: Default value of the attribute validationAppliesTo() of the constraint %s must be ConstraintTarget#IMPLICIT.";
/*      */   }
/*      */   
/*      */   public final ConstraintDefinitionException getValidationAppliesToParameterMustHaveDefaultValueImplicitException(String constraint) {
/* 1558 */     ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getValidationAppliesToParameterMustHaveDefaultValueImplicitException$str(), new Object[] { constraint }));
/* 1559 */     StackTraceElement[] st = result.getStackTrace();
/* 1560 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1561 */     return result;
/*      */   }
/*      */   
/*      */   protected String getValidationAppliesToParameterMustNotBeDefinedForNonGenericAndCrossParameterConstraintException$str() {
/* 1565 */     return "HV000159: Only constraints with generic as well as cross-parameter validators must define an attribute validationAppliesTo(), but constraint %s does.";
/*      */   }
/*      */   
/*      */   public final ConstraintDefinitionException getValidationAppliesToParameterMustNotBeDefinedForNonGenericAndCrossParameterConstraintException(String constraint) {
/* 1569 */     ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getValidationAppliesToParameterMustNotBeDefinedForNonGenericAndCrossParameterConstraintException$str(), new Object[] { constraint }));
/* 1570 */     StackTraceElement[] st = result.getStackTrace();
/* 1571 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1572 */     return result;
/*      */   }
/*      */   
/*      */   protected String getValidatorForCrossParameterConstraintMustEitherValidateObjectOrObjectArrayException$str() {
/* 1576 */     return "HV000160: Validator for cross-parameter constraint %s does not validate Object nor Object[].";
/*      */   }
/*      */   
/*      */   public final ConstraintDefinitionException getValidatorForCrossParameterConstraintMustEitherValidateObjectOrObjectArrayException(String constraint) {
/* 1580 */     ConstraintDefinitionException result = new ConstraintDefinitionException(String.format(getValidatorForCrossParameterConstraintMustEitherValidateObjectOrObjectArrayException$str(), new Object[] { constraint }));
/* 1581 */     StackTraceElement[] st = result.getStackTrace();
/* 1582 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1583 */     return result;
/*      */   }
/*      */   
/*      */   protected String getMethodsFromParallelTypesMustNotDefineGroupConversionsForCascadedReturnValueException$str() {
/* 1587 */     return "HV000161: Two methods defined in parallel types must not define group conversions for a cascaded method return value, if they are overridden by the same method, but methods %s and %s both define parameter constraints.";
/*      */   }
/*      */   
/*      */   public final ConstraintDeclarationException getMethodsFromParallelTypesMustNotDefineGroupConversionsForCascadedReturnValueException(Member method1, Member method2) {
/* 1591 */     ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getMethodsFromParallelTypesMustNotDefineGroupConversionsForCascadedReturnValueException$str(), new Object[] { method1, method2 }));
/* 1592 */     StackTraceElement[] st = result.getStackTrace();
/* 1593 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1594 */     return result;
/*      */   }
/*      */   
/*      */   protected String getMethodOrConstructorNotDefinedByValidatedTypeException$str() {
/* 1598 */     return "HV000162: The validated type %1$s does not specify the constructor/method: %2$s";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getMethodOrConstructorNotDefinedByValidatedTypeException(String validatedTypeName, Member member) {
/* 1602 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getMethodOrConstructorNotDefinedByValidatedTypeException$str(), new Object[] { validatedTypeName, member }));
/* 1603 */     StackTraceElement[] st = result.getStackTrace();
/* 1604 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1605 */     return result;
/*      */   }
/*      */   
/*      */   protected String getParameterTypesDoNotMatchException$str() {
/* 1609 */     return "HV000163: The actual parameter type '%1$s' is not assignable to the expected one '%2$s' for parameter %3$d of '%4$s'";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getParameterTypesDoNotMatchException(String actualType, String expectedType, int index, Member member) {
/* 1613 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getParameterTypesDoNotMatchException$str(), new Object[] { actualType, expectedType, Integer.valueOf(index), member }));
/* 1614 */     StackTraceElement[] st = result.getStackTrace();
/* 1615 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1616 */     return result;
/*      */   }
/*      */   
/*      */   protected String getHasToBeABoxedTypeException$str() {
/* 1620 */     return "HV000164: %s has to be a auto-boxed type.";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getHasToBeABoxedTypeException(Class<? extends Object> clazz) {
/* 1624 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getHasToBeABoxedTypeException$str(), new Object[] { clazz }));
/* 1625 */     StackTraceElement[] st = result.getStackTrace();
/* 1626 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1627 */     return result;
/*      */   }
/*      */   
/*      */   protected String getMixingImplicitWithOtherExecutableTypesException$str() {
/* 1631 */     return "HV000165: Mixing IMPLICIT and other executable types is not allowed.";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getMixingImplicitWithOtherExecutableTypesException() {
/* 1635 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getMixingImplicitWithOtherExecutableTypesException$str(), new Object[0]));
/* 1636 */     StackTraceElement[] st = result.getStackTrace();
/* 1637 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1638 */     return result;
/*      */   }
/*      */   
/*      */   protected String getValidateOnExecutionOnOverriddenOrInterfaceMethodException$str() {
/* 1642 */     return "HV000166: @ValidateOnExecution is not allowed on methods overriding a superclass method or implementing an interface. Check configuration for %1$s";
/*      */   }
/*      */   
/*      */   public final ValidationException getValidateOnExecutionOnOverriddenOrInterfaceMethodException(Method m) {
/* 1646 */     ValidationException result = new ValidationException(String.format(getValidateOnExecutionOnOverriddenOrInterfaceMethodException$str(), new Object[] { m }));
/* 1647 */     StackTraceElement[] st = result.getStackTrace();
/* 1648 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1649 */     return result;
/*      */   }
/*      */   
/*      */   protected String getOverridingConstraintDefinitionsInMultipleMappingFilesException$str() {
/* 1653 */     return "HV000167: A given constraint definition can only be overridden in one mapping file. %1$s is overridden in multiple files";
/*      */   }
/*      */   
/*      */   public final ValidationException getOverridingConstraintDefinitionsInMultipleMappingFilesException(String constraintClass) {
/* 1657 */     ValidationException result = new ValidationException(String.format(getOverridingConstraintDefinitionsInMultipleMappingFilesException$str(), new Object[] { constraintClass }));
/* 1658 */     StackTraceElement[] st = result.getStackTrace();
/* 1659 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1660 */     return result;
/*      */   }
/*      */   
/*      */   protected String getNonTerminatedParameterException$str() {
/* 1664 */     return "HV000168: The message descriptor '%1$s' contains an unbalanced meta character '%2$c' parameter.";
/*      */   }
/*      */   
/*      */   public final MessageDescriptorFormatException getNonTerminatedParameterException(String messageDescriptor, char character) {
/* 1668 */     MessageDescriptorFormatException result = new MessageDescriptorFormatException(String.format(getNonTerminatedParameterException$str(), new Object[] { messageDescriptor, Character.valueOf(character) }));
/* 1669 */     StackTraceElement[] st = result.getStackTrace();
/* 1670 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1671 */     return result;
/*      */   }
/*      */   
/*      */   protected String getNestedParameterException$str() {
/* 1675 */     return "HV000169: The message descriptor '%1$s' has nested parameters.";
/*      */   }
/*      */   
/*      */   public final MessageDescriptorFormatException getNestedParameterException(String messageDescriptor) {
/* 1679 */     MessageDescriptorFormatException result = new MessageDescriptorFormatException(String.format(getNestedParameterException$str(), new Object[] { messageDescriptor }));
/* 1680 */     StackTraceElement[] st = result.getStackTrace();
/* 1681 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1682 */     return result;
/*      */   }
/*      */   
/*      */   protected String getCreationOfScriptExecutorFailedException$str() {
/* 1686 */     return "HV000170: No JSR-223 scripting engine could be bootstrapped for language \"%s\".";
/*      */   }
/*      */   
/*      */   public final ConstraintDeclarationException getCreationOfScriptExecutorFailedException(String languageName, Exception e) {
/* 1690 */     ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getCreationOfScriptExecutorFailedException$str(), new Object[] { languageName }), e);
/* 1691 */     StackTraceElement[] st = result.getStackTrace();
/* 1692 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1693 */     return result;
/*      */   }
/*      */   
/*      */   protected String getBeanClassHasAlreadyBeConfiguredViaProgrammaticApiException$str() {
/* 1697 */     return "HV000171: %s is configured more than once via the programmatic constraint declaration API.";
/*      */   }
/*      */   
/*      */   public final ValidationException getBeanClassHasAlreadyBeConfiguredViaProgrammaticApiException(String beanClassName) {
/* 1701 */     ValidationException result = new ValidationException(String.format(getBeanClassHasAlreadyBeConfiguredViaProgrammaticApiException$str(), new Object[] { beanClassName }));
/* 1702 */     StackTraceElement[] st = result.getStackTrace();
/* 1703 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1704 */     return result;
/*      */   }
/*      */   
/*      */   protected String getPropertyHasAlreadyBeConfiguredViaProgrammaticApiException$str() {
/* 1708 */     return "HV000172: Property \"%2$s\" of type %1$s is configured more than once via the programmatic constraint declaration API.";
/*      */   }
/*      */   
/*      */   public final ValidationException getPropertyHasAlreadyBeConfiguredViaProgrammaticApiException(String beanClassName, String propertyName) {
/* 1712 */     ValidationException result = new ValidationException(String.format(getPropertyHasAlreadyBeConfiguredViaProgrammaticApiException$str(), new Object[] { beanClassName, propertyName }));
/* 1713 */     StackTraceElement[] st = result.getStackTrace();
/* 1714 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1715 */     return result;
/*      */   }
/*      */   
/*      */   protected String getMethodHasAlreadyBeConfiguredViaProgrammaticApiException$str() {
/* 1719 */     return "HV000173: Method %2$s of type %1$s is configured more than once via the programmatic constraint declaration API.";
/*      */   }
/*      */   
/*      */   public final ValidationException getMethodHasAlreadyBeConfiguredViaProgrammaticApiException(String beanClassName, String method) {
/* 1723 */     ValidationException result = new ValidationException(String.format(getMethodHasAlreadyBeConfiguredViaProgrammaticApiException$str(), new Object[] { beanClassName, method }));
/* 1724 */     StackTraceElement[] st = result.getStackTrace();
/* 1725 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1726 */     return result;
/*      */   }
/*      */   
/*      */   protected String getParameterHasAlreadyBeConfiguredViaProgrammaticApiException$str() {
/* 1730 */     return "HV000174: Parameter %3$s of method or constructor %2$s of type %1$s is configured more than once via the programmatic constraint declaration API.";
/*      */   }
/*      */   
/*      */   public final ValidationException getParameterHasAlreadyBeConfiguredViaProgrammaticApiException(String beanClassName, String executable, int parameterIndex) {
/* 1734 */     ValidationException result = new ValidationException(String.format(getParameterHasAlreadyBeConfiguredViaProgrammaticApiException$str(), new Object[] { beanClassName, executable, Integer.valueOf(parameterIndex) }));
/* 1735 */     StackTraceElement[] st = result.getStackTrace();
/* 1736 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1737 */     return result;
/*      */   }
/*      */   
/*      */   protected String getReturnValueHasAlreadyBeConfiguredViaProgrammaticApiException$str() {
/* 1741 */     return "HV000175: The return value of method or constructor %2$s of type %1$s is configured more than once via the programmatic constraint declaration API.";
/*      */   }
/*      */   
/*      */   public final ValidationException getReturnValueHasAlreadyBeConfiguredViaProgrammaticApiException(String beanClassName, String executable) {
/* 1745 */     ValidationException result = new ValidationException(String.format(getReturnValueHasAlreadyBeConfiguredViaProgrammaticApiException$str(), new Object[] { beanClassName, executable }));
/* 1746 */     StackTraceElement[] st = result.getStackTrace();
/* 1747 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1748 */     return result;
/*      */   }
/*      */   
/*      */   protected String getConstructorHasAlreadyBeConfiguredViaProgrammaticApiException$str() {
/* 1752 */     return "HV000176: Constructor %2$s of type %1$s is configured more than once via the programmatic constraint declaration API.";
/*      */   }
/*      */   
/*      */   public final ValidationException getConstructorHasAlreadyBeConfiguredViaProgrammaticApiException(String beanClassName, String constructor) {
/* 1756 */     ValidationException result = new ValidationException(String.format(getConstructorHasAlreadyBeConfiguredViaProgrammaticApiException$str(), new Object[] { beanClassName, constructor }));
/* 1757 */     StackTraceElement[] st = result.getStackTrace();
/* 1758 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1759 */     return result;
/*      */   }
/*      */   
/*      */   protected String getCrossParameterElementHasAlreadyBeConfiguredViaProgrammaticApiException$str() {
/* 1763 */     return "HV000177: Cross-parameter constraints for the method or constructor %2$s of type %1$s are declared more than once via the programmatic constraint declaration API.";
/*      */   }
/*      */   
/*      */   public final ValidationException getCrossParameterElementHasAlreadyBeConfiguredViaProgrammaticApiException(String beanClassName, String executable) {
/* 1767 */     ValidationException result = new ValidationException(String.format(getCrossParameterElementHasAlreadyBeConfiguredViaProgrammaticApiException$str(), new Object[] { beanClassName, executable }));
/* 1768 */     StackTraceElement[] st = result.getStackTrace();
/* 1769 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1770 */     return result;
/*      */   }
/*      */   
/*      */   protected String getMultiplierCannotBeNegativeException$str() {
/* 1774 */     return "HV000178: Multiplier cannot be negative: %d.";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getMultiplierCannotBeNegativeException(int multiplier) {
/* 1778 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getMultiplierCannotBeNegativeException$str(), new Object[] { Integer.valueOf(multiplier) }));
/* 1779 */     StackTraceElement[] st = result.getStackTrace();
/* 1780 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1781 */     return result;
/*      */   }
/*      */   
/*      */   protected String getWeightCannotBeNegativeException$str() {
/* 1785 */     return "HV000179: Weight cannot be negative: %d.";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getWeightCannotBeNegativeException(int weight) {
/* 1789 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getWeightCannotBeNegativeException$str(), new Object[] { Integer.valueOf(weight) }));
/* 1790 */     StackTraceElement[] st = result.getStackTrace();
/* 1791 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1792 */     return result;
/*      */   }
/*      */   
/*      */   protected String getTreatCheckAsIsNotADigitNorALetterException$str() {
/* 1796 */     return "HV000180: '%c' is not a digit nor a letter.";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getTreatCheckAsIsNotADigitNorALetterException(int weight) {
/* 1800 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getTreatCheckAsIsNotADigitNorALetterException$str(), new Object[] { Integer.valueOf(weight) }));
/* 1801 */     StackTraceElement[] st = result.getStackTrace();
/* 1802 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1803 */     return result;
/*      */   }
/*      */   
/*      */   protected String getInvalidParameterCountForExecutableException$str() {
/* 1807 */     return "HV000181: Wrong number of parameters. Method or constructor %1$s expects %2$d parameters, but got %3$d.";
/*      */   }
/*      */   
/*      */   public final IllegalArgumentException getInvalidParameterCountForExecutableException(String executable, int expectedParameterCount, int actualParameterCount) {
/* 1811 */     IllegalArgumentException result = new IllegalArgumentException(String.format(getInvalidParameterCountForExecutableException$str(), new Object[] { executable, Integer.valueOf(expectedParameterCount), Integer.valueOf(actualParameterCount) }));
/* 1812 */     StackTraceElement[] st = result.getStackTrace();
/* 1813 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1814 */     return result;
/*      */   }
/*      */   
/*      */   protected String getNoUnwrapperFoundForTypeException$str() {
/* 1818 */     return "HV000182: No validation value unwrapper is registered for type '%1$s'.";
/*      */   }
/*      */   
/*      */   public final ValidationException getNoUnwrapperFoundForTypeException(String typeName) {
/* 1822 */     ValidationException result = new ValidationException(String.format(getNoUnwrapperFoundForTypeException$str(), new Object[] { typeName }));
/* 1823 */     StackTraceElement[] st = result.getStackTrace();
/* 1824 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1825 */     return result;
/*      */   }
/*      */   
/*      */   protected String getUnableToInitializeELExpressionFactoryException$str() {
/* 1829 */     return "HV000183: Unable to initialize 'javax.el.ExpressionFactory'. Check that you have the EL dependencies on the classpath, or use ParameterMessageInterpolator instead";
/*      */   }
/*      */   
/*      */   public final ValidationException getUnableToInitializeELExpressionFactoryException(Throwable e) {
/* 1833 */     ValidationException result = new ValidationException(String.format(getUnableToInitializeELExpressionFactoryException$str(), new Object[0]), e);
/* 1834 */     StackTraceElement[] st = result.getStackTrace();
/* 1835 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1836 */     return result;
/*      */   }
/*      */   
/*      */   public final void creationOfParameterMessageInterpolation() {
/* 1840 */     this.log.logf(FQCN, Logger.Level.WARN, null, creationOfParameterMessageInterpolation$str(), new Object[0]);
/*      */   }
/*      */   
/*      */   protected String creationOfParameterMessageInterpolation$str() {
/* 1844 */     return "HV000184: ParameterMessageInterpolator has been chosen, EL interpolation will not be supported";
/*      */   }
/*      */   
/*      */   public final void getElUnsupported(String expression) {
/* 1848 */     this.log.logf(FQCN, Logger.Level.WARN, null, getElUnsupported$str(), expression);
/*      */   }
/*      */   
/*      */   protected String getElUnsupported$str() {
/* 1852 */     return "HV000185: Message contains EL expression: %1s, which is unsupported with chosen Interpolator";
/*      */   }
/*      */   
/*      */   protected String getConstraintValidatorExistsForWrapperAndWrappedValueException$str() {
/* 1856 */     return "HV000186: The constraint of type '%2$s' defined on '%1$s' has multiple matching constraint validators which is due to an additional value handler of type '%3$s'. It is unclear which value needs validating. Clarify configuration via @UnwrapValidatedValue.";
/*      */   }
/*      */   
/*      */   public final UnexpectedTypeException getConstraintValidatorExistsForWrapperAndWrappedValueException(String property, String constraint, String valueHandler) {
/* 1860 */     UnexpectedTypeException result = new UnexpectedTypeException(String.format(getConstraintValidatorExistsForWrapperAndWrappedValueException$str(), new Object[] { property, constraint, valueHandler }));
/* 1861 */     StackTraceElement[] st = result.getStackTrace();
/* 1862 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1863 */     return result;
/*      */   }
/*      */   
/*      */   protected String getTypeAnnotationConstraintOnIterableRequiresUseOfValidAnnotationException$str() {
/* 1867 */     return "HV000187: When using type annotation constraints on parameterized iterables or map @Valid must be used. Check %s#%s";
/*      */   }
/*      */   
/*      */   public final ValidationException getTypeAnnotationConstraintOnIterableRequiresUseOfValidAnnotationException(String declaringClass, String name) {
/* 1871 */     ValidationException result = new ValidationException(String.format(getTypeAnnotationConstraintOnIterableRequiresUseOfValidAnnotationException$str(), new Object[] { declaringClass, name }));
/* 1872 */     StackTraceElement[] st = result.getStackTrace();
/* 1873 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1874 */     return result;
/*      */   }
/*      */   
/*      */   public final void parameterizedTypeWithMoreThanOneTypeArgumentIsNotSupported(String type) {
/* 1878 */     this.log.logf(FQCN, Logger.Level.DEBUG, null, parameterizedTypeWithMoreThanOneTypeArgumentIsNotSupported$str(), type);
/*      */   }
/*      */   
/*      */   protected String parameterizedTypeWithMoreThanOneTypeArgumentIsNotSupported$str() {
/* 1882 */     return "HV000188: Parameterized type with more than one argument is not supported: %s";
/*      */   }
/*      */   
/*      */   protected String getInconsistentValueUnwrappingConfigurationBetweenFieldAndItsGetterException$str() {
/* 1886 */     return "HV000189: The configuration of value unwrapping for property '%s' of bean '%s' is inconsistent between the field and its getter.";
/*      */   }
/*      */   
/*      */   public final ConstraintDeclarationException getInconsistentValueUnwrappingConfigurationBetweenFieldAndItsGetterException(String property, String clazz) {
/* 1890 */     ConstraintDeclarationException result = new ConstraintDeclarationException(String.format(getInconsistentValueUnwrappingConfigurationBetweenFieldAndItsGetterException$str(), new Object[] { property, clazz }));
/* 1891 */     StackTraceElement[] st = result.getStackTrace();
/* 1892 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1893 */     return result;
/*      */   }
/*      */   
/*      */   protected String getUnableToCreateXMLEventReader$str() {
/* 1897 */     return "HV000190: Unable to parse %s.";
/*      */   }
/*      */   
/*      */   public final ValidationException getUnableToCreateXMLEventReader(String file, Exception e) {
/* 1901 */     ValidationException result = new ValidationException(String.format(getUnableToCreateXMLEventReader$str(), new Object[] { file }), e);
/* 1902 */     StackTraceElement[] st = result.getStackTrace();
/* 1903 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1904 */     return result;
/*      */   }
/*      */   
/*      */   protected String validatedValueUnwrapperCannotBeCreated$str() {
/* 1908 */     return "HV000191: Error creating unwrapper: %s";
/*      */   }
/*      */   
/*      */   public final ValidationException validatedValueUnwrapperCannotBeCreated(String className, Exception e) {
/* 1912 */     ValidationException result = new ValidationException(String.format(validatedValueUnwrapperCannotBeCreated$str(), new Object[] { className }), e);
/* 1913 */     StackTraceElement[] st = result.getStackTrace();
/* 1914 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1915 */     return result;
/*      */   }
/*      */   
/*      */   public final void unknownJvmVersion(String vmVersionStr) {
/* 1919 */     this.log.logf(FQCN, Logger.Level.WARN, null, unknownJvmVersion$str(), vmVersionStr);
/*      */   }
/*      */   
/*      */   protected String unknownJvmVersion$str() {
/* 1923 */     return "HV000192: Couldn't determine Java version from value %1s; Not enabling features requiring Java 8";
/*      */   }
/*      */   
/*      */   protected String getConstraintHasAlreadyBeenConfiguredViaProgrammaticApiException$str() {
/* 1927 */     return "HV000193: %s is configured more than once via the programmatic constraint definition API.";
/*      */   }
/*      */   
/*      */   public final ValidationException getConstraintHasAlreadyBeenConfiguredViaProgrammaticApiException(String annotationClassName) {
/* 1931 */     ValidationException result = new ValidationException(String.format(getConstraintHasAlreadyBeenConfiguredViaProgrammaticApiException$str(), new Object[] { annotationClassName }));
/* 1932 */     StackTraceElement[] st = result.getStackTrace();
/* 1933 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1934 */     return result;
/*      */   }
/*      */   
/*      */   protected String getEmptyElementOnlySupportedWhenCharSequenceIsExpectedExpection$str() {
/* 1938 */     return "HV000194: An empty element is only supported when a CharSequence is expected.";
/*      */   }
/*      */   
/*      */   public final ValidationException getEmptyElementOnlySupportedWhenCharSequenceIsExpectedExpection() {
/* 1942 */     ValidationException result = new ValidationException(String.format(getEmptyElementOnlySupportedWhenCharSequenceIsExpectedExpection$str(), new Object[0]));
/* 1943 */     StackTraceElement[] st = result.getStackTrace();
/* 1944 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1945 */     return result;
/*      */   }
/*      */   
/*      */   protected String getUnableToReachPropertyToValidateException$str() {
/* 1949 */     return "HV000195: Unable to reach the property to validate for the bean %s and the property path %s. A property is null along the way.";
/*      */   }
/*      */   
/*      */   public final ValidationException getUnableToReachPropertyToValidateException(Object bean, Path path) {
/* 1953 */     ValidationException result = new ValidationException(String.format(getUnableToReachPropertyToValidateException$str(), new Object[] { bean, path }));
/* 1954 */     StackTraceElement[] st = result.getStackTrace();
/* 1955 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1956 */     return result;
/*      */   }
/*      */   
/*      */   protected String getUnableToConvertTypeToClassException$str() {
/* 1960 */     return "HV000196: Unable to convert the Type %s to a Class.";
/*      */   }
/*      */   
/*      */   public final ValidationException getUnableToConvertTypeToClassException(Type type) {
/* 1964 */     ValidationException result = new ValidationException(String.format(getUnableToConvertTypeToClassException$str(), new Object[] { type }));
/* 1965 */     StackTraceElement[] st = result.getStackTrace();
/* 1966 */     result.setStackTrace((StackTraceElement[])Arrays.copyOfRange(st, 1, st.length));
/* 1967 */     return result;
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\logging\Log_$logger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */