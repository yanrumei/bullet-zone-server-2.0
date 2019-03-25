/*    */ package org.hibernate.validator.internal.constraintvalidators.hv;
/*    */ 
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorContext;
/*    */ import org.hibernate.validator.constraints.ScriptAssert;
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
/*    */ 
/*    */ public class ScriptAssertValidator
/*    */   implements ConstraintValidator<ScriptAssert, Object>
/*    */ {
/*    */   private String alias;
/*    */   private ScriptAssertContext scriptAssertContext;
/*    */   
/*    */   public void initialize(ScriptAssert constraintAnnotation)
/*    */   {
/* 31 */     validateParameters(constraintAnnotation);
/*    */     
/* 33 */     this.alias = constraintAnnotation.alias();
/* 34 */     this.scriptAssertContext = new ScriptAssertContext(constraintAnnotation.lang(), constraintAnnotation.script());
/*    */   }
/*    */   
/*    */   public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext)
/*    */   {
/* 39 */     return this.scriptAssertContext.evaluateScriptAssertExpression(value, this.alias);
/*    */   }
/*    */   
/*    */   private void validateParameters(ScriptAssert constraintAnnotation) {
/* 43 */     Contracts.assertNotEmpty(constraintAnnotation.script(), Messages.MESSAGES.parameterMustNotBeEmpty("script"));
/* 44 */     Contracts.assertNotEmpty(constraintAnnotation.lang(), Messages.MESSAGES.parameterMustNotBeEmpty("lang"));
/* 45 */     Contracts.assertNotEmpty(constraintAnnotation.alias(), Messages.MESSAGES.parameterMustNotBeEmpty("alias"));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\hv\ScriptAssertValidator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */