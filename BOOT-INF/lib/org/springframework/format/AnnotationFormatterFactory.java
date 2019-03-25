package org.springframework.format;

import java.lang.annotation.Annotation;
import java.util.Set;

public abstract interface AnnotationFormatterFactory<A extends Annotation>
{
  public abstract Set<Class<?>> getFieldTypes();
  
  public abstract Printer<?> getPrinter(A paramA, Class<?> paramClass);
  
  public abstract Parser<?> getParser(A paramA, Class<?> paramClass);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\format\AnnotationFormatterFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */