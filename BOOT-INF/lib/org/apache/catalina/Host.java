package org.apache.catalina;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.regex.Pattern;

public abstract interface Host
  extends Container
{
  public static final String ADD_ALIAS_EVENT = "addAlias";
  public static final String REMOVE_ALIAS_EVENT = "removeAlias";
  
  public abstract String getXmlBase();
  
  public abstract void setXmlBase(String paramString);
  
  public abstract File getConfigBaseFile();
  
  public abstract String getAppBase();
  
  public abstract File getAppBaseFile();
  
  public abstract void setAppBase(String paramString);
  
  public abstract boolean getAutoDeploy();
  
  public abstract void setAutoDeploy(boolean paramBoolean);
  
  public abstract String getConfigClass();
  
  public abstract void setConfigClass(String paramString);
  
  public abstract boolean getDeployOnStartup();
  
  public abstract void setDeployOnStartup(boolean paramBoolean);
  
  public abstract String getDeployIgnore();
  
  public abstract Pattern getDeployIgnorePattern();
  
  public abstract void setDeployIgnore(String paramString);
  
  public abstract ExecutorService getStartStopExecutor();
  
  public abstract boolean getCreateDirs();
  
  public abstract void setCreateDirs(boolean paramBoolean);
  
  public abstract boolean getUndeployOldVersions();
  
  public abstract void setUndeployOldVersions(boolean paramBoolean);
  
  public abstract void addAlias(String paramString);
  
  public abstract String[] findAliases();
  
  public abstract void removeAlias(String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\Host.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */