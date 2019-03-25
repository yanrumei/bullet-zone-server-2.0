package org.springframework.web.cors;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract interface CorsProcessor
{
  public abstract boolean processRequest(CorsConfiguration paramCorsConfiguration, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
    throws IOException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\cors\CorsProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */