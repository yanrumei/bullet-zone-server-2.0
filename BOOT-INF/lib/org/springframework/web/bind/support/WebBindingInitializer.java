package org.springframework.web.bind.support;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.WebRequest;

public abstract interface WebBindingInitializer
{
  public abstract void initBinder(WebDataBinder paramWebDataBinder, WebRequest paramWebRequest);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\bind\support\WebBindingInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */