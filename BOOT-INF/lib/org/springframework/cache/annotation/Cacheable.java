package org.springframework.cache.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Cacheable
{
  @AliasFor("cacheNames")
  String[] value() default {};
  
  @AliasFor("value")
  String[] cacheNames() default {};
  
  String key() default "";
  
  String keyGenerator() default "";
  
  String cacheManager() default "";
  
  String cacheResolver() default "";
  
  String condition() default "";
  
  String unless() default "";
  
  boolean sync() default false;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\annotation\Cacheable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */