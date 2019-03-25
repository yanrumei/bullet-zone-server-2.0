package org.springframework.web.servlet.mvc.method.annotation;

import org.springframework.web.context.request.async.DeferredResult;

@Deprecated
public abstract interface DeferredResultAdapter
{
  public abstract DeferredResult<?> adaptToDeferredResult(Object paramObject);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\DeferredResultAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */