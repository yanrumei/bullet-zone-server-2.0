package org.springframework.web.servlet.mvc.method.annotation;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

public abstract interface ResponseBodyAdvice<T>
{
  public abstract boolean supports(MethodParameter paramMethodParameter, Class<? extends HttpMessageConverter<?>> paramClass);
  
  public abstract T beforeBodyWrite(T paramT, MethodParameter paramMethodParameter, MediaType paramMediaType, Class<? extends HttpMessageConverter<?>> paramClass, ServerHttpRequest paramServerHttpRequest, ServerHttpResponse paramServerHttpResponse);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\ResponseBodyAdvice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */