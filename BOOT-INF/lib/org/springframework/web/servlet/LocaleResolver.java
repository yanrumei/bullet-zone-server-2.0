package org.springframework.web.servlet;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract interface LocaleResolver
{
  public abstract Locale resolveLocale(HttpServletRequest paramHttpServletRequest);
  
  public abstract void setLocale(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, Locale paramLocale);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\LocaleResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */