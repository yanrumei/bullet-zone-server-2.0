package org.springframework.context.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.core.annotation.AliasFor;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
@Documented
@Repeatable(ComponentScans.class)
public @interface ComponentScan
{
  @AliasFor("basePackages")
  String[] value() default {};
  
  @AliasFor("value")
  String[] basePackages() default {};
  
  Class<?>[] basePackageClasses() default {};
  
  Class<? extends BeanNameGenerator> nameGenerator() default BeanNameGenerator.class;
  
  Class<? extends ScopeMetadataResolver> scopeResolver() default AnnotationScopeMetadataResolver.class;
  
  ScopedProxyMode scopedProxy() default ScopedProxyMode.DEFAULT;
  
  String resourcePattern() default "**/*.class";
  
  boolean useDefaultFilters() default true;
  
  Filter[] includeFilters() default {};
  
  Filter[] excludeFilters() default {};
  
  boolean lazyInit() default false;
  
  @Retention(RetentionPolicy.RUNTIME)
  @Target({})
  public static @interface Filter
  {
    FilterType type() default FilterType.ANNOTATION;
    
    @AliasFor("classes")
    Class<?>[] value() default {};
    
    @AliasFor("value")
    Class<?>[] classes() default {};
    
    String[] pattern() default {};
  }
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\annotation\ComponentScan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */