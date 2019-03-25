package javax.validation.executable;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateOnExecution
{
  ExecutableType[] type() default {ExecutableType.IMPLICIT};
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\executable\ValidateOnExecution.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */