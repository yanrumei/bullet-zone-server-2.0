package ch.qos.logback.core.rolling.helper;

import ch.qos.logback.core.spi.ContextAware;
import java.util.Date;
import java.util.concurrent.Future;

public abstract interface ArchiveRemover
  extends ContextAware
{
  public abstract void clean(Date paramDate);
  
  public abstract void setMaxHistory(int paramInt);
  
  public abstract void setTotalSizeCap(long paramLong);
  
  public abstract Future<?> cleanAsynchronously(Date paramDate);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\rolling\helper\ArchiveRemover.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */