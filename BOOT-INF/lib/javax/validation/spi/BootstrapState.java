package javax.validation.spi;

import javax.validation.ValidationProviderResolver;

public abstract interface BootstrapState
{
  public abstract ValidationProviderResolver getValidationProviderResolver();
  
  public abstract ValidationProviderResolver getDefaultValidationProviderResolver();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\spi\BootstrapState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */