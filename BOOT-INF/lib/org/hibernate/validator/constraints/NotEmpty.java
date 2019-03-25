package org.hibernate.validator.constraints;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraintvalidation.SupportedValidationTarget;

@Documented
@Constraint(validatedBy={})
@SupportedValidationTarget({javax.validation.constraintvalidation.ValidationTarget.ANNOTATED_ELEMENT})
@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
@NotNull
@Size(min=1)
public @interface NotEmpty
{
  String message() default "{org.hibernate.validator.constraints.NotEmpty.message}";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};
  
  @Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.PARAMETER})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public static @interface List
  {
    NotEmpty[] value();
  }
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\constraints\NotEmpty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */