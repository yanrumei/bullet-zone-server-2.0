package org.springframework.context;

import org.springframework.beans.factory.Aware;
import org.springframework.core.env.Environment;

public abstract interface EnvironmentAware
  extends Aware
{
  public abstract void setEnvironment(Environment paramEnvironment);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\EnvironmentAware.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */