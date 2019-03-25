package org.springframework.boot.autoconfigure.web;

import java.util.Map;
import org.springframework.web.context.request.RequestAttributes;

public abstract interface ErrorAttributes
{
  public abstract Map<String, Object> getErrorAttributes(RequestAttributes paramRequestAttributes, boolean paramBoolean);
  
  public abstract Throwable getError(RequestAttributes paramRequestAttributes);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\web\ErrorAttributes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */