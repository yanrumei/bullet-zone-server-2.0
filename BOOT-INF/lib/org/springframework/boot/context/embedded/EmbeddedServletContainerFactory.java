package org.springframework.boot.context.embedded;

import org.springframework.boot.web.servlet.ServletContextInitializer;

public abstract interface EmbeddedServletContainerFactory
{
  public abstract EmbeddedServletContainer getEmbeddedServletContainer(ServletContextInitializer... paramVarArgs);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\EmbeddedServletContainerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */