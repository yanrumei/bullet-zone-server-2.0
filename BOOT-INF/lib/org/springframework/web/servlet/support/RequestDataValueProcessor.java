package org.springframework.web.servlet.support;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public abstract interface RequestDataValueProcessor
{
  public abstract String processAction(HttpServletRequest paramHttpServletRequest, String paramString1, String paramString2);
  
  public abstract String processFormFieldValue(HttpServletRequest paramHttpServletRequest, String paramString1, String paramString2, String paramString3);
  
  public abstract Map<String, String> getExtraHiddenFields(HttpServletRequest paramHttpServletRequest);
  
  public abstract String processUrl(HttpServletRequest paramHttpServletRequest, String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\support\RequestDataValueProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */