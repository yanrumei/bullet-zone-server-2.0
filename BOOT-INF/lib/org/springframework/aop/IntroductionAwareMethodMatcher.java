package org.springframework.aop;

import java.lang.reflect.Method;

public abstract interface IntroductionAwareMethodMatcher
  extends MethodMatcher
{
  public abstract boolean matches(Method paramMethod, Class<?> paramClass, boolean paramBoolean);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\IntroductionAwareMethodMatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */