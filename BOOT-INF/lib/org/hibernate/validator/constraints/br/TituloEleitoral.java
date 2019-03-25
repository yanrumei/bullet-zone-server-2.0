package org.hibernate.validator.constraints.br;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import org.hibernate.validator.constraints.Mod11Check.List;

@Pattern(regexp="[0-9]{12}")
@Mod11Check.List({@org.hibernate.validator.constraints.Mod11Check(threshold=9, endIndex=7, checkDigitIndex=10), @org.hibernate.validator.constraints.Mod11Check(threshold=9, startIndex=8, endIndex=10, checkDigitIndex=11)})
@ReportAsSingleViolation
@Documented
@Constraint(validatedBy={})
@SupportedValidationTarget({javax.validation.constraintvalidation.ValidationTarget.ANNOTATED_ELEMENT})
@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TituloEleitoral
{
  String message() default "{org.hibernate.validator.constraints.br.TituloEleitoral.message}";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\constraints\br\TituloEleitoral.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */