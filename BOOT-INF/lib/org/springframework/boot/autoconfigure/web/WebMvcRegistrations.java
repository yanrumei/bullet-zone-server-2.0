package org.springframework.boot.autoconfigure.web;

import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public abstract interface WebMvcRegistrations
{
  public abstract RequestMappingHandlerMapping getRequestMappingHandlerMapping();
  
  public abstract RequestMappingHandlerAdapter getRequestMappingHandlerAdapter();
  
  public abstract ExceptionHandlerExceptionResolver getExceptionHandlerExceptionResolver();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\web\WebMvcRegistrations.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */