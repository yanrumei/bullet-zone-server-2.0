package org.springframework.web.servlet;

import javax.servlet.http.HttpServletRequest;

public abstract interface RequestToViewNameTranslator
{
  public abstract String getViewName(HttpServletRequest paramHttpServletRequest)
    throws Exception;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\RequestToViewNameTranslator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */