package org.springframework.web.bind.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface RequestMapping
{
  String name() default "";
  
  @AliasFor("path")
  String[] value() default {};
  
  @AliasFor("value")
  String[] path() default {};
  
  RequestMethod[] method() default {};
  
  String[] params() default {};
  
  String[] headers() default {};
  
  String[] consumes() default {};
  
  String[] produces() default {};
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\bind\annotation\RequestMapping.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */