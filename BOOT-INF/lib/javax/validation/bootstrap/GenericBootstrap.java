package javax.validation.bootstrap;

import javax.validation.Configuration;
import javax.validation.ValidationProviderResolver;

public abstract interface GenericBootstrap
{
  public abstract GenericBootstrap providerResolver(ValidationProviderResolver paramValidationProviderResolver);
  
  public abstract Configuration<?> configure();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\bootstrap\GenericBootstrap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */