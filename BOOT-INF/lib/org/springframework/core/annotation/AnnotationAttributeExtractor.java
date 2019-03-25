package org.springframework.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

abstract interface AnnotationAttributeExtractor<S>
{
  public abstract Class<? extends Annotation> getAnnotationType();
  
  public abstract Object getAnnotatedElement();
  
  public abstract S getSource();
  
  public abstract Object getAttributeValue(Method paramMethod);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\annotation\AnnotationAttributeExtractor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */