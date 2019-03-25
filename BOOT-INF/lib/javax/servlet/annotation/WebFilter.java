package javax.servlet.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.servlet.DispatcherType;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebFilter
{
  String description() default "";
  
  String displayName() default "";
  
  WebInitParam[] initParams() default {};
  
  String filterName() default "";
  
  String smallIcon() default "";
  
  String largeIcon() default "";
  
  String[] servletNames() default {};
  
  String[] value() default {};
  
  String[] urlPatterns() default {};
  
  DispatcherType[] dispatcherTypes() default {DispatcherType.REQUEST};
  
  boolean asyncSupported() default false;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\annotation\WebFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */