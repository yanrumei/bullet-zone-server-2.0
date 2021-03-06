package org.springframework.web.accept;

import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.NativeWebRequest;

public abstract interface ContentNegotiationStrategy
{
  public abstract List<MediaType> resolveMediaTypes(NativeWebRequest paramNativeWebRequest)
    throws HttpMediaTypeNotAcceptableException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\accept\ContentNegotiationStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */