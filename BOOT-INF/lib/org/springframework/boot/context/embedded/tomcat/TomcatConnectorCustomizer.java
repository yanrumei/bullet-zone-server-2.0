package org.springframework.boot.context.embedded.tomcat;

import org.apache.catalina.connector.Connector;

public abstract interface TomcatConnectorCustomizer
{
  public abstract void customize(Connector paramConnector);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\tomcat\TomcatConnectorCustomizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */