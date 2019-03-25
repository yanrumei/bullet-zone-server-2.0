package org.springframework.boot.autoconfigure.web;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

public abstract interface ErrorViewResolver
{
  public abstract ModelAndView resolveErrorView(HttpServletRequest paramHttpServletRequest, HttpStatus paramHttpStatus, Map<String, Object> paramMap);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\web\ErrorViewResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */