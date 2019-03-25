package org.apache.tomcat;

import java.lang.reflect.InvocationTargetException;
import javax.naming.NamingException;

public abstract interface InstanceManager
{
  public abstract Object newInstance(Class<?> paramClass)
    throws IllegalAccessException, InvocationTargetException, NamingException, InstantiationException, IllegalArgumentException, NoSuchMethodException, SecurityException;
  
  public abstract Object newInstance(String paramString)
    throws IllegalAccessException, InvocationTargetException, NamingException, InstantiationException, ClassNotFoundException, IllegalArgumentException, NoSuchMethodException, SecurityException;
  
  public abstract Object newInstance(String paramString, ClassLoader paramClassLoader)
    throws IllegalAccessException, InvocationTargetException, NamingException, InstantiationException, ClassNotFoundException, IllegalArgumentException, NoSuchMethodException, SecurityException;
  
  public abstract void newInstance(Object paramObject)
    throws IllegalAccessException, InvocationTargetException, NamingException;
  
  public abstract void destroyInstance(Object paramObject)
    throws IllegalAccessException, InvocationTargetException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomcat\InstanceManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */