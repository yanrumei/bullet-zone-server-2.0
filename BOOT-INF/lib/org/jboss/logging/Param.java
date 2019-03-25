package org.jboss.logging;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.PARAMETER})
@Retention(RetentionPolicy.CLASS)
@Documented
@Deprecated
public @interface Param
{
  Class<?> value() default Object.class;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jboss-logging-3.3.1.Final.jar!\org\jboss\logging\Param.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */