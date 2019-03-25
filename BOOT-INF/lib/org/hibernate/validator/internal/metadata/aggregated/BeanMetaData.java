package org.hibernate.validator.internal.metadata.aggregated;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintDeclarationException;
import javax.validation.metadata.BeanDescriptor;
import org.hibernate.validator.internal.engine.groups.Sequence;
import org.hibernate.validator.internal.metadata.core.MetaConstraint;
import org.hibernate.validator.internal.metadata.facets.Validatable;
import org.hibernate.validator.internal.metadata.raw.ExecutableElement;

public abstract interface BeanMetaData<T>
  extends Validatable
{
  public abstract Class<T> getBeanClass();
  
  public abstract boolean hasConstraints();
  
  public abstract BeanDescriptor getBeanDescriptor();
  
  public abstract PropertyMetaData getMetaDataFor(String paramString);
  
  public abstract List<Class<?>> getDefaultGroupSequence(T paramT);
  
  public abstract Iterator<Sequence> getDefaultValidationSequence(T paramT);
  
  public abstract boolean defaultGroupSequenceIsRedefined();
  
  public abstract Set<MetaConstraint<?>> getMetaConstraints();
  
  public abstract Set<MetaConstraint<?>> getDirectMetaConstraints();
  
  public abstract ExecutableMetaData getMetaDataFor(ExecutableElement paramExecutableElement)
    throws ConstraintDeclarationException;
  
  public abstract List<Class<? super T>> getClassHierarchy();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\aggregated\BeanMetaData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */