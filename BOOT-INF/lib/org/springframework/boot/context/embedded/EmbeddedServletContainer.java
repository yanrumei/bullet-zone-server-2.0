package org.springframework.boot.context.embedded;

public abstract interface EmbeddedServletContainer
{
  public abstract void start()
    throws EmbeddedServletContainerException;
  
  public abstract void stop()
    throws EmbeddedServletContainerException;
  
  public abstract int getPort();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\EmbeddedServletContainer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */