package org.springframework.context.annotation;

import org.springframework.beans.factory.config.BeanDefinition;

public abstract interface ScopeMetadataResolver
{
  public abstract ScopeMetadata resolveScopeMetadata(BeanDefinition paramBeanDefinition);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\annotation\ScopeMetadataResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */