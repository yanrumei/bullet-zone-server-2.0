package org.springframework.cglib.core;

public abstract interface NamingPolicy
{
  public abstract String getClassName(String paramString1, String paramString2, Object paramObject, Predicate paramPredicate);
  
  public abstract boolean equals(Object paramObject);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\core\NamingPolicy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */