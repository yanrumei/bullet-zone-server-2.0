package org.hibernate.validator.constraints;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy={})
@Documented
public @interface ScriptAssert
{
  String message() default "{org.hibernate.validator.constraints.ScriptAssert.message}";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};
  
  String lang();
  
  String script();
  
  String alias() default "_this";
  
  @Target({java.lang.annotation.ElementType.TYPE})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public static @interface List
  {
    ScriptAssert[] value();
  }
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\constraints\ScriptAssert.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */