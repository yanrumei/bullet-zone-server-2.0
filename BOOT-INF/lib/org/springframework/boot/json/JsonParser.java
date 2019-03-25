package org.springframework.boot.json;

import java.util.List;
import java.util.Map;

public abstract interface JsonParser
{
  public abstract Map<String, Object> parseMap(String paramString);
  
  public abstract List<Object> parseList(String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\json\JsonParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */