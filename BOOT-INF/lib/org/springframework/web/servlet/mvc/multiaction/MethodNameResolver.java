package org.springframework.web.servlet.mvc.multiaction;

import javax.servlet.http.HttpServletRequest;

@Deprecated
public abstract interface MethodNameResolver
{
  public abstract String getHandlerMethodName(HttpServletRequest paramHttpServletRequest)
    throws NoSuchRequestHandlingMethodException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\multiaction\MethodNameResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */