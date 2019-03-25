package org.springframework.web.servlet.resource;

public abstract interface VersionPathStrategy
{
  public abstract String extractVersion(String paramString);
  
  public abstract String removeVersion(String paramString1, String paramString2);
  
  public abstract String addVersion(String paramString1, String paramString2);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\resource\VersionPathStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */