/*    */ package org.hibernate.validator.internal.util.logging;
/*    */ 
/*    */ import org.jboss.logging.annotations.Message;
/*    */ import org.jboss.logging.annotations.Message.Format;
/*    */ import org.jboss.logging.annotations.MessageBundle;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @MessageBundle(projectCode="HV")
/*    */ public abstract interface Messages
/*    */ {
/* 20 */   public static final Messages MESSAGES = (Messages)org.jboss.logging.Messages.getBundle(Messages.class);
/*    */   
/*    */   @Message(value="must not be null.", format=Message.Format.NO_FORMAT)
/*    */   public abstract String mustNotBeNull();
/*    */   
/*    */   @Message("%s must not be null.")
/*    */   public abstract String mustNotBeNull(String paramString);
/*    */   
/*    */   @Message("The parameter \"%s\" must not be null.")
/*    */   public abstract String parameterMustNotBeNull(String paramString);
/*    */   
/*    */   @Message("The parameter \"%s\" must not be empty.")
/*    */   public abstract String parameterMustNotBeEmpty(String paramString);
/*    */   
/*    */   @Message(value="The bean type cannot be null.", format=Message.Format.NO_FORMAT)
/*    */   public abstract String beanTypeCannotBeNull();
/*    */   
/*    */   @Message(value="null is not allowed as property path.", format=Message.Format.NO_FORMAT)
/*    */   public abstract String propertyPathCannotBeNull();
/*    */   
/*    */   @Message(value="The property name must not be empty.", format=Message.Format.NO_FORMAT)
/*    */   public abstract String propertyNameMustNotBeEmpty();
/*    */   
/*    */   @Message(value="null passed as group name.", format=Message.Format.NO_FORMAT)
/*    */   public abstract String groupMustNotBeNull();
/*    */   
/*    */   @Message(value="The bean type must not be null when creating a constraint mapping.", format=Message.Format.NO_FORMAT)
/*    */   public abstract String beanTypeMustNotBeNull();
/*    */   
/*    */   @Message(value="The method name must not be null.", format=Message.Format.NO_FORMAT)
/*    */   public abstract String methodNameMustNotBeNull();
/*    */   
/*    */   @Message(value="The object to be validated must not be null.", format=Message.Format.NO_FORMAT)
/*    */   public abstract String validatedObjectMustNotBeNull();
/*    */   
/*    */   @Message(value="The method to be validated must not be null.", format=Message.Format.NO_FORMAT)
/*    */   public abstract String validatedMethodMustNotBeNull();
/*    */   
/*    */   @Message(value="The class cannot be null.", format=Message.Format.NO_FORMAT)
/*    */   public abstract String classCannotBeNull();
/*    */   
/*    */   @Message(value="Class is null.", format=Message.Format.NO_FORMAT)
/*    */   public abstract String classIsNull();
/*    */   
/*    */   @Message("No JSR 223 script engine found for language \"%s\".")
/*    */   public abstract String unableToFindScriptEngine(String paramString);
/*    */   
/*    */   @Message(value="The constructor to be validated must not be null.", format=Message.Format.NO_FORMAT)
/*    */   public abstract String validatedConstructorMustNotBeNull();
/*    */   
/*    */   @Message(value="The method parameter array cannot not be null.", format=Message.Format.NO_FORMAT)
/*    */   public abstract String validatedParameterArrayMustNotBeNull();
/*    */   
/*    */   @Message(value="The created instance must not be null.", format=Message.Format.NO_FORMAT)
/*    */   public abstract String validatedConstructorCreatedInstanceMustNotBeNull();
/*    */   
/*    */   @Message("The input stream for #addMapping() cannot be null.")
/*    */   public abstract String inputStreamCannotBeNull();
/*    */   
/*    */   @Message("Constraints on the parameters of constructors of non-static inner classes are not supported if those parameters have a generic type due to JDK bug JDK-5087240.")
/*    */   public abstract String constraintOnConstructorOfNonStaticInnerClass();
/*    */   
/*    */   @Message("Custom parameterized types with more than one type argument are not supported and will not be checked for type use constraints.")
/*    */   public abstract String parameterizedTypesWithMoreThanOneTypeArgument();
/*    */   
/*    */   @Message("Hibernate Validator cannot instantiate AggregateResourceBundle.CONTROL. This can happen most notably in a Google App Engine environment. A PlatformResourceBundleLocator without bundle aggregation was created. This only effects you in case you are using multiple ConstraintDefinitionContributor jars. ConstraintDefinitionContributors are a Hibernate Validator specific feature. All Bean Validation features work as expected. See also https://hibernate.atlassian.net/browse/HV-1023.")
/*    */   public abstract String unableToUseResourceBundleAggregation();
/*    */   
/*    */   @Message(value="The annotation type must not be null when creating a constraint definition.", format=Message.Format.NO_FORMAT)
/*    */   public abstract String annotationTypeMustNotBeNull();
/*    */   
/*    */   @Message(value="The annotation type must be annotated with @javax.validation.Constraint when creating a constraint definition.", format=Message.Format.NO_FORMAT)
/*    */   public abstract String annotationTypeMustBeAnnotatedWithConstraint();
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\logging\Messages.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */