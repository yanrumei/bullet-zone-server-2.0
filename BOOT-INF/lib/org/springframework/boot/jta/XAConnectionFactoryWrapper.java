package org.springframework.boot.jta;

import javax.jms.ConnectionFactory;
import javax.jms.XAConnectionFactory;

public abstract interface XAConnectionFactoryWrapper
{
  public abstract ConnectionFactory wrapConnectionFactory(XAConnectionFactory paramXAConnectionFactory)
    throws Exception;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\jta\XAConnectionFactoryWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */