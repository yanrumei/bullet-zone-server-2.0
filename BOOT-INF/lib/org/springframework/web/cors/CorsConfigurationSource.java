package org.springframework.web.cors;

import javax.servlet.http.HttpServletRequest;

public abstract interface CorsConfigurationSource
{
  public abstract CorsConfiguration getCorsConfiguration(HttpServletRequest paramHttpServletRequest);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\cors\CorsConfigurationSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */