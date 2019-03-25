package org.apache.catalina.servlet4preview.http;

import java.util.Set;

public abstract interface PushBuilder
{
  public abstract PushBuilder method(String paramString);
  
  public abstract PushBuilder queryString(String paramString);
  
  public abstract PushBuilder sessionId(String paramString);
  
  public abstract PushBuilder setHeader(String paramString1, String paramString2);
  
  public abstract PushBuilder addHeader(String paramString1, String paramString2);
  
  public abstract PushBuilder removeHeader(String paramString);
  
  public abstract PushBuilder path(String paramString);
  
  public abstract void push();
  
  public abstract String getMethod();
  
  public abstract String getQueryString();
  
  public abstract String getSessionId();
  
  public abstract Set<String> getHeaderNames();
  
  public abstract String getHeader(String paramString);
  
  public abstract String getPath();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\servlet4preview\http\PushBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */