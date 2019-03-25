package org.hibernate.validator.internal.metadata.aggregated;

import java.lang.reflect.Type;
import java.util.List;
import javax.validation.ElementKind;
import javax.validation.metadata.ElementDescriptor;
import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
import org.hibernate.validator.internal.metadata.core.MetaConstraint;

public abstract interface ConstraintMetaData
  extends Iterable<MetaConstraint<?>>
{
  public abstract String getName();
  
  public abstract Type getType();
  
  public abstract ElementKind getKind();
  
  public abstract boolean isCascading();
  
  public abstract boolean isConstrained();
  
  public abstract ElementDescriptor asDescriptor(boolean paramBoolean, List<Class<?>> paramList);
  
  public abstract UnwrapMode unwrapMode();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\aggregated\ConstraintMetaData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */