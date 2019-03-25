package org.springframework.http.client;

import java.io.IOException;
import org.springframework.http.HttpRequest;
import org.springframework.util.concurrent.ListenableFuture;

public abstract interface AsyncClientHttpRequestExecution
{
  public abstract ListenableFuture<ClientHttpResponse> executeAsync(HttpRequest paramHttpRequest, byte[] paramArrayOfByte)
    throws IOException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\client\AsyncClientHttpRequestExecution.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */