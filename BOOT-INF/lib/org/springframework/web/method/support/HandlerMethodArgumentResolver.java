package org.springframework.web.method.support;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;

public abstract interface HandlerMethodArgumentResolver
{
  public abstract boolean supportsParameter(MethodParameter paramMethodParameter);
  
  public abstract Object resolveArgument(MethodParameter paramMethodParameter, ModelAndViewContainer paramModelAndViewContainer, NativeWebRequest paramNativeWebRequest, WebDataBinderFactory paramWebDataBinderFactory)
    throws Exception;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\method\support\HandlerMethodArgumentResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */