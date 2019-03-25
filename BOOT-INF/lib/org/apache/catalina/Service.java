package org.apache.catalina;

import org.apache.catalina.connector.Connector;
import org.apache.catalina.mapper.Mapper;

public abstract interface Service
  extends Lifecycle
{
  public abstract Engine getContainer();
  
  public abstract void setContainer(Engine paramEngine);
  
  public abstract String getName();
  
  public abstract void setName(String paramString);
  
  public abstract Server getServer();
  
  public abstract void setServer(Server paramServer);
  
  public abstract ClassLoader getParentClassLoader();
  
  public abstract void setParentClassLoader(ClassLoader paramClassLoader);
  
  public abstract String getDomain();
  
  public abstract void addConnector(Connector paramConnector);
  
  public abstract Connector[] findConnectors();
  
  public abstract void removeConnector(Connector paramConnector);
  
  public abstract void addExecutor(Executor paramExecutor);
  
  public abstract Executor[] findExecutors();
  
  public abstract Executor getExecutor(String paramString);
  
  public abstract void removeExecutor(Executor paramExecutor);
  
  public abstract Mapper getMapper();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\Service.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */