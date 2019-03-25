package javax.validation.bootstrap;

import javax.validation.Configuration;
import javax.validation.ValidationProviderResolver;

public abstract interface ProviderSpecificBootstrap<T extends Configuration<T>>
{
  public abstract ProviderSpecificBootstrap<T> providerResolver(ValidationProviderResolver paramValidationProviderResolver);
  
  public abstract T configure();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\bootstrap\ProviderSpecificBootstrap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */