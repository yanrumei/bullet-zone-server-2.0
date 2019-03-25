package org.springframework.format;

import java.lang.annotation.Annotation;
import org.springframework.core.convert.converter.ConverterRegistry;

public abstract interface FormatterRegistry
  extends ConverterRegistry
{
  public abstract void addFormatter(Formatter<?> paramFormatter);
  
  public abstract void addFormatterForFieldType(Class<?> paramClass, Formatter<?> paramFormatter);
  
  public abstract void addFormatterForFieldType(Class<?> paramClass, Printer<?> paramPrinter, Parser<?> paramParser);
  
  public abstract void addFormatterForFieldAnnotation(AnnotationFormatterFactory<? extends Annotation> paramAnnotationFormatterFactory);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\format\FormatterRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */