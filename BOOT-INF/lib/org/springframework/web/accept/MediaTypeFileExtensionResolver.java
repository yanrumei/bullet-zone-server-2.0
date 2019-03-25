package org.springframework.web.accept;

import java.util.List;
import org.springframework.http.MediaType;

public abstract interface MediaTypeFileExtensionResolver
{
  public abstract List<String> resolveFileExtensions(MediaType paramMediaType);
  
  public abstract List<String> getAllFileExtensions();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\accept\MediaTypeFileExtensionResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */