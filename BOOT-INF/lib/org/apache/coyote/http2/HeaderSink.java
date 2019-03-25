package org.apache.coyote.http2;

public class HeaderSink
  implements HpackDecoder.HeaderEmitter
{
  public void emitHeader(String name, String value) {}
  
  public void validateHeaders()
    throws StreamException
  {}
  
  public void setHeaderException(StreamException streamException) {}
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http2\HeaderSink.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */