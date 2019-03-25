package org.springframework.context.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.core.annotation.AliasFor;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
@Documented
public @interface ImportResource
{
  @AliasFor("locations")
  String[] value() default {};
  
  @AliasFor("value")
  String[] locations() default {};
  
  Class<? extends BeanDefinitionReader> reader() default BeanDefinitionReader.class;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\annotation\ImportResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */