package org.apache.coyote;

import org.apache.tomcat.util.net.SocketEvent;

public abstract interface Adapter
{
  public abstract void service(Request paramRequest, Response paramResponse)
    throws Exception;
  
  public abstract boolean prepare(Request paramRequest, Response paramResponse)
    throws Exception;
  
  public abstract boolean asyncDispatch(Request paramRequest, Response paramResponse, SocketEvent paramSocketEvent)
    throws Exception;
  
  public abstract void log(Request paramRequest, Response paramResponse, long paramLong);
  
  public abstract void checkRecycled(Request paramRequest, Response paramResponse);
  
  public abstract String getDomain();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\Adapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */