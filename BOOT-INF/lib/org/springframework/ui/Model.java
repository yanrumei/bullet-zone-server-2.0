package org.springframework.ui;

import java.util.Collection;
import java.util.Map;

public abstract interface Model
{
  public abstract Model addAttribute(String paramString, Object paramObject);
  
  public abstract Model addAttribute(Object paramObject);
  
  public abstract Model addAllAttributes(Collection<?> paramCollection);
  
  public abstract Model addAllAttributes(Map<String, ?> paramMap);
  
  public abstract Model mergeAttributes(Map<String, ?> paramMap);
  
  public abstract boolean containsAttribute(String paramString);
  
  public abstract Map<String, Object> asMap();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframewor\\ui\Model.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */