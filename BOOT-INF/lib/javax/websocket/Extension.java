package javax.websocket;

import java.util.List;

public abstract interface Extension
{
  public abstract String getName();
  
  public abstract List<Parameter> getParameters();
  
  public static abstract interface Parameter
  {
    public abstract String getName();
    
    public abstract String getValue();
  }
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\javax\websocket\Extension.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */