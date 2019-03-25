package javax.validation.spi;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.MessageInterpolator;
import javax.validation.ParameterNameProvider;
import javax.validation.TraversableResolver;

public abstract interface ConfigurationState
{
  public abstract boolean isIgnoreXmlConfiguration();
  
  public abstract MessageInterpolator getMessageInterpolator();
  
  public abstract Set<InputStream> getMappingStreams();
  
  public abstract ConstraintValidatorFactory getConstraintValidatorFactory();
  
  public abstract TraversableResolver getTraversableResolver();
  
  public abstract ParameterNameProvider getParameterNameProvider();
  
  public abstract Map<String, String> getProperties();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\spi\ConfigurationState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */