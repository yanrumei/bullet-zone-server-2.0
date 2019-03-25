package org.springframework.cglib.transform.impl;

import org.springframework.asm.Type;

public abstract interface InterceptFieldFilter
{
  public abstract boolean acceptRead(Type paramType, String paramString);
  
  public abstract boolean acceptWrite(Type paramType, String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\transform\impl\InterceptFieldFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */