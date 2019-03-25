package org.springframework.boot.autoconfigure;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AliasFor;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters={@org.springframework.context.annotation.ComponentScan.Filter(type=org.springframework.context.annotation.FilterType.CUSTOM, classes={org.springframework.boot.context.TypeExcludeFilter.class}), @org.springframework.context.annotation.ComponentScan.Filter(type=org.springframework.context.annotation.FilterType.CUSTOM, classes={AutoConfigurationExcludeFilter.class})})
public @interface SpringBootApplication
{
  @AliasFor(annotation=EnableAutoConfiguration.class, attribute="exclude")
  Class<?>[] exclude() default {};
  
  @AliasFor(annotation=EnableAutoConfiguration.class, attribute="excludeName")
  String[] excludeName() default {};
  
  @AliasFor(annotation=ComponentScan.class, attribute="basePackages")
  String[] scanBasePackages() default {};
  
  @AliasFor(annotation=ComponentScan.class, attribute="basePackageClasses")
  Class<?>[] scanBasePackageClasses() default {};
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\SpringBootApplication.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */