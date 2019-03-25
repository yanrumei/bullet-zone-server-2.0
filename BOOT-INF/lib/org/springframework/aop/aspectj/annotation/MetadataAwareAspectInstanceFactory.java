package org.springframework.aop.aspectj.annotation;

import org.springframework.aop.aspectj.AspectInstanceFactory;

public abstract interface MetadataAwareAspectInstanceFactory
  extends AspectInstanceFactory
{
  public abstract AspectMetadata getAspectMetadata();
  
  public abstract Object getAspectCreationMutex();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\aspectj\annotation\MetadataAwareAspectInstanceFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */