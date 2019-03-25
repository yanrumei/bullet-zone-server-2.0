package org.springframework.web.multipart;

import javax.servlet.http.HttpServletRequest;

public abstract interface MultipartResolver
{
  public abstract boolean isMultipart(HttpServletRequest paramHttpServletRequest);
  
  public abstract MultipartHttpServletRequest resolveMultipart(HttpServletRequest paramHttpServletRequest)
    throws MultipartException;
  
  public abstract void cleanupMultipart(MultipartHttpServletRequest paramMultipartHttpServletRequest);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\multipart\MultipartResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */