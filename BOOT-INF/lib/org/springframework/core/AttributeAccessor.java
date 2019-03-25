package org.springframework.core;

public abstract interface AttributeAccessor
{
  public abstract void setAttribute(String paramString, Object paramObject);
  
  public abstract Object getAttribute(String paramString);
  
  public abstract Object removeAttribute(String paramString);
  
  public abstract boolean hasAttribute(String paramString);
  
  public abstract String[] attributeNames();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\AttributeAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */