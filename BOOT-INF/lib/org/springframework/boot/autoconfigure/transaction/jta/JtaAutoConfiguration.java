package org.springframework.boot.autoconfigure.transaction.jta;

import javax.transaction.Transaction;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@ConditionalOnClass({Transaction.class})
@ConditionalOnProperty(prefix="spring.jta", value={"enabled"}, matchIfMissing=true)
@AutoConfigureBefore({XADataSourceAutoConfiguration.class, ActiveMQAutoConfiguration.class, ArtemisAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@Import({JndiJtaConfiguration.class, BitronixJtaConfiguration.class, AtomikosJtaConfiguration.class, NarayanaJtaConfiguration.class})
@EnableConfigurationProperties({JtaProperties.class})
public class JtaAutoConfiguration {}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\transaction\jta\JtaAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */