package org.springframework.boot.web.servlet;

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
@Import({ServletComponentScanRegistrar.class})
public @interface ServletComponentScan
{
  @AliasFor("basePackages")
  String[] value() default {};
  
  @AliasFor("value")
  String[] basePackages() default {};
  
  Class<?>[] basePackageClasses() default {};
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\web\servlet\ServletComponentScan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */