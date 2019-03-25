package org.springframework.web.servlet.mvc.method.annotation;

import java.io.IOException;
import java.lang.reflect.Type;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;

public abstract interface RequestBodyAdvice
{
  public abstract boolean supports(MethodParameter paramMethodParameter, Type paramType, Class<? extends HttpMessageConverter<?>> paramClass);
  
  public abstract Object handleEmptyBody(Object paramObject, HttpInputMessage paramHttpInputMessage, MethodParameter paramMethodParameter, Type paramType, Class<? extends HttpMessageConverter<?>> paramClass);
  
  public abstract HttpInputMessage beforeBodyRead(HttpInputMessage paramHttpInputMessage, MethodParameter paramMethodParameter, Type paramType, Class<? extends HttpMessageConverter<?>> paramClass)
    throws IOException;
  
  public abstract Object afterBodyRead(Object paramObject, HttpInputMessage paramHttpInputMessage, MethodParameter paramMethodParameter, Type paramType, Class<? extends HttpMessageConverter<?>> paramClass);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\RequestBodyAdvice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */