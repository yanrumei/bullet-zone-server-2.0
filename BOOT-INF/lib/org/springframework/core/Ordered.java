package org.springframework.core;

public abstract interface Ordered
{
  public static final int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;
  public static final int LOWEST_PRECEDENCE = Integer.MAX_VALUE;
  
  public abstract int getOrder();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\Ordered.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */