package org.apache.catalina;

import java.beans.PropertyChangeListener;

public abstract interface Loader
{
  public abstract void backgroundProcess();
  
  public abstract ClassLoader getClassLoader();
  
  public abstract Context getContext();
  
  public abstract void setContext(Context paramContext);
  
  public abstract boolean getDelegate();
  
  public abstract void setDelegate(boolean paramBoolean);
  
  public abstract boolean getReloadable();
  
  public abstract void setReloadable(boolean paramBoolean);
  
  public abstract void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener);
  
  public abstract boolean modified();
  
  public abstract void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\Loader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */