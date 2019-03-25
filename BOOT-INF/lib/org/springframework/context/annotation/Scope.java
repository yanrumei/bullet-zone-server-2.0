package org.springframework.context.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Target({java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope
{
  @AliasFor("scopeName")
  String value() default "";
  
  @AliasFor("value")
  String scopeName() default "";
  
  ScopedProxyMode proxyMode() default ScopedProxyMode.DEFAULT;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\annotation\Scope.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */