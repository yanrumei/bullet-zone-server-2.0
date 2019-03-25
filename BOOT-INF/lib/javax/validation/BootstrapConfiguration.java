package javax.validation;

import java.util.Map;
import java.util.Set;
import javax.validation.executable.ExecutableType;

public abstract interface BootstrapConfiguration
{
  public abstract String getDefaultProviderClassName();
  
  public abstract String getConstraintValidatorFactoryClassName();
  
  public abstract String getMessageInterpolatorClassName();
  
  public abstract String getTraversableResolverClassName();
  
  public abstract String getParameterNameProviderClassName();
  
  public abstract Set<String> getConstraintMappingResourcePaths();
  
  public abstract boolean isExecutableValidationEnabled();
  
  public abstract Set<ExecutableType> getDefaultValidatedExecutableTypes();
  
  public abstract Map<String, String> getProperties();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\BootstrapConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */