package org.springframework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContext;

public abstract interface LocaleContextResolver
  extends LocaleResolver
{
  public abstract LocaleContext resolveLocaleContext(HttpServletRequest paramHttpServletRequest);
  
  public abstract void setLocaleContext(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, LocaleContext paramLocaleContext);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\LocaleContextResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */