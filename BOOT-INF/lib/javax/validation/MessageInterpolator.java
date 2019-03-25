package javax.validation;

import java.util.Locale;
import javax.validation.metadata.ConstraintDescriptor;

public abstract interface MessageInterpolator
{
  public abstract String interpolate(String paramString, Context paramContext);
  
  public abstract String interpolate(String paramString, Context paramContext, Locale paramLocale);
  
  public static abstract interface Context
  {
    public abstract ConstraintDescriptor<?> getConstraintDescriptor();
    
    public abstract Object getValidatedValue();
    
    public abstract <T> T unwrap(Class<T> paramClass);
  }
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\MessageInterpolator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */