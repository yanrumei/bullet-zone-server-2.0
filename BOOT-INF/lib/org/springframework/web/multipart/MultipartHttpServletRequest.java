package org.springframework.web.multipart;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

public abstract interface MultipartHttpServletRequest
  extends HttpServletRequest, MultipartRequest
{
  public abstract HttpMethod getRequestMethod();
  
  public abstract HttpHeaders getRequestHeaders();
  
  public abstract HttpHeaders getMultipartHeaders(String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\multipart\MultipartHttpServletRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */