package javax.validation;

import java.io.InputStream;

public abstract interface Configuration<T extends Configuration<T>>
{
  public abstract T ignoreXmlConfiguration();
  
  public abstract T messageInterpolator(MessageInterpolator paramMessageInterpolator);
  
  public abstract T traversableResolver(TraversableResolver paramTraversableResolver);
  
  public abstract T constraintValidatorFactory(ConstraintValidatorFactory paramConstraintValidatorFactory);
  
  public abstract T parameterNameProvider(ParameterNameProvider paramParameterNameProvider);
  
  public abstract T addMapping(InputStream paramInputStream);
  
  public abstract T addProperty(String paramString1, String paramString2);
  
  public abstract MessageInterpolator getDefaultMessageInterpolator();
  
  public abstract TraversableResolver getDefaultTraversableResolver();
  
  public abstract ConstraintValidatorFactory getDefaultConstraintValidatorFactory();
  
  public abstract ParameterNameProvider getDefaultParameterNameProvider();
  
  public abstract BootstrapConfiguration getBootstrapConfiguration();
  
  public abstract ValidatorFactory buildValidatorFactory();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\Configuration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */