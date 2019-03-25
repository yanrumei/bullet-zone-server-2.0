package org.springframework.web.bind.support;

import org.springframework.web.context.request.WebRequest;

public abstract interface SessionAttributeStore
{
  public abstract void storeAttribute(WebRequest paramWebRequest, String paramString, Object paramObject);
  
  public abstract Object retrieveAttribute(WebRequest paramWebRequest, String paramString);
  
  public abstract void cleanupAttribute(WebRequest paramWebRequest, String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\bind\support\SessionAttributeStore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */