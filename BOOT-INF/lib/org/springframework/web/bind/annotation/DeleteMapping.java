package org.springframework.web.bind.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Target({java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping(method={RequestMethod.DELETE})
public @interface DeleteMapping
{
  @AliasFor(annotation=RequestMapping.class)
  String name() default "";
  
  @AliasFor(annotation=RequestMapping.class)
  String[] value() default {};
  
  @AliasFor(annotation=RequestMapping.class)
  String[] path() default {};
  
  @AliasFor(annotation=RequestMapping.class)
  String[] params() default {};
  
  @AliasFor(annotation=RequestMapping.class)
  String[] headers() default {};
  
  @AliasFor(annotation=RequestMapping.class)
  String[] consumes() default {};
  
  @AliasFor(annotation=RequestMapping.class)
  String[] produces() default {};
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\bind\annotation\DeleteMapping.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */