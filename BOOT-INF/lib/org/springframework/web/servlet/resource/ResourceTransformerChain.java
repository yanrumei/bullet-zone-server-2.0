package org.springframework.web.servlet.resource;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;

public abstract interface ResourceTransformerChain
{
  public abstract ResourceResolverChain getResolverChain();
  
  public abstract Resource transform(HttpServletRequest paramHttpServletRequest, Resource paramResource)
    throws IOException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\resource\ResourceTransformerChain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */