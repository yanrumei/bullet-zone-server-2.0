package org.springframework.web.servlet.view.freemarker;

import freemarker.ext.jsp.TaglibFactory;
import freemarker.template.Configuration;

public abstract interface FreeMarkerConfig
{
  public abstract Configuration getConfiguration();
  
  public abstract TaglibFactory getTaglibFactory();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\freemarker\FreeMarkerConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */