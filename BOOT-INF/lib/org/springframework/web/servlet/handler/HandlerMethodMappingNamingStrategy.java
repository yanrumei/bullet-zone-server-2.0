package org.springframework.web.servlet.handler;

import org.springframework.web.method.HandlerMethod;

public abstract interface HandlerMethodMappingNamingStrategy<T>
{
  public abstract String getName(HandlerMethod paramHandlerMethod, T paramT);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\handler\HandlerMethodMappingNamingStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */