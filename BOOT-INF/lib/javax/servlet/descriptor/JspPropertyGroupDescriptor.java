package javax.servlet.descriptor;

import java.util.Collection;

public abstract interface JspPropertyGroupDescriptor
{
  public abstract Collection<String> getUrlPatterns();
  
  public abstract String getElIgnored();
  
  public abstract String getPageEncoding();
  
  public abstract String getScriptingInvalid();
  
  public abstract String getIsXml();
  
  public abstract Collection<String> getIncludePreludes();
  
  public abstract Collection<String> getIncludeCodas();
  
  public abstract String getDeferredSyntaxAllowedAsLiteral();
  
  public abstract String getTrimDirectiveWhitespaces();
  
  public abstract String getDefaultContentType();
  
  public abstract String getBuffer();
  
  public abstract String getErrorOnUndeclaredNamespace();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\descriptor\JspPropertyGroupDescriptor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */