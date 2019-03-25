/*    */ package org.hibernate.validator.internal.constraintvalidators.hv;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorContext;
/*    */ import javax.validation.constraintvalidation.SupportedValidationTarget;
/*    */ import org.hibernate.validator.constraints.ParameterScriptAssert;
/*    */ import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
/*    */ import org.hibernate.validator.internal.util.CollectionHelper;
/*    */ import org.hibernate.validator.internal.util.Contracts;
/*    */ import org.hibernate.validator.internal.util.logging.Messages;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @SupportedValidationTarget({javax.validation.constraintvalidation.ValidationTarget.PARAMETERS})
/*    */ public class ParameterScriptAssertValidator
/*    */   implements ConstraintValidator<ParameterScriptAssert, Object[]>
/*    */ {
/*    */   private ScriptAssertContext scriptAssertContext;
/*    */   
/*    */   public void initialize(ParameterScriptAssert constraintAnnotation)
/*    */   {
/* 35 */     validateParameters(constraintAnnotation);
/* 36 */     this.scriptAssertContext = new ScriptAssertContext(constraintAnnotation.lang(), constraintAnnotation.script());
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean isValid(Object[] arguments, ConstraintValidatorContext constraintValidatorContext)
/*    */   {
/* 42 */     List<String> parameterNames = ((ConstraintValidatorContextImpl)constraintValidatorContext).getMethodParameterNames();
/*    */     
/* 44 */     Map<String, Object> bindings = getBindings(arguments, parameterNames);
/*    */     
/* 46 */     return this.scriptAssertContext.evaluateScriptAssertExpression(bindings);
/*    */   }
/*    */   
/*    */   private Map<String, Object> getBindings(Object[] arguments, List<String> parameterNames) {
/* 50 */     Map<String, Object> bindings = CollectionHelper.newHashMap();
/*    */     
/* 52 */     for (int i = 0; i < arguments.length; i++) {
/* 53 */       bindings.put(parameterNames.get(i), arguments[i]);
/*    */     }
/*    */     
/* 56 */     return bindings;
/*    */   }
/*    */   
/*    */   private void validateParameters(ParameterScriptAssert constraintAnnotation) {
/* 60 */     Contracts.assertNotEmpty(constraintAnnotation.script(), Messages.MESSAGES.parameterMustNotBeEmpty("script"));
/* 61 */     Contracts.assertNotEmpty(constraintAnnotation.lang(), Messages.MESSAGES.parameterMustNotBeEmpty("lang"));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\hv\ParameterScriptAssertValidator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */