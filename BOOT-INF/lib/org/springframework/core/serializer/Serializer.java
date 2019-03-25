package org.springframework.core.serializer;

import java.io.IOException;
import java.io.OutputStream;

public abstract interface Serializer<T>
{
  public abstract void serialize(T paramT, OutputStream paramOutputStream)
    throws IOException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\serializer\Serializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */