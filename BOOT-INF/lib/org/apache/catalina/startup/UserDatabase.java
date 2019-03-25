package org.apache.catalina.startup;

import java.util.Enumeration;

public abstract interface UserDatabase
{
  public abstract UserConfig getUserConfig();
  
  public abstract void setUserConfig(UserConfig paramUserConfig);
  
  public abstract String getHome(String paramString);
  
  public abstract Enumeration<String> getUsers();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\UserDatabase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */