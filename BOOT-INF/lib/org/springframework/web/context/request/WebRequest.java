package org.springframework.web.context.request;

import java.security.Principal;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public abstract interface WebRequest
  extends RequestAttributes
{
  public abstract String getHeader(String paramString);
  
  public abstract String[] getHeaderValues(String paramString);
  
  public abstract Iterator<String> getHeaderNames();
  
  public abstract String getParameter(String paramString);
  
  public abstract String[] getParameterValues(String paramString);
  
  public abstract Iterator<String> getParameterNames();
  
  public abstract Map<String, String[]> getParameterMap();
  
  public abstract Locale getLocale();
  
  public abstract String getContextPath();
  
  public abstract String getRemoteUser();
  
  public abstract Principal getUserPrincipal();
  
  public abstract boolean isUserInRole(String paramString);
  
  public abstract boolean isSecure();
  
  public abstract boolean checkNotModified(long paramLong);
  
  public abstract boolean checkNotModified(String paramString);
  
  public abstract boolean checkNotModified(String paramString, long paramLong);
  
  public abstract String getDescription(boolean paramBoolean);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\context\request\WebRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */