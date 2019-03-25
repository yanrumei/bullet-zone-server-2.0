package org.springframework.util;

import java.util.Comparator;
import java.util.Map;

public abstract interface PathMatcher
{
  public abstract boolean isPattern(String paramString);
  
  public abstract boolean match(String paramString1, String paramString2);
  
  public abstract boolean matchStart(String paramString1, String paramString2);
  
  public abstract String extractPathWithinPattern(String paramString1, String paramString2);
  
  public abstract Map<String, String> extractUriTemplateVariables(String paramString1, String paramString2);
  
  public abstract Comparator<String> getPatternComparator(String paramString);
  
  public abstract String combine(String paramString1, String paramString2);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\PathMatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */