package org.springframework.core.io;

public abstract interface ResourceLoader
{
  public static final String CLASSPATH_URL_PREFIX = "classpath:";
  
  public abstract Resource getResource(String paramString);
  
  public abstract ClassLoader getClassLoader();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\io\ResourceLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */