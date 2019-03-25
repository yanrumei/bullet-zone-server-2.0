package org.hibernate.validator.internal.metadata.provider;

import java.util.List;
import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptions;
import org.hibernate.validator.internal.metadata.raw.BeanConfiguration;

public abstract interface MetaDataProvider
{
  public abstract AnnotationProcessingOptions getAnnotationProcessingOptions();
  
  public abstract <T> List<BeanConfiguration<? super T>> getBeanConfigurationForHierarchy(Class<T> paramClass);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\provider\MetaDataProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */