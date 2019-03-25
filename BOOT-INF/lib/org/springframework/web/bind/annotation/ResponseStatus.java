package org.springframework.web.bind.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;
import org.springframework.http.HttpStatus;

@Target({java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseStatus
{
  @AliasFor("code")
  HttpStatus value() default HttpStatus.INTERNAL_SERVER_ERROR;
  
  @AliasFor("value")
  HttpStatus code() default HttpStatus.INTERNAL_SERVER_ERROR;
  
  String reason() default "";
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\bind\annotation\ResponseStatus.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */