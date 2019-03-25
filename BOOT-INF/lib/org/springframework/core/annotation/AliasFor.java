package org.springframework.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD})
@Documented
public @interface AliasFor
{
  @AliasFor("attribute")
  String value() default "";
  
  @AliasFor("value")
  String attribute() default "";
  
  Class<? extends Annotation> annotation() default Annotation.class;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\annotation\AliasFor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */