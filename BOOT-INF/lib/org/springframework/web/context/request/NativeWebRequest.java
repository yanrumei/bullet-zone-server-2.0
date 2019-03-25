package org.springframework.web.context.request;

public abstract interface NativeWebRequest
  extends WebRequest
{
  public abstract Object getNativeRequest();
  
  public abstract Object getNativeResponse();
  
  public abstract <T> T getNativeRequest(Class<T> paramClass);
  
  public abstract <T> T getNativeResponse(Class<T> paramClass);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\context\request\NativeWebRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */