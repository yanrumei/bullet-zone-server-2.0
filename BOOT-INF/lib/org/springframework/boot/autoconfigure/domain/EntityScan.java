package org.springframework.boot.autoconfigure.domain;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({EntityScanPackages.Registrar.class})
public @interface EntityScan
{
  @AliasFor("basePackages")
  String[] value() default {};
  
  @AliasFor("value")
  String[] basePackages() default {};
  
  Class<?>[] basePackageClasses() default {};
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\domain\EntityScan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */