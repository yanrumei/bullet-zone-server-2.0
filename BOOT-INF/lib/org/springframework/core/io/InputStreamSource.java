package org.springframework.core.io;

import java.io.IOException;
import java.io.InputStream;

public abstract interface InputStreamSource
{
  public abstract InputStream getInputStream()
    throws IOException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\io\InputStreamSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */