package org.apache.catalina.valves.rewrite;

public abstract interface RewriteMap
{
  public abstract String setParameters(String paramString);
  
  public abstract String lookup(String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\valves\rewrite\RewriteMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */