package ch.qos.logback.core;

import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.spi.PropertyContainer;
import ch.qos.logback.core.status.StatusManager;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public abstract interface Context
  extends PropertyContainer
{
  public abstract StatusManager getStatusManager();
  
  public abstract Object getObject(String paramString);
  
  public abstract void putObject(String paramString, Object paramObject);
  
  public abstract String getProperty(String paramString);
  
  public abstract void putProperty(String paramString1, String paramString2);
  
  public abstract Map<String, String> getCopyOfPropertyMap();
  
  public abstract String getName();
  
  public abstract void setName(String paramString);
  
  public abstract long getBirthTime();
  
  public abstract Object getConfigurationLock();
  
  public abstract ScheduledExecutorService getScheduledExecutorService();
  
  /**
   * @deprecated
   */
  public abstract ExecutorService getExecutorService();
  
  public abstract void register(LifeCycle paramLifeCycle);
  
  public abstract void addScheduledFuture(ScheduledFuture<?> paramScheduledFuture);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\Context.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */