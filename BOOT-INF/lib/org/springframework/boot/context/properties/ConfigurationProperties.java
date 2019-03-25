package org.springframework.boot.context.properties;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Target({java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfigurationProperties
{
  @AliasFor("prefix")
  String value() default "";
  
  @AliasFor("value")
  String prefix() default "";
  
  boolean ignoreInvalidFields() default false;
  
  boolean ignoreNestedProperties() default false;
  
  boolean ignoreUnknownFields() default true;
  
  @Deprecated
  boolean exceptionIfInvalid() default true;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\properties\ConfigurationProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */