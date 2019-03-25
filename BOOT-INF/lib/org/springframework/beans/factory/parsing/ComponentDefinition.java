package org.springframework.beans.factory.parsing;

import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanReference;

public abstract interface ComponentDefinition
  extends BeanMetadataElement
{
  public abstract String getName();
  
  public abstract String getDescription();
  
  public abstract BeanDefinition[] getBeanDefinitions();
  
  public abstract BeanDefinition[] getInnerBeanDefinitions();
  
  public abstract BeanReference[] getBeanReferences();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\parsing\ComponentDefinition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */