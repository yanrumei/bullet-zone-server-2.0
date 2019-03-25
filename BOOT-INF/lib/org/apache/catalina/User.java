package org.apache.catalina;

import java.security.Principal;
import java.util.Iterator;

public abstract interface User
  extends Principal
{
  public abstract String getFullName();
  
  public abstract void setFullName(String paramString);
  
  public abstract Iterator<Group> getGroups();
  
  public abstract String getPassword();
  
  public abstract void setPassword(String paramString);
  
  public abstract Iterator<Role> getRoles();
  
  public abstract UserDatabase getUserDatabase();
  
  public abstract String getUsername();
  
  public abstract void setUsername(String paramString);
  
  public abstract void addGroup(Group paramGroup);
  
  public abstract void addRole(Role paramRole);
  
  public abstract boolean isInGroup(Group paramGroup);
  
  public abstract boolean isInRole(Role paramRole);
  
  public abstract void removeGroup(Group paramGroup);
  
  public abstract void removeGroups();
  
  public abstract void removeRole(Role paramRole);
  
  public abstract void removeRoles();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\User.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */