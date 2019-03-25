package org.springframework.scheduling.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(Schedules.class)
public @interface Scheduled
{
  String cron() default "";
  
  String zone() default "";
  
  long fixedDelay() default -1L;
  
  String fixedDelayString() default "";
  
  long fixedRate() default -1L;
  
  String fixedRateString() default "";
  
  long initialDelay() default -1L;
  
  String initialDelayString() default "";
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scheduling\annotation\Scheduled.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */