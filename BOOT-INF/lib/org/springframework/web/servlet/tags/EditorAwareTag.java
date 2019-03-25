package org.springframework.web.servlet.tags;

import java.beans.PropertyEditor;
import javax.servlet.jsp.JspException;

public abstract interface EditorAwareTag
{
  public abstract PropertyEditor getEditor()
    throws JspException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\EditorAwareTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */