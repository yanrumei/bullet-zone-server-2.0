package org.hibernate.validator.internal.metadata.facets;

import java.lang.annotation.ElementType;
import java.lang.reflect.Type;
import java.util.Set;
import javax.validation.ElementKind;
import javax.validation.metadata.GroupConversionDescriptor;
import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
import org.hibernate.validator.internal.metadata.core.MetaConstraint;

public abstract interface Cascadable
{
  public abstract Class<?> convertGroup(Class<?> paramClass);
  
  public abstract Set<GroupConversionDescriptor> getGroupConversionDescriptors();
  
  public abstract ElementType getElementType();
  
  public abstract String getName();
  
  public abstract ElementKind getKind();
  
  public abstract Set<MetaConstraint<?>> getTypeArgumentsConstraints();
  
  public abstract UnwrapMode unwrapMode();
  
  public abstract Type getCascadableType();
  
  public abstract Object getValue(Object paramObject);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\facets\Cascadable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */