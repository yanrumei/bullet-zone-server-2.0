package org.springframework.web.context.request;

public abstract interface AsyncWebRequestInterceptor
  extends WebRequestInterceptor
{
  public abstract void afterConcurrentHandlingStarted(WebRequest paramWebRequest);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\context\request\AsyncWebRequestInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */