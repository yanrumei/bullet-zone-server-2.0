package com.google.j2objc.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.SOURCE)
public @interface AutoreleasePool {}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\j2objc-annotations-1.1.jar!\com\google\j2objc\annotations\AutoreleasePool.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */