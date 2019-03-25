package org.springframework.beans.factory.config;

import org.springframework.beans.factory.ObjectFactory;

public abstract interface Scope
{
  public abstract Object get(String paramString, ObjectFactory<?> paramObjectFactory);
  
  public abstract Object remove(String paramString);
  
  public abstract void registerDestructionCallback(String paramString, Runnable paramRunnable);
  
  public abstract Object resolveContextualObject(String paramString);
  
  public abstract String getConversationId();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\config\Scope.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */