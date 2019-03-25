package org.apache.coyote.http11;

import org.apache.coyote.Response;

public abstract interface OutputFilter
  extends HttpOutputBuffer
{
  public abstract void setResponse(Response paramResponse);
  
  public abstract void recycle();
  
  public abstract void setBuffer(HttpOutputBuffer paramHttpOutputBuffer);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http11\OutputFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */