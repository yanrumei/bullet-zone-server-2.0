package org.springframework.web.servlet.mvc.condition;

import javax.servlet.http.HttpServletRequest;

public abstract interface RequestCondition<T>
{
  public abstract T combine(T paramT);
  
  public abstract T getMatchingCondition(HttpServletRequest paramHttpServletRequest);
  
  public abstract int compareTo(T paramT, HttpServletRequest paramHttpServletRequest);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\condition\RequestCondition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */