package org.springframework.web.context.request;

import org.springframework.ui.ModelMap;

public abstract interface WebRequestInterceptor
{
  public abstract void preHandle(WebRequest paramWebRequest)
    throws Exception;
  
  public abstract void postHandle(WebRequest paramWebRequest, ModelMap paramModelMap)
    throws Exception;
  
  public abstract void afterCompletion(WebRequest paramWebRequest, Exception paramException)
    throws Exception;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\context\request\WebRequestInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */