package org.springframework.cglib.core;

import org.springframework.asm.Label;

public abstract interface ObjectSwitchCallback
{
  public abstract void processCase(Object paramObject, Label paramLabel)
    throws Exception;
  
  public abstract void processDefault()
    throws Exception;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\core\ObjectSwitchCallback.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */