package org.springframework.http.client;

import java.io.IOException;
import org.springframework.http.HttpRequest;
import org.springframework.util.concurrent.ListenableFuture;

public abstract interface AsyncClientHttpRequestInterceptor
{
  public abstract ListenableFuture<ClientHttpResponse> intercept(HttpRequest paramHttpRequest, byte[] paramArrayOfByte, AsyncClientHttpRequestExecution paramAsyncClientHttpRequestExecution)
    throws IOException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\client\AsyncClientHttpRequestInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */