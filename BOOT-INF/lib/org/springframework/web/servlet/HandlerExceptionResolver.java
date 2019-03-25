package org.springframework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract interface HandlerExceptionResolver
{
  public abstract ModelAndView resolveException(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, Object paramObject, Exception paramException);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\HandlerExceptionResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */