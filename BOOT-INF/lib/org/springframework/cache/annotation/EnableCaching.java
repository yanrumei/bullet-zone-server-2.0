package org.springframework.cache.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({CachingConfigurationSelector.class})
public @interface EnableCaching
{
  boolean proxyTargetClass() default false;
  
  AdviceMode mode() default AdviceMode.PROXY;
  
  int order() default Integer.MAX_VALUE;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\annotation\EnableCaching.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */