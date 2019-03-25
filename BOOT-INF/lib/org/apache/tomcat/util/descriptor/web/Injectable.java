package org.apache.tomcat.util.descriptor.web;

import java.util.List;

public abstract interface Injectable
{
  public abstract String getName();
  
  public abstract void addInjectionTarget(String paramString1, String paramString2);
  
  public abstract List<InjectionTarget> getInjectionTargets();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\Injectable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */