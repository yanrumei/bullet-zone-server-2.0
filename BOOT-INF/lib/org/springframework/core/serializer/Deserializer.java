package org.springframework.core.serializer;

import java.io.IOException;
import java.io.InputStream;

public abstract interface Deserializer<T>
{
  public abstract T deserialize(InputStream paramInputStream)
    throws IOException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\serializer\Deserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */