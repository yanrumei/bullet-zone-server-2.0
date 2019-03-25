package org.springframework.core;

@Deprecated
public abstract interface ControlFlow
{
  public abstract boolean under(Class<?> paramClass);
  
  public abstract boolean under(Class<?> paramClass, String paramString);
  
  public abstract boolean underToken(String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\ControlFlow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */