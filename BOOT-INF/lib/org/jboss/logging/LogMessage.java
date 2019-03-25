package org.jboss.logging;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({java.lang.annotation.ElementType.METHOD})
@Documented
@Deprecated
public @interface LogMessage
{
  Logger.Level level() default Logger.Level.INFO;
  
  Class<?> loggingClass() default Void.class;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jboss-logging-3.3.1.Final.jar!\org\jboss\logging\LogMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */