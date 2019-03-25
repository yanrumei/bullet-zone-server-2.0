package org.hibernate.validator.constraints;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy={})
@Documented
public @interface ParameterScriptAssert
{
  String message() default "{org.hibernate.validator.constraints.ParametersScriptAssert.message}";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};
  
  String lang();
  
  String script();
  
  @Target({java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.METHOD})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public static @interface List
  {
    ParameterScriptAssert[] value();
  }
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\constraints\ParameterScriptAssert.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */