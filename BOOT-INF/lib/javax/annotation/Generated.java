package javax.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.LOCAL_VARIABLE, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.PACKAGE, java.lang.annotation.ElementType.PARAMETER, java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface Generated
{
  String[] value();
  
  String date() default "";
  
  String comments() default "";
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-annotations-api-8.5.27.jar!\javax\annotation\Generated.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */