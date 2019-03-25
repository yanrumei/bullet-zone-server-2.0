package javax.validation;

import java.lang.annotation.ElementType;

public abstract interface TraversableResolver
{
  public abstract boolean isReachable(Object paramObject, Path.Node paramNode, Class<?> paramClass, Path paramPath, ElementType paramElementType);
  
  public abstract boolean isCascadable(Object paramObject, Path.Node paramNode, Class<?> paramClass, Path paramPath, ElementType paramElementType);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\TraversableResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */