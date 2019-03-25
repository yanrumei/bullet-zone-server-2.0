package org.springframework.jmx.export.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ManagedResource
{
  @AliasFor("objectName")
  String value() default "";
  
  @AliasFor("value")
  String objectName() default "";
  
  String description() default "";
  
  int currencyTimeLimit() default -1;
  
  boolean log() default false;
  
  String logFile() default "";
  
  String persistPolicy() default "";
  
  int persistPeriod() default -1;
  
  String persistName() default "";
  
  String persistLocation() default "";
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jmx\export\annotation\ManagedResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */