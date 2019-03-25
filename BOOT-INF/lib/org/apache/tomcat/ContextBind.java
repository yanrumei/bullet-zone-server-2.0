package org.apache.tomcat;

public abstract interface ContextBind
{
  public abstract ClassLoader bind(boolean paramBoolean, ClassLoader paramClassLoader);
  
  public abstract void unbind(boolean paramBoolean, ClassLoader paramClassLoader);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomcat\ContextBind.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */