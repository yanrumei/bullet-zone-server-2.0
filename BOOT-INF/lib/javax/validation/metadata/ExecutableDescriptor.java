package javax.validation.metadata;

import java.util.List;
import java.util.Set;

public abstract interface ExecutableDescriptor
  extends ElementDescriptor
{
  public abstract String getName();
  
  public abstract List<ParameterDescriptor> getParameterDescriptors();
  
  public abstract CrossParameterDescriptor getCrossParameterDescriptor();
  
  public abstract ReturnValueDescriptor getReturnValueDescriptor();
  
  public abstract boolean hasConstrainedParameters();
  
  public abstract boolean hasConstrainedReturnValue();
  
  public abstract boolean hasConstraints();
  
  public abstract Set<ConstraintDescriptor<?>> getConstraintDescriptors();
  
  public abstract ElementDescriptor.ConstraintFinder findConstraints();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\metadata\ExecutableDescriptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */