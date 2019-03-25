package javax.validation.constraintvalidation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SupportedValidationTarget
{
  ValidationTarget[] value();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\constraintvalidation\SupportedValidationTarget.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */