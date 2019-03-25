package org.springframework.instrument.classloading.jboss;

import java.lang.instrument.ClassFileTransformer;

abstract interface JBossClassLoaderAdapter
{
  public abstract void addTransformer(ClassFileTransformer paramClassFileTransformer);
  
  public abstract ClassLoader getInstrumentableClassLoader();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\instrument\classloading\jboss\JBossClassLoaderAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */