package org.apache.tomcat.util.digester;

import java.nio.charset.Charset;

public abstract interface DocumentProperties
{
  public static abstract interface Charset
  {
    public abstract void setCharset(Charset paramCharset);
  }
  
  @Deprecated
  public static abstract interface Encoding
  {
    public abstract void setEncoding(String paramString);
  }
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\digester\DocumentProperties.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */