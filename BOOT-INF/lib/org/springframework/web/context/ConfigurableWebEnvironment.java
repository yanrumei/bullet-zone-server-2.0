package org.springframework.web.context;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import org.springframework.core.env.ConfigurableEnvironment;

public abstract interface ConfigurableWebEnvironment
  extends ConfigurableEnvironment
{
  public abstract void initPropertySources(ServletContext paramServletContext, ServletConfig paramServletConfig);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\context\ConfigurableWebEnvironment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */