package org.springframework.boot.context.embedded.tomcat;

import org.apache.catalina.Context;

public abstract interface TomcatContextCustomizer
{
  public abstract void customize(Context paramContext);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\tomcat\TomcatContextCustomizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */