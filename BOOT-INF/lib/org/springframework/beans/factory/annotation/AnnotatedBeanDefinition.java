package org.springframework.beans.factory.annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;

public abstract interface AnnotatedBeanDefinition
  extends BeanDefinition
{
  public abstract AnnotationMetadata getMetadata();
  
  public abstract MethodMetadata getFactoryMethodMetadata();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\annotation\AnnotatedBeanDefinition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */