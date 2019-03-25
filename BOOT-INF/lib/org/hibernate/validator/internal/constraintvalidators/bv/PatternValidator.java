/*    */ package org.hibernate.validator.internal.constraintvalidators.bv;
/*    */ 
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.PatternSyntaxException;
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorContext;
/*    */ import javax.validation.constraints.Pattern.Flag;
/*    */ import org.hibernate.validator.internal.util.logging.Log;
/*    */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
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
/*    */ public class PatternValidator
/*    */   implements ConstraintValidator<javax.validation.constraints.Pattern, CharSequence>
/*    */ {
/* 23 */   private static final Log log = ;
/*    */   
/*    */   private java.util.regex.Pattern pattern;
/*    */   
/*    */   public void initialize(javax.validation.constraints.Pattern parameters)
/*    */   {
/* 29 */     Pattern.Flag[] flags = parameters.flags();
/* 30 */     int intFlag = 0;
/* 31 */     for (Pattern.Flag flag : flags) {
/* 32 */       intFlag |= flag.getValue();
/*    */     }
/*    */     try
/*    */     {
/* 36 */       this.pattern = java.util.regex.Pattern.compile(parameters.regexp(), intFlag);
/*    */     }
/*    */     catch (PatternSyntaxException e) {
/* 39 */       throw log.getInvalidRegularExpressionException(e);
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean isValid(CharSequence value, ConstraintValidatorContext constraintValidatorContext)
/*    */   {
/* 45 */     if (value == null) {
/* 46 */       return true;
/*    */     }
/* 48 */     Matcher m = this.pattern.matcher(value);
/* 49 */     return m.matches();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\bv\PatternValidator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */