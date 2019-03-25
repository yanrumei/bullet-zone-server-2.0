package org.springframework.boot.autoconfigure;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
public @interface AutoConfigureBefore
{
  Class<?>[] value() default {};
  
  String[] name() default {};
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\AutoConfigureBefore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */