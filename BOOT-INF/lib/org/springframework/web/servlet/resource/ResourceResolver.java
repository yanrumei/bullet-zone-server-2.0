package org.springframework.web.servlet.resource;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;

public abstract interface ResourceResolver
{
  public abstract Resource resolveResource(HttpServletRequest paramHttpServletRequest, String paramString, List<? extends Resource> paramList, ResourceResolverChain paramResourceResolverChain);
  
  public abstract String resolveUrlPath(String paramString, List<? extends Resource> paramList, ResourceResolverChain paramResourceResolverChain);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\resource\ResourceResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */