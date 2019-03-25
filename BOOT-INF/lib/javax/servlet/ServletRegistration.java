package javax.servlet;

import java.util.Collection;
import java.util.Set;

public abstract interface ServletRegistration
  extends Registration
{
  public abstract Set<String> addMapping(String... paramVarArgs);
  
  public abstract Collection<String> getMappings();
  
  public abstract String getRunAsRole();
  
  public static abstract interface Dynamic
    extends ServletRegistration, Registration.Dynamic
  {
    public abstract void setLoadOnStartup(int paramInt);
    
    public abstract void setMultipartConfig(MultipartConfigElement paramMultipartConfigElement);
    
    public abstract void setRunAsRole(String paramString);
    
    public abstract Set<String> setServletSecurity(ServletSecurityElement paramServletSecurityElement);
  }
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\ServletRegistration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */