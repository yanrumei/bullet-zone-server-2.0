package org.springframework.web.servlet.resource;

import org.springframework.core.io.Resource;

public abstract interface VersionStrategy
  extends VersionPathStrategy
{
  public abstract String getResourceVersion(Resource paramResource);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\resource\VersionStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */