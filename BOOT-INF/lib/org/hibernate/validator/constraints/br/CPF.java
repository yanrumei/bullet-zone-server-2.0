package org.hibernate.validator.constraints.br;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern.List;

@Pattern.List({@javax.validation.constraints.Pattern(regexp="([0-9]{3}[.]?[0-9]{3}[.]?[0-9]{3}-[0-9]{2})|([0-9]{11})"), @javax.validation.constraints.Pattern(regexp="^(?:(?!000\\.?000\\.?000-?00).)*$"), @javax.validation.constraints.Pattern(regexp="^(?:(?!111\\.?111\\.?111-?11).)*$"), @javax.validation.constraints.Pattern(regexp="^(?:(?!222\\.?222\\.?222-?22).)*$"), @javax.validation.constraints.Pattern(regexp="^(?:(?!333\\.?333\\.?333-?33).)*$"), @javax.validation.constraints.Pattern(regexp="^(?:(?!444\\.?444\\.?444-?44).)*$"), @javax.validation.constraints.Pattern(regexp="^(?:(?!555\\.?555\\.?555-?55).)*$"), @javax.validation.constraints.Pattern(regexp="^(?:(?!666\\.?666\\.?666-?66).)*$"), @javax.validation.constraints.Pattern(regexp="^(?:(?!777\\.?777\\.?777-?77).)*$"), @javax.validation.constraints.Pattern(regexp="^(?:(?!888\\.?888\\.?888-?88).)*$"), @javax.validation.constraints.Pattern(regexp="^(?:(?!999\\.?999\\.?999-?99).)*$")})
@ReportAsSingleViolation
@Documented
@Constraint(validatedBy={})
@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CPF
{
  String message() default "{org.hibernate.validator.constraints.br.CPF.message}";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\constraints\br\CPF.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */