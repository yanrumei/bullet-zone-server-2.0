package org.apache.catalina;

public abstract interface CredentialHandler
{
  public abstract boolean matches(String paramString1, String paramString2);
  
  public abstract String mutate(String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\CredentialHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */