package org.springframework.web.servlet.resource;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;

public abstract interface ResourceTransformer
{
  public abstract Resource transform(HttpServletRequest paramHttpServletRequest, Resource paramResource, ResourceTransformerChain paramResourceTransformerChain)
    throws IOException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\resource\ResourceTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */