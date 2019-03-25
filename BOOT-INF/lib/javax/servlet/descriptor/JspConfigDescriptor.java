package javax.servlet.descriptor;

import java.util.Collection;

public abstract interface JspConfigDescriptor
{
  public abstract Collection<TaglibDescriptor> getTaglibs();
  
  public abstract Collection<JspPropertyGroupDescriptor> getJspPropertyGroups();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\descriptor\JspConfigDescriptor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */