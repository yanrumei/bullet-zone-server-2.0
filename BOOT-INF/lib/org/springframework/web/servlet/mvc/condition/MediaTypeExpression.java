package org.springframework.web.servlet.mvc.condition;

import org.springframework.http.MediaType;

public abstract interface MediaTypeExpression
{
  public abstract MediaType getMediaType();
  
  public abstract boolean isNegated();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\condition\MediaTypeExpression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */