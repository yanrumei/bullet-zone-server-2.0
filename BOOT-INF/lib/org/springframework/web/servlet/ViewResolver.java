package org.springframework.web.servlet;

import java.util.Locale;

public abstract interface ViewResolver
{
  public abstract View resolveViewName(String paramString, Locale paramLocale)
    throws Exception;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\ViewResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */