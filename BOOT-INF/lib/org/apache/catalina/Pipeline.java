package org.apache.catalina;

import java.util.Set;

public abstract interface Pipeline
{
  public abstract Valve getBasic();
  
  public abstract void setBasic(Valve paramValve);
  
  public abstract void addValve(Valve paramValve);
  
  public abstract Valve[] getValves();
  
  public abstract void removeValve(Valve paramValve);
  
  public abstract Valve getFirst();
  
  public abstract boolean isAsyncSupported();
  
  public abstract Container getContainer();
  
  public abstract void setContainer(Container paramContainer);
  
  public abstract void findNonAsyncValves(Set<String> paramSet);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\Pipeline.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */