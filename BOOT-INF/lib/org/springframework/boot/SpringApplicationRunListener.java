package org.springframework.boot;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public abstract interface SpringApplicationRunListener
{
  public abstract void starting();
  
  public abstract void environmentPrepared(ConfigurableEnvironment paramConfigurableEnvironment);
  
  public abstract void contextPrepared(ConfigurableApplicationContext paramConfigurableApplicationContext);
  
  public abstract void contextLoaded(ConfigurableApplicationContext paramConfigurableApplicationContext);
  
  public abstract void finished(ConfigurableApplicationContext paramConfigurableApplicationContext, Throwable paramThrowable);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\SpringApplicationRunListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */