package org.springframework.cglib.proxy;

public abstract interface Factory
{
  public abstract Object newInstance(Callback paramCallback);
  
  public abstract Object newInstance(Callback[] paramArrayOfCallback);
  
  public abstract Object newInstance(Class[] paramArrayOfClass, Object[] paramArrayOfObject, Callback[] paramArrayOfCallback);
  
  public abstract Callback getCallback(int paramInt);
  
  public abstract void setCallback(int paramInt, Callback paramCallback);
  
  public abstract void setCallbacks(Callback[] paramArrayOfCallback);
  
  public abstract Callback[] getCallbacks();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\proxy\Factory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */