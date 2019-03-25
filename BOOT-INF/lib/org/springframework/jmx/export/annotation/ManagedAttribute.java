package org.springframework.jmx.export.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ManagedAttribute
{
  String defaultValue() default "";
  
  String description() default "";
  
  int currencyTimeLimit() default -1;
  
  String persistPolicy() default "";
  
  int persistPeriod() default -1;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jmx\export\annotation\ManagedAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */