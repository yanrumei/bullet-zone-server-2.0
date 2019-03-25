package org.springframework.jmx.export.metadata;

import java.lang.reflect.Method;

public abstract interface JmxAttributeSource
{
  public abstract ManagedResource getManagedResource(Class<?> paramClass)
    throws InvalidMetadataException;
  
  public abstract ManagedAttribute getManagedAttribute(Method paramMethod)
    throws InvalidMetadataException;
  
  public abstract ManagedMetric getManagedMetric(Method paramMethod)
    throws InvalidMetadataException;
  
  public abstract ManagedOperation getManagedOperation(Method paramMethod)
    throws InvalidMetadataException;
  
  public abstract ManagedOperationParameter[] getManagedOperationParameters(Method paramMethod)
    throws InvalidMetadataException;
  
  public abstract ManagedNotification[] getManagedNotifications(Class<?> paramClass)
    throws InvalidMetadataException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jmx\export\metadata\JmxAttributeSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */