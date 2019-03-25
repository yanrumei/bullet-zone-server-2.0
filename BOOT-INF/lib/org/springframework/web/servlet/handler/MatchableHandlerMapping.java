package org.springframework.web.servlet.handler;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.HandlerMapping;

public abstract interface MatchableHandlerMapping
  extends HandlerMapping
{
  public abstract RequestMatchResult match(HttpServletRequest paramHttpServletRequest, String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\handler\MatchableHandlerMapping.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */