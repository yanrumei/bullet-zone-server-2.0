package org.springframework.web.servlet.mvc.method.annotation;

import org.springframework.http.server.ServerHttpResponse;

@Deprecated
public abstract interface ResponseBodyEmitterAdapter
{
  public abstract ResponseBodyEmitter adaptToEmitter(Object paramObject, ServerHttpResponse paramServerHttpResponse);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\ResponseBodyEmitterAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */