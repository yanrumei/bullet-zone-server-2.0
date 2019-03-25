package org.springframework.cglib.proxy;

import java.lang.reflect.Method;

public abstract interface CallbackFilter
{
  public abstract int accept(Method paramMethod);
  
  public abstract boolean equals(Object paramObject);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\proxy\CallbackFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */