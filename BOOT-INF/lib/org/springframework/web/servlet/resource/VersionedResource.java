package org.springframework.web.servlet.resource;

import org.springframework.core.io.Resource;

public abstract interface VersionedResource
  extends Resource
{
  public abstract String getVersion();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\resource\VersionedResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */