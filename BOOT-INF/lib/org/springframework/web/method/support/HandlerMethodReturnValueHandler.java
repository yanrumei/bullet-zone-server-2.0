package org.springframework.web.method.support;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;

public abstract interface HandlerMethodReturnValueHandler
{
  public abstract boolean supportsReturnType(MethodParameter paramMethodParameter);
  
  public abstract void handleReturnValue(Object paramObject, MethodParameter paramMethodParameter, ModelAndViewContainer paramModelAndViewContainer, NativeWebRequest paramNativeWebRequest)
    throws Exception;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\method\support\HandlerMethodReturnValueHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */