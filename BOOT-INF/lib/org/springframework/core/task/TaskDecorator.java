package org.springframework.core.task;

public abstract interface TaskDecorator
{
  public abstract Runnable decorate(Runnable paramRunnable);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\task\TaskDecorator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */