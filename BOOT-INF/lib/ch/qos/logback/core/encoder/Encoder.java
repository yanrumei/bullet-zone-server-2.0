package ch.qos.logback.core.encoder;

import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.spi.LifeCycle;
import java.io.IOException;
import java.io.OutputStream;

public abstract interface Encoder<E>
  extends ContextAware, LifeCycle
{
  public abstract void init(OutputStream paramOutputStream)
    throws IOException;
  
  public abstract void doEncode(E paramE)
    throws IOException;
  
  public abstract void close()
    throws IOException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\encoder\Encoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */