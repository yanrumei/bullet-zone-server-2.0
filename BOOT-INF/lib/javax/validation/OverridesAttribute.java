package javax.validation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD})
public @interface OverridesAttribute
{
  Class<? extends Annotation> constraint();
  
  String name();
  
  int constraintIndex() default -1;
  
  @Documented
  @Target({java.lang.annotation.ElementType.METHOD})
  @Retention(RetentionPolicy.RUNTIME)
  public static @interface List
  {
    OverridesAttribute[] value();
  }
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\OverridesAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */