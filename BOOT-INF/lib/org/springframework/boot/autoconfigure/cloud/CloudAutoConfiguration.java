package org.springframework.boot.autoconfigure.cloud;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.config.java.CloudScanConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"cloud"})
@AutoConfigureOrder(-2147483628)
@ConditionalOnClass({CloudScanConfiguration.class})
@ConditionalOnMissingBean({Cloud.class})
@ConditionalOnProperty(prefix="spring.cloud", name={"enabled"}, havingValue="true", matchIfMissing=true)
@Import({CloudScanConfiguration.class})
public class CloudAutoConfiguration
{
  public static final int ORDER = -2147483628;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\cloud\CloudAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */