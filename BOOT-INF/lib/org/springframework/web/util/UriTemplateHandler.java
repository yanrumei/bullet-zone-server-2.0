package org.springframework.web.util;

import java.net.URI;
import java.util.Map;

public abstract interface UriTemplateHandler
{
  public abstract URI expand(String paramString, Map<String, ?> paramMap);
  
  public abstract URI expand(String paramString, Object... paramVarArgs);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\we\\util\UriTemplateHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */