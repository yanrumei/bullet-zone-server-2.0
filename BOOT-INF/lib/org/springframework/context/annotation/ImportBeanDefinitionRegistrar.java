package org.springframework.context.annotation;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.type.AnnotationMetadata;

public abstract interface ImportBeanDefinitionRegistrar
{
  public abstract void registerBeanDefinitions(AnnotationMetadata paramAnnotationMetadata, BeanDefinitionRegistry paramBeanDefinitionRegistry);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\annotation\ImportBeanDefinitionRegistrar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */