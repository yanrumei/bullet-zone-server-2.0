package org.springframework.context.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.jmx.support.RegistrationPolicy;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({MBeanExportConfiguration.class})
public @interface EnableMBeanExport
{
  String defaultDomain() default "";
  
  String server() default "";
  
  RegistrationPolicy registration() default RegistrationPolicy.FAIL_ON_EXISTING;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\annotation\EnableMBeanExport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */