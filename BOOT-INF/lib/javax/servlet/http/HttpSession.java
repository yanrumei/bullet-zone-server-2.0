package javax.servlet.http;

import java.util.Enumeration;
import javax.servlet.ServletContext;

public abstract interface HttpSession
{
  public abstract long getCreationTime();
  
  public abstract String getId();
  
  public abstract long getLastAccessedTime();
  
  public abstract ServletContext getServletContext();
  
  public abstract void setMaxInactiveInterval(int paramInt);
  
  public abstract int getMaxInactiveInterval();
  
  /**
   * @deprecated
   */
  public abstract HttpSessionContext getSessionContext();
  
  public abstract Object getAttribute(String paramString);
  
  /**
   * @deprecated
   */
  public abstract Object getValue(String paramString);
  
  public abstract Enumeration<String> getAttributeNames();
  
  /**
   * @deprecated
   */
  public abstract String[] getValueNames();
  
  public abstract void setAttribute(String paramString, Object paramObject);
  
  /**
   * @deprecated
   */
  public abstract void putValue(String paramString, Object paramObject);
  
  public abstract void removeAttribute(String paramString);
  
  /**
   * @deprecated
   */
  public abstract void removeValue(String paramString);
  
  public abstract void invalidate();
  
  public abstract boolean isNew();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\http\HttpSession.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */