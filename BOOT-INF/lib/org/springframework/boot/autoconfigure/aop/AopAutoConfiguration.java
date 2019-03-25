package org.springframework.boot.autoconfigure.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.Advice;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ConditionalOnClass({EnableAspectJAutoProxy.class, Aspect.class, Advice.class})
@ConditionalOnProperty(prefix="spring.aop", name={"auto"}, havingValue="true", matchIfMissing=true)
public class AopAutoConfiguration
{
  @Configuration
  @EnableAspectJAutoProxy(proxyTargetClass=true)
  @ConditionalOnProperty(prefix="spring.aop", name={"proxy-target-class"}, havingValue="true", matchIfMissing=false)
  public static class CglibAutoProxyConfiguration {}
  
  @Configuration
  @EnableAspectJAutoProxy(proxyTargetClass=false)
  @ConditionalOnProperty(prefix="spring.aop", name={"proxy-target-class"}, havingValue="false", matchIfMissing=true)
  public static class JdkDynamicAutoProxyConfiguration {}
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\aop\AopAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */