package org.hibernate.validator.internal.util.logging;

import java.lang.annotation.ElementType;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Set;
import java.util.regex.PatternSyntaxException;
import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintDefinitionException;
import javax.validation.ConstraintTarget;
import javax.validation.ElementKind;
import javax.validation.GroupDefinitionException;
import javax.validation.Path;
import javax.validation.UnexpectedTypeException;
import javax.validation.ValidationException;
import javax.xml.stream.XMLStreamException;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageDescriptorFormatException;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl.ConstraintType;
import org.jboss.logging.BasicLogger;
import org.jboss.logging.Logger.Level;
import org.jboss.logging.annotations.Cause;
import org.jboss.logging.annotations.LogMessage;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageLogger;

@MessageLogger(projectCode="HV")
public abstract interface Log
  extends BasicLogger
{
  @LogMessage(level=Logger.Level.INFO)
  @Message(id=1, value="Hibernate Validator %s")
  public abstract void version(String paramString);
  
  @LogMessage(level=Logger.Level.INFO)
  @Message(id=2, value="Ignoring XML configuration.")
  public abstract void ignoringXmlConfiguration();
  
  @LogMessage(level=Logger.Level.INFO)
  @Message(id=3, value="Using %s as constraint factory.")
  public abstract void usingConstraintFactory(String paramString);
  
  @LogMessage(level=Logger.Level.INFO)
  @Message(id=4, value="Using %s as message interpolator.")
  public abstract void usingMessageInterpolator(String paramString);
  
  @LogMessage(level=Logger.Level.INFO)
  @Message(id=5, value="Using %s as traversable resolver.")
  public abstract void usingTraversableResolver(String paramString);
  
  @LogMessage(level=Logger.Level.INFO)
  @Message(id=6, value="Using %s as validation provider.")
  public abstract void usingValidationProvider(String paramString);
  
  @LogMessage(level=Logger.Level.INFO)
  @Message(id=7, value="%s found. Parsing XML based configuration.")
  public abstract void parsingXMLFile(String paramString);
  
  @LogMessage(level=Logger.Level.WARN)
  @Message(id=8, value="Unable to close input stream.")
  public abstract void unableToCloseInputStream();
  
  @LogMessage(level=Logger.Level.WARN)
  @Message(id=10, value="Unable to close input stream for %s.")
  public abstract void unableToCloseXMLFileInputStream(String paramString);
  
  @LogMessage(level=Logger.Level.WARN)
  @Message(id=11, value="Unable to create schema for %1$s: %2$s")
  public abstract void unableToCreateSchema(String paramString1, String paramString2);
  
  @Message(id=12, value="Unable to create annotation for configured constraint")
  public abstract ValidationException getUnableToCreateAnnotationForConfiguredConstraintException(@Cause RuntimeException paramRuntimeException);
  
  @Message(id=13, value="The class %1$s does not have a property '%2$s' with access %3$s.")
  public abstract ValidationException getUnableToFindPropertyWithAccessException(Class<?> paramClass, String paramString, ElementType paramElementType);
  
  @Message(id=14, value="Type %1$s doesn't have a method %2$s.")
  public abstract IllegalArgumentException getUnableToFindMethodException(Class<?> paramClass, String paramString);
  
  @Message(id=16, value="%s does not represent a valid BigDecimal format.")
  public abstract IllegalArgumentException getInvalidBigDecimalFormatException(String paramString, @Cause NumberFormatException paramNumberFormatException);
  
  @Message(id=17, value="The length of the integer part cannot be negative.")
  public abstract IllegalArgumentException getInvalidLengthForIntegerPartException();
  
  @Message(id=18, value="The length of the fraction part cannot be negative.")
  public abstract IllegalArgumentException getInvalidLengthForFractionPartException();
  
  @Message(id=19, value="The min parameter cannot be negative.")
  public abstract IllegalArgumentException getMinCannotBeNegativeException();
  
  @Message(id=20, value="The max parameter cannot be negative.")
  public abstract IllegalArgumentException getMaxCannotBeNegativeException();
  
  @Message(id=21, value="The length cannot be negative.")
  public abstract IllegalArgumentException getLengthCannotBeNegativeException();
  
  @Message(id=22, value="Invalid regular expression.")
  public abstract IllegalArgumentException getInvalidRegularExpressionException(@Cause PatternSyntaxException paramPatternSyntaxException);
  
  @Message(id=23, value="Error during execution of script \"%s\" occurred.")
  public abstract ConstraintDeclarationException getErrorDuringScriptExecutionException(String paramString, @Cause Exception paramException);
  
  @Message(id=24, value="Script \"%s\" returned null, but must return either true or false.")
  public abstract ConstraintDeclarationException getScriptMustReturnTrueOrFalseException(String paramString);
  
  @Message(id=25, value="Script \"%1$s\" returned %2$s (of type %3$s), but must return either true or false.")
  public abstract ConstraintDeclarationException getScriptMustReturnTrueOrFalseException(String paramString1, Object paramObject, String paramString2);
  
  @Message(id=26, value="Assertion error: inconsistent ConfigurationImpl construction.")
  public abstract ValidationException getInconsistentConfigurationException();
  
  @Message(id=27, value="Unable to find provider: %s.")
  public abstract ValidationException getUnableToFindProviderException(Class<?> paramClass);
  
  @Message(id=28, value="Unexpected exception during isValid call.")
  public abstract ValidationException getExceptionDuringIsValidCallException(@Cause RuntimeException paramRuntimeException);
  
  @Message(id=29, value="Constraint factory returned null when trying to create instance of %s.")
  public abstract ValidationException getConstraintFactoryMustNotReturnNullException(String paramString);
  
  @Message(id=30, value="No validator could be found for constraint '%s' validating type '%s'. Check configuration for '%s'")
  public abstract UnexpectedTypeException getNoValidatorFoundForTypeException(String paramString1, String paramString2, String paramString3);
  
  @Message(id=31, value="There are multiple validator classes which could validate the type %1$s. The validator classes are: %2$s.")
  public abstract UnexpectedTypeException getMoreThanOneValidatorFoundForTypeException(Type paramType, String paramString);
  
  @Message(id=32, value="Unable to initialize %s.")
  public abstract ValidationException getUnableToInitializeConstraintValidatorException(String paramString, @Cause RuntimeException paramRuntimeException);
  
  @Message(id=33, value="At least one custom message must be created if the default error message gets disabled.")
  public abstract ValidationException getAtLeastOneCustomMessageMustBeCreatedException();
  
  @Message(id=34, value="%s is not a valid Java Identifier.")
  public abstract IllegalArgumentException getInvalidJavaIdentifierException(String paramString);
  
  @Message(id=35, value="Unable to parse property path %s.")
  public abstract IllegalArgumentException getUnableToParsePropertyPathException(String paramString);
  
  @Message(id=36, value="Type %s not supported for unwrapping.")
  public abstract ValidationException getTypeNotSupportedForUnwrappingException(Class<?> paramClass);
  
  @Message(id=37, value="Inconsistent fail fast configuration. Fail fast enabled via programmatic API, but explicitly disabled via properties.")
  public abstract ValidationException getInconsistentFailFastConfigurationException();
  
  @Message(id=38, value="Invalid property path.")
  public abstract IllegalArgumentException getInvalidPropertyPathException();
  
  @Message(id=39, value="Invalid property path. Either there is no property %2$s in entity %1$s or it is not possible to cascade to the property.")
  public abstract IllegalArgumentException getInvalidPropertyPathException(String paramString1, String paramString2);
  
  @Message(id=40, value="Property path must provide index or map key.")
  public abstract IllegalArgumentException getPropertyPathMustProvideIndexOrMapKeyException();
  
  @Message(id=41, value="Call to TraversableResolver.isReachable() threw an exception.")
  public abstract ValidationException getErrorDuringCallOfTraversableResolverIsReachableException(@Cause RuntimeException paramRuntimeException);
  
  @Message(id=42, value="Call to TraversableResolver.isCascadable() threw an exception.")
  public abstract ValidationException getErrorDuringCallOfTraversableResolverIsCascadableException(@Cause RuntimeException paramRuntimeException);
  
  @Message(id=43, value="Unable to expand default group list %1$s into sequence %2$s.")
  public abstract GroupDefinitionException getUnableToExpandDefaultGroupListException(List<?> paramList1, List<?> paramList2);
  
  @Message(id=44, value="At least one group has to be specified.")
  public abstract IllegalArgumentException getAtLeastOneGroupHasToBeSpecifiedException();
  
  @Message(id=45, value="A group has to be an interface. %s is not.")
  public abstract ValidationException getGroupHasToBeAnInterfaceException(String paramString);
  
  @Message(id=46, value="Sequence definitions are not allowed as composing parts of a sequence.")
  public abstract GroupDefinitionException getSequenceDefinitionsNotAllowedException();
  
  @Message(id=47, value="Cyclic dependency in groups definition")
  public abstract GroupDefinitionException getCyclicDependencyInGroupsDefinitionException();
  
  @Message(id=48, value="Unable to expand group sequence.")
  public abstract GroupDefinitionException getUnableToExpandGroupSequenceException();
  
  @Message(id=52, value="Default group sequence and default group sequence provider cannot be defined at the same time.")
  public abstract GroupDefinitionException getInvalidDefaultGroupSequenceDefinitionException();
  
  @Message(id=53, value="'Default.class' cannot appear in default group sequence list.")
  public abstract GroupDefinitionException getNoDefaultGroupInGroupSequenceException();
  
  @Message(id=54, value="%s must be part of the redefined default group sequence.")
  public abstract GroupDefinitionException getBeanClassMustBePartOfRedefinedDefaultGroupSequenceException(String paramString);
  
  @Message(id=55, value="The default group sequence provider defined for %s has the wrong type")
  public abstract GroupDefinitionException getWrongDefaultGroupSequenceProviderTypeException(String paramString);
  
  @Message(id=56, value="Method or constructor %1$s doesn't have a parameter with index %2$d.")
  public abstract IllegalArgumentException getInvalidExecutableParameterIndexException(String paramString, int paramInt);
  
  @Message(id=59, value="Unable to retrieve annotation parameter value.")
  public abstract ValidationException getUnableToRetrieveAnnotationParameterValueException(@Cause Exception paramException);
  
  @Message(id=62, value="Method or constructor %1$s has %2$s parameters, but the passed list of parameter meta data has a size of %3$s.")
  public abstract IllegalArgumentException getInvalidLengthOfParameterMetaDataListException(String paramString, int paramInt1, int paramInt2);
  
  @Message(id=63, value="Unable to instantiate %s.")
  public abstract ValidationException getUnableToInstantiateException(String paramString, @Cause Exception paramException);
  
  public abstract ValidationException getUnableToInstantiateException(Class<?> paramClass, @Cause Exception paramException);
  
  @Message(id=64, value="Unable to instantiate %1$s: %2$s.")
  public abstract ValidationException getUnableToInstantiateException(String paramString, Class<?> paramClass, @Cause Exception paramException);
  
  @Message(id=65, value="Unable to load class: %s from %s.")
  public abstract ValidationException getUnableToLoadClassException(String paramString, ClassLoader paramClassLoader, @Cause Exception paramException);
  
  @Message(id=68, value="Start index cannot be negative: %d.")
  public abstract IllegalArgumentException getStartIndexCannotBeNegativeException(int paramInt);
  
  @Message(id=69, value="End index cannot be negative: %d.")
  public abstract IllegalArgumentException getEndIndexCannotBeNegativeException(int paramInt);
  
  @Message(id=70, value="Invalid Range: %1$d > %2$d.")
  public abstract IllegalArgumentException getInvalidRangeException(int paramInt1, int paramInt2);
  
  @Message(id=71, value="A explicitly specified check digit must lie outside the interval: [%1$d, %2$d].")
  public abstract IllegalArgumentException getInvalidCheckDigitException(int paramInt1, int paramInt2);
  
  @Message(id=72, value="'%c' is not a digit.")
  public abstract NumberFormatException getCharacterIsNotADigitException(char paramChar);
  
  @Message(id=73, value="Parameters starting with 'valid' are not allowed in a constraint.")
  public abstract ConstraintDefinitionException getConstraintParametersCannotStartWithValidException();
  
  @Message(id=74, value="%2$s contains Constraint annotation, but does not contain a %1$s parameter.")
  public abstract ConstraintDefinitionException getConstraintWithoutMandatoryParameterException(String paramString1, String paramString2);
  
  @Message(id=75, value="%s contains Constraint annotation, but the payload parameter default value is not the empty array.")
  public abstract ConstraintDefinitionException getWrongDefaultValueForPayloadParameterException(String paramString);
  
  @Message(id=76, value="%s contains Constraint annotation, but the payload parameter is of wrong type.")
  public abstract ConstraintDefinitionException getWrongTypeForPayloadParameterException(String paramString, @Cause ClassCastException paramClassCastException);
  
  @Message(id=77, value="%s contains Constraint annotation, but the groups parameter default value is not the empty array.")
  public abstract ConstraintDefinitionException getWrongDefaultValueForGroupsParameterException(String paramString);
  
  @Message(id=78, value="%s contains Constraint annotation, but the groups parameter is of wrong type.")
  public abstract ConstraintDefinitionException getWrongTypeForGroupsParameterException(String paramString, @Cause ClassCastException paramClassCastException);
  
  @Message(id=79, value="%s contains Constraint annotation, but the message parameter is not of type java.lang.String.")
  public abstract ConstraintDefinitionException getWrongTypeForMessageParameterException(String paramString);
  
  @Message(id=80, value="Overridden constraint does not define an attribute with name %s.")
  public abstract ConstraintDefinitionException getOverriddenConstraintAttributeNotFoundException(String paramString);
  
  @Message(id=81, value="The overriding type of a composite constraint must be identical to the overridden one. Expected %1$s found %2$s.")
  public abstract ConstraintDefinitionException getWrongAttributeTypeForOverriddenConstraintException(String paramString, Class<?> paramClass);
  
  @Message(id=82, value="Wrong parameter type. Expected: %1$s Actual: %2$s.")
  public abstract ValidationException getWrongParameterTypeException(String paramString1, String paramString2);
  
  @Message(id=83, value="The specified annotation defines no parameter '%s'.")
  public abstract ValidationException getUnableToFindAnnotationParameterException(String paramString, @Cause NoSuchMethodException paramNoSuchMethodException);
  
  @Message(id=84, value="Unable to get '%1$s' from %2$s.")
  public abstract ValidationException getUnableToGetAnnotationParameterException(String paramString1, String paramString2, @Cause Exception paramException);
  
  @Message(id=85, value="No value provided for parameter '%1$s' of annotation @%2$s.")
  public abstract IllegalArgumentException getNoValueProvidedForAnnotationParameterException(String paramString1, String paramString2);
  
  @Message(id=86, value="Trying to instantiate %1$s with unknown parameter(s): %2$s.")
  public abstract RuntimeException getTryingToInstantiateAnnotationWithUnknownParametersException(Class<?> paramClass, Set<String> paramSet);
  
  @Message(id=87, value="Property name cannot be null or empty.")
  public abstract IllegalArgumentException getPropertyNameCannotBeNullOrEmptyException();
  
  @Message(id=88, value="Element type has to be FIELD or METHOD.")
  public abstract IllegalArgumentException getElementTypeHasToBeFieldOrMethodException();
  
  @Message(id=89, value="Member %s is neither a field nor a method.")
  public abstract IllegalArgumentException getMemberIsNeitherAFieldNorAMethodException(Member paramMember);
  
  @Message(id=90, value="Unable to access %s.")
  public abstract ValidationException getUnableToAccessMemberException(String paramString, @Cause Exception paramException);
  
  @Message(id=91, value="%s has to be a primitive type.")
  public abstract IllegalArgumentException getHasToBeAPrimitiveTypeException(Class<?> paramClass);
  
  @Message(id=93, value="null is an invalid type for a constraint validator.")
  public abstract ValidationException getNullIsAnInvalidTypeForAConstraintValidatorException();
  
  @Message(id=94, value="Missing actual type argument for type parameter: %s.")
  public abstract IllegalArgumentException getMissingActualTypeArgumentForTypeParameterException(TypeVariable<?> paramTypeVariable);
  
  @Message(id=95, value="Unable to instantiate constraint factory class %s.")
  public abstract ValidationException getUnableToInstantiateConstraintFactoryClassException(String paramString, @Cause ValidationException paramValidationException);
  
  @Message(id=96, value="Unable to open input stream for mapping file %s.")
  public abstract ValidationException getUnableToOpenInputStreamForMappingFileException(String paramString);
  
  @Message(id=97, value="Unable to instantiate message interpolator class %s.")
  public abstract ValidationException getUnableToInstantiateMessageInterpolatorClassException(String paramString, @Cause Exception paramException);
  
  @Message(id=98, value="Unable to instantiate traversable resolver class %s.")
  public abstract ValidationException getUnableToInstantiateTraversableResolverClassException(String paramString, @Cause Exception paramException);
  
  @Message(id=99, value="Unable to instantiate validation provider class %s.")
  public abstract ValidationException getUnableToInstantiateValidationProviderClassException(String paramString, @Cause Exception paramException);
  
  @Message(id=100, value="Unable to parse %s.")
  public abstract ValidationException getUnableToParseValidationXmlFileException(String paramString, @Cause Exception paramException);
  
  @Message(id=101, value="%s is not an annotation.")
  public abstract ValidationException getIsNotAnAnnotationException(String paramString);
  
  @Message(id=102, value="%s is not a constraint validator class.")
  public abstract ValidationException getIsNotAConstraintValidatorClassException(Class<?> paramClass);
  
  @Message(id=103, value="%s is configured at least twice in xml.")
  public abstract ValidationException getBeanClassHasAlreadyBeConfiguredInXmlException(String paramString);
  
  @Message(id=104, value="%1$s is defined twice in mapping xml for bean %2$s.")
  public abstract ValidationException getIsDefinedTwiceInMappingXmlForBeanException(String paramString1, String paramString2);
  
  @Message(id=105, value="%1$s does not contain the fieldType %2$s.")
  public abstract ValidationException getBeanDoesNotContainTheFieldException(String paramString1, String paramString2);
  
  @Message(id=106, value="%1$s does not contain the property %2$s.")
  public abstract ValidationException getBeanDoesNotContainThePropertyException(String paramString1, String paramString2);
  
  @Message(id=107, value="Annotation of type %1$s does not contain a parameter %2$s.")
  public abstract ValidationException getAnnotationDoesNotContainAParameterException(String paramString1, String paramString2);
  
  @Message(id=108, value="Attempt to specify an array where single value is expected.")
  public abstract ValidationException getAttemptToSpecifyAnArrayWhereSingleValueIsExpectedException();
  
  @Message(id=109, value="Unexpected parameter value.")
  public abstract ValidationException getUnexpectedParameterValueException();
  
  public abstract ValidationException getUnexpectedParameterValueException(@Cause ClassCastException paramClassCastException);
  
  @Message(id=110, value="Invalid %s format.")
  public abstract ValidationException getInvalidNumberFormatException(String paramString, @Cause NumberFormatException paramNumberFormatException);
  
  @Message(id=111, value="Invalid char value: %s.")
  public abstract ValidationException getInvalidCharValueException(String paramString);
  
  @Message(id=112, value="Invalid return type: %s. Should be a enumeration type.")
  public abstract ValidationException getInvalidReturnTypeException(Class<?> paramClass, @Cause ClassCastException paramClassCastException);
  
  @Message(id=113, value="%s, %s, %s are reserved parameter names.")
  public abstract ValidationException getReservedParameterNamesException(String paramString1, String paramString2, String paramString3);
  
  @Message(id=114, value="Specified payload class %s does not implement javax.validation.Payload")
  public abstract ValidationException getWrongPayloadClassException(String paramString);
  
  @Message(id=115, value="Error parsing mapping file.")
  public abstract ValidationException getErrorParsingMappingFileException(@Cause Exception paramException);
  
  @Message(id=116, value="%s")
  public abstract IllegalArgumentException getIllegalArgumentException(String paramString);
  
  @Message(id=118, value="Unable to cast %s (with element kind %s) to %s")
  public abstract ClassCastException getUnableToNarrowNodeTypeException(String paramString1, ElementKind paramElementKind, String paramString2);
  
  @LogMessage(level=Logger.Level.INFO)
  @Message(id=119, value="Using %s as parameter name provider.")
  public abstract void usingParameterNameProvider(String paramString);
  
  @Message(id=120, value="Unable to instantiate parameter name provider class %s.")
  public abstract ValidationException getUnableToInstantiateParameterNameProviderClassException(String paramString, @Cause ValidationException paramValidationException);
  
  @Message(id=121, value="Unable to parse %s.")
  public abstract ValidationException getUnableToDetermineSchemaVersionException(String paramString, @Cause XMLStreamException paramXMLStreamException);
  
  @Message(id=122, value="Unsupported schema version for %s: %s.")
  public abstract ValidationException getUnsupportedSchemaVersionException(String paramString1, String paramString2);
  
  @Message(id=124, value="Found multiple group conversions for source group %s: %s.")
  public abstract ConstraintDeclarationException getMultipleGroupConversionsForSameSourceException(Class<?> paramClass, Set<Class<?>> paramSet);
  
  @Message(id=125, value="Found group conversions for non-cascading element: %s.")
  public abstract ConstraintDeclarationException getGroupConversionOnNonCascadingElementException(String paramString);
  
  @Message(id=127, value="Found group conversion using a group sequence as source: %s.")
  public abstract ConstraintDeclarationException getGroupConversionForSequenceException(Class<?> paramClass);
  
  @LogMessage(level=Logger.Level.WARN)
  @Message(id=129, value="EL expression '%s' references an unknown property")
  public abstract void unknownPropertyInExpressionLanguage(String paramString, @Cause Exception paramException);
  
  @LogMessage(level=Logger.Level.WARN)
  @Message(id=130, value="Error in EL expression '%s'")
  public abstract void errorInExpressionLanguage(String paramString, @Cause Exception paramException);
  
  @Message(id=131, value="A method return value must not be marked for cascaded validation more than once in a class hierarchy, but the following two methods are marked as such: %s, %s.")
  public abstract ConstraintDeclarationException getMethodReturnValueMustNotBeMarkedMoreThanOnceForCascadedValidationException(Member paramMember1, Member paramMember2);
  
  @Message(id=132, value="Void methods must not be constrained or marked for cascaded validation, but method %s is.")
  public abstract ConstraintDeclarationException getVoidMethodsMustNotBeConstrainedException(Member paramMember);
  
  @Message(id=133, value="%1$s does not contain a constructor with the parameter types %2$s.")
  public abstract ValidationException getBeanDoesNotContainConstructorException(String paramString1, String paramString2);
  
  @Message(id=134, value="Unable to load parameter of type '%1$s' in %2$s.")
  public abstract ValidationException getInvalidParameterTypeException(String paramString1, String paramString2);
  
  @Message(id=135, value="%1$s does not contain a method with the name '%2$s' and parameter types %3$s.")
  public abstract ValidationException getBeanDoesNotContainMethodException(String paramString1, String paramString2, List<Class<?>> paramList);
  
  @Message(id=136, value="The specified constraint annotation class %1$s cannot be loaded.")
  public abstract ValidationException getUnableToLoadConstraintAnnotationClassException(String paramString, @Cause Exception paramException);
  
  @Message(id=137, value="The method '%1$s' is defined twice in the mapping xml for bean %2$s.")
  public abstract ValidationException getMethodIsDefinedTwiceInMappingXmlForBeanException(String paramString1, String paramString2);
  
  @Message(id=138, value="The constructor '%1$s' is defined twice in the mapping xml for bean %2$s.")
  public abstract ValidationException getConstructorIsDefinedTwiceInMappingXmlForBeanException(String paramString1, String paramString2);
  
  @Message(id=139, value="The constraint '%1$s' defines multiple cross parameter validators. Only one is allowed.")
  public abstract ConstraintDefinitionException getMultipleCrossParameterValidatorClassesException(String paramString);
  
  @Message(id=141, value="The constraint %1$s used ConstraintTarget#IMPLICIT where the target cannot be inferred.")
  public abstract ConstraintDeclarationException getImplicitConstraintTargetInAmbiguousConfigurationException(String paramString);
  
  @Message(id=142, value="Cross parameter constraint %1$s is illegally placed on a parameterless method or constructor '%2$s'.")
  public abstract ConstraintDeclarationException getCrossParameterConstraintOnMethodWithoutParametersException(String paramString1, String paramString2);
  
  @Message(id=143, value="Cross parameter constraint %1$s is illegally placed on class level.")
  public abstract ConstraintDeclarationException getCrossParameterConstraintOnClassException(String paramString);
  
  @Message(id=144, value="Cross parameter constraint %1$s is illegally placed on field '%2$s'.")
  public abstract ConstraintDeclarationException getCrossParameterConstraintOnFieldException(String paramString1, String paramString2);
  
  @Message(id=146, value="No parameter nodes may be added since path %s doesn't refer to a cross-parameter constraint.")
  public abstract IllegalStateException getParameterNodeAddedForNonCrossParameterConstraintException(Path paramPath);
  
  @Message(id=147, value="%1$s is configured multiple times (note, <getter> and <method> nodes for the same method are not allowed)")
  public abstract ValidationException getConstrainedElementConfiguredMultipleTimesException(String paramString);
  
  @LogMessage(level=Logger.Level.WARN)
  @Message(id=148, value="An exception occurred during evaluation of EL expression '%s'")
  public abstract void evaluatingExpressionLanguageExpressionCausedException(String paramString, @Cause Exception paramException);
  
  @Message(id=149, value="An exception occurred during message interpolation")
  public abstract ValidationException getExceptionOccurredDuringMessageInterpolationException(@Cause Exception paramException);
  
  @Message(id=150, value="The constraint '%s' defines multiple validators for the type '%s'. Only one is allowed.")
  public abstract UnexpectedTypeException getMultipleValidatorsForSameTypeException(String paramString1, String paramString2);
  
  @Message(id=151, value="A method overriding another method must not alter the parameter constraint configuration, but method %2$s changes the configuration of %1$s.")
  public abstract ConstraintDeclarationException getParameterConfigurationAlteredInSubTypeException(Member paramMember1, Member paramMember2);
  
  @Message(id=152, value="Two methods defined in parallel types must not declare parameter constraints, if they are overridden by the same method, but methods %s and %s both define parameter constraints.")
  public abstract ConstraintDeclarationException getParameterConstraintsDefinedInMethodsFromParallelTypesException(Member paramMember1, Member paramMember2);
  
  @Message(id=153, value="The constraint %1$s used ConstraintTarget#%2$s but is not specified on a method or constructor.")
  public abstract ConstraintDeclarationException getParametersOrReturnValueConstraintTargetGivenAtNonExecutableException(String paramString, ConstraintTarget paramConstraintTarget);
  
  @Message(id=154, value="Cross parameter constraint %1$s has no cross-parameter validator.")
  public abstract ConstraintDefinitionException getCrossParameterConstraintHasNoValidatorException(String paramString);
  
  @Message(id=155, value="Composed and composing constraints must have the same constraint type, but composed constraint %1$s has type %3$s, while composing constraint %2$s has type %4$s.")
  public abstract ConstraintDefinitionException getComposedAndComposingConstraintsHaveDifferentTypesException(String paramString1, String paramString2, ConstraintDescriptorImpl.ConstraintType paramConstraintType1, ConstraintDescriptorImpl.ConstraintType paramConstraintType2);
  
  @Message(id=156, value="Constraints with generic as well as cross-parameter validators must define an attribute validationAppliesTo(), but constraint %s doesn't.")
  public abstract ConstraintDefinitionException getGenericAndCrossParameterConstraintDoesNotDefineValidationAppliesToParameterException(String paramString);
  
  @Message(id=157, value="Return type of the attribute validationAppliesTo() of the constraint %s must be javax.validation.ConstraintTarget.")
  public abstract ConstraintDefinitionException getValidationAppliesToParameterMustHaveReturnTypeConstraintTargetException(String paramString);
  
  @Message(id=158, value="Default value of the attribute validationAppliesTo() of the constraint %s must be ConstraintTarget#IMPLICIT.")
  public abstract ConstraintDefinitionException getValidationAppliesToParameterMustHaveDefaultValueImplicitException(String paramString);
  
  @Message(id=159, value="Only constraints with generic as well as cross-parameter validators must define an attribute validationAppliesTo(), but constraint %s does.")
  public abstract ConstraintDefinitionException getValidationAppliesToParameterMustNotBeDefinedForNonGenericAndCrossParameterConstraintException(String paramString);
  
  @Message(id=160, value="Validator for cross-parameter constraint %s does not validate Object nor Object[].")
  public abstract ConstraintDefinitionException getValidatorForCrossParameterConstraintMustEitherValidateObjectOrObjectArrayException(String paramString);
  
  @Message(id=161, value="Two methods defined in parallel types must not define group conversions for a cascaded method return value, if they are overridden by the same method, but methods %s and %s both define parameter constraints.")
  public abstract ConstraintDeclarationException getMethodsFromParallelTypesMustNotDefineGroupConversionsForCascadedReturnValueException(Member paramMember1, Member paramMember2);
  
  @Message(id=162, value="The validated type %1$s does not specify the constructor/method: %2$s")
  public abstract IllegalArgumentException getMethodOrConstructorNotDefinedByValidatedTypeException(String paramString, Member paramMember);
  
  @Message(id=163, value="The actual parameter type '%1$s' is not assignable to the expected one '%2$s' for parameter %3$d of '%4$s'")
  public abstract IllegalArgumentException getParameterTypesDoNotMatchException(String paramString1, String paramString2, int paramInt, Member paramMember);
  
  @Message(id=164, value="%s has to be a auto-boxed type.")
  public abstract IllegalArgumentException getHasToBeABoxedTypeException(Class<?> paramClass);
  
  @Message(id=165, value="Mixing IMPLICIT and other executable types is not allowed.")
  public abstract IllegalArgumentException getMixingImplicitWithOtherExecutableTypesException();
  
  @Message(id=166, value="@ValidateOnExecution is not allowed on methods overriding a superclass method or implementing an interface. Check configuration for %1$s")
  public abstract ValidationException getValidateOnExecutionOnOverriddenOrInterfaceMethodException(Method paramMethod);
  
  @Message(id=167, value="A given constraint definition can only be overridden in one mapping file. %1$s is overridden in multiple files")
  public abstract ValidationException getOverridingConstraintDefinitionsInMultipleMappingFilesException(String paramString);
  
  @Message(id=168, value="The message descriptor '%1$s' contains an unbalanced meta character '%2$c' parameter.")
  public abstract MessageDescriptorFormatException getNonTerminatedParameterException(String paramString, char paramChar);
  
  @Message(id=169, value="The message descriptor '%1$s' has nested parameters.")
  public abstract MessageDescriptorFormatException getNestedParameterException(String paramString);
  
  @Message(id=170, value="No JSR-223 scripting engine could be bootstrapped for language \"%s\".")
  public abstract ConstraintDeclarationException getCreationOfScriptExecutorFailedException(String paramString, @Cause Exception paramException);
  
  @Message(id=171, value="%s is configured more than once via the programmatic constraint declaration API.")
  public abstract ValidationException getBeanClassHasAlreadyBeConfiguredViaProgrammaticApiException(String paramString);
  
  @Message(id=172, value="Property \"%2$s\" of type %1$s is configured more than once via the programmatic constraint declaration API.")
  public abstract ValidationException getPropertyHasAlreadyBeConfiguredViaProgrammaticApiException(String paramString1, String paramString2);
  
  @Message(id=173, value="Method %2$s of type %1$s is configured more than once via the programmatic constraint declaration API.")
  public abstract ValidationException getMethodHasAlreadyBeConfiguredViaProgrammaticApiException(String paramString1, String paramString2);
  
  @Message(id=174, value="Parameter %3$s of method or constructor %2$s of type %1$s is configured more than once via the programmatic constraint declaration API.")
  public abstract ValidationException getParameterHasAlreadyBeConfiguredViaProgrammaticApiException(String paramString1, String paramString2, int paramInt);
  
  @Message(id=175, value="The return value of method or constructor %2$s of type %1$s is configured more than once via the programmatic constraint declaration API.")
  public abstract ValidationException getReturnValueHasAlreadyBeConfiguredViaProgrammaticApiException(String paramString1, String paramString2);
  
  @Message(id=176, value="Constructor %2$s of type %1$s is configured more than once via the programmatic constraint declaration API.")
  public abstract ValidationException getConstructorHasAlreadyBeConfiguredViaProgrammaticApiException(String paramString1, String paramString2);
  
  @Message(id=177, value="Cross-parameter constraints for the method or constructor %2$s of type %1$s are declared more than once via the programmatic constraint declaration API.")
  public abstract ValidationException getCrossParameterElementHasAlreadyBeConfiguredViaProgrammaticApiException(String paramString1, String paramString2);
  
  @Message(id=178, value="Multiplier cannot be negative: %d.")
  public abstract IllegalArgumentException getMultiplierCannotBeNegativeException(int paramInt);
  
  @Message(id=179, value="Weight cannot be negative: %d.")
  public abstract IllegalArgumentException getWeightCannotBeNegativeException(int paramInt);
  
  @Message(id=180, value="'%c' is not a digit nor a letter.")
  public abstract IllegalArgumentException getTreatCheckAsIsNotADigitNorALetterException(int paramInt);
  
  @Message(id=181, value="Wrong number of parameters. Method or constructor %1$s expects %2$d parameters, but got %3$d.")
  public abstract IllegalArgumentException getInvalidParameterCountForExecutableException(String paramString, int paramInt1, int paramInt2);
  
  @Message(id=182, value="No validation value unwrapper is registered for type '%1$s'.")
  public abstract ValidationException getNoUnwrapperFoundForTypeException(String paramString);
  
  @Message(id=183, value="Unable to initialize 'javax.el.ExpressionFactory'. Check that you have the EL dependencies on the classpath, or use ParameterMessageInterpolator instead")
  public abstract ValidationException getUnableToInitializeELExpressionFactoryException(@Cause Throwable paramThrowable);
  
  @LogMessage(level=Logger.Level.WARN)
  @Message(id=184, value="ParameterMessageInterpolator has been chosen, EL interpolation will not be supported")
  public abstract void creationOfParameterMessageInterpolation();
  
  @LogMessage(level=Logger.Level.WARN)
  @Message(id=185, value="Message contains EL expression: %1s, which is unsupported with chosen Interpolator")
  public abstract void getElUnsupported(String paramString);
  
  @Message(id=186, value="The constraint of type '%2$s' defined on '%1$s' has multiple matching constraint validators which is due to an additional value handler of type '%3$s'. It is unclear which value needs validating. Clarify configuration via @UnwrapValidatedValue.")
  public abstract UnexpectedTypeException getConstraintValidatorExistsForWrapperAndWrappedValueException(String paramString1, String paramString2, String paramString3);
  
  @Message(id=187, value="When using type annotation constraints on parameterized iterables or map @Valid must be used. Check %s#%s")
  public abstract ValidationException getTypeAnnotationConstraintOnIterableRequiresUseOfValidAnnotationException(String paramString1, String paramString2);
  
  @LogMessage(level=Logger.Level.DEBUG)
  @Message(id=188, value="Parameterized type with more than one argument is not supported: %s")
  public abstract void parameterizedTypeWithMoreThanOneTypeArgumentIsNotSupported(String paramString);
  
  @Message(id=189, value="The configuration of value unwrapping for property '%s' of bean '%s' is inconsistent between the field and its getter.")
  public abstract ConstraintDeclarationException getInconsistentValueUnwrappingConfigurationBetweenFieldAndItsGetterException(String paramString1, String paramString2);
  
  @Message(id=190, value="Unable to parse %s.")
  public abstract ValidationException getUnableToCreateXMLEventReader(String paramString, @Cause Exception paramException);
  
  @Message(id=191, value="Error creating unwrapper: %s")
  public abstract ValidationException validatedValueUnwrapperCannotBeCreated(String paramString, @Cause Exception paramException);
  
  @LogMessage(level=Logger.Level.WARN)
  @Message(id=192, value="Couldn't determine Java version from value %1s; Not enabling features requiring Java 8")
  public abstract void unknownJvmVersion(String paramString);
  
  @Message(id=193, value="%s is configured more than once via the programmatic constraint definition API.")
  public abstract ValidationException getConstraintHasAlreadyBeenConfiguredViaProgrammaticApiException(String paramString);
  
  @Message(id=194, value="An empty element is only supported when a CharSequence is expected.")
  public abstract ValidationException getEmptyElementOnlySupportedWhenCharSequenceIsExpectedExpection();
  
  @Message(id=195, value="Unable to reach the property to validate for the bean %s and the property path %s. A property is null along the way.")
  public abstract ValidationException getUnableToReachPropertyToValidateException(Object paramObject, Path paramPath);
  
  @Message(id=196, value="Unable to convert the Type %s to a Class.")
  public abstract ValidationException getUnableToConvertTypeToClassException(Type paramType);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\logging\Log.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */