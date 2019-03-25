package org.springframework.boot.autoconfigure.condition;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Conditional;

@Target({java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional({OnBeanCondition.class})
public @interface ConditionalOnMissingBean
{
  Class<?>[] value() default {};
  
  String[] type() default {};
  
  Class<?>[] ignored() default {};
  
  String[] ignoredType() default {};
  
  Class<? extends Annotation>[] annotation() default {};
  
  String[] name() default {};
  
  SearchStrategy search() default SearchStrategy.ALL;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\condition\ConditionalOnMissingBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */