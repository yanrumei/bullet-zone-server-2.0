package org.apache.tomcat.util.http;

import java.nio.charset.Charset;
import javax.servlet.http.Cookie;

public abstract interface CookieProcessor
{
  public abstract void parseCookieHeader(MimeHeaders paramMimeHeaders, ServerCookies paramServerCookies);
  
  public abstract String generateHeader(Cookie paramCookie);
  
  public abstract Charset getCharset();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\CookieProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */