package org.springframework.instrument.classloading;

import java.lang.instrument.ClassFileTransformer;

public abstract interface LoadTimeWeaver
{
  public abstract void addTransformer(ClassFileTransformer paramClassFileTransformer);
  
  public abstract ClassLoader getInstrumentableClassLoader();
  
  public abstract ClassLoader getThrowawayClassLoader();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\instrument\classloading\LoadTimeWeaver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */