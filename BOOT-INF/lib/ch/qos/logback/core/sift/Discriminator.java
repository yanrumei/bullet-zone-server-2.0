package ch.qos.logback.core.sift;

import ch.qos.logback.core.spi.LifeCycle;

public abstract interface Discriminator<E>
  extends LifeCycle
{
  public abstract String getDiscriminatingValue(E paramE);
  
  public abstract String getKey();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\sift\Discriminator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */