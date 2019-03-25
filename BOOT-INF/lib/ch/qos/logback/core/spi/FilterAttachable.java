package ch.qos.logback.core.spi;

import ch.qos.logback.core.filter.Filter;
import java.util.List;

public abstract interface FilterAttachable<E>
{
  public abstract void addFilter(Filter<E> paramFilter);
  
  public abstract void clearAllFilters();
  
  public abstract List<Filter<E>> getCopyOfAttachedFiltersList();
  
  public abstract FilterReply getFilterChainDecision(E paramE);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\spi\FilterAttachable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */