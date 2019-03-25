package javax.servlet.http;

import java.util.Enumeration;

/**
 * @deprecated
 */
public abstract interface HttpSessionContext
{
  /**
   * @deprecated
   */
  public abstract HttpSession getSession(String paramString);
  
  /**
   * @deprecated
   */
  public abstract Enumeration<String> getIds();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\http\HttpSessionContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */