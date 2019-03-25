package org.springframework.context.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.core.annotation.AliasFor;

@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean
{
  @AliasFor("name")
  String[] value() default {};
  
  @AliasFor("value")
  String[] name() default {};
  
  Autowire autowire() default Autowire.NO;
  
  String initMethod() default "";
  
  String destroyMethod() default "(inferred)";
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\annotation\Bean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */