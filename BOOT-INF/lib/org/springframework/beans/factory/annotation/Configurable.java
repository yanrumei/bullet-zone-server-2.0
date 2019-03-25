package org.springframework.beans.factory.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Configurable
{
  String value() default "";
  
  Autowire autowire() default Autowire.NO;
  
  boolean dependencyCheck() default false;
  
  boolean preConstruction() default false;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\annotation\Configurable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */