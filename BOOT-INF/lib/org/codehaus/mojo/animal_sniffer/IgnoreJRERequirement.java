package org.codehaus.mojo.animal_sniffer;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Documented
@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.TYPE})
public @interface IgnoreJRERequirement {}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\animal-sniffer-annotations-1.14.jar!\org\codehaus\mojo\animal_sniffer\IgnoreJRERequirement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */