package org.springframework.web.method.support;

import java.util.Map;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.util.UriComponentsBuilder;

public abstract interface UriComponentsContributor
{
  public abstract boolean supportsParameter(MethodParameter paramMethodParameter);
  
  public abstract void contributeMethodArgument(MethodParameter paramMethodParameter, Object paramObject, UriComponentsBuilder paramUriComponentsBuilder, Map<String, Object> paramMap, ConversionService paramConversionService);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\method\support\UriComponentsContributor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */