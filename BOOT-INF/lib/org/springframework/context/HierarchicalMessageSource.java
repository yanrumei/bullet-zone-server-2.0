package org.springframework.context;

public abstract interface HierarchicalMessageSource
  extends MessageSource
{
  public abstract void setParentMessageSource(MessageSource paramMessageSource);
  
  public abstract MessageSource getParentMessageSource();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\HierarchicalMessageSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */