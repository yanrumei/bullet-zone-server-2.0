package javax.validation;

import java.util.List;
import javax.validation.spi.ValidationProvider;

public abstract interface ValidationProviderResolver
{
  public abstract List<ValidationProvider<?>> getValidationProviders();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\ValidationProviderResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */