package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;

public abstract interface DestructionAwareBeanPostProcessor
  extends BeanPostProcessor
{
  public abstract void postProcessBeforeDestruction(Object paramObject, String paramString)
    throws BeansException;
  
  public abstract boolean requiresDestruction(Object paramObject);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\config\DestructionAwareBeanPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */