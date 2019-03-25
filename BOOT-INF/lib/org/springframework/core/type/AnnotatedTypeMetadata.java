package org.springframework.core.type;

import java.util.Map;
import org.springframework.util.MultiValueMap;

public abstract interface AnnotatedTypeMetadata
{
  public abstract boolean isAnnotated(String paramString);
  
  public abstract Map<String, Object> getAnnotationAttributes(String paramString);
  
  public abstract Map<String, Object> getAnnotationAttributes(String paramString, boolean paramBoolean);
  
  public abstract MultiValueMap<String, Object> getAllAnnotationAttributes(String paramString);
  
  public abstract MultiValueMap<String, Object> getAllAnnotationAttributes(String paramString, boolean paramBoolean);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\type\AnnotatedTypeMetadata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */