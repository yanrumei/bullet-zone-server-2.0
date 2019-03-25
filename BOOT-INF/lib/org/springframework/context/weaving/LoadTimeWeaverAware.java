package org.springframework.context.weaving;

import org.springframework.beans.factory.Aware;
import org.springframework.instrument.classloading.LoadTimeWeaver;

public abstract interface LoadTimeWeaverAware
  extends Aware
{
  public abstract void setLoadTimeWeaver(LoadTimeWeaver paramLoadTimeWeaver);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\weaving\LoadTimeWeaverAware.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */