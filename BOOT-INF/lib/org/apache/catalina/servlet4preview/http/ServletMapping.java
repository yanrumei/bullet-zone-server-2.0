package org.apache.catalina.servlet4preview.http;

public abstract interface ServletMapping
{
  public abstract String getMatchValue();
  
  public abstract String getPattern();
  
  public abstract MappingMatch getMappingMatch();
  
  public abstract String getServletName();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\servlet4preview\http\ServletMapping.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */