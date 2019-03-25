/*     */ package org.hibernate.validator.internal.util.logging;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Messages_$bundle
/*     */   implements Messages, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  14 */   public static final bundle INSTANCE = new bundle();
/*     */   private static final String mustNotBeNull0 = "must not be null."; private static final String mustNotBeNull1 = "%s must not be null."; private static final String parameterMustNotBeNull = "The parameter \"%s\" must not be null."; private static final String parameterMustNotBeEmpty = "The parameter \"%s\" must not be empty.";
/*  16 */   protected Object readResolve() { return INSTANCE; }
/*     */   
/*     */   private static final String beanTypeCannotBeNull = "The bean type cannot be null.";
/*     */   private static final String propertyPathCannotBeNull = "null is not allowed as property path."; private static final String propertyNameMustNotBeEmpty = "The property name must not be empty."; private static final String groupMustNotBeNull = "null passed as group name.";
/*  20 */   protected String mustNotBeNull0$str() { return "must not be null."; }
/*     */   
/*     */   private static final String beanTypeMustNotBeNull = "The bean type must not be null when creating a constraint mapping.";
/*     */   private static final String methodNameMustNotBeNull = "The method name must not be null."; private static final String validatedObjectMustNotBeNull = "The object to be validated must not be null.";
/*  24 */   public final String mustNotBeNull() { return mustNotBeNull0$str(); }
/*     */   
/*     */   private static final String validatedMethodMustNotBeNull = "The method to be validated must not be null.";
/*     */   private static final String classCannotBeNull = "The class cannot be null."; private static final String classIsNull = "Class is null.";
/*  28 */   protected String mustNotBeNull1$str() { return "%s must not be null."; }
/*     */   
/*     */   private static final String unableToFindScriptEngine = "No JSR 223 script engine found for language \"%s\".";
/*     */   private static final String validatedConstructorMustNotBeNull = "The constructor to be validated must not be null."; private static final String validatedParameterArrayMustNotBeNull = "The method parameter array cannot not be null.";
/*  32 */   public final String mustNotBeNull(String parameterName) { return String.format(mustNotBeNull1$str(), new Object[] { parameterName }); }
/*     */   
/*     */   private static final String validatedConstructorCreatedInstanceMustNotBeNull = "The created instance must not be null.";
/*     */   private static final String inputStreamCannotBeNull = "The input stream for #addMapping() cannot be null."; private static final String constraintOnConstructorOfNonStaticInnerClass = "Constraints on the parameters of constructors of non-static inner classes are not supported if those parameters have a generic type due to JDK bug JDK-5087240.";
/*  36 */   protected String parameterMustNotBeNull$str() { return "The parameter \"%s\" must not be null."; }
/*     */   
/*     */   private static final String parameterizedTypesWithMoreThanOneTypeArgument = "Custom parameterized types with more than one type argument are not supported and will not be checked for type use constraints.";
/*     */   private static final String unableToUseResourceBundleAggregation = "Hibernate Validator cannot instantiate AggregateResourceBundle.CONTROL. This can happen most notably in a Google App Engine environment. A PlatformResourceBundleLocator without bundle aggregation was created. This only effects you in case you are using multiple ConstraintDefinitionContributor jars. ConstraintDefinitionContributors are a Hibernate Validator specific feature. All Bean Validation features work as expected. See also https://hibernate.atlassian.net/browse/HV-1023."; private static final String annotationTypeMustNotBeNull = "The annotation type must not be null when creating a constraint definition.";
/*  40 */   public final String parameterMustNotBeNull(String parameterName) { return String.format(parameterMustNotBeNull$str(), new Object[] { parameterName }); }
/*     */   
/*     */   private static final String annotationTypeMustBeAnnotatedWithConstraint = "The annotation type must be annotated with @javax.validation.Constraint when creating a constraint definition.";
/*     */   protected String parameterMustNotBeEmpty$str() {
/*  44 */     return "The parameter \"%s\" must not be empty."; }
/*     */   
/*     */ 
/*     */   public final String parameterMustNotBeEmpty(String parameterName) {
/*  48 */     return String.format(parameterMustNotBeEmpty$str(), new Object[] { parameterName });
/*     */   }
/*     */   
/*     */   protected String beanTypeCannotBeNull$str() {
/*  52 */     return "The bean type cannot be null.";
/*     */   }
/*     */   
/*     */   public final String beanTypeCannotBeNull() {
/*  56 */     return beanTypeCannotBeNull$str();
/*     */   }
/*     */   
/*     */   protected String propertyPathCannotBeNull$str() {
/*  60 */     return "null is not allowed as property path.";
/*     */   }
/*     */   
/*     */   public final String propertyPathCannotBeNull() {
/*  64 */     return propertyPathCannotBeNull$str();
/*     */   }
/*     */   
/*     */   protected String propertyNameMustNotBeEmpty$str() {
/*  68 */     return "The property name must not be empty.";
/*     */   }
/*     */   
/*     */   public final String propertyNameMustNotBeEmpty() {
/*  72 */     return propertyNameMustNotBeEmpty$str();
/*     */   }
/*     */   
/*     */   protected String groupMustNotBeNull$str() {
/*  76 */     return "null passed as group name.";
/*     */   }
/*     */   
/*     */   public final String groupMustNotBeNull() {
/*  80 */     return groupMustNotBeNull$str();
/*     */   }
/*     */   
/*     */   protected String beanTypeMustNotBeNull$str() {
/*  84 */     return "The bean type must not be null when creating a constraint mapping.";
/*     */   }
/*     */   
/*     */   public final String beanTypeMustNotBeNull() {
/*  88 */     return beanTypeMustNotBeNull$str();
/*     */   }
/*     */   
/*     */   protected String methodNameMustNotBeNull$str() {
/*  92 */     return "The method name must not be null.";
/*     */   }
/*     */   
/*     */   public final String methodNameMustNotBeNull() {
/*  96 */     return methodNameMustNotBeNull$str();
/*     */   }
/*     */   
/*     */   protected String validatedObjectMustNotBeNull$str() {
/* 100 */     return "The object to be validated must not be null.";
/*     */   }
/*     */   
/*     */   public final String validatedObjectMustNotBeNull() {
/* 104 */     return validatedObjectMustNotBeNull$str();
/*     */   }
/*     */   
/*     */   protected String validatedMethodMustNotBeNull$str() {
/* 108 */     return "The method to be validated must not be null.";
/*     */   }
/*     */   
/*     */   public final String validatedMethodMustNotBeNull() {
/* 112 */     return validatedMethodMustNotBeNull$str();
/*     */   }
/*     */   
/*     */   protected String classCannotBeNull$str() {
/* 116 */     return "The class cannot be null.";
/*     */   }
/*     */   
/*     */   public final String classCannotBeNull() {
/* 120 */     return classCannotBeNull$str();
/*     */   }
/*     */   
/*     */   protected String classIsNull$str() {
/* 124 */     return "Class is null.";
/*     */   }
/*     */   
/*     */   public final String classIsNull() {
/* 128 */     return classIsNull$str();
/*     */   }
/*     */   
/*     */   protected String unableToFindScriptEngine$str() {
/* 132 */     return "No JSR 223 script engine found for language \"%s\".";
/*     */   }
/*     */   
/*     */   public final String unableToFindScriptEngine(String languageName) {
/* 136 */     return String.format(unableToFindScriptEngine$str(), new Object[] { languageName });
/*     */   }
/*     */   
/*     */   protected String validatedConstructorMustNotBeNull$str() {
/* 140 */     return "The constructor to be validated must not be null.";
/*     */   }
/*     */   
/*     */   public final String validatedConstructorMustNotBeNull() {
/* 144 */     return validatedConstructorMustNotBeNull$str();
/*     */   }
/*     */   
/*     */   protected String validatedParameterArrayMustNotBeNull$str() {
/* 148 */     return "The method parameter array cannot not be null.";
/*     */   }
/*     */   
/*     */   public final String validatedParameterArrayMustNotBeNull() {
/* 152 */     return validatedParameterArrayMustNotBeNull$str();
/*     */   }
/*     */   
/*     */   protected String validatedConstructorCreatedInstanceMustNotBeNull$str() {
/* 156 */     return "The created instance must not be null.";
/*     */   }
/*     */   
/*     */   public final String validatedConstructorCreatedInstanceMustNotBeNull() {
/* 160 */     return validatedConstructorCreatedInstanceMustNotBeNull$str();
/*     */   }
/*     */   
/*     */   protected String inputStreamCannotBeNull$str() {
/* 164 */     return "The input stream for #addMapping() cannot be null.";
/*     */   }
/*     */   
/*     */   public final String inputStreamCannotBeNull() {
/* 168 */     return String.format(inputStreamCannotBeNull$str(), new Object[0]);
/*     */   }
/*     */   
/*     */   protected String constraintOnConstructorOfNonStaticInnerClass$str() {
/* 172 */     return "Constraints on the parameters of constructors of non-static inner classes are not supported if those parameters have a generic type due to JDK bug JDK-5087240.";
/*     */   }
/*     */   
/*     */   public final String constraintOnConstructorOfNonStaticInnerClass() {
/* 176 */     return String.format(constraintOnConstructorOfNonStaticInnerClass$str(), new Object[0]);
/*     */   }
/*     */   
/*     */   protected String parameterizedTypesWithMoreThanOneTypeArgument$str() {
/* 180 */     return "Custom parameterized types with more than one type argument are not supported and will not be checked for type use constraints.";
/*     */   }
/*     */   
/*     */   public final String parameterizedTypesWithMoreThanOneTypeArgument() {
/* 184 */     return String.format(parameterizedTypesWithMoreThanOneTypeArgument$str(), new Object[0]);
/*     */   }
/*     */   
/*     */   protected String unableToUseResourceBundleAggregation$str() {
/* 188 */     return "Hibernate Validator cannot instantiate AggregateResourceBundle.CONTROL. This can happen most notably in a Google App Engine environment. A PlatformResourceBundleLocator without bundle aggregation was created. This only effects you in case you are using multiple ConstraintDefinitionContributor jars. ConstraintDefinitionContributors are a Hibernate Validator specific feature. All Bean Validation features work as expected. See also https://hibernate.atlassian.net/browse/HV-1023.";
/*     */   }
/*     */   
/*     */   public final String unableToUseResourceBundleAggregation() {
/* 192 */     return String.format(unableToUseResourceBundleAggregation$str(), new Object[0]);
/*     */   }
/*     */   
/*     */   protected String annotationTypeMustNotBeNull$str() {
/* 196 */     return "The annotation type must not be null when creating a constraint definition.";
/*     */   }
/*     */   
/*     */   public final String annotationTypeMustNotBeNull() {
/* 200 */     return annotationTypeMustNotBeNull$str();
/*     */   }
/*     */   
/*     */   protected String annotationTypeMustBeAnnotatedWithConstraint$str() {
/* 204 */     return "The annotation type must be annotated with @javax.validation.Constraint when creating a constraint definition.";
/*     */   }
/*     */   
/*     */   public final String annotationTypeMustBeAnnotatedWithConstraint() {
/* 208 */     return annotationTypeMustBeAnnotatedWithConstraint$str();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\logging\Messages_$bundle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */