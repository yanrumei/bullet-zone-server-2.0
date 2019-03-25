package org.springframework.boot.autoconfigure.template;

import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

public abstract interface TemplateAvailabilityProvider
{
  public abstract boolean isTemplateAvailable(String paramString, Environment paramEnvironment, ClassLoader paramClassLoader, ResourceLoader paramResourceLoader);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\template\TemplateAvailabilityProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */