package org.springframework.boot.autoconfigure.jms.artemis;

import org.apache.activemq.artemis.core.config.Configuration;

public abstract interface ArtemisConfigurationCustomizer
{
  public abstract void customize(Configuration paramConfiguration);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jms\artemis\ArtemisConfigurationCustomizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */