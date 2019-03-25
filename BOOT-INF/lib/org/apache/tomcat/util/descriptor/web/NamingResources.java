package org.apache.tomcat.util.descriptor.web;

public abstract interface NamingResources
{
  public abstract void addEnvironment(ContextEnvironment paramContextEnvironment);
  
  public abstract void removeEnvironment(String paramString);
  
  public abstract void addResource(ContextResource paramContextResource);
  
  public abstract void removeResource(String paramString);
  
  public abstract void addResourceLink(ContextResourceLink paramContextResourceLink);
  
  public abstract void removeResourceLink(String paramString);
  
  public abstract Object getContainer();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\NamingResources.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */