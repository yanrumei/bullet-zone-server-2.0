package org.apache.tomcat.util.modeler;

import java.util.List;
import javax.management.ObjectName;

public abstract interface RegistryMBean
{
  public abstract void invoke(List<ObjectName> paramList, String paramString, boolean paramBoolean)
    throws Exception;
  
  public abstract void registerComponent(Object paramObject, String paramString1, String paramString2)
    throws Exception;
  
  public abstract void unregisterComponent(String paramString);
  
  public abstract int getId(String paramString1, String paramString2);
  
  public abstract void stop();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\modeler\RegistryMBean.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */