package org.springframework.context.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.io.support.PropertySourceFactory;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(PropertySources.class)
public @interface PropertySource
{
  String name() default "";
  
  String[] value();
  
  boolean ignoreResourceNotFound() default false;
  
  String encoding() default "";
  
  Class<? extends PropertySourceFactory> factory() default PropertySourceFactory.class;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\annotation\PropertySource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */