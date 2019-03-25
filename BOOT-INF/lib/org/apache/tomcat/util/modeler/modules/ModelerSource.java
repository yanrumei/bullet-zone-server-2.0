package org.apache.tomcat.util.modeler.modules;

import java.util.List;
import javax.management.ObjectName;
import org.apache.tomcat.util.modeler.Registry;

public abstract class ModelerSource
{
  protected Object source;
  
  public abstract List<ObjectName> loadDescriptors(Registry paramRegistry, String paramString, Object paramObject)
    throws Exception;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\modeler\modules\ModelerSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */