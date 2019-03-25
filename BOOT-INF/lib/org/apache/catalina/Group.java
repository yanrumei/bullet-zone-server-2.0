package org.apache.catalina;

import java.security.Principal;
import java.util.Iterator;

public abstract interface Group
  extends Principal
{
  public abstract String getDescription();
  
  public abstract void setDescription(String paramString);
  
  public abstract String getGroupname();
  
  public abstract void setGroupname(String paramString);
  
  public abstract Iterator<Role> getRoles();
  
  public abstract UserDatabase getUserDatabase();
  
  public abstract Iterator<User> getUsers();
  
  public abstract void addRole(Role paramRole);
  
  public abstract boolean isInRole(Role paramRole);
  
  public abstract void removeRole(Role paramRole);
  
  public abstract void removeRoles();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\Group.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */