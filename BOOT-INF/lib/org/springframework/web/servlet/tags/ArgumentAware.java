package org.springframework.web.servlet.tags;

import javax.servlet.jsp.JspTagException;

public abstract interface ArgumentAware
{
  public abstract void addArgument(Object paramObject)
    throws JspTagException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\ArgumentAware.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */