package org.springframework.web.method.support;

import org.springframework.core.MethodParameter;

public abstract interface AsyncHandlerMethodReturnValueHandler
  extends HandlerMethodReturnValueHandler
{
  public abstract boolean isAsyncReturnValue(Object paramObject, MethodParameter paramMethodParameter);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\method\support\AsyncHandlerMethodReturnValueHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */