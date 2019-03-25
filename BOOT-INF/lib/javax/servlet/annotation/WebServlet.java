package javax.servlet.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebServlet
{
  String name() default "";
  
  String[] value() default {};
  
  String[] urlPatterns() default {};
  
  int loadOnStartup() default -1;
  
  WebInitParam[] initParams() default {};
  
  boolean asyncSupported() default false;
  
  String smallIcon() default "";
  
  String largeIcon() default "";
  
  String description() default "";
  
  String displayName() default "";
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\annotation\WebServlet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */