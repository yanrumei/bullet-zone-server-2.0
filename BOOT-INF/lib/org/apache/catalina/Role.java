package org.apache.catalina;

import java.security.Principal;

public abstract interface Role
  extends Principal
{
  public abstract String getDescription();
  
  public abstract void setDescription(String paramString);
  
  public abstract String getRolename();
  
  public abstract void setRolename(String paramString);
  
  public abstract UserDatabase getUserDatabase();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\Role.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */