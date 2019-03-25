package org.apache.catalina;

import java.beans.PropertyChangeListener;
import java.io.File;
import javax.management.ObjectName;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.juli.logging.Log;

public abstract interface Container
  extends Lifecycle
{
  public static final String ADD_CHILD_EVENT = "addChild";
  public static final String ADD_VALVE_EVENT = "addValve";
  public static final String REMOVE_CHILD_EVENT = "removeChild";
  public static final String REMOVE_VALVE_EVENT = "removeValve";
  
  public abstract Log getLogger();
  
  public abstract String getLogName();
  
  public abstract ObjectName getObjectName();
  
  public abstract String getDomain();
  
  public abstract String getMBeanKeyProperties();
  
  public abstract Pipeline getPipeline();
  
  public abstract Cluster getCluster();
  
  public abstract void setCluster(Cluster paramCluster);
  
  public abstract int getBackgroundProcessorDelay();
  
  public abstract void setBackgroundProcessorDelay(int paramInt);
  
  public abstract String getName();
  
  public abstract void setName(String paramString);
  
  public abstract Container getParent();
  
  public abstract void setParent(Container paramContainer);
  
  public abstract ClassLoader getParentClassLoader();
  
  public abstract void setParentClassLoader(ClassLoader paramClassLoader);
  
  public abstract Realm getRealm();
  
  public abstract void setRealm(Realm paramRealm);
  
  public abstract void backgroundProcess();
  
  public abstract void addChild(Container paramContainer);
  
  public abstract void addContainerListener(ContainerListener paramContainerListener);
  
  public abstract void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener);
  
  public abstract Container findChild(String paramString);
  
  public abstract Container[] findChildren();
  
  public abstract ContainerListener[] findContainerListeners();
  
  public abstract void removeChild(Container paramContainer);
  
  public abstract void removeContainerListener(ContainerListener paramContainerListener);
  
  public abstract void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener);
  
  public abstract void fireContainerEvent(String paramString, Object paramObject);
  
  public abstract void logAccess(Request paramRequest, Response paramResponse, long paramLong, boolean paramBoolean);
  
  public abstract AccessLog getAccessLog();
  
  public abstract int getStartStopThreads();
  
  public abstract void setStartStopThreads(int paramInt);
  
  public abstract File getCatalinaBase();
  
  public abstract File getCatalinaHome();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\Container.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */