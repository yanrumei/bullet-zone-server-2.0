package org.springframework.web.client;

import java.io.IOException;
import org.springframework.http.client.AsyncClientHttpRequest;

public abstract interface AsyncRequestCallback
{
  public abstract void doWithRequest(AsyncClientHttpRequest paramAsyncClientHttpRequest)
    throws IOException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\client\AsyncRequestCallback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */