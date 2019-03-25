package javax.validation.spi;

import javax.validation.Configuration;
import javax.validation.ValidatorFactory;

public abstract interface ValidationProvider<T extends Configuration<T>>
{
  public abstract T createSpecializedConfiguration(BootstrapState paramBootstrapState);
  
  public abstract Configuration<?> createGenericConfiguration(BootstrapState paramBootstrapState);
  
  public abstract ValidatorFactory buildValidatorFactory(ConfigurationState paramConfigurationState);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\spi\ValidationProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */