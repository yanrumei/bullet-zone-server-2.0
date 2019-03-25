package org.springframework.web.servlet.resource;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;

public abstract interface ResourceResolverChain
{
  public abstract Resource resolveResource(HttpServletRequest paramHttpServletRequest, String paramString, List<? extends Resource> paramList);
  
  public abstract String resolveUrlPath(String paramString, List<? extends Resource> paramList);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\resource\ResourceResolverChain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */