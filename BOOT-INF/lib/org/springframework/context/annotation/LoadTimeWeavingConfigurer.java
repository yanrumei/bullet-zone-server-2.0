package org.springframework.context.annotation;

import org.springframework.instrument.classloading.LoadTimeWeaver;

public abstract interface LoadTimeWeavingConfigurer
{
  public abstract LoadTimeWeaver getLoadTimeWeaver();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\annotation\LoadTimeWeavingConfigurer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */