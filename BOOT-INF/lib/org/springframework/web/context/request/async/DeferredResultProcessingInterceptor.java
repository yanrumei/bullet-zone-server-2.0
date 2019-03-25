package org.springframework.web.context.request.async;

import org.springframework.web.context.request.NativeWebRequest;

public abstract interface DeferredResultProcessingInterceptor
{
  public abstract <T> void beforeConcurrentHandling(NativeWebRequest paramNativeWebRequest, DeferredResult<T> paramDeferredResult)
    throws Exception;
  
  public abstract <T> void preProcess(NativeWebRequest paramNativeWebRequest, DeferredResult<T> paramDeferredResult)
    throws Exception;
  
  public abstract <T> void postProcess(NativeWebRequest paramNativeWebRequest, DeferredResult<T> paramDeferredResult, Object paramObject)
    throws Exception;
  
  public abstract <T> boolean handleTimeout(NativeWebRequest paramNativeWebRequest, DeferredResult<T> paramDeferredResult)
    throws Exception;
  
  public abstract <T> void afterCompletion(NativeWebRequest paramNativeWebRequest, DeferredResult<T> paramDeferredResult)
    throws Exception;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\context\request\async\DeferredResultProcessingInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */