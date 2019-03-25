package org.springframework.web.bind.support;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;

public abstract interface WebDataBinderFactory
{
  public abstract WebDataBinder createBinder(NativeWebRequest paramNativeWebRequest, Object paramObject, String paramString)
    throws Exception;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\bind\support\WebDataBinderFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */