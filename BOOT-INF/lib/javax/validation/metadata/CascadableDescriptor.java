package javax.validation.metadata;

import java.util.Set;

public abstract interface CascadableDescriptor
{
  public abstract boolean isCascaded();
  
  public abstract Set<GroupConversionDescriptor> getGroupConversions();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\metadata\CascadableDescriptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */