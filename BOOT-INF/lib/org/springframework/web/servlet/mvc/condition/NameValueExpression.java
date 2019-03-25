package org.springframework.web.servlet.mvc.condition;

public abstract interface NameValueExpression<T>
{
  public abstract String getName();
  
  public abstract T getValue();
  
  public abstract boolean isNegated();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\condition\NameValueExpression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */