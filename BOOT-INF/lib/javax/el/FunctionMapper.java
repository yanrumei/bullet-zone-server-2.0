package javax.el;

import java.lang.reflect.Method;

public abstract class FunctionMapper
{
  public abstract Method resolveFunction(String paramString1, String paramString2);
  
  public void mapFunction(String prefix, String localName, Method method) {}
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\javax\el\FunctionMapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */