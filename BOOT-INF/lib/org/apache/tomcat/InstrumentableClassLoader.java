package org.apache.tomcat;

import java.lang.instrument.ClassFileTransformer;

public abstract interface InstrumentableClassLoader
{
  public abstract void addTransformer(ClassFileTransformer paramClassFileTransformer);
  
  public abstract void removeTransformer(ClassFileTransformer paramClassFileTransformer);
  
  public abstract ClassLoader copyWithoutTransformers();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomcat\InstrumentableClassLoader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */