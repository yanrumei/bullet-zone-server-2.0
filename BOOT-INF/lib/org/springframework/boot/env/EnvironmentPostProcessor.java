package org.springframework.boot.env;

import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;

public abstract interface EnvironmentPostProcessor
{
  public abstract void postProcessEnvironment(ConfigurableEnvironment paramConfigurableEnvironment, SpringApplication paramSpringApplication);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\env\EnvironmentPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */