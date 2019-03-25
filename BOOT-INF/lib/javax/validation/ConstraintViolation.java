package javax.validation;

import javax.validation.metadata.ConstraintDescriptor;

public abstract interface ConstraintViolation<T>
{
  public abstract String getMessage();
  
  public abstract String getMessageTemplate();
  
  public abstract T getRootBean();
  
  public abstract Class<T> getRootBeanClass();
  
  public abstract Object getLeafBean();
  
  public abstract Object[] getExecutableParameters();
  
  public abstract Object getExecutableReturnValue();
  
  public abstract Path getPropertyPath();
  
  public abstract Object getInvalidValue();
  
  public abstract ConstraintDescriptor<?> getConstraintDescriptor();
  
  public abstract <U> U unwrap(Class<U> paramClass);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\ConstraintViolation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */