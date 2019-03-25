package org.springframework.web.context;

import javax.servlet.ServletConfig;
import org.springframework.beans.factory.Aware;

public abstract interface ServletConfigAware
  extends Aware
{
  public abstract void setServletConfig(ServletConfig paramServletConfig);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\context\ServletConfigAware.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */