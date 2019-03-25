package ch.qos.logback.core.boolex;

import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.spi.LifeCycle;

public abstract interface EventEvaluator<E>
  extends ContextAware, LifeCycle
{
  public abstract boolean evaluate(E paramE)
    throws NullPointerException, EvaluationException;
  
  public abstract String getName();
  
  public abstract void setName(String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\boolex\EventEvaluator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */