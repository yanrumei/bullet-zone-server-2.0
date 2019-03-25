package ch.qos.logback.core.pattern;

import ch.qos.logback.core.Context;

public abstract interface PostCompileProcessor<E>
{
  public abstract void process(Context paramContext, Converter<E> paramConverter);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\pattern\PostCompileProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */