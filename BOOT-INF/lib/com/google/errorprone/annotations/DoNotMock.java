package com.google.errorprone.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.ANNOTATION_TYPE})
public @interface DoNotMock
{
  String value() default "Create a real instance instead";
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\error_prone_annotations-2.0.18.jar!\com\google\errorprone\annotations\DoNotMock.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */