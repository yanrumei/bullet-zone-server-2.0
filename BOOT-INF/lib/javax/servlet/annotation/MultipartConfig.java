package javax.servlet.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MultipartConfig
{
  String location() default "";
  
  long maxFileSize() default -1L;
  
  long maxRequestSize() default -1L;
  
  int fileSizeThreshold() default 0;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\annotation\MultipartConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */