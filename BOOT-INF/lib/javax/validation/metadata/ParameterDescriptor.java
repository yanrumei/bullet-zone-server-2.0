package javax.validation.metadata;

public abstract interface ParameterDescriptor
  extends ElementDescriptor, CascadableDescriptor
{
  public abstract int getIndex();
  
  public abstract String getName();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\metadata\ParameterDescriptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */